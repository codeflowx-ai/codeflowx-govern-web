# PROMPT DE LÓGICA DE NEGOCIO - MÓDULO AGENTS

**Fecha:** Diciembre 2025
**Módulo:** Agents (Agentes de IA)
**Objetivo:** Definir la lógica de negocio completa para el módulo de gestión de agentes de IA
**Esfuerzo Estimado:** 4-5 días

> **⚠️ IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** en este prompt.
> Las operaciones CRUD y consultas BBDD están implementadas, pero las integraciones con microservicios externos están pendientes de implementar cuando estén disponibles.

---

## 📋 CONTEXTO DEL MÓDULO

### **Descripción Funcional**
El módulo Agents gestiona el ciclo de vida completo de agentes de inteligencia artificial, incluyendo:
- Registro y aprobación de agentes
- Monitoreo de interacciones en tiempo real
- Análisis de ética y comportamiento
- Gestión de decisiones automatizadas
- Versionado y rollback de agentes
- Aprendizaje continuo y mejora
- Compliance con EU AI Act

### **Pantallas Asociadas**

#### **ViewModels Identificados (56 ViewModels)**
**Referencia:** `suinsit.nova.web/docs/migration/prompts/MIGRACION_AGENTS_01.md`

**ViewModels Principales:**
- `AgentApprovalWorkflowViewModel` - Workflow de aprobación de agentes
- `AgentsOverviewViewModel` - Vista general de agentes
- `AgentsDetailViewModel` - Detalles de agente
- `AgentHealthDashboardOverviewViewModel` - Dashboard de salud de agentes
- `AgentInteractionOverviewViewModel` - Interacciones de agentes
- `AgentLearningOverviewViewModel` - Aprendizaje de agentes
- `AgentMonitoringOverviewViewModel` - Monitoreo de agentes
- `AgentApprovalHumanOverrideViewModel` - Override humano (BPMN)
- Y 48 ViewModels adicionales para gestión completa de agentes

#### **Pantallas ZUL Identificadas (54 pantallas)**
**Referencia:** `suinsit.nova.web/docs/funcional/agents/01_REORGANIZACION_PANTALLAS_AGENTES.md`

- **33 pantallas** `*-overview.zul` (listados/consulta)
- **22 pantallas** `*-detail.zul` (edición/creación)
- **2 pantallas** `*-status.zul` (estados específicos)
- **Pantallas BPMN:**
  - `agent-approval-human-override-form.zul` - Override humano
  - `agent-approval-reminder-form.zul` - Recordatorios

#### **Pantallas Next.js Migradas (20+ pantallas)**
**Ubicación:** `app/(app)/agents/`

**Pantallas Principales Migradas:**
- ✅ `app/(app)/agents/monitoring/dashboard/page.tsx` - Dashboard de monitoreo
- ✅ `app/(app)/agents/monitoring/overview/page.tsx` - Vista general de monitoreo
- ✅ `app/(app)/agents/monitoring/health-overview/page.tsx` - Salud de agentes
- ✅ `app/(app)/agents/approval/overview/page.tsx` - Aprobaciones
- ✅ `app/(app)/agents/deployment/overview/page.tsx` - Despliegues
- ✅ `app/(app)/agents/interactions/overview/page.tsx` - Interacciones
- ✅ `app/(app)/agents/interactions/collaboration-overview/page.tsx` - Colaboración
- ✅ `app/(app)/agents/interactions/communication-overview/page.tsx` - Comunicación
- ✅ `app/(app)/agents/learning/overview/page.tsx` - Aprendizaje
- ✅ `app/(app)/agents/registry/domain-overview/page.tsx` - Dominios
- ✅ `app/(app)/agents/versioning/overview/page.tsx` - Versionado
- ✅ `app/(app)/agents/rollback/overview/page.tsx` - Rollback
- ✅ `app/(app)/agents/compliance/overview/page.tsx` - Compliance
- ✅ `app/(app)/agents/governance/overview/page.tsx` - Governance
- ✅ `app/(app)/agents/ethics/overview/page.tsx` - Ética
- ✅ `app/(app)/agents/decisions/overview/page.tsx` - Decisiones
- ✅ `app/(app)/agents/tools/overview/page.tsx` - Herramientas
- ✅ `app/(app)/agents/transparency/overview/page.tsx` - Transparencia
- ✅ `app/(app)/agents/bias-detection/overview/page.tsx` - Detección de sesgo
- ✅ `app/(app)/agents/alerts/overview/page.tsx` - Alertas

