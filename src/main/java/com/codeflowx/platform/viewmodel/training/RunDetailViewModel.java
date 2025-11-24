package com.codeflowx.platform.viewmodel.training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.suinsit.Context;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.entity.training.Checkpoint;
import com.codeflowx.govern.entity.training.HPOTrial;
import com.codeflowx.govern.entity.training.MetricSeries;
import com.codeflowx.govern.entity.training.MetricStream;
import com.codeflowx.govern.entity.training.Param;
import com.codeflowx.govern.entity.training.Run;
import com.codeflowx.govern.entity.training.Tag;
import com.codeflowx.govern.entity.training.TrainingAlert;
import com.codeflowx.govern.entity.training.TrainingArtifact;
import com.codeflowx.govern.entity.training.TrainingLog;
import com.codeflowx.govern.entity.training.TrainingMetric;

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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Run
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RunDetailViewModel extends MasterPage {

    @WireVariable
    private RunService runService;
    @WireVariable
    private CheckpointService checkpointService;
    @WireVariable
    private EnvironmentService environmentService;
    @WireVariable
    private HPOTrialService hpoTrialService;
    @WireVariable
    private MetricSeriesService metricSeriesService;
    @WireVariable
    private MetricStreamService metricStreamService;
    @WireVariable
    private ParamService paramService;
    @WireVariable
    private TagService tagService;
    @WireVariable
    private TrainingAlertService trainingAlertService;
    @WireVariable
    private TrainingArtifactService trainingArtifactService;
    @WireVariable
    private TrainingLogService trainingLogService;
    @WireVariable
    private TrainingMetricService trainingMetricService;
    @WireVariable
    private BusinessService businessService; // Mantener para logActivity y UniqueValidator

    @Autowired
    protected IEntityLocal dao;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    @WireVariable("APPLICATION_DS")
    protected DataSource ds;

    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";

    // ========== Modo de operación ==========
    private String mode;
    private Long idxrun;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private Run currentRun;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalTrnrunname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableTrnstatuss = new ArrayList<>();
    private List<String> availableTrngovernancestatuss = new ArrayList<>();
    private List<String> availableTrnruntypes = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedTrntags = new ArrayList<>();
    private String newTrntag = "";

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<Checkpoint> subtrncheckpoints = new ArrayList<>();
    private List<Environment> subtrnenvironments = new ArrayList<>();
    private List<HPOTrial> subtrnhpotrials = new ArrayList<>();
    private List<MetricSeries> subtrnmetricseries = new ArrayList<>();
    private List<MetricStream> subtrnmetricstreams = new ArrayList<>();
    private List<Param> subtrnparams = new ArrayList<>();
    private List<Run> subtrnruns = new ArrayList<>();
    private List<Tag> subtrntags = new ArrayList<>();
    private List<TrainingAlert> subtrntrainingalerts = new ArrayList<>();
    private List<TrainingArtifact> subtrntrainingartifacts = new ArrayList<>();
    private List<TrainingLog> subtrntraininglogs = new ArrayList<>();
    private List<TrainingMetric> subtrntrainingmetrics = new ArrayList<>();
    private boolean subtrncheckpointsLoaded = false;
    private boolean subtrnenvironmentsLoaded = false;
    private boolean subtrnhpotrialsLoaded = false;
    private boolean subtrnmetricseriesLoaded = false;
    private boolean subtrnmetricstreamsLoaded = false;
    private boolean subtrnparamsLoaded = false;
    private boolean subtrnrunsLoaded = false;
    private boolean subtrntagsLoaded = false;
    private boolean subtrntrainingalertsLoaded = false;
    private boolean subtrntrainingartifactsLoaded = false;
    private boolean subtrntraininglogsLoaded = false;
    private boolean subtrntrainingmetricsLoaded = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        // Obtener parámetros de navegación - con protección para action null


        if (super.action != null) {


            mode = super.action.name();


        } else {


            mode = (dataParam != null) ? "LOAD" : "NEW";


            log.warn("Action es null, infiriendo modo: {}", mode);


        }

        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxrun = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando RunDetailViewModel - mode: {}, idxrun: {}", mode, idxrun);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxrun != null) {
            loadItem(idxrun);
        } else {
            log.error("Modo inválido o falta idxrun");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentRun, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentRun = new Run();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadTrnstatuss();
        loadTrngovernancestatuss();
        loadTrnruntypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentRun = runService.findById(id);

            if (currentRun == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentRun.getTrnrunname();
        loadTrnstatuss();
        loadTrngovernancestatuss();
        loadTrnruntypes();

            // Cargar tags/roles existentes desde JSON
            selectedTrntags = convertJsonToList(currentRun.getTrntags());

            // Guardar valores originales para validación de unicidad
            originalTrnrunname = currentRun.getTrnrunname();

            // Auditar carga de registro
            logActivity("CONSULTA", "TRNRUNS", id, "Consulta: " + currentRun.getTrnrunname());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
        }
    }

    @Command
    @NotifyChange("*")
    public void saveItem() {
        try {
            log.info("Guardando registro");

            // Validar campos obligatorios
            if (!validateRequiredFields()) {
                return;
            }

            boolean isNew = currentRun.getIdxrun() == null;

            if (isNew) {
                currentRun = runService.create(currentRun);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "TRNRUNS", currentRun.getIdxrun(),
                    "Creado: " + currentRun.getTrnrunname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentRun = runService.update(currentRun);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "TRNRUNS", currentRun.getIdxrun(),
                    "Actualizado: " + currentRun.getTrnrunname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);

        } catch (GovernanceServiceException e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error al guardar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Valida que todos los campos obligatorios estén completos
     * @return true si la validación es exitosa
     */
    private boolean validateRequiredFields() {
        StringBuilder errors = new StringBuilder();

        if (currentRun.getTrnrunuuid() == null || currentRun.getTrnrunuuid().trim().isEmpty()) {
            errors.append("- Runuuid\n");
        }
        if (currentRun.getTrnrunuuid() != null && currentRun.getTrnrunuuid().length() > 64) {
            errors.append("- Runuuid no puede exceder 64 caracteres\n");
        }
        if (currentRun.getTrnstatus() == null || currentRun.getTrnstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentRun.getTrnstarttime() == null) {
            errors.append("- Starttime\n");
        }
        if (currentRun.getTrnlifecyclestage() == null || currentRun.getTrnlifecyclestage().trim().isEmpty()) {
            errors.append("- Lifecyclestage\n");
        }
        if (currentRun.getTrnlifecyclestage() != null && currentRun.getTrnlifecyclestage().length() > 50) {
            errors.append("- Lifecyclestage no puede exceder 50 caracteres\n");
        }
        if (currentRun.getTrncreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentRun.getTrnupdatedat() == null) {
            errors.append("- Updated At\n");
        }

        if (errors.length() > 0) {
            Messagebox.show("Por favor complete los siguientes campos:\n" + errors.toString(),
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }

        return true;
    }

    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición");
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", idxrun);
        params.put("action", Action.LOAD);
        appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadTrnstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableTrnstatuss.add("OPTION_1");
        availableTrnstatuss.add("OPTION_2");
        availableTrnstatuss.add("OPTION_3");
    }

    private void loadTrngovernancestatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableTrngovernancestatuss.add("OPTION_1");
        availableTrngovernancestatuss.add("OPTION_2");
        availableTrngovernancestatuss.add("OPTION_3");
    }

    private void loadTrnruntypes() {
        // TODO: Cargar valores desde configuración o BD
        availableTrnruntypes.add("OPTION_1");
        availableTrnruntypes.add("OPTION_2");
        availableTrnruntypes.add("OPTION_3");
    }

    @Command
    @NotifyChange("{'selectedTrntags', 'currentRun'}")
    public void addTrntag() {
        if (newTrntag != null && !newTrntag.trim().isEmpty() && !selectedTrntags.contains(newTrntag.trim())) {
            selectedTrntags.add(newTrntag.trim());
            newTrntag = "";
            // Convertir lista a JSON y actualizar en currentRun
            currentRun.setTrntags(convertListToJson(selectedTrntags));
        }
    }

    @Command
    @NotifyChange("{'selectedTrntags', 'currentRun'}")
    public void removeTrntag(@BindingParam("tag") String tag) {
        selectedTrntags.remove(tag);
        // Convertir lista a JSON y actualizar en currentRun
        currentRun.setTrntags(convertListToJson(selectedTrntags));
    }

    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) json.append(",");
            json.append("\"").append(list.get(i)).append("\"");
        }
        json.append("]");
        return json.toString();
    }

    private List<String> convertJsonToList(String json) {
        List<String> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return result;
        }
        // Simplificación: parseo básico de JSON array de strings
        String cleaned = json.replace("[", "").replace("]", "").replace("\"", "");
        if (!cleaned.isEmpty()) {
            for (String item : cleaned.split(",")) {
                result.add(item.trim());
            }
        }
        return result;
    }

    private void loadSubtrncheckpoints() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<Checkpoint> result = checkpointService.findAll(collectionParams, criterias);
                subtrncheckpoints = result != null ? result.getContent() : new ArrayList<>();
                subtrncheckpointsLoaded = true;
                log.debug("Cargados {} subtrncheckpoints", subtrncheckpoints.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrncheckpoints", e);
            subtrncheckpoints = new ArrayList<>();
        }
    }

    private void loadSubtrnenvironments() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<Environment> result = environmentService.findAll(collectionParams, criterias);
                subtrnenvironments = result != null ? result.getContent() : new ArrayList<>();
                subtrnenvironmentsLoaded = true;
                log.debug("Cargados {} subtrnenvironments", subtrnenvironments.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrnenvironments", e);
            subtrnenvironments = new ArrayList<>();
        }
    }

    private void loadSubtrnhpotrials() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<HPOTrial> result = hpoTrialService.findAll(collectionParams, criterias);
                subtrnhpotrials = result != null ? result.getContent() : new ArrayList<>();
                subtrnhpotrialsLoaded = true;
                log.debug("Cargados {} subtrnhpotrials", subtrnhpotrials.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrnhpotrials", e);
            subtrnhpotrials = new ArrayList<>();
        }
    }

    private void loadSubtrnmetricseries() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<MetricSeries> result = metricSeriesService.findAll(collectionParams, criterias);
                subtrnmetricseries = result != null ? result.getContent() : new ArrayList<>();
                subtrnmetricseriesLoaded = true;
                log.debug("Cargados {} subtrnmetricseries", subtrnmetricseries.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrnmetricseries", e);
            subtrnmetricseries = new ArrayList<>();
        }
    }

    private void loadSubtrnmetricstreams() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<MetricStream> result = metricStreamService.findAll(collectionParams, criterias);
                subtrnmetricstreams = result != null ? result.getContent() : new ArrayList<>();
                subtrnmetricstreamsLoaded = true;
                log.debug("Cargados {} subtrnmetricstreams", subtrnmetricstreams.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrnmetricstreams", e);
            subtrnmetricstreams = new ArrayList<>();
        }
    }

    private void loadSubtrnparams() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<Param> result = paramService.findAll(collectionParams, criterias);
                subtrnparams = result != null ? result.getContent() : new ArrayList<>();
                subtrnparamsLoaded = true;
                log.debug("Cargados {} subtrnparams", subtrnparams.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrnparams", e);
            subtrnparams = new ArrayList<>();
        }
    }

    private void loadSubtrnruns() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "parentRun");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<Run> result = runService.findAll(collectionParams, criterias);
                subtrnruns = result != null ? result.getContent() : new ArrayList<>();
                subtrnrunsLoaded = true;
                log.debug("Cargados {} subtrnruns", subtrnruns.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrnruns", e);
            subtrnruns = new ArrayList<>();
        }
    }

    private void loadSubtrntags() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<Tag> result = tagService.findAll(collectionParams, criterias);
                subtrntags = result != null ? result.getContent() : new ArrayList<>();
                subtrntagsLoaded = true;
                log.debug("Cargados {} subtrntags", subtrntags.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrntags", e);
            subtrntags = new ArrayList<>();
        }
    }

    private void loadSubtrntrainingalerts() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<TrainingAlert> result = trainingAlertService.findAll(collectionParams, criterias);
                subtrntrainingalerts = result != null ? result.getContent() : new ArrayList<>();
                subtrntrainingalertsLoaded = true;
                log.debug("Cargados {} subtrntrainingalerts", subtrntrainingalerts.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrntrainingalerts", e);
            subtrntrainingalerts = new ArrayList<>();
        }
    }

    private void loadSubtrntrainingartifacts() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<TrainingArtifact> result = trainingArtifactService.findAll(collectionParams, criterias);
                subtrntrainingartifacts = result != null ? result.getContent() : new ArrayList<>();
                subtrntrainingartifactsLoaded = true;
                log.debug("Cargados {} subtrntrainingartifacts", subtrntrainingartifacts.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrntrainingartifacts", e);
            subtrntrainingartifacts = new ArrayList<>();
        }
    }

    private void loadSubtrntraininglogs() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<TrainingLog> result = trainingLogService.findAll(collectionParams, criterias);
                subtrntraininglogs = result != null ? result.getContent() : new ArrayList<>();
                subtrntraininglogsLoaded = true;
                log.debug("Cargados {} subtrntraininglogs", subtrntraininglogs.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrntraininglogs", e);
            subtrntraininglogs = new ArrayList<>();
        }
    }

    private void loadSubtrntrainingmetrics() {
        try {
            if (currentRun != null && currentRun.getIdxrun() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "run");
                criteria.setValues(new Object[]{currentRun.getIdxrun()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<TrainingMetric> result = trainingMetricService.findAll(collectionParams, criterias);
                subtrntrainingmetrics = result != null ? result.getContent() : new ArrayList<>();
                subtrntrainingmetricsLoaded = true;
                log.debug("Cargados {} subtrntrainingmetrics", subtrntrainingmetrics.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subtrntrainingmetrics", e);
            subtrntrainingmetrics = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subtrncheckpoints")
    public void onSelectSubtrncheckpointsTab() {
        if (!subtrncheckpointsLoaded) {
            loadSubtrncheckpoints();
        }
    }

    @Command
    @NotifyChange("subtrnenvironments")
    public void onSelectSubtrnenvironmentsTab() {
        if (!subtrnenvironmentsLoaded) {
            loadSubtrnenvironments();
        }
    }

    @Command
    @NotifyChange("subtrnhpotrials")
    public void onSelectSubtrnhpotrialsTab() {
        if (!subtrnhpotrialsLoaded) {
            loadSubtrnhpotrials();
        }
    }

    @Command
    @NotifyChange("subtrnmetricseries")
    public void onSelectSubtrnmetricseriesTab() {
        if (!subtrnmetricseriesLoaded) {
            loadSubtrnmetricseries();
        }
    }

    @Command
    @NotifyChange("subtrnmetricstreams")
    public void onSelectSubtrnmetricstreamsTab() {
        if (!subtrnmetricstreamsLoaded) {
            loadSubtrnmetricstreams();
        }
    }

    @Command
    @NotifyChange("subtrnparams")
    public void onSelectSubtrnparamsTab() {
        if (!subtrnparamsLoaded) {
            loadSubtrnparams();
        }
    }

    @Command
    @NotifyChange("subtrnruns")
    public void onSelectSubtrnrunsTab() {
        if (!subtrnrunsLoaded) {
            loadSubtrnruns();
        }
    }

    @Command
    @NotifyChange("subtrntags")
    public void onSelectSubtrntagsTab() {
        if (!subtrntagsLoaded) {
            loadSubtrntags();
        }
    }

    @Command
    @NotifyChange("subtrntrainingalerts")
    public void onSelectSubtrntrainingalertsTab() {
        if (!subtrntrainingalertsLoaded) {
            loadSubtrntrainingalerts();
        }
    }

    @Command
    @NotifyChange("subtrntrainingartifacts")
    public void onSelectSubtrntrainingartifactsTab() {
        if (!subtrntrainingartifactsLoaded) {
            loadSubtrntrainingartifacts();
        }
    }

    @Command
    @NotifyChange("subtrntraininglogs")
    public void onSelectSubtrntraininglogsTab() {
        if (!subtrntraininglogsLoaded) {
            loadSubtrntraininglogs();
        }
    }

    @Command
    @NotifyChange("subtrntrainingmetrics")
    public void onSelectSubtrntrainingmetricsTab() {
        if (!subtrntrainingmetricsLoaded) {
            loadSubtrntrainingmetrics();
        }
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelo XXXX
     * @throws DaoException
     * @throws UiException
     */
    private void logActivity(String action, String model, Long pk, String mensaje) throws DaoException, UiException {
        try {
            Ssoractividad log = new Ssoractividad();
            log.setUsername(getUser().getUsername());
            log.setAccion(action);
            log.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            log.setModulo(model);
            log.setIdtupla(pk != null ? pk.intValue() : 0);
            log.setAplicacion(ctxBean.getApplicationName());
            log.setValuetupla(mensaje);
            businessService.save(log);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
            // No lanzar excepción para que no interrumpa el flujo normal
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
            // Limpiar entidad actual
            currentRun = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableTrnstatuss != null) {
                availableTrnstatuss.clear();
                availableTrnstatuss = null;
            }
            if (availableTrngovernancestatuss != null) {
                availableTrngovernancestatuss.clear();
                availableTrngovernancestatuss = null;
            }
            if (availableTrnruntypes != null) {
                availableTrnruntypes.clear();
                availableTrnruntypes = null;
            }

            // Limpiar colecciones @OneToMany
            if (subtrncheckpoints != null) {
                subtrncheckpoints.clear();
                subtrncheckpoints = null;
            }
            subtrncheckpointsLoaded = false;
            if (subtrnenvironments != null) {
                subtrnenvironments.clear();
                subtrnenvironments = null;
            }
            subtrnenvironmentsLoaded = false;
            if (subtrnhpotrials != null) {
                subtrnhpotrials.clear();
                subtrnhpotrials = null;
            }
            subtrnhpotrialsLoaded = false;
            if (subtrnmetricseries != null) {
                subtrnmetricseries.clear();
                subtrnmetricseries = null;
            }
            subtrnmetricseriesLoaded = false;
            if (subtrnmetricstreams != null) {
                subtrnmetricstreams.clear();
                subtrnmetricstreams = null;
            }
            subtrnmetricstreamsLoaded = false;
            if (subtrnparams != null) {
                subtrnparams.clear();
                subtrnparams = null;
            }
            subtrnparamsLoaded = false;
            if (subtrnruns != null) {
                subtrnruns.clear();
                subtrnruns = null;
            }
            subtrnrunsLoaded = false;
            if (subtrntags != null) {
                subtrntags.clear();
                subtrntags = null;
            }
            subtrntagsLoaded = false;
            if (subtrntrainingalerts != null) {
                subtrntrainingalerts.clear();
                subtrntrainingalerts = null;
            }
            subtrntrainingalertsLoaded = false;
            if (subtrntrainingartifacts != null) {
                subtrntrainingartifacts.clear();
                subtrntrainingartifacts = null;
            }
            subtrntrainingartifactsLoaded = false;
            if (subtrntraininglogs != null) {
                subtrntraininglogs.clear();
                subtrntraininglogs = null;
            }
            subtrntraininglogsLoaded = false;
            if (subtrntrainingmetrics != null) {
                subtrntrainingmetrics.clear();
                subtrntrainingmetrics = null;
            }
            subtrntrainingmetricsLoaded = false;

            // Limpiar tags/roles JSONB
            if (selectedTrntags != null) {
                selectedTrntags.clear();
                selectedTrntags = null;
            }
            newTrntag = null;

            // Limpiar validadores
            unique = null;

            // Limpiar BusinessService
            businessService = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
