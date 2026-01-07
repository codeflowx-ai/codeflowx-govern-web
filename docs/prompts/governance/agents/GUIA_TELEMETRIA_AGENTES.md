# 📡 GUÍA DE TELEMETRÍA PARA AGENTES DE IA

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Desarrolladores, AI Engineers, Agentes de IA
**Proyecto:** CodeFlowX AI Governance Platform

---

## 🎯 PROPÓSITO

Esta guía define cómo el módulo de telemetría debe procesar eventos recibidos de agentes de IA, incluyendo:
- Ejecución de reglas Drools
- Llamadas a LLMs para razonamiento complejo
- Schema de eventos
- Flujo de procesamiento
- Integración con el sistema de gobierno

**IMPORTANTE:** La telemetría se almacena en una **base de datos separada** de la base de datos de negocio por cuestiones de rendimiento. Se estiman aproximadamente **10 millones de eventos diarios**, por lo que no es viable almacenarlos en el modelo de gestión (PostgreSQL de negocio). Los eventos se relacionan con agentes mediante el `agent_uuid` como identificador común.

---

## 📊 ARQUITECTURA DE TELEMETRÍA

### Flujo General

```
Agente de IA (Runtime)
    ↓
Evento de Telemetría (JSON)
    ↓
Módulo de Telemetría (Backend Java)
    ↓
1. Validar Schema
2. Ejecutar Reglas Drools
3. Evaluar Necesidad de Revisión HITL
4. Llamar LLMs si es necesario
5. Crear Registros en Base de Datos de Telemetría
    ↓
Base de Datos de Telemetría (Time-Series DB / Optimizada)
    ↓
Frontend (Visualización / Agregaciones)
```

**IMPORTANTE:** La telemetría se almacena en una **base de datos separada** de la base de datos de negocio por cuestiones de rendimiento. Se estiman aproximadamente **10 millones de eventos diarios**, por lo que no es viable almacenarlos en el modelo de gestión (PostgreSQL de negocio).

### Arquitectura de Bases de Datos

```
Base de Datos de Negocio (PostgreSQL)
├── AGTAGENTS (agentes registrados)
├── AGTAPPROVALS (aprobaciones)
├── AGTCERTIFICATIONS (certificaciones)
└── ... (modelo de gestión)

Base de Datos de Telemetría (Time-Series / Optimizada)
├── agent_interactions (eventos de interacción)
├── agent_decisions (eventos de decisión)
├── agent_alerts (eventos de alerta)
└── ... (eventos de telemetría)

Relación mediante identificadores:
- agent_uuid: Identifica el agente (FK a AGTAGENTS)
- event_id: Identificador único del evento
- timestamp: Marca temporal del evento
```

### Características de la Base de Datos de Telemetría

- **Optimizada para escritura masiva:** Alta capacidad de inserción (10M eventos/día)
- **Time-series database:** Optimizada para consultas temporales
- **Retención configurable:** Datos históricos con políticas de retención
- **Agregaciones:** Pre-cálculo de métricas para consultas rápidas
- **Particionamiento:** Por fecha/agente para mejor rendimiento
- **Índices optimizados:** Sobre `agent_uuid`, `timestamp`, `event_type`

---

## 💾 BASE DE DATOS DE TELEMETRÍA

### Características

La base de datos de telemetría es una **base de datos separada** optimizada para:

- **Alta escritura:** 10 millones de eventos diarios (~115 eventos/segundo promedio, picos mucho mayores)
- **Time-series:** Optimizada para consultas temporales y agregaciones
- **Particionamiento:** Por fecha y/o agente para mejor rendimiento
- **Retención:** Políticas configurables de retención de datos
- **Escalabilidad:** Horizontal para manejar crecimiento

### Tecnologías Recomendadas

- **TimescaleDB:** Extensión de PostgreSQL optimizada para time-series
- **InfluxDB:** Base de datos time-series nativa
- **ClickHouse:** Columnar optimizado para analytics
- **Cassandra:** Distribuida para alta disponibilidad

### Relación con Base de Datos de Negocio

