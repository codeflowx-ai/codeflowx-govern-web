# 🔌 INTEGRACIÓN BACKEND - POST-MARKET MONITORING

**Módulo:** Compliance - Post-Market Monitoring
**Fecha:** Diciembre 2025
**Estado:** 📋 Guía de Integración

---

## 📋 RESUMEN

Este documento describe cómo integrar las pantallas de Post-Market Monitoring (PMM) con el backend, siguiendo la arquitectura establecida de microservicios reactivos.

---

## 🏗️ ARQUITECTURA DE INTEGRACIÓN

### Flujo de Datos

```
Frontend (Next.js)
    ↓ HTTP Request
API Route (Next.js) - app/api/compliance/pmm/*
    ↓ HTTP/WebClient (Reactivo)
Microservicio de Negocio (WebFlux)
    ↓ Mono.fromCallable() (envuelve llamadas síncronas)
Servicio de Negocio (codeflowx.govern.business) [Síncrono - JPA]
    ↓
Repositorio (codeflowx.govern.repository) [Síncrono - JPA]
    ↓
Entidades JPA (nocode.service.entitys)
    ↓
Base de Datos PostgreSQL
```

### Componentes Backend Existentes

#### 1. Entidades JPA

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`

- **PostMarketMonitoring**
  - Tabla: `GOVPOSTMARKETMONITORING`
  - Campos principales:
    - `IDXPOSTMARKETMONITORING` (Long, PK)
    - `IDXPROJECT` (Long, FK)
    - `PMMMONITORINGDATE` (Timestamp)
    - `PMMPERFORMANCEMETRICS` (JSONB)
    - `PMMQUALITYMETRICS` (JSONB)
    - `PMMDRIFTDETECTED` (Boolean)
    - `PMMANOMALIESDETECTED` (Boolean)
    - `PMMCREATEDAT` (Timestamp)
    - `IDUUID` (String)

- **Incident**
  - Tabla: `GOVINCIDENTS`
  - Campos principales:
    - `IDXINCIDENT` (Long, PK)
    - `IDXPROJECT` (Long, FK)
    - `INCSEVERITY` (String) - LOW, MEDIUM, HIGH, CRITICAL
    - `INCDESCRIPTION` (String)
    - `INCINCIDENTDATE` (Timestamp)
    - `INCAUTHORITYNOTIFIED` (Boolean)
    - `INCAUTHORITYNOTIFIEDAT` (Timestamp)
    - `INCUSERSNOTIFIED` (Boolean)
    - `INCRCA` (String) - Análisis de causa raíz
    - `INCSTATUS` (String) - OPEN, INVESTIGATING, RESOLVED, CLOSED
    - `INCCREATEDAT` (Timestamp)
    - `IDUUID` (String)

- **CorrectiveAction**
  - Tabla: `GOVCORRECTIVEACTIONS`
  - Campos principales:
    - `IDXCORRECTIVEACTION` (Long, PK)
    - `IDXINCIDENT` (Long, FK)
    - `IDXPROJECT` (Long, FK)
    - `CADESCRIPTION` (String)
    - `CAPLANNEDDATE` (Timestamp)
    - `CAACTUALDATE` (Timestamp)
    - `CAEFFECTIVENESS` (BigDecimal) - 0.00 - 1.00
    - `CASTATUS` (String) - PLANNED, IN_PROGRESS, COMPLETED, VERIFIED
    - `CACREATEDAT` (Timestamp)
    - `IDUUID` (String)

#### 2. Servicios de Negocio

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

##### PostMarketMonitoringService

```java
@Service
@Slf4j
public class PostMarketMonitoringService {

    @Autowired
    private PostMarketMonitoringRepository repository;

    @Autowired
    private ProjectService projectService;

