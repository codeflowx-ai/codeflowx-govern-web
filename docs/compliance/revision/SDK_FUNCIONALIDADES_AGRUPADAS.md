# SDKs Python/TypeScript - Funcionalidades Agrupadas

**Documento de Revisión** - Antes de Implementación
**Fecha:** 2025-01-XX
**Versión:** 1.0.0

---

## 📋 RESUMEN EJECUTIVO

Este documento agrupa todas las funcionalidades que tendrán los SDKs Python y TypeScript para integración con CodeflowX AI OS. Las funcionalidades están organizadas por categorías lógicas para facilitar la revisión y aprobación antes de la implementación.

**Tecnologías:**
- Python SDK (`codeflowx-sdk-python`)
- TypeScript SDK (`codeflowx-sdk-typescript`)

**Caso de Uso Principal:** Integración de agentes externos desarrollados por partners en sus propias aplicaciones.

---

## 🎯 GRUPO 1: CORE Y CONFIGURACIÓN

### 1.1. Cliente Principal (AgentClient)
**Clase:** `AgentClient`
**Módulo:** `agent_client.py` / `agent-client.ts`

**Responsabilidades:**
- Inicialización con API key (agent_token)
- Gestión de base URL y configuración
- Factory methods para creación desde config
- Gestión de estado interno del cliente

**Métodos:**
- `__init__(api_key, base_url, ...configs)` - Constructor principal
- `from_config(config)` - Factory method desde Config
- `from_env()` - Factory method desde variables de entorno
- `get_version()` - Versión del SDK
- `get_config()` - Obtener configuración actual

**Configuración:**
- API Key (requerido)
- Base URL (default: `https://gateway.codeflowx.ai`)
- Timeout (default: 30s)
- API Version (default: `v1`)
- SSL Verification (default: `true`)

---

### 1.2. Gestión de Configuración
**Clase:** `Config`
**Módulo:** `config.py` / `config.ts`

**Funcionalidades:**
- Carga desde variables de entorno (`CODEFLOWX_API_KEY`, `CODEFLOWX_BASE_URL`)
- Carga desde archivo (YAML/JSON)
- Validación de configuración
- Gestión segura de credenciales (no loggear keys)

**Métodos:**
- `from_env()` - Cargar desde env vars
- `from_file(path)` - Cargar desde archivo
- `validate()` - Validar configuración
- `to_dict()` - Serializar a dict/object

---

## 🔐 GRUPO 2: REGISTRO Y ONBOARDING

### 2.1. Registro de Agentes Externos
**Método:** `register_agent(...)`

**Parámetros:**
- `agent_name` (str) - Nombre del agente
- `agent_type` (str) - Tipo: "custom", "llm", "rag", etc.
- `workspace_uuid` (str) - UUID del workspace
- `capabilities` (Dict) - Capacidades del agente
  - `language`: idioma soportado
  - `domains`: dominios de aplicación
  - `features`: características específicas
- `callback_url` (str, opcional) - URL para webhooks
- `endpoint` (str, opcional) - Endpoint público del agente

**Respuesta:**
- `agent_id` (str) - UUID del agente registrado
- `agent_token` (str) - Token para autenticación
- `workspace_uuid` (str) - UUID del workspace
- `registration_date` (datetime) - Fecha de registro
- `status` (str) - Estado inicial: "ACTIVE", "PENDING_APPROVAL"

**Notas:**
- Solo se ejecuta una vez por agente
- Retorna `agent_token` que se usa para todas las operaciones posteriores
- El agente se registra como `AioComponent` con `type="AGENT"`

---

## 📊 GRUPO 3: TELEMETRÍA Y OBSERVABILIDAD

### 3.1. Envío de Telemetría
**Método:** `send_telemetry(...)`

**Parámetros:**
- `component_uuid` (str) - UUID del agente/componente
- `event_type` (str) - Tipo de evento
  - `AGENT_INVOCATION` - Invocación del agente
  - `AGENT_ERROR` - Error en agente
  - `AGENT_COMPLETION` - Finalización exitosa
  - `AGENT_TIMEOUT` - Timeout del agente
  - `CUSTOM_*` - Eventos personalizados
