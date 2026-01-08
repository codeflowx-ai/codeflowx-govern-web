# Model Approval v1 – Guía Técnica

## Artefactos
- **BPMN**: `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/model-approval-v1.bpmn`
- **Delegates**:
  - `ModelValidationDelegate` - Valida performance del modelo (llama a `leka-server-serving-evaluation`)
  - `BiasDetectionDelegate` - Detecta bias (llama a `leka-server-serving-evaluation`)
  - `ComplianceCheckDelegate` - Verifica compliance regulatorio
  - `MarkModelProductionDelegate` - Marca modelo como listo para producción
  - `MarkModelConditionalDelegate` - Marca modelo como aprobado con condiciones
  - `RejectModelDelegate` - Marca modelo como rechazado
- **Reglas**: `rules/model/model-approval-scoring.drl` (12 reglas, sesión `model-approval-session`)
- **Fact**: `ModelApprovalFact` (`com.codeflowx.govern.workflow.drools.facts.ModelApprovalFact`)
- **Formularios Frontend (Next.js)**:
  - `plataforma/workflow/model-approval-request-form.zul` → `app/(app)/bpmn/forms/model-approval-request/page.tsx` ✅
  - `plataforma/workflow/model-ml-review-form.zul` → `app/(app)/bpmn/forms/model-ml-review/page.tsx` ✅
  - `plataforma/workflow/model-governance-review-form.zul` → `app/(app)/bpmn/forms/model-governance-review/page.tsx` ✅
  - `bpmn/model-approval-reminder-form.zul` → `app/(app)/bpmn/forms/model-approval-reminder/page.tsx` ✅

## Variables del Workflow

### Variables Iniciales (disparadas desde `ModelBusinessService.createModel()`)
- `modelId` (Long) - ID del modelo
- `modelName` (String) - Nombre del modelo
- `modelType` (String) - Tipo del modelo
- `modelVersion` (String) - Versión del modelo (default: "1.0.0")
- `modelDescription` (String) - Descripción
- `status` (String) - Estado inicial (default: "PENDING")
- `createdBy` (String) - Usuario creador
- `createdAt` (String) - Fecha de creación

### Variables de Entrada (Submit Task - User Task)
- `modelId`, `versionId`
- `approvalType`: NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
- `targetEnvironment`: STAGING, PRODUCTION
- `businessJustification` (String)

### Variables de Salida (Validaciones Automáticas)
- `performanceScore` (Integer, 0-100) - De `ModelValidationDelegate`
- `biasScore` (Integer, 0-100, donde 100 = sin bias) - De `BiasDetectionDelegate`
- `complianceScore` (Integer, 0-100) - De `ComplianceCheckDelegate`

### Variables de Salida (Revisiones Humanas)
- `mlApproval` (String): APPROVED, REJECTED, NEEDS_CHANGES - De ML Engineer Review
- `reviewNotes` (String) - Notas del ML Engineer
- `governanceApproval` (String): APPROVED, REJECTED, CONDITIONAL - De Governance Review
- `riskAssessment` (String): LOW, MEDIUM, HIGH - De Governance Review

### Variables de Salida (Business Rule Task - Drools)
- `finalDecision` (String): APPROVED, CONDITIONAL_APPROVAL, REJECTED
- `minScore` (Integer) - Score mínimo calculado
- `confidenceLevel` (Double, 0.0-1.0) - Nivel de confianza
- `justification` (String) - Justificación de la decisión
- `requiresMonitoring` (Boolean) - Requiere monitorización adicional

### Variables Adicionales
- `immutableLogId`, `deploymentTicketId`, `approverEmail`

## Flujo técnico detallado

### 1. Inicio del Workflow
- Se dispara desde `ModelBusinessService.createModel()` cuando se crea un nuevo modelo
- Se pasa a `BpmnWorkflowClient.startProcess("model-approval-v1", variables)`

### 2. Submit Model for Approval (User Task)
- Usuario ML Engineer completa formulario `model-approval-request`
- Se capturan: `modelId`, `versionId`, `approvalType`, `targetEnvironment`, `businessJustification`

### 3. Validaciones en Paralelo (Parallel Gateway)
- **Performance Validation** (`ModelValidationDelegate`):
  - Llama a `leka-server-serving-evaluation`
  - Escribe `performanceScore` (0-100)
  - Error Handling: Si falla, Boundary Event ejecuta Script Task que asigna score conservador de 70
- **Bias Detection** (`BiasDetectionDelegate`):
  - Llama a `leka-server-serving-evaluation`
  - Escribe `biasScore` (0-100, donde 100 = sin bias)
- **Compliance Check** (`ComplianceCheckDelegate`):
  - Verifica compliance regulatorio
  - Escribe `complianceScore` (0-100)

### 4. Join Gateway
- Espera a que las 3 validaciones en paralelo completen

### 5. ML Engineer Review (User Task)
- Formulario `model-ml-review` muestra métricas: `performanceScore`, `biasScore`, `complianceScore`
- ML Engineer decide: `mlApproval` (APPROVED, REJECTED, NEEDS_CHANGES)
- Opcional: `reviewNotes`

