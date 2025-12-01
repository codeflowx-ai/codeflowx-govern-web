-- ============================================================================
-- PATCH 12: INC-021 - Versionado FRIA
-- ============================================================================
--
-- **Incidencia:** INC-021
-- **Prioridad:** 🟡 MEDIA
-- **Artículo EU AI Act:** Art. 27
-- **Fecha:** 2025-01-27
--
-- **Descripción:**
-- Agrega soporte de versionado a las evaluaciones FRIA según Art. 27.
-- Permite crear nuevas versiones de FRIA manteniendo historial de cambios.
--
-- **Cambios:**
-- 1. Agregar columna FRIAVERSION (INTEGER) - Número de versión
-- 2. Agregar columna FRIAPREVIOUSVERSIONID (BIGINT) - FK a versión anterior (self-reference)
-- 3. Crear índice para optimizar consultas de historial
-- 4. Agregar constraint de foreign key (self-reference)
--
-- ============================================================================

-- ============================================================================
-- 1. AGREGAR COLUMNAS DE VERSIONADO
-- ============================================================================

-- Agregar columna FRIAVERSION
ALTER TABLE FRIAFUNDAMENTALRIGHTSASSESSMENTS
ADD COLUMN IF NOT EXISTS FRIAVERSION INTEGER DEFAULT 1;

-- Comentario de columna
COMMENT ON COLUMN FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRIAVERSION IS
'Número de versión del FRIA. Se incrementa al crear nuevas versiones (INC-021)';

-- Agregar columna FRIAPREVIOUSVERSIONID
ALTER TABLE FRIAFUNDAMENTALRIGHTSASSESSMENTS
ADD COLUMN IF NOT EXISTS FRIAPREVIOUSVERSIONID BIGINT;

-- Comentario de columna
COMMENT ON COLUMN FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRIAPREVIOUSVERSIONID IS
'Foreign key a versión anterior del mismo FRIA (self-reference). NULL para la primera versión (INC-021)';

-- ============================================================================
-- 2. ACTUALIZAR VERSIONES EXISTENTES
-- ============================================================================

-- Establecer versión 1 para todos los FRIA existentes que no tengan versión
UPDATE FRIAFUNDAMENTALRIGHTSASSESSMENTS
SET FRIAVERSION = 1
WHERE FRIAVERSION IS NULL;

-- ============================================================================
-- 3. CREAR ÍNDICES
-- ============================================================================

-- Índice para búsqueda por versión
CREATE INDEX IF NOT EXISTS IDX_FRIA_VERSION
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAVERSION);

-- Índice para búsqueda de versión anterior (self-reference)
CREATE INDEX IF NOT EXISTS IDX_FRIA_PREVIOUS_VERSION
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAPREVIOUSVERSIONID);

-- Índice compuesto para búsqueda de versiones por proyecto
CREATE INDEX IF NOT EXISTS IDX_FRIA_PROJECT_VERSION
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXPROJECT, FRIAVERSION);

-- ============================================================================
-- 4. AGREGAR FOREIGN KEY CONSTRAINT (SELF-REFERENCE)
-- ============================================================================

-- Agregar foreign key constraint para self-reference
ALTER TABLE FRIAFUNDAMENTALRIGHTSASSESSMENTS
ADD CONSTRAINT FK_FRIA_PREVIOUS_VERSION
FOREIGN KEY (FRIAPREVIOUSVERSIONID)
REFERENCES FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXFRIAASSESSMENT)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- ============================================================================
-- 5. CREAR VISTA DE HISTORIAL DE VERSIONES (OPCIONAL)
-- ============================================================================

CREATE OR REPLACE VIEW VW_FRIA_VERSION_HISTORY AS
SELECT
    f.IDXFRIAASSESSMENT,
    f.IDXPROJECT,
    f.FRIAVERSION,
    f.FRIAPREVIOUSVERSIONID,
    f.FRIACREATEDAT,
    f.FRIAAPPROVED,
    f.FRIANOTIFIED,
    f.FRIACOMPLETENESSCORE,
    f.FRIAIMPACTSEVERITY,
    CASE
        WHEN f.FRIAPREVIOUSVERSIONID IS NULL THEN 'INITIAL'
        ELSE 'REVISION'
    END AS VERSION_TYPE,
    COUNT(pv.IDXFRIAASSESSMENT) AS TOTAL_VERSIONS
FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS f
LEFT JOIN FRIAFUNDAMENTALRIGHTSASSESSMENTS pv
    ON pv.IDXPROJECT = f.IDXPROJECT
    AND pv.FRIAPREVIOUSVERSIONID = f.IDXFRIAASSESSMENT
GROUP BY
    f.IDXFRIAASSESSMENT,
    f.IDXPROJECT,
    f.FRIAVERSION,
    f.FRIAPREVIOUSVERSIONID,
    f.FRIACREATEDAT,
    f.FRIAAPPROVED,
    f.FRIANOTIFIED,
    f.FRIACOMPLETENESSCORE,
    f.FRIAIMPACTSEVERITY;

-- Comentario de vista
COMMENT ON VIEW VW_FRIA_VERSION_HISTORY IS
'Vista que muestra el historial de versiones de FRIA con información de tipo de versión y total de versiones (INC-021)';

-- ============================================================================
-- 6. VERIFICACIÓN
-- ============================================================================

-- Verificar que las columnas se crearon correctamente
DO $$
BEGIN
    -- Verificar columna FRIAVERSION
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'FRIAFUNDAMENTALRIGHTSASSESSMENTS'
        AND column_name = 'FRIAVERSION'
    ) THEN
        RAISE EXCEPTION 'Columna FRIAVERSION no encontrada en FRIAFUNDAMENTALRIGHTSASSESSMENTS';
    END IF;

    -- Verificar columna FRIAPREVIOUSVERSIONID
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'FRIAFUNDAMENTALRIGHTSASSESSMENTS'
        AND column_name = 'FRIAPREVIOUSVERSIONID'
    ) THEN
        RAISE EXCEPTION 'Columna FRIAPREVIOUSVERSIONID no encontrada en FRIAFUNDAMENTALRIGHTSASSESSMENTS';
    END IF;

    RAISE NOTICE 'Patch 12 (INC-021) aplicado correctamente';
END $$;

-- ============================================================================
-- FIN DEL PATCH
-- ============================================================================
