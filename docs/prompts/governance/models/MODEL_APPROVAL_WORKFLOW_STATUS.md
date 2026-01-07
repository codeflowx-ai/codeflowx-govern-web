# Estado del Workflow de Aprobación de Modelos

## ✅ Workflow BPMN Implementado

**Ubicación:** `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/model-approval-v1.bpmn`

**ID del Proceso:** `model-approval-v1`

**Descripción:** Proceso de aprobación de Model ML - VERSIÓN OPTIMIZADA

**Características Avanzadas:**
- ✅ ParallelGateway - Validaciones en paralelo (3x más rápido)
- ✅ BusinessRuleTask - Drools para decisión final
- ✅ Error Boundaries - Resiliencia ante fallos
- ✅ Timer Boundary - SLA 3 días en Governance Review
- ✅ MailTask - Notificaciones automáticas

**Proporción:** 60% Automatizado - 40% HITL (ML Review + Governance Review)

### Flujo Detallado del Proceso

#### 1. **Submit Model for Approval** (User Task)
- **Grupo:** `ml-engineers`
- **FormKey:** `plataforma/workflow/model-approval-request-form.zul` ✅ **CREADO**
- **Prioridad:** 60
- **Inputs:**
  - `modelId`, `versionId`
  - `approvalType`: NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
  - `targetEnvironment`: STAGING, PRODUCTION
  - `businessJustification`

#### 2. **Validaciones en Paralelo** (Parallel Gateway - Fork)

Las 3 validaciones se ejecutan **simultáneamente** para mayor velocidad:

##### 2.1. **Performance Validation** (Service Task)
- **Delegate:** `ModelValidationDelegate`
- **Servicio:** `leka-server-serving-evaluation`
- **Output:** `performanceScore` (0-100)
- **Error Handling:** Boundary Event → Script Task "Score Conservador" (asigna 70 en caso de error)

##### 2.2. **Bias Detection** (Service Task)
- **Delegate:** `BiasDetectionDelegate`
- **Servicio:** `leka-server-serving-evaluation`
- **Output:** `biasScore` (0-100)

##### 2.3. **Compliance Check** (Service Task)
- **Delegate:** `ComplianceCheckDelegate`
- **Output:** `complianceScore` (0-100)

#### 3. **ML Engineer Review** (User Task)
- **Grupo:** `ml-engineers, senior-ml-engineers`
- **FormKey:** `plataforma/workflow/model-ml-review-form.zul` ✅ **CREADO**
- **Prioridad:** 75
- **Outputs:**
  - `mlApproval`: APPROVED, REJECTED, NEEDS_CHANGES
  - `reviewNotes`

#### 4. **Governance Review** (User Task)
- **Grupo:** `governance-admins`
- **FormKey:** `plataforma/workflow/model-governance-review-form.zul` ✅ **CREADO**
- **Prioridad:** 75
- **SLA:** 3 días (Timer Boundary - no cancela la tarea)
- **Recordatorio:** `bpmn/model-approval-reminder-form.zul` ✅ **EXISTE**
  - Se dispara al cumplir 3 días
  - Grupo: `governance-leads`
  - Prioridad: 85
- **Outputs:**
  - `governanceApproval`: APPROVED, REJECTED, CONDITIONAL
  - `riskAssessment`: LOW, MEDIUM, HIGH

#### 5. **Calculate Final Decision** (Business Rule Task - Drools)
- **Reglas DRL:** `model-approval-scoring.drl` (12 reglas)
- **Fact Input:** `ModelApprovalFact`
- **Variables Input:**
  - `performanceScore`, `biasScore`, `complianceScore`
  - `mlEngineerApproval`, `governanceApproval`
  - `targetEnvironment`
- **Output:** `finalDecision` (APPROVED / CONDITIONAL_APPROVAL / REJECTED)
- **Output Adicional:** `minScore`, `confidenceLevel`, `justification`, `requiresMonitoring`

