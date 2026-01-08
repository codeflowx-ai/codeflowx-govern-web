# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - CONFORMITY DECLARATION

**Versión:** 1.0
**Fecha:** Enero 2025
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
API Routes Next.js (/api/governance/compliance/conformity-declaration/...)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Conformity Declaration Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**API Routes Next.js:**
```
/api/governance/compliance/conformity-declaration
```

**BFF Endpoint Base:**
```
/api/v1/conformity-declaration
```

**Configuración:**
- Las API Routes están en `app/api/governance/compliance/conformity-declaration/`
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS API ROUTES

### 1. Lista de Proyectos con Declaraciones

#### GET `/api/governance/compliance/conformity-declaration/projects`

**Descripción:** Obtiene una lista paginada de proyectos con información resumida de sus declaraciones de conformidad.

**Query Parameters:**
- `page` (int, opcional, default 0)
- `size` (int, opcional, default 20)
- `hasDeclarations` (string, opcional, "true" o "false")
- `search` (string, opcional)

**Response:**
```typescript
interface ProjectsListResponse {
  success: boolean;
  data: {
    projects: Array<{
      projectId: number;
      projectName: string;
      description: string;
      totalDeclarations: number;
      signedDeclarations: number;
      draftDeclarations: number;
      latestDeclarationDate?: string; // ISO 8601
      latestDeclarationStatus?: "DRAFT" | "SIGNED";
      latestDeclarationVersion?: string;
      hasDeclarations: boolean;
    }>;
    pagination: {
      page: number;
      size: number;
      totalElements: number;
      totalPages: number;
    };
    statistics: {
      totalProjects: number;
      projectsWithDeclarations: number;
      totalDeclarations: number;
      totalSigned: number;
      totalDrafts: number;
    };
  };
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(
  `/api/governance/compliance/conformity-declaration/projects?page=0&size=12&hasDeclarations=true&search=healthcare`
);
const result = await response.json();
if (result.success) {
  const { projects, pagination, statistics } = result.data;
  // Usar datos
}
```

---

### 2. Datos del Manager de Declaraciones

#### GET `/api/governance/compliance/compliance/conformity-declaration/manager`

**Descripción:** Obtiene los datos completos para la pantalla de gestión de declaraciones de un proyecto específico.

**Query Parameters:**
- `projectId` (number, requerido)

**Response:**
```typescript
interface ManagerDataResponse {
  success: boolean;
  data: {
    projectId: number;
    projectName: string;
    assessments: Array<{
      idxcomplianceassessment: number;
      assessmentname: string;
      idxproject: number;
      comassessmenttype: string;
      comreadyforcertification: boolean;
    }>;
    declarations: Array<{
      idxDeclaration: number;
      projectId: number;
      projectName: string;
      system_name: string;
      provider_name: string;
      declaration_date: string; // YYYY-MM-DD
      status: "DRAFT" | "SIGNED";
      version: string;
    }>;
    preview: {
      systemName: string;
      providerName: string;
      assessmentId: number;
    } | null;
  };
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(
  `/api/governance/compliance/conformity-declaration/manager?projectId=1`
);
const result = await response.json();
if (result.success) {
  const { assessments, declarations, preview } = result.data;
  // Usar datos
}
```

---

### 3. Crear Declaración de Conformidad

#### POST `/api/governance/compliance/conformity-declaration`

**Descripción:** Crea una nueva declaración de conformidad basada en un assessment de compliance.

**Request Body:**
```typescript
interface CreateDeclarationRequest {
  assessmentId: number;
  providerName: string;
  aiSystemName: string;
}
```

**Response:**
```typescript
interface CreateDeclarationResponse {
  success: boolean;
  data: {
    idxDeclaration: number;
    assessmentId: number;
    providerName: string;
    aiSystemName: string;
    status: "DRAFT";
    createdAt: string; // ISO 8601
    version: string;
  };
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(
  `/api/governance/compliance/conformity-declaration`,
  {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      assessmentId: 1,
      providerName: "TechCorp Medical",
      aiSystemName: "Healthcare Diagnostics AI",
    }),
  }
);
const result = await response.json();
if (result.success) {
  const declaration = result.data;
  // Usar declaración creada
}
```

---

### 4. Firmar Declaración de Conformidad

#### POST `/api/governance/compliance/conformity-declaration/sign`

