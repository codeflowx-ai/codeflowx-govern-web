-- =====================================================
-- INSERT: PROCESOS BPMN EN BPMMPROCES
-- =====================================================
-- Proyecto: suinsit.nova.web
-- Propósito: Registrar todos los procesos BPMN de AI Governance
-- 
-- Nota: Estos procesos se pueden gestionar desde pantallas de
--       administración existentes. Los menús se crearán manualmente
--       desde la interfaz de usuario.
-- =====================================================

-- Limpiar procesos de governance existentes (solo si es necesario)
-- DELETE FROM BPMMPROCES WHERE KEYPROCESS LIKE '%approval%' OR KEYPROCESS LIKE '%detection%' OR KEYPROCESS LIKE '%monitoring%';

-- =====================================================
-- GRUPO 1: PROCESOS DE APROBACIÓN
-- =====================================================

-- 1. AGENT APPROVAL V1 (OPTIMIZADO)
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'agent-approval-v1',
    'Agent Approval V1',
    '1.0',
    'Proceso de aprobación de Agent IA con elementos BPMN avanzados.

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Error Boundaries, Timer Boundary (24h SLA), MailTask
PROPORCIÓN: 80% Automatizado - 20% HITL

FLUJO:
1. ParallelGateway → Risk + Compliance + Ethics (simultáneas)
2. BusinessRuleTask → Drools scoring (9 reglas)
3. ExclusiveGateway → AUTO_APPROVE / HITL_REQUIRED / AUTO_REJECT
4. MailTask → Notificaciones automáticas

PERFORMANCE: 2.4x más rápido que versión secuencial
RESILIENCIA: Error boundaries en llamadas Python
SLA: Timer 24h en HITL con recordatorio a governance-leads

CANDIDATEGROUPS: ml-engineers, governance-admins, governance-leads'
);

-- 2. MODEL APPROVAL V1 (OPTIMIZADO)
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'model-approval-v1',
    'Model Approval V1',
    '1.0',
    'Proceso de aprobación de Model ML con validaciones paralelas y Drools.

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Error Boundary, Timer Boundary (3 días SLA), MailTask
PROPORCIÓN: 60% Automatizado - 40% HITL (ML Review + Governance Review)

FLUJO:
1. Submit Request (User Task)
2. ParallelGateway → Performance + Bias + Compliance (simultáneas)
3. ML Engineer Review (User Task)
4. Governance Review (User Task) + Timer SLA 3 días
5. BusinessRuleTask → Drools scoring (12 reglas)
6. ExclusiveGateway → APPROVED / CONDITIONAL_APPROVAL / REJECTED
7. MailTask → 3 tipos de notificaciones

DECISIONES:
- APPROVED: Listo para producción
- CONDITIONAL_APPROVAL: Aprobado con monitorización adicional
- REJECTED: No cumple criterios

PERFORMANCE: 3x más rápido en validaciones
CANDIDATEGROUPS: ml-engineers, senior-ml-engineers, governance-admins, governance-leads'
);

-- =====================================================
-- GRUPO 2: PROCESOS DE MONITORIZACIÓN
-- =====================================================

-- 3. COMPLIANCE MONITORING
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'compliance-monitoring-process',
    'Compliance Monitoring',
    '1.0',
    'Monitorización continua de compliance de modelos ML en producción.

ELEMENTOS: Start Timer (cada 6 horas), BusinessRuleTask (Drools), Timer Boundary (7 días), User Tasks
PROPORCIÓN: 85% Automatizado - 15% HITL

FLUJO:
1. Start Timer (cada 6h)
2. Check Compliance (Delegate)
3. BusinessRuleTask → Drools classification
4. ExclusiveGateway → OK / REVIEW_REQUIRED / INCIDENT
5. Schedule Review + Timer Boundary (7 días)
6. User Task con decisión (RESTART / POSTPONE / FINISH)

