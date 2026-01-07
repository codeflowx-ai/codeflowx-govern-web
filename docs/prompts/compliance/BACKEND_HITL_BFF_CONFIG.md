# 🔀 CONFIGURACIÓN BFF - HITL SUPERVISION

**BFF (Backend for Frontend):** `codeflowx.govern.bff.compliance`
**Microservicio:** `codeflowx-governance-hitl-service` (puerto 8091)

---

## 📋 RESUMEN

El BFF actúa como punto de entrada único para el frontend y enruta las peticiones a los microservicios correspondientes. Para HITL, el BFF debe enrutar las peticiones al microservicio `codeflowx-governance-hitl-service`.

---

## 🏗️ ARQUITECTURA BFF

```
Frontend (Next.js)
    ↓ HTTP Request
    /api/compliance/hitl/*
    ↓
BFF (codeflowx.govern.bff.compliance)
    ↓ HTTP/WebClient (Reactivo)
    /api/v1/hitl/*
    ↓
Microservicio (codeflowx-governance-hitl-service:8091)
```

---

## ⚙️ CONFIGURACIÓN DEL BFF

### Opción 1: Routing mediante WebClient (Recomendado)

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/`

#### HitlBffController.java

```java
package com.codeflowx.govern.bff.compliance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/hitl")
@RequiredArgsConstructor
@Slf4j
public class HitlBffController {

    private final WebClient hitlServiceClient;

    @GetMapping("/dashboard")
    public Mono<ResponseEntity<Object>> getDashboard(
            @RequestParam(required = false) Long projectId) {
        return hitlServiceClient
            .get()
            .uri(uriBuilder -> {
                uriBuilder.path("/api/v1/hitl/dashboard");
                if (projectId != null) {
                    uriBuilder.queryParam("projectId", projectId);
                }
                return uriBuilder.build();
            })
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error routing to HITL service", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @GetMapping("/interventions")
    public Mono<ResponseEntity<Object>> getInterventions(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        return hitlServiceClient
            .get()
            .uri(uriBuilder -> {
                uriBuilder.path("/api/v1/hitl/interventions");
                if (projectId != null) uriBuilder.queryParam("projectId", projectId);
                if (status != null) uriBuilder.queryParam("status", status);
                if (type != null) uriBuilder.queryParam("type", type);
                return uriBuilder.build();
            })
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error routing to HITL service", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PostMapping("/interventions")
    public Mono<ResponseEntity<Object>> recordDecision(@RequestBody Object request) {
        return hitlServiceClient
            .post()
            .uri("/api/v1/hitl/interventions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error routing to HITL service", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @GetMapping("/config")
    public Mono<ResponseEntity<Object>> getConfig(
            @RequestParam(required = false) Long projectId) {
        return hitlServiceClient
            .get()
            .uri(uriBuilder -> {
                uriBuilder.path("/api/v1/hitl/config");
                if (projectId != null) {
                    uriBuilder.queryParam("projectId", projectId);
                }
                return uriBuilder.build();
            })
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error routing to HITL service", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PostMapping("/config")
    public Mono<ResponseEntity<Object>> updateConfig(@RequestBody Object configDto) {
        return hitlServiceClient
            .post()
            .uri("/api/v1/hitl/config")
            .bodyValue(configDto)
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error routing to HITL service", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }
}
```

#### WebClientConfig.java (si no existe)

```java
package com.codeflowx.govern.bff.compliance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${hitl.service.url:http://localhost:8091}")
    private String hitlServiceUrl;

    @Bean(name = "hitlServiceClient")
    public WebClient hitlServiceClient() {
        return WebClient.builder()
            .baseUrl(hitlServiceUrl)
            .build();
    }
}
```

#### application.yml del BFF

```yaml
# Configuración de URLs de microservicios
hitl:
  service:
    url: ${HITL_SERVICE_URL:http://localhost:8091}
```

---

### Opción 2: Routing mediante Gateway (Spring Cloud Gateway)

Si el proyecto usa Spring Cloud Gateway, la configuración sería:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: hitl-service
          uri: http://localhost:8091
          predicates:
            - Path=/api/v1/hitl/**
          filters:
            - StripPrefix=0
```

---

## 🔄 FLUJO COMPLETO

1. **Frontend** hace petición a `/api/compliance/hitl/dashboard`
2. **API Route de Next.js** verifica `USE_MOCK`:
   - Si `true`: Retorna datos mock
   - Si `false`: Hace petición HTTP a `BFF_BASE_URL/api/v1/hitl/dashboard`
3. **BFF** recibe petición en `/api/v1/hitl/dashboard`
4. **BFF** enruta mediante WebClient a `http://localhost:8091/api/v1/hitl/dashboard`
5. **Microservicio HITL** procesa la petición y retorna respuesta
6. **BFF** retorna respuesta al frontend
7. **Frontend** recibe y muestra los datos

---

## ✅ CHECKLIST DE CONFIGURACIÓN BFF

- [ ] Verificar que el BFF existe y está configurado
- [ ] Agregar `HitlBffController` al BFF
- [ ] Configurar `WebClient` para HITL service
- [ ] Agregar configuración de URL en `application.yml`
- [ ] Probar routing end-to-end
- [ ] Verificar manejo de errores
- [ ] Documentar endpoints en Swagger del BFF

---

## 📚 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Microservicio HITL:** `docs/prompts/compliance/BACKEND_HITL_SERVICE.md`
- **Integración Backend:** `docs/prompts/compliance/INTEGRACION_BACKEND_HITL.md`

---

**Última actualización:** Diciembre 2025
**Estado:** Configuración lista para implementación

