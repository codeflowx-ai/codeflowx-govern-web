-- ============================================================================
-- SCRIPT DE CREACIÓN DE VISTAS
-- Módulo: Technical Documentation
-- Tabla: GOVAIACTTECHNICALDOCS
-- Fecha: Diciembre 2025
-- ============================================================================

-- Este script crea vistas útiles para consultas frecuentes sobre documentación técnica

-- ============================================================================
-- VISTA: Resumen de documentaciones técnicas por modelo
-- ============================================================================

CREATE OR REPLACE VIEW vw_techdocs_model_summary AS
SELECT
    d.IDX_TECHNICAL_DOC AS doc_id,
    d.ENTITY_TYPE,
    d.ENTITY_ID AS model_id,
    d.SYSTEM_NAME,
    d.VERSION,
    d.STATUS,
    calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) AS completeness_score,
    is_techdocs_complete(d.IDX_TECHNICAL_DOC) AS is_complete,
    FLOOR(calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) * 11)::INTEGER AS completed_sections,
    11 AS total_sections,
    d.PDF_PATH AS pdf_url,
    d.CREATED_AT,
    d.UPDATED_AT,
    d.GENERATED_AT
FROM GOVAIACTTECHNICALDOCS d
WHERE d.ENTITY_TYPE = 'MODEL';

COMMENT ON VIEW vw_techdocs_model_summary IS
'Vista que proporciona resumen de documentaciones técnicas por modelo, incluyendo score de completitud';

-- ============================================================================
-- VISTA: Documentaciones pendientes de completar
-- ============================================================================

CREATE OR REPLACE VIEW vw_techdocs_pending AS
SELECT
    d.IDX_TECHNICAL_DOC AS doc_id,
    d.ENTITY_TYPE,
    d.ENTITY_ID,
    d.SYSTEM_NAME,
    d.VERSION,
    d.STATUS,
    calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) AS completeness_score,
    (11 - FLOOR(calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) * 11))::INTEGER AS missing_sections,
    CASE
        WHEN calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) < 0.5 THEN 'CRITICAL'
        WHEN calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) < 0.8 THEN 'HIGH'
        ELSE 'MEDIUM'
    END AS priority,
    d.CREATED_AT,
    d.UPDATED_AT,
    EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - d.UPDATED_AT)) / 86400 AS days_since_update
FROM GOVAIACTTECHNICALDOCS d
WHERE calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) < 1.00
ORDER BY
    CASE
        WHEN calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) < 0.5 THEN 1
        WHEN calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC) < 0.8 THEN 2
        ELSE 3
    END,
    d.UPDATED_AT ASC;

COMMENT ON VIEW vw_techdocs_pending IS
'Vista de documentaciones incompletas con prioridad y días desde última actualización';

-- ============================================================================
-- VISTA: Estadísticas de completitud por tipo de entidad
-- ============================================================================

CREATE OR REPLACE VIEW vw_techdocs_statistics_by_entity_type AS
SELECT
    d.ENTITY_TYPE,
    COUNT(*) AS total_docs,
    COUNT(*) FILTER (WHERE is_techdocs_complete(d.IDX_TECHNICAL_DOC)) AS complete_docs,
    COUNT(*) FILTER (WHERE NOT is_techdocs_complete(d.IDX_TECHNICAL_DOC)) AS incomplete_docs,
    ROUND(AVG(calculate_techdocs_completeness_score(d.IDX_TECHNICAL_DOC))::NUMERIC, 4) AS avg_completeness_score,
    ROUND(
        (COUNT(*) FILTER (WHERE is_techdocs_complete(d.IDX_TECHNICAL_DOC))::NUMERIC / COUNT(*)::NUMERIC * 100)::NUMERIC,
        2
    ) AS completion_percentage
FROM GOVAIACTTECHNICALDOCS d
GROUP BY d.ENTITY_TYPE;

COMMENT ON VIEW vw_techdocs_statistics_by_entity_type IS
'Vista con estadísticas de completitud agrupadas por tipo de entidad';

-- ============================================================================
-- VISTA: Documentaciones aprobadas listas para evaluación de conformidad
-- ============================================================================

CREATE OR REPLACE VIEW vw_techdocs_approved_for_conformity AS
SELECT
    d.IDX_TECHNICAL_DOC AS doc_id,
    d.ENTITY_TYPE,
    d.ENTITY_ID,
    d.SYSTEM_NAME,
    d.VERSION,
    d.PDF_PATH AS pdf_url,
    d.GENERATED_AT AS approved_at,
    d.UPDATED_AT,
    CASE
        WHEN d.GENERATED_AT IS NOT NULL THEN EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - d.GENERATED_AT)) / 86400
        ELSE NULL
    END AS days_since_approval
FROM GOVAIACTTECHNICALDOCS d
WHERE d.STATUS = 'APPROVED'
  AND is_techdocs_complete(d.IDX_TECHNICAL_DOC)
ORDER BY d.GENERATED_AT DESC;

COMMENT ON VIEW vw_techdocs_approved_for_conformity IS
'Vista de documentaciones aprobadas y completas, listas para iniciar evaluación de conformidad';

-- ============================================================================
-- VERIFICACIÓN
-- ============================================================================

-- Consulta para verificar vistas creadas
-- SELECT
--     table_name AS view_name,
--     view_definition
-- FROM information_schema.views
-- WHERE table_schema = 'public'
--   AND table_name LIKE '%techdocs%'
-- ORDER BY table_name;

