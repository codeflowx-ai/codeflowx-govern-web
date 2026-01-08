# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - CLASIFICACIÓN

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Frontend (Next.js/React)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Endpoints BFF](#endpoints-bff)
3. [Integración en Next.js](#integración-en-nextjs)
4. [Estructura de Pantallas](#estructura-de-pantallas)
5. [Ejemplos de Uso](#ejemplos-de-uso)
6. [Manejo de Errores](#manejo-de-errores)
7. [Mock Data](#mock-data)
8. [Configuración](#configuración)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
BFF (Backend for Frontend)
    ↓
Classification Microservice
    ↓
Business Services
    ↓
Database (PostgreSQL)
```

### Base URL

**BFF Endpoint Base:**
```
/api/compliance/classification
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS BFF

### 1. Clasificación de Proyectos

#### POST `/api/compliance/classification/classify`

**Descripción:** Clasifica un proyecto como sistema de alto riesgo.

**Request Body:**
```typescript
interface ClassificationRequest {
  projectId: number;
  category: string; // Código de categoría (ej: "III.5")
  subcategories: string[]; // Códigos de subcategorías (ej: ["III.5.b", "III.5.c"])
  justification: string; // Justificación obligatoria (mín 100 caracteres)
  prohibitedUseChecked: boolean; // Verificación Art. 5
  prohibitedUseJustification?: string; // Justificación opcional
  regulatedSector?: boolean;
  annexILegislation?: string[];
}

interface ClassificationResponse {
  success: boolean;
  message: string;
  data: {
    projectId: number;
    isHighRisk: boolean;
    category: string;
    categoryName: string;
    subcategories: string[];
    classificationDate: string; // ISO 8601
    classifiedBy: string;
    workflowTriggered: boolean;
    workflowInstanceId?: string;
    euRegistrationRequired: boolean;
    nextSteps: string[];
  };
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/compliance/classification/classify', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    projectId: 1001,
    category: 'III.5',
    subcategories: ['III.5.b', 'III.5.c'],
    justification: 'Este sistema debe clasificarse como de alto riesgo porque afecta significativamente...',
    prohibitedUseChecked: true,
  }),
});

const result = await response.json();
if (result.success) {
  console.log('Proyecto clasificado:', result.data);
}
```

---

### 2. Categorías Anexo III

#### GET `/api/compliance/classification/categories`

**Descripción:** Obtiene todas las categorías activas del Anexo III.

**Response:**
```typescript
interface AnnexIIICategory {
  idxannexiiicategory: number;
  iduuid: string;
  anncategorycode: string; // "III.1" a "III.8"
  anncategoryname: string;
  anncategorydescription: string;
  annsubcategorycode?: string; // Solo para subcategorías
  annsubcategoryname?: string;
  annsubcategorydescription?: string;
  annislevel1: boolean; // true para categorías principales
  annislevel2: boolean; // true para subcategorías
  annparentcategory?: number; // ID de categoría padre
  annannexiiisection: string;
  annarticlereference: string;
  annkeywords?: string;
  annactive: boolean;
  anndisplayorder: number;
  anncreatedat?: string; // ISO 8601
}

interface CategoriesResponse {
  success: boolean;
  data: AnnexIIICategory[];
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/compliance/classification/categories');
const result = await response.json();

// Filtrar categorías principales (level 1)
const mainCategories = result.data.filter(cat => cat.annislevel1);

// Filtrar subcategorías de una categoría
const subcategories = result.data.filter(
  cat => cat.annislevel2 && cat.annparentcategory === categoryId
);
```

---

### 3. Sugerencias con IA

#### POST `/api/compliance/classification/suggest`

**Descripción:** Obtiene sugerencias de categorías usando IA/LLM.

**Request Body:**
```typescript
interface SuggestionRequest {
  projectDescription: string;
}

interface CategorySuggestion {
  category: AnnexIIICategory;
  relevanceScore: number; // 0-100
  reason: string; // Explicación de la sugerencia
}

interface SuggestionResponse {
  success: boolean;
  data: CategorySuggestion[]; // Top 3 sugerencias
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/compliance/classification/suggest', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    projectDescription: 'Sistema de evaluación de solvencia crediticia...',
  }),
});

const result = await response.json();
if (result.success) {
  result.data.forEach((suggestion, index) => {
    console.log(`Sugerencia ${index + 1}:`, {
      category: suggestion.category.anncategorycode,
      score: suggestion.relevanceScore,
      reason: suggestion.reason,
    });
  });
}
```

---

### 4. Lista de Proyectos Clasificados

#### GET `/api/compliance/classification/projects`

**Descripción:** Lista proyectos clasificados con paginación y filtros.

**Query Parameters:**
- `page` (opcional, default: 0): Número de página
- `size` (opcional, default: 20): Tamaño de página
- `category` (opcional): Filtrar por categoría (ej: "III.5")
- `status` (opcional): Filtrar por estado
- `search` (opcional): Búsqueda por texto
- `classifiedBy` (opcional): Filtrar por usuario
- `dateFrom` (opcional): Fecha desde (ISO 8601)
- `dateTo` (opcional): Fecha hasta (ISO 8601)

**Response:**
```typescript
interface ProjectSummary {
  projectId: number;
  projectName: string;
  category: string;
  categoryName: string;
  isHighRisk: boolean;
  classificationDate: string; // ISO 8601
  classifiedBy: string;
  status: string;
}

interface ProjectListResponse {
  success: boolean;
  data: {
    projects: ProjectSummary[];
    pagination: {
      page: number;
      size: number;
      totalElements: number;
      totalPages: number;
      first: boolean;
      last: boolean;
    };
  };
}
```

**Ejemplo de Uso:**
```typescript
// Obtener primera página
const response = await fetch('/api/compliance/classification/projects?page=0&size=10');
const result = await response.json();

// Con filtros
const response = await fetch(
  '/api/compliance/classification/projects?category=III.5&status=ACTIVE&page=0&size=20'
);
const result = await response.json();
```

---

### 5. Detalles de Proyecto

#### GET `/api/compliance/classification/projects/{projectId}`

**Descripción:** Obtiene detalles completos de un proyecto clasificado.

**Path Parameters:**
- `projectId`: ID del proyecto

**Response:**
```typescript
interface ProjectDetailsResponse {
  success: boolean;
  message: string;
  data: {
    projectId: number;
    isHighRisk: boolean;
    category: string;
    categoryName: string;
    subcategories: string[];
    classificationDate: string;
    classifiedBy: string;
    workflowTriggered: boolean;
    workflowInstanceId?: string;
    euRegistrationRequired: boolean;
    nextSteps: string[];
  };
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/compliance/classification/projects/1001');
const result = await response.json();

if (result.success) {
  console.log('Detalles del proyecto:', result.data);
}
```

---

### 6. Estadísticas

#### GET `/api/compliance/classification/projects/statistics`

**Descripción:** Obtiene estadísticas agregadas de clasificaciones.

**Query Parameters:**
- `dateFrom` (opcional): Fecha desde (ISO 8601)
- `dateTo` (opcional): Fecha hasta (ISO 8601)

**Response:**
```typescript
interface StatisticsResponse {
  success: boolean;
  data: {
    totalProjects: number;
    activeProjects: number;
    pendingReviewProjects: number;
    archivedProjects: number;
    byCategory: Record<string, number>; // { "III.5": 10, "III.6": 5, ... }
    byStatus: Record<string, number>; // { "ACTIVE": 15, "PENDING": 3, ... }
  };
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch('/api/compliance/classification/projects/statistics');
const result = await response.json();

if (result.success) {
  console.log('Estadísticas:', {
    total: result.data.totalProjects,
    porCategoria: result.data.byCategory,
  });
}
```

---

## 📁 ESTRUCTURA DE PANTALLAS

### Pantalla 1: Clasificador de Alto Riesgo

**Ruta:** `app/(app)/governance/compliance/classification/page.tsx`

**Componentes Principales:**
- Información del proyecto (solo lectura)
- Tabs: General, Clasificación, Adicional
- Sugerencia con IA
- Selector de categoría y subcategorías
- Campo de justificación con validación
- Validación Art. 5

**Estado:**
```typescript
const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
const [selectedSubcategories, setSelectedSubcategories] = useState<string[]>([]);
const [justification, setJustification] = useState('');
const [validationErrors, setValidationErrors] = useState<string[]>([]);
const [aiSuggestion, setAiSuggestion] = useState<AISuggestion | null>(null);
```

---

### Pantalla 2: Catálogo de Categorías

**Ruta:** `app/(app)/governance/compliance/classification/annex-iii-categories/page.tsx`

**Componentes Principales:**
- Búsqueda de categorías
- Lista expandible/colapsable de categorías
- Subcategorías por categoría
- CRUD de categorías (admin)

---

### Pantalla 3: Lista de Proyectos

**Ruta:** `app/(app)/governance/compliance/classification/projects/page.tsx`

**Componentes Principales:**
- Filtros (categoría, estado, búsqueda)
- Tabla de proyectos con paginación
- Estadísticas (tarjetas)
- Acciones por proyecto (ver, editar)

---

## 💻 EJEMPLOS DE USO

### Ejemplo 1: Clasificar un Proyecto

```typescript
async function classifyProject(projectId: number, classificationData: ClassificationRequest) {
  try {
    const response = await fetch('/api/compliance/classification/classify', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        projectId,
        ...classificationData,
      }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al clasificar proyecto');
    }

    const result = await response.json();

    if (result.success) {
      // Redirigir a lista de proyectos o mostrar mensaje de éxito
      router.push('/governance/compliance/classification/projects');
    }
  } catch (error) {
    console.error('Error:', error);
    // Mostrar mensaje de error al usuario
  }
}
```

---

### Ejemplo 2: Obtener Sugerencias con IA

```typescript
async function getAISuggestions(projectDescription: string) {
  setAiLoading(true);

  try {
    const response = await fetch('/api/compliance/classification/suggest', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        projectDescription,
      }),
    });

    const result = await response.json();

    if (result.success) {
      setAiSuggestion({
        suggestions: result.data.map((s: CategorySuggestion) => ({
          category: s.category,
          score: s.relevanceScore,
          reason: s.reason,
        })),
      });
    }
  } catch (error) {
    console.error('Error obteniendo sugerencias:', error);
    // Mostrar error al usuario
  } finally {
    setAiLoading(false);
  }
}
```

---

### Ejemplo 3: Validación de Justificación en Tiempo Real

```typescript
function validateJustification(justification: string, categoryName: string): string[] {
  const errors: string[] = [];

  // Validar longitud mínima
  if (justification.length < 100) {
    errors.push(`Justificación debe tener al menos 100 caracteres. Actual: ${justification.length}`);
  }

  // Validar que menciona la categoría
  if (categoryName && !justification.toLowerCase().includes(categoryName.toLowerCase())) {
    errors.push(`Justificación debe mencionar la categoría seleccionada: ${categoryName}`);
  }

  // Validar palabras clave de riesgo
  const riskKeywords = ['riesgo', 'alto riesgo', 'vulnerable', 'crítico', 'critical',
                        'afecta', 'impacta', 'derechos fundamentales'];
  const foundKeywords = riskKeywords.filter(keyword =>
    justification.toLowerCase().includes(keyword.toLowerCase())
  );

  if (foundKeywords.length < 2) {
    errors.push(`Justificación debe contener al menos 2 palabras clave de riesgo. Encontradas: ${foundKeywords.length}`);
  }

  return errors;
}

// Uso en componente
const justificationErrors = validateJustification(justification, selectedCategoryName);
setValidationErrors(justificationErrors);
```

---

## 🚨 MANEJO DE ERRORES

### Errores Comunes

#### Error 400: Validación Fallida

```typescript
if (response.status === 400) {
  const error = await response.json();
  // error.message contiene mensaje descriptivo
  setValidationErrors([error.message]);
}
```

#### Error 404: Proyecto No Encontrado

```typescript
if (response.status === 404) {
  // Mostrar mensaje: "Proyecto no encontrado"
  // Redirigir a lista de proyectos
}
```

#### Error 500: Error del Servidor

```typescript
if (response.status === 500) {
  // Mostrar mensaje genérico: "Error del servidor. Por favor, intenta más tarde."
  // Registrar error para debugging
  console.error('Error del servidor:', error);
}
```

### Ejemplo de Manejo Completo

```typescript
async function handleClassification(classificationData: ClassificationRequest) {
  try {
    setLoading(true);
    setErrors([]);

    const response = await fetch('/api/compliance/classification/classify', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(classificationData),
    });

    const result = await response.json();

    if (!response.ok) {
      if (response.status === 400) {
        // Errores de validación
        setErrors([result.message || 'Error de validación']);
      } else if (response.status === 404) {
        setErrors(['Proyecto no encontrado']);
      } else {
        setErrors(['Error al clasificar proyecto. Por favor, intenta más tarde.']);
      }
      return;
    }

    if (result.success) {
      setSuccessMessage('Proyecto clasificado exitosamente');
      // Redirigir después de 2 segundos
      setTimeout(() => {
        router.push('/governance/compliance/classification/projects');
      }, 2000);
    }
  } catch (error) {
    console.error('Error inesperado:', error);
    setErrors(['Error inesperado. Por favor, intenta más tarde.']);
  } finally {
    setLoading(false);
  }
}
```

---

## 🎭 MOCK DATA

### Configuración

El módulo usa mock data para desarrollo y demo. Se puede desactivar mediante configuración.

**Archivo:** `app/config/mock.ts`

```typescript
export const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
export const BACKEND_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';
export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_URL || 'http://localhost:8090';
```

### Uso de Mock Data

**Archivo:** `app/(app)/governance/data/mockClassification.ts`

```typescript
// Ejemplo de uso condicional
const categories = USE_MOCK
  ? mockAnnexIIICategories
  : await fetchCategoriesFromAPI();
```

### Desactivar Mocks para Producción

**Archivo:** `.env.local` o `.env.production`

```bash
NEXT_PUBLIC_USE_MOCK=false
NEXT_PUBLIC_BFF_URL=http://localhost:8090
```

---

## ⚙️ CONFIGURACIÓN

### Variables de Entorno

```bash
# Desactivar mock data
NEXT_PUBLIC_USE_MOCK=false

# URL del BFF
NEXT_PUBLIC_BFF_URL=http://localhost:8090

# URL del backend (si se conecta directamente)
NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
```

### Internacionalización (i18n)

**Archivo:** `app/config/i18n/modules/governance/compliance.ts`

El módulo soporta múltiples idiomas:
- ES (Español)
- EN (Inglés)
- FR (Francés)
- DE (Alemán)
- IT (Italiano)
- PT (Portugués)

**Uso:**
```typescript
import { useTranslation } from "@/app/config/i18n";

const { t } = useTranslation();
const title = t('compliance.classification.title');
```

---

## 📝 TIPS Y MEJORES PRÁCTICAS

### 1. Validación en Tiempo Real

Siempre valida los campos mientras el usuario escribe para dar feedback inmediato.

```typescript
useEffect(() => {
  if (justification.length > 0) {
    const errors = validateJustification(justification, categoryName);
    setValidationErrors(errors);
  }
}, [justification, categoryName]);
```

### 2. Manejo de Estados de Carga

Muestra indicadores de carga para operaciones asíncronas (IA, clasificación).

```typescript
{aiLoading && <Loader2 className="animate-spin" />}
{classifying && <Loader2 className="animate-spin" />}
```

### 3. Mensajes de Éxito/Error Claros

Usa mensajes específicos y accionables.

```typescript
{successMessage && (
  <Alert variant="success">
    <CheckCircle className="h-4 w-4" />
    <AlertDescription>{successMessage}</AlertDescription>
  </Alert>
)}

{validationErrors.map((error, index) => (
  <Alert key={index} variant="destructive">
    <AlertCircle className="h-4 w-4" />
    <AlertDescription>{error}</AlertDescription>
  </Alert>
))}
```

### 4. Navegación Contextual

Mantén el contexto cuando navegas entre pantallas.

```typescript
// Pasar projectId en la URL
router.push(`/governance/compliance/classification?projectId=${projectId}`);

// Leer projectId desde query params
const searchParams = useSearchParams();
const projectId = searchParams.get('projectId');
```

---

**Última Actualización:** Diciembre 2025
**Versión:** 1.0
**Estado:** ✅ Operativo y listo para producción
