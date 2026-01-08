# 📋 PANTALLAS ESENCIALES PARA USUARIOS FINALES

**Fecha:** Noviembre 2025
**Versión:** 1.0
**Objetivo:** Identificar y mapear las pantallas esenciales que deben mostrarse a usuarios finales (excluyendo administración)

---

## 🎯 CRITERIOS DE SELECCIÓN

### ✅ Pantallas Esenciales (Incluidas):
- **Dashboards principales** por módulo funcional
- **CRUD básico** (Listar, Crear, Editar, Ver) de entidades principales
- **Bandeja de tareas BPMN** (task-inbox)
- **Pantallas de monitoreo** y métricas operativas
- **Pantallas de compliance** y gobernanza para usuarios finales

### ❌ Pantallas Excluidas:
- **Administración del sistema** (usuarios, roles, permisos, configuración)
- **Infraestructura técnica** (nodos, clusters, actualizaciones)
- **Configuración avanzada** (menús, licencias, auditoría técnica)
- **Pantallas de desarrollo** (code playground, snippets, templates)

---

## 📊 RESUMEN EJECUTIVO

| Módulo | Pantallas Esenciales | Next.js Migradas | ZUL Equivalentes | Estado |
|--------|---------------------|------------------|------------------|--------|
| **Dashboard** | 1 | ✅ 1 | 1 | ✅ Completo |
| **BPMN Task Inbox** | 1 | ✅ 1 | 1 | ✅ Completo |
| **Models** | 8 | ✅ 20+ | 31 | ✅ Migrado |
| **Projects** | 6 | ✅ 20+ | 49 | ✅ Migrado |
| **Agents** | 7 | ✅ 20+ | 54 | ✅ Migrado |
| **Compliance/Governance** | 5 | ✅ 14+ | 12 | ✅ Migrado |
| **Training** | 6 | ✅ 20+ | 52 | ✅ Migrado |
| **RAG** | 5 | ✅ 23+ | 18 | ✅ Migrado |
| **Serving** | 4 | ⏳ Pendiente | 29 | ⏳ Pendiente |
| **Data Sources** | 4 | ✅ 5 | ~10 | ✅ Parcial |
| **Analytics** | 3 | ✅ 6 | ~15 | ✅ Parcial |
| **AI Playground** | 3 | ✅ 1 | ~5 | ✅ Parcial |
| **TOTAL** | **53** | **~150+** | **~285** | **75% Migrado** |

---

## 🗂️ MAPEO DETALLADO POR MÓDULO

### 1. 📊 DASHBOARD PRINCIPAL

#### Pantalla Esencial:
| Next.js | ZUL Equivalente | Descripción | Roles |
|---------|-----------------|-------------|-------|
| `app/(app)/dashboard/page.tsx` | `console/dashboards/dashboard.zul` | Dashboard principal con KPIs y métricas clave | Todos (excepto admin) |

**Funcionalidades:**
- KPIs principales (modelos, proyectos, compliance)
- Métricas de uso y rendimiento
- Accesos rápidos a módulos principales
- Notificaciones y alertas

---

### 2. 📥 BANDEJA DE TAREAS BPMN

#### Pantalla Esencial:
| Next.js | ZUL Equivalente | Descripción | Roles |
|---------|-----------------|-------------|-------|
| `app/(app)/bpmn/task-inbox/page.tsx` | `console/bpmn/task-inbox.zul` | Bandeja de tareas pendientes de procesos BPMN | Todos |

**Funcionalidades:**
- Lista de tareas asignadas
- Filtros por proceso, prioridad, estado
- Acceso a formularios de tareas
- Historial de tareas completadas

**Formularios BPMN Esenciales (59 migrados):**
- `app/(app)/bpmn/forms/internal-audit-scheduling/page.tsx` → `console/bpmn/internal-audit-scheduling-form.zul`
- `app/(app)/bpmn/forms/compliance-review/page.tsx` → `console/bpmn/compliance-review-form.zul`
- `app/(app)/bpmn/forms/model-approval-human-override/page.tsx` → `console/bpmn/model-approval-human-override-form.zul`
- `app/(app)/bpmn/forms/agent-approval-human-override/page.tsx` → `console/bpmn/agent-approval-human-override-form.zul`
- ... (55 más)

---

### 3. 🤖 MODEL MANAGEMENT

