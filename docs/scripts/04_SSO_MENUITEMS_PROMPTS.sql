-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS PROMPTS
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo PROMPTS
-- Total: 16 menu items para PROMPTS
-- =====================================================================

-- =====================================================================
-- MENU 1: GESTIÓN DE PROMPTS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard de Prompts', 'platform/prompts/overview/page.zul', '/platform/prompts/overview/page', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Prompts', 'platform/prompts/overview/summary.zul', '/platform/prompts/overview/summary', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

-- =====================================================================
-- MENU 2: REGISTRO DE PROMPTS (1 item)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Registry', 'platform/prompts/registry/page.zul', '/platform/prompts/registry/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

-- =====================================================================
-- MENU 3: TEMPLATES DE PROMPTS (6 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Performance de Templates', 'platform/prompts/templates/performance.zul', '/platform/prompts/templates/performance', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Templates de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Optimización de Templates', 'platform/prompts/templates/optimization.zul', '/platform/prompts/templates/optimization', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Templates de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis de Costos', 'platform/prompts/templates/cost-analysis.zul', '/platform/prompts/templates/cost-analysis', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Templates de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Métricas', 'platform/prompts/templates/metrics-summary.zul', '/platform/prompts/templates/metrics-summary', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Templates de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resultados de Tests', 'platform/prompts/templates/test-results.zul', '/platform/prompts/templates/test-results', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Templates de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Estadísticas de Uso', 'platform/prompts/templates/usage-statistics.zul', '/platform/prompts/templates/usage-statistics', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Templates de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

-- =====================================================================
-- MENU 4: VALIDACIÓN DE PROMPTS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Overview de Validación', 'platform/prompts/validation/overview.zul', '/platform/prompts/validation/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Validación de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Validación', 'platform/prompts/validation/page.zul', '/platform/prompts/validation/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Validación de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

-- =====================================================================
-- MENU 5: VERSIONADO DE PROMPTS (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Versiones', 'platform/prompts/versioning/overview.zul', '/platform/prompts/versioning/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Versión', 'platform/prompts/versioning/page.zul', '/platform/prompts/versioning/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Historial de Cambios', 'platform/prompts/versioning/history.zul', '/platform/prompts/versioning/history', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de Prompts' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

-- =====================================================================
-- MENU 6: PROMPTS GOBIERNO (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Overview de Gobierno', 'gobierno/prompts/prompts-overview.zul', '/gobierno/prompts/prompts-overview', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Prompts Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Gobierno', 'gobierno/prompts/prompts-detail.zul', '/gobierno/prompts/prompts-detail', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Prompts Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS PROMPTS
-- =====================================================================

