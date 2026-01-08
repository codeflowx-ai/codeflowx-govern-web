-- =====================================================
-- DATABASE TRIGGERS - MÓDULO CLASIFICACIÓN
-- =====================================================
-- Módulo: EU AI Act Compliance - Classification
-- Propósito: Triggers de base de datos para validaciones y automatizaciones
-- Fecha: Diciembre 2025
-- =====================================================

-- =====================================================
-- TRIGGER 1: Validar JSONB de Categorías Anexo III
-- =====================================================
-- Valida que el JSONB PRJANNEXIIICATEGORIES tenga el formato correcto
-- antes de insertar o actualizar

CREATE OR REPLACE FUNCTION validate_annex_iii_categories_jsonb()
RETURNS TRIGGER AS $$
BEGIN
    -- Validar que PRJANNEXIIICATEGORIES tenga formato correcto si no es NULL
    IF NEW.PRJANNEXIIICATEGORIES IS NOT NULL THEN
        -- Validar que es un JSON válido
        IF NOT (NEW.PRJANNEXIIICATEGORIES::jsonb ? 'category') THEN
            RAISE EXCEPTION 'PRJANNEXIIICATEGORIES debe contener el campo "category"';
        END IF;

        -- Validar que category tiene formato III.X
        IF NOT (NEW.PRJANNEXIIICATEGORIES::jsonb->>'category' ~ '^III\.[1-8]$') THEN
            RAISE EXCEPTION 'Código de categoría inválido. Debe ser III.1 a III.8';
        END IF;

        -- Validar que subcategories es un array si existe
        IF NEW.PRJANNEXIIICATEGORIES::jsonb ? 'subcategories' THEN
            IF jsonb_typeof(NEW.PRJANNEXIIICATEGORIES::jsonb->'subcategories') != 'array' THEN
                RAISE EXCEPTION 'subcategories debe ser un array';
            END IF;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_annex_iii_categories
    BEFORE INSERT OR UPDATE ON PRJPROJECTS
    FOR EACH ROW
    WHEN (NEW.PRJANNEXIIICATEGORIES IS NOT NULL)
    EXECUTE FUNCTION validate_annex_iii_categories_jsonb();

COMMENT ON FUNCTION validate_annex_iii_categories_jsonb() IS
'Valida el formato del JSONB PRJANNEXIIICATEGORIES antes de insertar o actualizar';

-- =====================================================
-- TRIGGER 2: Actualizar PRJCLASSIFICATIONDATE automáticamente
-- =====================================================
-- Actualiza PRJCLASSIFICATIONDATE cuando se marca como alto riesgo

CREATE OR REPLACE FUNCTION update_classification_date()
RETURNS TRIGGER AS $$
BEGIN
    -- Si se marca como alto riesgo y no tiene fecha de clasificación
    IF NEW.PRJISHIGHRISK = true AND NEW.PRJCLASSIFICATIONDATE IS NULL THEN
        NEW.PRJCLASSIFICATIONDATE := CURRENT_TIMESTAMP;
    END IF;

    -- Si se desmarca como alto riesgo, limpiar campos relacionados
    IF NEW.PRJISHIGHRISK = false OR NEW.PRJISHIGHRISK IS NULL THEN
        NEW.PRJANNEXIIICATEGORIES := NULL;
        NEW.PRJCLASSIFICATIONDATE := NULL;
        NEW.PRJCLASSIFICATIONAUTHOR := NULL;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_classification_date
    BEFORE INSERT OR UPDATE ON PRJPROJECTS
    FOR EACH ROW
    EXECUTE FUNCTION update_classification_date();

COMMENT ON FUNCTION update_classification_date() IS
'Actualiza PRJCLASSIFICATIONDATE automáticamente cuando se marca como alto riesgo';

-- =====================================================
-- TRIGGER 3: Validar coherencia de clasificación
-- =====================================================
-- Valida que si PRJISHIGHRISK = true, debe tener categorías y fecha

CREATE OR REPLACE FUNCTION validate_classification_coherence()
RETURNS TRIGGER AS $$
BEGIN
    -- Si es alto riesgo, debe tener categorías y fecha
    IF NEW.PRJISHIGHRISK = true THEN
        IF NEW.PRJANNEXIIICATEGORIES IS NULL THEN
            RAISE EXCEPTION 'Proyecto de alto riesgo debe tener categorías Anexo III (PRJANNEXIIICATEGORIES)';
        END IF;

        IF NEW.PRJCLASSIFICATIONDATE IS NULL THEN
            RAISE EXCEPTION 'Proyecto de alto riesgo debe tener fecha de clasificación (PRJCLASSIFICATIONDATE)';
        END IF;

        IF NEW.PRJCLASSIFICATIONAUTHOR IS NULL OR NEW.PRJCLASSIFICATIONAUTHOR = '' THEN
            RAISE EXCEPTION 'Proyecto de alto riesgo debe tener autor de clasificación (PRJCLASSIFICATIONAUTHOR)';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_classification_coherence
    BEFORE INSERT OR UPDATE ON PRJPROJECTS
    FOR EACH ROW
    WHEN (NEW.PRJISHIGHRISK = true)
    EXECUTE FUNCTION validate_classification_coherence();

