package com.codeflowx.platform.viewmodel.artefacto;

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
import com.codeflowx.govern.entity.artefacto.BaseArtifact;
import com.codeflowx.govern.entity.artefacto.ArtifactDependency;
import com.codeflowx.govern.entity.artefacto.ArtifactVersion;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de BaseArtifact
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class BaseArtifactDetailViewModel extends MasterPage {
    
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
    private Long idxbaseartifact;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private BaseArtifact currentBaseArtifact;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalCatartname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableCatarttypes = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ArtifactDependency> subcatartifactdependencies = new ArrayList<>();
    private List<ArtifactDependency> subcatartifactdependenciesByTargetartifact = new ArrayList<>();
    private List<ArtifactVersion> subcatartifactversions = new ArrayList<>();
    private boolean subcatartifactdependenciesLoaded = false;
    private boolean subcatartifactdependenciesByTargetartifactLoaded = false;
    private boolean subcatartifactversionsLoaded = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxbaseartifact = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando BaseArtifactDetailViewModel - mode: {}, idxbaseartifact: {}", mode, idxbaseartifact);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxbaseartifact != null) {
            loadItem(idxbaseartifact);
        } else {
            log.error("Modo inválido o falta idxbaseartifact");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentBaseArtifact, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentBaseArtifact = new BaseArtifact();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadCatarttypes();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentBaseArtifact = businessService.findById(BaseArtifact.class, id);
            
            if (currentBaseArtifact == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentBaseArtifact.getCatartname();
        loadCatarttypes();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalCatartname = currentBaseArtifact.getCatartname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "CATBASEARTIFACTS", id, "Consulta: " + currentBaseArtifact.getCatartname());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentBaseArtifact.getIdxbaseartifact() == null;
            
            if (isNew) {
                businessService.save(currentBaseArtifact);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "CATBASEARTIFACTS", currentBaseArtifact.getIdxbaseartifact(), 
                    "Creado: " + currentBaseArtifact.getCatartname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentBaseArtifact);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "CATBASEARTIFACTS", currentBaseArtifact.getIdxbaseartifact(), 
                    "Actualizado: " + currentBaseArtifact.getCatartname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentBaseArtifact.getCatartname() == null || currentBaseArtifact.getCatartname().trim().isEmpty()) {
            errors.append("- Catartname\n");
        }
        if (currentBaseArtifact.getCatartname() != null && currentBaseArtifact.getCatartname().length() > 255) {
            errors.append("- Catartname no puede exceder 255 caracteres\n");
        }
        if (currentBaseArtifact.getCatarttype() == null || currentBaseArtifact.getCatarttype().trim().isEmpty()) {
            errors.append("- Catarttype\n");
        }
        if (currentBaseArtifact.getCatartcreatedat() == null) {
            errors.append("- Catartcreatedat\n");
        }
        if (currentBaseArtifact.getCatartupdatedat() == null) {
            errors.append("- Catartupdatedat\n");
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
        params.put("dataParam", idxbaseartifact);
        params.put("action", Action.LOAD);
        appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadCatarttypes() {
        // TODO: Cargar valores desde configuración o BD
        availableCatarttypes.add("OPTION_1");
        availableCatarttypes.add("OPTION_2");
        availableCatarttypes.add("OPTION_3");
    }
    
    private void loadSubcatartifactdependencies() {
        try {
            if (currentBaseArtifact != null && currentBaseArtifact.getIdxbaseartifact() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "sourceArtifact");
                criteria.setValues(new Object[]{currentBaseArtifact.getIdxbaseartifact()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ArtifactDependency> result = businessService.findAllEntity(ArtifactDependency.class, collectionParams, criterias);
                subcatartifactdependencies = result != null ? result.getContent() : new ArrayList<>();
                subcatartifactdependenciesLoaded = true;
                log.debug("Cargados {} subcatartifactdependencies", subcatartifactdependencies.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subcatartifactdependencies", e);
            subcatartifactdependencies = new ArrayList<>();
        }
    }
    
    private void loadSubcatartifactdependenciesByTargetartifact() {
        try {
            if (currentBaseArtifact != null && currentBaseArtifact.getIdxbaseartifact() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "targetArtifact");
                criteria.setValues(new Object[]{currentBaseArtifact.getIdxbaseartifact()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ArtifactDependency> result = businessService.findAllEntity(ArtifactDependency.class, collectionParams, criterias);
                subcatartifactdependenciesByTargetartifact = result != null ? result.getContent() : new ArrayList<>();
                subcatartifactdependenciesByTargetartifactLoaded = true;
                log.debug("Cargados {} subcatartifactdependenciesByTargetartifact", subcatartifactdependenciesByTargetartifact.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subcatartifactdependenciesByTargetartifact", e);
            subcatartifactdependenciesByTargetartifact = new ArrayList<>();
        }
    }
    
    private void loadSubcatartifactversions() {
        try {
            if (currentBaseArtifact != null && currentBaseArtifact.getIdxbaseartifact() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "artifact");
                criteria.setValues(new Object[]{currentBaseArtifact.getIdxbaseartifact()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ArtifactVersion> result = businessService.findAllEntity(ArtifactVersion.class, collectionParams, criterias);
                subcatartifactversions = result != null ? result.getContent() : new ArrayList<>();
                subcatartifactversionsLoaded = true;
                log.debug("Cargados {} subcatartifactversions", subcatartifactversions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subcatartifactversions", e);
            subcatartifactversions = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subcatartifactdependencies")
    public void onSelectSubcatartifactdependenciesTab() {
        if (!subcatartifactdependenciesLoaded) {
            loadSubcatartifactdependencies();
        }
    }
    
    @Command
    @NotifyChange("subcatartifactdependenciesByTargetartifact")
    public void onSelectSubcatartifactdependenciesByTargetartifactTab() {
        if (!subcatartifactdependenciesByTargetartifactLoaded) {
            loadSubcatartifactdependenciesByTargetartifact();
        }
    }
    
    @Command
    @NotifyChange("subcatartifactversions")
    public void onSelectSubcatartifactversionsTab() {
        if (!subcatartifactversionsLoaded) {
            loadSubcatartifactversions();
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
            currentBaseArtifact = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableCatarttypes != null) {
                availableCatarttypes.clear();
                availableCatarttypes = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subcatartifactdependencies != null) {
                subcatartifactdependencies.clear();
                subcatartifactdependencies = null;
            }
            subcatartifactdependenciesLoaded = false;
            if (subcatartifactdependenciesByTargetartifact != null) {
                subcatartifactdependenciesByTargetartifact.clear();
                subcatartifactdependenciesByTargetartifact = null;
            }
            subcatartifactdependenciesByTargetartifactLoaded = false;
            if (subcatartifactversions != null) {
                subcatartifactversions.clear();
                subcatartifactversions = null;
            }
            subcatartifactversionsLoaded = false;
            
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
