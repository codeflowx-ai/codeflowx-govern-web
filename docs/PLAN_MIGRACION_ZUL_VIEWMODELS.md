# PLAN DE MIGRACIÓN ZUL Y VIEWMODELS A NEXT.JS

**Fecha:** Noviembre 2025
**Objetivo:** Migrar ~782 pantallas ZUL y ~611 ViewModels de ZKoss a Next.js
**Estrategia:** Separar dashboards de listados/maestros para optimizar el proceso

---

## 📊 RESUMEN EJECUTIVO

| Categoría | Cantidad Estimada | Prioridad | Complejidad |
|-----------|-------------------|-----------|--------------|
| **Dashboards** | ~150-200 | 🔴 Alta | Media |
| **Listados/Maestros** | ~300-400 | 🟡 Media | Baja |
| **Formularios BPMN** | ~50 | 🔴 Alta | Media |
| **Formularios CRUD** | ~200-300 | 🟡 Media | Baja |
| **TOTAL** | **~782** | - | - |

**Patrón establecido:** `internal-audit-scheduling-form.zul` → `app/(app)/bpmn/forms/internal-audit-scheduling/page.tsx`

---

## 🎯 ESTRATEGIA DE CLASIFICACIÓN

### Criterios de Clasificación:

1. **DASHBOARDS:**
   - Contienen métricas, KPIs, gráficos
   - Nombres: `dashboard.zul`, `overview.zul`, `summary.zul`
   - ViewModels: `*OverviewViewModel`, `*SummaryViewModel`, `*DashboardViewModel`
   - Características: Visualizaciones, estadísticas, resúmenes

2. **LISTADOS/MAESTROS:**
   - Tablas/grids con datos
   - Nombres: `*-overview.zul`, `*-list.zul`, `*-page.zul`
   - ViewModels: `*OverviewViewModel` (con grid/listbox)
   - Características: CRUD, filtros, paginación, búsqueda

3. **FORMULARIOS:**
   - Formularios de entrada de datos
   - Nombres: `*-form.zul`, `*-detail.zul`, `*-wizard.zul`
   - ViewModels: `*DetailViewModel`, `*FormViewModel`, `*WizardViewModel`
   - Características: Inputs, validaciones, submit

---

## 📋 INVENTARIO POR MÓDULO

### 1. BPMN (50+ formularios)

#### Formularios BPMN (Prioridad Alta):
- ✅ `task-inbox.zul` - **MIGRADO** ✅
- ✅ `internal-audit-scheduling-form.zul` - **MIGRADO** ✅ (ejemplo)
- ⏳ `compliance-review-form.zul`
- ⏳ `deployer-approval-form.zul`
- ⏳ `agent-approval-human-override-form.zul`
- ⏳ `fria-wizard.zul` (wizard multi-paso)
- ⏳ `high-risk-classifier.zul` (formulario complejo)
- ⏳ `review-qms-gaps-form.zul`
- ⏳ `complete-documentation-form.zul`
- ⏳ `final-review-form.zul`
- ⏳ `approve-conformity-assessment-form.zul`
- ⏳ `document-incident-details-form.zul`
- ⏳ `root-cause-analysis-form.zul`
- ⏳ `define-corrective-actions-form.zul`
- ⏳ `verify-incident-resolution-form.zul`
- ⏳ `complete-missing-elements-form.zul`
- ⏳ `enhanced-review-form.zul`
- ⏳ `define-modifications-form.zul`
- ⏳ `eu-registration-form.zul`
- ⏳ `fix-validation-errors-form.zul`
- ⏳ `review-registration-package-form.zul`
- ⏳ `manual-resolution-form.zul`
- ⏳ ... (30+ más)

**Total:** ~50 formularios BPMN
**Estado:** 2 migrados (4%), 48 pendientes (96%)
**Prompt:** `docs/prompts/BPMN_FORM_MIGRATION.md` ✅

---

### 2. GOBIERNO/COMPLIANCE

