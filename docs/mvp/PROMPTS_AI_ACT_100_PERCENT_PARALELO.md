# 🏛️ PROMPTS AI ACT 100% COMPLIANCE - DESARROLLO PARALELO

**Fecha:** Sábado 1 Noviembre 2025 (Noche)  
**Objetivo:** 95% → 100% AI Act compliance  
**Estrategia:** 8 chats IA trabajando en paralelo = 8 developers  
**Timeline:** Esta noche → Listo mañana domingo  
**Demo:** Miércoles/Jueves (tiempo suficiente)

---

## 🎯 CONTEXTO GENERAL PARA TODOS LOS CHATS

### **ARQUITECTURA DEL PROYECTO:**

**Ubicaciones clave:**
- **Entidades JPA:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/src/main/java/codeflowx/nocode/persist/entitys/`
- **Aplicación Web:** `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/`
- **ViewModels:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/`
- **Procesos BPMN:** `suinsit.nova.web/src/main/resources/processes/`
- **Delegates:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/workflow/delegates/`
- **Pantallas ZUL:** `suinsit.nova.web/src/main/webapp/console/compliance/`

**Stack tecnológico:**
- Backend: Spring Boot + JPA/Hibernate + PostgreSQL
- UI: ZKoss (MVVM pattern)
- Workflow: Flowable BPMN + Drools
- Framework: EnArt (BusinessService para persistence)

**Convenciones:**
- Prefijo tablas: `GOV_` para governance
- PK autonumérica: `idx[nombre_entidad]`
- Tercera forma normal
- SOLID + KISS + Arquitectura hexagonal

---

## 📋 DISTRIBUCIÓN DE CHATS

```
Chat 1: AIActDocumentationService + Entity      [5 horas]
Chat 2: ConformityDeclarationService + Entity   [5 horas]
Chat 3: AIActLogExportService                   [3 horas]
Chat 4: Conformity Assessment BPMN Process      [4 horas]
Chat 5: Pantallas ZUL + ViewModels              [6 horas]
Chat 6: Deep Fake Detection Microservice        [6 horas]
Chat 7: EU Database Registration (preparatorio) [4 horas]
Chat 8: AI Interpreter Extension (disclosure)   [2 horas]

TOTAL: 8 chats = 1 noche trabajo paralelo
```

---

## 💬 CHAT 1: AI ACT DOCUMENTATION SERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para auto-generación de documentación técnica según EU AI Act Annex IV.

CONTEXTO:
- Proyecto: Spring Boot + JPA + PostgreSQL
- Framework: EnArt (BusinessService para persistence)
- Ubicación entidades: nocode.service/src/main/java/codeflowx/nocode/persist/entitys/
- Package servicios: com.codeflowx.govern.service
- EU AI Act Artículo 11: Technical Documentation mandatoria

═══════════════════════════════════════════
ENTIDAD JPA A CREAR:
═══════════════════════════════════════════

Ubicación: nocode.service/entitys/governance/AIActTechnicalDocumentation.java

@Entity
@Table(name = "GOV_AIACT_TECHNICAL_DOCS")
public class AIActTechnicalDocumentation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_technical_doc")
    private Long idxTechnicalDoc;
    
    // Identificación del sistema IA
    @NotNull
    @Column(name = "entity_type", length = 50)
    private String entityType; // MODEL, AGENT, PROMPT, RAG_SYSTEM
    
    @NotNull
    @Column(name = "entity_id")
    private Long entityId;
    
    @NotNull
    @Column(name = "system_name", length = 200)
    private String systemName;
    
    @NotNull
    @Column(name = "version", length = 50)
    private String version;
    
    // Secciones mandatorias Annex IV
    @Column(name = "system_description", columnDefinition = "TEXT")
    private String systemDescription;
    
    @Column(name = "intended_purpose", columnDefinition = "TEXT")
    private String intendedPurpose;
    
    @Column(name = "development_process", columnDefinition = "TEXT")
    private String developmentProcess;
    
    @Column(name = "data_governance", columnDefinition = "TEXT")
    private String dataGovernance;
    
    @Column(name = "validation_procedures", columnDefinition = "TEXT")
    private String validationProcedures;
    
    @Column(name = "testing_procedures", columnDefinition = "TEXT")
    private String testingProcedures;
    
    @Column(name = "monitoring_measures", columnDefinition = "TEXT")
    private String monitoringMeasures;
    
    @Column(name = "human_oversight", columnDefinition = "TEXT")
    private String humanOversight;
    
    @Column(name = "risk_management", columnDefinition = "TEXT")
    private String riskManagement;
    
    @Column(name = "accuracy_robustness", columnDefinition = "TEXT")
    private String accuracyRobustness;
    
    @Column(name = "cybersecurity_measures", columnDefinition = "TEXT")
    private String cybersecurityMeasures;
    
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
    
    // Relación con assessment (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id")
    private ComplianceAssessment assessment;
}

═══════════════════════════════════════════
SERVICIO A CREAR:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/service/AIActDocumentationService.java

@Service
@Slf4j
public class AIActDocumentationService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private AIGovernanceClient aiGovernanceClient;  // Cliente Java de microservicios
    
    /**
     * Genera documentación técnica AI Act Annex IV
     */
    public AIActTechnicalDocumentation generateDocumentation(
        String entityType,
        Long entityId
    ) {
        AIActTechnicalDocumentation doc = new AIActTechnicalDocumentation();
        
        // 1. Cargar metadata de entity
        Map<String, Object> metadata = loadEntityMetadata(entityType, entityId);
        
        doc.setEntityType(entityType);
        doc.setEntityId(entityId);
        doc.setSystemName((String) metadata.get("name"));
        doc.setVersion((String) metadata.get("version"));
        
        // 2. Auto-generar secciones llamando a microservicios
        doc.setSystemDescription(generateSystemDescription(metadata));
        doc.setIntendedPurpose((String) metadata.get("purpose"));
        doc.setDevelopmentProcess(generateDevelopmentSection(entityType, entityId));
        doc.setDataGovernance(generateDataGovernanceSection(entityType, entityId));
        doc.setValidationProcedures(generateValidationSection(entityType, entityId));
        doc.setTestingProcedures(generateTestingSection(entityType, entityId));
        doc.setMonitoringMeasures(generateMonitoringSection(entityType, entityId));
        doc.setHumanOversight(generateOversightSection(entityType));
        doc.setRiskManagement(generateRiskSection(entityType, entityId));
        doc.setAccuracyRobustness(generateAccuracySection(entityType, entityId));
        doc.setCybersecurityMeasures(generateSecuritySection(entityType, entityId));
        
        // 3. Metadata de generación
        doc.setGeneratedAt(new Timestamp(System.currentTimeMillis()));
        doc.setGeneratedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        doc.setStatus("DRAFT");
        doc.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        doc.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        // 4. Guardar
        businessService.save(doc);
        
        // 5. Generar PDF
        String pdfPath = generatePDF(doc);
        doc.setPdfPath(pdfPath);
        businessService.update(doc);
        
        return doc;
    }
    
    private String generateValidationSection(String entityType, Long entityId) {
        StringBuilder validation = new StringBuilder();
        
        try {
            if ("MODEL".equals(entityType)) {
                // Llamar microservicios de evaluación
                BiasAnalysisResponse bias = aiGovernanceClient.analyzeBias(buildBiasRequest(entityId));
                DriftDetectionResponse drift = aiGovernanceClient.detectDrift(buildDriftRequest(entityId));
                
                validation.append("Bias Analysis Results:\n");
                validation.append("- Classification: ").append(bias.getClassification()).append("\n");
                validation.append("- Demographic Parity: ").append(bias.getMetrics().getDemographicParityDifference()).append("\n\n");
                
                validation.append("Drift Detection Results:\n");
                validation.append("- Drift Detected: ").append(drift.getDriftDetected()).append("\n");
            }
        } catch (Exception e) {
            log.warn("Could not retrieve validation results: {}", e.getMessage());
            validation.append("Validation procedures: Automated evaluation via microservices\n");
        }
        
        return validation.toString();
    }
    
    private String generatePDF(AIActTechnicalDocumentation doc) {
        // Usar iText o Apache PDFBox para generar PDF
        // Template: Annex IV structure
        String outputPath = "/docs/ai-act/technical/" + doc.getIdxTechnicalDoc() + ".pdf";
        
        // TODO: Implementar generación PDF con template
        
        return outputPath;
    }
}

═══════════════════════════════════════════
REST CONTROLLER:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/AIActComplianceController.java

@RestController
@RequestMapping("/api/compliance/ai-act")
@Slf4j
public class AIActComplianceController {
    
    @Autowired
    private AIActDocumentationService documentationService;
    
    @PostMapping("/generate-documentation")
    public ResponseEntity<AIActTechnicalDocumentation> generateDocumentation(
        @RequestParam String entityType,
        @RequestParam Long entityId
    ) {
        AIActTechnicalDocumentation doc = documentationService.generateDocumentation(entityType, entityId);
        return ResponseEntity.ok(doc);
    }
    
    @GetMapping("/download-documentation/{docId}")
    public ResponseEntity<Resource> downloadDocumentation(@PathVariable Long docId) {
        File pdf = documentationService.getPDF(docId);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AI_Act_Technical_Documentation.pdf")
            .body(new FileSystemResource(pdf));
    }
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. AIActTechnicalDocumentation.java (entidad JPA)
2. AIActDocumentationService.java (servicio completo)
3. AIActComplianceController.java (REST endpoints)
4. Template Annex IV (estructura PDF)
5. SQL migration para crear tabla GOV_AIACT_TECHNICAL_DOCS
6. Tests unitarios (JUnit)

IMPORTANTE:
✅ Usar BusinessService para persistence
✅ Integrar con AIGovernanceClient (cliente de microservicios)
✅ Tercera forma normal
✅ Prefix GOV_ para tablas
✅ PK autonumérica idx[nombre]
✅ Audit fields (created_at, updated_at)
❌ NO duplicar código (reutilizar cliente microservicios)

Usa Spring Boot, JPA, Hibernate, PostgreSQL, buenas prácticas Java.
```

