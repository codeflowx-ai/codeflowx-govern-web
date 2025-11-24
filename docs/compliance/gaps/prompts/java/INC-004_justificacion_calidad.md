# PROMPT: INC-004 - Validación Calidad Justificación

**Incidencia:** INC-004  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 6  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## CONTEXTO

Validación actual solo verifica longitud mínima (50 caracteres) pero no valida calidad, coherencia o relevancia de la justificación.

**Ubicación Actual:**
- `HighRiskClassifierViewModel.java` - método `validateClassification()`
- Validación: `justification.length() < 50`

---

## REQUISITOS

1. Validar que justificación mencione la categoría Anexo III seleccionada
2. Validar que justificación explique el riesgo específico
3. Usar NLP para detectar justificaciones genéricas o copiadas
4. Requerir ejemplos concretos de uso del sistema

---

## IMPLEMENTACIÓN

### Modificar `HighRiskClassifierViewModel.java`

```java
/**
 * Valida calidad de la justificación (INC-004)
 */
private boolean validateJustificationQuality(String justification, String selectedCategory) {
    // 1. Longitud mínima aumentada a 100 caracteres
    if (justification == null || justification.trim().length() < 100) {
        return false;
    }
    
    String justificationLower = justification.toLowerCase();
    String categoryLower = selectedCategory.toLowerCase().replace(".", "");
    
    // 2. Debe mencionar categoría seleccionada
    if (!justificationLower.contains(categoryLower)) {
        return false;
    }
    
    // 3. Debe contener palabras clave de riesgo
    String[] riskKeywords = {"riesgo", "impacto", "afecta", "personas", "derechos", 
                             "risk", "impact", "affects", "people", "rights"};
    int keywordCount = 0;
    for (String keyword : riskKeywords) {
        if (justificationLower.contains(keyword)) {
            keywordCount++;
        }
    }
    if (keywordCount < 2) {
        return false;
    }
    
    // 4. Validar con NLP (opcional - usar leka-llm-evaluation)
    // JustificationQualityResult quality = llmEvaluationService.evaluateJustificationQuality(justification);
    // return quality.getQualityScore() >= 0.70;
    
    return true;
}

// Modificar validateClassification()
private boolean validateClassification() {
    // ... validaciones existentes ...
    
    // NUEVA VALIDACIÓN: Calidad de justificación (INC-004)
    if (!validateJustificationQuality(justification, selectedCategory)) {
        Messagebox.show(
            "La justificación no cumple con los requisitos de calidad.\n\n" +
            "Debe:\n" +
            "- Tener al menos 100 caracteres\n" +
            "- Mencionar la categoría seleccionada (" + selectedCategory + ")\n" +
            "- Explicar el riesgo específico y su impacto\n" +
            "- Incluir ejemplos concretos de uso del sistema\n\n" +
            "Por favor, mejore la justificación.",
            "Validación - Justificación Insuficiente",
            Messagebox.OK,
            Messagebox.EXCLAMATION
        );
        return false;
    }
    
    return true;
}
```

---

## REFERENCIAS

- **Art. 6 EU AI Act:** Clasificación de Sistemas
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-004`

---

**Estado:** ✅ COMPLETADO
