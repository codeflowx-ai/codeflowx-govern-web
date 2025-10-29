-- =====================================================================
-- SCRIPT MAESTRO DE POBLACIÓN SSO - CODEFLOWX GOVERN
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Script maestro que ejecuta todos los scripts de población SSO
-- 
-- ORDEN DE EJECUCIÓN:
--   1. Applications (9 registros)
--   2. Menus (74 registros)
--   3. Menu Items (265 registros) - divididos por módulo
--
-- TOTAL: 9 Applications | 74 Menus | 265 Menu Items
-- =====================================================================

-- =====================================================================
-- IMPORTANTE: EJECUTAR EN ORDEN
-- =====================================================================
-- Este script debe ejecutarse EN ORDEN SECUENCIAL debido a las
-- dependencias de Foreign Keys entre tablas:
--   SSOAPLICACION → SSOMENU → SSOMENUITEM
-- =====================================================================

\echo '====================================================================='
\echo 'INICIANDO POBLACIÓN SSO - CODEFLOWX GOVERN'
\echo '====================================================================='
\echo ''

-- =====================================================================
-- PASO 1: APPLICATIONS Y MENUS
-- =====================================================================

\echo 'Paso 1/10: Insertando Applications y Menus...'
\i 01_SSO_APPLICATIONS_MENUS.sql
\echo 'Completado: 9 Applications y 74 Menus insertados'
\echo ''

-- =====================================================================
-- PASO 2-10: MENU ITEMS POR MÓDULO
-- =====================================================================

\echo 'Paso 2/10: Insertando Menu Items - AGENTS (52 items)...'
\i 02_SSO_MENUITEMS_AGENTS.sql
\echo 'Completado'
\echo ''

\echo 'Paso 3/10: Insertando Menu Items - PROVIDERS (4 items)...'
\i 03_SSO_MENUITEMS_PROVIDERS.sql
\echo 'Completado'
\echo ''

\echo 'Paso 4/10: Insertando Menu Items - PROMPTS (16 items)...'
\i 04_SSO_MENUITEMS_PROMPTS.sql
\echo 'Completado'
\echo ''

\echo 'Paso 5/10: Insertando Menu Items - RAG (17 items)...'
\i 05_SSO_MENUITEMS_RAG.sql
\echo 'Completado'
\echo ''

\echo 'Paso 6/10: Insertando Menu Items - MODELS (30 items)...'
\i 06_SSO_MENUITEMS_MODELS.sql
\echo 'Completado'
\echo ''

\echo 'Paso 7/10: Insertando Menu Items - SERVING (26 items)...'
\i 07_SSO_MENUITEMS_SERVING.sql
\echo 'Completado'
\echo ''

\echo 'Paso 8/10: Insertando Menu Items - CORE (29 items)...'
\i 08_SSO_MENUITEMS_CORE.sql
\echo 'Completado'
\echo ''

\echo 'Paso 9/10: Insertando Menu Items - GOVERNANCE (47 items)...'
\i 09_SSO_MENUITEMS_GOVERNANCE.sql
\echo 'Completado'
\echo ''

\echo 'Paso 10/10: Insertando Menu Items - PROJECTS (44 items)...'
\i 10_SSO_MENUITEMS_PROJECTS.sql
\echo 'Completado'
\echo ''

-- =====================================================================
-- VERIFICACIÓN FINAL
-- =====================================================================

\echo '====================================================================='
\echo 'VERIFICANDO INSERCIÓN...'
\echo '====================================================================='

SELECT 'Applications insertadas:' AS descripcion, COUNT(*) AS total FROM SSOAPLICACION;
SELECT 'Menus insertados:' AS descripcion, COUNT(*) AS total FROM SSOMENU;
SELECT 'Menu Items insertados:' AS descripcion, COUNT(*) AS total FROM SSOMENUITEM;

\echo ''
\echo '====================================================================='
\echo 'POBLACIÓN SSO COMPLETADA EXITOSAMENTE'
\echo '====================================================================='
\echo 'Applications: 9'
\echo 'Menus: 74'
\echo 'Menu Items: 265'
\echo '====================================================================='

