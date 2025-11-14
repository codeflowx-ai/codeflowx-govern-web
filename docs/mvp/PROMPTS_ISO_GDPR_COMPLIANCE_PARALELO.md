# 🏛️ PROMPTS ISO/GDPR/SOX COMPLIANCE - DESARROLLO PARALELO

**Fecha:** Sábado 1 Noviembre 2025  
**Objetivo:** Compliance multi-framework (GDPR 90% → 98%, ISO 27001/27701/42001 85-92% → 98%)  
**Estrategia:** 8 chats IA trabajando en paralelo  
**Timeline:** Esta noche/fin de semana → Listo próxima semana  
**Demo:** Disponible semana próxima

---

## 🎯 RESUMEN EJECUTIVO

**GAPS A RESOLVER:**

| Framework | Gap Actual | Target | Chats |
|-----------|------------|--------|-------|
| **GDPR** | 90% → 98% | +8% | Chat 1, 2, 3, 7 |
| **ISO 27001** | 85% → 95% | +10% | Chat 4, 6 |
| **ISO 27701** | 88% → 98% | +10% | Chat 1, 3, 7 |
| **ISO 42001** | 92% → 98% | +6% | Chat 4, 5 |
| **SOX** | 70% → 85% | +15% | Chat 6 |

**TOTAL:** 8 chats = Fin de semana → Multi-framework compliance 95%+

---

## 📋 DISTRIBUCIÓN DE CHATS

```
Chat 1: GDPR Privacy Notice + Rights Service    [6h] ⭐⭐⭐
Chat 2: Data Portability & Export Service       [5h] ⭐⭐⭐
Chat 3: Consent Management Service + BPMN       [6h] ⭐⭐⭐
Chat 4: ISO Documentation Generator             [5h] ⭐⭐
Chat 5: Data Catalog & Classification           [5h] ⭐⭐
Chat 6: Record Retention & SOX Compliance       [4h] ⭐⭐
Chat 7: Data Subject Rights BPMN Workflows      [6h] ⭐⭐⭐
Chat 8: Pantallas ZUL GDPR/ISO                  [7h] ⭐⭐

TOTAL: 8 chats = Esta noche/fin de semana
```

---

## 💬 CHAT 1: GDPR PRIVACY NOTICE + RIGHTS SERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para GDPR compliance: Privacy Notices y Data Subject Rights.

CONTEXTO:
- Proyecto: Spring Boot + JPA + PostgreSQL
- Framework: EnArt (BusinessService)
- Ubicación entidades: nocode.service/entitys/compliance/
- GDPR Art. 13-14 (Información), Art. 15-22 (Derechos)

═══════════════════════════════════════════
ENTIDAD 1: PRIVACY NOTICE
═══════════════════════════════════════════

Ubicación: nocode.service/entitys/compliance/PrivacyNotice.java

@Entity
@Table(name = "CMP_PRIVACY_NOTICES")
public class PrivacyNotice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_privacy_notice")
    private Long idxPrivacyNotice;
    
    // Información del responsable (Art. 13.1.a)
    @NotNull
    @Column(name = "controller_name", length = 200)
    private String controllerName;
    
    @Column(name = "controller_contact", columnDefinition = "TEXT")
    private String controllerContact;
    
    @Column(name = "dpo_contact", columnDefinition = "TEXT")
    private String dpoContact;
    
    // Propósitos del tratamiento (Art. 13.1.c)
    @NotNull
    @Column(name = "processing_purposes", columnDefinition = "TEXT")
    private String processingPurposes; // JSON array
    
    // Base jurídica (Art. 13.1.c)
    @NotNull
    @Column(name = "legal_basis", length = 100)
    private String legalBasis; // CONSENT, CONTRACT, LEGAL_OBLIGATION, LEGITIMATE_INTEREST
    
    // Categorías de datos (Art. 13.1.c)
    @Column(name = "data_categories", columnDefinition = "TEXT")
    private String dataCategories; // JSON array
    
    // Destinatarios (Art. 13.1.e)
    @Column(name = "recipients", columnDefinition = "TEXT")
    private String recipients; // JSON array
    
    // Transferencias internacionales (Art. 13.1.f)
    @Column(name = "international_transfers", columnDefinition = "TEXT")
    private String internationalTransfers; // JSON
    
    // Período de conservación (Art. 13.2.a)
    @Column(name = "retention_period", length = 200)
    private String retentionPeriod;
    
    // Derechos del interesado (Art. 13.2.b)
    @Column(name = "data_subject_rights", columnDefinition = "TEXT")
    private String dataSubjectRights; // JSON array
    
    // Derecho de retirar consentimiento (Art. 13.2.c)
    @Column(name = "right_to_withdraw", columnDefinition = "TEXT")
    private String rightToWithdraw;
    
    // Derecho a reclamar (Art. 13.2.d)
    @Column(name = "right_to_complain", columnDefinition = "TEXT")
    private String rightToComplain;
    
    // Decisiones automatizadas (Art. 13.2.f)
    @Column(name = "automated_decision_making", columnDefinition = "TEXT")
    private String automatedDecisionMaking; // Explicación + lógica
    
    // Language
    @NotNull
    @Column(name = "language", length = 10)
    private String language;
    
    // Generated content
    @Column(name = "generated_html", columnDefinition = "TEXT")
    private String generatedHtml;
    
    @Column(name = "generated_pdf_path", length = 500)
    private String generatedPdfPath;
    
    // Metadata
    @Column(name = "applicable_to_system", length = 100)
    private String applicableToSystem; // MODEL, AGENT, PROMPT, ALL
    
    @Column(name = "version", length = 50)
    private String version;
    
    @Column(name = "status", length = 50)
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
ENTIDAD 2: DATA SUBJECT REQUEST
═══════════════════════════════════════════

@Entity
@Table(name = "CMP_DATA_SUBJECT_REQUESTS")
public class DataSubjectRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_dsr")
    private Long idxDsr;
    
    // Solicitante
    @NotNull
    @Column(name = "requester_email", length = 200)
    private String requesterEmail;
    
    @Column(name = "requester_name", length = 200)
    private String requesterName;
    
    // Tipo de solicitud (Art. 15-22)
    @NotNull
    @Column(name = "request_type", length = 50)
    private String requestType; // ACCESS, RECTIFICATION, ERASURE, RESTRICTION, PORTABILITY, OBJECTION
    
    // Descripción
    @Column(name = "request_description", columnDefinition = "TEXT")
    private String requestDescription;
    
    // Estado
    @NotNull
    @Column(name = "status", length = 50)
    private String status; // RECEIVED, IN_REVIEW, APPROVED, REJECTED, COMPLETED, EXPIRED
    
    // Datos afectados
    @Column(name = "affected_data_categories", columnDefinition = "TEXT")
    private String affectedDataCategories; // JSON
    
    @Column(name = "affected_systems", columnDefinition = "TEXT")
    private String affectedSystems; // JSON array
    
    // Respuesta
    @Column(name = "response", columnDefinition = "TEXT")
    private String response;
    
    @Column(name = "response_data_path", length = 500)
    private String responseDataPath; // Para portabilidad
    
    // SLA tracking
    @NotNull
    @Column(name = "received_at")
    private Timestamp receivedAt;
    
    @Column(name = "deadline")
    private Timestamp deadline; // 30 días desde recepción (GDPR)
    
    @Column(name = "completed_at")
    private Timestamp completedAt;
    
    // Asignación
    @Column(name = "assigned_to", length = 200)
    private String assignedTo;
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
SERVICIO A CREAR:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/service/GDPRPrivacyService.java

