-- ============================================================================
-- VISTAS PARA CONFORMITY DECLARATION
-- ============================================================================
-- Descripción: Script para crear vistas optimizadas para consultas comunes
-- Fecha: Enero 2025
-- Módulo: Conformity Declaration
-- ============================================================================

-- ============================================================================
-- VISTA 1: Declaraciones con información completa del proyecto
-- ============================================================================
-- Propósito: Vista que incluye información del proyecto y assessment
-- Optimiza: Queries que necesitan datos relacionados sin hacer múltiples JOINs
-- ============================================================================

CREATE OR REPLACE VIEW VW_CONFORMITY_DECLARATIONS_WITH_PROJECT AS
SELECT
    cd.IDX_DECLARATION,
    cd.PROVIDER_NAME,
    cd.AI_SYSTEM_NAME,
    cd.AI_SYSTEM_VERSION,
    cd.STATUS,
    cd.SIGNATURE_DATE,
    cd.SIGNED_BY,
    cd.CREATED_AT,
    cd.UPDATED_AT,
    cd.OVERALL_COMPLIANCE_SCORE,
    cd.COMPLIANCE_PERCENTAGE,
    -- Información del Assessment
    ca.IDXCOMPLIANCEASSESSMENT AS ASSESSMENT_ID,
    ca.COMASSESSMENTNAME AS ASSESSMENT_NAME,
    ca.COMREADYFORCERTIFICATION AS ASSESSMENT_READY,
    -- Información del Proyecto
    p.IDXPROJECT AS PROJECT_ID,
    p.PRJNAME AS PROJECT_NAME,
    p.PRJDESCRIPTION AS PROJECT_DESCRIPTION
FROM
    GOVCONFORMITYDECLARATIONS cd
    INNER JOIN COMPLIANCEASSESSMENTS ca ON cd.ASSESSMENT_ID = ca.IDXCOMPLIANCEASSESSMENT
    INNER JOIN PROJECTS p ON ca.IDXPROJECT = p.IDXPROJECT;

-- Comentario de la vista
COMMENT ON VIEW VW_CONFORMITY_DECLARATIONS_WITH_PROJECT IS
'Vista que incluye información completa de declaraciones con datos del proyecto y assessment';

-- ============================================================================
-- VISTA 2: Estadísticas de declaraciones por proyecto
-- ============================================================================
-- Propósito: Vista agregada con estadísticas por proyecto
-- Optimiza: Dashboard y reportes que necesitan estadísticas
-- ============================================================================

CREATE OR REPLACE VIEW VW_CONFORMITY_DECLARATIONS_STATISTICS AS
SELECT
    p.IDXPROJECT AS PROJECT_ID,
    p.PRJNAME AS PROJECT_NAME,
    COUNT(cd.IDX_DECLARATION) AS TOTAL_DECLARATIONS,
    COUNT(CASE WHEN cd.STATUS = 'SIGNED' THEN 1 END) AS SIGNED_DECLARATIONS,
    COUNT(CASE WHEN cd.STATUS = 'DRAFT' THEN 1 END) AS DRAFT_DECLARATIONS,
    COUNT(CASE WHEN cd.STATUS = 'PUBLISHED' THEN 1 END) AS PUBLISHED_DECLARATIONS,
    COUNT(CASE WHEN cd.STATUS = 'REVOKED' THEN 1 END) AS REVOKED_DECLARATIONS,
    MAX(cd.CREATED_AT) AS LATEST_DECLARATION_DATE,
    MAX(cd.SIGNATURE_DATE) AS LATEST_SIGNATURE_DATE,
    MAX(cd.AI_SYSTEM_VERSION) AS LATEST_VERSION
FROM
    PROJECTS p
    LEFT JOIN COMPLIANCEASSESSMENTS ca ON p.IDXPROJECT = ca.IDXPROJECT
    LEFT JOIN GOVCONFORMITYDECLARATIONS cd ON ca.IDXCOMPLIANCEASSESSMENT = cd.ASSESSMENT_ID
GROUP BY
    p.IDXPROJECT, p.PRJNAME;

-- Comentario de la vista
COMMENT ON VIEW VW_CONFORMITY_DECLARATIONS_STATISTICS IS
'Vista agregada con estadísticas de declaraciones por proyecto';

