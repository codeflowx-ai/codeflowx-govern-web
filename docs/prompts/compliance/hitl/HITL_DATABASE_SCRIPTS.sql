-- ============================================================================
-- SCRIPTS SQL PARA MÓDULO HITL (Human-In-The-Loop)
-- ============================================================================
-- Módulo: HITL Supervision (EU AI Act Art. 14)
-- Base de Datos: PostgreSQL
-- Fecha: Diciembre 2025
-- ============================================================================
--
-- Este archivo contiene scripts SQL para:
-- 1. Disparadores (Triggers)
-- 2. Funciones (Functions)
-- 3. Vistas (Views)
-- 4. Procedimientos almacenados (Stored Procedures)
--
-- NOTA: Los índices y claves únicas están definidos en las anotaciones JPA
-- de las entidades HitlSupervision y HitlDecision. JPA los creará automáticamente
-- si se usa ddl-auto: update o create.
-- ============================================================================

-- ============================================================================
-- 1. DISPARADORES (TRIGGERS)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Trigger: Actualizar campo HITLUPDATEDAT automáticamente
-- ----------------------------------------------------------------------------
-- Este trigger actualiza el campo HITLUPDATEDAT cuando se modifica un registro
-- en la tabla GOVHITLSUPERVISIONS. Aunque JPA ya lo hace con @PreUpdate,
-- este trigger asegura la integridad a nivel de base de datos.
-- ----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION update_hitl_supervision_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.HITLUPDATEDAT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_hitl_supervision_updated_at
    BEFORE UPDATE ON GOVHITLSUPERVISIONS
    FOR EACH ROW
    EXECUTE FUNCTION update_hitl_supervision_updated_at();

-- ----------------------------------------------------------------------------
-- Trigger: Actualizar campo HITLUPDATEDAT automáticamente en decisiones
-- ----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION update_hitl_decision_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.HITLUPDATEDAT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_hitl_decision_updated_at
    BEFORE UPDATE ON GOVHITLDECISIONS
    FOR EACH ROW
    EXECUTE FUNCTION update_hitl_decision_updated_at();

-- ----------------------------------------------------------------------------
-- Trigger: Validar que una supervisión solo tenga una decisión
-- ----------------------------------------------------------------------------
-- Este trigger previene que se inserten múltiples decisiones para la misma
-- supervisión. La clave única ya lo previene, pero este trigger proporciona
-- un mensaje de error más descriptivo.
-- ----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION validate_single_decision_per_supervision()
RETURNS TRIGGER AS $$
DECLARE
    decision_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO decision_count
    FROM GOVHITLDECISIONS
    WHERE IDXHITLSUPERVISION = NEW.IDXHITLSUPERVISION;

    IF decision_count > 0 THEN
        RAISE EXCEPTION 'Una supervisión solo puede tener una decisión. Supervisión ID: %', NEW.IDXHITLSUPERVISION;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_single_decision
    BEFORE INSERT ON GOVHITLDECISIONS
    FOR EACH ROW
    EXECUTE FUNCTION validate_single_decision_per_supervision();

-- ----------------------------------------------------------------------------
-- Trigger: Calcular tiempo de respuesta automáticamente
-- ----------------------------------------------------------------------------
-- Este trigger calcula automáticamente el tiempo de respuesta (en minutos)
-- cuando se inserta una decisión, basándose en la diferencia entre la fecha
-- de creación de la supervisión y la fecha de decisión.
-- ----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION calculate_hitl_response_time()
RETURNS TRIGGER AS $$
DECLARE
    supervision_created TIMESTAMP;
    response_time_minutes INTEGER;
BEGIN
    -- Obtener fecha de creación de la supervisión
    SELECT HITLCREATEDAT INTO supervision_created
    FROM GOVHITLSUPERVISIONS
    WHERE IDXHITLSUPERVISION = NEW.IDXHITLSUPERVISION;

    -- Calcular tiempo de respuesta en minutos
    IF supervision_created IS NOT NULL AND NEW.HITLDECISIONDATE IS NOT NULL THEN
        response_time_minutes := EXTRACT(EPOCH FROM (NEW.HITLDECISIONDATE - supervision_created)) / 60;
        NEW.HITLRESPONSETIME := response_time_minutes;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_calculate_response_time
    BEFORE INSERT OR UPDATE ON GOVHITLDECISIONS
    FOR EACH ROW
    WHEN (NEW.HITLDECISIONDATE IS NOT NULL)
    EXECUTE FUNCTION calculate_hitl_response_time();

