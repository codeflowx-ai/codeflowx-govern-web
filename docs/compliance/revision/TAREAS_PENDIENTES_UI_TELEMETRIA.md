# TAREAS PENDIENTES UI - TELEMETRÍA Y MONITORIZACIÓN TIEMPO REAL

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Identificar qué dashboards, buscadores y páginas de consulta faltan para telemetría y monitorización en tiempo real

---

## 📊 RESUMEN EJECUTIVO

| Componente | Estado Actual | Falta Crear | Prioridad |
|------------|---------------|-------------|-----------|
| **Dashboard Telemetría** | ❌ No existe | ✅ **SÍ** | 🔴 ALTA |
| **Buscador Telemetría** | ❌ No existe | ✅ **SÍ** | 🔴 ALTA |
| **Página Consulta Eventos** | ❌ No existe | ✅ **SÍ** | 🔴 ALTA |
| **Dashboard Métricas Incidentes** | ❌ No existe | ✅ **SÍ** | 🟡 MEDIA |
| **Visualización Tendencias** | ❌ No existe | ✅ **SÍ** | 🟡 MEDIA |
| **Exportación Reportes** | ❌ No existe | ✅ **SÍ** | 🟢 BAJA |

---

## 🔍 ANÁLISIS DE LO QUE EXISTE

### ViewModels Existentes (NO específicos para telemetría):

1. **MonitoringDashboardViewModel**
   - **Ubicación:** `com.codeflowx.govern.viewmodel.monitoring.MonitoringDashboardViewModel`
   - **ZUL:** `console/gobierno/monitoring/monitoring-overview.zul`
   - **Funcionalidad:** Dashboard genérico de monitoring
   - **Problema:** ❌ **NO consulta `AIOTELEMETRY`** - Usa vistas genéricas (`MonitoringDashboard`, `MetricsVisualization`, `AlertSummary`)
   - **Conclusión:** No es específico para telemetría de agentes externos

2. **MonitoringDashboardService**
   - **Funcionalidad:** Consulta vistas genéricas, no `AIOTELEMETRY`
   - **Problema:** ❌ No tiene acceso a base de datos `codeflowx_telemetry`

### Repositorios Existentes (solo en worker):

1. **AioTelemetryRepository**
   - **Ubicación:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/repository/AioTelemetryRepository.java`
   - **Problema:** ❌ Está en el **worker**, no accesible desde `suinsit.nova.web`
   - **Base de datos:** `codeflowx_telemetry` (separada)

2. **AioIncidentMetricRepository**
   - **Ubicación:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/repository/AioIncidentMetricRepository.java`
   - **Problema:** ❌ Está en el **worker**, no accesible desde `suinsit.nova.web`

### Servicios Existentes (solo en worker):

