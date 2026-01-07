# Prompt de Migración - Governance - OVERVIEWS

## Contexto del Módulo

**Módulo:** Governance
**Tipo de Pantallas:** overviews
**Total de Pantallas:** 1

## Documentación Funcional Asociada

**Documento:** `docs/funcional/governance/01_REORGANIZACION_PANTALLAS_GOVERNANCE.md`

**Ubicación:** `suinsit.nova.web/docs/funcional/governance/01_REORGANIZACION_PANTALLAS_GOVERNANCE.md`

### Resumen de la Documentación

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
- `quality/ethics-review.

---

## Arquitectura del Módulo

### ViewModels Identificados (17)


#### AICompetenceViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/AICompetenceViewModel.java`
- **Servicios Usados:** ModelService, AICompetenceBusinessService, AICCompetenceService, AITTrainingRecordService
- **Entidades Usadas:** AITTrainingRecord, AICCompetence
- **Mock Mode:** ❌ No

#### AIMSImprovementViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/AIMSImprovementViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### AIMSNonConformityViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/AIMSNonConformityViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### AIMSPerformanceViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/AIMSPerformanceViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### AIObjectivesViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/AIObjectivesViewModel.java`
- **Servicios Usados:** ModelService, AIObjectivesBusinessService, AIObjectiveService
- **Entidades Usadas:** AIObjective
- **Mock Mode:** ❌ No

#### ComplianceAiActViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/ComplianceAiActViewModel.java`
- **Servicios Usados:** PolicyService, ComplianceAssessmentService, PolicyChecklistItemService, ComplianceRequirementService
- **Entidades Usadas:** ComplianceRequirement, PolicyChecklistItem, ComplianceAssessment
- **Mock Mode:** ❌ No

#### ComplianceAutomatedChecksViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/ComplianceAutomatedChecksViewModel.java`
- **Servicios Usados:** ComplianceAssessmentService, ComplianceFindingService, ComplianceGapsAnalysisService, BusinessService
- **Entidades Usadas:** ComplianceAssessment, ComplianceFinding
- **Mock Mode:** ❌ No

#### EthicsAssessmentsViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/EthicsAssessmentsViewModel.java`
- **Servicios Usados:** PolicyService, PolicyEvaluationService, ComplianceAssessmentService
- **Entidades Usadas:** PolicyEvaluation, ComplianceAssessment
- **Mock Mode:** ❌ No

#### EthicsCommitteeViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/EthicsCommitteeViewModel.java`
- **Servicios Usados:** PolicyService, PolicyEvaluationService, ComplianceAssessmentService, UserService
- **Entidades Usadas:** PolicyEvaluation, ComplianceAssessment, User
- **Mock Mode:** ❌ No

#### EthicsImpactViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/EthicsImpactViewModel.java`
- **Servicios Usados:** ComplianceAssessmentService, ModelService
- **Entidades Usadas:** Model, ComplianceAssessment
- **Mock Mode:** ❌ No

#### EthicsMitigationViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/EthicsMitigationViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### EthicsViolationsViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/EthicsViolationsViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### GovernanceDashboardViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/GovernanceDashboardViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### GovernanceDetailViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/GovernanceDetailViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### GovernanceOverviewViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/GovernanceOverviewViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

### Servicios Backend Disponibles (0)


### Entidades JPA (21)

- **AIActTechnicalDocumentation** - `com.codeflowx.govern.entity.governance`
- **AICCompetence** - `com.codeflowx.govern.entity.governance`
- **AIObjective** - `com.codeflowx.govern.entity.governance`
- **AITTrainingRecord** - `com.codeflowx.govern.entity.governance`
- **ComplianceAssessment** - `com.codeflowx.govern.entity.governance`
- **ComplianceFinding** - `com.codeflowx.govern.entity.governance`
- **ComplianceRequirement** - `com.codeflowx.govern.entity.governance`
- **ConformityDeclaration** - `com.codeflowx.govern.entity.governance`
- **DatasetQuality** - `com.codeflowx.govern.entity.governance`
- **EthicsReview** - `com.codeflowx.govern.entity.governance`

