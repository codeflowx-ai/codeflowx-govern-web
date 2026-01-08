# 📘 PROMPT DE MIGRACIÓN - MÓDULO DE INFRAESTRUCTURA

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Developers, DevOps, Architects, IT Teams

---

## 🎯 OBJETIVO

Migrar y crear las páginas del módulo de Infrastructure desde los prompts de migración originales (`MIGRACION_INFRASTRUCTURE_DASHBOARDS.md`, `MIGRACION_INFRASTRUCTURE_DETAILS.md`, `MIGRACION_INFRASTRUCTURE_OVERVIEWS.md`) a componentes React/Next.js con TypeScript, siguiendo los estándares de diseño y arquitectura del proyecto.

---

## 📋 ALCANCE DE LA MIGRACIÓN

### Páginas a Crear

#### 1. Dashboard (1 página)
- `resource-utilization-dashboard/page.tsx` — Dashboard de utilización de recursos con métricas, gráficos y tabla de recursos

#### 2. Details (12 páginas)
- `cloud-credential-detail/page.tsx` — Detalle de credenciales cloud
- `cloud-provider-detail/page.tsx` — Detalle de proveedor cloud
- `cloud-region-detail/page.tsx` — Detalle de región cloud
- `cloud-resource-detail/page.tsx` — Detalle de recurso cloud con información, especificaciones y configuración de escalado
- `gpu-instance-detail/page.tsx` — Detalle de instancia GPU
- `infrastructure-audit-detail/page.tsx` — Detalle de auditoría de infraestructura
- `infrastructure-cost-detail/page.tsx` — Detalle de costos de infraestructura
- `infrastructure-metric-detail/page.tsx` — Detalle de métricas de infraestructura
- `infrastructure-template-detail/page.tsx` — Detalle de plantilla de infraestructura
- `kubernetes-cluster-detail/page.tsx` — Detalle de cluster Kubernetes
- `kubernetes-node-detail/page.tsx` — Detalle de nodo Kubernetes
- `resource-quota-detail/page.tsx` — Detalle de cuota de recursos

#### 3. Overviews (17 páginas)
- `capacity-forecast-overview/page.tsx` — Vista general de pronóstico de capacidad
- `cloud-credential-overview/page.tsx` — Vista general de credenciales cloud
- `cloud-provider-overview/page.tsx` — Vista general de proveedores cloud
- `cloud-region-overview/page.tsx` — Vista general de regiones cloud
- `cloud-resource-overview/page.tsx` — Vista general de recursos cloud con búsqueda y filtros
- `gpu-instance-overview/page.tsx` — Vista general de instancias GPU
- `infrastructure-audit-overview/page.tsx` — Vista general de auditorías de infraestructura
- `infrastructure-cost-analysis-overview/page.tsx` — Vista general de análisis de costos
- `infrastructure-cost-overview/page.tsx` — Vista general de costos de infraestructura
- `infrastructure-health-matrix-overview/page.tsx` — Vista general de matriz de salud
- `infrastructure-metric-overview/page.tsx` — Vista general de métricas
- `infrastructure-metrics-summary-overview/page.tsx` — Vista general de resumen de métricas
- `infrastructure-overview-overview/page.tsx` — Vista general de infraestructura
- `infrastructure-template-overview/page.tsx` — Vista general de plantillas
- `kubernetes-cluster-overview/page.tsx` — Vista general de clusters Kubernetes
- `kubernetes-node-overview/page.tsx` — Vista general de nodos Kubernetes
- `resource-quota-overview/page.tsx` — Vista general de cuotas de recursos

**Total: 30 páginas**

---

## 🎨 ESTÁNDARES DE DISEÑO

### Estilo "Wow Factor"

Todas las páginas deben implementar:

1. **Gradientes Sutiles**
   - Backgrounds con gradientes: `bg-gradient-to-br from-background via-background to-background/80`
   - Títulos con gradientes: `bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent`

2. **Partículas Animadas**
   - Partículas flotantes en el fondo con animación pulse
   - Múltiples partículas con diferentes delays
   - Ejemplo:
   ```tsx
   <div className="absolute inset-0 overflow-hidden pointer-events-none">
     <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
     <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
     <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
   </div>
   ```

3. **Glassmorphism**
   - Cards con efecto glassmorphism: `backdrop-blur-md bg-background/60 border-border/50`
   - DevelopmentBanner con glassmorphism: `backdrop-blur-md bg-background/60 border-border/50`
   - Hover effects: `hover:border-primary/50 transition-all duration-300`

