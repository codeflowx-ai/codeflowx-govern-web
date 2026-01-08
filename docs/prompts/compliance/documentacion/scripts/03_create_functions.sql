-- ============================================================================
-- SCRIPT DE CREACIÓN DE FUNCIONES
-- Módulo: Technical Documentation
-- Tabla: GOVAIACTTECHNICALDOCS
-- Fecha: Diciembre 2025
-- ============================================================================

-- Este script crea funciones útiles para operaciones sobre la tabla de documentación técnica

-- ============================================================================
-- FUNCIÓN: Calcular score de completitud de documentación
-- ============================================================================

CREATE OR REPLACE FUNCTION calculate_techdocs_completeness_score(p_doc_id BIGINT)
RETURNS NUMERIC(5,4) AS $$
DECLARE
    v_completed_sections INTEGER := 0;
    v_total_sections INTEGER := 11;
    v_score NUMERIC(5,4);
    v_doc RECORD;
BEGIN
    -- Obtener documentación
    SELECT * INTO v_doc
    FROM GOVAIACTTECHNICALDOCS
    WHERE IDX_TECHNICAL_DOC = p_doc_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Documentación con ID % no encontrada', p_doc_id;
    END IF;

    -- Contar secciones completas (más de 50 caracteres)
    IF v_doc.SYSTEM_DESCRIPTION IS NOT NULL AND LENGTH(TRIM(v_doc.SYSTEM_DESCRIPTION)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF v_doc.DEVELOPMENT_PROCESS IS NOT NULL AND LENGTH(TRIM(v_doc.DEVELOPMENT_PROCESS)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF v_doc.DATA_GOVERNANCE IS NOT NULL AND LENGTH(TRIM(v_doc.DATA_GOVERNANCE)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF v_doc.RISK_MANAGEMENT IS NOT NULL AND LENGTH(TRIM(v_doc.RISK_MANAGEMENT)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF v_doc.HUMAN_OVERSIGHT IS NOT NULL AND LENGTH(TRIM(v_doc.HUMAN_OVERSIGHT)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF v_doc.ACCURACY_ROBUSTNESS IS NOT NULL AND LENGTH(TRIM(v_doc.ACCURACY_ROBUSTNESS)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF v_doc.CYBERSECURITY_MEASURES IS NOT NULL AND LENGTH(TRIM(v_doc.CYBERSECURITY_MEASURES)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF (v_doc.VALIDATION_PROCEDURES IS NOT NULL AND LENGTH(TRIM(v_doc.VALIDATION_PROCEDURES)) > 50) OR
       (v_doc.TESTING_PROCEDURES IS NOT NULL AND LENGTH(TRIM(v_doc.TESTING_PROCEDURES)) > 50) THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    IF v_doc.MONITORING_MEASURES IS NOT NULL AND LENGTH(TRIM(v_doc.MONITORING_MEASURES)) > 50 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    -- CONFORMITY_ASSESSMENT: derivado de STATUS
    IF v_doc.STATUS = 'APPROVED' THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    -- RECORD_KEEPING: derivado de PDF_PATH
    IF v_doc.PDF_PATH IS NOT NULL AND LENGTH(TRIM(v_doc.PDF_PATH)) > 0 THEN
        v_completed_sections := v_completed_sections + 1;
    END IF;

    -- Calcular score (0.00 - 1.00)
    v_score := ROUND((v_completed_sections::NUMERIC / v_total_sections::NUMERIC)::NUMERIC, 4);

    RETURN v_score;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION calculate_techdocs_completeness_score(BIGINT) IS
'Calcula el score de completitud de una documentación técnica (0.00 - 1.00) basado en las 11 secciones del Anexo IV';

-- ============================================================================
-- FUNCIÓN: Validar si documentación está completa
-- ============================================================================

CREATE OR REPLACE FUNCTION is_techdocs_complete(p_doc_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    v_score NUMERIC(5,4);
BEGIN
    v_score := calculate_techdocs_completeness_score(p_doc_id);
    RETURN v_score >= 1.00;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION is_techdocs_complete(BIGINT) IS
'Retorna TRUE si la documentación técnica está 100% completa (score = 1.00)';

-- ============================================================================
-- FUNCIÓN: Obtener documentación por tipo e ID de entidad
-- ============================================================================

CREATE OR REPLACE FUNCTION get_techdocs_by_entity(
    p_entity_type VARCHAR(50),
    p_entity_id BIGINT
)
RETURNS TABLE (
    doc_id BIGINT,
    system_name VARCHAR(200),
    version VARCHAR(50),
    status VARCHAR(50),
    completeness_score NUMERIC(5,4),
    is_complete BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        d.IDX_TECHNICAL_DOC,
        d.SYSTEM_NAME,
        d.VERSION,
        d.STATUS,
        calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) AS completeness_score,
        is_techdocs_complete(d.IDX_TECHNICAL_DOC) AS is_complete,
        d.CREATED_AT,
        d.UPDATED_AT
    FROM GOVAIACTTECHNICALDOCS d
    WHERE d.ENTITY_TYPE = p_entity_type
      AND d.ENTITY_ID = p_entity_id;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION get_techdocs_by_entity(VARCHAR, BIGINT) IS
'Retorna información resumida de documentación técnica por tipo e ID de entidad';

-- ============================================================================
-- FUNCIÓN: Obtener lista de documentaciones incompletas
-- ============================================================================

CREATE OR REPLACE FUNCTION get_incomplete_techdocs(
    p_entity_type VARCHAR(50) DEFAULT NULL,
    p_min_score NUMERIC(5,4) DEFAULT 0.0
)
RETURNS TABLE (
    doc_id BIGINT,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    system_name VARCHAR(200),
    completeness_score NUMERIC(5,4),
    missing_sections INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        d.IDX_TECHNICAL_DOC,
        d.ENTITY_TYPE,
        d.ENTITY_ID,
        d.SYSTEM_NAME,
        calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) AS completeness_score,
        (11 - FLOOR(calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) * 11))::INTEGER AS missing_sections
    FROM GOVAIACTTECHNICALDOCS d
    WHERE (p_entity_type IS NULL OR d.ENTITY_TYPE = p_entity_type)
      AND calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) < 1.00
      AND calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) >= p_min_score
    ORDER BY completeness_score ASC, d.UPDATED_AT DESC;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION get_incomplete_techdocs(VARCHAR, NUMERIC) IS
'Retorna lista de documentaciones incompletas, opcionalmente filtradas por tipo de entidad y score mínimo';

-- ============================================================================
-- VERIFICACIÓN
-- ============================================================================

-- Consulta para verificar funciones creadas
-- SELECT
--     routine_name,
--     routine_type,
--     data_type AS return_type
-- FROM information_schema.routines
-- WHERE routine_schema = 'public'
--   AND routine_name LIKE '%techdocs%'
-- ORDER BY routine_name;

