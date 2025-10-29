# 📘 MANUAL DEL DESARROLLADOR - PROCESOS BPMN CODEFLOWX

**Versión:** 1.1.0  
**Fecha:** Octubre 2025  
**Audiencia:** Desarrolladores Backend, Integradores  
**Confidencial:** Uso interno CodeflowX

---

## 📋 ÍNDICE

1. [Arquitectura de Procesos](#arquitectura-de-procesos)
2. [Procesos de Aprobación](#procesos-de-aprobación)
3. [Procesos de Detección](#procesos-de-detección)
4. [Procesos de Evaluación](#procesos-de-evaluación)
5. [Procesos de Automatización](#procesos-de-automatización)
6. [Procesos de Governance](#procesos-de-governance)
7. [Referencia Técnica](#referencia-técnica)

---

## 🏗️ ARQUITECTURA DE PROCESOS

### Stack Tecnológico

```
┌─────────────────────────────────────────────────────┐
│  BPMN 2.0 (Flowable 6.8.1)                         │
│  • 17 Procesos Enterprise-Grade                    │
│  • Act_GE_* tables (Flowable)                       │
└─────────────────────────────────────────────────────┘
          ↓
┌─────────────────────────────────────────────────────┐
│  Java Delegates (65 clases)                        │
│  • com.codeflowx.govern.workflow.delegates          │
│  • Implementan JavaDelegate (Flowable)              │
│  • Business logic avanzada                          │
└─────────────────────────────────────────────────────┘
          ↓
┌─────────────────────────────────────────────────────┐
│  Drools Rules Engine                                │
│  • 11 Fact Objects                                  │
│  • 11 DRL files (120+ reglas)                       │
│  • Modificables sin redeploy                        │
└─────────────────────────────────────────────────────┘
          ↓
┌─────────────────────────────────────────────────────┐
│  Services Layer                                     │
│  • TaskManagementService                           │
│  • DroolsRulesService                              │
│  • ComplianceCheckService                           │
│  • RiskAssessmentService                           │
└─────────────────────────────────────────────────────┘
```

### Convenciones de Nombres

| Elemento | Convención | Ejemplo |
|----------|-----------|---------|
| Proceso BPMN | `{entity}-approval-v1.bpmn` | `agent-approval-v1.bpmn` |
| Java Delegate | `{Action}{Entity}Delegate` | `AutoApproveAgentDelegate` |
| Reglas Drools | `{entity}-scoring.drl` | `agent-scoring.drl` |
| Fact Object | `{Entity}Fact` | `AgentApprovalFact` |
| ViewModel | `{Entity}ApprovalWorkflowViewModel` | `AgentApprovalWorkflowViewModel` |

---

## 🔐 PROCESOS DE APROBACIÓN

### 1. Agent Approval (`agent-approval-v1.bpmn`)

**Propósito:** Aprobar agentes IA antes de producción

**Flujo:**
1. **Start Event** → Inicia con `agentId`, `agentName`
2. **Parallel Gateway** → 3 validaciones paralelas:
   - Risk Assessment (AIRiskAssessmentDelegate)
   - Compliance Check (AIComplianceCheckDelegate)
   - Ethics Review (AIEthicalReviewDelegate)
3. **Join** → Espera las 3 validaciones
4. **BusinessRuleTask** → Drools scoring (`agent-scoring.drl`)
5. **Exclusive Gateway** → 3 decisiones:
   - `AUTO_APPROVE` → AutoApproveAgentDelegate
   - `HITL_REQUIRED` → Human Review (user task)
   - `AUTO_REJECT` → AutoRejectAgentDelegate

**Variables de Proceso:**
```java
// INPUT
- agentId: Long
- agentName: String
- approverEmail: String

// OUTPUTS
- riskScore: Integer (0-100)
- complianceScore: Integer (0-100)
- ethicsScore: Integer (0-100)
- compliant: Boolean
- decision: String ("AUTO_APPROVE" | "HITL_REQUIRED" | "AUTO_REJECT")
- minScore: Integer
- confidenceLevel: Double
- justification: String
```

**Java Delegates:**
- `AIRiskAssessmentDelegate` - Usa LLM para evaluar riesgos
- `AIComplianceCheckDelegate` - Verifica compliance GDPR/EU AI Act
- `AIEthicalReviewDelegate` - Evalúa aspectos éticos
- `AutoApproveAgentDelegate` - Marca agente como aprobado
- `AutoRejectAgentDelegate` - Marca agente como rechazado

**Reglas Drools:**
```drl
// agent-scoring.drl
rule "Auto-Approve Agent - High Confidence"
when
  $fact : AgentApprovalFact(
    riskScore >= 80,
    complianceScore >= 80,
    ethicsScore >= 80,
    compliant == true
  )
then
  $fact.setDecision("AUTO_APPROVE");
end
```

**Errores Manejados:**
- Error boundary en `riskTask` → Score conservador (75)

---

### 2. Model Approval (`model-approval-v1.bpmn`)

**Propósito:** Aprobar modelos ML para producción

**Flujo:**
1. User Task → ML Engineer solicita aprobación
2. Parallel Gateway → 3 validaciones:
   - Performance (ModelValidationDelegate)
   - Bias Detection (BiasDetectionDelegate)
   - Compliance Check (ComplianceCheckDelegate)
3. Join
4. User Task → ML Engineer Review
5. User Task → Governance Review (SLA 3 días)
6. BusinessRuleTask → Drools scoring
7. Gateway → APPROVED / CONDITIONAL / REJECTED

**Variables:**
```java
- modelId, versionId
- approvalType: NEW_MODEL | VERSION_UPDATE | REDEPLOYMENT
- targetEnvironment: STAGING | PRODUCTION
- performanceScore, biasScore, complianceScore
- mlEngineerApproval, governanceApproval
- finalDecision: APPROVED | CONDITIONAL_APPROVAL | REJECTED
```

**Java Delegates:**
- `ModelValidationDelegate` - Valida performance
- `BiasDetectionDelegate` - Detecta bias
- `ComplianceCheckDelegate` - Verifica compliance
- `MarkModelProductionDelegate` - Marca para producción
- `MarkModelConditionalDelegate` - Aprobación condicional
- `RejectModelDelegate` - Rechaza modelo

---

### 3. Prompt Approval (`prompt-approval-v1.bpmn`)

**Propósito:** Aprobar prompts para RAG/LLM

**Flujo:**
1. Start → `promptId`, `promptText`
2. Service Task → Prompt Safety Check (PromptSafetyDelegate)
3. BusinessRuleTask → Drools rules
4. Gateway → AUTO_APPROVE | HITL | AUTO_REJECT

**Delegates:**
- `PromptSafetyDelegate` - Verifica seguridad del prompt
- `AutoApprovePromptDelegate` - Auto-aprobación
- `RejectEthicsDelegate` - Rechazo ético

---

## 🔍 PROCESOS DE DETECCIÓN

### 4. Bias Detection (`bias-detection-v1.bpmn`)

**Propósito:** Detectar sesgo en modelos/algoritmos

**Flujo:**
1. Timer Start (diario) o Event Start
2. Service Task → Prepare Demographic Data
3. Service Task → Execute Bias Detection
4. BusinessRuleTask → Drools classification
5. Gateway → Auto-Notify | Escalate | Ignore

**Delegates:**
- `PrepareDemographicDataDelegate` - Prepara datos demográficos
- `ExecuteBiasDetectionDelegate` - Ejecuta detección
- `AutoApproveNoBiasDelegate` - Sin bias detectado
- `RejectBiasedModelDelegate` - Rechaza modelo con bias
- `NotifyBiasAnalysisDelegate` - Notifica análisis

---

### 5. Drift Detection (`drift-detection-v1.bpmn`)

**Propósito:** Detectar concept/data drift en modelos

**Flujo:**
1. Start → `modelId`, `baselineTimestamp`
2. Service Task → Load Baseline Metrics
3. Service Task → Capture Current Metrics
4. Service Task → Analyze Drift
5. BusinessRuleTask → Drools (drift level)
6. Gateway → LOW | MEDIUM | CRITICAL
7. Service Task → Create Drift Alert

**Delegates:**
- `LoadBaselineMetricsDelegate` - Carga métricas baseline
- `CaptureCurrentMetricsDelegate` - Captura métricas actuales
- `AnalyzeDriftDelegate` - Analiza drift
- `CreateDriftAlertDelegate` - Crea alerta
- `SaveDriftResultsDelegate` - Guarda resultados
- `NotifyDriftDelegate` - Notifica drift

---

### 6. Performance Degradation (`performance-degradation-v1.bpmn`)

**Propósito:** Detectar degradación de performance

**Flujo:**
1. Start
2. Service Task → Load Baseline
3. Service Task → Check Current Metrics
4. Service Task → Compare (p-valor)
5. BusinessRuleTask → Drools
6. Gateway → DEGRADATION | OK
7. Service Task → Trigger Retraining

**Delegates:**
- `CheckPerformanceMetricsDelegate` - Verifica métricas
- `TriggerRetrainingDelegate` - Dispara re-entrenamiento
- `TuneThresholdsDelegate` - Ajusta thresholds
- `SavePerformanceMetricsDelegate` - Guarda métricas

---

### 7. Alert Response (`alert-response-v1.bpmn`)

**Propósito:** Responder a alertas del sistema

**Flujo:**
1. Start → `alertId`, `alertType`, `severity`
2. Service Task → Classify Alert (Drools)
3. Gateway → LOW | MEDIUM | HIGH | CRITICAL
4. Service Task → Log/Notify/Escalate
5. End

**Delegates:**
- `ClassifyAlertDelegate` - Clasifica alerta
- `LogLowAlertDelegate` - Registra alertas bajas
- `NotifyHighAlertDelegate` - Notifica alertas altas
- `AutoEscalateCriticalDelegate` - Escala críticas
- `UpdateAlertStatusDelegate` - Actualiza estado

---

## 📊 PROCESOS DE EVALUACIÓN

### 8. LLM Evaluation (`llm-evaluation-v1.bpmn`)

**Propósito:** Evaluar LLMs (GPT, Claude, Llama)

**Flujo:**
1. User Task → Submit LLM
2. Service Task → Prepare Evaluation
3. Service Task → Execute Evaluation
4. BusinessRuleTask → Drools
5. Gateway → APPROVE | REJECT | HITL

**Delegates:**
- `PrepareLlmEvaluationDelegate` - Prepara evaluación
- `ExecuteLlmEvaluationDelegate` - Ejecuta eval
- `AutoApproveLlmEvaluationDelegate` - Auto-aprueba
- `ManualApproveLlmEvaluationDelegate` - HITL
- `RejectLlmEvaluationDelegate` - Rechaza
- `SaveLlmEvaluationResultsDelegate` - Guarda resultados
- `NotifyLlmEvaluationDelegate` - Notifica

---

### 9. RAG Evaluation (`rag-evaluation-v1.bpmn`)

**Propósito:** Evaluar sistemas RAG

**Flujo:**
1. Start → `ragSystemId`
2. Service Task → Execute RAG Evaluation
3. Service Task → Store Results
4. BusinessRuleTask → Drools
5. Gateway

**Delegates:**
- `RagEvaluationDelegate` - Evalúa RAG
- `StoreRagEvaluationDelegate` - Guarda resultados
- `CreateRagAlertDelegate` - Crea alerta

---

### 10. Model Evaluation (`model-evaluation-v1.bpmn`)

**Propósito:** Evaluar modelos ML regulares

**Flujo:**
1. Start
2. Service Task → Model Evaluation
3. BusinessRuleTask → Drools
4. Gateway → APPROVED | REJECTED

**Delegates:**
- `ModelEvaluationDelegate` - Evalúa modelo
- `StoreEvaluationDelegate` - Guarda resultados

---

## 🤖 PROCESOS DE AUTOMATIZACIÓN

### 11. Deployment Automation (`deployment-automation-v1.bpmn`)

**Propósito:** Automatizar deployment de modelos

**Flujo:**
1. Start → `modelId`, `environment`
2. Service Task → Check Auto Scaling
3. Service Task → Deploy Model
4. Gateway → SUCCESS | ROLLBACK
5. Service Task → Auto Scale Deployment

**Delegates:**
- `CheckAutoScalingDelegate` - Verifica auto-scaling
- `AutoScaleDeploymentDelegate` - Auto-escala deployment
- `RollbackModelDelegate` - Rollback en fallo

---

### 12. Incident Response RCA (`incident-response-rca-v1.bpmn`)

**Propósito:** Root Cause Analysis de incidentes

**Flujo:**
1. Start → `incidentId`
2. Service Task → AI Impact Assessment
3. Service Task → Generate Mitigations
4. Gateway
5. End

**Delegates:**
- `AIImpactAssessmentDelegate` - Evalúa impacto
- `GenerateMitigationRecommendationsDelegate` - Genera mitigaciones

---

## 🛡️ PROCESOS DE GOVERNANCE

### 13. Compliance Monitoring (`compliance-monitoring-v1.bpmn`)

**Propósito:** Monitoreo continuo de compliance

**Flujo:**
1. Timer Start (cada 24h)
2. Service Task → Scheduled Compliance Check
3. Service Task → Create Compliance Alerts
4. Gateway → COMPLIANT | NON_COMPLIANT
5. End

**Delegates:**
- `ScheduledComplianceDelegate` - Ejecuta check
- `CreateComplianceAlertsDelegate` - Crea alertas
- `ComplianceCheckDelegate` - Verifica compliance

---

### 14. Ethics Review (`ethics-review-v1.bpmn`)

**Propósito:** Revisión ética de agentes/modelos

**Flujo:**
1. Start
2. Service Task → AI Ethical Review
3. Service Task → Save Ethics Evidence
4. Gateway → APPROVED | REJECTED

**Delegates:**
- `AIEthicalReviewDelegate` - Revisa ética
- `SaveEthicsEvidenceDelegate` - Guarda evidencia
- `RejectEthicsDelegate` - Rechazo ético

---

### 15. Risk Assessment (`risk-assessment-v1.bpmn`)

**Propósito:** Evaluar riesgos de IA

**Flujo:**
1. Start
2. Service Task → Risk Assessment
3. Service Task → Schedule Review
4. User Task → Accept Residual Risk
5. End

**Delegates:**
- `RiskAssessmentDelegate` - Evalúa riesgos
- `ScheduleReviewDelegate` - Programa review
- `AcceptResidualRiskDelegate` - Acepta riesgo residual

---

### 16. Dataset Quality (`dataset-quality-v1.bpmn`)

**Propósito:** Asegurar calidad de datasets

**Flujo:**
1. Start
2. Service Task → Dataset Quality Check
3. Gateway → PASS | FAIL
4. End

---

### 17. Model Retraining Orchestration (`model-retraining-orchestration-v1.bpmn`)

**Propósito:** Orquestar re-entrenamiento de modelos

**Flujo:**
1. Start → `modelId`
2. Service Task → Trigger Retraining
3. Service Task → Validate Retrained Model
4. Gateway
5. End

---

## 📚 REFERENCIA TÉCNICA

### Java Delegates - Estructura

```java
@Slf4j
@Component("delegateName")
public class MyDelegate implements JavaDelegate {
    
    @Autowired
    private MyService service;
    
    @Override
    public void execute(DelegateExecution execution) {
        // 1. Get process variables
        Long entityId = (Long) execution.getVariable("entityId");
        
        // 2. Business logic
        MyResult result = service.doWork(entityId);
        
        // 3. Set output variables
        execution.setVariable("result", result);
        
        // 4. Log
        log.info("Delegate executed for entityId: {}", entityId);
    }
}
```

### Fact Objects para Drools

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyApprovalFact implements Serializable {
    private Integer score1;
    private Integer score2;
    private Boolean compliant;
    private String decision;
    private Double confidenceLevel;
    private String justification;
}
```

### Reglas Drools - Estructura

```drl
package com.codeflowx.govern.workflow.drools.myentity;

import com.codeflowx.govern.workflow.drools.facts.MyApprovalFact;
global org.slf4j.Logger logger;

rule "My Rule Name"
    salience 100
when
    $fact : MyApprovalFact(
        score1 >= 80,
        compliant == true
    )
then
    $fact.setDecision("APPROVED");
    logger.info("Fact approved");
end
```

### Integración con Services

```java
// En el Delegate
@Autowired
private MyBusinessService service;

// Llamada
MyEntity entity = service.findById(entityId);
entity.setStatus("PROCESSING");
service.save(entity);
```

### Integración con Python ML Services

```java
// En el Delegate
@Autowired
@Qualifier("pythonMlClient")
private RestTemplate restTemplate;

String url = "http://leka-server-serving-evaluation:8000/evaluate";
EvaluationResult result = restTemplate.postForObject(url, request, EvaluationResult.class);
```

### Manejo de Errores

```java
@Slf4j
@Component("myDelegate")
public class MyDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) {
        try {
            // Logic
        } catch (Exception e) {
            log.error("Error in delegate", e);
            // Flowable lanzará el error automáticamente
            // Será capturado por Error Boundary Event
            throw new BpmnError("DELEGATE_ERROR", e.getMessage());
        }
    }
}
```

### Variables de Proceso

| Tipo | Java | JSON |
|------|------|------|
| String | `String` | `"text"` |
| Integer | `Integer` | `100` |
| Boolean | `Boolean` | `true` |
| Long | `Long` | `123456` |
| Double | `Double` | `85.5` |
| Date | `Date` | ISO 8601 |

---

## 🎯 BUENAS PRÁCTICAS

### 1. Nombres de Procesos

✅ **Bien:**
- `agent-approval-v1.bpmn`
- `bias-detection-v2.bpmn`

❌ **Mal:**
- `agent_approval.bpmn` (no usar snake_case)
- `AgentApproval.bpmn` (no usar PascalCase)

### 2. Java Delegates

✅ **Bien:**
```java
@Component("autoApproveAgentDelegate")
public class AutoApproveAgentDelegate implements JavaDelegate
```

❌ **Mal:**
```java
public class ApproveDelegate  // Sin @Component
public class AgentDelegate    // Nombre genérico
```

### 3. Logging

✅ **Bien:**
```java
log.info("Processing agent approval for agentId: {}", agentId);
log.error("Failed to process approval", exception);
```

❌ **Mal:**
```java
System.out.println("Processing");
log.debug("Processing");  // No debug en producción
```

### 4. Manejo de Errores

✅ **Bien:**
```java
try {
    result = service.doWork();
} catch (BusinessException e) {
    log.error("Business error", e);
    throw new BpmnError("BUSINESS_ERROR", e.getMessage());
}
```

### 5. Testabilidad

✅ **Bien:**
```java
@Component("myDelegate")
public class MyDelegate implements JavaDelegate {
    
    @Autowired
    private MyService service;  // Inyectado, fácil de mockear
    
    @Override
    public void execute(DelegateExecution execution) {
        // Lógica simple
    }
}
```

---

## 🧪 TESTING

### Unit Tests para Delegates

```java
@SpringBootTest
class MyDelegateTest {
    
    @Mock
    private MyService service;
    
    @InjectMocks
    private MyDelegate delegate;
    
    @Mock
    private DelegateExecution execution;
    
    @Test
    void testDelegateSuccess() {
        // Given
        when(execution.getVariable("entityId")).thenReturn(1L);
        when(service.doWork(1L)).thenReturn(new Result());
        
        // When
        delegate.execute(execution);
        
        // Then
        verify(execution).setVariable(eq("result"), any());
    }
}
```

---

## 📞 CONTACTO

Para dudas sobre implementación:
- Ver código en: `src/main/java/com/codeflowx/govern/workflow/delegates/`
- Ver procesos en: `src/main/resources/processes/`
- Ver reglas en: `src/main/resources/rules/`

**FIN DEL MANUAL**

