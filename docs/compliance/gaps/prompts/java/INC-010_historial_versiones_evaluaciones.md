# PROMPT: INC-010 - Historial de Versiones de Evaluaciones

**Incidencia:** INC-010  
**Prioridad:** 🟢 LOW  
**Artículo:** ISO 42001 8.2.2 (data governance)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Java - Backend

---

## CONTEXTO

No hay tracking de versiones de evaluaciones cuando un dataset se re-evalúa, lo que dificulta comparar evolución de calidad a lo largo del tiempo.

**Ubicación Actual:**
- `DatasetQuality.java` - No tiene campo de versión
- No hay relación entre evaluaciones del mismo dataset

---

## REQUISITOS

1. Campo `DQLVERSION` en `DQLDATASETQUALITY`
2. Relación con evaluación anterior (`DQLPREVIOUSEVALUATIONID`)
3. Vista de historial de evaluaciones por dataset
4. Gráfico de tendencia de calidad a lo largo del tiempo

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Extender Entidad `DatasetQuality.java`

**Añadir campos:**

```java
// Versionado
@Column(name = "DQLVERSION", nullable = false)
private Integer dqlversion = 1;  // Versión de la evaluación

@Column(name = "DQLPREVIOUSEVALUATIONID")
private Long dqlpreviousevaluationid;  // FK a evaluación anterior

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "DQLPREVIOUSEVALUATIONID", insertable = false, updatable = false)
private DatasetQuality previousEvaluation;

@OneToMany(mappedBy = "previousEvaluation", cascade = CascadeType.ALL)
private List<DatasetQuality> nextEvaluations;
```

### 2. Script SQL

**Archivo:** `sql-scripts/patches/12_dataset_quality_versioning.sql`

```sql
-- Añadir campos de versionado
ALTER TABLE DQLDATASETQUALITY 
ADD COLUMN IF NOT EXISTS DQLVERSION INTEGER DEFAULT 1,
ADD COLUMN IF NOT EXISTS DQLPREVIOUSEVALUATIONID BIGINT;

-- Foreign key a evaluación anterior
ALTER TABLE DQLDATASETQUALITY
ADD CONSTRAINT FK_DQL_PREVIOUS_EVAL 
FOREIGN KEY (DQLPREVIOUSEVALUATIONID) 
REFERENCES DQLDATASETQUALITY(IDXDATASETQUALITY);

-- Índice para búsqueda por versión
CREATE INDEX IF NOT EXISTS IDX_DQL_VERSION 
ON DQLDATASETQUALITY(DQLDATASETNAME, DQLVERSION);

-- Vista de historial
CREATE OR REPLACE VIEW VW_DATASET_QUALITY_HISTORY AS
SELECT 
    dq.IDXDATASETQUALITY,
    dq.DQLDATASETNAME,
    dq.DQLVERSION,
    dq.DQLOVERALLSCORE,
    dq.DQLQUALITYRATING,
    dq.DQLCREATEDAT,
    dq.DQLPREVIOUSEVALUATIONID,
    prev.DQLOVERALLSCORE AS PREVIOUSSCORE,
    dq.DQLOVERALLSCORE - COALESCE(prev.DQLOVERALLSCORE, dq.DQLOVERALLSCORE) AS SCORECHANGE
FROM DQLDATASETQUALITY dq
LEFT JOIN DQLDATASETQUALITY prev ON dq.DQLPREVIOUSEVALUATIONID = prev.IDXDATASETQUALITY
ORDER BY dq.DQLDATASETNAME, dq.DQLVERSION;
```

### 3. Service para gestión de versiones

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/governance/DatasetQualityVersioningService.java`

```java
@Service
public class DatasetQualityVersioningService {
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Crea nueva versión de evaluación
     */
    public DatasetQuality createNewVersion(String datasetName, DatasetQuality newEvaluation) {
        // Buscar última versión
        DatasetQuality lastVersion = getLatestVersion(datasetName);
        
        if (lastVersion != null) {
            newEvaluation.setDqlversion(lastVersion.getDqlversion() + 1);
            newEvaluation.setDqlpreviousevaluationid(lastVersion.getIdxdatasetquality());
        } else {
            newEvaluation.setDqlversion(1);
        }
        
        newEvaluation.setDqldatasetname(datasetName);
        businessService.save(newEvaluation);
        
        return newEvaluation;
    }
    
    /**
     * Obtiene historial completo de evaluaciones
     */
    public List<DatasetQuality> getEvaluationHistory(String datasetName) {
        String query = "SELECT dq FROM DatasetQuality dq "
                      + "WHERE dq.dqldatasetname = :datasetName "
                      + "ORDER BY dq.dqlversion ASC";
        
        return businessService.findByQuery(
            DatasetQuality.class,
            query,
            Map.of("datasetName", datasetName)
        );
    }
}
```

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** Backend Team

---

**Estado:** ✅ COMPLETADO

