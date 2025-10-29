# ⚖️ ETHICS - DOCUMENTACIÓN TÉCNICA

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo Ethics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Ethics** proporciona capacidades completas de gestión ética para el gobierno de IA, incluyendo evaluaciones, comité de ética, análisis de impacto, mitigación y gestión de violaciones.

---

## 🏗️ ARQUITECTURA TÉCNICA

### **Stack Tecnológico:**
- **Frontend:** ZKoss Framework (ZUL)
- **Backend:** Spring Boot + JPA
- **Base de Datos:** PostgreSQL
- **Procesamiento:** Java ViewModels + Delegates
- **Workflow:** Flowable BPMN
- **Reglas:** Drools Engine

---

## 📊 ENTIDADES JPA

### **Entidades Principales:**

#### **1. EthicsReview**
```java
@Entity
@Table(name = "eth_review")
public class EthicsReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "review_type")
    private String reviewType; // assessment, committee, impact, mitigation, violation
    
    @Column(name = "resource_id")
    private Long resourceId; // agent, model, prompt, rag
    
    @Column(name = "resource_type")
    private String resourceType;
    
    @Column(name = "review_status")
    private String reviewStatus; // pending, in_review, approved, rejected, mitigated
    
    @Column(name = "ethics_score")
    private BigDecimal ethicsScore; // 0-100
    
    @Column(name = "risk_level")
    private String riskLevel; // low, medium, high, critical
    
    @Column(name = "review_data", columnDefinition = "JSONB")
    private String reviewData;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "reviewed_by")
    private String reviewedBy;
    
    // Relaciones
    @OneToMany(mappedBy = "ethicsReview")
    private List<EthicsMitigationPlan> mitigationPlans;
    
    @OneToMany(mappedBy = "ethicsReview")
    private List<EthicsViolation> violations;
}
```

#### **2. EthicsMitigationPlan**
```java
@Entity
@Table(name = "eth_mitigation_plan")
public class EthicsMitigationPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plan_name")
    private String planName;
    
    @Column(name = "mitigation_type")
    private String mitigationType; // bias, fairness, transparency, accountability
    
    @Column(name = "mitigation_actions", columnDefinition = "JSONB")
    private String mitigationActions;
    
    @Column(name = "implementation_status")
    private String implementationStatus; // planned, in_progress, completed, failed
    
    @Column(name = "effectiveness_score")
    private BigDecimal effectivenessScore; // 0-100
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "ethics_review_id")
    private EthicsReview ethicsReview;
}
```

#### **3. EthicsViolation**
```java
@Entity
@Table(name = "eth_violation")
public class EthicsViolation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "violation_type")
    private String violationType; // bias, fairness, transparency, accountability
    
    @Column(name = "violation_severity")
    private String violationSeverity; // minor, moderate, major, critical
    
    @Column(name = "violation_description")
    private String violationDescription;
    
    @Column(name = "violation_evidence", columnDefinition = "JSONB")
    private String violationEvidence;
    
    @Column(name = "violation_status")
    private String violationStatus; // detected, acknowledged, mitigated, resolved
    
    @Column(name = "detected_at")
    private LocalDateTime detectedAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "ethics_review_id")
    private EthicsReview ethicsReview;
}
```

---

## 🔍 VIEWS Y FUNCIONES SQL

### **Views Principales:**

#### **1. Ethics Overview View**
```sql
CREATE VIEW v_ethics_overview AS
SELECT 
    er.resource_type,
    er.resource_id,
    COUNT(er.id) as total_reviews,
    AVG(er.ethics_score) as avg_ethics_score,
    COUNT(CASE WHEN er.review_status = 'approved' THEN 1 END) as approved_reviews,
    COUNT(CASE WHEN er.review_status = 'rejected' THEN 1 END) as rejected_reviews,
    COUNT(CASE WHEN er.risk_level = 'critical' THEN 1 END) as critical_risks,
    MAX(er.updated_at) as last_review_date
FROM eth_review er
WHERE er.review_status IN ('approved', 'rejected', 'in_review')
GROUP BY er.resource_type, er.resource_id;
```

