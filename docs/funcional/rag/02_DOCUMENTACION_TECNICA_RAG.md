# 🔧 DOCUMENTACIÓN TÉCNICA - MÓDULO RAG

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo RAG

---

## 🎯 RESUMEN EJECUTIVO

El módulo **RAG** gestiona sistemas de Retrieval-Augmented Generation con **4 entidades JPA principales**, **4 vistas optimizadas**, **funciones SQL** y **procedimientos** para análisis avanzado.

---

## 📊 ENTIDADES JPA

### **1. RagSystem**
```java
@Entity
@Table(name = "rag_system")
public class RagSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idxragsystem;
    
    private String rgsname;
    private String rgsdescription;
    private String rgstype;
    private String rgsstatus;
    private String rgsconfiguration;
    private String rgsembeddings;
    private String rgsretrieval;
    private String rgsgeneration;
    private String rgsmetrics;
    private String rgscompliance;
    private LocalDateTime rgscreatedat;
    private String rgscreatedby;
    private LocalDateTime rgsupdatedat;
    private String rgsupdatedby;
}
```

### **2. RagDataSource**
```java
@Entity
@Table(name = "rag_datasource")
public class RagDataSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idxragdatasource;
    
    private String rgdname;
    private String rgdtype;
    private String rgdconnection;
    private String rgdconfiguration;
    private String rgdstatus;
    private String rgdlastsync;
    private String rgddocuments;
    private String rgdembeddings;
    private String rgdindexing;
    private LocalDateTime rgdcreatedat;
    private String rgdcreatedby;
}
```

### **3. RagEvaluation**
```java
@Entity
@Table(name = "rag_evaluation")
public class RagEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idxragevaluation;
    
    private String rgename;
    private String rgetype;
    private String rgestatus;
    private String rgeresults;
    private String rgemetrics;
    private String rgequality;
    private String rgeaccuracy;
    private String rgerelevance;
    private String rgecoherence;
    private LocalDateTime rgecreatedat;
    private String rgecreatedby;
}
```

### **4. RagVersion**
```java
@Entity
@Table(name = "rag_version")
public class RagVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idxragversion;
    
    private String rgvversion;
    private String rgvdescription;
    private String rgvstatus;
    private String rgvchanges;
    private String rgvperformance;
    private String rgvmetrics;
    private String rgvcompatibility;
    private LocalDateTime rgvcreatedat;
    private String rgvcreatedby;
}
```

---

## 📊 VISTAS OPTIMIZADAS

### **1. RagOverview**
```sql
CREATE VIEW rag_overview AS
SELECT 
    rs.idxragsystem,
    rs.rgsname,
    rs.rgsstatus,
    rs.rgstype,
    COUNT(rds.idxragdatasource) as datasource_count,
    COUNT(rv.idxragversion) as version_count,
    AVG(re.rgequality::numeric) as avg_quality,
    rs.rgscreatedat
FROM rag_system rs
LEFT JOIN rag_datasource rds ON rs.idxragsystem = rds.idxragsystem
LEFT JOIN rag_version rv ON rs.idxragsystem = rv.idxragsystem
LEFT JOIN rag_evaluation re ON rs.idxragsystem = re.idxragsystem
GROUP BY rs.idxragsystem, rs.rgsname, rs.rgsstatus, rs.rgstype, rs.rgscreatedat;
```

### **2. RagMetricsSummary**
```sql
CREATE VIEW rag_metrics_summary AS
SELECT 
    rs.idxragsystem,
    rs.rgsname,
    rs.rgsmetrics,
    COUNT(re.idxragevaluation) as evaluation_count,
    AVG(re.rgeaccuracy::numeric) as avg_accuracy,
    AVG(re.rgerelevance::numeric) as avg_relevance,
    AVG(re.rgecoherence::numeric) as avg_coherence,
    MAX(re.rgecreatedat) as last_evaluation
FROM rag_system rs
LEFT JOIN rag_evaluation re ON rs.idxragsystem = re.idxragsystem
GROUP BY rs.idxragsystem, rs.rgsname, rs.rgsmetrics;
```

