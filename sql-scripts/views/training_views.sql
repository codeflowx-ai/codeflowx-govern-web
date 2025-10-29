-- ====================================================================
-- VISTAS SQL PARA TRAINING MODULE
-- ====================================================================

-- Vista principal: Training Overview
-- Combina información de experimentos con sus runs y métricas agregadas
CREATE OR REPLACE VIEW training_overview AS
SELECT 
    e.IDXEXPERIMENT,
    e.TRNNAME,
    e.TRNDESCRIPTION,
    e.TRNARTIFACTLOCATION,
    e.TRNLIFECYCLESTAGE,
    e.TRNTAGS,
    e.TRNMETADATA,
    e.TRNCREATEDAT,
    e.TRNUPDATEDAT,
    e.TRNCREATEDBY,
    e.TRNUPDATEDBY,
    
    -- Contadores de runs
    COUNT(DISTINCT r.IDXRUN) AS total_runs,
    COUNT(DISTINCT CASE WHEN 'RUNNING' = ANY(r.TRNSTATUS) THEN r.IDXRUN END) AS running_runs,
    COUNT(DISTINCT CASE WHEN 'COMPLETED' = ANY(r.TRNSTATUS) THEN r.IDXRUN END) AS completed_runs,
    COUNT(DISTINCT CASE WHEN 'FAILED' = ANY(r.TRNSTATUS) THEN r.IDXRUN END) AS failed_runs,
    
    -- Métricas del mejor run
    MAX(CASE WHEN tm.TRNMETRICNAME = 'accuracy' THEN tm.TRNMETRICVALUE END) AS best_accuracy,
    MAX(CASE WHEN tm.TRNMETRICNAME = 'loss' THEN tm.TRNMETRICVALUE END) AS best_loss,
    
    -- Información de artefactos
    COUNT(DISTINCT ta.IDXTRAININGARTIFACT) AS total_artifacts,
    
    -- Información de infraestructura
    COUNT(DISTINCT ti.IDXTRAININGINFRASTRUCTURE) AS total_infrastructure_configs,
    
    -- Duración promedio de runs (en minutos)
    AVG(EXTRACT(EPOCH FROM (r.TRNENDTIME - r.TRNSTARTTIME)) / 60) AS avg_run_duration_minutes,
    
    -- Última ejecución
    MAX(r.TRNSTARTTIME) AS last_run_date,
    
    -- Estado de governance
    MAX(tg.TRNCOMPLIANCESCORE) AS compliance_score

FROM TRNEXPERIMENTS e
LEFT JOIN TRNRUNS r ON e.IDXEXPERIMENT = r.IDXEXPERIMENT
LEFT JOIN TRNTRAININGMETRICS tm ON r.IDXRUN = tm.IDXRUN
LEFT JOIN TRNTRAININGARTIFACTS ta ON r.IDXRUN = ta.IDXRUN
LEFT JOIN TRNTRAININGINFRASTRUCTURE ti ON r.IDXRUN = ti.IDXRUN
LEFT JOIN TRNTRAININGGOVERNANCE tg ON r.IDXRUN = tg.IDXRUN
GROUP BY 
    e.IDXEXPERIMENT,
    e.TRNNAME,
    e.TRNDESCRIPTION,
    e.TRNARTIFACTLOCATION,
    e.TRNLIFECYCLESTAGE,
    e.TRNTAGS,
    e.TRNMETADATA,
    e.TRNCREATEDAT,
    e.TRNUPDATEDAT,
    e.TRNCREATEDBY,
    e.TRNUPDATEDBY;

-- Vista de métricas agregadas para el dashboard
CREATE OR REPLACE VIEW training_metrics_summary AS
SELECT 
    COUNT(DISTINCT e.IDXEXPERIMENT) AS total_experiments,
    COUNT(DISTINCT r.IDXRUN) AS total_runs,
    COUNT(DISTINCT CASE WHEN r.TRNSTATUS = 'RUNNING' THEN r.IDXRUN END) AS active_runs,
    COUNT(DISTINCT CASE WHEN r.TRNSTATUS = 'COMPLETED' THEN r.IDXRUN END) AS completed_runs,
    COUNT(DISTINCT CASE WHEN r.TRNSTATUS = 'FAILED' THEN r.IDXRUN END) AS failed_runs,
    
    -- Métricas promedio
    AVG(tm.TRNMETRICVALUE) FILTER (WHERE tm.TRNMETRICNAME = 'accuracy') AS avg_accuracy,
    MAX(tm.TRNMETRICVALUE) FILTER (WHERE tm.TRNMETRICNAME = 'accuracy') AS max_accuracy,
    AVG(tm.TRNMETRICVALUE) FILTER (WHERE tm.TRNMETRICNAME = 'loss') AS avg_loss,
    MIN(tm.TRNMETRICVALUE) FILTER (WHERE tm.TRNMETRICNAME = 'loss') AS min_loss,
    
    -- Recursos utilizados
    COUNT(DISTINCT ti.IDXTRAININGINFRASTRUCTURE) AS total_infrastructure_used,
    SUM(ti.TRNTOTALCOST) AS total_training_cost,
    
    -- Artefactos generados
    COUNT(DISTINCT ta.IDXTRAININGARTIFACT) AS total_artifacts_generated,
    
    -- Compliance
    AVG(tg.TRNCOMPLIANCESCORE) AS avg_compliance_score,
    COUNT(DISTINCT CASE WHEN tg.TRNCOMPLIANCESTATUS = 'COMPLIANT' THEN tg.IDXTRAININGGOVERNANCE END) AS compliant_runs

FROM TRNEXPERIMENTS e
LEFT JOIN TRNRUNS r ON e.IDXEXPERIMENT = r.IDXRUN
LEFT JOIN TRNTRAININGMETRICS tm ON r.IDXRUN = tm.IDXRUN
LEFT JOIN TRNTRAININGINFRASTRUCTURE ti ON r.IDXRUN = ti.IDXRUN
LEFT JOIN TRNTRAININGARTIFACTS ta ON r.IDXRUN = ta.IDXRUN
LEFT JOIN TRNTRAININGGOVERNANCE tg ON r.IDXRUN = tg.IDXRUN;

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_training_overview_lifecycle ON TRNEXPERIMENTS(TRNLIFECYCLESTAGE);
CREATE INDEX IF NOT EXISTS idx_training_overview_created ON TRNEXPERIMENTS(TRNCREATEDAT DESC);
CREATE INDEX IF NOT EXISTS idx_runs_experiment ON TRNRUNS(IDXEXPERIMENT);
CREATE INDEX IF NOT EXISTS idx_runs_status ON TRNRUNS(TRNSTATUS);