**Pantallas BPMN Migradas:**
- ✅ `app/(app)/bpmn/forms/agent-approval-human-override/page.tsx`

**Referencia:** `codeflowx-studio/docs/PLAN_MIGRACION_ZUL_VIEWMODELS.md`

### **Entidades JPA Principales**
- `Agent` - Entidad principal de agentes
- `AgentApproval` - Aprobaciones de agentes
- `AgentMonitoring` - Monitoreo de agentes
- `AgentInteraction` - Interacciones de agentes
- `AgentDecision` - Decisiones de agentes
- `AgentVersion` - Versiones de agentes
- `AgentLearning` - Aprendizaje de agentes
- `AgentEthics` - Evaluaciones éticas

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Business Services Existentes**
1. **ProhibitedSystemBusinessService** (✅ Creado)
   - `checkProhibitedSystem(Agent)` - Verifica si agente es sistema prohibido

### **Servicios CRUD (codeflowx.govern.services)**
- `AgentService` - CRUD básico de agentes
- `AgentApprovalService` - CRUD de aprobaciones
- `AgentMonitoringService` - CRUD de monitoreo
- `AgentInteractionService` - CRUD de interacciones

### **Microservicios Python Disponibles (COMENTADOS)**
- `codeflowx-governance-api` - API de governance
- `codeflowx-aios-telemetry` - Telemetría y métricas
- `codeflowx-aios-api` - API de AIOS

---

## 📚 REFERENCIAS DE PROMPTS JAVA

### **Prompt A.3 - Extensión Entidad Agent.java** (PROMPTS_03_JAVA_BACKEND_EXISTENTE.md)
**Campos EU AI Act añadidos:**
- `AGTISHIGHRISK` - Boolean si agente es alto riesgo
- `AGTANNEXIIICATEGORIES` - Array categorías Anexo III
- `AGTCLASSIFICATIONDATE` - Timestamp clasificación
- `AGTETHICSASSESSMENT` - Evaluación ética (JSONB)
- `AGTHUMANOVERSIGHT` - Supervisión humana requerida

---

## 🔍 VALIDACIONES DE AUDITORÍA

### **INC-005: Falta Validación de Sistemas Prohibidos (Art. 5)**
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 5, Anexo II

**Validación Requerida:**
```java
// Validar que se haya verificado contra sistemas prohibidos antes de aprobar
if (agent.getAgtprohibitedusechecked() == null || !agent.getAgtprohibitedusechecked()) {
    throw new ValidationException(
        "CRITICAL: Debe verificar contra Art. 5 (sistemas prohibidos) antes de aprobar agente."
    );
}
```

---

## 💼 LÓGICA DE NEGOCIO - BUSINESS SERVICES

### **1. AgentBusinessService**

#### **1.1. Operaciones CRUD Básicas**

**Método: `createAgent(Agent agent, String createdBy)`**
```java
/**
 * Crea un nuevo agente con validaciones iniciales
 *
 * Validaciones:
 * - Nombre único
 * - Tipo de agente válido
 * - Modelo asociado válido (si aplica)
 *
 * Consulta BBDD:
 * SELECT COUNT(*) FROM agtagents WHERE agtname = ?
 */
public Agent createAgent(Agent agent, String createdBy) {
    // 1. Validar nombre único
    validateUniqueName(agent.getAgtname());

    // 2. Validar modelo asociado (si aplica)
    if (agent.getIdxmodel() != null) {
        validateModel(agent.getIdxmodel());
    }

    // 3. Establecer valores por defecto
    agent.setAgtstatus("DRAFT");
    agent.setAgtcreatedby(createdBy);
    agent.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));

    // 4. Generar UUID si no existe
    if (agent.getIduuid() == null) {
        agent.setIduuid(UUID.randomUUID().toString());
    }

    // 5. Inicializar campos de compliance
    agent.setAgtishighrisk(false);
    agent.setAgtprohibitedusechecked(false);
    agent.setAgthumanoversight(false);

    // 6. Guardar
    return noCodeClient.save(agent);
}
```

