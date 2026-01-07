# Módulo de Diseño y Modelado - Portal Backend

## Descripción General

El módulo de **Diseño y Modelado** proporciona un sistema completo y agnóstico a la tecnología para el diseño de aplicaciones. Genera estructuras JSON internas que luego son procesadas por agentes especializados para generar código en múltiples tecnologías. El sistema incluye diseñadores de pantallas, formularios, modelado de datos y APIs, todos trabajando con una estructura JSON unificada.

## Arquitectura del Sistema

### **Principios de Diseño**

- **Agnóstico a la Tecnología**: Estructura JSON interna independiente de frameworks
- **Generación por Agentes**: Código generado por múltiples agentes especializados
- **Estructura Unificada**: Formato JSON consistente para todos los diseñadores
- **Versionado**: Control de versiones para todos los diseños
- **Colaboración**: Múltiples usuarios pueden trabajar en el mismo diseño
- **Templates**: Plantillas predefinidas para diferentes tipos de aplicaciones

### **Flujo de Trabajo**

1. **Diseño**: Usuario crea diseño usando diseñadores visuales
2. **JSON**: Sistema genera estructura JSON interna
3. **Validación**: Validación de consistencia y reglas de negocio
4. **Almacenamiento**: Persistencia en base de datos
5. **Generación**: Agentes especializados procesan JSON
6. **Código**: Generación de código en tecnologías específicas
7. **Repositorio**: Publicación en repositorios Git

## Entidades del Sistema

### 1. **DesignProject**

```java
@Entity
@Table(name = "dsg_design_projects")
public class DesignProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "description")
    private String description;

    @Column(name = "project_type")
    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    @Column(name = "technology_stack")
    private String technologyStack; // JSON array de tecnologías objetivo

    @Column(name = "design_data")
    private String designData; // JSON completo del proyecto

    @Column(name = "version")
    private String version;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DesignStatus status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ProjectType {
    WEB_APPLICATION, MOBILE_APP, DESKTOP_APP, API_SERVICE, MICROSERVICE,
    DASHBOARD, ECOMMERCE, CRM, ERP, CUSTOM
}

public enum DesignStatus {
    DRAFT, IN_PROGRESS, REVIEW, APPROVED, GENERATED, DEPLOYED
}
```

### 2. **ScreenDesign**

```java
@Entity
@Table(name = "dsg_screen_designs")
public class ScreenDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "screen_type")
    @Enumerated(EnumType.STRING)
    private ScreenType screenType;

    @Column(name = "layout_data")
    private String layoutData; // JSON con estructura de layout

    @Column(name = "components_data")
    private String componentsData; // JSON con componentes y propiedades

    @Column(name = "responsive_config")
    private String responsiveConfig; // JSON con configuración responsive

    @Column(name = "theme_config")
    private String themeConfig; // JSON con configuración de tema

    @Column(name = "navigation_config")
    private String navigationConfig; // JSON con configuración de navegación

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ScreenType {
    LANDING, DASHBOARD, FORM, LIST, DETAIL, LOGIN, REGISTER, PROFILE,
    SETTINGS, ADMIN, CUSTOM
}
```

### 3. **FormDesign**

```java
@Entity
@Table(name = "dsg_form_designs")
public class FormDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "form_type")
    @Enumerated(EnumType.STRING)
    private FormType formType;

    @Column(name = "fields_data")
    private String fieldsData; // JSON con campos y validaciones

    @Column(name = "validation_rules")
    private String validationRules; // JSON con reglas de validación

    @Column(name = "layout_config")
    private String layoutConfig; // JSON con configuración de layout

    @Column(name = "submission_config")
    private String submissionConfig; // JSON con configuración de envío

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum FormType {
    USER_REGISTRATION, LOGIN, DATA_ENTRY, SEARCH, FILTER,
    FEEDBACK, CONTACT, SUBSCRIPTION, CUSTOM
}
```

### 4. **DataModel**

```java
@Entity
@Table(name = "dsg_data_models")
public class DataModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_type")
    @Enumerated(EnumType.STRING)
    private DataModelType modelType;

    @Column(name = "entities_data")
    private String entitiesData; // JSON con entidades y relaciones

    @Column(name = "database_schema")
    private String databaseSchema; // JSON con esquema de base de datos

    @Column(name = "api_schema")
    private String apiSchema; // JSON con esquema de API

    @Column(name = "validation_rules")
    private String validationRules; // JSON con reglas de validación

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum DataModelType {
    RELATIONAL, DOCUMENT, GRAPH, KEY_VALUE, TIME_SERIES, HYBRID
}
```

