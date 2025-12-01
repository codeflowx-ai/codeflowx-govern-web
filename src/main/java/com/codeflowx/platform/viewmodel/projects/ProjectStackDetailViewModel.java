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
import com.codeflowx.govern.entity.projects.ProjectStack;
import com.codeflowx.govern.service.projects.ProjectStackService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ProjectStack
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProjectStackDetailViewModel extends BaseFront<ProjectStackDetailViewModel>{

    @WireVariable
    private ProjectStackService projectStackService;

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
    private Long idxprojectstack;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ProjectStack currentProjectStack;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalStackname = null;

    // ========== Listas para combos (FK) ==========

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
            idxprojectstack = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ProjectStackDetailViewModel - mode: {}, idxprojectstack: {}", mode, idxprojectstack);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxprojectstack != null) {
            loadItem(idxprojectstack);
        } else {
            log.error("Modo inválido o falta idxprojectstack");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentProjectStack, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentProjectStack = new ProjectStack();
        editing = false;
        pageTitle = "Crear Nuevo";
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentProjectStack = projectStackService.findById(id);

            if (currentProjectStack == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentProjectStack.getStackname();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalStackname = currentProjectStack.getStackname();

            // Auditar carga de registro
            logActivity("CONSULTA", "PRJPROJECTSTACKS", id, "Consulta: " + currentProjectStack.getStackname());

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

            boolean isNew = currentProjectStack.getIdxprojectstack() == null;

            if (isNew) {
                currentProjectStack = projectStackService.create(currentProjectStack);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PRJPROJECTSTACKS", currentProjectStack.getIdxprojectstack(),
                    "Creado: " + currentProjectStack.getStackname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentProjectStack = projectStackService.update(currentProjectStack);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PRJPROJECTSTACKS", currentProjectStack.getIdxprojectstack(),
                    "Actualizado: " + currentProjectStack.getStackname());
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

        if (currentProjectStack.getStackname() == null || currentProjectStack.getStackname().trim().isEmpty()) {
            errors.append("- Stackname\n");
        }
        if (currentProjectStack.getStackname() != null && currentProjectStack.getStackname().length() > 100) {
            errors.append("- Stackname no puede exceder 100 caracteres\n");
        }
        if (currentProjectStack.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentProjectStack.getUpdatedat() == null) {
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
        params.put("dataParam", idxprojectstack);
        params.put("action", Action.LOAD);
        appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el mode No lanzar excepción para que no interrumpa el flujo normal
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
            currentProjectStack = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING

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
