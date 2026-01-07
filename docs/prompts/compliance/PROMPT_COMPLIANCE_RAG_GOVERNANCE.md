# 🔍 PROMPT DE IMPLEMENTACIÓN - RAG GOVERNANCE (Art. 6, 27, 49 EU AI Act)

**Módulo:** Compliance - RAG Governance
**Artículos EU AI Act:** Art. 6 (Clasificación), Art. 27 (FRIA), Art. 49 (Registro EU), Art. 72 (PMM)
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación
**Esfuerzo Estimado:** 4-5 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el módulo de gobierno y cumplimiento normativo para sistemas RAG según el EU AI Act. RAG debe tratarse como otro sistema de IA más a gobernar, igual que modelos, agentes, prompts y datasets. El módulo permite registrar proyectos RAG, clasificarlos según riesgo, realizar evaluaciones FRIA, monitorear su cumplimiento y gestionar su ciclo de vida desde la perspectiva normativa.

### **Contexto**
Los sistemas RAG (Retrieval Augmented Generation) son sistemas de IA que combinan modelos de lenguaje con bases de conocimiento. Como tales, están sujetos al EU AI Act y deben:
- Ser clasificados según riesgo (Art. 6)
- Tener evaluación FRIA si son de alto riesgo (Art. 27)
- Ser registrados en el registro EU (Art. 49)
- Ser monitoreados post-mercado (Art. 72)
- Cumplir con requisitos de transparencia, trazabilidad y supervisión humana (Art. 12, 14, 19)

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/rag/registry/page.tsx` | ❌ No existe | Registro de proyectos RAG para gobierno | 🔴 Alta |
| 2 | `app/(app)/governance/rag/classification/page.tsx` | ❌ No existe | Clasificación de proyectos RAG (alto riesgo) | 🔴 Alta |
| 3 | `app/(app)/governance/rag/fria/page.tsx` | ❌ No existe | FRIA para proyectos RAG | 🔴 Alta |
| 4 | `app/(app)/governance/rag/monitoring/health-dashboard/page.tsx` | ⚠️ Existe en `/rag/` | Health Dashboard (monitoreo) | 🔴 Alta |
| 5 | `app/(app)/governance/rag/approval/page.tsx` | ❌ No existe | Aprobación de proyectos RAG | 🟡 Media |
| 6 | `app/(app)/governance/rag/performance/page.tsx` | ❌ No existe | Rendimiento y métricas de cumplimiento | 🟡 Media |
| 7 | `app/(app)/governance/rag/analytics/page.tsx` | ❌ No existe | Analíticas de gobierno RAG | 🟡 Media |
| 8 | `app/(app)/governance/rag/compliance/dashboard/page.tsx` | ❌ No existe | Dashboard de cumplimiento RAG | 🔴 Alta |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Relación con Entidades Existentes**

#### **Project (Proyecto RAG)**
- **Entidad:** `com.codeflowx.govern.entity.projects.Project`
- **Tabla:** `PRJPROJECTS` (prefijo `PRJ`)
- **Campos específicos para RAG:**
  - `PRJISTYPE` (String) - Tipo de proyecto: "RAG", "MODEL", "AGENT", "PROMPT", "DATASET"
  - `PRJRAGPROJECTID` (String) - ID del proyecto RAG en el sistema de ejecución
  - `PRJISHIGHRISK` (Boolean) - Clasificación de alto riesgo (Art. 6)
  - `PRJANNEXIIICATEGORY` (String) - Categoría Anexo III si es alto riesgo
  - `PRJCLASSIFICATIONJUSTIFICATION` (String) - Justificación de clasificación
  - `PRJCLASSIFICATIONDATE` (Timestamp) - Fecha de clasificación
  - `PRJCLASSIFIEDBY` (String) - Usuario que clasificó

#### **FriaAssessment (FRIA para RAG)**
- **Entidad:** `com.codeflowx.govern.entity.compliance.FriaAssessment`
- **Tabla:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS` (prefijo `FRA`)
- **Vinculación:** `IDXPROJECT` (FK a Project donde `PRJISTYPE = "RAG"`)
- **Reutiliza:** Misma entidad que FRIA de otros sistemas, filtrada por tipo de proyecto

#### **EU Registration (Registro EU para RAG)**
- **Entidad:** `com.codeflowx.govern.entity.compliance.EURegistration` (si existe)
- **Vinculación:** Proyectos RAG se registran igual que otros sistemas de IA

