# PROMPT: MIGRACIÓN DE PANTALLAS DE PROMPTS

## 📋 DESCRIPCIÓN DEL PROMPT

Este prompt (`cursor_end_prompts.md`) realiza la **migración de pantallas ZUL a Next.js** para el módulo de **Prompts**. Crea pantallas principales para la gestión de prompts con integración completa con **Governance** y **Compliance** según EU AI Act.

---

## 🎯 PROPÓSITO

Migrar las pantallas de gestión de prompts desde el sistema legacy (ZUL/Java) a Next.js con:
- Estilos "Wow Factor" (inspirado en Star Trek)
- Mock data para funcionamiento inmediato
- API routes preparadas para integración futura
- Internacionalización (español/inglés)
- Estructura moderna y responsive
- **Integración completa con módulos de Compliance (QMS, PMM, HITL, Traceability, etc.)**
- **Cumplimiento EU AI Act** (Art. 12, 14, 17, 19, 20, 27, 43, 49)

---

## 📦 PANTALLAS CREADAS

### 1. **Performance Comparison** (`governance/prompts/templates/performance`)

**Ubicación:** `app/(app)/governance/prompts/templates/performance/page.tsx`

**Funcionalidad:**
- ✅ **Tabla de comparación de performance** con métricas de prompts
- ✅ **Métricas mostradas:**
  - Total Items
  - Active Items
  - Deployed Items
  - Training Items
  - Offline Items
  - Average Score
- ✅ **Filtros:**
  - Búsqueda por término
  - Limpieza de filtros
- ✅ **Acciones:**
  - Ver detalles de item
- ✅ **Paginación** con navegación
- ✅ **Diseño responsive** con tabla interactiva

**API Routes:**
- `GET /api/prompts/templates/performance` - Obtener datos de performance

**Mock Data:**
- 5 items de ejemplo con métricas de performance
- Datos calculados para demostración

---

### 2. **Validation Overview** (`governance/prompts/validation/overview`)

**Ubicación:** `app/(app)/governance/prompts/validation/overview/page.tsx`

**Funcionalidad:**
- ✅ **Métricas principales:**
  - Total de validaciones
  - Validaciones activas
  - Validaciones pendientes
  - Validaciones inactivas
- ✅ **Tabla de validaciones** con información detallada:
  - ID de validación
  - Tipo de validación (ACTIVE, INACTIVE, PENDING)
  - Resultado de validación (PASSED, FAILED, PENDING)
  - Score de validación
  - Detalles de validación
  - Issues encontrados
- ✅ **Filtros avanzados:**
  - Búsqueda por término
  - Filtro por tipo de validación
  - Filtro por estado/resultado
- ✅ **Acciones:**
  - Ver detalles de validación
  - Eliminar validación
  - Registrar nueva validación
- ✅ **Paginación** con navegación
- ✅ **Badges visuales** para tipos y estados

**API Routes:**
- `GET /api/prompts/validation/overview` - Listar validaciones con filtros y paginación

**Mock Data:**
- 5 validaciones de ejemplo
- Métricas calculadas automáticamente

---

### 3. **Versioning Overview** (`governance/prompts/versioning/overview`)

**Ubicación:** `app/(app)/governance/prompts/versioning/overview/page.tsx`

**Funcionalidad:**
- ✅ **Métricas principales:**
  - Total de versiones
  - Versiones activas
  - Versiones pendientes
  - Versiones inactivas
- ✅ **Tabla de versiones** con información detallada:
  - ID de versión
  - Versión (semver)
  - Descripción
  - Contenido del prompt
  - Parámetros
  - Cambios realizados
- ✅ **Filtros avanzados:**
  - Búsqueda por término
  - Filtro por tipo de versión
  - Filtro por estado
- ✅ **Acciones:**
  - Ver detalles de versión
  - Eliminar versión
  - Registrar nueva versión
- ✅ **Paginación** con navegación
- ✅ **Badges visuales** para estados

**API Routes:**
- `GET /api/prompts/versioning/overview` - Listar versiones con filtros y paginación

**Mock Data:**
- 5 versiones de ejemplo
- Métricas calculadas automáticamente

---

### 4. **Prompts Overview** (`governance/prompts`) - Pantalla Principal

**Ubicación:** `app/(app)/governance/prompts/page.tsx`

**Funcionalidad:**
- ✅ **Listado principal de prompts** con tabla paginada
- ✅ **Búsqueda y filtros** por nombre, estado, proyecto
- ✅ **Acciones:**
  - Ver detalles de prompt
  - Editar prompt
  - Eliminar prompt
  - Crear nuevo prompt
- ✅ **Paginación** con navegación
- ✅ **Badges visuales** para estados

**Nota:** Esta pantalla no estaba documentada en el prompt original pero está implementada y es la pantalla principal del módulo.

---

### 5. **Prompt Detail** (`governance/prompts/[id]`) - Visualización y Detalle

**Ubicación:** `app/(app)/governance/prompts/[id]/page.tsx`