-- ============================================================================
-- 2. FUNCIONES (FUNCTIONS)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Función: Obtener métricas de HITL para un proyecto
-- ----------------------------------------------------------------------------
-- Calcula métricas agregadas de HITL para un proyecto específico:
-- - Tiempo promedio de respuesta
-- - Tasa de aprobación
-- - Cumplimiento de SLA
-- - Total de intervenciones
-- ----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION get_hitl_metrics_for_project(p_project_id BIGINT)
RETURNS TABLE (
    average_response_time_hours NUMERIC,
    approval_rate NUMERIC,
    sla_compliance_rate NUMERIC,
    total_interventions BIGINT,
    pending_interventions BIGINT,
    approved_decisions BIGINT,
    rejected_decisions BIGINT,
    modified_decisions BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        -- Tiempo promedio de respuesta en horas
        COALESCE(AVG(d.HITLRESPONSETIME::NUMERIC / 60.0), 0) AS average_response_time_hours,

        -- Tasa de aprobación (decisiones APPROVED / total decisiones)
        COALESCE(
            SUM(CASE WHEN d.HITLDECISION = 'APPROVED' THEN 1 ELSE 0 END)::NUMERIC /
            NULLIF(COUNT(d.IDXHITLDECISION), 0),
            0
        ) AS approval_rate,

        -- Cumplimiento de SLA (decisiones dentro de SLA / total decisiones)
        COALESCE(
            SUM(CASE
                WHEN d.HITLRESPONSETIME IS NOT NULL
                     AND s.HITLSLA IS NOT NULL
                     AND d.HITLRESPONSETIME <= (s.HITLSLA * 60)
                THEN 1
                ELSE 0
            END)::NUMERIC /
            NULLIF(COUNT(d.IDXHITLDECISION), 0),
            0
        ) AS sla_compliance_rate,

        -- Total de intervenciones
        COUNT(DISTINCT s.IDXHITLSUPERVISION) AS total_interventions,

        -- Intervenciones pendientes (sin decisión)
        COUNT(DISTINCT CASE WHEN d.IDXHITLDECISION IS NULL THEN s.IDXHITLSUPERVISION END) AS pending_interventions,

        -- Decisiones aprobadas
        SUM(CASE WHEN d.HITLDECISION = 'APPROVED' THEN 1 ELSE 0 END) AS approved_decisions,

        -- Decisiones rechazadas
        SUM(CASE WHEN d.HITLDECISION = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_decisions,

        -- Decisiones modificadas
        SUM(CASE WHEN d.HITLDECISION = 'MODIFIED' THEN 1 ELSE 0 END) AS modified_decisions

    FROM GOVHITLSUPERVISIONS s
    LEFT JOIN GOVHITLDECISIONS d ON s.IDXHITLSUPERVISION = d.IDXHITLSUPERVISION
    WHERE s.IDXPROJECT = p_project_id;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------------
-- Función: Obtener intervenciones pendientes con SLA vencido
-- ----------------------------------------------------------------------------
-- Retorna intervenciones que están pendientes y cuyo SLA ha vencido.
-- Útil para alertas y notificaciones.
-- ----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION get_pending_interventions_with_expired_sla()
RETURNS TABLE (
    supervision_id BIGINT,
    project_id BIGINT,
    entity_type VARCHAR,
    entity_id BIGINT,
    entity_name VARCHAR,
    supervision_type VARCHAR,
    sla_hours INTEGER,
    created_at TIMESTAMP,
    hours_overdue NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        s.IDXHITLSUPERVISION AS supervision_id,
        s.IDXPROJECT AS project_id,
        s.HITLENTITYTYPE AS entity_type,
        s.IDXENTITY AS entity_id,
        s.HITLENTITYNAME AS entity_name,
        s.HITLSUPERVISIONTYPE AS supervision_type,
        s.HITLSLA AS sla_hours,
        s.HITLCREATEDAT AS created_at,
        EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - s.HITLCREATEDAT)) / 3600.0 - s.HITLSLA AS hours_overdue
    FROM GOVHITLSUPERVISIONS s
    LEFT JOIN GOVHITLDECISIONS d ON s.IDXHITLSUPERVISION = d.IDXHITLSUPERVISION
    WHERE d.IDXHITLDECISION IS NULL  -- Sin decisión
      AND s.HITLSLA IS NOT NULL
      AND (CURRENT_TIMESTAMP - s.HITLCREATEDAT) > (s.HITLSLA || ' hours')::INTERVAL
    ORDER BY hours_overdue DESC;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------------
-- Función: Obtener estadísticas de decisiones por tipo de entidad
-- ----------------------------------------------------------------------------
-- Retorna estadísticas agregadas de decisiones agrupadas por tipo de entidad
-- (AGENT, MODEL, PROMPT).
-- ----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION get_decision_stats_by_entity_type()
RETURNS TABLE (
    entity_type VARCHAR,
    total_decisions BIGINT,
    approved_count BIGINT,
    rejected_count BIGINT,
    modified_count BIGINT,
    average_response_time_hours NUMERIC,
    sla_compliance_rate NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        d.HITLENTITYTYPE AS entity_type,
        COUNT(*) AS total_decisions,
        SUM(CASE WHEN d.HITLDECISION = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,
        SUM(CASE WHEN d.HITLDECISION = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,
        SUM(CASE WHEN d.HITLDECISION = 'MODIFIED' THEN 1 ELSE 0 END) AS modified_count,
        AVG(d.HITLRESPONSETIME::NUMERIC / 60.0) AS average_response_time_hours,
        COALESCE(
            SUM(CASE
                WHEN d.HITLRESPONSETIME IS NOT NULL
                     AND s.HITLSLA IS NOT NULL
                     AND d.HITLRESPONSETIME <= (s.HITLSLA * 60)
                THEN 1
                ELSE 0
            END)::NUMERIC /
            NULLIF(COUNT(*), 0),
            0
        ) AS sla_compliance_rate
    FROM GOVHITLDECISIONS d
    JOIN GOVHITLSUPERVISIONS s ON d.IDXHITLSUPERVISION = s.IDXHITLSUPERVISION
    GROUP BY d.HITLENTITYTYPE
    ORDER BY d.HITLENTITYTYPE;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- 3. VISTAS (VIEWS)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Vista: Resumen de intervenciones HITL
-- ----------------------------------------------------------------------------
-- Vista que agrega información de supervisión y decisión para facilitar
-- consultas y reportes.
-- ----------------------------------------------------------------------------

CREATE OR REPLACE VIEW v_hitl_interventions_summary AS
SELECT
    s.IDXHITLSUPERVISION AS supervision_id,
    s.IDUUID AS supervision_uuid,
    s.IDXPROJECT AS project_id,
    s.HITLSUPERVISIONTYPE AS supervision_type,
    s.HITLENTITYTYPE AS entity_type,
    s.IDXENTITY AS entity_id,
    s.HITLENTITYNAME AS entity_name,
    s.HITLSLA AS sla_hours,
    s.HITLCREATEDAT AS created_at,
    s.HITLUPDATEDAT AS updated_at,
    CASE
        WHEN d.IDXHITLDECISION IS NULL THEN 'PENDING'
        ELSE 'RESOLVED'
    END AS status,
    d.IDXHITLDECISION AS decision_id,
    d.HITLDECISION AS decision,
    d.HITLDECISIONDATE AS decision_date,
    d.HITLRESPONSETIME AS response_time_minutes,
    d.HITLUSERID AS decision_user_id,
    CASE
        WHEN d.IDXHITLDECISION IS NULL AND s.HITLSLA IS NOT NULL THEN
            EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - s.HITLCREATEDAT)) / 3600.0 - s.HITLSLA
        ELSE NULL
    END AS hours_overdue,
    CASE
        WHEN d.IDXHITLDECISION IS NULL AND s.HITLSLA IS NOT NULL THEN
            CASE
                WHEN EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - s.HITLCREATEDAT)) / 3600.0 - s.HITLSLA < -1 THEN 'LOW'
                WHEN EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - s.HITLCREATEDAT)) / 3600.0 - s.HITLSLA < 0 THEN 'MEDIUM'
                WHEN EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - s.HITLCREATEDAT)) / 3600.0 - s.HITLSLA < 1 THEN 'HIGH'
                ELSE 'CRITICAL'
            END
        ELSE NULL
    END AS urgency
