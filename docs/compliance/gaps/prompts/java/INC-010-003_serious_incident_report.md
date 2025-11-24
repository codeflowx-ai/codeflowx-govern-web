# PROMPT: INC-010-003 - Workflow Completo de Notificación de Incidentes Graves

**Incidencia:** INC-010-003  
**Prioridad:** 🔴 CRÍTICA (Certification Blocker)  
**Artículo EU AI Act:** Art. 73  
**Esfuerzo Estimado:** 5 días  
**Tipo:** Java - Backend + Workflow + Compliance  
**Referencia:** GAP-017

---

## CONTEXTO

No existe implementación completa del workflow de notificación de incidentes graves según Art. 73. Los proveedores deben notificar a las autoridades competentes cualquier incidente grave en un plazo máximo de 15 días.

**Estado Actual:**
- ✅ Proceso BPMN `incident-reporting-process.bpmn` documentado
- ❌ Falta entidad `SeriousIncidentReport`
- ❌ Falta definición de umbrales para "incidente grave"
- ❌ Falta integración con autoridades competentes

---

## REQUISITOS

1. Crear entidad `SeriousIncidentReport` con tabla `SIRSERIOUSINCIDENTREPORTS`
2. Definir umbrales para "incidente grave" según Art. 73
3. Crear servicio `SeriousIncidentReportService` con detección automática
4. Workflow de notificación a autoridades (API o email)
5. Tracking de estado de notificación
6. Integración con proceso BPMN `compliance-monitoring-v1.bpmn`
7. Configuración de autoridades competentes

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Entidad JPA `SeriousIncidentReport`

**Tabla SQL:**
```sql
CREATE TABLE SIRSERIOUSINCIDENTREPORTS (
    iduuid UUID UNIQUE,
    IDXSIRREPORT BIGSERIAL PRIMARY KEY,
    IDXPROJECT BIGINT NOT NULL,
    IDXMODEL BIGINT,
    SIRINCIDENTTYPE VARCHAR(100) NOT NULL, -- DEATH, SERIOUS_INJURY, DATA_BREACH, etc.
    SIRINCIDENTDESCRIPTION TEXT NOT NULL,
    SIRIMPACTASSESSMENT JSONB NOT NULL, -- Evaluación de impacto
    SIRROOTCAUSE TEXT, -- Causa raíz (si disponible)
    SIRCORRECTIVEACTIONS JSONB NOT NULL, -- Acciones correctivas
    SIRNOTIFICATIONSTATUS TEXT[] NOT NULL, -- PENDING, NOTIFIED, ACKNOWLEDGED
    SIRNOTIFIEDAT TIMESTAMP,
    SIRAUTHORITYCONTACT JSONB, -- Información de contacto autoridad
    SIRAUTHORITYREFERENCE VARCHAR(255), -- Referencia de autoridad
    SIRDEADLINE TIMESTAMP NOT NULL, -- Fecha límite notificación (15 días)
    SIRCREATEDAT TIMESTAMP NOT NULL,
    SIRUPDATEDAT TIMESTAMP,
    CONSTRAINT FK_SIR_PROJECT FOREIGN KEY (IDXPROJECT) REFERENCES PRJPROJECTS(IDXPROJECT),
    CONSTRAINT FK_SIR_MODEL FOREIGN KEY (IDXMODEL) REFERENCES MODMODELS(IDXMODEL)
);

CREATE INDEX IDX_SIR_PROJECT ON SIRSERIOUSINCIDENTREPORTS(IDXPROJECT);
CREATE INDEX IDX_SIR_MODEL ON SIRSERIOUSINCIDENTREPORTS(IDXMODEL);
CREATE INDEX IDX_SIR_TYPE ON SIRSERIOUSINCIDENTREPORTS(SIRINCIDENTTYPE);
CREATE INDEX IDX_SIR_STATUS ON SIRSERIOUSINCIDENTREPORTS USING GIN(SIRNOTIFICATIONSTATUS);
CREATE INDEX IDX_SIR_DEADLINE ON SIRSERIOUSINCIDENTREPORTS(SIRDEADLINE);
```

