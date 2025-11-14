-- ==================================================
-- VIEW: V_MODEL_LINEAGE_TREE
-- ==================================================
-- Fecha: Noviembre 2025
-- Objetivo: Vista recursiva para visualizar árbol completo de linaje de modelos
-- Permite tracking de Base Model → Adapter → Adapter específico, etc.

DROP VIEW IF EXISTS V_MODEL_LINEAGE_TREE CASCADE;

CREATE OR REPLACE VIEW V_MODEL_LINEAGE_TREE AS
WITH RECURSIVE model_tree AS (
    -- Base case: modelos sin parent (root models - modelos base originales)
    SELECT 
        IDXMODEL as model_id,
        MODNAME as model_name,
        MODTYPE as model_type,
        MODISGPAI as is_gpai,
        IDMODBASEMODEL as parent_id,
        MODADAPTATIONSTRATEGY as adaptation_strategy,
        MODISADAPTER as is_adapter,
        MODISFINETUNED as is_finetuned,
        MODISQUANTIZED as is_quantized,
        MODISMERGED as is_merged,
        MODADAPTERCONFIG as adapter_config,
        MODADAPTATIONMETADATA as adaptation_metadata,
        1 as depth,
        ARRAY[IDXMODEL] as path,
        IDXMODEL as root_model_id,
        MODCREATEDAT as created_at
    FROM MODMODELS
    WHERE IDMODBASEMODEL IS NULL
    
    UNION ALL
    
    -- Recursive case: modelos derivados (adapters, fine-tuned, etc.)
    SELECT 
        m.IDXMODEL,
        m.MODNAME,
        m.MODTYPE,
        m.MODISGPAI,
        m.IDMODBASEMODEL,
        m.MODADAPTATIONSTRATEGY,
        m.MODISADAPTER,
        m.MODISFINETUNED,
        m.MODISQUANTIZED,
        m.MODISMERGED,
        m.MODADAPTERCONFIG,
        m.MODADAPTATIONMETADATA,
        mt.depth + 1,
        mt.path || m.IDXMODEL,
        mt.root_model_id,
        m.MODCREATEDAT
    FROM MODMODELS m
    INNER JOIN model_tree mt ON m.IDMODBASEMODEL = mt.model_id
    WHERE NOT m.IDXMODEL = ANY(mt.path)  -- Evitar ciclos infinitos
      AND mt.depth < 10  -- Límite de profundidad
)
SELECT 
    model_id,
    model_name,
    model_type,
    is_gpai,
    parent_id,
    adaptation_strategy,
    is_adapter,
    is_finetuned,
    is_quantized,
    is_merged,
    adapter_config,
    adaptation_metadata,
    depth,
    path,
    root_model_id,
    array_length(path, 1) as lineage_length,
    CASE 
        WHEN depth = 1 THEN 'ROOT'
        WHEN depth = 2 THEN 'DERIVATIVE'
        WHEN depth = 3 THEN 'NESTED_DERIVATIVE'
        ELSE 'DEEP_DERIVATIVE'
    END as lineage_type,
    created_at,
    -- Campos calculados
    (SELECT MODNAME FROM MODMODELS WHERE IDXMODEL = root_model_id) as root_model_name,
    (SELECT MODNAME FROM MODMODELS WHERE IDXMODEL = parent_id) as parent_model_name
FROM model_tree
ORDER BY root_model_id, path;

-- Comentarios
COMMENT ON VIEW V_MODEL_LINEAGE_TREE IS 'Vista recursiva para árbol de linaje de modelos - tracking adaptación GPAI';

-- ==================================================
-- QUERIES ÚTILES
-- ==================================================

-- 1. Obtener árbol completo de un modelo (incluyendo ancestros y descendientes)
-- SELECT * FROM V_MODEL_LINEAGE_TREE 
-- WHERE root_model_id = (SELECT root_model_id FROM V_MODEL_LINEAGE_TREE WHERE model_id = 123);

-- 2. Ver solo derivados directos de un modelo
-- SELECT * FROM V_MODEL_LINEAGE_TREE WHERE parent_id = 123;

-- 3. Ver profundidad máxima de adaptación
-- SELECT root_model_id, root_model_name, MAX(depth) as max_depth, COUNT(*) as total_derivatives
-- FROM V_MODEL_LINEAGE_TREE
-- GROUP BY root_model_id, root_model_name
-- ORDER BY max_depth DESC;

-- 4. Ver todos los adapters en el sistema
-- SELECT model_id, model_name, parent_model_name, depth
-- FROM V_MODEL_LINEAGE_TREE
-- WHERE is_adapter = TRUE
-- ORDER BY created_at DESC;

-- 5. Estadísticas de adaptación por estrategia
-- SELECT 
--   adaptation_strategy,
--   COUNT(*) as total,
--   AVG((adaptation_metadata->>'cost_usd')::DECIMAL) as avg_cost,
--   AVG((adaptation_metadata->>'co2_kg')::DECIMAL) as avg_co2,
--   AVG(depth) as avg_lineage_depth
-- FROM V_MODEL_LINEAGE_TREE
-- WHERE adaptation_strategy IS NOT NULL
-- GROUP BY adaptation_strategy
-- ORDER BY total DESC;

-- 6. Verificar si crear adaptación crearía ciclo (función auxiliar)
CREATE OR REPLACE FUNCTION check_would_create_cycle(
    p_base_model_id BIGINT,
    p_new_model_id BIGINT
) RETURNS BOOLEAN AS $$
BEGIN
    -- Verifica si new_model está en el path de base_model (crearía ciclo)
    RETURN EXISTS (
        SELECT 1 FROM V_MODEL_LINEAGE_TREE 
        WHERE root_model_id = p_new_model_id 
        AND p_base_model_id = ANY(path)
    );
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION check_would_create_cycle IS 'Verifica si crear adaptación crearía ciclo en lineage tree';

-- Ejemplo uso:
-- SELECT check_would_create_cycle(456, 123);  -- TRUE si crearía ciclo

-- ==================================================
-- MATERIALIZED VIEW (OPCIONAL - Para performance)
-- ==================================================

-- Si el árbol crece mucho, crear materialized view
-- CREATE MATERIALIZED VIEW MV_MODEL_LINEAGE_TREE AS
-- SELECT * FROM V_MODEL_LINEAGE_TREE;
--
-- CREATE INDEX idx_mv_lineage_model ON MV_MODEL_LINEAGE_TREE(model_id);
-- CREATE INDEX idx_mv_lineage_root ON MV_MODEL_LINEAGE_TREE(root_model_id);
--
-- -- Refresh periódico
-- REFRESH MATERIALIZED VIEW MV_MODEL_LINEAGE_TREE;

-- ==================================================
-- FIN SCRIPT
-- ==================================================


