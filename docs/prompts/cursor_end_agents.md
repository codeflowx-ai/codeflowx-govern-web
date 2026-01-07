# PROMPT: MIGRACIÓN Y REORGANIZACIÓN DE PANTALLAS DE AGENTES

## 📋 DESCRIPCIÓN DEL PROMPT

Este prompt (`cursor_end_agents.md`) realiza la **reorganización y migración de pantallas de agentes** desde el sistema legacy (ZUL/Java) a Next.js. Reorganiza todas las pantallas de agentes bajo el módulo de **Governance** y crea múltiples pantallas para la gestión y monitoreo de agentes de IA.

---

## 🎯 PROPÓSITO

Reorganizar y migrar las pantallas de gestión de agentes desde el sistema legacy a Next.js con:
- Reorganización de estructura de carpetas bajo `governance/agents/`
- Estilos "Wow Factor" (inspirado en Star Trek)
- Mock data para funcionamiento inmediato
- API routes preparadas para integración futura
- Internacionalización completa (español/inglés/francés/alemán/italiano/portugués)
- Estructura moderna y responsive
- Integración con módulo de Governance

---

## 📦 REORGANIZACIÓN REALIZADA

### Estructura Anterior vs Nueva

**Antes:**
```
app/(app)/
├── agents/                    # Módulo separado
│   ├── monitoring/
│   ├── approval/
│   └── ...
└── catalog/                   # Módulo separado
    ├── models/
    ├── registry/
    └── ...
```

**Después:**
```
app/(app)/
├── governance/
│   ├── agents/                ← NUEVO: Todas las pantallas de agentes
│   │   ├── monitoring/
│   │   ├── approval/
│   │   ├── alerts/
│   │   ├── bias-detection/
│   │   ├── compliance/
│   │   ├── decisions/
│   │   ├── deployment/
│   │   ├── ethics/
│   │   ├── governance/
│   │   ├── interactions/
│   │   ├── learning/
│   │   ├── registry/
│   │   ├── rollback/
│   │   ├── tools/
│   │   ├── transparency/
│   │   ├── versioning/
│   │   └── workflow/
│   │
│   ├── models/                ← MOVER desde catalog/models
│   ├── registry/              ← MOVER desde catalog/registry
│   ├── dependencies/          ← MOVER desde catalog/dependencies
│   └── versioning/            ← MOVER desde catalog/versioning
│
├── bpmn/                      ← MANTENER (procesos de gobernanza)
├── admin/                     ← MANTENER (administración)
└── dashboard/                 ← MANTENER (dashboard principal)
```

### Cambios en `modules.ts`

- Módulo "Catalog" eliminado como módulo separado
- Módulo "Agents" integrado en "Governance"
- Rutas actualizadas a `/governance/agents/*`
- Menús reorganizados bajo Governance

---

## 📦 PANTALLAS CREADAS

### 1. **Dashboard de Agentes** (`governance/agents/monitoring/dashboard`)

**Ubicación:** `app/(app)/governance/agents/monitoring/dashboard/page.tsx`

**Funcionalidad:**
- ✅ **KPIs principales:**
  - Total de agentes
  - Health Score promedio
  - Compliance Score
  - Alertas críticas
  - Interacciones 24h
  - Agentes desplegados
- ✅ **Agentes recientes** con información básica
- ✅ **Métricas de rendimiento:**
  - Performance promedio
  - Tiempo de respuesta promedio
  - Interacciones por hora
- ✅ **Estados visuales:** activos, offline, aprobaciones pendientes, en producción
- ✅ **Layout responsive** con grid de 3 columnas para KPIs

**API Routes:**
- `GET /api/governance/agents/dashboard` - Obtener métricas del dashboard

**Mock Data:**
- 5 agentes de ejemplo
- Métricas calculadas automáticamente
- Datos de performance simulados

---

### 2. **Monitoring Overview** (`governance/agents/monitoring/overview`)

