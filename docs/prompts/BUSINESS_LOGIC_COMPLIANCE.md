# PROMPT DE LÓGICA DE NEGOCIO - MÓDULO COMPLIANCE

**Fecha:** Diciembre 2025
**Módulo:** Compliance (Cumplimiento EU AI Act)
**Objetivo:** Definir la lógica de negocio completa para el módulo de compliance
**Esfuerzo Estimado:** 5-6 días

> **⚠️ IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** en este prompt.
> Las operaciones CRUD y consultas BBDD están implementadas, pero las integraciones con microservicios externos están pendientes de implementar cuando estén disponibles.

---

## 📋 CONTEXTO DEL MÓDULO

### **Descripción Funcional**
El módulo Compliance gestiona el cumplimiento completo con el EU AI Act, incluyendo:
- Evaluaciones de conformidad (Anexo VI, Art. 43)
- Evaluaciones de Impacto en Derechos Fundamentales - FRIA (Art. 27)
- Registro en Base de Datos UE (Art. 49)
- Verificación de sistemas prohibidos (Art. 5)
- Gestión de documentación técnica (Art. 11, Anexo IV)
- Quality Management System - QMS (Art. 17)
- Logs inmutables (Art. 19)
- Catálogo de categorías alto riesgo (Anexo III)

### **Pantallas Asociadas**

#### **ViewModels Identificados (5+ ViewModels)**
**Referencia:** `suinsit.nova.web/docs/funcional/compliance/README_COMPLIANCE.md`

**ViewModels Principales:**
- `ComplianceAssessmentViewModel` - Evaluaciones de conformidad
- `FriaAssessmentViewModel` - Evaluaciones FRIA
- `ComplianceAssessmentDetailViewModel` - Detalles de evaluación
- `ComplianceAssessmentOverviewViewModel` - Vista general de evaluaciones
- `ComplianceFindingDetailViewModel` - Detalles de hallazgos
- `ComplianceByFrameworkOverviewViewModel` - Vista por framework
- `ComplianceAiActViewModel` - Compliance con AI Act
- `ComplianceReviewViewModel` - Revisión de compliance (BPMN)
- `ComplianceReviewDecisionViewModel` - Decisión de compliance (BPMN)

#### **Pantallas ZUL Identificadas (12 pantallas)**
**Referencia:** `suinsit.nova.web/docs/funcional/compliance/01_REORGANIZACION_PANTALLAS_COMPLIANCE.md`

- **8 pantallas** en `console/platform/governance/compliance/`:
  - `assessment.zul`, `by-framework.zul`, `finding-overview.zul`, `finding.zul`
  - `gaps-analysis.zul`, `page.zul`, `requirement-overview.zul`, `requirement.zul`
- **2 pantallas BPMN** en `console/bpmn/`:
  - `compliance-review-decision-form.zul`, `compliance-review-form.zul`
- **2 pantallas SLA** en `console/platform/serving/`:
  - `serving-sla-compliance-overview.zul`, `sla-compliance-overview.zul`

#### **Pantallas Next.js Migradas (14+ pantallas)**
**Ubicación:** `app/(app)/governance/compliance/` y `app/(app)/bpmn/forms/`

**Pantallas Principales Migradas:**
- ✅ `app/(app)/governance/compliance/page.tsx` - Compliance principal
- ✅ `app/(app)/governance/compliance/dashboard/page.tsx` - Dashboard de compliance
- ✅ `app/(app)/governance/compliance/sector/page.tsx` - Compliance por sector
- ✅ `app/(app)/governance/compliance/post-market-monitoring/page.tsx` - Monitoreo post-mercado
- ✅ `app/(app)/governance/compliance/conformity-review/page.tsx` - Revisión de conformidad
- ✅ `app/(app)/governance/compliance/conformity-declaration-manager/page.tsx` - Gestión de declaraciones
- ✅ `app/(app)/governance/page.tsx` - Governance principal
- ✅ `app/(app)/governance/overview/page.tsx` - Vista general de governance
- ✅ `app/(app)/governance/detail/[id]/page.tsx` - Detalle de governance
- ✅ `app/(app)/governance/risk-assessment/page.tsx` - Evaluación de riesgos
- ✅ `app/(app)/governance/monitoring/page.tsx` - Monitoreo
- ✅ `app/(app)/governance/policies/page.tsx` - Políticas
- ✅ `app/(app)/governance/security/page.tsx` - Seguridad
- ✅ `app/(app)/governance/auto-approval/page.tsx` - Auto-aprobación

