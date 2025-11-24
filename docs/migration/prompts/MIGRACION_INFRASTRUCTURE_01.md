# Migración de ViewModels a Servicios Dedicados - Infrastructure

## Información del Documento
- **Módulo**: Infrastructure
- **Total ViewModels**: 31
- **Documento**: 1 de 1
- **ViewModels en este documento**: 31

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


### InfrastructureOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/infrastructure/InfrastructureOverviewViewModel.java`

**Problemas encontrados**:
- Línea 99: businessService.findAllView(
- -> Debería usar:
- Línea 150: businessService.findAllView(InfrastructureMetricsSummary.class
- -> Debería usar: InfrastructureMetricsSummaryService

**Servicios a usar**:
- `InfrastructureMetricsSummaryService`

---


### CapacityForecastOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CapacityForecastOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(CapacityForecast.class
- -> Debería usar: CapacityForecastService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(CapacityForecastMetricsSummary.class
- -> Debería usar: CapacityForecastMetricsSummaryService

**Servicios a usar**:
- `CapacityForecastMetricsSummaryService`
- `CapacityForecastService`

---


### CloudCredentialDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudCredentialDetailViewModel.java`

**Problemas encontrados**:
- Línea 174: businessService.findById(CloudCredential.class
- -> Debería usar: CloudCredentialService

**Servicios a usar**:
- `CloudCredentialService`

---


### CloudCredentialOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudCredentialOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(CloudCredential.class
- -> Debería usar: CloudCredentialService
- Línea 180: businessService.findAllView(CloudCredentialMetricsSummary.class
- -> Debería usar: CloudCredentialMetricsSummaryService

**Servicios a usar**:
- `CloudCredentialMetricsSummaryService`
- `CloudCredentialService`

---


### CloudProviderDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudProviderDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(CloudProvider.class
- -> Debería usar: CloudProviderService

**Servicios a usar**:
- `CloudProviderService`

---


### CloudProviderOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudProviderOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(CloudProvider.class
- -> Debería usar: CloudProviderService
- Línea 180: businessService.findAllView(CloudProviderMetricsSummary.class
- -> Debería usar: CloudProviderMetricsSummaryService

**Servicios a usar**:
- `CloudProviderMetricsSummaryService`
- `CloudProviderService`

---


### CloudRegionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudRegionDetailViewModel.java`

**Problemas encontrados**:
- Línea 170: businessService.findById(CloudRegion.class
- -> Debería usar: CloudRegionService

**Servicios a usar**:
- `CloudRegionService`

---


### CloudRegionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudRegionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(CloudRegion.class
- -> Debería usar: CloudRegionService
- Línea 180: businessService.findAllView(CloudRegionMetricsSummary.class
- -> Debería usar: CloudRegionMetricsSummaryService

**Servicios a usar**:
- `CloudRegionMetricsSummaryService`
- `CloudRegionService`

---


### CloudResourceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudResourceDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(CloudResource.class
- -> Debería usar: CloudResourceService

**Servicios a usar**:
- `CloudResourceService`

---


### CloudResourceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/CloudResourceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(CloudResource.class
- -> Debería usar: CloudResourceService
- Línea 180: businessService.findAllView(CloudResourceMetricsSummary.class
- -> Debería usar: CloudResourceMetricsSummaryService

**Servicios a usar**:
- `CloudResourceMetricsSummaryService`
- `CloudResourceService`

---


### GpuInstanceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/GpuInstanceDetailViewModel.java`

**Problemas encontrados**:
- Línea 176: businessService.findById(GpuInstance.class
- -> Debería usar: GpuInstanceService

**Servicios a usar**:
- `GpuInstanceService`

---


### GpuInstanceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/GpuInstanceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(GpuInstance.class
- -> Debería usar: GpuInstanceService
- Línea 180: businessService.findAllView(GpuInstanceMetricsSummary.class
- -> Debería usar: GpuInstanceMetricsSummaryService

**Servicios a usar**:
- `GpuInstanceMetricsSummaryService`
- `GpuInstanceService`

---


### InfrastructureAuditDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureAuditDetailViewModel.java`

**Problemas encontrados**:
- Línea 172: businessService.findById(InfrastructureAudit.class
- -> Debería usar: InfrastructureAuditService

**Servicios a usar**:
- `InfrastructureAuditService`

---


### InfrastructureAuditOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureAuditOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(InfrastructureAudit.class
- -> Debería usar: InfrastructureAuditService
- Línea 180: businessService.findAllView(InfrastructureAuditMetricsSummary.class
- -> Debería usar: InfrastructureAuditMetricsSummaryService

**Servicios a usar**:
- `InfrastructureAuditMetricsSummaryService`
- `InfrastructureAuditService`

---


### InfrastructureCostAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureCostAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(InfrastructureCostAnalysis.class
- -> Debería usar: InfrastructureCostAnalysisService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(InfrastructureCostAnalysisMetricsSummary.class
- -> Debería usar: InfrastructureCostAnalysisMetricsSummaryService

**Servicios a usar**:
- `InfrastructureCostAnalysisMetricsSummaryService`
- `InfrastructureCostAnalysisService`

---


### InfrastructureCostDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureCostDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(InfrastructureCost.class
- -> Debería usar: InfrastructureCostService

**Servicios a usar**:
- `InfrastructureCostService`

---


### InfrastructureCostOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureCostOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(InfrastructureCost.class
- -> Debería usar: InfrastructureCostService
- Línea 180: businessService.findAllView(InfrastructureCostMetricsSummary.class
- -> Debería usar: InfrastructureCostMetricsSummaryService

**Servicios a usar**:
- `InfrastructureCostMetricsSummaryService`
- `InfrastructureCostService`

---


### InfrastructureHealthMatrixOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureHealthMatrixOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(InfrastructureHealthMatrix.class
- -> Debería usar: InfrastructureHealthMatrixService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(InfrastructureHealthMatrixMetricsSummary.class
- -> Debería usar: InfrastructureHealthMatrixMetricsSummaryService

**Servicios a usar**:
- `InfrastructureHealthMatrixMetricsSummaryService`
- `InfrastructureHealthMatrixService`

---


### InfrastructureMetricDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureMetricDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(InfrastructureMetric.class
- -> Debería usar: InfrastructureMetricService

**Servicios a usar**:
- `InfrastructureMetricService`

---


### InfrastructureMetricOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureMetricOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(InfrastructureMetric.class
- -> Debería usar: InfrastructureMetricService
- Línea 180: businessService.findAllView(InfrastructureMetricMetricsSummary.class
- -> Debería usar: InfrastructureMetricMetricsSummaryService

**Servicios a usar**:
- `InfrastructureMetricMetricsSummaryService`
- `InfrastructureMetricService`

---


### InfrastructureMetricsSummaryOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureMetricsSummaryOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(InfrastructureMetricsSummary.class
- -> Debería usar: InfrastructureMetricsSummaryService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(InfrastructureMetricsSummaryMetricsSummary.class
- -> Debería usar: InfrastructureMetricsSummaryMetricsSummaryService

**Servicios a usar**:
- `InfrastructureMetricsSummaryMetricsSummaryService`
- `InfrastructureMetricsSummaryService`

---


### InfrastructureOverviewOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureOverviewOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(InfrastructureOverview.class
- -> Debería usar: InfrastructureOverviewService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(InfrastructureOverviewMetricsSummary.class
- -> Debería usar: InfrastructureOverviewMetricsSummaryService

**Servicios a usar**:
- `InfrastructureOverviewMetricsSummaryService`
- `InfrastructureOverviewService`

---


### InfrastructureTemplateDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureTemplateDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(InfrastructureTemplate.class
- -> Debería usar: InfrastructureTemplateService

**Servicios a usar**:
- `InfrastructureTemplateService`

---


### InfrastructureTemplateOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/InfrastructureTemplateOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(InfrastructureTemplate.class
- -> Debería usar: InfrastructureTemplateService
- Línea 180: businessService.findAllView(InfrastructureTemplateMetricsSummary.class
- -> Debería usar: InfrastructureTemplateMetricsSummaryService

**Servicios a usar**:
- `InfrastructureTemplateMetricsSummaryService`
- `InfrastructureTemplateService`

---


### KubernetesClusterDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/KubernetesClusterDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(KubernetesCluster.class
- -> Debería usar: KubernetesClusterService

**Servicios a usar**:
- `KubernetesClusterService`

---


### KubernetesClusterOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/KubernetesClusterOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(KubernetesCluster.class
- -> Debería usar: KubernetesClusterService
- Línea 180: businessService.findAllView(KubernetesClusterMetricsSummary.class
- -> Debería usar: KubernetesClusterMetricsSummaryService

**Servicios a usar**:
- `KubernetesClusterMetricsSummaryService`
- `KubernetesClusterService`

---


### KubernetesNodeDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/KubernetesNodeDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(KubernetesNode.class
- -> Debería usar: KubernetesNodeService

**Servicios a usar**:
- `KubernetesNodeService`

---


### KubernetesNodeOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/KubernetesNodeOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(KubernetesNode.class
- -> Debería usar: KubernetesNodeService
- Línea 180: businessService.findAllView(KubernetesNodeMetricsSummary.class
- -> Debería usar: KubernetesNodeMetricsSummaryService

**Servicios a usar**:
- `KubernetesNodeMetricsSummaryService`
- `KubernetesNodeService`

---


### ResourceQuotaDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/ResourceQuotaDetailViewModel.java`

**Problemas encontrados**:
- Línea 169: businessService.findById(ResourceQuota.class
- -> Debería usar: ResourceQuotaService

**Servicios a usar**:
- `ResourceQuotaService`

---


### ResourceQuotaOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/ResourceQuotaOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(ResourceQuota.class
- -> Debería usar: ResourceQuotaService
- Línea 180: businessService.findAllView(ResourceQuotaMetricsSummary.class
- -> Debería usar: ResourceQuotaMetricsSummaryService

**Servicios a usar**:
- `ResourceQuotaMetricsSummaryService`
- `ResourceQuotaService`

---


### ResourceUtilizationDashboardOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/infrastructure/ResourceUtilizationDashboardOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(ResourceUtilizationDashboard.class
- -> Debería usar: ResourceUtilizationDashboardService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(ResourceUtilizationDashboardMetricsSummary.class
- -> Debería usar: ResourceUtilizationDashboardMetricsSummaryService

**Servicios a usar**:
- `ResourceUtilizationDashboardMetricsSummaryService`
- `ResourceUtilizationDashboardService`

---

