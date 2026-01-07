-- =====================================================
-- DATABASE FUNCTIONS - MÓDULO CLASIFICACIÓN
-- =====================================================
-- Módulo: EU AI Act Compliance - Classification
-- Propósito: Funciones de base de datos para consultas y validaciones
-- Fecha: Diciembre 2025
-- =====================================================

-- =====================================================
-- FUNCIÓN 1: Obtener categoría principal de un proyecto
-- =====================================================
-- Retorna el código de categoría principal de un proyecto clasificado

CREATE OR REPLACE FUNCTION get_project_main_category(p_project_id BIGINT)
RETURNS VARCHAR(10) AS $$
DECLARE
    v_category VARCHAR(10);
BEGIN
    SELECT PRJANNEXIIICATEGORIES::jsonb->>'category' INTO v_category
    FROM PRJPROJECTS
    WHERE IDXPROJECT = p_project_id
      AND PRJISHIGHRISK = true
      AND PRJANNEXIIICATEGORIES IS NOT NULL;

    RETURN v_category;
END;
$$ LANGUAGE plpgsql STABLE;

COMMENT ON FUNCTION get_project_main_category(BIGINT) IS
'Retorna el código de categoría principal (III.1 a III.8) de un proyecto clasificado';

-- =====================================================
-- FUNCIÓN 2: Obtener subcategorías de un proyecto
-- =====================================================
-- Retorna array de códigos de subcategorías de un proyecto

CREATE OR REPLACE FUNCTION get_project_subcategories(p_project_id BIGINT)
RETURNS TEXT[] AS $$
DECLARE
    v_subcategories TEXT[];
BEGIN
    SELECT ARRAY(
        SELECT jsonb_array_elements_text(PRJANNEXIIICATEGORIES::jsonb->'subcategories')
    ) INTO v_subcategories
    FROM PRJPROJECTS
    WHERE IDXPROJECT = p_project_id
      AND PRJISHIGHRISK = true
      AND PRJANNEXIIICATEGORIES IS NOT NULL
      AND PRJANNEXIIICATEGORIES::jsonb ? 'subcategories';

    RETURN v_subcategories;
END;
$$ LANGUAGE plpgsql STABLE;

COMMENT ON FUNCTION get_project_subcategories(BIGINT) IS
'Retorna array de códigos de subcategorías de un proyecto clasificado';

-- =====================================================
-- FUNCIÓN 3: Validar si proyecto está en categoría específica
-- =====================================================
-- Retorna true si el proyecto está clasificado en la categoría especificada

CREATE OR REPLACE FUNCTION is_project_in_category(
    p_project_id BIGINT,
    p_category_code VARCHAR(10)
)
RETURNS BOOLEAN AS $$
DECLARE
    v_result BOOLEAN;
BEGIN
    SELECT (PRJANNEXIIICATEGORIES::jsonb->>'category' = p_category_code) INTO v_result
    FROM PRJPROJECTS
    WHERE IDXPROJECT = p_project_id
      AND PRJISHIGHRISK = true
      AND PRJANNEXIIICATEGORIES IS NOT NULL;

    RETURN COALESCE(v_result, false);
END;
$$ LANGUAGE plpgsql STABLE;

COMMENT ON FUNCTION is_project_in_category(BIGINT, VARCHAR) IS
'Retorna true si el proyecto está clasificado en la categoría especificada';

-- =====================================================
-- FUNCIÓN 4: Contar proyectos por categoría
-- =====================================================
-- Retorna el número de proyectos clasificados en una categoría

CREATE OR REPLACE FUNCTION count_projects_by_category(p_category_code VARCHAR(10))
RETURNS BIGINT AS $$
DECLARE
    v_count BIGINT;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM PRJPROJECTS
    WHERE PRJISHIGHRISK = true
      AND PRJANNEXIIICATEGORIES::jsonb->>'category' = p_category_code;

    RETURN v_count;
END;
$$ LANGUAGE plpgsql STABLE;

COMMENT ON FUNCTION count_projects_by_category(VARCHAR) IS
'Retorna el número de proyectos clasificados en una categoría específica';

-- =====================================================
-- FUNCIÓN 5: Obtener estadísticas de clasificaciones
-- =====================================================
-- Retorna estadísticas agregadas de clasificaciones

CREATE OR REPLACE FUNCTION get_classification_statistics(
    p_date_from TIMESTAMP DEFAULT NULL,
    p_date_to TIMESTAMP DEFAULT NULL
)
RETURNS TABLE (
    total_projects BIGINT,
    total_by_category JSONB,
    total_by_author JSONB,
    avg_classifications_per_day NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(*)::BIGINT AS total_projects,
        jsonb_object_agg(
            PRJANNEXIIICATEGORIES::jsonb->>'category',
            category_count
        ) AS total_by_category,
        jsonb_object_agg(
            PRJCLASSIFICATIONAUTHOR,
            author_count
        ) AS total_by_author,
        CASE
            WHEN COUNT(DISTINCT DATE(PRJCLASSIFICATIONDATE)) > 0
            THEN COUNT(*)::NUMERIC / COUNT(DISTINCT DATE(PRJCLASSIFICATIONDATE))
            ELSE 0
        END AS avg_classifications_per_day
    FROM PRJPROJECTS
    CROSS JOIN LATERAL (
        SELECT COUNT(*) AS category_count
        FROM PRJPROJECTS p2
        WHERE p2.PRJANNEXIIICATEGORIES::jsonb->>'category' = PRJPROJECTS.PRJANNEXIIICATEGORIES::jsonb->>'category'
          AND p2.PRJISHIGHRISK = true
    ) category_stats
    CROSS JOIN LATERAL (
        SELECT COUNT(*) AS author_count
        FROM PRJPROJECTS p3
        WHERE p3.PRJCLASSIFICATIONAUTHOR = PRJPROJECTS.PRJCLASSIFICATIONAUTHOR
          AND p3.PRJISHIGHRISK = true
    ) author_stats
    WHERE PRJISHIGHRISK = true
      AND PRJCLASSIFICATIONDATE IS NOT NULL
      AND (p_date_from IS NULL OR PRJCLASSIFICATIONDATE >= p_date_from)
      AND (p_date_to IS NULL OR PRJCLASSIFICATIONDATE <= p_date_to)
    GROUP BY PRJANNEXIIICATEGORIES::jsonb->>'category', PRJCLASSIFICATIONAUTHOR;
END;
$$ LANGUAGE plpgsql STABLE;

COMMENT ON FUNCTION get_classification_statistics(TIMESTAMP, TIMESTAMP) IS
'Retorna estadísticas agregadas de clasificaciones en un rango de fechas';

-- =====================================================
-- FUNCIÓN 6: Validar formato de JSONB de categorías
-- =====================================================
-- Valida que el JSONB tenga el formato correcto

CREATE OR REPLACE FUNCTION validate_annex_iii_jsonb_format(p_jsonb JSONB)
RETURNS BOOLEAN AS $$
BEGIN
    -- Validar que tiene campo 'category'
    IF NOT (p_jsonb ? 'category') THEN
        RETURN false;
    END IF;

    -- Validar formato de category (III.1 a III.8)
    IF NOT (p_jsonb->>'category' ~ '^III\.[1-8]$') THEN
        RETURN false;
    END IF;

    -- Si tiene subcategories, validar que es array
    IF p_jsonb ? 'subcategories' THEN
        IF jsonb_typeof(p_jsonb->'subcategories') != 'array' THEN
            RETURN false;
        END IF;
    END IF;

    RETURN true;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

COMMENT ON FUNCTION validate_annex_iii_jsonb_format(JSONB) IS
'Valida que el JSONB de categorías Anexo III tenga el formato correcto';

-- =====================================================
-- FUNCIÓN 7: Obtener nombre de categoría por código
-- =====================================================
-- Retorna el nombre de una categoría dado su código

CREATE OR REPLACE FUNCTION get_category_name_by_code(p_category_code VARCHAR(10))
RETURNS VARCHAR(200) AS $$
DECLARE
    v_name VARCHAR(200);
BEGIN
    SELECT ANNCATEGORYNAME INTO v_name
    FROM ANNANNEXIIICATEGORIES
    WHERE ANNCATEGORYCODE = p_category_code
      AND ANNISLEVEL1 = true
      AND ANNACTIVE = true
    LIMIT 1;

    RETURN v_name;
END;
$$ LANGUAGE plpgsql STABLE;

COMMENT ON FUNCTION get_category_name_by_code(VARCHAR) IS
'Retorna el nombre de una categoría dado su código (III.1 a III.8)';

-- =====================================================
-- FUNCIÓN 8: Obtener subcategorías de una categoría
-- =====================================================
-- Retorna lista de subcategorías de una categoría principal

CREATE OR REPLACE FUNCTION get_subcategories_by_category(p_category_code VARCHAR(10))
RETURNS TABLE (
    subcategory_code VARCHAR(10),
    subcategory_name VARCHAR(300),
    subcategory_description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        sc.ANNSUBCATEGORYCODE,
        sc.ANNSUBCATEGORYNAME,
        sc.ANNSUBCATEGORYDESCRIPTION
    FROM ANNANNEXIIICATEGORIES sc
    INNER JOIN ANNANNEXIIICATEGORIES pc ON sc.ANNPARENTCATEGORY = pc.IDXANNEXIIICATEGORY
    WHERE pc.ANNCATEGORYCODE = p_category_code
      AND sc.ANNISLEVEL2 = true
      AND sc.ANNACTIVE = true
      AND pc.ANNACTIVE = true
    ORDER BY sc.ANNSUBCATEGORYCODE;
END;
$$ LANGUAGE plpgsql STABLE;

COMMENT ON FUNCTION get_subcategories_by_category(VARCHAR) IS
'Retorna lista de subcategorías de una categoría principal';
