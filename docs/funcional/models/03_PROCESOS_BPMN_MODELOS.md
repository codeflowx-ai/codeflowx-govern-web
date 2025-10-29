# 🔄 PROCESOS BPMN - MÓDULO MODELOS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación completa de procesos BPMN para el gobierno de modelos de IA

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **modelos** utiliza **3 procesos BPMN principales** que automatizan completamente el ciclo de vida de modelos de IA, integrando **validaciones paralelas**, **evaluaciones automáticas** y **orquestación de reentrenamiento** mediante **Drools Rules Engine** y **delegates especializados**.

### **Características de los Procesos:**
- **Automatización completa** del workflow de modelos
- **Validaciones paralelas** de rendimiento, sesgo y compliance
- **Decisión inteligente** entre auto-aprobación y revisión humana
- **Orquestación de reentrenamiento** con A/B testing
- **Trazabilidad total** de decisiones y cambios

---

## 🔄 PROCESO PRINCIPAL: MODEL APPROVAL

### **Identificación del Proceso:**
- **ID:** `model-approval-v1`
- **Nombre:** Model Approval Process V1
- **Versión:** 1.0
- **Namespace:** `http://www.activiti.org/test`

### **Flujo del Proceso:**

```
┌─────────────┐    ┌─────────────────┐    ┌──────────────┐
│ Start Event │───▶│ Submit Request  │───▶│Parallel Fork │
└─────────────┘    └─────────────────┘    └──────────────┘
                                                      │
                    ┌─────────────────────────────────┼─────────────────────────────────┐
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │Performance   │                │Bias          │                │Compliance    │
            │Validation    │                │Detection     │                │Check         │
            └──────────────┘                └──────────────┘                └──────────────┘
                    │                                 │                                 │
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │Parallel Join │                │ML Engineer   │                │Governance    │
            │              │                │Review        │                │Review        │
            └──────────────┘                └──────────────┘                └──────────────┘
                    │                                 │                                 │
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │Drools        │                │Decision      │                │Approved?     │
            │Scoring       │                │Gateway       │                │Gateway       │
            └──────────────┘                └──────────────┘                └──────────────┘
                    │                                 │                                 │
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │Mark          │                │Conditional   │                │Reject        │
            │Production    │                │Approval      │                │Model         │
            └──────────────┘                └──────────────┘                └──────────────┘
```

---

## 📋 TAREAS DEL PROCESO DE APROBACIÓN

### **1. User Tasks (Tareas de Usuario)**

#### **Submit Model for Approval**
- **ID:** `submitTask`
- **Nombre:** Submit Model for Approval
- **Candidatos:** `ml-engineers`
- **Formulario:** `plataforma/workflow/model-approval-request-form.zul`
- **Prioridad:** 60
- **Propósito:** Solicitud inicial de aprobación de modelo
- **Variables de entrada:**
  - `modelId` (Long) - ID del modelo a aprobar
  - `versionId` (Long) - ID de la versión
  - `approvalType` (String) - Tipo: NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
  - `targetEnvironment` (String) - STAGING, PRODUCTION
  - `businessJustification` (String) - Justificación del negocio

#### **ML Engineer Review**
- **ID:** `mlReviewTask`
- **Nombre:** ML Engineer Review
- **Candidatos:** `ml-engineers`, `senior-ml-engineers`
- **Formulario:** `plataforma/workflow/model-ml-review-form.zul`
- **Prioridad:** 75
- **Propósito:** Revisión técnica por ML Engineer
- **Variables de entrada:**
  - `modelId` (Long) - ID del modelo
  - `performanceScore` (Integer) - Score de rendimiento
  - `biasScore` (Integer) - Score de sesgo
  - `complianceScore` (Integer) - Score de compliance
- **Variables de salida:**
  - `mlApproval` (String) - APPROVED, REJECTED, NEEDS_CHANGES
  - `reviewNotes` (String) - Notas de la revisión

