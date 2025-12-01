# SDK Python - Guía Completa

SDK oficial Python para integración con CodeflowX AI OS.

---

## 📦 Instalación

```bash
pip install codeflowx-sdk-python
```

**Requisitos:** Python 3.8+

---

## 🚀 Inicio Rápido

```python
from codeflowx_sdk import AgentClient

# Inicializar cliente
client = AgentClient(api_key="cfx_sk_live_abc123...")

# Registrar agente
registration = client.register_agent(
    agent_name="mi-agente",
    agent_type="custom",
    workspace_uuid="workspace-uuid",
    capabilities={"language": "es"}
)

# Usar agent_token para operaciones
agent_client = AgentClient(api_key=registration["agent_token"])
```

---

## 📚 API Reference

### AgentClient

#### Constructor

```python
AgentClient(
    api_key: str,
    base_url: str = "https://gateway.codeflowx.ai",
    retry_config: Optional[RetryConfig] = None,
    circuit_breaker_config: Optional[CircuitBreakerConfig] = None,
    rate_limiter_config: Optional[RateLimiterConfig] = None,
    cache_config: Optional[CacheConfig] = None,
    offline_queue: Optional[OfflineQueue] = None,
    pii_filter: Optional[PIIFilter] = None,
    metrics_collector: Optional[MetricsCollector] = None
)
```

#### Métodos Principales

##### Registro y Onboarding

```python
register_agent(
    agent_name: str,
    agent_type: str,
    workspace_uuid: str,
    capabilities: Dict[str, Any],
    callback_url: Optional[str] = None,
    endpoint: Optional[str] = None
) -> Dict[str, Any]
```

**Ejemplo:**
```python
registration = client.register_agent(
    agent_name="customer-service-agent",
    agent_type="custom",
    workspace_uuid="workspace-uuid",
    capabilities={
        "language": "es",
        "domains": ["customer_service"],
        "features": ["chat", "qa"]
    },
    callback_url="https://mi-app.com/webhooks/codeflowx"
)

agent_id = registration["agent_id"]
agent_token = registration["agent_token"]
```

---

##### Telemetría

```python
send_telemetry(
    component_uuid: str,
    event_type: str,
    payload: Dict[str, Any],
    pii_filter: Optional[PIIFilter] = None,
    compress: bool = True
) -> None
```

**Eventos soportados:**
- `AGENT_INVOCATION` - Invocación del agente
- `AGENT_ERROR` - Error en agente
- `AGENT_COMPLETION` - Finalización exitosa
- `AGENT_TIMEOUT` - Timeout del agente
- `CUSTOM_*` - Eventos personalizados

**Ejemplo:**
```python
agent_client.send_telemetry(
    component_uuid=agent_id,
    event_type="AGENT_INVOCATION",
    payload={
        "prompt": "Hello",
        "response": "Hi there",
        "latency_ms": 245,
        "tokens_used": 150
    }
)
```

**Batch:**
```python
events = [
    {
        "component_uuid": agent_id,
        "event_type": "AGENT_INVOCATION",
        "payload": {"prompt": "Hello", "response": "Hi"}
    },
    {
        "component_uuid": agent_id,
        "event_type": "AGENT_COMPLETION",
        "payload": {"success": True}
    }
]
agent_client.send_telemetry_batch(events)
```

---

##### Memoria Centralizada

```python
# Obtener valor
value = agent_client.memory_get(
    agent_id=agent_id,
    namespace="conversation",
    key="user_preferences"
)

# Guardar valor
agent_client.memory_set(
    agent_id=agent_id,
    namespace="conversation",
    key="user_preferences",
    value={"theme": "dark", "language": "es"},
    expires_in_days=30  # Opcional
)

# Eliminar valor
agent_client.memory_delete(
    agent_id=agent_id,
    namespace="conversation",
    key="user_preferences"
)

# Batch
agent_client.memory_batch_set(
    agent_id=agent_id,
    namespace="conversation",
    entries={
        "key1": "value1",
        "key2": "value2"
    }
)

# Snapshots
snapshot = agent_client.memory_create_snapshot(agent_id)
agent_client.memory_restore_snapshot(agent_id, snapshot["snapshot_id"])
```

---

##### Supervisor

```python
# Polling de estado
status = agent_client.supervisor_get_status(agent_id)
# status = {
#     "state": "ACTIVE" | "PAUSED" | "TERMINATED" | "ERROR",
#     "last_update": "2025-01-01T00:00:00Z",
#     "messages": ["..."],
#     "actions_required": ["..."]
# }

# Manejar webhook
agent_client.supervisor_handle_webhook(
    action="PAUSE",  # PAUSE, RESUME, TERMINATE
    payload={"reason": "Maintenance"}
)
```

---

##### Compliance

```python
# Registrar evidencia inmutable
log_result = agent_client.audit_log_immutable(
    component_uuid=agent_id,
    operation="INVOKE",
    actor="user123",
    metadata={
        "decision": "APPROVED",
        "reason": "Policy check passed"
    }
)

# Verificar evidencia
verification = agent_client.verify_evidence(log_result["log_uuid"])
# verification = {
#     "is_valid": True,
#     "hash_match": True,
#     "verified_at": "2025-01-01T00:00:00Z"
# }
```

---

