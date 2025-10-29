-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS PROJECTS
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo PROJECTS
-- Total: 44 menu items para PROJECTS
-- =====================================================================

-- =====================================================================
-- MENU 1: GESTIÓN DE PROYECTOS (4 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Proyectos', 'platform/projects/project-overview.zul', '/platform/projects/project-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Proyectos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Proyecto', 'platform/projects/project-detail.zul', '/platform/projects/project-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Proyectos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Dominios', 'platform/projects/project-domain-overview.zul', '/platform/projects/project-domain-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Proyectos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Dominio', 'platform/projects/project-domain-detail.zul', '/platform/projects/project-domain-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Proyectos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 2: EQUIPO Y RECURSOS (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Miembros', 'platform/projects/project-member-overview.zul', '/platform/projects/project-member-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Equipo y Recursos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Miembro', 'platform/projects/project-member-detail.zul', '/platform/projects/project-member-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Equipo y Recursos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Asignación de Recursos', 'platform/projects/project-resource-allocation-overview.zul', '/platform/projects/project-resource-allocation-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Equipo y Recursos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Consumo', 'platform/projects/project-resource-consumption-overview.zul', '/platform/projects/project-resource-consumption-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Equipo y Recursos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Consumo', 'platform/projects/project-resource-consumption-detail.zul', '/platform/projects/project-resource-consumption-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Equipo y Recursos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 3: TAREAS Y SEGUIMIENTO (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Tareas', 'platform/projects/project-task-overview.zul', '/platform/projects/project-task-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Tareas y Seguimiento' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Tarea', 'platform/projects/project-task-detail.zul', '/platform/projects/project-task-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Tareas y Seguimiento' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Time Tracking', 'platform/projects/project-time-tracking-overview.zul', '/platform/projects/project-time-tracking-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Tareas y Seguimiento' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Registrar Tiempo', 'platform/projects/project-time-tracking-detail.zul', '/platform/projects/project-time-tracking-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Tareas y Seguimiento' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Timeline Gantt', 'platform/projects/project-timeline-gantt-overview.zul', '/platform/projects/project-timeline-gantt-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Tareas y Seguimiento' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 4: STACK TECNOLÓGICO (4 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Stacks', 'platform/projects/project-stack-overview.zul', '/platform/projects/project-stack-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Stack Tecnológico' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Stack', 'platform/projects/project-stack-detail.zul', '/platform/projects/project-stack-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Stack Tecnológico' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Tecnologías', 'platform/projects/project-technology-overview.zul', '/platform/projects/project-technology-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Stack Tecnológico' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Tecnología', 'platform/projects/project-technology-detail.zul', '/platform/projects/project-technology-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Stack Tecnológico' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 5: DOCUMENTACIÓN Y ARTEFACTOS (4 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Documentos', 'platform/projects/project-document-overview.zul', '/platform/projects/project-document-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Documentación y Artefactos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Documento', 'platform/projects/project-document-detail.zul', '/platform/projects/project-document-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Documentación y Artefactos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Artefactos', 'platform/projects/project-artifact-overview.zul', '/platform/projects/project-artifact-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Documentación y Artefactos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Artefacto', 'platform/projects/project-artifact-detail.zul', '/platform/projects/project-artifact-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Documentación y Artefactos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 6: REQUERIMIENTOS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Requerimientos', 'platform/projects/project-requirement-overview.zul', '/platform/projects/project-requirement-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Requerimientos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Requerimiento', 'platform/projects/project-requirement-detail.zul', '/platform/projects/project-requirement-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Requerimientos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 7: FACTURACIÓN Y BILLING (6 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Estado de Facturación', 'platform/projects/project-billing-status-overview.zul', '/platform/projects/project-billing-status-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Facturación y Billing' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Billing Details', 'platform/projects/project-billing-detail-overview.zul', '/platform/projects/project-billing-detail-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Facturación y Billing' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Billing Detail', 'platform/projects/project-billing-detail-detail.zul', '/platform/projects/project-billing-detail-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Facturación y Billing' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Invoices', 'platform/projects/project-invoice-overview.zul', '/platform/projects/project-invoice-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Facturación y Billing' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Invoice', 'platform/projects/project-invoice-detail.zul', '/platform/projects/project-invoice-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Facturación y Billing' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Aging Report', 'platform/projects/invoice-aging-report-overview.zul', '/platform/projects/invoice-aging-report-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Facturación y Billing' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 8: ANÁLISIS FINANCIERO (8 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Resumen Financiero', 'platform/projects/project-financial-summary-overview.zul', '/platform/projects/project-financial-summary-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Desglose de Costos', 'platform/projects/project-cost-breakdown-overview.zul', '/platform/projects/project-cost-breakdown-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Estimaciones', 'platform/projects/project-cost-estimator-overview.zul', '/platform/projects/project-cost-estimator-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Crear Estimación', 'platform/projects/project-cost-estimator-detail.zul', '/platform/projects/project-cost-estimator-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de ROI', 'platform/projects/project-roi-overview.zul', '/platform/projects/project-roi-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar ROI', 'platform/projects/project-roi-detail.zul', '/platform/projects/project-roi-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis de ROI', 'platform/projects/project-roi-analysis-overview.zul', '/platform/projects/project-roi-analysis-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis de Rentabilidad', 'platform/projects/client-profitability-analysis-overview.zul', '/platform/projects/client-profitability-analysis-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Análisis Financiero' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 9: DASHBOARDS Y ANÁLISIS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Portfolio Dashboard', 'platform/projects/project-portfolio-dashboard-overview.zul', '/platform/projects/project-portfolio-dashboard-overview', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Dashboards y Análisis' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Evaluación de Riesgos', 'platform/projects/project-risk-assessment-overview.zul', '/platform/projects/project-risk-assessment-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Dashboards y Análisis' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 10: LICENCIAS Y TOKENS (4 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Licencias', 'platform/projects/project-license-overview.zul', '/platform/projects/project-license-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Licencias y Tokens' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Licencia', 'platform/projects/project-license-detail.zul', '/platform/projects/project-license-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Licencias y Tokens' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Tokens', 'platform/projects/project-token-overview.zul', '/platform/projects/project-token-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Licencias y Tokens' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Token', 'platform/projects/project-token-detail.zul', '/platform/projects/project-token-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Licencias y Tokens' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

-- =====================================================================
-- MENU 11: VERSIONADO DE PROYECTOS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Versiones', 'platform/projects/project-version-overview.zul', '/platform/projects/project-version-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de Proyectos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Versión', 'platform/projects/project-version-detail.zul', '/platform/projects/project-version-detail', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Versionado de Proyectos' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS PROJECTS
-- =====================================================================
-- FIN DE TODOS LOS SCRIPTS SSO
-- Total scripts generados: 10
-- Total menu items insertados: 265
-- =====================================================================