---

## 💬 CHAT 2: CONFORMITY DECLARATION SERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para auto-generación de EU Declaration of Conformity según AI Act Annex V.

CONTEXTO:
- Proyecto: Spring Boot + JPA + PostgreSQL
- Framework: EnArt (BusinessService)
- Ubicación entidades: nocode.service/entitys/governance/
- Package servicios: com.codeflowx.govern.service
- EU AI Act Artículo 48: EU Declaration of Conformity mandatoria

═══════════════════════════════════════════
ENTIDAD JPA A CREAR:
═══════════════════════════════════════════

Ubicación: nocode.service/entitys/governance/ConformityDeclaration.java

@Entity
@Table(name = "GOV_CONFORMITY_DECLARATIONS")
public class ConformityDeclaration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_declaration")
    private Long idxDeclaration;
    
    // Provider information (mandatory Annex V)
    @NotNull
    @Column(name = "provider_name", length = 200)
    private String providerName;
    
    @Column(name = "provider_address", columnDefinition = "TEXT")
    private String providerAddress;
    
    @Column(name = "provider_country", length = 100)
    private String providerCountry;
    
    @Column(name = "provider_contact", length = 200)
    private String providerContact;
    
    // AI System information
    @NotNull
    @Column(name = "ai_system_name", length = 200)
    private String aiSystemName;
    
    @NotNull
    @Column(name = "ai_system_type", length = 50)
    private String aiSystemType;
    
    @Column(name = "ai_system_version", length = 50)
    private String aiSystemVersion;
    
    @Column(name = "intended_purpose", columnDefinition = "TEXT")
    private String intendedPurpose;
    
    @Column(name = "risk_category", length = 50)
    private String riskCategory; // HIGH_RISK, LIMITED_RISK
    
    // Conformity basis
    @Column(name = "conformity_basis", columnDefinition = "TEXT")
    private String conformityBasis; // Annex VI (internal) or Annex VII (third-party)
    
    @Column(name = "applied_standards", columnDefinition = "TEXT")
    private String appliedStandards; // JSON array
    
    @Column(name = "harmonized_standards", columnDefinition = "TEXT")
    private String harmonizedStandards;
    
    // Compliance per article (boolean)
    @Column(name = "art9_risk_management")
    private Boolean art9RiskManagement;
    
    @Column(name = "art10_data_governance")
    private Boolean art10DataGovernance;
    
    @Column(name = "art11_documentation")
    private Boolean art11Documentation;
    
    @Column(name = "art12_record_keeping")
    private Boolean art12RecordKeeping;
    
    @Column(name = "art13_transparency")
    private Boolean art13Transparency;
    
    @Column(name = "art14_human_oversight")
    private Boolean art14HumanOversight;
    
    @Column(name = "art15_accuracy")
    private Boolean art15Accuracy;
    
    // Scores
    @Column(name = "overall_compliance_score", precision = 5, scale = 2)
    private BigDecimal overallComplianceScore;
    
    @Column(name = "compliance_percentage", precision = 5, scale = 2)
    private BigDecimal compliancePercentage;
    
    // Signature
    @Column(name = "signature_date")
    private Timestamp signatureDate;
    
    @Column(name = "signed_by", length = 200)
    private String signedBy;
    
    @Column(name = "digital_signature", columnDefinition = "TEXT")
    private String digitalSignature; // For eIDAS (fase 2)
    
    // PDF generation
    @Column(name = "pdf_path", length = 500)
    private String pdfPath;
    
    @Column(name = "pdf_generated_at")
    private Timestamp pdfGeneratedAt;
    
    // Status
    @Column(name = "status", length = 50)
    private String status; // DRAFT, SIGNED, PUBLISHED, REVOKED
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id")
    private ComplianceAssessment assessment;
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
SERVICIO A CREAR:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/service/ConformityDeclarationService.java

