package com.codeflowx.govern.viewmodel.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.dashboard.CostMetrics;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.dashboard.ModuleStats;
import com.codeflowx.govern.entity.dashboard.QuickAction;
import com.codeflowx.govern.entity.dashboard.TokenMetrics;
import com.codeflowx.govern.entity.views.agents.AgentHealthDashboard;
import com.codeflowx.govern.entity.views.core.AdminDashboardSummary;
import com.codeflowx.govern.entity.views.governance.GovernanceDashboardSummary;
import com.codeflowx.govern.entity.views.serving.ServingPerformanceDashboard;
import com.codeflowx.govern.entity.views.training.HpoProgressDashboard;
import com.codeflowx.govern.service.dashboard.CostMetricsService;
import com.codeflowx.govern.service.dashboard.ModuleStatsService;
import com.codeflowx.govern.service.dashboard.QuickActionService;
import com.codeflowx.govern.service.dashboard.TokenMetricsService;
import com.codeflowx.govern.service.core.AdminDashboardSummaryService;
import com.codeflowx.govern.service.agents.AgentHealthDashboardService;
import com.codeflowx.govern.service.governance.GovernanceDashboardSummaryService;
import com.codeflowx.govern.service.training.HpoProgressDashboardService;
import com.codeflowx.govern.service.serving.ServingPerformanceDashboardService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para el Dashboard Principal del Sistema
 *
 * Responsabilidades:
 * - Mostrar KPIs principales del sistema
 * - Resumen ejecutivo de todos los módulos
 * - Quick actions para operaciones frecuentes
 * - Métricas de costos y tokens
 * - Health status agregado
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class MainDashboardViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private ModelService modelService;

    @WireVariable
    private AdminDashboardSummaryService adminDashboardSummaryService;

    @WireVariable
    private ModuleStatsService moduleStatsService;

    @WireVariable
    private QuickActionService quickActionService;

    @WireVariable
    private CostMetricsService costMetricsService;

    @WireVariable
    private TokenMetricsService tokenMetricsService;

    @WireVariable
    private AgentHealthDashboardService agentHealthDashboardService;

    @WireVariable
    private GovernanceDashboardSummaryService governanceDashboardSummaryService;

    @WireVariable
    private HpoProgressDashboardService hpoProgressDashboardService;

    @WireVariable
    private ServingPerformanceDashboardService servingPerformanceDashboardService;

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
        // Los servicios se inyectan automáticamente mediante @WireVariable
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;

    // ========== Datos del Dashboard ==========
    private AdminDashboardSummary dashboardSummary;
    private List<ModuleStats> moduleStats = new ArrayList<>();
    private List<QuickAction> quickActions = new ArrayList<>();
    private CostMetrics costMetrics;
    private TokenMetrics tokenMetrics;

    // ========== Dashboards de módulos específicos ==========
    private AgentHealthDashboard agentHealth;
    private GovernanceDashboardSummary governanceSummary;
    private HpoProgressDashboard trainingProgress;
    private ServingPerformanceDashboard servingPerformance;

    // ========== KPIs Principales ==========
    private Long totalProjects = 0L;
    private Long totalModels = 0L;
    private Long totalAgents = 0L;
    private Long activeDeployments = 0L;
    private BigDecimal totalCostToday = BigDecimal.ZERO;
    private Long totalTokensToday = 0L;
    private Integer systemHealthScore = 100;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();

        log.info("Inicializando MainDashboardViewModel");

        // Cargar todos los datos del dashboard
        loadDashboardSummary();
        loadModuleStats();
        loadQuickActions();
        loadCostMetrics();
        loadTokenMetrics();
        loadModuleDashboards();
        calculateKPIs();
    }

    /**
     * Inicializa los parámetros de paginación
     */
    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("dshcreatedat")
                .build();
    }

    // ========== Carga de Datos ==========

    /**
     * Carga el resumen principal del dashboard
     */
    private void loadDashboardSummary() {
        try {
            log.debug("Cargando resumen del dashboard");

            // AdminDashboardSummary es una VIEW - usar servicio dedicado
            PageResult<AdminDashboardSummary> result = adminDashboardSummaryService.findAll(pageParams);

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                dashboardSummary = result.getContent().get(0);
                log.info("Resumen del dashboard cargado correctamente");
            } else {
                dashboardSummary = new AdminDashboardSummary();
                log.warn("No se encontró resumen del dashboard, usando valores por defecto");
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar resumen del dashboard", e);
            dashboardSummary = new AdminDashboardSummary();
        }
    }

    /**
     * Carga estadísticas de módulos
     */
    private void loadModuleStats() {
        try {
            log.debug("Cargando estadísticas de módulos");

            // ModuleStats es una TABLE - usar servicio dedicado
            PageResult<ModuleStats> result = moduleStatsService.findAll(pageParams);

            if (result != null && result.getContent() != null) {
                moduleStats = result.getContent();
                log.info("Cargadas {} estadísticas de módulos", moduleStats.size());
            } else {
                moduleStats = new ArrayList<>();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar estadísticas de módulos", e);
            moduleStats = new ArrayList<>();
        }
    }

    /**
     * Carga acciones rápidas disponibles
     */
    private void loadQuickActions() {
        try {
            log.debug("Cargando quick actions");

            // QuickAction es una TABLE - usar servicio dedicado
            PageResult<QuickAction> result = quickActionService.findAll(pageParams);

            if (result != null && result.getContent() != null) {
                quickActions = result.getContent();
                log.info("Cargadas {} quick actions", quickActions.size());
            } else {
                quickActions = new ArrayList<>();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar quick actions", e);
            quickActions = new ArrayList<>();
        }
    }

    /**
     * Carga métricas de costos
     */
    private void loadCostMetrics() {
        try {
            log.debug("Cargando métricas de costos");

            // CostMetrics es una TABLE - usar servicio dedicado
            PageResult<CostMetrics> result = costMetricsService.findAll(pageParams);

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                costMetrics = result.getContent().get(0);
                log.info("Métricas de costos cargadas correctamente");
            } else {
                costMetrics = new CostMetrics();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar métricas de costos", e);
            costMetrics = new CostMetrics();
        }
    }

    /**
     * Carga métricas de tokens
     */
    private void loadTokenMetrics() {
        try {
            log.debug("Cargando métricas de tokens");

            // TokenMetrics es una TABLE - usar servicio dedicado
            PageResult<TokenMetrics> result = tokenMetricsService.findAll(pageParams);

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                tokenMetrics = result.getContent().get(0);
                log.info("Métricas de tokens cargadas correctamente");
            } else {
                tokenMetrics = new TokenMetrics();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar métricas de tokens", e);
            tokenMetrics = new TokenMetrics();
        }
    }

    /**
     * Carga dashboards de módulos específicos
     */
    private void loadModuleDashboards() {
        try {
            log.debug("Cargando dashboards de módulos específicos");

            // Agent Health - VIEW - usar servicio dedicado
            PageResult<AgentHealthDashboard> agentResult = agentHealthDashboardService.findAll(pageParams);
            if (agentResult != null && agentResult.getContent() != null && !agentResult.getContent().isEmpty()) {
                agentHealth = agentResult.getContent().get(0);
            }

            // Governance Summary - VIEW - usar servicio dedicado
            PageResult<GovernanceDashboardSummary> govResult = governanceDashboardSummaryService.findAll(pageParams);
            if (govResult != null && govResult.getContent() != null && !govResult.getContent().isEmpty()) {
                governanceSummary = govResult.getContent().get(0);
            }

            // Training Progress - VIEW - usar servicio dedicado
            PageResult<HpoProgressDashboard> trainResult = hpoProgressDashboardService.findAll(pageParams);
            if (trainResult != null && trainResult.getContent() != null && !trainResult.getContent().isEmpty()) {
                trainingProgress = trainResult.getContent().get(0);
            }

            // Serving Performance - VIEW - usar servicio dedicado
            PageResult<ServingPerformanceDashboard> servResult = servingPerformanceDashboardService.findAll(pageParams);
            if (servResult != null && servResult.getContent() != null && !servResult.getContent().isEmpty()) {
                servingPerformance = servResult.getContent().get(0);
            }

            log.info("Dashboards de módulos cargados correctamente");
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar dashboards de módulos", e);
        }
    }

    /**
     * Calcula KPIs principales a partir de los datos cargados
     */
    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs principales");

            if (dashboardSummary != null) {
                totalProjects = dashboardSummary.getTotalItems() != null ? dashboardSummary.getTotalItems() : 0L;
                activeDeployments = dashboardSummary.getDeployedItems() != null ? dashboardSummary.getDeployedItems() : 0L;
            }

            if (agentHealth != null) {
                totalAgents = agentHealth.getTotalItems() != null ? agentHealth.getTotalItems() : 0L;
            }

            if (costMetrics != null) {
                totalCostToday = costMetrics.getDshcosttoday() != null ? costMetrics.getDshcosttoday() : BigDecimal.ZERO;
            }

            if (tokenMetrics != null) {
                totalTokensToday = tokenMetrics.getDshtokenstoday() != null ? tokenMetrics.getDshtokenstoday() : 0L;
            }

            // System Health Score basado en score promedio
            if (dashboardSummary != null && dashboardSummary.getAvgScore() != null) {
                systemHealthScore = dashboardSummary.getAvgScore().intValue();
            }

            log.info("KPIs calculados - Proyectos: {}, Agentes: {}, Deployments: {}", totalProjects, totalAgents, activeDeployments);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    // ========== Comandos de Actualización ==========

    @Command
    @NotifyChange("*")
    public void refreshDashboard() {
        log.info("Refrescando dashboard completo");
        try {
            loadDashboardSummary();
            loadModuleStats();
            loadCostMetrics();
            loadTokenMetrics();
            loadModuleDashboards();
            calculateKPIs();

            Messagebox.show("Dashboard actualizado correctamente", "Éxito",
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al refrescar dashboard", e);
            Messagebox.show("Error al actualizar dashboard: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());

        try {
            // Limpiar objetos principales
            dashboardSummary = null;
            costMetrics = null;
            tokenMetrics = null;
            agentHealth = null;
            governanceSummary = null;
            trainingProgress = null;
            servingPerformance = null;

            // Limpiar listas
            if (moduleStats != null) {
                moduleStats.clear();
                moduleStats = null;
            }
            if (quickActions != null) {
                quickActions.clear();
                quickActions = null;
            }

            // Limpiar parámetros
            pageParams = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
