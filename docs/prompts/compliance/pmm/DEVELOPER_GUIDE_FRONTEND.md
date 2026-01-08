# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - PMM

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
Business Microservice (PMM Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**BFF Endpoint Base:**
```
/api/v1/pmm
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS BFF

### 1. Dashboard PMM

#### GET `/api/v1/pmm/dashboard`

**Descripción:** Obtiene métricas principales y sistemas en producción.

**Query Parameters:**
- `projectId` (opcional): Filtrar por proyecto específico

**Response:**
```typescript
interface PMMDashboardDto {
  totalSystems: number;
  activeIncidents: number;
  pendingActions: number;
  slaCompliance: number;
  systems: Array<{
    id: number;
    projectId: number;
    projectName: string;
    status: "HEALTHY" | "WARNING" | "CRITICAL";
    accuracy: number;
    latency: number;
    throughput: number;
    lastCheck: string; // ISO 8601
    driftDetected: boolean;
    anomaliesDetected: boolean;
  }>;
}
```

**Ejemplo de Uso:**
```typescript
// Sin filtro
const response = await fetch('/api/v1/pmm/dashboard');
const data = await response.json();

// Con filtro de proyecto
const response = await fetch('/api/v1/pmm/dashboard?projectId=1');
const data = await response.json();
```

---

### 2. Gestión de Incidentes

#### GET `/api/v1/pmm/incidents`

**Descripción:** Lista incidentes con filtros opcionales.

**Query Parameters:**
- `severity` (opcional): LOW, MEDIUM, HIGH, CRITICAL
- `status` (opcional): OPEN, INVESTIGATING, RESOLVED, CLOSED
- `projectId` (opcional): Filtrar por proyecto
- `search` (opcional): Búsqueda por texto en descripción

**Response:**
```typescript
interface IncidentDto {
  id: number;
  projectId: number;
  projectName: string;
  severity: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
  status: "OPEN" | "INVESTIGATING" | "RESOLVED" | "CLOSED";
  description: string;
  impact: string;
  reportedAt: string; // ISO 8601
  authorityNotified: boolean;
  notifiedAt?: string; // ISO 8601
}
```

**Ejemplo de Uso:**
```typescript
// Todos los incidentes
const response = await fetch('/api/v1/pmm/incidents');
const incidents = await response.json();

// Filtrados por proyecto y severidad
const response = await fetch(
  '/api/v1/pmm/incidents?projectId=1&severity=HIGH'
);
const incidents = await response.json();
```

#### POST `/api/v1/pmm/incidents`

**Descripción:** Reporta un nuevo incidente. Si severidad >= HIGH, notifica autoridades automáticamente (Art. 20.1).

**Request Body:**
```typescript
interface ReportIncidentRequest {
  projectId: number;
  severity: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
  description: string;
  impact: string;
}
```

**Response:**
```typescript
IncidentDto // Mismo formato que GET
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/v1/pmm/incidents', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    projectId: 1,
    severity: 'HIGH',
    description: 'Degradación de accuracy detectada',
    impact: 'Puede afectar decisiones crediticias',
  }),
});
const incident = await response.json();
```

#### POST `/api/v1/pmm/incidents/{id}/notify-authority`

**Descripción:** Notifica manualmente a autoridades sobre un incidente (Art. 20.1).

**Path Parameters:**
- `id`: ID del incidente

**Response:**
```typescript
// 200 OK (sin body)
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/v1/pmm/incidents/${incidentId}/notify-authority`, {
  method: 'POST',
});
```

---

### 3. Acciones Correctoras

#### GET `/api/v1/pmm/corrective-actions`

**Descripción:** Lista acciones correctoras con filtros opcionales.

**Query Parameters:**
- `status` (opcional): PLANNED, IN_PROGRESS, COMPLETED
- `incidentId` (opcional): Filtrar por incidente
- `search` (opcional): Búsqueda por texto

**Response:**
```typescript
interface CorrectiveActionDto {
  id: number;
  incidentId: number;
  incidentDescription: string;
  description: string;
  status: "PLANNED" | "IN_PROGRESS" | "COMPLETED";
  plannedDate: string; // ISO 8601
  startDate?: string; // ISO 8601
  completedDate?: string; // ISO 8601
  effectiveness?: number; // 0.00 - 1.00
}
```

**Ejemplo de Uso:**
```typescript
// Todas las acciones
const response = await fetch('/api/v1/pmm/corrective-actions');
const actions = await response.json();

// Filtradas por incidente
const response = await fetch(
  '/api/v1/pmm/corrective-actions?incidentId=1'
);
const actions = await response.json();
```

