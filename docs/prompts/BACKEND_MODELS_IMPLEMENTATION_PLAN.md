# PLAN DE IMPLEMENTACIÓN BACKEND - MÓDULO MODELS

**Fecha:** Diciembre 2025
**Estado:** Análisis completo - Listo para implementación
**Referencia:** `docs/prompts/cursor_front_back_models.md`

---

## 📋 RESUMEN EJECUTIVO

Este documento detalla el plan de implementación del backend para el módulo de **Model Management**, siguiendo la arquitectura establecida en `ARQUITECTURA_FRONTEND.md` y basándose en el análisis del código existente del backend.

### Estado Actual

✅ **Entidades JPA existentes:**
- `Model` - Entidad principal de modelos (tabla `MODMODELS`)
- `ModelProvider` - Proveedores de modelos (tabla `MODPROVIDERS`)
- `ProviderCredential` - Credenciales de proveedores (tabla `MODPROVIDERCREDENTIALS`)
- `ModelVersion` - Versiones de modelos (tabla `MODMODELVERSIONS`)

✅ **DTOs existentes:**
- `ModelDto` - DTO para modelos
- `ModelProviderDto` - DTO para proveedores
- `ProviderCredentialDto` - DTO para credenciales
- `ModelVersionDto` - DTO para versiones

✅ **Repositorios existentes:**
- `ModelRepository` - Repositorio básico para Model

❌ **Faltantes:**
- Repositorios para `ModelProvider`, `ProviderCredential`, `ModelVersion`
- Servicios de negocio para modelos y proveedores
- Microservicio de negocio para modelos
- Servicios y controladores en el BFF

---

## 🏗️ ARQUITECTURA DE IMPLEMENTACIÓN

### Flujo de Datos

```
Frontend (Next.js)
    ↓ HTTP
BFF (codeflowx.govern.bff.compliance) [Reactivo - WebFlux]
    ↓ HTTP/WebClient (Reactivo)
Microservicio de Negocio (codeflowx-governance-models-service) [Reactivo - WebFlux]
    ↓ Mono.fromCallable()
Servicios de Negocio (codeflowx.govern.business) [Síncrono]
    ↓ JPA Repositories
Entidades JPA (nocode.service.entitys)
```

---

## 📦 COMPONENTES A CREAR/MODIFICAR

### 1. REPOSITORIOS JPA

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/models/`

#### 1.1. ModelProviderRepository

```java
package com.codeflowx.govern.repository.models;

import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelProviderRepository extends GenericRepository<ModelProvider, Long> {

    /**
     * Busca proveedores por tipo (EXTERNAL, INTERNAL)
     */
    @Query("SELECT p FROM ModelProvider p WHERE p.modprovidertype = :type")
    List<ModelProvider> findByProviderType(@Param("type") String type);

    /**
     * Busca proveedores por estado (ACTIVE, INACTIVE, PENDING)
     */
    @Query("SELECT p FROM ModelProvider p WHERE p.modstatus = :status")
    List<ModelProvider> findByStatus(@Param("status") String status);

    /**
     * Busca proveedor por nombre
     */
    Optional<ModelProvider> findByModname(String name);
}
```

#### 1.2. ProviderCredentialRepository

```java
package com.codeflowx.govern.repository.models;

import com.codeflowx.govern.entity.models.ProviderCredential;
import com.codeflowx.govern.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderCredentialRepository extends GenericRepository<ProviderCredential, Long> {

    /**
     * Busca credenciales por proveedor
     */
    @Query("SELECT c FROM ProviderCredential c WHERE c.provider.idxmodelprovider = :providerId")
    List<ProviderCredential> findByProviderId(@Param("providerId") Long providerId);

    /**
     * Busca credenciales por proyecto (asociación opcional)
     */
    @Query("SELECT c FROM ProviderCredential c WHERE c.project.idxproject = :projectId")
    List<ProviderCredential> findByProjectId(@Param("projectId") Long projectId);

    /**
     * Busca credenciales activas por proveedor
     */
    @Query("SELECT c FROM ProviderCredential c WHERE c.provider.idxmodelprovider = :providerId AND c.modstatus = 'ACTIVE'")
    List<ProviderCredential> findActiveByProviderId(@Param("providerId") Long providerId);
}
```

#### 1.3. ModelVersionRepository

```java
package com.codeflowx.govern.repository.models;