#### Pantallas Esenciales (8):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/model-management/page.tsx` | `console/platform/models/overview/page.zul` | Dashboard de modelos | 🔴 Alta |
| 2 | `app/(app)/model-management/models/page.tsx` | `console/platform/models/model-overview.zul` | Listado de modelos | 🔴 Alta |
| 3 | `app/(app)/model-management/models/register/page.tsx` | `console/platform/models/model-detail.zul` | Crear/Editar modelo | 🔴 Alta |
| 4 | `app/(app)/model-management/providers/page.tsx` | `console/platform/models/model-provider-overview.zul` | Proveedores de modelos | 🟡 Media |
| 5 | `app/(app)/model-management/experiments/page.tsx` | `console/platform/training/experiments/overview.zul` | Experimentos | 🟡 Media |
| 6 | `app/(app)/model-management/approval/page.tsx` | `console/platform/models/approval/overview.zul` | Aprobaciones | 🔴 Alta |
| 7 | `app/(app)/model-management/performance/page.tsx` | `console/platform/models/performance/overview.zul` | Rendimiento | 🟡 Media |
| 8 | `app/(app)/model-management/bias-analysis/page.tsx` | `console/platform/models/bias-analysis/overview.zul` | Análisis de sesgo | 🟡 Media |

**Pantallas Adicionales Migradas (12+):**
- `artifacts/page.tsx`, `dependencies/page.tsx`, `explainability/page.tsx`, `marketplace/page.tsx`, `serving/page.tsx`, `registry/*/page.tsx`

---

### 4. 📁 PROJECTS

#### Pantallas Esenciales (6):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/projects/page.tsx` | `console/platform/projects/project-overview.zul` | Dashboard de proyectos | 🔴 Alta |
| 2 | `app/(app)/projects/conversational/page.tsx` | `console/platform/projects/conversational/overview.zul` | Proyectos conversacionales | 🔴 Alta |
| 3 | `app/(app)/projects/conversational/new/page.tsx` | `console/platform/projects/project-detail.zul` | Crear proyecto | 🔴 Alta |
| 4 | `app/(app)/projects/generation/page.tsx` | `console/platform/projects/generation/overview.zul` | Proyectos de generación | 🟡 Media |
| 5 | `app/(app)/projects/domains/page.tsx` | `console/platform/projects/project-domain-overview.zul` | Dominios | 🟡 Media |
| 6 | `app/(app)/projects/community/page.tsx` | `console/platform/projects/community/overview.zul` | Comunidad | 🟢 Baja |

**Pantallas Adicionales Migradas (14+):**
- `artifacts/page.tsx`, `documents/page.tsx`, `billing-detail/page.tsx`, `cost-estimator/page.tsx`, `financial-summary/page.tsx`, etc.

---

### 5. 🤖 AGENTS

#### Pantallas Esenciales (7):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/agents/monitoring/dashboard/page.tsx` | `console/platform/agents/monitoring/dashboard.zul` | Dashboard de monitoreo | 🔴 Alta |
| 2 | `app/(app)/agents/monitoring/overview/page.tsx` | `console/platform/agents/monitoring/overview.zul` | Vista general de agentes | 🔴 Alta |
| 3 | `app/(app)/agents/approval/overview/page.tsx` | `console/platform/agents/approval/overview.zul` | Aprobaciones | 🔴 Alta |
| 4 | `app/(app)/agents/deployment/overview/page.tsx` | `console/platform/agents/deployment/overview.zul` | Despliegues | 🟡 Media |
| 5 | `app/(app)/agents/interactions/overview/page.tsx` | `console/platform/agents/interactions/overview.zul` | Interacciones | 🟡 Media |
| 6 | `app/(app)/agents/compliance/overview/page.tsx` | `console/platform/agents/compliance/overview.zul` | Compliance | 🟡 Media |
| 7 | `app/(app)/agents/ethics/overview/page.tsx` | `console/platform/agents/ethics/overview.zul` | Ética | 🟡 Media |

**Pantallas Adicionales Migradas (13+):**
- `learning/overview/page.tsx`, `governance/overview/page.tsx`, `versioning/overview/page.tsx`, `tools/overview/page.tsx`, etc.

---

### 6. 🛡️ COMPLIANCE / GOVERNANCE

