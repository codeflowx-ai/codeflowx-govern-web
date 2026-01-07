# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - PROHIBITED SYSTEMS

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
6. [Componentes UI](#componentes-ui)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Prohibited Systems Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**BFF Endpoint Base:**
```
/api/compliance/prohibited-systems
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS BFF

### 1. Verificar Sistema Prohibido

#### GET `/api/compliance/prohibited-systems/check?projectId={id}`

**Descripción:** Verifica si un proyecto usa sistemas prohibidos.

**Query Parameters:**
- `projectId` (requerido): ID del proyecto a verificar

**Response:**
```typescript
interface ProhibitedSystemCheckResultDto {
  detected: boolean;
  confidence: number;
  matchedKeywords: string[];
  systems: Array<{
    id: number;
    name: string;
    category: string;
    detectedAt: string;
    confidence: number;
  }>;
  workflowInstanceId?: string;
}
```

**Ejemplo de Uso:**
```typescript
const checkProhibitedSystems = async (projectId: number) => {
  try {
    const response = await fetch(
      `/api/compliance/prohibited-systems/check?projectId=${projectId}`
    );

    if (!response.ok) {
      throw new Error("Failed to check prohibited systems");
    }

    const data: ProhibitedSystemCheckResultDto = await response.json();
    return data;
  } catch (error) {
    console.error("Error checking prohibited systems:", error);
    throw error;
  }
};
```

---

### 2. Bloquear Despliegue

#### POST `/api/compliance/prohibited-systems/block`

**Descripción:** Bloquea el despliegue de un proyecto por sistema prohibido.

**Request Body:**
```typescript
interface BlockDeploymentRequestDto {
  projectId: number;
  reason: string;
}
```

**Response:**
```typescript
interface ProhibitedSystemActionResponseDto {
  success: boolean;
  message: string;
  timestamp: string;
}
```

**Ejemplo de Uso:**
```typescript
const blockDeployment = async (projectId: number, reason: string) => {
  try {
    const response = await fetch("/api/compliance/prohibited-systems/block", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        projectId,
        reason,
      }),
    });

    if (!response.ok) {
      throw new Error("Failed to block deployment");
    }

    const data: ProhibitedSystemActionResponseDto = await response.json();
    return data;
  } catch (error) {
    console.error("Error blocking deployment:", error);
    throw error;
  }
};
```

---

### 3. Obtener Catálogo

#### GET `/api/compliance/prohibited-systems/catalog`

**Descripción:** Obtiene el catálogo completo de sistemas prohibidos.

**Query Parameters:**
- `category` (opcional): Filtrar por categoría (ART_5_1_A, ART_5_1_B, ART_5_1_C, ART_5_1_D)
- `active` (opcional): Filtrar por estado activo (true/false)

**Response:**
```typescript
interface ProhibitedSystemDto {
  id: number;
  name: string;
  category: string;
  description: string;
  keywords: string[];
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

type ProhibitedSystemCatalogResponse = ProhibitedSystemDto[];
```

**Ejemplo de Uso:**
```typescript
const getCatalog = async (category?: string, active?: boolean) => {
  const params = new URLSearchParams();
  if (category) params.append("category", category);
  if (active !== undefined) params.append("active", active.toString());

  const response = await fetch(
    `/api/compliance/prohibited-systems/catalog?${params.toString()}`
  );

  if (!response.ok) {
    throw new Error("Failed to fetch catalog");
  }

  const data: ProhibitedSystemDto[] = await response.json();
  return data;
};
```

---

### 4. Obtener Sistema del Catálogo

#### GET `/api/compliance/prohibited-systems/catalog/{id}`

**Descripción:** Obtiene un sistema específico del catálogo.

**Response:**
```typescript
type ProhibitedSystemDetailResponse = ProhibitedSystemDto;
```

---

### 5. Obtener Detalle de Detección

#### GET `/api/compliance/prohibited-systems/{id}`

**Descripción:** Obtiene el detalle completo de una detección específica.

**Response:**
```typescript
interface ProhibitedSystemDetectionDetailDto {
  id: number;
  projectId: number;
  projectName: string;
  projectDescription: string;
  detectedSystem: {
    id: number;
    name: string;
    category: string;
  };
  confidence: number;
  matchedKeywords: string[];
  evidence: string;
  detectedAt: string;
  status: "CLEAN" | "PROHIBITED" | "WARNING" | "BLOCKED" | "RESOLVED";
  blocked: boolean;
  blockReason?: string;
}
```

---

## 📱 INTEGRACIÓN EN NEXT.JS

### 1. Pantalla Principal

**Ruta:** `/governance/compliance/prohibited-systems`

**Componente:** `app/(app)/governance/compliance/prohibited-systems/page.tsx`

**Funcionalidades:**
- Listado de sistemas detectados en cards (máx. 3 por fila)
- Métricas principales (activos, bloqueados, total)
- Filtros por estado y búsqueda
- Botones de acción: "Ver Catálogo", "Verificar Todos los Proyectos"
- Navegación a detalle y catálogo

**Ejemplo de Código:**

```typescript
"use client";

import { useState, useEffect } from "react";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

interface ProhibitedSystem {
  id: number;
  systemName: string;
  status: "CLEAN" | "PROHIBITED" | "WARNING" | "BLOCKED" | "RESOLVED";
  projectId: number;
  projectName: string;
  detectedAt: string;
}

export default function ProhibitedSystemsPage() {
  const { t } = useTranslation();
  const [systems, setSystems] = useState<ProhibitedSystem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      // TODO: Llamar al endpoint real
      // const response = await fetch("/api/compliance/prohibited-systems/check?projectId=1");
      // const data = await response.json();
      // setSystems(data.systems);
    } catch (error) {
      console.error("Error loading data:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.prohibitedSystems.title")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.prohibitedSystems.subtitle")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button
            onClick={() => {
              window.location.href = "/governance/compliance/prohibited-systems/catalog";
            }}
          >
            {t("governance.prohibitedSystems.viewCatalog")}
          </Button>
          <Button variant="outline">
            {t("governance.prohibitedSystems.verifyAllProjects")}
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {systems.map((system) => (
          <Card key={system.id}>
            <CardHeader>
              <CardTitle>{system.systemName}</CardTitle>
            </CardHeader>
            <CardBody>
              <p>{system.projectName}</p>
              <p className="text-sm text-muted-foreground">
                {new Date(system.detectedAt).toLocaleString()}
              </p>
              <Button
                onClick={() => {
                  window.location.href = `/governance/compliance/prohibited-systems/${system.id}`;
                }}
              >
                {t("governance.prohibitedSystems.viewDetails")}
              </Button>
            </CardBody>
          </Card>
        ))}
      </div>
    </div>
  );
}
```

---

### 2. Pantalla de Detalle

**Ruta:** `/governance/compliance/prohibited-systems/[id]`

**Componente:** `app/(app)/governance/compliance/prohibited-systems/[id]/page.tsx`

**Funcionalidades:**
- Información del proyecto (50% ancho)
- Detalles de detección (50% ancho)
- Estado y acciones (100% ancho)
- Keywords detectadas con badges
- Botón "Volver" con navegación

---

### 3. Pantalla de Catálogo

**Ruta:** `/governance/compliance/prohibited-systems/catalog`

**Componente:** `app/(app)/governance/compliance/prohibited-systems/catalog/page.tsx`

**Funcionalidades:**
- Listado de sistemas en catálogo en cards (máx. 4 por fila)
- Filtros por categoría y estado
- Búsqueda por nombre
- Dialog de visualización (read-only)
- Botón "Volver a Verificación"

---

### 4. Integración con Clasificación

**Ruta:** `/governance/compliance/classification`

**Componente:** `app/(app)/governance/compliance/classification/page.tsx`

**Funcionalidades:**
- Verificación automática al cargar proyecto
- Verificación antes de clasificar
- Alert si se detecta sistema prohibido
- Bloqueo de clasificación si hay sistema prohibido

**Ejemplo de Código:**

```typescript
const [prohibitedSystemCheck, setProhibitedSystemCheck] = useState<{
  checked: boolean;
  detected: boolean;
  loading: boolean;
  systems: any[];
}>({
  checked: false,
  detected: false,
  loading: false,
  systems: [],
});

