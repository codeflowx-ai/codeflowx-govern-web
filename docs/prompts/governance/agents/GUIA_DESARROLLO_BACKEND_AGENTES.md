# 🔧 GUÍA DE DESARROLLO BACKEND - AGENTES DE IA

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Desarrolladores Backend, AI Engineers
**Proyecto:** CodeFlowX AI Governance Platform

---

## 🎯 PROPÓSITO

Esta guía define cómo desarrollar funcionalidades backend para el módulo de agentes de IA, incluyendo arquitectura, patrones, convenciones y mejores prácticas.

---

## 🏗️ ARQUITECTURA DEL BACKEND

### Estructura de Módulos

```
nocode-service/
├── nocode.service.entitys/          # Entidades JPA
│   └── src/main/java/com/codeflowx/govern/entity/agents/
│       ├── Agent.java
│       ├── AgentApproval.java
│       ├── AgentDeployment.java
│       ├── AgentCertification.java
│       ├── AgentRetirement.java
│       └── ...
├── codeflowx.govern.repository/     # Repositorios JPA
│   └── src/main/java/com/codeflowx/govern/repository/agents/
│       ├── AgentRepository.java
│       ├── AgentApprovalRepository.java
│       └── ...
├── codeflowx.govern.nocode.dtos/    # DTOs para API
│   └── src/main/java/com/codeflowx/govern/nocode/dtos/agents/
│       ├── AgentDto.java
│       ├── AgentApprovalDto.java
│       └── ...
├── codeflowx.govern.business/       # Servicios de Negocio
│   └── src/main/java/com/codeflowx/govern/business/agents/
│       ├── AgentBusinessService.java
│       ├── AgentApprovalBusinessService.java
│       └── ...
└── codeflowx-governance-agents-service/  # Microservicio REST
    └── src/main/java/com/codeflowx/govern/agents/
        ├── controller/
        │   └── AgentController.java
        └── config/
```

### Flujo de Datos

```
Frontend (Next.js)
    ↓ HTTP Request
REST Controller (AgentController)
    ↓
Business Service (AgentBusinessService)
    ↓
Repository (AgentRepository)
    ↓
PostgreSQL Database
```

---

## 📝 CONVENCIONES DE NOMENCLATURA

### Entidades JPA

- **Nombre de clase:** PascalCase, singular (ej: `Agent`, `AgentApproval`)
- **Nombre de tabla:** Prefijo de 3 caracteres + nombre en mayúsculas (ej: `AGTAGENTS`, `AGTAPPROVALS`)
- **Campos:** Prefijo de 3 caracteres + nombre en camelCase (ej: `agtname`, `agtstatus`)

**Ejemplo:**
```java
@Entity
@Table(name = "AGTAGENTS")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxagent")
    private Long idxagent;

    @Column(name = "agtname", nullable = false, length = 255)
    private String agtname;

    @Column(name = "agtstatus")
    private String agtstatus;

    @Column(name = "agtcreatedat")
    private Timestamp agtcreatedat;

    @Column(name = "agtcreatedby")
    private String agtcreatedby;
}
```

### DTOs

- **Nombre de clase:** PascalCase + "Dto" (ej: `AgentDto`, `AgentApprovalDto`)
- **Campos:** camelCase sin prefijo (ej: `name`, `status`)

**Ejemplo:**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {
    private Long id;
    private String uuid;
    private String name;
    private String description;
    private String status;
    private String version;
    private String domain;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
}
```

### Repositorios

- **Nombre de interfaz:** PascalCase + "Repository" (ej: `AgentRepository`)
- **Extiende:** `GenericRepository<T, ID>` o `JpaRepository<T, ID>`

**Ejemplo:**
```java
@Repository
public interface AgentRepository extends GenericRepository<Agent, Long> {
    List<Agent> findByAgtstatus(String status);
    List<Agent> findByAgtnameContainingIgnoreCase(String name);
    Optional<Agent> findByAgtuuid(String uuid);
}
```

### Servicios de Negocio

- **Nombre de clase:** PascalCase + "BusinessService" (ej: `AgentBusinessService`)
- **Anotación:** `@Service`
- **Métodos:** camelCase, descriptivos (ej: `save`, `getById`, `search`)

**Ejemplo:**
```java
@Service
@Slf4j
public class AgentBusinessService {

    @Autowired
    private AgentRepository agentRepository;

    public Agent save(Agent agent) {
        // Lógica de negocio
    }