**Funcionalidad:**
- ✅ **Visualización completa** del prompt con información detallada
- ✅ **Tabs organizados:**
  - **Overview:** Resumen con métricas (versión, validaciones, aprobación)
  - **Content:** Contenido completo del prompt
  - **Versions:** Historial de versiones
  - **Validations:** Validaciones y análisis realizados
- ✅ **Botones de acción:**
  - **Analizar:** Inicia análisis completo (COMPLIANCE, SAFETY, BIAS, PERFORMANCE)
  - **Evaluar:** Inicia evaluación de performance y métricas
  - **Editar:** Navega a página de edición
  - **Eliminar:** Elimina el prompt (con confirmación)
- ✅ **Métricas visuales:**
  - Total de versiones
  - Total de validaciones
  - Estado de aprobación
  - Información del proyecto asociado
- ✅ **Tabla de validaciones** con:
  - Tipo de validación (SAFETY, COMPLIANCE, PERFORMANCE, BIAS)
  - Resultado (PASSED, FAILED, WARNING, PENDING)
  - Score de validación
  - Issues encontrados
  - Fecha de validación
- ✅ **Tabla de versiones** con historial completo

**API Routes:**
- `GET /api/prompts/[id]` - Obtener detalle del prompt
- `GET /api/prompts/[id]/versions` - Obtener versiones del prompt
- `GET /api/prompts/[id]/validations` - Obtener validaciones del prompt
- `POST /api/prompts/[id]/validations` - Crear nueva validación (Analizar/Evaluar)
- `PUT /api/prompts/[id]` - Actualizar prompt
- `DELETE /api/prompts/[id]` - Eliminar prompt

**Nota:** Esta pantalla es nueva y permite visualizar y gestionar el prompt completo con análisis y evaluaciones.

---

### 6. **Register/Edit Prompt** (`governance/prompts/register`)

**Ubicación:** `app/(app)/governance/prompts/register/page.tsx`

**Funcionalidad:**
- ✅ **Formulario completo** para crear/editar prompts:
  - Nombre, descripción, tipo, categoría
  - Contenido del prompt (con editor de código)
  - Parámetros (temperature, max_tokens, etc.)
  - Estado y metadata
  - Cambios (changelog)
- ✅ **Editor de código** para contenido del prompt
- ✅ **Validaciones** de formulario
- ✅ **Integración con backend** para guardar
- ✅ **Botones de análisis** (solo en modo edición):
  - **Analizar:** Inicia análisis completo del prompt
  - **Evaluar:** Inicia evaluación de performance
- ✅ **Detección automática de cambios:**
  - Si el contenido cambia, se crea nueva versión automáticamente
  - Si solo cambia metadata, se actualiza sin crear versión
- ✅ **Gestión de relaciones:**
  - Agents asociados
  - Models asociados
  - RAG Systems asociados

**API Routes:**
- `POST /api/prompts/register` - Registrar nuevo prompt
- `GET /api/prompts/{id}` - Obtener prompt para edición
- `PUT /api/prompts/{id}` - Actualizar prompt
- `POST /api/prompts/{id}/validations` - Analizar/Evaluar prompt

**Nota:** Esta pantalla no estaba documentada en el prompt original pero está implementada. Los botones de Analizar y Evaluar solo aparecen en modo edición (cuando el prompt ya existe).

---

## 🏗️ ARQUITECTURA

### Estructura de Archivos Creados

```
app/(app)/governance/prompts/
├── page.tsx                            # Prompts Overview (pantalla principal)
├── [id]/
│   └── page.tsx                        # Prompt Detail (visualización y análisis)
├── register/
│   └── page.tsx                        # Register/Edit Prompt
├── templates/
│   └── performance/
│       └── page.tsx                    # Performance Comparison
├── validation/
│   └── overview/
│       └── page.tsx                    # Validation Overview
└── versioning/
    └── overview/
        └── page.tsx                    # Versioning Overview

app/api/prompts/
├── templates/
│   └── performance/
│       └── route.ts                    # GET (performance data)
├── validation/
│   └── overview/
│       └── route.ts                    # GET (validation list)
└── versioning/
    └── overview/
        └── route.ts                    # GET (version list)
```

### Componentes UI Utilizados

- `@/components/ui/card` - Cards con glassmorphism
- `@/components/ui/badge` - Badges con gradientes
- `@/components/ui/button` - Botones con estilos modernos
- `@/components/ui/input` - Inputs con estilos
- `@/components/ui/select` - Selects para filtros
- `@/components/ui/table` - Tablas responsive
- `@/components/ui/development-banner` - Banner de desarrollo
- `lucide-react` - Iconos modernos (Brain, Search, X, Eye, Trash2, Plus)

### Estilos "Wow Factor"

- ✅ Gradientes sutiles en backgrounds
- ✅ Partículas flotantes animadas
- ✅ Glassmorphism en cards y banners
- ✅ Gradientes en títulos
- ✅ Animaciones suaves
- ✅ Efectos hover en cards y filas de tabla
- ✅ Badges con colores temáticos
- ✅ Cards de métricas con gradientes de colores

