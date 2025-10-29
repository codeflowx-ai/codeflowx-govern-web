-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS SERVING
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo SERVING
-- Total: 26 menu items para SERVING
-- =====================================================================

-- =====================================================================
-- MENU 1: GESTIÓN DE DESPLIEGUES (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Despliegues', 'platform/serving/model-deployment-overview.zul', '/platform/serving/model-deployment-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Despliegues' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Despliegue', 'platform/serving/model-deployment-detail.zul', '/platform/serving/model-deployment-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Despliegues' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Estado de Despliegues', 'platform/serving/deployment-status-overview.zul', '/platform/serving/deployment-status-overview', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Despliegues' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Instancias', 'platform/serving/deployment-instance-overview.zul', '/platform/serving/deployment-instance-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Despliegues' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Instancia', 'platform/serving/deployment-instance-detail.zul', '/platform/serving/deployment-instance-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Despliegues' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

-- =====================================================================
-- MENU 2: MODELOS EN PRODUCCIÓN (4 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Modelos', 'platform/serving/model-overview.zul', '/platform/serving/model-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Modelos en Producción' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Modelo', 'platform/serving/model-detail.zul', '/platform/serving/model-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Modelos en Producción' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Versiones', 'platform/serving/model-version-overview.zul', '/platform/serving/model-version-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Modelos en Producción' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Versión', 'platform/serving/model-version-detail.zul', '/platform/serving/model-version-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Modelos en Producción' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

-- =====================================================================
-- MENU 3: ENDPOINTS DE SERVING (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Endpoints', 'platform/serving/serving-endpoint-overview.zul', '/platform/serving/serving-endpoint-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Endpoints de Serving' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Endpoint', 'platform/serving/serving-endpoint-detail.zul', '/platform/serving/serving-endpoint-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Endpoints de Serving' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

-- =====================================================================
-- MENU 4: MÉTRICAS Y PERFORMANCE (4 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Métricas', 'platform/serving/model-metrics-overview.zul', '/platform/serving/model-metrics-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Métricas y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Métricas', 'platform/serving/model-metrics-detail.zul', '/platform/serving/model-metrics-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Métricas y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Métricas Despliegue', 'platform/serving/deployment-metric-overview.zul', '/platform/serving/deployment-metric-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Métricas y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle Métrica Despliegue', 'platform/serving/deployment-metric-detail.zul', '/platform/serving/deployment-metric-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Métricas y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

-- =====================================================================
-- MENU 5: PREDICCIONES (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Predicciones', 'platform/serving/model-prediction-overview.zul', '/platform/serving/model-prediction-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Predicciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Predicción', 'platform/serving/model-prediction-detail.zul', '/platform/serving/model-prediction-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Predicciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

-- =====================================================================
-- MENU 6: REQUESTS DE SERVING (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Requests', 'platform/serving/serving-request-overview.zul', '/platform/serving/serving-request-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Requests de Serving' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Request', 'platform/serving/serving-request-detail.zul', '/platform/serving/serving-request-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Requests de Serving' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

-- =====================================================================
-- MENU 7: LOGS DE DESPLIEGUE (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Logs', 'platform/serving/deployment-log-overview.zul', '/platform/serving/deployment-log-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Logs de Despliegue' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Log', 'platform/serving/deployment-log-detail.zul', '/platform/serving/deployment-log-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Logs de Despliegue' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

-- =====================================================================
-- MENU 8: ANÁLISIS Y COMPLIANCE (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis de Errores', 'platform/serving/error-analysis-overview.zul', '/platform/serving/error-analysis-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis y Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis Detallado Errores', 'platform/serving/serving-error-analysis-overview.zul', '/platform/serving/serving-error-analysis-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis y Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('SLA Compliance', 'platform/serving/sla-compliance-overview.zul', '/platform/serving/sla-compliance-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis y Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('SLA Compliance Detallado', 'platform/serving/serving-sla-compliance-overview.zul', '/platform/serving/serving-sla-compliance-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis y Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Desglose de Costos', 'platform/serving/serving-cost-breakdown-overview.zul', '/platform/serving/serving-cost-breakdown-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis y Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS SERVING
-- =====================================================================