SCHEDULE: Cada 6 horas automático
SLA: 7 días para revisión de issues
CANDIDATEGROUPS: compliance-officers'
);

-- 4. DRIFT DETECTION
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'drift-detection-process',
    'Drift Detection',
    '1.0',
    'Detección de drift en modelos ML en producción.

ELEMENTOS: Start Timer (cada hora), BusinessRuleTask (Drools), Timer Boundary (48h), User Tasks
PROPORCIÓN: 90% Automatizado - 10% HITL

FLUJO:
1. Start Timer (cada 1h)
2. Calculate Drift Score (Delegate)
3. BusinessRuleTask → Drools classification
4. ExclusiveGateway → NO_DRIFT / DRIFT_DETECTED / CRITICAL_DRIFT
5. Notify + Timer Boundary (48h)
6. User Task decisión (RETRAIN / WAIT_MORE_DATA / ACCEPT_DRIFT)

SCHEDULE: Cada hora
DRIFT THRESHOLD: Configurable via Drools
SLA: 48 horas para decisión de reentrenamiento
CANDIDATEGROUPS: ml-engineers, mlops-engineers'
);

-- 5. PERFORMANCE DEGRADATION
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'performance-degradation-process',
    'Performance Degradation',
    '1.0',
    'Monitorización de degradación de performance de modelos.

ELEMENTOS: Start Timer (cada 15 min), BusinessRuleTask (Drools), Timer Boundary (30 min), User Task
PROPORCIÓN: 95% Automatizado - 5% HITL

FLUJO:
1. Start Timer (cada 15min)
2. Check Metrics (Delegate)
3. BusinessRuleTask → Drools severity
4. ExclusiveGateway → NORMAL / DEGRADATION_DETECTED / CRITICAL
5. Check Autoscaling + Timer Boundary (30 min)
6. User Task decisión urgente (ROLLBACK_NOW / SCALE_UP / INVESTIGATE)

SCHEDULE: Cada 15 minutos (crítico)
SLA: 30 minutos para intervención manual
CANDIDATEGROUPS: mlops-engineers'
);

-- =====================================================
-- GRUPO 3: PROCESOS DE DETECCIÓN Y REVISIÓN
-- =====================================================

-- 6. BIAS DETECTION
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'bias-detection-process',
    'Bias Detection',
    '1.0',
    'Detección y análisis de bias en modelos ML.

ELEMENTOS: BusinessRuleTask (Drools), Timer Boundary (24h), User Tasks
PROPORCIÓN: 70% Automatizado - 30% HITL

FLUJO:
1. Execute Bias Detection (Delegate → Python)
2. BusinessRuleTask → Drools severity
3. ExclusiveGateway → NO_BIAS / BIAS_DETECTED / CRITICAL_BIAS
4. Review Detected Biases (User Task) + Timer Boundary (24h)
5. User Task decisión urgente (MITIGATE / REJECT / ACCEPT_RISK)

BIAS TYPES: Demographic, Geographic, Temporal
SLA: 24 horas para decisión de mitigación
CANDIDATEGROUPS: ml-engineers, data-scientists, governance-admins, ml-leads'
);

-- 7. ETHICS REVIEW
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'ethics-review-process',
    'Ethics Review',
    '1.0',
    'Proceso de revisión ética de modelos y agents IA.

ELEMENTOS: Timer Boundary (14 días), User Tasks
PROPORCIÓN: 30% Automatizado - 70% HITL (proceso principalmente humano)

FLUJO:
1. Request Ethics Review (User Task)
2. Ethics Committee Review (User Task) + Timer Boundary (14 días)
3. ExclusiveGateway → APPROVED / REJECTED / NEEDS_MITIGATION
4. Define Mitigation Plan (User Task si aplica)

COMITÉ: Comité de ética multidisciplinar
SLA: 14 días para revisión de comité
CANDIDATEGROUPS: ml-engineers, governance-admins, ethics-committee'
);

