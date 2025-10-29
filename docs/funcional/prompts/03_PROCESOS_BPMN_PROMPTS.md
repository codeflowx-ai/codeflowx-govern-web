# 🔄 PROCESOS BPMN - MÓDULO PROMPTS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación completa de procesos BPMN para el gobierno de prompts de IA

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **prompts** utiliza **1 proceso BPMN principal** (`prompt-approval-v1`) que automatiza completamente el ciclo de aprobación de prompts de IA, integrando **validaciones de seguridad**, **compliance** y **decisiones inteligentes** mediante **Drools Rules Engine**.

### **Características del Proceso:**
- **Automatización completa** del workflow de aprobación
- **Validaciones paralelas** de seguridad y compliance
- **Decisión inteligente** entre auto-aprobación y revisión humana
- **Trazabilidad total** de decisiones y cambios
- **Notificaciones automáticas** a stakeholders

---

## 🔄 PROCESO PRINCIPAL: PROMPT APPROVAL

### **Identificación del Proceso:**
- **ID:** `prompt-approval-v1`
- **Nombre:** Prompt Approval Process V1
- **Versión:** 1.0
- **Namespace:** `http://codeflowx.com/govern/prompt-approval`

### **Flujo del Proceso:**

```
┌─────────────┐    ┌─────────────────┐    ┌──────────────┐
│ Start Event │───▶│ Request Approval│───▶│Parallel Start│
└─────────────┘    └─────────────────┘    └──────────────┘
                                                      │
                                                      ▼
                                            ┌─────────────────┐
                                            │   Safety Check  │
                                            └─────────────────┘
                                                      │
                                                      ▼
                                            ┌─────────────────┐
                                            │Parallel Merge   │
                                            └─────────────────┘
                                                      │
                                                      ▼
                                            ┌─────────────────┐
                                            │Auto-Approve?    │
                                            └─────────────────┘
                                                      │
                    ┌─────────────────────────────────┼─────────────────────────────────┐
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │Auto-Approve  │                │Human Review  │                │Approved?     │
            └──────────────┘                └──────────────┘                └──────────────┘
                    │                                 │                                 │
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │Notify        │                │Provide       │                │Notify        │
            │Approval      │                │Rejection     │                │Rejection    │
            └──────────────┘                │Reason        │                └──────────────┘
                    │                       └──────────────┘                         │
                    │                                 │                                 │
                    ▼                                 ▼                                 ▼
            ┌──────────────┐                ┌──────────────┐                ┌──────────────┐
            │End Approved  │                │Notify        │                │End Rejected │
            └──────────────┘                │Rejection     │                └──────────────┘
                                           └──────────────┘
```

---

## 📋 TAREAS DEL PROCESO

### **1. User Tasks (Tareas de Usuario)**

#### **Request Approval**
- **ID:** `requestApproval`
- **Nombre:** Request Prompt Approval
- **Candidatos:** `ml-engineers`
- **Formulario:** `prompt-approval-request-form`
- **Propósito:** Solicitud inicial de aprobación de prompt
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt a aprobar
  - `approvalType` (String) - Tipo de aprobación
  - `requestReason` (String) - Razón de la solicitud
- **Variables de salida:**
  - `approvalId` (Long) - ID de la aprobación creada
  - `requestData` (JSON) - Datos de la solicitud

#### **Human Review**
- **ID:** `humanReview`
- **Nombre:** Human Review
- **Candidatos:** `prompt-engineers`, `ml-engineers`
- **Formulario:** `prompt-human-review-form`
- **Propósito:** Revisión humana cuando la auto-aprobación no es posible
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt
  - `safetyCheckResult` (JSON) - Resultado de verificación de seguridad
  - `complianceCheckResult` (JSON) - Resultado de verificación de compliance
- **Variables de salida:**
  - `approved` (Boolean) - Decisión de aprobación
  - `reviewNotes` (String) - Notas de la revisión
  - `approverId` (String) - ID del aprobador

#### **Provide Rejection Reason**
- **ID:** `provideRejectionReason`
- **Nombre:** Provide Rejection Reason
- **Candidatos:** `prompt-engineers`, `ml-engineers`
- **Formulario:** `prompt-rejection-form`
- **Propósito:** Proporcionar razón detallada del rechazo
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt rechazado
  - `rejectionReason` (String) - Razón del rechazo
- **Variables de salida:**
  - `rejectionDetails` (JSON) - Detalles del rechazo
  - `suggestions` (String) - Sugerencias de mejora

### **2. Service Tasks (Tareas de Servicio)**

#### **Safety Check**
- **ID:** `safetyCheck`
- **Nombre:** Prompt Safety Check
- **Delegate:** `com.codeflowx.govern.workflow.delegates.PromptSafetyDelegate`
- **Propósito:** Verificación automática de seguridad del prompt
- **Funcionalidades:**
  - Detección de jailbreak attempts
  - Detección de injection attacks
  - Análisis de contenido malicioso
  - Cálculo de score de seguridad
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt
  - `promptContent` (String) - Contenido del prompt
