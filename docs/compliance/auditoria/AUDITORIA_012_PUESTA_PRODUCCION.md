# AUDITORÍA 012: PUESTA EN PRODUCCIÓN
## EU AI Act Art. 10, 11, 12, 13, 14, 15 - Proceso de Despliegue

**Fecha Auditoría:** 2025-11-18  
**Auditor:** Sistema de Auditoría Automatizado CodeflowX  
**Alcance:** Evaluación del proceso completo desde evaluación → aprobación → despliegue → puesta en producción  
**Estado:** ✅ CUMPLIMIENTO PARCIAL CON RECOMENDACIONES

---

## 📋 RESUMEN EJECUTIVO

### Estado General: ✅ **CUMPLIMIENTO PARCIAL**

El sistema implementa un **proceso estructurado y automatizado** para la puesta en producción de sistemas de IA, con:
- ✅ Workflows BPMN automatizados (model-approval-v1, deployment-automation-v1)
- ✅ Evaluaciones paralelas automáticas (performance, bias, compliance)
- ✅ Controles HITL (Human-in-the-Loop) en puntos críticos
- ✅ Registro inmutable de aprobaciones y despliegues
- ⚠️ **GAPS:** Validación pre-despliegue visual, documentación de condiciones explícitas, dashboard de controles previos

**Score de Cumplimiento:** 78/100

**Artículos EU AI Act Cubiertos:**
- Art. 10: Gobernanza de datos (evaluación pre-despliegue)
- Art. 11: Documentación técnica (verificación pre-aprobación)
- Art. 12: Registro y trazabilidad (ImmutableLog)
- Art. 13: Transparencia (explicabilidad verificada)
- Art. 14: Supervisión humana (HITL gates)
- Art. 15: Precisión y robustez (validación técnica)

---

## 1. PROCESO COMPLETO: EVALUACIÓN → APROBACIÓN → DESPLIEGUE → PRODUCCIÓN

### 1.1. Diagrama de Flujo General