#### 6. **Resultados Finales** (Exclusive Gateway)

##### 6.1. **APPROVED** (Condición: `finalDecision == 'APPROVED'`)
- **Service Task:** `MarkModelProductionDelegate`
  - Marca modelo como listo para producción
  - Actualiza `ModelApproval` en BD
- **Email Task:** Notificación automática
  - To: `${approverEmail}`
  - Subject: `Model Approved: ${modelName}`
  - Body: `Model approved for ${targetEnvironment}. Score: ${minScore}/100. Monitoring: ${requiresMonitoring}`
- **End Event:** `endApproved`

##### 6.2. **CONDITIONAL_APPROVAL** (Condición: `finalDecision == 'CONDITIONAL_APPROVAL'`)
- **Service Task:** `MarkModelConditionalDelegate`
  - Marca modelo como aprobado CON CONDICIONES
  - Requiere monitorización adicional
  - Actualiza `ModelApproval` en BD
- **Email Task:** Notificación automática
  - Subject: `Model Conditionally Approved: ${modelName}`
  - Body: `Model conditionally approved. Enhanced monitoring REQUIRED. Score: ${minScore}/100`
- **End Event:** `endConditional`

##### 6.3. **REJECTED** (Condición: `finalDecision == 'REJECTED'`)
- **Service Task:** `RejectModelDelegate`
  - Marca modelo como rechazado
  - Actualiza `ModelApproval` en BD
- **Email Task:** Notificación automática
  - Subject: `Model Rejected: ${modelName}`
  - Body: `Model rejected. Reason: ${justification}`
- **End Event:** `endRejected`

---

## 🔄 Integración con Backend

### Disparo del Workflow

