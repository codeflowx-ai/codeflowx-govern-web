package com.codeflowx.platform.viewmodel.projects;
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
import com.codeflowx.govern.entity.projects.ProjectTask;
import com.codeflowx.govern.service.projects.ProjectTaskService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ProjectTask
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProjectTaskDetailViewModel extends BaseFront<ProjectTaskDetailViewModel>{

    @WireVariable
    private ProjectTaskService projectTaskService;

    @WireVariable
    private BusinessService businessService; // Mantener para auditoría (Ssoractividad) y UniqueValidator

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
    private Long idxprojecttask;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ProjectTask currentProjectTask;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalTaskname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableStatuss = new ArrayList<>();
    private List<String> availablePrioritys = new ArrayList<>();

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
            idxprojecttask = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ProjectTaskDetailViewModel - mode: {}, idxprojecttask: {}", mode, idxprojecttask);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxprojecttask != null) {
            loadItem(idxprojecttask);
        } else {
            log.error("Modo inválido o falta idxprojecttask");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentProjectTask, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentProjectTask = new ProjectTask();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadStatuss();
        loadPrioritys();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentProjectTask = projectTaskService.findById(id);

            if (currentProjectTask == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentProjectTask.getTaskname();
        loadStatuss();
        loadPrioritys();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalTaskname = currentProjectTask.getTaskname();

            // Auditar carga de registro
            logActivity("CONSULTA", "PRJPROJECTTASKS", id, "Consulta: " + currentProjectTask.getTaskname());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentProjectTask.getIdxprojecttask() == null;

            if (isNew) {
                currentProjectTask = projectTaskService.create(currentProjectTask);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PRJPROJECTTASKS", currentProjectTask.getIdxprojecttask(),
                    "Creado: " + currentProjectTask.getTaskname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentProjectTask = projectTaskService.update(currentProjectTask);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PRJPROJECTTASKS", currentProjectTask.getIdxprojecttask(),
                    "Actualizado: " + currentProjectTask.getTaskname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentProjectTask.getTaskname() == null || currentProjectTask.getTaskname().trim().isEmpty()) {
            errors.append("- Taskname\n");
        }
        if (currentProjectTask.getTaskname() != null && currentProjectTask.getTaskname().length() > 100) {
            errors.append("- Taskname no puede exceder 100 caracteres\n");
        }
        if (currentProjectTask.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentProjectTask.getUpdatedat() == null) {
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
        params.put("dataParam", idxprojecttask);
        params.put("action", Action.LOAD);
        appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadStatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableStatuss.add("OPTION_1");
        availableStatuss.add("OPTION_2");
        availableStatuss.add("OPTION_3");
    }

    private void loadPrioritys() {
        // TODO: Cargar valores desde configuración o BD
        availablePrioritys.add("OPTION_1");
        availablePrioritys.add("OPTION_2");
        availablePrioritys.add("OPTION_3");
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelNo lanzar excepción para que no interrumpa el flujo normal
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
            currentProjectTask = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableStatuss != null) {
                availableStatuss.clear();
                availableStatuss = null;
            }
            if (availablePrioritys != null) {
                availablePrioritys.clear();
                availablePrioritys = null;
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