#### **Governance Review**
- **ID:** `govReviewTask`
- **Nombre:** Governance Review
- **Candidatos:** `governance-admins`
- **Formulario:** `plataforma/workflow/model-governance-review-form.zul`
- **Prioridad:** 75
- **Propósito:** Revisión de governance (ética, riesgos, compliance)
- **Variables de entrada:**
  - `modelId` (Long) - ID del modelo
  - `mlApproval` (String) - Aprobación del ML Engineer
  - `validationResults` (JSON) - Resultados de validaciones
- **Variables de salida:**
  - `governanceApproval` (String) - APPROVED, REJECTED, CONDITIONAL
  - `riskAssessment` (String) - LOW, MEDIUM, HIGH
  - `governanceNotes` (String) - Notas de governance

#### **SLA Reminder**
- **ID:** `slaReminder`
- **Nombre:** SLA Reminder
- **Candidatos:** `governance-leads`
- **Formulario:** `bpmn/model-approval-reminder-form.zul`
- **Prioridad:** 85
- **Propósito:** Recordatorio cuando se excede SLA de 3 días

### **2. Service Tasks (Tareas de Servicio)**

#### **Performance Validation**
- **ID:** `performanceTask`
- **Nombre:** Performance Validation
- **Delegate:** `com.codeflowx.govern.workflow.delegates.ModelValidationDelegate`
- **Propósito:** Validación de rendimiento del modelo
- **Integración:** `leka-server-serving-evaluation`
- **Variables de entrada:**
  - `modelId` (Long) - ID del modelo
  - `versionId` (Long) - ID de la versión
- **Variables de salida:**
  - `performanceScore` (Integer) - Score de rendimiento (0-100)

#### **Bias Detection**
- **ID:** `biasTask`
- **Nombre:** Bias Detection
- **Delegate:** `com.codeflowx.govern.workflow.delegates.BiasDetectionDelegate`
- **Propósito:** Detección de sesgo en el modelo
- **Integración:** `leka-server-serving-evaluation`
- **Variables de entrada:**
  - `modelId` (Long) - ID del modelo
  - `versionId` (Long) - ID de la versión
- **Variables de salida:**
  - `biasScore` (Integer) - Score de sesgo (0-100)

#### **Compliance Check**
- **ID:** `complianceTask`
- **Nombre:** Compliance Check
- **Delegate:** `com.codeflowx.govern.workflow.delegates.ComplianceCheckDelegate`
- **Propósito:** Verificación de compliance regulatorio
- **Variables de entrada:**
  - `modelId` (Long) - ID del modelo
  - `targetEnvironment` (String) - Entorno objetivo
- **Variables de salida:**
  - `complianceScore` (Integer) - Score de compliance (0-100)

#### **Calculate Final Decision (Drools)**
- **ID:** `droolsScore`
- **Nombre:** Calculate Final Decision (Drools)
- **Tipo:** BusinessRuleTask
- **Reglas:** `model-approval-scoring`
- **Propósito:** Ejecuta reglas Drools para decisión final
- **Variables de entrada:**
  - `modelApprovalFact` (Object) - Fact con scores y aprobaciones
- **Variables de salida:**
  - `finalDecision` (String) - APPROVED, CONDITIONAL_APPROVAL, REJECTED
  - `minScore` (Integer) - Score mínimo
  - `confidenceLevel` (Double) - Nivel de confianza
  - `justification` (String) - Justificación
  - `requiresMonitoring` (Boolean) - Requiere monitoreo

#### **Mark Production Ready**
- **ID:** `approveTask`
- **Nombre:** Mark Production Ready
- **Delegate:** `com.codeflowx.govern.workflow.delegates.MarkModelProductionDelegate`
- **Propósito:** Marca modelo como listo para producción

#### **Email Notifications**
- **ID:** `mailApprove`, `mailReject`, `mailConditional`
- **Nombre:** Email Notifications
- **Tipo:** MailTask
- **Propósito:** Notificaciones automáticas por email

