# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - TRACEABILITY

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Frontend (Next.js/React)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Endpoints BFF](#endpoints-bff)
3. [Integración en Next.js](#integración-en-nextjs)
4. [Ejemplos de Uso](#ejemplos-de-uso)
5. [Manejo de Errores](#manejo-de-errores)
6. [Mock Data](#mock-data)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Traceability Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**BFF Endpoint Base:**
```
/api/v1/traceability
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS BFF

### 1. Obtener Trazabilidad General o Específica

#### GET `/api/v1/traceability`

**Descripción:** Obtiene trazabilidad general o específica si se proporcionan entityType y entityId.

**Query Parameters:**
- `entityType` (opcional): MODEL, PROJECT, AGENT
- `entityId` (opcional): ID de la entidad

**Response:**
```typescript
interface TraceabilityEvidenceDto {
  entityType: string;
  entityId: number;
  integrityScore: number; // 0.0 - 1.0
  integrityStatus: "INTEGRITY_OK" | "INTEGRITY_WARNING" | "INTEGRITY_ERROR" | "NO_LOGS";
  verifiedLogs: number;
  totalLogs: number;
  exportDate?: string; // ISO 8601
  data: ModelTraceabilityDto | ProjectTraceabilityDto | AgentTraceabilityDto;
}
```

**Ejemplo de Uso:**
```typescript
// Trazabilidad general
const response = await fetch('/api/v1/traceability');
const data = await response.json();

// Trazabilidad específica
const response = await fetch('/api/v1/traceability?entityType=Model&entityId=123');
const data = await response.json();
```

---

### 2. Obtener Trazabilidad de Entidad Específica

#### GET `/api/v1/traceability/{entityType}/{id}`

**Descripción:** Obtiene trazabilidad completa de una entidad (Model, Project, Agent) con logs, decisiones HITL y outputs.

**Path Parameters:**
- `entityType`: MODEL, PROJECT, o AGENT
- `id`: ID de la entidad

**Response:**
```typescript
interface TraceabilityEvidenceDto {
  entityType: string;
  entityId: number;
  integrityScore: number;
  integrityStatus: string;
  verifiedLogs: number;
  totalLogs: number;
  data: ModelTraceabilityDto | ProjectTraceabilityDto | AgentTraceabilityDto;
}

interface ModelTraceabilityDto {
  model: {
    id: number;
    name: string;
    version: string;
    description?: string;
  };
  trainingDatasetId?: number;
  logs: ImmutableLogDto[];
  decisions: HitlDecisionDto[];
  outputs: ModelOutputDto[];
}

interface ProjectTraceabilityDto {
  project: {
    id: number;
    name: string;
    description?: string;
    status?: string;
  };
  models: ModelInfoDto[];
  logs: ImmutableLogDto[];
  decisions: HitlDecisionDto[];
  outputs: ProjectOutputDto[];
}

interface AgentTraceabilityDto {
  agent: {
    id: number;
    name: string;
    description?: string;
    status?: string;
  };
  logs: ImmutableLogDto[];
  decisions: HitlDecisionDto[];
  executions: AgentExecutionDto[];
  outputs: AgentOutputDto[];
}

interface ImmutableLogDto {
  id: number;
  uuid: string;
  entityType: string;
  entityId: number;
  action: string;
  userId: string;
  userName?: string;
  timestamp: string; // ISO 8601
  currentHash: string;
  previousHash: string;
  integrityStatus?: string;
}

interface HitlDecisionDto {
  id: number;
  entityType: string;
  entityId: number;
  type: string;
  decision: "APPROVED" | "REJECTED" | "PENDING";
  decisionReason?: string;
  decisionDate: string; // ISO 8601
  userId: string;
}

interface ModelOutputDto {
  id: number;
  timestamp: string; // ISO 8601
  input: string;
  output: string;
  confidence: number; // 0.0 - 1.0
}
```

**Ejemplo de Uso:**
```typescript
// Trazabilidad de modelo
const response = await fetch('/api/v1/traceability/Model/123');
const traceability = await response.json();

// Trazabilidad de proyecto
const response = await fetch('/api/v1/traceability/Project/1');
const traceability = await response.json();

// Trazabilidad de agente
const response = await fetch('/api/v1/traceability/Agent/789');
const traceability = await response.json();
```

---

### 3. Buscar Trazabilidad con Criterios

#### POST `/api/v1/traceability/search`

**Descripción:** Busca trazabilidad aplicando filtros de tipo de entidad, fecha, usuario, etc.

**Request Body:**
```typescript
interface TraceabilitySearchCriteriaDto {
  entityType?: string; // MODEL, PROJECT, AGENT
  entityId?: number;
  startDate?: string; // ISO 8601
  endDate?: string; // ISO 8601
  userId?: string;
  actionType?: string;
}
```

**Response:**
```typescript
TraceabilityEvidenceDto // Mismo formato que GET
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/v1/traceability/search', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    entityType: 'Model',
    entityId: 123,
    startDate: '2025-01-01T00:00:00Z',
    endDate: '2025-12-31T23:59:59Z',
  }),
});
const results = await response.json();
```

---

### 4. Exportar Evidencias de Trazabilidad

#### POST `/api/v1/traceability/export`

**Descripción:** Exporta evidencias de trazabilidad en formato JSON o PDF con verificación de integridad.

**Query Parameters:**
- `entityType` (requerido): MODEL, PROJECT, o AGENT
- `entityId` (requerido): ID de la entidad
- `format` (opcional): JSON o PDF (default: JSON)

**Response:**
```typescript
TraceabilityEvidenceDto // Con exportDate incluido
```

**Ejemplo de Uso:**
```typescript
// Exportar como JSON
const response = await fetch(
  '/api/v1/traceability/export?entityType=Model&entityId=123&format=JSON',
  { method: 'POST' }
);
const evidence = await response.json();

// Exportar como PDF (cuando esté implementado)
const response = await fetch(
  '/api/v1/traceability/export?entityType=Model&entityId=123&format=PDF',
  { method: 'POST' }
);
const blob = await response.blob();
```

---

## ⚛️ INTEGRACIÓN EN NEXT.JS

### Configuración Base

**Archivo:** `app/(app)/governance/compliance/traceability/page.tsx`

**Patrón de Uso:**
```typescript
'use client';

import { useState, useEffect } from 'react';
import { useSearchParams } from 'next/navigation';

export default function TraceabilityPage() {
  const searchParams = useSearchParams();
  const entityTypeFromUrl = searchParams.get('entityType');
  const entityIdFromUrl = searchParams.get('entityId');

  const [traceabilityData, setTraceabilityData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadTraceability();
  }, [entityTypeFromUrl, entityIdFromUrl]);

  const loadTraceability = async () => {
    try {
      setLoading(true);

      let url = '/api/v1/traceability';
      if (entityTypeFromUrl && entityIdFromUrl) {
        url = `/api/v1/traceability/${entityTypeFromUrl}/${entityIdFromUrl}`;
      }

      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to load traceability');

      const data = await response.json();
      setTraceabilityData(data);
    } catch (error) {
      console.error('Error loading traceability:', error);
    } finally {
      setLoading(false);
    }
  };

  // ... resto del componente
}
```

### Manejo de Mock Data

**Archivo:** `app/(app)/governance/data/mockTraceability.ts`

**Patrón:**
```typescript
const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK === 'true';

