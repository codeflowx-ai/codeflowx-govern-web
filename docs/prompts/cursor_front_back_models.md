# PROMPT: MIGRACIÓN DE PANTALLAS DE MODELOS - MODEL MANAGEMENT

## 📋 DESCRIPCIÓN DEL PROMPT

Este prompt (`cursor_end_models.md`) realiza la **migración y organización de pantallas de modelos** desde el backup a Next.js. Organiza las pantallas entre módulos Governance y Catalog, crea el módulo Models, implementa gestión de registro de modelos y proveedores con funcionalidades completas.

---

## 🎯 PROPÓSITO

Migrar y organizar las pantallas de gestión de modelos desde el sistema legacy a Next.js con:
- Organización entre Governance (control, cumplimiento) y Catalog (registro, inventario)
- Módulo Models dedicado para registro y proveedores
- Estilos "Wow Factor" (inspirado en Star Trek)
- Mock data para funcionamiento inmediato
- Internacionalización completa (6 idiomas)
- Gestión de proveedores con credenciales y asociación a proyectos
- Versión semántica automática y UUID automático para modelos
- Gestión de costes y consumo de tokens

---

## 📦 PANTALLAS CREADAS

### MÓDULO GOVERNANCE (5 pantallas)

#### 1. **Aprobaciones de Modelos** (`governance/models/approval`)

**Ubicación:** `app/(app)/governance/models/approval/page.tsx`

**Funcionalidad:**
- ✅ **Listado de solicitudes de aprobación** con tabla paginada
- ✅ **Métricas principales:**
  - Total pendientes
  - Total aprobadas
  - Total rechazadas
- ✅ **Filtros avanzados:**
  - Búsqueda por nombre de modelo o owner
  - Filtro por nivel de riesgo (HIGH, MEDIUM, LOW)
  - Filtro por owner
- ✅ **Acciones:**
  - Aprobar modelo (con comentarios opcionales)
  - Rechazar modelo (con razón obligatoria)
  - Ver detalles
- ✅ **Modal de aprobación y rechazo**
- ✅ **Validación:** razón obligatoria para rechazo

**Mock Data:**
- 3 solicitudes de aprobación de ejemplo
- Métricas calculadas automáticamente

---

#### 2. **Análisis de Sesgo** (`governance/models/bias-analysis`)

**Ubicación:** `app/(app)/governance/models/bias-analysis/page.tsx`

**Funcionalidad:**
- ✅ **Listado de análisis de sesgo** de modelos
- ✅ **Métricas principales:**
  - Total de análisis
  - Análisis con sesgo detectado
  - Análisis sin sesgo
  - Score promedio de sesgo
- ✅ **Filtros:**
  - Búsqueda por nombre de modelo
  - Filtro por tipo de modelo
  - Filtro por nivel de sesgo
- ✅ **Tabla con información de sesgo detectado**

---

#### 3. **Explicabilidad** (`governance/models/explainability`)

**Ubicación:** `app/(app)/governance/models/explainability/page.tsx`

**Funcionalidad:**
- ✅ **Listado de análisis de explicabilidad**
- ✅ **Métricas principales:**
  - Total de análisis
  - Modelos explicables
  - Modelos no explicables
  - Score promedio
- ✅ **Filtros:**
  - Búsqueda por nombre
  - Filtro por tipo
  - Filtro por score de explicabilidad
- ✅ **Visualización de scores y explicabilidad**

---

#### 4. **Rendimiento** (`governance/models/performance`)

**Ubicación:** `app/(app)/governance/models/performance/page.tsx`

**Funcionalidad:**
- ✅ **Listado de métricas de rendimiento**
- ✅ **Métricas principales:**
  - Total de modelos monitoreados
  - Modelos en objetivo
  - Modelos bajo rendimiento
  - Score promedio
- ✅ **Filtros:**
  - Búsqueda por nombre
  - Filtro por tipo
  - Filtro por estado de rendimiento
- ✅ **Tabla con métricas de rendimiento**

