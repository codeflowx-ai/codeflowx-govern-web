# Prompt de Migración - Prompts - OVERVIEWS

## Contexto del Módulo

**Módulo:** Prompts
**Tipo de Pantallas:** overviews
**Total de Pantallas:** 2

## Documentación Funcional Asociada

**Documento:** `docs/funcional/prompts/01_REORGANIZACION_PANTALLAS_PROMPTS.md`

**Ubicación:** `suinsit.nova.web/docs/funcional/prompts/01_REORGANIZACION_PANTALLAS_PROMPTS.md`

### Resumen de la Documentación

# 📱 REORGANIZACIÓN DE PANTALLAS - MÓDULO PROMPTS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Reorganizar pantallas de prompts siguiendo estructura Next.js original

---

## 🎯 OBJETIVO

Reorganizar las 14 pantallas ZUL del módulo de prompts para seguir la estructura funcional del proyecto Next.js original, separando claramente CRUD vs Consulta.

---

## 📊 ESTRUCTURA ACTUAL vs OBJETIVO

### **Estructura Actual (ZKoss):**
```
console/platform/prompts/
├── 3 pantallas *-detail.zul      # CRUD
├── 11 pantallas *-overview.zul   # Consulta
└── Total: 14 pantallas
```

### **Estructura Objetivo (Next.js Style):**
```
console/platform/prompts/
├── overview/                     # Vista general
├── registry/                     # Registro
├── approval/                     # Aprobación
├── ethics/                       # Ética
├── security/                     # Seguridad
├── templates/                    # Plantillas
├── validation/                   # Validación
├── versioning/                   # Versionado
└── rollback/                     # Rollback
```

---

## 🔄 MAPEO DE REORGANIZACIÓN

### **1. OVERVIEW (Vista General)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-overview.zul` | `prompts/overview/page.zul` | Consulta | `PromptsOverviewViewModel` |
| `prompts-overview-overview.zul` | `prompts/overview/summary.zul` | Consulta | `PromptsOverviewViewModel` |

### **2. REGISTRY (Registro)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-detail.zul` | `prompts/registry/page.zul` | CRUD | `PromptsDetailViewModel` |

### **3. VALIDATION (Validación)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-validation-detail.zul` | `prompts/validation/page.zul` | CRUD | `PromptValidationDetailViewModel` |
| `prompt-validation-overview.zul` | `prompts/validation/overview.zul` | Consulta | `PromptValidationOverviewViewModel` |

### **4. VERSIONING (Versionado)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-version-detail.zul` | `prompts/versioning/page.zul` | CRUD | `PromptVersionDetailViewModel` |
| `prompt-version-overview.zul` | `prompts/versioning/overview.zul` | Consulta | `PromptVersionOverviewViewModel` |
| `prompt-version-history-overview.zul` | `prompts/versioning/history.zul` | Consulta | `PromptVersionHistoryOverviewViewModel` |

### **5. TEMPLATES (Plantillas y Análisis)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-cost-analysis-overview.zul` | `prompts/templates/cost-analysis.zul` | Consulta | `PromptCostAnalysisOverviewViewModel` |
| `prompt-optimization-opportunities-overview.zul` | `prompts/templates/optimization.zul` | Consulta | `PromptOptimization

---

## Arquitectura del Módulo

### ViewModels Identificados (3)


#### PromptApprovalWorkflowViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.prompts`
- **Archivo:** `com/codeflowx/govern/viewmodel/prompts/PromptApprovalWorkflowViewModel.java`
- **Servicios Usados:** PromptService, PromptVersionService
- **Entidades Usadas:** Prompt, PromptVersion
- **Mock Mode:** ❌ No

#### PromptsDetailViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.prompts`
- **Archivo:** `com/codeflowx/govern/viewmodel/prompts/PromptsDetailViewModel.java`
- **Servicios Usados:** PromptService, PromptVersionService, PromptValidationService, BusinessService
- **Entidades Usadas:** Prompt, PromptValidation, PromptVersion
- **Mock Mode:** ❌ No

#### PromptsOverviewViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.prompts`
- **Archivo:** `com/codeflowx/govern/viewmodel/prompts/PromptsOverviewViewModel.java`
- **Servicios Usados:** PromptService, PromptsOverviewService, PromptsMetricsSummaryService, BusinessService
- **Entidades Usadas:** N/A
- **Mock Mode:** ❌ No

### Servicios Backend Disponibles (0)


### Entidades JPA (4)

