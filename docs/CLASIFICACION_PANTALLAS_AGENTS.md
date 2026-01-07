# Clasificación de Pantallas del Módulo Agents: Desarrollo vs. Gobierno

Este documento clasifica todas las pantallas del módulo de Agents según su propósito funcional: **Desarrollo de IA** o **Gobierno y Cumplimiento**.

## Criterios de Clasificación

### 🛠️ **DESARROLLO DE IA**
Pantallas orientadas a la construcción, configuración, despliegue y operación técnica de agentes:
- Registro y creación de agentes
- Despliegue técnico
- Configuración de herramientas
- Gestión de versiones técnicas
- Monitoreo operacional
- Aprendizaje y entrenamiento
- Ejecución de workflows técnicos

### 🛡️ **GOBIERNO Y CUMPLIMIENTO**
Pantallas orientadas a la gobernanza, cumplimiento normativo, auditoría y control:
- Aprobaciones y autorizaciones
- Cumplimiento normativo
- Auditoría y transparencia
- Detección de sesgos
- Ética y decisiones
- Monitoreo de gobernanza
- Alertas de cumplimiento
- Rollback por razones de gobernanza

---

## Clasificación Completa

### 🛠️ DESARROLLO DE IA (15 pantallas)

| # | Pantalla | Ruta | Propósito | Roles Principales |
|---|----------|------|-----------|-------------------|
| 1 | **Registry (Lista)** | `/governance/agents/registry` | Lista y gestión de agentes registrados | `admin`, `project_manager`, `developer` |
| 2 | **Registry (Crear)** | `/governance/agents/registry/create` | Crear nuevo agente | `admin`, `project_manager`, `developer` |
| 3 | **Registry (Detalle)** | `/governance/agents/registry/[id]` | Ver detalles de un agente | `admin`, `project_manager`, `developer` |
| 4 | **Registry (Editar)** | `/governance/agents/registry/[id]/edit` | Editar configuración de agente | `admin`, `project_manager`, `developer` |
| 5 | **Deployment (Lista)** | `/governance/agents/deployment/overview` | Lista de despliegues de agentes | `admin`, `project_manager`, `developer` |
| 6 | **Deployment (Crear)** | `/governance/agents/deployment/create` | Crear nuevo despliegue | `admin`, `project_manager`, `developer` |
| 7 | **Deployment (Detalle)** | `/governance/agents/deployment/[id]` | Ver detalles de despliegue | `admin`, `project_manager`, `developer` |
| 8 | **Deployment (Editar)** | `/governance/agents/deployment/[id]/edit` | Editar configuración de despliegue | `admin`, `project_manager`, `developer` |
| 9 | **Tools** | `/governance/agents/tools/overview` | Gestión de herramientas del agente | `admin`, `project_manager`, `developer` |
| 10 | **Versioning** | `/governance/agents/versioning/overview` | Control de versiones técnicas | `admin`, `project_manager`, `developer` |
| 11 | **Learning** | `/governance/agents/learning/overview` | Gestión de aprendizaje y entrenamiento | `admin`, `project_manager`, `developer` |
| 12 | **Workflow Execution** | `/governance/agents/workflow/execution-overview` | Ejecución de workflows técnicos | `admin`, `project_manager`, `developer` |
| 13 | **Monitoring (Dashboard)** | `/governance/agents/monitoring/dashboard` | Dashboard operacional de monitoreo | `admin`, `project_manager` |
| 14 | **Monitoring (Overview)** | `/governance/agents/monitoring/overview` | Vista general de monitoreo operacional | `admin`, `project_manager` |
| 15 | **Monitoring (Health)** | `/governance/agents/monitoring/health-overview` | Estado de salud de agentes | `admin`, `project_manager` |

---

### 🛡️ GOBIERNO Y CUMPLIMIENTO (13 pantallas)

