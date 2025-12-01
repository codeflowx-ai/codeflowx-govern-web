package com.codeflowx.platform.viewmodel.prompts;
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
import com.codeflowx.govern.entity.prompts.PromptValidation;
import com.codeflowx.govern.service.prompts.PromptValidationService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de PromptValidation
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PromptValidationDetailViewModel extends BaseFront<PromptValidationDetailViewModel>{

    @WireVariable
    private BusinessService businessService;
    @WireVariable
    private PromptValidationService promptValidationService;

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
    private Long idxpromptvalidation;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private PromptValidation currentPromptValidation;

    // ========== Validadores ==========
    private UniqueValidator unique;


    // ========== Listas para combos (FK) ==========
    private List<String> availablePrmvalidationtypes = new ArrayList<>();
    private List<String> availablePrmstatuss = new ArrayList<>();

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
            idxpromptvalidation = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando PromptValidationDetailViewModel - mode: {}, idxpromptvalidation: {}", mode, idxpromptvalidation);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxpromptvalidation != null) {
            loadItem(idxpromptvalidation);
        } else {
            log.error("Modo inválido o falta idxpromptvalidation");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentPromptValidation, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentPromptValidation = new PromptValidation();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadPrmvalidationtypes();
        loadPrmstatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentPromptValidation = promptValidationService.findById(id);

            if (currentPromptValidation == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentPromptValidation.getIdxpromptvalidation();
        loadPrmvalidationtypes();
        loadPrmstatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad

            // Auditar carga de registro
            logActivity("CONSULTA", "PRMPROMPTVALIDATIONS", id, "Consulta: " + currentPromptValidation.getIdxpromptvalidation());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
        } catch (Exception e) {
            log.error("Error inesperado al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentPromptValidation.getIdxpromptvalidation() == null;

            if (isNew) {
                currentPromptValidation = promptValidationService.create(currentPromptValidation);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PRMPROMPTVALIDATIONS", currentPromptValidation.getIdxpromptvalidation(),
                    "Creado: " + currentPromptValidation.getIdxpromptvalidation());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPromptValidation = promptValidationService.update(currentPromptValidation);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PRMPROMPTVALIDATIONS", currentPromptValidation.getIdxpromptvalidation(),
                    "Actualizado: " + currentPromptValidation.getIdxpromptvalidation());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);

        } catch (GovernanceServiceException e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error al guardar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al guardar", e);
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

        if (currentPromptValidation.getPrmvalidationtype() == null || currentPromptValidation.getPrmvalidationtype().trim().isEmpty()) {
            errors.append("- Validationtype\n");
        }
        if (currentPromptValidation.getPrmvalidationresult() == null || currentPromptValidation.getPrmvalidationresult().trim().isEmpty()) {
            errors.append("- Validationresult\n");
        }
        if (currentPromptValidation.getPrmvalidationresult() != null && currentPromptValidation.getPrmvalidationresult().length() > 50) {
            errors.append("- Validationresult no puede exceder 50 caracteres\n");
        }
        if (currentPromptValidation.getPrmstatus() == null || currentPromptValidation.getPrmstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentPromptValidation.getPrmcreatedby() == null || currentPromptValidation.getPrmcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentPromptValidation.getPrmcreatedby() != null && currentPromptValidation.getPrmcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentPromptValidation.getPrmcreatedat() == null) {
            errors.append("- Created At\n");
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
        params.put("dataParam", idxpromptvalidation);
        params.put("action", Action.LOAD);
        appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadPrmvalidationtypes() {
        // TODO: Cargar valores desde configuración o BD
        availablePrmvalidationtypes.add("OPTION_1");
        availablePrmvalidationtypes.add("OPTION_2");
        availablePrmvalidationtypes.add("OPTION_3");
    }

    private void loadPrmstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availablePrmstatuss.add("OPTION_1");
        availablePrmstatuss.add("OPTION_2");
        availablePrmstatuss.add("OPTION_3");
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
            currentPromptValidation = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availablePrmvalidationtypes != null) {
                availablePrmvalidationtypes.clear();
                availablePrmvalidationtypes = null;
            }
            if (availablePrmstatuss != null) {
                availablePrmstatuss.clear();
                availablePrmstatuss = null;
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