export const loadTraceability = async (entityType?: string, entityId?: number) => {
  if (USE_MOCK_DATA) {
    return mockTraceabilityData; // Datos mock
  }

  const url = entityType && entityId
    ? `/api/v1/traceability/${entityType}/${entityId}`
    : '/api/v1/traceability';

  const response = await fetch(url);
  return response.json();
};
```

---

## 📝 EJEMPLOS DE USO

### Ejemplo 1: Cargar Trazabilidad de Modelo

```typescript
const [modelTraceability, setModelTraceability] = useState(null);

useEffect(() => {
  const loadModelTraceability = async () => {
    const response = await fetch('/api/v1/traceability/Model/123');
    const data = await response.json();
    setModelTraceability(data);
  };

  loadModelTraceability();
}, []);
```

### Ejemplo 2: Buscar Trazabilidad con Filtros

```typescript
const searchTraceability = async (criteria: TraceabilitySearchCriteriaDto) => {
  try {
    const response = await fetch('/api/v1/traceability/search', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(criteria),
    });

    if (!response.ok) {
      throw new Error('Failed to search traceability');
    }

    const results = await response.json();
    return results;
  } catch (error) {
    console.error('Error searching traceability:', error);
    throw error;
  }
};
```

### Ejemplo 3: Exportar Evidencias

```typescript
const exportEvidence = async (entityType: string, entityId: number, format: 'JSON' | 'PDF') => {
  try {
    const response = await fetch(
      `/api/v1/traceability/export?entityType=${entityType}&entityId=${entityId}&format=${format}`,
      { method: 'POST' }
    );

    if (!response.ok) {
      throw new Error('Failed to export evidence');
    }

    if (format === 'JSON') {
      const evidence = await response.json();
      // Descargar como JSON
      const blob = new Blob([JSON.stringify(evidence, null, 2)], { type: 'application/json' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `traceability-${entityType}-${entityId}.json`;
      a.click();
      window.URL.revokeObjectURL(url);
    } else if (format === 'PDF') {
      const blob = await response.blob();
      // Descargar como PDF
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `traceability-${entityType}-${entityId}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    }
  } catch (error) {
    console.error('Error exporting evidence:', error);
    throw error;
  }
};
```

### Ejemplo 4: Mostrar Verificación de Integridad

```typescript
const IntegrityVerification = ({ evidence }: { evidence: TraceabilityEvidenceDto }) => {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'INTEGRITY_OK':
        return 'bg-green-500';
      case 'INTEGRITY_WARNING':
        return 'bg-yellow-500';
      case 'INTEGRITY_ERROR':
        return 'bg-red-500';
      default:
        return 'bg-gray-500';
    }
  };

  return (
    <div className="p-4 border rounded-lg">
      <h3 className="text-lg font-semibold mb-2">Verificación de Integridad</h3>
      <div className="flex items-center gap-4">
        <div>
          <p className="text-sm text-muted-foreground">Score de Integridad</p>
          <p className="text-2xl font-bold">
            {(evidence.integrityScore * 100).toFixed(0)}%
          </p>
        </div>
        <div>
          <p className="text-sm text-muted-foreground">Logs Verificados</p>
          <p className="text-xl font-bold">
            {evidence.verifiedLogs}/{evidence.totalLogs}
          </p>
        </div>
        <div>
          <Badge className={getStatusColor(evidence.integrityStatus)}>
            {evidence.integrityStatus}
          </Badge>
        </div>
      </div>
    </div>
  );
};
```

---

## ⚠️ MANEJO DE ERRORES

### Códigos de Estado HTTP

- **200 OK:** Operación exitosa
- **400 Bad Request:** Request inválido (validación fallida)
- **404 Not Found:** Entidad no encontrada
- **500 Internal Server Error:** Error interno del servidor

### Patrón de Manejo

```typescript
const handleApiCall = async (url: string, options?: RequestInit) => {
  try {
    const response = await fetch(url, options);

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Entidad no encontrada');
      } else if (response.status === 400) {
        const error = await response.json();
        throw new Error(error.message || 'Request inválido');
      } else {
        throw new Error(`Error ${response.status}: ${response.statusText}`);
      }
    }

    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
};
```

---

## 🎭 MOCK DATA

### Configuración

**Variable de Entorno:**
```env
NEXT_PUBLIC_USE_MOCK=true
```

**Archivo:** `app/(app)/governance/data/mockTraceability.ts`

**Estructura:**
```typescript
export const mockTraceability: ModelTraceability = {
  model: {
    id: 123,
    name: "Credit Scoring Model v1.0",
    version: "1.0",
  },
  dataset: {
    id: 456,
    name: "Training Dataset v1.0",
    version: "1.0",
  },
  logs: [
    {
      id: 1,
      type: "MODEL_TRAINING",
      timestamp: "2025-11-01T10:00:00Z",
      description: "Model training started",
      hash: "abc123...",
      previousHash: "000000...",
      userId: "ml-engineer@example.com",
      entityType: "Model",
      entityId: 123,
      integrityVerified: true,
    },
    // ... más logs
  ],
  decisions: [
    {
      id: 1,
      type: "MODEL_APPROVAL",
      decision: "APPROVED",
      timestamp: "2025-11-30T15:00:00Z",
      userId: "compliance-officer@example.com",
      entityType: "Model",
      entityId: 123,
      notes: "Model meets all compliance requirements",
    },
    // ... más decisiones
  ],
  outputs: [
    {
      id: 1,
      timestamp: "2025-12-01T11:00:00Z",
      input: "Customer ID: 12345, Credit History: Good, Income: $75,000",
      output: "Credit Score: 750 - APPROVED",
      confidence: 0.95,
      modelId: 123,
    },
    // ... más outputs
  ],
  integrityVerification: {
    score: 0.95,
    status: "INTEGRITY_OK",
    verifiedLogs: 19,
    totalLogs: 20,
  },
};

export const mockEntityTraceability: EntityTraceability = {
  entityType: "Model",
  entityId: 123,
  entityName: "Credit Scoring Model v1.0",
  traceability: mockTraceability,
  relatedEntities: [
    {
      type: "Dataset",
      id: 456,
      name: "Training Dataset v1.0",
      relationship: "TRAINED_WITH",
    },
    {
      type: "Project",
      id: 1,
      name: "AI Credit Scoring System",
      relationship: "BELONGS_TO",
    },
  ],
};
```

**Uso:**
```typescript
const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';

const loadData = async () => {
  if (USE_MOCK) {
    return mockTraceabilityData;
  }
  return fetch('/api/v1/traceability').then(res => res.json());
};
```

---

## 🔗 REFERENCIAS

- **BFF Controller:** `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/TraceabilityController.java`
- **BFF Service:** `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/impl/TraceabilityServiceImpl.java`
- **Pantallas Next.js:** `codeflowx-studio/app/(app)/governance/compliance/traceability/`
- **Mock Data:** `codeflowx-studio/app/(app)/governance/data/mockTraceability.ts`

---

## 🏗️ ESTRUCTURA DE ARCHIVOS FRONTEND

### Páginas Principales

**Página Principal:**
- `codeflowx-studio/app/(app)/governance/compliance/traceability/page.tsx`
- Componente principal con métricas, filtros y lista de evidencias

**Página de Detalle:**
- `codeflowx-studio/app/(app)/governance/compliance/traceability/[entityType]/[id]/page.tsx`
- Componente dinámico que muestra trazabilidad completa según tipo de entidad

### Mock Data

**Archivo:**
- `codeflowx-studio/app/(app)/governance/data/mockTraceability.ts`

**Estructura:**
```typescript
export const mockTraceability: ModelTraceability = {
  model: { id: 123, name: "...", version: "1.0" },
  logs: [...], // 20 logs
  decisions: [...], // 15 decisiones
  outputs: [...], // 20 outputs
  integrityVerification: { score: 0.95, status: "INTEGRITY_OK" }
};
```

### Mock API Routes

**Rutas:**
- `codeflowx-studio/app/api/compliance/traceability/route.ts` - GET (lista)
- `codeflowx-studio/app/api/compliance/traceability/[entityType]/[id]/route.ts` - GET (detalle)
- `codeflowx-studio/app/api/compliance/traceability/export/route.ts` - POST (exportación)

### Internacionalización

**Archivo:**
- `codeflowx-studio/app/config/i18n/modules/governance/compliance.ts`

**Claves de Traducción:**
- `governance.compliance.traceability.*` - Todas las traducciones del módulo
- Idiomas: es, en, fr, de, it, pt

---

## 🎨 COMPONENTES REUTILIZABLES

### Cards de Evidencias

**Ubicación:** `codeflowx-studio/app/(app)/governance/compliance/traceability/page.tsx`

**Uso:**
```typescript
<Card>
  <CardHeader>
    <Badge>{evidence.entityType}</Badge>
    <h3>{evidence.entityName}</h3>
  </CardHeader>
  <CardContent>
    <p>Integrity: {evidence.integrityScore * 100}%</p>
    <Button onClick={() => handleViewDetails(evidence)}>
      Ver Detalles
    </Button>
  </CardContent>
</Card>
```

### Cards con Scroll

**Ubicación:** `codeflowx-studio/app/(app)/governance/compliance/traceability/[entityType]/[id]/page.tsx`

**Uso:**
```typescript
<Card className="max-h-[600px] overflow-y-auto">
  <CardHeader>
    <h3>Logs Inmutables</h3>
  </CardHeader>
  <CardContent>
    {logs.map(log => (
      <div key={log.id}>{/* Log content */}</div>
    ))}
  </CardContent>
</Card>
```

---

## 🔧 CONFIGURACIÓN Y VARIABLES DE ENTORNO

### Variables de Entorno

**Archivo:** `.env.local` o `.env`

```env
# Mock Data
NEXT_PUBLIC_USE_MOCK=true

# API Base URL (si no se usa mock)
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080

# Feature Flags (opcionales)
NEXT_PUBLIC_ENABLE_TRACEABILITY_EXPORT_PDF=false
```

### Configuración de API Client

**Archivo:** `codeflowx-studio/app/(app)/governance/lib/api-client.ts`

**Ejemplo:**
```typescript
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || '/api';
const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';

export const fetchTraceability = async (entityType: string, id: number) => {
  if (USE_MOCK) {
    return loadMockTraceability(entityType, id);
  }

  const response = await fetch(`${API_BASE_URL}/v1/traceability/${entityType}/${id}`);
  return response.json();
};
```

---

## 🐛 TROUBLESHOOTING FRONTEND

### Problema 1: Traducciones No Se Aplican

**Síntoma:** Las cadenas se muestran en español aunque el idioma esté cambiado.

**Causas Posibles:**
- Clave de traducción incorrecta
- Idioma no está en localStorage
- Hook `useTranslation` no está configurado correctamente

**Solución:**
1. Verificar que las claves usan `governance.compliance.traceability.*`
2. Verificar que el idioma está guardado en localStorage
3. Revisar que `useTranslation()` está siendo llamado correctamente
4. Verificar que el archivo de traducciones tiene todas las claves

### Problema 2: Navegación No Funciona

**Síntoma:** Los botones "Ver Detalles" no navegan correctamente.

**Causa:** Uso de `router.push()` en lugar de `window.location.href`

**Solución:**
```typescript
// ❌ Incorrecto
router.push(`/governance/compliance/traceability/${entityType}/${id}`);

// ✅ Correcto
window.location.href = `/governance/compliance/traceability/${entityType}/${id}`;
```

### Problema 3: Scroll No Aparece en Cards

**Síntoma:** Los cards de logs/decisiones/outputs no muestran scroll aunque hay muchos elementos.

**Causa:** `max-h-[600px]` no está aplicado o el contenido no excede el límite

**Solución:**
1. Verificar que la clase `max-h-[600px] overflow-y-auto` está aplicada
2. Verificar que hay suficientes elementos mock (20+ logs)
3. Verificar que el contenedor padre no tiene `overflow-hidden`

### Problema 4: Layout No Ocupa 100% Ancho

**Síntoma:** La página tiene márgenes laterales.

**Causa:** Uso de `container mx-auto` en lugar de `w-full max-w-full`

**Solución:**
```typescript
// ❌ Incorrecto
<div className="container mx-auto">

// ✅ Correcto
<div className="w-full max-w-full">
```

---

## 🧪 TESTING FRONTEND

### Unit Tests - Componentes

**Ejemplo:**
```typescript
// __tests__/traceability/page.test.tsx
import { render, screen } from '@testing-library/react';
import TraceabilityPage from '@/app/(app)/governance/compliance/traceability/page';

describe('TraceabilityPage', () => {
  it('renders metrics cards', () => {
    render(<TraceabilityPage />);
    expect(screen.getByText('Total Entidades')).toBeInTheDocument();
  });

  it('renders evidence list', () => {
    render(<TraceabilityPage />);
    expect(screen.getByText('Ver Detalles')).toBeInTheDocument();
  });
});
```

### Integration Tests - Navegación

**Ejemplo:**
```typescript
// __tests__/traceability/navigation.test.tsx
import { render, screen, fireEvent } from '@testing-library/react';
import TraceabilityPage from '@/app/(app)/governance/compliance/traceability/page';

describe('Traceability Navigation', () => {
  it('navigates to detail page on button click', () => {
    const mockLocation = jest.fn();
    window.location.href = mockLocation;

    render(<TraceabilityPage />);
    const button = screen.getByText('Ver Detalles');
    fireEvent.click(button);

    expect(mockLocation).toHaveBeenCalledWith(
      expect.stringContaining('/traceability/Model/123')
    );
  });
});
```

---

## 🔄 EJEMPLOS DE EVOLUCIÓN FRONTEND

### Ejemplo 1: Agregar Nuevo Filtro

**Pasos:**

1. **Actualizar Interfaz:**
```typescript
interface TraceabilityFilters {
  entityName?: string;
  dateFrom?: string;
  dateTo?: string;
  userId?: string;
  logType?: string; // NUEVO
}
```

2. **Agregar Campo en UI:**
```typescript
<Input
  placeholder="Tipo de Log"
  value={filters.logType}
  onChange={(e) => setFilters({ ...filters, logType: e.target.value })}
/>
```

3. **Actualizar Llamada API:**
```typescript
const searchTraceability = async (criteria: TraceabilitySearchCriteriaDto) => {
  const response = await fetch('/api/v1/traceability/search', {
    method: 'POST',
    body: JSON.stringify({
      ...criteria,
      logType: filters.logType, // NUEVO
    }),
  });
  return response.json();
};
```

### Ejemplo 2: Agregar Visualización de Gráficos

**Pasos:**

1. **Instalar Librería:**
```bash
npm install recharts
```

2. **Crear Componente:**
```typescript
// components/TraceabilityChart.tsx
import { LineChart, Line, XAxis, YAxis } from 'recharts';

export const TraceabilityChart = ({ data }) => {
  return (
    <LineChart data={data}>
      <XAxis dataKey="date" />
      <YAxis />
      <Line type="monotone" dataKey="logs" stroke="#8884d8" />
    </LineChart>
  );
};
```

3. **Integrar en Página:**
```typescript
import { TraceabilityChart } from '@/components/TraceabilityChart';

<TraceabilityChart data={chartData} />
```

---

## 📱 RESPONSIVE DESIGN

### Breakpoints

**Tailwind CSS:**
- `sm`: 640px
- `md`: 768px
- `lg`: 1024px
- `xl`: 1280px

### Ejemplo de Grid Responsive

```typescript
// Grid de evidencias: 1 columna en móvil, 2 en tablet, 3 en desktop
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
  {evidences.map(evidence => (
    <EvidenceCard key={evidence.id} evidence={evidence} />
  ))}
</div>
```

### Cards con Scroll Responsive

```typescript
// Altura máxima: 400px en móvil, 600px en desktop
<Card className="max-h-[400px] lg:max-h-[600px] overflow-y-auto">
  {/* Content */}
</Card>
```

---

## 🎯 MEJORES PRÁCTICAS

### 1. Manejo de Estados

**Usar React Query para caché:**
```typescript
import { useQuery } from '@tanstack/react-query';

const { data, isLoading, error } = useQuery({
  queryKey: ['traceability', entityType, entityId],
  queryFn: () => fetchTraceability(entityType, entityId),
  staleTime: 5 * 60 * 1000, // 5 minutos
});
```

### 2. Manejo de Errores

**Mostrar errores de forma amigable:**
```typescript
if (error) {
  return (
    <Alert variant="destructive">
      <AlertTitle>Error</AlertTitle>
      <AlertDescription>
        No se pudo cargar la trazabilidad. Por favor, intente nuevamente.
      </AlertDescription>
    </Alert>
  );
}
```

### 3. Loading States

**Mostrar skeleton mientras carga:**
```typescript
if (isLoading) {
  return <TraceabilitySkeleton />;
}
```

### 4. Optimización de Rendimiento

**Lazy loading de componentes pesados:**
```typescript
import dynamic from 'next/dynamic';

const TraceabilityChart = dynamic(
  () => import('@/components/TraceabilityChart'),
  { ssr: false }
);
```

---

**Última Actualización:** Diciembre 2025
