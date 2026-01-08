# Módulo de Notebook y Agentes de IA - Portal Backend

## Descripción General

El módulo de **Notebook y Agentes de IA** proporciona un sistema completo para la creación, ejecución y gestión de agentes de inteligencia artificial reutilizables. Se integra con el frontend Next.js y permite a los usuarios crear agentes especializados que pueden ser compartidos, versionados y reutilizados en diferentes proyectos.

## Arquitectura del Sistema

### **Principios de Diseño**

- **Agentes Reutilizables**: Sistema de creación y gestión de agentes especializados
- **Notebooks Interactivos**: Entorno de desarrollo con celdas de código Python
- **Herramientas Personalizables**: Sistema de herramientas para agentes
- **Versionado y Control**: Control de versiones para agentes y notebooks
- **Colaboración**: Compartir y reutilizar agentes entre usuarios
- **Integración con IA**: Conexión con modelos externos y sistemas de IA

### **Flujo de Trabajo**

1. **Creación de Agente**: Usuario define configuración y herramientas
2. **Desarrollo en Notebook**: Creación y testing de código del agente
3. **Versionado**: Control de versiones y cambios
4. **Publicación**: Compartir agente en marketplace
5. **Reutilización**: Importar y usar agentes en otros proyectos
6. **Optimización**: Iteración basada en resultados y feedback

## Entidades del Sistema

### 1. **NotebookSession**

```java
@Entity
@Table(name = "ntb_notebook_sessions")
public class NotebookSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "tags")
    private String tags; // JSON array de tags

    @Column(name = "session_type")
    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum SessionType {
    AGENT_DEVELOPMENT, DATA_ANALYSIS, MODEL_TRAINING, AUTOMATION, RESEARCH, CUSTOM
}

public enum SessionStatus {
    DRAFT, ACTIVE, ARCHIVED, SHARED
}
```

### 2. **NotebookCell**

```java
@Entity
@Table(name = "ntb_notebook_cells")
public class NotebookCell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "cell_type")
    @Enumerated(EnumType.STRING)
    private CellType cellType;

    @Column(name = "content")
    private String content;

    @Column(name = "language")
    private String language;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CellStatus status;

    @Column(name = "output")
    private String output;

    @Column(name = "execution_time")
    private Float executionTime;

    @Column(name = "metadata")
    private String metadata; // JSON con metadatos

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum CellType {
    CODE, CONFIG, TOOL, TEST, MARKDOWN, AGENT_DEFINITION
}

public enum CellStatus {
    IDLE, RUNNING, SUCCESS, ERROR, STOPPED
}
```

### 3. **AgentTool**

```java
@Entity
@Table(name = "ntb_agent_tools")
public class AgentTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tool_name")
    private String toolName;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;

    @Column(name = "category")
    private String category;

    @Column(name = "parameters")
    private String parameters; // JSON con parámetros de la herramienta

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "usage_count")
    private Integer usageCount = 0;

    @Column(name = "rating")
    private Float rating = 0.0f;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 4. **AgentConfig**

```java
@Entity
@Table(name = "ntb_agent_configs")
public class AgentConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "config_name")
    private String configName;

    @Column(name = "description")
    private String description;

    @Column(name = "model")
    private String model;

    @Column(name = "temperature")
    private Float temperature = 0.7f;

    @Column(name = "max_tokens")
    private Integer maxTokens = 2000;

    @Column(name = "system_prompt")
    private String systemPrompt;

    @Column(name = "tools")
    private String tools; // JSON array de herramientas

    @Column(name = "memory_config")
    private String memoryConfig; // JSON con configuración de memoria

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 5. **ReusableAgent**

```java
@Entity
@Table(name = "ntb_reusable_agents")
public class ReusableAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version = "1.0.0";

    @Column(name = "agent_config")
    private String agentConfig; // JSON con configuración completa

    @Column(name = "tools_config")
    private String toolsConfig; // JSON con configuración de herramientas

    @Column(name = "system_prompt")
    private String systemPrompt;

    @Column(name = "category")
    private String category;

    @Column(name = "tags")
    private String tags; // JSON array de tags

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "rating")
    private Float rating = 0.0f;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "dependencies")
    private String dependencies; // JSON con dependencias

    @Column(name = "requirements")
    private String requirements; // JSON array de requisitos

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 6. **AgentVersion**

```java
@Entity
@Table(name = "ntb_agent_versions")
public class AgentVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "version")
    private String version;

    @Column(name = "changelog")
    private String changelog;

    @Column(name = "agent_config")
    private String agentConfig; // JSON con configuración de esta versión

    @Column(name = "tools_config")
    private String toolsConfig; // JSON con herramientas de esta versión

    @Column(name = "is_deprecated")
    private Boolean isDeprecated = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

### 7. **ExecutionLog**

```java
@Entity
@Table(name = "ntb_execution_logs")
public class ExecutionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cell_id")
    private Long cellId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "execution_start")
    private LocalDateTime executionStart;

    @Column(name = "execution_end")
    private LocalDateTime executionEnd;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = "output")
    private String output;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "execution_time")
    private Float executionTime;

    @Column(name = "memory_usage")
    private Long memoryUsage;

    @Column(name = "cpu_usage")
    private Float cpuUsage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ExecutionStatus {
    STARTED, COMPLETED, FAILED, TIMEOUT, CANCELLED
}
```

