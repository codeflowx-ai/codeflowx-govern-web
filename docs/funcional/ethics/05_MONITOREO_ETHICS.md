# ⚖️ ETHICS - MONITOREO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de monitoreo del módulo Ethics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Ethics** incluye monitoreo completo de:
- **Ethical Compliance:** Cumplimiento ético continuo
- **Bias Monitoring:** Monitoreo de sesgos en tiempo real
- **Fairness Tracking:** Seguimiento de equidad
- **Impact Monitoring:** Monitoreo de impacto social
- **Review Status:** Estado de revisiones éticas

---

## 📊 MÉTRICAS PRINCIPALES

### **1. MÉTRICAS DE COMPLIANCE ÉTICO**

#### **Ethical Compliance Score**
```sql
-- Vista para score de compliance ético
CREATE OR REPLACE VIEW eth_v_ethical_compliance AS
SELECT 
    er.eth_entity_type,
    er.eth_entity_id,
    AVG(er.eth_overall_score) AS avg_ethical_score,
    MIN(er.eth_overall_score) AS min_ethical_score,
    MAX(er.eth_overall_score) AS max_ethical_score,
    COUNT(*) AS total_reviews,
    COUNT(*) FILTER (WHERE er.eth_recommendation = 'APPROVED') AS approved_reviews,
    COUNT(*) FILTER (WHERE er.eth_recommendation = 'REJECTED') AS rejected_reviews,
    (COUNT(*) FILTER (WHERE er.eth_recommendation = 'APPROVED')::DECIMAL / 
     COUNT(*)) * 100 AS approval_rate,
    MAX(er.eth_completed_at) AS last_review_date,
    CASE 
        WHEN AVG(er.eth_overall_score) >= 8.0 THEN 'EXCELLENT'
        WHEN AVG(er.eth_overall_score) >= 7.0 THEN 'GOOD'
        WHEN AVG(er.eth_overall_score) >= 6.0 THEN 'ACCEPTABLE'
        ELSE 'NEEDS_IMPROVEMENT'
    END AS ethical_rating
FROM eth_ethical_reviews er
WHERE er.eth_status = 'COMPLETED'
AND er.eth_completed_at >= CURRENT_TIMESTAMP - INTERVAL '12 months'
GROUP BY er.eth_entity_type, er.eth_entity_id;
```

#### **Review Turnaround Time**
```sql
-- Vista para tiempo de revisión ética
CREATE OR REPLACE VIEW eth_v_review_turnaround AS
SELECT 
    er.eth_review_type,
    COUNT(*) AS total_reviews,
    AVG(EXTRACT(EPOCH FROM (er.eth_completed_at - er.eth_requested_at)) / 86400) AS avg_days,
    PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY EXTRACT(EPOCH FROM (er.eth_completed_at - er.eth_requested_at)) / 86400) AS median_days,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY EXTRACT(EPOCH FROM (er.eth_completed_at - er.eth_requested_at)) / 86400) AS p95_days,
    MAX(EXTRACT(EPOCH FROM (er.eth_completed_at - er.eth_requested_at)) / 86400) AS max_days,
    DATE_TRUNC('month', er.eth_completed_at) AS month
FROM eth_ethical_reviews er
WHERE er.eth_status = 'COMPLETED'
AND er.eth_completed_at >= CURRENT_TIMESTAMP - INTERVAL '12 months'
GROUP BY er.eth_review_type, DATE_TRUNC('month', er.eth_completed_at);
```

---

### **2. MÉTRICAS DE BIAS**

