package com.codeflowx.platform.viewmodel.evaluation;
import com.codeflowx.framework.zkoss.BaseFront;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.enartframework.suinsit.Context;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.web.annotation.Action;
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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.evaluation.BiasAnalysis;
import com.codeflowx.govern.entity.evaluation.BiasDetection;
import com.codeflowx.govern.entity.evaluation.BiasRecommendation;
import com.codeflowx.govern.entity.evaluation.FairnessMetric;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.evaluation.BiasAnalysisService;
import com.codeflowx.govern.service.evaluation.BiasDetectionService;
import com.codeflowx.govern.service.evaluation.BiasRecommendationService;
import com.codeflowx.govern.service.evaluation.FairnessMetricService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import org.enartframework.orm.exception.DaoException;
import org.zkoss.zk.ui.UiException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de BiasAnalysis
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class BiasAnalysisDetailViewModel extends BaseFront<BiasAnalysisDetailViewModel>{

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private BiasAnalysisService biasAnalysisService;

    @WireVariable
    private BiasDetectionService biasDetectionService;

    @WireVariable
    private BiasRecommendationService biasRecommendationService;

    @WireVariable
    private FairnessMetricService fairnessMetricService;

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
    private Long idxbiasanalysis;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private BiasAnalysis currentBiasAnalysis;

    // ========== Validadores ==========
    private UniqueValidator unique;


    // ========== Listas para combos (FK) ==========
    private List<String> availableStatuss = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<BiasDetection> subgovbiasdetections = new ArrayList<>();
    private List<BiasRecommendation> subgovbiasrecommendations = new ArrayList<>();
    private List<FairnessMetric> subgovfairnessmetrics = new ArrayList<>();
    private boolean subgovbiasdetectionsLoaded = false;
    private boolean subgovbiasrecommendationsLoaded = false;
    private boolean subgovfairnessmetricsLoaded = false;

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
            idxbiasanalysis = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando BiasAnalysisDetailViewModel - mode: {}, idxbiasanalysis: {}", mode, idxbiasanalysis);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxbiasanalysis != null) {
            loadItem(idxbiasanalysis);
        } else {
            log.error("Modo inválido o falta idxbiasanalysis");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentBiasAnalysis, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentBiasAnalysis = new BiasAnalysis();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadStatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentBiasAnalysis = biasAnalysisService.findById(id);

            if (currentBiasAnalysis == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentBiasAnalysis.getIdxbiasanalysis();
        loadStatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad

            // Auditar carga de registro
            logActivity("CONSULTA", "GOVBIASANALYSIS", id, "Consulta: " + currentBiasAnalysis.getIdxbiasanalysis());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentBiasAnalysis.getIdxbiasanalysis() == null;

            if (isNew) {
                currentBiasAnalysis = biasAnalysisService.create(currentBiasAnalysis);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "GOVBIASANALYSIS", currentBiasAnalysis.getIdxbiasanalysis(),
                    "Creado: " + currentBiasAnalysis.getIdxbiasanalysis());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentBiasAnalysis = biasAnalysisService.update(currentBiasAnalysis);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "GOVBIASANALYSIS", currentBiasAnalysis.getIdxbiasanalysis(),
                    "Actualizado: " + currentBiasAnalysis.getIdxbiasanalysis());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentBiasAnalysis.getAnalysistimestamp() == null) {
            errors.append("- Analysistimestamp\n");
        }
        if (currentBiasAnalysis.getOverallbiasscore() == null) {
            errors.append("- Overall Bias Score\n");
        }
        if (currentBiasAnalysis.getStatus() == null || currentBiasAnalysis.getStatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentBiasAnalysis.getCreatedby() == null || currentBiasAnalysis.getCreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentBiasAnalysis.getCreatedby() != null && currentBiasAnalysis.getCreatedby().length() > 100) {
            errors.append("- Created By no puede exceder 100 caracteres\n");
        }
        if (currentBiasAnalysis.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentBiasAnalysis.getUpdatedat() == null) {
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
        params.put("dataParam", idxbiasanalysis);
        params.put("action", Action.LOAD);
        appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadStatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableStatuss.add("OPTION_1");
        availableStatuss.add("OPTION_2");
        availableStatuss.add("OPTION_3");
    }

    private void loadSubgovbiasdetections() {
        try {
            if (currentBiasAnalysis != null && currentBiasAnalysis.getIdxbiasanalysis() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "analysis");
                criteria.setValues(new Object[]{currentBiasAnalysis.getIdxbiasanalysis()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<BiasDetection> result = biasDetectionService.findAll(collectionParams, criterias);
                subgovbiasdetections = result != null ? result.getContent() : new ArrayList<>();
                subgovbiasdetectionsLoaded = true;
                log.debug("Cargados {} subgovbiasdetections", subgovbiasdetections.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovbiasdetections", e);
            subgovbiasdetections = new ArrayList<>();
        }
    }

    private void loadSubgovbiasrecommendations() {
        try {
            if (currentBiasAnalysis != null && currentBiasAnalysis.getIdxbiasanalysis() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "analysis");
                criteria.setValues(new Object[]{currentBiasAnalysis.getIdxbiasanalysis()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<BiasRecommendation> result = biasRecommendationService.findAll(collectionParams, criterias);
                subgovbiasrecommendations = result != null ? result.getContent() : new ArrayList<>();
                subgovbiasrecommendationsLoaded = true;
                log.debug("Cargados {} subgovbiasrecommendations", subgovbiasrecommendations.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovbiasrecommendations", e);
            subgovbiasrecommendations = new ArrayList<>();
        }
    }

    private void loadSubgovfairnessmetrics() {
        try {
            if (currentBiasAnalysis != null && currentBiasAnalysis.getIdxbiasanalysis() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "analysis");
                criteria.setValues(new Object[]{currentBiasAnalysis.getIdxbiasanalysis()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<FairnessMetric> result = fairnessMetricService.findAll(collectionParams, criterias);
                subgovfairnessmetrics = result != null ? result.getContent() : new ArrayList<>();
                subgovfairnessmetricsLoaded = true;
                log.debug("Cargados {} subgovfairnessmetrics", subgovfairnessmetrics.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovfairnessmetrics", e);
            subgovfairnessmetrics = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subgovbiasdetections")
    public void onSelectSubgovbiasdetectionsTab() {
        if (!subgovbiasdetectionsLoaded) {
            loadSubgovbiasdetections();
        }
    }

    @Command
    @NotifyChange("subgovbiasrecommendations")
    public void onSelectSubgovbiasrecommendationsTab() {
        if (!subgovbiasrecommendationsLoaded) {
            loadSubgovbiasrecommendations();
        }
    }

    @Command
    @NotifyChange("subgovfairnessmetrics")
    public void onSelectSubgovfairnessmetricsTab() {
        if (!subgovfairnessmetricsLoaded) {
            loadSubgovfairnessmetrics();
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
            currentBiasAnalysis = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableStatuss != null) {
                availableStatuss.clear();
                availableStatuss = null;
            }

            // Limpiar colecciones @OneToMany
            if (subgovbiasdetections != null) {
                subgovbiasdetections.clear();
                subgovbiasdetections = null;
            }
            subgovbiasdetectionsLoaded = false;
            if (subgovbiasrecommendations != null) {
                subgovbiasrecommendations.clear();
                subgovbiasrecommendations = null;
            }
            subgovbiasrecommendationsLoaded = false;
            if (subgovfairnessmetrics != null) {
                subgovfairnessmetrics.clear();
                subgovfairnessmetrics = null;
            }
            subgovfairnessmetricsLoaded = false;

            // Limpiar tags/roles JSONB

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
