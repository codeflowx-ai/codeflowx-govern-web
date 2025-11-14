```
# Guía de Integración con CodeflowX OS (Clientes y Partners)

## 1. Panorama General
- CodeflowX OS = capa operativa y de gobierno sobre agentes IA, modelos, datasets, prompts y automatizaciones.
- El cliente puede ejecutar agentes en CodeflowX o en su infraestructura; en ambos casos debe registrarlos y gobernarlos via CodeflowX OS.
- Elementos clave:
  - API REST / SDK (`PROMPTS_15`).
  - Webhooks y entregas externas (`PROMPTS_16`).
  - Adaptador MCP (Machine Control Protocol) (`PROMPTS_17`) para herramientas como VSCode, Open Interpreter, Jupyter.

## 2. Requisitos Previos
- API Key emitida por CodeflowX (tabla `SECAPIKEYS`).
- Proyecto/Workspace registrado (`PRJPROJECTS` + `AioWorkspace`).
- Enlace de red seguro (https/TLS) y reglas firewall para tráfico saliente hacia CodeflowX OS.
- Configuración de logs y métricas para consumir `ImmutableLog` y endpoints Prometheus.

## 3. Registro de Componentes AI
**Flujo recomendado**
1. Preparar metadatos del agente (nombre, versión, capacidades, owner, SLA).
2. Invocar `POST /api/v1/aios/components` (SDK) para registrar:
   - `componentType`, `runtime`, `entrypoint`, `complianceTags`.
   - Ubicación (interna/externa) y credenciales cifradas.
3. Declarar bindings con `AioServiceBindingService` (`PROMPT_002`):
   - Endpoints HTTP/WebSocket del agente.
   - Eventos soportados y formatos (JSON, CSV, binario).
4. Registrar políticas aplicables (`AioPolicyBinding`).
5. Confirmar alta en el marketplace interno, si aplica.

## 4. Ejecución en Infraestructura Propia
**Si el agente corre fuera de CodeflowX OS (ej. Jupyter, microservicio Python):**
- Publicar manifiesto YAML (plantilla `docs/templates/aios_component_manifest.yaml`):
  - Recursos, dependencias, scopes.
  - Health checks y endpoints de telemetría.
- Integrar API REST:
  - **Eventos**: `POST /api/v1/governance/events`.
  - **Estado**: `GET /api/v1/governance/events/{eventUuid}`.
  - **Batch Evaluations**: `POST /api/v1/projects/{projectUuid}/evaluate`.
- Emitir telemetría via `POST /api/v1/aios/telemetry` (KPIs: uptime, risk, drift, consumo).
- Registrar logs críticos en `ImmutableLog` (REST o gRPC interno).
- Configurar webhooks para recibir decisiones (`governance.decisions` → PROMPT_16).

## 5. Ejecución Dentro de CodeflowX
- Provisionar el agente mediante `PROMPT_002` (runtime) → genera servicios, delegates, BPMN.
- Deploy automático en Kubernetes/Terraform gestionado.
- Telemetrías y logs se enrutan automáticamente a `AioTelemetryService` y `ImmutableLog`.
- Marketplace interno publica el componente tras pasar `PROMPT_003` (gobernanza).

## 6. Canales de Integración
**REST / SDK (PROMPTS_15)**  
- Endpoints principales: eventos, evaluaciones batch, consulta de estado, gestión de proyectos.
- Autenticación: header `X-Codeflowx-Key` + rate limiting.  
- Observabilidad: `/actuator/prometheus`, `/swagger-ui`.  
- Uso recomendado para agentes server-side, pipelines ETL, integraciones BPM/RPA.

**Webhooks & Deliveries (PROMPTS_16)**  
- Alta/baja y listado por proyecto.  
- Recepción de decisiones, alertas, recomendaciones.  
- Firma HMAC (`X-Codeflowx-Signature`) y reintentos exponenciales.  
- Ideal para sistemas de ticketing, ServiceNow, Slack, n8n.

**Adaptador MCP (PROMPTS_17)**  
- WebSocket JSON-RPC.  
- Métodos disponibles: `governance.registerEvent`, `governance.getStatus`, `governance.registerWebhook`.  
- Uso en IDEs, agentes locales, notebooks interactivos.

## 7. Procesos de Gobernanza
- Todo componente debe pasar por los procesos BPMN:
  - `ai-component-onboarding-v1.bpmn`.
  - `ai-marketplace-publish-v1.bpmn`.
  - `ai-policy-review-v1.bpmn`.
- Drools aplica reglas de riesgo y cumplimiento.
- Resultado (approved, HITL, rejected) se notifica vía webhook y queda en `ImmutableLog`.
- Artefactos generados/actualizados se registran en catálogo (`SectorMetamodelService`).

## 8. Monitoreo y Alertas
- Integrarse a las métricas Prometheus:
  - `governance_events_ingested_total`, `governance_webhook_failures_total`.
  - `aio_component_uptime_seconds`, `aio_policy_violations_total`.
- Configurar dashboards (Grafana JSON en `monitoring/grafana/`).
- Suscribirse a alertas (Prometheus Alertmanager) por workspace.

## 9. Seguridad y Auditoría
- Todos los llamados deben portar `requestId` y `traceId`.
- Logging estructurado JSON con campos (`apiKeyId`, `projectUuid`, `componentUuid`).
- Auditoría automatizada Art. 19 (`cor_auditlog`) para registros críticos:
  - Registro/desregistro de componentes.
  - Evaluaciones manuales/batch.
  - Fallos de webhook y recuperación.
- Cumplimiento multi-jurisdiccional gestionado por `AioPolicyBinding` (EU AI Act, LGPD, GDPR, SOC2, etc.).

## 10. Buenas Prácticas
- Mantener agentes versionados (`semver`).  
- Automatizar CI/CD contra la API de componentes para elevar nuevas versiones.  
- Usar plantillas oficiales (Flyway, MapStruct) para cualquier modelo persistente.  
- Documentar cada agente en el marketplace interno (targets, requisitos, tests).  
- Revisar `PROMPTS_FAAS_*` para generar servicios, UI y reglas coherentes por sector.

## 11. Próximos Pasos para Clientes
1. Solicitar API Key y workspace en CodeflowX.  
2. Registrar primer agente (manifiesto).  
3. Configurar webhooks y telemetría.  
4. Ejecutar proceso de onboarding (BPMN).  
5. Publicar agente en marketplace (si aplica).  
6. Monitorear KPIs y auditar decisiones periódicamente.

## 12. Soporte y Contacto
- Equipo CodeflowX OS Platform → `os-support@codeflowx.com`.  
- Requests de nuevas políticas/normativas → `compliance@codeflowx.com`.  
- Incidencias urgentes → canal privado Signal/Matrix (detalle en contrato).
```

