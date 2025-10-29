# 🗂️ ARQUITECTURA DE NAVEGACIÓN SSO - CODEFLOWX GOVERN

**Fecha:** Octubre 29, 2025  
**Versión:** 1.0  
**Propósito:** Documento maestro de la jerarquía completa de navegación SSO

---

## 📋 ESTRUCTURA JERÁRQUICA SSO

### **Niveles de Navegación:**

```
📱 NIVEL 1: SSO APPLICATION
   └── 🎯 Dashboard de Aplicación + Icono
       │
       ├── 📊 NIVEL 2: SSO MENU (Seguridad, Configuración, etc.)
       │   └── 🎯 Dashboard de Menú + Icono (opcional)
       │       │
       │       └── 📝 NIVEL 3: MENU ITEM (Enlaces a ZUL o BPMN)
       │           └── 🔐 Permisos asignados por ROL
```

---

## 🤖 SSO APPLICATION: AGENTS

**Nombre SSO Application:** `Agents`  
**Dashboard Principal:** `/platform/agents/monitoring/dashboard`  
**Archivo ZUL:** `console/platform/agents/monitoring/dashboard.zul`  
**Icono Sugerido:** 🤖 Robot / fa-robot  
**Descripción:** Gestión completa de agentes de IA

---

### 📊 SSO MENU 1: GESTIÓN DE AGENTES

**Nombre SSO Menu:** `Gestión de Agentes`  
**Dashboard (opcional):** `/platform/agents/overview/page`  
**Icono Sugerido:** 📋 fa-list-check  
**Descripción:** Gestión CRUD de agentes y sus dominios

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 1.1 | Listado de Agentes | ZUL | `/platform/agents/overview/page` | `console/platform/agents/overview/page.zul` | Admin, Manager, Developer, Viewer |
| 1.2 | Crear Agente | ZUL | `/platform/agents/create/page` | `console/platform/agents/create/page.zul` | Admin, Manager, Developer |
| 1.3 | Listado de Dominios | ZUL | `/platform/agents/registry/domain-overview` | `console/platform/agents/registry/domain-overview.zul` | Admin, Manager, Developer, Viewer |
| 1.4 | Gestionar Dominio | ZUL | `/platform/agents/registry/domain` | `console/platform/agents/registry/domain.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 2: OPERACIONES

**Nombre SSO Menu:** `Operaciones`  
**Dashboard (opcional):** `/platform/agents/deployment/overview`  
**Icono Sugerido:** ⚙️ fa-gears  
**Descripción:** Despliegues, versionado y rollback de agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 2.1 | Listado de Despliegues | ZUL | `/platform/agents/deployment/overview` | `console/platform/agents/deployment/overview.zul` | Admin, Manager, Developer, Viewer |
| 2.2 | Estado de Despliegues | ZUL | `/platform/agents/deployment/status` | `console/platform/agents/deployment/status.zul` | Admin, Manager, Developer, Viewer |
| 2.3 | Nuevo Despliegue | ZUL | `/platform/agents/deployment/page` | `console/platform/agents/deployment/page.zul` | Admin, Manager, Developer |
| 2.4 | Listado de Versiones | ZUL | `/platform/agents/versioning/overview` | `console/platform/agents/versioning/overview.zul` | Admin, Manager, Developer, Viewer |
| 2.5 | Gestionar Versión | ZUL | `/platform/agents/versioning/page` | `console/platform/agents/versioning/page.zul` | Admin, Manager, Developer |
| 2.6 | Listado de Rollbacks | ZUL | `/platform/agents/rollback/overview` | `console/platform/agents/rollback/overview.zul` | Admin, Manager, Developer, Viewer |
| 2.7 | Ejecutar Rollback | ZUL | `/platform/agents/rollback/page` | `console/platform/agents/rollback/page.zul` | Admin, Manager |

---

### 📊 SSO MENU 3: MONITOREO

**Nombre SSO Menu:** `Monitoreo y Performance`  
**Dashboard:** `/platform/agents/monitoring/dashboard` ⭐  
**Icono Sugerido:** 📈 fa-chart-line  
**Descripción:** Monitoreo en tiempo real de agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 3.1 | Dashboard Principal | ZUL | `/platform/agents/monitoring/dashboard` | `console/platform/agents/monitoring/dashboard.zul` | Admin, Manager, Developer, Viewer |
| 3.2 | Overview General | ZUL | `/platform/agents/monitoring/overview` | `console/platform/agents/monitoring/overview.zul` | Admin, Manager, Developer, Viewer |
| 3.3 | Listado de Salud | ZUL | `/platform/agents/monitoring/health-overview` | `console/platform/agents/monitoring/health-overview.zul` | Admin, Manager, Developer, Viewer |
| 3.4 | Detalle de Salud | ZUL | `/platform/agents/monitoring/health` | `console/platform/agents/monitoring/health.zul` | Admin, Manager, Developer, Viewer |
| 3.5 | Consumo de Recursos | ZUL | `/platform/agents/monitoring/resources` | `console/platform/agents/monitoring/resources.zul` | Admin, Manager, Developer, Viewer |
| 3.6 | Análisis de Errores | ZUL | `/platform/agents/monitoring/errors` | `console/platform/agents/monitoring/errors.zul` | Admin, Manager, Developer, Viewer |
| 3.7 | Configurar Monitoreo | ZUL | `/platform/agents/monitoring/page` | `console/platform/agents/monitoring/page.zul` | Admin, Manager |

---

### 📊 SSO MENU 4: ALERTAS

**Nombre SSO Menu:** `Alertas`  
**Dashboard (opcional):** `/platform/agents/alerts/overview`  
**Icono Sugerido:** 🚨 fa-bell  
**Descripción:** Gestión de alertas de agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 4.1 | Listado de Alertas | ZUL | `/platform/agents/alerts/overview` | `console/platform/agents/alerts/overview.zul` | Admin, Manager, Developer, Viewer |
| 4.2 | Configurar Alerta | ZUL | `/platform/agents/alerts/page` | `console/platform/agents/alerts/page.zul` | Admin, Manager |

---

### 📊 SSO MENU 5: INTERACCIONES

**Nombre SSO Menu:** `Interacciones`  
**Dashboard (opcional):** `/platform/agents/interactions/overview`  
**Icono Sugerido:** 🤝 fa-handshake  
**Descripción:** Interacciones, comunicación y colaboración entre agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 5.1 | Listado de Interacciones | ZUL | `/platform/agents/interactions/overview` | `console/platform/agents/interactions/overview.zul` | Admin, Manager, Developer, Viewer |
| 5.2 | Patrones de Interacción | ZUL | `/platform/agents/interactions/patterns` | `console/platform/agents/interactions/patterns.zul` | Admin, Manager, Developer, Viewer |
| 5.3 | Detalle de Interacción | ZUL | `/platform/agents/interactions/page` | `console/platform/agents/interactions/page.zul` | Admin, Manager, Developer |
| 5.4 | Listado de Comunicación | ZUL | `/platform/agents/interactions/communication-overview` | `console/platform/agents/interactions/communication-overview.zul` | Admin, Manager, Developer, Viewer |
| 5.5 | Configurar Comunicación | ZUL | `/platform/agents/interactions/communication` | `console/platform/agents/interactions/communication.zul` | Admin, Manager, Developer |
| 5.6 | Listado de Colaboración | ZUL | `/platform/agents/interactions/collaboration-overview` | `console/platform/agents/interactions/collaboration-overview.zul` | Admin, Manager, Developer, Viewer |
| 5.7 | Configurar Colaboración | ZUL | `/platform/agents/interactions/collaboration` | `console/platform/agents/interactions/collaboration.zul` | Admin, Manager, Developer |
| 5.8 | Red de Colaboración | ZUL | `/platform/agents/interactions/network` | `console/platform/agents/interactions/network.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 6: APRENDIZAJE

