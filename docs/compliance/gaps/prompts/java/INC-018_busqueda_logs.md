# PROMPT: INC-018 - Búsqueda Avanzada Logs

**Incidencia:** INC-018  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 19  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend + Frontend

---

## IMPLEMENTACIÓN

```java
public List<ImmutableLog> searchLogs(LogSearchCriteria criteria) {
    return immutableLogDAO.search(
        criteria.getEntityType(),
        criteria.getEntityId(),
        criteria.getAction(),
        criteria.getDateFrom(),
        criteria.getDateTo(),
        criteria.getUserId(),
        criteria.getTextSearch() // Búsqueda en IMLDATA
    );
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-018`

---

**Estado:** ✅ COMPLETADO