### Microservicios Java Disponibles

Los ViewModels pueden usar los siguientes clientes Java para llamar a microservicios:

1. **AIGovernanceClient** (Factoría)
   - Gateway: `http://api-leka-govern:8000` (K8s) o `http://localhost:8000` (local)
   - Proporciona acceso a múltiples clientes especializados

2. **AgentMonitoringClient**
   - Microservicio: `leka-agent-monitoring` (puerto 8005)
   - Endpoints: `/api/agent/*`
   - Métodos: `analyzeExecution()`, `evaluateReliability()`, `analyzeCost()`, etc.

3. **LLMEvaluationClient**
   - Microservicio: `leka-llm-evaluation` (puerto 8002)
   - Endpoints: `/api/llm/*`

4. **PromptGovernanceClient**
   - Microservicio: `leka-prompt-governance` (puerto 8003)
   - Endpoints: `/api/prompt/*`

5. **RAGEvaluationClient**
   - Microservicio: `leka-rag-evaluation` (puerto 8004)
   - Endpoints: `/api/rag/*`

6. **ModelWrapperClient**
   - Microservicio: `leka-model-wrapper` (puerto 8006)
   - Endpoints: `/api/models/*`

**Ubicación de Clientes:**
- `nocode.service/codeflowx.govern.nocode.client/src/main/java/com/codeflowx/governance/client/`

**Uso en ViewModels:**
```java
@WireVariable
private AIGovernanceClient aiGovernanceClient;

// Ejemplo de uso
AgentMonitoringClient agentClient = aiGovernanceClient.agentMonitoring();
AgentExecutionResponse response = agentClient.analyzeExecution(request);
```

---

## ⚠️ IMPORTANTE: Revisar Pantallas Existentes

**ANTES de migrar, verificar si la pantalla ya existe en Next.js:**

### 1. Páginas Existentes en `app/(app)/`

**Total encontradas:** 7

- `app/(app)/governance/page.tsx`
- `app/(app)/governance/auto-approval/page.tsx`
- `app/(app)/governance/compliance/page.tsx`
- `app/(app)/governance/monitoring/page.tsx`
- `app/(app)/governance/policies/page.tsx`
- `app/(app)/governance/risk-assessment/page.tsx`
- `app/(app)/governance/security/page.tsx`

### 2. Páginas Existentes en `app/` (raíz)

**No se encontraron páginas existentes**


### 3. Entradas en el Menú (`app/config/modules.ts`)

**Total encontradas:** 3

- `/governance`
- `/governance/security`
- `/governance/monitoring`

### 4. Acción a Realizar

**Si la pantalla YA EXISTE:**
1. ✅ **Revisar la página existente** en `app/(app)/[ruta]/page.tsx` o `app/[ruta]/page.tsx`
2. ✅ **Verificar funcionalidad actual** y comparar con el ZUL original
3. ✅ **Actualizar campos faltantes** según el ViewModel Java
4. ✅ **Mantener estilos "Wow Factor"** existentes (ver sección de estilos)
5. ✅ **Verificar entrada en menú** en `app/config/modules.ts`
6. ✅ **Actualizar traducciones** si faltan campos en `app/config/i18n.ts`

**Si la pantalla NO EXISTE:**
1. ✅ **Crear nueva página** siguiendo la estructura de carpetas
2. ✅ **Implementar con estilos "Wow Factor"** (ver sección de estilos)
3. ✅ **Agregar entrada al menú** en `app/config/modules.ts`
4. ✅ **Agregar traducciones** en `app/config/i18n.ts`

### 5. Mantener Estilos "Wow Factor"

**IMPORTANTE:** Todas las páginas deben mantener el estilo "Wow Factor" inspirado en Star Trek:

#### Estructura Base Requerida:
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { IconName } from "lucide-react";

