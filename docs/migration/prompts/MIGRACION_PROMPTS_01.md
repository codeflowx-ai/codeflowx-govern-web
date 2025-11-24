# Migración de ViewModels a Servicios Dedicados - Prompts

## Información del Documento
- **Módulo**: Prompts
- **Total ViewModels**: 15
- **Documento**: 1 de 1
- **ViewModels en este documento**: 15

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


### PromptsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/prompts/PromptsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 273: businessService.removeFromID(com.codeflowx.govern.entity.prompts.Prompt.class
- -> Debería usar: com.codeflowx.govern.entity.prompts.PromptService
- Línea 126: businessService.findAllView(
- -> Debería usar:
- Línea 191: businessService.findAllView(PromptsMetricsSummary.class
- -> Debería usar: PromptsMetricsSummaryService

**Servicios a usar**:
- `PromptsMetricsSummaryService`
- `com.codeflowx.govern.entity.prompts.PromptService`

---


### PromptCostAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptCostAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptCostAnalysis.class
- -> Debería usar: PromptCostAnalysisService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptCostAnalysisMetricsSummary.class
- -> Debería usar: PromptCostAnalysisMetricsSummaryService

**Servicios a usar**:
- `PromptCostAnalysisMetricsSummaryService`
- `PromptCostAnalysisService`

---


### PromptDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptDetailViewModel.java`

**Problemas encontrados**:
- Línea 347: businessService.findAllEntity(PromptValidation.class
- -> Debería usar: PromptValidationService
- Línea 373: businessService.findAllEntity(PromptVersion.class
- -> Debería usar: PromptVersionService
- Línea 181: businessService.findById(Prompt.class
- -> Debería usar: PromptService

**Servicios a usar**:
- `PromptService`
- `PromptValidationService`
- `PromptVersionService`

---


### PromptOptimizationOpportunitiesOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptOptimizationOpportunitiesOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptOptimizationOpportunities.class
- -> Debería usar: PromptOptimizationOpportunitiesService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptOptimizationOpportunitiesMetricsSummary.class
- -> Debería usar: PromptOptimizationOpportunitiesMetricsSummaryService

**Servicios a usar**:
- `PromptOptimizationOpportunitiesMetricsSummaryService`
- `PromptOptimizationOpportunitiesService`

---


### PromptOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 266: businessService.removeFromID(Prompt.class
- -> Debería usar: PromptService
- Línea 201: businessService.findAllView(PromptMetricsSummary.class
- -> Debería usar: PromptMetricsSummaryService

**Servicios a usar**:
- `PromptMetricsSummaryService`
- `PromptService`

---


### PromptPerformanceComparisonOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptPerformanceComparisonOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptPerformanceComparison.class
- -> Debería usar: PromptPerformanceComparisonService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptPerformanceComparisonMetricsSummary.class
- -> Debería usar: PromptPerformanceComparisonMetricsSummaryService

**Servicios a usar**:
- `PromptPerformanceComparisonMetricsSummaryService`
- `PromptPerformanceComparisonService`

---


### PromptsMetricsSummaryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptsMetricsSummaryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptsMetricsSummary.class
- -> Debería usar: PromptsMetricsSummaryService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptsMetricsSummaryMetricsSummary.class
- -> Debería usar: PromptsMetricsSummaryMetricsSummaryService

**Servicios a usar**:
- `PromptsMetricsSummaryMetricsSummaryService`
- `PromptsMetricsSummaryService`

---


### PromptsOverviewOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptsOverviewOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptsOverview.class
- -> Debería usar: PromptsOverviewService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptsOverviewMetricsSummary.class
- -> Debería usar: PromptsOverviewMetricsSummaryService

**Servicios a usar**:
- `PromptsOverviewMetricsSummaryService`
- `PromptsOverviewService`

---


### PromptTestResultsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptTestResultsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptTestResults.class
- -> Debería usar: PromptTestResultsService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptTestResultsMetricsSummary.class
- -> Debería usar: PromptTestResultsMetricsSummaryService

**Servicios a usar**:
- `PromptTestResultsMetricsSummaryService`
- `PromptTestResultsService`

---


### PromptUsageStatisticsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptUsageStatisticsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptUsageStatistics.class
- -> Debería usar: PromptUsageStatisticsService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptUsageStatisticsMetricsSummary.class
- -> Debería usar: PromptUsageStatisticsMetricsSummaryService

**Servicios a usar**:
- `PromptUsageStatisticsMetricsSummaryService`
- `PromptUsageStatisticsService`

---


### PromptValidationDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptValidationDetailViewModel.java`

**Problemas encontrados**:
- Línea 172: businessService.findById(PromptValidation.class
- -> Debería usar: PromptValidationService

**Servicios a usar**:
- `PromptValidationService`

---


### PromptValidationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptValidationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 266: businessService.removeFromID(PromptValidation.class
- -> Debería usar: PromptValidationService
- Línea 201: businessService.findAllView(PromptValidationMetricsSummary.class
- -> Debería usar: PromptValidationMetricsSummaryService

**Servicios a usar**:
- `PromptValidationMetricsSummaryService`
- `PromptValidationService`

---


### PromptVersionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptVersionDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(PromptVersion.class
- -> Debería usar: PromptVersionService

**Servicios a usar**:
- `PromptVersionService`

---


### PromptVersionHistoryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptVersionHistoryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 265: businessService.removeFromID(PromptVersionHistory.class
- -> Debería usar: PromptVersionHistoryService
- Línea 130: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(PromptVersionHistoryMetricsSummary.class
- -> Debería usar: PromptVersionHistoryMetricsSummaryService

**Servicios a usar**:
- `PromptVersionHistoryMetricsSummaryService`
- `PromptVersionHistoryService`

---


### PromptVersionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/prompts/PromptVersionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 266: businessService.removeFromID(PromptVersion.class
- -> Debería usar: PromptVersionService
- Línea 201: businessService.findAllView(PromptVersionMetricsSummary.class
- -> Debería usar: PromptVersionMetricsSummaryService

**Servicios a usar**:
- `PromptVersionMetricsSummaryService`
- `PromptVersionService`

---

