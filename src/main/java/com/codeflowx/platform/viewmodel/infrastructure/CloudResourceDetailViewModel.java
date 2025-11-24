package com.codeflowx.platform.viewmodel.infrastructure;

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
import com.codeflowx.govern.entity.infrastructure.CloudResource;
import com.codeflowx.govern.service.infrastructure.CloudResourceService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de CloudResource
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class CloudResourceDetailViewModel extends MasterPage {

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private CloudResourceService cloudResourceService;

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
    private Long idxcloudresource;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private CloudResource currentCloudResource;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalInfresname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableInfresresourcetypes = new ArrayList<>();
    private List<String> availableInfreshealthstatuss = new ArrayList<>();

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
            idxcloudresource = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando CloudResourceDetailViewModel - mode: {}, idxcloudresource: {}", mode, idxcloudresource);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxcloudresource != null) {
            loadItem(idxcloudresource);
        } else {
            log.error("Modo inválido o falta idxcloudresource");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentCloudResource, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentCloudResource = new CloudResource();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadInfresresourcetypes();
        loadInfreshealthstatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentCloudResource = cloudResourceService.findById(id);

            if (currentCloudResource == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentCloudResource.getInfresname();
        loadInfresresourcetypes();
        loadInfreshealthstatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalInfresname = currentCloudResource.getInfresname();

            // Auditar carga de registro
            logActivity("CONSULTA", "INFCLOUDRESOURCES", id, "Consulta: " + currentCloudResource.getInfresname());

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

            boolean isNew = currentCloudResource.getIdxcloudresource() == null;

            if (isNew) {
                currentCloudResource = cloudResourceService.create(currentCloudResource);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "INFCLOUDRESOURCES", currentCloudResource.getIdxcloudresource(),
                    "Creado: " + currentCloudResource.getInfresname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentCloudResource = cloudResourceService.update(currentCloudResource);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "INFCLOUDRESOURCES", currentCloudResource.getIdxcloudresource(),
                    "Actualizado: " + currentCloudResource.getInfresname());
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

        if (currentCloudResource.getInfresname() == null || currentCloudResource.getInfresname().trim().isEmpty()) {
            errors.append("- Resname\n");
        }
        if (currentCloudResource.getInfresname() != null && currentCloudResource.getInfresname().length() > 255) {
            errors.append("- Resname no puede exceder 255 caracteres\n");
        }
        if (currentCloudResource.getInfresresourcetype() == null || currentCloudResource.getInfresresourcetype().trim().isEmpty()) {
            errors.append("- Resresourcetype\n");
        }
        if (currentCloudResource.getInfrescreatedat() == null) {
            errors.append("- Rescreatedat\n");
        }
        if (currentCloudResource.getInfresupdatedat() == null) {
            errors.append("- Resupdatedat\n");
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
        params.put("dataParam", idxcloudresource);
        params.put("action", Action.LOAD);
        appendPage("plataforma/infrastructure/infrastructure-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadInfresresourcetypes() {
        // TODO: Cargar valores desde configuración o BD
        availableInfresresourcetypes.add("OPTION_1");
        availableInfresresourcetypes.add("OPTION_2");
        availableInfresresourcetypes.add("OPTION_3");
    }

    private void loadInfreshealthstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableInfreshealthstatuss.add("OPTION_1");
        availableInfreshealthstatuss.add("OPTION_2");
        availableInfreshealthstatuss.add("OPTION_3");
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
            currentCloudResource = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableInfresresourcetypes != null) {
                availableInfresresourcetypes.clear();
                availableInfresresourcetypes = null;
            }
            if (availableInfreshealthstatuss != null) {
                availableInfreshealthstatuss.clear();
                availableInfreshealthstatuss = null;
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
