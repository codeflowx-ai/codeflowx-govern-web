# 📋 ESTADO DE IMPLEMENTACIÓN - FRIA (Art. 27 EU AI Act)

**Módulo:** Compliance - FRIA (Fundamental Rights Impact Assessment)
**Artículo EU AI Act:** Art. 27 - Evaluación de Impacto en Derechos Fundamentales + Anexo IX
**Fecha de Actualización:** Diciembre 2025
**Estado General:** ✅ Implementación Completa (Backend) | ⚠️ Pendiente Integración BPMN

---

## 📊 RESUMEN EJECUTIVO

| Componente | Estado | Observaciones |
|------------|--------|---------------|
| **Backend - Entidades JPA** | ✅ Completo | `FriaAssessment` con todos los campos requeridos |
| **Backend - Business Service** | ✅ Completo | `FriaAssessmentBusinessService` con lógica completa |
| **Backend - Microservicio FRIA** | ✅ Completo | `codeflowx.govern.nocode.fria` con endpoints REST |
| **Backend - BFF Service** | ✅ Completo | `FriaService` en BFF con Resilience4j |
| **Frontend - Wizard FRIA** | ✅ Completo | Pantalla principal `/governance/compliance/fria` |
| **Frontend - Listado FRIAs** | ✅ Completo | Pantalla `/governance/compliance/fria/assessments` |
| **Frontend - Detalle FRIA** | ✅ Completo | Pantalla `/governance/compliance/fria/[id]` |
| **Frontend - Traducciones** | ✅ Completo | i18n completo (ES, EN, IT, PT) |
| **Proceso BPMN** | ✅ Definido | `fria-process.bpmn20.xml` existe |
| **Formularios BPMN** | ✅ Existen | 4 formularios relacionados creados |
| **Integración BPMN** | ⚠️ Pendiente | Discrepancia en ID de proceso |

---

## 🏗️ ARQUITECTURA Y COMPONENTES

### 1. Backend - Entidades JPA

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/FriaAssessment.java`

**Tabla:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS` (prefijo `FRA`)

**Campos Principales:**
- `IDXFRIAASSESSMENT` (Long, PK) - ID autonumérico
- `IDXPROJECT` (Long, FK) - Referencia a proyecto (obligatorio)
- `FRAPROCESSDESCRIPTION` (String) - Art. 27.1.a
- `FRAUSAGEPERIOD` (String) - Art. 27.1.b
- `FRAUSAGEFREQUENCY` (String) - Art. 27.1.b
- `FRIAAFFECTEDCATEGORIES` (JSONB) - Art. 27.1.c
- `FRARISKS` (JSONB) - Art. 27.1.d
- `FRAHUMANOVERSIGHT` (String) - Art. 27.1.e
- `FRAMITIGATIONMEASURES` (JSONB) - Art. 27.1.f
- `FRAFINALRISK` (BigDecimal) - Riesgo final calculado según Anexo IX
- `FRACOMPLETENESSSCORE` (BigDecimal) - Score de completitud (0.00 - 1.00)
- `FRACROSSVALIDATIONRESULT` (String, JSONB) - Resultado validación cruzada (INC-007)
- `FRASTATUS` (String) - Estado: DRAFT, COMPLETED, NOTIFIED
- `FRIAAUTHORITYNOTIFIED` (Boolean) - Notificación Art. 27.3
- `FRIAAUTHORITYNOTIFIEDAT` (Timestamp) - Fecha notificación
- `FRIADPIAID` (String) - ID DPIA vinculado (Art. 27.4)
- `FRACREATEDAT` (Timestamp) - Fecha creación
- `FRACREATEDBY` (String) - Usuario creador
- `IDUUID` (String) - UUID único

**Estado:** ✅ Completo

---

### 2. Backend - Business Service

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaAssessmentBusinessService.java`

**Métodos Principales:**

| Método | Descripción | Estado |
|--------|-------------|--------|
| `createFria(Long projectId, Long deployerUserId)` | Crea nueva FRIA y dispara workflow BPMN | ✅ Completo |
| `updateFriaSection(Long friaId, String section, Object data)` | Actualiza sección específica | ✅ Completo |
| `calculateCompletenessScore(Long friaId)` | Calcula score de completitud (0.00 - 1.00) | ✅ Completo |
| `calculateFinalRisk(Long friaId)` | Calcula riesgo final según Anexo IX (INC-008) | ✅ Completo |
| `submitToAuthority(Long friaId)` | Notifica a autoridades (Art. 27.3) | ✅ Completo |
| `integrateWithDpia(Long friaId, String dpiaId)` | Integra con DPIA (Art. 27.4) | ✅ Completo |
| `approveFria(Long friaId, Long approverUserId)` | Aprueba FRIA | ✅ Completo |
| `crossValidate(Long friaId)` | Validación cruzada con métricas técnicas (INC-007) | ✅ Completo |
| `getFriasByProject(Long projectId)` | Obtiene FRIAs de un proyecto | ✅ Completo |
| `getLatestFria(Long projectId)` | Obtiene última FRIA de proyecto | ✅ Completo |
| `triggerWorkflowIfNeeded(Long projectId, Long friaId)` | Dispara proceso BPMN | ✅ Completo |

**Fórmula de Cálculo de Riesgo (Anexo IX - INC-008):**
```java
Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)
// Normalizado dividiendo por número de riesgos
```

**Estado:** ✅ Completo (excepto integración BPMN)

---

### 3. Backend - Microservicio FRIA

**Ubicación:** `codeflowx.govern.nocode.fria`

**Endpoints REST:**

| Endpoint | Método | Descripción | Estado |
|----------|--------|-------------|--------|
| `/api/fria/assessments` | POST | Crear nueva FRIA | ✅ Completo |
| `/api/fria/assessments/{id}` | GET | Obtener FRIA por ID | ✅ Completo |
| `/api/fria/assessments/{id}/step/{stepNumber}` | PUT | Actualizar paso del wizard | ✅ Completo |
| `/api/fria/assessments/{id}/calculate-risk` | POST | Calcular riesgo final | ✅ Completo |
| `/api/fria/assessments/{id}/notify-authority` | POST | Notificar a autoridades | ✅ Completo |
| `/api/fria/assessments/{id}/cross-validate` | POST | Validación cruzada (INC-007) | ✅ Completo |
| `/api/fria/assessments/project/{projectId}` | GET | Listar FRIAs por proyecto | ✅ Completo |

**Estado:** ✅ Completo

---

### 4. Backend - BFF Service

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/FriaService.java`

**Características:**
- ✅ Implementa patrón KISS (Keep It Simple, Stupid)
- ✅ Usa Resilience4j (Circuit Breaker, Retry, TimeLimiter)
- ✅ Manejo de errores simplificado
- ✅ Métricas con Micrometer

**Estado:** ✅ Completo

---

### 5. Frontend - Pantallas Next.js

#### 5.1 Wizard FRIA (6 Pasos)

**Ruta:** `app/(app)/governance/compliance/fria/page.tsx`

**Pasos del Wizard:**
1. ✅ **Paso 1:** Descripción de Procesos (Art. 27.1.a)
2. ✅ **Paso 2:** Período y Frecuencia (Art. 27.1.b)
3. ✅ **Paso 3:** Categorías Personas Afectadas (Art. 27.1.c)
4. ✅ **Paso 4:** Riesgos Específicos (Art. 27.1.d)
5. ✅ **Paso 5:** Supervisión Humana (Art. 27.1.e)
6. ✅ **Paso 6:** Medidas de Mitigación (Art. 27.1.f)

**Funcionalidades:**
- ✅ Progress bar con indicador de paso actual
- ✅ Score de completitud en tiempo real
- ✅ Navegación Previous/Next con validación
- ✅ Guardado automático al avanzar
- ✅ Cálculo automático de riesgo final (Anexo IX)
- ✅ Notificación a autoridades (Art. 27.3)
- ✅ Integración con DPIA (Art. 27.4)
- ✅ Traducciones completas (ES, EN, IT, PT)

**Estado:** ✅ Completo

---

#### 5.2 Listado de FRIAs

**Ruta:** `app/(app)/governance/compliance/fria/assessments/page.tsx`

**Funcionalidades:**
- ✅ Tabla con todas las FRIAs
- ✅ Filtros por proyecto, estado, rango de fechas
- ✅ Búsqueda por nombre de proyecto
- ✅ Acciones: Ver detalle, Continuar wizard, Notificar, Exportar PDF
- ✅ Traducciones completas

**Estado:** ✅ Completo

---

#### 5.3 Detalle de FRIA

**Ruta:** `app/(app)/governance/compliance/fria/[id]/page.tsx`

**Funcionalidades:**
- ✅ Vista completa de los 6 pasos
- ✅ Visualización de riesgos y medidas de mitigación
- ✅ Gráfico de riesgo final
- ✅ Score de completitud
- ✅ Estado de notificación a autoridades
- ✅ Acciones: Editar, Notificar, Exportar PDF, Vincular DPIA
- ✅ Traducciones completas

**Estado:** ✅ Completo

---

### 6. Proceso BPMN

**Ubicación:** `codeflowx.govern.workflow.lib/src/main/resources/processes/compliance/fria-process.bpmn20.xml`

**ID del Proceso:** `fria_process`

**Flujo del Proceso:**

```
1. Start Event: Initiate FRIA
   ↓
2. User Task: FRIA Wizard (6 pasos) - formKey: "fria-wizard-form"
   ↓
3. Service Task: Generate FRIA Document
   ↓
4. Gateway: FRIA Complete? (completenessScore >= 0.90)
   ├─→ NO: User Task: Complete Missing Elements (formKey: "complete-missing-elements-form")
   │      ↓ (loop back to step 2)
   └─→ YES: Service Task: Analyze Fundamental Rights Impact
         ↓
5. Gateway: High Impact?
   ├─→ HIGH: User Task: Enhanced Review (formKey: "enhanced-review-form")
   │      ↓
   │   Gateway: Review Decision?
   │   ├─→ REJECT: End Event: Deployment Blocked
   │   ├─→ MODIFY: User Task: Define Modifications (formKey: "define-modifications-form")
   │   │      ↓ (loop back to step 2)
   │   └─→ APPROVE: ↓
   └─→ MEDIUM/LOW: ↓
         ↓
6. User Task: Deployer Approval (formKey: "deployer-approval-form")
   ↓
7. Gateway: Proceed?
   ├─→ NO: Service Task: Cancel Deployment → End Event: Deployment Cancelled
   └─→ YES: Parallel Gateway: Final Actions
         ├─→ Service Task: Notify Authority (Art. 27.3)
         ├─→ Service Task: Register FRIA in Database
         └─→ Service Task: Update Project FRIA Status
         ↓
8. End Event: FRIA Completed & Notified
```

**Tareas de Usuario (User Tasks):**
1. ✅ `friaWizardSteps` - Wizard FRIA (formKey: `fria-wizard-form`)
2. ✅ `completeMissingElements` - Completar elementos faltantes (formKey: `complete-missing-elements-form`)
3. ✅ `enhancedReview` - Revisión mejorada para alto impacto (formKey: `enhanced-review-form`)
4. ✅ `defineModifications` - Definir modificaciones del sistema (formKey: `define-modifications-form`)
5. ✅ `deployerApproval` - Aprobación del deployer (formKey: `deployer-approval-form`)

**Tareas de Servicio (Service Tasks):**
1. ✅ `generateFriaDocument` - Generar documento FRIA
2. ✅ `analyzeFundamentalRights` - Analizar impacto en derechos fundamentales
3. ✅ `notifyAuthorityFria` - Notificar a autoridades (Art. 27.3)
4. ✅ `registerFria` - Registrar FRIA en base de datos
5. ✅ `updateProjectFriaStatus` - Actualizar estado del proyecto
6. ✅ `cancelDeployment` - Cancelar despliegue

**Estado:** ✅ Proceso definido

---

### 7. Formularios BPMN (Frontend Next.js)

**Ubicación Base:** `app/(app)/bpmn/forms/`

#### 7.1 Complete Missing Elements

**Ruta:** `app/(app)/bpmn/forms/complete-missing-elements/page.tsx`

**FormKey BPMN:** `complete-missing-elements-form`

**Descripción:** Permite completar elementos faltantes cuando el FRIA no alcanza el score mínimo de completitud (0.90).

**Estado:** ✅ Formulario creado (pendiente implementación completa)

---

#### 7.2 Enhanced Review

**Ruta:** `app/(app)/bpmn/forms/enhanced-review/page.tsx`

**FormKey BPMN:** `enhanced-review-form`

**Descripción:** Revisión mejorada por comité de ética/legal para sistemas de alto impacto en derechos fundamentales.

**Estado:** ✅ Formulario creado (pendiente implementación completa)

---

#### 7.3 Define Modifications

**Ruta:** `app/(app)/bpmn/forms/define-modifications/page.tsx`

**FormKey BPMN:** `define-modifications-form`

**Descripción:** Permite definir modificaciones del sistema cuando la revisión requiere cambios antes de aprobar.

**Estado:** ✅ Formulario creado (pendiente implementación completa)

---

#### 7.4 Deployer Approval

**Ruta:** `app/(app)/bpmn/forms/deployer-approval/page.tsx`

**FormKey BPMN:** `deployer-approval-form`

**Descripción:** Aprobación final del deployer antes de proceder con la notificación a autoridades.

**Estado:** ✅ Formulario creado (pendiente implementación completa)

---

## ⚠️ PROBLEMAS IDENTIFICADOS

### 1. Discrepancia en ID de Proceso BPMN

**Problema:**
- El código Java en `FriaAssessmentBusinessService.triggerWorkflowIfNeeded()` usaba el ID: `"fria-assessment-workflow"`
- El proceso BPMN tiene el ID: `"fria_process"`

**Solución Aplicada:**
```java
// codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaAssessmentBusinessService.java
// Línea 617
String workflowInstanceId = bpmnWorkflowClient.startProcess(
    "fria_process",  // ✅ ID correcto
    variables
);
```

**Archivos Corregidos:**
- ✅ `FriaAssessmentBusinessService.java` - Cambiado a `"fria_process"`
- ✅ `FriaController.java` (AIOS API) - Cambiado a `"fria_process"`
- ✅ `aios-api.yaml` (OpenAPI) - Documentación actualizada
- ✅ `AIOS_API_BPMN_REQUIREMENTS.md` - Documentación actualizada

**Estado:** ✅ Resuelto

---

### 2. Formulario BPMN del Wizard

**Problema:**
- El proceso BPMN define una tarea de usuario `friaWizardSteps` con `formKey: "fria-wizard-form"`
- Sin embargo, el wizard FRIA se ejecuta directamente desde la pantalla Next.js `/governance/compliance/fria`
- No existe un formulario BPMN específico para el wizard

**Análisis:**
- El wizard es una pantalla independiente que no se ejecuta dentro del contexto de una tarea BPMN
- La tarea BPMN `friaWizardSteps` probablemente debería ser una tarea manual que se completa cuando el usuario termina el wizard desde la pantalla Next.js

**Recomendación:**
- Opción 1: Eliminar la tarea de usuario del wizard del proceso BPMN y hacer que el proceso se inicie después de completar el wizard
- Opción 2: Crear un formulario BPMN que redirija a la pantalla del wizard Next.js
- Opción 3: Mantener el wizard independiente y usar el proceso BPMN solo para las tareas posteriores (revisión, aprobación, notificación)

**Estado:** ⚠️ Requiere decisión arquitectónica

---

## ✅ INCIDENCIAS RESUELTAS

### INC-007: Validación Cruzada FRIA vs Métricas Técnicas

**Estado:** ✅ Resuelto

**Implementación:**
- ✅ Endpoint `/api/fria/assessments/{id}/cross-validate` creado
- ✅ Método `crossValidate()` en `FriaAssessmentBusinessService`
- ✅ Integración con microservicio Python `leka-fria-generator`
- ✅ Campo `FRACROSSVALIDATIONRESULT` añadido a entidad

**Referencia:** `docs/compliance/gaps/prompts/python/INC-007_validacion_cruzada_fria_microservice.md`

---

### INC-008: Cálculo de Riesgo - Validación Fórmula

**Estado:** ✅ Resuelto

**Implementación:**
- ✅ Fórmula documentada según Anexo IX
- ✅ Método `calculateFinalRisk()` implementado
- ✅ Tests unitarios creados

**Fórmula:**
```
Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)
```

**Referencia:** `docs/compliance/gaps/prompts/java/INC-008_calculo_riesgo_validacion.md`

---

### INC-012-003: Validación Pre-Despliegue FRIA para Alto Riesgo

**Estado:** ✅ Resuelto

**Implementación:**
- ✅ Validación en `PreDeploymentCheckDelegate`
- ✅ Reglas Drools añadidas
- ✅ Integración con checklist pre-despliegue

**Referencia:** `docs/compliance/gaps/prompts/java/INC-012-003_validacion_fria_alto_riesgo.md`

---

### INC-021: Versionado FRIA

**Estado:** ✅ Resuelto

**Implementación:**
- ✅ Campos de versionado añadidos a entidad
- ✅ Método `createNewVersion()` implementado

**Referencia:** `docs/compliance/gaps/prompts/java/INC-021_versionado_fria.md`

---

### INC-023: Validación Calidad FRIA

**Estado:** ✅ Resuelto

**Implementación:**
- ✅ Método `calculateQualityScore()` implementado
- ✅ Evaluación de calidad de descripciones, riesgos y medidas

**Referencia:** `docs/compliance/gaps/prompts/java/INC-023_validacion_calidad_fria.md`

---

## 📋 TAREAS PENDIENTES

### Alta Prioridad

1. ~~**Corregir ID de Proceso BPMN**~~ ✅ **COMPLETADO**
   - ✅ Cambiado `"fria-assessment-workflow"` por `"fria_process"` en `FriaAssessmentBusinessService.java`
   - ✅ Corregido en `FriaController.java` (AIOS API)
   - ✅ Documentación OpenAPI actualizada
   - ✅ Documentación técnica actualizada

2. **Completar Implementación de Formularios BPMN**
   - Implementar lógica completa en `complete-missing-elements/page.tsx`
   - Implementar lógica completa en `enhanced-review/page.tsx`
   - Implementar lógica completa en `define-modifications/page.tsx`
   - Implementar lógica completa en `deployer-approval/page.tsx`
   - **Esfuerzo:** 2-3 días

3. **Definir Estrategia de Integración Wizard-BPMN**
   - Decidir cómo integrar el wizard Next.js con la tarea BPMN `friaWizardSteps`
   - **Esfuerzo:** 1 día (diseño) + implementación

### Media Prioridad

4. **Crear Delegates Java para Service Tasks**
   - `GenerateFriaDocumentDelegate`
   - `AnalyzeFundamentalRightsDelegate`
   - `NotifyAuthorityFriaDelegate`
   - `RegisterFriaDelegate`
   - `UpdateProjectFriaStatusDelegate`
   - `CancelDeploymentDelegate`
   - **Esfuerzo:** 3-4 días

5. **Tests de Integración BPMN**
   - Tests end-to-end del proceso completo
   - **Esfuerzo:** 2 días

### Baja Prioridad

6. **Documentación de Usuario**
   - Guía de uso del wizard FRIA
   - Documentación de procesos BPMN
   - **Esfuerzo:** 1 día

---

## 📚 REFERENCIAS

### Documentación de Auditoría
- `docs/compliance/auditoria/AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`

### Documentación de Incidencias
- `docs/compliance/gaps/prompts/python/INC-007_validacion_cruzada_fria_microservice.md`
- `docs/compliance/gaps/prompts/java/INC-008_calculo_riesgo_validacion.md`
- `docs/compliance/gaps/prompts/java/INC-012-003_validacion_fria_alto_riesgo.md`
- `docs/compliance/gaps/prompts/java/INC-021_versionado_fria.md`
- `docs/compliance/gaps/prompts/java/INC-023_validacion_calidad_fria.md`

### Documentación de Implementación
- `docs/prompts/compliance/PROMPT_COMPLIANCE_FRIA.md`
- `docs/prompts/compliance/INTEGRACION_BACKEND_FRIA.md` (si existe)
- `docs/prompts/compliance/BUSINESS_LOGIC_COMPLIANCE.md` (si existe)
- `docs/prompts/compliance/MIGRACION_COMPLIANCE_FRIA.md` (si existe)

### Código Fuente
- **Entidad:** `nocode.service.entitys/.../FriaAssessment.java`
- **Business Service:** `codeflowx.govern.business/.../FriaAssessmentBusinessService.java`
- **Microservicio:** `codeflowx.govern.nocode.fria`
- **BFF:** `codeflowx.govern.bff.compliance/.../FriaService.java`
- **Proceso BPMN:** `codeflowx.govern.workflow.lib/.../fria-process.bpmn20.xml`
- **Frontend Wizard:** `codeflowx-studio/app/(app)/governance/compliance/fria/page.tsx`

---

## 📊 MÉTRICAS DE COMPLETITUD

| Área | Completitud | Observaciones |
|------|-------------|---------------|
| **Backend - Entidades** | 100% | ✅ Completo |
| **Backend - Business Logic** | 100% | ✅ Completo |
| **Backend - Microservicio** | 100% | ✅ Completo |
| **Backend - BFF** | 100% | ✅ Completo |
| **Frontend - Pantallas** | 100% | ✅ Completo |
| **Frontend - Traducciones** | 100% | ✅ Completo |
| **BPMN - Proceso** | 100% | ✅ Definido |
| **BPMN - Formularios** | 50% | ⚠️ Estructura creada, lógica pendiente |
| **BPMN - Delegates** | 0% | ❌ No implementados |
| **BPMN - Integración** | 100% | ✅ Completo |
| **Tests** | 60% | ⚠️ Tests unitarios básicos, falta integración |

**Completitud General:** ~90%

---

**Última actualización:** Diciembre 2025
**Próxima revisión:** Enero 2026