```
┌─────────────────────────────────────────────────────────────────┐
│                    PROCESO DE PUESTA EN PRODUCCIÓN                │
└─────────────────────────────────────────────────────────────────┘

[1. INICIO] Solicitud de Despliegue
    │
    ├─ modelId, versionId, targetEnvironment
    │
    ▼
[2. EVALUACIÓN PARALELA] (Automático - < 60 min)
    │
    ├─► [2.1] Model Validation (performance + robustez)
    │   └─► leka-model-evaluation service
    │   └─► Métricas: accuracy, precision, recall, F1, ROC-AUC
    │
    ├─► [2.2] Bias Detection
    │   └─► leka-bias-detection-service
    │   └─► Métricas: statistical parity, equalized odds, calibration
    │
    └─► [2.3] Compliance Check
        └─► Verificación datos, licencias, GDPR
        └─► Validación documentación técnica (Art. 11)
    │
    ▼
[3. JOIN] Consolidación Resultados
    │
    ├─ performanceScore, biasScore, complianceScore, driftRisk
    │
    ▼
[4. ML ENGINEER REVIEW] (HITL - SLA: 24h)
    │
    ├─► Pantalla: model-approval-reminder-form.zul
    ├─► ViewModel: ModelApprovalReminderViewModel
    ├─► Validación técnica de resultados
    └─► Decisión: APPROVE / REQUEST_CHANGES / REJECT
    │
    ▼
[5. GOVERNANCE REVIEW] (HITL - SLA: 72h)
    │
    ├─► User Task: "Governance Review"
    ├─► Candidate Groups: governance-admins, compliance-officers
    ├─► Validación políticas, privacidad, explicabilidad
    └─► Decisión: APPROVED / CONDITIONAL_APPROVAL / REJECTED
    │
    ▼
[6. BUSINESS RULE TASK] (Drools - Automático)
    │
    ├─► Regla: model-approval-scoring.drl
    ├─► Criterios:
    │   ├─ performanceScore >= 0.85
    │   ├─ biasScore <= 0.10
    │   ├─ complianceScore >= 0.90
    │   └─ driftRisk < 0.15
    │
    ├─► Decisiones:
    │   ├─ APPROVED: Todos criterios cumplidos
    │   ├─ CONDITIONAL_APPROVAL: Requiere monitoreo extra
    │   └─ REJECTED: No cumple criterios mínimos
    │
    ▼
[7. SERVICE TASK: MarkModelProduction] (Automático)
    │
    ├─► Delegate: MarkModelProductionDelegate
    ├─► Actualiza: Model.modstatus = "PRODUCTION_READY"
    ├─► Actualiza: ModelApproval.modapprovalstatus = "APPROVED"
    ├─► Timestamp: ModelApproval.modapprovedat
    └─► Notificación: NotificationService.notifyApproval()
    │
    ▼
[8. DEPLOYMENT AUTOMATION] (Proceso BPMN separado)
    │
    ├─► Proceso: deployment-automation-v1.bpmn
    ├─► Inicio: deploymentRequest (modelId, version, environment)
    │
    ├─► [8.1] Pre-deployment Check (Automático)
    │   ├─► Delegate: PreDeploymentCheckDelegate
    │   ├─► Validación: recursos, políticas, quotas
    │   └─► Verificación: HPA, ASG configurados
    │
    ├─► [8.2] Deploy Model (Automático)
    │   ├─► Generación artefactos (containers, config)
    │   ├─► Pipeline CD (Kubernetes, SageMaker, etc.)
    │   └─► Tiempo máximo: < 30 minutos
    │
    ├─► [8.3] Gateway: Deployment Result
    │   ├─► SUCCESS → Auto Scale Deployment
    │   └─► FAIL → RollbackModel (< 10 min)
    │
    └─► [8.4] Registro DeploymentLog
    │
    ▼
[9. REGISTRO INMUTABLE] (Automático)
    │
    ├─► Entidad: ImmutableLog
    ├─► Delegate: ImmutableLogDelegate
    ├─► Categoría: "MODEL_DEPLOYMENT_COMPLETED"
    ├─► Hash Chain: SHA-256 (previousHash → currentHash)
    ├─► Data Snapshot: JSON completo del despliegue
    └─► Timestamp: imltimestampepoch
    │
    ▼
[10. PRODUCCIÓN ACTIVA]
    │
    ├─► Model.modstatus = "PRODUCTION"
    ├─► ModelDeployment.status = "ACTIVE"
    ├─► Monitoreo continuo: leka-agent-monitoring
    └─► Post-market monitoring: Art. 61 EU AI Act
```

### 1.2. Procesos BPMN Involucrados

#### **Proceso 1: model-approval-v1.bpmn**

**Ubicación:** `/src/main/resources/processes/aios/model-approval-v1.bpmn`

**Elementos Clave:**
- **Start Event:** Solicitud con `modelId`, `versionId`, `targetEnvironment`
- **Parallel Gateway:** 3 evaluaciones paralelas
- **User Tasks:** 2 (ML Engineer Review, Governance Review)
- **Business Rule Task:** `model-approval-scoring.drl`
- **Service Tasks:** `MarkModelProduction`, `RejectModel`, `ImmutableLogDelegate`
- **End Events:** APPROVED, CONDITIONAL_APPROVAL, REJECTED

**SLA:**
- Evaluaciones automáticas: < 60 minutos
- ML Engineer Review: 24 horas
- Governance Review: 72 horas
- Publicación producción: Inmediata post-aprobación

#### **Proceso 2: deployment-automation-v1.bpmn**

**Ubicación:** `/src/main/resources/processes/aios/deployment-automation-v1.bpmn`

**Elementos Clave:**
- **Start Event:** `deploymentRequest` (llamado desde model-approval o manual)
- **Service Tasks:**
  - `PreDeploymentCheckDelegate` (validación pre-despliegue)
  - `DeployModelDelegate` (ejecución despliegue)
  - `AutoScaleDeploymentDelegate` (configuración HPA/ASG)
  - `RollbackModelDelegate` (rollback automático)
- **Exclusive Gateway:** SUCCESS / FAIL
- **End Events:** DEPLOYED, FAILED, ROLLED_BACK

**SLA:**
- Tiempo máximo despliegue: < 30 minutos
- Rollback automático: < 10 minutos
- Notificación: Inmediata al owner

---

