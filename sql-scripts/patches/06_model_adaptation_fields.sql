-- ==================================================
-- MIGRATION: Model Adaptation Fields - Art. 51-55 GPAI
-- ==================================================
-- Fecha: Noviembre 2025
-- Objetivo: Añadir campos de adaptación de modelos (Adapters, Fine-Tuning, Quantization, Merge)
-- Artículos: Art. 51-55 EU AI Act - GPAI Downstream Providers

-- Tipos de Adaptación (Flags Booleanos)
ALTER TABLE MODMODELS ADD COLUMN MODISADAPTER BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN MODMODELS.MODISADAPTER IS 'Indica si el modelo es un adapter (LoRA/QLoRA) - Art. 53';

ALTER TABLE MODMODELS ADD COLUMN MODISFINETUNED BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN MODMODELS.MODISFINETUNED IS 'Indica si el modelo ha sido fine-tuned completamente - Art. 53';

ALTER TABLE MODMODELS ADD COLUMN MODISQUANTIZED BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN MODMODELS.MODISQUANTIZED IS 'Indica si el modelo ha sido quantized (4-bit, 8-bit, etc.)';

ALTER TABLE MODMODELS ADD COLUMN MODISMERGED BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN MODMODELS.MODISMERGED IS 'Indica si el modelo es resultado de merge (SLERP, etc.)';

-- Estrategia de Adaptación
ALTER TABLE MODMODELS ADD COLUMN MODADAPTATIONSTRATEGY VARCHAR(50);
COMMENT ON COLUMN MODMODELS.MODADAPTATIONSTRATEGY IS 'Estrategia aplicada: ADAPTER_LORA, ADAPTER_QLORA, FINE_TUNING, QUANTIZATION, MERGE, NONE';

-- Modelo Base (FK self-referencing para linaje)
ALTER TABLE MODMODELS ADD COLUMN IDMODBASEMODEL BIGINT;
COMMENT ON COLUMN MODMODELS.IDMODBASEMODEL IS 'FK al modelo base original (si es adaptación/derivado)';

-- Constraint FK self-referencing
ALTER TABLE MODMODELS ADD CONSTRAINT fk_mod_basemodel 
    FOREIGN KEY (IDMODBASEMODEL) REFERENCES MODMODELS(IDXMODEL) 
    ON DELETE SET NULL;

CREATE INDEX idx_mod_basemodel ON MODMODELS(IDMODBASEMODEL) WHERE IDMODBASEMODEL IS NOT NULL;

-- Configuraciones Adaptación (JSONB para configs complejas)
ALTER TABLE MODMODELS ADD COLUMN MODADAPTERCONFIG JSONB;
COMMENT ON COLUMN MODMODELS.MODADAPTERCONFIG IS 'Configuración adapter: {rank, alpha, target_modules, dropout, ...}';

ALTER TABLE MODMODELS ADD COLUMN MODFINETUNINGCONFIG JSONB;
COMMENT ON COLUMN MODMODELS.MODFINETUNINGCONFIG IS 'Configuración fine-tuning: {epochs, batch_size, lr, optimizer, ...}';

ALTER TABLE MODMODELS ADD COLUMN MODQUANTIZATIONCONFIG JSONB;
COMMENT ON COLUMN MODMODELS.MODQUANTIZATIONCONFIG IS 'Configuración quantization: {bits, method, group_size, ...}';

ALTER TABLE MODMODELS ADD COLUMN MODMERGECONFIG JSONB;
COMMENT ON COLUMN MODMODELS.MODMERGECONFIG IS 'Configuración merge: {method, models, weights, ...}';

-- Metadata Adaptación (costos, tiempos, performance)
ALTER TABLE MODMODELS ADD COLUMN MODADAPTATIONMETADATA JSONB;
COMMENT ON COLUMN MODMODELS.MODADAPTATIONMETADATA IS 'Metadata adaptación: {cost_usd, co2_kg, time_hours, performance_gain, ...}';

-- Compliance GPAI Modifications (Art. 53, Anexo XII)
ALTER TABLE MODMODELS ADD COLUMN MODGPAIMODIFICATIONSDOCURL VARCHAR(500);
COMMENT ON COLUMN MODMODELS.MODGPAIMODIFICATIONSDOCURL IS 'URL documentación modificaciones GPAI según Anexo XII (transparency info)';

-- ==================================================
-- ÍNDICES ADICIONALES PARA QUERIES ADAPTACIÓN
-- ==================================================

CREATE INDEX idx_mod_is_adapter ON MODMODELS(MODISADAPTER) WHERE MODISADAPTER = TRUE;
CREATE INDEX idx_mod_is_finetuned ON MODMODELS(MODISFINETUNED) WHERE MODISFINETUNED = TRUE;
CREATE INDEX idx_mod_adaptation_strategy ON MODMODELS(MODADAPTATIONSTRATEGY) WHERE MODADAPTATIONSTRATEGY IS NOT NULL;

-- ==================================================
-- QUERIES EJEMPLO
-- ==================================================

-- Obtener todos los adapters de un modelo base
-- SELECT * FROM MODMODELS WHERE IDMODBASEMODEL = 123 AND MODISADAPTER = TRUE;

-- Obtener árbol de linaje (recursivo - ver vista V_MODEL_LINEAGE_TREE)
-- WITH RECURSIVE lineage AS (
--   SELECT * FROM MODMODELS WHERE IDXMODEL = 123
--   UNION ALL
--   SELECT m.* FROM MODMODELS m INNER JOIN lineage l ON m.IDMODBASEMODEL = l.IDXMODEL
-- ) SELECT * FROM lineage;

-- Estadísticas adaptación
-- SELECT 
--   MODADAPTATIONSTRATEGY,
--   COUNT(*) as total,
--   AVG((MODADAPTATIONMETADATA->>'cost_usd')::DECIMAL) as avg_cost,
--   AVG((MODADAPTATIONMETADATA->>'co2_kg')::DECIMAL) as avg_co2
-- FROM MODMODELS 
-- WHERE MODADAPTATIONMETADATA IS NOT NULL
-- GROUP BY MODADAPTATIONSTRATEGY;

-- ==================================================
-- FIN MIGRATION
-- ==================================================


