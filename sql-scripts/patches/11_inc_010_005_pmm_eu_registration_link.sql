-- ============================================================================
-- PATCH 11: INC-010-005 - Vinculación PMM con Registro Art. 49
-- ============================================================================
--
-- **Incidencia:** INC-010-005
-- **Prioridad:** 🔴 CRÍTICA (Certification Blocker)
-- **Artículo EU AI Act:** Art. 16.h
-- **Fecha:** 2025-01-27
--
-- **Descripción:**
-- Vincula Post-Market Monitoring (PMM) con Registro EU según Art. 49.
-- Art. 16.h requiere registrar el sistema de conformidad, incluyendo el sistema PMM.
--
-- **Cambios:**
-- 1. Agregar FK IDXEUREGISTRATION en PMMPOSTMARKETMONITORINGS
-- 2. Agregar FK IDXPOSTMARKETMONITORING en REGEUREGISTRATIONS
-- 3. Crear índices para optimizar consultas
-- 4. Agregar constraints de foreign key
--
-- ============================================================================

-- ============================================================================
-- 1. AGREGAR FK EN PMMPOSTMARKETMONITORINGS
-- ============================================================================

-- Agregar columna IDXEUREGISTRATION
ALTER TABLE PMMPOSTMARKETMONITORINGS
ADD COLUMN IF NOT EXISTS IDXEUREGISTRATION BIGINT;

-- Comentario de columna
COMMENT ON COLUMN PMMPOSTMARKETMONITORINGS.IDXEUREGISTRATION IS
'Foreign key a REGEUREGISTRATIONS. Vincula PMM con registro EU según Art. 16.h (INC-010-005)';

-- Crear índice
CREATE INDEX IF NOT EXISTS IDX_PMM_EUREGISTRATION
ON PMMPOSTMARKETMONITORINGS(IDXEUREGISTRATION);

-- Agregar foreign key constraint
ALTER TABLE PMMPOSTMARKETMONITORINGS
ADD CONSTRAINT FK_PMM_EUREGISTRATION
FOREIGN KEY (IDXEUREGISTRATION)
REFERENCES REGEUREGISTRATIONS(IDXEUREGISTRATION)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- ============================================================================
-- 2. AGREGAR FK EN REGEUREGISTRATIONS
-- ============================================================================

-- Agregar columna IDXPOSTMARKETMONITORING
ALTER TABLE REGEUREGISTRATIONS
ADD COLUMN IF NOT EXISTS IDXPOSTMARKETMONITORING BIGINT;

-- Comentario de columna
COMMENT ON COLUMN REGEUREGISTRATIONS.IDXPOSTMARKETMONITORING IS
'Foreign key a PMMPOSTMARKETMONITORINGS. Vincula registro EU con PMM según Art. 16.h (INC-010-005)';

-- Crear índice
CREATE INDEX IF NOT EXISTS IDX_EUR_PMM
ON REGEUREGISTRATIONS(IDXPOSTMARKETMONITORING);

-- Agregar foreign key constraint
ALTER TABLE REGEUREGISTRATIONS
ADD CONSTRAINT FK_EUR_PMM
FOREIGN KEY (IDXPOSTMARKETMONITORING)
REFERENCES PMMPOSTMARKETMONITORINGS(IDXPOSTMARKETMONITORING)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- ============================================================================
-- 3. CREAR VISTA DE RELACIÓN REGISTRO + PMM (OPCIONAL)
-- ============================================================================

CREATE OR REPLACE VIEW VW_EUR_REGISTRATIONS_WITH_PMM AS
SELECT
    eur.IDXEUREGISTRATION,
    eur.IDXPROJECT,
    eur.REGSTATUS,
    eur.IDXPOSTMARKETMONITORING,
    eur.REGEUREGISTRATIONID,
    pmm.IDXPOSTMARKETMONITORING AS PMM_ID,
    pmm.PMMPLANNAME,
    pmm.PMMFREQUENCY,
    pmm.PMMSTATUS,
    pmm.PMMCREATEDAT,
    pmm.PMMLASTMONITORINGDATE,
    pmm.PMMNEXTMONITORINGDATE,
    CASE
        WHEN eur.IDXPOSTMARKETMONITORING IS NOT NULL AND pmm.PMMSTATUS = 'ACTIVE'
        THEN 'COMPLIANT'
        ELSE 'NON_COMPLIANT'
    END AS ART16H_COMPLIANCE_STATUS
FROM REGEUREGISTRATIONS eur
LEFT JOIN PMMPOSTMARKETMONITORINGS pmm ON eur.IDXPOSTMARKETMONITORING = pmm.IDXPOSTMARKETMONITORING;

-- Comentario de vista
COMMENT ON VIEW VW_EUR_REGISTRATIONS_WITH_PMM IS
'Vista que muestra registros EU con información de PMM vinculado. Útil para verificar cumplimiento Art. 16.h (INC-010-005)';

-- ============================================================================
-- 4. VERIFICACIÓN
-- ============================================================================

-- Verificar que las columnas se crearon correctamente
DO $$
BEGIN
    -- Verificar columna en PMMPOSTMARKETMONITORINGS
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'PMMPOSTMARKETMONITORINGS'
        AND column_name = 'IDXEUREGISTRATION'
    ) THEN
        RAISE EXCEPTION 'Columna IDXEUREGISTRATION no encontrada en PMMPOSTMARKETMONITORINGS';
    END IF;

    -- Verificar columna en REGEUREGISTRATIONS
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'REGEUREGISTRATIONS'
        AND column_name = 'IDXPOSTMARKETMONITORING'
    ) THEN
        RAISE EXCEPTION 'Columna IDXPOSTMARKETMONITORING no encontrada en REGEUREGISTRATIONS';
    END IF;

    RAISE NOTICE 'Patch 11 (INC-010-005) aplicado correctamente';
END $$;

-- ============================================================================
-- FIN DEL PATCH
-- ============================================================================