**Nombre SSO Menu:** `Aprendizaje y Expertise`  
**Dashboard (opcional):** `/platform/agents/learning/overview`  
**Icono Sugerido:** 🎓 fa-graduation-cap  
**Descripción:** Gestión del conocimiento y expertise de agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 6.1 | Listado de Expertise | ZUL | `/platform/agents/learning/overview` | `console/platform/agents/learning/overview.zul` | Admin, Manager, Developer, Viewer |
| 6.2 | Gestionar Expertise | ZUL | `/platform/agents/learning/page` | `console/platform/agents/learning/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 7: WORKFLOWS

**Nombre SSO Menu:** `Workflows`  
**Dashboard (opcional):** `/platform/agents/workflow/overview`  
**Icono Sugerido:** 🔄 fa-diagram-project  
**Descripción:** Workflows y ejecuciones de agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 7.1 | Listado de Workflows | ZUL | `/platform/agents/workflow/overview` | `console/platform/agents/workflow/overview.zul` | Admin, Manager, Developer, Viewer |
| 7.2 | Crear/Editar Workflow | ZUL | `/platform/agents/workflow/page` | `console/platform/agents/workflow/page.zul` | Admin, Manager, Developer |
| 7.3 | Listado de Ejecuciones | ZUL | `/platform/agents/workflow/execution-overview` | `console/platform/agents/workflow/execution-overview.zul` | Admin, Manager, Developer, Viewer |
| 7.4 | Detalle de Ejecución | ZUL | `/platform/agents/workflow/execution` | `console/platform/agents/workflow/execution.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 8: ÉTICA Y GOBERNANZA

**Nombre SSO Menu:** `Ética y Gobernanza`  
**Dashboard (opcional):** `/platform/agents/ethics/overview`  
**Icono Sugerido:** ⚖️ fa-balance-scale  
**Descripción:** Evaluaciones éticas, detección de sesgo y transparencia

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 8.1 | Listado de Evaluaciones Éticas | ZUL | `/platform/agents/ethics/overview` | `console/platform/agents/ethics/overview.zul` | Admin, Manager, Viewer |
| 8.2 | Nueva Evaluación Ética | ZUL | `/platform/agents/ethics/page` | `console/platform/agents/ethics/page.zul` | Admin, Manager |
| 8.3 | Listado Detección de Sesgo | ZUL | `/platform/agents/bias-detection/overview` | `console/platform/agents/bias-detection/overview.zul` | Admin, Manager, Viewer |
| 8.4 | Analizar Sesgo | ZUL | `/platform/agents/bias-detection/page` | `console/platform/agents/bias-detection/page.zul` | Admin, Manager |
| 8.5 | Listado de Transparencia | ZUL | `/platform/agents/transparency/overview` | `console/platform/agents/transparency/overview.zul` | Admin, Manager, Viewer |
| 8.6 | Configurar Transparencia | ZUL | `/platform/agents/transparency/page` | `console/platform/agents/transparency/page.zul` | Admin, Manager |
| 8.7 | Listado de Gobernanza | ZUL | `/platform/agents/governance/overview` | `console/platform/agents/governance/overview.zul` | Admin, Manager, Viewer |
| 8.8 | Configurar Gobernanza | ZUL | `/platform/agents/governance/page` | `console/platform/agents/governance/page.zul` | Admin, Manager |

---

### 📊 SSO MENU 9: APROBACIONES

**Nombre SSO Menu:** `Aprobaciones y Decisiones`  
**Dashboard (opcional):** `/platform/agents/approval/overview`  
**Icono Sugerido:** ✅ fa-check-circle  
**Descripción:** Aprobaciones y decisiones de agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 9.1 | Listado de Aprobaciones | ZUL | `/platform/agents/approval/overview` | `console/platform/agents/approval/overview.zul` | Admin, Manager, Viewer |
| 9.2 | Procesar Aprobación | ZUL | `/platform/agents/approval/page` | `console/platform/agents/approval/page.zul` | Admin, Manager |
| 9.3 | Listado de Decisiones | ZUL | `/platform/agents/decisions/overview` | `console/platform/agents/decisions/overview.zul` | Admin, Manager, Viewer |
| 9.4 | Nueva/Editar Decisión | ZUL | `/platform/agents/decisions/page` | `console/platform/agents/decisions/page.zul` | Admin, Manager |
| 9.5 | Auditoría de Decisiones | ZUL | `/platform/agents/decisions/audit` | `console/platform/agents/decisions/audit.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 10: COMPLIANCE

**Nombre SSO Menu:** `Compliance`  
**Dashboard (opcional):** `/platform/agents/compliance/status`  
**Icono Sugerido:** ✓ fa-shield-check  
**Descripción:** Estado de cumplimiento de agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 10.1 | Estado de Compliance | ZUL | `/platform/agents/compliance/status` | `console/platform/agents/compliance/status.zul` | Admin, Manager, Viewer |
| 10.2 | Listado de Compliance | ZUL | `/platform/agents/compliance/overview` | `console/platform/agents/compliance/overview.zul` | Admin, Manager, Viewer |
| 10.3 | Configurar Compliance | ZUL | `/platform/agents/compliance/page` | `console/platform/agents/compliance/page.zul` | Admin, Manager |

---

### 📊 SSO MENU 11: HERRAMIENTAS

**Nombre SSO Menu:** `Herramientas`  
**Dashboard (opcional):** `/platform/agents/tools/overview`  
**Icono Sugerido:** 🛠️ fa-tools  
**Descripción:** Herramientas disponibles para agentes

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 11.1 | Listado de Tools | ZUL | `/platform/agents/tools/overview` | `console/platform/agents/tools/overview.zul` | Admin, Manager, Developer, Viewer |
| 11.2 | Nueva Tool | ZUL | `/platform/agents/tools/page` | `console/platform/agents/tools/page.zul` | Admin, Manager, Developer |

---

### 📊 PROCESOS BPMN RELACIONADOS

**Ubicación:** `console/bpmn/`

| Nombre Proceso BPMN | User Task Form | Descripción |
|---------------------|----------------|-------------|
| `agent-approval-process-v1.bpmn` | `agent-approval-human-override-form.zul` | Proceso de aprobación manual con override humano |
| `agent-hitl-sla-reminder-v1.bpmn` | `hitl-sla-reminder-form.zul` | Recordatorio de SLA para Human-in-the-Loop |

---

## 📊 RESUMEN - SSO APPLICATION: AGENTS

**Total de elementos:**
- **SSO Menus:** 11 menús funcionales
- **Menu Items:** 52 pantallas ZUL
- **Procesos BPMN:** 2 procesos con User Tasks

**Estado:** ✅ Completamente documentado

---

---

## 🤖 SSO APPLICATION: MODELS

**Nombre SSO Application:** `Models`  
**Dashboard Principal:** `/platform/models/overview/page`  
**Archivo ZUL:** `console/platform/models/overview/page.zul`  
**Icono Sugerido:** 🤖 Model / fa-brain  
**Descripción:** Gestión completa de modelos de IA, registro, versionado, performance y explicabilidad

---

### 📊 SSO MENU 1: GESTIÓN DE MODELOS

**Nombre SSO Menu:** `Gestión de Modelos`  
**Dashboard:** `/platform/models/overview/page`  
**Icono Sugerido:** 📋 fa-list-check  
**Descripción:** Gestión CRUD de modelos de IA

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 1.1 | Dashboard de Modelos | ZUL | `/platform/models/overview/page` | `console/platform/models/overview/page.zul` | Admin, Manager, Developer, Viewer |
| 1.2 | Resumen de Modelos | ZUL | `/platform/models/overview/summary` | `console/platform/models/overview/summary.zul` | Admin, Manager, Developer, Viewer |
| 1.3 | Crear Modelo | ZUL | `/platform/models/create/page` | `console/platform/models/create/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 2: REGISTRY

