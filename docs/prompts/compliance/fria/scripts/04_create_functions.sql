-- ============================================================================
-- SCRIPT DE CREACIÓN DE FUNCIONES PARA FRIA
-- ============================================================================
-- Módulo: FRIA (Fundamental Rights Impact Assessment)
-- Tabla: FRIAFUNDAMENTALRIGHTSASSESSMENTS
-- Fecha: Diciembre 2025
-- Descripción: Funciones para cálculos, validaciones y consultas complejas
-- ============================================================================

-- ============================================================================
-- FUNCIÓN: Calcular score de completitud
-- ============================================================================
-- Descripción: Calcula el score de completitud (0.00 - 1.00) basado en
--              los 6 elementos mandatorios del Art. 27.1
-- Parámetros: ID de la evaluación FRIA
-- Retorna: Score de completitud (DECIMAL)
-- ============================================================================

CREATE OR REPLACE FUNCTION calculate_fria_completeness_score(p_fria_id BIGINT)
RETURNS DECIMAL(5,2) AS $$
DECLARE
    v_completed_elements INTEGER := 0;
    v_total_elements INTEGER := 6;
    v_score DECIMAL(5,2);
    v_fria RECORD;
BEGIN
    -- Obtener datos de la FRIA
    SELECT
        FRIAPROCESSDESCRIPTION,
        FRIAUSAGEPERIOD,
        FRIAUSAGEFREQUENCY,
        FRIAAFFECTEDCATEGORIES,
        FRIARISKS,
        FRIAHUMANOVERSIGHT,
        FRIAMITIGATIONMEASURES
    INTO v_fria
    FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
    WHERE IDXFRIAASSESSMENT = p_fria_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'FRIA con ID % no encontrada', p_fria_id;
    END IF;

    -- Art. 27.1.a - Process description
    IF v_fria.FRIAPROCESSDESCRIPTION IS NOT NULL
       AND TRIM(v_fria.FRIAPROCESSDESCRIPTION) != '' THEN
        v_completed_elements := v_completed_elements + 1;
    END IF;

    -- Art. 27.1.b - Usage period and frequency
    IF v_fria.FRIAUSAGEPERIOD IS NOT NULL
       AND v_fria.FRIAUSAGEFREQUENCY IS NOT NULL THEN
        v_completed_elements := v_completed_elements + 1;
    END IF;

    -- Art. 27.1.c - Affected categories
    IF v_fria.FRIAAFFECTEDCATEGORIES IS NOT NULL
       AND TRIM(v_fria.FRIAAFFECTEDCATEGORIES) != '' THEN
        v_completed_elements := v_completed_elements + 1;
    END IF;

    -- Art. 27.1.d - Risks
    IF v_fria.FRIARISKS IS NOT NULL
       AND TRIM(v_fria.FRIARISKS) != '' THEN
        v_completed_elements := v_completed_elements + 1;
    END IF;

    -- Art. 27.1.e - Human oversight
    IF v_fria.FRIAHUMANOVERSIGHT IS NOT NULL
       AND TRIM(v_fria.FRIAHUMANOVERSIGHT) != '' THEN
        v_completed_elements := v_completed_elements + 1;
    END IF;

    -- Art. 27.1.f - Mitigation measures
    IF v_fria.FRIAMITIGATIONMEASURES IS NOT NULL
       AND TRIM(v_fria.FRIAMITIGATIONMEASURES) != '' THEN
        v_completed_elements := v_completed_elements + 1;
    END IF;

    -- Calcular score (0.00 - 1.00)
    v_score := ROUND((v_completed_elements::DECIMAL / v_total_elements::DECIMAL)::DECIMAL, 2);

    RETURN v_score;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Validar compliance Art. 27
-- ============================================================================
-- Descripción: Valida si una FRIA cumple con todos los requisitos del Art. 27
-- Parámetros: ID de la evaluación FRIA
-- Retorna: TRUE si es compliant, FALSE en caso contrario
-- ============================================================================