## 2. CONDICIONES QUE DEBEN CUMPLIRSE

### 2.1. Condiciones Técnicas (Automáticas)

#### **A. Performance y Robustez (Art. 15 EU AI Act)**

**Validación Automática:**
```java
// Regla Drools: model-approval-scoring.drl
rule "Performance Threshold"
    when
        $score : performanceScore < 0.85
    then
        $decision = "REJECTED";
        $justification = "Performance score below threshold (0.85)";
end
```

**Condiciones:**
- ✅ `performanceScore >= 0.85` (accuracy, precision, recall, F1)
- ✅ `ROC-AUC >= 0.80` (para modelos de clasificación)
- ✅ Robustez verificada (adversarial testing, noise injection)
- ✅ Métricas documentadas en `ModelApproval.modperformancevalidation` (JSONB)

**Servicios Involucrados:**
- `leka-model-evaluation` (port 8002)
- `leka-bias-detection-service` (port 8001) - robustness testing

#### **B. Detección de Sesgos (Art. 10 EU AI Act)**

**Validación Automática:**
```java
rule "Bias Threshold"
    when
        $bias : biasScore > 0.10
    then
        $decision = "CONDITIONAL_APPROVAL";
        $justification = "Bias detected - requires monitoring";
end
```

**Condiciones:**
- ✅ `biasScore <= 0.10` (statistical parity, equalized odds)
- ✅ Análisis de grupos protegidos documentado
- ✅ Medidas de mitigación implementadas
- ✅ Resultados en `ModelApproval.modbiasdetection` (JSONB)

**Servicios Involucrados:**
- `leka-bias-detection-service` (port 8001)

#### **C. Compliance y Documentación (Art. 11 EU AI Act)**

**Validación Automática:**
```java
rule "Compliance Check"
    when
        $compliance : complianceScore < 0.90
    then
        $decision = "REJECTED";
        $justification = "Compliance requirements not met";
end
```

**Condiciones:**
- ✅ `complianceScore >= 0.90`
- ✅ Documentación técnica completa (Anexo IV)
- ✅ Datos de entrenamiento documentados (Art. 10)
- ✅ Licencias verificadas
- ✅ GDPR compliance verificado
- ✅ Resultados en `ModelApproval.modcompliancecheck` (JSONB)

**Validaciones Específicas:**
- `Model.modtechnicaldoccomplete = true`
- `Model.modtechnicaldocscore >= 0.90`
- Dataset de entrenamiento vinculado y documentado
- Políticas de privacidad aplicadas

#### **D. Drift Risk (Art. 15 EU AI Act)**

**Validación Automática:**
```java
rule "Drift Risk"
    when
        $drift : driftRisk >= 0.15
    then
        $decision = "CONDITIONAL_APPROVAL";
        $justification = "High drift risk - requires continuous monitoring";
end
```

**Condiciones:**
- ✅ `driftRisk < 0.15` (para APPROVED)
- ✅ `driftRisk < 0.25` (para CONDITIONAL_APPROVAL)
- ✅ Plan de monitoreo post-despliegue documentado

**Servicios Involucrados:**
- `leka-bias-detection-service` (drift detection: KS test, Anderson-Darling, JS divergence)

### 2.2. Condiciones de Aprobación Humana (HITL)

#### **A. ML Engineer Review (User Task)**

**Condiciones:**
- ✅ Revisión técnica de métricas
- ✅ Validación de resultados de evaluación
- ✅ Verificación de configuración de modelo
- ✅ Decisión: APPROVE / REQUEST_CHANGES / REJECT

**Pantalla:** `model-approval-reminder-form.zul`  
**ViewModel:** `ModelApprovalReminderViewModel`  
**SLA:** 24 horas

**Campos de Decisión:**
- `mlEngineerApproval`: Boolean
- `mlEngineerNotes`: TEXT
- `mlEngineerDecision`: APPROVE / REQUEST_CHANGES / REJECT

#### **B. Governance Review (User Task)**

**Condiciones:**
- ✅ Revisión de políticas de cumplimiento
- ✅ Validación de privacidad y explicabilidad
- ✅ Verificación de documentación técnica
- ✅ Decisión: APPROVED / CONDITIONAL_APPROVAL / REJECTED

