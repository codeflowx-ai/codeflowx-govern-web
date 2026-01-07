-- ============================================================================
-- TRIGGERS PARA TABLA GOVCONFORMITYDECLARATIONS
-- ============================================================================
-- Módulo: Conformity Declaration (Declaración de Conformidad)
-- Fecha: Enero 2025
-- ============================================================================

-- ============================================================================
-- TRIGGER: Actualizar UPDATED_AT automáticamente
-- ============================================================================
-- Actualiza el campo UPDATED_AT cada vez que se modifica un registro
-- ============================================================================

CREATE OR REPLACE FUNCTION update_conformity_declaration_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.UPDATED_AT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_conformity_declaration_updated_at
    BEFORE UPDATE ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    EXECUTE FUNCTION update_conformity_declaration_updated_at();

-- ============================================================================
-- TRIGGER: Validar transición de estados
-- ============================================================================
-- Valida que las transiciones de estado sean válidas:
-- - DRAFT -> SIGNED (válido)
-- - SIGNED -> PUBLISHED (válido, futuro)
-- - PUBLISHED -> REVOKED (válido, futuro)
-- - Cualquier otra transición -> ERROR
-- ============================================================================

CREATE OR REPLACE FUNCTION validate_conformity_declaration_status_transition()
RETURNS TRIGGER AS $$
BEGIN
    -- Si el estado no cambió, permitir
    IF OLD.STATUS = NEW.STATUS THEN
        RETURN NEW;
    END IF;

    -- Validar transiciones permitidas
    IF OLD.STATUS = 'DRAFT' AND NEW.STATUS = 'SIGNED' THEN
        -- DRAFT -> SIGNED: Requiere signedBy y signatureDate
        IF NEW.SIGNED_BY IS NULL OR NEW.SIGNED_BY = '' THEN
            RAISE EXCEPTION 'Cannot sign declaration without signedBy';
        END IF;
        IF NEW.SIGNATURE_DATE IS NULL THEN
            NEW.SIGNATURE_DATE = CURRENT_TIMESTAMP;
        END IF;
        RETURN NEW;
    END IF;

    IF OLD.STATUS = 'SIGNED' AND NEW.STATUS = 'PUBLISHED' THEN
        -- SIGNED -> PUBLISHED: Permitido (futuro)
        RETURN NEW;
    END IF;

    IF OLD.STATUS = 'PUBLISHED' AND NEW.STATUS = 'REVOKED' THEN
        -- PUBLISHED -> REVOKED: Permitido (futuro)
        RETURN NEW;
    END IF;

    -- Cualquier otra transición no está permitida
    RAISE EXCEPTION 'Invalid status transition from % to %', OLD.STATUS, NEW.STATUS;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_declaration_status_transition
    BEFORE UPDATE ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    WHEN (OLD.STATUS IS DISTINCT FROM NEW.STATUS)
    EXECUTE FUNCTION validate_conformity_declaration_status_transition();

-- ============================================================================
-- TRIGGER: Generar versión automáticamente si no se proporciona
-- ============================================================================
-- Genera versión automática basada en el número de declaraciones del proyecto
-- ============================================================================

CREATE OR REPLACE FUNCTION generate_conformity_declaration_version()
RETURNS TRIGGER AS $$
DECLARE
    v_project_id BIGINT;
    v_declaration_count BIGINT;
    v_version VARCHAR(50);
BEGIN
    -- Si ya tiene versión, no hacer nada
    IF NEW.AI_SYSTEM_VERSION IS NOT NULL AND NEW.AI_SYSTEM_VERSION != '' THEN
        RETURN NEW;
    END IF;

    -- Obtener project_id desde assessment
    SELECT ca.IDXPROJECT INTO v_project_id
    FROM COMPLIANCEASSESSMENTS ca
    WHERE ca.IDXCOMPLIANCEASSESSMENT = NEW.ASSESSMENT_ID;

    IF v_project_id IS NULL THEN
        RAISE EXCEPTION 'Assessment not found: %', NEW.ASSESSMENT_ID;
    END IF;

    -- Contar declaraciones existentes del proyecto
    SELECT COUNT(*) INTO v_declaration_count
    FROM GOVCONFORMITYDECLARATIONS cd
    JOIN COMPLIANCEASSESSMENTS ca ON ca.IDXCOMPLIANCEASSESSMENT = cd.ASSESSMENT_ID
    WHERE ca.IDXPROJECT = v_project_id;

    -- Generar versión: v1.{count + 1}
    v_version := 'v1.' || (v_declaration_count + 1);
    NEW.AI_SYSTEM_VERSION := v_version;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_generate_declaration_version
    BEFORE INSERT ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    WHEN (NEW.AI_SYSTEM_VERSION IS NULL OR NEW.AI_SYSTEM_VERSION = '')
    EXECUTE FUNCTION generate_conformity_declaration_version();

-- ============================================================================
-- TRIGGER: Actualizar PDF_GENERATED_AT cuando se genera PDF
-- ============================================================================
-- Actualiza PDF_GENERATED_AT cuando se establece PDF_PATH
-- ============================================================================

CREATE OR REPLACE FUNCTION update_pdf_generated_at()
RETURNS TRIGGER AS $$
BEGIN
    -- Si PDF_PATH se establece y PDF_GENERATED_AT es NULL, establecer timestamp
    IF NEW.PDF_PATH IS NOT NULL AND NEW.PDF_PATH != '' AND
       (OLD.PDF_PATH IS NULL OR OLD.PDF_PATH = '' OR OLD.PDF_PATH != NEW.PDF_PATH) THEN
        NEW.PDF_GENERATED_AT := CURRENT_TIMESTAMP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_pdf_generated_at
    BEFORE INSERT OR UPDATE ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    EXECUTE FUNCTION update_pdf_generated_at();

-- ============================================================================
-- COMENTARIOS SOBRE TRIGGERS
-- ============================================================================
--
-- Estos triggers proporcionan:
-- 1. Actualización automática de timestamps
-- 2. Validación de reglas de negocio a nivel de BD
-- 3. Generación automática de valores (versiones)
-- 4. Integridad referencial adicional
--
-- NOTA: Los triggers se ejecutan ANTES de las validaciones de JPA,
-- por lo que proporcionan una capa adicional de seguridad.
--
-- ============================================================================
