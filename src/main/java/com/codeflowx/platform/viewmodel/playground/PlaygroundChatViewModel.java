package com.codeflowx.platform.viewmodel.playground;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.playground.*;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class PlaygroundChatViewModel extends BaseFront<PlaygroundChatViewModel> {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    // Datos de sesión
    private Long currentSessionId;
    private String currentSessionName = "New Chat";
    private List<PlaygroundSession> sessionsList = new ArrayList<>();
    private List<PlaygroundChat> messagesList = new ArrayList<>();
    
    // Configuración
    private String selectedModel = "gpt-4";
    private Double temperature = 0.7;
    private Integer maxTokens = 2000;
    private String messageInput;
    private boolean isSending = false;
    
    // Compliance monitoring
    private String sessionComplianceStatus = "PENDING_REVIEW";
    private String sessionGovernanceStatus = "PENDING";
    private String sessionRiskLevel = "LOW";
    private int contentViolations = 0;
    private int biasDetections = 0;
    private int piiDetections = 0;
    
    // Stats
    private int sessionMessageCount = 0;
    private long sessionTokens = 0L;
    private java.math.BigDecimal sessionCost = java.math.BigDecimal.ZERO;
    private int avgLatency = 0;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        loadSessions();
        
        // Cargar sesión actual si viene por parámetro
        String sessionIdParam = (String) Executions.getCurrent().getParameter("sessionId");
        if (sessionIdParam != null) {
            currentSessionId = Long.parseLong(sessionIdParam);
            loadSessionMessages();
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadSessions() {
        try {
            PageParams params = PageParams.builder().maxRows(20).pageActual(1).build();
            Criterias criterias = new Criterias();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "sessiontype", "CHAT"));
            
            PageResult<PlaygroundSession> result = businessService.findAllEntity(PlaygroundSession.class, params, criterias);
            if (result != null && result.getContent() != null) {
                sessionsList = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar sesiones", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadSessionMessages() {
        try {
            if (currentSessionId == null) return;
            
            PageParams params = PageParams.builder().maxRows(100).pageActual(1).build();
            Criterias criterias = new Criterias();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "playgroundSession.idxplaygroundsession", currentSessionId));
            
            PageResult<PlaygroundChat> result = businessService.findAllEntity(PlaygroundChat.class, params, criterias);
            if (result != null && result.getContent() != null) {
                messagesList = result.getContent();
                calculateSessionStats();
            }
        } catch (Exception e) {
            log.error("Error al cargar mensajes", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void newSession() {
        try {
            PlaygroundSession session = new PlaygroundSession();
            session.setSessionname("Chat Session " + System.currentTimeMillis());
            session.setSessiontype("CHAT");
            session.setSessionstatus("ACTIVE");
            session.setCompliancestatus("PENDING_REVIEW");
            session.setGovernancestatus("PENDING");
            session.setRisklevel("LOW");
            session.setMessagecount(0);
            session.setTokensused(0L);
            session.setCost(java.math.BigDecimal.ZERO);
            session.setSessioncreatedby(ctxBean.getUsuario().getUsuario());
            session.setSessioncreatedat(new Timestamp(System.currentTimeMillis()));
            
            businessService.save(session);
            currentSessionId = session.getIdxplaygroundsession();
            currentSessionName = session.getSessionname();
            
            loadSessions();
            messagesList.clear();
        } catch (Exception e) {
            log.error("Error al crear sesión", e);
            Messagebox.show("Error al crear sesión: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void selectSession(@BindingParam("session") PlaygroundSession session) {
        currentSessionId = session.getIdxplaygroundsession();
        currentSessionName = session.getSessionname();
        loadSessionMessages();
    }
    
    @Command
    @NotifyChange("*")
    public void sendMessage() {
        if (messageInput == null || messageInput.trim().isEmpty()) {
            return;
        }
        
        if (currentSessionId == null) {
            newSession();
        }
        
        try {
            isSending = true;
            
            // Guardar mensaje del usuario
            PlaygroundChat userMessage = new PlaygroundChat();
            userMessage.setChatmessage(messageInput);
            userMessage.setChatrole("USER");
            userMessage.setChatmodel(selectedModel);
            userMessage.setChatcreatedat(new Timestamp(System.currentTimeMillis()));
            
            PlaygroundSession session = new PlaygroundSession();
            session.setIdxplaygroundsession(currentSessionId);
            userMessage.setPlaygroundSession(session);
            
            businessService.save(userMessage);
            
            // TODO: Integración con leka-server para obtener respuesta del modelo
            // Por ahora simulamos respuesta
            PlaygroundChat assistantMessage = new PlaygroundChat();
            assistantMessage.setChatmessage(null);
            assistantMessage.setChatresponse("[Respuesta del modelo - Requiere integración con leka-server]");
            assistantMessage.setChatrole("ASSISTANT");
            assistantMessage.setChatmodel(selectedModel);
            assistantMessage.setChattokens(100);
            assistantMessage.setChatcost(new java.math.BigDecimal("0.002"));
            assistantMessage.setChatlatency(1500);
            assistantMessage.setChatcreatedat(new Timestamp(System.currentTimeMillis()));
            assistantMessage.setPlaygroundSession(session);
            
            // Simular análisis de compliance (TODO: integrar con leka-server)
            assistantMessage.setContentviol(false);
            assistantMessage.setToxicityscore(new java.math.BigDecimal("0.1"));
            assistantMessage.setBiasdetected(false);
            assistantMessage.setPiidetected(false);
            
            businessService.save(assistantMessage);
            
            messageInput = "";
            loadSessionMessages();
            
        } catch (Exception e) {
            log.error("Error al enviar mensaje", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        } finally {
            isSending = false;
        }
    }
    
    @Command
    @NotifyChange("*")
    public void clearChat() {
        Messagebox.show("¿Eliminar todos los mensajes?", "Confirmar", 
            Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    messagesList.clear();
                    // TODO: Eliminar mensajes de BD
                }
            });
    }
    
    @Command
    public void exportChat() {
        // TODO: Implementar exportación
        Messagebox.show("Exportación en desarrollo", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    private void calculateSessionStats() {
        sessionMessageCount = messagesList.size();
        sessionTokens = messagesList.stream()
            .mapToLong(m -> m.getChattokens() != null ? m.getChattokens() : 0)
            .sum();
        
        sessionCost = messagesList.stream()
            .map(m -> m.getChatcost() != null ? m.getChatcost() : java.math.BigDecimal.ZERO)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        contentViolations = (int) messagesList.stream()
            .filter(m -> Boolean.TRUE.equals(m.getContentviol()))
            .count();
        
        biasDetections = (int) messagesList.stream()
            .filter(m -> Boolean.TRUE.equals(m.getBiasdetected()))
            .count();
        
        piiDetections = (int) messagesList.stream()
            .filter(m -> Boolean.TRUE.equals(m.getPiidetected()))
            .count();
        
        // Calcular risk level basado en violations
        if (contentViolations > 5 || biasDetections > 3) {
            sessionRiskLevel = "HIGH";
        } else if (contentViolations > 2 || biasDetections > 1) {
            sessionRiskLevel = "MEDIUM";
        } else {
            sessionRiskLevel = "LOW";
        }
    }
    
    public String getGovernanceColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "APPROVED": return "success";
            case "REJECTED": return "danger";
            default: return "warning";
        }
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
    
    @Destroy
    public void destroy() {
        if (sessionsList != null) { 
            sessionsList.clear(); 
            sessionsList = null; 
        }
        if (messagesList != null) {
            messagesList.clear();
            messagesList = null;
        }
        businessService = null;
    }
}
