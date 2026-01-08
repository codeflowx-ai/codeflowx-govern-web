# PROMPT: MIGRACIÓN DE PANTALLAS RAG - RAG MANAGEMENT

## 📋 DESCRIPCIÓN DEL PROMPT

Este prompt (`cursor_front_back_rag_structured.md`) realiza la **migración y organización de pantallas RAG** desde el backup a Next.js. Crea el módulo RAG principal, implementa gestión completa de proyectos RAG, modelos, fuentes de datos, búsqueda semántica, monitoreo y versionado con funcionalidades completas.

---

## 🎯 PROPÓSITO

Migrar y organizar las pantallas de gestión RAG desde el sistema legacy a Next.js con:
- **EJECUCIÓN Y CREACIÓN**: Módulo RAG operativo para crear, gestionar y usar sistemas RAG
- **GOBIERNO**: Módulo de gobierno para monitoreo, aprobaciones y cumplimiento de sistemas RAG
- Gestión completa de proyectos RAG (creación y ejecución)
- Gestión de modelos (Embedding, Reranker, LLM)
- Fuentes de datos (Data Sources)
- Búsqueda semántica avanzada (uso operativo)
- Monitoreo y health dashboard (gobierno)
- Versionado de recursos RAG
- Estilos "Wow Factor" (glassmorphism, animaciones suaves)
- Mock data para funcionamiento inmediato
- Internacionalización completa (6 idiomas)

---

## 📦 PANTALLAS CREADAS

### CLASIFICACIÓN: EJECUCIÓN vs GOBIERNO

**EJECUCIÓN Y CREACIÓN** (Módulo `/rag` - Uso operativo):
- Dashboard principal
- Proyectos RAG (crear, editar, gestionar)
- Modelos RAG (configurar y asignar)
- Búsqueda semántica (uso práctico)
- Chat RAG (uso práctico)
- Chunks (gestión operativa)
- Data Sources (configurar fuentes)
- Versioning (gestión de versiones)

**GOBIERNO** (Módulo `/governance/rag` - Control y monitoreo):
- Health Dashboard (monitoreo de salud)
- Analytics (análisis y métricas de gobierno)
- Aprobaciones (aprobación de proyectos RAG)
- Compliance (cumplimiento normativo)
- Performance monitoring (monitoreo de rendimiento)

### MÓDULO RAG - EJECUCIÓN Y CREACIÓN (14 pantallas)

#### 1. **Dashboard Principal** (`/rag`)

**Ubicación:** `app/(app)/rag/page.tsx`

**Funcionalidad:**
- ✅ **Dashboard principal** con métricas generales
- ✅ **Métricas principales:**
  - Total de proyectos
  - Proyectos activos
  - Total de chunks
  - Total de embeddings
  - Total de búsquedas
  - Tiempo promedio de respuesta
  - Tasa de éxito
  - Total de modelos
- ✅ **Vista de proyectos recientes** con cards
- ✅ **Actividad reciente** con timeline
- ✅ **Estadísticas de modelos** con uso y costos

---

#### 2. **Proyectos RAG** (`/rag/projects`)

**Ubicación:** `app/(app)/rag/projects/page.tsx`

**Funcionalidad:**
- ✅ **Listado de proyectos RAG** con tabla/grid paginado
- ✅ **Métricas principales:**
  - Total de proyectos
  - Proyectos activos
  - Proyectos pausados
  - Proyectos con error
- ✅ **Filtros avanzados:**
  - Búsqueda por nombre o descripción
  - Filtro por estado (active, paused, error)
- ✅ **Información por proyecto:**
  - Chunks y embeddings
  - Número de búsquedas
  - Modelos asignados (Embedding, Reranker, LLM)
  - Rendimiento (tiempo de respuesta, tasa de éxito, costo)
- ✅ **Acciones:**
  - Ver detalles (modal)
  - Editar proyecto
  - Pausar/Reanudar
  - Eliminar proyecto
- ✅ **Modal de detalles** con información completa del proyecto