#### Dashboards:
- ⏳ `gobierno/compliance/dashboard.zul` - Dashboard compliance
- ⏳ `gobierno/compliance/sector-dashboard.zul` - Dashboard por sector
- ⏳ `gobierno/compliance/post-market-monitoring-dashboard.zul` - Dashboard PMM
- ⏳ `gobierno/governance/governance-overview.zul` - Overview gobernanza
- ⏳ `gobierno/projects/projects-dashboard.zul` - Dashboard proyectos
- ⏳ `gobierno/serving/serving-dashboard.zul` - Dashboard serving
- ⏳ `gobierno/rag/rag-systems-overview.zul` - Overview RAG
- ⏳ `gobierno/providers/providers-overview.zul` - Overview proveedores

#### Listados/Maestros:
- ⏳ `gobierno/projects/projects-overview.zul` - Listado proyectos
- ⏳ `gobierno/models/models-overview.zul` - Listado modelos
- ⏳ `gobierno/prompts/prompts-detail.zul` - Listado prompts
- ⏳ `gobierno/compliance/compliance-report.zul` - Reportes compliance
- ⏳ `gobierno/compliance/traceability-evidence.zul` - Trazabilidad
- ⏳ `gobierno/compliance/log-search-advanced.zul` - Búsqueda logs
- ⏳ `gobierno/compliance/bias-visualization.zul` - Visualización bias
- ⏳ `gobierno/compliance/risk-tree-export.zul` - Árbol riesgos
- ⏳ `gobierno/compliance/eu-registration-status.zul` - Estado registro UE
- ⏳ `gobierno/compliance/conformity-declaration-manager.zul` - Gestión declaraciones
- ⏳ `gobierno/compliance/ai-act-documentation-generator.zul` - Generador documentación
- ⏳ `gobierno/compliance/conformity-review-form.zul` - Revisión conformidad
- ⏳ `gobierno/governance/governance-detail.zul` - Detalle gobernanza
- ⏳ `gobierno/governance/ai_objectives_management.zul` - Gestión objetivos
- ⏳ `gobierno/governance/ai_competence_management.zul` - Gestión competencias
- ⏳ `gobierno/governance/hitl-supervision.zul` - Supervisión HITL
- ⏳ `gobierno/rag/rag-client-policies-overview.zul` - Políticas RAG
- ⏳ `gobierno/rag/rag-client-policies-detail.zul` - Detalle políticas RAG
- ⏳ `gobierno/integrations/external-platforms.zul` - Plataformas externas
- ⏳ `gobierno/reports/executive-report.zul` - Reporte ejecutivo

**Total estimado:** ~30 dashboards + ~50 listados/maestros

---

### 3. PLATFORM

#### Dashboards:
- ⏳ `platform/governance/dashboard/summary.zul` - Dashboard gobernanza
- ⏳ `platform/governance/dashboard/overview.zul` - Overview gobernanza
- ⏳ `platform/monitoring/dashboard/page.zul` - Dashboard monitoreo
- ⏳ `platform/monitoring/dashboard/overview.zul` - Overview monitoreo
- ⏳ `platform/monitoring/dashboard/summary.zul` - Summary monitoreo
- ⏳ `platform/playground/page.zul` - Playground principal
- ⏳ `platform/evaluation/model-evaluation/page.zul` - Evaluación modelos
- ⏳ `platform/evaluation/bias-detection/page.zul` - Detección bias
- ⏳ `platform/evaluation/rag-evaluation/overview.zul` - Evaluación RAG