export default function PageName() {
  const { t } = useTranslation();
  
  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <IconName className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("pageName.title", "Título")}
            </h1>
          </div>
        </div>
      </div>
    </div>
  );
}
```

#### Características "Wow Factor" a Mantener:
- ✅ **Gradientes sutiles** en backgrounds (`bg-gradient-to-br`)
- ✅ **Partículas flotantes animadas** en el fondo
- ✅ **Glassmorphism** en banners y cards (`backdrop-blur-md`, `bg-background/60`)
- ✅ **Gradientes en títulos** (`bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent`)
- ✅ **Animaciones suaves** (`animate-pulse`, `transition-all duration-300`)
- ✅ **Cards con efectos hover** (`hover:shadow-3xl`, `hover:border-primary/50`)
- ✅ **Iconos de Lucide React** con colores temáticos
- ✅ **Badges y chips** con estilos modernos
- ✅ **Modales con SimpleModal** (usar `@/components/ui/SimpleModal`)

#### Referencias de Estilos:
- **Guía completa:** `MIGRATION_GUIDE.md` en la raíz del proyecto
- **Ejemplos de páginas migradas:**
  - `app/(app)/model-management/page.tsx` - Dashboard con wow factor
  - `app/(app)/infrastructure/page.tsx` - Listado con wow factor
  - `app/(app)/dashboard/page.tsx` - Dashboard principal
  - `app/(app)/admin/page.tsx` - Panel de administración

#### Componentes UI Disponibles:
- `@/components/ui/SimpleModal` - Modales con wow factor
- `@/components/ui/button` - Botones con estilos modernos
- `@/components/ui/card` - Cards con glassmorphism
- `@/components/ui/badge` - Badges con gradientes
- `@/components/ui/development-banner` - Banner de desarrollo

### 6. Añadir al Menú para Validación y Demo

**IMPORTANTE:** Todas las pantallas migradas DEBEN añadirse al menú para poder validarlas y verlas como demo.

#### Ubicación del Menú:
- **Archivo:** `app/config/modules.ts`
- **Función:** `getMenuByModule(module: string): MenuItem[]`

#### Pasos para Añadir al Menú:

1. **Identificar el módulo correspondiente** en `modulesConfig`
2. **Buscar la función `getMenuByModule`** y el case del módulo
3. **Añadir la entrada del menú** con la siguiente estructura:

```typescript
{
  name: t("menu.governance.screen_name", "Nombre de la Pantalla"),
  href: "/governance/screen_path",
  icon: IconName, // Icono de lucide-react
  roles: ["admin", "developer", ...], // Roles que pueden acceder
}
```

#### Ejemplo de Entrada en el Menú:

```typescript
// En app/config/modules.ts
case "governance":
  return [
    // ... otras entradas existentes ...
    {
      name: t("menu.governance.screen_name", "Nombre de la Pantalla"),
      href: "/governance/screen_path",
      icon: Brain, // o el icono apropiado
      roles: ["admin", "developer", "viewer"],
    },
  ];
