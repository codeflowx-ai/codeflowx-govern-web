-- =====================================================
-- SCRIPTS SQL PARA MÓDULO USER TRAINING (trn_)
-- =====================================================

-- Script de DROP de Tablas (en orden inverso a la creación)
DROP TABLE IF EXISTS trn_course_roles CASCADE;
DROP TABLE IF EXISTS trn_certificates CASCADE;
DROP TABLE IF EXISTS trn_assessment_attempts CASCADE;
DROP TABLE IF EXISTS trn_assessment_questions CASCADE;
DROP TABLE IF EXISTS trn_assessments CASCADE;
DROP TABLE IF EXISTS trn_lesson_progress CASCADE;
DROP TABLE IF EXISTS trn_course_enrollments CASCADE;
DROP TABLE IF EXISTS trn_lessons CASCADE;
DROP TABLE IF EXISTS trn_courses CASCADE;

-- =====================================================
-- CREACIÓN DE TABLAS
-- =====================================================

-- Tabla de cursos
CREATE TABLE trn_courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    course_code VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(50) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    estimated_hours INTEGER,
    is_mandatory BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de lecciones
CREATE TABLE trn_lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lesson_number INTEGER NOT NULL,
    content_type VARCHAR(30) NOT NULL,
    content_url VARCHAR(500),
    content_text TEXT,
    estimated_minutes INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de inscripciones a cursos
