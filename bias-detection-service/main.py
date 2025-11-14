"""
AI Governance Platform - Bias Detection Service
FastAPI service for analyzing bias in ML models using Fairlearn
"""

from fastapi import FastAPI, File, UploadFile, Form, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from typing import List, Dict, Optional
import pandas as pd
import numpy as np
from io import StringIO
from datetime import datetime
import logging
from sklearn.metrics import accuracy_score, precision_score, recall_score, confusion_matrix
import traceback

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="Bias Detection Service",
    description="ML Model Bias Analysis API for AI Governance Platform",
    version="1.0.0"
)

# CORS configuration for integration with Java backend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify actual origins
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Response Models
class GroupAnalysis(BaseModel):
    group: str
    accuracy: float
    precision: float
    recall: float
    count: int


class BiasMetrics(BaseModel):
    demographic_parity_difference: float
    equal_opportunity_difference: float
    disparate_impact_ratio: float


class BiasAnalysisResponse(BaseModel):
    model_id: str
    analysis_date: str
    metrics: BiasMetrics
    classification: str
    recommendations: str
    groups_analysis: List[GroupAnalysis]
    threshold_used: float
    protected_attribute: str


# Helper Functions
def calculate_demographic_parity(y_true, y_pred, sensitive_feature):
    """
    Calculate Demographic Parity Difference
    Measures difference in positive prediction rates between groups
    """
    df = pd.DataFrame({
        'y_pred': y_pred,
        'sensitive': sensitive_feature
    })
    
    groups = df.groupby('sensitive')['y_pred'].mean()
    
    if len(groups) < 2:
        return 0.0
    
    # Difference between max and min positive rates
    dpd = groups.max() - groups.min()
    return float(dpd)


def calculate_equal_opportunity(y_true, y_pred, sensitive_feature):
    """
    Calculate Equal Opportunity Difference
    Measures difference in true positive rates between groups
    """
    df = pd.DataFrame({
        'y_true': y_true,
        'y_pred': y_pred,
        'sensitive': sensitive_feature
    })
    
    # Filter only positive cases (y_true = 1)
    positive_cases = df[df['y_true'] == 1]
    
    if len(positive_cases) == 0:
        return 0.0
    
    groups = positive_cases.groupby('sensitive')['y_pred'].mean()
    
    if len(groups) < 2:
        return 0.0
    
    # Difference between max and min TPR
    eod = groups.max() - groups.min()
    return float(eod)


def calculate_disparate_impact(y_true, y_pred, sensitive_feature):
    """
    Calculate Disparate Impact Ratio
    Ratio of positive prediction rates between groups
    Should be close to 1.0 (ideal: between 0.8 and 1.25)
    """
    df = pd.DataFrame({
        'y_pred': y_pred,
        'sensitive': sensitive_feature
    })
    
    groups = df.groupby('sensitive')['y_pred'].mean()
    
    if len(groups) < 2:
        return 1.0
    
    min_rate = groups.min()
    max_rate = groups.max()
    
    if max_rate == 0:
        return 1.0
    
    # Ratio of min/max (should be close to 1.0)
    di_ratio = min_rate / max_rate
    return float(di_ratio)


def classify_bias_severity(metrics: BiasMetrics, threshold: float) -> str:
    """
    Classify bias severity based on metrics
    """
    dpd = abs(metrics.demographic_parity_difference)
    eod = abs(metrics.equal_opportunity_difference)
    di_ratio = metrics.disparate_impact_ratio
    
    # Disparate impact should be between 0.8 and 1.25
    di_deviation = abs(1.0 - di_ratio)
    
    # Find maximum deviation
    max_deviation = max(dpd, eod, di_deviation)
    
    if max_deviation < 0.05:
        return "NO_BIAS"
    elif max_deviation < 0.10:
        return "LOW"
    elif max_deviation < 0.20:
        return "MODERATE"
    elif max_deviation < 0.30:
        return "HIGH"
    else:
        return "CRITICAL"


