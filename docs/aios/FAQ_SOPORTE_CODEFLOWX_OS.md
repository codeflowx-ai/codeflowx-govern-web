# FAQ & Playbook de Soporte · CodeflowX OS

## 1. Objetivo
Proporcionar respuestas rápidas y ruta de escalado para incidencias y consultas frecuentes de clientes y partners.

## 2. Categorías Principales
- **Acceso y autenticación**.
- **Integración SDK / MCP / Webhooks**.
- **Gobernanza y compliance**.
- **Marketplace y agentes**.
- **Observabilidad y métricas**.
- **Facturación/licencias**.

## 3. Preguntas Frecuentes

### 3.1 Acceso / Autenticación
- **¿Dónde obtengo mi API Key?**  
  Solicitar al administrador del workspace. Requiere aprobar términos y registrar dirección IP/uso.
- **Recibo 401/429 constantes**  
  Verificar encabezado `X-Codeflowx-Key`, sincronización de hora y límites configurados (Bucket4j). Escalar si persiste.

### 3.2 Integraciones
- **¿Cómo registro un agente externo Python?**  
  Seguir guía `INTEGRACION_CLIENTES_CODEFLOWX_OS.md`: manifest YAML, `POST /aios/components`, telemetría y webhooks.
- **Los webhooks no recibieron respuesta**  
  Revisar panel `Webhooks` y logs `GOVWEBHOOKDELIVERIES`. Reintentar desde `WebhookRetryScheduler`.  
- **Fallo handshake MCP**  
  Confirmar `X-Codeflowx-Key`, origen permitido y versión MCP. Consultar `governance_mcp_active_sessions`.

### 3.3 Gobernanza / Compliance
- **Proceso BPMN se queda en HITL**  
  Revisar tareas en Flowable Task List. Confirmar que reviewer o compliance officer complete la tarea.
- **¿Cómo generar evidencias para auditoría?**  
  Exportar `ImmutableLog` + reportes compliance (`docs/templates/report_audit.md`).

### 3.4 Marketplace
- **Quiero publicar mi agente**  
  Seguir `MANUAL_MARKETPLACE_AGENTES_CODEFLOWX_OS.md`. Necesita rol `AIOS_PUBLISHER`.
- **¿Cómo retiro una versión vulnerable?**  
  Marcar `Deprecated`, avisar a consumidores, actualizar Drools y ejecutar workflow de retiro.

### 3.5 Observabilidad
- **Lag en colas RabbitMQ**  
  Revisar panel `Governance Events`, escalar a SRE si > 10k mensajes en `governance.events`.
- **Métricas no aparecen en Prometheus**  
  Verificar `/actuator/prometheus`, configuración scrap, reiniciar exporter.

### 3.6 Facturación
- **¿Cómo se calcula el consumo?**  
  Por workspace: agentes activos, evaluaciones mensuales, almacenamiento de evidencias. Detalle en contrato.
- **Necesito ampliar licencias**  
  Contactar `comercial@codeflowx.com` y `support@codeflowx.com`.

## 4. Playbook de Escalado
1. **Nivel 1 (Soporte)**  
   - Aplica guías de este documento.  
   - Reproduce en sandbox si es posible.  
   - Documenta ticket con logs, métricas y pasos.
2. **Nivel 2 (Especialista Técnico)**  
   - Problemas con SDK, BPMN, Drools, telemetría.  
   - Involucrar al equipo AI OS Support.  
   - Tiempo objetivo respuesta: < 4h laboral.
3. **Nivel 3 (SRE/Platform & Compliance)**  
   - Incidentes críticos (P0/P1), brechas normativas, caída servicio.  
   - Activar runbook operativo, comité de crisis si aplica.  
   - Post-mortem obligatorio.

## 5. Matriz de Severidad
- **P0**: Caída total, violación normativa grave, datos comprometidos. Tiempo respuesta inmediato.  
- **P1**: Funcionalidad crítica degradada, webhooks masivos fallando. Tiempo respuesta < 1h.  
- **P2**: Impacto moderado, workaround disponible. Tiempo respuesta < 4h.  
- **P3**: Consulta o mejora menor. Tiempo respuesta < 2 días.

## 6. Recursos de Consulta
- Portal soporte: `https://support.codeflowx.com/aios`.  
- Base de conocimiento: `docs/aios/*`.  
- Comunidad partners: canal Matrix `#codeflowx-partners`.  
- Formación on-demand: LMS CodeflowX (módulo AI OS).

## 7. Formularios Rápidos
- **Solicitud API Key / workspace** → formulario `FORM-AIOS-ACCESS`.  
- **Reporte incidente** → `FORM-AIOS-INCIDENT`.  
- **Petición marketplace** → `FORM-AIOS-MARKETPLACE`.  
- **Feedback producto** → `FORM-AIOS-FEEDBACK`.




