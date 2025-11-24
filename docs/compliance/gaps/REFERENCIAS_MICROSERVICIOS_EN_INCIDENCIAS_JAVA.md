# REFERENCIAS A MICROSERVICIOS PYTHON EN INCIDENCIAS JAVA

**Fecha:** Diciembre 2025  
**Objetivo:** Consolidar todas las referencias a microservicios Python en los prompts/incidencias de Java

---

## RESUMEN EJECUTIVO

**Total Referencias Encontradas:** 8 referencias directas  
**Microservicios Mencionados:** 4 microservicios Python  
**Prompts Java con Referencias:** 6 prompts

---

## PARTE 1: REFERENCIAS DIRECTAS EN PROMPTS JAVA

### INC-001: Validación Coherencia Modelo-Dataset

**Archivo:** `prompts/java/INC-001_validacion_modelo_dataset.md`  
**Línea:** 215

**Referencia:**
```
2. **Validar calidad del dataset** (usar microservicio `leka-bias-detection-service`)
```

**Microservicio:** `leka-bias-detection-service` (8001)  
**Uso:** Validación de calidad del dataset antes de permitir clasificación como alto riesgo  
**Contexto:** Validaciones adicionales recomendadas  
**Estado:** ⚠️ Recomendación (no implementado en el prompt principal)

---

### INC-002: Sugerencia IA sin Validación de Confianza

**Archivo:** `prompts/java/INC-002_sugerencia_ia_validacion.md`  
**Línea:** 85

**Referencia:**
```java
// TODO: Obtener justificación del microservicio Python
```

**Microservicio:** No especificado explícitamente, pero contexto sugiere `leka-prompt-governance` o `leka-llm-evaluation`  
**Uso:** Obtener justificación de la sugerencia IA  
**Contexto:** Método `getSuggestionJustification()`  
**Estado:** ⚠️ TODO pendiente

**Nota:** Según `RESUMEN_PROMPTS_JAVA.md` línea 64, INC-007 menciona "Integrar llamada a microservicio Python para validación cruzada", pero el prompt específico no se encontró.

---

### INC-004: Justificación de Clasificación sin Validación de Calidad

**Archivo:** `prompts/java/INC-004_justificacion_calidad.md`  
**Línea:** 65-67

**Referencia:**
```java
// 4. Validar con NLP (opcional - usar leka-llm-evaluation)
// JustificationQualityResult quality = llmEvaluationService.evaluateJustificationQuality(justification);
// return quality.getQualityScore() >= 0.70;
```

**Microservicio:** `leka-llm-evaluation` (8002)  
**Uso:** Validación de calidad de justificación con NLP  
**Contexto:** Método `validateJustificationQuality()`  
**Estado:** ⚠️ Comentado (opcional, no implementado)

---

### INC-005: Validación Integridad Datasets

**Archivo:** `prompts/java/INC-005_validacion_integridad_datasets.md`  
**Línea:** 244

**Referencia:**
```
### 6. Modificar Microservicio Python para calcular hash
```

**Microservicio:** No especificado explícitamente, pero contexto sugiere `leka-bias-detection-service`  
**Uso:** Calcular hash SHA-256 de datasets subidos  
**Contexto:** Validación de integridad de datasets  
**Estado:** ⚠️ Pendiente de implementación

---

### INC-005-003: Validación Proactiva de Políticas

**Archivo:** `prompts/java/INC-005-003_validacion_proactiva_politicas.md`  
**Líneas:** 122, 187

**Referencias:**
```java
private RagPolicyScoringService scoringService; // Microservicio Python
// ...
// Llamar a microservicio Python para scoring
```

**Microservicio:** No especificado explícitamente, pero según `INCIDENCIAS_005_EVALUACION_RAG.md` es `leka-adversarial-robustness` (8007)  
**Uso:** Scoring de alineación con políticas del cliente  
**Contexto:** Validación proactiva de políticas antes de generar respuesta  
**Estado:** ⚠️ Pendiente de implementación

**Nota:** Según documentación de incidencias, este servicio está implementado en `leka-adversarial-robustness` con `PolicyScoringService`.

---

### INC-007: Validación Cruzada FRIA vs Métricas Técnicas

**Archivo:** `RESUMEN_PROMPTS_JAVA.md` (línea 63-68)  
**Prompt específico:** No encontrado (posiblemente no generado aún)

**Referencia:**
```
6. ✅ **INC-007:** Validación Cruzada FRIA vs Métricas Técnicas
   - **Descripción:** Integrar llamada a microservicio Python para validación cruzada
```

**Microservicios:** Según `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md` y `MAPEO_PROCESOS_BPMN_MICROSERVICIOS.md`:
- `leka-fria-generator` (8012)
- `leka-bias-detection-service` (8001)
- `leka-llm-evaluation` (8002)
- `leka-adversarial-robustness` (8007)

**Uso:** Validación cruzada de FRIA documental vs métricas técnicas reales  
**Estado:** ✅ Implementado según documentación (vía Delegates BPMN)

---