- `payload` (Dict) - Datos del evento
  - `prompt` (str, opcional) - Prompt enviado
  - `response` (str, opcional) - Respuesta generada
  - `latency_ms` (int, opcional) - Latencia en ms
  - `tokens_used` (int, opcional) - Tokens consumidos
  - `metadata` (Dict, opcional) - Metadatos adicionales
- `pii_filter` (PIIFilter, opcional) - Filtro de PII a aplicar
- `compress` (bool, opcional) - Comprimir payload si > 1KB (default: true)

**Características:**
- Filtrado automático de PII si se proporciona `pii_filter`
- Compresión automática para payloads grandes
- Validación de formato antes de enviar
- Retry automático en caso de fallo

---

### 3.2. Envío Batch de Telemetría
**Método:** `send_telemetry_batch(...)`

**Parámetros:**
- `events` (List[TelemetryEvent]) - Lista de eventos

**Características:**
- Envío eficiente de múltiples eventos
- Batching automático (agrupa hasta N eventos)
- Retry por evento individual
- Validación de cada evento

**Clase TelemetryEvent:**
```python
class TelemetryEvent:
    component_uuid: str
    event_type: str
    payload: Dict
    timestamp: datetime
```

---

### 3.3. Métricas y Observabilidad
**Clase:** `MetricsCollector`
**Módulo:** `metrics.py` / `metrics.ts`

**Funcionalidades:**
- Recopilación de métricas locales
- Exportación a Prometheus/OpenTelemetry
- Métricas de latencia, errores, retries

**Métodos:**
- `record_latency(operation, latency_ms)` - Registrar latencia
- `record_error(operation, error)` - Registrar error
- `record_retry(operation, attempt)` - Registrar retry
- `get_metrics_summary()` - Resumen de métricas
- `export_prometheus()` - Exportar formato Prometheus
- `export_opentelemetry()` - Exportar formato OpenTelemetry

**Métricas Recopiladas:**
- Latencia por operación (p50, p95, p99)
- Tasa de errores por operación
- Número de retries
- Tasa de éxito/fallo
- Throughput (requests/segundo)

---

## 💾 GRUPO 4: MEMORIA CENTRALIZADA

### 4.1. Operaciones de Memoria Básicas
**Métodos:**
- `memory_get(agent_id, namespace, key)` - Obtener valor
- `memory_set(agent_id, namespace, key, value, expires_in_days=None)` - Guardar valor
- `memory_delete(agent_id, namespace, key)` - Eliminar valor
- `memory_list(agent_id, namespace, pattern=None)` - Listar keys

**Parámetros:**
- `agent_id` (str) - UUID del agente
- `namespace` (str) - Namespace de memoria (ej: "conversation", "context", "user_preferences")
- `key` (str) - Clave del valor
- `value` (Any) - Valor a guardar (JSON serializable)
- `expires_in_days` (int, opcional) - Expiración en días

**Características:**
- Serialización automática a JSON
- Validación de tamaño (max 1MB por valor)
- TTL automático si se especifica expiración
- Namespace isolation (un agente no puede acceder a memoria de otro)

---

### 4.2. Operaciones Batch de Memoria
**Método:** `memory_batch_set(agent_id, namespace, entries)`

**Parámetros:**
- `agent_id` (str) - UUID del agente
- `namespace` (str) - Namespace
- `entries` (Dict[str, Any]) - Diccionario de key-value pairs

**Características:**
- Operación atómica (todo o nada)
- Validación de todos los valores antes de guardar
- Eficiente para múltiples escrituras

---

### 4.3. Snapshots de Memoria
**Métodos:**
- `memory_create_snapshot(agent_id)` - Crear snapshot
- `memory_restore_snapshot(agent_id, snapshot_id)` - Restaurar snapshot
- `memory_list_snapshots(agent_id)` - Listar snapshots

**Casos de Uso:**
- Backup antes de cambios importantes
- Rollback a estado anterior
- Migración de contexto entre versiones

**Respuesta de create_snapshot:**
- `snapshot_id` (str) - UUID del snapshot
- `created_at` (datetime) - Fecha de creación
- `size_bytes` (int) - Tamaño del snapshot
- `namespace_count` (int) - Número de namespaces

---

## 🎮 GRUPO 5: SUPERVISOR Y CONTROL REMOTO

