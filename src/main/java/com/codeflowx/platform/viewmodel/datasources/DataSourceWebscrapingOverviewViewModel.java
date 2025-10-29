package com.codeflowx.platform.viewmodel.datasources;

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
import com.codeflowx.govern.entity.datasources.DataSourceWebscraping;
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
public class DataSourceWebscrapingOverviewViewModel extends BaseFront<DataSourceWebscrapingOverviewViewModel> {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private PageParams pageParams;
    private PageResult<DataSourceWebscraping> pageResult;
    private String searchText = "";
    private String filterStatus = "";
    private List<DataSourceWebscraping> webscrapingList = new ArrayList<>();
    private int totalWebscrapings = 0;
    private int activeWebscrapings = 0;
    private int runningWebscrapings = 0;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder().maxRows(20).pageActual(1).rowActual(0).build();
        loadData();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            Criterias criterias = buildCriterias();
            pageResult = businessService.findAllEntity(DataSourceWebscraping.class, pageParams, criterias);
            
            if (pageResult != null && pageResult.getContent() != null) {
                webscrapingList = pageResult.getContent();
                totalWebscrapings = pageResult.getTotalRows();
                
                logActivity("BUSCAR", "DATASOURCEWEBSCRAPINGS", null, 
                    "Búsqueda: " + webscrapingList.size() + " resultados");
            } else {
                webscrapingList = new ArrayList<>();
                totalWebscrapings = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar web scrapings", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        activeWebscrapings = (int) webscrapingList.stream().filter(ws -> "ACTIVE".equals(ws.getWsstatus())).count();
        runningWebscrapings = (int) webscrapingList.stream().filter(ws -> "RUNNING".equals(ws.getWsstatus())).count();
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "wsname", searchText));
        }
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "wsstatus", filterStatus));
        }
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void refreshWebscrapings() {
        loadData();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void deleteWebscraping(@BindingParam("ws") DataSourceWebscraping ws) {
        Messagebox.show("¿Eliminar web scraping: " + ws.getWsname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(ws);
                        logActivity("ELIMINAR", "DATASOURCEWEBSCRAPINGS", ws.getIdxdatasourcewebscraping(), 
                            "Web scraping eliminado: " + ws.getWsname());
                        loadData();
                        loadMetrics();
                    } catch (Exception e) {
                        log.error("Error al eliminar", e);
                    }
                }
            });
    }
    
    public String getWsStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "ACTIVE": return "success";
            case "RUNNING": return "primary";
            case "ERROR": return "danger";
            default: return "secondary";
        }
    }
    
    public String formatDate(Timestamp ts) {
        return ts != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "-";
    }
    
    @Destroy
    public void destroy() {
        if (webscrapingList != null) { 
            webscrapingList.clear(); 
            webscrapingList = null; 
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