**Descripción:** Cambia el estado de una declaración de DRAFT a SIGNED.

**Request Body:**
```typescript
interface SignDeclarationRequest {
  declarationId: number;
  signedBy: string;
  digitalSignature?: string; // Opcional
}
```

**Response:**
```typescript
interface SignDeclarationResponse {
  success: boolean;
  data: {
    idxDeclaration: number;
    status: "SIGNED";
    signedBy: string;
    signatureDate: string; // ISO 8601
  };
}
```

**Ejemplo de Uso:**
```typescript
const response = await fetch(
  `/api/governance/compliance/conformity-declaration/sign`,
  {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      declarationId: 1,
      signedBy: "John Doe",
    }),
  }
);
const result = await response.json();
if (result.success) {
  const signedDeclaration = result.data;
  // Declaración firmada
}
```

---

### 5. Descargar PDF de Declaración

#### GET `/api/governance/compliance/conformity-declaration/{id}/pdf`

**Descripción:** Genera y descarga el documento PDF de una declaración de conformidad.

**Path Parameters:**
- `id` (number, requerido) - ID de la declaración

**Response:**
- Content-Type: `application/pdf`
- Content-Disposition: `attachment; filename=declaration_{id}.pdf`
- Body: ArrayBuffer con bytes del PDF

**Ejemplo de Uso:**
```typescript
const response = await fetch(
  `/api/governance/compliance/conformity-declaration/1/pdf`
);

if (response.ok) {
  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = `declaration_1.pdf`;
  document.body.appendChild(a);
  a.click();
  window.URL.revokeObjectURL(url);
  document.body.removeChild(a);
}
```

---

## 📁 ESTRUCTURA DE ARCHIVOS

### Pantallas Next.js

#### 1. Pantalla de Proyectos
**Ubicación:** `app/(app)/governance/compliance/conformity-declaration/projects/page.tsx`

**Componentes:**
- Lista paginada de proyectos
- Cards con información resumida
- Filtros y búsqueda
- Estadísticas globales

**Hooks:**
- `useState` para datos y paginación
- `useEffect` para cargar datos iniciales
- `useTranslation` para i18n

#### 2. Pantalla de Gestión de Declaraciones
**Ubicación:** `app/(app)/governance/compliance/conformity-declaration-manager/page.tsx`

**Componentes:**
- Header con información del proyecto
- Estadísticas en tiempo real
- Selector de assessment
- Vista previa de declaración
- Tabla de declaraciones
- Botones de acción (Firmar, Descargar PDF)

**Hooks:**
- `useState` para datos y estado
- `useEffect` para cargar datos
- `useSearchParams` para obtener projectId de URL
- `useTranslation` para i18n

### API Routes

#### 1. Projects Route
**Ubicación:** `app/api/governance/compliance/conformity-declaration/projects/route.ts`

**Funciones:**
- `GET()` - Lista proyectos con paginación y filtros

#### 2. Manager Route
**Ubicación:** `app/api/governance/compliance/conformity-declaration/manager/route.ts`

**Funciones:**
- `GET()` - Obtiene datos del manager por projectId

#### 3. Main Route
**Ubicación:** `app/api/governance/compliance/conformity-declaration/route.ts`

**Funciones:**
- `POST()` - Crea nueva declaración

#### 4. Sign Route
**Ubicación:** `app/api/governance/compliance/conformity-declaration/sign/route.ts`

**Funciones:**
- `POST()` - Firma declaración

#### 5. PDF Route
**Ubicación:** `app/api/governance/compliance/conformity-declaration/[id]/pdf/route.ts`

**Funciones:**
- `GET()` - Descarga PDF de declaración

---

## 💻 EJEMPLOS DE USO

### Ejemplo 1: Cargar Lista de Proyectos

```typescript
"use client";

import { useState, useEffect } from "react";

interface Project {
  projectId: number;
  projectName: string;
  totalDeclarations: number;
  signedDeclarations: number;
  draftDeclarations: number;
  hasDeclarations: boolean;
}

export default function ProjectsPage() {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(12);

  useEffect(() => {
    loadProjects();
  }, [page, size]);

  const loadProjects = async () => {
    try {
      setLoading(true);
      const response = await fetch(
        `/api/governance/compliance/conformity-declaration/projects?page=${page}&size=${size}`
      );
      const result = await response.json();

      if (result.success) {
        setProjects(result.data.projects);
      }
    } catch (error) {
      console.error("Error loading projects:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div>
          {projects.map((project) => (
            <div key={project.projectId}>
              <h3>{project.projectName}</h3>
              <p>Total: {project.totalDeclarations}</p>
              <p>Firmadas: {project.signedDeclarations}</p>
              <p>Borradores: {project.draftDeclarations}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
```

