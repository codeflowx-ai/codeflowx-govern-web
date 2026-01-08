# Estado de Integración con Microservicios Python

**Fecha:** Enero 2025  
**Análisis:** Verificación de integración entre módulo de modelos y microservicios Python

---

## ✅ RESUMEN EJECUTIVO

**Estado:** ⚠️ **CLIENTES EXISTEN PERO NO ESTÁN INTEGRADOS**

- ✅ **Clientes Java disponibles** en `codeflowx.govern.nocode.client`
- ✅ **Microservicios Python funcionando** (puertos 8001, 8011, 8006)
- ❌ **NO integrados** en `ModelBiasAnalysisBusinessService` ni `ModelExplainabilityBusinessService`
- ✅ **Sí se usan en otros servicios** (`PromptBusinessService`, `FriaAssessmentBusinessService`)

---

## 🔍 ANÁLISIS DETALLADO

### 1. Clientes Disponibles

#### ✅ `BiasDetectionClient` (Puerto 8001)

**Ubicación:** `codeflowx.govern.nocode.client/src/main/java/com/codeflowx/governance/client/BiasDetectionClient.java`

**Métodos disponibles:**
```java
// Análisis de sesgo
BiasAnalysisResponse analyzeBias(File csvFile, String modelId, String protectedAttribute)
BiasAnalysisResponse analyzeBias(File csvFile, String modelId, String protectedAttribute, 
                                 String favorableOutcome, Double threshold)

// Detección de deriva
DriftAnalysisResponse detectDrift(File referenceFile, File currentFile, 
                                   String numericalFeatures, String categoricalFeatures)

// Validación de calidad de datos
DataQualityResponse validateDataQuality(File csvFile, String targetColumn, 
                                         String numericalFeatures, String categoricalFeatures)

// Explicabilidad de predicciones
ExplainabilityResponse explainPredictions(File csvFile, String modelType, 
                                          String method, String predictionColumn)
```

**Formato CSV esperado para análisis de sesgo:**
```csv
y_true,y_pred,gender
1,1,male
0,0,female
1,0,female
0,1,male
```

**Respuesta `BiasAnalysisResponse`:**
- `modelId`: ID del modelo
- `classification`: LOW, MODERATE, HIGH
- `metrics`: `BiasMetrics` con:
  - `demographicParityDifference`
  - `equalOpportunityDifference`
  - `disparateImpactRatio`
- `groupsAnalysis`: Lista de `GroupAnalysis` por grupo protegido
- `recommendations`: Lista de recomendaciones

#### ✅ `AIInterpreterClient` (Puerto 8011)

**Ubicación:** `codeflowx.govern.nocode.client/src/main/java/com/codeflowx/governance/client/AIInterpreterClient.java`

**Métodos disponibles:**
```java
// Explicar resultado técnico en lenguaje natural
ExplanationResponse explainResult(ExplainRequest request)

// Responder preguntas sobre resultados
AnswerResponse answerQuestion(QuestionRequest request)

// Generar resumen ejecutivo
ExecutiveSummaryResponse generateExecutiveSummary(ExecutiveSummaryRequest request)

// Análisis de causa raíz
RootCauseAnalysisResponse rootCauseAnalysis(RootCauseAnalysisRequest request)

// Chat interactivo
ChatResponse chat(ChatRequest request)
```

**Uso para explicabilidad:**
```java
ExplainRequest request = ExplainRequest.builder()
    .analysisData(Map.of(
        "demographic_parity_difference", 0.15,
        "classification", "MODERATE"
    ))
    .analysisType(AnalysisType.BIAS)
    .language(LanguageType.SPANISH)
    .build();

ExplanationResponse response = governance.aiInterpreter().explainResult(request);
```

#### ✅ `ModelWrapperClient` (Puerto 8006)

**Ubicación:** `codeflowx.govern.nocode.client/src/main/java/com/codeflowx/governance/client/ModelWrapperClient.java`

**Métodos disponibles:**
```java
// Invocar modelo
ModelInvokeResponse invoke(ModelInvokeRequest request)

// Invocación en batch
BatchInvokeResponse batchInvoke(BatchInvokeRequest request)

// Comparar respuestas de modelos
CompareResponsesResponse compareResponses(CompareResponsesRequest request)

// Listar modelos disponibles
ListAvailableResponse listAvailable()
```

