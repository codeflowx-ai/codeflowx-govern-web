# Prompt de Migración - Módulo Projects

## Contexto

Este prompt está basado en el historial de migración exitosa del módulo Projects de ZUL a Next.js. Captura el proceso, patrones y mejores prácticas utilizados durante la migración de 46 pantallas.

## Objetivo

Migrar pantallas del módulo Projects desde ZUL (ZK Framework) a Next.js con:
- Diseño "Wow Factor" (interfaz moderna y atractiva)
- Mock data para desarrollo independiente del backend
- Traducciones en español e inglés
- Integración completa con el sistema de navegación
- Componentes reutilizables y responsive

## Estructura de Pantallas a Migrar

### Dashboards (1 pantalla)
- Portfolio Dashboard

### Overviews (27 pantallas)
- Lista de Proyectos
- Dominios
- Miembros
- Artefactos
- Documentos
- Facturas
- Requisitos
- Tareas
- Resumen Financiero
- Asignación de Recursos
- Desglose de Costos
- Evaluación de Riesgos
- Análisis de ROI
- Seguimiento de Tiempo
- Estimadores de Costos
- Licencias
- Consumo de Recursos
- ROI
- Stacks
- Tecnologías
- Tokens
- Versiones
- Estado de Facturación
- Detalles de Facturación
- Timeline Gantt
- Análisis de Rentabilidad de Clientes
- Reporte de Antigüedad de Facturas

### Details (18 pantallas)
- Detalle de Proyecto
- Detalle de Dominio
- Detalle de Miembro
- Detalle de Artefacto
- Detalle de Documento
- Detalle de Factura
- Detalle de Requisito
- Detalle de Tarea
- Detalle de Licencia
- Detalle de Estimador de Costos
- Detalle de ROI
- Detalle de Stack
- Detalle de Tecnología
- Detalle de Token
- Detalle de Versión
- Detalle de Seguimiento de Tiempo
- Detalle de Consumo de Recursos
- Detalle de Facturación

## Requisitos Técnicos

### 1. Estilo "Wow Factor"

Todas las pantallas deben incluir:

```typescript
// Fondo con gradiente y efectos visuales
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
</div>
```

**Características del diseño:**
- Cards con `backdrop-blur-md bg-background/60 border-border/50`
- Efectos hover: `hover:shadow-3xl hover:border-primary/50 transition-all duration-300`
- Títulos con gradiente: `bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent`
- Iconos de Lucide React
- Animaciones suaves y transiciones

### 2. Estructura de Componentes

**Imports estándar:**
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { /* iconos necesarios */ } from "lucide-react";
import Link from "next/link";
```

**Estructura de página:**
```typescript
export default function [Nombre]Page() {
  const { t } = useTranslation();

  // Mock data
  const mockData = [/* ... */];

  // Funciones auxiliares
  const getStatusColor = (status: string) => { /* ... */ };

  return (
    <div className="min-h-screen bg-gradient-to-br...">
      {/* Contenido */}
    </div>
  );
}
```

### 3. Mock Data

Cada pantalla debe incluir mock data realista:

```typescript
interface Project {
  id: number;
  name: string;
  description: string;
  status: "active" | "completed" | "on-hold" | "cancelled";
  startDate: string;
  endDate: string;
  budget: number;
  spent: number;
  teamSize: number;
  progress: number;
}

const mockProjects: Project[] = [
  {
    id: 1,
    name: "Proyecto Alpha",
    description: "Sistema de gestión empresarial",
    status: "active",
    startDate: "2024-01-15",
    endDate: "2024-12-31",
    budget: 500000,
    spent: 325000,
    teamSize: 12,
    progress: 65,
  },
  // ... más datos
];
```

### 4. Internacionalización (i18n)

**Uso de traducciones:**
```typescript
const { t } = useTranslation();

// En el JSX
{t("projects.overview.title", "Proyectos")}
{t("projects.status.active", "Activo")}
```

**Agregar traducciones en `app/config/i18n.ts`:**
- Español (es): traducciones completas
- Inglés (en): traducciones completas
- Estructura: `projects.[seccion].[clave]`

### 5. Integración con Menú

**Actualizar `lib/config/modules.ts`:**

```typescript
case "Projects":
  return [
    {
      name: "Portfolio Dashboard",
      href: `/projects/portfolio-dashboard`,
      icon: "BarChart3",
      roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
      isDefault: true,
    },
    {
      name: "Proyectos",
      href: `/projects/list`,
      icon: "FolderKanban",
      roles: [/* roles */],
    },
    // ... más items del menú
  ];
```

## Proceso de Migración

### Paso 1: Análisis
1. Revisar archivos ZUL originales
2. Identificar ViewModels y servicios
3. Entender la estructura de datos
4. Mapear funcionalidades a componentes Next.js

### Paso 2: Creación de Pantallas
1. **Dashboards primero**: Crear dashboards con métricas y KPIs
2. **Overviews**: Crear listas con búsqueda, filtros y cards
3. **Details**: Crear páginas de detalle con información completa

### Paso 3: Implementación
1. Crear archivo `page.tsx` en la ruta correspondiente
2. Implementar mock data
3. Agregar traducciones
4. Integrar en el menú
5. Aplicar estilo "Wow Factor"

### Paso 4: Validación
1. Verificar que todas las pantallas se renderizan correctamente
2. Validar traducciones en ambos idiomas
3. Verificar integración con menú
4. Corregir errores de linting

## Estructura de Archivos

```
app/
└── projects/
    ├── portfolio-dashboard/
    │   └── page.tsx
    ├── list/
    │   └── page.tsx
    ├── [id]/
    │   └── page.tsx
    ├── domains/
    │   ├── list/
    │   │   └── page.tsx
    │   └── [id]/
    │       └── page.tsx
    └── ... (más rutas)
