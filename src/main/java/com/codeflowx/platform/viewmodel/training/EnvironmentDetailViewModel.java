package com.codeflowx.platform.viewmodel.training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.suinsit.Context;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Environment
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class EnvironmentDetailViewModel extends MasterPage {
    
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
    private Long idxenvironment;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private com.codeflowx.govern.entity.training.Environment currentEnvironment;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableTrncputypes = new ArrayList<>();
    private List<String> availableTrngputypes = new ArrayList<>();
    private List<String> availableTrnframeworks = new ArrayList<>();
    private List<String> availableTrnframeworkversions = new ArrayList<>();
    private List<String> availableTrnframeworkbackends = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxenvironment = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando EnvironmentDetailViewModel - mode: {}, idxenvironment: {}", mode, idxenvironment);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxenvironment != null) {
            loadItem(idxenvironment);
        } else {
            log.error("Modo inválido o falta idxenvironment");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentEnvironment, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentEnvironment = new com.codeflowx.govern.entity.training.Environment();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadTrncputypes();
        loadTrngputypes();
        loadTrnframeworks();
        loadTrnframeworkversions();
        loadTrnframeworkbackends();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentEnvironment = businessService.findById(Environment.class, id);
            
            if (currentEnvironment == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentEnvironment.getIdxenvironment();
        loadTrncputypes();
        loadTrngputypes();
        loadTrnframeworks();
        loadTrnframeworkversions();
        loadTrnframeworkbackends();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            
            // Auditar carga de registro
            logActivity("CONSULTA", "TRNENVIRONMENTS", id, "Consulta: " + currentEnvironment.getIdxenvironment());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentEnvironment.getIdxenvironment() == null;
            
            if (isNew) {
                businessService.save(currentEnvironment);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "TRNENVIRONMENTS", currentEnvironment.getIdxenvironment(), 
                    "Creado: " + currentEnvironment.getIdxenvironment());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentEnvironment);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "TRNENVIRONMENTS", currentEnvironment.getIdxenvironment(), 
                    "Actualizado: " + currentEnvironment.getIdxenvironment());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentEnvironment.getTrnpythonversion() == null || currentEnvironment.getTrnpythonversion().trim().isEmpty()) {
            errors.append("- Pythonversion\n");
        }
        if (currentEnvironment.getTrnpythonversion() != null && currentEnvironment.getTrnpythonversion().length() > 50) {
            errors.append("- Pythonversion no puede exceder 50 caracteres\n");
        }
        if (currentEnvironment.getTrncreatedat() == null) {
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
        params.put("dataParam", idxenvironment);
        params.put("action", Action.LOAD);
        appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadTrncputypes() {
        // TODO: Cargar valores desde configuración o BD
        availableTrncputypes.add("OPTION_1");
        availableTrncputypes.add("OPTION_2");
        availableTrncputypes.add("OPTION_3");
    }
    
    private void loadTrngputypes() {
        // TODO: Cargar valores desde configuración o BD
        availableTrngputypes.add("OPTION_1");
        availableTrngputypes.add("OPTION_2");
        availableTrngputypes.add("OPTION_3");
    }
    
    private void loadTrnframeworks() {
        // TODO: Cargar valores desde configuración o BD
        availableTrnframeworks.add("OPTION_1");
        availableTrnframeworks.add("OPTION_2");
        availableTrnframeworks.add("OPTION_3");
    }
    
    private void loadTrnframeworkversions() {
        // TODO: Cargar valores desde configuración o BD
        availableTrnframeworkversions.add("OPTION_1");
        availableTrnframeworkversions.add("OPTION_2");
        availableTrnframeworkversions.add("OPTION_3");
    }
    
    private void loadTrnframeworkbackends() {
        // TODO: Cargar valores desde configuración o BD
        availableTrnframeworkbackends.add("OPTION_1");
        availableTrnframeworkbackends.add("OPTION_2");
        availableTrnframeworkbackends.add("OPTION_3");
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
            currentEnvironment = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableTrncputypes != null) {
                availableTrncputypes.clear();
                availableTrncputypes = null;
            }
            if (availableTrngputypes != null) {
                availableTrngputypes.clear();
                availableTrngputypes = null;
            }
            if (availableTrnframeworks != null) {
                availableTrnframeworks.clear();
                availableTrnframeworks = null;
            }
            if (availableTrnframeworkversions != null) {
                availableTrnframeworkversions.clear();
                availableTrnframeworkversions = null;
            }
            if (availableTrnframeworkbackends != null) {
                availableTrnframeworkbackends.clear();
                availableTrnframeworkbackends = null;
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
