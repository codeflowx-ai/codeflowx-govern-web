# PROMPTS_17 - ADAPTADOR MCP (Machine Control Protocol)
## EU AI Act Compliance · Integración con agentes locales (VSCode, Open Interpreter)

**Equipo:** Java Backend (Spring Boot)  
**Fecha:** 18 de noviembre de 2025  
**Objetivo:** Proveer un servidor MCP sobre HTTP/WebSocket que permita a agentes locales interactuar con CodeflowX (registrar eventos, consultar estados y gestionar webhooks) sin usar REST tradicional.

---

## 🧱 Alcance
- Submódulo `modules/governance-mcp-adapter` dependiente del módulo REST (PROMPTS_15).
- Implementación protocolo MCP v0.1 (handshake + mensajes JSON-RPC).
- Autenticación mediante API Key + validación de origen.
- Observabilidad y tests end-to-end.

---

## 📋 Prompt 17.1 · Estructura del Adaptador MCP
```
Crear módulo `governance-mcp-adapter`:
- Dependencias: spring-boot-starter-websocket, jackson, reactor, lombok.
- Endpoint `/mcp` que soporta upgrade WebSocket.
- Implementar handshake MCP (mensaje `capabilities` + `ready`).
- Registrar beans `McpSessionManager` y `McpMessageDispatcher`.
- Reutilizar servicios `GovernanceEventService` y `WebhookRegistrationService` via Spring.
```

### Checklist
- [ ] WebSocket configurado con STOMP deshabilitado (usamos JSON puro).  
- [ ] Registro de sesiones en memoria (ConcurrentMap).  
- [ ] Logs de conexión/desconexión con `sessionId`.

---

## 📋 Prompt 17.2 · Métodos MCP Expuestos
```
Implementar los métodos MCP:
1. `governance.registerEvent`
   - Request: igual a `POST /governance/events`.
   - Respuesta: eventUuid + status.
2. `governance.getStatus`
   - Request: eventUuid.
   - Respuesta: estado + resultados.
3. `governance.registerWebhook`
   - Request: projectUuid, name, url, events[], secret.
   - Respuesta: webhookUuid.
- Validar parámetros usando `javax.validation`.
- Mapear errores a respuestas MCP (`error.code`, `error.message`).
```

### Checklist
- [ ] Documentación interna de payloads (README).  
- [ ] Tests unitarios para cada método.

---

## 📋 Prompt 17.3 · Autenticación y Seguridad
```
- Requerir header `X-Codeflowx-Key` durante handshake (o query param `apiKey`).
- Validar origen permitido (`allowedOrigins` en config, ej. `vscode://`, `http://localhost`).
- Implementar limitación de sesiones concurrentes por clave (máx 5).
- Cerrado de sesión cuando expira API Key (hook de `ApiKeyService`).
```

### Checklist
- [ ] Test handshake exitoso y provocado (clave inválida).  
- [ ] Métrica `governance_mcp_active_sessions` expuesta.

---

## 📋 Prompt 17.4 · Observabilidad y Logging
```
- Métricas Prometheus:
  • `governance_mcp_requests_total{method, outcome}`
  • `governance_mcp_latency_seconds_histogram`
- Logs con MDC (`sessionId`, `method`, `apiKeyIdHash`).
- Dashboard Grafana para monitorear sesiones, errores y latency.
```

### Checklist
- [ ] Métricas en `/actuator/prometheus`.  
- [ ] Dashboard JSON en `monitoring/grafana/mcp.json`.

---

## 📋 Prompt 17.5 · Tests End-to-End
```
- Construir cliente MCP de prueba (Java) que abra WebSocket, envíe handshake y métodos.
- Escenarios:
  • Registro evento + verificación en DB (Testcontainers PostgreSQL + RabbitMQ).
  • Consulta status tras publicar decisión simulada.
  • Registro webhook y verificación en tabla.
- Automatizar en `governance-mcp-adapter-it` (JUnit5 + SpringBootTest).
```

### Checklist
- [ ] Pipeline CI ejecuta pruebas E2E (GitHub Actions).  
- [ ] Documentar comando de ejecución (`mvn -pl governance-mcp-adapter verify`).

---

## 🔁 Dependencias
- PROMPTS_15: Reutiliza servicios y DTOs.
- PROMPTS_16: Registra webhooks desde MCP.
- PROMPTS_18: Persistencia tablas `gov*`.

**Estado inicial:** Pendiente.  
**Duración estimada:** 2-3 días.  
**Entrega esperada:** Adaptador MCP certificado con pruebas E2E.