##### Policy Runtime

```python
# Verificar políticas
result = agent_client.policy_check(
    component_uuid=agent_id,
    action="INVOKE",
    context={
        "prompt": "Hello",
        "user_id": "user123",
        "workspace_uuid": "workspace-uuid"
    }
)

# result = {
#     "allowed": True,
#     "reason": "Policy check passed",
#     "violations": [],
#     "required_approvals": []
# }

if not result["allowed"]:
    raise Exception(f"Política violada: {result['reason']}")

# Pre-check (más rápido, solo lectura)
pre_check = agent_client.policy_pre_check(
    component_uuid=agent_id,
    action="INVOKE",
    context={"prompt": "Hello"}
)
```

---

##### Health Checks

```python
# Health check
health = agent_client.health()
# health = {
#     "status": "healthy" | "degraded" | "unhealthy",
#     "version": "1.0.0",
#     "uptime_seconds": 3600,
#     "last_successful_request": "2025-01-01T00:00:00Z"
# }

# Readiness (K8s)
is_ready = agent_client.ready()  # bool
```

---

## ⚙️ Configuración

### Retry

```python
from codeflowx_sdk import AgentClient, RetryConfig

retry_config = RetryConfig(
    max_attempts=5,
    initial_delay_ms=1000,
    max_delay_ms=10000,
    multiplier=2.0,
    retryable_status_codes=[503, 502, 429, 500]
)

client = AgentClient(
    api_key="...",
    retry_config=retry_config
)
```

### Circuit Breaker

```python
from codeflowx_sdk import AgentClient, CircuitBreakerConfig

circuit_breaker = CircuitBreakerConfig(
    failure_threshold=5,
    timeout_seconds=60,
    half_open_max_calls=3
)

client = AgentClient(
    api_key="...",
    circuit_breaker_config=circuit_breaker
)
```

### Rate Limiting

```python
from codeflowx_sdk import AgentClient, RateLimiterConfig

rate_limiter = RateLimiterConfig(
    requests_per_second=100,
    burst_size=200
)

client = AgentClient(
    api_key="...",
    rate_limiter_config=rate_limiter
)
```

### Caché

```python
from codeflowx_sdk import AgentClient, CacheConfig

cache_config = CacheConfig(
    enabled=True,
    ttl_seconds=300,
    max_size=1000,
    cache_policy_checks=True
)

client = AgentClient(
    api_key="...",
    cache_config=cache_config
)
```

### Offline Mode

```python
from codeflowx_sdk import AgentClient, OfflineQueue

offline_queue = OfflineQueue(
    max_queue_size=10000,
    persist_to_disk=True,
    retry_on_reconnect=True
)

client = AgentClient(
    api_key="...",
    offline_queue=offline_queue
)
```

### Filtrado PII

```python
from codeflowx_sdk import AgentClient, PIIFilter

pii_filter = PIIFilter(
    enabled=True,
    redaction_mode="MASK",  # MASK, HASH, REMOVE
    patterns=["email", "phone", "ssn", "credit_card"]
)

client = AgentClient(
    api_key="...",
    pii_filter=pii_filter
)

# Se aplica automáticamente en send_telemetry
client.send_telemetry(
    component_uuid=agent_id,
    event_type="AGENT_INVOCATION",
    payload={"email": "user@example.com"}  # Se filtra automáticamente
)
```

### Métricas

```python
from codeflowx_sdk import AgentClient, MetricsCollector

metrics = MetricsCollector(enabled=True)

client = AgentClient(
    api_key="...",
    metrics_collector=metrics
)

# Obtener resumen
summary = client.get_metrics_summary()
# summary = {
#     "operation": {
#         "total_requests": 100,
#         "success_count": 95,
#         "failure_count": 5,
#         "success_rate": 0.95,
#         "latency": {
#             "p50": 100,
#             "p95": 200,
#             "p99": 300,
#             "avg": 150
#         }
#     }
# }

# Exportar Prometheus
prometheus_metrics = metrics.export_prometheus()
```

---

## 🎨 Decoradores

### FastAPI

```python
from fastapi import FastAPI
from codeflowx_sdk import AgentClient
from codeflowx_sdk.decorators import telemetry_required, policy_check_required

app = FastAPI()
client = AgentClient.from_env()
agent_id = "agent-uuid"

@app.post("/agent/invoke")
@telemetry_required(component_uuid=agent_id)
@policy_check_required(component_uuid=agent_id, action="INVOKE")
async def invoke_agent(prompt: str):
    # Tu lógica de agente
    return {"response": "..."}
```

### Flask

```python
from flask import Flask
from codeflowx_sdk.decorators import telemetry_required

app = Flask(__name__)
client = AgentClient.from_env()
agent_id = "agent-uuid"

@app.route("/agent/invoke", methods=["POST"])
@telemetry_required(component_uuid=agent_id)
def invoke_agent():
    # Tu lógica de agente
    return {"response": "..."}
```

---

## 📊 Ejemplos Completos

Ver [Ejemplos Python](EJEMPLOS_PYTHON.md) para más casos de uso.

---

## 🔗 Enlaces

- [API Reference Completa](API_REFERENCE.md)
- [Troubleshooting](../TROUBLESHOOTING.md)
- [FAQ](../FAQ.md)
