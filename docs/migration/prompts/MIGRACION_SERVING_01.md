# Migración de ViewModels a Servicios Dedicados - Serving

## Información del Documento
- **Módulo**: Serving
- **Total ViewModels**: 29
- **Documento**: 1 de 1
- **ViewModels en este documento**: 29

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


### ServingDashboardViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/serving/ServingDashboardViewModel.java`

**Problemas encontrados**:
- Línea 141: businessService.findAllView(
- -> Debería usar:
- Línea 158: businessService.findAllView(
- -> Debería usar:
- Línea 175: businessService.findAllView(
- -> Debería usar:
- Línea 192: businessService.findAllView(
- -> Debería usar:
- Línea 209: businessService.findAllView(
- -> Debería usar:
- ... y 2 más

**Servicios a usar**:
- *Verificar servicio en el código*

---


### DeploymentInstanceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/DeploymentInstanceDetailViewModel.java`

**Problemas encontrados**:
- Línea 174: businessService.findById(DeploymentInstance.class
- -> Debería usar: DeploymentInstanceService

**Servicios a usar**:
- `DeploymentInstanceService`

---


### DeploymentInstanceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/DeploymentInstanceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(DeploymentInstance.class
- -> Debería usar: DeploymentInstanceService
- Línea 187: businessService.findAllView(DeploymentInstanceMetricsSummary.class
- -> Debería usar: DeploymentInstanceMetricsSummaryService

**Servicios a usar**:
- `DeploymentInstanceMetricsSummaryService`
- `DeploymentInstanceService`

---


### DeploymentLogDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/DeploymentLogDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(DeploymentLog.class
- -> Debería usar: DeploymentLogService

**Servicios a usar**:
- `DeploymentLogService`

---


### DeploymentLogOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/DeploymentLogOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(DeploymentLog.class
- -> Debería usar: DeploymentLogService
- Línea 187: businessService.findAllView(DeploymentLogMetricsSummary.class
- -> Debería usar: DeploymentLogMetricsSummaryService

**Servicios a usar**:
- `DeploymentLogMetricsSummaryService`
- `DeploymentLogService`

---


### DeploymentMetricDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/DeploymentMetricDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(DeploymentMetric.class
- -> Debería usar: DeploymentMetricService

**Servicios a usar**:
- `DeploymentMetricService`

---


### DeploymentMetricOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/DeploymentMetricOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(DeploymentMetric.class
- -> Debería usar: DeploymentMetricService
- Línea 187: businessService.findAllView(DeploymentMetricMetricsSummary.class
- -> Debería usar: DeploymentMetricMetricsSummaryService

**Servicios a usar**:
- `DeploymentMetricMetricsSummaryService`
- `DeploymentMetricService`

---


### DeploymentStatusOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/DeploymentStatusOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(DeploymentStatus.class
- -> Debería usar: DeploymentStatusService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(DeploymentStatusMetricsSummary.class
- -> Debería usar: DeploymentStatusMetricsSummaryService

**Servicios a usar**:
- `DeploymentStatusMetricsSummaryService`
- `DeploymentStatusService`

---


### EndpointAnalyticsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/EndpointAnalyticsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(EndpointAnalytics.class
- -> Debería usar: EndpointAnalyticsService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(EndpointAnalyticsMetricsSummary.class
- -> Debería usar: EndpointAnalyticsMetricsSummaryService

**Servicios a usar**:
- `EndpointAnalyticsMetricsSummaryService`
- `EndpointAnalyticsService`

---


### ErrorAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ErrorAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(ErrorAnalysis.class
- -> Debería usar: ErrorAnalysisService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(ErrorAnalysisMetricsSummary.class
- -> Debería usar: ErrorAnalysisMetricsSummaryService

**Servicios a usar**:
- `ErrorAnalysisMetricsSummaryService`
- `ErrorAnalysisService`

---


### ModelDeploymentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelDeploymentDetailViewModel.java`

**Problemas encontrados**:
- Línea 312: businessService.findAllEntity(ModelMetrics.class
- -> Debería usar: ModelMetricsService
- Línea 338: businessService.findAllEntity(ModelPrediction.class
- -> Debería usar: ModelPredictionService
- Línea 177: businessService.findById(ModelDeployment.class
- -> Debería usar: ModelDeploymentService

**Servicios a usar**:
- `ModelDeploymentService`
- `ModelMetricsService`
- `ModelPredictionService`

---


### ModelDeploymentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelDeploymentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ModelDeployment.class
- -> Debería usar: ModelDeploymentService
- Línea 187: businessService.findAllView(ModelDeploymentMetricsSummary.class
- -> Debería usar: ModelDeploymentMetricsSummaryService

**Servicios a usar**:
- `ModelDeploymentMetricsSummaryService`
- `ModelDeploymentService`

---


### ModelDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelDetailViewModel.java`

**Problemas encontrados**:
- Línea 175: businessService.findById(Model.class
- -> Debería usar: ModelService

**Servicios a usar**:
- `ModelService`

---


### ModelMetricsDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelMetricsDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(ModelMetrics.class
- -> Debería usar: ModelMetricsService

**Servicios a usar**:
- `ModelMetricsService`

---


### ModelMetricsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelMetricsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ModelMetrics.class
- -> Debería usar: ModelMetricsService
- Línea 187: businessService.findAllView(ModelMetricsMetricsSummary.class
- -> Debería usar: ModelMetricsMetricsSummaryService

**Servicios a usar**:
- `ModelMetricsMetricsSummaryService`
- `ModelMetricsService`

---


### ModelOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(Model.class
- -> Debería usar: ModelService
- Línea 187: businessService.findAllView(ModelMetricsSummary.class
- -> Debería usar: ModelMetricsSummaryService

**Servicios a usar**:
- `ModelMetricsSummaryService`
- `ModelService`

---


### ModelPredictionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelPredictionDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(ModelPrediction.class
- -> Debería usar: ModelPredictionService

**Servicios a usar**:
- `ModelPredictionService`

---


### ModelPredictionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelPredictionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ModelPrediction.class
- -> Debería usar: ModelPredictionService
- Línea 187: businessService.findAllView(ModelPredictionMetricsSummary.class
- -> Debería usar: ModelPredictionMetricsSummaryService

**Servicios a usar**:
- `ModelPredictionMetricsSummaryService`
- `ModelPredictionService`

---


### ModelVersionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelVersionDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(ModelVersion.class
- -> Debería usar: ModelVersionService

**Servicios a usar**:
- `ModelVersionService`

---


### ModelVersionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ModelVersionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ModelVersion.class
- -> Debería usar: ModelVersionService
- Línea 187: businessService.findAllView(ModelVersionMetricsSummary.class
- -> Debería usar: ModelVersionMetricsSummaryService

**Servicios a usar**:
- `ModelVersionMetricsSummaryService`
- `ModelVersionService`

---


### ServingCostBreakdownOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingCostBreakdownOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(ServingCostBreakdown.class
- -> Debería usar: ServingCostBreakdownService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(ServingCostBreakdownMetricsSummary.class
- -> Debería usar: ServingCostBreakdownMetricsSummaryService

**Servicios a usar**:
- `ServingCostBreakdownMetricsSummaryService`
- `ServingCostBreakdownService`

---


### ServingEndpointDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingEndpointDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(ServingEndpoint.class
- -> Debería usar: ServingEndpointService

**Servicios a usar**:
- `ServingEndpointService`

---


### ServingEndpointOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingEndpointOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ServingEndpoint.class
- -> Debería usar: ServingEndpointService
- Línea 187: businessService.findAllView(ServingEndpointMetricsSummary.class
- -> Debería usar: ServingEndpointMetricsSummaryService

**Servicios a usar**:
- `ServingEndpointMetricsSummaryService`
- `ServingEndpointService`

---


### ServingErrorAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingErrorAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(ServingErrorAnalysis.class
- -> Debería usar: ServingErrorAnalysisService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(ServingErrorAnalysisMetricsSummary.class
- -> Debería usar: ServingErrorAnalysisMetricsSummaryService

**Servicios a usar**:
- `ServingErrorAnalysisMetricsSummaryService`
- `ServingErrorAnalysisService`

---


### ServingPerformanceDashboardOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingPerformanceDashboardOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(ServingPerformanceDashboard.class
- -> Debería usar: ServingPerformanceDashboardService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(ServingPerformanceDashboardMetricsSummary.class
- -> Debería usar: ServingPerformanceDashboardMetricsSummaryService

**Servicios a usar**:
- `ServingPerformanceDashboardMetricsSummaryService`
- `ServingPerformanceDashboardService`

---


### ServingRequestDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingRequestDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(ServingRequest.class
- -> Debería usar: ServingRequestService

**Servicios a usar**:
- `ServingRequestService`

---


### ServingRequestOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingRequestOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ServingRequest.class
- -> Debería usar: ServingRequestService
- Línea 187: businessService.findAllView(ServingRequestMetricsSummary.class
- -> Debería usar: ServingRequestMetricsSummaryService

**Servicios a usar**:
- `ServingRequestMetricsSummaryService`
- `ServingRequestService`

---


### ServingSlaComplianceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/ServingSlaComplianceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(ServingSlaCompliance.class
- -> Debería usar: ServingSlaComplianceService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(ServingSlaComplianceMetricsSummary.class
- -> Debería usar: ServingSlaComplianceMetricsSummaryService

**Servicios a usar**:
- `ServingSlaComplianceMetricsSummaryService`
- `ServingSlaComplianceService`

---


### SlaComplianceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/serving/SlaComplianceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(SlaCompliance.class
- -> Debería usar: SlaComplianceService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(SlaComplianceMetricsSummary.class
- -> Debería usar: SlaComplianceMetricsSummaryService

**Servicios a usar**:
- `SlaComplianceMetricsSummaryService`
- `SlaComplianceService`

---