**Uso para generar predicciones:**
```java
ModelInvokeRequest request = ModelInvokeRequest.builder()
    .modelId("gpt-4")
    .messages(List.of(
        Message.builder().role("user").content("prompt").build()
    ))
    .build();

ModelInvokeResponse response = governance.modelWrapper().invoke(request);
```

---

## ❌ PROBLEMA: NO ESTÁN INTEGRADOS

### Estado Actual

**`ModelBiasAnalysisBusinessService`:**
```java
@Service
public class ModelBiasAnalysisBusinessService {
    private final ModelBiasAnalysisRepository repository;
    
    // ❌ NO usa BiasDetectionClient
    // ❌ NO ejecuta análisis
    // ✅ Solo hace CRUD
    
    public ModelBiasAnalysis create(ModelBiasAnalysis analysis, String createdBy) {
        // Solo guarda en BD, NO ejecuta análisis
        return repository.save(analysis);
    }
}
```

**`ModelExplainabilityBusinessService`:**
```java
@Service
public class ModelExplainabilityBusinessService {
    private final ModelExplainabilityRepository repository;
    
    // ❌ NO usa BiasDetectionClient.explainPredictions()
    // ❌ NO usa AIInterpreterClient
    // ✅ Solo hace CRUD
    
    public ModelExplainability create(ModelExplainability analysis, String createdBy) {
        // Solo guarda en BD, NO ejecuta análisis
        return repository.save(analysis);
    }
}
```

### Comparación con Servicios que SÍ Integran

**`PromptBusinessService` (SÍ integra):**
```java
@Service
public class PromptBusinessService {
    @Autowired(required = false)
    private AIGovernanceClient aiGovernanceClient;
    
    public void validarPrompt() {
        if (aiGovernanceClient != null) {
            // ✅ SÍ usa el cliente
            PromptSafetyResponse response = aiGovernanceClient
                .promptGovernance()
                .evaluateSafety(request);
        }
    }
}
```

---

## 🔧 SOLUCIÓN: CÓMO INTEGRAR

### 1. Integrar `BiasDetectionClient` en `ModelBiasAnalysisBusinessService`

**Código propuesto:**

