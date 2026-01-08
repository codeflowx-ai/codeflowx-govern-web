# Prompt de Migración - Projects - DASHBOARDS

## Contexto del Módulo

**Módulo:** Projects
**Tipo de Pantallas:** dashboards
**Total de Pantallas:** 1

## Documentación Funcional Asociada

**Documento:** `docs/funcional/projects/01_REORGANIZACION_PANTALLAS_PROJECTS.md`

**Ubicación:** `suinsit.nova.web/docs/funcional/projects/01_REORGANIZACION_PANTALLAS_PROJECTS.md`

### Resumen de la Documentación

# 📊 PROJECTS - REORGANIZACIÓN DE PANTALLAS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Análisis y reorganización de pantallas del módulo Projects

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Projects** gestiona **proyectos, recursos, costos y portfolios** de la organización. Se han identificado y analizado **49 pantallas ZUL** distribuidas en **3 directorios** principales.

### **Distribución de Pantallas:**
- **Platform Projects:** 46 pantallas (gestión completa)
- **Gobierno Projects:** 3 pantallas (governance)
- **StepApp DevOp:** 2 pantallas (desarrollo)

### **Total:** 49 pantallas ZUL implementadas

---

## 📁 ESTRUCTURA ACTUAL DE DIRECTORIOS

```
webapp/console/
├── platform/projects/          (46 pantallas)
│   ├── Core/                   (4 pantallas)
│   ├── Team/                   (4 pantallas)
│   ├── Resources/              (6 pantallas)
│   ├── Financial/              (14 pantallas)
│   ├── Planning/               (8 pantallas)
│   ├── Technology/             (6 pantallas)
│   └── Management/             (4 pantallas)
├── gobierno/projects/          (3 pantallas)
│   └── Governance/             (3 pantallas)
└── stepApp/devop/             (2 pantallas)
    └── Development/            (2 pantallas)
```

---

## 📊 ANÁLISIS DETALLADO POR MÓDULO

### **1. CORE - Gestión Básica de Proyectos**
**Directorio:** `platform/projects/`  
**Pantallas:** 4

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-overview.zul` | Vista general de proyectos |
| 2 | `project-detail.zul` | Detalle completo de proyecto |
| 3 | `project-domain-overview.zul` | Dominios de proyectos |
| 4 | `project-domain-detail.zul` | Detalle de dominios |

**Funcionalidades:**
- ✅ CRUD completo de proyectos
- ✅ Gestión de dominios
- ✅ Información general
- ✅ Dashboard ejecutivo

---

### **2. TEAM - Gestión de Equipo**
**Directorio:** `platform/projects/`  
**Pantallas:** 4

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-member-overview.zul` | Miembros del equipo |
| 2 | `project-member-detail.zul` | Detalle de miembro |
| 3 | `project-resource-allocation-overview.zul` | Asignación de recursos |
| 4 | `project-time-tracking-overview.zul` | Seguimiento de tiempo |

**Funcionalidades:**
- ✅ Gestión de miembros
- ✅ Asignación de recursos
- ✅ Tracking de tiempo
- ✅ Roles y permisos

---

### **3. RESOURCES - Gestión de Recursos**
**Directorio:** `platform/projects/`  
**Pantallas:** 6

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-resource-consumption-overview.zul` | Consumo de recursos |
| 2 | `project-resource-consumption-detail.zul` | Detalle de consumo |
| 3 | `project-artifact-overview.zul` | Artefactos del proyecto |
| 4 | `project-artifact-detail.zul` | Detalle de artefactos |
| 5 | `project-document-overview.zul` | Documentos del proyecto |
| 6 | `project-document-detail.zul` | Detalle de documentos |

**Funcionalidades:**
- ✅ Monitoreo de consumo
- ✅ Gestión de artefactos
- ✅ 

---

## Arquitectura del Módulo

### ViewModels Identificados (2)


#### ProjectAIInventoryViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.projects`
- **Archivo:** `com/codeflowx/govern/viewmodel/projects/ProjectAIInventoryViewModel.java`
- **Servicios Usados:** N/A
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

#### ProjectsDashboardViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.projects`
- **Archivo:** `com/codeflowx/govern/viewmodel/projects/ProjectsDashboardViewModel.java`
- **Servicios Usados:** ModelService, ProjectPortfolioDashboardService, ProjectFinancialSummaryService, ProjectResourceAllocationService, ProjectRiskAssessmentService, ProjectCostBreakdownService
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

### Servicios Backend Disponibles (0)


### Entidades JPA (18)

