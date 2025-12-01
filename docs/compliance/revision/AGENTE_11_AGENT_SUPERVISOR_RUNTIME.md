# GUÍA AGENTE 11 - AGENT SUPERVISOR RUNTIME

**Agente:** Backend Senior - Microservicios + BPMN + Proxy
**Equipo:** Equipo 11 - Agent Supervisor
**Duración:** 24 horas
**Objetivo:** Construir Agent Supervisor Runtime con proxy/interceptor, Memory Store y Kill-Switch por políticas

---

## 📋 TAREA ASIGNADA

### **Prompt 3 — Agent Runtime & Supervisor → Proxy/Interceptor, Memory Store y Kill-Switch**

**Estado:** 🔴 PENDIENTE

**Objetivo:** Construir el Agent Supervisor Runtime que permita interceptación (proxy) opcional no-intrusiva, gestión de memorias/contextos, supervisión centralizada y mecanismos de bloqueo/safety (kill-switch) por políticas.

**Esfuerzo Estimado:** 24 horas

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompt Principal:
- **`/docs/compliance/EVOLUCION_AIOS_OVERLAY.md`** - Sección "Prompt 3 — Agent Runtime & Supervisor"

### Documentación General:
- **BPMN Delegates:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/`
- **Procesos BPMN:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/resources/bpmn/`
- **Telemetría:** `/eclipse-workspace/nocode.service/codeflowx-aios-telemetry/`
- **Agent Monitoring:** Repositorio `leka-agent-monitoring` (puerto 8005)

### Referencias Técnicas:
- **ImmutableLoggingBusinessService:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java`
- **Policy Engine (Drools):** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/resources/rules/`
- **AioComponent:** Entidad existente en `nocode.service.entitys`
- **TimescaleDB:** Base de datos `codeflowx_telemetry`
- **MinIO:** Object storage para memoria cifrada
- **Telemetría (referencia arquitectura):** `/eclipse-workspace/nocode.service/codeflowx-aios-telemetry/` - Ver cómo usa RabbitMQ
- **Telemetría Worker:** `/eclipse-workspace/nocode.service/codeflowx-aios-telemetry-worker/` - Ver cómo consume de RabbitMQ

---

## 🏗️ ARQUITECTURA - CONVENCIONES OBLIGATORIAS

### **AioPolicyEnforcementDelegate:**
- **Ubicación:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/AioPolicyEnforcementDelegate.java`
- **Framework:** JavaDelegate de Activiti/Flowable
- **Integración:** Drools Policy Engine + ImmutableLoggingBusinessService

### **Agent Supervisor Microservicio (Arquitectura Asíncrona):**
- **Microservicio REST:** `codeflowx-agent-supervisor` (puerto 8087)
  - Recibe acciones HTTP
  - Publica a RabbitMQ (cola `aios.supervisor.actions`)
  - Retorna 202 Accepted (no bloquea)
- **Worker Asíncrono:** `codeflowx-agent-supervisor-worker`
  - Consume de RabbitMQ
  - Ejecuta acciones sobre agentes
  - Actualiza estado en BD
  - Dispara BPMN si es necesario
- **Ubicación:** `/eclipse-workspace/nocode.service/codeflowx-agent-supervisor/`
- **Framework:** Spring Boot 3.2.5+ con Spring AMQP (RabbitMQ)

### **Proxy/Interceptor (aios-proxy):**
- **Tipo:** Sidecar o Reverse-Proxy
- **Tecnología:** Spring Cloud Gateway o Envoy Proxy
- **Ubicación:** Repositorio separado o módulo en proyecto existente

### **Memory Store:**
- **Postgres:** Tabla `AIO_AGENT_MEMORIES` (metadata, versionado)
- **MinIO:** Objetos cifrados (AES-256-GCM) con path estructurado
- **Tipos:** CONVERSATION, CONTEXT, KNOWLEDGE, TEMPORARY, PERSISTENT

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **AioPolicyEnforcementDelegate:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/
└── AioPolicyEnforcementDelegate.java  ← CREAR
```