**Pantallas BPMN Migradas:**
- ✅ `app/(app)/bpmn/forms/compliance-review/page.tsx`
- ✅ `app/(app)/bpmn/forms/compliance-review-decision/page.tsx`
- ✅ `app/(app)/bpmn/forms/review-qms-gaps/page.tsx`
- ✅ `app/(app)/bpmn/forms/complete-documentation/page.tsx`

**Referencia:** `codeflowx-studio/docs/PLAN_MIGRACION_ZUL_VIEWMODELS.md`

### **Entidades JPA Principales**
- `ComplianceAssessment` - Evaluaciones de conformidad
- `FriaAssessment` - Evaluaciones FRIA
- `EuRegistration` - Registros en Base de Datos UE
- `ProhibitedSystem` - Sistemas prohibidos
- `ImmutableLog` - Logs inmutables
- `AnnexIIICategory` - Catálogo categorías alto riesgo
- `QualityManagementSystem` - Sistema de gestión de calidad
- `TechnicalDocumentation` - Documentación técnica

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Business Services Existentes**
1. **FriaAssessmentBusinessService** (✅ Creado)
   - `createFria()` - Crea evaluación FRIA
   - `calculateFinalRisk()` - Calcula riesgo final según Anexo IX
   - `validateFriaComplete()` - Valida completitud FRIA

2. **ComplianceAssessmentBusinessService** (✅ Creado)
   - `createAssessment()` - Crea evaluación de conformidad
   - `executeStep2QmsCheck()` - Ejecuta step 2 (QMS)
   - `executeStep3DocReview()` - Ejecuta step 3 (Documentación)
   - `calculateOverallScore()` - Calcula score overall

3. **ProhibitedSystemBusinessService** (✅ Creado)
   - `checkProhibitedSystem()` - Verifica sistema prohibido
   - `getActiveProhibitedSystems()` - Obtiene sistemas activos

4. **QualityManagementSystemBusinessService** (✅ Creado)
   - `getComplianceStrategy()` - Obtiene estrategia compliance
   - `updateComplianceStrategy()` - Actualiza estrategia
   - `calculateQmsComplianceScore()` - Calcula score QMS

### **Servicios CRUD (codeflowx.govern.services)**
- `ComplianceAssessmentService` - CRUD de evaluaciones
- `FriaAssessmentService` - CRUD de FRIAs
- `EuRegistrationService` - CRUD de registros UE
- `ProhibitedSystemService` - CRUD de sistemas prohibidos
- `ImmutableLogService` - CRUD de logs inmutables

### **Microservicios Python Disponibles (COMENTADOS)**
- `codeflowx-governance-api` - API de governance
- `leka-fria-generator` - Generador de FRIAs
- `codeflowx-aios-telemetry` - Telemetría y métricas

---

## 📚 REFERENCIAS DE PROMPTS JAVA

### **Prompt A.1 - Entidad ComplianceAssessment** (PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md)
**Funcionalidad:**
- 4 steps de evaluación (QMS, Documentación, Consistencia)
- Scores por step (0.00 - 1.00)
- Detección de gaps
- Ready for certification

### **Prompt A.2 - Entidad FriaAssessment** (PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md)
**Funcionalidad:**
- Art. 27.1.a-f: 6 elementos mandatorios
- Análisis Charter UE
- Notificación a autoridades (Art. 27.3)
- Integración con DPIA (Art. 27.4)

---

## 🔍 VALIDACIONES DE AUDITORÍA

### **INC-007: FRIA sin Validación Cruzada con Métricas Técnicas Reales**
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 27

**Validación Requerida:**
```java
// Validar FRIA contra métricas técnicas reales
FriaCrossValidationResult validation = validateFriaAgainstTechnicalMetrics(friaId);
if (validation.getConsistencyScore() < 0.70) {
    throw new ValidationException(
        "CRITICAL: FRIA no coincide con métricas técnicas reales. " +
        "Score de consistencia: " + validation.getConsistencyScore() + ". " +
        "Mínimo requerido: 0.70"
    );
}
```