FROM GOVHITLSUPERVISIONS s
LEFT JOIN GOVHITLDECISIONS d ON s.IDXHITLSUPERVISION = d.IDXHITLSUPERVISION;

-- ----------------------------------------------------------------------------
-- Vista: Dashboard de métricas HITL
-- ----------------------------------------------------------------------------
-- Vista agregada para el dashboard principal con todas las métricas necesarias.
-- ----------------------------------------------------------------------------

CREATE OR REPLACE VIEW v_hitl_dashboard_metrics AS
SELECT
    COUNT(DISTINCT s.IDXHITLSUPERVISION) AS total_interventions,
    COUNT(DISTINCT CASE WHEN d.IDXHITLDECISION IS NULL THEN s.IDXHITLSUPERVISION END) AS pending_interventions,
    COUNT(DISTINCT d.IDXHITLDECISION) AS total_decisions,
    AVG(d.HITLRESPONSETIME::NUMERIC / 60.0) AS average_response_time_hours,
    COALESCE(
        SUM(CASE WHEN d.HITLDECISION = 'APPROVED' THEN 1 ELSE 0 END)::NUMERIC /
        NULLIF(COUNT(d.IDXHITLDECISION), 0),
        0
    ) AS approval_rate,
    COALESCE(
        SUM(CASE
            WHEN d.HITLRESPONSETIME IS NOT NULL
                 AND s.HITLSLA IS NOT NULL
                 AND d.HITLRESPONSETIME <= (s.HITLSLA * 60)
            THEN 1
            ELSE 0
        END)::NUMERIC /
        NULLIF(COUNT(d.IDXHITLDECISION), 0),
        0
    ) AS sla_compliance_rate,
    COUNT(DISTINCT CASE WHEN s.HITLENTITYTYPE = 'AGENT' THEN s.IDXHITLSUPERVISION END) AS agent_interventions,
    COUNT(DISTINCT CASE WHEN s.HITLENTITYTYPE = 'MODEL' THEN s.IDXHITLSUPERVISION END) AS model_interventions,
    COUNT(DISTINCT CASE WHEN s.HITLENTITYTYPE = 'PROMPT' THEN s.IDXHITLSUPERVISION END) AS prompt_interventions,
    COUNT(DISTINCT CASE WHEN d.HITLDECISION = 'APPROVED' THEN d.IDXHITLDECISION END) AS approved_count,
    COUNT(DISTINCT CASE WHEN d.HITLDECISION = 'REJECTED' THEN d.IDXHITLDECISION END) AS rejected_count,
    COUNT(DISTINCT CASE WHEN d.HITLDECISION = 'MODIFIED' THEN d.IDXHITLDECISION END) AS modified_count
