# INCIDENCIAS Y RECOMENDACIONES - EVALUACIÓN DE MODELOS EXTERNOS
**Fecha:** Noviembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act (Reglamento UE 2024/1689)  
**Documento Relacionado:** `AUDITORIA_EVALUACION_MODELOS_EXTERNOS.md`

---

## 📋 RESUMEN EJECUTIVO

Este documento contiene **incidencias críticas** y **recomendaciones prioritarias** identificadas durante la auditoría del sistema de evaluación de modelos que llaman a APIs externas (ChatGPT, Claude, Vertex AI, etc.).

**Total Incidencias:** 2 críticas, 2 medias  
**Total Recomendaciones:** 4 acciones prioritarias  
**Esfuerzo Estimado Total:** 18 días (3 + 4.5 + 4 + 6.5)  
**Impacto Compliance:** 🔴 Crítico (afecta Art. 19, Art. 15)

---

## 🔴 INCIDENCIAS CRÍTICAS

### INCIDENCIA-001: Manejo Inadecuado de Fallos de API Externa

**Severidad:** 🔴 **CRÍTICA**  
**Prioridad:** **ALTA**  
**Impacto Compliance:** Art. 19 (Registros automáticos)  
**Estado:** ⚠️ **Parcialmente Implementado**

#### Descripción

El sistema actual maneja fallos de APIs externas de forma básica, utilizando valores por defecto cuando la API falla. Esto presenta varios problemas:

1. **No se registra el fallo en log inmutable** (requisito Art. 19)
2. **No se notifica al usuario** del fallo
3. **Métricas por defecto pueden ser engañosas** (falsos positivos)
4. **No hay retry logic** configurable
5. **No hay fallback a API alternativa**

#### Evidencia

**Código Actual:**
```java
// ModelEvaluationService.calculatePerformanceMetrics()
try {
    ResponseEntity<Map> response = restTemplate.exchange(...);
    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        metrics = (Map<String, Double>) response.getBody().get("metrics");
    } else {
        // Fallback: Métricas por defecto (PROBLEMA)
        metrics.put("latencyP50", 100.0);
        metrics.put("overallScore", 90.0);
    }
} catch (Exception e) {
    log.warn("Error obteniendo métricas, usando valores por defecto: {}", e.getMessage());
    // No se registra en ImmutableLog (PROBLEMA)
    metrics.put("overallScore", 90.0);
}
```

**Ubicación:**
- `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/ModelEvaluationService.java`
- Líneas: 116-153

#### Impacto

- **Compliance:** ❌ No cumple Art. 19 (registros completos e inalterables)
- **Confiabilidad:** ❌ Evaluaciones pueden mostrar scores falsos
- **Trazabilidad:** ❌ No se puede investigar por qué falló una evaluación
- **Operacional:** ❌ Usuarios no saben que hubo un fallo

#### Recomendaciones

**1. Implementar Retry Logic con Exponential Backoff**

```java
@Service
public class ModelEvaluationService {
    
    @Retryable(
        value = {HttpServerErrorException.class, ResourceAccessException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public Map<String, Double> callExternalAPI(String endpoint, Map<String, Object> request) {
        // Llamada API con retry automático
    }
    
    @Recover
    public Map<String, Double> recoverFromAPIFailure(Exception e, String endpoint, Map<String, Object> request) {
        log.error("API call failed after retries: {}", endpoint, e);
        // Registrar fallo y retornar null (no valores por defecto)
        return null;
    }
}
```

**2. Registrar Fallos en ImmutableLog**

```java
private void logAPIFailure(String apiProvider, String endpoint, Exception error, int retryCount) {
    ImmutableLog log = new ImmutableLog();
    log.setImlentitytype("MODEL_EVALUATION");
    log.setImlaction("API_CALL_FAILED");
    log.setImlentityid(String.valueOf(evaluationId));
    log.setImluserid("SYSTEM");
    log.setImltimestamp(new Timestamp(System.currentTimeMillis()));
    
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("api_provider", apiProvider);
    metadata.put("api_endpoint", endpoint);
    metadata.put("error_type", error.getClass().getSimpleName());
    metadata.put("error_message", error.getMessage());
    metadata.put("retry_count", retryCount);
    metadata.put("evaluation_id", evaluationId);
    
    log.setImlmetadata(objectMapper.writeValueAsString(metadata));
    log.setImlprevioushash(calculatePreviousHash());
    log.setImlcurrenthash(calculateCurrentHash(log));
    
    businessService.save(log);
}
```

**3. Notificar al Usuario**

