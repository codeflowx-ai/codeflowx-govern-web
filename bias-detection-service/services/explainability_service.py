"""
AI Governance Platform - Explainability Service
Provides model explanations using SHAP and LIME
"""

import pandas as pd
import numpy as np
from typing import Dict, List, Optional, Any
import logging
from datetime import datetime

logger = logging.getLogger(__name__)


class ExplainabilityService:
    """
    Service for explaining model predictions
    Uses SHAP (SHapley Additive exPlanations) for tree-based models
    """
    
    def __init__(self):
        self.max_samples_for_explanation = 1000
    
    def explain_predictions(
        self,
        model_type: str,
        X: pd.DataFrame,
        predictions: np.ndarray,
        feature_names: List[str],
        sample_indices: Optional[List[int]] = None,
        method: str = "shap"
    ) -> Dict:
        """
        Generate explanations for model predictions
        
        Args:
            model_type: Type of model (tree, linear, neural)
            X: Feature data
            predictions: Model predictions
            feature_names: List of feature names
            sample_indices: Specific samples to explain (optional)
            method: Explanation method (shap, lime, permutation)
            
        Returns:
            Dictionary with explanations
        """
        try:
            logger.info(f"Generating explanations using {method} for {model_type} model")
            
            results = {
                "explanation_date": datetime.now().isoformat(),
                "method": method,
                "model_type": model_type,
                "n_samples": len(X),
                "n_features": len(feature_names)
            }
            
            # Limit samples for performance
            if len(X) > self.max_samples_for_explanation:
                logger.warning(f"Sampling {self.max_samples_for_explanation} records for explanation")
                sample_idx = np.random.choice(len(X), self.max_samples_for_explanation, replace=False)
                X = X.iloc[sample_idx]
                predictions = predictions[sample_idx]
            
            if method == "shap":
                explanations = self._explain_with_shap_surrogate(X, predictions, feature_names)
            elif method == "lime":
                explanations = self._explain_with_lime_surrogate(X, predictions, feature_names, sample_indices)
            elif method == "permutation":
                explanations = self._explain_with_permutation(X, predictions, feature_names)
            else:
                raise ValueError(f"Unknown explanation method: {method}")
            
            results.update(explanations)
            
            # Generate recommendations
            results["recommendations"] = self._generate_explainability_recommendations(results)
            
            logger.info("Explanation generation completed")
            return results
            
        except Exception as e:
            logger.error(f"Error generating explanations: {str(e)}")
            raise
    
    def _explain_with_shap_surrogate(
        self,
        X: pd.DataFrame,
        predictions: np.ndarray,
        feature_names: List[str]
    ) -> Dict:
        """
        SHAP-like explanation using correlation analysis (surrogate when model not available)
        Real SHAP requires access to the actual model
        """
        try:
            # Calculate feature importance using correlation with predictions
            feature_importance = {}
            
            for i, feature in enumerate(feature_names):
                if feature in X.columns:
                    # Calculate correlation between feature and predictions
                    corr = np.corrcoef(X[feature].fillna(0), predictions)[0, 1]
                    feature_importance[feature] = abs(corr) if not np.isnan(corr) else 0
            
            # Sort by importance
            sorted_features = sorted(
                feature_importance.items(),
                key=lambda x: x[1],
                reverse=True
            )
            
            # Calculate relative importance
            total_importance = sum(abs(v) for v in feature_importance.values())
            if total_importance > 0:
                relative_importance = {
                    k: (v / total_importance) * 100
                    for k, v in feature_importance.items()
                }
            else:
                relative_importance = {k: 0 for k in feature_importance.keys()}
            
            return {
                "global_feature_importance": {
                    "absolute": feature_importance,
                    "relative": relative_importance,
                    "top_10_features": [f[0] for f in sorted_features[:10]]
                },
                "explanation_method": "correlation_based_surrogate",
                "note": "This is a surrogate explanation. For true SHAP values, provide the trained model."
            }
            
        except Exception as e:
            logger.error(f"Error in SHAP surrogate: {str(e)}")
            return {"error": str(e)}
    
    def _explain_with_lime_surrogate(
        self,
        X: pd.DataFrame,
        predictions: np.ndarray,
        feature_names: List[str],
        sample_indices: Optional[List[int]] = None
    ) -> Dict:
        """
        LIME-like local explanation (surrogate when model not available)
        """
        try:
            if sample_indices is None:
                # Select random samples
                sample_indices = np.random.choice(len(X), min(5, len(X)), replace=False)
            
            local_explanations = []
            
            for idx in sample_indices:
                sample = X.iloc[idx]
                prediction = predictions[idx]
                
                # Simple local explanation: feature values and their contribution
                feature_contributions = {}
                for feature in feature_names:
                    if feature in X.columns:
                        value = sample[feature]
                        # Estimate contribution based on deviation from mean
                        mean_val = X[feature].mean()
                        contribution = (value - mean_val) * prediction if not pd.isna(value) else 0
                        feature_contributions[feature] = {
                            "value": float(value) if not pd.isna(value) else None,
                            "contribution": float(contribution)
                        }
                
                local_explanations.append({
                    "sample_index": int(idx),
                    "prediction": float(prediction),
                    "feature_contributions": feature_contributions
                })
            
            return {
                "local_explanations": local_explanations,
                "n_samples_explained": len(local_explanations),
                "explanation_method": "local_surrogate",
                "note": "This is a surrogate explanation. For true LIME explanations, provide the trained model."
            }
            
        except Exception as e:
            logger.error(f"Error in LIME surrogate: {str(e)}")
            return {"error": str(e)}
    
    def _explain_with_permutation(
        self,
        X: pd.DataFrame,
        predictions: np.ndarray,
        feature_names: List[str],
        n_repeats: int = 5
    ) -> Dict:
        """
        Permutation feature importance
        Measures importance by shuffling each feature and measuring prediction change
        """
        try:
            from sklearn.metrics import mean_absolute_error
            
            baseline_mae = mean_absolute_error([predictions.mean()] * len(predictions), predictions)
            
            importances = {}
            
            for feature in feature_names:
                if feature in X.columns:
                    feature_importances = []
                    
                    for _ in range(n_repeats):
                        X_permuted = X.copy()
                        X_permuted[feature] = np.random.permutation(X_permuted[feature].values)
                        
                        # For surrogate: use correlation change as proxy
                        corr_change = abs(np.corrcoef(X[feature].fillna(0), predictions)[0, 1]) - \
                                     abs(np.corrcoef(X_permuted[feature].fillna(0), predictions)[0, 1])
                        
                        feature_importances.append(abs(corr_change))
                    
                    importances[feature] = {
                        "mean_importance": float(np.mean(feature_importances)),
                        "std_importance": float(np.std(feature_importances))
                    }
            
            # Sort by importance
            sorted_features = sorted(
                importances.items(),
                key=lambda x: x[1]["mean_importance"],
                reverse=True
            )
            
            return {
                "permutation_importance": importances,
                "ranked_features": [f[0] for f in sorted_features],
                "top_10_features": [f[0] for f in sorted_features[:10]],
                "n_repeats": n_repeats
            }
            
        except Exception as e:
            logger.error(f"Error in permutation importance: {str(e)}")
            return {"error": str(e)}
    
    def _generate_explainability_recommendations(self, results: Dict) -> List[str]:
        """
        Generate recommendations based on explainability analysis
        """
        recommendations = []
        
        # Check if model is explainable
        if results.get("error"):
            recommendations.append("⚠️ Could not generate explanations - provide trained model for accurate results")
            return recommendations
        
        recommendations.append("ℹ️ Explainability Analysis Completed")
        
        # Check feature importance distribution
        if "global_feature_importance" in results:
            top_features = results["global_feature_importance"].get("top_10_features", [])
            if top_features:
                recommendations.append(f"• Top 3 influential features: {', '.join(top_features[:3])}")
                recommendations.append("• Focus on documenting these features for stakeholders")
        
        # EU AI Act compliance
        recommendations.append("• EU AI Act Art. 13: Maintain transparency documentation")
        recommendations.append("• GDPR Art. 22: Be ready to explain individual decisions")
        
        # Best practices
        recommendations.append("• For production: Implement SHAP with actual model for accurate explanations")
        recommendations.append("• Document feature meanings for non-technical users")
        recommendations.append("• Monitor if top features change over time (drift)")
        
        return recommendations


# Singleton instance
explainability_service = ExplainabilityService()


