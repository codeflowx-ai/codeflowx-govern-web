# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - HITL SUPERVISION

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Backend (Java/Spring Boot)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Capas](#estructura-de-capas)
3. [Servicios de Negocio](#servicios-de-negocio)
4. [Entidades JPA](#entidades-jpa)
5. [Repositorios](#repositorios)
6. [BFF (Backend for Frontend)](#bff-backend-for-frontend)
7. [Microservicio de Negocio](#microservicio-de-negocio)
8. [Flujos de Negocio](#flujos-de-negocio)
9. [Integración BPMN](#integración-bpmn)
10. [Integración Python Microservices](#integración-python-microservices)
11. [Validaciones y Reglas](#validaciones-y-reglas)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
API Routes (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (HITL Service)
    ↓
Business Services (Lógica de Negocio)
    ↓
Repositories (JPA)
    ↓
Database (PostgreSQL)
```

### Componentes Principales

1. **BFF (Backend for Frontend)**
   - Ubicación: `codeflowx.govern.bff.compliance`
   - Responsabilidad: Agregar datos, optimizar respuestas para frontend
   - Tecnología: Spring WebFlux (Reactivo)

2. **Business Microservice**
   - Ubicación: `codeflowx-governance-hitl-service`
   - Responsabilidad: Endpoints REST para HITL
   - Tecnología: Spring WebFlux (Reactivo)
   - Puerto: `8099`

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, validaciones, reglas
   - Tecnología: Spring Boot (Transaccional)

4. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/`

**Componentes:**
- **Controller:** `controller/HitlController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados

- **Service:** `service/HitlService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/HitlServiceImpl.java`
  - Implementa llamadas a microservicio de negocio
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/hitl")
public class HitlController {
    private final HitlService hitlService;

    @GetMapping("/dashboard")
    public Mono<ResponseEntity<HitlDashboardDto>> getDashboard() {
        return hitlService.getDashboard()
                .map(ResponseEntity::ok);
    }
}
```

---

### 2. Microservicio de Negocio

**Ubicación:** `nocode.service/codeflowx-governance-hitl-service/`

**Componentes:**
- **Controller:** `controller/HitlController.java`
  - Endpoints REST reactivos
  - Convierte datos internos a DTOs
  - Maneja errores y validaciones

- **Application:** `HitlServiceApplication.java`
  - Clase principal del microservicio
  - Configuración de Spring Boot

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/hitl")
public class HitlController {
    private final HitlSupervisionBusinessService businessService;

    @GetMapping("/dashboard")
    public Mono<ResponseEntity<HitlDashboardDto>> getDashboard() {
        return Mono.fromCallable(() -> businessService.getDashboard())
                .subscribeOn(Schedulers.boundedElastic())
                .map(dashboardData -> {
                    HitlDashboardDto dto = convertToDto(dashboardData);
                    return ResponseEntity.ok(dto);
                });
    }
}
```

---

### 3. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

**Componente Principal:**
- **HitlSupervisionBusinessService.java**
  - Lógica de negocio central
  - Trabaja con entidades JPA (NO DTOs)
  - Retorna datos internos (data classes)
  - Integra BPMN workflows
  - Integra Python microservices (opcional)

**Ejemplo:**
```java
@Service
@Transactional
public class HitlSupervisionBusinessService {
    private final HitlSupervisionRepository supervisionRepository;
    private final HitlDecisionRepository decisionRepository;
    private final BpmnWorkflowClient bpmnWorkflowClient;
    private final AIGovernanceClient aiGovernanceClient;

    public HitlDashboardData getDashboard() {
        // Lógica de negocio usando repositorios JPA
        // Retorna HitlDashboardData (clase interna)
    }
}
```

---

### 4. Entities Layer

**Ubicación:** `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`

**Entidades:**
- **HitlSupervision.java**
  - Tabla: `hitl_supervision`
  - Campos principales:
    - `id`: Long (PK, autoincremental)
    - `hitlentitytype`: String (tipo de entidad)
    - `idxentity`: Long (ID de la entidad)
    - `hitlentityname`: String (nombre de la entidad)
    - `status`: String (PENDING, IN_REVIEW)
    - `urgency`: String (CRITICAL, HIGH, MEDIUM, LOW)
    - `slaDeadline`: LocalDateTime
    - `createdAt`: LocalDateTime
    - `updatedAt`: LocalDateTime

- **HitlDecision.java**
  - Tabla: `hitl_decision`
  - Campos principales:
    - `id`: Long (PK, autoincremental)
    - `supervision`: HitlSupervision (FK)
    - `decision`: String (APPROVED, REJECTED, MODIFIED)
    - `decisionReason`: String (HTML)
    - `responseTime`: Double (horas)
    - `decisionDate`: LocalDateTime
    - `userId`: String

**Ejemplo:**
```java
@Entity
@Table(name = "hitl_supervision")
public class HitlSupervision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hitlentitytype")
    private String hitlentitytype;

    @Column(name = "idxentity")
    private Long idxentity;

    @Column(name = "hitlentityname")
    private String hitlentityname;

    @Column(name = "status")
    private String status;

    // ... más campos
}
```

---

### 5. Repositories Layer

**Ubicación:** `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/`

**Repositorios:**
- **HitlSupervisionRepository.java**
  - Extiende `JpaRepository<HitlSupervision, Long>`
  - Métodos personalizados para consultas

- **HitlDecisionRepository.java**
  - Extiende `JpaRepository<HitlDecision, Long>`
  - Métodos personalizados para consultas

**Ejemplo:**
```java
@Repository
public interface HitlSupervisionRepository extends JpaRepository<HitlSupervision, Long> {
    List<HitlSupervision> findByStatus(String status);
    List<HitlSupervision> findByHitlentitytype(String entityType);
    List<HitlSupervision> findByStatusAndHitlentitytype(String status, String entityType);
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Obtener Dashboard

```
1. Frontend → GET /api/compliance/hitl/dashboard
2. API Route (Next.js) → GET /api/v1/hitl/dashboard (BFF)
3. BFF → GET http://hitl-service:8099/api/hitl/dashboard
4. Microservicio → HitlSupervisionBusinessService.getDashboard()
5. Business Service:
   - Consulta repositorios JPA
   - Calcula métricas
   - Retorna HitlDashboardData
6. Microservicio → Convierte a HitlDashboardDto
7. BFF → Retorna DTO al frontend
8. Frontend → Renderiza dashboard
```

### Flujo 2: Registrar Decisión

```
1. Frontend → POST /api/compliance/hitl/interventions
2. API Route (Next.js) → POST /api/v1/hitl/interventions (BFF)
3. BFF → POST http://hitl-service:8099/api/hitl/interventions
4. Microservicio → HitlSupervisionBusinessService.recordDecision()
5. Business Service:
   - Valida intervención existe
   - Calcula tiempo de respuesta
   - Crea HitlDecision
   - Actualiza HitlSupervision
   - Dispara workflow BPMN "hitl-decision-process"
   - Retorna HitlDecisionData
6. Microservicio → Convierte a HitlDecisionDto
7. BFF → Retorna DTO al frontend
8. Frontend → Actualiza UI
```

### Flujo 3: Crear Intervención (desde otro sistema)

```
1. Sistema externo → Crea HitlSupervision (directo a BD o vía API)
2. Business Service (opcional) → Dispara workflow BPMN "hitl-supervision-process"
3. Intervención aparece en dashboard como PENDING
```

---

## 🔗 INTEGRACIÓN BPMN

### Workflows Disponibles

1. **hitl-supervision-process**
   - Se dispara al crear una intervención HITL
   - Variables: `supervisionId`, `entityType`, `entityId`, `urgency`

2. **hitl-decision-process**
   - Se dispara al registrar una decisión
   - Variables: `decisionId`, `supervisionId`, `decision`, `userId`

### Uso en Business Service

```java
@Service
public class HitlSupervisionBusinessService {
    private final BpmnWorkflowClient bpmnWorkflowClient;

    public HitlDecisionData recordDecision(HitlDecisionRequest request) {
        // ... lógica de negocio ...

        // Disparar workflow BPMN
        try {
            Map<String, Object> variables = Map.of(
                "decisionId", decision.getId(),
                "supervisionId", supervision.getId(),
                "decision", decision.getDecision(),
                "userId", decision.getUserId()
            );
            bpmnWorkflowClient.startProcess("hitl-decision-process", variables);
        } catch (Exception e) {
            log.error("Error al disparar workflow BPMN", e);
            // No fallar la operación si el workflow falla
        }

        return decisionData;
    }
}
```

---

## 🐍 INTEGRACIÓN PYTHON MICROSERVICES

### Cliente AIGovernanceClient

El módulo HITL puede integrarse con microservicios Python especializados para proporcionar evaluación automática y contexto adicional a los supervisores. El cliente `AIGovernanceClient` actúa como una factoría que proporciona acceso a clientes especializados según el tipo de entidad.

**Dependencia:**
```xml
<dependency>
    <groupId>codeflowx.govern</groupId>
    <artifactId>codeflowx.govern.nocode.client</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Configuración:**
```yaml
codeflowx:
  leka:
    governance:
      enabled: true
      gateway:
        url: http://api-leka-govern:8000  # K8s: http://api-leka-govern:8000
                                          # Local: http://localhost:8000
      timeout: 30000  # 30 segundos
```

### Clientes Especializados por Tipo de Entidad

#### 1. Para Entidades de Tipo "Agent"

Usar `AgentMonitoringClient` para análisis de ejecución de agentes:

```java
@Service
public class HitlSupervisionBusinessService {
    @Autowired
    private AIGovernanceClient governance;

    public HitlInterventionData getInterventionContext(HitlSupervision supervision) {
        if ("Agent".equals(supervision.getHitlentitytype())) {
            try {
                // Obtener cliente especializado para agentes
                AgentMonitoringClient agentClient = governance.agentMonitoring();

                // Analizar ejecución del agente
                AgentExecutionRequest request = AgentExecutionRequest.builder()
                    .agentId(String.valueOf(supervision.getIdxentity()))
                    .executionTrace(getAgentTrace(supervision))
                    .build();

                AgentExecutionResponse response = agentClient.analyzeExecution(request);

                // Usar respuesta para contexto adicional
                return HitlInterventionData.builder()
                    .context("Análisis automático: " + response.getSummary())
                    .riskLevel(response.getRiskLevel())
                    .recommendations(response.getRecommendations())
                    .build();
            } catch (Exception e) {
                log.warn("No se pudo obtener evaluación automática del agente", e);
                // Continuar sin evaluación automática
            }
        }
        return null;
    }
}
```

**Endpoints disponibles:**
- `POST /api/agent/analyze-execution` - Análisis de ejecución individual
- `POST /api/agent/evaluate-reliability` - Evaluación de confiabilidad
- `POST /api/agent/analyze-costs` - Análisis de costos
- `POST /api/agent/detect-infinite-loops` - Detección de loops infinitos

#### 2. Para Entidades de Tipo "Model"

Usar `ModelWrapperClient` o `LLMEvaluationClient` para evaluación de modelos:

```java
@Service
public class HitlSupervisionBusinessService {
    @Autowired
    private AIGovernanceClient governance;

    public HitlInterventionData getInterventionContext(HitlSupervision supervision) {
        if ("Model".equals(supervision.getHitlentitytype())) {
            try {
                // Opción 1: Evaluar calidad del modelo
                LLMEvaluationClient llmClient = governance.llmEvaluation();

                HallucinationRequest request = HallucinationRequest.builder()
                    .modelId(String.valueOf(supervision.getIdxentity()))
                    .prompt(getModelPrompt(supervision))
                    .response(getModelResponse(supervision))
                    .build();

                HallucinationResponse response = llmClient.evaluateHallucination(request);

                // Opción 2: Invocar modelo para validación
                ModelWrapperClient modelClient = governance.modelWrapper();
                ModelInvokeResponse invokeResponse = modelClient.invoke(
                    createInvokeRequest(supervision)
                );

                return HitlInterventionData.builder()
                    .context("Evaluación automática: Hallucinación detectada: " +
                             response.getHallucinationDetected())
                    .riskLevel(response.getScore() > 0.7 ? "HIGH" : "MEDIUM")
                    .build();
            } catch (Exception e) {
                log.warn("No se pudo obtener evaluación automática del modelo", e);
            }
        }
        return null;
    }
}
```

**Endpoints disponibles:**
- `POST /api/llm/evaluate-hallucination` - Evaluación de alucinaciones
- `POST /api/llm/evaluate-toxicity` - Evaluación de toxicidad
- `POST /api/llm/evaluate-quality` - Evaluación de calidad
- `POST /api/models/invoke` - Invocación de modelo

#### 3. Para Entidades de Tipo "Prompt"

Usar `PromptGovernanceClient` para evaluación de seguridad y efectividad:

```java
@Service
public class HitlSupervisionBusinessService {
    @Autowired
    private AIGovernanceClient governance;

    public HitlInterventionData getInterventionContext(HitlSupervision supervision) {
        if ("Prompt".equals(supervision.getHitlentitytype())) {
            try {
                PromptGovernanceClient promptClient = governance.promptGovernance();

                PromptSafetyRequest request = PromptSafetyRequest.builder()
                    .prompt(getPromptText(supervision))
                    .modelTarget(getTargetModel(supervision))
                    .useCase(getUseCase(supervision))
                    .build();

                PromptSafetyResponse response = promptClient.evaluateSafety(request);

                return HitlInterventionData.builder()
                    .context("Evaluación automática: " +
                             (response.getIsSafe() ? "Prompt seguro" : "Riesgos detectados"))
                    .riskLevel(response.getIsSafe() ? "LOW" : "HIGH")
                    .recommendations(response.getRecommendations())
                    .build();
            } catch (Exception e) {
                log.warn("No se pudo obtener evaluación automática del prompt", e);
            }
        }
        return null;
    }
}
```

**Endpoints disponibles:**
- `POST /api/prompt/evaluate-safety` - Evaluación de seguridad
- `POST /api/prompt/evaluate-effectiveness` - Evaluación de efectividad
- `POST /api/prompt/detect-pii` - Detección de PII

### Uso en Business Service

El método `performAutoEvaluation()` en `HitlSupervisionBusinessService` realiza las llamadas reales a los microservicios Python:

```java
@Service
@Transactional
public class HitlSupervisionBusinessService {
    @Autowired(required = false)
    private AIGovernanceClient aiGovernanceClient;

    /**
     * Realiza evaluación automática usando microservicios de Python
     * Se ejecuta automáticamente al crear una intervención HITL
     */
    private String performAutoEvaluation(String entityType, Long entityId, String entityName) {
        if (aiGovernanceClient == null) {
            log.debug("AIGovernanceClient no disponible. Evaluación automática omitida.");
            return null;
        }

        try {
            Map<String, Object> evaluationResult = new HashMap<>();

            // Evaluar según el tipo de entidad usando clientes especializados
            switch (entityType.toUpperCase()) {
                case "PROMPT":
                    // Usa PromptGovernanceClient para evaluación de seguridad
                    PromptGovernanceClient promptClient = aiGovernanceClient.promptGovernance();
                    PromptSafetyRequest safetyRequest = PromptSafetyRequest.builder()
                        .prompt(promptContent)
                        .modelTarget("gpt-4")
                        .useCase("hitl_supervision")
                        .build();
                    PromptSafetyResponse safetyResponse = promptClient.evaluateSafety(safetyRequest);
                    // Almacena: isSafe, safetyScore, risksDetected, severity, recommendations
                    break;

                case "MODEL":
                    // Usa LLMEvaluationClient para evaluación de calidad
                    LLMEvaluationClient llmClient = aiGovernanceClient.llmEvaluation();
                    QualityRequest qualityRequest = QualityRequest.builder()
                        .modelId(String.valueOf(entityId))
                        .prompt(modelPrompt)
                        .response(modelResponse)
                        .build();
                    QualityResponse qualityResponse = llmClient.evaluateQuality(qualityRequest);
                    // Almacena: qualityScore, coherence, relevance, fluency, overallGrade
                    break;

                case "AGENT":
                    // Usa AgentMonitoringClient para análisis de ejecución
                    AgentMonitoringClient agentClient = aiGovernanceClient.agentMonitoring();
                    AgentExecutionRequest executionRequest = AgentExecutionRequest.builder()
                        .agentId(String.valueOf(entityId))
                        .executionTrace(executionTrace)
                        .finalOutput(entityName)
                        .build();
                    AgentExecutionResponse executionResponse = agentClient.analyzeExecution(executionRequest);
                    // Almacena: success, successRate, efficiencyScore, recommendations, bottlenecks
                    break;
            }

            // Serializa resultado a JSON y lo almacena en hitlconfiguration
            return objectMapper.writeValueAsString(evaluationResult);

        } catch (ServiceUnavailableException e) {
            log.warn("Microservicio de Python no disponible", e);
            return null;
        } catch (AIGovernanceException e) {
            log.warn("Error en microservicio de Python: service={}, status={}",
                e.getService(), e.getStatusCode(), e);
            return null;
        }
    }
}
```

**Ubicación del código:**
- Archivo: `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/HitlSupervisionBusinessService.java`
- Método: `performAutoEvaluation()` (líneas 807-1000 aproximadamente)
- Se ejecuta automáticamente en: `createIntervention()` (línea 184)

### Manejo de Errores

```java
try {
    AgentExecutionResponse response = governance.agentMonitoring()
        .analyzeExecution(request);
    // Procesar respuesta
} catch (ServiceUnavailableException e) {
    // Gateway no disponible (503)
    log.error("Gateway no disponible", e);
    // Continuar sin evaluación automática
} catch (AIGovernanceException e) {
    // Error genérico
    log.error("Error en governance: {}", e.getMessage(), e);
    log.error("Service: {}, Status code: {}", e.getService(), e.getStatusCode());
    // Continuar sin evaluación automática
}
```

### Referencias

- **Guía Completa:** `codeflowx.govern.nocode.client/GUIA_USO_CLIENTE.md`
- **Agent Monitoring:** `codeflowx.govern.nocode.client/AGENT_MONITORING_CLIENT_GUIDE.md`
- **Model Wrapper:** `codeflowx.govern.nocode.client/MODEL_WRAPPER_CLIENT_GUIDE.md`
- **Prompt Governance:** `codeflowx.govern.nocode.client/PROMPT_GOVERNANCE_CLIENT_GUIDE.md`
- **LLM Evaluation:** `codeflowx.govern.nocode.client/LLMEVALUATION_CLIENT_GUIDE.md`

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones en Business Service

1. **Validación de Intervención:**
   - La intervención debe existir
   - La intervención debe estar en estado PENDING o IN_REVIEW
   - No se puede decidir sobre intervenciones ya decididas

2. **Validación de Decisión:**
   - El tipo de decisión debe ser válido (APPROVED, REJECTED, MODIFIED)
   - La razón debe estar presente y no vacía
   - El usuario debe estar autenticado

3. **Validación de Configuración:**
   - El tipo de supervisión debe ser válido
   - El SLA debe ser un número positivo
   - La configuración debe existir antes de actualizar

### Reglas de Negocio

1. **Cálculo de Tiempo de Respuesta:**
   - `responseTime = (decisionDate - createdAt) en horas`

2. **Cálculo de SLA Compliance:**
   - `slaCompliance = (decisiones dentro de SLA / total decisiones) * 100`

3. **Cálculo de Tasa de Aprobación:**
   - `approvalRate = (decisiones APPROVED / total decisiones) * 100`

4. **Determinación de Urgencia:**
   - CRITICAL: tiempo restante < 1 hora
   - HIGH: tiempo restante < 2 horas
   - MEDIUM: tiempo restante < 4 horas
   - LOW: tiempo restante >= 4 horas

---

## 📦 DTOs (Data Transfer Objects)

### Ubicación
`nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

### DTOs Principales

1. **HitlDashboardDto**
   - Métricas principales (`HitlMetricsDto`)
   - Lista de intervenciones pendientes (`List<HitlInterventionDto>`)
   - Lista de decisiones recientes (`List<HitlDecisionDto>`)
   - Configuración de supervisión (`List<HitlSupervisionConfigDto>`)

2. **HitlMetricsDto**
   - `averageResponseTime`: Double (horas)
   - `approvalRate`: Double (0.0 - 1.0)
   - `slaCompliance`: Double (0.0 - 1.0)
   - `pendingInterventions`: Integer
   - `totalInterventions`: Integer
   - `interventionsByType`: Map<String, Integer>
   - `interventionsByStatus`: Map<String, Integer>

3. **HitlInterventionDto**
   - `id`: Long
   - `type`: String
   - `entityType`: String (PROMPT, MODEL, AGENT)
   - `entityId`: Long
   - `entityName`: String
   - `status`: String (PENDING, IN_REVIEW)
   - `createdAt`: LocalDateTime
   - `slaDeadline`: LocalDateTime
   - `slaHours`: Integer
   - `timeRemaining`: Double (horas)
   - `urgency`: String (CRITICAL, HIGH, MEDIUM, LOW)

4. **HitlDecisionDto**
   - `id`: Long
   - `type`: String
   - `entityType`: String
   - `entityId`: Long
   - `entityName`: String
   - `decision`: String (APPROVED, REJECTED, MODIFIED)
   - `decisionReason`: String (HTML)
   - `responseTime`: Double (horas)
   - `decisionDate`: LocalDateTime
   - `userId`: String

5. **HitlDecisionRequest**
   - `interventionId`: Long
   - `decision`: String (APPROVED, REJECTED, MODIFIED)
   - `reason`: String (HTML del editor enriquecido)
   - `userId`: String

6. **HitlSupervisionConfigDto**
   - `type`: String
   - `enabled`: Boolean
   - `slaHours`: Integer
   - `requiredRoles`: List<String>
   - `autoEscalation`: Boolean
   - `escalationHours`: Integer

### Regla Crítica: Separación de Capas

**IMPORTANTE:**
- **Controllers (BFF y Microservicio):** Trabajan SOLO con DTOs
- **Business Services:** Trabajan SOLO con Entidades JPA y clases de datos internas
- **Conversión:** Se hace en los controllers, NO en business services

---

## 🔄 CLASES DE DATOS INTERNAS

### Ubicación
Las clases de datos internas están definidas como clases estáticas internas en `HitlSupervisionBusinessService`:

**Archivo:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/HitlSupervisionBusinessService.java`

### Estructura de Clases de Datos

#### 1. **HitlDashboardData**
```java
public static class HitlDashboardData {
    public HitlMetricsData metrics;
    public List<HitlInterventionData> pendingInterventions;
    public List<HitlDecisionData> recentDecisions;
    public List<HitlSupervisionConfigData> supervisionConfig;
}
```

#### 2. **HitlMetricsData**
```java
public static class HitlMetricsData {
    public Double averageResponseTime;        // horas
    public Double approvalRate;              // 0.0 - 1.0
    public Double slaCompliance;             // 0.0 - 1.0
    public Integer pendingInterventions;
    public Integer totalInterventions;
    public Map<String, Integer> interventionsByType;
    public Map<String, Integer> interventionsByStatus;
}
```

#### 3. **HitlInterventionData**
```java
public static class HitlInterventionData {
    public Long id;
    public String type;
    public String entityType;                // PROMPT, MODEL, AGENT
    public Long entityId;
    public String entityName;
    public String status;                    // PENDING, IN_REVIEW
    public LocalDateTime createdAt;
    public LocalDateTime slaDeadline;
    public Integer slaHours;
    public Double timeRemaining;             // horas
    public String urgency;                   // CRITICAL, HIGH, MEDIUM, LOW
}
```

#### 4. **HitlDecisionData**
```java
public static class HitlDecisionData {
    public Long id;
    public String type;
    public String entityType;
    public Long entityId;
    public String entityName;
    public String decision;                  // APPROVED, REJECTED, MODIFIED
    public String decisionReason;            // HTML
    public Double responseTime;             // horas
    public LocalDateTime decisionDate;
    public String userId;
}
```

#### 5. **HitlDecisionRequestData**
```java
public static class HitlDecisionRequestData {
    public Long interventionId;
    public String decision;                  // APPROVED, REJECTED, MODIFIED
    public String reason;                   // HTML
    public String userId;
}
```

#### 6. **HitlSupervisionConfigData**
```java
public static class HitlSupervisionConfigData {
    public String type;
    public Boolean enabled;
    public Integer slaHours;
    public List<String> requiredRoles;
    public Boolean autoEscalation;
    public Integer escalationHours;
}
```

### Uso de Clases de Datos

**IMPORTANTE:** Estas clases son **internas al business service** y NO deben exponerse directamente a los controllers. Los controllers deben trabajar con DTOs.

**Flujo de Conversión:**
```
Entidad JPA → Clase de Datos Interna → DTO
     (Repository)    (Business Service)   (Controller)
```

---

## 🔀 MAPEO ENTRE CLASES DE DATOS Y DTOs

### Ubicación de Métodos de Mapeo

Los métodos de mapeo están en el **Controller del microservicio**:

**Archivo:** `nocode.service/codeflowx-governance-hitl-service/src/main/java/com/codeflowx/govern/hitl/controller/HitlController.java`

### Métodos de Conversión

#### 1. **toDashboardDto()**
Convierte `HitlDashboardData` → `HitlDashboardDto`

```java
private HitlDashboardDto toDashboardDto(HitlDashboardData data) {
    HitlDashboardDto dto = new HitlDashboardDto();
    dto.setMetrics(toMetricsDto(data.metrics));
    dto.setPendingInterventions(data.pendingInterventions.stream()
        .map(this::toInterventionDto)
        .collect(Collectors.toList()));
    dto.setRecentDecisions(data.recentDecisions.stream()
        .map(this::toDecisionDto)
        .collect(Collectors.toList()));
    dto.setSupervisionConfig(data.supervisionConfig.stream()
        .map(this::toConfigDto)
        .collect(Collectors.toList()));
    return dto;
}
```

#### 2. **toMetricsDto()**
Convierte `HitlMetricsData` → `HitlMetricsDto`

```java
private HitlMetricsDto toMetricsDto(HitlMetricsData data) {
    HitlMetricsDto dto = new HitlMetricsDto();
    dto.setAverageResponseTime(data.averageResponseTime);
    dto.setApprovalRate(data.approvalRate);
    dto.setSlaCompliance(data.slaCompliance);
    dto.setPendingInterventions(data.pendingInterventions);
    dto.setTotalInterventions(data.totalInterventions);
    dto.setInterventionsByType(data.interventionsByType);
    dto.setInterventionsByStatus(data.interventionsByStatus);
    return dto;
}
```

#### 3. **toInterventionDto()**
Convierte `HitlInterventionData` → `HitlInterventionDto`

```java
private HitlInterventionDto toInterventionDto(HitlInterventionData data) {
    HitlInterventionDto dto = new HitlInterventionDto();
    dto.setId(data.id);
    dto.setType(data.type);
    dto.setEntityType(data.entityType);
    dto.setEntityId(data.entityId);
    dto.setEntityName(data.entityName);
    dto.setStatus(data.status);
    dto.setCreatedAt(data.createdAt);
    dto.setSlaDeadline(data.slaDeadline);
    dto.setSlaHours(data.slaHours);
    dto.setTimeRemaining(data.timeRemaining);
    dto.setUrgency(data.urgency);
    return dto;
}
```

#### 4. **toDecisionDto()**
Convierte `HitlDecisionData` → `HitlDecisionDto`

```java
private HitlDecisionDto toDecisionDto(HitlDecisionData data) {
    HitlDecisionDto dto = new HitlDecisionDto();
    dto.setId(data.id);
    dto.setType(data.type);
    dto.setEntityType(data.entityType);
    dto.setEntityId(data.entityId);
    dto.setEntityName(data.entityName);
    dto.setDecision(data.decision);
    dto.setDecisionReason(data.decisionReason);
    dto.setResponseTime(data.responseTime);
    dto.setDecisionDate(data.decisionDate);
    dto.setUserId(data.userId);
    return dto;
}
```

#### 5. **toConfigDto() / toConfigData()**
Conversión bidireccional entre `HitlSupervisionConfigData` ↔ `HitlSupervisionConfigDto`

```java
// DTO → Clase de Datos Interna (para requests)
private HitlSupervisionConfigData toConfigData(HitlSupervisionConfigDto dto) {
    HitlSupervisionConfigData data = new HitlSupervisionConfigData();
    data.type = dto.getType();
    data.enabled = dto.getEnabled();
    data.slaHours = dto.getSlaHours();
    data.requiredRoles = dto.getRequiredRoles();
    data.autoEscalation = dto.getAutoEscalation();
    data.escalationHours = dto.getEscalationHours();
    return data;
}

// Clase de Datos Interna → DTO (para responses)
private HitlSupervisionConfigDto toConfigDto(HitlSupervisionConfigData data) {
    HitlSupervisionConfigDto dto = new HitlSupervisionConfigDto();
    dto.setType(data.type);
    dto.setEnabled(data.enabled);
    dto.setSlaHours(data.slaHours);
    dto.setRequiredRoles(data.requiredRoles);
    dto.setAutoEscalation(data.autoEscalation);
    dto.setEscalationHours(data.escalationHours);
    return dto;
}
```

### Reglas de Mapeo

1. **Siempre mapear en el Controller**, nunca en el Business Service
2. **Manejar valores null** apropiadamente
3. **Preservar tipos de datos** (Double, Integer, LocalDateTime, etc.)
4. **Mapear colecciones** usando streams y Collectors
5. **Validar datos** antes de mapear (usar `@Valid` en requests)

---

## 🔧 CONFIGURACIÓN

### BFF Configuration

**Archivo:** `codeflowx.govern.bff.compliance/src/main/resources/application.yml`

```yaml
services:
  hitl:
    base-url: http://localhost:8099

resilience4j:
  circuitbreaker:
    instances:
      hitlService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10
  retry:
    instances:
      hitlService:
        maxAttempts: 3
        waitDuration: 1s
```

### Microservicio Configuration

**Archivo:** `codeflowx-governance-hitl-service/src/main/resources/application.yml`

```yaml
server:
  port: 8099

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/codeflowx
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
```

---

## ⚠️ MANEJO DE EXCEPCIONES

### Excepciones en Business Service

El `HitlSupervisionBusinessService` puede lanzar las siguientes excepciones:

1. **IllegalArgumentException**
   - Cuando los parámetros de entrada son inválidos
   - Ejemplo: `decision` no es uno de los valores permitidos (APPROVED, REJECTED, MODIFIED)
   - Ubicación: Validaciones en métodos `recordDecision()`, `updateSupervisionConfig()`

2. **RuntimeException**
   - Errores genéricos durante operaciones de negocio
   - Ejemplo: Error al consultar repositorios
   - Ubicación: Cualquier método del business service

3. **EntityNotFoundException** (implícito)
   - Cuando una intervención no existe
   - Ejemplo: `supervisionRepository.findById()` retorna `Optional.empty()`
   - Ubicación: `recordDecision()`, `getInterventions()`

### Manejo de Excepciones en Controller

El `HitlController` maneja excepciones de la siguiente manera:

```java
// Ejemplo: recordDecision()
.onErrorResume(IllegalArgumentException.class, error -> {
    log.warn("Invalid request: {}", error.getMessage());
    return Mono.just(ResponseEntity.<HitlDecisionDto>badRequest().build());
})
.onErrorResume(error -> {
    log.error("Error recording HITL decision", error);
    return Mono.just(ResponseEntity.<HitlDecisionDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
});
```

### Códigos HTTP Retornados

| Situación | Código HTTP | Descripción |
|-----------|-------------|-------------|
| Éxito | 200 OK | Operación completada exitosamente |
| Request inválido | 400 Bad Request | Parámetros inválidos o validación fallida |
| No encontrado | 404 Not Found | Intervención o recurso no existe |
| Error interno | 500 Internal Server Error | Error no manejado en el servidor |

### Excepciones de Integración

#### BPMN Workflow Client
- **No bloquea la operación principal**: Si el workflow falla, se registra en logs pero la decisión se guarda
- **Manejo**: Try-catch en métodos que disparan workflows

```java
try {
    bpmnWorkflowClient.startProcess("hitl-decision-process", variables);
} catch (Exception e) {
    log.error("Error al disparar workflow BPMN", e);
    // No fallar la operación si el workflow falla
}
```

#### Python Microservices
- **ServiceUnavailableException**: Gateway no disponible (503)
- **AIGovernanceException**: Error genérico en microservicio Python
- **No bloquea la operación principal**: La evaluación automática es opcional

```java
try {
    PromptSafetyResponse response = promptClient.evaluateSafety(request);
    // Procesar respuesta
} catch (ServiceUnavailableException e) {
    log.warn("Microservicio de Python no disponible", e);
    // Continuar sin evaluación automática
} catch (AIGovernanceException e) {
    log.warn("Error en microservicio de Python: service={}, status={}",
        e.getService(), e.getStatusCode(), e);
    // Continuar sin evaluación automática
}
```

---

## 🧪 TESTING

### Unit Tests

**Ubicación:** `codeflowx.govern.business/src/test/java/com/codeflowx/govern/business/compliance/`

```java
@ExtendWith(MockitoExtension.class)
class HitlSupervisionBusinessServiceTest {
    @Mock
    private HitlSupervisionRepository supervisionRepository;

    @Mock
    private HitlDecisionRepository decisionRepository;

    @Mock
    private BpmnWorkflowClient bpmnWorkflowClient;

    @Mock
    private AIGovernanceClient aiGovernanceClient;

    @InjectMocks
    private HitlSupervisionBusinessService businessService;

    @Test
    void testGetDashboard() {
        // Arrange
        when(supervisionRepository.findAll()).thenReturn(createMockSupervisions());
        when(decisionRepository.findAll()).thenReturn(createMockDecisions());

        // Act
        HitlDashboardData result = businessService.getDashboardMetrics();

        // Assert
        assertNotNull(result);
        assertNotNull(result.metrics);
        assertEquals(10, result.pendingInterventions.size());
    }

    @Test
    void testRecordDecision_ValidRequest() {
        // Arrange
        HitlSupervision supervision = createMockSupervision();
        when(supervisionRepository.findById(1L)).thenReturn(Optional.of(supervision));
        when(decisionRepository.save(any(HitlDecision.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        HitlDecisionData result = businessService.recordDecision(
            1L, "APPROVED", "Reason", "user123"
        );

        // Assert
        assertNotNull(result);
        assertEquals("APPROVED", result.decision);
        assertEquals("user123", result.userId);
    }

    @Test
    void testRecordDecision_InvalidDecision() {
        // Arrange
        HitlSupervision supervision = createMockSupervision();
        when(supervisionRepository.findById(1L)).thenReturn(Optional.of(supervision));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            businessService.recordDecision(1L, "INVALID", "Reason", "user123");
        });
    }
}
```

### Integration Tests

**Ubicación:** `codeflowx-governance-hitl-service/src/test/java/com/codeflowx/govern/hitl/controller/`

```java
@SpringBootTest
@AutoConfigureMockMvc
class HitlControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HitlSupervisionBusinessService businessService;

    @Test
    void testGetDashboard() throws Exception {
        // Arrange
        HitlDashboardData mockData = createMockDashboardData();
        when(businessService.getDashboardMetrics()).thenReturn(mockData);

        // Act & Assert
        mockMvc.perform(get("/api/v1/hitl/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metrics").exists())
                .andExpect(jsonPath("$.metrics.pendingInterventions").value(10));
    }

    @Test
    void testRecordDecision_NotFound() throws Exception {
        // Arrange
        when(businessService.recordDecision(anyLong(), anyString(), anyString(), anyString()))
            .thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/hitl/decisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"interventionId\":999,\"decision\":\"APPROVED\",\"reason\":\"Test\",\"userId\":\"user123\"}"))
                .andExpect(status().isNotFound());
    }
}
```

### Testing de Mapeo

```java
@Test
void testToDashboardDto() {
    // Arrange
    HitlDashboardData data = createMockDashboardData();
    HitlController controller = new HitlController(mockBusinessService);

    // Act
    HitlDashboardDto dto = controller.toDashboardDto(data);

    // Assert
    assertNotNull(dto);
    assertEquals(data.metrics.pendingInterventions, dto.getMetrics().getPendingInterventions());
    assertEquals(data.pendingInterventions.size(), dto.getPendingInterventions().size());
}
```

---

## 🎯 MEJORES PRÁCTICAS

### 1. **Separación de Capas**
- Business Services trabajan con Entidades JPA
- Controllers trabajan con DTOs
- Conversión en controllers, no en business services

### 2. **Manejo de Errores**
- Usar excepciones específicas
- No exponer detalles internos en DTOs
- Logging apropiado

### 3. **Transacciones**
- Usar `@Transactional` en business services
- No usar transacciones en controllers reactivos

### 4. **BPMN Workflows**
- Manejar errores de BPMN sin fallar la operación principal
- Logging de errores de workflows

### 5. **Python Microservices**
- ✅ Llamadas reales implementadas en `performAutoEvaluation()`
- Hacer llamadas opcionales (no críticas)
- Manejar timeouts y errores gracefully (`ServiceUnavailableException`, `AIGovernanceException`)
- No bloquear operaciones principales
- Resultados almacenados en JSON en campo `hitlconfiguration`

**Estado Actual:**
- ✅ Llamadas reales implementadas para PROMPT, MODEL y AGENT
- ✅ Método `performAutoEvaluation()` ejecutándose en producción
- ✅ Resultados almacenados automáticamente al crear intervención

---

## 🔗 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **BPMN Integration:** Documentación de `BpmnWorkflowClient`
- **Python Microservices:**
  - `codeflowx.govern.nocode.client/GUIA_USO_CLIENTE.md`
  - `codeflowx.govern.nocode.client/AGENT_MONITORING_CLIENT_GUIDE.md`
  - `codeflowx.govern.nocode.client/LLMEVALUATION_CLIENT_GUIDE.md`
  - `codeflowx.govern.nocode.client/PROMPT_GOVERNANCE_CLIENT_GUIDE.md`
- **DTOs Normative:** `docs/ARQUITECTURA_FRONTEND.md` (sección DTOs)
- **Código de Implementación:** `HitlSupervisionBusinessService.performAutoEvaluation()`

---

## 🔧 TROUBLESHOOTING Y PROBLEMAS COMUNES

### Problema 1: Error "Intervention not found"

**Síntoma:** Al registrar una decisión, se recibe 404 Not Found.

**Causas posibles:**
- La intervención no existe en la base de datos
- El `interventionId` es incorrecto
- La intervención ya fue resuelta (tiene una decisión asociada)

**Solución:**
1. Verificar que la intervención existe: `SELECT * FROM hitl_supervision WHERE id = ?`
2. Verificar que no tiene decisión: `SELECT * FROM hitl_decision WHERE idxhitlsupervision = ?`
3. Revisar logs del business service para ver el error específico

### Problema 2: Error "Invalid decision type"

**Síntoma:** Al registrar una decisión, se recibe 400 Bad Request.

**Causas posibles:**
- El valor de `decision` no es uno de: APPROVED, REJECTED, MODIFIED
- El valor viene en minúsculas o con formato incorrecto

**Solución:**
1. Verificar que el frontend envía el valor correcto en mayúsculas
2. Validar en el frontend antes de enviar
3. Revisar logs: `IllegalArgumentException: Invalid decision type`

### Problema 3: Workflow BPMN no se dispara

**Síntoma:** La decisión se guarda pero el workflow BPMN no se ejecuta.

**Causas posibles:**
- `BpmnWorkflowClient` no está configurado (`@Autowired(required = false)`)
- El servicio BPMN no está disponible
- Error en las variables del workflow

**Solución:**
1. Verificar que `BpmnWorkflowClient` está inyectado: Revisar logs de inicio
2. Verificar conectividad con servicio BPMN
3. Revisar logs: `Error al disparar workflow BPMN`
4. **Nota:** El workflow es opcional, la decisión se guarda aunque falle

### Problema 4: Evaluación automática Python no funciona

**Síntoma:** No se obtienen resultados de evaluación automática.

**Causas posibles:**
- `AIGovernanceClient` no está configurado
- El gateway de Python no está disponible
- Error en la configuración de `application.yml`

**Solución:**
1. Verificar configuración en `application.yml`:
   ```yaml
   codeflowx:
     leka:
       governance:
         enabled: true
         gateway:
           url: http://api-leka-govern:8000
   ```
2. Verificar que el gateway está disponible: `curl http://api-leka-govern:8000/health`
3. Revisar logs: `Microservicio de Python no disponible`
4. **Nota:** La evaluación automática es opcional, no bloquea la operación

### Problema 5: Error de conversión DTO

**Síntoma:** Error al convertir entre clase de datos interna y DTO.

**Causas posibles:**
- Campo null en clase de datos interna
- Tipo de dato incorrecto
- Campo faltante en el mapeo

**Solución:**
1. Revisar método de mapeo en `HitlController`
2. Agregar null checks en métodos de mapeo
3. Verificar que todos los campos están mapeados
4. Revisar logs para identificar el campo problemático

### Problema 6: Métricas incorrectas en dashboard

**Síntoma:** Las métricas del dashboard muestran valores incorrectos.

**Causas posibles:**
- Error en cálculo de métricas
- Datos inconsistentes en base de datos
- Problema con fechas/SLA

**Solución:**
1. Revisar método `calculateMetrics()` en `HitlSupervisionBusinessService`
2. Verificar datos en base de datos:
   ```sql
   SELECT status, COUNT(*) FROM hitl_supervision GROUP BY status;
   SELECT decision, COUNT(*) FROM hitl_decision GROUP BY decision;
   ```
3. Verificar cálculos de SLA y tiempo de respuesta
4. Revisar logs del método `getDashboard()`

---

## 🚀 GUÍA PARA IMPLEMENTAR NUEVAS FUNCIONALIDADES

### Paso 1: Definir la Funcionalidad

1. **Identificar el flujo:**
   - ¿Qué entidad JPA se necesita?
   - ¿Qué repositorio se necesita?
   - ¿Qué validaciones se requieren?

2. **Definir el contrato:**
   - ¿Qué DTOs se necesitan?
   - ¿Qué endpoints REST se necesitan?

### Paso 2: Implementar en Business Service

1. **Crear método en `HitlSupervisionBusinessService`:**
   ```java
   public HitlNewData newFunctionality(Long param1, String param2) {
       // 1. Validar parámetros
       if (param1 == null) {
           throw new IllegalArgumentException("param1 is required");
       }

       // 2. Consultar repositorios
       HitlSupervision supervision = supervisionRepository.findById(param1)
           .orElseThrow(() -> new EntityNotFoundException("Supervision not found"));

       // 3. Lógica de negocio
       // ...

       // 4. Retornar clase de datos interna (NO DTO)
       return new HitlNewData();
   }
   ```

2. **Crear clase de datos interna si es necesario:**
   ```java
   public static class HitlNewData {
       public Long id;
       public String field1;
       // ... más campos
   }
   ```

### Paso 3: Implementar en Controller

1. **Crear endpoint en `HitlController`:**
   ```java
   @GetMapping("/new-endpoint")
   @Operation(summary = "Nueva funcionalidad")
   public Mono<ResponseEntity<HitlNewDto>> newEndpoint(
           @RequestParam Long param1,
           @RequestParam String param2) {
       return Mono.fromCallable(() ->
               hitlBusinessService.newFunctionality(param1, param2))
           .subscribeOn(Schedulers.boundedElastic())
           .map(data -> {
               HitlNewDto dto = toNewDto(data);
               return ResponseEntity.ok(dto);
           })
           .onErrorResume(error -> {
               log.error("Error in new functionality", error);
               return Mono.just(ResponseEntity
                   .<HitlNewDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
           });
   }
   ```

2. **Crear método de mapeo:**
   ```java
   private HitlNewDto toNewDto(HitlNewData data) {
       HitlNewDto dto = new HitlNewDto();
       dto.setId(data.id);
       dto.setField1(data.field1);
       return dto;
   }
   ```

### Paso 4: Crear DTO

1. **Crear DTO en módulo de DTOs:**
   ```java
   @Data
   public class HitlNewDto {
       private Long id;
       private String field1;
       // ... más campos
   }
   ```

2. **Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

### Paso 5: Integrar BPMN (si es necesario)

```java
try {
    Map<String, Object> variables = Map.of(
        "param1", param1,
        "param2", param2
    );
    bpmnWorkflowClient.startProcess("new-process", variables);
} catch (Exception e) {
    log.error("Error al disparar workflow BPMN", e);
    // No fallar la operación si el workflow falla
}
```

### Paso 6: Integrar Python Microservices (si es necesario)

```java
if (aiGovernanceClient != null) {
    try {
        // Llamada a microservicio Python
        PromptGovernanceClient client = aiGovernanceClient.promptGovernance();
        PromptSafetyResponse response = client.evaluateSafety(request);
        // Procesar respuesta
    } catch (ServiceUnavailableException e) {
        log.warn("Microservicio no disponible", e);
    } catch (AIGovernanceException e) {
        log.warn("Error en microservicio", e);
    }
}
```

### Paso 7: Testing

1. **Unit Test del Business Service:**
   ```java
   @Test
   void testNewFunctionality() {
       // Arrange
       when(supervisionRepository.findById(1L))
           .thenReturn(Optional.of(createMockSupervision()));

       // Act
       HitlNewData result = businessService.newFunctionality(1L, "test");

       // Assert
       assertNotNull(result);
       assertEquals("test", result.field1);
   }
   ```

2. **Integration Test del Controller:**
   ```java
   @Test
   void testNewEndpoint() throws Exception {
       mockMvc.perform(get("/api/v1/hitl/new-endpoint")
               .param("param1", "1")
               .param("param2", "test"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.field1").value("test"));
   }
   ```

### Checklist de Implementación

- [ ] Método implementado en Business Service
- [ ] Clase de datos interna creada (si es necesaria)
- [ ] Endpoint REST creado en Controller
- [ ] Método de mapeo DTO implementado
- [ ] DTO creado en módulo de DTOs
- [ ] Validaciones implementadas
- [ ] Manejo de excepciones implementado
- [ ] Integración BPMN (si aplica)
- [ ] Integración Python (si aplica)
- [ ] Unit tests escritos
- [ ] Integration tests escritos
- [ ] Documentación actualizada

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.1