-- =====================================================
-- GRUPO 4: PROCESOS DE EVALUACIÓN
-- =====================================================

-- 8. LLM EVALUATION
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'llm-evaluation-process',
    'LLM Evaluation',
    '1.0',
    'Evaluación completa de modelos LLM (precisión, calidad, seguridad).

ELEMENTOS: BusinessRuleTask (Drools), User Tasks
PROPORCIÓN: 75% Automatizado - 25% HITL

FLUJO:
1. Execute LLM Evaluation (Delegate → Python: 50+ métricas)
2. Store Results (Delegate)
3. BusinessRuleTask → Drools classification
4. ExclusiveGateway → PASSED / REVIEW_REQUIRED / FAILED
5. LLM Evaluation Review (User Task si requiere)

MÉTRICAS: Precisión, recall, F1, perplexity, toxicity, bias, hallucination
PYTHON SERVICE: leka-server-serving-evaluation
CANDIDATEGROUPS: ml-engineers'
);

-- 9. RAG EVALUATION
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'rag-evaluation-process',
    'RAG Evaluation',
    '1.0',
    'Evaluación de sistemas RAG (retrieval, relevancia, precisión).

ELEMENTOS: BusinessRuleTask (Drools), User Tasks
PROPORCIÓN: 75% Automatizado - 25% HITL

FLUJO:
1. Execute RAG Evaluation (Delegate → Python)
2. Store RAG Results (Delegate)
3. BusinessRuleTask → Drools quality assessment
4. ExclusiveGateway → PASSED / REVIEW_REQUIRED / FAILED

MÉTRICAS: Context relevance, answer accuracy, retrieval precision, hallucination detection
PYTHON SERVICE: leka-server-serving-evaluation
CANDIDATEGROUPS: ml-engineers'
);

-- 10. DATASET QUALITY GOVERNANCE
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'dataset-quality-governance-process',
    'Dataset Quality Governance',
    '1.0',
    'Evaluación de calidad de datasets antes de entrenamiento/evaluación.

ELEMENTOS: BusinessRuleTask (Drools), Timer Boundary (7 días), User Tasks, RabbitMQ
PROPORCIÓN: 75% Automatizado - 25% HITL

FLUJO:
1. Profile Dataset (Delegate → Python)
2. Detect Bias (Delegate → Python)
3. Detect PII (Delegate → Python)
4. BusinessRuleTask → Drools quality score
5. ExclusiveGateway → APPROVED / QUARANTINE / REJECTED
6. Human Review (User Task si quarantine) + Timer Boundary (7 días)

VALIDACIONES: Data profiling, bias detection, PII detection, schema validation
ASYNC: RabbitMQ para evaluaciones largas
SLA: 7 días para revisión de datasets en cuarentena
CANDIDATEGROUPS: data-engineers, governance-admins'
);

-- =====================================================
-- GRUPO 5: PROCESOS DE RESPUESTA Y AUTOMATIZACIÓN
-- =====================================================

-- 11. ALERT RESPONSE
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'alert-response-process',
    'Alert Response',
    '1.0',
    'Proceso de respuesta automática a alertas críticas de modelos.

ELEMENTOS: BusinessRuleTask (Drools), ServiceTasks
PROPORCIÓN: 100% Automatizado (sin HITL)

FLUJO:
1. Classify Alert (Delegate)
2. BusinessRuleTask → Drools severity
3. ExclusiveGateway → CRITICAL / WARNING / INFO
4. Execute Response Actions (Delegate)
5. Notify Team (Delegate)

TIEMPO RESPUESTA: < 30 segundos
ALERTAS: Performance, drift, bias, errors, compliance
TOTALMENTE AUTOMATIZADO: Sin intervención humana'
);

-- 12. DEPLOYMENT AUTOMATION
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'deployment-automation-process',
    'Deployment Automation',
    '1.0',
    'Automatización de deployment de modelos aprobados.