```
Base de Datos de Negocio (PostgreSQL)
├── AGTAGENTS
│   └── agtuuid (PK) ←──┐
│                       │
Base de Datos de Telemetría (Time-Series DB)  │
├── agent_interactions  │
│   └── agent_uuid ─────┘ (identificador común, NO FK física)
├── agent_decisions
│   └── agent_uuid ─────┘
└── agent_alerts
    └── agent_uuid ─────┘
```

**Características de la relación:**
- **NO hay Foreign Keys físicas** entre bases de datos
- **Relación mediante `agent_uuid`** como identificador común
- **Validación de existencia** del agente antes de procesar eventos
- **Cacheo de `agent_name`** en eventos para consultas rápidas sin JOIN

### Sincronización Selectiva

Solo eventos importantes se sincronizan a la base de datos de negocio:

1. **Decisiones que requieren revisión HITL:**
   - Se crea registro resumido en `AGTDECISIONS` (base de negocio)
   - El evento completo permanece en base de telemetría

2. **Alertas críticas:**
   - Se crea registro en `AGTALERTS` (base de negocio)
   - El evento completo permanece en base de telemetría

3. **Eventos reconocidos/resueltos:**
   - Se actualiza estado en base de negocio para auditoría
   - El evento completo permanece en base de telemetría

### Consultas y Agregaciones

**Desde Base de Datos de Telemetría:**
- Consultas de eventos individuales
- Agregaciones temporales (por hora, día, semana)
- Análisis de tendencias
- Búsquedas por `agent_uuid`, `timestamp`, `event_type`

**Desde Base de Datos de Negocio:**
- Información de agentes (nombre, estado, configuración)
- Eventos sincronizados (decisiones HITL, alertas críticas)
- Auditoría y cumplimiento

**JOIN entre bases de datos:**
- Se realiza en la capa de aplicación (Backend Java)
- No hay JOINs directos entre bases de datos
- Se consulta base de negocio para obtener info del agente
- Se consulta base de telemetría para obtener eventos

---

## 📋 SCHEMA DE EVENTOS

### 1. Evento de Interacción (`AgentInteraction`)

**Cuándo se envía:**
- Cada vez que un usuario interactúa con un agente
- Al finalizar la interacción (éxito o fallo)

**Schema JSON:**
```json
{
  "eventType": "AGENT_INTERACTION",
  "timestamp": "2025-01-15T10:30:00Z",
  "agentUuid": "550e8400-e29b-41d4-a716-446655440000",
  "agentName": "Customer Support Agent",
  "userId": "user-123",
  "sessionId": "session-456",
  "interaction": {
    "input": "¿Cuál es el estado de mi pedido?",
    "output": "Su pedido está en tránsito y llegará mañana.",
    "success": true,
    "error": null
  },
  "metrics": {
    "durationMs": 2340,
    "tokensUsed": {
      "input": 15,
      "output": 12,
      "total": 27
    },
    "cost": {
      "currency": "USD",
      "amount": 0.00054
    },
    "modelUsed": "gpt-4",
    "provider": "openai"
  },
  "context": {
    "environment": "production",
    "deploymentId": "deploy-789",
    "version": "1.2.3"
  }
}
```

**Mapeo a Entidad de Telemetría (Base de Datos Separada):**

**NOTA:** Esta entidad NO se persiste en PostgreSQL de negocio. Se almacena en la base de datos de telemetría optimizada.

```java
/**
 * Entidad para eventos de interacción en base de datos de telemetría.
 * NO se persiste en PostgreSQL de negocio.
 */
public class AgentInteractionEvent {
    private String eventId;           // UUID único del evento
    private String agentUuid;          // FK a AGTAGENTS (solo referencia)
    private String agentName;          // Cacheado para consultas rápidas
    private String userId;
    private String sessionId;
    private String input;              // TEXT
    private String output;             // TEXT
    private Integer durationMs;
    private Integer tokensUsed;
    private BigDecimal cost;
    private Instant timestamp;        // Timestamp del evento
    private Map<String, Object> metadata;  // JSON adicional
}
```