---

#### 3. **Modelos RAG** (`/rag/models`)

**Ubicación:** `app/(app)/rag/models/page.tsx`

**Funcionalidad:**
- ✅ **Listado de modelos** con cards
- ✅ **Métricas principales:**
  - Total de modelos
  - Modelos activos
  - Modelos locales
  - Costo total
- ✅ **Filtros:**
  - Búsqueda por nombre o proveedor
  - Filtro por tipo (EMBEDDING, RERANKER, LLM)
  - Filtro por proveedor
- ✅ **Información por modelo:**
  - Tipo (Embedding, Reranker, LLM)
  - Proveedor y versión
  - Local o remoto
  - Costo por token
  - Tokens máximos
  - Métricas de rendimiento (precisión, velocidad, fiabilidad)
  - Estadísticas de uso (requests, costo total)
- ✅ **Acciones:** Ver, Editar

---

#### 4. **Búsqueda Semántica** (`/rag/search`)

**Ubicación:** `app/(app)/rag/search/page.tsx`

**Funcionalidad:**
- ✅ **Búsqueda semántica avanzada**
- ✅ **Selección de proyecto RAG** (sidebar)
- ✅ **Tipos de búsqueda:**
  - Vectorial
  - Textual
  - Híbrida
  - Filtrada
- ✅ **Panel de búsqueda:**
  - Query input (textarea)
  - Selector de tipo de búsqueda
  - Botones de acción (Buscar, Limpiar)
- ✅ **Resultados de búsqueda:**
  - Métricas: Total resultados, Score promedio, Tiempo de procesamiento, Tiempo de rerank
  - Resultados rankeados con:
    - Score de similitud
    - Score de rerank (si aplica)
    - Texto del chunk
    - Tipo de chunk
    - Fuente (metadata)
    - Tags automáticos y personalizados
- ✅ **Información del proyecto seleccionado:**
  - Detalles del proyecto
  - Modelos asignados (Embedding, Reranker)
  - Estadísticas de uso

---

#### 5. **Chat RAG** (`/rag/chat`)

**Ubicación:** `app/(app)/rag/chat/page.tsx`

**Funcionalidad:**
- ✅ **Interfaz de chat conversacional**
- ✅ **Integración con RAG para respuestas contextuales**
- ✅ **Historial de conversaciones**
- ✅ **Selección de proyecto RAG**

---

#### 6. **Gestión de Chunks** (`/rag/chunks`)

**Ubicación:** `app/(app)/rag/chunks/page.tsx`

**Funcionalidad:**
- ✅ **Listado de chunks** procesados
- ✅ **Información de chunks:**
  - Texto
  - Metadata
  - Embeddings asociados
  - Fuente
- ✅ **Filtros y búsqueda**

---

#### 7. **Health Dashboard** (`/governance/rag/monitoring/health-dashboard`) ⚠️ GOBIERNO

**Ubicación:** `app/(app)/governance/rag/monitoring/health-dashboard/page.tsx`

**Funcionalidad:**
- ✅ **Dashboard de salud de índices RAG** (MONITOREO - Gobierno)
- ✅ **Métricas principales:**
  - Total Items
  - Active Items
  - Deployed Items
  - Training Items
  - Offline Items
  - Score promedio
- ✅ **Tabla de métricas** con información detallada
- ✅ **Filtros:** Búsqueda por métricas
- ✅ **Acciones:** Ver detalles

**Nota:** Esta pantalla pertenece a GOBIERNO, no a ejecución. Debería estar en `/governance/rag/monitoring/health-dashboard` en lugar de `/rag/monitoring/health-dashboard`.

---

#### 8. **Data Sources Overview** (`/rag/data-sources/overview`)

