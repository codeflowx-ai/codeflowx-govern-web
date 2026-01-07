# ⚙️ GUÍA DE CONFIGURACIÓN: PROCESOS, REGLAS Y MODELOS POR AGENTE

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Governance Managers, AI Engineers, Administradores
**Proyecto:** CodeFlowX AI Governance Platform

---

## 🎯 PROPÓSITO

Esta guía define cómo configurar procesos BPMN, reglas Drools y modelos/algoritmos específicos para cada agente, permitiendo personalización según tipo de agente, nivel de riesgo, contexto o requisitos específicos.

---

## 📊 ARQUITECTURA DE CONFIGURACIÓN

### Flujo de Configuración

```
Agente Registrado (AGTAGENTS)
    ↓
Configuración por Agente (cor_agent_config)
    ├── Procesos BPMN (cor_agent_process_config)
    ├── Reglas Drools (cor_agent_rule_config)
    └── Modelos/Algoritmos (cor_agent_algorithm_config)
    ↓
Runtime del Agente
    ├── Lanza procesos según configuración
    ├── Ejecuta reglas según configuración
    └── Usa modelos según configuración
```

---

## 🗄️ MODELO DE DATOS

### Tabla de Configuración de Procesos por Agente

```sql
CREATE TABLE cor_agent_process_config (
    id BIGSERIAL PRIMARY KEY,
    agent_uuid VARCHAR(36) NOT NULL,  -- FK a AGTAGENTS.agtuuid
    process_type VARCHAR(100) NOT NULL,  -- APPROVAL, CERTIFICATION, RETIREMENT, GOVERNANCE_POLICY, COMPLIANCE_ASSESSMENT, ETHICS_ASSESSMENT, DECISION_REVIEW, ROLLBACK_APPROVAL
    process_key VARCHAR(100) NOT NULL,  -- agent-approval-v1, agent-certification-v1, etc.
    conditions JSONB,  -- Condiciones para lanzar el proceso
    priority INTEGER DEFAULT 0,  -- Mayor prioridad = más preferido
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT fk_agent_process FOREIGN KEY (agent_uuid) REFERENCES AGTAGENTS(agtuuid),
    CONSTRAINT uk_agent_process UNIQUE (agent_uuid, process_type, process_key)
);

CREATE INDEX idx_agent_process_config_agent ON cor_agent_process_config(agent_uuid, process_type, is_active);
```

### Tabla de Configuración de Reglas por Agente

```sql
CREATE TABLE cor_agent_rule_config (
    id BIGSERIAL PRIMARY KEY,
    agent_uuid VARCHAR(36) NOT NULL,
    rule_type VARCHAR(50) NOT NULL,  -- DROOLS, LLM_PROMPT
    rule_id VARCHAR(100) NOT NULL,  -- ID de regla Drools o prompt LLM
    execution_order INTEGER NOT NULL,  -- Orden de ejecución
    priority INTEGER DEFAULT 0,
    conditions JSONB,  -- Condiciones para aplicar la regla
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_agent_rule FOREIGN KEY (agent_uuid) REFERENCES AGTAGENTS(agtuuid)
);

CREATE INDEX idx_agent_rule_config_agent ON cor_agent_rule_config(agent_uuid, rule_type, is_active);
```

### Tabla de Configuración de Modelos/Algoritmos por Agente

```sql
CREATE TABLE cor_agent_algorithm_config (
    id BIGSERIAL PRIMARY KEY,
    agent_uuid VARCHAR(36) NOT NULL,
    service VARCHAR(100) NOT NULL,  -- bias-detection, llm-evaluation, agent-monitoring, etc.
    algorithm_name VARCHAR(100) NOT NULL,  -- statistical_parity, hallucination_detection, etc.
    context VARCHAR(100),  -- agent-evaluation, production, etc.
    configuration JSONB,  -- Configuración específica del algoritmo
    priority INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_agent_algorithm FOREIGN KEY (agent_uuid) REFERENCES AGTAGENTS(agtuuid)
);

CREATE INDEX idx_agent_algorithm_config_agent ON cor_agent_algorithm_config(agent_uuid, service, is_active);
```