PROPORCIÓN: 100% Automatizado

FLUJO:
1. Prepare Deployment (Delegate)
2. Deploy to Environment (Delegate)
3. Health Check (Delegate)
4. ExclusiveGateway → SUCCESS / FAILED
5. Rollback (si falla) / Notify Success

ENTORNOS: Staging, Production
ROLLBACK: Automático si health check falla
TOTALMENTE AUTOMATIZADO: Sin intervención humana'
);

-- 13. MODEL EVALUATION
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'model-evaluation-process',
    'Model Evaluation',
    '1.0',
    'Evaluación técnica de modelos ML (métricas, performance, quality).

PROPORCIÓN: 80% Automatizado - 20% HITL

FLUJO:
1. Execute Model Evaluation (Delegate → Python: métricas técnicas)
2. Store Evaluation Results (Delegate)
3. BusinessRuleTask → Drools quality assessment
4. ExclusiveGateway → PASSED / REVIEW_REQUIRED / FAILED
5. Human Review (User Task si requiere)

MÉTRICAS: Accuracy, precision, recall, F1, ROC-AUC, confusion matrix
PYTHON SERVICE: leka-server-serving-evaluation
CANDIDATEGROUPS: ml-engineers, data-scientists'
);

-- 14. PROMPT APPROVAL
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'prompt-approval-process',
    'Prompt Approval',
    '1.0',
    'Proceso de aprobación de prompts para LLMs (seguridad, calidad, ética).

PROPORCIÓN: 70% Automatizado - 30% HITL

FLUJO:
1. Prompt Safety Check (Delegate → Python: toxicity, PII, etc.)
2. Prompt Quality Assessment (Delegate)
3. BusinessRuleTask → Drools classification
4. ExclusiveGateway → AUTO_APPROVE / REVIEW_REQUIRED / REJECT
5. Human Review (User Task para casos borderline)

VALIDACIONES: Safety, quality, hallucination risk, bias potential
SLA: 24 horas para revisión
CANDIDATEGROUPS: ml-engineers, governance-admins'
);

-- 15. RISK ASSESSMENT V1
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'risk-assessment-v1',
    'Risk Assessment V1',
    '1.0',
    'Proceso de evaluación integral de riesgos de modelos y agents IA.

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Timer Boundary (3 días SLA), Error Boundaries
PROPORCIÓN: 70% Automatizado - 30% HITL

FLUJO:
1. ParallelGateway → Technical + Business + Compliance Risk (simultáneas)
2. BusinessRuleTask → Drools risk scoring
3. ExclusiveGateway → LOW_RISK / MEDIUM_RISK / HIGH_RISK
4. Risk Review Task (User Task si MEDIUM) + Timer SLA 3 días
5. Store Risk Assessment + Notify

TIPOS DE RIESGO: Technical, Business, Compliance, Security, Operational
SLA: 3 días para revisión de riesgos medios
CANDIDATEGROUPS: governance-admins, risk-officers'
);

-- 16. MODEL RETRAINING ORCHESTRATION V1 (GAME CHANGER)
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'model-retraining-orchestration-v1',
    'Model Retraining Orchestration V1',
    '1.0',
    'Proceso de reentrenamiento automático con A/B testing y zero-downtime.

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Timer Boundary (6h timeout), Async execution
PROPORCIÓN: 90% Automatizado - 10% HITL (solo aprobación no-urgente)

FLUJO:
1. ParallelGateway → Prepare Data + Validate Infrastructure
2. BusinessRuleTask → Drools decide: INCREMENTAL vs FULL vs SKIP
3. Execute Retraining (async, 6h timeout)
4. Evaluate New Model (auto)
5. Setup A/B Testing (Champion vs Challenger)
6. Monitor A/B Test (statistical significance)
7. ExclusiveGateway → PROMOTE / ROLLBACK
8. Atomic swap en producción