#### POST `/api/v1/pmm/corrective-actions`

**Descripción:** Crea una nueva acción correctora.

**Request Body:**
```typescript
interface CreateCorrectiveActionRequest {
  incidentId: number;
  description: string;
  plannedDate: string; // ISO 8601
}
```

**Response:**
```typescript
CorrectiveActionDto // Mismo formato que GET
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/v1/pmm/corrective-actions', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    incidentId: 1,
    description: 'Reentrenar modelo con datos actualizados',
    plannedDate: '2025-01-20T10:00:00Z',
  }),
});
const action = await response.json();
```

#### PATCH `/api/v1/pmm/corrective-actions/{id}`

**Descripción:** Actualiza estado o efectividad de una acción correctora.

**Path Parameters:**
- `id`: ID de la acción

**Request Body:**
```typescript
interface UpdateCorrectiveActionRequest {
  status?: "PLANNED" | "IN_PROGRESS" | "COMPLETED";
  effectiveness?: number; // 0.00 - 1.00
}
```

**Response:**
```typescript
CorrectiveActionDto
```

**Ejemplo de Uso:**
```typescript
// Actualizar estado
const response = await fetch(`/api/v1/pmm/corrective-actions/${actionId}`, {
  method: 'PATCH',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    status: 'COMPLETED',
    effectiveness: 0.85,
  }),
});
const updatedAction = await response.json();
```

---

### 4. Planes PMM

#### GET `/api/v1/pmm/plans`

**Descripción:** Lista planes de monitoreo post-mercado.

**Query Parameters:**
- `projectId` (opcional): Filtrar por proyecto
- `status` (opcional): DRAFT, ACTIVE, SUSPENDED, ARCHIVED

**Response:**
```typescript
interface PostMarketMonitoringPlanDto {
  id: number;
  name: string;
  projectId: number;
  projectName: string;
  modelId?: number;
  monitoringFrequency: "HOURLY" | "DAILY" | "WEEKLY" | "MONTHLY" | "CUSTOM";
  customFrequencyHours?: number;
  metrics: string; // JSON string
  alertThresholds: string; // JSON string
  reportingFrequency: "DAILY" | "WEEKLY" | "MONTHLY" | "QUARTERLY" | "ANNUAL";
  status: "DRAFT" | "ACTIVE" | "SUSPENDED" | "ARCHIVED";
  lastMonitoringDate?: string; // ISO 8601
  createdAt: string; // ISO 8601
  updatedAt: string; // ISO 8601
}
```

**Ejemplo de Uso:**
```typescript
// Todos los planes
const response = await fetch('/api/v1/pmm/plans');
const plans = await response.json();

// Filtrados por proyecto y estado
const response = await fetch(
  '/api/v1/pmm/plans?projectId=1&status=ACTIVE'
);
const plans = await response.json();
```

#### GET `/api/v1/pmm/plans/{id}`

**Descripción:** Obtiene un plan por ID.

**Path Parameters:**
- `id`: ID del plan

**Response:**
```typescript
PostMarketMonitoringPlanDto
```

#### POST `/api/v1/pmm/plans`

**Descripción:** Crea un nuevo plan de monitoreo post-mercado.

**Request Body:**
```typescript
interface CreatePMMPlanRequest {
  pmmplanname: string;
  idxproject: number;
  idxmodel?: number;
  pmmmonitoringfrequency: "HOURLY" | "DAILY" | "WEEKLY" | "MONTHLY" | "CUSTOM";
  pmmcustomfrequencyhours?: number;
  pmmmetrics: string; // JSON string
  pmmalertthresholds: string; // JSON string
  pmmreportingfrequency: "DAILY" | "WEEKLY" | "MONTHLY" | "QUARTERLY" | "ANNUAL";
}
```

**Ejemplo de Request Body:**
```json
{
  "pmmplanname": "Plan Monitoreo Diario - AI Credit Scoring",
  "idxproject": 1,
  "pmmmonitoringfrequency": "DAILY",
  "pmmmetrics": "{\"accuracy\": true, \"latency\": true, \"throughput\": true}",
  "pmmalertthresholds": "{\"accuracy\": {\"warning\": 0.05, \"critical\": 0.10}}",
  "pmmreportingfrequency": "WEEKLY"
}
```

**Response:**
```typescript
PostMarketMonitoringPlanDto
```

#### PATCH `/api/v1/pmm/plans/{id}`

**Descripción:** Actualiza un plan de monitoreo post-mercado.

