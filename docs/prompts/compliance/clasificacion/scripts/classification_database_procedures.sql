-- =====================================================
-- DATABASE PROCEDURES - MÓDULO CLASIFICACIÓN
-- =====================================================
-- Módulo: EU AI Act Compliance - Classification
-- Propósito: Procedimientos almacenados para operaciones complejas
-- Fecha: Diciembre 2025
-- =====================================================

-- =====================================================
-- PROCEDIMIENTO 1: Reclasificar proyecto
-- =====================================================
-- Reclasifica un proyecto con nueva categoría y subcategorías

CREATE OR REPLACE PROCEDURE reclasify_project(
    p_project_id BIGINT,
    p_category_code VARCHAR(10),
    p_subcategories TEXT[] DEFAULT NULL,
    p_author VARCHAR(100) DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_category_exists BOOLEAN;
    v_jsonb JSONB;
BEGIN
    -- Validar que la categoría existe
    SELECT EXISTS(
        SELECT 1 FROM ANNANNEXIIICATEGORIES
        WHERE ANNCATEGORYCODE = p_category_code
          AND ANNISLEVEL1 = true
          AND ANNACTIVE = true
    ) INTO v_category_exists;

    IF NOT v_category_exists THEN
        RAISE EXCEPTION 'Categoría no encontrada o inactiva: %', p_category_code;
    END IF;

    -- Construir JSONB
    v_jsonb := jsonb_build_object('category', p_category_code);

    IF p_subcategories IS NOT NULL AND array_length(p_subcategories, 1) > 0 THEN
        v_jsonb := v_jsonb || jsonb_build_object('subcategories', to_jsonb(p_subcategories));
    END IF;

    -- Actualizar proyecto
    UPDATE PRJPROJECTS
    SET
        PRJISHIGHRISK = true,
        PRJANNEXIIICATEGORIES = v_jsonb::text,
        PRJCLASSIFICATIONDATE = CURRENT_TIMESTAMP,
        PRJCLASSIFICATIONAUTHOR = COALESCE(p_author, PRJCLASSIFICATIONAUTHOR),
        UPDATEDAT = CURRENT_TIMESTAMP
    WHERE IDXPROJECT = p_project_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Proyecto no encontrado: %', p_project_id;
    END IF;

    COMMIT;
END;
$$;

COMMENT ON PROCEDURE reclasify_project(BIGINT, VARCHAR, TEXT[], VARCHAR) IS
'Reclasifica un proyecto con nueva categoría y subcategorías';

-- =====================================================
-- PROCEDIMIENTO 2: Desclasificar proyecto
-- =====================================================
-- Desclasifica un proyecto (marca como no alto riesgo)

CREATE OR REPLACE PROCEDURE unclassify_project(p_project_id BIGINT)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE PRJPROJECTS
    SET
        PRJISHIGHRISK = false,
        PRJANNEXIIICATEGORIES = NULL,
        PRJCLASSIFICATIONDATE = NULL,
        PRJCLASSIFICATIONAUTHOR = NULL,
        UPDATEDAT = CURRENT_TIMESTAMP
    WHERE IDXPROJECT = p_project_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Proyecto no encontrado: %', p_project_id;
    END IF;

    COMMIT;
END;
$$;

COMMENT ON PROCEDURE unclassify_project(BIGINT) IS
'Desclasifica un proyecto (marca como no alto riesgo y limpia campos relacionados)';

-- =====================================================
-- PROCEDIMIENTO 3: Migrar categorías antiguas
-- =====================================================
-- Migra proyectos con formato antiguo de categorías al nuevo formato JSONB

CREATE OR REPLACE PROCEDURE migrate_old_category_format()
LANGUAGE plpgsql
AS $$
DECLARE
    v_project RECORD;
    v_jsonb JSONB;
    v_updated_count INTEGER := 0;
BEGIN
    -- Buscar proyectos con formato antiguo (si existiera)
    -- Este procedimiento es un ejemplo para migraciones futuras

    FOR v_project IN
        SELECT IDXPROJECT, PRJANNEXIIICATEGORIES
        FROM PRJPROJECTS
        WHERE PRJISHIGHRISK = true
          AND PRJANNEXIIICATEGORIES IS NOT NULL
          AND PRJANNEXIIICATEGORIES NOT LIKE '{%'
    LOOP
        -- Convertir formato antiguo a nuevo formato JSONB
        -- (Ejemplo: si había un formato diferente)

        -- Por ahora, solo contar
        v_updated_count := v_updated_count + 1;
    END LOOP;

    RAISE NOTICE 'Proyectos procesados: %', v_updated_count;

    COMMIT;
END;
$$;

COMMENT ON PROCEDURE migrate_old_category_format() IS
'Migra proyectos con formato antiguo de categorías al nuevo formato JSONB';

-- =====================================================
-- PROCEDIMIENTO 4: Validar y corregir inconsistencias
-- =====================================================
-- Valida y corrige inconsistencias en datos de clasificación

CREATE OR REPLACE PROCEDURE validate_and_fix_classification_data()
LANGUAGE plpgsql
AS $$
DECLARE
    v_project RECORD;
    v_fixed_count INTEGER := 0;
BEGIN
    -- Proyectos marcados como alto riesgo pero sin categorías
    FOR v_project IN
        SELECT IDXPROJECT
        FROM PRJPROJECTS
        WHERE PRJISHIGHRISK = true
          AND (PRJANNEXIIICATEGORIES IS NULL OR PRJANNEXIIICATEGORIES = '')
    LOOP
        -- Desclasificar proyectos inconsistentes
        UPDATE PRJPROJECTS
        SET PRJISHIGHRISK = false
        WHERE IDXPROJECT = v_project.IDXPROJECT;

        v_fixed_count := v_fixed_count + 1;
    END LOOP;

    -- Proyectos con categorías pero no marcados como alto riesgo
    FOR v_project IN
        SELECT IDXPROJECT
        FROM PRJPROJECTS
        WHERE PRJISHIGHRISK = false
          AND PRJANNEXIIICATEGORIES IS NOT NULL
          AND PRJANNEXIIICATEGORIES != ''
    LOOP
        -- Marcar como alto riesgo
        UPDATE PRJPROJECTS
        SET PRJISHIGHRISK = true
        WHERE IDXPROJECT = v_project.IDXPROJECT;

        v_fixed_count := v_fixed_count + 1;
    END LOOP;

    RAISE NOTICE 'Inconsistencias corregidas: %', v_fixed_count;

    COMMIT;
END;
$$;

COMMENT ON PROCEDURE validate_and_fix_classification_data() IS
'Valida y corrige inconsistencias en datos de clasificación';

-- =====================================================
-- PROCEDIMIENTO 5: Generar reporte de clasificaciones
-- =====================================================
-- Genera un reporte de clasificaciones en formato JSON

CREATE OR REPLACE PROCEDURE generate_classification_report(
    p_date_from TIMESTAMP DEFAULT NULL,
    p_date_to TIMESTAMP DEFAULT NULL,
    OUT p_report JSONB
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_report JSONB;
BEGIN
    SELECT jsonb_build_object(
        'period', jsonb_build_object(
            'from', p_date_from,
            'to', p_date_to
        ),
        'summary', jsonb_build_object(
            'total_classified', (
                SELECT COUNT(*)
                FROM PRJPROJECTS
                WHERE PRJISHIGHRISK = true
                  AND (p_date_from IS NULL OR PRJCLASSIFICATIONDATE >= p_date_from)
                  AND (p_date_to IS NULL OR PRJCLASSIFICATIONDATE <= p_date_to)
            ),
            'by_category', (
                SELECT jsonb_object_agg(
                    PRJANNEXIIICATEGORIES::jsonb->>'category',
                    category_count
                )
                FROM (
                    SELECT
                        PRJANNEXIIICATEGORIES::jsonb->>'category' AS category,
                        COUNT(*) AS category_count
                    FROM PRJPROJECTS
                    WHERE PRJISHIGHRISK = true
                      AND (p_date_from IS NULL OR PRJCLASSIFICATIONDATE >= p_date_from)
                      AND (p_date_to IS NULL OR PRJCLASSIFICATIONDATE <= p_date_to)
                    GROUP BY PRJANNEXIIICATEGORIES::jsonb->>'category'
                ) category_stats
            )
        ),
        'generated_at', CURRENT_TIMESTAMP
    ) INTO v_report;

    p_report := v_report;
END;
$$;

COMMENT ON PROCEDURE generate_classification_report(TIMESTAMP, TIMESTAMP, OUT JSONB) IS
'Genera un reporte de clasificaciones en formato JSON';
