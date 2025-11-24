# Migración de ViewModels a Servicios Dedicados - Governance

## Información del Documento
- **Módulo**: Governance
- **Total ViewModels**: 41
- **Documento**: 1 de 1
- **ViewModels en este documento**: 41

## Instrucciones de Migración

### 1. Objetivo
Migrar los ViewModels listados para que usen servicios dedicados en lugar de `businessService` directamente.

### 2. Contexto
Se han creado 333 servicios dedicados para todas las entidades y views del sistema. Los ViewModels deben usar estos servicios en lugar de llamar directamente a `businessService`.

### 2.1. Ubicación de Proyectos

**Servicios (Services)**:
- **Proyecto**: `nocode.service/codeflowx.govern.services`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx.govern.services/`
- **Paquete**: `com.codeflowx.govern.service.[module].[EntityName]Service`
- **Ejemplo**: `com.codeflowx.govern.service.models.ModelService`
- **Total**: 333 servicios (220 entidades + 113 views)

**Entidades JPA**:
- **Proyecto**: `nocode.service/nocode.service.entitys`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys/`
- **Paquete**: `com.codeflowx.govern.entity.[module].[EntityName]`
- **Ejemplo**: `com.codeflowx.govern.entity.models.Model`
- **Views**: `com.codeflowx.govern.entity.views.[module].[ViewName]`
- **Ejemplo**: `com.codeflowx.govern.entity.views.training.HpoProgressDashboard`

**ViewModels**:
- **Proyecto**: `suinsit.nova.web`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/`
- **Paquete**: `com.codeflowx.govern.viewmodel.[module]/[EntityName]ViewModel.java`
- **Ejemplo**: `com.codeflowx.govern.viewmodel.agents.AgentsDetailViewModel`

**Pantallas ZUL**:
- **Proyecto**: `suinsit.nova.web`
- **Ubicación**: `src/main/webapp/console/platform/[module]/[entity]/`
- **Ejemplos**:
  - `src/main/webapp/console/platform/agents/overview/page.zul`
  - `src/main/webapp/console/platform/models/detail/page.zul`
  - `src/main/webapp/console/governance/compliance/page.zul`

### 3. Pasos de Migración

#### Paso 1: Identificar el Servicio Correcto
- Para entidades: `EntityName` → `EntityNameService`
- Para views: `ViewName` → `ViewNameService` (**TODAS las views tienen servicio - 113 servicios creados**)
- Los servicios están en: `com.codeflowx.govern.service.*`
- **IMPORTANTE**: Todas las views tienen servicios dedicados (113 servicios de views creados)

#### Paso 2: Inyectar el Servicio
```java
@WireVariable
private EntityNameService entityNameService;
```

#### Paso 3: Reemplazar Llamadas

**Antes (INCORRECTO):**
```java
// findAllEntity
PageResult<Entity> result = businessService.findAllEntity(Entity.class, pageParams, criterias);

// findById
Entity entity = businessService.findById(Entity.class, id);

// save
Entity saved = businessService.save(entity);

// update
Entity updated = businessService.update(entity);

// removeFromID
boolean deleted = businessService.removeFromID(Entity.class, id);

// findAllView (SIEMPRE usar servicio de view si existe)
List<View> views = businessService.findAllView(View.class);
PageResult<View> result = businessService.findAllView(View.class, pageParams, criterias);
```

**Después (CORRECTO):**
```java
// findAllEntity → findAll
PageResult<Entity> result = entityService.findAll(pageParams, criterias);

// findById → findById
Entity entity = entityService.findById(id);

// save → create
Entity saved = entityService.create(entity);

// update → update
Entity updated = entityService.update(entity);

// removeFromID → deleteById
boolean deleted = entityService.deleteById(id);

// findAllView → findAll (para views) - TODAS las views tienen servicio
List<View> views = viewService.findAll();
PageResult<View> result = viewService.findAll(pageParams, criterias);
// NOTA: Todas las views tienen servicios dedicados (113 servicios de views creados)
```

#### Paso 4: Manejo de Excepciones
Todos los servicios lanzan `GovernanceServiceException` (checked exception):
```java
try {
    Entity entity = entityService.findById(id);
} catch (GovernanceServiceException e) {
    log.error("Error al buscar entidad", e);
    // Manejar error
}
```

#### Paso 5: Imports Necesarios
```java
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.[module].[EntityName]Service;
```

### 4. Métodos Disponibles en Servicios

#### Para Entidades (CRUD completo):
- `create(Entity entity) throws GovernanceServiceException`
- `update(Entity entity) throws GovernanceServiceException`
- `saveOrUpdate(Entity entity) throws GovernanceServiceException`
- `findById(Long id) throws GovernanceServiceException`
- `deleteById(Long id) throws GovernanceServiceException`
- `delete(Entity entity) throws GovernanceServiceException`
- `findAll(PageParams pageParams, Criterias criterias) throws GovernanceServiceException`
- `findAll(PageParams pageParams) throws GovernanceServiceException`
- `findAll() throws GovernanceServiceException`

#### Para Views (solo lectura):
- `findAll(PageParams pageParams, Criterias criterias) throws GovernanceServiceException`
- `findAll(PageParams pageParams) throws GovernanceServiceException`
- `findAll() throws GovernanceServiceException`

### 5. Verificación Post-Migración
- ✅ Eliminar todas las referencias a `businessService.findAllEntity`, `businessService.findById`, etc.
- ✅ Verificar que los imports del servicio sean correctos
- ✅ Verificar que se manejen las excepciones `GovernanceServiceException`
- ✅ Compilar y verificar que no hay errores
- ✅ Probar funcionalidad básica del ViewModel

### 5.1. Revisión de Entidades y Servicios

Antes de migrar, **revisar** las entidades y servicios correspondientes:

1. **Revisar la Entidad JPA**:
   - Ubicación: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/[module]/[EntityName].java`
   - Verificar campos: ID, createdat, updatedat, name
   - Verificar getters/setters correctos

2. **Revisar el Servicio**:
   - Ubicación: `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/[module]/[EntityName]Service.java`
   - Verificar métodos disponibles
   - Verificar manejo de excepciones

3. **Para Views**:
   - Ubicación entidad: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/views/[module]/[ViewName].java`
   - Ubicación servicio: `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/[module]/[ViewName]Service.java`
   - Verificar que solo tiene métodos `findAll()` (sin CRUD)

### 5.2. Revisión de Pantallas ZUL

**IMPORTANTE**: Después de migrar el ViewModel, **revisar la pantalla ZUL asociada**:

1. **Localizar el archivo ZUL**:
   - Buscar en: `suinsit.nova.web/src/main/webapp/console/platform/[module]/`
   - O en: `suinsit.nova.web/src/main/webapp/console/governance/[module]/`
   - O en: `suinsit.nova.web/src/main/webapp/console/gobierno/[module]/`
   - Patrones comunes:
     - `platform/[module]/overview/page.zul` - Para ViewModels Overview
     - `platform/[module]/detail/page.zul` - Para ViewModels Detail
     - `platform/[module]/[entity]/overview.zul` - Para ViewModels específicos
     - `platform/[module]/[entity]/page.zul` - Para ViewModels específicos
     - `governance/[module]/page.zul` - Para ViewModels de gobernanza
     - `gobierno/[module]/[entity]-overview.zul` - Para ViewModels en español
     - `gobierno/[module]/[entity]-detail.zul` - Para ViewModels detail en español
   - **Ejemplos reales**:
     - `AgentsDetailViewModel` → `platform/agents/detail/page.zul` o `gobierno/agents/agents-detail.zul`
     - `AgentsOverviewViewModel` → `platform/agents/overview/page.zul` o `gobierno/agents/agents-overview.zul`
     - `ModelBiasAnalysisViewModel` → `platform/evaluation/model-bias-analysis/overview.zul`

2. **Verificar en el ZUL**:
   - ✅ Que los bindings a métodos del ViewModel sigan funcionando
   - ✅ Que los eventos `@Command` estén correctamente vinculados
   - ✅ Que los datos se muestren correctamente después de la migración
   - ✅ Que la paginación funcione correctamente
   - ✅ Que los filtros sigan funcionando

3. **Probar la pantalla**:
   - Ejecutar la aplicación
   - Navegar a la pantalla ZUL
   - Verificar que carga datos correctamente
   - Probar operaciones CRUD si aplica
   - Verificar que no hay errores en consola

### 6. Ejemplo Completo

**Antes:**
```java
@WireVariable
private BusinessService businessService;

public void loadData() {
    try {
        PageParams pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        PageResult<Model> result = businessService.findAllEntity(
            Model.class,
            pageParams,
            new Criterias()
        );

        this.models = result.getContent();
    } catch (Exception e) {
        log.error("Error", e);
    }
}
```

**Después:**
```java
@WireVariable
private ModelService modelService;

public void loadData() {
    try {
        PageParams pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        PageResult<Model> result = modelService.findAll(pageParams);

        this.models = result.getContent();
    } catch (GovernanceServiceException e) {
        log.error("Error al cargar modelos", e);
    }
}
```

### 7. Notas Importantes sobre Preservación de Lógica de Negocio

**CRÍTICO**: Los ViewModels pueden tener lógica de negocio compleja que DEBE mantenerse:

1. **Llamadas a Múltiples Servicios Relacionados**:
   - Si un ViewModel necesita datos de múltiples entidades, puede inyectar múltiples servicios
   - Ejemplo: `ModelService`, `ModelVersionService`, `ModelPerformanceService`
   - Mantener todas las llamadas a servicios relacionados

2. **Uso de Views**:
   - **TODAS las views tienen servicios dedicados** (113 servicios de views creados)
   - **SIEMPRE usar el servicio de view** en lugar de `businessService.findAllView()`
   - Los servicios de views están en los mismos paquetes que las entidades relacionadas
   - Ejemplo: `HpoProgressDashboard` → `HpoProgressDashboardService` en `com.codeflowx.govern.service.training`

3. **Procedimientos Almacenados**:
   - Si el ViewModel llama a procedimientos almacenados, **MANTENER** `businessService` para esas llamadas
   - Los procedimientos no tienen servicios dedicados
   - Ejemplo: `businessService.executeProcedure("sp_name", params)` se mantiene

4. **Microservicios Python**:
   - **MANTENER** las llamadas a microservicios Python sin cambios
   - Solo migrar las llamadas a `businessService` para entidades/views
   - Ejemplo: `pythonServiceClient.call("/api/endpoint", request)` se mantiene

5. **Transformaciones y Validaciones**:
   - Mantener toda la lógica de transformación de datos
   - Mantener todas las validaciones de negocio
   - Mantener reglas de negocio específicas del ViewModel

6. **No eliminar `businessService` completamente**:
   - Mantenerlo si se usa para procedimientos almacenados
   - Mantenerlo si se usa para entidades sin servicio dedicado
   - Mantenerlo si se usa para operaciones especiales no cubiertas por servicios

7. **Testing Post-Migración**:
   - Verificar que toda la funcionalidad sigue funcionando correctamente
   - Probar llamadas a servicios relacionados
   - Probar uso de views
   - Probar procedimientos almacenados
   - Probar integraciones con microservicios Python

---

## ViewModels a Migrar


### ComplianceAutomatedChecksViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/governance/ComplianceAutomatedChecksViewModel.java`

**Problemas encontrados**:
- Línea 179: businessService.findAllView(
- -> Debería usar:

**Servicios a usar**:
- *Verificar servicio en el código*

---


### AutoApprovalAnalyticsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/AutoApprovalAnalyticsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AutoApprovalAnalytics.class
- -> Debería usar: AutoApprovalAnalyticsService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AutoApprovalAnalyticsMetricsSummary.class
- -> Debería usar: AutoApprovalAnalyticsMetricsSummaryService

**Servicios a usar**:
- `AutoApprovalAnalyticsMetricsSummaryService`
- `AutoApprovalAnalyticsService`

---


### ComplianceAssessmentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceAssessmentDetailViewModel.java`

**Problemas encontrados**:
- Línea 353: businessService.findAllEntity(ComplianceFinding.class
- -> Debería usar: ComplianceFindingService
- Línea 379: businessService.findAllEntity(ComplianceRequirement.class
- -> Debería usar: ComplianceRequirementService
- Línea 178: businessService.findById(ComplianceAssessment.class
- -> Debería usar: ComplianceAssessmentService

**Servicios a usar**:
- `ComplianceAssessmentService`
- `ComplianceFindingService`
- `ComplianceRequirementService`

---


### ComplianceAssessmentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceAssessmentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 174: businessService.findAllEntity(
- -> Debería usar:
- Línea 294: businessService.findAllEntity(
- -> Debería usar:
- Línea 316: businessService.findAllEntity(
- -> Debería usar:
- Línea 393: businessService.findAllEntity(
- -> Debería usar:

**Servicios a usar**:
- *Verificar servicio en el código*

---


### ComplianceByFrameworkOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceByFrameworkOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ComplianceByFramework.class
- -> Debería usar: ComplianceByFrameworkService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ComplianceByFrameworkMetricsSummary.class
- -> Debería usar: ComplianceByFrameworkMetricsSummaryService

**Servicios a usar**:
- `ComplianceByFrameworkMetricsSummaryService`
- `ComplianceByFrameworkService`

---


### ComplianceFindingDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceFindingDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(ComplianceFinding.class
- -> Debería usar: ComplianceFindingService

**Servicios a usar**:
- `ComplianceFindingService`

---


### ComplianceFindingOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceFindingOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ComplianceFinding.class
- -> Debería usar: ComplianceFindingService
- Línea 180: businessService.findAllView(ComplianceFindingMetricsSummary.class
- -> Debería usar: ComplianceFindingMetricsSummaryService

**Servicios a usar**:
- `ComplianceFindingMetricsSummaryService`
- `ComplianceFindingService`

---


### ComplianceGapsAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceGapsAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ComplianceGapsAnalysis.class
- -> Debería usar: ComplianceGapsAnalysisService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ComplianceGapsAnalysisMetricsSummary.class
- -> Debería usar: ComplianceGapsAnalysisMetricsSummaryService

**Servicios a usar**:
- `ComplianceGapsAnalysisMetricsSummaryService`
- `ComplianceGapsAnalysisService`

---


### ComplianceRequirementDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceRequirementDetailViewModel.java`

**Problemas encontrados**:
- Línea 172: businessService.findById(ComplianceRequirement.class
- -> Debería usar: ComplianceRequirementService

**Servicios a usar**:
- `ComplianceRequirementService`

---


### ComplianceRequirementOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ComplianceRequirementOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ComplianceRequirement.class
- -> Debería usar: ComplianceRequirementService
- Línea 180: businessService.findAllView(ComplianceRequirementMetricsSummary.class
- -> Debería usar: ComplianceRequirementMetricsSummaryService

**Servicios a usar**:
- `ComplianceRequirementMetricsSummaryService`
- `ComplianceRequirementService`

---


### GovernanceAuditTrailDetailedOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/GovernanceAuditTrailDetailedOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(GovernanceAuditTrailDetailed.class
- -> Debería usar: GovernanceAuditTrailDetailedService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(GovernanceAuditTrailDetailedMetricsSummary.class
- -> Debería usar: GovernanceAuditTrailDetailedMetricsSummaryService

**Servicios a usar**:
- `GovernanceAuditTrailDetailedMetricsSummaryService`
- `GovernanceAuditTrailDetailedService`

---


### GovernanceDashboardSummaryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/GovernanceDashboardSummaryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(GovernanceDashboardSummary.class
- -> Debería usar: GovernanceDashboardSummaryService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(GovernanceDashboardSummaryMetricsSummary.class
- -> Debería usar: GovernanceDashboardSummaryMetricsSummaryService

**Servicios a usar**:
- `GovernanceDashboardSummaryMetricsSummaryService`
- `GovernanceDashboardSummaryService`

---


### GovernanceKpisExecutiveOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/GovernanceKpisExecutiveOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(GovernanceKpisExecutive.class
- -> Debería usar: GovernanceKpisExecutiveService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(GovernanceKpisExecutiveMetricsSummary.class
- -> Debería usar: GovernanceKpisExecutiveMetricsSummaryService

**Servicios a usar**:
- `GovernanceKpisExecutiveMetricsSummaryService`
- `GovernanceKpisExecutiveService`

---


### GovernanceMetricDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/GovernanceMetricDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(GovernanceMetric.class
- -> Debería usar: GovernanceMetricService

**Servicios a usar**:
- `GovernanceMetricService`

---


### GovernanceMetricOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/GovernanceMetricOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(GovernanceMetric.class
- -> Debería usar: GovernanceMetricService
- Línea 180: businessService.findAllView(GovernanceMetricMetricsSummary.class
- -> Debería usar: GovernanceMetricMetricsSummaryService

**Servicios a usar**:
- `GovernanceMetricMetricsSummaryService`
- `GovernanceMetricService`

---


### GovernanceMetricsSummaryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/GovernanceMetricsSummaryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(GovernanceMetricsSummary.class
- -> Debería usar: GovernanceMetricsSummaryService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(GovernanceMetricsSummaryMetricsSummary.class
- -> Debería usar: GovernanceMetricsSummaryMetricsSummaryService

**Servicios a usar**:
- `GovernanceMetricsSummaryMetricsSummaryService`
- `GovernanceMetricsSummaryService`

---


### GovernanceOverviewOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/GovernanceOverviewOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(GovernanceOverview.class
- -> Debería usar: GovernanceOverviewService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(GovernanceOverviewMetricsSummary.class
- -> Debería usar: GovernanceOverviewMetricsSummaryService

**Servicios a usar**:
- `GovernanceOverviewMetricsSummaryService`
- `GovernanceOverviewService`

---


### PolicyAuditLogDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyAuditLogDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(PolicyAuditLog.class
- -> Debería usar: PolicyAuditLogService

**Servicios a usar**:
- `PolicyAuditLogService`

---


### PolicyAuditLogOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyAuditLogOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(PolicyAuditLog.class
- -> Debería usar: PolicyAuditLogService
- Línea 180: businessService.findAllView(PolicyAuditLogMetricsSummary.class
- -> Debería usar: PolicyAuditLogMetricsSummaryService

**Servicios a usar**:
- `PolicyAuditLogMetricsSummaryService`
- `PolicyAuditLogService`

---


### PolicyChecklistItemDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyChecklistItemDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(PolicyChecklistItem.class
- -> Debería usar: PolicyChecklistItemService

**Servicios a usar**:
- `PolicyChecklistItemService`

---


### PolicyChecklistItemOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyChecklistItemOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(PolicyChecklistItem.class
- -> Debería usar: PolicyChecklistItemService
- Línea 180: businessService.findAllView(PolicyChecklistItemMetricsSummary.class
- -> Debería usar: PolicyChecklistItemMetricsSummaryService

**Servicios a usar**:
- `PolicyChecklistItemMetricsSummaryService`
- `PolicyChecklistItemService`

---


### PolicyDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyDetailViewModel.java`

**Problemas encontrados**:
- Línea 359: businessService.findAllEntity(PolicyAuditLog.class
- -> Debería usar: PolicyAuditLogService
- Línea 385: businessService.findAllEntity(PolicyChecklistItem.class
- -> Debería usar: PolicyChecklistItemService
- Línea 411: businessService.findAllEntity(PolicyEvaluation.class
- -> Debería usar: PolicyEvaluationService
- Línea 437: businessService.findAllEntity(PolicyRule.class
- -> Debería usar: PolicyRuleService
- Línea 187: businessService.findById(Policy.class
- -> Debería usar: PolicyService

**Servicios a usar**:
- `PolicyAuditLogService`
- `PolicyChecklistItemService`
- `PolicyEvaluationService`
- `PolicyRuleService`
- `PolicyService`

---


### PolicyEffectivenessReportOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyEffectivenessReportOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(PolicyEffectivenessReport.class
- -> Debería usar: PolicyEffectivenessReportService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(PolicyEffectivenessReportMetricsSummary.class
- -> Debería usar: PolicyEffectivenessReportMetricsSummaryService

**Servicios a usar**:
- `PolicyEffectivenessReportMetricsSummaryService`
- `PolicyEffectivenessReportService`

---


### PolicyEvaluationDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyEvaluationDetailViewModel.java`

**Problemas encontrados**:
- Línea 174: businessService.findById(PolicyEvaluation.class
- -> Debería usar: PolicyEvaluationService

**Servicios a usar**:
- `PolicyEvaluationService`

---


### PolicyEvaluationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyEvaluationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(PolicyEvaluation.class
- -> Debería usar: PolicyEvaluationService
- Línea 180: businessService.findAllView(PolicyEvaluationMetricsSummary.class
- -> Debería usar: PolicyEvaluationMetricsSummaryService

**Servicios a usar**:
- `PolicyEvaluationMetricsSummaryService`
- `PolicyEvaluationService`

---


### PolicyEvaluationTrendsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyEvaluationTrendsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(PolicyEvaluationTrends.class
- -> Debería usar: PolicyEvaluationTrendsService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(PolicyEvaluationTrendsMetricsSummary.class
- -> Debería usar: PolicyEvaluationTrendsMetricsSummaryService

**Servicios a usar**:
- `PolicyEvaluationTrendsMetricsSummaryService`
- `PolicyEvaluationTrendsService`

---


### PolicyOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(Policy.class
- -> Debería usar: PolicyService
- Línea 180: businessService.findAllView(PolicyMetricsSummary.class
- -> Debería usar: PolicyMetricsSummaryService

**Servicios a usar**:
- `PolicyMetricsSummaryService`
- `PolicyService`

---


### PolicyRuleDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyRuleDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(PolicyRule.class
- -> Debería usar: PolicyRuleService

**Servicios a usar**:
- `PolicyRuleService`

---


### PolicyRuleOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyRuleOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(PolicyRule.class
- -> Debería usar: PolicyRuleService
- Línea 180: businessService.findAllView(PolicyRuleMetricsSummary.class
- -> Debería usar: PolicyRuleMetricsSummaryService

**Servicios a usar**:
- `PolicyRuleMetricsSummaryService`
- `PolicyRuleService`

---


### PolicyValidationConfigDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyValidationConfigDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(PolicyValidationConfig.class
- -> Debería usar: PolicyValidationConfigService

**Servicios a usar**:
- `PolicyValidationConfigService`

---


### PolicyValidationConfigOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyValidationConfigOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(PolicyValidationConfig.class
- -> Debería usar: PolicyValidationConfigService
- Línea 180: businessService.findAllView(PolicyValidationConfigMetricsSummary.class
- -> Debería usar: PolicyValidationConfigMetricsSummaryService

**Servicios a usar**:
- `PolicyValidationConfigMetricsSummaryService`
- `PolicyValidationConfigService`

---


### PolicyViolationDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyViolationDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(PolicyViolation.class
- -> Debería usar: PolicyViolationService

**Servicios a usar**:
- `PolicyViolationService`

---


### PolicyViolationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/PolicyViolationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(PolicyViolation.class
- -> Debería usar: PolicyViolationService
- Línea 180: businessService.findAllView(PolicyViolationMetricsSummary.class
- -> Debería usar: PolicyViolationMetricsSummaryService

**Servicios a usar**:
- `PolicyViolationMetricsSummaryService`
- `PolicyViolationService`

---


### RiskAssessmentMatrixOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/RiskAssessmentMatrixOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(RiskAssessmentMatrix.class
- -> Debería usar: RiskAssessmentMatrixService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(RiskAssessmentMatrixMetricsSummary.class
- -> Debería usar: RiskAssessmentMatrixMetricsSummaryService

**Servicios a usar**:
- `RiskAssessmentMatrixMetricsSummaryService`
- `RiskAssessmentMatrixService`

---


### SecurityMetricDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/SecurityMetricDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(SecurityMetric.class
- -> Debería usar: SecurityMetricService

**Servicios a usar**:
- `SecurityMetricService`

---


### SecurityMetricOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/SecurityMetricOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(SecurityMetric.class
- -> Debería usar: SecurityMetricService
- Línea 180: businessService.findAllView(SecurityMetricMetricsSummary.class
- -> Debería usar: SecurityMetricMetricsSummaryService

**Servicios a usar**:
- `SecurityMetricMetricsSummaryService`
- `SecurityMetricService`

---


### SecurityPolicyDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/SecurityPolicyDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(SecurityPolicy.class
- -> Debería usar: SecurityPolicyService

**Servicios a usar**:
- `SecurityPolicyService`

---


### SecurityPolicyOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/SecurityPolicyOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(SecurityPolicy.class
- -> Debería usar: SecurityPolicyService
- Línea 180: businessService.findAllView(SecurityPolicyMetricsSummary.class
- -> Debería usar: SecurityPolicyMetricsSummaryService

**Servicios a usar**:
- `SecurityPolicyMetricsSummaryService`
- `SecurityPolicyService`

---


### SecurityThreatDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/SecurityThreatDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(SecurityThreat.class
- -> Debería usar: SecurityThreatService

**Servicios a usar**:
- `SecurityThreatService`

---


### SecurityThreatOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/SecurityThreatOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(SecurityThreat.class
- -> Debería usar: SecurityThreatService
- Línea 180: businessService.findAllView(SecurityThreatMetricsSummary.class
- -> Debería usar: SecurityThreatMetricsSummaryService

**Servicios a usar**:
- `SecurityThreatMetricsSummaryService`
- `SecurityThreatService`

---


### ViolationHeatmapOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/governance/ViolationHeatmapOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ViolationHeatmap.class
- -> Debería usar: ViolationHeatmapService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ViolationHeatmapMetricsSummary.class
- -> Debería usar: ViolationHeatmapMetricsSummaryService

**Servicios a usar**:
- `ViolationHeatmapMetricsSummaryService`
- `ViolationHeatmapService`

---