### Ejemplo 2: Crear Declaración

```typescript
const handleCreateDeclaration = async (
  assessmentId: number,
  providerName: string,
  aiSystemName: string
) => {
  try {
    const response = await fetch(
      `/api/governance/compliance/conformity-declaration`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          assessmentId,
          providerName,
          aiSystemName,
        }),
      }
    );

    const result = await response.json();

    if (result.success) {
      const newDeclaration = result.data;
      console.log("Declaración creada:", newDeclaration);
      // Actualizar UI
      await loadDeclarations();
    } else {
      alert(`Error: ${result.error}`);
    }
  } catch (error) {
    console.error("Error creating declaration:", error);
    alert("Error al crear declaración");
  }
};
```

### Ejemplo 3: Firmar Declaración

```typescript
const handleSignDeclaration = async (declarationId: number) => {
  try {
    const response = await fetch(
      `/api/governance/compliance/conformity-declaration/sign`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          declarationId,
          signedBy: "Current User", // TODO: Obtener del contexto
        }),
      }
    );

    const result = await response.json();

    if (result.success) {
      console.log("Declaración firmada:", result.data);
      // Actualizar UI
      await loadDeclarations();
    } else {
      alert(`Error: ${result.error}`);
    }
  } catch (error) {
    console.error("Error signing declaration:", error);
    alert("Error al firmar declaración");
  }
};
```

### Ejemplo 4: Descargar PDF

```typescript
const handleDownloadPDF = async (declarationId: number) => {
  try {
    const response = await fetch(
      `/api/governance/compliance/conformity-declaration/${declarationId}/pdf`
    );

    if (!response.ok) {
      throw new Error("Failed to download PDF");
    }

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `declaration_${declarationId}.pdf`;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  } catch (error) {
    console.error("Error downloading PDF:", error);
    alert("Error al descargar PDF");
  }
};
```

---

## 🚨 MANEJO DE ERRORES

### Errores Comunes

#### 1. Assessment no listo para certificación
```typescript
if (result.error?.includes("not ready for certification")) {
  alert("El assessment seleccionado no está listo para certificación. Debe completar todos los pasos primero.");
}
```

#### 2. Declaración ya firmada
```typescript
if (result.error?.includes("Only DRAFT declarations can be signed")) {
  alert("Esta declaración ya está firmada. No se puede firmar nuevamente.");
}
```

#### 3. Error de red
```typescript
try {
  const response = await fetch(...);
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
  }
} catch (error) {
  if (error instanceof TypeError) {
    // Error de red
    alert("Error de conexión. Verifique su conexión a internet.");
  } else {
    // Otro error
    alert(`Error: ${error.message}`);
  }
}
```

### Validaciones Frontend

```typescript
const validateCreateDeclaration = (
  assessmentId: number | null,
  providerName: string,
  aiSystemName: string
): string | null => {
  if (!assessmentId) {
    return "Debe seleccionar un assessment";
  }
  if (!providerName || providerName.trim().length === 0) {
    return "El nombre del proveedor es requerido";
  }
  if (!aiSystemName || aiSystemName.trim().length === 0) {
    return "El nombre del sistema IA es requerido";
  }
  return null;
};
```

---

## 🎨 MOCK DATA

### Configuración

Las API Routes tienen soporte para mock data cuando:
- `USE_MOCK=true` en variables de entorno, o
- `NODE_ENV=development` (por defecto)

### Mock Data en API Routes

Los mocks están definidos directamente en cada route file:

**Projects Route:**
```typescript
const mockProjects = [
  {
    projectId: 1,
    projectName: "Healthcare Diagnostics AI",
    totalDeclarations: 2,
    signedDeclarations: 1,
    draftDeclarations: 1,
    // ...
  },
  // ...
];
```

**Manager Route:**
```typescript
const mockManagerData = {
  projectId: 1,
  projectName: "Healthcare AI Project",
  assessments: [/* ... */],
  declarations: [/* ... */],
};
```