---

#### 5. **Uso de Modelos** (`governance/models/performance/usage-overview`)

**Ubicación:** `app/(app)/governance/models/performance/usage-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de uso de modelos**
- ✅ **Campos:** ID, Request ID, User ID, Project ID, Agent ID, Prompt ID
- ✅ **Filtros:**
  - Por tipo
  - Por request ID
- ✅ **Acciones:** Ver, Eliminar

---

### MÓDULO CATALOG (8 pantallas)

#### 6. **Registro de Modelos** (`catalog/registry/catalog-overview`)

**Ubicación:** `app/(app)/catalog/registry/catalog-overview/page.tsx`

**Funcionalidad:**
- ✅ **Catálogo de modelos** con tabla paginada
- ✅ **Métricas principales:**
  - Total de modelos
  - Modelos activos
  - Modelos inactivos
  - Modelos pendientes
- ✅ **Filtros:**
  - Búsqueda por nombre
  - Filtro por tipo
  - Filtro por estado
- ✅ **Acciones:** Ver detalles, Editar, Eliminar

---

#### 7. **Artefactos** (`catalog/registry/artifact-overview`)

**Ubicación:** `app/(app)/catalog/registry/artifact-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de artefactos de modelos**
- ✅ **Métricas:** Total, activos, pendientes, inactivos
- ✅ **Filtros:** por nombre, tipo, estado
- ✅ **Tabla con información de artefactos**

---

#### 8. **Capacidades** (`catalog/registry/capability-overview`)

**Ubicación:** `app/(app)/catalog/registry/capability-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de capacidades de modelos**
- ✅ **Métricas:** Total, activas, pendientes, inactivas
- ✅ **Filtros:** por nombre, tipo, estado
- ✅ **Tabla con capacidades disponibles**

---

#### 9. **Endpoints** (`catalog/registry/endpoint-overview`)

**Ubicación:** `app/(app)/catalog/registry/endpoint-overview/page.tsx`

**Funcionalidad:**
- ✅ **Catálogo de endpoints** de modelos
- ✅ **Métricas:** Total, activos, inactivos
- ✅ **Filtros:** por nombre, tipo de endpoint
- ✅ **Tabla con endpoints disponibles**

---

#### 10. **Credenciales de Proveedor** (`catalog/registry/provider-credential-overview`)

**Ubicación:** `app/(app)/catalog/registry/provider-credential-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de credenciales de proveedores**
- ✅ **Métricas:** Total, activas, expiradas
- ✅ **Filtros:** por nombre, tipo, estado
- ✅ **Tabla con credenciales**

---

#### 11. **Proveedores** (`catalog/registry/provider-overview`)