### 5.1. Polling de Estado
**Método:** `supervisor_get_status(agent_id)`

**Respuesta:**
- `state` (str) - Estado actual: "ACTIVE", "PAUSED", "TERMINATED", "ERROR"
- `last_update` (datetime) - Última actualización
- `messages` (List[str]) - Mensajes del supervisor
- `actions_required` (List[str]) - Acciones requeridas

**Uso Típico:**
```python
# En thread separado
while True:
    status = client.supervisor_get_status(agent_id)
    if status.state == "PAUSED":
        agent.pause()
    elif status.state == "TERMINATED":
        agent.shutdown()
        break
    time.sleep(30)
```

---

### 5.2. Manejo de Webhooks
**Método:** `supervisor_handle_webhook(action, payload)`

**Acciones Soportadas:**
- `PAUSE` - Pausar agente
- `RESUME` - Reanudar agente
- `TERMINATE` - Terminar agente
- `UPDATE_CONFIG` - Actualizar configuración
- `EXECUTE_COMMAND` - Ejecutar comando personalizado

**Parámetros:**
- `action` (str) - Acción a ejecutar
- `payload` (Dict) - Datos de la acción

**Características:**
- Validación de firma del webhook
- Verificación de origen (IP whitelist)
- Logging de todas las acciones

---

## 📝 GRUPO 6: COMPLIANCE Y AUDITORÍA

### 6.1. Registro Inmutable de Evidencias
**Método:** `audit_log_immutable(...)`

**Parámetros:**
- `component_uuid` (str) - UUID del componente
- `operation` (str) - Operación realizada
- `actor` (str) - Actor que ejecutó la operación
- `metadata` (Dict) - Metadatos adicionales
  - `decision` (str, opcional) - Decisión tomada
  - `reason` (str, opcional) - Razón de la decisión
  - `context` (Dict, opcional) - Contexto adicional

**Características:**
- Registro inmutable (no se puede modificar)
- Hash criptográfico para verificación de integridad
- Cumplimiento Art. 19 EU AI Act
- Retención configurable (mínimo 6 meses)

---

### 6.2. Verificación de Evidencias
**Método:** `verify_evidence(log_uuid)`

**Parámetros:**
- `log_uuid` (str) - UUID del log a verificar

**Respuesta:**
- `is_valid` (bool) - Si el log es válido
- `hash_match` (bool) - Si el hash coincide
- `verified_at` (datetime) - Fecha de verificación
- `chain_valid` (bool) - Si la cadena de hashes es válida

---

## 🛡️ GRUPO 7: POLICY RUNTIME

### 7.1. Verificación de Políticas
**Método:** `policy_check(component_uuid, action, context)`

**Parámetros:**
- `component_uuid` (str) - UUID del componente
- `action` (str) - Acción a verificar: "INVOKE", "TRAIN", "DEPLOY", etc.
- `context` (Dict) - Contexto de la acción
  - `prompt` (str, opcional) - Prompt a verificar
  - `user_id` (str, opcional) - Usuario que ejecuta
  - `workspace_uuid` (str, opcional) - Workspace
  - `metadata` (Dict, opcional) - Metadatos adicionales

**Respuesta:**
- `allowed` (bool) - Si la acción está permitida
- `reason` (str) - Razón de la decisión
- `violations` (List[PolicyViolation]) - Violaciones detectadas
- `required_approvals` (List[str]) - Aprobaciones requeridas

**Características:**
- Caché de resultados (configurable)
- Evaluación en tiempo real
- Integración con Policy Engine (Drools)

---

### 7.2. Validación Pre-flight
**Método:** `policy_pre_check(component_uuid, action, context)`

**Similar a `policy_check` pero:**
- No ejecuta la acción
- Solo valida si sería permitida
- Más rápido (solo lectura)
- Útil para validar antes de ejecutar operaciones costosas

---

## 🔄 GRUPO 8: RESILENCIA

### 8.1. Retry con Backoff Exponencial
**Clase:** `RetryConfig`
**Módulo:** `resilience.py` / `resilience.ts`

