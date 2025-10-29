-- =====================================================================
-- SCRIPT DE POBLACIÓN SSO - APPLICATIONS Y MENUS
-- =====================================================================
-- Fecha: Octubre 29, 2025
-- Versión: 1.0
-- Descripción: Población de SSO Applications y Menus
-- 
-- Notas importantes:
-- - Todos los campos DASHBOARD/DASHBOARDPAGE/PAGE comienzan con 'platform/' o 'gobierno/' (sin barra inicial)
-- - Los IDs son SERIAL (autonuméricos), no se incluyen en INSERT
-- - Las referencias FK usan subconsultas SELECT
-- 
-- Total: 9 Applications | 74 Menus
-- =====================================================================

-- =====================================================================
-- PASO 1: INSERTAR SSO APPLICATIONS (9)
-- =====================================================================

-- 1. AGENTS
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Agents', 'platform/agents/monitoring/dashboard.zul', 'fa-robot', 'platform', 'Gestión de Agentes de IA');

-- 2. MODELS
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Models', 'platform/models/overview/page.zul', 'fa-brain', 'platform', 'Gestión de Modelos');

-- 3. PROMPTS
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Prompts', 'platform/prompts/overview/page.zul', 'fa-comments', 'platform', 'Gestión de Prompts');

-- 4. RAG
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('RAG', 'platform/rag/overview/page.zul', 'fa-search', 'platform', 'Gestión de RAG');

-- 5. PROVIDERS
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Providers', 'platform/providers/providers-overview-overview.zul', 'fa-plug', 'platform', 'Gestión de Providers');

-- 6. CORE
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Core', 'gobierno/core/admin-dashboard.zul', 'fa-cog', 'gobierno', 'Administración del Sistema');

-- 7. GOVERNANCE
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Governance', 'platform/governance/dashboard/overview.zul', 'fa-landmark', 'platform', 'Gobernanza de IA');

-- 8. SERVING
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Serving', 'platform/serving/deployment-status-overview.zul', 'fa-rocket', 'platform', 'Serving de Modelos');

-- 9. PROJECTS
INSERT INTO SSOAPLICACION (APLICACION, DASHBOARD, ICONCLASS, NAMESPACE, TITULO)
VALUES ('Projects', 'platform/projects/project-portfolio-dashboard-overview.zul', 'fa-folder-open', 'platform', 'Gestión de Proyectos');

COMMIT;

-- =====================================================================
-- PASO 2: INSERTAR SSO MENUS (74)
-- =====================================================================

-- ===================================================================
-- APPLICATION 1: AGENTS (11 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Agentes', 'platform/agents/overview/page.zul', 'fa-list-check', 'Gestión CRUD de agentes y sus dominios', 'Gestión de Agentes', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Operaciones', 'platform/agents/deployment/overview.zul', 'fa-gears', 'Despliegues, versionado y rollback de agentes', 'Operaciones', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Monitoreo y Performance', 'platform/agents/monitoring/dashboard.zul', 'fa-chart-line', 'Monitoreo en tiempo real de agentes', 'Monitoreo', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Alertas', 'platform/agents/alerts/overview.zul', 'fa-bell', 'Gestión de alertas de agentes', 'Alertas', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Interacciones', 'platform/agents/interactions/overview.zul', 'fa-handshake', 'Interacciones, comunicación y colaboración entre agentes', 'Interacciones', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Aprendizaje y Expertise', 'platform/agents/learning/overview.zul', 'fa-graduation-cap', 'Gestión del conocimiento y expertise de agentes', 'Aprendizaje', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Workflows', 'platform/agents/workflow/overview.zul', 'fa-diagram-project', 'Workflows y ejecuciones de agentes', 'Workflows', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Ética y Gobernanza', 'platform/agents/ethics/overview.zul', 'fa-balance-scale', 'Evaluaciones éticas, detección de sesgo y transparencia', 'Ética', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Aprobaciones y Decisiones', 'platform/agents/approval/overview.zul', 'fa-check-circle', 'Aprobaciones y decisiones de agentes', 'Aprobaciones', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Compliance', 'platform/agents/compliance/status.zul', 'fa-shield-check', 'Estado de cumplimiento de agentes', 'Compliance', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Herramientas', 'platform/agents/tools/overview.zul', 'fa-tools', 'Herramientas disponibles para agentes', 'Herramientas', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'));

