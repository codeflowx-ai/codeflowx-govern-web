-- ============================================================================
-- TRIGGERS AUTOMÁTICOS - CodeFlowX Govern Platform
-- ============================================================================
-- Este script crea triggers para actualizar automáticamente campos de auditoría
-- con CUALQUIER prefijo (PRM, AGT, MOD, TRN, DSH, COR, etc.)
-- ============================================================================

-- Habilitar extensión hstore para manipulación de ROW
CREATE EXTENSION IF NOT EXISTS hstore;

-- ============================================================================
-- FUNCIÓN GENÉRICA: Actualizar timestamps automáticamente
-- ============================================================================

CREATE OR REPLACE FUNCTION update_audit_timestamps()
RETURNS TRIGGER AS $$
DECLARE
    col_names TEXT[];
    col_name TEXT;
    new_hstore hstore;
BEGIN
    new_hstore := hstore(NEW);
    
    -- En INSERT: Establecer *CREATEDAT y *UPDATEDAT
    IF TG_OP = 'INSERT' THEN
        -- Obtener todas las columnas que terminan en 'createdat' o 'updatedat'
        SELECT array_agg(column_name::text) INTO col_names
            FROM information_schema.columns 
        WHERE table_schema = TG_TABLE_SCHEMA 
          AND table_name = TG_TABLE_NAME 
          AND (lower(column_name) LIKE '%createdat' OR lower(column_name) LIKE '%updatedat');
        
        -- Actualizar cada columna encontrada
        IF col_names IS NOT NULL THEN
            FOREACH col_name IN ARRAY col_names LOOP
                -- Si la columna es NULL o no existe en NEW, setearla a NOW()
                IF NOT new_hstore ? col_name OR new_hstore -> col_name IS NULL THEN
                    new_hstore := new_hstore || hstore(col_name, NOW()::text);
                END IF;
            END LOOP;
        END IF;
    END IF;
    
    -- En UPDATE: Actualizar solo *UPDATEDAT
    IF TG_OP = 'UPDATE' THEN
        SELECT array_agg(column_name::text) INTO col_names
            FROM information_schema.columns 
        WHERE table_schema = TG_TABLE_SCHEMA 
          AND table_name = TG_TABLE_NAME 
          AND lower(column_name) LIKE '%updatedat';
        
        IF col_names IS NOT NULL THEN
            FOREACH col_name IN ARRAY col_names LOOP
                new_hstore := new_hstore || hstore(col_name, NOW()::text);
            END LOOP;
        END IF;
    END IF;
    
    -- Convertir hstore de vuelta a ROW
    NEW := populate_record(NEW, new_hstore);
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION update_audit_timestamps() IS 
    'Actualiza automáticamente campos *CREATEDAT y *UPDATEDAT con cualquier prefijo';

-- ============================================================================
-- TRIGGERS POR TABLA
-- ============================================================================

-- CORE
DROP TRIGGER IF EXISTS trg_audit_cordepartments ON CORDEPARTMENTS;
CREATE TRIGGER trg_audit_cordepartments BEFORE INSERT OR UPDATE ON CORDEPARTMENTS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_corroles ON CORROLES;
CREATE TRIGGER trg_audit_corroles BEFORE INSERT OR UPDATE ON CORROLES
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_corusers ON CORUSERS;
CREATE TRIGGER trg_audit_corusers BEFORE INSERT OR UPDATE ON CORUSERS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_corpermissions ON CORPERMISSIONS;
CREATE TRIGGER trg_audit_corpermissions BEFORE INSERT OR UPDATE ON CORPERMISSIONS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_cormenus ON CORMENUS;
CREATE TRIGGER trg_audit_cormenus BEFORE INSERT OR UPDATE ON CORMENUS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_corusersessions ON CORUSERSESSIONS;
CREATE TRIGGER trg_audit_corusersessions BEFORE INSERT OR UPDATE ON CORUSERSESSIONS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

-- PROJECTS  
DROP TRIGGER IF EXISTS trg_audit_prjprojects ON PRJPROJECTS;
CREATE TRIGGER trg_audit_prjprojects BEFORE INSERT OR UPDATE ON PRJPROJECTS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

-- PROMPTS
DROP TRIGGER IF EXISTS trg_audit_prmprompts ON PRMPROMPTS;
CREATE TRIGGER trg_audit_prmprompts BEFORE INSERT OR UPDATE ON PRMPROMPTS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

-- AGENTS
DROP TRIGGER IF EXISTS trg_audit_agtagents ON AGTAGENTS;
CREATE TRIGGER trg_audit_agtagents BEFORE INSERT OR UPDATE ON AGTAGENTS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_agtagentalerts ON AGTAGENTALERTS;
CREATE TRIGGER trg_audit_agtagentalerts BEFORE INSERT OR UPDATE ON AGTAGENTALERTS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

-- MODELS
DROP TRIGGER IF EXISTS trg_audit_modmodels ON MODMODELS;
CREATE TRIGGER trg_audit_modmodels BEFORE INSERT OR UPDATE ON MODMODELS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

-- TRAINING
DROP TRIGGER IF EXISTS trg_audit_trnexperiments ON TRNEXPERIMENTS;
CREATE TRIGGER trg_audit_trnexperiments BEFORE INSERT OR UPDATE ON TRNEXPERIMENTS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_trnruns ON TRNRUNS;
CREATE TRIGGER trg_audit_trnruns BEFORE INSERT OR UPDATE ON TRNRUNS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

-- DASHBOARD
DROP TRIGGER IF EXISTS trg_audit_dshmodulestats ON DSHMODULESTATS;
CREATE TRIGGER trg_audit_dshmodulestats BEFORE INSERT OR UPDATE ON DSHMODULESTATS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_dshdistributions ON DSHDISTRIBUTIONS;
CREATE TRIGGER trg_audit_dshdistributions BEFORE INSERT OR UPDATE ON DSHDISTRIBUTIONS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_dshtimeseries ON DSHTIMESERIES;
CREATE TRIGGER trg_audit_dshtimeseries BEFORE INSERT OR UPDATE ON DSHTIMESERIES
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_dshmoduleactivity ON DSHMODULEACTIVITY;
CREATE TRIGGER trg_audit_dshmoduleactivity BEFORE INSERT OR UPDATE ON DSHMODULEACTIVITY
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_dshtokenmetrics ON DSHTOKENMETRICS;
CREATE TRIGGER trg_audit_dshtokenmetrics BEFORE INSERT OR UPDATE ON DSHTOKENMETRICS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_dshcostmetrics ON DSHCOSTMETRICS;
CREATE TRIGGER trg_audit_dshcostmetrics BEFORE INSERT OR UPDATE ON DSHCOSTMETRICS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

DROP TRIGGER IF EXISTS trg_audit_dshquickactions ON DSHQUICKACTIONS;
CREATE TRIGGER trg_audit_dshquickactions BEFORE INSERT OR UPDATE ON DSHQUICKACTIONS
    FOR EACH ROW EXECUTE FUNCTION update_audit_timestamps();

-- ============================================================================
-- FIN DE TRIGGERS
-- ============================================================================

COMMENT ON EXTENSION hstore IS 'Extensión para manipular registros como key-value pairs';

-- Verificar triggers creados
SELECT 
    trigger_name,
    event_object_table as table_name,
    action_timing,
    event_manipulation
FROM information_schema.triggers
WHERE trigger_schema = 'public'
  AND trigger_name LIKE 'trg_audit_%'
ORDER BY event_object_table;