**Ubicación:** `app/(app)/catalog/registry/provider-overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de proveedores**
- ✅ **Métricas:** Total, activos, pendientes, inactivos
- ✅ **Filtros:** por nombre, tipo, estado
- ✅ **Tabla con proveedores**

---

#### 12. **Versionado** (`catalog/versioning`)

**Ubicación:** `app/(app)/catalog/versioning/page.tsx`

**Funcionalidad:**
- ✅ **Gestión de versiones de modelos**
- ✅ **Métricas:** Total de versiones, versiones activas
- ✅ **Filtros:** por nombre de modelo, versión
- ✅ **Tabla con versiones**

---

### MÓDULO MODELS (2 pantallas principales)

#### 13. **Registro de Modelos** (`models/registry`)

**Ubicación:** `app/(app)/models/registry/page.tsx`

**Funcionalidad:**
- ✅ **Listado de modelos** con tabla paginada
- ✅ **Métricas principales:**
  - Total de modelos
  - Modelos activos
  - Modelos inactivos
  - Modelos pendientes
- ✅ **Filtros avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo
  - Filtro por estado
- ✅ **Acciones:**
  - Crear nuevo modelo (botón "Agregar Modelo")
  - Ver detalles
  - Editar modelo
  - Eliminar modelo
- ✅ **Modal de creación** con formulario completo
- ✅ **Navegación a página de detalle**

---

#### 14. **Detalle/Edición de Modelo** (`models/registry/[id]`)

**Ubicación:** `app/(app)/models/registry/[id]/page.tsx`

**Funcionalidad:**
- ✅ **Página completa** (no modal) con 5 tabs:
  - **Información General:**
    - Formulario editable para crear/editar modelo
    - Campos: nombre, displayName, descripción, tipo, framework, estado, etapa actual
    - **ID del modelo (UUID automático):** campo de solo lectura, se genera automáticamente con `crypto.randomUUID()`
    - **Versión semántica automática:** campo de solo lectura, se calcula automáticamente al crear nueva versión
    - **Proveedor:** campo select (se movió desde pestaña separada)
  - **Versiones:**
    - Gestión de versiones (crear, editar, eliminar)
    - Formulario inline para crear nuevas versiones
    - Versión semántica automática al crear nueva versión
    - Función `calculateNextVersion()` que incrementa patch/minor/major según corresponda
  - **Proyectos:**
    - Tabla con proyectos que usan el modelo
    - Información: nombre, usos, último uso
    - Modal para ver detalles del proyecto
  - **Costes y Consumo:**
    - Métricas: Total Tokens, Coste Total, Coste Mensual, Coste por Token
    - Consumo por versión: desglose de tokens y costes por cada versión
    - Gráfico de tendencias (placeholder para implementación futura)
  - **Métricas:**
    - KPIs: Total Requests, Tasa de Éxito, Latencia Promedio, Precisión
    - Gráfico de tendencias (placeholder)
- ✅ **Modo edición:** botones "Editar" y "Guardar" para alternar entre vista y edición
- ✅ **Parámetro URL:** `?edit=true` para abrir directamente en modo edición
- ✅ **Navegación:** botón "Volver" para regresar al listado

**Características especiales:**
- UUID automático al crear nuevo modelo
- Versión semántica automática (no manual)
- Proveedor como campo select en Información General (no pestaña separada)
- Costes y consumo de tokens por versión

---

#### 15. **Gestión de Proveedores** (`models/providers`)

**Ubicación:** `app/(app)/models/providers/page.tsx`

**Funcionalidad:**
- ✅ **Listado de proveedores** con tabla paginada
- ✅ **Métricas principales:**
  - Total de proveedores
  - Proveedores activos
  - Proveedores pendientes
  - Proveedores inactivos
- ✅ **Filtros avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo (EXTERNAL, INTERNAL)
  - Filtro por estado (ACTIVE, INACTIVE, PENDING)
- ✅ **Acciones:**
  - Crear nuevo proveedor (botón "Agregar Proveedor")
  - Ver detalles (navega a página de detalle)
  - Eliminar proveedor
- ✅ **Navegación:** uso de `window.location` (no router) en botones de acción

---

#### 16. **Detalle/Edición de Proveedor** (`models/providers/[id]`)

**Ubicación:** `app/(app)/models/providers/[id]/page.tsx`

**Funcionalidad:**
- ✅ **Página completa** (no modal) con 5 tabs:
  - **Información General:**
    - Formulario editable para crear/editar proveedor
    - Campos: nombre, displayName, descripción, tipo (EXTERNAL/INTERNAL), estado (ACTIVE/INACTIVE/PENDING), tipo de autenticación, URL base
    - Modo edición con botones "Editar" y "Guardar"
  - **Credenciales:**
    - Gestión completa de credenciales (crear, ver, eliminar)
    - Formulario inline para crear nuevas credenciales
    - Campos: nombre, tipo, ambiente, estado, proyecto (asociación opcional)
    - Tabla con: nombre, tipo, ambiente, estado, proyecto asociado, expiración, último uso, contador de uso
  - **Modelos Asociados:**
    - Tabla con modelos vinculados al proveedor
    - Información: nombre, tipo, versión, estado
  - **Proyectos Asociados:**
    - Tabla con proyectos que usan credenciales del proveedor
    - Información: nombre, credenciales usadas, último uso
  - **Costes y KPIs:**
    - KPIs: Total Requests, Tasa de Éxito, Latencia Promedio, Uptime, Credenciales Activas/Expiradas
    - Costes: Total Mensual/Diario, desglose por credencial
    - Gráfico de tendencias (placeholder)
- ✅ **Navegación:** botón "Volver" para regresar al listado
- ✅ **Página de creación:** usa la misma página con `id="new"`

**Estructura según entidades JPA:**
- **ModelProvider:** nombre, displayName, descripción, tipo (EXTERNAL/INTERNAL), estado (ACTIVE/INACTIVE), tipo de autenticación, URL base
- **ProviderCredential:** nombre, tipo, valor encriptado, estado, ambiente, expiración, último uso, contador de uso, **asociación opcional a Project**

---

## 🏗️ ARQUITECTURA

### Estructura de Archivos Creados

```
app/(app)/
├── governance/
│   └── models/
│       ├── approval/
│       │   └── page.tsx                    # Aprobaciones
│       ├── bias-analysis/
│       │   └── page.tsx                    # Análisis de sesgo
│       ├── explainability/
│       │   └── page.tsx                    # Explicabilidad
│       ├── performance/
│       │   ├── page.tsx                    # Rendimiento
│       │   └── usage-overview/
│       │       └── page.tsx                # Uso de modelos
│
├── catalog/
│   ├── registry/
│   │   ├── artifact-overview/
│   │   │   └── page.tsx                    # Artefactos
│   │   ├── capability-overview/
│   │   │   └── page.tsx                    # Capacidades
│   │   ├── catalog-overview/
│   │   │   └── page.tsx                    # Catálogo
│   │   ├── endpoint-overview/
│   │   │   └── page.tsx                    # Endpoints
│   │   ├── provider-credential-overview/
│   │   │   └── page.tsx                    # Credenciales
│   │   └── provider-overview/
│   │       └── page.tsx                    # Proveedores
│   └── versioning/
│       └── page.tsx                        # Versionado
│
└── models/
    ├── registry/
    │   ├── page.tsx                        # Listado de modelos
    │   └── [id]/
    │       └── page.tsx                    # Detalle/edición de modelo
    └── providers/
        ├── page.tsx                        # Listado de proveedores
        └── [id]/
            └── page.tsx                    # Detalle/edición de proveedor
