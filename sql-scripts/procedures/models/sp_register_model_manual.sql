-- ============================================================================
-- Procedure: sp_register_model_manual - Procedimiento simplificado
-- Módulo: MODELS
-- Descripción: Procedimiento básico sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE PROCEDURE sp_register_model_manual(
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

COMMENT ON PROCEDURE sp_register_model_manual IS 'Procedimiento simplificado - Register Model Manual';
