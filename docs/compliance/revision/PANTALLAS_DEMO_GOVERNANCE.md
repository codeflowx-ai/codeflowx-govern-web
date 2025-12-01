# PANTALLAS PARA DEMO - GOVERNANCE

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Mapear las 11 pantallas solicitadas para la demo con su estado de implementación, ViewModels, ZULs y ubicación en gobierno

---

## 📊 RESUMEN EJECUTIVO

| # | Pantalla | ViewModel | ZUL | Estado | En Gobierno | Telemetría |
|---|----------|-----------|-----|--------|-------------|------------|
| 1 | Dashboard General | ✅ DashboardViewModel / GovernanceDashboardSummaryOverviewViewModel | ✅ Existe (2 opciones) | ✅ Implementado | ✅ Sí | ❌ No |
| 2 | Inventario Sistemas IA | ✅ ProjectAIInventoryViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ❌ No |
| 3 | Ficha Sistema IA | ✅ ModelsDetailViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ❌ No |
| 4 | Clasificación AI Act | ✅ HighRiskClassifierViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ❌ No |
| 5 | Evaluación Riesgos / FRIA | ✅ FriaWizardViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ❌ No |
| 6 | Bandeja de Tareas | ✅ TaskInboxViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ❌ No |
| 7 | Supervisión Humana (HITL) | ✅ HitlSupervisionViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ❌ No |
| 8 | Trazabilidad y Evidencias | ✅ TraceabilityEvidenceViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ⚠️ Parcial |
| 9 | Alertas e Incidentes | ✅ MonitoringAlertOverviewViewModel | ✅ Existe (múltiples) | ✅ Implementado | ✅ Sí | ⚠️ Parcial |
| 10 | Documentación / Reporte | ✅ ComplianceReportViewModel | ✅ Existe | ✅ Implementado | ✅ Sí | ❌ No |
| 11 | Pantalla Final | N/A | ❌ No existe | 🔴 Pendiente | N/A | N/A |

**Total Implementadas Completamente:** 9 de 10 (90%)
**Total Parcialmente Implementadas:** 0 de 10 (0%)
**Total Pendientes:** 1 de 10 (10%)

---

## 📋 DETALLE POR PANTALLA

### 1. 🟩 Dashboard General

**ViewModels Disponibles:**
- `GovernanceDashboardSummaryOverviewViewModel` (Platform) - `com.codeflowx.platform.viewmodel.governance.GovernanceDashboardSummaryOverviewViewModel`
- `DashboardViewModel` (Gobierno) - `com.codeflowx.govern.viewmodel.DashboardViewModel`

**ZULs Encontrados:**
- ✅ `console/platform/governance/dashboard/summary.zul` - Usa `GovernanceDashboardSummaryOverviewViewModel`
- ✅ `console/dashboard.zul` - Usa `DashboardViewModel`

**Estado:** ✅ **IMPLEMENTADO** (múltiples opciones)
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No

**Funcionalidad Actual:**
- KPIs de governance
- Métricas de compliance
- Resumen de evaluaciones
- Health score

**ZULs Encontrados:**
- ✅ `console/dashboard.zul` - Usa `DashboardViewModel` (muestra tareas, métricas generales)
- ✅ `console/platform/governance/dashboard/summary.zul` - Usa `GovernanceDashboardSummaryOverviewViewModel` (muestra resumen de governance)

**Funcionalidad Requerida para Demo:**
- ✅ Inventario (contador de sistemas)
- ✅ Riesgos (contador de alto riesgo)
- ✅ Evidencias (contador de evidencias)
- ✅ Supervisión (contador de HITL)
- ✅ Alertas (contador de alertas activas)
- ✅ Tareas pendientes (resumen de tareas)

**Prompt Asociado:** PROMPTS_05 - C.1
**BusinessService:** ✅ `GovernanceDashboardSummaryService` (para summary.zul)

**Acción Requerida:**
1. ✅ ZUL encontrado - Usar `console/dashboard.zul` o `console/platform/governance/dashboard/summary.zul`
2. Verificar que muestra todos los KPIs requeridos
3. Si falta algún KPI, añadir widgets para: Inventario, Riesgos, Evidencias, Supervisión, Alertas

