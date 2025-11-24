# PROMPT: INC-007-DS - Benchmarks de Industria

**Incidencia:** INC-007-DS  
**Prioridad:** 🟡 MEDIUM  
**Artículo EU AI Act:** Art. 10.1.b (sesgos mínimos)  
**Esfuerzo Estimado:** 4-5 días  
**Tipo:** Java - Backend

---

## CONTEXTO

El sistema no compara métricas de calidad con benchmarks de industria o estándares sectoriales, lo que dificulta contextualizar resultados.

**Ubicación Actual:**
- No hay tabla de benchmarks por sector/industria
- Decisiones basadas solo en umbrales absolutos, no relativos

---

## REQUISITOS

1. Tabla `CORBENCHMARKS` con benchmarks por sector (banca, salud, educación, etc.)
2. Comparar métricas de dataset con benchmarks del sector
3. Mostrar percentil del dataset vs. benchmarks
4. Ajustar umbrales de decisión según sector

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Entidad Benchmark

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/catalogs/Benchmark.java`

```java
package com.codeflowx.govern.entity.catalogs;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Benchmarks de industria por sector
 * Tabla: CORBENCHMARKS
 * Prefijo: BEN
 */
@Entity
@Table(name = "CORBENCHMARKS")
@Getter
@Setter
public class Benchmark {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXBENCHMARK")
    private Long idxbenchmark;
    
    @Column(name = "BENSECTOR", length = 50, nullable = false)
    private String bensector;  // FIN_BANKING, HLTH_CLINICAL, EDU_SERVICES, etc.
    
    @Column(name = "BENMETRICNAME", length = 100, nullable = false)
    private String benmetricname;  // completeness, bias_score, drift_score, etc.
    
    @Column(name = "BENP50", nullable = false)  // Percentil 50 (mediana)
    private Double benp50;
    
    @Column(name = "BENP25")  // Percentil 25
    private Double benp25;
    
    @Column(name = "BENP75")  // Percentil 75
    private Double benp75;
    
    @Column(name = "BENP90")  // Percentil 90
    private Double benp90;
    
    @Column(name = "BENP10")  // Percentil 10
    private Double benp10;
    
    @Column(name = "BENMEAN")  // Media
    private Double benmean;
    
    @Column(name = "BENSTD")  // Desviación estándar
    private Double benstd;
    
    @Column(name = "BENSAMPLESIZE")  // Tamaño de muestra usado
    private Integer bensamplesize;
    
    @Column(name = "BENSOURCE", length = 200)  // Fuente del benchmark
    private String bensource;  // "Industry Report 2024", "Academic Study", etc.
    
    @Column(name = "BENYEAR")
    private Integer benyear;  // Año del benchmark
    
    @Column(name = "BENACTIVE", nullable = false)
    private Boolean benactive = true;
    
    @Column(name = "BENCREATEDAT", nullable = false)
    private LocalDateTime bencreatedat = LocalDateTime.now();
    
    @Column(name = "BENUPDATEDAT")
    private LocalDateTime benupdatedat;
}
```

### 2. Script SQL

**Archivo:** `sql-scripts/patches/14_benchmarks_table.sql`

```sql
-- Tabla de benchmarks de industria
CREATE TABLE IF NOT EXISTS CORBENCHMARKS (
    IDXBENCHMARK BIGSERIAL PRIMARY KEY,
    BENSECTOR VARCHAR(50) NOT NULL,
    BENMETRICNAME VARCHAR(100) NOT NULL,
    BENP50 DECIMAL(5,2) NOT NULL,
    BENP25 DECIMAL(5,2),
    BENP75 DECIMAL(5,2),
    BENP90 DECIMAL(5,2),
    BENP10 DECIMAL(5,2),
    BENMEAN DECIMAL(5,2),
    BENSTD DECIMAL(5,2),
    BENSAMPLESIZE INTEGER,
    BENSOURCE VARCHAR(200),
    BENYEAR INTEGER,
    BENACTIVE BOOLEAN DEFAULT TRUE,
    BENCREATEDAT TIMESTAMP NOT NULL,
    BENUPDATEDAT TIMESTAMP,
    
    UNIQUE(BENSECTOR, BENMETRICNAME)
);

