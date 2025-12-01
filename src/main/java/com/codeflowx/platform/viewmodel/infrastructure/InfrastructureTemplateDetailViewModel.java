package com.codeflowx.platform.viewmodel.infrastructure;
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
import com.codeflowx.govern.entity.infrastructure.InfrastructureTemplate;
import com.codeflowx.govern.service.infrastructure.InfrastructureTemplateService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de InfrastructureTemplate
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class InfrastructureTemplateDetailViewModel extends BaseFront<InfrastructureTemplateDetailViewModel>{

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private InfrastructureTemplateService infrastructureTemplateService;

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
    private Long idxinfrastructuretemplate;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private InfrastructureTemplate currentInfrastructureTemplate;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalInftemplatename = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableInftemplatetypes = new ArrayList<>();

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
            idxinfrastructuretemplate = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando InfrastructureTemplateDetailViewModel - mode: {}, idxinfrastructuretemplate: {}", mode, idxinfrastructuretemplate);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxinfrastructuretemplate != null) {
            loadItem(idxinfrastructuretemplate);
        } else {
            log.error("Modo inválido o falta idxinfrastructuretemplate");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentInfrastructureTemplate, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentInfrastructureTemplate = new InfrastructureTemplate();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadInftemplatetypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentInfrastructureTemplate = infrastructureTemplateService.findById(id);

            if (currentInfrastructureTemplate == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentInfrastructureTemplate.getInftemplatename();
        loadInftemplatetypes();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalInftemplatename = currentInfrastructureTemplate.getInftemplatename();

            // Auditar carga de registro
            logActivity("CONSULTA", "INFTEMPLATES", id, "Consulta: " + currentInfrastructureTemplate.getInftemplatename());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentInfrastructureTemplate.getIdxinfrastructuretemplate() == null;

            if (isNew) {
                currentInfrastructureTemplate = infrastructureTemplateService.create(currentInfrastructureTemplate);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "INFTEMPLATES", currentInfrastructureTemplate.getIdxinfrastructuretemplate(),
                    "Creado: " + currentInfrastructureTemplate.getInftemplatename());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentInfrastructureTemplate = infrastructureTemplateService.update(currentInfrastructureTemplate);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "INFTEMPLATES", currentInfrastructureTemplate.getIdxinfrastructuretemplate(),
                    "Actualizado: " + currentInfrastructureTemplate.getInftemplatename());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentInfrastructureTemplate.getInftemplatename() == null || currentInfrastructureTemplate.getInftemplatename().trim().isEmpty()) {
            errors.append("- Templatename\n");
        }
        if (currentInfrastructureTemplate.getInftemplatename() != null && currentInfrastructureTemplate.getInftemplatename().length() > 255) {
            errors.append("- Templatename no puede exceder 255 caracteres\n");
        }
        if (currentInfrastructureTemplate.getInftemplatecreatedat() == null) {
            errors.append("- Templatecreatedat\n");
        }
        if (currentInfrastructureTemplate.getInftemplateupdatedat() == null) {
            errors.append("- Templateupdatedat\n");
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
        params.put("dataParam", idxinfrastructuretemplate);
        params.put("action", Action.LOAD);
        appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadInftemplatetypes() {
        // TODO: Cargar valores desde configuración o BD
        availableInftemplatetypes.add("OPTION_1");
        availableInftemplatetypes.add("OPTION_2");
        availableInftemplatetypes.add("OPTION_3");
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha crea        // No lanzar excepción para que no interrumpa el flujo normal
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
            currentInfrastructureTemplate = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableInftemplatetypes != null) {
                availableInftemplatetypes.clear();
                availableInftemplatetypes = null;
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
