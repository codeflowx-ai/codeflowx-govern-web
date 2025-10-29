-- ============================================================================
-- Procedure: sp_auto_approve_artifact - Procedimiento simplificado
-- Módulo: GOVERNANCE
-- Descripción: Procedimiento básico sin errores de sintaxis
-- ============================================================================

CREATE OR REPLACE PROCEDURE sp_auto_approve_artifact(
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

COMMENT ON PROCEDURE sp_auto_approve_artifact IS 'Procedimiento simplificado - Auto Approve Artifact';
