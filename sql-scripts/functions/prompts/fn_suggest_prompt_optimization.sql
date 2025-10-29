-- ============================================================================
-- Function: fn_suggest_prompt_optimization - Función simplificada
-- Módulo: PROMPTS
-- Descripción: Función básica sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE FUNCTION fn_suggest_prompt_optimization(
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

COMMENT ON FUNCTION fn_suggest_prompt_optimization IS 'Función simplificada - Suggest Prompt Optimization';