---

## 🔧 CONFIGURACIÓN DE PROCESOS BPMN

### Tipos de Procesos por Agente

| Tipo de Proceso | Process Key | Cuándo se Lanza |
|-----------------|-------------|-----------------|
| **APPROVAL** | `agent-approval-v1` | Al crear solicitud de aprobación |
| **CERTIFICATION** | `agent-certification-v1` | Al solicitar certificación |
| **RETIREMENT** | `agent-retirement-v1` | Al crear solicitud de retiro |
| **GOVERNANCE_POLICY** | `agent-governance-policy-v1` | Al enviar política para aprobación |
| **COMPLIANCE_ASSESSMENT** | `agent-compliance-assessment-v1` | Al enviar evaluación de cumplimiento |
| **ETHICS_ASSESSMENT** | `agent-ethics-assessment-v1` | Al enviar evaluación ética |
| **DECISION_REVIEW** | `agent-decision-review-v1` | Cuando decisión requiere revisión HITL |
| **ROLLBACK_APPROVAL** | `agent-rollback-approval-v1` | Cuando reversión requiere aprobación HITL |

### Ejemplo de Configuración

```sql
-- Agente de alto riesgo: Usa proceso de aprobación estándar
INSERT INTO cor_agent_process_config (agent_uuid, process_type, process_key, conditions, priority)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'APPROVAL',
    'agent-approval-v1',
    '{"risk_level": "HIGH", "requires_ethics_review": true}'::jsonb,
    10
);

-- Agente de bajo riesgo: Usa proceso simplificado
INSERT INTO cor_agent_process_config (agent_uuid, process_type, process_key, conditions, priority)
VALUES (
    '660e8400-e29b-41d4-a716-446655440001',
    'APPROVAL',
    'agent-approval-simplified-v1',
    '{"risk_level": "LOW", "auto_approve_threshold": 0.9}'::jsonb,
    10
);

-- Agente financiero: Proceso con revisión regulatoria adicional
INSERT INTO cor_agent_process_config (agent_uuid, process_type, process_key, conditions, priority)
VALUES (
    '770e8400-e29b-41d4-a716-446655440002',
    'APPROVAL',
    'agent-approval-financial-v1',
    '{"domain": "FINANCE", "requires_regulatory_review": true}'::jsonb,
    10
);
```

### Lógica de Selección de Proceso

```java
@Service
public class AgentProcessConfigurationService {

    @Autowired
    private AgentProcessConfigRepository processConfigRepository;

    /**
     * Obtiene el proceso BPMN a lanzar para un agente y tipo de proceso
     */
    public String getProcessKey(String agentUuid, ProcessType processType, Map<String, Object> context) {
        // 1. Buscar configuración específica del agente
        List<AgentProcessConfig> configs = processConfigRepository.findByAgentUuidAndProcessTypeAndIsActiveTrue(
            agentUuid,
            processType
        );

        // 2. Evaluar condiciones y seleccionar proceso
        for (AgentProcessConfig config : configs.stream()
            .sorted(Comparator.comparing(AgentProcessConfig::getPriority).reversed())
            .collect(Collectors.toList())) {

            if (evaluateConditions(config.getConditions(), context)) {
                return config.getProcessKey();
            }
        }

        // 3. Fallback a proceso por defecto
        return getDefaultProcess(processType);
    }

    private boolean evaluateConditions(JsonNode conditions, Map<String, Object> context) {
        // Evaluar condiciones JSON contra contexto
        // Ejemplo: {"risk_level": "HIGH"} -> context.get("risk_level") == "HIGH"
        return true;  // Implementar lógica de evaluación
    }

    private String getDefaultProcess(ProcessType processType) {
        // Procesos por defecto
        return switch (processType) {
            case APPROVAL -> "agent-approval-v1";
            case CERTIFICATION -> "agent-certification-v1";
            case RETIREMENT -> "agent-retirement-v1";
            // ... otros tipos
        };
    }
}
```