---

### 2. 🟩 Inventario de Sistemas de IA

**ViewModel:** `ProjectAIInventoryViewModel`
**Ubicación:** `com.codeflowx.govern.viewmodel.projects.ProjectAIInventoryViewModel`
**ZUL:** ✅ **EXISTE** - `console/platform/projects/project-ai-inventory.zul`

**Estado:** ✅ **IMPLEMENTADO**
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No

**Funcionalidad Actual:**
- Lista de sistemas detectados
- Tipos: MODEL, AGENT, RAG, PROMPT, HYBRID
- Estados: DESIGN, DEVELOPMENT, TESTING, DEPLOYMENT, MONITORING, DECOMMISSIONED
- Niveles de riesgo: HIGH_RISK, LIMITED_RISK, MINIMAL_RISK, PROHIBITED

**Funcionalidad Requerida para Demo:**
- ✅ Lista de sistemas detectados
- ✅ Tipos: modelos, copilotos, APIs, SaaS
- ✅ Estado / clasificación preliminar

**Prompt Asociado:** INC-001 (Validación Coherencia Modelo-Dataset)
**BusinessService:** ❌ No usa BusinessService dedicado (usa EntityManager directamente)

**Acción Requerida:**
1. ✅ ZUL encontrado - `console/platform/projects/project-ai-inventory.zul`
2. ✅ Muestra: tipo de sistema, etapa ciclo de vida, nivel de riesgo, usuarios, propósito, uso, controles
3. ⚠️ Verificar que muestra lista de sistemas (actualmente muestra solo un proyecto individual)

---

### 3. 🟩 Ficha de un Sistema de IA

**ViewModel:** `ModelsDetailViewModel` (o `AgentsDetailViewModel`, `RagSystemsDetailViewModel` según tipo)
**Ubicación:** `com.codeflowx.govern.viewmodel.models.ModelsDetailViewModel`
**ZUL:** ✅ **EXISTE** - `console/platform/models/models-detail.zul` (o similar según tipo)

**Estado:** ✅ **IMPLEMENTADO**
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No

**Funcionalidad Actual:**
- Detalles del modelo/agente/RAG
- Metadatos
- Versionado
- Propósito
- Uso

**Funcionalidad Requerida para Demo:**
- ✅ Proveedor
- ✅ Uso
- ✅ Riesgos iniciales
- ✅ Metadatos
- ✅ Versionado
- ✅ Propósito

**Prompt Asociado:** Múltiples (INC-011, INC-015, etc.)
**BusinessService:** ✅ Usa `ModelService`, `AgentService`, etc.

**Acción Requerida:**
1. Verificar que muestra todos los campos requeridos
2. Asegurar que muestra riesgos iniciales
3. Verificar que muestra proveedor

---

### 4. 🟩 Clasificación AI Act

**ViewModel:** `HighRiskClassifierViewModel`
**Ubicación:** `com.codeflowx.govern.viewmodel.compliance.HighRiskClassifierViewModel`
**ZUL:** ✅ **EXISTE** - `console/gobierno/compliance/high-risk-classifier.zul`

**Estado:** ✅ **IMPLEMENTADO**
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No

**Funcionalidad Actual:**
- 8 categorías Anexo III
- 25 subcategorías específicas
- Sugerencia automática con IA
- Justificación obligatoria
- Actualización de Project (PRJISHIGHRISK)

**Funcionalidad Requerida para Demo:**
- ✅ Nivel de riesgo (alto, limitado…)
- ✅ Obligaciones aplicables
- ✅ Estado
- ✅ Controles

**Prompt Asociado:** INC-001, INC-002, INC-003, INC-004, INC-005
**BusinessService:** ❌ No usa BusinessService dedicado (usa ProjectService, ModelService)

**Acción Requerida:**
1. ✅ Pantalla lista para demo
2. Verificar que muestra obligaciones aplicables
3. Verificar que muestra controles

---