**Ubicación:** `app/(app)/governance/agents/monitoring/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de monitoreo** con tabla paginada
- ✅ **Filtros avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo de monitoreo
  - Filtro por estado
- ✅ **Acciones:**
  - Ver detalle
  - Registrar nuevo monitoreo
  - Eliminar monitoreo
- ✅ **Métricas y última verificación** mostradas en tabla

---

### 3. **Health Overview** (`governance/agents/monitoring/health-overview`)

**Ubicación:** `app/(app)/governance/agents/monitoring/health-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de salud de agentes** con tabla paginada
- ✅ **Health Status** con badges visuales
- ✅ **Scores** y última verificación
- ✅ **Filtros** por estado de salud

---

### 4. **Alerts Overview** (`governance/agents/alerts/overview`)

**Ubicación:** `app/(app)/governance/agents/alerts/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de alertas** con tabla paginada
- ✅ **Filtros:**
  - Tipo de alerta
  - Categoría de alerta
  - Severidad
- ✅ **Gestión de alertas** con acciones CRUD

---

### 5. **Approval Overview** (`governance/agents/approval/overview`)

**Ubicación:** `app/(app)/governance/agents/approval/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de aprobaciones** con tabla paginada
- ✅ **Información de aprobación:**
  - Tipo de aprobación
  - Estado de aprobación
  - Criterios de aprobación
  - Razón de solicitud
  - Detalles de solicitud
- ✅ **Gestión de aprobaciones** con acciones CRUD

---

### 6. **Bias Detection Overview** (`governance/agents/bias-detection/overview`)

**Ubicación:** `app/(app)/governance/agents/bias-detection/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de detecciones de sesgo** con tabla paginada
- ✅ **Información de detección:**
  - Tipo de detección
  - Severidad
  - Fecha de detección
  - Descripción
- ✅ **Gestión de detecciones** con acciones CRUD

---

### 7. **Compliance Overview** (`governance/agents/compliance/overview`)

**Ubicación:** `app/(app)/governance/agents/compliance/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de compliance de agentes** con tabla paginada
- ✅ **Métricas de cumplimiento**
- ✅ **Gestión de compliance** con acciones CRUD

---

### 8. **Decisions Overview** (`governance/agents/decisions/overview`)

**Ubicación:** `app/(app)/governance/agents/decisions/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de decisiones** con tabla paginada
- ✅ **Información de decisiones:**
  - Tipo de decisión
  - Estado
  - Fecha de decisión
- ✅ **Gestión de decisiones** con acciones CRUD

---

### 9. **Deployment Overview** (`governance/agents/deployment/overview`)

**Ubicación:** `app/(app)/governance/agents/deployment/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de despliegues** con tabla paginada
- ✅ **Información de despliegue:**
  - Estado de despliegue
  - Ambiente
  - Fecha de despliegue
- ✅ **Gestión de despliegues** con acciones CRUD

---

### 10. **Ethics Overview** (`governance/agents/ethics/overview`)

**Ubicación:** `app/(app)/governance/agents/ethics/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de ética** con tabla paginada
- ✅ **Evaluaciones éticas**
- ✅ **Gestión de ética** con acciones CRUD

---

### 11. **Governance Overview** (`governance/agents/governance/overview`)

**Ubicación:** `app/(app)/governance/agents/governance/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de gobernanza de agentes** con tabla paginada
- ✅ **Políticas aplicadas**
- ✅ **Gestión de gobernanza** con acciones CRUD

---

### 12. **Interactions Overview** (`governance/agents/interactions/overview`)

**Ubicación:** `app/(app)/governance/agents/interactions/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de interacciones** con tabla paginada
- ✅ **Información de interacciones:**
  - Tipo de interacción
  - Estado
  - Fecha
- ✅ **Gestión de interacciones** con acciones CRUD

---

### 13. **Collaboration Overview** (`governance/agents/interactions/collaboration-overview`)

**Ubicación:** `app/(app)/governance/agents/interactions/collaboration-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de colaboraciones** con tabla paginada
- ✅ **Información de colaboración:**
  - Agentes participantes
  - Estado
  - Fecha
