# Plantilla de Instrucciones para Migración de ViewModels

## Objetivo
Migrar ViewModels para que usen servicios dedicados en lugar de `businessService` directamente.

## Contexto
Se han creado 333 servicios dedicados para todas las entidades y views del sistema. Los ViewModels deben usar estos servicios en lugar de llamar directamente a `businessService`.

## Instrucciones Generales

### 1. Identificar el Servicio Correcto
- Para entidades: `EntityName` → `EntityNameService`
- Para views: `ViewName` → `ViewNameService`
- Los servicios están en: `com.codeflowx.govern.service.*`

### 2. Inyectar el Servicio
```java
@WireVariable
private EntityNameService entityNameService;
```

### 3. Reemplazar Llamadas a businessService

#### Antes (INCORRECTO):
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

// findAllView
List<View> views = businessService.findAllView(View.class);
PageResult<View> result = businessService.findAllView(View.class, pageParams, criterias);
```

#### Después (CORRECTO):
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

// findAllView → findAll (para views)
List<View> views = viewService.findAll();
PageResult<View> result = viewService.findAll(pageParams, criterias);
```

### 4. Manejo de Excepciones
Todos los servicios lanzan `GovernanceServiceException` (checked exception):
```java
try {
    Entity entity = entityService.findById(id);
} catch (GovernanceServiceException e) {
    log.error("Error al buscar entidad", e);
    // Manejar error
}
```

### 5. Métodos Disponibles en Servicios

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

### 6. Imports Necesarios
```java
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.[module].[EntityName]Service;
```

### 7. Verificación
- ✅ Eliminar todas las referencias a `businessService.findAllEntity`, `businessService.findById`, etc.
- ✅ Verificar que los imports del servicio sean correctos
- ✅ Verificar que se manejen las excepciones `GovernanceServiceException`
- ✅ Compilar y verificar que no hay errores

## Ejemplos Completos

### Ejemplo 1: Migración Simple

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

### Ejemplo 2: ViewModel con Múltiples Servicios y Lógica de Negocio

**Antes:**
```java
@WireVariable
private BusinessService businessService;

public void loadDashboardData() {
    try {
        // Cargar modelos
        PageResult<Model> models = businessService.findAllEntity(
            Model.class,
            pageParams,
            new Criterias()
        );

        // Cargar métricas desde view
        List<ModelMetricsSummary> metrics = businessService.findAllView(
            ModelMetricsSummary.class
        );

        // Llamar a procedimiento almacenado (MANTENER businessService)
        List<Object[]> stats = businessService.executeProcedure(
            "sp_get_model_statistics",
            params
        );

        // Llamar a microservicio Python (MANTENER sin cambios)
        PythonServiceResponse response = pythonServiceClient.call(
            "/api/models/analytics",
            request
        );

        // Lógica de negocio: combinar datos
        this.dashboardData = combineData(models, metrics, stats, response);

    } catch (Exception e) {
        log.error("Error", e);
    }
}
```

**Después:**
```java
@WireVariable
private ModelService modelService;
@WireVariable
private ModelMetricsSummaryService modelMetricsSummaryService;
@WireVariable
private BusinessService businessService; // MANTENER para procedimientos
// pythonServiceClient se mantiene igual

public void loadDashboardData() {
    try {
        // Cargar modelos - USAR SERVICIO
        PageResult<Model> models = modelService.findAll(pageParams);

        // Cargar métricas desde view - USAR SERVICIO DE VIEW
        List<ModelMetricsSummary> metrics = modelMetricsSummaryService.findAll();

        // Llamar a procedimiento almacenado - MANTENER businessService
        List<Object[]> stats = businessService.executeProcedure(
            "sp_get_model_statistics",
            params
        );

        // Llamar a microservicio Python - MANTENER sin cambios
        PythonServiceResponse response = pythonServiceClient.call(
            "/api/models/analytics",
            request
        );

        // Lógica de negocio: combinar datos - MANTENER
        this.dashboardData = combineData(models, metrics, stats, response);

    } catch (GovernanceServiceException e) {
        log.error("Error al cargar datos del dashboard", e);
    } catch (Exception e) {
        log.error("Error en procedimiento o microservicio", e);
    }
}
```

