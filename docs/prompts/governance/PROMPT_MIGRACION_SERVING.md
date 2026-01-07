# Prompt de Migración - Módulo Serving

## Contexto

Este prompt está basado en el historial de migración exitosa del módulo Serving de ZUL a Next.js. Captura el proceso, patrones y mejores prácticas utilizados durante la migración de 26 pantallas (10 DETAILS + 16 OVERVIEWS).

## Objetivo

Migrar pantallas del módulo Serving desde ZUL (ZK Framework) a Next.js con:
- Diseño "Wow Factor" (interfaz moderna y atractiva inspirada en Star Trek)
- Mock data para desarrollo independiente del backend
- Traducciones en español e inglés
- Integración completa con el sistema de navegación
- Componentes reutilizables y responsive
- Estrategia de mock centralizada para fácil desactivación

## Estructura de Pantallas Migradas

### DETAILS (10 pantallas)
1. `deployment-instance/detail` - Detalle de instancia de deployment
2. `deployment-log/detail` - Detalle de log de deployment
3. `deployment-metric/detail` - Detalle de métrica de deployment
4. `model/detail` - Detalle de modelo
5. `model-deployment/detail` - Detalle de deployment de modelo
6. `model-metrics/detail` - Detalle de métricas de modelo
7. `model-prediction/detail` - Detalle de predicción de modelo
8. `model-version/detail` - Detalle de versión de modelo
9. `serving-endpoint/detail` - Detalle de endpoint de serving
10. `serving-request/detail` - Detalle de request de serving

### OVERVIEWS (16 pantallas)
1. `deployment-instance` - Listado de instancias de deployment
2. `deployment-log` - Listado de logs de deployment
3. `deployment-metric` - Listado de métricas de deployment
4. `deployment-status` - Estado de deployments
5. `model` - Listado de modelos
6. `model-deployment` - Listado de deployments de modelos
7. `model-metrics` - Listado de métricas de modelos
8. `model-prediction` - Listado de predicciones de modelos
9. `model-version` - Listado de versiones de modelos
10. `serving-endpoint` - Listado de endpoints de serving
11. `serving-request` - Listado de requests de serving
12. `error-analysis` - Análisis de errores
13. `sla-compliance` - Cumplimiento de SLA
14. `cost-breakdown` - Desglose de costos
15. `serving-error-analysis` - Análisis de errores de serving (usando error-analysis)
16. `serving-sla-compliance` - Cumplimiento de SLA de serving (usando sla-compliance)

## Arquitectura del Módulo

### Entidades JPA Identificadas (11)
- **DeploymentInstance** - `com.codeflowx.govern.entity.serving`
- **DeploymentLog** - `com.codeflowx.govern.entity.serving`
- **DeploymentMetric** - `com.codeflowx.govern.entity.serving`
- **ModelDeployment** - `com.codeflowx.govern.entity.serving`
- **ModelMetrics** - `com.codeflowx.govern.entity.serving`
- **ModelPrediction** - `com.codeflowx.govern.entity.serving`
- **ServingEndpoint** - `com.codeflowx.govern.entity.serving`
- **ServingRequest** - `com.codeflowx.govern.entity.serving`
- **DeploymentScope** - `com.codeflowx.govern.entity.serving`
- **DeploymentStatus** - `com.codeflowx.govern.entity.serving`
- **Model** - `com.codeflowx.govern.entity.serving`

### ViewModels Identificados
- **ServingDashboardViewModel** - `com.codeflowx.govern.viewmodel.serving`
  - Servicios: ModelService, ServingPerformanceDashboardService, DeploymentStatusService, EndpointAnalyticsService, ServingSlaComplianceService, ServingErrorAnalysisService, ServingCostBreakdownService

### Microservicios Java Disponibles

Los ViewModels pueden usar los siguientes clientes Java para llamar a microservicios:

1. **AIGovernanceClient** (Factoría)
   - Gateway: `http://api-leka-govern:8000` (K8s) o `http://localhost:8000` (local)
   - Proporciona acceso a múltiples clientes especializados

2. **AgentMonitoringClient**
   - Microservicio: `leka-agent-monitoring` (puerto 8005)
   - Endpoints: `/api/agent/*`

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

