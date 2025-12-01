# ESTADO PROMPTS MONITORIZACIÓN TIEMPO REAL

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Aclarar qué ya existe y qué falta implementar para los prompts de monitorización en tiempo real (INC-009-002 a INC-009-006)

---

## ✅ INFRAESTRUCTURA EXISTENTE

### 1. Microservicios Implementados

#### `codeflowx-aios-telemetry` (Microservicio REST)
- **Ubicación:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx-aios-telemetry`
- **Puerto:** 8096
- **Estado:** ✅ **IMPLEMENTADO**
- **Funcionalidad:**
  - Recibe eventos HTTP de agentes externos
  - Valida payloads
  - Publica a RabbitMQ (`aios.telemetry.events`)
  - Endpoints:
    - `POST /api/v1/aios/telemetry/events` - Evento individual
    - `POST /api/v1/aios/telemetry/events/batch` - Múltiples eventos
    - `POST /api/v1/aios/telemetry/mlflow-trace` - Traces MLflow
    - `GET /api/v1/aios/telemetry/health` - Health check

#### `codeflowx-aios-telemetry-worker` (Worker)
- **Ubicación:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx-aios-telemetry-worker`
- **Estado:** ✅ **IMPLEMENTADO**
- **Funcionalidad:**
  - Consume eventos de RabbitMQ
  - Análisis de gobernanza (bias, toxicity, PII, secretos)
  - Persistencia en TimescaleDB
  - Disparo de procesos BPMN

---

### 2. Base de Datos Separada

#### Base de Datos: `codeflowx_telemetry`
- **Tipo:** PostgreSQL + TimescaleDB + pgvector
- **Configuración:** `jdbc:postgresql://localhost:5432/codeflowx_telemetry`
- **Estado:** ✅ **CONFIGURADA**

#### Tablas Existentes:

1. **`AIOTELEMETRY`** (Hypertable TimescaleDB)
   - **Estado:** ✅ **CREADA**
   - **Script:** `codeflowx-aios-telemetry-worker/src/main/resources/db/migration/aiotelemetry_setup.sql`
   - **Características:**
     - Particionamiento automático por tiempo (1 día)
     - Compresión automática (después de 30 días)
     - Retención automática (1 año)
     - Índices GIN para JSONB
     - pgvector para embeddings

2. **`AIOINCIDENTMETRICS`** (Métricas de Incidentes)
   - **Estado:** ✅ **CREADA**
   - **Script:** `nocode.service.entitys/src/main/resources/sql/ai_incident_metrics.sql`
   - **Entidad JPA:** `AioIncidentMetric.java`
   - **Servicio:** `AioIncidentMetricService.java` (en `codeflowx.govern.services`)
   - **Servicio Worker:** `IncidentMetricsService.java` (en `codeflowx-aios-telemetry-worker`)

---

### 3. Entidades y Servicios Existentes

#### Entidades JPA:
- ✅ `AioTelemetry` - Entidad principal de telemetría
- ✅ `AioIncidentMetric` - Métricas de incidentes

#### Servicios:
- ✅ `TelemetryService` - Publicación a RabbitMQ
- ✅ `RealtimeGovernanceService` - Análisis de gobernanza
- ✅ `IncidentMetricsService` - Tracking de métricas de incidentes
- ✅ `AioIncidentMetricService` - CRUD de métricas (en `codeflowx.govern.services`)

#### Repositorios:
- ✅ `AioTelemetryRepository` - JPA repository para telemetría
- ✅ `AioIncidentMetricRepository` - JPA repository para métricas

---

## 📋 ESTADO DE LOS PROMPTS

### INC-009-002: Throttling y Backpressure

**Estado según Prompt:** ✅ COMPLETADO
**Estado Real:** ⚠️ **PARCIALMENTE IMPLEMENTADO**

#### Lo que existe:
- ✅ Configuración RabbitMQ con límites de cola
- ✅ Verificación de tamaño de cola en `TelemetryController`
- ✅ Respuesta HTTP 503 cuando cola saturada

#### Lo que falta:
- ⚠️ **Verificar implementación real** en `TelemetryController.java`:
  - Método `getRabbitMQQueueSize()` debe estar implementado
  - Verificación de capacidad antes de aceptar eventos
  - Configuración de `maxLength` en cola RabbitMQ

