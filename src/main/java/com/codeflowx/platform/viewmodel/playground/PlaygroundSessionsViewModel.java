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
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import codeflowx.nocode.persist.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class PlaygroundSessionsViewModel extends BaseFront<PlaygroundSessionsViewModel> {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private List<PlaygroundSession> sessionsList = new ArrayList<>();
    private PageParams pageParams;
    private PageResult<PlaygroundSession> pageResult;
    
    // Métricas
    private int activeSessions = 0;
    private long totalTokens = 0L;
    private java.math.BigDecimal totalCost = java.math.BigDecimal.ZERO;
    private int totalViolations = 0;
    private int complianceRate = 100;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadSessions();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void loadSessions() {
        try {
            pageResult = businessService.findAllEntity(PlaygroundSession.class, pageParams, new Criterias());
            if (pageResult != null && pageResult.getContent() != null) {
                sessionsList = pageResult.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar sesiones", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        try {
            activeSessions = (int) sessionsList.stream()
                .filter(s -> "ACTIVE".equals(s.getSessionstatus()))
                .count();
            
            totalTokens = sessionsList.stream()
                .mapToLong(s -> s.getTokensused() != null ? s.getTokensused() : 0L)
                .sum();
            
            totalCost = sessionsList.stream()
                .map(s -> s.getCost() != null ? s.getCost() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            
            long compliantSessions = sessionsList.stream()
                .filter(s -> "COMPLIANT".equals(s.getCompliancestatus()))
                .count();
            
            if (!sessionsList.isEmpty()) {
                complianceRate = (int) ((compliantSessions * 100) / sessionsList.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }
    
    @Command
    public void createSession() {
        Messagebox.show("Select session type to create", "New Session", 
            Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    public void openChat() {
        Executions.sendRedirect("/playground/chat/page.zul");
    }
    
    @Command
    public void openImage() {
        Executions.sendRedirect("/playground/image/page.zul");
    }
    
    @Command
    public void openVoice() {
        Executions.sendRedirect("/playground/voice/page.zul");
    }
    
    @Command
    public void openTranslation() {
        Executions.sendRedirect("/playground/translation/page.zul");
    }
    
    @Command
    public void openRouting() {
        Executions.sendRedirect("/playground/routing/page.zul");
    }
    
    @Command
    @NotifyChange("*")
    public void refreshSessions() {
        loadSessions();
        loadMetrics();
    }
    
    @Command
    public void openSession(@BindingParam("session") PlaygroundSession session) {
        String url = "/playground/" + session.getSessiontype().toLowerCase() + "/page.zul?sessionId=" + session.getIdxplaygroundsession();
        Executions.sendRedirect(url);
    }
    
    @Command
    @NotifyChange("*")
    public void deleteSession(@BindingParam("session") PlaygroundSession session) {
        Messagebox.show("¿Eliminar sesión: " + session.getSessionname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(session);
                        loadSessions();
                        loadMetrics();
                    } catch (Exception e) {
                        log.error("Error al eliminar sesión", e);
                    }
                }
            });
    }
    
    public String getStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "ACTIVE": return "success";
            case "COMPLETED": return "info";
            case "ERROR": return "danger";
            default: return "secondary";
        }
    }
    
    public String getComplianceColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "COMPLIANT": return "success";
            case "NON_COMPLIANT": return "danger";
            case "PENDING_REVIEW": return "warning";
            default: return "secondary";
        }
    }
    
    public String getRiskColor(String risk) {
        if (risk == null) return "secondary";
        switch (risk) {
            case "LOW": return "success";
            case "MEDIUM": return "warning";
            case "HIGH": return "danger";
            case "CRITICAL": return "dark";
            default: return "secondary";
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
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
