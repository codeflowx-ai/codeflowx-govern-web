# Separación de Módulos: Governance vs Development

## Propuesta de Reorganización

### Estructura Actual vs Propuesta

#### **MÓDULOS DE GOBIERNO Y CUMPLIMIENTO** (Governance & Compliance)

Estos módulos están orientados a **gobierno, autorizaciones, testing, calidad y cumplimiento normativo**:

1. **Governance** (Gobierno)
   - Dashboard
   - Policies Overview
   - Policy Detail
   - Security
   - Monitoring
   - Telemetry
   - RAG Governance
   - ODS Impact
   - Risk Assessment
   - Auto Approval
   - Projects (gestión de proyectos de gobierno)
   - Models (gobernanza de modelos)
   - Dependencies
   - Versioning

2. **Compliance** (Cumplimiento Normativo)
   - Compliance Dashboard
   - Classification
   - FRIA Projects
   - EU Registration
   - Post-Market Monitoring
   - Technical Docs
   - HITL Supervision
   - Conformity Declaration
   - QMS (Quality Management System)
   - Traceability
   - Prohibited Systems
   - Immutable Logs

3. **Agents** (Gestión y Gobernanza de Agentes)
   - Dashboard
   - Registry
   - Monitoring Overview
   - Health Overview
   - Deployment (gestión de despliegues)
   - **Approval** (autorizaciones)
   - **Compliance** (cumplimiento)
   - Interactions
   - Collaboration
   - Communication
   - Learning
   - Versioning
   - Rollback
   - Decisions
   - Ethics
   - Governance
   - Tools
   - Transparency
   - Bias Detection
   - Alerts
   - Workflow Execution

4. **BPMN** (Procesos de Gobierno y Aprobaciones)
   - Task Inbox
   - Compliance Review
   - Internal Audit Scheduling
   - Internal Audit Execution
   - Model Approval - Override
   - Agent Approval - Override
   - Prompt Human Review
   - HITL SLA Reminder
   - EU Registration
   - Conformity Assessment Approval
   - Bias Review
   - Ethics Review Request
   - Corrective Action Assessment
   - Management Review

5. **Prompts** (Gobernanza de Prompts)
   - Prompts
   - Validation
   - Versioning
   - Performance

---

#### **MÓDULOS DE DESARROLLO** (Development)

Estos módulos están orientados a **desarrollo, entrenamiento, despliegue y operación técnica**:

1. **Models** (Desarrollo de Modelos)
   - Approval (desde perspectiva técnica)
   - Bias Analysis
   - Explainability
   - Performance
   - Usage Overview
   - Registry
   - Providers

2. **Training** (Entrenamiento de Modelos)
   - Training Governance Dashboard
   - HPO Dashboard
   - Experiments
   - Models
   - Datasets

3. **Serving** (Despliegue y Operación)
   - Deployments
   - Monitoring
   - Resources
   - Deployment Instance
   - Model
   - Model Deployment
   - Serving Endpoint

4. **Infrastructure** (Infraestructura)
   - Resource Utilization Dashboard
   - Resources
   - Providers
   - Credentials
   - Kubernetes
   - GPU Instances
   - Cost Management
   - Deployments

5. **RAG** (Sistemas RAG - Desarrollo)
   - Dashboard
   - Projects
   - Models
   - Data Sources
   - Monitoring
   - Versioning
   - Search
   - Chat
   - Chunks
   - Analytics
   - Database
   - API
   - Reranker
   - Community

---

## Propuesta de Separación en el Menú

### Opción 1: Separación por Secciones Visuales

```typescript
// Módulos de Gobierno (aparecen primero)
const governanceModules = [
  "Governance",
  "Compliance",
  "Agents",
  "BPMN",
  "Prompts"
];

// Módulos de Desarrollo (aparecen después)
const developmentModules = [
  "Models",
  "Training",
  "Serving",
  "Infrastructure",
  "RAG"
];

// Módulos Administrativos (al final)
const adminModules = [
  "Admin"
];
```

### Opción 2: Separación por Roles y Permisos

```typescript
// Roles de Gobierno
const governanceRoles = [
  "admin",
  "compliance_officer",
  "governance_manager",
  "auditor",
  "risk_manager",
  "security_officer"
];

// Roles de Desarrollo
const developmentRoles = [
  "developer",
  "data_scientist",
  "ai_developer",
  "devops",
  "it",
  "oem"
];
```

### Opción 3: Separación por Rutas (Recomendada)