**Ubicación:** `app/(app)/rag/data-sources/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de fuentes de datos**
- ✅ **Métricas principales:**
  - Total
  - Activos
  - Pendientes
  - Inactivos
- ✅ **Filtros:**
  - Búsqueda por nombre, descripción o ID
  - Filtro por tipo (ACTIVE, INACTIVE, PENDING)
  - Filtro por estado
  - Filtro por sync status
- ✅ **Información por fuente:**
  - ID
  - Nombre y descripción
  - Tipo
  - Configuración de conexión
  - Configuración de sincronización
- ✅ **Acciones:**
  - Registrar nueva fuente de datos
  - Editar
  - Eliminar

---

#### 9. **Versioning Overview** (`/rag/versioning/overview`)

**Ubicación:** `app/(app)/rag/versioning/overview/page.tsx`

**Funcionalidad:**
- ✅ **Gestión de versiones de recursos RAG**
- ✅ **Versionado de proyectos, modelos y fuentes de datos**
- ✅ **Historial de versiones**
- ✅ **Comparación de versiones**

---

#### 10-14. **Páginas Adicionales**

- **Analytics** (`/rag/analytics`) - Analíticas avanzadas
- **Database** (`/rag/database`) - Gestión de base de datos
- **API** (`/rag/api`) - Documentación y gestión de API
- **Reranker** (`/rag/reranker`) - Configuración de reranker
- **Community** (`/rag/community`) - Comunidad y recursos

---

## 🏗️ ARQUITECTURA

### Estructura de Archivos Creados

```
app/(app)/
└── rag/
    ├── page.tsx                                  # Dashboard principal
    ├── projects/
    │   └── page.tsx                              # Proyectos RAG
    ├── models/
    │   └── page.tsx                              # Modelos RAG
    ├── search/
    │   └── page.tsx                              # Búsqueda semántica
    ├── chat/
    │   └── page.tsx                              # Chat RAG
    ├── chunks/
    │   └── page.tsx                              # Gestión de chunks
    ├── monitoring/
    │   └── health-dashboard/
    │       └── page.tsx                          # Health Dashboard
    ├── data-sources/
    │   └── overview/
    │       └── page.tsx                          # Data Sources
    └── versioning/
        └── overview/
            └── page.tsx                          # Versionado
