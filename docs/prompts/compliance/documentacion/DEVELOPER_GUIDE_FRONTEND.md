# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - TECHNICAL DOCUMENTATION

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
API Routes (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Technical Docs Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**API Routes Next.js:**
```
/api/compliance/technical-docs
```

**BFF Endpoint Base:**
```
/api/v1/technical-docs
```

**Configuración:**
- Las API routes de Next.js actúan como proxy al BFF
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS BFF

### 1. Listar Modelos con Resumen

#### GET `/api/v1/technical-docs`

**Descripción:** Lista todos los modelos con resumen de su documentación técnica.

**Query Parameters:**
- `filterModel` (opcional): Filtrar por nombre de modelo
- `filterScore` (opcional): Filtrar por score (all, high, medium, low)
- `filterCompleteness` (opcional): Filtrar por completitud (all, complete, incomplete)

**Response:**
```typescript
interface TechnicalDocumentationSummaryDto {
  modelId: number;
  modelName: string;
  overallScore: number; // 0.00 - 1.00
  isComplete: boolean;
  completedSections: number; // 0-11
  totalSections: number; // 11
  pdfUrl: string | null;
  lastUpdated: string; // ISO 8601
}
```

**Ejemplo de Uso:**
```typescript
// Sin filtros
const response = await fetch('/api/compliance/technical-docs');
const result = await response.json();
const summaries = result.data; // TechnicalDocumentationSummaryDto[]

// Con filtros
const response = await fetch(
  '/api/compliance/technical-docs?filterModel=credit&filterScore=high&filterCompleteness=complete'
);
const result = await response.json();
```

---

### 2. Obtener Documentación Completa

#### GET `/api/v1/technical-docs/{modelId}`

**Descripción:** Obtiene la documentación técnica completa de un modelo específico.

**Path Parameters:**
- `modelId` (Long): ID del modelo

**Response:**
```typescript
interface TechnicalDocumentationDto {
  idxTechnicalDoc: number;
  entityType: string; // "MODEL"
  entityId: number; // modelId
  systemName: string;
  version: string | null;
  systemDescription: string | null;
  intendedPurpose: string | null;
  developmentProcess: string | null;
  dataGovernance: string | null;
  validationProcedures: string | null;
  testingProcedures: string | null;
  monitoringMeasures: string | null;
  humanOversight: string | null;
  riskManagement: string | null;
  accuracyRobustness: string | null;
  cybersecurityMeasures: string | null;
  pdfPath: string | null;
  status: string; // "DRAFT", "APPROVED", etc.
  generatedAt: string | null; // ISO 8601
  createdAt: string; // ISO 8601
  updatedAt: string; // ISO 8601
  overallScore: number; // 0.00 - 1.00
  isComplete: boolean;
  sections: TechnicalDocSectionDto[];
}

interface TechnicalDocSectionDto {
  id: number;
  name: string; // "GENERAL_DESCRIPTION", "SYSTEM_ARCHITECTURE", etc.
  displayName: string;
  description: string;
  complete: boolean;
  score: number; // 0.00 - 1.00
  content: string;
  lastUpdated: string | null; // ISO 8601
}
```

**Ejemplo de Uso:**
```typescript
const modelId = 1001;
const response = await fetch(`/api/compliance/technical-docs/${modelId}`);
const result = await response.json();
const documentation = result.data; // TechnicalDocumentationDto
```

---

### 3. Actualizar Documentación

#### PUT `/api/v1/technical-docs/{modelId}`

**Descripción:** Actualiza la documentación técnica completa de un modelo.

**Path Parameters:**
- `modelId` (Long): ID del modelo

**Request Body:**
```typescript
interface TechnicalDocumentationDto {
  // Todos los campos editables del DTO
  systemDescription?: string;
  intendedPurpose?: string;
  developmentProcess?: string;
  dataGovernance?: string;
  // ... otros campos
}
```

**Response:**
```typescript
TechnicalDocumentationDto
```

**Ejemplo de Uso:**
```typescript
const modelId = 1001;
const response = await fetch(`/api/compliance/technical-docs/${modelId}`, {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    systemDescription: 'Updated description...',
    dataGovernance: 'Updated data governance...',
  }),
});
const result = await response.json();
```

---

### 4. Generar Documentación Automática

#### POST `/api/v1/technical-docs/{modelId}/generate`

**Descripción:** Genera documentación técnica automáticamente desde datos del modelo.

**Path Parameters:**
- `modelId` (Long): ID del modelo

**Response:**
```typescript
{
  success: boolean;
  message: string;
  data: TechnicalDocumentationDto;
}
```

**Ejemplo de Uso:**
```typescript
const modelId = 1001;
const response = await fetch(`/api/compliance/technical-docs/${modelId}/generate`, {
  method: 'POST',
});
const result = await response.json();
if (result.success) {
  console.log('Documentation generated:', result.data);
}
```

---

### 5. Validar Completitud

#### POST `/api/v1/technical-docs/{modelId}/validate`

**Descripción:** Valida la completitud de la documentación técnica según Anexo IV.

**Path Parameters:**
- `modelId` (Long): ID del modelo

**Response:**
```typescript
interface TechnicalDocValidationResultDto {
  modelId: number;
  isComplete: boolean;
  overallScore: number; // 0.00 - 1.00
  completedSections: number; // 0-11
  totalSections: number; // 11
  missingSections: string[]; // Lista de nombres de secciones faltantes
  validationDate: string; // ISO 8601
}
```

**Ejemplo de Uso:**
```typescript
const modelId = 1001;
const response = await fetch(`/api/compliance/technical-docs/${modelId}/validate`, {
  method: 'POST',
});
const result = await response.json();
const validation = result.data; // TechnicalDocValidationResultDto

if (validation.isComplete) {
  console.log('Documentation is complete!');
} else {
  console.log('Missing sections:', validation.missingSections);
}
```

---

### 6. Generar PDF

#### POST `/api/v1/technical-docs/{modelId}/pdf`

**Descripción:** Genera PDF de la documentación técnica completa.

**Path Parameters:**
- `modelId` (Long): ID del modelo

**Response:**
```typescript
interface TechnicalDocPdfResultDto {
  modelId: number;
  pdfUrl: string;
  generatedAt: string; // ISO 8601
  size: number; // Tamaño en bytes
}
```

**Ejemplo de Uso:**
```typescript
const modelId = 1001;
const response = await fetch(`/api/compliance/technical-docs/${modelId}/pdf`, {
  method: 'POST',
});
const result = await response.json();
const pdfResult = result.data; // TechnicalDocPdfResultDto

// Descargar PDF
window.location.href = pdfResult.pdfUrl;
```

---

### 7. Actualizar Sección Específica

#### PUT `/api/v1/technical-docs/{modelId}/sections/{sectionName}`

**Descripción:** Actualiza una sección específica de la documentación técnica.

**Path Parameters:**
- `modelId` (Long): ID del modelo
- `sectionName` (String): Nombre de la sección (ej: "GENERAL_DESCRIPTION")

**Request Body:**
```typescript
interface TechnicalDocSectionUpdateDto {
  content: string;
}
```

**Response:**
```typescript
{
  success: boolean;
  message: string;
  data: TechnicalDocumentationDto;
}
```

**Ejemplo de Uso:**
```typescript
const modelId = 1001;
const sectionName = 'GENERAL_DESCRIPTION';
const response = await fetch(
  `/api/compliance/technical-docs/${modelId}/sections/${sectionName}`,
  {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      content: 'Updated content for general description...',
    }),
  }
);
const result = await response.json();
```

**Nombres de Secciones Válidos:**
- `GENERAL_DESCRIPTION`
- `SYSTEM_ARCHITECTURE`
- `DATA_GOVERNANCE`
- `RISK_MANAGEMENT`
- `HUMAN_OVERSIGHT`
- `ACCURACY_ROBUSTNESS`
- `CYBERSECURITY`
- `QUALITY_CONTROL`
- `POST_MARKET_MONITORING`

---

### 8. Marcar como Completo

#### POST `/api/v1/technical-docs/{modelId}/complete`

**Descripción:** Marca la documentación técnica como completa y lanza proceso BPMN de evaluación de conformidad.

**Path Parameters:**
- `modelId` (Long): ID del modelo

**Response:**
```typescript
{
  success: boolean;
  message: string;
  data: TechnicalDocumentationDto;
}
```

**Ejemplo de Uso:**
```typescript
const modelId = 1001;
const response = await fetch(`/api/compliance/technical-docs/${modelId}/complete`, {
  method: 'POST',
});
const result = await response.json();
if (result.success) {
  console.log('Documentation marked as complete!');
  // El proceso BPMN se lanza automáticamente
}
```

---

## 🔧 INTEGRACIÓN EN NEXT.JS

### Estructura de Archivos

```
app/
  (app)/
    governance/
      compliance/
        technical-docs/
          page.tsx                    # Listado de modelos
          [modelId]/
            page.tsx                  # Detalle de documentación
          complete/
            page.tsx                  # Completar documentación (BPMN)
  api/
    compliance/
      technical-docs/
        route.ts                      # GET /api/compliance/technical-docs
        [modelId]/
          route.ts                    # GET/PUT /api/compliance/technical-docs/[modelId]
          generate/
            route.ts                  # POST /api/compliance/technical-docs/[modelId]/generate
          validate/
            route.ts                  # POST /api/compliance/technical-docs/[modelId]/validate
          pdf/
            route.ts                  # POST /api/compliance/technical-docs/[modelId]/pdf
          sections/
            [sectionName]/
              route.ts                # PUT /api/compliance/technical-docs/[modelId]/sections/[sectionName]
          complete/
            route.ts                  # POST /api/compliance/technical-docs/[modelId]/complete
```

### API Routes (Next.js)

Las API routes actúan como proxy al BFF y manejan mock data cuando `USE_MOCK=true`.

**Ejemplo - Listado de Modelos:**
```typescript
// app/api/compliance/technical-docs/route.ts
import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    // Retornar mock data
    return NextResponse.json({
      success: true,
      data: mockModelsSummary,
    });
  }

  // Llamada real al BFF
  const url = `${BFF_BASE_URL}/api/v1/technical-docs`;
  const response = await fetch(url);
  return NextResponse.json(await response.json());
}
```

---

## 💻 EJEMPLOS DE USO

### 1. Cargar Listado de Modelos

```typescript
// Component
const [models, setModels] = useState<TechnicalDocumentationSummaryDto[]>([]);
const [loading, setLoading] = useState(true);

useEffect(() => {
  loadModels();
}, []);

const loadModels = async () => {
  try {
    setLoading(true);
    const response = await fetch('/api/compliance/technical-docs');
    const result = await response.json();
    setModels(result.data);
  } catch (error) {
    console.error('Error loading models:', error);
  } finally {
    setLoading(false);
  }
};
```

### 2. Cargar Documentación de un Modelo

```typescript
const [documentation, setDocumentation] = useState<TechnicalDocumentationDto | null>(null);
const modelId = 1001;

const loadDocumentation = async () => {
  try {
    const response = await fetch(`/api/compliance/technical-docs/${modelId}`);
    const result = await response.json();
    setDocumentation(result.data);
  } catch (error) {
    console.error('Error loading documentation:', error);
  }
};
```

### 3. Generar Documentación Automática

```typescript
const handleGenerate = async () => {
  try {
    setGenerating(true);
    const response = await fetch(`/api/compliance/technical-docs/${modelId}/generate`, {
      method: 'POST',
    });
    const result = await response.json();
    if (result.success) {
      alert('Documentation generated successfully!');
      await loadDocumentation(); // Recargar
    }
  } catch (error) {
    console.error('Error generating documentation:', error);
    alert('Error generating documentation');
  } finally {
    setGenerating(false);
  }
};
```

### 4. Validar Completitud

```typescript
const handleValidate = async () => {
  try {
    setValidating(true);
    const response = await fetch(`/api/compliance/technical-docs/${modelId}/validate`, {
      method: 'POST',
    });
    const result = await response.json();
    const validation = result.data;

    if (validation.isComplete) {
      alert(`Documentation is complete! Score: ${validation.overallScore * 100}%`);
    } else {
      alert(
        `Documentation incomplete. Score: ${validation.overallScore * 100}%. ` +
        `Missing sections: ${validation.missingSections.join(', ')}`
      );
    }
  } catch (error) {
    console.error('Error validating:', error);
  } finally {
    setValidating(false);
  }
};
```

### 5. Actualizar una Sección

```typescript
const handleSaveSection = async (sectionName: string, content: string) => {
  try {
    setSaving(true);
    const response = await fetch(
      `/api/compliance/technical-docs/${modelId}/sections/${sectionName}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content }),
      }
    );
    const result = await response.json();
    if (result.success) {
      alert('Section saved successfully!');
      await loadDocumentation(); // Recargar
    }
  } catch (error) {
    console.error('Error saving section:', error);
    alert('Error saving section');
  } finally {
    setSaving(false);
  }
};
```

### 6. Generar PDF

```typescript
const handleGeneratePdf = async () => {
  try {
    setGeneratingPdf(true);
    const response = await fetch(`/api/compliance/technical-docs/${modelId}/pdf`, {
      method: 'POST',
    });
    const result = await response.json();
    const pdfResult = result.data;

    // Descargar PDF
    window.open(pdfResult.pdfUrl, '_blank');
    alert('PDF generated successfully!');
  } catch (error) {
    console.error('Error generating PDF:', error);
    alert('Error generating PDF');
  } finally {
    setGeneratingPdf(false);
  }
};
```

---

## ⚠️ MANEJO DE ERRORES

### Errores HTTP Comunes

**404 - Not Found:**
```typescript
if (response.status === 404) {
  console.error('Documentation not found');
  // Mostrar mensaje al usuario
}
```

**400 - Bad Request:**
```typescript
if (response.status === 400) {
  const error = await response.json();
  console.error('Validation error:', error.message);
  // Mostrar error de validación
}
```

**500 - Internal Server Error:**
```typescript
if (response.status === 500) {
  console.error('Server error');
  // Mostrar mensaje genérico de error
}
```

### Manejo de Errores en Componentes

```typescript
const [error, setError] = useState<string | null>(null);