**Rutas de Gobierno:**
- `/governance/*` - Todo lo relacionado con gobierno
- `/governance/compliance/*` - Cumplimiento normativo
- `/governance/agents/*` - Gestión de agentes
- `/governance/prompts/*` - Gobernanza de prompts
- `/bpmn/*` - Procesos de gobierno y aprobaciones

**Rutas de Desarrollo:**
- `/models/*` - Desarrollo de modelos
- `/training/*` - Entrenamiento
- `/serving/*` - Despliegue
- `/infrastructure/*` - Infraestructura
- `/rag/*` - Sistemas RAG

---

## Implementación Propuesta

### 1. Actualizar `modules.ts` con categorías

```typescript
export interface ModuleConfig {
  name: string;
  icon: any;
  roles: string[];
  defaultPath: string;
  description?: string;
  category: "governance" | "development" | "admin"; // Nueva propiedad
  order: number; // Para ordenar dentro de la categoría
}

export const modulesConfig: ModuleConfig[] = [
  // ========== GOBIERNO Y CUMPLIMIENTO ==========
  {
    name: "Governance",
    category: "governance",
    order: 1,
    // ...
  },
  {
    name: "Compliance",
    category: "governance",
    order: 2,
    // ...
  },
  {
    name: "Agents",
    category: "governance",
    order: 3,
    // ...
  },
  {
    name: "BPMN",
    category: "governance",
    order: 4,
    // ...
  },
  {
    name: "Prompts",
    category: "governance",
    order: 5,
    // ...
  },

  // ========== DESARROLLO ==========
  {
    name: "Models",
    category: "development",
    order: 1,
    // ...
  },
  {
    name: "Training",
    category: "development",
    order: 2,
    // ...
  },
  {
    name: "Serving",
    category: "development",
    order: 3,
    // ...
  },
  {
    name: "Infrastructure",
    category: "development",
    order: 4,
    // ...
  },
  {
    name: "RAG",
    category: "development",
    order: 5,
    // ...
  },

  // ========== ADMINISTRACIÓN ==========
  {
    name: "Admin",
    category: "admin",
    order: 1,
    // ...
  },
];
```

### 2. Actualizar el Sidebar para mostrar secciones

```typescript
// Agrupar módulos por categoría
const groupedModules = modulesConfig.reduce((acc, module) => {
  if (!acc[module.category]) {
    acc[module.category] = [];
  }
  acc[module.category].push(module);
  return acc;
}, {} as Record<string, ModuleConfig[]>);

// Ordenar dentro de cada categoría
Object.keys(groupedModules).forEach(category => {
  groupedModules[category].sort((a, b) => a.order - b.order);
});
```

### 3. Actualizar el Header para mostrar categorías

El selector de módulos en el header podría mostrar:
- **Gobierno** (Governance, Compliance, Agents, BPMN, Prompts)
- **Desarrollo** (Models, Training, Serving, Infrastructure, RAG)
- **Administración** (Admin)

---

## Beneficios de la Separación

1. **Claridad**: Los usuarios de gobierno ven solo lo relevante para su trabajo
2. **Seguridad**: Separación de permisos más clara
3. **UX**: Menús más limpios y organizados
4. **Escalabilidad**: Fácil agregar nuevos módulos en la categoría correcta
5. **Mantenibilidad**: Código más organizado y fácil de mantener

---

## Migración Implementada ✅

1. ✅ **Fase 1**: Agregada propiedad `category` y `order` a todos los módulos en `modules.ts`
2. ✅ **Fase 2**: Creado archivo `modules-utils.ts` con utilidades para agrupar por categorías
3. ✅ **Fase 3**: Actualizado el header para mostrar módulos agrupados por categorías con encabezados
4. ✅ **Fase 4**: Agregadas traducciones para categorías en todos los idiomas (es, en, fr, de, it, pt)
5. ✅ **Fase 5**: Documentada la nueva estructura en este documento

### Archivos Modificados

- `app/config/modules.ts` - Agregadas propiedades `category` y `order` a todos los módulos
- `app/config/modules-utils.ts` - Nuevo archivo con utilidades para categorías
- `components/layout/header.tsx` - Actualizado para mostrar módulos agrupados por categorías
- `app/config/i18n/modules/layout.ts` - Agregadas traducciones de categorías
- `docs/SEPARACION_MODULOS_GOVERNANCE_DEVELOPMENT.md` - Documentación completa

---

## Consideraciones

- Algunos módulos pueden tener elementos de ambos (ej: Agents tiene deployment que es técnico, pero approval que es gobierno)
- La separación debe ser flexible para permitir acceso cruzado cuando sea necesario
- Los roles pueden tener acceso a múltiples categorías