---

## 🔀 GATEWAYS Y DECISIONES

### **1. Parallel Gateway - Fork**
- **ID:** `fork`
- **Nombre:** Fork Validaciones
- **Propósito:** División del flujo en validaciones paralelas
- **Flujos de salida:**
  - `flowToPerformance` → Performance Validation
  - `flowToBias` → Bias Detection
  - `flowToCompliance` → Compliance Check

### **2. Parallel Gateway - Join**
- **ID:** `join`
- **Nombre:** Join Validaciones
- **Propósito:** Convergencia de validaciones paralelas
- **Flujos de entrada:**
  - `flowFromPerformance` ← Performance Validation
  - `flowFromBias` ← Bias Detection
  - `flowFromCompliance` ← Compliance Check

### **3. Exclusive Gateway - Decision**
- **ID:** `gateway`
- **Nombre:** Decisión Final
- **Propósito:** Decisión basada en resultado de Drools
- **Condiciones:**
  - **Approved:** `${finalDecision == 'APPROVED'}`
  - **Conditional:** `${finalDecision == 'CONDITIONAL_APPROVAL'}`
  - **Rejected:** `${finalDecision == 'REJECTED'}`

---

## 🧠 INTEGRACIÓN CON DROOLS

### **Reglas de Decisión:**

#### **Auto-Approve Rule**
```drl
rule "Auto-Approve Model - High Performance and Low Risk"
    salience 100
    when
        $fact : ModelApprovalFact(
            performanceScore >= 85,
            biasScore >= 80,
            complianceScore >= 90,
            mlEngineerApproval == "APPROVED",
            governanceApproval == "APPROVED",
            riskAssessment == "LOW"
        )
    then
        $fact.setFinalDecision("APPROVED");
        $fact.setConfidenceLevel(0.95);
        $fact.setJustification("High scores across all dimensions with approvals");
        $fact.setRequiresMonitoring(false);
        logger.info("Model auto-approved");
end
```

#### **Conditional Approval Rule**
```drl
rule "Conditional Approval - Medium Risk"
    salience 80
    when
        $fact : ModelApprovalFact(
            performanceScore >= 70 && performanceScore < 85,
            biasScore >= 60 && biasScore < 80,
            complianceScore >= 80 && complianceScore < 90,
            mlEngineerApproval == "APPROVED",
            governanceApproval == "CONDITIONAL"
        )
    then
        $fact.setFinalDecision("CONDITIONAL_APPROVAL");
        $fact.setConfidenceLevel(0.75);
        $fact.setJustification("Medium risk model requires monitoring");
        $fact.setRequiresMonitoring(true);
        logger.info("Model conditionally approved");
end
```

#### **Rejection Rule**
```drl
rule "Reject Model - Critical Issues"
    salience 200
    when
        $fact : ModelApprovalFact(
            performanceScore < 60 || 
            biasScore < 50 || 
            complianceScore < 70 ||
            mlEngineerApproval == "REJECTED" ||
            governanceApproval == "REJECTED"
        )
    then
        $fact.setFinalDecision("REJECTED");
        $fact.setConfidenceLevel(1.0);
        $fact.setJustification("Critical issues detected");
        $fact.setRequiresMonitoring(false);
        logger.warn("Model rejected due to critical issues");
end
```

---

## 🔄 PROCESO DE EVALUACIÓN

### **Identificación del Proceso:**
- **ID:** `model-evaluation-v1`
- **Nombre:** Model Evaluation Process V1
- **Versión:** 1.0
- **Namespace:** `http://codeflowx.com/govern/model-evaluation`

### **Flujo del Proceso:**

