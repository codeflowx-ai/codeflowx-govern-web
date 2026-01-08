-- ============================================================================
-- CLAVES ÚNICAS PARA TABLA GOVCONFORMITYDECLARATIONS
-- ============================================================================
-- Descripción: Script para crear constraints de unicidad
-- Fecha: Enero 2025
-- Módulo: Conformity Declaration
-- ============================================================================

-- Constraint único: ASSESSMENT_ID + AI_SYSTEM_VERSION
-- Propósito: Evitar declaraciones duplicadas con la misma versión para un assessment
-- Regla de negocio: Un assessment puede tener múltiples declaraciones, pero cada versión debe ser única
CREATE UNIQUE INDEX IF NOT EXISTS UK_DECL_ASSESSMENT_VERSION
ON GOVCONFORMITYDECLARATIONS(ASSESSMENT_ID, AI_SYSTEM_VERSION)
WHERE AI_SYSTEM_VERSION IS NOT NULL;

-- NOTA: En PostgreSQL, usamos CREATE UNIQUE INDEX con WHERE para permitir NULLs
-- Si se requiere constraint a nivel de tabla, usar:
-- ALTER TABLE GOVCONFORMITYDECLARATIONS
-- ADD CONSTRAINT UK_DECL_ASSESSMENT_VERSION
-- UNIQUE (ASSESSMENT_ID, AI_SYSTEM_VERSION);

-- ============================================================================
-- VALIDACIÓN DE INTEGRIDAD
-- ============================================================================
--
-- Esta constraint asegura que:
-- 1. No se pueden crear dos declaraciones con la misma versión para un assessment
-- 2. Permite múltiples declaraciones del mismo assessment con versiones diferentes
-- 3. Evita errores de lógica de negocio en la generación automática de versiones
--
-- Ejemplo de violación que previene:
-- - Assessment 1, Versión v1.0 (permitido)
-- - Assessment 1, Versión v1.0 (BLOQUEADO - duplicado)
-- - Assessment 1, Versión v1.1 (permitido)
-- ============================================================================
