# 🔧 DOCUMENTACIÓN TÉCNICA - MÓDULO GOVERNANCE

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo governance

---

## 🎯 RESUMEN EJECUTIVO

El módulo **governance** es el **corazón del sistema** de gobierno de IA con **3 entidades JPA principales**, **5 vistas optimizadas**, **funciones SQL** y **procedimientos** para cálculo de scores y detección de anomalías.

---

## 📊 ENTIDADES JPA

### **1. GovernanceMetric**
```java
@Entity
@Table(name = "governance_metric")
public class GovernanceMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idxgovernancemetric;
    
    private String gvmname;
    private String gvmdescription;
    private String gvmtype;
    private String gvmcategory;
    private String gvmvalue;
    private String gvmthreshold;
    private String gvmstatus;
    private String gvmtrend;
    private String gvmcompliance;
    private String gvmrisk;
    private LocalDateTime gvmcreatedat;
    private String gvmcreatedby;
    private LocalDateTime gvmupdatedat;
    private String gvmupdatedby;
}
```

### **2. AgentGovernance**
```java
@Entity
@Table(name = "agent_governance")
public class AgentGovernance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idxagentgovernance;
    
    private String agngovname;
    private String agngovdescription;
    private String agngovstatus;
    private String agngovcompliance;
    private String agngovrisk;
    private String agngovmetrics;
    private String agngovpolicies;
    private String agngovaudit;
    private LocalDateTime agngovcreatedat;
    private String agngovcreatedby;
}
```

### **3. TrainingGovernance**
```java
@Entity
@Table(name = "training_governance")
public class TrainingGovernance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idxtraininggovernance;
    
    private String trggovname;
    private String trggovdescription;
    private String trggovstatus;
    private String trggovcompliance;
    private String trggovdata;
    private String trggovmodel;
    private String trggovmetrics;
    private String trggovaudit;
    private LocalDateTime trggovcreatedat;
    private String trggovcreatedby;
}
```

---

## 📊 VISTAS OPTIMIZADAS

### **1. GovernanceOverview**
```sql
CREATE VIEW governance_overview AS
SELECT 
    gm.idxgovernancemetric,
    gm.gvmname,
    gm.gvmtype,
    gm.gvmcategory,
    gm.gvmvalue,
    gm.gvmstatus,
    gm.gvmcompliance,
    gm.gvmrisk,
    COUNT(ag.idxagentgovernance) as agent_count,
    COUNT(tg.idxtraininggovernance) as training_count,
    gm.gvmcreatedat
FROM governance_metric gm
LEFT JOIN agent_governance ag ON gm.idxgovernancemetric = ag.idxgovernancemetric
LEFT JOIN training_governance tg ON gm.idxgovernancemetric = tg.idxgovernancemetric
GROUP BY gm.idxgovernancemetric, gm.gvmname, gm.gvmtype, gm.gvmcategory, 
         gm.gvmvalue, gm.gvmstatus, gm.gvmcompliance, gm.gvmrisk, gm.gvmcreatedat;
```

### **2. GovernanceDashboardSummary**
```sql
CREATE VIEW governance_dashboard_summary AS
SELECT 
    gm.gvmcategory,
    COUNT(*) as metric_count,
    AVG(gm.gvmvalue::numeric) as avg_value,
    COUNT(CASE WHEN gm.gvmstatus = 'HEALTHY' THEN 1 END) as healthy_count,
    COUNT(CASE WHEN gm.gvmstatus = 'WARNING' THEN 1 END) as warning_count,
    COUNT(CASE WHEN gm.gvmstatus = 'CRITICAL' THEN 1 END) as critical_count,
    COUNT(CASE WHEN gm.gvmcompliance = 'COMPLIANT' THEN 1 END) as compliant_count
FROM governance_metric gm
GROUP BY gm.gvmcategory;
```