#### **Bias Detection Trends**
```sql
-- Vista para tendencias de detección de sesgos
CREATE OR REPLACE VIEW eth_v_bias_trends AS
SELECT 
    bd.eth_entity_type,
    bd.eth_entity_id,
    bd.eth_sensitive_attribute,
    bd.eth_bias_metric,
    AVG(bd.eth_bias_value) AS avg_bias_value,
    MIN(bd.eth_bias_value) AS min_bias_value,
    MAX(bd.eth_bias_value) AS max_bias_value,
    STDDEV(bd.eth_bias_value) AS std_dev_bias,
    COUNT(*) AS detection_count,
    COUNT(*) FILTER (WHERE bd.eth_bias_passed = false) AS failed_count,
    DATE_TRUNC('week', bd.eth_detected_at) AS week,
    -- Tendencia
    CASE 
        WHEN AVG(bd.eth_bias_value) < LAG(AVG(bd.eth_bias_value)) 
             OVER (PARTITION BY bd.eth_entity_id, bd.eth_sensitive_attribute 
                   ORDER BY DATE_TRUNC('week', bd.eth_detected_at))
        THEN 'IMPROVING'
        WHEN AVG(bd.eth_bias_value) > LAG(AVG(bd.eth_bias_value)) 
             OVER (PARTITION BY bd.eth_entity_id, bd.eth_sensitive_attribute 
                   ORDER BY DATE_TRUNC('week', bd.eth_detected_at))
        THEN 'DEGRADING'
        ELSE 'STABLE'
    END AS trend_direction
FROM eth_bias_detections bd
WHERE bd.eth_detected_at >= CURRENT_TIMESTAMP - INTERVAL '90 days'
GROUP BY bd.eth_entity_type, bd.eth_entity_id, bd.eth_sensitive_attribute, 
         bd.eth_bias_metric, DATE_TRUNC('week', bd.eth_detected_at);
```

#### **Bias Severity Distribution**
```sql
-- Vista para distribución de severidad de sesgos
CREATE OR REPLACE VIEW eth_v_bias_severity AS
SELECT 
    bd.eth_entity_type,
    COUNT(*) AS total_detections,
    COUNT(*) FILTER (WHERE bd.eth_bias_level = 'LOW') AS low_bias,
    COUNT(*) FILTER (WHERE bd.eth_bias_level = 'MODERATE') AS moderate_bias,
    COUNT(*) FILTER (WHERE bd.eth_bias_level = 'HIGH') AS high_bias,
    (COUNT(*) FILTER (WHERE bd.eth_bias_level = 'HIGH')::DECIMAL / 
     COUNT(*)) * 100 AS high_bias_percent,
    DATE_TRUNC('month', bd.eth_detected_at) AS month
FROM eth_bias_detections bd
WHERE bd.eth_detected_at >= CURRENT_TIMESTAMP - INTERVAL '12 months'
GROUP BY bd.eth_entity_type, DATE_TRUNC('month', bd.eth_detected_at);
```

---

### **3. MÉTRICAS DE FAIRNESS**

#### **Fairness Metrics Dashboard**
```sql
-- Vista para dashboard de fairness
CREATE OR REPLACE VIEW eth_v_fairness_dashboard AS
SELECT 
    fa.eth_entity_type,
    fa.eth_entity_id,
    e.entity_name,
    fa.eth_overall_fairness_score,
    fa.eth_fairness_level,
    jsonb_object_agg(
        fm.eth_metric_name,
        jsonb_build_object(
            'score', fm.eth_metric_value,
            'threshold', fm.eth_threshold,
            'passed', fm.eth_passed
        )
    ) AS fairness_metrics,
    fa.eth_assessed_at,
    -- Tendencia vs evaluación anterior
    CASE 
        WHEN fa.eth_overall_fairness_score > LAG(fa.eth_overall_fairness_score) 
             OVER (PARTITION BY fa.eth_entity_id ORDER BY fa.eth_assessed_at)
        THEN 'IMPROVING'
        WHEN fa.eth_overall_fairness_score < LAG(fa.eth_overall_fairness_score) 
             OVER (PARTITION BY fa.eth_entity_id ORDER BY fa.eth_assessed_at)
        THEN 'DEGRADING'
        ELSE 'STABLE'
    END AS trend
FROM eth_fairness_assessments fa
JOIN eth_fairness_metrics fm ON fa.eth_id = fm.eth_assessment_id
JOIN (
    SELECT eth_entity_id, MAX(name) as entity_name
    FROM models_or_agents_table
    GROUP BY eth_entity_id
) e ON fa.eth_entity_id = e.eth_entity_id
WHERE fa.eth_assessed_at >= CURRENT_TIMESTAMP - INTERVAL '90 days'
GROUP BY fa.eth_entity_type, fa.eth_entity_id, e.entity_name, 
         fa.eth_overall_fairness_score, fa.eth_fairness_level, fa.eth_assessed_at;
```

---