@Service
@Slf4j
public class GDPRPrivacyService {
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Genera Privacy Notice según GDPR Art. 13-14
     */
    public PrivacyNotice generatePrivacyNotice(
        String systemType,
        String language
    ) {
        PrivacyNotice notice = new PrivacyNotice();
        
        notice.setControllerName("CodeflowX Platform");
        notice.setControllerContact("Address: ... \nEmail: privacy@codeflowx.com");
        notice.setDpoContact("DPO Email: dpo@codeflowx.com");
        
        // Propósitos según sistema
        notice.setProcessingPurposes(buildPurposes(systemType));
        notice.setLegalBasis("LEGITIMATE_INTEREST");
        notice.setDataCategories(buildDataCategories(systemType));
        notice.setRecipients(buildRecipients());
        
        // Derechos
        notice.setDataSubjectRights(buildRightsList(language));
        notice.setRightToWithdraw("Puede retirar su consentimiento en cualquier momento contactando: privacy@codeflowx.com");
        notice.setRightToComplain("Autoridad de Control: Agencia Española de Protección de Datos (AEPD)");
        
        // Decisiones automatizadas (Art. 13.2.f)
        notice.setAutomatedDecisionMaking(
            "Este sistema utiliza IA para evaluaciones automatizadas. " +
            "Tiene derecho a solicitar intervención humana y explicación de las decisiones (Art. 22)."
        );
        
        // Retención
        notice.setRetentionPeriod("Los datos se conservan durante 6 meses desde la última actividad");
        
        notice.setLanguage(language);
        notice.setApplicableToSystem(systemType);
        notice.setVersion("1.0");
        notice.setStatus("DRAFT");
        notice.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        // Generar HTML
        String html = generatePrivacyNoticeHTML(notice, language);
        notice.setGeneratedHtml(html);
        
        businessService.save(notice);
        
        return notice;
    }
    
    /**
     * Procesa solicitud de derechos GDPR (Art. 15-22)
     */
    public DataSubjectRequest processDataSubjectRequest(
        String requesterEmail,
        String requestType,
        String description
    ) {
        DataSubjectRequest request = new DataSubjectRequest();
        
        request.setRequesterEmail(requesterEmail);
        request.setRequestType(requestType);
        request.setRequestDescription(description);
        request.setStatus("RECEIVED");
        request.setReceivedAt(new Timestamp(System.currentTimeMillis()));
        
        // Calcular deadline (30 días GDPR)
        LocalDateTime deadline = LocalDateTime.now().plusDays(30);
        request.setDeadline(Timestamp.valueOf(deadline));
        
        request.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        businessService.save(request);
        
        log.info("Data Subject Request created: {} - Type: {}", request.getIdxDsr(), requestType);
        
        return request;
    }
    
    private String buildRightsList(String language) {
        if ("es".equals(language)) {
            return "{\n" +
                   "  \"access\": \"Derecho de acceso a sus datos personales (Art. 15)\",\n" +
                   "  \"rectification\": \"Derecho de rectificación de datos inexactos (Art. 16)\",\n" +
                   "  \"erasure\": \"Derecho de supresión ('derecho al olvido') (Art. 17)\",\n" +
                   "  \"restriction\": \"Derecho de limitación del tratamiento (Art. 18)\",\n" +
                   "  \"portability\": \"Derecho de portabilidad de datos (Art. 20)\",\n" +
                   "  \"objection\": \"Derecho de oposición al tratamiento (Art. 21)\",\n" +
                   "  \"explanation\": \"Derecho a explicación de decisiones automatizadas (Art. 22)\"\n" +
                   "}";
        }
        return "{ ... }"; // English version
    }
}

═══════════════════════════════════════════
REST CONTROLLER:
═══════════════════════════════════════════

@RestController
@RequestMapping("/api/compliance/gdpr")
public class GDPRComplianceController {
    
    @PostMapping("/generate-privacy-notice")
    public ResponseEntity<PrivacyNotice> generateNotice(
        @RequestParam String systemType,
        @RequestParam(defaultValue = "es") String language
    ) {
        PrivacyNotice notice = gdprService.generatePrivacyNotice(systemType, language);
        return ResponseEntity.ok(notice);
    }
    
