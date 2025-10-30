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
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.agents.AgentAlert;
import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.entity.agents.AgentBiasDetection;
import com.codeflowx.govern.entity.agents.AgentCollaboration;
import com.codeflowx.govern.entity.agents.AgentCommunication;
import com.codeflowx.govern.entity.agents.AgentCompliance;
import com.codeflowx.govern.entity.agents.AgentDecision;
import com.codeflowx.govern.entity.agents.AgentDeployment;
import com.codeflowx.govern.entity.agents.AgentEthicsAssessment;
import com.codeflowx.govern.entity.agents.AgentExpertise;
import com.codeflowx.govern.entity.agents.AgentGovernance;
import com.codeflowx.govern.entity.agents.AgentHealth;
import com.codeflowx.govern.entity.agents.AgentInteraction;
import com.codeflowx.govern.entity.agents.AgentMonitoring;
import com.codeflowx.govern.entity.agents.AgentRollback;
import com.codeflowx.govern.entity.agents.AgentTool;
import com.codeflowx.govern.entity.agents.AgentTransparency;
import com.codeflowx.govern.entity.agents.AgentVersion;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Agent
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentDetailViewModel extends MasterPage {
    
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
    private Long idxagent;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private Agent currentAgent;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAgtname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgttypes = new ArrayList<>();
    private List<String> availableAgtstatuss = new ArrayList<>();
    private List<String> availableAgtapprovalstatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<AgentAlert> subagtagentalerts = new ArrayList<>();
    private List<AgentApproval> subagtagentapprovals = new ArrayList<>();
    private List<AgentBiasDetection> subagtagentbiasdetections = new ArrayList<>();
    private List<AgentCollaboration> subagtagentcollaborations = new ArrayList<>();
    private List<AgentCollaboration> subagtagentcollaborationsByAgent2 = new ArrayList<>();
    private List<AgentCommunication> subagtagentcommunications = new ArrayList<>();
    private List<AgentCommunication> subagtagentcommunicationsByReceiveragent = new ArrayList<>();
    private List<AgentCompliance> subagtagentcompliance = new ArrayList<>();
    private List<AgentDecision> subagtagentdecisions = new ArrayList<>();
    private List<AgentDeployment> subagtagentdeployments = new ArrayList<>();
    private List<AgentEthicsAssessment> subagtagentethicsassessments = new ArrayList<>();
    private List<AgentExpertise> subagtagentexpertise = new ArrayList<>();
    private List<AgentGovernance> subagtagentgovernance = new ArrayList<>();
    private List<AgentHealth> subagtagenthealth = new ArrayList<>();
    private List<AgentInteraction> subagtagentinteractions = new ArrayList<>();
    private List<AgentMonitoring> subagtagentmonitoring = new ArrayList<>();
    private List<AgentRollback> subagtagentrollbacks = new ArrayList<>();
    private List<AgentTool> subagtagenttools = new ArrayList<>();
    private List<AgentTransparency> subagtagenttransparency = new ArrayList<>();
    private List<AgentVersion> subagtagentversions = new ArrayList<>();
    private boolean subagtagentalertsLoaded = false;
    private boolean subagtagentapprovalsLoaded = false;
    private boolean subagtagentbiasdetectionsLoaded = false;
    private boolean subagtagentcollaborationsLoaded = false;
    private boolean subagtagentcollaborationsByAgent2Loaded = false;
    private boolean subagtagentcommunicationsLoaded = false;
    private boolean subagtagentcommunicationsByReceiveragentLoaded = false;
    private boolean subagtagentcomplianceLoaded = false;
    private boolean subagtagentdecisionsLoaded = false;
    private boolean subagtagentdeploymentsLoaded = false;
    private boolean subagtagentethicsassessmentsLoaded = false;
    private boolean subagtagentexpertiseLoaded = false;
    private boolean subagtagentgovernanceLoaded = false;
    private boolean subagtagenthealthLoaded = false;
    private boolean subagtagentinteractionsLoaded = false;
    private boolean subagtagentmonitoringLoaded = false;
    private boolean subagtagentrollbacksLoaded = false;
    private boolean subagtagenttoolsLoaded = false;
    private boolean subagtagenttransparencyLoaded = false;
    private boolean subagtagentversionsLoaded = false;
    
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
            idxagent = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentDetailViewModel - mode: {}, idxagent: {}", mode, idxagent);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxagent != null) {
            loadItem(idxagent);
        } else {
            log.error("Modo inválido o falta idxagent");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgent, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgent = new Agent();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgttypes();
        loadAgtstatuss();
        loadAgtapprovalstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgent = businessService.findById(Agent.class, id);
            
            if (currentAgent == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgent.getAgtname();
        loadAgttypes();
        loadAgtstatuss();
        loadAgtapprovalstatuss();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalAgtname = currentAgent.getAgtname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTAGENTS", id, "Consulta: " + currentAgent.getAgtname());
            
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
            
            boolean isNew = currentAgent.getIdxagent() == null;
            
            if (isNew) {
                businessService.save(currentAgent);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTAGENTS", currentAgent.getIdxagent(), 
                    "Creado: " + currentAgent.getAgtname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentAgent);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTAGENTS", currentAgent.getIdxagent(), 
                    "Actualizado: " + currentAgent.getAgtname());
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
        
        if (currentAgent.getAgtname() == null || currentAgent.getAgtname().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentAgent.getAgtname() != null && currentAgent.getAgtname().length() > 255) {
            errors.append("- Name no puede exceder 255 caracteres\n");
        }
        if (currentAgent.getAgttype() == null || currentAgent.getAgttype().trim().isEmpty()) {
            errors.append("- Type\n");
        }
        if (currentAgent.getAgtversion() == null || currentAgent.getAgtversion().trim().isEmpty()) {
            errors.append("- Version\n");
        }
        if (currentAgent.getAgtversion() != null && currentAgent.getAgtversion().length() > 50) {
            errors.append("- Version no puede exceder 50 caracteres\n");
        }
        if (currentAgent.getAgtstatus() == null || currentAgent.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgent.getAgtcreatedby() == null || currentAgent.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgent.getAgtcreatedby() != null && currentAgent.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgent.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxagent);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgttypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgttypes.add("OPTION_1");
        availableAgttypes.add("OPTION_2");
        availableAgttypes.add("OPTION_3");
    }
    
    private void loadAgtstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtstatuss.add("OPTION_1");
        availableAgtstatuss.add("OPTION_2");
        availableAgtstatuss.add("OPTION_3");
    }
    
    private void loadAgtapprovalstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtapprovalstatuss.add("OPTION_1");
        availableAgtapprovalstatuss.add("OPTION_2");
        availableAgtapprovalstatuss.add("OPTION_3");
    }
    
    private void loadSubagtagentalerts() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentAlert> result = businessService.findAllEntity(AgentAlert.class, collectionParams, criterias);
                subagtagentalerts = result != null ? result.getContent() : new ArrayList<>();
                subagtagentalertsLoaded = true;
                log.debug("Cargados {} subagtagentalerts", subagtagentalerts.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentalerts", e);
            subagtagentalerts = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentapprovals() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentApproval> result = businessService.findAllEntity(AgentApproval.class, collectionParams, criterias);
                subagtagentapprovals = result != null ? result.getContent() : new ArrayList<>();
                subagtagentapprovalsLoaded = true;
                log.debug("Cargados {} subagtagentapprovals", subagtagentapprovals.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentapprovals", e);
            subagtagentapprovals = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentbiasdetections() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentBiasDetection> result = businessService.findAllEntity(AgentBiasDetection.class, collectionParams, criterias);
                subagtagentbiasdetections = result != null ? result.getContent() : new ArrayList<>();
                subagtagentbiasdetectionsLoaded = true;
                log.debug("Cargados {} subagtagentbiasdetections", subagtagentbiasdetections.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentbiasdetections", e);
            subagtagentbiasdetections = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentcollaborations() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent1");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentCollaboration> result = businessService.findAllEntity(AgentCollaboration.class, collectionParams, criterias);
                subagtagentcollaborations = result != null ? result.getContent() : new ArrayList<>();
                subagtagentcollaborationsLoaded = true;
                log.debug("Cargados {} subagtagentcollaborations", subagtagentcollaborations.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentcollaborations", e);
            subagtagentcollaborations = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentcollaborationsByAgent2() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent2");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentCollaboration> result = businessService.findAllEntity(AgentCollaboration.class, collectionParams, criterias);
                subagtagentcollaborationsByAgent2 = result != null ? result.getContent() : new ArrayList<>();
                subagtagentcollaborationsByAgent2Loaded = true;
                log.debug("Cargados {} subagtagentcollaborationsByAgent2", subagtagentcollaborationsByAgent2.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentcollaborationsByAgent2", e);
            subagtagentcollaborationsByAgent2 = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentcommunications() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "senderAgent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentCommunication> result = businessService.findAllEntity(AgentCommunication.class, collectionParams, criterias);
                subagtagentcommunications = result != null ? result.getContent() : new ArrayList<>();
                subagtagentcommunicationsLoaded = true;
                log.debug("Cargados {} subagtagentcommunications", subagtagentcommunications.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentcommunications", e);
            subagtagentcommunications = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentcommunicationsByReceiveragent() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "receiverAgent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentCommunication> result = businessService.findAllEntity(AgentCommunication.class, collectionParams, criterias);
                subagtagentcommunicationsByReceiveragent = result != null ? result.getContent() : new ArrayList<>();
                subagtagentcommunicationsByReceiveragentLoaded = true;
                log.debug("Cargados {} subagtagentcommunicationsByReceiveragent", subagtagentcommunicationsByReceiveragent.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentcommunicationsByReceiveragent", e);
            subagtagentcommunicationsByReceiveragent = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentcompliance() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentCompliance> result = businessService.findAllEntity(AgentCompliance.class, collectionParams, criterias);
                subagtagentcompliance = result != null ? result.getContent() : new ArrayList<>();
                subagtagentcomplianceLoaded = true;
                log.debug("Cargados {} subagtagentcompliance", subagtagentcompliance.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentcompliance", e);
            subagtagentcompliance = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentdecisions() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentDecision> result = businessService.findAllEntity(AgentDecision.class, collectionParams, criterias);
                subagtagentdecisions = result != null ? result.getContent() : new ArrayList<>();
                subagtagentdecisionsLoaded = true;
                log.debug("Cargados {} subagtagentdecisions", subagtagentdecisions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentdecisions", e);
            subagtagentdecisions = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentdeployments() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentDeployment> result = businessService.findAllEntity(AgentDeployment.class, collectionParams, criterias);
                subagtagentdeployments = result != null ? result.getContent() : new ArrayList<>();
                subagtagentdeploymentsLoaded = true;
                log.debug("Cargados {} subagtagentdeployments", subagtagentdeployments.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentdeployments", e);
            subagtagentdeployments = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentethicsassessments() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentEthicsAssessment> result = businessService.findAllEntity(AgentEthicsAssessment.class, collectionParams, criterias);
                subagtagentethicsassessments = result != null ? result.getContent() : new ArrayList<>();
                subagtagentethicsassessmentsLoaded = true;
                log.debug("Cargados {} subagtagentethicsassessments", subagtagentethicsassessments.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentethicsassessments", e);
            subagtagentethicsassessments = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentexpertise() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
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
    
    private void loadSubagtagentgovernance() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentGovernance> result = businessService.findAllEntity(AgentGovernance.class, collectionParams, criterias);
                subagtagentgovernance = result != null ? result.getContent() : new ArrayList<>();
                subagtagentgovernanceLoaded = true;
                log.debug("Cargados {} subagtagentgovernance", subagtagentgovernance.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentgovernance", e);
            subagtagentgovernance = new ArrayList<>();
        }
    }
    
    private void loadSubagtagenthealth() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentHealth> result = businessService.findAllEntity(AgentHealth.class, collectionParams, criterias);
                subagtagenthealth = result != null ? result.getContent() : new ArrayList<>();
                subagtagenthealthLoaded = true;
                log.debug("Cargados {} subagtagenthealth", subagtagenthealth.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagenthealth", e);
            subagtagenthealth = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentinteractions() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentInteraction> result = businessService.findAllEntity(AgentInteraction.class, collectionParams, criterias);
                subagtagentinteractions = result != null ? result.getContent() : new ArrayList<>();
                subagtagentinteractionsLoaded = true;
                log.debug("Cargados {} subagtagentinteractions", subagtagentinteractions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentinteractions", e);
            subagtagentinteractions = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentmonitoring() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentMonitoring> result = businessService.findAllEntity(AgentMonitoring.class, collectionParams, criterias);
                subagtagentmonitoring = result != null ? result.getContent() : new ArrayList<>();
                subagtagentmonitoringLoaded = true;
                log.debug("Cargados {} subagtagentmonitoring", subagtagentmonitoring.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentmonitoring", e);
            subagtagentmonitoring = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentrollbacks() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentRollback> result = businessService.findAllEntity(AgentRollback.class, collectionParams, criterias);
                subagtagentrollbacks = result != null ? result.getContent() : new ArrayList<>();
                subagtagentrollbacksLoaded = true;
                log.debug("Cargados {} subagtagentrollbacks", subagtagentrollbacks.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentrollbacks", e);
            subagtagentrollbacks = new ArrayList<>();
        }
    }
    
    private void loadSubagtagenttools() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentTool> result = businessService.findAllEntity(AgentTool.class, collectionParams, criterias);
                subagtagenttools = result != null ? result.getContent() : new ArrayList<>();
                subagtagenttoolsLoaded = true;
                log.debug("Cargados {} subagtagenttools", subagtagenttools.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagenttools", e);
            subagtagenttools = new ArrayList<>();
        }
    }
    
    private void loadSubagtagenttransparency() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentTransparency> result = businessService.findAllEntity(AgentTransparency.class, collectionParams, criterias);
                subagtagenttransparency = result != null ? result.getContent() : new ArrayList<>();
                subagtagenttransparencyLoaded = true;
                log.debug("Cargados {} subagtagenttransparency", subagtagenttransparency.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagenttransparency", e);
            subagtagenttransparency = new ArrayList<>();
        }
    }
    
    private void loadSubagtagentversions() {
        try {
            if (currentAgent != null && currentAgent.getIdxagent() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agent");
                criteria.setValues(new Object[]{currentAgent.getIdxagent()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentVersion> result = businessService.findAllEntity(AgentVersion.class, collectionParams, criterias);
                subagtagentversions = result != null ? result.getContent() : new ArrayList<>();
                subagtagentversionsLoaded = true;
                log.debug("Cargados {} subagtagentversions", subagtagentversions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtagentversions", e);
            subagtagentversions = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subagtagentalerts")
    public void onSelectSubagtagentalertsTab() {
        if (!subagtagentalertsLoaded) {
            loadSubagtagentalerts();
        }
    }
    
    @Command
    @NotifyChange("subagtagentapprovals")
    public void onSelectSubagtagentapprovalsTab() {
        if (!subagtagentapprovalsLoaded) {
            loadSubagtagentapprovals();
        }
    }
    
    @Command
    @NotifyChange("subagtagentbiasdetections")
    public void onSelectSubagtagentbiasdetectionsTab() {
        if (!subagtagentbiasdetectionsLoaded) {
            loadSubagtagentbiasdetections();
        }
    }
    
    @Command
    @NotifyChange("subagtagentcollaborations")
    public void onSelectSubagtagentcollaborationsTab() {
        if (!subagtagentcollaborationsLoaded) {
            loadSubagtagentcollaborations();
        }
    }
    
    @Command
    @NotifyChange("subagtagentcollaborationsByAgent2")
    public void onSelectSubagtagentcollaborationsByAgent2Tab() {
        if (!subagtagentcollaborationsByAgent2Loaded) {
            loadSubagtagentcollaborationsByAgent2();
        }
    }
    
    @Command
    @NotifyChange("subagtagentcommunications")
    public void onSelectSubagtagentcommunicationsTab() {
        if (!subagtagentcommunicationsLoaded) {
            loadSubagtagentcommunications();
        }
    }
    
    @Command
    @NotifyChange("subagtagentcommunicationsByReceiveragent")
    public void onSelectSubagtagentcommunicationsByReceiveragentTab() {
        if (!subagtagentcommunicationsByReceiveragentLoaded) {
            loadSubagtagentcommunicationsByReceiveragent();
        }
    }
    
    @Command
    @NotifyChange("subagtagentcompliance")
    public void onSelectSubagtagentcomplianceTab() {
        if (!subagtagentcomplianceLoaded) {
            loadSubagtagentcompliance();
        }
    }
    
    @Command
    @NotifyChange("subagtagentdecisions")
    public void onSelectSubagtagentdecisionsTab() {
        if (!subagtagentdecisionsLoaded) {
            loadSubagtagentdecisions();
        }
    }
    
    @Command
    @NotifyChange("subagtagentdeployments")
    public void onSelectSubagtagentdeploymentsTab() {
        if (!subagtagentdeploymentsLoaded) {
            loadSubagtagentdeployments();
        }
    }
    
    @Command
    @NotifyChange("subagtagentethicsassessments")
    public void onSelectSubagtagentethicsassessmentsTab() {
        if (!subagtagentethicsassessmentsLoaded) {
            loadSubagtagentethicsassessments();
        }
    }
    
    @Command
    @NotifyChange("subagtagentexpertise")
    public void onSelectSubagtagentexpertiseTab() {
        if (!subagtagentexpertiseLoaded) {
            loadSubagtagentexpertise();
        }
    }
    
    @Command
    @NotifyChange("subagtagentgovernance")
    public void onSelectSubagtagentgovernanceTab() {
        if (!subagtagentgovernanceLoaded) {
            loadSubagtagentgovernance();
        }
    }
    
    @Command
    @NotifyChange("subagtagenthealth")
    public void onSelectSubagtagenthealthTab() {
        if (!subagtagenthealthLoaded) {
            loadSubagtagenthealth();
        }
    }
    
    @Command
    @NotifyChange("subagtagentinteractions")
    public void onSelectSubagtagentinteractionsTab() {
        if (!subagtagentinteractionsLoaded) {
            loadSubagtagentinteractions();
        }
    }
    
    @Command
    @NotifyChange("subagtagentmonitoring")
    public void onSelectSubagtagentmonitoringTab() {
        if (!subagtagentmonitoringLoaded) {
            loadSubagtagentmonitoring();
        }
    }
    
    @Command
    @NotifyChange("subagtagentrollbacks")
    public void onSelectSubagtagentrollbacksTab() {
        if (!subagtagentrollbacksLoaded) {
            loadSubagtagentrollbacks();
        }
    }
    
    @Command
    @NotifyChange("subagtagenttools")
    public void onSelectSubagtagenttoolsTab() {
        if (!subagtagenttoolsLoaded) {
            loadSubagtagenttools();
        }
    }
    
    @Command
    @NotifyChange("subagtagenttransparency")
    public void onSelectSubagtagenttransparencyTab() {
        if (!subagtagenttransparencyLoaded) {
            loadSubagtagenttransparency();
        }
    }
    
    @Command
    @NotifyChange("subagtagentversions")
    public void onSelectSubagtagentversionsTab() {
        if (!subagtagentversionsLoaded) {
            loadSubagtagentversions();
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
            currentAgent = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgttypes != null) {
                availableAgttypes.clear();
                availableAgttypes = null;
            }
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            if (availableAgtapprovalstatuss != null) {
                availableAgtapprovalstatuss.clear();
                availableAgtapprovalstatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subagtagentalerts != null) {
                subagtagentalerts.clear();
                subagtagentalerts = null;
            }
            subagtagentalertsLoaded = false;
            if (subagtagentapprovals != null) {
                subagtagentapprovals.clear();
                subagtagentapprovals = null;
            }
            subagtagentapprovalsLoaded = false;
            if (subagtagentbiasdetections != null) {
                subagtagentbiasdetections.clear();
                subagtagentbiasdetections = null;
            }
            subagtagentbiasdetectionsLoaded = false;
            if (subagtagentcollaborations != null) {
                subagtagentcollaborations.clear();
                subagtagentcollaborations = null;
            }
            subagtagentcollaborationsLoaded = false;
            if (subagtagentcollaborationsByAgent2 != null) {
                subagtagentcollaborationsByAgent2.clear();
                subagtagentcollaborationsByAgent2 = null;
            }
            subagtagentcollaborationsByAgent2Loaded = false;
            if (subagtagentcommunications != null) {
                subagtagentcommunications.clear();
                subagtagentcommunications = null;
            }
            subagtagentcommunicationsLoaded = false;
            if (subagtagentcommunicationsByReceiveragent != null) {
                subagtagentcommunicationsByReceiveragent.clear();
                subagtagentcommunicationsByReceiveragent = null;
            }
            subagtagentcommunicationsByReceiveragentLoaded = false;
            if (subagtagentcompliance != null) {
                subagtagentcompliance.clear();
                subagtagentcompliance = null;
            }
            subagtagentcomplianceLoaded = false;
            if (subagtagentdecisions != null) {
                subagtagentdecisions.clear();
                subagtagentdecisions = null;
            }
            subagtagentdecisionsLoaded = false;
            if (subagtagentdeployments != null) {
                subagtagentdeployments.clear();
                subagtagentdeployments = null;
            }
            subagtagentdeploymentsLoaded = false;
            if (subagtagentethicsassessments != null) {
                subagtagentethicsassessments.clear();
                subagtagentethicsassessments = null;
            }
            subagtagentethicsassessmentsLoaded = false;
            if (subagtagentexpertise != null) {
                subagtagentexpertise.clear();
                subagtagentexpertise = null;
            }
            subagtagentexpertiseLoaded = false;
            if (subagtagentgovernance != null) {
                subagtagentgovernance.clear();
                subagtagentgovernance = null;
            }
            subagtagentgovernanceLoaded = false;
            if (subagtagenthealth != null) {
                subagtagenthealth.clear();
                subagtagenthealth = null;
            }
            subagtagenthealthLoaded = false;
            if (subagtagentinteractions != null) {
                subagtagentinteractions.clear();
                subagtagentinteractions = null;
            }
            subagtagentinteractionsLoaded = false;
            if (subagtagentmonitoring != null) {
                subagtagentmonitoring.clear();
                subagtagentmonitoring = null;
            }
            subagtagentmonitoringLoaded = false;
            if (subagtagentrollbacks != null) {
                subagtagentrollbacks.clear();
                subagtagentrollbacks = null;
            }
            subagtagentrollbacksLoaded = false;
            if (subagtagenttools != null) {
                subagtagenttools.clear();
                subagtagenttools = null;
            }
            subagtagenttoolsLoaded = false;
            if (subagtagenttransparency != null) {
                subagtagenttransparency.clear();
                subagtagenttransparency = null;
            }
            subagtagenttransparencyLoaded = false;
            if (subagtagentversions != null) {
                subagtagentversions.clear();
                subagtagentversions = null;
            }
            subagtagentversionsLoaded = false;
            
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