```
┌─────────────┐    ┌─────────────────┐    ┌──────────────┐
│ Start Event │───▶│ Execute         │───▶│ Store        │
│             │    │ Evaluation      │    │ Results      │
└─────────────┘    └─────────────────┘    └──────────────┘
                                                      │
                                                      ▼
                                            ┌─────────────────┐
                                            │ Performance     │
                                            │ Below Threshold?│
                                            └─────────────────┘
                                                      │
                    ┌─────────────────────────────────┼─────────────────────────────────┐
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │ Create       │                │ Update       │                │ End Success  │
            │ Alert        │                │ Dashboard    │                │              │
            └──────────────┘                └──────────────┘                └──────────────┘
```

### **Tareas del Proceso de Evaluación:**

#### **Execute Model Evaluation**
- **ID:** `executeEvaluation`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.ModelEvaluationDelegate`
- **Propósito:** Ejecuta evaluación completa del modelo

#### **Store Evaluation Results**
- **ID:** `storeResults`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.StoreEvaluationDelegate`
- **Propósito:** Almacena resultados de la evaluación

#### **Create Model Alert**
- **ID:** `createAlert`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.CreateModelAlertDelegate`
- **Propósito:** Crea alerta cuando el rendimiento está por debajo del umbral

#### **Update Dashboard**
- **ID:** `updateDashboard`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.UpdateComplianceDashboardDelegate`
- **Propósito:** Actualiza dashboard de compliance

---

## 🔄 PROCESO DE REENTRENAMIENTO

### **Identificación del Proceso:**
- **ID:** `model-retraining-orchestration-v1`
- **Nombre:** Model Retraining Orchestration V1
- **Versión:** 1.0
- **Namespace:** `http://codeflowx.com/govern/retraining`

### **Flujo del Proceso:**

```
┌─────────────┐    ┌─────────────────┐    ┌──────────────┐
│ Start       │───▶│ Parallel        │───▶│ Prepare      │
│ Retraining  │    │ Prepare         │    │ Data         │
└─────────────┘    └─────────────────┘    └──────────────┘
                            │
                            ▼
                   ┌─────────────────┐
                   │ Validate        │
                   │ Infrastructure  │
                   └─────────────────┘
                            │
                            ▼
                   ┌─────────────────┐
                   │ Parallel        │
                   │ Merge           │
                   └─────────────────┘
                            │
                            ▼
                   ┌─────────────────┐
                   │ Decide          │
                   │ Strategy        │
                   └─────────────────┘
                            │
                    ┌───────┼───────┐
                    ▼       ▼       ▼
            ┌──────────┐ ┌──────┐ ┌──────────┐
            │ Skip     │ │Retrain│ │ Approval │
            │          │ │      │ │          │
            └──────────┘ └──────┘ └──────────┘
```

### **Tareas del Proceso de Reentrenamiento:**

#### **Prepare Training Data**
- **ID:** `prepareData`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.PrepareTrainingDataDelegate`
- **Propósito:** Prepara datos de entrenamiento

#### **Validate Infrastructure**
- **ID:** `validateInfrastructure`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.ValidateInfrastructureDelegate`
- **Propósito:** Valida infraestructura para reentrenamiento

#### **Decide Retraining Strategy**
- **ID:** `decideRetrainingStrategy`
- **Tipo:** BusinessRuleTask
- **Propósito:** Decide estrategia de reentrenamiento usando Drools

#### **Execute Retraining**
- **ID:** `executeRetraining`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.ExecuteRetrainingDelegate`
- **Async:** true
- **Timeout:** 6 horas
- **Propósito:** Ejecuta reentrenamiento del modelo

#### **Evaluate New Model**
- **ID:** `evaluateNewModel`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.EvaluateNewModelDelegate`
- **Propósito:** Evalúa el nuevo modelo entrenado

#### **Setup A/B Testing**
- **ID:** `setupABTesting`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.SetupABTestingDelegate`
- **Propósito:** Configura A/B testing

#### **Monitor A/B Test**
- **ID:** `monitorABTest`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.MonitorABTestDelegate`
- **Propósito:** Monitorea A/B test