**Relación con Agente (Base de Datos de Negocio):**
- `agent_uuid` es el identificador que relaciona el evento con el agente en `AGTAGENTS`
- No hay FK física entre bases de datos
- La relación se mantiene mediante el `agent_uuid` como identificador común

---

### 2. Evento de Decisión (`AgentDecision`)

**Cuándo se envía:**
- Cada vez que un agente toma una decisión durante su ejecución
- Incluye decisiones que requieren revisión HITL según reglas configuradas

**Schema JSON:**
```json
{
  "eventType": "AGENT_DECISION",
  "timestamp": "2025-01-15T10:30:15Z",
  "agentUuid": "550e8400-e29b-41d4-a716-446655440000",
  "agentName": "Loan Approval Agent",
  "decision": {
    "decisionId": "decision-789",
    "decisionType": "LOAN_APPROVAL",
    "decisionReason": "Customer meets all criteria",
    "confidenceScore": 0.85,
    "inputData": {
      "customerId": "cust-123",
      "loanAmount": 50000,
      "creditScore": 750,
      "income": 80000
    },
    "outputData": {
      "approved": true,
      "interestRate": 3.5,
      "term": 60
    },
    "metadata": {
      "rulesTriggered": ["rule-1", "rule-2"],
      "modelVersion": "1.2.3"
    }
  },
  "context": {
    "environment": "production",
    "deploymentId": "deploy-789",
    "version": "1.2.3"
  }
}
```

**Mapeo a Entidad de Telemetría (Base de Datos Separada):**

**NOTA:** Los eventos de decisión se almacenan en la base de datos de telemetría. Solo las decisiones que requieren revisión HITL o son aprobadas/rechazadas se pueden sincronizar a la base de datos de negocio para auditoría.

```java
/**
 * Entidad para eventos de decisión en base de datos de telemetría.
 * Se almacena en base de datos de telemetría optimizada.
 */
public class AgentDecisionEvent {
    private String eventId;           // UUID único del evento
    private String agentUuid;         // FK a AGTAGENTS (solo referencia)
    private String agentName;         // Cacheado para consultas rápidas
    private String decisionType;
    private String decisionReason;    // TEXT
    private Double confidenceScore;
    private DecisionStatus status;    // PENDING_REVIEW, REVIEWED, APPROVED, REJECTED
    private String inputData;          // JSON
    private String outputData;        // JSON
    private String reviewedBy;        // Solo si fue revisada
    private Instant reviewedAt;       // Solo si fue revisada
    private Instant timestamp;        // Timestamp del evento
    private Map<String, Object> metadata;
}
```

**Sincronización con Base de Datos de Negocio:**
- Solo decisiones que requieren revisión HITL o fueron revisadas se pueden sincronizar
- Se crea un registro resumido en `AGTDECISIONS` (base de negocio) para auditoría
- El evento completo permanece en la base de telemetría

---

### 3. Evento de Alerta (`AgentAlert`)

**Cuándo se envía:**
- Cuando se detecta una condición anómala o crítica
- Puede ser generado por reglas Drools o por análisis automático

**Schema JSON:**
```json
{
  "eventType": "AGENT_ALERT",
  "timestamp": "2025-01-15T10:30:20Z",
  "agentUuid": "550e8400-e29b-41d4-a716-446655440000",
  "agentName": "Fraud Detection Agent",
  "alert": {
    "alertId": "alert-123",
    "alertType": "PERFORMANCE_DEGRADATION",
    "alertCategory": "OPERATIONAL",
    "severity": "HIGH",
    "status": "TRIGGERED",
    "priority": "URGENT",
    "impactLevel": "HIGH",
    "urgencyLevel": "HIGH",
    "title": "Response time exceeded threshold",
    "description": "Average response time is 5.2s, exceeding threshold of 3s",
    "message": "Agent performance has degraded significantly",
    "triggerCondition": "response_time > 3000",
    "triggerValues": {
      "current": 5200,
      "threshold": 3000,
      "average": 2500
    },
    "metadata": {
      "detectedBy": "performance-monitor",
      "ruleId": "rule-performance-001"
    }
  },
  "context": {
    "environment": "production",
    "deploymentId": "deploy-789",
    "version": "1.2.3"
  }
}
```

