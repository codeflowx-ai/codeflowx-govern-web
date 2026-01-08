# 🎨 GUÍA DE DESARROLLO FRONTEND - GOBIERNO DEL DATO

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Frontend Developers, UI/UX Developers, React Developers

---

## 📋 INTRODUCCIÓN

Esta guía describe cómo desarrollar nuevas funcionalidades en el frontend del módulo de Gobierno del Dato. El frontend está construido con **Next.js 14**, **React 18**, **TypeScript** y **Tailwind CSS**.

---

## 🏗️ ARQUITECTURA DEL FRONTEND

### Estructura de Directorios

```
codeflowx-studio/
├── app/
│   └── (app)/
│       └── governance/
│           └── data/
│               ├── dashboard/
│               │   └── page.tsx
│               ├── integrations/
│               │   ├── page.tsx
│               │   ├── create/
│               │   │   └── page.tsx
│               │   └── [id]/
│               │       ├── edit/
│               │       │   └── page.tsx
│               │       └── explore/
│               │           └── page.tsx
│               ├── origins/
│               │   ├── page.tsx
│               │   ├── create/
│               │   │   └── page.tsx
│               │   └── [id]/
│               │       └── edit/
│               │           └── page.tsx
│               ├── datasets/
│               │   ├── overview/
│               │   │   └── page.tsx
│               │   ├── create/
│               │   │   └── page.tsx
│               │   └── [id]/
│               │       └── page.tsx
│               ├── quality/
│               │   └── page.tsx
│               ├── risks/
│               │   └── page.tsx
│               ├── privacy/
│               │   └── page.tsx
│               ├── lineage/
│               │   └── page.tsx
│               ├── documentation/
│               │   └── page.tsx
│               └── roles/
│                   └── page.tsx
│
├── app/api/v1/governance/data/
│   ├── integrations/
│   │   ├── route.ts
│   │   └── [id]/
│   │       ├── route.ts
│   │       └── schema/
│   │           └── route.ts
│   ├── origins/
│   │   └── route.ts
│   └── datasets/
│       ├── route.ts
│       └── [id]/
│           ├── route.ts
│           ├── quality/
│           │   └── route.ts
│           ├── risks/
│           │   └── route.ts
│           ├── privacy/
│           │   └── route.ts
│           ├── lineage/
│           │   └── route.ts
│           ├── documentation/
│           │   └── route.ts
│           └── analysis-config/
│               └── route.ts
│
├── app/(app)/governance/data/types/
│   ├── data-governance.ts
│   └── integrations.ts
│
└── app/config/i18n/modules/governance/data/
    └── es.ts
```

---

## 🎨 COMPONENTES UI

### Componentes Base Disponibles

El proyecto utiliza componentes de `@/components/ui` basados en shadcn/ui:

- `Button` - Botones con variantes
- `Card`, `CardHeader`, `CardTitle`, `CardBody` - Tarjetas
- `Dialog`, `DialogContent`, `DialogHeader`, `DialogTitle` - Modales
- `Input` - Campos de texto
- `Textarea` - Áreas de texto
- `Select` - Selectores (pero usar `<select>` nativo según estándar)
- `Switch` - Interruptores
- `Badge` - Etiquetas
- `Tabs`, `TabsList`, `TabsTrigger`, `TabsContent` - Pestañas
- `Label` - Etiquetas de formulario

### Estándar de Diseño

#### Formato de Pantallas de Creación/Edición

**Línea 1:** Título y Subtítulo
```tsx
<div className="mb-6">
  <h1 className="text-3xl font-bold">Título Principal</h1>
  <p className="text-muted-foreground mt-2">Subtítulo descriptivo</p>
</div>
```

**Línea 2:** Botón Volver (izquierda) + Botones de Acción (derecha)
```tsx
<div className="flex items-center justify-between mb-6">
  <Button variant="ghost" onClick={() => router.back()}>
    <ArrowLeft className="mr-2 h-4 w-4" />
    Volver
  </Button>
  <div className="flex gap-2">
    <Button variant="outline">Cancelar</Button>
    <Button onClick={handleSave}>Guardar</Button>
  </div>
</div>
```

#### Ancho Completo

Todas las pantallas deben usar `w-full` en lugar de `container mx-auto`:

```tsx
<div className="w-full p-6">
  {/* Contenido */}
</div>
```

#### Selectores Nativos

Usar `<select>` HTML nativo en lugar de componentes custom:

```tsx
<select
  value={formData.type}
  onChange={(e) => setFormData({ ...formData, type: e.target.value })}
  className="w-full p-2 border rounded"
>
  <option value="">Seleccionar...</option>
  <option value="OPTION1">Opción 1</option>
</select>
```