- **Variables de salida:**
  - `safetyScore` (Integer) - Score de seguridad (0-100)
  - `jailbreakDetected` (Boolean) - Jailbreak detectado
  - `injectionDetected` (Boolean) - Injection detectado
  - `maliciousContentDetected` (Boolean) - Contenido malicioso detectado
  - `safetyRecommendation` (String) - Recomendaciones de seguridad

#### **Compliance Check**
- **ID:** `complianceCheck`
- **Nombre:** Compliance Check
- **Delegate:** `com.codeflowx.govern.workflow.delegates.ComplianceCheckDelegate`
- **Propósito:** Verificación automática de compliance normativo
- **Funcionalidades:**
  - Verificación de GDPR compliance
  - Verificación de AI Act compliance
  - Verificación de políticas internas
  - Análisis de riesgo regulatorio
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt
  - `promptMetadata` (JSON) - Metadatos del prompt
- **Variables de salida:**
  - `complianceScore` (Integer) - Score de compliance (0-100)
  - `gdprCompliant` (Boolean) - Cumplimiento GDPR
  - `aiActCompliant` (Boolean) - Cumplimiento AI Act
  - `complianceIssues` (JSON) - Issues de compliance encontrados
  - `complianceRecommendation` (String) - Recomendaciones de compliance

#### **Auto-Approve**
- **ID:** `autoApprove`
- **Nombre:** Auto-Approve
- **Delegate:** `com.codeflowx.govern.workflow.delegates.AutoApprovePromptDelegate`
- **Propósito:** Aprobación automática de prompts de bajo riesgo
- **Condiciones de activación:**
  - `safetyScore >= 80`
  - `complianceScore >= 85`
  - `autoApproveEligible = true`
- **Funcionalidades:**
  - Actualización del estado del prompt a APPROVED
  - Creación de registro de aprobación
  - Logging de la decisión automática
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt
  - `safetyScore` (Integer) - Score de seguridad
  - `complianceScore` (Integer) - Score de compliance
- **Variables de salida:**
  - `approvalDecision` (String) - Decisión tomada
  - `approvalTimestamp` (Timestamp) - Timestamp de aprobación
  - `approvalMethod` (String) - Método de aprobación (AUTO)

#### **Notify Approval**
- **ID:** `notifyApproval`
- **Nombre:** Notify Approval
- **Delegate:** `com.codeflowx.govern.workflow.delegates.NotifyApprovalDelegate`
- **Propósito:** Notificación de aprobación a stakeholders
- **Funcionalidades:**
  - Envío de email de notificación
  - Actualización de dashboard
  - Logging de notificación
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt
  - `approverId` (String) - ID del aprobador
  - `approvalMethod` (String) - Método de aprobación
- **Variables de salida:**
  - `notificationSent` (Boolean) - Notificación enviada
  - `notificationTimestamp` (Timestamp) - Timestamp de notificación

#### **Notify Rejection**
- **ID:** `notifyRejection`
- **Nombre:** Notify Rejection
- **Delegate:** `com.codeflowx.govern.workflow.delegates.NotifyApprovalDelegate`
- **Propósito:** Notificación de rechazo a stakeholders
- **Funcionalidades:**
  - Envío de email de notificación
  - Actualización de dashboard
  - Logging de notificación
- **Variables de entrada:**
  - `promptId` (Long) - ID del prompt
  - `rejectionReason` (String) - Razón del rechazo
  - `rejectionDetails` (JSON) - Detalles del rechazo
- **Variables de salida:**
  - `notificationSent` (Boolean) - Notificación enviada
  - `notificationTimestamp` (Timestamp) - Timestamp de notificación

---

## 🔀 GATEWAYS Y DECISIONES

### **1. Parallel Gateway - Start**
- **ID:** `parallelStart`
- **Nombre:** Parallel Start
- **Propósito:** División del flujo en validaciones paralelas
- **Flujos de salida:**
  - `flowToSafety` → Safety Check
  - `flowToCompliance` → Compliance Check

### **2. Parallel Gateway - Merge**
- **ID:** `parallelMerge`
- **Nombre:** Parallel Merge
- **Propósito:** Convergencia de validaciones paralelas
- **Flujos de entrada:**
  - `flowFromSafety` ← Safety Check
  - `flowFromCompliance` ← Compliance Check
- **Flujo de salida:**
  - `flow3` → Auto-Approve Gateway

### **3. Exclusive Gateway - Auto-Approve**
- **ID:** `autoApproveGateway`
- **Nombre:** Auto-Approve?
- **Propósito:** Decisión entre auto-aprobación y revisión humana
- **Condiciones:**
  - **Auto-Approve:** `${autoApproveEligible}`
  - **Need Review:** `${!autoApproveEligible}`
- **Flujos de salida:**
  - `flowAutoApprove` → Auto-Approve (si autoApproveEligible = true)
  - `flowNeedReview` → Human Review (si autoApproveEligible = false)

### **4. Exclusive Gateway - Approval Decision**
- **ID:** `approvalDecisionGateway`
- **Nombre:** Approved?
- **Propósito:** Decisión final de aprobación o rechazo
- **Condiciones:**
  - **Approved:** `${approved}`
  - **Rejected:** `${!approved}`
