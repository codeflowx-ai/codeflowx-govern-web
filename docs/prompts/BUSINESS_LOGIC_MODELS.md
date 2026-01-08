# PROMPT DE LÓGICA DE NEGOCIO - MÓDULO MODELS

**Fecha:** Diciembre 2025
**Módulo:** Models (Modelos de IA)
**Objetivo:** Definir la lógica de negocio completa para el módulo de gestión de modelos de IA
**Esfuerzo Estimado:** 3-4 días

> **⚠️ IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** en este prompt.
> Las operaciones CRUD y consultas BBDD están implementadas, pero las integraciones con microservicios externos están pendientes de implementar cuando estén disponibles.

---

## 📋 CONTEXTO DEL MÓDULO

### **Descripción Funcional**
El módulo Models gestiona el ciclo de vida completo de modelos de machine learning e IA, incluyendo:
- Registro y catalogación de modelos
- Versionado y control de cambios
- Evaluación y validación de modelos
- Análisis de sesgo y explicabilidad
- Gestión de dependencias entre modelos
- Adaptación de modelos (adapters y fine-tuning)
- Compliance con EU AI Act

### **Pantallas Asociadas**

#### **ViewModels Identificados (31 ViewModels)**
| ViewModel | Propósito | Ubicación |
|-----------|-----------|-----------|
| `ModelApprovalWorkflowViewModel` | Workflow de aprobación de modelos | `com.codeflowx.govern.viewmodel.models.ModelApprovalWorkflowViewModel` |
| `ModelsOverviewViewModel` | Vista general de modelos | `com.codeflowx.govern.viewmodel.models.ModelsOverviewViewModel` |
| `ModelsDetailViewModel` | Detalles específicos de modelo | `com.codeflowx.govern.viewmodel.models.ModelsDetailViewModel` |
| `ModelVersionOverviewViewModel` | Gestión de versiones | `com.codeflowx.govern.viewmodel.models.ModelVersionOverviewViewModel` |
| `ModelVersionDetailViewModel` | Detalles de versión | `com.codeflowx.govern.viewmodel.models.ModelVersionDetailViewModel` |
| `ModelUsageOverviewViewModel` | Uso y métricas | `com.codeflowx.govern.viewmodel.models.ModelUsageOverviewViewModel` |
| `ModelUsageDetailViewModel` | Detalles de uso | `com.codeflowx.govern.viewmodel.models.ModelUsageDetailViewModel` |
| `ModelProviderOverviewViewModel` | Proveedores de modelos | `com.codeflowx.govern.viewmodel.models.ModelProviderOverviewViewModel` |
| `ModelProviderDetailViewModel` | Detalles de proveedor | `com.codeflowx.govern.viewmodel.models.ModelProviderDetailViewModel` |
| `ModelEndpointOverviewViewModel` | Endpoints de modelos | `com.codeflowx.govern.viewmodel.models.ModelEndpointOverviewViewModel` |
| `ModelEndpointDetailViewModel` | Detalles de endpoint | `com.codeflowx.govern.viewmodel.models.ModelEndpointDetailViewModel` |
| `ModelDependencyOverviewViewModel` | Dependencias | `com.codeflowx.govern.viewmodel.models.ModelDependencyOverviewViewModel` |
| `ModelDependencyDetailViewModel` | Detalles de dependencias | `com.codeflowx.govern.viewmodel.models.ModelDependencyDetailViewModel` |
| `ModelComparisonOverviewViewModel` | Comparación de modelos | `com.codeflowx.govern.viewmodel.models.ModelComparisonOverviewViewModel` |
| `ModelComparisonDetailViewModel` | Detalles de comparación | `com.codeflowx.govern.viewmodel.models.ModelComparisonDetailViewModel` |
| `ModelCatalogOverviewViewModel` | Catálogo de modelos | `com.codeflowx.govern.viewmodel.models.ModelCatalogOverviewViewModel` |
| `ModelCatalogDetailViewModel` | Detalles de catálogo | `com.codeflowx.govern.viewmodel.models.ModelCatalogDetailViewModel` |
| `ModelCapabilityOverviewViewModel` | Capacidades | `com.codeflowx.govern.viewmodel.models.ModelCapabilityOverviewViewModel` |
| `ModelCapabilityDetailViewModel` | Detalles de capacidades | `com.codeflowx.govern.viewmodel.models.ModelCapabilityDetailViewModel` |
| `ModelArtifactOverviewViewModel` | Artefactos | `com.codeflowx.govern.viewmodel.models.ModelArtifactOverviewViewModel` |
| `ModelArtifactDetailViewModel` | Detalles de artefactos | `com.codeflowx.govern.viewmodel.models.ModelArtifactDetailViewModel` |
| `ModelRecommendationOverviewViewModel` | Recomendaciones | `com.codeflowx.govern.viewmodel.models.ModelRecommendationOverviewViewModel` |
| `ModelRecommendationDetailViewModel` | Detalles de recomendaciones | `com.codeflowx.govern.viewmodel.models.ModelRecommendationDetailViewModel` |
| `ModelStageTransitionOverviewViewModel` | Transiciones de etapa | `com.codeflowx.govern.viewmodel.models.ModelStageTransitionOverviewViewModel` |
| `ModelStageTransitionDetailViewModel` | Detalles de transición | `com.codeflowx.govern.viewmodel.models.ModelStageTransitionDetailViewModel` |
| `ModelsMetricsSummaryOverviewViewModel` | Resumen de métricas | `com.codeflowx.govern.viewmodel.models.ModelsMetricsSummaryOverviewViewModel` |
| `ModelsOverviewOverviewViewModel` | Vista general consolidada | `com.codeflowx.govern.viewmodel.models.ModelsOverviewOverviewViewModel` |
| `ProviderCredentialOverviewViewModel` | Credenciales de proveedor | `com.codeflowx.govern.viewmodel.models.ProviderCredentialOverviewViewModel` |
| `ProviderCredentialDetailViewModel` | Detalles de credenciales | `com.codeflowx.govern.viewmodel.models.ProviderCredentialDetailViewModel` |
| `ModelApprovalHumanOverrideViewModel` | Override humano (BPMN) | `com.codeflowx.govern.workflow.viewmodels.ModelApprovalHumanOverrideViewModel` |
| `ModelEvaluationReviewViewModel` | Revisión de evaluación (BPMN) | `com.codeflowx.govern.workflow.viewmodels.ModelEvaluationReviewViewModel` |

