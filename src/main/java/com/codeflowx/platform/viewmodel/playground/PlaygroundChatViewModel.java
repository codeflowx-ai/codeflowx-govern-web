package com.codeflowx.platform.viewmodel.playground;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.playground.PlaygroundChat;
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import com.codeflowx.govern.service.agents.AgentService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.service.playground.PlaygroundChatService;
import com.codeflowx.govern.service.playground.PlaygroundSessionService;

import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@Slf4j
public class PlaygroundChatViewModel extends BaseFront {

    @WireVariable
    private ModelService modelService;
    
    @WireVariable
    private AgentService agentService;
    
    @WireVariable
    private PlaygroundSessionService playgroundSessionService;
    
    @WireVariable
    private PlaygroundChatService playgroundChatService;

    private Long sessionId;
    private PlaygroundSession currentSession;
    private List<PlaygroundChat> messages = new ArrayList<>();
    
    private String currentPrompt = "";
    private Model selectedModel;
    private Agent selectedAgent;
    private BigDecimal temperature = new BigDecimal("1.0");
    private Integer maxTokens = 4096;
    private String systemPrompt = "Eres un asistente útil, preciso y profesional.";
    
    private List<Model> availableModels = new ArrayList<>();
    private List<Agent> availableAgents = new ArrayList<>();
    
    // Session stats
    private Integer messageCount = 0;
    private Long tokensUsed = 0L;
    private BigDecimal sessionCost = BigDecimal.ZERO;

