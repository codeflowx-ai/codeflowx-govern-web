-- ============================================================================
-- VIEWS - MODELS MODULE
-- ============================================================================

-- Vista para listado de modelos con todas las FKs resueltas
-- Primero eliminar si existe para poder recrear con nuevos tipos
DROP VIEW IF EXISTS V_MODELS_OVERVIEW CASCADE;

CREATE VIEW V_MODELS_OVERVIEW AS
SELECT 
    -- Campos del modelo (SOLO LOS QUE EXISTEN EN MODMODELS)
    m.IDXMODEL,
    m.MODNAME,
    m.MODDESCRIPTION,
    m.MODVERSION,
    m.MODTYPE,
    m.MODFRAMEWORK,
    m.MODSTATUS,
    m.MODAPPROVALSTATUS,
    m.MODPERFORMANCEMETRICS,
    m.MODTRAININGCONFIG,
    m.MODDEPLOYMENTCONFIG,
    m.MODAPPROVEDBY,
    m.MODAPPROVEDAT,
    m.MODCREATEDBY,
    m.MODUPDATEDBY,
    m.MODCREATEDAT,
    m.MODUPDATEDAT,
    m.MODCURRENTSTAGE,
    m.MODLIFECYCLESTAGE,
    m.MODTAGS,
    m.MODUSERID,
    m.MODCREATIONTIMESTAMP,
    m.MODLASTUPDATEDTIMESTAMP,
    
    -- FK: Provider (todas las columnas necesarias para el listado)
    m.IDMODPROVIDER,
    p.MODNAME AS MODPROVIDERNAME,
    p.MODDISPLAYNAME AS PROVIDER_DISPLAY_NAME,
    p.MODPROVIDERTYPE,
    p.MODBASEURL AS MODPROVIDERURL,
    p.MODSTATUS AS PROVIDER_STATUS,
    
    -- Contadores calculados (mejora performance)
    (SELECT COUNT(*) FROM MODMODELVERSIONS mv WHERE mv.IDMODMODELS0 = m.IDXMODEL) AS TOTAL_VERSIONS,
    (SELECT COUNT(*) FROM MODMODELARTIFACTS ma WHERE ma.IDMODMODELS0 = m.IDXMODEL) AS TOTAL_ARTIFACTS,
    
    -- Última métrica de accuracy
    (SELECT mp.MODMETRICVALUE 
     FROM MODMODELPERFORMANCES mp 
     WHERE mp.IDMODMODELS0 = m.IDXMODEL 
       AND mp.MODMETRICNAME = 'ACCURACY' 
     ORDER BY mp.MODCREATEDAT DESC 
     LIMIT 1) AS LAST_ACCURACY

FROM MODMODELS m
LEFT JOIN MODPROVIDERS p ON m.IDMODPROVIDER = p.IDXMODELPROVIDER;

-- Comentario en la vista
COMMENT ON VIEW V_MODELS_OVERVIEW IS 'Vista optimizada para listado de modelos con proveedores y contadores';


-- ============================================================================
-- Vista para métricas globales del módulo Models
-- ============================================================================

DROP VIEW IF EXISTS V_MODELS_METRICS_SUMMARY CASCADE;

CREATE VIEW V_MODELS_METRICS_SUMMARY AS
SELECT 
    -- Contadores totales
    COUNT(*) AS TOTAL_MODELS,
    COUNT(*) FILTER (WHERE 'ACTIVE' = ANY(MODSTATUS)) AS ACTIVE_MODELS,
    COUNT(*) FILTER (WHERE 'PENDING_APPROVAL' = ANY(MODAPPROVALSTATUS)) AS PENDING_APPROVAL,
    COUNT(*) FILTER (WHERE 'REJECTED' = ANY(MODAPPROVALSTATUS)) AS REJECTED_MODELS,
    COUNT(*) FILTER (WHERE 'APPROVED' = ANY(MODAPPROVALSTATUS)) AS APPROVED_MODELS,
    
    -- Métricas calculadas
    (SELECT AVG(mp.MODMETRICVALUE) 
     FROM MODMODELPERFORMANCES mp 
     WHERE mp.MODMETRICNAME = 'ACCURACY') AS AVERAGE_ACCURACY,
    
    (SELECT AVG(mba.MODBIASSCORE)
     FROM MODMODELBIASANALYSES mba) AS AVERAGE_BIAS_SCORE,
    
    -- Totales de versiones y artifacts
    (SELECT COUNT(*) FROM MODMODELVERSIONS) AS TOTAL_VERSIONS_ALL,
    (SELECT COUNT(*) FROM MODMODELARTIFACTS) AS TOTAL_ARTIFACTS_ALL,
    
    -- Timestamp de generación
    NOW() AS GENERATED_AT

FROM MODMODELS m;

COMMENT ON VIEW V_MODELS_METRICS_SUMMARY IS 'Vista para métricas globales del catálogo de modelos';

