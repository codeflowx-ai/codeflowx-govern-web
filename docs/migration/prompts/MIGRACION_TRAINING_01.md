# Migración de ViewModels a Servicios Dedicados - Training

## Información del Documento
- **Módulo**: Training
- **Total ViewModels**: 54
- **Documento**: 1 de 1
- **ViewModels en este documento**: 54

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


### ExperimentsDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/training/ExperimentsDetailViewModel.java`

**Problemas encontrados**:
- Línea 469: businessService.findAllView(TrainingMetricsSummary.class
- -> Debería usar: TrainingMetricsSummaryService

**Servicios a usar**:
- `TrainingMetricsSummaryService`

---


### TrainingDashboardViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/training/TrainingDashboardViewModel.java`

**Problemas encontrados**:
- Línea 136: businessService.findAllEntity(
- -> Debería usar:
- Línea 153: businessService.findAllEntity(
- -> Debería usar:
- Línea 170: businessService.findAllEntity(
- -> Debería usar:
- Línea 187: businessService.findAllEntity(
- -> Debería usar:
- Línea 204: businessService.findAllEntity(
- -> Debería usar:
- ... y 2 más

**Servicios a usar**:
- *Verificar servicio en el código*

---


### CheckpointDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/CheckpointDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(Checkpoint.class
- -> Debería usar: CheckpointService

**Servicios a usar**:
- `CheckpointService`

---


### CheckpointHistoryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/CheckpointHistoryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(CheckpointHistory.class
- -> Debería usar: CheckpointHistoryService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(CheckpointHistoryMetricsSummary.class
- -> Debería usar: CheckpointHistoryMetricsSummaryService

**Servicios a usar**:
- `CheckpointHistoryMetricsSummaryService`
- `CheckpointHistoryService`

---


### CheckpointOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/CheckpointOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(Checkpoint.class
- -> Debería usar: CheckpointService
- Línea 187: businessService.findAllView(CheckpointMetricsSummary.class
- -> Debería usar: CheckpointMetricsSummaryService

**Servicios a usar**:
- `CheckpointMetricsSummaryService`
- `CheckpointService`

---


### DatasetSourceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/DatasetSourceDetailViewModel.java`

**Problemas encontrados**:
- Línea 174: businessService.findById(DatasetSource.class
- -> Debería usar: DatasetSourceService

**Servicios a usar**:
- `DatasetSourceService`

---


### DatasetSourceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/DatasetSourceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(DatasetSource.class
- -> Debería usar: DatasetSourceService
- Línea 187: businessService.findAllView(DatasetSourceMetricsSummary.class
- -> Debería usar: DatasetSourceMetricsSummaryService

**Servicios a usar**:
- `DatasetSourceMetricsSummaryService`
- `DatasetSourceService`

---


### EnvironmentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/EnvironmentDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(Environment.class
- -> Debería usar: EnvironmentService

**Servicios a usar**:
- `EnvironmentService`

---


### EnvironmentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/EnvironmentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 249: businessService.removeFromID(Environment.class
- -> Debería usar: EnvironmentService
- Línea 186: businessService.findAllView(EnvironmentMetricsSummary.class
- -> Debería usar: EnvironmentMetricsSummaryService

**Servicios a usar**:
- `EnvironmentMetricsSummaryService`
- `EnvironmentService`

---


### ExperimentComparisonMatrixOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentComparisonMatrixOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(ExperimentComparisonMatrix.class
- -> Debería usar: ExperimentComparisonMatrixService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(ExperimentComparisonMatrixMetricsSummary.class
- -> Debería usar: ExperimentComparisonMatrixMetricsSummaryService

**Servicios a usar**:
- `ExperimentComparisonMatrixMetricsSummaryService`
- `ExperimentComparisonMatrixService`

---


### ExperimentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentDetailViewModel.java`

**Problemas encontrados**:
- Línea 368: businessService.findAllEntity(ExperimentLineage.class
- -> Debería usar: ExperimentLineageService
- Línea 394: businessService.findAllEntity(HPOExperiment.class
- -> Debería usar: HPOExperimentService
- Línea 420: businessService.findAllEntity(Run.class
- -> Debería usar: RunService
- Línea 181: businessService.findById(Experiment.class
- -> Debería usar: ExperimentService

**Servicios a usar**:
- `ExperimentLineageService`
- `ExperimentService`
- `HPOExperimentService`
- `RunService`

---


### ExperimentLeaderboardOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentLeaderboardOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(ExperimentLeaderboard.class
- -> Debería usar: ExperimentLeaderboardService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(ExperimentLeaderboardMetricsSummary.class
- -> Debería usar: ExperimentLeaderboardMetricsSummaryService

**Servicios a usar**:
- `ExperimentLeaderboardMetricsSummaryService`
- `ExperimentLeaderboardService`

---


### ExperimentLineageDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentLineageDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(ExperimentLineage.class
- -> Debería usar: ExperimentLineageService

**Servicios a usar**:
- `ExperimentLineageService`

---


### ExperimentLineageOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentLineageOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ExperimentLineage.class
- -> Debería usar: ExperimentLineageService
- Línea 187: businessService.findAllView(ExperimentLineageMetricsSummary.class
- -> Debería usar: ExperimentLineageMetricsSummaryService

**Servicios a usar**:
- `ExperimentLineageMetricsSummaryService`
- `ExperimentLineageService`

---


### ExperimentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 130: businessService.findAllEntity(
- -> Debería usar:
- Línea 251: businessService.removeFromID(Experiment.class
- -> Debería usar: ExperimentService
- Línea 188: businessService.findAllView(ExperimentMetricsSummary.class
- -> Debería usar: ExperimentMetricsSummaryService

**Servicios a usar**:
- `ExperimentMetricsSummaryService`
- `ExperimentService`

---


### ExperimentTemplateDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentTemplateDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(ExperimentTemplate.class
- -> Debería usar: ExperimentTemplateService

**Servicios a usar**:
- `ExperimentTemplateService`

---


### ExperimentTemplateOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ExperimentTemplateOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(ExperimentTemplate.class
- -> Debería usar: ExperimentTemplateService
- Línea 187: businessService.findAllView(ExperimentTemplateMetricsSummary.class
- -> Debería usar: ExperimentTemplateMetricsSummaryService

**Servicios a usar**:
- `ExperimentTemplateMetricsSummaryService`
- `ExperimentTemplateService`

---


### HPOExperimentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/HPOExperimentDetailViewModel.java`

**Problemas encontrados**:
- Línea 339: businessService.findAllEntity(HPOTrial.class
- -> Debería usar: HPOTrialService
- Línea 174: businessService.findById(HPOExperiment.class
- -> Debería usar: HPOExperimentService

**Servicios a usar**:
- `HPOExperimentService`
- `HPOTrialService`

---


### HPOExperimentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/HPOExperimentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(HPOExperiment.class
- -> Debería usar: HPOExperimentService
- Línea 187: businessService.findAllView(HPOExperimentMetricsSummary.class
- -> Debería usar: HPOExperimentMetricsSummaryService

**Servicios a usar**:
- `HPOExperimentMetricsSummaryService`
- `HPOExperimentService`

---


### HpoProgressDashboardOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/HpoProgressDashboardOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(HpoProgressDashboard.class
- -> Debería usar: HpoProgressDashboardService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(HpoProgressDashboardMetricsSummary.class
- -> Debería usar: HpoProgressDashboardMetricsSummaryService

**Servicios a usar**:
- `HpoProgressDashboardMetricsSummaryService`
- `HpoProgressDashboardService`

---


### HPOTrialDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/HPOTrialDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(HPOTrial.class
- -> Debería usar: HPOTrialService

**Servicios a usar**:
- `HPOTrialService`

---


### HPOTrialOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/HPOTrialOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(HPOTrial.class
- -> Debería usar: HPOTrialService
- Línea 187: businessService.findAllView(HPOTrialMetricsSummary.class
- -> Debería usar: HPOTrialMetricsSummaryService

**Servicios a usar**:
- `HPOTrialMetricsSummaryService`
- `HPOTrialService`

---


### MetricSeriesDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/MetricSeriesDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(MetricSeries.class
- -> Debería usar: MetricSeriesService

**Servicios a usar**:
- `MetricSeriesService`

---


### MetricSeriesOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/MetricSeriesOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(MetricSeries.class
- -> Debería usar: MetricSeriesService
- Línea 187: businessService.findAllView(MetricSeriesMetricsSummary.class
- -> Debería usar: MetricSeriesMetricsSummaryService

**Servicios a usar**:
- `MetricSeriesMetricsSummaryService`
- `MetricSeriesService`

---


### MetricSeriesVisualizationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/MetricSeriesVisualizationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(MetricSeriesVisualization.class
- -> Debería usar: MetricSeriesVisualizationService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(MetricSeriesVisualizationMetricsSummary.class
- -> Debería usar: MetricSeriesVisualizationMetricsSummaryService

**Servicios a usar**:
- `MetricSeriesVisualizationMetricsSummaryService`
- `MetricSeriesVisualizationService`

---


### MetricStreamDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/MetricStreamDetailViewModel.java`

**Problemas encontrados**:
- Línea 168: businessService.findById(MetricStream.class
- -> Debería usar: MetricStreamService

**Servicios a usar**:
- `MetricStreamService`

---


### MetricStreamOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/MetricStreamOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(MetricStream.class
- -> Debería usar: MetricStreamService
- Línea 187: businessService.findAllView(MetricStreamMetricsSummary.class
- -> Debería usar: MetricStreamMetricsSummaryService

**Servicios a usar**:
- `MetricStreamMetricsSummaryService`
- `MetricStreamService`

---


### ParamDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ParamDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(Param.class
- -> Debería usar: ParamService

**Servicios a usar**:
- `ParamService`

---


### ParamOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/ParamOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(Param.class
- -> Debería usar: ParamService
- Línea 187: businessService.findAllView(ParamMetricsSummary.class
- -> Debería usar: ParamMetricsSummaryService

**Servicios a usar**:
- `ParamMetricsSummaryService`
- `ParamService`

---


### RunComparisonDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/RunComparisonDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(RunComparison.class
- -> Debería usar: RunComparisonService

**Servicios a usar**:
- `RunComparisonService`

---


### RunComparisonOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/RunComparisonOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(RunComparison.class
- -> Debería usar: RunComparisonService
- Línea 187: businessService.findAllView(RunComparisonMetricsSummary.class
- -> Debería usar: RunComparisonMetricsSummaryService

**Servicios a usar**:
- `RunComparisonMetricsSummaryService`
- `RunComparisonService`

---


### RunDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/RunDetailViewModel.java`

**Problemas encontrados**:
- Línea 422: businessService.findAllEntity(Checkpoint.class
- -> Debería usar: CheckpointService
- Línea 448: businessService.findAllEntity(Environment.class
- -> Debería usar: EnvironmentService
- Línea 474: businessService.findAllEntity(HPOTrial.class
- -> Debería usar: HPOTrialService
- Línea 500: businessService.findAllEntity(MetricSeries.class
- -> Debería usar: MetricSeriesService
- Línea 526: businessService.findAllEntity(MetricStream.class
- -> Debería usar: MetricStreamService
- ... y 16 más

**Servicios a usar**:
- `CheckpointService`
- `EnvironmentService`
- `HPOTrialService`
- `MetricSeriesService`
- `MetricStreamService`
- `ParamService`
- `RunService`
- `TagService`
- `TrainingAlertService`
- `TrainingArtifactService`
- `TrainingLogService`
- `TrainingMetricService`

---


### RunOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/RunOverviewViewModel.java`

**Problemas encontrados**:
- Línea 130: businessService.findAllEntity(
- -> Debería usar:
- Línea 251: businessService.removeFromID(Run.class
- -> Debería usar: RunService
- Línea 188: businessService.findAllView(RunMetricsSummary.class
- -> Debería usar: RunMetricsSummaryService

**Servicios a usar**:
- `RunMetricsSummaryService`
- `RunService`

---


### TagDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TagDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(Tag.class
- -> Debería usar: TagService

**Servicios a usar**:
- `TagService`

---


### TagOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TagOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(Tag.class
- -> Debería usar: TagService
- Línea 187: businessService.findAllView(TagMetricsSummary.class
- -> Debería usar: TagMetricsSummaryService

**Servicios a usar**:
- `TagMetricsSummaryService`
- `TagService`

---


### TrainingAlertDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingAlertDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(TrainingAlert.class
- -> Debería usar: TrainingAlertService

**Servicios a usar**:
- `TrainingAlertService`

---


### TrainingAlertOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingAlertOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(TrainingAlert.class
- -> Debería usar: TrainingAlertService
- Línea 187: businessService.findAllView(TrainingAlertMetricsSummary.class
- -> Debería usar: TrainingAlertMetricsSummaryService

**Servicios a usar**:
- `TrainingAlertMetricsSummaryService`
- `TrainingAlertService`

---


### TrainingAlertsSummaryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingAlertsSummaryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(TrainingAlertsSummary.class
- -> Debería usar: TrainingAlertsSummaryService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(TrainingAlertsSummaryMetricsSummary.class
- -> Debería usar: TrainingAlertsSummaryMetricsSummaryService

**Servicios a usar**:
- `TrainingAlertsSummaryMetricsSummaryService`
- `TrainingAlertsSummaryService`

---


### TrainingArtifactDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingArtifactDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(TrainingArtifact.class
- -> Debería usar: TrainingArtifactService

**Servicios a usar**:
- `TrainingArtifactService`

---


### TrainingArtifactOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingArtifactOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(TrainingArtifact.class
- -> Debería usar: TrainingArtifactService
- Línea 187: businessService.findAllView(TrainingArtifactMetricsSummary.class
- -> Debería usar: TrainingArtifactMetricsSummaryService

**Servicios a usar**:
- `TrainingArtifactMetricsSummaryService`
- `TrainingArtifactService`

---


### TrainingCostAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingCostAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(TrainingCostAnalysis.class
- -> Debería usar: TrainingCostAnalysisService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(TrainingCostAnalysisMetricsSummary.class
- -> Debería usar: TrainingCostAnalysisMetricsSummaryService

**Servicios a usar**:
- `TrainingCostAnalysisMetricsSummaryService`
- `TrainingCostAnalysisService`

---


### TrainingExecutionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingExecutionDetailViewModel.java`

**Problemas encontrados**:
- Línea 172: businessService.findById(TrainingExecution.class
- -> Debería usar: TrainingExecutionService

**Servicios a usar**:
- `TrainingExecutionService`

---


### TrainingExecutionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingExecutionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(TrainingExecution.class
- -> Debería usar: TrainingExecutionService
- Línea 187: businessService.findAllView(TrainingExecutionMetricsSummary.class
- -> Debería usar: TrainingExecutionMetricsSummaryService

**Servicios a usar**:
- `TrainingExecutionMetricsSummaryService`
- `TrainingExecutionService`

---


### TrainingGovernanceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingGovernanceDetailViewModel.java`

**Problemas encontrados**:
- Línea 174: businessService.findById(TrainingGovernance.class
- -> Debería usar: TrainingGovernanceService

**Servicios a usar**:
- `TrainingGovernanceService`

---


### TrainingGovernanceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingGovernanceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(TrainingGovernance.class
- -> Debería usar: TrainingGovernanceService
- Línea 187: businessService.findAllView(TrainingGovernanceMetricsSummary.class
- -> Debería usar: TrainingGovernanceMetricsSummaryService

**Servicios a usar**:
- `TrainingGovernanceMetricsSummaryService`
- `TrainingGovernanceService`

---


### TrainingInfrastructureDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingInfrastructureDetailViewModel.java`

**Problemas encontrados**:
- Línea 176: businessService.findById(TrainingInfrastructure.class
- -> Debería usar: TrainingInfrastructureService

**Servicios a usar**:
- `TrainingInfrastructureService`

---


### TrainingInfrastructureOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingInfrastructureOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(TrainingInfrastructure.class
- -> Debería usar: TrainingInfrastructureService
- Línea 187: businessService.findAllView(TrainingInfrastructureMetricsSummary.class
- -> Debería usar: TrainingInfrastructureMetricsSummaryService

**Servicios a usar**:
- `TrainingInfrastructureMetricsSummaryService`
- `TrainingInfrastructureService`

---


### TrainingLogDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingLogDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(TrainingLog.class
- -> Debería usar: TrainingLogService

**Servicios a usar**:
- `TrainingLogService`

---


### TrainingLogOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingLogOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(TrainingLog.class
- -> Debería usar: TrainingLogService
- Línea 187: businessService.findAllView(TrainingLogMetricsSummary.class
- -> Debería usar: TrainingLogMetricsSummaryService

**Servicios a usar**:
- `TrainingLogMetricsSummaryService`
- `TrainingLogService`

---


### TrainingMetricDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingMetricDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(TrainingMetric.class
- -> Debería usar: TrainingMetricService

**Servicios a usar**:
- `TrainingMetricService`

---


### TrainingMetricOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingMetricOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 250: businessService.removeFromID(TrainingMetric.class
- -> Debería usar: TrainingMetricService
- Línea 187: businessService.findAllView(TrainingMetricMetricsSummary.class
- -> Debería usar: TrainingMetricMetricsSummaryService

**Servicios a usar**:
- `TrainingMetricMetricsSummaryService`
- `TrainingMetricService`

---


### TrainingMetricsSummaryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingMetricsSummaryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(TrainingMetricsSummary.class
- -> Debería usar: TrainingMetricsSummaryService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(TrainingMetricsSummaryMetricsSummary.class
- -> Debería usar: TrainingMetricsSummaryMetricsSummaryService

**Servicios a usar**:
- `TrainingMetricsSummaryMetricsSummaryService`
- `TrainingMetricsSummaryService`

---


### TrainingOverviewOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingOverviewOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(TrainingOverview.class
- -> Debería usar: TrainingOverviewService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(TrainingOverviewMetricsSummary.class
- -> Debería usar: TrainingOverviewMetricsSummaryService

**Servicios a usar**:
- `TrainingOverviewMetricsSummaryService`
- `TrainingOverviewService`

---


### TrainingResourceUtilizationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/training/TrainingResourceUtilizationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 249: businessService.removeFromID(TrainingResourceUtilization.class
- -> Debería usar: TrainingResourceUtilizationService
- Línea 128: businessService.findAllView(
- -> Debería usar:
- Línea 186: businessService.findAllView(TrainingResourceUtilizationMetricsSummary.class
- -> Debería usar: TrainingResourceUtilizationMetricsSummaryService

**Servicios a usar**:
- `TrainingResourceUtilizationMetricsSummaryService`
- `TrainingResourceUtilizationService`

---