**Configuración:**
- `max_attempts` (int, default: 3) - Número máximo de intentos
- `initial_delay_ms` (int, default: 1000) - Delay inicial en ms
- `max_delay_ms` (int, default: 10000) - Delay máximo en ms
- `multiplier` (float, default: 2.0) - Multiplicador exponencial
- `retryable_status_codes` (List[int], default: [503, 502, 429, 500]) - Códigos HTTP que activan retry

**Características:**
- Backoff exponencial con jitter
- Retry solo en errores transitorios
- Logging de cada intento
- Métricas de retries

---

### 8.2. Circuit Breaker
**Clase:** `CircuitBreakerConfig`
**Módulo:** `resilience.py` / `resilience.ts`

**Configuración:**
- `failure_threshold` (int, default: 5) - Umbral de fallos
- `timeout_seconds` (int, default: 60) - Tiempo de espera antes de half-open
- `half_open_max_calls` (int, default: 3) - Máximo de llamadas en half-open

**Estados:**
- `CLOSED` - Normal, todas las llamadas pasan
- `OPEN` - Fallando, todas las llamadas fallan inmediatamente
- `HALF_OPEN` - Probando, permite N llamadas para verificar recuperación

**Características:**
- Protección contra cascading failures
- Recuperación automática
- Métricas de estado del circuit breaker

---

### 8.3. Rate Limiting
**Clase:** `RateLimiterConfig`
**Módulo:** `resilience.py` / `resilience.ts`

**Configuración:**
- `requests_per_second` (int, default: 100) - Requests por segundo
- `burst_size` (int, default: 200) - Tamaño de burst permitido

**Características:**
- Token bucket algorithm
- Respeta límites del servidor
- Retry automático cuando hay rate limit (429)
- Métricas de rate limiting

---

## 💿 GRUPO 9: CACHÉ Y OFFLINE MODE

### 9.1. Caché Local
**Clase:** `CacheConfig`
**Módulo:** `cache.py` / `cache.ts`

**Configuración:**
- `enabled` (bool, default: true) - Habilitar caché
- `ttl_seconds` (int, default: 300) - TTL en segundos (5 minutos)
- `max_size` (int, default: 1000) - Tamaño máximo de caché
- `cache_policy_checks` (bool, default: true) - Cachear policy checks

**Operaciones Cacheables:**
- Policy checks (si `cache_policy_checks=True`)
- Health checks
- Status checks
- Memory reads (opcional)

**Características:**
- LRU eviction policy
- Invalidación automática por TTL
- Invalidación manual por key
- Métricas de hit/miss ratio

---

### 9.2. Modo Offline
**Clase:** `OfflineQueue`
**Módulo:** `cache.py` / `cache.ts`

**Configuración:**
- `max_queue_size` (int, default: 10000) - Tamaño máximo de cola
- `persist_to_disk` (bool, default: true) - Persistir a disco
- `retry_on_reconnect` (bool, default: true) - Reintentar al reconectar

**Operaciones en Cola:**
- Telemetría
- Memory writes
- Audit logs

**Características:**
- Cola FIFO
- Persistencia opcional a disco
- Reintento automático al reconectar
- Priorización de eventos críticos
- Métricas de cola (tamaño, tiempo en cola)

---

## 🔒 GRUPO 10: SEGURIDAD Y PRIVACIDAD

### 10.1. Filtrado de PII
**Clase:** `PIIFilter`
**Módulo:** `pii_filter.py` / `pii-filter.ts`

**Configuración:**
- `enabled` (bool, default: true) - Habilitar filtrado
- `redaction_mode` (str, default: "MASK") - Modo de redacción
  - `MASK` - Enmascarar (ej: `***@***.com`)
  - `HASH` - Hash SHA-256
  - `REMOVE` - Eliminar campo
- `patterns` (List[str], default: ["email", "phone", "ssn", "credit_card"]) - Patrones a detectar

**Patrones Soportados:**
- Email
- Phone (múltiples formatos)
- SSN/SSN-like
- Credit Card
- IP Address
- Custom regex patterns

**Métodos:**
- `filter_payload(payload)` - Filtrar payload completo
- `filter_string(text)` - Filtrar string
- `detect_pii(payload)` - Detectar PII sin filtrar

**Características:**
- Detección automática de PII
- Filtrado antes de enviar telemetría
- Logging de detecciones (opcional)
- Configuración por tipo de PII

