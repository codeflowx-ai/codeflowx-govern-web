package com.codeflowx.govern.viewmodel.analytics;

import java.util.List;

import org.springframework.core.env.Environment;
import org.suinsit.nocode.web.MasterBeanUI;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.codeflowx.govern.entity.views.analytics.AnalyticsOverview;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Dashboard de Vista General de Analytics
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AnalyticsOverviewViewModel extends MasterBeanUI {
    
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    public Environment environment;
    
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
        loadOverviewData();
    }
    
    @Command
    @NotifyChange("*")
    public void loadOverviewData() {
        try {
            log.debug("Cargando vista general de analytics");
            List<AnalyticsOverview> overviews = businessService.findAllView(AnalyticsOverview.class);
            
            if (overviews != null && !overviews.isEmpty()) {
                AnalyticsOverview overview = overviews.get(0);
                
                // Mapear datos
                this.totalItems = overview.getTotalItems() != null ? overview.getTotalItems() : 0L;
                this.activeItems = overview.getActiveItems() != null ? overview.getActiveItems() : 0L;
                this.deployedItems = overview.getDeployedItems() != null ? overview.getDeployedItems() : 0L;
                this.trainingItems = overview.getTrainingItems() != null ? overview.getTrainingItems() : 0L;
                this.offlineItems = overview.getOfflineItems() != null ? overview.getOfflineItems() : 0L;
                this.avgScore = overview.getAvgScore() != null ? overview.getAvgScore() : java.math.BigDecimal.ZERO;
                
                log.info("Vista general cargada - Total: {}, Activos: {}, Score: {}", 
                         totalItems, activeItems, avgScore);
            }
        } catch (Exception e) {
            log.error("Error cargando vista general de analytics", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void refresh() {
        log.debug("Refrescando vista general de analytics");
        loadOverviewData();
    }
    
    // Métodos auxiliares
    public String getActivePercentage() {
        if (totalItems == null || totalItems == 0 || activeItems == null) return "0";
        return String.format("%.1f", (activeItems * 100.0 / totalItems));
    }
    
    public String getDeployedPercentage() {
        if (totalItems == null || totalItems == 0 || deployedItems == null) return "0";
        return String.format("%.1f", (deployedItems * 100.0 / totalItems));
    }
    
    public String getScoreLevel() {
        if (avgScore == null) return "Desconocido";
        double score = avgScore.doubleValue();
        if (score >= 90) return "Excelente";
        if (score >= 70) return "Bueno";
        if (score >= 50) return "Aceptable";
        return "Bajo";
    }
    
    public String getScoreColor() {
        String level = getScoreLevel();
        switch (level) {
            case "Excelente": return "success";
            case "Bueno": return "primary";
            case "Aceptable": return "warning";
            case "Bajo": return "danger";
            default: return "secondary";
        }
    }
}