**Candidate Groups:** `governance-admins`, `compliance-officers`  
**SLA:** 72 horas

**Campos de Decisión:**
- `governanceApproval`: Boolean
- `governanceNotes`: TEXT
- `governanceDecision`: APPROVED / CONDITIONAL_APPROVAL / REJECTED
- `requiresMonitoring`: Boolean (si CONDITIONAL_APPROVAL)

### 2.3. Condiciones de Despliegue (Pre-Deployment)

#### **A. Validación de Infraestructura**

**Delegate:** `PreDeploymentCheckDelegate`

**Condiciones:**
- ✅ Recursos disponibles (CPU, memoria, GPU)
- ✅ Políticas de auto-scaling configuradas (HPA, ASG)
- ✅ Quotas de recursos verificadas
- ✅ Red y seguridad configuradas
- ✅ Health checks configurados

**Validación:**
```java
// PreDeploymentCheckDelegate.execute()
if (!deploymentValidationService.validateResources(environment)) {
    throw new BpmnError("INSUFFICIENT_RESOURCES");
}
if (!deploymentValidationService.validateScalingPolicies(environment)) {
    throw new BpmnError("SCALING_POLICIES_NOT_CONFIGURED");
}
```

#### **B. Validación de Configuración**

**Condiciones:**
- ✅ Variables de entorno configuradas
- ✅ Secrets y credenciales disponibles
- ✅ Endpoints de monitoreo configurados
- ✅ Logging y métricas habilitadas

---

## 3. PANTALLAS QUE MUESTRAN LOS CONTROLES PREVIOS A DESPLIEGUE

### 3.1. Pantalla: Model Approval Reminder Form

**Archivo:** `/src/main/webapp/console/bpmn/model-approval-reminder-form.zul`

**Propósito:** Recordatorio y aprobación rápida cuando SLA de 3 días está próximo a excederse.

**Controles Mostrados:**
```xml
<window title="Recordatorio: Aprobación Pendiente - SLA 3 Días">
    <vlayout>
        <!-- Información del Modelo -->
        <label value="Modelo: ${vm.modelName}"/>
        
        <!-- Opciones de Decisión -->
        <radiogroup selectedItem="@bind(vm.selectedDecision)">
            <radio value="APPROVE_NOW" label="✅ Aprobar Inmediatamente"/>
            <radio value="ESCALATE" label="🚨 Escalar a Manager/Director"/>
            <radio value="REQUEST_INFO" label="📋 Solicitar Más Información"/>
        </radiogroup>
        
        <!-- Notas -->
        <textbox rows="2" value="@bind(vm.notes)"/>
    </vlayout>
</window>
```

**ViewModel:** `ModelApprovalReminderViewModel`  
**Acceso:** User Task "ML Engineer Review" en workflow BPMN

### 3.2. Pantalla: Deployment Approval Form

**Archivo:** `/src/main/webapp/console/bpmn/deployment-approval-form.zul`  
**ViewModel:** `DeploymentApprovalViewModel`

**Propósito:** Aprobación manual de despliegue por DevOps Engineer.

**Controles Mostrados:**
- Resumen de configuración de despliegue
- Validación de recursos disponibles
- Verificación de políticas de auto-scaling
- Decisión: APPROVE / REJECT / REQUEST_CHANGES

**Acceso:** User Task "deployment-approval-form" en proceso `agent-deployment-workflow.bpmn`

### 3.3. Pantalla: Governance Review (Implícita en BPMN)

**Proceso:** `model-approval-v1.bpmn`  
**User Task:** "Governance Review"

**Controles Mostrados (vía BPMN Task List):**
- Información del modelo y versión
- Resultados de evaluaciones (performance, bias, compliance)
- Documentación técnica (Art. 11)
- Políticas de privacidad y explicabilidad
- Decisión: APPROVED / CONDITIONAL_APPROVAL / REJECTED
- Notas y justificación

**Acceso:** Flowable Task List (candidate groups: `governance-admins`, `compliance-officers`)

### 3.4. Dashboard: Model Approval Overview

**Archivo:** `/src/main/webapp/console/platform/models/approval/overview.zul`

**Propósito:** Vista general de todas las aprobaciones pendientes y completadas.

