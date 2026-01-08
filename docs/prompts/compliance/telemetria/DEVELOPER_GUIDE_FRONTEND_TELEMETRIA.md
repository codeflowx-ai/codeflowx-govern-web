# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - TELEMETRÍA

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
7. [Componentes y Pantallas](#componentes-y-pantallas)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
API Routes (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Telemetry Analytics Service)
    ↓
Database (PostgreSQL - codeflowx_telemetry)
```

### Base URL

**BFF Endpoint Base:**
```
/api/v1/telemetry-analytics
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.telemetry`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia
- Los endpoints retornan **DTOs tipados** (nunca `Map<String, Object>`)

---

## 🔌 ENDPOINTS BFF

### 1. KPIs Agregados

#### GET `/api/v1/telemetry-analytics/kpis`

**Descripción:** Obtiene KPIs agregados de telemetría.

**Query Parameters:**
- `startTime` (opcional): Fecha de inicio (ISO 8601)
- `endTime` (opcional): Fecha de fin (ISO 8601)

**Response:**
```typescript
interface TelemetryKpisResponseDto {
  totalEvents: number;
  totalCostUsd: number;
  totalTokens: number;
  avgLatencyMs: number;
  componentCount: number;
  startTime: string; // ISO 8601
  endTime: string; // ISO 8601
}
```

**Ejemplo de Uso:**
```typescript
// Sin filtros (últimos 7 días por defecto)
const response = await fetch('/api/v1/telemetry-analytics/kpis');
const data = await response.json();

// Con filtros de fecha
const startTime = new Date('2025-01-01').toISOString();
const endTime = new Date('2025-01-31').toISOString();
const response = await fetch(
  `/api/v1/telemetry-analytics/kpis?startTime=${startTime}&endTime=${endTime}`
);
const data = await response.json();
```

---

### 2. Búsqueda de Eventos

#### GET `/api/v1/telemetry-analytics/search/content`

**Descripción:** Busca eventos por contenido en payload JSONB.

**Query Parameters:**
- `searchText` (opcional): Texto a buscar (si está vacío, retorna todos los eventos)
- `startTime` (opcional): Fecha de inicio (ISO 8601)
- `endTime` (opcional): Fecha de fin (ISO 8601)
- `complianceStatus` (opcional): Estado de cumplimiento (PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION)
- `riskLevel` (opcional): Nivel de riesgo (LOW, MEDIUM, HIGH, CRITICAL)
- `complianceCategory` (opcional): Categoría de compliance (GDPR, SECURITY, LEGAL)
- `issueTag` (opcional): Etiqueta de problema (bias_critical, pii_exposure, secret_leak, etc.)
- `page` (opcional, default: 0): Número de página
- `size` (opcional, default: 20): Tamaño de página

**Response:**
```typescript
interface TelemetryEventsResponseDto {
  events: TelemetryEventDto[];
  total: number;
  page: number;
  size: number;
}

interface TelemetryEventDto {
  id: number;
  uuid: string;
  timestamp: string; // ISO 8601
  componentUuid: string;
  agentExternalId?: string;
  eventType: string;
  severity: string; // INFO, WARN, ERROR, DEBUG
  traceId?: string;
  runId?: string;
  metrics?: string; // JSON string
  payload?: string; // JSON string
  sourceTool?: string;
  biasChecked?: boolean;
  toxicityChecked?: boolean;
  piiDetected?: boolean;
  secretDetected?: boolean;
  complianceStatus?: string; // PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  riskLevel?: string; // LOW, MEDIUM, HIGH, CRITICAL
  complianceCategory?: string; // GDPR, SECURITY, LEGAL
  issueTags?: string; // JSON string array: ["bias_critical", "pii_exposure", etc.]
  analysisResults?: string; // JSON string
}
```

**Ejemplo de Uso:**
```typescript
// Búsqueda simple
const searchText = encodeURIComponent("error");
const startTime = new Date('2025-01-01').toISOString();
const endTime = new Date('2025-01-31').toISOString();

const response = await fetch(
  `/api/v1/telemetry-analytics/search/content?searchText=${searchText}&startTime=${startTime}&endTime=${endTime}&page=0&size=20`
);
const data = await response.json();

// Búsqueda con filtros de compliance/security
const params = new URLSearchParams({
  searchText: "error",
  startTime: startTime,
  endTime: endTime,
  complianceStatus: "CRITICAL_VIOLATION",
  riskLevel: "CRITICAL",
  complianceCategory: "SECURITY",
  issueTag: "secret_leak",
  page: "0",
  size: "20"
});

const response = await fetch(
  `/api/v1/telemetry-analytics/search/content?${params.toString()}`
);
const data = await response.json();
```

---

#### GET `/api/v1/telemetry-analytics/search/project`

**Descripción:** Busca eventos asociados a un proyecto.

**Query Parameters:**
- `projectId` (requerido): ID del proyecto
- `startTime` (opcional): Fecha de inicio (ISO 8601)
- `endTime` (opcional): Fecha de fin (ISO 8601)

**Response:**
```typescript
TelemetryEventsResponseDto // Mismo formato que search/content
```

**Ejemplo de Uso:**
```typescript
const projectId = 1;
const startTime = new Date('2025-01-01').toISOString();
const endTime = new Date('2025-01-31').toISOString();

const response = await fetch(
  `/api/v1/telemetry-analytics/search/project?projectId=${projectId}&startTime=${startTime}&endTime=${endTime}`
);
const data = await response.json();
```

---

### 3. Eventos por Componente

#### GET `/api/v1/telemetry-analytics/components/{componentUuid}/events`

**Descripción:** Obtiene eventos de un componente específico.

**Path Parameters:**
- `componentUuid`: UUID del componente

**Query Parameters:**
- `startTime` (opcional): Fecha de inicio (ISO 8601)
- `endTime` (opcional): Fecha de fin (ISO 8601)

**Response:**
```typescript
TelemetryEventsResponseDto
```

**Ejemplo de Uso:**
```typescript
const componentUuid = "550e8400-e29b-41d4-a716-446655440001";
const startTime = new Date('2025-01-01').toISOString();
const endTime = new Date('2025-01-31').toISOString();

const response = await fetch(
  `/api/v1/telemetry-analytics/components/${componentUuid}/events?startTime=${startTime}&endTime=${endTime}`
);
const data = await response.json();
```

---

### 4. Estadísticas por Componente

#### GET `/api/v1/telemetry-analytics/components/statistics`

**Descripción:** Obtiene estadísticas agregadas por componente.

**Query Parameters:**
- `startTime` (opcional): Fecha de inicio (ISO 8601)
- `endTime` (opcional): Fecha de fin (ISO 8601)

**Response:**
```typescript
interface ComponentStatisticsListResponseDto {
  statistics: ComponentStatisticsDto[];
  total: number;
}

interface ComponentStatisticsDto {
  componentUuid: string;
  totalEvents: number;
  avgLatencyMs: number;
  totalTokens: number;
  totalCostUsd: number;
}
```

**Ejemplo de Uso:**
```typescript
const startTime = new Date('2025-01-01').toISOString();
const endTime = new Date('2025-01-31').toISOString();

const response = await fetch(
  `/api/v1/telemetry-analytics/components/statistics?startTime=${startTime}&endTime=${endTime}`
);
const data = await response.json();
```

---

## ⚛️ INTEGRACIÓN EN NEXT.JS

### Configuración Base

**Archivo:** `app/(app)/governance/telemetry/services/telemetryService.ts`

**Patrón de Uso:**
```typescript
class TelemetryService {
  private baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3000';
  private isDemoMode(): boolean {
    return process.env.NEXT_PUBLIC_DEMO_MODE === 'true' || !process.env.NEXT_PUBLIC_API_URL;
  }

  async getKpis(
    startTime?: string,
    endTime?: string
  ): Promise<TelemetryKpisResponse> {
    if (this.isDemoMode()) {
      // Retornar mock data
      return { success: true, data: mockKpisData };
    }

    try {
      const params = new URLSearchParams();
      if (startTime) params.append('startTime', startTime);
      if (endTime) params.append('endTime', endTime);

      const response = await fetch(
        `${this.baseUrl}/api/governance/telemetry/kpis${params.toString() ? `?${params.toString()}` : ''}`
      );
      if (!response.ok) throw new Error('Failed to fetch telemetry KPIs');
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching telemetry KPIs: ${error}`,
      };
    }
  }
}

export const telemetryService = new TelemetryService();
```

### API Routes (Next.js)

**Archivo:** `app/api/governance/telemetry/kpis/route.ts`

**Patrón:**
```typescript
import { NextRequest, NextResponse } from 'next/server';

const TELEMETRY_SERVICE_URL = process.env.TELEMETRY_SERVICE_URL || 'http://localhost:8096';

export async function GET(request: NextRequest) {
  const searchParams = request.nextUrl.searchParams;
  const startTime = searchParams.get('startTime');
  const endTime = searchParams.get('endTime');

  try {
    const params = new URLSearchParams();
    if (startTime) params.append('startTime', startTime);
    if (endTime) params.append('endTime', endTime);

    const response = await fetch(
      `${TELEMETRY_SERVICE_URL}/api/v1/telemetry-analytics/kpis?${params.toString()}`
    );

    if (!response.ok) {
      throw new Error('Failed to fetch KPIs');
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    return NextResponse.json(
      { error: 'Failed to fetch telemetry KPIs' },
      { status: 500 }
    );
  }
}
```

---

## 📝 EJEMPLOS DE USO

### Ejemplo 1: Cargar KPIs con Filtros de Fecha

```typescript
'use client';

import { useState, useEffect } from 'react';
import { telemetryService } from '../services/telemetryService';
import type { TelemetryKpisResponseDto } from '../types/telemetry';

export default function TelemetryDashboard() {
  const [kpis, setKpis] = useState<TelemetryKpisResponseDto | null>(null);
  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');

  useEffect(() => {
    // Establecer fechas por defecto (últimos 30 días)
    const end = new Date();
    const start = new Date();
    start.setDate(start.getDate() - 30);
    setEndDate(end.toISOString().split('T')[0]);
    setStartDate(start.toISOString().split('T')[0]);
  }, []);

  useEffect(() => {
    if (startDate && endDate) {
      loadKpis();
    }
  }, [startDate, endDate]);

  const loadKpis = async () => {
    try {
      const startTime = new Date(startDate).toISOString();
      const endTime = new Date(endDate + 'T23:59:59').toISOString();

      const response = await telemetryService.getKpis(startTime, endTime);
      if (response.success && response.data) {
        setKpis(response.data);
      }
    } catch (error) {
      console.error('Error loading KPIs:', error);
    }
  };

  return (
    <div>
      {/* UI con KPIs */}
    </div>
  );
}
```

### Ejemplo 2: Buscar Eventos por Contenido

```typescript
const searchEvents = async (query: string, startDate?: string, endDate?: string) => {
  try {
    const startTime = startDate ? new Date(startDate).toISOString() : undefined;
    const endTime = endDate ? new Date(endDate + 'T23:59:59').toISOString() : undefined;

    const response = await telemetryService.searchByContent(
      query,
      0, // page
      20, // size
      startTime,
      endTime
    );

    if (response.success && response.data) {
      console.log(`Found ${response.data.total} events`);
      return response.data.events;
    }
  } catch (error) {
    console.error('Error searching events:', error);
    throw error;
  }
};
```

### Ejemplo 3: Obtener Estadísticas por Componente

```typescript
const loadComponentStatistics = async (startDate?: string, endDate?: string) => {
  try {
    const startTime = startDate ? new Date(startDate).toISOString() : undefined;
    const endTime = endDate ? new Date(endDate + 'T23:59:59').toISOString() : undefined;

    const response = await telemetryService.getComponentStatistics(
      undefined, // componentUuid (opcional)
      startTime,
      endTime
    );

    if (response.success && response.data) {
      return response.data.statistics;
    }
  } catch (error) {
    console.error('Error loading component statistics:', error);
    throw error;
  }
};
```

### Ejemplo 4: Parsear JSON de Métricas y Payload

```typescript
const parseEventData = (event: TelemetryEventDto) => {
  // Parsear métricas
  let metrics = null;
  if (event.metrics) {
    try {
      metrics = JSON.parse(event.metrics);
    } catch (e) {
      console.error('Error parsing metrics:', e);
    }
  }

  // Parsear payload
  let payload = null;
  if (event.payload) {
    try {
      payload = JSON.parse(event.payload);
    } catch (e) {
      console.error('Error parsing payload:', e);
    }
  }

  return { metrics, payload };
};

// Uso
const event = await getEventById(123);
const { metrics, payload } = parseEventData(event);
console.log('Latency:', metrics?.latency_ms);
console.log('Payload:', payload);
```

---

## ⚠️ MANEJO DE ERRORES

### Códigos de Estado HTTP

- **200 OK:** Operación exitosa
- **400 Bad Request:** Request inválido (parámetros faltantes o incorrectos)
- **404 Not Found:** Recurso no encontrado
- **500 Internal Server Error:** Error interno del servidor

### Patrón de Manejo

```typescript
const handleApiCall = async <T>(
  apiCall: () => Promise<{ success: boolean; data?: T; error?: string }>
): Promise<T | null> => {
  try {
    const response = await apiCall();

    if (!response.success) {
      console.error('API Error:', response.error);
      // Mostrar notificación al usuario
      return null;
    }

    return response.data || null;
  } catch (error) {
    console.error('Unexpected error:', error);
    // Mostrar notificación al usuario
    return null;
  }
};

// Uso
const kpis = await handleApiCall(() =>
  telemetryService.getKpis(startTime, endTime)
);
```

### Manejo de Errores en Componentes

```typescript
'use client';

import { useState } from 'react';

export default function TelemetryComponent() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loadData = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await telemetryService.getKpis();

      if (!response.success) {
        setError(response.error || 'Error desconocido');
        return;
      }

      // Procesar datos
    } catch (err) {
      setError('Error al cargar datos');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {error && (
        <div className="error-message">
          {error}
        </div>
      )}
      {loading && <div>Cargando...</div>}
      {/* Resto del componente */}
    </div>
  );
}
```

---

## 🎭 MOCK DATA

### Configuración

**Variable de Entorno:**
```env
NEXT_PUBLIC_DEMO_MODE=true
NEXT_PUBLIC_USE_MOCK_DATA=true
```

**Archivo:** `mocks/governance/telemetry/kpis.json`

**Estructura:**
```json
{
  "totalEvents": 125000,
  "totalCostUsd": 1250.50,
  "totalTokens": 5000000,
  "avgLatencyMs": 250.5,
  "componentCount": 15,
  "startTime": "2025-01-01T00:00:00Z",
  "endTime": "2025-01-31T23:59:59Z"
}
```

**Archivo:** `mocks/governance/telemetry/events.json`

**Estructura:**
```json
{
  "events": [
    {
      "id": 1,
      "uuid": "550e8400-e29b-41d4-a716-446655440001",
      "timestamp": "2025-01-15T10:30:00Z",
      "componentUuid": "550e8400-e29b-41d4-a716-446655440000",
      "eventType": "INTERACTION_COMPLETED",
      "severity": "INFO",
      "metrics": "{\"latency_ms\": 250.5, \"tokens_used\": 1000, \"cost_usd\": 0.01}",
      "payload": "{\"input\": \"test\", \"output\": \"result\"}"
    }
  ],
  "total": 1,
  "page": 0,
  "size": 20
}
```

**Uso:**
```typescript
const USE_MOCK = process.env.NEXT_PUBLIC_DEMO_MODE === 'true';

const loadData = async () => {
  if (USE_MOCK) {
    return mockTelemetryData;
  }
  return fetch('/api/governance/telemetry/kpis').then(res => res.json());
};
```

---

## 🎨 COMPONENTES Y PANTALLAS

### Estructura de Archivos

```
app/(app)/governance/telemetry/
├── dashboard/
│   └── page.tsx          # Dashboard principal
├── search/
│   └── page.tsx          # Búsqueda de eventos
├── analytics/
│   └── page.tsx          # Análisis mensual
├── services/
│   └── telemetryService.ts  # Servicio de API
└── types/
    └── telemetry.ts      # Tipos TypeScript
```

### Dashboard Principal

**Archivo:** `app/(app)/governance/telemetry/dashboard/page.tsx`

**Características:**
- KPIs en tarjetas
- Filtros de fecha
- Gráficos con Chart.js
- Tabla de estadísticas por componente
- Botones de navegación

**Ejemplo:**
```typescript
'use client';

import { telemetryService } from '../services/telemetryService';
import { Bar } from 'react-chartjs-2';

export default function TelemetryDashboardPage() {
  const [kpis, setKpis] = useState(null);
  const [componentStats, setComponentStats] = useState([]);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    const kpisResponse = await telemetryService.getKpis();
    const statsResponse = await telemetryService.getComponentStatistics();

    if (kpisResponse.success) setKpis(kpisResponse.data);
    if (statsResponse.success) setComponentStats(statsResponse.data.statistics);
  };

  return (
    <div>
      {/* KPIs Cards */}
      {/* Gráficos */}
      {/* Tabla de componentes */}
    </div>
  );
}
```

### Página de Búsqueda

**Archivo:** `app/(app)/governance/telemetry/search/page.tsx`

**Características:**
- Múltiples filtros de búsqueda (contenido, componente, agente, tipo, severidad)
- Filtros de compliance/security (complianceStatus, riskLevel, complianceCategory, issueTag)
- Búsqueda automática al cargar (muestra todos los eventos si no hay query)
- Paginación de resultados
- Cards de eventos con:
  - Información temporal y tipo
  - Identificadores (componente, agente, trace, run)
  - Verificaciones de gobernanza (badges)
  - Campos de compliance/security (badges con colores)
  - Métricas
  - Botón "Ver Detalle Completo" que navega a página de detalle
- Botón volver

### Página de Detalle de Evento

**Archivo:** `app/(app)/governance/telemetry/events/[id]/page.tsx`

**Características:**
- Visualización completa de todos los datos del evento
- Secciones organizadas:
  - Información principal
  - Verificaciones de gobernanza
  - Compliance/Security (con badges de colores)
  - Métricas (con botones copiar/descargar)
  - Payload completo (con scroll si es extenso, botones copiar/descargar)
  - Resultados de análisis (con botones copiar/descargar)
  - Datos raw del evento (con botones copiar/descargar)
- Funcionalidad de copiar al portapapeles
- Funcionalidad de descargar JSON por sección
- Botón volver a búsqueda

### Página de Análisis

**Archivo:** `app/(app)/governance/telemetry/analytics/page.tsx`

**Características:**
- Selector de año (dropdown con opciones 2024 y 2025, actualización automática)
- KPIs del año (total eventos, costos, tokens, latencia promedio)
- Gráficos de líneas mensuales (eventos, costos, tokens, latencia)
- Datos mock dinámicos con variaciones realistas:
  - Diferencias entre años (2025 tiene ~25% más actividad y mejor latencia)
  - Variaciones estacionales (mayor actividad en verano, menor en invierno)
  - Variación aleatoria (±15%) para mayor realismo
- Botón volver

---

## 🔧 CONFIGURACIÓN DE TIPOS

### Tipos TypeScript

**Archivo:** `app/(app)/governance/telemetry/types/telemetry.ts`

```typescript
export interface TelemetryKpisResponseDto {
  totalEvents: number;
  totalCostUsd: number;
  totalTokens: number;
  avgLatencyMs: number;
  componentCount: number;
  startTime: string;
  endTime: string;
}

export interface TelemetryEventDto {
  id: number;
  uuid: string;
  timestamp: string;
  componentUuid: string;
  agentExternalId?: string;
  eventType: string;
  severity: string;
  traceId?: string;
  runId?: string;
  metrics?: string;
  payload?: string;
  sourceTool?: string;
  biasChecked?: boolean;
  toxicityChecked?: boolean;
  piiDetected?: boolean;
  secretDetected?: boolean;
  complianceStatus?: string; // PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  riskLevel?: string; // LOW, MEDIUM, HIGH, CRITICAL
  complianceCategory?: string; // GDPR, SECURITY, LEGAL
  issueTags?: string; // JSON string array
  analysisResults?: string;
}

export interface TelemetryEventsResponseDto {
  events: TelemetryEventDto[];
  total: number;
  page: number;
  size: number;
}

export interface ComponentStatisticsDto {
  componentUuid: string;
  totalEvents: number;
  avgLatencyMs: number;
  totalTokens: number;
  totalCostUsd: number;
}

export interface ComponentStatisticsListResponseDto {
  statistics: ComponentStatisticsDto[];
  total: number;
}

// Wrappers con success/error
export interface TelemetryKpisResponse {
  success: boolean;
  data?: TelemetryKpisResponseDto;
  error?: string;
}

export interface TelemetryEventsResponse {
  success: boolean;
  data?: TelemetryEventsResponseDto;
  error?: string;
}

export interface ComponentStatisticsResponse {
  success: boolean;
  data?: ComponentStatisticsListResponseDto;
  error?: string;
}
```

---

## 🔗 REFERENCIAS

### Archivos Clave

**Servicios:**
- `codeflowx-studio/app/(app)/governance/telemetry/services/telemetryService.ts`

**Tipos:**
- `codeflowx-studio/app/(app)/governance/telemetry/types/telemetry.ts`

**Pantallas:**
- `codeflowx-studio/app/(app)/governance/telemetry/dashboard/page.tsx`
- `codeflowx-studio/app/(app)/governance/telemetry/search/page.tsx`
- `codeflowx-studio/app/(app)/governance/telemetry/analytics/page.tsx`
- `codeflowx-studio/app/(app)/governance/telemetry/events/[id]/page.tsx` (página de detalle)

**API Routes:**
- `codeflowx-studio/app/api/governance/telemetry/kpis/route.ts`
- `codeflowx-studio/app/api/governance/telemetry/search/content/route.ts`
- `codeflowx-studio/app/api/governance/telemetry/search/project/route.ts`
- `codeflowx-studio/app/api/governance/telemetry/events/component/route.ts`
- `codeflowx-studio/app/api/governance/telemetry/statistics/component/route.ts`

**Mocks:**
- `codeflowx-studio/mocks/governance/telemetry/kpis.json`
- `codeflowx-studio/mocks/governance/telemetry/events.json`
- `codeflowx-studio/mocks/governance/telemetry/statistics.json`

### BFF Backend

- **BFF Controller:** `nocode.service/codeflowx.govern.bff.telemetry/src/main/java/com/codeflowx/govern/bff/telemetry/controller/TelemetryAnalyticsController.java`
- **BFF Service:** `nocode.service/codeflowx.govern.bff.telemetry/src/main/java/com/codeflowx/govern/bff/telemetry/service/impl/TelemetryAnalyticsServiceImpl.java`

---

**Última Actualización:** Diciembre 2025

**Cambios Recientes:**
- Agregados campos de compliance/security en `TelemetryEventDto` (complianceStatus, riskLevel, complianceCategory, issueTags)
- Agregados filtros de compliance/security en endpoint de búsqueda
- Creada página de detalle de eventos (`/governance/telemetry/events/[id]`)
- Mejorada visualización de campos de compliance/security en cards de eventos (badges con colores)
- Actualizado selector de año en análisis mensual (dropdown con actualización automática)
- Mejorados datos mock con variaciones realistas por mes y año
- Cambio de método HTTP de POST a GET para búsqueda (REST best practices)