@Service
@Slf4j
public class ConformityDeclarationService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private AIGovernanceClient governanceClient;
    
    @Value("${company.name:Your Company Name}")
    private String companyName;
    
    @Value("${company.address:Company Address}")
    private String companyAddress;
    
    public ConformityDeclaration generateDeclaration(Long assessmentId) {
        // 1. Cargar assessment
        ComplianceAssessment assessment = businessService.findById(
            ComplianceAssessment.class, 
            assessmentId
        );
        
        if (assessment == null) {
            throw new EntityNotFoundException("Assessment not found: " + assessmentId);
        }
        
        // 2. Crear declaration
        ConformityDeclaration declaration = new ConformityDeclaration();
        
        // Provider info
        declaration.setProviderName(companyName);
        declaration.setProviderAddress(companyAddress);
        declaration.setProviderCountry("Spain"); // O desde config
        declaration.setProviderContact("compliance@company.com");
        
        // AI System info
        declaration.setAiSystemName(assessment.getAssessmentname());
        declaration.setAiSystemType(determineSystemType(assessment));
        declaration.setIntendedPurpose(assessment.getDescription());
        declaration.setRiskCategory("HIGH_RISK");
        
        // Conformity basis
        declaration.setConformityBasis("Internal conformity assessment procedure (Annex VI)");
        declaration.setAppliedStandards(buildAppliedStandards());
        declaration.setHarmonizedStandards("EN ISO/IEC 27001:2022, EN ISO/IEC 27701:2019");
        
        // Compliance per article (obtener de assessment results)
        declaration.setArt9RiskManagement(true);
        declaration.setArt10DataGovernance(true);
        declaration.setArt11Documentation(true);
        declaration.setArt12RecordKeeping(true);
        declaration.setArt13Transparency(true);
        declaration.setArt14HumanOversight(true);
        declaration.setArt15Accuracy(true);
        
        // Scores
        declaration.setOverallComplianceScore(assessment.getOverallscore());
        declaration.setCompliancePercentage(assessment.getCompliancepercentage());
        
        // Signature
        declaration.setSignatureDate(new Timestamp(System.currentTimeMillis()));
        declaration.setSignedBy(getCurrentUser());
        declaration.setStatus("DRAFT");
        
        // Audit
        declaration.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        declaration.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        declaration.setAssessment(assessment);
        
        // 3. Guardar
        businessService.save(declaration);
        
        // 4. Generar PDF
        String pdfPath = generateDeclarationPDF(declaration);
        declaration.setPdfPath(pdfPath);
        declaration.setPdfGeneratedAt(new Timestamp(System.currentTimeMillis()));
        businessService.update(declaration);
        
        log.info("Declaration generated: {}", declaration.getIdxDeclaration());
        
        return declaration;
    }
    
    private String generateDeclarationPDF(ConformityDeclaration declaration) {
        // Generar PDF con iText o Apache PDFBox
        // Template Annex V structure
        
        String outputPath = "/docs/declarations/declaration_" + 
                           declaration.getIdxDeclaration() + ".pdf";
        
        // TODO: PDF generation con template Annex V
        
        return outputPath;
    }
    
    private String buildAppliedStandards() {
        return "{\n" +
               "  \"risk_management\": \"ISO 31000:2018\",\n" +
               "  \"data_governance\": \"ISO/IEC 27001:2022\",\n" +
               "  \"privacy\": \"ISO/IEC 27701:2019\",\n" +
               "  \"ai_governance\": \"ISO/IEC 42001 (draft)\"\n" +
               "}";
    }
}

═══════════════════════════════════════════
REST CONTROLLER (añadir a AIActComplianceController):
═══════════════════════════════════════════

@PostMapping("/generate-declaration")
public ResponseEntity<ConformityDeclaration> generateDeclaration(
    @RequestParam Long assessmentId
) {
    ConformityDeclaration declaration = conformityService.generateDeclaration(assessmentId);
    return ResponseEntity.ok(declaration);
}

@GetMapping("/download-declaration/{declarationId}")
public ResponseEntity<Resource> downloadDeclaration(@PathVariable Long declarationId) {
    ConformityDeclaration declaration = businessService.findById(
        ConformityDeclaration.class, 
        declarationId
    );
    
    File pdf = new File(declaration.getPdfPath());
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, 
            "attachment; filename=EU_Declaration_Conformity.pdf")
        .body(new FileSystemResource(pdf));
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. ConformityDeclaration.java (entidad JPA)
2. ConformityDeclarationService.java (servicio completo)
3. Endpoints en AIActComplianceController.java
4. Template Annex V (PDF template)
5. SQL migration para GOV_CONFORMITY_DECLARATIONS
6. Tests unitarios

IMPORTANTE:
✅ Usar BusinessService
✅ Prefix GOV_ para tabla
✅ PK autonumérica
✅ Integrar con ComplianceAssessment existente
✅ Template Annex V oficial AI Act
❌ NO firma digital avanzada (fase 2)

Usa Spring Boot, JPA, Hibernate, buenas prácticas Java.
```

---

## 💬 CHAT 3: AI ACT LOG EXPORT SERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java para exportar logs en formato AI Act compliant (Artículo 12 - Record-keeping).

CONTEXTO:
- Proyecto: Spring Boot + JPA + PostgreSQL
- Framework: EnArt (BusinessService)
- Package: com.codeflowx.govern.service
- EU AI Act Artículo 12: Logs mandatorios para trazabilidad

═══════════════════════════════════════════
SERVICIO A CREAR:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/service/AIActLogExportService.java

@Service
@Slf4j
public class AIActLogExportService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * Exporta logs en formato AI Act para auditorías
     */
    public File exportLogsForAudit(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String entityType,
        Long entityId,
        String format  // JSON, CSV, XML
    ) {
        log.info("Exporting AI Act logs: {} {} from {} to {}", 
            entityType, entityId, startDate, endDate);
        
        // 1. Recopilar logs de audit tables
        List<AIActLogEntry> logs = collectLogs(startDate, endDate, entityType, entityId);
        
        // 2. Formatear según AI Act
        AIActLogExport export = formatForAIAct(logs, entityType, entityId, startDate, endDate);
        
        // 3. Generar archivo
        File exportFile = generateExportFile(export, format);
        
        log.info("AI Act logs exported: {} entries, file: {}", logs.size(), exportFile.getPath());
        
        return exportFile;
    }
    
    private List<AIActLogEntry> collectLogs(
        LocalDateTime start, LocalDateTime end,
        String entityType, Long entityId
    ) {
        List<AIActLogEntry> logs = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // Query audit tables según entity type
            String sql = buildAuditQuery(entityType, entityId, start, end);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setTimestamp(1, Timestamp.valueOf(start));
                stmt.setTimestamp(2, Timestamp.valueOf(end));
                
                if (entityId != null) {
                    stmt.setLong(3, entityId);
                }
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        AIActLogEntry entry = new AIActLogEntry();
                        entry.setTimestamp(rs.getTimestamp("timestamp"));
                        entry.setEventType(categorizeEvent(rs.getString("operation")));
                        entry.setComponent(rs.getString("entity_type"));
                        entry.setDescription(rs.getString("description"));
                        entry.setUserInvolved(rs.getString("user_id"));
                        entry.setDecisionMade(rs.getString("decision"));
                        entry.setSeverity(determineSeverity(rs.getString("operation")));
                        
                        logs.add(entry);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error collecting logs", e);
        }
        
        return logs;
    }
    
    private AIActLogExport formatForAIAct(
        List<AIActLogEntry> logs,
        String entityType, Long entityId,
        LocalDateTime start, LocalDateTime end
    ) {
        AIActLogExport export = new AIActLogExport();
        
        export.setVersion("1.0");
        export.setExportDate(LocalDateTime.now());
        
        // System info
        AISystemInfo systemInfo = new AISystemInfo();
        systemInfo.setType(entityType);
        systemInfo.setId(entityId);
        systemInfo.setRiskLevel("HIGH"); // O determinar dinámicamente
        export.setAiSystem(systemInfo);
        
        // Period
        Period period = new Period();
        period.setStart(start);
        period.setEnd(end);
        export.setPeriod(period);
        
        // Entries
        export.setEntries(logs);
        
        // Summary
        LogSummary summary = new LogSummary();
        summary.setTotalEvents(logs.size());
        summary.setSystemDecisions((int) logs.stream().filter(l -> "SYSTEM_DECISION".equals(l.getEventType())).count());
        summary.setHumanOverrides((int) logs.stream().filter(l -> "HUMAN_OVERRIDE".equals(l.getEventType())).count());
        summary.setAnomalies((int) logs.stream().filter(l -> "HIGH".equals(l.getSeverity())).count());
        export.setSummary(summary);
        
        return export;
    }
    
    private File generateExportFile(AIActLogExport export, String format) {
        String filename = "ai_act_logs_" + System.currentTimeMillis();
        String outputPath = "/tmp/ai-act-exports/";
        
        File outputDir = new File(outputPath);
        outputDir.mkdirs();
        
        File exportFile;
        
        if ("JSON".equalsIgnoreCase(format)) {
            exportFile = new File(outputPath + filename + ".json");
            writeJSON(export, exportFile);
        } else if ("CSV".equalsIgnoreCase(format)) {
            exportFile = new File(outputPath + filename + ".csv");
            writeCSV(export, exportFile);
        } else {
            exportFile = new File(outputPath + filename + ".xml");
            writeXML(export, exportFile);
        }
        
        return exportFile;
    }
}

// Classes DTO para el export
class AIActLogExport {
    private String version;
    private LocalDateTime exportDate;
    private AISystemInfo aiSystem;
    private Period period;
    private List<AIActLogEntry> entries;
    private LogSummary summary;
    // getters/setters
}

class AIActLogEntry {
    private Timestamp timestamp;
    private String eventType; // SYSTEM_DECISION, HUMAN_OVERRIDE, ANOMALY, etc.
    private String component;
    private String description;
    private String userInvolved;
    private String decisionMade;
    private String severity; // INFO, WARNING, HIGH, CRITICAL
    // getters/setters
}

═══════════════════════════════════════════
REST ENDPOINT (añadir a AIActComplianceController):
═══════════════════════════════════════════

@PostMapping("/export-logs")
public ResponseEntity<Resource> exportLogs(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
    @RequestParam String entityType,
    @RequestParam(required = false) Long entityId,
    @RequestParam(defaultValue = "JSON") String format
) {
    File exportFile = logExportService.exportLogsForAudit(
        startDate, endDate, entityType, entityId, format
    );
    
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, 
            "attachment; filename=" + exportFile.getName())
        .body(new FileSystemResource(exportFile));
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. AIActLogExportService.java (servicio completo)
2. AIActLogExport, AIActLogEntry, AISystemInfo classes (DTOs)
3. Endpoint en AIActComplianceController
4. Tests unitarios
5. Documentación formato AI Act log

IMPORTANTE:
✅ Query audit tables de PostgreSQL
✅ Formato JSON, CSV, XML
✅ Categorización de eventos (SYSTEM_DECISION, HUMAN_OVERRIDE, ANOMALY)
✅ Summary statistics
✅ Compliant con Art. 12 AI Act
❌ NO acceder directamente a microservice logs (usar audit tables)

Usa Spring Boot, JDBC, Jackson para JSON, buenas prácticas Java.
```

