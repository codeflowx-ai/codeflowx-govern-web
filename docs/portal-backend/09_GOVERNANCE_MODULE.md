## Módulo de Governance - Portal Backend

## Descripción General

El módulo de Governance gestiona las políticas, seguridad, cumplimiento regulatorio y gobierno de datos de la plataforma. Incluye funcionalidades de gestión de políticas, auditoría de seguridad, cumplimiento normativo y monitorización de gobernanza. Se integra con sistemas de auditoría y cumplimiento para proporcionar transparencia y control total.

## Entidades del Sistema

### 1\. GovernancePolicy

```java
@Entity
@Table(name = "governance_policies")
public class GovernancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "policy_type")
    @Enumerated(EnumType.STRING)
    private PolicyType policyType;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private PolicyCategory category;

    @Column(name = "policy_content")
    private String policyContent; // JSON policy definition

    @Column(name = "version")
    private String version;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    @Column(name = "enforcement_level")
    @Enumerated(EnumType.STRING)
    private EnforcementLevel enforcementLevel;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum PolicyType {
    DATA_PRIVACY, SECURITY, COMPLIANCE, QUALITY, ACCESS_CONTROL, RETENTION
}

public enum PolicyCategory {
    GDPR, HIPAA, SOX, CCPA, ISO27001, CUSTOM
}

public enum PolicyStatus {
    DRAFT, REVIEW, APPROVED, ACTIVE, EXPIRED, ARCHIVED
}

public enum EnforcementLevel {
    ADVISORY, MANDATORY, CRITICAL
}
```

### 2\. PolicyViolation

```java
@Entity
@Table(name = "policy_violations")
public class PolicyViolation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "violation_type")
    @Enumerated(EnumType.STRING)
    private ViolationType violationType;

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private ViolationSeverity severity;

    @Column(name = "description")
    private String description;

    @Column(name = "resource_affected")
    private String resourceAffected;

    @Column(name = "user_responsible")
    private Long userResponsible;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ViolationStatus status;

    @Column(name = "resolution_notes")
    private String resolutionNotes;

    @Column(name = "resolved_by")
    private Long resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ViolationType {
    DATA_ACCESS, SECURITY_BREACH, COMPLIANCE_FAILURE, QUALITY_ISSUE
}

public enum ViolationSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}

public enum ViolationStatus {
    OPEN, INVESTIGATING, RESOLVED, CLOSED
}
```

### 3\. SecurityAudit

```java
@Entity
@Table(name = "security_audits")
public class SecurityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_type")
    @Enumerated(EnumType.STRING)
    private AuditType auditType;

    @Column(name = "audit_name")
    private String auditName;

    @Column
    private String description;

    @Column(name = "scope")
    private String scope; // JSON audit scope

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "auditor")
    private String auditor;

    @Column(name = "findings")
    private String findings; // JSON audit findings

    @Column(name = "recommendations")
    private String recommendations; // JSON recommendations

    @Column(name = "risk_score")
    private Double riskScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum AuditType {
    SECURITY_ASSESSMENT, PENETRATION_TESTING, COMPLIANCE_AUDIT, RISK_ASSESSMENT
}

public enum AuditStatus {
    PLANNED, IN_PROGRESS, COMPLETED, REVIEWING, APPROVED
}
```

### 4\. ComplianceFramework

```java
@Entity
@Table(name = "compliance_frameworks")
public class ComplianceFramework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "framework_type")
    @Enumerated(EnumType.STRING)
    private FrameworkType frameworkType;

    @Column(name = "version")
    private String version;

    @Column(name = "requirements")
    private String requirements; // JSON framework requirements

    @Column(name = "controls")
    private String controls; // JSON security controls

    @Column(name = "compliance_status")
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;

    @Column(name = "last_assessment")
    private LocalDate lastAssessment;

    @Column(name = "next_assessment")
    private LocalDate nextAssessment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum FrameworkType {
    REGULATORY, INDUSTRY_STANDARD, INTERNAL, CERTIFICATION
}

public enum ComplianceStatus {
    COMPLIANT, NON_COMPLIANT, PARTIALLY_COMPLIANT, UNDER_REVIEW
}
```

