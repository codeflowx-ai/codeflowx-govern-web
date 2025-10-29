-- ============================================================================
-- Script: 09_dataset_quality_views.sql
-- Descripción: Vistas para Dataset Quality Dashboard
-- Fecha: 23 de Octubre de 2025
-- Metodología: EnArt (uppercase, no underscores, prefijos específicos)
-- ============================================================================

-- ============================================================================
-- Vista: VW_DATASET_QUALITY_DASHBOARD
-- Descripción: Dashboard para evaluaciones de calidad de datasets
-- ============================================================================

CREATE OR REPLACE VIEW VW_DATASET_QUALITY_DASHBOARD AS
SELECT 
    dq.IDXDATASETQUALITY,
    dq.DQLEVALUATIONID,
    dq.DQLDATASETNAME,
    dq.DQLDATASETPATH,
    dq.DQLSOURCETYPE,
    
    -- Scores
    dq.DQLOVERALLSCORE,
    dq.DQLQUALITYRATING,
    dq.DQLCOMPLETENESSCORE,
    dq.DQLVALIDITYSCORE,
    dq.DQLUNIQUENESSCORE,
    dq.DQLCONSISTENCYSCORE,
    
    -- Estadísticas
    dq.DQLTOTALROWS,
    dq.DQLTOTALCOLUMNS,
    dq.DQLMISSINGPERCENTAGE,
    dq.DQLOUTLIERSPERCENTAGE,
    dq.DQLDUPLICATESPERCENTAGE,
    
    -- Decisión
    dq.DQLDECISION,
    dq.DQLREQUIRESHUMANREVIEW,
    dq.DQLJUSTIFICATION,
    
    -- Metadata
    dq.DQLPROJECTID,
    dq.DQLCREATEDBY,
    dq.DQLCREATEDAT,
    dq.DQLSTATUS,
    dq.DQLEXECUTIONTIME,
    dq.DQLPROCESSINSTANCEID,
    
    -- Campos calculados
    EXTRACT(DAY FROM NOW() - dq.DQLCREATEDAT)::INTEGER AS DAYSSINCEEVAL,
    
    CASE 
        WHEN dq.DQLOVERALLSCORE >= 90 THEN 'EXCELLENT'
        WHEN dq.DQLOVERALLSCORE >= 75 THEN 'GOOD'
        WHEN dq.DQLOVERALLSCORE >= 60 THEN 'FAIR'
        ELSE 'POOR'
    END AS QUALITYLEVEL,
    
    CASE 
        WHEN dq.DQLDECISION = 'REVIEW_REQUIRED' AND EXTRACT(DAY FROM NOW() - dq.DQLCREATEDAT) > 2 THEN TRUE
        WHEN dq.DQLSTATUS = 'PROCESSING' AND EXTRACT(DAY FROM NOW() - dq.DQLCREATEDAT) > 1 THEN TRUE
        ELSE FALSE
    END AS ISOVERDUE,
    
    CASE 
        WHEN dq.DQLSTATUS = 'COMPLETED' THEN 'COMPLETED'
        WHEN dq.DQLSTATUS = 'FAILED' THEN 'FAILED'
        WHEN dq.DQLSTATUS LIKE 'PROCESSING%' THEN 'IN_PROGRESS'
        ELSE 'PENDING'
    END AS SIMPLESTATUS

FROM DQLDATASETQUALITY dq
ORDER BY dq.DQLCREATEDAT DESC;

-- Comentarios
COMMENT ON VIEW VW_DATASET_QUALITY_DASHBOARD IS 'Dashboard para evaluaciones de calidad de datasets';

-- ============================================================================
-- Vista: VW_DATASET_QUALITY_SUMMARY
-- Descripción: Resumen estadístico de evaluaciones por proyecto
-- ============================================================================