#### Listados/Maestros:
- ⏳ `platform/projects/project-ai-inventory.zul` - Inventario sistemas IA
- ⏳ `platform/projects/project-detail.zul` - Detalle proyecto
- ⏳ `platform/compliance/fria-assessments.zul` - Evaluaciones FRIA
- ⏳ `platform/compliance/assessments.zul` - Evaluaciones conformidad
- ⏳ `platform/models/detail/page.zul` - Detalle modelo
- ⏳ `platform/models/approval/overview.zul` - Aprobaciones modelos
- ⏳ `platform/monitoring/alerts/overview.zul` - Alertas
- ⏳ `platform/monitoring/alerts/detail.zul` - Detalle alerta
- ⏳ `platform/monitoring/alerts/system-overview.zul` - Alertas sistema
- ⏳ `platform/monitoring/alerts/system-detail.zul` - Detalle alerta sistema
- ⏳ `platform/monitoring/alerts/summary.zul` - Summary alertas
- ⏳ `platform/data-sources/page.zul` - Fuentes de datos
- ⏳ `platform/data-sources/documents/overview/page.zul` - Documentos
- ⏳ `platform/data-sources/documents/detail/page.zul` - Detalle documento
- ⏳ `platform/data-sources/database/overview/page.zul` - Bases de datos
- ⏳ `platform/data-sources/database/detail/page.zul` - Detalle base datos
- ⏳ `platform/data-sources/api/overview/page.zul` - APIs
- ⏳ `platform/data-sources/api/detail/page.zul` - Detalle API
- ⏳ `platform/data-sources/web-scraping/overview/page.zul` - Web scraping
- ⏳ `platform/data-sources/web-scraping/detail/page.zul` - Detalle web scraping
- ⏳ `platform/prompts/registry/page.zul` - Registro prompts
- ⏳ `platform/training/tags/page.zul` - Tags entrenamiento
- ⏳ `platform/training/tags/overview.zul` - Overview tags
- ⏳ `platform/core/user-detail.zul` - Detalle usuario
- ⏳ `platform/core/department-detail.zul` - Detalle departamento
- ⏳ `platform/serving/model-deployment-detail.zul` - Detalle deployment
- ⏳ `platform/serving/serving-endpoint-detail.zul` - Detalle endpoint
- ⏳ `platform/artefacto/base-artifact-detail.zul` - Detalle artefacto
- ⏳ `platform/evaluation/bias-detection/overview.zul` - Overview bias
- ⏳ `platform/evaluation/model-evaluation/overview.zul` - Overview evaluación
- ⏳ `platform/playground/translation/page.zul` - Traducción
- ⏳ `platform/playground/routing/page.zul` - Routing
- ⏳ `platform/playground/voice/page.zul` - Voz
- ⏳ `platform/playground/image/page.zul` - Imágenes
- ⏳ `platform/governance/compliance/page.zul` - Compliance
- ⏳ `platform/governance/iso42001_controls.zul` - Controles ISO 42001
- ⏳ `platform/governance/aims_improvement.zul` - Mejoras AIMS
- ⏳ `platform/governance/aims_nonconformity.zul` - No conformidades AIMS
- ⏳ `platform/governance/aims_performance.zul` - Rendimiento AIMS

**Total estimado:** ~15 dashboards + ~40 listados/maestros

---

### 4. OTROS MÓDULOS

#### AIOS:
- ⏳ `aios/dashboard/ai-runtime-dashboard.zul` - Dashboard runtime
- ⏳ `aios/agents/agent-list.zul` - Listado agentes
- ⏳ `aios/agents/agent-detail.zul` - Detalle agente
- ⏳ `aios/agents/supervisor-monitor.zul` - Monitor supervisor
- ⏳ `aios/agents/memory-list.zul` - Listado memorias

#### Developer:
- ⏳ `gobierno/developer/developer-console.zul` - Consola desarrollador

**Total estimado:** ~10 pantallas

---

## 📐 PLAN DE MIGRACIÓN POR FASES

### FASE 1: FORMULARIOS BPMN (Prioridad Alta)

**Objetivo:** Migrar todos los formularios BPMN siguiendo el patrón establecido

**Criterios:**
- Formularios asociados a User Tasks BPMN
- Formularios de workflow
- Formularios de aprobación/revisión

**Cantidad:** ~50 formularios

**Patrón:** `app/(app)/bpmn/forms/{form-name}/page.tsx`

**Priorización:**
1. **Críticos (10):**
   - `fria-wizard.zul` (wizard multi-paso)
   - `high-risk-classifier.zul` (complejo)
   - `compliance-review-form.zul`
   - `deployer-approval-form.zul`
   - `review-qms-gaps-form.zul`
   - `complete-documentation-form.zul`
   - `final-review-form.zul`
   - `approve-conformity-assessment-form.zul`
   - `eu-registration-form.zul`
   - `agent-approval-human-override-form.zul`

2. **Altos (15):**
   - Resto de formularios de conformidad
   - Formularios de incidentes
   - Formularios de acciones correctivas

