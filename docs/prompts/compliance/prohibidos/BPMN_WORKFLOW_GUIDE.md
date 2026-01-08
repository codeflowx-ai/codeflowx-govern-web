# 🔄 GUÍA COMPLETA DEL WORKFLOW BPMN - PROHIBITED SYSTEMS DETECTION

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Agente BPMN, Desarrolladores Backend, Compliance Officers

---

## 📋 INFORMACIÓN GENERAL DEL WORKFLOW

### Identificación

- **Process ID:** `prohibited-system-detection-workflow`
- **Process Name:** "Prohibited System Detection Workflow (Art. 5)"
- **Versión:** 1.0
- **Target Namespace:** `http://codeflowx.com/govern/compliance`
- **Ubicación del Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/resources/processes/compliance/prohibited-system-detection-workflow.bpmn20.xml`
- **Is Executable:** `true`

### Base Legal

- **EU AI Act Art. 5:** Prohibiciones de sistemas de IA
- **Anexo II:** Lista de sistemas prohibidos

### Propósito

Este workflow gestiona el proceso completo de detección, revisión y resolución de sistemas prohibidos detectados en proyectos de IA. Proporciona un flujo estructurado para:

1. Revisar detecciones automáticas
2. Confirmar o descartar falsos positivos
3. Tomar decisiones de compliance
4. Gestionar modificaciones de sistemas
5. Notificar stakeholders

---

## 🎯 ELEMENTOS DEL WORKFLOW

### Start Event

**ID:** `prohibitedSystemDetectedStart`
**Name:** "Prohibited System Detected"
**Type:** Start Event

**Descripción:**
- Punto de entrada del workflow
- Se dispara automáticamente cuando se detecta un sistema prohibido en un proyecto
- No requiere interacción del usuario

**Variables de Inicio (Input Variables):**
```java
Map<String, Object> variables = new HashMap<>();
variables.put("projectId", Long);           // ID del proyecto
variables.put("detectedSystems", List<String>); // Nombres de sistemas detectados
variables.put("detectionDate", String);     // Fecha de detección (ISO 8601)
variables.put("confidence", Double);        // Nivel de confianza (0.0 - 1.0)
variables.put("matchedKeywords", List<String>); // Keywords coincidentes
variables.put("evidence", String);          // Evidencia de la detección
```

**Outgoing Flow:** `flowToReviewDetection` → `reviewDetection`

---

### User Tasks

#### 1. Review Detection

**ID:** `reviewDetection`
**Name:** "Review Prohibited System Detection"
**Type:** User Task

**Descripción:**
- Primera tarea de usuario del workflow
- Permite a compliance officers, deployment managers y legal revisar la detección inicial
- El usuario debe decidir si la detección es un falso positivo o está confirmada

**Candidate Groups:**
- `compliance-officers`
- `deployment-manager`
- `legal`

**Form Key:** `prohibited-system-review-form`

**Formulario Esperado (ZUL o Next.js):**

**Campos:**
1. **Project Information (Read-Only)**
   - Project ID: `{projectId}`
   - Project Name: `{projectName}`
   - Project Description: `{projectDescription}`

2. **Detection Information (Read-Only)**
   - Detected Systems: `{detectedSystems}` (lista)
   - Confidence Level: `{confidence}` (porcentaje)
   - Matched Keywords: `{matchedKeywords}` (lista de badges)
   - Evidence: `{evidence}` (texto)
   - Detection Date: `{detectionDate}`

3. **Review Decision (Required)**
   - Radio buttons:
     - `FALSE_POSITIVE` - La detección es incorrecta
     - `CONFIRMED` - La detección es correcta

4. **Review Comments (Optional)**
   - Textarea para comentarios del revisor

**Variables de Salida (Output Variables):**
```java
variables.put("reviewDecision", String); // "FALSE_POSITIVE" o "CONFIRMED"
variables.put("reviewComments", String); // Comentarios del revisor
variables.put("reviewedBy", String);     // Usuario que revisó
variables.put("reviewedAt", String);      // Fecha de revisión (ISO 8601)
```

**Incoming Flow:** `flowToReviewDetection` ← `prohibitedSystemDetectedStart`
**Outgoing Flow:** `flowToReviewDecision` → `reviewDecisionGateway`

---

#### 2. Compliance Officer Review

**ID:** `complianceOfficerReview`
**Name:** "Compliance Officer Review"
**Type:** User Task

**Descripción:**
- Tarea de revisión por parte de compliance officers y legal
- Se ejecuta solo si la detección inicial fue confirmada
- El compliance officer debe decidir si bloquear permanentemente o requerir modificaciones

**Candidate Groups:**
- `compliance-officers`
- `legal`

**Form Key:** `compliance-officer-review-form`

**Formulario Esperado (ZUL o Next.js):**

**Campos:**
1. **Project Information (Read-Only)**
   - Project ID: `{projectId}`
   - Project Name: `{projectName}`

2. **Detection Information (Read-Only)**
   - Detected Systems: `{detectedSystems}` (lista)
   - Evidence: `{evidence}` (texto)
   - Initial Review Comments: `{reviewComments}` (read-only)

3. **Compliance Decision (Required)**
   - Radio buttons:
     - `BLOCK` - Bloquear despliegue permanentemente
     - `MODIFY` - Requerir modificaciones del sistema

4. **Compliance Comments (Required)**
   - Textarea para justificación de la decisión

5. **Block Reason (Required if BLOCK)**
   - Textarea para razón del bloqueo (máx. 500 caracteres)

**Variables de Salida (Output Variables):**
```java
variables.put("complianceDecision", String); // "BLOCK" o "MODIFY"
variables.put("complianceComments", String); // Comentarios del compliance officer
variables.put("blockReason", String);        // Razón del bloqueo (si BLOCK)
variables.put("reviewedBy", String);         // Usuario que revisó
variables.put("reviewedAt", String);         // Fecha de revisión (ISO 8601)
```

**Incoming Flow:** `flowConfirmedProhibited` ← `reviewDecisionGateway`
**Outgoing Flow:** `flowToComplianceDecision` → `complianceDecisionGateway`

---

#### 3. Define System Modifications

**ID:** `defineModifications`
**Name:** "Define System Modifications"
**Type:** User Task

**Descripción:**
- Tarea para que el equipo técnico defina las modificaciones necesarias
- Se ejecuta solo si la decisión de compliance fue "MODIFY"
- Permite un loop si las modificaciones no son verificadas correctamente

**Candidate Groups:**
- `deployer`
- `tech-team`

**Form Key:** `define-modifications-form`

**Formulario Esperado (ZUL o Next.js):**

**Campos:**
1. **Project Information (Read-Only)**
   - Project ID: `{projectId}`
   - Project Name: `{projectName}`

2. **Detection Information (Read-Only)**
   - Detected Systems: `{detectedSystems}` (lista)
   - Compliance Comments: `{complianceComments}` (read-only)

3. **Modifications Description (Required)**
   - Textarea para describir las modificaciones a realizar

4. **Modifications Plan (Required)**
   - Textarea para plan de implementación

5. **Estimated Completion Date (Required)**
   - Date picker para fecha estimada de completado

**Variables de Salida (Output Variables):**
```java
variables.put("modificationsDescription", String); // Descripción de modificaciones
variables.put("modificationsPlan", String);         // Plan de implementación
variables.put("estimatedCompletionDate", String);   // Fecha estimada (ISO 8601)
variables.put("modifiedBy", String);               // Usuario que definió modificaciones
variables.put("modifiedAt", String);                // Fecha de definición (ISO 8601)
```

**Incoming Flows:**
- `flowRequireModification` ← `complianceDecisionGateway`
- `flowModificationsRejected` ← `modificationGateway` (loop)

**Outgoing Flow:** `flowToVerifyModifications` → `verifyModifications`

---

### Service Tasks

#### 1. Unblock Deployment

**ID:** `unblockDeployment`
**Name:** "Unblock Deployment"
**Type:** Service Task

**Descripción:**
- Desbloquea el despliegue de un proyecto
- Se ejecuta cuando la detección es marcada como falso positivo
- Actualiza los campos `PRJDEPLOYMENTBLOCKED = false` y `PRJBLOCKREASON = null` en la entidad Project

**Delegate Class:** `com.codeflowx.govern.workflow.delegates.compliance.UnblockDeploymentDelegate`

**Implementación Esperada:**

```java
package com.codeflowx.govern.workflow.delegates.compliance;