-- ===================================================================
-- APPLICATION 2: MODELS (8 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Modelos', 'platform/models/overview/page.zul', 'fa-list-check', 'Gestión CRUD de modelos de IA', 'Gestión de Modelos', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Registro de Modelos', 'platform/models/registry/catalog-overview.zul', 'fa-book', 'Catálogo y registro de modelos, providers, endpoints, capabilities y artifacts', 'Registry', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Performance de Modelos', 'platform/models/performance/overview.zul', 'fa-chart-line', 'Monitoreo de performance, métricas y uso de modelos', 'Performance', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Versionado de Modelos', 'platform/models/versioning/overview.zul', 'fa-code-branch', 'Gestión de versiones de modelos', 'Versionado', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Dependencias', 'platform/models/dependencies/overview.zul', 'fa-link', 'Gestión de dependencias entre modelos', 'Dependencias', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Explicabilidad (XAI)', 'platform/models/explainability/overview.zul', 'fa-lightbulb', 'Análisis de explicabilidad e interpretabilidad de modelos', 'Explicabilidad', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Análisis de Sesgo', 'platform/models/bias-analysis/overview.zul', 'fa-balance-scale', 'Detección y análisis de sesgos en modelos', 'Sesgo', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Modelos Gobierno', 'gobierno/models/models-overview.zul', 'fa-landmark', 'Vista de gobierno corporativo de modelos', 'Gobierno', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Models'));

-- ===================================================================
-- APPLICATION 3: PROMPTS (6 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Prompts', 'platform/prompts/overview/page.zul', 'fa-list-check', 'Gestión CRUD de prompts', 'Gestión de Prompts', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Registro de Prompts', NULL, 'fa-book', 'Registro y catálogo de prompts', 'Registry', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Templates de Prompts', NULL, 'fa-file-alt', 'Plantillas predefinidas de prompts con métricas y análisis', 'Templates', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Validación de Prompts', 'platform/prompts/validation/overview.zul', 'fa-check-circle', 'Validación y testing de prompts', 'Validación', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Versionado de Prompts', 'platform/prompts/versioning/overview.zul', 'fa-code-branch', 'Gestión de versiones e histórico de prompts', 'Versionado', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Prompts Gobierno', 'gobierno/prompts/prompts-overview.zul', 'fa-landmark', 'Vista de gobierno corporativo de prompts', 'Gobierno', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Prompts'));

-- ===================================================================
-- APPLICATION 4: RAG (7 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de RAG', 'platform/rag/overview/page.zul', 'fa-list-check', 'Gestión general de sistemas RAG', 'Gestión de RAG', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Fuentes de Datos', 'platform/rag/data-sources/overview.zul', 'fa-folder-open', 'Gestión de fuentes de datos para RAG', 'Data Sources', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Registro de RAG', NULL, 'fa-book', 'Registro y catálogo de sistemas RAG', 'Registry', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Control de Calidad', NULL, 'fa-check-double', 'Control de calidad y evaluación de retrieval', 'Quality Control', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Monitoreo de RAG', 'platform/rag/monitoring/health-dashboard.zul', 'fa-chart-line', 'Monitoreo de salud, métricas y uso de sistemas RAG', 'Monitoreo', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Versionado de RAG', 'platform/rag/versioning/overview.zul', 'fa-code-branch', 'Gestión de versiones de sistemas RAG', 'Versionado', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('RAG Gobierno', 'gobierno/rag/rag-systems-overview.zul', 'fa-landmark', 'Vista de gobierno corporativo de sistemas RAG', 'Gobierno', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'RAG'));

-- ===================================================================
-- APPLICATION 5: PROVIDERS (2 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Providers', 'platform/providers/providers-overview-overview.zul', 'fa-list-check', 'Gestión de proveedores de IA y sus métricas', 'Gestión de Providers', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Providers'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Providers Gobierno', 'gobierno/providers/providers-overview.zul', 'fa-landmark', 'Vista de gobierno corporativo de providers', 'Gobierno', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Providers'));

-- ===================================================================
-- APPLICATION 6: CORE (9 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Administración', 'gobierno/core/admin-dashboard.zul', 'fa-user-shield', 'Dashboard principal de administración del sistema', 'Administración', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Usuarios', 'platform/core/user-overview.zul', 'fa-users', 'Gestión CRUD de usuarios', 'Usuarios', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Roles y Permisos', 'platform/core/role-overview.zul', 'fa-shield-alt', 'Gestión de roles y permisos del sistema', 'Roles y Permisos', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Departamentos', 'platform/core/department-overview.zul', 'fa-building', 'Gestión de departamentos organizacionales', 'Departamentos', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Menús', 'platform/core/menu-overview.zul', 'fa-bars', 'Gestión de menús del sistema', 'Menús', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Seguridad', 'gobierno/core/security-audit.zul', 'fa-lock', 'Auditoría y monitoreo de seguridad', 'Seguridad', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Sesiones de Usuario', 'platform/core/user-session-overview.zul', 'fa-key', 'Monitoreo y gestión de sesiones activas', 'Sesiones', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Actividad de Usuarios', 'gobierno/core/user-activity.zul', 'fa-chart-bar', 'Monitoreo de actividad y acciones de usuarios', 'Actividad', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Salud del Sistema', 'gobierno/core/system-health.zul', 'fa-heartbeat', 'Monitoreo de salud y estado del sistema', 'Salud', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Core'));

