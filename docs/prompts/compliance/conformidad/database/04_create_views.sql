-- ============================================================================
-- VISTAS PARA MÓDULO CONFORMITY DECLARATION
-- ============================================================================
-- Módulo: Conformity Declaration (Declaración de Conformidad)
-- Fecha: Enero 2025
-- ============================================================================

-- ============================================================================
-- VISTA: Declaraciones con información completa del proyecto
-- ============================================================================
-- Vista que combina información de declaraciones, assessments y proyectos
-- ============================================================================

CREATE OR REPLACE VIEW v_conformity_declarations_full AS
SELECT
    cd.IDX_DECLARATION as declaration_id,
    cd.ASSESSMENT_ID as assessment_id,
    cd.PROVIDER_NAME,
    cd.PROVIDER_ADDRESS,
    cd.PROVIDER_COUNTRY,
    cd.AI_SYSTEM_NAME,
    cd.AI_SYSTEM_TYPE,
    cd.AI_SYSTEM_VERSION,
    cd.STATUS as declaration_status,
    cd.OVERALL_COMPLIANCE_SCORE,
    cd.COMPLIANCE_PERCENTAGE,
    cd.SIGNATURE_DATE,
    cd.SIGNED_BY,
    cd.PDF_PATH,
    cd.PDF_GENERATED_AT,
    cd.CREATED_AT as declaration_created_at,
    cd.UPDATED_AT as declaration_updated_at,
    -- Información del Assessment
    ca.IDXCOMPLIANCEASSESSMENT as assessment_id_full,
    ca.COMOVERALLSCORE as assessment_overall_score,
    ca.COMREADYFORCERTIFICATION as assessment_ready_for_certification,
    -- Información del Proyecto
    p.IDXPROJECT as project_id,
    p.PRJNAME as project_name,
    p.PRJISHIGHRISK as project_is_high_risk,
    p.STATUS as project_status
FROM GOVCONFORMITYDECLARATIONS cd
JOIN COMPLIANCEASSESSMENTS ca ON ca.IDXCOMPLIANCEASSESSMENT = cd.ASSESSMENT_ID
JOIN PROJECTS p ON p.IDXPROJECT = ca.IDXPROJECT;

-- ============================================================================
-- VISTA: Resumen de declaraciones por proyecto
-- ============================================================================
-- Vista agregada con estadísticas de declaraciones por proyecto
-- ============================================================================

CREATE OR REPLACE VIEW v_project_declarations_summary AS
SELECT
    p.IDXPROJECT as project_id,
    p.PRJNAME as project_name,
    p.PRJISHIGHRISK as project_is_high_risk,
    COUNT(cd.IDX_DECLARATION) as total_declarations,
    COUNT(cd.IDX_DECLARATION) FILTER (WHERE cd.STATUS = 'SIGNED') as signed_count,
    COUNT(cd.IDX_DECLARATION) FILTER (WHERE cd.STATUS = 'DRAFT') as draft_count,
    COUNT(cd.IDX_DECLARATION) FILTER (WHERE cd.STATUS = 'PUBLISHED') as published_count,
    COUNT(cd.IDX_DECLARATION) FILTER (WHERE cd.STATUS = 'REVOKED') as revoked_count,
    MAX(cd.CREATED_AT) as latest_declaration_date,
    MAX(cd.SIGNATURE_DATE) as latest_signature_date,
    (SELECT cd2.STATUS
     FROM GOVCONFORMITYDECLARATIONS cd2
     JOIN COMPLIANCEASSESSMENTS ca2 ON ca2.IDXCOMPLIANCEASSESSMENT = cd2.ASSESSMENT_ID
     WHERE ca2.IDXPROJECT = p.IDXPROJECT
     ORDER BY cd2.CREATED_AT DESC
     LIMIT 1) as latest_declaration_status
FROM PROJECTS p
LEFT JOIN COMPLIANCEASSESSMENTS ca ON ca.IDXPROJECT = p.IDXPROJECT
LEFT JOIN GOVCONFORMITYDECLARATIONS cd ON cd.ASSESSMENT_ID = ca.IDXCOMPLIANCEASSESSMENT
GROUP BY p.IDXPROJECT, p.PRJNAME, p.PRJISHIGHRISK;

-- ============================================================================
-- VISTA: Declaraciones firmadas listas para publicación
-- ============================================================================
-- Vista que muestra solo declaraciones firmadas que pueden ser publicadas
-- ============================================================================

CREATE OR REPLACE VIEW v_signed_declarations_ready_for_publication AS
SELECT
    cd.IDX_DECLARATION as declaration_id,
    cd.AI_SYSTEM_NAME,
    cd.AI_SYSTEM_VERSION,
    cd.PROVIDER_NAME,
    cd.SIGNATURE_DATE,
    cd.SIGNED_BY,
    cd.OVERALL_COMPLIANCE_SCORE,
    cd.COMPLIANCE_PERCENTAGE,
    p.IDXPROJECT as project_id,
    p.PRJNAME as project_name,
    cd.CREATED_AT,
    cd.UPDATED_AT
FROM GOVCONFORMITYDECLARATIONS cd
JOIN COMPLIANCEASSESSMENTS ca ON ca.IDXCOMPLIANCEASSESSMENT = cd.ASSESSMENT_ID
JOIN PROJECTS p ON p.IDXPROJECT = ca.IDXPROJECT
WHERE cd.STATUS = 'SIGNED'
  AND cd.SIGNATURE_DATE IS NOT NULL
  AND cd.SIGNED_BY IS NOT NULL
  AND cd.PDF_PATH IS NOT NULL
ORDER BY cd.SIGNATURE_DATE DESC;

-- ============================================================================
-- COMENTARIOS SOBRE VISTAS
-- ============================================================================
--
-- Estas vistas proporcionan:
-- 1. Consultas simplificadas para reportes
-- 2. Mejor performance al pre-calcular JOINs complejos
-- 3. Abstracción de la estructura de datos
-- 4. Facilidad para consultas desde herramientas de BI
--
-- NOTA: Las vistas pueden ser utilizadas directamente en JPA con @Query nativas
-- o desde procedimientos almacenados.
--
-- ============================================================================
