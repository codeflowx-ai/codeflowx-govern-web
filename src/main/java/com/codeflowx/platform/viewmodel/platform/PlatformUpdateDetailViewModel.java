package com.codeflowx.platform.viewmodel.platform;

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
import com.codeflowx.govern.entity.platform.PlatformUpdate;
import com.codeflowx.govern.entity.platform.UpdateInstallation;
import com.codeflowx.govern.entity.platform.UpdateSchedule;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.platform.PlatformUpdateService;
import com.codeflowx.govern.service.platform.UpdateInstallationService;
import com.codeflowx.govern.service.platform.UpdateScheduleService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de PlatformUpdate
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PlatformUpdateDetailViewModel extends MasterPage {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private PlatformUpdateService platformUpdateService;
    
    @WireVariable
    private UpdateInstallationService updateInstallationService;
    
    @WireVariable
    private UpdateScheduleService updateScheduleService;
    
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
    private Long idxplatformupdate;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private PlatformUpdate currentPlatformUpdate;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    
    // ========== Listas para combos (FK) ==========
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<UpdateInstallation> subupdateinstallations = new ArrayList<>();
    private List<UpdateSchedule> subupdateschedules = new ArrayList<>();
    private boolean subupdateinstallationsLoaded = false;
    private boolean subupdateschedulesLoaded = false;
    
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
            idxplatformupdate = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando PlatformUpdateDetailViewModel - mode: {}, idxplatformupdate: {}", mode, idxplatformupdate);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxplatformupdate != null) {
            loadItem(idxplatformupdate);
        } else {
            log.error("Modo inválido o falta idxplatformupdate");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentPlatformUpdate, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentPlatformUpdate = new PlatformUpdate();
        editing = false;
        pageTitle = "Crear Nuevo";
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentPlatformUpdate = platformUpdateService.findById(id);
            
            if (currentPlatformUpdate == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentPlatformUpdate.getIdxplatformupdate();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            
            // Auditar carga de registro
            logActivity("CONSULTA", "PLATFORMUPDATES", id, "Consulta: " + currentPlatformUpdate.getIdxplatformupdate());
            
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentPlatformUpdate.getIdxplatformupdate() == null;
            
            if (isNew) {
                currentPlatformUpdate = platformUpdateService.create(currentPlatformUpdate);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PLATFORMUPDATES", currentPlatformUpdate.getIdxplatformupdate(), 
                    "Creado: " + currentPlatformUpdate.getIdxplatformupdate());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPlatformUpdate = platformUpdateService.update(currentPlatformUpdate);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PLATFORMUPDATES", currentPlatformUpdate.getIdxplatformupdate(), 
                    "Actualizado: " + currentPlatformUpdate.getIdxplatformupdate());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentPlatformUpdate.getReleasedate() == null) {
            errors.append("- Releasedate\n");
        }
        if (currentPlatformUpdate.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentPlatformUpdate.getUpdatedat() == null) {
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
        params.put("dataParam", idxplatformupdate);
        params.put("action", Action.LOAD);
        appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadSubupdateinstallations() {
        try {
            if (currentPlatformUpdate != null && currentPlatformUpdate.getIdxplatformupdate() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "update");
                criteria.setValues(new Object[]{currentPlatformUpdate.getIdxplatformupdate()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<UpdateInstallation> result = updateInstallationService.findAll(collectionParams, criterias);
                subupdateinstallations = result != null ? result.getContent() : new ArrayList<>();
                subupdateinstallationsLoaded = true;
                log.debug("Cargados {} subupdateinstallations", subupdateinstallations.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subupdateinstallations", e);
            subupdateinstallations = new ArrayList<>();
        }
    }
    
    private void loadSubupdateschedules() {
        try {
            if (currentPlatformUpdate != null && currentPlatformUpdate.getIdxplatformupdate() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "update");
                criteria.setValues(new Object[]{currentPlatformUpdate.getIdxplatformupdate()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<UpdateSchedule> result = updateScheduleService.findAll(collectionParams, criterias);
                subupdateschedules = result != null ? result.getContent() : new ArrayList<>();
                subupdateschedulesLoaded = true;
                log.debug("Cargados {} subupdateschedules", subupdateschedules.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subupdateschedules", e);
            subupdateschedules = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subupdateinstallations")
    public void onSelectSubupdateinstallationsTab() {
        if (!subupdateinstallationsLoaded) {
            loadSubupdateinstallations();
        }
    }
    
    @Command
    @NotifyChange("subupdateschedules")
    public void onSelectSubupdateschedulesTab() {
        if (!subupdateschedulesLoaded) {
            loadSubupdateschedules();
        }
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
            currentPlatformUpdate = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            
            // Limpiar colecciones @OneToMany
            if (subupdateinstallations != null) {
                subupdateinstallations.clear();
                subupdateinstallations = null;
            }
            subupdateinstallationsLoaded = false;
            if (subupdateschedules != null) {
                subupdateschedules.clear();
                subupdateschedules = null;
            }
            subupdateschedulesLoaded = false;
            
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