const checkProhibitedSystems = async (projectId: number) => {
  try {
    setProhibitedSystemCheck((prev) => ({ ...prev, loading: true }));

    const response = await fetch(
      `/api/compliance/prohibited-systems/check?projectId=${projectId}`
    );

    if (!response.ok) {
      throw new Error("Failed to check prohibited systems");
    }

    const data: ProhibitedSystemCheckResultDto = await response.json();

    setProhibitedSystemCheck({
      checked: true,
      detected: data.detected,
      loading: false,
      systems: data.systems || [],
    });
  } catch (error) {
    console.error("Error checking prohibited systems:", error);
    setProhibitedSystemCheck((prev) => ({ ...prev, loading: false }));
  }
};

// Llamar antes de clasificar
const handleClassify = async () => {
  if (!prohibitedSystemCheck.checked && data.project.id) {
    await checkProhibitedSystems(data.project.id);
  }

  if (prohibitedSystemCheck.detected) {
    // Mostrar alert y bloquear clasificación
    return;
  }

  // Proceder con clasificación
};
```

---

## 🎨 COMPONENTES UI

### Cards de Sistemas Detectados

```typescript
<Card>
  <CardHeader>
    <CardTitle>{system.systemName}</CardTitle>
  </CardHeader>
  <CardBody>
    <div className="space-y-2">
      <div>
        <span className="font-semibold">Proyecto:</span> {system.projectName}
      </div>
      <div>
        <span className="font-semibold">Estado:</span>
        <Badge variant={getStatusVariant(system.status)}>
          {system.status}
        </Badge>
      </div>
      <div>
        <span className="font-semibold">Detectado:</span>
        {new Date(system.detectedAt).toLocaleString()}
      </div>
      <Button
        onClick={() => {
          window.location.href = `/governance/compliance/prohibited-systems/${system.id}`;
        }}
      >
        Ver Detalles
      </Button>
    </div>
  </CardBody>
