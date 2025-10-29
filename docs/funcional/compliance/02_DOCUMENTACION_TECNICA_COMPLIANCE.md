# 🔧 DOCUMENTACIÓN TÉCNICA - MÓDULO COMPLIANCE

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo compliance

---

## 🎯 RESUMEN EJECUTIVO

El módulo **compliance** implementa un **sistema completo de auditoría y cumplimiento** con **4 entidades JPA principales**, **3 vistas optimizadas**, **funciones SQL** y **procedimientos** para evaluación automática y gestión de hallazgos de compliance.

---

## 📊 ENTIDADES JPA

### **1. ComplianceAssessment**
```java
@Entity
@Table(name = "GOVCOMPLIANCEASSESSMENTS")
public class ComplianceAssessment {
    @Id
    @Column(name = "IDXCOMPLIANCEASSESSMENT")
    private Long idxcomplianceassessment;
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "ASSESSMENTNAME")
    private String assessmentname;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @NotNull @NotBlank
    @Column(name = "COMPLIANCEFRAMEWORK")
    private String complianceframework; // AI_ACT, GDPR, SOX, ISO27001
    
    @NotNull @NotBlank
    @Column(name = "STATUS")
    private String status; // PLANNED, IN_PROGRESS, COMPLETED, FAILED
    
    @Column(name = "OVERALLSCORE")
    private BigDecimal overallscore;
    
    @Column(name = "COMPLIANCEPERCENTAGE")
    private BigDecimal compliancepercentage;
    
    @NotNull
    @Column(name = "ASSESSMENTDATE")
    private Timestamp assessmentdate;
    
    @Column(name = "VALIDUNTIL")
    private Timestamp validuntil;
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "ASSESSORNAME")
    private String assessorname;
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "ASSESSOREMAIL")
    private String assessoremail;
    
    @NotNull
    @Column(name = "CREATEDAT")
    private Timestamp createdat;
    
    @NotNull
    @Column(name = "UPDATEDAT")
    private Timestamp updatedat;
    
    @OneToMany(mappedBy = "assessment")
    private List<ComplianceFinding> subgovcompliancefindings;
    
    @OneToMany(mappedBy = "assessment")
    private List<ComplianceRequirement> subgovcompliancerequirements;
}
```

### **2. ComplianceFinding**
```java
@Entity
@Table(name = "GOVCOMPLIANCEFINDINGS")
public class ComplianceFinding {
    @Id
    @Column(name = "IDXCOMPLIANCEFINDING")
    private Long idxcompliancefinding;
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "FINDINGTITLE")
    private String findingtitle;
    
    @NotNull @NotBlank
    @Column(name = "FINDINGDESCRIPTION")
    private String findingdescription;
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "SEVERITY")
    private String severity; // CRITICAL, HIGH, MEDIUM, LOW
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "CATEGORY")
    private String category; // SECURITY, PRIVACY, TRANSPARENCY, FAIRNESS
    
    @NotNull @NotBlank
    @Column(name = "STATUS")
    private String status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    
    @Column(name = "REMEDIATIONPLAN")
    private String remediationplan;
    
    @Column(name = "REMEDIATIONDEADLINE")
    private Timestamp remediationdeadline;
    
    @Size(max = 100)
    @Column(name = "ASSIGNEDTO")
    private String assignedto;
    
    @NotNull
    @Column(name = "CREATEDAT")
    private Timestamp createdat;
    
    @NotNull
    @Column(name = "UPDATEDAT")
    private Timestamp updatedat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDGOVCOMPLIANCEASSESSMENTS0")
    private ComplianceAssessment assessment;
}
```

