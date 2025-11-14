"""
Unit Tests for Bias Detection Service
Run with: pytest test_main.py -v
"""

import pytest
from fastapi.testclient import TestClient
from main import app, calculate_demographic_parity, calculate_equal_opportunity, classify_bias_severity, BiasMetrics
import pandas as pd
import numpy as np
import io

client = TestClient(app)


class TestHealthEndpoints:
    """Test health check endpoints"""
    
    def test_root_endpoint(self):
        response = client.get("/")
        assert response.status_code == 200
        assert response.json()["service"] == "Bias Detection Service"
        assert response.json()["status"] == "running"
    
    def test_health_check(self):
        response = client.get("/health")
        assert response.status_code == 200
        assert response.json()["status"] == "healthy"
        assert "timestamp" in response.json()


class TestBiasMetrics:
    """Test fairness metric calculations"""
    
    def test_demographic_parity_no_bias(self):
        """Test demographic parity with no bias"""
        y_true = np.array([1, 1, 0, 0, 1, 1, 0, 0])
        y_pred = np.array([1, 1, 0, 0, 1, 1, 0, 0])
        sensitive = np.array(['A', 'A', 'A', 'A', 'B', 'B', 'B', 'B'])
        
        dpd = calculate_demographic_parity(y_true, y_pred, sensitive)
        assert abs(dpd) < 0.01  # Should be very small
    
    def test_demographic_parity_with_bias(self):
        """Test demographic parity with clear bias"""
        y_true = np.array([1, 1, 1, 1, 1, 1, 1, 1])
        y_pred = np.array([1, 1, 1, 1, 0, 0, 0, 0])  # Group A gets 100%, Group B gets 0%
        sensitive = np.array(['A', 'A', 'A', 'A', 'B', 'B', 'B', 'B'])
        
        dpd = calculate_demographic_parity(y_true, y_pred, sensitive)
        assert dpd == 1.0  # Maximum difference
    
    def test_equal_opportunity(self):
        """Test equal opportunity difference"""
        y_true = np.array([1, 1, 1, 1, 1, 1, 1, 1])
        y_pred = np.array([1, 1, 1, 1, 0, 0, 0, 0])
        sensitive = np.array(['A', 'A', 'A', 'A', 'B', 'B', 'B', 'B'])
        
        eod = calculate_equal_opportunity(y_true, y_pred, sensitive)
        assert eod == 1.0


class TestBiasClassification:
    """Test bias severity classification"""
    
    def test_no_bias_classification(self):
        metrics = BiasMetrics(
            demographic_parity_difference=0.02,
            equal_opportunity_difference=0.03,
            disparate_impact_ratio=0.98
        )
        classification = classify_bias_severity(metrics, 0.8)
        assert classification == "NO_BIAS"
    
    def test_low_bias_classification(self):
        metrics = BiasMetrics(
            demographic_parity_difference=0.08,
            equal_opportunity_difference=0.06,
            disparate_impact_ratio=0.93
        )
        classification = classify_bias_severity(metrics, 0.8)
        assert classification == "LOW"
    
    def test_moderate_bias_classification(self):
        metrics = BiasMetrics(
            demographic_parity_difference=0.15,
            equal_opportunity_difference=0.12,
            disparate_impact_ratio=0.85
        )
        classification = classify_bias_severity(metrics, 0.8)
        assert classification == "MODERATE"
    
    def test_high_bias_classification(self):
        metrics = BiasMetrics(
            demographic_parity_difference=0.25,
            equal_opportunity_difference=0.22,
            disparate_impact_ratio=0.70
        )
        classification = classify_bias_severity(metrics, 0.8)
        assert classification == "HIGH"
    
    def test_critical_bias_classification(self):
        metrics = BiasMetrics(
            demographic_parity_difference=0.40,
            equal_opportunity_difference=0.35,
            disparate_impact_ratio=0.50
        )
        classification = classify_bias_severity(metrics, 0.8)
        assert classification == "CRITICAL"