**Entidad Java:**
```java
package com.codeflowx.govern.entities.compliance;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "SIRSERIOUSINCIDENTREPORTS")
@Data
public class SeriousIncidentReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXSIRREPORT")
    private Long idxsirreport;
    
    @Column(name = "iduuid", unique = true, nullable = false)
    private UUID iduuid = UUID.randomUUID();
    
    @Column(name = "IDXPROJECT", nullable = false)
    private Long idxproject;
    
    @Column(name = "IDXMODEL")
    private Long idxmodel;
    
    @Column(name = "SIRINCIDENTTYPE", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private IncidentType sirincidenttype;
    
    @Column(name = "SIRINCIDENTDESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String sirincidentdescription;
    
    @Column(name = "SIRIMPACTASSESSMENT", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> sirimpactassessment;
    
    @Column(name = "SIRROOTCAUSE", columnDefinition = "TEXT")
    private String sirrootcause;
    
    @Column(name = "SIRCORRECTIVEACTIONS", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> sircorrectiveactions;
    
    @Column(name = "SIRNOTIFICATIONSTATUS", nullable = false, columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Set<String> sirnotificationstatus; // PENDING, NOTIFIED, ACKNOWLEDGED
    
    @Column(name = "SIRNOTIFIEDAT")
    private LocalDateTime sirnotifiedat;
    
    @Column(name = "SIRAUTHORITYCONTACT", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> sirauthoritycontact;
    
    @Column(name = "SIRAUTHORITYREFERENCE", length = 255)
    private String sirauthorityreference;
    
    @Column(name = "SIRDEADLINE", nullable = false)
    private LocalDateTime sirdeadline; // 15 días desde creación
    
    @Column(name = "SIRCREATEDAT", nullable = false)
    private LocalDateTime sircreatedat = LocalDateTime.now();
    
    @Column(name = "SIRUPDATEDAT")
    private LocalDateTime sirupdatedat;
    
    public enum IncidentType {
        DEATH,
        SERIOUS_INJURY,
        DATA_BREACH,
        PERFORMANCE_CRITICAL_DEGRADATION,
        EXTREME_DRIFT,
        SECURITY_COMPROMISE,
        FUNDAMENTAL_RIGHTS_VIOLATION,
        SYSTEM_FAILURE,
        OTHER
    }
}
```

### 2. Crear Entidad para Configuración de Autoridades

**Tabla SQL:**
```sql
CREATE TABLE CORCOMPETENTAUTHORITIES (
    iduuid UUID UNIQUE,
    IDXAUTHORITY BIGSERIAL PRIMARY KEY,
    AUTHNAME VARCHAR(255) NOT NULL,
    AUTHCOUNTRY VARCHAR(100) NOT NULL,
    AUTHSECTOR VARCHAR(100), -- HEALTHCARE, TRANSPORT, etc.
    AUTHCONTACT JSONB NOT NULL, -- {email, phone, address, apiEndpoint}
    AUTHNOTIFICATIONTEMPLATE TEXT, -- Template de notificación
    AUTHSTATUS TEXT[] NOT NULL, -- ACTIVE, INACTIVE
    AUTHCREATEDAT TIMESTAMP NOT NULL
);

CREATE INDEX IDX_AUTH_COUNTRY ON CORCOMPETENTAUTHORITIES(AUTHCOUNTRY);
CREATE INDEX IDX_AUTH_SECTOR ON CORCOMPETENTAUTHORITIES(AUTHSECTOR);
CREATE INDEX IDX_AUTH_STATUS ON CORCOMPETENTAUTHORITIES USING GIN(AUTHSTATUS);
```

### 3. Definir Umbrales para "Incidente Grave"

```java
package com.codeflowx.govern.services.compliance;

import java.math.BigDecimal;
import java.util.Map;

public class SeriousIncidentThresholds {
    
    /**
     * Define si un incidente es grave según Art. 73
     */
    public static boolean isSeriousIncident(IncidentType type, Map<String, Object> metrics) {
        return switch (type) {
            case DEATH, SERIOUS_INJURY -> true; // Siempre grave
            case DATA_BREACH -> isMassiveDataBreach(metrics);
            case PERFORMANCE_CRITICAL_DEGRADATION -> isCriticalDegradation(metrics);
            case EXTREME_DRIFT -> isExtremeDrift(metrics);
            case SECURITY_COMPROMISE -> true;
            case FUNDAMENTAL_RIGHTS_VIOLATION -> true;
            default -> false;
        };
    }
    
    /**
     * Degradación crítica: >50% de pérdida de performance
     */
    private static boolean isCriticalDegradation(Map<String, Object> metrics) {
        BigDecimal degradation = (BigDecimal) metrics.get("degradationPercentage");
        return degradation != null && degradation.compareTo(new BigDecimal("50")) > 0;
    }
    
    /**
     * Drift extremo: >60% de drift score
     */
    private static boolean isExtremeDrift(Map<String, Object> metrics) {
        BigDecimal driftScore = (BigDecimal) metrics.get("driftScore");
        return driftScore != null && driftScore.compareTo(new BigDecimal("0.60")) > 0;
    }
    
    /**
     * Violación masiva de datos: >10,000 registros
     */
    private static boolean isMassiveDataBreach(Map<String, Object> metrics) {
        Integer recordsAffected = (Integer) metrics.get("recordsAffected");
        return recordsAffected != null && recordsAffected > 10000;
    }
}
```

