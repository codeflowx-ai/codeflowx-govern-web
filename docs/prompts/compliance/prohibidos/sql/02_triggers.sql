-- ============================================================================
-- SCRIPTS DE TRIGGERS PARA PROHIBITED SYSTEMS
-- ============================================================================
--
-- Estos triggers proporcionan funcionalidad adicional de auditoría y
-- validación a nivel de base de datos.
--
-- ORDEN DE EJECUCIÓN:
-- 1. Iniciar aplicación (JPA crea tablas)
-- 2. Ejecutar 01_indexes.sql
-- 3. Ejecutar este script (triggers)
--
-- ============================================================================

-- ============================================================================
-- FUNCIÓN: Actualizar PRSUPDATEDAT automáticamente
-- ============================================================================
--
-- Actualiza el campo PRSUPDATEDAT cada vez que se modifica un registro
-- en GOVPROHIBITEDSYSTEMS. Aunque JPA ya lo hace con @PreUpdate, este
-- trigger asegura que se actualice incluso si se modifica directamente en BD.
--

CREATE OR REPLACE FUNCTION update_prohibited_system_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.PRSUPDATEDAT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para actualizar PRSUPDATEDAT
DROP TRIGGER IF EXISTS trg_prohibited_system_update_timestamp ON GOVPROHIBITEDSYSTEMS;
CREATE TRIGGER trg_prohibited_system_update_timestamp
    BEFORE UPDATE ON GOVPROHIBITEDSYSTEMS
    FOR EACH ROW
    EXECUTE FUNCTION update_prohibited_system_updated_at();

-- ============================================================================
-- FUNCIÓN: Validar que keywords no esté vacío al crear sistema activo
-- ============================================================================
--
-- Valida que si un sistema prohibido está activo (PRSACTIVE = true),
-- debe tener al menos un keyword en PRSKEYWORDS.
--

CREATE OR REPLACE FUNCTION validate_prohibited_system_keywords()
RETURNS TRIGGER AS $$
BEGIN
    -- Si el sistema está activo, debe tener keywords
    IF NEW.PRSACTIVE = true THEN
        IF NEW.PRSKEYWORDS IS NULL OR
           jsonb_array_length(NEW.PRSKEYWORDS) = 0 THEN
            RAISE EXCEPTION 'Un sistema prohibido activo debe tener al menos un keyword';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para validar keywords
DROP TRIGGER IF EXISTS trg_prohibited_system_validate_keywords ON GOVPROHIBITEDSYSTEMS;
CREATE TRIGGER trg_prohibited_system_validate_keywords
    BEFORE INSERT OR UPDATE ON GOVPROHIBITEDSYSTEMS
    FOR EACH ROW
    EXECUTE FUNCTION validate_prohibited_system_keywords();

-- ============================================================================
-- FUNCIÓN: Auditoría de cambios en sistemas prohibidos
-- ============================================================================
--
-- Registra cambios importantes en sistemas prohibidos en una tabla de auditoría.
-- Esto es útil para cumplimiento y trazabilidad.
--
-- NOTA: Requiere crear la tabla de auditoría primero (ver más abajo)
--

