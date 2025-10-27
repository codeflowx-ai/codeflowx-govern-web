# Patrón de Documentación JavaDoc - ViewModels Workflow

## Estructura Obligatoria

Cada ViewModel del workflow DEBE tener un JavaDoc completo con esta estructura:

```java
/**
 * ViewModel: [Nombre Descriptivo]
 * 
 * BPMN Process: [process-key-v1]
 * User Task: [taskId del BPMN]
 * Candidate Groups: [grupos que pueden reclamar la tarea]
 * 
 * Funcionalidad:
 * - [Descripción punto 1]
 * - [Descripción punto 2]
 * - [Descripción punto 3]
 * 
 * Input Variables (desde proceso BPMN):
 * - variable1: tipo - descripción
 * - variable2: tipo - descripción
 * 
 * Output Variables (al completar task):
 * - outputVar1: tipo - descripción
 * - outputVar2: tipo - descripción
 * 
 * Modo MOCK:
 * - URL: /workflow/[pantalla].zul?taskId=[id]&mock=true
 * - Datos simulados: [descripción breve]
 */
```

---

## Ejemplos Completos por Tipo

### Agent Approval

```java
/**
 * ViewModel: Agent Approval - Human Override Review
 * 
 * BPMN Process: agent-approval-v1
 * User Task: hitlTask
 * Candidate Groups: governance-admins
 * 
 * Funcionalidad:
 * - Mostrar scores de evaluaciones (risk, compliance, ethics)
 * - Mostrar decisión recomendada de Drools
 * - Permitir al usuario aprobar o rechazar manualmente
 * - Completar User Task con decisión final
 * - Registrar justificación de override si difiere de Drools
 * 
 * Input Variables (desde proceso BPMN):
 * - agentId: Long - ID del agente en evaluación
 * - agentName: String - Nombre del agente
 * - riskScore: Integer - Score de riesgo (0-100)
 * - complianceScore: Integer - Score de compliance (0-100)
 * - ethicsScore: Integer - Score ético (0-100)
 * - minScore: Integer - Score mínimo requerido
 * - confidenceLevel: Double - Nivel de confianza de Drools
 * - justification: String - Justificación de Drools
 * - droolsDecision: String - Decisión recomendada ('approve'/'reject')
 * 
 * Output Variables (al completar task):
 * - human_decision: String - 'approve' o 'reject'
 * - human_notes: String - Notas/justificación del revisor
 * - override_reason: String - Razón si difiere de Drools (opcional)
 * - reviewed_by: String - Username del revisor
 * - reviewed_at: Timestamp - Fecha/hora de la revisión
 * 
 * Modo MOCK:
 * - URL: /workflow/agent-approval-override.zul?taskId=mock-1&mock=true
 * - Datos simulados: Agente bancario con scores 85/92/88, decisión pendiente
 */
```

### Bias Review

```java
/**
 * ViewModel: Bias Detection - Human Review
 * 
 * BPMN Process: bias-detection-v1
 * User Task: reviewBiasTask
 * Candidate Groups: data-scientists, governance-analysts
 * 
 * Funcionalidad:
 * - Mostrar detalles del sesgo detectado por Drools
 * - Visualizar grupos afectados y métricas de impacto
 * - Permitir clasificar severidad (LOW, MEDIUM, HIGH, CRITICAL)
 * - Decidir acción: APPROVE (aceptar riesgo), MITIGATE (crear plan), REJECT (bloquear)
 * - Registrar análisis y justificación
 * 
 * Input Variables:
 * - modelId: Long - ID del modelo evaluado
 * - modelName: String - Nombre del modelo
 * - biasType: String - Tipo de sesgo (DEMOGRAPHIC, SELECTION, etc.)
 * - affectedGroups: List<String> - Grupos demográficos afectados
 * - biasScore: Double - Puntuación del sesgo (0-100)
 * - severityLevel: String - Severidad detectada
 * - confidenceScore: Double - Confianza de la detección
 * 
 * Output Variables:
 * - bias_decision: String - 'approve', 'mitigate', 'reject'
 * - bias_severity_override: String - Severidad final (puede diferir)
 * - mitigation_required: Boolean - Si requiere plan de mitigación
 * - review_notes: String - Análisis del revisor
 * - reviewed_by: String - Username
 * - reviewed_at: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/bias-review.zul?taskId=mock-4&mock=true
 * - Datos simulados: Sesgo demográfico en modelo crédito, género y edad afectados
 */
```

### Prompt Safety Review