**Mapeo a Entidad de Telemetría (Base de Datos Separada):**

**NOTA:** Los eventos de alerta se almacenan en la base de datos de telemetría. Solo alertas críticas o reconocidas/resueltas se pueden sincronizar a la base de datos de negocio.

```java
/**
 * Entidad para eventos de alerta en base de datos de telemetría.
 * Se almacena en base de datos de telemetría optimizada.
 */
public class AgentAlertEvent {
    private String eventId;           // UUID único del evento
    private String agentUuid;         // FK a AGTAGENTS (solo referencia)
    private String agentName;         // Cacheado para consultas rápidas
    private String alertType;
    private String alertCategory;
    private AlertSeverity severity;   // LOW, MEDIUM, HIGH, CRITICAL
    private AlertStatus status;       // TRIGGERED, ACKNOWLEDGED, RESOLVED
    private AlertPriority priority;   // LOW, MEDIUM, HIGH, URGENT
    private String title;
    private String description;       // TEXT
    private Instant triggeredAt;      // Timestamp del evento
    private Instant acknowledgedAt;   // Solo si fue reconocida
    private String acknowledgedBy;    // Solo si fue reconocida
    private Instant resolvedAt;       // Solo si fue resuelta
    private String resolvedBy;        // Solo si fue resuelta
    private Map<String, Object> triggerValues;  // Valores que dispararon la alerta
    private Map<String, Object> metadata;
}
```

**Sincronización con Base de Datos de Negocio:**
- Alertas críticas (CRITICAL) se sincronizan automáticamente
- Alertas reconocidas o resueltas se pueden sincronizar para auditoría
- El evento completo permanece en la base de telemetría

---

## 🔄 PROCESAMIENTO DE EVENTOS

### Flujo de Procesamiento

```java
@Service
public class AgentTelemetryService {

    @Autowired
    private AgentInteractionRepository interactionRepository;

    @Autowired
    private AgentDecisionRepository decisionRepository;

    @Autowired
    private AgentAlertRepository alertRepository;

    @Autowired
    private DroolsRulesService droolsService;

    @Autowired
    private LLMEvaluationService llmService;

    @Autowired
    private AgentReviewRulesService reviewRulesService;

    /**
     * Procesa evento de telemetría recibido de un agente
     */
    public void processTelemetryEvent(TelemetryEvent event) {
        // 1. Validar schema
        validateEventSchema(event);

        // 2. Determinar tipo de evento y procesar
        switch (event.getEventType()) {
            case "AGENT_INTERACTION":
                processInteractionEvent(event);
                break;
            case "AGENT_DECISION":
                processDecisionEvent(event);
                break;
            case "AGENT_ALERT":
                processAlertEvent(event);
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }

    /**
     * Procesa evento de interacción
     */
    private void processInteractionEvent(TelemetryEvent event) {
        // Mapear a entidad de telemetría
        AgentInteractionEvent interactionEvent = mapToInteractionEvent(event);

        // Guardar en base de datos de telemetría (NO en PostgreSQL de negocio)
        telemetryRepository.save(interactionEvent);

        // Opcional: Ejecutar análisis automático
        analyzeInteraction(interactionEvent);

        // NOTA: No se guarda en base de datos de negocio por volumen (10M eventos/día)
    }

    /**
     * Procesa evento de decisión
     */
    private void processDecisionEvent(TelemetryEvent event) {
        AgentDecision decision = mapToDecision(event);

        // 1. Obtener reglas de revisión configuradas para el agente
        List<ReviewRule> reviewRules = reviewRulesService.getReviewRules(decision.getAgentUuid());

        // 2. Ejecutar reglas Drools
        boolean requiresReview = evaluateDroolsRules(decision, reviewRules);

        // 3. Si requiere revisión, evaluar con LLM prompts
        if (requiresReview) {
            boolean llmRequiresReview = evaluateLLMPrompts(decision, reviewRules);
            requiresReview = requiresReview || llmRequiresReview;
        }

        // 4. Establecer estado según resultado
        if (requiresReview) {
            decision.setStatus(DecisionStatus.PENDING_REVIEW);
        } else {
            decision.setStatus(DecisionStatus.APPROVED);
        }

        // 5. Guardar evento en base de datos de telemetría
        telemetryRepository.saveDecisionEvent(decision);

        // 6. Si requiere revisión, crear tarea HITL y sincronizar a BD de negocio
        if (requiresReview) {
            // Sincronizar a base de datos de negocio para auditoría
            syncDecisionToBusinessDB(decision);
            createHITLReviewTask(decision);
        }
    }

    /**
     * Procesa evento de alerta
     */
    private void processAlertEvent(TelemetryEvent event) {
        AgentAlertEvent alertEvent = mapToAlertEvent(event);

        // Guardar en base de datos de telemetría
        telemetryRepository.saveAlertEvent(alertEvent);

        // Si es crítica, sincronizar a base de datos de negocio
        if (alertEvent.getSeverity() == AlertSeverity.CRITICAL) {
            syncAlertToBusinessDB(alertEvent);
        }

        // Notificar a usuarios relevantes según severidad
        notifyAlert(alertEvent);
    }
}
```