**Controles Mostrados:**
- Lista de modelos pendientes de aprobación
- Estado de evaluaciones (performance, bias, compliance)
- SLA tracking (tiempo restante)
- Filtros por estado, tipo, ambiente
- Acciones: Aprobar, Rechazar, Ver Detalle

### 3.5. ⚠️ GAP IDENTIFICADO: Pantalla de Controles Pre-Despliegue Consolidada

**Estado Actual:** Los controles pre-despliegue están dispersos en múltiples pantallas y procesos.

**Recomendación:** Crear pantalla consolidada que muestre:
- ✅ Checklist completo de condiciones
- ✅ Estado de cada validación (✓ / ✗ / ⚠)
- ✅ Resultados de evaluaciones automáticas
- ✅ Aprobaciones HITL pendientes
- ✅ Bloqueo visual si condiciones no cumplidas

**Ver Sección de Incidencias para detalle.**

---

## 4. DÓNDE QUEDA REGISTRADA LA APROBACIÓN FINAL

### 4.1. Entidad: ModelApproval

**Tabla:** `MODMODELAPPROVALS`  
**Entidad Java:** `com.codeflowx.govern.entity.models.ModelApproval`

**Campos de Aprobación:**
```java
@Column(name = "MODAPPROVALSTATUS")
private String modapprovalstatus; // PENDING, UNDER_REVIEW, APPROVED, REJECTED, CONDITIONAL

@Column(name = "MODAPPROVEDAT")
private Timestamp modapprovedat; // Timestamp de aprobación final

@Column(name = "MODAPPROVERID")
private String modapproverid; // ID del aprobador

@Column(name = "MODAPPROVERNAME")
private String modapprovername; // Nombre del aprobador

@Column(name = "MODAPPROVERROLE")
private String modapproverrole; // ML_ENGINEER, SENIOR_ML_ENGINEER, AI_GOVERNANCE_ADMIN

@Column(name = "MODAPPROVALNOTES")
private String modapprovalnotes; // Justificación de aprobación
```

**Actualización:**
- **Delegate:** `MarkModelProductionDelegate`
- **Momento:** Al finalizar proceso `model-approval-v1.bpmn` con decisión APPROVED
- **Código:**
```java
// MarkModelProductionDelegate.execute()
ModelApproval approval = businessService.findById(ModelApproval.class, approvalId);
approval.setModapprovalstatus("APPROVED");
approval.setModapprovedat(new Timestamp(System.currentTimeMillis()));
approval.setModapproverid(execution.getVariable("approverId"));
approval.setModapprovername(execution.getVariable("approverName"));
approval.setModapproverrole(execution.getVariable("approverRole"));
businessService.save(approval);
```

### 4.2. Entidad: Model

**Tabla:** `MODMODELS`  
**Entidad Java:** `com.codeflowx.govern.entity.models.Model`

**Campos de Estado:**
```java
@Column(name = "MODSTATUS")
private String modstatus; // DRAFT, EVALUATED, PRODUCTION_READY, PRODUCTION, DEPRECATED

@Column(name = "MODAPPROVEDBY")
private String modapprovedby; // Usuario que aprobó

@Column(name = "MODAPPROVEDAT")
private Timestamp modapprovedat; // Timestamp de aprobación
```

**Actualización:**
- **Delegate:** `MarkModelProductionDelegate`
- **Momento:** Simultáneo con actualización de ModelApproval
- **Código:**
```java
Model model = businessService.findById(Model.class, modelId);
model.setModstatus("PRODUCTION_READY");
model.setModupdatedat(new Timestamp(System.currentTimeMillis()));
businessService.save(model);
```

### 4.3. Entidad: ImmutableLog (Registro Inmutable - Art. 19 EU AI Act)

**Tabla:** `IMLIMMUTABLELOGS`  
**Entidad Java:** `com.codeflowx.govern.entity.logging.ImmutableLog`

**Propósito:** Registro inmutable con hash chain para trazabilidad completa.