## Scripts SQL

### **DROP TABLES**

```sql
-- Eliminar tablas en orden inverso (por dependencias)
DROP TABLE IF EXISTS ntb_execution_logs CASCADE;
DROP TABLE IF EXISTS ntb_agent_versions CASCADE;
DROP TABLE IF EXISTS ntb_agent_dependencies CASCADE;
DROP TABLE IF EXISTS ntb_reusable_agents CASCADE;
DROP TABLE IF EXISTS ntb_agent_configs CASCADE;
DROP TABLE IF EXISTS ntb_agent_tools CASCADE;
DROP TABLE IF EXISTS ntb_notebook_cells CASCADE;
DROP TABLE IF EXISTS ntb_notebook_sessions CASCADE;
```

### **CREATE TABLES**

```sql
-- Tabla de sesiones de notebook
CREATE TABLE ntb_notebook_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    tags TEXT, -- JSON array de tags
    session_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES cor_users(id) ON DELETE CASCADE
);

-- Tabla de celdas del notebook
CREATE TABLE ntb_notebook_cells (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    cell_type VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    language VARCHAR(20) NOT NULL,
    order_index INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'idle',
    output TEXT,
    execution_time FLOAT,
    metadata TEXT, -- JSON con metadatos
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES ntb_notebook_sessions(id) ON DELETE CASCADE
);

-- Tabla de herramientas de agentes
CREATE TABLE ntb_agent_tools (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tool_name VARCHAR(100) NOT NULL,
    description TEXT,
    code TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    parameters TEXT NOT NULL, -- JSON con parámetros de la herramienta
    is_public BOOLEAN DEFAULT FALSE,
    is_verified BOOLEAN DEFAULT FALSE,
    usage_count INTEGER DEFAULT 0,
    rating FLOAT DEFAULT 0.0,
    rating_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES cor_users(id) ON DELETE CASCADE
);

-- Tabla de configuraciones de agentes
CREATE TABLE ntb_agent_configs (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    config_name VARCHAR(255) NOT NULL,
    description TEXT,
    model VARCHAR(100) NOT NULL,
    temperature FLOAT DEFAULT 0.7,
    max_tokens INTEGER DEFAULT 2000,
    system_prompt TEXT,
    tools TEXT, -- JSON array de herramientas
    memory_config TEXT, -- JSON con configuración de memoria
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES ntb_notebook_sessions(id) ON DELETE CASCADE
);

-- Tabla de agentes reutilizables
CREATE TABLE ntb_reusable_agents (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    agent_name VARCHAR(255) NOT NULL,
    description TEXT,
    version VARCHAR(20) NOT NULL DEFAULT '1.0.0',
    agent_config TEXT NOT NULL, -- JSON con configuración completa
    tools_config TEXT NOT NULL, -- JSON con configuración de herramientas
    system_prompt TEXT,
    category VARCHAR(100) NOT NULL,
    tags TEXT, -- JSON array de tags
    is_public BOOLEAN DEFAULT FALSE,
    is_verified BOOLEAN DEFAULT FALSE,
    download_count INTEGER DEFAULT 0,
    rating FLOAT DEFAULT 0.0,
    rating_count INTEGER DEFAULT 0,
    dependencies TEXT, -- JSON con dependencias
    requirements TEXT, -- JSON array de requisitos
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES cor_users(id) ON DELETE CASCADE
);

-- Tabla de versiones de agentes
CREATE TABLE ntb_agent_versions (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    version VARCHAR(20) NOT NULL,
    changelog TEXT,
    agent_config TEXT NOT NULL, -- JSON con configuración de esta versión
    tools_config TEXT NOT NULL, -- JSON con herramientas de esta versión
    is_deprecated BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES ntb_reusable_agents(id) ON DELETE CASCADE
);

-- Tabla de dependencias entre agentes
CREATE TABLE ntb_agent_dependencies (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    dependency_id BIGINT NOT NULL,
    dependency_type VARCHAR(50) NOT NULL, -- 'required', 'optional', 'conflicts'
    version_constraint VARCHAR(100), -- '>=1.0.0', '~=2.0.0'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES ntb_reusable_agents(id) ON DELETE CASCADE,
    FOREIGN KEY (dependency_id) REFERENCES ntb_reusable_agents(id) ON DELETE CASCADE
);

-- Tabla de logs de ejecución
CREATE TABLE ntb_execution_logs (
    id BIGSERIAL PRIMARY KEY,
    cell_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    execution_start TIMESTAMP NOT NULL,
    execution_end TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    output TEXT,
    error_message TEXT,
    execution_time FLOAT,
    memory_usage BIGINT,
    cpu_usage FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cell_id) REFERENCES ntb_notebook_cells(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES cor_users(id) ON DELETE CASCADE
);
```

### **CREATE INDEXES**