```java
package com.codeflowx.govern.business.evaluation;

import com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.repository.evaluation.ModelBiasAnalysisRepository;
import com.codeflowx.govern.repository.models.ModelRepository;
import com.codeflowx.governance.client.AIGovernanceClient;
import com.codeflowx.governance.client.model.biasdetection.BiasAnalysisResponse;
import com.codeflowx.governance.client.model.biasdetection.GroupAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelBiasAnalysisBusinessService {

    private final ModelBiasAnalysisRepository repository;
    private final ModelRepository modelRepository;
    
    @Autowired(required = false)
    private AIGovernanceClient aiGovernanceClient;
    
    @Autowired(required = false)
    private com.codeflowx.governance.client.ModelWrapperClient modelWrapperClient;

    // Métodos existentes (CRUD)...
    
    /**
     * NUEVO: Ejecutar análisis de sesgo usando BiasDetectionClient
     * 
     * @param modelId ID del modelo a analizar
     * @param testDatasetFile Archivo CSV con predicciones (y_true, y_pred, protected_attribute)
     * @param protectedAttribute Atributo protegido (ej: "gender", "race")
     * @param createdBy Usuario que ejecuta el análisis
     * @return ModelBiasAnalysis con resultados del análisis
     */
    @Transactional
    public ModelBiasAnalysis executeBiasAnalysis(
            Long modelId,
            File testDatasetFile,
            String protectedAttribute,
            String createdBy) {
        
        log.info("Ejecutando análisis de sesgo para modelo: modelId={}, protectedAttribute={}", 
            modelId, protectedAttribute);
        
        // 1. Validar que el modelo existe
        Model model = modelRepository.findById(modelId)
            .orElseThrow(() -> new IllegalArgumentException("Modelo no encontrado: " + modelId));
        
        // 2. Validar que BiasDetectionClient está disponible
        if (aiGovernanceClient == null) {
            throw new IllegalStateException("AIGovernanceClient no está disponible. " +
                "No se puede ejecutar análisis de sesgo.");
        }
        
        // 3. Ejecutar análisis usando BiasDetectionClient
        BiasAnalysisResponse biasResponse;
        try {
            biasResponse = aiGovernanceClient.biasDetection()
                .analyzeBias(
                    testDatasetFile,
                    model.getModmodelid() != null ? model.getModmodelid() : modelId.toString(),
                    protectedAttribute
                );
        } catch (Exception e) {
            log.error("Error ejecutando análisis de sesgo para modelo: modelId={}", modelId, e);
            throw new RuntimeException("Error ejecutando análisis de sesgo: " + e.getMessage(), e);
        }
        
        // 4. Mapear respuesta a entidad ModelBiasAnalysis
        ModelBiasAnalysis analysis = new ModelBiasAnalysis();
        analysis.setModel(model);
        
        // Mapear métricas
        if (biasResponse.getMetrics() != null) {
            // Calcular bias score (0-1, donde 0 = sin sesgo, 1 = máximo sesgo)
            BigDecimal demographicParity = BigDecimal.valueOf(
                Math.abs(biasResponse.getMetrics().getDemographicParityDifference())
            );
            BigDecimal equalOpportunity = BigDecimal.valueOf(
                Math.abs(biasResponse.getMetrics().getEqualOpportunityDifference())
            );
            BigDecimal biasScore = demographicParity.add(equalOpportunity).divide(BigDecimal.valueOf(2));
            analysis.setModbiasscore(biasScore);
            
            // Determinar severidad
            String severity = "LOW";
            if (biasScore.compareTo(BigDecimal.valueOf(0.3)) > 0) {
                severity = "HIGH";
            } else if (biasScore.compareTo(BigDecimal.valueOf(0.15)) > 0) {
                severity = "MODERATE";
            }
            analysis.setModseverity(severity);
        }
        
        // Mapear tipo de sesgo
        analysis.setModbiastype("DEMOGRAPHIC_PARITY"); // O determinar desde respuesta
        
        // Mapear descripción
        analysis.setModbiasdescription(
            String.format("Análisis de sesgo detectado: %s. " +
                "Demographic Parity Difference: %.3f, " +
                "Equal Opportunity Difference: %.3f",
                biasResponse.getClassification(),
                biasResponse.getMetrics().getDemographicParityDifference(),
                biasResponse.getMetrics().getEqualOpportunityDifference())
        );
        
        // Mapear grupos afectados
        if (biasResponse.getGroupsAnalysis() != null) {
            List<String> affectedGroups = biasResponse.getGroupsAnalysis().stream()
                .map(GroupAnalysis::getGroup)
                .collect(Collectors.toList());
            // Serializar a JSONB
            try {
                String json = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(affectedGroups);
                analysis.setModaffectedgroups(json);
            } catch (Exception e) {
                log.warn("Error serializando affectedGroups", e);
            }
        }
        
        // Mapear recomendaciones
        if (biasResponse.getRecommendations() != null && !biasResponse.getRecommendations().isEmpty()) {
            String recommendations = String.join("; ", biasResponse.getRecommendations());
            analysis.setModmitigationstrategies(recommendations);
        }
        
        // Mapear datos de análisis (JSONB)
        try {
            String analysisData = new com.fasterxml.jackson.databind.ObjectMapper()
                .writeValueAsString(biasResponse);
            analysis.setModanalysisdata(analysisData);
        } catch (Exception e) {
            log.warn("Error serializando analysisData", e);
        }
        
        // Campos de auditoría
        analysis.setModstatus("REVIEWED");
        analysis.setModcreatedby(createdBy);
        analysis.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        
        // 5. Guardar en BD
        return repository.save(analysis);
    }
    
    /**
     * NUEVO: Ejecutar análisis de sesgo usando ModelWrapperClient para generar predicciones
     * 
     * Este método:
     * 1. Obtiene dataset de prueba
     * 2. Invoca modelo usando ModelWrapperClient
     * 3. Genera CSV con predicciones
     * 4. Ejecuta análisis de sesgo
     */
    @Transactional
    public ModelBiasAnalysis executeBiasAnalysisWithModelInvocation(
            Long modelId,
            List<String> testPrompts,
            List<String> groundTruth,
            String protectedAttribute,
            String createdBy) {
        
        log.info("Ejecutando análisis de sesgo con invocación de modelo: modelId={}", modelId);
        
        // 1. Validar modelo
        Model model = modelRepository.findById(modelId)
            .orElseThrow(() -> new IllegalArgumentException("Modelo no encontrado: " + modelId));
        
        // 2. Validar clientes
        if (aiGovernanceClient == null || modelWrapperClient == null) {
            throw new IllegalStateException("Clientes no disponibles");
        }
        
        // 3. Invocar modelo para cada prompt
        // TODO: Implementar lógica de invocación y generación de CSV
        // Por ahora, lanzar excepción indicando que requiere implementación
        throw new UnsupportedOperationException(
            "Este método requiere implementación de generación de CSV desde predicciones"
        );
    }
}
```