## API Endpoints

### Policy Management

```plaintext
GET    /api/v1/governance/policies
GET    /api/v1/governance/policies/{id}
POST   /api/v1/governance/policies
PUT    /api/v1/governance/policies/{id}
DELETE /api/v1/governance/policies/{id}
POST   /api/v1/governance/policies/{id}/approve
POST   /api/v1/governance/policies/{id}/activate
GET    /api/v1/governance/policies/types
GET    /api/v1/governance/policies/categories
```

### Policy Violations

```plaintext
GET    /api/v1/governance/violations
GET    /api/v1/governance/violations/{id}
POST   /api/v1/governance/violations
PUT    /api/v1/governance/violations/{id}
DELETE /api/v1/governance/violations/{id}
POST   /api/v1/governance/violations/{id}/resolve
GET    /api/v1/governance/violations/active
GET    /api/v1/governance/violations/severity/{severity}
```

### Security Audits

```plaintext
GET    /api/v1/governance/audits
GET    /api/v1/governance/audits/{id}
POST   /api/v1/governance/audits
PUT    /api/v1/governance/audits/{id}
DELETE /api/v1/governance/audits/{id}
POST   /api/v1/governance/audits/{id}/start
POST   /api/v1/governance/audits/{id}/complete
GET    /api/v1/governance/audits/types
GET    /api/v1/governance/audits/status/{status}
```

### Compliance Frameworks

```plaintext
GET    /api/v1/governance/frameworks
GET    /api/v1/governance/frameworks/{id}
POST   /api/v1/governance/frameworks
PUT    /api/v1/governance/frameworks/{id}
DELETE /api/v1/governance/frameworks/{id}
GET    /api/v1/governance/frameworks/types
GET    /api/v1/governance/frameworks/status/{status}
POST   /api/v1/governance/frameworks/{id}/assess
```

### Monitoring and Reporting

```plaintext
GET    /api/v1/governance/monitoring/overview
GET    /api/v1/governance/monitoring/policy-compliance
GET    /api/v1/governance/monitoring/security-metrics
GET    /api/v1/governance/monitoring/compliance-status
GET    /api/v1/governance/reports/compliance
GET    /api/v1/governance/reports/security
GET    /api/v1/governance/reports/violations
POST   /api/v1/governance/reports/generate
```

## Scripts de Base de Datos

```plaintext
-- Tabla de políticas de gobernanza
CREATE TABLE governance_policies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    policy_type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    policy_content TEXT NOT NULL,
    version VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'DRAFT',
    enforcement_level VARCHAR(50) NOT NULL,
    effective_date DATE,
    expiration_date DATE,
    created_by BIGINT REFERENCES users(id),
    approved_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de violaciones de políticas
CREATE TABLE policy_violations (
    id BIGSERIAL PRIMARY KEY,
    policy_id BIGINT REFERENCES governance_policies(id) ON DELETE CASCADE,
    violation_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    description TEXT,
    resource_affected VARCHAR(500),
    user_responsible BIGINT REFERENCES users(id),
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'OPEN',
    resolution_notes TEXT,
    resolved_by BIGINT REFERENCES users(id),
    resolved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de auditorías de seguridad
CREATE TABLE security_audits (
    id BIGSERIAL PRIMARY KEY,
    audit_type VARCHAR(50) NOT NULL,
    audit_name VARCHAR(255) NOT NULL,
    description TEXT,
    scope TEXT,
    status VARCHAR(50) DEFAULT 'PLANNED',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    auditor VARCHAR(255),
    findings TEXT,
    recommendations TEXT,
    risk_score DECIMAL(3,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de frameworks de cumplimiento
CREATE TABLE compliance_frameworks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    framework_type VARCHAR(50) NOT NULL,
    version VARCHAR(50) NOT NULL,
    requirements TEXT,
    controls TEXT,
    compliance_status VARCHAR(50) DEFAULT 'UNDER_REVIEW',
    last_assessment DATE,
    next_assessment DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_governance_policies_type ON governance_policies(policy_type);
CREATE INDEX idx_governance_policies_category ON governance_policies(category);
CREATE INDEX idx_governance_policies_status ON governance_policies(status);
CREATE INDEX idx_policy_violations_policy_id ON policy_violations(policy_id);
CREATE INDEX idx_policy_violations_type ON policy_violations(violation_type);
CREATE INDEX idx_policy_violations_severity ON policy_violations(severity);
CREATE INDEX idx_policy_violations_status ON policy_violations(status);
CREATE INDEX idx_security_audits_type ON security_audits(audit_type);
CREATE INDEX idx_security_audits_status ON security_audits(status);
CREATE INDEX idx_compliance_frameworks_type ON compliance_frameworks(framework_type);
CREATE INDEX idx_compliance_frameworks_status ON compliance_frameworks(compliance_status);
```

