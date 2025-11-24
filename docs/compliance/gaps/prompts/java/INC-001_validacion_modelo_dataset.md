# PROMPT: INC-001 - Validación Coherencia Modelo-Dataset

**Incidencia:** INC-001  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 10 (Gobernanza de Datos), Art. 11 (Documentación Técnica)  
**Esfuerzo Estimado:** 0.5 días  
**Tipo:** Java - Backend

---

## CONTEXTO

El sistema actual permite clasificar un modelo como alto riesgo sin verificar que tenga dataset de entrenamiento documentado. Esto viola los requisitos del Art. 10 (gobernanza de datos) y Art. 11 (documentación técnica) del EU AI Act.

**Ubicación Actual:**
- `HighRiskClassifierViewModel.java` - método `classifyAsHighRisk()`
- `Model.java` - campo `MODTRAININGCONFIG` (nullable)

---

## REQUISITOS

1. **Validar existencia de dataset** antes de permitir clasificación como alto riesgo
2. **Bloquear workflow** si falta dataset documentado
3. **Generar alerta CRITICAL** en lugar de WARNING
4. **Hacer obligatorio** `MODTRAININGCONFIG` si `MODISHIGHRISK = true`

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar `HighRiskClassifierViewModel.java`

**Ubicación:** Método `classifyAsHighRisk()`

**Antes de la línea 455** (antes de `classifying = true;`), añadir validación:

```java
/**
 * Valida que el modelo tenga dataset de entrenamiento documentado si es alto riesgo
 * Requisito: Art. 10 (Gobernanza de Datos), Art. 11 (Documentación Técnica)
 */
private void validateModelDatasetForHighRisk() {
    if (currentProject == null || projectId == null) {
        return; // Validación básica ya realizada
    }
    
    // Obtener modelo asociado al proyecto
    // TODO: Ajustar según relación Project-Model en tu esquema
    Model model = getModelForProject(projectId);
    
    if (model == null) {
        // Si no hay modelo, no se puede validar - permitir continuar con advertencia
        log.warn("Proyecto {} no tiene modelo asociado - no se puede validar dataset", projectId);
        return;
    }
    
    // Si el modelo es alto riesgo, debe tener dataset documentado
    if (model.getModishighrisk() != null && model.getModishighrisk()) {
        if (model.getModtrainingconfig() == null || model.getModtrainingconfig().trim().isEmpty()) {
            String errorMsg = "CRITICAL: Modelo alto riesgo requiere dataset de entrenamiento documentado.\n\n" +
                "Según Art. 10 (Gobernanza de Datos) y Art. 11 (Documentación Técnica) del EU AI Act, " +
                "los sistemas de alto riesgo deben tener documentación completa del dataset de entrenamiento.\n\n" +
                "Por favor, complete la configuración de entrenamiento (MODTRAININGCONFIG) antes de clasificar como alto riesgo.";
            
            log.error("Validación fallida - Modelo alto riesgo sin dataset: Model ID={}", model.getIdxmodel());
            
            Messagebox.show(
                errorMsg,
                "Validación CRÍTICA - Dataset Requerido",
                Messagebox.OK,
                Messagebox.ERROR
            );
            
            throw new ValidationException("Modelo alto riesgo sin dataset documentado (Art. 10, 11)");
        }
        
        // Validar que MODTRAININGCONFIG contiene información válida
        // Parsear JSON y verificar campos mínimos
        if (!isTrainingConfigValid(model.getModtrainingconfig())) {
            String errorMsg = "CRITICAL: Configuración de entrenamiento incompleta o inválida.\n\n" +
                "El campo MODTRAININGCONFIG debe contener información válida sobre:\n" +
                "- Dataset utilizado\n" +
                "- Método de entrenamiento\n" +
                "- Parámetros de entrenamiento\n\n" +
                "Por favor, complete la configuración correctamente.";
            
            log.error("Validación fallida - Configuración entrenamiento inválida: Model ID={}", model.getIdxmodel());
            
            Messagebox.show(
                errorMsg,
                "Validación CRÍTICA - Configuración Inválida",
                Messagebox.OK,
                Messagebox.ERROR
            );
            
            throw new ValidationException("Configuración de entrenamiento inválida");
        }
        
        log.info("Validación exitosa - Modelo alto riesgo con dataset documentado: Model ID={}", model.getIdxmodel());
    }
}

/**
 * Valida que la configuración de entrenamiento sea válida
 */
private boolean isTrainingConfigValid(String trainingConfig) {
    if (trainingConfig == null || trainingConfig.trim().isEmpty()) {
        return false;
    }
    
    try {
        // Intentar parsear como JSON
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> config = mapper.readValue(trainingConfig, Map.class);
        
        // Verificar campos mínimos requeridos
        // Ajustar según estructura real de MODTRAININGCONFIG
        boolean hasDataset = config.containsKey("dataset") || config.containsKey("datasetId") || 
                            config.containsKey("datasetName");
        boolean hasMethod = config.containsKey("method") || config.containsKey("trainingMethod");
        
        return hasDataset && hasMethod;
        
    } catch (Exception e) {
        log.warn("Error parseando MODTRAININGCONFIG: {}", e.getMessage());
        // Si no es JSON válido, considerar inválido
        return false;
    }
}

/**
 * Obtiene modelo asociado al proyecto
 * TODO: Ajustar según relación Project-Model en tu esquema
 */
private Model getModelForProject(Long projectId) {
    try {
        // Opción 1: Si hay relación directa Project -> Model
        // return currentProject.getModel();
        
        // Opción 2: Buscar por query
        String query = "SELECT * FROM MODMODELS WHERE IDXPROJECT = ? LIMIT 1";
        return businessService.findBySQL(Model.class, query, projectId);
        
        // Opción 3: Si hay tabla intermedia
        // String query = "SELECT m.* FROM MODMODELS m " +
        //               "INNER JOIN PRJPROJECTMODELS pm ON m.IDXMODEL = pm.IDXMODEL " +
        //               "WHERE pm.IDXPROJECT = ? LIMIT 1";
        // return businessService.findBySQL(Model.class, query, projectId);
        
    } catch (Exception e) {
        log.error("Error obteniendo modelo para proyecto {}", projectId, e);
        return null;
    }
}
```