import com.codeflowx.govern.business.compliance.ProhibitedSystemBusinessService;
import com.codeflowx.govern.entity.projects.Project;
import com.codeflowx.govern.repository.projects.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnblockDeploymentDelegate implements JavaDelegate {

    private final ProjectRepository projectRepository;
    private final ProhibitedSystemBusinessService prohibitedSystemBusinessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long projectId = (Long) execution.getVariable("projectId");

        log.info("Unblocking deployment for project: {}", projectId);

        // Obtener proyecto
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        // Desbloquear despliegue
        project.setPrjdeploymentblocked(false);
        project.setPrjblockreason(null);
        project.setUpdatedat(new Timestamp(System.currentTimeMillis()));

        projectRepository.save(project);

        log.info("Deployment unblocked for project: {}", projectId);

        // Establecer variable de resultado
        execution.setVariable("unblocked", true);
        execution.setVariable("unblockedAt", LocalDateTime.now().toString());
    }
}
```

**Variables de Entrada:**
- `projectId` (Long)

**Variables de Salida:**
- `unblocked` (Boolean) - true si se desbloqueó exitosamente
- `unblockedAt` (String) - Fecha de desbloqueo (ISO 8601)

**Incoming Flow:** `flowFalsePositive` ← `reviewDecisionGateway`
**Outgoing Flow:** `flowToEndResolved` → `endResolved`

---

#### 2. Block Deployment Permanently

**ID:** `blockDeploymentPermanently`
**Name:** "Block Deployment Permanently"
**Type:** Service Task

**Descripción:**
- Bloquea permanentemente el despliegue de un proyecto
- Se ejecuta cuando la decisión de compliance es "BLOCK"
- Actualiza los campos `PRJDEPLOYMENTBLOCKED = true` y `PRJBLOCKREASON = {blockReason}` en la entidad Project

**Delegate Class:** `com.codeflowx.govern.workflow.delegates.compliance.BlockDeploymentPermanentlyDelegate`

**Implementación Esperada:**

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class BlockDeploymentPermanentlyDelegate implements JavaDelegate {

    private final ProjectRepository projectRepository;
    private final ProhibitedSystemBusinessService prohibitedSystemBusinessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long projectId = (Long) execution.getVariable("projectId");
        String blockReason = (String) execution.getVariable("blockReason");

        log.info("Blocking deployment permanently for project: {}", projectId);

        // Llamar al business service para bloquear
        prohibitedSystemBusinessService.blockDeployment(projectId, blockReason);

        log.info("Deployment blocked permanently for project: {}", projectId);

        // Establecer variable de resultado
        execution.setVariable("blocked", true);
        execution.setVariable("blockedAt", LocalDateTime.now().toString());
    }
}
```