FROM GOVHITLSUPERVISIONS s
LEFT JOIN GOVHITLDECISIONS d ON s.IDXHITLSUPERVISION = d.IDXHITLSUPERVISION;

-- ============================================================================
-- 4. PROCEDIMIENTOS ALMACENADOS (STORED PROCEDURES)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Procedimiento: Limpiar decisiones antiguas
-- ----------------------------------------------------------------------------
-- Procedimiento para limpiar decisiones antiguas (más de N días) manteniendo
-- solo las estadísticas agregadas. Útil para mantenimiento de base de datos.
-- ----------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE cleanup_old_hitl_decisions(p_days_to_keep INTEGER DEFAULT 365)
LANGUAGE plpgsql
AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    -- Eliminar decisiones más antiguas que p_days_to_keep días
    DELETE FROM GOVHITLDECISIONS
    WHERE HITLDECISIONDATE < CURRENT_TIMESTAMP - (p_days_to_keep || ' days')::INTERVAL;

    GET DIAGNOSTICS deleted_count = ROW_COUNT;

    RAISE NOTICE 'Eliminadas % decisiones HITL anteriores a % días', deleted_count, p_days_to_keep;
END;
$$;

-- ----------------------------------------------------------------------------
-- Procedimiento: Actualizar métricas de cumplimiento de SLA
-- ----------------------------------------------------------------------------
-- Procedimiento para recalcular y actualizar métricas de cumplimiento de SLA
-- en la configuración de supervisión (campo HITLCONFIGURATION JSONB).
-- ----------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE update_sla_compliance_metrics()
LANGUAGE plpgsql
AS $$
DECLARE
    supervision_record RECORD;
    total_decisions INTEGER;
    compliant_decisions INTEGER;
    compliance_rate NUMERIC;
    config_json JSONB;
