package com.codeflowx.govern.viewmodel.training;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.codeflowx.govern.entity.views.training.ExperimentLeaderboard;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.views.training.HpoProgressDashboard;
import com.codeflowx.govern.entity.views.training.TrainingCostAnalysis;
import com.codeflowx.govern.entity.views.training.TrainingMetricsSummary;
import com.codeflowx.govern.entity.views.training.TrainingOverview;
import com.codeflowx.govern.entity.views.training.TrainingResourceUtilization;
import com.codeflowx.govern.service.training.HpoProgressDashboardService;
import com.codeflowx.govern.service.training.TrainingOverviewService;
import com.codeflowx.govern.service.training.TrainingMetricsSummaryService;
import com.codeflowx.govern.service.training.TrainingResourceUtilizationService;
import com.codeflowx.govern.service.training.TrainingCostAnalysisService;
import com.codeflowx.govern.service.training.ExperimentLeaderboardService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;

/**
 * ViewModel para el Dashboard de Training
 * Gestiona visualización de experimentos, HPO y métricas de entrenamiento
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TrainingDashboardViewModel extends MasterPage implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(TrainingDashboardViewModel.class);

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private ModelService modelService;
    @WireVariable
    private HpoProgressDashboardService hpoProgressDashboardService;
    @WireVariable
    private TrainingOverviewService trainingOverviewService;
    @WireVariable
    private TrainingMetricsSummaryService trainingMetricsSummaryService;
    @WireVariable
    private TrainingResourceUtilizationService trainingResourceUtilizationService;
    @WireVariable
    private TrainingCostAnalysisService trainingCostAnalysisService;
    @WireVariable
    private ExperimentLeaderboardService experimentLeaderboardService;

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

    // ========== Paginación ==========
    private PageParams pageParams;

    // ========== Datos del Dashboard ==========
    private HpoProgressDashboard hpoProgress;
    private TrainingOverview trainingOverview;
    private List<TrainingMetricsSummary> metricsSummaries = new ArrayList<>();
    private List<TrainingResourceUtilization> resourceUtilizations = new ArrayList<>();
    private List<TrainingCostAnalysis> costAnalyses = new ArrayList<>();
    private List<ExperimentLeaderboard> leaderboard = new ArrayList<>();

    // ========== KPIs ==========
    private Long totalExperiments = 0L;
    private Long runningExperiments = 0L;
    private Long completedExperiments = 0L;
    private Long failedExperiments = 0L;
    private BigDecimal avgTrainingCost = BigDecimal.ZERO;
    private Integer successRate = 0;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();

        log.info("Inicializando TrainingDashboardViewModel");

        loadHpoProgress();
        loadTrainingOverview();
        loadMetricsSummaries();
        loadResourceUtilizations();
        loadCostAnalyses();
        loadLeaderboard();
        calculateKPIs();
    }

    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trncreatedat")
                .build();
    }


    // ========== Carga de Datos ==========

    private void loadHpoProgress() {
        try {
            log.debug("Cargando progreso HPO");

            PageResult<HpoProgressDashboard> result = hpoProgressDashboardService.findAll(pageParams, new Criterias());

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                hpoProgress = result.getContent().get(0);
                log.info("Progreso HPO cargado correctamente");
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar progreso HPO", e);
        }
    }

    private void loadTrainingOverview() {
        try {
            log.debug("Cargando overview de training");

            PageResult<TrainingOverview> result = trainingOverviewService.findAll(pageParams, new Criterias());

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                trainingOverview = result.getContent().get(0);
                log.info("Overview de training cargado correctamente");
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar overview de training", e);
        }
    }

    private void loadMetricsSummaries() {
        try {
            log.debug("Cargando resúmenes de métricas");

            PageResult<TrainingMetricsSummary> result = trainingMetricsSummaryService.findAll(pageParams, new Criterias());

            if (result != null && result.getContent() != null) {
                metricsSummaries = result.getContent();
                log.info("Cargados {} resúmenes de métricas", metricsSummaries.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar resúmenes de métricas", e);
        }
    }

    private void loadResourceUtilizations() {
        try {
            log.debug("Cargando utilización de recursos");

            PageResult<TrainingResourceUtilization> result = trainingResourceUtilizationService.findAll(pageParams, new Criterias());

            if (result != null && result.getContent() != null) {
                resourceUtilizations = result.getContent();
                log.info("Cargadas {} utilizaciones de recursos", resourceUtilizations.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar utilización de recursos", e);
        }
    }

    private void loadCostAnalyses() {
        try {
            log.debug("Cargando análisis de costos");

            PageResult<TrainingCostAnalysis> result = trainingCostAnalysisService.findAll(pageParams, new Criterias());

            if (result != null && result.getContent() != null) {
                costAnalyses = result.getContent();
                log.info("Cargados {} análisis de costos", costAnalyses.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar análisis de costos", e);
        }
    }

    private void loadLeaderboard() {
        try {
            log.debug("Cargando leaderboard de experimentos");

            PageParams leaderboardParams = PageParams.builder()
                .maxRows(10)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trnscore")
                .build();

            PageResult<ExperimentLeaderboard> result = experimentLeaderboardService.findAll(leaderboardParams, new Criterias());

            if (result != null && result.getContent() != null) {
                leaderboard = result.getContent();
                log.info("Cargados {} experimentos en leaderboard", leaderboard.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar leaderboard", e);
        }
    }

    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs de training");

            if (trainingOverview != null) {
                totalExperiments = trainingOverview.getTotalRuns() != null ? trainingOverview.getTotalRuns() : 0L;
                runningExperiments = trainingOverview.getRunningRuns() != null ? trainingOverview.getRunningRuns() : 0L;
                completedExperiments = trainingOverview.getCompletedRuns() != null ? trainingOverview.getCompletedRuns() : 0L;
                failedExperiments = trainingOverview.getFailedRuns() != null ? trainingOverview.getFailedRuns() : 0L;
            }

            if (!costAnalyses.isEmpty()) {
                long count = costAnalyses.stream()
                    .filter(c -> c.getTotalItems() != null)
                    .count();

                if (count > 0) {
                    BigDecimal sum = costAnalyses.stream()
                        .filter(c -> c.getTotalItems() != null)
                        .map(c -> BigDecimal.valueOf(c.getTotalItems()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                    avgTrainingCost = sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
                }
            }

            if (totalExperiments > 0 && completedExperiments != null) {
                successRate = (int) ((completedExperiments * 100.0) / totalExperiments);
            }

            log.info("KPIs calculados - Total: {}, Running: {}, Completed: {}",
                totalExperiments, runningExperiments, completedExperiments);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange("*")
    public void refreshDashboard() {
        log.info("Refrescando dashboard de training");
        try {
            loadHpoProgress();
            loadTrainingOverview();
            loadMetricsSummaries();
            loadResourceUtilizations();
            loadCostAnalyses();
            loadLeaderboard();
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

    public HpoProgressDashboard getHpoProgress() {
        return hpoProgress;
    }

    public TrainingOverview getTrainingOverview() {
        return trainingOverview;
    }

    public List<TrainingMetricsSummary> getMetricsSummaries() {
        return metricsSummaries;
    }

    public List<TrainingResourceUtilization> getResourceUtilizations() {
        return resourceUtilizations;
    }

    public List<TrainingCostAnalysis> getCostAnalyses() {
        return costAnalyses;
    }

    public List<ExperimentLeaderboard> getLeaderboard() {
        return leaderboard;
    }

    public Long getTotalExperiments() {
        return totalExperiments;
    }

    public Long getRunningExperiments() {
        return runningExperiments;
    }

    public Long getCompletedExperiments() {
        return completedExperiments;
    }

    public Long getFailedExperiments() {
        return failedExperiments;
    }

    public BigDecimal getAvgTrainingCost() {
        return avgTrainingCost;
    }

    public Integer getSuccessRate() {
        return successRate;
    }

    // ========== Cleanup ==========

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            hpoProgress = null;
            trainingOverview = null;

            if (metricsSummaries != null) {
                metricsSummaries.clear();
                metricsSummaries = null;
            }

            if (resourceUtilizations != null) {
                resourceUtilizations.clear();
                resourceUtilizations = null;
            }

            if (costAnalyses != null) {
                costAnalyses.clear();
                costAnalyses = null;
            }

            if (leaderboard != null) {
                leaderboard.clear();
                leaderboard = null;
            }

            pageParams = null;
            modelService = null;
            hpoProgressDashboardService = null;
            trainingOverviewService = null;
            trainingMetricsSummaryService = null;
            trainingResourceUtilizationService = null;
            trainingCostAnalysisService = null;
            experimentLeaderboardService = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }

	@Override
	public void setBeans(Object bean) {
		// TODO Auto-generated method stub

	}
}
