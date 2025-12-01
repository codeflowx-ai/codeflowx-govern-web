# API Reference - CodeflowX SDKs

Referencia completa de la API para todos los SDKs (Python, TypeScript, Java).

---

## 🔗 Base URL

**Producción:** `https://gateway.codeflowx.ai`
**Desarrollo:** `http://localhost:8080` (configurable)

---

## 🔐 Autenticación

Todos los requests requieren autenticación mediante API Key:

**Header:**
```
Authorization: Bearer {api_key}
```

**API Key Format:**
- Producción: `cfx_sk_live_...`
- Desarrollo: `cfx_sk_test_...`

---

## 📋 Endpoints

### Agentes

#### POST `/api/v1/agents/register`

Registra un nuevo agente externo.

**Request:**
```json
{
  "agent_name": "customer-service-agent",
  "agent_type": "custom",
  "workspace_uuid": "workspace-uuid",
  "capabilities": {
    "language": "es",
    "domains": ["customer_service"]
  },
  "callback_url": "https://mi-app.com/webhooks/codeflowx",
  "endpoint": "https://mi-app.com/api/agent/invoke"
}
```

**Response:**
```json
{
  "agent_id": "agent-uuid",
  "agent_token": "cfx_agent_abc123...",
  "workspace_uuid": "workspace-uuid",
  "registration_date": "2025-01-01T00:00:00Z",
  "status": "ACTIVE"
}
```

---

### Telemetría

#### POST `/api/v1/telemetry`

Envía telemetría de un componente.

**Request:**
```json
{
  "component_uuid": "agent-uuid",
  "event_type": "AGENT_INVOCATION",
  "payload": {
    "prompt": "Hello",
    "response": "Hi there",
    "latency_ms": 245
  },
  "timestamp": "2025-01-01T00:00:00Z"
}
```

**Response:** `204 No Content`

---

#### POST `/api/v1/telemetry/batch`

Envía múltiples eventos de telemetría.

**Request:**
```json
{
  "events": [
    {
      "component_uuid": "agent-uuid",
      "event_type": "AGENT_INVOCATION",
      "payload": {...}
    },
    {
      "component_uuid": "agent-uuid",
      "event_type": "AGENT_COMPLETION",
      "payload": {...}
    }
  ]
}
```

**Response:** `204 No Content`

---

### Memoria

#### GET `/api/v1/memory/{agent_id}/{namespace}/{key}`

Obtiene valor de memoria.

**Response:**
```json
{
  "value": {...}
}
```

---

#### POST `/api/v1/memory/{agent_id}`

Guarda valor en memoria.

**Request:**
```json
{
  "namespace": "conversation",
  "key": "user_preferences",
  "value": {...},
  "expires_in_days": 30
}
```

**Response:** `204 No Content`

---

#### DELETE `/api/v1/memory/{agent_id}/{namespace}/{key}`

Elimina valor de memoria.

**Response:** `204 No Content`

---

#### POST `/api/v1/memory/{agent_id}/batch`

Guarda múltiples valores.

**Request:**
```json
{
  "namespace": "conversation",
  "entries": {
    "key1": "value1",
    "key2": "value2"
  }
}
```

**Response:** `204 No Content`

---

#### POST `/api/v1/memory/{agent_id}/snapshot`

Crea snapshot de memoria.

**Response:**
```json
{
  "snapshot_id": "snapshot-uuid",
  "created_at": "2025-01-01T00:00:00Z",
  "size_bytes": 1024
}
```

---

#### POST `/api/v1/memory/{agent_id}/snapshot/{snapshot_id}/restore`

Restaura snapshot.

**Response:** `204 No Content`

---

### Supervisor

#### GET `/api/v1/supervisor/{agent_id}/status`

Obtiene estado del supervisor.

**Response:**
```json
{
  "state": "ACTIVE",
  "last_update": "2025-01-01T00:00:00Z",
  "messages": ["..."],
  "actions_required": ["..."]
}
```

---

#### POST `/api/v1/supervisor/webhook`

Maneja webhook del supervisor.

**Request:**
```json
{
  "action": "PAUSE",
  "payload": {
    "reason": "Maintenance"
  }
}
```

**Response:** `204 No Content`

---

### Compliance

#### POST `/api/v1/audit/logs`

Registra evidencia inmutable.

**Request:**
```json
{
  "component_uuid": "agent-uuid",
  "operation": "INVOKE",
  "actor": "user123",
  "metadata": {
    "decision": "APPROVED"
  }
}
```

**Response:**
```json
{
  "log_uuid": "log-uuid",
  "created_at": "2025-01-01T00:00:00Z",
  "hash": "abc123..."
}
```

---

#### GET `/api/v1/audit/logs/{log_uuid}/verify`

Verifica integridad de evidencia.

**Response:**
```json
{
  "is_valid": true,
  "hash_match": true,
  "verified_at": "2025-01-01T00:00:00Z",
  "chain_valid": true
}
```

---

### Policy Runtime

#### POST `/api/v1/policy/check`

Verifica políticas.

**Request:**
```json
{
  "component_uuid": "agent-uuid",
  "action": "INVOKE",
  "context": {
    "prompt": "Hello",
    "user_id": "user123"
  }
}
```

**Response:**
```json
{
  "allowed": true,
  "reason": "Policy check passed",
  "violations": [],
  "required_approvals": []
}
```

---

### Health

#### GET `/api/v1/health`

Health check.

**Response:**
```json
{
  "status": "healthy",
  "version": "1.0.0"
}
```

---

## 📊 Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 429 | Rate Limit Exceeded |
| 500 | Internal Server Error |
| 502 | Bad Gateway |
| 503 | Service Unavailable |

---

## ⚠️ Códigos de Error

### 401 Unauthorized

```json
{
  "error": "AuthenticationError",
  "message": "Invalid or expired API key",
  "status_code": 401
}
```

### 403 Forbidden

```json
{
  "error": "PolicyViolationError",
  "message": "Policy violation",
  "violations": [
    {
      "type": "BIAS",
      "severity": "HIGH",
      "message": "Bias detected in prompt"
    }
  ],
  "status_code": 403
}
```

### 429 Rate Limit Exceeded

```json
{
  "error": "RateLimitError",
  "message": "Rate limit exceeded",
  "retry_after": 60,
  "status_code": 429
}
```

---

## 🔄 Rate Limiting

- **Límite:** 100 requests/segundo por API key
- **Burst:** 200 requests
- **Header de respuesta:** `Retry-After: 60` (segundos)

---

## 📝 Paginación

Endpoints que retornan listas soportan paginación:

**Query Parameters:**
- `page` (int, default: 1)
- `size` (int, default: 20, max: 100)

**Response:**
```json
{
  "content": [...],
  "totalElements": 150,
  "totalPages": 8,
  "size": 20,
  "number": 1
}
```

---

## 🔗 Enlaces

- [SDK Python](SDK_PYTHON.md)
- [SDK TypeScript](SDK_TYPESCRIPT.md)
- [SDK Java](SDK_JAVA.md)
- [Troubleshooting](../TROUBLESHOOTING.md)
