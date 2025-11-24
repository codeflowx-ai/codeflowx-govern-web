package com.codeflowx.govern.viewmodel.training;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.training.Checkpoint;
import com.codeflowx.govern.service.training.ExperimentService;
import com.codeflowx.govern.entity.training.Experiment;
import com.codeflowx.govern.entity.training.HPOTrial;
import com.codeflowx.govern.entity.training.MetricSeries;
import com.codeflowx.govern.entity.training.Param;
import com.codeflowx.govern.entity.training.Run;
import com.codeflowx.govern.entity.training.TrainingArtifact;
import com.codeflowx.govern.entity.training.TrainingGovernance;
import com.codeflowx.govern.entity.training.TrainingLog;
import com.codeflowx.govern.entity.training.TrainingMetric;
import com.codeflowx.govern.entity.views.training.TrainingMetricsSummary;
import com.codeflowx.govern.service.training.HPOTrialService;
import com.codeflowx.govern.service.training.MetricSeriesService;
import com.codeflowx.govern.service.training.ParamService;
import com.codeflowx.govern.service.training.TrainingLogService;
import com.codeflowx.govern.service.training.TrainingMetricService;
import com.codeflowx.govern.service.training.TrainingGovernanceService;
import com.codeflowx.govern.service.training.TrainingArtifactService;
import com.codeflowx.govern.service.training.CheckpointService;
import com.codeflowx.govern.service.training.RunService;
import com.codeflowx.govern.service.training.TrainingMetricsSummaryService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;