```java
private void notifyAPIFailure(Long evaluationId, String apiProvider, Exception error) {
    // Actualizar estado evaluación
    ModelEvaluation evaluation = businessService.findById(ModelEvaluation.class, evaluationId);
    evaluation.setStatus("FAILED");
    evaluation.setDescription("API call failed: " + error.getMessage());
    businessService.save(evaluation);
    
    // Enviar notificación
    notificationService.sendEmail(
        evaluation.getCreatedBy(),
        "Evaluación Fallida - API Externa",
        String.format("La evaluación %s falló debido a error en API %s: %s", 
            evaluation.getEvaluationname(), apiProvider, error.getMessage())
    );
}
```

**4. Fallback a API Alternativa**

```java
public Map<String, Double> evaluateWithFallback(Long modelId, String primaryAPI) {
    List<String> apiProviders = Arrays.asList(primaryAPI, "claude", "vertex", "bedrock");
    
    for (String provider : apiProviders) {
        try {
            return callExternalAPI(provider, buildRequest(modelId));
        } catch (Exception e) {
            log.warn("API {} failed, trying next provider", provider, e);
            continue;
        }
    }
    
    // Si todas fallan, marcar como FAILED (no usar valores por defecto)
    throw new AllAPIsFailedException("All API providers failed");
}
```

**5. Marcar Evaluación como Requiere Revisión Manual**

```java
if (apiCallFailed) {
    evaluation.setStatus("REQUIRES_MANUAL_REVIEW");
    evaluation.setDescription("API call failed. Manual review required.");
    // No establecer métricas por defecto
    evaluation.setOverallscore(null);
}
```

#### Esfuerzo Estimado

- **Desarrollo:** 2 días
- **Testing:** 0.5 días
- **Documentación:** 0.5 días
- **Total:** 3 días

#### Criterios de Aceptación

- ✅ Todos los fallos de API se registran en `ImmutableLog`
- ✅ Usuarios reciben notificación de fallos
- ✅ No se usan valores por defecto engañosos
- ✅ Retry logic funciona con exponential backoff
- ✅ Fallback a API alternativa implementado
- ✅ Evaluaciones fallidas se marcan como "FAILED" o "REQUIRES_MANUAL_REVIEW"

---

### INCIDENCIA-002: Falta Detección Automática de Cambios de Comportamiento en APIs

**Severidad:** 🔴 **CRÍTICA**  
**Prioridad:** **ALTA**  
**Impacto Compliance:** Art. 15 (Precisión, Robustez)  
**Estado:** ❌ **No Implementado**

#### Descripción

El sistema actual no detecta automáticamente cuando una API externa cambia su comportamiento (ej: OpenAI actualiza GPT-4, degradación gradual de calidad). Esto puede llevar a:

1. **Evaluaciones inconsistentes** sin detectar la causa
2. **Degradación silenciosa** de métricas
3. **Falta de trazabilidad** de cambios en comportamiento
4. **No hay alertas** cuando API cambia significativamente

#### Evidencia

**Código Actual:**
```java
// No hay tracking de versión API
// No hay comparación con evaluaciones anteriores
// No hay detección de cambios en distribución de outputs
```

**Ubicación:**
- `ModelEvaluationService.java` - No existe funcionalidad de detección

#### Impacto

- **Compliance:** ⚠️ Parcial - Art. 15 requiere precisión continua
- **Confiabilidad:** ❌ No se detecta degradación de calidad
- **Operacional:** ❌ Problemas solo se detectan cuando usuarios reportan
- **Trazabilidad:** ❌ No se puede correlacionar cambios API con degradación

#### Recomendaciones

**1. Version Tracking de API**

```java
@Column(name = "API_VERSION", length = 50)
private String apiVersion;  // Ej: "2024-11-20", "gpt-4-turbo-preview"

@Column(name = "API_MODEL", length = 100)
private String apiModel;    // Ej: "gpt-4-turbo-preview"

@Column(name = "API_BEHAVIOR_HASH", length = 64)
private String apiBehaviorHash;  // SHA-256 hash de distribución outputs
```

**2. Cálculo de Hash de Comportamiento**

```java
private String calculateBehaviorHash(List<String> outputs) {
    // Calcular estadísticas de distribución
    Map<String, Object> stats = new HashMap<>();
    stats.put("avg_length", calculateAvgLength(outputs));
    stats.put("token_distribution", calculateTokenDistribution(outputs));
    stats.put("sentiment_distribution", calculateSentimentDistribution(outputs));
    stats.put("top_tokens", getTopTokens(outputs, 100));
    
    // Calcular hash
    String json = objectMapper.writeValueAsString(stats);
    return DigestUtils.sha256Hex(json);
}
```