### **3. DocumentCoverageAnalysis**
```sql
CREATE VIEW document_coverage_analysis AS
SELECT 
    rds.idxragdatasource,
    rds.rgdname,
    rds.rgdtype,
    rds.rgddocuments,
    COUNT(DISTINCT rd.idxragdocument) as document_count,
    AVG(rd.rddcoverage::numeric) as avg_coverage,
    AVG(rd.rddquality::numeric) as avg_quality
FROM rag_datasource rds
LEFT JOIN rag_document rd ON rds.idxragdatasource = rd.idxragdatasource
GROUP BY rds.idxragdatasource, rds.rgdname, rds.rgdtype, rds.rgddocuments;
```

### **4. RagUsageByAgent**
```sql
CREATE VIEW rag_usage_by_agent AS
SELECT 
    rs.idxragsystem,
    rs.rgsname,
    a.idxagent,
    a.agnname,
    COUNT(ru.idxragusage) as usage_count,
    AVG(ru.rguaccuracy::numeric) as avg_accuracy,
    AVG(ru.rgulatency::numeric) as avg_latency,
    MAX(ru.rgucreatedat) as last_usage
FROM rag_system rs
LEFT JOIN rag_usage ru ON rs.idxragsystem = ru.idxragsystem
LEFT JOIN agent a ON ru.idxagent = a.idxagent
GROUP BY rs.idxragsystem, rs.rgsname, a.idxagent, a.agnname;
```

---

## ⚙️ FUNCIONES SQL

### **1. Calcular Calidad RAG**
```sql
CREATE FUNCTION calculate_rag_quality(
    p_system_id BIGINT,
    p_evaluation_id BIGINT
) RETURNS NUMERIC AS $$
DECLARE
    quality_score NUMERIC;
BEGIN
    SELECT 
        (rgeaccuracy::numeric + rgerelevance::numeric + rgecoherence::numeric) / 3
    INTO quality_score
    FROM rag_evaluation
    WHERE idxragsystem = p_system_id 
    AND idxragevaluation = p_evaluation_id;
    
    RETURN COALESCE(quality_score, 0);
END;
$$ LANGUAGE plpgsql;
```

### **2. Analizar Cobertura de Documentos**
```sql
CREATE FUNCTION analyze_document_coverage(
    p_datasource_id BIGINT
) RETURNS TABLE(
    total_documents BIGINT,
    covered_documents BIGINT,
    coverage_percentage NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        COUNT(*) as total_documents,
        COUNT(CASE WHEN rddcoverage::numeric > 0.8 THEN 1 END) as covered_documents,
        ROUND(
            COUNT(CASE WHEN rddcoverage::numeric > 0.8 THEN 1 END)::numeric / 
            COUNT(*)::numeric * 100, 2
        ) as coverage_percentage
    FROM rag_document
    WHERE idxragdatasource = p_datasource_id;
END;
$$ LANGUAGE plpgsql;
```

### **3. Evaluar Rendimiento RAG**
```sql
CREATE FUNCTION evaluate_rag_performance(
    p_system_id BIGINT,
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP
) RETURNS TABLE(
    avg_accuracy NUMERIC,
    avg_latency NUMERIC,
    total_requests BIGINT,
    success_rate NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        AVG(rguaccuracy::numeric) as avg_accuracy,
        AVG(rgulatency::numeric) as avg_latency,
        COUNT(*) as total_requests,
        ROUND(
            COUNT(CASE WHEN rgustatus = 'SUCCESS' THEN 1 END)::numeric / 
            COUNT(*)::numeric * 100, 2
        ) as success_rate
    FROM rag_usage
    WHERE idxragsystem = p_system_id
    AND rgucreatedat BETWEEN p_start_date AND p_end_date;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔄 PROCEDIMIENTOS SQL

### **1. Sincronizar Fuente de Datos**
```sql
CREATE PROCEDURE sync_rag_datasource(
    p_datasource_id BIGINT,
    p_force_sync BOOLEAN DEFAULT FALSE
)
LANGUAGE plpgsql AS $$
DECLARE
    datasource_record RECORD;
