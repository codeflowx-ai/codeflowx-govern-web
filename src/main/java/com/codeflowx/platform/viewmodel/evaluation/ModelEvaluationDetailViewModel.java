package com.codeflowx.platform.viewmodel.evaluation;

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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.evaluation.ModelEvaluation;
import com.codeflowx.govern.entity.evaluation.EvaluationMetric;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.evaluation.ModelEvaluationService;
import com.codeflowx.govern.service.evaluation.EvaluationMetricService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModelEvaluation
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelEvaluationDetailViewModel extends MasterPage {

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private ModelEvaluationService modelEvaluationService;

    @WireVariable
    private EvaluationMetricService evaluationMetricService;

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
    private Long idxmodelevaluation;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ModelEvaluation currentModelEvaluation;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalEvaluationname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableEvaluationtypes = new ArrayList<>();
    private List<String> availableEvaluationframeworks = new ArrayList<>();
    private List<String> availableStatuss = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<EvaluationMetric> subgovevaluationmetrics = new ArrayList<>();
    private boolean subgovevaluationmetricsLoaded = false;

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
            idxmodelevaluation = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ModelEvaluationDetailViewModel - mode: {}, idxmodelevaluation: {}", mode, idxmodelevaluation);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmodelevaluation != null) {
            loadItem(idxmodelevaluation);
        } else {
            log.error("Modo inválido o falta idxmodelevaluation");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModelEvaluation, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModelEvaluation = new ModelEvaluation();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadEvaluationtypes();
        loadEvaluationframeworks();
        loadStatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentModelEvaluation = modelEvaluationService.findById(id);

            if (currentModelEvaluation == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentModelEvaluation.getEvaluationname();
        loadEvaluationtypes();
        loadEvaluationframeworks();
        loadStatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalEvaluationname = currentModelEvaluation.getEvaluationname();

            // Auditar carga de registro
            logActivity("CONSULTA", "GOVMODELEVALUATIONS", id, "Consulta: " + currentModelEvaluation.getEvaluationname());

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

            boolean isNew = currentModelEvaluation.getIdxmodelevaluation() == null;

            if (isNew) {
                currentModelEvaluation = modelEvaluationService.create(currentModelEvaluation);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "GOVMODELEVALUATIONS", currentModelEvaluation.getIdxmodelevaluation(),
                    "Creado: " + currentModelEvaluation.getEvaluationname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentModelEvaluation = modelEvaluationService.update(currentModelEvaluation);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "GOVMODELEVALUATIONS", currentModelEvaluation.getIdxmodelevaluation(),
                    "Actualizado: " + currentModelEvaluation.getEvaluationname());
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

        if (currentModelEvaluation.getModelid() == null) {
            errors.append("- Model Id\n");
        }
        if (currentModelEvaluation.getEvaluationname() == null || currentModelEvaluation.getEvaluationname().trim().isEmpty()) {
            errors.append("- Luationname\n");
        }
        if (currentModelEvaluation.getEvaluationname() != null && currentModelEvaluation.getEvaluationname().length() > 100) {
            errors.append("- Luationname no puede exceder 100 caracteres\n");
        }
        if (currentModelEvaluation.getEvaluationtype() == null || currentModelEvaluation.getEvaluationtype().trim().isEmpty()) {
            errors.append("- Luationtype\n");
        }
        if (currentModelEvaluation.getEvaluationframework() == null || currentModelEvaluation.getEvaluationframework().trim().isEmpty()) {
            errors.append("- Luationframework\n");
        }
        if (currentModelEvaluation.getStatus() == null || currentModelEvaluation.getStatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentModelEvaluation.getCreatedby() == null) {
            errors.append("- Created By\n");
        }
        if (currentModelEvaluation.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentModelEvaluation.getUpdatedat() == null) {
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
        params.put("dataParam", idxmodelevaluation);
        params.put("action", Action.LOAD);
        appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadEvaluationtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableEvaluationtypes.add("OPTION_1");
        availableEvaluationtypes.add("OPTION_2");
        availableEvaluationtypes.add("OPTION_3");
    }

    private void loadEvaluationframeworks() {
        // TODO: Cargar valores desde configuración o BD
        availableEvaluationframeworks.add("OPTION_1");
        availableEvaluationframeworks.add("OPTION_2");
        availableEvaluationframeworks.add("OPTION_3");
    }

    private void loadStatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableStatuss.add("OPTION_1");
        availableStatuss.add("OPTION_2");
        availableStatuss.add("OPTION_3");
    }

    private void loadSubgovevaluationmetrics() {
        try {
            if (currentModelEvaluation != null && currentModelEvaluation.getIdxmodelevaluation() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "evaluation");
                criteria.setValues(new Object[]{currentModelEvaluation.getIdxmodelevaluation()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<EvaluationMetric> result = evaluationMetricService.findAll(collectionParams, criterias);
                subgovevaluationmetrics = result != null ? result.getContent() : new ArrayList<>();
                subgovevaluationmetricsLoaded = true;
                log.debug("Cargados {} subgovevaluationmetrics", subgovevaluationmetrics.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovevaluationmetrics", e);
            subgovevaluationmetrics = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subgovevaluationmetrics")
    public void onSelectSubgovevaluationmetricsTab() {
        if (!subgovevaluationmetricsLoaded) {
            loadSubgovevaluationmetrics();
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
            currentModelEvaluation = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableEvaluationtypes != null) {
                availableEvaluationtypes.clear();
                availableEvaluationtypes = null;
            }
            if (availableEvaluationframeworks != null) {
                availableEvaluationframeworks.clear();
                availableEvaluationframeworks = null;
            }
            if (availableStatuss != null) {
                availableStatuss.clear();
                availableStatuss = null;
            }

            // Limpiar colecciones @OneToMany
            if (subgovevaluationmetrics != null) {
                subgovevaluationmetrics.clear();
                subgovevaluationmetrics = null;
            }
            subgovevaluationmetricsLoaded = false;

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
