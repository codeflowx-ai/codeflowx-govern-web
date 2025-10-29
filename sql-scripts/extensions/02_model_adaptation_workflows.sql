-- ============================================================================
-- PROCESOS BPMN PARA GOBIERNO DE ADAPTACIÓN DE MODELOS
-- ============================================================================
-- Proyecto: CodeFlowX Govern
-- Propósito: Procesos BPMN específicos para adapters, merge, quantización
-- Versión: 1.0
-- ============================================================================

-- =====================================================
-- PROCESO 1: ADAPTER CREATION APPROVAL (LoRA, QLoRA, etc.)
-- =====================================================
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION) VALUES (
    'adapter-creation-approval-v1',
    'Adapter Creation Approval V1',
    '1.0',
    'Proceso de aprobación para creación de adapters (LoRA, QLoRA, DoRA, etc.)

FILOSOFÍA: Adapters son PREFERIDOS sobre fine-tuning completo
VENTAJAS: Menor costo, más rápido, menos riesgo, facilita experimentación

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Timer SLA 48h
PROPORCIÓN: 70% Automatizado - 30% HITL

FLUJO:
1. Request Adapter Creation (User Task)
   - Seleccionar modelo base (obligatorio)
   - Método: LoRA / QLoRA / DoRA / IA³
   - Config: rank, alpha, target_modules, dropout
   - Dataset y objetivo

2. ParallelGateway → Validaciones simultáneas:
   - Base Model Compatibility Check (Service Task)
   - Dataset Quality Check (Service Task)
   - License Compatibility Check (Service Task)
   - Estimated Cost Calculation (Service Task)

3. BusinessRuleTask → Drools Decision:
   REGLAS:
   - rank > 64 → REQUIRE_REVIEW (riesgo de overfitting)
   - base_model no tiene licencia comercial → AUTO_REJECT
   - dataset < 1000 samples → REQUIRE_REVIEW
   - estimated_cost > $100 → REQUIRE_APPROVAL
   - ALL_PASS → AUTO_APPROVE

4. ExclusiveGateway:
   - AUTO_APPROVE → Trigger Training Job + Notify
   - REQUIRE_REVIEW → ML Engineer Review (User Task)
   - AUTO_REJECT → Notify Rejection + End

5. ML Engineer Review (User Task) [HITL 30%]
   - Review config, approve/reject/modify
   - Timer SLA: 48h
   - Candidategroups: ml-engineers, model-specialists

6. Final Approval → Register Adapter + Link to Base Model

CANDIDATEGROUPS: ml-engineers, governance-admins, model-specialists
KPIs: 
- Time to Adapter: < 2 días
- Auto-approval rate: > 70%
- Cost savings vs full fine-tuning: > 90%'
);

-- =====================================================
-- PROCESO 2: MODEL MERGE APPROVAL
-- =====================================================
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION) VALUES (
    'model-merge-approval-v1',
    'Model Merge Approval V1',
    '1.0',
    'Proceso de aprobación para merge de modelos (DARE, TIES, SLERP, etc.)

FILOSOFÍA: Merge permite combinar capacidades sin entrenar
VENTAJAS: Zero-shot capability fusion, sin GPU training

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Timer SLA 72h
PROPORCIÓN: 60% Automatizado - 40% HITL (requiere más validación)

FLUJO:
1. Request Model Merge (User Task)
   - Seleccionar 2+ modelos a mergear (obligatorio)
   - Método: DARE-TIES / Task Arithmetic / SLERP / Stock
   - Config: weights por modelo, merge density
   - Objetivo esperado

2. ParallelGateway → Validaciones:
   - Architecture Compatibility (Service Task)
     * Mismo tamaño, arquitectura, tokenizer
   - License Compatibility Check (Service Task)
     * Todas las licencias deben ser compatibles
   - Performance Baseline (Service Task)
     * Obtener métricas de modelos fuente
   - Risk Assessment (Service Task)
     * Evaluar riesgos de comportamiento inesperado

3. BusinessRuleTask → Drools Decision:
   REGLAS:
   - architectures_incompatible → AUTO_REJECT
   - license_conflict → AUTO_REJECT
   - num_models > 4 → REQUIRE_REVIEW (complejidad alta)
   - same_family_models + compatible_licenses → AUTO_APPROVE
   - DEFAULT → REQUIRE_REVIEW

4. ExclusiveGateway:
   - AUTO_APPROVE → Execute Merge + Evaluate
   - REQUIRE_REVIEW → Technical Review (User Task)
   - AUTO_REJECT → Notify + End

5. Technical Review (User Task) [HITL 40%]
   - Review compatibility, weights, expected capabilities
   - Timer SLA: 72h
   - Candidategroups: ml-engineers, research-team

6. Execute Merge (Service Task)
   - Ejecutar merge con método seleccionado
   - Auto-evaluation del modelo resultante

7. Merge Quality Review (User Task)
   - Revisar métricas del modelo merged
   - Decidir: ACCEPT / REJECT / RETRY_WITH_DIFFERENT_WEIGHTS

8. Register Merged Model → Link to Source Models

CANDIDATEGROUPS: ml-engineers, research-team, governance-admins
KPIs:
- Time to Merge: < 3 días
- Success rate: > 80%
- Capability preservation: > 95%'
);