**Modificar método `classifyAsHighRisk()`:**

```java
@Command
@NotifyChange("*")
public void classifyAsHighRisk() {
    log.info("Clasificando proyecto como ALTO RIESGO: {}", projectId);
    
    // Validaciones básicas existentes
    if (!validateClassification()) {
        return;
    }
    
    // NUEVA VALIDACIÓN: Dataset de entrenamiento (INC-001)
    try {
        validateModelDatasetForHighRisk();
    } catch (ValidationException e) {
        // Ya se mostró mensaje de error, solo loggear
        log.error("Validación dataset fallida: {}", e.getMessage());
        return; // Bloquear clasificación
    }
    
    try {
        classifying = true;
        
        // ... resto del código existente ...
```

### 2. Añadir Import

Añadir al inicio del archivo:

```java
import com.codeflowx.govern.entity.models.Model;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
```

### 3. Crear Exception Personalizada (si no existe)

Si no existe `ValidationException`, crear:

```java
package com.codeflowx.govern.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Validar que dataset existe en sistema** (no solo que está documentado)
2. **Validar calidad del dataset** (usar microservicio `leka-bias-detection-service`)
3. **Generar log inmutable** cuando se detecta falta de dataset

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Intentar clasificar modelo alto riesgo sin dataset → Debe bloquear
2. **Test 2:** Intentar clasificar modelo alto riesgo con dataset válido → Debe permitir
3. **Test 3:** Intentar clasificar modelo NO alto riesgo sin dataset → Debe permitir (no aplica validación)
4. **Test 4:** Intentar clasificar con MODTRAININGCONFIG inválido (JSON malformado) → Debe bloquear

---

## LOGS INMUTABLES

Añadir log inmutable cuando se detecta falta de dataset:

```java
// En validateModelDatasetForHighRisk(), cuando se detecta falta de dataset
immutableLoggingService.createLogEntry(
    "PROJECT",
    projectId,
    "VALIDATION_FAILED_DATASET_MISSING",
    ctxBean.getUser().getIdxuser(),
    ctxBean.getUser().getUsuname(),
    Map.of(
        "modelId", model.getIdxmodel(),
        "reason", "High risk model without documented training dataset",
        "article", "Art. 10, Art. 11"
    )
);
```

---

## REFERENCIAS

- **Art. 10 EU AI Act:** Gobernanza de Datos
- **Art. 11 EU AI Act:** Documentación Técnica
- **Anexo IV:** Requisitos de Documentación Técnica
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-001`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar método `getModelForProject()` según relación real Project-Model en tu esquema
- Ajustar validación `isTrainingConfigValid()` según estructura real de `MODTRAININGCONFIG`
- Considerar hacer `MODTRAININGCONFIG` obligatorio a nivel de entidad si `MODISHIGHRISK = true`

---

**Estado:** ✅ COMPLETADO
