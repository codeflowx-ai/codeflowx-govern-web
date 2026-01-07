-- ============================================================================
-- SCRIPTS DE ÍNDICES ADICIONALES PARA PROHIBITED SYSTEMS
-- ============================================================================
--
-- IMPORTANTE: Estos índices se crean manualmente porque JPA no puede crear
-- ciertos tipos de índices especiales (como GIN para JSONB en PostgreSQL).
--
-- Los índices básicos definidos en las anotaciones @Index de las entidades
-- JPA se crean automáticamente al iniciar la aplicación.
--
-- ORDEN DE EJECUCIÓN:
-- 1. Iniciar aplicación (JPA crea tablas e índices básicos)
-- 2. Ejecutar este script (índices adicionales)
--
-- ============================================================================

-- ============================================================================
-- ÍNDICES PARA GOVPROHIBITEDSYSTEMS
-- ============================================================================

-- Índice GIN para búsqueda eficiente en keywords JSONB
-- Este índice permite búsquedas rápidas dentro del array JSONB de keywords
-- Útil para queries como: WHERE PRSKEYWORDS @> '["keyword"]'::jsonb
CREATE INDEX IF NOT EXISTS idx_prohibited_systems_keywords_gin
ON GOVPROHIBITEDSYSTEMS USING GIN (PRSKEYWORDS);

-- Índice parcial para sistemas activos (solo indexa registros con PRSACTIVE = true)
-- Mejora el rendimiento de consultas que filtran solo sistemas activos
CREATE INDEX IF NOT EXISTS idx_prohibited_systems_active_partial
ON GOVPROHIBITEDSYSTEMS (PRSNAME, PRSCATEGORY)
WHERE PRSACTIVE = true;

-- Índice para búsqueda por fecha de creación (útil para ordenamiento)
-- Ya está definido en JPA, pero se incluye aquí por si acaso
-- CREATE INDEX IF NOT EXISTS idx_prohibited_systems_created_at
-- ON GOVPROHIBITEDSYSTEMS (PRSCREATEDAT DESC);

-- ============================================================================
-- ÍNDICES PARA PRJPROJECTS (Campos de Bloqueo)
-- ============================================================================

-- Índice parcial para proyectos bloqueados (solo indexa registros con PRJDEPLOYMENTBLOCKED = true)
-- Mejora el rendimiento de consultas que buscan proyectos bloqueados
CREATE INDEX IF NOT EXISTS idx_projects_blocked_partial
ON PRJPROJECTS (IDXPROJECT, NAME, PRJBLOCKREASON)
WHERE PRJDEPLOYMENTBLOCKED = true;

-- Índice compuesto para búsquedas de proyectos bloqueados con verificación
-- Útil para queries que filtran por ambos campos
CREATE INDEX IF NOT EXISTS idx_projects_blocked_checked_comp
ON PRJPROJECTS (PRJDEPLOYMENTBLOCKED, PRJPROHIBITEDUSECHECKED, IDXPROJECT)
WHERE PRJDEPLOYMENTBLOCKED = true OR PRJPROHIBITEDUSECHECKED = true;

-- ============================================================================
-- VERIFICACIÓN DE ÍNDICES CREADOS
-- ============================================================================

-- Consulta para verificar que los índices se crearon correctamente
-- SELECT
--     schemaname,
--     tablename,
--     indexname,
--     indexdef
-- FROM pg_indexes
-- WHERE tablename IN ('GOVPROHIBITEDSYSTEMS', 'PRJPROJECTS')
-- ORDER BY tablename, indexname;

-- ============================================================================
-- NOTAS
-- ============================================================================
--
-- 1. Los índices GIN son más pesados que los índices B-tree estándar,
--    pero proporcionan búsquedas mucho más rápidas en datos JSONB.
--
-- 2. Los índices parciales (con WHERE) son más eficientes en espacio
--    y rendimiento cuando solo se necesita indexar un subconjunto de filas.
--
-- 3. Los índices compuestos son útiles cuando se filtran por múltiples
--    columnas frecuentemente.
--
-- 4. Monitorear el tamaño de los índices periódicamente:
--    SELECT pg_size_pretty(pg_relation_size('idx_prohibited_systems_keywords_gin'));
--
-- ============================================================================

