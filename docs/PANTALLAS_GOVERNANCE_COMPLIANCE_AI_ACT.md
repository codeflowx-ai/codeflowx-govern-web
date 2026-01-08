# 🏛️ PANTALLAS DE GOBIERNO DE IA, IA OS Y CUMPLIMIENTO NORMATIVO

**Fecha:** Noviembre 2025
**Versión:** 1.0
**Objetivo:** Mapear pantallas esenciales de gobierno de IA, IA OS y cumplimiento normativo con artículos específicos del EU AI Act
**Alcance:** Desde clasificación hasta monitorización post-mercado

---

## 📊 RESUMEN EJECUTIVO

| Categoría | Pantallas Esenciales | Artículos EU AI Act | Estado Migración |
|-----------|---------------------|---------------------|-------------------|
| **Clasificación** | 2 | Art. 5, 6, Anexo III | ✅ 100% |
| **FRIA** | 3 | Art. 27, Anexo IX | ✅ 100% |
| **Conformidad** | 4 | Art. 43, Anexo VI | ✅ 100% |
| **Registro UE** | 2 | Art. 49, Anexo VIII | ✅ 100% |
| **Post-Market Monitoring** | 3 | Art. 20, 72 | ✅ 100% |
| **Logs Inmutables** | 2 | Art. 12, 19 | ✅ 100% |
| **QMS** | 2 | Art. 17 | ✅ 100% |
| **Documentación Técnica** | 2 | Art. 11, Anexo IV | ✅ 100% |
| **Supervisión Humana** | 2 | Art. 14 | ✅ 100% |
| **Trazabilidad** | 2 | Art. 12, 19 | ✅ 100% |
| **Sistemas Prohibidos** | 1 | Art. 5 | ✅ 100% |
| **TOTAL** | **25** | **13 artículos + 4 anexos** | **✅ 100%** |

---

## 🎯 MAPEO POR ARTÍCULO EU AI ACT

### 1. ARTÍCULO 5 - SISTEMAS PROHIBIDOS

#### **Pantalla Esencial:**

| Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---------|-----------------|-------------|----------|-------|
| ⏳ `app/(app)/governance/compliance/prohibited-systems/page.tsx` | `console/gobierno/compliance/prohibited-systems.zul` | Verificación de sistemas prohibidos | **Art. 5** | - |

**Funcionalidades:**
- Verificación automática contra catálogo de sistemas prohibidos
- Listado de sistemas prohibidos activos
- Alertas cuando se detecta sistema prohibido
- Bloqueo automático de despliegue

**Entidad JPA:** `ProhibitedSystem`
**Business Service:** `ProhibitedSystemBusinessService.checkProhibitedSystem()`

---

### 2. ARTÍCULO 6 + ANEXO III - CLASIFICACIÓN DE SISTEMAS DE ALTO RIESGO

#### **Pantallas Esenciales (2):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ⏳ `app/(app)/governance/compliance/classification/page.tsx` | `console/gobierno/compliance/high-risk-classifier.zul` | Clasificador de alto riesgo | **Art. 6.2** | **Anexo III** |
| 2 | ⏳ `app/(app)/governance/compliance/annex-iii-categories/page.tsx` | `console/gobierno/compliance/annex-iii-categories.zul` | Catálogo categorías Anexo III | **Art. 6.2** | **Anexo III** |

**Funcionalidades:**
- Clasificación automática basada en keywords y catálogo
- 8 categorías principales Anexo III:
  1. Biometric identification and categorisation
  2. Management and operation of critical infrastructure
  3. Education and vocational training
  4. Employment, workers management and access to self-employment
  5. Access to and enjoyment of essential private services and public services and benefits
  6. Law enforcement
  7. Migration, asylum and border control management
  8. Administration of justice and democratic processes
- 25+ subcategorías
- Justificación obligatoria de clasificación
- Generación de reporte PDF

**Entidad JPA:** `AnnexIIICategory`, `Project` (campo `riskClassification`)
**Business Service:** `HighRiskClassifierBusinessService.classifySystem()`

---

### 3. ARTÍCULO 27 + ANEXO IX - EVALUACIÓN DE IMPACTO EN DERECHOS FUNDAMENTALES (FRIA)

#### **Pantallas Esenciales (3):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ⏳ `app/(app)/governance/compliance/fria/page.tsx` | `console/gobierno/compliance/fria-wizard.zul` | Wizard FRIA (6 pasos) | **Art. 27.1** | **Anexo IX** |
| 2 | ⏳ `app/(app)/governance/compliance/fria/assessments/page.tsx` | `console/gobierno/compliance/fria-assessments.zul` | Listado de FRIAs | **Art. 27** | **Anexo IX** |
| 3 | ⏳ `app/(app)/governance/compliance/fria/[id]/page.tsx` | `console/gobierno/compliance/fria-detail.zul` | Detalle de FRIA | **Art. 27** | **Anexo IX** |

