package com.codeflowx.platform.viewmodel.models;

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
import com.codeflowx.govern.entity.models.ModelEndpoint;
import com.codeflowx.govern.service.models.ModelEndpointService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModelEndpoint
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelEndpointDetailViewModel extends MasterPage {

    @WireVariable
    private ModelEndpointService modelEndpointService;
    @WireVariable
    private BusinessService businessService; // Mantener para Ssoractividad y UniqueValidator

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
    private Long idxmodelendpoint;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ModelEndpoint currentModelEndpoint;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalModendpointname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableModendpointtypes = new ArrayList<>();
    private List<String> availableModdeploymenttypes = new ArrayList<>();
    private List<String> availableModstatuss = new ArrayList<>();
    private List<String> availableModhealthstatuss = new ArrayList<>();

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
            idxmodelendpoint = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ModelEndpointDetailViewModel - mode: {}, idxmodelendpoint: {}", mode, idxmodelendpoint);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmodelendpoint != null) {
            loadItem(idxmodelendpoint);
        } else {
            log.error("Modo inválido o falta idxmodelendpoint");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModelEndpoint, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModelEndpoint = new ModelEndpoint();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadModendpointtypes();
        loadModdeploymenttypes();
        loadModstatuss();
        loadModhealthstatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentModelEndpoint = modelEndpointService.findById(id);

            if (currentModelEndpoint == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentModelEndpoint.getModendpointname();
        loadModendpointtypes();
        loadModdeploymenttypes();
        loadModstatuss();
        loadModhealthstatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalModendpointname = currentModelEndpoint.getModendpointname();

            // Auditar carga de registro
            logActivity("CONSULTA", "MODENDPOINTS", id, "Consulta: " + currentModelEndpoint.getModendpointname());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
        } catch (Exception e) {
            log.error("Error inesperado al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentModelEndpoint.getIdxmodelendpoint() == null;

            if (isNew) {
                currentModelEndpoint = modelEndpointService.create(currentModelEndpoint);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "MODENDPOINTS", currentModelEndpoint.getIdxmodelendpoint(),
                    "Creado: " + currentModelEndpoint.getModendpointname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentModelEndpoint = modelEndpointService.update(currentModelEndpoint);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "MODENDPOINTS", currentModelEndpoint.getIdxmodelendpoint(),
                    "Actualizado: " + currentModelEndpoint.getModendpointname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentModelEndpoint.getModendpointname() == null || currentModelEndpoint.getModendpointname().trim().isEmpty()) {
            errors.append("- Endpoint Name\n");
        }
        if (currentModelEndpoint.getModendpointname() != null && currentModelEndpoint.getModendpointname().length() > 255) {
            errors.append("- Endpoint Name no puede exceder 255 caracteres\n");
        }
        if (currentModelEndpoint.getModendpointurl() == null || currentModelEndpoint.getModendpointurl().trim().isEmpty()) {
            errors.append("- Endpoint Url\n");
        }
        if (currentModelEndpoint.getModendpointurl() != null && currentModelEndpoint.getModendpointurl().length() > 500) {
            errors.append("- Endpoint Url no puede exceder 500 caracteres\n");
        }
        if (currentModelEndpoint.getModstatus() == null || currentModelEndpoint.getModstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentModelEndpoint.getModcreatedby() == null || currentModelEndpoint.getModcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentModelEndpoint.getModcreatedby() != null && currentModelEndpoint.getModcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentModelEndpoint.getModcreatedat() == null) {
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
        params.put("dataParam", idxmodelendpoint);
        params.put("action", Action.LOAD);
        appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadModendpointtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableModendpointtypes.add("OPTION_1");
        availableModendpointtypes.add("OPTION_2");
        availableModendpointtypes.add("OPTION_3");
    }

    private void loadModdeploymenttypes() {
        // TODO: Cargar valores desde configuración o BD
        availableModdeploymenttypes.add("OPTION_1");
        availableModdeploymenttypes.add("OPTION_2");
        availableModdeploymenttypes.add("OPTION_3");
    }

    private void loadModstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableModstatuss.add("OPTION_1");
        availableModstatuss.add("OPTION_2");
        availableModstatuss.add("OPTION_3");
    }

    private void loadModhealthstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableModhealthstatuss.add("OPTION_1");
        availableModhealthstatuss.add("OPTION_2");
        availableModhealthstatuss.add("OPTION_3");
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
            currentModelEndpoint = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableModendpointtypes != null) {
                availableModendpointtypes.clear();
                availableModendpointtypes = null;
            }
            if (availableModdeploymenttypes != null) {
                availableModdeploymenttypes.clear();
                availableModdeploymenttypes = null;
            }
            if (availableModstatuss != null) {
                availableModstatuss.clear();
                availableModstatuss = null;
            }
            if (availableModhealthstatuss != null) {
                availableModhealthstatuss.clear();
                availableModhealthstatuss = null;
            }

            // Limpiar colecciones @OneToMany

            // Limpiar tags/roles JSONB

            // Limpiar validadores
            unique = null;

            // Limpiar Servicios
            modelEndpointService = null;
            businessService = null; // Mantener para Ssoractividad y UniqueValidator

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
