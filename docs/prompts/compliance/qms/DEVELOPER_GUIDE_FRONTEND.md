# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - QMS

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Frontend (Next.js/React)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Endpoints API](#endpoints-api)
3. [Integración en Next.js](#integración-en-nextjs)
4. [Ejemplos de Uso](#ejemplos-de-uso)
5. [Manejo de Errores](#manejo-de-errores)
6. [Internacionalización](#internacionalización)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
API Routes (Next.js)
    ↓
Microservicio QMS (Spring WebFlux)
    ↓
Database (PostgreSQL)
```

### Base URL

**API Routes Base:**
```
/api/governance/compliance/qms
```

**Configuración:**
- Los API routes están en `app/api/governance/compliance/qms/`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS API

### 1. Dashboard QMS Principal

#### GET `/api/governance/compliance/qms?projectId={id}`

**Descripción:** Obtiene datos completos QMS para un proyecto.

**Query Parameters:**
- `projectId` (requerido): ID del proyecto

**Response:**
```typescript
interface QmsData {
  projectId: number;
  projectName: string;
  overallScore: number; // 0.00 - 1.00
  complianceStatus: "COMPLIANT" | "PARTIAL" | "NON_COMPLIANT";
  modules: Array<{
    name: string; // "COMPLIANCE_STRATEGY", "RISK_MANAGEMENT", etc.
    displayName: string;
    score: number; // 0.00 - 1.00
    threshold: number; // 0.80
    gaps: Array<{
      description: string;
      severity: "LOW" | "MEDIUM" | "HIGH";
    }>;
  }>;
  gaps: Array<{
    module: string;
    description: string;
    severity: "LOW" | "MEDIUM" | "HIGH";
    currentScore: number;
    targetScore: number;
    gap: number;
    recommendedActions: string[];
  }>;
  improvementPlan: Array<{
    gap: string;
    action: string;
    priority: "LOW" | "MEDIUM" | "HIGH";
  }>;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/governance/compliance/qms?projectId=${projectId}`);
const result = await response.json();

if (result.success) {
  const data: QmsData = result.data;
  // Usar data
}
```

#### POST `/api/governance/compliance/qms`

**Descripción:** Calcula scores QMS para un proyecto.

**Request Body:**
```typescript
{
  projectId: number;
  action: "calculate";
}
```

**Response:**
```typescript
{
  success: boolean;
  data: QmsData;
  message?: string;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch("/api/governance/compliance/qms", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ projectId, action: "calculate" }),
});

const result = await response.json();
if (result.success) {
  setData(result.data);
}
```

### 2. Lista de Proyectos QMS

#### GET `/api/governance/compliance/qms/projects`

**Descripción:** Lista proyectos con información QMS resumida.

**Query Parameters:**
- `page` (default: 0): Número de página
- `size` (default: 10): Tamaño de página
- `status` (opcional): "COMPLIANT" | "PARTIAL" | "NON_COMPLIANT"
- `search` (opcional): Búsqueda por nombre

**Response:**
```typescript
interface QmsProjectsResponse {
  success: boolean;
  data: {
    projects: Array<{
      projectId: number;
      projectName: string;
      overallScore: number;
      complianceStatus: "COMPLIANT" | "PARTIAL" | "NON_COMPLIANT";
      totalModules: number;
      compliantModules: number;
      gapsCount: number;
      lastUpdated: string; // ISO 8601
      createdAt: string; // ISO 8601
    }>;
    statistics: {
      totalProjects: number;
      totalCompliant: number;
      totalPartial: number;
      totalNonCompliant: number;
      averageScore: number;
    };
    pagination?: {
      page: number;
      size: number;
      totalPages: number;
      totalElements: number;
    };
  };
}
```

**Ejemplo de Uso:**
```typescript
const params = new URLSearchParams();
params.append("page", String(currentPage - 1));
params.append("size", String(ITEMS_PER_PAGE));
if (filterStatus !== "all") {
  params.append("status", filterStatus);
}
if (searchQuery) {
  params.append("search", searchQuery);
}

const response = await fetch(`/api/governance/compliance/qms/projects?${params.toString()}`);
const result = await response.json();

if (result.success) {
  setData(result.data);
}
```

### 3. Revisión de Conformidad

#### GET `/api/governance/compliance/qms/review?projectId={id}`

**Descripción:** Obtiene datos de revisión para un proyecto.

**Query Parameters:**
- `projectId` (requerido): ID del proyecto

**Response:**
```typescript
interface QmsReviewData {
  projectId: number;
  projectName: string;
  overallScore: number;
  gaps: Array<{
    module: string;
    description: string;
    severity: "LOW" | "MEDIUM" | "HIGH";
    currentScore: number;
    targetScore: number;
    gap: number;
    recommendedActions: string[];
    isAcceptable?: boolean;
  }>;
  reviewNotes: string;
  reviewerName: string;
}
```

#### POST `/api/governance/compliance/qms/review`

**Descripción:** Procesa revisión de gaps QMS.

**Request Body:**
```typescript
{
  projectId: number;
  decision: "APPROVED" | "CORRECTIONS_REQUIRED";
  reviewerName: string;
  reviewNotes: string;
  taskId?: string; // Opcional, para BPMN
}
```

**Response:**
```typescript
{
  success: boolean;
  message: string;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch("/api/governance/compliance/qms/review", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    projectId,
    decision: "APPROVED",
    reviewerName: currentUser.name,
    reviewNotes: reviewNotes,
    taskId: taskId, // Si está en workflow BPMN
  }),
});

const result = await response.json();
if (result.success) {
  // Mostrar mensaje de éxito
  toast.success(result.message);
}
```

---

## 📱 INTEGRACIÓN EN NEXT.JS

### Estructura de Archivos

```
app/
├── (app)/
│   └── governance/
│       └── compliance/
│           ├── qms/
│           │   ├── page.tsx              # Dashboard QMS principal
│           │   └── projects/
│           │       └── page.tsx           # Lista de proyectos
│           └── conformity-review/
│               └── page.tsx               # Revisión de conformidad
└── api/
    └── governance/
        └── compliance/
            └── qms/
                ├── route.ts              # API route principal
                └── projects/
                    └── route.ts          # API route proyectos
```

### API Routes

#### `app/api/governance/compliance/qms/route.ts`

**GET Handler:**
```typescript
import { NextResponse } from "next/server";

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const projectId = searchParams.get("projectId");

  if (!projectId) {
    return NextResponse.json(
      { success: false, error: "projectId is required" },
      { status: 400 }
    );
  }

  const backendUrl = `${process.env.NEXT_PUBLIC_BFF_API_URL}/qms?projectId=${projectId}`;

  try {
    const response = await fetch(backendUrl, {
      headers: {
        "Content-Type": "application/json",
        // Add authentication headers if needed
      },
    });

    if (!response.ok) {
      const errorData = await response.json();
      return NextResponse.json(
        { success: false, error: errorData.message || "Failed to fetch QMS data" },
        { status: response.status }
      );
    }

    const data = await response.json();
    return NextResponse.json({ success: true, data });
  } catch (error) {
    console.error("Error fetching QMS data:", error);
    return NextResponse.json(
      { success: false, error: "Internal server error" },
      { status: 500 }
    );
  }
}
```

**POST Handler:**
```typescript
export async function POST(request: Request) {
  const body = await request.json();
  const { projectId, action } = body;

  if (!projectId || action !== "calculate") {
    return NextResponse.json(
      { success: false, error: "Invalid request" },
      { status: 400 }
    );
  }

  const backendUrl = `${process.env.NEXT_PUBLIC_BFF_API_URL}/qms/calculate`;

  try {
    const response = await fetch(backendUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ projectId }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      return NextResponse.json(
        { success: false, error: errorData.message || "Failed to calculate QMS" },
        { status: response.status }
      );
    }

    const data = await response.json();
    return NextResponse.json({ success: true, data });
  } catch (error) {
    console.error("Error calculating QMS:", error);
    return NextResponse.json(
      { success: false, error: "Internal server error" },
      { status: 500 }
    );
  }
}
```

---

## 💡 EJEMPLOS DE USO

### Ejemplo 1: Cargar Datos QMS en Dashboard

```typescript
"use client";

import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";

export default function QmsPage() {
  const searchParams = useSearchParams();
  const [data, setData] = useState<QmsData | null>(null);
  const [loading, setLoading] = useState(true);

  const getProjectId = (): number => {
    const projectIdParam = searchParams.get("projectId");
    if (projectIdParam) {
      const parsed = parseInt(projectIdParam, 10);
      if (!isNaN(parsed)) {
        return parsed;
      }
    }
    return 1; // Default
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);

  const loadData = async () => {
    try {
      setLoading(true);
      const projectId = getProjectId();

      const response = await fetch(`/api/governance/compliance/qms?projectId=${projectId}`);
      if (!response.ok) {
        throw new Error("Failed to load QMS data");
      }

      const result = await response.json();
      if (result.success) {
        setData(result.data);
      } else {
        throw new Error(result.error || "Failed to load QMS data");
      }
    } catch (error) {
      console.error("Error loading QMS data:", error);
      // Handle error (show toast, fallback data, etc.)
    } finally {
      setLoading(false);
    }
  };

  if (loading || !data) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>QMS Dashboard</h1>
      <p>Score Overall: {(data.overallScore * 100).toFixed(0)}%</p>
      {/* Render rest of UI */}
    </div>
  );
}
```

### Ejemplo 2: Calcular Scores QMS

```typescript
const handleCalculateScores = async () => {
  try {
    setLoading(true);
    const projectId = getProjectId();

    const response = await fetch("/api/governance/compliance/qms", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ projectId, action: "calculate" }),
    });

    if (!response.ok) {
      throw new Error("Failed to calculate QMS scores");
    }

    const result = await response.json();
    if (result.success) {
      setData(result.data);
      toast.success("Scores calculated successfully");
    } else {
      throw new Error(result.error || "Failed to calculate QMS scores");
    }
  } catch (error) {
    console.error("Error calculating QMS scores:", error);
    toast.error("Error calculating QMS scores");
  } finally {
    setLoading(false);
  }
};
```

### Ejemplo 3: Marcar Gap como Aceptable

```typescript
const handleMarkGapAcceptable = (gapIndex: number) => {
  setData((prevData) => {
    if (!prevData) return prevData;

    const updatedGaps = [...prevData.gaps];
    const currentGap = updatedGaps[gapIndex];
    updatedGaps[gapIndex] = {
      ...currentGap,
      isAcceptable: !currentGap.isAcceptable,
    };

    return { ...prevData, gaps: updatedGaps };
  });
};
```

### Ejemplo 4: Procesar Revisión de Conformidad

```typescript
const handleProcessReview = async (decision: "APPROVED" | "CORRECTIONS_REQUIRED") => {
  if (!data?.reviewNotes.trim()) {
    toast.error("Review notes are required");
    return;
  }

  try {
    setLoading(true);

    const response = await fetch("/api/governance/compliance/qms/review", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        projectId: getProjectId(),
        decision,
        reviewerName: getCurrentUserName(),
        reviewNotes: data.reviewNotes,
        taskId: taskId, // Si está en workflow BPMN
      }),
    });

    if (!response.ok) {
      throw new Error("Failed to process review");
    }

    const result = await response.json();
    if (result.success) {
      toast.success(result.message);
      // Navigate or refresh
    } else {
      throw new Error(result.error || "Failed to process review");
    }
  } catch (error) {
    console.error("Error processing review:", error);
    toast.error("Error processing review");
  } finally {
    setLoading(false);
  }
};
```

---

## ⚠️ MANEJO DE ERRORES

### Patrón Recomendado

```typescript
try {
  const response = await fetch(url);

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || "Request failed");
  }

  const result = await response.json();
  if (!result.success) {
    throw new Error(result.error || "Operation failed");
  }

  return result.data;
} catch (error) {
  console.error("Error:", error);
  toast.error(error instanceof Error ? error.message : "An error occurred");
  throw error;
}
```

### Tipos de Errores

1. **Errores de Red:**
   - Timeout
   - Connection refused
   - Network error

2. **Errores HTTP:**
   - 400: Bad Request (projectId faltante, datos inválidos)
   - 404: Not Found (proyecto no existe)
   - 500: Internal Server Error

3. **Errores de Negocio:**
   - Proyecto sin QMS
   - Datos inválidos
   - Operación no permitida

---

## 🌍 INTERNACIONALIZACIÓN

### Uso de Traducciones

```typescript
import { useTranslation } from "@/app/config/i18n";

export default function QmsPage() {
  const { t, language } = useTranslation();

  return (
    <div>
      <h1>{t("governance.qms.title", "Quality Management System (QMS)")}</h1>
      <p>{t("governance.qms.subtitle", "Art. 17 EU AI Act - Compliance monitoring by modules")}</p>

      {/* Traducir nombres de módulos */}
      {data.modules.map((module) => (
        <div key={module.name}>
          {t(`governance.qms.moduleNames.${module.name}`, module.displayName)}
        </div>
      ))}
    </div>
  );
}
```

### Rutas de Traducción

- `governance.qms.*` - Traducciones QMS generales
- `governance.compliance.qms.projects.*` - Traducciones proyectos
- `governance.compliance.qms.review.*` - Traducciones revisión
- `governance.qms.moduleNames.*` - Nombres de módulos
- `governance.qms.status.*` - Estados de compliance
- `governance.qms.severity.*` - Severidades de gaps

### Formato de Fechas

```typescript
const formatDate = (date: string) => {
  const localeMap: Record<string, string> = {
    es: "es-ES",
    en: "en-US",
    fr: "fr-FR",
    de: "de-DE",
    it: "it-IT",
    pt: "pt-PT",
  };
  const locale = localeMap[language] || "es-ES";
  return new Date(date).toLocaleDateString(locale, {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
};
```

---

## 🔧 EVOLUCIONES Y CORRECCIONES

### Cómo Agregar una Nueva Pantalla QMS

#### 1. Crear Página Next.js

**Ubicación:** `app/(app)/governance/compliance/qms/[nueva-pantalla]/page.tsx`

**Ejemplo:**
```typescript
"use client";

import { useState, useEffect } from "react";
import { useSearchParams } from "next/navigation";
import { useTranslation } from "@/app/config/i18n";

export default function NuevaPantallaPage() {
  const searchParams = useSearchParams();
  const { t } = useTranslation();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const getProjectId = (): number => {
    const projectIdParam = searchParams.get("projectId");
    if (projectIdParam) {
      const parsed = parseInt(projectIdParam, 10);
      if (!isNaN(parsed)) {
        return parsed;
      }
    }
    return 1; // Default
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);

  const loadData = async () => {
    try {
      setLoading(true);
      const projectId = getProjectId();
      const response = await fetch(`/api/governance/compliance/qms/nueva-pantalla?projectId=${projectId}`);

      if (!response.ok) {
        throw new Error("Failed to load data");
      }

      const result = await response.json();
      if (result.success) {
        setData(result.data);
      }
    } catch (error) {
      console.error("Error loading data:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>{t("governance.qms.nuevaPantalla.title", "Nueva Pantalla")}</h1>
      {/* Render UI */}
    </div>
  );
}
```

#### 2. Crear API Route

**Ubicación:** `app/api/governance/compliance/qms/nueva-pantalla/route.ts`

**Ejemplo:**
```typescript
import { NextResponse } from "next/server";

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const projectId = searchParams.get("projectId");

  if (!projectId) {
    return NextResponse.json(
      { success: false, error: "projectId is required" },
      { status: 400 }
    );
  }

  const backendUrl = `${process.env.NEXT_PUBLIC_BFF_API_URL}/qms/nueva-pantalla?projectId=${projectId}`;

  try {
    const response = await fetch(backendUrl, {
      headers: { "Content-Type": "application/json" },
    });

    if (!response.ok) {
      return NextResponse.json(
        { success: false, error: "Failed to fetch data" },
        { status: response.status }
      );
    }

    const data = await response.json();
    return NextResponse.json({ success: true, data });
  } catch (error) {
    console.error("Error:", error);
    return NextResponse.json(
      { success: false, error: "Internal server error" },
      { status: 500 }
    );
  }
}
```

#### 3. Agregar Traducciones

**Ubicación:** `app/config/i18n/modules/governance/compliance.ts`

```typescript
qms: {
  nuevaPantalla: {
    title: {
      es: "Nueva Pantalla",
      en: "New Screen",
      fr: "Nouvel Écran",
      // ...
    }
  }
}
```

### Cómo Corregir un Bug Común

#### Bug: Traducciones no se muestran correctamente

**Causa:** Ruta de traducción incorrecta o traducción faltante.

**Solución:**
1. Verificar ruta en el archivo de traducciones:
```typescript
// ❌ Incorrecto
t("governance.qms.title")

// ✅ Correcto
t("governance.compliance.qms.title")
```

2. Verificar que existe en todos los idiomas:
```typescript
// app/config/i18n/modules/governance/compliance.ts
qms: {
  title: {
    es: "Sistema de Gestión de Calidad",
    en: "Quality Management System",
    fr: "Système de Gestion de la Qualité",
    de: "Qualitätsmanagementsystem",
    it: "Sistema di Gestione della Qualità",
    pt: "Sistema de Gestão da Qualidade"
  }
}
```

#### Bug: projectId no se obtiene de la URL

**Causa:** `useSearchParams()` no se usa correctamente o falta en el componente.

**Solución:**
```typescript
"use client"; // ⚠️ IMPORTANTE: Debe estar en la primera línea

import { useSearchParams } from "next/navigation";

export default function QmsPage() {
  const searchParams = useSearchParams(); // ✅ Correcto

  const getProjectId = (): number => {
    const projectIdParam = searchParams.get("projectId");
    if (projectIdParam) {
      const parsed = parseInt(projectIdParam, 10);
      if (!isNaN(parsed)) {
        return parsed;
      }
    }
    return 1; // Fallback
  };

  useEffect(() => {
    loadData();
  }, [searchParams]); // ✅ Incluir searchParams en dependencias
}
```

#### Bug: Datos no se actualizan después de POST

**Causa:** No se recarga la data después de una operación POST.

**Solución:**
```typescript
const handleSubmit = async () => {
  try {
    const response = await fetch("/api/governance/compliance/qms/action", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ projectId, data }),
    });

    if (response.ok) {
      const result = await response.json();
      if (result.success) {
        // ✅ Recargar datos después de éxito
        await loadData();
        toast.success("Operation successful");
      }
    }
  } catch (error) {
    console.error("Error:", error);
    toast.error("Operation failed");
  }
};
```

### Troubleshooting

#### Problema: Error "Cannot read property 'get' of null" con useSearchParams

**Causa:** `useSearchParams()` se usa en un componente Server Component.

**Solución:**
1. Asegurar que el componente es Client Component (`"use client"`)
2. O usar `useRouter()` y `router.query`:
```typescript
import { useRouter } from "next/navigation";

const router = useRouter();
const projectId = router.query?.projectId;
```

#### Problema: API route retorna 404

**Diagnóstico:**
1. Verificar que el archivo está en la ruta correcta: `app/api/governance/compliance/qms/[ruta]/route.ts`
2. Verificar que el método HTTP está implementado (GET, POST, etc.)
3. Verificar que el servidor Next.js está corriendo

**Solución:**
```typescript
// app/api/governance/compliance/qms/test/route.ts
export async function GET() {
  return NextResponse.json({ success: true, message: "Test endpoint works" });
}
```

#### Problema: Datos no se cargan en el primer render

**Causa:** `useEffect` no se ejecuta o hay race condition.

**Solución:**
```typescript
useEffect(() => {
  // ✅ Asegurar que se ejecuta después del mount
  if (searchParams) {
    loadData();
  }
}, [searchParams]);

// O usar estado de montaje:
const [mounted, setMounted] = useState(false);

useEffect(() => {
  setMounted(true);
}, []);

useEffect(() => {
  if (mounted) {
    loadData();
  }
}, [mounted, searchParams]);
```

### Testing

#### Unit Tests para Componentes

**Ubicación:** `app/(app)/governance/compliance/qms/__tests__/QmsPage.test.tsx`

**Ejemplo:**
```typescript
import { render, screen, waitFor } from "@testing-library/react";
import { useSearchParams } from "next/navigation";
import QmsPage from "../page";

jest.mock("next/navigation");

describe("QmsPage", () => {
  beforeEach(() => {
    (useSearchParams as jest.Mock).mockReturnValue({
      get: (key: string) => (key === "projectId" ? "1" : null),
    });
  });

  it("renders QMS dashboard", async () => {
    render(<QmsPage />);

    await waitFor(() => {
      expect(screen.getByText(/Quality Management System/i)).toBeInTheDocument();
    });
  });

  it("loads data for project", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({
        success: true,
        data: { projectId: 1, overallScore: 0.85 },
      }),
    });

    render(<QmsPage />);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining("projectId=1")
      );
    });
  });
});
```

#### Integration Tests para API Routes

**Ubicación:** `app/api/governance/compliance/qms/__tests__/route.test.ts`

**Ejemplo:**
```typescript
import { GET } from "../route";
import { NextRequest } from "next/server";

describe("GET /api/governance/compliance/qms", () => {
  it("returns QMS data for project", async () => {
    const request = new NextRequest(
      "http://localhost:3000/api/governance/compliance/qms?projectId=1"
    );

    const response = await GET(request);
    const data = await response.json();

    expect(response.status).toBe(200);
    expect(data.success).toBe(true);
    expect(data.data).toHaveProperty("projectId");
  });

  it("returns error if projectId is missing", async () => {
    const request = new NextRequest(
      "http://localhost:3000/api/governance/compliance/qms"
    );

    const response = await GET(request);
    const data = await response.json();

    expect(response.status).toBe(400);
    expect(data.success).toBe(false);
    expect(data.error).toContain("projectId");
  });
});
```

### Mejores Prácticas

#### 1. Manejo de Estados de Carga

```typescript
const [loading, setLoading] = useState(true);
const [error, setError] = useState<string | null>(null);
const [data, setData] = useState<QmsData | null>(null);

const loadData = async () => {
  try {
    setLoading(true);
    setError(null);

    const response = await fetch(url);
    if (!response.ok) {
      throw new Error("Failed to load data");
    }

    const result = await response.json();
    setData(result.data);
  } catch (err) {
    setError(err instanceof Error ? err.message : "Unknown error");
  } finally {
    setLoading(false);
  }
};

// En el render:
if (loading) return <LoadingSpinner />;
if (error) return <ErrorMessage error={error} />;
if (!data) return <EmptyState />;
```

#### 2. Optimización de Re-renders

```typescript
// ✅ Usar useMemo para cálculos costosos
const filteredGaps = useMemo(() => {
  return data.gaps.filter(gap => gap.severity === "HIGH");
}, [data.gaps]);

// ✅ Usar useCallback para funciones pasadas como props
const handleGapClick = useCallback((gapId: string) => {
  // Handle click
}, []);
```

#### 3. Internacionalización Consistente

```typescript
// ✅ Crear helper para traducciones de módulos
const getModuleName = (moduleKey: string): string => {
  return t(`governance.qms.moduleNames.${moduleKey}`, moduleKey);
};

// ✅ Usar helper para formatear fechas
const formatDate = (date: string): string => {
  const localeMap: Record<string, string> = {
    es: "es-ES",
    en: "en-US",
    // ...
  };
  const locale = localeMap[language] || "es-ES";
  return new Date(date).toLocaleDateString(locale);
};
```

### Configuración del Entorno

#### Variables de Entorno

**Archivo:** `.env.local`

```env
# Backend API URL
NEXT_PUBLIC_BFF_API_URL=http://localhost:8080/api/v1

# Feature Flags
NEXT_PUBLIC_ENABLE_QMS_MODULES=true
NEXT_PUBLIC_ENABLE_QMS_BPMN=true

# Analytics (opcional)
NEXT_PUBLIC_ANALYTICS_ID=your-analytics-id
```

#### Configuración de TypeScript

**Archivo:** `tsconfig.json`

```json
{
  "compilerOptions": {
    "paths": {
      "@/*": ["./app/*"],
      "@/components/*": ["./app/components/*"],
      "@/config/*": ["./app/config/*"]
    }
  }
}
```

---

## 📚 REFERENCIAS

- **Guía Backend:** `docs/prompts/compliance/qms/DEVELOPER_GUIDE_BACKEND.md`
- **Estado de Implementación:** `docs/prompts/compliance/qms/ESTADO_IMPLEMENTACION_QMS.md`
- **Endpoints REST:** `docs/prompts/compliance/QMS_ENDPOINTS_F_M.md`
- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`

---

**Última actualización:** Diciembre 2025
**Versión:** 1.0.0