class TestBiasAnalysisEndpoint:
    """Test the main bias analysis endpoint"""
    
    def create_test_csv(self, bias_level="none"):
        """Helper to create test CSV data"""
        if bias_level == "none":
            # No bias: equal performance
            data = {
                'y_true': [1, 0, 1, 0, 1, 0, 1, 0] * 10,
                'y_pred': [1, 0, 1, 0, 1, 0, 1, 0] * 10,
                'gender': ['male', 'male', 'male', 'male', 
                          'female', 'female', 'female', 'female'] * 10
            }
        else:
            # Bias: different performance
            data = {
                'y_true': [1, 1, 1, 1, 1, 1, 1, 1] * 10,
                'y_pred': ([1, 1, 1, 1] * 10) + ([0, 0, 0, 0] * 10),
                'gender': (['male'] * 40) + (['female'] * 40)
            }
        
        df = pd.DataFrame(data)
        return df.to_csv(index=False)
    
    def test_successful_bias_analysis(self):
        """Test successful bias analysis with valid data"""
        csv_content = self.create_test_csv("none")
        
        files = {
            "file": ("test.csv", io.StringIO(csv_content), "text/csv")
        }
        data = {
            "model_id": "test_model_123",
            "protected_attribute": "gender",
            "favorable_outcome": "1",
            "threshold": "0.8"
        }
        
        response = client.post(
            "/api/bias-analysis/analyze",
            files=files,
            data=data
        )
        
        assert response.status_code == 200
        result = response.json()
        
        # Check response structure
        assert "model_id" in result
        assert result["model_id"] == "test_model_123"
        assert "metrics" in result
        assert "classification" in result
        assert "recommendations" in result
        assert "groups_analysis" in result
        
        # Check metrics
        assert "demographic_parity_difference" in result["metrics"]
        assert "equal_opportunity_difference" in result["metrics"]
        assert "disparate_impact_ratio" in result["metrics"]
        
        # Check classification
        assert result["classification"] in ["NO_BIAS", "LOW", "MODERATE", "HIGH", "CRITICAL"]
    
    def test_missing_file(self):
        """Test error handling when file is missing"""
        data = {
            "model_id": "test_model",
            "protected_attribute": "gender"
        }
        
        response = client.post(
            "/api/bias-analysis/analyze",
            data=data
        )
        
        assert response.status_code == 422  # Validation error
    
    def test_missing_columns(self):
        """Test error handling when CSV is missing required columns"""
        # CSV without protected attribute column
        csv_content = "y_true,y_pred\n1,0\n0,1\n"
        
        files = {
            "file": ("test.csv", io.StringIO(csv_content), "text/csv")
        }
        data = {
            "model_id": "test_model",
            "protected_attribute": "gender"  # This column doesn't exist
        }
        
        response = client.post(
            "/api/bias-analysis/analyze",
            files=files,
            data=data
        )
        
        assert response.status_code == 400
        assert "Missing required columns" in response.json()["detail"]
    
    def test_invalid_csv_format(self):
        """Test error handling with invalid CSV format"""
        invalid_csv = "not,a,valid\ncsv,format"
        
        files = {
            "file": ("test.csv", io.StringIO(invalid_csv), "text/csv")
        }
        data = {
            "model_id": "test_model",
            "protected_attribute": "gender"
        }
        
        response = client.post(
            "/api/bias-analysis/analyze",
            files=files,
            data=data
        )
        
        assert response.status_code == 400
    
    def test_empty_csv(self):
        """Test error handling with empty CSV"""
        csv_content = "y_true,y_pred,gender\n"
        
        files = {
            "file": ("test.csv", io.StringIO(csv_content), "text/csv")
        }
        data = {
            "model_id": "test_model",
            "protected_attribute": "gender"
        }
        
        response = client.post(
            "/api/bias-analysis/analyze",
            files=files,
            data=data
        )
        
        assert response.status_code == 400
        assert "empty" in response.json()["detail"].lower()
    
    def test_biased_dataset(self):
        """Test detection of biased dataset"""
        csv_content = self.create_test_csv("biased")
        
        files = {
            "file": ("test.csv", io.StringIO(csv_content), "text/csv")
        }
        data = {
            "model_id": "biased_model",
            "protected_attribute": "gender"
        }
        
        response = client.post(
            "/api/bias-analysis/analyze",
            files=files,
            data=data
        )
        
        assert response.status_code == 200
        result = response.json()
        
        # Should detect bias
        assert result["classification"] in ["MODERATE", "HIGH", "CRITICAL"]
        assert len(result["groups_analysis"]) == 2  # Two groups


class TestDataGeneration:
    """Test data generation utilities"""
    
    def test_generate_balanced_data(self):
        """Test generating balanced dataset"""
        from generate_test_data import generate_biased_data
        
        df = generate_biased_data(
            n_samples=100,
            bias_level="none",
            protected_attr_name="test_attr"
        )
        
        assert len(df) == 100
        assert "y_true" in df.columns
        assert "y_pred" in df.columns
        assert "test_attr" in df.columns
    
    def test_generate_biased_data(self):
        """Test generating biased dataset"""
        from generate_test_data import generate_biased_data
        
        df = generate_biased_data(
            n_samples=100,
            bias_level="high",
            protected_attr_name="test_attr"
        )
        
        assert len(df) == 100
        # Should have different accuracy for groups


if __name__ == "__main__":
    pytest.main([__file__, "-v", "--tb=short"])

