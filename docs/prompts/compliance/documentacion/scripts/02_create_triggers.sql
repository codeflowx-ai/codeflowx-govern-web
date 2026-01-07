-- ============================================================================
-- SCRIPT DE CREACIÓN DE TRIGGERS
-- Módulo: Technical Documentation
-- Tabla: GOVAIACTTECHNICALDOCS
-- Fecha: Diciembre 2025
-- ============================================================================

-- Este script crea triggers para actualización automática de campos
-- Nota: La entidad JPA ya maneja UPDATED_AT con @PreUpdate,
-- pero estos triggers proporcionan una capa adicional de seguridad a nivel de BD

-- ============================================================================
-- FUNCIÓN: Actualizar campo UPDATED_AT automáticamente
-- ============================================================================

CREATE OR REPLACE FUNCTION update_techdocs_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.UPDATED_AT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- TRIGGER: Actualizar UPDATED_AT antes de UPDATE
-- ============================================================================

-- Eliminar trigger si existe
DROP TRIGGER IF EXISTS trg_techdocs_update_updated_at ON GOVAIACTTECHNICALDOCS;

-- Crear trigger
CREATE TRIGGER trg_techdocs_update_updated_at
    BEFORE UPDATE ON GOVAIACTTECHNICALDOCS
    FOR EACH ROW
    EXECUTE FUNCTION update_techdocs_updated_at();

COMMENT ON TRIGGER trg_techdocs_update_updated_at ON GOVAIACTTECHNICALDOCS IS
'Trigger que actualiza automáticamente el campo UPDATED_AT antes de cada actualización';

-- ============================================================================
-- FUNCIÓN: Validar constraint único ENTITY_TYPE + ENTITY_ID
-- ============================================================================

-- Esta validación es redundante porque ya existe el constraint único,
-- pero puede ser útil para mensajes de error personalizados

CREATE OR REPLACE FUNCTION validate_techdocs_entity_unique()
RETURNS TRIGGER AS $$
DECLARE
    existing_count INTEGER;
BEGIN
    -- Verificar si ya existe otra documentación para la misma entidad
    SELECT COUNT(*) INTO existing_count
    FROM GOVAIACTTECHNICALDOCS
    WHERE ENTITY_TYPE = NEW.ENTITY_TYPE
      AND ENTITY_ID = NEW.ENTITY_ID
      AND IDX_TECHNICAL_DOC != COALESCE(NEW.IDX_TECHNICAL_DOC, 0);

    IF existing_count > 0 THEN
        RAISE EXCEPTION 'Ya existe documentación técnica para la entidad % con ID %',
            NEW.ENTITY_TYPE, NEW.ENTITY_ID;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- TRIGGER: Validar unicidad antes de INSERT/UPDATE
-- ============================================================================

-- Eliminar trigger si existe
DROP TRIGGER IF EXISTS trg_techdocs_validate_entity_unique ON GOVAIACTTECHNICALDOCS;

-- Crear trigger
CREATE TRIGGER trg_techdocs_validate_entity_unique
    BEFORE INSERT OR UPDATE ON GOVAIACTTECHNICALDOCS
    FOR EACH ROW
    EXECUTE FUNCTION validate_techdocs_entity_unique();

COMMENT ON TRIGGER trg_techdocs_validate_entity_unique ON GOVAIACTTECHNICALDOCS IS
'Trigger que valida que no exista duplicado de documentación para la misma entidad';

-- ============================================================================
-- FUNCIÓN: Establecer CREATED_AT si no está definido
-- ============================================================================

CREATE OR REPLACE FUNCTION set_techdocs_created_at()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.CREATED_AT IS NULL THEN
        NEW.CREATED_AT = CURRENT_TIMESTAMP;
    END IF;

    IF NEW.UPDATED_AT IS NULL THEN
        NEW.UPDATED_AT = NEW.CREATED_AT;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- TRIGGER: Establecer CREATED_AT antes de INSERT
-- ============================================================================

-- Eliminar trigger si existe
DROP TRIGGER IF EXISTS trg_techdocs_set_created_at ON GOVAIACTTECHNICALDOCS;

-- Crear trigger
CREATE TRIGGER trg_techdocs_set_created_at
    BEFORE INSERT ON GOVAIACTTECHNICALDOCS
    FOR EACH ROW
    EXECUTE FUNCTION set_techdocs_created_at();

COMMENT ON TRIGGER trg_techdocs_set_created_at ON GOVAIACTTECHNICALDOCS IS
'Trigger que establece automáticamente CREATED_AT y UPDATED_AT si no están definidos';

-- ============================================================================
-- VERIFICACIÓN
-- ============================================================================

-- Consulta para verificar triggers creados
-- SELECT
--     trigger_name,
--     event_manipulation,
--     event_object_table,
--     action_statement
-- FROM information_schema.triggers
-- WHERE event_object_table = 'govaiacttechnicaldocs'
-- ORDER BY trigger_name;

