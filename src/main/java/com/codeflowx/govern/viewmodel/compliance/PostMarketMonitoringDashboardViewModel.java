package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.codeflowx.govern.business.compliance.AlertThresholdService;
import com.codeflowx.govern.business.compliance.PostMarketMonitoringDashboardService;
import com.codeflowx.govern.business.compliance.PostMarketMonitoringService;
import com.codeflowx.govern.business.exception.BussinessException;
import com.codeflowx.govern.entity.compliance.AlertThreshold;
import com.codeflowx.govern.entity.compliance.Incident;
import com.codeflowx.govern.entity.compliance.PostMarketMonitoring;
import com.codeflowx.govern.entity.compliance.PostMarketSurveillanceReport;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para Dashboard de Post-Market Monitoring según EU AI Act Art. 72.
 *
 * <p>Funcionalidad:</p>
 * <ul>
 * <li>KPIs en tiempo real (métricas de performance, degradación detectada)</li>
 * <li>Gráficos de tendencias (latencia, throughput, error rate)</li>
 * <li>Tabla de alertas activas</li>
 * <li>Lista de incidentes recientes</li>
 * <li>Filtros por proyecto, fecha, tipo de métrica</li>
 * </ul>
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PostMarketMonitoringDashboardViewModel extends BaseFront<PostMarketMonitoringDashboardViewModel> {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private PostMarketMonitoringDashboardService dashboardService;

    @WireVariable
    private PostMarketMonitoringService pmmService;

    @WireVariable
    private AlertThresholdService alertThresholdService;

    // Datos del dashboard
    private Map<String, Object> dashboardMetrics = new HashMap<>();
    private List<AlertThreshold> activeAlerts = new ArrayList<>();
    private List<Incident> recentIncidents = new ArrayList<>();
    private List<PostMarketMonitoring> activePlans = new ArrayList<>();
    private List<PostMarketSurveillanceReport> recentReports = new ArrayList<>();

    // Filtros
    private Long selectedProjectId;
    private String selectedMetricType;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        loadDashboard();
    }

    @Command
    @NotifyChange({ "dashboardMetrics", "activeAlerts", "recentIncidents", "activePlans", "recentReports" })
    public void loadDashboard() {
        try {
            // Cargar métricas consolidadas
            dashboardMetrics = dashboardService.getDashboardMetrics(selectedProjectId);

            // Cargar alertas activas
            activeAlerts = dashboardService.getActiveAlerts(selectedProjectId);

            // Cargar incidentes recientes (últimos 10)
            recentIncidents = dashboardService.getRecentIncidents(selectedProjectId, 10);

            // Cargar planes activos
            activePlans = dashboardService.getActivePlans(selectedProjectId);

            // Cargar informes recientes (últimos 5)
            recentReports = dashboardService.getRecentReports(selectedProjectId, 5);

            log.debug("Dashboard loaded: {} metrics, {} alerts, {} incidents, {} plans, {} reports",
                    dashboardMetrics.size(), activeAlerts.size(), recentIncidents.size(), activePlans.size(),
                    recentReports.size());

        } catch (BussinessException e) {
            log.error("Error loading dashboard", e);
            org.zkoss.zk.ui.util.Clients.showNotification("Error cargando dashboard: " + e.getMessage(), "error", null,
                    null, 5000);
        }
    }

    @Command
    @NotifyChange({ "dashboardMetrics", "activeAlerts", "recentIncidents", "activePlans", "recentReports" })
    public void applyFilters() {
        loadDashboard();
    }

    @Command
    @NotifyChange({ "dashboardMetrics", "activeAlerts", "recentIncidents", "activePlans", "recentReports" })
    public void clearFilters() {
        selectedProjectId = null;
        selectedMetricType = null;
        loadDashboard();
    }

    // Getters para KPIs
    public Long getActivePlansCount() {
        return dashboardMetrics != null ? (Long) dashboardMetrics.getOrDefault("activePlans", 0L) : 0L;
    }

    public Long getRecentIncidentsCount() {
        return dashboardMetrics != null ? (Long) dashboardMetrics.getOrDefault("recentIncidents", 0L) : 0L;
    }

    public Long getActiveAlertsCount() {
        return dashboardMetrics != null ? (Long) dashboardMetrics.getOrDefault("activeAlerts", 0L) : 0L;
    }

    public Long getRecentReportsCount() {
        return dashboardMetrics != null ? (Long) dashboardMetrics.getOrDefault("recentReports", 0L) : 0L;
    }
}