### 2. Integrar `BiasDetectionClient.explainPredictions()` en `ModelExplainabilityBusinessService`

**Código propuesto:**

```java
package com.codeflowx.govern.business.evaluation;

import com.codeflowx.govern.entity.evaluation.ModelExplainability;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.repository.evaluation.ModelExplainabilityRepository;
import com.codeflowx.govern.repository.models.ModelRepository;
import com.codeflowx.governance.client.AIGovernanceClient;
import com.codeflowx.governance.client.model.biasdetection.ExplainabilityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelExplainabilityBusinessService {

    private final ModelExplainabilityRepository repository;
    private final ModelRepository modelRepository;
    
    @Autowired(required = false)
    private AIGovernanceClient aiGovernanceClient;

    // Métodos existentes (CRUD)...
    
    /**
     * NUEVO: Ejecutar análisis de explicabilidad usando BiasDetectionClient.explainPredictions()
     * 
     * @param modelId ID del modelo
     * @param predictionsFile Archivo CSV con predicciones y características
     * @param method Método de explicabilidad (SHAP, LIME, etc.)
     * @param createdBy Usuario que ejecuta
     * @return ModelExplainability con resultados
     */
    @Transactional
    public ModelExplainability executeExplainabilityAnalysis(
            Long modelId,
            File predictionsFile,
            String method,
            String createdBy) {
        
        log.info("Ejecutando análisis de explicabilidad para modelo: modelId={}, method={}", 
            modelId, method);
        
        // 1. Validar modelo
        Model model = modelRepository.findById(modelId)
            .orElseThrow(() -> new IllegalArgumentException("Modelo no encontrado: " + modelId));
        
        // 2. Validar cliente
        if (aiGovernanceClient == null) {
            throw new IllegalStateException("AIGovernanceClient no está disponible");
        }
        
        // 3. Ejecutar explicabilidad
        ExplainabilityResponse explainResponse;
        try {
            explainResponse = aiGovernanceClient.biasDetection()
                .explainPredictions(
                    predictionsFile,
                    determineModelType(model), // "tree", "linear", "neural", etc.
                    method != null ? method : "shap",
                    "prediction" // columna de predicción
                );
        } catch (Exception e) {
            log.error("Error ejecutando análisis de explicabilidad: modelId={}", modelId, e);
            throw new RuntimeException("Error ejecutando explicabilidad: " + e.getMessage(), e);
        }
        
        // 4. Mapear respuesta a entidad
        ModelExplainability analysis = new ModelExplainability();
        analysis.setModel(model);
        analysis.setModexplainabilitymethod(method != null ? method.toUpperCase() : "SHAP");
        
        // Mapear score (0-100)
        if (explainResponse.getOverallScore() != null) {
            analysis.setModexplainabilityscore(
                BigDecimal.valueOf(explainResponse.getOverallScore() * 100)
            );
        }
        
        // Determinar si es explicable (score > 50)
        analysis.setModisexplainable(
            explainResponse.getOverallScore() != null && 
            explainResponse.getOverallScore() > 0.5
        );
        
        // Mapear importancia de características
        if (explainResponse.getFeatureImportance() != null) {
            try {
                String json = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(explainResponse.getFeatureImportance());
                analysis.setModfeatureimportance(json);
            } catch (Exception e) {
                log.warn("Error serializando featureImportance", e);
            }
        }
        
        // Determinar calidad
        if (explainResponse.getOverallScore() != null) {
            double score = explainResponse.getOverallScore();
            if (score >= 0.8) {
                analysis.setModexplanationquality("EXCELLENT");
            } else if (score >= 0.6) {
                analysis.setModexplanationquality("GOOD");
            } else if (score >= 0.4) {
                analysis.setModexplanationquality("FAIR");
            } else {
                analysis.setModexplanationquality("POOR");
            }
        }
        
        // Campos de auditoría
        analysis.setModstatus("REVIEWED");
        analysis.setModcreatedby(createdBy);
        analysis.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        
        // 5. Guardar
        return repository.save(analysis);
    }
    
    /**
     * Determinar tipo de modelo desde Model entity
     */
    private String determineModelType(Model model) {
        // Lógica para determinar tipo desde modtype o moddescription
        String modelType = model.getModtype() != null ? model.getModtype().toLowerCase() : "";
        if (modelType.contains("tree") || modelType.contains("forest") || modelType.contains("xgboost")) {
            return "tree";
        } else if (modelType.contains("linear") || modelType.contains("regression")) {
            return "linear";
        } else if (modelType.contains("neural") || modelType.contains("deep") || modelType.contains("llm")) {
            return "neural";
        }
        return "tree"; // default
    }
}
```

