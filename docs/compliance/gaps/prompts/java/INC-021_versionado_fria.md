# PROMPT: INC-021 - Versionado FRIA

**Incidencia:** INC-021  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 27  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend

---

## IMPLEMENTACIÓN

```java
// Modificar entidad para soportar versionado
@Column(name = "FRIAVERSION")
private Integer friaversion = 1;

@Column(name = "FRIAPREVIOUSVERSIONID")
private Long friapreviousversionid;

// Al modificar, crear nueva versión
public FriaAssessment createNewVersion(Long friaId) {
    FriaAssessment current = friaService.getFria(friaId);
    FriaAssessment newVersion = new FriaAssessment();
    copyFriaData(current, newVersion);
    newVersion.setFriaversion(current.getFriaversion() + 1);
    newVersion.setFriapreviousversionid(current.getIdxfriaassessment());
    return newVersion;
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-021`

---

**Estado:** ✅ COMPLETADO

