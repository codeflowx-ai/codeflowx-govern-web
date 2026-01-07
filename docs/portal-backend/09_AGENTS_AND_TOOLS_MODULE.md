# Módulo de Agentes y Herramientas (AGT)

## Descripción General

El módulo de **Agentes y Herramientas (AGT)** es el núcleo de la plataforma Leka, implementando un sistema de agentes de IA especializados que actúan como el "cerebro" de la plataforma. Este módulo transforma Leka en una plataforma unificada de datos y ML similar a Databricks, pero sin dependencia de Spark y en self-hosting.

### Características Principales

- **Agentes Especializados**: Agentes de IA para diferentes dominios (datos, ML, desarrollo, análisis)
- **Orquestación BPMN**: Diseñador de flujos de trabajo para coordinar agentes
- **Integración Leka Server**: Conectividad con servicios externos y herramientas
- **Frontend Multi-Agente**: Dashboard para gestionar y monitorear agentes
- **Arquitectura LangChain/LangGraph**: Framework moderno para agentes de IA
- **Auto-clasificación**: Clasificación automática de contenido usando LLMs

## Arquitectura del Sistema

### Componentes Principales

1. **Agent Orchestrator**: Coordina la ejecución de agentes y flujos de trabajo
2. **Agent Registry**: Catálogo de agentes disponibles y sus capacidades
3. **Tool Manager**: Gestión de herramientas y conectores externos
4. **Workflow Engine**: Motor de ejecución de flujos BPMN
5. **Agent Monitor**: Monitoreo y métricas de agentes en tiempo real

### Integración con Otros Módulos

- **RAG Module**: Agentes especializados en búsqueda y generación
- **Training Module**: Agentes para entrenamiento y optimización de modelos
- **Infrastructure Module**: Agentes para gestión de infraestructura
- **Project Management**: Agentes para gestión de proyectos y tareas

## Entidades del Sistema

### 1. Agent

```java
@Entity
@Table(name = "agt_agents")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_type")
    @Enumerated(EnumType.STRING)
    private AgentType agentType;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "agent_config")
    private String agentConfig; // JSON configuration

    @Column(name = "prompt_template")
    private String promptTemplate;

    @Column(name = "model_config")
    private String modelConfig; // JSON model configuration

    @Column(name = "tools_config")
    private String toolsConfig; // JSON tools configuration

    @Column(name = "memory_config")
    private String memoryConfig; // JSON memory configuration

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum AgentType {
    DATA_ANALYST, ML_ENGINEER, CODE_REVIEWER, DOCUMENT_PROCESSOR,
    RESEARCH_ASSISTANT, PROJECT_MANAGER, INFRASTRUCTURE_MANAGER,
    COMPLIANCE_OFFICER, QUALITY_ASSURANCE, CUSTOM
}
```

### 2. AgentTool

```java
@Entity
@Table(name = "agt_agent_tools")
public class AgentTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "tool_name")
    private String toolName;

    @Column(name = "tool_type")
    @Enumerated(EnumType.STRING)
    private ToolType toolType;

    @Column(name = "tool_config")
    private String toolConfig; // JSON configuration

    @Column(name = "endpoint_url")
    private String endpointUrl;

    @Column(name = "api_key")
    private String apiKey; // Encrypted

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ToolType {
    DATABASE_CONNECTOR, API_CLIENT, FILE_PROCESSOR, WEB_SCRAPER,
    ML_MODEL, VECTOR_DATABASE, MESSAGE_QUEUE, CUSTOM
}
```

### 3. AgentWorkflow

```java
@Entity
@Table(name = "agt_agent_workflows")
public class AgentWorkflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_name")
    private String workflowName;

    @Column(name = "description")
    private String description;

    @Column(name = "bpmn_definition")
    private String bpmnDefinition; // XML BPMN definition

    @Column(name = "workflow_config")
    private String workflowConfig; // JSON configuration

    @Column(name = "version")
    private String version;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 4. WorkflowExecution

```java
@Entity
@Table(name = "agt_workflow_executions")
public class WorkflowExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_id")
    private Long workflowId;

    @Column(name = "execution_id")
    private String executionId; // UUID

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "execution_log")
    private String executionLog; // JSON execution log

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ExecutionStatus {
    PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, TIMEOUT
}
```

### 5. AgentExecution

```java
@Entity
@Table(name = "agt_agent_executions")
public class AgentExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "workflow_execution_id")
    private Long workflowExecutionId;

    @Column(name = "execution_id")
    private String executionId; // UUID

    @Column(name = "input_data")
    private String inputData; // JSON input data

    @Column(name = "output_data")
    private String outputData; // JSON output data

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 6. AgentMemory

