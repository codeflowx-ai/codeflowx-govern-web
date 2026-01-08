-- =====================================================
-- EU REGISTRATION - VISTAS
-- =====================================================
-- Módulo: EU AI Act Compliance
-- Propósito: Vistas para reportes y consultas complejas
-- Fecha: Diciembre 2025
-- =====================================================

-- Vista: Resumen de registros por proyecto y estado
CREATE OR REPLACE VIEW v_reg_registrations_summary AS
SELECT 
    p.IDXPROJECT,
    p.PRJNAME AS project_name,
    r.REGREGISTRATIONTYPE,
    r.REGSTATUS,
    COUNT(*) AS total_registrations,
    MAX(r.REGCREATEDAT) AS latest_registration_date,
    MAX(r.REGREGISTRATIONDATE) AS latest_registration_date_eu,
    COUNT(CASE WHEN r.REGEUREGISTRATIONID IS NOT NULL THEN 1 END) AS registered_count
FROM REGEUREGISTRATIONS r
INNER JOIN PRJPROJECTS p ON r.IDXPROJECT = p.IDXPROJECT
GROUP BY p.IDXPROJECT, p.PRJNAME, r.REGREGISTRATIONTYPE, r.REGSTATUS
ORDER BY p.IDXPROJECT, r.REGREGISTRATIONTYPE, r.REGSTATUS;

-- Vista: Registros pendientes de envío
CREATE OR REPLACE VIEW v_reg_pending_submissions AS
SELECT 
    r.IDXEUREGISTRATION,
    r.iduuid,
    r.IDXPROJECT,
    p.PRJNAME AS project_name,
    r.REGREGISTRATIONTYPE,
    r.REGSTATUS,
    r.REGATTEMPTS,
    r.REGLASTATTEMPTAT,
    r.REGCREATEDAT,
    fn_reg_calculate_retry_delay(r.REGATTEMPTS) AS retry_delay_minutes,
    (r.REGLASTATTEMPTAT + (fn_reg_calculate_retry_delay(r.REGATTEMPTS) || ' minutes')::INTERVAL) AS next_retry_at
FROM REGEUREGISTRATIONS r
INNER JOIN PRJPROJECTS p ON r.IDXPROJECT = p.IDXPROJECT
WHERE r.REGSTATUS IN ('DRAFT', 'PENDING', 'ERROR')
AND r.REGATTEMPTS < 5
ORDER BY r.REGLASTATTEMPTAT NULLS FIRST, r.REGCREATEDAT;

-- Vista: Registros que fallaron en el envío
CREATE OR REPLACE VIEW v_reg_failed_submissions AS
SELECT 
    r.IDXEUREGISTRATION,
    r.iduuid,
    r.IDXPROJECT,
    p.PRJNAME AS project_name,
    r.REGREGISTRATIONTYPE,
    r.REGSTATUS,
    r.REGATTEMPTS,
    r.REGERRORMESSAGE,
    r.REGLASTATTEMPTAT,
    r.REGCREATEDAT,
    CASE 
        WHEN r.REGATTEMPTS >= 5 THEN 'MAX_ATTEMPTS_REACHED'
        ELSE 'RETRY_POSSIBLE'
    END AS failure_status
FROM REGEUREGISTRATIONS r
INNER JOIN PRJPROJECTS p ON r.IDXPROJECT = p.IDXPROJECT
WHERE r.REGSTATUS IN ('ERROR', 'REJECTED')
ORDER BY r.REGLASTATTEMPTAT DESC;

-- Vista: Registros exitosos (registrados en EU Database)
CREATE OR REPLACE VIEW v_reg_successful_registrations AS
SELECT 
    r.IDXEUREGISTRATION,
    r.iduuid,
    r.IDXPROJECT,
    p.PRJNAME AS project_name,
    r.REGREGISTRATIONTYPE,
    r.REGEUREGISTRATIONID,
    r.REGREGISTRATIONDATE,
    r.REGSUBMISSIONDATE,
    EXTRACT(EPOCH FROM (r.REGREGISTRATIONDATE - r.REGSUBMISSIONDATE)) / 3600 AS processing_hours,
    r.REGATTEMPTS,
    r.REGCREATEDAT
FROM REGEUREGISTRATIONS r
INNER JOIN PRJPROJECTS p ON r.IDXPROJECT = p.IDXPROJECT
WHERE r.REGSTATUS = 'REGISTERED'
AND r.REGEUREGISTRATIONID IS NOT NULL
ORDER BY r.REGREGISTRATIONDATE DESC;

-- Vista: Estadísticas de registros por tipo y estado
CREATE OR REPLACE VIEW v_reg_statistics AS
SELECT 
    REGREGISTRATIONTYPE,
    REGSTATUS,
    COUNT(*) AS count,
    AVG(REGATTEMPTS) AS avg_attempts,
    MAX(REGATTEMPTS) AS max_attempts,
    COUNT(CASE WHEN REGEUREGISTRATIONID IS NOT NULL THEN 1 END) AS registered_count,
    AVG(EXTRACT(EPOCH FROM (REGREGISTRATIONDATE - REGSUBMISSIONDATE)) / 3600) AS avg_processing_hours
FROM REGEUREGISTRATIONS
GROUP BY REGREGISTRATIONTYPE, REGSTATUS
ORDER BY REGREGISTRATIONTYPE, REGSTATUS;