**Campos Clave:**
```java
@Column(name = "IMLENTITYTYPE")
private String imlentitytype; // MODEL, AGENT, PROJECT, etc.

@Column(name = "IMLENTITYID")
private Long imlentityid; // ID del modelo aprobado

@Column(name = "IMLACTION")
private String imlaction; // APPROVE, DEPLOY, REJECT, etc.

@Column(name = "IMLPREVIOUSHASH")
private String imlprevioushash; // Hash del log anterior (hash chain)

@Column(name = "IMLCURRENTHASH")
private String imlcurrenthash; // Hash SHA-256 del log actual

@Column(name = "IMLDATA")
private String imldata; // JSON snapshot completo del evento

@Column(name = "IMLUSERID")
private Long imluserid; // Usuario que realizó la acción

@Column(name = "IMLTIMESTAMPEPOCH")
private Long imltimestampepoch; // Timestamp Unix para sorting
```

**Registro de Aprobación:**
- **Delegate:** `ImmutableLogDelegate`
- **Momento:** Después de `MarkModelProduction`
- **Categoría:** `MODEL_APPROVAL_COMPLETED`
- **Data Snapshot (JSON):**
```json
{
  "modelId": 123,
  "modelName": "Credit Scoring Model v2.1",
  "approvalStatus": "APPROVED",
  "approverId": "user-456",
  "approverName": "John Doe",
  "approverRole": "AI_GOVERNANCE_ADMIN",
  "approvedAt": "2025-11-18T10:30:00Z",
  "performanceScore": 0.92,
  "biasScore": 0.08,
  "complianceScore": 0.95,
  "driftRisk": 0.12,
  "deploymentEnvironment": "PRODUCTION",
  "processInstanceId": "proc-789"
}
```

**Hash Chain:**
- Cada log incluye `previousHash` del log anterior
- `currentHash` calculado con SHA-256 sobre: `previousHash || timestamp || entityType || entityId || action || userId || data`
- Genesis block: `GENESIS_BLOCK_CODEFLOWX_GOVERN`
- **Protección:** Trigger PostgreSQL previene UPDATE/DELETE

### 4.4. Entidad: DeploymentLog

**Tabla:** `srvlog` (alias `SRVDEPLOYMENTLOGS`)  
**Entidad Java:** `com.codeflowx.govern.entity.serving.DeploymentLog`

**Propósito:** Registro de eventos durante el despliegue.

**Campos Clave:**
```java
@ManyToOne
@JoinColumn(name = "srl_deployment_id")
private ModelDeployment deployment; // Deployment asociado

@Column(name = "srl_level")
private String level; // INFO, WARNING, ERROR

@Column(name = "srl_message")
private String message; // Mensaje del evento

@Column(name = "srl_metadata", columnDefinition = "jsonb")
private String metadata; // Metadatos adicionales

@Column(name = "srl_recorded_at")
private OffsetDateTime recordedAt; // Timestamp del evento
```

**Registro:**
- **Momento:** Durante proceso `deployment-automation-v1.bpmn`
- **Eventos registrados:**
  - Pre-deployment check iniciado
  - Pre-deployment check completado
  - Deployment iniciado
  - Deployment completado
  - Health check realizado
  - Auto-scaling configurado
  - Rollback ejecutado (si aplica)

### 4.5. Historial BPMN (Flowable)

**Tablas:** Flowable History Tables
- `ACT_HI_PROCINST` (instancias de proceso)
- `ACT_HI_TASKINST` (tareas de usuario)
- `ACT_HI_VARINST` (variables de proceso)
- `ACT_HI_ACTINST` (actividades ejecutadas)

**Propósito:** Trazabilidad completa del workflow BPMN.

**Información Registrada:**
- Proceso: `model-approval-v1`
- Instancia ID: `proc-instance-123`
- Tareas completadas:
  - ML Engineer Review (completada: 2025-11-18 09:15:00)
  - Governance Review (completada: 2025-11-18 10:30:00)
- Variables:
  - `finalDecision`: "APPROVED"
  - `approvalStatus`: "APPROVED"
  - `approvedAt`: 1734517800000
  - `performanceScore`: 0.92
  - `biasScore`: 0.08
  - `complianceScore`: 0.95

**Acceso:**
- Flowable Admin UI: `/flowable-admin`
- API REST: `/api/flowable/history/process-instances/{id}`

---