### 3. Actualizar Endpoints en `ModelAnalysisController`

**Agregar endpoints que ejecuten análisis:**

```java
@PostMapping("/{modelId}/bias-analysis/execute")
@Operation(summary = "Ejecutar análisis de sesgo", 
    description = "Ejecuta análisis de sesgo usando BiasDetectionClient")
public Mono<ResponseEntity<ModelBiasAnalysisResponseDto>> executeBiasAnalysis(
        @PathVariable Long modelId,
        @RequestParam("file") MultipartFile file,
        @RequestParam("protectedAttribute") String protectedAttribute) {
    
    return Mono.fromCallable(() -> {
            // Guardar archivo temporal
            File tempFile = File.createTempFile("bias-analysis-", ".csv");
            file.transferTo(tempFile);
            
            try {
                // Ejecutar análisis
                ModelBiasAnalysis analysis = biasAnalysisService.executeBiasAnalysis(
                    modelId,
                    tempFile,
                    protectedAttribute,
                    "system" // TODO: Obtener usuario del contexto
                );
                
                return biasAnalysisToResponseDto(analysis);
            } finally {
                // Limpiar archivo temporal
                tempFile.delete();
            }
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error ejecutando análisis de sesgo: modelId={}", modelId, error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
}

@PostMapping("/{modelId}/explainability/execute")
@Operation(summary = "Ejecutar análisis de explicabilidad",
    description = "Ejecuta análisis de explicabilidad usando BiasDetectionClient.explainPredictions()")
public Mono<ResponseEntity<ModelExplainabilityDto>> executeExplainability(
        @PathVariable Long modelId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "method", defaultValue = "shap") String method) {
    
    return Mono.fromCallable(() -> {
            File tempFile = File.createTempFile("explainability-", ".csv");
            file.transferTo(tempFile);
            
            try {
                ModelExplainability analysis = explainabilityService.executeExplainabilityAnalysis(
                    modelId,
                    tempFile,
                    method,
                    "system"
                );
                
                return explainabilityToDto(analysis);
            } finally {
                tempFile.delete();
            }
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error ejecutando explicabilidad: modelId={}", modelId, error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
}
```

---

## 📋 CHECKLIST DE INTEGRACIÓN

### Para `ModelBiasAnalysisBusinessService`:

- [ ] Inyectar `AIGovernanceClient` (opcional)
- [ ] Crear método `executeBiasAnalysis(File csvFile, ...)`
- [ ] Mapear `BiasAnalysisResponse` → `ModelBiasAnalysis`
- [ ] Agregar endpoint `POST /{modelId}/bias-analysis/execute`
- [ ] Manejar errores y validaciones
- [ ] Agregar tests unitarios

### Para `ModelExplainabilityBusinessService`:

- [ ] Inyectar `AIGovernanceClient` (opcional)
- [ ] Crear método `executeExplainabilityAnalysis(File csvFile, ...)`
- [ ] Mapear `ExplainabilityResponse` → `ModelExplainability`
- [ ] Agregar endpoint `POST /{modelId}/explainability/execute`
- [ ] Manejar errores y validaciones
- [ ] Agregar tests unitarios

### Configuración:

- [ ] Verificar `application.yml` tiene configuración de `AIGovernanceClient`
- [ ] Verificar que el Gateway está accesible
- [ ] Verificar que los microservicios Python están corriendo

---

## 🎯 CONCLUSIÓN

**Estado actual:**
- ✅ Clientes Java existen y están bien diseñados
- ✅ Microservicios Python funcionan
- ❌ **NO están integrados** en los servicios de negocio de modelos
- ✅ Se usan en otros servicios (ejemplo: `PromptBusinessService`)

**Acción requerida:**
1. Integrar `BiasDetectionClient` en `ModelBiasAnalysisBusinessService`
2. Integrar `BiasDetectionClient.explainPredictions()` en `ModelExplainabilityBusinessService`
3. Agregar endpoints que ejecuten análisis (no solo CRUD)
4. Agregar manejo de archivos CSV (upload, validación, limpieza)

**Beneficio:**
- Los usuarios podrán ejecutar análisis reales desde el frontend
- Los resultados se almacenarán automáticamente en BD
- Se completará el flujo: Ejecutar → Analizar → Almacenar → Visualizar