---

## 📡 GRUPO 11: EVENTOS Y CALLBACKS

### 11.1. Sistema de Eventos
**Clase:** `AgentEventListener`
**Módulo:** `events.py` / `events.ts`

**Eventos Soportados:**
- `on_status_changed(agent_id, old_status, new_status)` - Cambio de estado
- `on_policy_violation(agent_id, violation)` - Violación de política
- `on_memory_updated(agent_id, namespace, key)` - Memoria actualizada
- `on_telemetry_sent(agent_id, event_type)` - Telemetría enviada
- `on_error(agent_id, error)` - Error en agente

**Uso:**
```python
class MyEventListener(AgentEventListener):
    def on_status_changed(self, agent_id, old_status, new_status):
        print(f"Agent {agent_id} changed from {old_status} to {new_status}")

listener = MyEventListener()
client.add_event_listener(listener)
```

**Características:**
- Múltiples listeners
- Eventos asíncronos (no bloquean)
- Manejo de errores en listeners
- Desregistro de listeners

---

## 🔌 GRUPO 12: INTEGRACIONES Y DECORADORES

### 12.1. Decoradores para Frameworks
**Módulo:** `decorators.py` / `decorators.ts`

**Decoradores Disponibles:**

#### `@telemetry_required`
```python
@telemetry_required(component_uuid="agent-uuid-123")
def my_agent_function(prompt: str):
    return agent.invoke(prompt)
```
- Reporta telemetría automáticamente
- Captura latencia, errores, tokens

#### `@policy_check_required`
```python
@policy_check_required(component_uuid="agent-uuid-123", action="INVOKE")
def my_agent_function(prompt: str):
    return agent.invoke(prompt)
```
- Valida políticas antes de ejecutar
- Lanza excepción si no está permitido

#### `@audit_required`
```python
@audit_required(component_uuid="agent-uuid-123", operation="INVOKE")
def my_agent_function(prompt: str):
    return agent.invoke(prompt)
```
- Registra en ImmutableLog automáticamente

**Frameworks Soportados:**
- FastAPI (Python)
- Flask (Python)
- Express.js (TypeScript)
- Next.js (TypeScript)

---

## ✅ GRUPO 13: VALIDACIÓN Y UTILIDADES

### 13.1. Validación de Datos
**Clase:** `ValidationError`
**Módulo:** `validation.py` / `validation.ts`

**Validaciones:**
- Formato JSON
- Tamaño máximo de payloads
- Tipos de datos
- UUIDs válidos
- Strings no vacíos
- Números en rango válido

**Métodos:**
- `validate_payload(payload)` - Validar payload completo
- `validate_uuid(uuid)` - Validar UUID
- `validate_size(data, max_size)` - Validar tamaño

**Características:**
- Validación antes de enviar
- Mensajes de error descriptivos
- Validación opcional (configurable)

---

### 13.2. Compresión de Payloads
**Funcionalidad:** Automática en `send_telemetry`

**Configuración:**
- `compress` (bool, default: true) - Habilitar compresión
- `min_size_bytes` (int, default: 1024) - Tamaño mínimo para comprimir

**Algoritmo:**
- gzip para Python
- zlib para TypeScript

**Características:**
- Compresión automática si payload > 1KB
- Descompresión automática en servidor
- Headers HTTP apropiados

---

## 🏥 GRUPO 14: HEALTH CHECKS

### 14.1. Health Check
**Método:** `health()`

**Respuesta:**
- `status` (str) - "healthy", "degraded", "unhealthy"
- `version` (str) - Versión del SDK
- `uptime_seconds` (int) - Tiempo activo
- `last_successful_request` (datetime) - Última request exitosa

---

### 14.2. Readiness Check (K8s)
**Método:** `ready()`

**Respuesta:**
- `ready` (bool) - Si está listo
- `checks` (Dict) - Estado de checks individuales
  - `api_connection` (bool)
  - `cache` (bool)
  - `offline_queue` (bool)

**Uso en K8s:**
```yaml
livenessProbe:
  exec:
    command: ["python", "-c", "from codeflowx_sdk import AgentClient; AgentClient(...).health()"]
readinessProbe:
  exec:
    command: ["python", "-c", "from codeflowx_sdk import AgentClient; AgentClient(...).ready()"]
```