**Consulta BBDD:**
```sql
SELECT * FROM friafundamentalrightsassessments WHERE idxfriaassessment = ?
```

**Llamada Microservicio Python (COMENTADA - PENDIENTE):**
```java
// TODO: Llamar a microservicio Python para validación cruzada
// PENDIENTE: Implementar cuando microservicio esté disponible
/*
FriaCrossValidationResult validation = friaValidationClient.crossValidate(friaId);
*/
```

### **INC-008: Cálculo de Riesgo sin Validación de Fórmula**
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 27, Anexo IX

**Validación Requerida:**
```java
// Fórmula validada según Anexo IX
// Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)
BigDecimal risk = calculateFinalRisk(fria);
```

---

## 💼 LÓGICA DE NEGOCIO - BUSINESS SERVICES

### **1. FriaAssessmentBusinessService**

#### **1.1. Operaciones CRUD Básicas**

**Método: `createFria(Long projectId, String createdBy)`**
```java
/**
 * Crea una nueva evaluación FRIA
 *
 * Validaciones:
 * - Proyecto existe
 * - Proyecto no tiene FRIA activa
 *
 * Consulta BBDD:
 * SELECT * FROM prjprojects WHERE idxproject = ?
 * SELECT * FROM friafundamentalrightsassessments WHERE idxproject = ? AND friastatus = 'ACTIVE'
 */
public FriaAssessment createFria(Long projectId, String createdBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // Validar que no tenga FRIA activa
    FriaAssessment existingFria = findActiveFria(projectId);
    if (existingFria != null) {
        throw new BusinessException("Proyecto ya tiene una FRIA activa");
    }

    FriaAssessment fria = new FriaAssessment();
    fria.setProject(project);
    fria.setFriastatus("DRAFT");
    fria.setFriacreatedat(new Timestamp(System.currentTimeMillis()));
    fria.setFriacreatedby(createdBy);
    fria.setIduuid(UUID.randomUUID().toString());

    return noCodeClient.save(fria);
}
```

**Método: `completeFria(Long friaId, FriaData data, String completedBy)`**
```java
/**
 * Completa una evaluación FRIA con todos los elementos Art. 27.1
 *
 * Validaciones:
 * - FRIA existe
 * - Todos los elementos Art. 27.1 están completos
 * - Validación cruzada con métricas técnicas (INC-007)
 *
 * Consultas BBDD:
 * SELECT * FROM friafundamentalrightsassessments WHERE idxfriaassessment = ?
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - leka-fria-generator: POST /api/fria/cross-validate
 */
public FriaAssessment completeFria(Long friaId, FriaData data, String completedBy) {
    FriaAssessment fria = noCodeClient.findById(FriaAssessment.class, friaId);
    if (fria == null) {
        throw new EntityNotFoundException("FRIA no encontrada: " + friaId);
    }

    // Completar elementos Art. 27.1
    fria.setFriaprocessdescription(data.getProcessDescription()); // Art. 27.1.a
    fria.setFriausageperiod(data.getUsagePeriod()); // Art. 27.1.b
    fria.setFriausagefrequency(data.getUsageFrequency()); // Art. 27.1.b
    fria.setFriaaffectedcategories(data.getAffectedCategories()); // Art. 27.1.c
    fria.setFriarisks(data.getRisks()); // Art. 27.1.d
    fria.setFriahumanoversight(data.getHumanOversight()); // Art. 27.1.e
    fria.setFriamitigationmeasures(data.getMitigationMeasures()); // Art. 27.1.f

    // Calcular riesgo final (INC-008)
    BigDecimal finalRisk = calculateFinalRisk(fria);
    fria.setFriafinalrisk(finalRisk);

    // TODO: Validación cruzada con métricas técnicas (INC-007)
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        FriaCrossValidationResult validation = friaValidationClient.crossValidate(friaId);
        if (validation.getConsistencyScore() < 0.70) {
            throw new ValidationException(
                "CRITICAL: FRIA no coincide con métricas técnicas reales. " +
                "Score de consistencia: " + validation.getConsistencyScore()
            );
        }
        fria.setFriacrossvalidationresult(validation.toJson());
    } catch (Exception e) {
        log.error("Error en validación cruzada FRIA", e);
        // No fallar, pero registrar error
    }
    */

    fria.setFriastatus("COMPLETED");
    fria.setFriaupdatedby(completedBy);
    fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));

    return noCodeClient.save(fria);
}
```

