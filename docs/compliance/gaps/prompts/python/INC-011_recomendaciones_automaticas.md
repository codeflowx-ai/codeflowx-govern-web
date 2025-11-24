# PROMPT: INC-011-DS - Recomendaciones Accionables Automáticas

**Incidencia:** INC-011-DS  
**Prioridad:** 🟢 LOW  
**Artículo EU AI Act:** Art. 10.2 (medidas de mitigación)  
**Esfuerzo Estimado:** 4-5 días  
**Tipo:** Python - Microservicio  
**Estado:** ✅ **CORREGIDA** (Noviembre 2025)

---

## CONTEXTO

El sistema detecta problemas pero no genera recomendaciones específicas y accionables para corregirlos (ej: "Eliminar columnas X, Y para reducir leakage").

**Ubicación Actual:**
- `MODMITIGATIONSTRATEGIES` (JSONB) - Solo almacena, no genera automáticamente
- No hay lógica de generación de recomendaciones basada en problemas detectados

---

## REQUISITOS

1. Análisis de problemas detectados
2. Generación de recomendaciones específicas por tipo de problema
3. Priorización de recomendaciones por impacto
4. Ejemplos de código/comandos para implementar recomendaciones

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Service de Generación de Recomendaciones

**Archivo:** `bias-detection-service/services/recommendation_generator.py`

```python
"""
Recommendation Generator Service
Genera recomendaciones accionables basadas en problemas detectados
"""
from typing import Dict, List, Optional
import logging

logger = logging.getLogger(__name__)


class RecommendationGenerator:
    """
    Genera recomendaciones automáticas para corregir problemas detectados
    """
    
    def generate_recommendations(self, quality_results: Dict) -> List[Dict]:
        """
        Genera recomendaciones basadas en problemas detectados
        
        Args:
            quality_results: Resultados de evaluación de calidad
            
        Returns:
            Lista de recomendaciones con prioridad y acciones
        """
        recommendations = []
        
        # Analizar cada problema
        issues = quality_results.get("issues", [])
        
        for issue in issues:
            category = issue.get("category")
            severity = issue.get("severity")
            message = issue.get("message")
            
            # Generar recomendaciones según categoría
            if category == "Missing Values":
                recommendations.extend(
                    self._recommendations_missing_values(issue, quality_results)
                )
            elif category == "Duplicates":
                recommendations.extend(
                    self._recommendations_duplicates(issue, quality_results)
                )
            elif category == "Outliers":
                recommendations.extend(
                    self._recommendations_outliers(issue, quality_results)
                )
            elif category == "Bias":
                recommendations.extend(
                    self._recommendations_bias(issue, quality_results)
                )
            elif category == "Label Leakage":
                recommendations.extend(
                    self._recommendations_leakage(issue, quality_results)
                )
        
        # Priorizar recomendaciones
        recommendations = self._prioritize_recommendations(recommendations)
        
        return recommendations
    
    def _recommendations_missing_values(self, issue: Dict, results: Dict) -> List[Dict]:
        """Recomendaciones para valores faltantes"""
        recommendations = []
        
        missing_analysis = results.get("missing_values", {})
        column = issue.get("column")
        
        if column and column in missing_analysis:
            missing_pct = missing_analysis[column].get("missing_percentage", 0)
            
            if missing_pct > 50:
                recommendations.append({
                    "type": "REMOVE_COLUMN",
                    "priority": "HIGH",
                    "title": f"Eliminar columna '{column}' (más del 50% valores faltantes)",
                    "description": f"La columna '{column}' tiene {missing_pct:.1f}% de valores faltantes, "
                                 f"lo que la hace poco útil para análisis.",
                    "action": {
                        "type": "python_code",
                        "code": f"df = df.drop(columns=['{column}'])",
                        "impact": "Elimina columna del dataset"
                    },
                    "estimated_impact": "Alta - Mejora completitud general"
                })
            elif missing_pct > 20:
                recommendations.append({
                    "type": "IMPUTE",
                    "priority": "MEDIUM",
                    "title": f"Imputar valores faltantes en '{column}'",
                    "description": f"La columna '{column}' tiene {missing_pct:.1f}% de valores faltantes. "
                                 f"Considerar imputación con media, mediana o modelo predictivo.",
                    "action": {
                        "type": "python_code",
                        "code": f"from sklearn.impute import SimpleImputer\n"
                               f"imputer = SimpleImputer(strategy='mean')\n"
                               f"df['{column}'] = imputer.fit_transform(df[['{column}']])",
                        "impact": "Imputa valores faltantes con media"
                    },
                    "estimated_impact": "Media - Mejora completitud sin perder datos"
                })
            else:
                recommendations.append({
                    "type": "DROP_ROWS",
                    "priority": "LOW",
                    "title": f"Eliminar filas con valores faltantes en '{column}'",
                    "description": f"La columna '{column}' tiene {missing_pct:.1f}% de valores faltantes. "
                                 f"Si la columna es crítica, considerar eliminar filas con valores faltantes.",
                    "action": {
                        "type": "python_code",
                        "code": f"df = df.dropna(subset=['{column}'])",
                        "impact": "Elimina filas con valores faltantes"
                    },
                    "estimated_impact": "Baja - Pérdida de datos pero mejora calidad"
                })
        
        return recommendations
    
    def _recommendations_duplicates(self, issue: Dict, results: Dict) -> List[Dict]:
        """Recomendaciones para duplicados"""
        recommendations = []
        
        duplicates_analysis = results.get("duplicates", {})
        duplicate_pct = duplicates_analysis.get("duplicate_percentage", 0)
        
        if duplicate_pct > 5:
            recommendations.append({
                "type": "REMOVE_DUPLICATES",
                "priority": "HIGH",
                "title": "Eliminar registros duplicados",
                "description": f"Se encontraron {duplicates_analysis.get('n_duplicates', 0)} registros duplicados "
                             f"({duplicate_pct:.1f}% del dataset). Esto puede afectar la calidad del modelo.",
                "action": {
                    "type": "python_code",
                    "code": "df = df.drop_duplicates()\n"
                           "# O mantener primera ocurrencia:\n"
                           "# df = df.drop_duplicates(keep='first')",
                    "impact": "Elimina registros duplicados"
                },
                "estimated_impact": "Alta - Mejora calidad y reduce sesgo"
            })
        
        return recommendations
    
    def _recommendations_outliers(self, issue: Dict, results: Dict) -> List[Dict]:
        """Recomendaciones para outliers"""
        recommendations = []
        
        outliers_analysis = results.get("outliers", {})
        column = issue.get("column")
        
        if column and column in outliers_analysis:
            outlier_pct = outliers_analysis[column].get("outlier_percentage", 0)
            
            if outlier_pct > 10:
                recommendations.append({
                    "type": "HANDLE_OUTLIERS",
                    "priority": "MEDIUM",
                    "title": f"Manejar outliers en '{column}'",
                    "description": f"La columna '{column}' tiene {outlier_pct:.1f}% de outliers. "
                                 f"Considerar capping, winsorization o eliminación.",
                    "action": {
                        "type": "python_code",
                        "code": f"# Opción 1: Capping (limitar a percentiles 5 y 95)\n"
                               f"Q1 = df['{column}'].quantile(0.05)\n"
                               f"Q3 = df['{column}'].quantile(0.95)\n"
                               f"df['{column}'] = df['{column}'].clip(lower=Q1, upper=Q3)\n\n"
                               f"# Opción 2: Eliminar outliers\n"
                               f"# Q1 = df['{column}'].quantile(0.25)\n"
                               f"# Q3 = df['{column}'].quantile(0.75)\n"
                               f"# IQR = Q3 - Q1\n"
                               f"# df = df[(df['{column}'] >= Q1 - 1.5*IQR) & (df['{column}'] <= Q3 + 1.5*IQR)]",
                        "impact": "Limita o elimina outliers"
                    },
                    "estimated_impact": "Media - Mejora robustez del modelo"
                })
        
        return recommendations
    
    def _recommendations_bias(self, issue: Dict, results: Dict) -> List[Dict]:
        """Recomendaciones para sesgo"""
        recommendations = []
        
        bias_analysis = results.get("bias_analysis", {})
        protected_attribute = bias_analysis.get("protected_attribute")
        bias_severity = bias_analysis.get("bias_severity")
        
        if bias_severity in ["HIGH", "CRITICAL"]:
            recommendations.append({
                "type": "MITIGATE_BIAS",
                "priority": "CRITICAL",
                "title": f"Mitigar sesgo en atributo protegido '{protected_attribute}'",
                "description": f"Se detectó sesgo {bias_severity} en el atributo '{protected_attribute}'. "
                             f"Esto puede causar discriminación y violar EU AI Act Art. 10.",
                "action": {
                    "type": "python_code",
                    "code": f"# Opción 1: Remover atributo protegido (si no es necesario)\n"
                           f"# df = df.drop(columns=['{protected_attribute}'])\n\n"
                           f"# Opción 2: Usar técnicas de debiasing\n"
                           f"# from aif360.algorithms.preprocessing import Reweighing\n"
                           f"# rw = Reweighing(unprivileged_groups=[{{'{protected_attribute}': 0}}],\n"
                           f"#                privileged_groups=[{{'{protected_attribute}': 1}}])\n"
                           f"# df_transformed = rw.fit_transform(df)",
                    "impact": "Reduce sesgo en el dataset"
                },
                "estimated_impact": "Crítica - Requerido para cumplimiento EU AI Act"
            })
        
        return recommendations
    
    def _recommendations_leakage(self, issue: Dict, results: Dict) -> List[Dict]:
        """Recomendaciones para label leakage"""
        recommendations = []
        
        leakage_analysis = results.get("leakage_analysis", {})
        leaking_columns = leakage_analysis.get("leaking_columns", [])
        
        if leaking_columns:
            recommendations.append({
                "type": "REMOVE_LEAKING_COLUMNS",
                "priority": "CRITICAL",
                "title": f"Eliminar columnas con label leakage: {', '.join(leaking_columns)}",
                "description": f"Las columnas {leaking_columns} contienen información del target, "
                             f"lo que causa data leakage y resultados no válidos.",
                "action": {
                    "type": "python_code",
                    "code": f"df = df.drop(columns={leaking_columns})",
                    "impact": "Elimina columnas con leakage"
                },
                "estimated_impact": "Crítica - Requerido para validez del modelo"
            })
        
        return recommendations
    
    def _prioritize_recommendations(self, recommendations: List[Dict]) -> List[Dict]:
        """Prioriza recomendaciones por impacto y severidad"""
        priority_order = {"CRITICAL": 0, "HIGH": 1, "MEDIUM": 2, "LOW": 3}
        
        return sorted(
            recommendations,
            key=lambda r: (
                priority_order.get(r.get("priority", "LOW"), 3),
                r.get("estimated_impact", "")
            )
        )
```

### 2. Integrar en Endpoint de Validación

**Archivo:** `bias-detection-service/main.py`

```python
from services.recommendation_generator import RecommendationGenerator

recommendation_generator = RecommendationGenerator()

@app.post("/api/data-quality/validate")
async def validate_data_quality(...):
    # ... validación existente ...
    
    # Generar recomendaciones
    recommendations = recommendation_generator.generate_recommendations(result)
    result["recommendations"] = recommendations
    
    return result
```

### 3. Almacenar en BD

**Modificar StoreEvaluationDelegate:**

```java
// Parsear recomendaciones desde respuesta Python
List<Map<String, Object>> recommendations = parseJson(
    pythonResponse.get("recommendations")
);

// Almacenar en JSONB
quality.setDqlrecommendations(serializeToJson(recommendations));
```

---

## VALIDACIONES

1. ✅ Recomendaciones se generan para cada problema
2. ✅ Recomendaciones están priorizadas
3. ✅ Código de acción es ejecutable
4. ✅ Impacto estimado es realista
5. ✅ Recomendaciones se almacenan en BD

---

## TESTING

```python
def test_generate_recommendations():
    quality_results = {
        "issues": [
            {"category": "Missing Values", "severity": "HIGH", "column": "age"}
        ]
    }
    recommendations = generator.generate_recommendations(quality_results)
    assert len(recommendations) > 0
    assert recommendations[0]["type"] == "REMOVE_COLUMN" or "IMPUTE"
```

---

## DOCUMENTACIÓN

Actualizar:
- `bias-detection-service/README.md` - Recomendaciones automáticas
- Crear guía de uso de recomendaciones

---

## CUMPLIMIENTO EU AI ACT

**Art. 10.2:** Medidas de mitigación
- ✅ Recomendaciones específicas y accionables
- ✅ Priorización por impacto
- ✅ Código de ejemplo para implementar

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** Data Science Team