**3. Comparación con Evaluaciones Anteriores**

```java
public BehaviorChange detectBehaviorChange(Long modelId, String currentBehaviorHash) {
    // Obtener última evaluación exitosa
    ModelEvaluation lastEvaluation = findLastSuccessfulEvaluation(modelId);
    
    if (lastEvaluation == null || lastEvaluation.getApiBehaviorHash() == null) {
        return BehaviorChange.NO_BASELINE;
    }
    
    if (!lastEvaluation.getApiBehaviorHash().equals(currentBehaviorHash)) {
        // Calcular diferencia
        double similarity = calculateSimilarity(
            lastEvaluation.getApiBehaviorHash(),
            currentBehaviorHash
        );
        
        if (similarity < 0.90) {  // Cambio > 10%
            return BehaviorChange.SIGNIFICANT_CHANGE;
        } else if (similarity < 0.95) {  // Cambio 5-10%
            return BehaviorChange.MODERATE_CHANGE;
        } else {
            return BehaviorChange.MINOR_CHANGE;
        }
    }
    
    return BehaviorChange.NO_CHANGE;
}
```

**4. Alertas Automáticas**

```java
private void alertBehaviorChange(Long evaluationId, BehaviorChange change) {
    ModelEvaluation evaluation = businessService.findById(ModelEvaluation.class, evaluationId);
    
    if (change == BehaviorChange.SIGNIFICANT_CHANGE) {
        // Alerta crítica
        notificationService.sendAlert(
            "CRITICAL",
            "Significant API Behavior Change Detected",
            String.format("Evaluation %s detected significant change in API behavior. Manual review required.", 
                evaluation.getEvaluationname())
        );
        
        // Marcar evaluación para revisión
        evaluation.setStatus("REQUIRES_MANUAL_REVIEW");
        evaluation.setDescription("Significant API behavior change detected");
    } else if (change == BehaviorChange.MODERATE_CHANGE) {
        // Alerta warning
        notificationService.sendAlert(
            "WARNING",
            "Moderate API Behavior Change Detected",
            String.format("Evaluation %s detected moderate change in API behavior.", 
                evaluation.getEvaluationname())
        );
    }
    
    businessService.save(evaluation);
}
```

**5. A/B Testing Automático**

```java
public ComparisonResult compareAPIVersions(String modelId, String oldVersion, String newVersion) {
    // Ejecutar evaluación en ambas versiones
    Map<String, Double> oldMetrics = evaluateWithAPI(modelId, oldVersion);
    Map<String, Double> newMetrics = evaluateWithAPI(modelId, newVersion);
    
    // Comparar métricas
    ComparisonResult result = new ComparisonResult();
    result.setAccuracyChange(newMetrics.get("accuracy") - oldMetrics.get("accuracy"));
    result.setLatencyChange(newMetrics.get("latencyP50") - oldMetrics.get("latencyP50"));
    result.setOverallChange(newMetrics.get("overallScore") - oldMetrics.get("overallScore"));
    
    // Alertar si degradación significativa
    if (result.getOverallChange() < -0.05) {  // Degradación > 5%
        alertAPIDegradation(modelId, oldVersion, newVersion, result);
    }
    
    return result;
}
```

#### Esfuerzo Estimado

- **Desarrollo:** 3 días
- **Testing:** 1 día
- **Documentación:** 0.5 días
- **Total:** 4.5 días

#### Criterios de Aceptación

- ✅ Versión API se almacena en cada evaluación
- ✅ Hash de comportamiento se calcula y almacena
- ✅ Comparación automática con evaluaciones anteriores
- ✅ Alertas se envían cuando cambio > umbral
- ✅ A/B testing disponible para comparar versiones
- ✅ Dashboard muestra histórico de cambios de comportamiento

---

## 🟡 INCIDENCIAS MEDIAS

### INCIDENCIA-003: Falta Dashboard UI para Histórico de Evaluaciones

**Severidad:** 🟡 **MEDIA**  
**Prioridad:** **MEDIA**  
**Impacto Compliance:** Art. 12 (Mantenimiento de registros - accesibilidad)  
**Estado:** ❌ **No Implementado**

#### Descripción

Aunque el histórico de evaluaciones está disponible vía SQL y APIs, no existe una interfaz de usuario (UI) para visualizar y analizar el histórico de evaluaciones por versión de modelo.

#### Evidencia

**Disponible:**
- ✅ SQL queries documentadas
- ✅ Vista `ModelValidationHistory`
- ✅ APIs REST (pendiente implementación completa)