```java
@Entity
@Table(name = "agt_agent_memories")
public class AgentMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "memory_type")
    @Enumerated(EnumType.STRING)
    private MemoryType memoryType;

    @Column(name = "memory_key")
    private String memoryKey;

    @Column(name = "memory_value")
    private String memoryValue; // JSON memory value

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum MemoryType {
    CONVERSATION, CONTEXT, KNOWLEDGE, TEMPORARY, PERSISTENT
}
```

### 7. AgentPerformance

```java
@Entity
@Table(name = "agt_agent_performances")
public class AgentPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "metric_name")
    private String metricName;

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "metric_unit")
    private String metricUnit;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

### 8. LekaServerIntegration

```java
@Entity
@Table(name = "agt_leka_server_integrations")
public class LekaServerIntegration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integration_name")
    private String integrationName;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private LekaServiceType serviceType;

    @Column(name = "endpoint_url")
    private String endpointUrl;

    @Column(name = "api_key")
    private String apiKey; // Encrypted

    @Column(name = "service_config")
    private String serviceConfig; // JSON configuration

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_health_check")
    private LocalDateTime lastHealthCheck;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum LekaServiceType {
    MODEL_SEARCH, DATASET_DOWNLOAD, WEB_SCRAPING, DOCUMENT_PROCESSING,
    CODE_GENERATION, API_GATEWAY, CUSTOM
}
```

## Scripts SQL

### DROP TABLES

```sql
-- Eliminar tablas en orden inverso (por dependencias)
DROP TABLE IF EXISTS agt_agent_performances CASCADE;
DROP TABLE IF EXISTS agt_agent_memories CASCADE;
DROP TABLE IF EXISTS agt_agent_executions CASCADE;
DROP TABLE IF EXISTS agt_workflow_executions CASCADE;
DROP TABLE IF EXISTS agt_agent_workflows CASCADE;
DROP TABLE IF EXISTS agt_agent_tools CASCADE;
DROP TABLE IF EXISTS agt_leka_server_integrations CASCADE;
DROP TABLE IF EXISTS agt_agents CASCADE;
```

### CREATE TABLES

```sql
-- Tabla de agentes
CREATE TABLE agt_agents (
    id BIGSERIAL PRIMARY KEY,
    agent_name VARCHAR(255) NOT NULL,
    agent_type VARCHAR(50) NOT NULL,
    description TEXT,
    version VARCHAR(50),
    agent_config TEXT, -- JSON configuration
    prompt_template TEXT,
    model_config TEXT, -- JSON model configuration
    tools_config TEXT, -- JSON tools configuration
    memory_config TEXT, -- JSON memory configuration
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de herramientas de agentes
CREATE TABLE agt_agent_tools (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT REFERENCES agt_agents(id) ON DELETE CASCADE,
    tool_name VARCHAR(255) NOT NULL,
    tool_type VARCHAR(50) NOT NULL,
    tool_config TEXT, -- JSON configuration
    endpoint_url VARCHAR(500),
    api_key VARCHAR(255), -- Encrypted
    is_enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de flujos de trabajo de agentes
CREATE TABLE agt_agent_workflows (
    id BIGSERIAL PRIMARY KEY,
    workflow_name VARCHAR(255) NOT NULL,
    description TEXT,
    bpmn_definition TEXT, -- XML BPMN definition
    workflow_config TEXT, -- JSON configuration
    version VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de ejecuciones de flujos de trabajo
CREATE TABLE agt_workflow_executions (
    id BIGSERIAL PRIMARY KEY,
    workflow_id BIGINT REFERENCES agt_agent_workflows(id) ON DELETE CASCADE,
    execution_id VARCHAR(255) UNIQUE NOT NULL, -- UUID
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    execution_log TEXT, -- JSON execution log
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de ejecuciones de agentes
CREATE TABLE agt_agent_executions (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT REFERENCES agt_agents(id) ON DELETE CASCADE,
    workflow_execution_id BIGINT REFERENCES agt_workflow_executions(id) ON DELETE CASCADE,
    execution_id VARCHAR(255) UNIQUE NOT NULL, -- UUID
    input_data TEXT, -- JSON input data
    output_data TEXT, -- JSON output data
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    execution_time_ms BIGINT,
    tokens_used INTEGER,
    cost DECIMAL(10,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de memoria de agentes
CREATE TABLE agt_agent_memories (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT REFERENCES agt_agents(id) ON DELETE CASCADE,
    memory_type VARCHAR(50) NOT NULL,
    memory_key VARCHAR(255) NOT NULL,
    memory_value TEXT, -- JSON memory value
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de rendimiento de agentes
CREATE TABLE agt_agent_performances (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT REFERENCES agt_agents(id) ON DELETE CASCADE,
    metric_name VARCHAR(255) NOT NULL,
    metric_value DECIMAL(15,6),
    metric_unit VARCHAR(50),
    timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de integraciones con Leka Server
CREATE TABLE agt_leka_server_integrations (
    id BIGSERIAL PRIMARY KEY,
    integration_name VARCHAR(255) NOT NULL,
    service_type VARCHAR(50) NOT NULL,
    endpoint_url VARCHAR(500),
    api_key VARCHAR(255), -- Encrypted
    service_config TEXT, -- JSON configuration
    is_active BOOLEAN DEFAULT true,
    last_health_check TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### CREATE INDEXES

```sql
-- Índices para optimización
CREATE INDEX idx_agt_agents_type ON agt_agents(agent_type);
CREATE INDEX idx_agt_agents_active ON agt_agents(is_active);
CREATE INDEX idx_agt_agent_tools_agent_id ON agt_agent_tools(agent_id);
CREATE INDEX idx_agt_agent_tools_type ON agt_agent_tools(tool_type);
CREATE INDEX idx_agt_agent_tools_enabled ON agt_agent_tools(is_enabled);
CREATE INDEX idx_agt_agent_workflows_active ON agt_agent_workflows(is_active);
CREATE INDEX idx_agt_workflow_executions_workflow_id ON agt_workflow_executions(workflow_id);
CREATE INDEX idx_agt_workflow_executions_status ON agt_workflow_executions(status);
CREATE INDEX idx_agt_workflow_executions_execution_id ON agt_workflow_executions(execution_id);
CREATE INDEX idx_agt_agent_executions_agent_id ON agt_agent_executions(agent_id);
CREATE INDEX idx_agt_agent_executions_workflow_execution_id ON agt_agent_executions(workflow_execution_id);
CREATE INDEX idx_agt_agent_executions_status ON agt_agent_executions(status);
CREATE INDEX idx_agt_agent_executions_execution_id ON agt_agent_executions(execution_id);
CREATE INDEX idx_agt_agent_memories_agent_id ON agt_agent_memories(agent_id);
CREATE INDEX idx_agt_agent_memories_type ON agt_agent_memories(memory_type);
CREATE INDEX idx_agt_agent_memories_expires ON agt_agent_memories(expires_at);
CREATE INDEX idx_agt_agent_performances_agent_id ON agt_agent_performances(agent_id);
CREATE INDEX idx_agt_agent_performances_timestamp ON agt_agent_performances(timestamp);
CREATE INDEX idx_agt_leka_server_integrations_type ON agt_leka_server_integrations(service_type);
CREATE INDEX idx_agt_leka_server_integrations_active ON agt_leka_server_integrations(is_active);
```

### INSERT DEMO DATA

```sql
-- Insertar agentes de demostración
INSERT INTO agt_agents (agent_name, agent_type, description, version, agent_config, prompt_template, model_config, tools_config, memory_config, is_active) VALUES
('DataAnalyst_001', 'DATA_ANALYST', 'Agente especializado en análisis de datos y generación de insights', '1.0.0', '{"max_tokens": 4000, "temperature": 0.1}', 'Eres un analista de datos experto. Analiza los datos proporcionados y genera insights valiosos.', '{"model": "gpt-4", "provider": "openai"}', '{"tools": ["pandas", "matplotlib", "seaborn"]}', '{"memory_type": "conversation", "max_messages": 50}', true),
('MLEngineer_001', 'ML_ENGINEER', 'Agente especializado en ingeniería de machine learning y optimización de modelos', '1.0.0', '{"max_tokens": 4000, "temperature": 0.2}', 'Eres un ingeniero de ML experto. Ayuda a optimizar y entrenar modelos de machine learning.', '{"model": "gpt-4", "provider": "openai"}', '{"tools": ["scikit-learn", "tensorflow", "pytorch"]}', '{"memory_type": "knowledge", "max_items": 100}', true),
('CodeReviewer_001', 'CODE_REVIEWER', 'Agente especializado en revisión de código y análisis de calidad', '1.0.0', '{"max_tokens": 3000, "temperature": 0.1}', 'Eres un revisor de código experto. Analiza el código y proporciona sugerencias de mejora.', '{"model": "gpt-4", "provider": "openai"}', '{"tools": ["pylint", "black", "mypy"]}', '{"memory_type": "context", "max_items": 25}', true);

-- Insertar herramientas de agentes
INSERT INTO agt_agent_tools (agent_id, tool_name, tool_type, tool_config, endpoint_url, is_enabled) VALUES
(1, 'PandasAnalyzer', 'CUSTOM', '{"data_processing": true, "statistical_analysis": true}', 'http://localhost:8001/api/v1/tools/pandas', true),
(1, 'MatplotlibVisualizer', 'CUSTOM', '{"chart_types": ["line", "bar", "scatter", "histogram"]}', 'http://localhost:8001/api/v1/tools/matplotlib', true),
(2, 'ScikitLearnTrainer', 'CUSTOM', '{"algorithms": ["random_forest", "svm", "neural_network"]}', 'http://localhost:8001/api/v1/tools/scikit-learn', true),
(3, 'CodeQualityAnalyzer', 'CUSTOM', '{"metrics": ["complexity", "maintainability", "test_coverage"]}', 'http://localhost:8001/api/v1/tools/code-quality', true);

-- Insertar flujos de trabajo
INSERT INTO agt_agent_workflows (workflow_name, description, bpmn_definition, workflow_config, version, is_active) VALUES
('DataAnalysisWorkflow', 'Flujo de trabajo para análisis de datos automatizado', '<?xml version="1.0" encoding="UTF-8"?><bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL">...</bpmn:definitions>', '{"steps": ["data_ingestion", "cleaning", "analysis", "visualization", "reporting"]}', '1.0.0', true),
('MLTrainingWorkflow', 'Flujo de trabajo para entrenamiento de modelos ML', '<?xml version="1.0" encoding="UTF-8"?><bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL">...</bpmn:definitions>', '{"steps": ["data_preparation", "feature_engineering", "model_selection", "training", "evaluation"]}', '1.0.0', true);

-- Insertar integraciones con Leka Server
INSERT INTO agt_leka_server_integrations (integration_name, service_type, endpoint_url, service_config, is_active) VALUES
('ModelSearchService', 'MODEL_SEARCH', 'http://localhost:8002/api/v1/models/search', '{"search_engines": ["huggingface", "openai", "anthropic"]}', true),
('DatasetDownloadService', 'DATASET_DOWNLOAD', 'http://localhost:8002/api/v1/datasets/download', '{"providers": ["huggingface", "kaggle", "uci"]}', true),
('WebScrapingService', 'WEB_SCRAPING', 'http://localhost:8002/api/v1/scraping/execute', '{"browsers": ["chrome", "firefox"], "rate_limiting": true}', true);
```

## API Endpoints

### Agents

```plaintext
GET    /api/v1/agents
GET    /api/v1/agents/{id}
POST   /api/v1/agents
PUT    /api/v1/agents/{id}
DELETE /api/v1/agents/{id}
GET    /api/v1/agents/type/{agentType}
GET    /api/v1/agents/search
POST   /api/v1/agents/{id}/execute
POST   /api/v1/agents/{id}/train
GET    /api/v1/agents/{id}/performance
```

### Agent Tools

```plaintext
GET    /api/v1/agents/{agentId}/tools
GET    /api/v1/agents/{agentId}/tools/{id}
POST   /api/v1/agents/{agentId}/tools
PUT    /api/v1/agents/{agentId}/tools/{id}
DELETE /api/v1/agents/{agentId}/tools/{id}
POST   /api/v1/agents/{agentId}/tools/{id}/test
GET    /api/v1/agents/{agentId}/tools/types
```

### Agent Workflows

```plaintext
GET    /api/v1/agent-workflows
GET    /api/v1/agent-workflows/{id}
POST   /api/v1/agent-workflows
PUT    /api/v1/agent-workflows/{id}
DELETE /api/v1/agent-workflows/{id}
POST   /api/v1/agent-workflows/{id}/execute
GET    /api/v1/agent-workflows/{id}/executions
GET    /api/v1/agent-workflows/search
```

### Workflow Executions

```plaintext
GET    /api/v1/workflow-executions
GET    /api/v1/workflow-executions/{id}
GET    /api/v1/workflow-executions/workflow/{workflowId}
GET    /api/v1/workflow-executions/status/{status}
POST   /api/v1/workflow-executions/{id}/cancel
GET    /api/v1/workflow-executions/{id}/logs
```

### Agent Executions

```plaintext
GET    /api/v1/agent-executions
GET    /api/v1/agent-executions/{id}
GET    /api/v1/agent-executions/agent/{agentId}
GET    /api/v1/agent-executions/workflow/{workflowExecutionId}
GET    /api/v1/agent-executions/status/{status}
GET    /api/v1/agent-executions/{id}/metrics
```

### Agent Memory

```plaintext
GET    /api/v1/agents/{agentId}/memories
GET    /api/v1/agents/{agentId}/memories/{id}
POST   /api/v1/agents/{agentId}/memories
PUT    /api/v1/agents/{agentId}/memories/{id}
DELETE /api/v1/agents/{agentId}/memories/{id}
GET    /api/v1/agents/{agentId}/memories/type/{memoryType}
POST   /api/v1/agents/{agentId}/memories/search
```

### Leka Server Integrations

```plaintext
GET    /api/v1/leka-server-integrations
GET    /api/v1/leka-server-integrations/{id}
POST   /api/v1/leka-server-integrations
PUT    /api/v1/leka-server-integrations/{id}
DELETE /api/v1/leka-server-integrations/{id}
POST   /api/v1/leka-server-integrations/{id}/test
GET    /api/v1/leka-server-integrations/types
GET    /api/v1/leka-server-integrations/health
```

### Advanced Agent Features

```plaintext
POST   /api/v1/agents/{id}/multi-agent-collaboration
POST   /api/v1/agents/{id}/context-switching
POST   /api/v1/agents/{id}/memory-optimization
POST   /api/v1/agents/{id}/performance-tuning
POST   /api/v1/agents/{id}/tool-discovery
POST   /api/v1/agents/{id}/learning-adaptation
```

### Agent Analytics and Monitoring

```plaintext
GET    /api/v1/agents/analytics/overview
GET    /api/v1/agents/analytics/performance
GET    /api/v1/agents/analytics/usage
GET    /api/v1/agents/analytics/costs
GET    /api/v1/agents/analytics/efficiency
GET    /api/v1/agents/analytics/collaboration
```

## Servicios Java

### AgentService

```java
@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentExecutionService executionService;

    @Autowired
    private LangChainService langChainService;

    public Agent createAgent(AgentCreateRequest request) {
        Agent agent = new Agent();
        agent.setAgentName(request.getAgentName());
        agent.setAgentType(request.getAgentType());
        agent.setDescription(request.getDescription());
        agent.setAgentConfig(request.getAgentConfig());
        agent.setPromptTemplate(request.getPromptTemplate());
        agent.setModelConfig(request.getModelConfig());
        agent.setToolsConfig(request.getToolsConfig());
        agent.setMemoryConfig(request.getMemoryConfig());

        return agentRepository.save(agent);
    }

    public AgentExecution executeAgent(Long agentId, String inputData) {
        Agent agent = agentRepository.findById(agentId)
            .orElseThrow(() -> new AgentNotFoundException("Agent not found: " + agentId));

        return executionService.executeAgent(agent, inputData);
    }

    public List<Agent> getAgentsByType(AgentType agentType) {
        return agentRepository.findByAgentType(agentType);
    }

    public AgentPerformance getAgentPerformance(Long agentId) {
        return executionService.getAgentPerformance(agentId);
    }
}
```

### AgentWorkflowService

```java
@Service
public class AgentWorkflowService {

    @Autowired
    private AgentWorkflowRepository workflowRepository;

    @Autowired
    private WorkflowExecutionService executionService;

    @Autowired
    private BPMNEngine bpmnEngine;

    public AgentWorkflow createWorkflow(WorkflowCreateRequest request) {
        AgentWorkflow workflow = new AgentWorkflow();
        workflow.setWorkflowName(request.getWorkflowName());
        workflow.setDescription(request.getDescription());
        workflow.setBpmnDefinition(request.getBpmnDefinition());
        workflow.setWorkflowConfig(request.getWorkflowConfig());
        workflow.setVersion(request.getVersion());

        return workflowRepository.save(workflow);
    }

    public WorkflowExecution executeWorkflow(Long workflowId, String inputData) {
        AgentWorkflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new WorkflowNotFoundException("Workflow not found: " + workflowId));

        return executionService.executeWorkflow(workflow, inputData);
    }

    public List<WorkflowExecution> getWorkflowExecutions(Long workflowId) {
        return executionService.getExecutionsByWorkflow(workflowId);
    }

    public void validateBPMN(String bpmnDefinition) {
        bpmnEngine.validate(bpmnDefinition);
    }
}
```

### LekaServerIntegrationService

```java
@Service
public class LekaServerIntegrationService {

    @Autowired
    private LekaServerIntegrationRepository integrationRepository;

    @Autowired
    private RestTemplate restTemplate;

    public LekaServerIntegration createIntegration(IntegrationCreateRequest request) {
        LekaServerIntegration integration = new LekaServerIntegration();
        integration.setIntegrationName(request.getIntegrationName());
        integration.setServiceType(request.getServiceType());
        integration.setEndpointUrl(request.getEndpointUrl());
        integration.setServiceConfig(request.getServiceConfig());

        return integrationRepository.save(integration);
    }

    public Object callLekaService(Long integrationId, String servicePath, Object request) {
        LekaServerIntegration integration = integrationRepository.findById(integrationId)
            .orElseThrow(() -> new IntegrationNotFoundException("Integration not found: " + integrationId));

        String url = integration.getEndpointUrl() + servicePath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + integration.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
            url, HttpMethod.POST, entity, Object.class);

        return response.getBody();
    }

    public boolean testConnection(Long integrationId) {
        try {
            callLekaService(integrationId, "/health", null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

## Métricas y Monitoreo

### Prometheus Metrics

```java
@Component
public class AgentMetrics {

    private final Counter agentExecutionCounter;
    private final Counter agentExecutionErrorCounter;
    private final Histogram agentExecutionDuration;
    private final Gauge activeAgentsGauge;
    private final Counter workflowExecutionCounter;

    public AgentMetrics(MeterRegistry meterRegistry) {
        this.agentExecutionCounter = Counter.builder("agt_agent_executions_total")
            .description("Total number of agent executions")
            .register(meterRegistry);

        this.agentExecutionErrorCounter = Counter.builder("agt_agent_execution_errors_total")
            .description("Total number of agent execution errors")
            .register(meterRegistry);

        this.agentExecutionDuration = Histogram.builder("agt_agent_execution_duration_seconds")
            .description("Agent execution duration in seconds")
            .register(meterRegistry);

        this.activeAgentsGauge = Gauge.builder("agt_active_agents")
            .description("Number of active agents")
            .register(meterRegistry);

        this.workflowExecutionCounter = Counter.builder("agt_workflow_executions_total")
            .description("Total number of workflow executions")
            .register(meterRegistry);
    }

    public void recordAgentExecution() {
        agentExecutionCounter.increment();
    }

    public void recordAgentExecutionError() {
        agentExecutionErrorCounter.increment();
    }

    public void recordAgentExecutionDuration(double duration) {
        agentExecutionDuration.observe(duration);
    }

    public void setActiveAgents(int count) {
        activeAgentsGauge.set(count);
    }

    public void recordWorkflowExecution() {
        workflowExecutionCounter.increment();
    }
}
```

## Configuración de Aplicación

### application.yml

```yaml
agt:
  agents:
    default-model: gpt-4
    max-concurrent-executions: 10
    execution-timeout: 300s
    memory-retention-days: 30

  workflows:
    bpmn-engine: camunda
    max-workflow-instances: 100
    workflow-timeout: 3600s

  leka-server:
    connection-timeout: 30s
    read-timeout: 60s
    max-retries: 3
    retry-delay: 1s

  monitoring:
    metrics-enabled: true
    health-check-interval: 30s
    performance-tracking: true

langchain:
  memory:
    max-token-limit: 4000
    memory-type: conversation
    retention-policy: sliding-window

  tools:
    discovery-enabled: true
    auto-registration: true
    tool-caching: true

bpmn:
  engine: camunda
  database:
    url: jdbc:postgresql://localhost:5432/codeflowx_bpmn
    username: ${BPMN_DB_USERNAME:codeflowx}
    password: ${BPMN_DB_PASSWORD:codeflowx}
  execution:
    max-instances: 100
    timeout: 3600s
```

## Características Innovadoras

### 1. Auto-clasificación de Chunks

El sistema implementa clasificación automática de chunks usando LLMs para documentos heterogéneos:

- **Clasificación Inteligente**: Los LLMs analizan el contenido y asignan tags automáticamente
- **Tags Personalizables**: Los usuarios pueden definir tags específicos o usar los generados por LLMs
- **Fácil Remoción**: Los tags se pueden eliminar fácilmente si no son apropiados
- **Aprendizaje Continuo**: El sistema mejora la clasificación basándose en feedback del usuario

### 2. Orquestación BPMN

- **Diseñador Visual**: Interfaz drag-and-drop para crear flujos de trabajo
- **Integración con Agentes**: Los agentes se ejecutan como tareas en los flujos BPMN
- **Monitoreo en Tiempo Real**: Seguimiento de la ejecución de flujos y agentes
- **Escalabilidad**: Los flujos se pueden ejecutar en paralelo y distribuir

### 3. Colaboración Multi-Agente

- **Agentes Especializados**: Cada agente tiene un dominio específico de expertise
- **Comunicación Inteligente**: Los agentes pueden comunicarse y colaborar entre sí
- **Contexto Compartido**: Memoria compartida para mantener contexto entre agentes
- **Optimización Automática**: El sistema optimiza la colaboración basándose en resultados

### 4. Integración Leka Server

- **Servicios Externos**: Conectividad con servicios de búsqueda, descarga y procesamiento
- **Auto-descubrimiento**: Los agentes pueden descubrir y usar herramientas automáticamente
- **Gestión de Conectores**: Configuración centralizada de integraciones externas
- **Monitoreo de Salud**: Verificación continua de la conectividad con servicios externos

## Conclusión

El módulo de **Agentes y Herramientas** transforma Leka en una plataforma unificada de datos y ML, implementando:

- **Agentes de IA Especializados** para diferentes dominios
- **Orquestación BPMN** para flujos de trabajo complejos
- **Integración Leka Server** para conectividad externa
- **Auto-clasificación Inteligente** para gestión de contenido
- **Colaboración Multi-Agente** para tareas complejas
- **Monitoreo y Métricas** para optimización continua

Este módulo es fundamental para la visión de Leka como "Databricks sin Spark" en self-hosting, proporcionando una plataforma unificada y inteligente para gestión de datos, ML y automatización de procesos.