### Desactivar Mocks

Para usar datos reales del backend:
1. Configurar `USE_MOCK=false` en `.env.local`
2. Asegurar que el BFF esté corriendo en `http://localhost:8090`
3. Asegurar que el microservicio esté corriendo en `http://localhost:8099`

---

## 🌐 INTERNACIONALIZACIÓN (i18n)

### Uso de Traducciones

```typescript
import { useTranslation } from "react-i18next";

export default function DeclarationManager() {
  const { t } = useTranslation();

  return (
    <div>
      <h1>{t("governance.compliance.conformity.conformityDeclaration.title")}</h1>
      <button>
        {t("governance.compliance.conformity.conformityDeclaration.generateDeclaration")}
      </button>
    </div>
  );
}
```

### Claves de Traducción Disponibles

Todas las claves están bajo:
```
governance.compliance.conformity.conformityDeclaration.*
```

**Ejemplos:**
- `title` - Título de la pantalla
- `projects.title` - Título de proyectos
- `generateDeclaration` - Botón generar
- `signDeclaration` - Botón firmar
- `downloadPDF` - Botón descargar
- `status.draft` - Estado borrador
- `status.signed` - Estado firmado
- Y muchas más...

**Ubicación:** `app/config/i18n/modules/governance/compliance.ts`

**Idiomas soportados:** es, en, fr, de, it, pt

---

## 📝 NOTAS IMPORTANTES

### 1. Navegación

- La pantalla de proyectos está en: `/governance/compliance/conformity-declaration/projects`
- La pantalla de gestión está en: `/governance/compliance/conformity-declaration-manager?projectId={id}`
- El botón "Volver" usa `window.location.href` (no `router.push()`)

### 2. Filtrado por Proyecto

- La pantalla de gestión filtra automáticamente por `projectId` desde la URL
- Si no hay `projectId`, muestra error o datos vacíos

### 3. Estados de Declaración

- `DRAFT` - Borrador, puede ser editado y firmado
- `SIGNED` - Firmada, no puede ser editada ni firmada nuevamente

### 4. Versiones

- Las versiones se generan automáticamente: `v1.{count + 1}`
- El formato es: `v1.0`, `v1.1`, `v1.2`, etc.

---

## 🧪 TESTING

### Testing de Componentes

```typescript
// Ejemplo: Test de carga de proyectos
import { render, screen, waitFor } from '@testing-library/react';
import ProjectsPage from './projects/page';

describe('ProjectsPage', () => {
  it('should load and display projects', async () => {
    render(<ProjectsPage />);

    await waitFor(() => {
      expect(screen.getByText('Healthcare Diagnostics AI')).toBeInTheDocument();
    });
  });
});
```

### Testing de API Routes

```typescript
// Ejemplo: Test de API route
import { GET } from './projects/route';
import { NextRequest } from 'next/server';

describe('GET /api/governance/compliance/conformity-declaration/projects', () => {
  it('should return projects list', async () => {
    const request = new NextRequest('http://localhost/api/governance/compliance/conformity-declaration/projects?page=0&size=12');
    const response = await GET(request);
    const data = await response.json();

    expect(data.success).toBe(true);
    expect(data.data.projects).toBeInstanceOf(Array);
  });
});
```

### Mocking de Servicios

```typescript
// Mock de fetch para tests
global.fetch = jest.fn(() =>
  Promise.resolve({
    ok: true,
    json: async () => ({ success: true, data: { projects: [] } }),
  })
) as jest.Mock;
```

---

## 🐛 TROUBLESHOOTING

### Problemas Comunes

#### 1. Error: "Assessment not found"
**Causa:** El assessmentId no existe o fue eliminado.
**Solución:**
- Verificar que el assessment existe en la base de datos
- Verificar que el ID es correcto
- Revisar logs del backend

#### 2. Error: "Assessment is not ready for certification"
**Causa:** El assessment no ha completado todos los pasos.
**Solución:**
- Completar todos los pasos del assessment (Step 1, 2, 3, 4)
- Calcular overall score
- Verificar que `readyForCertification = true`

#### 3. Error: "Only DRAFT declarations can be signed"
**Causa:** Intentar firmar una declaración que ya está SIGNED.
**Solución:**
- Verificar el estado de la declaración
- Solo declaraciones DRAFT pueden ser firmadas
- Crear nueva versión si es necesario

#### 4. Error de Red: "Failed to fetch"
**Causa:** El BFF o microservicio no está corriendo.
**Solución:**
- Verificar que el BFF está corriendo en `http://localhost:8090`
- Verificar que el microservicio está corriendo en `http://localhost:8099`
- Revisar logs de los servicios
- Verificar configuración de `BFF_BASE_URL` en `.env.local`

#### 5. PDF no se descarga
**Causa:** Error en generación de PDF o permisos del navegador.
**Solución:**
- Verificar permisos del navegador para descargas
- Revisar logs del backend
- Verificar que el PDF fue generado correctamente
- Intentar en otro navegador

### Debugging

#### Habilitar Logs Detallados

```typescript
// En desarrollo, agregar logs detallados
const loadProjects = async () => {
  try {
    console.log('[DEBUG] Loading projects, page:', page, 'size:', size);
    const response = await fetch(...);
    console.log('[DEBUG] Response status:', response.status);
    const result = await response.json();
    console.log('[DEBUG] Response data:', result);
    // ...
  } catch (error) {
    console.error('[ERROR] Error loading projects:', error);
    console.error('[ERROR] Stack:', error.stack);
  }
};
```

#### Verificar Estado de Servicios

```bash
# Verificar que el BFF está corriendo
curl http://localhost:8090/actuator/health

# Verificar que el microservicio está corriendo
curl http://localhost:8099/actuator/health

# Verificar endpoints específicos
curl http://localhost:8099/api/v1/conformity-declaration/projects?page=0&size=12
```

---

## 🔄 EXTENSIBILIDAD

### Agregar Nuevo Endpoint

1. **Crear API Route:**
```typescript
// app/api/governance/compliance/conformity-declaration/nuevo-endpoint/route.ts
import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
  // Implementación
}
```

2. **Agregar al BFF:**
- Actualizar `ConformityDeclarationService.java` (interfaz)
- Actualizar `ConformityDeclarationServiceImpl.java` (implementación)
- Actualizar `ConformityDeclarationController.java` (BFF)

3. **Agregar al Microservicio:**
- Actualizar `ConformityDeclarationController.java` (microservicio)
- Agregar método en `ConformityDeclarationBusinessService.java` si es necesario

4. **Actualizar Frontend:**
- Crear función de servicio en el componente
- Agregar UI si es necesario

### Agregar Nuevo Campo a Declaración

1. **Backend:**
   - Agregar campo a entidad `ConformityDeclaration`
   - Agregar campo a DTOs (`ConformityDeclarationDto`, etc.)
   - Actualizar `ConformityDeclarationBusinessService` si es necesario
   - Crear migración de BD si es necesario

2. **Frontend:**
   - Actualizar interfaces TypeScript
   - Agregar campo en formularios
   - Agregar columna en tablas si aplica
   - Actualizar traducciones

### Agregar Nuevo Estado

1. **Backend:**
   - Actualizar enum/constantes de estados
   - Agregar validaciones en `signDeclaration()` o métodos relevantes
   - Actualizar queries del repositorio si es necesario

2. **Frontend:**
   - Actualizar tipos TypeScript
   - Agregar badge/estilo para nuevo estado
   - Actualizar traducciones
   - Agregar lógica de UI para transiciones de estado

---

## 🔒 SEGURIDAD Y AUTENTICACIÓN

### Autenticación

**Nota:** La autenticación se maneja a nivel de aplicación. Las API Routes deben verificar el token de sesión.

```typescript
// Ejemplo de verificación de autenticación (futuro)
import { getServerSession } from "next-auth";

export async function GET(request: NextRequest) {
  const session = await getServerSession();

  if (!session) {
    return NextResponse.json(
      { success: false, error: "Unauthorized" },
      { status: 401 }
    );
  }

  // Continuar con la lógica
}
```

### Autorización

**Roles requeridos:**
- **Compliance Officer:** Puede crear, firmar y gestionar declaraciones
- **Project Manager:** Puede ver declaraciones de sus proyectos
- **Admin:** Acceso completo

**Validación en Backend:**
- El backend debe validar permisos antes de operaciones críticas
- Firmar declaración requiere rol específico

---

## 📊 PERFORMANCE

### Optimizaciones Frontend