import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelVersionRepository extends GenericRepository<ModelVersion, Long> {

    /**
     * Busca versiones por modelo
     * NOTA: ModelVersion tiene relación ManyToOne con Model a través del campo que referencia a Model
     * Revisar estructura exacta de la relación en la entidad ModelVersion
     */
    // @Query("SELECT v FROM ModelVersion v WHERE v.model.idxmodel = :modelId")
    // List<ModelVersion> findByModelId(@Param("modelId") Long modelId);

    /**
     * Busca versión por número de versión y modelo
     */
    // @Query("SELECT v FROM ModelVersion v WHERE v.model.idxmodel = :modelId AND v.modversion = :version")
    // Optional<ModelVersion> findByModelIdAndVersion(@Param("modelId") Long modelId, @Param("version") String version);

    /**
     * NOTA: Revisar estructura de ModelVersion para confirmar relación con Model
     * La relación puede estar en Model (OneToMany) o en ModelVersion (ManyToOne)
     */
}
```

**⚠️ IMPORTANTE:** La relación entre `Model` y `ModelVersion` necesita ser revisada. En la entidad `Model` hay un `OneToMany` a `ModelVersion`, pero necesitamos verificar si `ModelVersion` tiene la relación inversa.

---

### 2. SERVICIOS DE NEGOCIO

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/`

#### 2.1. ModelBusinessService