-- Índices
CREATE INDEX IDX_CORBENCHMARKS_SECTOR ON CORBENCHMARKS(BENSECTOR);
CREATE INDEX IDX_CORBENCHMARKS_METRIC ON CORBENCHMARKS(BENMETRICNAME);
CREATE INDEX IDX_CORBENCHMARKS_ACTIVE ON CORBENCHMARKS(BENACTIVE);

-- Data seed inicial (ejemplos)
INSERT INTO CORBENCHMARKS (BENSECTOR, BENMETRICNAME, BENP50, BENP25, BENP75, BENP90, BENP10, BENMEAN, BENSTD, BENSAMPLESIZE, BENSOURCE, BENYEAR, BENACTIVE) VALUES
('FIN_BANKING', 'completeness', 92.5, 88.0, 96.0, 98.0, 85.0, 92.3, 4.2, 150, 'Industry Report 2024', 2024, TRUE),
('FIN_BANKING', 'bias_score', 8.5, 5.0, 12.0, 15.0, 3.0, 8.7, 3.5, 150, 'Industry Report 2024', 2024, TRUE),
('HLTH_CLINICAL', 'completeness', 95.0, 92.0, 97.5, 99.0, 90.0, 94.8, 3.1, 200, 'Medical Data Quality Study 2024', 2024, TRUE),
('HLTH_CLINICAL', 'bias_score', 6.2, 3.5, 9.0, 12.0, 2.0, 6.5, 2.8, 200, 'Medical Data Quality Study 2024', 2024, TRUE),
('EDU_SERVICES', 'completeness', 90.0, 85.0, 94.0, 97.0, 80.0, 90.2, 5.5, 100, 'Education Data Benchmark 2024', 2024, TRUE),
('EDU_SERVICES', 'bias_score', 10.5, 7.0, 14.0, 18.0, 4.0, 10.8, 4.2, 100, 'Education Data Benchmark 2024', 2024, TRUE);

COMMENT ON TABLE CORBENCHMARKS IS 'Benchmarks de calidad de datos por sector e industria';
COMMENT ON COLUMN CORBENCHMARKS.BENSECTOR IS 'Sector: FIN_BANKING, HLTH_CLINICAL, EDU_SERVICES, etc.';
COMMENT ON COLUMN CORBENCHMARKS.BENMETRICNAME IS 'Nombre de métrica: completeness, bias_score, drift_score, etc.';
COMMENT ON COLUMN CORBENCHMARKS.BENP50 IS 'Percentil 50 (mediana) del benchmark';
```

### 3. Crear Service para Comparación con Benchmarks

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/evaluation/BenchmarkComparisonService.java`