**Método: `approveAgent(Long agentId, String approvedBy, String approvalNotes)`**
```java
/**
 * Aprueba un agente para uso en producción
 *
 * Validaciones:
 * - Agente existe
 * - No está ya aprobado
 * - Verificación contra sistemas prohibidos completada
 * - Evaluación ética completada (si aplica)
 *
 * Consultas BBDD:
 * SELECT * FROM agtagents WHERE idxagent = ?
 * SELECT * FROM agtagentapprovals WHERE idxagent = ? AND agtapprovalstatus = 'APPROVED'
 */
public AgentApproval approveAgent(Long agentId, String approvedBy, String approvalNotes) {
    Agent agent = noCodeClient.findById(Agent.class, agentId);
    if (agent == null) {
        throw new EntityNotFoundException("Agente no encontrado: " + agentId);
    }

    // Validar que no esté ya aprobado
    AgentApproval existingApproval = findActiveApproval(agentId);
    if (existingApproval != null && "APPROVED".equals(existingApproval.getAgtapprovalstatus())) {
        throw new BusinessException("Agente ya está aprobado");
    }

    // VALIDACIÓN: Verificación contra sistemas prohibidos (INC-005)
    if (agent.getAgtprohibitedusechecked() == null || !agent.getAgtprohibitedusechecked()) {
        throw new ValidationException(
            "CRITICAL: Debe verificar contra Art. 5 (sistemas prohibidos) antes de aprobar agente."
        );
    }

    // Crear registro de aprobación
    AgentApproval approval = new AgentApproval();
    approval.setAgent(agent);
    approval.setAgtapprovalstatus("APPROVED");
    approval.setAgtapprovalnotes(approvalNotes);
    approval.setAgtapprovalapprovedby(approvedBy);
    approval.setAgtapprovalapprovedat(new Timestamp(System.currentTimeMillis()));

    // Actualizar estado del agente
    agent.setAgtstatus("APPROVED");
    agent.setAgtupdatedby(approvedBy);
    agent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(agent);

    return noCodeClient.save(approval);
}
```

**Método: `rejectAgent(Long agentId, String rejectedBy, String rejectionReason)`**
```java
/**
 * Rechaza un agente
 *
 * Consultas BBDD:
 * SELECT * FROM agtagents WHERE idxagent = ?
 * INSERT INTO agtagentapprovals (...)
 */
public AgentApproval rejectAgent(Long agentId, String rejectedBy, String rejectionReason) {
    Agent agent = noCodeClient.findById(Agent.class, agentId);
    if (agent == null) {
        throw new EntityNotFoundException("Agente no encontrado: " + agentId);
    }

    AgentApproval rejection = new AgentApproval();
    rejection.setAgent(agent);
    rejection.setAgtapprovalstatus("REJECTED");
    rejection.setAgtapprovalnotes(rejectionReason);
    rejection.setAgtapprovalapprovedby(rejectedBy);
    rejection.setAgtapprovalapprovedat(new Timestamp(System.currentTimeMillis()));

    // Actualizar estado del agente
    agent.setAgtstatus("REJECTED");
    agent.setAgtupdatedby(rejectedBy);
    agent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(agent);

    return noCodeClient.save(rejection);
}
```

#### **1.2. Operaciones de Monitoreo**

**Método: `recordAgentInteraction(Long agentId, InteractionData data, String recordedBy)`**
```java
/**
 * Registra una interacción del agente
 *
 * Consultas BBDD:
 * INSERT INTO agtagentinteractions (...)
 * UPDATE agtagents SET agtlastinteractionat = ? WHERE idxagent = ?
 */
public AgentInteraction recordAgentInteraction(Long agentId, InteractionData data, String recordedBy) {
    Agent agent = noCodeClient.findById(Agent.class, agentId);
    if (agent == null) {
        throw new EntityNotFoundException("Agente no encontrado: " + agentId);
    }

    AgentInteraction interaction = new AgentInteraction();
    interaction.setAgent(agent);
    interaction.setAgtinteractiontype(data.getInteractionType());
    interaction.setAgtinteractioninput(data.getInput());
    interaction.setAgtinteractionoutput(data.getOutput());
    interaction.setAgtinteractiontimestamp(new Timestamp(System.currentTimeMillis()));
    interaction.setAgtinteractionrecordedby(recordedBy);

    // Actualizar última interacción del agente
    agent.setAgtlastinteractionat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(agent);

    return noCodeClient.save(interaction);
}
```

