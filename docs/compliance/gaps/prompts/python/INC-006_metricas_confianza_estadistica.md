# PROMPT: INC-006-DS - Métricas de Confianza Estadística

**Incidencia:** INC-006-DS  
**Prioridad:** 🟡 MEDIUM  
**Artículo EU AI Act:** Art. 15.1 (precisión)  
**Esfuerzo Estimado:** 3-4 días  
**Tipo:** Python - Microservicio + Java - Backend

---

## CONTEXTO

Las evaluaciones no incluyen intervalos de confianza o p-values para las métricas calculadas, lo que limita la interpretación estadística de los resultados.

**Ubicación Actual:**
- `DQLDATASETQUALITIES` - No hay campos para intervalos de confianza
- Métricas de bias no incluyen p-values de tests estadísticos

---

## REQUISITOS

1. Añadir intervalos de confianza (95%, 99%) para scores de calidad
2. Añadir p-values para tests de bias (KS test, Chi-square)
3. Nivel de confianza en decisión (APPROVED/REJECTED)
4. Campos en BD: `DQLCONFIDENCEINTERVAL` (JSONB), `DQLPVALUE` (DECIMAL)

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar Microservicio Python

**Archivo:** `bias-detection-service/services/data_quality_service.py`

```python
import numpy as np
from scipy import stats
from typing import Dict, List, Tuple
import json

class DataQualityService:
    """
    Service for validating data quality with statistical confidence
    """
    
    def validate_dataset(
        self,
        df: pd.DataFrame,
        target_column: Optional[str] = None,
        confidence_level: float = 0.95
    ) -> Dict:
        """
        Comprehensive data quality validation with confidence intervals
        
        Args:
            df: Dataset to validate
            target_column: Name of target column (if classification)
            confidence_level: Confidence level (0.95 for 95%, 0.99 for 99%)
            
        Returns:
            Dictionary with quality metrics, confidence intervals, and p-values
        """
        results = {
            "validation_date": datetime.now().isoformat(),
            "n_samples": len(df),
            "n_features": len(df.columns),
            "confidence_level": confidence_level,
            "issues": [],
            "quality_score": 0,
            "confidence_intervals": {},
            "p_values": {}
        }
        
        # Calcular métricas con intervalos de confianza
        completeness_ci = self._calculate_completeness_with_ci(df, confidence_level)
        results["completeness_score"] = completeness_ci["mean"]
        results["confidence_intervals"]["completeness"] = {
            "lower": completeness_ci["ci_lower"],
            "upper": completeness_ci["ci_upper"],
            "level": confidence_level
        }
        
        # Duplicados con test estadístico
        duplicates_result = self._analyze_duplicates_with_test(df)
        results["duplicates"] = duplicates_result["analysis"]
        results["p_values"]["duplicates"] = duplicates_result["p_value"]
        
        # Outliers con test
        outliers_result = self._detect_outliers_with_test(df)
        results["outliers"] = outliers_result["analysis"]
        results["p_values"]["outliers"] = outliers_result["p_value"]
        
        # Calcular score global con intervalo de confianza
        quality_score_ci = self._calculate_quality_score_with_ci(results, confidence_level)
        results["quality_score"] = quality_score_ci["mean"]
        results["confidence_intervals"]["quality_score"] = {
            "lower": quality_score_ci["ci_lower"],
            "upper": quality_score_ci["ci_upper"],
            "level": confidence_level
        }
        
        # Determinar nivel de confianza en decisión
        results["decision_confidence"] = self._determine_decision_confidence(
            results["quality_score"],
            quality_score_ci["ci_lower"],
            quality_score_ci["ci_upper"]
        )
        
        return results
    
    def _calculate_completeness_with_ci(
        self,
        df: pd.DataFrame,
        confidence_level: float
    ) -> Dict:
        """
        Calcula completitud con intervalo de confianza usando bootstrap
        """
        n_samples = len(df)
        completeness_scores = []
        
        # Bootstrap sampling para calcular CI
        n_bootstrap = 1000
        for _ in range(n_bootstrap):
            sample = df.sample(n=min(n_samples, 1000), replace=True)
            completeness = 1 - (sample.isna().sum().sum() / (len(sample) * len(sample.columns)))
            completeness_scores.append(completeness)
        
        mean_completeness = np.mean(completeness_scores)
        std_completeness = np.std(completeness_scores)
        
        # Calcular intervalo de confianza
        alpha = 1 - confidence_level
        ci_lower, ci_upper = np.percentile(
            completeness_scores,
            [alpha/2 * 100, (1 - alpha/2) * 100]
        )
        
        return {
            "mean": mean_completeness,
            "std": std_completeness,
            "ci_lower": ci_lower,
            "ci_upper": ci_upper
        }
    
    def _analyze_duplicates_with_test(self, df: pd.DataFrame) -> Dict:
        """
        Analiza duplicados con test estadístico
        """
        n_duplicates = df.duplicated().sum()
        n_total = len(df)
        duplicate_rate = n_duplicates / n_total
        
        # Test binomial: ¿es la tasa de duplicados significativamente diferente de 0?
        # H0: duplicate_rate = 0
        # H1: duplicate_rate > 0
        from scipy.stats import binomtest
        
        p_value = binomtest(
            n_duplicates,
            n_total,
            p=0.0,  # H0: no hay duplicados
            alternative='greater'
        ).pvalue
        
        return {
            "analysis": {
                "n_duplicates": n_duplicates,
                "duplicate_percentage": duplicate_rate * 100
            },
            "p_value": p_value,
            "significant": p_value < 0.05
        }
    
    def _detect_outliers_with_test(
        self,
        df: pd.DataFrame,
        numerical_features: List[str]
    ) -> Dict:
        """
        Detecta outliers con test estadístico (KS test)
        """
        outlier_results = {}
        p_values = {}
        
        for feature in numerical_features:
            if feature not in df.columns:
                continue
            
            values = df[feature].dropna()
            if len(values) < 30:
                continue
            
            # IQR method
            Q1 = values.quantile(0.25)
            Q3 = values.quantile(0.75)
            IQR = Q3 - Q1
            
            if IQR == 0:
                continue
            
            lower_bound = Q1 - 1.5 * IQR
            upper_bound = Q3 + 1.5 * IQR
            
            outliers = values[(values < lower_bound) | (values > upper_bound)]
            n_outliers = len(outliers)
            
            # Test: ¿la distribución es normal? (KS test)
            # Si no es normal, outliers pueden ser esperados
            from scipy.stats import kstest, norm
            
            # Normalizar valores
            normalized = (values - values.mean()) / values.std()
            
            ks_statistic, p_value = kstest(
                normalized,
                norm.cdf
            )
            
            outlier_results[feature] = {
                "n_outliers": n_outliers,
                "outlier_percentage": (n_outliers / len(values)) * 100,
                "is_normal": p_value > 0.05  # Si p > 0.05, distribución es normal
            }
            
            p_values[feature] = p_value
        
        # P-value agregado (promedio)
        avg_p_value = np.mean(list(p_values.values())) if p_values else 1.0
        
        return {
            "analysis": outlier_results,
            "p_value": avg_p_value,
            "significant": avg_p_value < 0.05
        }
    
    def _calculate_quality_score_with_ci(
        self,
        results: Dict,
        confidence_level: float
    ) -> Dict:
        """
        Calcula score global con intervalo de confianza usando Monte Carlo
        """
        # Simular variación en componentes del score
        n_simulations = 1000
        scores = []
        
        for _ in range(n_simulations):
            # Simular variación en cada componente
            completeness = np.random.normal(
                results["completeness_score"],
                results["confidence_intervals"]["completeness"]["std"] if "std" in results["confidence_intervals"]["completeness"] else 0.01
            )
            
            # Calcular score simulado
            score = completeness * 0.30  # Peso completitud
            # Añadir otros componentes...
            
            scores.append(max(0, min(100, score * 100)))
        
        mean_score = np.mean(scores)
        ci_lower, ci_upper = np.percentile(
            scores,
            [(1 - confidence_level)/2 * 100, (1 + confidence_level)/2 * 100]
        )
        
        return {
            "mean": mean_score,
            "ci_lower": ci_lower,
            "ci_upper": ci_upper
        }
    
    def _determine_decision_confidence(
        self,
        quality_score: float,
        ci_lower: float,
        ci_upper: float
    ) -> str:
        """
        Determina nivel de confianza en decisión basado en intervalo
        """
        threshold_approved = 75.0
        threshold_rejected = 60.0
        
        # Si todo el intervalo está por encima del umbral, alta confianza
        if ci_lower >= threshold_approved:
            return "HIGH"  # Alta confianza en APPROVED
        elif ci_upper < threshold_rejected:
            return "HIGH"  # Alta confianza en REJECTED
        elif ci_lower >= threshold_rejected and ci_upper < threshold_approved:
            return "MEDIUM"  # Confianza media, requiere revisión
        else:
            return "LOW"  # Baja confianza, intervalo cruza umbrales
```