---

## 🔧 CONFIGURACIÓN DE REGLAS

### Reglas Drools por Agente

Ya existe configuración en `ReviewRulesTab`, pero se puede extender:

```sql
-- Reglas de revisión para agente de alto riesgo
INSERT INTO cor_agent_rule_config (agent_uuid, rule_type, rule_id, execution_order, priority, conditions)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'DROOLS', 'low-confidence-rule', 1, 10, '{"min_confidence": 0.6}'::jsonb),
    ('550e8400-e29b-41d4-a716-446655440000', 'DROOLS', 'high-value-rule', 2, 10, '{"min_value": 10000}'::jsonb),
    ('550e8400-e29b-41d4-a716-446655440000', 'LLM_PROMPT', 'prompt-ethics-review', 3, 10, NULL);
```

### Reglas de Negocio por Tipo de Proceso

```sql
-- Reglas para proceso de aprobación
INSERT INTO cor_agent_rule_config (agent_uuid, rule_type, rule_id, execution_order, priority, conditions)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'DROOLS', 'approval-risk-assessment', 1, 10,
     '{"process_type": "APPROVAL"}'::jsonb),
    ('550e8400-e29b-41d4-a716-446655440000', 'LLM_PROMPT', 'approval-ethics-check', 2, 10,
     '{"process_type": "APPROVAL"}'::jsonb);
```

---

## 🔧 CONFIGURACIÓN DE MODELOS/ALGORITMOS

### Algoritmos por Agente y Contexto

```sql
-- Agente de detección de fraude: Usa algoritmos específicos
INSERT INTO cor_agent_algorithm_config (agent_uuid, service, algorithm_name, context, configuration, priority)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'bias-detection', 'statistical_parity', 'agent-evaluation',
     '{"protected_attribute": "gender", "threshold": 0.1}'::jsonb, 10),
    ('550e8400-e29b-41d4-a716-446655440000', 'llm-evaluation', 'hallucination_detection', 'production',
     '{"confidence_threshold": 0.8}'::jsonb, 10),
    ('550e8400-e29b-41d4-a716-446655440000', 'agent-monitoring', 'execution_analysis', 'production',
     '{"min_success_rate": 0.95}'::jsonb, 10);
```

### Lógica de Selección de Algoritmo

```java
@Service
public class AgentAlgorithmConfigurationService {

    @Autowired
    private AgentAlgorithmConfigRepository algorithmConfigRepository;

    /**
     * Obtiene algoritmo configurado para un agente y servicio
     */
    public AlgorithmConfig getAlgorithm(String agentUuid, String service, String context) {
        // 1. Buscar configuración específica del agente
        List<AgentAlgorithmConfig> configs = algorithmConfigRepository.findByAgentUuidAndServiceAndContextAndIsActiveTrue(
            agentUuid,
            service,
            context
        );

        if (!configs.isEmpty()) {
            // Retornar algoritmo con mayor prioridad
            return configs.stream()
                .max(Comparator.comparing(AgentAlgorithmConfig::getPriority))
                .map(this::toAlgorithmConfig)
                .orElse(null);
        }

        // 2. Fallback a configuración global
        return getGlobalAlgorithmConfig(service, context);
    }
}
```

---

## 🎯 CONFIGURACIÓN POR TIPO DE AGENTE

### Plantillas de Configuración

