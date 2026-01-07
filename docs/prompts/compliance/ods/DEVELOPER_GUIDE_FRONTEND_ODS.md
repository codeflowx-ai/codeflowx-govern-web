# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - ODS IMPACT

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Frontend (Next.js/React)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Endpoints BFF](#endpoints-bff)
3. [Integración en Next.js](#integración-en-nextjs)
4. [Componentes Principales](#componentes-principales)
5. [Ejemplos de Uso](#ejemplos-de-uso)
6. [Manejo de Errores](#manejo-de-errores)
7. [Mock Data](#mock-data)
8. [Internacionalización](#internacionalización)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (ODS Impact Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**BFF Endpoint Base:**
```
/api/v1/compliance/ods-impact
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.governance` o `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS BFF

### 1. Dashboard Principal ODS Impact

#### GET `/api/v1/compliance/ods-impact`

**Descripción:** Obtiene el dashboard principal con métricas generales y datos de todos los ODS.

**Response:**
```typescript
interface ODSImpactDashboard {
  overallScore: number; // 0-100
  complianceRate: number; // Porcentaje de KPIs que cumplen metas
  odsList: ODSImpact[];
  topCriticalKPIs: ODSKPI[];
  temporalEvolution: TemporalDataPoint[]; // Últimos 12 meses
}

interface ODSImpact {
  odsNumber: number;
  odsName: string;
  score: number; // 0-100
  trend: "up" | "down" | "stable";
  modulesCount: number;
  status: "good" | "warning" | "critical";
  kpis: ODSKPI[];
}

interface ODSKPI {
  code: string; // Ej: "KPI 16.1"
  name: string;
  value: number;
  unit: string; // "%", "", "horas", etc.
  meta: number; // Valor objetivo
  criticalThreshold: number; // Umbral crítico
  status: "good" | "warning" | "critical";
  trend: "up" | "down" | "stable";
  trendValue?: number; // Porcentaje de cambio
  description: string;
  frequency: "daily" | "weekly" | "monthly" | "quarterly";
  sources: string[]; // Tablas de BD utilizadas
}

interface TemporalDataPoint {
  date: string; // ISO 8601
  value: number;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/v1/compliance/ods-impact');
const dashboard: ODSImpactDashboard = await response.json();
```

---

### 2. Datos de un ODS Específico

#### GET `/api/v1/compliance/ods-impact/ods-{odsNumber}`

**Descripción:** Obtiene los datos completos de un ODS específico, incluyendo todos sus KPIs.

**Path Parameters:**
- `odsNumber`: Número del ODS (3, 4, 5, 7, 8, 9, 10, 12, 16, 17)

**Response:**
```typescript
ODSImpact // Mismo formato que en dashboard
```

**Ejemplo de Uso:**
```typescript
// Obtener datos del ODS 16
const response = await fetch('/api/v1/compliance/ods-impact/ods-16');
const odsData: ODSImpact = await response.json();
```

---

### 3. KPI Específico

#### GET `/api/v1/compliance/ods-impact/ods-{odsNumber}/kpis/{kpiCode}`

**Descripción:** Obtiene los datos de un KPI específico.

**Path Parameters:**
- `odsNumber`: Número del ODS
- `kpiCode`: Código del KPI (ej: "KPI 16.1", debe ser URL-encoded)

**Response:**
```typescript
ODSKPI
```

**Ejemplo de Uso:**
```typescript
// Obtener KPI 16.1 del ODS 16
const kpiCode = encodeURIComponent("KPI 16.1");
const response = await fetch(
  `/api/v1/compliance/ods-impact/ods-16/kpis/${kpiCode}`
);
const kpi: ODSKPI = await response.json();
```

---

## 🔧 INTEGRACIÓN EN NEXT.JS

### Servicio de Datos

**Ubicación:** `app/(app)/governance/ods-impact/services/odsImpactService.ts`

**Implementación:**
```typescript
/**
 * Servicio para integración con backend de KPIs ODS Impact
 */
import {
  ODSImpactDashboard,
  ODSImpact,
  ODSKPI,
} from "../data/mockODSKPIs";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:3000";
const IS_DEMO_MODE =
  process.env.NEXT_PUBLIC_DEMO_MODE === "true" ||
  !process.env.NEXT_PUBLIC_API_URL;

export interface ODSImpactServiceResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
}

class ODSImpactService {
  private baseUrl = API_BASE_URL;

  private isDemoMode(): boolean {
    return IS_DEMO_MODE;
  }

  /**
   * Obtiene el dashboard principal de impacto ODS
   */
  async getDashboard(): Promise<
    ODSImpactServiceResponse<ODSImpactDashboard>
  > {
    if (this.isDemoMode()) {
      return {
        success: true,
        data: mockODSImpactDashboard,
      };
    }

    try {
      const response = await fetch(
        `${this.baseUrl}/api/v1/compliance/ods-impact`
      );
      if (!response.ok)
        throw new Error("Failed to fetch ODS impact dashboard");
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching ODS impact dashboard: ${error}`,
      };
    }
  }

  /**
   * Obtiene los datos de un ODS específico
   */
  async getODSData(
    odsNumber: number
  ): Promise<ODSImpactServiceResponse<ODSImpact>> {
    if (this.isDemoMode()) {
      const data = getODSData(odsNumber);
      if (!data) {
        return {
          success: false,
          error: `ODS ${odsNumber} data not found`,
        };
      }
      return {
        success: true,
        data,
      };
    }

    try {
      const response = await fetch(
        `${this.baseUrl}/api/v1/compliance/ods-impact/ods-${odsNumber}`
      );
      if (!response.ok)
        throw new Error(`Failed to fetch ODS ${odsNumber} data`);
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching ODS ${odsNumber} data: ${error}`,
      };
    }
  }

  /**
   * Obtiene un KPI específico de un ODS
   */
  async getKPI(
    odsNumber: number,
    kpiCode: string
  ): Promise<ODSImpactServiceResponse<ODSKPI>> {
    if (this.isDemoMode()) {
      const odsData = getODSData(odsNumber);
      if (!odsData) {
        return {
          success: false,
          error: `ODS ${odsNumber} data not found`,
        };
      }
      const kpi = odsData.kpis.find((k) => k.code === kpiCode);
      if (!kpi) {
        return {
          success: false,
          error: `KPI ${kpiCode} not found in ODS ${odsNumber}`,
        };
      }
      return {
        success: true,
        data: kpi,
      };
    }

    try {
      const kpiCodeEncoded = encodeURIComponent(kpiCode);
      const response = await fetch(
        `${this.baseUrl}/api/v1/compliance/ods-impact/ods-${odsNumber}/kpis/${kpiCodeEncoded}`
      );
      if (!response.ok)
        throw new Error(`Failed to fetch KPI ${kpiCode} for ODS ${odsNumber}`);
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching KPI ${kpiCode} for ODS ${odsNumber}: ${error}`,
      };
    }
  }
}

export const odsImpactService = new ODSImpactService();
```

---

## 🧩 COMPONENTES PRINCIPALES

### 1. Dashboard Principal

**Ubicación:** `app/(app)/governance/ods-impact/page.tsx`

**Características:**
- Muestra métricas principales (Score General, Tasa de Cumplimiento)
- Grid de cards de ODS
- Top 5 KPIs Críticos
- Gráfico de evolución temporal

**Ejemplo de Uso:**
```typescript
"use client";

import { useEffect, useState } from "react";
import { odsImpactService } from "./services/odsImpactService";
import { ODSImpactDashboard } from "./data/mockODSKPIs";
import { ODSCard } from "./components/ODSCard";
import { KPICard } from "./components/KPICard";

export default function ODSImpactPage() {
  const [dashboard, setDashboard] = useState<ODSImpactDashboard | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDashboard = async () => {
      const response = await odsImpactService.getDashboard();
      if (response.success && response.data) {
        setDashboard(response.data);
      } else {
        setError(response.error || "Error loading dashboard");
      }
      setLoading(false);
    };

    fetchDashboard();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!dashboard) return null;

  return (
    <div className="w-full p-6">
      <h1>Impacto ODS</h1>

      {/* Métricas principales */}
      <div className="grid grid-cols-3 gap-4 mb-6">
        <Card>
          <CardTitle>Score General</CardTitle>
          <CardContent>{dashboard.overallScore.toFixed(1)}</CardContent>
        </Card>
        <Card>
          <CardTitle>Tasa de Cumplimiento</CardTitle>
          <CardContent>{dashboard.complianceRate.toFixed(1)}%</CardContent>
        </Card>
      </div>

      {/* Cards de ODS */}
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4 mb-6">
        {dashboard.odsList.map((ods) => (
          <ODSCard
            key={ods.odsNumber}
            odsNumber={ods.odsNumber}
            odsName={ods.odsName}
            score={ods.score}
            trend={ods.trend}
            modulesCount={ods.modulesCount}
            status={ods.status}
            href={`/governance/ods-impact/ods-${ods.odsNumber}`}
          />
        ))}
      </div>

      {/* Top 5 KPIs Críticos */}
      <div className="mb-6">
        <h2>Top 5 KPIs Críticos</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {dashboard.topCriticalKPIs.map((kpi) => (
            <KPICard key={kpi.code} kpi={kpi} />
          ))}
        </div>
      </div>
    </div>
  );
}
```

---

### 2. Página de Detalle de ODS

**Ubicación:** `app/(app)/governance/ods-impact/ods-{number}/page.tsx`

**Características:**
- Muestra información completa del ODS
- Lista todos los KPIs del ODS
- Gráficos de evolución y distribución

**Ejemplo de Uso:**
```typescript
"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { odsImpactService } from "../services/odsImpactService";
import { ODSImpact } from "../data/mockODSKPIs";
import { KPICard } from "../components/KPICard";
import { Line, Bar } from "react-chartjs-2";

export default function ODSDetailPage() {
  const params = useParams();
  const odsNumber = parseInt(params.odsNumber as string);
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchODSData = async () => {
      const response = await odsImpactService.getODSData(odsNumber);
      if (response.success && response.data) {
        setOdsData(response.data);
      }
      setLoading(false);
    };

    fetchODSData();
  }, [odsNumber]);

  if (loading) return <div>Loading...</div>;
  if (!odsData) return <div>ODS not found</div>;

  return (
    <div className="w-full p-6">
      <button onClick={() => window.location.href = "/governance/ods-impact"}>
        Volver
      </button>

      <h1>ODS {odsData.odsNumber} - {odsData.odsName}</h1>
      <p>Score: {odsData.score.toFixed(1)}</p>
      <p>Estado: {odsData.status}</p>

      {/* KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
        {odsData.kpis.map((kpi) => (
          <KPICard key={kpi.code} kpi={kpi} />
        ))}
      </div>

      {/* Gráficos */}
      {/* ... implementar gráficos con Chart.js */}
    </div>
  );
}
```

---

### 3. Componente ODSCard

**Ubicación:** `app/(app)/governance/ods-impact/components/ODSCard.tsx`

**Características:**
- Muestra información resumida de un ODS
- Navegación a página de detalle
- Estados visuales (bueno, atención, crítico)

---

### 4. Componente KPICard

**Ubicación:** `app/(app)/governance/ods-impact/components/KPICard.tsx`

**Características:**
- Muestra información de un KPI
- Estados visuales (cumplido, atención, crítico)
- Indicadores de tendencia

---

## 🎨 INTERNACIONALIZACIÓN

### Archivo de Traducciones

**Ubicación:** `app/config/i18n/modules/compliance/ods-impact.ts`

**Estructura:**
```typescript
export const odsImpactTranslations: TranslationModule = {
  es: {
    compliance: {
      odsImpact: {
        title: "Impacto ODS",
        subtitle: "Medición del impacto de la plataforma en los Objetivos de Desarrollo Sostenible",
        // ... más traducciones
      },
    },
  },
  en: { /* ... */ },
  fr: { /* ... */ },
  de: { /* ... */ },
  it: { /* ... */ },
  pt: { /* ... */ },
};
```

**Uso en Componentes:**
```typescript
import { useTranslation } from "@/app/config/i18n";

export function ODSCard({ odsNumber, odsName, ... }) {
  const { t } = useTranslation();

  return (
    <div>
      <h3>ODS {odsNumber}</h3>
      <p>{t(`compliance.odsImpact.odsNames.ods${odsNumber}`, odsName)}</p>
    </div>
  );
}
```

---

## 🚨 MANEJO DE ERRORES

### Errores Comunes

1. **Error de Red:**
```typescript
try {
  const response = await odsImpactService.getDashboard();
  if (!response.success) {
    console.error("Error:", response.error);
    // Mostrar mensaje al usuario
  }
} catch (error) {
  console.error("Network error:", error);
  // Mostrar mensaje de error de red
}
```

2. **ODS No Encontrado:**
```typescript
const response = await odsImpactService.getODSData(odsNumber);
if (!response.success) {
  if (response.error?.includes("not found")) {
    // Mostrar mensaje: "ODS no encontrado"
  }
}
```

3. **KPI No Encontrado:**
```typescript
const response = await odsImpactService.getKPI(odsNumber, kpiCode);
if (!response.success) {
  if (response.error?.includes("not found")) {
    // Mostrar mensaje: "KPI no encontrado"
  }
}
```

---

## 🧪 MOCK DATA

### Ubicación
`app/(app)/governance/ods-impact/data/mockODSKPIs.ts`

### Características
- Datos mock para desarrollo y demo
- Estructura idéntica a datos reales
- Se usa cuando `NEXT_PUBLIC_DEMO_MODE=true` o `NEXT_PUBLIC_API_URL` no está definido

### Uso
El servicio `odsImpactService` detecta automáticamente si debe usar mock data o datos reales basándose en variables de entorno.

---

## 📚 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **KPIs y Dashboards:** `docs/prompts/compliance/PROMPT_KPIS_DASHBOARDS_ODS.md`
- **Queries SQL:** `docs/prompts/compliance/ODS_IMPACT_SQL_QUERIES.md`
- **Análisis de Factibilidad:** `docs/prompts/compliance/ODS_IMPACT_FEASIBILITY_ANALYSIS_UPDATED.md`

---

**Última Actualización:** Diciembre 2025