    /**
     * Monitorea sistema en producción
     */
    public PostMarketMonitoring monitorSystem(Long projectId) {
        log.info("Monitoring system for project: {}", projectId);
        Project project = projectService.findById(projectId);

        // Obtener métricas actuales
        Map<String, Object> currentMetrics = getCurrentMetrics(projectId);
        Map<String, Object> qualityMetrics = getQualityMetrics(projectId);

        // Comparar con baseline
        Map<String, Object> baselineMetrics = getBaselineMetrics(projectId);
        boolean driftDetected = detectDrift(currentMetrics, baselineMetrics);
        boolean anomaliesDetected = detectAnomalies(currentMetrics, baselineMetrics);

        // Crear registro de monitoreo
        PostMarketMonitoring monitoring = new PostMarketMonitoring();
        monitoring.setIdxproject(projectId);
        monitoring.setPmmmonitoringdate(new Timestamp(System.currentTimeMillis()));
        monitoring.setPmmperformancemetrics(currentMetrics);
        monitoring.setPmmqualitymetrics(qualityMetrics);
        monitoring.setPmmdriftdetected(driftDetected);
        monitoring.setPmmanomaliesdetected(anomaliesDetected);
        monitoring.setPmmcreatedat(new Timestamp(System.currentTimeMillis()));
        monitoring.setIduuid(UUID.randomUUID().toString());

        // Si se detectan anomalías, crear alerta
        if (anomaliesDetected) {
            createAlert(projectId, "Anomalías detectadas en monitoreo post-mercado");
        }

        return repository.save(monitoring);
    }

    /**
     * Obtiene historial de monitoreo
     */
    public List<PostMarketMonitoring> getMonitoringHistory(Long projectId, Date from, Date to) {
        log.info("Getting monitoring history for project: {} from {} to {}", projectId, from, to);
        return repository.findByProjectIdAndDateRange(projectId, from, to);
    }

    /**
     * Obtiene métricas del dashboard
     */
    public PMMMetrics getDashboardMetrics() {
        long systemsMonitored = repository.countDistinctProjects();
        long activeIncidents = incidentService.countActiveIncidents();
        long pendingActions = correctiveActionService.countPendingActions();
        double slaCompliance = calculateSLACompliance();

        return new PMMMetrics(systemsMonitored, activeIncidents, pendingActions, slaCompliance);
    }
}
```

##### IncidentService

```java
@Service
@Slf4j
public class IncidentService {

    @Autowired
    private IncidentRepository repository;

    /**
     * Reporta un incidente
     * Si severidad >= HIGH, notifica autoridades automáticamente (Art. 20.1)
     */
    public Incident reportIncident(Long projectId, IncidentData data, String reportedBy) {
        log.info("Reporting incident for project: {} with severity: {}", projectId, data.getSeverity());

        Incident incident = new Incident();
        incident.setIdxproject(projectId);
        incident.setIncseverity(data.getSeverity());
        incident.setIncdescription(data.getDescription());
        incident.setIncincidentdate(new Timestamp(System.currentTimeMillis()));
        incident.setIncstatus("OPEN");
        incident.setInccreatedat(new Timestamp(System.currentTimeMillis()));
        incident.setIduuid(UUID.randomUUID().toString());

        incident = repository.save(incident);

        // Si severidad >= HIGH, notificar autoridades automáticamente (Art. 20.1)
        if ("HIGH".equals(data.getSeverity()) || "CRITICAL".equals(data.getSeverity())) {
            notifyAuthority(incident.getIdxincident(), reportedBy);
        }

        return incident;
    }

    /**
     * Notifica a autoridades
     */
    public void notifyAuthority(Long incidentId, String notifiedBy) {
        log.info("Notifying authority for incident: {}", incidentId);

        Incident incident = repository.findById(incidentId)
            .orElseThrow(() -> new IncidentNotFoundException("Incident not found"));

        incident.setIncauthoritynotified(true);
        incident.setIncauthoritynotifiedat(new Timestamp(System.currentTimeMillis()));
        repository.save(incident);

        // Enviar notificación real a autoridades
        // authorityNotificationService.sendNotification(incident);
    }

    /**
     * Realiza análisis de causa raíz
     */
    public void performRCA(Long incidentId, String rcaAnalysis) {
        log.info("Performing RCA for incident: {}", incidentId);

        Incident incident = repository.findById(incidentId)
            .orElseThrow(() -> new IncidentNotFoundException("Incident not found"));

        incident.setIncrca(rcaAnalysis);
        repository.save(incident);
    }
}
```

##### CorrectiveActionService

```java
@Service
@Slf4j
public class CorrectiveActionService {

    @Autowired
    private CorrectiveActionRepository repository;

    @Autowired
    private IncidentService incidentService;