---

## 🔌 INTEGRACIÓN CON BACKEND

### Estado Actual: **BACKEND IMPLEMENTADO** ✅

El backend está **completamente implementado** en el microservicio `codeflowx-governance-prompts-service`.

**Ubicación del Backend:**
- **Controller:** `nocode-service/codeflowx-governance-prompts-service/src/main/java/com/codeflowx/govern/prompts/controller/PromptController.java`
- **Service:** `nocode-service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/prompts/PromptBusinessService.java`
- **Repositorios:** `nocode-service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/prompts/`

### Endpoints Backend Implementados

**Microservicio:** `codeflowx-governance-prompts-service`

**Base URL:** `/api/v1/prompts`

**Endpoints Principales:**

#### CRUD de Prompts
- ✅ `POST /api/v1/prompts/register` - Registrar nuevo prompt
- ✅ `GET /api/v1/prompts/{promptId}` - Obtener detalle de prompt
- ✅ `GET /api/v1/prompts` - Listar prompts con paginación
- ✅ `PUT /api/v1/prompts/{promptId}` - Actualizar prompt
- ✅ `DELETE /api/v1/prompts/{promptId}` - Eliminar prompt

#### Versioning
- ✅ `POST /api/v1/prompts/{promptId}/versions` - Crear nueva versión
- ✅ `GET /api/v1/prompts/{promptId}/versions` - Obtener versiones de un prompt
- ✅ `GET /api/v1/prompts/versioning/overview` - Overview de versioning (con paginación y filtros)

#### Validation
- ✅ `POST /api/v1/prompts/{promptId}/validations` - Crear validación
- ✅ `GET /api/v1/prompts/{promptId}/validations` - Obtener validaciones de un prompt
- ✅ `GET /api/v1/prompts/validation/overview` - Overview de validación (con paginación y filtros)

#### Performance
- ⚠️ `GET /api/v1/prompts/templates/performance` - Overview de performance
  - **Estado:** Endpoint implementado pero retorna datos vacíos (TODO pendiente)
  - **Nota:** La lógica de cálculo de performance aún no está implementada

#### Relaciones (Agents, Models, RAG)
- ✅ `POST /api/v1/prompts/{promptId}/agents/{agentId}` - Asociar Agent
- ✅ `DELETE /api/v1/prompts/{promptId}/agents/{agentId}` - Desasociar Agent
- ✅ `GET /api/v1/prompts/{promptId}/agents` - Obtener Agents asociados
- ✅ `POST /api/v1/prompts/{promptId}/models/{modelId}` - Asociar Model
- ✅ `DELETE /api/v1/prompts/{promptId}/models/{modelId}` - Desasociar Model
- ✅ `GET /api/v1/prompts/{promptId}/models` - Obtener Models asociados
- ✅ `POST /api/v1/prompts/{promptId}/rags/{ragSystemId}` - Asociar RAG System
- ✅ `DELETE /api/v1/prompts/{promptId}/rags/{ragSystemId}` - Desasociar RAG System
- ✅ `GET /api/v1/prompts/{promptId}/rags` - Obtener RAG Systems asociados

### Entidades JPA Relacionadas

**Entidades Principales:**
- ✅ **Prompt** (`com.codeflowx.govern.entity.prompts.Prompt`) - Entidad principal de prompts
- ✅ **PromptVersion** (`com.codeflowx.govern.entity.prompts.PromptVersion`) - Versiones de prompts
- ✅ **PromptValidation** (`com.codeflowx.govern.entity.prompts.PromptValidation`) - Validaciones de prompts

**Entidades de Relaciones:**
- ✅ **PromptAgent** - Relación Prompt ↔ Agent
- ✅ **PromptModel** - Relación Prompt ↔ Model
- ✅ **PromptRag** - Relación Prompt ↔ RAG System

**Repositorios:**
- ✅ `PromptRepository` - CRUD de prompts
- ✅ `PromptVersionRepository` - Gestión de versiones
- ✅ `PromptValidationRepository` - Gestión de validaciones
- ✅ `PromptAgentRepository` - Relaciones con agents
- ✅ `PromptModelRepository` - Relaciones con models
- ✅ `PromptRagRepository` - Relaciones con RAG systems

**DTOs:**
- ✅ `PromptDto` - DTO principal de prompt
- ✅ `PromptVersionDto` - DTO de versión
- ✅ `PromptValidationDto` - DTO de validación
- ✅ `PromptVersioningOverviewResponseDto` - Response del overview de versioning
- ✅ `PromptValidationOverviewResponseDto` - Response del overview de validación
- ✅ `PromptPerformanceOverviewResponseDto` - Response del overview de performance
- ✅ `PromptListResponseDto` - Response de listado paginado
- ✅ `PromptRelationDto` - DTO de relaciones (Agent/Model/RAG)

### Estado de Integración Frontend ↔ Backend

**Estado Actual:** Las pantallas frontend usan **mock data** en las API routes de Next.js.