CREATE OR REPLACE VIEW VW_DATASET_QUALITY_SUMMARY AS
SELECT 
    dq.DQLPROJECTID,
    
    -- Totales
    COUNT(*) AS TOTALEVALUATIONS,
    COUNT(*) FILTER (WHERE dq.DQLDECISION = 'APPROVED') AS TOTALAPPROVED,
    COUNT(*) FILTER (WHERE dq.DQLDECISION = 'REJECTED') AS TOTALREJECTED,
    COUNT(*) FILTER (WHERE dq.DQLDECISION = 'REVIEW_REQUIRED') AS TOTALREVIEW,
    COUNT(*) FILTER (WHERE dq.DQLREQUIRESHUMANREVIEW = TRUE) AS TOTALHITL,
    
    -- Porcentajes
    ROUND((COUNT(*) FILTER (WHERE dq.DQLDECISION = 'APPROVED')::NUMERIC / NULLIF(COUNT(*), 0) * 100), 2) AS APPROVALRATE,
    ROUND((COUNT(*) FILTER (WHERE dq.DQLDECISION = 'REJECTED')::NUMERIC / NULLIF(COUNT(*), 0) * 100), 2) AS REJECTIONRATE,
    
    -- Promedios de scores
    ROUND(AVG(dq.DQLOVERALLSCORE)::NUMERIC, 2) AS AVGQUALITYSCORE,
    ROUND(AVG(dq.DQLCOMPLETENESSCORE)::NUMERIC, 3) AS AVGCOMPLETENESS,
    ROUND(AVG(dq.DQLVALIDITYSCORE)::NUMERIC, 3) AS AVGVALIDITY,
    ROUND(AVG(dq.DQLUNIQUENESSCORE)::NUMERIC, 3) AS AVGUNIQUENESS,
    
    -- Estadísticas de datos
    SUM(dq.DQLTOTALROWS) AS TOTALROWSPROCESSED,
    ROUND(AVG(dq.DQLMISSINGPERCENTAGE)::NUMERIC, 2) AS AVGMISSINGPCT,
    ROUND(AVG(dq.DQLOUTLIERSPERCENTAGE)::NUMERIC, 2) AS AVGOUTLIERSPCT,
    
    -- Tiempos
    ROUND(AVG(dq.DQLEXECUTIONTIME)::NUMERIC, 2) AS AVGEXECUTIONTIME,
    ROUND(MAX(dq.DQLEXECUTIONTIME)::NUMERIC, 2) AS MAXEXECUTIONTIME,
    
    -- Estados
    COUNT(*) FILTER (WHERE dq.DQLSTATUS = 'COMPLETED') AS TOTALCOMPLETED,
    COUNT(*) FILTER (WHERE dq.DQLSTATUS = 'FAILED') AS TOTALFAILED,
    COUNT(*) FILTER (WHERE dq.DQLSTATUS LIKE 'PROCESSING%') AS TOTALINPROGRESS,
    
    -- Última evaluación
    MAX(dq.DQLCREATEDAT) AS LASTEVALUATION

FROM DQLDATASETQUALITY dq
GROUP BY dq.DQLPROJECTID
ORDER BY TOTALEVALUATIONS DESC;

-- Comentarios
COMMENT ON VIEW VW_DATASET_QUALITY_SUMMARY IS 'Resumen estadístico de evaluaciones por proyecto';

-- ============================================================================
-- Vista: VW_DATASET_QUALITY_TRENDS
-- Descripción: Tendencias de calidad de datasets por día
-- ============================================================================

CREATE OR REPLACE VIEW VW_DATASET_QUALITY_TRENDS AS
SELECT 
    DATE(dq.DQLCREATEDAT) AS EVALUATIONDATE,
    dq.DQLPROJECTID,
    
    -- Conteos
    COUNT(*) AS DAILYEVALUATIONS,
    COUNT(*) FILTER (WHERE dq.DQLDECISION = 'APPROVED') AS DAILYAPPROVED,
    COUNT(*) FILTER (WHERE dq.DQLDECISION = 'REJECTED') AS DAILYREJECTED,
    
    -- Promedios
    ROUND(AVG(dq.DQLOVERALLSCORE)::NUMERIC, 2) AS AVGDAILYQUALITY,
    ROUND(AVG(dq.DQLCOMPLETENESSCORE)::NUMERIC, 3) AS AVGDAILYCOMPLETENESS,
    ROUND(AVG(dq.DQLVALIDITYSCORE)::NUMERIC, 3) AS AVGDAILYVALIDITY,
    
    -- Min/Max
    MIN(dq.DQLOVERALLSCORE) AS MINQUALITY,
    MAX(dq.DQLOVERALLSCORE) AS MAXQUALITY