```java
/**
 * ViewModel: Prompt Approval - Human Safety Review
 * 
 * BPMN Process: prompt-approval-process
 * User Task: humanReviewTask
 * Candidate Groups: prompt-engineers, security-reviewers
 * 
 * Funcionalidad:
 * - Mostrar contenido del prompt a revisar
 * - Visualizar resultados de análisis de seguridad automático
 * - Mostrar scores de safety y compliance
 * - Indicar si se detectó jailbreak, injection o contenido malicioso
 * - Permitir aprobar o rechazar con justificación
 * 
 * Input Variables:
 * - promptId: Long - ID del prompt
 * - approvalId: Long - ID del registro de aprobación
 * - promptContent: String - Contenido completo del prompt
 * - safetyScore: Integer - Score de seguridad (0-100)
 * - complianceScore: Integer - Score de compliance (0-100)
 * - jailbreakDetected: Boolean
 * - injectionDetected: Boolean
 * - maliciousContentDetected: Boolean
 * - safetyCheckResult: String (JSON) - Resultado detallado del análisis
 * - complianceCheckResult: String (JSON)
 * 
 * Output Variables:
 * - approved: Boolean - Decisión final
 * - approvalNotes: String - Notas del revisor
 * - rejectionReason: String - Razón de rechazo (si aplica)
 * - reviewerId: String - Username del revisor
 * - reviewerName: String - Nombre completo
 * - reviewedAt: Long - Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/prompt-human-review.zul?taskId=mock-23&mock=true
 * - Datos simulados: Prompt marketing con safety 95/100, sin riesgos críticos
 */
```

### Ethics Committee Review

```java
/**
 * ViewModel: Ethics Committee Review
 * 
 * BPMN Process: ethics-review-v1
 * User Task: committeeReviewTask
 * Candidate Groups: ethics-committee
 * 
 * Funcionalidad:
 * - Presentar sistema de IA para revisión ética
 * - Mostrar análisis automático de impacto ético
 * - Evaluar riesgos éticos identificados
 * - Decidir: APPROVE (aprobar), REQUEST_CHANGES (solicitar cambios), REJECT (rechazar)
 * - Registrar dictamen del comité con justificación detallada
 * 
 * Input Variables:
 * - systemId: Long - ID del sistema a revisar
 * - systemName: String - Nombre del sistema
 * - systemType: String - Tipo (CONVERSATIONAL, PREDICTIVE, GENERATIVE, etc.)
 * - impactLevel: String - Nivel de impacto (LOW, MEDIUM, HIGH, CRITICAL)
 * - estimatedUsers: String - Usuarios afectados estimados
 * - ethicalRisks: List<String> - Riesgos éticos identificados
 * - autoAnalysisResult: String (JSON) - Resultado del análisis automático
 * 
 * Output Variables:
 * - committee_decision: String - 'approve', 'request_changes', 'reject'
 * - ethical_assessment: String - Evaluación detallada
 * - mitigation_recommendations: List<String> - Recomendaciones
 * - follow_up_required: Boolean - Si requiere seguimiento
 * - committee_notes: String - Notas del comité
 * - reviewed_by_committee: String - Miembros del comité
 * - review_date: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/ethics-committee-review.zul?taskId=mock-11&mock=true
 * - Datos simulados: Chatbot médico, impacto 10K pacientes, riesgos privacidad
 */
```

### Performance Intervention

```java
/**
 * ViewModel: Performance Degradation - Immediate Intervention
 * 
 * BPMN Process: performance-degradation-v1
 * User Task: performanceInterventionTask
 * Candidate Groups: devops-engineers, ml-engineers
 * 
 * Funcionalidad:
 * - Mostrar métricas de performance degradadas
 * - Comparar baseline vs actual
 * - Mostrar % de degradación y severidad
 * - Permitir tomar acción correctiva inmediata
 * - Decidir: RESTART, ROLLBACK, SCALE_UP, INVESTIGATE
 * 
 * Input Variables:
 * - endpointName: String - Nombre del endpoint afectado
 * - metricType: String - Tipo de métrica (LATENCY, THROUGHPUT, ERROR_RATE)
 * - baselineValue: Double - Valor baseline
 * - currentValue: Double - Valor actual
 * - degradationPercent: Double - % de degradación
 * - criticalityLevel: String - CRITICAL, HIGH, MEDIUM
 * - affectedRequests: Long - Requests impactados
 * 
 * Output Variables:
 * - intervention_action: String - Acción tomada
 * - action_details: String - Detalles de la intervención
 * - estimated_recovery_time: Integer - Tiempo estimado (minutos)
 * - requires_followup: Boolean
 * - intervened_by: String - Username
 * - intervention_time: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/performance-intervention.zul?taskId=mock-20&mock=true
 * - Datos simulados: API inference 300ms→1200ms, degradación +300%, crítico
 */
```

