# PROMPT: INC-007 - Validación Cruzada FRIA vs Métricas Técnicas (Microservicio Python)

**Incidencia:** INC-007  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 27 (FRIA)  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Python - Microservicio

---

## CONTEXTO

El FRIA se genera solo con datos del wizard (documental) sin validar contra métricas técnicas reales del sistema. Esto permite inconsistencias entre riesgos declarados y realidad técnica.

**Microservicios Involucrados:**
- `leka-fria-generator` (Port 8005) - Generación FRIA
- `leka-bias-detection-service` (Port 8001) - Análisis de sesgos
- `leka-llm-evaluation` (Port 8002) - Evaluación de modelos
- `leka-adversarial-robustness` (Port 8012) - Robustez adversarial

---

## REQUISITOS

1. Ejecutar evaluaciones técnicas automáticas al generar FRIA
2. Comparar riesgos declarados vs métricas técnicas reales
3. Generar alerta si hay inconsistencia significativa
4. Requerir justificación si FRIA documental no coincide con métricas

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear nuevo endpoint en `leka-fria-generator`

**Archivo:** `leka-fria-generator/app/api/routes/fria_validation.py`

```python
"""
Endpoint para validación cruzada FRIA vs métricas técnicas
"""
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Dict, Optional
import logging

from app.services.fria_cross_validation import FriaCrossValidationService
from app.services.technical_metrics_fetcher import TechnicalMetricsFetcher

router = APIRouter()
logger = logging.getLogger(__name__)


class FriaDataDTO(BaseModel):
    """Datos del FRIA desde wizard"""
    project_id: int
    model_id: Optional[int] = None
    dataset_id: Optional[int] = None
    
    # Art. 27.1.d - Riesgos declarados
    declared_risks: List[Dict]
    
    # Art. 27.1.f - Medidas de mitigación declaradas
    declared_mitigations: List[Dict]


class TechnicalMetricsDTO(BaseModel):
    """Métricas técnicas reales del sistema"""
    dataset_quality: Optional[Dict] = None
    model_performance: Optional[Dict] = None
    bias_analysis: Optional[Dict] = None
    adversarial_robustness: Optional[Dict] = None
    prompt_safety: Optional[Dict] = None
    rag_quality: Optional[Dict] = None


class CrossValidationResultDTO(BaseModel):
    """Resultado de validación cruzada"""
    consistency_score: float  # 0.0 - 1.0
    is_consistent: bool
    inconsistencies: List[Dict]
    recommendations: List[str]
    requires_justification: bool


@router.post("/api/fria/cross-validate", response_model=CrossValidationResultDTO)
async def cross_validate_fria(
    fria_data: FriaDataDTO,
    technical_metrics: Optional[TechnicalMetricsDTO] = None
):
    """
    Valida consistencia entre FRIA documental y métricas técnicas reales
    
    Args:
        fria_data: Datos del FRIA desde wizard
        technical_metrics: Métricas técnicas (opcional, se obtienen si no se proporcionan)
    
    Returns:
        CrossValidationResultDTO con resultado de validación
    """
    try:
        logger.info(f"Validación cruzada FRIA iniciada: Project ID={fria_data.project_id}")
        
        # 1. Obtener métricas técnicas si no se proporcionan
        if technical_metrics is None:
            metrics_fetcher = TechnicalMetricsFetcher()
            technical_metrics = await metrics_fetcher.fetch_all_metrics(
                project_id=fria_data.project_id,
                model_id=fria_data.model_id,
                dataset_id=fria_data.dataset_id
            )
        
        # 2. Ejecutar validación cruzada
        validation_service = FriaCrossValidationService()
        result = await validation_service.validate(
            fria_data=fria_data,
            technical_metrics=technical_metrics
        )
        
        logger.info(
            f"Validación cruzada completada: "
            f"Project ID={fria_data.project_id}, "
            f"Consistency={result.consistency_score:.2f}, "
            f"Consistent={result.is_consistent}"
        )
        
        return result
        
    except Exception as e:
        logger.error(f"Error en validación cruzada FRIA: {str(e)}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Error en validación cruzada: {str(e)}")
```

### 2. Crear servicio de obtención de métricas técnicas