**Método: `recordAgentDecision(Long agentId, DecisionData data, String recordedBy)`**
```java
/**
 * Registra una decisión tomada por el agente
 *
 * Consultas BBDD:
 * INSERT INTO agtagentdecisions (...)
 */
public AgentDecision recordAgentDecision(Long agentId, DecisionData data, String recordedBy) {
    Agent agent = noCodeClient.findById(Agent.class, agentId);
    if (agent == null) {
        throw new EntityNotFoundException("Agente no encontrado: " + agentId);
    }

    AgentDecision decision = new AgentDecision();
    decision.setAgent(agent);
    decision.setAgtdecisiontype(data.getDecisionType());
    decision.setAgtdecisioncontext(data.getContext());
    decision.setAgtdecisionoutcome(data.getOutcome());
    decision.setAgtdecisionconfidence(data.getConfidence());
    decision.setAgtdecisiontimestamp(new Timestamp(System.currentTimeMillis()));
    decision.setAgtdecisionrecordedby(recordedBy);

    return noCodeClient.save(decision);
}
```

#### **1.3. Operaciones de Evaluación Ética**

**Método: `evaluateAgentEthics(Long agentId, EthicsEvaluationConfig config, String evaluatedBy)`**
```java
/**
 * Evalúa aspectos éticos del agente
 *
 * Consultas BBDD:
 * INSERT INTO agtagentethics (...)
 * UPDATE agtagents SET agtethicsassessment = ? WHERE idxagent = ?
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-governance-api: POST /api/v1/agents/{agentId}/ethics-evaluation
 */
public AgentEthics evaluateAgentEthics(Long agentId, EthicsEvaluationConfig config, String evaluatedBy) {
    Agent agent = noCodeClient.findById(Agent.class, agentId);
    if (agent == null) {
        throw new EntityNotFoundException("Agente no encontrado: " + agentId);
    }

    AgentEthics ethics = new AgentEthics();
    ethics.setAgent(agent);
    ethics.setAgtethicsbiasScore(config.getBiasScore());
    ethics.setAgtethicsfairnessScore(config.getFairnessScore());
    ethics.setAgtethicsprivacyScore(config.getPrivacyScore());
    ethics.setAgtethicstransparencyScore(config.getTransparencyScore());
    ethics.setAgtethicsevaluatedat(new Timestamp(System.currentTimeMillis()));
    ethics.setAgtethicsevaluatedby(evaluatedBy);

    // Calcular score general
    BigDecimal overallScore = calculateOverallEthicsScore(ethics);
    ethics.setAgtethicsoverallScore(overallScore);

    // Actualizar evaluación en agente
    agent.setAgtethicsassessment(ethics.toJson());
    noCodeClient.save(agent);

    // TODO: Llamar a microservicio Python para evaluación avanzada
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        EthicsEvaluationResult result = governanceApiClient.evaluateAgentEthics(agentId, config);
        ethics.setAgtethicsdetailedResults(result.toJson());
    } catch (Exception e) {
        log.error("Error en evaluación ética avanzada", e);
    }
    */

    return noCodeClient.save(ethics);
}
```

#### **1.4. Operaciones de Versionado**