1. **IncidentMetricsService**
   - **Ubicación:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/service/IncidentMetricsService.java`
   - **Problema:** ❌ Está en el **worker**, no accesible desde `suinsit.nova.web`

---

## ❌ LO QUE FALTA CREAR

### 1. 🔴 Dashboard de Telemetría en Tiempo Real

**ViewModel:** `TelemetryDashboardViewModel.java`
**ZUL:** `console/gobierno/telemetry/telemetry-dashboard.zul`
**BusinessService:** `TelemetryDashboardService.java` (nuevo)

**Funcionalidad Requerida:**
- KPIs en tiempo real:
  - Eventos recibidos/segundo (throughput)
  - Eventos procesados/segundo
  - Lag de procesamiento (segundos)
  - Tamaño de cola RabbitMQ
  - Workers activos
- Gráficos:
  - Throughput en tiempo real (últimas 24h)
  - Latencia p50, p95, p99
  - Eventos por tipo (INTERACTION_COMPLETED, CODE_GENERATION, etc.)
  - Eventos por severidad (INFO, WARNING, CRITICAL)
  - Eventos por agente externo
- Alertas activas:
  - Incidentes críticos detectados
  - Análisis pendientes
  - Cola saturada (>80%)

**Integración:**
- **API REST:** Crear endpoint en `codeflowx-aios-telemetry` o nuevo servicio
- **Base de datos:** Consultar `codeflowx_telemetry.AIOTELEMETRY`
- **Métricas:** Consultar `codeflowx_telemetry.AIOINCIDENTMETRICS`

**Prioridad:** 🔴 ALTA
**Esfuerzo:** 5-7 días
**Prompt Asociado:** INC-009-001 (Documentación Capacidad), INC-009-006 (Health Indicator)

---

### 2. 🔴 Buscador de Telemetría

**ViewModel:** `TelemetrySearchViewModel.java`
**ZUL:** `console/gobierno/telemetry/telemetry-search.zul`
**BusinessService:** `TelemetrySearchService.java` (nuevo)

**Funcionalidad Requerida:**
- Búsqueda avanzada con filtros:
  - Por componente UUID
  - Por agente externo (`TELAGENTEXTERNALID`)
  - Por tipo de evento (`TELEVENTTYPE`)
  - Por severidad (`TELSEVERITY`)
  - Por rango de fechas (`TELTIMESTAMP`)
  - Por trace ID (`TELTRACEID`) - MLflow
  - Por run ID (`TELRUNID`) - MLflow
  - Por herramienta origen (`TELSOURCETOOL`)
- Búsqueda semántica:
  - Búsqueda por texto en payload (usando embeddings pgvector)
  - Búsqueda de eventos similares
- Filtros de análisis:
  - Eventos con bias detectado
  - Eventos con PII detectado
  - Eventos con secretos detectados
  - Eventos con problemas de compliance
  - Eventos que dispararon procesos BPMN

**Integración:**
- **API REST:** Crear endpoint en `codeflowx-aios-telemetry` o nuevo servicio
- **Base de datos:** Consultar `codeflowx_telemetry.AIOTELEMETRY` con búsqueda semántica (pgvector)

**Prioridad:** 🔴 ALTA
**Esfuerzo:** 5-7 días
**Prompt Asociado:** INC-008-018 (Búsqueda Avanzada Logs - similar pero para telemetría)

---

### 3. 🔴 Página de Consulta de Eventos Individuales

**ViewModel:** `TelemetryEventDetailViewModel.java`
**ZUL:** `console/gobierno/telemetry/telemetry-event-detail.zul`
**BusinessService:** `TelemetryEventService.java` (nuevo)

**Funcionalidad Requerida:**
- Detalle completo del evento:
  - Información básica (timestamp, componente, agente, tipo, severidad)
  - Métricas (`TELMETRICS` JSONB):
    - Latencia (ms)
    - Tokens usados
    - Costo (USD)
    - Otros métricas personalizadas
  - Payload completo (`TELPAYLOAD` JSONB):
    - Request completo
    - Response completo
    - Contexto de usuario
    - Metadata del modelo
  - Resultados de análisis (`TELANALYSISRESULTS` JSONB):
    - Resultado de bias detection
    - Resultado de toxicity check
    - Hallazgos de PII
    - Hallazgos de secretos
    - Violaciones de compliance
  - Proceso BPMN disparado:
    - ID del proceso
    - Estado del proceso
    - Enlace a detalle del proceso
  - Eventos relacionados:
    - Mismo trace ID
    - Mismo run ID
    - Mismo componente

**Integración:**
- **API REST:** Crear endpoint en `codeflowx-aios-telemetry` o nuevo servicio
- **Base de datos:** Consultar `codeflowx_telemetry.AIOTELEMETRY` por UUID

**Prioridad:** 🔴 ALTA
**Esfuerzo:** 3-5 días

---

### 4. 🟡 Dashboard de Métricas de Incidentes

**ViewModel:** `TelemetryIncidentMetricsViewModel.java`
**ZUL:** `console/gobierno/telemetry/telemetry-incident-metrics.zul`
**BusinessService:** `TelemetryIncidentMetricsService.java` (nuevo)

**Funcionalidad Requerida:**
- Métricas de efectividad:
  - Total incidentes detectados (último mes)
  - Tiempo promedio de detección (detección → alerta) en ms
  - Tiempo promedio de respuesta (alerta → acción) en ms
  - Tiempo promedio de resolución (detección → resolución) en ms
  - Tasa de falsos positivos (%)
  - Tasa de resolución automática vs manual (%)
- Distribuciones:
  - Por tipo de incidente (DRIFT, SECURITY, COMPLIANCE, BIAS)
  - Por impacto (LOW, MEDIUM, HIGH, CRITICAL)
  - Por agente externo
  - Por componente
- Gráficos:
  - Tendencias temporales (últimos 6 meses)
  - Comparativa mensual
  - Heatmap de incidentes por día/hora

**Integración:**
- **API REST:** Crear endpoint en `codeflowx-aios-telemetry` o nuevo servicio
- **Base de datos:** Consultar `codeflowx_telemetry.AIOINCIDENTMETRICS`

**Prioridad:** 🟡 MEDIA
**Esfuerzo:** 4-6 días
**Prompt Asociado:** INC-009-004 (Métricas de Incidentes) - ✅ Backend implementado, falta UI

---

### 5. 🟡 Visualización de Tendencias

**ViewModel:** `TelemetryTrendsViewModel.java`
**ZUL:** `console/gobierno/telemetry/telemetry-trends.zul`
**BusinessService:** `TelemetryTrendsService.java` (nuevo)

**Funcionalidad Requerida:**
- Tendencias temporales:
  - Throughput por día/semana/mes
  - Latencia promedio por día
  - Costo acumulado por día
  - Tokens usados por día
  - Eventos por tipo de evento
  - Eventos por severidad
- Análisis de patrones:
  - Horas pico de tráfico
  - Días de mayor actividad
  - Estacionalidad
- Comparativas:
  - Comparar períodos (mes actual vs mes anterior)
  - Comparar agentes externos
  - Comparar componentes

**Integración:**
- **API REST:** Crear endpoint en `codeflowx-aios-telemetry` o nuevo servicio
- **Base de datos:** Consultar `codeflowx_telemetry.AIOTELEMETRY` con agregaciones TimescaleDB

**Prioridad:** 🟡 MEDIA
**Esfuerzo:** 4-6 días
**Prompt Asociado:** INC-010-013 (Visualización Tendencias Avanzadas)

---

### 6. 🟢 Exportación de Reportes

**ViewModel:** Extender `TelemetryDashboardViewModel` o `TelemetrySearchViewModel`
**Funcionalidad:** Métodos de exportación

**Funcionalidad Requerida:**
- Exportar a PDF:
  - Reporte ejecutivo de telemetría
  - Resumen de incidentes
  - Análisis de tendencias
- Exportar a Excel/CSV:
  - Eventos filtrados
  - Métricas de incidentes
  - Tendencias temporales
- Exportar a JSON:
  - Eventos completos (para análisis externo)
  - Payloads completos

**Prioridad:** 🟢 BAJA
**Esfuerzo:** 2-3 días
**Prompt Asociado:** INC-008-DS (Exportación Reportes)

---

## 🏗️ ARQUITECTURA PROPUESTA

### Opción 1: API REST en Microservicio Existente

**Crear endpoints REST en `codeflowx-aios-telemetry`:**

```
GET  /api/v1/aios/telemetry/dashboard/metrics
GET  /api/v1/aios/telemetry/events/search
GET  /api/v1/aios/telemetry/events/{uuid}
GET  /api/v1/aios/telemetry/incidents/metrics
GET  /api/v1/aios/telemetry/trends
```

**Ventajas:**
- ✅ Reutiliza microservicio existente
- ✅ Acceso directo a base de datos `codeflowx_telemetry`
- ✅ No requiere cambios en `suinsit.nova.web` (solo ViewModels)

**Desventajas:**
- ⚠️ Microservicio actual solo recibe eventos, no consulta
- ⚠️ Requiere añadir dependencias JPA/Spring Data

---

### Opción 2: Nuevo Servicio en `codeflowx.govern.services`

**Crear servicios en módulo existente:**

```
com.codeflowx.govern.service.telemetry.TelemetryDashboardService
com.codeflowx.govern.service.telemetry.TelemetrySearchService
com.codeflowx.govern.service.telemetry.TelemetryEventService
com.codeflowx.govern.service.telemetry.TelemetryIncidentMetricsService
com.codeflowx.govern.service.telemetry.TelemetryTrendsService
```

**Ventajas:**
- ✅ Sigue arquitectura existente
- ✅ Acceso desde ViewModels mediante `@WireVariable`
- ✅ Puede usar `BusinessService` existente

**Desventajas:**
- ⚠️ Requiere configuración de datasource para `codeflowx_telemetry`
- ⚠️ Requiere añadir dependencia a `AioTelemetry` entity

---

### Opción 3: API REST Separada (Recomendada)

**Crear nuevo microservicio:** `codeflowx-aios-telemetry-api`

**Propósito:** Solo consultas (read-only) de telemetría

**Endpoints:**
```
GET  /api/v1/telemetry/dashboard/metrics
GET  /api/v1/telemetry/events/search
GET  /api/v1/telemetry/events/{uuid}
GET  /api/v1/telemetry/incidents/metrics
GET  /api/v1/telemetry/trends
GET  /api/v1/telemetry/health
```

**Ventajas:**
- ✅ Separación de responsabilidades (ingest vs query)
- ✅ Escalabilidad independiente
- ✅ No afecta performance de ingestión
- ✅ Puede usar cache (Redis) para consultas frecuentes

**Desventajas:**
- ⚠️ Requiere crear nuevo microservicio
- ⚠️ Más complejidad operativa

---

## 📋 TAREAS PENDIENTES DETALLADAS

### Tarea 1: Dashboard de Telemetría en Tiempo Real

**Componentes a Crear:**
1. **ViewModel:** `TelemetryDashboardViewModel.java`
   - Ubicación: `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/telemetry/TelemetryDashboardViewModel.java`
   - Funcionalidad:
     - Cargar KPIs en tiempo real
     - Cargar gráficos de throughput
     - Cargar alertas activas
     - Auto-refresh cada 30 segundos

2. **ZUL:** `telemetry-dashboard.zul`
   - Ubicación: `suinsit.nova.web/src/main/webapp/console/gobierno/telemetry/telemetry-dashboard.zul`
   - Componentes:
     - KPIs cards (throughput, lag, cola, workers)
     - Gráficos Chart.js (throughput, latencia)
     - Tabla de alertas activas
     - Botón de refresh manual

3. **BusinessService:** `TelemetryDashboardService.java`
   - Ubicación: `nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/telemetry/TelemetryDashboardService.java`
   - Métodos:
     - `getDashboardMetrics()` - KPIs principales
     - `getThroughputChartData(OffsetDateTime start, OffsetDateTime end)` - Datos para gráfico
     - `getActiveAlerts()` - Alertas activas
     - `getQueueStatus()` - Estado de cola RabbitMQ

4. **API REST (opcional):** Endpoints en `codeflowx-aios-telemetry` o nuevo servicio

**Prioridad:** 🔴 ALTA
**Esfuerzo:** 5-7 días
**Dependencias:**
- Configurar datasource para `codeflowx_telemetry`
- Añadir dependencia a `AioTelemetry` entity

---

### Tarea 2: Buscador de Telemetría

**Componentes a Crear:**
1. **ViewModel:** `TelemetrySearchViewModel.java`
   - Ubicación: `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/telemetry/TelemetrySearchViewModel.java`
   - Funcionalidad:
     - Filtros múltiples
     - Búsqueda por texto (semántica)
     - Paginación
     - Exportación

2. **ZUL:** `telemetry-search.zul`
   - Ubicación: `suinsit.nova.web/src/main/webapp/console/gobierno/telemetry/telemetry-search.zul`
   - Componentes:
     - Panel de filtros (componente, agente, tipo, severidad, fecha)
     - Campo de búsqueda semántica
     - Grid con resultados
     - Paginación
     - Botones de exportación

3. **BusinessService:** `TelemetrySearchService.java`
   - Ubicación: `nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/telemetry/TelemetrySearchService.java`
   - Métodos:
     - `searchTelemetry(TelemetrySearchCriteria criteria, PageParams pageParams)` - Búsqueda con filtros
     - `searchSemantic(String query, PageParams pageParams)` - Búsqueda semántica (pgvector)
     - `findByUuid(String uuid)` - Evento individual

**Prioridad:** 🔴 ALTA
**Esfuerzo:** 5-7 días
**Dependencias:**
- Configurar datasource para `codeflowx_telemetry`
- Implementar búsqueda semántica con pgvector

---

### Tarea 3: Página de Consulta de Eventos Individuales

**Componentes a Crear:**
1. **ViewModel:** `TelemetryEventDetailViewModel.java`
   - Ubicación: `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/telemetry/TelemetryEventDetailViewModel.java`
   - Funcionalidad:
     - Cargar evento por UUID
     - Mostrar payload completo
     - Mostrar resultados de análisis
     - Mostrar proceso BPMN relacionado
     - Mostrar eventos relacionados

2. **ZUL:** `telemetry-event-detail.zul`
   - Ubicación: `suinsit.nova.web/src/main/webapp/console/gobierno/telemetry/telemetry-event-detail.zul`
   - Componentes:
     - Panel de información básica
     - Panel de métricas (JSONB formateado)
     - Panel de payload (JSONB formateado con syntax highlighting)
     - Panel de resultados de análisis
     - Panel de proceso BPMN
     - Panel de eventos relacionados

3. **BusinessService:** `TelemetryEventService.java`
   - Ubicación: `nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/telemetry/TelemetryEventService.java`
   - Métodos:
     - `findByUuid(String uuid)` - Evento individual
     - `findRelatedEvents(String traceId, String runId, String componentUuid)` - Eventos relacionados

**Prioridad:** 🔴 ALTA
**Esfuerzo:** 3-5 días

---

### Tarea 4: Dashboard de Métricas de Incidentes

**Componentes a Crear:**
1. **ViewModel:** `TelemetryIncidentMetricsViewModel.java`
   - Ubicación: `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/telemetry/TelemetryIncidentMetricsViewModel.java`
   - Funcionalidad:
     - Cargar métricas de incidentes
     - Cargar distribuciones
     - Cargar gráficos de tendencias

2. **ZUL:** `telemetry-incident-metrics.zul`
   - Ubicación: `suinsit.nova.web/src/main/webapp/console/gobierno/telemetry/telemetry-incident-metrics.zul`
   - Componentes:
     - KPIs de métricas
     - Gráficos de distribuciones
     - Gráficos de tendencias temporales
     - Heatmap de incidentes

3. **BusinessService:** `TelemetryIncidentMetricsService.java`
   - Ubicación: `nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/telemetry/TelemetryIncidentMetricsService.java`
   - Métodos:
     - `getMonthlyReport(int year, int month)` - Reporte mensual (ya existe en worker, reutilizar)
     - `getTrends(OffsetDateTime start, OffsetDateTime end)` - Tendencias
     - `getDistributions()` - Distribuciones

**Prioridad:** 🟡 MEDIA
**Esfuerzo:** 4-6 días
**Nota:** El backend ya está implementado (`IncidentMetricsService` en worker), solo falta UI

---

### Tarea 5: Visualización de Tendencias

**Componentes a Crear:**
1. **ViewModel:** `TelemetryTrendsViewModel.java`
   - Ubicación: `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/telemetry/TelemetryTrendsViewModel.java`
   - Funcionalidad:
     - Cargar tendencias temporales
     - Cargar análisis de patrones
     - Cargar comparativas

2. **ZUL:** `telemetry-trends.zul`
   - Ubicación: `suinsit.nova.web/src/main/webapp/console/gobierno/telemetry/telemetry-trends.zul`
   - Componentes:
     - Selector de período
     - Gráficos de tendencias (Chart.js)
     - Análisis de patrones
     - Comparativas

3. **BusinessService:** `TelemetryTrendsService.java`
   - Ubicación: `nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/telemetry/TelemetryTrendsService.java`
   - Métodos:
     - `getThroughputTrends(OffsetDateTime start, OffsetDateTime end, String granularity)` - Tendencias de throughput
     - `getLatencyTrends(OffsetDateTime start, OffsetDateTime end, String granularity)` - Tendencias de latencia
     - `getCostTrends(OffsetDateTime start, OffsetDateTime end, String granularity)` - Tendencias de costo
     - `getPeakHours()` - Horas pico
     - `comparePeriods(OffsetDateTime period1Start, OffsetDateTime period1End, OffsetDateTime period2Start, OffsetDateTime period2End)` - Comparativa

**Prioridad:** 🟡 MEDIA
**Esfuerzo:** 4-6 días
**Prompt Asociado:** INC-010-013 (Visualización Tendencias Avanzadas)

---

### Tarea 6: Exportación de Reportes

**Componentes a Crear:**
1. **Métodos en ViewModels existentes:**
   - `exportToPDF()` - Exportar a PDF
   - `exportToExcel()` - Exportar a Excel
   - `exportToCSV()` - Exportar a CSV
   - `exportToJSON()` - Exportar a JSON

2. **Servicios de Exportación:**
   - `TelemetryExportService.java` - Servicio para generar reportes

**Prioridad:** 🟢 BAJA
**Esfuerzo:** 2-3 días

---

## 🔗 INTEGRACIÓN CON PROMPTS EXISTENTES

### Prompts que Requieren UI de Telemetría:

1. **INC-009-001:** Documentación Capacidad
   - **Requiere:** Dashboard con métricas de throughput, latencia, capacidad
   - **Tarea:** Tarea 1 (Dashboard Telemetría)

2. **INC-009-004:** Métricas de Incidentes
   - **Requiere:** Dashboard de métricas de incidentes
   - **Tarea:** Tarea 4 (Dashboard Métricas Incidentes)
   - **Estado Backend:** ✅ Implementado
   - **Estado UI:** ❌ Falta

3. **INC-009-006:** Health Indicator
   - **Requiere:** Visualización de health en dashboard
   - **Tarea:** Tarea 1 (Dashboard Telemetría)

4. **INC-010-013:** Visualización Tendencias Avanzadas
   - **Requiere:** Visualización de tendencias
   - **Tarea:** Tarea 5 (Visualización Tendencias)

5. **INC-008-018:** Búsqueda Avanzada Logs (similar para telemetría)
   - **Requiere:** Buscador avanzado
   - **Tarea:** Tarea 2 (Buscador Telemetría)

---

## 📊 RESUMEN DE TAREAS PENDIENTES

| # | Tarea | ViewModel | ZUL | BusinessService | Prioridad | Esfuerzo | Prompt |
|---|-------|-----------|-----|-----------------|-----------|----------|---------|
| 1 | Dashboard Telemetría Tiempo Real | ✅ Crear | ✅ Crear | ✅ Crear | 🔴 ALTA | 5-7 días | INC-009-001, INC-009-006 |
| 2 | Buscador Telemetría | ✅ Crear | ✅ Crear | ✅ Crear | 🔴 ALTA | 5-7 días | INC-008-018 (similar) |
| 3 | Consulta Eventos Individuales | ✅ Crear | ✅ Crear | ✅ Crear | 🔴 ALTA | 3-5 días | - |
| 4 | Dashboard Métricas Incidentes | ✅ Crear | ✅ Crear | ✅ Crear | 🟡 MEDIA | 4-6 días | INC-009-004 |
| 5 | Visualización Tendencias | ✅ Crear | ✅ Crear | ✅ Crear | 🟡 MEDIA | 4-6 días | INC-010-013 |
| 6 | Exportación Reportes | ⚠️ Extender | ⚠️ Extender | ✅ Crear | 🟢 BAJA | 2-3 días | INC-008-DS |

**Total Esfuerzo Estimado:** 23-34 días
**Total ViewModels a Crear:** 5
**Total ZULs a Crear:** 5
**Total BusinessServices a Crear:** 6

---

## 🎯 RECOMENDACIÓN DE IMPLEMENTACIÓN

### Fase 1: Críticas (Semana 1-2)
1. **Tarea 1:** Dashboard Telemetría Tiempo Real
2. **Tarea 2:** Buscador Telemetría
3. **Tarea 3:** Consulta Eventos Individuales

### Fase 2: Medias (Semana 3-4)
4. **Tarea 4:** Dashboard Métricas Incidentes
5. **Tarea 5:** Visualización Tendencias

### Fase 3: Bajas (Semana 5)
6. **Tarea 6:** Exportación Reportes

---

## 🔧 CONFIGURACIÓN REQUERIDA

### 1. Datasource para Base de Datos Separada

**Archivo:** `nocode.service/codeflowx.govern.services/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    # Datasource principal (existente)
    url: jdbc:postgresql://localhost:5432/codeflowx_governance
    username: postgres
    password: postgres

  # Datasource para telemetría (nuevo)
  datasource:
    telemetry:
      url: jdbc:postgresql://localhost:5432/codeflowx_telemetry
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver
```

### 2. Configuración de EntityManager

**Archivo:** `TelemetryDataSourceConfig.java` (nuevo)

```java
@Configuration
@EnableJpaRepositories(
    basePackages = "com.codeflowx.govern.repository.telemetry",
    entityManagerFactoryRef = "telemetryEntityManagerFactory",
    transactionManagerRef = "telemetryTransactionManager"
)
public class TelemetryDataSourceConfig {
    // Configuración para base de datos de telemetría
}
```

---

## 📝 NOTAS IMPORTANTES

1. **Base de Datos Separada:**
   - La telemetría está en `codeflowx_telemetry` (separada de `codeflowx_governance`)
   - Requiere configuración de datasource adicional
   - Las entidades `AioTelemetry` y `AioIncidentMetric` están en `nocode.service.entitys`

2. **Repositorios Existentes:**
   - `AioTelemetryRepository` existe pero está en el **worker**
   - Necesita crearse en `codeflowx.govern.services` o acceder vía API REST

3. **Servicios Existentes:**
   - `IncidentMetricsService` existe pero está en el **worker**
   - El método `generateMonthlyReport()` ya está implementado, solo falta exponerlo vía UI

4. **Arquitectura Recomendada:**
   - **Opción 3 (API REST Separada)** es la más recomendada para no afectar performance de ingestión
   - Alternativamente, crear servicios en `codeflowx.govern.services` con datasource separado

---

**Última actualización:** 25 de noviembre de 2025
**Total Tareas Pendientes:** 6
**Total Esfuerzo:** 23-34 días
**Prioridad Alta:** 3 tareas (15-19 días)
