package com.codeflowx.govern.viewmodel.governance;
import com.codeflowx.framework.zkoss.BaseFront;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import nocode.services.entitys.governance.APSAimsPerformance;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AIMSPerformanceViewModel extends BaseFront<AIMSPerformanceViewModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private List<APSAimsPerformance> metrics;
    private APSAimsPerformance selectedMetric;
    private APSAimsPerformance newMetric;

    @Init
    public void init() {
        loadMetrics();
        newMetric = createDefaultMetric();
    }

    @Command
    @NotifyChange({"metrics", "selectedMetric"})
    public void loadMetrics() {
        metrics = entityManager.createQuery(
                        "FROM APSAimsPerformance m ORDER BY m.apsperiodend DESC NULLS LAST, m.apscreatedat DESC",
                        APSAimsPerformance.class)
                .getResultList();
        if (!metrics.isEmpty()) {
            selectedMetric = metrics.get(0);
        }
    }

    @Command
    @Transactional
    @NotifyChange({"metrics", "newMetric"})
    public void createMetric() {
        if (!validateMetric(newMetric)) {
            return;
        }
        newMetric.setIduuid(UUID.randomUUID().toString());
        newMetric.setApscreatedat(Timestamp.from(Instant.now()));
        calculateVariance(newMetric);
        entityManager.persist(newMetric);
        Clients.showNotification("Métrica registrada correctamente", "info", null, "top_center", 3000);
        loadMetrics();
        newMetric = createDefaultMetric();
    }

    @Command
    @Transactional
    @NotifyChange("metrics")
    public void updateMetric(@BindingParam("metric") APSAimsPerformance metric) {
        if (metric == null) {
            Clients.showNotification("Seleccione una métrica para actualizar", "warning", null, "top_center", 3000);
            return;
        }
        calculateVariance(metric);
        metric.setApsupdatedat(Timestamp.from(Instant.now()));
        entityManager.merge(metric);
        Clients.showNotification("Métrica actualizada", "info", null, "top_center", 3000);
        loadMetrics();
    }

    @Command
    @Transactional
    @NotifyChange("metrics")
    public void deleteMetric(@BindingParam("metric") APSAimsPerformance metric) {
        if (metric == null) {
            Clients.showNotification("Seleccione una métrica para eliminar", "warning", null, "top_center", 3000);
            return;
        }
        entityManager.remove(entityManager.contains(metric) ? metric : entityManager.merge(metric));
        Clients.showNotification("Métrica eliminada", "info", null, "top_center", 3000);
        loadMetrics();
    }

    @Command
    @NotifyChange("metrics")
    public void refresh() {
        loadMetrics();
        Clients.showNotification("Datos refrescados", "info", null, "top_center", 3000);
    }

    public double getOnTrackRatio() {
        if (metrics == null || metrics.isEmpty()) {
            return 0; 
        }
        long onTrack = metrics.stream()
                .filter(m -> "ON_TRACK".equalsIgnoreCase(m.getApsstatus()))
                .count();
        return (double) onTrack * 100 / metrics.size();
    }

    private boolean validateMetric(APSAimsPerformance metric) {
        if (metric == null) {
            Clients.showNotification("Registro inválido", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(metric.getApsmetricname())) {
            Clients.showNotification("El nombre de la métrica es obligatorio", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(metric.getApsmetriccategory())) {
            Clients.showNotification("La categoría es obligatoria", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(metric.getApsstatus())) {
            metric.setApsstatus("ON_TRACK");
        }
        return true;
    }

    private void calculateVariance(APSAimsPerformance metric) {
        BigDecimal value = metric.getApsmetricvalue();
        BigDecimal target = metric.getApstargetvalue();
        if (value != null && target != null) {
            metric.setApsvariance(value.subtract(target));
        }
    }

    private APSAimsPerformance createDefaultMetric() {
        APSAimsPerformance metric = new APSAimsPerformance();
        metric.setApsstatus("ON_TRACK");
        metric.setApsmetriccategory("PERFORMANCE");
        metric.setApscreatedat(Timestamp.from(Instant.now()));
        return metric;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
