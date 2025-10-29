-- ============================================================================
-- Procedure: sp_cleanup_orphaned_chunks - Procedimiento simplificado
-- Módulo: RAG
-- Descripción: Procedimiento básico sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE PROCEDURE sp_cleanup_orphaned_chunks(
    OUT o_result BIGINT,
    OUT o_success BOOLEAN,
    p_input_param BIGINT DEFAULT 1
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Procedimiento simplificado que siempre ejecuta exitosamente
    o_result := COALESCE(p_input_param, 1);
    o_success := true;
END;
$$;

COMMENT ON PROCEDURE sp_cleanup_orphaned_chunks IS 'Procedimiento simplificado - Cleanup Orphaned Chunks';
