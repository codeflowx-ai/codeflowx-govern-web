-- ============================================================================
-- SCRIPT DE CREACIÓN DE TRIGGERS PARA FRIA
-- ============================================================================
-- Módulo: FRIA (Fundamental Rights Impact Assessment)
-- Tabla: FRIAFUNDAMENTALRIGHTSASSESSMENTS
-- Fecha: Diciembre 2025
-- Descripción: Triggers para auditoría, validación y lógica de negocio
-- ============================================================================

-- ============================================================================
-- FUNCIÓN: Actualización automática de FRIAUPDATEDAT
-- ============================================================================
-- Descripción: Actualiza automáticamente FRIAUPDATEDAT cuando se modifica
--              cualquier campo de la tabla (excepto en INSERT)
-- ============================================================================

CREATE OR REPLACE FUNCTION update_fria_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.FRIAUPDATEDAT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Actualizar FRIAUPDATEDAT en UPDATE
CREATE TRIGGER trg_fria_update_timestamp
    BEFORE UPDATE ON FRIAFUNDAMENTALRIGHTSASSESSMENTS
    FOR EACH ROW
    EXECUTE FUNCTION update_fria_updated_at();

-- ============================================================================
-- FUNCIÓN: Validación de notificación a autoridades
-- ============================================================================
-- Descripción: Valida que cuando se marca FRIANOTIFIED = TRUE, se debe
--              proporcionar FRIANOTIFICATIONID y FRIANOTIFICATIONDATE
-- ============================================================================

CREATE OR REPLACE FUNCTION validate_fria_notification()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.FRIANOTIFIED = TRUE THEN
        IF NEW.FRIANOTIFICATIONID IS NULL OR NEW.FRIANOTIFICATIONID = '' THEN
            RAISE EXCEPTION 'FRIANOTIFICATIONID es obligatorio cuando FRIANOTIFIED = TRUE';
        END IF;
        IF NEW.FRIANOTIFICATIONDATE IS NULL THEN
            RAISE EXCEPTION 'FRIANOTIFICATIONDATE es obligatorio cuando FRIANOTIFIED = TRUE';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Validar notificación
CREATE TRIGGER trg_fria_validate_notification
    BEFORE INSERT OR UPDATE ON FRIAFUNDAMENTALRIGHTSASSESSMENTS
    FOR EACH ROW
    EXECUTE FUNCTION validate_fria_notification();

-- ============================================================================
-- FUNCIÓN: Validación de aprobación
-- ============================================================================
-- Descripción: Valida que cuando se marca FRIAAPPROVED = TRUE, se debe
--              proporcionar FRIAAPPROVEDBY y FRIAAPPROVALDATE
-- ============================================================================

CREATE OR REPLACE FUNCTION validate_fria_approval()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.FRIAAPPROVED = TRUE THEN
        IF NEW.FRIAAPPROVEDBY IS NULL THEN
            RAISE EXCEPTION 'FRIAAPPROVEDBY es obligatorio cuando FRIAAPPROVED = TRUE';
        END IF;
        IF NEW.FRIAAPPROVALDATE IS NULL THEN
            RAISE EXCEPTION 'FRIAAPPROVALDATE es obligatorio cuando FRIAAPPROVED = TRUE';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Validar aprobación
CREATE TRIGGER trg_fria_validate_approval
    BEFORE INSERT OR UPDATE ON FRIAFUNDAMENTALRIGHTSASSESSMENTS
    FOR EACH ROW
    EXECUTE FUNCTION validate_fria_approval();

-- ============================================================================
-- FUNCIÓN: Validación de compliance Art. 27
-- ============================================================================
-- Descripción: Valida que cuando se marca FRIAART27COMPLIANT = TRUE, el
--              FRIACOMPLETENESSCORE debe ser >= 1.00 (100%)
-- ============================================================================

CREATE OR REPLACE FUNCTION validate_fria_compliance()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.FRIAART27COMPLIANT = TRUE THEN
        IF NEW.FRIACOMPLETENESSCORE IS NULL OR NEW.FRIACOMPLETENESSCORE < 1.00 THEN
            RAISE EXCEPTION 'FRIACOMPLETENESSCORE debe ser >= 1.00 cuando FRIAART27COMPLIANT = TRUE';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Validar compliance
CREATE TRIGGER trg_fria_validate_compliance
    BEFORE INSERT OR UPDATE ON FRIAFUNDAMENTALRIGHTSASSESSMENTS
    FOR EACH ROW
    EXECUTE FUNCTION validate_fria_compliance();

-- ============================================================================
-- FUNCIÓN: Auditoría de cambios en FRIA
-- ============================================================================
-- Descripción: Registra cambios importantes en una tabla de auditoría
--              (requiere tabla FRIAFUNDAMENTALRIGHTSASSESSMENTS_AUDIT)
-- ============================================================================