---

## 💬 CHAT 4: CONFORMITY ASSESSMENT BPMN PROCESS

### **PROMPT ESPECÍFICO:**

```
Necesito crear proceso BPMN para Internal Conformity Assessment según EU AI Act Annex VI.

CONTEXTO:
- Framework: Flowable BPMN
- Ubicación: suinsit.nova.web/src/main/resources/processes/
- Delegates: suinsit.nova.web/src/main/java/com/codeflowx/govern/workflow/delegates/
- EU AI Act Artículo 43: Conformity assessment mandatoria

═══════════════════════════════════════════
PROCESO BPMN A CREAR:
═══════════════════════════════════════════

Archivo: internal-conformity-assessment-v1.bpmn

ID Proceso: internal-conformity-assessment-v1
Nombre: Internal Conformity Assessment (Annex VI)
Namespace: http://codeflowx.com/govern/conformity

FLUJO:
1. Start Event → startConformityAssessment
2. Service Task → verifyRiskManagement (Art. 9)
3. Service Task → verifyDataGovernance (Art. 10)
4. Service Task → verifyDocumentation (Art. 11)
5. Service Task → verifyRecordKeeping (Art. 12)
6. Service Task → verifyTransparency (Art. 13)
7. Service Task → verifyHumanOversight (Art. 14)
8. Service Task → verifyAccuracy (Art. 15)
9. Business Rule Task → calculateComplianceScore (Drools)
10. Exclusive Gateway → compliancePassGateway
    - Pass (>=90%) → generateDeclaration
    - Fail (<90%) → humanReview
11. User Task → humanReview (compliance-officers)
12. Exclusive Gateway → reviewDecisionGateway
    - Approved → generateDeclaration
    - Rejected → rejectAssessment
13. Service Task → generateDeclaration (ConformityDeclarationService)
14. Service Task → issueDeclaration
15. End Event → assessmentCompleted

VARIABLES PROCESO:
- assessmentId (Long)
- entityType (String)
- entityId (Long)
- art9Compliant (Boolean)
- art10Compliant (Boolean)
- art11Compliant (Boolean)
- art12Compliant (Boolean)
- art13Compliant (Boolean)
- art14Compliant (Boolean)
- art15Compliant (Boolean)
- overallScore (BigDecimal)
- compliancePassed (Boolean)
- declarationId (Long)

═══════════════════════════════════════════
DELEGATES A CREAR:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/workflow/delegates/conformity/

1. VerifyRiskManagementDelegate.java:

@Component
public class VerifyRiskManagementDelegate implements JavaDelegate {
    
    @Autowired
    private AIGovernanceClient governanceClient;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long entityId = (Long) execution.getVariable("entityId");
        String entityType = (String) execution.getVariable("entityType");
        
        try {
            // Llamar leka-agent-monitoring para verificar risk management
            SafetyViolationsResponse safety = governanceClient.analyzeSafetyViolations(request);
            
            boolean compliant = safety.getViolationsCount() == 0;
            execution.setVariable("art9Compliant", compliant);
            execution.setVariable("art9Score", safety.getSafetyScore());
            
        } catch (Exception e) {
            log.error("Error verifying risk management", e);
            execution.setVariable("art9Compliant", false);
        }
    }
}

2. VerifyDataGovernanceDelegate.java:

@Component
public class VerifyDataGovernanceDelegate implements JavaDelegate {
    
    @Autowired
    private AIGovernanceClient governanceClient;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long entityId = (Long) execution.getVariable("entityId");
        
        try {
            // Llamar leka-bias-detection para verificar data governance
            BiasAnalysisResponse bias = governanceClient.analyzeBias(request);
            DataQualityResponse quality = governanceClient.evaluateDataQuality(request);
            
            boolean compliant = "NO_BIAS".equals(bias.getClassification()) ||
                               "LOW".equals(bias.getClassification());
            
            execution.setVariable("art10Compliant", compliant);
            execution.setVariable("art10Score", 100 - bias.getMetrics().getDemographicParityDifference() * 100);
            
        } catch (Exception e) {
            log.error("Error verifying data governance", e);
            execution.setVariable("art10Compliant", false);
        }
    }
}

3. VerifyTransparencyDelegate.java
4. VerifyAccuracyDelegate.java
5. GenerateDeclarationDelegate.java
6. IssueDeclarationDelegate.java

(Similar pattern para cada artículo)

═══════════════════════════════════════════
USER TASK FORM:
═══════════════════════════════════════════

Pantalla ZUL: conformity-review-form.zul (crear en Chat 5)

CAMPOS:
- Assessment ID
- Compliance scores per article (read-only)
- Overall score (read-only)
- Decision: APPROVE / REJECT
- Review notes (textarea)
- Reviewer name (auto-fill)

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. internal-conformity-assessment-v1.bpmn (archivo BPMN completo)
2. 6 Delegates (VerifyRiskManagement, VerifyDataGovernance, etc.)
3. Drools rule para calculateComplianceScore
4. Tests unitarios de delegates
5. Documentación del proceso

IMPORTANTE:
✅ Delegates llaman AIGovernanceClient (microservicios vía Gateway)
✅ Cada artículo verificado independientemente
✅ Drools calcula score final
✅ Human review gate para scores <90%
✅ Genera ConformityDeclaration al finalizar
❌ NO lógica compleja en delegates (delegar a microservicios)

Usa Flowable BPMN, Spring Boot, buenas prácticas workflow.
```