**Request Body:**
```typescript
interface UpdatePMMPlanRequest {
  pmmplanname?: string;
  pmmmonitoringfrequency?: "HOURLY" | "DAILY" | "WEEKLY" | "MONTHLY" | "CUSTOM";
  pmmcustomfrequencyhours?: number;
  pmmmetrics?: string;
  pmmalertthresholds?: string;
  pmmreportingfrequency?: "DAILY" | "WEEKLY" | "MONTHLY" | "QUARTERLY" | "ANNUAL";
}
```

#### POST `/api/v1/pmm/plans/{id}/activate`

**Descripción:** Activa un plan PMM. Solo puede haber un plan activo por proyecto.

**Path Parameters:**
- `id`: ID del plan

**Response:**
```typescript
PostMarketMonitoringPlanDto
```

**Nota:** Si hay otro plan ACTIVE del mismo proyecto, se suspende automáticamente.

#### POST `/api/v1/pmm/plans/{id}/suspend`

**Descripción:** Suspende un plan PMM activo.

**Path Parameters:**
- `id`: ID del plan

**Response:**
```typescript
PostMarketMonitoringPlanDto
```

#### DELETE `/api/v1/pmm/plans/{id}`

**Descripción:** Elimina (archiva) un plan PMM.

**Path Parameters:**
- `id`: ID del plan

**Response:**
```typescript
// 200 OK (sin body)
```

---

### 5. Reportes de Vigilancia

#### GET `/api/v1/pmm/reports`

**Descripción:** Lista reportes de vigilancia post-mercado.

**Query Parameters:**
- `projectId` (opcional): Filtrar por proyecto
- `reportType` (opcional): DAILY, WEEKLY, MONTHLY, AD_HOC

**Response:**
```typescript
interface PostMarketSurveillanceReportDto {
  id: number;
  projectId: number;
  projectName: string;
  modelId?: number;
  reportType: "DAILY" | "WEEKLY" | "MONTHLY" | "AD_HOC";
  reportDate: string; // ISO 8601
  status: "DRAFT" | "GENERATED" | "APPROVED" | "ARCHIVED";
  reportData: string; // JSON string
  pdfPath?: string;
  createdAt: string; // ISO 8601
  updatedAt: string; // ISO 8601
}
```

#### GET `/api/v1/pmm/reports/{id}`

**Descripción:** Obtiene un reporte por ID.

**Path Parameters:**
- `id`: ID del reporte

**Response:**
```typescript
PostMarketSurveillanceReportDto
```

#### GET `/api/v1/pmm/reports/{id}/pdf`

**Descripción:** Descarga el PDF del reporte.

**Path Parameters:**
- `id`: ID del reporte

**Response:**
```typescript
// Binary PDF file
// Content-Type: application/pdf
// Content-Disposition: attachment; filename="report-{id}.pdf"
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/v1/pmm/reports/${reportId}/pdf`);
const blob = await response.blob();
const url = window.URL.createObjectURL(blob);
const a = document.createElement('a');
a.href = url;
a.download = `report-${reportId}.pdf`;
a.click();
```

#### POST `/api/v1/pmm/reports/generate`

**Descripción:** Genera un nuevo reporte de vigilancia post-mercado.

**Query Parameters:**
- `projectId` (requerido): ID del proyecto
- `modelId` (opcional): ID del modelo
- `reportType` (requerido): DAILY, WEEKLY, MONTHLY, AD_HOC
- `reportDate` (opcional): Fecha del reporte (ISO 8601)

**Response:**
```typescript
PostMarketSurveillanceReportDto
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(
  '/api/v1/pmm/reports/generate?projectId=1&reportType=WEEKLY',
  {
    method: 'POST',
  }
);
const report = await response.json();
```

#### POST `/api/v1/pmm/reports/{id}/approve`

**Descripción:** Aprueba un reporte de vigilancia generado.

**Path Parameters:**
- `id`: ID del reporte

**Response:**
```typescript
PostMarketSurveillanceReportDto
```

---

## ⚛️ INTEGRACIÓN EN NEXT.JS

### Configuración Base

**Archivo:** `app/(app)/governance/compliance/post-market-monitoring/page.tsx`

**Patrón de Uso:**
```typescript
'use client';

import { useState, useEffect } from 'react';
import { useSearchParams } from 'next/navigation';

export default function PostMarketMonitoringPage() {
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get('projectId');

  const [pmmData, setPmmData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, [projectIdFromUrl]);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      const url = projectIdFromUrl
        ? `/api/v1/pmm/dashboard?projectId=${projectIdFromUrl}`
        : '/api/v1/pmm/dashboard';

      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to load dashboard');

      const data = await response.json();
      setPmmData(data);
    } catch (error) {
      console.error('Error loading dashboard:', error);
    } finally {
      setLoading(false);
    }
  };

  // ... resto del componente
}
```

### Manejo de Mock Data

**Archivo:** `app/(app)/governance/data/mockPMM.ts`

**Patrón:**
```typescript
const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === 'true';