    /**
     * Crea una acción correctora
     */
    public CorrectiveAction createCorrectiveAction(Long incidentId, CorrectiveActionData data) {
        log.info("Creating corrective action for incident: {}", incidentId);

        Incident incident = incidentService.findById(incidentId);

        CorrectiveAction action = new CorrectiveAction();
        action.setIdxincident(incidentId);
        action.setIdxproject(incident.getIdxproject());
        action.setCadescription(data.getDescription());
        action.setCaplanneddate(data.getPlannedDate());
        action.setCastatus("PLANNED");
        action.setCacreatedat(new Timestamp(System.currentTimeMillis()));
        action.setIduuid(UUID.randomUUID().toString());

        return repository.save(action);
    }

    /**
     * Rastrea efectividad de la acción
     */
    public void trackEffectiveness(Long actionId, BigDecimal effectiveness) {
        log.info("Tracking effectiveness for action: {} with value: {}", actionId, effectiveness);

        CorrectiveAction action = repository.findById(actionId)
            .orElseThrow(() -> new CorrectiveActionNotFoundException("Action not found"));

        action.setCaeffectiveness(effectiveness);
        repository.save(action);

        // Si efectividad >= 0.80, cerrar incidente
        if (effectiveness.compareTo(new BigDecimal("0.80")) >= 0) {
            incidentService.closeIncident(action.getIdxincident());
        }
    }

    /**
     * Actualiza estado de la acción
     */
    public void updateStatus(Long actionId, String status) {
        log.info("Updating status for action: {} to {}", actionId, status);

        CorrectiveAction action = repository.findById(actionId)
            .orElseThrow(() -> new CorrectiveActionNotFoundException("Action not found"));

        action.setCastatus(status);

        if ("COMPLETED".equals(status)) {
            action.setCaactualdate(new Timestamp(System.currentTimeMillis()));
        }

        repository.save(action);
    }
}
```

---

## 🚀 CREACIÓN DEL MICROSERVICIO

### Paso 1: Crear Estructura del Microservicio

**Ubicación:** `nocode.service/codeflowx-governance-pmm-service/`

```
codeflowx-governance-pmm-service/
├── pom.xml
├── src/main/
│   ├── java/com/codeflowx/govern/pmm/
│   │   ├── PMMServiceApplication.java
│   │   ├── controller/
│   │   │   └── PMMController.java
│   │   ├── config/
│   │   │   └── WebClientConfig.java
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.yml
└── README.md
```

### Paso 2: Configurar pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <parent>
        <groupId>com.codeflowx</groupId>
        <artifactId>nocode.service</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>codeflowx-governance-pmm-service</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- Spring Boot WebFlux (NO Web MVC) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- Business Services -->
        <dependency>
            <groupId>com.codeflowx</groupId>
            <artifactId>codeflowx.govern.business</artifactId>
        </dependency>

        <!-- Repository -->
        <dependency>
            <groupId>com.codeflowx</groupId>
            <artifactId>codeflowx.govern.repository</artifactId>
        </dependency>

        <!-- DTOs -->
        <dependency>
            <groupId>com.codeflowx</groupId>
            <artifactId>codeflowx.govern.nocode.dtos</artifactId>
        </dependency>

        <!-- Entities -->
        <dependency>
            <groupId>com.codeflowx</groupId>
            <artifactId>nocode.service.entitys</artifactId>
        </dependency>

        <!-- OpenAPI (WebFlux) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
        </dependency>

        <!-- PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
```

### Paso 3: Crear Application Principal

```java
@SpringBootApplication(scanBasePackages = {
    "com.codeflowx.govern.pmm",
    "com.codeflowx.govern.business",
    "com.codeflowx.govern.repository"
})
@EnableJpaRepositories(basePackages = "com.codeflowx.govern.repository")
@EntityScan(basePackages = "com.codeflowx.govern.entity")
public class PMMServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PMMServiceApplication.class, args);
    }
}
```

### Paso 4: Crear Controller Reactivo