- **Project** - `com.codeflowx.govern.entity.projects`
- **ProjectArtifact** - `com.codeflowx.govern.entity.projects`
- **ProjectBillingDetail** - `com.codeflowx.govern.entity.projects`
- **ProjectCostEstimator** - `com.codeflowx.govern.entity.projects`
- **ProjectDocument** - `com.codeflowx.govern.entity.projects`
- **ProjectDomain** - `com.codeflowx.govern.entity.projects`
- **ProjectInvoice** - `com.codeflowx.govern.entity.projects`
- **ProjectLicense** - `com.codeflowx.govern.entity.projects`
- **ProjectMember** - `com.codeflowx.govern.entity.projects`
- **ProjectRequirement** - `com.codeflowx.govern.entity.projects`

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

**Total encontradas:** 5

- `app/(app)/projects/page.tsx`
- `app/(app)/projects/community/page.tsx`
- `app/(app)/projects/conversational/page.tsx`
- `app/(app)/projects/generation/page.tsx`
- `app/(app)/projects/conversational/new/page.tsx`

### 2. Páginas Existentes en `app/` (raíz)

**No se encontraron páginas existentes**


### 3. Entradas en el Menú (`app/config/modules.ts`)

**Total encontradas:** 6

- `/projects`
- `/projects/conversational`
- `/projects/conversational/new`
- `/projects/generation`
- `/rag/projects`
- `/projects/community`

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
  name: t("menu.projects.screen_name", "Nombre de la Pantalla"),
  href: "/projects/screen_path",
  icon: IconName, // Icono de lucide-react
  roles: ["admin", "developer", ...], // Roles que pueden acceder
}
```

#### Ejemplo de Entrada en el Menú:

```typescript
// En app/config/modules.ts
case "projects":
  return [
    // ... otras entradas existentes ...
    {
      name: t("menu.projects.screen_name", "Nombre de la Pantalla"),
      href: "/projects/screen_path",
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
  projects: {
    screen_name: {
      es: "Nombre en Español",
      en: "Name in English",
    },
  },
},
```

---

## Pantallas a Migrar (1)


### 1. project-portfolio-dashboard-overview
- **Archivo ZUL:** `console/platform/projects/project-portfolio-dashboard-overview.zul`
- **ViewModel Asociado:** [Identificar en el código ZUL]
- **Servicio Backend:** [Identificar del ViewModel]
- **Entidad JPA:** [Identificar del ViewModel]
- **Tipo:** dashboards

---

## Estrategia de Migración

### 1. Identificación de Componentes

Para cada pantalla, identificar:
1. **ViewModel Java:** Buscar en `com.codeflowx.govern.viewmodel.projects`
2. **Servicio Backend:** Identificar servicios inyectados con `@WireVariable`
3. **Entidad JPA:** Identificar entidades importadas y usadas
4. **Mock Mode:** Verificar si el ViewModel tiene modo mock

### 2. Patrón de Migración

#### Para Dashboards:
- Usar componentes de visualización (charts, cards, metrics)
- Implementar mock data para métricas
- Crear API routes mock: `/api/projects/dashboard/metrics`

#### Para Overview/Listados:
- Implementar tabla con paginación
- Filtros y búsqueda
- Mock data para lista
- API route: `/api/projects/[entity]/list`

#### Para Detail/CRUD:
- Formulario completo con validaciones
- Estados: create, edit, view
- Mock data para formulario
- API routes: `/api/projects/[entity]/[id]` (GET, POST, PUT, DELETE)

#### Para Forms:
- Formulario simple con validaciones
- Mock data para campos
- API route: `/api/projects/[entity]/submit`

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
| `com.codeflowx.govern.service.projects.EntityService.findAll()` | GET List | `/api/projects/[entity]` | GET |
| `com.codeflowx.govern.service.projects.EntityService.findById(id)` | GET Detail | `/api/projects/[entity]/[id]` | GET |
| `com.codeflowx.govern.service.projects.EntityService.create(entity)` | POST | `/api/projects/[entity]` | POST |
| `com.codeflowx.govern.service.projects.EntityService.update(entity)` | PUT | `/api/projects/[entity]/[id]` | PUT |
| `com.codeflowx.govern.service.projects.EntityService.deleteById(id)` | DELETE | `/api/projects/[entity]/[id]` | DELETE |

**BusinessService (genérico):**
- `businessService.findAllEntity(Entity.class, pageParams, criterias)` → `/api/projects/[entity]?page=1&size=10&...`
- `businessService.findById(Entity.class, id)` → `/api/projects/[entity]/[id]`
- `businessService.save(entity)` → `/api/projects/[entity]` (POST/PUT según ID)

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
- `console/platform/projects/example-overview.zul`

**ViewModel:**
- `com.codeflowx.govern.viewmodel.projects.ExampleOverviewViewModel`

**Servicios:**
- `com.codeflowx.govern.service.projects.ExampleService`

**Entidades:**
- `com.codeflowx.govern.entity.projects.Example`

**Página Next.js:**
- `app/(app)/projects/example/page.tsx`

**API Route Mock:**
- `app/api/projects/example/route.ts`

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