- ✅ **Gestión de colaboraciones** con acciones CRUD

---

### 14. **Communication Overview** (`governance/agents/interactions/communication-overview`)

**Ubicación:** `app/(app)/governance/agents/interactions/communication-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de comunicaciones** con tabla paginada
- ✅ **Información de comunicación:**
  - Tipo de comunicación
  - Estado
  - Fecha
- ✅ **Gestión de comunicaciones** con acciones CRUD

---

### 15. **Learning Overview** (`governance/agents/learning/overview`)

**Ubicación:** `app/(app)/governance/agents/learning/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de aprendizaje** con tabla paginada
- ✅ **Información de aprendizaje:**
  - Tipo de aprendizaje
  - Estado
  - Progreso
- ✅ **Gestión de aprendizaje** con acciones CRUD

---

### 16. **Registry** (`governance/agents/registry`)

**Ubicación:** `app/(app)/governance/agents/registry/page.tsx`

**Funcionalidad:**
- ✅ **Registro de agentes** con tabla paginada
- ✅ **Información de registro:**
  - Dominios
  - Capabilities
  - Endpoints
  - Providers
- ✅ **Gestión de registro** con acciones CRUD

---

### 17. **Domain Overview** (`governance/agents/registry/domain-overview`)

**Ubicación:** `app/(app)/governance/agents/registry/domain-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de dominios** con tabla paginada
- ✅ **Información de dominio:**
  - Nombre
  - Descripción
  - Agentes asociados
- ✅ **Gestión de dominios** con acciones CRUD

---

### 18. **Rollback Overview** (`governance/agents/rollback/overview`)

**Ubicación:** `app/(app)/governance/agents/rollback/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de rollbacks** con tabla paginada
- ✅ **Información de rollback:**
  - Versión anterior
  - Versión actual
  - Estado
  - Fecha
- ✅ **Gestión de rollbacks** con acciones CRUD

---

### 19. **Tools Overview** (`governance/agents/tools/overview`)

**Ubicación:** `app/(app)/governance/agents/tools/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de herramientas** con tabla paginada
- ✅ **Información de herramienta:**
  - Tipo
  - Estado
  - Último uso
- ✅ **Gestión de herramientas** con acciones CRUD

---

### 20. **Transparency Overview** (`governance/agents/transparency/overview`)

**Ubicación:** `app/(app)/governance/agents/transparency/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de transparencia** con tabla paginada
- ✅ **Información de transparencia:**
  - Nivel de transparencia
  - Estado
  - Fecha
- ✅ **Gestión de transparencia** con acciones CRUD

---

### 21. **Versioning Overview** (`governance/agents/versioning/overview`)

**Ubicación:** `app/(app)/governance/agents/versioning/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de versiones** con tabla paginada
- ✅ **Información de versión:**
  - Versión
  - Estado
  - Fecha de creación
- ✅ **Gestión de versiones** con acciones CRUD

---

### 22. **Workflow Execution Overview** (`governance/agents/workflow/execution-overview`)

**Ubicación:** `app/(app)/governance/agents/workflow/execution-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de ejecuciones de workflow** con tabla paginada
- ✅ **Información de ejecución:**
  - Workflow
  - Estado
  - Fecha de ejecución
- ✅ **Gestión de ejecuciones** con acciones CRUD

---

## 🏗️ ARQUITECTURA

### Estructura de Archivos Creados