**Archivo:** `leka-fria-generator/app/services/technical_metrics_fetcher.py`

```python
"""
Servicio para obtener métricas técnicas de otros microservicios
"""
import httpx
import logging
from typing import Dict, Optional
from app.config import settings

logger = logging.getLogger(__name__)


class TechnicalMetricsFetcher:
    """Obtiene métricas técnicas de microservicios"""
    
    async def fetch_all_metrics(
        self,
        project_id: int,
        model_id: Optional[int] = None,
        dataset_id: Optional[int] = None
    ) -> Dict:
        """
        Obtiene todas las métricas técnicas relevantes
        
        Returns:
            TechnicalMetricsDTO con todas las métricas
        """
        metrics = {}
        
        # 1. Dataset Quality (leka-bias-detection-service)
        if dataset_id:
            metrics['dataset_quality'] = await self._fetch_dataset_quality(dataset_id)
        
        # 2. Model Performance (leka-llm-evaluation)
        if model_id:
            metrics['model_performance'] = await self._fetch_model_performance(model_id)
            metrics['bias_analysis'] = await self._fetch_bias_analysis(model_id)
            metrics['adversarial_robustness'] = await self._fetch_adversarial_robustness(model_id)
        
        # 3. Prompt Safety (leka-prompt-governance)
        # TODO: Obtener prompt_id del proyecto
        # metrics['prompt_safety'] = await self._fetch_prompt_safety(prompt_id)
        
        # 4. RAG Quality (leka-rag-evaluation)
        # TODO: Obtener rag_system_id del proyecto
        # metrics['rag_quality'] = await self._fetch_rag_quality(rag_system_id)
        
        return metrics
    
    async def _fetch_dataset_quality(self, dataset_id: int) -> Dict:
        """Obtiene calidad del dataset desde leka-bias-detection-service"""
        try:
            async with httpx.AsyncClient() as client:
                response = await client.get(
                    f"{settings.BIAS_DETECTION_SERVICE_URL}/api/tabular/evaluate-data-quality",
                    params={"dataset_id": dataset_id},
                    timeout=30.0
                )
                response.raise_for_status()
                return response.json()
        except Exception as e:
            logger.warning(f"Error obteniendo calidad de dataset {dataset_id}: {str(e)}")
            return None
    
    async def _fetch_model_performance(self, model_id: int) -> Dict:
        """Obtiene performance del modelo desde leka-llm-evaluation"""
        try:
            async with httpx.AsyncClient() as client:
                response = await client.get(
                    f"{settings.LLM_EVALUATION_SERVICE_URL}/api/model/performance",
                    params={"model_id": model_id},
                    timeout=30.0
                )
                response.raise_for_status()
                return response.json()
        except Exception as e:
            logger.warning(f"Error obteniendo performance de modelo {model_id}: {str(e)}")
            return None
    
    async def _fetch_bias_analysis(self, model_id: int) -> Dict:
        """Obtiene análisis de sesgos desde leka-bias-detection-service"""
        try:
            async with httpx.AsyncClient() as client:
                response = await client.get(
                    f"{settings.BIAS_DETECTION_SERVICE_URL}/api/model/analyze-bias",
                    params={"model_id": model_id},
                    timeout=30.0
                )
                response.raise_for_status()
                return response.json()
        except Exception as e:
            logger.warning(f"Error obteniendo análisis de sesgos {model_id}: {str(e)}")
            return None
    
    async def _fetch_adversarial_robustness(self, model_id: int) -> Dict:
        """Obtiene robustez adversarial desde leka-adversarial-robustness"""
        try:
            async with httpx.AsyncClient() as client:
                response = await client.get(
                    f"{settings.ADVERSARIAL_SERVICE_URL}/api/model/test-robustness",
                    params={"model_id": model_id},
                    timeout=60.0  # Más tiempo para tests adversariales
                )
                response.raise_for_status()
                return response.json()
        except Exception as e:
            logger.warning(f"Error obteniendo robustez adversarial {model_id}: {str(e)}")
            return None
```

### 3. Crear servicio de validación cruzada

**Archivo:** `leka-fria-generator/app/services/fria_cross_validation.py`

