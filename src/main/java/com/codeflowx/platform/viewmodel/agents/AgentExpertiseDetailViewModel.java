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
import com.codeflowx.govern.entity.agents.AgentExpertise;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import codeflowx.nocode.persist.BusinessService;
import com.codeflowx.govern.service.agents.AgentExpertiseService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.framework.zkoss.BaseFront;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AgentExpertise
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentExpertiseDetailViewModel extends BaseFront<AgentExpertiseDetailViewModel> {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private AgentExpertiseService agentExpertiseService;
    
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
    private Long idxagentexpertise;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AgentExpertise currentAgentExpertise;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgtexpertiselevels = new ArrayList<>();
    private List<String> availableAgtcertificationstatuss = new ArrayList<>();
    private List<String> availableAgtstatuss = new ArrayList<>();
    private List<String> availableAgtprioritys = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedAgtskills = new ArrayList<>();
    private String newAgtskill = "";
    
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

        
            // Si action es null, intentar determinar el modo por el contexto

        
            mode = (dataParam != null) ? "LOAD" : "NEW";

        
            log.warn("Action es null, infiriendo modo: {}", mode);

        
        }
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxagentexpertise = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentExpertiseDetailViewModel - mode: {}, idxagentexpertise: {}", mode, idxagentexpertise);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxagentexpertise != null) {
            loadItem(idxagentexpertise);
        } else {
            log.error("Modo inválido o falta idxagentexpertise");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgentExpertise, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgentExpertise = new AgentExpertise();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgtexpertiselevels();
        loadAgtcertificationstatuss();
        loadAgtstatuss();
        loadAgtprioritys();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgentExpertise = agentExpertiseService.findById(id);
            
            if (currentAgentExpertise == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgentExpertise.getIdxagentexpertise();
        loadAgtexpertiselevels();
        loadAgtcertificationstatuss();
        loadAgtstatuss();
        loadAgtprioritys();
            
            // Cargar tags/roles existentes desde JSON
            selectedAgtskills = convertJsonToList(currentAgentExpertise.getAgtskills());
            
            // Guardar valores originales para validación de unicidad
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTAGENTEXPERTISE", id, "Consulta: " + currentAgentExpertise.getIdxagentexpertise());
            
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
            
            boolean isNew = currentAgentExpertise.getIdxagentexpertise() == null;
            
            if (isNew) {
                currentAgentExpertise = agentExpertiseService.create(currentAgentExpertise);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTAGENTEXPERTISE", currentAgentExpertise.getIdxagentexpertise(), 
                    "Creado: " + currentAgentExpertise.getIdxagentexpertise());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentAgentExpertise = agentExpertiseService.update(currentAgentExpertise);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTAGENTEXPERTISE", currentAgentExpertise.getIdxagentexpertise(), 
                    "Actualizado: " + currentAgentExpertise.getIdxagentexpertise());
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
        
        if (currentAgentExpertise.getAgtexpertiselevel() == null || currentAgentExpertise.getAgtexpertiselevel().trim().isEmpty()) {
            errors.append("- Expertise Level\n");
        }
        if (currentAgentExpertise.getAgtstatus() == null || currentAgentExpertise.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgentExpertise.getAgtcreatedby() == null || currentAgentExpertise.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgentExpertise.getAgtcreatedby() != null && currentAgentExpertise.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgentExpertise.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxagentexpertise);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgtexpertiselevels() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtexpertiselevels.add("OPTION_1");
        availableAgtexpertiselevels.add("OPTION_2");
        availableAgtexpertiselevels.add("OPTION_3");
    }
    
    private void loadAgtcertificationstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtcertificationstatuss.add("OPTION_1");
        availableAgtcertificationstatuss.add("OPTION_2");
        availableAgtcertificationstatuss.add("OPTION_3");
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
    
    @Command
    @NotifyChange("{'selectedAgtskills', 'currentAgentExpertise'}")
    public void addAgtskill() {
        if (newAgtskill != null && !newAgtskill.trim().isEmpty() && !selectedAgtskills.contains(newAgtskill.trim())) {
            selectedAgtskills.add(newAgtskill.trim());
            newAgtskill = "";
            // Convertir lista a JSON y actualizar en currentAgentExpertise
            currentAgentExpertise.setAgtskills(convertListToJson(selectedAgtskills));
        }
    }
    
    @Command
    @NotifyChange("{'selectedAgtskills', 'currentAgentExpertise'}")
    public void removeAgtskill(@BindingParam("tag") String tag) {
        selectedAgtskills.remove(tag);
        // Convertir lista a JSON y actualizar en currentAgentExpertise
        currentAgentExpertise.setAgtskills(convertListToJson(selectedAgtskills));
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
    
    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelo XXXX
     * @throws DaoException
     * @throws UiException
     */
    
    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar entidad actual
            currentAgentExpertise = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgtexpertiselevels != null) {
                availableAgtexpertiselevels.clear();
                availableAgtexpertiselevels = null;
            }
            if (availableAgtcertificationstatuss != null) {
                availableAgtcertificationstatuss.clear();
                availableAgtcertificationstatuss = null;
            }
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            if (availableAgtprioritys != null) {
                availableAgtprioritys.clear();
                availableAgtprioritys = null;
            }
            
            // Limpiar colecciones @OneToMany
            
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
