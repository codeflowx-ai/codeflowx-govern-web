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
import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.entity.rag.RagDataSource;
import com.codeflowx.govern.entity.rag.RagVersion;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de RagSystem
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RagSystemDetailViewModel extends MasterPage {
    
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
    private Long idxragsystem;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private RagSystem currentRagSystem;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalRagsystemname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableRagtypes = new ArrayList<>();
    private List<String> availableRagstatuss = new ArrayList<>();
    private List<String> availableRaggovernancestatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<RagDataSource> subragragdatasources = new ArrayList<>();
    private List<RagVersion> subragragversions = new ArrayList<>();
    private boolean subragragdatasourcesLoaded = false;
    private boolean subragragversionsLoaded = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxragsystem = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando RagSystemDetailViewModel - mode: {}, idxragsystem: {}", mode, idxragsystem);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxragsystem != null) {
            loadItem(idxragsystem);
        } else {
            log.error("Modo inválido o falta idxragsystem");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentRagSystem, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentRagSystem = new RagSystem();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadRagtypes();
        loadRagstatuss();
        loadRaggovernancestatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentRagSystem = businessService.findById(RagSystem.class, id);
            
            if (currentRagSystem == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentRagSystem.getRagsystemname();
            loadRagtypes();
            loadRagstatuss();
            loadRaggovernancestatuss();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalRagsystemname = currentRagSystem.getRagsystemname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "RAGSYSTEMS", id, "Consulta: " + currentRagSystem.getRagsystemname());
            
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
            
            boolean isNew = currentRagSystem.getIdxragsystem() == null;
            
            if (isNew) {
                businessService.save(currentRagSystem);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "RAGSYSTEMS", currentRagSystem.getIdxragsystem(), 
                    "Creado: " + currentRagSystem.getRagsystemname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentRagSystem);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "RAGSYSTEMS", currentRagSystem.getIdxragsystem(), 
                    "Actualizado: " + currentRagSystem.getRagsystemname());
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
        
        if (currentRagSystem.getRagsystemname() == null || currentRagSystem.getRagsystemname().trim().isEmpty()) {
            errors.append("- System Name\n");
        }
        if (currentRagSystem.getRagsystemname() != null && currentRagSystem.getRagsystemname().length() > 255) {
            errors.append("- System Name no puede exceder 255 caracteres\n");
        }
        if (currentRagSystem.getRagtype() == null || currentRagSystem.getRagtype().trim().isEmpty()) {
            errors.append("- Type\n");
        }
        if (currentRagSystem.getRagversion() == null || currentRagSystem.getRagversion().trim().isEmpty()) {
            errors.append("- Version\n");
        }
        if (currentRagSystem.getRagversion() != null && currentRagSystem.getRagversion().length() > 50) {
            errors.append("- Version no puede exceder 50 caracteres\n");
        }
        if (currentRagSystem.getRagstatus() == null || currentRagSystem.getRagstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentRagSystem.getRagcompliance() == null || currentRagSystem.getRagcompliance().trim().isEmpty()) {
            errors.append("- Compliance\n");
        }
        if (currentRagSystem.getRagcreatedat() == null) {
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
        params.put("dataParam", idxragsystem);
        params.put("action", Action.LOAD);
        appendPage("plataforma/rag/rag-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadRagtypes() {
        // Cargar tipos de sistema RAG
        availableRagtypes.add("KNOWLEDGE_BASE");
        availableRagtypes.add("LEGAL_ASSISTANT");
        availableRagtypes.add("CODE_ASSISTANT");
        availableRagtypes.add("SUPPORT");
    }
    
    private void loadRagstatuss() {
        // Cargar estados de sistema RAG
        availableRagstatuss.add("ACTIVE");
        availableRagstatuss.add("INACTIVE");
        availableRagstatuss.add("DRAFT");
        availableRagstatuss.add("ARCHIVED");
    }
    
    private void loadRaggovernancestatuss() {
        // Cargar estados de gobierno
        availableRaggovernancestatuss.add("APPROVED");
        availableRaggovernancestatuss.add("REJECTED");
        availableRaggovernancestatuss.add("PENDING");
    }
    
    private void loadSubragragdatasources() {
        try {
            if (currentRagSystem != null && currentRagSystem.getIdxragsystem() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragSystem");
                criteria.setValues(new Object[]{currentRagSystem.getIdxragsystem()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<RagDataSource> result = businessService.findAllEntity(RagDataSource.class, collectionParams, criterias);
                subragragdatasources = result != null ? result.getContent() : new ArrayList<>();
                subragragdatasourcesLoaded = true;
                log.debug("Cargados {} subragragdatasources", subragragdatasources.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subragragdatasources", e);
            subragragdatasources = new ArrayList<>();
        }
    }
    
    private void loadSubragragversions() {
        try {
            if (currentRagSystem != null && currentRagSystem.getIdxragsystem() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragSystem");
                criteria.setValues(new Object[]{currentRagSystem.getIdxragsystem()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<RagVersion> result = businessService.findAllEntity(RagVersion.class, collectionParams, criterias);
                subragragversions = result != null ? result.getContent() : new ArrayList<>();
                subragragversionsLoaded = true;
                log.debug("Cargados {} subragragversions", subragragversions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subragragversions", e);
            subragragversions = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subragragdatasources")
    public void onSelectSubragragdatasourcesTab() {
        if (!subragragdatasourcesLoaded) {
            loadSubragragdatasources();
        }
    }
    
    @Command
    @NotifyChange("subragragversions")
    public void onSelectSubragragversionsTab() {
        if (!subragragversionsLoaded) {
            loadSubragragversions();
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
            currentRagSystem = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableRagtypes != null) {
                availableRagtypes.clear();
                availableRagtypes = null;
            }
            if (availableRagstatuss != null) {
                availableRagstatuss.clear();
                availableRagstatuss = null;
            }
            if (availableRaggovernancestatuss != null) {
                availableRaggovernancestatuss.clear();
                availableRaggovernancestatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subragragdatasources != null) {
                subragragdatasources.clear();
                subragragdatasources = null;
            }
            subragragdatasourcesLoaded = false;
            if (subragragversions != null) {
                subragragversions.clear();
                subragragversions = null;
            }
            subragragversionsLoaded = false;
            
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