```sql
-- Índices para optimizar consultas por sesión
CREATE INDEX idx_ntb_notebook_cells_session_id ON ntb_notebook_cells(session_id);
CREATE INDEX idx_ntb_agent_configs_session_id ON ntb_agent_configs(session_id);

-- Índices para optimizar consultas por usuario
CREATE INDEX idx_ntb_notebook_sessions_user_id ON ntb_notebook_sessions(user_id);
CREATE INDEX idx_ntb_agent_tools_user_id ON ntb_agent_tools(user_id);
CREATE INDEX idx_ntb_reusable_agents_user_id ON ntb_reusable_agents(user_id);

-- Índices para optimizar consultas por tipo
CREATE INDEX idx_ntb_notebook_sessions_type ON ntb_notebook_sessions(session_type);
CREATE INDEX idx_ntb_notebook_sessions_status ON ntb_notebook_sessions(status);
CREATE INDEX idx_ntb_notebook_cells_type ON ntb_notebook_cells(cell_type);
CREATE INDEX idx_ntb_notebook_cells_status ON ntb_notebook_cells(status);
CREATE INDEX idx_ntb_agent_tools_category ON ntb_agent_tools(category);
CREATE INDEX idx_ntb_reusable_agents_category ON ntb_reusable_agents(category);

-- Índices para optimizar consultas por estado de ejecución
CREATE INDEX idx_ntb_execution_logs_status ON ntb_execution_logs(status);
CREATE INDEX idx_ntb_execution_logs_cell_id ON ntb_execution_logs(cell_id);

-- Índices para optimizar consultas por fecha
CREATE INDEX idx_ntb_notebook_sessions_created_at ON ntb_notebook_sessions(created_at);
CREATE INDEX idx_ntb_notebook_cells_created_at ON ntb_notebook_cells(created_at);
CREATE INDEX idx_ntb_reusable_agents_created_at ON ntb_reusable_agents(created_at);
CREATE INDEX idx_ntb_execution_logs_execution_start ON ntb_execution_logs(execution_start);

-- Índices para búsqueda de texto
CREATE INDEX idx_ntb_notebook_sessions_name ON ntb_notebook_sessions USING gin(to_tsvector('english', name));
CREATE INDEX idx_ntb_reusable_agents_name ON ntb_reusable_agents USING gin(to_tsvector('english', agent_name));
CREATE INDEX idx_ntb_agent_tools_name ON ntb_agent_tools USING gin(to_tsvector('english', tool_name));

-- Índices para optimizar consultas de marketplace
CREATE INDEX idx_ntb_reusable_agents_public_rating ON ntb_reusable_agents(is_public, rating) WHERE is_public = TRUE;
CREATE INDEX idx_ntb_agent_tools_public_rating ON ntb_agent_tools(is_public, rating) WHERE is_public = TRUE;

-- Índices para optimizar consultas de versionado
CREATE INDEX idx_ntb_agent_versions_agent_version ON ntb_agent_versions(agent_id, version);
CREATE INDEX idx_ntb_agent_dependencies_agent_dependency ON ntb_agent_dependencies(agent_id, dependency_id);
```

### **INSERT DEMO DATA**

