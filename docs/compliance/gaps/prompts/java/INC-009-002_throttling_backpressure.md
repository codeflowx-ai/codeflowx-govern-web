# PROMPT: INC-009-002 - Throttling y Backpressure
## Monitorización en Tiempo Real - EU AI Act Art. 12

**Incidencia:** INC-009-002  
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12  
**Estado:** ✅ COMPLETADO (código aplicado)

---

## DESCRIPCIÓN

Implementar mecanismos de throttling y backpressure en el sistema de telemetría para prevenir saturación de RabbitMQ y degradación de servicio bajo carga extrema.

---

## CAMBIOS APLICADOS

### 1. TelemetryController - Verificación de Capacidad de Cola

**Archivo:** `codeflowx-aios-telemetry/src/main/java/com/codeflowx/aios/telemetry/controller/TelemetryController.java`

**Cambios:**
- ✅ Agregado `RabbitAdmin` y `RabbitTemplate` para verificar tamaño de cola
- ✅ Agregado propiedades de configuración:
  - `telemetry.queue.max-size` (default: 100000)
  - `telemetry.queue.warning-threshold` (default: 0.8)
- ✅ Método `getRabbitMQQueueSize()` para obtener tamaño actual de cola
- ✅ Verificación de capacidad antes de aceptar eventos:
  - Si cola > 80%: Log warning
  - Si cola >= 100%: Rechazar con HTTP 503 (Service Unavailable)
- ✅ Respuesta 503 incluye:
  - `error`: "Service temporarily unavailable"
  - `reason`: "Queue is full"
  - `retry_after`: 60 segundos
  - `queue_size`: Tamaño actual
  - `max_queue_size`: Tamaño máximo

### 2. Configuración RabbitMQ - Backpressure

**Archivo:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/config/RabbitMQConfig.java`

**Cambios requeridos:**
```java
@Bean
public Queue telemetryQueue() {
    return QueueBuilder.durable(TELEMETRY_QUEUE)
        .maxLength(100000)  // Máximo mensajes en cola
        .maxLengthBytes(1073741824)  // 1GB máximo
        .overflow(QueueBuilder.Overflow.rejectPublish)  // Rechazar si llena
        .build();
}
```

**Archivo:** `application.yml`

**Cambios requeridos:**
```yaml
telemetry:
  queue:
    max-size: ${TELEMETRY_QUEUE_MAX_SIZE:100000}
    warning-threshold: ${TELEMETRY_QUEUE_WARNING_THRESHOLD:0.8}
```

---

## TESTING

### Test 1: Verificación de Capacidad de Cola

```java
@Test
void testQueueCapacityCheck() {
    // Simular cola al 90%
    when(rabbitAdmin.getQueueInfo("aios.telemetry.events"))
        .thenReturn(new QueueInformation(90000, 0));
    
    // Enviar evento
    ResponseEntity<TelemetryEventResponseDto> response = 
        controller.registerEvent(Mono.just(createTestEvent())).block();
    
    // Verificar que se rechaza con 503
    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
}
```

### Test 2: Aceptación Normal

```java
@Test
void testNormalAcceptance() {
    // Simular cola al 50%
    when(rabbitAdmin.getQueueInfo("aios.telemetry.events"))
        .thenReturn(new QueueInformation(50000, 0));
    
    // Enviar evento
    ResponseEntity<TelemetryEventResponseDto> response = 
        controller.registerEvent(Mono.just(createTestEvent())).block();
    
    // Verificar que se acepta con 202
    assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
}
```

---

## CONFIGURACIÓN

### Variables de Entorno

```bash
# Tamaño máximo de cola RabbitMQ
TELEMETRY_QUEUE_MAX_SIZE=100000

# Umbral de warning (0.0 - 1.0)
TELEMETRY_QUEUE_WARNING_THRESHOLD=0.8
```

---

## MONITOREO

### Métricas a Monitorear

- `telemetry.queue.size`: Tamaño actual de cola
- `telemetry.queue.usage`: Porcentaje de uso (0.0 - 1.0)
- `telemetry.rejected.requests`: Contador de requests rechazados (503)
- `telemetry.accepted.requests`: Contador de requests aceptados (202)

### Alertas

- **Warning:** Cola > 80% capacidad
- **Critical:** Cola > 95% capacidad
- **Critical:** Requests rechazados > 100/minuto

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md#inc-009-002`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`

---

**Estado:** ✅ COMPLETADO  
**Fecha:** 2025-11-17