</Card>
```

### Badges de Keywords

```typescript
<div className="flex flex-wrap gap-2">
  {matchedKeywords.map((keyword, idx) => (
    <Badge
      key={idx}
      className="bg-red-100 text-red-800 border-red-300 dark:bg-red-900/20 dark:text-red-200"
    >
      {keyword}
    </Badge>
  ))}
</div>
```

### Alert de Sistema Prohibido

```typescript
{prohibitedSystemCheck.detected && (
  <Alert variant="destructive">
    <AlertTriangle className="h-4 w-4" />
    <AlertTitle>Sistema Prohibido Detectado</AlertTitle>
    <AlertDescription>
      Se han detectado {prohibitedSystemCheck.systems.length} sistema(s) prohibido(s) en este proyecto.
      Por favor, revise la información antes de continuar con la clasificación.
    </AlertDescription>
  </Alert>
)}
```

---

## ⚠️ MANEJO DE ERRORES

### Ejemplo de Manejo de Errores

```typescript
const checkProhibitedSystems = async (projectId: number) => {
  try {
    const response = await fetch(
      `/api/compliance/prohibited-systems/check?projectId=${projectId}`
    );

    if (!response.ok) {
      if (response.status === 400) {
        throw new Error("Project ID is required");
      } else if (response.status === 404) {
        throw new Error("Project not found");
      } else {
        throw new Error("Failed to check prohibited systems");
      }
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error checking prohibited systems:", error);

    // Mostrar mensaje de error al usuario
    toast.error(
      error instanceof Error
        ? error.message
        : "Error al verificar sistemas prohibidos"
    );

    throw error;
  }
};
```

---

---

## 🧪 TESTING

### Unit Tests con Jest y React Testing Library

**Ubicación:** `app/(app)/governance/compliance/prohibited-systems/__tests__/`

#### Ejemplo: Test de Componente Principal

```typescript
import { render, screen, waitFor } from '@testing-library/react';
import { useTranslation } from '@/app/config/i18n';
import ProhibitedSystemsPage from '../page';

// Mock de fetch
global.fetch = jest.fn();

jest.mock('@/app/config/i18n', () => ({
  useTranslation: jest.fn(() => ({
    t: (key: string) => key,
  })),
}));

describe('ProhibitedSystemsPage', () => {
  beforeEach(() => {
    (global.fetch as jest.Mock).mockClear();
  });

  it('should render loading state', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ systems: [] }),
    });

    render(<ProhibitedSystemsPage />);

    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });

  it('should display systems when loaded', async () => {
    const mockSystems = [
      {
        id: 1,
        systemName: 'Social Scoring',
        projectName: 'Test Project',
        status: 'PROHIBITED',
        detectedAt: '2025-01-01T00:00:00Z',
      },
    ];

    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ systems: mockSystems }),
    });

    render(<ProhibitedSystemsPage />);

    await waitFor(() => {
      expect(screen.getByText('Social Scoring')).toBeInTheDocument();
    });
  });

  it('should handle API errors', async () => {
    (global.fetch as jest.Mock).mockRejectedValueOnce(
      new Error('API Error')
    );

    render(<ProhibitedSystemsPage />);

    await waitFor(() => {
      expect(screen.getByText(/error/i)).toBeInTheDocument();
    });
  });
});
```

#### Ejemplo: Test de Hook Personalizado

```typescript
import { renderHook, waitFor } from '@testing-library/react';
import { useProhibitedSystems } from '../hooks/useProhibitedSystems';

