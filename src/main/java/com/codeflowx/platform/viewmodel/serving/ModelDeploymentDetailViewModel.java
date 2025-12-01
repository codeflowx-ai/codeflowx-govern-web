package com.codeflowx.platform.viewmodel.serving;
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
import com.codeflowx.framework.zkoss.BaseFront;
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
import com.codeflowx.govern.entity.serving.ModelDeployment;
import com.codeflowx.govern.entity.serving.ModelMetrics;
import com.codeflowx.govern.entity.serving.ModelPrediction;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.serving.ModelDeploymentService;
import com.codeflowx.govern.service.serving.ModelMetricsService;
import com.codeflowx.govern.service.serving.ModelPredictionService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModelDeployment
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelDeploymentDetailViewModel extends BaseFront<ModelDeploymentDetailViewModel>{

    @WireVariable
    private ModelDeploymentService modelDeploymentService;

    @WireVariable
    private ModelMetricsService modelMetricsService;

    @WireVariable
    private ModelPredictionService modelPredictionService;

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
    private Long idxdeployment;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ModelDeployment currentModelDeployment;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalSrvdeploymentname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableSrvdeploymentstatuss = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ModelMetrics> subsrvmodelmetrics = new ArrayList<>();
    private List<ModelPrediction> subsrvpredictions = new ArrayList<>();
    private boolean subsrvmodelmetricsLoaded = false;
    private boolean subsrvpredictionsLoaded = false;

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
            idxdeployment = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ModelDeploymentDetailViewModel - mode: {}, idxdeployment: {}", mode, idxdeployment);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxdeployment != null) {
            loadItem(idxdeployment);
        } else {
            log.error("Modo inválido o falta idxdeployment");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModelDeployment, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModelDeployment = new ModelDeployment();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadSrvdeploymentstatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentModelDeployment = modelDeploymentService.findById(id);

            if (currentModelDeployment == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentModelDeployment.getSrvdeploymentname();
        loadSrvdeploymentstatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalSrvdeploymentname = currentModelDeployment.getSrvdeploymentname();

            // Auditar carga de registro
            logActivity("CONSULTA", "SRVDEPLOYMENTS", id, "Consulta: " + currentModelDeployment.getSrvdeploymentname());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentModelDeployment.getIdxdeployment() == null;

            if (isNew) {
                currentModelDeployment = modelDeploymentService.create(currentModelDeployment);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "SRVDEPLOYMENTS", currentModelDeployment.getIdxdeployment(),
                    "Creado: " + currentModelDeployment.getSrvdeploymentname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentModelDeployment = modelDeploymentService.update(currentModelDeployment);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "SRVDEPLOYMENTS", currentModelDeployment.getIdxdeployment(),
                    "Actualizado: " + currentModelDeployment.getSrvdeploymentname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentModelDeployment.getSrvdeploymentname() == null || currentModelDeployment.getSrvdeploymentname().trim().isEmpty()) {
            errors.append("- Srvdeploymentname\n");
        }
        if (currentModelDeployment.getSrvdeploymentname() != null && currentModelDeployment.getSrvdeploymentname().length() > 255) {
            errors.append("- Srvdeploymentname no puede exceder 255 caracteres\n");
        }
        if (currentModelDeployment.getSrvdeploymentstatus() == null || currentModelDeployment.getSrvdeploymentstatus().trim().isEmpty()) {
            errors.append("- Srvdeploymentstatus\n");
        }
        if (currentModelDeployment.getSrvdeploymentcreatedat() == null) {
            errors.append("- Srvdeploymentcreatedat\n");
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
        params.put("dataParam", idxdeployment);
        params.put("action", Action.LOAD);
        appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadSrvdeploymentstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableSrvdeploymentstatuss.add("OPTION_1");
        availableSrvdeploymentstatuss.add("OPTION_2");
        availableSrvdeploymentstatuss.add("OPTION_3");
    }

    private void loadSubsrvmodelmetrics() {
        try {
            if (currentModelDeployment != null && currentModelDeployment.getIdxdeployment() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "deployment");
                criteria.setValues(new Object[]{currentModelDeployment.getIdxdeployment()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<ModelMetrics> result = modelMetricsService.findAll(collectionParams, criterias);
                subsrvmodelmetrics = result != null ? result.getContent() : new ArrayList<>();
                subsrvmodelmetricsLoaded = true;
                log.debug("Cargados {} subsrvmodelmetrics", subsrvmodelmetrics.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subsrvmodelmetrics", e);
            subsrvmodelmetrics = new ArrayList<>();
        }
    }

    private void loadSubsrvpredictions() {
        try {
            if (currentModelDeployment != null && currentModelDeployment.getIdxdeployment() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "deployment");
                criteria.setValues(new Object[]{currentModelDeployment.getIdxdeployment()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<ModelPrediction> result = modelPredictionService.findAll(collectionParams, criterias);
                subsrvpredictions = result != null ? result.getContent() : new ArrayList<>();
                subsrvpredictionsLoaded = true;
                log.debug("Cargados {} subsrvpredictions", subsrvpredictions.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subsrvpredictions", e);
            subsrvpredictions = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subsrvmodelmetrics")
    public void onSelectSubsrvmodelmetricsTab() {
        if (!subsrvmodelmetricsLoaded) {
            loadSubsrvmodelmetrics();
        }
    }

    @Command
    @NotifyChange("subsrvpredictions")
    public void onSelectSubsrvpredictionsTab() {
        if (!subsrvpredictionsLoaded) {
            loadSubsrvpredictions();
        }
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el m // No lanzar excepción para que no interrumpa el flujo normal
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
            currentModelDeployment = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableSrvdeploymentstatuss != null) {
                availableSrvdeploymentstatuss.clear();
                availableSrvdeploymentstatuss = null;
            }

            // Limpiar colecciones @OneToMany
            if (subsrvmodelmetrics != null) {
                subsrvmodelmetrics.clear();
                subsrvmodelmetrics = null;
            }
            subsrvmodelmetricsLoaded = false;
            if (subsrvpredictions != null) {
                subsrvpredictions.clear();
                subsrvpredictions = null;
            }
            subsrvpredictionsLoaded = false;

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