def generate_recommendations(
    classification: str,
    protected_attribute: str,
    metrics: BiasMetrics,
    groups_analysis: List[GroupAnalysis]
) -> str:
    """
    Generate automated recommendations based on bias analysis
    """
    if classification == "NO_BIAS":
        return f"No significant bias detected in {protected_attribute} attribute. Model shows fair performance across groups."
    
    # Find disadvantaged group
    worst_group = min(groups_analysis, key=lambda x: x.accuracy)
    best_group = max(groups_analysis, key=lambda x: x.accuracy)
    
    accuracy_gap = best_group.accuracy - worst_group.accuracy
    
    recommendations = []
    
    if classification in ["HIGH", "CRITICAL"]:
        recommendations.append(f"⚠️ {classification} bias detected in {protected_attribute} attribute.")
        recommendations.append(f"The '{worst_group.group}' group shows significantly lower accuracy ({worst_group.accuracy:.2%}) compared to '{best_group.group}' ({best_group.accuracy:.2%}).")
        recommendations.append("\nImmediate actions required:")
        recommendations.append("1. DO NOT deploy this model to production")
        recommendations.append("2. Collect more balanced training data for underrepresented groups")
        recommendations.append("3. Consider using fairness constraints during model retraining")
        recommendations.append("4. Apply bias mitigation techniques (reweighting, adversarial debiasing)")
    elif classification == "MODERATE":
        recommendations.append(f"⚠️ Moderate bias detected in {protected_attribute} attribute.")
        recommendations.append(f"Performance gap of {accuracy_gap:.1%} between '{worst_group.group}' and '{best_group.group}' groups.")
        recommendations.append("\nRecommended actions:")
        recommendations.append("1. Review model before production deployment")
        recommendations.append("2. Consider rebalancing training dataset")
        recommendations.append("3. Monitor model performance by group in production")
        recommendations.append("4. Explore fairness-aware algorithms")
    else:  # LOW
        recommendations.append(f"ℹ️ Low bias detected in {protected_attribute} attribute.")
        recommendations.append("The model shows minor performance differences between groups.")
        recommendations.append("\nRecommended actions:")
        recommendations.append("1. Monitor model performance continuously")
        recommendations.append("2. Document known limitations")
        recommendations.append("3. Implement fairness metrics in production monitoring")
    
    return "\n".join(recommendations)


def analyze_groups_performance(y_true, y_pred, sensitive_feature) -> List[GroupAnalysis]:
    """
    Calculate performance metrics for each group
    """
    df = pd.DataFrame({
        'y_true': y_true,
        'y_pred': y_pred,
        'sensitive': sensitive_feature
    })
    
    groups_analysis = []
    
    for group_name, group_data in df.groupby('sensitive'):
        y_true_group = group_data['y_true'].values
        y_pred_group = group_data['y_pred'].values
        
        # Handle binary classification
        try:
            accuracy = accuracy_score(y_true_group, y_pred_group)
            
            # Precision and recall with zero_division handling
            precision = precision_score(y_true_group, y_pred_group, zero_division=0, average='binary')
            recall = recall_score(y_true_group, y_pred_group, zero_division=0, average='binary')
            
        except Exception as e:
            logger.warning(f"Error calculating metrics for group {group_name}: {e}")
            accuracy = 0.0
            precision = 0.0
            recall = 0.0
        
        groups_analysis.append(GroupAnalysis(
            group=str(group_name),
            accuracy=float(accuracy),
            precision=float(precision),
            recall=float(recall),
            count=len(group_data)
        ))
    
    return groups_analysis


@app.get("/")
def root():
    """Health check endpoint"""
    return {
        "service": "Bias Detection Service",
        "status": "running",
        "version": "1.0.0"
    }


@app.get("/health")
def health_check():
    """Detailed health check"""
    return {
        "status": "healthy",
        "timestamp": datetime.now().isoformat(),
        "dependencies": {
            "pandas": pd.__version__,
            "numpy": np.__version__
        }
    }


