# PROMPT: INC-023 - Validación Calidad FRIA

**Incidencia:** INC-023  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 27  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend

---

## IMPLEMENTACIÓN

```java
public BigDecimal calculateQualityScore(FriaAssessment fria) {
    BigDecimal score = BigDecimal.ZERO;
    
    // Calidad de descripción de procesos
    BigDecimal processDescQuality = evaluateTextQuality(fria.getFriaprocessdescription());
    score = score.add(processDescQuality.multiply(new BigDecimal("0.20")));
    
    // Calidad de riesgos (específicos vs genéricos)
    BigDecimal risksQuality = evaluateRisksQuality(parseRisks(fria.getFriarisks()));
    score = score.add(risksQuality.multiply(new BigDecimal("0.30")));
    
    // Calidad de medidas (específicas vs genéricas)
    BigDecimal measuresQuality = evaluateMeasuresQuality(parseMeasures(fria.getFriamitigationmeasures()));
    score = score.add(measuresQuality.multiply(new BigDecimal("0.30")));
    
    // Calidad de supervisión humana
    BigDecimal oversightQuality = evaluateTextQuality(fria.getFriahumanoversight());
    score = score.add(oversightQuality.multiply(new BigDecimal("0.20")));
    
    return score;
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-023`

---

**Estado:** ✅ COMPLETADO