### **3. ComplianceRequirement**
```java
@Entity
@Table(name = "GOVCOMPLIANCEREQUIREMENTS")
public class ComplianceRequirement {
    @Id
    @Column(name = "IDXCOMPLIANCEREQUIREMENT")
    private Long idxcompliancerequirement;
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "REQUIREMENTID")
    private String requirementid;
    
    @NotNull @NotBlank
    @Column(name = "REQUIREMENTTEXT")
    private String requirementtext;
    
    @NotNull @NotBlank @Size(max = 100)
    @Column(name = "CATEGORY")
    private String category;
    
    @NotNull @NotBlank
    @Column(name = "PRIORITY")
    private String priority; // CRITICAL, HIGH, MEDIUM, LOW
    
    @NotNull @NotBlank
    @Column(name = "STATUS")
    private String status; // NOT_ASSESSED, COMPLIANT, NON_COMPLIANT, PARTIALLY_COMPLIANT
    
    @Column(name = "EVIDENCE")
    private String evidence;
    
    @Column(name = "NOTES")
    private String notes;
    
    @Column(name = "ASSESSEDAT")
    private Timestamp assessedat;
    
    @Size(max = 100)
    @Column(name = "ASSESSEDBY")
    private String assessedby;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDGOVCOMPLIANCEASSESSMENTS0")
    private ComplianceAssessment assessment;
}
```

### **4. AgentCompliance**
```java
@Entity
@Table(name = "AGTAGENTCOMPLIANCE")
public class AgentCompliance {
    @Id
    @Column(name = "IDXAGENTCOMPLIANCE")
    private Long idxagentcompliance;
    
    @NotNull @NotBlank
    @Column(name = "AGTCOMPLIANCETYPE")
    private String agtcompliancetype; // AI_ACT, GDPR, INTERNAL_POLICY
    
    @NotNull @NotBlank
    @Column(name = "AGTCOMPLIANCESTATUS")
    private String agtcompliancestatus; // COMPLIANT, NON_COMPLIANT, UNDER_REVIEW
    
    @Column(name = "AGTCOMPLIANCESCORE")
    private BigDecimal agtcompliancescore;
    
    @Column(name = "AGTRISKLEVEL")
    private String agtrisklevel; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "AGTREQUIREMENTS")
    private String agtrequirements; // JSON
    
    @Column(name = "AGTASSESSMENTRESULTS")
    private String agtassessmentresults; // JSON
    
    @Column(name = "AGTVIOLATIONS")
    private String agtviolations; // JSON
    
    @Column(name = "AGTREMEDIATIONPLAN")
    private String agtremediationplan; // JSON
    
    @Column(name = "AGTEVIDENCE")
    private String agtevidence; // JSON
    
    @Column(name = "AGTAUDITTRAIL")
    private String agtaudittrail; // JSON
    
    @Column(name = "AGTLASTASSESSMENTAT")
    private Timestamp agtlastassessmentat;
    
    @Column(name = "AGTNEXTASSESSMENTAT")
    private Timestamp agtnextassessmentat;
    
    @Size(max = 50)
    @Column(name = "AGTASSESSMENTFREQUENCY")
    private String agtassessmentfrequency; // DAILY, WEEKLY, MONTHLY, QUARTERLY
    
    @Size(max = 255)
    @Column(name = "AGTASSESSORID")
    private String agtassessorid;
    
    @Size(max = 255)
    @Column(name = "AGTASSESSORNAME")
    private String agtassessorname;
    
    @Size(max = 100)
    @Column(name = "AGTASSESSMENTMETHOD")
    private String agtassessmentmethod; // AUTOMATED, MANUAL, HYBRID
    
    @Column(name = "AGTCERTIFICATIONSTATUS")
    private String agtcertificationstatus; // CERTIFIED, PENDING, EXPIRED
    
    @Column(name = "AGTCERTIFICATIONDATE")
    private Timestamp agtcertificationdate;
    
    @Column(name = "AGTCERTIFICATIONEXPIRY")
    private Timestamp agtcertificationexpiry;
    
    @Size(max = 255)
    @Column(name = "AGTCERTIFICATIONAUTHORITY")
    private String agtcertificationauthority;
    
    @Column(name = "AGTNOTES")
    private String agtnotes;
    
    @NotNull @NotBlank @Size(max = 255)
    @Column(name = "AGTCREATEDBY")
    private String agtcreatedby;
    
    @Size(max = 255)
    @Column(name = "AGTUPDATEDBY")
    private String agtupdatedby;
    
    @NotNull
    @Column(name = "AGTCREATEDAT")
    private Timestamp agtcreatedat;
    
    @Column(name = "AGTUPDATEDAT")
    private Timestamp agtupdatedat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDAGTAGENTS0")
    private Agent agent;
}
```