```

#### Verificar en el Menú:

1. **Abrir la aplicación** en el navegador
2. **Navegar al módulo** correspondiente en el sidebar
3. **Verificar que la entrada aparece** en el menú
4. **Hacer clic y verificar** que la página carga correctamente
5. **Validar funcionalidad** y estilos "Wow Factor"

#### Notas Importantes:

- ✅ **Añadir TODAS las pantallas** al menú, incluso si son subpáginas
- ✅ **Usar traducciones** del archivo `app/config/i18n.ts`
- ✅ **Asignar roles apropiados** según la funcionalidad
- ✅ **Usar iconos consistentes** con el módulo
- ✅ **Mantener estructura jerárquica** si hay submenús

#### Traducciones Requeridas:

Añadir en `app/config/i18n.ts`:

```typescript
menu: {
  governance: {
    screen_name: {
      es: "Nombre en Español",
      en: "Name in English",
    },
  },
},
```

---

## Pantallas a Migrar (1)


### 1. governance-overview
- **Archivo ZUL:** `console/gobierno/governance/governance-overview.zul`
- **ViewModel Asociado:** [Identificar en el código ZUL]
- **Servicio Backend:** [Identificar del ViewModel]
- **Entidad JPA:** [Identificar del ViewModel]
- **Tipo:** overviews

---

## Estrategia de Migración

### 1. Identificación de Componentes

Para cada pantalla, identificar:
1. **ViewModel Java:** Buscar en `com.codeflowx.govern.viewmodel.governance`
2. **Servicio Backend:** Identificar servicios inyectados con `@WireVariable`
3. **Entidad JPA:** Identificar entidades importadas y usadas
4. **Mock Mode:** Verificar si el ViewModel tiene modo mock

### 2. Patrón de Migración

#### Para Dashboards:
- Usar componentes de visualización (charts, cards, metrics)
- Implementar mock data para métricas
- Crear API routes mock: `/api/governance/dashboard/metrics`

#### Para Overview/Listados:
- Implementar tabla con paginación
- Filtros y búsqueda
- Mock data para lista
- API route: `/api/governance/[entity]/list`

#### Para Detail/CRUD:
- Formulario completo con validaciones
- Estados: create, edit, view
- Mock data para formulario
- API routes: `/api/governance/[entity]/[id]` (GET, POST, PUT, DELETE)

#### Para Forms:
- Formulario simple con validaciones
- Mock data para campos
- API route: `/api/governance/[entity]/submit`

### 3. Configuración de Mock

**Objetivo:** Que cuando se ponga operativa solo haya que desactivar el mock sin revisar cada pantalla

#### Estrategia de Mock Centralizado

**1. Variable de Entorno:**
```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=true
```

**2. Configuración Centralizada en Next.js:**
```typescript
// app/config/mock.ts
export const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true'

// app/lib/api-client.ts
export async function apiCall<T>(
  endpoint: string,
  options?: RequestInit
): Promise<T> {
  if (USE_MOCK) {
    // Cargar mock data desde archivo
    const mockData = await import(`@/mocks${endpoint}.json`)
    return mockData.default as T
  }

  const response = await fetch(`/api${endpoint}`, options)
  if (!response.ok) {
    throw new Error(`API Error: ${response.statusText}`)
  }
  return response.json()
}
```

**3. Estructura de Mock Data:**
```
app/mocks/
├── agents/
│   ├── list.json
│   ├── detail.json
│   └── dashboard.json
└── models/
    ├── list.json
    └── detail.json
```

**4. Uso en Componentes:**
```typescript
// app/(app)/agents/page.tsx
import { apiCall } from '@/lib/api-client'
import type { Agent } from '@/types/agent'

export default async function AgentsPage() {
  const agents = await apiCall<Agent[]>('/agents/list')
  // ... renderizar
}
```

**5. Desactivación de Mock (Operativa):**
```bash
# Solo cambiar esta variable
NEXT_PUBLIC_USE_MOCK=false
```

**6. Referencia ViewModel Java (para entender lógica):**
```java
// Los ViewModels Java usan BusinessService o servicios dedicados
@WireVariable
private AgentService agentService;

// En modo mock (si existe)
private boolean mockMode = false;

