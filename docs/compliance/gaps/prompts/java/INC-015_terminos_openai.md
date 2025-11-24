# PROMPT: INC-015 - Verificación Términos OpenAI

**Incidencia:** INC-015  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 10  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## IMPLEMENTACIÓN

```java
public void validateOpenAITerms(String modelId, Project project) {
    OpenAITerms terms = getOpenAITerms(modelId);
    
    // Verificar uso de datos personales
    if (project.getPrjishighrisk() && 
        project.getFriaaffectedcategories().contains("General public")) {
        if (!terms.isDataProcessingAllowed()) {
            throw new ValidationException(
                "Términos de OpenAI no permiten procesamiento de datos personales"
            );
        }
    }
    
    // Verificar opt-out de entrenamiento
    if (!terms.isDataOptOutEnabled()) {
        addWarning("Datos pueden ser usados para entrenamiento OpenAI");
    }
    
    documentTermsCompliance(modelId, terms, project);
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-015`

---

**Estado:** ✅ COMPLETADO