```java
@RestController
@RequestMapping("/api/v1/pmm")
@RequiredArgsConstructor
@Slf4j
public class PMMController {

    private final PostMarketMonitoringService monitoringService;
    private final IncidentService incidentService;
    private final CorrectiveActionService correctiveActionService;

    // ========== Dashboard ==========

    @GetMapping("/dashboard")
    public Mono<ResponseEntity<PMMDashboardDto>> getDashboard(
            @RequestParam(required = false) Long projectId) {
        return Mono.fromCallable(() -> {
            PMMMetrics metrics = monitoringService.getDashboardMetrics();
            List<PMMSystem> systems = monitoringService.getSystemsInProduction(projectId);
            return new PMMDashboardDto(metrics, systems);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error getting dashboard", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    // ========== Incidents ==========

    @GetMapping("/incidents")
    public Mono<ResponseEntity<List<IncidentDto>>> getIncidents(
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String search) {
        return Mono.fromCallable(() -> {
            List<Incident> incidents = incidentService.findByFilters(severity, status, projectId, search);
            return incidents.stream()
                .map(this::toIncidentDto)
                .collect(Collectors.toList());
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error getting incidents", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    @PostMapping("/incidents")
    public Mono<ResponseEntity<IncidentDto>> reportIncident(
            @RequestBody ReportIncidentRequest request,
            @AuthenticationPrincipal String username) {
        return Mono.fromCallable(() -> {
            IncidentData data = new IncidentData();
            data.setSeverity(request.getSeverity());
            data.setDescription(request.getDescription());

            Incident incident = incidentService.reportIncident(
                request.getProjectId(),
                data,
                username
            );

            return toIncidentDto(incident);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error reporting incident", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    @PostMapping("/incidents/{id}/notify-authority")
    public Mono<ResponseEntity<Void>> notifyAuthority(
            @PathVariable Long id,
            @AuthenticationPrincipal String username) {
        return Mono.fromCallable(() -> {
            incidentService.notifyAuthority(id, username);
            return null;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error notifying authority", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    // ========== Corrective Actions ==========

    @GetMapping("/corrective-actions")
    public Mono<ResponseEntity<List<CorrectiveActionDto>>> getCorrectiveActions(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long incidentId,
            @RequestParam(required = false) String search) {
        return Mono.fromCallable(() -> {
            List<CorrectiveAction> actions = correctiveActionService.findByFilters(status, incidentId, search);
            return actions.stream()
                .map(this::toCorrectiveActionDto)
                .collect(Collectors.toList());
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error getting corrective actions", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    @PostMapping("/corrective-actions")
    public Mono<ResponseEntity<CorrectiveActionDto>> createCorrectiveAction(
            @RequestBody CreateCorrectiveActionRequest request) {
        return Mono.fromCallable(() -> {
            CorrectiveActionData data = new CorrectiveActionData();
            data.setDescription(request.getDescription());
            data.setPlannedDate(request.getPlannedDate());

            CorrectiveAction action = correctiveActionService.createCorrectiveAction(
                request.getIncidentId(),
                data
            );

            return toCorrectiveActionDto(action);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error creating corrective action", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    @PatchMapping("/corrective-actions/{id}")
    public Mono<ResponseEntity<CorrectiveActionDto>> updateCorrectiveAction(
            @PathVariable Long id,
            @RequestBody UpdateCorrectiveActionRequest request) {
        return Mono.fromCallable(() -> {
            if (request.getStatus() != null) {
                correctiveActionService.updateStatus(id, request.getStatus());
            }
            if (request.getEffectiveness() != null) {
                correctiveActionService.trackEffectiveness(id, request.getEffectiveness());
            }

            CorrectiveAction action = correctiveActionService.findById(id);
            return toCorrectiveActionDto(action);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error updating corrective action", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    // ========== DTO Mappers ==========

    private IncidentDto toIncidentDto(Incident incident) {
        IncidentDto dto = new IncidentDto();
        dto.setId(incident.getIdxincident());
        dto.setProjectId(incident.getIdxproject());
        dto.setSeverity(incident.getIncseverity());
        dto.setDescription(incident.getIncdescription());
        dto.setReportedAt(incident.getIncincidentdate());
        dto.setAuthorityNotified(incident.getIncauthoritynotified());
        dto.setAuthorityNotifiedAt(incident.getIncauthoritynotifiedat());
        dto.setStatus(incident.getIncstatus());
        dto.setRootCauseAnalysis(incident.getIncrca());
        return dto;
    }

    private CorrectiveActionDto toCorrectiveActionDto(CorrectiveAction action) {
        CorrectiveActionDto dto = new CorrectiveActionDto();
        dto.setId(action.getIdcorrectiveaction());
        dto.setIncidentId(action.getIdxincident());
        dto.setDescription(action.getCadescription());
        dto.setStatus(action.getCastatus());
        dto.setEffectiveness(action.getCaeffectiveness());
        dto.setPlannedDate(action.getCaplanneddate());
        dto.setCompletedDate(action.getCaactualdate());
        return dto;
    }
}
```

