package com.codeflowx.govern.viewmodel.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.service.prompts.PromptService;
import com.codeflowx.govern.service.agents.AgentService;
import com.codeflowx.govern.service.rag.RagSystemService;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Dashboard de Catálogo
 * Vista consolidada de todos los artefactos (Models, Agents, Prompts, RAG)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class CatalogDashboardViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private ModelService modelService;
    @WireVariable
    private RagSystemService ragSystemService;
    @WireVariable
    private AgentService agentService;
    @WireVariable
    private PromptService promptService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }
    
    @Override
    public void setBeans(Object bean) {}

    private PageParams pageParams;
    
    // Listas de artefactos
    private List<Model> modelsList = new ArrayList<>();
    private List<Agent> agentsList = new ArrayList<>();
    private List<Prompt> promptsList = new ArrayList<>();
    private List<RagSystem> ragSystemsList = new ArrayList<>();
    
    // KPIs consolidados
    private Long totalModels = 0L;
    private Long totalAgents = 0L;
    private Long totalPrompts = 0L;
    private Long totalRagSystems = 0L;
    private Long totalArtefacts = 0L;
    
    // Filtros
    private String statusFilter = "ALL";
    private String typeFilter = "ALL";
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("createdat")
                .build();
        
        log.info("Inicializando CatalogDashboardViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar modelos
            String sql1 = "SELECT * FROM MDLMODELS LIMIT 100";
            List<Model> result1 = businessService.findByParams(Model.class, sql1, null);
            if (result1 != null) {
                modelsList = result1;
            }
            
            // Cargar agentes
            String sql2 = "SELECT * FROM AGTAGENTS LIMIT 100";
            List<Agent> result2 = businessService.findByParams(Agent.class, sql2, null);
            if (result2 != null) {
                agentsList = result2;
            }
            
            // Cargar prompts
            String sql3 = "SELECT * FROM PRMPROMPTS LIMIT 100";
            List<Prompt> result3 = businessService.findByParams(Prompt.class, sql3, null);
            if (result3 != null) {
                promptsList = result3;
            }
            
            // Cargar RAG systems
            String sql4 = "SELECT * FROM RAGRAGSYSTEMS LIMIT 100";
            List<RagSystem> result4 = businessService.findByParams(RagSystem.class, sql4, null);
            if (result4 != null) {
                ragSystemsList = result4;
            }
            
            log.info("Cargados {} modelos, {} agentes, {} prompts, {} RAG systems", 
                modelsList.size(), agentsList.size(), promptsList.size(), ragSystemsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos del catálogo", e);
        }
    }
    
    private void calculateKPIs() {
        totalModels = (long) modelsList.size();
        totalAgents = (long) agentsList.size();
        totalPrompts = (long) promptsList.size();
        totalRagSystems = (long) ragSystemsList.size();
        totalArtefacts = totalModels + totalAgents + totalPrompts + totalRagSystems;
        
        log.info("KPIs calculados - Total: {}, Models: {}, Agents: {}, Prompts: {}, RAG: {}", 
            totalArtefacts, totalModels, totalAgents, totalPrompts, totalRagSystems);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void filterByType(String type) {
        typeFilter = type;
        loadData();
        calculateKPIs();
    }
    
    @Command
    @NotifyChange("*")
    public void filterByStatus(String status) {
        statusFilter = status;
        try {
            String sql = "SELECT * FROM MDLMODELS WHERE MDLSTATUS = :status LIMIT 100";
            Map<String, Object> params = new HashMap<>();
            params.put("status", status);
            
            List<Model> result = businessService.findByParams(Model.class, sql, params);
            if (result != null) {
                modelsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por estado", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void searchArtifacts(String searchTerm) {
        try {
            String sql = "SELECT * FROM MDLMODELS WHERE MDLNAME LIKE :searchTerm LIMIT 100";
            Map<String, Object> params = new HashMap<>();
            params.put("searchTerm", "%" + searchTerm + "%");
            
            List<Model> result = businessService.findByParams(Model.class, sql, params);
            if (result != null) {
                modelsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al buscar artefactos", e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (modelsList != null) { modelsList.clear(); modelsList = null; }
        if (agentsList != null) { agentsList.clear(); agentsList = null; }
        if (promptsList != null) { promptsList.clear(); promptsList = null; }
        if (ragSystemsList != null) { ragSystemsList.clear(); ragSystemsList = null; }
        ragSystemService = null;
            modelService = null;
            agentService = null;
            promptService = null;
    }
}