---

## 💬 CHAT 5: PANTALLAS ZUL + VIEWMODELS

### **PROMPT ESPECÍFICO:**

```
Necesito crear pantallas ZUL + ViewModels para gestión de AI Act compliance.

CONTEXTO:
- Framework UI: ZKoss (MVVM pattern)
- Ubicación ViewModels: suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/
- Ubicación pantallas: suinsit.nova.web/src/main/webapp/console/compliance/ai-act/
- Framework persistence: EnArt BusinessService

═══════════════════════════════════════════
PANTALLA 1: DOCUMENTATION GENERATOR
═══════════════════════════════════════════

Archivo ZUL: ai-act-documentation-generator.zul

LAYOUT:
┌────────────────────────────────────────────────┐
│ AI Act Technical Documentation Generator      │
├────────────────────────────────────────────────┤
│ Entity Type: [MODEL ▼]  Entity ID: [____]    │
│ System Name: [________________]                │
│ Version:     [________________]                │
│                                                │
│ [Generate Documentation] [Download PDF]       │
│                                                │
│ Generated Documents:                           │
│ ┌──────────────────────────────────────────┐ │
│ │ ID | System | Generated | Status | PDF  │ │
│ │ 1  | Agent1 | 2025-11-01| DRAFT  | [📄]│ │
│ │ 2  | Model2 | 2025-11-01| SIGNED | [📄]│ │
│ └──────────────────────────────────────────┘ │
└────────────────────────────────────────────────┘

ViewModel: AIActDocumentationGeneratorViewModel.java

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AIActDocumentationGeneratorViewModel extends MasterPage {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private AIActDocumentationService documentationService;
    
    // Data
    private String selectedEntityType = "MODEL";
    private Long entityId;
    private String systemName;
    private String version;
    private List<AIActTechnicalDocumentation> generatedDocs = new ArrayList<>();
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        loadGeneratedDocuments();
    }
    
    @Command
    @NotifyChange("*")
    public void generateDocumentation() {
        try {
            AIActTechnicalDocumentation doc = documentationService.generateDocumentation(
                selectedEntityType, 
                entityId
            );
            
            Messagebox.show("Documentation generated successfully. ID: " + doc.getIdxTechnicalDoc(),
                "Success", Messagebox.OK, Messagebox.INFORMATION);
            
            loadGeneratedDocuments();
        } catch (Exception e) {
            log.error("Error generating documentation", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void downloadPDF(@BindingParam("docId") Long docId) {
        // Trigger file download
        Filedownload.save(new File(pdfPath), "application/pdf");
    }
}

═══════════════════════════════════════════
PANTALLA 2: CONFORMITY DECLARATION MANAGER
═══════════════════════════════════════════

Archivo ZUL: conformity-declaration-manager.zul

LAYOUT:
┌────────────────────────────────────────────────┐
│ EU Declaration of Conformity Manager          │
├────────────────────────────────────────────────┤
│ Assessment: [Select assessment... ▼]          │
│                                                │
│ [Generate Declaration] [Sign] [Download]      │
│                                                │
│ Existing Declarations:                         │
│ ┌──────────────────────────────────────────┐ │
│ │ ID | System | Date | Status | Actions  │ │
│ │ 1  | Agent1 | 11/01| DRAFT  | [Sign]   │ │
│ │ 2  | Model2 | 11/01| SIGNED | [📄]     │ │
│ └──────────────────────────────────────────┘ │
│                                                │
│ Declaration Preview:                           │
│ ┌──────────────────────────────────────────┐ │
│ │ Provider: CodeflowX                      │ │
│ │ AI System: Customer Support Agent v2     │ │
│ │ Compliance Score: 95%                    │ │
│ │ Art. 9-15: All compliant ✅              │ │
│ └──────────────────────────────────────────┘ │
└────────────────────────────────────────────────┘

ViewModel: ConformityDeclarationManagerViewModel.java

(Similar pattern a documentation generator)

═══════════════════════════════════════════
PANTALLA 3: CONFORMITY REVIEW FORM (BPMN)
═══════════════════════════════════════════

Archivo ZUL: conformity-review-form.zul

LAYOUT (para User Task en BPMN):
┌────────────────────────────────────────────────┐
│ Conformity Assessment Review                  │
├────────────────────────────────────────────────┤
│ System: Agent XYZ                              │
│ Assessment Date: 2025-11-01                    │
│                                                │
│ Compliance Scores:                             │
│ Art. 9 (Risk):          ✅ 100%               │
│ Art. 10 (Data):         ✅ 95%                │
│ Art. 11 (Documentation):⚠️ 85%                │
│ ...                                            │
│                                                │
│ Overall Score: 92%                             │
│                                                │
│ Decision: ⚪ APPROVE  ⚪ REJECT                │
│                                                │
│ Review Notes:                                  │
│ [____________________________]                 │
│                                                │
│ [Submit Review]                                │
└────────────────────────────────────────────────┘

ViewModel: ConformityReviewViewModel.java (BPMN task)

═══════════════════════════════════════════
PANTALLA 4: EU REGISTRATION STATUS
═══════════════════════════════════════════

Archivo ZUL: eu-registration-status.zul

Dashboard de estado de registros en EU Database

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. ai-act-documentation-generator.zul + ViewModel
2. conformity-declaration-manager.zul + ViewModel
3. conformity-review-form.zul + ViewModel (BPMN)
4. eu-registration-status.zul + ViewModel
5. Tests de ViewModels
6. Navegación/menús actualizados

IMPORTANTE:
✅ MVVM pattern (ZKoss)
✅ BusinessService para persistence
✅ @Command para acciones
✅ @NotifyChange para refresh UI
✅ Messagebox para feedback
✅ Grid/Listbox para listados
❌ NO lógica de negocio en ViewModel (delegar a servicios)

Usa ZKoss 9.x, Spring, MVVM, buenas prácticas UI.
```

---

## 💬 CHAT 6: DEEP FAKE DETECTION MICROSERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear microservicio Python FastAPI para detección de deep fakes (EU AI Act Art. 52.3).

CONTEXTO:
- Framework: FastAPI
- Puerto: 8012
- Responsabilidad: Detectar contenido sintético (imagen/video/audio)
- EU AI Act Art. 52.3: Disclosure mandatorio para contenido generado/manipulado
- Stateless (NO base de datos)

═══════════════════════════════════════════
ENDPOINTS A CREAR:
═══════════════════════════════════════════

1. POST /api/deepfake/detect-image
2. POST /api/deepfake/detect-video
3. POST /api/deepfake/detect-audio
4. POST /api/deepfake/watermark-content
5. POST /api/deepfake/generate-disclosure
6. GET /health

═══════════════════════════════════════════
ENDPOINT 1: DETECT SYNTHETIC IMAGE
═══════════════════════════════════════════

POST /api/deepfake/detect-image

INPUT:
- file: imagen (multipart/form-data)
- threshold: float (default 0.5)

OUTPUT:
{
  "is_synthetic": true,
  "confidence": 0.87,
  "evidence": {
    "artifacts_detected": ["gan_artifacts", "color_inconsistency"],
    "model_probability": 0.87,
    "analysis_method": "EfficientNet-B4"
  },
  "recommendation": "DISCLOSE",
  "disclosure_required": true,
  "compliant_with": "EU_AI_ACT_ART_52_3"
}