4. **Iconos**
   - Usar iconos de `lucide-react`
   - Iconos contextuales según el tipo de página
   - Tamaño estándar: `w-8 h-8` para títulos, `w-4 h-4` para botones, `w-5 h-5` para cards

5. **Animaciones Suaves**
   - Transiciones en todos los elementos interactivos
   - Progress bars con animaciones: `transition-all duration-500`

### Responsive Design

- Grids adaptativos:
  - Mobile: `grid-cols-1`
  - Tablet: `md:grid-cols-2`
  - Desktop: `lg:grid-cols-3` o `lg:grid-cols-4`
- Container con padding: `container mx-auto px-4 py-6`
- Espaciado consistente: `space-y-6`

---

## 🏗️ ESTRUCTURA DE COMPONENTES

### Estructura Base de Página

```tsx
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { [Iconos] } from "lucide-react";

export default function [Nombre]Page() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas animadas */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        {/* Partículas aquí */}
      </div>

      {/* Development Banner */}
      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      {/* Contenido principal */}
      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        {/* Contenido específico */}
      </div>
    </div>
  );
}
```

### Patrón para Dashboard

- Header con título e icono
- Cards de resumen (métricas principales)
- Gráficos o visualizaciones
- Tabla de recursos con datos mock
- Filtros de tiempo (24h, 7d, 30d)

### Patrón para Details

- Header con botón de regreso (`ArrowLeft`)
- Badge de estado (Active/Inactive)
- Botón de edición
- Grid de 2 columnas (lg:col-span-2 para contenido principal, 1 columna para sidebar)
- Secciones:
  - Basic Information
  - Specifications (JSON en `<pre>`)
  - Scaling Configuration (si aplica)
  - Cost Information (sidebar)
  - Metadata (sidebar)

### Patrón para Overviews

- Header con título, icono y botón "Add"
- Barra de búsqueda con icono `Search`
- Grid de cards (responsive)
- Cada card incluye:
  - Título
  - Badges (tipo, estado)
  - Métricas o información clave
  - Progress bars (si aplica)
  - Botones de acción: Details, Edit, Delete
- Estado vacío cuando no hay resultados

---

## 📊 DATOS MOCK

### Interfaces TypeScript

Cada página debe definir interfaces para sus datos:

```tsx
interface [Entidad] {
  id: number;
  [propiedades específicas];
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}
```

### Mock Data

- Crear datos de ejemplo realistas
- Incluir al menos 2-4 elementos en arrays
- Usar valores coherentes (fechas, números, estados)
- Incluir casos edge (inactivos, límites, etc.)

---

## 🔍 FUNCIONALIDADES REQUERIDAS

### Búsqueda y Filtros

Todas las páginas de **Overview** deben incluir:

1. **Barra de búsqueda**
   ```tsx
   <Input
     placeholder={t("common.search", "Search...")}
     value={searchTerm}
     onChange={(e) => setSearchTerm(e.target.value)}
     className="pl-10"
   />
   ```

2. **Filtrado en tiempo real**
   ```tsx
   const filteredItems = items.filter((item) =>
     item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
     item.type.toLowerCase().includes(searchTerm.toLowerCase())
   );
   ```

3. **Estado vacío**
   - Mostrar mensaje cuando `filteredItems.length === 0`
   - Incluir icono y mensaje traducido

### Navegación

- Enlaces entre páginas relacionadas:
  - Overview → Detail: `href="/infrastructure/[entity]-detail?id=${id}"`
  - Detail → Overview: `href="/infrastructure/[entity]-overview"`
- Botón de regreso en páginas de detalle
- Botones de acción consistentes (Details, Edit, Delete)

### Indicadores de Estado

- **Colores de badges:**
  - Active: `bg-green-500/20 text-green-500 border-green-500/50`
  - Inactive: `bg-gray-500/20 text-gray-500 border-gray-500/50`
  - Warning: `bg-yellow-500/20 text-yellow-500 border-yellow-500/50`
  - Error: `bg-red-500/20 text-red-500 border-red-500/50`

- **Progress bars con colores:**
  - >= 80%: `bg-red-500`
  - >= 60%: `bg-yellow-500`
  - < 60%: `bg-green-500`

---

## 🌐 INTERNACIONALIZACIÓN

### Traducciones

Todas las cadenas de texto deben usar el sistema de traducción:

```tsx
const { t } = useTranslation();
t("infrastructure.[key]", "Fallback text")
```