```java
package com.codeflowx.govern.business.models;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.repository.models.ModelRepository;
import com.codeflowx.govern.repository.models.ModelProviderRepository;
import com.codeflowx.govern.repository.models.ModelVersionRepository;
import com.codeflowx.govern.business.client.BpmnWorkflowClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ModelBusinessService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelProviderRepository modelProviderRepository;

    @Autowired
    private ModelVersionRepository modelVersionRepository;

    @Autowired(required = false)
    private BpmnWorkflowClient bpmnWorkflowClient;

    /**
     * Obtener todos los modelos
     */
    public List<Model> getAll() {
        log.info("Retrieving all models");
        return modelRepository.findAll();
    }

    /**
     * Obtener modelo por ID
     */
    public Optional<Model> getById(Long id) {
        log.info("Retrieving model: id={}", id);
        return modelRepository.findById(id);
    }

    /**
     * Crear nuevo modelo
     * - Genera UUID automático para modelId (campo modmodelid)
     * - Calcula versión semántica inicial (1.0.0)
     */
    public Model createModel(Model model, String createdBy) {
        log.info("Creating new model: name={}", model.getModname());

        // Generar UUID automático si no existe
        if (model.getModmodelid() == null || model.getModmodelid().isEmpty()) {
            model.setModmodelid(UUID.randomUUID().toString());
        }

        // Versión inicial
        if (model.getModversion() == null || model.getModversion().isEmpty()) {
            model.setModversion("1.0.0");
        }

        // Campos de auditoría
        model.setModcreatedby(createdBy);
        model.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        model.setModstatus(model.getModstatus() != null ? model.getModstatus() : "PENDING");

        Model saved = modelRepository.save(model);

        // Disparar workflow BPMN si es necesario
        triggerModelApprovalWorkflowIfNeeded(saved);

        log.info("Model created: id={}, modelId={}", saved.getIdxmodel(), saved.getModmodelid());
        return saved;
    }

    /**
     * Actualizar modelo
     */
    public Model updateModel(Model model, String updatedBy) {
        log.info("Updating model: id={}", model.getIdxmodel());

        model.setModupdatedby(updatedBy);
        model.setModupdatedat(new Timestamp(System.currentTimeMillis()));

        return modelRepository.save(model);
    }

    /**
     * Eliminar modelo
     */
    public void deleteModel(Long id) {
        log.info("Deleting model: id={}", id);
        modelRepository.deleteById(id);
    }

    /**
     * Buscar modelos por tipo
     */
    public List<Model> findByType(String type) {
        return modelRepository.findByType(type);
    }

    /**
     * Obtener versiones de un modelo
     */
    public List<ModelVersion> getModelVersions(Long modelId) {
        log.info("Retrieving versions for model: id={}", modelId);
        // TODO: Implementar cuando se confirme la relación ModelVersion -> Model
        // return modelVersionRepository.findByModelId(modelId);
        throw new UnsupportedOperationException("Pendiente de implementar después de revisar relación ModelVersion");
    }

    /**
     * Crear nueva versión de modelo
     * Calcula automáticamente la siguiente versión semántica
     */
    public ModelVersion createModelVersion(Long modelId, ModelVersion version, String createdBy) {
        log.info("Creating new version for model: modelId={}", modelId);

        Optional<Model> modelOpt = modelRepository.findById(modelId);
        if (modelOpt.isEmpty()) {
            throw new IllegalArgumentException("Model not found: " + modelId);
        }

        // Calcular siguiente versión semántica
        String nextVersion = calculateNextVersion(modelId, version.getModversion());
        version.setModversion(nextVersion);

        // Campos de auditoría
        version.setModcreatedby(createdBy);
        version.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        version.setModstatus(version.getModstatus() != null ? version.getModstatus() : "PENDING");

        // TODO: Asignar relación con Model cuando se confirme estructura
        // version.setModel(modelOpt.get());

        ModelVersion saved = modelVersionRepository.save(version);

        // Actualizar versión actual del modelo
        Model model = modelOpt.get();
        model.setModversion(nextVersion);
        modelRepository.save(model);

        log.info("Model version created: id={}, version={}", saved.getIdxmodelversion(), saved.getModversion());
        return saved;
    }

    /**
     * Calcular siguiente versión semántica (MAJOR.MINOR.PATCH)
     */
    private String calculateNextVersion(Long modelId, String requestedVersion) {
        // Si se especifica una versión, usarla
        if (requestedVersion != null && !requestedVersion.isEmpty()) {
            return requestedVersion;
        }

        // Obtener última versión del modelo
        List<ModelVersion> versions = getModelVersions(modelId);
        if (versions.isEmpty()) {
            return "1.0.0";
        }

        // Buscar última versión
        ModelVersion lastVersion = versions.stream()
            .max((v1, v2) -> {
                String[] v1Parts = v1.getModversion().split("\\.");
                String[] v2Parts = v2.getModversion().split("\\.");
                // Comparación simple (mejorar con semver library)
                return Integer.compare(Integer.parseInt(v1Parts[0]), Integer.parseInt(v2Parts[0]));
            })
            .orElse(null);

        if (lastVersion == null) {
            return "1.0.0";
        }

        // Incrementar patch version
        String[] parts = lastVersion.getModversion().split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        return String.format("%d.%d.%d", major, minor, patch + 1);
    }

    /**
     * Disparar workflow BPMN para aprobación de modelo
     */
    private void triggerModelApprovalWorkflowIfNeeded(Model model) {
        if (bpmnWorkflowClient == null || !bpmnWorkflowClient.isAvailable()) {
            log.warn("BpmnWorkflowClient no disponible. Workflow de aprobación no se disparará.");
            return;
        }

        try {
            Map<String, Object> variables = Map.of(
                "modelId", model.getIdxmodel(),
                "modelName", model.getModname(),
                "modelType", model.getModtype(),
                "status", model.getModstatus()
            );

            String workflowInstanceId = bpmnWorkflowClient.startProcess(
                "model-approval-workflow",
                variables
            );

            if (workflowInstanceId != null) {
                log.info("Workflow BPMN de aprobación disparado: modelId={}, workflowInstanceId={}",
                    model.getIdxmodel(), workflowInstanceId);
            }
        } catch (Exception e) {
            log.error("Error disparando workflow BPMN de aprobación para modelo: {}", model.getIdxmodel(), e);
            // No fallar la operación si el workflow falla
        }
    }
}
```

#### 2.2. ModelProviderBusinessService