## Requisitos Técnicos

### 1. Estilo "Wow Factor"

Todas las pantallas deben incluir el estilo "Wow Factor" inspirado en Star Trek:

```typescript
// Estructura base requerida
<div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
  {/* Partículas animadas de fondo */}
  <div className="absolute inset-0 overflow-hidden pointer-events-none">
    <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
    <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
    <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
  </div>

  {/* Banner de desarrollo */}
  <div className="relative z-10">
    <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
  </div>

  {/* Contenido */}
  <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
    {/* Contenido de la página */}
  </div>
</div>
```

**Características del diseño:**
- ✅ **Gradientes sutiles** en backgrounds (`bg-gradient-to-br`)
- ✅ **Partículas flotantes animadas** en el fondo
- ✅ **Glassmorphism** en banners y cards (`backdrop-blur-md`, `bg-background/60`)
- ✅ **Gradientes en títulos** (`bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent`)
- ✅ **Animaciones suaves** (`animate-pulse`, `transition-all duration-300`)
- ✅ **Cards con efectos hover** (`hover:shadow-3xl`, `hover:border-primary/50`)
- ✅ **Iconos de Lucide React** con colores temáticos
- ✅ **Badges y chips** con estilos modernos
- ✅ **Modales con SimpleModal** (usar `@/components/ui/SimpleModal`)

### 2. Estructura de Componentes

**Imports estándar para DETAIL pages:**
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ArrowLeft, Save, [IconName] } from "lucide-react";
import Link from "next/link";
import { useState } from "react";
```

**Imports estándar para OVERVIEW pages:**
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Plus, Search, [IconName], Edit, Trash2 } from "lucide-react";
import Link from "next/link";
import { useState } from "react";
```

### 3. Mock Data

Cada pantalla debe incluir mock data realista directamente en el componente:

```typescript
// Para OVERVIEW pages
const [data] = useState([
  {
    id: 1,
    field1: "Value 1",
    field2: "Value 2",
    status: "ACTIVE",
  },
  // ... más datos
]);

// Para DETAIL pages
const [formData, setFormData] = useState({
  field1: "",
  field2: "",
  status: "",
  createdat: new Date().toISOString().split("T")[0],
  updatedat: new Date().toISOString().split("T")[0],
});
```

**Estrategia de Mock:**
- Mock data embebido en el componente para desarrollo rápido
- No requiere archivos JSON externos inicialmente
- Fácil de reemplazar cuando se conecten APIs reales

### 4. Internacionalización (i18n)

**Uso de traducciones:**
```typescript
const { t } = useTranslation();

// En el JSX
{t("serving.deploymentInstance.title", "Deployment Instance")}
{t("serving.deploymentInstance.detail.title", "Deployment Instance Detail")}
```

**Agregar traducciones en `app/config/i18n.ts`:**
- Español (es): traducciones completas
- Inglés (en): traducciones completas
- Estructura: `serving.[entidad].[campo]`

### 5. Integración con Menú

**Actualizar `app/config/modules.ts`:**

```typescript
case "serving":
  return [
    {
      name: t("menu.serving.deploymentInstance", "Deployment Instance"),
      href: "/serving/deployment-instance",
      icon: Server,
      roles: ["admin", "developer", "viewer"],
    },
    {
      name: t("menu.serving.model", "Model"),
      href: "/serving/model",
      icon: Brain,
      roles: ["admin", "developer", "viewer"],
    },
    // ... más items del menú
  ];
```

**IMPORTANTE:** Todas las pantallas migradas DEBEN añadirse al menú para poder validarlas y verlas como demo.

## Proceso de Migración

### Paso 1: Análisis
1. Revisar archivos ZUL originales en `console/platform/serving/`
2. Identificar ViewModels asociados en `com.codeflowx.govern.viewmodel.serving`
3. Identificar servicios backend usados (con `@WireVariable`)
4. Identificar entidades JPA usadas
5. Verificar si la pantalla ya existe en Next.js antes de crear

### Paso 2: Verificación de Pantallas Existentes

**ANTES de migrar, verificar si la pantalla ya existe:**