-- =====================================================
-- PROCESO 3: MODEL QUANTIZATION APPROVAL
-- =====================================================
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION) VALUES (
    'model-quantization-approval-v1',
    'Model Quantization Approval V1',
    '1.0',
    'Proceso de aprobación para quantización de modelos (GPTQ, AWQ, GGUF, etc.)

FILOSOFÍA: Democratizar acceso reduciendo requisitos de hardware
VENTAJAS: Menor VRAM, más rápido, deployment en edge

ELEMENTOS: ParallelGateway, BusinessRuleTask (Drools), Timer SLA 24h
PROPORCIÓN: 80% Automatizado - 20% HITL

FLUJO:
1. Request Quantization (User Task)
   - Seleccionar modelo base (obligatorio)
   - Método: GPTQ / AWQ / GGUF / bitsandbytes
   - Bits: 2 / 4 / 8
   - Config específica: group_size, calibration_dataset

2. ParallelGateway → Validaciones:
   - Model Size Check (Service Task)
     * Calcular ahorro esperado de VRAM/storage
   - Performance Impact Estimation (Service Task)
     * Estimar degradación esperada por bits
   - License Check (Service Task)
   - Hardware Compatibility (Service Task)
     * Verificar compatibilidad con targets (CPU, GPU, edge)

3. BusinessRuleTask → Drools Decision:
   REGLAS:
   - bits = 2 → REQUIRE_REVIEW (degradación alta)
   - model_size > 70B AND bits = 4 → AUTO_APPROVE (caso común)
   - estimated_degradation > 10% → REQUIRE_REVIEW
   - calibration_dataset < 512 samples → REQUIRE_REVIEW
   - DEFAULT bits=8 + large_model → AUTO_APPROVE

4. ExclusiveGateway:
   - AUTO_APPROVE → Execute Quantization
   - REQUIRE_REVIEW → Quality Review (User Task)
   - AUTO_REJECT → Notify + End

5. Execute Quantization (Service Task)
   - Ejecutar quantización
   - Benchmarks automáticos (latency, memory, perplexity)

6. Quality Gate Check (Service Task)
   - Comparar métricas: quantized vs original
   - Umbral: < 5% degradación para auto-pass

7. ExclusiveGateway:
   - degradation_acceptable → Register Quantized Model
   - degradation_too_high → Manual Review (User Task)

8. Manual Quality Review (User Task) [HITL 20%]
   - Decidir si degradación es aceptable para use case
   - Timer SLA: 24h

CANDIDATEGROUPS: ml-engineers, infra-team, governance-admins
KPIs:
- Time to Quantization: < 1 día
- VRAM Savings: > 50%
- Performance degradation: < 5%
- Auto-approval rate: > 80%'
);

