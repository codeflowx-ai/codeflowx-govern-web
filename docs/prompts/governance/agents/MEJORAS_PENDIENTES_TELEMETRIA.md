# 🔧 MEJORAS PENDIENTES: CORRELACIÓN TELEMETRÍA + PROMETHEUS

**Versión:** 1.0
**Fecha:** Enero 2025
**Estado:** ✅ Prometheus implementado | ⚠️ Falta correlación
**Proyecto:** CodeFlowX AI Governance Platform

---

## 🎯 SITUACIÓN ACTUAL

### ✅ LO QUE YA TIENEN

1. **Prometheus implementado en AI OS:**
   - ✅ Métricas de sistema disponibles
   - ✅ Captura de procesos del SO
   - ✅ Métricas de CPU, memoria, I/O, red

2. **Cálculos a nivel de proyecto:**
   - ✅ Costes de proyecto
   - ✅ CPUs de proyecto
   - ✅ Métricas agregadas por proyecto

3. **Telemetría de aplicación:**
   - ✅ Eventos de interacción, decisión, alerta
   - ✅ Base de datos de telemetría separada (10M eventos/día)
   - ✅ Trazas de ejecución con pasos y acciones

### ❌ LO QUE FALTA

**Correlación entre telemetría de aplicación y métricas Prometheus**

---

## 🔗 CORRELACIÓN NECESARIA

### 1. Vincular Eventos con Métricas Prometheus

**Problema:** Los eventos de telemetría y las métricas Prometheus están separados, no se correlacionan.

**Solución:**

```java
// En eventos de telemetría, agregar:
public class AgentInteractionEvent {
    // ... campos existentes
    private String eventId;  // UUID único del evento

    // NUEVO: Correlación con Prometheus
    private String prometheusQueryId;  // ID para consultar Prometheus
    private Map<String, Object> prometheusMetrics;  // Métricas correlacionadas
    private LatencyBreakdown latencyBreakdown;  // Desglose desde Prometheus
    private SystemProcessInfo systemProcesses;  // Procesos iniciados
}
```

**Implementación:**

```java
@Service
public class TelemetryPrometheusCorrelationService {

    @Autowired
    private PrometheusClient prometheusClient;

    /**
     * Correlaciona evento de telemetría con métricas Prometheus
     */
    public void correlateEventWithPrometheus(TelemetryEvent event) {
        // 1. Obtener métricas Prometheus para el timestamp del evento
        Instant eventTimestamp = event.getTimestamp();
        String agentUuid = event.getAgentUuid();

        // 2. Consultar Prometheus para métricas de sistema
        PrometheusMetrics metrics = prometheusClient.queryRange(
            "agent_cpu_usage{agent_uuid=\"" + agentUuid + "\"}",
            eventTimestamp.minusSeconds(5),  // 5 segundos antes
            eventTimestamp.plusSeconds(5)    // 5 segundos después
        );

        // 3. Obtener procesos iniciados por el agente
        List<ProcessInfo> processes = prometheusClient.query(
            "process_started_by_agent{agent_uuid=\"" + agentUuid + "\"}",
            eventTimestamp
        );

        // 4. Calcular desglose de latencia
        LatencyBreakdown latency = calculateLatencyBreakdown(metrics);

        // 5. Agregar a evento
        event.setPrometheusMetrics(metrics);
        event.setSystemProcesses(processes);
        event.setLatencyBreakdown(latency);
    }

    private LatencyBreakdown calculateLatencyBreakdown(PrometheusMetrics metrics) {
        return LatencyBreakdown.builder()
            .totalDurationMs(metrics.getTotalDuration())
            .cpuTimeMs(metrics.getCpuTime())
            .ioWaitTimeMs(metrics.getIoWaitTime())
            .networkLatencyMs(metrics.getNetworkLatency())
            .queueWaitTimeMs(metrics.getQueueWaitTime())
            .build();
    }
}
```

---

### 2. Desglose de Latencia en Eventos

**Problema:** Los eventos solo tienen `duration_ms` total, no desglose (CPU, I/O, red).

**Solución:**

```java
public class LatencyBreakdown {
    private Long totalDurationMs;      // Duración total (ya existe)
    private Long cpuTimeMs;            // Tiempo de CPU (desde Prometheus)
    private Long ioWaitTimeMs;        // Tiempo de espera I/O (desde Prometheus)
    private Long networkLatencyMs;     // Latencia de red (desde Prometheus)
    private Long queueWaitTimeMs;      // Tiempo en cola (desde Prometheus)
    private Long otherTimeMs;          // Otro tiempo
}
```

**Agregar a eventos:**

```java
// En AgentInteractionEvent
public class AgentInteractionEvent {
    // ... campos existentes
    private Integer durationMs;  // Ya existe

    // NUEVO: Desglose de latencia
    private LatencyBreakdown latencyBreakdown;
}
```