const handleAction = async () => {
  try {
    setError(null);
    // ... llamada API
  } catch (error) {
    setError('An error occurred. Please try again.');
    console.error('Error:', error);
  }
};

// En el render:
{error && (
  <div className="error-message">
    {error}
  </div>
)}
```

---

## 🎨 MOCK DATA

### Configuración

El mock data está configurado en `app/config/mock.ts`:

```typescript
export const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_BASE_URL || 'http://localhost:8080';
```

### Mock Data Structure

**Mock Models Summary:**
```typescript
// app/(app)/governance/data/mockTechnicalDocs.ts
export const mockModelsSummary: ModelDocumentationSummary[] = [
  {
    modelId: 1001,
    modelName: 'Credit Scoring Model',
    overallScore: 0.85,
    isComplete: false,
    completedSections: 9,
    totalSections: 11,
    pdfUrl: null,
    lastUpdated: '2025-01-15T10:30:00Z',
  },
  // ... más modelos
];
```

**Mock Technical Docs:**
```typescript
export const mockTechnicalDocs: TechnicalDocsData = {
  modelId: 1001,
  modelName: 'Credit Scoring Model',
  overallScore: 0.85,
  isComplete: false,
  sections: [
    {
      id: 1,
      name: 'GENERAL_DESCRIPTION',
      displayName: 'General Description',
      description: 'General description of the AI system',
      complete: true,
      score: 0.95,
      content: '...',
    },
    // ... más secciones
  ],
  pdfUrl: null,
  createdAt: '2025-01-10T08:00:00Z',
  updatedAt: '2025-01-15T10:30:00Z',
};
```

---

## 🔄 ESTADOS Y CARGA

### Estados de Carga

```typescript
const [loading, setLoading] = useState(true);
const [generating, setGenerating] = useState(false);
const [validating, setValidating] = useState(false);
const [generatingPdf, setGeneratingPdf] = useState(false);
```

### Indicadores Visuales

```typescript
{loading && <Spinner />}
{generating && <LoadingButton text="Generating..." />}
{validating && <LoadingButton text="Validating..." />}
```

---

## 🌐 INTERNACIONALIZACIÓN

### Uso de Traducciones

```typescript
import { useTranslation } from "@/app/config/i18n";

const { t } = useTranslation();

// En el componente:
<h1>{t("governance.compliance.technicalDocs.title")}</h1>
<Button>{t("governance.compliance.technicalDocs.generate")}</Button>
```

### Claves de Traducción Disponibles

- `governance.compliance.technicalDocs.title`
- `governance.compliance.technicalDocs.subtitle`
- `governance.compliance.technicalDocs.generate`
- `governance.compliance.technicalDocs.validate`
- `governance.compliance.technicalDocs.generatePdf`
- `governance.compliance.technicalDocs.save`
- `governance.compliance.technicalDocs.sections`
- Y muchas más...

---

## 📚 REFERENCIAS

- **API Routes:** `app/api/compliance/technical-docs/`
- **Pantallas:** `app/(app)/governance/compliance/technical-docs/`
- **Mock Data:** `app/(app)/governance/data/mockTechnicalDocs.ts`
- **Traducciones:** `app/config/i18n/modules/governance/compliance.ts`

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.0