### 5. **APIDesign**

```java
@Entity
@Table(name = "dsg_api_designs")
public class APIDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "api_name")
    private String apiName;

    @Column(name = "api_type")
    @Enumerated(EnumType.STRING)
    private APIType apiType;

    @Column(name = "endpoints_data")
    private String endpointsData; // JSON con endpoints y métodos

    @Column(name = "request_schemas")
    private String requestSchemas; // JSON con esquemas de request

    @Column(name = "response_schemas")
    private String responseSchemas; // JSON con esquemas de response

    @Column(name = "authentication_config")
    private String authenticationConfig; // JSON con configuración de auth

    @Column(name = "rate_limiting_config")
    private String rateLimitingConfig; // JSON con configuración de rate limiting

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum APIType {
    REST, GRAPHQL, GRPC, SOAP, EVENT_DRIVEN, HYBRID
}
```

### 6. **DesignTemplate**

```java
@Entity
@Table(name = "dsg_design_templates")
public class DesignTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_type")
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "template_data")
    private String templateData; // JSON completo del template

    @Column(name = "technology_targets")
    private String technologyTargets; // JSON array de tecnologías soportadas

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum TemplateType {
    ECOMMERCE, CRM, DASHBOARD, BLOG, PORTFOLIO, LANDING_PAGE,
    ADMIN_PANEL, MOBILE_APP, API_SERVICE, CUSTOM
}
```

### 7. **CodeGenerationJob**

```java
@Entity
@Table(name = "dsg_code_generation_jobs")
public class CodeGenerationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private GenerationJobType jobType;

    @Column(name = "target_technology")
    private String targetTechnology;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "agent_id")
    private Long agentId; // ID del agente que procesa el trabajo

    @Column(name = "input_data")
    private String inputData; // JSON con datos de entrada

    @Column(name = "output_data")
    private String outputData; // JSON con código generado

    @Column(name = "error_log")
    private String errorLog;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum GenerationJobType {
    FRONTEND, BACKEND, DATABASE, API, MOBILE, DESKTOP, DOCUMENTATION
}

public enum JobStatus {
    PENDING, IN_PROGRESS, COMPLETED, FAILED, CANCELLED
}
```

## Estructura JSON Interna

### **Estructura de Proyecto**

```json
{
  "project": {
    "id": "proj_001",
    "name": "E-commerce Platform",
    "type": "WEB_APPLICATION",
    "version": "1.0.0",
    "technologyStack": ["React", "Node.js", "PostgreSQL"],
    "screens": [...],
    "forms": [...],
    "dataModels": [...],
    "apis": [...],
    "theme": {...},
    "navigation": {...}
  }
}
```

### **Estructura de Pantalla**

```json
{
  "screen": {
    "id": "screen_001",
    "name": "Product List",
    "type": "LIST",
    "layout": {
      "type": "grid",
      "columns": 12,
      "rows": "auto",
      "areas": [...]
    },
    "components": [
      {
        "id": "comp_001",
        "type": "BootstrapContainer",
        "props": {
          "className": "container-fluid",
          "children": [...]
        }
      }
    ],
    "responsive": {
      "breakpoints": {
        "xs": {...},
        "sm": {...},
        "md": {...},
        "lg": {...},
        "xl": {...}
      }
    }
  }
}
```

### **Estructura de Formulario**

```json
{
  "form": {
    "id": "form_001",
    "name": "User Registration",
    "type": "USER_REGISTRATION",
    "fields": [
      {
        "id": "field_001",
        "name": "email",
        "type": "email",
        "label": "Email Address",
        "required": true,
        "validation": {
          "pattern": "^[^@]+@[^@]+\\.[^@]+$",
          "message": "Please enter a valid email address"
        }
      }
    ],
    "layout": {
      "type": "vertical",
      "spacing": "md"
    },
    "submission": {
      "method": "POST",
      "endpoint": "/api/users/register",
      "successRedirect": "/login"
    }
  }
}
```

## Scripts SQL

### **DROP TABLES**

```sql
-- Eliminar tablas en orden inverso (por dependencias)
DROP TABLE IF EXISTS dsg_code_generation_jobs CASCADE;
DROP TABLE IF EXISTS dsg_design_templates CASCADE;
DROP TABLE IF EXISTS dsg_api_designs CASCADE;
DROP TABLE IF EXISTS dsg_data_models CASCADE;
DROP TABLE IF EXISTS dsg_form_designs CASCADE;
DROP TABLE IF EXISTS dsg_screen_designs CASCADE;
DROP TABLE IF EXISTS dsg_design_projects CASCADE;
```

### **CREATE TABLES**

```sql
-- Tabla principal de proyectos de diseño
CREATE TABLE dsg_design_projects (
    id BIGSERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    description TEXT,
    project_type VARCHAR(50) NOT NULL,
    technology_stack TEXT, -- JSON array de tecnologías objetivo
    design_data TEXT NOT NULL, -- JSON completo del proyecto
    version VARCHAR(50) DEFAULT '1.0.0',
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de diseños de pantallas
CREATE TABLE dsg_screen_designs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    screen_name VARCHAR(255) NOT NULL,
    screen_type VARCHAR(50) NOT NULL,
    layout_data TEXT, -- JSON con estructura de layout
    components_data TEXT, -- JSON con componentes y propiedades
    responsive_config TEXT, -- JSON con configuración responsive
    theme_config TEXT, -- JSON con configuración de tema
    navigation_config TEXT, -- JSON con configuración de navegación
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES dsg_design_projects(id) ON DELETE CASCADE
);

-- Tabla de diseños de formularios
CREATE TABLE dsg_form_designs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    form_name VARCHAR(255) NOT NULL,
    form_type VARCHAR(50) NOT NULL,
    fields_data TEXT, -- JSON con campos y validaciones
    validation_rules TEXT, -- JSON con reglas de validación
    layout_config TEXT, -- JSON con configuración de layout
    submission_config TEXT, -- JSON con configuración de envío
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES dsg_design_projects(id) ON DELETE CASCADE
);

-- Tabla de modelos de datos
CREATE TABLE dsg_data_models (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    model_name VARCHAR(255) NOT NULL,
    model_type VARCHAR(50) NOT NULL,
    entities_data TEXT, -- JSON con entidades y relaciones
    database_schema TEXT, -- JSON con esquema de base de datos
    api_schema TEXT, -- JSON con esquema de API
    validation_rules TEXT, -- JSON con reglas de validación
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES dsg_design_projects(id) ON DELETE CASCADE
);

-- Tabla de diseños de APIs
CREATE TABLE dsg_api_designs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    api_name VARCHAR(255) NOT NULL,
    api_type VARCHAR(50) NOT NULL,
    endpoints_data TEXT, -- JSON con endpoints y métodos
    request_schemas TEXT, -- JSON con esquemas de request
    response_schemas TEXT, -- JSON con esquemas de response
    authentication_config TEXT, -- JSON con configuración de auth
    rate_limiting_config TEXT, -- JSON con configuración de rate limiting
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES dsg_design_projects(id) ON DELETE CASCADE
);

-- Tabla de templates de diseño
CREATE TABLE dsg_design_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(255) NOT NULL,
    template_type VARCHAR(50) NOT NULL,
    category VARCHAR(100),
    description TEXT,
    template_data TEXT NOT NULL, -- JSON completo del template
    technology_targets TEXT, -- JSON array de tecnologías soportadas
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de trabajos de generación de código
CREATE TABLE dsg_code_generation_jobs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    target_technology VARCHAR(100) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    agent_id BIGINT,
    input_data TEXT NOT NULL, -- JSON con datos de entrada
    output_data TEXT, -- JSON con código generado
    error_log TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES dsg_design_projects(id) ON DELETE CASCADE
);
```

### **CREATE INDEXES**

```sql
-- Índices para optimizar consultas por proyecto
CREATE INDEX idx_dsg_screen_designs_project_id ON dsg_screen_designs(project_id);
CREATE INDEX idx_dsg_form_designs_project_id ON dsg_form_designs(project_id);
CREATE INDEX idx_dsg_data_models_project_id ON dsg_data_models(project_id);
CREATE INDEX idx_dsg_api_designs_project_id ON dsg_api_designs(project_id);
CREATE INDEX idx_dsg_code_generation_jobs_project_id ON dsg_code_generation_jobs(project_id);

-- Índices para optimizar consultas por tipo
CREATE INDEX idx_dsg_design_projects_type ON dsg_design_projects(project_type);
CREATE INDEX idx_dsg_design_projects_status ON dsg_design_projects(status);
CREATE INDEX idx_dsg_screen_designs_type ON dsg_screen_designs(screen_type);
CREATE INDEX idx_dsg_form_designs_type ON dsg_form_designs(form_type);
CREATE INDEX idx_dsg_data_models_type ON dsg_data_models(model_type);
CREATE INDEX idx_dsg_api_designs_type ON dsg_api_designs(api_type);
CREATE INDEX idx_dsg_design_templates_type ON dsg_design_templates(template_type);

-- Índices para optimizar consultas por usuario
CREATE INDEX idx_dsg_design_projects_created_by ON dsg_design_projects(created_by);

-- Índices para optimizar consultas por estado de trabajo
CREATE INDEX idx_dsg_code_generation_jobs_status ON dsg_code_generation_jobs(status);
CREATE INDEX idx_dsg_code_generation_jobs_agent_id ON dsg_code_generation_jobs(agent_id);

-- Índices para optimizar consultas por fecha
CREATE INDEX idx_dsg_design_projects_created_at ON dsg_design_projects(created_at);
CREATE INDEX idx_dsg_code_generation_jobs_created_at ON dsg_code_generation_jobs(created_at);

-- Índices para búsqueda de texto
CREATE INDEX idx_dsg_design_projects_name ON dsg_design_projects USING gin(to_tsvector('english', project_name));
CREATE INDEX idx_dsg_screen_designs_name ON dsg_screen_designs USING gin(to_tsvector('english', screen_name));
CREATE INDEX idx_dsg_form_designs_name ON dsg_form_designs USING gin(to_tsvector('english', form_name));
```

### **INSERT DEMO DATA**