```sql
-- Insertar sesiones de notebook de ejemplo
INSERT INTO ntb_notebook_sessions (user_id, name, description, session_type, tags, status) VALUES
(
    1,
    'Data Analysis Agent',
    'Notebook para desarrollo de agente de análisis de datos',
    'AGENT_DEVELOPMENT',
    '["data-analysis", "pandas", "numpy", "machine-learning"]',
    'ACTIVE'
),
(
    1,
    'Web Scraping Agent',
    'Notebook para desarrollo de agente de web scraping',
    'AGENT_DEVELOPMENT',
    '["web-scraping", "requests", "beautifulsoup4", "automation"]',
    'ACTIVE'
),
(
    1,
    'Customer Support Agent',
    'Notebook para desarrollo de agente de soporte al cliente',
    'AGENT_DEVELOPMENT',
    '["customer-support", "nlp", "chatbot", "automation"]',
    'DRAFT'
);

-- Insertar celdas de ejemplo para Data Analysis Agent
INSERT INTO ntb_notebook_cells (session_id, cell_type, content, language, order_index, status) VALUES
(
    1,
    'MARKDOWN',
    '# Data Analysis Agent\n\nEste notebook contiene el desarrollo de un agente especializado en análisis de datos.',
    'markdown',
    1,
    'SUCCESS'
),
(
    1,
    'CODE',
    'import pandas as pd\nimport numpy as np\nfrom sklearn.preprocessing import StandardScaler\n\nprint("Data Analysis libraries imported successfully!")',
    'python',
    2,
    'SUCCESS'
),
(
    1,
    'AGENT_DEFINITION',
    '{\n  "name": "DataAnalysisAgent",\n  "description": "Agente especializado en análisis de datos y machine learning",\n  "tools": ["pandas", "numpy", "sklearn"],\n  "capabilities": ["data_cleaning", "feature_engineering", "model_training"]\n}',
    'json',
    3,
    'SUCCESS'
),
(
    1,
    'CODE',
    'class DataAnalysisAgent:\n    def __init__(self):\n        self.name = "DataAnalysisAgent"\n        self.tools = ["pandas", "numpy", "sklearn"]\n    \n    def analyze_data(self, data):\n        return data.describe()\n\nagent = DataAnalysisAgent()\nprint(f"Agent {agent.name} created successfully!")',
    'python',
    4,
    'SUCCESS'
);

-- Insertar celdas de ejemplo para Web Scraping Agent
INSERT INTO ntb_notebook_cells (session_id, cell_type, content, language, order_index, status) VALUES
(
    2,
    'MARKDOWN',
    '# Web Scraping Agent\n\nEste notebook contiene el desarrollo de un agente especializado en web scraping.',
    'markdown',
    1,
    'SUCCESS'
),
(
    2,
    'CODE',
    'import requests\nfrom bs4 import BeautifulSoup\nimport time\n\nprint("Web scraping libraries imported successfully!")',
    'python',
    2,
    'SUCCESS'
),
(
    2,
    'AGENT_DEFINITION',
    '{\n  "name": "WebScrapingAgent",\n  "description": "Agente especializado en web scraping y extracción de datos",\n  "tools": ["requests", "beautifulsoup4", "selenium"],\n  "capabilities": ["data_extraction", "url_processing", "content_parsing"]\n}',
    'json',
    3,
    'SUCCESS'
);

-- Insertar herramientas de agentes de ejemplo
INSERT INTO ntb_agent_tools (user_id, tool_name, description, code, category, parameters, is_public, rating) VALUES
(
    1,
    'DataCleaner',
    'Herramienta para limpieza y preprocesamiento de datos',
    'def clean_data(data, remove_duplicates=True, fill_missing="mean"):\n    if remove_duplicates:\n        data = data.drop_duplicates()\n    if fill_missing == "mean":\n        data = data.fillna(data.mean())\n    return data',
    'data-processing',
    '{"remove_duplicates": {"type": "boolean", "default": true}, "fill_missing": {"type": "string", "default": "mean", "options": ["mean", "median", "mode"]}}',
    TRUE,
    4.5
),
(
    1,
    'WebScraper',
    'Herramienta para extraer contenido de páginas web',
    'def scrape_webpage(url, selector="body"):\n    response = requests.get(url)\n    soup = BeautifulSoup(response.content, "html.parser")\n    return soup.select(selector)',
    'web-scraping',
    '{"url": {"type": "string", "required": true}, "selector": {"type": "string", "default": "body"}}',
    TRUE,
    4.2
),
(
    1,
    'TextAnalyzer',
    'Herramienta para análisis de texto y NLP',
    'def analyze_text(text, language="en"):\n    words = text.split()\n    return {\n        "word_count": len(words),\n        "char_count": len(text),\n        "language": language\n    }',
    'nlp',
    '{"text": {"type": "string", "required": true}, "language": {"type": "string", "default": "en"}}',
    TRUE,
    4.0
);

-- Insertar configuraciones de agentes de ejemplo
INSERT INTO ntb_agent_configs (session_id, config_name, description, model, temperature, max_tokens, system_prompt, tools) VALUES
(
    1,
    'DataAnalysisConfig',
    'Configuración para agente de análisis de datos',
    'gpt-4',
    0.3,
    4000,
    'Eres un agente especializado en análisis de datos y machine learning. Tu objetivo es ayudar a los usuarios a analizar, limpiar y procesar datos de manera eficiente.',
    '["DataCleaner", "FeatureEngineer", "ModelTrainer"]'
),
(
    2,
    'WebScrapingConfig',
    'Configuración para agente de web scraping',
    'gpt-4',
    0.2,
    3000,
    'Eres un agente especializado en web scraping y extracción de datos web. Tu objetivo es extraer información relevante de páginas web de manera ética y eficiente.',
    '["WebScraper", "URLProcessor", "ContentParser"]'
);

-- Insertar agentes reutilizables de ejemplo
INSERT INTO ntb_reusable_agents (user_id, agent_name, description, version, agent_config, tools_config, system_prompt, category, tags, is_public, rating, rating_count) VALUES
(
    1,
    'DataAnalysisAgent',
    'Agente especializado en análisis de datos, limpieza y preprocesamiento para machine learning',
    '1.0.0',
    '{"model": "gpt-4", "temperature": 0.3, "max_tokens": 4000, "memory": "conversation_buffer"}',
    '{"tools": ["DataCleaner", "FeatureEngineer", "ModelTrainer"], "tool_configs": {"DataCleaner": {"remove_duplicates": true, "fill_missing": "mean"}}}',
    'Eres un agente especializado en análisis de datos y machine learning. Tu objetivo es ayudar a los usuarios a analizar, limpiar y procesar datos de manera eficiente.',
    'data-analysis',
    '["data-analysis", "machine-learning", "pandas", "numpy", "sklearn"]',
    TRUE,
    4.8,
    15
),
(
    1,
    'WebScrapingAgent',
    'Agente especializado en web scraping y extracción de datos web de manera ética',
    '1.0.0',
    '{"model": "gpt-4", "temperature": 0.2, "max_tokens": 3000, "memory": "conversation_buffer"}',
    '{"tools": ["WebScraper", "URLProcessor", "ContentParser"], "tool_configs": {"WebScraper": {"timeout": 30, "respect_robots": true}}}',
    'Eres un agente especializado en web scraping y extracción de datos web. Tu objetivo es extraer información relevante de páginas web de manera ética y eficiente.',
    'web-scraping',
    '["web-scraping", "data-extraction", "automation", "requests", "beautifulsoup4"]',
    TRUE,
    4.6,
    12
),
(
    1,
    'CustomerSupportAgent',
    'Agente especializado en soporte al cliente con capacidades de NLP y resolución de problemas',
    '1.0.0',
    '{"model": "gpt-4", "temperature": 0.7, "max_tokens": 2000, "memory": "conversation_buffer"}',
    '{"tools": ["TextAnalyzer", "IssueClassifier", "SolutionFinder"], "tool_configs": {"TextAnalyzer": {"language": "en", "sentiment_analysis": true}}}',
    'Eres un agente especializado en soporte al cliente. Tu objetivo es ayudar a los usuarios a resolver problemas de manera amigable y eficiente.',
    'customer-support',
    '["customer-support", "nlp", "chatbot", "automation", "problem-solving"]',
    TRUE,
    4.4,
    8
);

-- Insertar versiones de agentes de ejemplo
INSERT INTO ntb_agent_versions (agent_id, version, changelog, agent_config, tools_config) VALUES
(
    1,
    '1.0.0',
    'Versión inicial del agente de análisis de datos',
    '{"model": "gpt-4", "temperature": 0.3, "max_tokens": 4000, "memory": "conversation_buffer"}',
    '{"tools": ["DataCleaner", "FeatureEngineer", "ModelTrainer"], "tool_configs": {"DataCleaner": {"remove_duplicates": true, "fill_missing": "mean"}}}'
),
(
    1,
    '1.1.0',
    'Mejoras en el manejo de datos faltantes y nuevas herramientas de visualización',
    '{"model": "gpt-4", "temperature": 0.3, "max_tokens": 4000, "memory": "conversation_buffer", "new_features": ["visualization_tools", "advanced_cleaning"]}',
    '{"tools": ["DataCleaner", "FeatureEngineer", "ModelTrainer", "DataVisualizer"], "tool_configs": {"DataCleaner": {"remove_duplicates": true, "fill_missing": "mean", "outlier_detection": true}}}'
),
(
    2,
    '1.0.0',
    'Versión inicial del agente de web scraping',
    '{"model": "gpt-4", "temperature": 0.2, "max_tokens": 3000, "memory": "conversation_buffer"}',
    '{"tools": ["WebScraper", "URLProcessor", "ContentParser"], "tool_configs": {"WebScraper": {"timeout": 30, "respect_robots": true}}}'
);

-- Insertar dependencias entre agentes
INSERT INTO ntb_agent_dependencies (agent_id, dependency_id, dependency_type, version_constraint) VALUES
(
    1, -- DataAnalysisAgent
    2, -- WebScrapingAgent
    'optional',
    '>=1.0.0'
),
(
    3, -- CustomerSupportAgent
    1, -- DataAnalysisAgent
    'optional',
    '>=1.1.0'
);

-- Insertar logs de ejecución de ejemplo
INSERT INTO ntb_execution_logs (cell_id, user_id, execution_start, execution_end, status, output, execution_time, memory_usage, cpu_usage) VALUES
(
    2, -- Celda de importación de librerías
    1,
    CURRENT_TIMESTAMP - INTERVAL '1 hour',
    CURRENT_TIMESTAMP - INTERVAL '1 hour' + INTERVAL '2 seconds',
    'COMPLETED',
    'Data Analysis libraries imported successfully!',
    2.1,
    52428800, -- 50MB
    15.5
),
(
    4, -- Celda de creación del agente
    1,
    CURRENT_TIMESTAMP - INTERVAL '30 minutes',
    CURRENT_TIMESTAMP - INTERVAL '30 minutes' + INTERVAL '1 second',
    'COMPLETED',
    'Agent DataAnalysisAgent created successfully!',
    1.2,
    26214400, -- 25MB
    8.2
),
(
    6, -- Celda de importación de librerías de web scraping
    1,
    CURRENT_TIMESTAMP - INTERVAL '15 minutes',
    CURRENT_TIMESTAMP - INTERVAL '15 minutes' + INTERVAL '1 second',
    'COMPLETED',
    'Web scraping libraries imported successfully!',
    1.1,
    20971520, -- 20MB
    6.8
);
```

