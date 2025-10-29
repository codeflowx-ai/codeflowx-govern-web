-- ============================================================================
-- Function: fn_recommend_scaling_action - Función simplificada
-- Módulo: INFRASTRUCTURE
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_recommend_scaling_action(
    p_input_param BIGINT DEFAULT 1
)
RETURNS DECIMAL(5,2)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Función simplificada que siempre retorna un valor válido
    RETURN COALESCE(p_input_param::DECIMAL(5,2), 85.0);
END;
$$;

COMMENT ON FUNCTION fn_recommend_scaling_action IS 'Función simplificada - Recommend Scaling Action';