    public Agent getById(Long id) {
        // Lógica de negocio
    }
}
```

### Controllers REST

- **Nombre de clase:** PascalCase + "Controller" (ej: `AgentController`)
- **Anotación:** `@RestController`
- **Request Mapping:** `/api/v1/agents/{resource}`
- **Métodos:** camelCase, verbos HTTP (ej: `listAgents`, `createAgent`, `updateAgent`)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
@Slf4j
public class AgentController {

    private final AgentBusinessService agentBusinessService;

    @GetMapping("/registry/list")
    public Mono<ResponseEntity<AgentListResponseDto>> listAgents(...) {
        // Implementación
    }
}
```

---

## 🔨 DESARROLLO PASO A PASO

### 1. Crear Entidad JPA

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/agents/`

**Template:**
```java
package com.codeflowx.govern.entity.agents;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Table(name = "AGTMYENTITY")
@Data
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxmyentity")
    private Long idxmyentity;

    @Column(name = "myeagentuuid", nullable = false, length = 36)
    private String myeagentuuid;

    @Column(name = "myename", nullable = false, length = 255)
    private String myename;

    @Column(name = "myestatus")
    private String myestatus;

    @Column(name = "myecreatedat")
    private Timestamp myecreatedat;

    @Column(name = "myecreatedby", length = 100)
    private String myecreatedby;

    @Column(name = "myeupdatedat")
    private Timestamp myeupdatedat;

    @Column(name = "myeupdatedby", length = 100)
    private String myeupdatedby;
}
```

**Reglas:**
- Prefijo de tabla: 3 caracteres (ej: `AGT` para agents, `PRJ` para projects)
- Prefijo de campos: 3 caracteres (ej: `mye` para MyEntity)
- Siempre incluir campos de auditoría: `createdat`, `createdby`, `updatedat`, `updatedby`
- Usar `@Data` de Lombok para getters/setters
- Usar `Timestamp` para fechas

---

### 2. Crear Repositorio

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/agents/`

**Template:**
```java
package com.codeflowx.govern.repository.agents;

import com.codeflowx.govern.entity.agents.MyEntity;
import com.codeflowx.govern.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyEntityRepository extends GenericRepository<MyEntity, Long> {

    // Búsquedas básicas
    List<MyEntity> findByMyeagentuuid(String agentUuid);
    List<MyEntity> findByMyestatus(String status);
    Optional<MyEntity> findByMyeagentuuidAndMyestatus(String agentUuid, String status);

    // Búsquedas con LIKE
    List<MyEntity> findByMyenameContainingIgnoreCase(String name);

    // Consultas personalizadas
    @Query("SELECT e FROM MyEntity e WHERE e.myeagentuuid = :agentUuid AND e.mytestatus = :status")
    List<MyEntity> findActiveByAgent(@Param("agentUuid") String agentUuid, @Param("status") String status);
}
```

**Reglas:**
- Extender `GenericRepository<T, ID>` o `JpaRepository<T, ID>`
- Usar nombres descriptivos para métodos
- Usar `@Query` para consultas complejas
- Usar `@Param` para parámetros en `@Query`

---