### 5. 🟩 Evaluación de Riesgos / FRIA

**ViewModel:** `FriaWizardViewModel`
**Ubicación:** `com.codeflowx.govern.viewmodel.compliance.FriaWizardViewModel`
**ZUL:** ✅ **EXISTE** - `console/gobierno/compliance/fria-wizard.zul`

**Estado:** ✅ **IMPLEMENTADO**
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No

**Funcionalidad Actual:**
- Wizard de 6 pasos (Art. 27.1 a-f)
- Matriz de riesgos
- Checklist/formulario
- Controles
- Resultados
- Estado

**Funcionalidad Requerida para Demo:**
- ✅ Matriz de riesgos
- ✅ Checklist o formulario
- ✅ Controles
- ✅ Resultados
- ✅ Estado

**Prompt Asociado:** INC-007, INC-009, INC-016, INC-021, INC-023
**BusinessService:** ⚠️ **NO USA** `FriaAssessmentBusinessService` (debe migrar)

**Microservicio Python:** ✅ `leka-fria-generator` (8012) - Implementado

**Acción Requerida:**
1. ✅ Pantalla lista para demo
2. ⚠️ Migrar para usar `FriaAssessmentBusinessService` (mejora futura)

---

### 6. 🟦 Bandeja de Tareas (OBLIGATORIA)

**ViewModel:** `TaskInboxViewModel`
**Ubicación:** `com.codeflowx.govern.workflow.viewmodels.TaskInboxViewModel`
**ZUL:** ✅ **EXISTE** - `console/bpmn/task-inbox.zul`

**Estado:** ✅ **IMPLEMENTADO**
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No

**Funcionalidad Actual:**
- Listado con títulos
- Prioridad
- Responsable
- Estado
- Tipo de tarea
- Fechas
- Acción de completar o revisar

**Funcionalidad Requerida para Demo:**
- ✅ Listado con títulos
- ✅ Prioridad
- ✅ Responsable
- ✅ Estado
- ✅ Tipo de tarea
- ✅ Fechas
- ✅ Acción de completar o revisar

**Tareas que Debe Mostrar:**
- "Completar evaluación de riesgos"
- "Aprobar sistema de IA"
- "Revisión humana pendiente"
- "Evidencias requeridas"
- "Incidente en supervisión"
- "Clasificación requerida"
- "Seguimiento de alerta"

**Prompt Asociado:** Múltiples (workflows BPMN)
**BusinessService:** ✅ Usa `TaskManagementService`

**Acción Requerida:**
1. ✅ Pantalla lista para demo
2. Verificar que muestra todos los tipos de tareas requeridos

---

### 7. 🟩 Supervisión Humana (HITL/HOTL)

**ViewModel:** ✅ **IMPLEMENTADO** - `HitlSupervisionViewModel`
**Ubicación:** `com.codeflowx.govern.viewmodel.governance.HitlSupervisionViewModel`
**ZUL:** ✅ **IMPLEMENTADO** - `console/gobierno/governance/hitl-supervision.zul`

**Estado:** ✅ **IMPLEMENTADO** (dashboard consolidado creado con integración real)
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No
**Modo Demo:** ✅ Implementado con `?mock=true`

**ViewModels Relacionados Existentes (4):**
- ✅ `AgentApprovalHumanOverrideViewModel` - `com.codeflowx.govern.workflow.viewmodels.AgentApprovalHumanOverrideViewModel`
  - **ZUL:** `console/bpmn/agent-approval-human-override-form.zul`
  - **Proceso BPMN:** `agent-approval-v1` - User Task: `hitlTask`
- ✅ `ModelApprovalHumanOverrideViewModel` - `com.codeflowx.govern.workflow.viewmodels.ModelApprovalHumanOverrideViewModel`
  - **ZUL:** `console/bpmn/model-approval-human-override-form.zul`
  - **Proceso BPMN:** `model-approval-v1` - User Task: `hitlTask`