**Método: `calculateFinalRisk(FriaAssessment fria)`**
```java
/**
 * Calcula el riesgo final según Anexo IX del EU AI Act
 *
 * Fórmula: Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)
 *
 * Consulta BBDD:
 * SELECT * FROM friafundamentalrightsassessments WHERE idxfriaassessment = ?
 */
public BigDecimal calculateFinalRisk(FriaAssessment fria) {
    // Parsear riesgos y medidas desde JSONB
    List<FriaRisk> risks = parseRisks(fria.getFriarisks());
    List<MitigationMeasure> measures = parseMeasures(fria.getFriamitigationmeasures());

    BigDecimal totalRisk = BigDecimal.ZERO;

    for (FriaRisk risk : risks) {
        // Calcular riesgo bruto: Severity × Probability × Impact
        BigDecimal severityScore = getSeverityScore(risk.getSeverity());
        BigDecimal impactScore = getImpactScore(risk.getImpact());
        BigDecimal probability = risk.getProbability();

        BigDecimal rawRisk = severityScore
            .multiply(probability)
            .multiply(impactScore);

        // Aplicar efectividad de mitigación
        BigDecimal mitigationEffectiveness = getMitigationEffectiveness(risk.getId(), measures);
        BigDecimal adjustedRisk = rawRisk.multiply(
            BigDecimal.ONE.subtract(mitigationEffectiveness)
        );

        totalRisk = totalRisk.add(adjustedRisk);
    }

    // Normalizar dividiendo por número de riesgos
    if (!risks.isEmpty()) {
        totalRisk = totalRisk.divide(
            new BigDecimal(risks.size()),
            4,
            RoundingMode.HALF_UP
        );
    }

    return totalRisk;
}
```

#### **1.2. Operaciones de Notificación**

**Método: `notifyAuthority(Long friaId, String notifiedBy)`**
```java
/**
 * Notifica a autoridades según Art. 27.3
 *
 * Consultas BBDD:
 * SELECT * FROM friafundamentalrightsassessments WHERE idxfriaassessment = ?
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-governance-api: POST /api/v1/fria/{friaId}/notify-authority
 */
public void notifyAuthority(Long friaId, String notifiedBy) {
    FriaAssessment fria = noCodeClient.findById(FriaAssessment.class, friaId);
    if (fria == null) {
        throw new EntityNotFoundException("FRIA no encontrada: " + friaId);
    }

    // Validar que FRIA esté completa
    if (!"COMPLETED".equals(fria.getFriastatus())) {
        throw new BusinessException("FRIA debe estar completa antes de notificar a autoridades");
    }

    // TODO: Llamar a microservicio Python para notificación
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        governanceApiClient.notifyAuthority(friaId, fria.toJson());
        fria.setFriaauthoritynotified(true);
        fria.setFriaauthoritynotifiedat(new Timestamp(System.currentTimeMillis()));
        noCodeClient.save(fria);
    } catch (Exception e) {
        log.error("Error notificando a autoridades", e);
        throw new BusinessException("Error en notificación: " + e.getMessage(), e);
    }
    */

    // Por ahora, solo marcar como notificado
    fria.setFriaauthoritynotified(true);
    fria.setFriaauthoritynotifiedat(new Timestamp(System.currentTimeMillis()));
    fria.setFriaupdatedby(notifiedBy);
    fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(fria);
}
```

### **2. ComplianceAssessmentBusinessService**

#### **2.1. Operaciones de Evaluación**