FROM DQLDATASETQUALITY dq
WHERE dq.DQLCREATEDAT >= NOW() - INTERVAL '30 days'
GROUP BY DATE(dq.DQLCREATEDAT), dq.DQLPROJECTID
ORDER BY EVALUATIONDATE DESC, dq.DQLPROJECTID;

-- Comentarios
COMMENT ON VIEW VW_DATASET_QUALITY_TRENDS IS 'Tendencias diarias de calidad de datasets (últimos 30 días)';

-- ============================================================================
-- Vista: VW_DATASET_QUALITY_ISSUES
-- Descripción: Datasets con problemas que requieren atención
-- ============================================================================

CREATE OR REPLACE VIEW VW_DATASET_QUALITY_ISSUES AS
SELECT 
    dq.IDXDATASETQUALITY,
    dq.DQLEVALUATIONID,
    dq.DQLDATASETNAME,
    dq.DQLOVERALLSCORE,
    dq.DQLDECISION,
    dq.DQLREQUIRESHUMANREVIEW,
    dq.DQLJUSTIFICATION,
    dq.DQLMISSINGPERCENTAGE,
    dq.DQLOUTLIERSPERCENTAGE,
    dq.DQLCREATEDAT,
    dq.DQLPROJECTID,
    dq.DQLSTATUS,
    
    -- Categoría del problema
    CASE 
        WHEN dq.DQLSTATUS = 'FAILED' THEN 'EVALUATION_FAILED'
        WHEN dq.DQLDECISION = 'REJECTED' THEN 'REJECTED'
        WHEN dq.DQLMISSINGPERCENTAGE > 15 THEN 'HIGH_MISSING_VALUES'
        WHEN dq.DQLOUTLIERSPERCENTAGE > 10 THEN 'HIGH_OUTLIERS'
        WHEN dq.DQLOVERALLSCORE < 75 THEN 'LOW_QUALITY'
        WHEN dq.DQLREQUIRESHUMANREVIEW = TRUE THEN 'NEEDS_REVIEW'
        ELSE 'OTHER'
    END AS ISSUECATEGORY,
    
    -- Severidad
    CASE 
        WHEN dq.DQLSTATUS = 'FAILED' THEN 'CRITICAL'
        WHEN dq.DQLDECISION = 'REJECTED' THEN 'HIGH'
        WHEN dq.DQLOVERALLSCORE < 60 THEN 'HIGH'
        WHEN dq.DQLOVERALLSCORE < 75 THEN 'MEDIUM'
        ELSE 'LOW'
    END AS ISSUESEVERITY,
    
    EXTRACT(DAY FROM NOW() - dq.DQLCREATEDAT)::INTEGER AS DAYSOPEN

FROM DQLDATASETQUALITY dq
WHERE 
    dq.DQLSTATUS = 'FAILED'
    OR dq.DQLDECISION IN ('REJECTED', 'REVIEW_REQUIRED')
    OR dq.DQLOVERALLSCORE < 75
    OR dq.DQLMISSINGPERCENTAGE > 15
    OR dq.DQLOUTLIERSPERCENTAGE > 10
    OR dq.DQLREQUIRESHUMANREVIEW = TRUE
ORDER BY 
    CASE 
        WHEN dq.DQLSTATUS = 'FAILED' THEN 1
        WHEN dq.DQLDECISION = 'REJECTED' THEN 2
        WHEN dq.DQLOVERALLSCORE < 60 THEN 3
        ELSE 4
    END,
    dq.DQLCREATEDAT DESC;

-- Comentarios
COMMENT ON VIEW VW_DATASET_QUALITY_ISSUES IS 'Datasets con problemas que requieren atención';

-- ============================================================================
-- FIN DEL SCRIPT
-- ============================================================================

