# 📘 CODEFLOWX - DOCUMENTACIÓN TÉCNICA DE PROCESOS BPMN

**Versión:** 1.0  
**Fecha:** Octubre 2025  
**Audiencia:** Desarrolladores, Analistas de Sistemas, Arquitectos  
**Proyecto:** CodeflowX AI Governance Platform

---

## 📑 TABLA DE CONTENIDOS

1. [Arquitectura General](#arquitectura-general)
2. [Procesos de Aprobación](#procesos-de-aprobacion)
3. [Procesos de Detección](#procesos-de-deteccion)
4. [Procesos de Evaluación](#procesos-de-evaluacion)
5. [Procesos de Governance](#procesos-de-governance)
6. [Procesos de Automatización](#procesos-de-automatizacion)
7. [Anexos Técnicos](#anexos-tecnicos)

---

## 🏗️ ARQUITECTURA GENERAL

### Stack Tecnológico

```
┌─────────────────────────────────────────────────────┐
│  CAPA DE PRESENTACIÓN                               │
│  • ZKoss (ZUL + MVVM)                              │
│  • Bandeja de Tareas Unificada                     │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE ORQUESTACIÓN                               │
│  • Flowable BPMN Engine                            │
│  • 17 Procesos BPMN 2.0 Avanzados                  │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE REGLAS DE NEGOCIO                          │
│  • Drools Rules Engine                             │
│  • 11 Fact Objects + 11 DRL (120+ reglas)          │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE SERVICIOS                                  │
│  • Spring Boot Microservices                       │
│  • JavaDelegates (80+ delegates)                   │
│  • Integration con Python ML Services              │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE PERSISTENCIA                               │
│  • PostgreSQL (JPA Entities)                       │
│  • MinIO (Object Storage)                          │
│  • Qdrant (Vector DB)                              │
└─────────────────────────────────────────────────────┘
```

### Elementos BPMN Avanzados Utilizados

| Elemento | Uso | Beneficio |
|----------|-----|-----------|
| **ParallelGateway** | Ejecución paralela de validaciones | 2-3x más rápido |
| **BusinessRuleTask** | Integración nativa con Drools | Reglas modificables sin redeploy |
| **Timer Boundary Event** | SLAs automáticos con recordatorios | Cumplimiento garantizado |
| **Error Boundary Event** | Resiliencia ante fallos externos | Alta disponibilidad |
| **Message Start Event** | Comunicación inter-proceso | Event-driven architecture |
| **MailTask** | Notificaciones automáticas | Sin código custom |

---

## 📋 PROCESOS DE APROBACIÓN

### 1. AGENT-APPROVAL-V1

**ID Proceso:** `agent-approval-v1`  
**Proporción:** 80% Automatizado - 20% HITL

#### Descripción Funcional:

**Finalidad:**  
Aprobar o rechazar agents de IA (chatbots, asistentes virtuales, agents autónomos) antes de su despliegue en producción, garantizando que cumplen con criterios de riesgo, compliance y ética.

**Objetivo:**  
Automatizar la evaluación de agents IA mediante validaciones paralelas y un motor de reglas inteligente, reduciendo el tiempo de aprobación de semanas a minutos, mientras se mantiene validación humana en casos críticos o borderline.

**¿Cuándo se usa?**  
- Cuando un ML Engineer desarrolla un nuevo agent IA
- Cuando se actualiza significativamente un agent existente
- Cuando se cambia el propósito o ámbito de uso de un agent
- Antes de desplegar a producción o staging

**Resultado Esperado:**  
- **80% casos:** Aprobación automática en 10-15 minutos
- **15% casos:** Revisión humana por Governance Admin (24-48h)
- **5% casos:** Rechazo automático por criterios críticos incumplidos

**Criterios de Evaluación:**
1. Risk Score (0-100): Debe ser ≤70 para auto-aprobación
2. Compliance Score (0-100): Debe ser ≥60 para aprobación
3. Ethics Score (0-100): Debe ser ≥40 (crítico)
4. GDPR Compliance: Obligatorio
5. Agent Capabilities: Evaluadas por riesgo potencial

#### Cómo se lanza:
```java
// Opción 1: Desde código Java
Map<String, Object> variables = new HashMap<>();
variables.put("agentId", "agent-123");
variables.put("agentName", "Customer Support Bot");
variables.put("agentType", "CHATBOT");
runtimeService.startProcessInstanceByKey("agent-approval-v1", variables);

// Opción 2: Desde menú ZKoss (Ssomenuitem)
// Configurar menuitem con KEYPROCESS = 'agent-approval-v1'
// y FORMKEY = 'console/bpmn/agent-approval-request-form.zul'

// Opción 3: Desde API REST
POST /api/workflow/start
{
  "processKey": "agent-approval-v1",
  "variables": { "agentId": "agent-123", ... }
}
```

#### Flujo Detallado:

**FASE 1: Validaciones Paralelas (ParallelGateway)**
```
┌─────────────────┐
│ Start           │
└────────┬────────┘
         ↓
┌─────────────────┐
│ ParallelGateway │──┬──→ calculateRiskScore (ServiceTask)
└─────────────────┘  │       └─ Delegate: CalculateRiskScoreDelegate
                     │       └─ Python: risk_assessment.py
                     │
                     ├──→ checkCompliance (ServiceTask)
                     │       └─ Delegate: CheckComplianceDelegate
                     │       └─ Valida: GDPR, EU AI Act
                     │
                     └──→ checkEthics (ServiceTask)
                             └─ Delegate: CheckEthicsDelegate
                             └─ Análisis ético básico
         ↓ (Merge)
```

**FASE 2: Scoring con Drools**
```
┌────────────────────────────────────┐
│ calculateDroolsScore               │
│ (BusinessRuleTask)                 │
│                                    │
│ Input:  ${agentApprovalFact}      │
│ Output: ${agentScoringResult}     │
│ Rules:  agent-scoring.drl (9)     │
└────────────────────────────────────┘
```

**FASE 3: Decisión Automática**
```
ExclusiveGateway → Decision:
├─ scoringResult.decision == 'AUTO_APPROVE' 
│  └→ notifyApproval (MailTask) → END
│
├─ scoringResult.decision == 'HITL_REQUIRED'
│  └→ humanOverride (UserTask)
│     ├─ Assignee: ${governanceAdminId}
│     ├─ FormKey: agent-approval-human-override-form
│     ├─ Timer Boundary: 24h
│     │  └→ hitlSlaReminder (UserTask) si no responde
│     └→ Decision → Approve/Reject
│
└─ scoringResult.decision == 'AUTO_REJECT'
   └→ notifyRejection (MailTask) → END
```

#### Responsabilidades por Tarea:

| Tarea | Tipo | Responsable | Descripción | Datos Requeridos |
|-------|------|-------------|-------------|------------------|
| **calculateRiskScore** | ServiceTask | Sistema (Python) | Calcula risk score del agent | agentId, agentType, agentCapabilities |
| **checkCompliance** | ServiceTask | Sistema | Valida cumplimiento regulatorio | agentId, targetRegion, dataProcessing |
| **checkEthics** | ServiceTask | Sistema | Evaluación ética básica | agentId, ethicalConsiderations |
| **calculateDroolsScore** | BusinessRuleTask | Drools Engine | Scoring con 9 reglas | agentApprovalFact (completo) |
| **humanOverride** | UserTask | **Governance Admin** | Decisión humana para casos borderline | scoringResult, riskAssessment |
| **hitlSlaReminder** | UserTask | **Governance Leads** | Recordatorio si SLA excedido (24h) | taskId, daysOverdue |
| **notifyApproval** | MailTask | Sistema | Email automático de aprobación | agentId, approverEmail |
| **notifyRejection** | MailTask | Sistema | Email automático de rechazo | agentId, rejectionReason |

#### Variables de Proceso:

**Input:**
- `agentId` (String): ID único del agent
- `agentName` (String): Nombre del agent
- `agentType` (String): Tipo (CHATBOT, ASSISTANT, etc.)
- `riskScore` (Double): Score de riesgo inicial
- `initiatorId` (String): Usuario que inicia el proceso

**Output:**
- `approved` (Boolean): Si fue aprobado
- `approvalMethod` (String): AUTO / MANUAL
- `finalScore` (Integer): Score final Drools (0-100)
- `decision` (String): AUTO_APPROVE / HITL_REQUIRED / AUTO_REJECT

#### Drools Rules Aplicadas (agent-scoring.drl):

1. **Rule: "Low Risk Auto-Approve"** - Score ≥80 + Risk ≤30 → AUTO_APPROVE
2. **Rule: "High Risk Auto-Reject"** - Risk >70 || Ethics <40 → AUTO_REJECT
3. **Rule: "Medium Risk HITL"** - Casos intermedios → HITL_REQUIRED
4. **Rule: "Critical Ethics Issue"** - Ethics <30 → AUTO_REJECT + Alert
5. **Rule: "Compliance Fail"** - !GDPR compliant → AUTO_REJECT
6. **Rule: "High Performance Agent"** - Score >90 + Low risk → AUTO_APPROVE + Fast-track
7. **Rule: "Borderline Case"** - 60 ≤ Score < 80 → HITL_REQUIRED
8. **Rule: "Calculate Final Score"** - Weighted average de métricas
9. **Rule: "Set Approval Groups"** - Define quién debe aprobar según riesgo

---

### 2. MODEL-APPROVAL-V1

**ID Proceso:** `model-approval-v1`  
**Proporción:** 60% Automatizado - 40% HITL

#### Descripción Funcional:

**Finalidad:**  
Aprobar modelos de Machine Learning para despliegue en producción, garantizando calidad técnica, ausencia de sesgo crítico, y cumplimiento regulatorio mediante un proceso de revisión multi-nivel.

**Objetivo:**  
Combinar validaciones técnicas automatizadas con revisiones humanas especializadas (ML Engineer + Governance Admin) para garantizar que solo modelos de alta calidad y compliance lleguen a producción.

**¿Cuándo se usa?**  
- Antes de desplegar un nuevo modelo a producción
- Cuando se actualiza significativamente un modelo existente
- Cuando cambian los datos de entrenamiento o el propósito del modelo
- Después de un reentrenamiento importante

**Resultado Esperado:**  
- **40% casos:** Aprobación completa en 5-7 días (validaciones paralelas)
- **40% casos:** Aprobación condicional con monitoreo adicional
- **20% casos:** Rechazo por criterios técnicos o compliance

**Criterios de Evaluación:**
1. Performance Metrics: Accuracy, Precision, Recall, F1 (thresholds específicos por tipo)
2. Bias Score: Debe ser <0.3 (máximo aceptable)
3. Compliance Score: GDPR, data lineage, consent tracking
4. ML Engineer Review: Validación técnica profunda
5. Governance Review: Validación de compliance y riesgos
6. Drools Final Decision: 12 reglas consolidan todas las validaciones

**Tipos de Aprobación:**
- **APPROVED:** Listo para producción sin restricciones
- **CONDITIONAL_APPROVAL:** Aprobado con monitorización adicional obligatoria
- **REJECTED:** No cumple criterios mínimos

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("modelId", "model-456");
variables.put("modelType", "CLASSIFICATION");
variables.put("targetEnvironment", "PRODUCTION");
runtimeService.startProcessInstanceByKey("model-approval-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Submit Request (User Task)**
```
submitModelApproval (UserTask)
├─ CandidateGroups: ml-engineers
├─ FormKey: model-approval-request-form
└─ Input: modelId, modelMetrics, trainingData, targetEnv
```

**FASE 2: Validaciones Paralelas**
```
ParallelGateway:
├─ validatePerformance → Métricas técnicas (accuracy, F1, etc.)
├─ validateBias → Sesgo demográfico, geográfico
└─ validateCompliance → GDPR, EU AI Act, data lineage
```

**FASE 3: Reviews Humanas Secuenciales**
```
mlEngineerReview (UserTask)
├─ Assignee: ${seniorMlEngineerId}
├─ Timer SLA: 3 días
└─ Decision: APPROVE / REJECT / REQUEST_CHANGES
         ↓
governanceReview (UserTask)
├─ CandidateGroups: governance-admins
├─ Timer SLA: 3 días
│  └─ Timer Boundary → governanceSlaReminder
└─ Decision: APPROVE / CONDITIONAL / REJECT
```

**FASE 4: Scoring Final con Drools**
```
BusinessRuleTask: modelApprovalScoring
├─ Input: modelApprovalFact (12 reglas)
├─ Output: APPROVED / CONDITIONAL_APPROVAL / REJECTED
└─ Si CONDITIONAL → Mark model for monitoring
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | SLA |
|-------|-------------|-------------|-----|
| **submitModelApproval** | ML Engineer | Envía modelo para aprobación | - |
| **validatePerformance** | Sistema | Valida métricas técnicas | 5 min |
| **validateBias** | Sistema (Python) | Detecta sesgos | 10 min |
| **validateCompliance** | Sistema | Valida cumplimiento | 5 min |
| **mlEngineerReview** | Senior ML Engineer | Revisión técnica profunda | 3 días |
| **governanceReview** | Governance Admin | Revisión de compliance | 3 días |
| **modelApprovalScoring** | Drools Engine | Decisión final automatizada | 1 min |
| **markModelConditional** | Sistema | Marca modelo para monitoreo | 1 min |
| **notifyApproval** | Sistema (MailTask) | Notifica aprobación | Inmediato |

#### Variables de Proceso:

**Input:**
- `modelId`, `modelType`, `modelVersion`
- `accuracy`, `precision`, `recall`, `f1Score`
- `biasScore`, `complianceScore`
- `targetEnvironment` (STAGING/PRODUCTION)

**Output:**
- `finalDecision` (APPROVED/CONDITIONAL_APPROVAL/REJECTED)
- `requiresMonitoring` (Boolean)
- `approvalTimestamp` (LocalDateTime)

---

### 3. PROMPT-APPROVAL-V1

**ID Proceso:** `prompt-approval-v1`  
**Proporción:** 70% Automatizado - 30% HITL

#### Descripción Funcional:

**Finalidad:**  
Aprobar prompts para modelos LLM antes de su uso en producción, garantizando seguridad (ausencia de toxicity, PII), calidad, y cumplimiento de políticas de uso de datos.

**Objetivo:**  
Validar automáticamente prompts mediante checks paralelos de seguridad y compliance, aprobando el 70% de forma automática y escalando solo casos borderline a revisión humana.

**¿Cuándo se usa?**  
- Antes de usar un nuevo prompt en producción
- Al modificar prompts existentes
- Al cambiar el contexto o propósito de un prompt
- Para validación de prompt templates

**Resultado Esperado:**  
- **70% casos:** Auto-aprobación si safety + compliance OK
- **30% casos:** Review humano para casos borderline
- **Tiempo:** <10 minutos para auto-aprobación, 24h para review humano

**Criterios de Evaluación:**
1. Toxicity Score: Debe ser <0.2
2. PII Detection: No debe contener información personal
3. Jailbreak Attempts: No debe intentar bypasses
4. GDPR Compliance: Uso de datos debe ser conforme
5. Quality Score: Claridad, precisión, propósito bien definido

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("promptId", "prompt-789");
variables.put("promptText", "Analyze customer sentiment...");
variables.put("targetLLM", "gpt-4");
runtimeService.startProcessInstanceByKey("prompt-approval-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Request (User Task)**
```
requestApproval (UserTask)
├─ CandidateGroups: ml-engineers
└─ Input: promptText, targetLLM, purpose
```

**FASE 2: Validaciones Paralelas**
```
ParallelGateway:
├─ safetyCheck → Toxicity, PII, jailbreak attempts
└─ complianceCheck → GDPR, data usage, retention
```

**FASE 3: Auto-Approve o Review**
```
ExclusiveGateway:
├─ autoApproveEligible == true → autoApprove → END
└─ autoApproveEligible == false → humanReview → Decision
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Criterios Auto-Approve |
|-------|-------------|-------------|------------------------|
| **requestApproval** | ML Engineer | Solicita aprobación de prompt | - |
| **safetyCheck** | Sistema (Python) | Detecta toxicity, PII, bias | toxicityScore <0.2 |
| **complianceCheck** | Sistema | Valida cumplimiento | GDPR compliant |
| **autoApprove** | Sistema | Aprobación automática | Safety OK + Compliance OK |
| **humanReview** | Prompt Engineer | Revisión manual | Casos borderline |

---

## 🔍 PROCESOS DE DETECCIÓN

### 4. BIAS-DETECTION-V1

**ID Proceso:** `bias-detection-v1`  
**Proporción:** 70% Automatizado - 30% HITL

#### Descripción Funcional:

**Finalidad:**  
Detectar y analizar sesgos (bias) en modelos de IA para garantizar equidad y justicia en las predicciones, identificando disparidades demográficas, geográficas y temporales.

**Objetivo:**  
Ejecutar análisis automático multi-dimensional de sesgo, clasificar la severidad, y generar recomendaciones de mitigación, requiriendo intervención humana solo cuando se detecta sesgo significativo.

**¿Cuándo se usa?**  
- Automáticamente después de entrenar un modelo (integrado en pipeline)
- Periódicamente para modelos en producción (cada 7-30 días)
- Cuando se detectan quejas de usuarios sobre equidad
- Antes de model-approval (como validación previa)
- Bajo demanda para auditorías

**Resultado Esperado:**  
- **70% casos:** No bias detectado → Documentación automática
- **20% casos:** Bias leve-moderado → Mitigación con review humano
- **10% casos:** Bias crítico → Rechazo o mitigación obligatoria

**Tipos de Sesgo Detectados:**
1. **Demographic Bias:** Disparidad por género, edad, etnia, religión
2. **Geographic Bias:** Disparidad por región, país, zona urbana/rural
3. **Temporal Bias:** Cambios de comportamiento en el tiempo

**Criterios de Evaluación:**
- Bias Score global: <0.2 (aceptable), 0.2-0.4 (review), 0.4-0.6 (mitigación), >0.6 (crítico)
- Demographic Parity: Diferencia <10% entre grupos
- Equalized Odds: True positive rate similar entre grupos
- Statistical Significance: p-value <0.05

**Acciones Posibles:**
- **NO_BIAS:** Aprobar y documentar
- **MITIGATE:** Implementar técnicas de mitigación (re-weighting, re-sampling)
- **REJECT:** Rechazar modelo por sesgo inaceptable
- **ACCEPT_RISK:** Documentar y aceptar riesgo residual (con justificación)

#### Cómo se lanza:
```java
// Opción 1: Manual
runtimeService.startProcessInstanceByKey("bias-detection-v1", variables);

// Opción 2: Trigger automático desde model-approval-v1
// Opción 3: Scheduled (cron job cada 7 días)
```

#### Flujo Detallado:

**FASE 1: Preparación y Detección**
```
prepareDemographicData → Prepara datos demográficos
         ↓
executeBiasDetection → Ejecuta análisis en Python
    (ServiceTask)       ├─ Demographic bias
                        ├─ Geographic bias
                        └─ Temporal bias
```

**FASE 2: Scoring con Drools**
```
BusinessRuleTask: biasScoring
├─ Input: biasDetectionFact
├─ Rules: bias-detection-scoring.drl (13 reglas)
└─ Output: NO_BIAS / BIAS_DETECTED / CRITICAL_BIAS
```

**FASE 3: Decisión Humana (si bias detected)**
```
reviewDetectedBiases (UserTask)
├─ CandidateGroups: ml-engineers, data-scientists
├─ Timer Boundary: 24h
│  └─ urgentBiasDecision (si timeout)
└─ Decision: MITIGATE / REJECT / ACCEPT_RISK
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | SLA |
|-------|-------------|-------------|-----|
| **prepareDemographicData** | Sistema | Prepara datos para análisis | 5 min |
| **executeBiasDetection** | Sistema (Python) | Detecta 3 tipos de sesgo | 15 min |
| **biasScoring** | Drools | Clasifica severidad | 1 min |
| **reviewDetectedBiases** | ML Engineer + Data Scientist | Decide acción de mitigación | 24h |
| **urgentBiasDecision** | ML Leads | Decisión urgente si timeout | Inmediato |
| **generateMitigationRecommendations** | Sistema | Genera recomendaciones auto | 2 min |
| **acceptResidualRisk** | Sistema | Documenta riesgo aceptado | 1 min |
| **rejectBiasedModel** | Sistema | Marca modelo como rechazado | 1 min |

#### Drools Rules (bias-detection-scoring.drl):

1. **No Bias Detected** - Todos los scores <0.2 → AUTO_APPROVE
2. **Low Bias** - 0.2 ≤ bias <0.4 → REVIEW_REQUIRED
3. **Moderate Bias** - 0.4 ≤ bias <0.6 → MITIGATION_REQUIRED
4. **High Bias** - 0.6 ≤ bias <0.8 → CRITICAL_REVIEW
5. **Critical Bias** - bias ≥0.8 → AUTO_REJECT
6. **Demographic Bias Critical** - Disparity >40% → CRITICAL
7. **Geographic Bias** - Regional disparity >30% → REVIEW
8. **Temporal Bias** - Time-based drift >25% → MONITOR
9-13. (Reglas adicionales de clasificación y recomendaciones)

---

### 5. DRIFT-DETECTION-V1

**ID Proceso:** `drift-detection-v1`  
**Proporción:** 90% Automatizado - 10% HITL

#### Descripción Funcional:

**Finalidad:**  
Monitorear continuamente modelos en producción para detectar drift (desviación de datos o comportamiento), alertando tempranamente antes de que afecte la calidad de predicciones.

**Objetivo:**  
Detectar automáticamente 3 tipos de drift mediante análisis estadístico cada hora, clasificar severidad, y trigger automático de reentrenamiento cuando sea necesario.

**¿Cuándo se usa?**  
- **Automático:** Ejecuta cada 1 hora para modelos en producción (timer event)
- **Manual:** Análisis ad-hoc cuando se sospecha drift
- **Trigger:** Desde dashboards de monitoreo
- **Post-incident:** Análisis tras degradación de performance

**Resultado Esperado:**  
- **80% casos:** No drift detectado → Continuar monitoreo
- **15% casos:** Drift leve-moderado → Alert + Decisión humana
- **5% casos:** Drift crítico → Trigger automático de reentrenamiento

**Tipos de Drift Detectados:**
1. **Feature Drift:** Distribución de features de entrada cambió (PSI, KL divergence)
2. **Prediction Drift:** Distribución de predicciones cambió
3. **Concept Drift:** Relación entre features y target cambió

**Métricas Utilizadas:**
- **PSI** (Population Stability Index): >0.2 indica drift
- **KL Divergence:** Mide diferencia entre distribuciones
- **Statistical Tests:** Chi-square, KS test

**Acciones Posibles:**
- **NO_DRIFT:** Continuar monitoreo normal
- **RETRAIN:** Trigger automático de model-retraining-orchestration-v1
- **WAIT_MORE_DATA:** Esperar más datos antes de decidir
- **ACCEPT_DRIFT:** Documentar y aceptar (si impacto mínimo)

**SLA:**
- Detección: <1 hora desde que ocurre drift
- Decisión humana: 48h (solo si drift >0.5)
- Reentrenamiento: Automático si drift >0.7

#### Cómo se lanza:
```java
// Lanzamiento automático por timer (cada hora)
// O manual para análisis ad-hoc
Map<String, Object> variables = new HashMap<>();
variables.put("modelId", "model-prod-123");
variables.put("referenceDatasetId", "baseline-2024-10");
runtimeService.startProcessInstanceByKey("drift-detection-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Cálculo de Drift**
```
calculateDriftMetrics (ServiceTask)
└─ Python: drift_detection.py
   ├─ Feature drift (PSI, KL divergence)
   ├─ Prediction drift (model output distribution)
   └─ Concept drift (relationship changes)
```

**FASE 2: Scoring con Drools**
```
BusinessRuleTask: driftScoring
├─ 12 reglas de clasificación
└─ Output: NO_DRIFT / DRIFT_DETECTED / CRITICAL_DRIFT
```

**FASE 3: Decisión de Acción**
```
reviewDecisionGateway:
├─ RETRAIN → triggerRetraining → END
├─ WAIT_MORE_DATA → saveDriftResults → END
└─ ACCEPT_DRIFT → saveDriftResults → END
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Threshold |
|-------|-------------|-------------|-----------|
| **calculateDriftMetrics** | Sistema (Python) | Calcula PSI, KL, drift scores | - |
| **driftScoring** | Drools | Clasifica severidad drift | PSI >0.2 |
| **reviewDriftAnalysis** | MLOps Engineer | Decide acción (solo si drift >0.5) | 48h SLA |
| **triggerRetraining** | Sistema | Dispara proceso de reentrenamiento | - |
| **saveDriftResults** | Sistema | Persiste resultados en BBDD | - |

---

### 6. PERFORMANCE-DEGRADATION-V1

**ID Proceso:** `performance-degradation-v1`  
**Proporción:** 95% Automatizado - 5% HITL

#### Descripción Funcional:

**Finalidad:**  
Monitorear en tiempo real la degradación de performance de modelos en producción, detectando problemas antes de que impacten significativamente a usuarios finales.

**Objetivo:**  
Ejecutar checks de performance cada 15 minutos, detectar degradación automáticamente, intentar auto-remediation (scaling), y escalar a humano solo en casos críticos (P1).

**¿Cuándo se usa?**  
- **Automático:** Timer cada 15 minutos para todos los modelos en producción
- **Crítico:** Para modelos customer-facing de alta criticidad
- **Real-time:** Monitoring continuo de latency, error rate, throughput

**Resultado Esperado:**  
- **85% casos:** Performance normal → Continuar monitoreo
- **10% casos:** Degradación leve → Auto-scaling automático
- **5% casos:** Degradación crítica (P1) → Decisión humana urgente (30 min SLA)

**Métricas Monitoreadas:**
1. **Latency:** P50, P95, P99 (ms)
2. **Error Rate:** Porcentaje de errores
3. **Throughput:** Requests per second
4. **Resource Utilization:** CPU, Memory, GPU

**Thresholds de Alerta:**
- **Normal:** Latency P95 <200ms, Error rate <1%
- **Degradation:** Latency P95 200-500ms, Error rate 1-5%
- **Critical:** Latency P95 >500ms, Error rate >5%

**Acciones Posibles:**
- **CONTINUE_MONITORING:** Sin acción necesaria
- **AUTO_SCALE:** Scaling horizontal automático
- **ROLLBACK_NOW:** Rollback inmediato a versión anterior
- **SCALE_UP:** Aumentar recursos (vertical scaling)
- **INVESTIGATE:** Requiere investigación profunda

#### Cómo se lanza:
```java
// Lanzamiento automático por timer (cada 15 minutos)
// Crítico para detectar degradación en tiempo real
```

#### Flujo Detallado:

**FASE 1: Check Metrics**
```
checkPerformanceMetrics (ServiceTask)
└─ Métricas en tiempo real:
   ├─ Latency P50, P95, P99
   ├─ Error rate
   ├─ Throughput
   └─ Resource utilization
```

**FASE 2: Drools Scoring**
```
BusinessRuleTask: performanceScoring
├─ 12 reglas de severidad
└─ Output: NORMAL / DEGRADATION_DETECTED / CRITICAL
```

**FASE 3: Auto-Scaling Check**
```
checkAutoscaling (ServiceTask)
├─ Si degradation leve → auto-scale
└─ Si crítico → urgentDecision (UserTask)
    ├─ Timer: 30 min
    └─ Decision: ROLLBACK_NOW / SCALE_UP / INVESTIGATE
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | SLA |
|-------|-------------|-------------|-----|
| **checkPerformanceMetrics** | Sistema | Colecta métricas tiempo real | 1 min |
| **performanceScoring** | Drools | Clasifica severidad | Inmediato |
| **checkAutoscaling** | Sistema | Intenta auto-scaling | 2 min |
| **urgentDecision** | MLOps Engineer | Decisión crítica (solo P1) | 30 min |
| **notifyDegradation** | Sistema | Alerta a on-call team | Inmediato |

---

## 📊 PROCESOS DE EVALUACIÓN

### 7. LLM-EVALUATION-V1

**ID Proceso:** `llm-evaluation-v1`  
**Proporción:** 75% Automatizado - 25% HITL

#### Descripción Funcional:

**Finalidad:**  
Evaluar modelos LLM (Large Language Models) de forma exhaustiva, midiendo 50+ métricas de calidad, seguridad y performance antes de aprobar para producción o durante monitoreo continuo.

**Objetivo:**  
Ejecutar evaluación técnica completa automáticamente, clasificar calidad mediante Drools, y escalar solo casos borderline a revisión humana.

**¿Cuándo se usa?**  
- Pre-deployment: Antes de aprobar LLM para producción
- Post-training: Después de fine-tuning o reentrenamiento
- Periodic evaluation: Cada 30 días para LLMs en producción
- A/B testing: Comparar LLM challenger vs champion

**Resultado Esperado:**  
- **PASSED:** 70% - LLM cumple todos los criterios
- **REVIEW_REQUIRED:** 25% - Casos borderline requieren humano
- **FAILED:** 5% - No cumple criterios mínimos

**Métricas Evaluadas (50+):**
- Performance: Accuracy, Precision, Recall, F1, Perplexity
- Quality: BLEU, ROUGE, Coherence, Fluency
- Safety: Toxicity, Bias, Hallucination rate
- Context: Relevance, Coverage

**Threshold:** overallScore >0.75 para PASSED

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("llmId", "gpt-4-custom");
variables.put("evaluationDatasetId", "eval-2024-10");
variables.put("evaluationType", "COMPREHENSIVE"); // o "QUICK"
runtimeService.startProcessInstanceByKey("llm-evaluation-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Ejecución de Evaluación (Python)**
```
executeLlmEvaluation (ServiceTask)
└─ Python Service: leka-server-serving-evaluation
   └─ Métricas (50+):
       ├─ Accuracy, Precision, Recall, F1
       ├─ Perplexity, BLEU, ROUGE
       ├─ Toxicity, Bias, Hallucination
       ├─ Context relevance
       └─ Response quality
```

**FASE 2: Store + Scoring**
```
storeResults → BusinessRuleTask (Drools)
              └─ Output: PASSED / REVIEW_REQUIRED / FAILED
```

**FASE 3: Review (si requiere)**
```
llmEvaluationReview (UserTask)
├─ Solo si REVIEW_REQUIRED
└─ CandidateGroups: ml-engineers
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Métricas |
|-------|-------------|-------------|----------|
| **executeLlmEvaluation** | Python Service | Evaluación completa LLM | 50+ métricas |
| **storeResults** | Sistema | Persiste en PostgreSQL + MinIO | - |
| **llmEvaluationScoring** | Drools | Clasifica calidad (13 reglas) | Threshold: 0.75 |
| **llmEvaluationReview** | ML Engineer | Revisión humana casos borderline | 24h |

---

### 8. RAG-EVALUATION-V1

**ID Proceso:** `rag-evaluation-v1`  
**Proporción:** 75% Automatizado - 25% HITL

#### Descripción Funcional:

**Finalidad:**  
Evaluar sistemas RAG (Retrieval-Augmented Generation) midiendo calidad de retrieval, relevancia de contexto, precisión de respuestas y detección de alucinaciones.

**Objetivo:**  
Validar que el sistema RAG genera respuestas precisas, relevantes y sin alucinaciones, mediante evaluación automática y review humano solo para casos de riesgo medio.

**¿Cuándo se usa?**  
- Pre-deployment: Antes de lanzar sistema RAG
- Post-update: Después de actualizar vector store o modelo generativo
- Periodic: Cada 15 días para RAG en producción
- Quality assurance: Validación de calidad de respuestas

**Resultado Esperado:**  
- **Excellent/Good (LOW risk):** 70% - RAG funciona óptimamente
- **Moderate (MEDIUM risk):** 20% - Requiere review y ajustes
- **Poor/Critical (HIGH risk):** 10% - Requiere correcciones urgentes

**Métricas Evaluadas:**
1. Relevance Score: Relevancia de la respuesta (0-1)
2. Accuracy Score: Precisión factual (0-1)
3. Retrieval Precision/Recall: Calidad de recuperación de documentos
4. Context Relevance: Relevancia del contexto recuperado
5. Hallucination Count: Número de alucinaciones detectadas

**Threshold:** overallScore >0.75, hallucinationCount ==0

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("ragId", "rag-system-123");
variables.put("testQueries", testQueriesList);
variables.put("expectedAnswers", expectedAnswersList);
runtimeService.startProcessInstanceByKey("rag-evaluation-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Evaluación Paralela**
```
ParallelGateway:
├─ executeEvaluation → RAG metrics (relevance, accuracy, etc.)
└─ prepareContext → Context quality analysis
         ↓ (Merge)
BusinessRuleTask: ragScoring (13 reglas)
└─ Output: riskLevel (LOW/MEDIUM/HIGH/CRITICAL)
```

**FASE 2: Decisión por Risk Level**
```
ExclusiveGateway:
├─ LOW → storeResults → END
├─ HIGH/CRITICAL → createAlert → END
└─ MEDIUM → manualReview → END
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Métricas Clave |
|-------|-------------|-------------|----------------|
| **executeEvaluation** | Sistema (Python) | Evalúa RAG system | relevance, accuracy, hallucinations |
| **prepareContext** | Sistema | Analiza calidad de contexto | context_relevance, coverage |
| **ragScoring** | Drools | Clasifica riesgo (13 reglas) | overallScore threshold: 0.75 |
| **manualReview** | ML Engineer | Review manual (solo MEDIUM) | 24h |
| **storeResults** | Sistema | Persiste evaluación | - |
| **createAlert** | Sistema | Crea alerta para HIGH/CRITICAL | - |

---

### 9. DATASET-QUALITY-V1

**ID Proceso:** `dataset-quality-v1`  
**Proporción:** 75% Automatizado - 25% HITL

#### Descripción Funcional:

**Finalidad:**  
Validar calidad, integridad y compliance de datasets antes de usarlos para entrenamiento o evaluación de modelos, detectando problemas de calidad, sesgo y PII.

**Objetivo:**  
Ejecutar validaciones automáticas multi-dimensionales (formato, calidad, sesgo, PII) en paralelo, decidir automáticamente APPROVED/QUARANTINE/REJECTED, y escalar a humano solo datasets en cuarentena.

**¿Cuándo se usa?**  
- Pre-training: Antes de entrenar modelos con nuevo dataset
- Pre-evaluation: Antes de usar dataset para evaluación
- Periodic audit: Auditorías periódicas de datasets en uso
- Post-collection: Validación de datasets recién recolectados

**Resultado Esperado:**  
- **APPROVED:** 60% - Dataset cumple todos los criterios
- **QUARANTINE (Review Required):** 30% - Requiere review humano
- **REJECTED:** 10% - Formato inválido o problemas críticos

**Criterios de Evaluación:**
1. **Formato:** Schema validation, tipos de datos correctos
2. **Calidad:** Completitud >80%, Duplicados <10%, Nulos <20%
3. **Sesgo:** Bias score <0.3, representatividad balanceada
4. **PII:** PII count <2% del total de registros
5. **GDPR:** Cumplimiento de retención, consentimiento

**Threshold:** overallQualityScore >0.8 para APPROVED

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("datasetId", "training-dataset-2024");
variables.put("datasetPath", "s3://bucket/datasets/...");
variables.put("expectedSchema", schemaDefinition);
runtimeService.startProcessInstanceByKey("dataset-quality-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Validación de Formato**
```
validateFormat (ServiceTask)
└─ ExclusiveGateway:
   ├─ formatValid == false → END (Rejected)
   └─ formatValid == true → Continuar
```

**FASE 2: Data Profiling**
```
datasetProfiling (ServiceTask)
└─ Python: Análisis completo
   ├─ Completitud, precisión, consistencia
   ├─ Duplicados, nulos, inválidos
   └─ Estadísticas (mean, std, outliers)
```

**FASE 3: Análisis Paralelo**
```
ParallelGateway:
├─ detectBias → Sesgo en datos
└─ validateCompliance → PII detection, GDPR
         ↓ (Merge)
BusinessRuleTask: datasetQualityScoring
└─ Decision: APPROVED / REVIEW_REQUIRED / REJECTED
```

**FASE 4: Review Humana (si requiere)**
```
humanReview (UserTask)
├─ CandidateGroups: data-engineers, governance-admins
├─ Timer Boundary: 7 días
│  └─ datasetReviewReminder
└─ Decision: APPROVE / REJECT
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Threshold |
|-------|-------------|-------------|-----------|
| **validateFormat** | Sistema | Valida schema, formato | Schema compliance |
| **datasetProfiling** | Sistema (Python) | Profiling completo | 6 dimensiones calidad |
| **detectBias** | Sistema (Python) | Detecta sesgos en datos | biasScore <0.3 |
| **validateCompliance** | Sistema | PII detection, GDPR | piiCount <2% |
| **datasetQualityScoring** | Drools | Decisión final (13 reglas) | overallQuality >0.8 |
| **humanReview** | Data Engineer | Review manual datasets borderline | 7 días |
| **datasetReviewReminder** | Governance Admin | Recordatorio 7 días | - |

---

### 10. MODEL-EVALUATION-V1

**ID Proceso:** `model-evaluation-v1`  
**Proporción:** 80% Automatizado - 20% HITL

#### Cómo se lanza:
```java
// Lanzamiento automático post-training
Map<String, Object> variables = new HashMap<>();
variables.put("modelId", "model-new-version");
variables.put("testDatasetId", "test-2024-10");
runtimeService.startProcessInstanceByKey("model-evaluation-v1", variables);
```

#### Flujo Simple:
```
executeEvaluation → storeResults → ThresholdGateway
                                   ├─ Below → createAlert
                                   └─ Above → updateDashboard
```

#### Responsabilidades:

| Tarea | Responsable | Métricas |
|-------|-------------|----------|
| **executeEvaluation** | Sistema (Python) | Accuracy, precision, recall, F1, ROC-AUC |
| **storeResults** | Sistema | Persiste en PostgreSQL |
| **createAlert** | Sistema | Si performance <threshold |
| **updateDashboard** | Sistema | Actualiza métricas en tiempo real |

---

## 🛡️ PROCESOS DE GOVERNANCE

### 11. ETHICS-REVIEW-V1

**ID Proceso:** `ethics-review-v1`  
**Proporción:** 30% Automatizado - 70% HITL

#### Descripción Funcional:

**Finalidad:**  
Realizar revisión ética formal de sistemas IA de alto impacto, evaluando fairness, transparency, accountability, privacy, safety y autonomía humana, cumpliendo con EU AI Act.

**Objetivo:**  
Facilitar el trabajo del comité de ética mediante preparación automática (impact assessment, recolección de datos), permitiendo que el comité se enfoque en la decisión ética en lugar de tareas administrativas.

**¿Cuándo se usa?**  
- **Obligatorio:** Sistemas AI de alto riesgo (EU AI Act Art. 6)
- **Recomendado:** Sistemas que afectan >100K usuarios
- **Requerido:** Sistemas que afectan grupos vulnerables
- **Opcional:** Cualquier sistema donde se requiera validación ética formal

**Resultado Esperado:**  
- **APPROVED:** 40% - Sistema cumple estándares éticos
- **CONDITIONAL:** 40% - Aprobado con plan de mitigación
- **REJECTED:** 20% - No cumple estándares éticos mínimos

**Dimensiones Éticas Evaluadas:**
1. **Fairness:** Equidad en outcomes para todos los grupos
2. **Transparency:** Explicabilidad y claridad de funcionamiento
3. **Accountability:** Responsabilidad clara de decisiones
4. **Privacy:** Protección de datos personales y privacidad
5. **Safety:** Seguridad y ausencia de daños
6. **Human Autonomy:** Preservación de autonomía y agencia humana

**Riesgos Éticos Críticos (Auto-Reject):**
- Riesgo de discriminación
- Riesgo de doble uso (militar/civil)
- Clasificación EU AI Act: "Unacceptable Risk"

**SLA:** 14 días para decisión de comité

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("systemId", "ai-system-456");
variables.put("systemType", "GENERATIVE_AI");
variables.put("impactAssessment", impactData);
runtimeService.startProcessInstanceByKey("ethics-review-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Request + Assessment**
```
requestEthicsReview (UserTask)
├─ CandidateGroups: ml-engineers, governance-admins
└─ Input: System description, stakeholders, impact
         ↓
aiImpactAssessment (ServiceTask)
└─ Análisis automático de impacto social
```

**FASE 2: Scoring con Drools**
```
BusinessRuleTask: ethicsScoring
├─ Input: ethicsReviewFact
├─ Rules: ethics-review-scoring.drl (13 reglas)
├─ Evalúa: Fairness, Transparency, Accountability,
│          Privacy, Safety, Autonomy
└─ Output: requiresCommitteeReview (Boolean)
```

**FASE 3: Comité de Ética (si requiere)**
```
ethicsCommitteeReview (UserTask)
├─ CandidateGroups: ethics-committee
├─ Timer Boundary: 14 días
│  └─ ethicsReviewReminder
│     └─ Gateway: COMPLETE_NOW / ESCALATE / EXTEND
└─ Decision: APPROVE / CONDITIONAL / REJECT
```

**FASE 4: Mitigation Plan (si conditional)**
```
defineMitigationPlan (UserTask)
├─ CandidateGroups: governance-admins
└─ Define acciones de mitigación
         ↓
saveEthicsEvidence → END
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | SLA |
|-------|-------------|-------------|-----|
| **requestEthicsReview** | ML Engineer | Solicita revisión ética | - |
| **aiImpactAssessment** | Sistema | Assessment automático impacto | 10 min |
| **ethicsScoring** | Drools | Scoring ético (13 reglas) | 1 min |
| **ethicsCommitteeReview** | **Ethics Committee** | Revisión por comité multidisciplinar | 14 días |
| **ethicsReviewReminder** | Governance Admin | Recordatorio 14 días | - |
| **defineMitigationPlan** | Governance Admin | Define plan de mitigación | 7 días |
| **saveEthicsEvidence** | Sistema | Documenta evidencia y decisión | - |
| **rejectEthicsReview** | Sistema | Rechaza sistema IA | - |

#### Drools Rules (ethics-review-scoring.drl):

1. **Excellent Ethics** - All scores >0.9 + No risks → AUTO_APPROVE
2. **Good Ethics** - Minor concerns → CONDITIONAL
3. **Critical - Discrimination Risk** - → COMMITTEE_REQUIRED
4. **Critical - Dual Use Risk** - → AUTO_REJECT
5. **High Risk - Manipulation** - → COMMITTEE_REQUIRED
6. **High Risk - Surveillance** - → COMMITTEE_REQUIRED
7. **EU AI Act - Unacceptable** - → AUTO_REJECT
8. **EU AI Act - High Risk** - → COMMITTEE + Conformity assessment
9. **Vulnerable Groups Affected** - → COMMITTEE + Special protections
10. **Large Scale Impact** (>1M users) → COMMITTEE + External audit
11-13. (Compliance, regulatory, stakeholder rules)

---

### 12. COMPLIANCE-MONITORING-V1

**ID Proceso:** `compliance-monitoring-v1`  
**Proporción:** 85% Automatizado - 15% HITL

#### Descripción Funcional:

**Finalidad:**  
Monitorear compliance de todos los modelos en producción de forma continua (24/7), detectando automáticamente violaciones de GDPR, EU AI Act, data lineage y consent tracking.

**Objetivo:**  
Ejecutar validaciones de compliance cada 24 horas de forma completamente automática, creando incidents solo cuando se detectan problemas, y requiriendo intervención humana solo para issues críticos.

**¿Cuándo se usa?**  
- **Automático:** Timer Start Event ejecuta cada 24 horas
- **No requiere lanzamiento manual**
- **Aplica a:** TODOS los modelos, agents y sistemas IA en producción

**Resultado Esperado:**  
- **85% ejecuciones:** Compliant → Solo update dashboard
- **10% ejecuciones:** Non-compliant (non-critical) → Schedule review
- **5% ejecuciones:** Critical compliance issue → Create incident inmediato

**Validaciones Automáticas:**
1. **GDPR:** Retención de datos, consentimiento, right to be forgotten
2. **EU AI Act:** Documentación, transparency, human oversight
3. **Data Lineage:** Trazabilidad de origen de datos
4. **Consent Tracking:** Validación de consentimientos vigentes
5. **Model Registry:** Modelos documentados correctamente

**SLA:**
- Check: Cada 24 horas
- Review (non-critical): 7 días
- Incident (critical): Inmediato

#### Cómo se lanza:
```java
// Timer Start Event: Ejecuta automáticamente cada 24 horas
// No requiere lanzamiento manual
```

#### Flujo Detallado:

**FASE 1: Ejecución Automática**
```
Timer Start (24h) → executeComplianceCheck
                    └─ Valida compliance de todos los modelos activos
```

**FASE 2: Decisión**
```
ExclusiveGateway:
├─ Compliant → updateDashboard → END
└─ Non-Compliant → createAlerts → reviewIssues
                                  └─ Gateway: CRITICAL / NON_CRITICAL
                                     ├─ CRITICAL → createIncident
                                     └─ NON_CRITICAL → scheduleReview
                                                       ├─ Timer: 7 días
                                                       └─ reviewDecisionTask
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Frecuencia |
|-------|-------------|-------------|------------|
| **executeComplianceCheck** | Sistema | Valida compliance de todos los modelos | Cada 24h |
| **createAlerts** | Sistema | Genera alertas de non-compliance | Automático |
| **reviewIssues** | Compliance Officer | Revisa issues detectados | Ad-hoc |
| **createIncident** | Compliance Officer | Crea incident formal | Inmediato |
| **scheduleReview** | Sistema | Programa revisión futura | - |
| **reviewDecisionTask** | Compliance Officer | Decisión post-timer (7 días) | 7 días |

---

### 13. RISK-ASSESSMENT-V1

**ID Proceso:** `risk-assessment-v1`  
**Proporción:** 70% Automatizado - 30% HITL

#### Descripción Funcional:

**Finalidad:**  
Realizar evaluación integral de riesgos de sistemas IA en 3 dimensiones (técnico, negocio, compliance), consolidando en un risk score global y plan de mitigación.

**Objetivo:**  
Ejecutar análisis paralelo de riesgos en 3 dimensiones, consolidar mediante Drools, y escalar a Risk Officers solo casos de riesgo medio que requieran decisión humana.

**¿Cuándo se usa?**  
- Pre-deployment: Antes de desplegar sistema IA crítico
- Post-incident: Después de incidentes mayores
- Periodic: Cada 90 días para sistemas de alto riesgo
- Regulatory: Para cumplimiento de EU AI Act Article 9

**Resultado Esperado:**  
- **LOW_RISK:** 50% - Sistema de bajo riesgo, auto-documentado
- **MEDIUM_RISK:** 30% - Requiere review de Risk Officer
- **HIGH_RISK:** 20% - Escalación inmediata + documentación exhaustiva

**Dimensiones de Riesgo:**
1. **Technical Risk:** Performance, reliability, security, scalability
2. **Business Risk:** ROI, adoption, market, reputation
3. **Compliance Risk:** Regulatory, legal, privacy, ethical

**Threshold:** Overall risk score <0.6 para LOW_RISK

**SLA:** 3 días para review de riesgos medios

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("systemId", "ai-system-789");
variables.put("assessmentType", "COMPREHENSIVE"); // TECHNICAL, BUSINESS, COMPLIANCE
runtimeService.startProcessInstanceByKey("risk-assessment-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Análisis Paralelo de Riesgos**
```
ParallelGateway (3 branches):
├─ technicalRiskAssessment → Riesgos técnicos (performance, reliability)
├─ businessRiskAssessment → Riesgos de negocio (ROI, adoption)
└─ complianceRiskAssessment → Riesgos regulatorios (GDPR, EU AI Act)
         ↓ (Merge)
```

**FASE 2: Scoring con Drools**
```
BusinessRuleTask: calculateRiskScore
├─ Input: riskAssessmentFact (3 dimensiones)
├─ Output: LOW_RISK / MEDIUM_RISK / HIGH_RISK
└─ Calcula: Overall risk score ponderado
```

**FASE 3: Decisión por Nivel de Riesgo**
```
ExclusiveGateway:
├─ LOW_RISK → storeRiskAssessment → END
├─ MEDIUM_RISK → riskReviewTask (UserTask)
│                ├─ Timer: 3 días
│                └─ riskSlaReminder → Gateway:
│                                      ├─ ESCALATE
│                                      ├─ EXTEND
│                                      └─ FORCE_DECISION
└─ HIGH_RISK → storeRiskAssessment → notifyRiskDecision → END
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Tipos de Riesgo |
|-------|-------------|-------------|-----------------|
| **technicalRiskAssessment** | Sistema | Evalúa riesgos técnicos | Performance, Security, Reliability |
| **businessRiskAssessment** | Sistema | Evalúa riesgos de negocio | ROI, Adoption, Market |
| **complianceRiskAssessment** | Sistema | Evalúa riesgos regulatorios | GDPR, EU AI Act, Industry regs |
| **calculateRiskScore** | Drools | Scoring consolidado | Overall risk (weighted) |
| **riskReviewTask** | Risk Officer + Governance Admin | Review manual (solo MEDIUM) | 3 días |
| **riskSlaReminder** | Governance Leads | Recordatorio 3 días | - |
| **storeRiskAssessment** | Sistema | Documenta assessment | - |
| **notifyRiskDecision** | Sistema | Notifica decisión | - |

---

## 🤖 PROCESOS DE AUTOMATIZACIÓN

### 14. DEPLOYMENT-AUTOMATION-V1

**ID Proceso:** `deployment-automation-v1`  
**Proporción:** 100% Automatizado

#### Descripción Funcional:

**Finalidad:**  
Automatizar completamente el deployment de modelos aprobados a staging y producción, con validaciones, health checks y rollback automático en caso de fallo.

**Objetivo:**  
Desplegar modelos sin intervención humana, garantizando zero-downtime, validación automática, y rollback instantáneo si algo falla.

**¿Cuándo se usa?**  
- Trigger automático desde model-approval-v1 (cuando APPROVED)
- Trigger automático desde model-retraining (cuando promote)
- Manual para deployments ad-hoc

**Resultado Esperado:**  
- **Success:** 95% - Deployment exitoso
- **Failure con Rollback:** 5% - Auto-rollback si health check falla

**Validaciones:**
1. Environment validation (K8s cluster, resources)
2. Staging deployment + health checks
3. Production deployment solo si staging OK
4. Post-deployment health checks
5. Auto-rollback si cualquier check falla

**Timeout:** 10 minutos (auto-rollback)

#### Cómo se lanza:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("modelId", "model-approved-123");
variables.put("targetEnvironment", "PRODUCTION");
variables.put("deploymentStrategy", "BLUE_GREEN"); // o CANARY, ROLLING
runtimeService.startProcessInstanceByKey("deployment-automation-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Preparación y Validación**
```
prepareDeployment → validateEnvironment
                    └─ Gateway: Valid / Invalid
                       ├─ Invalid → rollback → END
                       └─ Valid → Continue
```

**FASE 2: Deployment Paralelo**
```
ParallelGateway:
├─ deployToStaging → Deploy en staging
│  └─ Timer Boundary: 10 min timeout
└─ updateConfiguration → Config maps, secrets
         ↓ (Merge)
healthCheck → Gateway: Healthy / Unhealthy
```

**FASE 3: Production Deployment**
```
ExclusiveGateway:
├─ Healthy → deployToProduction → notifySuccess → END
└─ Unhealthy → rollback → notifyFailure → END
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Timeout |
|-------|-------------|-------------|---------|
| **prepareDeployment** | Sistema | Prepara artefactos deployment | 5 min |
| **validateEnvironment** | Sistema | Valida K8s cluster, resources | 2 min |
| **deployToStaging** | Sistema | Deploy a staging env | 10 min |
| **updateConfiguration** | Sistema | Actualiza ConfigMaps, Secrets | 2 min |
| **healthCheck** | Sistema | Smoke tests + health checks | 3 min |
| **deployToProduction** | Sistema | Atomic swap en producción | 5 min |
| **rollback** | Sistema | Rollback automático si falla | 3 min |
| **notifySuccess/Failure** | Sistema | Notifica resultado | Inmediato |

---

### 15. ALERT-RESPONSE-V1

**ID Proceso:** `alert-response-v1`  
**Proporción:** 85% Automatizado - 15% HITL

#### Descripción Funcional:

**Finalidad:**  
Responder automáticamente a alertas de modelos ML, clasificando severidad y routing a la acción apropiada (auto-escalación, notificación, o log).

**Objetivo:**  
Clasificar alertas automáticamente y ejecutar respuesta apropiada sin intervención humana, escalando a on-call solo alertas P1 críticas.

**¿Cuándo se usa?**  
- **Automático:** Message Start Event cuando cualquier proceso crea alerta
- **Triggers:** drift-detection, performance-degradation, bias-detection, etc.

**Resultado Esperado:**  
- **P1 CRITICAL:** 10% - Escalación inmediata a on-call (15 min SLA)
- **P2 HIGH:** 20% - Notificación a equipo técnico
- **P3/P4 MEDIUM/LOW:** 70% - Log para análisis posterior

**Routing Automático:**
- CRITICAL → Auto-escalate + On-call response
- HIGH → Notify team
- MEDIUM/LOW → Log only

#### Cómo se lanza:
```java
// Message Start Event: Lanzado automáticamente cuando se crea alerta
// Mensaje: "alert_created"
// No requiere lanzamiento manual
```

#### Flujo Detallado:

**FASE 1: Clasificación**
```
Message Start → classifyAlert
                └─ Clasifica severidad: CRITICAL, HIGH, MEDIUM, LOW
```

**FASE 2: Routing por Severidad**
```
ExclusiveGateway:
├─ CRITICAL → autoEscalateCritical → oncallResponse (UserTask)
│                                     └─ DueDate: 15 min
├─ HIGH → notifyHighAlert → updateAlertStatus
└─ MEDIUM/LOW → logLowAlert → updateAlertStatus
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | SLA |
|-------|-------------|-------------|-----|
| **classifyAlert** | Sistema | Clasifica severidad alerta | 1 min |
| **autoEscalateCritical** | Sistema | Escala automáticamente P1 | Inmediato |
| **oncallResponse** | **MLOps On-Call** | Respuesta on-call (solo CRITICAL) | 15 min |
| **notifyHighAlert** | Sistema | Notifica a equipo (HIGH) | Inmediato |
| **logLowAlert** | Sistema | Log para análisis posterior | - |
| **updateAlertStatus** | Sistema | Actualiza estado en BBDD | - |

---

### 16. MODEL-RETRAINING-ORCHESTRATION-V1 🔥 **GAME CHANGER**

**ID Proceso:** `model-retraining-orchestration-v1`  
**Proporción:** 90% Automatizado - 10% HITL

#### Cómo se lanza:
```java
// Opción 1: Trigger automático desde drift-detection o performance-degradation
// Opción 2: Manual
Map<String, Object> variables = new HashMap<>();
variables.put("modelId", "model-prod-123");
variables.put("triggerReason", "DRIFT"); // DRIFT, DEGRADATION, SCHEDULED, MANUAL
variables.put("driftScore", 0.65);
variables.put("performanceDegradation", 25.0);
runtimeService.startProcessInstanceByKey("model-retraining-orchestration-v1", variables);

// Opción 3: Scheduled (cron: cada 90 días preventivo)
```

#### Flujo Detallado:

**FASE 1: Preparación Paralela**
```
ParallelGateway:
├─ prepareData → Prepara training data incremental/full
└─ validateInfrastructure → Valida GPUs, storage, cost
         ↓ (Merge)
```

**FASE 2: Decisión Inteligente con Drools**
```
BusinessRuleTask: decideRetrainingStrategy
├─ Input: modelRetrainingFact
│  ├─ currentAccuracy, driftScore, performanceDegradation
│  ├─ trainingDataSize, newDataAvailable
│  ├─ estimatedCost, spotInstancesAvailable
│  └─ daysSinceLastRetrain
│
├─ Rules: model-retraining-scoring.drl (13 reglas)
│
└─ Output: Decision
   ├─ SKIP (performance estable, <5% degradation)
   ├─ INCREMENTAL_RETRAIN (10-20% degradation)
   ├─ FULL_RETRAIN (>20% degradation o schema change)
   └─ HITL_REQUIRED (casos ambiguos)
```

**FASE 3: Ejecución de Reentrenamiento**
```
executeRetraining (ServiceTask - ASYNC)
├─ Estrategia: INCREMENTAL / FULL / TRANSFER_LEARNING
├─ Infrastructure: SPOT instances (70% ahorro) o ON_DEMAND
├─ Timeout: 6 horas
├─ Automated HPO: Si urgencyLevel == CRITICAL
└─ Checkpointing: Cada 30 min (si spot instances)
```

**FASE 4: Evaluación + A/B Testing**
```
evaluateNewModel → Gateway: Better / Worse
                   ├─ Better → setupABTesting
                   │           ├─ Champion (modelo actual)
                   │           ├─ Challenger (modelo nuevo)
                   │           ├─ Traffic split: 10% (configurable)
                   │           └─ Duration: 3 días
                   │                ↓
                   │           monitorABTest
                   │           └─ Statistical significance test
                   │              ↓
                   │           Gateway: Success / Failed
                   │           ├─ Success → promoteToProduction (atomic swap)
                   │           └─ Failed → rollback
                   │
                   └─ Worse → rollback → END
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Decisión Drools |
|-------|-------------|-------------|-----------------|
| **prepareData** | Sistema | Prepara datos training | - |
| **validateInfrastructure** | Sistema | Valida GPUs, cost limits | - |
| **decideRetrainingStrategy** | Drools | Decide SKIP/INCREMENTAL/FULL | 13 reglas inteligentes |
| **approvalTask** | ML Engineer (solo no-urgente) | Aprueba retraining | Solo si !critical |
| **executeRetraining** | Sistema (Async) | Reentrena modelo | Timeout: 6h |
| **evaluateNewModel** | Sistema | Evalúa nuevo modelo | Reutiliza model-evaluation |
| **setupABTesting** | Sistema | Configura A/B test | Champion vs Challenger |
| **monitorABTest** | Sistema | Monitorea statistical significance | 3 días |
| **promoteToProduction** | Sistema | Atomic swap zero-downtime | 2 min |
| **rollback** | Sistema | Rollback si falla | 3 min |

#### Innovaciones Técnicas:

**Zero-Downtime Retraining:**
```
1. Modelo actual (Champion) sigue en producción
2. Nuevo modelo (Challenger) se entrena en paralelo
3. A/B Testing con traffic split controlado
4. Atomic swap si Challenger gana
5. Rollback instantáneo si falla
```

**Cost Optimization:**
```
Drools Rules:
- Si urgencyLevel != CRITICAL → Usar SPOT instances (70% ahorro)
- Si estimatedCost > $100 → Deshabilitar HPO automático
- Si trainingTime >2h + spotAvailable → Checkpointing cada 30 min
```

**Champion/Challenger Pattern:**
```
┌──────────────┐         ┌──────────────┐
│  Champion    │ 90%     │  Challenger  │ 10%
│  (v1.2.3)    │ ─────→  │  (v1.3.0)    │ ─────→
│  Prod actual │  traffic│  Nuevo modelo│  traffic
└──────────────┘         └──────────────┘
        ↓                        ↓
    Metrics                  Metrics
        └────────────┬───────────┘
                     ↓
            Statistical Test
           (3 días, p-value <0.05)
                     ↓
         ┌───────────┴───────────┐
         ↓                       ↓
     Winner                  Loser
   (Promote)              (Rollback)
```

---

### 17. INCIDENT-RESPONSE-RCA-V1 🔥 **KILLER FEATURE**

**ID Proceso:** `incident-response-rca-v1`  
**Proporción:** 85% Automatizado - 15% HITL

#### Cómo se lanza:
```java
// Message Start Event: Trigger automático cuando se crea incident
// Mensaje: "incident_created"

// También puede lanzarse manualmente:
Map<String, Object> variables = new HashMap<>();
variables.put("incidentId", "INC-2024-001");
variables.put("modelId", "model-prod-456");
variables.put("severity", "P1_CRITICAL");
runtimeService.startProcessInstanceByKey("incident-response-rca-v1", variables);
```

#### Flujo Detallado:

**FASE 1: Recolección Paralela de Diagnóstico**
```
Message Start → ParallelGateway (3 branches):
                ├─ collectLogs → Últimos 1000 logs
                ├─ collectMetrics → Time-series 24h
                └─ collectTraces → Distributed tracing
                         ↓ (Merge)
```

**FASE 2: Clasificación Inicial con Drools**
```
BusinessRuleTask: classifyIncident
├─ Input: incidentResponseFact
├─ Rules: incident-response-scoring.drl (12 reglas)
├─ Clasifica:
│  └─ Severity: P1_CRITICAL / P2_HIGH / P3_MEDIUM / P4_LOW
│  └─ Impact: CUSTOMER_FACING / INTERNAL / MONITORING_ONLY
│  └─ Type: PERFORMANCE / ERROR / DRIFT / BIAS / AVAILABILITY
│
└─ Output:
   ├─ suspectedCauses (List)
   ├─ isKnownIssue (Boolean)
   ├─ similarIncidentId (String)
   └─ canAutoRemediate (Boolean)
```

**FASE 3: AI-Powered Root Cause Analysis (LLM)**
```
aiRootCauseAnalysis (ServiceTask - ASYNC)
└─ LLM (GPT-4) analiza:
   ├─ Logs (últimos 1000)
   ├─ Error patterns
   ├─ Correlación temporal con deployments/configs
   ├─ Historical similar incidents
   └─ Infrastructure events
         ↓
   Genera:
   ├─ primaryCause (String)
   ├─ causeConfidence (0-1)
   ├─ diagnosticData (JSON)
   └─ recommendedActions (List)
```

**FASE 4: Auto-Remediation (5 estrategias)**
```
ExclusiveGateway: remediationGateway
│
├─ AUTO_FIX (isKnownIssue + confidence >0.8)
│  └→ applyAutoFix
│     ├─ Busca fix en Knowledge Base
│     ├─ Aplica fix automático
│     └─ Verifica resolución en 2 min
│
├─ ROLLBACK (recentDeployment + confidence >0.7)
│  └→ executeRollback
│     ├─ Rollback a última versión estable
│     ├─ Validación post-rollback
│     └─ Estimado: 5 min recovery
│
├─ SCALE (throughputDrop >30%)
│  └→ executeScaling
│     ├─ Horizontal scaling +50%
│     ├─ Auto-scaling agresivo
│     └─ Validación en 3 min
│
├─ HITL_REQUIRED (casos nuevos o baja confidence)
│  └→ manualIntervention (UserTask)
│     ├─ CandidateGroups: mlops-engineers, on-call-team
│     ├─ Priority: 100 (máxima)
│     └─ DueDate: Según severity
│
└─ ESCALATE (P1 >15 min sin resolver)
   └→ escalateIncident
      ├─ Notifica engineering leadership
      ├─ Activa incident commander
      └─ Requiere post-mortem
```

**FASE 5: Verificación y Aprendizaje**
```
verifyResolution → Gateway: Resolved / Not Resolved
                   ├─ Resolved → updateKnowledgeBase (Self-Learning)
                   │             └─ Guarda fix para futuros incidentes
                   │                  ↓
                   │             generatePostMortem (auto con LLM)
                   │                  ↓
                   │             notifyResolution → END
                   │
                   └─ Not Resolved → END (Escalated)
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | MTTR Target |
|-------|-------------|-------------|-------------|
| **collectLogs/Metrics/Traces** | Sistema | Recolección paralela diagnóstico | 1 min |
| **classifyIncident** | Drools | Clasificación automática | 30 seg |
| **aiRootCauseAnalysis** | **LLM (GPT-4)** | RCA con IA - Analiza logs | 2-3 min |
| **applyAutoFix** | Sistema | Fix automático (conocidos) | 2 min |
| **executeRollback** | Sistema | Rollback automático | 5 min |
| **executeScaling** | Sistema | Scaling horizontal | 3 min |
| **manualIntervention** | MLOps On-Call | Solo casos nuevos (15%) | Según P |
| **escalateIncident** | Sistema | Escala a leadership | Inmediato |
| **verifyResolution** | Sistema | Valida que incident está resuelto | 2 min |
| **updateKnowledgeBase** | Sistema | **Self-Learning** - Guarda fix | 1 min |
| **generatePostMortem** | LLM | Post-mortem automático | 5 min |

#### Métricas de Performance:

| Métrica | Target | Actual Industria | Mejora |
|---------|--------|------------------|--------|
| **MTTR** (Mean Time To Recovery) | <5 min | 30-60 min | **6-12x** |
| **Auto-Resolution Rate** | 85% | 10-15% | **5-8x** |
| **RCA Accuracy** | >90% | 60-70% | **1.3-1.5x** |
| **False Positive Rate** | <5% | 20-30% | **4-6x** |

---

### 16. MODEL-RETRAINING-ORCHESTRATION-V1 🔥

**Proporción:** 90% Automatizado - 10% HITL

#### Triggers Automáticos:
```java
// Trigger 1: Desde drift-detection-v1
if (driftScore > 0.7) {
    messagingService.send("trigger-retraining", modelId);
}

// Trigger 2: Desde performance-degradation-v1
if (performanceDegradation > 20) {
    messagingService.send("trigger-retraining", modelId);
}

// Trigger 3: Scheduled (cada 90 días preventivo)
@Scheduled(cron = "0 0 2 */90 * *") // 2 AM cada 90 días
public void scheduledRetraining() {
    // Lanza retraining preventivo
}

// Trigger 4: Manual
runtimeService.startProcessInstanceByKey(
    "model-retraining-orchestration-v1", variables
);
```

#### Drools Decision Logic:

```
Drools evalúa:
├─ performanceDegradation >30% || driftScore >0.8
│  └→ Decision: FULL_RETRAIN (inmediato, no approval)
│
├─ performanceDegradation 15-30% || driftScore 0.5-0.8
│  └→ Decision: INCREMENTAL_RETRAIN (no approval)
│
├─ performanceDegradation 10-15% || daysSinceLastRetrain >90
│  └→ Decision: INCREMENTAL_RETRAIN (requiere approval)
│
├─ performanceDegradation <5% && driftScore <0.2
│  └→ Decision: SKIP (notifica + monitorea)
│
└─ Caso ambiguo
   └→ Decision: HITL_REQUIRED (approval obligatorio)
```

#### Innovación: Zero-Downtime A/B Testing

**Setup A/B Testing:**
```python
# Champion: Modelo actual en producción (v1.2.3)
# Challenger: Modelo recién entrenado (v1.3.0)

traffic_split = {
    "champion": 90,   # 90% tráfico actual
    "challenger": 10  # 10% tráfico nuevo modelo
}

monitoring = {
    "metrics": ["accuracy", "latency", "error_rate"],
    "duration_days": 3,
    "statistical_test": "t-test",
    "significance_level": 0.05
}
```

**Monitor A/B Test:**
```python
# Cada hora durante 3 días:
if challenger.accuracy > champion.accuracy + 0.02:  # 2% mejora
    if p_value < 0.05:  # Statistical significance
        decision = "PROMOTE"  # Challenger gana
elif challenger.accuracy < champion.accuracy - 0.01:  # 1% peor
    decision = "ROLLBACK"  # Champion gana
else:
    continue_monitoring()  # Esperar más datos
```

**Promote to Production:**
```python
# Atomic swap (zero-downtime):
1. Update load balancer config
2. Gradual traffic shift: 10% → 25% → 50% → 100%
3. Monitor during shift (rollback if any spike)
4. Archive old model as backup
5. Update model registry
```

#### Responsabilidades por Tarea:

| Tarea | Responsable | Descripción | Automatización |
|-------|-------------|-------------|----------------|
| **prepareData** | Sistema | Prepara training data | 100% |
| **validateInfrastructure** | Sistema | Valida GPUs, cuotas, cost | 100% |
| **decideRetrainingStrategy** | Drools | SKIP/INCREMENTAL/FULL | 100% |
| **approvalTask** | ML Engineer | Solo casos no-urgentes | Manual |
| **executeRetraining** | Sistema (Async) | Ejecuta training job | 100% |
| **evaluateNewModel** | Sistema | Evalúa nuevo modelo | 100% |
| **setupABTesting** | Sistema | Configura Champion/Challenger | 100% |
| **monitorABTest** | Sistema | Monitor + statistical test | 100% |
| **promoteToProduction** | Sistema | Atomic swap | 100% |
| **rollback** | Sistema | Rollback si falla | 100% |

---

## 📖 ANEXOS TÉCNICOS

### A. Convenciones de Naming

**Procesos:**
- Formato: `{nombre}-v{version}.bpmn`
- Ejemplo: `agent-approval-v1.bpmn`

**Variables:**
- camelCase para variables de proceso
- `{entity}Id` para IDs
- `{entity}Result` para outputs de Drools

**Tasks:**
- camelCase para task IDs
- Nombres descriptivos en inglés

**User Tasks:**
- FormKey: `{nombre}-form` (sin extensión .zul en BPMN)
- CandidateGroups: roles separados por comas

### B. Integración con Drools

**Pattern estándar:**
```xml
<businessRuleTask id="calculateScore" 
                  name="Calculate Score" 
                  activiti:ruleVariablesInput="${myFact}" 
                  activiti:resultVariable="myResult">
```

**En el Delegate previo:**
```java
// Preparar fact object
MyFact fact = new MyFact();
fact.setProperty1(value1);
fact.setProperty2(value2);

// Inyectar en proceso
execution.setVariable("myFact", fact);

// Drools ejecuta automáticamente
// Resultado disponible en ${myResult}
```

### C. Timer Boundaries (SLAs)

**Sintaxis:**
```xml
<boundaryEvent id="slaTimer" 
               name="SLA Reminder" 
               attachedToRef="userTask" 
               cancelActivity="false">
    <timerEventDefinition>
        <timeDuration>P3D</timeDuration>  <!-- 3 días -->
    </timerEventDefinition>
</boundaryEvent>
```

**Duraciones:**
- PT15M - 15 minutos
- PT24H - 24 horas
- P3D - 3 días
- P7D - 7 días
- P14D - 14 días

### D. Error Boundaries

**Sintaxis:**
```xml
<boundaryEvent id="errorEvent" 
               name="Handle Error" 
               attachedToRef="serviceTask">
    <errorEventDefinition errorRef="pythonServiceError"/>
</boundaryEvent>

<error id="pythonServiceError" 
       name="PythonServiceError" 
       errorCode="PYTHON_ERROR"/>
```

**Manejo en Delegate:**
```java
@Component
public class MyDelegate implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        try {
            // Llamada a Python
        } catch (Exception e) {
            throw new BpmnError("PYTHON_ERROR", e.getMessage());
        }
    }
}
```

### E. Roles y Grupos

**Grupos definidos en SSOROL:**

| Grupo | Procesos | Responsabilidades |
|-------|----------|-------------------|
| **ml-engineers** | Todos | Usuarios estándar ML |
| **senior-ml-engineers** | model-approval | Revisiones técnicas avanzadas |
| **governance-admins** | Aprobaciones, Ethics | Administradores governance |
| **governance-leads** | SLA reminders | Escalación y decisiones críticas |
| **ethics-committee** | ethics-review | Revisiones éticas |
| **compliance-officers** | compliance-monitoring | Oficiales de cumplimiento |
| **data-scientists** | bias-detection, dataset-quality | Expertos en datos |
| **data-engineers** | dataset-quality | Ingenieros de datos |
| **mlops-engineers** | performance, deployment, incidents | Operaciones ML |
| **on-call-team** | alert-response, incidents | Guardia 24/7 |
| **risk-officers** | risk-assessment | Oficiales de riesgo |

### F. Configuración de Drools

**Ubicación de archivos:**
```
src/main/resources/rules/
├── agent-scoring.drl
├── model-approval-scoring.drl
├── bias-detection-scoring.drl
├── drift-detection-scoring.drl
├── performance-degradation-scoring.drl
├── llm-evaluation-scoring.drl
├── rag-evaluation-scoring.drl
├── dataset-quality-scoring.drl
├── ethics-review-scoring.drl
├── model-retraining-scoring.drl
└── incident-response-scoring.drl
```

**Activación en Spring Boot:**
```java
@Configuration
public class DroolsConfig {
    
    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        
        // Cargar todos los DRL
        kieFileSystem.write("src/main/resources/rules/agent-scoring.drl", 
            resourcePatternResolver.getResource("classpath:rules/agent-scoring.drl"));
        // ... (repetir para cada DRL)
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        
        return kieServices.newKieContainer(
            kieServices.getRepository().getDefaultReleaseId()
        );
    }
}
```

### G. Bandeja de Tareas

**Consulta de tareas pendientes:**
```java
List<Task> tasks = taskService.createTaskQuery()
    .taskCandidateGroup("ml-engineers")
    .orderByTaskPriority().desc()
    .orderByTaskCreateTime().asc()
    .list();
```

**Completar tarea:**
```java
Map<String, Object> variables = new HashMap<>();
variables.put("decision", "APPROVE");
variables.put("comments", "Model meets all criteria");
taskService.complete(taskId, variables);
```

---

## 🎓 GUÍA DE TROUBLESHOOTING

### Problema: Proceso no se visualiza en Eclipse

**Solución:**
1. Verificar namespace: `xmlns:activiti="http://activiti.org/bpmn"`
2. Verificar que `<definitions>` esté en una sola línea
3. Verificar coordenadas (decimales en waypoints)
4. Verificar que exista `<bpmndi:BPMNDiagram>` completo

### Problema: BusinessRuleTask no ejecuta reglas

**Solución:**
1. Verificar que Fact object esté en variable: `${myFact}`
2. Verificar que DRL tenga package correcto
3. Verificar imports en DRL
4. Check logs Drools: `log.debug("Drools result: {}", result);`

### Problema: Timer Boundary no se dispara

**Solución:**
1. Verificar `cancelActivity="false"` (no cancela tarea)
2. Verificar formato duración: `P3D` (ISO-8601)
3. Verificar Flowable Async Executor está activo
4. Check: `flowable.async-executor-activate=true`

---

**FIN DOCUMENTACIÓN TÉCNICA**

---

**Para dudas técnicas:**
- Revisar código en: `src/main/java/com/codeflowx/govern/workflow/`
- Logs Flowable: `flowable.engine` level DEBUG
- Drools logs: `org.drools` level DEBUG