-- =====================================================
-- PROCESO 4: FINE-TUNING APPROVAL (Completo, debe justificarse)
-- =====================================================
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION) VALUES (
    'finetuning-approval-v1',
    'Fine-Tuning Approval V1',
    '1.0',
    'Proceso de aprobación para fine-tuning COMPLETO (requiere justificación)

FILOSOFÍA: Fine-tuning completo es ÚLTIMO RECURSO
DEBE JUSTIFICAR: ¿Por qué no usar adapter, merge o quantización?

ELEMENTOS: BusinessRuleTask (Drools ESTRICTO), Timer SLA 5 días
PROPORCIÓN: 40% Automatizado - 60% HITL (más restrictivo)

FLUJO:
1. Request Fine-Tuning (User Task)
   - Seleccionar modelo base
   - Dataset (min 10k samples recomendado)
   - **OBLIGATORIO**: Justificar por qué NO adapter
   - Estimación de costo ($$$)

2. Adapter Feasibility Check (Service Task)
   - Evaluar automáticamente si adapter sería suficiente
   - Output: ADAPTER_SUFFICIENT / FINETUNING_JUSTIFIED

3. ExclusiveGateway:
   - ADAPTER_SUFFICIENT → Suggest Adapter Creation + End
   - FINETUNING_JUSTIFIED → Continue

4. BusinessRuleTask → Drools STRICT Decision:
   REGLAS ESTRICTAS:
   - no_adapter_justification → AUTO_REJECT
   - dataset < 5000 samples → AUTO_REJECT
   - estimated_cost > $1000 → REQUIRE_BUDGET_APPROVAL
   - base_model_license_incompatible → AUTO_REJECT
   - use_case_critical + dataset_quality_high → REQUIRE_REVIEW

5. Budget Approval (User Task si costo alto)
   - Aprobar presupuesto
   - Candidategroups: finance-team, project-managers

6. Technical Deep Review (User Task) [HITL 60%]
   - ML Engineer + Governance Admin
   - Validar: dataset quality, justificación, alternativas
   - Timer SLA: 5 días
   - Candidategroups: ml-engineers, governance-admins, research-team

7. Final Decision:
   - APPROVED → Trigger Training Experiment
   - REJECTED → Suggest alternatives (adapter, merge)
   - CONDITIONAL → Require dataset improvement

CANDIDATEGROUPS: ml-engineers, governance-admins, finance-team, research-team
KPIs:
- Rejection rate: > 60% (forzar uso de adapters)
- Time to decision: < 5 días
- Cost per approved fine-tuning: Tracked
- Adapter adoption rate: Target > 70%'
);

-- =====================================================
-- PROCESO 5: ADAPTATION STRATEGY RECOMMENDATION (NUEVO - IA)
-- =====================================================
INSERT INTO BPMMPROCES (KEYPROCESS, PROCESO, VERSION, DESCRIPCION) VALUES (
    'adaptation-strategy-recommendation-v1',
    'Adaptation Strategy Recommendation V1',
    '1.0',
    'Proceso INTELIGENTE que recomienda la mejor estrategia de adaptación

FILOSOFÍA: La plataforma AYUDA al usuario a elegir la mejor opción
OBJETIVO: Maximizar eficiencia, minimizar costo

ELEMENTOS: BusinessRuleTask (Drools), AI Service Delegate
PROPORCIÓN: 95% Automatizado - 5% HITL

FLUJO:
1. Submit Adaptation Request (User Task)
   INPUT:
   - Use case description
   - Target task (Q&A, summarization, code, etc.)
   - Dataset (optional, para análisis)
   - Performance requirements
   - Budget constraints
   - Deployment target (cloud, edge, etc.)

2. Analyze Requirements (Service Task - AI)
   - LLM analiza use case
   - Clasifica tarea
   - Evalúa complejidad

3. Search Base Models (Service Task)
   - Buscar modelos base compatibles en catálogo
   - Filtrar por licencia, tamaño, performance

4. ParallelGateway → Evaluate Strategies:
   - Evaluate Adapter Strategy (Service Task)
     * Costo estimado, tiempo, params trainable
   - Evaluate Merge Strategy (Service Task)
     * Modelos candidatos, compatibilidad
   - Evaluate Quantization Strategy (Service Task)
     * Savings, degradación estimada
   - Evaluate Fine-Tuning Strategy (Service Task)
     * Costo, tiempo, requerimientos

5. BusinessRuleTask → Rank Strategies (Drools):
   CRITERIOS:
   - cost_weight = 0.3
   - time_weight = 0.2
   - performance_weight = 0.3
   - feasibility_weight = 0.2
   
   SCORING:
   - Adapter → +50 points (preferido)
   - Merge → +40 points
   - Quantization → +30 points
   - Fine-Tuning → +10 points (penalizado)
   
   OUTPUT: Ranked list of strategies

6. Present Recommendations (User Task)
   - Mostrar 3 mejores opciones con:
     * Costo estimado ($)
     * Tiempo estimado (days)
     * Performance esperado (%)
     * Pros/Cons
   - Usuario elige opción

7. ExclusiveGateway → Route to specific process:
   - ADAPTER → Start ''adapter-creation-approval-v1''
   - MERGE → Start ''model-merge-approval-v1''
   - QUANTIZATION → Start ''model-quantization-approval-v1''
   - FINE_TUNING → Start ''finetuning-approval-v1''

CANDIDATEGROUPS: ml-engineers, data-scientists, all-users (democratizar)
KPIs:
- Adapter recommendation rate: Target > 60%
- User satisfaction: > 85%
- Cost savings vs naive fine-tuning: > 80%
- Time to recommendation: < 5 minutos'
);

