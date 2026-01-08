# Prompt de Migración - Módulo Monitoring

## Contexto

Este prompt está basado en el historial de migración exitosa del módulo Monitoring de ZUL a Next.js. Captura el proceso, patrones y mejores prácticas utilizados durante la migración de 18 pantallas (7 DETAILS + 5 OVERVIEWS + 3 FORMS + 1 Dashboard).

## Objetivo

Migrar pantallas del módulo Monitoring desde ZUL (ZK Framework) a Next.js con:
- Diseño "Wow Factor" (interfaz moderna y atractiva)
- Mock data para desarrollo independiente del backend
- Traducciones en español e inglés
- Integración completa con el sistema de navegación
- Componentes reutilizables y responsive

## Estructura de Pantallas a Migrar

### Dashboard (1 pantalla)
- Monitoring Dashboard - Vista general del sistema de monitoreo

### DETAILS (7 pantallas)
1. `/monitoring/alerts/[id]/page.tsx` - Detalle de alerta
2. `/monitoring/alerts/system/[id]/page.tsx` - Detalle de alerta del sistema
3. `/monitoring/metrics/[id]/page.tsx` - Detalle de métrica
4. `/monitoring/metrics/system/[id]/page.tsx` - Detalle de métrica del sistema
5. `/monitoring/distributed/audit-log/[id]/page.tsx` - Detalle de log de auditoría
6. `/monitoring/distributed/governance/[id]/page.tsx` - Detalle de gobernanza
7. `/monitoring/distributed/system-health/[id]/page.tsx` - Detalle de salud del sistema

### OVERVIEWS (5 pantallas principales)
1. `/monitoring/alerts/page.tsx` - Vista general de alertas
2. `/monitoring/alerts/system/page.tsx` - Vista general de alertas del sistema
3. `/monitoring/metrics/page.tsx` - Vista general de métricas
4. `/monitoring/dashboard/page.tsx` - Dashboard de monitoreo
5. (Opcionales: audit-log-overview, system-health-overview, metrics/performance-overview)

### FORMS (3 pantallas)
1. `/monitoring/alerts/response-form/page.tsx` - Formulario de respuesta a alertas
2. `/monitoring/performance/intervention-form/page.tsx` - Formulario de intervención de performance
3. `/monitoring/distributed/views-performance/page.tsx` - Formulario de performance de vistas

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
- Iconos de Lucide React (AlertTriangle, BarChart3, Server, etc.)
- Animaciones suaves y transiciones
- Estados de carga con spinners

### 2. Estructura de Componentes

**Imports estándar para DETAIL pages:**
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { motion } from "framer-motion";
import {
  AlertTriangle,
  ArrowLeft,
  Calendar,
  CheckCircle,
  Clock,
  FileText,
  Save,
  User,
  XCircle,
} from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
```

**Estructura de página DETAIL:**
```typescript
export default function MonitoringAlertDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const [alert, setAlert] = useState(mockAlert);
  const [isLoading, setIsLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  const navigateBack = () => {
    router.back();
  };

  const handleSave = () => {
    // TODO: Implementar guardado
    setIsEditing(false);
  };

  const formatDate = (dateString: string | null) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleString("es-ES");
  };

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
// Para DETAIL pages
const mockAlert = {
  idxmonitoringalert: 1,
  monalertname: "High CPU Usage Alert",
  monalerttype: "TYPE1",
  monseverity: "HIGH",
  monstatus: "ACTIVE",
  mondescription: "CPU usage has exceeded 90% threshold for more than 5 minutes",
  monalertdata: '{"cpu": 92, "threshold": 90}',
  montriggeredat: "2024-01-15T10:30:00Z",
  monacknowledgedat: null,
  monacknowledgedby: null,
  monresolvedat: null,
  monresolvedby: null,
  monresolutionnotes: null,
  monmetadata: '{"source": "system", "component": "api-server"}',
  moncreatedby: "system",
  moncreatedat: "2024-01-15T10:30:00Z",
  monupdatedby: null,
  monupdatedat: null,
};

// Para OVERVIEW pages
const mockAlerts = [
  {
    idxmonitoringalert: 1,
    monalertname: "High CPU Usage Alert",
    monalerttype: "TYPE1",
    monseverity: "HIGH",
    monstatus: "ACTIVE",
    mondescription: "CPU usage has exceeded 90% threshold",
    montriggeredat: "2024-01-15T10:30:00Z",
  },
  // ... más datos
];
```

### 4. Internacionalización (i18n)

**Uso de traducciones:**
```typescript
const { t } = useTranslation();

