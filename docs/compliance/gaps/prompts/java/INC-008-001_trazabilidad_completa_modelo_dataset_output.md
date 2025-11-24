# PROMPT: INC-008-001 - Trazabilidad Completa Modelo-Dataset-Output No Integrada
## EU AI Act Art. 19 - Registro Inmutable

**Incidencia:** INC-008-001  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 19.1 (registros completos)  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java - Backend + SQL

---

## CONTEXTO

El sistema tiene componentes separados para linaje de modelos (`TRNEXPERIMENTLINEAGE`), predicciones (`srv_prediction`), y logs inmutables (`IMLIMMUTABLELOGS`), pero **no existe una vista unificada** que muestre la traza completa desde modelo → dataset → entrenamiento → evaluación → despliegue → predicción → output.

**Ubicación Actual:**
- `ImmutableLoggingBusinessService.java` - Servicio de logs inmutables
- `TRNEXPERIMENTLINEAGE` - Tabla de linaje de modelos
- `srv_prediction` - Tabla de predicciones
- `IMLIMMUTABLELOGS` - Tabla de logs inmutables

---

## REQUISITOS

1. **Crear vista SQL unificada** `v_complete_model_trace` que consolide:
   - Logs ImmutableLogs con `IMLENTITYTYPE = 'MODEL'` y `IMLACTION = 'TRAIN'`
   - Logs ImmutableLogs con `IMLENTITYTYPE = 'DATASET'` y `IMLACTION = 'USE'`
   - Logs ImmutableLogs con `IMLENTITYTYPE = 'PREDICTION'` y `IMLACTION = 'PREDICT'`
   - Unir mediante `IMLENTITYID` y referencias en `IMLDATA` (JSON)

2. **Implementar endpoint REST:**
   ```
   GET /api/v1/compliance/trace/model/{modelId}
   Response: {
     "model_id": "...",
     "datasets": [...],
     "training_events": [...],
     "evaluation_events": [...],
     "deployment_events": [...],
     "predictions": [...],
     "outputs": [...]
   }
   ```

3. **Crear ViewModel ZK** para visualización en UI:
   - Timeline interactivo mostrando traza completa
   - Filtros por fecha, acción, entidad
   - Exportación a PDF/CSV

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Vista SQL Unificada

**Archivo:** `nocode.service.entitys/src/main/resources/sql/views/v_complete_model_trace.sql`

```sql
-- Vista unificada de trazabilidad completa modelo-dataset-output
-- EU AI Act Art. 19.1 - Registros completos
CREATE OR REPLACE VIEW v_complete_model_trace AS
WITH model_logs AS (
    SELECT 
        IDXIMMUTABLELOG,
        IMLTIMESTAMP,
        IMLTIMESTAMPEPOCH,
        IMLENTITYID AS model_id,
        IMLUSERID,
        IMLUSERNAME,
        IMLACTION,
        IMLDATA,
        IMLCURRENTHASH,
        IMLPREVIOUSHASH
    FROM IMLIMMUTABLELOGS
    WHERE IMLENTITYTYPE = 'MODEL'
),
dataset_logs AS (
    SELECT 
        IDXIMMUTABLELOG,
        IMLTIMESTAMP,
        IMLTIMESTAMPEPOCH,
        IMLENTITYID AS dataset_id,
        IMLUSERID,
        IMLUSERNAME,
        IMLACTION,
        IMLDATA,
        IMLDATA->>'model_id' AS referenced_model_id,
        IMLCURRENTHASH,
        IMLPREVIOUSHASH
    FROM IMLIMMUTABLELOGS
    WHERE IMLENTITYTYPE = 'DATASET'
),
prediction_logs AS (
    SELECT 
        IDXIMMUTABLELOG,
        IMLTIMESTAMP,
        IMLTIMESTAMPEPOCH,
        IMLENTITYID AS prediction_id,
        IMLUSERID,
        IMLUSERNAME,
        IMLACTION,
        IMLDATA,
        IMLDATA->>'model_id' AS referenced_model_id,
        IMLDATA->>'input' AS prediction_input,
        IMLDATA->>'output' AS prediction_output,
        IMLCURRENTHASH,
        IMLPREVIOUSHASH
    FROM IMLIMMUTABLELOGS
    WHERE IMLENTITYTYPE = 'PREDICTION'
)
SELECT 
    m.IDXIMMUTABLELOG AS model_log_id,
    m.IMLTIMESTAMP AS model_timestamp,
    m.model_id,
    m.IMLACTION AS model_action,
    m.IMLUSERNAME AS model_user,
    m.IMLDATA AS model_data,
    m.IMLCURRENTHASH AS model_hash,
    
    -- Dataset relacionado
    d.IDXIMMUTABLELOG AS dataset_log_id,
    d.IMLTIMESTAMP AS dataset_timestamp,
    d.dataset_id,
    d.IMLACTION AS dataset_action,
    d.IMLDATA AS dataset_data,
    d.IMLCURRENTHASH AS dataset_hash,
    
    -- Predicción relacionada
    p.IDXIMMUTABLELOG AS prediction_log_id,
    p.IMLTIMESTAMP AS prediction_timestamp,
    p.prediction_id,
    p.IMLACTION AS prediction_action,
    p.prediction_input,
    p.prediction_output,
    p.IMLDATA AS prediction_data,
    p.IMLCURRENTHASH AS prediction_hash,
    
    -- Metadata de trazabilidad
    CASE 
        WHEN d.IDXIMMUTABLELOG IS NOT NULL AND p.IDXIMMUTABLELOG IS NOT NULL THEN 'COMPLETE'
        WHEN d.IDXIMMUTABLELOG IS NOT NULL THEN 'PARTIAL'
        ELSE 'INCOMPLETE'
    END AS trace_status
    
FROM model_logs m
LEFT JOIN dataset_logs d ON d.referenced_model_id::TEXT = m.model_id::TEXT
LEFT JOIN prediction_logs p ON p.referenced_model_id::TEXT = m.model_id::TEXT
ORDER BY m.IMLTIMESTAMP DESC;

-- Índice para mejorar performance
CREATE INDEX IF NOT EXISTS idx_iml_model_trace 
ON IMLIMMUTABLELOGS(IMLENTITYTYPE, IMLENTITYID, IMLACTION) 
WHERE IMLENTITYTYPE IN ('MODEL', 'DATASET', 'PREDICTION');
```

### 2. Servicio de Trazabilidad

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/trace/ModelTraceService.java` (NUEVO)

```java
package com.codeflowx.govern.business.trace;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para trazabilidad completa modelo-dataset-output
 * EU AI Act Art. 19.1 - Registros completos
 */
@Service
@Slf4j
public class ModelTraceService {
    
    @Autowired
    private DAO dao;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Obtiene traza completa de un modelo
     */
    public ModelTraceResponse getCompleteTrace(Long modelId) {
        log.info("Obteniendo traza completa para modelo: {}", modelId);
        
        // Query a vista unificada
        String query = "SELECT * FROM v_complete_model_trace WHERE model_id = ? ORDER BY model_timestamp DESC";
        List<Map<String, Object>> traceRows = dao.findListBySQL(Map.class, query, modelId);
        
        if (traceRows.isEmpty()) {
            log.warn("No se encontró traza para modelo: {}", modelId);
            return ModelTraceResponse.empty(modelId);
        }
        
        ModelTraceResponse response = new ModelTraceResponse();
        response.setModelId(modelId);
        
        // Agrupar por modelo y extraer eventos
        Set<Long> datasetIds = new HashSet<>();
        List<TrainingEvent> trainingEvents = new ArrayList<>();
        List<EvaluationEvent> evaluationEvents = new ArrayList<>();
        List<DeploymentEvent> deploymentEvents = new ArrayList<>();
        List<PredictionEvent> predictions = new ArrayList<>();
        
        for (Map<String, Object> row : traceRows) {
            // Extraer eventos de entrenamiento
            if (row.get("model_action") != null) {
                String action = row.get("model_action").toString();
                if ("TRAIN".equals(action)) {
                    TrainingEvent event = extractTrainingEvent(row);
                    trainingEvents.add(event);
                } else if ("EVALUATE".equals(action)) {
                    EvaluationEvent event = extractEvaluationEvent(row);
                    evaluationEvents.add(event);
                } else if ("DEPLOY".equals(action)) {
                    DeploymentEvent event = extractDeploymentEvent(row);
                    deploymentEvents.add(event);
                }
            }
            
            // Extraer datasets
            if (row.get("dataset_id") != null) {
                datasetIds.add(Long.parseLong(row.get("dataset_id").toString()));
            }
            
            // Extraer predicciones
            if (row.get("prediction_id") != null) {
                PredictionEvent event = extractPredictionEvent(row);
                predictions.add(event);
            }
        }
        
        // Obtener información de datasets
        List<DatasetInfo> datasets = getDatasetInfo(new ArrayList<>(datasetIds));
        
        response.setDatasets(datasets);
        response.setTrainingEvents(trainingEvents);
        response.setEvaluationEvents(evaluationEvents);
        response.setDeploymentEvents(deploymentEvents);
        response.setPredictions(predictions);
        
        log.info("Traza completa obtenida: {} datasets, {} entrenamientos, {} evaluaciones, {} despliegues, {} predicciones",
            datasets.size(), trainingEvents.size(), evaluationEvents.size(), 
            deploymentEvents.size(), predictions.size());
        
        return response;
    }
    
    private TrainingEvent extractTrainingEvent(Map<String, Object> row) {
        TrainingEvent event = new TrainingEvent();
        event.setTimestamp((Timestamp) row.get("model_timestamp"));
        event.setUserId((Long) row.get("model_user_id"));
        event.setUserName((String) row.get("model_user"));
        event.setHash((String) row.get("model_hash"));
        
        // Parsear IMLDATA JSON
        try {
            String dataJson = (String) row.get("model_data");
            if (dataJson != null) {
                JsonNode data = objectMapper.readTree(dataJson);
                event.setConfig(data.get("config"));
                event.setMetrics(data.get("metrics"));
            }
        } catch (Exception e) {
            log.error("Error parseando model_data", e);
        }
        
        return event;
    }
    
    private EvaluationEvent extractEvaluationEvent(Map<String, Object> row) {
        EvaluationEvent event = new EvaluationEvent();
        event.setTimestamp((Timestamp) row.get("model_timestamp"));
        event.setUserId((Long) row.get("model_user_id"));
        event.setHash((String) row.get("model_hash"));
        
        try {
            String dataJson = (String) row.get("model_data");
            if (dataJson != null) {
                JsonNode data = objectMapper.readTree(dataJson);
                event.setMetrics(data.get("metrics"));
                event.setScore(data.get("score") != null ? data.get("score").asDouble() : null);
            }
        } catch (Exception e) {
            log.error("Error parseando evaluation data", e);
        }
        
        return event;
    }
    
    private DeploymentEvent extractDeploymentEvent(Map<String, Object> row) {
        DeploymentEvent event = new DeploymentEvent();
        event.setTimestamp((Timestamp) row.get("model_timestamp"));
        event.setUserId((Long) row.get("model_user_id"));
        event.setHash((String) row.get("model_hash"));
        
        try {
            String dataJson = (String) row.get("model_data");
            if (dataJson != null) {
                JsonNode data = objectMapper.readTree(dataJson);
                event.setEnvironment(data.get("environment") != null ? data.get("environment").asText() : null);
                event.setVersion(data.get("version") != null ? data.get("version").asText() : null);
            }
        } catch (Exception e) {
            log.error("Error parseando deployment data", e);
        }
        
        return event;
    }
    
    private PredictionEvent extractPredictionEvent(Map<String, Object> row) {
        PredictionEvent event = new PredictionEvent();
        event.setTimestamp((Timestamp) row.get("prediction_timestamp"));
        event.setPredictionId((Long) row.get("prediction_id"));
        event.setInput((String) row.get("prediction_input"));
        event.setOutput((String) row.get("prediction_output"));
        event.setHash((String) row.get("prediction_hash"));
        
        return event;
    }
    
    private List<DatasetInfo> getDatasetInfo(List<Long> datasetIds) {
        if (datasetIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        String placeholders = datasetIds.stream()
            .map(id -> "?")
            .collect(Collectors.joining(","));
        
        String query = "SELECT DISTINCT dataset_id, dataset_timestamp, dataset_data " +
                      "FROM v_complete_model_trace " +
                      "WHERE dataset_id IN (" + placeholders + ")";
        
        List<Map<String, Object>> rows = dao.findListBySQL(Map.class, query, datasetIds.toArray());
        
        return rows.stream().map(row -> {
            DatasetInfo info = new DatasetInfo();
            info.setDatasetId(Long.parseLong(row.get("dataset_id").toString()));
            info.setTimestamp((Timestamp) row.get("dataset_timestamp"));
            
            try {
                String dataJson = (String) row.get("dataset_data");
                if (dataJson != null) {
                    JsonNode data = objectMapper.readTree(dataJson);
                    info.setName(data.get("name") != null ? data.get("name").asText() : null);
                    info.setSize(data.get("size") != null ? data.get("size").asLong() : null);
                }
            } catch (Exception e) {
                log.error("Error parseando dataset data", e);
            }
            
            return info;
        }).collect(Collectors.toList());
    }
    
    // DTOs
    @lombok.Data
    public static class ModelTraceResponse {
        private Long modelId;
        private List<DatasetInfo> datasets = new ArrayList<>();
        private List<TrainingEvent> trainingEvents = new ArrayList<>();
        private List<EvaluationEvent> evaluationEvents = new ArrayList<>();
        private List<DeploymentEvent> deploymentEvents = new ArrayList<>();
        private List<PredictionEvent> predictions = new ArrayList<>();
        
        public static ModelTraceResponse empty(Long modelId) {
            ModelTraceResponse response = new ModelTraceResponse();
            response.setModelId(modelId);
            return response;
        }
    }
    
    @lombok.Data
    public static class DatasetInfo {
        private Long datasetId;
        private String name;
        private Long size;
        private Timestamp timestamp;
    }
    
    @lombok.Data
    public static class TrainingEvent {
        private Timestamp timestamp;
        private Long userId;
        private String userName;
        private String hash;
        private JsonNode config;
        private JsonNode metrics;
    }
    
    @lombok.Data
    public static class EvaluationEvent {
        private Timestamp timestamp;
        private Long userId;
        private String hash;
        private JsonNode metrics;
        private Double score;
    }
    
    @lombok.Data
    public static class DeploymentEvent {
        private Timestamp timestamp;
        private Long userId;
        private String hash;
        private String environment;
        private String version;
    }
    
    @lombok.Data
    public static class PredictionEvent {
        private Long predictionId;
        private Timestamp timestamp;
        private String input;
        private String output;
        private String hash;
    }
}
```

### 3. Controller REST

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/TraceController.java` (NUEVO)

```java
package com.codeflowx.govern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codeflowx.govern.business.trace.ModelTraceService;
import com.codeflowx.govern.business.trace.ModelTraceService.ModelTraceResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para trazabilidad completa modelo-dataset-output
 * EU AI Act Art. 19.1
 */
@RestController
@RequestMapping("/api/v1/compliance/trace")
@Slf4j
public class TraceController {
    
    @Autowired
    private ModelTraceService traceService;
    
    /**
     * Obtiene traza completa de un modelo
     * GET /api/v1/compliance/trace/model/{modelId}
     */
    @GetMapping("/model/{modelId}")
    public ResponseEntity<ModelTraceResponse> getModelTrace(@PathVariable Long modelId) {
        log.info("GET /api/v1/compliance/trace/model/{}", modelId);
        
        ModelTraceResponse trace = traceService.getCompleteTrace(modelId);
        
        return ResponseEntity.ok(trace);
    }
}
```

### 4. ViewModel ZK (Opcional - para UI)

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/trace/ModelTraceViewModel.java` (NUEVO)

```java
package com.codeflowx.govern.viewmodel.trace;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import com.codeflowx.govern.business.trace.ModelTraceService;
import com.codeflowx.govern.business.trace.ModelTraceService.ModelTraceResponse;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * ViewModel para visualización de trazabilidad completa
 * EU AI Act Art. 19.1
 */
public class ModelTraceViewModel {
    
    @WireVariable
    private ModelTraceService traceService;
    
    @Getter @Setter
    private Long modelId;
    
    @Getter @Setter
    private ModelTraceResponse trace;
    
    @Init
    public void init(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        // Inicializar con modelId del contexto
    }
    
    @Command
    public void loadTrace() {
        if (modelId != null) {
            trace = traceService.getCompleteTrace(modelId);
        }
    }
    
    @Command
    public void exportToPDF() {
        // TODO: Implementar exportación PDF
    }
    
    @Command
    public void exportToCSV() {
        // TODO: Implementar exportación CSV
    }
}
```

---

## PRUEBAS REQUERIDAS

### 1. Prueba de Vista SQL

```sql
-- Verificar que la vista funciona
SELECT * FROM v_complete_model_trace WHERE model_id = 1 LIMIT 10;

-- Verificar performance
EXPLAIN ANALYZE 
SELECT * FROM v_complete_model_trace WHERE model_id = 1;
```

### 2. Prueba de Endpoint REST

```bash
# Obtener traza completa
curl -X GET "http://localhost:8080/api/v1/compliance/trace/model/1" \
  -H "Authorization: Bearer <token>"
```

### 3. Prueba de Integración

- Crear log de modelo con `IMLENTITYTYPE='MODEL'` y `IMLACTION='TRAIN'`
- Crear log de dataset con referencia al modelo en `IMLDATA`
- Crear log de predicción con referencia al modelo
- Verificar que el endpoint retorna traza completa

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md#inc-008-001`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
- **Artículo EU AI Act:** Art. 19.1 (registros completos)

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 3 días  
**Responsable:** Backend Team + DBA Team