**Acción Requerida:**
1. Verificar que `TelemetryController` tiene el código de throttling
2. Verificar configuración de cola RabbitMQ con `maxLength`
3. Si falta, implementar según prompt

---

### INC-009-003: Análisis Asíncrono

**Estado según Prompt:** ✅ COMPLETADO
**Estado Real:** ⚠️ **PARCIALMENTE IMPLEMENTADO**

#### Lo que existe:
- ✅ `RealtimeGovernanceServiceImpl` con separación de análisis
- ✅ Análisis críticos síncronos (PII, Secretos)
- ✅ Análisis no críticos asíncronos (Bias, Toxicity)

#### Lo que falta:
- ⚠️ **Verificar implementación real** en `RealtimeGovernanceServiceImpl.java`:
  - Método `executeCriticalAnalysis()` debe estar implementado
  - Método `executeNonCriticalAnalysisAsync()` debe estar implementado
  - Uso de `CompletableFuture` para análisis asíncronos

**Acción Requerida:**
1. Verificar que `RealtimeGovernanceServiceImpl` tiene la separación de análisis
2. Si falta, implementar según prompt

---

### INC-009-004: Métricas de Incidentes

**Estado según Prompt:** ✅ COMPLETADO
**Estado Real:** ✅ **IMPLEMENTADO**

#### Lo que existe:
- ✅ Tabla `AIOINCIDENTMETRICS` creada
- ✅ Entidad JPA `AioIncidentMetric` implementada
- ✅ Servicio `IncidentMetricsService` implementado
- ✅ Métodos:
  - `recordIncidentDetection()`
  - `recordIncidentAlert()`
  - `recordIncidentAction()`
  - `recordIncidentResolution()`
  - `generateMonthlyReport()`
- ✅ Integración en `RealtimeGovernanceServiceImpl`

**Acción Requerida:**
- ✅ **Ninguna** - Está completamente implementado

---

### INC-009-006: Health Indicator

**Estado según Prompt:** ✅ COMPLETADO
**Estado Real:** ❌ **NO ENCONTRADO**

#### Lo que existe:
- ✅ Actuator endpoints configurados
- ✅ Health checks básicos de Spring Boot

#### Lo que falta:
- ❌ **`TelemetryHealthIndicator` NO encontrado**
- ❌ Health check específico para telemetría:
  - Estado RabbitMQ
  - Workers activos
  - Tamaño de cola
  - Lag de procesamiento
  - Throughput

**Acción Requerida:**
1. **Crear** `TelemetryHealthIndicator.java` en `codeflowx-aios-telemetry`
2. Implementar checks según prompt:
   - RabbitMQ conectado
   - Workers procesando
   - Cola no saturada
   - BD accesible
   - Throughput normal
3. Exponer en `/actuator/health/telemetry`

---

## 🎯 RESUMEN DE ACCIONES REQUERIDAS

| Prompt | Estado Prompt | Estado Real | Acción Requerida |
|--------|---------------|-------------|------------------|
| **INC-009-002** | ✅ COMPLETADO | ⚠️ PARCIAL | Verificar e implementar si falta |
| **INC-009-003** | ✅ COMPLETADO | ⚠️ PARCIAL | Verificar e implementar si falta |
| **INC-009-004** | ✅ COMPLETADO | ✅ COMPLETO | ✅ Ninguna |
| **INC-009-006** | ✅ COMPLETADO | ❌ FALTA | **Crear TelemetryHealthIndicator** |

---

## 📝 NOTAS IMPORTANTES

### Arquitectura Existente:

```
┌─────────────────────────────────────┐
│  Agente Externo                     │
│  (Chatbot, RAG, Copilot, etc.)      │
└──────────────┬──────────────────────┘
               │ HTTP POST
               ▼
┌─────────────────────────────────────┐
│  codeflowx-aios-telemetry           │
│  (Puerto 8096)                      │
│  - Recibe eventos                   │
│  - Valida payload                   │
│  - Publica a RabbitMQ               │
└──────────────┬──────────────────────┘
               │ RabbitMQ
               │ (aios.telemetry.events)
               ▼
┌─────────────────────────────────────┐
│  codeflowx-aios-telemetry-worker    │
│  - Consume de RabbitMQ              │
│  - Análisis de gobernanza           │
│  - Guarda en BD                     │
│  - Dispara BPMN si hay problemas   │
└──────────────┬──────────────────────┘
               │
        ┌──────┴──────┐
        ▼             ▼
┌─────────────┐  ┌─────────────┐
│ TimescaleDB │  │ Flowable    │
│ codeflowx_  │  │ BPMN Engine │
│ telemetry   │  │             │
│ AIOTELEMETRY│  │             │
│ AIOINCIDENT │  │             │
│ METRICS     │  │             │
└─────────────┘  └─────────────┘
```