**Método: `executeComplianceAssessment(Long projectId, String assessmentType, String executedBy)`**
```java
/**
 * Ejecuta evaluación completa de conformidad (Anexo VI, Art. 43)
 *
 * Validaciones:
 * - Proyecto existe
 * - Proyecto es alto riesgo
 *
 * Consultas BBDD:
 * SELECT * FROM prjprojects WHERE idxproject = ? AND prjishighrisk = true
 * INSERT INTO comcomplianceassessments (...)
 */
public ComplianceAssessment executeComplianceAssessment(Long projectId, String assessmentType, String executedBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // Validar que proyecto sea alto riesgo
    if (project.getPrjishighrisk() == null || !project.getPrjishighrisk()) {
        throw new BusinessException("Solo proyectos de alto riesgo requieren evaluación de conformidad");
    }

    ComplianceAssessment assessment = new ComplianceAssessment();
    assessment.setIdxproject(projectId);
    assessment.setComassessmenttype(assessmentType);
    assessment.setComassessmentdate(new Timestamp(System.currentTimeMillis()));
    assessment.setComannexvicompliant(false);
    assessment.setComcreatedat(new Timestamp(System.currentTimeMillis()));
    assessment.setIduuid(UUID.randomUUID().toString());

    return noCodeClient.save(assessment);
}
```

**Método: `executeStep2QmsCheck(Long assessmentId, String executedBy)`**
```java
/**
 * Ejecuta step 2 - QMS compliance check
 *
 * Consultas BBDD:
 * SELECT * FROM comcomplianceassessments WHERE idxcomplianceassessment = ?
 * SELECT * FROM prjprojects WHERE idxproject = ?
 */
public void executeStep2QmsCheck(Long assessmentId, String executedBy) {
    ComplianceAssessment assessment = noCodeClient.findById(ComplianceAssessment.class, assessmentId);
    if (assessment == null) {
        throw new EntityNotFoundException("Evaluación no encontrada: " + assessmentId);
    }

    // Calcular score QMS
    BigDecimal qmsScore = qualityManagementSystemBusinessService.calculateQmsComplianceScore(
        assessment.getIdxproject()
    );

    assessment.setComstep2qmsscore(qmsScore);
    assessment.setComupdatedby(executedBy);
    assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(assessment);
}
```

**Método: `executeStep3DocReview(Long assessmentId, String executedBy)`**
```java
/**
 * Ejecuta step 3 - Technical doc review
 *
 * Consultas BBDD:
 * SELECT * FROM comcomplianceassessments WHERE idxcomplianceassessment = ?
 * SELECT * FROM modmodels WHERE idxproject = ?
 */
public void executeStep3DocReview(Long assessmentId, String executedBy) {
    ComplianceAssessment assessment = noCodeClient.findById(ComplianceAssessment.class, assessmentId);
    if (assessment == null) {
        throw new EntityNotFoundException("Evaluación no encontrada: " + assessmentId);
    }

    // Calcular score de documentación
    BigDecimal docScore = technicalDocumentationBusinessService.calculateDocumentationScore(
        assessment.getIdxproject()
    );

    assessment.setComstep3docscore(docScore);
    assessment.setComupdatedby(executedBy);
    assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(assessment);
}
```

**Método: `calculateOverallScore(Long assessmentId)`**
```java
/**
 * Calcula score overall de la evaluación
 *
 * Consulta BBDD:
 * SELECT * FROM comcomplianceassessments WHERE idxcomplianceassessment = ?
 */
public BigDecimal calculateOverallScore(Long assessmentId) {
    ComplianceAssessment assessment = noCodeClient.findById(ComplianceAssessment.class, assessmentId);
    if (assessment == null) {
        throw new EntityNotFoundException("Evaluación no encontrada: " + assessmentId);
    }

    // Calcular promedio de los 3 steps
    BigDecimal step2 = assessment.getComstep2qmsscore() != null ?
        assessment.getComstep2qmsscore() : BigDecimal.ZERO;
    BigDecimal step3 = assessment.getComstep3docscore() != null ?
        assessment.getComstep3docscore() : BigDecimal.ZERO;
    BigDecimal step4 = assessment.getComstep4consistencyscore() != null ?
        assessment.getComstep4consistencyscore() : BigDecimal.ZERO;

    BigDecimal sum = step2.add(step3).add(step4);
    BigDecimal avg = sum.divide(new BigDecimal(3), 4, RoundingMode.HALF_UP);

    assessment.setComoverallscore(avg);
    assessment.setComannexvicompliant(avg.compareTo(new BigDecimal("0.90")) >= 0);
    assessment.setComreadyforcertification(avg.compareTo(new BigDecimal("0.90")) >= 0);
    assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));

    noCodeClient.save(assessment);

    return avg;
}
```

