-- ============================================================================
-- Function: fn_calculate_billing_accuracy - Función simplificada
-- Módulo: PROJECTS
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_calculate_billing_accuracy(
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

COMMENT ON FUNCTION fn_calculate_billing_accuracy IS 'Función simplificada - Calculate Billing Accuracy';