```
app/(app)/governance/agents/
├── monitoring/
│   ├── dashboard/
│   │   └── page.tsx           # Dashboard principal
│   ├── overview/
│   │   └── page.tsx           # Monitoring overview
│   └── health-overview/
│       └── page.tsx           # Health overview
├── alerts/
│   └── overview/
│       └── page.tsx           # Alerts overview
├── approval/
│   └── overview/
│       └── page.tsx           # Approval overview
├── bias-detection/
│   └── overview/
│       └── page.tsx           # Bias detection overview
├── compliance/
│   └── overview/
│       └── page.tsx           # Compliance overview
├── decisions/
│   └── overview/
│       └── page.tsx           # Decisions overview
├── deployment/
│   └── overview/
│       └── page.tsx           # Deployment overview
├── ethics/
│   └── overview/
│       └── page.tsx           # Ethics overview
├── governance/
│   └── overview/
│       └── page.tsx           # Governance overview
├── interactions/
│   ├── overview/
│   │   └── page.tsx           # Interactions overview
│   ├── collaboration-overview/
│   │   └── page.tsx           # Collaboration overview
│   └── communication-overview/
│       └── page.tsx           # Communication overview
├── learning/
│   └── overview/
│       └── page.tsx           # Learning overview
├── registry/
│   ├── page.tsx               # Registry principal
│   └── domain-overview/
│       └── page.tsx           # Domain overview
├── rollback/
│   └── overview/
│       └── page.tsx           # Rollback overview
├── tools/
│   └── overview/
│       └── page.tsx           # Tools overview
├── transparency/
│   └── overview/
│       └── page.tsx           # Transparency overview
├── versioning/
│   └── overview/
│       └── page.tsx           # Versioning overview
└── workflow/
    └── execution-overview/
        └── page.tsx           # Workflow execution overview

app/config/i18n/modules/agents/
├── index.ts                   # Módulo de traducciones de agentes
├── dashboard.ts               # Traducciones del dashboard
├── monitoring-overview.ts     # Traducciones de monitoring overview
├── health-overview.ts         # Traducciones de health overview
├── alerts.ts                  # Traducciones de alerts
├── approval.ts                # Traducciones de approval
├── bias-detection.ts          # Traducciones de bias detection
├── compliance.ts              # Traducciones de compliance
├── decisions.ts               # Traducciones de decisions
├── deployment.ts              # Traducciones de deployment
├── ethics.ts                  # Traducciones de ethics
├── governance.ts              # Traducciones de governance
├── interactions.ts            # Traducciones de interactions
├── collaboration.ts           # Traducciones de collaboration
├── communication.ts            # Traducciones de communication
├── learning.ts                # Traducciones de learning
├── registry.ts                # Traducciones de registry
├── domain-overview.ts         # Traducciones de domain overview
├── rollback.ts                # Traducciones de rollback
├── tools.ts                   # Traducciones de tools
├── transparency.ts            # Traducciones de transparency
├── versioning.ts              # Traducciones de versioning
└── workflow.ts                # Traducciones de workflow

app/config/modules.ts          # Configuración de módulos actualizada
```

### Componentes UI Utilizados

- `Card`, `CardBody`, `CardHeader`, `CardTitle` - Tarjetas de contenido
- `Button` - Botones de acción
- `Badge` - Badges de estado
- `Input` - Campos de entrada
- `Select` - Selectores
- `Table` - Tablas de datos
- `Tabs`, `TabsContent`, `TabsList`, `TabsTrigger` - Pestañas
- `SimpleModal` - Modales informativos

### Estilos "Wow Factor"

- Bordes de color en cards de KPIs
- Transiciones suaves en hover
- Layout responsive con grid
- Badges visuales para estados
- Iconos de Lucide React

---

## 🔌 INTEGRACIÓN CON BACKEND

### Estado Actual: **MOCK MODE**

Todas las pantallas funcionan con **mock data** para permitir desarrollo y testing sin backend.

### Desactivación de Mock (Futuro)

Para desactivar el mock y conectar con el backend real:

1. Actualizar las API routes en `app/api/governance/agents/*/route.ts`
2. Cambiar `USE_MOCK_DATA = false` en cada route
3. Configurar la URL del backend en variables de entorno

### Endpoints Backend Esperados

#### Dashboard
- `GET /api/v1/governance/agents/dashboard` - Obtener métricas del dashboard

#### Monitoring
- `GET /api/v1/governance/agents/monitoring` - Listar monitoreos
- `GET /api/v1/governance/agents/monitoring/{id}` - Obtener monitoreo
- `POST /api/v1/governance/agents/monitoring` - Crear monitoreo
- `PUT /api/v1/governance/agents/monitoring/{id}` - Actualizar monitoreo
- `DELETE /api/v1/governance/agents/monitoring/{id}` - Eliminar monitoreo

#### Health
- `GET /api/v1/governance/agents/health` - Listar estados de salud
- `GET /api/v1/governance/agents/health/{id}` - Obtener estado de salud

#### Alerts
- `GET /api/v1/governance/agents/alerts` - Listar alertas
- `POST /api/v1/governance/agents/alerts` - Crear alerta
- `PUT /api/v1/governance/agents/alerts/{id}` - Actualizar alerta
- `DELETE /api/v1/governance/agents/alerts/{id}` - Eliminar alerta

#### Approval
- `GET /api/v1/governance/agents/approvals` - Listar aprobaciones
- `POST /api/v1/governance/agents/approvals` - Crear aprobación
- `PUT /api/v1/governance/agents/approvals/{id}` - Actualizar aprobación
- `DELETE /api/v1/governance/agents/approvals/{id}` - Eliminar aprobación

#### Bias Detection
- `GET /api/v1/governance/agents/bias-detection` - Listar detecciones
- `POST /api/v1/governance/agents/bias-detection` - Crear detección
- `PUT /api/v1/governance/agents/bias-detection/{id}` - Actualizar detección
- `DELETE /api/v1/governance/agents/bias-detection/{id}` - Eliminar detección

#### Compliance
- `GET /api/v1/governance/agents/compliance` - Listar compliance
- `POST /api/v1/governance/agents/compliance` - Crear compliance
- `PUT /api/v1/governance/agents/compliance/{id}` - Actualizar compliance
- `DELETE /api/v1/governance/agents/compliance/{id}` - Eliminar compliance

#### Decisions
- `GET /api/v1/governance/agents/decisions` - Listar decisiones
- `POST /api/v1/governance/agents/decisions` - Crear decisión
- `PUT /api/v1/governance/agents/decisions/{id}` - Actualizar decisión
- `DELETE /api/v1/governance/agents/decisions/{id}` - Eliminar decisión

#### Deployment
- `GET /api/v1/governance/agents/deployments` - Listar despliegues
- `POST /api/v1/governance/agents/deployments` - Crear despliegue
- `PUT /api/v1/governance/agents/deployments/{id}` - Actualizar despliegue
- `DELETE /api/v1/governance/agents/deployments/{id}` - Eliminar despliegue

#### Ethics
- `GET /api/v1/governance/agents/ethics` - Listar ética
- `POST /api/v1/governance/agents/ethics` - Crear ética
- `PUT /api/v1/governance/agents/ethics/{id}` - Actualizar ética
- `DELETE /api/v1/governance/agents/ethics/{id}` - Eliminar ética

#### Governance
- `GET /api/v1/governance/agents/governance` - Listar gobernanza
- `POST /api/v1/governance/agents/governance` - Crear gobernanza
- `PUT /api/v1/governance/agents/governance/{id}` - Actualizar gobernanza
- `DELETE /api/v1/governance/agents/governance/{id}` - Eliminar gobernanza

#### Interactions
- `GET /api/v1/governance/agents/interactions` - Listar interacciones
- `POST /api/v1/governance/agents/interactions` - Crear interacción
- `PUT /api/v1/governance/agents/interactions/{id}` - Actualizar interacción
- `DELETE /api/v1/governance/agents/interactions/{id}` - Eliminar interacción