    @Init(superclass = true)
    public void init() {
        logActivity("PLAYGROUND_CHAT", "ACCESS", null, "Usuario accedió al Chat Playground");
        loadAvailableModels();
        loadAvailableAgents();
        loadOrCreateSession();
        loadMessages();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Destroy
    public void destroy() {
        logActivity("PLAYGROUND_CHAT", "LEAVE", null, "Usuario salió del Chat Playground");
    }

    private void loadAvailableModels() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria(  "modelstatus", Operation.EQUAL, "ACTIVE", Evaluation.STRING);
            
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
            if (sessionId != null) {
                currentSession = playgroundSessionService.findById(sessionId);
            }
            
            if (currentSession == null) {
                currentSession = new PlaygroundSession();
                currentSession.setSessionname("Chat Session - " + new Timestamp(System.currentTimeMillis()));
                currentSession.setSessiontype("CHAT");
                currentSession.setSessionstatus("ACTIVE");
                currentSession.setSessioncreatedby(getUserName());
                currentSession.setSessioncreatedat(new Timestamp(System.currentTimeMillis()));
                currentSession.setMessagecount(0);
                currentSession.setTokensused(0L);
                currentSession.setCost(BigDecimal.ZERO);
                currentSession = playgroundSessionService.create(currentSession);
            }
        } catch (GovernanceServiceException e) {
            log.error("Error loading/creating session", e);
            Messagebox.show("Error al cargar la sesión: " + e.getMessage(), 
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private void loadMessages() {
        try {
            if (currentSession != null) {
                messages = currentSession.getSubplaygroundchats();
                updateSessionStats();
            }
        } catch (Exception e) {
            log.error("Error loading messages", e);
        }
    }

    @Command
    @NotifyChange({"messages", "currentPrompt", "messageCount", "tokensUsed", "sessionCost"})
    public void sendMessage() {
        if (selectedModel == null) {
            Messagebox.show("Por favor selecciona un modelo", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            // Create user message
            PlaygroundChat userMessage = new PlaygroundChat();
            userMessage.setSession(currentSession);
            userMessage.setChatprompt(currentPrompt);
            userMessage.setChatrole("USER");
            userMessage.setChatstatus("COMPLETED");
            userMessage.setModel(selectedModel);
            if (selectedAgent != null) {
                userMessage.setAgent(selectedAgent);
            }
            userMessage.setChattokensused(estimateTokens(currentPrompt));
            userMessage.setChatcreatedby(getUserName());
            userMessage.setChatcreatedat(new Timestamp(System.currentTimeMillis()));
            userMessage = playgroundChatService.create(userMessage);
            
            // Simulate assistant response (in production, call AI service)
            PlaygroundChat assistantMessage = new PlaygroundChat();
            assistantMessage.setSession(currentSession);
            assistantMessage.setChatprompt(currentPrompt);
            assistantMessage.setChatresponse("Esta es una respuesta simulada. En producción, aquí iría la respuesta del modelo " + selectedModel.getModelname());
            assistantMessage.setChatrole("ASSISTANT");
            assistantMessage.setChatstatus("COMPLETED");
            assistantMessage.setModel(selectedModel);
            if (selectedAgent != null) {
                assistantMessage.setAgent(selectedAgent);
            }
            assistantMessage.setChattokensused(estimateTokens("Esta es una respuesta simulada"));
            assistantMessage.setChatlatency(245);
            assistantMessage.setChatcost(new BigDecimal("0.0001"));
            assistantMessage.setChattemperature(temperature);
            assistantMessage.setChatmaxtokens(maxTokens);
            assistantMessage.setChatcreatedby("SYSTEM");
            assistantMessage.setChatcreatedat(new Timestamp(System.currentTimeMillis()));
            assistantMessage = playgroundChatService.create(assistantMessage);
            
            // Update session
            currentSession.setMessagecount(currentSession.getMessagecount() + 2);
            currentSession.setTokensused(currentSession.getTokensused() + userMessage.getChattokensused() + assistantMessage.getChattokensused());
            currentSession.setCost(currentSession.getCost().add(assistantMessage.getChatcost()));
            currentSession.setSessionlastaccessat(new Timestamp(System.currentTimeMillis()));
            currentSession = playgroundSessionService.update(currentSession);
            
            loadMessages();
            currentPrompt = "";
            
            logActivity("PLAYGROUND_CHAT", "SEND_MESSAGE", "Mensaje enviado en sesión: " + currentSession.getIdxplaygroundsession());
            
        } catch (GovernanceServiceException e) {
            log.error("Error sending message", e);
            Messagebox.show("Error al enviar el mensaje: " + e.getMessage(), 
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"messages", "messageCount", "tokensUsed", "sessionCost"})
    public void newConversation() {
        try {
            currentSession = new PlaygroundSession();
            currentSession.setSessionname("Chat Session - " + new Timestamp(System.currentTimeMillis()));
            currentSession.setSessiontype("CHAT");
            currentSession.setSessionstatus("ACTIVE");
            currentSession.setSessioncreatedby(getUserName());
            currentSession.setSessioncreatedat(new Timestamp(System.currentTimeMillis()));
            currentSession.setMessagecount(0);
            currentSession.setTokensused(0L);
            currentSession.setCost(BigDecimal.ZERO);
            currentSession = playgroundSessionService.create(currentSession);
            
            messages.clear();
            currentPrompt = "";
            updateSessionStats();
            
            logActivity("PLAYGROUND_CHAT", "NEW_CONVERSATION", null, "Nueva conversación creada");
            
            Messagebox.show("Nueva conversación iniciada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (GovernanceServiceException e) {
            log.error("Error creating new conversation", e);
            Messagebox.show("Error al crear nueva conversación: " + e.getMessage(), 
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void showConfig() {
        logActivity("PLAYGROUND_CHAT", "SHOW_CONFIG", null, "Mostrando configuración");
        // Show config dialog
    }

    private Integer estimateTokens(String text) {
        // Simple estimation: ~4 characters per token
        return text != null ? text.length() / 4 : 0;
    }

    private void updateSessionStats() {
        if (currentSession != null) {
            messageCount = currentSession.getMessagecount();
            tokensUsed = currentSession.getTokensused();
            sessionCost = currentSession.getCost();
        }
    }

    // Getters and Setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public List<PlaygroundChat> getMessages() {
        return messages;
    }

    public String getCurrentPrompt() {
        return currentPrompt;
    }

    public void setCurrentPrompt(String currentPrompt) {
        this.currentPrompt = currentPrompt;
    }

    public Model getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(Model selectedModel) {
        this.selectedModel = selectedModel;
    }

    public Agent getSelectedAgent() {
        return selectedAgent;
    }

    public void setSelectedAgent(Agent selectedAgent) {
        this.selectedAgent = selectedAgent;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public List<Model> getAvailableModels() {
        return availableModels;
    }

    public List<Agent> getAvailableAgents() {
        return availableAgents;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public Long getTokensUsed() {
        return tokensUsed;
    }

    public BigDecimal getSessionCost() {
        return sessionCost;
    }
}