---

## 📝 CREAR NUEVA PANTALLA

### Paso 1: Crear Archivo de Página

```tsx
// app/(app)/governance/data/{module}/page.tsx
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { ArrowLeft } from "lucide-react";
import { useRouter } from "next/navigation";

export default function {Module}Page() {
  const { t } = useTranslation();
  const router = useRouter();

  return (
    <div className="w-full p-6">
      {/* Línea 1: Título y Subtítulo */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold">
          {t("governance.data.{module}.title", "Título")}
        </h1>
        <p className="text-muted-foreground mt-2">
          {t("governance.data.{module}.subtitle", "Subtítulo")}
        </p>
      </div>

      {/* Línea 2: Botón Volver + Acciones */}
      <div className="flex items-center justify-between mb-6">
        <Button variant="ghost" onClick={() => router.back()}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          <Button variant="outline">
            {t("common.cancel", "Cancelar")}
          </Button>
          <Button>
            {t("common.save", "Guardar")}
          </Button>
        </div>
      </div>

      {/* Contenido */}
      <Card>
        <CardHeader>
          <CardTitle>Contenido</CardTitle>
        </CardHeader>
        <CardBody>
          {/* ... */}
        </CardBody>
      </Card>
    </div>
  );
}
```

### Paso 2: Crear API Route (Mock o Proxy)

```tsx
// app/api/v1/governance/data/{module}/route.ts
import { NextRequest, NextResponse } from "next/server";

const USE_MOCK = process.env.USE_MOCK === "true";

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    // Retornar datos mock
    return NextResponse.json({
      data: [],
    });
  }

  // Llamar al backend real
  const backendUrl = process.env.BACKEND_URL || "http://localhost:8080";
  const response = await fetch(`${backendUrl}/api/v1/governance/data/{module}`);

  if (!response.ok) {
    return NextResponse.json(
      { error: "Error al obtener datos" },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}

export async function POST(request: NextRequest) {
  const body = await request.json();

  if (USE_MOCK) {
    // Simular creación
    return NextResponse.json({
      id: Date.now(),
      ...body,
    });
  }

  // Llamar al backend real
  const backendUrl = process.env.BACKEND_URL || "http://localhost:8080";
  const response = await fetch(`${backendUrl}/api/v1/governance/data/{module}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: "Error al crear" },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
