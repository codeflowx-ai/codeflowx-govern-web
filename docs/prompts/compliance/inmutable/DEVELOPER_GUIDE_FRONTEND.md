# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - IMMUTABLE LOGS

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
6. [Mock Data](#mock-data)
7. [Componentes Principales](#componentes-principales)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
API Routes (Next.js BFF)
    ↓
BFF (Backend for Frontend) - Spring WebFlux
    ↓
Business Microservice (Immutable Logs Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**API Routes (Next.js):**
```
/api/compliance/immutable-logs
```

**BFF Endpoint Base:**
```
/api/v1/immutable-logs
```

**Configuración:**
- Los API routes están en `app/api/compliance/immutable-logs/`
- Todos los endpoints son **asíncronos** (async/await)
- Soporte para mock data mediante `USE_MOCK`

---

## 🔌 ENDPOINTS API

### 1. Búsqueda de Logs

#### POST `/api/compliance/immutable-logs/search`

**Descripción:** Busca logs inmutables según criterios de búsqueda.

**Request Body:**
```typescript
interface ImmutableLogSearchCriteria {
  logType?: string;
  entityType?: string;
  entityId?: number;
  userId?: string;
  startDate?: string; // ISO 8601
  endDate?: string; // ISO 8601
  hash?: string;
  searchText?: string;
  page?: number;
  pageSize?: number;
  sortBy?: "timestamp" | "logType" | "entityType";
  sortOrder?: "asc" | "desc";
}
```

**Response:**
```typescript
interface ImmutableLogSearchResponse {
  logs: ImmutableLog[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}

interface ImmutableLog {
  id: number;
  logType: string;
  entityType: string;
  entityId: number;
  entityName?: string;
  userId: string;
  timestamp: string; // ISO 8601
  hash: string;
  previousHash: string;
  logData: any; // JSON object
  integrityVerified: boolean;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/compliance/immutable-logs/search', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    entityType: 'Model',
    page: 1,
    pageSize: 20,
    sortBy: 'timestamp',
    sortOrder: 'desc'
  })
});

const data: ImmutableLogSearchResponse = await response.json();
```

---

### 2. Verificación de Integridad

#### POST `/api/compliance/immutable-logs/verify-integrity`

**Descripción:** Verifica la integridad de la hash chain en un rango de logs.

**Request Body:**
```typescript
interface IntegrityVerificationRequest {
  startId: number;
  endId: number;
}
```

**Response:**
```typescript
interface IntegrityVerificationResult {
  startId: number;
  endId: number;
  totalLogs: number;
  verifiedLogs: number;
  integrityScore: number; // 0.00 - 1.00
  status: "INTEGRITY_OK" | "INTEGRITY_PARTIAL" | "INTEGRITY_BROKEN";
  brokenChains?: Array<{
    logId: number;
    expectedHash: string;
    actualHash: string;
  }>;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/compliance/immutable-logs/verify-integrity', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    startId: 1,
    endId: 100
  })
});

const result: IntegrityVerificationResult = await response.json();
```

---

### 3. Detalle de Log

#### GET `/api/compliance/immutable-logs/[id]`

**Descripción:** Obtiene el detalle completo de un log con su hash chain.

**Response:**
```typescript
interface ImmutableLogDetailResponse {
  log: ImmutableLog;
  chain: ImmutableLog[];
  previousLog: ImmutableLog | null;
  nextLog: ImmutableLog | null;
  chainLength: number;
  currentPosition: number;
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(`/api/compliance/immutable-logs/${logId}`);
const detail: ImmutableLogDetailResponse = await response.json();
```

---

## 📱 INTEGRACIÓN EN NEXT.JS

### Estructura de Archivos

```
app/
├── (app)/
│   └── governance/
│       └── compliance/
│           └── immutable-logs/
│               ├── page.tsx              # Pantalla de búsqueda
│               └── [id]/
│                   └── page.tsx          # Pantalla de detalle
├── api/
│   └── compliance/
│       └── immutable-logs/
│           ├── search/
│           │   └── route.ts             # API route búsqueda
│           ├── verify-integrity/
│           │   └── route.ts             # API route verificación
│           └── [id]/
│               └── route.ts              # API route detalle
└── config/
    └── mock.ts                           # Configuración de mocks
```

### Configuración de Mocks

**Ubicación:** `app/config/mock.ts`

```typescript
export const USE_MOCK = process.env.NODE_ENV !== 'production'
  ? process.env.NEXT_PUBLIC_USE_MOCK !== 'false'
  : process.env.NEXT_PUBLIC_USE_MOCK === 'true';

export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_BASE_URL || 'http://localhost:8080';
```

**Uso:**
- En desarrollo: `USE_MOCK = true` por defecto (usa datos mock)
- En producción: `USE_MOCK = false` (usa backend real)
- Se puede forzar con `NEXT_PUBLIC_USE_MOCK=false`

---

## 💻 EJEMPLOS DE USO

### Pantalla de Búsqueda

**Ubicación:** `app/(app)/governance/compliance/immutable-logs/page.tsx`

**Características:**
- Búsqueda avanzada con múltiples filtros
- Paginación y ordenamiento
- Verificación de integridad
- Exportación CSV, JSON, Hash Chain

**Ejemplo de Búsqueda:**
```typescript
const [criteria, setCriteria] = useState<SearchCriteria>({
  page: 1,
  pageSize: 20,
  sortBy: "timestamp",
  sortOrder: "desc"
});

const performSearch = async () => {
  try {
    setLoading(true);
    const response = await fetch("/api/compliance/immutable-logs/search", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(criteria),
    });

    if (response.ok) {
      const data = await response.json();
      setSearchResults(data);
    }
  } catch (error) {
    console.error("Error searching logs:", error);
  } finally {
    setLoading(false);
  }
};
```

**Ejemplo de Verificación de Integridad:**
```typescript
const handleVerifyIntegrity = async () => {
  if (!searchResults || searchResults.total === 0) return;

  try {
    setVerifying(true);
    // Obtener todos los resultados (sin paginación)
    const allResultsCriteria = { ...criteria, page: 1, pageSize: searchResults.total };
    const response = await fetch("/api/compliance/immutable-logs/search", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(allResultsCriteria),
    });

    if (response.ok) {
      const allResults = await response.json();
      if (allResults.logs && allResults.logs.length > 0) {
        const logIds = allResults.logs.map((log: ImmutableLog) => log.id);
        const startId = Math.min(...logIds);
        const endId = Math.max(...logIds);

        const verifyResponse = await fetch("/api/compliance/immutable-logs/verify-integrity", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ startId, endId }),
        });

        if (verifyResponse.ok) {
          const data = await verifyResponse.json();
          setIntegrityResult(data);
        }
      }
    }
  } catch (error) {
    console.error("Error verifying integrity:", error);
  } finally {
    setVerifying(false);
  }
};
```

---

### Pantalla de Detalle

**Ubicación:** `app/(app)/governance/compliance/immutable-logs/[id]/page.tsx`

**Características:**
- Información completa del log
- Visualización de hash chain
- Navegación anterior/siguiente
- Exportación de log

**Ejemplo de Carga de Detalle:**
```typescript
const loadLogDetail = async (logId: string | string[] | undefined) => {
  if (!logId) return;

  const id = Array.isArray(logId) ? logId[0] : logId;
  if (!id) return;

  try {
    setLoading(true);
    const response = await fetch(`/api/compliance/immutable-logs/${id}`);

    if (response.ok) {
      const data = await response.json();
      setLogDetail(data);
    } else {
      console.error("Error loading log detail");
    }
  } catch (error) {
    console.error("Error loading log detail:", error);
  } finally {
    setLoading(false);
  }
};
```

---

## 🎨 COMPONENTES PRINCIPALES

### 1. Pantalla de Búsqueda

**Componentes:**
- **Filtros:** Tipo de log, entidad, usuario, fechas, hash
- **Búsqueda de texto:** Búsqueda libre en todos los campos
- **Tabla de resultados:** Lista de logs con paginación
- **Botones de acción:** Verificar integridad, exportar

**Estado:**
```typescript
const [loading, setLoading] = useState(false);
const [searchResults, setSearchResults] = useState<SearchResponse | null>(null);
const [integrityResult, setIntegrityResult] = useState<IntegrityVerificationResult | null>(null);
const [showFilters, setShowFilters] = useState(false);
const [verifying, setVerifying] = useState(false);
const [criteria, setCriteria] = useState<SearchCriteria>({
  page: 1,
  pageSize: 20,
  sortBy: "timestamp",
  sortOrder: "desc"
});
```

---

### 2. Pantalla de Detalle

**Componentes:**
- **Información general:** Tipo, entidad, usuario, fecha
- **Hash chain:** Hash actual y anterior
- **Datos del log:** JSON formateado
- **Visualización de cadena:** Lista completa de logs en la cadena
- **Navegación:** Botones anterior/siguiente

**Estado:**
```typescript
const [loading, setLoading] = useState(true);
const [logDetail, setLogDetail] = useState<LogDetailResponse | null>(null);
const [copiedHash, setCopiedHash] = useState<string | null>(null);
```

---

## 📦 MOCK DATA

### Ubicación
`app/(app)/governance/data/mockImmutableLogs.ts`

### Estructura

```typescript
export interface ImmutableLog {
  id: number;
  logType: string;
  entityType: string;
  entityId: number;
  entityName?: string;
  userId: string;
  timestamp: string;
  hash: string;
  previousHash: string;
  logData: any;
  integrityVerified: boolean;
}

export const mockImmutableLogs: ImmutableLog[] = [
  {
    id: 1,
    logType: "MODEL_DEPLOYMENT",
    entityType: "Model",
    entityId: 123,
    entityName: "Credit Scoring Model v1.0",
    userId: "user1@example.com",
    timestamp: "2025-12-01T10:30:00Z",
    hash: "a1b2c3d4e5f6...",
    previousHash: "0",
    logData: {
      action: "deploy",
      modelId: 123,
      version: "1.0",
      environment: "production"
    },
    integrityVerified: true
  },
  // ... más logs
];
```

---

## 🚨 MANEJO DE ERRORES

### Errores Comunes

1. **Error de conexión:**
```typescript
try {
  const response = await fetch('/api/compliance/immutable-logs/search', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(criteria)
  });

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  const data = await response.json();
} catch (error) {
  console.error("Error searching logs:", error);
  // Mostrar mensaje de error al usuario
}
```

2. **Error de validación:**
```typescript
if (!searchResults || searchResults.logs.length === 0) {
  // Mostrar mensaje: "No se encontraron logs"
  return;
}
```

3. **Error de carga:**
```typescript
if (loading) {
  return <LoadingSpinner />;
}

if (!logDetail) {
  return <ErrorMessage message="Log no encontrado" />;
}
```

---

## 🌐 TRADUCCIONES

### Ubicación
`app/config/i18n/modules/governance/compliance.ts`

### Uso

```typescript
import { useTranslation } from "@/app/config/i18n";

export default function ImmutableLogsPage() {
  const { t, language } = useTranslation();

  return (
    <div>
      <h1>{t("governance.compliance.immutableLogs.title", "Logs Inmutables")}</h1>
      <p>{t("governance.compliance.immutableLogs.subtitle", "Búsqueda y verificación...")}</p>
    </div>
  );
}
```

### Idiomas Soportados

- Español (ES)
- Inglés (EN)
- Francés (FR)
- Alemán (DE)
- Italiano (IT)
- Portugués (PT)

---

## 🎨 ESTILOS

### Framework
- **Tailwind CSS:** Utilizado para todos los estilos
- **Componentes UI:** Reutilizables de `@/components/ui/`

### Estilos "Wow Factor"

- Gradientes de fondo
- Partículas flotantes animadas
- Cards con sombras y bordes
- Transiciones suaves
- Colores púrpura para tema de seguridad

---

---

## 📁 RUTAS EXACTAS DE ARCHIVOS

### Pantallas

**Búsqueda de Logs:**
- `codeflowx-studio/app/(app)/governance/compliance/immutable-logs/page.tsx`

**Detalle de Log:**
- `codeflowx-studio/app/(app)/governance/compliance/immutable-logs/[id]/page.tsx`

### API Routes

**Búsqueda:**
- `codeflowx-studio/app/api/compliance/immutable-logs/search/route.ts`

**Verificación de Integridad:**
- `codeflowx-studio/app/api/compliance/immutable-logs/verify-integrity/route.ts`

**Detalle:**
- `codeflowx-studio/app/api/compliance/immutable-logs/[id]/route.ts`

### Configuración

**Mocks:**
- `codeflowx-studio/app/config/mock.ts`

**Traducciones:**
- `codeflowx-studio/app/config/i18n/modules/governance/compliance.ts`

**Mock Data:**
- `codeflowx-studio/app/(app)/governance/data/mockImmutableLogs.ts`

---

## ⚙️ CONFIGURACIÓN DE VARIABLES DE ENTORNO

### Variables Requeridas

**`.env.local` (Desarrollo):**
```bash
# BFF Base URL
NEXT_PUBLIC_BFF_BASE_URL=http://localhost:8080

# Mock Data (opcional, por defecto true en desarrollo)
NEXT_PUBLIC_USE_MOCK=true
```

**`.env.production` (Producción):**
```bash
# BFF Base URL
NEXT_PUBLIC_BFF_BASE_URL=https://bff.production.com

# Mock Data (debe ser false en producción)
NEXT_PUBLIC_USE_MOCK=false
```

### Configuración de Mocks

**Ubicación:** `app/config/mock.ts`

```typescript
export const USE_MOCK = process.env.NODE_ENV !== 'production'
  ? process.env.NEXT_PUBLIC_USE_MOCK !== 'false'
  : process.env.NEXT_PUBLIC_USE_MOCK === 'true';

export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_BASE_URL || 'http://localhost:8080';
```

**Lógica:**
- **Desarrollo:** `USE_MOCK = true` por defecto (usa datos mock)
- **Producción:** `USE_MOCK = false` por defecto (usa backend real)
- Se puede forzar con variable de entorno

---

## 🔄 FLUJOS DE INTEGRACIÓN

### Flujo 1: Búsqueda de Logs

```
1. Usuario ingresa criterios de búsqueda
2. Frontend → performSearch()
3. POST /api/compliance/immutable-logs/search
4. API Route verifica USE_MOCK:
   - Si true → Retorna mockImmutableLogs filtrados
   - Si false → POST /api/v1/immutable-logs/search (BFF)
5. BFF → Business Microservice
6. Microservice → Business Service
7. Service retorna resultados
8. Frontend muestra resultados en tabla
```

### Flujo 2: Verificación de Integridad

```
1. Usuario hace clic en "Verificar Integridad"
2. Frontend → handleVerifyIntegrity()
3. Obtiene TODOS los resultados (sin paginación)
4. Calcula startId y endId del rango
5. POST /api/compliance/immutable-logs/verify-integrity
6. API Route verifica USE_MOCK:
   - Si true → Retorna resultado mock
   - Si false → POST /api/v1/immutable-logs/verify-integrity (BFF)
7. BFF → Business Microservice
8. Microservice → Business Service.verifyIntegrity()
9. Service verifica hash chain
10. Si detecta corrupción → Dispara workflow BPMN
11. Frontend muestra resultado en tarjeta
```

### Flujo 3: Navegación a Detalle

```
1. Usuario hace clic en "Ver" en tabla
2. Frontend → window.location.href = `/governance/compliance/immutable-logs/${logId}`
3. Next.js carga página dinámica [id]/page.tsx
4. useEffect → loadLogDetail(logId)
5. GET /api/compliance/immutable-logs/${logId}
6. API Route verifica USE_MOCK:
   - Si true → Retorna mockImmutableLogs[id]
   - Si false → GET /api/v1/immutable-logs/${id} (BFF)
7. BFF → Business Microservice
8. Microservice → Business Service.getLogDetailWithChain()
9. Service obtiene log + cadena completa
10. Frontend muestra detalle completo
```

---

## 🧪 TESTING

### Tests Recomendados

**Componentes:**
- `ImmutableLogsPage` - Renderizado, búsqueda, filtros
- `ImmutableLogDetailPage` - Renderizado, navegación, exportación

**Hooks:**
- `useTranslation` - Verificar traducciones
- `useState` - Verificar estado de carga

**Funciones:**
- `performSearch()` - Verificar llamada API
- `handleVerifyIntegrity()` - Verificar cálculo de rango
- `handleExportCSV()` - Verificar formato CSV
- `handleExportJSON()` - Verificar formato JSON
- `handleExportHashChain()` - Verificar formato Hash Chain

**API Routes:**
- `POST /api/compliance/immutable-logs/search` - Mock y real
- `POST /api/compliance/immutable-logs/verify-integrity` - Mock y real
- `GET /api/compliance/immutable-logs/[id]` - Mock y real

---

## 🐛 TROUBLESHOOTING

### Problemas Comunes

**1. No se muestran logs:**
- **Causa:** `USE_MOCK = false` pero backend no está disponible
- **Solución:** Verificar que BFF está corriendo, verificar `NEXT_PUBLIC_BFF_BASE_URL`

**2. Errores de traducción:**
- **Causa:** Clave de traducción incorrecta o falta traducción
- **Solución:** Verificar `app/config/i18n/modules/governance/compliance.ts`

**3. Navegación no funciona:**
- **Causa:** `useParams()` retorna array en Next.js 15+
- **Solución:** Usar `Array.isArray(params.id) ? params.id[0] : params.id`

**4. Hash chain no se visualiza:**
- **Causa:** Datos no están en formato correcto
- **Solución:** Verificar que `logDetail.chain` es un array

**5. Exportación no funciona:**
- **Causa:** Navegador bloquea descarga
- **Solución:** Verificar permisos del navegador, usar `window.open()` si es necesario

---

## 📝 MEJORES PRÁCTICAS

### Manejo de Estado

1. **Usar useState para estado local:**
   ```typescript
   const [loading, setLoading] = useState(false);
   const [searchResults, setSearchResults] = useState<SearchResponse | null>(null);
   ```

2. **Usar useEffect para carga inicial:**
   ```typescript
   useEffect(() => {
     loadLogDetail(params.id);
   }, [params.id]);
   ```

3. **Limpiar efectos:**
   ```typescript
   useEffect(() => {
     const timer = setTimeout(() => {
       // acción
     }, 1000);
     return () => clearTimeout(timer);
   }, [dependencies]);
   ```

### Manejo de Errores

1. **Siempre usar try-catch:**
   ```typescript
   try {
     const response = await fetch(url);
     if (!response.ok) throw new Error(`HTTP ${response.status}`);
     const data = await response.json();
   } catch (error) {
     console.error("Error:", error);
     // Mostrar mensaje al usuario
   }
   ```

2. **Validar datos antes de usar:**
   ```typescript
   if (!searchResults || searchResults.logs.length === 0) {
     return <EmptyState />;
   }
   ```

### Performance

1. **Debounce en búsquedas:**
   ```typescript
   const debouncedSearch = useMemo(
     () => debounce(performSearch, 300),
     [criteria]
   );
   ```

2. **Lazy loading de componentes pesados:**
   ```typescript
   const HeavyComponent = lazy(() => import('./HeavyComponent'));
   ```

3. **Memoización de cálculos costosos:**
   ```typescript
   const filteredLogs = useMemo(() => {
     return logs.filter(/* filtros */);
   }, [logs, filters]);
   ```

---

## 🔗 REFERENCIAS

### Documentación Relacionada

- **Arquitectura Frontend:** `docs/prompts/ARQUITECTURA_FRONTEND.md`
- **Integración Backend:** `docs/prompts/compliance/INTEGRACION_BACKEND_IMMUTABLE_LOGS.md`
- **Estado de Implementación:** `docs/prompts/compliance/inmutable/ESTADO_IMPLEMENTACION_IMMUTABLE_LOGS.md`

### Componentes Reutilizables

- **UI Components:** `app/components/ui/`
- **Layout Components:** `app/components/layout/`
- **Icons:** `app/components/icons/`

---

## 🚀 PRÓXIMOS PASOS

### Mejoras Opcionales

1. **Optimización de Rendimiento:**
   - Virtualización de listas largas (react-window)
   - Lazy loading de imágenes
   - Debounce en búsquedas
   - Memoización de componentes

2. **Mejoras de UX:**
   - Skeleton loaders durante carga
   - Animaciones de transición suaves
   - Notificaciones toast para acciones
   - Confirmaciones para acciones destructivas

3. **Funcionalidades Adicionales:**
   - Filtros guardados (localStorage)
   - Búsquedas recientes
   - Comparación de logs lado a lado
   - Visualización de hash chain en gráfico

4. **Testing:**
   - Tests unitarios con Jest
   - Tests de integración con Playwright
   - Tests E2E para flujos completos

---

**Última actualización:** Diciembre 2025
**Versión:** 1.0.0