---

## 📊 VISTAS OPTIMIZADAS

### **1. ComplianceByFramework**
```sql
CREATE VIEW V_COMPLIANCE_BY_FRAMEWORK AS
SELECT 
    cf.complianceframework,
    COUNT(*) as total_items,
    COUNT(CASE WHEN ca.status = 'COMPLETED' THEN 1 END) as active_items,
    COUNT(CASE WHEN ca.status = 'COMPLETED' AND ca.overallscore >= 80 THEN 1 END) as deployed_items,
    COUNT(CASE WHEN ca.status = 'IN_PROGRESS' THEN 1 END) as training_items,
    COUNT(CASE WHEN ca.status = 'FAILED' THEN 1 END) as offline_items,
    AVG(ca.overallscore) as avg_score,
    NOW() as generated_at
FROM GOVCOMPLIANCEASSESSMENTS ca
LEFT JOIN GOVCOMPLIANCEFINDINGS cf ON ca.idxcomplianceassessment = cf.idgovcomplianceassessments0
GROUP BY cf.complianceframework;
```

### **2. ComplianceGapsAnalysis**
```sql
CREATE VIEW V_COMPLIANCE_GAPS_ANALYSIS AS
SELECT 
    cr.category,
    cr.priority,
    COUNT(*) as total_requirements,
    COUNT(CASE WHEN cr.status = 'COMPLIANT' THEN 1 END) as compliant_count,
    COUNT(CASE WHEN cr.status = 'NON_COMPLIANT' THEN 1 END) as non_compliant_count,
    COUNT(CASE WHEN cr.status = 'PARTIALLY_COMPLIANT' THEN 1 END) as partially_compliant_count,
    COUNT(CASE WHEN cr.status = 'NOT_ASSESSED' THEN 1 END) as not_assessed_count,
    ROUND(
        COUNT(CASE WHEN cr.status = 'COMPLIANT' THEN 1 END)::numeric / 
        COUNT(*)::numeric * 100, 2
    ) as compliance_percentage,
    NOW() as generated_at
FROM GOVCOMPLIANCEREQUIREMENTS cr
GROUP BY cr.category, cr.priority
ORDER BY cr.priority DESC, compliance_percentage ASC;
```

### **3. AgentComplianceStatus**
```sql
CREATE VIEW V_AGENT_COMPLIANCE_STATUS AS
SELECT 
    a.agtname as agent_name,
    ac.agtcompliancetype as compliance_type,
    ac.agtcompliancestatus as compliance_status,
    ac.agtcompliancescore as compliance_score,
    ac.agtrisklevel as risk_level,
    ac.agtcertificationstatus as certification_status,
    ac.agtlastassessmentat as last_assessment,
    ac.agtnextassessmentat as next_assessment,
    ac.agtassessmentfrequency as assessment_frequency,
    CASE 
        WHEN ac.agtnextassessmentat < NOW() THEN 'OVERDUE'
        WHEN ac.agtnextassessmentat < NOW() + INTERVAL '7 days' THEN 'DUE_SOON'
        ELSE 'SCHEDULED'
    END as assessment_status,
    NOW() as generated_at
FROM AGTAGENTS a
LEFT JOIN AGTAGENTCOMPLIANCE ac ON a.idxagent = ac.idagtagents0
WHERE a.agtstatus = 'ACTIVE';
```

---

## ⚙️ FUNCIONES SQL