### **Servicios Existentes (Reutilizar)**

- **ProjectService** - Gestión de proyectos (incluye RAG)
- **HighRiskClassifierBusinessService** - Clasificación de alto riesgo (reutilizar para RAG)
- **FriaAssessmentBusinessService** - Evaluaciones FRIA (reutilizar para RAG)
- **EURegistrationService** - Registro EU (reutilizar para RAG)
- **PostMarketMonitoringService** - Monitoreo post-mercado (reutilizar para RAG)

### **Nuevos Servicios Necesarios**

- **RAGGovernanceBusinessService** (si no existe)
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/RAGGovernanceBusinessService.java`
  - **Métodos:**
    - `registerRAGProject(String ragProjectId, String name, String description, String owner)` - Registra proyecto RAG para gobierno
    - `syncRAGProjectFromExecution(String ragProjectId)` - Sincroniza datos desde sistema de ejecución
    - `getRAGProjectMetrics(Long projectId)` - Obtiene métricas de gobierno
    - `validateRAGCompliance(Long projectId)` - Valida cumplimiento normativo

---

## 📦 PANTALLAS DETALLADAS

### 1. **Registro de Proyectos RAG** (`/governance/rag/registry`)

**Objetivo:** Registrar y gestionar proyectos RAG desde la perspectiva de gobierno

**Funcionalidades:**
- ✅ **Listado de proyectos RAG registrados** para gobierno
- ✅ **Métricas principales:**
  - Total de proyectos RAG registrados
  - Proyectos con clasificación pendiente
  - Proyectos de alto riesgo
  - Proyectos con FRIA completado
  - Proyectos registrados en EU
- ✅ **Filtros:**
  - Búsqueda por nombre, ID, owner
  - Filtro por estado de clasificación
  - Filtro por estado de FRIA
  - Filtro por estado de registro EU
  - Filtro por nivel de riesgo
- ✅ **Información por proyecto:**
  - ID del proyecto (UUID)
  - Nombre y descripción
  - Owner y creador
  - Estado de clasificación (Pendiente, Clasificado, Alto Riesgo)
  - Estado de FRIA (No iniciado, En progreso, Completado)
  - Estado de registro EU (No registrado, Pendiente, Registrado)
  - Última sincronización con sistema de ejecución
  - Métricas de cumplimiento
- ✅ **Acciones:**
  - Registrar nuevo proyecto RAG (manual o sincronizar desde ejecución)
  - Ver detalles completos (modal o página)
  - Clasificar proyecto (navega a clasificación)
  - Iniciar FRIA (navega a FRIA)
  - Sincronizar con sistema de ejecución
  - Ver historial de cambios

**Mock Data:**
```typescript
interface RAGProjectRegistry {
  id: number;
  projectId: string; // UUID
  ragProjectId: string; // ID en sistema de ejecución
  name: string;
  description: string;
  owner: string;
  createdAt: string;
  updatedAt: string;
  classificationStatus: "PENDING" | "CLASSIFIED" | "HIGH_RISK";
  classificationDate?: string;
  classifiedBy?: string;
  friaStatus: "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED";
  friaId?: number;
  euRegistrationStatus: "NOT_REGISTERED" | "PENDING" | "REGISTERED";
  lastSyncAt: string;
  complianceScore: number; // 0-100
  metrics: {
    totalChunks: number;
    totalSearches: number;
    avgResponseTime: number;
    successRate: number;
  };
}
```

---

### 2. **Clasificación de Proyectos RAG** (`/governance/rag/classification`)

**Objetivo:** Clasificar proyectos RAG según Art. 6 y Anexo III del EU AI Act

**Funcionalidades:**
- ✅ **Reutiliza lógica de Classification existente** adaptada para RAG
- ✅ **Wizard de clasificación:**
  - Paso 1: Información del proyecto RAG
  - Paso 2: Selección de categoría Anexo III (si aplica)
  - Paso 3: Justificación (mínimo 100 caracteres, 2 keywords de riesgo)
  - Paso 4: Confirmación y guardado
- ✅ **Validaciones:**
  - Justificación mínima 100 caracteres
  - Mencionar categoría seleccionada
  - Al menos 2 palabras clave de riesgo
- ✅ **Integración:**
  - Actualiza `PRJISHIGHRISK` en Project
  - Actualiza `PRJANNEXIIICATEGORY` en Project
  - Dispara workflow BPMN si es alto riesgo
  - Notifica a stakeholders

**Reutiliza:** `HighRiskClassifierBusinessService.classifySystem()`

---

### 3. **FRIA para Proyectos RAG** (`/governance/rag/fria`)

**Objetivo:** Realizar evaluación FRIA para proyectos RAG de alto riesgo

**Funcionalidades:**
- ✅ **Reutiliza wizard FRIA existente** adaptado para RAG
- ✅ **6 pasos del Art. 27.1:**
  - a) Descripción de procesos RAG
  - b) Período y frecuencia de uso
  - c) Categorías de personas afectadas
  - d) Riesgos específicos (sesgo en embeddings, calidad de chunks, etc.)
  - e) Medidas de supervisión humana (HITL)
  - f) Medidas de mitigación
- ✅ **Riesgos específicos RAG:**
  - Sesgo en modelos de embedding
  - Calidad y veracidad de chunks
  - Protección de datos personales en chunks
  - Transparencia de fuentes de datos
  - Exactitud de información recuperada
- ✅ **Cálculo de riesgo final** según Anexo IX
- ✅ **Integración:**
  - Vincula FRIA con Project (IDXPROJECT)
  - Notifica a autoridades si riesgo crítico (Art. 27.3)
  - Integra con DPIA si aplica (Art. 27.4)

**Reutiliza:** `FriaAssessmentBusinessService` completo

---

### 4. **Health Dashboard** (`/governance/rag/monitoring/health-dashboard`)

**Objetivo:** Monitorear salud y cumplimiento de proyectos RAG

**Funcionalidades:**
- ✅ **Métricas de salud por proyecto:**
  - Total Items (chunks)
  - Active Items
  - Deployed Items
  - Training Items
  - Offline Items
  - Score promedio de calidad
- ✅ **Métricas de cumplimiento:**
  - Proyectos con clasificación actualizada
  - Proyectos con FRIA vencido (requiere renovación)
  - Proyectos con registros EU pendientes
  - Alertas de cumplimiento
- ✅ **Filtros:**
  - Por proyecto
  - Por estado de salud
  - Por estado de cumplimiento
- ✅ **Gráficos:**
  - Evolución de métricas de salud
  - Distribución de estados de cumplimiento
  - Tendencias de riesgos

**Nota:** Esta pantalla actualmente está en `/rag/monitoring/health-dashboard` pero debe moverse a `/governance/rag/monitoring/health-dashboard`

---

### 5. **Aprobación de Proyectos RAG** (`/governance/rag/approval`)

**Objetivo:** Flujo de aprobación de proyectos RAG antes de despliegue

**Funcionalidades:**
- ✅ **Listado de solicitudes de aprobación**
- ✅ **Métricas:**
  - Total pendientes
  - Total aprobadas
  - Total rechazadas
- ✅ **Información por solicitud:**
  - Proyecto RAG
  - Solicitante
  - Fecha de solicitud
  - Estado de clasificación
  - Estado de FRIA
  - Justificación
- ✅ **Acciones:**
  - Aprobar proyecto (con comentarios)
  - Rechazar proyecto (con razón obligatoria)
  - Solicitar más información
  - Ver detalles completos
- ✅ **Validaciones:**
  - Proyecto debe estar clasificado
  - Si es alto riesgo, debe tener FRIA completado
  - Debe tener documentación técnica
  - Razón obligatoria para rechazo

---

### 6. **Rendimiento y Métricas de Cumplimiento** (`/governance/rag/performance`)

**Objetivo:** Analizar rendimiento de proyectos RAG desde perspectiva de cumplimiento

**Funcionalidades:**
- ✅ **Métricas de rendimiento:**
  - Latencia promedio
  - Tasa de éxito
  - Calidad de respuestas
  - Uso de recursos
- ✅ **Métricas de cumplimiento:**
  - Score de cumplimiento normativo (0-100)
  - Proyectos en cumplimiento
  - Proyectos con incumplimientos
  - Tendencias de cumplimiento
- ✅ **Análisis comparativo:**
  - Comparar proyectos RAG
  - Benchmarking de cumplimiento
  - Identificar mejores prácticas
- ✅ **Alertas:**
  - Degradación de rendimiento
  - Incumplimientos detectados
  - FRIA próximos a vencer

---

### 7. **Analíticas de Gobierno RAG** (`/governance/rag/analytics`)

**Objetivo:** Dashboard analítico de gobierno y cumplimiento RAG

**Funcionalidades:**
- ✅ **KPIs principales:**
  - Total proyectos RAG gobernados
  - Porcentaje con clasificación completa
  - Porcentaje con FRIA completo
  - Porcentaje registrados en EU
  - Score promedio de cumplimiento
- ✅ **Gráficos:**
  - Distribución por nivel de riesgo
  - Evolución temporal de registros
  - Distribución por categoría Anexo III
  - Estado de FRIAs
- ✅ **Filtros temporales:**
  - Por rango de fechas
  - Por trimestre/año
- ✅ **Exportación:**
  - Exportar reportes
  - Exportar métricas

---

### 8. **Dashboard de Cumplimiento RAG** (`/governance/rag/compliance/dashboard`)

**Objetivo:** Vista consolidada de cumplimiento normativo de proyectos RAG

**Funcionalidades:**
- ✅ **Vista consolidada:**
  - Estado general de cumplimiento
  - Proyectos por estado
  - Alertas y pendientes
  - Próximas acciones requeridas
- ✅ **Matriz de cumplimiento:**
  - Proyectos vs Requisitos normativos
  - Estado de cada requisito
  - Enlaces a detalle
- ✅ **Accesos rápidos:**
  - Proyectos pendientes de clasificación
  - FRIAs próximos a vencer
  - Registros EU pendientes
  - Proyectos con incumplimientos

---

## 🔌 ESPECIFICACIÓN DE ENDPOINTS REST

### **Base URL:** `/api/compliance/rag-governance`

### **Endpoints Principales:**

```typescript
// Registro de proyectos RAG
GET    /api/compliance/rag-governance/registry              // Listar proyectos registrados
POST   /api/compliance/rag-governance/registry              // Registrar nuevo proyecto
GET    /api/compliance/rag-governance/registry/:id          // Obtener proyecto
PUT    /api/compliance/rag-governance/registry/:id          // Actualizar proyecto
POST   /api/compliance/rag-governance/registry/:id/sync     // Sincronizar con ejecución