### **4. MÉTRICAS DE IMPACTO**

#### **Impact Analysis Tracking**
```sql
-- Vista para tracking de análisis de impacto
CREATE OR REPLACE VIEW eth_v_impact_tracking AS
SELECT 
    ia.eth_entity_type,
    ia.eth_entity_id,
    ia.eth_overall_impact_score,
    jsonb_object_agg(
        id.eth_dimension_name,
        jsonb_build_object(
            'score', id.eth_score,
            'positive_impacts', id.eth_positive_impacts,
            'negative_impacts', id.eth_negative_impacts
        )
    ) AS impact_dimensions,
    COUNT(ra.eth_id) AS recommended_actions_count,
    COUNT(ra.eth_id) FILTER (WHERE ra.eth_status = 'COMPLETED') AS completed_actions,
    (COUNT(ra.eth_id) FILTER (WHERE ra.eth_status = 'COMPLETED')::DECIMAL / 
     NULLIF(COUNT(ra.eth_id), 0)) * 100 AS action_completion_rate,
    ia.eth_analyzed_at
FROM eth_impact_analyses ia
JOIN eth_impact_dimensions id ON ia.eth_id = id.eth_analysis_id
LEFT JOIN eth_recommended_actions ra ON ia.eth_id = ra.eth_analysis_id
GROUP BY ia.eth_entity_type, ia.eth_entity_id, ia.eth_overall_impact_score, ia.eth_analyzed_at;
```

---

## 🚨 ALERTAS Y NOTIFICACIONES

### **Sistema de Alertas Éticas**

```java
@Service
public class EthicsAlertService {
    
    @Autowired
    private EthicalReviewService ethicalReviewService;
    
    @Autowired
    private BiasDetectionService biasDetectionService;
    
    @Autowired
    private AlertService alertService;
    
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void checkEthicalAlerts() {
        // Check pending reviews
        checkPendingReviews();
        
        // Check bias thresholds
        checkBiasThresholds();
        
        // Check fairness violations
        checkFairnessViolations();
        
        // Check expired reviews
        checkExpiredReviews();
    }
    
    private void checkPendingReviews() {
        List<EthicalReview> overdueReviews = ethicalReviewService
            .getPendingReviews()
            .stream()
            .filter(r -> r.getRequestedAt().isBefore(
                LocalDateTime.now().minusDays(7)
            ))
            .collect(Collectors.toList());
        
        for (EthicalReview review : overdueReviews) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.ETHICAL_REVIEW_OVERDUE)
                .severity(AlertSeverity.MEDIUM)
                .title("Ethical Review Overdue")
                .message(String.format(
                    "Ethical review for %s %d pending for %d days",
                    review.getEntityType(),
                    review.getEntityId(),
                    ChronoUnit.DAYS.between(review.getRequestedAt(), LocalDateTime.now())
                ))
                .build());
        }
    }
    
    private void checkBiasThresholds() {
        List<Model> activeModels = modelService.getActiveModels();
        
        for (Model model : activeModels) {
            BiasDetectionResult latestBias = biasDetectionService
                .getLatestBiasAnalysis(model.getId());
            
            if (latestBias != null && latestBias.getBiasLevel() == BiasLevel.HIGH) {
                alertService.sendAlert(Alert.builder()
                    .type(AlertType.HIGH_BIAS_DETECTED)
                    .severity(AlertSeverity.HIGH)
                    .title("High Bias Detected")
                    .message(String.format(
                        "Model %s shows high bias level (score: %.2f)",
                        model.getName(),
                        latestBias.getOverallBiasScore()
                    ))
                    .modelId(model.getId())
                    .build());
            }
        }
    }
    
    private void checkFairnessViolations() {
        List<FairnessAssessment> recentAssessments = fairnessService
            .getRecentAssessments(Duration.ofHours(24));
        
        for (FairnessAssessment assessment : recentAssessments) {
            if (assessment.getFairnessLevel() == FairnessLevel.POOR) {
                alertService.sendAlert(Alert.builder()
                    .type(AlertType.FAIRNESS_VIOLATION)
                    .severity(AlertSeverity.CRITICAL)
                    .title("Fairness Violation Detected")
                    .message(String.format(
                        "Entity %d shows poor fairness (score: %.2f)",
                        assessment.getEntityId(),
                        assessment.getOverallFairnessScore()
                    ))
                    .build());
            }
        }
    }
    
    private void checkExpiredReviews() {
        // Reviews más antiguas de 90 días necesitan renovación
        List<Model> modelsNeedingReview = modelService.getActiveModels()
            .stream()
            .filter(m -> {
                EthicalReview latest = ethicalReviewService
                    .getLatestReview(EntityType.MODEL, m.getId());
                return latest == null || 
                       latest.getCompletedAt().isBefore(
                           LocalDateTime.now().minusDays(90)
                       );
            })
            .collect(Collectors.toList());
        
        for (Model model : modelsNeedingReview) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.ETHICAL_REVIEW_EXPIRED)
                .severity(AlertSeverity.MEDIUM)
                .title("Ethical Review Needed")
                .message(String.format(
                    "Model %s requires ethical review update",
                    model.getName()
                ))
                .modelId(model.getId())
                .build());
        }
    }
}
```