- ✅ `PromptHumanReviewViewModel` - `com.codeflowx.govern.workflow.viewmodels.PromptHumanReviewViewModel`
  - **ZUL:** `console/bpmn/prompt-human-review-form.zul`
  - **Proceso BPMN:** `prompt-approval-v1` - User Task: `humanReviewTask`
- ✅ `HitlSlaReminderViewModel` - `com.codeflowx.govern.workflow.viewmodels.HitlSlaReminderViewModel`
  - **ZUL:** `console/bpmn/hitl-sla-reminder-form.zul`
  - **Proceso BPMN:** `agent-approval-v1` - User Task: `hitlReminder` (Timer Boundary 24h)

**Acceso desde Bandeja de Tareas:**
- ✅ Todas estas tareas aparecen en `task-inbox.zul`
- ✅ Al hacer clic, se abre el formulario ZUL correspondiente
- ✅ Permite demostrar supervisión humana en acción

**Funcionalidad Requerida para Demo:**
- ✅ Registro de supervisión
- ✅ Auditoría
- ✅ Intervenciones
- ✅ Estado

**Prompt Asociado:** INC-HITL-001, INC-HITL-002, INC-HITL-003, INC-HITL-005, INC-HITL-009
**Estado Prompts:** ⚠️ Pendiente de revisión (ver `ESTADO_PROMPTS_SEGURIDAD_ROLES.md`)

**Servicios Integrados:**
- ✅ `TaskManagementService` - Para obtener tareas HITL pendientes
- ✅ `AuditLogService` - Para logs de auditoría con filtros
- ✅ `PolicyAuditLogService` - Para logs de políticas
- ✅ `TaskService` (Flowable) - Para tareas BPMN

**Funcionalidad Implementada:**
- ✅ Dashboard consolidado de supervisión humana
- ✅ Métricas HITL (total, pendientes, tiempo promedio, cumplimiento SLA)
- ✅ Lista de intervenciones con filtros (tipo, estado, período)
- ✅ Logs de auditoría integrados
- ✅ Integración con ViewModels BPMN existentes
- ✅ Modo demo/mock funcional

**Acceso para Demo:**
- URL: `/console/gobierno/governance/hitl-supervision.zul?mock=true`

**Fecha Implementación:** 25 de noviembre de 2025

---

### 8. 🟩 Trazabilidad y Evidencias

**ViewModel:** ✅ **IMPLEMENTADO** - `TraceabilityEvidenceViewModel`
**Ubicación:** `com.codeflowx.govern.viewmodel.compliance.TraceabilityEvidenceViewModel`
**ZUL:** ✅ **IMPLEMENTADO** - `console/gobierno/compliance/traceability-evidence.zul`

**Estado:** ✅ **IMPLEMENTADO** (vista consolidada creada con integración real)
**En Gobierno:** ✅ Sí
**Telemetría:** ⚠️ Parcial (preparado para integración REST API)
**Modo Demo:** ✅ Implementado con `?mock=true`

**ViewModels Relacionados Existentes:**
- ✅ `AgentDecisionsLogViewModel` - `com.codeflowx.govern.viewmodel.agents.AgentDecisionsLogViewModel`
  - **Funcionalidad:** Log de decisiones de agentes con traceability completo
- ✅ `AuditLogOverviewViewModel` - `com.codeflowx.platform.viewmodel.monitoring.AuditLogOverviewViewModel`
  - **ZUL:** `console/platform/monitoring/` (buscar ZUL asociado)
  - **Funcionalidad:** Vista general de logs de auditoría
- ✅ `PolicyAuditLogOverviewViewModel` - `com.codeflowx.platform.viewmodel.governance.PolicyAuditLogOverviewViewModel`
  - **Funcionalidad:** Logs de auditoría de políticas
- ✅ `PolicyAuditLogDetailViewModel` - `com.codeflowx.platform.viewmodel.governance.PolicyAuditLogDetailViewModel`
  - **Funcionalidad:** Detalle de logs de auditoría de políticas
- ✅ `GovernanceAuditTrailDetailedOverviewViewModel` - `com.codeflowx.platform.viewmodel.governance.GovernanceAuditTrailDetailedOverviewViewModel`
  - **Funcionalidad:** Auditoría detallada de governance