## Servicios del Sistema

### **NotebookService**

```java
@Service
public class NotebookService {

    @Autowired
    private NotebookSessionRepository sessionRepository;

    @Autowired
    private NotebookCellRepository cellRepository;

    @Autowired
    private JupyterKernelService kernelService;

    public NotebookSession createSession(CreateSessionRequest request) {
        NotebookSession session = new NotebookSession();
        session.setUserId(getCurrentUserId());
        session.setName(request.getName());
        session.setDescription(request.getDescription());
        session.setSessionType(request.getSessionType());
        session.setTags(JsonUtils.toJson(request.getTags()));
        session.setStatus(SessionStatus.DRAFT);
        session.setCreatedAt(LocalDateTime.now());

        return sessionRepository.save(session);
    }

    public NotebookCell addCell(Long sessionId, CreateCellRequest request) {
        NotebookCell cell = new NotebookCell();
        cell.setSessionId(sessionId);
        cell.setCellType(request.getCellType());
        cell.setContent(request.getContent());
        cell.setLanguage(request.getLanguage());
        cell.setOrderIndex(request.getOrderIndex());
        cell.setMetadata(JsonUtils.toJson(request.getMetadata()));
        cell.setCreatedAt(LocalDateTime.now());

        return cellRepository.save(cell);
    }

    public ExecutionResult executeCell(Long cellId) {
        NotebookCell cell = cellRepository.findById(cellId)
            .orElseThrow(() -> new CellNotFoundException(cellId));

        // Cambiar estado a ejecutando
        cell.setStatus(CellStatus.RUNNING);
        cellRepository.save(cell);

        try {
            // Ejecutar código usando Jupyter Kernel
            ExecutionResult result = kernelService.executeCode(cell.getContent(), cell.getLanguage());

            // Actualizar celda con resultado
            cell.setOutput(result.getOutput());
            cell.setExecutionTime(result.getExecutionTime());
            cell.setStatus(CellStatus.SUCCESS);
            cellRepository.save(cell);

            return result;
        } catch (Exception e) {
            cell.setStatus(CellStatus.ERROR);
            cell.setOutput("Error: " + e.getMessage());
            cellRepository.save(cell);
            throw e;
        }
    }
}
```

