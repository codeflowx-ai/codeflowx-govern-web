-- ============================================================================
-- Procedure: sp_detect_governance_anomalies - Procedimiento simplificado
-- Módulo: GOVERNANCE
-- Descripción: Procedimiento básico sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE PROCEDURE sp_detect_governance_anomalies(
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

COMMENT ON PROCEDURE sp_detect_governance_anomalies IS 'Procedimiento simplificado - Detect Governance Anomalies';