1. **Páginas en `app/(app)/serving/`**
2. **Páginas en `app/serving/`** (raíz)
3. **Entradas en el menú** (`app/config/modules.ts`)

**Si la pantalla YA EXISTE:**
1. ✅ Revisar la página existente
2. ✅ Verificar funcionalidad actual y comparar con el ZUL original
3. ✅ Actualizar campos faltantes según el ViewModel Java
4. ✅ Mantener estilos "Wow Factor" existentes
5. ✅ Verificar entrada en menú
6. ✅ Actualizar traducciones si faltan campos

**Si la pantalla NO EXISTE:**
1. ✅ Crear nueva página siguiendo la estructura de carpetas
2. ✅ Implementar con estilos "Wow Factor"
3. ✅ Agregar entrada al menú
4. ✅ Agregar traducciones

### Paso 3: Creación de Pantallas

**Orden recomendado:**
1. **DETAILS primero**: Crear páginas de detalle con formularios completos
2. **OVERVIEWS después**: Crear listados con tablas, filtros y búsqueda

**Estructura de carpetas:**
```
app/(app)/serving/
├── deployment-instance/
│   ├── page.tsx (OVERVIEW)
│   └── detail/
│       └── page.tsx (DETAIL)
├── deployment-log/
│   ├── page.tsx (OVERVIEW)
│   └── detail/
│       └── page.tsx (DETAIL)
└── ... (más rutas)
```

### Paso 4: Implementación

**Para DETAIL pages:**
1. Crear archivo `detail/page.tsx` en la ruta correspondiente
2. Implementar formulario con todos los campos del ViewModel
3. Agregar validaciones (campos requeridos marcados con `*`)
4. Implementar mock data para formulario
5. Agregar botón de guardar (con handler que loguea por ahora)
6. Agregar botón de volver a la página overview
7. Agregar traducciones
8. Aplicar estilo "Wow Factor"

**Para OVERVIEW pages:**
1. Crear archivo `page.tsx` en la ruta correspondiente
2. Implementar tabla con paginación (mock data)
3. Agregar filtros y búsqueda (funcionalidad básica)
4. Agregar cards de métricas (total, activos, pendientes, etc.)
5. Agregar botón "Nuevo" que navega a detail
6. Agregar acciones en tabla (editar, eliminar)
7. Agregar traducciones
8. Aplicar estilo "Wow Factor"

### Paso 5: Integración
1. Agregar entrada al menú en `app/config/modules.ts`
2. Agregar traducciones en `app/config/i18n.ts`
3. Verificar navegación entre overview y detail
4. Verificar que todas las páginas se renderizan correctamente

### Paso 6: Validación
1. Verificar que todas las pantallas se renderizan correctamente
2. Validar traducciones en ambos idiomas
3. Verificar integración con menú
4. Corregir errores de linting
5. Verificar responsive design

## Patrones de Diseño

### DETAIL Pages

**Estructura:**
```typescript
export default function EntityDetailPage() {
  const { t } = useTranslation();
  const [formData, setFormData] = useState({
    // Campos del formulario
  });

  const handleSave = () => {
    // TODO: Implementar guardado
    console.log("Guardando:", formData);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br...">
      {/* Header con botón volver, título y botón guardar */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Link href="/serving/entity">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="w-4 h-4" />
            </Button>
          </Link>
          <div className="flex items-center gap-3">
            <IconName className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r...">
              {t("serving.entity.detail.title", "Entity Detail")}
            </h1>
          </div>
        </div>
        <Button onClick={handleSave} className="gap-2">
          <Save className="w-4 h-4" />
          {t("common.save", "Guardar")}
        </Button>
      </div>

      {/* Card con formulario */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
        <CardHeader>
          <CardTitle>{t("serving.entity.detail.formTitle", "Información")}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Campos del formulario */}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
```

**Características:**
- Header con botón volver, título con icono y botón guardar
- Formulario en grid de 2 columnas (responsive: 1 col móvil, 2 desktop)
- Campos con Label e Input/Select
- Campos requeridos marcados con `*`
- Validación básica con `required` en inputs

### OVERVIEW Pages