---

## 🎯 EJECUCIÓN DE REGLAS DROOLS

### Obtener Reglas Configuradas

```java
@Service
public class AgentReviewRulesService {

    @Autowired
    private AgentReviewRuleRepository reviewRuleRepository;

    /**
     * Obtiene reglas de revisión configuradas para un agente
     */
    public List<ReviewRule> getReviewRules(String agentUuid) {
        return reviewRuleRepository.findByAgentUuidAndIsActiveTrue(agentUuid)
            .stream()
            .sorted(Comparator.comparing(AgentReviewRule::getPriority).reversed()
                .thenComparing(AgentReviewRule::getExecutionOrder))
            .map(this::mapToReviewRule)
            .collect(Collectors.toList());
    }
}
```

### Ejecutar Reglas Drools

```java
@Service
public class DroolsRulesService {

    @Autowired
    private KieContainer kieContainer;

    /**
     * Evalúa reglas Drools para una decisión
     */
    public boolean evaluateDroolsRules(AgentDecision decision, List<ReviewRule> reviewRules) {
        // Filtrar solo reglas Drools
        List<ReviewRule> droolsRules = reviewRules.stream()
            .filter(rule -> rule.getType() == ReviewRuleType.DROOLS)
            .collect(Collectors.toList());

        if (droolsRules.isEmpty()) {
            return false;
        }

        // Crear fact para Drools
        AgentDecisionFact fact = new AgentDecisionFact(decision);

        // Ejecutar cada regla en orden
        for (ReviewRule rule : droolsRules) {
            KieSession kieSession = kieContainer.newKieSession(rule.getRuleName());
            kieSession.insert(fact);
            kieSession.fireAllRules();
            kieSession.dispose();

            // Si la regla indica que requiere revisión, retornar true
            if (fact.isRequiresReview()) {
                return true;
            }
        }

        return false;
    }
}
```

### Fact para Drools

```java
public class AgentDecisionFact {
    private AgentDecision decision;
    private boolean requiresReview = false;
    private String reviewReason;

    public AgentDecisionFact(AgentDecision decision) {
        this.decision = decision;
    }

    // Getters y setters
    public AgentDecision getDecision() { return decision; }
    public boolean isRequiresReview() { return requiresReview; }
    public void setRequiresReview(boolean requiresReview) {
        this.requiresReview = requiresReview;
    }
    public String getReviewReason() { return reviewReason; }
    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }
}
```

### Ejemplo de Regla Drools

```drl
package com.codeflowx.govern.rules.agent;

import com.codeflowx.govern.telemetry.AgentDecisionFact;
import com.codeflowx.govern.entity.agents.AgentDecision;

rule "Low Confidence Decision Requires Review"
    when
        $fact: AgentDecisionFact(decision.confidenceScore < 0.6)
    then
        $fact.setRequiresReview(true);
        $fact.setReviewReason("Confidence score below threshold: " + $fact.getDecision().getConfidenceScore());
    end

rule "High Value Decision Requires Review"
    when
        $fact: AgentDecisionFact(
            decision.decisionType == "LOAN_APPROVAL",
            decision.inputData contains "loanAmount",
            decision.inputData.loanAmount > 100000
        )
    then
        $fact.setRequiresReview(true);
        $fact.setReviewReason("High value loan approval requires human review");
    end
```

