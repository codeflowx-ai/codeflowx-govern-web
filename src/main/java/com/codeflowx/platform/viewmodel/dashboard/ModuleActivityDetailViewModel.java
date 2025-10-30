package com.codeflowx.platform.viewmodel.dashboard;

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
import com.codeflowx.govern.entity.dashboard.ModuleActivity;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModuleActivity
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModuleActivityDetailViewModel extends MasterPage {
    
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
    private Long idxactivity;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private ModuleActivity currentModuleActivity;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalDshmodulename = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableDshactivitytypes = new ArrayList<>();
    private List<String> availableDshstatuss = new ArrayList<>();
    
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
            idxactivity = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ModuleActivityDetailViewModel - mode: {}, idxactivity: {}", mode, idxactivity);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxactivity != null) {
            loadItem(idxactivity);
        } else {
            log.error("Modo inválido o falta idxactivity");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModuleActivity, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModuleActivity = new ModuleActivity();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadDshactivitytypes();
        loadDshstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentModuleActivity = businessService.findById(ModuleActivity.class, id);
            
            if (currentModuleActivity == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentModuleActivity.getDshmodulename();
        loadDshactivitytypes();
        loadDshstatuss();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalDshmodulename = currentModuleActivity.getDshmodulename();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "DSHMODULEACTIVITY", id, "Consulta: " + currentModuleActivity.getDshmodulename());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentModuleActivity.getIdxactivity() == null;
            
            if (isNew) {
                businessService.save(currentModuleActivity);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "DSHMODULEACTIVITY", currentModuleActivity.getIdxactivity(), 
                    "Creado: " + currentModuleActivity.getDshmodulename());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentModuleActivity);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "DSHMODULEACTIVITY", currentModuleActivity.getIdxactivity(), 
                    "Actualizado: " + currentModuleActivity.getDshmodulename());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
            
        } catch (Exception e) {
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
        
        if (currentModuleActivity.getDshmodulename() == null || currentModuleActivity.getDshmodulename().trim().isEmpty()) {
            errors.append("- Dshmodulename\n");
        }
        if (currentModuleActivity.getDshmodulename() != null && currentModuleActivity.getDshmodulename().length() > 100) {
            errors.append("- Dshmodulename no puede exceder 100 caracteres\n");
        }
        if (currentModuleActivity.getDshactivitytype() == null || currentModuleActivity.getDshactivitytype().trim().isEmpty()) {
            errors.append("- Dshactivitytype\n");
        }
        if (currentModuleActivity.getDshdescription() == null || currentModuleActivity.getDshdescription().trim().isEmpty()) {
            errors.append("- Dshdescription\n");
        }
        if (currentModuleActivity.getDshtimestamp() == null) {
            errors.append("- Dshtimestamp\n");
        }
        if (currentModuleActivity.getDshcreatedat() == null) {
            errors.append("- Dshcreatedat\n");
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
        params.put("dataParam", idxactivity);
        params.put("action", Action.LOAD);
        appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadDshactivitytypes() {
        // TODO: Cargar valores desde configuración o BD
        availableDshactivitytypes.add("OPTION_1");
        availableDshactivitytypes.add("OPTION_2");
        availableDshactivitytypes.add("OPTION_3");
    }
    
    private void loadDshstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableDshstatuss.add("OPTION_1");
        availableDshstatuss.add("OPTION_2");
        availableDshstatuss.add("OPTION_3");
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
            currentModuleActivity = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableDshactivitytypes != null) {
                availableDshactivitytypes.clear();
                availableDshactivitytypes = null;
            }
            if (availableDshstatuss != null) {
                availableDshstatuss.clear();
                availableDshstatuss = null;
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
