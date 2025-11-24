# PROMPT: INC-014 - Retención Histórica Evaluaciones

**Incidencia:** INC-014  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 12, Art. 18  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend

---

## IMPLEMENTACIÓN

### Crear tabla de historial

```sql
CREATE TABLE GOVEVALUATIONHISTORY (
    IDXEVALUATIONHISTORY BIGSERIAL PRIMARY KEY,
    EVALUATIONTYPE VARCHAR(50) NOT NULL,
    ENTITYTYPE VARCHAR(50) NOT NULL,
    ENTITYID BIGINT NOT NULL,
    EVALUATIONDATE TIMESTAMP NOT NULL,
    METRICS JSONB NOT NULL,
    RESULT VARCHAR(20) NOT NULL,
    CREATEDAT TIMESTAMP NOT NULL
);
```

### Almacenar en cada evaluación

```java
public void storeEvaluationHistory(String type, String entityType, Long entityId, 
                                   Map<String, Object> metrics, String result) {
    EvaluationHistory history = new EvaluationHistory();
    history.setEvaluationType(type);
    history.setEntityType(entityType);
    history.setEntityId(entityId);
    history.setEvaluationDate(new Timestamp(System.currentTimeMillis()));
    history.setMetrics(serializeMetrics(metrics));
    history.setResult(result);
    evaluationHistoryDAO.save(history);
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-014`

---

**Estado:** ✅ COMPLETADO

