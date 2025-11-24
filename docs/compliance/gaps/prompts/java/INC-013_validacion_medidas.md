# PROMPT: INC-013 - Validación Medidas de Mitigación Implementadas

**Incidencia:** INC-013  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 27.1.f  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend

---

## CONTEXTO

El sistema permite declarar medidas de mitigación en FRIA sin verificar que estén realmente implementadas en el sistema.

**Ubicación Actual:**
- `FriaWizardViewModel.java` - medidas declaradas en wizard
- No hay validación de implementación real

---

## REQUISITOS

1. Validar que medidas declaradas estén implementadas
2. Verificar configuración técnica de medidas
3. Validar que medidas estén activas y funcionando
4. Requerir evidencia técnica de implementación

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear servicio de validación de medidas

**Archivo:** `com.codeflowx.govern.business.compliance.MitigationMeasureValidationService.java`

```java
package com.codeflowx.govern.business.compliance;

import com.codeflowx.govern.entity.compliance.FriaAssessment;
import com.codeflowx.govern.entity.projects.Project;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class MitigationMeasureValidationService {
    
    @Autowired
    private BiasDetectionService biasDetectionService;
    
    @Autowired
    private ModelEvaluationService modelEvaluationService;
    
    @Autowired
    private AdversarialRobustnessService adversarialService;
    
    /**
     * Valida que medidas de mitigación declaradas estén implementadas
     */
    public MitigationValidationResult validateMitigationMeasures(
        FriaAssessment fria, 
        Project project
    ) {
        log.info("Validando medidas de mitigación: FRIA ID={}, Project ID={}", 
            fria.getIdxfriaassessment(), project.getIdxproject());
        
        MitigationValidationResult result = new MitigationValidationResult();
        
        // Parsear medidas del FRIA
        List<Map<String, Object>> declaredMeasures = parseMitigationMeasures(fria);
        
        for (Map<String, Object> measure : declaredMeasures) {
            String measureType = (String) measure.get("type");
            String measureDescription = (String) measure.get("description");
            
            boolean isImplemented = false;
            String implementationStatus = "";
            
            // Validar según tipo de medida
            switch (measureType) {
                case "PREVENTIVE":
                    isImplemented = validatePreventiveMeasure(measure, project, result);
                    break;
                case "DETECTIVE":
                    isImplemented = validateDetectiveMeasure(measure, project, result);
                    break;
                case "CORRECTIVE":
                    isImplemented = validateCorrectiveMeasure(measure, project, result);
                    break;
                default:
                    result.addIssue("UNKNOWN_MEASURE_TYPE", 
                        "Tipo de medida desconocido: " + measureType);
            }
            
            if (!isImplemented) {
                result.addIssue("MEASURE_NOT_IMPLEMENTED", 
                    "Medida declarada pero no implementada: " + measureDescription);
            }
        }
        
        result.calculateScore();
        
        return result;
    }
    
    /**
     * Valida medidas preventivas
     */
    private boolean validatePreventiveMeasure(
        Map<String, Object> measure, 
        Project project, 
        MitigationValidationResult result
    ) {
        String description = ((String) measure.get("description")).toLowerCase();
        
        // Auditoría periódica de sesgos
        if (description.contains("auditoría") && description.contains("sesgo") ||
            description.contains("audit") && description.contains("bias")) {
            // Verificar que leka-bias-detection está configurado y activo
            boolean monitoringActive = biasDetectionService.isMonitoringActive(
                project.getModelId()
            );
            if (!monitoringActive) {
                result.addIssue("BIAS_MONITORING_NOT_ACTIVE", 
                    "Monitoreo de sesgos no está activo");
                return false;
            }
            return true;
        }
        
        // Validación de datos de entrada
        if (description.contains("validación") && description.contains("entrada") ||
            description.contains("input") && description.contains("validation")) {
            // Verificar que hay validación de inputs configurada
            boolean inputValidationActive = checkInputValidationActive(project);
            if (!inputValidationActive) {
                result.addIssue("INPUT_VALIDATION_NOT_ACTIVE", 
                    "Validación de inputs no está activa");
                return false;
            }
            return true;
        }
        
        // Más validaciones según tipos de medidas preventivas...
        
        return false; // Por defecto, no implementada
    }
    
    /**
     * Valida medidas detective
     */
    private boolean validateDetectiveMeasure(
        Map<String, Object> measure, 
        Project project, 
        MitigationValidationResult result
    ) {
        String description = ((String) measure.get("description")).toLowerCase();
        
        // Monitoreo continuo de métricas
        if (description.contains("monitoreo") || description.contains("monitoring")) {
            boolean monitoringActive = checkMonitoringActive(project);
            if (!monitoringActive) {
                result.addIssue("MONITORING_NOT_ACTIVE", 
                    "Monitoreo continuo no está activo");
                return false;
            }
            return true;
        }
        
        // Alertas automáticas
        if (description.contains("alerta") || description.contains("alert")) {
            boolean alertsConfigured = checkAlertsConfigured(project);
            if (!alertsConfigured) {
                result.addIssue("ALERTS_NOT_CONFIGURED", 
                    "Sistema de alertas no configurado");
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Valida medidas correctivas
     */
    private boolean validateCorrectiveMeasure(
        Map<String, Object> measure, 
        Project project, 
        MitigationValidationResult result
    ) {
        String description = ((String) measure.get("description")).toLowerCase();
        
        // Proceso de apelación
        if (description.contains("apelación") || description.contains("appeal")) {
            boolean appealProcessExists = checkAppealProcessExists(project);
            if (!appealProcessExists) {
                result.addIssue("APPEAL_PROCESS_NOT_EXISTS", 
                    "Proceso de apelación no existe");
                return false;
            }
            return true;
        }
        
        // Capacidad de override humano
        if (description.contains("override") || description.contains("sobrescritura")) {
            boolean overrideCapability = checkOverrideCapability(project);
            if (!overrideCapability) {
                result.addIssue("OVERRIDE_NOT_AVAILABLE", 
                    "Capacidad de override no disponible");
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    // Métodos auxiliares de verificación
    private boolean checkInputValidationActive(Project project) {
        // TODO: Verificar configuración de validación de inputs
        return false;
    }
    
    private boolean checkMonitoringActive(Project project) {
        // TODO: Verificar que monitoreo está activo
        return false;
    }
    
    private boolean checkAlertsConfigured(Project project) {
        // TODO: Verificar configuración de alertas
        return false;
    }
    
    private boolean checkAppealProcessExists(Project project) {
        // TODO: Verificar existencia de proceso de apelación
        return false;
    }
    
    private boolean checkOverrideCapability(Project project) {
        // TODO: Verificar capacidad de override
        return false;
    }
    
    @Data
    public static class MitigationValidationResult {
        private List<String> issues = new ArrayList<>();
        private BigDecimal score = BigDecimal.ZERO;
        private boolean allImplemented = false;
        
        public void addIssue(String code, String message) {
            issues.add(code + ": " + message);
        }
        
        public void calculateScore() {
            // Score basado en medidas implementadas vs declaradas
            // TODO: Implementar cálculo
            allImplemented = issues.isEmpty();
            score = allImplemented ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }
}
```

### 2. Integrar en `FriaWizardViewModel.java`

```java
@Autowired
private MitigationMeasureValidationService mitigationValidationService;

private void generateFria() {
    // ... código existente ...
    
    // NUEVA VALIDACIÓN: Medidas de mitigación implementadas (INC-013)
    try {
        MitigationValidationResult mitigationValidation = 
            mitigationValidationService.validateMitigationMeasures(fria, currentProject);
        
        if (!mitigationValidation.isAllImplemented()) {
            showMitigationValidationAlert(mitigationValidation);
            // Requerir justificación o implementar medidas
        }
        
        fria.setFriamitigationvalidationresult(mitigationValidation);
        
    } catch (Exception e) {
        log.error("Error validando medidas de mitigación", e);
    }
}
```

---

## REFERENCIAS

- **Art. 27.1.f EU AI Act:** Medidas de Mitigación
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-013`

---

**Estado:** ✅ COMPLETADO

