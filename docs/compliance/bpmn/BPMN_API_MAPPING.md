# Mapeo API AI-OS ↔ Procesos BPMN

> **Objetivo**: Identificar qué endpoints del API AI-OS deben disparar procesos BPMN y qué endpoints faltan para permitir integración externa completa.

## Principio de diseño

**Aplicaciones externas deben poder integrarse desde su infraestructura** sin necesidad de acceso directo al motor BPMN. El API AI-OS actúa como capa de orquestación que:
1. Recibe solicitudes de aplicaciones externas (SDK, MCP, REST)
2. Valida y enriquece el contexto
3. Dispara procesos BPMN correspondientes
4. Devuelve identificadores de proceso para seguimiento

---

## 1. Endpoints existentes que DEBEN disparar procesos BPMN

### ✅ Gestión de Componentes

| Endpoint | Proceso BPMN | Estado | Notas |
|----------|--------------|--------|-------|
| `POST /api/v1/aios/components` | `ai-component-onboarding-v1` | ⚠️ **FALTA** | Al crear componente con `type=AGENT|MODEL|PROMPT`, debe disparar onboarding automático. |
| `PUT /api/v1/aios/components/{uuid}` (cambio de status a `IN_REVIEW`) | `agent-approval-v1`<br>`model-approval-v1`<br>`prompt-approval-v1` | ⚠️ **FALTA** | Según `type` del componente, dispara el proceso de aprobación correspondiente. |
| `POST /api/v1/aios/components/{uuid}/approvals` | `agent-approval-v1`<br>`model-approval-v1`<br>`prompt-approval-v1`<br>`adapter-creation-approval-v1`<br>`finetuning-approval-v1` | ⚠️ **FALTA** | El campo `ApprovalRequest.type` determina qué proceso BPMN iniciar. |

### ✅ Marketplace y Publicación

| Endpoint | Proceso BPMN | Estado | Notas |
|----------|--------------|--------|-------|
| `POST /api/v1/aios/marketplace/publish` | `ai-marketplace-publish-v1` | ⚠️ **FALTA** | Debe iniciar proceso de publicación con scoring y revisiones legales. |

### ✅ Evaluaciones y Análisis

| Endpoint | Proceso BPMN | Estado | Notas |
|----------|--------------|--------|-------|
| `POST /api/v1/aios/bias/analyze` | `bias-detection-v1` | ⚠️ **FALTA** | Dispara proceso de detección de sesgo (puede ser asíncrono si el CSV es grande). |
| `POST /api/v1/aios/llm/evaluate` | `llm-evaluation-v1` | ⚠️ **FALTA** | Para evaluaciones que requieren aprobación HITL o scoring de compliance. |
| `POST /api/v1/aios/rag/evaluate` | `rag-evaluation-v1` | ⚠️ **FALTA** | Similar a LLM, dispara proceso si requiere governance. |
| `POST /api/v1/aios/model/invoke` (con evaluación) | `model-evaluation-v1` | ⚠️ **FALTA** | Si el request incluye `evaluate: true`, dispara proceso de evaluación. |

### ✅ Runtime y Despliegues

| Endpoint | Proceso BPMN | Estado | Notas |
|----------|--------------|--------|-------|
| `POST /api/v1/aios/runtime/deployments` | `deployment-automation-v1` | ⚠️ **FALTA** | Dispara proceso de despliegue automatizado con validaciones. |
| `POST /api/v1/aios/runtime/deployments/{uuid}/actions` (SCALE_UP/DOWN) | `deployment-automation-v1` | ⚠️ **FALTA** | Si requiere aprobación, dispara proceso. |

### ✅ Telemetría y Monitoreo (Gobernanza en Tiempo Real)

> **⚠️ IMPORTANTE**: La telemetría se ha movido a un **microservicio separado** (`codeflowx-aios-telemetry`) debido al alto volumen de tráfico. El **worker** (`codeflowx-aios-telemetry-worker`) es el responsable de disparar procesos BPMN cuando detecta problemas.

**Arquitectura:**
- **Microservicio REST** (`codeflowx-aios-telemetry`): Recibe eventos HTTP y los publica a RabbitMQ
- **Worker** (`codeflowx-aios-telemetry-worker`): Consume de RabbitMQ, realiza análisis de gobernanza y **dispara procesos BPMN**

