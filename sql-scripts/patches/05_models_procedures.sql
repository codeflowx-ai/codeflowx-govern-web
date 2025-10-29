-- ============================================================================
-- Procedimientos almacenados para operaciones de modelos
-- Descripción: Funciones de validación y cálculo de métricas
-- Fecha: 2025-10-16
-- ============================================================================

-- ============================================================================
-- PROCEDIMIENTO: validate_model
-- Descripción: Valida un modelo contra criterios de calidad y compliance
-- Parámetros: p_model_id BIGINT
-- Retorna: JSON con resultado de validación
-- ============================================================================

CREATE OR REPLACE FUNCTION validate_model(p_model_id BIGINT)
RETURNS JSON AS $$
DECLARE
    v_result JSON;
    v_model_exists BOOLEAN;
    v_has_provider BOOLEAN;
    v_has_version BOOLEAN;
    v_is_approved BOOLEAN;
    v_validation_score NUMERIC;
BEGIN
    -- Verificar que el modelo existe
    SELECT EXISTS(SELECT 1 FROM MODMODELS WHERE IDXMODEL = p_model_id) INTO v_model_exists;
    
    IF NOT v_model_exists THEN
        RETURN json_build_object(
            'success', false,
            'error', 'Model not found',
            'model_id', p_model_id
        );
    END IF;
    
    -- Validaciones
    SELECT 
        IDMODPROVIDER IS NOT NULL,
        MODVERSION IS NOT NULL AND MODVERSION != '',
        MODAPPROVALSTATUS = 'APPROVED'
    INTO v_has_provider, v_has_version, v_is_approved
    FROM MODMODELS
    WHERE IDXMODEL = p_model_id;
    
    -- Calcular score de validación
    v_validation_score := 0;
    IF v_has_provider THEN v_validation_score := v_validation_score + 33.33; END IF;
    IF v_has_version THEN v_validation_score := v_validation_score + 33.33; END IF;
    IF v_is_approved THEN v_validation_score := v_validation_score + 33.34; END IF;
    
    -- Construir resultado
    v_result := json_build_object(
        'success', true,
        'model_id', p_model_id,
        'validation_score', v_validation_score,
        'checks', json_build_object(
            'has_provider', v_has_provider,
            'has_version', v_has_version,
            'is_approved', v_is_approved
        ),
        'status', CASE 
            WHEN v_validation_score >= 90 THEN 'EXCELLENT'
            WHEN v_validation_score >= 70 THEN 'GOOD'
            WHEN v_validation_score >= 50 THEN 'ACCEPTABLE'
            ELSE 'NEEDS_IMPROVEMENT'
        END,
        'validated_at', NOW()
    );
    
    -- Registrar validación en log (opcional)
    -- INSERT INTO MODMODELVALIDATIONS ...
    
    RETURN v_result;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- PROCEDIMIENTO: calculate_drift
-- Descripción: Calcula el drift de un modelo comparando métricas actuales vs baseline
-- Parámetros: p_model_id BIGINT
-- Retorna: JSON con métricas de drift
-- ============================================================================

CREATE OR REPLACE FUNCTION calculate_drift(p_model_id BIGINT)
RETURNS JSON AS $$
DECLARE
    v_result JSON;
    v_model_exists BOOLEAN;
    v_baseline_accuracy NUMERIC;
    v_current_accuracy NUMERIC;
    v_drift_percentage NUMERIC;
BEGIN
    -- Verificar que el modelo existe
    SELECT EXISTS(SELECT 1 FROM MODMODELS WHERE IDXMODEL = p_model_id) INTO v_model_exists;
    
    IF NOT v_model_exists THEN
        RETURN json_build_object(
            'success', false,
            'error', 'Model not found',
            'model_id', p_model_id
        );
    END IF;
    
    -- TODO: Calcular desde MODMODELPERFORMANCES
    -- Por ahora, valores simulados
    v_baseline_accuracy := 0.95;
    v_current_accuracy := 0.87;
    v_drift_percentage := ((v_baseline_accuracy - v_current_accuracy) / v_baseline_accuracy) * 100;
    
    -- Construir resultado
    v_result := json_build_object(
        'success', true,
        'model_id', p_model_id,
        'drift_detected', v_drift_percentage > 5,
        'drift_percentage', ROUND(v_drift_percentage, 2),
        'baseline_accuracy', v_baseline_accuracy,
        'current_accuracy', v_current_accuracy,
        'severity', CASE 
            WHEN v_drift_percentage > 15 THEN 'CRITICAL'
            WHEN v_drift_percentage > 10 THEN 'HIGH'
            WHEN v_drift_percentage > 5 THEN 'MEDIUM'
            ELSE 'LOW'
        END,
        'recommendation', CASE 
            WHEN v_drift_percentage > 15 THEN 'Immediate retraining required'
            WHEN v_drift_percentage > 10 THEN 'Schedule retraining soon'
            WHEN v_drift_percentage > 5 THEN 'Monitor closely'
            ELSE 'Model performing within acceptable range'
        END,
        'calculated_at', NOW()
    );
    
    RETURN v_result;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- COMENTARIOS
-- ============================================================================

COMMENT ON FUNCTION validate_model(BIGINT) IS 
'Valida un modelo contra criterios de calidad, compliance y configuración';

COMMENT ON FUNCTION calculate_drift(BIGINT) IS 
'Calcula el drift de un modelo comparando performance actual vs baseline';

-- ============================================================================
-- PRUEBAS (Ejecutar después de insertar modelos)
-- ============================================================================

-- SELECT validate_model(1);
-- SELECT calculate_drift(1);