```java
package com.codeflowx.govern.business.models;

import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.entity.models.ProviderCredential;
import com.codeflowx.govern.repository.models.ModelProviderRepository;
import com.codeflowx.govern.repository.models.ProviderCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ModelProviderBusinessService {

    @Autowired
    private ModelProviderRepository providerRepository;

    @Autowired
    private ProviderCredentialRepository credentialRepository;

    /**
     * Obtener todos los proveedores
     */
    public List<ModelProvider> getAll() {
        log.info("Retrieving all providers");
        return providerRepository.findAll();
    }

    /**
     * Obtener proveedor por ID
     */
    public Optional<ModelProvider> getById(Long id) {
        log.info("Retrieving provider: id={}", id);
        return providerRepository.findById(id);
    }

    /**
     * Crear nuevo proveedor
     */
    public ModelProvider createProvider(ModelProvider provider, String createdBy) {
        log.info("Creating new provider: name={}", provider.getModname());

        provider.setModcreatedby(createdBy);
        provider.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        provider.setModstatus(provider.getModstatus() != null ? provider.getModstatus() : "PENDING");

        ModelProvider saved = providerRepository.save(provider);
        log.info("Provider created: id={}, name={}", saved.getIdxmodelprovider(), saved.getModname());
        return saved;
    }

    /**
     * Actualizar proveedor
     */
    public ModelProvider updateProvider(ModelProvider provider, String updatedBy) {
        log.info("Updating provider: id={}", provider.getIdxmodelprovider());

        provider.setModupdatedby(updatedBy);
        provider.setModupdatedat(new Timestamp(System.currentTimeMillis()));

        return providerRepository.save(provider);
    }

    /**
     * Eliminar proveedor
     */
    public void deleteProvider(Long id) {
        log.info("Deleting provider: id={}", id);
        providerRepository.deleteById(id);
    }

    /**
     * Buscar proveedores por tipo
     */
    public List<ModelProvider> findByProviderType(String type) {
        return providerRepository.findByProviderType(type);
    }

    /**
     * Buscar proveedores por estado
     */
    public List<ModelProvider> findByStatus(String status) {
        return providerRepository.findByStatus(status);
    }

    /**
     * Obtener credenciales de un proveedor
     */
    public List<ProviderCredential> getProviderCredentials(Long providerId) {
        log.info("Retrieving credentials for provider: id={}", providerId);
        return credentialRepository.findByProviderId(providerId);
    }

    /**
     * Crear credencial para un proveedor
     */
    public ProviderCredential createCredential(Long providerId, ProviderCredential credential, String createdBy) {
        log.info("Creating credential for provider: providerId={}", providerId);

        Optional<ModelProvider> providerOpt = providerRepository.findById(providerId);
        if (providerOpt.isEmpty()) {
            throw new IllegalArgumentException("Provider not found: " + providerId);
        }

        credential.setProvider(providerOpt.get());
        credential.setModcreatedby(createdBy);
        credential.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        credential.setModstatus(credential.getModstatus() != null ? credential.getModstatus() : "ACTIVE");

        // Inicializar contador de uso
        if (credential.getModusagecount() == null) {
            credential.setModusagecount(0L);
        }

        ProviderCredential saved = credentialRepository.save(credential);
        log.info("Credential created: id={}, name={}", saved.getIdxprovidercredential(), saved.getModcredentialname());
        return saved;
    }

    /**
     * Eliminar credencial
     */
    public void deleteCredential(Long credentialId) {
        log.info("Deleting credential: id={}", credentialId);
        credentialRepository.deleteById(credentialId);
    }
}
```

---

### 3. MICROSERVICIO DE NEGOCIO

**Ubicación:** `codeflowx-governance-models-service/`

**Seguir plantilla de:** `codeflowx-governance-classification-service/`

#### Estructura del Microservicio

```
codeflowx-governance-models-service/
├── pom.xml
├── src/main/
│   ├── java/com/codeflowx/govern/models/
│   │   ├── ModelsServiceApplication.java
│   │   ├── controller/
│   │   │   ├── ModelController.java
│   │   │   └── ModelProviderController.java
│   │   ├── config/
│   │   │   └── WebClientConfig.java
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.yml
└── README.md
```

#### 3.1. ModelController (Microservicio)