**Nombre SSO Menu:** `Registro de Modelos`  
**Dashboard (opcional):** `/platform/models/registry/catalog-overview`  
**Icono Sugerido:** 📚 fa-book  
**Descripción:** Catálogo y registro de modelos, providers, endpoints, capabilities y artifacts

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 2.1 | Listado de Catálogo | ZUL | `/platform/models/registry/catalog-overview` | `console/platform/models/registry/catalog-overview.zul` | Admin, Manager, Developer, Viewer |
| 2.2 | Gestionar Catálogo | ZUL | `/platform/models/registry/catalog` | `console/platform/models/registry/catalog.zul` | Admin, Manager, Developer |
| 2.3 | Listado de Providers | ZUL | `/platform/models/registry/provider-overview` | `console/platform/models/registry/provider-overview.zul` | Admin, Manager, Developer, Viewer |
| 2.4 | Gestionar Provider | ZUL | `/platform/models/registry/provider` | `console/platform/models/registry/provider.zul` | Admin, Manager, Developer |
| 2.5 | Listado de Credenciales | ZUL | `/platform/models/registry/provider-credential-overview` | `console/platform/models/registry/provider-credential-overview.zul` | Admin, Manager |
| 2.6 | Gestionar Credencial | ZUL | `/platform/models/registry/provider-credential` | `console/platform/models/registry/provider-credential.zul` | Admin, Manager |
| 2.7 | Listado de Endpoints | ZUL | `/platform/models/registry/endpoint-overview` | `console/platform/models/registry/endpoint-overview.zul` | Admin, Manager, Developer, Viewer |
| 2.8 | Gestionar Endpoint | ZUL | `/platform/models/registry/endpoint` | `console/platform/models/registry/endpoint.zul` | Admin, Manager, Developer |
| 2.9 | Listado de Capabilities | ZUL | `/platform/models/registry/capability-overview` | `console/platform/models/registry/capability-overview.zul` | Admin, Manager, Developer, Viewer |
| 2.10 | Gestionar Capability | ZUL | `/platform/models/registry/capability` | `console/platform/models/registry/capability.zul` | Admin, Manager, Developer |
| 2.11 | Listado de Artifacts | ZUL | `/platform/models/registry/artifact-overview` | `console/platform/models/registry/artifact-overview.zul` | Admin, Manager, Developer, Viewer |
| 2.12 | Gestionar Artifact | ZUL | `/platform/models/registry/artifact` | `console/platform/models/registry/artifact.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 3: PERFORMANCE

**Nombre SSO Menu:** `Performance de Modelos`  
**Dashboard:** `/platform/models/performance/overview`  
**Icono Sugerido:** 📈 fa-chart-line  
**Descripción:** Monitoreo de performance, métricas y uso de modelos

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 3.1 | Dashboard de Performance | ZUL | `/platform/models/performance/overview` | `console/platform/models/performance/overview.zul` | Admin, Manager, Developer, Viewer |
| 3.2 | Configurar Performance | ZUL | `/platform/models/performance/page` | `console/platform/models/performance/page.zul` | Admin, Manager, Developer |
| 3.3 | Resumen de Métricas | ZUL | `/platform/models/performance/metrics-summary` | `console/platform/models/performance/metrics-summary.zul` | Admin, Manager, Developer, Viewer |
| 3.4 | Listado de Uso | ZUL | `/platform/models/performance/usage-overview` | `console/platform/models/performance/usage-overview.zul` | Admin, Manager, Developer, Viewer |
| 3.5 | Detalle de Uso | ZUL | `/platform/models/performance/usage` | `console/platform/models/performance/usage.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 4: VERSIONADO

**Nombre SSO Menu:** `Versionado de Modelos`  
**Dashboard (opcional):** `/platform/models/versioning/overview`  
**Icono Sugerido:** 🔄 fa-code-branch  
**Descripción:** Gestión de versiones de modelos

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 4.1 | Listado de Versiones | ZUL | `/platform/models/versioning/overview` | `console/platform/models/versioning/overview.zul` | Admin, Manager, Developer, Viewer |
| 4.2 | Gestionar Versión | ZUL | `/platform/models/versioning/page` | `console/platform/models/versioning/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 5: DEPENDENCIAS

**Nombre SSO Menu:** `Dependencias`  
**Dashboard (opcional):** `/platform/models/dependencies/overview`  
**Icono Sugerido:** 🔗 fa-link  
**Descripción:** Gestión de dependencias entre modelos

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 5.1 | Listado de Dependencias | ZUL | `/platform/models/dependencies/overview` | `console/platform/models/dependencies/overview.zul` | Admin, Manager, Developer, Viewer |
| 5.2 | Gestionar Dependencias | ZUL | `/platform/models/dependencies/page` | `console/platform/models/dependencies/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 6: EXPLICABILIDAD

**Nombre SSO Menu:** `Explicabilidad (XAI)`  
**Dashboard (opcional):** `/platform/models/explainability/overview`  
**Icono Sugerido:** 💡 fa-lightbulb  
**Descripción:** Análisis de explicabilidad e interpretabilidad de modelos

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 6.1 | Listado de Explicabilidad | ZUL | `/platform/models/explainability/overview` | `console/platform/models/explainability/overview.zul` | Admin, Manager, Developer, Viewer |
| 6.2 | Análisis de Explicabilidad | ZUL | `/platform/models/explainability/page` | `console/platform/models/explainability/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 7: ANÁLISIS DE SESGO

**Nombre SSO Menu:** `Análisis de Sesgo`  
**Dashboard (opcional):** `/platform/models/bias-analysis/overview`  
**Icono Sugerido:** ⚖️ fa-balance-scale  
**Descripción:** Detección y análisis de sesgos en modelos

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 7.1 | Listado de Análisis de Sesgo | ZUL | `/platform/models/bias-analysis/overview` | `console/platform/models/bias-analysis/overview.zul` | Admin, Manager, Viewer |
| 7.2 | Realizar Análisis de Sesgo | ZUL | `/platform/models/bias-analysis/page` | `console/platform/models/bias-analysis/page.zul` | Admin, Manager |

---

### 📊 SSO MENU 8: MODELOS GOBIERNO

**Nombre SSO Menu:** `Modelos Gobierno`  
**Dashboard (opcional):** `/gobierno/models/models-overview`  
**Icono Sugerido:** 🏛️ fa-landmark  
**Descripción:** Vista de gobierno corporativo de modelos

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 8.1 | Overview de Gobierno | ZUL | `/gobierno/models/models-overview` | `console/gobierno/models/models-overview.zul` | Admin, Manager, Viewer |
| 8.2 | Detalle de Gobierno | ZUL | `/gobierno/models/models-detail` | `console/gobierno/models/models-detail.zul` | Admin, Manager, Viewer |

---

## 📊 RESUMEN - SSO APPLICATION: MODELS

**Total de elementos:**
- **SSO Menus:** 8 menús funcionales
- **Menu Items:** 30 pantallas ZUL
- **Procesos BPMN:** Ninguno identificado

**Estado:** ✅ Completamente documentado

---

---

## 💬 SSO APPLICATION: PROMPTS

**Nombre SSO Application:** `Prompts`  
**Dashboard Principal:** `/platform/prompts/overview/page`  
**Archivo ZUL:** `console/platform/prompts/overview/page.zul`  
**Icono Sugerido:** 💬 Message / fa-comments  
**Descripción:** Gestión completa de prompts, templates, validación, versionado y optimización

---

### 📊 SSO MENU 1: GESTIÓN DE PROMPTS

**Nombre SSO Menu:** `Gestión de Prompts`  
**Dashboard:** `/platform/prompts/overview/page`  
**Icono Sugerido:** 📋 fa-list-check  
**Descripción:** Gestión CRUD de prompts

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 1.1 | Dashboard de Prompts | ZUL | `/platform/prompts/overview/page` | `console/platform/prompts/overview/page.zul` | Admin, Manager, Developer, Viewer |
| 1.2 | Resumen de Prompts | ZUL | `/platform/prompts/overview/summary` | `console/platform/prompts/overview/summary.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 2: REGISTRY