### **3. EuRegistrationBusinessService**

#### **3.1. Operaciones de Registro UE**

**Método: `registerInEuDatabase(Long projectId, String section, RegistrationData data, String registeredBy)`**
```java
/**
 * Registra proyecto en Base de Datos UE según Art. 49
 *
 * Validaciones:
 * - Proyecto existe
 * - Proyecto es alto riesgo
 * - Sección válida (A, B, o C según Anexo VIII)
 *
 * Consultas BBDD:
 * SELECT * FROM prjprojects WHERE idxproject = ? AND prjishighrisk = true
 * INSERT INTO regeuregistrations (...)
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-governance-api: POST /api/v1/eu-registrations/register
 */
public EuRegistration registerInEuDatabase(Long projectId, String section, RegistrationData data, String registeredBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // Validar que proyecto sea alto riesgo
    if (project.getPrjishighrisk() == null || !project.getPrjishighrisk()) {
        throw new BusinessException("Solo proyectos de alto riesgo deben registrarse en Base de Datos UE");
    }

    EuRegistration registration = new EuRegistration();
    registration.setIdxproject(projectId);
    registration.setRegsection(section); // A, B, o C
    registration.setRegsubmissiondata(data.toJson());
    registration.setRegstatus("PENDING");
    registration.setRegcreatedat(new Timestamp(System.currentTimeMillis()));
    registration.setRegcreatedby(registeredBy);
    registration.setIduuid(UUID.randomUUID().toString());

    // TODO: Llamar a microservicio Python para registro en EU Database
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        EuRegistrationResponse response = governanceApiClient.registerInEuDatabase(
            projectId, section, data
        );
        registration.setRegregistrationid(response.getRegistrationId());
        registration.setRegstatus("REGISTERED");
        registration.setRegresponse(response.toJson());
    } catch (Exception e) {
        log.error("Error registrando en EU Database", e);
        registration.setRegstatus("ERROR");
        registration.setRegerror(e.getMessage());
    }
    */

    return noCodeClient.save(registration);
}
```

---

## 📊 CONSULTAS BBDD ESPECÍFICAS

### **1. Obtener FRIAs incompletas**
```sql
SELECT f.idxfriaassessment, p.prjname, f.friastatus,
       f.friaprocessdescription, f.friarisks, f.friafinalrisk
FROM friafundamentalrightsassessments f
JOIN prjprojects p ON f.idxproject = p.idxproject
WHERE f.friastatus = 'DRAFT'
ORDER BY f.friacreatedat DESC;
```

### **2. Obtener evaluaciones de conformidad pendientes**
```sql
SELECT ca.idxcomplianceassessment, p.prjname, ca.comassessmenttype,
       ca.comstep2qmsscore, ca.comstep3docscore, ca.comoverallscore
FROM comcomplianceassessments ca
JOIN prjprojects p ON ca.idxproject = p.idxproject
WHERE ca.comreadyforcertification = false
ORDER BY ca.comassessmentdate DESC;
```

### **3. Obtener registros UE pendientes**
```sql
SELECT er.idxregregistration, p.prjname, er.regsection, er.regstatus,
       er.regcreatedat, er.regregistrationid
FROM regeuregistrations er
JOIN prjprojects p ON er.idxproject = p.idxproject
WHERE er.regstatus IN ('PENDING', 'ERROR')
ORDER BY er.regcreatedat DESC;
```

### **4. Obtener FRIAs con riesgo alto**
```sql
SELECT f.idxfriaassessment, p.prjname, f.friafinalrisk,
       f.friaauthoritynotified, f.friaauthoritynotifiedat
FROM friafundamentalrightsassessments f
JOIN prjprojects p ON f.idxproject = p.idxproject
WHERE f.friafinalrisk >= 0.75
ORDER BY f.friafinalrisk DESC;
```

### **5. Obtener evaluaciones listas para certificación**
```sql
SELECT ca.idxcomplianceassessment, p.prjname, ca.comoverallscore,
       ca.comreadyforcertification, ca.comcertificateid
FROM comcomplianceassessments ca
JOIN prjprojects p ON ca.idxproject = p.idxproject
WHERE ca.comreadyforcertification = true
  AND ca.comcertificateid IS NULL
ORDER BY ca.comoverallscore DESC;
```