### **AgentService**

```java
@Service
public class AgentService {

    @Autowired
    private ReusableAgentRepository agentRepository;

    @Autowired
    private AgentVersionRepository versionRepository;

    @Autowired
    private AgentRunnerService runnerService;

    public ReusableAgent createAgent(CreateAgentRequest request) {
        ReusableAgent agent = new ReusableAgent();
        agent.setUserId(getCurrentUserId());
        agent.setAgentName(request.getAgentName());
        agent.setDescription(request.getDescription());
        agent.setAgentConfig(JsonUtils.toJson(request.getAgentConfig()));
        agent.setToolsConfig(JsonUtils.toJson(request.getToolsConfig()));
        agent.setSystemPrompt(request.getSystemPrompt());
        agent.setCategory(request.getCategory());
        agent.setTags(JsonUtils.toJson(request.getTags()));
        agent.setCreatedAt(LocalDateTime.now());

        agent = agentRepository.save(agent);

        // Crear versión inicial
        createAgentVersion(agent.getId(), "1.0.0", "Initial version", request.getAgentConfig(), request.getToolsConfig());

        return agent;
    }

    public AgentVersion createAgentVersion(Long agentId, String version, String changelog,
                                         Object agentConfig, Object toolsConfig) {
        AgentVersion agentVersion = new AgentVersion();
        agentVersion.setAgentId(agentId);
        agentVersion.setVersion(version);
        agentVersion.setChangelog(changelog);
        agentVersion.setAgentConfig(JsonUtils.toJson(agentConfig));
        agentVersion.setToolsConfig(JsonUtils.toJson(toolsConfig));
        agentVersion.setCreatedAt(LocalDateTime.now());

        return versionRepository.save(agentVersion);
    }

    public AgentExecutionResult executeAgent(Long agentId, AgentExecutionRequest request) {
        ReusableAgent agent = agentRepository.findById(agentId)
            .orElseThrow(() -> new AgentNotFoundException(agentId));

        // Ejecutar agente usando el servicio de ejecución
        return runnerService.executeAgent(agent, request);
    }

    public List<ReusableAgent> searchAgents(AgentSearchRequest request) {
        // Búsqueda de agentes por categoría, tags, rating, etc.
        return agentRepository.searchAgents(
            request.getCategory(),
            request.getTags(),
            request.getMinRating(),
            request.getIsPublic()
        );
    }
}
```

### **JupyterKernelService**

```java
@Service
public class JupyterKernelService {

    @Autowired
    private KernelManager kernelManager;

    @Autowired
    private ExecutionMetricsService metricsService;

    public ExecutionResult executeCode(String code, String language) {
        if (!"python".equalsIgnoreCase(language)) {
            throw new UnsupportedLanguageException(language);
        }

        Kernel kernel = kernelManager.getOrCreateKernel(getCurrentUserId());

        try {
            Timer.Sample timer = metricsService.startExecutionTimer();

            // Ejecutar código en el kernel
            ExecutionResult result = kernel.executeCode(code);

            // Registrar métricas
            metricsService.recordExecutionSuccess(timer.stop());
            metricsService.recordExecutionTime(result.getExecutionTime());

            return result;
        } catch (Exception e) {
            metricsService.recordExecutionError();
            throw e;
        }
    }

    public void stopExecution(Long cellId) {
        Kernel kernel = kernelManager.getKernelForCell(cellId);
        if (kernel != null) {
            kernel.interrupt();
        }
    }
}
```

## API Endpoints

### **Gestión de Notebooks**

```java
@RestController
@RequestMapping("/api/v1/notebooks")
public class NotebookController {

    @Autowired
    private NotebookService notebookService;

    @PostMapping
    public ResponseEntity<NotebookSession> createSession(@RequestBody CreateSessionRequest request) {
        NotebookSession session = notebookService.createSession(request);
        return ResponseEntity.ok(session);
    }

    @GetMapping
    public ResponseEntity<List<NotebookSession>> getUserSessions() {
        List<NotebookSession> sessions = notebookService.getUserSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<NotebookSession> getSession(@PathVariable Long sessionId) {
        NotebookSession session = notebookService.getSession(sessionId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{sessionId}/cells")
    public ResponseEntity<NotebookCell> addCell(@PathVariable Long sessionId,
                                               @RequestBody CreateCellRequest request) {
        NotebookCell cell = notebookService.addCell(sessionId, request);
        return ResponseEntity.ok(cell);
    }

    @PostMapping("/{sessionId}/cells/{cellId}/execute")
    public ResponseEntity<ExecutionResult> executeCell(@PathVariable Long sessionId,
                                                      @PathVariable Long cellId) {
        ExecutionResult result = notebookService.executeCell(cellId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{sessionId}/cells/{cellId}/stop")
    public ResponseEntity<Void> stopCellExecution(@PathVariable Long sessionId,
                                                 @PathVariable Long cellId) {
        notebookService.stopCellExecution(cellId);
        return ResponseEntity.ok().build();
    }
}
```

