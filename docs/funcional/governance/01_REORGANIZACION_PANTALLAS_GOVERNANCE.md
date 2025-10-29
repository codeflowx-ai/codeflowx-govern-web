# 🖥️ REORGANIZACIÓN DE PANTALLAS - MÓDULO GOVERNANCE

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Reorganización de pantallas ZUL del módulo governance siguiendo estructura Next.js

---

## ✅ REORGANIZACIÓN COMPLETADA

**Fecha de Implementación:** Octubre 2025  
**Estado:** ✅ **COMPLETADO**

### **Resumen de la Reorganización:**
- **Pantallas Reorganizadas:** 43 pantallas
- **Módulos Funcionales Creados:** 13 módulos
- **Pantallas BPMN Preservadas:** 2 pantallas en `console/bpmn/`
- **Referencias Actualizadas:** 4 ViewModels

### **Estructura Final Implementada:**

#### **1. Dashboard (2 pantallas):**
- `dashboard/overview.zul` - Vista general de gobierno
- `dashboard/summary.zul` - Resumen de dashboard

#### **2. Métricas (3 pantallas):**
- `metrics/page.zul` - Detalle/Edición de métrica
- `metrics/overview.zul` - Vista general de métricas
- `metrics/summary.zul` - Resumen de métricas

#### **3. Auditoría (3 pantallas):**
- `audit/log.zul` - Detalle de logs de auditoría
- `audit/log-overview.zul` - Vista general de logs
- `audit/trail.zul` - Trazabilidad detallada

#### **4. KPIs Ejecutivos (1 pantalla):**
- `kpis/executive.zul` - KPIs ejecutivos

#### **5. Compliance (7 pantallas):**
- `compliance/page.zul` - Detalle de evaluación
- `compliance/assessment.zul` - Vista general de evaluaciones
- `compliance/finding.zul` - Detalle de hallazgos
- `compliance/finding-overview.zul` - Vista general de hallazgos
- `compliance/requirement.zul` - Detalle de requisitos
- `compliance/requirement-overview.zul` - Vista general de requisitos
- `compliance/by-framework.zul` - Vista por framework
- `compliance/gaps-analysis.zul` - Análisis de gaps

#### **6. Políticas (12 pantallas):**
- `policies/page.zul` - Detalle de políticas
- `policies/overview.zul` - Vista general de políticas
- `policies/rule.zul` - Detalle de reglas
- `policies/rule-overview.zul` - Vista general de reglas
- `policies/evaluation.zul` - Detalle de evaluación
- `policies/evaluation-overview.zul` - Vista general de evaluación
- `policies/violation.zul` - Detalle de violaciones
- `policies/violation-overview.zul` - Vista general de violaciones
- `policies/validation-config.zul` - Detalle de configuración
- `policies/validation-config-overview.zul` - Vista general de configuración
- `policies/checklist-item.zul` - Detalle de items de checklist
- `policies/checklist-item-overview.zul` - Vista general de items

#### **7. Seguridad (6 pantallas):**
- `security/policy.zul` - Detalle de políticas de seguridad
- `security/policy-overview.zul` - Vista general de políticas
- `security/metric.zul` - Detalle de métricas de seguridad
- `security/metric-overview.zul` - Vista general de métricas
- `security/threat.zul` - Detalle de amenazas
- `security/threat-overview.zul` - Vista general de amenazas

#### **8. Calidad (3 pantallas):**
- `quality/dataset.zul` - Vista general de calidad de datasets
- `quality/dataset-review.zul` - Formulario de revisión de calidad
- `quality/ethics-review.zul` - Revisión ética

#### **9. Reportes (1 pantalla):**
- `reports/effectiveness.zul` - Reportes de efectividad

#### **10. Riesgos (1 pantalla):**
- `risks/matrix.zul` - Matriz de evaluación de riesgos

#### **11. Analytics (3 pantallas):**
- `analytics/auto-approval.zul` - Analytics de auto-aprobación
- `analytics/evaluation-trends.zul` - Tendencias de evaluación
- `analytics/violation-heatmap.zul` - Heatmap de violaciones

#### **12. Pantallas BPMN Preservadas (2 pantallas):**
- `console/bpmn/compliance-review-form.zul` - Formulario de revisión de compliance
- `console/bpmn/compliance-review-decision-form.zul` - Formulario de decisión de compliance

