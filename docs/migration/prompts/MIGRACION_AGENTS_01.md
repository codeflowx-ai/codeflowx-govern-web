# Migración de ViewModels a Servicios Dedicados - Agents

## Información del Documento
- **Módulo**: Agents
- **Total ViewModels**: 56
- **Documento**: 1 de 1
- **ViewModels en este documento**: 56

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


### AgentsDashboardViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/agents/AgentsDashboardViewModel.java`

**Problemas encontrados**:
- Línea 137: businessService.findAllView(
- -> Debería usar:
- Línea 171: businessService.findAllView(
- -> Debería usar:
- Línea 200: businessService.findAllView(
- -> Debería usar:
- Línea 229: businessService.findAllView(
- -> Debería usar:

**Servicios a usar**:
- *Verificar servicio en el código*

---


### AgentsDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/govern/viewmodel/agents/AgentsDetailViewModel.java`

**Problemas encontrados**:
- Línea 452: businessService.findAllView(
- -> Debería usar:
- Línea 483: businessService.findAllView(
- -> Debería usar:

**Servicios a usar**:
- *Verificar servicio en el código*

---


### AgentAlertDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentAlertDetailViewModel.java`

**Problemas encontrados**:
- Línea 167: businessService.findById(AgentAlert.class
- -> Debería usar: AgentAlertService

**Servicios a usar**:
- `AgentAlertService`

---


### AgentAlertOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentAlertOverviewViewModel.java`

**Problemas encontrados**:
- Línea 133: businessService.findAllEntity(
- -> Debería usar:
- Línea 282: businessService.removeFromID(AgentAlert.class
- -> Debería usar: AgentAlertService
- Línea 215: businessService.findAllView(AgentAlertMetricsSummary.class
- -> Debería usar: AgentAlertMetricsSummaryService

**Servicios a usar**:
- `AgentAlertMetricsSummaryService`
- `AgentAlertService`

---


### AgentApprovalDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentApprovalDetailViewModel.java`

**Problemas encontrados**:
- Línea 180: businessService.findById(AgentApproval.class
- -> Debería usar: AgentApprovalService

**Servicios a usar**:
- `AgentApprovalService`

---


### AgentApprovalOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentApprovalOverviewViewModel.java`

**Problemas encontrados**:
- Línea 132: businessService.findAllEntity(
- -> Debería usar:
- Línea 275: businessService.removeFromID(AgentApproval.class
- -> Debería usar: AgentApprovalService
- Línea 209: businessService.findAllView(AgentApprovalMetricsSummary.class
- -> Debería usar: AgentApprovalMetricsSummaryService

**Servicios a usar**:
- `AgentApprovalMetricsSummaryService`
- `AgentApprovalService`

---


### AgentBiasDetectionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentBiasDetectionDetailViewModel.java`

**Problemas encontrados**:
- Línea 181: businessService.findById(AgentBiasDetection.class
- -> Debería usar: AgentBiasDetectionService

**Servicios a usar**:
- `AgentBiasDetectionService`

---


### AgentBiasDetectionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentBiasDetectionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 133: businessService.findAllEntity(
- -> Debería usar:
- Línea 283: businessService.removeFromID(AgentBiasDetection.class
- -> Debería usar: AgentBiasDetectionService
- Línea 216: businessService.findAllView(AgentBiasDetectionMetricsSummary.class
- -> Debería usar: AgentBiasDetectionMetricsSummaryService

**Servicios a usar**:
- `AgentBiasDetectionMetricsSummaryService`
- `AgentBiasDetectionService`

---


### AgentCollaborationDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentCollaborationDetailViewModel.java`

**Problemas encontrados**:
- Línea 175: businessService.findById(AgentCollaboration.class
- -> Debería usar: AgentCollaborationService

**Servicios a usar**:
- `AgentCollaborationService`

---


### AgentCollaborationNetworkOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentCollaborationNetworkOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentCollaborationNetwork.class
- -> Debería usar: AgentCollaborationNetworkService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentCollaborationNetworkMetricsSummary.class
- -> Debería usar: AgentCollaborationNetworkMetricsSummaryService

**Servicios a usar**:
- `AgentCollaborationNetworkMetricsSummaryService`
- `AgentCollaborationNetworkService`

---


### AgentCollaborationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentCollaborationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 130: businessService.findAllEntity(
- -> Debería usar:
- Línea 258: businessService.removeFromID(AgentCollaboration.class
- -> Debería usar: AgentCollaborationService
- Línea 194: businessService.findAllView(AgentCollaborationMetricsSummary.class
- -> Debería usar: AgentCollaborationMetricsSummaryService

**Servicios a usar**:
- `AgentCollaborationMetricsSummaryService`
- `AgentCollaborationService`

---


### AgentCommunicationDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentCommunicationDetailViewModel.java`

**Problemas encontrados**:
- Línea 179: businessService.findById(AgentCommunication.class
- -> Debería usar: AgentCommunicationService

**Servicios a usar**:
- `AgentCommunicationService`

---


### AgentCommunicationOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentCommunicationOverviewViewModel.java`

**Problemas encontrados**:
- Línea 132: businessService.findAllEntity(
- -> Debería usar:
- Línea 274: businessService.removeFromID(AgentCommunication.class
- -> Debería usar: AgentCommunicationService
- Línea 208: businessService.findAllView(AgentCommunicationMetricsSummary.class
- -> Debería usar: AgentCommunicationMetricsSummaryService

**Servicios a usar**:
- `AgentCommunicationMetricsSummaryService`
- `AgentCommunicationService`

---


### AgentComplianceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentComplianceDetailViewModel.java`

**Problemas encontrados**:
- Línea 180: businessService.findById(AgentCompliance.class
- -> Debería usar: AgentComplianceService

**Servicios a usar**:
- `AgentComplianceService`

---


### AgentComplianceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentComplianceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 132: businessService.findAllEntity(
- -> Debería usar:
- Línea 275: businessService.removeFromID(AgentCompliance.class
- -> Debería usar: AgentComplianceService
- Línea 209: businessService.findAllView(AgentComplianceMetricsSummary.class
- -> Debería usar: AgentComplianceMetricsSummaryService

**Servicios a usar**:
- `AgentComplianceMetricsSummaryService`
- `AgentComplianceService`

---


### AgentComplianceStatusOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentComplianceStatusOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentComplianceStatus.class
- -> Debería usar: AgentComplianceStatusService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentComplianceStatusMetricsSummary.class
- -> Debería usar: AgentComplianceStatusMetricsSummaryService

**Servicios a usar**:
- `AgentComplianceStatusMetricsSummaryService`
- `AgentComplianceStatusService`

---


### AgentDecisionAuditOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDecisionAuditOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentDecisionAudit.class
- -> Debería usar: AgentDecisionAuditService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentDecisionAuditMetricsSummary.class
- -> Debería usar: AgentDecisionAuditMetricsSummaryService

**Servicios a usar**:
- `AgentDecisionAuditMetricsSummaryService`
- `AgentDecisionAuditService`

---


### AgentDecisionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDecisionDetailViewModel.java`

**Problemas encontrados**:
- Línea 175: businessService.findById(AgentDecision.class
- -> Debería usar: AgentDecisionService

**Servicios a usar**:
- `AgentDecisionService`

---


### AgentDecisionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDecisionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 130: businessService.findAllEntity(
- -> Debería usar:
- Línea 259: businessService.removeFromID(AgentDecision.class
- -> Debería usar: AgentDecisionService
- Línea 195: businessService.findAllView(AgentDecisionMetricsSummary.class
- -> Debería usar: AgentDecisionMetricsSummaryService

**Servicios a usar**:
- `AgentDecisionMetricsSummaryService`
- `AgentDecisionService`

---


### AgentDeploymentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDeploymentDetailViewModel.java`

**Problemas encontrados**:
- Línea 336: businessService.findAllEntity(AgentRollback.class
- -> Debería usar: AgentRollbackService
- Línea 177: businessService.findById(AgentDeployment.class
- -> Debería usar: AgentDeploymentService

**Servicios a usar**:
- `AgentDeploymentService`
- `AgentRollbackService`

---


### AgentDeploymentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDeploymentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 251: businessService.removeFromID(AgentDeployment.class
- -> Debería usar: AgentDeploymentService
- Línea 188: businessService.findAllView(AgentDeploymentMetricsSummary.class
- -> Debería usar: AgentDeploymentMetricsSummaryService

**Servicios a usar**:
- `AgentDeploymentMetricsSummaryService`
- `AgentDeploymentService`

---


### AgentDeploymentStatusOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDeploymentStatusOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentDeploymentStatus.class
- -> Debería usar: AgentDeploymentStatusService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentDeploymentStatusMetricsSummary.class
- -> Debería usar: AgentDeploymentStatusMetricsSummaryService

**Servicios a usar**:
- `AgentDeploymentStatusMetricsSummaryService`
- `AgentDeploymentStatusService`

---


### AgentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDetailViewModel.java`

**Problemas encontrados**:
- Línea 402: businessService.findAllEntity(AgentAlert.class
- -> Debería usar: AgentAlertService
- Línea 428: businessService.findAllEntity(AgentApproval.class
- -> Debería usar: AgentApprovalService
- Línea 454: businessService.findAllEntity(AgentBiasDetection.class
- -> Debería usar: AgentBiasDetectionService
- Línea 480: businessService.findAllEntity(AgentCollaboration.class
- -> Debería usar: AgentCollaborationService
- Línea 506: businessService.findAllEntity(AgentCollaboration.class
- -> Debería usar: AgentCollaborationService
- ... y 32 más

**Servicios a usar**:
- `AgentAlertService`
- `AgentApprovalService`
- `AgentBiasDetectionService`
- `AgentCollaborationService`
- `AgentCommunicationService`
- `AgentComplianceService`
- `AgentDecisionService`
- `AgentDeploymentService`
- `AgentEthicsAssessmentService`
- `AgentExpertiseService`
- `AgentGovernanceService`
- `AgentHealthService`
- `AgentInteractionService`
- `AgentMonitoringService`
- `AgentRollbackService`
- `AgentService`
- `AgentToolService`
- `AgentTransparencyService`
- `AgentVersionService`

---


### AgentDomainDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDomainDetailViewModel.java`

**Problemas encontrados**:
- Línea 402: businessService.findAllEntity(AgentExpertise.class
- -> Debería usar: AgentExpertiseService
- Línea 184: businessService.findById(AgentDomain.class
- -> Debería usar: AgentDomainService

**Servicios a usar**:
- `AgentDomainService`
- `AgentExpertiseService`

---


### AgentDomainOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentDomainOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 266: businessService.removeFromID(AgentDomain.class
- -> Debería usar: AgentDomainService
- Línea 201: businessService.findAllView(AgentDomainMetricsSummary.class
- -> Debería usar: AgentDomainMetricsSummaryService

**Servicios a usar**:
- `AgentDomainMetricsSummaryService`
- `AgentDomainService`

---


### AgentErrorAnalysisOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentErrorAnalysisOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentErrorAnalysis.class
- -> Debería usar: AgentErrorAnalysisService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentErrorAnalysisMetricsSummary.class
- -> Debería usar: AgentErrorAnalysisMetricsSummaryService

**Servicios a usar**:
- `AgentErrorAnalysisMetricsSummaryService`
- `AgentErrorAnalysisService`

---


### AgentEthicsAssessmentDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentEthicsAssessmentDetailViewModel.java`

**Problemas encontrados**:
- Línea 177: businessService.findById(AgentEthicsAssessment.class
- -> Debería usar: AgentEthicsAssessmentService

**Servicios a usar**:
- `AgentEthicsAssessmentService`

---


### AgentEthicsAssessmentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentEthicsAssessmentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 267: businessService.removeFromID(AgentEthicsAssessment.class
- -> Debería usar: AgentEthicsAssessmentService
- Línea 202: businessService.findAllView(AgentEthicsAssessmentMetricsSummary.class
- -> Debería usar: AgentEthicsAssessmentMetricsSummaryService

**Servicios a usar**:
- `AgentEthicsAssessmentMetricsSummaryService`
- `AgentEthicsAssessmentService`

---


### AgentExpertiseDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentExpertiseDetailViewModel.java`

**Problemas encontrados**:
- Línea 181: businessService.findById(AgentExpertise.class
- -> Debería usar: AgentExpertiseService

**Servicios a usar**:
- `AgentExpertiseService`

---


### AgentExpertiseOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentExpertiseOverviewViewModel.java`

**Problemas encontrados**:
- Línea 132: businessService.findAllEntity(
- -> Debería usar:
- Línea 274: businessService.removeFromID(AgentExpertise.class
- -> Debería usar: AgentExpertiseService
- Línea 208: businessService.findAllView(AgentExpertiseMetricsSummary.class
- -> Debería usar: AgentExpertiseMetricsSummaryService

**Servicios a usar**:
- `AgentExpertiseMetricsSummaryService`
- `AgentExpertiseService`

---


### AgentGovernanceDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentGovernanceDetailViewModel.java`

**Problemas encontrados**:
- Línea 180: businessService.findById(AgentGovernance.class
- -> Debería usar: AgentGovernanceService

**Servicios a usar**:
- `AgentGovernanceService`

---


### AgentGovernanceOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentGovernanceOverviewViewModel.java`

**Problemas encontrados**:
- Línea 132: businessService.findAllEntity(
- -> Debería usar:
- Línea 275: businessService.removeFromID(AgentGovernance.class
- -> Debería usar: AgentGovernanceService
- Línea 209: businessService.findAllView(AgentGovernanceMetricsSummary.class
- -> Debería usar: AgentGovernanceMetricsSummaryService

**Servicios a usar**:
- `AgentGovernanceMetricsSummaryService`
- `AgentGovernanceService`

---


### AgentHealthDashboardOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentHealthDashboardOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentHealthDashboard.class
- -> Debería usar: AgentHealthDashboardService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentHealthDashboardMetricsSummary.class
- -> Debería usar: AgentHealthDashboardMetricsSummaryService

**Servicios a usar**:
- `AgentHealthDashboardMetricsSummaryService`
- `AgentHealthDashboardService`

---


### AgentHealthDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentHealthDetailViewModel.java`

**Problemas encontrados**:
- Línea 175: businessService.findById(AgentHealth.class
- -> Debería usar: AgentHealthService

**Servicios a usar**:
- `AgentHealthService`

---


### AgentHealthOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentHealthOverviewViewModel.java`

**Problemas encontrados**:
- Línea 130: businessService.findAllEntity(
- -> Debería usar:
- Línea 258: businessService.removeFromID(AgentHealth.class
- -> Debería usar: AgentHealthService
- Línea 194: businessService.findAllView(AgentHealthMetricsSummary.class
- -> Debería usar: AgentHealthMetricsSummaryService

**Servicios a usar**:
- `AgentHealthMetricsSummaryService`
- `AgentHealthService`

---


### AgentInteractionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentInteractionDetailViewModel.java`

**Problemas encontrados**:
- Línea 171: businessService.findById(AgentInteraction.class
- -> Debería usar: AgentInteractionService

**Servicios a usar**:
- `AgentInteractionService`

---


### AgentInteractionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentInteractionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(AgentInteraction.class
- -> Debería usar: AgentInteractionService
- Línea 180: businessService.findAllView(AgentInteractionMetricsSummary.class
- -> Debería usar: AgentInteractionMetricsSummaryService

**Servicios a usar**:
- `AgentInteractionMetricsSummaryService`
- `AgentInteractionService`

---


### AgentInteractionPatternsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentInteractionPatternsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentInteractionPatterns.class
- -> Debería usar: AgentInteractionPatternsService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentInteractionPatternsMetricsSummary.class
- -> Debería usar: AgentInteractionPatternsMetricsSummaryService

**Servicios a usar**:
- `AgentInteractionPatternsMetricsSummaryService`
- `AgentInteractionPatternsService`

---


### AgentMonitoringDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentMonitoringDetailViewModel.java`

**Problemas encontrados**:
- Línea 180: businessService.findById(AgentMonitoring.class
- -> Debería usar: AgentMonitoringService

**Servicios a usar**:
- `AgentMonitoringService`

---


### AgentMonitoringOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentMonitoringOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 267: businessService.removeFromID(AgentMonitoring.class
- -> Debería usar: AgentMonitoringService
- Línea 202: businessService.findAllView(AgentMonitoringMetricsSummary.class
- -> Debería usar: AgentMonitoringMetricsSummaryService

**Servicios a usar**:
- `AgentMonitoringMetricsSummaryService`
- `AgentMonitoringService`

---


### AgentOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 266: businessService.removeFromID(Agent.class
- -> Debería usar: AgentService
- Línea 201: businessService.findAllView(AgentMetricsSummary.class
- -> Debería usar: AgentMetricsSummaryService

**Servicios a usar**:
- `AgentMetricsSummaryService`
- `AgentService`

---


### AgentPerformanceMetricsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentPerformanceMetricsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentPerformanceMetrics.class
- -> Debería usar: AgentPerformanceMetricsService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentPerformanceMetricsMetricsSummary.class
- -> Debería usar: AgentPerformanceMetricsMetricsSummaryService

**Servicios a usar**:
- `AgentPerformanceMetricsMetricsSummaryService`
- `AgentPerformanceMetricsService`

---


### AgentResourceConsumptionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentResourceConsumptionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentResourceConsumption.class
- -> Debería usar: AgentResourceConsumptionService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentResourceConsumptionMetricsSummary.class
- -> Debería usar: AgentResourceConsumptionMetricsSummaryService

**Servicios a usar**:
- `AgentResourceConsumptionMetricsSummaryService`
- `AgentResourceConsumptionService`

---


### AgentRollbackDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentRollbackDetailViewModel.java`

**Problemas encontrados**:
- Línea 175: businessService.findById(AgentRollback.class
- -> Debería usar: AgentRollbackService

**Servicios a usar**:
- `AgentRollbackService`

---


### AgentRollbackOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentRollbackOverviewViewModel.java`

**Problemas encontrados**:
- Línea 130: businessService.findAllEntity(
- -> Debería usar:
- Línea 259: businessService.removeFromID(AgentRollback.class
- -> Debería usar: AgentRollbackService
- Línea 195: businessService.findAllView(AgentRollbackMetricsSummary.class
- -> Debería usar: AgentRollbackMetricsSummaryService

**Servicios a usar**:
- `AgentRollbackMetricsSummaryService`
- `AgentRollbackService`

---


### AgentToolDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentToolDetailViewModel.java`

**Problemas encontrados**:
- Línea 182: businessService.findById(AgentTool.class
- -> Debería usar: AgentToolService

**Servicios a usar**:
- `AgentToolService`

---


### AgentToolOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentToolOverviewViewModel.java`

**Problemas encontrados**:
- Línea 133: businessService.findAllEntity(
- -> Debería usar:
- Línea 283: businessService.removeFromID(AgentTool.class
- -> Debería usar: AgentToolService
- Línea 216: businessService.findAllView(AgentToolMetricsSummary.class
- -> Debería usar: AgentToolMetricsSummaryService

**Servicios a usar**:
- `AgentToolMetricsSummaryService`
- `AgentToolService`

---


### AgentTransparencyDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentTransparencyDetailViewModel.java`

**Problemas encontrados**:
- Línea 177: businessService.findById(AgentTransparency.class
- -> Debería usar: AgentTransparencyService

**Servicios a usar**:
- `AgentTransparencyService`

---


### AgentTransparencyOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentTransparencyOverviewViewModel.java`

**Problemas encontrados**:
- Línea 131: businessService.findAllEntity(
- -> Debería usar:
- Línea 267: businessService.removeFromID(AgentTransparency.class
- -> Debería usar: AgentTransparencyService
- Línea 202: businessService.findAllView(AgentTransparencyMetricsSummary.class
- -> Debería usar: AgentTransparencyMetricsSummaryService

**Servicios a usar**:
- `AgentTransparencyMetricsSummaryService`
- `AgentTransparencyService`

---


### AgentVersionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentVersionDetailViewModel.java`

**Problemas encontrados**:
- Línea 173: businessService.findById(AgentVersion.class
- -> Debería usar: AgentVersionService

**Servicios a usar**:
- `AgentVersionService`

---


### AgentVersionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentVersionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 129: businessService.findAllEntity(
- -> Debería usar:
- Línea 251: businessService.removeFromID(AgentVersion.class
- -> Debería usar: AgentVersionService
- Línea 188: businessService.findAllView(AgentVersionMetricsSummary.class
- -> Debería usar: AgentVersionMetricsSummaryService

**Servicios a usar**:
- `AgentVersionMetricsSummaryService`
- `AgentVersionService`

---


### AgentWorkflowAnalyticsOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentWorkflowAnalyticsOverviewViewModel.java`

**Problemas encontrados**:
- Línea 241: businessService.removeFromID(AgentWorkflowAnalytics.class
- -> Debería usar: AgentWorkflowAnalyticsService
- Línea 127: businessService.findAllView(
- -> Debería usar:
- Línea 179: businessService.findAllView(AgentWorkflowAnalyticsMetricsSummary.class
- -> Debería usar: AgentWorkflowAnalyticsMetricsSummaryService

**Servicios a usar**:
- `AgentWorkflowAnalyticsMetricsSummaryService`
- `AgentWorkflowAnalyticsService`

---


### AgentWorkflowDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentWorkflowDetailViewModel.java`

**Problemas encontrados**:
- Línea 384: businessService.findAllEntity(AgentWorkflowExecution.class
- -> Debería usar: AgentWorkflowExecutionService
- Línea 181: businessService.findById(AgentWorkflow.class
- -> Debería usar: AgentWorkflowService

**Servicios a usar**:
- `AgentWorkflowExecutionService`
- `AgentWorkflowService`

---


### AgentWorkflowExecutionDetailViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentWorkflowExecutionDetailViewModel.java`

**Problemas encontrados**:
- Línea 175: businessService.findById(AgentWorkflowExecution.class
- -> Debería usar: AgentWorkflowExecutionService

**Servicios a usar**:
- `AgentWorkflowExecutionService`

---


### AgentWorkflowExecutionOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentWorkflowExecutionOverviewViewModel.java`

**Problemas encontrados**:
- Línea 128: businessService.findAllEntity(
- -> Debería usar:
- Línea 242: businessService.removeFromID(AgentWorkflowExecution.class
- -> Debería usar: AgentWorkflowExecutionService
- Línea 180: businessService.findAllView(AgentWorkflowExecutionMetricsSummary.class
- -> Debería usar: AgentWorkflowExecutionMetricsSummaryService

**Servicios a usar**:
- `AgentWorkflowExecutionMetricsSummaryService`
- `AgentWorkflowExecutionService`

---


### AgentWorkflowOverviewViewModel

**Archivo**: `src/main/java/com/codeflowx/platform/viewmodel/agents/AgentWorkflowOverviewViewModel.java`

**Problemas encontrados**:
- Línea 130: businessService.findAllEntity(
- -> Debería usar:
- Línea 259: businessService.removeFromID(AgentWorkflow.class
- -> Debería usar: AgentWorkflowService
- Línea 195: businessService.findAllView(AgentWorkflowMetricsSummary.class
- -> Debería usar: AgentWorkflowMetricsSummaryService

**Servicios a usar**:
- `AgentWorkflowMetricsSummaryService`
- `AgentWorkflowService`

---