**Variables de Entrada:**
- `projectId` (Long)
- `blockReason` (String)

**Variables de Salida:**
- `blocked` (Boolean) - true si se bloqueó exitosamente
- `blockedAt` (String) - Fecha de bloqueo (ISO 8601)

**Incoming Flow:** `flowBlockPermanently` ← `complianceDecisionGateway`
**Outgoing Flow:** `flowToNotifyStakeholders` → `notifyStakeholders`

---

#### 3. Notify Stakeholders

**ID:** `notifyStakeholders`
**Name:** "Notify Stakeholders"
**Type:** Service Task

**Descripción:**
- Notifica a los stakeholders relevantes sobre el bloqueo de despliegue
- Envía notificaciones por email o sistema de mensajería
- Notifica a: project owner, compliance officers, deployment manager

**Delegate Class:** `com.codeflowx.govern.workflow.delegates.compliance.NotifyStakeholdersDelegate`

**Implementación Esperada:**

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyStakeholdersDelegate implements JavaDelegate {

    private final NotificationService notificationService;
    private final ProjectRepository projectRepository;

    @Override
    public void execute(DelegateExecution execution) {
        Long projectId = (Long) execution.getVariable("projectId");
        String blockReason = (String) execution.getVariable("blockReason");

        log.info("Notifying stakeholders for project: {}", projectId);

        // Obtener proyecto
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        // Construir mensaje
        String message = String.format(
            "El despliegue del proyecto '%s' ha sido bloqueado debido a la detección de un sistema prohibido.\n\n" +
            "Razón: %s\n\n" +
            "Por favor, revise el proyecto y tome las acciones necesarias.",
            project.getName(),
            blockReason
        );

        // Notificar stakeholders
        notificationService.notifyProjectOwner(projectId, "Despliegue Bloqueado", message);
        notificationService.notifyComplianceOfficers(projectId, "Despliegue Bloqueado", message);
        notificationService.notifyDeploymentManager(projectId, "Despliegue Bloqueado", message);

        log.info("Stakeholders notified for project: {}", projectId);

        // Establecer variable de resultado
        execution.setVariable("notified", true);
        execution.setVariable("notifiedAt", LocalDateTime.now().toString());
    }
}
```

**Variables de Entrada:**
- `projectId` (Long)
- `blockReason` (String)

**Variables de Salida:**
- `notified` (Boolean) - true si se notificó exitosamente
- `notifiedAt` (String) - Fecha de notificación (ISO 8601)

**Incoming Flow:** `flowToNotifyStakeholders` ← `blockDeploymentPermanently`
**Outgoing Flow:** `flowToEndBlocked` → `endBlocked`

---

#### 4. Verify Modifications

**ID:** `verifyModifications`
**Name:** "Verify Modifications"
**Type:** Service Task

**Descripción:**
- Verifica si las modificaciones definidas por el equipo técnico son suficientes
- Puede incluir verificación automática o revisión manual
- Establece `modificationsVerified` como true o false

**Delegate Class:** `com.codeflowx.govern.workflow.delegates.compliance.VerifyModificationsDelegate`

**Implementación Esperada:**

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class VerifyModificationsDelegate implements JavaDelegate {

    private final ProhibitedSystemBusinessService prohibitedSystemBusinessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long projectId = (Long) execution.getVariable("projectId");
        String modificationsDescription = (String) execution.getVariable("modificationsDescription");
        String modificationsPlan = (String) execution.getVariable("modificationsPlan");

        log.info("Verifying modifications for project: {}", projectId);

        // Verificar modificaciones (lógica de negocio)
        // Por ahora, asumimos que las modificaciones son verificadas automáticamente
        // En producción, esto podría incluir:
        // - Re-verificación de keywords
        // - Revisión manual por compliance officer
        // - Análisis automatizado del código

        boolean verified = verifyModificationsLogic(projectId, modificationsDescription, modificationsPlan);

        execution.setVariable("modificationsVerified", verified);
        execution.setVariable("verificationDate", LocalDateTime.now().toString());

        log.info("Modifications verification result for project {}: {}", projectId, verified);
    }

    private boolean verifyModificationsLogic(Long projectId, String description, String plan) {
        // TODO: Implementar lógica de verificación
        // Por ahora, retornamos true si hay descripción y plan
        return description != null && !description.trim().isEmpty() &&
               plan != null && !plan.trim().isEmpty();
    }
}
```

