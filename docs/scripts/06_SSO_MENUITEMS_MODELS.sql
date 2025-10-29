-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS MODELS
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo MODELS
-- Total: 30 menu items para MODELS
-- =====================================================================

-- =====================================================================
-- MENU 1: GESTIÓN DE MODELOS (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard de Modelos', 'platform/models/overview/page.zul', '/platform/models/overview/page', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Modelos', 'platform/models/overview/summary.zul', '/platform/models/overview/summary', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Crear Modelo', 'platform/models/create/page.zul', '/platform/models/create/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

-- =====================================================================
-- MENU 2: REGISTRO DE MODELOS (12 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Catálogo', 'platform/models/registry/catalog-overview.zul', '/platform/models/registry/catalog-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Catálogo', 'platform/models/registry/catalog.zul', '/platform/models/registry/catalog', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Providers', 'platform/models/registry/provider-overview.zul', '/platform/models/registry/provider-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Provider', 'platform/models/registry/provider.zul', '/platform/models/registry/provider', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Credenciales', 'platform/models/registry/provider-credential-overview.zul', '/platform/models/registry/provider-credential-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Credencial', 'platform/models/registry/provider-credential.zul', '/platform/models/registry/provider-credential', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Endpoints', 'platform/models/registry/endpoint-overview.zul', '/platform/models/registry/endpoint-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Endpoint', 'platform/models/registry/endpoint.zul', '/platform/models/registry/endpoint', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Capabilities', 'platform/models/registry/capability-overview.zul', '/platform/models/registry/capability-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Capability', 'platform/models/registry/capability.zul', '/platform/models/registry/capability', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Artifacts', 'platform/models/registry/artifact-overview.zul', '/platform/models/registry/artifact-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Artifact', 'platform/models/registry/artifact.zul', '/platform/models/registry/artifact', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

-- =====================================================================
-- MENU 3: PERFORMANCE DE MODELOS (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard de Performance', 'platform/models/performance/overview.zul', '/platform/models/performance/overview', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Performance de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Performance', 'platform/models/performance/page.zul', '/platform/models/performance/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Performance de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Métricas', 'platform/models/performance/metrics-summary.zul', '/platform/models/performance/metrics-summary', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Performance de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Uso', 'platform/models/performance/usage-overview.zul', '/platform/models/performance/usage-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Performance de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Uso', 'platform/models/performance/usage.zul', '/platform/models/performance/usage', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Performance de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

-- =====================================================================
-- MENU 4: VERSIONADO DE MODELOS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Versiones', 'platform/models/versioning/overview.zul', '/platform/models/versioning/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Versión', 'platform/models/versioning/page.zul', '/platform/models/versioning/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de Modelos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

-- =====================================================================
-- MENU 5: DEPENDENCIAS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Dependencias', 'platform/models/dependencies/overview.zul', '/platform/models/dependencies/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Dependencias' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Dependencias', 'platform/models/dependencies/page.zul', '/platform/models/dependencies/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Dependencias' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

-- =====================================================================
-- MENU 6: EXPLICABILIDAD (XAI) (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Explicabilidad', 'platform/models/explainability/overview.zul', '/platform/models/explainability/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Explicabilidad (XAI)' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis de Explicabilidad', 'platform/models/explainability/page.zul', '/platform/models/explainability/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Explicabilidad (XAI)' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

-- =====================================================================
-- MENU 7: ANÁLISIS DE SESGO (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Análisis de Sesgo', 'platform/models/bias-analysis/overview.zul', '/platform/models/bias-analysis/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis de Sesgo' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Realizar Análisis de Sesgo', 'platform/models/bias-analysis/page.zul', '/platform/models/bias-analysis/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis de Sesgo' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

-- =====================================================================
-- MENU 8: MODELOS GOBIERNO (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Overview de Gobierno', 'gobierno/models/models-overview.zul', '/gobierno/models/models-overview', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Modelos Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Gobierno', 'gobierno/models/models-detail.zul', '/gobierno/models/models-detail', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Modelos Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS MODELS
-- =====================================================================