---

## 🤖 LLAMADAS A LLMs

### Obtener Prompts LLM Configurados

```java
@Service
public class LLMPromptService {

    @Autowired
    private GovernancePromptRepository promptRepository;

    /**
     * Obtiene prompts LLM configurados para revisión de decisiones
     */
    public List<GovernancePrompt> getReviewPrompts(String agentUuid) {
        return promptRepository.findByCategoryAndIsActiveTrue(
            PromptCategory.DECISION_REVIEW
        ).stream()
        .filter(prompt -> isPromptApplicable(prompt, agentUuid))
        .sorted(Comparator.comparing(GovernancePrompt::getVersion).reversed())
        .collect(Collectors.toList());
    }

    private boolean isPromptApplicable(GovernancePrompt prompt, String agentUuid) {
        // Verificar que el prompt está asociado al agente o es global
        return prompt.getAssociatedAgents().isEmpty() ||
               prompt.getAssociatedAgents().contains(agentUuid);
    }
}
```

### Ejecutar Prompts LLM

```java
@Service
public class LLMEvaluationService {

    @Autowired
    private AIGovernanceClient aiGovernanceClient;

    /**
     * Evalúa decisión usando prompts LLM configurados
     */
    public boolean evaluateLLMPrompts(AgentDecision decision, List<ReviewRule> reviewRules) {
        // Filtrar solo reglas LLM
        List<ReviewRule> llmRules = reviewRules.stream()
            .filter(rule -> rule.getType() == ReviewRuleType.LLM_PROMPT)
            .collect(Collectors.toList());

        if (llmRules.isEmpty()) {
            return false;
        }

        // Obtener prompts LLM
        List<GovernancePrompt> prompts = llmPromptService.getReviewPrompts(decision.getAgentUuid());

        // Ejecutar cada prompt en orden
        for (ReviewRule rule : llmRules) {
            GovernancePrompt prompt = prompts.stream()
                .filter(p -> p.getId().equals(rule.getPromptId()))
                .findFirst()
                .orElse(null);

            if (prompt == null) {
                continue;
            }

            // Construir prompt con contexto de la decisión
            String fullPrompt = buildPromptWithContext(prompt, decision);

            // Llamar a LLM
            LLMEvaluationResponse response = aiGovernanceClient.llmEvaluation()
                .evaluateQuality(LLMQualityRequest.builder()
                    .prompt(fullPrompt)
                    .response(decision.getDecisionReason())
                    .build());

            // Evaluar respuesta
            if (response.getOverallScore() < 0.7) {
                return true; // Requiere revisión
            }
        }

        return false;
    }

    /**
     * Construye prompt completo con contexto de la decisión
     */
    private String buildPromptWithContext(GovernancePrompt prompt, AgentDecision decision) {
        return String.format(
            "%s\n\n" +
            "Contexto de la decisión:\n" +
            "- Tipo: %s\n" +
            "- Confianza: %.2f\n" +
            "- Razón: %s\n" +
            "- Input: %s\n" +
            "- Output: %s\n\n" +
            "¿Esta decisión requiere revisión humana?",
            prompt.getContent(),
            decision.getDecisionType(),
            decision.getConfidenceScore(),
            decision.getDecisionReason(),
            decision.getInputData(),
            decision.getOutputData()
        );
    }
}
```

---

## 📥 ENDPOINT DE RECEPCIÓN

### REST Endpoint

