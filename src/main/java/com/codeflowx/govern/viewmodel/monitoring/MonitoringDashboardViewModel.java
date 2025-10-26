package com.codeflowx.govern.viewmodel.monitoring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.views.monitoring.AlertSummary;
import com.codeflowx.govern.entity.views.monitoring.AnomalyHeatmap;
import com.codeflowx.govern.entity.views.monitoring.MetricsVisualization;
import com.codeflowx.govern.entity.views.monitoring.MonitoringDashboard;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;

/**
 * ViewModel para el Dashboard de Monitoring
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MonitoringDashboardViewModel extends MasterPage implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MonitoringDashboardViewModel.class);

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private BusinessService businessService;
    
    @Autowired
    protected IEntityLocal dao;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;
    
    private MonitoringDashboard monitoringDashboard;
    private List<MetricsVisualization> metricsVisualizations = new ArrayList<>();
    private List<AnomalyHeatmap> anomalyHeatmaps = new ArrayList<>();
    private List<AlertSummary> alertSummaries = new ArrayList<>();
    
    private Long totalMetrics = 0L;
    private Long totalAlerts = 0L;
    private Long criticalAlerts = 0L;
    private Integer systemHealth = 100;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();
        
        log.info("Inicializando MonitoringDashboardViewModel");
        
        loadMonitoringDashboard();
        loadMetricsVisualizations();
        loadAnomalyHeatmaps();
        loadAlertSummaries();
        calculateKPIs();
    }
    
    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("moncreatedat")
                .build();
    }

    

    private void loadMonitoringDashboard() {
        try {
            PageResult<MonitoringDashboard> result = businessService.findAllEntity(
                MonitoringDashboard.class, pageParams, new HashMap<>()
            );
            
            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                monitoringDashboard = result.getContent().get(0);
            }
        } catch (Exception e) {
            log.error("Error al cargar monitoring dashboard", e);
        }
    }
    
    private void loadMetricsVisualizations() {
        try {
            PageResult<MetricsVisualization> result = businessService.findAllEntity(
                MetricsVisualization.class, pageParams, new HashMap<>()
            );
            
            if (result != null && result.getContent() != null) {
                metricsVisualizations = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar metrics visualizations", e);
        }
    }
    
    private void loadAnomalyHeatmaps() {
        try {
            PageResult<AnomalyHeatmap> result = businessService.findAllEntity(
                AnomalyHeatmap.class, pageParams, new HashMap<>()
            );
            
            if (result != null && result.getContent() != null) {
                anomalyHeatmaps = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar anomaly heatmaps", e);
        }
    }
    
    private void loadAlertSummaries() {
        try {
            PageResult<AlertSummary> result = businessService.findAllEntity(
                AlertSummary.class, pageParams, new HashMap<>()
            );
            
            if (result != null && result.getContent() != null) {
                alertSummaries = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar alert summaries", e);
        }
    }
    
    private void calculateKPIs() {
        try {
            if (monitoringDashboard != null) {
                totalMetrics = monitoringDashboard.getTotalItems() != null ? monitoringDashboard.getTotalItems() : 0L;
            }
            
            totalAlerts = alertSummaries.stream()
                .filter(a -> a.getTotalItems() != null)
                .mapToLong(a -> a.getTotalItems())
                .sum();
            
            criticalAlerts = alertSummaries.stream()
                .filter(a -> a.getActiveItems() != null)
                .mapToLong(a -> Long.parseLong(a.getActiveItems()))
                .sum();
            
            if (totalAlerts > 0) {
                systemHealth = (int) Math.max(0, 100 - (criticalAlerts * 10));
            }
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void refreshDashboard() {
        log.info("Refrescando dashboard de monitoring");
        try {
            loadMonitoringDashboard();
            loadMetricsVisualizations();
            loadAnomalyHeatmaps();
            loadAlertSummaries();
            calculateKPIs();
            
            Messagebox.show("Dashboard actualizado correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al refrescar dashboard", e);
        }
    }

    public MonitoringDashboard getMonitoringDashboard() { return monitoringDashboard; }
    public List<MetricsVisualization> getMetricsVisualizations() { return metricsVisualizations; }
    public List<AnomalyHeatmap> getAnomalyHeatmaps() { return anomalyHeatmaps; }
    public List<AlertSummary> getAlertSummaries() { return alertSummaries; }
    public Long getTotalMetrics() { return totalMetrics; }
    public Long getTotalAlerts() { return totalAlerts; }
    public Long getCriticalAlerts() { return criticalAlerts; }
    public Integer getSystemHealth() { return systemHealth; }

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            monitoringDashboard = null;
            if (metricsVisualizations != null) { metricsVisualizations.clear(); metricsVisualizations = null; }
            if (anomalyHeatmaps != null) { anomalyHeatmaps.clear(); anomalyHeatmaps = null; }
            if (alertSummaries != null) { alertSummaries.clear(); alertSummaries = null; }
            pageParams = null;
            businessService = null;
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}


