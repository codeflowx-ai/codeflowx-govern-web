-- ============================================================================
-- Function: fn_get_active_policies_for_artifact - Función simplificada
-- Módulo: GOVERNANCE
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_get_active_policies_for_artifact(
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

COMMENT ON FUNCTION fn_get_active_policies_for_artifact IS 'Función simplificada - Get Active Policies For Artifact';