---

## Patrón Completo de Documentación

### Secciones Obligatorias:

1. **Título:** `ViewModel: [Nombre Descriptivo]`
2. **BPMN Process:** Key del proceso
3. **User Task:** ID del task en el BPMN
4. **Candidate Groups:** Roles/grupos que pueden reclamar
5. **Funcionalidad:** Lista de características (3-5 puntos)
6. **Input Variables:** Variables que recibe del proceso
7. **Output Variables:** Variables que envía al completar
8. **Modo MOCK:** URL y descripción de datos simulados

### Formato:

```java
/**
 * ViewModel: [Título Descriptivo del Propósito]
 * 
 * BPMN Process: [process-definition-key]
 * User Task: [task-id-in-bpmn]
 * Candidate Groups: [role1, role2, role3]
 * 
 * Funcionalidad:
 * - [Punto 1: Qué muestra]
 * - [Punto 2: Qué permite hacer]
 * - [Punto 3: Qué decide]
 * - [Punto 4: Qué registra/guarda]
 * - [Punto 5: Integraciones (opcional)]
 * 
 * Input Variables (desde proceso BPMN):
 * - variable1: tipo - descripción clara
 * - variable2: tipo - descripción clara
 * - variable3: tipo - descripción clara
 * 
 * Output Variables (al completar task):
 * - output1: tipo - descripción clara
 * - output2: tipo - descripción clara
 * 
 * Modo MOCK:
 * - URL: /workflow/[pantalla].zul?taskId=mock-N&mock=true
 * - Datos simulados: [descripción de qué datos se muestran en demo]
 */
```

---

## Lista de Documentación Necesaria

### ✅ Ya Documentado:
1. ✅ AgentApprovalHumanOverrideViewModel - COMPLETO

### ❌ Por Documentar (23 archivos):

1. ❌ AlertResponseViewModel
2. ❌ BiasMitigationPlanViewModel
3. ❌ BiasReviewViewModel
4. ❌ BiasUrgentDecisionViewModel
5. ❌ ComplianceReviewDecisionViewModel
6. ❌ ComplianceReviewViewModel
7. ❌ DatasetReviewReminderViewModel
8. ❌ DriftAnalysisViewModel
9. ❌ DriftReviewDecisionViewModel
10. ❌ EthicsCommitteeReviewViewModel
11. ❌ EthicsMitigationPlanViewModel
12. ❌ EthicsReviewReminderViewModel
13. ❌ EthicsReviewRequestViewModel
14. ❌ HitlSlaReminderViewModel
15. ❌ LlmEvaluationReviewViewModel
16. ❌ ModelApprovalHumanOverrideViewModel
17. ❌ ModelApprovalReminderViewModel
18. ❌ ModelEvaluationReviewViewModel
19. ❌ PerformanceInterventionViewModel
20. ❌ PerformanceReviewDecisionViewModel
21. ❌ PromptApprovalRequestViewModel (inicio de proceso)
22. ❌ PromptHumanReviewViewModel
23. ❌ RagEvaluationReviewViewModel
24. ❌ TaskInboxViewModel (bandeja)

---

## Checklist de Calidad para Documentación

Cada JavaDoc debe responder:

- [ ] ¿Qué proceso BPMN ejecuta?
- [ ] ¿En qué tarea del proceso se ubica?
- [ ] ¿Quién puede acceder a esta pantalla?
- [ ] ¿Qué funcionalidad ofrece? (3-5 puntos)
- [ ] ¿Qué datos recibe del proceso?
- [ ] ¿Qué datos envía al completar?
- [ ] ¿Cómo se accede en modo MOCK?
- [ ] ¿Qué datos simulados muestra?

---

## Beneficios de la Documentación Completa

1. **Desarrollo:**
   - Los desarrolladores entienden el propósito sin leer código
   - Claridad en variables de entrada/salida
   - Facilita debugging de workflows

2. **Testing:**
   - QA sabe qué probar en cada pantalla
   - Casos de prueba claros
   - URLs MOCK documentadas

3. **Demos:**
   - URLs directas para presentaciones
   - Datos de ejemplo documentados
   - Flujos completos trazables

4. **Mantenimiento:**
   - Documentación actualizada en el código
   - Mapeo claro BPMN ↔ ViewModel
   - Onboarding de nuevos desarrolladores

---

**Archivo de referencia:** `AgentApprovalHumanOverrideViewModel.java` (líneas 36-57)