#### Collaboration
- `GET /api/v1/governance/agents/collaborations` - Listar colaboraciones
- `POST /api/v1/governance/agents/collaborations` - Crear colaboración
- `PUT /api/v1/governance/agents/collaborations/{id}` - Actualizar colaboración
- `DELETE /api/v1/governance/agents/collaborations/{id}` - Eliminar colaboración

#### Communication
- `GET /api/v1/governance/agents/communications` - Listar comunicaciones
- `POST /api/v1/governance/agents/communications` - Crear comunicación
- `PUT /api/v1/governance/agents/communications/{id}` - Actualizar comunicación
- `DELETE /api/v1/governance/agents/communications/{id}` - Eliminar comunicación

#### Learning
- `GET /api/v1/governance/agents/learning` - Listar aprendizaje
- `POST /api/v1/governance/agents/learning` - Crear aprendizaje
- `PUT /api/v1/governance/agents/learning/{id}` - Actualizar aprendizaje
- `DELETE /api/v1/governance/agents/learning/{id}` - Eliminar aprendizaje

#### Registry
- `GET /api/v1/governance/agents/registry` - Listar registro
- `GET /api/v1/governance/agents/registry/domains` - Listar dominios
- `POST /api/v1/governance/agents/registry/domains` - Crear dominio
- `PUT /api/v1/governance/agents/registry/domains/{id}` - Actualizar dominio
- `DELETE /api/v1/governance/agents/registry/domains/{id}` - Eliminar dominio

#### Rollback
- `GET /api/v1/governance/agents/rollbacks` - Listar rollbacks
- `POST /api/v1/governance/agents/rollbacks` - Crear rollback
- `PUT /api/v1/governance/agents/rollbacks/{id}` - Actualizar rollback
- `DELETE /api/v1/governance/agents/rollbacks/{id}` - Eliminar rollback

#### Tools
- `GET /api/v1/governance/agents/tools` - Listar herramientas
- `POST /api/v1/governance/agents/tools` - Crear herramienta
- `PUT /api/v1/governance/agents/tools/{id}` - Actualizar herramienta
- `DELETE /api/v1/governance/agents/tools/{id}` - Eliminar herramienta

#### Transparency
- `GET /api/v1/governance/agents/transparency` - Listar transparencia
- `POST /api/v1/governance/agents/transparency` - Crear transparencia
- `PUT /api/v1/governance/agents/transparency/{id}` - Actualizar transparencia
- `DELETE /api/v1/governance/agents/transparency/{id}` - Eliminar transparencia

#### Versioning
- `GET /api/v1/governance/agents/versioning` - Listar versiones
- `POST /api/v1/governance/agents/versioning` - Crear versión
- `PUT /api/v1/governance/agents/versioning/{id}` - Actualizar versión
- `DELETE /api/v1/governance/agents/versioning/{id}` - Eliminar versión

#### Workflow
- `GET /api/v1/governance/agents/workflows/executions` - Listar ejecuciones
- `POST /api/v1/governance/agents/workflows/executions` - Crear ejecución
- `PUT /api/v1/governance/agents/workflows/executions/{id}` - Actualizar ejecución
- `DELETE /api/v1/governance/agents/workflows/executions/{id}` - Eliminar ejecución

---

## 📊 DATOS Y MODELOS

### Interfaces TypeScript

