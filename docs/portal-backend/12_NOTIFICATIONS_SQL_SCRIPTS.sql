-- =====================================================
-- SCRIPTS SQL PARA MÓDULO NOTIFICATIONS (ntf_)
-- =====================================================

-- Script de DROP de Tablas (en orden inverso a la creación)
DROP TABLE IF EXISTS ntf_notification_subscriptions CASCADE;
DROP TABLE IF EXISTS ntf_notification_preferences CASCADE;
DROP TABLE IF EXISTS ntf_notification_deliveries CASCADE;
DROP TABLE IF EXISTS ntf_notification_templates CASCADE;
DROP TABLE IF EXISTS ntf_notifications CASCADE;

-- =====================================================
-- CREACIÓN DE TABLAS
-- =====================================================

-- Tabla de notificaciones
CREATE TABLE ntf_notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) DEFAULT 'NORMAL',
    category VARCHAR(50),
    source_module VARCHAR(50),
    source_id BIGINT,
    metadata JSONB,
    is_read BOOLEAN DEFAULT false,
    read_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de plantillas de notificación
CREATE TABLE ntf_notification_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(100) UNIQUE NOT NULL,
    template_type VARCHAR(50) NOT NULL,
    title_template TEXT NOT NULL,
    message_template TEXT NOT NULL,
    variables JSONB,
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de entregas de notificaciones
CREATE TABLE ntf_notification_deliveries (
    id BIGSERIAL PRIMARY KEY,
    notification_id BIGINT REFERENCES ntf_notifications(id) ON DELETE CASCADE,
    delivery_channel VARCHAR(50) NOT NULL,
    delivery_status VARCHAR(50) DEFAULT 'PENDING',
    sent_at TIMESTAMP,
    delivered_at TIMESTAMP,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 3,
    next_retry_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de preferencias de notificación
CREATE TABLE ntf_notification_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    notification_type VARCHAR(50) NOT NULL,
    delivery_channel VARCHAR(50) NOT NULL,
    is_enabled BOOLEAN DEFAULT true,
    quiet_hours_start TIME,
    quiet_hours_end TIME,
    timezone VARCHAR(50) DEFAULT 'UTC',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, notification_type, delivery_channel)
);

-- Tabla de suscripciones a notificaciones
CREATE TABLE ntf_notification_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    subscription_type VARCHAR(50) NOT NULL,
    source_module VARCHAR(50),
    source_id BIGINT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, subscription_type, source_module, source_id)
);

-- =====================================================
-- CREACIÓN DE ÍNDICES
-- =====================================================

-- Índices para notificaciones
CREATE INDEX idx_ntf_notifications_user_id ON ntf_notifications(user_id);
CREATE INDEX idx_ntf_notifications_type ON ntf_notifications(notification_type);
CREATE INDEX idx_ntf_notifications_priority ON ntf_notifications(priority);
CREATE INDEX idx_ntf_notifications_category ON ntf_notifications(category);
CREATE INDEX idx_ntf_notifications_source ON ntf_notifications(source_module, source_id);
CREATE INDEX idx_ntf_notifications_read ON ntf_notifications(is_read);
CREATE INDEX idx_ntf_notifications_created ON ntf_notifications(created_at);
CREATE INDEX idx_ntf_notifications_expires ON ntf_notifications(expires_at);

-- Índices para plantillas
CREATE INDEX idx_ntf_templates_name ON ntf_notification_templates(template_name);
CREATE INDEX idx_ntf_templates_type ON ntf_notification_templates(template_type);
CREATE INDEX idx_ntf_templates_active ON ntf_notification_templates(is_active);

-- Índices para entregas
CREATE INDEX idx_ntf_deliveries_notification_id ON ntf_notification_deliveries(notification_id);
CREATE INDEX idx_ntf_deliveries_channel ON ntf_notification_deliveries(delivery_channel);
CREATE INDEX idx_ntf_deliveries_status ON ntf_notification_deliveries(delivery_status);
CREATE INDEX idx_ntf_deliveries_retry ON ntf_notification_deliveries(next_retry_at);

-- Índices para preferencias
CREATE INDEX idx_ntf_preferences_user_id ON ntf_notification_preferences(user_id);
CREATE INDEX idx_ntf_preferences_type ON ntf_notification_preferences(notification_type);
CREATE INDEX idx_ntf_preferences_channel ON ntf_notification_preferences(delivery_channel);

-- Índices para suscripciones
CREATE INDEX idx_ntf_subscriptions_user_id ON ntf_notification_subscriptions(user_id);
CREATE INDEX idx_ntf_subscriptions_type ON ntf_notification_subscriptions(subscription_type);
CREATE INDEX idx_ntf_subscriptions_source ON ntf_notification_subscriptions(source_module, source_id);

-- =====================================================
-- DATOS DEMO
-- =====================================================