| Componente | Proceso BPMN | Estado | Notas |
|------------|--------------|--------|-------|
| `codeflowx-aios-telemetry-worker` (severidad CRITICAL) | `ai-runtime-health-v1`<br>`alert-response-v1` | ⚠️ **FALTA** | Si `severity=CRITICAL`, dispara proceso de salud runtime o alerta. |
| `codeflowx-aios-telemetry-worker` (métricas de degradación) | `performance-degradation-v1`<br>`drift-detection-v1` | ⚠️ **FALTA** | Si detecta degradación/drift, dispara proceso correspondiente. |
| `codeflowx-aios-telemetry-worker` (detección de sesgo) | `bias-detection-v1` | ⚠️ **FALTA** | Si análisis de bias detecta `severity=HIGH\|CRITICAL`, dispara proceso. |
| `codeflowx-aios-telemetry-worker` (toxicidad detectada) | `alert-response-v1` | ⚠️ **FALTA** | Si `toxicity_score > threshold`, dispara proceso de alerta. |
| `codeflowx-aios-telemetry-worker` (violación de política) | `ai-policy-review-v1` | ⚠️ **FALTA** | Si detecta violación de política AI-OS, dispara proceso de revisión. |

> **Nota importante**: El worker `codeflowx-aios-telemetry-worker` es el **punto de procesamiento principal para gobernanza en tiempo real** desde agentes externos (chatbot, RAG, Copilot, ChatGPT, n8n). Analiza todas las interacciones recibidas vía RabbitMQ y dispara procesos BPMN automáticamente cuando detecta problemas. Ver [`GOVERNANCE_REALTIME_VS_IMMUTABLELOGS.md`](../../../nocode.service/codeflowx-aios-api/docs/GOVERNANCE_REALTIME_VS_IMMUTABLELOGS.md) y [`TELEMETRY_MICROSERVICE_ARCHITECTURE.md`](../../../nocode.service/codeflowx-aios-api/docs/TELEMETRY_MICROSERVICE_ARCHITECTURE.md) para detalles completos.

---

## 2. Endpoints que NO deben disparar procesos (solo consulta/registro)

Estos endpoints son **síncronos** y no requieren orquestación BPMN:

- `GET /api/v1/aios/components` (consulta)
- `GET /api/v1/aios/components/{uuid}` (consulta)
- `GET /api/v1/aios/approvals/pending` (consulta)
- `GET /api/v1/aios/bias/results/{uuid}` (consulta)
- `GET /api/v1/aios/audit/logs` (consulta)
- `GET /api/v1/aios/fria/{uuid}` (consulta)
- `GET /api/v1/aios/compliance/dashboard` (consulta)
- `POST /api/v1/aios/audit/logs` (registro directo, no proceso)
- `POST /api/v1/aios/telemetry/events` (⚠️ **DEPRECATED**: Movido a microservicio `codeflowx-aios-telemetry`. El worker dispara procesos BPMN, no el endpoint)

---

## 3. Procesos BPMN SIN endpoints asociados (⚠️ CRÍTICO)

### Compliance / Regulatorios

| Proceso BPMN | Endpoint faltante | Prioridad | Uso externo |
|--------------|-------------------|-----------|-------------|
| `fria-process` | `POST /api/v1/aios/fria/assessments` | 🔴 **ALTA** | Aplicaciones externas necesitan iniciar FRIA para componentes de alto riesgo. **Nota**: Actualmente solo existe `GET /api/v1/aios/fria/{componentUuid}` para consultar estado, falta el endpoint para iniciar el proceso. |
| `conformity-assessment-process` | `POST /api/v1/aios/compliance/assessments` | 🔴 **ALTA** | Iniciar evaluación de conformidad Art. 43 (Annex VI). |
| `internal-conformity-assessment-v1` | `POST /api/v1/aios/compliance/internal-assessments` | 🟡 **MEDIA** | Similar al anterior pero para evaluaciones internas. |
| `incident-reporting-process` | `POST /api/v1/aios/incidents/report` | 🔴 **ALTA** | Reportar incidentes regulatorios (Art. 20, 62, 73). |
| `eu-database-registration-process` | `POST /api/v1/aios/registrations/eu-database` | 🔴 **ALTA** | Registrar componentes en base de datos EU (Art. 49). |
| `consent-management-v1` | `POST /api/v1/aios/consents`<br>`POST /api/v1/aios/consents/{uuid}/revoke` | 🟡 **MEDIA** | Gestión de consentimientos GDPR (registro y revocación). |
| `risk-assessment-v1` | `POST /api/v1/aios/risk/assessments` | 🟡 **MEDIA** | Iniciar evaluación de riesgos para componentes. |
| `ethics-review-v1` | `POST /api/v1/aios/ethics/reviews` | 🟡 **MEDIA** | Solicitar revisión ética HITL. |
| `compliance-monitoring-v1` | (Timer interno) | ✅ OK | Se ejecuta automáticamente cada 24h, no requiere endpoint. |