**Nombre SSO Menu:** `Registro de Prompts`  
**Dashboard (opcional):** N/A  
**Icono Sugerido:** 📚 fa-book  
**Descripción:** Registro y catálogo de prompts

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 2.1 | Gestionar Registry | ZUL | `/platform/prompts/registry/page` | `console/platform/prompts/registry/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 3: TEMPLATES

**Nombre SSO Menu:** `Templates de Prompts`  
**Dashboard (opcional):** N/A  
**Icono Sugerido:** 📄 fa-file-alt  
**Descripción:** Plantillas predefinidas de prompts con métricas y análisis

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 3.1 | Performance de Templates | ZUL | `/platform/prompts/templates/performance` | `console/platform/prompts/templates/performance.zul` | Admin, Manager, Developer, Viewer |
| 3.2 | Optimización de Templates | ZUL | `/platform/prompts/templates/optimization` | `console/platform/prompts/templates/optimization.zul` | Admin, Manager, Developer |
| 3.3 | Análisis de Costos | ZUL | `/platform/prompts/templates/cost-analysis` | `console/platform/prompts/templates/cost-analysis.zul` | Admin, Manager, Viewer |
| 3.4 | Resumen de Métricas | ZUL | `/platform/prompts/templates/metrics-summary` | `console/platform/prompts/templates/metrics-summary.zul` | Admin, Manager, Developer, Viewer |
| 3.5 | Resultados de Tests | ZUL | `/platform/prompts/templates/test-results` | `console/platform/prompts/templates/test-results.zul` | Admin, Manager, Developer, Viewer |
| 3.6 | Estadísticas de Uso | ZUL | `/platform/prompts/templates/usage-statistics` | `console/platform/prompts/templates/usage-statistics.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 4: VALIDACIÓN

**Nombre SSO Menu:** `Validación de Prompts`  
**Dashboard:** `/platform/prompts/validation/overview`  
**Icono Sugerido:** ✅ fa-check-circle  
**Descripción:** Validación y testing de prompts

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 4.1 | Overview de Validación | ZUL | `/platform/prompts/validation/overview` | `console/platform/prompts/validation/overview.zul` | Admin, Manager, Developer, Viewer |
| 4.2 | Configurar Validación | ZUL | `/platform/prompts/validation/page` | `console/platform/prompts/validation/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 5: VERSIONADO

**Nombre SSO Menu:** `Versionado de Prompts`  
**Dashboard:** `/platform/prompts/versioning/overview`  
**Icono Sugerido:** 🔄 fa-code-branch  
**Descripción:** Gestión de versiones e histórico de prompts

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 5.1 | Listado de Versiones | ZUL | `/platform/prompts/versioning/overview` | `console/platform/prompts/versioning/overview.zul` | Admin, Manager, Developer, Viewer |
| 5.2 | Gestionar Versión | ZUL | `/platform/prompts/versioning/page` | `console/platform/prompts/versioning/page.zul` | Admin, Manager, Developer |
| 5.3 | Historial de Cambios | ZUL | `/platform/prompts/versioning/history` | `console/platform/prompts/versioning/history.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 6: PROMPTS GOBIERNO

**Nombre SSO Menu:** `Prompts Gobierno`  
**Dashboard (opcional):** `/gobierno/prompts/prompts-overview`  
**Icono Sugerido:** 🏛️ fa-landmark  
**Descripción:** Vista de gobierno corporativo de prompts

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 6.1 | Overview de Gobierno | ZUL | `/gobierno/prompts/prompts-overview` | `console/gobierno/prompts/prompts-overview.zul` | Admin, Manager, Viewer |
| 6.2 | Detalle de Gobierno | ZUL | `/gobierno/prompts/prompts-detail` | `console/gobierno/prompts/prompts-detail.zul` | Admin, Manager, Viewer |

---

## 📊 RESUMEN - SSO APPLICATION: PROMPTS

**Total de elementos:**
- **SSO Menus:** 6 menús funcionales
- **Menu Items:** 16 pantallas ZUL
- **Procesos BPMN:** Ninguno identificado

**Estado:** ✅ Completamente documentado

---

---

## 🔍 SSO APPLICATION: RAG

**Nombre SSO Application:** `RAG`  
**Dashboard Principal:** `/platform/rag/overview/page`  
**Archivo ZUL:** `console/platform/rag/overview/page.zul`  
**Icono Sugerido:** 🔍 Search / fa-search  
**Descripción:** Gestión completa de sistemas RAG (Retrieval Augmented Generation), data sources, embeddings, calidad y monitoreo

---

### 📊 SSO MENU 1: GESTIÓN DE RAG

**Nombre SSO Menu:** `Gestión de RAG`  
**Dashboard:** `/platform/rag/overview/page`  
**Icono Sugerido:** 📋 fa-list-check  
**Descripción:** Gestión general de sistemas RAG

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 1.1 | Dashboard de RAG | ZUL | `/platform/rag/overview/page` | `console/platform/rag/overview/page.zul` | Admin, Manager, Developer, Viewer |
| 1.2 | Resumen de RAG | ZUL | `/platform/rag/overview/summary` | `console/platform/rag/overview/summary.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 2: DATA SOURCES

**Nombre SSO Menu:** `Fuentes de Datos`  
**Dashboard:** `/platform/rag/data-sources/overview`  
**Icono Sugerido:** 📂 fa-folder-open  
**Descripción:** Gestión de fuentes de datos para RAG

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 2.1 | Listado de Data Sources | ZUL | `/platform/rag/data-sources/overview` | `console/platform/rag/data-sources/overview.zul` | Admin, Manager, Developer, Viewer |
| 2.2 | Gestionar Data Source | ZUL | `/platform/rag/data-sources/page` | `console/platform/rag/data-sources/page.zul` | Admin, Manager, Developer |
| 2.3 | Progreso de Embeddings | ZUL | `/platform/rag/data-sources/embedding-progress` | `console/platform/rag/data-sources/embedding-progress.zul` | Admin, Manager, Developer, Viewer |
| 2.4 | Análisis de Cobertura | ZUL | `/platform/rag/data-sources/coverage-analysis` | `console/platform/rag/data-sources/coverage-analysis.zul` | Admin, Manager, Developer, Viewer |
| 2.5 | Estadísticas | ZUL | `/platform/rag/data-sources/statistics` | `console/platform/rag/data-sources/statistics.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 3: REGISTRY