**Funcionalidades:**

**Wizard FRIA (6 pasos según Art. 27.1.a-f):**
1. **Step 1:** Descripción de procesos (Art. 27.1.a)
2. **Step 2:** Período y frecuencia de uso (Art. 27.1.b)
3. **Step 3:** Categorías de personas afectadas + grupos vulnerables (Art. 27.1.c)
4. **Step 4:** Riesgos específicos identificados (Art. 27.1.d)
5. **Step 5:** Supervisión humana (HITL) (Art. 27.1.e)
6. **Step 6:** Medidas de mitigación (Art. 27.1.f)

**Características adicionales:**
- Cálculo de riesgo final según Anexo IX
- Notificación a autoridades (Art. 27.3)
- Integración con DPIA (Art. 27.4)
- Validación cruzada con métricas técnicas
- Generación de documento PDF

**Entidad JPA:** `FriaAssessment`, `FriaRisk`, `MitigationMeasure`
**Business Service:** `FriaAssessmentBusinessService.createFria()`, `calculateFinalRisk()`

---

### 4. ARTÍCULO 43 + ANEXO VI - EVALUACIÓN DE CONFORMIDAD

#### **Pantallas Esenciales (4):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ✅ `app/(app)/governance/compliance/dashboard/page.tsx` | `console/platform/governance/compliance/assessment.zul` | Dashboard de evaluaciones | **Art. 43** | **Anexo VI** |
| 2 | ✅ `app/(app)/governance/compliance/conformity-review/page.tsx` | `console/bpmn/compliance-review-form.zul` | Revisión de conformidad | **Art. 43** | **Anexo VI** |
| 3 | ✅ `app/(app)/governance/compliance/conformity-declaration-manager/page.tsx` | `console/gobierno/compliance/conformity-declaration-manager.zul` | Gestión declaraciones | **Art. 48** | **Anexo V** |
| 4 | ⏳ `app/(app)/governance/compliance/assessments/[id]/page.tsx` | `console/platform/governance/compliance/assessment-detail.zul` | Detalle de evaluación | **Art. 43** | **Anexo VI** |

**Funcionalidades:**

**Evaluación de Conformidad (4 steps según Anexo VI):**
1. **Step 1:** Verificación inicial
2. **Step 2:** QMS Check (Art. 17)
3. **Step 3:** Documentación técnica (Art. 11, Anexo IV)
4. **Step 4:** Verificación de consistencia

**Scores por step (0.00 - 1.00):**
- `step1Score`, `step2QmsScore`, `step3DocScore`, `step4ConsistencyScore`
- `overallScore` = promedio ponderado
- `readyForCertification` = (overallScore >= 0.80)

**Entidad JPA:** `ComplianceAssessment`
**Business Service:** `ComplianceAssessmentBusinessService.createAssessment()`, `executeStep2QmsCheck()`, `executeStep3DocReview()`, `calculateOverallScore()`

---

### 5. ARTÍCULO 49 + ANEXO VIII - REGISTRO EN BASE DE DATOS UE

#### **Pantallas Esenciales (2):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ⏳ `app/(app)/governance/compliance/eu-registration/page.tsx` | `console/gobierno/compliance/eu-registration-form.zul` | Formulario registro UE | **Art. 49** | **Anexo VIII** |
| 2 | ⏳ `app/(app)/governance/compliance/eu-registration/status/page.tsx` | `console/gobierno/compliance/eu-registration-status.zul` | Estado de registros | **Art. 49.4, 49.5** | **Anexo VIII** |

**Funcionalidades:**

**Formulario Registro UE (3 secciones según Anexo VIII):**
- **Sección A:** Información del proveedor
- **Sección B:** Información del sistema IA
- **Sección C:** Información de conformidad

**Estados:**
- `DRAFT` - Borrador
- `PENDING` - Pendiente de envío
- `SUBMITTED` - Enviado a BD UE
- `REGISTERED` - Registrado exitosamente
- `REJECTED` - Rechazado

**Características especiales:**
- Registro no público para sistemas sensibles (Art. 49.4)
- Registro nacional para infraestructuras críticas (Art. 49.5)
- Generación automática de payload JSON para API UE

**Entidad JPA:** `EuRegistration`
**Business Service:** `EuRegistrationBusinessService.submitToEuDatabase()`

---

### 6. ARTÍCULO 20 + ARTÍCULO 72 - POST-MARKET MONITORING