**Falta:**
- ❌ Vista ZUL para dashboard
- ❌ ViewModel para gestión
- ❌ Gráficos temporales de scores
- ❌ Comparación visual entre versiones
- ❌ Exportación a PDF/CSV

#### Impacto

- **Usabilidad:** ❌ Usuarios no pueden consultar histórico fácilmente
- **Transparencia:** ❌ Falta visibilidad de evolución de modelos
- **Compliance:** ⚠️ Parcial - Art. 12 requiere accesibilidad

#### Recomendaciones

**1. Crear ViewModel**

```java
@ViewModel
public class ModelEvaluationHistoryViewModel extends MasterPage {
    
    private Model selectedModel;
    private List<ModelEvaluation> evaluations;
    private ModelVersion selectedVersion;
    
    @Command
    @NotifyChange({"evaluations", "chartData"})
    public void loadEvaluationHistory() {
        if (selectedModel != null) {
            evaluations = evaluationService.findByModelId(
                selectedModel.getIdxmodel(),
                selectedVersion != null ? selectedVersion.getIdxmodelversion() : null
            );
        }
    }
    
    public ChartData getChartData() {
        // Preparar datos para gráfico temporal
        return ChartData.fromEvaluations(evaluations);
    }
}
```

**2. Crear Vista ZUL**

```xml
<window title="Histórico de Evaluaciones" apply="org.zkoss.bind.BindComposer">
    <vbox>
        <!-- Filtros -->
        <hbox>
            <combobox model="@bind(vm.models)" selectedItem="@bind(vm.selectedModel)"/>
            <combobox model="@bind(vm.versions)" selectedItem="@bind(vm.selectedVersion)"/>
            <button label="Cargar" onClick="@command('loadEvaluationHistory')"/>
        </hbox>
        
        <!-- Gráfico temporal -->
        <chart id="scoreChart" type="line" data="@bind(vm.chartData)"/>
        
        <!-- Tabla de evaluaciones -->
        <listbox model="@bind(vm.evaluations)">
            <listhead>
                <listheader label="Fecha"/>
                <listheader label="Tipo"/>
                <listheader label="Score"/>
                <listheader label="Estado"/>
            </listhead>
        </listbox>
    </vbox>
</window>
```

**3. Implementar Exportación**

```java
@Command
public void exportToPDF() {
    // Generar PDF con histórico
    PDFGenerator.generateEvaluationHistoryPDF(selectedModel, evaluations);
}

@Command
public void exportToCSV() {
    // Generar CSV con histórico
    CSVGenerator.generateEvaluationHistoryCSV(selectedModel, evaluations);
}
```

#### Esfuerzo Estimado

- **Desarrollo:** 3 días
- **Testing:** 0.5 días
- **Documentación:** 0.5 días
- **Total:** 4 días

#### Criterios de Aceptación

- ✅ Vista ZUL muestra histórico de evaluaciones
- ✅ Filtros por modelo y versión funcionan
- ✅ Gráfico temporal muestra evolución de scores
- ✅ Comparación entre versiones disponible
- ✅ Exportación a PDF y CSV funciona

---

### INCIDENCIA-004: Falta Visualización de Trazabilidad en UI

**Severidad:** 🟡 **MEDIA**  
**Prioridad:** **BAJA**  
**Impacto Compliance:** Art. 12 (Mantenimiento de registros - accesibilidad)  
**Estado:** ❌ **No Implementado**

#### Descripción

Aunque la trazabilidad completa existe en base de datos (`ExperimentLineage`, `ModelPrediction`), no hay una interfaz visual para explorar las relaciones modelo → prompts → datasets → outputs.

#### Evidencia

**Disponible:**
- ✅ Entidad `ExperimentLineage` con provenance graph
- ✅ Procedimientos SQL para consulta
- ✅ APIs REST (parcial)

**Falta:**
- ❌ Componente visual para grafo
- ❌ Navegación interactiva
- ❌ Filtros y búsqueda

#### Impacto

- **Usabilidad:** ❌ Dificulta exploración de dependencias
- **Transparencia:** ❌ Falta visibilidad de relaciones

#### Recomendaciones

**1. Implementar Componente de Grafo**

```typescript
// Componente React con D3.js o Cytoscape.js
import { GraphVisualization } from './GraphVisualization';

export const ModelLineageGraph = ({ modelId }) => {
    const [graphData, setGraphData] = useState(null);
    
    useEffect(() => {
        fetch(`/api/v1/models/${modelId}/lineage`)
            .then(res => res.json())
            .then(data => setGraphData(data));
    }, [modelId]);
    
    return <GraphVisualization data={graphData} />;
};
```