-- ============================================================================
-- VISTA 3: Declaraciones firmadas listas para publicación
-- ============================================================================
-- Propósito: Vista de declaraciones SIGNED que pueden ser publicadas
-- Optimiza: Queries para integración con Registro EU (Art. 49)
-- ============================================================================

CREATE OR REPLACE VIEW VW_CONFORMITY_DECLARATIONS_READY_FOR_PUBLICATION AS
SELECT
    cd.IDX_DECLARATION,
    cd.PROVIDER_NAME,
    cd.AI_SYSTEM_NAME,
    cd.AI_SYSTEM_VERSION,
    cd.SIGNATURE_DATE,
    cd.SIGNED_BY,
    cd.DIGITAL_SIGNATURE,
    cd.PDF_PATH,
    p.IDXPROJECT AS PROJECT_ID,
    p.PRJNAME AS PROJECT_NAME,
    ca.IDXCOMPLIANCEASSESSMENT AS ASSESSMENT_ID
FROM
    GOVCONFORMITYDECLARATIONS cd
    INNER JOIN COMPLIANCEASSESSMENTS ca ON cd.ASSESSMENT_ID = ca.IDXCOMPLIANCEASSESSMENT
    INNER JOIN PROJECTS p ON ca.IDXPROJECT = p.IDXPROJECT
WHERE
    cd.STATUS = 'SIGNED'
    AND cd.SIGNATURE_DATE IS NOT NULL
    AND cd.SIGNED_BY IS NOT NULL
    AND cd.PDF_PATH IS NOT NULL
ORDER BY
    cd.SIGNATURE_DATE DESC;

-- Comentario de la vista
COMMENT ON VIEW VW_CONFORMITY_DECLARATIONS_READY_FOR_PUBLICATION IS
'Vista de declaraciones SIGNED completas listas para publicación en Registro EU (Art. 49)';

-- ============================================================================
-- VISTA 4: Historial de versiones por proyecto
-- ============================================================================
-- Propósito: Vista ordenada de todas las versiones de declaraciones por proyecto
-- Optimiza: Queries de historial y comparación de versiones
-- ============================================================================

CREATE OR REPLACE VIEW VW_CONFORMITY_DECLARATIONS_VERSION_HISTORY AS
SELECT
    p.IDXPROJECT AS PROJECT_ID,
    p.PRJNAME AS PROJECT_NAME,
    cd.IDX_DECLARATION,
    cd.AI_SYSTEM_VERSION,
    cd.STATUS,
    cd.CREATED_AT,
    cd.SIGNATURE_DATE,
    cd.OVERALL_COMPLIANCE_SCORE,
    cd.COMPLIANCE_PERCENTAGE,
    ROW_NUMBER() OVER (
        PARTITION BY p.IDXPROJECT
        ORDER BY cd.CREATED_AT DESC
    ) AS VERSION_NUMBER
FROM
    GOVCONFORMITYDECLARATIONS cd
    INNER JOIN COMPLIANCEASSESSMENTS ca ON cd.ASSESSMENT_ID = ca.IDXCOMPLIANCEASSESSMENT
    INNER JOIN PROJECTS p ON ca.IDXPROJECT = p.IDXPROJECT
ORDER BY
    p.IDXPROJECT, cd.CREATED_AT DESC;

-- Comentario de la vista
COMMENT ON VIEW VW_CONFORMITY_DECLARATIONS_VERSION_HISTORY IS
'Vista del historial de versiones de declaraciones por proyecto, ordenado por fecha';

-- ============================================================================
-- COMENTARIOS SOBRE VISTAS
-- ============================================================================
--
-- Estas vistas optimizan:
-- 1. Consultas que necesitan datos relacionados (proyecto + assessment)
-- 2. Estadísticas agregadas para dashboards
-- 3. Declaraciones listas para publicación
-- 4. Historial de versiones
--
-- NOTA: Las vistas pueden ser materializadas si el rendimiento lo requiere:
-- CREATE MATERIALIZED VIEW ... AS SELECT ...
-- REFRESH MATERIALIZED VIEW ...;
-- ============================================================================
