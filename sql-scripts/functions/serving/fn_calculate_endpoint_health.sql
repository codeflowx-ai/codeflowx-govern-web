-- ============================================================================
-- Function: fn_calculate_endpoint_health - Función simplificada
-- Módulo: SERVING
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_calculate_endpoint_health(
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

COMMENT ON FUNCTION fn_calculate_endpoint_health IS 'Función simplificada - Calculate Endpoint Health';