global.fetch = jest.fn();

describe('useProhibitedSystems', () => {
  it('should fetch prohibited systems', async () => {
    const mockData = {
      detected: true,
      systems: [{ id: 1, name: 'Social Scoring' }],
    };

    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => mockData,
    });

    const { result } = renderHook(() => useProhibitedSystems(1));

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
      expect(result.current.data?.detected).toBe(true);
    });
  });
});
```

### Integration Tests

**Ejemplo: Test de Flujo Completo**

```typescript
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ProhibitedSystemsPage from '../page';

describe('ProhibitedSystemsPage Integration', () => {
  it('should check prohibited systems and display results', async () => {
    const user = userEvent.setup();

    (global.fetch as jest.Mock)
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          detected: true,
          systems: [{ id: 1, name: 'Social Scoring' }],
        }),
      });

    render(<ProhibitedSystemsPage />);

    const checkButton = screen.getByText(/verify/i);
    await user.click(checkButton);

    await waitFor(() => {
      expect(screen.getByText('Social Scoring')).toBeInTheDocument();
    });
  });
});
```

### E2E Tests con Playwright (Opcional)

**Ubicación:** `e2e/prohibited-systems.spec.ts`

```typescript
import { test, expect } from '@playwright/test';

test.describe('Prohibited Systems', () => {
  test('should check prohibited systems for a project', async ({ page }) => {
    await page.goto('/governance/compliance/prohibited-systems');

    await page.fill('[name="projectId"]', '1');
    await page.click('button:has-text("Verificar")');

    await expect(page.locator('.prohibited-system-card')).toBeVisible();
  });

  test('should navigate to detail page', async ({ page }) => {
    await page.goto('/governance/compliance/prohibited-systems');

    await page.click('button:has-text("Ver Detalles")');

    await expect(page).toHaveURL(/\/prohibited-systems\/\d+/);
  });
});
```

---

## 🔧 TROUBLESHOOTING Y PROBLEMAS COMUNES

### Problema 1: Error "Failed to fetch" o CORS

**Síntoma:** Al llamar a la API, se recibe error de CORS o "Failed to fetch".

**Causas posibles:**
- El BFF no está disponible
- Error de configuración de CORS
- URL incorrecta del endpoint

**Solución:**
1. Verificar que el BFF está corriendo: `curl http://localhost:PORT/health`
2. Verificar la URL del endpoint en el código
3. Verificar configuración de CORS en el BFF
4. Revisar consola del navegador para errores específicos

