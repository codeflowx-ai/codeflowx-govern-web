-- ============================================================================
-- Patch: Agregar FK de Provider a Model
-- Descripción: Un modelo siempre depende de un proveedor (OpenAI, Anthropic, etc.)
-- Fecha: 2025-10-16
-- ============================================================================

-- 1. Agregar columna FK a MODMODELS
ALTER TABLE MODMODELS 
ADD COLUMN IF NOT EXISTS IDMODPROVIDER BIGINT;

-- 2. Crear índice para mejorar performance en joins
CREATE INDEX IF NOT EXISTS idx_modmodels_provider 
ON MODMODELS(IDMODPROVIDER);

-- 3. Agregar constraint de FK
ALTER TABLE MODMODELS 
ADD CONSTRAINT fk_modmodels_provider 
FOREIGN KEY (IDMODPROVIDER) 
REFERENCES MODPROVIDERS(IDXMODELPROVIDER)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- 4. Comentario descriptivo
COMMENT ON COLUMN MODMODELS.IDMODPROVIDER IS 'FK: Proveedor del modelo (OpenAI, Anthropic, Google, etc.)';

-- ============================================================================
-- Notas de aplicación:
-- 1. Esta columna puede ser NULL para modelos legacy sin proveedor asignado
-- 2. Se recomienda poblar los proveedores en MODPROVIDERS antes de asignar
-- 3. El ON DELETE SET NULL preserva los modelos si se elimina un proveedor
-- ============================================================================

-- Verificación:
-- SELECT m.modname, p.moddisplayname as provider 
-- FROM modmodels m 
-- LEFT JOIN modproviders p ON m.idmodprovider = p.idxmodelprovider
-- LIMIT 10;

