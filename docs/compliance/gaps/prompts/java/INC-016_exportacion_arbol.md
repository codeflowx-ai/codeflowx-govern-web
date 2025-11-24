# PROMPT: INC-016 - Exportación Árbol Riesgo

**Incidencia:** INC-016  
**Prioridad:** 🟢 BAJA  
**Artículo EU AI Act:** Art. 27  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Frontend

---

## IMPLEMENTACIÓN

```java
@Listen("onClick = #btnExportRiskTree")
public void exportRiskTree() {
    RiskTree tree = buildRiskTree(friaData);
    
    // Exportar PDF
    byte[] pdf = riskTreeExporter.exportToPDF(tree);
    Filedownload.save(pdf, "application/pdf", "fria-risk-tree.pdf");
    
    // Exportar JSON
    String json = riskTreeExporter.exportToJSON(tree);
    Filedownload.save(json.getBytes(), "application/json", "fria-risk-tree.json");
    
    // Exportar imagen
    byte[] image = riskTreeExporter.exportToPNG(tree);
    Filedownload.save(image, "image/png", "fria-risk-tree.png");
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-016`

---

**Estado:** ✅ COMPLETADO