/**
 * ViewModel complejo para DETALLE/EDICIÓN/CREACIÓN de Experimentos de Training
 *
 * Funcionalidad:
 * - CRUD de experimentos
 * - Gestión de runs y trials HPO
 * - Monitoreo de métricas en tiempo real
 * - Gestión de checkpoints
 * - Visualización de logs
 * - Comparación de runs
 * - Governance y compliance
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExperimentsDetailViewModel extends MasterPage implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ExperimentsDetailViewModel.class);

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private ExperimentService experimentService;
    @WireVariable
    private RunService runService;
    @WireVariable
    private CheckpointService checkpointService;
    @WireVariable
    private TrainingArtifactService trainingArtifactService;
    @WireVariable
    private TrainingGovernanceService trainingGovernanceService;
    @WireVariable
    private TrainingMetricService trainingMetricService;
    @WireVariable
    private TrainingLogService trainingLogService;
    @WireVariable
    private ParamService paramService;
    @WireVariable
    private MetricSeriesService metricSeriesService;
    @WireVariable
    private HPOTrialService hPOTrialService;
    @WireVariable
    private TrainingMetricsSummaryService trainingMetricsSummaryService;

    @WireVariable
    private BusinessService businessService; // Mantener para findByParams y procedimientos almacenados

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

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Entidad Principal ==========
    private Experiment currentExperiment;
    private boolean isNewExperiment = false;
    private boolean isEditMode = false;

    // ========== Información Descendente ==========
    private List<Run> experimentRuns = new ArrayList<>();
    private List<HPOTrial> hpoTrials = new ArrayList<>();
    private List<Checkpoint> checkpoints = new ArrayList<>();
    private List<TrainingMetric> metrics = new ArrayList<>();
    private List<TrainingLog> trainingLogs = new ArrayList<>();
    private List<TrainingArtifact> artifacts = new ArrayList<>();
    private List<Param> parameters = new ArrayList<>();
    private List<MetricSeries> metricSeries = new ArrayList<>();
    private TrainingGovernance governanceInfo;
    private TrainingMetricsSummary metricsSummary;

    // ========== KPIs del Experimento ==========
    private Integer totalRuns = 0;
    private Integer completedRuns = 0;
    private Integer failedRuns = 0;
    private Integer runningRuns = 0;
    private BigDecimal bestScore = BigDecimal.ZERO;
    private String bestRunId = "";
    private Double avgTrainingTime = 0.0;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("Inicializando ExperimentsDetailViewModel");

        String idParam = Executions.getCurrent().getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                Long experimentId = Long.parseLong(idParam);
                loadExperiment(experimentId);
            } catch (NumberFormatException e) {
                log.error("ID de experimento inválido: {}", idParam, e);
                Messagebox.show("ID de experimento inválido", "Error", Messagebox.OK, Messagebox.ERROR);
            }
        } else {
            initNewExperiment();
        }
    }



    private void initNewExperiment() {
        log.info("Inicializando nuevo experimento");
        isNewExperiment = true;
        isEditMode = true;
        currentExperiment = new Experiment();

        // Experiment no tiene campo trnstatus, usar trnlifecyclestage
        currentExperiment.setTrnlifecyclestage("DRAFT");
        // trncreatedby y trnupdatedby son UUID, no String
        // currentExperiment.setTrncreatedby(getUser().getUsername());
        currentExperiment.setTrncreatedat(new Timestamp(System.currentTimeMillis()));
        // currentExperiment.setTrnupdatedby(getUser().getUsername());
        currentExperiment.setTrnupdatedat(new Timestamp(System.currentTimeMillis()));
    }

    private void loadExperiment(Long experimentId) {
        try {
            log.info("Cargando experimento ID: {}", experimentId);

            currentExperiment = experimentService.findById(experimentId);

            if (currentExperiment != null) {
                isNewExperiment = false;
                isEditMode = false;

                loadExperimentRuns(experimentId);
                loadHpoTrials(experimentId);
                loadCheckpoints(experimentId);
                loadMetrics(experimentId);
                loadTrainingLogs(experimentId);
                loadArtifacts(experimentId);
                loadParameters(experimentId);
                loadMetricSeries(experimentId);
                loadGovernanceInfo(experimentId);
                loadMetricsSummary(experimentId);

                calculateKPIs();

                log.info("Experimento cargado correctamente: {}", currentExperiment.getTrnname());
            } else {
                log.warn("No se encontró experimento con ID: {}", experimentId);
                Messagebox.show("Experimento no encontrado", "Advertencia",
                    Messagebox.OK, Messagebox.EXCLAMATION);
                initNewExperiment();
            }
        } catch (Exception e) {
            log.error("Error al cargar experimento", e);
            Messagebox.show("Error al cargar experimento: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Carga de Datos Relacionados ==========

    private void loadExperimentRuns(Long experimentId) {
        try {
            log.debug("Cargando runs del experimento");

            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trncreatedat")
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDTRNEXPERIMENTS0");
            criteria.setValueEnd(experimentId);
            criterias.addCriteria(criteria);

            PageResult<Run> result = runService.findAll(params, criterias);

            if (result != null && result.getContent() != null) {
                experimentRuns = result.getContent();
                log.info("Cargados {} runs", experimentRuns.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar runs", e);
        }
    }

    private void loadHpoTrials(Long experimentId) {
        try {
            log.debug("Cargando HPO trials");

            // NOTE: HPOTrial no tiene FK directa a Experiment, se relaciona a través de HPOExperiment
            // Para cargar correctamente, necesitamos primero cargar HPOExperiment o hacer un join
            PageParams params = PageParams.builder()
                .maxRows(200)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trncreatedat")
                .build();

            PageResult<HPOTrial> result = hPOTrialService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null) {
                hpoTrials = result.getContent();
                log.info("Cargados {} HPO trials", hpoTrials.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar HPO trials", e);
        }
    }

    private void loadCheckpoints(Long experimentId) {
        try {
            log.debug("Cargando checkpoints");

            // Checkpoints tienen FK a Run (IDTRNRUNS0), no directamente a Experiment
            // Cargar usando SQL con JOIN o filtrar por los runs ya cargados
            String sql = "SELECT c.* FROM TRNCHECKPOINTS c " +
                        "INNER JOIN TRNRUNS r ON c.IDTRNRUNS0 = r.IDXRUN " +
                        "WHERE r.IDTRNEXPERIMENTS0 = :experimentId " +
                        "ORDER BY c.TRNCREATEDAT DESC LIMIT 50";

            Map<String, Object> params = new HashMap<>();
            params.put("experimentId", experimentId);

            PageParams pageParams = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            PageResult<Checkpoint> result = businessService.findByParams(Checkpoint.class, sql, params, pageParams);

            if (result != null && result.getContent() != null) {
                checkpoints = result.getContent();
                log.info("Cargados {} checkpoints", checkpoints.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar checkpoints", e);
        }
    }

    private void loadMetrics(Long experimentId) {
        try {
            log.debug("Cargando métricas");

            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trncreatedat")
                .build();

            // TODO: Filtrar correctamente por FK de Run relacionado a este Experiment
            PageResult<TrainingMetric> result = trainingMetricService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null) {
                metrics = result.getContent();
                log.info("Cargadas {} métricas", metrics.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }

    private void loadTrainingLogs(Long experimentId) {
        try {
            log.debug("Cargando logs de training");

            PageParams params = PageParams.builder()
                .maxRows(200)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trncreatedat")
                .build();

            // TODO: Filtrar correctamente por FK de Run relacionado a este Experiment
            PageResult<TrainingLog> result = trainingLogService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null) {
                trainingLogs = result.getContent();
                log.info("Cargados {} logs", trainingLogs.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar logs", e);
        }
    }

    private void loadArtifacts(Long experimentId) {
        try {
            log.debug("Cargando artifacts");

            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trncreatedat")
                .build();

            // TODO: Filtrar correctamente por FK de Run relacionado a este Experiment
            PageResult<TrainingArtifact> result = trainingArtifactService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null) {
                artifacts = result.getContent();
                log.info("Cargados {} artifacts", artifacts.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar artifacts", e);
        }
    }

    private void loadParameters(Long experimentId) {
        try {
            log.debug("Cargando parámetros");

            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trnkey")
                .build();

            // TODO: Filtrar correctamente por FK de Run relacionado a este Experiment
            PageResult<Param> result = paramService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null) {
                parameters = result.getContent();
                log.info("Cargados {} parámetros", parameters.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar parámetros", e);
        }
    }

    private void loadMetricSeries(Long experimentId) {
        try {
            log.debug("Cargando series de métricas");

            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("trntimestamp")
                .build();

            // TODO: Filtrar correctamente por FK de Run relacionado a este Experiment
            PageResult<MetricSeries> result = metricSeriesService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null) {
                metricSeries = result.getContent();
                log.info("Cargadas {} series de métricas", metricSeries.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar series de métricas", e);
        }
    }

    private void loadGovernanceInfo(Long experimentId) {
        try {
            log.debug("Cargando información de governance");

            PageParams params = PageParams.builder()
                .maxRows(1)
                .pageActual(1)
                .rowActual(0)
                .build();

            // TODO: Filtrar correctamente por FK de Run relacionado a este Experiment
            PageResult<TrainingGovernance> result = trainingGovernanceService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                governanceInfo = result.getContent().get(0);
                log.info("Información de governance cargada");
            }
        } catch (Exception e) {
            log.error("Error al cargar governance info", e);
        }
    }

    private void loadMetricsSummary(Long experimentId) {
        try {
            log.debug("Cargando resumen de métricas");

            // TrainingMetricsSummary es una VIEW, no tiene FK a experimento específico
            // Es un resumen global de todas las métricas
            PageParams params = PageParams.builder()
                .maxRows(1)
                .pageActual(1)
                .rowActual(0)
                .build();

            PageResult<TrainingMetricsSummary> result = trainingMetricsSummaryService.findAll(params, new Criterias());

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                metricsSummary = result.getContent().get(0);
                log.info("Resumen de métricas cargado");
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar resumen de métricas", e);
        }
    }

    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs del experimento");

            totalRuns = experimentRuns.size();

            completedRuns = (int) experimentRuns.stream()
                .filter(r -> "COMPLETED".equals(r.getTrnstatus()))
                .count();

            failedRuns = (int) experimentRuns.stream()
                .filter(r -> "FAILED".equals(r.getTrnstatus()))
                .count();

            runningRuns = (int) experimentRuns.stream()
                .filter(r -> "RUNNING".equals(r.getTrnstatus()))
                .count();

            // NOTE: Run no tiene campo trnsc ore directo.
            // El score estaría en TrainingMetric asociado al Run
            // Por ahora dejamos en 0
            bestScore = BigDecimal.ZERO;
            bestRunId = "";

            // Calcular tiempo promedio de duración (trnendtime - trnstarttime)
            if (!experimentRuns.isEmpty()) {
                long totalTime = experimentRuns.stream()
                    .filter(r -> r.getTrnstarttime() != null && r.getTrnendtime() != null)
                    .mapToLong(r -> r.getTrnendtime().getTime() - r.getTrnstarttime().getTime())
                    .sum();
                avgTrainingTime = totalTime > 0 ? (double) totalTime / experimentRuns.size() / 1000.0 : 0.0; // convertir a segundos
            }

            log.info("KPIs calculados - Total Runs: {}, Completed: {}, Best Score: {}",
                totalRuns, completedRuns, bestScore);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    // ========== Comandos CRUD ==========

    @Command
    @NotifyChange("*")
    public void saveExperiment() {
        log.info("Guardando experimento");
        try {
            if (validateExperiment()) {
                if (isNewExperiment) {
                    // trncreatedby es UUID, no String
                    // currentExperiment.setTrncreatedby(getUser().getUsername());
                    currentExperiment.setTrncreatedat(new Timestamp(System.currentTimeMillis()));
                }

                // trnupdatedby es UUID, no String
                // currentExperiment.setTrnupdatedby(getUser().getUsername());
                currentExperiment.setTrnupdatedat(new Timestamp(System.currentTimeMillis()));

                currentExperiment = experimentService.create(currentExperiment);

                isNewExperiment = false;
                isEditMode = false;

                Messagebox.show("Experimento guardado correctamente", "Éxito",
                    Messagebox.OK, Messagebox.INFORMATION);

                log.info("Experimento guardado exitosamente: {}", currentExperiment.getIdxexperiment());
            }
        } catch (Exception e) {
            log.error("Error al guardar experimento", e);
            Messagebox.show("Error al guardar experimento: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"isEditMode"})
    public void enableEdit() {
        log.info("Habilitando modo edición");
        isEditMode = true;
    }

    @Command
    public void cancel() {
        cancelEdit();
    }

    @Command
    public void cancelEdit() {
        log.info("Cancelando edición");
        if (isNewExperiment) {
            Executions.sendRedirect("/training/experiments-overview.zul");
        } else {
            loadExperiment(currentExperiment.getIdxexperiment());
            isEditMode = false;
        }
    }

    private boolean validateExperiment() {
        if (currentExperiment.getTrnname() == null || currentExperiment.getTrnname().trim().isEmpty()) {
            Messagebox.show("El nombre del experimento es obligatorio", "Validación",
                Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }

        return true;
    }

    // ========== Comandos de Operaciones ==========

    @Command
    public void viewRun(@BindingParam("run") Run run) {
        log.info("Navegando a detalle de Run ID={}", run.getIdxrun());
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", run.getIdxrun());
        params.put("action", org.enartframework.web.annotation.Action.LOAD);
        Executions.sendRedirect("/platform/training/runs/page.zul");
    }

    @Command
    @NotifyChange("*")
    public void startExperiment() {
        log.info("Iniciando experimento: {}", currentExperiment.getIdxexperiment());
        try {
            currentExperiment.setTrnlifecyclestage("RUNNING");
            // TODO: Verificar si existe trnstartedat o es otro atributo
            // currentExperiment.setTrnstartedat(new Timestamp(System.currentTimeMillis()));
            // trnupdatedby es UUID, no String
            // currentExperiment.setTrnupdatedby(getUser().getUsername());
            currentExperiment.setTrnupdatedat(new Timestamp(System.currentTimeMillis()));

            currentExperiment = experimentService.create(currentExperiment);

            Messagebox.show("Experimento iniciado correctamente", "Éxito",
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al iniciar experimento", e);
            Messagebox.show("Error al iniciar experimento: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void stopExperiment() {
        log.info("Deteniendo experimento: {}", currentExperiment.getIdxexperiment());
        try {
            currentExperiment.setTrnlifecyclestage("STOPPED");
            // NOTE: Experiment no tiene trnfinishedat, solo tiene trncreatedat/trnupdatedat
            currentExperiment.setTrnupdatedat(new Timestamp(System.currentTimeMillis()));
            // trnupdatedby es UUID, no String
            // currentExperiment.setTrnupdatedby(getUser().getUsername());

            currentExperiment = experimentService.create(currentExperiment);

            Messagebox.show("Experimento detenido correctamente", "Éxito",
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al detener experimento", e);
            Messagebox.show("Error al detener experimento: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void refreshData() {
        log.info("Refrescando datos del experimento");
        if (currentExperiment != null && currentExperiment.getIdxexperiment() != null) {
            loadExperiment(currentExperiment.getIdxexperiment());
        }
    }

    // ========== Getters ==========

    public Experiment getCurrentExperiment() {
        return currentExperiment;
    }

    public boolean isNewExperiment() {
        return isNewExperiment;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public List<Run> getExperimentRuns() {
        return experimentRuns;
    }

    public List<HPOTrial> getHpoTrials() {
        return hpoTrials;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public List<TrainingMetric> getMetrics() {
        return metrics;
    }

    public List<TrainingLog> getTrainingLogs() {
        return trainingLogs;
    }

    public List<TrainingArtifact> getArtifacts() {
        return artifacts;
    }

    public List<Param> getParameters() {
        return parameters;
    }

    public List<MetricSeries> getMetricSeries() {
        return metricSeries;
    }

    public TrainingGovernance getGovernanceInfo() {
        return governanceInfo;
    }

    public TrainingMetricsSummary getMetricsSummary() {
        return metricsSummary;
    }

    public Integer getTotalRuns() {
        return totalRuns;
    }

    public Integer getCompletedRuns() {
        return completedRuns;
    }

    public Integer getFailedRuns() {
        return failedRuns;
    }

    public Integer getRunningRuns() {
        return runningRuns;
    }

    public BigDecimal getBestScore() {
        return bestScore;
    }

    public String getBestRunId() {
        return bestRunId;
    }

    public Double getAvgTrainingTime() {
        return avgTrainingTime;
    }

    // ========== Cleanup ==========

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            currentExperiment = null;
            governanceInfo = null;
            metricsSummary = null;

            if (experimentRuns != null) {
                experimentRuns.clear();
                experimentRuns = null;
            }

            if (hpoTrials != null) {
                hpoTrials.clear();
                hpoTrials = null;
            }

            if (checkpoints != null) {
                checkpoints.clear();
                checkpoints = null;
            }

            if (metrics != null) {
                metrics.clear();
                metrics = null;
            }

            if (trainingLogs != null) {
                trainingLogs.clear();
                trainingLogs = null;
            }

            if (artifacts != null) {
                artifacts.clear();
                artifacts = null;
            }

            if (parameters != null) {
                parameters.clear();
                parameters = null;
            }

            if (metricSeries != null) {
                metricSeries.clear();
                metricSeries = null;
            }

            experimentService = null;
            runService = null;
            checkpointService = null;
            trainingArtifactService = null;
            trainingGovernanceService = null;
            trainingMetricService = null;
            trainingLogService = null;
            paramService = null;
            metricSeriesService = null;
            hPOTrialService = null;
            trainingMetricsSummaryService = null;
            businessService = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
