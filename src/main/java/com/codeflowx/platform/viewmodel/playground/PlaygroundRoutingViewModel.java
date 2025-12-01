package com.codeflowx.platform.viewmodel.playground;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.playground.PlaygroundRouting;
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import com.codeflowx.govern.service.agents.AgentService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.service.playground.PlaygroundRoutingService;
import com.codeflowx.govern.service.playground.PlaygroundSessionService;

import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;

import lombok.extern.slf4j.Slf4j;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@Slf4j
public class PlaygroundRoutingViewModel extends BaseFront<PlaygroundRoutingViewModel> {

    @WireVariable
    private ModelService modelService;

    @WireVariable
    private AgentService agentService;

    @WireVariable
    private PlaygroundSessionService playgroundSessionService;

    @WireVariable
    private PlaygroundRoutingService playgroundRoutingService;

    private PlaygroundSession currentSession;
    private List<PlaygroundRouting> allRoutings = new ArrayList<>();
    private List<PlaygroundRouting> filteredRoutings = new ArrayList<>();

    private String inputQuery = "";
    private PlaygroundRouting routingResult;

    private List<Model> availableModels = new ArrayList<>();
    private List<Agent> availableAgents = new ArrayList<>();
    private List<String> availableStatuses = List.of("PENDING", "ROUTING", "COMPLETED", "ERROR");

    private String searchTerm = "";
    private String filterStatus = "";
    private int activePage = 0;
    private int pageSize = 10;

    @Init(superclass = true)
    public void init() {
        logActivity("ACCESS", "PLAYGROUND_ROUTING", null, "Usuario accedió a Routing Playground");
        loadAvailableModels();
        loadAvailableAgents();
        loadOrCreateSession();
        loadRoutings();
    }

    @Destroy
    public void destroy() {
        logActivity("LEAVE", "PLAYGROUND_ROUTING", null, "Usuario salió de Routing Playground");
    }