// Clasificación (reutiliza endpoints de classification)
POST   /api/compliance/classification/classify              // Clasificar proyecto (con PRJISTYPE=RAG)

// FRIA (reutiliza endpoints de FRIA)
GET    /api/compliance/fria/projects/:projectId             // Obtener FRIA de proyecto RAG
POST   /api/compliance/fria/projects/:projectId             // Crear/iniciar FRIA

// Monitoreo
GET    /api/compliance/rag-governance/monitoring/health     // Health dashboard
GET    /api/compliance/rag-governance/monitoring/metrics    // Métricas de cumplimiento

// Aprobación
GET    /api/compliance/rag-governance/approval              // Listar aprobaciones
POST   /api/compliance/rag-governance/approval/:id/approve  // Aprobar proyecto
POST   /api/compliance/rag-governance/approval/:id/reject   // Rechazar proyecto

// Analytics
GET    /api/compliance/rag-governance/analytics             // KPIs y gráficos
GET    /api/compliance/rag-governance/analytics/compliance-score // Score de cumplimiento

// Integración con Telemetría
GET    /api/compliance/rag-governance/registry/:id/telemetry-metrics // Métricas desde telemetría
GET    /api/compliance/rag-governance/registry/:id/telemetry-events  // Eventos desde telemetría (últimos N)
```

---

## 📊 MODELOS DE DATOS

### **RAGProjectRegistry (Frontend)**

```typescript
interface RAGProjectRegistry {
  id: number;
  projectId: string; // UUID del Project (usado para filtrar en Telemetría)
  ragProjectId: string; // ID en sistema de ejecución
  name: string;
  description: string;
  owner: string;
  createdBy: string;
  createdAt: string;
  updatedAt: string;