#### **2. Ethics Violations View**
```sql
CREATE VIEW v_ethics_violations AS
SELECT 
    ev.violation_type,
    ev.violation_severity,
    COUNT(ev.id) as violation_count,
    AVG(CASE 
        WHEN ev.violation_status = 'resolved' 
        THEN EXTRACT(EPOCH FROM (ev.resolved_at - ev.detected_at))/3600 
        END) as avg_resolution_hours,
    COUNT(CASE WHEN ev.violation_status = 'detected' THEN 1 END) as pending_violations
FROM eth_violation ev
WHERE ev.detected_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY ev.violation_type, ev.violation_severity;
```

### **Funciones SQL:**

#### **1. Calculate Ethics Score**
```sql
CREATE OR REPLACE FUNCTION calculate_ethics_score(
    p_resource_id BIGINT,
    p_review_type VARCHAR(50)
) RETURNS DECIMAL(5,2) AS $$
DECLARE
    v_ethics_score DECIMAL(5,2);
    v_bias_score DECIMAL(5,2);
    v_fairness_score DECIMAL(5,2);
    v_transparency_score DECIMAL(5,2);
    v_accountability_score DECIMAL(5,2);
BEGIN
    -- Calcular scores individuales
    SELECT COALESCE(AVG(CASE WHEN metric_type = 'bias' THEN metric_value END), 0)
    INTO v_bias_score
    FROM anl_metric
    WHERE resource_id = p_resource_id;
    
    SELECT COALESCE(AVG(CASE WHEN metric_type = 'fairness' THEN metric_value END), 0)
    INTO v_fairness_score
    FROM anl_metric
    WHERE resource_id = p_resource_id;
    
    SELECT COALESCE(AVG(CASE WHEN metric_type = 'transparency' THEN metric_value END), 0)
    INTO v_transparency_score
    FROM anl_metric
    WHERE resource_id = p_resource_id;
    
    SELECT COALESCE(AVG(CASE WHEN metric_type = 'accountability' THEN metric_value END), 0)
    INTO v_accountability_score
    FROM anl_metric
    WHERE resource_id = p_resource_id;
    
    -- Calcular score promedio ponderado
    v_ethics_score := (v_bias_score * 0.3 + v_fairness_score * 0.3 + 
                       v_transparency_score * 0.2 + v_accountability_score * 0.2);
    
    RETURN LEAST(v_ethics_score, 100);
END;
$$ LANGUAGE plpgsql;
```

#### **2. Generate Ethics Report**
```sql
CREATE OR REPLACE FUNCTION generate_ethics_report(
    p_resource_id BIGINT,
    p_review_type VARCHAR(50)
) RETURNS JSONB AS $$
DECLARE
    v_report_data JSONB;
    v_ethics_score DECIMAL(5,2);
BEGIN
    -- Calcular score ético
    v_ethics_score := calculate_ethics_score(p_resource_id, p_review_type);
    
    -- Generar reporte
    SELECT jsonb_build_object(
        'resource_id', p_resource_id,
        'review_type', p_review_type,
        'ethics_score', v_ethics_score,
        'risk_level', 
        CASE 
            WHEN v_ethics_score >= 80 THEN 'low'
            WHEN v_ethics_score >= 60 THEN 'medium'
            WHEN v_ethics_score >= 40 THEN 'high'
            ELSE 'critical'
        END,
        'generated_at', NOW(),
        'violations', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'violation_type', violation_type,
                    'violation_severity', violation_severity,
                    'violation_status', violation_status
                )
            )
            FROM eth_violation
            WHERE ethics_review_id IN (
                SELECT id FROM eth_review 
                WHERE resource_id = p_resource_id
            )
        ),
        'mitigation_plans', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'mitigation_type', mitigation_type,
                    'implementation_status', implementation_status,
                    'effectiveness_score', effectiveness_score
                )
            )
            FROM eth_mitigation_plan
            WHERE ethics_review_id IN (
                SELECT id FROM eth_review 
                WHERE resource_id = p_resource_id
            )
        )
    )
    INTO v_report_data;
    
    RETURN v_report_data;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔄 PROCEDIMIENTOS ALMACENADOS

### **1. Process Ethics Reviews**
```sql
CREATE OR REPLACE PROCEDURE process_ethics_reviews()
LANGUAGE plpgsql AS $$
BEGIN
    -- Procesar evaluaciones éticas pendientes
    UPDATE eth_review 
    SET ethics_score = calculate_ethics_score(resource_id, review_type),
        risk_level = CASE 
            WHEN calculate_ethics_score(resource_id, review_type) >= 80 THEN 'low'
            WHEN calculate_ethics_score(resource_id, review_type) >= 60 THEN 'medium'
            WHEN calculate_ethics_score(resource_id, review_type) >= 40 THEN 'high'
            ELSE 'critical'
        END,
        updated_at = NOW()
    WHERE review_status = 'pending';
    
    -- Generar alertas para riesgos críticos
    INSERT INTO notification (type, title, message, created_at)
    SELECT 
        'ethics_alert',
        'Ethics Risk Alert',
        'Resource ' || resource_id || ' has critical ethics risk',
        NOW()
    FROM eth_review
    WHERE risk_level = 'critical'
    AND review_status = 'pending';
    
    -- Actualizar timestamps
    UPDATE eth_review 
    SET updated_at = NOW()
    WHERE review_status IN ('in_review', 'approved', 'rejected');