#### Pantallas Esenciales (5):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/dashboard/page.tsx` | `console/gobierno/compliance/dashboard.zul` | Dashboard de compliance | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/page.tsx` | `console/gobierno/compliance/compliance-report.zul` | Reportes de compliance | 🔴 Alta |
| 3 | `app/(app)/governance/compliance/post-market-monitoring/page.tsx` | `console/gobierno/compliance/post-market-monitoring-dashboard.zul` | Monitoreo post-mercado | 🟡 Media |
| 4 | `app/(app)/governance/risk-assessment/page.tsx` | `console/gobierno/governance/risk-assessment.zul` | Evaluación de riesgos | 🟡 Media |
| 5 | `app/(app)/governance/policies/page.tsx` | `console/gobierno/governance/policies.zul` | Políticas | 🟡 Media |

**Pantallas Adicionales Migradas (9+):**
- `governance/overview/page.tsx`, `governance/monitoring/page.tsx`, `governance/security/page.tsx`, `governance/compliance/sector/page.tsx`, etc.

---

### 7. 🧠 AI TRAINING

#### Pantallas Esenciales (6):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/training/experiments/page.tsx` | `console/platform/training/experiments/page.zul` | Dashboard de experimentos | 🔴 Alta |
| 2 | `app/(app)/training/experiments/overview/page.tsx` | `console/platform/training/experiments/overview.zul` | Vista general de experimentos | 🔴 Alta |
| 3 | `app/(app)/training/hpo/dashboard/page.tsx` | `console/platform/training/hpo/dashboard.zul` | Dashboard HPO | 🟡 Media |
| 4 | `app/(app)/training/metrics/overview/page.tsx` | `console/platform/training/metrics/overview.zul` | Métricas de entrenamiento | 🟡 Media |
| 5 | `app/(app)/training/execution/overview/page.tsx` | `console/platform/training/execution/overview.zul` | Ejecuciones | 🟡 Media |
| 6 | `app/(app)/training/governance/dashboard/page.tsx` | `console/platform/training/governance/dashboard.zul` | Dashboard de governance | 🟡 Media |

**Pantallas Adicionales Migradas (14+):**
- `experiments/lineage-overview/page.tsx`, `hpo/trial-overview/page.tsx`, `artifacts/overview/page.tsx`, `checkpoints/overview/page.tsx`, etc.

---

### 8. 🔍 RAG SYSTEM

#### Pantallas Esenciales (5):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/rag/page.tsx` | `console/platform/rag/overview/page.zul` | Dashboard RAG | 🔴 Alta |
| 2 | `app/(app)/rag/projects/page.tsx` | `console/platform/rag/projects/overview.zul` | Proyectos RAG | 🔴 Alta |
| 3 | `app/(app)/rag/chat/page.tsx` | `console/platform/rag/chat/page.zul` | Chat RAG | 🔴 Alta |
| 4 | `app/(app)/rag/chunks/page.tsx` | `console/platform/rag/chunks/overview.zul` | Chunks | 🟡 Media |
| 5 | `app/(app)/rag/analytics/page.tsx` | `console/platform/rag/analytics/overview.zul` | Analytics RAG | 🟡 Media |

**Pantallas Adicionales Migradas (18+):**
- `rag/models/page.tsx`, `rag/reranker/page.tsx`, `rag/search/page.tsx`, `rag/monitoring/health-dashboard/page.tsx`, etc.

---

### 9. 🚀 SERVING

#### Pantallas Esenciales (4) - ⏳ PENDIENTE DE MIGRACIÓN:

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | ⏳ `app/(app)/serving/page.tsx` | `console/platform/serving/dashboard.zul` | Dashboard de serving | 🔴 Alta |
| 2 | ⏳ `app/(app)/serving/deployments/page.tsx` | `console/platform/serving/deployment/model-deployment-overview.zul` | Despliegues | 🔴 Alta |
| 3 | ⏳ `app/(app)/serving/monitoring/page.tsx` | `console/platform/serving/monitoring/overview.zul` | Monitoreo | 🟡 Media |
| 4 | ⏳ `app/(app)/serving/endpoints/page.tsx` | `console/platform/serving/endpoints/serving-endpoint-overview.zul` | Endpoints | 🟡 Media |

**Estado:** ⏳ Pendiente de migración (29 pantallas ZUL identificadas)

---

### 10. 💾 DATA SOURCES

