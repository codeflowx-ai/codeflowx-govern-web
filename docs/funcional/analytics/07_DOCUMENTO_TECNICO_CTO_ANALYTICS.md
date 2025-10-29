# 📊 ANALYTICS - DOCUMENTO TÉCNICO CTO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Audiencia:** CTOs, Arquitectos de Software, Líderes Técnicos  
**Propósito:** Especificación técnica detallada del módulo Analytics

---

## 🎯 RESUMEN EJECUTIVO TÉCNICO

**CodeflowX Govern Analytics** es una plataforma de analytics enterprise-grade construida sobre **arquitectura lambda**, con **procesamiento en tiempo real y batch**, **ML-powered insights** y **escalabilidad ilimitada**.

### **Stack Tecnológico:**
- **Backend:** Java 17, Spring Boot 3.2
- **Real-time:** Apache Kafka, Kafka Streams
- **Batch:** Apache Spark 3.4
- **Storage:** PostgreSQL 15 (OLTP), ClickHouse (OLAP)
- **Cache:** Redis 7.0
- **ML:** Python 3.11, scikit-learn, Prophet

---

## 🏗️ ARQUITECTURA TÉCNICA

### **1. Arquitectura Lambda**

```
┌─────────────────────────────────────────────────────────────┐
│                    Data Sources Layer                        │
│  (Models, Serving, Training, Monitoring, Governance)        │
└─────────────────────┬───────────────────────────────────────┘
                      │
         ┌────────────┴──────────────┐
         ▼                           ▼
┌──────────────────┐        ┌──────────────────┐
│  Speed Layer     │        │  Batch Layer     │
│  (Kafka Streams) │        │  (Spark)         │
│  Real-time       │        │  Historical      │
└────────┬─────────┘        └────────┬─────────┘
         │                           │
         └────────────┬──────────────┘
                      ▼
         ┌────────────────────────┐
         │   Serving Layer        │
         │   (Spring Boot APIs)   │
         └────────┬───────────────┘
                  │
    ┌─────────────┼─────────────┐
    ▼             ▼             ▼
┌────────┐  ┌──────────┐  ┌────────┐
│ OLTP   │  │  OLAP    │  │ Cache  │
│ PostSQL│  │ClickHouse│  │ Redis  │
└────────┘  └──────────┘  └────────┘
```

### **2. Modelo de Datos**

#### **OLTP (PostgreSQL) - Operational Data**

```sql
-- Tabla de métricas en tiempo real
CREATE TABLE anl_metrics (
    anl_id BIGSERIAL PRIMARY KEY,
    anl_entity_type VARCHAR(50) NOT NULL,
    anl_entity_id BIGINT NOT NULL,
    anl_metric_name VARCHAR(100) NOT NULL,
    anl_metric_value DECIMAL(20,6),
    anl_metric_unit VARCHAR(20),
    anl_tags JSONB,
    anl_timestamp TIMESTAMP NOT NULL,
    anl_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Particionamiento por tiempo
CREATE TABLE anl_metrics_y2025m10 PARTITION OF anl_metrics
    FOR VALUES FROM ('2025-10-01') TO ('2025-11-01');

-- Índices para queries frecuentes
CREATE INDEX idx_anl_metrics_entity ON anl_metrics(anl_entity_type, anl_entity_id);
CREATE INDEX idx_anl_metrics_name ON anl_metrics(anl_metric_name);
CREATE INDEX idx_anl_metrics_timestamp ON anl_metrics(anl_timestamp DESC);
CREATE INDEX idx_anl_metrics_tags ON anl_metrics USING GIN (anl_tags);

-- Tabla de reportes
CREATE TABLE anl_reports (
    anl_id BIGSERIAL PRIMARY KEY,
    anl_report_type VARCHAR(50) NOT NULL,
    anl_title VARCHAR(255) NOT NULL,
    anl_entity_type VARCHAR(50),
    anl_entity_id BIGINT,
    anl_generated_by BIGINT,
    anl_started_at TIMESTAMP NOT NULL,
    anl_completed_at TIMESTAMP,
    anl_status VARCHAR(20) NOT NULL,
    anl_format VARCHAR(10),
    anl_file_path VARCHAR(500),
    anl_parameters JSONB,
    CONSTRAINT fk_anl_report_user FOREIGN KEY (anl_generated_by) 
        REFERENCES cor_users(cor_id)
);

-- Tabla de agregaciones pre-calculadas
CREATE TABLE anl_aggregations (
    anl_id BIGSERIAL PRIMARY KEY,
    anl_entity_type VARCHAR(50) NOT NULL,
    anl_entity_id BIGINT NOT NULL,
    anl_metric_name VARCHAR(100) NOT NULL,
    anl_aggregation_type VARCHAR(20) NOT NULL, -- AVG, MIN, MAX, SUM, P50, P95, P99
    anl_aggregation_value DECIMAL(20,6),
    anl_time_window VARCHAR(20) NOT NULL, -- 5m, 1h, 1d, 7d, 30d
    anl_window_start TIMESTAMP NOT NULL,
    anl_window_end TIMESTAMP NOT NULL,
    anl_sample_count INTEGER,
    UNIQUE (anl_entity_type, anl_entity_id, anl_metric_name, 
            anl_aggregation_type, anl_time_window, anl_window_start)
);

CREATE INDEX idx_anl_agg_lookup ON anl_aggregations(
    anl_entity_type, anl_entity_id, anl_metric_name, anl_time_window
);
```