**Estructura:**
```typescript
export default function EntityOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");

  // Mock data
  const [data] = useState([/* ... */]);

  // Métricas
  const metrics = {
    total: data.length,
    active: data.filter((d) => d.status === "ACTIVE").length,
    // ... más métricas
  };

  return (
    <div className="min-h-screen bg-gradient-to-br...">
      {/* Header con título y botón nuevo */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <IconName className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r...">
            {t("serving.entity.title", "Entity")}
          </h1>
        </div>
        <Link href="/serving/entity/detail">
          <Button className="gap-2">
            <Plus className="w-4 h-4" />
            {t("serving.entity.register", "Registrar Entity")}
          </Button>
        </Link>
      </div>

      {/* Cards de métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {/* Cards con métricas */}
      </div>

      {/* Filtros */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardContent className="p-4">
          <div className="flex flex-wrap gap-4 items-center">
            {/* Búsqueda y filtros */}
          </div>
        </CardContent>
      </Card>

      {/* Tabla */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
        <CardHeader>
          <CardTitle>{t("serving.entity.list", "Lista")}</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            {/* Tabla con datos */}
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
```

**Características:**
- Header con título y botón "Nuevo"
- Cards de métricas (4 columnas responsive)
- Barra de búsqueda y filtros
- Tabla con datos mock
- Acciones en cada fila (editar, eliminar)
- Badges para estados con colores

## Componentes Reutilizables

Utilizar componentes de `@/components/ui/`:
- `Card`, `CardContent`, `CardHeader`, `CardTitle`
- `Badge` (con variantes: default, secondary, outline, destructive)
- `Button` (con variantes: default, outline, ghost)
- `Input`, `Label`
- `Select`, `SelectContent`, `SelectItem`, `SelectTrigger`, `SelectValue`
- `Table`, `TableBody`, `TableCell`, `TableHead`, `TableHeader`, `TableRow`
- `DevelopmentBanner`

## Mapeo de Servicios Backend

### Servicios Java → API Routes Next.js

**Servicios Dedicados:**
| Servicio Java | Método | API Route Next.js | Método HTTP |
|---------------|--------|-------------------|-------------|
| `EntityService.findAll()` | GET List | `/api/serving/[entity]` | GET |
| `EntityService.findById(id)` | GET Detail | `/api/serving/[entity]/[id]` | GET |
| `EntityService.create(entity)` | POST | `/api/serving/[entity]` | POST |
| `EntityService.update(entity)` | PUT | `/api/serving/[entity]/[id]` | PUT |
| `EntityService.deleteById(id)` | DELETE | `/api/serving/[entity]/[id]` | DELETE |

**BusinessService (genérico):**
- `businessService.findAllEntity(Entity.class, pageParams, criterias)` → `/api/serving/[entity]?page=1&size=10&...`
- `businessService.findById(Entity.class, id)` → `/api/serving/[entity]/[id]`
- `businessService.save(entity)` → `/api/serving/[entity]` (POST/PUT según ID)

**Microservicios (clientes Java):**
- `AgentMonitoringClient.analyzeExecution()` → `/api/agent/analyze-execution` (POST)
- `LLMEvaluationClient.evaluate()` → `/api/llm/evaluate` (POST)
- `ModelWrapperClient.*()` → `/api/models/*`

## Checklist de Tareas

### Por cada pantalla DETAIL:
- [ ] Crear archivo `detail/page.tsx` en la ruta correcta
- [ ] Implementar formulario con todos los campos del ViewModel
- [ ] Agregar validaciones (campos requeridos con `*`)
- [ ] Implementar mock data para formulario
- [ ] Agregar botón de guardar (handler que loguea por ahora)
- [ ] Agregar botón de volver a overview
- [ ] Aplicar estilo "Wow Factor" (gradientes, animaciones, efectos)
- [ ] Agregar traducciones en español e inglés
- [ ] Integrar componente `DevelopmentBanner`
- [ ] Implementar responsive design
- [ ] Agregar iconos apropiados de Lucide React
- [ ] Validar que no hay errores de linting