#### Pantallas Esenciales (4):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/data-sources/page.tsx` | `console/platform/data-sources/page.zul` | Dashboard de fuentes | 🔴 Alta |
| 2 | `app/(app)/data-sources/database/page.tsx` | `console/platform/data-sources/database/overview/page.zul` | Bases de datos | 🔴 Alta |
| 3 | `app/(app)/data-sources/api/page.tsx` | `console/platform/data-sources/api/overview/page.zul` | APIs | 🟡 Media |
| 4 | `app/(app)/data-sources/upload-documents/page.tsx` | `console/platform/data-sources/documents/overview/page.zul` | Documentos | 🟡 Media |

**Pantallas Adicionales Migradas (1+):**
- `web-scraping/page.tsx`

---

### 11. 📈 ANALYTICS

#### Pantallas Esenciales (3):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/analytics/overview/dashboard/page.tsx` | `console/gobierno/analytics/dashboard.zul` | Dashboard de analytics | 🔴 Alta |
| 2 | `app/(app)/analytics/metrics/overview/page.tsx` | `console/gobierno/analytics/metrics/overview.zul` | Métricas | 🟡 Media |
| 3 | `app/(app)/analytics/reports/overview/page.tsx` | `console/gobierno/analytics/reports/overview.zul` | Reportes | 🟡 Media |

**Pantallas Adicionales Migradas (3+):**
- `trends/overview/page.tsx`, `metrics/detail/page.tsx`, `reports/detail/page.tsx`

---

### 12. 🎮 AI PLAYGROUND

#### Pantallas Esenciales (3):

| # | Next.js | ZUL Equivalente | Descripción | Prioridad |
|---|---------|-----------------|-------------|-----------|
| 1 | `app/(app)/ai-playground/page.tsx` | `console/platform/playground/chat/page.zul` | Chat & Text | 🟡 Media |
| 2 | ⏳ `app/(app)/playground/images/page.tsx` | `console/platform/playground/image/page.zul` | Generación de imágenes | 🟢 Baja |
| 3 | ⏳ `app/(app)/playground/translation/page.tsx` | `console/platform/playground/translation/page.zul` | Traducción | 🟢 Baja |

**Estado:** ⏳ Parcialmente migrado

---

## 🎯 RECOMENDACIONES DE IMPLEMENTACIÓN

### Fase 1: Pantallas Críticas (Prioridad Alta) - 30 pantallas
1. ✅ Dashboard principal
2. ✅ Bandeja de tareas BPMN
3. ✅ Models: Dashboard, Listado, Crear/Editar, Aprobaciones
4. ✅ Projects: Dashboard, Listado, Crear
5. ✅ Agents: Dashboard, Monitoreo, Aprobaciones
6. ✅ Compliance: Dashboard, Reportes
7. ✅ Training: Dashboard, Experimentos
8. ✅ RAG: Dashboard, Proyectos, Chat
9. ✅ Data Sources: Dashboard, Bases de datos
10. ✅ Analytics: Dashboard

### Fase 2: Pantallas Operativas (Prioridad Media) - 23 pantallas
- Monitoreo y métricas detalladas
- Análisis y reportes avanzados
- Gestión de recursos y configuración

### Fase 3: Pantallas Complementarias (Prioridad Baja) - 10 pantallas
- Marketplace y comunidad
- Herramientas de desarrollo
- Playgrounds y experimentación

---

## 📋 CHECKLIST DE VALIDACIÓN

### Para cada pantalla esencial, verificar:

- [ ] **Funcionalidad Core:** CRUD básico operativo
- [ ] **Navegación:** Enlaces y rutas funcionando
- [ ] **Permisos:** Roles y acceso configurados
- [ ] **Datos:** Integración con backend operativa
- [ ] **UI/UX:** Diseño "Wow Factor" aplicado
- [ ] **Internacionalización:** Traducciones ES/EN completas
- [ ] **Responsive:** Adaptación móvil/tablet
- [ ] **Performance:** Carga < 2 segundos

---

## 🔗 REFERENCIAS

- **Documentación de Migración:** `docs/PLAN_MIGRACION_ZUL_VIEWMODELS.md`
- **Prompts de Lógica de Negocio:** `docs/prompts/BUSINESS_LOGIC_*.md`
- **Configuración de Módulos:** `app/config/modules.ts`
- **Reorganización de Pantallas:** `suinsit.nova.web/docs/funcional/*/01_REORGANIZACION_PANTALLAS_*.md`

---

**Última actualización:** Noviembre 2025
**Estado:** ✅ Documento activo - 75% de pantallas esenciales migradas