```java
public enum AgentType {
    LOW_RISK,
    MEDIUM_RISK,
    HIGH_RISK,
    FINANCIAL,
    HEALTHCARE,
    CUSTOMER_SERVICE
}

@Service
public class AgentConfigurationTemplateService {

    /**
     * Aplica plantilla de configuración según tipo de agente
     */
    public void applyTemplate(String agentUuid, AgentType agentType) {
        switch (agentType) {
            case LOW_RISK:
                applyLowRiskTemplate(agentUuid);
                break;
            case HIGH_RISK:
                applyHighRiskTemplate(agentUuid);
                break;
            case FINANCIAL:
                applyFinancialTemplate(agentUuid);
                break;
            // ... otros tipos
        }
    }

    private void applyHighRiskTemplate(String agentUuid) {
        // Procesos
        createProcessConfig(agentUuid, ProcessType.APPROVAL, "agent-approval-v1",
            Map.of("requires_ethics_review", true, "requires_compliance_review", true));

        // Reglas
        createRuleConfig(agentUuid, RuleType.DROOLS, "low-confidence-rule", 1);
        createRuleConfig(agentUuid, RuleType.DROOLS, "high-value-rule", 2);
        createRuleConfig(agentUuid, RuleType.LLM_PROMPT, "ethics-review-prompt", 3);

        // Algoritmos
        createAlgorithmConfig(agentUuid, "bias-detection", "statistical_parity", "agent-evaluation");
        createAlgorithmConfig(agentUuid, "llm-evaluation", "hallucination_detection", "production");
    }
}
```

---

## 🔄 INTEGRACIÓN CON RUNTIME

### Lanzamiento de Procesos

```java
@Service
public class AgentProcessLauncherService {

    @Autowired
    private AgentProcessConfigurationService processConfigService;

    @Autowired
    private BpmnWorkflowClient bpmnClient;

    /**
     * Lanza proceso BPMN configurado para un agente
     */
    public void launchProcess(String agentUuid, ProcessType processType, Map<String, Object> variables) {
        // 1. Obtener proceso configurado
        String processKey = processConfigService.getProcessKey(agentUuid, processType, variables);

        // 2. Agregar variables de configuración
        Map<String, Object> processVariables = new HashMap<>(variables);
        processVariables.put("agentUuid", agentUuid);
        processVariables.put("processType", processType.name());

        // 3. Lanzar proceso
        bpmnClient.startProcess(processKey, processVariables);
    }
}
```

### Uso de Algoritmos Configurados

```java
@Service
public class AgentEvaluationService {

    @Autowired
    private AgentAlgorithmConfigurationService algorithmConfigService;

    @Autowired
    private AIGovernanceClient aiGovernanceClient;

    /**
     * Evalúa agente usando algoritmos configurados
     */
    public BiasAnalysisResponse evaluateBias(String agentUuid, BiasAnalysisRequest request) {
        // 1. Obtener algoritmo configurado
        AlgorithmConfig config = algorithmConfigService.getAlgorithm(
            agentUuid,
            "bias-detection",
            "agent-evaluation"
        );

        // 2. Agregar configuración al request
        if (config != null) {
            request.setAlgorithm(config.getAlgorithmName());
            request.setConfig(config.getConfiguration());
        }

        // 3. Llamar microservicio
        return aiGovernanceClient.biasDetection().analyzeBias(request);
    }
}
```

---

## 📋 INTERFAZ DE CONFIGURACIÓN

### Tab en Detalle de Agente

Crear nueva pestaña "Configuración" en el detalle del agente:

```typescript
// app/(app)/governance/agents/registry/[id]/ConfigurationTab.tsx

export default function ConfigurationTab({ agentId, agentUuid }: ConfigurationTabProps) {
  return (
    <Tabs defaultValue="processes">
      <TabsList>
        <TabsTrigger value="processes">Procesos BPMN</TabsTrigger>
        <TabsTrigger value="rules">Reglas</TabsTrigger>
        <TabsTrigger value="algorithms">Modelos/Algoritmos</TabsTrigger>
      </TabsList>

      <TabsContent value="processes">
        <ProcessConfigurationPanel agentUuid={agentUuid} />
      </TabsContent>

      <TabsContent value="rules">
        <RulesConfigurationPanel agentUuid={agentUuid} />
      </TabsContent>

      <TabsContent value="algorithms">
        <AlgorithmsConfigurationPanel agentUuid={agentUuid} />
      </TabsContent>
    </Tabs>
  );
}
```

---

## 📝 EJEMPLOS DE CONFIGURACIÓN