MODELO RECOMENDADO:
- selimsef/dfdc_efficientnet_b7 (Hugging Face)
- O efficientnet-pytorch pre-trained

═══════════════════════════════════════════
ENDPOINT 2: DETECT SYNTHETIC VIDEO
═══════════════════════════════════════════

POST /api/deepfake/detect-video

INPUT:
- file: video (multipart/form-data)
- sample_frames: int (default 10)
- threshold: float (default 0.5)

OUTPUT:
{
  "is_synthetic": true,
  "confidence": 0.92,
  "frames_analyzed": 10,
  "synthetic_frames": 8,
  "temporal_inconsistencies": true,
  "evidence": {
    "frame_analysis": [...],
    "temporal_analysis": {...}
  },
  "recommendation": "DISCLOSE"
}

TÉCNICA:
- Sample frames del video
- Análisis por frame + análisis temporal
- Detectar inconsistencias entre frames

═══════════════════════════════════════════
ENDPOINT 3: DETECT SYNTHETIC AUDIO
═══════════════════════════════════════════

POST /api/deepfake/detect-audio

INPUT:
- file: audio (multipart/form-data)
- threshold: float (default 0.5)

OUTPUT:
{
  "is_synthetic": false,
  "confidence": 0.23,
  "analysis": {
    "spectral_analysis": {...},
    "voice_artifacts": [],
    "model_probability": 0.23
  },
  "recommendation": "NO_ACTION"
}

MODELO:
- wavefake detection (Hugging Face)
- O análisis espectral con librosa

═══════════════════════════════════════════
ENDPOINT 4: WATERMARK CONTENT
═══════════════════════════════════════════

POST /api/deepfake/watermark-content

INPUT:
{
  "content_type": "image",
  "content_base64": "...",
  "watermark_text": "AI-Generated",
  "watermark_position": "bottom-right"
}

OUTPUT:
{
  "watermarked_content_base64": "...",
  "watermark_applied": true,
  "visible": false,
  "metadata_embedded": true
}

TÉCNICA:
- Invisible watermarking (LSB steganography)
- Metadata embedding (EXIF for images)

═══════════════════════════════════════════
ENDPOINT 5: GENERATE DISCLOSURE
═══════════════════════════════════════════

POST /api/deepfake/generate-disclosure

INPUT:
{
  "content_type": "image",
  "system_name": "CodeflowX Image Generator",
  "generation_method": "Stable Diffusion",
  "language": "es"
}

OUTPUT:
{
  "disclosure_message": "Esta imagen fue generada por inteligencia artificial (CodeflowX Image Generator).",
  "disclosure_html": "<div class='ai-disclosure'>...</div>",
  "watermark_text": "AI-Generated",
  "metadata": {
    "system": "CodeflowX",
    "method": "Stable Diffusion",
    "generated_at": "2025-11-01T12:00:00Z"
  },
  "compliant_with": "EU_AI_ACT_ART_52_3"
}

═══════════════════════════════════════════
ESTRUCTURA PROYECTO:
═══════════════════════════════════════════

leka-deepfake-detection/
├── main.py
├── services/
│   ├── image_detection_service.py
│   ├── video_detection_service.py
│   ├── audio_detection_service.py
│   ├── watermarking_service.py
│   └── disclosure_service.py
├── models/
│   └── schemas.py
├── utils/
│   ├── image_utils.py
│   ├── video_utils.py
│   └── audio_utils.py
├── requirements.txt
├── Dockerfile
└── README.md

═══════════════════════════════════════════
REQUIREMENTS.TXT:
═══════════════════════════════════════════

fastapi==0.104.1
uvicorn==0.24.0
pydantic==2.5.0
torch==2.1.0
torchvision==0.16.0
torchaudio==2.1.0
opencv-python==4.8.0
pillow==10.1.0
librosa==0.10.0
efficientnet-pytorch==0.7.1
transformers==4.36.0
python-multipart==0.0.6
numpy>=1.26.0

═══════════════════════════════════════════
DOCKERFILE:
═══════════════════════════════════════════

FROM python:3.11-slim

RUN apt-get update && apt-get install -y \
    ffmpeg \
    libsndfile1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Download models at build time
RUN python3 -c "from transformers import AutoModelForImageClassification; \
    print('Downloading deepfake detection model...'); \
    AutoModelForImageClassification.from_pretrained('timm/efficientnet_b4.ra_in1k'); \
    print('Model cached')"

COPY . .

EXPOSE 8012

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8012"]

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. main.py con 6 endpoints completos
2. image_detection_service.py (modelo deepfake detection)
3. video_detection_service.py (frame sampling + analysis)
4. audio_detection_service.py (spectral analysis)
5. watermarking_service.py (invisible watermark)
6. disclosure_service.py (generate disclosure messages)
7. Pydantic schemas completos
8. requirements.txt
9. Dockerfile
10. README.md con ejemplos cURL
11. Kubernetes deployment YAML

IMPORTANTE:
✅ Stateless (NO base de datos)
✅ Upload multipart/form-data para archivos
✅ Modelos pre-trained (EfficientNet, WaveFake)
✅ Watermarking invisible (LSB steganography)
✅ Disclosure messages multi-idioma
✅ EU AI Act Art. 52.3 compliant
❌ NO persistencia (solo análisis y retorno)

Usa FastAPI, PyTorch, OpenCV, librosa, buenas prácticas Python.
```

---

## 💬 CHAT 7: EU DATABASE REGISTRATION SERVICE

### **PROMPT ESPECÍFICO:**

```
Necesito crear servicio Java PREPARATORIO para registro en EU Database for High-Risk AI (Art. 51).

CONTEXTO:
- Proyecto: Spring Boot + JPA
- API EU Database: AÚN NO DISPONIBLE (Q2-Q3 2025)
- Objetivo: Preparar infrastructure para cuando API esté lista
- Package: com.codeflowx.govern.service

═══════════════════════════════════════════
ENTIDAD JPA A CREAR:
═══════════════════════════════════════════

Ubicación: nocode.service/entitys/governance/EUDatabaseRegistration.java

@Entity
@Table(name = "GOV_EU_DATABASE_REGISTRATIONS")
public class EUDatabaseRegistration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_eu_registration")
    private Long idxEuRegistration;
    
    // Entity info
    @NotNull
    @Column(name = "entity_type", length = 50)
    private String entityType;
    
    @NotNull
    @Column(name = "entity_id")
    private Long entityId;
    
    @Column(name = "system_name", length = 200)
    private String systemName;
    
    // EU Database info (cuando se registre)
    @Column(name = "eu_registration_id", length = 200)
    private String euRegistrationId;
    
    @NotNull
    @Column(name = "registration_status", length = 50)
    private String registrationStatus; // PENDING, SUBMITTED, REGISTERED, FAILED
    
    // Registration payload (stored for submission)
    @Column(name = "registration_payload", columnDefinition = "TEXT")
    private String registrationPayload; // JSON
    
    // API response (cuando esté disponible)
    @Column(name = "api_response", columnDefinition = "TEXT")
    private String apiResponse;
    
    // Timestamps
    @Column(name = "prepared_at")
    private Timestamp preparedAt;
    
    @Column(name = "submitted_at")
    private Timestamp submittedAt;
    
    @Column(name = "registered_at")
    private Timestamp registeredAt;
    
    @Column(name = "next_retry_at")
    private Timestamp nextRetryAt;
    
    @Column(name = "retry_count")
    private Integer retryCount = 0;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaration_id")
    private ConformityDeclaration declaration;
    
    // Audit
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