```

### Módulo RAG en Menú

**Ubicación:** `app/config/modules.ts`

Módulo "RAG" agregado al sidebar con:
- Dashboard (página principal)
- Projects (Proyectos RAG)
- Models (Modelos RAG)
- Data Sources (Fuentes de datos)
- Monitoring (Monitoreo)
- Versioning (Versionado)
- Search (Búsqueda)
- Chat (Chat RAG)
- Chunks (Gestión de chunks)
- Analytics (Analíticas)
- Database (Base de datos)
- API (API)
- Reranker (Reranker)
- Community (Comunidad)

**Configuración del módulo:**
- Nombre: "RAG"
- Icono: "Search"
- Roles: admin, project_manager, compliance_officer, governance_manager, developer, data_scientist, viewer
- Ruta por defecto: `/rag`
- Descripción: "Retrieval Augmented Generation - Gestión de sistemas RAG"

### Componentes UI Utilizados

- `@/components/ui/card` - Cards con glassmorphism
- `@/components/ui/badge` - Badges con colores temáticos
- `@/components/ui/button` - Botones con estilos modernos
- `@/components/ui/input` - Inputs con estilos
- `@/components/ui/textarea` - Textareas
- `@/components/ui/select` - Selects
- `@/components/ui/tabs` - Tabs para organización de contenido
- `@/components/ui/SimpleModal` - Modales reutilizables
- `@/components/ui/Progress` - Barras de progreso
- `@/components/ui/help-tooltip` - Tooltips de ayuda
- `lucide-react` - Iconos modernos

### Estilos "Wow Factor"

- ✅ Cards con bordes (sin gradientes excesivos)
- ✅ Títulos alineados a la izquierda (sin gradientes)
- ✅ Glassmorphism en cards (`backdrop-blur-md bg-background/60`)
- ✅ Animaciones suaves (`transition-all`, `hover:shadow-lg`)
- ✅ Efectos hover en cards
- ✅ Badges con colores temáticos
- ✅ Gradientes sutiles en botones principales
- ✅ Fondo con efectos de partículas animadas

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

**Microservicio:** RAG Service (puerto a definir)

**Endpoints principales:**
- `GET /api/v1/rag/projects` - Listar proyectos RAG
- `GET /api/v1/rag/projects/{id}` - Obtener proyecto
- `POST /api/v1/rag/projects` - Crear proyecto
- `PUT /api/v1/rag/projects/{id}` - Actualizar proyecto
- `DELETE /api/v1/rag/projects/{id}` - Eliminar proyecto
- `GET /api/v1/rag/models` - Listar modelos RAG
- `GET /api/v1/rag/models/{id}` - Obtener modelo
- `POST /api/v1/rag/data-sources` - Crear fuente de datos
- `GET /api/v1/rag/data-sources` - Listar fuentes de datos
- `POST /api/v1/rag/search` - Realizar búsqueda semántica
- `GET /api/v1/rag/monitoring/health` - Obtener métricas de salud
- `GET /api/v1/rag/versioning` - Listar versiones

---

## 📊 DATOS Y MODELOS

### Interfaces TypeScript

**RAG Project:**
```typescript
{
  id: number;
  name: string;
  description: string;
  status: "active" | "paused" | "error";
  owner: string;
  createdAt: string;
  updatedAt: string;
  chunks: number;
  embeddings: number;
  searches: number;
  assignments: {
    documents: number;
    webscraping: number;
    datasources: number;
  };
  models: {
    embedding: string;
    reranker?: string;
    llm: string;
  };
  performance: {
    avgResponseTime: number;
    successRate: number;
    totalCost: number;
  };
}
```

**RAG Model:**
```typescript
{
  id: number;
  modelName: string;
  modelType: "EMBEDDING" | "RERANKER" | "LLM";
  modelProvider: string;
  modelVersion: string;
  modelEndpoint: string;
  isLocal: boolean;
  isActive: boolean;
  performanceMetrics: {
    accuracy: number;
    speed: number;
    reliability: number;
  };
  costPerToken: number;
  maxTokens: number;
  contextWindow: number;
  usage: {
    totalTokens: number;
    totalCost: number;
    requests: number;
    avgResponseTime: number;
  };
}
```

**DataSource:**
```typescript
{
  idxragdatasource: number;
  ragname: string;
  ragdescription: string;
  ragtype: "ACTIVE" | "INACTIVE" | "PENDING";
  ragconnectionconfig: string;
  ragsyncconfig: string;
}
```

**Search Result:**
```typescript
{
  id: string;
  searchType: "vector" | "textual" | "hybrid" | "filtered";
  query: string;
  results: Array<{
    id: string;
    chunkType: string;
    chunkText: string;
    similarityScore: number;
    rerankScore?: number;
    source: {
      type: string;
      name: string;
    };
    metadata: {
      keywords: string[];
      autoTags: string[];
      customTags: string[];
    };
  }>;
  metrics: {
    totalResults: number;
    averageScore: number;
    processingTime: number;
    rerankTime?: number;
  };
  createdAt: string;
}
```

---

## 🌐 INTERNACIONALIZACIÓN

### Traducciones Incluidas

**6 idiomas soportados:** Español (es), Inglés (en), Francés (fr), Alemán (de), Italiano (it), Portugués (pt)

**Ubicación:** `app/config/i18n/modules/rag/`

**Archivos de traducciones:**
- `rag-dashboard.ts` - Traducciones del dashboard
- `rag-projects.ts` - Traducciones de proyectos
- `rag-models.ts` - Traducciones de modelos
- `rag-search.ts` - Traducciones de búsqueda
- `rag-chat.ts` - Traducciones de chat
- `rag-chunks.ts` - Traducciones de chunks
- `rag-monitoring.ts` - Traducciones de monitoreo
- `rag-data-sources.ts` - Traducciones de fuentes de datos
- `rag-versioning.ts` - Traducciones de versionado

**Estructura de claves:**
```typescript
rag.{pantalla}.{seccion}.{campo}
```

**Ejemplo:**
```typescript
t("rag.projects.title", "Proyectos RAG")
t("rag.search.semanticSearch", "Búsqueda Semántica")
t("rag.models.totalModels", "Total Modelos")
```

### Patrón de Uso

Todas las pantallas siguen el patrón de arquitectura front:
- Importar `useTranslation` desde `@/app/config/i18n`
- Usar `t()` con clave completa y valor por defecto
- Traducciones organizadas por módulo y pantalla

---

## 📍 NAVEGACIÓN Y MENÚ

### Rutas Disponibles

**RAG - EJECUCIÓN Y CREACIÓN:**
- `/rag` - Dashboard principal (operativo)
- `/rag/projects` - Proyectos RAG (crear/gestionar)
- `/rag/models` - Modelos RAG (configurar)
- `/rag/search` - Búsqueda semántica (uso práctico)
- `/rag/chat` - Chat RAG (uso práctico)
- `/rag/chunks` - Gestión de chunks (operativo)
- `/rag/data-sources/overview` - Fuentes de datos (configurar)
- `/rag/versioning/overview` - Versionado (gestión)
- `/rag/database` - Base de datos
- `/rag/api` - API
- `/rag/reranker` - Reranker

**RAG - GOBIERNO:**
- `/governance/rag/monitoring/health-dashboard` - Health Dashboard (monitoreo)
- `/governance/rag/analytics` - Analíticas (gobierno)
- `/governance/rag/approval` - Aprobaciones (gobierno)
- `/governance/rag/compliance` - Cumplimiento (gobierno)
- `/governance/rag/performance` - Monitoreo de rendimiento (gobierno)

### Entrada en Menú

**Ubicación:** `app/config/modules.ts`

Módulo "RAG" registrado en el menú principal con:
- Configuración en `modulesConfig`
- Menú completo en `getMenuByModule("RAG")`
- Detección de ruta en `getModuleByPath()` añadiendo `rag: "RAG"` al `moduleMap`

---

## ✅ ESTADO ACTUAL

### Pantallas Implementadas

**RAG - EJECUCIÓN Y CREACIÓN (`/rag`):**
- ✅ `app/(app)/rag/page.tsx` - **COMPLETADO** (Dashboard operativo)
- ✅ `app/(app)/rag/projects/page.tsx` - **COMPLETADO** (Crear/gestionar proyectos)
- ✅ `app/(app)/rag/models/page.tsx` - **COMPLETADO** (Configurar modelos)
- ✅ `app/(app)/rag/search/page.tsx` - **COMPLETADO** (Búsqueda semántica - uso)
- ✅ `app/(app)/rag/data-sources/overview/page.tsx` - **COMPLETADO** (Configurar fuentes)
- ✅ `app/(app)/rag/chat/page.tsx` - **PENDIENTE/PLACEHOLDER** (Chat operativo)
- ✅ `app/(app)/rag/chunks/page.tsx` - **PENDIENTE/PLACEHOLDER** (Gestión de chunks)
- ✅ `app/(app)/rag/versioning/overview/page.tsx` - **PENDIENTE/PLACEHOLDER** (Gestión de versiones)

**RAG - GOBIERNO (`/governance/rag` - Propuesto):**
- ⚠️ `app/(app)/governance/rag/monitoring/health-dashboard/page.tsx` - **MOVER AQUÍ** (Monitoreo)
- ⚠️ `app/(app)/governance/rag/analytics/page.tsx` - **CREAR** (Analíticas de gobierno)
- ⚠️ `app/(app)/governance/rag/approval/page.tsx` - **CREAR** (Aprobaciones)
- ⚠️ `app/(app)/governance/rag/compliance/page.tsx` - **CREAR** (Cumplimiento)

**Nota:** Actualmente el Health Dashboard está en `/rag/monitoring/health-dashboard` pero debería moverse a `/governance/rag/monitoring/health-dashboard` para separar ejecución de gobierno.

### Traducciones Implementadas

- ✅ Traducciones base para las pantallas principales
- ⚠️ Pendiente completar todas las traducciones según se implementen las páginas

### Estado en Backup

- ✅ **Pantallas originales están en el backup** (`backup/app-app/rag/`)
- ✅ **Pantallas migradas y organizadas** según estructura acordada
- ✅ **Módulo RAG añadido al menú principal**

---

## 🔄 FLUJO DE USUARIO

### Flujo de Gestión de Proyectos RAG

1. **Usuario accede a `/rag`**
   - Ve dashboard principal con métricas generales
   - Puede ver proyectos recientes y actividad
   - Puede navegar a diferentes secciones

2. **Usuario accede a `/rag/projects`**
   - Ve listado de proyectos con filtros
   - Puede crear nuevo proyecto
   - Puede ver detalles, editar, pausar/reanudar, eliminar

3. **Usuario gestiona modelos en `/rag/models`**
   - Ve modelos disponibles (Embedding, Reranker, LLM)
   - Puede filtrar por tipo y proveedor
   - Puede ver métricas de rendimiento y costos

4. **Usuario realiza búsqueda en `/rag/search`**
   - Selecciona proyecto RAG
   - Escribe consulta
   - Selecciona tipo de búsqueda (vectorial, textual, híbrida, filtrada)
   - Ve resultados rankeados con scores y metadata

5. **Usuario monitorea salud en `/rag/monitoring/health-dashboard`**
   - Ve métricas de salud de índices RAG
   - Puede filtrar y buscar
   - Ve detalles de cada métrica

---

## 📝 NOTAS IMPORTANTES

### Características Especiales

1. **Separación Ejecución vs Gobierno:**
   - **`/rag`**: Módulo de EJECUCIÓN Y CREACIÓN para uso operativo (crear proyectos, usar búsqueda, configurar modelos)
   - **`/governance/rag`**: Módulo de GOBIERNO para control, monitoreo y cumplimiento (health dashboard, aprobaciones, analytics)

2. **Módulo Principal RAG:** El módulo RAG ejecutivo (`/rag`) es un módulo principal con su propio menú en el sidebar, no está bajo Governance.
3. **Módulo Gobierno RAG:** El módulo de gobierno RAG (`/governance/rag`) debe estar bajo Governance para monitoreo y control.

2. **Búsqueda Semántica Avanzada:** La página de búsqueda soporta múltiples tipos (vectorial, textual, híbrida, filtrada) con reranking opcional.

3. **Gestión de Modelos:** Se gestionan tres tipos de modelos: Embedding, Reranker y LLM, cada uno con sus propias métricas y costos.

4. **Data Sources:** Las fuentes de datos pueden ser de diferentes tipos (PostgreSQL, MongoDB, Elasticsearch, S3, etc.) con configuración de conexión y sincronización.

5. **Mock Data Completo:** Todas las pantallas tienen mock data detallado para funcionamiento inmediato.

6. **Estilos Consistentes:** Todas las pantallas usan el mismo estilo "Wow Factor" con glassmorphism y animaciones suaves.

7. **Navegación con `window.location`:** Los botones de acción usan `window.location` para navegación.

---

## 🚀 PRÓXIMOS PASOS

1. **Integración Backend:** Conectar con microservicio RAG
2. **Implementar Páginas Pendientes:** Completar chat, chunks, versioning y otras páginas
3. **Validaciones Avanzadas:** Agregar validaciones más robustas
4. **Testing:** Crear tests unitarios y de integración
5. **Documentación:** Documentar APIs y flujos
6. **Optimizaciones:** Mejorar rendimiento y UX
7. **Gráficos y Visualizaciones:** Implementar gráficos de tendencias y visualizaciones avanzadas

---

**Última actualización:** Diciembre 2025
**Estado:** ✅ Implementado parcialmente con mock data - Módulo principal completo, algunas páginas pendientes
