# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - FRIA

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Frontend (Next.js/React)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Archivos](#estructura-de-archivos)
3. [Endpoints BFF](#endpoints-bff)
4. [Integración en Next.js](#integración-en-nextjs)
5. [Ejemplos de Uso](#ejemplos-de-uso)
6. [Manejo de Errores](#manejo-de-errores)
7. [Mock Data](#mock-data)
8. [Configuración](#configuración)
9. [Testing](#testing)
10. [Troubleshooting](#troubleshooting)
11. [Performance y Optimización](#performance-y-optimización)
12. [Seguridad](#seguridad)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (FRIA Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**BFF Endpoint Base:**
```
/api/v1/fria
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 📁 ESTRUCTURA DE ARCHIVOS

### Estructura Completa del Frontend

```
app/(app)/governance/compliance/fria/
├── page.tsx                    # Wizard FRIA (6 pasos)
├── [id]/
│   └── page.tsx               # Detalle FRIA
├── projects/
│   └── page.tsx               # Listado de proyectos con FRIA
└── assessments/               # (Legacy, mantenido para compatibilidad)
    └── page.tsx               # Listado de evaluaciones

components/
└── ui/                        # Componentes reutilizables
    ├── tabs.tsx               # Componente Tabs personalizado
    ├── card.tsx                # Card component
    ├── button.tsx              # Button component
    └── ...

app/config/
├── i18n/
│   └── modules/
│       └── governance/
│           └── compliance.ts  # Traducciones FRIA
└── modules.ts                  # Configuración de menú sidebar
```

### Convenciones de Nombres

**Archivos:**
- Páginas: `page.tsx` (Next.js App Router)
- Componentes: `*.tsx` (PascalCase)
- Hooks: `use*.ts` (camelCase con prefijo `use`)
- Utilidades: `*.ts` (camelCase)

**Carpetas:**
- Páginas: `app/(app)/governance/compliance/fria/`
- Componentes: `components/`
- Configuración: `app/config/`
- Traducciones: `app/config/i18n/modules/`

### Mapeo Funcionalidad → Archivo

| Funcionalidad | Archivo |
|---------------|---------|
| Wizard FRIA (6 pasos) | `app/(app)/governance/compliance/fria/page.tsx` |
| Detalle FRIA | `app/(app)/governance/compliance/fria/[id]/page.tsx` |
| Listado de proyectos | `app/(app)/governance/compliance/fria/projects/page.tsx` |
| Listado de evaluaciones | `app/(app)/governance/compliance/fria/assessments/page.tsx` |
| Traducciones | `app/config/i18n/modules/governance/compliance.ts` |
| Menú sidebar | `app/config/modules.ts` |

---

## 🔌 ENDPOINTS BFF

### 1. Crear FRIA

#### POST `/api/v1/fria/create`

**Descripción:** Crea una nueva evaluación FRIA asociada a un proyecto.

**Request Body:**
```typescript
interface FriaCreateRequest {
  projectId: number;
  userId: number;
}
```

**Response:**
```typescript
interface FriaAssessmentDto {
  id: number;
  projectId: number;
  status: "DRAFT" | "COMPLETED" | "NOTIFIED" | "APPROVED";
  completenessScore: number; // 0.00 - 1.00
  finalRisk: number | null; // 0.0 - 1.0
  riskLevel: "low" | "medium" | "high" | "critical" | null;
  createdAt: string; // ISO 8601
  updatedAt: string; // ISO 8601
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/v1/fria/create', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    projectId: 1,
    userId: 123,
  }),
});

if (!response.ok) {
  throw new Error(`HTTP error! status: ${response.status}`);
}

const fria = await response.json();
console.log('FRIA creada:', fria.id);
```

---

### 2. Actualizar Paso del Wizard

#### PUT `/api/v1/fria/{friaId}/step/{stepNumber}`

**Descripción:** Actualiza un paso específico del wizard FRIA (1-6).

**Path Parameters:**
- `friaId`: ID de la evaluación FRIA
- `stepNumber`: Número del paso (1-6)

**Request Body:**
```typescript
interface FriaStepUpdateRequest {
  // Paso 1: Art. 27.1.a - Process Description
  processDescription?: string;

  // Paso 2: Art. 27.1.b - Usage Period and Frequency
  usagePeriodStart?: string; // ISO 8601
  usagePeriodEnd?: string; // ISO 8601
  usageFrequency?: "DAILY" | "WEEKLY" | "MONTHLY" | "CONTINUOUS" | "OTHER";

  // Paso 3: Art. 27.1.c - Affected Categories
  affectedCategories?: string[];
  vulnerableGroupsIncluded?: boolean;
  vulnerableGroupsDescription?: string;

  // Paso 4: Art. 27.1.d - Risks
  risks?: Array<{
    type: "DISCRIMINATION" | "PRIVACY" | "TRANSPARENCY" | "AUTONOMY" | "DIGNITY" | "OTHER";
    severity: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
    probability: number; // 0.0 - 1.0
    impact: "LOW" | "MEDIUM" | "HIGH";
    description: string;
  }>;

  // Paso 5: Art. 27.1.e - Human Oversight
  hitlEnabled?: boolean;
  humanOversightDescription?: string;

  // Paso 6: Art. 27.1.f - Mitigation Measures
  mitigationMeasures?: Array<{
    type: "PREVENTIVE" | "DETECTIVE" | "CORRECTIVE";
    description: string;
    effectiveness: number; // 0.0 - 1.0
    associatedRiskId?: number; // Opcional
  }>;
}
```

**Response:**
```typescript
interface FriaStepUpdateResponse {
  success: boolean;
  completenessScore: number; // 0.00 - 1.00
  message?: string;
}
```

**Ejemplo de Uso:**
```typescript
// Actualizar Paso 1
const response = await fetch(`/api/v1/fria/${friaId}/step/1`, {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    processDescription: 'El sistema se usa para evaluación crediticia...',
  }),
});

const result = await response.json();
console.log('Completitud actualizada:', result.completenessScore);
```

---

### 3. Calcular Riesgo Final

#### POST `/api/v1/fria/{friaId}/calculate-risk`

**Descripción:** Calcula el riesgo final según Anexo IX del EU AI Act.

**Path Parameters:**
- `friaId`: ID de la evaluación FRIA

**Response:**
```typescript
interface FriaCalculateRiskResponse {
  success: boolean;
  finalRisk: number; // 0.0 - 1.0
  riskLevel: "low" | "medium" | "high" | "critical";
  message?: string;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/v1/fria/${friaId}/calculate-risk`, {
  method: 'POST',
});

const result = await response.json();
console.log('Riesgo final:', result.finalRisk);
console.log('Nivel de riesgo:', result.riskLevel);
```

---

### 4. Validación Cruzada (INC-007)

#### POST `/api/v1/fria/{friaId}/cross-validate`

**Descripción:** Valida consistencia entre FRIA documental y métricas técnicas reales.

**Path Parameters:**
- `friaId`: ID de la evaluación FRIA

**Response:**
```typescript
interface FriaCrossValidateResponse {
  success: boolean;
  consistencyScore: number; // 0.00 - 1.00
  inconsistencies: Array<{
    field: string;
    expected: string;
    actual: string;
    severity: "LOW" | "MEDIUM" | "HIGH";
  }>;
  requiresJustification: boolean; // true si score < 0.70
  message?: string;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/v1/fria/${friaId}/cross-validate`, {
  method: 'POST',
});

const result = await response.json();
console.log('Score de consistencia:', result.consistencyScore);

if (result.requiresJustification) {
  console.warn('Se requiere justificación:', result.inconsistencies);
}
```

---

### 5. Notificar Autoridades (Art. 27.3)

#### POST `/api/v1/fria/{friaId}/notify-authority`

**Descripción:** Notifica a autoridades competentes cuando riesgo final >= 0.75.

**Path Parameters:**
- `friaId`: ID de la evaluación FRIA

**Request Body (Opcional):**
```typescript
interface FriaNotifyAuthorityRequest {
  notificationDetails?: string;
  authorityName?: string;
}
```

**Response:**
```typescript
interface FriaNotifyAuthorityResponse {
  success: boolean;
  friaId: number;
  notificationId: string;
  timestamp: string; // ISO 8601
  message: string;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/v1/fria/${friaId}/notify-authority`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    notificationDetails: 'Detalles adicionales de la notificación',
    authorityName: 'EU AI Act Authority',
  }),
});

const result = await response.json();
console.log('Notificación enviada:', result.notificationId);
```

---

### 6. Obtener Detalle FRIA

#### GET `/api/v1/fria/{friaId}`

**Descripción:** Obtiene el detalle completo de una evaluación FRIA.

**Path Parameters:**
- `friaId`: ID de la evaluación FRIA

**Response:**
```typescript
interface FriaAssessmentDto {
  id: number;
  projectId: number;
  projectName: string;
  status: "DRAFT" | "COMPLETED" | "NOTIFIED" | "APPROVED";
  completenessScore: number;
  finalRisk: number | null;
  riskLevel: "low" | "medium" | "high" | "critical" | null;

  // Paso 1: Art. 27.1.a
  processDescription: string | null;

  // Paso 2: Art. 27.1.b
  usagePeriodStart: string | null;
  usagePeriodEnd: string | null;
  usageFrequency: string | null;

  // Paso 3: Art. 27.1.c
  affectedCategories: string[];
  vulnerableGroupsIncluded: boolean;
  vulnerableGroupsDescription: string | null;

  // Paso 4: Art. 27.1.d
  risks: Array<{
    id: number;
    type: string;
    severity: string;
    probability: number;
    impact: string;
    description: string;
  }>;

  // Paso 5: Art. 27.1.e
  hitlEnabled: boolean;
  humanOversightDescription: string | null;

  // Paso 6: Art. 27.1.f
  mitigationMeasures: Array<{
    id: number;
    type: string;
    description: string;
    effectiveness: number;
    associatedRiskId: number | null;
  }>;

  // Cross-validation
  crossValidationScore: number | null;
  crossValidationResult: any | null;

  // Notification
  notified: boolean;
  notificationDate: string | null;
  notificationId: string | null;

  // DPIA
  dpiaIntegrated: boolean;
  dpiaId: number | null;

  // Timestamps
  createdAt: string;
  updatedAt: string;
  createdBy: string;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/v1/fria/${friaId}`);
const fria = await response.json();
console.log('FRIA:', fria);
```

---

### 7. Listar Proyectos con FRIA

#### GET `/api/v1/fria/projects`

**Descripción:** Lista proyectos con sus evaluaciones FRIA agrupadas, con paginación y filtros.

**Query Parameters:**
- `page` (default: 0): Número de página (0-indexed)
- `size` (default: 10): Tamaño de página
- `search` (opcional): Búsqueda por nombre, descripción o categoría
- `status` (opcional): Filtrar por estado de última FRIA (DRAFT, COMPLETED, NOTIFIED, APPROVED, all)

**Response:**
```typescript
interface FriaProjectsListResponse {
  projects: Array<{
    projectId: number;
    projectName: string;
    projectDescription: string;
    projectCategory: string;
    frias: Array<FriaAssessmentSummary>;
    latestFria: FriaAssessmentSummary | null;
    totalFrias: number;
  }>;
  pagination: {
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
  };
  statistics: {
    totalProjects: number;
    totalFrias: number;
    notifiedFrias: number;
    draftFrias: number;
  };
}

interface FriaAssessmentSummary {
  id: number;
  version: string | null;
  createdAt: string;
  updatedAt: string;
  status: "DRAFT" | "COMPLETED" | "NOTIFIED" | "APPROVED";
  completenessScore: number;
  finalRisk: number | null;
  riskLevel: "low" | "medium" | "high" | "critical" | null;
  notified: boolean;
  notificationDate: string | null;
  approved: boolean;
  approvalDate: string | null;
  createdBy: string;
}
```

**Ejemplo de Uso:**
```typescript
// Listar todos los proyectos
const response = await fetch('/api/v1/fria/projects?page=0&size=10');
const data = await response.json();

// Filtrar por estado
const response = await fetch('/api/v1/fria/projects?status=NOTIFIED');

// Búsqueda
const response = await fetch('/api/v1/fria/projects?search=credit');
```

---

## 🎨 INTEGRACIÓN EN NEXT.JS

### Estructura de Archivos

```
app/(app)/governance/compliance/fria/
├── page.tsx                    # Wizard FRIA (6 pasos)
├── [id]/page.tsx              # Detalle FRIA
└── projects/
    └── page.tsx                # Listado de proyectos con FRIA
```

### Componentes Principales

#### 1. Wizard FRIA (`page.tsx`)

**Características:**
- Wizard de 6 pasos correspondientes a Art. 27.1.a-f
- Navegación Previous/Next con validación
- Progress bar con indicador de paso actual
- Cálculo de completitud en tiempo real
- Guardado automático al avanzar
- Cálculo de riesgo final
- Validación cruzada
- Notificación a autoridades

**Hooks Principales:**
```typescript
const [currentStep, setCurrentStep] = useState(1);
const [friaData, setFriaData] = useState<FriaData>({});
const [completenessScore, setCompletenessScore] = useState(0);
const [finalRisk, setFinalRisk] = useState<number | null>(null);
```

**Funciones Principales:**
```typescript
// Guardar paso
const saveStep = async (stepNumber: number, stepData: any) => {
  const response = await fetch(`/api/v1/fria/${friaId}/step/${stepNumber}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(stepData),
  });
  const result = await response.json();
  setCompletenessScore(result.completenessScore);
};

// Calcular riesgo
const calculateRisk = async () => {
  const response = await fetch(`/api/v1/fria/${friaId}/calculate-risk`, {
    method: 'POST',
  });
  const result = await response.json();
  setFinalRisk(result.finalRisk);
};
```

#### 2. Listado de Proyectos (`projects/page.tsx`)

**Características:**
- Lista de proyectos con evaluaciones FRIA agrupadas
- Estadísticas (total proyectos, total FRIA, notificadas, borradores)
- Filtros (búsqueda, estado)
- Paginación
- Expandir/contraer proyectos
- Acciones por proyecto (Nueva FRIA, Ver, Editar, Crear Versión)

**Hooks Principales:**
```typescript
const [projects, setProjects] = useState<ProjectWithFrias[]>([]);
const [currentPage, setCurrentPage] = useState(1);
const [searchQuery, setSearchQuery] = useState('');
const [filterStatus, setFilterStatus] = useState('all');
const [statistics, setStatistics] = useState<Statistics>({});
```

**Funciones Principales:**
```typescript
// Cargar proyectos
const loadProjects = async () => {
  const params = new URLSearchParams();
  params.append('page', String(currentPage - 1));
  params.append('size', String(ITEMS_PER_PAGE));
  if (filterStatus !== 'all') {
    params.append('status', filterStatus);
  }
  if (searchQuery) {
    params.append('search', searchQuery);
  }

  const response = await fetch(`/api/v1/fria/projects?${params.toString()}`);
  const data = await response.json();
  setProjects(data.projects);
  setStatistics(data.statistics);
};
```

#### 3. Detalle FRIA (`[id]/page.tsx`)

**Características:**
- Vista completa de todos los 6 pasos
- Cards de resumen (Estado, Completitud, Riesgo, Fecha)
- Información detallada de riesgos y medidas
- Acciones (Editar, Notificar, Exportar PDF)

**Funciones Principales:**
```typescript
// Cargar detalle
const loadFria = async () => {
  const response = await fetch(`/api/v1/fria/${friaId}`);
  const fria = await response.json();
  setFriaData(fria);
};
```

---

## 💡 EJEMPLOS DE USO

### Ejemplo 1: Crear Nueva FRIA

```typescript
'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function CreateFriaButton({ projectId }: { projectId: number }) {
  const router = useRouter();
  const [loading, setLoading] = useState(false);

  const handleCreateFria = async () => {
    setLoading(true);
    try {
      const response = await fetch('/api/v1/fria/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          projectId,
          userId: 123, // Obtener del contexto de usuario
        }),
      });

      if (!response.ok) {
        throw new Error('Error creando FRIA');
      }

      const fria = await response.json();
      router.push(`/governance/compliance/fria?friaId=${fria.id}`);
    } catch (error) {
      console.error('Error:', error);
      alert('Error al crear FRIA');
    } finally {
      setLoading(false);
    }
  };

  return (
    <button onClick={handleCreateFria} disabled={loading}>
      {loading ? 'Creando...' : 'Nueva FRIA'}
    </button>
  );
}
```

### Ejemplo 2: Actualizar Paso del Wizard

```typescript
const updateStep1 = async (friaId: number, processDescription: string) => {
  const response = await fetch(`/api/v1/fria/${friaId}/step/1`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      processDescription,
    }),
  });

  if (!response.ok) {
    throw new Error('Error actualizando paso');
  }

  const result = await response.json();
  console.log('Completitud:', result.completenessScore);
  return result;
};
```

### Ejemplo 3: Calcular y Mostrar Riesgo

```typescript
const calculateAndDisplayRisk = async (friaId: number) => {
  const response = await fetch(`/api/v1/fria/${friaId}/calculate-risk`, {
    method: 'POST',
  });

  if (!response.ok) {
    throw new Error('Error calculando riesgo');
  }

  const result = await response.json();

  // Mostrar resultado
  alert(`Riesgo Final: ${(result.finalRisk * 100).toFixed(0)}%\nNivel: ${result.riskLevel}`);

  return result;
};
```

### Ejemplo 4: Validación Cruzada

```typescript
const performCrossValidation = async (friaId: number) => {
  const response = await fetch(`/api/v1/fria/${friaId}/cross-validate`, {
    method: 'POST',
  });

  if (!response.ok) {
    throw new Error('Error en validación cruzada');
  }

  const result = await response.json();

  if (result.requiresJustification) {
    // Mostrar inconsistencias y solicitar justificación
    console.warn('Inconsistencias detectadas:', result.inconsistencies);
    // Abrir diálogo para justificación
  }

  return result;
};
```

---

## ⚠️ MANEJO DE ERRORES

### Estrategia General

```typescript
const handleApiCall = async (apiCall: () => Promise<Response>) => {
  try {
    const response = await apiCall();

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error('API Error:', error);

    // Mostrar mensaje al usuario
    if (error instanceof Error) {
      alert(`Error: ${error.message}`);
    } else {
      alert('Error desconocido');
    }

    throw error;
  }
};
```

### Errores Específicos

```typescript
// Error de validación
if (response.status === 400) {
  const error = await response.json();
  console.error('Error de validación:', error);
  // Mostrar errores de validación al usuario
}

// Error de autorización
if (response.status === 401 || response.status === 403) {
  // Redirigir a login
  window.location.href = '/login';
}

// Error del servidor
if (response.status >= 500) {
  // Mostrar mensaje genérico
  alert('Error del servidor. Por favor, intente más tarde.');
}
```

---

## 🎭 MOCK DATA

### Configuración

El sistema soporta modo MOCK mediante variable de entorno:

```typescript
const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
```

### Ejemplo de Mock

```typescript
const mockFriaData: FriaAssessmentDto = {
  id: 1,
  projectId: 1001,
  projectName: 'AI Credit Scoring System',
  status: 'DRAFT',
  completenessScore: 0.67,
  finalRisk: null,
  riskLevel: null,
  processDescription: 'El sistema se usa para evaluación crediticia...',
  // ... más campos
};

const loadFria = async (friaId: number) => {
  if (USE_MOCK) {
    return mockFriaData;
  }

  const response = await fetch(`/api/v1/fria/${friaId}`);
  return await response.json();
};
```

---

## 🌐 TRADUCCIONES

### Ubicación

Las traducciones están en:
```
app/config/i18n/modules/governance/compliance.ts
```

### Uso

```typescript
import { useTranslation } from 'react-i18next';

const { t } = useTranslation();

// Usar traducción
const title = t('governance.compliance.fria.projects.title');
```

### Claves Principales

```typescript
// Pantalla de proyectos
'governance.compliance.fria.projects.title'
'governance.compliance.fria.projects.subtitle'
'governance.compliance.fria.projects.newFria'

// Estados
'governance.compliance.fria.status.draft'
'governance.compliance.fria.status.completed'
'governance.compliance.fria.status.notified'
'governance.compliance.fria.status.approved'

// Niveles de riesgo
'governance.compliance.fria.riskLevels.low'
'governance.compliance.fria.riskLevels.medium'
'governance.compliance.fria.riskLevels.high'
'governance.compliance.fria.riskLevels.critical'
```

---

## 📚 RECURSOS ADICIONALES

### Documentación Relacionada

- **Guía Funcional:** `docs/prompts/compliance/fria/user_guide/GUIA_FUNCIONAL_FRIA.md`
- **Guía de Uso:** `docs/prompts/compliance/fria/user_guide/GUIA_USO_PANTALLAS_FRIA.md`
- **Estado de Implementación:** `docs/prompts/compliance/fria/ESTADO_IMPLEMENTACION_FRIA.md`

### Referencias Técnicas

- **EU AI Act Art. 27:** Requisitos de evaluación FRIA
- **Anexo IX:** Fórmula de cálculo de riesgo
- **INC-007:** Especificación de validación cruzada

---

## ⚙️ CONFIGURACIÓN

### Variables de Entorno

**Archivo:** `.env.local` (desarrollo) o `.env.production` (producción)

**Variables Disponibles:**
```bash
# Modo Mock (desarrollo)
NEXT_PUBLIC_USE_MOCK=false

# URL del BFF
NEXT_PUBLIC_API_URL=http://localhost:8083

# Configuración de i18n
NEXT_PUBLIC_DEFAULT_LOCALE=es
NEXT_PUBLIC_SUPPORTED_LOCALES=es,en,it,pt,fr,de
```

### Configuración de Next.js

**Archivo:** `next.config.js` o `next.config.ts`

**Configuración Recomendada:**
```javascript
/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  i18n: {
    locales: ['es', 'en', 'it', 'pt', 'fr', 'de'],
    defaultLocale: 'es',
  },
  // Configuración de imágenes si se usan
  images: {
    domains: ['localhost'],
  },
};

module.exports = nextConfig;
```

### Configuración de i18n

**Archivo:** `app/config/i18n/modules/governance/compliance.ts`

**Estructura:**
```typescript
export const governanceComplianceTranslations = {
  es: {
    governance: {
      compliance: {
        fria: {
          // Traducciones FRIA
        },
      },
    },
  },
  // ... otros idiomas
};
```

### Configuración del Menú

**Archivo:** `app/config/modules.ts`

**Estructura:**
```typescript
{
  name: "FRIA Projects",
  href: `/governance/compliance/fria/projects`,
  icon: "Shield",
  roles: ["admin", "compliance_officer", "project_manager"],
}
```

---

## 🧪 TESTING

### Configuración de Testing

**Dependencias:**
```json
{
  "devDependencies": {
    "@testing-library/react": "^14.0.0",
    "@testing-library/jest-dom": "^6.0.0",
    "@testing-library/user-event": "^14.0.0",
    "jest": "^29.0.0",
    "jest-environment-jsdom": "^29.0.0"
  }
}
```

### Tests Unitarios

#### Ejemplo: Test del Wizard

**Ubicación:** `app/(app)/governance/compliance/fria/__tests__/page.test.tsx`

```typescript
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { useRouter } from 'next/navigation';
import FriaWizardPage from '../page';

jest.mock('next/navigation', () => ({
  useRouter: jest.fn(),
  useSearchParams: jest.fn(),
}));

describe('FriaWizardPage', () => {
  beforeEach(() => {
    (useRouter as jest.Mock).mockReturnValue({
      push: jest.fn(),
    });
  });

  it('should render wizard with 6 steps', () => {
    render(<FriaWizardPage />);

    expect(screen.getByText(/Paso 1/i)).toBeInTheDocument();
    expect(screen.getByText(/Paso 6/i)).toBeInTheDocument();
  });

  it('should navigate to next step', async () => {
    render(<FriaWizardPage />);

    const nextButton = screen.getByRole('button', { name: /siguiente/i });
    fireEvent.click(nextButton);

    await waitFor(() => {
      expect(screen.getByText(/Paso 2/i)).toBeInTheDocument();
    });
  });

  it('should save step data', async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ success: true, completenessScore: 0.17 }),
    });

    render(<FriaWizardPage />);

    const descriptionInput = screen.getByLabelText(/descripción/i);
    fireEvent.change(descriptionInput, {
      target: { value: 'Test description' }
    });

    const nextButton = screen.getByRole('button', { name: /siguiente/i });
    fireEvent.click(nextButton);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/v1/fria/'),
        expect.objectContaining({
          method: 'PUT',
        })
      );
    });
  });
});
```

#### Ejemplo: Test de Listado de Proyectos

**Ubicación:** `app/(app)/governance/compliance/fria/projects/__tests__/page.test.tsx`

```typescript
import { render, screen, waitFor } from '@testing-library/react';
import FriaProjectsPage from '../page';

describe('FriaProjectsPage', () => {
  it('should load and display projects', async () => {
    const mockProjects = {
      projects: [
        {
          projectId: 1,
          projectName: 'Test Project',
          totalFrias: 2,
          latestFria: {
            id: 1,
            status: 'COMPLETED',
            completenessScore: 0.95,
          },
        },
      ],
      statistics: {
        totalProjects: 1,
        totalFrias: 2,
      },
    };

    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => mockProjects,
    });

    render(<FriaProjectsPage />);

    await waitFor(() => {
      expect(screen.getByText('Test Project')).toBeInTheDocument();
    });
  });

  it('should handle API errors gracefully', async () => {
    global.fetch = jest.fn().mockRejectedValue(new Error('API Error'));

    render(<FriaProjectsPage />);

    await waitFor(() => {
      // Verificar que se muestra mensaje de error o fallback
      expect(screen.getByText(/error/i)).toBeInTheDocument();
    });
  });
});
```

### Tests de Integración

#### Ejemplo: Test E2E del Flujo Completo

**Ubicación:** `e2e/fria-wizard.spec.ts`

```typescript
import { test, expect } from '@playwright/test';

test('complete FRIA wizard flow', async ({ page }) => {
  // 1. Navegar a crear FRIA
  await page.goto('/governance/compliance/fria/projects');
  await page.click('button:has-text("Nueva FRIA")');

  // 2. Completar Paso 1
  await page.fill('textarea[name="processDescription"]',
    'Sistema de evaluación crediticia...');
  await page.click('button:has-text("Siguiente")');

  // 3. Completar Paso 2
  await page.fill('input[name="usagePeriodStart"]', '2025-01-01');
  await page.fill('input[name="usagePeriodEnd"]', '2025-12-31');
  await page.selectOption('select[name="usageFrequency"]', 'DAILY');
  await page.click('button:has-text("Siguiente")');

  // 4. Verificar progreso
  const progress = await page.textContent('.progress-bar');
  expect(progress).toContain('33%');

  // 5. Completar todos los pasos...
  // ...

  // 6. Calcular riesgo
  await page.click('button:has-text("Calcular Riesgo")');
  await expect(page.locator('.risk-level')).toBeVisible();

  // 7. Completar FRIA
  await page.click('button:has-text("Completar FRIA")');
  await expect(page.locator('.success-message')).toBeVisible();
});
```

### Mocking de APIs

#### Ejemplo: Mock de Fetch

```typescript
// __mocks__/fetch.ts
export default jest.fn(() =>
  Promise.resolve({
    ok: true,
    json: async () => ({}),
  })
);
```

#### Ejemplo: Mock de useRouter

```typescript
jest.mock('next/navigation', () => ({
  useRouter: () => ({
    push: jest.fn(),
    replace: jest.fn(),
    back: jest.fn(),
  }),
  useSearchParams: () => new URLSearchParams(),
  usePathname: () => '/governance/compliance/fria',
}));
```

---

## 🔍 TROUBLESHOOTING

### Problemas Comunes y Soluciones

#### 1. Error: "Cannot read property of undefined"

**Síntomas:**
- Error en consola del navegador
- Componente no renderiza
- Datos no cargan

**Causas:**
- Datos no inicializados
- Estado asíncrono no manejado
- Propiedades undefined

**Solución:**
1. Agregar validaciones:
   ```typescript
   if (!friaData || !friaData.risks) {
     return <div>Loading...</div>;
   }
   ```

2. Usar optional chaining:
   ```typescript
   const riskLevel = friaData?.finalRisk?.riskLevel;
   ```

3. Inicializar estados:
   ```typescript
   const [friaData, setFriaData] = useState<FriaData | null>(null);
   ```

#### 2. Error: "Failed to fetch"

**Síntomas:**
- Llamadas API fallan
- Error 404 o 500
- Network error en consola

**Causas:**
- BFF no disponible
- URL incorrecta
- CORS issues
- Timeout

**Solución:**
1. Verificar que BFF está corriendo:
   ```bash
   curl http://localhost:8083/actuator/health
   ```

2. Verificar URL en código:
   ```typescript
   const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8083';
   ```

3. Verificar CORS en BFF:
   ```java
   @CrossOrigin(origins = "http://localhost:3000")
   ```

4. Aumentar timeout:
   ```typescript
   const controller = new AbortController();
   const timeoutId = setTimeout(() => controller.abort(), 30000);
   ```

#### 3. Error: "Button does nothing"

**Síntomas:**
- Botones no responden
- Navegación no funciona
- Eventos no se disparan

**Causas:**
- Event handlers no definidos
- Event propagation bloqueada
- Estado no actualizado

**Solución:**
1. Verificar event handlers:
   ```typescript
   <button onClick={(e) => {
     e.preventDefault();
     e.stopPropagation();
     handleClick();
   }}>
   ```

2. Verificar que funciones están definidas:
   ```typescript
   const handleClick = () => {
     console.log('Button clicked');
     // ... lógica
   };
   ```

3. Usar `window.location` para navegación si Next.js router falla:
   ```typescript
   window.location.href = '/governance/compliance/fria/projects';
   ```

#### 4. Error: "Translation key not found"

**Síntomas:**
- Texto muestra clave de traducción
- Ej: "governance.compliance.fria.title"
- Traducciones no cargan

**Causas:**
- Clave de traducción incorrecta
- Traducción no existe
- i18n no configurado

**Solución:**
1. Verificar clave en `compliance.ts`:
   ```typescript
   // Buscar en app/config/i18n/modules/governance/compliance.ts
   ```

2. Agregar traducción faltante:
   ```typescript
   fria: {
     title: "Evaluación FRIA", // Agregar si falta
   }
   ```

3. Verificar que i18n está configurado:
   ```typescript
   import { useTranslation } from 'react-i18next';
   const { t } = useTranslation();
   ```

#### 5. Error: "Cannot update during render"

**Síntomas:**
- Warning en consola
- Estado no se actualiza
- Componente se re-renderiza infinitamente

**Causas:**
- Actualización de estado durante render
- useEffect sin dependencias correctas
- Llamadas API en render

**Solución:**
1. Mover lógica a useEffect:
   ```typescript
   useEffect(() => {
     loadData();
   }, [friaId]); // Dependencias correctas
   ```

2. Evitar actualizaciones en render:
   ```typescript
   // ❌ MAL
   const [data, setData] = useState(loadData()); // Llamada en render

   // ✅ BIEN
   const [data, setData] = useState(null);
   useEffect(() => {
     loadData().then(setData);
   }, []);
   ```

#### 6. Error: "Pagination not working"

**Síntomas:**
- Paginación no cambia página
- Datos no se actualizan
- Botones deshabilitados incorrectamente

**Causas:**
- Estado de página no actualizado
- useEffect no se dispara
- Backend no recibe parámetros correctos

**Solución:**
1. Verificar que estado se actualiza:
   ```typescript
   const handlePageChange = (newPage: number) => {
     setCurrentPage(newPage);
     // useEffect debería dispararse automáticamente
   };
   ```

2. Verificar dependencias de useEffect:
   ```typescript
   useEffect(() => {
     loadProjects();
   }, [currentPage, searchQuery, filterStatus]); // Todas las dependencias
   ```

3. Verificar parámetros en URL:
   ```typescript
   const params = new URLSearchParams();
   params.append('page', String(currentPage - 1)); // Backend usa 0-indexed
   ```

### Debugging Tips

1. **Usar React DevTools:**
   - Inspeccionar estado de componentes
   - Ver props y hooks
   - Profiler para performance

2. **Console Logging:**
   ```typescript
   console.log('Current state:', { currentStep, friaData, completenessScore });
   ```

3. **Network Tab:**
   - Verificar requests HTTP
   - Verificar responses
   - Verificar errores

4. **Breakpoints:**
   ```typescript
   debugger; // Pausa ejecución
   ```

---

## ⚡ PERFORMANCE Y OPTIMIZACIÓN

### Optimizaciones Aplicadas

1. **Lazy Loading:**
   - Componentes pesados cargados bajo demanda
   - Imágenes con `next/image`

2. **Memoización:**
   ```typescript
   const memoizedProjects = useMemo(() => {
     return projects.filter(/* ... */);
   }, [projects, filterStatus]);
   ```

3. **Debouncing en Búsqueda:**
   ```typescript
   const debouncedSearch = useDebounce(searchQuery, 300);
   useEffect(() => {
     loadProjects();
   }, [debouncedSearch]);
   ```

4. **Paginación del Backend:**
   - No cargar todos los datos a la vez
   - Paginación en servidor

### Consideraciones de Performance

1. **Evitar Re-renders Innecesarios:**
   - Usar `React.memo` para componentes pesados
   - Usar `useMemo` para cálculos costosos
   - Usar `useCallback` para funciones

2. **Optimizar Imágenes:**
   - Usar `next/image` con optimización automática
   - Lazy loading de imágenes

3. **Code Splitting:**
   - Next.js hace code splitting automático por ruta
   - Lazy load componentes pesados

### Métricas de Performance

**Herramientas:**
- Lighthouse (Chrome DevTools)
- Next.js Analytics
- Web Vitals

**Métricas Objetivo:**
- First Contentful Paint (FCP): < 1.8s
- Largest Contentful Paint (LCP): < 2.5s
- Time to Interactive (TTI): < 3.8s

---

## 🔒 SEGURIDAD

### Validaciones de Seguridad

1. **Validación de Input:**
   ```typescript
   const validateStep = (stepData: any) => {
     if (stepData.processDescription && stepData.processDescription.length < 50) {
       throw new Error('Descripción debe tener al menos 50 caracteres');
     }
   };
   ```

2. **Sanitización:**
   - Next.js sanitiza automáticamente
   - No usar `dangerouslySetInnerHTML` a menos que sea necesario

3. **Autenticación:**
   - Verificar que usuario está autenticado
   - Redirigir a login si no está autenticado

4. **Autorización:**
   - Verificar roles antes de mostrar acciones
   - Ocultar botones si usuario no tiene permisos

### Consideraciones de Privacidad

1. **No Almacenar Datos Sensibles:**
   - No almacenar datos personales en localStorage
   - Limpiar datos al cerrar sesión

2. **HTTPS:**
   - Usar HTTPS en producción
   - No enviar datos sensibles por HTTP

---

## 📚 RECURSOS ADICIONALES

### Documentación Relacionada

- **Guía Funcional:** `docs/prompts/compliance/fria/user_guide/GUIA_FUNCIONAL_FRIA.md`
- **Guía de Uso:** `docs/prompts/compliance/fria/user_guide/GUIA_USO_PANTALLAS_FRIA.md`
- **Guía Backend:** `docs/prompts/compliance/fria/DEVELOPER_GUIDE_BACKEND.md`
- **Estado de Implementación:** `docs/prompts/compliance/fria/ESTADO_IMPLEMENTACION_FRIA.md`

### Referencias Técnicas

- **Next.js Docs:** https://nextjs.org/docs
- **React Docs:** https://react.dev
- **TypeScript Docs:** https://www.typescriptlang.org/docs
- **Tailwind CSS:** https://tailwindcss.com/docs

---

**Última actualización:** Diciembre 2025
