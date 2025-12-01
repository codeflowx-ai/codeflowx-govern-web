-- ============================================================================
-- PATCH 14: INC-015 - Campos de Términos y Compliance en ModelProvider
-- ============================================================================
--
-- **Incidencia:** INC-015
-- **Prioridad:** 🟡 MEDIA
-- **Artículo EU AI Act:** Art. 10
-- **Fecha:** 2025-01-27
--
-- **Descripción:**
-- Agrega campos JSONB en ModelProvider para almacenar información de términos de uso,
-- política de datos y compliance. Esto permite validación genérica y escalable de
-- términos para millones de modelos y miles de proveedores.
--
-- **Cambios:**
-- 1. Agregar columna MODTERMSOFUSE (JSONB) - Términos de uso del proveedor
-- 2. Agregar columna MODDATAPOLICY (JSONB) - Política de datos
-- 3. Agregar columna MODCOMPLIANCEINFO (JSONB) - Información de compliance
--
-- **Estructura JSON esperada:**
--
-- MODTERMSOFUSE:
-- {
--   "url": "https://...",
--   "version": "2024-01",
--   "lastUpdated": "2024-01-15"
-- }
--
-- MODDATAPOLICY:
-- {
--   "allowsPersonalData": true,
--   "optOutAvailable": true,
--   "optOutRequired": false,
--   "trainingDataPolicy": "OPT_IN" | "OPT_OUT" | "NEVER",
--   "dataRetentionDays": 30
-- }
--
-- MODCOMPLIANCEINFO:
-- {
--   "gdprCompliant": true,
--   "euAiActCompliant": true,
--   "certifications": ["ISO 27001", "SOC 2"],
--   "dataResidency": ["EU", "US"]
-- }
--
-- ============================================================================

-- ============================================================================
-- 1. AGREGAR COLUMNAS
-- ============================================================================

-- Columna para términos de uso
ALTER TABLE MODPROVIDERS
ADD COLUMN IF NOT EXISTS MODTERMSOFUSE JSONB;

-- Columna para política de datos
ALTER TABLE MODPROVIDERS
ADD COLUMN IF NOT EXISTS MODDATAPOLICY JSONB;

-- Columna para información de compliance
ALTER TABLE MODPROVIDERS
ADD COLUMN IF NOT EXISTS MODCOMPLIANCEINFO JSONB;

-- ============================================================================
-- 2. COMENTARIOS DE COLUMNAS
-- ============================================================================

COMMENT ON COLUMN MODPROVIDERS.MODTERMSOFUSE IS
'JSON con términos de uso del proveedor. Estructura: { "url": "...", "version": "...", "lastUpdated": "..." }';

COMMENT ON COLUMN MODPROVIDERS.MODDATAPOLICY IS
'JSON con política de datos del proveedor. Estructura: { "allowsPersonalData": boolean, "optOutAvailable": boolean, "trainingDataPolicy": "OPT_IN|OPT_OUT|NEVER", "dataRetentionDays": number }';

COMMENT ON COLUMN MODPROVIDERS.MODCOMPLIANCEINFO IS
'JSON con información de compliance del proveedor. Estructura: { "gdprCompliant": boolean, "euAiActCompliant": boolean, "certifications": [...], "dataResidency": [...] }';

-- ============================================================================
-- 3. ÍNDICES (opcional, para búsquedas por compliance)
-- ============================================================================

-- Índice GIN para búsquedas en JSONB de compliance
CREATE INDEX IF NOT EXISTS IDX_MODPROVIDERS_COMPLIANCE_GIN
ON MODPROVIDERS USING GIN (MODCOMPLIANCEINFO)
WHERE MODCOMPLIANCEINFO IS NOT NULL;

-- Índice GIN para búsquedas en JSONB de política de datos
CREATE INDEX IF NOT EXISTS IDX_MODPROVIDERS_DATAPOLICY_GIN
ON MODPROVIDERS USING GIN (MODDATAPOLICY)
WHERE MODDATAPOLICY IS NOT NULL;

-- ============================================================================
-- 4. EJEMPLO DE DATOS PARA PROVEEDORES COMUNES
-- ============================================================================

-- Ejemplo: OpenAI
UPDATE MODPROVIDERS
SET
    MODTERMSOFUSE = '{"url": "https://openai.com/policies/terms-of-use", "version": "2024-01", "lastUpdated": "2024-01-15"}'::jsonb,
    MODDATAPOLICY = '{"allowsPersonalData": true, "optOutAvailable": true, "optOutRequired": false, "trainingDataPolicy": "OPT_OUT", "dataRetentionDays": 30}'::jsonb,
    MODCOMPLIANCEINFO = '{"gdprCompliant": true, "euAiActCompliant": true, "certifications": ["SOC 2"], "dataResidency": ["US", "EU"]}'::jsonb
WHERE MODNAME ILIKE '%openai%'
  AND (MODTERMSOFUSE IS NULL OR MODDATAPOLICY IS NULL OR MODCOMPLIANCEINFO IS NULL);

-- Ejemplo: Anthropic
UPDATE MODPROVIDERS
SET
    MODTERMSOFUSE = '{"url": "https://www.anthropic.com/terms", "version": "2024-01", "lastUpdated": "2024-01-10"}'::jsonb,
    MODDATAPOLICY = '{"allowsPersonalData": true, "optOutAvailable": true, "optOutRequired": false, "trainingDataPolicy": "OPT_OUT", "dataRetentionDays": 30}'::jsonb,
    MODCOMPLIANCEINFO = '{"gdprCompliant": true, "euAiActCompliant": true, "certifications": ["SOC 2"], "dataResidency": ["US"]}'::jsonb
WHERE MODNAME ILIKE '%anthropic%'
  AND (MODTERMSOFUSE IS NULL OR MODDATAPOLICY IS NULL OR MODCOMPLIANCEINFO IS NULL);

-- ============================================================================
-- 5. VERIFICACIÓN
-- ============================================================================

-- Verificar que las columnas se crearon correctamente
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'MODPROVIDERS' AND column_name = 'MODTERMSOFUSE'
    ) THEN
        RAISE EXCEPTION 'Columna MODTERMSOFUSE no encontrada';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'MODPROVIDERS' AND column_name = 'MODDATAPOLICY'
    ) THEN
        RAISE EXCEPTION 'Columna MODDATAPOLICY no encontrada';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'MODPROVIDERS' AND column_name = 'MODCOMPLIANCEINFO'
    ) THEN
        RAISE EXCEPTION 'Columna MODCOMPLIANCEINFO no encontrada';
    END IF;

    RAISE NOTICE 'Patch 14 (INC-015) aplicado correctamente';
END $$;

-- ============================================================================
-- FIN DEL PATCH
-- ============================================================================