### **3. GovernanceKpisExecutive**
```sql
CREATE VIEW governance_kpis_executive AS
SELECT 
    'OVERALL_GOVERNANCE_SCORE' as kpi_name,
    AVG(gm.gvmvalue::numeric) as kpi_value,
    'PERCENTAGE' as kpi_unit,
    CASE 
        WHEN AVG(gm.gvmvalue::numeric) >= 90 THEN 'EXCELLENT'
        WHEN AVG(gm.gvmvalue::numeric) >= 80 THEN 'GOOD'
        WHEN AVG(gm.gvmvalue::numeric) >= 70 THEN 'FAIR'
        ELSE 'POOR'
    END as kpi_status
FROM governance_metric gm
WHERE gm.gvmtype = 'SCORE'

UNION ALL

SELECT 
    'COMPLIANCE_RATE' as kpi_name,
    COUNT(CASE WHEN gm.gvmcompliance = 'COMPLIANT' THEN 1 END)::numeric / 
    COUNT(*)::numeric * 100 as kpi_value,
    'PERCENTAGE' as kpi_unit,
    CASE 
        WHEN COUNT(CASE WHEN gm.gvmcompliance = 'COMPLIANT' THEN 1 END)::numeric / 
             COUNT(*)::numeric * 100 >= 95 THEN 'EXCELLENT'
        WHEN COUNT(CASE WHEN gm.gvmcompliance = 'COMPLIANT' THEN 1 END)::numeric / 
             COUNT(*)::numeric * 100 >= 85 THEN 'GOOD'
        ELSE 'NEEDS_IMPROVEMENT'
    END as kpi_status
FROM governance_metric gm;
```

### **4. GovernanceMetricsSummary**
```sql
CREATE VIEW governance_metrics_summary AS
SELECT 
    gm.gvmtype,
    gm.gvmcategory,
    COUNT(*) as total_metrics,
    AVG(gm.gvmvalue::numeric) as avg_value,
    MIN(gm.gvmvalue::numeric) as min_value,
    MAX(gm.gvmvalue::numeric) as max_value,
    STDDEV(gm.gvmvalue::numeric) as std_deviation,
    COUNT(CASE WHEN gm.gvmtrend = 'IMPROVING' THEN 1 END) as improving_count,
    COUNT(CASE WHEN gm.gvmtrend = 'STABLE' THEN 1 END) as stable_count,
    COUNT(CASE WHEN gm.gvmtrend = 'DEGRADING' THEN 1 END) as degrading_count
FROM governance_metric gm
GROUP BY gm.gvmtype, gm.gvmcategory;
```

### **5. GovernanceAuditTrailDetailed**
```sql
CREATE VIEW governance_audit_trail_detailed AS
SELECT 
    gat.idxgovernanceaudittrail,
    gat.gatoperation,
    gat.gatentity,
    gat.gatentityid,
    gat.gatoldvalue,
    gat.gatnewvalue,
    gat.gatchangedby,
    gat.gatchangedat,
    gat.gatreason,
    gat.gatipaddress,
    gat.gatuseragent,
    gm.gvmname as metric_name,
    gm.gvmcategory as metric_category
FROM governance_audit_trail gat
LEFT JOIN governance_metric gm ON gat.gatentityid = gm.idxgovernancemetric
ORDER BY gat.gatchangedat DESC;
```

---

## ⚙️ FUNCIONES SQL

### **1. Calcular Score de Gobierno**
```sql
CREATE FUNCTION calculate_governance_score(
    p_category VARCHAR(50),
    p_period_start TIMESTAMP,
    p_period_end TIMESTAMP
) RETURNS NUMERIC AS $$
DECLARE
    total_score NUMERIC;
    metric_count INTEGER;
BEGIN
    SELECT 
        AVG(gvmvalue::numeric),
        COUNT(*)
    INTO total_score, metric_count
    FROM governance_metric
    WHERE gvmcategory = p_category
    AND gvmcreatedat BETWEEN p_period_start AND p_period_end;
    
    RETURN COALESCE(total_score, 0);
END;
$$ LANGUAGE plpgsql;
```

