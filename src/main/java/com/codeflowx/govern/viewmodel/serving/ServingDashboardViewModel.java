package com.codeflowx.govern.viewmodel.serving;
import com.codeflowx.framework.zkoss.BaseFront;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.enartframework.annotation.context.Autowired;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.codeflowx.govern.entity.views.serving.DeploymentStatus;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.views.serving.EndpointAnalytics;
import com.codeflowx.govern.entity.views.serving.ServingCostBreakdown;
import com.codeflowx.govern.entity.views.serving.ServingErrorAnalysis;
import com.codeflowx.govern.entity.views.serving.ServingPerformanceDashboard;
import com.codeflowx.govern.entity.views.serving.ServingSlaCompliance;
import com.codeflowx.govern.service.serving.ServingPerformanceDashboardService;
import com.codeflowx.govern.service.serving.DeploymentStatusService;
import com.codeflowx.govern.service.serving.EndpointAnalyticsService;
import com.codeflowx.govern.service.serving.ServingSlaComplianceService;
import com.codeflowx.govern.service.serving.ServingErrorAnalysisService;
import com.codeflowx.govern.service.serving.ServingCostBreakdownService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;

/**
 * ViewModel para el Dashboard de Serving
 * Gestiona visualización de endpoints, performance, SLA y costos
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ServingDashboardViewModel extends BaseFront<ServingDashboardViewModel>{

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ServingDashboardViewModel.class);

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private ModelService modelService;

    @WireVariable
    private ServingPerformanceDashboardService servingPerformanceDashboardService;

    @WireVariable
    private DeploymentStatusService deploymentStatusService;

    @WireVariable
    private EndpointAnalyticsService endpointAnalyticsService;

    @WireVariable
    private ServingSlaComplianceService servingSlaComplianceService;

    @WireVariable
    private ServingErrorAnalysisService servingErrorAnalysisService;

    @WireVariable
    private ServingCostBreakdownService servingCostBreakdownService;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;


    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // Los servicios se inyectan automáticamente mediante @WireVariable
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;

    // ========== Datos del Dashboard ==========
    private ServingPerformanceDashboard performanceDashboard;
    private List<DeploymentStatus> deploymentStatuses = new ArrayList<>();
    private List<EndpointAnalytics> endpointAnalytics = new ArrayList<>();
    private List<ServingSlaCompliance> slaCompliances = new ArrayList<>();
    private List<ServingErrorAnalysis> errorAnalyses = new ArrayList<>();
    private List<ServingCostBreakdown> costBreakdowns = new ArrayList<>();

    // ========== KPIs ==========
    private Long totalEndpoints = 0L;
    private Long activeEndpoints = 0L;
    private Long totalRequests = 0L;
    private BigDecimal avgLatency = BigDecimal.ZERO;
    private BigDecimal errorRate = BigDecimal.ZERO;
    private Integer slaCompliance = 100;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();

        log.info("Inicializando ServingDashboardViewModel");

        loadPerformanceDashboard();
        loadDeploymentStatuses();
        loadEndpointAnalytics();
        loadSlaCompliances();
        loadErrorAnalyses();
        loadCostBreakdowns();
        calculateKPIs();
    }

    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("srvcreatedat")
                .build();
    }



    // ========== Carga de Datos ==========

    private void loadPerformanceDashboard() {
        try {
            log.debug("Cargando dashboard de performance");

            PageResult<ServingPerformanceDashboard> result = servingPerformanceDashboardService.findAll(
                pageParams, new Criterias()
            );

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                performanceDashboard = result.getContent().get(0);
                log.info("Dashboard de performance cargado correctamente");
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar dashboard de performance", e);
        }
    }

    private void loadDeploymentStatuses() {
        try {
            log.debug("Cargando estados de deployment");

            PageResult<DeploymentStatus> result = deploymentStatusService.findAll(
                pageParams, new Criterias()
            );

            if (result != null && result.getContent() != null) {
                deploymentStatuses = result.getContent();
                log.info("Cargados {} estados de deployment", deploymentStatuses.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar estados de deployment", e);
        }
    }

    private void loadEndpointAnalytics() {
        try {
            log.debug("Cargando analytics de endpoints");

            PageResult<EndpointAnalytics> result = endpointAnalyticsService.findAll(
                pageParams, new Criterias()
            );

            if (result != null && result.getContent() != null) {
                endpointAnalytics = result.getContent();
                log.info("Cargados {} analytics de endpoints", endpointAnalytics.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar analytics de endpoints", e);
        }
    }

    private void loadSlaCompliances() {
        try {
            log.debug("Cargando SLA compliances");

            PageResult<ServingSlaCompliance> result = servingSlaComplianceService.findAll(
                pageParams, new Criterias()
            );

            if (result != null && result.getContent() != null) {
                slaCompliances = result.getContent();
                log.info("Cargados {} SLA compliances", slaCompliances.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar SLA compliances", e);
        }
    }

    private void loadErrorAnalyses() {
        try {
            log.debug("Cargando análisis de errores");

            PageResult<ServingErrorAnalysis> result = servingErrorAnalysisService.findAll(
                pageParams, new Criterias()
            );

            if (result != null && result.getContent() != null) {
                errorAnalyses = result.getContent();
                log.info("Cargados {} análisis de errores", errorAnalyses.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar análisis de errores", e);
        }
    }

    private void loadCostBreakdowns() {
        try {
            log.debug("Cargando breakdowns de costos");

            PageResult<ServingCostBreakdown> result = servingCostBreakdownService.findAll(
                pageParams, new Criterias()
            );

            if (result != null && result.getContent() != null) {
                costBreakdowns = result.getContent();
                log.info("Cargados {} breakdowns de costos", costBreakdowns.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar breakdowns de costos", e);
        }
    }

    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs de serving");

            if (performanceDashboard != null) {
                totalEndpoints = performanceDashboard.getTotalItems() != null ? performanceDashboard.getTotalItems() : 0L;
                // activeItems es String, no Long - parsear si es necesario
                activeEndpoints = performanceDashboard.getActiveItems() != null ?
                    parseLong(performanceDashboard.getActiveItems()) : 0L;
            }

            totalRequests = endpointAnalytics.stream()
                .filter(e -> e.getTotalItems() != null)
                .mapToLong(e -> e.getTotalItems())
                .sum();

            if (!endpointAnalytics.isEmpty()) {
                Long totalLatency = endpointAnalytics.stream()
                    .filter(e -> e.getTotalItems() != null)
                    .mapToLong(e -> e.getTotalItems())
                    .sum();
                avgLatency = BigDecimal.valueOf(totalLatency)
                    .divide(BigDecimal.valueOf(endpointAnalytics.size()), 2, RoundingMode.HALF_UP);
            }

            if (!errorAnalyses.isEmpty()) {
                Long totalErrors = errorAnalyses.stream()
                    .filter(e -> e.getTotalItems() != null)
                    .mapToLong(e -> e.getTotalItems())
                    .sum();
                if (totalRequests > 0) {
                    errorRate = BigDecimal.valueOf(totalErrors * 100.0 / totalRequests)
                        .setScale(2, RoundingMode.HALF_UP);
                }
            }

            if (!slaCompliances.isEmpty()) {
                Long compliantCount = slaCompliances.stream()
                    .filter(s -> s.getTotalItems() != null && s.getTotalItems() > 0)
                    .count();
                slaCompliance = (int) ((compliantCount * 100.0) / slaCompliances.size());
            }

            log.info("KPIs calculados - Endpoints: {}, Requests: {}, Error Rate: {}%",
                totalEndpoints, totalRequests, errorRate);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    private Long parseLong(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Long.parseLong(value.trim()) : 0L;
        } catch (NumberFormatException e) {
            log.warn("No se pudo parsear valor Long: {}", value);
            return 0L;
        }
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange("*")
    public void refreshDashboard() {
        log.info("Refrescando dashboard de serving");
        try {
            loadPerformanceDashboard();
            loadDeploymentStatuses();
            loadEndpointAnalytics();
            loadSlaCompliances();
            loadErrorAnalyses();
            loadCostBreakdowns();
            calculateKPIs();

            Messagebox.show("Dashboard actualizado correctamente", "Éxito",
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al refrescar dashboard", e);
            Messagebox.show("Error al refrescar dashboard: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Getters ==========

    public ServingPerformanceDashboard getPerformanceDashboard() {
        return performanceDashboard;
    }

    public List<DeploymentStatus> getDeploymentStatuses() {
        return deploymentStatuses;
    }

    public List<EndpointAnalytics> getEndpointAnalytics() {
        return endpointAnalytics;
    }

    public List<ServingSlaCompliance> getSlaCompliances() {
        return slaCompliances;
    }

    public List<ServingErrorAnalysis> getErrorAnalyses() {
        return errorAnalyses;
    }

    public List<ServingCostBreakdown> getCostBreakdowns() {
        return costBreakdowns;
    }

    public Long getTotalEndpoints() {
        return totalEndpoints;
    }

    public Long getActiveEndpoints() {
        return activeEndpoints;
    }

    public Long getTotalRequests() {
        return totalRequests;
    }

    public BigDecimal getAvgLatency() {
        return avgLatency;
    }

    public BigDecimal getErrorRate() {
        return errorRate;
    }

    public Integer getSlaCompliance() {
        return slaCompliance;
    }

    // ========== Cleanup ==========

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            performanceDashboard = null;

            if (deploymentStatuses != null) {
                deploymentStatuses.clear();
                deploymentStatuses = null;
            }

            if (endpointAnalytics != null) {
                endpointAnalytics.clear();
                endpointAnalytics = null;
            }

            if (slaCompliances != null) {
                slaCompliances.clear();
                slaCompliances = null;
            }

            if (errorAnalyses != null) {
                errorAnalyses.clear();
                errorAnalyses = null;
            }

            if (costBreakdowns != null) {
                costBreakdowns.clear();
                costBreakdowns = null;
            }

            pageParams = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
