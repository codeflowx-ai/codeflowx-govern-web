# Checklist · API-First + Microservicios Reutilizables

> Objetivo: garantizar que la capa pública de CodeflowX (Gateway + APIs + SDKs + MCP) se diseñe “API-first”, con contratos reutilizables desde front y agentes, antes de implementar BPMN o lógica interna.

---

## 1. Contratos API-First
- [ ] Documento OpenAPI único (`/docs/openapi/governance-api.yaml`) versionado (semver) y tratado como single source of truth.
- [ ] DTOs compartidos en `codeflowx.govern.nocode.dtos` (Java) y plantillas JSON para SDK Python/Java (tipos equivalentes).
- [ ] Reglas de versionado: `v1` estable con control de breaking changes; `v1alpha` para rutas experimentales.
- [ ] Convenciones de naming y casing alineadas a AI OS (`snake_case` en payload externo, camelCase en DTO interno).
- [ ] Inclusión de ejemplos (request/response) en OpenAPI y validación automática (schemathesis / springdoc tests).

## 2. Microservicios necesarios
| Microservicio | Rol | Reutilización desde Front/SDK |
|---------------|-----|--------------------------------|
| `codeflowx-govern-gateway` | Gateway Spring Cloud (DMZ): auth, rate limiting, métricas globales, rutas a servicios internos | Front consume únicamente este endpoint público |
| `codeflowx-governance-api` | API Edge: expone `/api/v1/governance/*`, `/api/v1/projects/*/webhooks`, health, OpenAPI | Reutilizado por SPA, SDKs, MCP adapter, integraciones externas |
| `codeflowx-governance-events-service` | Backend interno: persiste eventos, habla con RabbitMQ, AI OS runtime, resuelve estados | Consumido por API Edge y MCP adapter; no expuesto directo a front |
| `governance-mcp-adapter` (WebSocket) | Adapta servicios REST a MCP JSON-RPC | Reutiliza `GovernanceEventService` / `WebhookRegistrationService` |
| `codeflowx-aios-api` (opcional) | Exponer CRUD AI OS (modelos, approvals, bias, compliance) si se requiere para SDKs | Permite a front y SDKs reutilizar AI OS sin tocar monolito |

## 3. Requisitos comunes para todos los micros
- [ ] Arquitectura hexagonal (puertos/adaptadores, sin dependencias directas a infra en capa web).
- [ ] Seguridad:
  - API Key (`X-Codeflowx-Key`) obligatoria; soporte JWT opcional.
  - Validación de cuota por clave (Bucket4j) y reglas WAF en Gateway.
- [ ] Observabilidad (Prometheus + Grafana):
  - `*_requests_total{service,status}`,
  - `*_latency_seconds_bucket`,
  - MDC con `requestId`, `traceId`, `apiKeyId`.
- [ ] Logging estructurado (JSON) → centralizado en ELK / Loki.
- [ ] Retries + circuit breakers al llamar servicios internos (WebClient con Resilience4j).
- [ ] Documentación de dependencias (config YAML, secrets) para despliegue GitOps.

## 4. Pasos para construir API-First
1. **Diseño**
   - Recopilar requisitos de PROMPT_001‑003, PROMPTS_15‑18 y doc SDK.
   - Identificar recursos/acciones y modelarlos en OpenAPI.
2. **Validación del contrato**
   - Revisar con equipos Front, SDK y MCP.
   - Generar mocks con Prism/Stoplight y pruebas contractuales.
3. **Implementación Edge (`codeflowx-governance-api`)**
   - Generar stubs a partir del OpenAPI.
   - Integrar filtros de seguridad/rate limiting.
   - Delegar a interfaces (client) que hablan con `codeflowx-governance-events-service`.
4. **Implementación Backend (`codeflowx-governance-events-service`)**
   - Implementar puertos (DAO, Rabbit, AI OS runtime).
   - Test containers para PostgreSQL/Rabbit/Flowable.
   - Exponer APIs internas `/internal/v1/*`.
5. **Gateway**
   - Configurar rutas, políticas (rate limit, CORS, headers).
   - Centralizar métricas y trazas.
6. **SDKs y MCP**
   - Generar SDK Java/Python desde OpenAPI (openapi-generator + plantillas).
   - MCP adapter reusa los mismos servicios y respeta límites de sesión.
7. **QA + Publicación**
   - Tests e2e (Gateway → Edge → Backend → AI OS).
   - Publicar documentación (Swagger UI con OpenAPI estático).

## 5. Checklist de verificación final
- [ ] OpenAPI aprobada por Front y SDKs.
- [ ] Gateway levantado con políticas definidas.
- [ ] API Edge compila y pasa contract tests.
- [ ] Backend de eventos con adaptadores reales y colas declaradas.
- [ ] SDKs empaquetados (Maven Central / PyPI) y ejemplos actualizados.
- [ ] MCP adapter validado con clientes (VSCode, Claude, etc.).
- [ ] Observabilidad (dashboards, alertas) conectada.
- [ ] Guidelines de versión y deprecation publicadas.

> Próxima acción: una vez el API-First esté cerrado y los micros alineados, otro agente sincroniza BPMN/Flowable con estos endpoints.