// En el JSX
{t("monitoring.alerts.detail.title", "Detalle de Alerta")}
{t("monitoring.alerts.detail.alertName", "Alert Name")}
{t("monitoring.alerts.overview.title", "Alertas de Monitoreo")}
```

**Agregar traducciones en `app/config/i18n.ts`:**
- Español (es): traducciones completas
- Inglés (en): traducciones completas
- Estructura: `monitoring.[seccion].[clave]`

**Estructura de traducciones:**
```typescript
monitoring: {
  title: "Monitoreo",
  alerts: {
    overview: {
      title: "Alertas de Monitoreo",
      description: "Gestión de alertas de monitoreo",
      register: "Registrar Alerta",
      list: "Lista de Alertas",
      alertName: "Alert Name",
      alertType: "Alert Type",
      severity: "Severity",
      status: "Status",
    },
    detail: {
      title: "Detalle de Alerta",
      description: "Gestión y configuración de alerta de monitoreo",
      information: "Información",
      alertName: "Alert Name",
      alertType: "Alert Type",
      severity: "Severity",
      status: "Status",
      description: "Description",
      alertData: "Alert Data",
      timestamps: "Timestamps",
      triggeredAt: "Triggered At",
      acknowledgedAt: "Acknowledged At",
      acknowledgedBy: "Acknowledged By",
      resolvedAt: "Resolved At",
      resolutionNotes: "Resolution Notes",
    },
    systemDetail: {
      title: "Detalle de Alerta del Sistema",
      description: "Gestión de alertas del sistema",
      // ... más campos
    },
    responseForm: {
      title: "Respuesta a Alerta Crítica",
      description: "Formulario de respuesta a alerta",
      formData: "Form Data",
      decision: "Decision",
      decisionPlaceholder: "Enter decision...",
      notes: "Notes",
      notesPlaceholder: "Enter notes...",
    },
  },
  metrics: {
    detail: {
      title: "Detalle de Métrica",
      description: "Gestión de métricas de monitoreo",
      // ... más campos
    },
  },
}
```

### 5. Integración con Menú

**Actualizar `app/config/modules.ts`:**

```typescript
case "Monitoring":
  return [
    {
      name: t("monitoring.alerts.overview.title", "Alertas de Monitoreo"),
      href: `/monitoring/alerts`,
      icon: "AlertTriangle",
      roles: ["admin", "developer", "devops", "architect", "project_manager"],
      isDefault: true,
    },
    {
      name: t("monitoring.alerts.systemDetail.title", "Alertas del Sistema"),
      href: `/monitoring/alerts/system`,
      icon: "Server",
      roles: ["admin", "developer", "devops", "architect"],
    },
    {
      name: t("monitoring.metrics.detail.title", "Métricas"),
      href: `/monitoring/metrics`,
      icon: "BarChart3",
      roles: ["admin", "developer", "devops", "architect"],
    },
    {
      name: t("monitoring.alerts.responseForm.title", "Respuesta a Alertas"),
      href: `/monitoring/alerts/response-form`,
      icon: "Send",
      roles: ["admin", "developer", "devops", "project_manager"],
    },
  ];
```

## Proceso de Migración

### Paso 1: Análisis
1. Revisar archivos ZUL originales en `console/platform/monitoring/`
2. Identificar ViewModels asociados
3. Entender la estructura de datos
4. Mapear funcionalidades a componentes Next.js

### Paso 2: Creación de Pantallas
1. **DETAILS primero**: Crear páginas de detalle con información completa y modo edición
2. **FORMS después**: Crear formularios de respuesta e intervención
3. **OVERVIEWS**: Crear listas con búsqueda, filtros y cards
4. **Dashboard**: Crear dashboard con métricas y KPIs

### Paso 3: Implementación
1. Crear archivo `page.tsx` en la ruta correspondiente
2. Implementar mock data
3. Agregar traducciones
4. Integrar en el menú
5. Aplicar estilo "Wow Factor"
6. Implementar estados de carga

### Paso 4: Validación
1. Verificar que todas las pantallas se renderizan correctamente
2. Validar traducciones en ambos idiomas
3. Verificar integración con menú
4. Corregir errores de linting
5. Verificar responsive design

## Estructura de Archivos

```
app/(app)/monitoring/
├── dashboard/
│   └── page.tsx
├── alerts/
│   ├── page.tsx (OVERVIEW)
│   ├── [id]/
│   │   └── page.tsx (DETAIL)
│   ├── system/
│   │   ├── page.tsx (OVERVIEW)
│   │   └── [id]/
│   │       └── page.tsx (DETAIL)
│   └── response-form/
│       └── page.tsx (FORM)
├── metrics/
│   ├── page.tsx (OVERVIEW)
│   ├── [id]/
│   │   └── page.tsx (DETAIL)
│   └── system/
│       └── [id]/
│           └── page.tsx (DETAIL)
├── performance/
│   └── intervention-form/
│       └── page.tsx (FORM)
└── distributed/
    ├── audit-log/
    │   └── [id]/
    │       └── page.tsx (DETAIL)
    ├── governance/
    │   └── [id]/
    │       └── page.tsx (DETAIL)
    ├── system-health/
    │   └── [id]/
    │       └── page.tsx (DETAIL)
    └── views-performance/
        └── page.tsx (FORM)