## Servicios de Governance

### GovernancePolicyService

```java
@Service
@Transactional
public class GovernancePolicyService {

    @Autowired
    private GovernancePolicyRepository policyRepository;

    @Autowired
    private PolicyViolationService violationService;

    @Autowired
    private AuditService auditService;

    public GovernancePolicy createPolicy(GovernancePolicyDto policyDto) {
        // Validar datos de la política
        validatePolicyData(policyDto);

        // Crear política
        GovernancePolicy policy = new GovernancePolicy();
        policy.setName(policyDto.getName());
        policy.setDescription(policyDto.getDescription());
        policy.setPolicyType(policyDto.getPolicyType());
        policy.setCategory(policyDto.getCategory());
        policy.setPolicyContent(policyDto.getPolicyContent());
        policy.setVersion("1.0.0");
        policy.setStatus(PolicyStatus.DRAFT);
        policy.setEnforcementLevel(policyDto.getEnforcementLevel());
        policy.setEffectiveDate(policyDto.getEffectiveDate());
        policy.setExpirationDate(policyDto.getExpirationDate());
        policy.setCreatedBy(getCurrentUserId());
        policy.setCreatedAt(LocalDateTime.now());

        GovernancePolicy savedPolicy = policyRepository.save(policy);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_GOVERNANCE_POLICY",
            "GOVERNANCE_POLICY",
            savedPolicy.getId().toString()
        );

        return savedPolicy;
    }

    public GovernancePolicy approvePolicy(Long policyId) {
        GovernancePolicy policy = policyRepository.findById(policyId)
            .orElseThrow(() -&gt; new PolicyNotFoundException("Policy not found"));

        // Validar que la política esté en estado REVIEW
        if (policy.getStatus() != PolicyStatus.REVIEW) {
            throw new InvalidPolicyStateException("Policy must be in REVIEW state to approve");
        }

        // Aprobar política
        policy.setStatus(PolicyStatus.APPROVED);
        policy.setApprovedBy(getCurrentUserId());
        policy.setUpdatedAt(LocalDateTime.now());

        GovernancePolicy approvedPolicy = policyRepository.save(policy);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "APPROVE_GOVERNANCE_POLICY",
            "GOVERNANCE_POLICY",
            policyId.toString()
        );

        return approvedPolicy;
    }

    public GovernancePolicy activatePolicy(Long policyId) {
        GovernancePolicy policy = policyRepository.findById(policyId)
            .orElseThrow(() -&gt; new PolicyNotFoundException("Policy not found"));

        // Validar que la política esté aprobada
        if (policy.getStatus() != PolicyStatus.APPROVED) {
            throw new InvalidPolicyStateException("Policy must be approved to activate");
        }

        // Activar política
        policy.setStatus(PolicyStatus.ACTIVE);
        policy.setEffectiveDate(LocalDate.now());
        policy.setUpdatedAt(LocalDateTime.now());

        GovernancePolicy activePolicy = policyRepository.save(policy);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "ACTIVATE_GOVERNANCE_POLICY",
            "GOVERNANCE_POLICY",
            policyId.toString()
        );

        return activePolicy;
    }

    private void validatePolicyData(GovernancePolicyDto policyDto) {
        if (policyDto.getName() == null || policyDto.getName().trim().isEmpty()) {
            throw new ValidationException("Policy name is required");
        }

        if (policyDto.getPolicyType() == null) {
            throw new ValidationException("Policy type is required");
        }

        if (policyDto.getCategory() == null) {
            throw new ValidationException("Policy category is required");
        }

        if (policyDto.getPolicyContent() == null || policyDto.getPolicyContent().trim().isEmpty()) {
            throw new ValidationException("Policy content is required");
        }

        if (policyDto.getEnforcementLevel() == null) {
            throw new ValidationException("Enforcement level is required");
        }
    }

    private Long getCurrentUserId() {
        // Obtener ID del usuario actual
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        // Obtener username del usuario actual
        return "current_user"; // Placeholder
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class GovernanceMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter policiesCreatedCounter;
    private final Counter policiesApprovedCounter;
    private final Counter policiesActivatedCounter;
    private final Counter violationsDetectedCounter;
    private final Counter violationsResolvedCounter;
    private final Timer policyCreationTimer;
    private final Timer auditExecutionTimer;

    public GovernanceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.policiesCreatedCounter = Counter.builder("governance.policies.created")
            .description("Total policies created")
            .register(meterRegistry);

        this.policiesApprovedCounter = Counter.builder("governance.policies.approved")
            .description("Total policies approved")
            .register(meterRegistry);

        this.policiesActivatedCounter = Counter.builder("governance.policies.activated")
            .description("Total policies activated")
            .register(meterRegistry);

        this.violationsDetectedCounter = Counter.builder("governance.violations.detected")
            .description("Total violations detected")
            .register(meterRegistry);

        this.violationsResolvedCounter = Counter.builder("governance.violations.resolved")
            .description("Total violations resolved")
            .register(meterRegistry);

        this.policyCreationTimer = Timer.builder("governance.policy.creation.time")
            .description("Policy creation time")
            .register(meterRegistry);

        this.auditExecutionTimer = Timer.builder("governance.audit.execution.time")
            .description("Audit execution time")
            .register(meterRegistry);
    }

    public void incrementPoliciesCreated() {
        policiesCreatedCounter.increment();
    }

    public void incrementPoliciesApproved() {
        policiesApprovedCounter.increment();
    }

    public void incrementPoliciesActivated() {
        policiesActivatedCounter.increment();
    }

    public void incrementViolationsDetected() {
        violationsDetectedCounter.increment();
    }

    public void incrementViolationsResolved() {
        violationsResolvedCounter.increment();
    }

    public Timer.Sample startPolicyCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startAuditExecutionTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-governance.yml

```plaintext
governance:
  # Configuración de políticas
  policies:
    auto-versioning: true
    approval-workflow: true
    enforcement-automation: true
    default-expiration-days: 365

  # Configuración de violaciones
  violations:
    auto-detection: true
    notification-channels: ["email", "slack", "webhook"]
    escalation-rules: true
    auto-resolution: false

  # Configuración de auditorías
  audits:
    scheduled-audits: true
    auto-risk-assessment: true
    compliance-tracking: true
    report-generation: true

  # Configuración de cumplimiento
  compliance:
    frameworks: ["GDPR", "HIPAA", "SOX", "CCPA", "ISO27001"]
    auto-assessment: true
    compliance-threshold: 0.9
    remediation-tracking: true

  # Configuración de monitorización
  monitoring:
    real-time-alerts: true
    policy-compliance-tracking: true
    security-metrics: true
    compliance-dashboard: true
