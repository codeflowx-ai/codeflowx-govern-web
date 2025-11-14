# PROMPTS_15 - API PÚBLICA CODEFLOWX GOVERNANCE (Java)
## EU AI Act Compliance · Ingesta REST de eventos IA externos

**Equipo:** Java Backend (Spring Boot)  
**Fecha:** 18 de noviembre de 2025  
**Objetivo:** Exponer API REST segura (`codeflowx-governance-api`) para que agentes externos (n8n, ChatGPT, Claude, RAGs existentes) envíen eventos a CodeflowX y disparen el pipeline de gobierno.

---

## 🧱 Alcance
- Nuevo módulo Spring Boot dentro de `codeflowx-govern-backend` (`modules/governance-api`).
- Arquitectura hexagonal: Controllers → Application Services → Ports (RabbitMQ / DAO EnArt).
- Seguridad por API Key (`X-Codeflowx-Key`) y soporte opcional JWT.
- Documentación OpenAPI 3 disponible en `/swagger-ui`.
- Sin acoplar microservicios Python (comunicación vía RabbitMQ).

---

## ✅ Requisitos Previos
1. RabbitMQ operativo con exchange `governance.events` (topic) y `governance.decisions` (fanout).  
2. Claves API almacenadas en tabla `SECAPIKEYS` (existe en backend).  
3. Configuración `application-governance-api.yml` con rate limiting (Bucket4j) y logging estructurado JSON.

---

## 📋 Prompt 15.1 · Estructura Proyecto y Configuración Básica
```
Necesito crear el módulo Maven `codeflowx-governance-api` dentro de `codeflowx-govern-backend`.
- Empaquetado Spring Boot 3.2.
- Dependencias: web, validation, actuator, amqp, springdoc-openapi, logback encoder.
- Configuración `application-governance-api.yml` con:
  • Puerto configurable (default 8095).
  • Connection RabbitMQ: host, username, password.
  • Propiedad `governance.api.rate-limit.requests-per-minute`.
- Clase `GovernanceApiApplication` con escaneo limitado a paquete `com.codeflowx.governance.api`.
- Health endpoint `/actuator/health` y métricas Prometheus `/actuator/prometheus`.
```

### Checklist
- [ ] Módulo Maven generado en `pom.xml` raíz.  
- [ ] Configuración Spring Boot independiente (`spring.config.activate.on-profile`).  
- [ ] Logging JSON (`logstash-logback-encoder`).

---

## 📋 Prompt 15.2 · Seguridad API Key + Rate Limiting
```
Implementar seguridad para `codeflowx-governance-api`.
- Filtro `ApiKeyAuthenticationFilter` que verifica header `X-Codeflowx-Key`.
- Servicio `ApiKeyService` que consulta DAO EnArt (`SECAPIKEYS`).
- Registrar claves en cache Caffeine (TTL 5 min).
- Rate limiting por clave usando Bucket4j (configurable).
- Respuestas estándar:
  • 401 cuando falta o es inválida la clave.
  • 429 cuando se supera el límite.
- Logs estructurados: requestId, apiKeyId (hash), endpoint.
```

### Checklist
- [ ] Test unitario con MockMvc para 401/429.  
- [ ] Métrica `governance_api_requests_total{status="401"}` generada.

---

## 📋 Prompt 15.3 · Endpoint `POST /api/v1/governance/events`
```
Crear controlador `GovernanceEventController` con endpoint POST.
- Request DTO `GovernanceEventRequest` con validaciones:
  projectUuid (UUID), sourceSystem (enum), input/output, model info, riskFlags (lista <=10), traceId (opcional).
- Service `GovernanceEventService`:
  1. Recupera proyecto EnArt (tabla PRJPROJECTS) por UUID.
  2. Persiste evento en tabla `GOVGOVERNANCEEVENTS` (usar DAO). Estado inicial `PENDING`.
  3. Publica mensaje JSON en exchange `governance.events` routing `governance.event.created`.
- Respuesta 202 con payload `{ "eventUuid": "uuid", "status": "PENDING" }`.
- Manejar errores:
  • 404 cuando proyecto no existe.
  • 400 cuando payload inválido.
  • 500 cuando RabbitMQ indisponible (reintentar 3 veces, backoff exponencial).
```

### Checklist
- [ ] DTO validado con `@Valid` y `@JsonProperty` snake_case → camelCase.  
- [ ] Test de integración con Testcontainers RabbitMQ.  
- [ ] Evento registrado en tabla con hash prompt (trigger se implementa en PROMPTS_18).

---

## 📋 Prompt 15.4 · Endpoint `GET /api/v1/governance/events/{eventUuid}`
```
Añadir endpoint GET para recuperar estado del evento.
- Busca en tabla `GOVGOVERNANCEEVENTS` por UUID.
- Incluye resultados asociados de `GOVGOVERNANCERESULTS` agrupados por etapa.
- Si todavía está `PENDING`, devuelve `results: []`.
- Respuesta JSON incluye enlaces a reportes (cuando existan).
```

### Checklist
- [ ] Respuesta cacheable 30s (Spring Cache Caffeine).  
- [ ] Test para evento inexistente (404) y evento completado.

---

## 📋 Prompt 15.5 · Endpoint `POST /api/v1/projects/{projectUuid}/evaluate`
```
Endpoint para evaluaciones manuales batch.
- Payload: lista de interacciones exportadas (máx 500 por request).
- Cada interacción se convierte en evento PENDING y se publica en RabbitMQ.
- Registrar auditoría en `cor_auditlog` (Art. 19) con acción `BATCH_EVALUATION_REQUEST`.
- Respuesta: total registros aceptados, rechazados y traceId global.
```

### Checklist
- [ ] Validación de tamaño (500) → retorna 400 si excede.  
- [ ] Auditoría insertada con hash chain (función existente en PROMPTS_14).

---

## 📋 Prompt 15.6 · Documentación y Observabilidad
```
- Configurar SpringDoc (`@OpenAPIDefinition`) con tags y ejemplos.
- Exponer métricas personalizadas:
  • `governance_events_ingested_total{sourceSystem}`.
  • `governance_events_rejected_total{reason}`.
- Centralizar logs con MDC (`requestId`, `traceId`, `projectUuid`).
- Añadir Dashboard Grafana con paneles ingest rate, errores, 401/429.
```

### Checklist
- [ ] Swagger UI documenta los 3 endpoints.  
- [ ] Métricas registradas en `/actuator/prometheus`.  
- [ ] Configuración dashboard exportada (JSON en `monitoring/grafana`).

---

## 🔁 Dependencias y Referencias
- PROMPTS_16 (webhooks) → endpoints `/webhooks`.
- PROMPTS_17 (MCP) → comparte servicios core.
- PROMPTS_18 (PostgreSQL) → migraciones `gov*` y triggers.

**Estado inicial:** Pendiente.  
**Duración estimada:** 4-5 días (dos agentes en paralelo).