---

## 🔗 INTEGRACIÓN CON MICROSERVICIOS PYTHON

> **⚠️ NOTA IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** y pendientes de implementación.
> Se deben implementar cuando los microservicios estén disponibles y operativos.

### **1. leka-fria-generator**

**Endpoint: `POST /api/fria/cross-validate`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public FriaCrossValidationResult crossValidateFria(Long friaId) {
    String url = friaGeneratorBaseUrl + "/api/fria/cross-validate";
    Map<String, Object> payload = Map.of("friaId", friaId);
    return restTemplate.postForObject(url, payload, FriaCrossValidationResult.class);
}
*/
```

### **2. codeflowx-governance-api**

**Endpoint: `POST /api/v1/fria/{friaId}/notify-authority`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public void notifyAuthority(Long friaId, String friaJson) {
    String url = governanceApiBaseUrl + "/api/v1/fria/" + friaId + "/notify-authority";
    restTemplate.postForObject(url, friaJson, Void.class);
}
*/
```

**Endpoint: `POST /api/v1/eu-registrations/register`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public EuRegistrationResponse registerInEuDatabase(Long projectId, String section, RegistrationData data) {
    String url = governanceApiBaseUrl + "/api/v1/eu-registrations/register";
    Map<String, Object> payload = Map.of(
        "projectId", projectId,
        "section", section,
        "data", data
    );
    return restTemplate.postForObject(url, payload, EuRegistrationResponse.class);
}
*/
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Fase 1: Business Services Base**
- [ ] Crear `FriaAssessmentBusinessService.java` (si no existe)
- [ ] Crear `ComplianceAssessmentBusinessService.java` (si no existe)
- [ ] Crear `EuRegistrationBusinessService.java`
- [ ] Implementar operaciones CRUD básicas
- [ ] Implementar validaciones de auditoría (INC-007, INC-008)

### **Fase 2: Operaciones de Evaluación**
- [ ] Implementar creación y completado de FRIAs
- [ ] Implementar cálculo de riesgo final (Anexo IX)
- [ ] Implementar ejecución de evaluación de conformidad
- [ ] Implementar cálculo de scores (QMS, Documentación, Overall)

### **Fase 3: Operaciones de Registro**
- [ ] Implementar registro en Base de Datos UE
- [ ] Implementar notificación a autoridades
- [ ] Implementar gestión de certificados

### **Fase 4: Integración con Microservicios (PENDIENTE)**
- [ ] ⚠️ **PENDIENTE:** Configurar clientes REST para microservicios Python
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `leka-fria-generator`
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-governance-api`
- [ ] **NOTA:** Todas las llamadas a microservicios están comentadas en el código

### **Fase 5: Consultas BBDD**
- [ ] Implementar consultas específicas de validación
- [ ] Implementar consultas de FRIAs y evaluaciones
- [ ] Implementar consultas de registros UE

### **Fase 6: Testing y Validación**
- [ ] Crear tests unitarios para cada método
- [ ] Validar integración con microservicios
- [ ] Validar consultas BBDD
- [ ] Validar cumplimiento de auditoría

---

## 📝 NOTAS IMPORTANTES

1. **Arquitectura EnArt:** Usar `NoCodeClient` (no Repository) para acceso a datos
2. **Validaciones Críticas:** Implementar todas las validaciones de auditoría antes de permitir operaciones
3. **Microservicios Python:** ⚠️ **TODAS LAS LLAMADAS ESTÁN COMENTADAS** - Pendientes de implementar cuando microservicios estén disponibles
4. **Logging:** Registrar todas las operaciones críticas para auditoría
5. **Transacciones:** Usar `@Transactional` para operaciones que modifican múltiples entidades
6. **Entidades JPA:** Revisar entidades en `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/` para campos disponibles
7. **Fórmulas:** El cálculo de riesgo debe seguir exactamente la fórmula del Anexo IX
8. **Notificaciones:** Las notificaciones a autoridades deben ser auditables y trazables

---

**Última actualización:** Diciembre 2025
**Estado:** Pendiente de implementación