### 6. Governance Review (User Task)
- Formulario `model-governance-review` muestra métricas y decisión del ML Engineer
- Governance Admin decide: `governanceApproval` (APPROVED, REJECTED, CONDITIONAL), `riskAssessment` (LOW, MEDIUM, HIGH)
- SLA: 3 días (Timer Boundary que no cancela la tarea)
- Si se excede SLA: se dispara recordatorio `model-approval-reminder`

### 7. Business Rule Task (Drools)
- **Input:** `ModelApprovalFact` con todos los scores y aprobaciones
- **Ejecuta:** 12 reglas en `model-approval-scoring.drl`
- **Sesión Drools:** `model-approval-session`
- **Output:** `finalDecision` (APPROVED, CONDITIONAL_APPROVAL, REJECTED), `minScore`, `confidenceLevel`, `justification`, `requiresMonitoring`

### 8. Exclusive Gateway - Decisión Final
- **APPROVED** (`finalDecision == 'APPROVED'`):
  - `MarkModelProductionDelegate` → Actualiza `ModelApproval` en BD → Marca modelo como listo para producción
  - Email Task → Notificación automática
  - End Event: `endApproved`
- **CONDITIONAL_APPROVAL** (`finalDecision == 'CONDITIONAL_APPROVAL'`):
  - `MarkModelConditionalDelegate` → Marca `requiresMonitoring=true` → Actualiza `ModelApproval` en BD
  - Email Task → Notificación automática
  - End Event: `endConditional`
- **REJECTED** (`finalDecision == 'REJECTED'`):
  - `RejectModelDelegate` → Marca modelo como rechazado → Actualiza `ModelApproval` en BD
  - Email Task → Notificación automática
  - End Event: `endRejected`

## Integraciones

### Servicios Internos
- `ModelRegistry` / `ModelRepository` (persistencia JPA - tabla `MODMODELAPPROVALS`)
- `ModelApproval` (Entidad JPA) - Se actualiza por los delegates durante el workflow
- `ModelBusinessService` - Dispara el workflow al crear un modelo
- `BpmnWorkflowClient` - Cliente para iniciar procesos BPMN

### Servicios Externos
- `leka-server-serving-evaluation` - Para `ModelValidationDelegate` y `BiasDetectionDelegate`
- Email Service (MailTask) - Notificaciones automáticas desde `governance@company.com`
- `DeploymentAutomation` BPMN (call activity o REST) - Pendiente integración

### Notificaciones por Email
Todas las notificaciones se envían desde: `governance@company.com`

1. **Model Approved:**
   - To: `${approverEmail}`
   - Subject: `Model Approved: ${modelName}`
   - Body: `Model approved for ${targetEnvironment}. Score: ${minScore}/100. Monitoring: ${requiresMonitoring}`

2. **Conditional Approval:**
   - Subject: `Model Conditionally Approved: ${modelName}`
   - Body: `Model conditionally approved. Enhanced monitoring REQUIRED. Score: ${minScore}/100`

3. **Model Rejected:**
   - Subject: `Model Rejected: ${modelName}`
   - Body: `Model rejected. Reason: ${justification}`

## Entidades JPA

- **`ModelApproval`** (Tabla: `MODMODELAPPROVALS`)
  - `idxmodelapproval` (PK)
  - Campos relacionados con el estado de aprobación
  - Se actualiza por los delegates durante el workflow

- **`Model`**
  - `idxmodel` (PK)
  - `modapprovalstatus` - Estado de aprobación
  - `modapprovedby` - Usuario que aprobó
  - `modapprovedat` - Fecha de aprobación

## Grupos de Usuarios (Candidate Groups)

- `ml-engineers` - Puede iniciar proceso y revisar técnicamente (Submit Task, ML Review)
- `senior-ml-engineers` - Puede revisar técnicamente (ML Review)
- `governance-admins` - Puede revisar aspectos de governance (Governance Review)
- `governance-leads` - Recibe recordatorios de SLA (SLA Reminder)

## Prioridades de Tareas

- Submit Task: 60
- ML Review: 75
- Governance Review: 75
- SLA Reminder: 85

## Estado de Implementación

### ✅ Completado
- Workflow BPMN implementado
- Delegates Java implementados
- Formularios frontend creados (Next.js)
- Integración con `ModelBusinessService`
- Reglas Drools definidas (12 reglas)
- Fact `ModelApprovalFact` implementado

### ⚠️ Pendientes
- Traducciones i18n para formularios frontend
- Configurar mapeo de formKeys BPMN a rutas Next.js
- Implementar carga de datos del workflow en formularios
- Refactorizar para consumir los nuevos servicios de AI‑OS Runtime (telemetría, policy binding)
- Agregar métricas de explainability y fairness específicas por dominio
- Integrar con `AioDeploymentService` para despliegues automáticos
- Escribir pruebas unitarias/integrales (Flowable) que cubran los tres escenarios de decisión
- Validar reglas Drools en runtime
- Configurar servicio de email
