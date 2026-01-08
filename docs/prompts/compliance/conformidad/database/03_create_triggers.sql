-- ============================================================================
-- TRIGGERS PARA TABLA GOVCONFORMITYDECLARATIONS
-- ============================================================================
-- Descripción: Script para crear triggers de base de datos
-- Fecha: Enero 2025
-- Módulo: Conformity Declaration
-- ============================================================================

-- ============================================================================
-- TRIGGER 1: Actualizar UPDATED_AT automáticamente
-- ============================================================================
-- Propósito: Mantener UPDATED_AT actualizado automáticamente en cada UPDATE
-- ============================================================================

-- Función para actualizar UPDATED_AT
CREATE OR REPLACE FUNCTION update_conformity_declaration_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.UPDATED_AT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger que ejecuta la función antes de cada UPDATE
CREATE TRIGGER trg_conformity_declaration_updated_at
    BEFORE UPDATE ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    EXECUTE FUNCTION update_conformity_declaration_updated_at();

-- ============================================================================
-- TRIGGER 2: Validar transición de estados
-- ============================================================================
-- Propósito: Validar que las transiciones de estado sean válidas
-- Reglas: DRAFT -> SIGNED -> PUBLISHED (no se puede retroceder)
-- ============================================================================

-- Función para validar transición de estados
CREATE OR REPLACE FUNCTION validate_conformity_declaration_status_transition()
RETURNS TRIGGER AS $$
BEGIN
    -- Validar transiciones permitidas
    IF OLD.STATUS = 'SIGNED' AND NEW.STATUS = 'DRAFT' THEN
        RAISE EXCEPTION 'Cannot change status from SIGNED to DRAFT. Status transitions are: DRAFT -> SIGNED -> PUBLISHED';
    END IF;

    IF OLD.STATUS = 'PUBLISHED' AND NEW.STATUS IN ('DRAFT', 'SIGNED') THEN
        RAISE EXCEPTION 'Cannot change status from PUBLISHED to %', NEW.STATUS;
    END IF;

    IF OLD.STATUS = 'REVOKED' AND NEW.STATUS != 'REVOKED' THEN
        RAISE EXCEPTION 'Cannot change status from REVOKED. Revoked declarations cannot be modified.';
    END IF;

    -- Validar que SIGNED requiere SIGNATURE_DATE y SIGNED_BY
    IF NEW.STATUS = 'SIGNED' AND (NEW.SIGNATURE_DATE IS NULL OR NEW.SIGNED_BY IS NULL) THEN
        RAISE EXCEPTION 'SIGNED status requires SIGNATURE_DATE and SIGNED_BY to be set';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger que ejecuta la validación antes de cada UPDATE
CREATE TRIGGER trg_conformity_declaration_validate_status
    BEFORE UPDATE ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    WHEN (OLD.STATUS IS DISTINCT FROM NEW.STATUS)
    EXECUTE FUNCTION validate_conformity_declaration_status_transition();

-- ============================================================================
-- TRIGGER 3: Actualizar PDF_GENERATED_AT cuando se genera PDF
-- ============================================================================
-- Propósito: Registrar automáticamente la fecha cuando se genera un PDF
-- ============================================================================

-- Función para actualizar PDF_GENERATED_AT
CREATE OR REPLACE FUNCTION update_conformity_declaration_pdf_generated_at()
RETURNS TRIGGER AS $$
BEGIN
    -- Si PDF_PATH cambia de NULL a un valor, actualizar PDF_GENERATED_AT
    IF OLD.PDF_PATH IS NULL AND NEW.PDF_PATH IS NOT NULL THEN
        NEW.PDF_GENERATED_AT = CURRENT_TIMESTAMP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger que ejecuta la función antes de cada UPDATE
CREATE TRIGGER trg_conformity_declaration_pdf_generated_at
    BEFORE UPDATE ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    WHEN (OLD.PDF_PATH IS DISTINCT FROM NEW.PDF_PATH)
    EXECUTE FUNCTION update_conformity_declaration_pdf_generated_at();

-- ============================================================================
-- TRIGGER 4: Validar que assessment esté listo para certificación
-- ============================================================================
-- Propósito: Validar a nivel de BD que el assessment esté listo antes de crear declaración
-- ============================================================================

-- Función para validar assessment listo para certificación
CREATE OR REPLACE FUNCTION validate_assessment_ready_for_certification()
RETURNS TRIGGER AS $$
DECLARE
    assessment_ready BOOLEAN;
BEGIN
    -- Verificar que el assessment esté listo para certificación
    SELECT COMRDYFORCERTIFICATION INTO assessment_ready
    FROM COMPLIANCEASSESSMENTS
    WHERE IDXCOMPLIANCEASSESSMENT = NEW.ASSESSMENT_ID;

    IF assessment_ready IS NULL OR assessment_ready = FALSE THEN
        RAISE EXCEPTION 'Cannot create declaration. Assessment % is not ready for certification (COMRDYFORCERTIFICATION = %)',
            NEW.ASSESSMENT_ID, assessment_ready;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger que ejecuta la validación antes de cada INSERT
CREATE TRIGGER trg_conformity_declaration_validate_assessment
    BEFORE INSERT ON GOVCONFORMITYDECLARATIONS
    FOR EACH ROW
    EXECUTE FUNCTION validate_assessment_ready_for_certification();

-- ============================================================================
-- COMENTARIOS SOBRE TRIGGERS
-- ============================================================================
--
-- Estos triggers proporcionan:
-- 1. Actualización automática de UPDATED_AT (auditoría)
-- 2. Validación de transiciones de estado (integridad de negocio)
-- 3. Registro automático de generación de PDF (auditoría)
-- 4. Validación de assessment listo (integridad referencial)
--
-- NOTA: Los triggers complementan las validaciones en el Business Service,
-- proporcionando una capa adicional de integridad a nivel de base de datos.
-- ============================================================================
