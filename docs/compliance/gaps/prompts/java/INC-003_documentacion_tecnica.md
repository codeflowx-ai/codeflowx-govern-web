# PROMPT: INC-003 - Validación Documentación Técnica Completa

**Incidencia:** INC-003  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 11 + Anexo IV  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## CONTEXTO

El sistema permite clasificar como alto riesgo sin verificar que la documentación técnica (Anexo IV) esté completa. El campo `MODTECHNICALDOCCOMPLETE` existe pero no se valida antes de clasificación.

**Ubicación Actual:**
- `Model.java` - campos `MODTECHNICALDOCCOMPLETE`, `MODTECHNICALDOCSCORE`
- `HighRiskClassifierViewModel.java` - método `classifyAsHighRisk()`

---

## REQUISITOS

1. Validar `MODTECHNICALDOCCOMPLETE = true` antes de permitir clasificación alto riesgo
2. Validar `MODTECHNICALDOCSCORE >= 0.90` (90% completitud)
3. Bloquear workflow si documentación incompleta
4. Generar tarea automática para completar documentación

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar `HighRiskClassifierViewModel.java`

Añadir método de validación:

```java
/**
 * Valida que la documentación técnica esté completa antes de clasificar como alto riesgo
 * Requisito: Art. 11 + Anexo IV del EU AI Act
 */
private void validateTechnicalDocumentation() {
    if (currentProject == null || projectId == null) {
        return;
    }
    
    Model model = getModelForProject(projectId);
    
    if (model == null) {
        log.warn("Proyecto {} no tiene modelo asociado - no se puede validar documentación", projectId);
        return;
    }
    
    // Validar documentación técnica para modelos alto riesgo
    if (model.getModishighrisk() != null && model.getModishighrisk()) {
        
        // Validación 1: MODTECHNICALDOCCOMPLETE debe ser true
        if (model.getModtechnicaldoccomplete() == null || !model.getModtechnicaldoccomplete()) {
            String errorMsg = "CRITICAL: Documentación técnica incompleta (Art. 11 + Anexo IV).\n\n" +
                "Según el Art. 11 del EU AI Act, los sistemas de alto riesgo deben tener " +
                "documentación técnica completa antes de ser clasificados.\n\n" +
                "Por favor, complete la documentación técnica del modelo antes de clasificar como alto riesgo.\n\n" +
                "Campos requeridos según Anexo IV:\n" +
                "- Descripción del sistema\n" +
                "- Especificaciones de entrada/salida\n" +
                "- Métricas de precisión\n" +
                "- Datos de entrenamiento\n" +
                "- Evaluación de riesgos\n" +
                "- Medidas de mitigación";
            
            log.error("Validación fallida - Documentación técnica incompleta: Model ID={}", model.getIdxmodel());
            
            Messagebox.show(
                errorMsg,
                "Validación CRÍTICA - Documentación Técnica Incompleta",
                Messagebox.OK,
                Messagebox.ERROR
            );
            
            throw new ValidationException("Documentación técnica incompleta (Art. 11 + Anexo IV)");
        }
        
        // Validación 2: MODTECHNICALDOCSCORE debe ser >= 0.90
        if (model.getModtechnicaldocscore() == null) {
            String errorMsg = "CRITICAL: Score de documentación técnica no calculado.\n\n" +
                "El sistema no ha calculado el score de completitud de la documentación técnica.\n\n" +
                "Por favor, ejecute la evaluación de documentación técnica antes de clasificar.";
            
            log.error("Validación fallida - Score no calculado: Model ID={}", model.getIdxmodel());
            
            Messagebox.show(
                errorMsg,
                "Validación CRÍTICA - Score No Calculado",
                Messagebox.OK,
                Messagebox.ERROR
            );
            
            throw new ValidationException("Score de documentación técnica no calculado");
        }
        
        BigDecimal minScore = new BigDecimal("0.90");
        if (model.getModtechnicaldocscore().compareTo(minScore) < 0) {
            String errorMsg = String.format(
                "CRITICAL: Score de documentación técnica insuficiente: %.2f%%. Mínimo requerido: 90%%.\n\n" +
                "Según el Art. 11 del EU AI Act, la documentación técnica debe estar completa (≥90%%) " +
                "antes de clasificar como alto riesgo.\n\n" +
                "Score actual: %.2f%%\n" +
                "Score requerido: 90.00%%\n\n" +
                "Por favor, complete la documentación técnica faltante antes de clasificar.",
                model.getModtechnicaldocscore().multiply(new BigDecimal("100")).doubleValue(),
                model.getModtechnicaldocscore().multiply(new BigDecimal("100")).doubleValue()
            );
            
            log.error("Validación fallida - Score insuficiente: Model ID={}, Score={}", 
                model.getIdxmodel(), model.getModtechnicaldocscore());
            
            Messagebox.show(
                errorMsg,
                "Validación CRÍTICA - Score Insuficiente",
                Messagebox.OK,
                Messagebox.ERROR
            );
            
            throw new ValidationException(
                String.format("Score de documentación técnica insuficiente: %s. Mínimo requerido: 0.90", 
                    model.getModtechnicaldocscore())
            );
        }
        
        // Validación 3: MODTECHNICALDOCURL debe existir
        if (model.getModtechnicaldocurl() == null || model.getModtechnicaldocurl().trim().isEmpty()) {
            String errorMsg = "CRITICAL: URL de documentación técnica no especificada.\n\n" +
                "La documentación técnica debe tener una URL válida donde se puede acceder al documento.\n\n" +
                "Por favor, especifique la URL de la documentación técnica.";
            
            log.error("Validación fallida - URL no especificada: Model ID={}", model.getIdxmodel());
            
            Messagebox.show(
                errorMsg,
                "Validación CRÍTICA - URL No Especificada",
                Messagebox.OK,
                Messagebox.ERROR
            );
            
            throw new ValidationException("URL de documentación técnica no especificada");
        }
        
        log.info("Validación exitosa - Documentación técnica completa: Model ID={}, Score={}", 
            model.getIdxmodel(), model.getModtechnicaldocscore());
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
        log.error("Validación dataset fallida: {}", e.getMessage());
        return;
    }
    
    // NUEVA VALIDACIÓN: Documentación técnica completa (INC-003)
    try {
        validateTechnicalDocumentation();
    } catch (ValidationException e) {
        log.error("Validación documentación técnica fallida: {}", e.getMessage());
        return; // Bloquear clasificación
    }
    
    try {
        classifying = true;
        // ... resto del código existente ...
```