### **2. Detectar Anomalías de Gobierno**
```sql
CREATE FUNCTION detect_governance_anomalies(
    p_metric_id BIGINT,
    p_threshold NUMERIC DEFAULT 2.0
) RETURNS TABLE(
    anomaly_detected BOOLEAN,
    anomaly_score NUMERIC,
    deviation_percent NUMERIC
) AS $$
DECLARE
    current_value NUMERIC;
    avg_value NUMERIC;
    std_dev NUMERIC;
    deviation_score NUMERIC;
BEGIN
    -- Obtener valor actual
    SELECT gvmvalue::numeric INTO current_value
    FROM governance_metric
    WHERE idxgovernancemetric = p_metric_id;
    
    -- Calcular promedio y desviación estándar histórica
    SELECT 
        AVG(gvmvalue::numeric),
        STDDEV(gvmvalue::numeric)
    INTO avg_value, std_dev
    FROM governance_metric
    WHERE gvmtype = (SELECT gvmtype FROM governance_metric WHERE idxgovernancemetric = p_metric_id)
    AND gvmcreatedat < (SELECT gvmcreatedat FROM governance_metric WHERE idxgovernancemetric = p_metric_id);
    
    -- Calcular score de desviación
    IF std_dev > 0 THEN
        deviation_score = ABS(current_value - avg_value) / std_dev;
    ELSE
        deviation_score = 0;
    END IF;
    
    RETURN QUERY SELECT 
        deviation_score > p_threshold,
        deviation_score,
        ((current_value - avg_value) / avg_value * 100);
END;
$$ LANGUAGE plpgsql;
```

### **3. Evaluar Compliance**
```sql
CREATE FUNCTION evaluate_governance_compliance(
    p_entity_type VARCHAR(50),
    p_entity_id BIGINT
) RETURNS TABLE(
    compliance_score NUMERIC,
    compliance_status VARCHAR(20),
    violations_count INTEGER,
    recommendations TEXT[]
) AS $$
DECLARE
    total_metrics INTEGER;
    compliant_metrics INTEGER;
    compliance_percentage NUMERIC;
    violations INTEGER;
    recs TEXT[];
BEGIN
    -- Contar métricas totales y cumplidas
    SELECT 
        COUNT(*),
        COUNT(CASE WHEN gvmcompliance = 'COMPLIANT' THEN 1 END)
    INTO total_metrics, compliant_metrics
    FROM governance_metric
    WHERE gvmcategory = p_entity_type;
    
    -- Calcular porcentaje de compliance
    IF total_metrics > 0 THEN
        compliance_percentage = (compliant_metrics::numeric / total_metrics::numeric) * 100;
    ELSE
        compliance_percentage = 0;
    END IF;
    
    -- Contar violaciones
    violations = total_metrics - compliant_metrics;
    
    -- Generar recomendaciones
    IF compliance_percentage < 80 THEN
        recs := ARRAY['Improve compliance monitoring', 'Review governance policies', 'Implement corrective actions'];
    ELSIF compliance_percentage < 95 THEN
        recs := ARRAY['Monitor compliance trends', 'Address minor violations'];
    ELSE
        recs := ARRAY['Maintain current compliance level'];
    END IF;
    
    RETURN QUERY SELECT 
        compliance_percentage,
        CASE 
            WHEN compliance_percentage >= 95 THEN 'EXCELLENT'
            WHEN compliance_percentage >= 85 THEN 'GOOD'
            WHEN compliance_percentage >= 70 THEN 'FAIR'
            ELSE 'POOR'
        END,
        violations,
        recs;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔄 PROCEDIMIENTOS SQL

### **1. Actualizar Métricas de Gobierno**
```sql
CREATE PROCEDURE update_governance_metrics(
    p_category VARCHAR(50),
    p_force_update BOOLEAN DEFAULT FALSE
)
LANGUAGE plpgsql AS $$
DECLARE
    metric_record RECORD;
    new_value NUMERIC;
BEGIN
    -- Actualizar métricas por categoría
    FOR metric_record IN 
        SELECT * FROM governance_metric 
        WHERE gvmcategory = p_category
    LOOP
        -- Calcular nuevo valor basado en tipo de métrica
        CASE metric_record.gvmtype
            WHEN 'COMPLIANCE_SCORE' THEN
                new_value := calculate_compliance_score(metric_record.idxgovernancemetric);
            WHEN 'RISK_SCORE' THEN
                new_value := calculate_risk_score(metric_record.idxgovernancemetric);
            WHEN 'PERFORMANCE_SCORE' THEN
                new_value := calculate_performance_score(metric_record.idxgovernancemetric);
            ELSE
                new_value := metric_record.gvmvalue::numeric;
        END CASE;
        
        -- Actualizar métrica
        UPDATE governance_metric
        SET gvmvalue = new_value::text,
            gvmupdatedat = NOW(),
            gvmupdatedby = 'system'
        WHERE idxgovernancemetric = metric_record.idxgovernancemetric;
        
        -- Registrar en auditoría
        INSERT INTO governance_audit_trail (
            gatoperation, gatentity, gatentityid, gatoldvalue, gatnewvalue,
            gatchangedby, gatchangedat, gatreason
        ) VALUES (
            'UPDATE', 'governance_metric', metric_record.idxgovernancemetric,
            metric_record.gvmvalue, new_value::text, 'system', NOW(),
            'Automatic metric update'
        );
    END LOOP;