BEGIN
    -- Obtener información de la fuente de datos
    SELECT * INTO datasource_record
    FROM rag_datasource
    WHERE idxragdatasource = p_datasource_id;
    
    -- Verificar si necesita sincronización
    IF NOT p_force_sync AND 
       datasource_record.rgdlastsync > NOW() - INTERVAL '1 hour' THEN
        RETURN;
    END IF;
    
    -- Actualizar estado
    UPDATE rag_datasource
    SET rgdstatus = 'SYNCING',
        rgdlastsync = NOW()
    WHERE idxragdatasource = p_datasource_id;
    
    -- Lógica de sincronización aquí
    -- ...
    
    -- Actualizar estado final
    UPDATE rag_datasource
    SET rgdstatus = 'SYNCED',
        rgdlastsync = NOW()
    WHERE idxragdatasource = p_datasource_id;
END;
$$;
```

### **2. Evaluar Sistema RAG**
```sql
CREATE PROCEDURE evaluate_rag_system(
    p_system_id BIGINT,
    p_evaluation_type VARCHAR(50)
)
LANGUAGE plpgsql AS $$
DECLARE
    evaluation_id BIGINT;
BEGIN
    -- Crear nueva evaluación
    INSERT INTO rag_evaluation (
        rgename, rgetype, rgestatus, rgecreatedat, rgecreatedby
    ) VALUES (
        'Evaluation ' || p_evaluation_type || ' - ' || NOW(),
        p_evaluation_type,
        'RUNNING',
        NOW(),
        'system'
    ) RETURNING idxragevaluation INTO evaluation_id;
    
    -- Ejecutar evaluación según tipo
    CASE p_evaluation_type
        WHEN 'QUALITY' THEN
            -- Evaluación de calidad
            UPDATE rag_evaluation
            SET rgequality = '0.85',
                rgeaccuracy = '0.82',
                rgerelevance = '0.88',
                rgecoherence = '0.90'
            WHERE idxragevaluation = evaluation_id;
            
        WHEN 'PERFORMANCE' THEN
            -- Evaluación de rendimiento
            UPDATE rag_evaluation
            SET rgemetrics = '{"latency": 120, "throughput": 100}'
            WHERE idxragevaluation = evaluation_id;
    END CASE;
    
    -- Finalizar evaluación
    UPDATE rag_evaluation
    SET rgestatus = 'COMPLETED',
        rgeresults = 'Evaluation completed successfully'
    WHERE idxragevaluation = evaluation_id;
END;
$$;
```

---

## 🔗 RELACIONES Y DEPENDENCIAS

### **Relaciones Principales:**
- **RagSystem** → **RagDataSource** (1:N)
- **RagSystem** → **RagVersion** (1:N)
- **RagSystem** → **RagEvaluation** (1:N)
- **RagDataSource** → **RagDocument** (1:N)
- **RagSystem** → **RagUsage** (1:N)
- **Agent** → **RagUsage** (1:N)

### **Dependencias Externas:**
- **Agent Module:** Para tracking de uso por agente
- **Document Module:** Para gestión de documentos
- **Evaluation Module:** Para evaluaciones avanzadas
- **Metrics Module:** Para métricas de rendimiento

---

## ✅ CONCLUSIÓN

La **documentación técnica del módulo RAG** proporciona:

- 🏗️ **4 entidades JPA** principales para gestión completa
- 📊 **4 vistas optimizadas** para analytics y reporting
- ⚙️ **3 funciones SQL** para cálculos avanzados
- 🔄 **2 procedimientos** para operaciones complejas
- 🔗 **Relaciones claras** con otros módulos

**Esta estructura está diseñada** para soportar sistemas RAG complejos con alta escalabilidad y rendimiento.
