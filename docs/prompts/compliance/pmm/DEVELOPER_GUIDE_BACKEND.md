# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - PMM

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Backend (Java/Spring Boot)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Capas](#estructura-de-capas)
3. [Servicios de Negocio](#servicios-de-negocio)
4. [Entidades JPA](#entidades-jpa)
5. [Repositorios](#repositorios)
6. [BFF (Backend for Frontend)](#bff-backend-for-frontend)
7. [Microservicio de Negocio](#microservicio-de-negocio)
8. [Flujos de Negocio](#flujos-de-negocio)
9. [Validaciones y Reglas](#validaciones-y-reglas)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (PMM Service)
    ↓
Business Services (Lógica de Negocio)
    ↓
Repositories (JPA)
    ↓
Database (PostgreSQL)
```

### Componentes Principales

1. **BFF (Backend for Frontend)**
   - Ubicación: `codeflowx.govern.bff.compliance`
   - Responsabilidad: Agregar datos, optimizar respuestas para frontend
   - Tecnología: Spring WebFlux (Reactivo)

2. **Business Microservice**
   - Ubicación: `codeflowx-governance-pmm-service`
   - Responsabilidad: Endpoints REST para PMM
   - Tecnología: Spring WebFlux (Reactivo)

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, validaciones, reglas
   - Tecnología: Spring Boot (Transaccional)

4. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/`

**Componentes:**
- **Controller:** `controller/PMMController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados

- **Service:** `service/PMMService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/PMMServiceImpl.java`
  - Implementa llamadas a microservicio de negocio
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/pmm")
public class PMMController {
    private final PMMService pmmService;

    @GetMapping("/dashboard")
    public Mono<ResponseEntity<PMMDashboardDto>> getDashboard(
            @RequestParam(required = false) Long projectId) {
        return pmmService.getDashboard(projectId)
                .map(ResponseEntity::ok);
    }
}
```

---

### 2. Business Microservice Layer

**Ubicación:** `nocode.service/codeflowx-governance-pmm-service/`

**Componentes:**
- **Controller:** `controller/PMMController.java`
  - Endpoints REST del microservicio
  - Llama a Business Services
  - Convierte Entities a DTOs

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/pmm")
public class PMMController {
    private final PostMarketMonitoringPlanService planService;

    @PostMapping("/plans")
    public Mono<ResponseEntity<PostMarketMonitoringPlanDto>> createPlan(
            @Valid @RequestBody CreatePMMPlanRequest request) {
        PostMarketMonitoringPlan plan = toEntity(request);
        PostMarketMonitoringPlan created = planService.createPlan(plan);
        return Mono.just(ResponseEntity.ok(toDto(created)));
    }
}
```

---

### 3. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

**Servicios Principales:**

#### `PostMarketMonitoringPlanService`

**Responsabilidades:**
- Crear planes PMM
- Validar planes según Art. 72
- Activar/suspender planes
- Gestionar unicidad (solo un plan activo por proyecto)
- Soft delete (archivar)

**Métodos Principales:**
```java
@Service
public class PostMarketMonitoringPlanService {

    // Crear plan (estado DRAFT)
    @Transactional
    public PostMarketMonitoringPlan createPlan(PostMarketMonitoringPlan plan) {
        validatePlan(plan); // Valida Art. 72
        plan.setPmmstatus(PlanStatus.DRAFT);
        return repository.save(plan);
    }

    // Activar plan (suspende otros activos del mismo proyecto)
    @Transactional
    public PostMarketMonitoringPlan activatePlan(Long planId) {
        PostMarketMonitoringPlan plan = findById(planId);

        // Suspender otros planes activos del mismo proyecto
        List<PostMarketMonitoringPlan> activePlans =
            repository.findActiveByProjectId(plan.getProject().getIdxproject());
        for (PostMarketMonitoringPlan activePlan : activePlans) {
            if (!activePlan.getIdxpmmplan().equals(planId)) {
                activePlan.setPmmstatus(PlanStatus.SUSPENDED);
                repository.save(activePlan);
            }
        }

        plan.setPmmstatus(PlanStatus.ACTIVE);
        return repository.save(plan);
    }

    // Validar según Art. 72
    private void validatePlan(PostMarketMonitoringPlan plan) {
        if (plan.getPmmplanname() == null || plan.getPmmplanname().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del plan es obligatorio");
        }
        // ... más validaciones
    }
}
```

#### `PostMarketSurveillanceReportService`

**Responsabilidades:**
- Generar reportes automáticos
- Recopilar métricas, alertas, incidentes
- Generar PDF
- Almacenar PDF en storage

**Métodos Principales:**
```java
@Service
public class PostMarketSurveillanceReportService {

    @Transactional
    public PostMarketSurveillanceReport generateReport(
            Long projectId,
            Long modelId,
            ReportType reportType,
            LocalDate reportDate) {

        // 1. Validar proyecto
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // 2. Crear reporte
        PostMarketSurveillanceReport report = new PostMarketSurveillanceReport();
        report.setProject(project);
        report.setPmsreporttype(reportType);
        report.setPmsreportdate(reportDate != null ? reportDate : LocalDate.now());

        // 3. Recopilar datos
        Map<String, Object> reportData = collectReportData(projectId, modelId, reportType);
        report.setPmsreportdata(objectMapper.writeValueAsString(reportData));

        // 4. Generar PDF
        byte[] pdf = pdfGenerator.generateReport(report);

        // 5. Almacenar PDF
        String pdfPath = storageService.store(pdf, "report-" + report.getIdxpmsreport() + ".pdf");
        report.setPmsreportpdfpath(pdfPath);

        // 6. Guardar reporte
        report.setPmsstatus(ReportStatus.GENERATED);
        return repository.save(report);
    }

    private Map<String, Object> collectReportData(Long projectId, Long modelId, ReportType reportType) {
        // Recopilar métricas, alertas, incidentes del período
        // ...
    }
}
```

#### `IncidentService`

**Responsabilidades:**
- Reportar incidentes
- Notificar autoridades automáticamente (Art. 20.1, Art. 73)
- Gestionar estados de incidentes
- Integrar con workflows BPMN

**Métodos Principales:**
```java
@Service
public class IncidentService {

    @Transactional
    public Incident reportIncident(Incident incident) {
        // Validar incidente
        validateIncident(incident);

        // Guardar incidente
        incident.setIncstatus(IncidentStatus.OPEN);
        Incident saved = repository.save(incident);

        // Si severidad >= HIGH, notificar autoridades automáticamente
        if (incident.getIncseverity() == Severity.HIGH ||
            incident.getIncseverity() == Severity.CRITICAL) {
            notifyAuthorities(saved);
        }

        // Disparar workflow BPMN si está configurado
        if (bpmnService != null) {
            bpmnService.startIncidentWorkflow(saved);
        }

        return saved;
    }

    private void notifyAuthorities(Incident incident) {
        // TODO: Implementar notificación real a autoridades
        // Por ahora, solo marca como notificado
        incident.setIncauthoritynotified(true);
        incident.setIncauthoritynotifiedat(new Timestamp(System.currentTimeMillis()));
        repository.save(incident);
    }
}
```

#### `CorrectiveActionService`

**Responsabilidades:**
- Crear acciones correctoras vinculadas a incidentes
- Gestionar estados (PLANNED, IN_PROGRESS, COMPLETED)
- Evaluar efectividad

**Métodos Principales:**
```java
@Service
public class CorrectiveActionService {

    @Transactional
    public CorrectiveAction createAction(CorrectiveAction action) {
        // Validar que el incidente existe
        Incident incident = incidentRepository.findById(action.getIncident().getIdxincident())
            .orElseThrow(() -> new IllegalArgumentException("Incident not found"));

        action.setIncident(incident);
        action.setCacstatus(CorrectiveActionStatus.PLANNED);

        return repository.save(action);
    }

    @Transactional
    public CorrectiveAction updateAction(Long actionId, CorrectiveAction updated) {
        CorrectiveAction action = findById(actionId);

        if (updated.getCacstatus() != null) {
            action.setCacstatus(updated.getCacstatus());
        }

        if (updated.getCaceffectiveness() != null) {
            action.setCaceffectiveness(updated.getCaceffectiveness());
        }

        return repository.save(action);
    }
}
```

#### `AlertThresholdService`

**Responsabilidades:**
- Gestionar thresholds de alertas por proyecto/modelo
- Validar thresholds
- Obtener thresholds activos

**Métodos Principales:**
```java
@Service
public class AlertThresholdService {

    @Transactional
    public AlertThreshold createThreshold(AlertThreshold threshold) {
        validateThreshold(threshold);
        threshold.setAltstatus(ThresholdStatus.ACTIVE);
        return repository.save(threshold);
    }

    public List<AlertThreshold> getActiveThresholdsByProject(Long projectId) {
        return repository.findByProjectIdxprojectAndAltstatus(
            projectId, ThresholdStatus.ACTIVE);
    }
}
```

---

## 🗄️ ENTIDADES JPA

### Ubicación
`nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`

### Entidades Principales

#### `PostMarketMonitoringPlan`

**Tabla:** `PMMPOSTMARKETMONITORINGPLANS`

**Campos Principales:**
- `idxpmmplan` (PK)
- `iduuid` (UUID)
- `pmmplanname` (nombre del plan)
- `idxproject` (FK a Project)
- `idxmodel` (FK a Model, opcional)
- `pmmmonitoringfrequency` (HOURLY, DAILY, WEEKLY, MONTHLY, CUSTOM)
- `pmmcustomfrequencyhours` (si CUSTOM)
- `pmmmetrics` (JSON string con métricas)
- `pmmalertthresholds` (JSON string con thresholds)
- `pmmreportingfrequency` (DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUAL)
- `pmmstatus` (DRAFT, ACTIVE, SUSPENDED, ARCHIVED)
- `pmmcreatedat`, `pmmupdatedat`

**Enums:**
```java
public enum MonitoringFrequency {
    HOURLY, DAILY, WEEKLY, MONTHLY, CUSTOM
}

public enum PlanStatus {
    DRAFT, ACTIVE, SUSPENDED, ARCHIVED
}
```

#### `PostMarketSurveillanceReport`

**Tabla:** `PMMPOSTMARKETSURVEILLANCEREPORTS`

**Campos Principales:**
- `idxpmsreport` (PK)
- `iduuid` (UUID)
- `idxproject` (FK a Project)
- `idxmodel` (FK a Model, opcional)
- `pmsreporttype` (DAILY, WEEKLY, MONTHLY, AD_HOC)
- `pmsreportdate` (fecha del reporte)
- `pmsreportdata` (JSON string con datos)
- `pmsreportpdf` (BLOB, opcional)
- `pmsreportpdfpath` (ruta al PDF almacenado)
- `pmsstatus` (DRAFT, GENERATED, APPROVED, ARCHIVED)
- `pmscreatedat`, `pmsupdatedat`

#### `Incident`

**Tabla:** `PMMINCIDENTS`

**Campos Principales:**
- `idxincident` (PK)
- `idxproject` (FK a Project)
- `incseverity` (LOW, MEDIUM, HIGH, CRITICAL)
- `incstatus` (OPEN, INVESTIGATING, RESOLVED, CLOSED)
- `incdescription` (descripción)
- `incimpact` (impacto estimado)
- `incauthoritynotified` (boolean)
- `incauthoritynotifiedat` (timestamp)

#### `CorrectiveAction`

**Tabla:** `PMMCORRECTIVEACTIONS`

**Campos Principales:**
- `idxcorrectiveaction` (PK)
- `idxincident` (FK a Incident)
- `cacdescription` (descripción)
- `cacstatus` (PLANNED, IN_PROGRESS, COMPLETED)
- `cacplanneddate` (fecha planificada)
- `cacstartdate` (fecha de inicio)
- `caccompleteddate` (fecha de completado)
- `caceffectiveness` (0.00 - 1.00)

#### `AlertThreshold`

**Tabla:** `PMMALERTTHRESHOLDS`

**Campos Principales:**
- `idxalertthreshold` (PK)
- `idxproject` (FK a Project)
- `idxmodel` (FK a Model, opcional)
- `altmetricname` (ACCURACY, LATENCY, THROUGHPUT, BIAS, DRIFT)
- `altmetrictype` (PERCENTAGE, ABSOLUTE, RATIO)
- `altwarningthreshold` (umbral de warning)
- `altcriticalthreshold` (umbral de critical)
- `altseverity` (LOW, MEDIUM, HIGH, CRITICAL)
- `altstatus` (ACTIVE, INACTIVE)

---

## 📚 REPOSITORIOS

### Ubicación
`nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/`

### Repositorios Principales

#### `PostMarketMonitoringPlanRepository`

```java
@Repository
public interface PostMarketMonitoringPlanRepository
    extends JpaRepository<PostMarketMonitoringPlan, Long> {

    List<PostMarketMonitoringPlan> findByIdxproject(Long projectId);

    List<PostMarketMonitoringPlan> findByIdxprojectAndPmmstatus(
        Long projectId, PostMarketMonitoringPlan.PlanStatus status);

    Optional<PostMarketMonitoringPlan> findActivePlanByProjectId(Long projectId);

    List<PostMarketMonitoringPlan> findAllActive();
}
```

#### `PostMarketSurveillanceReportRepository`

```java
@Repository
public interface PostMarketSurveillanceReportRepository
    extends JpaRepository<PostMarketSurveillanceReport, Long> {

    List<PostMarketSurveillanceReport> findByProjectIdxprojectOrderByPmsreportdateDesc(
        Long projectId);

    List<PostMarketSurveillanceReport> findByProjectIdxprojectAndPmsreporttypeAndPmsreportdate(
        Long projectId, ReportType type, LocalDate date);
}
```

#### `IncidentRepository`

```java
@Repository
public interface IncidentRepository
    extends JpaRepository<Incident, Long> {

    List<Incident> findByProjectIdxproject(Long projectId);

    List<Incident> findByProjectIdxprojectAndIncseverity(
        Long projectId, Incident.Severity severity);

    List<Incident> findByProjectIdxprojectAndIncstatus(
        Long projectId, Incident.IncidentStatus status);
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Crear y Activar Plan PMM

```
1. Frontend → POST /api/v1/pmm/plans
2. BFF → Business Microservice → POST /api/v1/pmm/plans
3. Business Microservice → PostMarketMonitoringPlanService.createPlan()
4. Service valida según Art. 72
5. Service crea plan en estado DRAFT
6. Service guarda en BD
7. Frontend → POST /api/v1/pmm/plans/{id}/activate
8. Service.activatePlan() suspende otros planes activos del mismo proyecto
9. Service activa el plan
10. Plan queda en estado ACTIVE
```

### Flujo 2: Reportar Incidente Grave

```
1. Frontend → POST /api/v1/pmm/incidents (severity: HIGH/CRITICAL)
2. BFF → Business Microservice → POST /api/v1/pmm/incidents
3. Business Microservice → IncidentService.reportIncident()
4. Service valida incidente
5. Service guarda incidente en estado OPEN
6. Service detecta severidad >= HIGH
7. Service.notifyAuthorities() marca como notificado
8. Service dispara workflow BPMN (si está configurado)
9. Frontend recibe incidente con authorityNotified = true
```

### Flujo 3: Generar Reporte de Vigilancia

```
1. Frontend → POST /api/v1/pmm/reports/generate
2. BFF → Business Microservice → POST /api/v1/pmm/reports/generate
3. Business Microservice → PostMarketSurveillanceReportService.generateReport()
4. Service valida proyecto
5. Service recopila datos del período:
   - Métricas de monitoreo
   - Alertas generadas
   - Incidentes detectados
   - Análisis de tendencias
6. Service genera JSON con datos
7. Service genera PDF usando ReportPdfGenerator
8. Service almacena PDF en StorageService
9. Service guarda reporte en estado GENERATED
10. Frontend recibe reporte con PDF path
```

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de Planes PMM (Art. 72)

**Ubicación:** `PostMarketMonitoringPlanService.validatePlan()`

**Reglas:**
1. Nombre del plan es obligatorio
2. Proyecto es obligatorio
3. Frecuencia de monitoreo es obligatoria
4. Si frecuencia es CUSTOM, horas personalizadas deben ser > 0
5. Al menos una métrica debe estar configurada
6. Thresholds de alertas son obligatorios
7. Frecuencia de reporte es obligatoria

### Reglas de Activación de Planes

**Ubicación:** `PostMarketMonitoringPlanService.activatePlan()`

**Reglas:**
1. Solo puede haber un plan ACTIVE por proyecto
2. Al activar un plan, se suspenden automáticamente otros planes activos del mismo proyecto
3. Solo se pueden activar planes en estado DRAFT o SUSPENDED

### Reglas de Notificación de Incidentes

**Ubicación:** `IncidentService.reportIncident()`

**Reglas:**
1. Si severidad es HIGH o CRITICAL → Notificación automática a autoridades (Art. 20.1, Art. 73)
2. Se marca `incauthoritynotified = true`
3. Se registra timestamp de notificación

### Reglas de Actualización de Planes

**Ubicación:** `PostMarketMonitoringPlanService.updatePlan()`

**Reglas:**
1. Solo se pueden actualizar planes en estado DRAFT o SUSPENDED
2. No se puede actualizar un plan ACTIVE (debe suspenderse primero)
3. Se revalida el plan después de actualizar

---

## 🔗 REFERENCIAS

### Archivos Clave

**BFF:**
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/PMMController.java`
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/impl/PMMServiceImpl.java`

**Business Services:**
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringPlanService.java`
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketSurveillanceReportService.java`
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/IncidentService.java`
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/CorrectiveActionService.java`
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/AlertThresholdService.java`

**Entities:**
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketMonitoringPlan.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketSurveillanceReport.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/Incident.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/CorrectiveAction.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/AlertThreshold.java`

**Repositories:**
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/PostMarketMonitoringPlanRepository.java`
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/PostMarketSurveillanceReportRepository.java`

---

**Última Actualización:** Diciembre 2025
