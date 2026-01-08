-- =====================================================
-- SCRIPTS SQL PARA MÓDULO GENERATOR (gen_)
-- =====================================================

-- Script de DROP de Tablas (en orden inverso a la creación)
DROP TABLE IF EXISTS gen_generation_agents CASCADE;
DROP TABLE IF EXISTS gen_generation_history CASCADE;
DROP TABLE IF EXISTS gen_generation_configs CASCADE;
DROP TABLE IF EXISTS gen_code_templates CASCADE;
DROP TABLE IF EXISTS gen_code_generation_jobs CASCADE;

-- =====================================================
-- CREACIÓN DE TABLAS
-- =====================================================

-- Tabla de trabajos de generación de código
CREATE TABLE gen_code_generation_jobs (
    id BIGSERIAL PRIMARY KEY,
    job_name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    target_technology VARCHAR(50) NOT NULL,
    source_design_id BIGINT,
    source_module VARCHAR(100),
    configuration TEXT,
    generated_files TEXT,
    error_log TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de plantillas de código
CREATE TABLE gen_code_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    technology VARCHAR(50) NOT NULL,
    template_type VARCHAR(50) NOT NULL,
    template_content TEXT,
    variables TEXT,
    is_active BOOLEAN DEFAULT true,
    version VARCHAR(20) DEFAULT '1.0.0',
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de configuraciones de generación
CREATE TABLE gen_generation_configs (
    id BIGSERIAL PRIMARY KEY,
    config_name VARCHAR(100) NOT NULL,
    description TEXT,
    technology VARCHAR(50) NOT NULL,
    config_type VARCHAR(50) NOT NULL,
    settings TEXT,
    is_default BOOLEAN DEFAULT false,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de historial de generaciones
CREATE TABLE gen_generation_history (
    id BIGSERIAL PRIMARY KEY,
    job_id BIGINT REFERENCES gen_code_generation_jobs(id) ON DELETE CASCADE,
    generation_type VARCHAR(50) NOT NULL,
    source_entity VARCHAR(100),
    source_id BIGINT,
    target_technology VARCHAR(50) NOT NULL,
    generated_files_count INTEGER DEFAULT 0,
    total_lines_of_code INTEGER DEFAULT 0,
    generation_time_seconds INTEGER,
    success_rate DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de agentes de generación
CREATE TABLE gen_generation_agents (
    id BIGSERIAL PRIMARY KEY,
    agent_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    specialization VARCHAR(50) NOT NULL,
    technology_stack TEXT,
    capabilities TEXT,
    is_active BOOLEAN DEFAULT true,
    version VARCHAR(20) DEFAULT '1.0.0',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- CREACIÓN DE ÍNDICES
-- =====================================================

-- Índices para trabajos de generación
CREATE INDEX idx_gen_jobs_name ON gen_code_generation_jobs(job_name);
CREATE INDEX idx_gen_jobs_status ON gen_code_generation_jobs(status);
CREATE INDEX idx_gen_jobs_technology ON gen_code_generation_jobs(target_technology);
CREATE INDEX idx_gen_jobs_source ON gen_code_generation_jobs(source_module, source_design_id);
CREATE INDEX idx_gen_jobs_created_by ON gen_code_generation_jobs(created_by);
CREATE INDEX idx_gen_jobs_created_at ON gen_code_generation_jobs(created_at);

-- Índices para plantillas
CREATE INDEX idx_gen_templates_name ON gen_code_templates(template_name);
CREATE INDEX idx_gen_templates_technology ON gen_code_templates(technology);
CREATE INDEX idx_gen_templates_type ON gen_code_templates(template_type);
CREATE INDEX idx_gen_templates_active ON gen_code_templates(is_active);

-- Índices para configuraciones
CREATE INDEX idx_gen_configs_name ON gen_generation_configs(config_name);
CREATE INDEX idx_gen_configs_technology ON gen_generation_configs(technology);
CREATE INDEX idx_gen_configs_type ON gen_generation_configs(config_type);
CREATE INDEX idx_gen_configs_default ON gen_generation_configs(is_default);

-- Índices para historial
CREATE INDEX idx_gen_history_job_id ON gen_generation_history(job_id);
CREATE INDEX idx_gen_history_type ON gen_generation_history(generation_type);
CREATE INDEX idx_gen_history_technology ON gen_generation_history(target_technology);
CREATE INDEX idx_gen_history_created ON gen_generation_history(created_at);

-- Índices para agentes
CREATE INDEX idx_gen_agents_name ON gen_generation_agents(agent_name);
CREATE INDEX idx_gen_agents_specialization ON gen_generation_agents(specialization);
CREATE INDEX idx_gen_agents_active ON gen_generation_agents(is_active);

-- =====================================================
-- DATOS DEMO
-- =====================================================

-- Datos demo para plantillas de código
INSERT INTO gen_code_templates (template_name, description, technology, template_type, template_content, created_by) VALUES
('react_component', 'Plantilla para componente React', 'REACT', 'COMPONENT', 'import React from "react";\n\nconst {{componentName}} = ({ {{props}} }) => {\n  return (\n    <div className="{{className}}">\n      {{content}}\n    </div>\n  );\n};\n\nexport default {{componentName}};', 1),
('spring_controller', 'Plantilla para controlador Spring Boot', 'SPRING_BOOT', 'CONTROLLER', '@RestController\n@RequestMapping("/api/v1/{{endpoint}}")\npublic class {{controllerName}} {\n\n    @Autowired\n    private {{serviceName}} {{serviceVariable}};\n\n    @GetMapping\n    public ResponseEntity<List<{{entityName}}>> getAll() {\n        return ResponseEntity.ok({{serviceVariable}}.findAll());\n    }\n}', 1),
('postgresql_table', 'Plantilla para tabla PostgreSQL', 'POSTGRESQL', 'TABLE', 'CREATE TABLE {{tableName}} (\n    id BIGSERIAL PRIMARY KEY,\n    {{columns}}\n    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n);', 1),
('docker_compose', 'Plantilla para Docker Compose', 'DOCKER', 'COMPOSE', 'version: "3.8"\n\nservices:\n  {{serviceName}}:\n    image: {{imageName}}\n    ports:\n      - "{{port}}:{{containerPort}}"\n    environment:\n      {{environmentVariables}}\n    volumes:\n      {{volumes}}', 1),
('python_api', 'Plantilla para API FastAPI', 'FASTAPI', 'API', 'from fastapi import FastAPI, HTTPException\nfrom pydantic import BaseModel\n\napp = FastAPI(title="{{apiTitle}}")\n\nclass {{modelName}}(BaseModel):\n    {{fields}}\n\n@app.get("/{{endpoint}}")\nasync def get_{{endpoint}}():\n    return {"message": "{{endpoint}} endpoint"}\n', 1);

-- Datos demo para configuraciones de generación
INSERT INTO gen_generation_configs (config_name, description, technology, config_type, settings, is_default, created_by) VALUES
('react_default', 'Configuración por defecto para React', 'REACT', 'FRONTEND', '{"useTypeScript": true, "useTailwindCSS": true, "useReactQuery": true}', true, 1),
('spring_default', 'Configuración por defecto para Spring Boot', 'SPRING_BOOT', 'BACKEND', '{"useSpringSecurity": true, "useJPA": true, "useSwagger": true}', true, 1),
('postgresql_default', 'Configuración por defecto para PostgreSQL', 'POSTGRESQL', 'DATABASE', '{"useMigrations": true, "includeIndexes": true, "namingConvention": "SNAKE_CASE"}', true, 1),
('docker_default', 'Configuración por defecto para Docker', 'DOCKER', 'INFRASTRUCTURE', '{"useMultiStage": true, "optimizeImages": true, "securityScan": true}', true, 1),
('fastapi_default', 'Configuración por defecto para FastAPI', 'FASTAPI', 'BACKEND', '{"usePydantic": true, "useSQLAlchemy": true, "useJWT": true}', true, 1);

-- Datos demo para agentes de generación
INSERT INTO gen_generation_agents (agent_name, description, specialization, technology_stack, capabilities, created_by) VALUES
('Frontend Agent', 'Agente especializado en frontend', 'FRONTEND_UI', '["React", "Vue", "Angular", "Next.js"]', '["Component Generation", "State Management", "Routing", "Styling"]', 1),
('Backend Agent', 'Agente especializado en backend', 'BACKEND_API', '["Spring Boot", "FastAPI", "Express.js", "Django"]', '["API Generation", "Database Integration", "Authentication", "Validation"]', 1),
('Database Agent', 'Agente especializado en bases de datos', 'DATABASE_SCHEMA', '["PostgreSQL", "MySQL", "MongoDB", "Redis"]', '["Schema Design", "Indexing", "Migrations", "Optimization"]', 1),
('DevOps Agent', 'Agente especializado en DevOps', 'DEVOPS_INFRASTRUCTURE', '["Docker", "Kubernetes", "Terraform", "GitHub Actions"]', '["Containerization", "Orchestration", "Infrastructure as Code", "CI/CD"]', 1),
('Testing Agent', 'Agente especializado en testing', 'TESTING_FRAMEWORK', '["Jest", "JUnit", "Pytest", "Cypress"]', '["Unit Tests", "Integration Tests", "E2E Tests", "Test Coverage"]', 1);

-- Datos demo para trabajos de generación
INSERT INTO gen_code_generation_jobs (job_name, description, status, target_technology, source_design_id, source_module, created_by) VALUES
('Portal Frontend React', 'Generación de aplicación React para portal de administración', 'COMPLETED', 'REACT', 123, 'DESIGN_MODELING', 1),
('User Management API', 'Generación de API REST para gestión de usuarios', 'IN_PROGRESS', 'SPRING_BOOT', 456, 'DESIGN_MODELING', 1),
('E-commerce Database', 'Generación de esquema de base de datos para e-commerce', 'PENDING', 'POSTGRESQL', 789, 'DESIGN_MODELING', 2),
('Microservices Docker', 'Generación de configuración Docker para microservicios', 'COMPLETED', 'DOCKER', 321, 'DESIGN_MODELING', 1),
('AI Training API', 'Generación de API para entrenamiento de modelos de IA', 'PENDING', 'FASTAPI', 654, 'DESIGN_MODELING', 3);

-- Datos demo para historial de generaciones
INSERT INTO gen_generation_history (job_id, generation_type, source_entity, source_id, target_technology, generated_files_count, total_lines_of_code, generation_time_seconds, success_rate) VALUES
(1, 'FRONTEND_APPLICATION', 'DESIGN_MODELING', 123, 'REACT', 25, 1500, 180, 95.5),
(2, 'BACKEND_API', 'DESIGN_MODELING', 456, 'SPRING_BOOT', 15, 800, 120, 92.0),
(4, 'INFRASTRUCTURE', 'DESIGN_MODELING', 321, 'DOCKER', 8, 300, 60, 98.0);

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