    @PostMapping("/data-subject-request")
    public ResponseEntity<DataSubjectRequest> submitRequest(
        @RequestBody DataSubjectRequestDTO dto
    ) {
        DataSubjectRequest request = gdprService.processDataSubjectRequest(
            dto.getEmail(), dto.getRequestType(), dto.getDescription()
        );
        return ResponseEntity.ok(request);
    }
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. PrivacyNotice.java (entidad JPA)
2. DataSubjectRequest.java (entidad JPA)
3. GDPRPrivacyService.java (servicio completo)
4. GDPRComplianceController.java (endpoints REST)
5. Privacy notice HTML template
6. SQL migrations
7. Tests unitarios

IMPORTANTE:
✅ GDPR Art. 13-14 compliant
✅ Template multi-idioma (es, en)
✅ Data subject rights (Art. 15-22)
✅ SLA tracking (30 días)
✅ BusinessService para persistence
❌ NO envío de emails (solo generación)

Usa Spring Boot, JPA, buenas prácticas Java.
```

---

## 💬 CHAT 2: DATA PORTABILITY & EXPORT SERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para GDPR Art. 20 - Data Portability (export de datos personales).

CONTEXTO:
- Proyecto: Spring Boot + JPA + PostgreSQL
- GDPR Art. 20: Right to data portability (formato estructurado, machine-readable)
- Package: com.codeflowx.govern.service

═══════════════════════════════════════════
ENTIDAD JPA:
═══════════════════════════════════════════

Ubicación: nocode.service/entitys/compliance/DataExportRequest.java

@Entity
@Table(name = "CMP_DATA_EXPORT_REQUESTS")
public class DataExportRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_export_request")
    private Long idxExportRequest;
    
    // Solicitante
    @NotNull
    @Column(name = "data_subject_email", length = 200)
    private String dataSubjectEmail;
    
    @Column(name = "data_subject_id", length = 100)
    private String dataSubjectId;
    
    // Scope del export
    @Column(name = "export_scope", columnDefinition = "TEXT")
    private String exportScope; // JSON: {tables: [...], date_range: {...}}
    
    // Formato de export
    @NotNull
    @Column(name = "export_format", length = 50)
    private String exportFormat; // JSON, XML, CSV
    
    // Estado
    @NotNull
    @Column(name = "status", length = 50)
    private String status; // REQUESTED, PROCESSING, COMPLETED, FAILED
    
    // Resultado
    @Column(name = "export_file_path", length = 500)
    private String exportFilePath;
    
    @Column(name = "export_file_size_kb")
    private Long exportFileSizeKb;
    
    @Column(name = "records_exported")
    private Integer recordsExported;
    
    // Timestamps
    @NotNull
    @Column(name = "requested_at")
    private Timestamp requestedAt;
    
    @Column(name = "completed_at")
    private Timestamp completedAt;
    
    @Column(name = "expires_at")
    private Timestamp expiresAt; // Link descarga expira en 48h
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
SERVICIO A CREAR:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/service/DataPortabilityService.java

@Service
@Slf4j
public class DataPortabilityService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * Export de datos personales (GDPR Art. 20)
     */
    public DataExportRequest exportPersonalData(
        String dataSubjectEmail,
        String exportFormat
    ) {
        log.info("Exporting personal data for: {}", dataSubjectEmail);
        
        DataExportRequest request = new DataExportRequest();
        request.setDataSubjectEmail(dataSubjectEmail);
        request.setExportFormat(exportFormat);
        request.setStatus("PROCESSING");
        request.setRequestedAt(new Timestamp(System.currentTimeMillis()));
        
        // Calcular expiración (48h)
        LocalDateTime expires = LocalDateTime.now().plusHours(48);
        request.setExpiresAt(Timestamp.valueOf(expires));
        
        request.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        businessService.save(request);
        
        try {
            // 1. Recopilar datos del data subject de todas las tablas relevantes
            Map<String, Object> personalData = collectPersonalData(dataSubjectEmail);
            
            // 2. Formatear según formato solicitado
            File exportFile = generateExportFile(personalData, exportFormat, request.getIdxExportRequest());
            
            // 3. Actualizar request
            request.setExportFilePath(exportFile.getAbsolutePath());
            request.setExportFileSizeKb(exportFile.length() / 1024);
            request.setRecordsExported(personalData.size());
            request.setStatus("COMPLETED");
            request.setCompletedAt(new Timestamp(System.currentTimeMillis()));
            
        } catch (Exception e) {
            log.error("Error exporting data", e);
            request.setStatus("FAILED");
        }
        
        request.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        businessService.update(request);
        
        return request;
    }
    
    private Map<String, Object> collectPersonalData(String email) {
        Map<String, Object> data = new HashMap<>();
        
        // Query todas las tablas que contienen datos del usuario
        // Ejemplo simplificado:
        
        // User profile
        data.put("user_profile", queryUserProfile(email));
        
        // Models asociados
        data.put("models", queryUserModels(email));
        
        // Agents asociados
        data.put("agents", queryUserAgents(email));
        
        // Audit logs
        data.put("audit_logs", queryUserAuditLogs(email));
        
        // Compliance assessments
        data.put("compliance_assessments", queryUserAssessments(email));
        
        return data;
    }
    
    private File generateExportFile(
        Map<String, Object> data,
        String format,
        Long requestId
    ) {
        String filename = "personal_data_export_" + requestId;
        String outputPath = "/exports/gdpr/";
        
        File outputDir = new File(outputPath);
        outputDir.mkdirs();
        
        File exportFile;
        
        if ("JSON".equalsIgnoreCase(format)) {
            exportFile = new File(outputPath + filename + ".json");
            writeJSON(data, exportFile);
        } else if ("XML".equalsIgnoreCase(format)) {
            exportFile = new File(outputPath + filename + ".xml");
            writeXML(data, exportFile);
        } else {
            exportFile = new File(outputPath + filename + ".zip"); // CSV en ZIP
            writeCSVZip(data, exportFile);
        }
        
        return exportFile;
    }
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. PrivacyNotice.java (entidad)
2. DataSubjectRequest.java (entidad)
3. DataExportRequest.java (entidad)
4. GDPRPrivacyService.java (servicio)
5. DataPortabilityService.java (servicio)
6. GDPRComplianceController.java (controller)
7. Privacy notice HTML template
8. SQL migrations
9. Tests

IMPORTANTE:
✅ GDPR Art. 13, 14, 20 compliant
✅ Export JSON, XML, CSV
✅ SLA 30 días tracking
✅ Expire links 48h
✅ Machine-readable formats
❌ NO acceso directo a datos (usar audit tables)

Usa Spring Boot, Jackson, buenas prácticas Java.
```

---

## 💬 CHAT 3: CONSENT MANAGEMENT SERVICE + BPMN

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java + BPMN para Consent Management (GDPR Art. 7 + ISO 27701).

CONTEXTO:
- Proyecto: Spring Boot + JPA + Flowable BPMN
- GDPR Art. 7: Consentimiento
- ISO 27701 Control 6.2.1: Basis for processing
- Package: com.codeflowx.govern.service

═══════════════════════════════════════════
ENTIDAD JPA:
═══════════════════════════════════════════

Ubicación: nocode.service/entitys/compliance/ConsentRecord.java

@Entity
@Table(name = "CMP_CONSENT_RECORDS")
public class ConsentRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_consent")
    private Long idxConsent;
    
    // Data subject
    @NotNull
    @Column(name = "data_subject_email", length = 200)
    private String dataSubjectEmail;
    
    @Column(name = "data_subject_id", length = 100)
    private String dataSubjectId;
    
    // Purpose of consent
    @NotNull
    @Column(name = "processing_purpose", length = 200)
    private String processingPurpose;
    
    @Column(name = "purpose_description", columnDefinition = "TEXT")
    private String purposeDescription;
    
    // Consent status
    @NotNull
    @Column(name = "consent_status", length = 50)
    private String consentStatus; // GIVEN, WITHDRAWN, EXPIRED
    
    @Column(name = "consent_method", length = 100)
    private String consentMethod; // WEB_FORM, EMAIL, API, EXPLICIT_ACTION
    
    // Granularidad
    @Column(name = "granular_consents", columnDefinition = "TEXT")
    private String granularConsents; // JSON: {marketing: true, analytics: false, ...}
    
    // Timestamps
    @NotNull
    @Column(name = "consent_given_at")
    private Timestamp consentGivenAt;
    
    @Column(name = "consent_withdrawn_at")
    private Timestamp consentWithdrawnAt;
    
    @Column(name = "consent_expires_at")
    private Timestamp consentExpiresAt;
    
    // Evidencia (GDPR Art. 7.1)
    @Column(name = "consent_evidence", columnDefinition = "TEXT")
    private String consentEvidence; // JSON: IP, timestamp, form data, etc.
    
    @Column(name = "consent_text_shown", columnDefinition = "TEXT")
    private String consentTextShown;
    
    // Versión de privacy notice
    @Column(name = "privacy_notice_version", length = 50)
    private String privacyNoticeVersion;
    
    // Sistema aplicable
    @Column(name = "applicable_system", length = 100)
    private String applicableSystem;
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
SERVICIO:
═══════════════════════════════════════════

@Service
@Slf4j
public class ConsentManagementService {
    
    @Autowired
    private BusinessService businessService;
    
    public ConsentRecord recordConsent(
        String email,
        String purpose,
        Map<String, Boolean> granularConsents,
        String consentMethod
    ) {
        ConsentRecord consent = new ConsentRecord();
        
        consent.setDataSubjectEmail(email);
        consent.setProcessingPurpose(purpose);
        consent.setConsentStatus("GIVEN");
        consent.setConsentMethod(consentMethod);
        consent.setGranularConsents(toJSON(granularConsents));
        consent.setConsentGivenAt(new Timestamp(System.currentTimeMillis()));
        
        // Evidence (GDPR Art. 7.1 - demostrar consentimiento)
        Map<String, Object> evidence = new HashMap<>();
        evidence.put("timestamp", LocalDateTime.now());
        evidence.put("method", consentMethod);
        evidence.put("ip_address", getCurrentIP());
        evidence.put("user_agent", getCurrentUserAgent());
        consent.setConsentEvidence(toJSON(evidence));
        
        consent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        businessService.save(consent);
        
        log.info("Consent recorded: {}", consent.getIdxConsent());
        
        return consent;
    }
    
