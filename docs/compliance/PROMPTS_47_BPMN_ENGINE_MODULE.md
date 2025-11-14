# PROMPTS_47: REFACTORIZACIÓN BPMN – MICROSERVICIO DEDICADO

**Fecha:** 10 Noviembre 2025  
**Propósito:** Encapsular la ejecución de BPMN/Drools en un microservicio dentro de `nocode.service`, separando delegates, reglas y procesos del proyecto frontend (`suinsit.nova.web`).

---

## 🎯 Objetivo General

Crear dos módulos Maven en `nocode.service`:

1. **`codeflowx.govern.workflow.lib`** → Librería con delegados, reglas Drools y recursos BPMN.
2. **`codeflowx.govern.workflow.engine`** → Microservicio Spring Boot que carga Flowable/Drools desde la librería y expone endpoints REST para gestionar procesos.

El frontend consumirá este microservicio vía REST; no debe contener lógica de ejecución de procesos.

---

## 📦 Alcance Detallado

### 1. Módulo `codeflowx.govern.workflow.lib`

- Crear módulo Maven bajo `nocode.service` y añadirlo al `pom.xml` raíz.
- Estructura sugerida:
  ```
  src/main/java/com/codeflowx/govern/workflow/lib/
      delegates/
      services/
      config/
  src/main/resources/
      processes/
      rules/
      config/
  ```
- Mover todos los delegates Java desde `suinsit.nova.web` (`com.codeflowx.govern.workflow.delegates.*`) a este nuevo módulo, manteniendo `@Component` para inyección.
- Incluir en `processes/` los BPMN existentes (ej. `education/edu-consent-hub.bpmn20.xml`, `compliance-monitoring-v1.bpmn`, etc.).
- Incluir en `rules/` todos los `.drl` (ej. `faas-sector-governance.drl`, `education-policies.drl`).
- Preparar configuración compartida (`FlowableConfig`, `KieConfig`) para que el microservicio pueda importarla.

### 2. Módulo `codeflowx.govern.workflow.engine`

- Crear módulo Spring Boot (packaging `jar`) que dependa de `codeflowx.govern.workflow.lib`.
- Dependencias mínimas: `spring-boot-starter-web`, `flowable-spring-boot-starter`, `spring-boot-starter-actuator`, `spring-boot-starter-validation`, `spring-boot-starter-security` (auth básica opcional), `springdoc-openapi-ui` (opcional), `spring-boot-starter-data-redis` (opcional), `spring-boot-starter-cache`.
- Configurar Flowable Engine en modo standalone apuntando a la BD existente (usar propiedades externas). Asegurar creación de tablas si no existen.
- Al iniciar la aplicación (`CommandLineRunner`):
  - Escanear recursos BPMN del JAR (`classpath:processes/**/*.bpmn*`).
  - Desplegar definiciones en Flowable (versionar si ya existen).
  - Cargar reglas Drools (`classpath:rules/**/*.drl`) y preparar `KieContainer` disponible vía Spring.

#### Endpoints REST requeridos

1. `POST /api/v1/processes/deploy`
   - Entrada: `MultipartFile` (`application/xml` o `text/xml`).
   - Lógica: desplegar definición BPMN. Si existe `processDefinitionKey`, crear nueva versión. Si no, registrar nueva definición.
   - Respuesta: JSON con `processDefinitionId`, `version`, `deploymentId`.

2. `POST /api/v1/processes/{processKey}/start`
   - Entrada: JSON con variables (ej. `sector_code`, `use_case_code`, `studentAge`).
   - Lógica: invocar `runtimeService.startProcessInstanceByKey`.
   - Respuesta: `processInstanceId`, `status`.

3. `GET /api/v1/processes/{processInstanceId}`
   - Devuelve estado, variables, actividades pendientes.

4. `GET /api/v1/processes/{processKey}/definitions`
   - Lista las versiones desplegadas (`FlowableRepositoryService`).

> **Nota:** Dejar preparado endpoint `POST /api/v1/processes/deploy/batch` (TODO) para subir ZIP con múltiples procesos.

#### Integración con Drools

- Configurar `KieContainer` usando los `.drl` del módulo librería.
- Exponer un servicio (`DecisionService`) para evaluar políticas (ej. `EDU_SERVICES_POLICY_BASE_v1.0.0`).
- Delegates deben inyectar este servicio desde la librería.

---

## 🔄 Refactor en `suinsit.nova.web`

- Eliminar dependencias Flowable/Drools del frontend.
- Sustituir llamadas directas a Flowable por llamadas REST al nuevo micro.
- Ajustar `pom.xml` para eliminar referencia a delegates.
- Actualizar ViewModels/ZUL para consumir APIs (`RestTemplate`/`WebClient`).
- Documentar cambios en `PROMPTS_26_METAMODELO_SECTORIAL.md` y `PROMPTS_46_DISCOVERY_SERVING.md`.

---

## 🧪 Datos de Prueba

- BPMN `edu-concent-hub.bpmn20.xml`.
- Variables de arranque:
  ```json
  {
    "sector_code": "EDU_SERVICES",
    "use_case_code": "EDU_PROCTORING_AI",
    "studentAge": 15,
    "contentId": "CONTENT-001"
  }
  ```
- Validar despliegue versionando el proceso (`POST /deploy`) y ejecutando (`POST /{processKey}/start`).

---

## ✅ Checklist de Validación

1. ✅ Nuevos módulos añadidos en `nocode.service/pom.xml`.
2. ✅ Delegates y procesos residen en `codeflowx.govern.workflow.lib`.
3. ✅ Micro `codeflowx.govern.workflow.engine` (Spring Boot 3 + WebFlux) expone:
   - `POST /api/v1/processes/deploy`
   - `POST /api/v1/processes/{processKey}/start`
   - `GET /api/v1/processes/{processInstanceId}`
   - `GET /api/v1/processes/{processKey}/definitions`
   - `GET /api/v1/tasks` (por `assignee`/`roles`)
   - `GET /api/v1/tasks/{taskId}`
   - `POST /api/v1/tasks/{taskId}/claim|release|complete`
4. ✅ Despliegue automático de BPMN en arranque (`FlowableDeploymentConfig`) y Drools (`KieContainer`).
5. ☐ Pruebas manuales/automatizadas en curso (ejecutar procesos y tareas end-to-end).
6. ☐ Frontend (`suinsit.nova.web`) pendiente de consumir REST (Task Inbox, BPMN start) → plan en siguiente iteración.
7. ✅ Documentación actualizada (este PROMPT); revisar metamodelo/serving cuando se integre el micro.

---

## ⚠️ Consideraciones

- Mantener naming convencional en tablas (`cor_`, `gov_`, `srv_`), PK `bigserial` (`idx + nombre tabla`).
- Usar JSONB/hstore para metadatos en eventos.
- Preparar `application.yml` con configuración externa (BD, Redis, credenciales).
- Incluir pruebas (`@SpringBootTest`) que desplieguen BPMN de muestra y arranquen procesos.
- Añadir script Maven en README: `mvn -pl codeflowx.govern.workflow.engine -am install`.
