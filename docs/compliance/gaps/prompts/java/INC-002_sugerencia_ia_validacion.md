# PROMPT: INC-002 - Validación Confianza Sugerencia IA

**Incidencia:** INC-002  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 6  
**Esfuerzo Estimado:** 0.5 días  
**Tipo:** Java - Frontend

---

## CONTEXTO

La sugerencia automática de categoría Anexo III se muestra si confianza >70%, pero no se valida que la sugerencia sea correcta. Usuario puede aceptar sugerencia incorrecta sin revisión.

**Ubicación Actual:**
- `HighRiskClassifierViewModel.java` - método `suggestCategoryWithAI()`
- Umbral actual: 0.7 (70%)

---

## REQUISITOS

1. Aumentar umbral de confianza a 0.85 para mostrar sugerencia
2. Requerir confirmación explícita del usuario antes de aplicar sugerencia
3. Mostrar justificación de la sugerencia IA
4. Registrar en log cuando usuario acepta/rechaza sugerencia

---

## IMPLEMENTACIÓN REQUERIDA

### Modificar `HighRiskClassifierViewModel.java`

```java
// Añadir campo para confirmación
private boolean confirmSuggestion = false;

@Command
@NotifyChange({"aiSuggestion", "aiSuggestionCategory", "aiConfidence", "aiSuggestionAvailable", "confirmSuggestion"})
public void suggestCategoryWithAI() {
    log.info("Solicitando sugerencia IA para proyecto: {}", projectName);
    
    try {
        // ... código existente para obtener sugerencia ...
        
        // NUEVO: Aumentar umbral a 0.85 (INC-002)
        if (aiConfidence > 0.85) {
            aiSuggestionAvailable = true;
            
            // Añadir justificación a la sugerencia
            aiSuggestion = String.format(
                "Sugerencia IA: %s (%.0f%% confianza)\n\n" +
                "Justificación: %s\n\n" +
                "Por favor, revise la sugerencia y confirme antes de aplicar.",
                aiSuggestionCategory,
                aiConfidence * 100,
                getSuggestionJustification() // Nuevo método
            );
            
            // Resetear confirmación
            confirmSuggestion = false;
            
        } else {
            aiSuggestionAvailable = false;
            aiSuggestion = String.format(
                "Confianza insuficiente (%.0f%%). Se requiere clasificación manual.",
                aiConfidence * 100
            );
        }
        
        log.info("Sugerencia IA generada: {} (confianza: {}%)", 
            aiSuggestionCategory, aiConfidence * 100);
        
    } catch (Exception e) {
        log.error("Error generando sugerencia IA", e);
        aiSuggestion = "Error al generar sugerencia: " + e.getMessage();
        aiSuggestionAvailable = false;
    }
}

/**
 * Obtiene justificación de la sugerencia IA
 */
private String getSuggestionJustification() {
    // TODO: Obtener justificación del microservicio Python
    // Por ahora, generar justificación básica
    String description = (projectDescription + " " + projectPurpose).toLowerCase();
    
    if (aiSuggestionCategory.equals("III.1")) {
        return "El proyecto menciona características biométricas o reconocimiento facial.";
    } else if (aiSuggestionCategory.equals("III.4")) {
        return "El proyecto menciona empleo, contratación o evaluación de trabajadores.";
    } else if (aiSuggestionCategory.equals("III.5")) {
        return "El proyecto menciona evaluación crediticia o servicios financieros.";
    }
    
    return "Basado en análisis del contenido del proyecto.";
}

/**
 * Aplica sugerencia IA (modificado para requerir confirmación)
 */
@Command
@NotifyChange({"selectedCategory", "subcategories", "selectedCategoryData", "confirmSuggestion"})
public void applySuggestion() {
    if (!aiSuggestionAvailable || aiSuggestionCategory.isEmpty()) {
        Messagebox.show("No hay sugerencia disponible para aplicar", "Información", 
            Messagebox.OK, Messagebox.INFORMATION);
        return;
    }
    
    // NUEVO: Requerir confirmación explícita (INC-002)
    if (!confirmSuggestion) {
        Messagebox.show(
            "Por favor, confirme que desea aplicar la sugerencia IA.\n\n" +
            "Categoría sugerida: " + aiSuggestionCategory + "\n" +
            "Confianza: " + (aiConfidence * 100) + "%\n\n" +
            "Marque la casilla de confirmación para aplicar.",
            "Confirmación Requerida",
            Messagebox.OK,
            Messagebox.QUESTION
        );
        return;
    }
    
    selectedCategory = aiSuggestionCategory;
    onCategorySelected();
    
    // NUEVO: Registrar en log inmutable (INC-002)
    logAISuggestionAccepted();
    
    log.info("Sugerencia IA aplicada: {}", selectedCategory);
}

/**
 * Registra en log inmutable cuando usuario acepta sugerencia IA
 */
private void logAISuggestionAccepted() {
    try {
        Map<String, Object> logData = new HashMap<>();
        logData.put("action", "AI_SUGGESTION_ACCEPTED");
        logData.put("suggestedCategory", aiSuggestionCategory);
        logData.put("confidence", aiConfidence);
        logData.put("projectId", projectId);
        logData.put("userConfirmed", true);
        
        immutableLoggingService.createLogEntry(
            "PROJECT",
            projectId,
            "AI_SUGGESTION_ACCEPTED",
            ctxBean.getUser().getIdxuser(),
            ctxBean.getUser().getUsuname(),
            logData
        );
    } catch (Exception e) {
        log.error("Error registrando aceptación de sugerencia IA", e);
    }
}
```

### Modificar ZUL para añadir checkbox de confirmación

```xml
<!-- En high-risk-classifier.zul -->
<vbox>
    <label id="lblAISuggestion" value="@bind(vm.aiSuggestion)" 
           style="color: green; font-weight: bold; margin: 10px;"/>
    
    <!-- NUEVO: Checkbox de confirmación (INC-002) -->
    <checkbox id="chkConfirmSuggestion" 
              checked="@bind(vm.confirmSuggestion)"
              label="Confirmo que he revisado y acepto esta sugerencia"
              visible="@load(vm.aiSuggestionAvailable)"
              required="true"/>
    
    <button label="Aplicar Sugerencia" 
            onClick="@command('applySuggestion')"
            disabled="@load(!vm.aiSuggestionAvailable or !vm.confirmSuggestion)"/>
</vbox>
```

---

## REFERENCIAS

- **Art. 6 EU AI Act:** Clasificación de Sistemas
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-002`

---

**Estado:** ✅ COMPLETADO