**Nombre SSO Menu:** `Registro de RAG`  
**Dashboard (opcional):** N/A  
**Icono Sugerido:** 📚 fa-book  
**Descripción:** Registro y catálogo de sistemas RAG

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 3.1 | Gestionar Registry | ZUL | `/platform/rag/registry/page` | `console/platform/rag/registry/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 4: QUALITY CONTROL

**Nombre SSO Menu:** `Control de Calidad`  
**Dashboard (opcional):** N/A  
**Icono Sugerido:** ✓ fa-check-double  
**Descripción:** Control de calidad y evaluación de retrieval

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 4.1 | Calidad de Retrieval | ZUL | `/platform/rag/quality-control/retrieval-quality` | `console/platform/rag/quality-control/retrieval-quality.zul` | Admin, Manager, Developer, Viewer |
| 4.2 | Distribución de Chunks | ZUL | `/platform/rag/quality-control/chunk-distribution` | `console/platform/rag/quality-control/chunk-distribution.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 5: MONITORING

**Nombre SSO Menu:** `Monitoreo de RAG`  
**Dashboard:** `/platform/rag/monitoring/health-dashboard`  
**Icono Sugerido:** 📈 fa-chart-line  
**Descripción:** Monitoreo de salud, métricas y uso de sistemas RAG

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 5.1 | Dashboard de Salud | ZUL | `/platform/rag/monitoring/health-dashboard` | `console/platform/rag/monitoring/health-dashboard.zul` | Admin, Manager, Developer, Viewer |
| 5.2 | Resumen de Métricas | ZUL | `/platform/rag/monitoring/metrics-summary` | `console/platform/rag/monitoring/metrics-summary.zul` | Admin, Manager, Developer, Viewer |
| 5.3 | Uso por Agente | ZUL | `/platform/rag/monitoring/usage-by-agent` | `console/platform/rag/monitoring/usage-by-agent.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 6: VERSIONADO

**Nombre SSO Menu:** `Versionado de RAG`  
**Dashboard:** `/platform/rag/versioning/overview`  
**Icono Sugerido:** 🔄 fa-code-branch  
**Descripción:** Gestión de versiones de sistemas RAG

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 6.1 | Listado de Versiones | ZUL | `/platform/rag/versioning/overview` | `console/platform/rag/versioning/overview.zul` | Admin, Manager, Developer, Viewer |
| 6.2 | Gestionar Versión | ZUL | `/platform/rag/versioning/page` | `console/platform/rag/versioning/page.zul` | Admin, Manager, Developer |

---

### 📊 SSO MENU 7: RAG GOBIERNO

**Nombre SSO Menu:** `RAG Gobierno`  
**Dashboard (opcional):** `/gobierno/rag/rag-systems-overview`  
**Icono Sugerido:** 🏛️ fa-landmark  
**Descripción:** Vista de gobierno corporativo de sistemas RAG

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 7.1 | Overview de Gobierno | ZUL | `/gobierno/rag/rag-systems-overview` | `console/gobierno/rag/rag-systems-overview.zul` | Admin, Manager, Viewer |
| 7.2 | Detalle de Gobierno | ZUL | `/gobierno/rag/rag-systems-detail` | `console/gobierno/rag/rag-systems-detail.zul` | Admin, Manager, Viewer |

---

## 📊 RESUMEN - SSO APPLICATION: RAG

**Total de elementos:**
- **SSO Menus:** 7 menús funcionales
- **Menu Items:** 17 pantallas ZUL
- **Procesos BPMN:** Ninguno identificado

**Estado:** ✅ Completamente documentado

---

---

## 🔌 SSO APPLICATION: PROVIDERS

**Nombre SSO Application:** `Providers`  
**Dashboard Principal:** `/platform/providers/providers-overview-overview`  
**Archivo ZUL:** `console/platform/providers/providers-overview-overview.zul`  
**Icono Sugerido:** 🔌 Plug / fa-plug  
**Descripción:** Gestión de proveedores de IA (OpenAI, Azure, Anthropic, etc.)

---

### 📊 SSO MENU 1: GESTIÓN DE PROVIDERS

**Nombre SSO Menu:** `Gestión de Providers`  
**Dashboard:** `/platform/providers/providers-overview-overview`  
**Icono Sugerido:** 📋 fa-list-check  
**Descripción:** Gestión de proveedores de IA y sus métricas

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 1.1 | Dashboard de Providers | ZUL | `/platform/providers/providers-overview-overview` | `console/platform/providers/providers-overview-overview.zul` | Admin, Manager, Developer, Viewer |
| 1.2 | Resumen de Métricas | ZUL | `/platform/providers/providers-metrics-summary-overview` | `console/platform/providers/providers-metrics-summary-overview.zul` | Admin, Manager, Developer, Viewer |

---

### 📊 SSO MENU 2: PROVIDERS GOBIERNO

**Nombre SSO Menu:** `Providers Gobierno`  
**Dashboard (opcional):** `/gobierno/providers/providers-overview`  
**Icono Sugerido:** 🏛️ fa-landmark  
**Descripción:** Vista de gobierno corporativo de providers

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 2.1 | Overview de Gobierno | ZUL | `/gobierno/providers/providers-overview` | `console/gobierno/providers/providers-overview.zul` | Admin, Manager, Viewer |
| 2.2 | Detalle de Gobierno | ZUL | `/gobierno/providers/providers-detail` | `console/gobierno/providers/providers-detail.zul` | Admin, Manager, Viewer |

---

## 📊 RESUMEN - SSO APPLICATION: PROVIDERS

**Total de elementos:**
- **SSO Menus:** 2 menús funcionales
- **Menu Items:** 4 pantallas ZUL
- **Procesos BPMN:** Ninguno identificado

**Estado:** ✅ Completamente documentado

---

---

## ⚙️ SSO APPLICATION: CORE

**Nombre SSO Application:** `Core`  
**Dashboard Principal:** `/gobierno/core/admin-dashboard`  
**Archivo ZUL:** `console/gobierno/core/admin-dashboard.zul`  
**Icono Sugerido:** ⚙️ Cog / fa-cog  
**Descripción:** Gestión de usuarios, roles, permisos, seguridad y configuración del sistema

---

### 📊 SSO MENU 1: ADMINISTRACIÓN

**Nombre SSO Menu:** `Administración`  
**Dashboard:** `/gobierno/core/admin-dashboard`  
**Icono Sugerido:** 👨‍💼 fa-user-shield  
**Descripción:** Dashboard principal de administración del sistema

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 1.1 | Dashboard Admin | ZUL | `/gobierno/core/admin-dashboard` | `console/gobierno/core/admin-dashboard.zul` | Admin |
| 1.2 | Resumen Dashboard | ZUL | `/platform/core/admin-dashboard-summary-overview` | `console/platform/core/admin-dashboard-summary-overview.zul` | Admin |

---

### 📊 SSO MENU 2: USUARIOS

**Nombre SSO Menu:** `Gestión de Usuarios`  
**Dashboard:** `/platform/core/user-overview`  
**Icono Sugerido:** 👥 fa-users  
**Descripción:** Gestión CRUD de usuarios

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 2.1 | Listado de Usuarios (Gobierno) | ZUL | `/gobierno/core/users` | `console/gobierno/core/users.zul` | Admin |
| 2.2 | Listado de Usuarios | ZUL | `/platform/core/user-overview` | `console/platform/core/user-overview.zul` | Admin |
| 2.3 | Detalle de Usuario | ZUL | `/platform/core/user-detail` | `console/platform/core/user-detail.zul` | Admin |

---

### 📊 SSO MENU 3: ROLES Y PERMISOS

**Nombre SSO Menu:** `Roles y Permisos`  
**Dashboard:** `/platform/core/role-overview`  
**Icono Sugerido:** 🔐 fa-shield-alt  
**Descripción:** Gestión de roles y permisos del sistema

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 3.1 | Gestión de Roles (Gobierno) | ZUL | `/gobierno/core/roles` | `console/gobierno/core/roles.zul` | Admin |
| 3.2 | Listado de Roles | ZUL | `/platform/core/role-overview` | `console/platform/core/role-overview.zul` | Admin |
| 3.3 | Detalle de Rol | ZUL | `/platform/core/role-detail` | `console/platform/core/role-detail.zul` | Admin |
| 3.4 | Gestión de Permisos (Gobierno) | ZUL | `/gobierno/core/permissions` | `console/gobierno/core/permissions.zul` | Admin |
| 3.5 | Listado de Permisos | ZUL | `/platform/core/permission-overview` | `console/platform/core/permission-overview.zul` | Admin |
| 3.6 | Detalle de Permiso | ZUL | `/platform/core/permission-detail` | `console/platform/core/permission-detail.zul` | Admin |

---

### 📊 SSO MENU 4: DEPARTAMENTOS

**Nombre SSO Menu:** `Departamentos`  
**Dashboard:** `/platform/core/department-overview`  
**Icono Sugerido:** 🏢 fa-building  
**Descripción:** Gestión de departamentos organizacionales

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 4.1 | Gestión de Departamentos (Gobierno) | ZUL | `/gobierno/core/departments` | `console/gobierno/core/departments.zul` | Admin |
| 4.2 | Listado de Departamentos | ZUL | `/platform/core/department-overview` | `console/platform/core/department-overview.zul` | Admin |
| 4.3 | Detalle de Departamento | ZUL | `/platform/core/department-detail` | `console/platform/core/department-detail.zul` | Admin |

---

### 📊 SSO MENU 5: MENÚS

**Nombre SSO Menu:** `Gestión de Menús`  
**Dashboard:** `/platform/core/menu-overview`  
**Icono Sugerido:** 📋 fa-bars  
**Descripción:** Gestión de menús del sistema

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 5.1 | Gestión de Menús (Gobierno) | ZUL | `/gobierno/core/menus` | `console/gobierno/core/menus.zul` | Admin |
| 5.2 | Listado de Menús | ZUL | `/platform/core/menu-overview` | `console/platform/core/menu-overview.zul` | Admin |
| 5.3 | Detalle de Menú | ZUL | `/platform/core/menu-detail` | `console/platform/core/menu-detail.zul` | Admin |

---

### 📊 SSO MENU 6: SEGURIDAD

**Nombre SSO Menu:** `Seguridad`  
**Dashboard:** `/gobierno/core/security-audit`  
**Icono Sugerido:** 🔒 fa-lock  
**Descripción:** Auditoría y monitoreo de seguridad

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 6.1 | Auditoría de Seguridad (Gobierno) | ZUL | `/gobierno/core/security-audit` | `console/gobierno/core/security-audit.zul` | Admin |
| 6.2 | Resumen de Auditoría | ZUL | `/platform/core/security-audit-summary-overview` | `console/platform/core/security-audit-summary-overview.zul` | Admin |
| 6.3 | Intentos de Login (Gobierno) | ZUL | `/gobierno/core/login-attempts` | `console/gobierno/core/login-attempts.zul` | Admin |
| 6.4 | Listado de Intentos | ZUL | `/platform/core/login-attempt-overview` | `console/platform/core/login-attempt-overview.zul` | Admin |
| 6.5 | Detalle de Intento | ZUL | `/platform/core/login-attempt-detail` | `console/platform/core/login-attempt-detail.zul` | Admin |

---

### 📊 SSO MENU 7: SESIONES

**Nombre SSO Menu:** `Sesiones de Usuario`  
**Dashboard:** `/platform/core/user-session-overview`  
**Icono Sugerido:** 🔑 fa-key  
**Descripción:** Monitoreo y gestión de sesiones activas

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 7.1 | Sesiones de Usuario (Gobierno) | ZUL | `/gobierno/core/user-sessions` | `console/gobierno/core/user-sessions.zul` | Admin |
| 7.2 | Listado de Sesiones | ZUL | `/platform/core/user-session-overview` | `console/platform/core/user-session-overview.zul` | Admin |
| 7.3 | Detalle de Sesión | ZUL | `/platform/core/user-session-detail` | `console/platform/core/user-session-detail.zul` | Admin |

---

### 📊 SSO MENU 8: ACTIVIDAD

**Nombre SSO Menu:** `Actividad de Usuarios`  
**Dashboard:** `/gobierno/core/user-activity`  
**Icono Sugerido:** 📊 fa-chart-bar  
**Descripción:** Monitoreo de actividad y acciones de usuarios

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 8.1 | Actividad de Usuario (Gobierno) | ZUL | `/gobierno/core/user-activity` | `console/gobierno/core/user-activity.zul` | Admin |
| 8.2 | Resumen de Actividad | ZUL | `/platform/core/user-activity-summary-overview` | `console/platform/core/user-activity-summary-overview.zul` | Admin |

---

### 📊 SSO MENU 9: SALUD DEL SISTEMA

**Nombre SSO Menu:** `Salud del Sistema`  
**Dashboard:** `/gobierno/core/system-health`  
**Icono Sugerido:** 💚 fa-heartbeat  
**Descripción:** Monitoreo de salud y estado del sistema

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 9.1 | Salud del Sistema (Gobierno) | ZUL | `/gobierno/core/system-health` | `console/gobierno/core/system-health.zul` | Admin |
| 9.2 | Overview de Salud | ZUL | `/platform/core/system-health-overview-overview` | `console/platform/core/system-health-overview-overview.zul` | Admin |

---

## 📊 RESUMEN - SSO APPLICATION: CORE

**Total de elementos:**
- **SSO Menus:** 9 menús funcionales
- **Menu Items:** 29 pantallas ZUL
- **Procesos BPMN:** Ninguno identificado

**Estado:** ✅ Completamente documentado

---

---

## 🏛️ SSO APPLICATION: GOVERNANCE

**Nombre SSO Application:** `Governance`  
**Dashboard Principal:** `/platform/governance/dashboard/overview`  
**Archivo ZUL:** `console/platform/governance/dashboard/overview.zul`  
**Icono Sugerido:** 🏛️ Landmark / fa-landmark  
**Descripción:** Gobernanza empresarial de IA, compliance, políticas, riesgos, auditoría y seguridad

---

### 📊 SSO MENU 1: DASHBOARD

**Nombre SSO Menu:** `Dashboard de Gobernanza`  
**Dashboard:** `/platform/governance/dashboard/overview`  
**Icono Sugerido:** 📊 fa-tachometer-alt  
**Descripción:** Dashboard principal de gobernanza

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 1.1 | Dashboard Overview | ZUL | `/platform/governance/dashboard/overview` | `console/platform/governance/dashboard/overview.zul` | Admin, Manager, Viewer |
| 1.2 | Resumen Dashboard | ZUL | `/platform/governance/dashboard/summary` | `console/platform/governance/dashboard/summary.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 2: POLÍTICAS