#### **Pantallas Esenciales (3):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ✅ `app/(app)/governance/compliance/post-market-monitoring/page.tsx` | `console/gobierno/compliance/post-market-monitoring-dashboard.zul` | Dashboard PMM | **Art. 20, 72** | - |
| 2 | ⏳ `app/(app)/governance/compliance/incidents/page.tsx` | `console/gobierno/compliance/incident-reporting.zul` | Reporte de incidentes | **Art. 20.1** | - |
| 3 | ⏳ `app/(app)/governance/compliance/corrective-actions/page.tsx` | `console/gobierno/compliance/corrective-actions.zul` | Acciones correctoras | **Art. 20.2** | - |

**Funcionalidades:**

**Post-Market Monitoring:**
- Monitoreo continuo de sistemas en producción
- Métricas de rendimiento y calidad
- Detección de drift y degradación
- Alertas automáticas de anomalías

**Gestión de Incidentes:**
- Clasificación de severidad (LOW, MEDIUM, HIGH, CRITICAL)
- Notificación a autoridades de vigilancia del mercado (Art. 20.1)
- Notificación a usuarios afectados
- Análisis de causa raíz (RCA)

**Acciones Correctoras:**
- Planificación de acciones correctoras
- Seguimiento de efectividad
- Cierre de incidentes
- Escalación si es necesario

**Entidad JPA:** `PostMarketMonitoring`, `Incident`, `CorrectiveAction`
**Business Service:** `PostMarketMonitoringService.monitorSystem()`, `IncidentService.reportIncident()`

---

### 7. ARTÍCULO 12 + ARTÍCULO 19 - LOGS INMUTABLES Y REGISTROS

#### **Pantallas Esenciales (2):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ⏳ `app/(app)/governance/compliance/immutable-logs/page.tsx` | `console/gobierno/compliance/log-search-advanced.zul` | Búsqueda de logs inmutables | **Art. 19** | - |
| 2 | ⏳ `app/(app)/governance/compliance/traceability-evidence/page.tsx` | `console/gobierno/compliance/traceability-evidence.zul` | Trazabilidad y evidencias | **Art. 12** | - |

**Funcionalidades:**

**Logs Inmutables (Art. 19):**
- Hash chain SHA-256 para integridad
- Trigger PostgreSQL APPEND-ONLY
- Búsqueda avanzada por:
  - Tipo de operación
  - Entidad afectada
  - Usuario
  - Rango de fechas
  - Hash de auditoría

**Trazabilidad (Art. 12):**
- Registro automático de eventos críticos
- Evidencias de cumplimiento
- Exportación de logs para auditoría
- Verificación de integridad de hash chain

**Entidad JPA:** `ImmutableLog`
**Business Service:** `ImmutableLoggingBusinessService.logEvent()`, `verifyIntegrity()`

---

### 8. ARTÍCULO 17 - SISTEMA DE GESTIÓN DE CALIDAD (QMS)

#### **Pantallas Esenciales (2):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ✅ `app/(app)/governance/compliance/conformity-review/page.tsx` | `console/bpmn/review-qms-gaps-form.zul` | Revisión gaps QMS | **Art. 17** | - |
| 2 | ⏳ `app/(app)/governance/compliance/qms/page.tsx` | `console/gobierno/governance/qms-compliance.zul` | Dashboard QMS | **Art. 17** | - |

**Funcionalidades:**

**QMS Compliance:**
- 13 módulos QMS según Art. 17:
  1. Estrategia de compliance
  2. Gestión de riesgos
  3. Gestión de datos
  4. Diseño y desarrollo
  5. Validación y testing
  6. Documentación técnica
  7. Supervisión humana
  8. Acciones correctoras
  9. Post-market monitoring
  10. Gestión de cambios
  11. Gestión de proveedores
  12. Auditoría interna
  13. Gestión de no conformidades

**Cálculo de Score QMS:**
- Score por módulo (0.00 - 1.00)
- Score overall QMS
- Detección de gaps
- Plan de mejora continua

**Entidad JPA:** `QualityManagementSystem`
**Business Service:** `QualityManagementSystemBusinessService.calculateQmsComplianceScore()`

---

### 9. ARTÍCULO 11 + ANEXO IV - DOCUMENTACIÓN TÉCNICA

#### **Pantallas Esenciales (2):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ✅ `app/(app)/governance/compliance/conformity-review/page.tsx` | `console/bpmn/complete-documentation-form.zul` | Completar documentación | **Art. 11** | **Anexo IV** |
| 2 | ⏳ `app/(app)/governance/compliance/technical-docs/page.tsx` | `console/gobierno/compliance/technical-documentation.zul` | Gestión documentación técnica | **Art. 11** | **Anexo IV** |

**Funcionalidades:**

**Documentación Técnica (11 secciones según Anexo IV):**
1. System Description
2. Intended Purpose
3. Development Process
4. Data Governance
5. Validation Procedures
6. Testing Procedures
7. Monitoring Measures
8. Human Oversight
9. Risk Management
10. Accuracy and Robustness
11. Cybersecurity Measures