```python
"""
Servicio para validar consistencia entre FRIA documental y métricas técnicas
"""
import logging
from typing import List, Dict
from app.models.fria_cross_validation import CrossValidationResult

logger = logging.getLogger(__name__)


class FriaCrossValidationService:
    """Valida consistencia entre FRIA y métricas técnicas"""
    
    async def validate(
        self,
        fria_data: Dict,
        technical_metrics: Dict
    ) -> CrossValidationResult:
        """
        Valida consistencia entre FRIA documental y métricas técnicas
        
        Returns:
            CrossValidationResult con resultado de validación
        """
        inconsistencies = []
        recommendations = []
        
        # 1. Validar riesgos declarados vs métricas de sesgo
        bias_inconsistencies = self._validate_bias_consistency(
            fria_data['declared_risks'],
            technical_metrics.get('bias_analysis')
        )
        inconsistencies.extend(bias_inconsistencies)
        
        # 2. Validar medidas de mitigación vs implementación real
        mitigation_inconsistencies = self._validate_mitigation_implementation(
            fria_data['declared_mitigations'],
            technical_metrics
        )
        inconsistencies.extend(mitigation_inconsistencies)
        
        # 3. Validar riesgos de precisión vs métricas reales
        accuracy_inconsistencies = self._validate_accuracy_consistency(
            fria_data['declared_risks'],
            technical_metrics.get('model_performance')
        )
        inconsistencies.extend(accuracy_inconsistencies)
        
        # 4. Calcular score de consistencia
        consistency_score = self._calculate_consistency_score(
            len(fria_data['declared_risks']),
            len(inconsistencies)
        )
        
        # 5. Generar recomendaciones
        if inconsistencies:
            recommendations = self._generate_recommendations(inconsistencies)
        
        return CrossValidationResult(
            consistency_score=consistency_score,
            is_consistent=consistency_score >= 0.70,  # Umbral 70%
            inconsistencies=inconsistencies,
            recommendations=recommendations,
            requires_justification=consistency_score < 0.70
        )
    
    def _validate_bias_consistency(
        self,
        declared_risks: List[Dict],
        bias_metrics: Optional[Dict]
    ) -> List[Dict]:
        """Valida consistencia entre riesgos de sesgo declarados y métricas reales"""
        inconsistencies = []
        
        if not bias_metrics:
            return inconsistencies
        
        # Buscar riesgos relacionados con sesgo/discriminación
        bias_risks = [
            r for r in declared_risks
            if 'discrimin' in r.get('description', '').lower() or
               'bias' in r.get('description', '').lower() or
               'sesgo' in r.get('description', '').lower()
        ]
        
        # Si hay riesgos de sesgo declarados pero no se detecta sesgo en métricas
        if bias_risks and bias_metrics.get('bias_score', 0) < 0.10:
            inconsistencies.append({
                'type': 'BIAS_UNDERESTIMATED',
                'description': 'Riesgos de sesgo declarados pero métricas no muestran sesgo significativo',
                'declared_risks': [r.get('description') for r in bias_risks],
                'actual_bias_score': bias_metrics.get('bias_score'),
                'severity': 'MEDIUM'
            })
        
        # Si NO hay riesgos de sesgo declarados pero métricas muestran sesgo alto
        if not bias_risks and bias_metrics.get('bias_score', 0) > 0.15:
            inconsistencies.append({
                'type': 'BIAS_NOT_DECLARED',
                'description': 'Sesgo detectado en métricas pero no declarado en FRIA',
                'actual_bias_score': bias_metrics.get('bias_score'),
                'affected_groups': bias_metrics.get('affected_groups', []),
                'severity': 'HIGH'
            })
        
        return inconsistencies
    
    def _validate_mitigation_implementation(
        self,
        declared_mitigations: List[Dict],
        technical_metrics: Dict
    ) -> List[Dict]:
        """Valida que medidas de mitigación declaradas estén realmente implementadas"""
        inconsistencies = []
        
        for mitigation in declared_mitigations:
            mitigation_desc = mitigation.get('description', '').lower()
            
            # Verificar si medida está implementada según tipo
            if 'auditoría' in mitigation_desc or 'audit' in mitigation_desc:
                # Verificar que hay monitoreo activo
                if not technical_metrics.get('bias_analysis', {}).get('monitoring_active', False):
                    inconsistencies.append({
                        'type': 'MITIGATION_NOT_IMPLEMENTED',
                        'description': f"Medida de mitigación declarada pero no implementada: {mitigation.get('description')}",
                        'mitigation': mitigation,
                        'severity': 'HIGH'
                    })
            
            # Más validaciones según tipo de medida...
        
        return inconsistencies
    
    def _validate_accuracy_consistency(
        self,
        declared_risks: List[Dict],
        performance_metrics: Optional[Dict]
    ) -> List[Dict]:
        """Valida consistencia entre riesgos de precisión y métricas reales"""
        inconsistencies = []
        
        if not performance_metrics:
            return inconsistencies
        
        # Buscar riesgos relacionados con precisión/accuracy
        accuracy_risks = [
            r for r in declared_risks
            if 'precisión' in r.get('description', '').lower() or
               'accuracy' in r.get('description', '').lower() or
               'error' in r.get('description', '').lower()
        ]
        
        actual_accuracy = performance_metrics.get('accuracy', 1.0)
        
        # Si hay riesgos de precisión pero accuracy es alta
        if accuracy_risks and actual_accuracy > 0.90:
            inconsistencies.append({
                'type': 'ACCURACY_UNDERESTIMATED',
                'description': 'Riesgos de precisión declarados pero accuracy real es alta',
                'declared_risks': [r.get('description') for r in accuracy_risks],
                'actual_accuracy': actual_accuracy,
                'severity': 'MEDIUM'
            })
        
        return inconsistencies
    
    def _calculate_consistency_score(
        self,
        total_risks: int,
        total_inconsistencies: int
    ) -> float:
        """Calcula score de consistencia (0.0 - 1.0)"""
        if total_risks == 0:
            return 1.0
        
        # Penalizar por cada inconsistencia
        penalty_per_inconsistency = 0.15
        score = 1.0 - (total_inconsistencies * penalty_per_inconsistency)
        
        return max(0.0, min(1.0, score))
    
    def _generate_recommendations(self, inconsistencies: List[Dict]) -> List[str]:
        """Genera recomendaciones basadas en inconsistencias"""
        recommendations = []
        
        for inconsistency in inconsistencies:
            if inconsistency['type'] == 'BIAS_NOT_DECLARED':
                recommendations.append(
                    f"CRITICAL: Añadir riesgo de sesgo al FRIA. "
                    f"Sesgo detectado: {inconsistency['actual_bias_score']:.2%}"
                )
            elif inconsistency['type'] == 'MITIGATION_NOT_IMPLEMENTED':
                recommendations.append(
                    f"HIGH: Implementar medida de mitigación declarada: "
                    f"{inconsistency['mitigation'].get('description')}"
                )
            # Más recomendaciones...
        
        return recommendations
```

