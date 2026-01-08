# Estado Actual: Formularios BPMN - Migración a Next.js

**Fecha:** Enero 2025
**Objetivo:** Documentar el estado de migración de formularios BPMN de ZKoss (ZUL) a Next.js 14 y proporcionar un prompt para agentes que continúen la migración.

---

## 📊 Resumen Ejecutivo

### Estado General
- **Total procesos BPMN documentados:** 40+ procesos
- **Formularios identificados:** ~80 formularios (estimado)
- **Formularios migrados:** 4 formularios confirmados (model-approval)
- **Formularios pendientes:** ~76 formularios
- **Progreso:** ~5% completado

### Procesos con Formularios Migrados
1. ✅ **model-approval-v1** - 4 formularios migrados
   - `model-approval-request` ✅
   - `model-ml-review` ✅
   - `model-governance-review` ✅
   - `model-approval-reminder` ✅

---

## 📋 Inventario por Categoría de Proceso

### 1. AI-OS / Runtime (`processes/aios/`)

#### ✅ model-approval-v1 (COMPLETADO)
| Formulario | ZUL Original | Next.js | Estado | Pendientes |
|------------|--------------|---------|--------|------------|
| model-approval-request | `plataforma/workflow/model-approval-request-form.zul` | `app/(app)/bpmn/forms/model-approval-request/page.tsx` | ✅ Migrado | Traducciones i18n, carga datos workflow |
| model-ml-review | `plataforma/workflow/model-ml-review-form.zul` | `app/(app)/bpmn/forms/model-ml-review/page.tsx` | ✅ Migrado | Traducciones i18n, carga datos workflow |
| model-governance-review | `plataforma/workflow/model-governance-review-form.zul` | `app/(app)/bpmn/forms/model-governance-review/page.tsx` | ✅ Migrado | Traducciones i18n, carga datos workflow |
| model-approval-reminder | `bpmn/model-approval-reminder-form.zul` | `app/(app)/bpmn/forms/model-approval-reminder/page.tsx` | ✅ Migrado | - |

**Documentación:** `docs/compliance/bpmn/aios/model-approval/functional.md` y `technical.md`

#### ⚠️ agent-approval-v1 (PENDIENTE)
**Formularios esperados:**
- `agent-approval-request-form` - Solicitud de aprobación de agente
- `agent-risk-review-form` - Revisión de riesgos
- `agent-compliance-review-form` - Revisión de compliance
- `agent-ethics-review-form` - Revisión ética

**Documentación:** `docs/compliance/bpmn/aios/agent-approval/functional.md` y `technical.md`

#### ⚠️ prompt-approval-v1 (PENDIENTE)
**Formularios esperados:**
- `prompt-approval-request-form` - Solicitud de aprobación de prompt
- `prompt-safety-review-form` - Revisión de seguridad
- `prompt-policy-review-form` - Revisión de políticas

**Documentación:** `docs/compliance/bpmn/aios/prompt-approval/functional.md` y `technical.md`

#### ⚠️ adapter-creation-approval-v1 (PENDIENTE)
**Formularios esperados:**
- `adapter-creation-request-form` - Solicitud de creación de adapter
- `adapter-risk-assessment-form` - Evaluación de riesgos
- `adapter-approval-form` - Aprobación final

**Documentación:** `docs/compliance/bpmn/aios/adapter-creation-approval/functional.md` y `technical.md`

#### ⚠️ finetuning-approval-v1 (PENDIENTE)
**Formularios esperados:**
- `finetuning-request-form` - Solicitud de fine-tuning
- `finetuning-evidence-form` - Evidencias de fine-tuning
- `finetuning-approval-form` - Aprobación

**Documentación:** `docs/compliance/bpmn/aios/finetuning-approval/functional.md` y `technical.md`

#### ⚠️ ai-component-onboarding-v1 (PENDIENTE)
**Formularios esperados:**
- `component-onboarding-request-form` - Solicitud de onboarding
- `component-verification-form` - Verificación de componente
- `component-activation-form` - Activación

**Documentación:** `docs/compliance/bpmn/aios/ai-component-onboarding/functional.md` y `technical.md`

#### ⚠️ ai-marketplace-publish-v1 (PENDIENTE)
**Formularios esperados:**
- `marketplace-publish-request-form` - Solicitud de publicación
- `marketplace-scoring-review-form` - Revisión de scoring
- `marketplace-legal-review-form` - Revisión legal