INNOVACIÓN:
- Zero-downtime retraining
- A/B testing automático
- Spot instances optimization (70% ahorro)
- Champion/Challenger pattern
- Auto-rollback si falla

COMPETENCIA: ÚNICO EN EL MERCADO
CANDIDATEGROUPS: ml-engineers, mlops-engineers, governance-admins'
);

-- 17. AI INCIDENT RESPONSE & RCA V1 (KILLER FEATURE)
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION)
VALUES (
    'incident-response-rca-v1',
    'AI Incident Response & Root Cause Analysis V1',
    '1.0',
    'Respuesta automática a incidentes con AI-powered RCA y self-healing.

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Message Start Event, Async LLM
PROPORCIÓN: 85% Automatizado - 15% HITL (solo casos nuevos)

FLUJO:
1. Message Start (incident-created)
2. ParallelGateway → Collect Logs + Metrics + Traces
3. BusinessRuleTask → Drools classify severity + probable cause
4. ServiceTask → AI Root Cause Analysis (LLM analiza logs)
5. ExclusiveGateway → AUTO_FIX / ROLLBACK / SCALE / HITL / ESCALATE
6. Apply Remediation (automático para problemas conocidos)
7. Verify Resolution
8. Update Knowledge Base (self-learning)
9. Generate Post-Mortem (auto)

INNOVACIÓN:
- LLM-powered RCA (GPT-4 analiza logs)
- Auto-remediation 85% casos
- Self-learning KB
- MTTR < 5 min (vs 30-60 min industria)
- Automatic runbook updates

COMPETENCIA: NADIE tiene AI-powered RCA + Self-healing
CANDIDATEGROUPS: mlops-engineers, on-call-team, governance-leads'
);

-- =====================================================
-- CATEGORÍAS DE PROCESOS (OPCIONAL)
-- =====================================================

-- Crear categorías si no existen
INSERT INTO BPMCATEGORIA (CATEGORIA)
VALUES 
    ('AI Governance - Aprobaciones'),
    ('AI Governance - Monitorización'),
    ('AI Governance - Detección'),
    ('AI Governance - Evaluación'),
    ('AI Governance - Automatización')
ON CONFLICT DO NOTHING;

-- Asociar procesos a categorías
UPDATE BPMMPROCES SET IDBPMCATEGORIA0 = (SELECT IDXBPMCATEGORIA FROM BPMCATEGORIA WHERE CATEGORIA = 'AI Governance - Aprobaciones')
WHERE KEYPROCESS IN ('agent-approval-v1', 'model-approval-v1');

UPDATE BPMMPROCES SET IDBPMCATEGORIA0 = (SELECT IDXBPMCATEGORIA FROM BPMCATEGORIA WHERE CATEGORIA = 'AI Governance - Monitorización')
WHERE KEYPROCESS IN ('compliance-monitoring-v1', 'drift-detection-v1', 'performance-degradation-v1');

UPDATE BPMMPROCES SET IDBPMCATEGORIA0 = (SELECT IDXBPMCATEGORIA FROM BPMCATEGORIA WHERE CATEGORIA = 'AI Governance - Detección')
WHERE KEYPROCESS IN ('bias-detection-v1');

UPDATE BPMMPROCES SET IDBPMCATEGORIA0 = (SELECT IDXBPMCATEGORIA FROM BPMCATEGORIA WHERE CATEGORIA = 'AI Governance - Evaluación')
WHERE KEYPROCESS IN ('llm-evaluation-v1', 'rag-evaluation-v1', 'dataset-quality-v1', 'ethics-review-v1', 'risk-assessment-v1', 'model-evaluation-v1');