-- ===================================================================
-- APPLICATION 7: GOVERNANCE (12 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Dashboard de Gobernanza', 'platform/governance/dashboard/overview.zul', 'fa-tachometer-alt', 'Dashboard principal de gobernanza', 'Dashboard', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Políticas de Gobernanza', 'platform/governance/policies/overview.zul', 'fa-scroll', 'Gestión de políticas, reglas, evaluaciones y violaciones', 'Políticas', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Compliance', 'platform/governance/compliance/page.zul', 'fa-clipboard-check', 'Gestión de compliance, requerimientos, hallazgos y frameworks regulatorios', 'Compliance', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Auditoría', 'platform/governance/audit/log-overview.zul', 'fa-clipboard-list', 'Logs de auditoría y audit trail', 'Auditoría', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Métricas de Gobernanza', 'platform/governance/metrics/overview.zul', 'fa-chart-line', 'Métricas y KPIs de gobernanza', 'Métricas', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('KPIs Ejecutivos', 'platform/governance/kpis/executive.zul', 'fa-bullseye', 'KPIs para nivel ejecutivo', 'KPIs', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Analytics de Gobernanza', 'platform/governance/analytics/evaluation-trends.zul', 'fa-chart-area', 'Analytics, tendencias y auto-aprobación', 'Analytics', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Control de Calidad', NULL, 'fa-check-double', 'Revisiones éticas y de datasets', 'Calidad', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Riesgos', 'platform/governance/risks/matrix.zul', 'fa-exclamation-triangle', 'Matriz de riesgos', 'Riesgos', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Seguridad de Gobernanza', 'platform/governance/security/policy-overview.zul', 'fa-shield-alt', 'Políticas de seguridad, amenazas y métricas', 'Seguridad', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Reportes de Gobernanza', NULL, 'fa-file-alt', 'Reportes de efectividad', 'Reportes', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Governance Gobierno', 'gobierno/governance/governance-overview.zul', 'fa-landmark', 'Vista de gobierno corporativo de governance', 'Gobierno', 'gobierno', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Governance'));