**Nombre SSO Menu:** `Políticas de Gobernanza`  
**Dashboard:** `/platform/governance/policies/overview`  
**Icono Sugerido:** 📜 fa-scroll  
**Descripción:** Gestión de políticas, reglas, evaluaciones y violaciones

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 2.1 | Listado de Políticas | ZUL | `/platform/governance/policies/overview` | `console/platform/governance/policies/overview.zul` | Admin, Manager, Viewer |
| 2.2 | Gestionar Política | ZUL | `/platform/governance/policies/page` | `console/platform/governance/policies/page.zul` | Admin, Manager |
| 2.3 | Listado de Reglas | ZUL | `/platform/governance/policies/rule-overview` | `console/platform/governance/policies/rule-overview.zul` | Admin, Manager, Viewer |
| 2.4 | Gestionar Regla | ZUL | `/platform/governance/policies/rule` | `console/platform/governance/policies/rule.zul` | Admin, Manager |
| 2.5 | Listado de Evaluaciones | ZUL | `/platform/governance/policies/evaluation-overview` | `console/platform/governance/policies/evaluation-overview.zul` | Admin, Manager, Viewer |
| 2.6 | Detalle de Evaluación | ZUL | `/platform/governance/policies/evaluation` | `console/platform/governance/policies/evaluation.zul` | Admin, Manager, Viewer |
| 2.7 | Listado de Violaciones | ZUL | `/platform/governance/policies/violation-overview` | `console/platform/governance/policies/violation-overview.zul` | Admin, Manager, Viewer |
| 2.8 | Detalle de Violación | ZUL | `/platform/governance/policies/violation` | `console/platform/governance/policies/violation.zul` | Admin, Manager |
| 2.9 | Listado Checklist Items | ZUL | `/platform/governance/policies/checklist-item-overview` | `console/platform/governance/policies/checklist-item-overview.zul` | Admin, Manager, Viewer |
| 2.10 | Gestionar Checklist Item | ZUL | `/platform/governance/policies/checklist-item` | `console/platform/governance/policies/checklist-item.zul` | Admin, Manager |
| 2.11 | Listado Validaciones | ZUL | `/platform/governance/policies/validation-config-overview` | `console/platform/governance/policies/validation-config-overview.zul` | Admin, Manager |
| 2.12 | Configurar Validación | ZUL | `/platform/governance/policies/validation-config` | `console/platform/governance/policies/validation-config.zul` | Admin, Manager |