**Variables de Entrada:**
- `projectId` (Long)
- `modificationsDescription` (String)
- `modificationsPlan` (String)

**Variables de Salida:**
- `modificationsVerified` (Boolean) - true si las modificaciones son suficientes
- `verificationDate` (String) - Fecha de verificación (ISO 8601)

**Incoming Flow:** `flowToVerifyModifications` ← `defineModifications`
**Outgoing Flow:** `flowToModificationGateway` → `modificationGateway`

---

#### 5. Unblock and Re-check

**ID:** `unblockAndRecheck`
**Name:** "Unblock and Re-check Prohibited Systems"
**Type:** Service Task

**Descripción:**
- Desbloquea el despliegue y re-verifica el proyecto
- Se ejecuta cuando las modificaciones son verificadas correctamente
- Realiza una nueva verificación de sistemas prohibidos

**Delegate Class:** `com.codeflowx.govern.workflow.delegates.compliance.UnblockAndRecheckDelegate`

**Implementación Esperada:**

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class UnblockAndRecheckDelegate implements JavaDelegate {

    private final ProjectRepository projectRepository;
    private final ProhibitedSystemBusinessService prohibitedSystemBusinessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long projectId = (Long) execution.getVariable("projectId");

        log.info("Unblocking and re-checking project: {}", projectId);

        // 1. Desbloquear despliegue
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        project.setPrjdeploymentblocked(false);
        project.setPrjblockreason(null);
        project.setUpdatedat(new Timestamp(System.currentTimeMillis()));
        projectRepository.save(project);

        // 2. Re-verificar sistemas prohibidos
        ProhibitedSystemBusinessService.ProhibitedSystemCheckResult result =
            prohibitedSystemBusinessService.checkProhibitedSystem(projectId);

        boolean stillProhibited = result.getHasProhibitedSystem();

        execution.setVariable("recheckResult", stillProhibited);
        execution.setVariable("recheckDate", LocalDateTime.now().toString());

        if (stillProhibited) {
            log.warn("Project {} still has prohibited systems after modifications", projectId);
        } else {
            log.info("Project {} is now clean after modifications", projectId);
        }
    }
}
```

**Variables de Entrada:**
- `projectId` (Long)

**Variables de Salida:**
- `recheckResult` (Boolean) - true si aún hay sistemas prohibidos
- `recheckDate` (String) - Fecha de re-verificación (ISO 8601)

**Incoming Flow:** `flowModificationsOK` ← `modificationGateway`
**Outgoing Flow:** `flowToEndResolved` → `endResolved`

---

### Gateways

#### 1. Review Decision Gateway

**ID:** `reviewDecisionGateway`
**Name:** "Review Decision?"
**Type:** Exclusive Gateway

**Descripción:**
- Evalúa la decisión de la revisión inicial
- Decide entre falso positivo o confirmado

**Condition Expressions:**

**Flow: `flowFalsePositive`**
```xml
<conditionExpression xsi:type="tFormalExpression">
  <![CDATA[${reviewDecision == 'FALSE_POSITIVE'}]]>
