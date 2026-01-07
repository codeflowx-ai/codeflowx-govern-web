-- ============================================================================
-- SCRIPT DE CREACIÓN DE ÍNDICES Y CONSTRAINTS
-- Módulo: Technical Documentation
-- Tabla: GOVAIACTTECHNICALDOCS
-- Fecha: Diciembre 2025
-- ============================================================================

-- Este script crea los índices y constraints únicos definidos en la entidad JPA
-- Ejecutar después de crear la tabla o cuando se actualice el schema

-- ============================================================================
-- ÍNDICES SIMPLES
-- ============================================================================

-- Índice para búsqueda por tipo de entidad
CREATE INDEX IF NOT EXISTS idx_techdocs_entity_type
ON GOVAIACTTECHNICALDOCS(ENTITY_TYPE);

-- Índice para búsqueda por ID de entidad
CREATE INDEX IF NOT EXISTS idx_techdocs_entity_id
ON GOVAIACTTECHNICALDOCS(ENTITY_ID);

-- Índice para filtrado por estado
CREATE INDEX IF NOT EXISTS idx_techdocs_status
ON GOVAIACTTECHNICALDOCS(STATUS);

-- Índice para búsqueda por nombre de sistema
CREATE INDEX IF NOT EXISTS idx_techdocs_system_name
ON GOVAIACTTECHNICALDOCS(SYSTEM_NAME);

-- Índice para filtrado por versión
CREATE INDEX IF NOT EXISTS idx_techdocs_version
ON GOVAIACTTECHNICALDOCS(VERSION);

-- Índice para ordenación por fecha de creación
CREATE INDEX IF NOT EXISTS idx_techdocs_created_at
ON GOVAIACTTECHNICALDOCS(CREATED_AT);

-- Índice para ordenación por fecha de actualización
CREATE INDEX IF NOT EXISTS idx_techdocs_updated_at
ON GOVAIACTTECHNICALDOCS(UPDATED_AT);

-- Índice para consultas por fecha de generación
CREATE INDEX IF NOT EXISTS idx_techdocs_generated_at
ON GOVAIACTTECHNICALDOCS(GENERATED_AT);

-- ============================================================================
-- ÍNDICES COMPUESTOS
-- ============================================================================

-- Índice compuesto para búsqueda por tipo e ID de entidad (uso más frecuente)
CREATE INDEX IF NOT EXISTS idx_techdocs_entity
ON GOVAIACTTECHNICALDOCS(ENTITY_TYPE, ENTITY_ID);

-- Índice compuesto para filtrado por estado y tipo de entidad
CREATE INDEX IF NOT EXISTS idx_techdocs_status_entity
ON GOVAIACTTECHNICALDOCS(STATUS, ENTITY_TYPE);

-- Índice compuesto para ordenación por estado y fecha de creación
CREATE INDEX IF NOT EXISTS idx_techdocs_status_created
ON GOVAIACTTECHNICALDOCS(STATUS, CREATED_AT);

-- ============================================================================
-- CONSTRAINTS ÚNICOS
-- ============================================================================

-- Constraint único: solo una documentación por entidad
-- Esto garantiza que no puede haber duplicados de documentación para la misma entidad
CREATE UNIQUE INDEX IF NOT EXISTS uk_techdocs_entity
ON GOVAIACTTECHNICALDOCS(ENTITY_TYPE, ENTITY_ID);

-- Nota: Si se necesita usar ALTER TABLE para agregar constraint único:
-- ALTER TABLE GOVAIACTTECHNICALDOCS
-- ADD CONSTRAINT uk_techdocs_entity UNIQUE (ENTITY_TYPE, ENTITY_ID);

-- ============================================================================
-- COMENTARIOS
-- ============================================================================

COMMENT ON INDEX idx_techdocs_entity IS 'Índice compuesto para búsqueda rápida de documentación por tipo e ID de entidad';
COMMENT ON INDEX idx_techdocs_status IS 'Índice para filtrado por estado de documentación';
COMMENT ON INDEX idx_techdocs_status_entity IS 'Índice compuesto para filtrado combinado por estado y tipo de entidad';
COMMENT ON INDEX uk_techdocs_entity IS 'Constraint único que garantiza una sola documentación por entidad';

-- ============================================================================
-- VERIFICACIÓN
-- ============================================================================

-- Consulta para verificar índices creados
-- SELECT
--     indexname,
--     indexdef
-- FROM pg_indexes
-- WHERE tablename = 'govaiacttechnicaldocs'
-- ORDER BY indexname;

-- Consulta para verificar constraints únicos
-- SELECT
--     conname AS constraint_name,
--     contype AS constraint_type,
--     pg_get_constraintdef(oid) AS constraint_definition
-- FROM pg_constraint
-- WHERE conrelid = 'govaiacttechnicaldocs'::regclass
--   AND contype = 'u';