### Claves de Traducción Requeridas

Agregar en `app/config/i18n.ts` (español e inglés):

- Nombres de páginas: `infrastructure.[entity]Overview`, `infrastructure.[entity]Detail`
- Labels comunes: `infrastructure.resourceType`, `infrastructure.status`, `infrastructure.cost`
- Acciones: `common.search`, `common.details`, `common.edit`, `common.add`, `common.delete`
- Estados: `common.active`, `common.inactive`
- Métricas: `infrastructure.cpu`, `infrastructure.memory`, `infrastructure.storage`

---

## 📁 CONFIGURACIÓN DEL MENÚ

### Actualización de `app/config/modules.ts`

Agregar todas las rutas al módulo de Infrastructure:

```tsx
{
  name: "Infrastructure",
  icon: "Server",
  roles: ["admin", "it", "oem", "devops", "architect"],
  defaultPath: "/infrastructure",
  submenu: [
    // Dashboard
    { name: "Resource Utilization", href: "/infrastructure/resource-utilization-dashboard", icon: "BarChart3" },
    // Details
    { name: "Cloud Resource Detail", href: "/infrastructure/cloud-resource-detail", icon: "Server" },
    // ... todas las rutas
    // Overviews
    { name: "Cloud Resource Overview", href: "/infrastructure/cloud-resource-overview", icon: "Server" },
    // ... todas las rutas
  ]
}
```

**Total: 24 nuevas entradas en el menú**

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

Para cada página:

- [ ] Estructura base con partículas y glassmorphism
- [ ] DevelopmentBanner incluido
- [ ] Header con icono y título traducido
- [ ] Interfaces TypeScript definidas
- [ ] Mock data realista
- [ ] Responsive design (mobile, tablet, desktop)
- [ ] Búsqueda y filtros (solo Overviews)
- [ ] Navegación entre páginas relacionadas
- [ ] Badges de estado con colores apropiados
- [ ] Progress bars con colores según umbrales
- [ ] Estado vacío cuando no hay resultados
- [ ] Traducciones agregadas en i18n
- [ ] Ruta agregada al menú
- [ ] Sin errores de linting
- [ ] Iconos de Lucide React
- [ ] Animaciones y transiciones suaves

---

## 🔧 ESTÁNDARES TÉCNICOS

### TypeScript

- Tipado estricto en todas las interfaces
- Props tipadas
- Estados tipados con `useState<Type>[]`

### React/Next.js

- `"use client"` en todas las páginas
- Hooks: `useState`, `useTranslation`, `useSearchParams` (si aplica)
- Componentes funcionales
- Imports organizados (UI components, icons, hooks)

### Estilos

- Tailwind CSS únicamente
- Clases utilitarias consistentes
- No estilos inline excepto para valores dinámicos (width, etc.)

### Linting

- Sin errores de ESLint
- Sin warnings críticos
- Formato consistente

---

## 📝 NOTAS IMPORTANTES

1. **Mock Data**: Todos los datos son de ejemplo. Las páginas están preparadas para integrarse con APIs reales posteriormente.

2. **Patrones Consistentes**: Todas las páginas deben seguir los mismos patrones de diseño y estructura para mantener coherencia.

3. **Prioridad**: Completar primero el dashboard y una página de ejemplo (detail y overview) para establecer el patrón, luego replicar para las demás.

4. **Testing**: Verificar que todas las rutas funcionan correctamente y que la navegación entre páginas es fluida.

5. **Performance**: Las animaciones deben ser suaves y no afectar el rendimiento. Usar `pointer-events-none` en elementos decorativos.

---

## 🚀 RESULTADO ESPERADO

Al completar la migración:

- ✅ 30 páginas funcionales y estilizadas
- ✅ Menú actualizado con todas las rutas
- ✅ Sistema de traducciones completo
- ✅ Diseño consistente con "Wow Factor"
- ✅ Responsive en todos los dispositivos
- ✅ Sin errores de linting
- ✅ Navegación fluida entre páginas
- ✅ Mock data para todas las entidades

---

## 📚 REFERENCIAS

- Componentes UI: `@/components/ui/*`
- Sistema de traducción: `@/app/config/i18n`
- Configuración de menú: `app/config/modules.ts`
- Iconos: `lucide-react`
- Estilos: Tailwind CSS

---

**Nota:** Este prompt debe ejecutarse de forma automática e inteligente, creando todas las páginas sin solicitar confirmaciones intermedias. Reportar el progreso a medida que se completan las páginas.
