# 📗 Guía de Desarrollo Backend — Módulo Prompts

**Ámbito:** backend (JPA + repositorios + servicios de negocio + controllers WebFlux) del módulo **Prompts**.
**Repositorio:** `nocode-service/`
**Objetivo:** que cualquier dev pueda **entender, extender y depurar** el backend de Prompts de forma consistente (KISS, SOLID, arquitectura por capas/hexagonal en la práctica).

---

## ✅ Arquitectura (camino de llamadas)

El módulo se publica como endpoints `/api/v1/prompts/*` en el **microservicio** y también puede exponerse vía **BFF** (si se usa).

- **Microservicio de negocio (Prompts):** `nocode-service/codeflowx-governance-prompts-service`
  - Controller: `.../controller/PromptController.java` (WebFlux)
  - Business: `nocode-service/codeflowx.govern.business/.../prompts/PromptBusinessService.java` (síncrono)
  - Repos: `nocode-service/codeflowx.govern.repository/.../prompts/*Repository.java` (JPA)
  - Entidades: `nocode-service/nocode.service.entitys/.../prompts/*.java` (JPA)

- **BFF (si aplica en tu despliegue):** `nocode-service/codeflowx.govern.bff.governance`
  - Controller BFF (Prompts): `.../controller/PromptController.java`
  - Service BFF: `.../service/PromptService.java` + `.../service/impl/PromptServiceImpl.java`

> Nota: el **controller del microservicio** trabaja con **DTOs** (`codeflowx.govern.nocode.dtos`) y convierte a entidades internamente (patrón recomendado y consistente con `ARQUITECTURA_FRONTEND.md`).

---

## 📦 Modelo de datos (JPA)

### Tablas principales (prefijo `PRM*`)

- **`PRMPROMPTS`** → entidad `Prompt`
  - Archivo: `nocode-service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/prompts/Prompt.java`
  - Campos clave: `IDXPROMPT`, `PRMUUID`, `PRMNAME`, `PRMTYPE`, `PRMCATEGORY`, `PRMVERSION`, `PRMSTATUS`, `PRMCONTENT`, `PRMPARAMETERS (JSONB)`, `PRMMETADATA (JSONB)`, auditoría (`PRMCREATEDBY/AT`, `PRMUPDATEDBY/AT`)
  - Relaciones:
    - `Prompt` → `PromptVersion` (1:N) vía `subprmpromptversions`
    - `Prompt` → `PromptValidation` (1:N) vía `subprmpromptvalidations`

- **`PRMPROMPTVERSIONS`** → entidad `PromptVersion`
  - Archivo: `.../prompts/PromptVersion.java`
  - FK: `IDPRMPROMPTS0 → PRMPROMPTS.IDXPROMPT`

- **`PRMPROMPTVALIDATIONS`** → entidad `PromptValidation`
  - Archivo: `.../prompts/PromptValidation.java`
  - FK: `IDPRMPROMPTS0 → PRMPROMPTS.IDXPROMPT`

- **Relaciones M:N (con PK autonumérica identity):**
  - `PRMPROMPTAGENTS` → `PromptAgent` (`IDXPROMPTAGENT`)
  - `PRMPROMPTMODELS` → `PromptModel` (`IDXPROMPTMODEL`)
  - `PRMPROMPTRAGS` → `PromptRag` (`IDXPROMPTRAG`)
  - Archivos: `.../prompts/PromptAgent.java`, `PromptModel.java`, `PromptRag.java`

- **Aprobaciones:**
  - `PRMPROMPTAPPROVALS` → `PromptApproval` (`IDXPROMPTAPPROVAL`)
  - Archivo: `.../prompts/PromptApproval.java`

### ⚠️ Inconsistencias detectadas (importante para dev)

- **Relación Prompt→Project**:
  - En `PromptRepository` se usa `p.project.idxproject` (queries JPQL).
  - En `Prompt.java` **no aparece** el campo `project` (al menos en el estado actual del fichero), por lo que esas queries serían inválidas/rotas.
  - Acción recomendada: alinear modelo (`Prompt` debe tener `@ManyToOne Project project` si el dominio lo requiere) o corregir repositorio.

- **Tipos en validaciones:**
  - `PromptValidation.prmvalidationscore` es `BigDecimal`, pero `PromptBusinessService` setea `double`.
  - `PromptValidation.prmissuesfound` es `String (JSONB)`, pero el service setea `int`.
  - Acción recomendada: convertir tipos (ej. `BigDecimal.valueOf(score)` y `objectMapper.writeValueAsString(...)`) o ajustar entidad/DTO.

---

## 🧩 DTOs (contratos)

Todos los contratos están en:

- `nocode-service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/prompts/`

DTOs principales:
- `PromptDto`
- `PromptListResponseDto`
- `PromptVersionDto`
- `PromptValidationDto`
- `PromptVersioningOverviewResponseDto` + `PromptVersioningMetricsDto`
- `PromptValidationOverviewResponseDto` + `PromptValidationMetricsDto`
- `PromptPerformanceOverviewResponseDto` + `PromptPerformanceMetricsDto` + `PromptPerformanceItemDto`
- `PromptRelationDto`

Regla: **controllers ↔ BFF ↔ frontend hablan SOLO DTOs**, las entidades JPA **no salen**.

---

## 🌐 Endpoints del microservicio (Prompts)

Archivo fuente:
`nocode-service/codeflowx-governance-prompts-service/src/main/java/com/codeflowx/govern/prompts/controller/PromptController.java`