### **Gestión de Agentes**

```java
@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping
    public ResponseEntity<ReusableAgent> createAgent(@RequestBody CreateAgentRequest request) {
        ReusableAgent agent = agentService.createAgent(request);
        return ResponseEntity.ok(agent);
    }

    @GetMapping
    public ResponseEntity<List<ReusableAgent>> searchAgents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Boolean isPublic) {

        AgentSearchRequest searchRequest = AgentSearchRequest.builder()
            .category(category)
            .tags(tags)
            .minRating(minRating)
            .isPublic(isPublic)
            .build();

        List<ReusableAgent> agents = agentService.searchAgents(searchRequest);
        return ResponseEntity.ok(agents);
    }

    @PostMapping("/{agentId}/execute")
    public ResponseEntity<AgentExecutionResult> executeAgent(@PathVariable Long agentId,
                                                            @RequestBody AgentExecutionRequest request) {
        AgentExecutionResult result = agentService.executeAgent(agentId, request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{agentId}/versions")
    public ResponseEntity<AgentVersion> createVersion(@PathVariable Long agentId,
                                                     @RequestBody CreateVersionRequest request) {
        AgentVersion version = agentService.createAgentVersion(
            agentId,
            request.getVersion(),
            request.getChangelog(),
            request.getAgentConfig(),
            request.getToolsConfig()
        );
        return ResponseEntity.ok(version);
    }
}
```

### **Gestión de Herramientas**

```java
@RestController
@RequestMapping("/api/v1/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @PostMapping
    public ResponseEntity<AgentTool> createTool(@RequestBody CreateToolRequest request) {
        AgentTool tool = toolService.createTool(request);
        return ResponseEntity.ok(tool);
    }

    @GetMapping
    public ResponseEntity<List<AgentTool>> getTools(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isPublic) {

        List<AgentTool> tools = toolService.getTools(category, isPublic);
        return ResponseEntity.ok(tools);
    }

    @PostMapping("/{toolId}/test")
    public ResponseEntity<ToolTestResult> testTool(@PathVariable Long toolId,
                                                  @RequestBody ToolTestRequest request) {
        ToolTestResult result = toolService.testTool(toolId, request);
        return ResponseEntity.ok(result);
    }
}
```

## WebSocket para Tiempo Real

### **WebSocket Handler**

```java
@Component
public class NotebookWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketSessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String notebookId = extractNotebookId(session);
        sessionManager.addSession(notebookId, session);

        // Enviar mensaje de conexión establecida
        sendMessage(session, "connection", "Connected to notebook: " + notebookId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Procesar mensajes del cliente
        String notebookId = extractNotebookId(session);
        JsonNode jsonMessage = objectMapper.readTree(message.getPayload());

        String eventType = jsonMessage.get("type").asText();

        switch (eventType) {
            case "cell:execute":
                handleCellExecution(notebookId, jsonMessage);
                break;
            case "cell:stop":
                handleCellStop(notebookId, jsonMessage);
                break;
            case "agent:execute":
                handleAgentExecution(notebookId, jsonMessage);
                break;
        }
    }

    private void handleCellExecution(String notebookId, JsonNode message) {
        String cellId = message.get("cellId").asText();

        // Notificar inicio de ejecución
        broadcastToNotebook(notebookId, "cell:start", Map.of(
            "cellId", cellId,
            "timestamp", LocalDateTime.now().toString()
        ));

        // Ejecutar celda en background
        CompletableFuture.runAsync(() -> {
            try {
                ExecutionResult result = notebookService.executeCell(Long.parseLong(cellId));

                // Notificar completado
                broadcastToNotebook(notebookId, "cell:complete", Map.of(
                    "cellId", cellId,
                    "status", "success",
                    "executionTime", result.getExecutionTime(),
                    "output", result.getOutput()
                ));
            } catch (Exception e) {
                // Notificar error
                broadcastToNotebook(notebookId, "cell:error", Map.of(
                    "cellId", cellId,
                    "error", e.getMessage(),
                    "traceback", getStackTrace(e)
                ));
            }
        });
    }
}
```

## Configuración de Aplicación

### **application-notebook.yml**

```yaml
notebook-system:
  # Configuración de kernels Jupyter
  jupyter:
    enabled: true
    max-kernels-per-user: 3
    kernel-timeout: 300s
    memory-limit: "2GB"
    cpu-limit: 2.0

  # Configuración de agentes
  agents:
    max-agents-per-user: 50
    max-tools-per-agent: 20
    agent-execution-timeout: 600s
    memory-limit: "4GB"

  # Configuración de herramientas
  tools:
    max-tools-per-user: 100
    tool-execution-timeout: 120s
    sandbox-enabled: true
    allowed-libraries: ["pandas", "numpy", "requests", "beautifulsoup4"]

  # Configuración de marketplace
  marketplace:
    enabled: true
    moderation-enabled: true
    auto-verification: false
    rating-threshold: 4.0

  # Configuración de WebSocket
  websocket:
    enabled: true
    max-connections-per-notebook: 10
    heartbeat-interval: 30s
    connection-timeout: 300s

  # Configuración de métricas
  metrics:
    execution-tracking: true
    performance-monitoring: true
    usage-analytics: true
    cost-tracking: true
```