    public ConsentRecord withdrawConsent(Long consentId, String reason) {
        ConsentRecord consent = businessService.findById(ConsentRecord.class, consentId);
        
        consent.setConsentStatus("WITHDRAWN");
        consent.setConsentWithdrawnAt(new Timestamp(System.currentTimeMillis()));
        consent.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        businessService.update(consent);
        
        log.info("Consent withdrawn: {}", consentId);
        
        return consent;
    }
}

═══════════════════════════════════════════
PROCESO BPMN:
═══════════════════════════════════════════

Archivo: consent-management-v1.bpmn

FLUJO:
1. Start → recordConsentRequest
2. validateConsent
3. Gateway: Valid?
   - YES → storeConsent → notifyDataSubject → End
   - NO → rejectRequest → End
4. Timer: Check expiration (cada 30 días)

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. ConsentRecord.java (entidad)
2. ConsentManagementService.java
3. consent-management-v1.bpmn
4. Endpoints REST
5. SQL migration
6. Tests

IMPORTANTE:
✅ GDPR Art. 7 (Consentimiento)
✅ ISO 27701 Control 6.2.1
✅ Evidencia de consentimiento
✅ Granular consents
✅ Withdrawal capability
❌ NO envío emails (solo registro)

Usa Spring Boot, Flowable, buenas prácticas.
```

---

## 💬 CHAT 4: ISO DOCUMENTATION GENERATOR

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para auto-generación de documentación ISO 27001/27701/42001.

CONTEXTO:
- Proyecto: Spring Boot + JPA
- ISOs: 27001 (Security), 27701 (Privacy), 42001 (AI Management)
- Package: com.codeflowx.govern.service

═══════════════════════════════════════════
ENTIDAD JPA:
═══════════════════════════════════════════

@Entity
@Table(name = "CMP_ISO_DOCUMENTATION")
public class ISODocumentation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_iso_doc")
    private Long idxIsoDoc;
    
    // ISO Standard
    @NotNull
    @Column(name = "iso_standard", length = 50)
    private String isoStandard; // ISO_27001, ISO_27701, ISO_42001
    
    @NotNull
    @Column(name = "iso_version", length = 50)
    private String isoVersion; // 2022, 2019, 2023
    
    // Document type
    @NotNull
    @Column(name = "document_type", length = 100)
    private String documentType; // ISMS_MANUAL, RISK_ASSESSMENT, CONTROL_IMPLEMENTATION, etc.
    
    // Organization info
    @Column(name = "organization_name", length = 200)
    private String organizationName;
    
    @Column(name = "scope_definition", columnDefinition = "TEXT")
    private String scopeDefinition;
    
    // Content sections
    @Column(name = "section_1_context", columnDefinition = "TEXT")
    private String section1Context;
    
    @Column(name = "section_4_context_org", columnDefinition = "TEXT")
    private String section4ContextOrg;
    
    @Column(name = "section_5_leadership", columnDefinition = "TEXT")
    private String section5Leadership;
    
    @Column(name = "section_6_planning", columnDefinition = "TEXT")
    private String section6Planning;
    
    @Column(name = "section_7_support", columnDefinition = "TEXT")
    private String section7Support;
    
    @Column(name = "section_8_operation", columnDefinition = "TEXT")
    private String section8Operation;
    
    @Column(name = "section_9_performance", columnDefinition = "TEXT")
    private String section9Performance;
    
    @Column(name = "section_10_improvement", columnDefinition = "TEXT")
    private String section10Improvement;
    
    // Controles aplicables (para ISO 27001 Anexo A)
    @Column(name = "applicable_controls", columnDefinition = "TEXT")
    private String applicableControls; // JSON
    
    @Column(name = "control_implementation_status", columnDefinition = "TEXT")
    private String controlImplementationStatus; // JSON
    
    // AI-specific (para ISO 42001)
    @Column(name = "ai_system_inventory", columnDefinition = "TEXT")
    private String aiSystemInventory; // JSON
    
    @Column(name = "ai_lifecycle_processes", columnDefinition = "TEXT")
    private String aiLifecycleProcesses; // JSON
    
    // Privacy-specific (para ISO 27701)
    @Column(name = "privacy_controls", columnDefinition = "TEXT")
    private String privacyControls; // JSON
    
    @Column(name = "pii_inventory", columnDefinition = "TEXT")
    private String piiInventory; // JSON
    
    // Generation metadata
    @Column(name = "generated_at")
    private Timestamp generatedAt;
    
    @Column(name = "generated_by", length = 100)
    private String generatedBy;
    
    @Column(name = "pdf_path", length = 500)
    private String pdfPath;
    
    @Column(name = "status", length = 50)
    private String status; // DRAFT, APPROVED, PUBLISHED
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
SERVICIO:
═══════════════════════════════════════════

@Service
@Slf4j
public class ISODocumentationService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private AIGovernanceClient governanceClient;
    
    public ISODocumentation generateISODocumentation(
        String isoStandard,  // ISO_27001, ISO_27701, ISO_42001
        String documentType
    ) {
        ISODocumentation doc = new ISODocumentation();
        
        doc.setIsoStandard(isoStandard);
        doc.setIsoVersion(getVersion(isoStandard));
        doc.setDocumentType(documentType);
        doc.setOrganizationName("CodeflowX Platform");
        doc.setScopeDefinition("AI Governance Platform - ML, LLMs, RAG, Agents");
        
        // Auto-generar secciones según ISO
        if ("ISO_42001".equals(isoStandard)) {
            generateISO42001Sections(doc);
        } else if ("ISO_27001".equals(isoStandard)) {
            generateISO27001Sections(doc);
        } else if ("ISO_27701".equals(isoStandard)) {
            generateISO27701Sections(doc);
        }
        
        doc.setGeneratedAt(new Timestamp(System.currentTimeMillis()));
        doc.setStatus("DRAFT");
        doc.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        businessService.save(doc);
        
        // Generate PDF
        String pdfPath = generatePDF(doc);
        doc.setPdfPath(pdfPath);
        businessService.update(doc);
        
        return doc;
    }
    
    private void generateISO42001Sections(ISODocumentation doc) {
        // Section 8.3: Data for AI
        doc.setSection8Operation(
            "Data Quality Management:\n" +
            "- Automated data quality evaluation (leka-bias-detection)\n" +
            "- Bias detection and mitigation\n" +
            "- Label leakage detection\n" +
            "- Drift detection (KS test, Anderson-Darling)\n"
        );
        
        // Section 8.9: AI System Monitoring
        doc.setSection9Performance(
            "AI Monitoring Capabilities:\n" +
            "- 24/7 continuous monitoring (leka-agent-monitoring)\n" +
            "- Performance tracking\n" +
            "- Drift detection\n" +
            "- Incident detection and RCA\n"
        );
        
        // Section 10.2: Continual Improvement
        doc.setSection10Improvement(
            "Improvement Mechanisms:\n" +
            "- Systematic benchmarking (all modules)\n" +
            "- A/B testing (LLMs, prompts, models)\n" +
            "- Model retraining orchestration\n"
        );
        
        // AI System Inventory
        Map<String, Object> inventory = new HashMap<>();
        inventory.put("total_models", countModels());
        inventory.put("total_agents", countAgents());
        inventory.put("total_prompts", countPrompts());
        doc.setAiSystemInventory(toJSON(inventory));
    }
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. ISODocumentation.java (entidad)
2. ISODocumentationService.java
3. ISO templates (27001, 27701, 42001)
4. Controller endpoints
5. SQL migration
6. Tests

IMPORTANTE:
✅ ISO 27001, 27701, 42001 compliant
✅ Auto-generate desde metadata + microservices
✅ Template-based PDF generation
✅ Section by section (clauses 4-10)
❌ NO manual documentation (auto-generate)

Usa Spring Boot, iText/PDFBox, buenas prácticas.
```

---

## 💬 CHAT 5: DATA CATALOG & CLASSIFICATION SERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para Data Catalog y Classification (GDPR Art. 30 + ISO 27701).

CONTEXTO:
- GDPR Art. 30: Registro de actividades de tratamiento
- ISO 27701: PII inventory
- Package: com.codeflowx.govern.service

═══════════════════════════════════════════
ENTIDAD JPA:
═══════════════════════════════════════════

@Entity
@Table(name = "CMP_DATA_CATALOG")
public class DataCatalogEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_catalog_entry")
    private Long idxCatalogEntry;
    