═══════════════════════════════════════════
SERVICIO A CREAR:
═══════════════════════════════════════════

Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/govern/service/EUDatabaseRegistrationService.java

@Service
@Slf4j
public class EUDatabaseRegistrationService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${eu.aiact.database.url:https://eu-ai-database.europa.eu/api/v1}")
    private String euDatabaseUrl;
    
    @Value("${eu.aiact.provider.id:PENDING}")
    private String providerId;
    
    /**
     * Prepara registro para EU Database (payload listo)
     */
    public EUDatabaseRegistration prepareRegistration(
        String entityType,
        Long entityId,
        ConformityDeclaration declaration
    ) {
        log.info("Preparing EU Database registration for {} {}", entityType, entityId);
        
        EUDatabaseRegistration registration = new EUDatabaseRegistration();
        
        registration.setEntityType(entityType);
        registration.setEntityId(entityId);
        registration.setSystemName(declaration.getAiSystemName());
        registration.setRegistrationStatus("PENDING");
        
        // Build payload según spec EU (cuando esté disponible)
        EURegistrationPayload payload = buildPayload(entityType, entityId, declaration);
        registration.setRegistrationPayload(toJSON(payload));
        
        registration.setPreparedAt(new Timestamp(System.currentTimeMillis()));
        registration.setDeclaration(declaration);
        registration.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        registration.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        businessService.save(registration);
        
        log.info("Registration prepared: {}", registration.getIdxEuRegistration());
        
        return registration;
    }
    
    /**
     * Intenta submit a EU Database (cuando API disponible)
     */
    public EUDatabaseRegistration submitToEUDatabase(Long registrationId) {
        EUDatabaseRegistration registration = businessService.findById(
            EUDatabaseRegistration.class, 
            registrationId
        );
        
        try {
            log.info("Attempting submission to EU Database...");
            
            // Parse payload
            EURegistrationPayload payload = fromJSON(registration.getRegistrationPayload());
            
            // Call EU API (cuando esté disponible)
            ResponseEntity<EURegistrationResponse> response = restTemplate.postForEntity(
                euDatabaseUrl + "/register",
                payload,
                EURegistrationResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                registration.setEuRegistrationId(response.getBody().getRegistrationId());
                registration.setRegistrationStatus("REGISTERED");
                registration.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
                registration.setApiResponse(toJSON(response.getBody()));
                
                log.info("Registration successful: {}", response.getBody().getRegistrationId());
            }
            
        } catch (HttpClientErrorException e) {
            log.warn("EU Database API not available: {}", e.getMessage());
            registration.setRegistrationStatus("PENDING");
            registration.setNextRetryAt(calculateNextRetry());
            registration.setRetryCount(registration.getRetryCount() + 1);
            
        } catch (Exception e) {
            log.error("Error submitting to EU Database", e);
            registration.setRegistrationStatus("FAILED");
            registration.setApiResponse(e.getMessage());
        }
        
        registration.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        businessService.update(registration);
        
        return registration;
    }
    
    private EURegistrationPayload buildPayload(
        String entityType, Long entityId, ConformityDeclaration declaration
    ) {
        EURegistrationPayload payload = new EURegistrationPayload();
        
        payload.setProviderId(providerId);
        payload.setProviderName(declaration.getProviderName());
        payload.setProviderCountry(declaration.getProviderCountry());
        payload.setSystemName(declaration.getAiSystemName());
        payload.setSystemType(entityType);
        payload.setRiskLevel("HIGH");
        payload.setIntendedPurpose(declaration.getIntendedPurpose());
        payload.setDeploymentDate(new Date());
        payload.setConformityDeclarationRef(declaration.getIdxDeclaration().toString());
        payload.setComplianceScore(declaration.getOverallComplianceScore());
        
        return payload;
    }
}

═══════════════════════════════════════════
PROCESO BPMN (crear en Chat 4 o aquí):
═══════════════════════════════════════════

Archivo: eu-database-registration-v1.bpmn

FLUJO:
1. Start → prepareRegistration
2. submitToEUDatabase
3. Gateway: Success?
   - YES → storeRegistrationID → notify → End
   - NO → scheduleRetry → End

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. EUDatabaseRegistration.java (entidad)
2. EUDatabaseRegistrationService.java (servicio)
3. EURegistrationPayload.java (DTO)
4. Endpoint en AIActComplianceController
5. eu-database-registration-v1.bpmn (proceso)
6. SQL migration
7. Tests unitarios

IMPORTANTE:
✅ Preparatorio (API EU aún no existe)
✅ Payload listo para cuando API disponible
✅ Retry mechanism
✅ Graceful degradation (guardar local si API no disponible)
❌ NO bloquear flujos (submission opcional)

Usa Spring Boot, RestTemplate, buenas prácticas Java.
```

---

## 💬 CHAT 8: AI INTERPRETER EXTENSION (Disclosure)

### **PROMPT ESPECÍFICO:**

```
Necesito añadir endpoint a leka-ai-interpreter para generar disclosure messages (EU AI Act Art. 52).

CONTEXTO:
- Microservicio existente: leka-ai-interpreter (puerto 8011)
- Framework: FastAPI + Phi-3 Mini (CPU)
- Responsabilidad: Generar mensajes disclosure para chatbots/conversational AI
- EU AI Act Art. 52.1: Chatbots deben informar que son IA

═══════════════════════════════════════════
NUEVO ENDPOINT A AÑADIR:
═══════════════════════════════════════════

Ubicación: leka-ai-interpreter/main.py

@app.post("/api/interpret/generate-disclosure")
async def generate_disclosure(request: DisclosureRequest):
    """
    Genera disclosure message para chatbots (EU AI Act Art. 52.1)
    """
    
    # Construir prompt para Phi-3 Mini
    prompt = f"""
You are an expert in EU AI Act compliance generating disclosure messages.

Generate a clear, concise disclosure message in {request.language} to inform users 
they are interacting with an AI system.

AI System Information:
- Name: {request.system_name}
- Type: {request.system_type}
- Capabilities: {', '.join(request.capabilities)}
- Limitations: {', '.join(request.limitations)}
- Human Oversight: {request.human_oversight_info}

Requirements:
- User-friendly language (non-technical)
- Clear and transparent
- Compliant with EU AI Act Article 52
- Length: 2-3 sentences maximum
- Tone: Professional but approachable

Generate the disclosure message:
    """
    
    # Generate con Phi-3 Mini
    disclosure = generate_with_phi3(prompt, max_tokens=150, temperature=0.3)
    
    # También generar versión HTML
    html_disclosure = f"""
    <div class="ai-disclosure-banner" style="background: #f0f8ff; padding: 10px; border-left: 4px solid #0066cc;">
        <i class="icon-robot"></i>
        <strong>Asistente de IA:</strong> {disclosure}
        <a href="#" onclick="showMoreInfo()">Más información</a>
    </div>
    """
    
    return {
        "disclosure_message": disclosure,
        "disclosure_html": html_disclosure,
        "language": request.language,
        "system_name": request.system_name,
        "compliant_with": "EU_AI_ACT_ART_52_1",
        "generated_at": datetime.now().isoformat()
    }

═══════════════════════════════════════════
PYDANTIC SCHEMA:
═══════════════════════════════════════════

Añadir a models/schemas.py:

class DisclosureRequest(BaseModel):
    system_name: str
    system_type: str = "chatbot"
    capabilities: List[str]
    limitations: List[str]
    human_oversight_info: str
    language: str = "es"
    