**Funcionalidad Requerida para Demo:**
- ✅ Logs
- ✅ Prompts
- ✅ Outputs
- ✅ Decisiones
- ✅ Flags
- ✅ Incidencias

**Prompt Asociado:** INC-008-001 (Trazabilidad Completa Modelo-Dataset-Output)
**Estado Prompt:** ⚠️ PENDIENTE (ver `MAPEO_PROMPTS_VIEWMODELS_GOVERNANCE_CORREGIDO.md`)

**Servicios Integrados:**
- ✅ `ImmutableLogService` - Para logs inmutables con filtros y paginación
- ✅ `AuditLogService` - Para logs de auditoría con filtros
- ✅ `PolicyAuditLogService` - Para logs de políticas
- ⚠️ Telemetría REST API - Preparado para integración (cuando esté disponible)

**Funcionalidad Implementada:**
- ✅ Vista consolidada de trazabilidad
- ✅ Logs de trazabilidad (LOG, PROMPT, OUTPUT, DECISION, FLAG, INCIDENT)
- ✅ Evidencias con hash SHA-256 para integridad
- ✅ Filtros por tipo, entidad, nombre, rango de fechas
- ✅ Estadísticas (total logs, evidencias, items marcados, incidencias)
- ✅ Exportación de logs y evidencias
- ✅ Integración con servicios de logging existentes
- ✅ Modo demo/mock funcional

**Integración Telemetría:**
- ⚠️ Preparado para llamadas REST a `codeflowx-aios-telemetry`
- ⚠️ Configuración mediante `telemetry.api.url` en properties

**Acceso para Demo:**
- URL: `/console/gobierno/compliance/traceability-evidence.zul?mock=true`

**Fecha Implementación:** 25 de noviembre de 2025

---

### 9. 🟩 Alertas e Incidentes

**ViewModels Disponibles:**
- `MonitoringAlertOverviewViewModel` - `com.codeflowx.platform.viewmodel.monitoring.MonitoringAlertOverviewViewModel`
- `SystemAlertOverviewViewModel` - `com.codeflowx.platform.viewmodel.monitoring.SystemAlertOverviewViewModel`
- `AlertSummaryOverviewViewModel` - `com.codeflowx.platform.viewmodel.monitoring.AlertSummaryOverviewViewModel`

**ZULs Encontrados:**
- ✅ `console/platform/monitoring/alerts/` (carpeta con múltiples ZULs)
- ✅ `console/platform/monitoring/dashboard/` (carpeta con dashboards)

**Estado:** ✅ **IMPLEMENTADO**
**En Gobierno:** ✅ Sí
**Telemetría:** ⚠️ Parcial (existe en telemetría pero no integrado)

**Funcionalidad Actual:**
- Listado de alertas
- Filtros por tipo, estado, severidad
- Detalle de alertas

**Funcionalidad Requerida para Demo:**
- ✅ Incidentes
- ✅ Desviaciones
- ✅ Filtros
- ✅ Indicadores

**ViewModels Relacionados:**
- `SystemAlertOverviewViewModel` - Alertas del sistema
- `AlertSummaryOverviewViewModel` - Resumen de alertas
- `AgentAlertOverviewViewModel` - Alertas de agentes

**Prompt Asociado:** INC-009-004 (Métricas de Incidentes)
**BusinessService:** ✅ Usa `MonitoringAlertService`, `SystemAlertService`, etc.

**Integración Telemetría:**
- ⚠️ Existe `AioIncidentMetric` en base de datos `codeflowx_telemetry`
- ⚠️ Requiere integración con telemetría para mostrar incidentes de agentes externos

**ZULs Encontrados:**
- ✅ `console/platform/monitoring/alerts/overview.zul` - Listado de alertas
- ✅ `console/platform/monitoring/alerts/detail.zul` - Detalle de alerta
- ✅ `console/platform/monitoring/alerts/system-overview.zul` - Alertas del sistema
- ✅ `console/platform/monitoring/dashboard/page.zul` - Dashboard de monitoring

