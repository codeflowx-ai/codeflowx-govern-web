package com.codeflowx.platform.viewmodel.playground;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.playground.PlaygroundRouting;
import com.codeflowx.govern.entity.agents.Agent;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class PlaygroundRoutingViewModel extends BaseFront<PlaygroundRoutingViewModel> {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private String routingQuery;
    private String routingStrategy = "SIMILARITY";
    private Double confidenceThreshold = 0.7;
    private boolean isRouting = false;
    
    private Agent selectedAgent;
    private String selectedAgentName;
    private String selectedAgentDescription;
    private Double routingConfidence = 0.0;
    
    private boolean showRoutingScores = false;
    private List<AgentScore> agentScoresList = new ArrayList<>();
    private List<PlaygroundRouting> routingsList = new ArrayList<>();
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        loadRoutingHistory();
    }
    
    @Command
    @NotifyChange("*")
    public void loadRoutingHistory() {
        try {
            PageParams params = PageParams.builder().maxRows(20).pageActual(1).build();
            PageResult<PlaygroundRouting> result = businessService.findAllEntity(
                PlaygroundRouting.class, params, new Criterias());
            
            if (result != null && result.getContent() != null) {
                routingsList = result.getContent();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "PLAYGROUNDROUTINGS", null, 
                    "Búsqueda: " + routingsList.size() + " routings");
            } else {
                routingsList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error loading routing history", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void routeQuery() {
        if (routingQuery == null || routingQuery.trim().isEmpty()) {
            Messagebox.show("Please enter a query", "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            isRouting = true;
            showRoutingScores = true;
            
            // TODO: Integración con leka-server para routing inteligente
            // Simulación de scores
            agentScoresList = simulateAgentScores();
            
            // Seleccionar el agente con mayor score
            if (!agentScoresList.isEmpty()) {
                AgentScore best = agentScoresList.get(0);
                selectedAgentName = best.agentName;
                selectedAgentDescription = best.agentCapability;
                routingConfidence = best.finalScore;
            }
            
        } catch (Exception e) {
            log.error("Error routing query", e);
        } finally {
            isRouting = false;
        }
    }
    
    @Command
    @NotifyChange("*")
    public void markCorrect(@BindingParam("routing") PlaygroundRouting routing) {
        try {
            routing.setUserfeedback("CORRECT");
            routing.setRoutingaccuracy(new java.math.BigDecimal("1.0"));
            businessService.save(routing);
            
            // Auditar actualización
            logActivity("EDITAR", "PLAYGROUNDROUTINGS", routing.getIdxplaygroundrouting(), 
                "Feedback: CORRECT");
            
            loadRoutingHistory();
        } catch (Exception e) {
            log.error("Error updating feedback", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void markIncorrect(@BindingParam("routing") PlaygroundRouting routing) {
        try {
            routing.setUserfeedback("INCORRECT");
            routing.setRoutingaccuracy(new java.math.BigDecimal("0.0"));
            businessService.save(routing);
            
            // Auditar actualización
            logActivity("EDITAR", "PLAYGROUNDROUTINGS", routing.getIdxplaygroundrouting(), 
                "Feedback: INCORRECT");
            
            loadRoutingHistory();
        } catch (Exception e) {
            log.error("Error updating feedback", e);
        }
    }
    
    private List<AgentScore> simulateAgentScores() {
        // TODO: Reemplazar con scores reales de leka-server
        List<AgentScore> scores = new ArrayList<>();
        scores.add(new AgentScore("Customer Support Agent", "Customer service", 95.0, 90.0, 10.0, 92.0, true));
        scores.add(new AgentScore("Technical Agent", "Technical support", 80.0, 85.0, 20.0, 82.0, false));
        scores.add(new AgentScore("Sales Agent", "Sales", 60.0, 70.0, 15.0, 65.0, false));
        return scores;
    }
    
    public String getAgentName(Long agentId) {
        // TODO: Buscar nombre real del agente
        return agentId != null ? "Agent #" + agentId : "-";
    }
    
    public String getFeedbackColor(String feedback) {
        if (feedback == null) return "secondary";
        switch (feedback) {
            case "CORRECT": return "success";
            case "INCORRECT": return "danger";
            default: return "secondary";
        }
    }
    
    public String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length) + "..." : text;
    }
    
    public String formatDate(Timestamp ts) {
        return ts != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "-";
    }
    
    @Destroy
    public void destroy() {
        if (routingsList != null) { 
            routingsList.clear(); 
            routingsList = null; 
        }
        if (agentScoresList != null) {
            agentScoresList.clear();
            agentScoresList = null;
        }
        businessService = null;
    }
    
    // Inner class para scores de agentes
    @Getter
    @Setter
    @AllArgsConstructor
    public static class AgentScore {
        private String agentName;
        private String agentCapability;
        private Double capabilityMatch;
        private Double similarityScore;
        private Double currentLoad;
        private Double finalScore;
        private boolean selected;
    }
}
