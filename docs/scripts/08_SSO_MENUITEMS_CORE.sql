-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS CORE
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo CORE
-- Total: 29 menu items para CORE
-- =====================================================================

-- =====================================================================
-- MENU 1: ADMINISTRACIÓN (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard Admin', 'gobierno/core/admin-dashboard.zul', '/gobierno/core/admin-dashboard', true, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Administración' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen Dashboard', 'platform/core/admin-dashboard-summary-overview.zul', '/platform/core/admin-dashboard-summary-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Administración' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 2: GESTIÓN DE USUARIOS (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Usuarios (Gobierno)', 'gobierno/core/users.zul', '/gobierno/core/users', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Usuarios' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Usuarios', 'platform/core/user-overview.zul', '/platform/core/user-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Usuarios' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Usuario', 'platform/core/user-detail.zul', '/platform/core/user-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Usuarios' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 3: ROLES Y PERMISOS (6 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestión de Roles (Gobierno)', 'gobierno/core/roles.zul', '/gobierno/core/roles', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Roles y Permisos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Roles', 'platform/core/role-overview.zul', '/platform/core/role-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Roles y Permisos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Rol', 'platform/core/role-detail.zul', '/platform/core/role-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Roles y Permisos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestión de Permisos (Gobierno)', 'gobierno/core/permissions.zul', '/gobierno/core/permissions', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Roles y Permisos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Permisos', 'platform/core/permission-overview.zul', '/platform/core/permission-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Roles y Permisos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Permiso', 'platform/core/permission-detail.zul', '/platform/core/permission-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Roles y Permisos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 4: DEPARTAMENTOS (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestión de Departamentos (Gobierno)', 'gobierno/core/departments.zul', '/gobierno/core/departments', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Departamentos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Departamentos', 'platform/core/department-overview.zul', '/platform/core/department-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Departamentos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Departamento', 'platform/core/department-detail.zul', '/platform/core/department-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Departamentos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 5: GESTIÓN DE MENÚS (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestión de Menús (Gobierno)', 'gobierno/core/menus.zul', '/gobierno/core/menus', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Menús' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Menús', 'platform/core/menu-overview.zul', '/platform/core/menu-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Menús' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Menú', 'platform/core/menu-detail.zul', '/platform/core/menu-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Menús' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 6: SEGURIDAD (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Auditoría de Seguridad (Gobierno)', 'gobierno/core/security-audit.zul', '/gobierno/core/security-audit', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Seguridad' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Auditoría', 'platform/core/security-audit-summary-overview.zul', '/platform/core/security-audit-summary-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Seguridad' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Intentos de Login (Gobierno)', 'gobierno/core/login-attempts.zul', '/gobierno/core/login-attempts', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Seguridad' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Intentos', 'platform/core/login-attempt-overview.zul', '/platform/core/login-attempt-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Seguridad' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Intento', 'platform/core/login-attempt-detail.zul', '/platform/core/login-attempt-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Seguridad' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 7: SESIONES DE USUARIO (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Sesiones de Usuario (Gobierno)', 'gobierno/core/user-sessions.zul', '/gobierno/core/user-sessions', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Sesiones de Usuario' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Sesiones', 'platform/core/user-session-overview.zul', '/platform/core/user-session-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Sesiones de Usuario' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Sesión', 'platform/core/user-session-detail.zul', '/platform/core/user-session-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Sesiones de Usuario' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 8: ACTIVIDAD DE USUARIOS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Actividad de Usuario (Gobierno)', 'gobierno/core/user-activity.zul', '/gobierno/core/user-activity', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Actividad de Usuarios' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Actividad', 'platform/core/user-activity-summary-overview.zul', '/platform/core/user-activity-summary-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Actividad de Usuarios' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

-- =====================================================================
-- MENU 9: SALUD DEL SISTEMA (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Salud del Sistema (Gobierno)', 'gobierno/core/system-health.zul', '/gobierno/core/system-health', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Salud del Sistema' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Overview de Salud', 'platform/core/system-health-overview-overview.zul', '/platform/core/system-health-overview-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Salud del Sistema' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS CORE
-- =====================================================================

