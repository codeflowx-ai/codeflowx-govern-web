-- ============================================================================
-- PROCEDIMIENTOS ALMACENADOS PARA MÓDULO CONFORMITY DECLARATION
-- ============================================================================
-- Módulo: Conformity Declaration (Declaración de Conformidad)
-- Fecha: Enero 2025
-- ============================================================================

-- ============================================================================
-- PROCEDIMIENTO: Firmar declaración
-- ============================================================================
-- Procedimiento para firmar una declaración de conformidad
-- Valida requisitos y actualiza estado
-- ============================================================================

CREATE OR REPLACE PROCEDURE sign_conformity_declaration(
    p_declaration_id BIGINT,
    p_signed_by VARCHAR(200),
    p_digital_signature TEXT DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_status VARCHAR(50);
    v_assessment_ready BOOLEAN;
BEGIN
    -- Obtener estado actual
    SELECT STATUS INTO v_current_status
    FROM GOVCONFORMITYDECLARATIONS
    WHERE IDX_DECLARATION = p_declaration_id;

    IF v_current_status IS NULL THEN
        RAISE EXCEPTION 'Declaration not found: %', p_declaration_id;
    END IF;

    IF v_current_status != 'DRAFT' THEN
        RAISE EXCEPTION 'Declaration must be in DRAFT status to be signed. Current status: %', v_current_status;
    END IF;

    -- Validar que el assessment esté listo
    SELECT is_assessment_ready_for_declaration(ASSESSMENT_ID) INTO v_assessment_ready
    FROM GOVCONFORMITYDECLARATIONS
    WHERE IDX_DECLARATION = p_declaration_id;

    IF NOT v_assessment_ready THEN
        RAISE EXCEPTION 'Assessment is not ready for certification';
    END IF;

    -- Actualizar declaración
    UPDATE GOVCONFORMITYDECLARATIONS
    SET
        STATUS = 'SIGNED',
        SIGNED_BY = p_signed_by,
        SIGNATURE_DATE = CURRENT_TIMESTAMP,
        DIGITAL_SIGNATURE = COALESCE(p_digital_signature, DIGITAL_SIGNATURE),
        UPDATED_AT = CURRENT_TIMESTAMP
    WHERE IDX_DECLARATION = p_declaration_id;

    COMMIT;
END;
$$;

-- ============================================================================
-- PROCEDIMIENTO: Revocar declaración
-- ============================================================================
-- Procedimiento para revocar una declaración publicada
-- ============================================================================

CREATE OR REPLACE PROCEDURE revoke_conformity_declaration(
    p_declaration_id BIGINT,
    p_revocation_reason TEXT DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_status VARCHAR(50);
BEGIN
    -- Obtener estado actual
    SELECT STATUS INTO v_current_status
    FROM GOVCONFORMITYDECLARATIONS
    WHERE IDX_DECLARATION = p_declaration_id;

    IF v_current_status IS NULL THEN
        RAISE EXCEPTION 'Declaration not found: %', p_declaration_id;
    END IF;

    IF v_current_status != 'PUBLISHED' THEN
        RAISE EXCEPTION 'Only PUBLISHED declarations can be revoked. Current status: %', v_current_status;
    END IF;

    -- Actualizar declaración
    UPDATE GOVCONFORMITYDECLARATIONS
    SET
        STATUS = 'REVOKED',
        UPDATED_AT = CURRENT_TIMESTAMP
    WHERE IDX_DECLARATION = p_declaration_id;

    -- TODO: Si se requiere, registrar razón de revocación en tabla de auditoría
    -- INSERT INTO DECLARATION_AUDIT_LOG (declaration_id, action, reason, created_at)
    -- VALUES (p_declaration_id, 'REVOKED', p_revocation_reason, CURRENT_TIMESTAMP);

    COMMIT;
END;
$$;

-- ============================================================================
-- PROCEDIMIENTO: Publicar declaración
-- ============================================================================
-- Procedimiento para publicar una declaración firmada
-- ============================================================================

CREATE OR REPLACE PROCEDURE publish_conformity_declaration(
    p_declaration_id BIGINT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_status VARCHAR(50);
    v_pdf_path VARCHAR(500);
BEGIN
    -- Obtener estado actual y PDF path
    SELECT STATUS, PDF_PATH INTO v_current_status, v_pdf_path
    FROM GOVCONFORMITYDECLARATIONS
    WHERE IDX_DECLARATION = p_declaration_id;

    IF v_current_status IS NULL THEN
        RAISE EXCEPTION 'Declaration not found: %', p_declaration_id;
    END IF;

    IF v_current_status != 'SIGNED' THEN
        RAISE EXCEPTION 'Declaration must be SIGNED to be published. Current status: %', v_current_status;
    END IF;

    IF v_pdf_path IS NULL OR v_pdf_path = '' THEN
        RAISE EXCEPTION 'PDF must be generated before publishing declaration';
    END IF;

    -- Actualizar declaración
    UPDATE GOVCONFORMITYDECLARATIONS
    SET
        STATUS = 'PUBLISHED',
        UPDATED_AT = CURRENT_TIMESTAMP
    WHERE IDX_DECLARATION = p_declaration_id;

    COMMIT;
END;
$$;

-- ============================================================================
-- PROCEDIMIENTO: Limpiar declaraciones antiguas (archivado)
-- ============================================================================
-- Procedimiento para archivar declaraciones antiguas (más de N días)
-- ============================================================================

CREATE OR REPLACE PROCEDURE archive_old_declarations(
    p_days_old INTEGER DEFAULT 365,
    p_status_filter VARCHAR(50) DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_archived_count INTEGER;
BEGIN
    -- Archivar declaraciones antiguas
    -- NOTA: Este procedimiento puede mover registros a una tabla de archivo
    -- o simplemente marcarlos. Por ahora, solo actualiza un campo de auditoría.

    UPDATE GOVCONFORMITYDECLARATIONS
    SET
        UPDATED_AT = CURRENT_TIMESTAMP
    WHERE CREATED_AT < CURRENT_TIMESTAMP - (p_days_old || ' days')::INTERVAL
      AND (p_status_filter IS NULL OR STATUS = p_status_filter);

    GET DIAGNOSTICS v_archived_count = ROW_COUNT;

    RAISE NOTICE 'Archived % declarations older than % days', v_archived_count, p_days_old;

    COMMIT;
END;
$$;

-- ============================================================================
-- COMENTARIOS SOBRE PROCEDIMIENTOS
-- ============================================================================
--
-- Estos procedimientos proporcionan:
-- 1. Lógica de negocio centralizada en la BD
-- 2. Transacciones atómicas complejas
-- 3. Validaciones a nivel de BD
-- 4. Facilidad para operaciones batch
--
-- NOTA: Los procedimientos pueden ser llamados desde JPA usando
-- @Procedure annotation o EntityManager.createStoredProcedureQuery()
--
-- ============================================================================