END;
$$;
```

### **2. Generate Ethics Mitigation Plans**
```sql
CREATE OR REPLACE PROCEDURE generate_ethics_mitigation_plans()
LANGUAGE plpgsql AS $$
BEGIN
    -- Generar planes de mitigación para riesgos altos y críticos
    INSERT INTO eth_mitigation_plan (ethics_review_id, plan_name, mitigation_type, mitigation_actions, implementation_status, created_at)
    SELECT 
        er.id,
        'Mitigation Plan for ' || er.resource_type || ' ' || er.resource_id,
        CASE 
            WHEN er.risk_level = 'critical' THEN 'comprehensive'
            WHEN er.risk_level = 'high' THEN 'targeted'
            ELSE 'monitoring'
        END,
        jsonb_build_object(
            'actions', jsonb_build_array(
                'Implement bias detection',
                'Add fairness monitoring',
                'Enhance transparency',
                'Strengthen accountability'
            ),
            'timeline', '30 days',
            'resources_required', 'ethics_team,technical_team'
        ),
        'planned',
        NOW()
    FROM eth_review er
    WHERE er.risk_level IN ('high', 'critical')
    AND er.review_status = 'approved'
    AND NOT EXISTS (
        SELECT 1 FROM eth_mitigation_plan emp 
        WHERE emp.ethics_review_id = er.id
    );
    
    -- Actualizar timestamps
    UPDATE eth_mitigation_plan 
    SET updated_at = NOW()
    WHERE implementation_status IN ('in_progress', 'completed');
END;
$$;
```

---

## 🔄 PROCESOS BPMN

### **1. Ethics Review Process**
```xml
<!-- ethics-review-v1.bpmn -->
<process id="ethics-review-v1" name="Ethics Review Process">
    
    <!-- Start Event -->
    <startEvent id="startEthicsReview" name="Start Ethics Review">
        <extensionElements>
            <flowable:formProperty id="resourceId" name="Resource ID" type="long" required="true"/>
            <flowable:formProperty id="reviewType" name="Review Type" type="string" required="true"/>
        </extensionElements>
    </startEvent>
    
    <!-- Ethics Assessment Task -->
    <userTask id="ethicsAssessment" name="Ethics Assessment" 
              formKey="ethics-review-request-form">
        <extensionElements>
            <flowable:taskListener event="create" class="com.codeflowx.govern.workflow.delegates.SaveEthicsEvidenceDelegate"/>
        </extensionElements>
    </userTask>
    
    <!-- Committee Review Task -->
    <userTask id="committeeReview" name="Committee Review" 
              formKey="ethics-committee-review-form">
        <extensionElements>
            <flowable:taskListener event="complete" class="com.codeflowx.govern.workflow.delegates.RejectEthicsDelegate"/>
        </extensionElements>
    </userTask>
    
    <!-- Mitigation Plan Task -->
    <userTask id="mitigationPlan" name="Create Mitigation Plan" 
              formKey="ethics-mitigation-plan-form">
    </userTask>
    
    <!-- Violation Management Task -->
    <userTask id="violationManagement" name="Violation Management" 
              formKey="ethics-review-reminder-form">
    </userTask>
    
    <!-- End Event -->
    <endEvent id="endEthicsReview" name="End Ethics Review"/>
    
    <!-- Sequence Flows -->
    <sequenceFlow id="flow1" sourceRef="startEthicsReview" targetRef="ethicsAssessment"/>
    <sequenceFlow id="flow2" sourceRef="ethicsAssessment" targetRef="committeeReview"/>
    <sequenceFlow id="flow3" sourceRef="committeeReview" targetRef="mitigationPlan"/>
    <sequenceFlow id="flow4" sourceRef="mitigationPlan" targetRef="violationManagement"/>
    <sequenceFlow id="flow5" sourceRef="violationManagement" targetRef="endEthicsReview"/>
    