### Ejemplo 3: ViewModel con Validaciones y Transformaciones

**Antes:**
```java
@WireVariable
private BusinessService businessService;

public void saveModel(Model model) {
    try {
        // Validación de negocio (MANTENER)
        if (model.getModname() == null || model.getModname().isEmpty()) {
            throw new ValidationException("El nombre es requerido");
        }

        // Transformación de datos (MANTENER)
        model.setModstatus("PENDING");
        model.setModcreatedat(new Timestamp(System.currentTimeMillis()));

        // Guardar usando businessService
        Model saved = businessService.save(model);

        // Lógica adicional después de guardar (MANTENER)
        if (saved != null) {
            notifyStakeholders(saved);
            updateRelatedEntities(saved);
        }

    } catch (Exception e) {
        log.error("Error", e);
    }
}
```

**Después:**
```java
@WireVariable
private ModelService modelService;

public void saveModel(Model model) throws GovernanceServiceException {
    try {
        // Validación de negocio (MANTENER)
        if (model.getModname() == null || model.getModname().isEmpty()) {
            throw new ValidationException("El nombre es requerido");
        }

        // Transformación de datos (MANTENER)
        model.setModstatus("PENDING");
        // Nota: El servicio maneja createdat automáticamente, pero si hay lógica específica, mantenerla

        // Guardar usando SERVICIO
        Model saved = modelService.create(model);

        // Lógica adicional después de guardar (MANTENER)
        if (saved != null) {
            notifyStakeholders(saved);
            updateRelatedEntities(saved);
        }

    } catch (GovernanceServiceException e) {
        log.error("Error al guardar modelo", e);
        throw e;
    } catch (ValidationException e) {
        log.error("Error de validación", e);
        throw e;
    }
}
```

## Notas Importantes

1. **Preservar Lógica de Negocio**: Los ViewModels pueden tener lógica de negocio compleja que debe mantenerse:
   - Llamadas a múltiples servicios relacionados
   - Uso de views para consultas complejas
   - Llamadas a procedimientos almacenados
   - Integración con microservicios Python
   - Transformaciones de datos
   - Validaciones y reglas de negocio

2. **No eliminar `businessService` completamente**:
   - Mantenerlo si se usa para procedimientos almacenados
   - Mantenerlo si se usa para entidades sin servicio dedicado
   - Mantenerlo si se usa para operaciones especiales no cubiertas por servicios

3. **Views vs Entidades**:
   - Las views solo tienen métodos `findAll()`, no tienen CRUD
   - Usar servicios de views para consultas de solo lectura

4. **Servicios Relacionados**:
   - Si un ViewModel necesita datos de múltiples entidades relacionadas, puede inyectar múltiples servicios
   - Ejemplo: `ModelService`, `ModelVersionService`, `ModelPerformanceService`

5. **Procedimientos Almacenados**:
   - Si el ViewModel llama a procedimientos almacenados, mantener `businessService` para esas llamadas
   - Los procedimientos no tienen servicios dedicados

6. **Microservicios Python**:
   - Mantener las llamadas a microservicios Python sin cambios
   - Solo migrar las llamadas a `businessService` para entidades/views

7. **Paginación**: Los métodos `findAll()` de servicios ya manejan `PageParams` y `Criterias` correctamente.

8. **Nombres de Servicios**: Verificar el nombre exacto del servicio en el paquete correspondiente.

9. **Testing**: Después de migrar, verificar que toda la funcionalidad sigue funcionando correctamente, incluyendo:
   - Llamadas a servicios relacionados
   - Uso de views
   - Procedimientos almacenados
   - Integraciones con microservicios Python