**Query Prometheus:**

```promql
# CPU time
agent_cpu_time_seconds{agent_uuid="..."} - agent_cpu_time_seconds{agent_uuid="...", timestamp="..."} offset 1s

# I/O wait time
agent_io_wait_time_seconds{agent_uuid="..."}

# Network latency
agent_network_latency_seconds{agent_uuid="..."}
```

---

### 3. Procesos Iniciados por Agente

**Problema:** No se correlaciona qué procesos del SO inició el agente.

**Solución:**

```java
public class SystemProcessInfo {
    private String processId;              // PID del proceso del agente
    private String parentProcessId;         // PPID
    private List<ChildProcess> childProcesses;  // Procesos hijos iniciados
    private Map<String, Long> systemCalls;      // Syscalls realizados
    private List<FileAccess> fileAccesses;       // Accesos a archivos
    private List<NetworkConnection> networkConnections;  // Conexiones de red
}

public class ChildProcess {
    private String processId;
    private String command;
    private Instant startedAt;
    private Instant finishedAt;
    private Integer exitCode;
}
```

**Query Prometheus:**

```promql
# Procesos iniciados por agente
process_started_by_agent{agent_uuid="...", parent_pid="..."}

# Syscalls
syscalls_by_agent{agent_uuid="...", syscall="..."}

# File accesses
file_accesses_by_agent{agent_uuid="...", file_path="..."}

# Network connections
network_connections_by_agent{agent_uuid="...", remote_address="..."}
```

---

### 4. Correlación Costes Proyecto ↔ Agente

**Problema:** Tienen costes a nivel de proyecto, pero no se correlaciona con costes de agente en eventos individuales.

**Solución:**

```java
// En eventos de telemetría, agregar:
public class AgentInteractionEvent {
    // ... campos existentes
    private BigDecimal cost;  // Coste del evento (ya existe)

    // NUEVO: Correlación con proyecto
    private Long projectId;              // ID del proyecto
    private String projectName;          // Nombre del proyecto
    private BigDecimal projectCostShare;  // Porción del coste del proyecto
    private Map<String, Object> projectMetrics;  // Métricas del proyecto
}
```

**Implementación:**

```java
@Service
public class ProjectAgentCorrelationService {

    @Autowired
    private ProjectAgentRepository projectAgentRepository;

    @Autowired
    private ProjectMetricsService projectMetricsService;

    /**
     * Correlaciona evento de agente con métricas de proyecto
     */
    public void correlateWithProject(TelemetryEvent event) {
        // 1. Obtener proyectos asociados al agente
        List<ProjectAgent> projects = projectAgentRepository.findByAgentUuid(
            event.getAgentUuid()
        );

        // 2. Para cada proyecto, calcular porción del coste
        for (ProjectAgent project : projects) {
            // Calcular coste del proyecto en el período del evento
            BigDecimal projectCost = projectMetricsService.getCostForPeriod(
                project.getProjectId(),
                event.getTimestamp().minusHours(1),
                event.getTimestamp().plusHours(1)
            );

            // Calcular porción del coste del agente
            BigDecimal agentCostShare = calculateCostShare(
                event.getCost(),
                projectCost,
                project.getUsageType()  // PRIMARY, SECONDARY, etc.
            );

            // Agregar a evento
            event.setProjectId(project.getProjectId());
            event.setProjectName(project.getProjectName());
            event.setProjectCostShare(agentCostShare);
        }
    }
}
```

---

### 5. Dashboard Combinado

**Problema:** No hay dashboard que combine telemetría de aplicación con métricas Prometheus.

**Solución:**

Crear dashboard que muestre:

1. **Vista de Agente:**
   - Métricas de aplicación (interacciones, decisiones, alertas)
   - Métricas Prometheus (CPU, memoria, I/O, red)
   - Procesos iniciados por el agente
   - Desglose de latencia (CPU, I/O, red)
   - Costes de agente vs costes de proyecto

2. **Vista de Proyecto:**
   - Costes totales del proyecto
   - Costes por agente (desglose)
   - CPUs por agente
   - Métricas Prometheus agregadas por proyecto

3. **Vista de Evento Individual:**
   - Detalles del evento (aplicación)
   - Métricas Prometheus en el momento del evento
   - Procesos iniciados
   - Desglose de latencia
   - Coste del evento y porción del proyecto

---

## 📋 IMPLEMENTACIÓN PASO A PASO

### Paso 1: Agregar Campos de Correlación