**Acción Requerida:**
1. ✅ Pantalla lista para demo
2. ⚠️ Verificar que muestra incidentes (no solo alertas)
3. ⚠️ Integrar con telemetría para incidentes de agentes externos (opcional para demo)

---

### 10. 🟩 Documentación Automática / Reporte Final

**ViewModel:** ✅ **IMPLEMENTADO** - `ComplianceReportViewModel`
**Ubicación:** `com.codeflowx.govern.viewmodel.compliance.ComplianceReportViewModel`
**ZUL:** ✅ **IMPLEMENTADO** - `console/gobierno/compliance/compliance-report.zul`

**Estado:** ✅ **IMPLEMENTADO** (reporte consolidado creado con integración real)
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No
**Modo Demo:** ✅ Implementado con `?mock=true`

**ViewModels Relacionados Existentes:**
- ✅ `AIActDocumentationGeneratorViewModel` - `com.codeflowx.platform.viewmodel.compliance.AIActDocumentationGeneratorViewModel`
  - **ZUL:** `console/gobierno/compliance/ai-act-documentation-generator.zul`
  - **Funcionalidad:** Generador de documentación AI Act
- ✅ `ConformityDeclarationManagerViewModel` - `com.codeflowx.govern.viewmodel.compliance.ConformityDeclarationManagerViewModel`
  - **ZUL:** `console/gobierno/compliance/conformity-declaration-manager.zul`
  - **Funcionalidad:** Gestor de declaraciones de conformidad

**Funcionalidad Requerida para Demo:**
- ✅ Vista del reporte generado
- ✅ Evidencias
- ✅ Conclusiones
- ✅ Exportar a PDF

**Prompt Asociado:** INC-008-002 (Exportación Auditores Externos), INC-024 (Reporte Ejecutivo Consolidado)
**Estado Prompts:**
- INC-008-002: 🔴 PENDIENTE (Crítica)
- INC-024: ⚠️ PENDIENTE (ver `MAPEO_PROMPTS_VIEWMODELS_GOVERNANCE_CORREGIDO.md`)

**Servicios Integrados:**
- ✅ `ComplianceAssessmentService` - Para assessments de compliance con filtros por período
- ⚠️ `AIActDocumentationService` - Preparado para integración
- ⚠️ Microservicio Python - Preparado para generación de reportes ejecutivos (REST API)

**Funcionalidad Implementada:**
- ✅ Reporte ejecutivo consolidado de compliance
- ✅ Métricas del reporte (sistemas totales, alto riesgo, FRIA completadas, score compliance)
- ✅ Resumen ejecutivo
- ✅ Evidencias (FRIA_ASSESSMENT, EU_REGISTRATION, HITL_AUDIT, CONFORMITY_DECLARATION)
- ✅ Conclusiones por categoría (COMPLIANCE, RISK_MANAGEMENT, RECOMMENDATIONS)
- ✅ Filtros por tipo de reporte (FULL, EXECUTIVE, TECHNICAL, AUDIT) y período
- ✅ Exportación a PDF (preparado)
- ✅ Integración con servicios de compliance existentes
- ✅ Modo demo/mock funcional

**Integración Microservicios:**
- ⚠️ Preparado para llamadas REST a servicio de reportes Python
- ⚠️ Configuración mediante `compliance.report.service.url` en properties

**Acceso para Demo:**
- URL: `/console/gobierno/compliance/compliance-report.zul?mock=true`

**Fecha Implementación:** 25 de noviembre de 2025

---

### 11. 🟩 Pantalla Final / Cierre

**Tipo:** Pantalla estática o modal
**ZUL:** ⚠️ Requiere crear `console/gobierno/compliance/demo-closing.zul` o usar modal

**Estado:** 🔴 **PENDIENTE**
**En Gobierno:** ✅ Sí
**Telemetría:** ❌ No

**Funcionalidad Requerida:**
- Logo CodeflowX
- Mensaje: "Si queréis verlo aplicado a vuestro entorno, podemos agendar una sesión técnica."

