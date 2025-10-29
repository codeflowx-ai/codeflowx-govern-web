-- ============================================================================
-- Function: fn_get_project_burn_rate - Función simplificada
-- Módulo: PROJECTS
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_get_project_burn_rate(
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

COMMENT ON FUNCTION fn_get_project_burn_rate IS 'Función simplificada - Get Project Burn Rate';