```

### Módulo Models en Menú

**Ubicación:** `app/config/modules.ts`

Módulo "Models" agregado al sidebar con:
- Registry (Registro de Modelos)
- Providers (Proveedores)

### Componentes UI Utilizados

- `@/components/ui/card` - Cards con glassmorphism
- `@/components/ui/badge` - Badges con colores temáticos
- `@/components/ui/button` - Botones con estilos modernos
- `@/components/ui/input` - Inputs con estilos
- `@/components/ui/textarea` - Textareas
- `@/components/ui/select` - Selects
- `@/components/ui/tabs` - Tabs para organización de contenido
- `@/components/ui/SimpleModal` - Modales reutilizables
- `lucide-react` - Iconos modernos

### Estilos "Wow Factor"

- ✅ Cards con bordes (sin gradientes excesivos)
- ✅ Títulos alineados a la izquierda (sin gradientes)
- ✅ Glassmorphism en cards
- ✅ Animaciones suaves
- ✅ Efectos hover en cards
- ✅ Badges con colores temáticos

---

## 🔌 INTEGRACIÓN CON BACKEND

### Estado Actual: **MOCK MODE**

Las pantallas funcionan con **mock data** para permitir desarrollo y testing sin backend.

### Desactivación de Mock (Futuro)

Cuando el backend esté disponible, cambiar en `.env.local`:
```bash
NEXT_PUBLIC_USE_MOCK=false
```

### Endpoints Backend Esperados

**Microservicio:** Model Management (puerto a definir)

**Endpoints principales:**
- `GET /api/v1/models` - Listar modelos
- `GET /api/v1/models/{id}` - Obtener modelo
- `POST /api/v1/models` - Crear modelo
- `PUT /api/v1/models/{id}` - Actualizar modelo
- `DELETE /api/v1/models/{id}` - Eliminar modelo
- `GET /api/v1/models/{id}/versions` - Listar versiones
- `POST /api/v1/models/{id}/versions` - Crear nueva versión
- `GET /api/v1/models/{id}/costs` - Obtener costes y consumo
- `GET /api/v1/models/{id}/metrics` - Obtener métricas
- `GET /api/v1/providers` - Listar proveedores
- `GET /api/v1/providers/{id}` - Obtener proveedor
- `POST /api/v1/providers` - Crear proveedor
- `PUT /api/v1/providers/{id}` - Actualizar proveedor
- `DELETE /api/v1/providers/{id}` - Eliminar proveedor
- `GET /api/v1/providers/{id}/credentials` - Listar credenciales
- `POST /api/v1/providers/{id}/credentials` - Crear credencial
- `DELETE /api/v1/providers/{id}/credentials/{credId}` - Eliminar credencial

### Entidades JPA Relacionadas

- **Model** - Entidad principal de modelos
- **ModelVersion** - Versiones de modelos
- **ModelProvider** - Proveedores de modelos
- **ProviderCredential** - Credenciales de proveedores (con asociación opcional a Project)

---

## 📊 DATOS Y MODELOS

### Interfaces TypeScript

**Model (Registro):**
```typescript
{
  id: number;
  modelId: string;           // UUID (generado automáticamente)
  name: string;
  displayName: string;
  version: string;           // Versión semántica (automática)
  type: string;
  framework: string;
  status: string;
  currentStage: string;
  description: string;
  provider?: Provider;
}
```

**Model (Detalle):**
```typescript
{
  id: number;
  modelId: string;           // UUID (solo lectura, automático)
  name: string;
  displayName: string;
  version: string;           // Versión semántica (solo lectura, automática)
  type: string;
  framework: string;
  status: string;
  currentStage: string;
  description: string;
  provider?: Provider;
  versions?: ModelVersion[];
  projects?: Project[];
  costs?: {
    totalTokens: number;
    totalCost: number;
    monthlyCost: number;
    costPerToken: number;
    consumptionByVersion: Array<{
      version: string;
      tokens: number;
      cost: number;
    }>;
  };
  metrics?: {
    totalRequests: number;
    successRate: number;
    averageLatency: number;
    accuracy: number;
  };
}
```

**Provider:**
```typescript
{
  id: number;
  name: string;
  displayName: string;
  description: string;
  providerType: string;      // EXTERNAL | INTERNAL
  status: string;            // ACTIVE | INACTIVE | PENDING
  authType: string;
  baseUrl: string;
  credentials?: ProviderCredential[];
  models?: Model[];
  projects?: Project[];
}
```

**ProviderCredential:**
```typescript
{
  id: number;
  name: string;
  type: string;
  encryptedValue: string;
  environment: string;
  status: string;
  projectId?: number;        // Asociación opcional a proyecto
  projectName?: string;
  expiresAt?: string;
  lastUsedAt?: string;
  usageCount: number;
}
```

**ModelVersion:**
```typescript
{
  id: number;
  version: string;           // Versión semántica (automática)
  status: string;
  createdAt: string;
  description?: string;
}
```

---

## 🌐 INTERNACIONALIZACIÓN

### Traducciones Incluidas

**6 idiomas soportados:** Español (es), Inglés (en), Francés (fr), Alemán (de), Italiano (it), Portugués (pt)

**Ubicación:** `app/config/i18n/modules/governance/models/`

**Archivos de traducciones:**
- `models-approval.ts` - Traducciones de aprobaciones
- `models-bias-analysis.ts` - Traducciones de análisis de sesgo
- `models-explainability.ts` - Traducciones de explicabilidad
- `models-performance.ts` - Traducciones de rendimiento
- `models-usage-overview.ts` - Traducciones de uso
- `models-registry.ts` - Traducciones de registro
- `models-providers.ts` - Traducciones de proveedores

**Estructura de claves:**
```typescript
governance.models.{pantalla}.{seccion}.{campo}
```

**Ejemplo:**
```typescript
t("governance.models.registry.title", "Registro de Modelos")
t("governance.models.registry.detail.general.modelId", "ID del Modelo")
t("governance.models.registry.detail.costs.totalTokens", "Total Tokens")
```

### Patrón de Uso

Todas las pantallas siguen el patrón de arquitectura front:
- Importar `useTranslation` desde `@/app/config/i18n`
- Usar `t()` con clave completa y valor por defecto
- Traducciones organizadas por módulo y pantalla

---

## 📍 NAVEGACIÓN Y MENÚ

### Rutas Disponibles

**Governance:**
- `/governance/models/approval` - Aprobaciones
- `/governance/models/bias-analysis` - Análisis de sesgo
- `/governance/models/explainability` - Explicabilidad
- `/governance/models/performance` - Rendimiento
- `/governance/models/performance/usage-overview` - Uso

**Catalog:**
- `/catalog/registry/catalog-overview` - Catálogo
- `/catalog/registry/artifact-overview` - Artefactos
- `/catalog/registry/capability-overview` - Capacidades
- `/catalog/registry/endpoint-overview` - Endpoints
- `/catalog/registry/provider-credential-overview` - Credenciales
- `/catalog/registry/provider-overview` - Proveedores
- `/catalog/versioning` - Versionado

**Models:**
- `/models/registry` - Listado de modelos
- `/models/registry/[id]` - Detalle/edición de modelo
- `/models/registry/new` - Crear nuevo modelo
- `/models/providers` - Listado de proveedores
- `/models/providers/[id]` - Detalle/edición de proveedor
- `/models/providers/new` - Crear nuevo proveedor

### Entrada en Menú

**Ubicación:** `app/config/modules.ts`

Módulo "Models" registrado en el menú con:
- Registry
- Providers

---

## ✅ ESTADO ACTUAL

### Pantallas Implementadas

**Governance (5 pantallas):**
- ✅ `app/(app)/governance/models/approval/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/models/bias-analysis/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/models/explainability/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/models/performance/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/models/performance/usage-overview/page.tsx` - **COMPLETADO**

**Catalog (8 pantallas):**
- ✅ `app/(app)/catalog/registry/catalog-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/catalog/registry/artifact-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/catalog/registry/capability-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/catalog/registry/endpoint-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/catalog/registry/provider-credential-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/catalog/registry/provider-overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/catalog/versioning/page.tsx` - **COMPLETADO**

**Models (2 pantallas principales):**
- ✅ `app/(app)/models/registry/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/models/registry/[id]/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/models/providers/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/models/providers/[id]/page.tsx` - **COMPLETADO**

### Traducciones Implementadas

- ✅ `app/config/i18n/modules/governance/models/models-approval.ts` - **COMPLETADO**
- ✅ `app/config/i18n/modules/governance/models/models-bias-analysis.ts` - **COMPLETADO**
- ✅ `app/config/i18n/modules/governance/models/models-explainability.ts` - **COMPLETADO**
- ✅ `app/config/i18n/modules/governance/models/models-performance.ts` - **COMPLETADO**
- ✅ `app/config/i18n/modules/governance/models/models-usage-overview.ts` - **COMPLETADO**
- ✅ `app/config/i18n/modules/governance/models/models-registry.ts` - **COMPLETADO**
- ✅ `app/config/i18n/modules/governance/models/models-providers.ts` - **COMPLETADO**

### Estado en Backup

- ✅ **Pantallas originales están en el backup** (`backup/app-app/model-management/`)
- ✅ **Pantallas migradas y organizadas** según estructura acordada

---

## 🔄 FLUJO DE USUARIO

### Flujo de Registro de Modelos

1. **Usuario accede a `/models/registry`**
   - Ve listado de modelos con métricas
   - Puede filtrar y buscar
   - Puede crear nuevo modelo (botón "Agregar Modelo")

2. **Usuario crea nuevo modelo**
   - Completa formulario (nombre, displayName, descripción, tipo, framework, estado, etapa)
   - ID (UUID) y versión se generan automáticamente
   - Guarda y navega a página de detalle

3. **Usuario accede a detalle de modelo (`/models/registry/[id]`)**
   - Ve información completa en tabs
   - Puede editar información general
   - Puede gestionar versiones (crear, editar, eliminar)
   - Puede ver proyectos asociados
   - Puede ver costes y consumo
   - Puede ver métricas

4. **Usuario gestiona versiones**
   - Al crear nueva versión, se calcula automáticamente (semántica)
   - Se muestra la siguiente versión calculada

### Flujo de Gestión de Proveedores

1. **Usuario accede a `/models/providers`**
   - Ve listado de proveedores con métricas
   - Puede filtrar por tipo (EXTERNAL/INTERNAL) y estado
   - Puede crear nuevo proveedor

2. **Usuario crea/edita proveedor**
   - Completa información general (nombre, tipo, estado, URL base, tipo de autenticación)
   - Guarda y navega a página de detalle

3. **Usuario gestiona credenciales**
   - En pestaña "Credenciales" puede crear nuevas credenciales
   - Puede asociar credencial a proyecto (opcional)
   - Puede ver, eliminar credenciales

4. **Usuario ve información adicional**
   - Modelos asociados al proveedor
   - Proyectos que usan credenciales del proveedor
   - Costes y KPIs del proveedor

---

## 📝 NOTAS IMPORTANTES

### Características Especiales

1. **UUID Automático:** El ID del modelo se genera automáticamente con `crypto.randomUUID()`. El campo es de solo lectura.

2. **Versión Semántica Automática:** La versión del modelo sigue semántica (MAJOR.MINOR.PATCH) y se calcula automáticamente al crear nuevas versiones. Función `calculateNextVersion()` implementada.

3. **Proveedor como Campo Select:** El proveedor está en "Información General" como campo select, no como pestaña separada.

4. **Costes y Consumo:** Pestaña dedicada para métricas de tokens y costes, con desglose por versión.

5. **Navegación con `window.location`:** Los botones de acción usan `window.location` en lugar de `router` para navegación.

6. **Asociación Opcional a Proyectos:** Las credenciales pueden estar asociadas opcionalmente a un proyecto.

7. **Estructura según JPA:** La estructura de proveedores y credenciales sigue las entidades JPA `ModelProvider` y `ProviderCredential`.

### Mock Data

Todas las pantallas funcionan con datos mock para desarrollo:
- Modelos de ejemplo
- Proveedores de ejemplo
- Credenciales de ejemplo
- Métricas y costes simulados

### Estilos

- Sin gradientes excesivos en títulos
- Títulos alineados a la izquierda
- Cards con bordes (no gradientes)
- Estilo "Wow Factor" más sutil

### Validaciones

- Formularios incluyen validaciones básicas
- Razón obligatoria para rechazo de modelos
- Campos obligatorios marcados
- Validación de formato para versiones semánticas

---

## 🚀 PRÓXIMOS PASOS

1. **Integración Backend:** Conectar con microservicio de Model Management
2. **Validaciones Avanzadas:** Agregar validaciones más robustas
3. **Gráficos:** Implementar gráficos de tendencias en métricas y costes
4. **Testing:** Crear tests unitarios y de integración
5. **Documentación:** Documentar APIs y flujos
6. **Optimizaciones:** Mejorar rendimiento y UX
7. **Asociación Proyectos-Credenciales:** Implementar backend para asociación opcional

---

**Última actualización:** Diciembre 2025
**Estado:** ✅ Implementado y funcional con mock data