```

## Pendiente

### **Gobierno de IA y Cumplimiento Regulatorio**

El gobierno de IA es crucial para la plataforma Leka. El sistema debe analizar periódicamente los históricos de conversaciones, evaluar el cumplimiento de reglas, integrarse con la **IA Act Europea**, autorizar el uso de modelos para serving, gestionar prompts y templates, y monitorizar agentes de forma automática.

#### **1. Integración con IA Act Europea**

```java
@Entity
@Table(name = "gov_ia_act_compliance")
public class IAActCompliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private IAActRiskLevel riskLevel;

    @Column(name = "compliance_status")
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;

    @Column(name = "risk_assessment_date")
    private LocalDateTime riskAssessmentDate;

    @Column(name = "next_assessment_date")
    private LocalDateTime nextAssessmentDate;

    @Column(name = "compliance_score")
    private Double complianceScore;

    @Column(name = "risk_factors")
    private String riskFactors; // JSON array

    @Column(name = "mitigation_measures")
    private String mitigationMeasures; // JSON array

    @Column(name = "regulatory_requirements")
    private String regulatoryRequirements; // JSON array

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum IAActRiskLevel {
    MINIMAL_RISK, LIMITED_RISK, HIGH_RISK, UNACCEPTABLE_RISK
}

public enum ComplianceStatus {
    COMPLIANT, NON_COMPLIANT, UNDER_REVIEW, PENDING_ASSESSMENT
}
```

#### **2. Autorización de Modelos para Serving**

```java
@Entity
@Table(name = "gov_model_authorizations")
public class ModelAuthorization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "authorization_status")
    @Enumerated(EnumType.STRING)
    private AuthorizationStatus authorizationStatus;

    @Column(name = "authorized_by")
    private Long authorizedBy;

    @Column(name = "authorization_date")
    private LocalDateTime authorizationDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "authorization_reason")
    private String authorizationReason;

    @Column(name = "risk_assessment_id")
    private Long riskAssessmentId;

    @Column(name = "compliance_checks_passed")
    private Boolean complianceChecksPassed;

    @Column(name = "blocked")
    private Boolean blocked = false;

    @Column(name = "blocked_by")
    private Long blockedBy;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @Column(name = "blocked_date")
    private LocalDateTime blockedDate;

    @Column(name = "blocking_evidence")
    private String blockingEvidence; // Documentos, textos, análisis

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum AuthorizationStatus {
    PENDING, AUTHORIZED, REJECTED, EXPIRED, SUSPENDED
}
```

#### **3. Gestión de Prompts y Templates**

```java
@Entity
@Table(name = "gov_prompt_governance")
public class PromptGovernance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prompt_id")
    private Long promptId;

    @Column(name = "prompt_name")
    private String promptName;

    @Column(name = "prompt_type")
    @Enumerated(EnumType.STRING)
    private PromptType promptType;

    @Column(name = "governance_status")
    @Enumerated(EnumType.STRING)
    private GovernanceStatus governanceStatus;

    @Column(name = "risk_score")
    private Double riskScore;

    @Column(name = "compliance_score")
    private Double complianceScore;

    @Column(name = "content_analysis")
    private String contentAnalysis; // JSON con análisis de contenido

    @Column(name = "bias_detection")
    private String biasDetection; // JSON con detección de sesgos

    @Column(name = "toxicity_score")
    private Double toxicityScore;

    @Column(name = "ethical_concerns")
    private String ethicalConcerns; // JSON array

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "blocked")
    private Boolean blocked = false;

    @Column(name = "blocked_by")
    private Long blockedBy;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @Column(name = "blocking_evidence")
    private String blockingEvidence;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum PromptType {
    SYSTEM_PROMPT, USER_PROMPT, TEMPLATE, CONVERSATION_STARTER
}

