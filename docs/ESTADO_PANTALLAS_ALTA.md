# 📊 ESTADO DE PANTALLAS DE ALTA - CODEFLOWX

**Fecha:** Octubre 2025  
**Status:** Documentado para mañana  
**Nota:** Pantallas BPMN las actualiza el usuario manualmente

---

## ✅ PANTALLAS ACTUALIZADAS A BOOTSTRAP

### console/bpmn/
- ✅ `task-inbox.zul` (269 líneas) - **ACTUALIZADO** - Usa Bootstrap

---

## ⚠️ PANTALLAS PENDIENTES DE ACTUALIZACIÓN

### console/bpmn/ (24 pantallas)

Pantallas que aún usan componentes ZKoss antiguos (`<hlayout>`, `<vlayout>`, etc.):

1. `agent-approval-human-override-form.zul` (155 líneas)
2. `alert-response-form.zul` (55 líneas)
3. `bias-mitigation-plan-form.zul` (55 líneas)
4. `bias-review-form.zul` (130 líneas)
5. `bias-urgent-decision-form.zul` (108 líneas)
6. `compliance-review-decision-form.zul` (125 líneas)
7. `compliance-review-form.zul` (55 líneas)
8. `dataset-review-reminder-form.zul` (33 líneas)
9. `drift-analysis-form.zul` (55 líneas)
10. `drift-review-decision-form.zul` (102 líneas)
11. `ethics-committee-review-form.zul` (55 líneas)
12. `ethics-mitigation-plan-form.zul` (55 líneas)
13. `ethics-review-reminder-form.zul` (35 líneas)
14. `ethics-review-request-form.zul` (55 líneas)
15. `hitl-sla-reminder-form.zul` (109 líneas)
16. `llm-evaluation-review-form.zul` (140 líneas)
17. `model-approval-human-override-form.zul` (55 líneas)
18. `model-approval-reminder-form.zul` (33 líneas)
19. `model-evaluation-review-form.zul` (55 líneas)
20. `performance-intervention-form.zul` (55 líneas)
21. `performance-review-decision-form.zul` (100 líneas)
22. `prompt-approval-request-form.zul` (118 líneas)
23. `prompt-human-review-form.zul` (180 líneas)
24. `rag-evaluation-review-form.zul` (55 líneas)

**Total:** 24 pantallas pendientes de actualización

---

## 📋 PATRÓN DE ACTUALIZACIÓN

### Componentes ZKoss → Bootstrap

| ZKoss Antiguo | Bootstrap Nuevo |
|---------------|-----------------|
| `<hlayout>` | `<div class="d-flex gap-2">` |
| `<vlayout>` | `<div class="vstack gap-2">` |
| `<borderlayout>` | `<div class="row"><div class="col-12">` |
| `<north>` | `<div class="header-section">` |
| `<center>` | `<div class="main-content">` |
| `<south>` | `<div class="footer-section">` |

### Ejemplo de Migración

**Antes (ZKoss):**
```xml
<hlayout spacing="10px">
    <label value="Nombre"/>
    <textbox value="@bind(vm.name)"/>
</hlayout>
```

**Después (Bootstrap):**
```xml
<div class="d-flex gap-2 align-items-center">
    <label value="Nombre" style="min-width: 150px;"/>
    <textbox value="@bind(vm.name)" class="form-control flex-grow-1"/>
</div>
```

---

## 🎯 PRIORIDAD DE ACTUALIZACIÓN

### Alta Prioridad (Pantallas más usadas)
1. `agent-approval-human-override-form.zul` - Revisión de agentes
2. `model-approval-human-override-form.zul` - Revisión de modelos
3. `compliance-review-form.zul` - Revisión de compliance
4. `llm-evaluation-review-form.zul` - Evaluación de LLMs
5. `prompt-human-review-form.zul` - Revisión de prompts

### Media Prioridad
6. `bias-review-form.zul` - Revisión de sesgo
7. `drift-review-decision-form.zul` - Decisión de drift
8. `ethics-committee-review-form.zul` - Comité ético
9. `performance-review-decision-form.zul` - Revisión de performance

### Baja Prioridad (Recordatorios)
10. `model-approval-reminder-form.zul`
11. `ethics-review-reminder-form.zul`
12. `dataset-review-reminder-form.zul`
13. `hitl-sla-reminder-form.zul`

---

## ✅ VIEWMODELS EXISTENTES

Todos los ViewModels para estas pantallas ya existen en:
```
src/main/java/com/codeflowx/govern/workflow/viewmodels/
```

Lista completa:
- `TaskInboxViewModel.java` ✅
- `AgentApprovalHumanOverrideViewModel.java` ✅
- `ModelApprovalHumanOverrideViewModel.java` ✅
- `ComplianceReviewViewModel.java` ✅
- ... (25 ViewModels totales)

---

## 🚀 PLAN DE ACCIÓN

### Para el Usuario (Mañana):
1. Actualizar las 24 pantallas BPMN manualmente
2. Aplicar patrón Bootstrap consistente
3. Mantener funcionalidad BPMN intacta
4. Verificar que ViewModels funcionan correctamente

### Orden sugerido:
1. `agent-approval-human-override-form.zul`
2. `model-approval-human-override-form.zul`
3. `compliance-review-form.zul`
4. `llm-evaluation-review-form.zul`
5. `prompt-human-review-form.zul`

---

## 📝 PANTALLAS CRUD (NO BPMN)

### ViewModels DetailView (Listados + Detalles):
- `AgentsDetailViewModel.java`
- `ModelsDetailViewModel.java`
- `PromptsDetailViewModel.java`
- `RagSystemsDetailViewModel.java`
- `ProvidersDetailViewModel.java`
- `GovernanceDetailViewModel.java`
- `InfrastructureDetailViewModel.java`
- `ExperimentsDetailViewModel.java`

### ViewModels Dashboard:
- `AgentsDashboardViewModel.java`
- `MainDashboardViewModel.java`
- `GovernanceDashboardViewModel.java`
- `TrainingDashboardViewModel.java`
- `MonitoringDashboardViewModel.java`
- `ServingDashboardViewModel.java`
- `ProjectsDashboardViewModel.java`
- `CatalogDashboardViewModel.java`
- `AdminDashboardViewModel.java`

**Total:** 17 ViewModels manuales (CRUD)

---

## ✅ RESULTADO

**Pantallas CRUD:** Ya listas (no requieren actualización)  
**Pantallas BPMN:** Pendientes de actualización Bootstrap (usuario las hace manualmente)  
**ViewModels:** Todos existentes y funcionales ✅

**Estado Final:** Listo para mañana ✅

