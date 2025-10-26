package com.codeflowx.platform.viewmodel.agents;

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
import com.codeflowx.govern.entity.agents.AgentDomain;
import com.codeflowx.govern.entity.agents.AgentExpertise;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AgentDomain
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentDomainDetailViewModel extends MasterPage {
    
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
    private Long idxagentdomain;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AgentDomain currentAgentDomain;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAgtdomainname = null;
    private String originalAgtdomaincode = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgtstatuss = new ArrayList<>();
    private List<String> availableAgtprioritys = new ArrayList<>();
    private List<String> availableAgtmaturitylevels = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedAgtskills = new ArrayList<>();
    private String newAgtskill = "";
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<AgentExpertise> subagtagentexpertise = new ArrayList<>();
    private boolean subagtagentexpertiseLoaded = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxagentdomain = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentDomainDetailViewModel - mode: {}, idxagentdomain: {}", mode, idxagentdomain);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxagentdomain != null) {
            loadItem(idxagentdomain);
        } else {
            log.error("Modo inválido o falta idxagentdomain");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgentDomain, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgentDomain = new AgentDomain();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgtstatuss();
        loadAgtprioritys();
        loadAgtmaturitylevels();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgentDomain = businessService.findById(AgentDomain.class, id);
            
            if (currentAgentDomain == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgentDomain.getAgtdomainname();
        loadAgtstatuss();
        loadAgtprioritys();
        loadAgtmaturitylevels();
            
            // Cargar tags/roles existentes desde JSON
            selectedAgtskills = convertJsonToList(currentAgentDomain.getAgtskills());
            
            // Guardar valores originales para validación de unicidad
            originalAgtdomainname = currentAgentDomain.getAgtdomainname();
            originalAgtdomaincode = currentAgentDomain.getAgtdomaincode();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTAGENTDOMAINS", id, "Consulta: " + currentAgentDomain.getAgtdomainname());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentAgentDomain.getIdxagentdomain() == null;
            
            if (isNew) {
                businessService.save(currentAgentDomain);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTAGENTDOMAINS", currentAgentDomain.getIdxagentdomain(), 
                    "Creado: " + currentAgentDomain.getAgtdomainname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentAgentDomain);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTAGENTDOMAINS", currentAgentDomain.getIdxagentdomain(), 
                    "Actualizado: " + currentAgentDomain.getAgtdomainname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentAgentDomain.getAgtdomainname() == null || currentAgentDomain.getAgtdomainname().trim().isEmpty()) {
            errors.append("- Domain Name\n");
        }
        if (currentAgentDomain.getAgtdomainname() != null && currentAgentDomain.getAgtdomainname().length() > 255) {
            errors.append("- Domain Name no puede exceder 255 caracteres\n");
        }
        if (currentAgentDomain.getAgtdomaincode() == null || currentAgentDomain.getAgtdomaincode().trim().isEmpty()) {
            errors.append("- Domain Code\n");
        }
        if (currentAgentDomain.getAgtdomaincode() != null && currentAgentDomain.getAgtdomaincode().length() > 100) {
            errors.append("- Domain Code no puede exceder 100 caracteres\n");
        }
        if (currentAgentDomain.getAgtcategory() == null || currentAgentDomain.getAgtcategory().trim().isEmpty()) {
            errors.append("- Category\n");
        }
        if (currentAgentDomain.getAgtcategory() != null && currentAgentDomain.getAgtcategory().length() > 100) {
            errors.append("- Category no puede exceder 100 caracteres\n");
        }
        if (currentAgentDomain.getAgtstatus() == null || currentAgentDomain.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgentDomain.getAgtcreatedby() == null || currentAgentDomain.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgentDomain.getAgtcreatedby() != null && currentAgentDomain.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgentDomain.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxagentdomain);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgtstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtstatuss.add("OPTION_1");
        availableAgtstatuss.add("OPTION_2");
        availableAgtstatuss.add("OPTION_3");
    }
    
    private void loadAgtprioritys() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtprioritys.add("OPTION_1");
        availableAgtprioritys.add("OPTION_2");
        availableAgtprioritys.add("OPTION_3");
    }
    
    private void loadAgtmaturitylevels() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtmaturitylevels.add("OPTION_1");
        availableAgtmaturitylevels.add("OPTION_2");
        availableAgtmaturitylevels.add("OPTION_3");
    }
    
    @Command
    @NotifyChange("{'selectedAgtskills', 'currentAgentDomain'}")
    public void addAgtskill() {
        if (newAgtskill != null && !newAgtskill.trim().isEmpty() && !selectedAgtskills.contains(newAgtskill.trim())) {
            selectedAgtskills.add(newAgtskill.trim());
            newAgtskill = "";
            // Convertir lista a JSON y actualizar en currentAgentDomain
            currentAgentDomain.setAgtskills(convertListToJson(selectedAgtskills));
        }
    }
    
    @Command
    @NotifyChange("{'selectedAgtskills', 'currentAgentDomain'}")
    public void removeAgtskill(@BindingParam("tag") String tag) {
        selectedAgtskills.remove(tag);
        // Convertir lista a JSON y actualizar en currentAgentDomain
        currentAgentDomain.setAgtskills(convertListToJson(selectedAgtskills));
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
    
    private void loadSubagtagentexpertise() {
        try {
            if (currentAgentDomain != null && currentAgentDomain.getIdxagentdomain() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "domain");
                criteria.setValues(new Object[]{currentAgentDomain.getIdxagentdomain()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentExpertise> result = businessService.findAllEntity(AgentExpertise.class, collectionParams, criterias);
                subagtagentexpertise = result != null ? result.getContent() : new ArrayList<>();
                subagtagentexpertiseLoaded = true;
                log.debug("Cargados {} subagtagentexpertise", subagtagentexpertise.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentexpertise", e);
            subagtagentexpertise = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subagtagentexpertise")
    public void onSelectSubagtagentexpertiseTab() {
        if (!subagtagentexpertiseLoaded) {
            loadSubagtagentexpertise();
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
            currentAgentDomain = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            if (availableAgtprioritys != null) {
                availableAgtprioritys.clear();
                availableAgtprioritys = null;
            }
            if (availableAgtmaturitylevels != null) {
                availableAgtmaturitylevels.clear();
                availableAgtmaturitylevels = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subagtagentexpertise != null) {
                subagtagentexpertise.clear();
                subagtagentexpertise = null;
            }
            subagtagentexpertiseLoaded = false;
            
            // Limpiar tags/roles JSONB
            if (selectedAgtskills != null) {
                selectedAgtskills.clear();
                selectedAgtskills = null;
            }
            newAgtskill = null;
            
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