**Archivo:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelBusinessService.java`

El workflow se dispara automáticamente al crear un nuevo modelo mediante `triggerModelApprovalWorkflowIfNeeded()`:

```java
String workflowInstanceId = bpmnWorkflowClient.startProcess(
    "model-approval-v1",
    variables
);
```

**Variables Iniciales Pasadas al Workflow:**
- `modelId` - ID del modelo (Long)
- `modelName` - Nombre del modelo (String)
- `modelType` - Tipo del modelo (String)
- `modelVersion` - Versión del modelo (String, default: "1.0.0")
- `modelDescription` - Descripción del modelo (String)
- `status` - Estado inicial (String, default: "PENDING")
- `createdBy` - Usuario creador (String)
- `createdAt` - Fecha de creación (String)

### Variables del Workflow

**Inputs del Usuario (Submit Task):**
- `modelId`, `versionId`
- `approvalType`: NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
- `targetEnvironment`: STAGING, PRODUCTION
- `businessJustification`

**Outputs de Validaciones Automáticas:**
- `performanceScore` (0-100) - De `ModelValidationDelegate`
- `biasScore` (0-100) - De `BiasDetectionDelegate`
- `complianceScore` (0-100) - De `ComplianceCheckDelegate`

**Outputs de Revisiones Humanas:**
- `mlApproval`: APPROVED, REJECTED, NEEDS_CHANGES - De ML Engineer Review
- `reviewNotes` - Notas del ML Engineer
- `governanceApproval`: APPROVED, REJECTED, CONDITIONAL - De Governance Review
- `riskAssessment`: LOW, MEDIUM, HIGH - De Governance Review

**Outputs del Business Rule Task (Drools):**
- `finalDecision`: APPROVED, CONDITIONAL_APPROVAL, REJECTED
- `minScore` - Score mínimo calculado
- `confidenceLevel` - Nivel de confianza (0.0-1.0)
- `justification` - Justificación de la decisión
- `requiresMonitoring` - Requiere monitorización adicional

### ModelApprovalFact (Drools)

**Ubicación:** `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/drools/facts/ModelApprovalFact.java`

Fact object usado por el BusinessRuleTask para calcular la decisión final mediante reglas Drools.

**Campos:**
- Inputs: `performanceScore`, `biasScore`, `complianceScore`, `mlEngineerApproval`, `governanceApproval`, `targetEnvironment`
- Outputs: `finalDecision`, `minScore`, `confidenceLevel`, `justification`, `requiresMonitoring`

**Métodos de Utilidad:**
- `calculateMinScore()` - Calcula el score mínimo
- `calculateAvgScore()` - Calcula el score promedio
- `allScoresAvailable()` - Verifica si todos los scores están disponibles
- `bothReviewsApproved()` - Verifica si ambas revisiones aprobaron
- `isProductionDeployment()` - Verifica si es deployment a PRODUCTION

**Reglas Drools:**
- Archivo: `model-approval-scoring.drl` (12 reglas)
- KieBase: `model-approval-rules`
- KieSession: `model-approval-session`

### Service Tasks y Delegates

1. **`ModelValidationDelegate`**
   - Llama a `leka-server-serving-evaluation`
   - Actualiza `ModelApproval` en BD con `performanceScore`

2. **`BiasDetectionDelegate`**
   - Llama a `leka-server-serving-evaluation`
   - Actualiza `ModelApproval` en BD con `biasScore`

3. **`ComplianceCheckDelegate`**
   - Verifica compliance regulatorio
   - Actualiza `ModelApproval` en BD con `complianceScore`

4. **`MarkModelProductionDelegate`**
   - Marca modelo como listo para producción
   - Actualiza `ModelApproval` en BD con estado APPROVED

5. **`MarkModelConditionalDelegate`**
   - Marca modelo como CONDITIONAL_APPROVAL
   - Requiere monitorización adicional
   - Actualiza `ModelApproval` en BD

6. **`RejectModelDelegate`**
   - Marca modelo como rechazado
   - Actualiza `ModelApproval` en BD con estado REJECTED

### Entidades JPA Relacionadas

**`ModelApproval`** (Tabla: `MODMODELAPPROVALS`)
- `idxmodelapproval` (PK)
- Campos relacionados con el estado de aprobación
- Se actualiza por los delegates durante el workflow

**`Model`**
- `idxmodel` (PK)
- `modapprovalstatus` - Estado de aprobación
- `modapprovedby` - Usuario que aprobó
- `modapprovedat` - Fecha de aprobación

### Notificaciones por Email

Todas las notificaciones se envían desde: `governance@company.com`

1. **Email: Model Approved**
   - To: `${approverEmail}`
   - Subject: `Model Approved: ${modelName}`
   - Body: `Model approved for ${targetEnvironment}. Score: ${minScore}/100. Monitoring: ${requiresMonitoring}`

2. **Email: Conditional Approval**
   - To: `${approverEmail}`
   - Subject: `Model Conditionally Approved: ${modelName}`
   - Body: `Model conditionally approved. Enhanced monitoring REQUIRED. Score: ${minScore}/100`

3. **Email: Model Rejected**
   - To: `${approverEmail}`
   - Subject: `Model Rejected: ${modelName}`
   - Body: `Model rejected. Reason: ${justification}`

---

## 📋 Formularios Requeridos

### ✅ Formularios Existentes

1. **`model-approval-reminder`** ✅
   - Ubicación: `app/(app)/bpmn/forms/model-approval-reminder/page.tsx`
   - Usado en: SLA Reminder (3 días)

2. **`model-approval-human-override`** ✅
   - Ubicación: `app/(app)/bpmn/forms/model-approval-human-override/page.tsx`
   - (No directamente usado en este workflow, pero relacionado)

3. **`model-evaluation-review`** ✅
   - Ubicación: `app/(app)/bpmn/forms/model-evaluation-review/page.tsx`
   - (Relacionado con evaluación de modelos)

4. **`bias-review`** ✅
   - Ubicación: `app/(app)/bpmn/forms/bias-review/page.tsx`
   - (Relacionado con revisión de sesgo)

### ✅ Formularios Creados

1. **`model-approval-request`** ✅ **CREADO**
   - Ubicación: `app/(app)/bpmn/forms/model-approval-request/page.tsx`
   - BPMN FormKey: `plataforma/workflow/model-approval-request-form.zul`
   - Usado en: Submit Model for Approval
   - Inputs implementados:
     - modelId, versionId
     - approvalType: NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
     - targetEnvironment: STAGING, PRODUCTION
     - businessJustification
   - ⚠️ Pendiente: Traducciones i18n

2. **`model-ml-review`** ✅ **CREADO**
   - Ubicación: `app/(app)/bpmn/forms/model-ml-review/page.tsx`
   - BPMN FormKey: `plataforma/workflow/model-ml-review-form.zul`
   - Usado en: ML Engineer Review
   - Outputs implementados:
     - mlApproval: APPROVED, REJECTED, NEEDS_CHANGES
     - reviewNotes
   - Muestra métricas: performanceScore, biasScore, complianceScore
   - ⚠️ Pendiente: Traducciones i18n, carga de datos del workflow

3. **`model-governance-review`** ✅ **CREADO**
   - Ubicación: `app/(app)/bpmn/forms/model-governance-review/page.tsx`
   - BPMN FormKey: `plataforma/workflow/model-governance-review-form.zul`
   - Usado en: Governance Review
   - Outputs implementados:
     - governanceApproval: APPROVED, REJECTED, CONDITIONAL
     - riskAssessment: LOW, MEDIUM, HIGH
     - reviewNotes
   - Muestra métricas y decisión previa del ML Engineer
   - ⚠️ Pendiente: Traducciones i18n, carga de datos del workflow

---

## 🔧 Corrección Aplicada

### Código Java

**Archivo:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelBusinessService.java`