---

### 📊 SSO MENU 3: COMPLIANCE

**Nombre SSO Menu:** `Compliance`  
**Dashboard:** `/platform/governance/compliance/page`  
**Icono Sugerido:** ✓ fa-clipboard-check  
**Descripción:** Gestión de compliance, requerimientos, hallazgos y frameworks regulatorios

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 3.1 | Dashboard Compliance | ZUL | `/platform/governance/compliance/page` | `console/platform/governance/compliance/page.zul` | Admin, Manager, Viewer |
| 3.2 | Assessment | ZUL | `/platform/governance/compliance/assessment` | `console/platform/governance/compliance/assessment.zul` | Admin, Manager |
| 3.3 | Por Framework | ZUL | `/platform/governance/compliance/by-framework` | `console/platform/governance/compliance/by-framework.zul` | Admin, Manager, Viewer |
| 3.4 | Análisis de Gaps | ZUL | `/platform/governance/compliance/gaps-analysis` | `console/platform/governance/compliance/gaps-analysis.zul` | Admin, Manager, Viewer |
| 3.5 | Listado de Requerimientos | ZUL | `/platform/governance/compliance/requirement-overview` | `console/platform/governance/compliance/requirement-overview.zul` | Admin, Manager, Viewer |
| 3.6 | Gestionar Requerimiento | ZUL | `/platform/governance/compliance/requirement` | `console/platform/governance/compliance/requirement.zul` | Admin, Manager |
| 3.7 | Listado de Hallazgos | ZUL | `/platform/governance/compliance/finding-overview` | `console/platform/governance/compliance/finding-overview.zul` | Admin, Manager, Viewer |
| 3.8 | Detalle de Hallazgo | ZUL | `/platform/governance/compliance/finding` | `console/platform/governance/compliance/finding.zul` | Admin, Manager |

---

### 📊 SSO MENU 4: AUDITORÍA

**Nombre SSO Menu:** `Auditoría`  
**Dashboard:** `/platform/governance/audit/log-overview`  
**Icono Sugerido:** 📋 fa-clipboard-list  
**Descripción:** Logs de auditoría y audit trail

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 4.1 | Listado de Logs | ZUL | `/platform/governance/audit/log-overview` | `console/platform/governance/audit/log-overview.zul` | Admin, Manager, Viewer |
| 4.2 | Detalle de Log | ZUL | `/platform/governance/audit/log` | `console/platform/governance/audit/log.zul` | Admin, Manager, Viewer |
| 4.3 | Audit Trail | ZUL | `/platform/governance/audit/trail` | `console/platform/governance/audit/trail.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 5: MÉTRICAS

**Nombre SSO Menu:** `Métricas de Gobernanza`  
**Dashboard:** `/platform/governance/metrics/overview`  
**Icono Sugerido:** 📈 fa-chart-line  
**Descripción:** Métricas y KPIs de gobernanza

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 5.1 | Overview de Métricas | ZUL | `/platform/governance/metrics/overview` | `console/platform/governance/metrics/overview.zul` | Admin, Manager, Viewer |
| 5.2 | Gestionar Métrica | ZUL | `/platform/governance/metrics/page` | `console/platform/governance/metrics/page.zul` | Admin, Manager |
| 5.3 | Resumen de Métricas | ZUL | `/platform/governance/metrics/summary` | `console/platform/governance/metrics/summary.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 6: KPIs

**Nombre SSO Menu:** `KPIs Ejecutivos`  
**Dashboard:** `/platform/governance/kpis/executive`  
**Icono Sugerido:** 🎯 fa-bullseye  
**Descripción:** KPIs para nivel ejecutivo

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 6.1 | KPIs Ejecutivos | ZUL | `/platform/governance/kpis/executive` | `console/platform/governance/kpis/executive.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 7: ANALYTICS

**Nombre SSO Menu:** `Analytics de Gobernanza`  
**Dashboard:** `/platform/governance/analytics/evaluation-trends`  
**Icono Sugerido:** 📊 fa-chart-area  
**Descripción:** Analytics, tendencias y auto-aprobación

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 7.1 | Tendencias de Evaluación | ZUL | `/platform/governance/analytics/evaluation-trends` | `console/platform/governance/analytics/evaluation-trends.zul` | Admin, Manager, Viewer |
| 7.2 | Heatmap de Violaciones | ZUL | `/platform/governance/analytics/violation-heatmap` | `console/platform/governance/analytics/violation-heatmap.zul` | Admin, Manager, Viewer |
| 7.3 | Auto-Aprobación | ZUL | `/platform/governance/analytics/auto-approval` | `console/platform/governance/analytics/auto-approval.zul` | Admin, Manager |

---

### 📊 SSO MENU 8: CALIDAD

**Nombre SSO Menu:** `Control de Calidad`  
**Dashboard:** N/A  
**Icono Sugerido:** ✓ fa-check-double  
**Descripción:** Revisiones éticas y de datasets

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 8.1 | Revisión de Datasets | ZUL | `/platform/governance/quality/dataset-review` | `console/platform/governance/quality/dataset-review.zul` | Admin, Manager, Viewer |
| 8.2 | Gestionar Dataset | ZUL | `/platform/governance/quality/dataset` | `console/platform/governance/quality/dataset.zul` | Admin, Manager |
| 8.3 | Revisión Ética | ZUL | `/platform/governance/quality/ethics-review` | `console/platform/governance/quality/ethics-review.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 9: RIESGOS