```typescript
interface Agent {
  id: number;
  name: string;
  version: string;
  status: string;
  healthScore: number;
  complianceScore: number;
  performanceScore: number;
  responseTime: number;
  interactions24h: number;
  lastInteractionAt?: string;
  deployedAt?: string;
}

interface Monitoring {
  id: number;
  agentId: number;
  agentName: string;
  monitoringType: string;
  metrics: Record<string, any>;
  lastCheck: string;
  status: string;
}

interface Alert {
  id: number;
  agentId: number;
  agentName: string;
  alertType: string;
  alertCategory: string;
  severity: string;
  message: string;
  detectedAt: string;
  resolvedAt?: string;
  status: string;
}

interface Approval {
  id: number;
  agentId: number;
  agentName: string;
  approvalType: string;
  approvalStatus: string;
  approvalCriteria: string;
  requestReason: string;
  requestDetails: string;
  requestedAt: string;
  approvedAt?: string;
  approvedBy?: string;
}
```

---

## 🌐 INTERNACIONALIZACIÓN

### Traducciones Incluidas

Todas las pantallas incluyen traducciones completas en:
- ✅ Español (es)
- ✅ Inglés (en)
- ✅ Francés (fr)
- ✅ Alemán (de)
- ✅ Italiano (it)
- ✅ Portugués (pt)

### Estructura de Traducciones

```typescript
// Ejemplo: agents/dashboard.ts
export const agentsDashboardTranslations: TranslationModule = {
  es: {
    title: "Dashboard de Agentes",
    subtitle: "Métricas y estado general de agentes AI",
    refresh: "Refrescar",
    totalAgents: "Total Agentes",
    healthScore: "Health Score Promedio",
    // ...
  },
  en: { /* ... */ },
  fr: { /* ... */ },
  de: { /* ... */ },
  it: { /* ... */ },
  pt: { /* ... */ },
};
```

### Uso en Componentes

```typescript
const { t } = useTranslation();
// ...
<h1>{t("agents.dashboard.title", "Dashboard de Agentes")}</h1>
```

---

## 📍 NAVEGACIÓN Y MENÚ

### Rutas Disponibles

- `/governance/agents/monitoring/dashboard` - Dashboard principal (default)
- `/governance/agents/monitoring/overview` - Monitoring overview
- `/governance/agents/monitoring/health-overview` - Health overview
- `/governance/agents/alerts/overview` - Alerts overview
- `/governance/agents/approval/overview` - Approval overview
- `/governance/agents/bias-detection/overview` - Bias detection overview
- `/governance/agents/compliance/overview` - Compliance overview
- `/governance/agents/decisions/overview` - Decisions overview
- `/governance/agents/deployment/overview` - Deployment overview
- `/governance/agents/ethics/overview` - Ethics overview
- `/governance/agents/governance/overview` - Governance overview
- `/governance/agents/interactions/overview` - Interactions overview
- `/governance/agents/interactions/collaboration-overview` - Collaboration overview
- `/governance/agents/interactions/communication-overview` - Communication overview
- `/governance/agents/learning/overview` - Learning overview
- `/governance/agents/registry` - Registry principal
- `/governance/agents/registry/domain-overview` - Domain overview
- `/governance/agents/rollback/overview` - Rollback overview
- `/governance/agents/tools/overview` - Tools overview
- `/governance/agents/transparency/overview` - Transparency overview
- `/governance/agents/versioning/overview` - Versioning overview
- `/governance/agents/workflow/execution-overview` - Workflow execution overview

### Entrada en Menú

Las pantallas están registradas en el menú del módulo `Agents` para acceso desde el sidebar:

```typescript
case "Agents":
  return [
    {
      name: "Dashboard",
      href: `/governance/agents/monitoring/dashboard`,
      icon: "BarChart3",
      roles: ["admin", "project_manager", "compliance_officer", "governance_manager", "auditor", "developer"],
      isDefault: true,
    },
    {
      name: "Monitoring",
      href: `/governance/agents/monitoring/overview`,
      icon: "Activity",
      roles: ["admin", "project_manager", "compliance_officer", "governance_manager"],
    },
    // ... más items del menú
  ];
```

---

## ✅ ESTADO ACTUAL

### Pantallas Implementadas