**Para conectar con el backend real:**

1. **Configurar URL del backend** en variables de entorno:
```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
# o la URL del microservicio en producción
```

2. **Actualizar las API routes** de Next.js para hacer llamadas al backend:
   - `app/api/prompts/templates/performance/route.ts`
   - `app/api/prompts/validation/overview/route.ts`
   - `app/api/prompts/versioning/overview/route.ts`

3. **Ejemplo de integración:**
```typescript
// En lugar de retornar mock data:
const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/prompts/validation/overview?page=${page}&size=${size}`);
const data = await response.json();
return NextResponse.json(data);
```

---

## 📊 DATOS Y MODELOS

### Interfaces TypeScript

**PerformanceItem:**
```typescript
{
  totalItems: number;
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  avgScore: number;
}
```

**ValidationItem:**
```typescript
{
  idxpromptvalidation: number;
  prmvalidationtype: string;        // ACTIVE, INACTIVE, PENDING
  prmvalidationresult: string;       // PASSED, FAILED, PENDING
  prmvalidationscore: number;
  prmvalidationdetails: string;
  prmissuesfound: number;
}
```

**VersionItem:**
```typescript
{
  idxpromptversion: number;
  prmversion: string;                // Semver format
  prmdescription: string;
  prmcontent: string;
  prmparameters: string;
  prmchanges: string;
}
```

---

## 🌐 INTERNACIONALIZACIÓN

### Traducciones Incluidas

**Español/Inglés** para:
- Títulos y subtítulos
- Labels de tablas
- Mensajes de botones
- Estados y tipos
- Mensajes de error/éxito
- Placeholders de búsqueda
- Métricas y estadísticas

**Ubicación:** `app/config/i18n.ts`

**Claves agregadas:**
- `prompts.performance.*` - Traducciones para Performance Comparison
- `prompts.validation.*` - Traducciones para Validation Overview
- `prompts.versioning.*` - Traducciones para Versioning Overview

---

## 📍 NAVEGACIÓN Y MENÚ

### Rutas Disponibles

- `/governance/prompts` - Prompts Overview (pantalla principal, default)
- `/governance/prompts/[id]` - Prompt Detail (visualización, análisis y evaluación)
- `/governance/prompts/register` - Register/Edit Prompt (crear nuevo)
- `/governance/prompts/[id]/edit` - Edit Prompt (editar existente, redirige a register con modo edición)
- `/governance/prompts/templates/performance` - Performance Comparison
- `/governance/prompts/validation/overview` - Validation Overview
- `/governance/prompts/versioning/overview` - Versioning Overview

### Entrada en Menú

**Ubicación:** `app/config/modules.ts`

Las pantallas están registradas en el menú del módulo `Prompts` para acceso desde el sidebar:

**Ubicación:** `app/config/modules.ts` (líneas 915-970)

```typescript
case "Prompts":
  return [
    {
      name: "Prompts",
      href: `/governance/prompts`,
      icon: "FileText",
      roles: [...],
      isDefault: true,
    },
    {
      name: "Validation",
      href: `/governance/prompts/validation/overview`,
      icon: "CheckCircle",
      roles: [...],
    },
    {
      name: "Versioning",
      href: `/governance/prompts/versioning/overview`,
      icon: "GitBranch",
      roles: [...],
    },
    {
      name: "Performance",
      href: `/governance/prompts/templates/performance`,
      icon: "TrendingUp",
      roles: [...],
    },
  ];