```java
// Actualizar AgentInteractionEvent
public class AgentInteractionEvent {
    // ... campos existentes

    // Correlación Prometheus
    private String prometheusQueryId;
    private Map<String, Object> prometheusMetrics;
    private LatencyBreakdown latencyBreakdown;
    private SystemProcessInfo systemProcesses;

    // Correlación Proyecto
    private Long projectId;
    private String projectName;
    private BigDecimal projectCostShare;
}
```

### Paso 2: Servicio de Correlación

```java
@Service
public class TelemetryCorrelationService {

    @Autowired
    private PrometheusClient prometheusClient;

    @Autowired
    private ProjectAgentRepository projectAgentRepository;

    /**
     * Correlaciona evento con Prometheus y proyecto
     */
    public void correlateEvent(TelemetryEvent event) {
        // 1. Correlación con Prometheus
        correlateWithPrometheus(event);

        // 2. Correlación con proyecto
        correlateWithProject(event);
    }
}
```

### Paso 3: Actualizar Procesamiento de Eventos

```java
@Service
public class AgentTelemetryService {

    @Autowired
    private TelemetryCorrelationService correlationService;

    private void processInteractionEvent(TelemetryEvent event) {
        // 1. Mapear a entidad
        AgentInteractionEvent interactionEvent = mapToInteractionEvent(event);

        // 2. Correlacionar con Prometheus y proyecto
        correlationService.correlateEvent(interactionEvent);

        // 3. Guardar en base de datos de telemetría
        telemetryRepository.save(interactionEvent);
    }
}
```

### Paso 4: Cliente Prometheus

```java
@Service
public class PrometheusClient {

    private final String prometheusUrl;

    /**
     * Consulta métricas de Prometheus
     */
    public PrometheusMetrics queryRange(String query, Instant start, Instant end) {
        // Implementar consulta a Prometheus
        // Retornar métricas correlacionadas
    }

    /**
     * Consulta procesos iniciados por agente
     */
    public List<ProcessInfo> queryProcesses(String agentUuid, Instant timestamp) {
        // Query: process_started_by_agent{agent_uuid="..."}
    }
}
```

---

## 📊 QUERIES PROMETHEUS NECESARIAS

### Métricas de Sistema por Agente

```promql
# CPU usage
agent_cpu_usage_percent{agent_uuid="..."}

# Memory usage
agent_memory_usage_bytes{agent_uuid="..."}

# I/O wait time
agent_io_wait_time_seconds{agent_uuid="..."}

# Network latency
agent_network_latency_seconds{agent_uuid="..."}

# Queue wait time
agent_queue_wait_time_seconds{agent_uuid="..."}
```

### Procesos Iniciados

```promql
# Procesos hijos iniciados por agente
process_started_by_agent{agent_uuid="...", parent_pid="..."}

# Syscalls realizados
syscalls_by_agent{agent_uuid="..."}

# File accesses
file_accesses_by_agent{agent_uuid="..."}

# Network connections
network_connections_by_agent{agent_uuid="..."}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Correlación Prometheus

- [ ] Crear `PrometheusClient` para consultar métricas
- [ ] Agregar `prometheusMetrics` en eventos de telemetría
- [ ] Agregar `latencyBreakdown` en eventos
- [ ] Agregar `systemProcesses` en eventos
- [ ] Implementar correlación en `TelemetryCorrelationService`
- [ ] Actualizar procesamiento de eventos para incluir correlación

### Correlación Proyecto

- [ ] Agregar `projectId`, `projectName`, `projectCostShare` en eventos
- [ ] Implementar `ProjectAgentCorrelationService`
- [ ] Calcular porción de coste del proyecto por evento
- [ ] Vincular CPUs de proyecto con eventos de agente

### Dashboard

- [ ] Crear vista de agente con métricas combinadas
- [ ] Crear vista de proyecto con desglose por agente
- [ ] Crear vista de evento individual con correlaciones
- [ ] Integrar gráficos Prometheus en dashboard

---

## 🚨 CONSIDERACIONES DE RENDIMIENTO

### Consultas Prometheus

- **Caché:** Cachear métricas Prometheus por ventana de tiempo
- **Batch:** Agregar múltiples eventos antes de consultar Prometheus
- **Async:** Consultas asíncronas para no bloquear procesamiento
- **Timeouts:** Timeouts cortos para consultas Prometheus

### Base de Datos

- **Índices:** Índices sobre `event_id`, `agent_uuid`, `project_id`, `timestamp`
- **Particionamiento:** Particionar por fecha para mejor rendimiento
- **Agregaciones:** Pre-calcular agregaciones para dashboards

---

## 📚 REFERENCIAS

- **Prometheus Query API:** https://prometheus.io/docs/prometheus/latest/querying/api/
- **PromQL:** https://prometheus.io/docs/prometheus/latest/querying/basics/
- **OpenTelemetry:** https://opentelemetry.io/ (si se requiere más detalle)

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** ⚠️ Pendiente de implementación