**Nombre SSO Menu:** `Gestión de Riesgos`  
**Dashboard:** `/platform/governance/risks/matrix`  
**Icono Sugerido:** ⚠️ fa-exclamation-triangle  
**Descripción:** Matriz de riesgos

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 9.1 | Matriz de Riesgos | ZUL | `/platform/governance/risks/matrix` | `console/platform/governance/risks/matrix.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 10: SEGURIDAD

**Nombre SSO Menu:** `Seguridad de Gobernanza`  
**Dashboard:** `/platform/governance/security/policy-overview`  
**Icono Sugerido:** 🔒 fa-shield-alt  
**Descripción:** Políticas de seguridad, amenazas y métricas

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 10.1 | Listado de Políticas | ZUL | `/platform/governance/security/policy-overview` | `console/platform/governance/security/policy-overview.zul` | Admin, Manager |
| 10.2 | Gestionar Política | ZUL | `/platform/governance/security/policy` | `console/platform/governance/security/policy.zul` | Admin, Manager |
| 10.3 | Listado de Amenazas | ZUL | `/platform/governance/security/threat-overview` | `console/platform/governance/security/threat-overview.zul` | Admin, Manager, Viewer |
| 10.4 | Gestionar Amenaza | ZUL | `/platform/governance/security/threat` | `console/platform/governance/security/threat.zul` | Admin, Manager |
| 10.5 | Listado de Métricas | ZUL | `/platform/governance/security/metric-overview` | `console/platform/governance/security/metric-overview.zul` | Admin, Manager, Viewer |
| 10.6 | Gestionar Métrica | ZUL | `/platform/governance/security/metric` | `console/platform/governance/security/metric.zul` | Admin, Manager |

---

### 📊 SSO MENU 11: REPORTES

**Nombre SSO Menu:** `Reportes de Gobernanza`  
**Dashboard:** N/A  
**Icono Sugerido:** 📄 fa-file-alt  
**Descripción:** Reportes de efectividad

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 11.1 | Reporte de Efectividad | ZUL | `/platform/governance/reports/effectiveness` | `console/platform/governance/reports/effectiveness.zul` | Admin, Manager, Viewer |

---

### 📊 SSO MENU 12: GOVERNANCE GOBIERNO

**Nombre SSO Menu:** `Governance Gobierno`  
**Dashboard (opcional):** `/gobierno/governance/governance-overview`  
**Icono Sugerido:** 🏛️ fa-landmark  
**Descripción:** Vista de gobierno corporativo de governance

#### **Menu Items:**

| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| 12.1 | Overview de Gobierno | ZUL | `/gobierno/governance/governance-overview` | `console/gobierno/governance/governance-overview.zul` | Admin, Manager, Viewer |
| 12.2 | Detalle de Gobierno | ZUL | `/gobierno/governance/governance-detail` | `console/gobierno/governance/governance-detail.zul` | Admin, Manager, Viewer |

---

## 📊 RESUMEN - SSO APPLICATION: GOVERNANCE

**Total de elementos:**
- **SSO Menus:** 12 menús funcionales
- **Menu Items:** 47 pantallas ZUL
- **Procesos BPMN:** Múltiples procesos de evaluación ética y compliance

**Estado:** ✅ Completamente documentado

---

---

## 🏛️ SSO APPLICATION: GOBIERNO

**Nombre SSO Application:** `Gobierno`  
**Dashboard Principal:** `[PENDIENTE DEFINIR]`  
**Icono Sugerido:** 🏛️ fa-landmark  
**Descripción:** Gobierno y compliance de IA

### 📊 SSO MENUS (A completar por otros chats)

1. **Serving** - [PENDIENTE]
2. **Training** - [PENDIENTE]
3. **Projects** - [PENDIENTE]

---

## 🏗️ SSO APPLICATION: INFRAESTRUCTURA

**Nombre SSO Application:** `Infraestructura`  
**Dashboard Principal:** `[PENDIENTE DEFINIR]`  
**Icono Sugerido:** 🏗️ fa-server  
**Descripción:** Gestión de infraestructura y recursos

### 📊 SSO MENUS (A completar por otros chats)

1. **Monitoring** - [PENDIENTE]
2. **Analytics** - [PENDIENTE]
3. **Data Sources** - [PENDIENTE]
4. **Playground** - [PENDIENTE]

---

## 📋 ROLES Y PERMISOS

### **Roles Definidos:**

| Rol | Nivel de Acceso | Descripción |
|-----|-----------------|-------------|
| **Admin** | Completo | Acceso total a todas las funcionalidades |
| **Manager** | Gestión | Gestión operativa, aprobaciones, sin configuración de sistema |
| **Developer** | Desarrollo | Desarrollo y testing, sin aprobaciones ni compliance |
| **Viewer** | Solo Lectura | Consulta de información, sin modificación |

### **Matriz de Permisos Tipo:**

| Funcionalidad | Admin | Manager | Developer | Viewer |
|---------------|-------|---------|-----------|--------|
| **Dashboard** | ✅ | ✅ | ✅ | ✅ |
| **Listados (overview)** | ✅ | ✅ | ✅ | ✅ |
| **CRUD (crear/editar)** | ✅ | ✅ | ✅ | ❌ |
| **Aprobaciones** | ✅ | ✅ | ❌ | ❌ |
| **Compliance** | ✅ | ✅ | ❌ | 👁️ |
| **Configuración** | ✅ | ⚠️ | ❌ | ❌ |

**Leyenda:**
- ✅ = Acceso completo
- 👁️ = Solo lectura
- ⚠️ = Acceso limitado
- ❌ = Sin acceso

---

## 🔗 INSTRUCCIONES PARA COMPLETAR

### **Para completar otros módulos:**

1. **Identificar SSO Application** (Gobierno, Infraestructura, etc.)
2. **Definir Dashboard principal** de la aplicación
3. **Listar SSO Menus** dentro de la aplicación
4. **Para cada SSO Menu:**
   - Nombre exacto del menú
   - Dashboard del menú (opcional)
   - Icono sugerido
   - Descripción breve
5. **Para cada Menu Item:**
   - Nombre exacto del item
   - Tipo (ZUL o BPMN)
   - Ruta completa
   - Archivo físico ZUL
   - Roles con permiso

### **Formato de tabla para Menu Items:**

```markdown
| # | Nombre Menu Item | Tipo | Ruta | Archivo ZUL | Permisos Rol |
|---|------------------|------|------|-------------|--------------|
| X.X | [Nombre] | ZUL/BPMN | [Ruta] | [Archivo] | [Roles] |
```

---

## 📞 CONTACTO

**Documentación:** Este documento será completado progresivamente  
**Última actualización:** Octubre 29, 2025  
**Estado de completitud:**

### ✅ MÓDULOS COMPLETADOS (7/10+)

| SSO Application | SSO Menus | Menu Items | Estado |
|-----------------|-----------|------------|--------|
| **Agents** | 11 | 52 | ✅ Completo |
| **Models** | 8 | 30 | ✅ Completo |
| **Prompts** | 6 | 16 | ✅ Completo |
| **RAG** | 7 | 17 | ✅ Completo |
| **Providers** | 2 | 4 | ✅ Completo |
| **Core** | 9 | 29 | ✅ Completo |
| **Governance** | 12 | 47 | ✅ Completo |

**Total documentado:** 55 menús funcionales | 195 pantallas ZUL

### ⏳ MÓDULOS PENDIENTES

- **Serving** (gobierno/serving + platform/serving)
- **Training** (gobierno/training + platform/training)
- **Projects** (gobierno/projects + platform/projects)
- **Monitoring** (platform/monitoring)
- **Analytics** (gobierno/analytics + platform/analytics)
- **Data Sources** (platform/data-sources)
- **Playground** (platform/playground)

**Progreso estimado:** 7 de 14+ módulos completados (~50%)
