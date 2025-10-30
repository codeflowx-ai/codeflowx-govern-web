package com.codeflowx.platform.viewmodel.projects;

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
import com.codeflowx.govern.entity.projects.Project;
import com.codeflowx.govern.entity.projects.ProjectArtifact;
import com.codeflowx.govern.entity.projects.ProjectMember;
import com.codeflowx.govern.entity.projects.ProjectTechnology;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Project
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProjectDetailViewModel extends MasterPage {
    
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
    private Long idxproject;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private Project currentProject;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalName = null;
    private String originalProjectcode = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableProjecttypes = new ArrayList<>();
    private List<String> availableStatuss = new ArrayList<>();
    private List<String> availablePrioritys = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ProjectArtifact> subgovprojectartifacts = new ArrayList<>();
    private List<ProjectMember> subgovprojectmembers = new ArrayList<>();
    private List<ProjectTechnology> subprjprojecttechnologies = new ArrayList<>();
    private boolean subgovprojectartifactsLoaded = false;
    private boolean subgovprojectmembersLoaded = false;
    private boolean subprjprojecttechnologiesLoaded = false;
    
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
            idxproject = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ProjectDetailViewModel - mode: {}, idxproject: {}", mode, idxproject);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxproject != null) {
            loadItem(idxproject);
        } else {
            log.error("Modo inválido o falta idxproject");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentProject, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentProject = new Project();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadProjecttypes();
        loadStatuss();
        loadPrioritys();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentProject = businessService.findById(Project.class, id);
            
            if (currentProject == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentProject.getName();
        loadProjecttypes();
        loadStatuss();
        loadPrioritys();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalName = currentProject.getName();
            originalProjectcode = currentProject.getProjectcode();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "PRJPROJECTS", id, "Consulta: " + currentProject.getName());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentProject.getIdxproject() == null;
            
            if (isNew) {
                businessService.save(currentProject);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PRJPROJECTS", currentProject.getIdxproject(), 
                    "Creado: " + currentProject.getName());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentProject);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PRJPROJECTS", currentProject.getIdxproject(), 
                    "Actualizado: " + currentProject.getName());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentProject.getName() == null || currentProject.getName().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentProject.getName() != null && currentProject.getName().length() > 100) {
            errors.append("- Name no puede exceder 100 caracteres\n");
        }
        if (currentProject.getProjectcode() == null || currentProject.getProjectcode().trim().isEmpty()) {
            errors.append("- Projectcode\n");
        }
        if (currentProject.getProjectcode() != null && currentProject.getProjectcode().length() > 100) {
            errors.append("- Projectcode no puede exceder 100 caracteres\n");
        }
        if (currentProject.getProjecttype() == null || currentProject.getProjecttype().trim().isEmpty()) {
            errors.append("- Projecttype\n");
        }
        if (currentProject.getStatus() == null || currentProject.getStatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentProject.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentProject.getUpdatedat() == null) {
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
        params.put("dataParam", idxproject);
        params.put("action", Action.LOAD);
        appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadProjecttypes() {
        // TODO: Cargar valores desde configuración o BD
        availableProjecttypes.add("OPTION_1");
        availableProjecttypes.add("OPTION_2");
        availableProjecttypes.add("OPTION_3");
    }
    
    private void loadStatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableStatuss.add("OPTION_1");
        availableStatuss.add("OPTION_2");
        availableStatuss.add("OPTION_3");
    }
    
    private void loadPrioritys() {
        // TODO: Cargar valores desde configuración o BD
        availablePrioritys.add("OPTION_1");
        availablePrioritys.add("OPTION_2");
        availablePrioritys.add("OPTION_3");
    }
    
    private void loadSubgovprojectartifacts() {
        try {
            if (currentProject != null && currentProject.getIdxproject() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "project");
                criteria.setValues(new Object[]{currentProject.getIdxproject()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ProjectArtifact> result = businessService.findAllEntity(ProjectArtifact.class, collectionParams, criterias);
                subgovprojectartifacts = result != null ? result.getContent() : new ArrayList<>();
                subgovprojectartifactsLoaded = true;
                log.debug("Cargados {} subgovprojectartifacts", subgovprojectartifacts.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subgovprojectartifacts", e);
            subgovprojectartifacts = new ArrayList<>();
        }
    }
    
    private void loadSubgovprojectmembers() {
        try {
            if (currentProject != null && currentProject.getIdxproject() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "project");
                criteria.setValues(new Object[]{currentProject.getIdxproject()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ProjectMember> result = businessService.findAllEntity(ProjectMember.class, collectionParams, criterias);
                subgovprojectmembers = result != null ? result.getContent() : new ArrayList<>();
                subgovprojectmembersLoaded = true;
                log.debug("Cargados {} subgovprojectmembers", subgovprojectmembers.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subgovprojectmembers", e);
            subgovprojectmembers = new ArrayList<>();
        }
    }
    
    private void loadSubprjprojecttechnologies() {
        try {
            if (currentProject != null && currentProject.getIdxproject() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "project");
                criteria.setValues(new Object[]{currentProject.getIdxproject()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ProjectTechnology> result = businessService.findAllEntity(ProjectTechnology.class, collectionParams, criterias);
                subprjprojecttechnologies = result != null ? result.getContent() : new ArrayList<>();
                subprjprojecttechnologiesLoaded = true;
                log.debug("Cargados {} subprjprojecttechnologies", subprjprojecttechnologies.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subprjprojecttechnologies", e);
            subprjprojecttechnologies = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subgovprojectartifacts")
    public void onSelectSubgovprojectartifactsTab() {
        if (!subgovprojectartifactsLoaded) {
            loadSubgovprojectartifacts();
        }
    }
    
    @Command
    @NotifyChange("subgovprojectmembers")
    public void onSelectSubgovprojectmembersTab() {
        if (!subgovprojectmembersLoaded) {
            loadSubgovprojectmembers();
        }
    }
    
    @Command
    @NotifyChange("subprjprojecttechnologies")
    public void onSelectSubprjprojecttechnologiesTab() {
        if (!subprjprojecttechnologiesLoaded) {
            loadSubprjprojecttechnologies();
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
            currentProject = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableProjecttypes != null) {
                availableProjecttypes.clear();
                availableProjecttypes = null;
            }
            if (availableStatuss != null) {
                availableStatuss.clear();
                availableStatuss = null;
            }
            if (availablePrioritys != null) {
                availablePrioritys.clear();
                availablePrioritys = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subgovprojectartifacts != null) {
                subgovprojectartifacts.clear();
                subgovprojectartifacts = null;
            }
            subgovprojectartifactsLoaded = false;
            if (subgovprojectmembers != null) {
                subgovprojectmembers.clear();
                subgovprojectmembers = null;
            }
            subgovprojectmembersLoaded = false;
            if (subprjprojecttechnologies != null) {
                subprjprojecttechnologies.clear();
                subprjprojecttechnologies = null;
            }
            subprjprojecttechnologiesLoaded = false;
            
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