**Documentación:** `docs/compliance/bpmn/aios/ai-marketplace-publish/functional.md` y `technical.md`

#### ⚠️ external-model-approval-v1 (PENDIENTE)
**Formularios esperados:**
- `external-model-request-form` - Solicitud de modelo externo
- `external-model-risk-form` - Evaluación de riesgo
- `external-model-fria-form` - FRIA si aplica

**Documentación:** `docs/compliance/bpmn/aios/external-model-approval/functional.md` y `technical.md`

#### ⚠️ deployment-automation-v1 (PENDIENTE)
**Formularios esperados:**
- `deployment-request-form` - Solicitud de despliegue
- `deployment-approval-form` - Aprobación de despliegue

**Documentación:** `docs/compliance/bpmn/aios/deployment-automation/functional.md` y `technical.md`

#### ⚠️ model-retraining-orchestration-v1 (PENDIENTE)
**Formularios esperados:**
- `retraining-request-form` - Solicitud de re-entrenamiento
- `retraining-validation-form` - Validación de re-entrenamiento

**Documentación:** `docs/compliance/bpmn/aios/model-retraining/functional.md` y `technical.md`

#### ⚠️ model-evaluation-v1 (PENDIENTE)
**Formularios esperados:**
- `model-evaluation-request-form` - Solicitud de evaluación
- `model-evaluation-review-form` - Revisión de evaluación

**Documentación:** `docs/compliance/bpmn/aios/model-evaluation/functional.md` y `technical.md`

#### ⚠️ llm-evaluation-v1 (PENDIENTE)
**Formularios esperados:**
- `llm-evaluation-request-form` - Solicitud de evaluación LLM
- `llm-evaluation-review-form` - Revisión de evaluación

**Documentación:** `docs/compliance/bpmn/aios/llm-evaluation/functional.md` y `technical.md`

#### ⚠️ rag-evaluation-v1 (PENDIENTE)
**Formularios esperados:**
- `rag-evaluation-request-form` - Solicitud de evaluación RAG
- `rag-evaluation-review-form` - Revisión de evaluación

**Documentación:** `docs/compliance/bpmn/aios/rag-evaluation/functional.md` y `technical.md`

#### ⚠️ ai-runtime-health-v1 (PENDIENTE)
**Formularios esperados:**
- `runtime-health-alert-form` - Alerta de salud runtime
- `runtime-health-resolution-form` - Resolución de problemas

**Documentación:** `docs/compliance/bpmn/aios/ai-runtime-health/functional.md` y `technical.md`

#### ⚠️ ai-policy-review-v1 (PENDIENTE)
**Formularios esperados:**
- `policy-review-request-form` - Solicitud de revisión de política
- `policy-remediation-form` - Remedios de política

**Documentación:** `docs/compliance/bpmn/aios/ai-policy-review/functional.md` y `technical.md`

---

### 2. Compliance / EU AI Act (`processes/compliance/`)

#### ⚠️ compliance-monitoring-v1 (PENDIENTE)
**Formularios esperados:**
- `compliance-issue-review-form` - Revisión de issues de compliance
- `compliance-incident-form` - Creación de incidente
- `compliance-resolution-form` - Resolución de issues

**Documentación:** `docs/compliance/bpmn/compliance/compliance-monitoring/functional.md` y `technical.md`

#### ⚠️ conformity-assessment-process (PENDIENTE)
**Formularios esperados:**
- `initiate-conformity-assessment-form` - Iniciar evaluación
- `review-qms-gaps-form` - Revisar gaps QMS
- `complete-documentation-form` - Completar documentación
- `final-review-form` - Revisión final
- `approve-conformity-form` - Aprobar conformidad

**Documentación:** `docs/compliance/bpmn/compliance/conformity-assessment/functional.md` y `technical.md`

#### ⚠️ internal-conformity-assessment-v1 (PENDIENTE)
**Formularios esperados:**
- `internal-conformity-request-form` - Solicitud de evaluación interna
- `internal-conformity-review-form` - Revisión interna

**Documentación:** `docs/compliance/bpmn/compliance/internal-conformity/functional.md` y `technical.md`

#### ⚠️ consent-management-v1 (PENDIENTE)
**Formularios esperados:**
- `consent-registration-form` - Registro de consentimiento
- `consent-validation-form` - Validación de consentimiento
- `consent-revocation-form` - Revocación de consentimiento

**Documentación:** `docs/compliance/bpmn/compliance/consent-management/functional.md` y `technical.md`

#### ⚠️ fria-process (PENDIENTE)
**Formularios esperados:**
- `fria-wizard-form` - Wizard de FRIA
- `fria-analysis-form` - Análisis FRIA
- `fria-ethics-review-form` - Revisión ética FRIA
- `fria-authority-notification-form` - Notificación a autoridad

**Documentación:** `docs/compliance/bpmn/compliance/fria/functional.md` y `technical.md`

#### ⚠️ incident-reporting-process (PENDIENTE)
**Formularios esperados:**
- `incident-report-form` - Reporte de incidente
- `incident-classification-form` - Clasificación de incidente
- `incident-authority-notification-form` - Notificación a autoridad
- `incident-user-notification-form` - Notificación a usuarios
- `incident-resolution-form` - Resolución de incidente

**Documentación:** `docs/compliance/bpmn/compliance/incident-reporting/functional.md` y `technical.md`

#### ⚠️ eu-database-registration-process (PENDIENTE)
**Formularios esperados:**
- `eu-registration-form` - Formulario de registro EU
- `fix-validation-errors-form` - Corregir errores de validación
- `review-registration-package-form` - Revisar paquete de registro
- `manual-resolution-form` - Resolución manual

**Documentación:** `docs/compliance/bpmn/compliance/eu-database-registration/functional.md` y `technical.md`

#### ⚠️ risk-assessment-v1 (PENDIENTE)
**Formularios esperados:**
- `risk-assessment-request-form` - Solicitud de evaluación de riesgo
- `risk-assessment-review-form` - Revisión de evaluación
- `risk-acceptance-form` - Aceptación de riesgo residual

**Documentación:** `docs/compliance/bpmn/compliance/risk-assessment/functional.md` y `technical.md`

#### ⚠️ ethics-review-v1 (PENDIENTE)
**Formularios esperados:**
- `ethics-review-request-form` - Solicitud de revisión ética
- `ethics-review-form` - Formulario de revisión ética

**Documentación:** `docs/compliance/bpmn/compliance/ethics-review/functional.md` y `technical.md`

---

### 3. Audit / ISO (`processes/audit/`)

#### ⚠️ iso42001-management-review-v1 (PENDIENTE)
**Formularios esperados:**
- `schedule-review-meeting-form` - Agendar reunión de revisión
- `management-review-form` - Formulario de revisión de gestión

**Documentación:** `docs/compliance/bpmn/audit/iso42001-management-review/functional.md` y `technical.md`

#### ⚠️ iso42001-internal-audit-v1 (PENDIENTE)
**Formularios esperados:**
- `audit-scheduling-form` - Programación de auditoría
- `audit-execution-form` - Ejecución de auditoría

**Documentación:** `docs/compliance/bpmn/audit/iso42001-internal-audit/functional.md` y `technical.md`

#### ⚠️ iso42001-corrective-action-v1 (PENDIENTE)
**Formularios esperados:**
- `root-cause-analysis-form` - Análisis de causa raíz
- `corrective-action-form` - Acción correctiva
- `effectiveness-verification-form` - Verificación de efectividad

**Documentación:** `docs/compliance/bpmn/audit/iso42001-corrective-action/functional.md` y `technical.md`

#### ⚠️ iso42001-competence-gap-v1 (PENDIENTE)
**Formularios esperados:**
- `competence-gap-identification-form` - Identificación de brecha
- `training-plan-form` - Plan de entrenamiento
- `competence-verification-form` - Verificación de competencia

**Documentación:** `docs/compliance/bpmn/audit/iso42001-competence-gap/functional.md` y `technical.md`

#### ⚠️ iso42001-ai-decommissioning-v1 (PENDIENTE)
**Formularios esperados:**
- `decommission-request-form` - Solicitud de desmantelamiento
- `decommission-approval-form` - Aprobación de desmantelamiento

**Documentación:** `docs/compliance/bpmn/audit/iso42001-ai-decommissioning/functional.md` y `technical.md`

#### ⚠️ iso38507-board-decision-v1 (PENDIENTE)
**Formularios esperados:**
- `board-decision-request-form` - Solicitud de decisión de junta
- `board-decision-review-form` - Revisión de decisión

**Documentación:** `docs/compliance/bpmn/audit/iso38507-board-decision/functional.md` y `technical.md`

---

### 4. Metrics / Monitoring (`processes/metrics/`)

#### ⚠️ bias-detection-v1 (PENDIENTE)
**Formularios esperados:**
- `bias-detection-request-form` - Solicitud de detección de sesgo
- `bias-detection-review-form` - Revisión de detección

**Documentación:** `docs/compliance/bpmn/metrics/bias-detection/functional.md` y `technical.md`

#### ⚠️ drift-detection-v1 (PENDIENTE)
**Formularios esperados:**
- `drift-detection-alert-form` - Alerta de drift
- `drift-resolution-form` - Resolución de drift

**Documentación:** `docs/compliance/bpmn/metrics/drift-detection/functional.md` y `technical.md`

#### ⚠️ performance-degradation-v1 (PENDIENTE)
**Formularios esperados:**
- `performance-degradation-alert-form` - Alerta de degradación
- `performance-resolution-form` - Resolución de degradación

**Documentación:** `docs/compliance/bpmn/metrics/performance-degradation/functional.md` y `technical.md`

#### ⚠️ alert-response-v1 (PENDIENTE)
**Formularios esperados:**
- `alert-classification-form` - Clasificación de alerta
- `alert-resolution-form` - Resolución de alerta

**Documentación:** `docs/compliance/bpmn/metrics/alert-response/functional.md` y `technical.md`

#### ⚠️ incident-response-rca-v1 (PENDIENTE)
**Formularios esperados:**
- `rca-request-form` - Solicitud de RCA
- `rca-review-form` - Revisión de RCA
- `mitigation-approval-form` - Aprobación de mitigaciones

**Documentación:** `docs/compliance/bpmn/metrics/incident-response-rca/functional.md` y `technical.md`

#### ⚠️ dataset-quality-v1 (PENDIENTE)
**Formularios esperados:**
- `dataset-quality-request-form` - Solicitud de validación de dataset
- `dataset-quality-review-form` - Revisión de calidad

**Documentación:** `docs/compliance/bpmn/metrics/dataset-quality/functional.md` y `technical.md`

---

## 🔧 Guía de Migración

### Patrón Establecido

**Referencia:** `docs/prompts/BPMN_FORM_MIGRATION.md` (codeflowx-studio)

**Estructura de archivos:**
```
app/(app)/bpmn/forms/{form-name}/page.tsx
```

**Ejemplo migrado:**
- `app/(app)/bpmn/forms/model-approval-request/page.tsx`
- `app/(app)/bpmn/forms/model-ml-review/page.tsx`
- `app/(app)/bpmn/forms/model-governance-review/page.tsx`
- `app/(app)/bpmn/forms/model-approval-reminder/page.tsx`

### Pasos de Migración

1. **Identificar formulario ZUL original**
   - Buscar en `suinsit.nova.web/src/main/webapp/console/bpmn/` o `plataforma/workflow/`
   - Identificar ViewModel Java asociado

2. **Leer documentación funcional/técnica**
   - Revisar `docs/compliance/bpmn/{categoria}/{proceso}/functional.md`
   - Revisar `docs/compliance/bpmn/{categoria}/{proceso}/technical.md`
   - Identificar variables del workflow y campos del formulario

3. **Crear página Next.js**
   - Seguir patrón de `BPMN_FORM_MIGRATION.md`
   - Mapear campos ZUL a componentes React
   - Implementar validaciones
   - Agregar traducciones i18n

4. **Configurar mapeo formKey → ruta**
   - Agregar entrada en `app/config/modules.ts` (caso "BPMN")
   - Configurar mapeo de `formKey` BPMN a ruta Next.js

5. **Integrar con backend**
   - Implementar carga de datos del workflow
   - Implementar submit de variables al workflow

---

## 📝 PROMPT PARA AGENTE: Continuar Migración de Formularios BPMN

### Contexto

Estás continuando la migración de formularios BPMN de ZKoss (ZUL/Java) a Next.js 14 (React/TypeScript). El proyecto tiene ~80 formularios BPMN que necesitan migración.

### Estado Actual

- ✅ **4 formularios migrados** (model-approval-v1)
- ⚠️ **~76 formularios pendientes**
- 📖 **Documentación completa** disponible en `docs/compliance/bpmn/`

### Objetivo

Migrar formularios BPMN siguiendo el patrón establecido, manteniendo:
- Estructura y funcionalidad del formulario original
- Internacionalización (español/inglés)
- Integración con el layout de la aplicación
- Validaciones y manejo de errores
- Carga de datos del workflow
- Submit de variables al workflow

### Archivos de Referencia

1. **Guía de migración:** `docs/prompts/BPMN_FORM_MIGRATION.md` (codeflowx-studio)
2. **Ejemplos migrados:**
   - `app/(app)/bpmn/forms/model-approval-request/page.tsx`
   - `app/(app)/bpmn/forms/model-ml-review/page.tsx`
   - `app/(app)/bpmn/forms/model-governance-review/page.tsx`
   - `app/(app)/bpmn/forms/model-approval-reminder/page.tsx`

3. **Documentación de procesos:**
   - `docs/compliance/bpmn/{categoria}/{proceso}/functional.md` - Guía funcional
   - `docs/compliance/bpmn/{categoria}/{proceso}/technical.md` - Guía técnica

4. **Configuración:**
   - `app/config/modules.ts` - Menú BPMN
   - `app/config/i18n.ts` - Traducciones

### Proceso de Trabajo

#### Paso 1: Seleccionar Proceso BPMN

Priorizar por:
1. **Prioridad ALTA:** Procesos de compliance regulatorio (FRIA, conformity, incidents, EU registration)
2. **Prioridad MEDIA:** Procesos de aprobación (agent, prompt, adapter, finetuning)
3. **Prioridad BAJA:** Procesos de auditoría ISO

#### Paso 2: Analizar Documentación

Para cada proceso seleccionado:

1. **Leer `functional.md`:**
   - Identificar User Tasks y sus formularios
   - Identificar variables de entrada/salida
   - Identificar actores y roles

2. **Leer `technical.md`:**
   - Identificar ViewModels Java (si existen)
   - Identificar delegates y servicios
   - Identificar entidades JPA relacionadas

3. **Buscar formularios ZUL originales:**
   - Buscar en `suinsit.nova.web/src/main/webapp/console/bpmn/`
   - Buscar en `plataforma/workflow/`
   - Identificar estructura de campos

#### Paso 3: Migrar Formularios

Para cada formulario del proceso:

1. **Crear página Next.js:**
   ```
   app/(app)/bpmn/forms/{form-name}/page.tsx
   ```

2. **Mapear campos:**
   - ZUL `<textbox>` → React `<Input>`
   - ZUL `<datebox>` → React `<Input type="datetime-local">`
   - ZUL `<combobox>` → React `<Select>`
   - ZUL `<textarea>` → React `<textarea>`

3. **Implementar estado:**
   - Cada campo del ViewModel → `useState`
   - Valores por defecto → inicialización de `useState`

4. **Implementar validaciones:**
   - Validaciones del ViewModel → función `validateForm()`
   - Mensajes de error → claves de traducción

5. **Implementar submit:**
   - Mapear variables según `technical.md`
   - Llamar a `/api/bpmn/tasks/${taskId}/complete`
   - Redirigir a `/bpmn/task-inbox` tras éxito

6. **Agregar traducciones:**
   - Agregar en `app/config/i18n.ts` (español e inglés)
   - Usar claves anidadas: `{formKey}.title`, `{formKey}.field1`, etc.

7. **Agregar al menú:**
   - Agregar entrada en `app/config/modules.ts` (caso "BPMN")
   - Configurar icono y roles

#### Paso 4: Integrar con Backend

1. **Carga de datos del workflow:**
   - Implementar `useEffect` para cargar datos del workflow
   - Llamar a `/api/bpmn/tasks/${taskId}` para obtener variables
   - Poblar campos del formulario con datos del workflow

2. **Submit de variables:**
   - Mapear campos del formulario a variables del workflow
   - Incluir `projectId`, `submittedBy`, etc.
   - Enviar a `/api/bpmn/tasks/${taskId}/complete`

#### Paso 5: Verificar y Probar

1. **Verificar linter:** `read_lints` en archivos creados
2. **Verificar navegación:** Probar acceso desde sidebar
3. **Probar validaciones:** Intentar submit sin completar campos
4. **Probar submit:** Completar y enviar (debe redirigir correctamente)
5. **Probar carga de datos:** Verificar que se cargan datos del workflow

### Checklist de Migración

Para cada formulario:

- [ ] Archivo ZUL leído y analizado
- [ ] ViewModel Java leído y analizado (si existe)
- [ ] Documentación funcional/técnica revisada
- [ ] Página Next.js creada en `app/(app)/bpmn/forms/{form-name}/page.tsx`
- [ ] Campos mapeados correctamente
- [ ] Validaciones implementadas
- [ ] Traducciones agregadas (español e inglés)
- [ ] Entrada agregada al menú BPMN en `modules.ts`
- [ ] Icono agregado al sidebar si no existe
- [ ] Carga de datos del workflow implementada
- [ ] Submit de variables al workflow implementado
- [ ] Linter sin errores
- [ ] Probado en el navegador
- [ ] Navegación funciona correctamente
- [ ] Validaciones funcionan correctamente
- [ ] Submit redirige a `/bpmn/task-inbox`

### Prioridades de Migración

#### Fase 1: Compliance Regulatorio (Prioridad ALTA)
1. `fria-process` - 4 formularios
2. `conformity-assessment-process` - 5 formularios
3. `incident-reporting-process` - 5 formularios
4. `eu-database-registration-process` - 4 formularios
5. `consent-management-v1` - 3 formularios

**Total Fase 1:** ~21 formularios

#### Fase 2: Aprobaciones AI-OS (Prioridad MEDIA)
1. `agent-approval-v1` - 4 formularios
2. `prompt-approval-v1` - 3 formularios
3. `adapter-creation-approval-v1` - 3 formularios
4. `finetuning-approval-v1` - 3 formularios
5. `ai-component-onboarding-v1` - 3 formularios
6. `external-model-approval-v1` - 3 formularios

**Total Fase 2:** ~19 formularios

#### Fase 3: Monitoreo y Métricas (Prioridad MEDIA)
1. `compliance-monitoring-v1` - 3 formularios
2. `bias-detection-v1` - 2 formularios
3. `drift-detection-v1` - 2 formularios
4. `performance-degradation-v1` - 2 formularios
5. `alert-response-v1` - 2 formularios
6. `incident-response-rca-v1` - 3 formularios
7. `dataset-quality-v1` - 2 formularios

**Total Fase 3:** ~16 formularios

#### Fase 4: Auditoría ISO (Prioridad BAJA)
1. `iso42001-management-review-v1` - 2 formularios
2. `iso42001-internal-audit-v1` - 2 formularios
3. `iso42001-corrective-action-v1` - 3 formularios
4. `iso42001-competence-gap-v1` - 3 formularios
5. `iso42001-ai-decommissioning-v1` - 2 formularios
6. `iso38507-board-decision-v1` - 2 formularios

**Total Fase 4:** ~14 formularios

#### Fase 5: Otros Procesos (Prioridad BAJA)
1. `risk-assessment-v1` - 3 formularios
2. `ethics-review-v1` - 2 formularios
3. `deployment-automation-v1` - 2 formularios
4. `model-retraining-orchestration-v1` - 2 formularios
5. `model-evaluation-v1` - 2 formularios
6. `llm-evaluation-v1` - 2 formularios
7. `rag-evaluation-v1` - 2 formularios
8. `ai-runtime-health-v1` - 2 formularios
9. `ai-policy-review-v1` - 2 formularios
10. `ai-marketplace-publish-v1` - 3 formularios

**Total Fase 5:** ~22 formularios

### Notas Importantes

1. **Integración con Backend:**
   - La integración real con el backend puede estar pendiente
   - Usar mock data en las API routes si es necesario
   - El endpoint `/api/bpmn/tasks/${taskId}/complete` debe existir

2. **Autenticación:**
   - El usuario actual se obtiene del contexto cuando esté disponible
   - Por ahora usar placeholder `'current-user'`

3. **Navegación:**
   - Usar `globalThis.window.location.href` para navegación directa
   - Más confiable que `router.push` en este contexto

4. **Internacionalización:**
   - Todas las cadenas deben usar `t()` con claves anidadas
   - Formato: `{formKey}.title`, `{formKey}.field1`, `{formKey}.errors.field1Required`

5. **Errores:**
   - Mostrar errores de validación debajo de cada campo
   - Errores de submit con `alert()` (mejorar con toast cuando esté disponible)

6. **Loading States:**
   - Mostrar estado de carga durante submit
   - Deshabilitar botones durante carga

### Recursos Adicionales

- **Catálogo BPMN:** `docs/compliance/bpmn/BPMN_CATALOG.md`
- **Estado de Auditoría:** `docs/compliance/bpmn/BPMN_AUDIT_STATUS.md`
- **Mapeo API:** `docs/compliance/bpmn/BPMN_API_MAPPING.md`
- **Guía de Procesos:** `docs/compliance/bpmn/BPMN_PROCESS_GUIDES.md`

---

**Última actualización:** Enero 2025
**Mantenido por:** Equipo de Desarrollo Frontend
**Contacto:** `frontend@codeflowx.internal`