```

---

## ✅ ESTADO ACTUAL

### Pantallas Implementadas

- ✅ `app/(app)/governance/prompts/page.tsx` - **COMPLETADO** (pantalla principal)
- ✅ `app/(app)/governance/prompts/[id]/page.tsx` - **COMPLETADO** (detalle, visualización, análisis)
- ✅ `app/(app)/governance/prompts/register/page.tsx` - **COMPLETADO** (registro/edición con botones de análisis)
- ✅ `app/(app)/governance/prompts/templates/performance/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/prompts/validation/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/prompts/versioning/overview/page.tsx` - **COMPLETADO**

### API Routes Implementadas (Frontend - Mock)

- ✅ `app/api/prompts/register/route.ts` - **COMPLETADO** (POST para registro)
- ✅ `app/api/prompts/[id]/route.ts` - **COMPLETADO** (GET, PUT, DELETE para detalle)
- ✅ `app/api/prompts/[id]/versions/route.ts` - **COMPLETADO** (GET versiones)
- ✅ `app/api/prompts/[id]/validations/route.ts` - **COMPLETADO** (GET, POST validaciones)
- ✅ `app/api/prompts/templates/performance/route.ts` - **COMPLETADO** (mock data)
- ✅ `app/api/prompts/validation/overview/route.ts` - **COMPLETADO** (mock data)
- ✅ `app/api/prompts/versioning/overview/route.ts` - **COMPLETADO** (mock data)

**Nota:** Las API routes del frontend actualmente retornan mock data. Para conectar con el backend real, deben actualizarse para hacer llamadas HTTP al microservicio.

### Configuración

- ✅ `app/config/i18n.ts` - Traducciones agregadas (es/en)
- ✅ `app/config/modules.ts` - Módulo "Prompts" y menú agregado

---

## 🔄 FLUJO DE USUARIO

1. **Usuario accede a `/governance/prompts`** (pantalla principal)
   - Ve listado de prompts con búsqueda y filtros
   - Puede crear, editar, eliminar prompts
   - Puede ver detalles de cada prompt (clic en fila o botón "Ver")

2. **Usuario accede a `/governance/prompts/[id]`** (pantalla de detalle)
   - Ve información completa del prompt
   - Puede **Analizar** el prompt (validaciones completas)
   - Puede **Evaluar** el prompt (métricas de performance)
   - Ve resultados de análisis previos en tab "Validations"
   - Ve historial de versiones en tab "Versions"
   - Puede editar o eliminar el prompt

3. **Usuario accede a `/governance/prompts/register`** (crear nuevo)
   - Completa formulario para crear prompt
   - Usa editor de código para contenido
   - Guarda y regresa a la lista

4. **Usuario accede a `/governance/prompts/[id]/edit`** (editar existente)
   - Ve formulario prellenado con datos del prompt
   - Puede **Analizar** el prompt (botón visible en modo edición)
   - Puede **Evaluar** el prompt (botón visible en modo edición)
   - Si cambia el contenido, se crea nueva versión automáticamente
   - Guarda cambios y regresa a la lista o detalle

5. **Usuario accede a `/governance/prompts/templates/performance`**
   - Ve tabla de comparación de performance
   - Puede buscar y filtrar
   - Puede ver detalles de items

6. **Usuario accede a `/governance/prompts/validation/overview`**
   - Ve métricas de validaciones
   - Puede filtrar por tipo y estado
   - Puede registrar nueva validación
   - Puede ver/eliminar validaciones existentes

7. **Usuario accede a `/governance/prompts/versioning/overview`**
   - Ve métricas de versiones
   - Puede filtrar por tipo y estado
   - Puede registrar nueva versión
   - Puede ver/eliminar versiones existentes

---

## 📝 NOTAS IMPORTANTES

1. **Mock Data:** Todas las pantallas funcionan con datos mock para desarrollo
2. **Estilos:** Mantienen el estilo "Wow Factor" consistente con el resto de la aplicación
3. **Responsive:** Diseño adaptativo para móvil, tablet y desktop
4. **Validaciones:** Filtros y búsqueda funcionan en tiempo real
5. **Error Handling:** Manejo de errores básico implementado
6. **Loading States:** Estados de carga durante peticiones (preparado para futuro)
7. **Análisis y Evaluación:** Botones "Analizar" y "Evaluar" implementados en:
   - Página de detalle (`/governance/prompts/[id]`)
   - Página de edición (`/governance/prompts/register` en modo edición)
8. **Navegación:** Implementada correctamente entre todas las pantallas

---

## 🚀 PRÓXIMOS PASOS

1. **Integración Backend:** Conectar con microservicio `leka-prompts`
2. **Navegación:** Implementar navegación a pantallas de detalle
3. **Formularios:** Crear pantallas de registro/edición para validaciones y versiones
4. **Validaciones Avanzadas:** Agregar validaciones más robustas en formularios
5. **Testing:** Crear tests unitarios y de integración
6. **Documentación:** Documentar APIs y flujos
7. **Optimizaciones:** Mejorar rendimiento y UX

---

## 🔍 ANÁLISIS Y EVALUACIÓN DE PROMPTS

### Botones de Análisis y Evaluación

Los prompts incluyen funcionalidad de **análisis** y **evaluación** integrada directamente en las pantallas de detalle y edición.

#### **Botón "Analizar"** (BarChart3)

**Ubicación:**
- Página de detalle: `/governance/prompts/[id]` (siempre visible)
- Página de edición: `/governance/prompts/register` (solo en modo edición)

**Funcionalidad:**
- Inicia un **análisis completo** del prompt que incluye:
  - **SAFETY:** Validación de seguridad (prompt injection, jailbreak)
  - **COMPLIANCE:** Cumplimiento EU AI Act
  - **BIAS:** Detección de sesgos
  - **PERFORMANCE:** Análisis básico de rendimiento
- Crea una validación de tipo `COMPLIANCE` con estado `PENDING`
- Los resultados se muestran en el tab "Validations" de la página de detalle
- Permite identificar problemas antes de aprobar el prompt

**Endpoint Backend:**
- `POST /api/v1/prompts/{promptId}/validations`
- Body: `{ prmvalidationtype: "COMPLIANCE", ... }`

**Flujo:**
```
1. Usuario hace clic en "Analizar"
2. Se crea validación con tipo COMPLIANCE
3. Backend ejecuta análisis completo
4. Resultados se muestran en tab "Validations"
5. Usuario puede revisar issues y scores
```

---

#### **Botón "Evaluar"** (TrendingUp)

**Ubicación:**
- Página de detalle: `/governance/prompts/[id]` (siempre visible)
- Página de edición: `/governance/prompts/register` (solo en modo edición)

**Funcionalidad:**
- Inicia una **evaluación de performance** del prompt que incluye:
  - Métricas de rendimiento
  - Tiempo de respuesta
  - Eficiencia de tokens
  - Calidad de respuestas
- Crea una validación de tipo `PERFORMANCE` con estado `PENDING`
- Los resultados se muestran en el tab "Validations" de la página de detalle
- Permite optimizar el prompt antes de producción

**Endpoint Backend:**
- `POST /api/v1/prompts/{promptId}/validations`
- Body: `{ prmvalidationtype: "PERFORMANCE", ... }`

**Flujo:**
```
1. Usuario hace clic en "Evaluar"
2. Se crea validación con tipo PERFORMANCE
3. Backend ejecuta evaluación de métricas
4. Resultados se muestran en tab "Validations"
5. Usuario puede revisar scores y optimizar
```

---

#### **Visualización de Resultados**

**En la página de detalle (`/governance/prompts/[id]`):**

**Tab "Validations":**
- Tabla con todas las validaciones realizadas
- Columnas:
  - **Tipo:** SAFETY, COMPLIANCE, PERFORMANCE, BIAS (con badges de colores)
  - **Resultado:** PASSED, FAILED, WARNING, PENDING (con badges)
  - **Score:** Puntuación de 0-10
  - **Issues:** Número de problemas encontrados
  - **Fecha:** Fecha de la validación
- Botón "Actualizar" para refrescar resultados
- Filtros por tipo y resultado (futuro)

**Tipos de Validación:**
- **SAFETY** (rojo): Validación de seguridad
- **COMPLIANCE** (azul): Cumplimiento normativo
- **PERFORMANCE** (verde): Rendimiento y métricas
- **BIAS** (amarillo): Detección de sesgos

**Estados de Resultado:**
- **PASSED** (verde): Validación exitosa
- **FAILED** (rojo): Validación fallida
- **WARNING** (amarillo): Advertencias encontradas
- **PENDING** (gris): Análisis en proceso

---

#### **Integración con Compliance**

Los análisis y evaluaciones se integran automáticamente con los módulos de compliance:

1. **HITL:** Si el análisis detecta problemas críticos, puede requerir aprobación HITL
2. **QMS:** Los scores de validación se incluyen en el cálculo del score QMS del proyecto
3. **PMM:** Las evaluaciones de performance se monitorean post-mercado
4. **Traceability:** Todas las validaciones se registran en logs inmutables

---

## 🔗 INTEGRACIÓN CON GOVERNANCE Y COMPLIANCE

### Relación con Módulos de Compliance

Los prompts están **directamente integrados** con los módulos de compliance y governance según EU AI Act:

#### 1. **Project como Núcleo (OBLIGATORIO)**

**Relación:** `Prompt → Project` (ManyToOne, nullable = false)

**Justificación:**
- **Trazabilidad**: Todo prompt debe estar asociado a un proyecto para cumplimiento EU AI Act
- **Clasificación**: El proyecto determina la categoría de riesgo (B.1, B.2, C, D, Prohibido)
- **FRIA**: Los prompts de sistemas de alto riesgo requieren evaluación FRIA asociada al proyecto
- **Auditoría**: Permite rastrear todos los prompts usados en un proyecto específico

**Implementación en Backend:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXPROJECT", nullable = false)
private Project project;
```