### **Referencias Actualizadas en ViewModels:**
1. **GovernanceDetailViewModel.java:** `/governance/governance-overview.zul` → `/governance/dashboard/overview.zul`
2. **GovernanceOverviewViewModel.java:** `gobierno/governance/governance-detail.zul` → `governance/policies/page.zul`
3. **BiasUrgentDecisionViewModel.java:** `/console/govern/governance-reports.zul` → `/governance/reports/effectiveness.zul`
4. **ComplianceReviewDecisionViewModel.java:** `/console/govern/governance-reports.zul` → `/governance/reports/effectiveness.zul`

### **Beneficios Obtenidos:**
- ✅ **Organización funcional** clara y consistente
- ✅ **Navegación mejorada** por módulos específicos
- ✅ **Alineación** con estructura Next.js
- ✅ **Separación** entre pantallas CRUD y consulta
- ✅ **Preservación** de pantallas BPMN críticas
- ✅ **Referencias actualizadas** para funcionamiento correcto

---

---

## 📊 ANÁLISIS DE PANTALLAS ACTUALES

### **Distribución Actual (47 pantallas reales):**

#### **Console/Platform/Governance (42 pantallas):**

**Compliance (8 pantallas):**
- `compliance-assessment-detail.zul` - Detalle de evaluación
- `compliance-assessment-overview.zul` - Vista general de evaluaciones
- `compliance-by-framework-overview.zul` - Vista por framework
- `compliance-finding-detail.zul` - Detalle de hallazgos
- `compliance-finding-overview.zul` - Vista general de hallazgos
- `compliance-gaps-analysis-overview.zul` - Análisis de gaps
- `compliance-requirement-detail.zul` - Detalle de requisitos
- `compliance-requirement-overview.zul` - Vista general de requisitos

**Policy Management (16 pantallas):**
- `policy-detail.zul` - Detalle de políticas
- `policy-overview.zul` - Vista general de políticas
- `policy-rule-detail.zul` - Detalle de reglas
- `policy-rule-overview.zul` - Vista general de reglas
- `policy-evaluation-detail.zul` - Detalle de evaluación
- `policy-evaluation-overview.zul` - Vista general de evaluación
- `policy-evaluation-trends-overview.zul` - Tendencias de evaluación
- `policy-violation-detail.zul` - Detalle de violaciones
- `policy-violation-overview.zul` - Vista general de violaciones
- `policy-validation-config-detail.zul` - Detalle de configuración
- `policy-validation-config-overview.zul` - Vista general de configuración
- `policy-audit-log-detail.zul` - Detalle de logs
- `policy-audit-log-overview.zul` - Vista general de logs
- `policy-checklist-item-detail.zul` - Detalle de checklist
- `policy-checklist-item-overview.zul` - Vista general de checklist
- `policy-effectiveness-report-overview.zul` - Reporte de efectividad

**Security (6 pantallas):**
- `security-policy-detail.zul` - Detalle de políticas de seguridad
- `security-policy-overview.zul` - Vista general de políticas de seguridad
- `security-metric-detail.zul` - Detalle de métricas de seguridad
- `security-metric-overview.zul` - Vista general de métricas de seguridad
- `security-threat-detail.zul` - Detalle de amenazas
- `security-threat-overview.zul` - Vista general de amenazas

**Risk Assessment (3 pantallas):**
- `risk-assessment-matrix-overview.zul` - Matriz de evaluación de riesgos
- `risk-indicator-detail.zul` - Detalle de indicadores de riesgo
- `risk-indicator-overview.zul` - Vista general de indicadores de riesgo

**Monitoring (4 pantallas):**
- `governance-metric-detail.zul` - Detalle de métricas
- `governance-metric-overview.zul` - Vista general de métricas
- `governance-metrics-summary-overview.zul` - Resumen de métricas
- `governance-dashboard-summary-overview.zul` - Resumen de dashboard

**Quality Control (3 pantallas):**
- `dataset-quality-overview.zul` - Vista general de calidad de datasets
- `dataset-quality-review-form.zul` - Formulario de revisión de calidad
- `ethics-review-overview.zul` - Vista general de revisión ética

**Analytics (2 pantallas):**
- `auto-approval-analytics-overview.zul` - Analytics de auto-aprobación
- `violation-heatmap-overview.zul` - Heatmap de violaciones

#### **Console/Gobierno/Governance (2 pantallas):**
- `governance-detail.zul` - Detalle de gobierno
- `governance-overview.zul` - Vista general de gobierno

