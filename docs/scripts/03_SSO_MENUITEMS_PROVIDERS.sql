-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS PROVIDERS
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo PROVIDERS
-- Total: 4 menu items para PROVIDERS
-- =====================================================================

-- =====================================================================
-- MENU 1: GESTIÓN DE PROVIDERS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard de Providers', 'platform/providers/providers-overview-overview.zul', '/platform/providers/providers-overview-overview', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Providers' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Providers')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Métricas', 'platform/providers/providers-metrics-summary-overview.zul', '/platform/providers/providers-metrics-summary-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Providers' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Providers')));

-- =====================================================================
-- MENU 2: PROVIDERS GOBIERNO (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Overview de Gobierno', 'gobierno/providers/providers-overview.zul', '/gobierno/providers/providers-overview', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Providers Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Providers')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Gobierno', 'gobierno/providers/providers-detail.zul', '/gobierno/providers/providers-detail', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Providers Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Providers')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS PROVIDERS
-- =====================================================================