### AI-OS / Aprobaciones

| Proceso BPMN | Endpoint faltante | Prioridad | Uso externo |
|--------------|-------------------|-----------|-------------|
| `external-model-approval-v1` | `POST /api/v1/aios/models/external/approve` | 🟡 **MEDIA** | Aprobar modelos de terceros con FRIA si aplica. |
| `ai-policy-review-v1` | `POST /api/v1/aios/policies/reviews` | 🟡 **MEDIA** | Iniciar revisión de políticas AI-OS. |

### Evaluaciones y Retraining

| Proceso BPMN | Endpoint faltante | Prioridad | Uso externo |
|--------------|-------------------|-----------|-------------|
| `model-retraining-orchestration-v1` | `POST /api/v1/aios/models/{uuid}/retrain` | 🟡 **MEDIA** | Programar re-entrenamiento tras degradación. |
| `dataset-quality-v1` | `POST /api/v1/aios/datasets/{uuid}/validate` | 🟡 **MEDIA** | Validar datasets antes de entrenar. |

### Audit / ISO

| Proceso BPMN | Endpoint faltante | Prioridad | Uso externo |
|--------------|-------------------|-----------|-------------|
| `iso42001-management-review-v1` | (Timer interno) | ✅ OK | Se ejecuta trimestralmente, no requiere endpoint externo. |
| `iso42001-internal-audit-v1` | `POST /api/v1/aios/audit/internal/start` | 🟢 **BAJA** | Iniciar auditoría interna (puede ser manual desde UI). |
| `iso42001-corrective-action-v1` | `POST /api/v1/aios/audit/corrective-actions` | 🟡 **MEDIA** | Crear acción correctiva desde no conformidad. |
| `iso42001-competence-gap-v1` | `POST /api/v1/aios/audit/competence-gaps` | 🟢 **BAJA** | Registrar brecha de competencias (puede ser manual). |
| `iso42001-ai-decommissioning-v1` | `POST /api/v1/aios/components/{uuid}/decommission` | 🟡 **MEDIA** | Desmantelar componente AI (data retention). |
| `iso38507-board-decision-v1` | `POST /api/v1/aios/governance/board-decisions` | 🟢 **BAJA** | Aprobación de junta (puede ser manual desde UI). |

### Metrics / Monitoreo

| Proceso BPMN | Endpoint faltante | Prioridad | Uso externo |
|--------------|-------------------|-----------|-------------|
| `incident-response-rca-v1` | (Disparado desde `incident-reporting`) | ✅ OK | Se dispara automáticamente desde `incident-reporting-process`. |

---

## 4. Endpoints propuestos (nuevos)

### Compliance / Regulatorios (Prioridad ALTA)

```yaml
# FRIA Assessment
POST /api/v1/aios/fria/assessments
Request:
  component_uuid: string (required)
  project_uuid: string (required)
  trigger_reason: string (required) # "HIGH_RISK", "REGULATORY_REQUIREMENT", "ETHICS_REVIEW"
  initial_data: object (optional)
Response:
  assessment_uuid: string
  process_instance_id: string
  status: "STARTED"
  estimated_completion: datetime

# Conformity Assessment
POST /api/v1/aios/compliance/assessments
Request:
  component_uuid: string (required)
  assessment_type: "FULL" | "INTERNAL" (required)
  notified_body_id: string (optional) # Para assessments externos
Response:
  assessment_uuid: string
  process_instance_id: string
  status: "INITIATED"

# Incident Reporting
POST /api/v1/aios/incidents/report
Request:
  component_uuid: string (required)
  incident_type: "SAFETY" | "PRIVACY" | "PERFORMANCE" | "BIAS" (required)
  severity: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL" (required)
  description: string (required)
  affected_users: number (optional)
  detected_at: datetime (required)
Response:
  incident_uuid: string
  process_instance_id: string
  status: "REPORTED"
  notification_required: boolean

# EU Database Registration
POST /api/v1/aios/registrations/eu-database
Request:
  component_uuid: string (required)
  registration_section: "A" | "B" | "C" (required) # Annex VIII
  declaration_data: object (required)
Response:
  registration_uuid: string
  process_instance_id: string
  status: "SUBMITTED"
```