public enum GovernanceStatus {
    PENDING_REVIEW, APPROVED, REJECTED, UNDER_INVESTIGATION
}
```

#### **4. Monitorización de Agentes**

```java
@Entity
@Table(name = "gov_agent_monitoring")
public class AgentMonitoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_type")
    @Enumerated(EnumType.STRING)
    private AgentType agentType;

    @Column(name = "monitoring_status")
    @Enumerated(EnumType.STRING)
    private MonitoringStatus monitoringStatus;

    @Column(name = "risk_assessment_score")
    private Double riskAssessmentScore;

    @Column(name = "behavior_analysis")
    private String behaviorAnalysis; // JSON con análisis de comportamiento

    @Column(name = "decision_logging")
    private String decisionLogging; // JSON con logging de decisiones

    @Column(name = "ethical_compliance_score")
    private Double ethicalComplianceScore;

    @Column(name = "last_monitoring_date")
    private LocalDateTime lastMonitoringDate;

    @Column(name = "next_monitoring_date")
    private LocalDateTime nextMonitoringDate;

    @Column(name = "blocked")
    private Boolean blocked = false;

    @Column(name = "blocked_by")
    private Long blockedBy;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @Column(name = "blocking_evidence")
    private String blockingEvidence;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum AgentType {
    CONVERSATIONAL, DECISION_MAKING, AUTOMATION, ANALYSIS, CREATIVE
}

