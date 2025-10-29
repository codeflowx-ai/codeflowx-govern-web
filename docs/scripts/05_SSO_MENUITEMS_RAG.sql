-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS RAG
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo RAG
-- Total: 17 menu items para RAG
-- =====================================================================

-- =====================================================================
-- MENU 1: GESTIÓN DE RAG (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard de RAG', 'platform/rag/overview/page.zul', '/platform/rag/overview/page', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de RAG', 'platform/rag/overview/summary.zul', '/platform/rag/overview/summary', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

-- =====================================================================
-- MENU 2: FUENTES DE DATOS (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Data Sources', 'platform/rag/data-sources/overview.zul', '/platform/rag/data-sources/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Fuentes de Datos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Data Source', 'platform/rag/data-sources/page.zul', '/platform/rag/data-sources/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Fuentes de Datos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Progreso de Embeddings', 'platform/rag/data-sources/embedding-progress.zul', '/platform/rag/data-sources/embedding-progress', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Fuentes de Datos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis de Cobertura', 'platform/rag/data-sources/coverage-analysis.zul', '/platform/rag/data-sources/coverage-analysis', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Fuentes de Datos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Estadísticas', 'platform/rag/data-sources/statistics.zul', '/platform/rag/data-sources/statistics', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Fuentes de Datos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

-- =====================================================================
-- MENU 3: REGISTRO DE RAG (1 item)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Registry', 'platform/rag/registry/page.zul', '/platform/rag/registry/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Registro de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

-- =====================================================================
-- MENU 4: CONTROL DE CALIDAD (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Calidad de Retrieval', 'platform/rag/quality-control/retrieval-quality.zul', '/platform/rag/quality-control/retrieval-quality', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Control de Calidad' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Distribución de Chunks', 'platform/rag/quality-control/chunk-distribution.zul', '/platform/rag/quality-control/chunk-distribution', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Control de Calidad' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

-- =====================================================================
-- MENU 5: MONITOREO DE RAG (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard de Salud', 'platform/rag/monitoring/health-dashboard.zul', '/platform/rag/monitoring/health-dashboard', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen de Métricas', 'platform/rag/monitoring/metrics-summary.zul', '/platform/rag/monitoring/metrics-summary', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Uso por Agente', 'platform/rag/monitoring/usage-by-agent.zul', '/platform/rag/monitoring/usage-by-agent', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

-- =====================================================================
-- MENU 6: VERSIONADO DE RAG (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Versiones', 'platform/rag/versioning/overview.zul', '/platform/rag/versioning/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Versión', 'platform/rag/versioning/page.zul', '/platform/rag/versioning/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de RAG' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

-- =====================================================================
-- MENU 7: RAG GOBIERNO (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Overview de Gobierno', 'gobierno/rag/rag-systems-overview.zul', '/gobierno/rag/rag-systems-overview', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'RAG Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Gobierno', 'gobierno/rag/rag-systems-detail.zul', '/gobierno/rag/rag-systems-detail', false, 'gobierno', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'RAG Gobierno' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS RAG
-- =====================================================================

