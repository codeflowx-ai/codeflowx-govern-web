package com.codeflowx.platform.viewmodel.serving;

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
import com.codeflowx.govern.entity.serving.ServingEndpoint;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.serving.ServingEndpointService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ServingEndpoint
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ServingEndpointDetailViewModel extends MasterPage {

    @WireVariable
    private ServingEndpointService servingEndpointService;

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
    private Long idxservingendpoint;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ServingEndpoint currentServingEndpoint;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalEndpointname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableEndpointtypes = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedAuthorizationroles = new ArrayList<>();
    private String newAuthorizationrole = "";

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
            idxservingendpoint = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ServingEndpointDetailViewModel - mode: {}, idxservingendpoint: {}", mode, idxservingendpoint);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxservingendpoint != null) {
            loadItem(idxservingendpoint);
        } else {
            log.error("Modo inválido o falta idxservingendpoint");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentServingEndpoint, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentServingEndpoint = new ServingEndpoint();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadEndpointtypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentServingEndpoint = servingEndpointService.findById(id);

            if (currentServingEndpoint == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentServingEndpoint.getEndpointname();
        loadEndpointtypes();

            // Cargar tags/roles existentes desde JSON
            selectedAuthorizationroles = convertJsonToList(currentServingEndpoint.getAuthorizationroles());

            // Guardar valores originales para validación de unicidad
            originalEndpointname = currentServingEndpoint.getEndpointname();

            // Auditar carga de registro
            logActivity("CONSULTA", "SRVSERVINGENDPOINTS", id, "Consulta: " + currentServingEndpoint.getEndpointname());

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

            boolean isNew = currentServingEndpoint.getIdxservingendpoint() == null;

            if (isNew) {
                currentServingEndpoint = servingEndpointService.create(currentServingEndpoint);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "SRVSERVINGENDPOINTS", currentServingEndpoint.getIdxservingendpoint(),
                    "Creado: " + currentServingEndpoint.getEndpointname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentServingEndpoint = servingEndpointService.update(currentServingEndpoint);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "SRVSERVINGENDPOINTS", currentServingEndpoint.getIdxservingendpoint(),
                    "Actualizado: " + currentServingEndpoint.getEndpointname());
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

        if (currentServingEndpoint.getEndpointname() == null || currentServingEndpoint.getEndpointname().trim().isEmpty()) {
            errors.append("- Endpoint Name\n");
        }
        if (currentServingEndpoint.getEndpointname() != null && currentServingEndpoint.getEndpointname().length() > 100) {
            errors.append("- Endpoint Name no puede exceder 100 caracteres\n");
        }
        if (currentServingEndpoint.getEndpointpath() == null || currentServingEndpoint.getEndpointpath().trim().isEmpty()) {
            errors.append("- Endpoint Path\n");
        }
        if (currentServingEndpoint.getEndpointpath() != null && currentServingEndpoint.getEndpointpath().length() > 100) {
            errors.append("- Endpoint Path no puede exceder 100 caracteres\n");
        }
        if (currentServingEndpoint.getHttpmethod() == null || currentServingEndpoint.getHttpmethod().trim().isEmpty()) {
            errors.append("- Httpmethod\n");
        }
        if (currentServingEndpoint.getHttpmethod() != null && currentServingEndpoint.getHttpmethod().length() > 100) {
            errors.append("- Httpmethod no puede exceder 100 caracteres\n");
        }
        if (currentServingEndpoint.getEndpointtype() == null || currentServingEndpoint.getEndpointtype().trim().isEmpty()) {
            errors.append("- Endpoint Type\n");
        }
        if (currentServingEndpoint.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentServingEndpoint.getUpdatedat() == null) {
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
        params.put("dataParam", idxservingendpoint);
        params.put("action", Action.LOAD);
        appendPage("plataforma/serving/serving-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadEndpointtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableEndpointtypes.add("OPTION_1");
        availableEndpointtypes.add("OPTION_2");
        availableEndpointtypes.add("OPTION_3");
    }

    @Command
    @NotifyChange("{'selectedAuthorizationroles', 'currentServingEndpoint'}")
    public void addAuthorizationrole() {
        if (newAuthorizationrole != null && !newAuthorizationrole.trim().isEmpty() && !selectedAuthorizationroles.contains(newAuthorizationrole.trim())) {
            selectedAuthorizationroles.add(newAuthorizationrole.trim());
            newAuthorizationrole = "";
            // Convertir lista a JSON y actualizar en currentServingEndpoint
            currentServingEndpoint.setAuthorizationroles(convertListToJson(selectedAuthorizationroles));
        }
    }

    @Command
    @NotifyChange("{'selectedAuthorizationroles', 'currentServingEndpoint'}")
    public void removeAuthorizationrole(@BindingParam("tag") String tag) {
        selectedAuthorizationroles.remove(tag);
        // Convertir lista a JSON y actualizar en currentServingEndpoint
        currentServingEndpoint.setAuthorizationroles(convertListToJson(selectedAuthorizationroles));
    }

    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) json.append(",");
            json.append("\"").append(list.get(i)).append("\"");
        }
        json.append("]");
        return json.toString();
    }

    private List<String> convertJsonToList(String json) {
        List<String> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return result;
        }
        // Simplificación: parseo básico de JSON array de strings
        String cleaned = json.replace("[", "").replace("]", "").replace("\"", "");
        if (!cleaned.isEmpty()) {
            for (String item : cleaned.split(",")) {
                result.add(item.trim());
            }
        }
        return result;
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
            currentServingEndpoint = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableEndpointtypes != null) {
                availableEndpointtypes.clear();
                availableEndpointtypes = null;
            }

            // Limpiar colecciones @OneToMany

            // Limpiar tags/roles JSONB
            if (selectedAuthorizationroles != null) {
                selectedAuthorizationroles.clear();
                selectedAuthorizationroles = null;
            }
            newAuthorizationrole = null;

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