-- Crear tabla de auditoría si no existe
CREATE TABLE IF NOT EXISTS FRIAFUNDAMENTALRIGHTSASSESSMENTS_AUDIT (
    AUDIT_ID BIGSERIAL PRIMARY KEY,
    IDXFRIAASSESSMENT BIGINT NOT NULL,
    ACTION_TYPE VARCHAR(10) NOT NULL, -- INSERT, UPDATE, DELETE
    CHANGED_FIELDS TEXT, -- JSON con campos modificados
    OLD_VALUES TEXT, -- JSON con valores antiguos
    NEW_VALUES TEXT, -- JSON con valores nuevos
    CHANGED_BY VARCHAR(255), -- Usuario que realizó el cambio
    CHANGED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índice en tabla de auditoría
CREATE INDEX IF NOT EXISTS IDX_FRIA_AUDIT_FRIA_ID
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS_AUDIT(IDXFRIAASSESSMENT);

CREATE INDEX IF NOT EXISTS IDX_FRIA_AUDIT_CHANGED_AT
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS_AUDIT(CHANGED_AT DESC);

-- Función de auditoría
CREATE OR REPLACE FUNCTION audit_fria_changes()
RETURNS TRIGGER AS $$
DECLARE
    changed_fields_json TEXT;
    old_values_json TEXT;
    new_values_json TEXT;
BEGIN
    -- Construir JSON con campos modificados (solo en UPDATE)
    IF TG_OP = 'UPDATE' THEN
        -- Comparar campos importantes
        changed_fields_json := json_build_object(
            'frianotified', CASE WHEN OLD.FRIANOTIFIED IS DISTINCT FROM NEW.FRIANOTIFIED THEN 'changed' ELSE NULL END,
            'friaapproved', CASE WHEN OLD.FRIAAPPROVED IS DISTINCT FROM NEW.FRIAAPPROVED THEN 'changed' ELSE NULL END,
            'friaart27compliant', CASE WHEN OLD.FRIAART27COMPLIANT IS DISTINCT FROM NEW.FRIAART27COMPLIANT THEN 'changed' ELSE NULL END,
            'friafinalrisk', CASE WHEN OLD.FRIAFINALRISK IS DISTINCT FROM NEW.FRIAFINALRISK THEN 'changed' ELSE NULL END,
            'friacompletenessscore', CASE WHEN OLD.FRIACOMPLETENESSCORE IS DISTINCT FROM NEW.FRIACOMPLETENESSCORE THEN 'changed' ELSE NULL END
        )::TEXT;

        old_values_json := json_build_object(
            'frianotified', OLD.FRIANOTIFIED,
            'friaapproved', OLD.FRIAAPPROVED,
            'friaart27compliant', OLD.FRIAART27COMPLIANT,
            'friafinalrisk', OLD.FRIAFINALRISK,
            'friacompletenessscore', OLD.FRIACOMPLETENESSCORE
        )::TEXT;

        new_values_json := json_build_object(
            'frianotified', NEW.FRIANOTIFIED,
            'friaapproved', NEW.FRIAAPPROVED,
            'friaart27compliant', NEW.FRIAART27COMPLIANT,
            'friafinalrisk', NEW.FRIAFINALRISK,
            'friacompletenessscore', NEW.FRIACOMPLETENESSCORE
        )::TEXT;
    END IF;

    INSERT INTO FRIAFUNDAMENTALRIGHTSASSESSMENTS_AUDIT (
        IDXFRIAASSESSMENT,
        ACTION_TYPE,
        CHANGED_FIELDS,
        OLD_VALUES,
        NEW_VALUES,
        CHANGED_AT
    ) VALUES (
        COALESCE(NEW.IDXFRIAASSESSMENT, OLD.IDXFRIAASSESSMENT),
        TG_OP,
        changed_fields_json,
        old_values_json,
        new_values_json,
        CURRENT_TIMESTAMP
    );

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Trigger: Auditoría de cambios
CREATE TRIGGER trg_fria_audit_changes
    AFTER INSERT OR UPDATE OR DELETE ON FRIAFUNDAMENTALRIGHTSASSESSMENTS
    FOR EACH ROW
    EXECUTE FUNCTION audit_fria_changes();

-- ============================================================================
-- COMENTARIOS SOBRE TRIGGERS
-- ============================================================================
--
-- 1. Los triggers BEFORE se ejecutan antes de la operación, permitiendo
--    validaciones y modificaciones de datos.
--
-- 2. Los triggers AFTER se ejecutan después de la operación, permitiendo
--    acciones de auditoría y logging.
--
-- 3. La tabla de auditoría se puede limpiar periódicamente para mantener
--    el rendimiento. Se recomienda mantener los últimos 2 años de datos.
--
-- 4. Los triggers de validación lanzan excepciones que se propagan a la
--    aplicación, por lo que deben manejarse adecuadamente en el código Java.
--
-- ============================================================================

