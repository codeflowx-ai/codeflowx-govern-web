package com.codeflowx.platform.viewmodel.playground;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.playground.PlaygroundSessionService;

import codeflowx.nocode.persist.Criterias;

import lombok.extern.slf4j.Slf4j;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@Slf4j
public class PlaygroundOverviewViewModel extends BaseFront<PlaygroundOverviewViewModel> {

    @WireVariable
    private PlaygroundSessionService playgroundSessionService;

    private List<PlaygroundSession> allSessions = new ArrayList<>();
    private List<PlaygroundSession> filteredSessions = new ArrayList<>();

    private String searchTerm = "";

    // Pagination
    private int activePage = 0;
    private int pageSize = 10;

    // Statistics
    private long totalSessions = 0;
    private long activeSessions = 0;
    private long totalMessages = 0;
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Init(superclass = true)
    public void init() {
        logActivity("ACCESS", "PLAYGROUND", null, "Usuario accedió al módulo de Playground");
        loadSessions();
        calculateStatistics();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Destroy
    public void destroy() {
        logActivity("LEAVE", "PLAYGROUND", null, "Usuario salió del módulo de Playground");
    }

    private void loadSessions() {
        try {
            Criterias criterias = new Criterias();
            allSessions = playgroundSessionService.findAll(criterias);
            applyFilters();
        } catch (GovernanceServiceException e) {
            log.error("Error loading playground sessions", e);
            Messagebox.show("Error al cargar las sesiones: " + e.getMessage(),
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"filteredSessions", "sessionsList", "totalSize"})
    public void applyFilters() {
        filteredSessions = allSessions.stream()
            .filter(session -> {
                if (StringUtils.isNotBlank(searchTerm) &&
                    !session.getSessionname().toLowerCase().contains(searchTerm.toLowerCase())) {
                    return false;
                }
                return true;
            })
            .collect(Collectors.toList());
    }

    @Command
    public void createSession() {
        logActivity("CREATE_INIT", "PLAYGROUND", null, "Iniciando creación de nueva sesión");
        // Navigate to create session or show dialog
    }

    @Command
    public void openChat() {
        logActivity("OPEN_CHAT", "PLAYGROUND", null, "Abriendo módulo de Chat");
        Executions.sendRedirect("/console/platform/playground/chat/page.zul");
    }

    @Command
    public void openImage() {
        logActivity("OPEN_IMAGE", "PLAYGROUND", null, "Abriendo módulo de Generación de Imágenes");
        Executions.sendRedirect("/console/platform/playground/image/page.zul");
    }

    @Command
    public void openVoice() {
        logActivity("OPEN_VOICE", "PLAYGROUND", null, "Abriendo módulo de Voz");
        Executions.sendRedirect("/console/platform/playground/voice/page.zul");
    }

    @Command
    public void openTranslation() {
        logActivity("OPEN_TRANSLATION", "PLAYGROUND", null, "Abriendo módulo de Traducción");
        Executions.sendRedirect("/console/platform/playground/translation/page.zul");
    }

    @Command
    public void openRouting() {
        logActivity("OPEN_ROUTING", "PLAYGROUND", null, "Abriendo módulo de Routing");
        Executions.sendRedirect("/console/platform/playground/routing/page.zul");
    }

    @Command
    public void viewSession(PlaygroundSession session) {
        logActivity("VIEW_SESSION", "PLAYGROUND", session.getIdxplaygroundsession(), "Viendo detalles de sesión: " + session.getSessionname());
        // Navigate to session details based on type
        switch (session.getSessiontype()) {
            case "CHAT":
                Executions.sendRedirect("/console/platform/playground/chat/page.zul?sessionId=" + session.getIdxplaygroundsession());
                break;
            case "IMAGE":
                Executions.sendRedirect("/console/platform/playground/image/page.zul?sessionId=" + session.getIdxplaygroundsession());
                break;
            case "VOICE":
                Executions.sendRedirect("/console/platform/playground/voice/page.zul?sessionId=" + session.getIdxplaygroundsession());
                break;
            case "TRANSLATION":
                Executions.sendRedirect("/console/platform/playground/translation/page.zul?sessionId=" + session.getIdxplaygroundsession());
                break;
            case "ROUTING":
                Executions.sendRedirect("/console/platform/playground/routing/page.zul?sessionId=" + session.getIdxplaygroundsession());
                break;
        }
    }

    @Command
    @NotifyChange({"allSessions", "filteredSessions", "sessionsList", "totalSessions", "activeSessions"})
    public void deleteSession(PlaygroundSession session) {
        Messagebox.show("¿Está seguro de eliminar la sesión '" + session.getSessionname() + "'?",
            "Confirmar Eliminación", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        playgroundSessionService.deleteById(session.getIdxplaygroundsession());
                        Messagebox.show("Sesión eliminada correctamente",
                                      "Éxito", Messagebox.OK, Messagebox.INFORMATION);

                        logActivity("DELETE_SESSION", "PLAYGROUND", session.getIdxplaygroundsession(), "Sesión eliminada: " + session.getSessionname());
                        loadSessions();
                        calculateStatistics();
                    } catch (GovernanceServiceException e) {
                        log.error("Error deleting session", e);
                        Messagebox.show("Error al eliminar: " + e.getMessage(),
                                      "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    @Command
    @NotifyChange({"sessionsList"})
    public void changePage() {
        logActivity("PAGE_CHANGE", "PLAYGROUND", null, "Cambio a página: " + activePage);
    }

    @NotifyChange({"totalSessions", "activeSessions", "totalMessages", "totalCost"})
    private void calculateStatistics() {
        totalSessions = allSessions.size();
        activeSessions = allSessions.stream()
            .filter(session -> "ACTIVE".equals(session.getSessionstatus()))
            .count();

        totalMessages = allSessions.stream()
            .filter(session -> session.getMessagecount() != null)
            .mapToLong(PlaygroundSession::getMessagecount)
            .sum();

        totalCost = allSessions.stream()
            .filter(session -> session.getCost() != null)
            .map(PlaygroundSession::getCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and Setters
    public List<PlaygroundSession> getSessionsList() {
        int start = activePage * pageSize;
        int end = Math.min(start + pageSize, filteredSessions.size());
        return start < filteredSessions.size() ?
               filteredSessions.subList(start, end) : new ArrayList<>();
    }

    public int getTotalSize() {
        return filteredSessions.size();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
        applyFilters();
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

    public long getTotalSessions() {
        return totalSessions;
    }

    public long getActiveSessions() {
        return activeSessions;
    }

    public long getTotalMessages() {
        return totalMessages;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
}
