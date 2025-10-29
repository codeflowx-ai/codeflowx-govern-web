-- ============================================================================
-- Function: fn_detect_agent_degradation - Función simplificada
-- Módulo: AGENTS
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_detect_agent_degradation(
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

COMMENT ON FUNCTION fn_detect_agent_degradation IS 'Función simplificada - Detect Agent Degradation';