  // Clasificación
  classificationStatus: "PENDING" | "CLASSIFIED" | "HIGH_RISK";
  isHighRisk: boolean;
  annexIIICategory?: string;
  classificationJustification?: string;
  classificationDate?: string;
  classifiedBy?: string;

  // FRIA
  friaStatus: "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED" | "EXPIRED";
  friaId?: number;
  friaCompletedAt?: string;
  friaExpiresAt?: string;

  // Registro EU
  euRegistrationStatus: "NOT_REGISTERED" | "PENDING" | "REGISTERED";
  euRegistrationId?: string;
  euRegistrationDate?: string;

  // Sincronización
  lastSyncAt: string;
  syncStatus: "SYNCED" | "PENDING" | "ERROR";

  // Métricas (desde sistema de ejecución)
  metrics: {
    totalChunks: number;
    totalEmbeddings: number;
    totalSearches: number;
    avgResponseTime: number;
    successRate: number;
  };

  // Métricas desde Telemetría (por UUID del proyecto)
  telemetryMetrics?: {
    totalEvents: number; // Total eventos RAG en telemetría
    totalCost: number; // Costo total desde telemetría (USD)
    totalTokens: number; // Tokens totales consumidos
    avgLatency: number; // Latencia promedio (ms)
    lastEventAt?: string; // Último evento registrado
  };