#### **Console/Platform/Training (2 pantallas):**
- `training-governance-detail.zul` - Detalle de gobierno de entrenamiento
- `training-governance-overview.zul` - Vista general de gobierno de entrenamiento

#### **Console/Platform/Views/Governance (1 pantalla):**
- `views/governance/dataset-quality-dashboard-overview.zul` - Dashboard de calidad de datasets

---

## 📊 ANÁLISIS: PANTALLAS NEXT.JS vs ZKOSS - MÓDULO GOVERNANCE

### **Pantallas que EXISTEN en Next.js pero NO en ZKoss:**

#### **1. Auto-aprobación:**
- `governance/auto-approval/` - Sistema de auto-aprobación automatizada
- **Funcionalidad:** Configuración y monitoreo de auto-aprobaciones
- **Estado:** ❌ **FALTANTE** - Requiere implementación

#### **2. Servicios de Governance:**
- `governance/services/` - Gestión de servicios de governance
- **Funcionalidad:** Configuración de servicios de governance
- **Estado:** ❌ **FALTANTE** - Requiere implementación

### **Pantallas que EXISTEN en ZKoss pero NO están claramente definidas en Next.js:**

#### **1. Políticas Detalladas:**
- `policy-detail.zul`, `policy-overview.zul` - Gestión completa de políticas
- **Funcionalidad:** CRUD completo de políticas de governance
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **2. Reglas de Políticas:**
- `policy-rule-detail.zul`, `policy-rule-overview.zul` - Gestión de reglas
- **Funcionalidad:** Configuración de reglas de políticas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **3. Evaluación de Políticas:**
- `policy-evaluation-detail.zul`, `policy-evaluation-overview.zul` - Evaluación de políticas
- **Funcionalidad:** Evaluación y scoring de políticas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **4. Violaciones de Políticas:**
- `policy-violation-detail.zul`, `policy-violation-overview.zul` - Gestión de violaciones
- **Funcionalidad:** Tracking y gestión de violaciones
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **5. Configuración de Validación:**
- `policy-validation-config-detail.zul`, `policy-validation-config-overview.zul` - Configuración de validación
- **Funcionalidad:** Configuración de validaciones automáticas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **6. Logs de Auditoría de Políticas:**
- `policy-audit-log-detail.zul`, `policy-audit-log-overview.zul` - Logs de auditoría
- **Funcionalidad:** Auditoría detallada de políticas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **7. Items de Checklist:**
- `policy-checklist-item-detail.zul`, `policy-checklist-item-overview.zul` - Items de checklist
- **Funcionalidad:** Gestión de checklist de políticas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **8. Reportes de Efectividad:**
- `policy-effectiveness-report-overview.zul` - Reportes de efectividad
- **Funcionalidad:** Análisis de efectividad de políticas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **9. Tendencias de Evaluación:**
- `policy-evaluation-trends-overview.zul` - Tendencias de evaluación
- **Funcionalidad:** Análisis de tendencias de políticas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **10. Heatmap de Violaciones:**
- `violation-heatmap-overview.zul` - Heatmap de violaciones
- **Funcionalidad:** Visualización de violaciones por área
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **11. Seguridad Detallada:**
- `security-policy-detail.zul`, `security-policy-overview.zul` - Políticas de seguridad
- `security-metric-detail.zul`, `security-metric-overview.zul` - Métricas de seguridad
- `security-threat-detail.zul`, `security-threat-overview.zul` - Amenazas de seguridad
- **Funcionalidad:** Gestión completa de seguridad
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **12. Calidad de Datasets:**
- `dataset-quality-overview.zul`, `dataset-quality-review-form.zul` - Calidad de datasets
- **Funcionalidad:** Evaluación de calidad de datos
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **13. Revisión Ética:**
- `ethics-review-overview.zul` - Revisión ética
- **Funcionalidad:** Evaluación ética de sistemas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **14. Analytics de Auto-aprobación:**
- `auto-approval-analytics-overview.zul` - Analytics de auto-aprobación
- **Funcionalidad:** Análisis de auto-aprobaciones
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

### **Pantallas que EXISTEN en AMBOS (con nombres diferentes):**

#### **Compliance:**
- **Next.js:** `governance/compliance/` ↔ **ZKoss:** `compliance-assessment-*`, `compliance-finding-*`, `compliance-requirement-*`
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

#### **Monitoring:**
- **Next.js:** `governance/monitoring/` ↔ **ZKoss:** `governance-metric-*`, `governance-metrics-summary-*`
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