```java
package com.codeflowx.govern.models.controller;

import com.codeflowx.govern.business.models.ModelBusinessService;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.nocode.dtos.models.ModelDto;
import com.codeflowx.govern.nocode.dtos.models.ModelVersionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Models", description = "Gestión de Modelos de IA")
public class ModelController {

    private final ModelBusinessService modelBusinessService;

    @GetMapping
    @Operation(summary = "Listar todos los modelos")
    public Mono<ResponseEntity<List<ModelDto>>> getAll() {
        return Mono.fromCallable(() -> modelBusinessService.getAll())
            .subscribeOn(Schedulers.boundedElastic())
            .map(models -> models.stream()
                .map(this::toDto)
                .collect(Collectors.toList()))
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error retrieving models", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener modelo por ID")
    public Mono<ResponseEntity<ModelDto>> getById(@PathVariable Long id) {
        return Mono.fromCallable(() -> modelBusinessService.getById(id))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(modelOpt -> {
                if (modelOpt.isEmpty()) {
                    return Mono.just(ResponseEntity.notFound().build());
                }
                return Mono.just(ResponseEntity.ok(toDto(modelOpt.get())));
            })
            .onErrorResume(error -> {
                log.error("Error retrieving model: id={}", id, error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PostMapping
    @Operation(summary = "Crear nuevo modelo")
    public Mono<ResponseEntity<ModelDto>> create(@Valid @RequestBody ModelDto dto) {
        return Mono.fromCallable(() -> {
                Model entity = toEntity(dto);
                Model saved = modelBusinessService.createModel(entity, "system"); // TODO: Obtener usuario actual
                return toDto(saved);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error creating model", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar modelo")
    public Mono<ResponseEntity<ModelDto>> update(@PathVariable Long id, @Valid @RequestBody ModelDto dto) {
        return Mono.fromCallable(() -> {
                Model entity = toEntity(dto);
                entity.setIdxmodel(id);
                Model saved = modelBusinessService.updateModel(entity, "system"); // TODO: Obtener usuario actual
                return toDto(saved);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error updating model: id={}", id, error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar modelo")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return Mono.fromCallable(() -> {
                modelBusinessService.deleteModel(id);
                return ResponseEntity.noContent().build();
            })
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(error -> {
                log.error("Error deleting model: id={}", id, error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @GetMapping("/{id}/versions")
    @Operation(summary = "Obtener versiones de un modelo")
    public Mono<ResponseEntity<List<ModelVersionDto>>> getVersions(@PathVariable Long id) {
        return Mono.fromCallable(() -> modelBusinessService.getModelVersions(id))
            .subscribeOn(Schedulers.boundedElastic())
            .map(versions -> versions.stream()
                .map(this::versionToDto)
                .collect(Collectors.toList()))
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error retrieving model versions: id={}", id, error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PostMapping("/{id}/versions")
    @Operation(summary = "Crear nueva versión de modelo")
    public Mono<ResponseEntity<ModelVersionDto>> createVersion(
            @PathVariable Long id,
            @Valid @RequestBody ModelVersionDto dto) {
        return Mono.fromCallable(() -> {
                ModelVersion version = versionToEntity(dto);
                ModelVersion saved = modelBusinessService.createModelVersion(id, version, "system");
                return versionToDto(saved);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error creating model version: modelId={}", id, error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    // Métodos de conversión DTO ↔ Entidad
    private ModelDto toDto(Model entity) {
        ModelDto dto = new ModelDto();
        dto.setIdxmodel(entity.getIdxmodel());
        dto.setModname(entity.getModname());
        dto.setModdisplayname(entity.getModdisplayname());
        dto.setModdescription(entity.getModdescription());
        dto.setModtype(entity.getModtype());
        dto.setModframework(entity.getModframework());
        dto.setModversion(entity.getModversion());
        dto.setModstatus(entity.getModstatus());
        dto.setModcurrentstage(entity.getModcurrentstage());
        // ... mapear todos los campos
        return dto;
    }

    private Model toEntity(ModelDto dto) {
        Model entity = new Model();
        entity.setIdxmodel(dto.getIdxmodel());
        entity.setModname(dto.getModname());
        entity.setModdisplayname(dto.getModdisplayname());
        entity.setModdescription(dto.getModdescription());
        entity.setModtype(dto.getModtype());
        entity.setModframework(dto.getModframework());
        entity.setModversion(dto.getModversion());
        entity.setModstatus(dto.getModstatus());
        entity.setModcurrentstage(dto.getModcurrentstage());
        // ... mapear todos los campos
        return entity;
    }

    private ModelVersionDto versionToDto(ModelVersion entity) {
        ModelVersionDto dto = new ModelVersionDto();
        dto.setIdxmodelversion(entity.getIdxmodelversion());
        dto.setModversion(entity.getModversion());
        dto.setModdescription(entity.getModdescription());
        dto.setModstatus(entity.getModstatus());
        // ... mapear todos los campos
        return dto;
    }

    private ModelVersion versionToEntity(ModelVersionDto dto) {
        ModelVersion entity = new ModelVersion();
        entity.setIdxmodelversion(dto.getIdxmodelversion());
        entity.setModversion(dto.getModversion());
        entity.setModdescription(dto.getModdescription());
        entity.setModstatus(dto.getModstatus());
        // ... mapear todos los campos
        return entity;
    }
}
```