```java
@RestController
@RequestMapping("/api/v1/telemetry/agents")
public class AgentTelemetryController {

    @Autowired
    private AgentTelemetryService telemetryService;

    @Autowired
    private AgentRepository agentRepository;  // Solo para validar que el agente existe

    /**
     * Recibe evento de telemetría de un agente
     *
     * IMPORTANTE: Los eventos se almacenan en base de datos de telemetría separada.
     * No se guardan en PostgreSQL de negocio por volumen (10M eventos/día).
     */
    @PostMapping("/events")
    public ResponseEntity<Void> receiveTelemetryEvent(
            @RequestBody TelemetryEvent event,
            @RequestHeader("X-Agent-UUID") String agentUuid) {

        // Validar que el agente existe en base de datos de negocio (solo validación)
        validateAgent(agentUuid);

        // Procesar evento (se guarda en base de datos de telemetría)
        telemetryService.processTelemetryEvent(event);

        return ResponseEntity.accepted().build();
    }

    /**
     * Batch de eventos (para alta frecuencia)
     *
     * Optimizado para recibir múltiples eventos en una sola petición.
     * Procesamiento asíncrono para mejor rendimiento.
     */
    @PostMapping("/events/batch")
    public ResponseEntity<Void> receiveTelemetryEventsBatch(
            @RequestBody List<TelemetryEvent> events,
            @RequestHeader("X-Agent-UUID") String agentUuid) {

        // Validar agente (una sola vez para el batch)
        validateAgent(agentUuid);

        // Procesar eventos de forma asíncrona
        telemetryService.processTelemetryEventsBatch(events);

        return ResponseEntity.accepted().build();
    }

    /**
     * Valida que el agente existe en base de datos de negocio
     */
    private void validateAgent(String agentUuid) {
        Optional<Agent> agent = agentRepository.findByAgtuuid(agentUuid);
        if (agent.isEmpty() || !"ACTIVE".equals(agent.get().getAgtstatus())) {
            throw new IllegalArgumentException("Agent not found or not active: " + agentUuid);
        }
    }
}
```

### DTO de Evento

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryEvent {
    private String eventType; // AGENT_INTERACTION, AGENT_DECISION, AGENT_ALERT
    private String timestamp;
    private String agentUuid;
    private String agentName;
    private Map<String, Object> interaction; // Para AGENT_INTERACTION
    private Map<String, Object> decision;    // Para AGENT_DECISION
    private Map<String, Object> alert;        // Para AGENT_ALERT
    private Map<String, Object> metrics;      // Para AGENT_INTERACTION
    private Map<String, Object> context;
}
```

---

## 🔍 VALIDACIÓN DE SCHEMA

### Validación con JSON Schema

```java
@Service
public class TelemetrySchemaValidator {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance();

