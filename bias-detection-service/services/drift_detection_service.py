"""
AI Governance Platform - Drift Detection Service
Detects data drift and model performance degradation over time
"""

import pandas as pd
import numpy as np
from scipy import stats
from typing import Dict, List, Optional
import logging
from datetime import datetime

logger = logging.getLogger(__name__)


class DriftDetectionService:
    """
    Service for detecting drift in data and model predictions
    """
    
    def __init__(self):
        self.drift_threshold = 0.05  # p-value threshold for KS test
        self.psi_thresholds = {
            "no_drift": 0.1,
            "moderate": 0.2,
            "severe": 0.25
        }
    
    def detect_data_drift(
        self,
        reference_data: pd.DataFrame,
        current_data: pd.DataFrame,
        numerical_features: List[str],
        categorical_features: List[str]
    ) -> Dict:
        """
        Detect drift in input features
        
        Args:
            reference_data: Training/baseline data
            current_data: Production/current data
            numerical_features: List of numerical column names
            categorical_features: List of categorical column names
            
        Returns:
            Dictionary with drift analysis results
        """
        try:
            logger.info("Starting data drift detection")
            
            results = {
                "analysis_date": datetime.now().isoformat(),
                "n_reference_samples": len(reference_data),
                "n_current_samples": len(current_data),
                "numerical_drift": {},
                "categorical_drift": {},
                "overall_drift_detected": False
            }
            
            # Numerical features drift (Kolmogorov-Smirnov test)
            drift_count = 0
            for feature in numerical_features:
                if feature in reference_data.columns and feature in current_data.columns:
                    drift_result = self._detect_numerical_drift(
                        reference_data[feature].dropna(),
                        current_data[feature].dropna(),
                        feature
                    )
                    results["numerical_drift"][feature] = drift_result
                    if drift_result["has_drift"]:
                        drift_count += 1
            
            # Categorical features drift (Chi-square test)
            for feature in categorical_features:
                if feature in reference_data.columns and feature in current_data.columns:
                    drift_result = self._detect_categorical_drift(
                        reference_data[feature].dropna(),
                        current_data[feature].dropna(),
                        feature
                    )
                    results["categorical_drift"][feature] = drift_result
                    if drift_result["has_drift"]:
                        drift_count += 1
            
            total_features = len(numerical_features) + len(categorical_features)
            results["drift_percentage"] = (drift_count / total_features) * 100 if total_features > 0 else 0
            results["overall_drift_detected"] = drift_count > 0
            
            # Calculate PSI (Population Stability Index) if predictions available
            if "prediction" in reference_data.columns and "prediction" in current_data.columns:
                psi = self._calculate_psi(
                    reference_data["prediction"],
                    current_data["prediction"]
                )
                results["psi"] = psi
                results["psi_interpretation"] = self._interpret_psi(psi)
            
            # Generate recommendations
            results["recommendations"] = self._generate_drift_recommendations(results)
            
            logger.info(f"Drift detection completed. Drift detected: {results['overall_drift_detected']}")
            return results
            
        except Exception as e:
            logger.error(f"Error in drift detection: {str(e)}")
            raise
    
    def _detect_numerical_drift(
        self,
        reference_values: pd.Series,
        current_values: pd.Series,
        feature_name: str
    ) -> Dict:
        """
        Detect drift in numerical feature using KS test
        """
        # Kolmogorov-Smirnov test
        ks_statistic, p_value = stats.ks_2samp(reference_values, current_values)
        
        has_drift = p_value < self.drift_threshold
        
        # Calculate distributional statistics
        ref_mean = float(reference_values.mean())
        cur_mean = float(current_values.mean())
        ref_std = float(reference_values.std())
        cur_std = float(current_values.std())
        
        mean_shift = abs(cur_mean - ref_mean) / ref_std if ref_std > 0 else 0
        
        return {
            "feature": feature_name,
            "test": "Kolmogorov-Smirnov",
            "has_drift": bool(has_drift),
            "p_value": float(p_value),
            "ks_statistic": float(ks_statistic),
            "reference_mean": ref_mean,
            "current_mean": cur_mean,
            "reference_std": ref_std,
            "current_std": cur_std,
            "mean_shift_std": float(mean_shift),
            "severity": self._classify_drift_severity(p_value, mean_shift)
        }
    
    def _detect_categorical_drift(
        self,
        reference_values: pd.Series,
        current_values: pd.Series,
        feature_name: str
    ) -> Dict:
        """
        Detect drift in categorical feature using Chi-square test
        """
        # Get value counts
        ref_counts = reference_values.value_counts(normalize=True)
        cur_counts = current_values.value_counts(normalize=True)
        
        # Align categories
        all_categories = set(ref_counts.index) | set(cur_counts.index)
        ref_dist = [ref_counts.get(cat, 0) for cat in all_categories]
        cur_dist = [cur_counts.get(cat, 0) for cat in all_categories]
        
        # Chi-square test (only if we have enough samples)
        if len(reference_values) > 30 and len(current_values) > 30:
            chi2_stat, p_value = stats.chisquare(
                f_obs=np.array(cur_dist) * len(current_values),
                f_exp=np.array(ref_dist) * len(current_values)
            )
        else:
            chi2_stat, p_value = 0, 1.0
        
        has_drift = p_value < self.drift_threshold
        
        # Calculate category distribution changes
        category_changes = {}
        for cat in all_categories:
            ref_prop = ref_counts.get(cat, 0)
            cur_prop = cur_counts.get(cat, 0)
            category_changes[str(cat)] = {
                "reference": float(ref_prop),
                "current": float(cur_prop),
                "change": float(cur_prop - ref_prop)
            }
        
        return {
            "feature": feature_name,
            "test": "Chi-square",
            "has_drift": bool(has_drift),
            "p_value": float(p_value),
            "chi2_statistic": float(chi2_stat),
            "category_changes": category_changes,
            "severity": self._classify_drift_severity(p_value, abs(chi2_stat))
        }
    
    def _calculate_psi(
        self,
        reference: pd.Series,
        current: pd.Series,
        bins: int = 10
    ) -> float:
        """
        Calculate Population Stability Index (PSI)
        """
        # Bin the data
        min_val = min(reference.min(), current.min())
        max_val = max(reference.max(), current.max())
        
        breakpoints = np.linspace(min_val, max_val, bins + 1)
        
        ref_percents = np.histogram(reference, bins=breakpoints)[0] / len(reference)
        cur_percents = np.histogram(current, bins=breakpoints)[0] / len(current)
        
        # Avoid division by zero
        ref_percents = np.where(ref_percents == 0, 0.0001, ref_percents)
        cur_percents = np.where(cur_percents == 0, 0.0001, cur_percents)
        
        # Calculate PSI
        psi = np.sum((cur_percents - ref_percents) * np.log(cur_percents / ref_percents))
        
        return float(psi)
    
    def _interpret_psi(self, psi: float) -> str:
        """
        Interpret PSI value
        """
        if psi < self.psi_thresholds["no_drift"]:
            return "NO_DRIFT"
        elif psi < self.psi_thresholds["moderate"]:
            return "MODERATE_DRIFT"
        elif psi < self.psi_thresholds["severe"]:
            return "SEVERE_DRIFT"
        else:
            return "CRITICAL_DRIFT"
    
    def _classify_drift_severity(self, p_value: float, statistic: float) -> str:
        """
        Classify drift severity
        """
        if p_value >= self.drift_threshold:
            return "NO_DRIFT"
        elif p_value >= 0.01:
            return "LOW"
        elif p_value >= 0.001:
            return "MODERATE"
        else:
            return "HIGH"
    
    def _generate_drift_recommendations(self, results: Dict) -> List[str]:
        """
        Generate actionable recommendations based on drift analysis
        """
        recommendations = []
        
        if not results["overall_drift_detected"]:
            recommendations.append("✅ No significant drift detected. Continue monitoring.")
            return recommendations
        
        drift_pct = results.get("drift_percentage", 0)
        
        if drift_pct > 50:
            recommendations.append("🚨 CRITICAL: Over 50% of features show drift")
            recommendations.append("1. STOP using current model in production")
            recommendations.append("2. Investigate data pipeline for changes")
            recommendations.append("3. Retrain model with recent data immediately")
        elif drift_pct > 25:
            recommendations.append("⚠️ HIGH: Significant drift detected in multiple features")
            recommendations.append("1. Schedule model retraining within 1 week")
            recommendations.append("2. Increase monitoring frequency")
            recommendations.append("3. Review data collection process")
        else:
            recommendations.append("ℹ️ MODERATE: Some drift detected")
            recommendations.append("1. Monitor affected features closely")
            recommendations.append("2. Plan model retraining within 1 month")
            recommendations.append("3. Document drift for audit trail")
        
        # PSI-specific recommendations
        if "psi" in results:
            psi_interp = results.get("psi_interpretation", "")
            if psi_interp in ["SEVERE_DRIFT", "CRITICAL_DRIFT"]:
                recommendations.append("4. PSI indicates population shift - retrain urgently")
        
        return recommendations


# Singleton instance
drift_service = DriftDetectionService()