#### 3.2. ModelProviderController (Microservicio)

Similar estructura a `ModelController`, pero para proveedores y credenciales.

---

### 4. BFF - SERVICIOS Y CONTROLADORES

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/`

#### 4.1. ModelService (Interfaz)

```java
package com.codeflowx.govern.bff.compliance.service;

import com.codeflowx.govern.nocode.dtos.models.ModelDto;
import com.codeflowx.govern.nocode.dtos.models.ModelVersionDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ModelService {
    Mono<List<ModelDto>> getAll(int page, int size, String search, String type, String status);
    Mono<ModelDto> getById(Long id);
    Mono<ModelDto> create(ModelDto dto);
    Mono<ModelDto> update(Long id, ModelDto dto);
    Mono<Void> delete(Long id);
    Mono<List<ModelVersionDto>> getVersions(Long id);
    Mono<ModelVersionDto> createVersion(Long id, ModelVersionDto dto);
    Mono<Map<String, Object>> getCosts(Long id);
    Mono<Map<String, Object>> getMetrics(Long id);
}
```

#### 4.2. ModelServiceImpl

```java
package com.codeflowx.govern.bff.compliance.service.impl;

import com.codeflowx.govern.bff.compliance.service.ModelService;
import com.codeflowx.govern.nocode.dtos.models.ModelDto;
import com.codeflowx.govern.nocode.dtos.models.ModelVersionDto;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelServiceImpl implements ModelService {

    private final WebClient webClient;
    private final CircuitBreaker modelServiceCircuitBreaker;
    private final Retry modelServiceRetry;

    @Value("${services.models.base-url}")
    private String modelsServiceBaseUrl;

    @Override
    public Mono<List<ModelDto>> getAll(int page, int size, String search, String type, String status) {
        log.debug("Listando modelos: page={}, size={}, search={}, type={}, status={}",
            page, size, search, type, status);

        StringBuilder uri = new StringBuilder(modelsServiceBaseUrl + "/api/v1/models?page=")
            .append(page).append("&size=").append(size);
        if (search != null) uri.append("&search=").append(search);
        if (type != null) uri.append("&type=").append(type);
        if (status != null) uri.append("&status=").append(status);

        return webClient.get()
            .uri(uri.toString())
            .retrieve()
            .bodyToFlux(ModelDto.class)
            .collectList()
            .transformDeferred(CircuitBreakerOperator.of(modelServiceCircuitBreaker))
            .transformDeferred(RetryOperator.of(modelServiceRetry))
            .doOnError(error -> log.error("Error listando modelos", error));
    }

    @Override
    public Mono<ModelDto> getById(Long id) {
        log.debug("Obteniendo modelo: id={}", id);
        return webClient.get()
            .uri(modelsServiceBaseUrl + "/api/v1/models/{id}", id)
            .retrieve()
            .bodyToMono(ModelDto.class)
            .transformDeferred(CircuitBreakerOperator.of(modelServiceCircuitBreaker))
            .transformDeferred(RetryOperator.of(modelServiceRetry))
            .doOnError(error -> log.error("Error obteniendo modelo: id={}", id, error));
    }

    @Override
    public Mono<ModelDto> create(ModelDto dto) {
        log.info("Creando modelo: name={}", dto.getModname());
        return webClient.post()
            .uri(modelsServiceBaseUrl + "/api/v1/models")
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(ModelDto.class)
            .transformDeferred(CircuitBreakerOperator.of(modelServiceCircuitBreaker))
            .transformDeferred(RetryOperator.of(modelServiceRetry))
            .doOnError(error -> log.error("Error creando modelo", error));
    }

    // ... implementar resto de métodos
}
```

#### 4.3. ModelController (BFF)

Similar a `FriaController`, pero para modelos.

---

### 5. CONFIGURACIÓN

#### 5.1. application.yml del BFF

Agregar en `codeflowx.govern.bff.compliance/src/main/resources/application.yml`:

```yaml
services:
  models:
    base-url: ${MODELS_SERVICE_BASE_URL:http://localhost:8101}

resilience4j:
  circuitbreaker:
    instances:
      modelService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
      modelProviderService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
  retry:
    instances:
      modelService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
      modelProviderService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
```

#### 5.2. application.yml del Microservicio

```yaml
spring:
  application:
    name: governance-models-service
  webflux:
    base-path: /
  jpa:
    hibernate:
      ddl-auto: none
server:
  port: ${SERVER_PORT:8101}
```

---

## ⚠️ PUNTOS PENDIENTES DE REVISIÓN

### 1. Relación Model ↔ ModelVersion

**Problema:** Necesita confirmarse la estructura exacta de la relación entre `Model` y `ModelVersion`.

**Acción:**
- Revisar entidad `ModelVersion` para ver si tiene `@ManyToOne` a `Model`
- O si la relación es solo desde `Model` (OneToMany)
- Confirmar campo de unión en la base de datos

### 2. UUID en Model

**Problema:** El frontend espera un campo `modelId` (UUID), pero en la entidad `Model` puede estar como `modmodelid` (String).

**Acción:**
- Verificar si existe campo `modmodelid` en la entidad `Model`
- Si no existe, considerar agregarlo o usar otro campo como identificador único

### 3. Costes y Métricas

**Problema:** El frontend espera endpoints para costes y métricas, pero estos datos pueden venir de otras entidades/tablas.

**Acción:**
- Revisar entidades `ModelMetrics`, `ModelDeployment` para obtener métricas
- Revisar si hay entidad para tracking de costes/tokens
- Implementar lógica de agregación en servicios de negocio

### 4. Proyectos Asociados

**Problema:** El frontend muestra proyectos que usan un modelo, pero la relación `Model` ↔ `Project` no está clara.

**Acción:**
- Revisar si existe relación directa o indirecta entre Model y Project
- Implementar queries para obtener proyectos que usan un modelo

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Fase 1: Repositorios
- [ ] Crear `ModelProviderRepository`
- [ ] Crear `ProviderCredentialRepository`
- [ ] Crear `ModelVersionRepository` (después de revisar relación)
- [ ] Agregar métodos de búsqueda personalizados

### Fase 2: Servicios de Negocio
- [ ] Crear `ModelBusinessService`
- [ ] Crear `ModelProviderBusinessService`
- [ ] Implementar lógica de versión semántica automática
- [ ] Implementar generación de UUID automático
- [ ] Agregar disparo de workflows BPMN

### Fase 3: Microservicio de Negocio
- [ ] Crear estructura del microservicio siguiendo plantilla
- [ ] Crear `ModelController` con todos los endpoints
- [ ] Crear `ModelProviderController` con todos los endpoints
- [ ] Implementar métodos de conversión DTO ↔ Entidad
- [ ] Configurar `application.yml`

### Fase 4: BFF
- [ ] Crear `ModelService` (interfaz)
- [ ] Crear `ModelServiceImpl` con llamadas WebClient
- [ ] Crear `ModelController` en BFF
- [ ] Crear `ModelProviderService` y `ModelProviderController`
- [ ] Configurar URLs y Resilience4j en `application.yml`

### Fase 5: Endpoints Adicionales
- [ ] Implementar endpoint de costes (`/models/{id}/costs`)
- [ ] Implementar endpoint de métricas (`/models/{id}/metrics`)
- [ ] Implementar endpoint de proyectos asociados (`/models/{id}/projects`)

### Fase 6: Testing y Documentación
- [ ] Crear tests unitarios para servicios de negocio
- [ ] Crear tests de integración para microservicio
- [ ] Documentar endpoints con Swagger
- [ ] Crear README.md del microservicio

---

## 📚 REFERENCIAS

- **Plantilla de Microservicio:** `nocode.service/codeflowx-governance-classification-service/`
- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Prompt de Frontend:** `docs/prompts/cursor_front_back_models.md`
- **Repositorios:** `codeflowx.govern.repository/README.md`
- **DTOs:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/models/`

---

**Última actualización:** Diciembre 2025
**Estado:** ✅ Análisis completo - Listo para implementación