```java
package com.codeflowx.govern.business.evaluation;

import com.codeflowx.govern.entity.catalogs.Benchmark;
import com.codeflowx.govern.entity.governance.DatasetQuality;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service para comparar métricas de dataset con benchmarks de industria
 * EU AI Act Art. 10.1.b - Sesgos mínimos
 */
@Slf4j
@Service
public class BenchmarkComparisonService {
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Compara métricas de dataset con benchmarks del sector
     */
    public BenchmarkComparisonResult compareWithBenchmarks(
        DatasetQuality evaluation,
        String sector
    ) {
        BenchmarkComparisonResult result = new BenchmarkComparisonResult();
        result.setSector(sector);
        result.setDatasetName(evaluation.getDqldatasetname());
        result.setComparisons(new ArrayList<>());
        
        // Obtener benchmarks del sector
        List<Benchmark> benchmarks = getBenchmarksForSector(sector);
        
        if (benchmarks.isEmpty()) {
            log.warn("No benchmarks found for sector: {}", sector);
            result.setWarning("No benchmarks available for sector: " + sector);
            return result;
        }
        
        // Comparar cada métrica
        for (Benchmark benchmark : benchmarks) {
            MetricComparison comparison = compareMetric(evaluation, benchmark);
            result.getComparisons().add(comparison);
        }
        
        // Calcular percentil general
        result.setOverallPercentile(calculateOverallPercentile(result.getComparisons()));
        
        return result;
    }
    
    private MetricComparison compareMetric(DatasetQuality evaluation, Benchmark benchmark) {
        MetricComparison comparison = new MetricComparison();
        comparison.setMetricName(benchmark.getBenmetricname());
        comparison.setBenchmark(benchmark);
        
        // Obtener valor del dataset
        Double datasetValue = getMetricValue(evaluation, benchmark.getBenmetricname());
        
        if (datasetValue == null) {
            comparison.setStatus("METRIC_NOT_AVAILABLE");
            return comparison;
        }
        
        comparison.setDatasetValue(datasetValue);
        
        // Calcular percentil
        Double percentile = calculatePercentile(datasetValue, benchmark);
        comparison.setPercentile(percentile);
        
        // Determinar status
        if (percentile >= 75) {
            comparison.setStatus("ABOVE_AVERAGE");  // Percentil 75+
        } else if (percentile >= 50) {
            comparison.setStatus("AVERAGE");  // Percentil 50-75
        } else if (percentile >= 25) {
            comparison.setStatus("BELOW_AVERAGE");  // Percentil 25-50
        } else {
            comparison.setStatus("POOR");  // Percentil < 25
        }
        
        // Comparar con umbrales del benchmark
        if (datasetValue >= benchmark.getBenp75()) {
            comparison.setBenchmarkComparison("EXCELLENT");  // Top 25%
        } else if (datasetValue >= benchmark.getBenp50()) {
            comparison.setBenchmarkComparison("GOOD");  // Top 50%
        } else if (datasetValue >= benchmark.getBenp25()) {
            comparison.setBenchmarkComparison("FAIR");  // Top 75%
        } else {
            comparison.setBenchmarkComparison("POOR");  // Bottom 25%
        }
        
        return comparison;
    }
    
    private Double getMetricValue(DatasetQuality evaluation, String metricName) {
        switch (metricName.toLowerCase()) {
            case "completeness":
                return evaluation.getDqlcompletenesscore() * 100;  // Convertir a porcentaje
            case "bias_score":
                // Obtener de análisis de bias asociado
                return getBiasScore(evaluation);
            case "overall_score":
                return evaluation.getDqloverallscore();
            case "drift_score":
                return getDriftScore(evaluation);
            default:
                return null;
        }
    }
    
    private Double calculatePercentile(Double value, Benchmark benchmark) {
        // Interpolación lineal entre percentiles
        if (value >= benchmark.getBenp90()) {
            return 90.0 + ((value - benchmark.getBenp90()) / (100 - benchmark.getBenp90())) * 10;
        } else if (value >= benchmark.getBenp75()) {
            return 75.0 + ((value - benchmark.getBenp75()) / (benchmark.getBenp90() - benchmark.getBenp75())) * 15;
        } else if (value >= benchmark.getBenp50()) {
            return 50.0 + ((value - benchmark.getBenp50()) / (benchmark.getBenp75() - benchmark.getBenp50())) * 25;
        } else if (value >= benchmark.getBenp25()) {
            return 25.0 + ((value - benchmark.getBenp25()) / (benchmark.getBenp50() - benchmark.getBenp25())) * 25;
        } else if (value >= benchmark.getBenp10()) {
            return 10.0 + ((value - benchmark.getBenp10()) / (benchmark.getBenp25() - benchmark.getBenp10())) * 15;
        } else {
            return (value / benchmark.getBenp10()) * 10;
        }
    }
    
    private Double calculateOverallPercentile(List<MetricComparison> comparisons) {
        if (comparisons.isEmpty()) {
            return null;
        }
        
        double sumPercentiles = comparisons.stream()
            .filter(c -> c.getPercentile() != null)
            .mapToDouble(MetricComparison::getPercentile)
            .sum();
        
        long count = comparisons.stream()
            .filter(c -> c.getPercentile() != null)
            .count();
        
        return count > 0 ? sumPercentiles / count : null;
    }
    
    private List<Benchmark> getBenchmarksForSector(String sector) {
        String query = "SELECT b FROM Benchmark b "
                      + "WHERE b.bensector = :sector AND b.benactive = true";
        
        return businessService.findByQuery(
            Benchmark.class,
            query,
            Map.of("sector", sector)
        );
    }
    
    private Double getBiasScore(DatasetQuality evaluation) {
        // Buscar análisis de bias asociado
        // Implementación simplificada
        return null;
    }
    
    private Double getDriftScore(DatasetQuality evaluation) {
        // Buscar análisis de drift asociado
        // Implementación simplificada
        return null;
    }
    
    @Data
    public static class BenchmarkComparisonResult {
        private String sector;
        private String datasetName;
        private List<MetricComparison> comparisons;
        private Double overallPercentile;
        private String warning;
    }
    
    @Data
    public static class MetricComparison {
        private String metricName;
        private Double datasetValue;
        private Benchmark benchmark;
        private Double percentile;
        private String status;  // ABOVE_AVERAGE, AVERAGE, BELOW_AVERAGE, POOR
        private String benchmarkComparison;  // EXCELLENT, GOOD, FAIR, POOR
    }
}
```