**Cambio:** Actualizado el nombre del proceso de `"model-approval-workflow"` a `"model-approval-v1"` para que coincida con el ID del proceso BPMN.

---

## 📝 Proceso Completo de Validación

Cuando se registra un nuevo modelo (mediante `ModelBusinessService.createModel()`), se dispara automáticamente el workflow `model-approval-v1` que incluye:

### Fase 1: Solicitud de Aprobación (User Task)
- ML Engineer completa formulario con:
  - Tipo de aprobación (nuevo modelo, actualización, redespliegue)
  - Ambiente objetivo (staging, producción)
  - Justificación del negocio

### Fase 2: Validaciones Automáticas en Paralelo (60% Automatizado)

1. **Performance Validation** (`ModelValidationDelegate`)
   - Ejecución de tests de rendimiento
   - Validación de métricas (accuracy, precision, recall, F1-score)
   - Tests de carga y latencia
   - Llama a: `leka-server-serving-evaluation`
   - Output: `performanceScore` (0-100)
   - Error Handling: Si falla, asigna score conservador de 70

2. **Bias Detection** (`BiasDetectionDelegate`)
   - Detección de sesgos en el modelo
   - Evaluación de equidad (fairness)
   - Análisis de impacto potencial
   - Llama a: `leka-server-serving-evaluation`
   - Output: `biasScore` (0-100, donde 100 = sin bias)

3. **Compliance Check** (`ComplianceCheckDelegate`)
   - Verificación de políticas y regulaciones
   - Validación de clasificación y riesgos
   - Revisión de documentación requerida
   - Output: `complianceScore` (0-100)

### Fase 3: Revisiones Humanas (40% HITL)

1. **ML Engineer Review** (User Task)
   - Revisión técnica del modelo
   - Evaluación de métricas y resultados de validaciones
   - Decisión: APPROVED, REJECTED, NEEDS_CHANGES
   - Notas técnicas opcionales

2. **Governance Review** (User Task)
   - Revisión de aspectos éticos, riesgos y compliance
   - Evaluación de riesgo: LOW, MEDIUM, HIGH
   - Decisión: APPROVED, REJECTED, CONDITIONAL
   - SLA: 3 días (con recordatorio automático si se excede)