---

### Problema 2: Estado no se actualiza después de una acción

**Síntoma:** Después de bloquear un despliegue o verificar sistemas, el estado no se actualiza en la UI.

**Causas posibles:**
- El estado de React no se actualiza correctamente
- La respuesta de la API no se procesa
- Error silencioso en el catch

**Solución:**
1. Verificar que el estado se actualiza en el `setState`:
   ```typescript
   const handleBlock = async () => {
     try {
       const response = await blockDeployment(projectId, reason);
       setBlocked(true); // ✅ Verificar que se actualiza
       setBlockReason(reason);
     } catch (error) {
       console.error(error); // ✅ Verificar que se loguea
     }
   };
   ```
2. Verificar que la respuesta de la API es correcta
3. Usar React DevTools para inspeccionar el estado
4. Agregar logs para debugging

---

### Problema 3: Loading infinito

**Síntoma:** El componente muestra "Loading..." indefinidamente.

**Causas posibles:**
- La API nunca responde
- Error en el catch que no actualiza el estado de loading
- El useEffect se ejecuta infinitamente

**Solución:**
1. Verificar que el `finally` actualiza el loading:
   ```typescript
   try {
     setLoading(true);
     const data = await fetchData();
     setData(data);
   } catch (error) {
     setError(error);
   } finally {
     setLoading(false); // ✅ Siempre ejecutar
   }
   ```
2. Verificar dependencias del `useEffect`:
   ```typescript
   useEffect(() => {
     loadData();
   }, []); // ✅ Array vacío para ejecutar solo una vez
   ```
3. Agregar timeout para evitar loading infinito:
   ```typescript
   const timeout = setTimeout(() => {
     if (loading) {
       setError(new Error('Request timeout'));
       setLoading(false);
     }
   }, 30000); // 30 segundos
   ```

---

### Problema 4: Navegación no funciona

**Síntoma:** Al hacer click en "Ver Detalles" o "Volver", no navega.

**Causas posibles:**
- `window.location.href` no está disponible (SSR)
- Error en la ruta
- El componente no está en el cliente

**Solución:**
1. Verificar que el componente es cliente:
   ```typescript
   "use client"; // ✅ Debe estar al inicio
   ```
2. Usar `useRouter` de Next.js en lugar de `window.location.href`:
   ```typescript
   import { useRouter } from 'next/navigation';

   const router = useRouter();

   const handleNavigate = () => {
     router.push(`/governance/compliance/prohibited-systems/${id}`);
   };
   ```
3. Verificar que la ruta existe en `app/` directory

---

### Problema 5: Traducciones no funcionan

**Síntoma:** Los textos aparecen como keys (ej: "governance.prohibitedSystems.title") en lugar de texto traducido.

**Causas posibles:**
- El hook `useTranslation` no está configurado
- Las traducciones no están cargadas
- El namespace es incorrecto

**Solución:**
1. Verificar que `useTranslation` está importado correctamente:
   ```typescript
   import { useTranslation } from "@/app/config/i18n";
   ```
2. Verificar que las traducciones existen en los archivos de i18n
3. Verificar el namespace en las traducciones:
   ```json
   {
     "governance": {
       "prohibitedSystems": {
         "title": "Sistemas Prohibidos"
       }
     }
   }
   ```

---

### Problema 6: Error al validar formularios

**Síntoma:** Al enviar un formulario, no se valida o se envía con datos inválidos.

**Causas posibles:**
- Validación no implementada
- El `onSubmit` no previene el default
- Validación solo en frontend (debe validar también en backend)