### **Agent Supervisor Microservicio (REST + Worker):**
```
/eclipse-workspace/nocode.service/codeflowx-agent-supervisor/
├── pom.xml
├── src/main/java/com/codeflowx/agent/supervisor/
│   ├── AgentSupervisorApplication.java  ← Microservicio REST
│   ├── controller/
│   │   └── AgentSupervisorController.java
│   ├── service/
│   │   ├── AgentSupervisorService.java  ← Publica a RabbitMQ
│   │   ├── MemoryStoreService.java
│   │   └── impl/
│   │       └── AgentSupervisorServiceImpl.java
│   ├── dto/
│   │   ├── AgentActionRequest.java
│   │   ├── AgentActionResponse.java
│   │   ├── MemoryRequest.java
│   │   └── MemoryResponse.java
│   └── config/
│       ├── RabbitMQConfig.java  ← Configuración RabbitMQ
│       └── AgentSupervisorProperties.java
└── src/main/resources/
    └── application.yml

/eclipse-workspace/nocode.service/codeflowx-agent-supervisor-worker/
├── pom.xml
├── src/main/java/com/codeflowx/agent/supervisor/worker/
│   ├── AgentSupervisorWorkerApplication.java  ← Worker asíncrono
│   ├── listener/
│   │   └── AgentActionListener.java  ← @RabbitListener
│   ├── service/
│   │   ├── AgentActionExecutorService.java  ← Ejecuta acciones
│   │   └── impl/
│   │       └── AgentActionExecutorServiceImpl.java
│   └── config/
│       └── RabbitMQConfig.java
└── src/main/resources/
    └── application.yml
```

### **Proxy/Interceptor:**
```
codeflowx-aios-proxy/
├── pom.xml (si Spring Cloud Gateway)
├── src/main/java/.../AiosProxyApplication.java
└── src/main/resources/application.yml
```

### **Memory Store (SQL):**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/
└── agent_memories.sql  ← CREAR tabla AIO_AGENT_MEMORIES
```

---

## 🔧 IMPLEMENTACIÓN

### **Fase 1: AioPolicyEnforcementDelegate**

#### **1.1. Crear AioPolicyEnforcementDelegate.java:**
```java
package com.codeflowx.govern.workflow.delegates;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import com.codeflowx.govern.workflow.services.DroolsPolicyService;

/**
 * Delegate para ejecutar acciones de política (PAUSE, RESUME, TERMINATE, NOTIFY, CREATE_INCIDENT).
 *
 * <p>Se invoca desde procesos BPMN cuando se detecta policy violation.</p>
 *
 * <p><b>Artículo EU AI Act:</b> Art. 19 (Logging inmutable)</p>
 */
@Slf4j
@Component
public class AioPolicyEnforcementDelegate implements JavaDelegate {

    @Autowired
    private DroolsPolicyService droolsPolicyService;

    @Autowired
    private ImmutableLoggingBusinessService immutableLoggingBusinessService;

    @Override
    public void execute(DelegateExecution execution) {
        log.debug("Executing AioPolicyEnforcementDelegate for process: {}", execution.getProcessInstanceId());

        // Obtener parámetros del proceso BPMN
        String componentUuid = (String) execution.getVariable("componentUuid");
        String violationType = (String) execution.getVariable("violationType");
        String action = (String) execution.getVariable("action"); // PAUSE, RESUME, TERMINATE, etc.

        try {
            // Evaluar políticas usando Drools
            PolicyEvaluationResult result = droolsPolicyService.evaluatePolicy(
                componentUuid, violationType, action
            );

            // Ejecutar acción
            executeAction(componentUuid, action, result);

            // Registrar en ImmutableLog
            immutableLoggingBusinessService.createLog(
                componentUuid,
                "AI_POLICY_ENFORCEMENT",
                "system",
                Map.of(
                    "action", action,
                    "violation_type", violationType,
                    "result", result.getResult()
                )
            );

        } catch (Exception e) {
            log.error("Error executing policy enforcement", e);
            throw new RuntimeException("Policy enforcement failed", e);
        }
    }

    private void executeAction(String componentUuid, String action, PolicyEvaluationResult result) {
        switch (action) {
            case "PAUSE":
                // Pausar agente
                break;
            case "RESUME":
                // Reanudar agente
                break;
            case "TERMINATE":
                // Terminar agente
                break;
            case "NOTIFY":
                // Notificar humanos
                break;
            case "CREATE_INCIDENT":
                // Crear incidencia
                break;
        }
    }
}
```

### **Fase 2: Agent Supervisor Microservicio (REST + Worker)**

#### **2.1. AgentSupervisorController.java (Microservicio REST):**
```java
package com.codeflowx.agent.supervisor.controller;

