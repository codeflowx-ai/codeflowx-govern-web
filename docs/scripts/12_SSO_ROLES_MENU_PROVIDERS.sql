-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - PERMISOS DE ROLES PARA MENU ITEMS (PROVIDERS)
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Asignación de permisos de roles a menu items del módulo PROVIDERS
-- 
-- Estructura SSOROLESMENU:
--   - IDXSSOROLESMENU: PK autonumérico (no se incluye en INSERT)
--   - IDSSOMENUITEM0: FK al menu item (mediante subconsulta por ITEM + PAGE)
--   - IDSSOROL0: FK al rol (mediante subconsulta por ROL)
-- 
-- Roles simplificados (mapeo directo):
--   Admin → Administracion
--   Manager → Governance Manager
--   Developer → AI Developer
--   Viewer → End User
-- 
-- Total PROVIDERS: 4 menu items con permisos asignados
-- =====================================================================

-- 1. Dashboard de Providers - Admin, Manager, Developer, Viewer
INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Dashboard de Providers' AND PAGE = 'platform/providers/providers-overview-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Administracion' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Dashboard de Providers' AND PAGE = 'platform/providers/providers-overview-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Governance Manager' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Dashboard de Providers' AND PAGE = 'platform/providers/providers-overview-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'AI Developer' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Dashboard de Providers' AND PAGE = 'platform/providers/providers-overview-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'End User' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);


-- 2. Resumen de Métricas - Admin, Manager, Developer, Viewer
INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Resumen de Métricas' AND PAGE = 'platform/providers/providers-metrics-summary-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Administracion' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Resumen de Métricas' AND PAGE = 'platform/providers/providers-metrics-summary-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Governance Manager' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Resumen de Métricas' AND PAGE = 'platform/providers/providers-metrics-summary-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'AI Developer' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Resumen de Métricas' AND PAGE = 'platform/providers/providers-metrics-summary-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'End User' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);


-- 3. Overview de Gobierno - Admin, Manager, Developer, Viewer
INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Overview de Gobierno' AND PAGE = 'gobierno/providers/providers-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Administracion' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Overview de Gobierno' AND PAGE = 'gobierno/providers/providers-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Governance Manager' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Overview de Gobierno' AND PAGE = 'gobierno/providers/providers-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'AI Developer' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Overview de Gobierno' AND PAGE = 'gobierno/providers/providers-overview.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'End User' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);


-- 4. Detalle de Gobierno - Admin, Manager, Developer, Viewer
INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Detalle de Gobierno' AND PAGE = 'gobierno/providers/providers-detail.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Administracion' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Detalle de Gobierno' AND PAGE = 'gobierno/providers/providers-detail.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'Governance Manager' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Detalle de Gobierno' AND PAGE = 'gobierno/providers/providers-detail.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'AI Developer' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

INSERT INTO SSOROLESMENU (IDSSOMENUITEM0, IDSSOROL0)
VALUES (
  (SELECT IDXSSOMENUITEM FROM SSOMENUITEM WHERE ITEM = 'Detalle de Gobierno' AND PAGE = 'gobierno/providers/providers-detail.zul' LIMIT 1),
  (SELECT IDXSSOROL FROM SSOROL WHERE ROL = 'End User' AND (BAJA IS NULL OR BAJA > CURRENT_DATE) LIMIT 1)
);

-- =====================================================================
-- FIN DEL SCRIPT - PERMISOS DE ROLES PARA MENU ITEMS (PROVIDERS)
-- =====================================================================
