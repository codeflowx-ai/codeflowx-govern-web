# PROMPT: INC-009 - Persistencia Estado Árbol Riesgo

**Incidencia:** INC-009  
**Prioridad:** 🟢 BAJA  
**Artículo EU AI Act:** Art. 27  
**Esfuerzo Estimado:** 0.5 días  
**Tipo:** Java - Frontend

---

## CONTEXTO

Árbol de riesgo se recalcula en memoria pero no se persiste estado intermedio. Si usuario cierra wizard, pierde cambios.

**Ubicación:** `FriaWizardViewModel.java`

---

## IMPLEMENTACIÓN

```java
@Listen("onChange = #cmbRiskSeverity")
public void onRiskSeverityChanged() {
    recalculateRiskTree();
    autoSaveFriaState(); // Auto-guardar cada cambio
    lblAutoSaveStatus.setValue("Guardado: " + new Date());
}

private void autoSaveFriaState() {
    // Guardar estado en sesión o BD temporal
    FriaDraftState draft = new FriaDraftState();
    draft.setProjectId(projectId);
    draft.setRisks(risks);
    draft.setMitigationMeasures(mitigationMeasures);
    draft.setLastSaved(new Timestamp(System.currentTimeMillis()));
    friaDraftService.saveDraft(draft);
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-009`

---

**Estado:** ✅ COMPLETADO