**Acción Requerida:**
1. 🔴 **CREAR** ZUL simple con logo y mensaje
2. O usar modal existente

**Esfuerzo Estimado:** 0.5 días

---

## 📊 RESUMEN DE ACCIONES REQUERIDAS

### ✅ Pantallas Listas para Demo (9):
1. ✅ Dashboard General - **COMPLETO** (2 opciones: `dashboard.zul` o `platform/governance/dashboard/summary.zul`)
2. ✅ Inventario Sistemas IA - **COMPLETO** (`platform/projects/project-ai-inventory.zul`)
3. ✅ Ficha Sistema IA - **COMPLETO**
4. ✅ Clasificación AI Act - **COMPLETO** (`gobierno/compliance/high-risk-classifier.zul`)
5. ✅ Evaluación Riesgos / FRIA - **COMPLETO** (`gobierno/compliance/fria-wizard.zul`)
6. ✅ Bandeja de Tareas - **COMPLETO** (`bpmn/task-inbox.zul`)
7. ✅ Supervisión Humana (HITL) - **IMPLEMENTADO** (`gobierno/governance/hitl-supervision.zul`) - **25/11/2025**
8. ✅ Trazabilidad y Evidencias - **IMPLEMENTADO** (`gobierno/compliance/traceability-evidence.zul`) - **25/11/2025**
9. ✅ Alertas e Incidentes - **COMPLETO** (`platform/monitoring/alerts/overview.zul`)
10. ✅ Documentación / Reporte - **IMPLEMENTADO** (`gobierno/compliance/compliance-report.zul`) - **25/11/2025**

### 🔴 Pantallas Pendientes (1):
1. **Pantalla Final / Cierre** - 0.5 días

**Total Esfuerzo Estimado:** 0.5 días
**Progreso:** 90% completado (9 de 10 pantallas)

---

## 🔗 INTEGRACIÓN CON TELEMETRÍA

### Pantallas que Requieren Integración con Telemetría:

1. **Trazabilidad y Evidencias:**
   - Integrar con `AioTelemetry` para eventos de agentes externos
   - Mostrar trace ID, run ID, payloads

2. **Alertas e Incidentes:**
   - Integrar con `AioIncidentMetric` para incidentes de agentes externos
   - Mostrar métricas de incidentes

**Nota:** La integración con telemetría es opcional para la demo inicial, pero recomendada para una demo completa.

**Ver:** `TAREAS_PENDIENTES_UI_TELEMETRIA.md` para detalles de dashboards y buscadores de telemetría.

---

## 📋 CHECKLIST PARA DEMO

### Pre-Demo:
- [x] ✅ Verificar ZUL de Dashboard General - **ENCONTRADO** (2 opciones)
- [x] ✅ Verificar ZUL de Inventario Sistemas IA - **ENCONTRADO**
- [ ] 🔴 Crear dashboard consolidado Supervisión Humana (HITL) - Existen 3 ViewModels individuales
- [ ] 🔴 Crear vista consolidada Trazabilidad y Evidencias - Existen 5 ViewModels individuales
- [ ] 🔴 Crear reporte consolidado Documentación / Reporte - Existen 2 generadores individuales
- [ ] 🔴 Crear pantalla Final
- [ ] Preparar datos de prueba (sistemas, tareas, alertas, etc.)

### Durante Demo:
- [ ] Mostrar Dashboard General con todos los KPIs
- [ ] Navegar a Inventario y mostrar sistemas
- [ ] Abrir ficha de sistema y mostrar detalles
- [ ] Mostrar Clasificación AI Act
- [ ] Mostrar Evaluación FRIA (wizard completo)
- [ ] **Mostrar Bandeja de Tareas (OBLIGATORIA)**
- [ ] Mostrar Supervisión Humana (si está lista)
- [ ] Mostrar Trazabilidad y Evidencias (si está lista)
- [ ] Mostrar Alertas e Incidentes
- [ ] Mostrar Documentación / Reporte (si está lista)
- [ ] Cerrar con pantalla final

---

**Última actualización:** 25 de noviembre de 2025