### Consent Management (Prioridad MEDIA)

```yaml
# Consent Registration
POST /api/v1/aios/consents
Request:
  data_subject_id: string (required)
  component_uuid: string (required)
  consent_type: "PROCESSING" | "STORAGE" | "SHARING" (required)
  purpose: string (required)
  expiration_date: datetime (optional)
Response:
  consent_uuid: string
  process_instance_id: string
  status: "REGISTERED"

# Consent Revocation
POST /api/v1/aios/consents/{consent_uuid}/revoke
Request:
  reason: string (optional)
Response:
  consent_uuid: string
  process_instance_id: string
  status: "REVOKED"
```

### Risk Assessment (Prioridad MEDIA)

```yaml
POST /api/v1/aios/risk/assessments
Request:
  component_uuid: string (required)
  assessment_scope: "FULL" | "QUICK" (required)
  risk_categories: array<string> (optional) # ["SAFETY", "PRIVACY", "BIAS"]
Response:
  assessment_uuid: string
  process_instance_id: string
  status: "INITIATED"
```

### Ethics Review (Prioridad MEDIA)

```yaml
POST /api/v1/aios/ethics/reviews
Request:
  component_uuid: string (required)
  review_reason: string (required)
  urgency: "LOW" | "MEDIUM" | "HIGH" (required)
Response:
  review_uuid: string
  process_instance_id: string
  status: "PENDING_REVIEW"
```

### AI-OS / Decommissioning (Prioridad MEDIA)

```yaml
POST /api/v1/aios/components/{component_uuid}/decommission
Request:
  decommission_reason: string (required)
  data_retention_years: number (required)
  archive_location: string (optional)
Response:
  decommission_uuid: string
  process_instance_id: string
  status: "INITIATED"
```

---

## 5. Patrón de implementación recomendado

### En el API AI-OS (Gateway/Controller)

```java
@PostMapping("/api/v1/aios/fria/assessments")
public ResponseEntity<FriaAssessmentResponse> initiateFria(
    @Valid @RequestBody FriaAssessmentRequest request) {
    
    // 1. Validar request y enriquecer contexto
    AioComponent component = componentService.getById(request.getComponentUuid());
    
    // 2. Disparar proceso BPMN
    Map<String, Object> variables = Map.of(
        "componentUuid", request.getComponentUuid(),
        "projectUuid", request.getProjectUuid(),
        "triggerReason", request.getTriggerReason(),
        "initialData", request.getInitialData()
    );
    
    ProcessInstance instance = workflowEngineService.startProcess(
        "fria-process", 
        variables
    );
    
    // 3. Registrar en ImmutableLog
    immutableLogService.log(
        "FRIA_ASSESSMENT_INITIATED",
        component.getUuid(),
        Map.of("processInstanceId", instance.getId())
    );
    
    // 4. Retornar respuesta con identificadores
    return ResponseEntity.ok(FriaAssessmentResponse.builder()
        .assessmentUuid(UUID.randomUUID().toString())
        .processInstanceId(instance.getId())
        .status("STARTED")
        .estimatedCompletion(calculateEstimatedCompletion())
        .build());
}
```

### En el Workflow Engine

El `workflow.engine` debe exponer un servicio interno que el API AI-OS pueda consumir:

```java
@Service
public class WorkflowOrchestrationService {
    
    public ProcessInstance startProcess(
        String processKey, 
        Map<String, Object> variables,
        String businessKey) {
        
        return runtimeService.startProcessByKey(
            processKey, 
            businessKey, 
            variables
        );
    }
    
    public ProcessInstanceStatus getProcessStatus(String processInstanceId) {
        // Consultar estado y variables actuales
    }
}
```

---

## 6. Checklist de implementación