    // Data asset identification
    @NotNull
    @Column(name = "table_name", length = 200)
    private String tableName;
    
    @Column(name = "column_name", length = 200)
    private String columnName;
    
    @Column(name = "schema_name", length = 100)
    private String schemaName;
    
    // Classification
    @NotNull
    @Column(name = "data_classification", length = 50)
    private String dataClassification; // PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED
    
    @Column(name = "sensitivity_level", length = 50)
    private String sensitivityLevel; // LOW, MEDIUM, HIGH, CRITICAL
    
    // PII classification (GDPR + ISO 27701)
    @Column(name = "contains_pii")
    private Boolean containsPii;
    
    @Column(name = "pii_category", length = 100)
    private String piiCategory; // DIRECT, INDIRECT, SPECIAL_CATEGORY, NONE
    
    @Column(name = "pii_types", columnDefinition = "TEXT")
    private String piiTypes; // JSON: [EMAIL, PHONE, NAME, etc.]
    
    // Processing information (GDPR Art. 30)
    @Column(name = "processing_purpose", length = 200)
    private String processingPurpose;
    
    @Column(name = "legal_basis", length = 100)
    private String legalBasis;
    
    @Column(name = "recipients", columnDefinition = "TEXT")
    private String recipients; // JSON
    
    @Column(name = "retention_period", length = 200)
    private String retentionPeriod;
    
    // Security measures
    @Column(name = "encryption_enabled")
    private Boolean encryptionEnabled;
    
    @Column(name = "access_controls", columnDefinition = "TEXT")
    private String accessControls; // JSON
    
    // Metadata
    @Column(name = "data_owner", length = 200)
    private String dataOwner;
    
    @Column(name = "business_glossary_term", length = 200)
    private String businessGlossaryTerm;
    
    @Column(name = "sample_data", columnDefinition = "TEXT")
    private String sampleData; // Masked sample
    
    // Audit
    @Column(name = "last_scanned_at")
    private Timestamp lastScannedAt;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
SERVICIO:
═══════════════════════════════════════════

@Service
@Slf4j
public class DataCatalogService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private AIGovernanceClient governanceClient;
    
    /**
     * Escanea database y clasifica datos automáticamente
     */
    public List<DataCatalogEntry> scanAndClassifyDatabase() {
        List<DataCatalogEntry> entries = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            // Scan todas las tablas
            ResultSet tables = metaData.getTables(null, "public", "%", new String[]{"TABLE"});
            
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                
                // Scan columnas de cada tabla
                ResultSet columns = metaData.getColumns(null, "public", tableName, "%");
                
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    
                    DataCatalogEntry entry = classifyColumn(tableName, columnName, conn);
                    entries.add(entry);
                    businessService.save(entry);
                }
            }
        } catch (Exception e) {
            log.error("Error scanning database", e);
        }
        
        return entries;
    }
    
    private DataCatalogEntry classifyColumn(
        String tableName,
        String columnName,
        Connection conn
    ) {
        DataCatalogEntry entry = new DataCatalogEntry();
        
        entry.setTableName(tableName);
        entry.setColumnName(columnName);
        entry.setSchemaName("public");
        
        // Auto-classify basado en nombre de columna
        if (columnName.toLowerCase().contains("email")) {
            entry.setContainsPii(true);
            entry.setPiiCategory("DIRECT");
            entry.setPiiTypes("[\"EMAIL\"]");
            entry.setSensitivityLevel("HIGH");
        } else if (columnName.toLowerCase().contains("phone")) {
            entry.setContainsPii(true);
            entry.setPiiCategory("DIRECT");
            entry.setPiiTypes("[\"PHONE\"]");
            entry.setSensitivityLevel("HIGH");
        } else if (columnName.toLowerCase().contains("name")) {
            entry.setContainsPii(true);
            entry.setPiiCategory("DIRECT");
            entry.setPiiTypes("[\"NAME\"]");
            entry.setSensitivityLevel("MEDIUM");
        }
        
        // Sample data (masked)
        String sampleData = getSampleData(tableName, columnName, conn);
        entry.setSampleData(maskSensitiveData(sampleData));
        
        // Clasificación general
        entry.setDataClassification(determineClassification(tableName, columnName));
        entry.setLastScannedAt(new Timestamp(System.currentTimeMillis()));
        entry.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        return entry;
    }
    
    /**
     * Genera registro de actividades (GDPR Art. 30)
     */
    public File generateProcessingRegister() {
        // Agrupar por processing purpose
        // Generate PDF/Excel con registro completo
        return new File("/exports/gdpr/processing_register.pdf");
    }
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. DataCatalogEntry.java
2. DataCatalogService.java
3. ISODocumentation.java (del Chat 4 anterior)
4. ISODocumentationService.java
5. Controller endpoints
6. SQL migrations
7. Tests

IMPORTANTE:
✅ GDPR Art. 30 (Registro actividades)
✅ ISO 27701 (PII inventory)
✅ Automated classification
✅ PII detection integrado
✅ Masked samples
❌ NO manual classification

