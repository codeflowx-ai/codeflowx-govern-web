# PROMPT: Migración de Analytics Dashboard de ZUL a Next.js

## Contexto

Se requiere migrar el módulo de Analytics desde la aplicación ZUL (ZK Framework) a Next.js. La migración incluye 6 pantallas principales que deben implementarse siguiendo los estándares del proyecto.

## Objetivo

Migrar completamente el módulo Analytics creando las siguientes pantallas en Next.js:

1. **Dashboard**: Vista principal de analytics overview
2. **Overviews** (3 pantallas):
   - Analytics Metrics Overview
   - Analytics Reports Overview
   - Analytics Trends Overview
3. **Details** (2 pantallas):
   - Analytics Metrics Detail (formulario)
   - Analytics Reports Detail (formulario)

## Referencias

Los prompts detallados de migración están en:
- `docs/prompts/MIGRACION_ANALYTICS_DASHBOARDS.md`
- `docs/prompts/MIGRACION_ANALYTICS_OVERVIEWS.md`
- `docs/prompts/MIGRACION_ANALYTICS_DETAILS.md`

## Requisitos Técnicos

### Estructura de Archivos

Las páginas deben crearse en:
- Dashboard: `/app/(app)/analytics/overview/dashboard/page.tsx`
- Metrics Overview: `/app/(app)/analytics/metrics/overview/page.tsx`
- Reports Overview: `/app/(app)/analytics/reports/overview/page.tsx`
- Trends Overview: `/app/(app)/analytics/trends/overview/page.tsx`
- Metrics Detail: `/app/(app)/analytics/metrics/detail/page.tsx`
- Reports Detail: `/app/(app)/analytics/reports/detail/page.tsx`

### Estilos y Diseño

- **"Wow Factor"**: Implementar gradientes, partículas animadas y glassmorphism
- Usar componentes de `@/components/ui/` (Card, Badge, Button, Input, etc.)
- Aplicar `backdrop-blur-md bg-background/60 border-border/50` para efectos glassmorphism
- Incluir animaciones sutiles con partículas en el fondo
- Usar gradientes en títulos: `bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent`

### Internacionalización

- Usar el hook `useTranslation()` de `@/app/config/i18n`
- Todas las cadenas de texto deben usar `t()` con claves descriptivas
- Agregar traducciones en español e inglés en `app/config/i18n/modules/governance/analytics/es.ts` (o módulo correspondiente)
- Estructura de claves: `analytics.{seccion}.{subseccion}.{campo}`

### Datos Mock

- Crear datos mock realistas para todas las pantallas durante el desarrollo
- Estructura de datos debe reflejar los campos del ViewModel original de ZUL
- Campos principales para Metrics:
  - `idxanalyticsmetric` (PK)
  - `anlmetricname`
  - `anlmetrictype`
  - `anlmetricvalue`
  - `anlmetricunit`
  - `anlthresholdmin`
  - `anlstatus` (ACTIVE, INACTIVE, PENDING)

### Funcionalidades Requeridas

#### Overviews
- Tabla con datos mock
- Búsqueda por texto (filtro en tiempo real)
- Filtros por tipo y estado
- Métricas resumen (Total, Activos, Pendientes, Inactivos) en cards
- Badges de estado con colores diferenciados
- Botón para crear nuevo registro (navega a detail)
- Acciones por fila (ver, editar, eliminar)

#### Details (Formularios)
- Formulario completo con todos los campos del ViewModel
- Validación de campos requeridos
- Botones: Guardar, Cancelar, Volver
- Navegación correcta usando `useRouter()` de Next.js

#### Dashboard
- Vista general con métricas agregadas
- Listado de analytics overview
- Búsqueda y filtros básicos

### Configuración del Menú

1. Agregar módulo "Analytics" a `modulesConfig` con roles:
   - `admin`
   - `developer`
   - `ai_analytics`
   - `business_analytics`

2. Agregar entradas en `getMenuByModule()` para el caso "Analytics":
   - Analytics Overview (default: `isDefault: true`)
   - Analytics Metrics
   - Analytics Reports
   - Analytics Trends

### Componentes Adicionales

- Incluir `DevelopmentBanner` en todas las pantallas
- Usar iconos de `lucide-react` apropiados:
  - `BarChart3` para analytics
  - `Search` para búsqueda
  - `Plus` para crear
  - `Edit`, `Trash2` para acciones
  - `TrendingUp`, `TrendingDown` para métricas
  - `Activity`, `Package`, `Server` según contexto

### Validaciones

- Formularios deben validar campos requeridos antes de enviar
- Mostrar mensajes de error claros
- Usar estados de React para manejar validación

### Navegación

- Usar `useRouter()` de `next/navigation`
- Botones de navegación deben usar `router.push()`
- Manejar eventos de click en filas de tabla para navegar a detalles

## Estructura de Implementación

### Orden de Ejecución

1. Crear estructura de carpetas
2. Implementar Dashboard
3. Implementar Overviews (Metrics, Reports, Trends)
4. Implementar Details (Metrics, Reports)
5. Agregar traducciones
6. Configurar menú
7. Verificar linting y errores

### Patrón de Código

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
// ... más imports

export default function NombrePagina() {
  const { t } = useTranslation();
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState("");

  // ... lógica de filtrado y estado

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas animadas */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        {/* ... partículas ... */}
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Contenido */}
      </div>
    </div>
  );
}
```

## Checklist de Completitud

- [ ] Dashboard creado y funcional
- [ ] 3 Overviews creados con tablas, búsqueda y filtros
- [ ] 2 Details creados con formularios completos
- [ ] Traducciones agregadas (español e inglés)
- [ ] Módulo Analytics agregado al menú
- [ ] Estilos "Wow Factor" aplicados
- [ ] Datos mock implementados
- [ ] Validaciones en formularios
- [ ] Navegación funcional
- [ ] Sin errores de linting
- [ ] Componentes UI consistentes

## Notas Importantes

- Seguir estrictamente los tipos y esquemas definidos en los ViewModels originales
- No inventar campos o estructuras de datos
- Mantener consistencia con el resto de la aplicación
- Usar los mismos patrones de diseño que otras páginas migradas
- Los endpoints se publican internamente; Spring Boot microservices normalizan datos

## Resultado Esperado

Al completar la migración, el usuario debe poder:
1. Acceder al módulo Analytics desde el menú lateral
2. Ver el dashboard con métricas generales
3. Navegar a cada overview y ver listados con búsqueda y filtros
4. Crear y editar registros desde los formularios de detail
5. Ver una UI moderna y consistente con el resto de la aplicación