CREATE TABLE trn_course_enrollments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP,
    progress_percentage DECIMAL(5,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ENROLLED',
    certificate_issued BOOLEAN DEFAULT false,
    certificate_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de progreso de lecciones
CREATE TABLE trn_lesson_progress (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    time_spent_minutes INTEGER,
    is_completed BOOLEAN DEFAULT false,
    score DECIMAL(5,2),
    attempts INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de evaluaciones
CREATE TABLE trn_assessments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assessment_type VARCHAR(20) NOT NULL,
    passing_score DECIMAL(5,2) DEFAULT 70.00,
    max_attempts INTEGER DEFAULT 3,
    time_limit_minutes INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de preguntas de evaluación
CREATE TABLE trn_assessment_questions (
    id BIGSERIAL PRIMARY KEY,
    assessment_id BIGINT REFERENCES trn_assessments(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    options TEXT,
    correct_answer TEXT,
    points DECIMAL(5,2) DEFAULT 1.00,
    order_index INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de intentos de evaluación
CREATE TABLE trn_assessment_attempts (
    id BIGSERIAL PRIMARY KEY,
    assessment_id BIGINT REFERENCES trn_assessments(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    attempt_number INTEGER NOT NULL,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    time_spent_minutes INTEGER,
    score DECIMAL(5,2),
    is_passed BOOLEAN,
    answers TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de certificados
CREATE TABLE trn_certificates (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    certificate_number VARCHAR(100) UNIQUE NOT NULL,
    issued_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_date TIMESTAMP,
    certificate_url VARCHAR(500),
    is_valid BOOLEAN DEFAULT true,
    revoked_reason TEXT,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de relación curso-rol
CREATE TABLE trn_course_roles (
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES cor_roles(id) ON DELETE CASCADE,
    PRIMARY KEY (course_id, role_id)
);

-- =====================================================
-- CREACIÓN DE ÍNDICES
-- =====================================================

CREATE INDEX idx_trn_courses_code ON trn_courses(course_code);
CREATE INDEX idx_trn_courses_category ON trn_courses(category);
CREATE INDEX idx_trn_courses_difficulty ON trn_courses(difficulty_level);
CREATE INDEX idx_trn_courses_active ON trn_courses(is_active);
CREATE INDEX idx_trn_lessons_course_id ON trn_lessons(course_id);
CREATE INDEX idx_trn_lessons_number ON trn_lessons(lesson_number);
CREATE INDEX idx_trn_enrollments_course_id ON trn_course_enrollments(course_id);
CREATE INDEX idx_trn_enrollments_user_id ON trn_course_enrollments(user_id);
CREATE INDEX idx_trn_enrollments_status ON trn_course_enrollments(status);
CREATE INDEX idx_trn_progress_lesson_id ON trn_lesson_progress(lesson_id);
CREATE INDEX idx_trn_progress_user_id ON trn_lesson_progress(user_id);
CREATE INDEX idx_trn_progress_completed ON trn_lesson_progress(is_completed);
CREATE INDEX idx_trn_assessments_course_id ON trn_assessments(course_id);
CREATE INDEX idx_trn_assessments_type ON trn_assessments(assessment_type);
CREATE INDEX idx_trn_questions_assessment_id ON trn_assessment_questions(assessment_id);
CREATE INDEX idx_trn_attempts_assessment_id ON trn_assessment_attempts(assessment_id);
CREATE INDEX idx_trn_attempts_user_id ON trn_assessment_attempts(user_id);
CREATE INDEX idx_trn_certificates_course_id ON trn_certificates(course_id);
CREATE INDEX idx_trn_certificates_user_id ON trn_certificates(user_id);
CREATE INDEX idx_trn_certificates_number ON trn_certificates(certificate_number);
CREATE INDEX idx_trn_certificates_valid ON trn_certificates(is_valid);

-- =====================================================
-- DATOS DEMO
-- =====================================================

-- Datos demo para cursos
INSERT INTO trn_courses (title, description, course_code, category, difficulty_level, estimated_hours, is_mandatory, created_by) VALUES
('Fundamentos de CodeflowX', 'Curso introductorio a la plataforma CodeflowX', 'CFX_FUNDAMENTALS_001', 'SYSTEM_BASICS', 'BEGINNER', 8, true, 1),
('Gestión de Agentes IA', 'Aprende a crear y gestionar agentes de IA', 'AI_AGENTS_001', 'AI_FUNDAMENTALS', 'INTERMEDIATE', 12, false, 1),
('Sistema RAG Avanzado', 'Implementación avanzada de RAG en CodeflowX', 'RAG_ADVANCED_001', 'RAG_SYSTEM', 'ADVANCED', 16, false, 1),
('Gobierno y Cumplimiento', 'Políticas de gobierno y cumplimiento normativo', 'GOVERNANCE_001', 'GOVERNANCE', 'INTERMEDIATE', 10, true, 1),
('Desarrollo de Notebooks', 'Creación de notebooks interactivos para IA', 'NOTEBOOKS_001', 'NOTEBOOKS', 'INTERMEDIATE', 14, false, 1);

-- Datos demo para lecciones
INSERT INTO trn_lessons (course_id, title, description, lesson_number, content_type, estimated_minutes) VALUES
(1, 'Introducción a CodeflowX', 'Visión general de la plataforma', 1, 'VIDEO', 30),
(1, 'Navegación Básica', 'Cómo navegar por la interfaz', 2, 'INTERACTIVE', 45),
(1, 'Primer Proyecto', 'Crear tu primer proyecto', 3, 'PRACTICAL_EXERCISE', 60),
(2, 'Conceptos de Agentes IA', 'Fundamentos de los agentes de IA', 1, 'VIDEO', 40),
(2, 'Creación de Agentes', 'Paso a paso para crear agentes', 2, 'INTERACTIVE', 90),
(3, 'Arquitectura RAG', 'Componentes del sistema RAG', 1, 'TEXT', 60),
(3, 'Implementación Práctica', 'Ejercicios prácticos de RAG', 2, 'PRACTICAL_EXERCISE', 120),
(4, 'IA Act Europea', 'Marco normativo europeo', 1, 'DOCUMENT', 45),
(4, 'Políticas de Gobierno', 'Implementación de políticas', 2, 'INTERACTIVE', 75),
(5, 'Jupyter Kernel', 'Configuración del kernel', 1, 'VIDEO', 50),
(5, 'Celdas Interactivas', 'Trabajo con celdas de código', 2, 'INTERACTIVE', 80);

-- Datos demo para inscripciones
INSERT INTO trn_course_enrollments (course_id, user_id, status, progress_percentage) VALUES
(1, 1, 'COMPLETED', 100.00),
(1, 2, 'IN_PROGRESS', 75.00),
(2, 1, 'ENROLLED', 0.00),
(3, 3, 'IN_PROGRESS', 50.00),
(4, 1, 'COMPLETED', 100.00),
(5, 2, 'ENROLLED', 0.00);

-- Datos demo para evaluaciones
INSERT INTO trn_assessments (course_id, title, description, assessment_type, passing_score, max_attempts) VALUES
(1, 'Evaluación Final CodeflowX', 'Evaluación completa del curso', 'EXAM', 80.00, 2),
(2, 'Quiz Agentes IA', 'Cuestionario sobre conceptos básicos', 'QUIZ', 70.00, 3),
(3, 'Proyecto RAG', 'Implementación práctica de RAG', 'PROJECT', 85.00, 1),
(4, 'Test Cumplimiento', 'Evaluación de conocimientos normativos', 'EXAM', 90.00, 2),
(5, 'Notebook Final', 'Creación de notebook funcional', 'PROJECT', 80.00, 1);

-- Datos demo para preguntas de evaluación
INSERT INTO trn_assessment_questions (assessment_id, question, question_type, options, correct_answer, points, order_index) VALUES
(1, '¿Cuál es el objetivo principal de CodeflowX?', 'MULTIPLE_CHOICE', '["Plataforma de IA", "Sistema de gestión", "Herramienta de desarrollo", "Todas las anteriores"]', 'Todas las anteriores', 2.00, 1),
(1, '¿Qué significa RAG?', 'MULTIPLE_CHOICE', '["Retrieval Augmented Generation", "Random Access Generation", "Real-time AI Generation", "Remote Access Gateway"]', 'Retrieval Augmented Generation', 1.00, 2),
(2, 'Un agente de IA puede ser una herramienta', 'TRUE_FALSE', '["Verdadero", "Falso"]', 'Verdadero', 1.00, 1),
(2, '¿Qué framework se recomienda para agentes?', 'MULTIPLE_CHOICE', '["TensorFlow", "LangChain", "PyTorch", "Scikit-learn"]', 'LangChain', 2.00, 2),
(4, '¿Qué regula la IA Act Europea?', 'MULTIPLE_CHOICE', '["Solo modelos grandes", "Solo sistemas críticos", "Sistemas de IA de alto riesgo", "Todas las IA"]', 'Sistemas de IA de alto riesgo', 3.00, 1);

-- Datos demo para certificados
INSERT INTO trn_certificates (course_id, user_id, certificate_number, expiry_date) VALUES
(1, 1, 'CERT-CFX-001-2025', '2026-01-15 00:00:00'),
(4, 1, 'CERT-GOV-001-2025', '2026-01-15 00:00:00');

-- Datos demo para roles de curso
INSERT INTO trn_course_roles (course_id, role_id) VALUES
(1, 1), -- ADMIN puede acceder a fundamentos
(2, 2), -- DEVELOPER puede acceder a agentes IA
(3, 2), -- DEVELOPER puede acceder a RAG avanzado
(4, 1), -- ADMIN puede acceder a gobierno
(5, 2); -- DEVELOPER puede acceder a notebooks

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