import com.codeflowx.agent.supervisor.dto.*;
import com.codeflowx.agent.supervisor.service.AgentSupervisorService;
import com.codeflowx.agent.supervisor.service.MemoryStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/supervisor")
@Tag(name = "Agent Supervisor", description = "API REST para supervisión centralizada de agentes")
@RequiredArgsConstructor
public class AgentSupervisorController {

    private final AgentSupervisorService agentSupervisorService;
    private final MemoryStoreService memoryStoreService;

    @PostMapping("/{agentId}/actions")
    @Operation(summary = "Ejecutar acción sobre agente", description = "Pausa, reanuda o termina un agente. Publica a RabbitMQ para procesamiento asíncrono.")
    public ResponseEntity<AgentActionResponse> executeAction(
            @PathVariable String agentId,
            @RequestBody AgentActionRequest request) {
        // Publica a RabbitMQ y retorna 202 Accepted (no bloquea)
        AgentActionResponse response = agentSupervisorService.queueAction(agentId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/{agentId}/status")
    @Operation(summary = "Obtener estado del agente", description = "Obtiene el estado runtime del agente (consulta síncrona desde BD)")
    public ResponseEntity<AgentStatusResponse> getStatus(@PathVariable String agentId) {
        AgentStatusResponse response = agentSupervisorService.getStatus(agentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{agentId}/memory")
    @Operation(summary = "Guardar memoria", description = "Guarda memoria del agente")
    public ResponseEntity<MemoryResponse> setMemory(
            @PathVariable String agentId,
            @RequestBody MemoryRequest request) {
        MemoryResponse response = memoryStoreService.setMemory(agentId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{agentId}/memory")
    @Operation(summary = "Leer memoria", description = "Lee memoria del agente")
    public ResponseEntity<MemoryResponse> getMemory(
            @PathVariable String agentId,
            @RequestParam String namespace,
            @RequestParam String key) {
        MemoryResponse response = memoryStoreService.getMemory(agentId, namespace, key);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{agentId}/memory/snapshot")
    @Operation(summary = "Crear snapshot", description = "Crea snapshot completo de memoria")
    public ResponseEntity<SnapshotResponse> createSnapshot(@PathVariable String agentId) {
        SnapshotResponse response = memoryStoreService.createSnapshot(agentId);
        return ResponseEntity.ok(response);
    }

    // ... otros endpoints según prompt
}
```

### **Fase 3: Memory Store**

#### **3.1. Script SQL - Tabla AIO_AGENT_MEMORIES:**
```sql
-- ============================================================================
-- TABLA: AIO_AGENT_MEMORIES
-- Descripción: Almacena metadata y referencias de memoria de agentes
-- Artículo EU AI Act: Art. 19 (Logging inmutable)
-- ============================================================================

CREATE TABLE IF NOT EXISTS AIO_AGENT_MEMORIES (
    IDXAGENTMEMORY BIGSERIAL NOT NULL,
    iduuid VARCHAR(36) NOT NULL UNIQUE,
    IDXAGENT UUID NOT NULL,  -- FK a AIOCOMPONENTS (type="AGENT")

    namespace VARCHAR(50) NOT NULL,  -- CONVERSATION, CONTEXT, KNOWLEDGE, TEMPORARY, PERSISTENT
    memory_key VARCHAR(255) NOT NULL,
    version INTEGER NOT NULL DEFAULT 1,

    storage_path VARCHAR(500),  -- Path en MinIO
    encryption_key_id VARCHAR(100),  -- ID de clave de cifrado

    expires_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT PK_AIO_AGENT_MEMORIES PRIMARY KEY (IDXAGENTMEMORY),
    CONSTRAINT FK_AIO_AGENT_MEMORIES_AGENT FOREIGN KEY (IDXAGENT)
        REFERENCES AIOCOMPONENTS(iduuid) ON DELETE CASCADE,
    CONSTRAINT UQ_AIO_AGENT_MEMORIES_KEY_VERSION UNIQUE (IDXAGENT, namespace, memory_key, version)
);

-- Índices
CREATE INDEX IF NOT EXISTS IDX_AIO_AGENT_MEMORIES_AGENT
    ON AIO_AGENT_MEMORIES(IDXAGENT);
CREATE INDEX IF NOT EXISTS IDX_AIO_AGENT_MEMORIES_NAMESPACE
    ON AIO_AGENT_MEMORIES(namespace);
CREATE INDEX IF NOT EXISTS IDX_AIO_AGENT_MEMORIES_EXPIRES
    ON AIO_AGENT_MEMORIES(expires_at);

-- Comentarios
COMMENT ON TABLE AIO_AGENT_MEMORIES IS 'Metadata y referencias de memoria de agentes (contenido cifrado en MinIO)';
COMMENT ON COLUMN AIO_AGENT_MEMORIES.IDXAGENTMEMORY IS 'Primary key autonumérico';
COMMENT ON COLUMN AIO_AGENT_MEMORIES.iduuid IS 'UUID único para trazabilidad';
COMMENT ON COLUMN AIO_AGENT_MEMORIES.namespace IS 'Tipo de memoria: CONVERSATION, CONTEXT, KNOWLEDGE, TEMPORARY, PERSISTENT';
COMMENT ON COLUMN AIO_AGENT_MEMORIES.storage_path IS 'Path del objeto cifrado en MinIO';
```

#### **3.2. MemoryStoreService.java:**
```java
package com.codeflowx.agent.supervisor.service.impl;

import com.codeflowx.agent.supervisor.dto.MemoryRequest;
import com.codeflowx.agent.supervisor.dto.MemoryResponse;
import com.codeflowx.agent.supervisor.service.MemoryStoreService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoryStoreServiceImpl implements MemoryStoreService {

    private final MinioClient minioClient;
    private final BusinessService businessService;  // Para tabla AIO_AGENT_MEMORIES

    @Override
    public MemoryResponse setMemory(String agentId, String namespace, String key, Object value, Integer expiresInDays) {
        log.debug("Setting memory for agent: {}, namespace: {}, key: {}", agentId, namespace, key);

        // 1. Cifrar valor (AES-256-GCM)
        byte[] encrypted = encryptValue(value);

        // 2. Guardar en MinIO
        String storagePath = saveToMinIO(agentId, namespace, key, encrypted);

        // 3. Guardar metadata en Postgres
        AgentMemory memory = new AgentMemory();
        memory.setIdagent(agentId);
        memory.setNamespace(namespace);
        memory.setMemoryKey(key);
        memory.setStoragePath(storagePath);
        memory.setEncryptionKeyId("default-key-id");
        if (expiresInDays != null) {
            memory.setExpiresAt(calculateExpiration(expiresInDays));
        }

        businessService.insert(memory);

        return MemoryResponse.builder()
            .agentId(agentId)
            .namespace(namespace)
            .key(key)
            .version(memory.getVersion())
            .build();
    }

    @Override
    public MemoryResponse getMemory(String agentId, String namespace, String key) {
        log.debug("Getting memory for agent: {}, namespace: {}, key: {}", agentId, namespace, key);

        // 1. Leer metadata de Postgres
        AgentMemory memory = businessService.queryfromParams(
            Map.of("idagent", agentId, "namespace", namespace, "memory_key", key),
            AgentMemory.class
        ).stream().findFirst().orElseThrow();

        // 2. Leer de MinIO
        byte[] encrypted = readFromMinIO(memory.getStoragePath());

        // 3. Descifrar
        Object value = decryptValue(encrypted);

        return MemoryResponse.builder()
            .agentId(agentId)
            .namespace(namespace)
            .key(key)
            .value(value)
            .version(memory.getVersion())
            .build();
    }

    private byte[] encryptValue(Object value) {
        // Implementar cifrado AES-256-GCM
        // ...
    }

    private Object decryptValue(byte[] encrypted) {
        // Implementar descifrado AES-256-GCM
        // ...
    }

    // ... otros métodos
}
```

### **Fase 4: Proxy/Interceptor (aios-proxy)**

#### **4.1. AiosProxyApplication.java (Spring Cloud Gateway):**
```java
package com.codeflowx.aios.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiosProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiosProxyApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("agent-proxy", r -> r
                .path("/api/agent/**")
                .filters(f -> f
                    .filter(new TelemetryFilter())  // Intercepta y envía a telemetría
                    .filter(new PolicyCheckFilter())  // Verifica políticas
                )
                .uri("http://agent-service:8080"))
            .build();
    }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Antes de Empezar:**
- [ ] Leer prompt completo en `EVOLUCION_AIOS_OVERLAY.md`
- [ ] Revisar delegates existentes en `codeflowx.govern.workflow.lib`
- [ ] Revisar procesos BPMN existentes
- [ ] Revisar `ImmutableLoggingBusinessService`
- [ ] Revisar Policy Engine (Drools)

### **Fase 1: AioPolicyEnforcementDelegate**
- [ ] Crear `AioPolicyEnforcementDelegate.java`
- [ ] Integrar con Drools Policy Engine
- [ ] Implementar acciones: PAUSE, RESUME, TERMINATE, NOTIFY, CREATE_INCIDENT
- [ ] Registrar acciones en ImmutableLog
- [ ] Tests unitarios
- [ ] Integrar en proceso BPMN de ejemplo

### **Fase 2: Agent Supervisor Microservicio (REST + Worker)**
- [ ] Crear módulo Maven `codeflowx-agent-supervisor` (REST)
- [ ] Crear módulo Maven `codeflowx-agent-supervisor-worker` (Worker)
- [ ] Configurar RabbitMQ (Exchange, Queue, Binding) - similar a telemetría
- [ ] Crear `AgentSupervisorController` con endpoints que publican a RabbitMQ
- [ ] Crear `AgentSupervisorService` que publica a RabbitMQ (retorna 202 Accepted)
- [ ] Crear `AgentActionListener` (@RabbitListener) en worker
- [ ] Crear `AgentActionExecutorService` que ejecuta acciones
- [ ] Integrar con `AioComponent` (type="AGENT")
- [ ] Tests (unit + integration)
- [ ] Documentación OpenAPI

### **Fase 3: Memory Store**
- [ ] Crear script SQL `agent_memories.sql`
- [ ] Crear entidad `AgentMemory` (si no existe)
- [ ] Crear `MemoryStoreService` con cifrado AES-256-GCM
- [ ] Integrar con MinIO
- [ ] Implementar versionado
- [ ] Implementar RBAC
- [ ] Implementar retención configurable
- [ ] Tests (unit + integration)

### **Fase 4: Proxy/Interceptor**
- [ ] Crear `aios-proxy` (Spring Cloud Gateway o Envoy)
- [ ] Implementar interceptación de requests/responses
- [ ] Implementar redacción de PII
- [ ] Integrar con `codeflowx-aios-telemetry`
- [ ] Disparar BPMN si detecta violaciones
- [ ] Tests de interceptación

### **Fase 5: Kill-Switch**
- [ ] Integrar kill-switch con BPMN + `AioPolicyEnforcementDelegate`
- [ ] Implementar circuit breaker
- [ ] Tests de kill-switch (simular violación)

### **Después de Implementación:**
- [ ] Compilar sin errores (REST + Worker)
- [ ] Todos los tests pasan
- [ ] Microservicio REST inicia en puerto 8087
- [ ] Worker inicia y consume de RabbitMQ
- [ ] RabbitMQ configurado correctamente (Exchange, Queue, Binding)
- [ ] Acciones se publican a RabbitMQ y retornan 202 Accepted
- [ ] Worker ejecuta acciones correctamente
- [ ] Proxy intercepta correctamente
- [ ] Memory Store funciona (cifrado/descifrado)
- [ ] Kill-switch activa BPMN correctamente
- [ ] Documentación completa
- [ ] Commit con mensaje: `[AGENTE-11] Agent Supervisor Runtime (REST + Worker asíncrono)`

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**

**AioPolicyEnforcementDelegate:**
- [ ] `codeflowx.govern.workflow.lib/.../delegates/AioPolicyEnforcementDelegate.java`

**Agent Supervisor (REST):**
- [ ] `codeflowx-agent-supervisor/pom.xml`
- [ ] `codeflowx-agent-supervisor/.../AgentSupervisorApplication.java`
- [ ] `codeflowx-agent-supervisor/.../controller/AgentSupervisorController.java`
- [ ] `codeflowx-agent-supervisor/.../service/AgentSupervisorService.java` (publica a RabbitMQ)
- [ ] `codeflowx-agent-supervisor/.../service/MemoryStoreService.java`
- [ ] `codeflowx-agent-supervisor/.../config/RabbitMQConfig.java`
- [ ] `codeflowx-agent-supervisor/.../dto/AgentActionRequest.java`
- [ ] `codeflowx-agent-supervisor/.../dto/MemoryRequest.java`

**Agent Supervisor (Worker):**
- [ ] `codeflowx-agent-supervisor-worker/pom.xml`
- [ ] `codeflowx-agent-supervisor-worker/.../AgentSupervisorWorkerApplication.java`
- [ ] `codeflowx-agent-supervisor-worker/.../listener/AgentActionListener.java` (@RabbitListener)
- [ ] `codeflowx-agent-supervisor-worker/.../service/AgentActionExecutorService.java`
- [ ] `codeflowx-agent-supervisor-worker/.../dto/AgentActionMessage.java`

**Memory Store:**
- [ ] `nocode.service.entitys/.../sql/agent_memories.sql`
- [ ] `nocode.service.entitys/.../entity/AgentMemory.java` (si no existe)

**Proxy:**
- [ ] `codeflowx-aios-proxy/pom.xml`
- [ ] `codeflowx-aios-proxy/.../AiosProxyApplication.java`
- [ ] `codeflowx-aios-proxy/.../filter/TelemetryFilter.java`
- [ ] `codeflowx-aios-proxy/.../filter/PolicyCheckFilter.java`

**Fecha Inicio:** [FECHA]
**Fecha Finalización:** [FECHA]

---

## 🚨 GESTIÓN DE BLOQUEOS

### **Si encuentras bloqueos:**

1. **RabbitMQ no configurado:**
   - Revisar configuración de `codeflowx-aios-telemetry` como referencia
   - Verificar Exchange, Queue, Binding
   - Verificar credenciales en `application.yml`

2. **Drools Policy Engine no tiene método necesario:**
   - Revisar `DroolsPolicyService` existente
   - Agregar método si es necesario

3. **MinIO no configurado:**
   - Verificar configuración en `application.yml`
   - Verificar credenciales

4. **AioComponent no tiene campo runtimeState:**
   - Agregar campo en metadata si es necesario
   - O crear entidad separada para estado

5. **Worker no consume de RabbitMQ:**
   - Verificar `@RabbitListener` está correctamente configurado
   - Verificar que el worker está corriendo
   - Revisar logs del worker

6. **Duda sobre implementación:**
   - Revisar prompt completo en `EVOLUCION_AIOS_OVERLAY.md`
   - Revisar `codeflowx-aios-telemetry` como referencia de arquitectura asíncrona
   - Revisar delegates existentes como referencia
   - Documentar la duda en reporte

---

## 📊 REPORTE DE PROGRESO (Cada 2 horas)

```
AGENTE 11 - REPORTE HORA [X]
===========================
Fase Actual: [Fase 1/2/3/4/5]
Progreso: [%]

Tareas Completadas:
- [Lista de tareas]

Tareas En Progreso:
- [Tarea]: [Progreso %] - [Bloqueos si hay]

Archivos Creados/Modificados:
- [Lista de archivos]

Bloqueos/Problemas:
- [Lista de bloqueos]

Tiempo Estimado Restante:
- [Horas estimadas]
```

---

## 📚 REFERENCIAS RÁPIDAS

### **Comandos Útiles:**
```bash
# Compilar workflow.lib
cd /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib
mvn clean install -DskipTests

# Compilar agent-supervisor
cd /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx-agent-supervisor
mvn clean install -DskipTests

# Ejecutar microservicio
mvn spring-boot:run

# Verificar puerto 8087
curl http://localhost:8087/actuator/health
```

### **Rutas Importantes:**
- **Prompt:** `/git/suinsit.nova.web/docs/compliance/EVOLUCION_AIOS_OVERLAY.md`
- **Delegates:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/`
- **BPMN:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/resources/bpmn/`
- **Telemetría (referencia):** `/eclipse-workspace/nocode.service/codeflowx-aios-telemetry/` - Ver RabbitMQConfig, TelemetryServiceImpl
- **Telemetría Worker (referencia):** `/eclipse-workspace/nocode.service/codeflowx-aios-telemetry-worker/` - Ver @RabbitListener
- **Microservicio REST:** `/eclipse-workspace/nocode.service/codeflowx-agent-supervisor/`
- **Worker:** `/eclipse-workspace/nocode.service/codeflowx-agent-supervisor-worker/`

---

**Última Actualización:** [FECHA]
**Próxima Revisión:** Al completar cada fase