CREATE OR REPLACE FUNCTION validate_fria_art27_compliance(p_fria_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    v_completeness_score DECIMAL(5,2);
    v_is_compliant BOOLEAN := FALSE;
BEGIN
    -- Calcular score de completitud
    v_completeness_score := calculate_fria_completeness_score(p_fria_id);

    -- Es compliant si el score es 1.00 (100%)
    v_is_compliant := (v_completeness_score >= 1.00);

    RETURN v_is_compliant;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Obtener última FRIA de un proyecto
-- ============================================================================
-- Descripción: Retorna la última evaluación FRIA creada para un proyecto
-- Parámetros: ID del proyecto
-- Retorna: ID de la FRIA más reciente, o NULL si no existe
-- ============================================================================

CREATE OR REPLACE FUNCTION get_latest_fria_by_project(p_project_id BIGINT)
RETURNS BIGINT AS $$
DECLARE
    v_fria_id BIGINT;
BEGIN
    SELECT IDXFRIAASSESSMENT
    INTO v_fria_id
    FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
    WHERE IDXPROJECT = p_project_id
    ORDER BY FRIACREATEDAT DESC
    LIMIT 1;

    RETURN v_fria_id;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Contar FRIAs por proyecto
-- ============================================================================
-- Descripción: Cuenta el número total de evaluaciones FRIA para un proyecto
-- Parámetros: ID del proyecto
-- Retorna: Número total de FRIAs
-- ============================================================================

CREATE OR REPLACE FUNCTION count_frias_by_project(p_project_id BIGINT)
RETURNS INTEGER AS $$
DECLARE
    v_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
    WHERE IDXPROJECT = p_project_id;

    RETURN v_count;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Obtener estadísticas de FRIA por proyecto
-- ============================================================================
-- Descripción: Retorna estadísticas agregadas de FRIAs para un proyecto
-- Parámetros: ID del proyecto
-- Retorna: JSON con estadísticas
-- ============================================================================

CREATE OR REPLACE FUNCTION get_fria_statistics_by_project(p_project_id BIGINT)
RETURNS JSON AS $$
DECLARE
    v_stats JSON;
BEGIN
    SELECT json_build_object(
        'total_frias', COUNT(*),
        'notified_frias', COUNT(*) FILTER (WHERE FRIANOTIFIED = TRUE),
        'approved_frias', COUNT(*) FILTER (WHERE FRIAAPPROVED = TRUE),
        'compliant_frias', COUNT(*) FILTER (WHERE FRIAART27COMPLIANT = TRUE),
        'avg_completeness_score', ROUND(AVG(FRIACOMPLETENESSCORE)::DECIMAL, 2),
        'avg_final_risk', ROUND(AVG(FRIAFINALRISK)::DECIMAL, 4),
        'latest_fria_id', MAX(IDXFRIAASSESSMENT) FILTER (WHERE FRIACREATEDAT = (
            SELECT MAX(FRIACREATEDAT)
            FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
            WHERE IDXPROJECT = p_project_id
        ))
    )
    INTO v_stats
    FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
    WHERE IDXPROJECT = p_project_id;

    RETURN v_stats;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Verificar si proyecto puede crear nueva FRIA
-- ============================================================================
-- Descripción: Verifica si un proyecto puede crear una nueva evaluación FRIA
--              (por ejemplo, si no hay una FRIA activa en borrador)
-- Parámetros: ID del proyecto
-- Retorna: TRUE si puede crear, FALSE en caso contrario
-- ============================================================================

CREATE OR REPLACE FUNCTION can_create_new_fria(p_project_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    v_has_draft_fria BOOLEAN;
BEGIN
    -- Verificar si existe una FRIA en borrador (completeness < 1.00)
    SELECT EXISTS(
        SELECT 1
        FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
        WHERE IDXPROJECT = p_project_id
          AND (FRIACOMPLETENESSCORE IS NULL OR FRIACOMPLETENESSCORE < 1.00)
          AND (FRIANOTIFIED IS NULL OR FRIANOTIFIED = FALSE)
          AND (FRIAAPPROVED IS NULL OR FRIAAPPROVED = FALSE)
    ) INTO v_has_draft_fria;

    -- Puede crear nueva FRIA si no hay borrador activo
    RETURN NOT v_has_draft_fria;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- COMENTARIOS SOBRE FUNCIONES
-- ============================================================================
--
-- 1. Las funciones pueden ser llamadas desde código Java usando
--    @Query con nativeQuery = true, o mediante StoredProcedureQuery.
--
-- 2. Las funciones que retornan JSON son útiles para agregaciones
--    complejas que se pueden usar directamente en el frontend.
--
-- 3. Las funciones de validación pueden ser llamadas antes de
--    operaciones críticas para garantizar integridad de datos.
--
-- 4. Se recomienda usar índices en los campos utilizados en las
--    funciones para optimizar el rendimiento.
--
-- ============================================================================

