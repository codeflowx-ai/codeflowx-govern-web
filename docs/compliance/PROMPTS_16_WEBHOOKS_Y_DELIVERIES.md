# PROMPTS_16 - WEBHOOKS Y CALLBACKS GOVERNANCE
## EU AI Act Compliance · Notificaciones externas (n8n, ServiceNow, Slack)

**Equipo:** Java Backend + PostgreSQL  
**Fecha:** 18 de noviembre de 2025  
**Objetivo:** Registrar webhooks por proyecto, entregar decisiones de gobierno y gestionar reintentos garantizando trazabilidad Art. 15/19.

---

## 🧱 Alcance
- API REST para alta/baja/listado de webhooks (reutiliza módulo `codeflowx-governance-api`).
- Persistencia en tablas `GOVWEBHOOKSUBSCRIPTIONS` y `GOVWEBHOOKDELIVERIES` (ver PROMPTS_18).
- Motor de reintentos exponenciales + firma HMAC.
- Observabilidad completa (métricas, logs, dashboard).

---

## 📋 Prompt 16.1 · Modelo y Servicio de Registro
```
Crear `WebhookRegistrationService`:
- Métodos:
  • registerWebhook(projectUuid, request)
  • deactivateWebhook(webhookUuid)
  • listWebhooks(projectUuid)
- Validar URL (formato https, longitud <=500, DNS opcional).
- Guardar secreto cifrado (pgcrypto) y eventos permitidos.
- Generar UUID y fecha de creación.
- Registrar auditoría Art. 19 (`cor_auditlog`) acción WEBHOOK_REGISTERED.
```

### Checklist
- [ ] Tests unitarios con Mockito (registro, desactivación).  
- [ ] Auditoría insertada y verificada.

---

## 📋 Prompt 16.2 · Endpoints REST Webhooks
```
Extender `GovernanceWebhookController` en `codeflowx-governance-api`.
- POST `/api/v1/projects/{projectUuid}/webhooks`
  • Request: name, targetUrl, events[], secret.
  • Respuesta 201 con webhookUuid.
- GET `/api/v1/projects/{projectUuid}/webhooks`
  • Lista activa/inactiva.
- DELETE `/api/v1/projects/{projectUuid}/webhooks/{webhookUuid}`
  • Marca `active=false`.
- Validar ownership: proyecto debe existir y pertenecer a la organización del API Key.
```

### Checklist
- [ ] Documentados en OpenAPI (PROMPTS_15).  
- [ ] Respuestas uniformes (`ApiErrorResponse`).

---

## 📋 Prompt 16.3 · Motor de Entregas y Firma HMAC
```
Diseñar componente `GovernanceDecisionNotifier`:
- Listener RabbitMQ cola `governance.decisions`.
- Resolve webhooks activos por proyecto.
- Construye payload JSON con:
  • eventUuid, projectUuid, decision, severity, score, recommendations, timestamp.
  • Enlace GET status evento (PROMPTS_15).
- Firma HMAC SHA-256: header `X-Codeflowx-Signature` = hex(digest(payload + secret)).
- Persistir en `GOVWEBHOOKDELIVERIES` estado PENDING, attempts=0.
- Enviar HTTP (RestTemplate/HttpClient) configurable: timeout 5s, retries 3.
- Actualizar estado según respuesta (2xx → SENT, 5xx/timeout → RETRYING, 4xx → FAILED).
```

### Checklist
- [ ] Retries exponenciales: 1min, 5min, 15min, 60min, 180min.  
- [ ] Logs estructurados (deliveryUuid, webhookUuid, status).  
- [ ] Test integración con WireMock.

---

## 📋 Prompt 16.4 · Scheduler Reintentos Pendientes
```
Implementar `WebhookRetryScheduler` (Spring @Scheduled cada 60s):
- Consulta `GOVWEBHOOKDELIVERIES` con status RETRYING y `nextRetryAt <= now`.
- Reintenta envío (misma lógica de firma).
- Si supera 5 intentos → status FAILED + registrar auditoría WEBHOOK_FAILED.
- Notificar a soporte vía email/Slack (usar existing NotificationService).
```

### Checklist
- [ ] Query optimizada usando índice `idx_gwd_nextretry`.  
- [ ] Métrica `governance_webhook_failures_total` con etiquetas `reason`.

---

## 📋 Prompt 16.5 · Observabilidad y Dashboard
```
- Métricas Prometheus:
  • `governance_webhooks_registered_total{status}`
  • `governance_webhook_deliveries_total{status}`
  • `governance_webhook_latency_seconds_histogram`
- Logs con MDC (`webhookUuid`, `deliveryUuid`).
- Dashboard Grafana (paneles: deliver, retries, failures, p95 latency).
- Alertas Prometheus: >5 fallos consecutivos por webhook -> alerta "CRITICAL".
```

### Checklist
- [ ] Métricas exportadas /actuator/prometheus.  
- [ ] Dashboard JSON en `monitoring/grafana/webhooks.json`.

---

## 🔁 Dependencias
- PROMPTS_15 → reutiliza `GovernanceEventService` y autenticación.
- PROMPTS_18 → requiere tablas `GOVWEBHOOKSUBSCRIPTIONS`, `GOVWEBHOOKDELIVERIES`.

**Estado inicial:** Pendiente.  
**Duración estimada:** 3 días (un agente Java + un agente datos).