### 2. Modificar Endpoint para Incluir Métricas de Confianza

**Archivo:** `bias-detection-service/main.py`

```python
@app.post("/api/data-quality/validate")
async def validate_data_quality(
    file: UploadFile = File(...),
    confidence_level: float = Form(0.95),
    ...
):
    """
    Validación de calidad con métricas de confianza estadística
    """
    contents = await file.read()
    df = pd.read_csv(StringIO(contents.decode('utf-8')))
    
    # Validar con confianza
    result = data_quality_service.validate_dataset(
        df,
        target_column=target_column,
        confidence_level=confidence_level
    )
    
    return result
```

### 3. Extender Entidad Java

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/DatasetQuality.java`

```java
// Intervalos de confianza (JSONB)
@Column(name = "DQLCONFIDENCEINTERVAL", columnDefinition = "JSONB")
private String dqlconfidenceinterval;  // JSON con intervalos por métrica

// P-values (JSONB)
@Column(name = "DQLPVALUE", columnDefinition = "JSONB")
private String dqlpvalue;  // JSON con p-values por test

// Nivel de confianza en decisión
@Column(name = "DQLDECISIONCONFIDENCE", length = 20)
private String dqldecisionconfidence;  // HIGH, MEDIUM, LOW

// Nivel de confianza usado (0.95, 0.99)
@Column(name = "DQLCONFIDENCELEVEL")
private Double dqlconfidencelevel = 0.95;
```

### 4. Script SQL

**Archivo:** `sql-scripts/patches/13_dataset_quality_confidence_fields.sql`

```sql
-- Añadir campos de confianza estadística
ALTER TABLE DQLDATASETQUALITY 
ADD COLUMN IF NOT EXISTS DQLCONFIDENCEINTERVAL JSONB,
ADD COLUMN IF NOT EXISTS DQLPVALUE JSONB,
ADD COLUMN IF NOT EXISTS DQLDECISIONCONFIDENCE VARCHAR(20),
ADD COLUMN IF NOT EXISTS DQLCONFIDENCELEVEL DECIMAL(3,2) DEFAULT 0.95;

