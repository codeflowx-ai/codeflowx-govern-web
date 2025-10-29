-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - MENU ITEMS AGENTS
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de Menu Items para el módulo AGENTS
-- 
-- Estructura SSOMENUITEM:
--   - ITEM: Nombre descriptivo del menu item
--   - PAGE: Ruta al archivo ZUL (ej: platform/agents/overview/page.zul)
--   - URL: Ruta de navegación (ej: /platform/agents/overview/page)
--   - DASHBOARD: Boolean (false en general, true solo para dashboards principales)
--   - ICONO: Ícono Font Awesome (opcional)
--   - NAMESPACE: platform o gobierno
--   - IDSSOMENU0: FK al menú padre (mediante subconsulta)
-- 
-- Total: 52 menu items para AGENTS
-- =====================================================================

-- =====================================================================
-- MENU 1: GESTIÓN DE AGENTES (4 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Agentes', 'platform/agents/overview/page.zul', '/platform/agents/overview/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Agentes' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Crear Agente', 'platform/agents/create/page.zul', '/platform/agents/create/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Agentes' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Dominios', 'platform/agents/registry/domain-overview.zul', '/platform/agents/registry/domain-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Agentes' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Dominio', 'platform/agents/registry/domain.zul', '/platform/agents/registry/domain', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Agentes' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 2: OPERACIONES (7 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Despliegues', 'platform/agents/deployment/overview.zul', '/platform/agents/deployment/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Operaciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Estado de Despliegues', 'platform/agents/deployment/status.zul', '/platform/agents/deployment/status', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Operaciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Nuevo Despliegue', 'platform/agents/deployment/page.zul', '/platform/agents/deployment/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Operaciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Versiones', 'platform/agents/versioning/overview.zul', '/platform/agents/versioning/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Operaciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Versión', 'platform/agents/versioning/page.zul', '/platform/agents/versioning/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Operaciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Rollbacks', 'platform/agents/rollback/overview.zul', '/platform/agents/rollback/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Operaciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Ejecutar Rollback', 'platform/agents/rollback/page.zul', '/platform/agents/rollback/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Operaciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 3: MONITOREO Y PERFORMANCE (7 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Dashboard Principal', 'platform/agents/monitoring/dashboard.zul', '/platform/agents/monitoring/dashboard', true, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Overview General', 'platform/agents/monitoring/overview.zul', '/platform/agents/monitoring/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Salud', 'platform/agents/monitoring/health-overview.zul', '/platform/agents/monitoring/health-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Salud', 'platform/agents/monitoring/health.zul', '/platform/agents/monitoring/health', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Consumo de Recursos', 'platform/agents/monitoring/resources.zul', '/platform/agents/monitoring/resources', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Análisis de Errores', 'platform/agents/monitoring/errors.zul', '/platform/agents/monitoring/errors', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Monitoreo', 'platform/agents/monitoring/page.zul', '/platform/agents/monitoring/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Monitoreo y Performance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 4: ALERTAS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Alertas', 'platform/agents/alerts/overview.zul', '/platform/agents/alerts/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Alertas' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Alerta', 'platform/agents/alerts/page.zul', '/platform/agents/alerts/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Alertas' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 5: INTERACCIONES (8 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Interacciones', 'platform/agents/interactions/overview.zul', '/platform/agents/interactions/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Patrones de Interacción', 'platform/agents/interactions/patterns.zul', '/platform/agents/interactions/patterns', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Interacción', 'platform/agents/interactions/page.zul', '/platform/agents/interactions/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Comunicación', 'platform/agents/interactions/communication-overview.zul', '/platform/agents/interactions/communication-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Comunicación', 'platform/agents/interactions/communication.zul', '/platform/agents/interactions/communication', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Colaboración', 'platform/agents/interactions/collaboration-overview.zul', '/platform/agents/interactions/collaboration-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Colaboración', 'platform/agents/interactions/collaboration.zul', '/platform/agents/interactions/collaboration', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Red de Colaboración', 'platform/agents/interactions/network.zul', '/platform/agents/interactions/network', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Interacciones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 6: APRENDIZAJE Y EXPERTISE (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Expertise', 'platform/agents/learning/overview.zul', '/platform/agents/learning/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Aprendizaje y Expertise' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Gestionar Expertise', 'platform/agents/learning/page.zul', '/platform/agents/learning/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Aprendizaje y Expertise' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 7: WORKFLOWS (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Workflows', 'platform/agents/workflow/overview.zul', '/platform/agents/workflow/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Workflows' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Crear/Editar Workflow', 'platform/agents/workflow/page.zul', '/platform/agents/workflow/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Workflows' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Ejecuciones', 'platform/agents/workflow/execution-overview.zul', '/platform/agents/workflow/execution-overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Workflows' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Detalle de Ejecución', 'platform/agents/workflow/execution.zul', '/platform/agents/workflow/execution', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Workflows' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 8: ÉTICA Y GOBERNANZA (8 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Evaluaciones Éticas', 'platform/agents/ethics/overview.zul', '/platform/agents/ethics/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Nueva Evaluación Ética', 'platform/agents/ethics/page.zul', '/platform/agents/ethics/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado Detección de Sesgo', 'platform/agents/bias-detection/overview.zul', '/platform/agents/bias-detection/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Analizar Sesgo', 'platform/agents/bias-detection/page.zul', '/platform/agents/bias-detection/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Transparencia', 'platform/agents/transparency/overview.zul', '/platform/agents/transparency/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Transparencia', 'platform/agents/transparency/page.zul', '/platform/agents/transparency/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Gobernanza', 'platform/agents/governance/overview.zul', '/platform/agents/governance/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Gobernanza', 'platform/agents/governance/page.zul', '/platform/agents/governance/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Ética y Gobernanza' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 9: APROBACIONES Y DECISIONES (5 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Aprobaciones', 'platform/agents/approval/overview.zul', '/platform/agents/approval/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Aprobaciones y Decisiones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Procesar Aprobación', 'platform/agents/approval/page.zul', '/platform/agents/approval/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Aprobaciones y Decisiones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Decisiones', 'platform/agents/decisions/overview.zul', '/platform/agents/decisions/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Aprobaciones y Decisiones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Nueva/Editar Decisión', 'platform/agents/decisions/page.zul', '/platform/agents/decisions/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Aprobaciones y Decisiones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Auditoría de Decisiones', 'platform/agents/decisions/audit.zul', '/platform/agents/decisions/audit', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Aprobaciones y Decisiones' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 10: COMPLIANCE (3 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Estado de Compliance', 'platform/agents/compliance/status.zul', '/platform/agents/compliance/status', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Compliance', 'platform/agents/compliance/overview.zul', '/platform/agents/compliance/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Configurar Compliance', 'platform/agents/compliance/page.zul', '/platform/agents/compliance/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Compliance' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

-- =====================================================================
-- MENU 11: HERRAMIENTAS (2 items)
-- =====================================================================

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Listado de Tools', 'platform/agents/tools/overview.zul', '/platform/agents/tools/overview', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Herramientas' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

INSERT INTO SSOMENUITEM (ITEM, PAGE, URL, DASHBOARD, NAMESPACE, IDSSOMENU0)
VALUES ('Nueva Tool', 'platform/agents/tools/page.zul', '/platform/agents/tools/page', false, 'platform', 
  (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Herramientas' AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - MENU ITEMS AGENTS
-- =====================================================================
-- Total: 52 menu items insertados para el módulo AGENTS
-- =====================================================================