### Ejemplo 1: Agente de Bajo Riesgo

```sql
-- Proceso simplificado de aprobación
INSERT INTO cor_agent_process_config VALUES (
    'agent-uuid-1', 'APPROVAL', 'agent-approval-simplified-v1',
    '{"auto_approve_threshold": 0.9}'::jsonb, 10, true
);

-- Reglas básicas
INSERT INTO cor_agent_rule_config VALUES
    ('agent-uuid-1', 'DROOLS', 'basic-confidence-rule', 1, 10, NULL);

-- Algoritmos estándar
INSERT INTO cor_agent_algorithm_config VALUES
    ('agent-uuid-1', 'bias-detection', 'statistical_parity', 'agent-evaluation', NULL, 10);
```

### Ejemplo 2: Agente de Alto Riesgo Financiero

```sql
-- Proceso completo con todas las revisiones
INSERT INTO cor_agent_process_config VALUES
    ('agent-uuid-2', 'APPROVAL', 'agent-approval-financial-v1',
     '{"requires_ethics_review": true, "requires_compliance_review": true, "requires_regulatory_review": true}'::jsonb,
     10, true);

-- Múltiples reglas de revisión
INSERT INTO cor_agent_rule_config VALUES
    ('agent-uuid-2', 'DROOLS', 'low-confidence-rule', 1, 10, NULL),
    ('agent-uuid-2', 'DROOLS', 'high-value-rule', 2, 10, '{"min_value": 1000}'::jsonb),
    ('agent-uuid-2', 'DROOLS', 'financial-risk-rule', 3, 10, NULL),
    ('agent-uuid-2', 'LLM_PROMPT', 'ethics-review-prompt', 4, 10, NULL),
    ('agent-uuid-2', 'LLM_PROMPT', 'compliance-check-prompt', 5, 10, NULL);

-- Algoritmos avanzados
INSERT INTO cor_agent_algorithm_config VALUES
    ('agent-uuid-2', 'bias-detection', 'equalized_odds', 'agent-evaluation',
     '{"protected_attribute": "gender", "threshold": 0.05}'::jsonb, 10),
    ('agent-uuid-2', 'llm-evaluation', 'hallucination_detection', 'production',
     '{"confidence_threshold": 0.9}'::jsonb, 10);
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Backend

- [ ] Crear tablas de configuración (`cor_agent_process_config`, `cor_agent_rule_config`, `cor_agent_algorithm_config`)
- [ ] Crear entidades JPA para configuración
- [ ] Crear repositorios para configuración
- [ ] Crear servicios de configuración (`AgentProcessConfigurationService`, `AgentAlgorithmConfigurationService`)
- [ ] Actualizar `AgentProcessLauncherService` para usar configuración
- [ ] Actualizar servicios de evaluación para usar algoritmos configurados
- [ ] Crear endpoints REST para gestión de configuración

### Frontend

- [ ] Crear tab "Configuración" en detalle de agente
- [ ] Crear panel de configuración de procesos BPMN
- [ ] Crear panel de configuración de reglas
- [ ] Crear panel de configuración de algoritmos
- [ ] Implementar selección de procesos disponibles
- [ ] Implementar selección de reglas disponibles
- [ ] Implementar selección de algoritmos disponibles
- [ ] Implementar plantillas por tipo de agente

---

## 🚨 MEJORES PRÁCTICAS

1. **Configuración por defecto:**
   - Siempre tener configuración por defecto si no hay específica
   - Aplicar plantillas según tipo de agente al crear

2. **Priorización:**
   - Configuraciones específicas tienen mayor prioridad
   - Evaluar condiciones en orden de prioridad

3. **Validación:**
   - Validar que el proceso BPMN existe antes de configurar
   - Validar que la regla existe antes de configurar
   - Validar que el algoritmo existe antes de configurar

4. **Auditoría:**
   - Registrar quién configuró y cuándo
   - Mantener historial de cambios de configuración

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** ⚠️ Pendiente de implementación