@app.post("/api/bias-analysis/analyze", response_model=BiasAnalysisResponse)
async def analyze_bias(
    file: UploadFile = File(...),
    model_id: str = Form(...),
    protected_attribute: str = Form(...),
    favorable_outcome: str = Form(default="1"),
    threshold: float = Form(default=0.8)
):
    """
    Analyze bias in ML model predictions
    
    Args:
        file: CSV file with columns [y_true, y_pred, protected_attribute]
        model_id: ID of the model being analyzed
        protected_attribute: Name of the protected attribute column
        favorable_outcome: Value considered as favorable (default: "1")
        threshold: Fairness threshold (default: 0.8)
    
    Returns:
        BiasAnalysisResponse with metrics and recommendations
    """
    try:
        logger.info(f"Starting bias analysis for model {model_id}")
        
        # Validate file type
        if not file.filename.endswith('.csv'):
            raise HTTPException(
                status_code=400,
                detail="File must be CSV format"
            )
        
        # Check file size (max 100MB)
        contents = await file.read()
        file_size_mb = len(contents) / (1024 * 1024)
        
        if file_size_mb > 100:
            raise HTTPException(
                status_code=413,
                detail=f"File too large ({file_size_mb:.1f}MB). Maximum size is 100MB"
            )
        
        # Parse CSV
        try:
            df = pd.read_csv(StringIO(contents.decode('utf-8')))
        except Exception as e:
            raise HTTPException(
                status_code=400,
                detail=f"Invalid CSV format: {str(e)}"
            )
        
        # Validate required columns
        required_columns = ['y_true', 'y_pred', protected_attribute]
        missing_columns = [col for col in required_columns if col not in df.columns]
        
        if missing_columns:
            raise HTTPException(
                status_code=400,
                detail=f"Missing required columns: {missing_columns}. Found columns: {list(df.columns)}"
            )
        
        # Validate data
        if len(df) == 0:
            raise HTTPException(
                status_code=400,
                detail="CSV file is empty"
            )
        
        if len(df) < 30:
            logger.warning(f"Small dataset: only {len(df)} rows")
        
        # Extract data
        y_true = df['y_true'].values
        y_pred = df['y_pred'].values
        sensitive_feature = df[protected_attribute].values
        
        # Check for missing values
        if pd.isna(y_true).any() or pd.isna(y_pred).any() or pd.isna(sensitive_feature).any():
            raise HTTPException(
                status_code=400,
                detail="Data contains missing values. Please clean the dataset."
            )
        
        # Calculate fairness metrics
        logger.info("Calculating fairness metrics...")
        
        dpd = calculate_demographic_parity(y_true, y_pred, sensitive_feature)
        eod = calculate_equal_opportunity(y_true, y_pred, sensitive_feature)
        di_ratio = calculate_disparate_impact(y_true, y_pred, sensitive_feature)
        
        metrics = BiasMetrics(
            demographic_parity_difference=dpd,
            equal_opportunity_difference=eod,
            disparate_impact_ratio=di_ratio
        )
        
        # Classify bias severity
        classification = classify_bias_severity(metrics, threshold)
        
        # Analyze performance by group
        groups_analysis = analyze_groups_performance(y_true, y_pred, sensitive_feature)
        
        # Generate recommendations
        recommendations = generate_recommendations(
            classification,
            protected_attribute,
            metrics,
            groups_analysis
        )
        
        logger.info(f"Analysis complete. Classification: {classification}")
        
        response = BiasAnalysisResponse(
            model_id=model_id,
            analysis_date=datetime.now().isoformat(),
            metrics=metrics,
            classification=classification,
            recommendations=recommendations,
            groups_analysis=groups_analysis,
            threshold_used=threshold,
            protected_attribute=protected_attribute
        )
        
        return response
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error in bias analysis: {str(e)}")
        logger.error(traceback.format_exc())
        raise HTTPException(
            status_code=500,
            detail=f"Internal server error during bias analysis: {str(e)}"
        )


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8001)