</process>
```

---

## 🧠 REGLAS DROOLS

### **1. Ethics Review Scoring**
```drl
// ethics-review-scoring.drl
package com.codeflowx.govern.workflow.drools;

import com.codeflowx.govern.workflow.drools.facts.EthicsReviewFact;

rule "Calculate Ethics Score"
    when
        $fact: EthicsReviewFact(
            biasScore >= 0,
            fairnessScore >= 0,
            transparencyScore >= 0,
            accountabilityScore >= 0
        )
    then
        double ethicsScore = ($fact.getBiasScore() * 0.3 + 
                             $fact.getFairnessScore() * 0.3 + 
                             $fact.getTransparencyScore() * 0.2 + 
                             $fact.getAccountabilityScore() * 0.2);
        
        $fact.setEthicsScore(ethicsScore);
        
        if (ethicsScore >= 80) {
            $fact.setRiskLevel("low");
        } else if (ethicsScore >= 60) {
            $fact.setRiskLevel("medium");
        } else if (ethicsScore >= 40) {
            $fact.setRiskLevel("high");
        } else {
            $fact.setRiskLevel("critical");
        }
        
        update($fact);
end

rule "Generate Mitigation Plan for High Risk"
    when
        $fact: EthicsReviewFact(
            riskLevel == "high" || riskLevel == "critical"
        )
    then
        $fact.setRequiresMitigationPlan(true);
        $fact.setMitigationPriority("high");
        update($fact);
end

rule "Generate Violation Alert for Critical Risk"
    when
        $fact: EthicsReviewFact(
            riskLevel == "critical"
        )
    then
        $fact.setRequiresViolationAlert(true);
        $fact.setAlertPriority("critical");
        update($fact);
end
```

---

## 🎯 VIEWMODELS ESPECIALIZADOS

### **1. Ethics Assessments ViewModel**
```java
@Component
public class EthicsAssessmentsViewModel {
    
    @Autowired
    private EthicsService ethicsService;
    
    public List<EthicsReviewDTO> getPendingAssessments() {
        return ethicsService.getPendingAssessments();
    }
    
    public EthicsReviewDTO getAssessmentDetail(Long reviewId) {
        return ethicsService.getAssessmentDetail(reviewId);
    }
    
    public void submitAssessment(EthicsAssessmentDTO assessment) {
        ethicsService.submitAssessment(assessment);
    }
    
    public Map<String, Object> getAssessmentDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalAssessments", ethicsService.getTotalAssessments());
        dashboard.put("pendingAssessments", ethicsService.getPendingAssessments().size());
        dashboard.put("approvedAssessments", ethicsService.getApprovedAssessments().size());
        dashboard.put("rejectedAssessments", ethicsService.getRejectedAssessments().size());
        return dashboard;
    }
}
```

### **2. Ethics Committee ViewModel**
```java
@Component
public class EthicsCommitteeViewModel {
    
    @Autowired
    private EthicsService ethicsService;
    
    public List<EthicsReviewDTO> getCommitteeReviews() {
        return ethicsService.getCommitteeReviews();
    }
    