### **1. Calcular Compliance Ponderado**
```sql
CREATE FUNCTION calculate_weighted_compliance(
    p_assessment_id BIGINT,
    p_framework VARCHAR(50)
) RETURNS NUMERIC AS $$
DECLARE
    total_weight NUMERIC := 0;
    weighted_score NUMERIC := 0;
    requirement_record RECORD;
BEGIN
    -- Calcular score ponderado por requisitos
    FOR requirement_record IN 
        SELECT cr.priority, cr.status, 
               CASE cr.priority 
                   WHEN 'CRITICAL' THEN 4
                   WHEN 'HIGH' THEN 3
                   WHEN 'MEDIUM' THEN 2
                   WHEN 'LOW' THEN 1
                   ELSE 1
               END as weight
        FROM GOVCOMPLIANCEREQUIREMENTS cr
        WHERE cr.idgovcomplianceassessments0 = p_assessment_id
    LOOP
        total_weight := total_weight + requirement_record.weight;
        
        CASE requirement_record.status
            WHEN 'COMPLIANT' THEN
                weighted_score := weighted_score + (requirement_record.weight * 100);
            WHEN 'PARTIALLY_COMPLIANT' THEN
                weighted_score := weighted_score + (requirement_record.weight * 50);
            WHEN 'NON_COMPLIANT' THEN
                weighted_score := weighted_score + (requirement_record.weight * 0);
            ELSE
                weighted_score := weighted_score + (requirement_record.weight * 0);
        END CASE;
    END LOOP;
    
    IF total_weight > 0 THEN
        RETURN weighted_score / total_weight;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;
```

### **2. Estimar Esfuerzo de Compliance**
```sql
CREATE FUNCTION estimate_compliance_effort(
    p_framework VARCHAR(50),
    p_entity_type VARCHAR(50),
    p_entity_id BIGINT
) RETURNS TABLE(
    effort_hours INTEGER,
    effort_level VARCHAR(20),
    complexity_score NUMERIC
) AS $$
DECLARE
    total_requirements INTEGER;
    avg_complexity NUMERIC;
    estimated_hours INTEGER;
BEGIN
    -- Contar requisitos del framework
    SELECT COUNT(*)
    INTO total_requirements
    FROM GOVCOMPLIANCEREQUIREMENTS cr
    JOIN GOVCOMPLIANCEASSESSMENTS ca ON cr.idgovcomplianceassessments0 = ca.idxcomplianceassessment
    WHERE ca.complianceframework = p_framework;
    
    -- Calcular complejidad promedio
    SELECT AVG(
        CASE priority
            WHEN 'CRITICAL' THEN 4.0
            WHEN 'HIGH' THEN 3.0
            WHEN 'MEDIUM' THEN 2.0
            WHEN 'LOW' THEN 1.0
            ELSE 1.0
        END
    )
    INTO avg_complexity
    FROM GOVCOMPLIANCEREQUIREMENTS cr
    JOIN GOVCOMPLIANCEASSESSMENTS ca ON cr.idgovcomplianceassessments0 = ca.idxcomplianceassessment
    WHERE ca.complianceframework = p_framework;
    
    -- Estimar horas basado en requisitos y complejidad
    estimated_hours := total_requirements * avg_complexity * 2;
    
    -- Determinar nivel de esfuerzo
    RETURN QUERY SELECT 
        estimated_hours,
        CASE 
            WHEN estimated_hours <= 20 THEN 'LOW'
            WHEN estimated_hours <= 40 THEN 'MEDIUM'
            WHEN estimated_hours <= 80 THEN 'HIGH'
            ELSE 'CRITICAL'
        END,
        avg_complexity;
END;
$$ LANGUAGE plpgsql;
```

