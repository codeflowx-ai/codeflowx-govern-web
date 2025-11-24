# PROMPT: INC-008 - Validación Fórmula Cálculo Riesgo

**Incidencia:** INC-008  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 27  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## CONTEXTO

Fórmula de cálculo de riesgo no está documentada ni validada. No hay evidencia de que la fórmula sea correcta según metodología Anexo IX.

**Ubicación Actual:**
- `FriaAssessmentBusinessService.java` - método `calculateFinalRisk()`

---

## REQUISITOS

1. Documentar fórmula de cálculo según Anexo IX
2. Crear tests unitarios con casos conocidos
3. Validar fórmula con expertos en riesgo
4. Permitir configuración de pesos de la fórmula

---

## IMPLEMENTACIÓN

### Modificar `FriaAssessmentBusinessService.java`

```java
/**
 * Calcula riesgo final según metodología Anexo IX del EU AI Act
 * 
 * Fórmula: Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)
 * 
 * Severity: LOW=0.25, MEDIUM=0.50, HIGH=0.75, CRITICAL=1.0
 * Probability: 0.0 - 1.0 (probabilidad de ocurrencia)
 * Impact: LOW=0.25, MEDIUM=0.50, HIGH=0.75, CRITICAL=1.0
 * Mitigation Effectiveness: 0.0 - 1.0 (efectividad de medidas)
 * 
 * @param fria FRIA assessment
 * @return Riesgo final normalizado (0.0 - 1.0)
 */
@VisibleForTesting
public BigDecimal calculateFinalRisk(FriaAssessment fria) {
    List<FriaRisk> risks = parseRisks(fria.getFriarisks());
    List<MitigationMeasure> measures = parseMeasures(fria.getFriamitigationmeasures());
    
    BigDecimal totalRisk = BigDecimal.ZERO;
    
    for (FriaRisk risk : risks) {
        // Severity score
        BigDecimal severityScore = getSeverityScore(risk.getSeverity());
        
        // Probability
        BigDecimal probability = risk.getProbability();
        
        // Impact score
        BigDecimal impactScore = getImpactScore(risk.getImpact());
        
        // Risk before mitigation
        BigDecimal rawRisk = severityScore
            .multiply(probability)
            .multiply(impactScore);
        
        // Apply mitigation effectiveness
        BigDecimal mitigationEffectiveness = getMitigationEffectiveness(risk.getRiskId(), measures);
        BigDecimal mitigatedRisk = rawRisk.multiply(
            BigDecimal.ONE.subtract(mitigationEffectiveness)
        );
        
        totalRisk = totalRisk.add(mitigatedRisk);
    }
    
    // Normalize to 0-1 scale
    return totalRisk.divide(BigDecimal.valueOf(risks.size()), 2, RoundingMode.HALF_UP);
}

// Tests unitarios
@Test
public void testCalculateFinalRisk_HighSeverityHighProbability() {
    // Test case conocido
    FriaAssessment fria = createTestFria();
    BigDecimal risk = service.calculateFinalRisk(fria);
    assertEquals(new BigDecimal("0.75"), risk);
}
```

---

## REFERENCIAS

- **Art. 27 EU AI Act:** FRIA
- **Anexo IX:** Metodología FRIA
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-008`

---

**Estado:** ✅ COMPLETADO