@AfterCompose
public void afterCompose() {
    if (System.getenv("MOCK_MODE") != null) {
        mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE"));
    }
    if (mockMode) {
        loadMockData(); // Cargar datos de prueba
    } else {
        loadRealData(); // Llamar a servicios reales
    }
}
```

### 4. Mapeo de Servicios Backend

#### Servicios Java → API Routes Next.js

**Servicios Dedicados (codeflowx.govern.services):**

| Servicio Java | Método | API Route Next.js | Método HTTP |
|---------------|--------|-------------------|-------------|
| `com.codeflowx.govern.service.governance.EntityService.findAll()` | GET List | `/api/governance/[entity]` | GET |
| `com.codeflowx.govern.service.governance.EntityService.findById(id)` | GET Detail | `/api/governance/[entity]/[id]` | GET |
| `com.codeflowx.govern.service.governance.EntityService.create(entity)` | POST | `/api/governance/[entity]` | POST |
| `com.codeflowx.govern.service.governance.EntityService.update(entity)` | PUT | `/api/governance/[entity]/[id]` | PUT |
| `com.codeflowx.govern.service.governance.EntityService.deleteById(id)` | DELETE | `/api/governance/[entity]/[id]` | DELETE |

**BusinessService (genérico):**
- `businessService.findAllEntity(Entity.class, pageParams, criterias)` → `/api/governance/[entity]?page=1&size=10&...`
- `businessService.findById(Entity.class, id)` → `/api/governance/[entity]/[id]`
- `businessService.save(entity)` → `/api/governance/[entity]` (POST/PUT según ID)

**Microservicios (clientes Java):**
- `AgentMonitoringClient.analyzeExecution()` → `/api/agent/analyze-execution` (POST)
- `LLMEvaluationClient.evaluate()` → `/api/llm/evaluate` (POST)
- `PromptGovernanceClient.validate()` → `/api/prompt/validate` (POST)
- Ver sección "Microservicios Java Disponibles" para más detalles

#### Identificación de Servicios por Pantalla

Para cada pantalla ZUL:
1. **Leer el ViewModel asociado** (definido en el ZUL con `viewModel="..."`)
2. **Identificar servicios inyectados:**
   ```java
   @WireVariable
   private AgentService agentService;  // ← Servicio dedicado

   @WireVariable
   private BusinessService businessService;  // ← Servicio genérico

   @WireVariable
   private AIGovernanceClient aiGovernanceClient;  // ← Cliente microservicio
   ```
3. **Identificar métodos llamados:**
   ```java
   agentService.findById(id)  // ← Mapear a GET /api/agents/[id]
   businessService.findAllEntity(Agent.class, ...)  // ← Mapear a GET /api/agents
   ```
4. **Identificar entidades usadas:**
   ```java
   import com.codeflowx.govern.entity.agents.Agent;  // ← Entidad JPA
   ```

### 5. Checklist de Migración

Para cada pantalla:
- [ ] Leer archivo ZUL original
- [ ] Identificar ViewModel asociado
- [ ] Identificar servicios backend usados
- [ ] Identificar entidades JPA usadas
- [ ] Crear página Next.js en estructura correcta
- [ ] Implementar mock data
- [ ] Crear API routes mock
- [ ] Agregar traducciones (español/inglés)
- [ ] Agregar entrada al menú si aplica
- [ ] Verificar linter
- [ ] Documentar estrategia de desactivación de mock

---

## Ejemplo de Migración

### Pantalla: [Nombre de ejemplo]

**ZUL Original:**
- `console/platform/governance/example-overview.zul`

**ViewModel:**
- `com.codeflowx.govern.viewmodel.governance.ExampleOverviewViewModel`

**Servicios:**
- `com.codeflowx.govern.service.governance.ExampleService`

**Entidades:**
- `com.codeflowx.govern.entity.governance.Example`

**Página Next.js:**
- `app/(app)/governance/example/page.tsx`

**API Route Mock:**
- `app/api/governance/example/route.ts`

**Mock Data:**
```typescript
export const mockExampleData = {
  items: [...],
  total: 100,
  page: 1
}
```

**Desactivación de Mock:**
```typescript
// Cambiar en .env.local
NEXT_PUBLIC_USE_MOCK=false
```

---

## Notas Importantes

1. **NO migrar formularios BPMN** - Ya están migrados
2. **Mantener estructura de carpetas** según documentación funcional
3. **Usar mock data consistente** para facilitar testing
4. **Documentar servicios backend** para integración futura
5. **Identificar todas las dependencias** antes de migrar

---

**Generado automáticamente** - Revisar y completar información específica de cada pantalla
