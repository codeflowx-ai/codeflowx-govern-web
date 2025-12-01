package com.codeflowx.platform.viewmodel.prompts;
import com.codeflowx.framework.zkoss.BaseFront;

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
import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.entity.prompts.PromptValidation;
import com.codeflowx.govern.entity.prompts.PromptVersion;
import com.codeflowx.govern.service.prompts.PromptService;
import com.codeflowx.govern.service.prompts.PromptValidationService;
import com.codeflowx.govern.service.prompts.PromptVersionService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Prompt
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PromptDetailViewModel extends BaseFront<PromptDetailViewModel>{
    
    @WireVariable
    private BusinessService businessService;
    @WireVariable
    private PromptService promptService;
    @WireVariable
    private PromptValidationService promptValidationService;
    @WireVariable
    private PromptVersionService promptVersionService;
    
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
    private Long idxprompt;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private Prompt currentPrompt;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalPrmname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availablePrmtypes = new ArrayList<>();
    private List<String> availablePrmstatuss = new ArrayList<>();
    private List<String> availablePrmapprovalstatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<PromptValidation> subprmpromptvalidations = new ArrayList<>();
    private List<PromptVersion> subprmpromptversions = new ArrayList<>();
    private boolean subprmpromptvalidationsLoaded = false;
    private boolean subprmpromptversionsLoaded = false;
    
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
            idxprompt = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando PromptDetailViewModel - mode: {}, idxprompt: {}", mode, idxprompt);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxprompt != null) {
            loadItem(idxprompt);
        } else {
            log.error("Modo inválido o falta idxprompt");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentPrompt, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentPrompt = new Prompt();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadPrmtypes();
        loadPrmstatuss();
        loadPrmapprovalstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentPrompt = promptService.findById(id);
            
            if (currentPrompt == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentPrompt.getPrmname();
        loadPrmtypes();
        loadPrmstatuss();
        loadPrmapprovalstatuss();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalPrmname = currentPrompt.getPrmname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "PRMPROMPTS", id, "Consulta: " + currentPrompt.getPrmname());
            
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
        } catch (Exception e) {
            log.error("Error inesperado al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentPrompt.getIdxprompt() == null;
            
            if (isNew) {
                currentPrompt = promptService.create(currentPrompt);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PRMPROMPTS", currentPrompt.getIdxprompt(), 
                    "Creado: " + currentPrompt.getPrmname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPrompt = promptService.update(currentPrompt);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PRMPROMPTS", currentPrompt.getIdxprompt(), 
                    "Actualizado: " + currentPrompt.getPrmname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentPrompt.getPrmname() == null || currentPrompt.getPrmname().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentPrompt.getPrmname() != null && currentPrompt.getPrmname().length() > 255) {
            errors.append("- Name no puede exceder 255 caracteres\n");
        }
        if (currentPrompt.getPrmtype() == null || currentPrompt.getPrmtype().trim().isEmpty()) {
            errors.append("- Type\n");
        }
        if (currentPrompt.getPrmversion() == null || currentPrompt.getPrmversion().trim().isEmpty()) {
            errors.append("- Version\n");
        }
        if (currentPrompt.getPrmversion() != null && currentPrompt.getPrmversion().length() > 50) {
            errors.append("- Version no puede exceder 50 caracteres\n");
        }
        if (currentPrompt.getPrmstatus() == null || currentPrompt.getPrmstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentPrompt.getPrmcreatedby() == null || currentPrompt.getPrmcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentPrompt.getPrmcreatedby() != null && currentPrompt.getPrmcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentPrompt.getPrmcreatedat() == null) {
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
        params.put("dataParam", idxprompt);
        params.put("action", Action.LOAD);
        appendPage("plataforma/prompts/prompts-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadPrmtypes() {
        // TODO: Cargar valores desde configuración o BD
        availablePrmtypes.add("OPTION_1");
        availablePrmtypes.add("OPTION_2");
        availablePrmtypes.add("OPTION_3");
    }
    
    private void loadPrmstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availablePrmstatuss.add("OPTION_1");
        availablePrmstatuss.add("OPTION_2");
        availablePrmstatuss.add("OPTION_3");
    }
    
    private void loadPrmapprovalstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availablePrmapprovalstatuss.add("OPTION_1");
        availablePrmapprovalstatuss.add("OPTION_2");
        availablePrmapprovalstatuss.add("OPTION_3");
    }
    
    private void loadSubprmpromptvalidations() {
        try {
            if (currentPrompt != null && currentPrompt.getIdxprompt() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prompt");
                criteria.setValues(new Object[]{currentPrompt.getIdxprompt()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<PromptValidation> result = promptValidationService.findAll(collectionParams, criterias);
                subprmpromptvalidations = result != null ? result.getContent() : new ArrayList<>();
                subprmpromptvalidationsLoaded = true;
                log.debug("Cargados {} subprmpromptvalidations", subprmpromptvalidations.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subprmpromptvalidations", e);
            subprmpromptvalidations = new ArrayList<>();
        } catch (Exception e) {
            log.error("Error inesperado al cargar subprmpromptvalidations", e);
            subprmpromptvalidations = new ArrayList<>();
        }
    }
    
    private void loadSubprmpromptversions() {
        try {
            if (currentPrompt != null && currentPrompt.getIdxprompt() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prompt");
                criteria.setValues(new Object[]{currentPrompt.getIdxprompt()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<PromptVersion> result = promptVersionService.findAll(collectionParams, criterias);
                subprmpromptversions = result != null ? result.getContent() : new ArrayList<>();
                subprmpromptversionsLoaded = true;
                log.debug("Cargados {} subprmpromptversions", subprmpromptversions.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subprmpromptversions", e);
            subprmpromptversions = new ArrayList<>();
        } catch (Exception e) {
            log.error("Error inesperado al cargar subprmpromptversions", e);
            subprmpromptversions = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subprmpromptvalidations")
    public void onSelectSubprmpromptvalidationsTab() {
        if (!subprmpromptvalidationsLoaded) {
            loadSubprmpromptvalidations();
        }
    }
    
    @Command
    @NotifyChange("subprmpromptversions")
    public void onSelectSubprmpromptversionsTab() {
        if (!subprmpromptversionsLoaded) {
            loadSubprmpromptversions();
        }
    }
    
    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelo XXXnzar excepción para que no interrumpa el flujo normal
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
            currentPrompt = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availablePrmtypes != null) {
                availablePrmtypes.clear();
                availablePrmtypes = null;
            }
            if (availablePrmstatuss != null) {
                availablePrmstatuss.clear();
                availablePrmstatuss = null;
            }
            if (availablePrmapprovalstatuss != null) {
                availablePrmapprovalstatuss.clear();
                availablePrmapprovalstatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subprmpromptvalidations != null) {
                subprmpromptvalidations.clear();
                subprmpromptvalidations = null;
            }
            subprmpromptvalidationsLoaded = false;
            if (subprmpromptversions != null) {
                subprmpromptversions.clear();
                subprmpromptversions = null;
            }
            subprmpromptversionsLoaded = false;
            
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