    /**
     * Valida evento contra JSON Schema
     */
    public void validateEvent(TelemetryEvent event) {
        String schemaPath = getSchemaPath(event.getEventType());
        JsonSchema schema = schemaFactory.getSchema(
            new FileInputStream(schemaPath)
        );

        JsonNode eventNode = objectMapper.valueToTree(event);
        Set<ValidationMessage> errors = schema.validate(eventNode);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                "Event validation failed: " + errors.toString()
            );
        }
    }

    private String getSchemaPath(String eventType) {
        switch (eventType) {
            case "AGENT_INTERACTION":
                return "schemas/agent-interaction.schema.json";
            case "AGENT_DECISION":
                return "schemas/agent-decision.schema.json";
            case "AGENT_ALERT":
                return "schemas/agent-alert.schema.json";
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
}
```

### JSON Schema Example

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "eventType": {
      "type": "string",
      "enum": ["AGENT_INTERACTION", "AGENT_DECISION", "AGENT_ALERT"]
    },
    "timestamp": {
      "type": "string",
      "format": "date-time"
    },
    "agentUuid": {
      "type": "string",
      "format": "uuid"
    },
    "agentName": {
      "type": "string"
    },
    "interaction": {
      "type": "object",
      "properties": {
        "input": {"type": "string"},
        "output": {"type": "string"},
        "success": {"type": "boolean"}
      },
      "required": ["input", "output", "success"]
    },
    "metrics": {
      "type": "object",
      "properties": {
        "durationMs": {"type": "integer"},
        "tokensUsed": {"type": "object"},
        "cost": {"type": "object"}
      }
    }
  },
  "required": ["eventType", "timestamp", "agentUuid"]
}
```

---

## 📋 CHECKLIST DE IMPLEMENTACIÓN

### Módulo de Telemetría

- [ ] Endpoint REST para recibir eventos (`POST /api/v1/telemetry/agents/events`)
- [ ] Endpoint REST para batch de eventos (`POST /api/v1/telemetry/agents/events/batch`)
- [ ] Validación de schema JSON para cada tipo de evento
- [ ] Validación de que el agente existe en base de datos de negocio
- [ ] Mapeo de eventos a entidades de telemetría
- [ ] **Persistencia en base de datos de telemetría separada (NO PostgreSQL de negocio)**
- [ ] Configuración de base de datos de telemetría (Time-Series DB)
- [ ] Particionamiento de datos por fecha/agente
- [ ] Índices optimizados sobre `agent_uuid`, `timestamp`, `event_type`
- [ ] Políticas de retención de datos
- [ ] Ejecución de reglas Drools configuradas
- [ ] Llamadas a LLMs para prompts configurados
- [ ] Creación de tareas HITL cuando se requiere revisión
- [ ] Sincronización selectiva a base de datos de negocio (solo eventos importantes)
- [ ] Notificaciones de alertas según severidad
- [ ] Logging detallado de eventos procesados
- [ ] Manejo de errores y retry para eventos fallidos
- [ ] Procesamiento asíncrono para alta frecuencia
- [ ] Agregaciones pre-calculadas para consultas rápidas

### Integración con Reglas de Revisión

- [ ] Consulta de reglas Drools configuradas por agente
- [ ] Consulta de prompts LLM configurados por agente
- [ ] Ejecución de reglas en orden de prioridad
- [ ] Evaluación de resultados de reglas
- [ ] Determinación de necesidad de revisión HITL

---

## 🚨 MEJORES PRÁCTICAS

1. **Validación temprana:**
   - Validar schema antes de procesar
   - Validar que el agente existe en base de datos de negocio (solo validación)
   - Validar que los datos requeridos están presentes

2. **Procesamiento asíncrono:**
   - Usar colas de mensajes para alta frecuencia (10M eventos/día)
   - Procesar eventos en background
   - Retornar respuesta inmediata al agente (202 Accepted)

3. **Base de datos separada:**
   - **NO almacenar eventos en PostgreSQL de negocio**
   - Usar base de datos de telemetría optimizada (Time-Series DB)
   - Particionar datos por fecha/agente
   - Implementar políticas de retención

4. **Idempotencia:**
   - Usar IDs únicos para eventos (`eventId`)
   - Evitar procesar el mismo evento dos veces
   - Implementar deduplicación en base de datos de telemetría

5. **Manejo de errores:**
   - No fallar todo el batch si un evento falla
   - Registrar errores para análisis
   - Implementar retry con backoff exponencial
   - Dead letter queue para eventos fallidos

6. **Performance:**
   - Cachear reglas y prompts frecuentemente usados
   - Usar batch processing para alta frecuencia
   - **NO hacer consultas a base de datos de negocio durante procesamiento**
   - Pre-calcular agregaciones para consultas rápidas
   - Usar índices optimizados en base de datos de telemetría

7. **Sincronización selectiva:**
   - Solo sincronizar eventos importantes a base de datos de negocio
   - Decisiones que requieren revisión HITL
   - Alertas críticas
   - Eventos reconocidos/resueltos para auditoría

8. **Relación con agentes:**
   - Usar `agent_uuid` como identificador común
   - No hay FK física entre bases de datos
   - Validar existencia del agente antes de procesar
   - Cachear `agent_name` en eventos para consultas rápidas

---

## 📚 REFERENCIAS

- **Entidades de Negocio:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/agents/`
- **Base de Datos de Telemetría:** Time-Series DB optimizada (TimescaleDB, InfluxDB, ClickHouse)
- **Cliente LLM:** `codeflowx.govern.nocode.client`
- **Reglas Drools:** `mocks/drools/*.drl`
- **JSON Schema:** https://json-schema.org/
- **TimescaleDB:** https://www.timescale.com/
- **InfluxDB:** https://www.influxdata.com/
- **ClickHouse:** https://clickhouse.com/

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** ✅ Operativo