### Base de Datos Separada:

- **Nombre:** `codeflowx_telemetry`
- **Propósito:** Almacenar telemetría de alto volumen sin afectar BD principal
- **Características:**
  - TimescaleDB para time-series data
  - Compresión automática (5-10x)
  - Retención automática (1 año)
  - pgvector para búsqueda semántica

---

## 🔍 VERIFICACIÓN REQUERIDA

### 1. Verificar Throttling y Backpressure (INC-009-002)

**Archivo:** `codeflowx-aios-telemetry/src/main/java/com/codeflowx/aios/telemetry/controller/TelemetryController.java`

**Verificar:**
- [ ] Método `getRabbitMQQueueSize()` implementado
- [ ] Verificación de capacidad antes de aceptar eventos
- [ ] Respuesta HTTP 503 cuando cola > 80% o >= 100%
- [ ] Configuración de `telemetry.queue.max-size` en `application.yml`

**Archivo:** `codeflowx-aios-telemetry/src/main/java/com/codeflowx/aios/telemetry/config/RabbitMQConfig.java`

**Verificar:**
- [ ] Cola configurada con `maxLength(100000)`
- [ ] `overflow(QueueBuilder.Overflow.rejectPublish)`

---

### 2. Verificar Análisis Asíncrono (INC-009-003)

**Archivo:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/service/impl/RealtimeGovernanceServiceImpl.java`

**Verificar:**
- [ ] Método `executeCriticalAnalysis()` implementado
- [ ] Método `executeNonCriticalAnalysisAsync()` implementado
- [ ] Análisis críticos (PII, Secretos) ejecutados síncronamente
- [ ] Análisis no críticos (Bias, Toxicity) ejecutados con `CompletableFuture`
- [ ] Resultados asíncronos se guardan en BD cuando completan

---

### 3. Implementar Health Indicator (INC-009-006)

**Archivo a Crear:** `codeflowx-aios-telemetry/src/main/java/com/codeflowx/aios/telemetry/health/TelemetryHealthIndicator.java`

**Implementar:**
- [ ] Clase `TelemetryHealthIndicator` que implementa `HealthIndicator`
- [ ] Check RabbitMQ (conectado, tamaño de cola)
- [ ] Check Workers (último mensaje procesado, lag)
- [ ] Check Database (accesible)
- [ ] Check Throughput (eventos/segundo)
- [ ] Exponer en `/actuator/health/telemetry`

**Métodos Repository Requeridos:**
- [ ] `findTopByOrderByTeltimestampDesc()` en `AioTelemetryRepository`
- [ ] `countByTeltimestampAfter(Timestamp timestamp)` en `AioTelemetryRepository`

---

## 📊 CONCLUSIÓN

### ✅ Lo que NO hay que crear (ya existe):
1. **Microservicio REST** (`codeflowx-aios-telemetry`) - ✅ Existe
2. **Worker** (`codeflowx-aios-telemetry-worker`) - ✅ Existe
3. **Base de datos separada** (`codeflowx_telemetry`) - ✅ Existe
4. **Tabla AIOTELEMETRY** (TimescaleDB) - ✅ Existe
5. **Tabla AIOINCIDENTMETRICS** - ✅ Existe
6. **Entidades JPA** - ✅ Existen
7. **Servicios básicos** - ✅ Existen

### ⚠️ Lo que hay que verificar/implementar:
1. **INC-009-002:** Verificar throttling y backpressure en `TelemetryController`
2. **INC-009-003:** Verificar análisis asíncrono en `RealtimeGovernanceServiceImpl`
3. **INC-009-006:** **Crear** `TelemetryHealthIndicator` (NO existe)

### ✅ Lo que está completo:
1. **INC-009-004:** Métricas de incidentes - ✅ Completamente implementado

---

**Última actualización:** 25 de noviembre de 2025
**Próximo paso:** Verificar implementación real de INC-009-002 y INC-009-003, crear INC-009-006