### 3. Crear DTO

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/agents/`

**Template:**
```java
package com.codeflowx.govern.nocode.dtos.agents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyEntityDto {
    private Long id;
    private String agentUuid;
    private String agentName;
    private String name;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
```

**Reglas:**
- Usar `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` de Lombok
- Campos en camelCase sin prefijo
- Usar `LocalDateTime` para fechas en DTOs
- Incluir campos de auditoría si son relevantes para el frontend

---

### 4. Crear Servicio de Negocio

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/agents/`

**Template:**
```java
package com.codeflowx.govern.business.agents;

import com.codeflowx.govern.entity.agents.MyEntity;
import com.codeflowx.govern.repository.agents.MyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MyEntityBusinessService {

    @Autowired
    private MyEntityRepository myEntityRepository;

    /**
     * Obtiene todas las entidades.
     */
    public List<MyEntity> getAll() {
        log.info("Retrieving all entities");
        List<MyEntity> entities = myEntityRepository.findAll();
        log.info("Retrieved {} entities", entities.size());
        return entities;
    }

    /**
     * Obtiene una entidad por ID.
     */
    public MyEntity getById(Long id) {
        log.info("Retrieving entity: id={}", id);
        Optional<MyEntity> entityOpt = myEntityRepository.findById(id);
        if (entityOpt.isEmpty()) {
            log.warn("Entity not found: id={}", id);
            return null;
        }
        return entityOpt.get();
    }

    /**
     * Crea una nueva entidad.
     */
    public MyEntity save(MyEntity entity) {
        log.info("Saving entity: name={}", entity.getMyename());

        // Si es nueva entidad, establecer timestamps
        if (entity.getIdxmyentity() == null) {
            entity.setMyecreatedat(new Timestamp(System.currentTimeMillis()));
            if (entity.getMyecreatedby() == null || entity.getMyecreatedby().isEmpty()) {
                entity.setMyecreatedby("system");
            }
        }

        entity.setMyeupdatedat(new Timestamp(System.currentTimeMillis()));
        if (entity.getMyeupdatedby() == null || entity.getMyeupdatedby().isEmpty()) {
            entity.setMyeupdatedby("system");
        }

        MyEntity saved = myEntityRepository.save(entity);
        log.info("Entity saved: id={}, name={}", saved.getIdxmyentity(), saved.getMyename());
        return saved;
    }

    /**
     * Actualiza una entidad existente.
     */
    public MyEntity update(Long id, MyEntity entity) {
        log.info("Updating entity: id={}", id);

        Optional<MyEntity> existingOpt = myEntityRepository.findById(id);
        if (existingOpt.isEmpty()) {
            log.warn("Entity not found for update: id={}", id);
            throw new IllegalArgumentException("Entity not found: " + id);
        }

        MyEntity existing = existingOpt.get();

        // Actualizar campos
        existing.setMyename(entity.getMyename());
        existing.setMyestatus(entity.getMyestatus());
        existing.setMyeupdatedat(new Timestamp(System.currentTimeMillis()));
        existing.setMyeupdatedby(entity.getMyeupdatedby() != null ? entity.getMyeupdatedby() : "system");

        MyEntity updated = myEntityRepository.save(existing);
        log.info("Entity updated: id={}", updated.getIdxmyentity());
        return updated;
    }

    /**
     * Elimina una entidad.
     */
    public void delete(Long id) {
        log.info("Deleting entity: id={}", id);
        myEntityRepository.deleteById(id);
        log.info("Entity deleted: id={}", id);
    }

    /**
     * Búsqueda con filtros.
     */
    public List<MyEntity> search(String status, String search) {
        log.info("Searching entities: status={}, search={}", status, search);

        if (status != null && !status.isEmpty()) {
            if (search != null && !search.isEmpty()) {
                return myEntityRepository.findByMyestatusAndMyenameContainingIgnoreCase(status, search);
            }
            return myEntityRepository.findByMyestatus(status);
        }

        if (search != null && !search.isEmpty()) {
            return myEntityRepository.findByMyenameContainingIgnoreCase(search);
        }

        return myEntityRepository.findAll();
    }
}
```

**Reglas:**
- Usar `@Service` y `@Slf4j`
- Inyectar repositorios con `@Autowired`
- Siempre establecer timestamps de auditoría
- Validar existencia antes de actualizar
- Usar logging para operaciones importantes
- Lanzar excepciones descriptivas

---

### 5. Crear Controller REST

**Ubicación:** `codeflowx-governance-agents-service/src/main/java/com/codeflowx/govern/agents/controller/`

**Template:**
```java
package com.codeflowx.govern.agents.controller;

import com.codeflowx.govern.business.agents.MyEntityBusinessService;
import com.codeflowx.govern.entity.agents.MyEntity;
import com.codeflowx.govern.nocode.dtos.agents.MyEntityDto;
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
@RequestMapping("/api/v1/agents/my-entity")
@Tag(name = "My Entity", description = "Operaciones para gestión de MyEntity")
@RequiredArgsConstructor
@Slf4j
public class MyEntityController {

    private final MyEntityBusinessService myEntityBusinessService;

    @GetMapping("/list")
    @Operation(summary = "Listar entidades", description = "Obtiene una lista de entidades con filtros.")
    public Mono<ResponseEntity<List<MyEntityDto>>> listEntities(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {

        log.info("Request to list entities: status={}, search={}", status, search);

        return Mono.fromCallable(() -> {
                List<MyEntity> entities = myEntityBusinessService.search(status, search);
                List<MyEntityDto> dtos = entities.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
                return dtos;
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error listing entities", error);
                return Mono.just(ResponseEntity.<List<MyEntityDto>>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entidad", description = "Obtiene una entidad por ID.")
    public Mono<ResponseEntity<MyEntityDto>> getEntity(@PathVariable Long id) {
        log.info("Request to get entity: id={}", id);

        return Mono.fromCallable(() -> {
                MyEntity entity = myEntityBusinessService.getById(id);
                if (entity == null) {
                    return null;
                }
                return toDto(entity);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(dto -> {
                if (dto == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(dto);
            })
            .onErrorResume(error -> {
                log.error("Error getting entity", error);
                return Mono.just(ResponseEntity.<MyEntityDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PostMapping
    @Operation(summary = "Crear entidad", description = "Crea una nueva entidad.")
    public Mono<ResponseEntity<MyEntityDto>> createEntity(@Valid @RequestBody MyEntityDto dto) {
        log.info("Request to create entity: name={}", dto.getName());

        return Mono.fromCallable(() -> {
                MyEntity entity = toEntity(dto);
                MyEntity saved = myEntityBusinessService.save(entity);
                return toDto(saved);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
            .onErrorResume(error -> {
                log.error("Error creating entity", error);
                return Mono.just(ResponseEntity.<MyEntityDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar entidad", description = "Actualiza una entidad existente.")
    public Mono<ResponseEntity<MyEntityDto>> updateEntity(
            @PathVariable Long id,
            @Valid @RequestBody MyEntityDto dto) {
        log.info("Request to update entity: id={}", id);

        return Mono.fromCallable(() -> {
                MyEntity entity = toEntity(dto);
                MyEntity updated = myEntityBusinessService.update(id, entity);
                return toDto(updated);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error updating entity", error);
                if (error instanceof IllegalArgumentException) {
                    return Mono.just(ResponseEntity.<MyEntityDto>notFound().build());
                }
                return Mono.just(ResponseEntity.<MyEntityDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entidad", description = "Elimina una entidad.")
    public Mono<ResponseEntity<Void>> deleteEntity(@PathVariable Long id) {
        log.info("Request to delete entity: id={}", id);

        return Mono.fromRunnable(() -> myEntityBusinessService.delete(id))
            .subscribeOn(Schedulers.boundedElastic())
            .then(Mono.just(ResponseEntity.noContent().build()))
            .onErrorResume(error -> {
                log.error("Error deleting entity", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    // Métodos de conversión
    private MyEntityDto toDto(MyEntity entity) {
        return MyEntityDto.builder()
            .id(entity.getIdxmyentity())
            .agentUuid(entity.getMyeagentuuid())
            .name(entity.getMyename())
            .status(entity.getMyestatus())
            .createdAt(entity.getMyecreatedat() != null ? entity.getMyecreatedat().toLocalDateTime() : null)
            .updatedAt(entity.getMyeupdatedat() != null ? entity.getMyeupdatedat().toLocalDateTime() : null)
            .createdBy(entity.getMyecreatedby())
            .updatedBy(entity.getMyeupdatedby())
            .build();
    }

    private MyEntity toEntity(MyEntityDto dto) {
        MyEntity entity = new MyEntity();
        if (dto.getId() != null) {
            entity.setIdxmyentity(dto.getId());
        }
        entity.setMyeagentuuid(dto.getAgentUuid());
        entity.setMyename(dto.getName());
        entity.setMyestatus(dto.getStatus());
        entity.setMyecreatedby(dto.getCreatedBy());
        entity.setMyeupdatedby(dto.getUpdatedBy());
        return entity;
    }
}
```

**Reglas:**
- Usar `@RestController` y `@RequestMapping`
- Usar `@RequiredArgsConstructor` para inyección de dependencias
- Usar `Mono` para operaciones reactivas
- Usar `Schedulers.boundedElastic()` para operaciones bloqueantes
- Manejar errores con `onErrorResume`
- Usar `@Valid` para validación de DTOs
- Documentar con Swagger (`@Operation`, `@Tag`)
- Implementar métodos de conversión `toDto` y `toEntity`

---

## 🔄 PROGRAMACIÓN REACTIVA

### Uso de Mono/Flux

El backend usa Spring WebFlux para programación reactiva. Todas las operaciones deben retornar `Mono` o `Flux`.

**Operaciones bloqueantes (JPA):**
```java
return Mono.fromCallable(() -> {
        // Operación bloqueante (JPA)
        List<Agent> agents = agentRepository.findAll();
        return agents;
    })
    .subscribeOn(Schedulers.boundedElastic())
    .map(ResponseEntity::ok);
```

**Operaciones no bloqueantes:**
```java
return webClient.get()
    .uri("/api/external-service")
    .retrieve()
    .bodyToMono(String.class)
    .map(ResponseEntity::ok);
```

---

## 🔐 VALIDACIÓN Y SEGURIDAD

### Validación de DTOs

```java
@Data
public class AgentDto {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Min(value = 0, message = "Value must be positive")
    private Integer value;
}
```

### Manejo de Errores

```java
@ControllerAdvice
public class AgentExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
```

---

## 📊 INTEGRACIÓN CON BPMN

### Lanzar Proceso BPMN

```java
@Service
public class BPMNService {

    @Autowired
    private BpmnWorkflowClient bpmnClient;

    public void launchApprovalProcess(Long approvalId, String agentUuid, String approvalType) {
        Map<String, Object> variables = Map.of(
            "approvalId", approvalId,
            "agentUuid", agentUuid,
            "approvalType", approvalType
        );

        bpmnClient.startProcess("agent-approval-v1", variables);
    }
}
```

---

## 🧪 TESTING

### Test de Repositorio

```java
@DataJpaTest
class AgentRepositoryTest {

    @Autowired
    private AgentRepository agentRepository;

    @Test
    void testFindByStatus() {
        Agent agent = new Agent();
        agent.setAgtstatus("ACTIVE");
        agentRepository.save(agent);

        List<Agent> activeAgents = agentRepository.findByAgtstatus("ACTIVE");
        assertThat(activeAgents).hasSize(1);
    }
}
```

### Test de Servicio

```java
@ExtendWith(MockitoExtension.class)
class AgentBusinessServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private AgentBusinessService agentBusinessService;

    @Test
    void testSave() {
        Agent agent = new Agent();
        agent.setAgtname("Test Agent");

        when(agentRepository.save(any(Agent.class))).thenReturn(agent);

        Agent saved = agentBusinessService.save(agent);
        assertThat(saved).isNotNull();
        assertThat(saved.getAgtname()).isEqualTo("Test Agent");
    }
}
```

### Test de Controller

```java
@WebFluxTest(AgentController.class)
class AgentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AgentBusinessService agentBusinessService;

    @Test
    void testListAgents() {
        when(agentBusinessService.getAll()).thenReturn(List.of(new Agent()));

        webTestClient.get()
            .uri("/api/v1/agents/registry/list")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(AgentDto.class);
    }
}
```

---

## 📋 CHECKLIST DE DESARROLLO

### Al Crear Nueva Funcionalidad

- [ ] Crear entidad JPA con prefijo correcto
- [ ] Crear repositorio con métodos de búsqueda necesarios
- [ ] Crear DTO con campos en camelCase
- [ ] Crear servicio de negocio con lógica de negocio
- [ ] Crear controller REST con endpoints documentados
- [ ] Implementar métodos de conversión `toDto` y `toEntity`
- [ ] Agregar validación a DTOs
- [ ] Agregar logging en operaciones importantes
- [ ] Manejar errores apropiadamente
- [ ] Escribir tests unitarios
- [ ] Escribir tests de integración
- [ ] Documentar con Swagger
- [ ] Actualizar documentación de API

---

## 🚨 MEJORES PRÁCTICAS

1. **Siempre usar programación reactiva:**
   - Usar `Mono`/`Flux` en controllers
   - Usar `Schedulers.boundedElastic()` para operaciones bloqueantes

2. **Validar datos de entrada:**
   - Usar `@Valid` en controllers
   - Agregar validaciones en DTOs

3. **Manejar errores apropiadamente:**
   - Usar `@ControllerAdvice` para manejo global
   - Retornar códigos HTTP apropiados
   - Incluir mensajes de error descriptivos

4. **Logging:**
   - Usar `@Slf4j` en todas las clases
   - Loggear operaciones importantes
   - Usar niveles apropiados (INFO, WARN, ERROR)

5. **Auditoría:**
   - Siempre establecer `createdat`, `createdby`, `updatedat`, `updatedby`
   - Obtener usuario actual del contexto de seguridad

6. **Performance:**
   - Usar paginación para listados grandes
   - Optimizar consultas JPA
   - Usar índices en base de datos

---

## 📚 REFERENCIAS

- **Spring WebFlux:** https://docs.spring.io/spring-framework/reference/web/webflux.html
- **Spring Data JPA:** https://spring.io/projects/spring-data-jpa
- **Lombok:** https://projectlombok.org/
- **Swagger/OpenAPI:** https://swagger.io/

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** ✅ Operativo