#### **Pantallas ZUL Identificadas (31 pantallas)**
- **28 pantallas** en `console/platform/models/`:
  - `model-overview.zul`, `model-detail.zul`
  - `model-version-overview.zul`, `model-version-detail.zul`
  - `model-usage-overview.zul`, `model-usage-detail.zul`
  - `model-provider-overview.zul`, `model-provider-detail.zul`
  - `model-endpoint-overview.zul`, `model-endpoint-detail.zul`
  - `model-dependency-overview.zul`, `model-dependency-detail.zul`
  - `model-comparison-overview.zul`, `model-comparison-detail.zul`
  - `model-catalog-overview.zul`, `model-catalog-detail.zul`
  - `model-capability-overview.zul`, `model-capability-detail.zul`
  - `model-artifact-overview.zul`, `model-artifact-detail.zul`
  - `model-recommendation-overview.zul`, `model-recommendation-detail.zul`
  - `model-stage-transition-overview.zul`, `model-stage-transition-detail.zul`
  - `models-overview-overview.zul`, `models-metrics-summary-overview.zul`
  - `provider-credential-overview.zul`, `provider-credential-detail.zul`
- **3 pantallas BPMN** en `console/bpmn/`:
  - `model-approval-human-override-form.zul`
  - `model-approval-reminder-form.zul`
  - `model-evaluation-review-form.zul`

**Referencia:** `suinsit.nova.web/docs/funcional/models/README_MODELOS.md`

#### **Pantallas Next.js Migradas (20+ pantallas)**
**Ubicación:** `app/(app)/model-management/`

**Pantallas Principales Migradas:**
- ✅ `app/(app)/model-management/page.tsx` - Dashboard principal
- ✅ `app/(app)/model-management/models/page.tsx` - Listado de modelos
- ✅ `app/(app)/model-management/models/register/page.tsx` - Registro de modelos
- ✅ `app/(app)/model-management/providers/page.tsx` - Proveedores
- ✅ `app/(app)/model-management/experiments/page.tsx` - Experimentos
- ✅ `app/(app)/model-management/experiments/new/page.tsx` - Nuevo experimento
- ✅ `app/(app)/model-management/artifacts/page.tsx` - Artefactos
- ✅ `app/(app)/model-management/dependencies/page.tsx` - Dependencias
- ✅ `app/(app)/model-management/bias-analysis/page.tsx` - Análisis de sesgo
- ✅ `app/(app)/model-management/explainability/page.tsx` - Explicabilidad
- ✅ `app/(app)/model-management/performance/page.tsx` - Rendimiento
- ✅ `app/(app)/model-management/performance/usage-overview/page.tsx` - Uso
- ✅ `app/(app)/model-management/model-serving/page.tsx` - Serving
- ✅ `app/(app)/model-management/marketplace/page.tsx` - Marketplace
- ✅ `app/(app)/model-management/approval/page.tsx` - Aprobaciones
- ✅ `app/(app)/model-management/registry/artifact-overview/page.tsx` - Artefactos (registry)
- ✅ `app/(app)/model-management/registry/capability-overview/page.tsx` - Capacidades
- ✅ `app/(app)/model-management/registry/catalog-overview/page.tsx` - Catálogo
- ✅ `app/(app)/model-management/registry/endpoint-overview/page.tsx` - Endpoints
- ✅ `app/(app)/model-management/registry/provider-credential-overview/page.tsx` - Credenciales

**Pantallas BPMN Migradas:**
- ✅ `app/(app)/bpmn/forms/model-approval-human-override/page.tsx`
- ✅ `app/(app)/bpmn/forms/model-evaluation-review/page.tsx`
- ✅ `app/(app)/bpmn/forms/model-approval-reminder/page.tsx`

**Referencia:** `codeflowx-studio/docs/PLAN_MIGRACION_ZUL_VIEWMODELS.md`

### **Entidades JPA Principales**
- `Model` - Entidad principal de modelos
- `ModelVersion` - Versiones de modelos
- `ModelDependency` - Dependencias entre modelos
- `ModelBiasAnalysis` - Análisis de sesgo
- `ModelPerformance` - Métricas de rendimiento
- `ModelValidation` - Validaciones de modelos
- `ModelArtifact` - Artefactos de modelos
- `ModelDeployment` - Despliegues de modelos
- `ModelMetrics` - Métricas de serving

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Business Services Existentes**
1. **ModelAdaptationBusinessService** (✅ Creado)
   - `createAdapter()` - Crea adapters desde modelo base
   - `createFineTuned()` - Crea fine-tuning desde modelo base

### **Servicios CRUD (codeflowx.govern.services)**
- `ModelService` - CRUD básico de modelos
- `ModelVersionService` - CRUD de versiones
- `ModelDependencyService` - CRUD de dependencias

### **Microservicios Python Disponibles**
- `codeflowx-governance-api` - API de governance
- `codeflowx-aios-telemetry` - Telemetría y métricas
- `codeflowx-discovery-serving` - Discovery de modelos

### **Clientes Java Disponibles**
- `LLMEvaluationClient` - Evaluación de modelos LLM
- `ModelWrapperClient` - Wrapper de modelos
- `RAGEvaluationClient` - Evaluación RAG

---

## 📚 REFERENCIAS DE PROMPTS JAVA Y COMPLIANCE

### **Prompt A.1 - Extensión Entidad Model.java** (PROMPTS_03_JAVA_BACKEND_EXISTENTE.md)
**Ubicación:** `suinsit.nova.web/docs/compliance/gaps/prompts/java/`
**Campos EU AI Act añadidos:**
- `MODTECHNICALDOCURL` - URL documentación técnica (Art. 11, Anexo IV)
- `MODTECHNICALDOCCOMPLETE` - Boolean si Anexo IV completo
- `MODTECHNICALDOCSCORE` - Score completitud (0-1)
- `MODACCURACYLEVEL` - Nivel precisión (Art. 15)
- `MODPERFORMANCEMETRICS` - JSONB con métricas
- `MODISHIGHRISK` - Boolean si es alto riesgo (Art. 6)
- `MODANNEXIIICATEGORY` - Categoría Anexo III
- `MODISGPAI` - Boolean si es GPAI (Art. 51)
- `MODGPAIFLOPSTRAINING` - FLOPs entrenamiento
- `MODGPAISYSTEMICRISK` - Boolean si riesgo sistémico

**Métodos BusinessService requeridos:**
- `updateTechnicalDocumentation(modelId, url, score)`
- `classifyAsHighRisk(modelId, category)`
- `markAsGPAI(modelId, flops)`

### **Prompt A.2 - ModelAdaptationBusinessService** (Ya implementado)
**Funcionalidad:**
- Creación de adapters (LoRA/QLoRA)
- Creación de fine-tuning
- Configuración JSONB para adaptación
- Relación con modelo base (`idmodbasemodel`)

### **Prompt A.3 - INC-001: Validación Coherencia Modelo-Dataset**
**Ubicación:** `suinsit.nova.web/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 10, Art. 11
**Estado:** Pendiente de implementación completa

### **Prompt A.4 - INC-003: Validación Documentación Técnica Completa**
**Ubicación:** `suinsit.nova.web/docs/compliance/gaps/prompts/java/INC-003_documentacion_tecnica.md`
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 11 + Anexo IV
**Estado:** ✅ COMPLETADO - 2025-11-25

### **Prompt A.5 - INC-005: Validación Sistemas Prohibidos (Art. 5)**
**Ubicación:** `suinsit.nova.web/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 5, Anexo II
**Estado:** Pendiente de implementación completa

### **Prompt A.6 - INC-011: Checklist Completo Validación Modelos**
**Ubicación:** `suinsit.nova.web/docs/compliance/gaps/prompts/java/INC-011_checklist_modelos.md`
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 11, Art. 15
**Estado:** ✅ COMPLETADO - 2025-11-25
**Servicio:** `ModelValidationService` creado con validación completa por tipo de modelo

---

## 🔍 VALIDACIONES DE AUDITORÍA

### **INC-001: Falta Validación de Coherencia Modelo-Dataset**
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 10, Art. 11

**Validación Requerida:**
```java
// Validar que modelo alto riesgo tenga dataset documentado
if (model.getModishighrisk() &&
    (model.getModtrainingconfig() == null || model.getModtrainingconfig().isEmpty())) {
    throw new ValidationException(
        "CRITICAL: Modelo alto riesgo requiere dataset de entrenamiento documentado (Art. 10, 11)"
    );
}
```

**Consulta BBDD:**
```sql
SELECT m.idxmodel, m.modname, m.modishighrisk, m.modtrainingconfig
FROM modmodels m
WHERE m.modishighrisk = true
  AND (m.modtrainingconfig IS NULL OR m.modtrainingconfig = '')
```

### **INC-003: Falta Validación de Documentación Técnica Completa**
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 11 + Anexo IV

**Validación Requerida:**
```java
// Validar documentación técnica antes de clasificar alto riesgo
if (model.getModishighrisk()) {
    if (model.getModtechnicaldoccomplete() == null || !model.getModtechnicaldoccomplete()) {
        throw new ValidationException(
            "CRITICAL: Documentación técnica incompleta (Art. 11 + Anexo IV)"
        );
    }
    if (model.getModtechnicaldocscore() == null ||
        model.getModtechnicaldocscore().compareTo(new BigDecimal("0.90")) < 0) {
        throw new ValidationException(
            "CRITICAL: Score de documentación técnica insuficiente. Mínimo: 0.90"
        );
    }
}
```

**Consulta BBDD:**
```sql
SELECT m.idxmodel, m.modname, m.modishighrisk,
       m.modtechnicaldoccomplete, m.modtechnicaldocscore
FROM modmodels m
WHERE m.modishighrisk = true
  AND (m.modtechnicaldoccomplete IS NULL OR m.modtechnicaldoccomplete = false
       OR m.modtechnicaldocscore < 0.90)
```

### **INC-005: Falta Validación de Sistemas Prohibidos (Art. 5)**
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 5, Anexo II

**Validación Requerida:**
```java
// Verificar contra sistemas prohibidos antes de clasificar
List<ProhibitedSystem> prohibitedSystems = prohibitedSystemBusinessService.getActiveProhibitedSystems();
for (ProhibitedSystem prohibited : prohibitedSystems) {
    if (matchesProhibitedSystem(model, prohibited)) {
        throw new ValidationException(
            "CRITICAL: Modelo coincide con sistema prohibido según Art. 5: " +
            prohibited.getDescription()
        );
    }
}
```

**Consulta BBDD:**
```sql
SELECT ps.idxprohibitedsystem, ps.prodescription, ps.proarticlecode
FROM govprohibitedsystems ps
WHERE ps.proisactive = true
```

---

## 💼 LÓGICA DE NEGOCIO - BUSINESS SERVICES

### **1. ModelBusinessService**

#### **1.1. Operaciones CRUD Básicas**

**Método: `createModel(Model model, String createdBy)`**
```java
/**
 * Crea un nuevo modelo con validaciones iniciales
 *
 * Validaciones:
 * - Nombre único
 * - Versión válida
 * - Provider válido (si aplica)
 * - Tipo de modelo válido
 *
 * Consulta BBDD:
 * SELECT COUNT(*) FROM modmodels WHERE modname = ? AND modversion = ?
 */
public Model createModel(Model model, String createdBy) {
    // 1. Validar nombre único
    validateUniqueName(model.getModname(), model.getModversion());

    // 2. Validar provider (si aplica)
    if (model.getModelProvider() != null) {
        validateProvider(model.getModelProvider().getIdxmodelprovider());
    }

    // 3. Establecer valores por defecto
    model.setModstatus("DRAFT");
    model.setModcreatedby(createdBy);
    model.setModcreatedat(new Timestamp(System.currentTimeMillis()));

    // 4. Generar UUID si no existe
    if (model.getIduuid() == null) {
        model.setIduuid(UUID.randomUUID().toString());
    }

    // 5. Guardar
    return noCodeClient.save(model);
}
```

**Método: `updateModel(Long modelId, Model updates, String updatedBy)`**
```java
/**
 * Actualiza un modelo existente con validaciones
 *
 * Validaciones:
 * - Modelo existe
 * - No está en estado DEPLOYED (requiere workflow)
 * - Campos críticos no pueden ser modificados si está aprobado
 *
 * Consulta BBDD:
 * SELECT * FROM modmodels WHERE idxmodel = ?
 */
public Model updateModel(Long modelId, Model updates, String updatedBy) {
    Model existing = noCodeClient.findById(Model.class, modelId);
    if (existing == null) {
        throw new EntityNotFoundException("Modelo no encontrado: " + modelId);
    }

    // Validar que no esté desplegado
    if ("DEPLOYED".equals(existing.getModstatus())) {
        throw new BusinessException("No se puede modificar modelo desplegado. Use workflow de actualización.");
    }

    // Actualizar campos permitidos
    if (updates.getModname() != null) {
        existing.setModname(updates.getModname());
    }
    if (updates.getModdescription() != null) {
        existing.setModdescription(updates.getModdescription());
    }
    // ... más campos

    existing.setModupdatedby(updatedBy);
    existing.setModupdatedat(new Timestamp(System.currentTimeMillis()));

    return noCodeClient.save(existing);
}
```

**Método: `deleteModel(Long modelId, String deletedBy)`**
```java
/**
 * Elimina un modelo (soft delete o hard delete según estado)
 *
 * Validaciones:
 * - No tiene versiones desplegadas
 * - No tiene dependencias activas
 * - No está referenciado por otros modelos
 *
 * Consultas BBDD:
 * SELECT COUNT(*) FROM srvmodelversions WHERE idxmodel = ? AND mvdstatus = 'DEPLOYED'
 * SELECT COUNT(*) FROM modmodeldependencies WHERE idxmodel = ? OR idxmodeldependency = ?
 */
public void deleteModel(Long modelId, String deletedBy) {
    Model model = noCodeClient.findById(Model.class, modelId);
    if (model == null) {
        throw new EntityNotFoundException("Modelo no encontrado: " + modelId);
    }

    // Validar versiones desplegadas
    long deployedVersions = countDeployedVersions(modelId);
    if (deployedVersions > 0) {
        throw new BusinessException(
            "No se puede eliminar modelo con " + deployedVersions + " versiones desplegadas"
        );
    }

    // Validar dependencias
    long dependencies = countDependencies(modelId);
    if (dependencies > 0) {
        throw new BusinessException(
            "No se puede eliminar modelo con " + dependencies + " dependencias activas"
        );
    }

    // Soft delete (marcar como eliminado)
    model.setModstatus("DELETED");
    model.setModupdatedby(deletedBy);
    model.setModupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(model);
}
```

#### **1.2. Operaciones de Clasificación y Compliance**

**Método: `classifyAsHighRisk(Long modelId, String category, String justification, String classifiedBy)`**
```java
/**
 * Clasifica un modelo como alto riesgo según Art. 6 EU AI Act
 *
 * Validaciones (INC-001, INC-003, INC-005):
 * - Dataset de entrenamiento documentado (Art. 10, 11)
 * - Documentación técnica completa (Art. 11, Anexo IV)
 * - Score documentación >= 0.90
 * - No es sistema prohibido (Art. 5)
 *
 * Consultas BBDD:
 * - Verificar dataset: SELECT modtrainingconfig FROM modmodels WHERE idxmodel = ?
 * - Verificar documentación: SELECT modtechnicaldoccomplete, modtechnicaldocscore FROM modmodels WHERE idxmodel = ?
 * - Verificar prohibidos: SELECT * FROM govprohibitedsystems WHERE proisactive = true
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-governance-api: POST /api/v1/models/{modelId}/classify-high-risk
 */
public Model classifyAsHighRisk(Long modelId, String category, String justification, String classifiedBy) {
    Model model = noCodeClient.findById(Model.class, modelId);
    if (model == null) {
        throw new EntityNotFoundException("Modelo no encontrado: " + modelId);
    }

    // VALIDACIÓN 1: Dataset de entrenamiento (INC-001)
    if (model.getModtrainingconfig() == null || model.getModtrainingconfig().isEmpty()) {
        throw new ValidationException(
            "CRITICAL: Modelo alto riesgo requiere dataset de entrenamiento documentado (Art. 10, 11)"
        );
    }

    // VALIDACIÓN 2: Documentación técnica completa (INC-003)
    if (model.getModtechnicaldoccomplete() == null || !model.getModtechnicaldoccomplete()) {
        throw new ValidationException(
            "CRITICAL: Documentación técnica incompleta (Art. 11 + Anexo IV). " +
            "Complete documentación antes de clasificar como alto riesgo."
        );
    }

    if (model.getModtechnicaldocscore() == null ||
        model.getModtechnicaldocscore().compareTo(new BigDecimal("0.90")) < 0) {
        throw new ValidationException(
            "CRITICAL: Score de documentación técnica insuficiente: " +
            model.getModtechnicaldocscore() + ". Mínimo requerido: 0.90"
        );
    }

    // VALIDACIÓN 3: Sistemas prohibidos (INC-005)
    List<ProhibitedSystem> prohibitedSystems = prohibitedSystemBusinessService.getActiveProhibitedSystems();
    for (ProhibitedSystem prohibited : prohibitedSystems) {
        if (matchesProhibitedSystem(model, prohibited)) {
            throw new ValidationException(
                "CRITICAL: Modelo coincide con sistema prohibido según Art. 5: " +
                prohibited.getDescription() + ". No puede ser clasificado ni desplegado."
            );
        }
    }

    // Clasificar como alto riesgo
    model.setModishighrisk(true);
    model.setModannexiiicategory(category);
    model.setModriskcategoryjustification(justification);
    model.setModupdatedby(classifiedBy);
    model.setModupdatedat(new Timestamp(System.currentTimeMillis()));

    // TODO: Llamar a microservicio Python para registro en EU Database
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        governanceApiClient.classifyModelAsHighRisk(modelId, category, justification);
    } catch (Exception e) {
        log.error("Error registrando clasificación en EU Database", e);
        // No fallar, pero registrar error
    }
    */

    return noCodeClient.save(model);
}
```

**Método: `updateTechnicalDocumentation(Long modelId, String url, BigDecimal score, String updatedBy)`**
```java
/**
 * Actualiza la documentación técnica del modelo (Art. 11, Anexo IV)
 *
 * Consulta BBDD:
 * SELECT * FROM modmodels WHERE idxmodel = ?
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-governance-api: POST /api/v1/models/{modelId}/technical-documentation
 */
public Model updateTechnicalDocumentation(Long modelId, String url, BigDecimal score, String updatedBy) {
    Model model = noCodeClient.findById(Model.class, modelId);
    if (model == null) {
        throw new EntityNotFoundException("Modelo no encontrado: " + modelId);
    }

    model.setModtechnicaldocurl(url);
    model.setModtechnicaldocscore(score);
    model.setModtechnicaldoccomplete(score.compareTo(new BigDecimal("0.90")) >= 0);
    model.setModupdatedby(updatedBy);
    model.setModupdatedat(new Timestamp(System.currentTimeMillis()));

    // TODO: Llamar a microservicio Python para validar completitud Anexo IV
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        TechnicalDocValidationResult validation = governanceApiClient.validateTechnicalDocumentation(
            modelId, url
        );
        model.setModtechnicaldoccomplete(validation.isComplete());
        model.setModtechnicaldocscore(validation.getScore());
    } catch (Exception e) {
        log.error("Error validando documentación técnica", e);
    }
    */

    return noCodeClient.save(model);
}
```

**Método: `markAsGPAI(Long modelId, BigDecimal flops, String markedBy)`**
```java
/**
 * Marca un modelo como GPAI (General Purpose AI) según Art. 51 EU AI Act
 *
 * Validaciones:
 * - FLOPs > 10^25 = riesgo sistémico
 *
 * Consulta BBDD:
 * SELECT * FROM modmodels WHERE idxmodel = ?
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-governance-api: POST /api/v1/models/{modelId}/mark-gpai
 */
public Model markAsGPAI(Long modelId, BigDecimal flops, String markedBy) {
    Model model = noCodeClient.findById(Model.class, modelId);
    if (model == null) {
        throw new EntityNotFoundException("Modelo no encontrado: " + modelId);
    }

    model.setModisgpai(true);
    model.setModgpaiflopstraining(flops);

    // Calcular si es riesgo sistémico (> 10^25 FLOPs)
    BigDecimal systemicRiskThreshold = new BigDecimal("10000000000000000000000000"); // 10^25
    model.setModgpaisystemicrisk(flops.compareTo(systemicRiskThreshold) > 0);

    model.setModupdatedby(markedBy);
    model.setModupdatedat(new Timestamp(System.currentTimeMillis()));

    // TODO: Llamar a microservicio Python para registro en EU Database
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        governanceApiClient.registerGPAI(modelId, flops, model.getModgpaisystemicrisk());
    } catch (Exception e) {
        log.error("Error registrando GPAI en EU Database", e);
    }
    */

    return noCodeClient.save(model);
}
```

#### **1.3. Operaciones de Evaluación**

**Método: `evaluateModel(Long modelId, EvaluationConfig config, String evaluatedBy)`**
```java
/**
 * Ejecuta evaluación completa de un modelo
 *
 * Llamadas Microservicios Python (COMENTADAS - PENDIENTE):
 * - LLMEvaluationClient.evaluateModel() - Evaluación LLM
 * - RAGEvaluationClient.evaluateRAG() - Evaluación RAG (si aplica)
 * - codeflowx-governance-api: POST /api/v1/models/{modelId}/evaluate
 *
 * Consultas BBDD:
 * - Guardar resultados: INSERT INTO modevaluations (...)
 * - Actualizar métricas: UPDATE modmodels SET modperformancemetrics = ?
 */
public EvaluationResult evaluateModel(Long modelId, EvaluationConfig config, String evaluatedBy) {
    Model model = noCodeClient.findById(Model.class, modelId);
    if (model == null) {
        throw new EntityNotFoundException("Modelo no encontrado: " + modelId);
    }

    // TODO: Llamar a microservicio Python para evaluación
    // PENDIENTE: Implementar cuando microservicios estén disponibles
    EvaluationResult result;
    // Por ahora, crear resultado mock para desarrollo
    result = createMockEvaluationResult(model, config);

    // Método helper para crear resultado mock (eliminar cuando se implemente integración real)
    /*
    private EvaluationResult createMockEvaluationResult(Model model, EvaluationConfig config) {
        EvaluationResult mock = new EvaluationResult();
        mock.setModelId(model.getIdxmodel());
        mock.setEvaluationType(config.getEvaluationType());
        mock.setScore(new BigDecimal("0.85"));
        mock.setStatus("COMPLETED");
        mock.setTimestamp(new Timestamp(System.currentTimeMillis()));
        // Agregar más campos según necesidad
        return mock;
    }
    */
    /*
    try {
        if ("LLM".equals(model.getModtype())) {
            result = llmEvaluationClient.evaluateModel(modelId, config);
        } else if ("RAG".equals(model.getModtype())) {
            result = ragEvaluationClient.evaluateRAG(modelId, config);
        } else {
            result = modelWrapperClient.evaluateModel(modelId, config);
        }
    } catch (Exception e) {
        log.error("Error evaluando modelo", e);
        throw new BusinessException("Error en evaluación: " + e.getMessage(), e);
    }
    */

    // Guardar resultados en BBDD
    ModelEvaluation evaluation = new ModelEvaluation();
    evaluation.setModel(model);
    evaluation.setEvaltype(config.getEvaluationType());
    evaluation.setEvalresults(result.toJson());
    evaluation.setEvaldate(new Timestamp(System.currentTimeMillis()));
    evaluation.setEvalcreatedby(evaluatedBy);
    noCodeClient.save(evaluation);

    // Actualizar métricas del modelo
    updatePerformanceMetrics(model, result);

    return result;
}
```

#### **1.4. Operaciones de Dependencias**

**Método: `addDependency(Long sourceModelId, Long targetModelId, String dependencyType, String addedBy)`**
```java
/**
 * Añade una dependencia entre modelos
 *
 * Validaciones:
 * - No crear dependencias circulares
 * - Modelos existen
 *
 * Consultas BBDD:
 * - Verificar circularidad: SELECT * FROM modmodeldependencies WHERE idxmodel = ? OR idxmodeldependency = ?
 * - INSERT INTO modmodeldependencies (...)
 */
public ModelDependency addDependency(Long sourceModelId, Long targetModelId, String dependencyType, String addedBy) {
    // Validar que no sea circular
    if (wouldCreateCircularDependency(sourceModelId, targetModelId)) {
        throw new BusinessException("No se puede crear dependencia circular");
    }

    ModelDependency dependency = new ModelDependency();
    dependency.setSourceModel(noCodeClient.findById(Model.class, sourceModelId));
    dependency.setTargetModel(noCodeClient.findById(Model.class, targetModelId));
    dependency.setDepdependencytype(dependencyType);
    dependency.setDepcreatedby(addedBy);
    dependency.setDepcreatedat(new Timestamp(System.currentTimeMillis()));

    return noCodeClient.save(dependency);
}
```

---

## 🔗 INTEGRACIÓN CON MICROSERVICIOS PYTHON

> **⚠️ NOTA IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** y pendientes de implementación.
> Se deben implementar cuando los microservicios estén disponibles y operativos.

### **1. codeflowx-governance-api**

**Endpoint: `POST /api/v1/models/{modelId}/classify-high-risk`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
// Cliente: GovernanceApiClient
/*
public void classifyModelAsHighRisk(Long modelId, String category, String justification) {
    String url = governanceApiBaseUrl + "/api/v1/models/" + modelId + "/classify-high-risk";
    Map<String, Object> payload = Map.of(
        "category", category,
        "justification", justification,
        "timestamp", System.currentTimeMillis()
    );
    restTemplate.postForObject(url, payload, Void.class);
}
*/
```

**Endpoint: `POST /api/v1/models/{modelId}/technical-documentation`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public TechnicalDocValidationResult validateTechnicalDocumentation(Long modelId, String docUrl) {
    String url = governanceApiBaseUrl + "/api/v1/models/" + modelId + "/technical-documentation";
    Map<String, Object> payload = Map.of("documentationUrl", docUrl);
    return restTemplate.postForObject(url, payload, TechnicalDocValidationResult.class);
}
*/
```

### **2. LLMEvaluationClient**

**Método: `evaluateModel(Long modelId, EvaluationConfig config)`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public EvaluationResult evaluateModel(Long modelId, EvaluationConfig config) {
    String url = llmEvaluationBaseUrl + "/api/v1/evaluate";
    Map<String, Object> payload = Map.of(
        "modelId", modelId,
        "config", config
    );
    return restTemplate.postForObject(url, payload, EvaluationResult.class);
}
*/
```

### **3. codeflowx-aios-telemetry**

**Endpoint: `POST /api/v1/telemetry/models/{modelId}/metrics`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public void sendModelMetrics(Long modelId, Map<String, Object> metrics) {
    String url = telemetryBaseUrl + "/api/v1/telemetry/models/" + modelId + "/metrics";
    restTemplate.postForObject(url, metrics, Void.class);
}
*/
```

---

## 📊 CONSULTAS BBDD ESPECÍFICAS

### **1. Obtener modelos con documentación incompleta**
```sql
SELECT m.idxmodel, m.modname, m.modishighrisk,
       m.modtechnicaldoccomplete, m.modtechnicaldocscore
FROM modmodels m
WHERE m.modishighrisk = true
  AND (m.modtechnicaldoccomplete IS NULL OR m.modtechnicaldoccomplete = false
       OR m.modtechnicaldocscore < 0.90)
ORDER BY m.modcreatedat DESC;
```

### **2. Obtener modelos sin dataset documentado**
```sql
SELECT m.idxmodel, m.modname, m.modishighrisk, m.modtrainingconfig
FROM modmodels m
WHERE m.modishighrisk = true
  AND (m.modtrainingconfig IS NULL OR m.modtrainingconfig = '')
ORDER BY m.modcreatedat DESC;
```

### **3. Obtener dependencias de un modelo**
```sql
SELECT md.idxmodeldependency,
       m1.modname AS source_model,
       m2.modname AS target_model,
       md.depdependencytype
FROM modmodeldependencies md
JOIN modmodels m1 ON md.idxmodel = m1.idxmodel
JOIN modmodels m2 ON md.idxmodeldependency = m2.idxmodel
WHERE md.idxmodel = ? OR md.idxmodeldependency = ?
ORDER BY md.depcreatedat DESC;
```

### **4. Obtener versiones desplegadas de un modelo**
```sql
SELECT mv.idxmodelversion, mv.mvdversion, mv.mvdstatus, mv.mvddeployedat
FROM srvmodelversions mv
WHERE mv.idxmodel = ?
  AND mv.mvdstatus = 'DEPLOYED'
ORDER BY mv.mvddeployedat DESC;
```

### **5. Obtener métricas de rendimiento de un modelo**
```sql
SELECT mp.idxmodelperformance, mp.metmetricname, mp.metmetricvalue, mp.metdate
FROM modmodelperformances mp
WHERE mp.idxmodel = ?
ORDER BY mp.metdate DESC
LIMIT 100;
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Fase 1: Business Service Base**
- [ ] Crear `ModelBusinessService.java`
- [ ] Implementar operaciones CRUD básicas
- [ ] Implementar validaciones de auditoría (INC-001, INC-003, INC-005)
- [ ] Implementar métodos de clasificación y compliance

### **Fase 2: Integración con Microservicios (PENDIENTE)**
- [ ] ⚠️ **PENDIENTE:** Configurar clientes REST para microservicios Python
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-governance-api`
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `LLMEvaluationClient`
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-aios-telemetry`
- [ ] **NOTA:** Todas las llamadas a microservicios están comentadas en el código

### **Fase 3: Consultas BBDD**
- [ ] Implementar consultas específicas de validación
- [ ] Implementar consultas de dependencias
- [ ] Implementar consultas de métricas

### **Fase 4: Testing y Validación**
- [ ] Crear tests unitarios para cada método
- [ ] Validar integración con microservicios
- [ ] Validar consultas BBDD
- [ ] Validar cumplimiento de auditoría

---

## 📝 NOTAS IMPORTANTES

1. **Arquitectura EnArt:** Usar `NoCodeClient` (no Repository) para acceso a datos
2. **Validaciones Críticas:** Implementar todas las validaciones de auditoría antes de permitir operaciones
3. **Microservicios Python:** ⚠️ **TODAS LAS LLAMADAS ESTÁN COMENTADAS** - Pendientes de implementar cuando microservicios estén disponibles
4. **Logging:** Registrar todas las operaciones críticas para auditoría
5. **Transacciones:** Usar `@Transactional` para operaciones que modifican múltiples entidades
6. **Entidades JPA:** Revisar entidades en `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/models/` para campos disponibles

---

**Última actualización:** Diciembre 2025
**Estado:** Pendiente de implementación
