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
import com.codeflowx.govern.entity.agents.AgentMonitoring;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AgentMonitoring
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentMonitoringDetailViewModel extends MasterPage {
    
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
    private Long idxagentmonitoring;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AgentMonitoring currentAgentMonitoring;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAgtmetricname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgtmetrictypes = new ArrayList<>();
    private List<String> availableAgtstatuss = new ArrayList<>();
    private List<String> availableAgtanomalytypes = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedAgttags = new ArrayList<>();
    private String newAgttag = "";
    
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
            idxagentmonitoring = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentMonitoringDetailViewModel - mode: {}, idxagentmonitoring: {}", mode, idxagentmonitoring);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxagentmonitoring != null) {
            loadItem(idxagentmonitoring);
        } else {
            log.error("Modo inválido o falta idxagentmonitoring");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgentMonitoring, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgentMonitoring = new AgentMonitoring();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgtmetrictypes();
        loadAgtstatuss();
        loadAgtanomalytypes();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgentMonitoring = businessService.findById(AgentMonitoring.class, id);
            
            if (currentAgentMonitoring == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgentMonitoring.getAgtmetricname();
        loadAgtmetrictypes();
        loadAgtstatuss();
        loadAgtanomalytypes();
            
            // Cargar tags/roles existentes desde JSON
            selectedAgttags = convertJsonToList(currentAgentMonitoring.getAgttags());
            
            // Guardar valores originales para validación de unicidad
            originalAgtmetricname = currentAgentMonitoring.getAgtmetricname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTAGENTMONITORING", id, "Consulta: " + currentAgentMonitoring.getAgtmetricname());
            
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
            
            boolean isNew = currentAgentMonitoring.getIdxagentmonitoring() == null;
            
            if (isNew) {
                businessService.save(currentAgentMonitoring);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTAGENTMONITORING", currentAgentMonitoring.getIdxagentmonitoring(), 
                    "Creado: " + currentAgentMonitoring.getAgtmetricname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentAgentMonitoring);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTAGENTMONITORING", currentAgentMonitoring.getIdxagentmonitoring(), 
                    "Actualizado: " + currentAgentMonitoring.getAgtmetricname());
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
        
        if (currentAgentMonitoring.getAgtmetrictype() == null || currentAgentMonitoring.getAgtmetrictype().trim().isEmpty()) {
            errors.append("- Metric Type\n");
        }
        if (currentAgentMonitoring.getAgtmetricname() == null || currentAgentMonitoring.getAgtmetricname().trim().isEmpty()) {
            errors.append("- Metric Name\n");
        }
        if (currentAgentMonitoring.getAgtmetricname() != null && currentAgentMonitoring.getAgtmetricname().length() > 255) {
            errors.append("- Metric Name no puede exceder 255 caracteres\n");
        }
        if (currentAgentMonitoring.getAgtmetricvalue() == null) {
            errors.append("- Metric Value\n");
        }
        if (currentAgentMonitoring.getAgtstatus() == null || currentAgentMonitoring.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgentMonitoring.getAgttimestamp() == null) {
            errors.append("- Time Stamp\n");
        }
        if (currentAgentMonitoring.getAgtcreatedby() == null || currentAgentMonitoring.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgentMonitoring.getAgtcreatedby() != null && currentAgentMonitoring.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgentMonitoring.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxagentmonitoring);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgtmetrictypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtmetrictypes.add("OPTION_1");
        availableAgtmetrictypes.add("OPTION_2");
        availableAgtmetrictypes.add("OPTION_3");
    }
    
    private void loadAgtstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtstatuss.add("OPTION_1");
        availableAgtstatuss.add("OPTION_2");
        availableAgtstatuss.add("OPTION_3");
    }
    
    private void loadAgtanomalytypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtanomalytypes.add("OPTION_1");
        availableAgtanomalytypes.add("OPTION_2");
        availableAgtanomalytypes.add("OPTION_3");
    }
    
    @Command
    @NotifyChange("{'selectedAgttags', 'currentAgentMonitoring'}")
    public void addAgttag() {
        if (newAgttag != null && !newAgttag.trim().isEmpty() && !selectedAgttags.contains(newAgttag.trim())) {
            selectedAgttags.add(newAgttag.trim());
            newAgttag = "";
            // Convertir lista a JSON y actualizar en currentAgentMonitoring
            currentAgentMonitoring.setAgttags(convertListToJson(selectedAgttags));
        }
    }
    
    @Command
    @NotifyChange("{'selectedAgttags', 'currentAgentMonitoring'}")
    public void removeAgttag(@BindingParam("tag") String tag) {
        selectedAgttags.remove(tag);
        // Convertir lista a JSON y actualizar en currentAgentMonitoring
        currentAgentMonitoring.setAgttags(convertListToJson(selectedAgttags));
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
            currentAgentMonitoring = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgtmetrictypes != null) {
                availableAgtmetrictypes.clear();
                availableAgtmetrictypes = null;
            }
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            if (availableAgtanomalytypes != null) {
                availableAgtanomalytypes.clear();
                availableAgtanomalytypes = null;
            }
            
            // Limpiar colecciones @OneToMany
            
            // Limpiar tags/roles JSONB
            if (selectedAgttags != null) {
                selectedAgttags.clear();
                selectedAgttags = null;
            }
            newAgttag = null;
            
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
