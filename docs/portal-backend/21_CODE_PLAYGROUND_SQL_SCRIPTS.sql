-- =====================================================
-- SCRIPTS SQL PARA MÓDULO CODE PLAYGROUND (plg_)
-- =====================================================

-- Script de DROP de Tablas (en orden inverso a la creación)
DROP TABLE IF EXISTS plg_execution_logs CASCADE;
DROP TABLE IF EXISTS plg_language_configs CASCADE;
DROP TABLE IF EXISTS plg_playground_files CASCADE;
DROP TABLE IF EXISTS plg_code_executions CASCADE;
DROP TABLE IF EXISTS plg_playground_sessions CASCADE;

-- =====================================================
-- CREACIÓN DE TABLAS
-- =====================================================

-- Tabla de sesiones de playground
CREATE TABLE plg_playground_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    programming_language VARCHAR(30) NOT NULL,
    session_config TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    started_at TIMESTAMP,
    last_activity TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de ejecuciones de código
CREATE TABLE plg_code_executions (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES plg_playground_sessions(id) ON DELETE CASCADE,
    code_snippet TEXT,
    input_data TEXT,
    execution_result TEXT,
    error_message TEXT,
    execution_time_ms BIGINT,
    memory_usage_mb BIGINT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de archivos del playground
CREATE TABLE plg_playground_files (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES plg_playground_sessions(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500),
    file_content TEXT,
    file_size_bytes BIGINT,
    mime_type VARCHAR(100),
    is_directory BOOLEAN DEFAULT false,
    parent_file_id BIGINT REFERENCES plg_playground_files(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de configuraciones de lenguaje
CREATE TABLE plg_language_configs (
    id BIGSERIAL PRIMARY KEY,
    programming_language VARCHAR(30) NOT NULL UNIQUE,
    version VARCHAR(20),
    runtime_config TEXT,
    allowed_libraries TEXT,
    security_restrictions TEXT,
    timeout_seconds INTEGER DEFAULT 300,
    memory_limit_mb INTEGER DEFAULT 512,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de logs de ejecución
CREATE TABLE plg_execution_logs (
    id BIGSERIAL PRIMARY KEY,
    execution_id BIGINT NOT NULL REFERENCES plg_code_executions(id) ON DELETE CASCADE,
    log_level VARCHAR(20) NOT NULL,
    message TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(100),
    metadata TEXT
);

-- =====================================================
-- CREACIÓN DE ÍNDICES
-- =====================================================

-- Índices para sesiones
CREATE INDEX idx_plg_sessions_user_id ON plg_playground_sessions(user_id);
CREATE INDEX idx_plg_sessions_language ON plg_playground_sessions(programming_language);
CREATE INDEX idx_plg_sessions_status ON plg_playground_sessions(status);
CREATE INDEX idx_plg_sessions_created ON plg_playground_sessions(created_at);
CREATE INDEX idx_plg_sessions_expires ON plg_playground_sessions(expires_at);

-- Índices para ejecuciones
CREATE INDEX idx_plg_executions_session_id ON plg_code_executions(session_id);
CREATE INDEX idx_plg_executions_status ON plg_code_executions(status);
CREATE INDEX idx_plg_executions_started ON plg_code_executions(started_at);
CREATE INDEX idx_plg_executions_completed ON plg_code_executions(completed_at);

-- Índices para archivos
CREATE INDEX idx_plg_files_session_id ON plg_playground_files(session_id);
CREATE INDEX idx_plg_files_name ON plg_playground_files(file_name);
CREATE INDEX idx_plg_files_parent ON plg_playground_files(parent_file_id);
CREATE INDEX idx_plg_files_directory ON plg_playground_files(is_directory);

-- Índices para configuraciones de lenguaje
CREATE INDEX idx_plg_lang_configs_language ON plg_language_configs(programming_language);
CREATE INDEX idx_plg_lang_configs_active ON plg_language_configs(is_active);

-- Índices para logs
CREATE INDEX idx_plg_logs_execution_id ON plg_execution_logs(execution_id);
CREATE INDEX idx_plg_logs_level ON plg_execution_logs(log_level);
CREATE INDEX idx_plg_logs_timestamp ON plg_execution_logs(timestamp);

-- =====================================================
-- DATOS DEMO
-- =====================================================

-- Datos demo para configuraciones de lenguaje
INSERT INTO plg_language_configs (programming_language, version, runtime_config, allowed_libraries, security_restrictions, timeout_seconds, memory_limit_mb) VALUES
('JAVASCRIPT', '18.0.0', '{"nodeVersion": "18", "npmEnabled": true}', '["lodash", "axios", "moment"]', '{"blockedCommands": ["rm", "del", "format"]}', 300, 512),
('PYTHON', '3.11.0', '{"pythonVersion": "3.11", "pipEnabled": true}', '["requests", "pandas", "numpy"]', '{"blockedCommands": ["rm", "del", "format"]}', 300, 512),
('JAVA', '17.0.0', '{"javaVersion": "17", "mavenEnabled": true}', '["junit", "mockito", "spring-boot"]', '{"blockedCommands": ["rm", "del", "format"]}', 600, 1024),
('SQL', '15.0.0', '{"database": "postgresql", "readOnly": false}', '[]', '{"blockedCommands": ["DROP", "TRUNCATE", "DELETE"]}', 120, 256),
('BASH', '5.0.0', '{"shell": "bash", "limitedCommands": true}', '[]', '{"blockedCommands": ["rm", "del", "format", "shutdown"]}', 60, 128),
('DOCKER', '20.0.0', '{"dockerVersion": "20", "privileged": false}', '[]', '{"blockedCommands": ["rm", "del", "format"]}', 300, 1024);

-- Datos demo para sesiones de playground
INSERT INTO plg_playground_sessions (session_name, description, user_id, programming_language, status, started_at, expires_at) VALUES
('JavaScript Testing', 'Sesión para testing de algoritmos JavaScript', 1, 'JAVASCRIPT', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '24 hours'),
('Python ML Testing', 'Sesión para testing de machine learning con Python', 2, 'PYTHON', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '24 hours'),
('Java API Testing', 'Sesión para testing de APIs Java', 1, 'JAVA', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '24 hours'),
('SQL Queries', 'Sesión para testing de consultas SQL', 3, 'SQL', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '24 hours'),
('Docker Testing', 'Sesión para testing de contenedores Docker', 2, 'DOCKER', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '24 hours');

-- Datos demo para archivos del playground
INSERT INTO plg_playground_files (session_id, file_name, file_path, file_content, file_size_bytes, mime_type, is_directory) VALUES
(1, 'main.js', '/app/main.js', 'function quickSort(arr) {\n    if (arr.length <= 1) return arr;\n    \n    const pivot = arr[Math.floor(arr.length / 2)];\n    const left = arr.filter(x => x < pivot);\n    const middle = arr.filter(x => x === pivot);\n    const right = arr.filter(x => x > pivot);\n    \n    return [...quickSort(left), ...middle, ...quickSort(right)];\n}\n\nconst testArray = [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5];\nconsole.log("Original:", testArray);\nconsole.log("Ordenado:", quickSort(testArray));', 450, 'application/javascript', false),
(1, 'test.js', '/app/test.js', '// Test cases for quickSort\nconst testCases = [\n    [1, 2, 3, 4, 5],\n    [5, 4, 3, 2, 1],\n    [1],\n    [],\n    [3, 3, 3, 3]\n];\n\ntestCases.forEach((testCase, index) => {\n    console.log(`Test case ${index + 1}:`, testCase);\n    console.log("Result:", quickSort([...testCase]));\n});', 280, 'application/javascript', false),
(2, 'ml_test.py', '/app/ml_test.py', 'import numpy as np\nimport pandas as pd\nfrom sklearn.linear_model import LinearRegression\n\n# Generate sample data\nX = np.random.rand(100, 2)\ny = 2 * X[:, 0] + 3 * X[:, 1] + np.random.rand(100) * 0.1\n\n# Create and train model\nmodel = LinearRegression()\nmodel.fit(X, y)\n\n# Make predictions\npredictions = model.predict(X)\n\n# Calculate accuracy\naccuracy = model.score(X, y)\nprint(f"Model accuracy: {accuracy:.4f}")\nprint(f"Coefficients: {model.coef_}")\nprint(f"Intercept: {model.intercept_:.4f}")', 520, 'text/x-python', false),
(3, 'ApiTest.java', '/app/ApiTest.java', 'import org.springframework.web.bind.annotation.*;\nimport org.springframework.http.ResponseEntity;\nimport java.util.List;\nimport java.util.ArrayList;\n\n@RestController\n@RequestMapping("/api/test")\npublic class ApiTest {\n    \n    @GetMapping("/hello")\n    public ResponseEntity<String> hello() {\n        return ResponseEntity.ok("Hello from Java API!");\n    }\n    \n    @PostMapping("/calculate")\n    public ResponseEntity<Integer> calculate(@RequestBody CalculationRequest request) {\n        int result = request.getA() + request.getB();\n        return ResponseEntity.ok(result);\n    }\n}', 680, 'text/x-java-source', false),
(4, 'complex_query.sql', '/app/complex_query.sql', 'WITH user_stats AS (\n    SELECT \n        u.department_id,\n        d.name as department_name,\n        COUNT(*) as user_count,\n        AVG(u.salary) as avg_salary\n    FROM users u\n    JOIN departments d ON u.department_id = d.id\n    WHERE u.is_active = true\n    GROUP BY u.department_id, d.name\n)\nSELECT \n    department_name,\n    user_count,\n    ROUND(avg_salary, 2) as avg_salary,\n    CASE \n        WHEN avg_salary > 50000 THEN "High"\n        WHEN avg_salary > 30000 THEN "Medium"\n        ELSE "Low"\n    END as salary_category\nFROM user_stats\nORDER BY avg_salary DESC;', 890, 'application/sql', false);

-- Datos demo para ejecuciones de código
INSERT INTO plg_code_executions (session_id, code_snippet, status, execution_result, execution_time_ms, memory_usage_mb, started_at, completed_at) VALUES
(1, 'const arr = [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5];\nconsole.log(quickSort(arr));', 'COMPLETED', 'Original: [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5]\nOrdenado: [1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9]', 150, 45, CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP - INTERVAL '2 hours' + INTERVAL '150 milliseconds'),
(2, 'print("Testing numpy and pandas")\nimport numpy as np\nprint(f"NumPy version: {np.__version__}")', 'COMPLETED', 'Testing numpy and pandas\nNumPy version: 1.24.3', 80, 120, CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '1 hour' + INTERVAL '80 milliseconds'),
(3, 'System.out.println("Hello from Java!");', 'COMPLETED', 'Hello from Java!', 200, 256, CURRENT_TIMESTAMP - INTERVAL '30 minutes', CURRENT_TIMESTAMP - INTERVAL '30 minutes' + INTERVAL '200 milliseconds'),
(4, 'SELECT COUNT(*) FROM users WHERE is_active = true;', 'COMPLETED', 'count\n-----\n  150', 50, 64, CURRENT_TIMESTAMP - INTERVAL '15 minutes', CURRENT_TIMESTAMP - INTERVAL '15 minutes' + INTERVAL '50 milliseconds'),
(5, 'docker --version', 'COMPLETED', 'Docker version 20.10.21, build baeda1f', 30, 32, CURRENT_TIMESTAMP - INTERVAL '5 minutes', CURRENT_TIMESTAMP - INTERVAL '5 minutes' + INTERVAL '30 milliseconds');

-- Datos demo para logs de ejecución
INSERT INTO plg_execution_logs (execution_id, log_level, message, source, metadata) VALUES
(1, 'INFO', 'Code execution started', 'JavaScript Runtime', '{"language": "javascript", "version": "18.0.0"}'),
(1, 'INFO', 'Function quickSort loaded successfully', 'JavaScript Runtime', '{"function": "quickSort", "parameters": 1}'),
(1, 'INFO', 'Code execution completed successfully', 'JavaScript Runtime', '{"executionTime": 150, "memoryUsage": 45}'),
(2, 'INFO', 'Python environment initialized', 'Python Runtime', '{"version": "3.11.0", "path": "/usr/local/bin/python"}'),
(2, 'INFO', 'Libraries imported successfully', 'Python Runtime', '{"libraries": ["numpy", "pandas"]}'),
(3, 'INFO', 'Java compilation started', 'Java Runtime', '{"version": "17.0.0", "compiler": "javac"}'),
(3, 'INFO', 'Java execution completed', 'Java Runtime', '{"executionTime": 200, "memoryUsage": 256}'),
(4, 'INFO', 'SQL connection established', 'PostgreSQL Runtime', '{"database": "playground", "user": "playground_user"}'),
(4, 'INFO', 'Query executed successfully', 'PostgreSQL Runtime', '{"rows": 1, "executionTime": 50}'),
(5, 'INFO', 'Docker command executed', 'Docker Runtime', '{"command": "docker --version", "result": "success"}');

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
