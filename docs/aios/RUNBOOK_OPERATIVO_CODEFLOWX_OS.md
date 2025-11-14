# Runbook Operativo · CodeflowX OS

## 1. Objetivo
Instrucciones día a día para operar CodeflowX OS garantizando continuidad, cumplimiento y seguridad.

## 2. Roles Involucrados
- **Operator Lead**: responsable del turno, coordina actividades y escalados.
- **Compliance Officer**: valida alertas normativas y evidencias Art. 19.
- **SRE / Platform Engineer**: gestiona infraestructura (Kubernetes/Terraform, observabilidad).
- **AI OS Support**: atención a tickets de clientes/partners.

## 3. Calendario Operativo
- **Diario**  
  1. Revisar panel `AIOS-Operational` (Grafana): uptime agentes, colas RabbitMQ, KPIs críticos.  
  2. Verificar alertas Prometheus (sla incidents, policy breach, webhook failure).  
  3. Revisar `ImmutableLog` últimas 24h (filtrar eventos `ERROR`, `SECURITY`).  
  4. Validar ejecuciones BPMN pendientes en Flowable Task List.  
  5. Confirmar entregas webhook (estado SENT > 98%).  
- **Semanal**  
  1. Revisión de agentes nuevos en marketplace (aprobación/comentarios).  
  2. Auditoría de API Keys activas e intents fallidos (rate limit).  
  3. Revisión de knowledge gaps y planes de documentación normativa.  
  4. Backup verificado de metamodelos y configuraciones (`SectorMetamodelService`).  
- **Mensual**  
  1. Simulacro de contingencia (failover cluster, restauración backup).  
  2. Revisión de políticas (Drools) vs normativa actualizada.  
  3. Informe ejecutivo: métricas, incidentes, mejoras.  
  4. Actualización de roadmap y feedback partners/clientes.

## 4. Procedimientos Clave
- **Gestión de Alertas SLA**  
  - Identificar agente afectado.  
  - Revisar telemetría `aio_component_latency_seconds`.  
  - Coordinación con equipo del agente → decidir rollback o hotfix.  
  - Documentar en `agent_execution_log.md` y `ImmutableLog`.
- **Incidente de Compliance**  
  - Pausar agente vía BPMN (estado `SUSPENDED`).  
  - Revisión de políticas y logs (Drools, ImmutableLog).  
  - Generar reporte rápido (plantilla `docs/templates/compliance_incident.md`).  
  - Escalar a Compliance Officer y comité.  
- **Fallo en Webhooks**  
  - Consultar `governance_webhook_failures_total`.  
  - Ver `GOVWEBHOOKDELIVERIES` status RETRYING/FAILED.  
  - Contactar cliente, coordinar reactivación.  
  - Reprocesar en scheduler o disparar manual.
- **Actualización de Frameworks**  
  - Ejecutar pipeline de verificación (QA interna).  
  - Asegurar que `PROMPTS_FAAS_*` referencian nueva versión.  
  - Comunicar a partners impactados.

## 5. Herramientas y Dashboards
- Grafana: `AIOS-Operational`, `Governance-Events`, `Webhooks`, `MCP Sessions`.  
- Kibana/ELK: tracking de `ImmutableLog` y logs estructurados.  
- Prometheus/Alertmanager: alertas automáticas (lag colas, policy breach, drift).  
- Flowable Modeler/Task: seguimiento BPMN.  
- Jira Service Desk: tickets `AIOS-*`.

## 6. Gestión de Cambios
- Cambios planificados requieren RFC interno: análisis impacto, ventana, rollback.  
- Requiere aprobación de Operator Lead + Compliance (si afecta políticas).  
- Post-mortem obligatorio para incidentes P0/P1 (<48h).  
- Conservar logs y métricas 1 año (auditorías).

## 7. Contactos de Escalado
- Operator Lead (24/7) → `+34 XXX XXX XXX`.  
- Compliance Officer → `compliance@codeflowx.com`.  
- Platform/SRE → canal Matrix `#aios-platform`.  
- Arquitectura AI OS → `arch-os@codeflowx.com`.

## 8. Documentación Referencia
- `QA_INTERNA_CODEFLOWX_OS.md`, `INTEGRACION_CLIENTES_CODEFLOWX_OS.md`.  
- `PROMPTS_001/002/003`, `PROMPTS_15-17`.  
- `AI_OS_OVERVIEW.md`, `ORQUESTACION_AGENTES.md`.