### **Tipos de Alertas:**

| Tipo | Severidad | Descripción |
|------|-----------|-------------|
| **ETHICAL_REVIEW_OVERDUE** | MEDIUM | Revisión ética pendiente >7 días |
| **ETHICAL_REVIEW_EXPIRED** | MEDIUM | Revisión ética >90 días |
| **HIGH_BIAS_DETECTED** | HIGH | Sesgo alto detectado |
| **BIAS_DRIFT** | HIGH | Drift en métricas de sesgo |
| **FAIRNESS_VIOLATION** | CRITICAL | Violación de equidad |
| **FAIRNESS_DEGRADATION** | HIGH | Degradación de fairness |
| **IMPACT_CONCERN** | MEDIUM | Preocupación de impacto social |
| **TRANSPARENCY_LOW** | MEDIUM | Score de transparencia bajo |
| **ETHICAL_COMPLIANCE_FAIL** | CRITICAL | Fallo en compliance ético |

---

## 📊 DASHBOARDS

### **Ethics Monitoring Dashboard**

**Pantallas ZUL:**
1. **`ethics-overview.zul`** - Vista general de ética
2. **`ethics-reviews-dashboard.zul`** - Dashboard de revisiones
3. **`bias-detection-overview.zul`** - Monitoreo de sesgos
4. **`fairness-metrics-overview.zul`** - Métricas de equidad
5. **`impact-analysis-overview.zul`** - Análisis de impacto
6. **`transparency-scorecard-overview.zul`** - Scorecard de transparencia

---

## 📈 REPORTES AUTOMÁTICOS

### **Reportes Programados**

```java
@Service
public class EthicsReportingService {
    
    @Scheduled(cron = "0 0 9 * * MON") // Lunes 9AM
    public void generateWeeklyEthicsReport() {
        LocalDate lastWeekStart = LocalDate.now().minusWeeks(1);
        LocalDate lastWeekEnd = LocalDate.now();
        
        // Reporte semanal de ética
        WeeklyEthicsReport report = WeeklyEthicsReport.builder()
            .period(new DateRange(lastWeekStart, lastWeekEnd))
            .reviewsCompleted(countCompletedReviews(lastWeekStart, lastWeekEnd))
            .newBiasDetections(countNewBiasDetections(lastWeekStart, lastWeekEnd))
            .fairnessViolations(getFairnessViolations(lastWeekStart, lastWeekEnd))
            .criticalIssues(getCriticalEthicalIssues())
            .recommendations(generateWeeklyRecommendations())
            .build();
        
        // Enviar a stakeholders
        emailService.sendReport(
            "ethics-committee@company.com",
            "Weekly Ethics Report",
            report
        );
    }
    
    @Scheduled(cron = "0 0 8 1 * *") // Primer día del mes, 8AM
    public void generateMonthlyEthicsReport() {
        LocalDate lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate lastMonthEnd = LocalDate.now().withDayOfMonth(1).minusDays(1);
        
        // Reporte mensual comprehensivo
        MonthlyEthicsReport report = MonthlyEthicsReport.builder()
            .period(new DateRange(lastMonthStart, lastMonthEnd))
            .ethicalComplianceSummary(generateComplianceSummary())
            .biasTrends(analyzeBiasTrends(lastMonthStart, lastMonthEnd))
            .fairnessMetrics(aggregateFairnessMetrics(lastMonthStart, lastMonthEnd))
            .impactAnalysisSummary(summarizeImpactAnalyses())
            .keyFindings(extractKeyFindings())
            .actionItems(generateActionItems())
            .build();
        
        // Enviar a dirección
        emailService.sendReport(
            "executives@company.com",
            "Monthly Ethics Report",
            report
        );
    }
}
```

---

## 🔍 AUDITORÍA ÉTICA

### **Audit Trail Completo**

```java
@Service
public class EthicsAuditService {
    
    /**
     * Registra todas las operaciones éticas para auditoría
     */
    @EventListener
    public void onEthicalEvent(EthicalEvent event) {
        EthicalAuditEntry entry = EthicalAuditEntry.builder()
            .eventType(event.getType())
            .entityType(event.getEntityType())
            .entityId(event.getEntityId())
            .userId(event.getUserId())
            .action(event.getAction())
            .beforeState(event.getBeforeState())
            .afterState(event.getAfterState())
            .justification(event.getJustification())
            .timestamp(LocalDateTime.now())
            .build();
        
        auditRepository.save(entry);
    }
    
    /**
     * Genera reporte de auditoría ética
     */
    public EthicalAuditReport generateAuditReport(
        LocalDate from,
        LocalDate to,
        AuditScope scope
    ) {
        List<EthicalAuditEntry> entries = auditRepository
            .findByDateRange(from, to);
        
        // Filtrar por scope
        if (scope.getEntityType() != null) {
            entries = entries.stream()
                .filter(e -> e.getEntityType() == scope.getEntityType())
                .collect(Collectors.toList());
        }
        
        // Agrupar por tipo de acción
        Map<String, Long> actionCounts = entries.stream()
            .collect(Collectors.groupingBy(
                EthicalAuditEntry::getAction,
                Collectors.counting()
            ));
        
        // Identificar cambios significativos
        List<SignificantChange> significantChanges = identifySignificantChanges(entries);
        
        return EthicalAuditReport.builder()
            .period(new DateRange(from, to))
            .totalActions(entries.size())
            .actionBreakdown(actionCounts)
            .significantChanges(significantChanges)
            .complianceStatus(assessComplianceStatus(entries))
            .generatedAt(LocalDateTime.now())
            .build();
    }
}
```

---

## 📊 INTEGRACIÓN CON PROMETHEUS

### **Métricas Expuestas**

```java
@Component
public class EthicsMetricsExporter {
    
    private final Gauge ethicalScore;
    private final Gauge biasScore;
    private final Gauge fairnessScore;
    private final Counter reviewsCompleted;
    
    public EthicsMetricsExporter(MeterRegistry registry) {
        this.ethicalScore = Gauge.builder("ethics_overall_score")
            .description("Overall ethical score")
            .tags("entity_type", "entity_id")
            .register(registry);
        
        this.biasScore = Gauge.builder("ethics_bias_score")
            .description("Bias score (higher is better)")
            .tags("entity_type", "entity_id", "attribute")
            .register(registry);
        
        this.fairnessScore = Gauge.builder("ethics_fairness_score")
            .description("Fairness score")
            .tags("entity_type", "entity_id")
            .register(registry);
        
        this.reviewsCompleted = Counter.builder("ethics_reviews_completed_total")
            .description("Total ethical reviews completed")
            .tags("review_type", "recommendation")
            .register(registry);
    }
}
```

---

## 🎯 CONCLUSIÓN

El módulo **Ethics** incluye monitoreo completo con:

- ✅ **Compliance Ético** - Score y rating automático
- ✅ **Monitoreo de Sesgos** - Tendencias y severidad
- ✅ **Tracking de Fairness** - Métricas continuas
- ✅ **Monitoreo de Impacto** - Seguimiento de acciones
- ✅ **Alertas Automáticas** - 9 tipos de alertas
- ✅ **Dashboards** - 6 dashboards especializados
- ✅ **Reportes Programados** - Semanales y mensuales
- ✅ **Auditoría Completa** - Tracking de cambios
- ✅ **Integración Prometheus** - Métricas exportadas

**Monitoreo end-to-end para garantía ética continua de sistemas AI.**