### INC-010-004: Implementación Real PostMarketMonitoringService

**Archivo:** `prompts/java/INC-010-004_implementacion_real_pmm_service.md`  
**Líneas:** 29, 116, 322, 496-497, 506, 517

**Referencias:**
```
1. Integrar con `leka-bias-detection-service` endpoint `/api/drift/detect`
// ...
// Llamar a leka-bias-detection-service
@Value("${services.leka-bias-detection.url}")
leka-bias-detection:
  url: http://leka-bias-detection-service:8080
1. ✅ Integración con leka-bias-detection-service
- Implementar circuit breakers para llamadas a microservicios
```

**Microservicio:** `leka-bias-detection-service` (8001)  
**Uso:** Detección de drift para Post-Market Monitoring  
**Endpoint:** `/api/drift/detect`  
**Estado:** ⚠️ Pendiente de implementación completa

---

### INC-013: Validación Medidas Mitigación Implementadas

**Archivo:** `prompts/java/INC-013_validacion_medidas.md`  
**Línea:** 123

**Referencia:**
```java
// Verificar que leka-bias-detection está configurado y activo
```

**Microservicio:** `leka-bias-detection-service` (8001)  
**Uso:** Verificar que medidas de mitigación de sesgo estén realmente implementadas  
**Contexto:** Validación de medidas de mitigación declaradas en FRIA  
**Estado:** ⚠️ Pendiente de implementación

---

## PARTE 2: RESUMEN POR MICROSERVICIO

### leka-bias-detection-service (8001)
**Referencias:** 4  
**Prompts:**
- INC-001: Validación calidad dataset
- INC-010-004: Detección de drift (PMM)
- INC-013: Verificación medidas mitigación
- INC-005: Cálculo hash (implícito)

**Endpoints mencionados:**
- `/api/drift/detect` (INC-010-004)

---

### leka-llm-evaluation (8002)
**Referencias:** 1  
**Prompts:**
- INC-004: Validación calidad justificación con NLP

**Uso:** Validación de calidad de texto con NLP (opcional)

---

### leka-adversarial-robustness (8007)
**Referencias:** 1 (implícita)  
**Prompts:**
- INC-005-003: Scoring de políticas

**Uso:** PolicyScoringService para validación proactiva de políticas

---

### leka-fria-generator (8012)
**Referencias:** 1 (en resumen, no en prompt específico)  
**Prompts:**
- INC-007: Validación cruzada FRIA

**Uso:** Generación y validación cruzada de FRIA

---

## PARTE 3: REFERENCIAS IMPLÍCITAS

### INC-007: Validación Cruzada FRIA
Según documentación de auditoría y mapeo BPMN, INC-007 requiere integración con múltiples microservicios:
- `leka-fria-generator` (8012) - Generación FRIA
- `leka-bias-detection-service` (8001) - Métricas de sesgo
- `leka-llm-evaluation` (8002) - Métricas de performance
- `leka-adversarial-robustness` (8007) - Métricas de robustez

**Estado:** ✅ Implementado vía Delegates BPMN (`CrossValidateFriaDelegate`)

---

## PARTE 4: GAPS IDENTIFICADOS

### Prompts con TODOs pendientes:
1. **INC-002:** TODO obtener justificación del microservicio Python
2. **INC-004:** Validación NLP comentada (opcional)
3. **INC-005:** Modificar microservicio Python para calcular hash
4. **INC-005-003:** Llamar a microservicio Python para scoring
5. **INC-010-004:** Integración con `leka-bias-detection-service` pendiente
6. **INC-013:** Verificación de configuración de `leka-bias-detection` pendiente

### Prompts sin referencias pero que deberían tenerlas:
1. **INC-011:** Checklist Validación Modelos - Podría usar `leka-model-wrapper`, `leka-bias-detection-service`, `leka-llm-evaluation`
2. **INC-015:** Verificación Términos OpenAI - Podría usar `leka-model-wrapper`
3. **INC-010:** Umbrales Configurables - Podría usar `leka-bias-detection-service` para obtener métricas

---

## CONCLUSIÓN

### Microservicios más referenciados en incidencias Java:
1. **leka-bias-detection-service** (8001) - 4 referencias
2. **leka-llm-evaluation** (8002) - 1 referencia directa + 1 implícita
3. **leka-adversarial-robustness** (8007) - 1 referencia implícita
4. **leka-fria-generator** (8012) - 1 referencia (en resumen)

### Estado de implementación:
- ✅ **INC-007:** Implementado (vía Delegates BPMN)
- ⚠️ **INC-001, INC-002, INC-004, INC-005, INC-005-003, INC-010-004, INC-013:** Pendientes o con TODOs

### Recomendaciones:
1. Completar integraciones pendientes en los prompts identificados
2. Crear clientes Java formales para microservicios que actualmente se llaman con RestTemplate
3. Documentar explícitamente qué microservicio usar en cada incidencia
4. Actualizar prompts con referencias específicas a endpoints y clientes Java disponibles

---

**Última actualización:** Diciembre 2025