1. **Lazy Loading:**
```typescript
// Cargar componentes pesados de forma lazy
const DeclarationTable = lazy(() => import('./DeclarationTable'));

<Suspense fallback={<Loading />}>
  <DeclarationTable declarations={declarations} />
</Suspense>
```

2. **Memoización:**
```typescript
// Memoizar cálculos costosos
const statistics = useMemo(() => {
  return calculateStatistics(declarations);
}, [declarations]);
```

3. **Paginación:**
- Siempre usar paginación para listas grandes
- Tamaño de página recomendado: 12-24 items

### Optimizaciones Backend

1. **Índices de BD:**
- Índice en `IDXCOMPLIANCEASSESSMENT` (FK)
- Índice en `STATUS`
- Índice en `CREATEDAT` para ordenamiento

2. **Caché:**
- Considerar caché para estadísticas (Redis)
- TTL recomendado: 5-10 minutos

3. **Queries Optimizadas:**
- Usar `@Query` con JOINs cuando sea necesario
- Evitar N+1 queries

---

## 📝 MIGRACIONES Y CAMBIOS DE ESQUEMA

### Agregar Nuevo Campo a BD

1. **Crear Migración SQL:**
```sql
-- migrations/V2__add_new_field_to_declarations.sql
ALTER TABLE GOVCONFORMITYDECLARATIONS
ADD COLUMN NEWFIELD VARCHAR(255);
```

2. **Actualizar Entidad JPA:**
```java
@Column(name = "NEWFIELD")
private String newField;
```

3. **Actualizar DTOs:**
```java
private String newField;
```

4. **Actualizar Business Service:**
- Agregar lógica para el nuevo campo si es necesario

### Cambios Breaking

Si se hace un cambio breaking (ej: eliminar campo, cambiar tipo):

1. **Versionar API:**
   - Mantener endpoint anterior (`/api/v1/...`)
   - Crear nuevo endpoint (`/api/v2/...`)

2. **Deprecar Gradualmente:**
   - Marcar endpoint anterior como `@Deprecated`
   - Documentar fecha de eliminación
   - Notificar a consumidores

---

## 🔗 DEPENDENCIAS ENTRE MÓDULOS

### Compliance Assessment
- **Dependencia:** Conformity Declaration requiere assessments completados
- **Impacto:** Si se cambia estructura de Assessment, puede afectar creación de declaraciones
- **Validación:** Siempre verificar que assessment existe y está listo

### Projects
- **Dependencia:** Declaraciones están vinculadas a proyectos
- **Impacto:** Eliminar proyecto puede afectar declaraciones (cascada o restricción)
- **Validación:** Verificar que proyecto existe antes de crear declaración

---

## 📚 CASOS EDGE Y ERRORES CONOCIDOS

### Casos Edge Documentados

1. **Múltiples Assessments del Mismo Proyecto:**
   - Un proyecto puede tener múltiples assessments
   - Solo assessments con `readyForCertification = true` aparecen en selector
   - Cada assessment puede generar una declaración

2. **Versiones Concurrentes:**
   - Si dos usuarios crean declaraciones simultáneamente, pueden tener la misma versión
   - **Solución:** Usar transacciones y locks en backend

3. **Assessment Cambia Después de Crear Declaración:**
   - La declaración NO se actualiza automáticamente
   - **Solución:** Crear nueva versión de declaración desde assessment actualizado

4. **Eliminación de Assessment:**
   - Si se elimina un assessment, las declaraciones vinculadas quedan huérfanas
   - **Solución:** Implementar cascada o restricción en BD

### Errores Conocidos

1. **Error de Timeout en PDF:**
   - **Causa:** Generación de PDF puede tardar mucho
   - **Solución:** Implementar generación asíncrona con polling

2. **Race Condition en Versiones:**
   - **Causa:** Múltiples usuarios creando declaraciones simultáneamente
   - **Solución:** Usar `@Transactional` con isolation level adecuado

---

## 🔗 REFERENCIAS

- **Documentación Backend:** `docs/prompts/compliance/conformidad/DEVELOPER_GUIDE_BACKEND.md`
- **Estado de Implementación:** `docs/prompts/compliance/conformidad/ESTADO_IMPLEMENTACION_CONFORMITY_DECLARATION.md`
- **Guía de Usuario:** `docs/prompts/compliance/conformidad/user_guide/GUIA_FUNCIONAL_CONFORMITY_DECLARATION.md`