#### **Risk Assessment:**
- **Next.js:** `governance/risk-assessment/` ↔ **ZKoss:** `risk-assessment-matrix-overview.zul`
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

#### **Security:**
- **Next.js:** `governance/security/` ↔ **ZKoss:** `security-*` pantallas
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

### **Resumen de Deficiencias:**

| Funcionalidad | Next.js | ZKoss | Estado |
|---------------|---------|-------|--------|
| **Auto-aprobación** | ✅ | ❌ | **FALTANTE** |
| **Servicios Governance** | ✅ | ❌ | **FALTANTE** |
| **Políticas Detalladas** | ❌ | ✅ | **ADICIONAL** |
| **Reglas de Políticas** | ❌ | ✅ | **ADICIONAL** |
| **Evaluación de Políticas** | ❌ | ✅ | **ADICIONAL** |
| **Violaciones** | ❌ | ✅ | **ADICIONAL** |
| **Configuración Validación** | ❌ | ✅ | **ADICIONAL** |
| **Logs Auditoría** | ❌ | ✅ | **ADICIONAL** |
| **Checklist Items** | ❌ | ✅ | **ADICIONAL** |
| **Reportes Efectividad** | ❌ | ✅ | **ADICIONAL** |
| **Tendencias Evaluación** | ❌ | ✅ | **ADICIONAL** |
| **Heatmap Violaciones** | ❌ | ✅ | **ADICIONAL** |
| **Seguridad Detallada** | ❌ | ✅ | **ADICIONAL** |
| **Calidad Datasets** | ❌ | ✅ | **ADICIONAL** |
| **Revisión Ética** | ❌ | ✅ | **ADICIONAL** |
| **Analytics Auto-aprobación** | ❌ | ✅ | **ADICIONAL** |

### **Acciones Requeridas:**

#### **Implementar en ZKoss:**
1. **Auto-aprobación** - Sistema de auto-aprobación automatizada
2. **Servicios Governance** - Gestión de servicios de governance

#### **Mantener en ZKoss (Funcionalidades Avanzadas):**
1. **Gestión Completa de Políticas** - CRUD avanzado de políticas
2. **Sistema de Reglas** - Configuración de reglas de políticas
3. **Evaluación y Scoring** - Evaluación automática de políticas
4. **Gestión de Violaciones** - Tracking completo de violaciones
5. **Configuración de Validación** - Validaciones automáticas
6. **Auditoría Detallada** - Logs completos de auditoría
7. **Sistema de Checklist** - Checklist de políticas
8. **Reportes de Efectividad** - Análisis de efectividad
9. **Análisis de Tendencias** - Tendencias de políticas
10. **Visualización Avanzada** - Heatmaps de violaciones
11. **Seguridad Integral** - Gestión completa de seguridad
12. **Calidad de Datos** - Evaluación de calidad de datasets
13. **Revisión Ética** - Evaluación ética de sistemas
14. **Analytics Avanzados** - Análisis de auto-aprobaciones

### **Procesos BPMN de Governance (2 procesos):**

#### **1. Compliance Monitoring (compliance-monitoring-v1.bpmn):**
- **Propósito:** Monitoreo automático de compliance cada 24 horas
- **Funcionalidades:**
  - Ejecución automática de verificaciones de compliance
  - Detección de no conformidades
  - Creación automática de alertas
  - Revisión manual de issues por compliance officers
  - Gestión de incidentes críticos
  - Actualización automática de dashboards
- **Pantallas BPMN:** `compliance-review-form.zul`, `compliance-review-decision-form.zul`

#### **2. Risk Assessment (risk-assessment-v1.bpmn):**
- **Propósito:** Evaluación integral de riesgos
- **Funcionalidades:**
  - Evaluación paralela de riesgos técnicos, de negocio y de compliance
  - Consolidación de evaluaciones
  - Cálculo automático de score de riesgo usando Drools
  - Revisión manual por governance-admins y risk-officers
  - Almacenamiento de evaluaciones
- **Pantallas BPMN:** `risk-review-form.zul`

### **Procesos BPMN Relacionados con Governance (8 procesos):**

#### **1. Ethics Review (ethics-review-v1.bpmn):**
- **Propósito:** Revisión ética de sistemas de IA
- **Relación:** Parte del framework de governance ético

#### **2. Bias Detection (bias-detection-v1.bpmn):**
- **Propósito:** Detección automática de sesgos
- **Relación:** Monitoreo de governance de equidad

#### **3. Dataset Quality (dataset-quality-v1.bpmn):**
- **Propósito:** Evaluación de calidad de datasets
- **Relación:** Governance de calidad de datos

#### **4. Performance Degradation (performance-degradation-v1.bpmn):**
- **Propósito:** Detección de degradación de rendimiento
- **Relación:** Monitoreo de governance de rendimiento

#### **5. Alert Response (alert-response-v1.bpmn):**
- **Propósito:** Respuesta a alertas del sistema
- **Relación:** Gestión de alertas de governance

#### **6. Incident Response RCA (incident-response-rca-v1.bpmn):**
- **Propósito:** Análisis de causa raíz de incidentes
- **Relación:** Governance de gestión de incidentes

#### **7. Deployment Automation (deployment-automation-v1.bpmn):**
- **Propósito:** Automatización de despliegues
- **Relación:** Governance de despliegues

#### **8. Model Retraining Orchestration (model-retraining-orchestration-v1.bpmn):**
- **Propósito:** Orquestación de reentrenamiento de modelos
- **Relación:** Governance de ciclo de vida de modelos

---

### **Pantallas Identificadas pero No Analizadas:**

#### **Pantallas de Governance por Módulo:**
- **AgentGovernance:** Pantallas específicas de gobierno de agentes
- **ModelGovernance:** Pantallas específicas de gobierno de modelos  
- **RAGGovernance:** Pantallas específicas de gobierno de sistemas RAG
- **PromptGovernance:** Pantallas específicas de gobierno de prompts

#### **Pantallas de Métricas Avanzadas:**
- **Governance Analytics:** Pantallas de analytics avanzados de gobierno
- **Governance Trends:** Pantallas de análisis de tendencias
- **Governance Benchmarking:** Pantallas de benchmarking de gobierno
- **Governance Risk Assessment:** Pantallas de evaluación de riesgos

#### **Pantallas de Reportes Ejecutivos:**
- **Executive Dashboards:** Dashboards específicos para ejecutivos
- **Governance Scorecards:** Scorecards de gobierno por área
- **Compliance Dashboards:** Dashboards de compliance integrados
- **Risk Dashboards:** Dashboards de gestión de riesgos

### **Acciones Requeridas:**
1. **Identificar** pantallas adicionales en otros módulos
2. **Analizar** funcionalidad de cada pantalla pendiente
3. **Mapear** relaciones con entidades JPA existentes
4. **Evaluar** necesidad de nuevas entidades o vistas
5. **Documentar** integración con procesos BPMN

### **Estado:**
- ✅ **43 pantallas** analizadas y documentadas
- 🔄 **Pantallas pendientes** identificadas para análisis futuro
- 📋 **Plan de análisis** definido para completar cobertura

---

## 🎯 ESTRUCTURA OBJETIVO (8 MÓDULOS)

### **1. Dashboard Ejecutivo**
**Propósito:** Vista general y KPIs principales para ejecutivos
- **Pantalla Principal:** `governance-executive-dashboard.zul`
- **Funcionalidades:**
  - KPIs principales de gobierno de IA
  - Métricas de compliance en tiempo real
  - Alertas ejecutivas de riesgos
  - Tendencias de gobierno
  - Acceso rápido a funciones críticas

### **2. Métricas de Gobierno**
**Propósito:** CRUD de métricas principales de gobierno
- **Pantallas CRUD:**
  - `governance-metrics-list.zul` - Lista de métricas
  - `governance-metric-detail.zul` - Detalle/Edición de métrica
  - `governance-metric-create.zul` - Creación de métrica
- **Pantallas de Consulta:**
  - `governance-metrics-overview.zul` - Vista general
  - `governance-metrics-analytics.zul` - Analytics avanzados

### **3. Auditoría y Compliance**
**Propósito:** Gestión de auditoría y compliance
- **Pantallas CRUD:**
  - `governance-audit-list.zul` - Lista de auditorías
  - `governance-audit-detail.zul` - Detalle/Edición
  - `governance-audit-create.zul` - Creación de auditoría
- **Pantallas de Consulta:**
  - `governance-audit-overview.zul` - Vista general
  - `governance-audit-trail.zul` - Trazabilidad completa