**Método: `createAgentVersion(Long agentId, String versionName, String createdBy)`**
```java
/**
 * Crea una nueva versión del agente
 *
 * Consultas BBDD:
 * SELECT * FROM agtagents WHERE idxagent = ?
 * SELECT MAX(agtversionnumber) FROM agtagentversions WHERE idxagent = ?
 * INSERT INTO agtagentversions (...)
 */
public AgentVersion createAgentVersion(Long agentId, String versionName, String createdBy) {
    Agent agent = noCodeClient.findById(Agent.class, agentId);
    if (agent == null) {
        throw new EntityNotFoundException("Agente no encontrado: " + agentId);
    }

    // Obtener siguiente número de versión
    Integer nextVersion = getNextVersionNumber(agentId);

    AgentVersion version = new AgentVersion();
    version.setAgent(agent);
    version.setAgtversionname(versionName);
    version.setAgtversionnumber(nextVersion);
    version.setAgtversionstatus("DRAFT");
    version.setAgtversioncreatedat(new Timestamp(System.currentTimeMillis()));
    version.setAgtversioncreatedby(createdBy);

    // Copiar configuración del agente actual
    version.setAgtversionconfig(agent.getAgtconfig());

    return noCodeClient.save(version);
}
```

**Método: `rollbackToVersion(Long agentId, Long versionId, String rolledBackBy)`**
```java
/**
 * Hace rollback del agente a una versión anterior
 *
 * Validaciones:
 * - Versión existe
 * - Versión está aprobada
 * - No hay interacciones activas
 *
 * Consultas BBDD:
 * SELECT * FROM agtagentversions WHERE idxagentversion = ?
 * SELECT COUNT(*) FROM agtagentinteractions WHERE idxagent = ? AND agtinteractionstatus = 'ACTIVE'
 */
public Agent rollbackToVersion(Long agentId, Long versionId, String rolledBackBy) {
    Agent agent = noCodeClient.findById(Agent.class, agentId);
    if (agent == null) {
        throw new EntityNotFoundException("Agente no encontrado: " + agentId);
    }

    AgentVersion version = noCodeClient.findById(AgentVersion.class, versionId);
    if (version == null || !version.getAgent().getIdxagent().equals(agentId)) {
        throw new EntityNotFoundException("Versión no encontrada o no pertenece al agente");
    }

    // Validar que versión esté aprobada
    if (!"APPROVED".equals(version.getAgtversionstatus())) {
        throw new BusinessException("Solo se puede hacer rollback a versiones aprobadas");
    }

    // Validar que no haya interacciones activas
    long activeInteractions = countActiveInteractions(agentId);
    if (activeInteractions > 0) {
        throw new BusinessException(
            "No se puede hacer rollback con " + activeInteractions + " interacciones activas"
        );
    }

    // Restaurar configuración de la versión
    agent.setAgtconfig(version.getAgtversionconfig());
    agent.setAgtversion(version.getAgtversionnumber().toString());
    agent.setAgtupdatedby(rolledBackBy);
    agent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));

    return noCodeClient.save(agent);
}
```

---

## 📊 CONSULTAS BBDD ESPECÍFICAS

### **1. Obtener agentes con verificación de sistemas prohibidos pendiente**
```sql
SELECT a.idxagent, a.agtname, a.agtishighrisk, a.agtprohibitedusechecked
FROM agtagents a
WHERE a.agtishighrisk = true
  AND (a.agtprohibitedusechecked IS NULL OR a.agtprohibitedusechecked = false)
ORDER BY a.agtcreatedat DESC;
```

### **2. Obtener interacciones recientes de un agente**
```sql
SELECT ai.idxagentinteraction, ai.agtinteractiontype,
       ai.agtinteractiontimestamp, ai.agtinteractioninput, ai.agtinteractionoutput
FROM agtagentinteractions ai
WHERE ai.idxagent = ?
ORDER BY ai.agtinteractiontimestamp DESC
LIMIT 100;
```

### **3. Obtener decisiones del agente por tipo**
```sql
SELECT ad.idxagentdecision, ad.agtdecisiontype, ad.agtdecisionoutcome,
       ad.agtdecisionconfidence, ad.agtdecisiontimestamp
FROM agtagentdecisions ad
WHERE ad.idxagent = ?
  AND ad.agtdecisiontype = ?
ORDER BY ad.agtdecisiontimestamp DESC;
```

### **4. Obtener aprobaciones de un agente**
```sql
SELECT aa.idxagentapproval, aa.agtapprovalstatus, aa.agtapprovalnotes,
       aa.agtapprovalapprovedby, aa.agtapprovalapprovedat
FROM agtagentapprovals aa
WHERE aa.idxagent = ?
ORDER BY aa.agtapprovalapprovedat DESC;
```

### **5. Obtener versiones de un agente**
```sql
SELECT av.idxagentversion, av.agtversionname, av.agtversionnumber,
       av.agtversionstatus, av.agtversioncreatedat
FROM agtagentversions av
WHERE av.idxagent = ?
ORDER BY av.agtversionnumber DESC;
```

### **6. Obtener métricas de monitoreo del agente**
```sql
SELECT am.idxagentmonitoring, am.agtmonitoringmetricname,
       am.agtmonitoringmetricvalue, am.agtmonitoringtimestamp
FROM agtagentmonitoring am
WHERE am.idxagent = ?
ORDER BY am.agtmonitoringtimestamp DESC
LIMIT 100;
```

---

## 🔗 INTEGRACIÓN CON MICROSERVICIOS PYTHON

> **⚠️ NOTA IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** y pendientes de implementación.
> Se deben implementar cuando los microservicios estén disponibles y operativos.

### **1. codeflowx-governance-api**

**Endpoint: `POST /api/v1/agents/{agentId}/ethics-evaluation`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public EthicsEvaluationResult evaluateAgentEthics(Long agentId, EthicsEvaluationConfig config) {
    String url = governanceApiBaseUrl + "/api/v1/agents/" + agentId + "/ethics-evaluation";
    Map<String, Object> payload = Map.of("config", config);
    return restTemplate.postForObject(url, payload, EthicsEvaluationResult.class);
}
*/
```

### **2. codeflowx-aios-telemetry**

**Endpoint: `POST /api/v1/telemetry/agents/{agentId}/interactions`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public void sendAgentInteraction(Long agentId, InteractionData data) {
    String url = telemetryBaseUrl + "/api/v1/telemetry/agents/" + agentId + "/interactions";
    restTemplate.postForObject(url, data, Void.class);
}
*/
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Fase 1: Business Service Base**
- [ ] Crear `AgentBusinessService.java`
- [ ] Implementar operaciones CRUD básicas
- [ ] Implementar validaciones de auditoría (INC-005)
- [ ] Implementar métodos de aprobación y rechazo

### **Fase 2: Monitoreo y Evaluación**
- [ ] Implementar registro de interacciones
- [ ] Implementar registro de decisiones
- [ ] Implementar evaluación ética
- [ ] Implementar monitoreo en tiempo real

### **Fase 3: Versionado y Rollback**
- [ ] Implementar creación de versiones
- [ ] Implementar rollback a versiones anteriores
- [ ] Implementar gestión de versiones

### **Fase 4: Integración con Microservicios (PENDIENTE)**
- [ ] ⚠️ **PENDIENTE:** Configurar clientes REST para microservicios Python
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-governance-api`
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-aios-telemetry`
- [ ] **NOTA:** Todas las llamadas a microservicios están comentadas en el código

### **Fase 5: Consultas BBDD**
- [ ] Implementar consultas específicas de validación
- [ ] Implementar consultas de interacciones y decisiones
- [ ] Implementar consultas de monitoreo

### **Fase 6: Testing y Validación**
- [ ] Crear tests unitarios para cada método
- [ ] Validar integración con microservicios
- [ ] Validar consultas BBDD
- [ ] Validar cumplimiento de auditoría

---

## 📝 NOTAS IMPORTANTES

1. **Arquitectura EnArt:** Usar `NoCodeClient` (no Repository) para acceso a datos
2. **Validaciones Críticas:** Implementar todas las validaciones de auditoría antes de permitir operaciones
3. **Microservicios Python:** ⚠️ **TODAS LAS LLAMADAS ESTÁN COMENTADAS** - Pendientes de implementar cuando microservicios estén disponibles
4. **Logging:** Registrar todas las operaciones críticas para auditoría
5. **Transacciones:** Usar `@Transactional` para operaciones que modifican múltiples entidades
6. **Entidades JPA:** Revisar entidades en `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/agents/` para campos disponibles
7. **Monitoreo:** Las interacciones y decisiones deben registrarse en tiempo real para auditoría

---

**Última actualización:** Diciembre 2025
**Estado:** Pendiente de implementación