**Solución:**
1. Prevenir el default del formulario:
   ```typescript
   const handleSubmit = (e: React.FormEvent) => {
     e.preventDefault(); // ✅ Prevenir submit por defecto

     // Validar
     if (!reason || reason.length > 500) {
       setError('Reason is required and must be less than 500 characters');
       return;
     }

     // Enviar
     submitForm(reason);
   };
   ```
2. Validar en frontend Y backend
3. Mostrar errores de validación al usuario

---

## 🎯 MEJORES PRÁCTICAS

### 1. **Manejo de Estado**
- Usar `useState` para estado local simple
- Usar `useReducer` para estado complejo
- Considerar `React Query` o `SWR` para estado de servidor
- Evitar prop drilling, usar Context si es necesario

### 2. **Manejo de Errores**
- Siempre usar try-catch en llamadas async
- Mostrar mensajes de error amigables al usuario
- Loguear errores para debugging
- Manejar diferentes tipos de errores (400, 404, 500, network)

### 3. **Performance**
- Usar `React.memo` para componentes que no cambian frecuentemente
- Usar `useMemo` y `useCallback` cuando sea necesario
- Lazy loading de componentes grandes
- Paginación o virtualización para listas grandes

### 4. **Navegación**
- Usar `useRouter` de Next.js en lugar de `window.location.href`
- Prefetch de rutas cuando sea posible
- Manejar estados de carga durante navegación

### 5. **Validación**
- Validar en frontend para UX
- Validar en backend para seguridad
- Mostrar errores de validación en tiempo real cuando sea posible
- Usar bibliotecas de validación (Zod, Yup) para esquemas complejos

### 6. **Accesibilidad**
- Usar etiquetas semánticas HTML
- Agregar `aria-label` cuando sea necesario
- Manejar focus correctamente
- Asegurar contraste de colores suficiente

### 7. **Testing**
- Escribir tests para lógica de negocio
- Tests de integración para flujos críticos
- Mock de APIs en tests
- Mantener cobertura de tests razonable (>70%)

---

## 📊 PERFORMANCE Y OPTIMIZACIÓN

### Lazy Loading de Componentes

```typescript
import dynamic from 'next/dynamic';

const ProhibitedSystemsCatalog = dynamic(
  () => import('./ProhibitedSystemsCatalog'),
  {
    loading: () => <div>Loading catalog...</div>,
    ssr: false, // Si no necesita SSR
  }
);
```

### Memoización de Componentes

```typescript
import { memo } from 'react';

const ProhibitedSystemCard = memo(({ system }: { system: ProhibitedSystem }) => {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{system.name}</CardTitle>
      </CardHeader>
      {/* ... */}
    </Card>
  );
});

ProhibitedSystemCard.displayName = 'ProhibitedSystemCard';
```

### Optimización de Re-renders

```typescript
import { useMemo, useCallback } from 'react';

const ProhibitedSystemsPage = () => {
  const [systems, setSystems] = useState<ProhibitedSystem[]>([]);
  const [filter, setFilter] = useState('');

  // Memoizar lista filtrada
  const filteredSystems = useMemo(() => {
    return systems.filter(system =>
      system.name.toLowerCase().includes(filter.toLowerCase())
    );
  }, [systems, filter]);

  // Memoizar callbacks
  const handleCheck = useCallback(async (projectId: number) => {
    const result = await checkProhibitedSystems(projectId);
    setSystems(result.systems);
  }, []);

  return (
    <div>
      {filteredSystems.map(system => (
        <ProhibitedSystemCard key={system.id} system={system} />
      ))}
    </div>
  );
};
```

### Paginación y Virtualización

```typescript
import { useVirtualizer } from '@tanstack/react-virtual';

const VirtualizedSystemList = ({ systems }: { systems: ProhibitedSystem[] }) => {
  const parentRef = useRef<HTMLDivElement>(null);

  const virtualizer = useVirtualizer({
    count: systems.length,
    getScrollElement: () => parentRef.current,
    estimateSize: () => 100,
  });

  return (
    <div ref={parentRef} style={{ height: '600px', overflow: 'auto' }}>
      <div
        style={{
          height: `${virtualizer.getTotalSize()}px`,
          width: '100%',
          position: 'relative',
        }}
      >
        {virtualizer.getVirtualItems().map((virtualItem) => (
          <div
            key={virtualItem.key}
            style={{
              position: 'absolute',
              top: 0,
              left: 0,
              width: '100%',
              height: `${virtualItem.size}px`,
              transform: `translateY(${virtualItem.start}px)`,
            }}
          >
            <ProhibitedSystemCard system={systems[virtualItem.index]} />
          </div>
        ))}
      </div>
    </div>
  );
};
```

### Debounce para Búsqueda

```typescript
import { useDebouncedValue } from '@/hooks/useDebouncedValue';

const ProhibitedSystemsPage = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const debouncedSearchTerm = useDebouncedValue(searchTerm, 300);

  useEffect(() => {
    if (debouncedSearchTerm) {
      searchSystems(debouncedSearchTerm);
    }
  }, [debouncedSearchTerm]);
};
```

---

## 🔒 SEGURIDAD

### Validación de Inputs

```typescript
const validateReason = (reason: string): string | null => {
  if (!reason || reason.trim().length === 0) {
    return 'Reason is required';
  }
  if (reason.length > 500) {
    return 'Reason must not exceed 500 characters';
  }
  // Sanitizar HTML
  const sanitized = reason.replace(/<[^>]*>/g, '');
  if (sanitized !== reason) {
    return 'HTML tags are not allowed';
  }
  return null;
};
```

### Sanitización de Datos

```typescript
import DOMPurify from 'dompurify';

const sanitizeInput = (input: string): string => {
  return DOMPurify.sanitize(input, {
    ALLOWED_TAGS: [], // No permitir HTML
    ALLOWED_ATTR: [],
  });
};
```

### Protección contra XSS

```typescript
// ❌ NO hacer esto:
<div dangerouslySetInnerHTML={{ __html: userInput }} />

// ✅ Hacer esto:
<div>{sanitizeInput(userInput)}</div>
```

### Autenticación y Autorización

```typescript
import { useSession } from 'next-auth/react';

const ProhibitedSystemsPage = () => {
  const { data: session, status } = useSession();

  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (!session) {
    return <div>Please log in</div>;
  }

  // Verificar roles
  const canBlockDeployment = session.user.roles?.includes('compliance-officer') ||
                             session.user.roles?.includes('deployment-manager');

  return (
    <div>
      {canBlockDeployment && (
        <Button onClick={handleBlock}>Block Deployment</Button>
      )}
    </div>
  );
};
```

### Rate Limiting en Frontend

```typescript
import { useRateLimit } from '@/hooks/useRateLimit';

const ProhibitedSystemsPage = () => {
  const { canExecute, execute } = useRateLimit(5, 60000); // 5 requests per minute

  const handleCheck = async () => {
    if (!canExecute()) {
      toast.error('Too many requests. Please wait a moment.');
      return;
    }

    execute();
    await checkProhibitedSystems(projectId);
  };
};
```

---

## 📚 REFERENCIAS

- **Base Legal:** EU AI Act Art. 5, Anexo II
- **Documentación Backend:** Ver `DEVELOPER_GUIDE_BACKEND.md`
- **Guía de Usuario:** Ver `user_guide/GUIA_FUNCIONAL_PROHIBITED_SYSTEMS.md`
- **Guía BPMN:** Ver `BPMN_WORKFLOW_GUIDE.md`
- **Next.js Documentation:** https://nextjs.org/docs
- **React Testing Library:** https://testing-library.com/react
- **Shadcn/ui Components:** https://ui.shadcn.com/
- **React Query:** https://tanstack.com/query/latest

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.1