**Características:**
- Generación automática desde datos del sistema
- Validación de completitud
- Generación de PDF
- Versionado de documentación

**Entidad JPA:** `TechnicalDocumentation` (tabla `GOV_AIACT_TECHNICAL_DOCS`)
**Business Service:** `TechnicalDocumentationService.generateDocumentation()`

---

### 10. ARTÍCULO 14 - SUPERVISIÓN HUMANA (HITL)

#### **Pantallas Esenciales (2):**

| # | Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---|---------|-----------------|-------------|----------|-------|
| 1 | ⏳ `app/(app)/governance/compliance/hitl-supervision/page.tsx` | `console/gobierno/governance/hitl-supervision.zul` | Supervisión humana | **Art. 14** | - |
| 2 | ✅ `app/(app)/bpmn/forms/hitl-sla-reminder/page.tsx` | `console/bpmn/hitl-sla-reminder-form.zul` | Recordatorio SLA HITL | **Art. 14** | - |

**Funcionalidades:**

**Supervisión Humana:**
- Configuración de puntos de supervisión
- Capacidades HITL:
  - Pre-deployment review
  - In-loop supervision
  - Post-deployment review
  - Override capabilities
- SLA de respuesta humana
- Registro de decisiones humanas

**Entidad JPA:** `HitlSupervision`, `HitlDecision`
**Business Service:** `HitlSupervisionService.configureSupervision()`

---

### 11. ARTÍCULO 48 + ANEXO V - DECLARACIÓN DE CONFORMIDAD

#### **Pantalla Esencial:**

| Next.js | ZUL Equivalente | Descripción | Artículo | Anexo |
|---------|-----------------|-------------|----------|-------|
| ✅ `app/(app)/governance/compliance/conformity-declaration-manager/page.tsx` | `console/gobierno/compliance/conformity-declaration-manager.zul` | Gestión declaraciones de conformidad | **Art. 48** | **Anexo V** |

**Funcionalidades:**

**Declaración de Conformidad:**
- Información del proveedor
- Información del sistema IA
- Base de conformidad (conformity basis)
- Estándares aplicados
- Cumplimiento por artículo (Art. 9-15):
  - Art. 9 - Risk Management
  - Art. 10 - Data Governance
  - Art. 11 - Technical Documentation
  - Art. 12 - Record-Keeping
  - Art. 13 - Transparency
  - Art. 14 - Human Oversight
  - Art. 15 - Accuracy, Robustness, Cybersecurity
- Firma digital
- Generación de PDF

**Entidad JPA:** `ConformityDeclaration` (tabla `GOV_CONFORMITY_DECLARATIONS`)
**Business Service:** `ConformityDeclarationService.generateDeclaration()`

---

## 📋 FLUJO COMPLETO DE CUMPLIMIENTO

### **Fase 1: Clasificación (Art. 5, 6, Anexo III)**
1. Verificar sistema prohibido (Art. 5)
2. Clasificar sistema de alto riesgo (Art. 6, Anexo III)
3. Seleccionar categorías Anexo III

### **Fase 2: Evaluación FRIA (Art. 27, Anexo IX)**
4. Completar wizard FRIA (6 pasos)
5. Calcular riesgo final
6. Notificar autoridades si es necesario (Art. 27.3)

### **Fase 3: Evaluación de Conformidad (Art. 43, Anexo VI)**
7. Iniciar evaluación de conformidad
8. Ejecutar Step 2: QMS Check (Art. 17)
9. Ejecutar Step 3: Documentación técnica (Art. 11, Anexo IV)
10. Ejecutar Step 4: Verificación de consistencia
11. Calcular score overall

### **Fase 4: Registro UE (Art. 49, Anexo VIII)**
12. Completar formulario registro UE (3 secciones)
13. Enviar a Base de Datos UE
14. Monitorear estado de registro

### **Fase 5: Post-Market Monitoring (Art. 20, 72)**
15. Monitoreo continuo de sistemas en producción
16. Reporte de incidentes
17. Gestión de acciones correctoras

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Entidades EU AI Act:** `nocode.service/nocode.service.entitys/EU_AI_ACT_ENTITIES_README.md`
- **Verificación Artículos:** `suinsit.nova.web/docs/compliance/ARTICULOS_PENDIENTES_VERIFICACION.md`
- **Gaps Detectados:** `suinsit.nova.web/docs/compliance/GAPS_DETECTED_AI_ACT_VERIFICATION.md`

---

**Última actualización:** Noviembre 2025
**Estado:** ✅ Documento completo - 25 pantallas mapeadas con 13 artículos + 4 anexos del EU AI Act