## Métricas Prometheus

```java
@Component
public class NotebookMetrics {

    private final Counter sessionsCreatedCounter;
    private final Counter cellsExecutedCounter;
    private final Counter agentsCreatedCounter;
    private final Counter toolsCreatedCounter;
    private final Timer cellExecutionTimer;
    private final Timer agentExecutionTimer;
    private final Gauge activeKernelsGauge;
    private final Gauge activeSessionsGauge;

    public NotebookMetrics(MeterRegistry meterRegistry) {
        this.sessionsCreatedCounter = Counter.builder("notebook.sessions.created")
            .description("Total notebook sessions created")
            .register(meterRegistry);

        this.cellsExecutedCounter = Counter.builder("notebook.cells.executed")
            .description("Total cells executed")
            .register(meterRegistry);

        this.agentsCreatedCounter = Counter.builder("notebook.agents.created")
            .description("Total agents created")
            .register(meterRegistry);

        this.toolsCreatedCounter = Counter.builder("notebook.tools.created")
            .description("Total tools created")
            .register(meterRegistry);

        this.cellExecutionTimer = Timer.builder("notebook.cell.execution.time")
            .description("Time to execute notebook cell")
            .register(meterRegistry);

        this.agentExecutionTimer = Timer.builder("notebook.agent.execution.time")
            .description("Time to execute agent")
            .register(meterRegistry);

        this.activeKernelsGauge = Gauge.builder("notebook.kernels.active")
            .description("Number of active Jupyter kernels")
            .register(meterRegistry);

        this.activeSessionsGauge = Gauge.builder("notebook.sessions.active")
            .description("Number of active notebook sessions")
            .register(meterRegistry);
    }

    public void incrementSessionsCreated() {
        sessionsCreatedCounter.increment();
    }

    public void incrementCellsExecuted() {
        cellsExecutedCounter.increment();
    }

    public void incrementAgentsCreated() {
        agentsCreatedCounter.increment();
    }

    public void incrementToolsCreated() {
        toolsCreatedCounter.increment();
    }

    public Timer.Sample startCellExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startAgentExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void setActiveKernels(int count) {
        activeKernelsGauge.set(count);
    }

    public void setActiveSessions(int count) {
        activeSessionsGauge.set(count);
    }
}
```

## Integración con Frontend

### **Eventos WebSocket**

```typescript
// Tipos de eventos del frontend
interface WebSocketEvents {
  // Celdas
  "cell:start": { cellId: string; timestamp: string };
  "cell:output": { cellId: string; output: string; type: "stdout" | "stderr" };
  "cell:complete": {
    cellId: string;
    status: "success" | "error";
    executionTime: number;
  };
  "cell:error": { cellId: string; error: string; traceback: string };

  // Agentes
  "agent:start": { agentId: string; timestamp: string };
  "agent:thinking": { agentId: string; thought: string };
  "agent:tool_use": { agentId: string; tool: string; input: any };
  "agent:complete": { agentId: string; result: any; executionTime: number };

  // Sistema
  connection: { message: string };
  error: { message: string; code: string };
}
```

### **Ejemplo de Uso del Frontend**

```typescript
// Conexión WebSocket
const ws = new WebSocket(`ws://localhost:8080/ws/notebooks/${notebookId}`);

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);

  switch (data.type) {
    case "cell:start":
      updateCellStatus(data.cellId, "running");
      break;

    case "cell:output":
      appendCellOutput(data.cellId, data.output, data.type);
      break;

    case "cell:complete":
      updateCellStatus(data.cellId, data.status);
      updateCellExecutionTime(data.cellId, data.executionTime);
      break;

    case "cell:error":
      updateCellStatus(data.cellId, "error");
      showCellError(data.cellId, data.error);
      break;
  }
};

// Ejecutar celda
const executeCell = (cellId: string) => {
  ws.send(
    JSON.stringify({
      type: "cell:execute",
      cellId: cellId,
    })
  );
};
```

## Beneficios del Sistema

- **Agentes Reutilizables**: Crear y compartir agentes especializados
- **Desarrollo Interactivo**: Notebooks con ejecución en tiempo real
- **Herramientas Personalizables**: Sistema extensible de herramientas
- **Versionado**: Control completo de versiones de agentes
- **Marketplace**: Compartir y descubrir agentes útiles
- **Colaboración**: Trabajo en equipo en notebooks
- **Escalabilidad**: Sistema distribuido y escalable
- **Integración**: Conexión con sistemas externos de IA
- **Monitoreo**: Métricas y logs completos
- **Seguridad**: Sandboxing y control de acceso

## Conclusión

El módulo de Notebook y Agentes de IA proporciona un sistema completo para la creación, desarrollo y gestión de agentes de inteligencia artificial reutilizables. Al integrar notebooks interactivos, herramientas personalizables y un sistema de versionado, permite a los usuarios desarrollar agentes especializados de manera eficiente y colaborativa. El sistema es escalable, seguro y mantiene la flexibilidad para diferentes casos de uso y tecnologías.