public enum MonitoringStatus {
    ACTIVE, SUSPENDED, UNDER_INVESTIGATION, BLOCKED
}
```

#### **5. Sistema de Bloqueo y Restricciones**

```java
@Entity
@Table(name = "gov_blocking_system")
public class BlockingSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type")
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "blocking_status")
    @Enumerated(EnumType.STRING)
    private BlockingStatus blockingStatus;

    @Column(name = "blocked_by")
    private Long blockedBy;

    @Column(name = "blocking_reason")
    private String blockingReason;

    @Column(name = "blocking_date")
    private LocalDateTime blockingDate;

    @Column(name = "blocking_evidence")
    private String blockingEvidence; // Documentos, análisis, métricas

    @Column(name = "risk_assessment_id")
    private Long riskAssessmentId;

    @Column(name = "compliance_violations")
    private String complianceViolations; // JSON array

    @Column(name = "ethical_concerns")
    private String ethicalConcerns; // JSON array

    @Column(name = "unblocking_criteria")
    private String unblockingCriteria; // Criterios para desbloquear

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum EntityType {
    MODEL, PROMPT, TEMPLATE, AGENT, ASSISTANT, CONVERSATION
}

public enum BlockingStatus {
    ACTIVE, SUSPENDED, UNDER_REVIEW, UNBLOCKED
}
```

#### **6. Análisis Automático de Conversaciones**

```java
@Entity
@Table(name = "gov_conversation_analysis")
public class ConversationAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "analysis_date")
    private LocalDateTime analysisDate;

    @Column(name = "risk_score")
    private Double riskScore;

    @Column(name = "compliance_score")
    private Double complianceScore;

    @Column(name = "ethical_concerns")
    private String ethicalConcerns; // JSON array

    @Column(name = "policy_violations")
    private String policyViolations; // JSON array

    @Column(name = "content_analysis")
    private String contentAnalysis; // JSON con análisis detallado

    @Column(name = "bias_detection")
    private String biasDetection; // JSON con detección de sesgos

    @Column(name = "toxicity_analysis")
    private String toxicityAnalysis; // JSON con análisis de toxicidad

    @Column(name = "recommendations")
    private String recommendations; // JSON con recomendaciones

    @Column(name = "requires_human_review")
    private Boolean requiresHumanReview;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

#### **7. Servicios de Gobierno de IA**