COMMENT ON COLUMN DQLDATASETQUALITY.DQLCONFIDENCEINTERVAL IS 'Intervalos de confianza por métrica (JSON)';
COMMENT ON COLUMN DQLDATASETQUALITY.DQLPVALUE IS 'P-values de tests estadísticos (JSON)';
COMMENT ON COLUMN DQLDATASETQUALITY.DQLDECISIONCONFIDENCE IS 'Nivel de confianza en decisión: HIGH, MEDIUM, LOW';
COMMENT ON COLUMN DQLDATASETQUALITY.DQLCONFIDENCELEVEL IS 'Nivel de confianza usado (0.95 para 95%, 0.99 para 99%)';
```

---

## VALIDACIONES

1. ✅ Intervalos de confianza se calculan correctamente
2. ✅ P-values se calculan para tests estadísticos
3. ✅ Nivel de confianza en decisión se determina correctamente
4. ✅ Datos se almacenan en BD
5. ✅ API retorna métricas de confianza

---

## TESTING

```python
def test_confidence_intervals():
    """Test que intervalos de confianza se calculan"""
    result = service.validate_dataset(df, confidence_level=0.95)
    assert "confidence_intervals" in result
    assert "completeness" in result["confidence_intervals"]
    assert result["confidence_intervals"]["completeness"]["lower"] < result["completeness_score"]
    assert result["confidence_intervals"]["completeness"]["upper"] > result["completeness_score"]

def test_p_values():
    """Test que p-values se calculan"""
    result = service.validate_dataset(df)
    assert "p_values" in result
    assert "duplicates" in result["p_values"]
    assert 0 <= result["p_values"]["duplicates"] <= 1
```

---

## DOCUMENTACIÓN

Actualizar:
- `bias-detection-service/README.md` - Métricas de confianza
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Confianza estadística

---

## CUMPLIMIENTO EU AI ACT

**Art. 15.1:** Precisión
- ✅ Intervalos de confianza permiten evaluar precisión de métricas
- ✅ P-values permiten determinar significancia estadística
- ✅ Nivel de confianza en decisión mejora calidad de aprobaciones

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** Data Science Team

