-- =====================================================
-- EU REGISTRATION - PROCEDIMIENTOS ALMACENADOS
-- =====================================================
-- Módulo: EU AI Act Compliance
-- Propósito: Procedimientos para mantenimiento y operaciones batch
-- Fecha: Diciembre 2025
-- =====================================================

-- Procedimiento: Limpiar registros en estado DRAFT antiguos
CREATE OR REPLACE PROCEDURE sp_reg_cleanup_old_drafts(
    days_old INTEGER DEFAULT 90,
    OUT deleted_count INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM REGEUREGISTRATIONS
    WHERE REGSTATUS = 'DRAFT'
    AND REGCREATEDAT < CURRENT_TIMESTAMP - (days_old || ' days')::INTERVAL
    AND REGATTEMPTS = 0;
    
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
END;
$$;

-- Procedimiento: Reintentar envíos fallidos
CREATE OR REPLACE PROCEDURE sp_reg_retry_failed_submissions(
    max_attempts INTEGER DEFAULT 5,
    OUT retried_count INTEGER
)
LANGUAGE plpgsql
AS $$
DECLARE
    reg_record RECORD;
    delay_minutes INTEGER;
BEGIN
    retried_count := 0;
    
    -- Obtener registros que pueden ser reintentados
    FOR reg_record IN 
        SELECT * FROM fn_reg_get_pending_submissions(max_attempts)
    LOOP
        -- Calcular delay exponencial
        delay_minutes := fn_reg_calculate_retry_delay(reg_record.regattempts);
        
        -- Verificar si ha pasado el tiempo de delay
        IF reg_record.reglastattemptat IS NULL OR 
           reg_record.reglastattemptat + (delay_minutes || ' minutes')::INTERVAL <= CURRENT_TIMESTAMP THEN
            -- Cambiar estado a PENDING para reintento
            UPDATE REGEUREGISTRATIONS
            SET REGSTATUS = 'PENDING',
                REGUPDATEDAT = CURRENT_TIMESTAMP
            WHERE IDXEUREGISTRATION = reg_record.idxeuregistration;
            
            retried_count := retried_count + 1;
        END IF;
    END LOOP;
END;
$$;

-- Procedimiento: Actualizar estadísticas de registros
CREATE OR REPLACE PROCEDURE sp_reg_update_statistics(
    OUT total_registrations INTEGER,
    OUT registered_count INTEGER,
    OUT pending_count INTEGER,
    OUT failed_count INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT COUNT(*) INTO total_registrations FROM REGEUREGISTRATIONS;
    
    SELECT COUNT(*) INTO registered_count 
    FROM REGEUREGISTRATIONS 
    WHERE REGSTATUS = 'REGISTERED' AND REGEUREGISTRATIONID IS NOT NULL;
    
    SELECT COUNT(*) INTO pending_count 
    FROM REGEUREGISTRATIONS 
    WHERE REGSTATUS IN ('DRAFT', 'PENDING');
    
    SELECT COUNT(*) INTO failed_count 
    FROM REGEUREGISTRATIONS 
    WHERE REGSTATUS IN ('ERROR', 'REJECTED');
END;
$$;

-- Procedimiento: Validar integridad de datos
CREATE OR REPLACE PROCEDURE sp_reg_validate_integrity(
    OUT validation_errors TEXT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    error_msg TEXT;
BEGIN
    validation_errors := ARRAY[]::TEXT[];
    
    -- Validar que todos los registros tengan UUID
    IF EXISTS (SELECT 1 FROM REGEUREGISTRATIONS WHERE iduuid IS NULL) THEN
        validation_errors := array_append(validation_errors, 'Found registrations without UUID');
    END IF;
    
    -- Validar que los registros REGISTERED tengan EU Registration ID
    IF EXISTS (SELECT 1 FROM REGEUREGISTRATIONS WHERE REGSTATUS = 'REGISTERED' AND REGEUREGISTRATIONID IS NULL) THEN
        validation_errors := array_append(validation_errors, 'Found REGISTERED registrations without EU Registration ID');
    END IF;
    
    -- Validar que los registros SUBMITTED tengan fecha de envío
    IF EXISTS (SELECT 1 FROM REGEUREGISTRATIONS WHERE REGSTATUS = 'SUBMITTED' AND REGSUBMISSIONDATE IS NULL) THEN
        validation_errors := array_append(validation_errors, 'Found SUBMITTED registrations without submission date');
    END IF;
    
    -- Validar referencias a proyectos
    IF EXISTS (SELECT 1 FROM REGEUREGISTRATIONS r 
               LEFT JOIN PRJPROJECTS p ON r.IDXPROJECT = p.IDXPROJECT 
               WHERE p.IDXPROJECT IS NULL) THEN
        validation_errors := array_append(validation_errors, 'Found registrations with invalid project references');
    END IF;
END;
$$;