### Por cada pantalla OVERVIEW:
- [ ] Crear archivo `page.tsx` en la ruta correcta
- [ ] Implementar tabla con paginación (mock data)
- [ ] Agregar filtros y búsqueda (funcionalidad básica)
- [ ] Agregar cards de métricas (total, activos, pendientes, etc.)
- [ ] Agregar botón "Nuevo" que navega a detail
- [ ] Agregar acciones en tabla (editar, eliminar)
- [ ] Aplicar estilo "Wow Factor" (gradientes, animaciones, efectos)
- [ ] Agregar traducciones en español e inglés
- [ ] Integrar componente `DevelopmentBanner`
- [ ] Implementar responsive design
- [ ] Agregar iconos apropiados de Lucide React
- [ ] Validar que no hay errores de linting

### Integración global:
- [ ] Actualizar `app/config/modules.ts` con todas las rutas
- [ ] Agregar todas las traducciones en `app/config/i18n.ts`
- [ ] Verificar permisos y roles en el menú
- [ ] Verificar navegación entre overview y detail
- [ ] Verificar que todas las páginas se renderizan correctamente

## Ejemplo Completo: DETAIL Page

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ArrowLeft, Save, Server } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function DeploymentInstanceDetailPage() {
  const { t } = useTranslation();
  const [formData, setFormData] = useState({
    deploymentid: "",
    instanceid: "",
    instancename: "",
    instancetype: "",
    status: "",
    ipaddress: "",
    port: "",
    nodename: "",
    resourceusage: "",
    startedat: "",
    lastheartbeat: "",
    createdat: new Date().toISOString().split("T")[0],
    updatedat: new Date().toISOString().split("T")[0],
  });

  const handleSave = () => {
    console.log("Guardando:", formData);
  };

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
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Link href="/serving/deployment-instance">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div className="flex items-center gap-3">
              <Server className="w-8 h-8 text-primary" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("serving.deploymentInstance.detail.title", "Deployment Instance")}
              </h1>
            </div>
          </div>
          <Button onClick={handleSave} className="gap-2">
            <Save className="w-4 h-4" />
            {t("common.save", "Guardar")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
          <CardHeader>
            <CardTitle>
              {t("serving.deploymentInstance.detail.formTitle", "Información del Deployment Instance")}
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2">
                <Label htmlFor="instanceid">
                  {t("serving.deploymentInstance.instanceid", "Instance ID")} *
                </Label>
                <Input
                  id="instanceid"
                  value={formData.instanceid}
                  onChange={(e) =>
                    setFormData({ ...formData, instanceid: e.target.value })
                  }
                  maxLength={100}
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="instancename">
                  {t("serving.deploymentInstance.instancename", "Instance Name")} *
                </Label>
                <Input
                  id="instancename"
                  value={formData.instancename}
                  onChange={(e) =>
                    setFormData({ ...formData, instancename: e.target.value })
                  }
                  maxLength={100}
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="status">
                  {t("serving.deploymentInstance.status", "Status")} *
                </Label>
                <Select
                  value={formData.status}
                  onValueChange={(value) =>
                    setFormData({ ...formData, status: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar estado" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ACTIVE">Activo</SelectItem>
                    <SelectItem value="INACTIVE">Inactivo</SelectItem>
                    <SelectItem value="PENDING">Pendiente</SelectItem>
                    <SelectItem value="REJECTED">Rechazado</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
```

## Ejemplo Completo: OVERVIEW Page

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Plus, Search, Server, Edit, Trash2 } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function DeploymentInstanceOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");

  const [data] = useState([
    {
      id: 1,
      deploymentid: "DEP-001",
      instanceid: "INST-001",
      instancename: "Instance 1",
      status: "ACTIVE",
    },
    {
      id: 2,
      deploymentid: "DEP-002",
      instanceid: "INST-002",
      instancename: "Instance 2",
      status: "INACTIVE",
    },
  ]);

  const metrics = {
    total: data.length,
    active: data.filter((d) => d.status === "ACTIVE").length,
    inactive: data.filter((d) => d.status === "INACTIVE").length,
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, string> = {
      ACTIVE: "default",
      INACTIVE: "secondary",
      PENDING: "outline",
      REJECTED: "destructive",
    };
    return (
      <Badge variant={variants[status] as any}>
        {status}
      </Badge>
    );
  };

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
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Server className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("serving.deploymentInstance.title", "Deployment Instance")}
            </h1>
          </div>
          <Link href="/serving/deployment-instance/detail">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("serving.deploymentInstance.register", "Registrar Deployment Instance")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-gradient-to-br from-blue-500/20 to-cyan-500/20 border-border/50">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Total</p>
              <p className="text-3xl font-bold">{metrics.total}</p>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-green-500/20 to-emerald-500/20 border-border/50">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Activos</p>
              <p className="text-3xl font-bold">{metrics.active}</p>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-pink-500/20 to-rose-500/20 border-border/50">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Inactivos</p>
              <p className="text-3xl font-bold">{metrics.inactive}</p>
            </CardContent>
          </Card>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="p-4">
            <div className="flex flex-wrap gap-4 items-center">
              <div className="flex-1 min-w-[200px]">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <Input
                    placeholder={t("common.search", "Buscar...")}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-[200px]">
                  <SelectValue placeholder="Filtrar por Status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">TODOS</SelectItem>
                  <SelectItem value="ACTIVE">ACTIVO</SelectItem>
                  <SelectItem value="INACTIVE">INACTIVO</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </CardContent>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
          <CardHeader>
            <CardTitle>{t("serving.deploymentInstance.list", "Lista")}</CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>ID</TableHead>
                  <TableHead>Deployment ID</TableHead>
                  <TableHead>Instance ID</TableHead>
                  <TableHead>Instance Name</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Acciones</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {data.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell>{item.id}</TableCell>
                    <TableCell>{item.deploymentid}</TableCell>
                    <TableCell>{item.instanceid}</TableCell>
                    <TableCell>{item.instancename}</TableCell>
                    <TableCell>{getStatusBadge(item.status)}</TableCell>
                    <TableCell>
                      <div className="flex gap-2">
                        <Link href={`/serving/deployment-instance/detail?id=${item.id}`}>
                          <Button variant="ghost" size="icon">
                            <Edit className="w-4 h-4" />
                          </Button>
                        </Link>
                        <Button variant="ghost" size="icon">
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
```

## Estructura Final Creada

```
app/(app)/serving/
├── deployment-instance/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── deployment-log/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── deployment-metric/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── deployment-status/
│   └── page.tsx (OVERVIEW)
├── model/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── model-deployment/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── model-metrics/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── model-prediction/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── model-version/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── serving-endpoint/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── serving-request/
│   ├── page.tsx (OVERVIEW)
│   └── detail/page.tsx
├── error-analysis/
│   └── page.tsx (OVERVIEW)
├── sla-compliance/
│   └── page.tsx (OVERVIEW)
└── cost-breakdown/
    └── page.tsx (OVERVIEW)
```

## Notas Finales

- **Prioridad**: DETAILS → OVERVIEWS
- **Mock Data**: Embebido en componentes para desarrollo rápido
- **Responsive**: Todas las pantallas deben funcionar en móvil, tablet y desktop
- **Accesibilidad**: Usar semántica HTML correcta y ARIA labels cuando sea necesario
- **Performance**: Usar `"use client"` solo cuando sea necesario para interactividad
- **Menú**: TODAS las pantallas deben estar en el menú para validación y demo
- **Traducciones**: Completas en español e inglés

## Resultado Esperado

Al finalizar la migración:
- ✅ 26 pantallas migradas (10 DETAILS + 16 OVERVIEWS)
- ✅ Todas con diseño "Wow Factor"
- ✅ Mock data implementado en componentes
- ✅ Traducciones completas (es/en)
- ✅ Integración completa con menú y navegación
- ✅ Sin errores de linting
- ✅ Listas para desarrollo frontend independiente del backend
- ✅ Fácil conexión a APIs reales cuando estén disponibles

## Próximos Pasos (Post-Migración)

1. **Conectar APIs reales**: Cuando se desactive el modo mock, las páginas están listas para conectarse a los endpoints reales
2. **Añadir más campos**: Según los ViewModels Java originales
3. **Implementar validaciones**: Reglas de negocio específicas
4. **Añadir gráficos**: Visualizaciones para métricas y análisis
5. **Implementar paginación real**: Cuando se conecten las APIs
6. **Añadir filtros avanzados**: Según necesidades de negocio