</conditionExpression>
```
- **Target:** `unblockDeployment`
- **Condición:** `reviewDecision == 'FALSE_POSITIVE'`

**Flow: `flowConfirmedProhibited`**
```xml
<conditionExpression xsi:type="tFormalExpression">
  <![CDATA[${reviewDecision == 'CONFIRMED'}]]>
</conditionExpression>
```
- **Target:** `complianceOfficerReview`
- **Condición:** `reviewDecision == 'CONFIRMED'`

**Incoming Flow:** `flowToReviewDecision` ← `reviewDetection`
**Outgoing Flows:**
- `flowFalsePositive` → `unblockDeployment`
- `flowConfirmedProhibited` → `complianceOfficerReview`

---

#### 2. Compliance Decision Gateway

**ID:** `complianceDecisionGateway`
**Name:** "Compliance Decision?"
**Type:** Exclusive Gateway

**Descripción:**
- Evalúa la decisión del compliance officer
- Decide entre bloquear permanentemente o requerir modificaciones

**Condition Expressions:**

**Flow: `flowBlockPermanently`**
```xml
<conditionExpression xsi:type="tFormalExpression">
  <![CDATA[${complianceDecision == 'BLOCK'}]]>
</conditionExpression>
```
- **Target:** `blockDeploymentPermanently`
- **Condición:** `complianceDecision == 'BLOCK'`

**Flow: `flowRequireModification`**
```xml
<conditionExpression xsi:type="tFormalExpression">
  <![CDATA[${complianceDecision == 'MODIFY'}]]>
</conditionExpression>
```
- **Target:** `defineModifications`
- **Condición:** `complianceDecision == 'MODIFY'`

**Incoming Flow:** `flowToComplianceDecision` ← `complianceOfficerReview`
**Outgoing Flows:**
- `flowBlockPermanently` → `blockDeploymentPermanently`
- `flowRequireModification` → `defineModifications`

---

#### 3. Modification Gateway

**ID:** `modificationGateway`
**Name:** "Modifications Verified?"
**Type:** Exclusive Gateway

**Descripción:**
- Evalúa si las modificaciones fueron verificadas correctamente
- Permite loop si las modificaciones no son suficientes

**Condition Expressions:**

**Flow: `flowModificationsOK`**
```xml
<conditionExpression xsi:type="tFormalExpression">
  <![CDATA[${modificationsVerified == true}]]>
</conditionExpression>
```
- **Target:** `unblockAndRecheck`
- **Condición:** `modificationsVerified == true`

**Flow: `flowModificationsRejected`**
```xml
<conditionExpression xsi:type="tFormalExpression">
  <![CDATA[${modificationsVerified == false}]]>
</conditionExpression>
```
- **Target:** `defineModifications` (loop)
- **Condición:** `modificationsVerified == false`

**Incoming Flow:** `flowToModificationGateway` ← `verifyModifications`
**Outgoing Flows:**
- `flowModificationsOK` → `unblockAndRecheck`
- `flowModificationsRejected` → `defineModifications` (loop)

---

### End Events

#### 1. End Resolved

**ID:** `endResolved`
**Name:** "Resolved (False Positive)"
**Type:** End Event

**Descripción:**
- Finaliza el workflow cuando la detección es resuelta
- Puede ser por falso positivo o por modificaciones verificadas

**Incoming Flows:**
- `flowToEndResolved` ← `unblockDeployment` (falso positivo)
- `flowToEndResolved` ← `unblockAndRecheck` (modificaciones verificadas)

**Estado Final:**
- Proyecto desbloqueado
- Sistema prohibido resuelto

---

#### 2. End Blocked

**ID:** `endBlocked`
**Name:** "Deployment Blocked"
**Type:** End Event

**Descripción:**
- Finaliza el workflow cuando el despliegue es bloqueado permanentemente
- Stakeholders han sido notificados

**Incoming Flow:** `flowToEndBlocked` ← `notifyStakeholders`

**Estado Final:**
- Proyecto bloqueado permanentemente
- Stakeholders notificados
- Razón del bloqueo registrada

---

## 🔄 FLUJOS COMPLETOS DEL WORKFLOW

### Flujo 1: Falso Positivo

```
1. Start Event (prohibitedSystemDetectedStart)
   ↓
2. User Task: Review Detection (reviewDetection)
   ↓
3. Gateway: Review Decision (reviewDecisionGateway)
   ├─ FALSE_POSITIVE →
   ↓
4. Service Task: Unblock Deployment (unblockDeployment)
   ↓
5. End Event: Resolved (endResolved)
```

**Resultado:** Proyecto desbloqueado, detección marcada como falso positivo.

---

### Flujo 2: Bloqueo Permanente

```
1. Start Event (prohibitedSystemDetectedStart)
   ↓
2. User Task: Review Detection (reviewDetection)
   ↓
3. Gateway: Review Decision (reviewDecisionGateway)
   ├─ CONFIRMED →
   ↓
4. User Task: Compliance Officer Review (complianceOfficerReview)
   ↓
5. Gateway: Compliance Decision (complianceDecisionGateway)
   ├─ BLOCK →
   ↓
6. Service Task: Block Deployment Permanently (blockDeploymentPermanently)
   ↓
7. Service Task: Notify Stakeholders (notifyStakeholders)
   ↓
8. End Event: Deployment Blocked (endBlocked)
```

**Resultado:** Proyecto bloqueado permanentemente, stakeholders notificados.

---

### Flujo 3: Modificaciones Requeridas

```
1. Start Event (prohibitedSystemDetectedStart)
   ↓
2. User Task: Review Detection (reviewDetection)
   ↓
3. Gateway: Review Decision (reviewDecisionGateway)
   ├─ CONFIRMED →
   ↓
4. User Task: Compliance Officer Review (complianceOfficerReview)
   ↓
5. Gateway: Compliance Decision (complianceDecisionGateway)
   ├─ MODIFY →
   ↓
6. User Task: Define System Modifications (defineModifications)
   ↓
7. Service Task: Verify Modifications (verifyModifications)
   ↓
8. Gateway: Modifications Verified? (modificationGateway)
   ├─ false → Loop back to 6 (defineModifications)
   ├─ true →
   ↓
9. Service Task: Unblock and Re-check (unblockAndRecheck)
   ↓
10. End Event: Resolved (endResolved)
```

**Resultado:** Proyecto modificado, re-verificado y desbloqueado si está limpio.

---

## 📝 FORMULARIOS ASOCIADOS

### 1. prohibited-system-review-form

**Tarea:** `reviewDetection`
**Tipo:** User Task Form

**Campos:**
- Project Information (read-only)
- Detection Information (read-only)
- Review Decision (required): FALSE_POSITIVE | CONFIRMED
- Review Comments (optional)

**Pantalla Asociada (Next.js):**
- Ruta sugerida: `/governance/compliance/prohibited-systems/review/{processInstanceId}`
- Componente: Dialog o página dedicada

---

### 2. compliance-officer-review-form

**Tarea:** `complianceOfficerReview`
**Tipo:** User Task Form

**Campos:**
- Project Information (read-only)
- Detection Information (read-only)
- Compliance Decision (required): BLOCK | MODIFY
- Compliance Comments (required)
- Block Reason (required if BLOCK)

**Pantalla Asociada (Next.js):**
- Ruta sugerida: `/governance/compliance/prohibited-systems/compliance-review/{processInstanceId}`
- Componente: Dialog o página dedicada

---

### 3. define-modifications-form

**Tarea:** `defineModifications`
**Tipo:** User Task Form

**Campos:**
- Project Information (read-only)
- Detection Information (read-only)
- Modifications Description (required)
- Modifications Plan (required)
- Estimated Completion Date (required)

**Pantalla Asociada (Next.js):**
- Ruta sugerida: `/governance/compliance/prohibited-systems/modifications/{processInstanceId}`
- Componente: Dialog o página dedicada

---

## 🔧 INTEGRACIÓN CON BACKEND

### Iniciar Workflow

**Código Java:**

```java
@Autowired
private BpmnWorkflowClient bpmnWorkflowClient;

public ProhibitedSystemCheckResult checkProhibitedSystem(Long projectId) {
    // ... lógica de detección ...

    if (!detected.isEmpty() && bpmnWorkflowClient != null) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("projectId", projectId);
        variables.put("detectedSystems", detected.stream()
            .map(ProhibitedSystem::getPrsname)
            .collect(Collectors.toList()));
        variables.put("detectionDate", LocalDateTime.now().toString());
        variables.put("confidence", result.getConfidence());
        variables.put("matchedKeywords", result.getMatchedKeywords());
        variables.put("evidence", "Project description and model names contain keywords matching prohibited system patterns.");

        String workflowInstanceId = bpmnWorkflowClient.startProcess(
            "prohibited-system-detection-workflow",
            variables
        );

        log.info("BPMN workflow started: {}", workflowInstanceId);
    }

    return result;
}
```

### Obtener Tareas de Usuario

**Código Java:**

```java
List<Task> tasks = taskService.createTaskQuery()
    .processDefinitionKey("prohibited-system-detection-workflow")
    .taskCandidateGroup("compliance-officers")
    .list();
```

### Completar Tarea de Usuario

**Código Java:**

```java
Map<String, Object> variables = new HashMap<>();
variables.put("reviewDecision", "CONFIRMED");
variables.put("reviewComments", "Sistema confirmado como prohibido según Art. 5.1.c");
variables.put("reviewedBy", currentUser);
variables.put("reviewedAt", LocalDateTime.now().toString());

taskService.complete(taskId, variables);
```

---

## 📊 VARIABLES DEL WORKFLOW

### Variables de Inicio (Input)

| Variable | Tipo | Descripción | Requerido |
|----------|------|-------------|-----------|
| `projectId` | Long | ID del proyecto | ✅ Sí |
| `detectedSystems` | List<String> | Nombres de sistemas detectados | ✅ Sí |
| `detectionDate` | String | Fecha de detección (ISO 8601) | ✅ Sí |
| `confidence` | Double | Nivel de confianza (0.0 - 1.0) | ✅ Sí |
| `matchedKeywords` | List<String> | Keywords coincidentes | ✅ Sí |
| `evidence` | String | Evidencia de la detección | ✅ Sí |

### Variables Intermedias

| Variable | Tipo | Descripción | Establecida Por |
|----------|------|-------------|-----------------|
| `reviewDecision` | String | Decisión de revisión (FALSE_POSITIVE / CONFIRMED) | reviewDetection |
| `reviewComments` | String | Comentarios del revisor | reviewDetection |
| `reviewedBy` | String | Usuario que revisó | reviewDetection |
| `reviewedAt` | String | Fecha de revisión (ISO 8601) | reviewDetection |
| `complianceDecision` | String | Decisión de compliance (BLOCK / MODIFY) | complianceOfficerReview |
| `complianceComments` | String | Comentarios del compliance officer | complianceOfficerReview |
| `blockReason` | String | Razón del bloqueo | complianceOfficerReview |
| `modificationsDescription` | String | Descripción de modificaciones | defineModifications |
| `modificationsPlan` | String | Plan de implementación | defineModifications |
| `estimatedCompletionDate` | String | Fecha estimada de completado (ISO 8601) | defineModifications |
| `modificationsVerified` | Boolean | Si las modificaciones son verificadas | verifyModifications |

### Variables de Salida (Output)

| Variable | Tipo | Descripción | Establecida Por |
|----------|------|-------------|-----------------|
| `unblocked` | Boolean | Si se desbloqueó exitosamente | unblockDeployment |
| `unblockedAt` | String | Fecha de desbloqueo (ISO 8601) | unblockDeployment |
| `blocked` | Boolean | Si se bloqueó exitosamente | blockDeploymentPermanently |
| `blockedAt` | String | Fecha de bloqueo (ISO 8601) | blockDeploymentPermanently |
| `notified` | Boolean | Si se notificó exitosamente | notifyStakeholders |
| `notifiedAt` | String | Fecha de notificación (ISO 8601) | notifyStakeholders |
| `recheckResult` | Boolean | Resultado de re-verificación | unblockAndRecheck |
| `recheckDate` | String | Fecha de re-verificación (ISO 8601) | unblockAndRecheck |

---

## 🚀 IMPLEMENTACIÓN DE DELEGATES

### Ubicación Esperada

```
nocode.service/codeflowx.govern.workflow.delegates/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/
```

### Estructura de Clase

```java
package com.codeflowx.govern.workflow.delegates.compliance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class [DelegateName]Delegate implements JavaDelegate {

    // Inyectar dependencias necesarias
    // private final SomeService someService;

    @Override
    public void execute(DelegateExecution execution) {
        // 1. Obtener variables
        // 2. Ejecutar lógica de negocio
        // 3. Establecer variables de salida
        // 4. Logging
    }
}
```

---

## 📚 REFERENCIAS

- **Base Legal:** EU AI Act Art. 5, Anexo II
- **Archivo BPMN:** `prohibited-system-detection-workflow.bpmn20.xml`
- **Documentación Backend:** Ver `DEVELOPER_GUIDE_BACKEND.md`
- **Guía de Usuario:** Ver `user_guide/GUIA_FUNCIONAL_PROHIBITED_SYSTEMS.md`

---

**Última Actualización:** Diciembre 2025