### **3. Obtener Porcentaje de Compliance**
```sql
CREATE FUNCTION get_compliance_percentage(
    p_framework VARCHAR(50),
    p_entity_type VARCHAR(50) DEFAULT NULL,
    p_entity_id BIGINT DEFAULT NULL
) RETURNS NUMERIC AS $$
DECLARE
    total_requirements INTEGER;
    compliant_requirements INTEGER;
    compliance_percentage NUMERIC;
BEGIN
    -- Contar requisitos totales
    SELECT COUNT(*)
    INTO total_requirements
    FROM GOVCOMPLIANCEREQUIREMENTS cr
    JOIN GOVCOMPLIANCEASSESSMENTS ca ON cr.idgovcomplianceassessments0 = ca.idxcomplianceassessment
    WHERE ca.complianceframework = p_framework
    AND (p_entity_type IS NULL OR cr.category = p_entity_type)
    AND (p_entity_id IS NULL OR cr.idxcompliancerequirement = p_entity_id);
    
    -- Contar requisitos cumplidos
    SELECT COUNT(*)
    INTO compliant_requirements
    FROM GOVCOMPLIANCEREQUIREMENTS cr
    JOIN GOVCOMPLIANCEASSESSMENTS ca ON cr.idgovcomplianceassessments0 = ca.idxcomplianceassessment
    WHERE ca.complianceframework = p_framework
    AND cr.status = 'COMPLIANT'
    AND (p_entity_type IS NULL OR cr.category = p_entity_type)
    AND (p_entity_id IS NULL OR cr.idxcompliancerequirement = p_entity_id);
    
    -- Calcular porcentaje
    IF total_requirements > 0 THEN
        compliance_percentage := (compliant_requirements::numeric / total_requirements::numeric) * 100;
    ELSE
        compliance_percentage := 0;
    END IF;
    
    RETURN compliance_percentage;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔄 PROCEDIMIENTOS SQL

### **1. Verificación Masiva de Compliance**
```sql
CREATE PROCEDURE bulk_compliance_check(
    p_framework VARCHAR(50),
    p_assessment_id BIGINT DEFAULT NULL
)
LANGUAGE plpgsql AS $$
DECLARE
    assessment_record RECORD;
    requirement_record RECORD;
    finding_record RECORD;
    compliance_score NUMERIC;
    violation_count INTEGER := 0;
BEGIN
    -- Procesar evaluaciones del framework
    FOR assessment_record IN 
        SELECT * FROM GOVCOMPLIANCEASSESSMENTS 
        WHERE complianceframework = p_framework
        AND (p_assessment_id IS NULL OR idxcomplianceassessment = p_assessment_id)
    LOOP
        -- Calcular score de compliance
        compliance_score := calculate_weighted_compliance(
            assessment_record.idxcomplianceassessment, 
            p_framework
        );
        
        -- Actualizar evaluación
        UPDATE GOVCOMPLIANCEASSESSMENTS
        SET overallscore = compliance_score,
            compliancepercentage = compliance_score,
            updatedat = NOW()
        WHERE idxcomplianceassessment = assessment_record.idxcomplianceassessment;
        
        -- Verificar requisitos y crear hallazgos
        FOR requirement_record IN 
            SELECT * FROM GOVCOMPLIANCEREQUIREMENTS
            WHERE idgovcomplianceassessments0 = assessment_record.idxcomplianceassessment
            AND status IN ('NON_COMPLIANT', 'NOT_ASSESSED')
        LOOP
            -- Crear hallazgo de compliance
            INSERT INTO GOVCOMPLIANCEFINDINGS (
                findingtitle, findingdescription, severity, category, status,
                remediationplan, assignedto, createdat, updatedat, idgovcomplianceassessments0
            ) VALUES (
                'Non-compliance: ' || requirement_record.requirementid,
                'Requirement not met: ' || requirement_record.requirementtext,
                CASE requirement_record.priority
                    WHEN 'CRITICAL' THEN 'CRITICAL'
                    WHEN 'HIGH' THEN 'HIGH'
                    ELSE 'MEDIUM'
                END,
                requirement_record.category,
                'OPEN',
                'Review requirement and implement necessary controls',
                'compliance-team@company.com',
                NOW(),
                NOW(),
                assessment_record.idxcomplianceassessment
            );
            
            violation_count := violation_count + 1;
        END LOOP;
        
        -- Actualizar estado de evaluación
        UPDATE GOVCOMPLIANCEASSESSMENTS
        SET status = CASE 
            WHEN compliance_score >= 90 THEN 'COMPLETED'
            WHEN compliance_score >= 70 THEN 'IN_PROGRESS'
            ELSE 'FAILED'
        END
        WHERE idxcomplianceassessment = assessment_record.idxcomplianceassessment;
    END LOOP;
    
    -- Registrar resultado
    RAISE NOTICE 'Bulk compliance check completed. Violations found: %', violation_count;
END;
$$;
```

### **2. Generar Reporte de Compliance**
```sql
CREATE PROCEDURE generate_compliance_report(
    p_framework VARCHAR(50),
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP
)
LANGUAGE plpgsql AS $$
DECLARE
    report_id BIGINT;
    report_data JSONB;
BEGIN
    -- Crear reporte
    INSERT INTO GOVCOMPLIANCEREPORTS (
        reporttype, framework, period_start, period_end, status, createdat
    ) VALUES (
        'COMPLIANCE_SUMMARY', p_framework, p_start_date, p_end_date, 'GENERATING', NOW()
    ) RETURNING idxcompliancereport INTO report_id;
    
    -- Generar datos del reporte
    report_data := (
        SELECT jsonb_build_object(
            'framework', p_framework,
            'period', jsonb_build_object(
                'start', p_start_date,
                'end', p_end_date
            ),
            'summary', jsonb_build_object(
                'total_assessments', COUNT(*),
                'completed_assessments', COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END),
                'avg_compliance_score', AVG(overallscore),
                'avg_compliance_percentage', AVG(compliancepercentage)
            ),
            'findings', (
                SELECT jsonb_agg(
                    jsonb_build_object(
                        'severity', severity,
                        'category', category,
                        'count', count(*)
                    )
                )
                FROM GOVCOMPLIANCEFINDINGS cf
                JOIN GOVCOMPLIANCEASSESSMENTS ca ON cf.idgovcomplianceassessments0 = ca.idxcomplianceassessment
                WHERE ca.complianceframework = p_framework
                AND ca.assessmentdate BETWEEN p_start_date AND p_end_date
                GROUP BY severity, category
            ),
            'requirements', (
                SELECT jsonb_agg(
                    jsonb_build_object(
                        'category', category,
                        'priority', priority,
                        'compliance_rate', ROUND(
                            COUNT(CASE WHEN status = 'COMPLIANT' THEN 1 END)::numeric / 
                            COUNT(*)::numeric * 100, 2
                        )
                    )
                )
                FROM GOVCOMPLIANCEREQUIREMENTS cr
                JOIN GOVCOMPLIANCEASSESSMENTS ca ON cr.idgovcomplianceassessments0 = ca.idxcomplianceassessment
                WHERE ca.complianceframework = p_framework
                AND ca.assessmentdate BETWEEN p_start_date AND p_end_date
                GROUP BY category, priority
            )
        )
        FROM GOVCOMPLIANCEASSESSMENTS
        WHERE complianceframework = p_framework
        AND assessmentdate BETWEEN p_start_date AND p_end_date
    );
    
    -- Actualizar reporte con datos
    UPDATE GOVCOMPLIANCEREPORTS
    SET reportdata = report_data,
        status = 'COMPLETED',
        completedat = NOW()
    WHERE idxcompliancereport = report_id;
END;
$$;
```

---

## 🔗 RELACIONES Y DEPENDENCIAS

### **Relaciones Principales:**
- **ComplianceAssessment** → **ComplianceFinding** (1:N)
- **ComplianceAssessment** → **ComplianceRequirement** (1:N)
- **Agent** → **AgentCompliance** (1:N)

### **Dependencias Externas:**
- **Agent Module:** Para compliance específico de agentes
- **Governance Module:** Para métricas de gobierno
- **Audit Module:** Para trazabilidad de auditorías

---

## ✅ CONCLUSIÓN

La **documentación técnica del módulo compliance** proporciona:

- 🏗️ **4 entidades JPA** principales para compliance completo
- 📊 **3 vistas optimizadas** para analytics de compliance
- ⚙️ **3 funciones SQL** para cálculos avanzados
- 🔄 **2 procedimientos** para operaciones complejas
- 🔗 **Relaciones claras** con módulos de agentes y governance

**Esta estructura está diseñada** para soportar el compliance regulatorio completo con evaluaciones automáticas, gestión de hallazgos y reportes ejecutivos.
