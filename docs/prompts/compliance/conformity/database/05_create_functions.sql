-- ============================================================================
-- FUNCIONES PARA CONFORMITY DECLARATION
-- ============================================================================
-- Descripción: Script para crear funciones de base de datos
-- Fecha: Enero 2025
-- Módulo: Conformity Declaration
-- ============================================================================

-- ============================================================================
-- FUNCIÓN 1: Generar siguiente versión automáticamente
-- ============================================================================
-- Propósito: Generar la siguiente versión basada en declaraciones existentes
-- Uso: Puede ser llamada desde triggers o desde el Business Service
-- ============================================================================

CREATE OR REPLACE FUNCTION get_next_declaration_version(p_assessment_id BIGINT)
RETURNS VARCHAR(50) AS $$
DECLARE
    v_project_id BIGINT;
    v_existing_count BIGINT;
    v_next_version VARCHAR(50);
BEGIN
    -- Obtener project_id desde assessment
    SELECT ca.IDXPROJECT INTO v_project_id
    FROM COMPLIANCEASSESSMENTS ca
    WHERE ca.IDXCOMPLIANCEASSESSMENT = p_assessment_id;

    IF v_project_id IS NULL THEN
        RAISE EXCEPTION 'Assessment % not found', p_assessment_id;
    END IF;

    -- Contar declaraciones existentes del proyecto
    SELECT COUNT(*) INTO v_existing_count
    FROM GOVCONFORMITYDECLARATIONS cd
    INNER JOIN COMPLIANCEASSESSMENTS ca ON cd.ASSESSMENT_ID = ca.IDXCOMPLIANCEASSESSMENT
    WHERE ca.IDXPROJECT = v_project_id;

    -- Generar siguiente versión: v1.{count + 1}
    v_next_version := 'v1.' || (v_existing_count + 1);

    RETURN v_next_version;
END;
$$ LANGUAGE plpgsql;

-- Comentario de la función
COMMENT ON FUNCTION get_next_declaration_version(BIGINT) IS
'Genera la siguiente versión de declaración basada en el número de declaraciones existentes del proyecto';

-- ============================================================================
-- FUNCIÓN 2: Obtener estadísticas de declaraciones por proyecto
-- ============================================================================
-- Propósito: Función que retorna estadísticas agregadas como JSON
-- Uso: Para APIs y reportes que necesitan estadísticas en formato estructurado
-- ============================================================================

CREATE OR REPLACE FUNCTION get_declaration_statistics_by_project(p_project_id BIGINT)
RETURNS JSON AS $$
DECLARE
    v_result JSON;
BEGIN
    SELECT json_build_object(
        'projectId', p_project_id,
        'totalDeclarations', COUNT(cd.IDX_DECLARATION),
        'signedDeclarations', COUNT(CASE WHEN cd.STATUS = 'SIGNED' THEN 1 END),
        'draftDeclarations', COUNT(CASE WHEN cd.STATUS = 'DRAFT' THEN 1 END),
        'publishedDeclarations', COUNT(CASE WHEN cd.STATUS = 'PUBLISHED' THEN 1 END),
        'revokedDeclarations', COUNT(CASE WHEN cd.STATUS = 'REVOKED' THEN 1 END),
        'latestDeclarationDate', MAX(cd.CREATED_AT),
        'latestSignatureDate', MAX(cd.SIGNATURE_DATE),
        'latestVersion', MAX(cd.AI_SYSTEM_VERSION)
    ) INTO v_result
    FROM GOVCONFORMITYDECLARATIONS cd
    INNER JOIN COMPLIANCEASSESSMENTS ca ON cd.ASSESSMENT_ID = ca.IDXCOMPLIANCEASSESSMENT
    WHERE ca.IDXPROJECT = p_project_id;

    RETURN COALESCE(v_result, json_build_object(
        'projectId', p_project_id,
        'totalDeclarations', 0,
        'signedDeclarations', 0,
        'draftDeclarations', 0,
        'publishedDeclarations', 0,
        'revokedDeclarations', 0
    ));