---

## INTEGRACIÓN CON JAVA

En `FriaWizardViewModel.java`, modificar método `generateFria()`:

```java
private void generateFria() {
    // ... código existente para generar FRIA documental ...
    
    // NUEVO: Validación cruzada con métricas técnicas (INC-007)
    try {
        FriaCrossValidationResult validation = friaCrossValidationService.validate(
            friaData,
            projectId
        );
        
        if (validation.getConsistencyScore() < 0.70) {
            // Mostrar alerta de inconsistencia
            showInconsistencyAlert(validation);
            
            // Requerir justificación
            requireJustificationForInconsistency(validation);
        }
        
        // Guardar resultado de validación
        fria.setFriaCrossValidationResult(validation);
        
    } catch (Exception e) {
        log.error("Error en validación cruzada FRIA", e);
        // Continuar con FRIA documental pero marcar como no validado
    }
}
```

---

## PRUEBAS REQUERIDAS

1. **Test 1:** FRIA con riesgos de sesgo pero métricas no muestran sesgo → Debe detectar inconsistencia
2. **Test 2:** FRIA sin riesgos de sesgo pero métricas muestran sesgo alto → Debe detectar inconsistencia CRITICAL
3. **Test 3:** Medidas de mitigación declaradas pero no implementadas → Debe detectar inconsistencia
4. **Test 4:** FRIA consistente con métricas → Debe pasar validación

---

## REFERENCIAS

- **Art. 27 EU AI Act:** FRIA
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-007`

