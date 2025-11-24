# PROMPT: INC-022 - Caché Evaluaciones Técnicas

**Incidencia:** INC-022  
**Prioridad:** 🟢 BAJA  
**Artículo EU AI Act:** Art. 15  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## IMPLEMENTACIÓN

```java
@Cacheable(value = "evaluationResults", key = "#modelId + '_' + #evaluationType")
public EvaluationResult getEvaluationResult(String modelId, String evaluationType) {
    return evaluationService.executeEvaluation(modelId, evaluationType);
}

@CacheEvict(value = "evaluationResults", key = "#modelId + '_*'")
public void invalidateEvaluationCache(String modelId) {
    // Caché invalidado automáticamente
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-022`

---

**Estado:** ✅ COMPLETADO