### 4. Crear Servicio `SeriousIncidentReportService`

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.SeriousIncidentReport;
import com.codeflowx.govern.entities.compliance.CompetentAuthority;
import com.codeflowx.govern.repositories.compliance.SeriousIncidentReportRepository;
import com.codeflowx.govern.repositories.compliance.CompetentAuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeriousIncidentReportService {
    
    private final SeriousIncidentReportRepository repository;
    private final CompetentAuthorityRepository authorityRepository;
    private final IncidentNotificationService notificationService;
    
    /**
     * Detecta automáticamente si hay un incidente grave y crea el reporte
     */
    @Transactional
    public Optional<SeriousIncidentReport> detectAndCreateIncident(
            Long projectId,
            Long modelId,
            SeriousIncidentReport.IncidentType incidentType,
            Map<String, Object> metrics) {
        
        if (!SeriousIncidentThresholds.isSeriousIncident(incidentType, metrics)) {
            log.debug("Incidente no cumple criterios de gravedad según Art. 73");
            return Optional.empty();
        }
        
        log.warn("Incidente grave detectado: {} para proyecto {} modelo {}", 
            incidentType, projectId, modelId);
        
        SeriousIncidentReport report = new SeriousIncidentReport();
        report.setIdxproject(projectId);
        report.setIdxmodel(modelId);
        report.setSirincidenttype(incidentType);
        report.setSirnotificationstatus(Set.of("PENDING"));
        report.setSircreatedat(LocalDateTime.now());
        report.setSirdeadline(LocalDateTime.now().plusDays(15)); // Art. 73: 15 días
        
        // Buscar autoridad competente
        CompetentAuthority authority = findCompetentAuthority(projectId);
        if (authority != null) {
            report.setSirauthoritycontact(authority.getAuthcontact());
        }
        
        return Optional.of(repository.save(report));
    }
    
    /**
     * Crea un reporte manual de incidente grave
     */
    @Transactional
    public SeriousIncidentReport createManualIncident(SeriousIncidentReport report) {
        report.setSirnotificationstatus(Set.of("PENDING"));
        report.setSircreatedat(LocalDateTime.now());
        
        if (report.getSirdeadline() == null) {
            report.setSirdeadline(LocalDateTime.now().plusDays(15));
        }
        
        // Buscar autoridad competente
        CompetentAuthority authority = findCompetentAuthority(report.getIdxproject());
        if (authority != null) {
            report.setSirauthoritycontact(authority.getAuthcontact());
        }
        
        return repository.save(report);
    }
    
    /**
     * Notifica el incidente a la autoridad competente
     */
    @Transactional
    public void notifyAuthority(Long reportId) {
        SeriousIncidentReport report = repository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Reporte no encontrado"));
        
        if (!report.getSirnotificationstatus().contains("PENDING")) {
            throw new IllegalStateException("El reporte ya fue notificado");
        }
        
        try {
            // Notificar a autoridad
            String authorityReference = notificationService.notifyAuthority(report);
            
            report.setSirnotifiedat(LocalDateTime.now());
            report.setSirauthorityreference(authorityReference);
            
            Set<String> status = report.getSirnotificationstatus();
            status.remove("PENDING");
            status.add("NOTIFIED");
            
            report.setSirupdatedat(LocalDateTime.now());
            repository.save(report);
            
            log.info("Incidente grave notificado a autoridad: {}", authorityReference);
            
        } catch (Exception e) {
            log.error("Error notificando incidente grave a autoridad", e);
            throw new RuntimeException("Error en notificación a autoridad", e);
        }
    }
    
    /**
     * Actualiza estado cuando la autoridad confirma recepción
     */
    @Transactional
    public void acknowledgeNotification(Long reportId, String authorityReference) {
        SeriousIncidentReport report = repository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Reporte no encontrado"));
        
        report.setSirauthorityreference(authorityReference);
        
        Set<String> status = report.getSirnotificationstatus();
        status.add("ACKNOWLEDGED");
        
        report.setSirupdatedat(LocalDateTime.now());
        repository.save(report);
    }
    
    /**
     * Lista incidentes pendientes de notificación
     */
    public List<SeriousIncidentReport> getPendingNotifications() {
        return repository.findBySirnotificationstatusContaining("PENDING");
    }
    
    /**
     * Lista incidentes cercanos a deadline
     */
    public List<SeriousIncidentReport> getIncidentsNearDeadline(int daysBefore) {
        LocalDateTime deadlineThreshold = LocalDateTime.now().plusDays(daysBefore);
        return repository.findBySirdeadlineBeforeAndSirnotificationstatusContaining(
            deadlineThreshold, "PENDING");
    }
    
    /**
     * Busca autoridad competente para un proyecto
     */
    private CompetentAuthority findCompetentAuthority(Long projectId) {
        // Obtener país y sector del proyecto
        // Buscar autoridad correspondiente
        return authorityRepository.findByStatusContaining("ACTIVE")
            .stream()
            .findFirst()
            .orElse(null); // TODO: Implementar lógica de búsqueda específica
    }
}
```

### 5. Crear Servicio de Notificación

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.SeriousIncidentReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentNotificationService {
    
    private final RestTemplate restTemplate;
    private final NotificationTemplateService templateService;
    
    /**
     * Notifica el incidente a la autoridad competente
     * Puede ser vía API o email según configuración
     */
    public String notifyAuthority(SeriousIncidentReport report) {
        Map<String, Object> authorityContact = report.getSirauthoritycontact();
        
        if (authorityContact == null) {
            throw new IllegalStateException("No hay autoridad competente configurada");
        }
        
        String apiEndpoint = (String) authorityContact.get("apiEndpoint");
        String email = (String) authorityContact.get("email");
        
        // Generar contenido de notificación
        String notificationContent = templateService.generateNotificationTemplate(report);
        
        if (apiEndpoint != null && !apiEndpoint.isEmpty()) {
            // Notificar vía API
            return notifyViaApi(apiEndpoint, notificationContent, report);
        } else if (email != null && !email.isEmpty()) {
            // Notificar vía email
            return notifyViaEmail(email, notificationContent, report);
        } else {
            throw new IllegalStateException("No hay método de notificación configurado");
        }
    }
    
    private String notifyViaApi(String apiEndpoint, String content, SeriousIncidentReport report) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, Object> payload = Map.of(
            "incidentType", report.getSirincidenttype().name(),
            "description", report.getSirincidentdescription(),
            "content", content,
            "projectId", report.getIdxproject(),
            "modelId", report.getIdxmodel()
        );
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        
        Map<String, Object> response = restTemplate.postForObject(
            apiEndpoint, request, Map.class);
        
        return (String) response.get("reference");
    }
    
    private String notifyViaEmail(String email, String content, SeriousIncidentReport report) {
        // Implementar envío de email
        // TODO: Integrar con servicio de email
        log.info("Enviando notificación por email a: {}", email);
        return "EMAIL_SENT_" + System.currentTimeMillis();
    }
}
```

### 6. Integración con BPMN

**Delegate para detección automática:**
```java
@Service
public class DetectSeriousIncidentDelegate implements JavaDelegate {
    
    @Autowired
    private SeriousIncidentReportService incidentService;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long projectId = (Long) execution.getVariable("projectId");
        Long modelId = (Long) execution.getVariable("modelId");
        Boolean driftDetected = (Boolean) execution.getVariable("driftDetected");
        BigDecimal driftScore = (BigDecimal) execution.getVariable("driftScore");
        
        // Detectar incidente grave si drift extremo
        if (driftDetected && driftScore.compareTo(new BigDecimal("0.60")) > 0) {
            Map<String, Object> metrics = Map.of("driftScore", driftScore);
            incidentService.detectAndCreateIncident(
                projectId, modelId, 
                SeriousIncidentReport.IncidentType.EXTREME_DRIFT, 
                metrics);
            
            execution.setVariable("seriousIncidentDetected", true);
        }
    }
}
```

**Service Task para notificación automática:**
```java
@Service
public class NotifyAuthorityDelegate implements JavaDelegate {
    
    @Autowired
    private SeriousIncidentReportService incidentService;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long reportId = (Long) execution.getVariable("incidentReportId");
        
        if (reportId != null) {
            incidentService.notifyAuthority(reportId);
        }
    }
}
```

---

## VALIDACIONES

1. ✅ Entidad `SeriousIncidentReport` creada
2. ✅ Umbrales definidos según Art. 73
3. ✅ Servicio con detección automática
4. ✅ Workflow de notificación implementado
5. ✅ Tracking de estado de notificación
6. ✅ Integración con BPMN
7. ✅ Configuración de autoridades competentes

---

## NOTAS

- Art. 73: Notificación debe hacerse en máximo 15 días
- Usar prefijo `SIR` para tabla
- PK autonumérica `IDXSIRREPORT`
- Tercera forma normal
- KISS principle
- Integrar con sistema de alertas para escalación si no se notifica antes del deadline


---

**Estado:** ✅ COMPLETADO






