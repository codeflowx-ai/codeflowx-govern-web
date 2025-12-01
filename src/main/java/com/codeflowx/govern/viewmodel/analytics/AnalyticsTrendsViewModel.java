package com.codeflowx.govern.viewmodel.analytics;
import com.codeflowx.framework.zkoss.BaseFront;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import com.codeflowx.framework.zkoss.BaseFront;
import codeflowx.nocode.persist.*;
import com.codeflowx.govern.entity.views.analytics.AnalyticsTrends;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.service.analytics.AnalyticsTrendsService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Dashboard de Tendencias de Analytics
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AnalyticsTrendsViewModel extends BaseFront<AnalyticsTrendsViewModel>{

    @WireVariable
    private ModelService modelService;
    @WireVariable
    private AnalyticsTrendsService analyticsTrendsService;
    @Autowired
    protected IEntityLocal dao;
    @WireVariable
    public Environment environment;
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    @WireVariable("ctxBean")
    protected Context ctxBean;

    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }


    private Long totalItems = 0L;
    private Long activeItems = 0L;
    private Long deployedItems = 0L;
    private Long trainingItems = 0L;
    private Long offlineItems = 0L;
    private java.math.BigDecimal avgScore = java.math.BigDecimal.ZERO;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        doAfterCompose(view);
        initDao();
        loadTrendsData();
    }

    @Command
    @NotifyChange("*")
    public void loadTrendsData() {
        try {
            log.debug("Cargando tendencias de analytics");
            List<AnalyticsTrends> trends = analyticsTrendsService.findAll();

            if (trends != null && !trends.isEmpty()) {
                AnalyticsTrends trend = trends.get(0);

                // Mapear datos
                this.totalItems = trend.getTotalItems() != null ? trend.getTotalItems() : 0L;
                this.activeItems = trend.getActiveItems() != null ? trend.getActiveItems() : 0L;
                this.deployedItems = trend.getDeployedItems() != null ? trend.getDeployedItems() : 0L;
                this.trainingItems = trend.getTrainingItems() != null ? trend.getTrainingItems() : 0L;
                this.offlineItems = trend.getOfflineItems() != null ? trend.getOfflineItems() : 0L;
                this.avgScore = trend.getAvgScore() != null ? trend.getAvgScore() : java.math.BigDecimal.ZERO;

                log.info("Tendencias cargadas - Total: {}, Activos: {}, Score: {}",
                         totalItems, activeItems, avgScore);
            }
        } catch (GovernanceServiceException e) {
            log.error("Error cargando tendencias de analytics", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void refresh() {
        log.debug("Refrescando tendencias de analytics");
        loadTrendsData();
    }

    // Métodos auxiliares
    public String getTrendDirection() {
        if (activeItems == null || deployedItems == null) return "unknown";

        if (activeItems > deployedItems) return "up";
        if (activeItems < deployedItems) return "down";
        return "stable";
    }

    public String getTrendIcon() {
        String direction = getTrendDirection();
        switch (direction) {
            case "up": return "bi-arrow-up-circle";
            case "down": return "bi-arrow-down-circle";
            case "stable": return "bi-dash-circle";
            default: return "bi-question-circle";
        }
    }

    public String getTrendColor() {
        String direction = getTrendDirection();
        switch (direction) {
            case "up": return "success";
            case "down": return "danger";
            case "stable": return "info";
            default: return "secondary";
        }
    }

    public String getActivityRate() {
        if (totalItems == null || totalItems == 0 || activeItems == null) return "0";
        return String.format("%.1f", (activeItems * 100.0 / totalItems));
    }

    public String getScoreLevel() {
        if (avgScore == null) return "Desconocido";
        double score = avgScore.doubleValue();
        if (score >= 90) return "Excelente";
        if (score >= 70) return "Bueno";
        if (score >= 50) return "Aceptable";
        return "Bajo";
    }


    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