END;
$$ LANGUAGE plpgsql;

-- Comentario de la función
COMMENT ON FUNCTION get_declaration_statistics_by_project(BIGINT) IS
'Retorna estadísticas de declaraciones por proyecto en formato JSON';

-- ============================================================================
-- FUNCIÓN 3: Validar si una declaración puede ser firmada
-- ============================================================================
-- Propósito: Validar reglas de negocio antes de firmar
-- Uso: Puede ser llamada desde triggers o desde el Business Service
-- ============================================================================

CREATE OR REPLACE FUNCTION can_sign_declaration(p_declaration_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    v_status VARCHAR(50);
    v_has_provider_name BOOLEAN;
    v_has_system_name BOOLEAN;
BEGIN
    -- Obtener estado actual
    SELECT STATUS,
           (PROVIDER_NAME IS NOT NULL AND LENGTH(TRIM(PROVIDER_NAME)) > 0),
           (AI_SYSTEM_NAME IS NOT NULL AND LENGTH(TRIM(AI_SYSTEM_NAME)) > 0)
    INTO v_status, v_has_provider_name, v_has_system_name
    FROM GOVCONFORMITYDECLARATIONS
    WHERE IDX_DECLARATION = p_declaration_id;

    IF v_status IS NULL THEN
        RETURN FALSE; -- Declaración no existe
    END IF;

    -- Solo DRAFT puede ser firmada
    IF v_status != 'DRAFT' THEN
        RETURN FALSE;
    END IF;

    -- Debe tener provider name y system name
    IF NOT v_has_provider_name OR NOT v_has_system_name THEN
        RETURN FALSE;
    END IF;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

-- Comentario de la función
COMMENT ON FUNCTION can_sign_declaration(BIGINT) IS
'Valida si una declaración puede ser firmada según reglas de negocio';

-- ============================================================================
-- FUNCIÓN 4: Obtener última declaración de un proyecto
-- ============================================================================
-- Propósito: Obtener la declaración más reciente de un proyecto
-- Uso: Para mostrar última declaración en dashboards
-- ============================================================================

CREATE OR REPLACE FUNCTION get_latest_declaration_by_project(p_project_id BIGINT)
RETURNS TABLE (
    declaration_id BIGINT,
    version VARCHAR(50),
    status VARCHAR(50),
    created_at TIMESTAMP,
    signature_date TIMESTAMP,
    signed_by VARCHAR(200)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        cd.IDX_DECLARATION,
        cd.AI_SYSTEM_VERSION,
        cd.STATUS,
        cd.CREATED_AT,
        cd.SIGNATURE_DATE,
        cd.SIGNED_BY
    FROM GOVCONFORMITYDECLARATIONS cd
    INNER JOIN COMPLIANCEASSESSMENTS ca ON cd.ASSESSMENT_ID = ca.IDXCOMPLIANCEASSESSMENT
    WHERE ca.IDXPROJECT = p_project_id
    ORDER BY cd.CREATED_AT DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- Comentario de la función
COMMENT ON FUNCTION get_latest_declaration_by_project(BIGINT) IS
'Retorna la declaración más reciente de un proyecto';

-- ============================================================================
-- COMENTARIOS SOBRE FUNCIONES
-- ============================================================================
--
-- Estas funciones proporcionan:
-- 1. Generación automática de versiones (lógica de negocio)
-- 2. Estadísticas agregadas (reportes y dashboards)
-- 3. Validación de reglas de negocio (integridad)
-- 4. Consultas optimizadas (rendimiento)
--
-- NOTA: Las funciones pueden ser llamadas desde:
-- - Triggers (para validaciones automáticas)
-- - Business Services (para lógica de negocio)
-- - Stored Procedures (para operaciones complejas)
-- - APIs directas (para consultas optimizadas)
-- ============================================================================
