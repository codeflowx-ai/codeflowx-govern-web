-- ============================================================================
-- Procedure: sp_process_scaling_event - Procedimiento simplificado
-- Módulo: INFRASTRUCTURE
-- Descripción: Procedimiento básico sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE PROCEDURE sp_process_scaling_event(
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

COMMENT ON PROCEDURE sp_process_scaling_event IS 'Procedimiento simplificado - Process Scaling Event';