```

### Paso 3: Agregar Traducciones

```typescript
// app/config/i18n/modules/governance/data/es.ts
export default {
  // ... traducciones existentes
  "{module}": {
    "title": "Título del Módulo",
    "subtitle": "Subtítulo descriptivo",
    // ...
  },
};
```

### Paso 4: Agregar al Menú

```typescript
// app/config/modules.ts
export function getMenuByModule(module: string) {
  // ...
  if (module === "Data Governance") {
    return [
      // ... items existentes
      {
        label: t("governance.data.{module}.title"),
        path: "/governance/data/{module}",
        icon: IconComponent,
      },
    ];
  }
}
```

---

## 🔧 COMPONENTES ESPECÍFICOS

### Visualización Gráfica de Línea de Base

```tsx
function LineageGraphView({ nodes, edges }: Props) {
  const [selectedNode, setSelectedNode] = useState<string | null>(null);

  return (
    <div className="relative w-full h-[600px] border rounded-lg bg-gray-50 dark:bg-gray-900 overflow-hidden">
      <svg width="100%" height="100%" className="absolute inset-0">
        {/* Dibujar aristas */}
        {edges.map((edge, idx) => (
          <line
            key={`edge-${idx}`}
            x1={edge.from.x}
            y1={edge.from.y}
            x2={edge.to.x}
            y2={edge.to.y}
            stroke="#94a3b8"
            strokeWidth="2"
            markerEnd="url(#arrowhead)"
          />
        ))}

        {/* Dibujar nodos */}
        {nodes.map((node, idx) => (
          <g key={node.id}>
            <circle
              cx={node.x}
              cy={node.y}
              r={node.type === "DATASET" ? 50 : 35}
              fill={node.type === "DATASET" ? "#3b82f6" : "#64748b"}
              onClick={() => setSelectedNode(node.id)}
            />
            {/* Etiqueta */}
            <text x={node.x} y={node.y + 75} textAnchor="middle">
              {node.label}
            </text>
          </g>
        ))}
      </svg>

      {/* Panel de detalles */}
      {selectedNode && (
        <div className="absolute top-4 right-4 w-80 bg-white dark:bg-gray-800 border rounded-lg shadow-lg p-4">
          {/* Detalles del nodo */}
        </div>
      )}
    </div>
  );
}
```

### Formulario de Permisos Granulares

```tsx
function PermissionsForm({ permissions, onChange }: Props) {
  return (
    <div className="space-y-3 p-4 border rounded-lg">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Eye className="h-4 w-4" />
          <Label>Lectura</Label>
        </div>
        <Switch
          checked={permissions.read}
          onChange={(checked) => onChange({ ...permissions, read: checked })}
        />
      </div>
      {/* Repetir para write, delete, approve */}
    </div>
  );
}
```

### Análisis de Impacto

```tsx
function ImpactAnalysisTab({ datasetId }: { datasetId: string }) {
  const [impactData, setImpactData] = useState<ImpactData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadImpactAnalysis();
  }, [datasetId]);

  const loadImpactAnalysis = async () => {
    try {
      const response = await fetch(
        `/api/v1/governance/data/datasets/${datasetId}/impact-analysis`
      );
      const data = await response.json();
      setImpactData(data);
    } catch (error) {
      console.error("Error loading impact analysis:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardBody>
            <p className="text-sm text-muted-foreground">Total Afectados</p>
            <p className="text-2xl font-bold">{impactData?.totalImpact || 0}</p>
          </CardBody>
        </Card>
        {/* Más métricas */}
      </div>

      {/* Lista de datasets afectados */}
      {impactData?.affectedDatasets.map((dataset) => (
        <Card key={dataset.id} className="border-l-4 border-l-orange-500">
          <CardBody>
            <div className="flex items-center gap-2 mb-2">
              <h4 className="font-semibold">{dataset.name}</h4>
              <Badge className={getImpactColor(dataset.impact)}>
                Impacto {dataset.impact}
              </Badge>
            </div>
            <p className="text-sm text-muted-foreground">{dataset.reason}</p>
          </CardBody>
        </Card>
      ))}
    </div>
  );
}
```

---

## 🔄 GESTIÓN DE ESTADO

### Estado Local con useState

Para pantallas simples, usar `useState`:

```tsx
const [formData, setFormData] = useState({
  name: "",
  description: "",
  type: "TRAINING",
});

const handleChange = (field: string, value: any) => {
  setFormData(prev => ({ ...prev, [field]: value }));
};
```

### Estado con useEffect

Para cargar datos:

```tsx
const [data, setData] = useState(null);
const [loading, setLoading] = useState(true);

useEffect(() => {
  async function loadData() {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/datasets/${id}`);
      const json = await response.json();
      setData(json);
    } catch (error) {
      console.error("Error:", error);
    } finally {
      setLoading(false);
    }
  }

  if (id) {
    loadData();
  }
}, [id]);
```

---

## 🎨 ESTILOS Y TEMA

### Dark Mode

Todos los componentes deben soportar dark mode:

```tsx
<div className="bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100">
  <p className="text-muted-foreground dark:text-gray-400">
    Texto secundario
  </p>
</div>
```

### Colores por Estado

```tsx
// Éxito
className="bg-green-100 text-green-800 dark:bg-green-900/40 dark:text-green-200"

// Advertencia
className="bg-yellow-100 text-yellow-800 dark:bg-yellow-900/40 dark:text-yellow-200"

// Error
className="bg-red-100 text-red-800 dark:bg-red-900/40 dark:text-red-200"

// Información
className="bg-blue-100 text-blue-800 dark:bg-blue-900/40 dark:text-blue-200"
```

---

## 📝 CHECKLIST DE DESARROLLO

### Nueva Pantalla

- [ ] Crear archivo de página en `app/(app)/governance/data/{module}/page.tsx`
- [ ] Seguir estándar de diseño (2 líneas: título/subtítulo + botones)
- [ ] Usar `w-full` para ancho completo
- [ ] Usar `<select>` nativo
- [ ] Crear API route en `app/api/v1/governance/data/{module}/route.ts`
- [ ] Agregar traducciones en `es.ts`
- [ ] Agregar al menú en `modules.ts`
- [ ] Soporte dark mode
- [ ] Manejo de errores
- [ ] Estados de carga
- [ ] Validaciones de formulario

### Nueva Funcionalidad

- [ ] Crear componente reutilizable si aplica
- [ ] Agregar tipos TypeScript
- [ ] Integrar con API (mock o real)
- [ ] Agregar traducciones
- [ ] Tests (si aplica)
- [ ] Documentación

---

## 🚀 PRÓXIMOS PASOS

1. **Revisar estándares:** Asegurar consistencia con diseño existente
2. **Crear componentes base:** Reutilizables para funcionalidades comunes
3. **Integrar con backend:** Conectar API routes con backend real
4. **Testing:** Tests de componentes y flujos
5. **Documentación:** Documentar componentes y patrones
6. **Optimización:** Performance y SEO

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** 📋 Planificación
