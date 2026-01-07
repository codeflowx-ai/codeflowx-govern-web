-- ============================================================================
-- SCRIPT DE CREACIÓN DE PROCEDIMIENTOS ALMACENADOS PARA FRIA
-- ============================================================================
-- Módulo: FRIA (Fundamental Rights Impact Assessment)
-- Tabla: FRIAFUNDAMENTALRIGHTSASSESSMENTS
-- Fecha: Diciembre 2025
-- Descripción: Procedimientos almacenados para operaciones complejas
-- ============================================================================

-- ============================================================================
-- PROCEDIMIENTO: Actualizar score de completitud automáticamente
-- ============================================================================
-- Descripción: Actualiza el FRIACOMPLETENESSCORE de una FRIA basándose
--              en los elementos completados
-- Parámetros: ID de la evaluación FRIA
-- ============================================================================

CREATE OR REPLACE PROCEDURE update_fria_completeness_score(
    p_fria_id BIGINT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_score DECIMAL(5,2);
BEGIN
    -- Calcular score usando la función
    v_score := calculate_fria_completeness_score(p_fria_id);

    -- Actualizar el score
    UPDATE FRIAFUNDAMENTALRIGHTSASSESSMENTS
    SET FRIACOMPLETENESSCORE = v_score,
        FRIAUPDATEDAT = CURRENT_TIMESTAMP
    WHERE IDXFRIAASSESSMENT = p_fria_id;

    -- Actualizar compliance si el score es 1.00
    IF v_score >= 1.00 THEN
        UPDATE FRIAFUNDAMENTALRIGHTSASSESSMENTS
        SET FRIAART27COMPLIANT = TRUE
        WHERE IDXFRIAASSESSMENT = p_fria_id;
    ELSE
        UPDATE FRIAFUNDAMENTALRIGHTSASSESSMENTS
        SET FRIAART27COMPLIANT = FALSE
        WHERE IDXFRIAASSESSMENT = p_fria_id;
    END IF;

    COMMIT;
END;
$$;

-- ============================================================================
-- PROCEDIMIENTO: Notificar a autoridades
-- ============================================================================
-- Descripción: Marca una FRIA como notificada y registra la información
--              de notificación
-- Parámetros: ID de la FRIA, ID de notificación, detalles
-- ============================================================================

CREATE OR REPLACE PROCEDURE notify_fria_authority(
    p_fria_id BIGINT,
    p_notification_id VARCHAR(100),
    p_notification_details TEXT DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_completeness_score DECIMAL(5,2);
    v_final_risk DECIMAL(5,4);
BEGIN
    -- Validar que la FRIA existe
    IF NOT EXISTS (SELECT 1 FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS WHERE IDXFRIAASSESSMENT = p_fria_id) THEN
        RAISE EXCEPTION 'FRIA con ID % no encontrada', p_fria_id;
    END IF;

    -- Obtener scores
    SELECT FRIACOMPLETENESSCORE, FRIAFINALRISK
    INTO v_completeness_score, v_final_risk
    FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
    WHERE IDXFRIAASSESSMENT = p_fria_id;

    -- Validar que la FRIA está completa
    IF v_completeness_score IS NULL OR v_completeness_score < 1.00 THEN
        RAISE EXCEPTION 'FRIA debe estar completa (score >= 1.00) para notificar a autoridades';
    END IF;

    -- Validar que el riesgo final es alto (>= 0.75) según Art. 27.3
    IF v_final_risk IS NULL OR v_final_risk < 0.75 THEN
        RAISE EXCEPTION 'FRIA debe tener riesgo final >= 0.75 para notificar a autoridades (Art. 27.3)';
    END IF;

    -- Actualizar estado de notificación
    UPDATE FRIAFUNDAMENTALRIGHTSASSESSMENTS
    SET FRIANOTIFIED = TRUE,
        FRIANOTIFICATIONID = p_notification_id,
        FRIANOTIFICATIONDATE = CURRENT_TIMESTAMP,
        FRIAUPDATEDAT = CURRENT_TIMESTAMP
    WHERE IDXFRIAASSESSMENT = p_fria_id;

    COMMIT;
END;
$$;

-- ============================================================================
-- PROCEDIMIENTO: Aprobar FRIA
-- ============================================================================
-- Descripción: Marca una FRIA como aprobada y registra la información
--              de aprobación
-- Parámetros: ID de la FRIA, ID del usuario que aprueba
-- ============================================================================

CREATE OR REPLACE PROCEDURE approve_fria(
    p_fria_id BIGINT,
    p_approved_by BIGINT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_completeness_score DECIMAL(5,2);
BEGIN
    -- Validar que la FRIA existe
    IF NOT EXISTS (SELECT 1 FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS WHERE IDXFRIAASSESSMENT = p_fria_id) THEN
        RAISE EXCEPTION 'FRIA con ID % no encontrada', p_fria_id;
    END IF;

    -- Obtener score de completitud
    SELECT FRIACOMPLETENESSCORE
    INTO v_completeness_score
    FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
    WHERE IDXFRIAASSESSMENT = p_fria_id;

    -- Validar que la FRIA está completa
    IF v_completeness_score IS NULL OR v_completeness_score < 1.00 THEN
        RAISE EXCEPTION 'FRIA debe estar completa (score >= 1.00) para ser aprobada';
    END IF;

    -- Actualizar estado de aprobación
    UPDATE FRIAFUNDAMENTALRIGHTSASSESSMENTS
    SET FRIAAPPROVED = TRUE,
        FRIAAPPROVEDBY = p_approved_by,
        FRIAAPPROVALDATE = CURRENT_TIMESTAMP,
        FRIAUPDATEDAT = CURRENT_TIMESTAMP
    WHERE IDXFRIAASSESSMENT = p_fria_id;

    COMMIT;
END;
$$;

-- ============================================================================
-- PROCEDIMIENTO: Crear nueva versión de FRIA
-- ============================================================================
-- Descripción: Crea una nueva evaluación FRIA basada en una existente
--              (copiando datos pero con nuevo UUID y fecha)
-- Parámetros: ID de la FRIA base, ID del proyecto, ID del usuario
-- Retorna: ID de la nueva FRIA creada
-- ============================================================================

CREATE OR REPLACE PROCEDURE create_fria_version(
    p_base_fria_id BIGINT,
    p_project_id BIGINT,
    p_user_id BIGINT,
    OUT p_new_fria_id BIGINT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_base_fria RECORD;
    v_new_uuid VARCHAR(36);
BEGIN
    -- Validar que la FRIA base existe
    IF NOT EXISTS (SELECT 1 FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS WHERE IDXFRIAASSESSMENT = p_base_fria_id) THEN
        RAISE EXCEPTION 'FRIA base con ID % no encontrada', p_base_fria_id;
    END IF;

    -- Obtener datos de la FRIA base
    SELECT *
    INTO v_base_fria
    FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS
    WHERE IDXFRIAASSESSMENT = p_base_fria_id;

    -- Generar nuevo UUID
    v_new_uuid := gen_random_uuid()::VARCHAR;

    -- Crear nueva FRIA copiando datos de la base
    INSERT INTO FRIAFUNDAMENTALRIGHTSASSESSMENTS (
        iduuid,
        IDXPROJECT,
        IDXUSER,
        FRIAPROCESSDESCRIPTION,
        FRIAUSAGEPERIOD,
        FRIAUSAGEFREQUENCY,
        FRIAAFFECTEDCATEGORIES,
        FRIAVULNERABLEGROUPSINCLUDED,
        FRIARISKS,
        FRIAHUMANOVERSIGHT,
        FRIAHITLENABLED,
        FRIAMITIGATIONMEASURES,
        FRIAART27COMPLIANT,
        FRIACOMPLETENESSCORE,
        FRIAQUALSCORE,
        FRIAFINALRISK,
        FRIACROSSVALIDATIONRESULT,
        FRIACHARTERARTICLES,
        FRIAIMPACTSEVERITY,
        FRIANOTIFIED,
        FRIANOTIFICATIONID,
        FRIANOTIFICATIONDATE,
        FRIADOCUMENTURL,
        FRIADOCUMENTVERSION,
        FRIADPIAINTEGRATED,
        FRIADPIAID,
        FRIAAPPROVED,
        FRIAAPPROVEDBY,
        FRIAAPPROVALDATE,
        FRIACREATEDAT,
        FRIAUPDATEDAT
    ) VALUES (
        v_new_uuid,
        p_project_id,
        p_user_id,
        v_base_fria.FRIAPROCESSDESCRIPTION,
        v_base_fria.FRIAUSAGEPERIOD,
        v_base_fria.FRIAUSAGEFREQUENCY,
        v_base_fria.FRIAAFFECTEDCATEGORIES,
        v_base_fria.FRIAVULNERABLEGROUPSINCLUDED,
        v_base_fria.FRIARISKS,
        v_base_fria.FRIAHUMANOVERSIGHT,
        v_base_fria.FRIAHITLENABLED,
        v_base_fria.FRIAMITIGATIONMEASURES,
        FALSE, -- Nueva versión empieza como no compliant
        NULL,  -- Score se recalcula
        NULL,  -- Quality score se recalcula
        NULL,  -- Final risk se recalcula
        NULL,  -- Cross validation se rehace
        v_base_fria.FRIACHARTERARTICLES,
        NULL,  -- Impact severity se recalcula
        FALSE, -- No notificada
        NULL,  -- Sin notification ID
        NULL,  -- Sin notification date
        NULL,  -- Sin document URL
        NULL,  -- Sin document version
        v_base_fria.FRIADPIAINTEGRATED,
        v_base_fria.FRIADPIAID,
        FALSE, -- No aprobada
        NULL,  -- Sin approved by
        NULL,  -- Sin approval date
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    )
    RETURNING IDXFRIAASSESSMENT INTO p_new_fria_id;

    COMMIT;
END;
$$;

-- ============================================================================
-- COMENTARIOS SOBRE PROCEDIMIENTOS ALMACENADOS
-- ============================================================================
--
-- 1. Los procedimientos almacenados pueden ser llamados desde código Java
--    usando StoredProcedureQuery o @Procedure.
--
-- 2. Los procedimientos encapsulan lógica de negocio compleja que puede
--    ejecutarse directamente en la base de datos, mejorando el rendimiento.
--
-- 3. Los procedimientos pueden usar transacciones internas (COMMIT) o
--    dejar que la aplicación gestione las transacciones.
--
-- 4. Se recomienda documentar los procedimientos y mantenerlos sincronizados
--    con la lógica de negocio en el código Java.
--
-- ============================================================================