### Paso 5: Configurar application.yml

```yaml
spring:
  application:
    name: governance-pmm-service
  webflux:
    base-path: /
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:codeflowx}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}

server:
  port: ${SERVER_PORT:8095}

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

---

## 🔄 ACTUALIZAR API ROUTES DE NEXT.JS

### Dashboard Route

```typescript
// app/api/compliance/pmm/dashboard/route.ts
import { NextResponse } from "next/server";

const PMM_SERVICE_URL = process.env.PMM_SERVICE_URL || "http://localhost:8095";

export async function GET(request: Request) {
  try {
    const { searchParams } = new URL(request.url);
    const projectId = searchParams.get("projectId");

    const url = new URL(`${PMM_SERVICE_URL}/api/v1/pmm/dashboard`);
    if (projectId) {
      url.searchParams.append("projectId", projectId);
    }

    const response = await fetch(url.toString(), {
      headers: {
        "Authorization": request.headers.get("Authorization") || "",
      },
    });

    if (!response.ok) {
      throw new Error(`PMM Service error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json({ success: true, data });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error fetching PMM dashboard data" },
      { status: 500 }
    );
  }
}
```

### Incidents Route

```typescript
// app/api/compliance/pmm/incidents/route.ts
import { NextResponse } from "next/server";

const PMM_SERVICE_URL = process.env.PMM_SERVICE_URL || "http://localhost:8095";

export async function GET(request: Request) {
  try {
    const { searchParams } = new URL(request.url);
    const url = new URL(`${PMM_SERVICE_URL}/api/v1/pmm/incidents`);

    searchParams.forEach((value, key) => {
      url.searchParams.append(key, value);
    });

    const response = await fetch(url.toString(), {
      headers: {
        "Authorization": request.headers.get("Authorization") || "",
      },
    });

    if (!response.ok) {
      throw new Error(`PMM Service error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json({ success: true, data });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error fetching incidents" },
      { status: 500 }
    );
  }
}

export async function POST(request: Request) {
  try {
    const body = await request.json();

    const response = await fetch(`${PMM_SERVICE_URL}/api/v1/pmm/incidents`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": request.headers.get("Authorization") || "",
      },
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      throw new Error(`PMM Service error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json({ success: true, data });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error creating incident" },
      { status: 500 }
    );
  }
}
```

---

## ✅ CHECKLIST DE INTEGRACIÓN

### Backend

- [ ] Verificar que las entidades JPA existan y estén correctamente mapeadas
- [ ] Verificar que los servicios de negocio existan y tengan los métodos necesarios
- [ ] Crear repositorios si no existen (o usar GenericRepository)
- [ ] Crear DTOs en `codeflowx.govern.nocode.dtos` si no existen
- [ ] Crear estructura del microservicio `codeflowx-governance-pmm-service`
- [ ] Configurar pom.xml con dependencias WebFlux
- [ ] Crear controller reactivo con todos los endpoints
- [ ] Configurar application.yml
- [ ] Agregar módulo al pom.xml padre
- [ ] Probar endpoints con Swagger UI

### Frontend

- [ ] Actualizar API routes para llamar al microservicio
- [ ] Configurar variable de entorno `PMM_SERVICE_URL`
- [ ] Actualizar pantallas para usar las API routes actualizadas
- [ ] Manejar errores de conexión con el backend
- [ ] Probar integración end-to-end

---

## 🔗 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Arquitectura Backend:** `docs/portal-backend/PORTAL_BACKEND_ARCHITECTURE.md`
- **Módulo Governance:** `docs/portal-backend/09_GOVERNANCE_MODULE.md`
- **Plantilla Microservicio:** `nocode.service/codeflowx-governance-classification-service/`
- **Prompt PMM:** `docs/prompts/compliance/PROMPT_COMPLIANCE_PMM.md`

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación



