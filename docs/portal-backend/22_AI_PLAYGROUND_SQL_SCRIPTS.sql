-- =====================================================
-- SCRIPTS SQL PARA MÓDULO AI PLAYGROUND (aip_)
-- =====================================================

-- Script de DROP de Tablas (en orden inverso a la creación)
DROP TABLE IF EXISTS aip_performance_metrics CASCADE;
DROP TABLE IF EXISTS aip_model_configs CASCADE;
DROP TABLE IF EXISTS aip_test_datasets CASCADE;
DROP TABLE IF EXISTS aip_model_tests CASCADE;
DROP TABLE IF EXISTS aip_ai_playground_sessions CASCADE;

-- =====================================================
-- CREACIÓN DE TABLAS
-- =====================================================

-- Tabla de sesiones de AI Playground
CREATE TABLE aip_ai_playground_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    session_type VARCHAR(50) NOT NULL,
    model_config TEXT,
    dataset_config TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    started_at TIMESTAMP,
    last_activity TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tests de modelos
CREATE TABLE aip_model_tests (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES aip_ai_playground_sessions(id) ON DELETE CASCADE,
    test_name VARCHAR(255) NOT NULL,
    model_id BIGINT,
    test_type VARCHAR(50) NOT NULL,
    input_data TEXT,
    expected_output TEXT,
    actual_output TEXT,
    performance_metrics TEXT,
    execution_time_ms BIGINT,
    tokens_used INTEGER,
    cost_usd DECIMAL(10,6),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de datasets de prueba
CREATE TABLE aip_test_datasets (
    id BIGSERIAL PRIMARY KEY,
    dataset_name VARCHAR(255) NOT NULL,
    description TEXT,
    dataset_type VARCHAR(50) NOT NULL,
    data_schema TEXT,
    sample_data TEXT,
    total_records INTEGER,
    file_size_mb BIGINT,
    file_path VARCHAR(500),
    is_public BOOLEAN DEFAULT false,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de configuraciones de modelos
CREATE TABLE aip_model_configs (
    id BIGSERIAL PRIMARY KEY,
    config_name VARCHAR(255) NOT NULL,
    description TEXT,
    model_provider VARCHAR(100) NOT NULL,
    model_name VARCHAR(255) NOT NULL,
    model_version VARCHAR(50),
    parameters TEXT,
    api_config TEXT,
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de métricas de rendimiento
CREATE TABLE aip_performance_metrics (
    id BIGSERIAL PRIMARY KEY,
    test_id BIGINT NOT NULL REFERENCES aip_model_tests(id) ON DELETE CASCADE,
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(15,6) NOT NULL,
    metric_unit VARCHAR(50),
    threshold_value DECIMAL(15,6),
    is_threshold_met BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- CREACIÓN DE ÍNDICES
-- =====================================================

-- Índices para sesiones
CREATE INDEX idx_aip_sessions_user_id ON aip_ai_playground_sessions(user_id);
CREATE INDEX idx_aip_sessions_type ON aip_ai_playground_sessions(session_type);
CREATE INDEX idx_aip_sessions_status ON aip_ai_playground_sessions(status);
CREATE INDEX idx_aip_sessions_created ON aip_ai_playground_sessions(created_at);
CREATE INDEX idx_aip_sessions_expires ON aip_ai_playground_sessions(expires_at);

-- Índices para tests
CREATE INDEX idx_aip_tests_session_id ON aip_model_tests(session_id);
CREATE INDEX idx_aip_tests_model_id ON aip_model_tests(model_id);
CREATE INDEX idx_aip_tests_type ON aip_model_tests(test_type);
CREATE INDEX idx_aip_tests_status ON aip_model_tests(status);
CREATE INDEX idx_aip_tests_created ON aip_model_tests(created_at);

-- Índices para datasets
CREATE INDEX idx_aip_datasets_name ON aip_test_datasets(dataset_name);
CREATE INDEX idx_aip_datasets_type ON aip_test_datasets(dataset_type);
CREATE INDEX idx_aip_datasets_public ON aip_test_datasets(is_public);
CREATE INDEX idx_aip_datasets_created_by ON aip_test_datasets(created_by);

-- Índices para configuraciones de modelos
CREATE INDEX idx_aip_model_configs_name ON aip_model_configs(config_name);
CREATE INDEX idx_aip_model_configs_provider ON aip_model_configs(model_provider);
CREATE INDEX idx_aip_model_configs_active ON aip_model_configs(is_active);
CREATE INDEX idx_aip_model_configs_created_by ON aip_model_configs(created_by);

-- Índices para métricas
CREATE INDEX idx_aip_metrics_test_id ON aip_performance_metrics(test_id);
CREATE INDEX idx_aip_metrics_name ON aip_performance_metrics(metric_name);
CREATE INDEX idx_aip_metrics_threshold ON aip_performance_metrics(is_threshold_met);

-- =====================================================
-- DATOS DEMO
-- =====================================================

-- Datos demo para configuraciones de modelos
INSERT INTO aip_model_configs (config_name, description, model_provider, model_name, model_version, parameters, api_config, created_by) VALUES
('GPT-4 Default', 'Configuración por defecto para GPT-4', 'OpenAI', 'gpt-4', '1106-preview', '{"temperature": 0.7, "max_tokens": 1000, "top_p": 1.0}', '{"api_key": "env:OPENAI_API_KEY", "base_url": "https://api.openai.com/v1"}', 1),
('Claude-3 Default', 'Configuración por defecto para Claude-3', 'Anthropic', 'claude-3-sonnet', '20240229', '{"temperature": 0.7, "max_tokens": 1000}', '{"api_key": "env:ANTHROPIC_API_KEY", "base_url": "https://api.anthropic.com"}', 1),
('LLaMA Local', 'Configuración para LLaMA local', 'Local', 'llama-2-7b', '7b', '{"temperature": 0.7, "max_tokens": 1000, "top_p": 0.9}', '{"endpoint": "http://localhost:8080", "model_path": "/models/llama-2-7b"}', 1),
('Mistral Default', 'Configuración por defecto para Mistral', 'Mistral AI', 'mistral-large', 'latest', '{"temperature": 0.7, "max_tokens": 1000}', '{"api_key": "env:MISTRAL_API_KEY", "base_url": "https://api.mistral.ai"}', 1),
('Embeddings OpenAI', 'Configuración para embeddings de OpenAI', 'OpenAI', 'text-embedding-ada-002', 'v2', '{"dimensions": 1536}', '{"api_key": "env:OPENAI_API_KEY", "base_url": "https://api.openai.com/v1"}', 1);

-- Datos demo para datasets de prueba
INSERT INTO aip_test_datasets (dataset_name, description, dataset_type, data_schema, sample_data, total_records, file_size_mb, is_public, created_by) VALUES
('Product Descriptions', 'Dataset de descripciones de productos para testing de generación de texto', 'TEXT_GENERATION', '{"fields": ["product_name", "features", "target_audience", "expected_output"]}', '[{"product_name": "Smartphone Android", "features": ["5G", "128GB", "Triple Camera"], "target_audience": "Profesionales jóvenes", "expected_output": "Descripción profesional del producto"}]', 100, 2.5, true, 1),
('QA Technical Docs', 'Dataset de preguntas y respuestas sobre documentación técnica', 'QUESTION_ANSWERING', '{"fields": ["context", "question", "expected_answer", "difficulty"]}', '[{"context": "Documentación de CodeflowX", "question": "¿Cómo configurar un agente?", "expected_answer": "Pasos de configuración", "difficulty": "medium"}]', 50, 1.8, true, 1),
('Code Generation', 'Dataset de prompts para generación de código', 'CODE_GENERATION', '{"fields": ["prompt", "language", "expected_output", "complexity"]}', '[{"prompt": "Crear función de ordenamiento", "language": "Python", "expected_output": "def quicksort(arr):", "complexity": "easy"}]', 75, 3.2, true, 1),
('Sentiment Analysis', 'Dataset de textos para análisis de sentimientos', 'SENTIMENT_ANALYSIS', '{"fields": ["text", "expected_sentiment", "confidence"]}', '[{"text": "Me encanta este producto", "expected_sentiment": "positive", "confidence": 0.9}]', 200, 4.1, true, 2),
('Translation Pairs', 'Dataset de pares de traducción para testing', 'TRANSLATION', '{"fields": ["source_text", "source_language", "target_language", "expected_translation"]}', '[{"source_text": "Hello world", "source_language": "en", "target_language": "es", "expected_translation": "Hola mundo"}]', 150, 2.8, true, 2);

-- Datos demo para sesiones de AI Playground
INSERT INTO aip_ai_playground_sessions (session_name, description, user_id, session_type, model_config, dataset_config, status, started_at, expires_at) VALUES
('Text Generation Testing', 'Sesión para testing de generación de texto con GPT-4', 1, 'MODEL_TESTING', '{"model_id": 1, "temperature": 0.7}', '{"dataset_id": 1, "sample_size": 10}', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '48 hours'),
('RAG System Testing', 'Sesión para testing de sistema RAG con Claude-3', 2, 'RAG_TESTING', '{"model_id": 2, "temperature": 0.5}', '{"dataset_id": 2, "context_window": 4000}', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '48 hours'),
('Code Generation Testing', 'Sesión para testing de generación de código con LLaMA', 1, 'MODEL_TESTING', '{"model_id": 3, "temperature": 0.3}', '{"dataset_id": 3, "sample_size": 15}', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '48 hours'),
('Fine-tuning Testing', 'Sesión para testing de fine-tuning con Mistral', 3, 'FINE_TUNING', '{"model_id": 4, "learning_rate": 0.0001}', '{"dataset_id": 4, "train_split": 0.8}', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '48 hours'),
('Embedding Testing', 'Sesión para testing de embeddings con OpenAI', 2, 'EMBEDDING_TESTING', '{"model_id": 5, "dimensions": 1536}', '{"dataset_id": 5, "sample_size": 20}', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '48 hours');

-- Datos demo para tests de modelos
INSERT INTO aip_model_tests (session_id, test_name, model_id, test_type, input_data, expected_output, status, execution_time_ms, tokens_used, cost_usd, started_at, completed_at) VALUES
(1, 'Product Description Generation', 1, 'TEXT_GENERATION', '{"product": "Smartphone Android", "features": ["5G", "128GB", "Triple Camera"], "target_audience": "Profesionales jóvenes"}', 'Descripción profesional y atractiva del producto', 'COMPLETED', 2500, 150, 0.0030, CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP - INTERVAL '2 hours' + INTERVAL '2.5 seconds'),
(2, 'Technical QA Test', 2, 'QUESTION_ANSWERING', '{"context": "Documentación de CodeflowX", "question": "¿Cómo configurar un agente de IA?"}', 'Respuesta basada en la documentación técnica', 'COMPLETED', 3200, 200, 0.0040, CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '1 hour' + INTERVAL '3.2 seconds'),
(3, 'Python Function Generation', 3, 'CODE_GENERATION', '{"prompt": "Crear función de ordenamiento quicksort en Python", "language": "Python"}', 'def quicksort(arr):', 'COMPLETED', 1800, 120, 0.0000, CURRENT_TIMESTAMP - INTERVAL '30 minutes', CURRENT_TIMESTAMP - INTERVAL '30 minutes' + INTERVAL '1.8 seconds'),
(4, 'Sentiment Analysis Test', 4, 'SENTIMENT_ANALYSIS', '{"text": "Este producto es increíble, lo recomiendo totalmente"}', 'positive', 'COMPLETED', 1500, 80, 0.0020, CURRENT_TIMESTAMP - INTERVAL '15 minutes', CURRENT_TIMESTAMP - INTERVAL '15 minutes' + INTERVAL '1.5 seconds'),
(5, 'Translation Test', 5, 'TRANSLATION', '{"text": "Hello world", "source_language": "en", "target_language": "es"}', 'Hola mundo', 'COMPLETED', 1200, 60, 0.0010, CURRENT_TIMESTAMP - INTERVAL '5 minutes', CURRENT_TIMESTAMP - INTERVAL '5 minutes' + INTERVAL '1.2 seconds');

-- Datos demo para métricas de rendimiento
INSERT INTO aip_performance_metrics (test_id, metric_name, metric_value, metric_unit, threshold_value, is_threshold_met) VALUES
(1, 'accuracy', 0.92, 'percentage', 0.85, true),
(1, 'response_time', 2.5, 'seconds', 5.0, true),
(1, 'cost_per_token', 0.00002, 'USD', 0.00005, true),
(1, 'quality_score', 0.88, 'score', 0.80, true),
(2, 'accuracy', 0.89, 'percentage', 0.85, true),
(2, 'response_time', 3.2, 'seconds', 5.0, true),
(2, 'context_relevance', 0.91, 'score', 0.85, true),
(3, 'code_quality', 0.94, 'score', 0.80, true),
(3, 'syntax_correctness', 0.98, 'percentage', 0.95, true),
(3, 'execution_success', 1.0, 'percentage', 0.90, true),
(4, 'sentiment_accuracy', 0.96, 'percentage', 0.85, true),
(4, 'confidence_score', 0.89, 'score', 0.80, true),
(5, 'translation_accuracy', 0.93, 'percentage', 0.85, true),
(5, 'fluency_score', 0.91, 'score', 0.80, true);

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