class DisclosureResponse(BaseModel):
    disclosure_message: str
    disclosure_html: str
    language: str
    system_name: str
    compliant_with: str
    generated_at: str

═══════════════════════════════════════════
EJEMPLO cURL:
═══════════════════════════════════════════

curl -X POST http://localhost:8011/api/interpret/generate-disclosure \
  -H "Content-Type: application/json" \
  -d '{
    "system_name": "CodeflowX Customer Support Bot",
    "system_type": "chatbot",
    "capabilities": [
      "Responder preguntas sobre productos",
      "Buscar en documentación",
      "Generar resúmenes"
    ],
    "limitations": [
      "No puede procesar pagos",
      "No puede acceder a datos personales",
      "No proporciona asesoría financiera"
    ],
    "human_oversight_info": "Supervisión humana disponible Lun-Vie 9-18h",
    "language": "es"
  }'

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

1. Nuevo endpoint /api/interpret/generate-disclosure en main.py
2. DisclosureRequest, DisclosureResponse schemas
3. README.md actualizado
4. Ejemplos cURL
5. Tests del nuevo endpoint

IMPORTANTE:
✅ Reutilizar Phi-3 Mini ya cargado
✅ Temperature baja (0.3) para consistency
✅ Multi-idioma (es, en, fr, de)
✅ Output HTML + plain text
✅ Compliant EU AI Act Art. 52.1
❌ NO nueva lógica compleja (prompt simple)

Usa FastAPI, Phi-3 Mini, Pydantic, buenas prácticas Python.
```

---

## 🎯 DISTRIBUCIÓN RECOMENDADA DE CHATS

### **PRIORIDAD ALTA (Mandatorio para 98% compliance):**

```
Chat 1: Documentation Service        [5h] ⭐⭐⭐
Chat 2: Declaration Service          [5h] ⭐⭐⭐
Chat 3: Log Export Service           [3h] ⭐⭐
Chat 4: Conformity Assessment BPMN   [4h] ⭐⭐
Chat 5: Pantallas ZUL                [6h] ⭐⭐

TOTAL: 5 chats = Esta noche → 98% compliance
```

### **PRIORIDAD MEDIA (Para 100% compliance):**

```
Chat 6: Deep Fake Detection          [6h] ⭐
Chat 7: EU Registration (prep)       [4h] ⭐
Chat 8: AI Interpreter Extension     [2h] ⭐

TOTAL: 3 chats = Esta noche → 100% compliance
```

---

## ⏱️ TIMELINE REALISTA

### **ESTA NOCHE (Sábado 23:00 - Domingo 08:00):**

```
23:00 - Lanzar 8 chats con prompts
23:30 - Chats generando código
01:00 - Revisar código Chat 8 (más rápido: 2h)
02:00 - Revisar código Chat 3 (log export: 3h)
03:00 - Revisar código Chat 4 (BPMN: 4h)
04:00 - Revisar código Chat 1,2 (servicios: 5h)
05:00 - Revisar código Chat 5,6,7 (6h)
06:00 - Integración y fixes
07:00 - Testing básico
08:00 - LISTO ✅
```

**Domingo:** Descanso + testing adicional  
**Lunes:** Preparar demo  
**Martes:** Lanzamiento (post LinkedIn)  
**Miércoles/Jueves:** Demo funcionando ✅

---

## 📊 RESULTADO ESPERADO

### **Domingo Mañana (08:00):**

**Backend Java:**
- ✅ 3 entidades nuevas (Documentation, Declaration, EURegistration)
- ✅ 4 servicios nuevos
- ✅ 2 procesos BPMN nuevos
- ✅ 4 pantallas ZUL nuevas
- ✅ Controller con endpoints AI Act

**Microservicios:**
- ✅ leka-deepfake-detection (puerto 8012)
- ✅ leka-ai-interpreter (endpoint disclosure)

**Compliance:**
- ✅ 98-100% AI Act compliance
- ✅ Documentation auto-generation
- ✅ Declaration generator
- ✅ Log export utility
- ✅ Deep fake detection
- ✅ EU Database registration (preparatorio)

---

## 🚀 ORDEN DE EJECUCIÓN RECOMENDADO

### **Si tienes 8 chats disponibles:**

```
ABRIR TODOS A LA VEZ (23:00):
├─ Chat 1 → Documentation Service
├─ Chat 2 → Declaration Service
├─ Chat 3 → Log Export Service
├─ Chat 4 → Conformity BPMN
├─ Chat 5 → Pantallas ZUL
├─ Chat 6 → Deep Fake Detection
├─ Chat 7 → EU Registration
└─ Chat 8 → AI Interpreter Extension

MONITOREAR cada 30-60 min
APLICAR código según se complete
TESTING integrado al final
```

### **Si tienes menos chats disponibles, PRIORIZAR:**

```
1º Chat 1 + Chat 2 (servicios críticos)
2º Chat 3 + Chat 4 (export + BPMN)
3º Chat 5 (UI)
4º Chat 6 (deep fake)
5º Chat 7 + Chat 8 (preparatorios)
```

---

## ✅ CHECKLIST EJECUCIÓN

### **ANTES DE EMPEZAR (22:00-23:00):**

- [ ] Tener 8 ventanas de chat abiertas (Claude, ChatGPT, Gemini, etc.)
- [ ] Copiar prompts de este documento
- [ ] Verificar que cliente Java unificado está funcionando
- [ ] Café ☕

### **DURANTE (23:00-06:00):**

- [ ] 23:00 - Lanzar los 8 prompts
- [ ] 00:00 - Revisar progreso, responder preguntas
- [ ] 01:00 - Revisar código Chat 8 (primero en terminar)
- [ ] 02:00 - Revisar código Chat 3
- [ ] 03:00 - Revisar código Chat 4
- [ ] 04:00 - Revisar código Chat 1,2
- [ ] 05:00 - Revisar código Chat 5,6,7
- [ ] 06:00 - Integración final

### **DESPUÉS (06:00-08:00):**

- [ ] Testing integración
- [ ] Compilar proyecto
- [ ] Corregir errores
- [ ] Desplegar en K8s (deepfake)
- [ ] Testing end-to-end
- [ ] Git commit: "feat: EU AI Act 100% compliance implementation"

### **DOMINGO:**

- [ ] Descanso merecido
- [ ] Testing adicional
- [ ] Preparar demos

### **LUNES-MARTES:**

- [ ] Preparar demo AI Act compliance
- [ ] Lanzamiento LinkedIn

### **MIÉRCOLES/JUEVES:**

- [ ] Demo funcionando completa ✅
- [ ] Reunión con consultores con demos en vivo

---

## 🎬 RESULTADO FINAL

### **Domingo 08:00 - LO QUE TENDRÁS:**

```
COMPLIANCE:
├─ Alto Riesgo:      98% ✅
├─ Riesgo Limitado:  100% ✅
└─ Riesgo Mínimo:    100% ✅ (opcional)

COMPONENTES:
├─ Backend Java:     7 nuevos componentes
├─ Microservicios:   1 nuevo (deepfake) + 1 extensión (interpreter)
├─ Procesos BPMN:    2 nuevos
├─ Pantallas ZUL:    4 nuevas
└─ Tests:            Unitarios básicos

ESTADO:
└─ Production-ready: 95%
└─ Testing pendiente: 5%
└─ Demo: Lista miércoles/jueves ✅
```

---

**Esta noche → 100% AI Act compliance implementado.**  
**Esta semana → Testing y demos.**  
**Próxima semana → Líderes del mercado en AI Governance.**

🚀 **¡A trabajar!**

