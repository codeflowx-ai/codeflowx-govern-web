"""
AI Governance Platform - Data Quality Validation Service
Validates data quality for ML model training and deployment
"""

import pandas as pd
import numpy as np
from typing import Dict, List, Optional
import logging
from datetime import datetime

logger = logging.getLogger(__name__)


class DataQualityService:
    """
    Service for validating data quality
    """
    
    def __init__(self):
        self.quality_thresholds = {
            "missing_threshold": 0.05,  # 5% max missing values
            "duplicate_threshold": 0.01,  # 1% max duplicates
            "outlier_threshold": 0.05,  # 5% max outliers
            "imbalance_threshold": 0.2  # 20% min class proportion
        }
    
    def validate_dataset(
        self,
        df: pd.DataFrame,
        target_column: Optional[str] = None,
        numerical_features: Optional[List[str]] = None,
        categorical_features: Optional[List[str]] = None
    ) -> Dict:
        """
        Comprehensive data quality validation
        
        Args:
            df: Dataset to validate
            target_column: Name of target column (if classification)
            numerical_features: List of numerical columns
            categorical_features: List of categorical columns
            
        Returns:
            Dictionary with quality metrics and issues
        """
        try:
            logger.info(f"Starting data quality validation for dataset with {len(df)} rows")
            
            results = {
                "validation_date": datetime.now().isoformat(),
                "n_samples": len(df),
                "n_features": len(df.columns),
                "issues": [],
                "quality_score": 0
            }
            
            # Auto-detect feature types if not provided
            if numerical_features is None:
                numerical_features = df.select_dtypes(include=[np.number]).columns.tolist()
            if categorical_features is None:
                categorical_features = df.select_dtypes(include=['object', 'category']).columns.tolist()
            
            # 1. Missing values analysis
            missing_analysis = self._analyze_missing_values(df)
            results["missing_values"] = missing_analysis
            if missing_analysis["has_critical_missing"]:
                results["issues"].append({
                    "severity": "HIGH",
                    "category": "Missing Values",
                    "message": f"{missing_analysis['critical_columns']} have >5% missing values"
                })
            
            # 2. Duplicate records
            duplicates_analysis = self._analyze_duplicates(df)
            results["duplicates"] = duplicates_analysis
            if duplicates_analysis["duplicate_percentage"] > self.quality_thresholds["duplicate_threshold"]:
                results["issues"].append({
                    "severity": "MEDIUM",
                    "category": "Duplicates",
                    "message": f"{duplicates_analysis['n_duplicates']} duplicate records found"
                })
            
            # 3. Outliers detection (for numerical features)
            outliers_analysis = self._detect_outliers(df, numerical_features)
            results["outliers"] = outliers_analysis
            
            # 4. Data type consistency
            type_analysis = self._analyze_data_types(df)
            results["data_types"] = type_analysis
            
            # 5. Statistical properties
            stats_analysis = self._analyze_statistics(df, numerical_features)
            results["statistics"] = stats_analysis
            
            # 6. Class imbalance (if target provided)
            if target_column and target_column in df.columns:
                imbalance_analysis = self._analyze_class_imbalance(df[target_column])
                results["class_balance"] = imbalance_analysis
                if imbalance_analysis["is_imbalanced"]:
                    results["issues"].append({
                        "severity": "HIGH",
                        "category": "Class Imbalance",
                        "message": f"Minority class has only {imbalance_analysis['min_class_percentage']:.1%}"
                    })
            
            # 7. Feature correlations (multicollinearity)
            if len(numerical_features) > 1:
                corr_analysis = self._analyze_correlations(df[numerical_features])
                results["correlations"] = corr_analysis
                if corr_analysis["high_correlations"]:
                    results["issues"].append({
                        "severity": "MEDIUM",
                        "category": "Multicollinearity",
                        "message": f"{len(corr_analysis['high_correlations'])} feature pairs highly correlated"
                    })
            
            # 8. Constant and quasi-constant features
            constant_features = self._detect_constant_features(df)
            results["constant_features"] = constant_features
            if constant_features["constant_features"]:
                results["issues"].append({
                    "severity": "LOW",
                    "category": "Constant Features",
                    "message": f"{len(constant_features['constant_features'])} constant features found"
                })
            
            # Calculate overall quality score
            results["quality_score"] = self._calculate_quality_score(results)
            results["quality_level"] = self._classify_quality_level(results["quality_score"])
            
            # Generate recommendations
            results["recommendations"] = self._generate_quality_recommendations(results)
            
            logger.info(f"Data quality validation completed. Score: {results['quality_score']}/100")
            return results
            
        except Exception as e:
            logger.error(f"Error in data quality validation: {str(e)}")
            raise
    
    def _analyze_missing_values(self, df: pd.DataFrame) -> Dict:
        """
        Analyze missing values in dataset
        """
        missing_counts = df.isnull().sum()
        missing_percentages = (missing_counts / len(df)) * 100
        
        critical_columns = [
            col for col, pct in missing_percentages.items()
            if pct > (self.quality_thresholds["missing_threshold"] * 100)
        ]
        
        return {
            "total_missing": int(missing_counts.sum()),
            "columns_with_missing": missing_counts[missing_counts > 0].to_dict(),
            "missing_percentages": missing_percentages[missing_percentages > 0].to_dict(),
            "critical_columns": critical_columns,
            "has_critical_missing": len(critical_columns) > 0
        }
    
    def _analyze_duplicates(self, df: pd.DataFrame) -> Dict:
        """
        Analyze duplicate records
        """
        n_duplicates = df.duplicated().sum()
        duplicate_percentage = (n_duplicates / len(df)) * 100
        
        return {
            "n_duplicates": int(n_duplicates),
            "duplicate_percentage": float(duplicate_percentage),
            "has_duplicates": n_duplicates > 0
        }
    
    def _detect_outliers(self, df: pd.DataFrame, numerical_features: List[str]) -> Dict:
        """
        Detect outliers using IQR method
        """
        outliers = {}
        
        for col in numerical_features:
            if col in df.columns:
                Q1 = df[col].quantile(0.25)
                Q3 = df[col].quantile(0.75)
                IQR = Q3 - Q1
                
                lower_bound = Q1 - 1.5 * IQR
                upper_bound = Q3 + 1.5 * IQR
                
                n_outliers = ((df[col] < lower_bound) | (df[col] > upper_bound)).sum()
                outlier_percentage = (n_outliers / len(df)) * 100
                
                if n_outliers > 0:
                    outliers[col] = {
                        "n_outliers": int(n_outliers),
                        "percentage": float(outlier_percentage),
                        "lower_bound": float(lower_bound),
                        "upper_bound": float(upper_bound)
                    }
        
        total_outliers = sum(o["n_outliers"] for o in outliers.values())
        
        return {
            "features_with_outliers": outliers,
            "total_outliers": total_outliers
        }
    
    def _analyze_data_types(self, df: pd.DataFrame) -> Dict:
        """
        Analyze data type consistency
        """
        type_counts = df.dtypes.value_counts().to_dict()
        
        # Convert dtype objects to strings
        type_counts = {str(k): int(v) for k, v in type_counts.items()}
        
        return {
            "type_distribution": type_counts,
            "n_numerical": len(df.select_dtypes(include=[np.number]).columns),
            "n_categorical": len(df.select_dtypes(include=['object', 'category']).columns)
        }
    
    def _analyze_statistics(self, df: pd.DataFrame, numerical_features: List[str]) -> Dict:
        """
        Calculate statistical properties
        """
        stats = {}
        
        for col in numerical_features:
            if col in df.columns:
                stats[col] = {
                    "mean": float(df[col].mean()),
                    "median": float(df[col].median()),
                    "std": float(df[col].std()),
                    "min": float(df[col].min()),
                    "max": float(df[col].max()),
                    "skewness": float(df[col].skew()),
                    "kurtosis": float(df[col].kurtosis())
                }
        
        return stats
    
    def _analyze_class_imbalance(self, target: pd.Series) -> Dict:
        """
        Analyze class balance in target variable
        """
        value_counts = target.value_counts()
        proportions = target.value_counts(normalize=True)
        
        min_class_pct = proportions.min()
        
        return {
            "class_distribution": value_counts.to_dict(),
            "class_proportions": proportions.to_dict(),
            "min_class_percentage": float(min_class_pct),
            "is_imbalanced": min_class_pct < self.quality_thresholds["imbalance_threshold"],
            "imbalance_ratio": float(proportions.max() / min_class_pct) if min_class_pct > 0 else float('inf')
        }
    
    def _analyze_correlations(self, df: pd.DataFrame, threshold: float = 0.9) -> Dict:
        """
        Detect high correlations between features
        """
        corr_matrix = df.corr()
        
        high_correlations = []
        for i in range(len(corr_matrix.columns)):
            for j in range(i+1, len(corr_matrix.columns)):
                if abs(corr_matrix.iloc[i, j]) > threshold:
                    high_correlations.append({
                        "feature1": corr_matrix.columns[i],
                        "feature2": corr_matrix.columns[j],
                        "correlation": float(corr_matrix.iloc[i, j])
                    })
        
        return {
            "high_correlations": high_correlations,
            "n_high_correlations": len(high_correlations)
        }
    
    def _detect_constant_features(self, df: pd.DataFrame) -> Dict:
        """
        Detect constant and quasi-constant features
        """
        constant = []
        quasi_constant = []
        
        for col in df.columns:
            n_unique = df[col].nunique()
            if n_unique == 1:
                constant.append(col)
            elif n_unique < 5 and len(df) > 100:
                # Quasi-constant: very few unique values relative to dataset size
                dominant_frequency = df[col].value_counts().iloc[0] / len(df)
                if dominant_frequency > 0.95:
                    quasi_constant.append(col)
        
        return {
            "constant_features": constant,
            "quasi_constant_features": quasi_constant
        }
    
    def _calculate_quality_score(self, results: Dict) -> float:
        """
        Calculate overall quality score (0-100)
        """
        score = 100.0
        
        # Deduct points for issues
        for issue in results.get("issues", []):
            if issue["severity"] == "HIGH":
                score -= 15
            elif issue["severity"] == "MEDIUM":
                score -= 10
            elif issue["severity"] == "LOW":
                score -= 5
        
        # Deduct for missing values
        missing = results.get("missing_values", {})
        if missing.get("total_missing", 0) > 0:
            missing_pct = (missing["total_missing"] / (results["n_samples"] * results["n_features"])) * 100
            score -= min(missing_pct * 2, 20)  # Max 20 points deduction
        
        return max(0.0, score)
    
    def _classify_quality_level(self, score: float) -> str:
        """
        Classify quality level based on score
        """
        if score >= 90:
            return "EXCELLENT"
        elif score >= 75:
            return "GOOD"
        elif score >= 60:
            return "FAIR"
        elif score >= 40:
            return "POOR"
        else:
            return "CRITICAL"
    
    def _generate_quality_recommendations(self, results: Dict) -> List[str]:
        """
        Generate quality improvement recommendations
        """
        recommendations = []
        
        quality_level = results.get("quality_level", "")
        
        if quality_level in ["EXCELLENT", "GOOD"]:
            recommendations.append("✅ Data quality is acceptable for model training")
        else:
            recommendations.append("⚠️ Data quality issues detected. Address before training:")
        
        # Specific recommendations based on issues
        for issue in results.get("issues", []):
            if issue["category"] == "Missing Values":
                recommendations.append("• Impute or remove features with excessive missing values")
            elif issue["category"] == "Duplicates":
                recommendations.append("• Remove duplicate records to avoid data leakage")
            elif issue["category"] == "Class Imbalance":
                recommendations.append("• Apply resampling (SMOTE, undersampling) or use class weights")
            elif issue["category"] == "Multicollinearity":
                recommendations.append("• Remove or combine highly correlated features")
            elif issue["category"] == "Constant Features":
                recommendations.append("• Remove constant features (zero information)")
        
        # Outliers recommendation
        outliers = results.get("outliers", {})
        if outliers.get("total_outliers", 0) > 0:
            recommendations.append("• Review outliers: cap, transform, or remove if errors")
        
        return recommendations


# Singleton instance
data_quality_service = DataQualityService()