END;
$$;
```

### **2. Generar Reporte de Gobierno**
```sql
CREATE PROCEDURE generate_governance_report(
    p_report_type VARCHAR(50),
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP
)
LANGUAGE plpgsql AS $$
DECLARE
    report_id BIGINT;
    report_data JSONB;
BEGIN
    -- Crear reporte
    INSERT INTO governance_report (
        grrtype, grrperiod_start, grrperiod_end, grrstatus, grrcreatedat, grrcreatedby
    ) VALUES (
        p_report_type, p_start_date, p_end_date, 'GENERATING', NOW(), 'system'
    ) RETURNING idxgovernancereport INTO report_id;
    
    -- Generar datos del reporte según tipo
    CASE p_report_type
        WHEN 'EXECUTIVE_SUMMARY' THEN
            report_data := (
                SELECT jsonb_build_object(
                    'overall_score', AVG(gvmvalue::numeric),
                    'compliance_rate', COUNT(CASE WHEN gvmcompliance = 'COMPLIANT' THEN 1 END)::numeric / COUNT(*)::numeric * 100,
                    'risk_level', COUNT(CASE WHEN gvmrisk = 'HIGH' THEN 1 END),
                    'total_metrics', COUNT(*)
                )
                FROM governance_metric
                WHERE gvmcreatedat BETWEEN p_start_date AND p_end_date
            );
            
        WHEN 'COMPLIANCE_DETAIL' THEN
            report_data := (
                SELECT jsonb_agg(
                    jsonb_build_object(
                        'category', gvmcategory,
                        'compliance_rate', COUNT(CASE WHEN gvmcompliance = 'COMPLIANT' THEN 1 END)::numeric / COUNT(*)::numeric * 100,
                        'violations', COUNT(CASE WHEN gvmcompliance != 'COMPLIANT' THEN 1 END)
                    )
                )
                FROM governance_metric
                WHERE gvmcreatedat BETWEEN p_start_date AND p_end_date
                GROUP BY gvmcategory
            );
    END CASE;
    
    -- Actualizar reporte con datos
    UPDATE governance_report
    SET grrdata = report_data,
        grrstatus = 'COMPLETED',
        grrcompletedat = NOW()
    WHERE idxgovernancereport = report_id;
END;
$$;
```

---

## 🔗 RELACIONES Y DEPENDENCIAS

### **Relaciones Principales:**
- **GovernanceMetric** → **AgentGovernance** (1:N)
- **GovernanceMetric** → **TrainingGovernance** (1:N)
- **GovernanceMetric** → **GovernanceAuditTrail** (1:N)
- **GovernanceMetric** → **GovernanceReport** (1:N)

### **Dependencias Externas:**
- **Agent Module:** Para métricas de gobierno de agentes
- **Model Module:** Para métricas de gobierno de modelos
- **RAG Module:** Para métricas de gobierno de sistemas RAG
- **Prompt Module:** Para métricas de gobierno de prompts

---

## ✅ CONCLUSIÓN

La **documentación técnica del módulo governance** proporciona:

- 🏗️ **3 entidades JPA** principales para gobierno completo
- 📊 **5 vistas optimizadas** para analytics ejecutivos
- ⚙️ **3 funciones SQL** para cálculos avanzados
- 🔄 **2 procedimientos** para operaciones complejas
- 🔗 **Relaciones claras** con todos los módulos

**Esta estructura está diseñada** para soportar el gobierno completo de IA con métricas ejecutivas, compliance y auditoría.
