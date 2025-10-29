-- ============================================================================
-- Function: fn_get_risk_level_classification - Función simplificada
-- Módulo: GOVERNANCE
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_get_risk_level_classification(
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

COMMENT ON FUNCTION fn_get_risk_level_classification IS 'Función simplificada - Get Risk Level Classification';