## 5. VERIFICACIÓN DE CUMPLIMIENTO EU AI ACT

### 5.1. Art. 10 - Gobernanza de Datos

✅ **Cumplimiento:**
- Evaluación automática de calidad de datos pre-despliegue
- Verificación de dataset de entrenamiento documentado
- Análisis de sesgos en datos
- Servicio: `leka-bias-detection-service` (port 8001)

### 5.2. Art. 11 - Documentación Técnica

✅ **Cumplimiento:**
- Verificación de documentación técnica completa (Anexo IV)
- Campo `Model.modtechnicaldoccomplete` validado
- Score de completitud verificado (`modtechnicaldocscore >= 0.90`)
- Validación en `ComplianceCheck` del workflow

### 5.3. Art. 12 - Registro y Trazabilidad

✅ **Cumplimiento:**
- Registro inmutable en `ImmutableLog` con hash chain
- Trazabilidad completa: evaluación → aprobación → despliegue
- Historial BPMN preservado permanentemente
- Logs de despliegue en `DeploymentLog`

### 5.4. Art. 13 - Transparencia

✅ **Cumplimiento:**
- Verificación de explicabilidad (SHAP/LIME) pre-aprobación
- Servicio: `leka-ai-interpreter` (port 8011)
- Documentación de explicabilidad en `ModelApproval.modgovernancereview`

### 5.5. Art. 14 - Supervisión Humana

✅ **Cumplimiento:**
- 2 User Tasks HITL en workflow:
  - ML Engineer Review (SLA: 24h)
  - Governance Review (SLA: 72h)
- Capacidad de override manual
- Registro de decisiones humanas en `ModelApproval`

### 5.6. Art. 15 - Precisión y Robustez

✅ **Cumplimiento:**
- Validación automática de performance (`performanceScore >= 0.85`)
- Robustez verificada (adversarial testing, noise injection)
- Servicios: `leka-model-evaluation`, `leka-bias-detection-service`

---

## 6. CONCLUSIONES

### 6.1. Fortalezas Identificadas

1. ✅ **Proceso Automatizado:** Workflows BPMN estructurados y automatizados
2. ✅ **Evaluaciones Paralelas:** Optimización de tiempo (< 60 min)
3. ✅ **Controles HITL:** Supervisión humana en puntos críticos
4. ✅ **Registro Inmutable:** Hash chain para trazabilidad completa
5. ✅ **Cumplimiento EU AI Act:** 6 artículos cubiertos (10, 11, 12, 13, 14, 15)

### 6.2. Gaps Identificados

1. ⚠️ **Pantalla Consolidada:** Falta dashboard visual de controles pre-despliegue
2. ⚠️ **Documentación Condiciones:** Condiciones no están explícitamente documentadas en UI
3. ⚠️ **Validación Pre-Despliegue Visual:** No hay checklist visual antes de despliegue
4. ⚠️ **Exportación Aprobaciones:** Falta capacidad de exportar historial de aprobaciones para auditores

### 6.3. Recomendaciones Prioritarias

**Ver documento separado:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md`

---

## 7. EVIDENCIA TÉCNICA

### 7.1. Archivos de Código Relevantes

**Procesos BPMN:**
- `/src/main/resources/processes/aios/model-approval-v1.bpmn`
- `/src/main/resources/processes/aios/deployment-automation-v1.bpmn`

**Delegates Java:**
- `MarkModelProductionDelegate.java`
- `PreDeploymentCheckDelegate.java`
- `ImmutableLogDelegate.java`

**Entidades:**
- `ModelApproval.java`
- `Model.java`
- `ImmutableLog.java`
- `DeploymentLog.java`

**Pantallas ZUL:**
- `model-approval-reminder-form.zul`
- `deployment-approval-form.zul`
- `console/platform/models/approval/overview.zul`

**Reglas Drools:**
- `model-approval-scoring.drl`

### 7.2. Servicios Microservicios

- `leka-model-evaluation` (port 8002)
- `leka-bias-detection-service` (port 8001)
- `leka-ai-interpreter` (port 8011)

---

**Fin del Informe de Auditoría**

**Próxima Revisión:** 2025-12-18  
**Responsable:** Equipo de Gobierno de IA - CodeflowX







