package com.codeflowx.platform.viewmodel.rag;

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
import com.codeflowx.govern.entity.rag.RagDataSource;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de RagDataSource
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RagDataSourceDetailViewModel extends MasterPage {
    
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
    private Long idxragdatasource;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private RagDataSource currentRagDataSource;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalRagdssourcename = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableRagdssourcetypes = new ArrayList<>();
    private List<String> availableRagdsindexstatuss = new ArrayList<>();
    
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
            idxragdatasource = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando RagDataSourceDetailViewModel - mode: {}, idxragdatasource: {}", mode, idxragdatasource);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxragdatasource != null) {
            loadItem(idxragdatasource);
        } else {
            log.error("Modo inválido o falta idxragdatasource");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentRagDataSource, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentRagDataSource = new RagDataSource();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadRagdssourcetypes();
        loadRagdsindexstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentRagDataSource = businessService.findById(RagDataSource.class, id);
            
            if (currentRagDataSource == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentRagDataSource.getRagdssourcename();
            loadRagdssourcetypes();
            loadRagdsindexstatuss();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalRagdssourcename = currentRagDataSource.getRagdssourcename();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "RAGDATASOURCES", id, "Consulta: " + currentRagDataSource.getRagdssourcename());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentRagDataSource.getIdxragdatasource() == null;
            
            if (isNew) {
                businessService.save(currentRagDataSource);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "RAGDATASOURCES", currentRagDataSource.getIdxragdatasource(), 
                    "Creado: " + currentRagDataSource.getRagdssourcename());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentRagDataSource);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "RAGDATASOURCES", currentRagDataSource.getIdxragdatasource(), 
                    "Actualizado: " + currentRagDataSource.getRagdssourcename());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentRagDataSource.getRagdssourcename() == null || currentRagDataSource.getRagdssourcename().trim().isEmpty()) {
            errors.append("- Source Name\n");
        }
        if (currentRagDataSource.getRagdssourcename() != null && currentRagDataSource.getRagdssourcename().length() > 255) {
            errors.append("- Source Name no puede exceder 255 caracteres\n");
        }
        if (currentRagDataSource.getRagdssourcetype() == null || currentRagDataSource.getRagdssourcetype().trim().isEmpty()) {
            errors.append("- Source Type\n");
        }
        if (currentRagDataSource.getRagdsindexstatus() == null || currentRagDataSource.getRagdsindexstatus().trim().isEmpty()) {
            errors.append("- Index Status\n");
        }
        if (currentRagDataSource.getRagdscreatedat() == null) {
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
        params.put("dataParam", idxragdatasource);
        params.put("action", Action.LOAD);
        appendPage("plataforma/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadRagdssourcetypes() {
        // Cargar tipos de fuente
        availableRagdssourcetypes.add("FILE_SYSTEM");
        availableRagdssourcetypes.add("API");
        availableRagdssourcetypes.add("DATABASE");
        availableRagdssourcetypes.add("WEB_SCRAPING");
    }
    
    private void loadRagdsindexstatuss() {
        // Cargar estados de indexación
        availableRagdsindexstatuss.add("PENDING");
        availableRagdsindexstatuss.add("IN_PROGRESS");
        availableRagdsindexstatuss.add("COMPLETED");
        availableRagdsindexstatuss.add("FAILED");
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
            currentRagDataSource = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableRagdssourcetypes != null) {
                availableRagdssourcetypes.clear();
                availableRagdssourcetypes = null;
            }
            if (availableRagdsindexstatuss != null) {
                availableRagdsindexstatuss.clear();
                availableRagdsindexstatuss = null;
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