BEGIN
    -- Iterar sobre todas las supervisiones con decisiones
    FOR supervision_record IN
        SELECT DISTINCT s.IDXHITLSUPERVISION, s.HITLCONFIGURATION
        FROM GOVHITLSUPERVISIONS s
        JOIN GOVHITLDECISIONS d ON s.IDXHITLSUPERVISION = d.IDXHITLSUPERVISION
    LOOP
        -- Calcular métricas
        SELECT
            COUNT(*),
            SUM(CASE
                WHEN d.HITLRESPONSETIME IS NOT NULL
                     AND s.HITLSLA IS NOT NULL
                     AND d.HITLRESPONSETIME <= (s.HITLSLA * 60)
                THEN 1
                ELSE 0
            END)
        INTO total_decisions, compliant_decisions
        FROM GOVHITLDECISIONS d
        JOIN GOVHITLSUPERVISIONS s ON d.IDXHITLSUPERVISION = s.IDXHITLSUPERVISION
        WHERE s.IDXHITLSUPERVISION = supervision_record.IDXHITLSUPERVISION;

        -- Calcular tasa de cumplimiento
        compliance_rate := CASE
            WHEN total_decisions > 0 THEN compliant_decisions::NUMERIC / total_decisions
            ELSE 0
        END;

        -- Actualizar configuración JSONB
        config_json := COALESCE(supervision_record.HITLCONFIGURATION::JSONB, '{}'::JSONB);
        config_json := config_json || jsonb_build_object(
            'slaComplianceRate', compliance_rate,
            'totalDecisions', total_decisions,
            'compliantDecisions', compliant_decisions,
            'lastUpdated', CURRENT_TIMESTAMP
        );

        -- Actualizar registro
        UPDATE GOVHITLSUPERVISIONS
        SET HITLCONFIGURATION = config_json::TEXT
        WHERE IDXHITLSUPERVISION = supervision_record.IDXHITLSUPERVISION;
    END LOOP;

    RAISE NOTICE 'Métricas de cumplimiento de SLA actualizadas';
END;
$$;

-- ============================================================================
-- 5. ÍNDICES ADICIONALES (si se necesitan índices especiales)
-- ============================================================================
-- NOTA: Los índices básicos están definidos en las anotaciones JPA.
-- Estos índices adicionales son para casos especiales de optimización.

-- Índice GIN para búsquedas en JSONB (si se usan consultas JSONB frecuentes)
-- CREATE INDEX IF NOT EXISTS idx_hitl_supervision_config_gin
-- ON GOVHITLSUPERVISIONS USING GIN (HITLCONFIGURATION::JSONB);

-- Índice para búsquedas de texto completo en razón de decisión (si se necesita)
-- CREATE INDEX IF NOT EXISTS idx_hitl_decision_reason_fts
-- ON GOVHITLDECISIONS USING GIN (to_tsvector('spanish', HITLDECISIONREASON));

-- ============================================================================
-- FIN DE SCRIPTS SQL
-- ============================================================================

