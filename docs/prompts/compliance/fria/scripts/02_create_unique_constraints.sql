-- ============================================================================
-- SCRIPT DE CREACIÓN DE CONSTRAINTS ÚNICOS PARA FRIA
-- ============================================================================
-- Módulo: FRIA (Fundamental Rights Impact Assessment)
-- Tabla: FRIAFUNDAMENTALRIGHTSASSESSMENTS
-- Fecha: Diciembre 2025
-- Descripción: Constraints únicos para garantizar integridad de datos
-- ============================================================================

-- NOTA: Estos constraints se crean automáticamente mediante JPA cuando se usa
-- hibernate.hbm2ddl.auto=update o validate. Este script es para referencia
-- o para aplicar manualmente en producción.

-- ============================================================================
-- CONSTRAINTS ÚNICOS
-- ============================================================================

-- Constraint único en UUID
-- Garantiza que cada FRIA tenga un UUID único
-- Ya está definido en la columna con @Column(unique = true)
-- Se crea automáticamente, pero se documenta aquí para referencia
-- ALTER TABLE FRIAFUNDAMENTALRIGHTSASSESSMENTS
-- ADD CONSTRAINT UK_FRIA_UUID UNIQUE (iduuid);

-- Constraint único en Notification ID
-- Garantiza que cada ID de notificación sea único (si existe)
-- Permite NULL para FRIAs no notificadas
-- ALTER TABLE FRIAFUNDAMENTALRIGHTSASSESSMENTS
-- ADD CONSTRAINT UK_FRIA_NOTIFICATION_ID UNIQUE (FRIANOTIFICATIONID);

-- ============================================================================
-- NOTAS IMPORTANTES
-- ============================================================================
--
-- 1. El constraint UK_FRIA_UUID es obligatorio y se crea automáticamente
--    mediante JPA con @Column(unique = true).
--
-- 2. El constraint UK_FRIA_NOTIFICATION_ID permite NULL, por lo que múltiples
--    filas pueden tener NULL sin violar la unicidad. Solo los valores no NULL
--    deben ser únicos.
--
-- 3. En PostgreSQL, los constraints únicos crean automáticamente un índice
--    único, por lo que no es necesario crear índices adicionales para estos
--    campos.
--
-- ============================================================================