### CRUD
- `POST /api/v1/prompts/register`
- `GET /api/v1/prompts/{promptId}`
- `GET /api/v1/prompts?page=&size=&projectId=&status=&search=`
- `PUT /api/v1/prompts/{promptId}` (si cambia contenido: crea versión automática; si no: metadata)
- `DELETE /api/v1/prompts/{promptId}`

### Versionado
- `POST /api/v1/prompts/{promptId}/versions`
- `GET /api/v1/prompts/{promptId}/versions`
- `GET /api/v1/prompts/versioning/overview?page=&size=&search=&status=`

### Validaciones
- `POST /api/v1/prompts/{promptId}/validations`
- `GET /api/v1/prompts/{promptId}/validations`
- `GET /api/v1/prompts/validation/overview?page=&size=&search=&validationType=&status=`

### Performance
- `GET /api/v1/prompts/templates/performance?page=&size=&search=`
  - **Estado actual:** devuelve **items vacíos** y métricas en 0 (hay `TODO` explícito en controller).

### Relaciones (Agents / Models / RAG)
- Agents:
  - `POST /api/v1/prompts/{promptId}/agents/{agentId}?createdBy=...`
  - `DELETE /api/v1/prompts/{promptId}/agents/{agentId}`
  - `GET /api/v1/prompts/{promptId}/agents`
- Models:
  - `POST /api/v1/prompts/{promptId}/models/{modelId}?createdBy=...`
  - `DELETE /api/v1/prompts/{promptId}/models/{modelId}`
  - `GET /api/v1/prompts/{promptId}/models`
- RAG:
  - `POST /api/v1/prompts/{promptId}/rags/{ragSystemId}?createdBy=...`
  - `DELETE /api/v1/prompts/{promptId}/rags/{ragSystemId}`
  - `GET /api/v1/prompts/{promptId}/rags`

### Análisis/Evaluación (integración con microservicios Python vía `AIGovernanceClient`)
- `POST /api/v1/prompts/{promptId}/analyze?createdBy=...` → crea validación tipo `COMPLIANCE`
- `POST /api/v1/prompts/{promptId}/evaluate?createdBy=...` → crea validación tipo `PERFORMANCE`

### Testing (ejecución contra modelos)
- `POST /api/v1/prompts/{promptId}/test`
- `POST /api/v1/prompts/{promptId}/test/compare`

⚠️ Nota de arquitectura: en `ARQUITECTURA_FRONTEND.md` se recomienda **no retornar `Map<String,Object>`** directamente. Aquí se retorna `Map` para test/compare. Si se quiere alinear, crear DTO específico (ej. `PromptTestResponseDto` / `PromptCompareResponseDto`).

---

## 🧠 Servicio de negocio (PromptBusinessService)

Archivo: `nocode-service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/prompts/PromptBusinessService.java`

Responsabilidades:
- Validación de nombre único + generación UUID + versión inicial.
- Update “metadata-only” vs “content-changed” (creación de versión).
- Persistencia de versiones/validaciones.
- Gestión de relaciones M:N (agent/model/rag) con repositorios de relación.
- Integración con `AIGovernanceClient` para:
  - Safety + Effectiveness → compliance
  - LLMQuality + CostEfficiency → performance
- Resiliencia: anotaciones Resilience4j `@CircuitBreaker`, `@Retry`, `@TimeLimiter` en métodos síncronos de análisis/evaluación.

---

## ⚙️ Repositorios JPA

Directorio: `nocode-service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/prompts/`

Principales:
- `PromptRepository`
- `PromptVersionRepository`
- `PromptValidationRepository`
- `PromptAgentRepository`
- `PromptModelRepository`
- `PromptRagRepository`

Regla KISS: usar JPQL simple en repositorios; si se requiere SQL nativo, preferir `JdbcTemplate` **solo** en casos necesarios (según `ARQUITECTURA_FRONTEND.md`).

---

## 🔧 BFF (si se usa en tu despliegue)

### Implementación
- Service interface: `nocode-service/codeflowx.govern.bff.governance/.../service/PromptService.java`
- Impl: `.../service/impl/PromptServiceImpl.java`

`PromptServiceImpl` construye URIs hacia `/api/v1/prompts/...` y aplica:
- `CircuitBreakerOperator.of(promptsServiceCircuitBreaker)`
- `RetryOperator.of(promptsServiceRetry)`

### Configuración
Archivo: `nocode-service/codeflowx.govern.bff.governance/src/main/resources/application.yml`

Claves:
- `services.gateway.base-url` → `GATEWAY_BASE_URL` (por defecto `http://localhost:8080`)
- Instancias Resilience4j: `promptsService` (circuit breaker / retry)

---

## 🧪 Checklist rápido para añadir un endpoint nuevo (backend)

1. **DTO primero** (si falta): `codeflowx.govern.nocode.dtos/.../prompts/`
2. **Business method** (síncrono) en `PromptBusinessService`
3. **Repo** (si aplica) en `codeflowx.govern.repository/prompts`
4. **Controller WebFlux** (microservicio): envolver con `Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())`
5. **OpenAPI annotations** (`@Operation`, `@ApiResponses`)
6. Si el frontend usa BFF, **exponer también en BFF** (controller + service) o enrutar por gateway.

---

## 🧯 Troubleshooting (fallos típicos)

- **400 por validación**: revisa constraints en DTOs (`@NotBlank`, `@NotNull`) y que el frontend envíe los campos requeridos (`prmcreatedby`, etc.).
- **Fallo de análisis/evaluación**: `AIGovernanceClient` es `@Autowired(required=false)`; si no está disponible, el service crea validaciones “sin análisis externo”.
- **Performance overview vacío**: hoy está “by design” (hay TODO en controller).
- **JPQL rompe en runtime**: revisar desalineaciones entre entidad y repositorio (ej. relación `project`).