-- ===================================================================
-- APPLICATION 8: SERVING (8 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Despliegues', 'platform/serving/model-deployment-overview.zul', 'fa-box', 'Gestión de despliegues de modelos', 'Despliegues', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Modelos en Producción', 'platform/serving/model-overview.zul', 'fa-robot', 'Gestión de modelos servidos', 'Modelos', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Endpoints de Serving', 'platform/serving/serving-endpoint-overview.zul', 'fa-plug', 'Gestión de endpoints de serving', 'Endpoints', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Métricas y Performance', 'platform/serving/model-metrics-overview.zul', 'fa-chart-line', 'Métricas de modelos en producción', 'Métricas', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Predicciones', 'platform/serving/model-prediction-overview.zul', 'fa-bullseye', 'Gestión de predicciones de modelos', 'Predicciones', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Requests de Serving', 'platform/serving/serving-request-overview.zul', 'fa-envelope', 'Gestión de requests a endpoints', 'Requests', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Logs de Despliegue', 'platform/serving/deployment-log-overview.zul', 'fa-file-alt', 'Logs de despliegues', 'Logs', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Análisis y Compliance', 'platform/serving/error-analysis-overview.zul', 'fa-chart-bar', 'Análisis de errores y compliance SLA', 'Análisis', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Serving'));

-- ===================================================================
-- APPLICATION 9: PROJECTS (11 menus)
-- ===================================================================

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Gestión de Proyectos', 'platform/projects/project-overview.zul', 'fa-list-check', 'CRUD de proyectos y dominios', 'Proyectos', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Equipo y Recursos', 'platform/projects/project-member-overview.zul', 'fa-users', 'Gestión de miembros y asignación de recursos', 'Equipo', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Tareas y Seguimiento', 'platform/projects/project-task-overview.zul', 'fa-tasks', 'Gestión de tareas y tracking de tiempo', 'Tareas', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Stack Tecnológico', 'platform/projects/project-stack-overview.zul', 'fa-tools', 'Gestión de stack y tecnologías', 'Stack', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Documentación y Artefactos', 'platform/projects/project-document-overview.zul', 'fa-file-alt', 'Gestión de documentos y artefactos', 'Documentación', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Requerimientos', 'platform/projects/project-requirement-overview.zul', 'fa-clipboard-list', 'Gestión de requerimientos del proyecto', 'Requerimientos', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Facturación y Billing', 'platform/projects/project-billing-status-overview.zul', 'fa-dollar-sign', 'Gestión de facturación e invoices', 'Facturación', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Análisis Financiero', 'platform/projects/project-financial-summary-overview.zul', 'fa-chart-pie', 'Análisis financiero y ROI', 'Financiero', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Dashboards y Análisis', 'platform/projects/project-portfolio-dashboard-overview.zul', 'fa-chart-line', 'Dashboards y análisis de riesgos', 'Dashboards', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Licencias y Tokens', 'platform/projects/project-license-overview.zul', 'fa-key', 'Gestión de licencias y tokens', 'Licencias', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

INSERT INTO SSOMENU (MENU, DASHBOARDPAGE, ICONO, DESCRIPCION, TITLE, NAMESPACE, IDSSOAPLICACION0)
VALUES ('Versionado de Proyectos', 'platform/projects/project-version-overview.zul', 'fa-code-branch', 'Gestión de versiones de proyecto', 'Versiones', 'platform', (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Projects'));

COMMIT;

-- =====================================================================
-- FIN DEL SCRIPT - APPLICATIONS Y MENUS
-- =====================================================================
-- Los MENU ITEMS (265 items) se generarán en scripts complementarios
-- separados por módulo para facilitar mantenimiento
-- =====================================================================