#### **Promote to Production**
- **ID:** `promoteToProduction`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.PromoteModelDelegate`
- **Propósito:** Promueve modelo a producción

#### **Rollback Model**
- **ID:** `rollbackModel`
- **Delegate:** `com.codeflowx.govern.workflow.delegates.RollbackModelDelegate`
- **Propósito:** Hace rollback del modelo

---

## 📊 MÉTRICAS Y KPIs DE LOS PROCESOS

### **Métricas de Tiempo:**
- **Tiempo promedio de aprobación:** 2-5 días
- **Tiempo de validaciones paralelas:** 30-60 minutos
- **SLA de governance review:** 3 días
- **Tiempo de reentrenamiento:** 2-6 horas

### **Métricas de Calidad:**
- **Tasa de auto-aprobación:** 40-60%
- **Tasa de aprobación condicional:** 20-30%
- **Tasa de rechazo:** 10-20%
- **Precisión de validaciones:** 95%+

### **Métricas de Volumen:**
- **Modelos procesados por día:** 10-50
- **Pico de procesamiento:** 20-100 modelos/día
- **Capacidad máxima:** 200 modelos/día

---

## 🔧 CONFIGURACIÓN Y PARÁMETROS

### **Parámetros del Proceso de Aprobación:**
- **SLA de governance:** 3 días
- **Timeout de validaciones:** 2 horas
- **Retry attempts:** 3
- **Escalamiento:** Después de SLA

### **Umbrales de Decisión:**
- **Performance Score mínimo:** 60
- **Bias Score mínimo:** 50
- **Compliance Score mínimo:** 70
- **Auto-approve Performance:** 85
- **Auto-approve Bias:** 80
- **Auto-approve Compliance:** 90

### **Configuración de Reentrenamiento:**
- **Timeout de reentrenamiento:** 6 horas
- **Umbral de mejora:** 5%
- **Duración de A/B test:** 7 días
- **Umbral de éxito A/B:** 10%

---

## 🚨 MANEJO DE ERRORES

### **Errores Comunes:**
1. **Modelo no encontrado**
   - **Causa:** ID de modelo inválido
   - **Acción:** Terminar proceso con error
   - **Notificación:** Email al solicitante

2. **Validación fallida**
   - **Causa:** Error en servicio de validación
   - **Acción:** Score conservador (70)
   - **Escalamiento:** Notificar a administradores

3. **Timeout de reentrenamiento**
   - **Causa:** Reentrenamiento excede 6 horas
   - **Acción:** Rollback automático
   - **Notificación:** Email a ML engineers

### **Recuperación de Errores:**
- **Retry automático** para errores transitorios
- **Escalamiento** para errores persistentes
- **Rollback** para cambios de estado
- **Logging completo** para debugging

---

## 📈 OPTIMIZACIONES Y MEJORAS

### **Optimizaciones Implementadas:**
- **Validaciones paralelas** para reducir tiempo
- **Caché de resultados** de validaciones
- **Async reentrenamiento** para mejor rendimiento
- **A/B testing automático** para validación

### **Mejoras Futuras:**
- **ML-based scoring** para decisiones más precisas
- **Predictive retraining** basado en drift
- **Multi-model comparison** automático
- **Integration con más validadores** externos

---

## ✅ CONCLUSIÓN

Los **procesos BPMN del módulo modelos** proporcionan una **automatización completa** y **robusta** del gobierno de modelos de IA con:

- 🔄 **3 procesos automatizados** para aprobación, evaluación y reentrenamiento
- 🛡️ **Validaciones paralelas** de rendimiento, sesgo y compliance
- 🧠 **Integración con Drools** para decisiones complejas
- 📊 **Métricas completas** para monitoreo y optimización
- 🚨 **Manejo robusto** de errores y excepciones
- 📈 **Escalabilidad** para alto volumen de modelos

**Estos procesos están optimizados** para manejar el gobierno de modelos de IA de manera eficiente, segura y compliant con regulaciones.