### Fase 1: Endpoints críticos (Prioridad ALTA)
- [ ] `POST /api/v1/aios/fria/assessments` → `fria-process`
- [ ] `POST /api/v1/aios/compliance/assessments` → `conformity-assessment-process`
- [ ] `POST /api/v1/aios/incidents/report` → `incident-reporting-process`
- [ ] `POST /api/v1/aios/registrations/eu-database` → `eu-database-registration-process`
- [ ] `POST /api/v1/aios/components` → `ai-component-onboarding-v1`
- [ ] `POST /api/v1/aios/components/{uuid}/approvals` → procesos de aprobación según tipo

### Fase 2: Endpoints de integración (Prioridad MEDIA)
- [ ] `POST /api/v1/aios/consents` → `consent-management-v1`
- [ ] `POST /api/v1/aios/risk/assessments` → `risk-assessment-v1`
- [ ] `POST /api/v1/aios/ethics/reviews` → `ethics-review-v1`
- [ ] `POST /api/v1/aios/components/{uuid}/decommission` → `iso42001-ai-decommissioning-v1`
- [ ] `POST /api/v1/aios/marketplace/publish` → `ai-marketplace-publish-v1`
- [ ] `POST /api/v1/aios/runtime/deployments` → `deployment-automation-v1`

### Fase 3: Endpoints de monitoreo (Prioridad MEDIA)
- [ ] `POST /api/v1/aios/bias/analyze` → `bias-detection-v1` (asíncrono si CSV grande)
- [ ] **Worker `codeflowx-aios-telemetry-worker`** → disparar `ai-runtime-health-v1`, `alert-response-v1`, `performance-degradation-v1`, `drift-detection-v1`, `bias-detection-v1`, `ai-policy-review-v1` según condiciones detectadas en análisis de gobernanza

### Fase 4: Endpoints de evaluación (Prioridad MEDIA)
- [ ] `POST /api/v1/aios/llm/evaluate` → `llm-evaluation-v1` (si requiere governance)
- [ ] `POST /api/v1/aios/rag/evaluate` → `rag-evaluation-v1` (si requiere governance)
- [ ] `POST /api/v1/aios/models/{uuid}/retrain` → `model-retraining-orchestration-v1`
- [ ] `POST /api/v1/aios/datasets/{uuid}/validate` → `dataset-quality-v1`

---

## 7. Consideraciones para aplicaciones externas

### Autenticación
- Todas las rutas requieren `X-Codeflowx-Key` (API Key)
- Agentes externos deben registrarse vía `POST /api/v1/agents/register` para obtener `agent_token`

### Respuestas asíncronas
- Procesos largos (FRIA, assessments, bias analysis) retornan `process_instance_id` y `status: "STARTED"`
- Las aplicaciones externas pueden consultar estado vía `GET /api/v1/processes/{processInstanceId}` (workflow.engine)
- O usar webhooks para notificaciones: `POST /api/v1/projects/{uuid}/webhooks`

### Paginación y seguimiento
- Usar `X-Next-Cursor` para listar instancias de procesos
- Todos los procesos registran en `ImmutableLog` para auditoría

### Manejo de errores
- Errores 4xx/5xx siguen formato `ErrorResponse` estándar
- Si el proceso BPMN falla, se retorna `code: "PROCESS_FAILED"` con `process_instance_id` para debugging

---

## 8. Resumen ejecutivo

### Endpoints que DEBEN disparar procesos: **14 endpoints existentes** (1 movido a worker)
- ✅ 3 ya tienen lógica parcial (approvals, marketplace)
- ⚠️ 11 necesitan implementación en API
- ⚠️ 1 movido a worker (`codeflowx-aios-telemetry-worker` para telemetría)

### Endpoints faltantes: **13 endpoints nuevos**
- 🔴 **ALTA prioridad**: 5 (FRIA, conformity, incidents, EU registration, component onboarding)
- 🟡 **MEDIA prioridad**: 7 (consents, risk, ethics, decommission, retrain, dataset validation)
- 🟢 **BAJA prioridad**: 1 (internal audit - puede ser manual)

### Componentes que DEBEN disparar procesos: **1 worker**
- ⚠️ `codeflowx-aios-telemetry-worker`: Disparar procesos BPMN según análisis de gobernanza (bias, toxicidad, degradación, violaciones de política)

### Total a implementar: **28 endpoints + 1 worker**
- 14 endpoints existentes que deben disparar procesos (11 en API + 1 en worker)
- 13 nuevos endpoints para procesos sin API

---

> **Nota**: Este documento debe actualizarse cuando se implementen los endpoints o se modifiquen los procesos BPMN.