#### Esfuerzo Estimado

- **Desarrollo:** 5 días
- **Testing:** 1 día
- **Documentación:** 0.5 días
- **Total:** 6.5 días

#### Criterios de Aceptación

- ✅ Grafo visual muestra relaciones modelo → prompts → datasets → outputs
- ✅ Navegación interactiva funciona
- ✅ Filtros y búsqueda disponibles
- ✅ Exportación de grafo a imagen/PDF

---

## 📊 RESUMEN DE RECOMENDACIONES

### Priorización

| Incidencia | Severidad | Esfuerzo | Prioridad | Impacto Compliance |
|------------|-----------|----------|-----------|-------------------|
| **INCIDENCIA-001** | 🔴 Crítica | 3 días | **ALTA** | Art. 19 |
| **INCIDENCIA-002** | 🔴 Crítica | 4.5 días | **ALTA** | Art. 15 |
| **INCIDENCIA-003** | 🟡 Media | 4 días | **MEDIA** | Art. 12 |
| **INCIDENCIA-004** | 🟡 Media | 6.5 días | **BAJA** | Art. 12 |

### Plan de Acción Recomendado

#### Fase 1: Críticas (Semanas 1-2)

**Semana 1:**
- Día 1-3: Implementar INCIDENCIA-001 (Manejo fallos API)
- Día 4-5: Testing y documentación

**Semana 2:**
- Día 1-3: Implementar INCIDENCIA-002 (Detección cambios API)
- Día 4-5: Testing y documentación

#### Fase 2: Mejoras UX (Semana 3-4)

**Semana 3:**
- Día 1-4: Implementar INCIDENCIA-003 (Dashboard histórico)

**Semana 4:**
- Día 1-3: Testing y refinamiento
- Día 4-5: Documentación

#### Fase 3: Opcionales (Semana 5+)

- Implementar INCIDENCIA-004 (Visualización trazabilidad) cuando haya disponibilidad

### Métricas de Éxito

**INCIDENCIA-001:**
- ✅ 100% de fallos API registrados en ImmutableLog
- ✅ 0 evaluaciones con métricas por defecto engañosas
- ✅ Tiempo medio de detección fallos < 5 minutos

**INCIDENCIA-002:**
- ✅ 100% de cambios significativos detectados automáticamente
- ✅ Tiempo medio de detección cambios < 24 horas
- ✅ 0 degradaciones silenciosas no detectadas

**INCIDENCIA-003:**
- ✅ Dashboard accesible para 100% de usuarios
- ✅ Tiempo de consulta histórico < 2 segundos
- ✅ Exportación funciona en 100% de casos

---

## 📝 NOTAS ADICIONALES

### Dependencias Técnicas

**INCIDENCIA-001:**
- Spring Retry (`@Retryable`)
- ImmutableLog entity (ya existe)
- NotificationService (ya existe)

**INCIDENCIA-002:**
- Apache Commons Codec (para SHA-256)
- Jackson ObjectMapper (ya existe)
- AlertService (crear si no existe)

**INCIDENCIA-003:**
- ZK Framework (ya existe)
- Chart.js o similar (añadir dependencia)
- PDF/CSV generators (crear o usar librerías)

**INCIDENCIA-004:**
- React o Vue.js (si se usa frontend moderno)
- D3.js o Cytoscape.js
- GraphQL o REST API para datos

### Consideraciones de Compliance

Todas las incidencias están alineadas con requisitos EU AI Act:

- **Art. 19:** Requiere registros completos e inalterables (INCIDENCIA-001)
- **Art. 15:** Requiere precisión y robustez continuas (INCIDENCIA-002)
- **Art. 12:** Requiere accesibilidad de registros (INCIDENCIA-003, INCIDENCIA-004)

### Riesgos si No se Implementan

**INCIDENCIA-001:**
- ❌ No compliance Art. 19
- ❌ Evaluaciones no confiables
- ❌ Imposible investigar incidentes

**INCIDENCIA-002:**
- ⚠️ Degradación silenciosa de calidad
- ⚠️ No compliance Art. 15 (precisión continua)
- ⚠️ Problemas detectados tarde

**INCIDENCIA-003:**
- ⚠️ Baja usabilidad
- ⚠️ Parcial compliance Art. 12 (accesibilidad)

**INCIDENCIA-004:**
- ⚠️ Baja usabilidad
- ⚠️ Parcial compliance Art. 12 (accesibilidad)

---

**Fin del Documento de Incidencias y Recomendaciones**

**Fecha:** Noviembre 2025  
**Versión:** 1.0  
**Próxima Revisión:** Después de implementación Fase 1