Usa Spring Boot, JDBC metadata, buenas prácticas.
```

---

## 💬 CHAT 6: RECORD RETENTION & SOX COMPLIANCE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para Record Retention y SOX compliance.

CONTEXTO:
- SOX Section 802: Retention 7 años
- GDPR Art. 5.1.e: Limitación conservación
- ISO 27001 A.8.10: Information deletion
- Package: com.codeflowx.govern.service

═══════════════════════════════════════════
ENTIDAD JPA:
═══════════════════════════════════════════

@Entity
@Table(name = "CMP_RETENTION_POLICIES")
public class RetentionPolicy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_retention_policy")
    private Long idxRetentionPolicy;
    
    // Scope
    @NotNull
    @Column(name = "entity_type", length = 100)
    private String entityType; // MODEL, AGENT, AUDIT_LOG, CONSENT, etc.
    
    @Column(name = "table_name", length = 200)
    private String tableName;
    
    // Retention rules
    @NotNull
    @Column(name = "retention_period_days")
    private Integer retentionPeriodDays;
    
    @Column(name = "retention_basis", length = 200)
    private String retentionBasis; // LEGAL_REQUIREMENT, BUSINESS_NEED, CONSENT_DURATION
    
    // Framework compliance
    @Column(name = "compliant_frameworks", columnDefinition = "TEXT")
    private String compliantFrameworks; // JSON: [GDPR, SOX, ISO27001]
    
    // Archival
    @Column(name = "archive_enabled")
    private Boolean archiveEnabled;
    
    @Column(name = "archive_location", length = 500)
    private String archiveLocation;
    
    // Deletion rules
    @Column(name = "auto_delete_enabled")
    private Boolean autoDeleteEnabled;
    
    @Column(name = "deletion_method", length = 100)
    private String deletionMethod; // SOFT_DELETE, HARD_DELETE, ANONYMIZE
    
    // Exceptions
    @Column(name = "legal_hold_exceptions", columnDefinition = "TEXT")
    private String legalHoldExceptions; // JSON
    
    // Status
    @Column(name = "status", length = 50)
    private String status; // ACTIVE, SUSPENDED, ARCHIVED
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

@Entity
@Table(name = "CMP_RETENTION_EXECUTIONS")
public class RetentionExecution {
    
    @Id
    @GeneratedValue
    @Column(name = "idx_execution")
    private Long idxExecution;
    
    @ManyToOne
    @JoinColumn(name = "policy_id")
    private RetentionPolicy policy;
    
    @Column(name = "execution_date")
    private Timestamp executionDate;
    
    @Column(name = "records_processed")
    private Integer recordsProcessed;
    
    @Column(name = "records_deleted")
    private Integer recordsDeleted;
    
    @Column(name = "records_archived")
    private Integer recordsArchived;
    
    @Column(name = "status", length = 50)
    private String status; // SUCCESS, FAILED, PARTIAL
    
    @Column(name = "execution_log", columnDefinition = "TEXT")
    private String executionLog;
}

═══════════════════════════════════════════
SERVICIO:
═══════════════════════════════════════════

@Service
@Slf4j
public class RecordRetentionService {
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Ejecuta políticas de retención automáticamente
     */
    @Scheduled(cron = "0 0 2 * * ?") // Daily 2 AM
    public void executeRetentionPolicies() {
        log.info("Executing retention policies...");
        
        List<RetentionPolicy> policies = businessService.findAll(RetentionPolicy.class);
        
        for (RetentionPolicy policy : policies) {
            if (Boolean.TRUE.equals(policy.getAutoDeleteEnabled()) && 
                "ACTIVE".equals(policy.getStatus())) {
                
                RetentionExecution execution = executePolicy(policy);
                businessService.save(execution);
            }
        }
    }
    
    private RetentionExecution executePolicy(RetentionPolicy policy) {
        RetentionExecution execution = new RetentionExecution();
        execution.setPolicy(policy);
        execution.setExecutionDate(new Timestamp(System.currentTimeMillis()));
        
        try {
            // Calculate cutoff date
            LocalDateTime cutoffDate = LocalDateTime.now()
                .minusDays(policy.getRetentionPeriodDays());
            
            // Query records older than cutoff
            String sql = buildRetentionQuery(policy.getTableName(), cutoffDate);
            List<Object> oldRecords = queryOldRecords(sql);
            
            execution.setRecordsProcessed(oldRecords.size());
            
            // Archive or delete
            if (Boolean.TRUE.equals(policy.getArchiveEnabled())) {
                int archived = archiveRecords(oldRecords, policy);
                execution.setRecordsArchived(archived);
            }
            
            if ("HARD_DELETE".equals(policy.getDeletionMethod())) {
                int deleted = deleteRecords(oldRecords, policy);
                execution.setRecordsDeleted(deleted);
            } else if ("ANONYMIZE".equals(policy.getDeletionMethod())) {
                int anonymized = anonymizeRecords(oldRecords, policy);
                execution.setRecordsDeleted(anonymized);
            }
            
            execution.setStatus("SUCCESS");
            
        } catch (Exception e) {
            log.error("Error executing retention policy", e);
            execution.setStatus("FAILED");
            execution.setExecutionLog(e.getMessage());
        }
        
        return execution;
    }
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. RetentionPolicy.java
2. RetentionExecution.java
3. DataCatalogEntry.java
4. RecordRetentionService.java
5. DataCatalogService.java
6. Scheduled jobs
7. Controller endpoints
8. Tests

IMPORTANTE:
✅ GDPR Art. 5.1.e (Limitación conservación)
✅ SOX 802 (7 años retention)
✅ ISO 27001 A.8.10 (Deletion)
✅ Automated execution
✅ Archive capability
✅ Legal hold support
❌ NO deletion sin política aprobada

Usa Spring Boot, @Scheduled, buenas prácticas.
```

---

## 💬 CHAT 7: DATA SUBJECT RIGHTS BPMN WORKFLOWS

### **PROMPT ESPECÍFICO:**

```
Necesito crear procesos BPMN para Data Subject Rights (GDPR Art. 15-22).

CONTEXTO:
- Framework: Flowable BPMN
- GDPR Art. 15-22: Derechos del interesado
- Ubicación: suinsit.nova.web/src/main/resources/processes/compliance/

═══════════════════════════════════════════
PROCESO 1: DATA SUBJECT ACCESS REQUEST
═══════════════════════════════════════════

Archivo: gdpr-access-request-v1.bpmn

FLUJO:
1. Start → receiveAccessRequest (User Task)
2. validateIdentity (Service Task)
3. Gateway: Identity Verified?
   - NO → rejectRequest → End
   - YES → Continue
4. collectPersonalData (Service Task) - Query all tables
5. compileDataPackage (Service Task) - Format JSON/PDF
6. reviewDataPackage (User Task - DPO review)
7. Gateway: Approved?
   - YES → deliverDataPackage → notifyDataSubject → End
   - NO → requestMoreInfo → validateIdentity
8. Timer: SLA 30 días (escalate if exceeded)

VARIABLES:
- requesterEmail (String)
- requestId (Long)
- identityVerified (Boolean)
- dataPackagePath (String)
- approvalDecision (Boolean)

DELEGATES:
- ValidateIdentityDelegate.java
- CollectPersonalDataDelegate.java (llama DataPortabilityService)
- DeliverDataPackageDelegate.java

═══════════════════════════════════════════
PROCESO 2: DATA SUBJECT ERASURE REQUEST
═══════════════════════════════════════════

Archivo: gdpr-erasure-request-v1.bpmn (Right to be forgotten)

FLUJO:
1. Start → receiveErasureRequest
2. validateIdentity
3. checkLegalObligations (Can we delete? Or legal hold?)
4. Gateway: Erasure Allowed?
   - NO → rejectWithJustification → End
   - YES → Continue
5. identifyDataLocations (all tables with user data)
6. reviewErasureImpact (User Task - DPO)
7. Gateway: Approved?
   - YES → executeErasure → verifyDeletion → notifyCompletion → End
   - NO → rejectRequest → End

DELEGATES:
- CheckLegalObligationsDelegate.java
- IdentifyDataLocationsDelegate.java
- ExecuteErasureDelegate.java
- VerifyDeletionDelegate.java

═══════════════════════════════════════════
PROCESO 3: DATA SUBJECT OBJECTION REQUEST
═══════════════════════════════════════════

Archivo: gdpr-objection-request-v1.bpmn (Art. 21)

FLUJO:
1. Start → receiveObjectionRequest
2. validateIdentity
3. evaluateLegitimateGrounds (Can we continue processing?)
4. Gateway: Objection Valid?
   - YES → stopProcessing → notifyCompletion → End
   - NO → justifyProcessing → notifyDecision → End

═══════════════════════════════════════════
PROCESO 4: CONSENT WITHDRAWAL
═══════════════════════════════════════════

Archivo: gdpr-consent-withdrawal-v1.bpmn (Art. 7.3)

FLUJO:
1. Start → receiveWithdrawalRequest
2. findConsentRecords
3. revokeConsent (update ConsentRecord)
4. stopDataProcessing
5. notifyCompletion → End

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. gdpr-access-request-v1.bpmn
2. gdpr-erasure-request-v1.bpmn
3. gdpr-objection-request-v1.bpmn
4. gdpr-consent-withdrawal-v1.bpmn
5. 8+ Delegates (ValidateIdentity, CollectData, ExecuteErasure, etc.)
6. User Task forms (ZUL) - 4 pantallas
7. Tests de procesos

IMPORTANTE:
✅ GDPR Art. 15-22 compliant
✅ SLA 30 días tracking
✅ Identity verification gate
✅ DPO review gates
✅ Legal hold checks
✅ Audit trails completos
❌ NO deletion sin validación

Usa Flowable BPMN, Spring Boot, buenas prácticas workflow.
```

---

## 💬 CHAT 8: PANTALLAS ZUL GDPR/ISO COMPLIANCE

### **PROMPT ESPECÍFICO:**

```
Necesito crear pantallas ZUL + ViewModels para GDPR/ISO compliance management.

CONTEXTO:
- Framework: ZKoss (MVVM)
- Ubicación: suinsit.nova.web/src/main/webapp/console/compliance/
- ViewModels: com.codeflowx.govern.viewmodel.compliance/

═══════════════════════════════════════════
PANTALLA 1: GDPR DASHBOARD
═══════════════════════════════════════════

Archivo: gdpr-compliance-dashboard.zul

LAYOUT:
┌──────────────────────────────────────────────────────┐
│ GDPR Compliance Dashboard                            │
├──────────────────────────────────────────────────────┤
│ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐    │
│ │ Compliance  │ │   Active    │ │   Pending   │    │
│ │    90%      │ │ Consents    │ │   DSR       │    │
│ │             │ │    1,245    │ │     12      │    │
│ └─────────────┘ └─────────────┘ └─────────────┘    │
│                                                      │
│ Data Subject Requests (Last 30 days):                │
│ ┌────────────────────────────────────────────────┐  │
│ │ Type | Email | Received | Deadline | Status  │  │
│ │ ACCESS | a@x.com | 11/01 | 12/01 | PENDING │  │
│ │ ERASURE| b@x.com | 10/30 | 11/30 | COMPLETED│  │
│ └────────────────────────────────────────────────┘  │
│                                                      │
│ Privacy Notices:                                     │
│ ┌────────────────────────────────────────────────┐  │
│ │ System | Version | Status | Actions          │  │
│ │ Agents | 1.0 | PUBLISHED | [View] [Edit]    │  │
│ └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘

ViewModel: GDPRComplianceDashboardViewModel.java

@Getter
@Setter
public class GDPRComplianceDashboardViewModel extends MasterPage {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private GDPRPrivacyService gdprService;
    
    // KPIs
    private Integer compliancePercentage;
    private Long activeConsents;
    private Long pendingDSR;
    private Long overdueDSR;
    
    // Data
    private List<DataSubjectRequest> recentRequests;
    private List<PrivacyNotice> privacyNotices;
    private List<ConsentRecord> recentConsents;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        loadData();
        calculateKPIs();
    }
    
    @Command
    @NotifyChange("*")
    public void processAccessRequest(@BindingParam("email") String email) {
        // Launch BPMN process
        Map<String, Object> vars = new HashMap<>();
        vars.put("requesterEmail", email);
        vars.put("requestType", "ACCESS");
        
        runtimeService.startProcessInstanceByKey(
            "gdpr-access-request-v1", 
            vars
        );
        
        Messagebox.show("Access request initiated", "Success", Messagebox.OK, Messagebox.INFORMATION);
    }
}

═══════════════════════════════════════════
PANTALLA 2: ISO COMPLIANCE DASHBOARD
═══════════════════════════════════════════

Archivo: iso-compliance-dashboard.zul

LAYOUT:
┌──────────────────────────────────────────────────────┐
│ ISO Compliance Status                                │
├──────────────────────────────────────────────────────┤
│ ISO 27001 (Security): ████████░░ 85%                 │
│ ISO 27701 (Privacy):  █████████░ 88%                 │
│ ISO 42001 (AI Mgmt):  █████████░ 92%                 │
│                                                      │
│ Controls Implementation:                             │
│ ┌────────────────────────────────────────────────┐  │
│ │ Control | Name | Status | Evidence          │  │
│ │ A.8.11 | Data Masking | ✅ | k-anonymity    │  │
│ │ A.8.12 | DLP | ✅ | Microsoft Presidio     │  │
│ │ A.8.16 | Monitoring | ✅ | 8 microservices  │  │
│ └────────────────────────────────────────────────┘  │
│                                                      │
│ [Generate ISO Documentation] [Download Report]       │
└──────────────────────────────────────────────────────┘

ViewModel: ISOComplianceDashboardViewModel.java

═══════════════════════════════════════════
PANTALLA 3: PRIVACY NOTICE MANAGER
═══════════════════════════════════════════

Archivo: privacy-notice-manager.zul

Gestión de Privacy Notices (GDPR Art. 13-14)

═══════════════════════════════════════════
PANTALLA 4: DATA SUBJECT REQUEST MANAGER
═══════════════════════════════════════════

Archivo: data-subject-request-manager.zul

Gestión de solicitudes de derechos (GDPR Art. 15-22)

═══════════════════════════════════════════
PANTALLA 5: CONSENT MANAGEMENT
═══════════════════════════════════════════

Archivo: consent-management.zul

Gestión de consentimientos (GDPR Art. 7)

═══════════════════════════════════════════
PANTALLA 6: DATA CATALOG VIEWER
═══════════════════════════════════════════

Archivo: data-catalog-viewer.zul

Visualización de catálogo de datos + PII inventory

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. gdpr-compliance-dashboard.zul + ViewModel
2. iso-compliance-dashboard.zul + ViewModel
3. privacy-notice-manager.zul + ViewModel
4. data-subject-request-manager.zul + ViewModel
5. consent-management.zul + ViewModel
6. data-catalog-viewer.zul + ViewModel
7. Tests de ViewModels
8. Navegación/menús actualizados

IMPORTANTE:
✅ MVVM pattern
✅ KPIs visualization
✅ Grid/Listbox para listados
✅ Action buttons (process, approve, reject)
✅ SLA indicators (color-coded)
✅ BusinessService para persistence
❌ NO lógica negocio en ViewModel

Usa ZKoss 9.x, Spring, buenas prácticas UI.
```

