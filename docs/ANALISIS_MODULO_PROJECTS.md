# Análisis del Módulo Projects - Gobierno de IA, Datos y Cumplimiento Normativo

## Contexto

El módulo **Projects** está en la categoría **"governance"** y es la **base central del gobierno de IA, datos y cumplimiento normativo**.

**NO es una gestión tradicional de proyectos** porque:
- Se integrará con herramientas externas (Jira, ServiceNow, etc.)
- Su propósito es gobernar proyectos de IA/datos, no gestionar tareas/timelines
- El foco está en cumplimiento normativo, trazabilidad y gobernanza

---

## 🚫 Pantallas que SOBRAN (Gestión Tradicional)

Estas pantallas duplican funcionalidad que ya manejan Jira, ServiceNow u otras herramientas:

### ❌ Eliminar Completamente

1. **Time Tracking** (`/projects/time-tracking/list`)
   - **Razón**: Jira/ServiceNow ya manejan time tracking
   - **Acción**: Eliminar

2. **Tasks** (`/projects/tasks/list`)
   - **Razón**: Jira maneja tareas y tickets
   - **Acción**: Eliminar (o convertir en vista de integración con Jira)

3. **Invoices** (`/projects/invoices/list`)
   - **Razón**: Muy específico de gestión tradicional de proyectos
   - **Acción**: Eliminar

4. **Billing Status** (`/projects/billing-status`)
   - **Razón**: Muy específico de gestión tradicional
   - **Acción**: Eliminar

5. **Billing Details** (`/projects/billing-details/list`)
   - **Razón**: Muy específico de gestión tradicional
   - **Acción**: Eliminar

6. **Timeline Gantt** (`/projects/timeline-gantt`)
   - **Razón**: Jira/Project manejan Gantt charts
   - **Acción**: Eliminar (o convertir en vista de integración)

7. **Client Profitability** (`/projects/client-profitability`)
   - **Razón**: Muy específico de gestión tradicional
   - **Acción**: Eliminar

8. **Invoice Aging Report** (`/projects/invoice-aging`)
   - **Razón**: Muy específico de gestión tradicional
   - **Acción**: Eliminar

9. **Cost Estimators** (`/projects/cost-estimators/list`)
   - **Razón**: Muy específico de gestión tradicional
   - **Acción**: Eliminar

10. **Requirements** (`/projects/requirements/list`)
    - **Razón**: Jira maneja requirements
    - **Acción**: Eliminar (o convertir en vista de integración con Jira)

### ⚠️ Revisar/Adaptar

1. **Resource Allocation** (`/projects/resource-allocation`)
   - **Actual**: Asignación de recursos humanos
   - **Propuesta**: Enfocar en recursos de IA (GPUs, modelos, infraestructura)
   - **Acción**: Adaptar para mostrar recursos de IA por proyecto

2. **Cost Breakdown** (`/projects/cost-breakdown`)
   - **Actual**: Costos generales
   - **Propuesta**: Enfocar en costos de IA (compute, storage, APIs, modelos)
   - **Acción**: Adaptar para mostrar costos específicos de IA

3. **Financial Summary** (`/projects/financial-summary`)
   - **Actual**: Resumen financiero general
   - **Propuesta**: Enfocar en costos de infraestructura de IA
   - **Acción**: Adaptar o eliminar si no es relevante

4. **ROI Analysis** (`/projects/roi-analysis`)
   - **Actual**: ROI general
   - **Propuesta**: ROI específico de proyectos de IA (impacto de modelos, agentes)
   - **Acción**: Adaptar para métricas de IA

---

## ✅ Pantallas que FALTAN (Gobierno de IA/Datos/Cumplimiento)

**IMPORTANTE**: Projects es el **HUB CENTRAL** que **conecta y agrega** información de otros módulos, **NO duplica** funcionalidad.

### 🔴 Críticas (Alta Prioridad)

1. **Integrations Dashboard** (`/projects/integrations`)
   - **Propósito**: Vista centralizada de integraciones con herramientas externas
   - **Contenido**:
     - Estado de integraciones (Jira, ServiceNow, etc.)
     - Sincronización de datos
     - Configuración de conexiones
     - Logs de sincronización
   - **Roles**: admin, project_manager, governance_manager

2. **AI Governance Overview** (`/projects/ai-governance-overview`)
   - **Propósito**: Vista agregada de gobierno de IA por proyecto (NO duplica módulos)
   - **Contenido**:
     - Resumen de modelos asociados (link a Models module)
     - Resumen de agentes asociados (link a Agents module)
     - Compliance status agregado (link a Compliance module)
     - Approval workflows pendientes (link a BPMN module)
     - Risk assessment específico de IA (agregado)
   - **Roles**: admin, governance_manager, compliance_officer, auditor
   - **Nota**: Agrega datos, no duplica pantallas de otros módulos

3. **Compliance Status by Project** (`/projects/compliance-status`)
   - **Propósito**: Vista agregada de compliance por proyecto (NO duplica Compliance module)
   - **Contenido**:
     - Estado agregado de FRIA (link a `/governance/compliance/fria/projects`)
     - Estado agregado de EU Registration (link a `/governance/compliance/eu-registration`)
     - Estado agregado de Post-Market Monitoring (link a `/governance/compliance/post-market-monitoring`)
     - Estado agregado de Conformity (link a `/governance/compliance/conformity-declaration/projects`)
     - Estado agregado de HITL (link a `/governance/compliance/hitl-supervision`)
     - Estado agregado de Classification (link a `/governance/compliance/classification/projects`)
   - **Roles**: compliance_officer, governance_manager, auditor
   - **Nota**: Solo muestra estado agregado, no duplica funcionalidad de Compliance

4. **Data Lineage Overview** (`/projects/data-lineage-overview`)
   - **Propósito**: Vista agregada de trazabilidad por proyecto (NO duplica Traceability completo)
   - **Contenido**:
     - Resumen de datasets utilizados (link a Training module)
     - Resumen de modelos entrenados/desplegados (link a Models/Serving modules)
     - Resumen de versiones (link a Versions)
     - Resumen de artefactos (link a Artifacts)
     - Link a trazabilidad completa: `/governance/compliance/traceability?projectId={id}`
   - **Roles**: admin, governance_manager, compliance_officer, developer
   - **Nota**: Vista resumida, link a Traceability completo en Compliance

5. **Model Performance Overview** (`/projects/model-performance-overview`)
   - **Propósito**: Vista agregada de performance de modelos por proyecto
   - **Contenido**:
     - Métricas agregadas (accuracy, precision, recall) por modelo
     - Bias detection results agregados
     - Explainability scores agregados
     - Links a detalles en Models module
   - **Roles**: admin, governance_manager, data_scientist, developer
   - **Nota**: Agrega métricas, links a detalles en Models module

### 🟡 Importantes (Media Prioridad)

6. **Risk Assessment (IA-specific)** (`/projects/ai-risk-assessment`)
   - **Actual**: Risk Assessment genérico
   - **Propuesta**: Enfocar en riesgos específicos de IA (agregado por proyecto)
   - **Contenido**:
     - Bias risks agregados
     - Security risks (model poisoning, adversarial attacks)
     - Compliance risks (GDPR, AI Act) - link a Compliance module
     - Ethical risks
     - Performance degradation risks
   - **Acción**: Mejorar la pantalla existente para agregar riesgos de IA

7. **Approval Workflows Overview** (`/projects/approval-workflows-overview`)
   - **Propósito**: Vista agregada de workflows de aprobación por proyecto
   - **Contenido**:
     - Model approvals pendientes (link a BPMN/Models)
     - Agent approvals pendientes (link a BPMN/Agents)
     - Compliance approvals (link a BPMN/Compliance)
     - BPMN tasks relacionadas (link a `/bpmn/task-inbox?projectId={id}`)
   - **Roles**: governance_manager, compliance_officer, auditor
   - **Nota**: Vista agregada, links a BPMN module

8. **ODS Impact** (`/projects/ods-impact`)
   - **Propósito**: Impacto en Objetivos de Desarrollo Sostenible (cumplimiento Y gobierno)
   - **Contenido**:
     - ODS relacionados con el proyecto
     - Métricas de impacto
     - Reportes de sostenibilidad
   - **Roles**: admin, governance_manager, compliance_officer
   - **Nota**: ODS es cumplimiento Y gobierno, por lo que está en Projects

9. **External Tools Sync** (`/projects/external-sync`)
   - **Propósito**: Sincronización con herramientas externas
   - **Contenido**:
     - Jira issues sincronizados
     - ServiceNow tickets sincronizados
     - Estado de sincronización
     - Configuración de mapeo de datos
   - **Roles**: admin, project_manager

### 🟢 Opcionales (Baja Prioridad)

10. **Technology Stack** (`/projects/stacks/list`)
    - **Actual**: Stacks genéricos
    - **Propuesta**: Enfocar en stacks de IA (ML frameworks, infraestructura)
    - **Acción**: Mantener pero adaptar

11. **Licenses** (`/projects/licenses/list`)
    - **Actual**: Licencias genéricas
    - **Propuesta**: Enfocar en licencias de modelos, APIs, software de IA
    - **Acción**: Mantener pero adaptar

---

## 📊 Estructura Propuesta del Menú

### Dashboard
- ✅ Portfolio Dashboard (mantener, adaptar para métricas de IA)

### Core
- ✅ Projects List (mantener)
- ✅ Domains (mantener)
- ✅ Members (mantener - roles en proyectos de IA)
- ✅ Artifacts (mantener - artefactos de IA)
- ✅ Documents (mantener - documentos de compliance)

### Gobierno de IA (Vistas Agregadas - NO duplican otros módulos)
- 🆕 **AI Governance Overview** (NUEVO - agrega datos de Models/Agents)
- 🆕 **Compliance Status by Project** (NUEVO - agrega estado de Compliance module)
- 🆕 **Data Lineage Overview** (NUEVO - vista resumida, link a Traceability completo)
- 🆕 **Model Performance Overview** (NUEVO - agrega métricas, links a Models)
- ⚠️ Risk Assessment (adaptar para IA - agregado por proyecto)
- 🆕 **Approval Workflows Overview** (NUEVO - agrega datos de BPMN)
- 🆕 **ODS Impact** (NUEVO - cumplimiento Y gobierno)

### Integraciones
- 🆕 **Integrations Dashboard** (NUEVO)
- 🆕 **External Tools Sync** (NUEVO)

### Recursos y Costos (Adaptados)
- ⚠️ Resource Allocation (adaptar para recursos de IA)
- ⚠️ Resource Consumption (adaptar para consumo de IA)
- ⚠️ Cost Breakdown (adaptar para costos de IA)
- ⚠️ ROI Analysis (adaptar para ROI de IA)

### Tecnología (Adaptados)
- ⚠️ Stacks (adaptar para stacks de IA)
- ⚠️ Technologies (adaptar para tecnologías de IA)
- ⚠️ Tokens (mantener - tokens de APIs)
- ⚠️ Versions (mantener - versiones de modelos/artefactos)
- ⚠️ Licenses (adaptar para licencias de IA)

### Eliminar
- ❌ Time Tracking
- ❌ Tasks
- ❌ Invoices
- ❌ Billing Status
- ❌ Billing Details
- ❌ Timeline Gantt
- ❌ Client Profitability
- ❌ Invoice Aging Report
- ❌ Cost Estimators
- ❌ Requirements
- ❌ Financial Summary (o adaptar)

---

## 🎯 Plan de Acción

### Fase 1: Limpieza (Inmediato)
1. Eliminar pantallas de gestión tradicional
2. Documentar qué se elimina y por qué

### Fase 2: Adaptación (Corto Plazo)
1. Adaptar Resource Allocation para recursos de IA
2. Adaptar Cost Breakdown para costos de IA
3. Adaptar Risk Assessment para riesgos de IA
4. Adaptar ROI Analysis para ROI de IA

### Fase 3: Nuevas Pantallas (Mediano Plazo)
1. Crear Integrations Dashboard
2. Crear AI Governance Metrics
3. Crear Compliance Tracking
4. Crear Data Lineage & Traceability
5. Crear Model Performance by Project

### Fase 4: Mejoras (Largo Plazo)
1. Crear Approval Workflows
2. Crear ODS Impact
3. Crear External Tools Sync
4. Mejorar integraciones con Jira/ServiceNow

---

## 📝 Notas Adicionales

### Principio Fundamental: Projects como HUB CENTRAL

**Projects NO duplica funcionalidad de otros módulos**. En su lugar:

1. **Agrega información** de otros módulos por proyecto
2. **Proporciona links** a módulos especializados para detalles
3. **Conecta** información dispersa en un solo lugar
4. **Integra** con herramientas externas (Jira, ServiceNow)

### Módulos Existentes (NO duplicar en Projects):

- **Compliance Module** (`/governance/compliance/*`):
  - ✅ Classification
  - ✅ FRIA Projects
  - ✅ EU Registration
  - ✅ Post-Market Monitoring
  - ✅ Technical Docs
  - ✅ HITL Supervision
  - ✅ Conformity Declaration
  - ✅ QMS
  - ✅ Traceability (completo)
  - ✅ Prohibited Systems
  - ✅ Immutable Logs

- **Models Module**: Gestión completa de modelos
- **Agents Module**: Gestión completa de agentes
- **BPMN Module**: Workflows de aprobación completos
- **Training Module**: Entrenamiento de modelos

### ODS y Telemetría:

- **ODS Impact**: Está en Projects porque es cumplimiento Y gobierno
- **Telemetría**: Está en Compliance porque es cumplimiento Y gobierno

### Integraciones:

- El sistema debe ser el "single source of truth" para gobierno
- Sincronizar con herramientas externas para gestión operativa
- Projects muestra estado de integraciones y datos sincronizados

### Trazabilidad:

- Traceability completo está en Compliance module
- Projects muestra vista resumida y link a trazabilidad completa