**Impacto en Frontend:**
- Al crear/editar prompt, **debe seleccionarse un proyecto** (obligatorio)
- El nivel de riesgo del proyecto determina las validaciones requeridas
- Los prompts se filtran por proyecto en las vistas

---

#### 2. **HITL Supervision (Art. 14)**

**Módulo:** `governance/compliance/hitl-supervision`

**Integración:**
- Prompts en proyectos **B.1, B.2** (alto riesgo) requieren **aprobación HITL obligatoria**
- Prompts en proyectos **C, D** pueden auto-aprobarse con validación exitosa
- Prompts en proyectos **Prohibidos** son bloqueados automáticamente

**Flujo:**
```
1. Crear Prompt → Asociar a Project (B.1)
2. Crear PromptVersion (1.0.0) → Estado: DRAFT
3. Ejecutar Validaciones (SAFETY, COMPLIANCE, BIAS)
4. Crear PromptApproval → Requiere aprobación HITL
5. Si aprobado → Activar versión → Estado: ACTIVE
```

**Endpoints Relacionados:**
- `POST /api/v1/prompts/{promptId}/approval` - Crear solicitud de aprobación HITL
- `GET /api/v1/compliance/hitl/pending` - Obtener aprobaciones pendientes de prompts
- `POST /api/v1/compliance/hitl/decisions` - Registrar decisión HITL