- **Prompt** - `com.codeflowx.govern.entity.prompts`
- **PromptApproval** - `com.codeflowx.govern.entity.prompts`
- **PromptValidation** - `com.codeflowx.govern.entity.prompts`
- **PromptVersion** - `com.codeflowx.govern.entity.prompts`

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

**Total encontradas:** 1

- `app/(app)/generation/page.tsx`

### 2. Páginas Existentes en `app/` (raíz)

**No se encontraron páginas existentes**


### 3. Entradas en el Menú (`app/config/modules.ts`)

**Total encontradas:** 1

- `/projects/generation`

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
  name: t("menu.prompts.screen_name", "Nombre de la Pantalla"),
  href: "/prompts/screen_path",
  icon: IconName, // Icono de lucide-react
  roles: ["admin", "developer", ...], // Roles que pueden acceder
}
```

#### Ejemplo de Entrada en el Menú:

```typescript
// En app/config/modules.ts
case "prompts":
  return [
    // ... otras entradas existentes ...
    {
      name: t("menu.prompts.screen_name", "Nombre de la Pantalla"),
      href: "/prompts/screen_path",
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
  prompts: {
    screen_name: {
      es: "Nombre en Español",
      en: "Name in English",
    },
  },
},
```

---

## Pantallas a Migrar (2)


### 1. overview
- **Archivo ZUL:** `console/platform/prompts/validation/overview.zul`
- **ViewModel Asociado:** [Identificar en el código ZUL]
- **Servicio Backend:** [Identificar del ViewModel]
- **Entidad JPA:** [Identificar del ViewModel]
- **Tipo:** overviews

### 2. overview
- **Archivo ZUL:** `console/platform/prompts/versioning/overview.zul`
- **ViewModel Asociado:** [Identificar en el código ZUL]
- **Servicio Backend:** [Identificar del ViewModel]
- **Entidad JPA:** [Identificar del ViewModel]
- **Tipo:** overviews

---

## Estrategia de Migración

### 1. Identificación de Componentes

Para cada pantalla, identificar:
1. **ViewModel Java:** Buscar en `com.codeflowx.govern.viewmodel.prompts`
2. **Servicio Backend:** Identificar servicios inyectados con `@WireVariable`
3. **Entidad JPA:** Identificar entidades importadas y usadas
4. **Mock Mode:** Verificar si el ViewModel tiene modo mock

### 2. Patrón de Migración

#### Para Dashboards:
- Usar componentes de visualización (charts, cards, metrics)
- Implementar mock data para métricas
- Crear API routes mock: `/api/prompts/dashboard/metrics`

#### Para Overview/Listados:
- Implementar tabla con paginación
- Filtros y búsqueda
- Mock data para lista
- API route: `/api/prompts/[entity]/list`

#### Para Detail/CRUD:
- Formulario completo con validaciones
- Estados: create, edit, view
- Mock data para formulario
- API routes: `/api/prompts/[entity]/[id]` (GET, POST, PUT, DELETE)

#### Para Forms:
- Formulario simple con validaciones
- Mock data para campos
- API route: `/api/prompts/[entity]/submit`

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
| `com.codeflowx.govern.service.prompts.EntityService.findAll()` | GET List | `/api/prompts/[entity]` | GET |
| `com.codeflowx.govern.service.prompts.EntityService.findById(id)` | GET Detail | `/api/prompts/[entity]/[id]` | GET |
| `com.codeflowx.govern.service.prompts.EntityService.create(entity)` | POST | `/api/prompts/[entity]` | POST |
| `com.codeflowx.govern.service.prompts.EntityService.update(entity)` | PUT | `/api/prompts/[entity]/[id]` | PUT |
| `com.codeflowx.govern.service.prompts.EntityService.deleteById(id)` | DELETE | `/api/prompts/[entity]/[id]` | DELETE |

**BusinessService (genérico):**
- `businessService.findAllEntity(Entity.class, pageParams, criterias)` → `/api/prompts/[entity]?page=1&size=10&...`
- `businessService.findById(Entity.class, id)` → `/api/prompts/[entity]/[id]`
- `businessService.save(entity)` → `/api/prompts/[entity]` (POST/PUT según ID)

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
- `console/platform/prompts/example-overview.zul`

**ViewModel:**
- `com.codeflowx.govern.viewmodel.prompts.ExampleOverviewViewModel`

**Servicios:**
- `com.codeflowx.govern.service.prompts.ExampleService`

**Entidades:**
- `com.codeflowx.govern.entity.prompts.Example`

**Página Next.js:**
- `app/(app)/prompts/example/page.tsx`

**API Route Mock:**
- `app/api/prompts/example/route.ts`

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