COMMENT ON FUNCTION validate_classification_coherence() IS
'Valida coherencia de datos cuando un proyecto es marcado como alto riesgo';

-- =====================================================
-- TRIGGER 4: Validar Art. 5 antes de clasificar
-- =====================================================
-- Valida que se haya verificado Art. 5 antes de clasificar como alto riesgo

CREATE OR REPLACE FUNCTION validate_art5_before_classification()
RETURNS TRIGGER AS $$
BEGIN
    -- Si se marca como alto riesgo, debe haber verificado Art. 5
    IF NEW.PRJISHIGHRISK = true AND (OLD.PRJISHIGHRISK IS NULL OR OLD.PRJISHIGHRISK = false) THEN
        IF NEW.PRJPROHIBITEDUSECHECKED IS NULL OR NEW.PRJPROHIBITEDUSECHECKED = false THEN
            RAISE EXCEPTION 'Debe verificar Art. 5 (sistemas prohibidos) antes de clasificar como alto riesgo. PRJPROHIBITEDUSECHECKED debe ser true';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_art5_before_classification
    BEFORE UPDATE ON PRJPROJECTS
    FOR EACH ROW
    WHEN (NEW.PRJISHIGHRISK = true AND (OLD.PRJISHIGHRISK IS NULL OR OLD.PRJISHIGHRISK = false))
    EXECUTE FUNCTION validate_art5_before_classification();

COMMENT ON FUNCTION validate_art5_before_classification() IS
'Valida que se haya verificado Art. 5 antes de clasificar como alto riesgo';

-- =====================================================
-- TRIGGER 5: Actualizar UPDATEDAT en ANNANNEXIIICATEGORIES
-- =====================================================
-- Actualiza timestamp cuando se modifica una categoría

CREATE OR REPLACE FUNCTION update_annex_iii_category_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    -- Si no existe ANNCREATEDAT, establecerlo
    IF NEW.ANNCREATEDAT IS NULL THEN
        NEW.ANNCREATEDAT := CURRENT_TIMESTAMP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_annex_iii_category_timestamp
    BEFORE INSERT OR UPDATE ON ANNANNEXIIICATEGORIES
    FOR EACH ROW
    EXECUTE FUNCTION update_annex_iii_category_timestamp();

COMMENT ON FUNCTION update_annex_iii_category_timestamp() IS
'Actualiza ANNCREATEDAT automáticamente en ANNANNEXIIICATEGORIES';

-- =====================================================
-- TRIGGER 6: Validar jerarquía de categorías
-- =====================================================
-- Valida que las subcategorías tengan un padre válido

CREATE OR REPLACE FUNCTION validate_category_hierarchy()
RETURNS TRIGGER AS $$
BEGIN
    -- Si es subcategoría (ANNISLEVEL2 = true), debe tener padre
    IF NEW.ANNISLEVEL2 = true THEN
        IF NEW.ANNPARENTCATEGORY IS NULL THEN
            RAISE EXCEPTION 'Subcategoría (ANNISLEVEL2 = true) debe tener ANNPARENTCATEGORY';
        END IF;

        -- Validar que el padre existe y es categoría principal
        IF NOT EXISTS (
            SELECT 1 FROM ANNANNEXIIICATEGORIES
            WHERE IDXANNEXIIICATEGORY = NEW.ANNPARENTCATEGORY
            AND ANNISLEVEL1 = true
        ) THEN
            RAISE EXCEPTION 'ANNPARENTCATEGORY debe referenciar una categoría principal (ANNISLEVEL1 = true)';
        END IF;
    END IF;

    -- Si es categoría principal (ANNISLEVEL1 = true), no debe tener padre
    IF NEW.ANNISLEVEL1 = true AND NEW.ANNPARENTCATEGORY IS NOT NULL THEN
        RAISE EXCEPTION 'Categoría principal (ANNISLEVEL1 = true) no debe tener ANNPARENTCATEGORY';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_category_hierarchy
    BEFORE INSERT OR UPDATE ON ANNANNEXIIICATEGORIES
    FOR EACH ROW
    EXECUTE FUNCTION validate_category_hierarchy();

COMMENT ON FUNCTION validate_category_hierarchy() IS
'Valida la jerarquía de categorías Anexo III (padre-hijo)';