3. **Medios (25):**
   - Formularios de gestión
   - Formularios de evaluación
   - Formularios de monitoreo

**Esfuerzo estimado:** 2-3 días por formulario (con agente)
**Total:** ~100-150 días de trabajo
**Con agente:** ~20-30 días (5 formularios en paralelo)

---

### FASE 2: DASHBOARDS (Prioridad Alta)

**Objetivo:** Migrar dashboards principales con métricas y visualizaciones

**Criterios:**
- Contienen KPIs, métricas, gráficos
- Nombres: `dashboard.zul`, `overview.zul`, `summary.zul`
- ViewModels: `*DashboardViewModel`, `*OverviewViewModel`, `*SummaryViewModel`

**Cantidad:** ~150-200 dashboards

**Patrón:** `app/(app)/{module}/{submodule}/dashboard/page.tsx` o `app/(app)/{module}/{submodule}/page.tsx`

**Priorización:**

#### 2.1 Dashboards Críticos (20):
1. ⏳ `gobierno/compliance/dashboard.zul` - Dashboard compliance principal
2. ⏳ `gobierno/governance/governance-overview.zul` - Overview gobernanza
3. ⏳ `gobierno/projects/projects-dashboard.zul` - Dashboard proyectos
4. ⏳ `gobierno/serving/serving-dashboard.zul` - Dashboard serving
5. ⏳ `platform/governance/dashboard/summary.zul` - Summary gobernanza
6. ⏳ `platform/monitoring/dashboard/page.zul` - Dashboard monitoreo
7. ⏳ `gobierno/compliance/post-market-monitoring-dashboard.zul` - Dashboard PMM
8. ⏳ `gobierno/compliance/sector-dashboard.zul` - Dashboard por sector
9. ⏳ `gobierno/rag/rag-systems-overview.zul` - Overview RAG
10. ⏳ `gobierno/providers/providers-overview.zul` - Overview proveedores
11. ⏳ `platform/evaluation/model-evaluation/page.zul` - Evaluación modelos
12. ⏳ `platform/evaluation/bias-detection/page.zul` - Detección bias
13. ⏳ `platform/playground/page.zul` - Playground principal
14. ⏳ `aios/dashboard/ai-runtime-dashboard.zul` - Dashboard runtime
15. ⏳ `console/dashboard.zul` - Dashboard principal
16. ⏳ `platform/governance/dashboard/overview.zul` - Overview gobernanza
17. ⏳ `platform/monitoring/dashboard/overview.zul` - Overview monitoreo
18. ⏳ `platform/monitoring/dashboard/summary.zul` - Summary monitoreo
19. ⏳ `platform/evaluation/rag-evaluation/overview.zul` - Evaluación RAG
20. ⏳ `gobierno/governance/hitl-supervision.zul` - Supervisión HITL

#### 2.2 Dashboards Importantes (30):
- Dashboards de módulos específicos
- Dashboards de métricas
- Dashboards de análisis

#### 2.3 Dashboards Secundarios (100-150):
- Dashboards de sub-módulos
- Dashboards de reportes
- Dashboards de visualización

**Esfuerzo estimado:** 3-5 días por dashboard (más complejo que formularios)
**Total:** ~450-1000 días de trabajo
**Con agente:** ~30-50 días (5 dashboards en paralelo)

---

### FASE 3: LISTADOS/MAESTROS (Prioridad Media)

**Objetivo:** Migrar listados y páginas maestras con CRUD

**Criterios:**
- Contienen tablas/grids con datos
- Nombres: `*-overview.zul`, `*-list.zul`, `*-page.zul`
- ViewModels: `*OverviewViewModel` (con grid/listbox)
- Funcionalidades: CRUD, filtros, paginación, búsqueda

**Cantidad:** ~300-400 listados/maestros

**Patrón:** `app/(app)/{module}/{submodule}/page.tsx` (listado) + `app/(app)/{module}/{submodule}/[id]/page.tsx` (detalle)

**Priorización:**