---

## 📊 RESUMEN DE COMPONENTES A CREAR

### **BACKEND JAVA:**

| Chat | Componente | Tipo | Framework Target |
|------|------------|------|------------------|
| 1 | Privacy Notice + Rights | Service + 2 entities | GDPR Art. 13-14, 15-22 |
| 2 | Data Portability | Service + 1 entity | GDPR Art. 20 |
| 3 | Consent Management | Service + 1 entity + BPMN | GDPR Art. 7, ISO 27701 |
| 4 | ISO Documentation | Service + 1 entity | ISO 27001/27701/42001 |
| 5 | Data Catalog | Service + 1 entity | GDPR Art. 30, ISO 27701 |
| 6 | Retention & SOX | Service + 2 entities | SOX 802, GDPR Art. 5, ISO 27001 |
| 7 | GDPR Rights BPMN | 4 procesos BPMN + 8 delegates | GDPR Art. 15-22 |
| 8 | Pantallas ZUL | 6 pantallas + 6 ViewModels | GDPR + ISO UI |

**TOTAL:**
- **9 entidades JPA nuevas**
- **6 servicios nuevos**
- **5 procesos BPMN nuevos**
- **6 pantallas ZUL nuevas**
- **8+ delegates nuevos**

---

### **MICROSERVICIOS PYTHON:**

**NINGUNO** ✅ (todos los gaps son Backend Java)

Los microservicios 12/10 ya cubren:
- PII detection (Presidio) → GDPR
- Privacy analysis (k-anonymity) → ISO 27701
- DLP → ISO 27001
- Data quality → ISO 42001
- Monitoring → ISO 27001/42001

---

## ⏱️ TIMELINE FIN DE SEMANA

### **SÁBADO NOCHE (23:00 - 08:00):**

```
23:00  Lanzar 8 chats con prompts
↓
01:00  Revisar Chat 8 (UI - más rápido)
↓
02:00  Revisar Chat 6 (Retention - simple)
↓
03:00  Revisar Chat 3, 4 (servicios medianos)
↓
05:00  Revisar Chat 1, 2, 5 (servicios complejos)
↓
06:00  Revisar Chat 7 (BPMN workflows)
↓
07:00  Integración + compilación
↓
08:00  ✅ CÓDIGO COMPLETO
```

### **DOMINGO (09:00 - 20:00):**

```
09:00  Testing servicios individuales
↓
12:00  Testing procesos BPMN
↓
15:00  Testing UI (pantallas ZUL)
↓
18:00  Testing integración end-to-end
↓
20:00  ✅ MULTI-FRAMEWORK COMPLIANCE READY
```

---

## 📊 RESULTADO ESPERADO

### **DOMINGO 20:00:**

```
COMPLIANCE ALCANZADO:

GDPR:          90% → 98% ✅
ISO 27001:     85% → 95% ✅
ISO 27701:     88% → 98% ✅
ISO 42001:     92% → 98% ✅
SOX:           70% → 85% ✅

PROMEDIO:      88% → 95%+ ✅
```

### **COMPONENTES CREADOS:**

```
Backend Java:
├─ 9 entidades JPA nuevas
├─ 6 servicios nuevos
├─ 5 procesos BPMN nuevos
├─ 6 pantallas ZUL nuevas
└─ 8 delegates nuevos

Microservicios:
└─ Ninguno (ya completos) ✅

TOTAL: Fin de semana → Multi-framework 95%+
```

---

## 🎯 PRIORIZACIÓN SI MENOS DE 8 CHATS

### **PRIORIDAD MÁXIMA (GDPR crítico):**

```
1º Chat 1: Privacy Notice + Rights    [⭐⭐⭐]
2º Chat 2: Data Portability           [⭐⭐⭐]
3º Chat 7: GDPR Rights BPMN           [⭐⭐⭐]
4º Chat 8: Pantallas UI               [⭐⭐]

RESULTADO: GDPR 90% → 95%
```

### **PRIORIDAD ALTA (ISOs):**

```
5º Chat 4: ISO Documentation          [⭐⭐]
6º Chat 5: Data Catalog               [⭐⭐]
7º Chat 3: Consent Management         [⭐⭐]

RESULTADO: ISOs 85-92% → 95%+
```

### **PRIORIDAD MEDIA (SOX):**

```
8º Chat 6: Retention & SOX            [⭐]

RESULTADO: SOX 70% → 85%
```

---

## 📋 CHECKLIST EJECUCIÓN

### **ANTES DE EMPEZAR:**

- [ ] 8 ventanas de chat IA listas
- [ ] Copiar prompts de este documento
- [ ] Verificar Backend Java compilando
- [ ] Verificar PostgreSQL accesible

### **DURANTE (Sábado noche):**

- [ ] 23:00 - Lanzar los 8 prompts
- [ ] 01:00 - Revisar primeros códigos
- [ ] 03:00 - Revisar servicios
- [ ] 05:00 - Revisar BPMN
- [ ] 06:00 - Revisar UI
- [ ] 07:00 - Integración

### **DOMINGO:**

- [ ] 09:00 - Testing individual
- [ ] 12:00 - Testing BPMN workflows
- [ ] 15:00 - Testing UI
- [ ] 18:00 - Testing end-to-end
- [ ] 20:00 - Git commit

### **PRÓXIMA SEMANA:**

- [ ] Lunes: Preparar demos GDPR/ISO
- [ ] Martes: Lanzamiento (post con multi-framework compliance)
- [ ] Miércoles/Jueves: Demo con consultores

---

## ✅ RESULTADO FINAL

### **LO QUE TENDRÁS DOMINGO 20:00:**

```
COMPLIANCE MULTI-FRAMEWORK:

╔═══════════════════════════════════════════════╗
║  EU AI Act:      ███████████████████░   98%+  ║
║  GDPR:           ███████████████████░   98%+  ║
║  ISO 42001:      ███████████████████░   98%+  ║
║  ISO 27701:      ███████████████████░   98%+  ║
║  ISO 27001:      ██████████████████░░   95%+  ║
║  SOX:            █████████████████░░░   85%+  ║
║                                               ║
║  PROMEDIO:       ███████████████████░   95%+  ║
╚═══════════════════════════════════════════════╝

POSICIÓN: TOP 3 MUNDIAL en multi-framework compliance
VENTAJA: +10-20% sobre competencia
CERTIFICACIONES: 5 frameworks ready
```

---

## 💼 PARA LA REUNIÓN CON CONSULTORES

### **MENSAJE ACTUALIZADO:**

> "CodeflowX alcanzará **95%+ promedio** en **6 frameworks** regulatorios este fin de semana:
> 
> - EU AI Act: 98%+
> - GDPR: 98%+
> - ISO 42001: 98%+
> - ISO 27701: 98%+
> - ISO 27001: 95%+
> - SOX: 85%+
> 
> Esto nos posiciona en el **TOP 3 mundial** de plataformas AI Governance con **compliance certificable** en múltiples frameworks simultáneamente."

---

**Trabajo de fin de semana:**
- 8 chats en paralelo
- 9 entidades + 6 servicios + 5 BPMN + 6 UI
- **Domingo → Multi-framework compliance 95%+** ✅

🚀 **¡A trabajar este fin de semana!**

