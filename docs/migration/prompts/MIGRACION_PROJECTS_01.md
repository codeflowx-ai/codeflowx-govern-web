# Migración de ViewModels a Servicios Dedicados - Projects

## Información del Documento
- **Módulo**: Projects
- **Total ViewModels**: 47
- **Documento**: 1 de 1
- **ViewModels en este documento**: 47

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


### ProjectsDashboardViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/projects/ProjectsDashboardViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 142: businessService.findAllView(
- -> Debería usar:
- Línea 156: businessService.findAllView(
- -> Debería usar:
- Línea 170: businessService.findAllView(
- -> Debería usar:
- Línea 184: businessService.findAllView(
- -> Debería usar:

**Servicios a usar**:
- *Verificar servicio en el código*

---


### ClientProfitabilityAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ClientProfitabilityAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ClientProfitabilityAnalysis.class
- -> Debería usar: ClientProfitabilityAnalysisService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ClientProfitabilityAnalysisMetricsSummary.class
- -> Debería usar: ClientProfitabilityAnalysisMetricsSummaryService

**Servicios a usar**:
- `ClientProfitabilityAnalysisMetricsSummaryService`
- `ClientProfitabilityAnalysisService`

---


### InvoiceAgingReportOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/InvoiceAgingReportOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(InvoiceAgingReport.class
- -> Debería usar: InvoiceAgingReportService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(InvoiceAgingReportMetricsSummary.class
- -> Debería usar: InvoiceAgingReportMetricsSummaryService

**Servicios a usar**:
- `InvoiceAgingReportMetricsSummaryService`
- `InvoiceAgingReportService`

---


### ProjectArtifactDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectArtifactDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(ProjectArtifact.class
- -> Debería usar: ProjectArtifactService

**Servicios a usar**:
- `ProjectArtifactService`

---


### ProjectArtifactOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectArtifactOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectArtifact.class
- -> Debería usar: ProjectArtifactService
- Línea 180: businessService.findAllView(ProjectArtifactMetricsSummary.class
- -> Debería usar: ProjectArtifactMetricsSummaryService

**Servicios a usar**:
- `ProjectArtifactMetricsSummaryService`
- `ProjectArtifactService`

---


### ProjectBillingDetailDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectBillingDetailDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(ProjectBillingDetail.class
- -> Debería usar: ProjectBillingDetailService

**Servicios a usar**:
- `ProjectBillingDetailService`

---


### ProjectBillingDetailOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectBillingDetailOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectBillingDetail.class
- -> Debería usar: ProjectBillingDetailService
- Línea 180: businessService.findAllView(ProjectBillingDetailMetricsSummary.class
- -> Debería usar: ProjectBillingDetailMetricsSummaryService

**Servicios a usar**:
- `ProjectBillingDetailMetricsSummaryService`
- `ProjectBillingDetailService`

---


### ProjectBillingStatusOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectBillingStatusOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectBillingStatus.class
- -> Debería usar: ProjectBillingStatusService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectBillingStatusMetricsSummary.class
- -> Debería usar: ProjectBillingStatusMetricsSummaryService

**Servicios a usar**:
- `ProjectBillingStatusMetricsSummaryService`
- `ProjectBillingStatusService`

---


### ProjectCostBreakdownOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectCostBreakdownOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectCostBreakdown.class
- -> Debería usar: ProjectCostBreakdownService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectCostBreakdownMetricsSummary.class
- -> Debería usar: ProjectCostBreakdownMetricsSummaryService

**Servicios a usar**:
- `ProjectCostBreakdownMetricsSummaryService`
- `ProjectCostBreakdownService`

---


### ProjectCostEstimatorDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectCostEstimatorDetailViewModel.java`

**Problemas encontrados**:
- Línea 311: businessService.findAllEntity(ProjectBillingDetail.class
- -> Debería usar: ProjectBillingDetailService
- Línea 337: businessService.findAllEntity(ProjectInvoice.class
- -> Debería usar: ProjectInvoiceService
- Línea 175: businessService.findById(ProjectCostEstimator.class
- -> Debería usar: ProjectCostEstimatorService

**Servicios a usar**:
- `ProjectBillingDetailService`
- `ProjectCostEstimatorService`
- `ProjectInvoiceService`

---


### ProjectCostEstimatorOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectCostEstimatorOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectCostEstimator.class
- -> Debería usar: ProjectCostEstimatorService
- Línea 180: businessService.findAllView(ProjectCostEstimatorMetricsSummary.class
- -> Debería usar: ProjectCostEstimatorMetricsSummaryService

**Servicios a usar**:
- `ProjectCostEstimatorMetricsSummaryService`
- `ProjectCostEstimatorService`

---


### ProjectDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectDetailViewModel.java`

**Problemas encontrados**:
- Línea 349: businessService.findAllEntity(ProjectArtifact.class
- -> Debería usar: ProjectArtifactService
- Línea 375: businessService.findAllEntity(ProjectMember.class
- -> Debería usar: ProjectMemberService
- Línea 401: businessService.findAllEntity(ProjectTechnology.class
- -> Debería usar: ProjectTechnologyService
- Línea 185: businessService.findById(Project.class
- -> Debería usar: ProjectService

**Servicios a usar**:
- `ProjectArtifactService`
- `ProjectMemberService`
- `ProjectService`
- `ProjectTechnologyService`

---


### ProjectDocumentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectDocumentDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(ProjectDocument.class
- -> Debería usar: ProjectDocumentService

**Servicios a usar**:
- `ProjectDocumentService`

---


### ProjectDocumentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectDocumentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectDocument.class
- -> Debería usar: ProjectDocumentService
- Línea 180: businessService.findAllView(ProjectDocumentMetricsSummary.class
- -> Debería usar: ProjectDocumentMetricsSummaryService

**Servicios a usar**:
- `ProjectDocumentMetricsSummaryService`
- `ProjectDocumentService`

---


### ProjectDomainDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectDomainDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(ProjectDomain.class
- -> Debería usar: ProjectDomainService

**Servicios a usar**:
- `ProjectDomainService`

---


### ProjectDomainOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectDomainOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectDomain.class
- -> Debería usar: ProjectDomainService
- Línea 180: businessService.findAllView(ProjectDomainMetricsSummary.class
- -> Debería usar: ProjectDomainMetricsSummaryService

**Servicios a usar**:
- `ProjectDomainMetricsSummaryService`
- `ProjectDomainService`

---


### ProjectFinancialSummaryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectFinancialSummaryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectFinancialSummary.class
- -> Debería usar: ProjectFinancialSummaryService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectFinancialSummaryMetricsSummary.class
- -> Debería usar: ProjectFinancialSummaryMetricsSummaryService

**Servicios a usar**:
- `ProjectFinancialSummaryMetricsSummaryService`
- `ProjectFinancialSummaryService`

---


### ProjectInvoiceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectInvoiceDetailViewModel.java`

**Problemas encontrados**:
- Línea 174: businessService.findById(ProjectInvoice.class
- -> Debería usar: ProjectInvoiceService

**Servicios a usar**:
- `ProjectInvoiceService`

---


### ProjectInvoiceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectInvoiceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectInvoice.class
- -> Debería usar: ProjectInvoiceService
- Línea 180: businessService.findAllView(ProjectInvoiceMetricsSummary.class
- -> Debería usar: ProjectInvoiceMetricsSummaryService

**Servicios a usar**:
- `ProjectInvoiceMetricsSummaryService`
- `ProjectInvoiceService`

---


### ProjectLicenseDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectLicenseDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(ProjectLicense.class
- -> Debería usar: ProjectLicenseService

**Servicios a usar**:
- `ProjectLicenseService`

---


### ProjectLicenseOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectLicenseOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectLicense.class
- -> Debería usar: ProjectLicenseService
- Línea 180: businessService.findAllView(ProjectLicenseMetricsSummary.class
- -> Debería usar: ProjectLicenseMetricsSummaryService

**Servicios a usar**:
- `ProjectLicenseMetricsSummaryService`
- `ProjectLicenseService`

---


### ProjectMemberDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectMemberDetailViewModel.java`

**Problemas encontrados**:
- Línea 172: businessService.findById(ProjectMember.class
- -> Debería usar: ProjectMemberService

**Servicios a usar**:
- `ProjectMemberService`

---


### ProjectMemberOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectMemberOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectMember.class
- -> Debería usar: ProjectMemberService
- Línea 180: businessService.findAllView(ProjectMemberMetricsSummary.class
- -> Debería usar: ProjectMemberMetricsSummaryService

**Servicios a usar**:
- `ProjectMemberMetricsSummaryService`
- `ProjectMemberService`

---


### ProjectOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(Project.class
- -> Debería usar: ProjectService
- Línea 180: businessService.findAllView(ProjectMetricsSummary.class
- -> Debería usar: ProjectMetricsSummaryService

**Servicios a usar**:
- `ProjectMetricsSummaryService`
- `ProjectService`

---


### ProjectPortfolioDashboardOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectPortfolioDashboardOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectPortfolioDashboard.class
- -> Debería usar: ProjectPortfolioDashboardService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectPortfolioDashboardMetricsSummary.class
- -> Debería usar: ProjectPortfolioDashboardMetricsSummaryService

**Servicios a usar**:
- `ProjectPortfolioDashboardMetricsSummaryService`
- `ProjectPortfolioDashboardService`

---


### ProjectRequirementDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectRequirementDetailViewModel.java`

**Problemas encontrados**:
- Línea 175: businessService.findById(ProjectRequirement.class
- -> Debería usar: ProjectRequirementService

**Servicios a usar**:
- `ProjectRequirementService`

---


### ProjectRequirementOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectRequirementOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectRequirement.class
- -> Debería usar: ProjectRequirementService
- Línea 180: businessService.findAllView(ProjectRequirementMetricsSummary.class
- -> Debería usar: ProjectRequirementMetricsSummaryService

**Servicios a usar**:
- `ProjectRequirementMetricsSummaryService`
- `ProjectRequirementService`

---


### ProjectResourceAllocationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectResourceAllocationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectResourceAllocation.class
- -> Debería usar: ProjectResourceAllocationService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectResourceAllocationMetricsSummary.class
- -> Debería usar: ProjectResourceAllocationMetricsSummaryService

**Servicios a usar**:
- `ProjectResourceAllocationMetricsSummaryService`
- `ProjectResourceAllocationService`

---


### ProjectResourceConsumptionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectResourceConsumptionDetailViewModel.java`

**Problemas encontrados**:
- Línea 172: businessService.findById(ProjectResourceConsumption.class
- -> Debería usar: ProjectResourceConsumptionService

**Servicios a usar**:
- `ProjectResourceConsumptionService`

---


### ProjectResourceConsumptionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectResourceConsumptionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectResourceConsumption.class
- -> Debería usar: ProjectResourceConsumptionService
- Línea 180: businessService.findAllView(ProjectResourceConsumptionMetricsSummary.class
- -> Debería usar: ProjectResourceConsumptionMetricsSummaryService

**Servicios a usar**:
- `ProjectResourceConsumptionMetricsSummaryService`
- `ProjectResourceConsumptionService`

---


### ProjectRiskAssessmentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectRiskAssessmentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectRiskAssessment.class
- -> Debería usar: ProjectRiskAssessmentService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectRiskAssessmentMetricsSummary.class
- -> Debería usar: ProjectRiskAssessmentMetricsSummaryService

**Servicios a usar**:
- `ProjectRiskAssessmentMetricsSummaryService`
- `ProjectRiskAssessmentService`

---


### ProjectRoiAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectRoiAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectRoiAnalysis.class
- -> Debería usar: ProjectRoiAnalysisService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectRoiAnalysisMetricsSummary.class
- -> Debería usar: ProjectRoiAnalysisMetricsSummaryService

**Servicios a usar**:
- `ProjectRoiAnalysisMetricsSummaryService`
- `ProjectRoiAnalysisService`

---


### ProjectROIDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectROIDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(ProjectROI.class
- -> Debería usar: ProjectROIService

**Servicios a usar**:
- `ProjectROIService`

---


### ProjectROIOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectROIOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectROI.class
- -> Debería usar: ProjectROIService
- Línea 180: businessService.findAllView(ProjectROIMetricsSummary.class
- -> Debería usar: ProjectROIMetricsSummaryService

**Servicios a usar**:
- `ProjectROIMetricsSummaryService`
- `ProjectROIService`

---


### ProjectStackDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectStackDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(ProjectStack.class
- -> Debería usar: ProjectStackService

**Servicios a usar**:
- `ProjectStackService`

---


### ProjectStackOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectStackOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectStack.class
- -> Debería usar: ProjectStackService
- Línea 180: businessService.findAllView(ProjectStackMetricsSummary.class
- -> Debería usar: ProjectStackMetricsSummaryService

**Servicios a usar**:
- `ProjectStackMetricsSummaryService`
- `ProjectStackService`

---


### ProjectTaskDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTaskDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(ProjectTask.class
- -> Debería usar: ProjectTaskService

**Servicios a usar**:
- `ProjectTaskService`

---


### ProjectTaskOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTaskOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectTask.class
- -> Debería usar: ProjectTaskService
- Línea 180: businessService.findAllView(ProjectTaskMetricsSummary.class
- -> Debería usar: ProjectTaskMetricsSummaryService

**Servicios a usar**:
- `ProjectTaskMetricsSummaryService`
- `ProjectTaskService`

---


### ProjectTechnologyDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTechnologyDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(ProjectTechnology.class
- -> Debería usar: ProjectTechnologyService

**Servicios a usar**:
- `ProjectTechnologyService`

---


### ProjectTechnologyOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTechnologyOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectTechnology.class
- -> Debería usar: ProjectTechnologyService
- Línea 180: businessService.findAllView(ProjectTechnologyMetricsSummary.class
- -> Debería usar: ProjectTechnologyMetricsSummaryService

**Servicios a usar**:
- `ProjectTechnologyMetricsSummaryService`
- `ProjectTechnologyService`

---


### ProjectTimelineGanttOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTimelineGanttOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ProjectTimelineGantt.class
- -> Debería usar: ProjectTimelineGanttService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ProjectTimelineGanttMetricsSummary.class
- -> Debería usar: ProjectTimelineGanttMetricsSummaryService

**Servicios a usar**:
- `ProjectTimelineGanttMetricsSummaryService`
- `ProjectTimelineGanttService`

---


### ProjectTimeTrackingDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTimeTrackingDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(ProjectTimeTracking.class
- -> Debería usar: ProjectTimeTrackingService

**Servicios a usar**:
- `ProjectTimeTrackingService`

---


### ProjectTimeTrackingOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTimeTrackingOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectTimeTracking.class
- -> Debería usar: ProjectTimeTrackingService
- Línea 180: businessService.findAllView(ProjectTimeTrackingMetricsSummary.class
- -> Debería usar: ProjectTimeTrackingMetricsSummaryService

**Servicios a usar**:
- `ProjectTimeTrackingMetricsSummaryService`
- `ProjectTimeTrackingService`

---


### ProjectTokenDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTokenDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(ProjectToken.class
- -> Debería usar: ProjectTokenService

**Servicios a usar**:
- `ProjectTokenService`

---


### ProjectTokenOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectTokenOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectToken.class
- -> Debería usar: ProjectTokenService
- Línea 180: businessService.findAllView(ProjectTokenMetricsSummary.class
- -> Debería usar: ProjectTokenMetricsSummaryService

**Servicios a usar**:
- `ProjectTokenMetricsSummaryService`
- `ProjectTokenService`

---


### ProjectVersionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectVersionDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(ProjectVersion.class
- -> Debería usar: ProjectVersionService

**Servicios a usar**:
- `ProjectVersionService`

---


### ProjectVersionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/projects/ProjectVersionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ProjectVersion.class
- -> Debería usar: ProjectVersionService
- Línea 180: businessService.findAllView(ProjectVersionMetricsSummary.class
- -> Debería usar: ProjectVersionMetricsSummaryService

**Servicios a usar**:
- `ProjectVersionMetricsSummaryService`
- `ProjectVersionService`

---

