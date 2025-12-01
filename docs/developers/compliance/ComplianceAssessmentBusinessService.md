# ComplianceAssessmentBusinessService

**Ubicación:** `com.codeflowx.govern.business.compliance.ComplianceAssessmentBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

El `ComplianceAssessmentBusinessService` gestiona el proceso completo de **evaluación de conformidad (Conformity Assessment)** según el **Anexo VI y Art. 43 del EU AI Act**. Este proceso es obligatorio para sistemas de IA de alto riesgo antes de su certificación.

### Propósito

- Gestionar el ciclo completo de evaluación de conformidad
- Ejecutar los 4 pasos del proceso de evaluación
- Calcular scores de compliance
- Determinar si un sistema está listo para certificación

---

## 🎯 Responsabilidades

### ✅ Qué Hace Este Servicio

1. **Gestión del Proceso de Evaluación**
   - Crear nuevas evaluaciones de conformidad
   - Ejecutar los 4 pasos del proceso (Steps 2-4)
   - Calcular score overall
   - Determinar readiness para certificación

2. **Integración con Otros Servicios**
   - Step 2: Integra con `QualityManagementSystemBusinessService`
   - Step 3: Integra con `TechnicalDocumentationBusinessService`
   - Step 4: Verificación de consistencia interna

3. **Consultas**
   - Obtener evaluaciones por proyecto
   - Obtener última evaluación de un proyecto

### ❌ Qué NO Hace Este Servicio

- **NO** gestiona el contenido de QMS (eso lo hace QualityManagementSystemBusinessService)
- **NO** gestiona documentación técnica (eso lo hace TechnicalDocumentationBusinessService)
- **NO** emite certificaciones (solo determina readiness)

---

## 🔗 Dependencias

### Entidades
- `ComplianceAssessment`
- `Project`

### Servicios
- `QualityManagementSystemBusinessService` - Para Step 2
- `TechnicalDocumentationBusinessService` - Para Step 3
- `BusinessService` (DAO)

---

## 🏗️ Arquitectura

### Proceso de Evaluación (Anexo VI)

```
Step 1: Crear Assessment
    ↓
Step 2: QMS Compliance Check
    ↓ (QualityManagementSystemBusinessService)
Step 3: Technical Documentation Review
    ↓ (TechnicalDocumentationBusinessService)
Step 4: Process Consistency Check
    ↓
Calcular Overall Score
    ↓
Determinar Readiness for Certification
```

### Cálculo de Scores

```
overallScore = (step2Score + step3Score + step4Score) / 3
readyForCertification = overallScore >= 0.80 && annexVICompliant == true
```

---

## 📚 API Pública

### `createAssessment(Long projectId, String assessmentType)`

Crea nueva evaluación de conformidad.

**Parámetros:**
- `projectId` (Long): ID del proyecto
- `assessmentType` (String): Tipo de evaluación (FULL_ASSESSMENT, PERIODIC_REVIEW, etc.)

**Retorna:** `ComplianceAssessment`

**Ejemplo:**
```java
ComplianceAssessment assessment = complianceService.createAssessment(
    projectId, "FULL_ASSESSMENT");
```

---

### `executeStep2QmsCheck(Long assessmentId)`

Ejecuta Step 2: Verificación de QMS (Art. 17).

**Parámetros:**
- `assessmentId` (Long): ID del assessment

**Ejemplo:**
```java
complianceService.executeStep2QmsCheck(assessmentId);
BigDecimal qmsScore = assessment.getComstep2qmsscore();
```

---

### `executeStep3DocReview(Long assessmentId)`

Ejecuta Step 3: Revisión de documentación técnica (Anexo IV).

**Parámetros:**
- `assessmentId` (Long): ID del assessment

**Ejemplo:**
```java
complianceService.executeStep3DocReview(assessmentId);
BigDecimal docScore = assessment.getComstep3docscore();
```

---

### `executeStep4ConsistencyCheck(Long assessmentId)`

Ejecuta Step 4: Verificación de consistencia del proceso.

**Parámetros:**
- `assessmentId` (Long): ID del assessment

**Nota:** Actualmente retorna score fijo (0.85). Requiere implementación real.

---

### `calculateOverallScore(Long assessmentId)`

Calcula score overall como promedio de Steps 2, 3 y 4.

**Parámetros:**
- `assessmentId` (Long): ID del assessment

**Retorna:** `BigDecimal` (0.00 - 1.00) o `null` si algún step no está completo

**Ejemplo:**
```java
BigDecimal overallScore = complianceService.calculateOverallScore(assessmentId);
```

---

### `isReadyForCertification(Long assessmentId)`

Determina si el sistema está listo para certificación.

**Criterios:**
- `overallScore >= 0.80`
- `annexVICompliant == true`
- Todos los steps (2, 3, 4) completados

**Retorna:** `Boolean`

**Ejemplo:**
```java
Boolean ready = complianceService.isReadyForCertification(assessmentId);
if (ready) {
    // Proceder con certificación
}
```

---

## 💡 Casos de Uso

### Caso 1: Proceso Completo de Evaluación

```java
// 1. Crear assessment
ComplianceAssessment assessment = complianceService.createAssessment(
    projectId, "FULL_ASSESSMENT");

// 2. Ejecutar steps
complianceService.executeStep2QmsCheck(assessment.getIdxcomplianceassessment());
complianceService.executeStep3DocReview(assessment.getIdxcomplianceassessment());
complianceService.executeStep4ConsistencyCheck(assessment.getIdxcomplianceassessment());

// 3. Calcular score overall
BigDecimal overallScore = complianceService.calculateOverallScore(
    assessment.getIdxcomplianceassessment());

// 4. Verificar readiness
Boolean ready = complianceService.isReadyForCertification(
    assessment.getIdxcomplianceassessment());
```

---

## 🧪 Testing

```java
@Test
public void testCompleteAssessmentWorkflow() throws BussinessException {
    ComplianceAssessment assessment = complianceService.createAssessment(
        projectId, "FULL_ASSESSMENT");

    complianceService.executeStep2QmsCheck(assessment.getIdxcomplianceassessment());
    complianceService.executeStep3DocReview(assessment.getIdxcomplianceassessment());
    complianceService.executeStep4ConsistencyCheck(assessment.getIdxcomplianceassessment());

    BigDecimal overallScore = complianceService.calculateOverallScore(
        assessment.getIdxcomplianceassessment());

    assertNotNull(overallScore);
    assertTrue(overallScore.compareTo(BigDecimal.ZERO) > 0);
}
```

---

## 🔄 Evolución

### Mejoras Futuras

1. **Step 4 Real:** Implementar lógica real de verificación de consistencia
2. **Workflow BPMN:** Integrar con workflows de aprobación
3. **Versionado:** Soporte para versiones de assessments

---

## 📖 Referencias

- **Art. 43 EU AI Act:** Conformity Assessment
- **Anexo VI:** Proceso de evaluación
- **Prompts:** INC-043, INC-017, INC-ANEXO-IV, INC-024

---

**Última actualización:** 25 de noviembre de 2025