#### 3.1 Listados Críticos (30):
1. ⏳ `gobierno/projects/projects-overview.zul` - Listado proyectos
2. ⏳ `gobierno/models/models-overview.zul` - Listado modelos
3. ⏳ `platform/projects/project-ai-inventory.zul` - Inventario sistemas IA
4. ⏳ `platform/compliance/fria-assessments.zul` - Evaluaciones FRIA
5. ⏳ `platform/compliance/assessments.zul` - Evaluaciones conformidad
6. ⏳ `platform/monitoring/alerts/overview.zul` - Alertas
7. ⏳ `platform/data-sources/page.zul` - Fuentes de datos
8. ⏳ `platform/prompts/registry/page.zul` - Registro prompts
9. ⏳ `gobierno/compliance/compliance-report.zul` - Reportes compliance
10. ⏳ `gobierno/compliance/traceability-evidence.zul` - Trazabilidad
11. ⏳ `gobierno/compliance/log-search-advanced.zul` - Búsqueda logs
12. ⏳ `gobierno/compliance/eu-registration-status.zul` - Estado registro UE
13. ⏳ `gobierno/compliance/conformity-declaration-manager.zul` - Gestión declaraciones
14. ⏳ `gobierno/governance/governance-detail.zul` - Detalle gobernanza
15. ⏳ `gobierno/governance/ai_objectives_management.zul` - Gestión objetivos
16. ⏳ `gobierno/governance/ai_competence_management.zul` - Gestión competencias
17. ⏳ `gobierno/rag/rag-client-policies-overview.zul` - Políticas RAG
18. ⏳ `gobierno/integrations/external-platforms.zul` - Plataformas externas
19. ⏳ `platform/models/approval/overview.zul` - Aprobaciones modelos
20. ⏳ `platform/data-sources/documents/overview/page.zul` - Documentos
21. ⏳ `platform/data-sources/database/overview/page.zul` - Bases de datos
22. ⏳ `platform/data-sources/api/overview/page.zul` - APIs
23. ⏳ `platform/data-sources/web-scraping/overview/page.zul` - Web scraping
24. ⏳ `platform/training/tags/page.zul` - Tags entrenamiento
25. ⏳ `aios/agents/agent-list.zul` - Listado agentes
26. ⏳ `platform/monitoring/alerts/system-overview.zul` - Alertas sistema
27. ⏳ `platform/core/user-detail.zul` - Detalle usuario
28. ⏳ `platform/core/department-detail.zul` - Detalle departamento
29. ⏳ `platform/serving/model-deployment-detail.zul` - Detalle deployment
30. ⏳ `platform/serving/serving-endpoint-detail.zul` - Detalle endpoint

#### 3.2 Listados Importantes (70):
- Listados de sub-módulos
- Listados de gestión
- Listados de configuración

#### 3.3 Listados Secundarios (200-300):
- Listados de reportes
- Listados de auditoría
- Listados de administración

**Esfuerzo estimado:** 2-3 días por listado (más simple que dashboards)
**Total:** ~600-1200 días de trabajo
**Con agente:** ~60-120 días (5 listados en paralelo)

---

### FASE 4: FORMULARIOS CRUD (Prioridad Media)

**Objetivo:** Migrar formularios de creación/edición CRUD

**Criterios:**
- Formularios de creación/edición
- Nombres: `*-detail.zul`, `*-form.zul`
- ViewModels: `*DetailViewModel`, `*FormViewModel`
- Funcionalidades: Inputs, validaciones, CRUD

**Cantidad:** ~200-300 formularios CRUD

**Patrón:** `app/(app)/{module}/{submodule}/[id]/page.tsx` (edición) + `app/(app)/{module}/{submodule}/new/page.tsx` (creación)

**Esfuerzo estimado:** 1-2 días por formulario (similar a BPMN pero más simple)
**Total:** ~200-600 días de trabajo
**Con agente:** ~40-60 días (5 formularios en paralelo)

---

## 🎯 PRIORIZACIÓN GENERAL

### Prioridad 🔴 CRÍTICA (Inmediata):

1. **Formularios BPMN críticos** (10 formularios)
   - Esfuerzo: ~20-30 días
   - Impacto: Funcionalidad core workflows

2. **Dashboards principales** (20 dashboards)
   - Esfuerzo: ~60-100 días
   - Impacto: Visibilidad y métricas

**Total Fase 1:** ~80-130 días

### Prioridad 🟡 ALTA (Próximo Sprint):

3. **Formularios BPMN altos** (15 formularios)
   - Esfuerzo: ~30-45 días

4. **Listados críticos** (30 listados)
   - Esfuerzo: ~60-90 días

5. **Dashboards importantes** (30 dashboards)
   - Esfuerzo: ~90-150 días

**Total Fase 2:** ~180-285 días

### Prioridad 🟢 MEDIA (Backlog):

6. **Formularios BPMN medios** (25 formularios)
   - Esfuerzo: ~50-75 días

7. **Listados importantes** (70 listados)
   - Esfuerzo: ~140-210 días

8. **Formularios CRUD** (200-300 formularios)
   - Esfuerzo: ~200-600 días

**Total Fase 3:** ~390-885 días

---

## 📝 ESTRUCTURA DE ARCHIVOS NEXT.JS

### Para Dashboards:

```
app/(app)/
  {module}/
    {submodule}/
      page.tsx                    # Dashboard principal
      dashboard/
        page.tsx                  # Dashboard alternativo
      overview/
        page.tsx                  # Overview
      summary/
        page.tsx                  # Summary
```

**Ejemplo:**
```
app/(app)/
  gobierno/
    compliance/
      page.tsx                    # Dashboard compliance
      dashboard/
        page.tsx                  # Dashboard alternativo
```

### Para Listados/Maestros:

```
app/(app)/
  {module}/
    {submodule}/
      page.tsx                    # Listado (overview)
      [id]/
        page.tsx                  # Detalle/Edición
      new/
        page.tsx                  # Creación (opcional)
```

**Ejemplo:**
```
app/(app)/
  gobierno/
    projects/
      page.tsx                    # Listado proyectos
      [id]/
        page.tsx                  # Detalle proyecto
      new/
        page.tsx                  # Crear proyecto
```

### Para Formularios BPMN:

```
app/(app)/
  bpmn/
    forms/
      {form-name}/
        page.tsx                  # Formulario
```

**Ejemplo:**
```
app/(app)/
  bpmn/
    forms/
      internal-audit-scheduling/
        page.tsx                  # ✅ Ya migrado
      compliance-review/
        page.tsx                  # Pendiente
```

---

## 🔄 PROCESO DE MIGRACIÓN

### Paso 1: Análisis

1. **Leer archivo ZUL:**
   - Identificar componentes (grid, listbox, textbox, etc.)
   - Identificar estructura y layout
   - Identificar validaciones

2. **Leer ViewModel Java:**
   - Identificar propiedades (`@Getter @Setter`)
   - Identificar comandos (`@Command`)
   - Identificar validaciones
   - Identificar servicios utilizados

3. **Clasificar:**
   - ¿Es dashboard, listado o formulario?
   - ¿Qué complejidad tiene?
   - ¿Qué dependencias tiene?

### Paso 2: Creación

1. **Crear estructura de archivos:**
   - Crear directorio según patrón
   - Crear `page.tsx`

2. **Migrar componentes:**
   - ZUL → React/Next.js
   - ViewModel → React hooks (useState, useEffect)
   - Comandos → Funciones handler

3. **Agregar traducciones:**
   - Agregar claves en `i18n.ts`
   - Español e inglés

4. **Agregar al menú:**
   - Agregar en `modules.ts` si aplica
   - Agregar icono si no existe

### Paso 3: Validación

1. **Verificar linter:**
   - Sin errores TypeScript
   - Sin warnings

2. **Probar en navegador:**
   - Navegación funciona
   - Componentes renderizan
   - Validaciones funcionan

3. **Documentar:**
   - Actualizar este plan
   - Marcar como completado

---

## 📊 MÉTRICAS Y SEGUIMIENTO

### Tracking por Fase:

| Fase | Total | Migrados | Pendientes | % Completado |
|------|-------|----------|------------|--------------|
| **Fase 1: BPMN Críticos** | 10 | 2 | 8 | 20% |
| **Fase 2: Dashboards Críticos** | 20 | 0 | 20 | 0% |
| **Fase 3: Listados Críticos** | 30 | 0 | 30 | 0% |
| **Fase 4: BPMN Altos** | 15 | 0 | 15 | 0% |
| **Fase 5: Dashboards Importantes** | 30 | 0 | 30 | 0% |
| **Fase 6: Listados Importantes** | 70 | 0 | 70 | 0% |
| **Fase 7: Resto** | ~627 | 0 | ~627 | 0% |
| **TOTAL** | **~782** | **2** | **~780** | **0.3%** |

### Velocidad Estimada:

- **Con agente:** 5 pantallas/día (en paralelo)
- **Sin agente:** 1 pantalla/día (manual)
- **Total con agente:** ~156 días (~5 meses)
- **Total sin agente:** ~782 días (~2.5 años)

---

## 🛠️ HERRAMIENTAS Y RECURSOS

### Prompts Disponibles:

1. ✅ `docs/prompts/BPMN_FORM_MIGRATION.md` - Para formularios BPMN
2. ⏳ `docs/prompts/DASHBOARD_MIGRATION.md` - Por crear
3. ⏳ `docs/prompts/LISTADO_MAESTRO_MIGRATION.md` - Por crear

### Componentes Reutilizables:

- `components/ui/Table.tsx` - Tablas
- `components/ui/Card.tsx` - Cards
- `components/ui/Button.tsx` - Botones
- `components/ui/Input.tsx` - Inputs
- `components/ui/Select.tsx` - Selects
- `components/ui/Dialog.tsx` - Modales
- `components/ui/Badge.tsx` - Badges

### Patrones Establecidos:

- ✅ Formulario BPMN: `internal-audit-scheduling` (ejemplo)
- ⏳ Dashboard: Por definir
- ⏳ Listado/Maestro: Por definir

---

## 📅 CRONOGRAMA SUGERIDO

### Sprint 1-2 (4 semanas): Formularios BPMN Críticos

- **Objetivo:** Migrar 10 formularios BPMN críticos
- **Esfuerzo:** ~20-30 días
- **Resultado:** Workflows BPMN funcionales en Next.js

### Sprint 3-4 (4 semanas): Dashboards Críticos

- **Objetivo:** Migrar 20 dashboards principales
- **Esfuerzo:** ~60-100 días
- **Resultado:** Dashboards principales visibles

### Sprint 5-6 (4 semanas): Listados Críticos

- **Objetivo:** Migrar 30 listados críticos
- **Esfuerzo:** ~60-90 días
- **Resultado:** CRUD principal funcional

### Sprint 7+ (Continuo): Resto

- **Objetivo:** Completar migración gradual
- **Esfuerzo:** ~600-1500 días
- **Resultado:** Migración completa

---

## ✅ CHECKLIST DE MIGRACIÓN

Para cada pantalla migrada:

- [ ] Archivo ZUL leído y analizado
- [ ] ViewModel Java leído y analizado
- [ ] Clasificación realizada (dashboard/listado/formulario)
- [ ] Página Next.js creada
- [ ] Componentes migrados correctamente
- [ ] Estado mapeado (ViewModel → React hooks)
- [ ] Validaciones implementadas
- [ ] Traducciones agregadas (español e inglés)
- [ ] Agregada al menú si aplica
- [ ] Icono agregado si no existe
- [ ] Linter sin errores
- [ ] Probado en navegador
- [ ] Navegación funciona
- [ ] Funcionalidad validada
- [ ] Documentado en este plan

---

## 📚 REFERENCIAS

### Documentos Relacionados:

- `docs/prompts/BPMN_FORM_MIGRATION.md` - Prompt para formularios BPMN
- `MIGRATION_GUIDE.md` - Guía general de migración
- `docs/compliance/ESTADO_GENERAL_IMPLEMENTACION.md` - Estado actual

### Ejemplos Migrados:

- ✅ `app/(app)/bpmn/task-inbox/page.tsx` - Bandeja de tareas
- ✅ `app/(app)/bpmn/forms/internal-audit-scheduling/page.tsx` - Formulario ejemplo

---

**Última actualización:** Noviembre 2025
**Mantenido por:** Equipo Arquitectura CodeflowX
**Próxima revisión:** Diciembre 2025