```java
@Service
public class IAGovernanceService {

    @Autowired
    private ConversationAnalysisService analysisService;

    @Autowired
    private ModelAuthorizationService authService;

    @Autowired
    private PromptGovernanceService promptService;

    @Autowired
    private AgentMonitoringService agentService;

    @Autowired
    private BlockingSystemService blockingService;

    @Scheduled(fixedRate = 3600000) // Cada hora
    public void performPeriodicAnalysis() {
        // Analizar conversaciones recientes
        List<Conversation> recentConversations = getRecentConversations();

        for (Conversation conversation : recentConversations) {
            ConversationAnalysis analysis = analysisService.analyzeConversation(conversation);

            // Verificar violaciones de políticas
            if (analysis.getRiskScore() > 0.7) {
                createPolicyViolation(conversation, analysis);
            }

            // Verificar si requiere revisión humana
            if (analysis.getRequiresHumanReview()) {
                notifyHumanReviewers(conversation, analysis);
            }
        }
    }

    public void authorizeModel(Long modelId, Long authorizedBy, String reason) {
        ModelAuthorization auth = new ModelAuthorization();
        auth.setModelId(modelId);
        auth.setAuthorizedBy(authorizedBy);
        auth.setAuthorizationReason(reason);
        auth.setAuthorizationStatus(AuthorizationStatus.AUTHORIZED);
        auth.setAuthorizationDate(LocalDateTime.now());
        auth.setComplianceChecksPassed(true);

        authService.save(auth);
    }

    public void blockEntity(EntityType entityType, Long entityId, Long blockedBy,
                           String reason, String evidence) {
        BlockingSystem blocking = new BlockingSystem();
        blocking.setEntityType(entityType);
        blocking.setEntityId(entityId);
        blocking.setBlockedBy(blockedBy);
        blocking.setBlockingReason(reason);
        blocking.setBlockingEvidence(evidence);
        blocking.setBlockingStatus(BlockingStatus.ACTIVE);
        blocking.setBlockingDate(LocalDateTime.now());

        blockingService.save(blocking);

        // Aplicar bloqueo en la entidad correspondiente
        applyBlockingToEntity(entityType, entityId);
    }

    private void applyBlockingToEntity(EntityType entityType, Long entityId) {
        switch (entityType) {
            case MODEL:
                markModelAsBlocked(entityId);
                break;
            case PROMPT:
                markPromptAsBlocked(entityId);
                break;
            case AGENT:
                markAgentAsBlocked(entityId);
                break;
            case ASSISTANT:
                markAssistantAsBlocked(entityId);
                break;
        }
    }

    public IAActCompliance assessIAActCompliance(Long modelId) {
        // Evaluar cumplimiento con IA Act Europea
        Model model = modelService.findById(modelId);

        IAActCompliance compliance = new IAActCompliance();
        compliance.setModelId(modelId);
        compliance.setModelName(model.getName());

        // Calcular nivel de riesgo
        Double riskScore = calculateIAActRiskScore(model);
        compliance.setRiskLevel(determineRiskLevel(riskScore));

        // Evaluar cumplimiento
        Double complianceScore = evaluateComplianceScore(model);
        compliance.setComplianceStatus(determineComplianceStatus(complianceScore));

        // Establecer fechas de evaluación
        compliance.setRiskAssessmentDate(LocalDateTime.now());
        compliance.setNextAssessmentDate(LocalDateTime.now().plusMonths(6));

        return compliance;
    }
}
```

#### **8. Configuración de IA Act Europea**

```yaml
# application-ia-act.yml
ia-act-compliance:
  risk-assessment:
    enabled: true
    assessment-interval: 6 months
    risk-thresholds:
      minimal: 0.0
      limited: 0.3
      high: 0.7
      unacceptable: 0.9

  compliance-frameworks:
    - name: "IA Act Europea"
      version: "2024"
      requirements:
        - "Transparencia"
        - "Responsabilidad"
        - "No discriminación"
        - "Privacidad"
        - "Seguridad"

  automated-monitoring:
    conversation-analysis: true
    bias-detection: true
    toxicity-analysis: true
    ethical-compliance: true

  human-review:
    required-risk-threshold: 0.7
    notification-channels: ["email", "dashboard", "slack"]
    escalation-rules: true

  blocking-system:
    auto-blocking: false
    human-approval-required: true
    evidence-required: true
    review-process: true
```

#### **9. Métricas de Cumplimiento**