    public void approveReview(Long reviewId, String comments) {
        ethicsService.approveReview(reviewId, comments);
    }
    
    public void rejectReview(Long reviewId, String reason) {
        ethicsService.rejectReview(reviewId, reason);
    }
    
    public List<EthicsViolationDTO> getViolations() {
        return ethicsService.getViolations();
    }
}
```

---

## 📊 SERVICIOS DE ETHICS

### **1. Ethics Service**
```java
@Service
@Transactional
public class EthicsService {
    
    @Autowired
    private EthicsReviewRepository reviewRepository;
    
    @Autowired
    private EthicsMitigationPlanRepository mitigationRepository;
    
    @Autowired
    private EthicsViolationRepository violationRepository;
    
    public List<EthicsReviewDTO> getPendingAssessments() {
        return reviewRepository.findPendingAssessments();
    }
    
    public EthicsReviewDTO getAssessmentDetail(Long reviewId) {
        return reviewRepository.findAssessmentDetail(reviewId);
    }
    
    public void submitAssessment(EthicsAssessmentDTO assessment) {
        EthicsReview review = new EthicsReview();
        review.setResourceId(assessment.getResourceId());
        review.setResourceType(assessment.getResourceType());
        review.setReviewType(assessment.getReviewType());
        review.setReviewData(assessment.getReviewData());
        review.setReviewStatus("pending");
        review.setCreatedAt(LocalDateTime.now());
        
        reviewRepository.save(review);
    }
    
    public void approveReview(Long reviewId, String comments) {
        EthicsReview review = reviewRepository.findById(reviewId).orElseThrow();
        review.setReviewStatus("approved");
        review.setReviewedBy(getCurrentUser());
        review.setUpdatedAt(LocalDateTime.now());
        
        reviewRepository.save(review);
    }
    
    public void rejectReview(Long reviewId, String reason) {
        EthicsReview review = reviewRepository.findById(reviewId).orElseThrow();
        review.setReviewStatus("rejected");
        review.setReviewedBy(getCurrentUser());
        review.setUpdatedAt(LocalDateTime.now());
        
        reviewRepository.save(review);
    }
}
```

---

## 🔍 INTEGRACIÓN CON OTROS MÓDULOS

### **1. Analytics Integration**
- **Métricas:** Ethics scores, violation rates, mitigation effectiveness
- **Reportes:** Ethics compliance reports, risk assessments
- **Tendencias:** Ethics score trends, violation patterns

### **2. Governance Integration**
- **Políticas:** Ethics policies and guidelines
- **Compliance:** Ethics compliance monitoring
- **Auditoría:** Ethics audit trails

### **3. Compliance Integration**
- **Regulaciones:** AI Act, GDPR compliance
- **Estándares:** ISO 27001, SOX compliance
- **Reportes:** Regulatory compliance reports

---

## 📊 MÉTRICAS Y KPIs

### **Métricas Principales:**
- **Ethics Score:** 0-100 (objetivo > 80)
- **Violation Rate:** % de violaciones por recurso
- **Mitigation Effectiveness:** % de efectividad de planes
- **Committee Response Time:** Tiempo de respuesta del comité
- **Resolution Time:** Tiempo de resolución de violaciones

### **KPIs del Módulo:**
- **Total Reviews:** Número total de evaluaciones éticas
- **Approval Rate:** % de evaluaciones aprobadas
- **Critical Risks:** Número de riesgos críticos
- **Mitigation Plans:** Planes de mitigación activos

---

## 🎯 CONCLUSIÓN

El módulo **Ethics** proporciona capacidades completas de gestión ética para el gobierno de IA, con:

- **6 Pantallas ZUL** especializadas
- **10 ViewModels** especializados
- **3 Entidades JPA** principales
- **2 Views SQL** optimizadas
- **2 Funciones SQL** de cálculo
- **2 Procedimientos** de procesamiento
- **1 Proceso BPMN** completo
- **1 Regla Drools** de scoring
- **Integración completa** con Analytics, Governance y Compliance

**El módulo está completamente implementado con BPMN y gobierno, superando las funcionalidades del catálogo Next.js original.**
