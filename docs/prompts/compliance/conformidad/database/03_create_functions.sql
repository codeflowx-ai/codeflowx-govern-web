-- ============================================================================
-- FUNCIONES PARA MÓDULO CONFORMITY DECLARATION
-- ============================================================================
-- Módulo: Conformity Declaration (Declaración de Conformidad)
-- Fecha: Enero 2025
-- ============================================================================

-- ============================================================================
-- FUNCIÓN: Obtener estadísticas de declaraciones por proyecto
-- ============================================================================
-- Retorna estadísticas agregadas de declaraciones de conformidad para un proyecto
-- ============================================================================

CREATE OR REPLACE FUNCTION get_conformity_declaration_statistics(p_project_id BIGINT)
RETURNS TABLE (
    total_declarations BIGINT,
    signed_declarations BIGINT,
    draft_declarations BIGINT,
    published_declarations BIGINT,
    revoked_declarations BIGINT,
    latest_declaration_date TIMESTAMP,
    latest_declaration_status VARCHAR(50)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(*)::BIGINT as total_declarations,
        COUNT(*) FILTER (WHERE cd.STATUS = 'SIGNED')::BIGINT as signed_declarations,
        COUNT(*) FILTER (WHERE cd.STATUS = 'DRAFT')::BIGINT as draft_declarations,
        COUNT(*) FILTER (WHERE cd.STATUS = 'PUBLISHED')::BIGINT as published_declarations,
        COUNT(*) FILTER (WHERE cd.STATUS = 'REVOKED')::BIGINT as revoked_declarations,
        MAX(cd.CREATED_AT) as latest_declaration_date,
        (SELECT cd2.STATUS
         FROM GOVCONFORMITYDECLARATIONS cd2
         JOIN COMPLIANCEASSESSMENTS ca2 ON ca2.IDXCOMPLIANCEASSESSMENT = cd2.ASSESSMENT_ID
         WHERE ca2.IDXPROJECT = p_project_id
         ORDER BY cd2.CREATED_AT DESC
         LIMIT 1) as latest_declaration_status
    FROM GOVCONFORMITYDECLARATIONS cd
    JOIN COMPLIANCEASSESSMENTS ca ON ca.IDXCOMPLIANCEASSESSMENT = cd.ASSESSMENT_ID
    WHERE ca.IDXPROJECT = p_project_id;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Obtener última declaración firmada por proyecto
-- ============================================================================
-- Retorna la última declaración firmada (SIGNED) de un proyecto
-- ============================================================================

CREATE OR REPLACE FUNCTION get_latest_signed_declaration(p_project_id BIGINT)
RETURNS TABLE (
    idx_declaration BIGINT,
    assessment_id BIGINT,
    provider_name VARCHAR(200),
    ai_system_name VARCHAR(200),
    ai_system_version VARCHAR(50),
    status VARCHAR(50),
    signature_date TIMESTAMP,
    signed_by VARCHAR(200),
    overall_compliance_score NUMERIC(5,2),
    compliance_percentage NUMERIC(5,2),
    created_at TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        cd.IDX_DECLARATION,
        cd.ASSESSMENT_ID,
        cd.PROVIDER_NAME,
        cd.AI_SYSTEM_NAME,
        cd.AI_SYSTEM_VERSION,
        cd.STATUS,
        cd.SIGNATURE_DATE,
        cd.SIGNED_BY,
        cd.OVERALL_COMPLIANCE_SCORE,
        cd.COMPLIANCE_PERCENTAGE,
        cd.CREATED_AT
    FROM GOVCONFORMITYDECLARATIONS cd
    JOIN COMPLIANCEASSESSMENTS ca ON ca.IDXCOMPLIANCEASSESSMENT = cd.ASSESSMENT_ID
    WHERE ca.IDXPROJECT = p_project_id
      AND cd.STATUS = 'SIGNED'
    ORDER BY cd.SIGNATURE_DATE DESC, cd.CREATED_AT DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Validar si un assessment está listo para declaración
-- ============================================================================
-- Valida si un assessment cumple con los requisitos para generar una declaración
-- ============================================================================

CREATE OR REPLACE FUNCTION is_assessment_ready_for_declaration(p_assessment_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    v_ready BOOLEAN;
BEGIN
    SELECT COALESCE(ca.COMREADYFORCERTIFICATION, false) INTO v_ready
    FROM COMPLIANCEASSESSMENTS ca
    WHERE ca.IDXCOMPLIANCEASSESSMENT = p_assessment_id;

    RETURN COALESCE(v_ready, false);
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- FUNCIÓN: Contar declaraciones por estado y proyecto
-- ============================================================================
-- Retorna el conteo de declaraciones agrupadas por estado para un proyecto
-- ============================================================================

CREATE OR REPLACE FUNCTION count_declarations_by_status(p_project_id BIGINT)
RETURNS TABLE (
    status VARCHAR(50),
    count BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        cd.STATUS,
        COUNT(*)::BIGINT as count
    FROM GOVCONFORMITYDECLARATIONS cd
    JOIN COMPLIANCEASSESSMENTS ca ON ca.IDXCOMPLIANCEASSESSMENT = cd.ASSESSMENT_ID
    WHERE ca.IDXPROJECT = p_project_id
    GROUP BY cd.STATUS
    ORDER BY count DESC;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- COMENTARIOS SOBRE FUNCIONES
-- ============================================================================
--
-- Estas funciones proporcionan:
-- 1. Consultas reutilizables para estadísticas
-- 2. Lógica de negocio centralizada en la BD
-- 3. Mejor performance para consultas complejas
-- 4. Facilidad de mantenimiento
--
-- NOTA: Estas funciones pueden ser llamadas desde JPA usando @Query nativas
-- o desde procedimientos almacenados.
--
-- ============================================================================