### 2. Añadir Imports

```java
import java.math.BigDecimal;
```

---

## GENERAR TAREA AUTOMÁTICA

Si la documentación está incompleta, generar tarea automática:

```java
/**
 * Genera tarea automática para completar documentación técnica
 */
private void createTechnicalDocTask(Model model) {
    try {
        // Crear tarea en sistema de tareas (ajustar según tu sistema)
        String taskDescription = String.format(
            "Completar documentación técnica del modelo %s (ID: %d).\n\n" +
            "Score actual: %.2f%%\n" +
            "Score requerido: 90.00%%\n\n" +
            "Requisito: Art. 11 + Anexo IV EU AI Act",
            model.getModname(),
            model.getIdxmodel(),
            model.getModtechnicaldocscore() != null ? 
                model.getModtechnicaldocscore().multiply(new BigDecimal("100")).doubleValue() : 0.0
        );
        
        // TODO: Integrar con sistema de tareas
        // taskService.createTask(
        //     "COMPLETE_TECHNICAL_DOC",
        //     taskDescription,
        //     model.getIdxmodel(),
        //     "HIGH",
        //     ctxBean.getUser().getIdxuser()
        // );
        
        log.info("Tarea generada para completar documentación técnica: Model ID={}", model.getIdxmodel());
        
    } catch (Exception e) {
        log.error("Error generando tarea de documentación técnica", e);
    }
}
```

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Clasificar con `MODTECHNICALDOCCOMPLETE = false` → Debe bloquear
2. **Test 2:** Clasificar con `MODTECHNICALDOCCOMPLETE = true` pero `MODTECHNICALDOCSCORE < 0.90` → Debe bloquear
3. **Test 3:** Clasificar con documentación completa y score ≥ 0.90 → Debe permitir
4. **Test 4:** Clasificar sin URL de documentación → Debe bloquear

---

## REFERENCIAS

- **Art. 11 EU AI Act:** Documentación Técnica
- **Anexo IV:** Requisitos de Documentación Técnica
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-003`

---

**Estado:** ✅ COMPLETADO