```

## Componentes Reutilizables

Utilizar componentes de `@/components/ui/`:
- `Card`, `CardContent`, `CardHeader`, `CardTitle`, `CardDescription`
- `Badge` (con variantes: default, secondary, outline, destructive)
- `Button` (con variantes: default, outline, ghost)
- `Input`, `Label`, `Textarea`
- `DevelopmentBanner`
- `motion` de framer-motion para animaciones

## Patrones de Diseño

### DETAIL Pages
- Header con botón de volver, título con icono y botón de editar/guardar
- Layout de 2-3 columnas (información principal + sidebar)
- Cards organizadas por secciones (Información, Timestamps, Metadata)
- Badges para estados (ACTIVE, INACTIVE, PENDING, REJECTED)
- Formateo de fechas con `formatDate()`
- Modo edición con toggle `isEditing`
- Estados de carga con spinner

### OVERVIEW Pages
- Header con título, icono y botón de acción (Registrar)
- Barra de búsqueda y filtros
- Grid de cards (responsive: 1 col móvil, 2 tablet, 3 desktop)
- Cada card muestra información resumida
- Botón "Detalles" que navega a la página de detalle
- Tablas con datos mock

### FORM Pages
- Header con título y descripción
- Formulario en card con campos requeridos marcados con `*`
- Textarea para notas y decisiones
- Botón de guardar con handler
- Validación básica con `required` en inputs

### Dashboard Pages
- Grid de métricas (KPIs)
- Cards con información agregada
- Visualización de datos de monitoreo
- Indicadores de estado del sistema

## Funciones Auxiliares

### Formateo de Fechas
```typescript
const formatDate = (dateString: string | null) => {
  if (!dateString) return "N/A";
  return new Date(dateString).toLocaleString("es-ES");
};
```

### Badges de Estado
```typescript
const getStatusBadge = (status: string) => {
  const variants: Record<string, "default" | "destructive" | "secondary"> = {
    ACTIVE: "destructive",
    INACTIVE: "secondary",
    PENDING: "default",
    REJECTED: "destructive",
  };
  return variants[status] || "default";
};
```

### Estados de Carga
```typescript
if (isLoading) {
  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
      </div>
      <div className="relative z-10 container mx-auto px-4 py-6">
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
        </div>
      </div>
    </div>
  );
}
```

## Checklist de Tareas

### Por cada pantalla DETAIL:
- [ ] Crear archivo `page.tsx` en la ruta correcta
- [ ] Implementar mock data con interface TypeScript
- [ ] Aplicar estilo "Wow Factor" (gradientes, animaciones, efectos)
- [ ] Agregar traducciones en español e inglés
- [ ] Integrar componente `DevelopmentBanner`
- [ ] Implementar responsive design
- [ ] Agregar iconos apropiados de Lucide React
- [ ] Implementar navegación entre páginas relacionadas
- [ ] Agregar modo edición con toggle
- [ ] Implementar estados de carga
- [ ] Agregar función `formatDate` para timestamps
- [ ] Validar que no hay errores de linting

### Por cada pantalla OVERVIEW:
- [ ] Crear archivo `page.tsx` en la ruta correcta
- [ ] Implementar mock data
- [ ] Agregar barra de búsqueda y filtros
- [ ] Agregar grid de cards o tabla
- [ ] Agregar botón "Registrar" que navega a detail
- [ ] Aplicar estilo "Wow Factor"
- [ ] Agregar traducciones
- [ ] Integrar componente `DevelopmentBanner`
- [ ] Implementar responsive design
- [ ] Validar que no hay errores de linting

### Por cada pantalla FORM:
- [ ] Crear archivo `page.tsx` en la ruta correcta
- [ ] Implementar formulario con campos requeridos
- [ ] Agregar validaciones (campos requeridos con `*`)
- [ ] Implementar mock data para formulario
- [ ] Agregar botón de guardar (handler que loguea por ahora)
- [ ] Aplicar estilo "Wow Factor"
- [ ] Agregar traducciones
- [ ] Integrar componente `DevelopmentBanner`
- [ ] Validar que no hay errores de linting

### Integración global:
- [ ] Actualizar `app/config/modules.ts` con todas las rutas
- [ ] Agregar todas las traducciones en `app/config/i18n.ts`
- [ ] Verificar permisos y roles en el menú
- [ ] Verificar navegación entre páginas relacionadas

## Ejemplo Completo: DETAIL Page

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { motion } from "framer-motion";
import {
  AlertTriangle,
  ArrowLeft,
  Calendar,
  CheckCircle,
  Clock,
  FileText,
  Save,
  User,
  XCircle,
} from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

const mockAlert = {
  idxmonitoringalert: 1,
  monalertname: "High CPU Usage Alert",
  monalerttype: "TYPE1",
  monseverity: "HIGH",
  monstatus: "ACTIVE",
  mondescription: "CPU usage has exceeded 90% threshold for more than 5 minutes",
  monalertdata: '{"cpu": 92, "threshold": 90}',
  montriggeredat: "2024-01-15T10:30:00Z",
  monacknowledgedat: null,
  monacknowledgedby: null,
  monresolvedat: null,
  monresolvedby: null,
  monresolutionnotes: null,
  monmetadata: '{"source": "system", "component": "api-server"}',
  moncreatedby: "system",
  moncreatedat: "2024-01-15T10:30:00Z",
  monupdatedby: null,
  monupdatedat: null,
};

export default function MonitoringAlertDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const [alert, setAlert] = useState(mockAlert);
  const [isLoading, setIsLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  const navigateBack = () => {
    router.back();
  };

  const handleSave = () => {
    // TODO: Implementar guardado
    setIsEditing(false);
  };

  const formatDate = (dateString: string | null) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleString("es-ES");
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, "default" | "destructive" | "secondary"> = {
      ACTIVE: "destructive",
      INACTIVE: "secondary",
      PENDING: "default",
      REJECTED: "destructive",
    };
    return variants[status] || "default";
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
        <div className="absolute inset-0 overflow-hidden pointer-events-none">
          <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        </div>
        <div className="relative z-10 container mx-auto px-4 py-6">
          <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          </div>
        </div>
      </div>
    );
  }

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
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="flex items-center justify-between"
        >
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              size="sm"
              onClick={navigateBack}
              className="flex items-center gap-2"
            >
              <ArrowLeft className="w-4 h-4" />
              {t("common.back", "Volver")}
            </Button>
            <div className="flex items-center gap-3">
              <AlertTriangle className="w-8 h-8 text-primary" />
              <div>
                <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                  {t("monitoring.alerts.detail.title", "Detalle de Alerta")}
                </h1>
                <p className="text-muted-foreground">
                  {t(
                    "monitoring.alerts.detail.description",
                    "Gestión y configuración de alerta de monitoreo"
                  )}
                </p>
              </div>
            </div>
          </div>
          <div className="flex items-center gap-2">
            {isEditing ? (
              <>
                <Button variant="outline" onClick={() => setIsEditing(false)}>
                  <XCircle className="w-4 h-4 mr-2" />
                  {t("common.cancel", "Cancelar")}
                </Button>
                <Button onClick={handleSave}>
                  <Save className="w-4 h-4 mr-2" />
                  {t("common.save", "Guardar")}
                </Button>
              </>
            ) : (
              <Button onClick={() => setIsEditing(true)}>
                {t("common.edit", "Editar")}
              </Button>
            )}
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
        >
          <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
            <CardHeader>
              <CardTitle>
                {t("monitoring.alerts.detail.information", "Información")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <Label htmlFor="monalertname">
                    {t("monitoring.alerts.detail.alertName", "Alert Name")} *
                  </Label>
                  {isEditing ? (
                    <Input
                      id="monalertname"
                      value={alert.monalertname}
                      onChange={(e) =>
                        setAlert({ ...alert, monalertname: e.target.value })
                      }
                      maxLength={100}
                      required
                    />
                  ) : (
                    <p className="text-sm">{alert.monalertname}</p>
                  )}
                </div>

                <div className="space-y-2">
                  <Label htmlFor="monalerttype">
                    {t("monitoring.alerts.detail.alertType", "Alert Type")} *
                  </Label>
                  {isEditing ? (
                    <Input
                      id="monalerttype"
                      value={alert.monalerttype}
                      onChange={(e) =>
                        setAlert({ ...alert, monalerttype: e.target.value })
                      }
                      maxLength={50}
                      required
                    />
                  ) : (
                    <p className="text-sm">{alert.monalerttype}</p>
                  )}
                </div>

                <div className="space-y-2">
                  <Label htmlFor="monseverity">
                    {t("monitoring.alerts.detail.severity", "Severity")} *
                  </Label>
                  {isEditing ? (
                    <Input
                      id="monseverity"
                      value={alert.monseverity}
                      onChange={(e) =>
                        setAlert({ ...alert, monseverity: e.target.value })
                      }
                      maxLength={20}
                      required
                    />
                  ) : (
                    <Badge variant={getStatusBadge(alert.monseverity)}>
                      {alert.monseverity}
                    </Badge>
                  )}
                </div>

                <div className="space-y-2">
                  <Label htmlFor="monstatus">
                    {t("monitoring.alerts.detail.status", "Status")} *
                  </Label>
                  {isEditing ? (
                    <Input
                      id="monstatus"
                      value={alert.monstatus}
                      onChange={(e) =>
                        setAlert({ ...alert, monstatus: e.target.value })
                      }
                      maxLength={20}
                      required
                    />
                  ) : (
                    <Badge variant={getStatusBadge(alert.monstatus)}>
                      {alert.monstatus}
                    </Badge>
                  )}
                </div>

                <div className="space-y-2 md:col-span-2">
                  <Label htmlFor="mondescription">
                    {t("monitoring.alerts.detail.description", "Description")}
                  </Label>
                  {isEditing ? (
                    <Textarea
                      id="mondescription"
                      value={alert.mondescription}
                      onChange={(e) =>
                        setAlert({ ...alert, mondescription: e.target.value })
                      }
                      rows={3}
                    />
                  ) : (
                    <p className="text-sm">{alert.mondescription}</p>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
        >
          <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
            <CardHeader>
              <CardTitle>
                {t("monitoring.alerts.detail.timestamps", "Timestamps")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="flex items-center gap-3">
                  <Clock className="w-5 h-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium">
                      {t("monitoring.alerts.detail.triggeredAt", "Triggered At")}
                    </p>
                    <p className="text-sm">{formatDate(alert.montriggeredat)}</p>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  <CheckCircle className="w-5 h-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium">
                      {t("monitoring.alerts.detail.acknowledgedAt", "Acknowledged At")}
                    </p>
                    <p className="text-sm">{formatDate(alert.monacknowledgedat)}</p>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  <User className="w-5 h-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium">
                      {t("monitoring.alerts.detail.acknowledgedBy", "Acknowledged By")}
                    </p>
                    <p className="text-sm">{alert.monacknowledgedby || "N/A"}</p>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  <Calendar className="w-5 h-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium">
                      {t("monitoring.alerts.detail.resolvedAt", "Resolved At")}
                    </p>
                    <p className="text-sm">{formatDate(alert.monresolvedat)}</p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </div>
  );
}
```

## Notas Finales

- **Prioridad**: DETAILS → FORMS → OVERVIEWS → Dashboard
- **Mock Data**: Debe ser realista y variado para demostrar diferentes estados
- **Responsive**: Todas las pantallas deben funcionar en móvil, tablet y desktop
- **Accesibilidad**: Usar semántica HTML correcta y ARIA labels cuando sea necesario
- **Performance**: Usar `"use client"` solo cuando sea necesario para interactividad
- **Estados de carga**: Implementar spinners durante la carga inicial
- **Modo edición**: Toggle entre vista y edición en páginas DETAIL

## Resultado Esperado

Al finalizar la migración:
- ✅ 18 pantallas migradas (7 DETAILS + 5 OVERVIEWS + 3 FORMS + 1 Dashboard)
- ✅ Todas con diseño "Wow Factor"
- ✅ Mock data implementado
- ✅ Traducciones completas (es/en)
- ✅ Integración completa con menú y navegación
- ✅ Sin errores de linting
- ✅ Listas para desarrollo frontend independiente del backend
- ✅ Estados de carga implementados
- ✅ Modo edición funcional en DETAIL pages