-- Crear tabla de auditoría si no existe
CREATE TABLE IF NOT EXISTS GOVPROHIBITEDSYSTEMS_AUDIT (
    AUDIT_ID BIGSERIAL PRIMARY KEY,
    IDXPROHIBITEDSYSTEM BIGINT NOT NULL,
    PRSNAME VARCHAR(255) NOT NULL,
    ACTION VARCHAR(10) NOT NULL, -- INSERT, UPDATE, DELETE
    OLD_VALUES JSONB,
    NEW_VALUES JSONB,
    CHANGED_BY VARCHAR(100),
    CHANGED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para búsquedas rápidas en auditoría
CREATE INDEX IF NOT EXISTS idx_prohibited_systems_audit_system
ON GOVPROHIBITEDSYSTEMS_AUDIT (IDXPROHIBITEDSYSTEM);

CREATE INDEX IF NOT EXISTS idx_prohibited_systems_audit_date
ON GOVPROHIBITEDSYSTEMS_AUDIT (CHANGED_AT DESC);

-- Función de auditoría
CREATE OR REPLACE FUNCTION audit_prohibited_system_changes()
RETURNS TRIGGER AS $$
DECLARE
    old_data JSONB;
    new_data JSONB;
BEGIN
    IF TG_OP = 'DELETE' THEN
        old_data := row_to_json(OLD)::jsonb;
        INSERT INTO GOVPROHIBITEDSYSTEMS_AUDIT (
            IDXPROHIBITEDSYSTEM,
            PRSNAME,
            ACTION,
            OLD_VALUES,
            CHANGED_BY,
            CHANGED_AT
        ) VALUES (
            OLD.IDXPROHIBITEDSYSTEM,
            OLD.PRSNAME,
            'DELETE',
            old_data,
            OLD.PRSUPDATEDBY,
            CURRENT_TIMESTAMP
        );
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        old_data := row_to_json(OLD)::jsonb;
        new_data := row_to_json(NEW)::jsonb;
        INSERT INTO GOVPROHIBITEDSYSTEMS_AUDIT (
            IDXPROHIBITEDSYSTEM,
            PRSNAME,
            ACTION,
            OLD_VALUES,
            NEW_VALUES,
            CHANGED_BY,
            CHANGED_AT
        ) VALUES (
            NEW.IDXPROHIBITEDSYSTEM,
            NEW.PRSNAME,
            'UPDATE',
            old_data,
            new_data,
            NEW.PRSUPDATEDBY,
            CURRENT_TIMESTAMP
        );
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        new_data := row_to_json(NEW)::jsonb;
        INSERT INTO GOVPROHIBITEDSYSTEMS_AUDIT (
            IDXPROHIBITEDSYSTEM,
            PRSNAME,
            ACTION,
            NEW_VALUES,
            CHANGED_BY,
            CHANGED_AT
        ) VALUES (
            NEW.IDXPROHIBITEDSYSTEM,
            NEW.PRSNAME,
            'INSERT',
            new_data,
            NEW.PRSCREATEDBY,
            CURRENT_TIMESTAMP
        );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger de auditoría
DROP TRIGGER IF EXISTS trg_prohibited_system_audit ON GOVPROHIBITEDSYSTEMS;
CREATE TRIGGER trg_prohibited_system_audit
    AFTER INSERT OR UPDATE OR DELETE ON GOVPROHIBITEDSYSTEMS
    FOR EACH ROW
    EXECUTE FUNCTION audit_prohibited_system_changes();

-- ============================================================================
-- FUNCIÓN: Validar bloqueo de despliegue en proyectos
-- ============================================================================
--
-- Valida que si un proyecto tiene PRJDEPLOYMENTBLOCKED = true,
-- debe tener una razón de bloqueo (PRJBLOCKREASON) no vacía.
--

CREATE OR REPLACE FUNCTION validate_project_block_reason()
RETURNS TRIGGER AS $$
BEGIN
    -- Si el despliegue está bloqueado, debe haber una razón
    IF NEW.PRJDEPLOYMENTBLOCKED = true THEN
        IF NEW.PRJBLOCKREASON IS NULL OR
           TRIM(NEW.PRJBLOCKREASON) = '' THEN
            RAISE EXCEPTION 'Un proyecto con despliegue bloqueado debe tener una razón de bloqueo';
        END IF;

        -- Validar longitud máxima
        IF LENGTH(NEW.PRJBLOCKREASON) > 500 THEN
            RAISE EXCEPTION 'La razón de bloqueo no puede exceder 500 caracteres';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para validar razón de bloqueo
DROP TRIGGER IF EXISTS trg_project_validate_block_reason ON PRJPROJECTS;
CREATE TRIGGER trg_project_validate_block_reason
    BEFORE INSERT OR UPDATE ON PRJPROJECTS
    FOR EACH ROW
    WHEN (NEW.PRJDEPLOYMENTBLOCKED = true)
    EXECUTE FUNCTION validate_project_block_reason();

-- ============================================================================
-- VERIFICACIÓN DE TRIGGERS CREADOS
-- ============================================================================

-- Consulta para verificar que los triggers se crearon correctamente
-- SELECT
--     trigger_name,
--     event_object_table,
--     action_statement,
--     action_timing,
--     event_manipulation
-- FROM information_schema.triggers
-- WHERE event_object_table IN ('GOVPROHIBITEDSYSTEMS', 'PRJPROJECTS')
-- ORDER BY event_object_table, trigger_name;

-- ============================================================================
-- NOTAS
-- ============================================================================
--
-- 1. Los triggers de validación se ejecutan ANTES de INSERT/UPDATE,
--    por lo que pueden prevenir operaciones inválidas.
--
-- 2. Los triggers de auditoría se ejecutan DESPUÉS de INSERT/UPDATE/DELETE,
--    por lo que siempre se registran los cambios.
--
-- 3. La tabla de auditoría puede crecer mucho con el tiempo. Considerar:
--    - Implementar rotación de datos antiguos
--    - Archivar datos de más de X meses
--    - Usar particionamiento por fecha
--
-- 4. Para deshabilitar temporalmente un trigger:
--    ALTER TABLE GOVPROHIBITEDSYSTEMS DISABLE TRIGGER trg_prohibited_system_audit;
--
-- 5. Para habilitar un trigger:
--    ALTER TABLE GOVPROHIBITEDSYSTEMS ENABLE TRIGGER trg_prohibited_system_audit;
--
-- ============================================================================

