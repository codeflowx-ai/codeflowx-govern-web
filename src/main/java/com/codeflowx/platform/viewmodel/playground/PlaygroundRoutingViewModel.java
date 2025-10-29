package com.codeflowx.platform.viewmodel.playground;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.playground.PlaygroundRouting;
import com.codeflowx.govern.entity.agents.Agent;
import codeflowx.nocode.persist.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PlaygroundRoutingViewModel extends MasterPage {
    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private BusinessService businessService;
    @WireVariable
    public Environment environment;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((javax.sql.DataSource) environment.getProperty("APPLICATION_DS", javax.sql.DataSource.class));
        }
    }
    
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
        initDao();
        loadRoutingHistory();
    }
    
    @Command
    @NotifyChange("*")
    public void loadRoutingHistory() {
        try {
            PageParams params = PageParams.builder().maxRows(20).pageActual(1).build();
            PageResult<PlaygroundRouting> result = businessService.findAllEntity(PlaygroundRouting.class, params, new Criterias());
            if (result != null) routingsList = result.getContent();
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
            businessService.saveEntity(routing);
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
            businessService.saveEntity(routing);
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