-- =====================================================
-- TABLA AUXILIAR: ADAPTATION STRATEGY HISTORY
-- =====================================================
CREATE TABLE IF NOT EXISTS MODADAPTATIONSTRATEGIES (
    iduuid UUID UNIQUE,
    IDXADAPTATIONSTRATEGY BIGSERIAL PRIMARY KEY,
    
    -- Request info
    MODUSECASE TEXT NOT NULL,
    MODTARGETTASK VARCHAR(100),
    MODDATASETID BIGINT,
    MODPERFORMANCEREQUIREMENTS JSONB,
    MODBUDGETCONSTRAINTS JSONB,
    MODDEPLOYMENTTARGET VARCHAR(100),
    
    -- Recommendations generated
    MODRECOMMENDATIONS JSONB NOT NULL, -- Array de estrategias rankeadas
    MODSELECTEDSTRATEGY VARCHAR(50), -- ADAPTER / MERGE / QUANTIZATION / FINETUNING
    MODSELECTEDREASON TEXT,
    
    -- Outcome
    MODRESULTINGMODELID BIGINT, -- FK a MODMODELS
    MODACTUALCOST DECIMAL,
    MODACTUALTIME INTEGER, -- days
    MODACTUALPERFORMANCE DECIMAL,
    
    -- Metadata
    MODCREATEDBY VARCHAR(255) NOT NULL,
    MODCREATEDAT TIMESTAMP NOT NULL,
    MODUPDATEDAT TIMESTAMP,
    
    FOREIGN KEY (MODRESULTINGMODELID) REFERENCES MODMODELS(IDXMODEL) ON DELETE SET NULL
);

CREATE INDEX idx_adaptation_strategies_usecase ON MODADAPTATIONSTRATEGIES USING gin(to_tsvector('english', MODUSECASE));
CREATE INDEX idx_adaptation_strategies_selected ON MODADAPTATIONSTRATEGIES(MODSELECTEDSTRATEGY);

COMMENT ON TABLE MODADAPTATIONSTRATEGIES IS 'Historial de estrategias de adaptación recomendadas y seleccionadas - para ML sobre recomendaciones';

-- =====================================================
-- VISTA: DASHBOARD DE ESTRATEGIAS DE ADAPTACIÓN
-- =====================================================
CREATE OR REPLACE VIEW V_ADAPTATION_STRATEGIES_DASHBOARD AS
SELECT 
    COUNT(*) AS total_requests,
    COUNT(*) FILTER (WHERE MODSELECTEDSTRATEGY = 'ADAPTER') AS adapter_selected,
    COUNT(*) FILTER (WHERE MODSELECTEDSTRATEGY = 'MERGE') AS merge_selected,
    COUNT(*) FILTER (WHERE MODSELECTEDSTRATEGY = 'QUANTIZATION') AS quantization_selected,
    COUNT(*) FILTER (WHERE MODSELECTEDSTRATEGY = 'FINETUNING') AS finetuning_selected,
    
    -- Porcentajes
    ROUND(100.0 * COUNT(*) FILTER (WHERE MODSELECTEDSTRATEGY = 'ADAPTER') / NULLIF(COUNT(*), 0), 2) AS adapter_percentage,
    
    -- Savings promedio
    AVG(MODACTUALCOST) FILTER (WHERE MODSELECTEDSTRATEGY != 'FINETUNING') AS avg_cost_efficient,
    AVG(MODACTUALCOST) FILTER (WHERE MODSELECTEDSTRATEGY = 'FINETUNING') AS avg_cost_finetuning,
    
    -- Time to market
    AVG(MODACTUALTIME) FILTER (WHERE MODSELECTEDSTRATEGY = 'ADAPTER') AS avg_time_adapter,
    AVG(MODACTUALTIME) FILTER (WHERE MODSELECTEDSTRATEGY = 'FINETUNING') AS avg_time_finetuning,
    
    NOW() AS generated_at
FROM MODADAPTATIONSTRATEGIES
WHERE MODRESULTINGMODELID IS NOT NULL; -- Solo requests completados

COMMENT ON VIEW V_ADAPTATION_STRATEGIES_DASHBOARD IS 'KPIs de estrategias de adaptación - Medir adopción de adapters vs fine-tuning';