### **4. KPIs Ejecutivos**
**Propósito:** Gestión de KPIs para ejecutivos
- **Pantallas CRUD:**
  - `governance-kpis-list.zul` - Lista de KPIs
  - `governance-kpi-detail.zul` - Detalle/Edición
  - `governance-kpi-create.zul` - Creación de KPI
- **Pantallas de Consulta:**
  - `governance-kpis-overview.zul` - Vista general
  - `governance-kpis-executive.zul` - Vista ejecutiva

### **5. Control de Riesgos**
**Propósito:** Gestión de riesgos de IA
- **Pantallas CRUD:**
  - `governance-risks-list.zul` - Lista de riesgos
  - `governance-risk-detail.zul` - Detalle/Edición
  - `governance-risk-create.zul` - Creación de riesgo
- **Pantallas de Consulta:**
  - `governance-risks-overview.zul` - Vista general
  - `governance-risk-analysis.zul` - Análisis de riesgos

### **6. Reportes y Documentación**
**Propósito:** Generación de reportes y documentación
- **Pantallas CRUD:**
  - `governance-reports-list.zul` - Lista de reportes
  - `governance-report-detail.zul` - Detalle/Edición
  - `governance-report-create.zul` - Creación de reporte
- **Pantallas de Consulta:**
  - `governance-reports-overview.zul` - Vista general
  - `governance-reports-generator.zul` - Generador de reportes

### **7. Configuración de Gobierno**
**Propósito:** Configuración de políticas y reglas de gobierno
- **Pantallas CRUD:**
  - `governance-policies-list.zul` - Lista de políticas
  - `governance-policy-detail.zul` - Detalle/Edición
  - `governance-policy-create.zul` - Creación de política
- **Pantallas de Consulta:**
  - `governance-policies-overview.zul` - Vista general
  - `governance-compliance-check.zul` - Verificación de compliance

### **8. Monitoreo y Alertas**
**Propósito:** Monitoreo continuo y gestión de alertas
- **Pantallas CRUD:**
  - `governance-alerts-list.zul` - Lista de alertas
  - `governance-alert-detail.zul` - Detalle/Edición
- **Pantallas de Consulta:**
  - `governance-alerts-overview.zul` - Vista general
  - `governance-monitoring.zul` - Monitoreo en tiempo real

---

## 🔄 MAPEO DE MIGRACIÓN

### **Pantallas a Consolidar:**

#### **Dashboard Ejecutivo:**
- Pantallas de dashboard ejecutivo → `governance-executive-dashboard.zul`

#### **Métricas de Gobierno:**
- Pantallas de métricas → `governance-metrics-list.zul`
- Pantallas de detalle de métricas → `governance-metric-detail.zul`
- Pantallas de resumen → `governance-metrics-overview.zul`

#### **Auditoría y Compliance:**
- Pantallas de auditoría → `governance-audit-list.zul`
- Pantallas de auditoría detallada → `governance-audit-trail.zul`
- Pantallas de compliance → `governance-compliance-check.zul`

#### **KPIs Ejecutivos:**
- Pantallas de KPIs ejecutivos → `governance-kpis-executive.zul`
- Pantallas de KPIs generales → `governance-kpis-overview.zul`

#### **Control de Riesgos:**
- Pantallas de riesgos → `governance-risks-list.zul`
- Pantallas de análisis → `governance-risk-analysis.zul`

#### **Reportes:**
- Pantallas de reportes → `governance-reports-list.zul`
- Pantallas de generación → `governance-reports-generator.zul`

#### **Configuración:**
- Pantallas de políticas → `governance-policies-list.zul`
- Pantallas de configuración → `governance-policy-detail.zul`

#### **Monitoreo:**
- Pantallas de alertas → `governance-alerts-list.zul`
- Pantallas de monitoreo → `governance-monitoring.zul`

---

## 📋 PLAN DE MIGRACIÓN

### **Fase 1: Dashboard Ejecutivo (1-2 semanas)**
1. **Crear dashboard** ejecutivo principal
2. **Migrar KPIs** críticos
3. **Configurar alertas** ejecutivas
4. **Probar funcionalidad** básica

### **Fase 2: Métricas y Auditoría (2-3 semanas)**
1. **Migrar pantallas** de métricas
2. **Implementar** auditoría detallada
3. **Configurar** trazabilidad completa
4. **Probar integración** con otros módulos

### **Fase 3: Control de Riesgos y Reportes (2-3 semanas)**
1. **Migrar pantallas** de riesgos
2. **Implementar** generación de reportes
3. **Configurar** políticas de gobierno
4. **Probar funcionalidad** completa

### **Fase 4: Optimización (1-2 semanas)**
1. **Optimizar rendimiento** de pantallas
2. **Mejorar UX** y navegación
3. **Implementar** funcionalidades avanzadas
4. **Documentar** cambios y procedimientos

---

## ✅ BENEFICIOS DE LA REORGANIZACIÓN

### **Para Ejecutivos:**
- **Dashboard unificado** con KPIs críticos
- **Acceso rápido** a información ejecutiva
- **Alertas proactivas** de riesgos
- **Reportes automáticos** para toma de decisiones

### **Para Equipos de Gobierno:**
- **Herramientas consolidadas** para gestión
- **Flujos de trabajo** optimizados
- **Visibilidad completa** del estado de gobierno
- **Automatización** de procesos manuales

### **Para la Organización:**
- **Cumplimiento** mejorado con regulaciones
- **Reducción de riesgos** mediante monitoreo proactivo
- **Transparencia** hacia stakeholders
- **Eficiencia** operacional mejorada

---

## 🎯 CONCLUSIÓN

La reorganización del módulo governance de **43 pantallas** a **8 módulos funcionales** proporcionará:

- 🎯 **Dashboard ejecutivo** unificado y efectivo
- 🚀 **Mejor experiencia** de usuario para todos los niveles
- 🔧 **Herramientas consolidadas** para gestión de gobierno
- 📊 **KPIs y métricas** optimizadas y accesibles
- 🎨 **Alineación** con estructura Next.js

**Esta reorganización está diseñada** para mejorar significativamente la efectividad del gobierno de IA en la organización.

---

## 📊 ANÁLISIS: PANTALLAS NEXT.JS vs ZKOSS - MÓDULO GOVERNANCE

### **Pantallas que EXISTEN en Next.js pero NO en ZKoss:**

#### **1. Auto-aprobación:**
- `governance/auto-approval/` - Sistema de auto-aprobación automatizada
- **Funcionalidad:** Configuración de reglas de auto-aprobación
- **Estado:** ❌ **FALTANTE** - Requiere implementación

#### **2. Servicios de Governance:**
- `governance/services/` - Servicios centralizados de governance
- **Funcionalidad:** APIs y servicios de governance
- **Estado:** ❌ **FALTANTE** - Requiere implementación

### **Pantallas que EXISTEN en ZKoss pero NO están claramente definidas en Next.js:**

#### **1. Análisis de Auto-aprobación:**
- `auto-approval-analytics-overview.zul` - Analytics de auto-aprobación
- **Funcionalidad:** Análisis detallado de decisiones de auto-aprobación
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **2. Evaluación de Calidad de Datasets:**
- `dataset-quality-overview.zul` - Evaluación de calidad de datasets
- `dataset-quality-review-form.zul` - Formulario de revisión de calidad
- **Funcionalidad:** Control de calidad específico de datasets
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **3. Revisión Ética:**
- `ethics-review-overview.zul` - Vista general de revisiones éticas
- **Funcionalidad:** Evaluaciones éticas específicas
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **4. Matriz de Evaluación de Riesgos:**
- `risk-assessment-matrix-overview.zul` - Matriz de evaluación de riesgos
- **Funcionalidad:** Evaluación matricial de riesgos
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **5. Heatmap de Violaciones:**
- `violation-heatmap-overview.zul` - Heatmap de violaciones
- **Funcionalidad:** Visualización de violaciones por área/tiempo
- **Estado:** ✅ **IMPLEMENTADO** - Funcionalidad adicional

#### **6. Governance por Módulo:**
- **Agents Governance:** `agents/governance/` (3 pantallas)
- **Training Governance:** `training/training-governance-*` (2 pantallas)
- **Views Governance:** `views/governance/` (2 pantallas)
- **Estado:** ✅ **IMPLEMENTADO** - Governance específico por módulo

### **Pantallas que EXISTEN en AMBOS (con nombres diferentes):**

#### **Compliance:**
- **Next.js:** `governance/compliance/` ↔ **ZKoss:** `compliance-*` (8 pantallas)
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

#### **Monitoring:**
- **Next.js:** `governance/monitoring/` ↔ **ZKoss:** `governance-metric-*` (4 pantallas)
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

#### **Policies:**
- **Next.js:** `governance/policies/` ↔ **ZKoss:** `policy-*` (16 pantallas)
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