**Documentación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_HITL.md`

---

#### 3. **QMS - Quality Management System (Art. 17)**

**Módulo:** `governance/compliance/qms`

**Integración:**
- Los prompts deben cumplir con los **13 módulos QMS** (A-M)
- Validaciones de calidad antes de aprobación
- Detección de gaps en cumplimiento QMS

**Validaciones QMS para Prompts:**
- **Módulo A (Compliance Strategy)**: Estrategia de cumplimiento del prompt
- **Módulo B (Design Control)**: Control de diseño del prompt
- **Módulo C (Quality Assurance)**: Aseguramiento de calidad
- **Módulo D (Test Validation)**: Validación de pruebas

**Endpoints Relacionados:**
- `GET /api/v1/compliance/qms/projects/{projectId}/prompts` - Prompts del proyecto con scores QMS
- `POST /api/v1/compliance/qms/prompts/{promptId}/validate` - Validar prompt contra QMS

**Documentación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_QMS.md`

---

#### 4. **PMM - Post-Market Monitoring (Art. 20, 72)**

**Módulo:** `governance/compliance/post-market-monitoring`

**Integración:**
- Monitoreo de prompts en producción
- Detección de incidentes relacionados con prompts
- Métricas de uso y rendimiento
- Alertas cuando un prompt causa problemas

**Métricas PMM para Prompts:**
- Uso del prompt (número de ejecuciones)
- Tasa de éxito/fallo
- Tiempo de respuesta
- Detección de sesgos en producción
- Incidentes reportados

**Endpoints Relacionados:**
- `GET /api/v1/compliance/pmm/prompts/{promptId}/metrics` - Métricas PMM del prompt
- `POST /api/v1/compliance/pmm/prompts/{promptId}/incidents` - Reportar incidente

**Documentación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_PMM.md`

---

#### 5. **Traceability (Art. 12, 19)**

**Módulo:** `governance/compliance/traceability`

**Integración:**
- Trazabilidad completa de prompts: versiones, validaciones, aprobaciones
- Logs inmutables de todos los cambios
- Evidencias para auditorías

**Trazabilidad Requerida:**
- Historial completo de versiones (inmutable)
- Registro de todas las validaciones realizadas
- Decisiones HITL relacionadas
- Uso en producción (qué agentes/models lo usan)
- Cambios y quién los realizó

**Endpoints Relacionados:**
- `GET /api/v1/compliance/traceability/prompts/{promptId}` - Trazabilidad completa del prompt
- `GET /api/v1/compliance/traceability/prompts/{promptId}/evidence` - Exportar evidencias

**Documentación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_TRACEABILITY.md`

---

#### 6. **Classification (Art. 6 + Anexo III)**

**Módulo:** `governance/compliance/classification`

**Integración:**
- El nivel de riesgo del **proyecto** determina las validaciones del prompt
- Prompts en proyectos de alto riesgo requieren validaciones estrictas
- Prompts en proyectos prohibidos no pueden ser aprobados

**Reglas de Clasificación:**
- **Proyecto B.1, B.2**: Validaciones estrictas + HITL obligatorio
- **Proyecto C, D**: Validaciones estándar + auto-aprobación posible
- **Proyecto Prohibido**: Bloqueo automático

**Endpoints Relacionados:**
- `GET /api/v1/compliance/classification/projects/{projectId}/risk-level` - Obtener nivel de riesgo
- `GET /api/v1/prompts?projectId={id}&riskLevel={level}` - Filtrar prompts por riesgo

**Documentación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_CLASSIFICATION.md`

---

#### 7. **Technical Documentation (Art. 11 + Anexo IV)**

**Módulo:** `governance/compliance/technical-docs`

**Integración:**
- Los prompts deben documentarse en **AIActTechnicalDocumentation**
- Deben incluirse en la **ConformityDeclaration** del proyecto
- Disponibles para auditorías

**Documentación Requerida:**
- Contenido del prompt
- Parámetros de configuración
- Versiones y cambios
- Validaciones realizadas
- Aprobaciones obtenidas

**Endpoints Relacionados:**
- `POST /api/v1/compliance/technical-docs/prompts/{promptId}` - Documentar prompt
- `GET /api/v1/compliance/technical-docs/projects/{projectId}/prompts` - Prompts documentados

**Documentación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_TECHNICAL_DOCS.md`

---

#### 8. **Conformity Assessment (Art. 43 + Anexo VI)**

**Módulo:** `governance/compliance/conformity`

**Integración:**
- Los prompts deben incluirse en la **ConformityDeclaration** del proyecto
- Evaluación de conformidad antes de aprobación
- Certificación de cumplimiento

**Endpoints Relacionados:**
- `GET /api/v1/compliance/conformity/projects/{projectId}/prompts` - Prompts en declaración
- `POST /api/v1/compliance/conformity/prompts/{promptId}/assess` - Evaluar conformidad

**Documentación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_CONFORMITY.md`

---

### Flujo Completo de Aprobación con Compliance

```
1. Crear Prompt
   └─> Asociar a Project (OBLIGATORIO)
       └─> Determinar nivel de riesgo (Classification)