---

## 📦 GRUPO 15: OPERACIONES BATCH

### 15.1. Batch Operations
**Métodos:**
- `send_telemetry_batch(events)` - Ya descrito en Grupo 3
- `memory_batch_set(agent_id, namespace, entries)` - Ya descrito en Grupo 4

**Características Comunes:**
- Eficiencia para múltiples operaciones
- Validación antes de ejecutar
- Retry por operación individual
- Métricas agregadas

---

## 📚 GRUPO 16: DOCUMENTACIÓN Y EJEMPLOS

### 16.1. Documentación Incluida
- **README.md** - Guía principal
- **QUICKSTART.md** - Guía rápida de inicio
- **API_REFERENCE.md** - Referencia completa de API
- **EXAMPLES/** - Ejemplos de código
  - `quickstart.py` / `quickstart.ts`
  - `fastapi_example.py`
  - `flask_example.py`
  - `express_example.ts`
  - `jupyter_example.py`
- **GUIDES/** - Guías específicas
  - `integration_guide.md` - Guía de integración
  - `resilience_guide.md` - Guía de resiliencia
  - `compliance_guide.md` - Guía de compliance
  - `pii_filtering_guide.md` - Guía de filtrado PII
  - `memory_guide.md` - Guía de memoria centralizada

---

## 🎯 RESUMEN DE GRUPOS

| Grupo | Funcionalidades | Prioridad | Complejidad |
|-------|----------------|-----------|-------------|
| 1. Core y Configuración | Cliente principal, Config | 🔴 ALTA | Baja |
| 2. Registro y Onboarding | Registro de agentes | 🔴 ALTA | Media |
| 3. Telemetría | Envío de telemetría, batch, métricas | 🔴 ALTA | Media |
| 4. Memoria Centralizada | CRUD memoria, snapshots | 🟡 MEDIA | Media |
| 5. Supervisor | Polling, webhooks | 🟡 MEDIA | Alta |
| 6. Compliance | Audit logs, verificación | 🔴 ALTA | Media |
| 7. Policy Runtime | Verificación de políticas | 🔴 ALTA | Alta |
| 8. Resiliencia | Retry, circuit breaker, rate limit | 🟡 MEDIA | Alta |
| 9. Caché y Offline | Caché local, cola offline | 🟢 BAJA | Alta |
| 10. Seguridad | Filtrado PII | 🟡 MEDIA | Media |
| 11. Eventos | Callbacks y listeners | 🟢 BAJA | Baja |
| 12. Integraciones | Decoradores, frameworks | 🟢 BAJA | Media |
| 13. Validación | Validación de datos | 🟡 MEDIA | Baja |
| 14. Health Checks | Health, readiness | 🟡 MEDIA | Baja |
| 15. Batch Operations | Operaciones batch | 🟢 BAJA | Media |
| 16. Documentación | Docs y ejemplos | 🔴 ALTA | Baja |

---

## ❓ PREGUNTAS PARA REVISIÓN

1. **¿Faltan funcionalidades críticas?**
2. **¿Alguna funcionalidad es innecesaria o debe ser opcional?**
3. **¿Los nombres de métodos/clases son claros?**
4. **¿Las configuraciones por defecto son apropiadas?**
5. **¿Falta alguna integración específica (framework, herramienta)?**
6. **¿Los casos de uso están bien cubiertos?**
7. **¿La priorización de grupos es correcta?**

---

## 📝 NOTAS DE IMPLEMENTACIÓN

- **Python SDK:** Usar `requests` o `httpx` para HTTP, `pydantic` para validación
- **TypeScript SDK:** Usar `axios` o `fetch` para HTTP, `zod` para validación
- **Compatibilidad:** Ambos SDKs deben tener la misma API (mismos métodos, mismos parámetros)
- **Versionado:** Seguir Semantic Versioning (MAJOR.MINOR.PATCH)
- **Testing:** Unit tests + Integration tests + Smoke tests
- **CI/CD:** Tests automáticos en cada commit

---

**Última Actualización:** 2025-01-XX
**Próximo Paso:** Revisión y aprobación de funcionalidades antes de implementación