```java
@Component
public class IAGovernanceMetrics {

    private final Counter modelsAuthorizedCounter;
    private final Counter modelsBlockedCounter;
    private final Counter promptsAnalyzedCounter;
    private final Counter conversationsAnalyzedCounter;
    private final Counter policyViolationsCounter;
    private final Timer complianceAssessmentTimer;

    public IAGovernanceMetrics(MeterRegistry meterRegistry) {
        this.modelsAuthorizedCounter = Counter.builder("governance.ia.models.authorized")
            .description("Total models authorized for serving")
            .register(meterRegistry);

        this.modelsBlockedCounter = Counter.builder("governance.ia.models.blocked")
            .description("Total models blocked from serving")
            .register(meterRegistry);

        this.promptsAnalyzedCounter = Counter.builder("governance.ia.prompts.analyzed")
            .description("Total prompts analyzed for governance")
            .register(meterRegistry);

        this.conversationsAnalyzedCounter = Counter.builder("governance.ia.conversations.analyzed")
            .description("Total conversations analyzed")
            .register(meterRegistry);

        this.policyViolationsCounter = Counter.builder("governance.ia.policy.violations")
            .description("Total policy violations detected")
            .register(meterRegistry);

        this.complianceAssessmentTimer = Timer.builder("governance.ia.compliance.assessment.time")
            .description("Time to assess IA Act compliance")
            .register(meterRegistry);
    }

    public void incrementModelsAuthorized() {
        modelsAuthorizedCounter.increment();
    }

    public void incrementModelsBlocked() {
        modelsBlockedCounter.increment();
    }

    public void incrementPromptsAnalyzed() {
        promptsAnalyzedCounter.increment();
    }

    public void incrementConversationsAnalyzed() {
        conversationsAnalyzedCounter.increment();
    }

    public void incrementPolicyViolations() {
        policyViolationsCounter.increment();
    }

    public Timer.Sample startComplianceAssessmentTimer() {
        return Timer.start(meterRegistry);
    }
}
```

#### **10. Flujo de Gobierno de IA**

1. **Análisis Periódico**: Sistema analiza conversaciones cada hora
2. **Evaluación de Riesgo**: Calcula scores de riesgo y cumplimiento
3. **Detección Automática**: Identifica violaciones y preocupaciones éticas
4. **Notificación**: Alerta a revisores humanos cuando es necesario
5. **Autorización**: Solo humanos pueden autorizar modelos para serving
6. **Bloqueo**: Solo humanos pueden bloquear entidades con evidencia
7. **Monitorización**: Seguimiento continuo de agentes y comportamientos
8. **Cumplimiento**: Evaluación automática con IA Act Europea
9. **Auditoría**: Registro completo de todas las decisiones
10. **Revisión**: Proceso de revisión periódica y desbloqueo

#### **11. Beneficios del Sistema**

- **Cumplimiento Regulatorio**: Integración completa con IA Act Europea
- **Transparencia**: Todas las decisiones documentadas y justificadas
- **Control Humano**: Solo humanos pueden bloquear/desbloquear
- **Análisis Automático**: Detección automática de riesgos y violaciones
- **Monitorización Continua**: Seguimiento en tiempo real de cumplimiento
- **Evidencia Documentada**: Requisito de evidencia para bloqueos
- **Revisión Periódica**: Proceso de revisión y actualización continua
- **Métricas Integradas**: Sistema de métricas con Prometheus
- **Escalabilidad**: Proceso automatizado para grandes volúmenes
- **Auditoría**: Registro completo para cumplimiento regulatorio

## Conclusión

El módulo de Governance proporciona un sistema completo de gobierno y cumplimiento, incluyendo:

- **Gestión de Políticas**: Creación, aprobación y activación de políticas de gobernanza
- **Detección de Violaciones**: Monitoreo automático y detección de violaciones de políticas
- **Auditorías de Seguridad**: Evaluaciones de seguridad y cumplimiento
- **Frameworks de Cumplimiento**: Gestión de estándares regulatorios y de la industria
- **Monitorización**: Seguimiento en tiempo real de cumplimiento y métricas de seguridad

El sistema está diseñado para ser auditable, cumplir con estándares regulatorios estrictos y proporcionar transparencia total en la gobernanza de la plataforma.