| # | Pantalla | Ruta | Propósito | Roles Principales |
|---|----------|------|-----------|-------------------|
| 1 | **Approval (Lista)** | `/governance/agents/approval/overview` | Lista de aprobaciones pendientes | `admin`, `project_manager`, `compliance_officer`, `governance_manager` |
| 2 | **Approval (Crear)** | `/governance/agents/approval/create` | Crear solicitud de aprobación | `admin`, `project_manager`, `compliance_officer`, `governance_manager` |
| 3 | **Compliance** | `/governance/agents/compliance/overview` | Cumplimiento normativo (GDPR, HIPAA, etc.) | `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor` |
| 4 | **Governance** | `/governance/agents/governance/overview` | Políticas y gobernanza de agentes | `admin`, `project_manager`, `compliance_officer`, `governance_manager` |
| 5 | **Ethics** | `/governance/agents/ethics/overview` | Evaluaciones éticas de agentes | `admin`, `project_manager`, `compliance_officer`, `governance_manager` |
| 6 | **Bias Detection** | `/governance/agents/bias-detection/overview` | Detección de sesgos en agentes | `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor` |
| 7 | **Transparency** | `/governance/agents/transparency/overview` | Transparencia y explicabilidad | `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor` |
| 8 | **Decisions** | `/governance/agents/decisions/overview` | Auditoría de decisiones tomadas | `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor` |
| 9 | **Rollback** | `/governance/agents/rollback/overview` | Rollback por razones de gobernanza | `admin`, `project_manager`, `compliance_officer`, `governance_manager` |
| 10 | **Alerts** | `/governance/agents/alerts/overview` | Alertas de cumplimiento y gobernanza | `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor` |
| 11 | **Interactions (Overview)** | `/governance/agents/interactions/overview` | Auditoría de interacciones | `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor` |
| 12 | **Interactions (Collaboration)** | `/governance/agents/interactions/collaboration-overview` | Auditoría de colaboraciones | `admin`, `project_manager`, `compliance_officer`, `governance_manager` |
| 13 | **Interactions (Communication)** | `/governance/agents/interactions/communication-overview` | Auditoría de comunicaciones | `admin`, `project_manager`, `compliance_officer`, `governance_manager` |

---

## Resumen Estadístico

- **Total de pantallas**: 28
- **Desarrollo de IA**: 15 pantallas (53.6%)
- **Gobierno y Cumplimiento**: 13 pantallas (46.4%)

---

## Análisis por Funcionalidad

### Funcionalidades de Desarrollo
1. **Registro y Gestión de Agentes** (4 pantallas)
   - Lista, crear, ver detalle, editar
2. **Despliegue Técnico** (4 pantallas)
   - Lista, crear, ver detalle, editar
3. **Configuración Técnica** (3 pantallas)
   - Tools, Versioning, Learning
4. **Operación y Monitoreo** (3 pantallas)
   - Dashboard, Overview, Health
5. **Workflows Técnicos** (1 pantalla)
   - Workflow Execution

### Funcionalidades de Gobierno
1. **Aprobaciones** (2 pantallas)
   - Lista, crear
2. **Cumplimiento Normativo** (2 pantallas)
   - Compliance, Governance
3. **Auditoría y Transparencia** (4 pantallas)
   - Ethics, Bias Detection, Transparency, Decisions
4. **Control Operacional** (2 pantallas)
   - Rollback, Alerts
5. **Auditoría de Interacciones** (3 pantallas)
   - Overview, Collaboration, Communication

---

## Recomendaciones de Reorganización

### Opción 1: Separación por Rutas
- **Desarrollo**: `/agents/development/...` o `/agents/dev/...`
- **Gobierno**: `/agents/governance/...` o `/governance/agents/...`

### Opción 2: Separación por Módulos
- **Módulo "Agents" (Desarrollo)**: Todas las pantallas de desarrollo
- **Módulo "Agents Governance" (Gobierno)**: Todas las pantallas de gobierno

### Opción 3: Mantener Estructura Actual con Categorización
- Mantener `/governance/agents/...` pero categorizar visualmente en el menú
- Agrupar por secciones: "Development" y "Governance"

---

## Notas Adicionales

1. **Pantallas Híbridas**: Algunas pantallas como `Monitoring Dashboard` pueden tener aspectos tanto de desarrollo como de gobierno. Se clasificaron según su propósito principal.

2. **Roles Superpuestos**: Muchas pantallas tienen roles superpuestos (ej: `admin`, `project_manager` aparecen en ambas categorías). Esto es normal ya que estos roles tienen acceso a todo.

3. **Interactions**: Las pantallas de interacciones se clasificaron como gobierno porque su propósito principal es la auditoría y el cumplimiento, no la construcción técnica.

4. **Monitoring**: Se clasificó como desarrollo porque su enfoque es operacional (salud, rendimiento), aunque también puede usarse para gobernanza.

---

## Próximos Pasos

1. Revisar y validar esta clasificación con el equipo
2. Decidir la estrategia de reorganización (rutas, módulos, o categorización visual)
3. Actualizar la configuración de módulos en `app/config/modules.ts`
4. Actualizar las rutas y navegación según la decisión tomada
5. Actualizar la documentación de arquitectura