```sql
-- Insertar templates predefinidos
INSERT INTO dsg_design_templates (template_name, template_type, category, description, template_data, technology_targets) VALUES
(
    'E-commerce Platform',
    'ECOMMERCE',
    'Business',
    'Template completo para plataforma de comercio electrónico',
    '{"project": {"name": "E-commerce Platform", "type": "WEB_APPLICATION", "screens": [{"name": "Home", "type": "LANDING"}, {"name": "Product List", "type": "LIST"}, {"name": "Product Detail", "type": "DETAIL"}, {"name": "Shopping Cart", "type": "CUSTOM"}, {"name": "Checkout", "type": "FORM"}, {"name": "User Profile", "type": "PROFILE"}], "forms": [{"name": "User Registration", "type": "USER_REGISTRATION"}, {"name": "Login", "type": "LOGIN"}, {"name": "Product Search", "type": "SEARCH"}], "dataModels": [{"name": "User", "type": "RELATIONAL"}, {"name": "Product", "type": "RELATIONAL"}, {"name": "Order", "type": "RELATIONAL"}], "apis": [{"name": "User API", "type": "REST"}, {"name": "Product API", "type": "REST"}, {"name": "Order API", "type": "REST"}]}}',
    '["React", "Node.js", "PostgreSQL", "MongoDB", "Vue", "Angular"]'
),
(
    'Admin Dashboard',
    'DASHBOARD',
    'Business',
    'Template para panel de administración con métricas y gestión',
    '{"project": {"name": "Admin Dashboard", "type": "DASHBOARD", "screens": [{"name": "Dashboard", "type": "DASHBOARD"}, {"name": "User Management", "type": "LIST"}, {"name": "Analytics", "type": "CUSTOM"}, {"name": "Settings", "type": "SETTINGS"}], "forms": [{"name": "User Creation", "type": "DATA_ENTRY"}, {"name": "Configuration", "type": "DATA_ENTRY"}], "dataModels": [{"name": "User", "type": "RELATIONAL"}, {"name": "Metric", "type": "TIME_SERIES"}], "apis": [{"name": "Admin API", "type": "REST"}]}}',
    '["React", "Vue", "Angular", "Node.js", "Python", "Java"]'
),
(
    'Mobile App',
    'MOBILE_APP',
    'Mobile',
    'Template para aplicación móvil con navegación y formularios',
    '{"project": {"name": "Mobile App", "type": "MOBILE_APP", "screens": [{"name": "Home", "type": "LANDING"}, {"name": "Profile", "type": "PROFILE"}, {"name": "Settings", "type": "SETTINGS"}], "forms": [{"name": "User Registration", "type": "USER_REGISTRATION"}, {"name": "Login", "type": "LOGIN"}], "dataModels": [{"name": "User", "type": "DOCUMENT"}], "apis": [{"name": "Mobile API", "type": "REST"}]}}',
    '["React Native", "Flutter", "Ionic", "Xamarin"]'
);

-- Insertar proyecto de ejemplo
INSERT INTO dsg_design_projects (project_name, description, project_type, technology_stack, design_data, status, created_by) VALUES
(
    'Demo E-commerce Platform',
    'Proyecto de demostración para plataforma de comercio electrónico',
    'WEB_APPLICATION',
    '["React", "Node.js", "PostgreSQL"]',
    '{"project": {"name": "Demo E-commerce", "type": "WEB_APPLICATION", "version": "1.0.0", "screens": [{"id": "screen_001", "name": "Home", "type": "LANDING", "layout": {"type": "grid", "columns": 12}, "components": [{"id": "comp_001", "type": "BootstrapContainer", "props": {"className": "container-fluid"}}]}], "forms": [{"id": "form_001", "name": "User Registration", "type": "USER_REGISTRATION", "fields": [{"id": "field_001", "name": "email", "type": "email", "required": true}]}], "dataModels": [{"id": "model_001", "name": "User", "type": "RELATIONAL", "entities": [{"name": "User", "fields": [{"name": "id", "type": "BIGINT", "primary": true}, {"name": "email", "type": "VARCHAR", "length": 255}]}]}], "apis": [{"id": "api_001", "name": "User API", "type": "REST", "endpoints": [{"path": "/api/users", "method": "GET", "description": "Get all users"}]}]}}',
    'DRAFT',
    1
);

-- Insertar pantallas de ejemplo
INSERT INTO dsg_screen_designs (project_id, screen_name, screen_type, layout_data, components_data, responsive_config) VALUES
(
    1,
    'Home Page',
    'LANDING',
    '{"type": "grid", "columns": 12, "rows": "auto", "areas": [{"name": "header", "start": [1, 1], "end": [12, 1]}, {"name": "main", "start": [1, 2], "end": [12, 11]}, {"name": "footer", "start": [1, 12], "end": [12, 12]}]}',
    '[{"id": "comp_001", "type": "BootstrapContainer", "props": {"className": "container-fluid", "children": [{"id": "comp_002", "type": "BootstrapPanel", "props": {"title": "Welcome", "content": "Welcome to our e-commerce platform"}}]}}]',
    '{"breakpoints": {"xs": {"columns": 1}, "sm": {"columns": 2}, "md": {"columns": 3}, "lg": {"columns": 4}, "xl": {"columns": 6}}}'
),
(
    1,
    'Product List',
    'LIST',
    '{"type": "grid", "columns": 12, "rows": "auto", "areas": [{"name": "filters", "start": [1, 1], "end": [3, 1]}, {"name": "products", "start": [4, 1], "end": [12, 10]}, {"name": "pagination", "start": [1, 11], "end": [12, 12]}]}',
    '[{"id": "comp_003", "type": "BootstrapContainer", "props": {"className": "container-fluid", "children": [{"id": "comp_004", "type": "BootstrapPanel", "props": {"title": "Products", "content": "Product list will be displayed here"}}]}}]',
    '{"breakpoints": {"xs": {"columns": 1}, "sm": {"columns": 2}, "md": {"columns": 3}, "lg": {"columns": 4}, "xl": {"columns": 6}}}'
);

-- Insertar formularios de ejemplo
INSERT INTO dsg_form_designs (project_id, form_name, form_type, fields_data, validation_rules, layout_config) VALUES
(
    1,
    'User Registration',
    'USER_REGISTRATION',
    '[{"id": "field_001", "name": "email", "type": "email", "label": "Email Address", "required": true, "placeholder": "Enter your email"}, {"id": "field_002", "name": "password", "type": "password", "label": "Password", "required": true, "placeholder": "Enter your password"}, {"id": "field_003", "name": "confirmPassword", "type": "password", "label": "Confirm Password", "required": true, "placeholder": "Confirm your password"}]',
    '[{"field": "email", "pattern": "^[^@]+@[^@]+\\.[^@]+$", "message": "Please enter a valid email address"}, {"field": "password", "minLength": 8, "message": "Password must be at least 8 characters long"}, {"field": "confirmPassword", "custom": "password === confirmPassword", "message": "Passwords must match"}]',
    '{"type": "vertical", "spacing": "md", "columns": 1}'
),
(
    1,
    'Product Search',
    'SEARCH',
    '[{"id": "field_004", "name": "query", "type": "text", "label": "Search Products", "required": false, "placeholder": "Enter product name or description"}, {"id": "field_005", "name": "category", "type": "select", "label": "Category", "required": false, "options": ["All", "Electronics", "Clothing", "Books"]}]',
    '[{"field": "query", "minLength": 2, "message": "Search query must be at least 2 characters long"}]',
    '{"type": "horizontal", "spacing": "sm", "columns": 2}'
);

-- Insertar modelo de datos de ejemplo
INSERT INTO dsg_data_models (project_id, model_name, model_type, entities_data, database_schema, api_schema) VALUES
(
    1,
    'User Management',
    'RELATIONAL',
    '[{"name": "User", "fields": [{"name": "id", "type": "BIGINT", "primary": true, "auto_increment": true}, {"name": "email", "type": "VARCHAR", "length": 255, "unique": true, "nullable": false}, {"name": "password_hash", "type": "VARCHAR", "length": 255, "nullable": false}, {"name": "first_name", "type": "VARCHAR", "length": 100, "nullable": true}, {"name": "last_name", "type": "VARCHAR", "length": 100, "nullable": true}, {"name": "created_at", "type": "TIMESTAMP", "default": "CURRENT_TIMESTAMP"}, {"name": "updated_at", "type": "TIMESTAMP", "default": "CURRENT_TIMESTAMP"}], "relationships": []}, {"name": "Product", "fields": [{"name": "id", "type": "BIGINT", "primary": true, "auto_increment": true}, {"name": "name", "type": "VARCHAR", "length": 255, "nullable": false}, {"name": "description", "type": "TEXT", "nullable": true}, {"name": "price", "type": "DECIMAL", "precision": 10, "scale": 2, "nullable": false}, {"name": "category_id", "type": "BIGINT", "nullable": true}, {"name": "created_at", "type": "TIMESTAMP", "default": "CURRENT_TIMESTAMP"}]}, {"name": "Category", "fields": [{"name": "id", "type": "BIGINT", "primary": true, "auto_increment": true}, {"name": "name", "type": "VARCHAR", "length": 100, "nullable": false}], "relationships": []}]',
    '{"tables": [{"name": "users", "columns": [{"name": "id", "type": "BIGSERIAL PRIMARY KEY"}, {"name": "email", "type": "VARCHAR(255) UNIQUE NOT NULL"}, {"name": "password_hash", "type": "VARCHAR(255) NOT NULL"}, {"name": "first_name", "type": "VARCHAR(100)"}, {"name": "last_name", "type": "VARCHAR(100)"}, {"name": "created_at", "type": "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"}, {"name": "updated_at", "type": "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"}]}, {"name": "products", "columns": [{"name": "id", "type": "BIGSERIAL PRIMARY KEY"}, {"name": "name", "type": "VARCHAR(255) NOT NULL"}, {"name": "description", "type": "TEXT"}, {"name": "price", "type": "DECIMAL(10,2) NOT NULL"}, {"name": "category_id", "type": "BIGINT REFERENCES categories(id) ON DELETE SET NULL"}, {"name": "created_at", "type": "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"}]}, {"name": "categories", "columns": [{"name": "id", "type": "BIGSERIAL PRIMARY KEY"}, {"name": "name", "type": "VARCHAR(100) NOT NULL"}]}]}',
    '{"schemas": [{"name": "User", "properties": {"id": {"type": "integer", "format": "int64"}, "email": {"type": "string", "format": "email"}, "firstName": {"type": "string"}, "lastName": {"type": "string"}, "createdAt": {"type": "string", "format": "date-time"}, "updatedAt": {"type": "string", "format": "date-time"}}, "required": ["email"]}, {"name": "Product", "properties": {"id": {"type": "integer", "format": "int64"}, "name": {"type": "string"}, "description": {"type": "string"}, "price": {"type": "number", "format": "decimal"}, "categoryId": {"type": "integer", "format": "int64"}, "createdAt": {"type": "string", "format": "date-time"}}, "required": ["name", "price"]}]}'
);

-- Insertar diseño de API de ejemplo
INSERT INTO dsg_api_designs (project_id, api_name, api_type, endpoints_data, request_schemas, response_schemas) VALUES
(
    1,
    'User Management API',
    'REST',
    '[{"path": "/api/users", "method": "GET", "description": "Get all users", "parameters": [{"name": "page", "type": "integer", "required": false, "default": 1}, {"name": "size", "type": "integer", "required": false, "default": 20}], "responses": {"200": "List of users", "400": "Bad request", "401": "Unauthorized"}}, {"path": "/api/users", "method": "POST", "description": "Create a new user", "requestBody": "UserCreateRequest", "responses": {"201": "User created", "400": "Bad request", "409": "User already exists"}}, {"path": "/api/users/{id}", "method": "GET", "description": "Get user by ID", "parameters": [{"name": "id", "type": "integer", "required": true, "in": "path"}], "responses": {"200": "User found", "404": "User not found"}}]',
    '{"UserCreateRequest": {"type": "object", "properties": {"email": {"type": "string", "format": "email"}, "password": {"type": "string", "minLength": 8}, "firstName": {"type": "string"}, "lastName": {"type": "string"}}, "required": ["email", "password"]}}',
    '{"User": {"type": "object", "properties": {"id": {"type": "integer", "format": "int64"}, "email": {"type": "string", "format": "email"}, "firstName": {"type": "string"}, "lastName": {"type": "string"}, "createdAt": {"type": "string", "format": "date-time"}, "updatedAt": {"type": "string", "format": "date-time"}}}, "UserList": {"type": "object", "properties": {"users": {"type": "array", "items": {"$ref": "#/components/schemas/User"}}, "total": {"type": "integer"}, "page": {"type": "integer"}, "size": {"type": "integer"}}}'
);

-- Insertar trabajo de generación de código de ejemplo
INSERT INTO dsg_code_generation_jobs (project_id, job_type, target_technology, status, input_data, created_at) VALUES
(
    1,
    'FRONTEND',
    'React',
    'COMPLETED',
    '{"project": {"name": "Demo E-commerce", "screens": [{"name": "Home", "type": "LANDING"}], "forms": [{"name": "User Registration", "type": "USER_REGISTRATION"}]}}',
    CURRENT_TIMESTAMP - INTERVAL '1 hour'
),
(
    1,
    'BACKEND',
    'Node.js',
    'IN_PROGRESS',
    '{"project": {"name": "Demo E-commerce", "dataModels": [{"name": "User", "type": "RELATIONAL"}], "apis": [{"name": "User API", "type": "REST"}]}}',
    CURRENT_TIMESTAMP - INTERVAL '30 minutes'
);
```