export const loadDashboard = async (projectId?: number) => {
  if (USE_MOCK_DATA) {
    return mockPMMData; // Datos mock
  }

  const url = projectId
    ? `/api/v1/pmm/dashboard?projectId=${projectId}`
    : '/api/v1/pmm/dashboard';

  const response = await fetch(url);
  return response.json();
};
```

---

## 📝 EJEMPLOS DE USO

### Ejemplo 1: Cargar Dashboard con Filtro

```typescript
const [dashboard, setDashboard] = useState(null);

useEffect(() => {
  const projectId = searchParams.get('projectId');

  fetch(`/api/v1/pmm/dashboard${projectId ? `?projectId=${projectId}` : ''}`)
    .then(res => res.json())
    .then(data => setDashboard(data))
    .catch(err => console.error(err));
}, [searchParams]);
```

### Ejemplo 2: Reportar Incidente

```typescript
const reportIncident = async (incidentData: ReportIncidentRequest) => {
  try {
    const response = await fetch('/api/v1/pmm/incidents', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(incidentData),
    });

    if (!response.ok) {
      throw new Error('Failed to report incident');
    }

    const incident = await response.json();

    // Si severidad es HIGH o CRITICAL, ya se notificó automáticamente
    if (incident.severity === 'HIGH' || incident.severity === 'CRITICAL') {
      alert('Incidente reportado y autoridades notificadas automáticamente');
    }

    return incident;
  } catch (error) {
    console.error('Error reporting incident:', error);
    throw error;
  }
};
```

### Ejemplo 3: Crear y Activar Plan PMM

```typescript
const createAndActivatePlan = async (planData: CreatePMMPlanRequest) => {
  try {
    // 1. Crear plan
    const createResponse = await fetch('/api/v1/pmm/plans', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(planData),
    });

    const plan = await createResponse.json();

    // 2. Activar plan
    const activateResponse = await fetch(`/api/v1/pmm/plans/${plan.id}/activate`, {
      method: 'POST',
    });

    const activatedPlan = await activateResponse.json();

    return activatedPlan;
  } catch (error) {
    console.error('Error creating/activating plan:', error);
    throw error;
  }
};
```

### Ejemplo 4: Descargar PDF de Reporte

```typescript
const downloadReportPdf = async (reportId: number) => {
  try {
    const response = await fetch(`/api/v1/pmm/reports/${reportId}/pdf`);

    if (!response.ok) {
      throw new Error('Failed to download PDF');
    }

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `report-${reportId}.pdf`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error('Error downloading PDF:', error);
    throw error;
  }
};
```

---

## ⚠️ MANEJO DE ERRORES

### Códigos de Estado HTTP

- **200 OK:** Operación exitosa
- **400 Bad Request:** Request inválido (validación fallida)
- **404 Not Found:** Recurso no encontrado
- **500 Internal Server Error:** Error interno del servidor

### Patrón de Manejo

```typescript
const handleApiCall = async (url: string, options?: RequestInit) => {
  try {
    const response = await fetch(url, options);

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Recurso no encontrado');
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
NEXT_PUBLIC_USE_MOCK_DATA=true
```

**Archivo:** `app/(app)/governance/data/mockPMM.ts`

**Estructura:**
```typescript
export const mockPMMData = {
  totalSystems: 4,
  activeIncidents: 2,
  pendingActions: 3,
  slaCompliance: 95.5,
  systems: [
    {
      id: 1,
      projectId: 1,
      projectName: "AI Credit Scoring System",
      status: "HEALTHY",
      // ...
    },
    // ...
  ],
};

export const mockProjects = [
  { id: 1, name: "AI Credit Scoring System" },
  { id: 2, name: "Facial Recognition System" },
  // ...
];
```

**Uso:**
```typescript
const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK_DATA === 'true';

const loadData = async () => {
  if (USE_MOCK) {
    return mockPMMData;
  }
  return fetch('/api/v1/pmm/dashboard').then(res => res.json());
};
```

---

## 🔗 REFERENCIAS

- **BFF Controller:** `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/PMMController.java`
- **BFF Service:** `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/impl/PMMServiceImpl.java`
- **Pantallas Next.js:** `codeflowx-studio/app/(app)/governance/compliance/`

---

**Última Actualización:** Diciembre 2025
