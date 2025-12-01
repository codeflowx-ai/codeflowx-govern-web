package com.codeflowx.platform.viewmodel.governance;
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
import com.codeflowx.govern.entity.governance.PolicyEvaluation;
import com.codeflowx.govern.service.governance.PolicyEvaluationService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de PolicyEvaluation
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PolicyEvaluationDetailViewModel extends BaseFront<PolicyEvaluationDetailViewModel>{

    @WireVariable
    private PolicyEvaluationService policyEvaluationService;
    @WireVariable
    private BusinessService businessService; // Mantener para procedimientos almacenados y auditoría

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
    private Long idxpolicyevaluation;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private PolicyEvaluation currentPolicyEvaluation;

    // ========== Validadores ==========
    private UniqueValidator unique;


    // ========== Listas para combos (FK) ==========
    private List<String> availableArtifacttypes = new ArrayList<>();
    private List<String> availableEvaluationtypes = new ArrayList<>();
    private List<String> availableStatuss = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========

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
            idxpolicyevaluation = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando PolicyEvaluationDetailViewModel - mode: {}, idxpolicyevaluation: {}", mode, idxpolicyevaluation);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxpolicyevaluation != null) {
            loadItem(idxpolicyevaluation);
        } else {
            log.error("Modo inválido o falta idxpolicyevaluation");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentPolicyEvaluation, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentPolicyEvaluation = new PolicyEvaluation();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadArtifacttypes();
        loadEvaluationtypes();
        loadStatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentPolicyEvaluation = policyEvaluationService.findById(id);

            if (currentPolicyEvaluation == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentPolicyEvaluation.getArtifactid();
        loadArtifacttypes();
        loadEvaluationtypes();
        loadStatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad

            // Auditar carga de registro
            logActivity("CONSULTA", "GOVPOLICYEVALUATIONS", id, "Consulta: " + currentPolicyEvaluation.getArtifactid());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentPolicyEvaluation.getIdxpolicyevaluation() == null;

            if (isNew) {
                currentPolicyEvaluation = policyEvaluationService.create(currentPolicyEvaluation);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "GOVPOLICYEVALUATIONS", currentPolicyEvaluation.getIdxpolicyevaluation(),
                    "Creado: " + currentPolicyEvaluation.getArtifactid());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPolicyEvaluation = policyEvaluationService.update(currentPolicyEvaluation);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "GOVPOLICYEVALUATIONS", currentPolicyEvaluation.getIdxpolicyevaluation(),
                    "Actualizado: " + currentPolicyEvaluation.getArtifactid());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentPolicyEvaluation.getArtifacttype() == null || currentPolicyEvaluation.getArtifacttype().trim().isEmpty()) {
            errors.append("- Artifact Type\n");
        }
        if (currentPolicyEvaluation.getArtifactid() == null || currentPolicyEvaluation.getArtifactid().trim().isEmpty()) {
            errors.append("- Artifact Id\n");
        }
        if (currentPolicyEvaluation.getArtifactid() != null && currentPolicyEvaluation.getArtifactid().length() > 100) {
            errors.append("- Artifact Id no puede exceder 100 caracteres\n");
        }
        if (currentPolicyEvaluation.getEvaluationtype() == null || currentPolicyEvaluation.getEvaluationtype().trim().isEmpty()) {
            errors.append("- Evaluation Type\n");
        }
        if (currentPolicyEvaluation.getStatus() == null || currentPolicyEvaluation.getStatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentPolicyEvaluation.getEvaluatedat() == null) {
            errors.append("- Evaluatedat\n");
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
        params.put("dataParam", idxpolicyevaluation);
        params.put("action", Action.LOAD);
        appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadArtifacttypes() {
        // TODO: Cargar valores desde configuración o BD
        availableArtifacttypes.add("OPTION_1");
        availableArtifacttypes.add("OPTION_2");
        availableArtifacttypes.add("OPTION_3");
    }

    private void loadEvaluationtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableEvaluationtypes.add("OPTION_1");
        availableEvaluationtypes.add("OPTION_2");
        availableEvaluationtypes.add("OPTION_3");
    }

    private void loadStatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableStatuss.add("OPTION_1");
        availableStatuss.add("OPTION_2");
        availableStatuss.add("OPTION_3");
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el  // No lanzar excepción para que no interrumpa el flujo normal
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
            currentPolicyEvaluation = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableArtifacttypes != null) {
                availableArtifacttypes.clear();
                availableArtifacttypes = null;
            }
            if (availableEvaluationtypes != null) {
                availableEvaluationtypes.clear();
                availableEvaluationtypes = null;
            }
            if (availableStatuss != null) {
                availableStatuss.clear();
                availableStatuss = null;
            }

            // Limpiar colecciones @OneToMany

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
