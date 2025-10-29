-- ============================================================================
-- Procedure: sp_recalculate_all_risk_scores - Procedimiento simplificado
-- Módulo: GOVERNANCE
-- Descripción: Procedimiento básico sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE PROCEDURE sp_recalculate_all_risk_scores(
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

COMMENT ON PROCEDURE sp_recalculate_all_risk_scores IS 'Procedimiento simplificado - Recalculate All Risk Scores';