```

## Componentes Reutilizables

Utilizar componentes de `@/components/ui/`:
- `Card`, `CardContent`, `CardHeader`, `CardTitle`
- `Badge` (con variantes: default, secondary, outline, destructive)
- `Button` (con variantes: default, outline, ghost)
- `DevelopmentBanner`

## Patrones de Diseño

### Overview Pages
- Header con título, icono y botón de acción
- Barra de búsqueda y filtros
- Grid de cards (responsive: 1 col móvil, 2 tablet, 3 desktop)
- Cada card muestra información resumida
- Botón "Detalles" que navega a la página de detalle

### Detail Pages
- Header con botón de volver, título y botón de editar
- Layout de 2-3 columnas (información principal + sidebar)
- Cards organizadas por secciones
- Badges para estados
- Progress bars para métricas
- Información de metadata (createdAt, updatedAt)

### Dashboard Pages
- Grid de métricas (KPIs)
- Cards con gráficos (placeholders inicialmente)
- Visualización de datos agregados
- Indicadores de estado

## Checklist de Tareas

### Por cada pantalla:
- [ ] Crear archivo `page.tsx` en la ruta correcta
- [ ] Implementar mock data con interface TypeScript
- [ ] Aplicar estilo "Wow Factor" (gradientes, animaciones, efectos)
- [ ] Agregar traducciones en español e inglés
- [ ] Integrar componente `DevelopmentBanner`
- [ ] Implementar responsive design
- [ ] Agregar iconos apropiados de Lucide React
- [ ] Implementar navegación entre páginas relacionadas
- [ ] Validar que no hay errores de linting
- [ ] Verificar que la página aparece en el menú

### Integración global:
- [ ] Actualizar `lib/config/modules.ts` con todas las rutas
- [ ] Agregar todas las traducciones en `app/config/i18n.ts`
- [ ] Verificar permisos y roles en el menú

## Ejemplo Completo: Overview Page

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { FolderKanban, Plus, Search, Filter, ArrowRight, Users, DollarSign } from "lucide-react";
import Link from "next/link";

interface Project {
  id: number;
  name: string;
  description: string;
  status: "active" | "completed" | "on-hold" | "cancelled";
  startDate: string;
  endDate: string;
  budget: number;
  spent: number;
  teamSize: number;
  progress: number;
}

const mockProjects: Project[] = [
  {
    id: 1,
    name: "Proyecto Alpha",
    description: "Sistema de gestión empresarial",
    status: "active",
    startDate: "2024-01-15",
    endDate: "2024-12-31",
    budget: 500000,
    spent: 325000,
    teamSize: 12,
    progress: 65,
  },
];

const getStatusColor = (status: string) => {
  switch (status) {
    case "active": return "default";
    case "completed": return "secondary";
    case "on-hold": return "outline";
    case "cancelled": return "destructive";
    default: return "outline";
  }
};

export default function ProjectsOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <FolderKanban className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("projects.overview.title", "Proyectos")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("projects.overview.newProject", "Nuevo Proyecto")}
          </Button>
        </div>

        <div className="flex gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input
              type="text"
              placeholder={t("projects.overview.search", "Buscar proyectos...")}
              className="w-full pl-10 pr-4 py-2 bg-background/60 backdrop-blur-md border border-border/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <Button variant="outline">
            <Filter className="w-4 h-4 mr-2" />
            {t("common.filter", "Filtrar")}
          </Button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockProjects.map((project) => (
            <Card
              key={project.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{project.name}</CardTitle>
                    <p className="text-sm text-muted-foreground line-clamp-2">{project.description}</p>
                  </div>
                  <Badge variant={getStatusColor(project.status) as any}>
                    {t(`projects.status.${project.status}`, project.status)}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-muted-foreground">{t("projects.overview.progress", "Progreso")}</span>
                    <span className="font-medium">{project.progress}%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div
                      className="bg-primary h-2 rounded-full transition-all duration-500"
                      style={{ width: `${project.progress}%` }}
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div className="flex items-center gap-2">
                    <Users className="w-4 h-4 text-muted-foreground" />
                    <span>{project.teamSize} {t("projects.overview.members", "miembros")}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <DollarSign className="w-4 h-4 text-muted-foreground" />
                    <span>${(project.spent / 1000).toLocaleString()}K</span>
                  </div>
                </div>

                <Link href={`/projects/${project.id}`}>
                  <Button variant="outline" className="w-full">
                    {t("common.details", "Detalles")}
                    <ArrowRight className="w-4 h-4 ml-2" />
                  </Button>
                </Link>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}
```

## Notas Finales

- **Prioridad**: Dashboards → Overviews → Details
- **Mock Data**: Debe ser realista y variado para demostrar diferentes estados
- **Responsive**: Todas las pantallas deben funcionar en móvil, tablet y desktop
- **Accesibilidad**: Usar semántica HTML correcta y ARIA labels cuando sea necesario
- **Performance**: Usar `"use client"` solo cuando sea necesario para interactividad

## Resultado Esperado

Al finalizar la migración:
- ✅ 46 pantallas migradas (1 dashboard + 27 overviews + 18 details)
- ✅ Todas con diseño "Wow Factor"
- ✅ Mock data implementado
- ✅ Traducciones completas (es/en)
- ✅ Integración completa con menú y navegación
- ✅ Sin errores de linting
- ✅ Listas para desarrollo frontend independiente del backend