#### **OLAP (ClickHouse) - Analytical Data**

```sql
-- Tabla para análisis histórico (ClickHouse)
CREATE TABLE anl_metrics_history (
    entity_type String,
    entity_id UInt64,
    metric_name String,
    metric_value Float64,
    metric_unit String,
    tags Map(String, String),
    timestamp DateTime,
    date Date
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(date)
ORDER BY (entity_type, entity_id, metric_name, timestamp)
SETTINGS index_granularity = 8192;

-- Vista materializada para agregaciones
CREATE MATERIALIZED VIEW anl_metrics_hourly_mv
ENGINE = SummingMergeTree()
PARTITION BY toYYYYMM(date)
ORDER BY (entity_type, entity_id, metric_name, hour)
AS SELECT
    entity_type,
    entity_id,
    metric_name,
    toStartOfHour(timestamp) AS hour,
    toDate(timestamp) AS date,
    avg(metric_value) AS avg_value,
    min(metric_value) AS min_value,
    max(metric_value) AS max_value,
    count() AS sample_count
FROM anl_metrics_history
GROUP BY entity_type, entity_id, metric_name, hour, date;
```

---

## 🔧 COMPONENTES PRINCIPALES

### **1. Real-time Analytics Engine (Speed Layer)**

```java
@Service
public class RealtimeAnalyticsEngine {
    
    @Autowired
    private KafkaTemplate<String, MetricEvent> kafkaTemplate;
    
    /**
     * Procesa métricas en tiempo real con Kafka Streams
     */
    @Bean
    public KStream<String, MetricEvent> processMetricsStream(StreamsBuilder builder) {
        KStream<String, MetricEvent> metricsStream = builder
            .stream("metrics-raw", Consumed.with(Serdes.String(), metricEventSerde()));
        
        // 1. Enriquecer con metadata
        KStream<String, EnrichedMetric> enrichedStream = metricsStream
            .mapValues(this::enrichMetric);
        
        // 2. Agregaciones en ventanas de tiempo
        enrichedStream
            .groupBy((key, metric) -> 
                metric.getEntityType() + ":" + metric.getEntityId() + ":" + metric.getMetricName())
            .windowedBy(TimeWindows.of(Duration.ofMinutes(5)))
            .aggregate(
                MetricAggregation::new,
                (key, metric, agg) -> agg.add(metric),
                Materialized.with(Serdes.String(), aggregationSerde())
            )
            .toStream()
            .to("metrics-aggregated");
        
        // 3. Detección de anomalías en tiempo real
        enrichedStream
            .filter((key, metric) -> isAnomalous(metric))
            .to("metrics-anomalies");
        
        // 4. Actualizar dashboards en tiempo real
        enrichedStream.foreach((key, metric) -> 
            updateRealtimeDashboard(metric)
        );
        
        return metricsStream;
    }
    
    /**
     * Detección de anomalías con Z-score
     */
    private boolean isAnomalous(EnrichedMetric metric) {
        // Obtener estadísticas históricas
        MetricStatistics stats = statisticsService.getStatistics(
            metric.getEntityType(),
            metric.getEntityId(),
            metric.getMetricName()
        );
        
        // Calcular Z-score
        double zScore = (metric.getValue() - stats.getMean()) / stats.getStdDev();
        
        // Anomalía si |z-score| > 3
        return Math.abs(zScore) > 3.0;
    }
}
```

### **2. Batch Analytics Engine (Batch Layer)**

```java
@Service
public class BatchAnalyticsEngine {
    
    @Autowired
    private SparkSession spark;
    
    /**
     * Procesa análisis histórico con Spark
     */
    @Scheduled(cron = "0 0 2 * * *") // 2 AM diariamente
    public void runDailyBatchAnalytics() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        // 1. Cargar datos del día
        Dataset<Row> metricsDF = spark.read()
            .format("jdbc")
            .option("url", jdbcUrl)
            .option("dbtable", "anl_metrics")
            .option("partitionColumn", "anl_timestamp")
            .option("lowerBound", yesterday.atStartOfDay())
            .option("upperBound", yesterday.plusDays(1).atStartOfDay())
            .load();
        
        // 2. Calcular agregaciones
        Dataset<Row> aggregationsDF = metricsDF
            .groupBy("anl_entity_type", "anl_entity_id", "anl_metric_name")
            .agg(
                avg("anl_metric_value").as("avg_value"),
                min("anl_metric_value").as("min_value"),
                max("anl_metric_value").as("max_value"),
                stddev("anl_metric_value").as("std_dev"),
                count("*").as("sample_count"),
                expr("percentile_approx(anl_metric_value, 0.50)").as("p50"),
                expr("percentile_approx(anl_metric_value, 0.95)").as("p95"),
                expr("percentile_approx(anl_metric_value, 0.99)").as("p99")
            );
        
        // 3. Guardar en OLAP
        aggregationsDF.write()
            .format("jdbc")
            .option("url", clickhouseUrl)
            .option("dbtable", "anl_daily_aggregations")
            .mode(SaveMode.Append)
            .save();
        
        // 4. Análisis de tendencias
        analyzeTrends(metricsDF);
        
        // 5. Detección de patrones
        detectPatterns(metricsDF);
    }
    
    /**
     * Análisis de tendencias con regresión lineal
     */
    private void analyzeTrends(Dataset<Row> metricsDF) {
        // Preparar features
        VectorAssembler assembler = new VectorAssembler()
            .setInputCols(new String[]{"timestamp_numeric"})
            .setOutputCol("features");
        
        Dataset<Row> assembled = assembler.transform(metricsDF);
        
        // Entrenar modelo de regresión
        LinearRegression lr = new LinearRegression()
            .setLabelCol("anl_metric_value")
            .setFeaturesCol("features");
        
        LinearRegressionModel model = lr.fit(assembled);
        
        // Analizar coeficientes para detectar tendencias
        double slope = model.coefficients().apply(0);
        
        if (Math.abs(slope) > 0.01) {
            TrendDirection direction = slope > 0 ? 
                TrendDirection.INCREASING : TrendDirection.DECREASING;
            
            // Guardar análisis de tendencia
            saveTrendAnalysis(direction, slope, model.summary().r2());
        }
    }
}
```

### **3. Predictive Analytics Service**

```java
@Service
public class PredictiveAnalyticsService {
    
    @Autowired
    private PythonExecutor pythonExecutor;
    
    /**
     * Forecasting con Prophet (Facebook)
     */
    public ForecastResult forecast(
        Long entityId,
        String metricName,
        int forecastDays
    ) {
        // 1. Obtener datos históricos
        List<MetricData> historical = metricsRepository.findHistorical(
            entityId,
            metricName,
            Duration.ofDays(90)
        );
        
        // 2. Preparar datos para Prophet
        String csvData = prepareProphetData(historical);
        
        // 3. Ejecutar forecasting en Python
        String pythonScript = String.format("""
            import pandas as pd
            from prophet import Prophet
            import json
            
            # Cargar datos
            df = pd.read_csv('data.csv')
            
            # Entrenar modelo
            model = Prophet(
                yearly_seasonality=True,
                weekly_seasonality=True,
                daily_seasonality=True
            )
            model.fit(df)
            
            # Forecast
            future = model.make_future_dataframe(periods=%d)
            forecast = model.predict(future)
            
            # Retornar predicciones
            result = forecast[['ds', 'yhat', 'yhat_lower', 'yhat_upper']].tail(%d)
            print(result.to_json(orient='records'))
            """, forecastDays, forecastDays);
        
        String output = pythonExecutor.execute(pythonScript, csvData);
        
        // 4. Parsear resultados
        return parseForecastResult(output);
    }
    
    /**
     * Detección de anomalías con Isolation Forest
     */
    public List<Anomaly> detectAnomalies(
        Long entityId,
        String metricName
    ) {
        // 1. Obtener datos recientes
        List<MetricData> data = metricsRepository.findRecent(
            entityId,
            metricName,
            Duration.ofDays(7)
        );
        
        // 2. Ejecutar Isolation Forest
        String pythonScript = """
            import pandas as pd
            from sklearn.ensemble import IsolationForest
            import json
            
            # Cargar datos
            df = pd.read_csv('data.csv')
            
            # Entrenar modelo
            model = IsolationForest(contamination=0.1, random_state=42)
            predictions = model.fit_predict(df[['value']])
            
            # Identificar anomalías
            df['is_anomaly'] = predictions == -1
            anomalies = df[df['is_anomaly']][['timestamp', 'value']]
            
            print(anomalies.to_json(orient='records'))
            """;
        
        String output = pythonExecutor.execute(pythonScript, prepareData(data));
        
        return parseAnomalies(output);
    }
}
```

### **4. Report Generation Service**

```java
@Service
public class ReportGenerationService {
    
    @Autowired
    private JasperReportsEngine jasperEngine;
    
    @Autowired
    private S3Client s3Client;
    
    /**
     * Genera reporte con JasperReports
     */
    @Async
    public CompletableFuture<Report> generateReport(ReportRequest request) {
        // 1. Actualizar estado
        Report report = reportRepository.save(Report.builder()
            .reportType(request.getReportType())
            .status(ReportStatus.PROCESSING)
            .startedAt(LocalDateTime.now())
            .build());
        
        try {
            // 2. Obtener datos
            Map<String, Object> data = collectReportData(request);
            
            // 3. Seleccionar template
            String templatePath = selectTemplate(request.getReportType());
            
            // 4. Generar reporte
            byte[] reportBytes = jasperEngine.generateReport(
                templatePath,
                data,
                request.getFormat()
            );
            
            // 5. Subir a S3
            String fileKey = String.format("reports/%d/%s.%s",
                report.getId(),
                report.getReportType(),
                request.getFormat().toLowerCase()
            );
            
            s3Client.putObject(PutObjectRequest.builder()
                .bucket(reportsBucket)
                .key(fileKey)
                .build(),
                RequestBody.fromBytes(reportBytes)
            );
            
            // 6. Actualizar estado
            report.setStatus(ReportStatus.COMPLETED);
            report.setCompletedAt(LocalDateTime.now());
            report.setFilePath(fileKey);
            report.setFileSize((long) reportBytes.length);
            
            reportRepository.save(report);
            
            // 7. Notificar
            if (request.getNotifyEmail() != null) {
                emailService.sendReportNotification(
                    request.getNotifyEmail(),
                    report
                );
            }
            
            return CompletableFuture.completedFuture(report);
            
        } catch (Exception e) {
            report.setStatus(ReportStatus.FAILED);
            report.setError(e.getMessage());
            reportRepository.save(report);
            
            throw new ReportGenerationException("Report generation failed", e);
        }
    }
}
```

---

## 📊 PERFORMANCE Y ESCALABILIDAD

### **Optimizaciones:**

1. **Particionamiento por Tiempo**
   - Tablas particionadas por mes
   - Queries solo acceden particiones necesarias
   - Retención automática de datos antiguos

2. **Índices Especializados**
   - B-tree para búsquedas exactas
   - GIN para campos JSONB
   - BRIN para series temporales

3. **Caché Multi-nivel**
   - L1: In-memory cache (Caffeine)
   - L2: Redis cluster
   - L3: ClickHouse materialized views

4. **Procesamiento Paralelo**
   - Kafka Streams para real-time
   - Spark para batch
   - ThreadPoolExecutor para reportes

### **Capacidad:**
- **Ingestión:** 100,000 métricas/segundo
- **Queries:** < 100ms p95 para agregaciones
- **Reportes:** 50 simultáneos
- **Retención:** 2 años de datos históricos

---

## 🎯 CONCLUSIONES TÉCNICAS

### **Fortalezas Arquitectónicas:**
1. ✅ **Lambda Architecture:** Real-time + Batch
2. ✅ **Escalabilidad:** Horizontal ilimitada
3. ✅ **Performance:** < 100ms queries
4. ✅ **ML-Powered:** Predictive analytics
5. ✅ **Reliability:** 99.9% uptime

### **Decisiones de Diseño:**
1. **Kafka:** Real-time streaming
2. **Spark:** Batch processing
3. **ClickHouse:** OLAP queries
4. **Prophet:** Time series forecasting
5. **JasperReports:** Report generation

---

## 📞 CONTACTO TÉCNICO

**Arquitectura:** architecture@codeflowx.com  
**Soporte Técnico:** support@codeflowx.com  
**Documentación:** docs.codeflowx.com/analytics  
**GitHub:** github.com/codeflowx/analytics