    private void loadAvailableModels() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria("modelstatus", Operation.EQUAL, "ACTIVE", Evaluation.STRING);
            availableModels = modelService.findAll(criterias);
        } catch (GovernanceServiceException e) {
            log.error("Error loading models", e);
        }
    }

    private void loadAvailableAgents() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria("agentstatus", Operation.EQUAL, "ACTIVE", Evaluation.STRING);
            availableAgents = agentService.findAll(criterias);
        } catch (GovernanceServiceException e) {
            log.error("Error loading agents", e);
        }
    }

    private void loadOrCreateSession() {
        try {
            currentSession = new PlaygroundSession();
            currentSession.setSessionname("Routing Session - " + new Timestamp(System.currentTimeMillis()));
            currentSession.setSessiontype("ROUTING");
            currentSession.setSessionstatus("ACTIVE");
            currentSession.setSessioncreatedby(getUserName());
            currentSession.setSessioncreatedat(new Timestamp(System.currentTimeMillis()));
            currentSession = playgroundSessionService.create(currentSession);
        } catch (GovernanceServiceException e) {
            log.error("Error creating session", e);
        }
    }

    private void loadRoutings() {
        try {
            if (currentSession != null) {
                allRoutings = currentSession.getSubplaygroundroutings();
                applyFilters();
            }
        } catch (Exception e) {
            log.error("Error loading routings", e);
        }
    }

    @Command
    @NotifyChange({"routingResult", "filteredRoutings", "routingHistory"})
    public void analyzeAndRoute() {
        if (StringUtils.isBlank(inputQuery)) {
            Messagebox.show("Por favor ingresa una consulta", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            PlaygroundRouting routing = new PlaygroundRouting();
            routing.setSession(currentSession);
            routing.setRoutinginput(inputQuery);
            routing.setRoutingstatus("ROUTING");
            routing.setRoutingcreatedby(getUserName());
            routing.setRoutingcreatedat(new Timestamp(System.currentTimeMillis()));

            // Simulate intelligent routing (in production, use ML/AI service)
            Model selectedModel = !availableModels.isEmpty() ? availableModels.get(0) : null;
            Agent selectedAgent = !availableAgents.isEmpty() ? availableAgents.get(0) : null;

            if (selectedModel != null) {
                routing.setRoutingselectedmodel(selectedModel.getModelname());
                routing.setModel(selectedModel);
            }

            if (selectedAgent != null) {
                routing.setRoutingselectedagent(selectedAgent.getAgentname());
                routing.setAgent(selectedAgent);
            }

            routing.setRoutingconfidence(new BigDecimal("92.3"));
            routing.setRoutingreason("El modelo " + (selectedModel != null ? selectedModel.getModelname() : "N/A") +
                                     " es el más adecuado para este tipo de consulta basado en su especialización y rendimiento histórico.");
            routing.setRoutingoutput("Esta es una respuesta simulada al routing. En producción, aquí iría la respuesta del modelo/agente seleccionado.");
            routing.setRoutingstatus("COMPLETED");
            routing.setRoutingprocessingtime(450);
            routing.setRoutingcost(new BigDecimal("0.003"));

            // Simulate alternatives
            routing.setRoutingalternatives("{\"alternatives\": [{\"model\": \"gpt-4\", \"confidence\": 88.1}, {\"model\": \"claude-3\", \"confidence\": 85.7}]}");

            routing = playgroundRoutingService.create(routing);

            routingResult = routing;
            loadRoutings();

            logActivity("ANALYZE", "PLAYGROUND_ROUTING", null, "Consulta enrutada");
            Messagebox.show("Routing completado exitosamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (GovernanceServiceException e) {
            log.error("Error routing query", e);
            Messagebox.show("Error al enrutar: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"inputQuery", "routingResult"})
    public void clear() {
        inputQuery = "";
        routingResult = null;
        logActivity("CLEAR", "PLAYGROUND_ROUTING", null, "Campos limpiados");
    }

    @Command
    public void showAlternatives() {
        if (routingResult != null) {
            Messagebox.show("Alternativas: " + routingResult.getRoutingalternatives(),
                          "Alternativas de Routing", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    public void viewRouting(PlaygroundRouting routing) {
        routingResult = routing;
        inputQuery = routing.getRoutinginput();
        logActivity("VIEW", "PLAYGROUND_ROUTING", null, "Viendo detalles de routing");
    }

    @Command
    @NotifyChange({"routingResult", "filteredRoutings", "routingHistory"})
    public void rerun(PlaygroundRouting routing) {
        inputQuery = routing.getRoutinginput();
        analyzeAndRoute();
    }

    @Command
    @NotifyChange({"filteredRoutings", "routingHistory"})
    public void deleteRouting(PlaygroundRouting routing) {
        try {
            playgroundRoutingService.deleteById(routing.getIdxplaygroundrouting());
            if (routingResult != null && routingResult.getIdxplaygroundrouting().equals(routing.getIdxplaygroundrouting())) {
                routingResult = null;
            }
            loadRoutings();
            logActivity("DELETE", "PLAYGROUND_ROUTING", null, "Routing eliminado");
        } catch (GovernanceServiceException e) {
            log.error("Error deleting routing", e);
        }
    }

    @Command
    @NotifyChange({"filteredRoutings", "routingHistory"})
    public void applyFilter() {
        applyFilters();
    }

    @Command
    @NotifyChange({"filteredRoutings", "routingHistory"})
    public void search() {
        applyFilters();
    }

    @Command
    @NotifyChange({"routingHistory"})
    public void changePage() {
        logActivity("PAGE_CHANGE", "PLAYGROUND_ROUTING", null, "Cambio a página: " + activePage);
    }

    @Command
    @NotifyChange("*")
    public void newSession() {
        loadOrCreateSession();
        allRoutings.clear();
        filteredRoutings.clear();
        inputQuery = "";
        routingResult = null;
    }

    private void applyFilters() {
        filteredRoutings = allRoutings.stream()
            .filter(r -> {
                if (StringUtils.isNotBlank(searchTerm) &&
                    !r.getRoutinginput().toLowerCase().contains(searchTerm.toLowerCase())) {
                    return false;
                }
                if (StringUtils.isNotBlank(filterStatus) && !r.getRoutingstatus().equals(filterStatus)) {
                    return false;
                }
                return true;
            })
            .collect(Collectors.toList());
    }

    // Getters and Setters
    public List<PlaygroundRouting> getRoutingHistory() {
        int start = activePage * pageSize;
        int end = Math.min(start + pageSize, filteredRoutings.size());
        return start < filteredRoutings.size() ?
               filteredRoutings.subList(start, end) : new ArrayList<>();
    }

    public int getTotalSize() {
        return filteredRoutings.size();
    }

    public String getInputQuery() {
        return inputQuery;
    }

    public void setInputQuery(String inputQuery) {
        this.inputQuery = inputQuery;
    }

    public PlaygroundRouting getRoutingResult() {
        return routingResult;
    }

    public List<Model> getAvailableModels() {
        return availableModels;
    }

    public List<Agent> getAvailableAgents() {
        return availableAgents;
    }

    public List<String> getAvailableStatuses() {
        return availableStatuses;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        this.filterStatus = filterStatus;
    }

    public int getActivePage() {
        return activePage;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
