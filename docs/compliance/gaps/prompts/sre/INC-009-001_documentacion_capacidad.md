# PROMPT: INC-009-001 - Documentación Formal de Capacidad
## Monitorización en Tiempo Real - EU AI Act Art. 12, Art. 15

**Incidencia:** INC-009-001  
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12, Art. 15  
**Estado:** 🔴 PENDIENTE

---

## DESCRIPCIÓN

Realizar tests de carga formales y documentar límites reales medidos del sistema de telemetría: throughput máximo sostenido, latencia bajo carga, punto de degradación, etc.

---

## REQUISITOS

### 1. Plan de Tests de Carga

**Herramientas:**
- JMeter, Gatling, o k6
- Prometheus para métricas
- Grafana para visualización

**Escenarios:**

#### Test 1: Throughput Sostenido
- **Duración:** 1 hora
- **Target:** 1000 eventos/segundo
- **Métricas:**
  - Latency p95 < 500ms
  - Latency p99 < 1000ms
  - Error rate < 0.1%
  - Throughput estable (sin degradación)

#### Test 2: Pico de Carga
- **Duración:** 10 minutos
- **Target:** 10000 eventos/segundo
- **Métricas:**
  - Sistema no se cae
  - Recovery time < 5 minutos después del pico
  - Pérdida de mensajes < 0.01%

#### Test 3: Carga Extrema
- **Duración:** 5 minutos
- **Target:** 50000 eventos/segundo
- **Métricas:**
  - Throttling activado correctamente
  - Mensajes en DLQ < 1%
  - Sistema se recupera automáticamente

### 2. Documentación de Límites

**Archivo:** `docs/compliance/telemetry/CAPACITY_LIMITS.md`

**Contenido:**
- Throughput máximo sostenido (eventos/segundo)
- Throughput máximo en pico (eventos/segundo)
- Latencia p95 bajo carga normal (ms)
- Latencia p99 bajo carga normal (ms)
- Latencia p95 bajo carga extrema (ms)
- Punto de degradación (eventos/segundo)
- Capacidad de RabbitMQ (mensajes/segundo)
- Capacidad de Workers (eventos procesados/segundo)
- Capacidad de PostgreSQL (inserciones/segundo)

### 3. SLA de Throughput

**Archivo:** `docs/compliance/telemetry/SLA_THROUGHPUT.md`

**SLA Definido:**
- **Throughput garantizado:** 1000 eventos/segundo
- **Throughput máximo:** 10000 eventos/segundo (con throttling)
- **Latencia p95 garantizada:** < 500ms
- **Latencia p99 garantizada:** < 1000ms
- **Disponibilidad:** 99.9% (8.76 horas downtime/año)

### 4. Estrategia de Throttling

**Archivo:** `docs/compliance/telemetry/THROTTLING_STRATEGY.md`

**Estrategia:**
- **Rate limiting:** 1000 eventos/segundo por cliente
- **Backpressure:** Rechazar cuando cola > 80% capacidad
- **Circuit breaker:** Activar si error rate > 5%
- **Retry:** 3 intentos con backoff exponencial

---

## SCRIPTS DE TEST

### Test con k6

**Archivo:** `tests/load/telemetry_throughput_test.js`

```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

export const options = {
  stages: [
    { duration: '5m', target: 1000 },  // Ramp up a 1000 eventos/seg
    { duration: '1h', target: 1000 },  // Mantener 1000 eventos/seg
    { duration: '5m', target: 0 },     // Ramp down
  ],
  thresholds: {
    'http_req_duration': ['p(95)<500', 'p(99)<1000'],
    'errors': ['rate<0.001'],
  },
};

export default function () {
  const payload = JSON.stringify({
    component_uuid: 'test-component-uuid',
    event_type: 'INTERACTION_COMPLETED',
    payload: {
      user_query: 'Test query',
      model_response: 'Test response',
    },
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post('http://localhost:8080/api/v1/aios/telemetry/events', payload, params);
  
  const result = check(res, {
    'status is 202': (r) => r.status === 202,
  });
  
  errorRate.add(!result);
  sleep(0.1); // 10 eventos/segundo por VU
}
```

---

## MÉTRICAS A CAPTURAR

### Durante Tests

- `telemetry.ingestion.rate`: Eventos recibidos/segundo
- `telemetry.processing.rate`: Eventos procesados/segundo
- `telemetry.queue.size`: Tamaño de cola RabbitMQ
- `telemetry.latency.p95`: Latencia p95 (ms)
- `telemetry.latency.p99`: Latencia p99 (ms)
- `telemetry.error.rate`: Tasa de error (%)
- `telemetry.rejected.count`: Contador de requests rechazados
- `telemetry.workers.active`: Workers activos
- `telemetry.workers.cpu`: CPU usage de workers (%)
- `telemetry.workers.memory`: Memory usage de workers (MB)

---

## REPORTE DE TESTS

**Archivo:** `docs/compliance/telemetry/LOAD_TEST_REPORT.md`

**Contenido:**
- Fecha de ejecución
- Configuración del test
- Resultados por escenario
- Gráficas de métricas
- Análisis de resultados
- Límites identificados
- Recomendaciones

---

## MONITOREO CONTINUO

### Dashboard Grafana

**Panel 1: Throughput**
- Eventos recibidos/segundo
- Eventos procesados/segundo
- Límite de capacidad

**Panel 2: Latencia**
- Latencia p50, p95, p99
- Latencia por endpoint

**Panel 3: Cola RabbitMQ**
- Tamaño de cola
- Uso de cola (%)
- Mensajes procesados/segundo

**Panel 4: Workers**
- Workers activos
- CPU usage
- Memory usage
- Lag de procesamiento

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md#inc-009-001`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`

---

**Estado:** 🔴 PENDIENTE  
**Esfuerzo Estimado:** 2-3 días  
**Responsable:** SRE Team + Backend Team