### Fase 4: Decisión Final (Business Rule Task - Drools)

- Ejecuta 12 reglas Drools en `model-approval-scoring.drl`
- Calcula decisión final basada en:
  - Scores de validaciones automáticas
  - Decisiones de revisiones humanas
  - Ambiente objetivo (PRODUCTION requiere scores más altos)
- Output: APPROVED, CONDITIONAL_APPROVAL, o REJECTED

### Fase 5: Acciones Finales

- **APPROVED**: Marca modelo como listo para producción → Email
- **CONDITIONAL_APPROVAL**: Marca con condiciones y monitorización requerida → Email
- **REJECTED**: Marca modelo como rechazado → Email

---

## 🚀 Estado de Implementación

1. ✅ Corregido nombre del proceso en el código Java (`model-approval-v1`)
2. ✅ Creado formulario `model-approval-request`
3. ✅ Creado formulario `model-ml-review`
4. ✅ Creado formulario `model-governance-review`

### ⚠️ Próximos Pasos Pendientes

1. ⚠️ **Agregar traducciones i18n** para los 3 nuevos formularios:
   - `model-approval-request`
   - `model-ml-review`
   - `model-governance-review`

2. ⚠️ **Configurar mapeo de formKeys del BPMN a rutas Next.js**:
   - `plataforma/workflow/model-approval-request-form.zul` → `/bpmn/forms/model-approval-request/[taskId]`
   - `plataforma/workflow/model-ml-review-form.zul` → `/bpmn/forms/model-ml-review/[taskId]`
   - `plataforma/workflow/model-governance-review-form.zul` → `/bpmn/forms/model-governance-review/[taskId]`

3. ⚠️ **Implementar carga de datos del workflow**:
   - Cargar variables del workflow (performanceScore, biasScore, complianceScore, etc.)
   - Cargar información del modelo relacionado
   - Cargar decisiones previas (mlEngineerApproval en governance review)

4. ⚠️ **Implementar integración con API de tareas BPMN**:
   - Endpoint `/api/bpmn/tasks/{taskId}/complete` debe estar implementado
   - Validar que las variables se envían correctamente al workflow

5. ⚠️ **Verificar reglas Drools**:
   - Archivo `model-approval-scoring.drl` debe existir y contener las 12 reglas
   - Verificar que `ModelApprovalFact` se mapee correctamente

6. ⚠️ **Configurar servicio de email**:
   - Verificar configuración de `governance@company.com`
   - Validar que las plantillas de email funcionen correctamente

---

## 📚 Referencias Técnicas

### Archivos Clave

- **BPMN:** `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/model-approval-v1.bpmn`
- **Fact Drools:** `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/drools/facts/ModelApprovalFact.java`
- **Reglas Drools:** `model-approval-scoring.drl` (12 reglas)
- **Business Service:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelBusinessService.java`
- **Delegates:**
  - `ModelValidationDelegate`
  - `BiasDetectionDelegate`
  - `ComplianceCheckDelegate`
  - `MarkModelProductionDelegate`
  - `MarkModelConditionalDelegate`
  - `RejectModelDelegate`
- **Entidad JPA:** `codeflowx.govern.entity/src/main/java/com/codeflowx/govern/entity/models/ModelApproval.java`
- **Tabla BD:** `MODMODELAPPROVALS`

### Grupos de Usuarios (Candidate Groups)

- `ml-engineers` - Puede iniciar proceso y revisar técnicamente
- `senior-ml-engineers` - Puede revisar técnicamente
- `governance-admins` - Puede revisar aspectos de governance
- `governance-leads` - Recibe recordatorios de SLA

### Prioridades de Tareas

- Submit Task: 60
- ML Review: 75
- Governance Review: 75
- SLA Reminder: 85

---

**Última actualización:** Diciembre 2025