UPDATE BPMMPROCES SET IDBPMCATEGORIA0 = (SELECT IDXBPMCATEGORIA FROM BPMCATEGORIA WHERE CATEGORIA = 'AI Governance - Automatización')
WHERE KEYPROCESS IN ('alert-response-v1', 'deployment-automation-v1', 'prompt-approval-v1', 'model-retraining-orchestration-v1', 'incident-response-rca-v1');

-- =====================================================
-- ROLES POR PROCESO (BPMROLES)
-- =====================================================

-- Agent Approval V1
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'agent-approval-v1'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers', 'governance-admins', 'governance-leads');

-- Model Approval V1
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'model-approval-v1'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers', 'senior-ml-engineers', 'governance-admins', 'governance-leads');

-- Compliance Monitoring
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'compliance-monitoring-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('compliance-officers');

-- Drift Detection
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'drift-detection-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers', 'mlops-engineers');

-- Performance Degradation
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'performance-degradation-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('mlops-engineers');

-- Bias Detection
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'bias-detection-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers', 'data-scientists', 'governance-admins', 'ml-leads');

-- Ethics Review
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'ethics-review-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers', 'governance-admins', 'ethics-committee');

-- LLM Evaluation
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'llm-evaluation-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers');

-- RAG Evaluation
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'rag-evaluation-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers');

-- Dataset Quality
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'dataset-quality-governance-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('data-engineers', 'governance-admins');

-- Risk Assessment
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'risk-assessment-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('governance-admins', 'risk-officers');

-- Model Evaluation
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'model-evaluation-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers', 'data-scientists');

-- Prompt Approval
INSERT INTO BPMROLES (IDBPMMPROCES0, IDSSOROL0)
SELECT 
    (SELECT IDXBPMMPROCES FROM BPMMPROCES WHERE KEYPROCESS = 'prompt-approval-process'),
    r.IDXSSOROL
FROM SSOROL r
WHERE r.ROL IN ('ml-engineers', 'governance-admins');

-- Alert Response (sin roles - completamente automatizado)
-- Deployment Automation (sin roles - completamente automatizado)

-- =====================================================
-- ROLES DEL SISTEMA (si no existen)
-- =====================================================

INSERT INTO SSOROL (ROL, SYSTEM) VALUES
('ml-engineers', TRUE),
('mlops-engineers', TRUE),
('data-scientists', TRUE),
('data-engineers', TRUE),
('governance-admins', TRUE),
('governance-leads', TRUE),
('compliance-officers', TRUE),
('ethics-committee', TRUE),
('senior-ml-engineers', TRUE),
('ml-leads', TRUE),
('risk-officers', TRUE)
ON CONFLICT (ROL) DO NOTHING;

-- =====================================================
-- VERIFICACIÓN
-- =====================================================

-- Ver procesos insertados
SELECT 
    KEYPROCESS,
    PROCESO,
    VERSION,
    SUBSTRING(DESCRIPCION, 1, 100) AS DESCRIPCION_PREVIEW
FROM BPMMPROCES
WHERE KEYPROCESS LIKE '%approval%' 
   OR KEYPROCESS LIKE '%detection%' 
   OR KEYPROCESS LIKE '%monitoring%'
   OR KEYPROCESS LIKE '%evaluation%'
ORDER BY KEYPROCESS;

-- Ver roles por proceso
SELECT 
    p.KEYPROCESS,
    p.PROCESO,
    STRING_AGG(r.ROL, ', ') AS ROLES
FROM BPMMPROCES p
LEFT JOIN BPMROLES br ON p.IDXBPMMPROCES = br.IDBPMMPROCES0
LEFT JOIN SSOROL r ON br.IDSSOROL0 = r.IDXSSOROL
WHERE p.KEYPROCESS LIKE '%approval%' 
   OR p.KEYPROCESS LIKE '%detection%' 
   OR p.KEYPROCESS LIKE '%monitoring%'
GROUP BY p.IDXBPMMPROCES, p.KEYPROCESS, p.PROCESO
ORDER BY p.KEYPROCESS;

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================