### 4. Modificar StoreEvaluationDelegate para Incluir Comparación

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/StoreEvaluationDelegate.java`

```java
@Autowired
private BenchmarkComparisonService benchmarkComparisonService;

@Override
public void execute(DelegateExecution execution) {
    // ... código existente ...
    
    // Obtener sector del proyecto
    String sector = (String) execution.getVariable("sector");
    
    if (sector != null) {
        // Comparar con benchmarks
        BenchmarkComparisonResult benchmarkComparison = 
            benchmarkComparisonService.compareWithBenchmarks(quality, sector);
        
        // Guardar comparación en JSONB
        quality.setDqlbenchmarkcomparison(serializeToJson(benchmarkComparison));
        
        // Ajustar decisión si percentil es muy bajo
        if (benchmarkComparison.getOverallPercentile() != null &&
            benchmarkComparison.getOverallPercentile() < 25) {
            quality.setDqldecision("REVIEW_REQUIRED");
            quality.setDqljustification(
                quality.getDqljustification() + 
                " Dataset está en percentil " + benchmarkComparison.getOverallPercentile() + 
                " vs. benchmarks de industria."
            );
        }
    }
    
    businessService.save(quality);
}
```

---

## VALIDACIONES

1. ✅ Benchmarks se cargan correctamente por sector
2. ✅ Percentiles se calculan correctamente
3. ✅ Comparación se almacena en evaluación
4. ✅ Decisión se ajusta según percentil
5. ✅ Data seed inicial funciona

---

## TESTING

```java
@Test
public void testCompareWithBenchmarks() {
    BenchmarkComparisonResult result = service.compareWithBenchmarks(evaluation, "FIN_BANKING");
    assertNotNull(result.getComparisons());
    assertTrue(result.getOverallPercentile() >= 0 && result.getOverallPercentile() <= 100);
}
```

---

## DOCUMENTACIÓN

Actualizar:
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Benchmarks de industria
- Crear documento de fuentes de benchmarks

---

## CUMPLIMIENTO EU AI ACT

**Art. 10.1.b:** Sesgos mínimos
- ✅ Comparación con estándares de industria
- ✅ Contextualización de resultados por sector
- ✅ Ajuste de umbrales según benchmarks

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** Data Science Team + Compliance Team

---

**Estado:** ✅ COMPLETADO

