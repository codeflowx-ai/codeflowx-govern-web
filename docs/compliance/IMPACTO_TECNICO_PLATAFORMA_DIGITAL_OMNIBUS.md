# IMPACTO TÉCNICO DIGITAL OMNIBUS - CODEFLOWX GOVERN

**Fecha:** Noviembre 2025
**Plataforma:** CodeflowX Govern
**Alcance:** Cambios técnicos concretos requeridos en código, workflows, base de datos e integraciones

---

## 📋 RESUMEN EJECUTIVO

Este documento detalla los **cambios técnicos específicos** que deben implementarse en CodeflowX Govern para cumplir con el Digital Omnibus (COM(2025) 837).

### **Archivos a Modificar/Crear:**

| Prioridad | Tipo | Archivos | Estado |
|-----------|------|----------|--------|
| 🔴 ALTA | Java Service | `EnisaSingleEntryPointService.java` | **CREAR** |
| 🔴 ALTA | Java Delegate | `NotifyMarketSurveillanceAuthorityDelegate.java` | **MODIFICAR** |
| 🔴 ALTA | BPMN | `incident-reporting-process.bpmn20.xml` | **MODIFICAR** |
| 🟡 MEDIA | Java Service | `GDPRPrivacyService.java` | **EXTENDER** |
| 🟡 MEDIA | SQL | `patches/XX_digital_omnibus_extensions.sql` | **CREAR** |
| 🟢 BAJA | Java Entity | `Organization.java` | **EXTENDER** |

---

## 🔴 CAMBIO 1: SINGLE-ENTRY POINT ENISA (PRIORIDAD ALTA)

### **1.1. Crear Servicio ENISA Single-Entry Point**

**Archivo nuevo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/compliance/EnisaSingleEntryPointService.java`

```java
package com.codeflowx.govern.workflow.services.compliance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codeflowx.govern.workflow.services.logging.ImmutableLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para integración con ENISA Single-Entry Point
 *
 * Digital Omnibus: Consolida reportes de múltiples frameworks regulatorios
 * - AI Act (Art. 73)
 * - GDPR (Art. 33)
 * - NIS2 (Art. 23)
 * - DORA
 * - CRA
 * - eIDAS
 * - CER
 */
@Slf4j
@Service
public class EnisaSingleEntryPointService {

    @Value("${enisa.single-entry-point.url:https://single-entry-point.enisa.europa.eu}")
    private String enisaEndpoint;

    @Value("${enisa.api-key:}")
    private String enisaApiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ImmutableLogService immutableLogService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Reporta incidente consolidado al single-entry point ENISA
     * ENISA distribuye automáticamente a autoridades competentes según framework
     */
    public EnisaReportResponse reportIncident(ConsolidatedIncidentReport report) {
        log.info("Reportando incidente consolidado a ENISA: incidentId={}, frameworks={}",
                report.getIncidentId(),
                report.getFrameworks().stream()
                    .map(FrameworkReport::getFramework)
                    .collect(Collectors.toList()));

        try {
            // 1. Validar que todos los frameworks requeridos están incluidos
            validateFrameworks(report);

            // 2. Construir payload consolidado
            EnisaPayload payload = buildConsolidatedPayload(report);

            // 3. Enviar a ENISA single-entry point
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-ENISA-API-Key", enisaApiKey);
            headers.set("X-Report-Source", "CODEFLOWX_GOVERN");

            ResponseEntity<EnisaReportResponse> response = restTemplate.postForEntity(
                enisaEndpoint + "/api/v1/incidents/report",
                new HttpEntity<>(payload, headers),
                EnisaReportResponse.class
            );

            // 4. Registrar en ImmutableLog
            immutableLogService.log(
                "ENISA_SINGLE_ENTRY_POINT_REPORT",
                report.getIncidentId(),
                Map.of(
                    "enisaReportId", response.getBody().getReportId(),
                    "frameworks", report.getFrameworks().stream()
                        .map(FrameworkReport::getFramework)
                        .collect(Collectors.toList()),
                    "status", response.getBody().getStatus(),
                    "timestamp", System.currentTimeMillis()
                )
            );

            log.info("Incidente reportado exitosamente a ENISA: enisaReportId={}",
                    response.getBody().getReportId());

            return response.getBody();

        } catch (Exception e) {
            log.error("Error reportando incidente a ENISA: incidentId={}",
                     report.getIncidentId(), e);

            // Registrar error en ImmutableLog
            immutableLogService.log(
                "ENISA_SINGLE_ENTRY_POINT_REPORT_ERROR",
                report.getIncidentId(),
                Map.of(
                    "error", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
                )
            );

            throw new RuntimeException("Error reportando a ENISA: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta estado de reporte enviado
     */
    public EnisaReportStatus getReportStatus(String enisaReportId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-ENISA-API-Key", enisaApiKey);

        ResponseEntity<EnisaReportStatus> response = restTemplate.getForEntity(
            enisaEndpoint + "/api/v1/incidents/" + enisaReportId + "/status",
            EnisaReportStatus.class
        );

        return response.getBody();
    }

    private void validateFrameworks(ConsolidatedIncidentReport report) {
        if (report.getFrameworks() == null || report.getFrameworks().isEmpty()) {
            throw new IllegalArgumentException("Al menos un framework debe ser especificado");
        }
    }

    private EnisaPayload buildConsolidatedPayload(ConsolidatedIncidentReport report) {
        EnisaPayload payload = new EnisaPayload();
        payload.setIncidentId(report.getIncidentId());
        payload.setReportedAt(report.getDetectedAt());
        payload.setReportingEntity(buildReportingEntity(report));
        payload.setIncidentDetails(buildIncidentDetails(report));
        payload.setRegulatoryFrameworks(report.getFrameworks());
        payload.setMetadata(buildMetadata(report));
        return payload;
    }

    private ReportingEntity buildReportingEntity(ConsolidatedIncidentReport report) {
        ReportingEntity entity = new ReportingEntity();
        entity.setName("CodeflowX");
        entity.setCountry("ES");
        entity.setSector("ICT");
        return entity;
    }

    private IncidentDetails buildIncidentDetails(ConsolidatedIncidentReport report) {
        IncidentDetails details = new IncidentDetails();
        details.setType(report.getIncidentType());
        details.setSeverity(report.getSeverity());
        details.setAffectedUsers(report.getAffectedUsersCount());
        details.setDescription(report.getDescription());
        return details;
    }

    private Map<String, Object> buildMetadata(ConsolidatedIncidentReport report) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "CODEFLOWX_GOVERN");
        metadata.put("workflowInstanceId", report.getWorkflowInstanceId());
        metadata.put("platformVersion", "1.0");
        return metadata;
    }

    // DTOs
    @Data
    public static class ConsolidatedIncidentReport {
        private String incidentId;
        private String workflowInstanceId;
        private Long detectedAt;
        private String incidentType;
        private String severity;
        private Integer affectedUsersCount;
        private String description;
        private List<FrameworkReport> frameworks;
    }

    @Data
    public static class FrameworkReport {
        private String framework; // AI_ACT, GDPR, NIS2, DORA, CRA, eIDAS, CER
        private String article;
        private String authority;
        private Map<String, Object> frameworkSpecificData;
    }

    @Data
    public static class EnisaPayload {
        private String incidentId;
        private Long reportedAt;
        private ReportingEntity reportingEntity;
        private IncidentDetails incidentDetails;
        private List<FrameworkReport> regulatoryFrameworks;
        private Map<String, Object> metadata;
    }

    @Data
    public static class ReportingEntity {
        private String name;
        private String country;
        private String sector;
    }

    @Data
    public static class IncidentDetails {
        private String type;
        private String severity;
        private Integer affectedUsers;
        private String description;
    }

    @Data
    public static class EnisaReportResponse {
        private String reportId;
        private String status; // ACCEPTED, PENDING, REJECTED
        private String message;
        private Long timestamp;
    }

    @Data
    public static class EnisaReportStatus {
        private String reportId;
        private String status;
        private List<String> distributedToAuthorities;
        private Long lastUpdate;
    }
}
```

### **1.2. Modificar Delegate Existente**

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/incident/NotifyMarketSurveillanceAuthorityDelegate.java`

**Cambios requeridos:**

```java
package com.codeflowx.govern.workflow.delegates.incident;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.workflow.services.compliance.EnisaSingleEntryPointService;
import com.codeflowx.govern.workflow.services.compliance.EnisaSingleEntryPointService.ConsolidatedIncidentReport;
import com.codeflowx.govern.workflow.services.compliance.EnisaSingleEntryPointService.FrameworkReport;
import com.codeflowx.govern.workflow.services.compliance.EnisaSingleEntryPointService.EnisaReportResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para notificar a autoridad de vigilancia de mercado
 *
 * Digital Omnibus: Ahora usa ENISA Single-Entry Point
 * - Art. 73.1 EU AI Act - Notificación INMEDIATA de incidentes graves
 * - Consolida reportes de múltiples frameworks (AI Act, GDPR, NIS2, etc.)
 * - Plazo: < 24 horas
 */
@Slf4j
@Component("notifyMarketSurveillanceAuthorityDelegate")
public class NotifyMarketSurveillanceAuthorityDelegate implements JavaDelegate {

    @Autowired
    private EnisaSingleEntryPointService enisaService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        String severity = (String) execution.getVariable("severity");
        String incidentDescription = (String) execution.getVariable("incidentDescription");
        String incidentId = (String) execution.getVariable("incidentId");
        Boolean isSerious = (Boolean) execution.getVariable("isSerious");
        Integer affectedUsersCount = (Integer) execution.getVariable("affectedUsersCount");
        Boolean dataBreach = (Boolean) execution.getVariable("dataBreach");

        log.info("NotifyMarketSurveillanceAuthorityDelegate - Digital Omnibus: processId={}, severity={}, isSerious={}",
                processInstanceId, severity, isSerious);

        try {
            // Construir reporte consolidado según Digital Omnibus
            ConsolidatedIncidentReport report = buildConsolidatedReport(execution);

            // Enviar a ENISA single-entry point
            EnisaReportResponse response = enisaService.reportIncident(report);

            // Guardar confirmación notificación
            execution.setVariable("authorityNotified", true);
            execution.setVariable("enisaReportId", response.getReportId());
            execution.setVariable("authorityNotificationId", response.getReportId());
            execution.setVariable("authorityNotificationDate", new Timestamp(System.currentTimeMillis()));
            execution.setVariable("notificationTimestamp", System.currentTimeMillis());
            execution.setVariable("notificationStatus", response.getStatus());

            log.info("Notificación ENISA exitosa: enisaReportId={}, status={}",
                    response.getReportId(), response.getStatus());

        } catch (Exception e) {
            log.error("ERROR CRÍTICO notificando a ENISA", e);
            execution.setVariable("authorityNotified", false);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error notificando a ENISA: " + e.getMessage(), e);
        }
    }

    private ConsolidatedIncidentReport buildConsolidatedReport(DelegateExecution execution) {
        ConsolidatedIncidentReport report = new ConsolidatedIncidentReport();

        report.setIncidentId((String) execution.getVariable("incidentId"));
        report.setWorkflowInstanceId(execution.getProcessInstanceId());
        report.setDetectedAt(System.currentTimeMillis());
        report.setIncidentType((String) execution.getVariable("incidentType"));
        report.setSeverity((String) execution.getVariable("severity"));
        report.setAffectedUsersCount((Integer) execution.getVariable("affectedUsersCount"));
        report.setDescription((String) execution.getVariable("incidentDescription"));

        // Identificar frameworks aplicables
        List<FrameworkReport> frameworks = new ArrayList<>();

        // AI Act - Art. 73 (si es incidente grave)
        if (Boolean.TRUE.equals(execution.getVariable("isSerious"))) {
            FrameworkReport aiAct = new FrameworkReport();
            aiAct.setFramework("AI_ACT");
            aiAct.setArticle("73");
            aiAct.setAuthority("Market Surveillance Authority");
            aiAct.setFrameworkSpecificData(buildAiActData(execution));
            frameworks.add(aiAct);
        }

        // GDPR - Art. 33 (si hay data breach)
        if (Boolean.TRUE.equals(execution.getVariable("dataBreach"))) {
            FrameworkReport gdpr = new FrameworkReport();
            gdpr.setFramework("GDPR");
            gdpr.setArticle("33");
            gdpr.setAuthority("Data Protection Authority");
            gdpr.setFrameworkSpecificData(buildGdprData(execution));
            frameworks.add(gdpr);
        }

        // NIS2 - Art. 23 (si aplica a entidad esencial/importante)
        if (isNis2Applicable(execution)) {
            FrameworkReport nis2 = new FrameworkReport();
            nis2.setFramework("NIS2");
            nis2.setArticle("23");
            nis2.setAuthority("CSIRT");
            nis2.setFrameworkSpecificData(buildNis2Data(execution));
            frameworks.add(nis2);
        }

        report.setFrameworks(frameworks);

        return report;
    }

    private Map<String, Object> buildAiActData(DelegateExecution execution) {
        Map<String, Object> data = new HashMap<>();
        data.put("seriousIncident", true);
        data.put("affectedUsers", execution.getVariable("affectedUsersCount"));
        data.put("impactOnFundamentalRights", execution.getVariable("impactOnFundamentalRights"));
        return data;
    }

    private Map<String, Object> buildGdprData(DelegateExecution execution) {
        Map<String, Object> data = new HashMap<>();
        data.put("dataBreach", true);
        data.put("personalDataAffected", execution.getVariable("personalDataAffected"));
        data.put("breachType", execution.getVariable("breachType"));
        return data;
    }

    private Map<String, Object> buildNis2Data(DelegateExecution execution) {
        Map<String, Object> data = new HashMap<>();
        data.put("significantIncident", true);
        data.put("essentialEntity", execution.getVariable("essentialEntity"));
        return data;
    }

    private boolean isNis2Applicable(DelegateExecution execution) {
        // Lógica para determinar si aplica NIS2
        return Boolean.TRUE.equals(execution.getVariable("essentialEntity")) ||
               Boolean.TRUE.equals(execution.getVariable("importantEntity"));
    }
}
```

### **1.3. Configuración de Propiedades**

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/resources/application.properties`

**Añadir:**

```properties
# ENISA Single-Entry Point Configuration
enisa.single-entry-point.url=${ENISA_SINGLE_ENTRY_POINT_URL:https://single-entry-point.enisa.europa.eu}
enisa.api-key=${ENISA_API_KEY:}
enisa.timeout=30000

# Frameworks habilitados para reporte consolidado
enisa.frameworks.enabled=AI_ACT,GDPR,NIS2,DORA,CRA,eIDAS,CER
```

---

## 🟡 CAMBIO 2: CLARIFICACIONES GDPR PARA IA (PRIORIDAD MEDIA)

### **2.1. Extender GDPRPrivacyService**

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/compliance/GDPRPrivacyService.java`

**Añadir métodos:**

```java
/**
 * Determina base legal para procesamiento de datos para entrenamiento de IA
 * según clarificaciones Digital Omnibus
 *
 * Digital Omnibus: El procesamiento para desarrollo/operación de IA puede basarse
 * en interés legítimo (Art. 6(1)(f) GDPR), excepto cuando DMA u otras leyes
 * requieran consentimiento explícito.
 */
public LegalBasisResult determineAITrainingLegalBasis(AITrainingProcessingRequest request) {
    log.info("Determinando base legal para entrenamiento IA: purpose={}",
            request.getPurpose());

    LegalBasisResult result = new LegalBasisResult();

    // 1. Verificar si aplica DMA (gatekeepers)
    if (isGatekeeperUnderDMA(request.getController())) {
        result.setLegalBasis(LegalBasis.CONSENT_REQUIRED);
        result.setReason("DMA Art. 5(2) requiere consentimiento explícito para gatekeepers");
        result.setCompatible(false);
        return result;
    }

    // 2. Verificar si hay ley nacional/UE que requiera consentimiento
    if (hasExplicitConsentRequirement(request)) {
        result.setLegalBasis(LegalBasis.CONSENT_REQUIRED);
        result.setReason("Ley nacional/UE requiere consentimiento explícito");
        result.setCompatible(false);
        return result;
    }

    // 3. Por defecto: interés legítimo según Digital Omnibus
    if (meetsLegitimateInterestCriteria(request)) {
        result.setLegalBasis(LegalBasis.LEGITIMATE_INTEREST);
        result.setReason("Procesamiento compatible según clarificaciones Digital Omnibus " +
                        "para entrenamiento de IA con medidas técnicas y organizativas apropiadas");
        result.setCompatible(true);
        return result;
    }

    result.setLegalBasis(LegalBasis.INVALID);
    result.setCompatible(false);
    result.setReason("No cumple criterios de interés legítimo");
    return result;
}

/**
 * Valida excepción Art. 9 GDPR para procesamiento residual de categorías especiales
 * en desarrollo de IA según Digital Omnibus
 */
public SpecialCategoryExceptionValidation validateSpecialCategoryException(
        AITrainingDataset dataset) {

    log.info("Validando excepción Art. 9 para categorías especiales: datasetId={}",
            dataset.getDatasetId());

    SpecialCategoryExceptionValidation validation =
        new SpecialCategoryExceptionValidation();

    // 1. Verificar que no hay intención de procesar categorías especiales
    if (dataset.hasIntentionalSpecialCategories()) {
        validation.setValid(false);
        validation.addError("Controller aims to process special categories - excepción no aplica");
        return validation;
    }

    // 2. Verificar medidas técnicas y organizativas
    if (!hasAppropriateMeasures(dataset)) {
        validation.setValid(false);
        validation.addError("Missing technical/organizational measures");
        return validation;
    }

    // 3. Verificar capacidad de identificación y eliminación
    if (!hasIdentificationAndRemovalCapability(dataset)) {
        validation.setValid(false);
        validation.addError("Cannot identify/remove special categories");
        return validation;
    }

    // 4. Verificar protección contra inferencias
    if (!hasInferenceProtection(dataset)) {
        validation.setValid(false);
        validation.addError("Missing inference protection measures");
        return validation;
    }

    validation.setValid(true);
    validation.setJustification(
        "Excepción Art. 9 aplicable según Digital Omnibus: " +
        "procesamiento residual con medidas técnicas y organizativas apropiadas"
    );

    return validation;
}

// DTOs y clases auxiliares
@Data
public static class AITrainingProcessingRequest {
    private String purpose;
    private String controller;
    private String datasetId;
    private Boolean hasTechnicalMeasures;
    private Boolean hasOrganizationalMeasures;
    private Boolean dataMinimized;
    private Boolean hasTechnicalDocumentation;
}

@Data
public static class LegalBasisResult {
    private LegalBasis legalBasis;
    private Boolean compatible;
    private String reason;
}

public enum LegalBasis {
    LEGITIMATE_INTEREST,
    CONSENT_REQUIRED,
    INVALID
}

@Data
public static class AITrainingDataset {
    private String datasetId;
    private Boolean hasIntentionalSpecialCategories;
    private Boolean hasPseudonymization;
    private Boolean hasSecureProcessingEnvironment;
    private Boolean hasAccessControls;
    private Boolean hasAuditTrail;
    private Boolean canIdentifySpecialCategories;
    private Boolean canRemoveSpecialCategories;
    private Boolean hasInferenceProtection;
}

@Data
public static class SpecialCategoryExceptionValidation {
    private Boolean valid;
    private List<String> errors = new ArrayList<>();
    private String justification;

    public void addError(String error) {
        this.errors.add(error);
    }
}
```

### **2.2. Extensión de Base de Datos**

**Archivo nuevo:** `sql-scripts/patches/XX_digital_omnibus_gdpr_extensions.sql`

```sql
-- ============================================================================
-- EXTENSIONES GDPR PARA PROCESAMIENTO DE DATOS PARA IA
-- Digital Omnibus - Clarificaciones Art. 6(1)(f) y Art. 9 GDPR
-- ============================================================================

-- Extensión tabla de registros de procesamiento
ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN IF NOT EXISTS PRCAITRAININGPURPOSE BOOLEAN DEFAULT FALSE;

ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN IF NOT EXISTS PRCAITRAININGLEGALBASIS VARCHAR(50);

ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN IF NOT EXISTS PRCAITRAININGSPECIALCATEGORYEXCEPTION BOOLEAN DEFAULT FALSE;

ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN IF NOT EXISTS PRCAITRAININGMEASURES JSONB;

ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN IF NOT EXISTS PRCAITRAININGSPECIALCATEGORYREMOVAL BOOLEAN DEFAULT FALSE;

-- Comentarios
COMMENT ON COLUMN GDPR_PROCESSING_RECORDS.PRCAITRAININGPURPOSE IS
    'Indica si el procesamiento es para entrenamiento/desarrollo de IA';

COMMENT ON COLUMN GDPR_PROCESSING_RECORDS.PRCAITRAININGLEGALBASIS IS
    'Base legal: LEGITIMATE_INTEREST, CONSENT_REQUIRED, etc.';

COMMENT ON COLUMN GDPR_PROCESSING_RECORDS.PRCAITRAININGSPECIALCATEGORYEXCEPTION IS
    'Indica si aplica excepción Art. 9 para categorías especiales residuales';

COMMENT ON COLUMN GDPR_PROCESSING_RECORDS.PRCAITRAININGMEASURES IS
    'JSON con medidas técnicas y organizativas implementadas';

COMMENT ON COLUMN GDPR_PROCESSING_RECORDS.PRCAITRAININGSPECIALCATEGORYREMOVAL IS
    'Indica si se han eliminado categorías especiales identificadas';

-- Índices
CREATE INDEX IF NOT EXISTS idx_prc_ai_training_purpose
    ON GDPR_PROCESSING_RECORDS(PRCAITRAININGPURPOSE)
    WHERE PRCAITRAININGPURPOSE = TRUE;

CREATE INDEX IF NOT EXISTS idx_prc_ai_training_legal_basis
    ON GDPR_PROCESSING_RECORDS(PRCAITRAININGLEGALBASIS);
```

---

## 🟢 CAMBIO 3: CLASIFICACIÓN SMC (PRIORIDAD BAJA)

### **3.1. Extender Entidad Organization**

**Archivo:** `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/organizations/Organization.java`

**Añadir campos:**

```java
/**
 * Indica si la organización es Small Mid-Cap (SMC)
 * Digital Omnibus: Extensión de exenciones a SMCs (250-500 empleados)
 */
@Column(name = "ORGISSMC")
private Boolean orgissmc = false;

/**
 * Fecha de clasificación como SMC
 */
@Column(name = "ORGSMCCLASSIFICATIONDATE")
private Timestamp orgsmcclassificationdate;
```

### **3.2. Script SQL**

**Añadir a:** `sql-scripts/patches/XX_digital_omnibus_smc_extensions.sql`

```sql
-- Extensión tabla de organizaciones para SMC
ALTER TABLE ORGORGANIZATIONS
ADD COLUMN IF NOT EXISTS ORGISSMC BOOLEAN DEFAULT FALSE;

ALTER TABLE ORGORGANIZATIONS
ADD COLUMN IF NOT EXISTS ORGSMCCLASSIFICATIONDATE TIMESTAMP;

-- Comentarios
COMMENT ON COLUMN ORGORGANIZATIONS.ORGISSMC IS
    'Small Mid-Cap: entre 250 y 500 empleados (Digital Omnibus)';

COMMENT ON COLUMN ORGORGANIZATIONS.ORGSMCCLASSIFICATIONDATE IS
    'Fecha de clasificación como SMC';

-- Índice
CREATE INDEX IF NOT EXISTS idx_org_issmc
    ON ORGORGANIZATIONS(ORGISSMC)
    WHERE ORGISSMC = TRUE;
```

---

## 📊 PLAN DE IMPLEMENTACIÓN

### **Fase 1: Single-Entry Point (Semana 1-2)**

- [ ] **Día 1-2:** Crear `EnisaSingleEntryPointService.java`
- [ ] **Día 3-4:** Modificar `NotifyMarketSurveillanceAuthorityDelegate.java`
- [ ] **Día 5:** Actualizar workflows BPMN
- [ ] **Día 6-7:** Testing con sandbox ENISA (cuando esté disponible)
- [ ] **Día 8-10:** Integración y pruebas E2E

### **Fase 2: Clarificaciones GDPR (Semana 3)**

- [ ] **Día 1-2:** Extender `GDPRPrivacyService.java`
- [ ] **Día 3:** Crear script SQL de extensiones
- [ ] **Día 4:** Ejecutar script en DEV
- [ ] **Día 5:** Testing y validación

### **Fase 3: Clasificación SMC (Semana 4)**

- [ ] **Día 1:** Extender entidad `Organization.java`
- [ ] **Día 2:** Crear script SQL
- [ ] **Día 3:** Actualizar servicios de clasificación
- [ ] **Día 4:** Testing

---

## 🔧 CONFIGURACIÓN REQUERIDA

### **Variables de Entorno**

```bash
# ENISA Single-Entry Point
export ENISA_SINGLE_ENTRY_POINT_URL="https://single-entry-point.enisa.europa.eu"
export ENISA_API_KEY="your-api-key-here"
```

### **Dependencias Maven**

No se requieren dependencias nuevas (ya se usa `RestTemplate` de Spring).

---

## 📚 REFERENCIAS

- **Documento Base:** COM(2025) 837 - Digital Omnibus
- **SWD:** SWD(2025) 836 - Staff Working Document
- **Archivos Modificados:** Ver tabla al inicio del documento

---

**Última actualización:** Noviembre 2025
**Estado:** Pendiente de implementación