-- Datos demo para plantillas de notificación
INSERT INTO ntf_notification_templates (template_name, template_type, title_template, message_template, variables, created_by) VALUES
('welcome_user', 'SYSTEM', 'Bienvenido a CodeflowX, {{user_name}}', 'Tu cuenta ha sido creada exitosamente. Comienza explorando la plataforma.', '{"user_name": "string"}', 1),
('course_completed', 'TRAINING', '¡Felicidades! Has completado {{course_name}}', 'Has obtenido {{score}} puntos en la evaluación final. Tu certificado está disponible.', '{"course_name": "string", "score": "number"}', 1),
('project_assigned', 'PROJECT', 'Nuevo proyecto asignado: {{project_name}}', 'Has sido asignado al proyecto {{project_name}} con rol {{role}}.', '{"project_name": "string", "role": "string"}', 1),
('model_training_complete', 'AI_TRAINING', 'Entrenamiento completado: {{model_name}}', 'El modelo {{model_name}} ha finalizado su entrenamiento con {{accuracy}}% de precisión.', '{"model_name": "string", "accuracy": "number"}', 1),
('system_maintenance', 'SYSTEM', 'Mantenimiento programado: {{maintenance_type}}', 'El sistema estará en mantenimiento el {{date}} de {{start_time}} a {{end_time}}.', '{"maintenance_type": "string", "date": "date", "start_time": "time", "end_time": "time"}', 1);

-- Datos demo para preferencias de notificación
INSERT INTO ntf_notification_preferences (user_id, notification_type, delivery_channel, is_enabled, quiet_hours_start, quiet_hours_end, timezone) VALUES
(1, 'SYSTEM', 'EMAIL', true, '22:00:00', '08:00:00', 'Europe/Madrid'),
(1, 'TRAINING', 'EMAIL', true, NULL, NULL, 'Europe/Madrid'),
(1, 'PROJECT', 'PUSH', true, '23:00:00', '07:00:00', 'Europe/Madrid'),
(2, 'SYSTEM', 'EMAIL', true, '21:00:00', '09:00:00', 'Europe/Madrid'),
(2, 'AI_TRAINING', 'EMAIL', true, NULL, NULL, 'Europe/Madrid'),
(3, 'SYSTEM', 'EMAIL', false, NULL, NULL, 'UTC'),
(3, 'PROJECT', 'PUSH', true, NULL, NULL, 'UTC');

-- Datos demo para suscripciones
INSERT INTO ntf_notification_subscriptions (user_id, subscription_type, source_module, source_id, is_active) VALUES
(1, 'PROJECT_UPDATES', 'PROJECT_MANAGEMENT', 1, true),
(1, 'TRAINING_PROGRESS', 'USER_TRAINING', NULL, true),
(2, 'MODEL_TRAINING', 'AI_TRAINING', NULL, true),
(2, 'SYSTEM_ALERTS', 'INFRASTRUCTURE', NULL, true),
(3, 'PROJECT_ASSIGNMENTS', 'PROJECT_MANAGEMENT', NULL, true);

-- Datos demo para notificaciones
INSERT INTO ntf_notifications (user_id, title, message, notification_type, priority, category, source_module, source_id, is_read) VALUES
(1, 'Bienvenido a CodeflowX, Admin User', 'Tu cuenta ha sido creada exitosamente. Comienza explorando la plataforma.', 'SYSTEM', 'NORMAL', 'ACCOUNT', 'CORE', 1, false),
(1, '¡Felicidades! Has completado Fundamentos de CodeflowX', 'Has obtenido 95 puntos en la evaluación final. Tu certificado está disponible.', 'TRAINING', 'HIGH', 'COURSE_COMPLETION', 'USER_TRAINING', 1, false),
(2, 'Nuevo proyecto asignado: Portal Backend', 'Has sido asignado al proyecto Portal Backend con rol Developer.', 'PROJECT', 'NORMAL', 'PROJECT_ASSIGNMENT', 'PROJECT_MANAGEMENT', 1, false),
(3, 'Entrenamiento completado: Modelo RAG', 'El modelo RAG ha finalizado su entrenamiento con 87% de precisión.', 'AI_TRAINING', 'HIGH', 'MODEL_TRAINING', 'AI_TRAINING', 1, false),
(1, 'Mantenimiento programado: Actualización de seguridad', 'El sistema estará en mantenimiento el 2025-01-20 de 02:00 a 04:00.', 'SYSTEM', 'HIGH', 'MAINTENANCE', 'INFRASTRUCTURE', NULL, false);

-- Datos demo para entregas de notificaciones
INSERT INTO ntf_notification_deliveries (notification_id, delivery_channel, delivery_status, sent_at, delivered_at) VALUES
(1, 'EMAIL', 'DELIVERED', '2025-01-15 10:00:00', '2025-01-15 10:01:00'),
(1, 'PUSH', 'DELIVERED', '2025-01-15 10:00:00', '2025-01-15 10:00:30'),
(2, 'EMAIL', 'DELIVERED', '2025-01-15 14:30:00', '2025-01-15 14:31:00'),
(3, 'PUSH', 'DELIVERED', '2025-01-15 16:00:00', '2025-01-15 16:00:15'),
(4, 'EMAIL', 'PENDING', '2025-01-15 18:00:00', NULL),
(5, 'EMAIL', 'DELIVERED', '2025-01-15 20:00:00', '2025-01-15 20:01:00');

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