- **Flujos de salida:**
  - `flowApproved` → Notify Approval (si approved = true)
  - `flowRejected` → Provide Rejection Reason (si approved = false)

---

## 🧠 INTEGRACIÓN CON DROOLS

### **Reglas de Decisión:**

#### **Auto-Approve Eligibility Rule**
```drl
rule "Auto-Approve Prompt - High Safety and Compliance"
    salience 100
    when
        $fact : PromptApprovalFact(
            safetyScore >= 80,
            complianceScore >= 85,
            jailbreakDetected == false,
            injectionDetected == false,
            maliciousContentDetected == false,
            gdprCompliant == true,
            aiActCompliant == true
        )
    then
        $fact.setAutoApproveEligible(true);
        $fact.setConfidenceLevel(0.95);
        $fact.setJustification("High scores across all safety and compliance dimensions");
        logger.info("Prompt eligible for auto-approval");
end
```

#### **Rejection Rule**
```drl
rule "Reject Prompt - Critical Safety Issues"
    salience 200
    when
        $fact : PromptApprovalFact(
            jailbreakDetected == true || 
            injectionDetected == true || 
            maliciousContentDetected == true
        )
    then
        $fact.setAutoApproveEligible(false);
        $fact.setRecommendedAction("REJECT");
        $fact.setConfidenceLevel(1.0);
        $fact.setJustification("Critical safety issues detected");
        logger.warn("Prompt rejected due to critical safety issues");
end
```

#### **Human Review Rule**
```drl
rule "Human Review Required - Medium Risk"
    salience 50
    when
        $fact : PromptApprovalFact(
            safetyScore >= 60 && safetyScore < 80,
            complianceScore >= 70 && complianceScore < 85,
            autoApproveEligible == false
        )
    then
        $fact.setRecommendedAction("HUMAN_REVIEW");
        $fact.setConfidenceLevel(0.75);
        $fact.setJustification("Medium risk prompt requires human review");
        logger.info("Prompt requires human review");
end
```

---

## 📊 MÉTRICAS Y KPIs DEL PROCESO

### **Métricas de Tiempo:**
- **Tiempo promedio de aprobación:** 2-4 horas
- **Tiempo de auto-aprobación:** 15-30 minutos
- **Tiempo de revisión humana:** 4-8 horas
- **SLA de aprobación:** 24 horas máximo

### **Métricas de Calidad:**
- **Tasa de auto-aprobación:** 70-80%
- **Tasa de aprobación humana:** 85-95%
- **Tasa de rechazo:** 5-15%
- **Precisión de validaciones:** 95%+

### **Métricas de Volumen:**
- **Prompts procesados por día:** 50-200
- **Pico de procesamiento:** 100-300 prompts/hora
- **Capacidad máxima:** 500 prompts/día

---

## 🔧 CONFIGURACIÓN Y PARÁMETROS

### **Parámetros del Proceso:**
- **SLA de aprobación:** 24 horas
- **Timeout de validaciones:** 30 minutos
- **Retry attempts:** 3
- **Escalamiento:** Después de 4 horas

### **Umbrales de Decisión:**
- **Safety Score mínimo:** 60
- **Compliance Score mínimo:** 70
- **Auto-approve Safety Score:** 80
- **Auto-approve Compliance Score:** 85

### **Configuración de Notificaciones:**
- **Email templates:** Configurables por tipo
- **Canales de notificación:** Email, Slack, Teams
- **Frecuencia de recordatorios:** Cada 4 horas

---

## 🚨 MANEJO DE ERRORES

### **Errores Comunes:**
1. **Prompt no encontrado**
   - **Causa:** ID de prompt inválido
   - **Acción:** Terminar proceso con error
   - **Notificación:** Email al solicitante

2. **Validación fallida**
   - **Causa:** Error en servicio de validación
   - **Acción:** Reintentar hasta 3 veces
   - **Escalamiento:** Notificar a administradores

3. **Timeout de proceso**
   - **Causa:** Proceso excede SLA
   - **Acción:** Escalamiento automático
   - **Notificación:** Email a managers

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
- **Batch processing** para múltiples prompts
- **Async notifications** para mejor rendimiento

### **Mejoras Futuras:**
- **ML-based scoring** para decisiones más precisas
- **A/B testing** de prompts automático
- **Predictive analytics** para riesgo
- **Integration con más validadores** externos

---

## ✅ CONCLUSIÓN

El **proceso BPMN de aprobación de prompts** proporciona una **automatización completa** y **robusta** del gobierno de prompts de IA con:

- 🔄 **Workflow automatizado** con decisiones inteligentes
- 🛡️ **Validaciones paralelas** de seguridad y compliance
- 🧠 **Integración con Drools** para decisiones complejas
- 📊 **Métricas completas** para monitoreo y optimización
- 🚨 **Manejo robusto** de errores y excepciones
- 📈 **Escalabilidad** para alto volumen de prompts

**Este proceso está optimizado** para manejar el gobierno de prompts de IA de manera eficiente, segura y compliant con regulaciones.