#### **Risk Assessment:**
- **Next.js:** `governance/risk-assessment/` ↔ **ZKoss:** `risk-*` (3 pantallas)
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

#### **Security:**
- **Next.js:** `governance/security/` ↔ **ZKoss:** `security-*` (6 pantallas)
- **Estado:** ✅ **EQUIVALENTE** - Funcionalidad similar

### **Resumen de Deficiencias:**

| Funcionalidad | Next.js | ZKoss | Estado |
|---------------|---------|-------|--------|
| **Auto-aprobación** | ✅ | ❌ | **FALTANTE** |
| **Servicios Governance** | ✅ | ❌ | **FALTANTE** |
| **Analytics Auto-aprobación** | ❌ | ✅ | **ADICIONAL** |
| **Calidad Datasets** | ❌ | ✅ | **ADICIONAL** |
| **Revisión Ética** | ❌ | ✅ | **ADICIONAL** |
| **Matriz Riesgos** | ❌ | ✅ | **ADICIONAL** |
| **Heatmap Violaciones** | ❌ | ✅ | **ADICIONAL** |
| **Governance por Módulo** | ❌ | ✅ | **ADICIONAL** |

### **Distribución Real de Pantallas ZKoss (47 pantallas):**

#### **Console/Platform/Governance (42 pantallas):**
- **Compliance (8 pantallas):** assessment, finding, requirement, gaps
- **Policy Management (16 pantallas):** detail, overview, rule, validation, violation, evaluation
- **Security (6 pantallas):** metric, policy, threat
- **Risk Assessment (3 pantallas):** matrix, indicator
- **Monitoring (4 pantallas):** metric, dashboard, KPIs, audit trail
- **Quality Control (3 pantallas):** dataset quality, ethics review
- **Analytics (2 pantallas):** auto-approval analytics, violation heatmap

#### **Console/Gobierno/Governance (2 pantallas):**
- `governance-detail.zul` - Detalle de gobierno
- `governance-overview.zul` - Vista general de gobierno

#### **Console/Platform/Otros (3 pantallas):**
- **Training Governance:** `training-governance-*` (2 pantallas)
- **Views Governance:** `views/governance/` (1 pantalla)

### **Acciones Requeridas:**

#### **Implementar en ZKoss:**
1. **Auto-aprobación** - Sistema de configuración de auto-aprobación
2. **Servicios Governance** - APIs centralizadas de governance

#### **Mantener en ZKoss:**
1. **Analytics Auto-aprobación** - Funcionalidad valiosa adicional
2. **Calidad Datasets** - Control específico de calidad
3. **Revisión Ética** - Evaluaciones éticas especializadas
4. **Matriz Riesgos** - Evaluación matricial avanzada
5. **Heatmap Violaciones** - Visualización avanzada
6. **Governance por Módulo** - Governance específico por área

---

### **Pantallas Identificadas pero No Analizadas:**

#### **Pantallas de Governance por Módulo:**
- **AgentGovernance:** Pantallas específicas de gobierno de agentes
- **ModelGovernance:** Pantallas específicas de gobierno de modelos  
- **RAGGovernance:** Pantallas específicas de gobierno de sistemas RAG
- **PromptGovernance:** Pantallas específicas de gobierno de prompts

#### **Pantallas de Métricas Avanzadas:**
- **Governance Analytics:** Pantallas de analytics avanzados de gobierno
- **Governance Trends:** Pantallas de análisis de tendencias
- **Governance Benchmarking:** Pantallas de benchmarking de gobierno
- **Governance Risk Assessment:** Pantallas de evaluación de riesgos

#### **Pantallas de Reportes Ejecutivos:**
- **Executive Dashboards:** Dashboards específicos para ejecutivos
- **Governance Scorecards:** Scorecards de gobierno por área
- **Compliance Dashboards:** Dashboards de compliance integrados
- **Risk Dashboards:** Dashboards de gestión de riesgos

### **Acciones Requeridas:**
1. **Identificar** pantallas adicionales en otros módulos
2. **Analizar** funcionalidad de cada pantalla pendiente
3. **Mapear** relaciones con entidades JPA existentes
4. **Evaluar** necesidad de nuevas entidades o vistas
5. **Documentar** integración con procesos BPMN

### **Estado:**
- ✅ **12 pantallas** analizadas y documentadas
- 🔄 **Pantallas pendientes** identificadas para análisis futuro
- 📋 **Plan de análisis** definido para completar cobertura
