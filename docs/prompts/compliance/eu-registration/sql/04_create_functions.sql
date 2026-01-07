-- =====================================================
-- EU REGISTRATION - FUNCIONES
-- =====================================================
-- Módulo: EU AI Act Compliance
-- Propósito: Funciones de validación, utilidad y consulta
-- Fecha: Diciembre 2025
-- =====================================================

-- Función para validar submission data según tipo de registro
CREATE OR REPLACE FUNCTION fn_reg_validate_submission_data(
    jsonb_data JSONB,
    registration_type VARCHAR
)
RETURNS BOOLEAN AS $$
DECLARE
    required_fields TEXT[];
    field TEXT;
BEGIN
    -- Definir campos requeridos según tipo
    IF registration_type = 'STANDARD' THEN
        required_fields := ARRAY['sectionA', 'sectionB', 'sectionC'];
    ELSIF registration_type = 'SENSITIVE' THEN
        required_fields := ARRAY['sectionA', 'sectionB'];
    ELSIF registration_type = 'NATIONAL' THEN
        required_fields := ARRAY['sectionA', 'sectionC'];
    ELSE
        RAISE EXCEPTION 'Unknown registration type: %', registration_type;
    END IF;
    
    -- Validar que existan todos los campos requeridos
    FOREACH field IN ARRAY required_fields
    LOOP
        IF NOT (jsonb_data ? field) THEN
            RETURN FALSE;
        END IF;
    END LOOP;
    
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

-- Función para obtener el último registro de un proyecto
CREATE OR REPLACE FUNCTION fn_reg_get_latest_by_project(
    project_id BIGINT
)
RETURNS TABLE (
    idxeuregistration BIGINT,
    iduuid VARCHAR,
    regregistrationtype VARCHAR,
    regstatus VARCHAR,
    regeuregistrationid VARCHAR,
    regcreatedat TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        r.IDXEUREGISTRATION,
        r.iduuid,
        r.REGREGISTRATIONTYPE,
        r.REGSTATUS,
        r.REGEUREGISTRATIONID,
        r.REGCREATEDAT
    FROM REGEUREGISTRATIONS r
    WHERE r.IDXPROJECT = project_id
    ORDER BY r.REGCREATEDAT DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- Función para contar registros por estado
CREATE OR REPLACE FUNCTION fn_reg_count_by_status(
    status_filter VARCHAR DEFAULT NULL
)
RETURNS TABLE (
    status VARCHAR,
    count BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        r.REGSTATUS::VARCHAR,
        COUNT(*)::BIGINT
    FROM REGEUREGISTRATIONS r
    WHERE (status_filter IS NULL OR r.REGSTATUS = status_filter)
    GROUP BY r.REGSTATUS
    ORDER BY COUNT(*) DESC;
END;
$$ LANGUAGE plpgsql;

-- Función para calcular delay exponencial para retry
CREATE OR REPLACE FUNCTION fn_reg_calculate_retry_delay(
    attempts INTEGER
)
RETURNS INTEGER AS $$
BEGIN
    -- Exponential backoff: 2^attempts minutos (máximo 24 horas = 1440 minutos)
    RETURN LEAST(POWER(2, attempts)::INTEGER, 1440);
END;
$$ LANGUAGE plpgsql;

-- Función para obtener registros pendientes de envío
CREATE OR REPLACE FUNCTION fn_reg_get_pending_submissions(
    max_attempts INTEGER DEFAULT 5
)
RETURNS TABLE (
    idxeuregistration BIGINT,
    iduuid VARCHAR,
    idxproject BIGINT,
    regregistrationtype VARCHAR,
    regattempts INTEGER,
    reglastattemptat TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        r.IDXEUREGISTRATION,
        r.iduuid,
        r.IDXPROJECT,
        r.REGREGISTRATIONTYPE,
        r.REGATTEMPTS,
        r.REGLASTATTEMPTAT
    FROM REGEUREGISTRATIONS r
    WHERE r.REGSTATUS IN ('DRAFT', 'PENDING', 'ERROR')
    AND r.REGATTEMPTS < max_attempts
    ORDER BY r.REGLASTATTEMPTAT NULLS FIRST, r.REGCREATEDAT;
END;
$$ LANGUAGE plpgsql;

-- Función para validar que un proyecto tenga máximo un registro activo por tipo
CREATE OR REPLACE FUNCTION fn_reg_validate_unique_active(
    project_id BIGINT,
    registration_type VARCHAR,
    exclude_id BIGINT DEFAULT NULL
)
RETURNS BOOLEAN AS $$
DECLARE
    count_active INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO count_active
    FROM REGEUREGISTRATIONS r
    WHERE r.IDXPROJECT = project_id
    AND r.REGREGISTRATIONTYPE = registration_type
    AND r.REGSTATUS NOT IN ('REJECTED', 'DELETED', 'CANCELLED')
    AND (exclude_id IS NULL OR r.IDXEUREGISTRATION != exclude_id);
    
    RETURN count_active = 0;
END;
$$ LANGUAGE plpgsql;