## Servicios del Sistema

### **DesignService**

```java
@Service
public class DesignService {

    @Autowired
    private DesignProjectRepository projectRepository;

    @Autowired
    private CodeGenerationService generationService;

    public DesignProject createProject(CreateProjectRequest request) {
        DesignProject project = new DesignProject();
        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setProjectType(request.getProjectType());
        project.setTechnologyStack(JsonUtils.toJson(request.getTechnologyStack()));
        project.setDesignData(JsonUtils.toJson(request.getDesignData()));
        project.setStatus(DesignStatus.DRAFT);
        project.setCreatedBy(getCurrentUserId());
        project.setCreatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }

    public DesignProject updateProject(Long projectId, UpdateProjectRequest request) {
        DesignProject project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.setDesignData(JsonUtils.toJson(request.getDesignData()));
        project.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }

    public void generateCode(Long projectId, String targetTechnology) {
        DesignProject project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        CodeGenerationJob job = new CodeGenerationJob();
        job.setProjectId(projectId);
        job.setJobType(determineJobType(targetTechnology));
        job.setTargetTechnology(targetTechnology);
        job.setStatus(JobStatus.PENDING);
        job.setInputData(project.getDesignData());
        job.setCreatedAt(LocalDateTime.now());

        generationService.submitJob(job);
    }
}
```

### **CodeGenerationService**

```java
@Service
public class CodeGenerationService {

    @Autowired
    private CodeGenerationJobRepository jobRepository;

    @Autowired
    private AgentOrchestrationService agentService;

    public void submitJob(CodeGenerationJob job) {
        jobRepository.save(job);

        // Enviar trabajo al agente apropiado
        agentService.processGenerationJob(job);
    }

    public CodeGenerationJob getJobStatus(Long jobId) {
        return jobRepository.findById(jobId)
            .orElseThrow(() -> new JobNotFoundException(jobId));
    }

    public List<CodeGenerationJob> getProjectJobs(Long projectId) {
        return jobRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }
}
```

## API Endpoints

### **Gestión de Proyectos**

```java
@RestController
@RequestMapping("/api/v1/design")
public class DesignController {

    @Autowired
    private DesignService designService;

    @PostMapping("/projects")
    public ResponseEntity<DesignProject> createProject(@RequestBody CreateProjectRequest request) {
        DesignProject project = designService.createProject(request);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<DesignProject> updateProject(@PathVariable Long projectId,
                                                     @RequestBody UpdateProjectRequest request) {
        DesignProject project = designService.updateProject(projectId, request);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<DesignProject> getProject(@PathVariable Long projectId) {
        DesignProject project = designService.getProject(projectId);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/projects/{projectId}/generate")
    public ResponseEntity<CodeGenerationJob> generateCode(@PathVariable Long projectId,
                                                        @RequestBody GenerateCodeRequest request) {
        designService.generateCode(projectId, request.getTargetTechnology());
        return ResponseEntity.ok().build();
    }
}
```

### **Gestión de Templates**

```java
@RestController
@RequestMapping("/api/v1/design/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<DesignTemplate>> getTemplates(
            @RequestParam(required = false) TemplateType type,
            @RequestParam(required = false) String category) {

        List<DesignTemplate> templates = templateService.getTemplates(type, category);
        return ResponseEntity.ok(templates);
    }

    @PostMapping
    public ResponseEntity<DesignTemplate> createTemplate(@RequestBody CreateTemplateRequest request) {
        DesignTemplate template = templateService.createTemplate(request);
        return ResponseEntity.ok(template);
    }

    @PostMapping("/{templateId}/apply")
    public ResponseEntity<DesignProject> applyTemplate(@PathVariable Long templateId,
                                                     @RequestBody ApplyTemplateRequest request) {
        DesignProject project = templateService.applyTemplate(templateId, request);
        return ResponseEntity.ok(project);
    }
}
```

## Configuración de Aplicación

### **application-design.yml**

```yaml
design-system:
  # Configuración de diseñadores
  designers:
    screen-designer:
      enabled: true
      max-components: 1000
      auto-save-interval: 30s

    form-designer:
      enabled: true
      max-fields: 100
      validation-engine: "json-schema"

    data-modeler:
      enabled: true
      max-entities: 100
      relationship-validation: true

    api-designer:
      enabled: true
      max-endpoints: 200
      schema-validation: true

  # Configuración de generación de código
  code-generation:
    enabled: true
    max-concurrent-jobs: 10
    job-timeout: 30m
    auto-retry: true
    max-retries: 3

  # Configuración de templates
  templates:
    auto-load: true
    template-path: "/templates/design"
    default-templates:
      - "E-commerce Platform"
      - "Admin Dashboard"
      - "Mobile App"
      - "API Service"

  # Configuración de agentes
  agents:
    auto-discovery: true
    health-check-interval: 30s
    load-balancing: true
    fallback-strategy: "round-robin"

  # Configuración de almacenamiento
  storage:
    auto-backup: true
    backup-interval: 1h
    version-history: 50
    compression: true
```

## Integración con Agentes

### **Flujo de Generación de Código**

1. **Usuario Solicita Generación**: Selecciona tecnología objetivo
2. **Sistema Crea Job**: Genera trabajo de generación de código
3. **Agente Especializado**: Procesa JSON y genera código
4. **Validación**: Sistema valida código generado
5. **Repositorio Git**: Código se publica en repositorio
6. **Notificación**: Usuario recibe notificación de completado

### **Tipos de Agentes**

- **Frontend Agent**: Genera React, Vue, Angular, etc.
- **Backend Agent**: Genera Node.js, Python, Java, etc.
- **Database Agent**: Genera esquemas SQL, NoSQL
- **API Agent**: Genera documentación OpenAPI, Postman
- **Mobile Agent**: Genera React Native, Flutter, etc.
- **Documentation Agent**: Genera documentación técnica

## Beneficios del Sistema

- **Agnóstico a Tecnología**: Diseño independiente de frameworks
- **Generación Automática**: Código generado por agentes especializados
- **Consistencia**: Estructura JSON unificada
- **Reutilización**: Templates y componentes reutilizables
- **Colaboración**: Múltiples usuarios pueden trabajar
- **Versionado**: Control de versiones completo
- **Escalabilidad**: Sistema distribuido y escalable
- **Calidad**: Código generado con mejores prácticas
- **Rapidez**: Desarrollo acelerado con templates
- **Mantenibilidad**: Código estructurado y documentado

## Conclusión

El módulo de Diseño y Modelado proporciona un sistema completo y agnóstico para el diseño de aplicaciones. Al generar estructuras JSON internas y usar agentes especializados para la generación de código, asegura consistencia, calidad y rapidez en el desarrollo. El sistema es escalable, colaborativo y mantiene la flexibilidad para múltiples tecnologías objetivo.