  // Score de cumplimiento
  complianceScore: number; // 0-100
}
```

---

## 🎨 ESTILOS "Wow Factor"

Seguir el mismo patrón que otras pantallas de compliance:
- Cards con glassmorphism
- Títulos alineados a la izquierda
- Animaciones suaves
- Badges con colores temáticos
- Gradientes sutiles en botones principales

---

## 🌐 INTERNACIONALIZACIÓN

**Ubicación:** `app/config/i18n/modules/governance/rag/`

**Archivos:**
- `rag-registry.ts` - Registro de proyectos
- `rag-classification.ts` - Clasificación
- `rag-fria.ts` - FRIA
- `rag-monitoring.ts` - Monitoreo
- `rag-approval.ts` - Aprobaciones
- `rag-performance.ts` - Rendimiento
- `rag-analytics.ts` - Analíticas
- `rag-compliance.ts` - Cumplimiento

**Estructura:**
```typescript
governance.rag.{pantalla}.{seccion}.{campo}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Fase 1: Registro y Clasificación**
- [ ] Crear pantalla de registro (`/governance/rag/registry`)
- [ ] Integrar con ProjectService
- [ ] Implementar sincronización con sistema de ejecución
- [ ] Crear pantalla de clasificación (reutilizar lógica existente)
- [ ] Configurar filtros y búsqueda
- [ ] Mock data completo

### **Fase 2: FRIA y Aprobación**
- [ ] Adaptar wizard FRIA para RAG
- [ ] Agregar riesgos específicos RAG
- [ ] Crear pantalla de aprobación
- [ ] Implementar validaciones
- [ ] Integrar con workflows BPMN

### **Fase 3: Monitoreo y Analytics**
- [ ] Mover Health Dashboard a `/governance/rag/monitoring/health-dashboard`
- [ ] Crear pantalla de rendimiento
- [ ] Crear pantalla de analytics
- [ ] Implementar gráficos y KPIs
- [ ] Dashboard de cumplimiento consolidado

### **Fase 4: Traducciones y Estilos**
- [ ] Traducciones completas (6 idiomas)
- [ ] Aplicar estilos "Wow Factor"
- [ ] Validar responsive
- [ ] Testing

---

## 🔗 INTEGRACIÓN CON SISTEMA DE EJECUCIÓN Y TELEMETRÍA

### **Sincronización de Datos con Sistema de Ejecución**

El sistema de gobierno debe poder sincronizarse con el sistema de ejecución RAG:

```typescript
// Endpoint en sistema de ejecución (futuro)
GET /api/rag-execution/projects/:id

// Sincronización
POST /api/compliance/rag-governance/registry/:id/sync
```

**Datos a sincronizar:**
- Nombre y descripción
- Owner
- Métricas de uso (chunks, searches, etc.)
- Estado operativo
- Última actualización

### **Integración con Telemetría**

**Los proyectos RAG se integran con Telemetría mediante UUID de proyectos.**

#### **Identificación de Eventos RAG en Telemetría**

- **Project UUID:** Los proyectos RAG en gobierno tienen un `projectId` (UUID) que es el mismo que en la entidad `Project`
- **Componente UUID:** Los componentes RAG (embedding, reranker, llm) tienen sus propios UUIDs en telemetría
- **Filtrado:** Telemetría permite filtrar eventos por:
  - `projectId` (UUID del proyecto RAG)
  - `componentUuid` (UUID del componente específico: embedding, reranker, llm)
  - `agentExternalId` (si el RAG es usado por un agente)

#### **Métricas de Telemetría para RAG**

Telemetría recibirá y analizará información de RAG:

**Eventos RAG en Telemetría:**
- **Tipo de Evento:** `RAG_SEARCH`, `RAG_CHUNK_PROCESSED`, `RAG_EMBEDDING_GENERATED`, `RAG_RERANK_COMPLETED`
- **Métricas incluidas:**
  - `tokens` - Tokens consumidos (embedding, reranker, llm)
  - `latency` - Tiempo de respuesta total
  - `cost` - Costo en USD
  - `similarityScore` - Score de similitud (para búsquedas)
  - `chunkCount` - Número de chunks procesados
  - `resultsCount` - Número de resultados retornados

**Payload de Evento RAG:**
```json
{
  "eventType": "RAG_SEARCH",
  "projectId": "550e8400-e29b-41d4-a716-446655440000", // UUID del proyecto RAG
  "componentUuid": "embedding-model-uuid",
  "agentExternalId": "agent-123", // Si es usado por agente
  "metadata": {
    "ragProjectId": "rag-proj-001", // ID en sistema de ejecución
    "searchType": "vector",
    "query": "ejemplo de consulta",
    "similarityScore": 0.95,
    "resultsCount": 10,
    "embeddingModel": "text-embedding-ada-002",
    "rerankerModel": "cohere-rerank-multilingual-v2.0",
    "llmModel": "gpt-4-turbo"
  },
  "metrics": {
    "tokens": 1500,
    "latency": 1250,
    "cost": 0.015,
    "chunkCount": 100,
    "resultsCount": 10
  },
  "governance": {
    "biasChecked": true,
    "toxicityChecked": false,
    "piiDetected": false,
    "secretDetected": false
  }
}
```

#### **Análisis de RAG en Telemetría**

**Filtros disponibles para RAG:**
- Filtrar por `projectId` (UUID del proyecto RAG en gobierno)
- Filtrar por `componentUuid` (componente específico)
- Filtrar por `eventType` que incluya "RAG_*"
- Búsqueda por contenido en query o resultados

**Métricas agregadas en Dashboard:**
- Total eventos RAG por proyecto
- Costo total de proyectos RAG
- Latencia promedio de búsquedas RAG
- Tokens consumidos por proyectos RAG
- Distribución de tipos de búsqueda (vector, textual, hybrid)

**Vínculo Governance ↔ Telemetría:**

```
Project (Gobierno)
  └─ projectId (UUID) ──────┐
                             │
                             ├─→ Telemetry Events
                             │   └─ projectId: UUID
                             │
RAG Project (Ejecución)      │
  └─ ragProjectId            │
      └─ metadata.ragProjectId en eventos
```

#### **Integración en Pantallas de Governance**

Las pantallas de gobierno RAG deben mostrar:

1. **Métricas desde Telemetría:**
   - Total de eventos del proyecto (últimos 30 días)
   - Costo total desde telemetría
   - Latencia promedio desde telemetría
   - Tokens consumidos

2. **Enlaces a Telemetría:**
   - Botón "Ver en Telemetría" que filtra por `projectId`
   - Link directo: `/governance/telemetry/search?projectId={uuid}`

3. **Sincronización de Métricas:**
   - Las métricas pueden venir de dos fuentes:
     - **Sistema de Ejecución:** Métricas operativas (chunks, searches)
     - **Telemetría:** Métricas de eventos y cumplimiento (tokens, costos, latencia, gobernanza)

---

## 📝 NOTAS IMPORTANTES

1. **RAG es otro sistema más:** RAG debe tratarse igual que models, agents, prompts y datasets desde perspectiva de gobierno
2. **Reutilizar servicios existentes:** Clasificación, FRIA, Registro EU, PMM se reutilizan completamente
3. **Sincronización:** El sistema de gobierno puede sincronizar datos del sistema de ejecución, pero el gobierno es la fuente de verdad para cumplimiento normativo
4. **Vinculación:** Los proyectos RAG se vinculan a la entidad Project con `PRJISTYPE = "RAG"` y `PRJRAGPROJECTID` con el ID del sistema de ejecución
5. **Integración con Telemetría:**
   - Telemetría recibe eventos de RAG con `projectId` (UUID del proyecto en gobierno)
   - Las pantallas de gobierno pueden mostrar métricas desde telemetría filtradas por UUID
   - Los eventos RAG incluyen `eventType: "RAG_*"` y metadata con `ragProjectId`
   - Enlaces directos desde gobierno a telemetría usando `projectId` como filtro

---

**Última actualización:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación
