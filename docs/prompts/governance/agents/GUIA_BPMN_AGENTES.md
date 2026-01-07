# 📋 GUÍA BPMN PARA AGENTES DE IA

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Agentes de IA, Desarrolladores, Governance Managers
**Proyecto:** CodeFlowX AI Governance Platform

---

## 🎯 PROPÓSITO

Esta guía define los procesos BPMN que el sistema debe **verificar** y **crear automáticamente** cuando sea necesario para el gobierno de agentes de IA. El agente BPMN debe asegurar que todos los procesos requeridos estén disponibles y configurados correctamente.

---

## 🔍 PROCESOS BPMN REQUERIDOS PARA AGENTES

### 1. **Aprobación de Agentes** (`agent-approval-v1`)

**Ubicación:** `data/bpmn-processes/aios/agent-approval-v1.bpmn`

**Cuándo se usa:**
- Al crear una solicitud de aprobación de agente (`POST /api/v1/agents/approval`)
- Cuando el estado de aprobación es `PENDING`

**Variables del proceso:**
```json
{
  "approvalId": "Long",
  "agentUuid": "String",
  "approvalType": "String (DEPLOYMENT, VERSION_UPDATE, CONFIGURATION_CHANGE, PRODUCTION_ACCESS)",
  "agentName": "String",
  "requestReason": "String",
  "requestDetails": "String",
  "approverEmail": "String"
}
```

**Tareas del proceso:**
1. **AI Risk Assessment** (ServiceTask) - Evalúa riesgos usando LLM
2. **AI Compliance Check** (ServiceTask) - Verifica cumplimiento
3. **AI Ethical Review** (ServiceTask) - Evalúa ética
4. **Calculate Score (Drools)** (BusinessRuleTask) - Ejecuta reglas Drools
5. **Decisión Automática:**
   - AUTO_APPROVE: Si score cumple criterios
   - HITL_REQUIRED: Si requiere revisión humana
   - AUTO_REJECT: Si no cumple criterios mínimos
6. **Human Review** (UserTask) - Si HITL_REQUIRED, tarea humana con SLA 24h
7. **Auto Aprobar/Rechazar** (ServiceTask) - Actualiza estado del agente

**Verificación:**
- ✅ Verificar que el proceso existe en `data/bpmn-processes/aios/agent-approval-v1.bpmn`
- ✅ Verificar que las reglas Drools `agent-scoring` están configuradas
- ✅ Verificar que los delegates Java existen:
  - `AIRiskAssessmentDelegate`
  - `AIComplianceCheckDelegate`
  - `AIEthicalReviewDelegate`
  - `AutoApproveAgentDelegate`
  - `AutoRejectAgentDelegate`

**Creación automática si no existe:**
- Si el proceso no existe, crearlo basándose en el template `agent-approval-v1.bpmn`
- Configurar grupos de candidatos: `ml-engineers`, `governance-admins`
- Configurar SLA de 24 horas para tarea HITL

---

### 2. **Certificación de Agentes** (`agent-certification-v1`)

**Ubicación:** `data/bpmn-processes/aios/agent-certification-v1.bpmn` (CREAR SI NO EXISTE)

**Cuándo se usa:**
- Al solicitar certificación de agente (`POST /api/v1/agents/registry/{id}/certification`)
- Cuando el estado de certificación cambia a `PENDING`

**Variables del proceso:**
```json
{
  "certificationId": "Long",
  "agentId": "Long",
  "agentUuid": "String",
  "agentName": "String",
  "certifyingBody": "String",
  "expiryDate": "String (ISO 8601)",
  "certifiedBy": "String"
}
```

**Tareas del proceso:**
1. **Verificar Requisitos** (ServiceTask):
   - Clasificación de riesgo completada
   - Evaluación FRIA completada
   - Registro EU completado
   - Declaración de conformidad completada
   - Documentación técnica disponible
   - QMS validado
2. **Validación Automática** (BusinessRuleTask):
   - Ejecutar reglas Drools para validar requisitos
3. **Revisión de Certificación** (UserTask):
   - Asignado a: `compliance-officers`, `governance-managers`
   - SLA: 48 horas
4. **Aprobación/Rechazo:**
   - Si aprobado: Actualizar estado a `CERTIFIED`, generar `certificateId`
   - Si rechazado: Actualizar estado a `NOT_CERTIFIED`, registrar razón

**Verificación:**
- ✅ Verificar que el proceso existe
- ✅ Si no existe, **CREAR** el proceso con la estructura descrita

**Template del proceso:**
```xml
<process id="agent-certification-v1" name="Agent Certification V1">
  <startEvent id="start"/>
  <serviceTask id="verifyRequirements" name="Verify Certification Requirements"/>
  <businessRuleTask id="validateRequirements" name="Validate Requirements (Drools)"/>
  <exclusiveGateway id="requirementsGateway"/>
  <userTask id="certificationReview" name="Certification Review"
            activiti:candidateGroups="compliance-officers,governance-managers"
            activiti:dueDate="P2D"/>
  <exclusiveGateway id="decisionGateway"/>
  <serviceTask id="approveCertification" name="Approve Certification"/>
  <serviceTask id="rejectCertification" name="Reject Certification"/>
  <endEvent id="endApproved"/>
  <endEvent id="endRejected"/>
</process>
```

---

### 3. **Retiro de Agentes** (`agent-retirement-v1`)

**Ubicación:** `data/bpmn-processes/aios/agent-retirement-v1.bpmn` (CREAR SI NO EXISTE)

**Cuándo se usa:**
- Al crear solicitud de retiro (`POST /api/v1/agents/registry/{id}/retirement`)
- Cuando el estado de retiro cambia a `PENDING_APPROVAL`

**Variables del proceso:**
```json
{
  "retirementId": "Long",
  "agentId": "Long",
  "agentUuid": "String",
  "agentName": "String",
  "retirementType": "String (PLANNED, IMMEDIATE, GRACEFUL, REPLACEMENT)",
  "retirementDate": "String (ISO 8601)",
  "retirementReason": "String",
  "migrationPlan": "String",
  "dependencies": {
    "projects": "Integer",
    "deployments": "Integer",
    "integrations": "Integer"
  }
}
```

**Tareas del proceso:**
1. **Validar Dependencias** (ServiceTask):
   - Verificar proyectos asociados
   - Verificar despliegues activos
   - Verificar integraciones
2. **Validar Plan de Migración** (ServiceTask):
   - Si hay dependencias, validar que el plan de migración es completo
3. **Revisión de Retiro** (UserTask):
   - Asignado a: `governance-managers`, `project-managers`
   - SLA: 72 horas
   - Formulario: Revisar plan de migración y dependencias
4. **Aprobación/Rechazo:**
   - Si aprobado: Actualizar estado a `APPROVED`, luego `RETIRED`
   - Si rechazado: Actualizar estado a `CANCELLED`, registrar razón

**Verificación:**
- ✅ Verificar que el proceso existe
- ✅ Si no existe, **CREAR** el proceso con la estructura descrita

**Template del proceso:**
```xml
<process id="agent-retirement-v1" name="Agent Retirement V1">
  <startEvent id="start"/>
  <serviceTask id="validateDependencies" name="Validate Dependencies"/>
  <serviceTask id="validateMigrationPlan" name="Validate Migration Plan"/>
  <exclusiveGateway id="validationGateway"/>
  <userTask id="retirementReview" name="Retirement Review"
            activiti:candidateGroups="governance-managers,project-managers"
            activiti:dueDate="P3D"/>
  <exclusiveGateway id="decisionGateway"/>
  <serviceTask id="approveRetirement" name="Approve Retirement"/>
  <serviceTask id="rejectRetirement" name="Reject Retirement"/>
  <endEvent id="endApproved"/>
  <endEvent id="endRejected"/>
</process>
```

---

### 4. **Política de Gobierno de Agentes** (`agent-governance-policy-v1`)

**Ubicación:** `data/bpmn-processes/aios/agent-governance-policy-v1.bpmn` (CREAR SI NO EXISTE)

**Cuándo se usa:**
- Al enviar política de gobierno para aprobación (`POST /api/governance/agents/governance`)
- Cuando el estado de política cambia a `PENDING`

**Variables del proceso:**
```json
{
  "policyId": "Long",
  "agentUuid": "String",
  "agentName": "String",
  "policyType": "String",
  "policyName": "String",
  "enforcementLevel": "String",
  "createdBy": "String"
}
```

**Tareas del proceso:**
1. **Validar Política** (ServiceTask):
   - Verificar formato y completitud
   - Validar contra políticas existentes
2. **Revisión de Política** (UserTask):
   - Asignado a: `governance-managers`, `compliance-officers`
   - SLA: 48 horas
3. **Aprobación/Rechazo:**
   - Si aprobado: Actualizar estado a `APPROVED`, establecer `effectiveFrom`
   - Si rechazado: Actualizar estado a `REJECTED`, registrar razón

**Verificación:**
- ✅ Verificar que el proceso existe
- ✅ Si no existe, **CREAR** el proceso

---

### 5. **Evaluación de Cumplimiento de Agentes** (`agent-compliance-assessment-v1`)

**Ubicación:** `data/bpmn-processes/compliance/compliance-assessment-process.bpmn20.xml` (REUTILIZAR O CREAR ESPECÍFICO)

**Cuándo se usa:**
- Al enviar evaluación de cumplimiento para revisión (`POST /api/governance/agents/compliance`)
- Cuando el estado de cumplimiento cambia a `PENDING`

**Variables del proceso:**
```json
{
  "complianceId": "Long",
  "agentUuid": "String",
  "agentName": "String",
  "complianceType": "String",
  "assessorId": "String",
  "assessorName": "String",
  "assessmentMethod": "String (MANUAL, AUTOMATIC, HYBRID, CONTINUOUS)"
}
```

**Tareas del proceso:**
1. **Ejecutar Evaluación** (ServiceTask):
   - Si `assessmentMethod` es AUTOMATIC o HYBRID, ejecutar evaluación automática
2. **Revisión de Evaluación** (UserTask):
   - Asignado a: `compliance-officers`
   - SLA: 24 horas
3. **Aprobación/Rechazo:**
   - Si aprobado: Actualizar estado a `COMPLIANT`, registrar `complianceScore`
   - Si rechazado: Actualizar estado a `NON_COMPLIANT`, registrar acciones correctivas

**Verificación:**
- ✅ Verificar que el proceso existe o puede reutilizarse
- ✅ Si no existe, **CREAR** proceso específico para agentes

---

### 6. **Evaluación Ética de Agentes** (`agent-ethics-assessment-v1`)

**Ubicación:** `data/bpmn-processes/compliance/ethics-review-v1.bpmn` (REUTILIZAR O CREAR ESPECÍFICO)

**Cuándo se usa:**
- Al enviar evaluación ética para revisión (`POST /api/governance/agents/ethics`)
- Cuando el estado de evaluación cambia a `PENDING`

**Variables del proceso:**
```json
{
  "ethicsId": "Long",
  "agentUuid": "String",
  "agentName": "String",
  "assessmentType": "String",
  "humanReviewerId": "String",
  "humanReviewerName": "String",
  "algorithmUsed": "String",
  "datasetUsed": "String"
}
```

**Tareas del proceso:**
1. **Ejecutar Evaluación Ética** (ServiceTask):
   - Llamar a microservicio de evaluación ética si es automático
2. **Revisión Ética** (UserTask):
   - Asignado a: `ethics-officers`, `governance-managers`
   - SLA: 48 horas
3. **Aprobación/Rechazo:**
   - Si aprobado: Actualizar estado a `APPROVED`, registrar `overallScore` y `riskLevel`
   - Si rechazado: Actualizar estado a `REJECTED`, registrar acciones correctivas

**Verificación:**
- ✅ Verificar que el proceso existe o puede reutilizarse
- ✅ Si no existe, **CREAR** proceso específico para agentes

---

### 7. **Revisión HITL de Decisiones** (`agent-decision-review-v1`)

**Ubicación:** `data/bpmn-processes/aios/agent-decision-review-v1.bpmn` (CREAR SI NO EXISTE)

**Cuándo se usa:**
- Cuando una decisión del agente requiere revisión humana según reglas configuradas
- Al hacer clic en "Aprobar", "Rechazar" o "Escalar" en la pantalla de revisión

**Variables del proceso:**
```json
{
  "decisionId": "Long",
  "agentUuid": "String",
  "agentName": "String",
  "decisionType": "String",
  "confidenceScore": "Double",
  "decisionReason": "String",
  "inputData": "String (JSON)",
  "outputData": "String (JSON)",
  "reviewerId": "String",
  "reviewComments": "String",
  "reviewAction": "String (APPROVE, REJECT, ESCALATE)"
}
```

**Tareas del proceso:**
1. **Evaluar Necesidad de Revisión** (BusinessRuleTask):
   - Ejecutar reglas Drools configuradas para el agente
   - Ejecutar prompts LLM configurados para razonamiento complejo
   - Determinar si requiere revisión HITL
2. **Revisión HITL** (UserTask):
   - Asignado según reglas de negocio
   - SLA: 4 horas para decisiones de alta prioridad
3. **Acciones:**
   - **APPROVE**: Marcar decisión como aprobada, permitir ejecución
   - **REJECT**: Marcar decisión como rechazada, registrar razón
   - **ESCALATE**: Escalar a nivel superior, crear nueva tarea

**Verificación:**
- ✅ Verificar que el proceso existe
- ✅ Si no existe, **CREAR** el proceso

---

### 8. **Aprobación HITL de Reversiones** (`agent-rollback-approval-v1`)

**Ubicación:** `data/bpmn-processes/aios/agent-rollback-approval-v1.bpmn` (CREAR SI NO EXISTE)

**Cuándo se usa:**
- Cuando una reversión automática requiere aprobación humana
- Al hacer clic en "Aprobar Reversión", "Rechazar Reversión" o "Escalar"

**Variables del proceso:**
```json
{
  "rollbackId": "Long",
  "agentUuid": "String",
  "agentName": "String",
  "rollbackType": "String",
  "fromVersion": "String",
  "toVersion": "String",
  "rollbackReason": "String",
  "triggerEvent": "String",
  "approverId": "String",
  "approvalComments": "String",
  "approvalAction": "String (APPROVE, REJECT, ESCALATE)"
}
```

**Tareas del proceso:**
1. **Validar Reversión** (ServiceTask):
   - Verificar que las versiones existen
   - Validar que la reversión es segura
2. **Revisión de Reversión** (UserTask):
   - Asignado a: `governance-managers`, `devops`
   - SLA: 8 horas
3. **Acciones:**
   - **APPROVE**: Ejecutar reversión, actualizar estado a `APPROVED`
   - **REJECT**: Cancelar reversión, actualizar estado a `REJECTED`
   - **ESCALATE**: Escalar a nivel superior

**Verificación:**
- ✅ Verificar que el proceso existe
- ✅ Si no existe, **CREAR** el proceso

---

## 🔧 PROCESO DE VERIFICACIÓN Y CREACIÓN

### Al Iniciar el Sistema

El agente BPMN debe ejecutar el siguiente flujo al iniciar:

```python
def verify_and_create_bpmn_processes():
    """
    Verifica y crea procesos BPMN necesarios para agentes
    """
    required_processes = [
        {
            "id": "agent-approval-v1",
            "path": "data/bpmn-processes/aios/agent-approval-v1.bpmn",
            "required": True,
            "template": "agent-approval-template.bpmn"
        },
        {
            "id": "agent-certification-v1",
            "path": "data/bpmn-processes/aios/agent-certification-v1.bpmn",
            "required": True,
            "template": "agent-certification-template.bpmn"
        },
        {
            "id": "agent-retirement-v1",
            "path": "data/bpmn-processes/aios/agent-retirement-v1.bpmn",
            "required": True,
            "template": "agent-retirement-template.bpmn"
        },
        {
            "id": "agent-governance-policy-v1",
            "path": "data/bpmn-processes/aios/agent-governance-policy-v1.bpmn",
            "required": True,
            "template": "agent-governance-policy-template.bpmn"
        },
        {
            "id": "agent-compliance-assessment-v1",
            "path": "data/bpmn-processes/compliance/agent-compliance-assessment-v1.bpmn",
            "required": True,
            "template": "agent-compliance-assessment-template.bpmn"
        },
        {
            "id": "agent-ethics-assessment-v1",
            "path": "data/bpmn-processes/compliance/agent-ethics-assessment-v1.bpmn",
            "required": True,
            "template": "agent-ethics-assessment-template.bpmn"
        },
        {
            "id": "agent-decision-review-v1",
            "path": "data/bpmn-processes/aios/agent-decision-review-v1.bpmn",
            "required": True,
            "template": "agent-decision-review-template.bpmn"
        },
        {
            "id": "agent-rollback-approval-v1",
            "path": "data/bpmn-processes/aios/agent-rollback-approval-v1.bpmn",
            "required": True,
            "template": "agent-rollback-approval-template.bpmn"
        }
    ]

    for process in required_processes:
        if not process_exists(process["id"]):
            if process["required"]:
                create_process_from_template(process["template"], process["path"])
                log.info(f"Created BPMN process: {process['id']}")
            else:
                log.warning(f"Optional BPMN process missing: {process['id']}")
        else:
            validate_process(process["id"])
            log.info(f"Verified BPMN process: {process['id']}")
```

### Validación de Procesos

Para cada proceso, verificar:

1. **Estructura XML válida:**
   - Validar sintaxis BPMN 2.0
   - Verificar que todos los elementos están correctamente definidos

2. **Variables requeridas:**
   - Verificar que todas las variables del proceso están documentadas
   - Verificar tipos de datos correctos

3. **Delegates Java:**
   - Verificar que los ServiceTask tienen delegates implementados
   - Verificar que los BusinessRuleTask tienen reglas Drools configuradas

4. **UserTasks:**
   - Verificar que tienen grupos de candidatos definidos
   - Verificar que tienen SLA configurado
   - Verificar que tienen formularios asociados (si aplica)

5. **Integración con Backend:**
   - Verificar que los endpoints del backend están disponibles
   - Verificar que las variables del proceso mapean correctamente a los DTOs

---

## 📝 TEMPLATES DE PROCESOS

### Template: Agent Certification

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             targetNamespace="http://www.codeflowx.org/bpmn/agents">
  <process id="agent-certification-v1" name="Agent Certification V1" isExecutable="true">
    <documentation>
      Proceso de certificación de agentes de IA.
      Verifica requisitos de cumplimiento antes de certificar un agente para producción.
    </documentation>

    <startEvent id="start" name="Inicio"/>

    <serviceTask id="verifyRequirements" name="Verificar Requisitos de Certificación"
                  activiti:class="com.codeflowx.govern.workflow.delegates.VerifyCertificationRequirementsDelegate">
      <documentation>
        Verifica que todos los requisitos estén cumplidos:
        - Clasificación de riesgo
        - Evaluación FRIA
        - Registro EU
        - Declaración de conformidad
        - Documentación técnica
        - QMS
      </documentation>
    </serviceTask>

    <businessRuleTask id="validateRequirements" name="Validar Requisitos (Drools)"
                      activiti:ruleVariablesInput="${certificationFact}"
                      activiti:rules="agent-certification-validation"
                      activiti:resultVariable="validationResult">
      <documentation>
        Ejecuta reglas Drools para validar requisitos de certificación.
      </documentation>
    </businessRuleTask>

    <exclusiveGateway id="requirementsGateway" name="¿Requisitos Cumplidos?"/>

    <userTask id="certificationReview" name="Revisión de Certificación"
              activiti:candidateGroups="compliance-officers,governance-managers"
              activiti:dueDate="P2D"
              activiti:formKey="bpmn/agent-certification-review-form.zul"
              activiti:priority="80">
      <documentation>
        Revisión humana de certificación. SLA: 48 horas.
      </documentation>
    </userTask>

    <exclusiveGateway id="decisionGateway" name="Decisión"/>

    <serviceTask id="approveCertification" name="Aprobar Certificación"
                 activiti:class="com.codeflowx.govern.workflow.delegates.ApproveAgentCertificationDelegate">
      <documentation>
        Actualiza estado a CERTIFIED y genera certificateId.
      </documentation>
    </serviceTask>

    <serviceTask id="rejectCertification" name="Rechazar Certificación"
                 activiti:class="com.codeflowx.govern.workflow.delegates.RejectAgentCertificationDelegate">
      <documentation>
        Actualiza estado a NOT_CERTIFIED y registra razón.
      </documentation>
    </serviceTask>

    <endEvent id="endApproved" name="Certificado"/>
    <endEvent id="endRejected" name="No Certificado"/>

    <!-- Sequence flows -->
    <sequenceFlow id="flow1" sourceRef="start" targetRef="verifyRequirements"/>
    <sequenceFlow id="flow2" sourceRef="verifyRequirements" targetRef="validateRequirements"/>
    <sequenceFlow id="flow3" sourceRef="validateRequirements" targetRef="requirementsGateway"/>
    <sequenceFlow id="flow4" name="Todos Cumplidos" sourceRef="requirementsGateway" targetRef="certificationReview">
      <conditionExpression xsi:type="tFormalExpression">
        <![CDATA[${validationResult.allRequirementsMet == true}]]>
      </conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow5" name="Faltan Requisitos" sourceRef="requirementsGateway" targetRef="rejectCertification">
      <conditionExpression xsi:type="tFormalExpression">
        <![CDATA[${validationResult.allRequirementsMet == false}]]>
      </conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow6" sourceRef="certificationReview" targetRef="decisionGateway"/>
    <sequenceFlow id="flow7" name="Aprobar" sourceRef="decisionGateway" targetRef="approveCertification">
      <conditionExpression xsi:type="tFormalExpression">
        <![CDATA[${human_decision == 'approve'}]]>
      </conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow8" name="Rechazar" sourceRef="decisionGateway" targetRef="rejectCertification">
      <conditionExpression xsi:type="tFormalExpression">
        <![CDATA[${human_decision == 'reject'}]]>
      </conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow9" sourceRef="approveCertification" targetRef="endApproved"/>
    <sequenceFlow id="flow10" sourceRef="rejectCertification" targetRef="endRejected"/>
  </process>
</definitions>
```

---

## 🔗 INTEGRACIÓN CON BACKEND

### Endpoints del Backend

Los procesos BPMN deben integrarse con los siguientes endpoints:

1. **Aprobación:**
   - `POST /api/v1/agents/approval` - Crear aprobación
   - `PUT /api/v1/agents/approval/{id}` - Actualizar estado

2. **Certificación:**
   - `POST /api/v1/agents/registry/{id}/certification` - Crear/actualizar certificación
   - `GET /api/v1/agents/registry/{id}/certification` - Obtener certificación

3. **Retiro:**
   - `POST /api/v1/agents/registry/{id}/retirement` - Crear solicitud de retiro
   - `PUT /api/v1/agents/registry/{id}/retirement` - Actualizar estado

4. **Política de Gobierno:**
   - `POST /api/governance/agents/governance` - Crear política
   - `PUT /api/governance/agents/governance/{id}` - Actualizar política

5. **Cumplimiento:**
   - `POST /api/governance/agents/compliance` - Crear evaluación
   - `PUT /api/governance/agents/compliance/{id}` - Actualizar evaluación

6. **Ética:**
   - `POST /api/governance/agents/ethics` - Crear evaluación
   - `PUT /api/governance/agents/ethics/{id}` - Actualizar evaluación

---

## ✅ CHECKLIST DE VERIFICACIÓN

### Al Iniciar el Sistema

- [ ] Verificar que `agent-approval-v1.bpmn` existe y es válido
- [ ] Verificar que `agent-certification-v1.bpmn` existe (crear si no existe)
- [ ] Verificar que `agent-retirement-v1.bpmn` existe (crear si no existe)
- [ ] Verificar que `agent-governance-policy-v1.bpmn` existe (crear si no existe)
- [ ] Verificar que `agent-compliance-assessment-v1.bpmn` existe (crear si no existe)
- [ ] Verificar que `agent-ethics-assessment-v1.bpmn` existe (crear si no existe)
- [ ] Verificar que `agent-decision-review-v1.bpmn` existe (crear si no existe)
- [ ] Verificar que `agent-rollback-approval-v1.bpmn` existe (crear si no existe)

### Validación de Cada Proceso

- [ ] Validar sintaxis BPMN 2.0
- [ ] Verificar que todas las variables están documentadas
- [ ] Verificar que los ServiceTask tienen delegates implementados
- [ ] Verificar que los BusinessRuleTask tienen reglas Drools configuradas
- [ ] Verificar que los UserTask tienen grupos de candidatos
- [ ] Verificar que los UserTask tienen SLA configurado
- [ ] Verificar que los endpoints del backend están disponibles

---

## 🚨 ACCIONES EN CASO DE ERROR

### Si un Proceso No Existe

1. **Crear el proceso desde template:**
   - Usar el template correspondiente
   - Ajustar variables según el contexto
   - Validar sintaxis BPMN

2. **Registrar en el sistema:**
   - Guardar en `data/bpmn-processes/aios/` o `data/bpmn-processes/compliance/`
   - Registrar en el motor BPMN
   - Verificar que se puede ejecutar

3. **Notificar:**
   - Log de creación del proceso
   - Alertar a administradores si es crítico

### Si un Proceso Tiene Errores

1. **Validar sintaxis:**
   - Usar validador BPMN
   - Corregir errores encontrados

2. **Verificar integración:**
   - Verificar que los delegates existen
   - Verificar que las reglas Drools existen
   - Verificar que los endpoints están disponibles

3. **Regenerar si es necesario:**
   - Si el proceso está corrupto, regenerar desde template
   - Mantener versiones anteriores para rollback

---

## 📚 REFERENCIAS

- **BPMN 2.0 Specification:** https://www.omg.org/spec/BPMN/2.0/
- **Flowable Engine:** https://www.flowable.com/
- **Procesos Existentes:** `data/bpmn-processes/`
- **Delegates Java:** `codeflowx.govern.workflow.delegates.*`
- **Reglas Drools:** `mocks/drools/*.drl`

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** ✅ Operativo
