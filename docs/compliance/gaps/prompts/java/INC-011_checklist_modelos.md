# PROMPT: INC-011 - Checklist Completo Validación Modelos

**Incidencia:** INC-011  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 11, Art. 15  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java - Backend

---

## CONTEXTO

Las validaciones de modelos no cubren todos los requisitos del Art. 11 (documentación técnica) y Art. 15 (precisión, robustez). No hay checklist completo de validación por tipo de modelo.

**Ubicación Actual:**
- Validaciones parciales en diferentes servicios
- No hay servicio centralizado de validación completa

---

## REQUISITOS

1. Crear checklist completo de validación por tipo de modelo
2. Validar documentación técnica (Anexo IV) para todos los modelos
3. Validar robustez adversarial para modelos propios
4. Validar licencias y términos para modelos open source

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear servicio centralizado de validación

**Archivo:** `com.codeflowx.govern.business.compliance.ModelValidationService.java`

```java
package com.codeflowx.govern.business.compliance;

import com.codeflowx.govern.entity.models.Model;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ModelValidationService {
    
    @Autowired
    private TechnicalDocumentationService technicalDocService;
    
    @Autowired
    private RobustnessValidationService robustnessService;
    
    @Autowired
    private LicenseValidationService licenseService;
    
    /**
     * Valida modelo completo según tipo
     */
    public ModelValidationResult validateModelComplete(Long modelId, ModelType type) {
        log.info("Iniciando validación completa de modelo: ID={}, Type={}", modelId, type);
        
        Model model = modelService.getModel(modelId);
        if (model == null) {
            throw new IllegalArgumentException("Model not found: " + modelId);
        }
        
        ModelValidationResult result = new ModelValidationResult();
        result.setModelId(modelId);
        result.setModelType(type);
        
        // Checklist común (todos los modelos)
        validateCommonRequirements(model, result);
        
        // Checklist específico por tipo
        switch (type) {
            case OPENAI:
                validateOpenAISpecific(model, result);
                break;
            case OPEN_SOURCE:
                validateOpenSourceSpecific(model, result);
                break;
            case INTERNAL:
                validateInternalSpecific(model, result);
                break;
            default:
                log.warn("Tipo de modelo desconocido: {}", type);
        }
        
        // Validar documentación técnica (Anexo IV) - TODOS los modelos
        validateTechnicalDocumentation(model, result);
        
        // Validar robustez (Art. 15) - TODOS los modelos
        validateRobustness(model, result);
        
        // Calcular score final
        result.calculateFinalScore();
        
        log.info("Validación completada: Model ID={}, Score={}, Pass={}", 
            modelId, result.getFinalScore(), result.isPass());
        
        return result;
    }
    
    /**
     * Checklist común para todos los modelos
     */
    private void validateCommonRequirements(Model model, ModelValidationResult result) {
        // 1. Nombre del modelo
        if (model.getModname() == null || model.getModname().trim().isEmpty()) {
            result.addFailure("MODEL_NAME_MISSING", "Nombre del modelo es obligatorio");
        }
        
        // 2. Tipo de modelo
        if (model.getModtype() == null || model.getModtype().trim().isEmpty()) {
            result.addFailure("MODEL_TYPE_MISSING", "Tipo de modelo es obligatorio");
        }
        
        // 3. Versión
        if (model.getModversion() == null || model.getModversion().trim().isEmpty()) {
            result.addFailure("MODEL_VERSION_MISSING", "Versión del modelo es obligatoria");
        }
        
        // 4. Estado
        if (model.getModstatus() == null || model.getModstatus().trim().isEmpty()) {
            result.addFailure("MODEL_STATUS_MISSING", "Estado del modelo es obligatorio");
        }
        
        // 5. Creador
        if (model.getModcreatedby() == null || model.getModcreatedby().trim().isEmpty()) {
            result.addFailure("MODEL_CREATOR_MISSING", "Creador del modelo es obligatorio");
        }
    }
    
    /**
     * Checklist específico para modelos OpenAI
     */
    private void validateOpenAISpecific(Model model, ModelValidationResult result) {
        // 1. API Key configurada y encriptada
        String apiKey = getApiKeyForModel(model);
        if (apiKey == null || apiKey.isEmpty()) {
            result.addFailure("OPENAI_API_KEY_MISSING", "API Key de OpenAI no configurada");
        } else if (!isApiKeyEncrypted(apiKey)) {
            result.addWarning("OPENAI_API_KEY_NOT_ENCRYPTED", "API Key no está encriptada");
        }
        
        // 2. Verificar términos de uso
        OpenAITermsValidation termsValidation = licenseService.validateOpenAITerms(model);
        if (!termsValidation.isCompliant()) {
            result.addFailure("OPENAI_TERMS_NOT_COMPLIANT", 
                "Términos de uso de OpenAI no cumplidos: " + termsValidation.getReason());
        }
        
        // 3. Verificar rate limits
        RateLimitStatus rateLimit = checkRateLimits(model);
        if (rateLimit.getUsage() > 0.90) {
            result.addWarning("OPENAI_RATE_LIMIT_HIGH", 
                "Rate limit cerca del máximo: " + rateLimit.getUsage() + "%");
        }
        
        // 4. Verificar costos
        BigDecimal monthlyCost = calculateMonthlyCost(model);
        if (monthlyCost.compareTo(new BigDecimal("1000")) > 0) {
            result.addWarning("OPENAI_COST_HIGH", 
                "Costos mensuales altos: $" + monthlyCost);
        }
        
        // 5. Verificar data opt-out
        if (!termsValidation.isDataOptOutEnabled()) {
            result.addWarning("OPENAI_DATA_OPT_OUT", 
                "Datos pueden ser usados para entrenamiento OpenAI");
        }
    }
    
    /**
     * Checklist específico para modelos open source
     */
    private void validateOpenSourceSpecific(Model model, ModelValidationResult result) {
        // 1. Licencia
        String license = getLicenseForModel(model);
        if (license == null || license.isEmpty()) {
            result.addFailure("OPEN_SOURCE_LICENSE_MISSING", "Licencia del modelo no especificada");
        } else if (!isLicenseCompatible(license)) {
            result.addFailure("OPEN_SOURCE_LICENSE_INCOMPATIBLE", 
                "Licencia incompatible: " + license);
        }
        
        // 2. Model Card
        String modelCard = getModelCardForModel(model);
        if (modelCard == null || modelCard.isEmpty()) {
            result.addFailure("OPEN_SOURCE_MODEL_CARD_MISSING", "Model card no disponible");
        }
        
        // 3. Checksum verification
        String expectedHash = getExpectedChecksum(model);
        String actualHash = calculateModelHash(model);
        if (expectedHash != null && !expectedHash.equals(actualHash)) {
            result.addFailure("OPEN_SOURCE_CHECKSUM_MISMATCH", 
                "Checksum no coincide - modelo puede estar corrupto");
        }
        
        // 4. Bias documentation
        String biasDoc = getBiasDocumentation(model);
        if (biasDoc == null || biasDoc.isEmpty()) {
            result.addWarning("OPEN_SOURCE_BIAS_NOT_DOCUMENTED", 
                "Sesgos no documentados en model card");
        }
        
        // 5. Security scan
        List<SecurityVulnerability> vulnerabilities = securityScanService.scanModel(model);
        if (!vulnerabilities.isEmpty()) {
            result.addFailure("OPEN_SOURCE_VULNERABILITIES", 
                "Vulnerabilidades de seguridad detectadas: " + vulnerabilities.size());
        }
    }
    
    /**
     * Checklist específico para modelos propios (internos)
     */
    private void validateInternalSpecific(Model model, ModelValidationResult result) {
        // 1. Training dataset quality
        Long datasetId = getTrainingDatasetId(model);
        if (datasetId == null) {
            result.addFailure("INTERNAL_DATASET_MISSING", 
                "Dataset de entrenamiento no especificado");
        } else {
            DataQualityResult datasetQuality = biasDetectionService.evaluateDataQuality(datasetId);
            if (!datasetQuality.isPass()) {
                result.addFailure("INTERNAL_DATASET_QUALITY_INSUFFICIENT", 
                    "Calidad del dataset insuficiente: " + datasetQuality.getScore());
            }
        }
        
        // 2. Bias analysis
        BiasAnalysisResult bias = biasDetectionService.analyzeBias(model.getIdxmodel());
        if (bias.getBiasScore() > 0.10) {
            result.addFailure("INTERNAL_BIAS_DETECTED", 
                "Sesgo detectado en modelo: " + bias.getBiasScore());
        }
        
        // 3. Performance metrics
        PerformanceMetrics metrics = modelEvaluationService.getPerformanceMetrics(model.getIdxmodel());
        if (metrics.getAccuracy() < 0.85) {
            result.addWarning("INTERNAL_ACCURACY_LOW", 
                "Accuracy por debajo del umbral: " + metrics.getAccuracy());
        }
        
        // 4. Adversarial robustness (REQUERIDO para modelos propios)
        AdversarialRobustnessResult robustness = adversarialService.testRobustness(model.getIdxmodel());
        if (robustness.getRobustnessScore() < 0.80) {
            result.addFailure("INTERNAL_ROBUSTNESS_INSUFFICIENT", 
                "Robustez adversarial insuficiente: " + robustness.getRobustnessScore());
        }
        
        // 5. Technical documentation completeness
        if (!model.getModtechnicaldoccomplete()) {
            result.addFailure("INTERNAL_TECHNICAL_DOC_INCOMPLETE", 
                "Documentación técnica incompleta (Art. 11)");
        }
    }
    
    /**
     * Valida documentación técnica (Anexo IV) - TODOS los modelos
     */
    private void validateTechnicalDocumentation(Model model, ModelValidationResult result) {
        TechnicalDocumentationValidation docValidation = 
            technicalDocService.validateTechnicalDocumentation(model);
        
        if (!docValidation.isComplete()) {
            result.addFailure("TECHNICAL_DOC_INCOMPLETE", 
                "Documentación técnica incompleta: " + docValidation.getMissingElements());
        }
        
        if (docValidation.getScore() < 0.90) {
            result.addFailure("TECHNICAL_DOC_SCORE_INSUFFICIENT", 
                "Score de documentación técnica insuficiente: " + docValidation.getScore());
        }
    }
    
    /**
     * Valida robustez (Art. 15) - TODOS los modelos
     */
    private void validateRobustness(Model model, ModelValidationResult result) {
        // Para modelos propios, ya se validó en validateInternalSpecific
        // Para otros, validación básica
        if (model.getModishighrisk() != null && model.getModishighrisk()) {
            RobustnessValidation robustness = robustnessService.validateRobustness(model);
            if (!robustness.isPass()) {
                result.addWarning("ROBUSTNESS_VALIDATION_FAILED", 
                    "Validación de robustez falló: " + robustness.getReason());
            }
        }
    }
    
    /**
     * Clase para resultado de validación
     */
    @Data
    public static class ModelValidationResult {
        private Long modelId;
        private ModelType modelType;
        private List<ValidationIssue> failures = new ArrayList<>();
        private List<ValidationIssue> warnings = new ArrayList<>();
        private BigDecimal finalScore = BigDecimal.ZERO;
        private boolean pass = false;
        
        public void addFailure(String code, String message) {
            failures.add(new ValidationIssue(code, message, "FAILURE"));
        }
        
        public void addWarning(String code, String message) {
            warnings.add(new ValidationIssue(code, message, "WARNING"));
        }
        
        public void calculateFinalScore() {
            int totalChecks = failures.size() + warnings.size();
            if (totalChecks == 0) {
                finalScore = BigDecimal.ONE;
                pass = true;
                return;
            }
            
            // Penalizar por failures (más peso) y warnings
            BigDecimal penalty = BigDecimal.valueOf(failures.size() * 0.20 + warnings.size() * 0.05);
            finalScore = BigDecimal.ONE.subtract(penalty);
            finalScore = finalScore.max(BigDecimal.ZERO);
            
            pass = failures.isEmpty() && finalScore.compareTo(new BigDecimal("0.80")) >= 0;
        }
    }
    
    @Data
    public static class ValidationIssue {
        private String code;
        private String message;
        private String severity;
        
        public ValidationIssue(String code, String message, String severity) {
            this.code = code;
            this.message = message;
            this.severity = severity;
        }
    }
    
    public enum ModelType {
        OPENAI,
        OPEN_SOURCE,
        INTERNAL
    }
}
```

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Validar modelo OpenAI completo → Debe pasar todos los checks
2. **Test 2:** Validar modelo open source sin licencia → Debe fallar
3. **Test 3:** Validar modelo propio sin dataset → Debe fallar
4. **Test 4:** Validar modelo con documentación incompleta → Debe fallar

---

## REFERENCIAS

- **Art. 11 EU AI Act:** Documentación Técnica
- **Art. 15 EU AI Act:** Precisión y Robustez
- **Anexo IV:** Requisitos de Documentación Técnica
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-011`

---

**Estado:** ✅ COMPLETADO