2. Crear PromptVersion (1.0.0)
   └─> Estado: DRAFT

3. Ejecutar Validaciones
   ├─> SAFETY (prompt injection, jailbreak)
   ├─> COMPLIANCE (EU AI Act)
   ├─> PERFORMANCE (rendimiento)
   └─> BIAS (sesgos)

4. Validación QMS
   └─> Verificar cumplimiento módulos A-M

5. Aprobación HITL (si proyecto alto riesgo)
   ├─> Si B.1, B.2 → HITL OBLIGATORIO
   ├─> Si C, D → Auto-aprobación con validación
   └─> Si Prohibido → BLOQUEO AUTOMÁTICO

6. Documentación Técnica
   └─> Registrar en AIActTechnicalDocumentation

7. Conformity Assessment
   └─> Incluir en ConformityDeclaration

8. Activar Versión
   └─> Estado: ACTIVE

9. Post-Market Monitoring
   └─> Monitorear uso y rendimiento

10. Trazabilidad
    └─> Registrar en logs inmutables
```

---

### Campos de Entidad Prompt Relacionados con Compliance

```java
// Relación obligatoria con Project
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXPROJECT", nullable = false)
private Project project;

// Estado de aprobación (relacionado con HITL)
@Column(name = "PRMAPPROVALSTATUS")
private String prmapprovalstatus; // PENDING, UNDER_REVIEW, APPROVED, REJECTED

// Metadata de compliance (JSONB)
@Column(name = "PRMMETADATA", columnDefinition = "JSONB")
private String prmmetadata; // Incluye: riskLevel, qmsScore, complianceChecks

// Campos de auditoría (obligatorios para trazabilidad)
@Column(name = "PRMCREATEDBY")
private String prmcreatedby;

@Column(name = "PRMCREATEDAT")
private Timestamp prmcreatedat;

@Column(name = "PRMUPDATEDBY")
private String prmupdatedby;

@Column(name = "PRMUPDATEDAT")
private Timestamp prmupdatedat;
```

---

### Endpoints de Integración con Compliance

**HITL:**
- `POST /api/v1/prompts/{promptId}/approval` - Solicitar aprobación HITL
- `GET /api/v1/compliance/hitl/prompts/pending` - Aprobaciones pendientes

**QMS:**
- `GET /api/v1/compliance/qms/prompts/{promptId}/score` - Score QMS del prompt
- `POST /api/v1/compliance/qms/prompts/{promptId}/validate` - Validar contra QMS

**PMM:**
- `GET /api/v1/compliance/pmm/prompts/{promptId}/metrics` - Métricas PMM
- `POST /api/v1/compliance/pmm/prompts/{promptId}/incidents` - Reportar incidente

**Traceability:**
- `GET /api/v1/compliance/traceability/prompts/{promptId}` - Trazabilidad completa
- `GET /api/v1/compliance/traceability/prompts/{promptId}/evidence` - Exportar evidencias

**Technical Docs:**
- `POST /api/v1/compliance/technical-docs/prompts/{promptId}` - Documentar prompt

**Conformity:**
- `GET /api/v1/compliance/conformity/projects/{projectId}/prompts` - Prompts en declaración

---

## 📚 REFERENCIAS

### Archivos de Migración Originales

- `MIGRACION_PROMPTS_FORMS.md` - Prompt de migración para forms
- `MIGRACION_PROMPTS_OVERVIEWS.md` - Prompt de migración para overviews

### Documentación Funcional

- `docs/funcional/prompts/01_REORGANIZACION_PANTALLAS_PROMPTS.md` - Reorganización de pantallas

### Documentación de Compliance

- `docs/prompts/PROMPT_GOVERNANCE_ARCHITECTURE.md` - Arquitectura de gobernanza de prompts
- `docs/prompts/compliance/PROMPT_COMPLIANCE_HITL.md` - HITL Supervision
- `docs/prompts/compliance/PROMPT_COMPLIANCE_QMS.md` - Quality Management System
- `docs/prompts/compliance/PROMPT_COMPLIANCE_PMM.md` - Post-Market Monitoring
- `docs/prompts/compliance/PROMPT_COMPLIANCE_TRACEABILITY.md` - Traceability
- `docs/prompts/compliance/PROMPT_COMPLIANCE_CLASSIFICATION.md` - Classification
- `docs/prompts/compliance/PROMPT_COMPLIANCE_TECHNICAL_DOCS.md` - Technical Documentation
- `docs/prompts/compliance/PROMPT_COMPLIANCE_CONFORMITY.md` - Conformity Assessment

---

**Última actualización:** Diciembre 2025
**Estado:** ✅ Frontend implementado con mock data | ✅ Backend completamente implementado
**Pantallas migradas:** 6 de 6 (100%)
**Backend:** ✅ Completamente implementado (excepto lógica de performance que tiene TODO)
**Integración Compliance:** ⚠️ Pendiente de implementar en frontend (backend listo)
**Análisis y Evaluación:** ✅ Botones implementados en detalle y edición