- ✅ `app/(app)/governance/agents/monitoring/dashboard/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/monitoring/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/monitoring/health-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/alerts/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/approval/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/bias-detection/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/compliance/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/decisions/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/deployment/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/ethics/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/governance/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/interactions/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/interactions/collaboration-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/interactions/communication-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/learning/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/registry/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/registry/domain-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/rollback/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/tools/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/transparency/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/versioning/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/agents/workflow/execution-overview/page.tsx` - **COMPLETADO**

**Total: 22 pantallas implementadas**

### Traducciones Implementadas

- ✅ Módulo de traducciones de agentes creado
- ✅ Traducciones para todas las pantallas en 6 idiomas
- ✅ Integración con sistema de i18n global

### Configuración

- ✅ Módulo "Agents" configurado en `modules.ts`
- ✅ Rutas actualizadas a `/governance/agents/*`
- ✅ Menús reorganizados bajo Governance

---

## 🔄 FLUJO DE USUARIO

### Dashboard Principal

1. **Usuario accede a `/governance/agents/monitoring/dashboard`** (pantalla principal)
2. **Visualiza KPIs principales:**
   - Total de agentes
   - Health Score promedio
   - Compliance Score
   - Alertas críticas
   - Interacciones 24h
   - Agentes desplegados
3. **Ve agentes recientes** con información básica
4. **Revisa métricas de rendimiento**
5. **Navega a otras pantallas** desde el menú lateral

### Pantallas Overview

1. **Usuario accede a una pantalla overview** (ej: `/governance/agents/alerts/overview`)
2. **Visualiza listado** con tabla paginada
3. **Aplica filtros** si es necesario
4. **Realiza acciones CRUD:**
   - Ver detalle
   - Crear nuevo registro
   - Editar registro
   - Eliminar registro
5. **Navega a otras pantallas** desde el menú lateral

---

## 📝 NOTAS IMPORTANTES

1. **Mock Data:** Todas las pantallas funcionan con datos mock para desarrollo
2. **Reorganización:** Todas las pantallas de agentes están ahora bajo `governance/agents/`
3. **Compliance:** No se modificó `governance/compliance/` (corresponde con EU AI Act)
4. **Internacionalización:** Todas las pantallas usan el sistema de traducciones centralizado
5. **Estructura:** La estructura sigue el patrón establecido en la documentación de arquitectura frontend

---

## 🚀 PRÓXIMOS PASOS

1. **Integración Backend:** Conectar las API routes con el backend real
2. **Pantallas de Detalle:** Crear pantallas de detalle para cada módulo (ej: `/governance/agents/alerts/detail/[id]`)
3. **Formularios:** Crear formularios de creación/edición para cada módulo
4. **Validaciones:** Agregar validaciones de formularios
5. **Testing:** Implementar tests unitarios y de integración
6. **Documentación:** Completar documentación de API

---

## 📚 REFERENCIAS

### Archivos de Migración Originales

- `docs/funcional/agents/01_REORGANIZACION_PANTALLAS_AGENTES.md` - Reorganización de pantallas
- `docs/prompts/BUSINESS_LOGIC_AGENTS.md` - Lógica de negocio de agentes
- `docs/prompts/MIGRACION_AGENTS_OVERVIEWS.md` - Migración de overviews
- `docs/prompts/MIGRACION_AGENTS_DASHBOARDS.md` - Migración de dashboards

### Documentación Funcional

- `docs/ARQUITECTURA_FRONTEND.md` - Arquitectura frontend
- `docs/PANTALLAS_ESENCIALES_USUARIOS.md` - Pantallas esenciales
- `app/config/modules.ts` - Configuración de módulos

---

## 📊 RESUMEN

**Pantallas migradas:** 22 de 22 (100%)
**Traducciones:** 6 idiomas (es, en, fr, de, it, pt)
**Estructura:** Reorganizada bajo `governance/agents/`
**Estado:** ✅ Completado con mock data
**Próximo paso:** Integración con backend real
