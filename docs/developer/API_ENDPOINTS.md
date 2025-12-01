# API Endpoints - Lista Completa

Lista completa de endpoints de la API de CodeflowX.

---

## 🔗 Base URL

**Producción:** `https://gateway.codeflowx.ai`
**Desarrollo:** `http://localhost:8080`

---

## 📋 Endpoints

### Agentes
- `POST /api/v1/agents/register` - Registrar agente
- `GET /api/v1/agents/{agent_id}` - Obtener agente
- `PUT /api/v1/agents/{agent_id}` - Actualizar agente
- `DELETE /api/v1/agents/{agent_id}` - Eliminar agente

### Telemetría
- `POST /api/v1/telemetry` - Enviar telemetría
- `POST /api/v1/telemetry/batch` - Enviar batch

### Memoria
- `GET /api/v1/memory/{agent_id}/{namespace}/{key}` - Obtener valor
- `POST /api/v1/memory/{agent_id}` - Guardar valor
- `DELETE /api/v1/memory/{agent_id}/{namespace}/{key}` - Eliminar valor
- `POST /api/v1/memory/{agent_id}/batch` - Batch
- `POST /api/v1/memory/{agent_id}/snapshot` - Crear snapshot
- `POST /api/v1/memory/{agent_id}/snapshot/{id}/restore` - Restaurar snapshot

### Supervisor
- `GET /api/v1/supervisor/{agent_id}/status` - Estado
- `POST /api/v1/supervisor/webhook` - Webhook

### Compliance
- `POST /api/v1/audit/logs` - Registrar evidencia
- `GET /api/v1/audit/logs/{log_uuid}/verify` - Verificar evidencia

### Policy
- `POST /api/v1/policy/check` - Verificar políticas
- `POST /api/v1/policy/pre-check` - Pre-check

### Health
- `GET /api/v1/health` - Health check
- `GET /api/v1/ready` - Readiness (K8s)

---

## 📚 Documentación Completa

Ver [API Reference](SDKS/API_REFERENCE.md) para detalles completos.

---

**Más:** [API Reference](SDKS/API_REFERENCE.md)
