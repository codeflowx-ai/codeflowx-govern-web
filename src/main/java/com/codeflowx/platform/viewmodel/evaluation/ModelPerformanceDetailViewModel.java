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
import com.codeflowx.govern.entity.evaluation.ModelPerformance;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.evaluation.ModelPerformanceService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModelPerformance
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelPerformanceDetailViewModel extends BaseFront<ModelPerformanceDetailViewModel>{

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private ModelPerformanceService modelPerformanceService;

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
    private Long idxmodelperformance;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ModelPerformance currentModelPerformance;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalModmetricname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableModdatasettypes = new ArrayList<>();

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
            idxmodelperformance = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ModelPerformanceDetailViewModel - mode: {}, idxmodelperformance: {}", mode, idxmodelperformance);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmodelperformance != null) {
            loadItem(idxmodelperformance);
        } else {
            log.error("Modo inválido o falta idxmodelperformance");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModelPerformance, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModelPerformance = new ModelPerformance();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadModdatasettypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentModelPerformance = modelPerformanceService.findById(id);

            if (currentModelPerformance == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentModelPerformance.getModmetricname();
        loadModdatasettypes();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalModmetricname = currentModelPerformance.getModmetricname();

            // Auditar carga de registro
            logActivity("CONSULTA", "MODMODELPERFORMANCES", id, "Consulta: " + currentModelPerformance.getModmetricname());

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

            boolean isNew = currentModelPerformance.getIdxmodelperformance() == null;

            if (isNew) {
                currentModelPerformance = modelPerformanceService.create(currentModelPerformance);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "MODMODELPERFORMANCES", currentModelPerformance.getIdxmodelperformance(),
                    "Creado: " + currentModelPerformance.getModmetricname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentModelPerformance = modelPerformanceService.update(currentModelPerformance);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "MODMODELPERFORMANCES", currentModelPerformance.getIdxmodelperformance(),
                    "Actualizado: " + currentModelPerformance.getModmetricname());
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

        if (currentModelPerformance.getModmetricname() == null || currentModelPerformance.getModmetricname().trim().isEmpty()) {
            errors.append("- Modmetricname\n");
        }
        if (currentModelPerformance.getModmetricname() != null && currentModelPerformance.getModmetricname().length() > 100) {
            errors.append("- Modmetricname no puede exceder 100 caracteres\n");
        }
        if (currentModelPerformance.getModmetricvalue() == null) {
            errors.append("- Modmetricvalue\n");
        }
        if (currentModelPerformance.getModcreatedby() == null || currentModelPerformance.getModcreatedby().trim().isEmpty()) {
            errors.append("- Modcreatedby\n");
        }
        if (currentModelPerformance.getModcreatedby() != null && currentModelPerformance.getModcreatedby().length() > 255) {
            errors.append("- Modcreatedby no puede exceder 255 caracteres\n");
        }
        if (currentModelPerformance.getModcreatedat() == null) {
            errors.append("- Modcreatedat\n");
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
        params.put("dataParam", idxmodelperformance);
        params.put("action", Action.LOAD);
        appendPage("plataforma/evaluation/evaluation-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadModdatasettypes() {
        // TODO: Cargar valores desde configuración o BD
        availableModdatasettypes.add("OPTION_1");
        availableModdatasettypes.add("OPTION_2");
        availableModdatasettypes.add("OPTION_3");
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
            currentModelPerformance = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableModdatasettypes != null) {
                availableModdatasettypes.clear();
                availableModdatasettypes = null;
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
