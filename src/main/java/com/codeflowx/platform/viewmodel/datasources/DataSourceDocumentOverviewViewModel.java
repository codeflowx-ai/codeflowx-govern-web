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
import com.codeflowx.govern.entity.datasources.DataSourceDocument;
import codeflowx.nocode.persist.Criterias;
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
public class DataSourceDocumentOverviewViewModel extends BaseFront<DataSourceDocumentOverviewViewModel> {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private PageParams pageParams;
    private PageResult<DataSourceDocument> pageResult;
    private String searchText = "";
    private String filterType = "";
    private String filterStatus = "";
    private List<DataSourceDocument> documentsList = new ArrayList<>();
    private int totalDocuments = 0;
    private int processedDocuments = 0;
    private int errorDocuments = 0;
    
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
            pageResult = businessService.findAllEntity(DataSourceDocument.class, pageParams, criterias);
            
            if (pageResult != null && pageResult.getContent() != null) {
                documentsList = pageResult.getContent();
                totalDocuments = pageResult.getTotalRows();
                
                logActivity("BUSCAR", "DATASOURCEDOCUMENTS", null, 
                    "Búsqueda: " + documentsList.size() + " resultados");
            } else {
                documentsList = new ArrayList<>();
                totalDocuments = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar documentos", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        processedDocuments = (int) documentsList.stream().filter(d -> "PROCESSED".equals(d.getDocstatus())).count();
        errorDocuments = (int) documentsList.stream().filter(d -> "ERROR".equals(d.getDocstatus())).count();
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria("docname", searchText, "LIKE");
        }
        if (filterType != null && !filterType.trim().isEmpty()) {
            criterias.addCriteria("doctype", filterType, "=");
        }
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria("docstatus", filterStatus, "=");
        }
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void refreshDocuments() {
        loadData();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void deleteDocument(@BindingParam("doc") DataSourceDocument doc) {
        Messagebox.show("¿Eliminar documento: " + doc.getDocname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(doc);
                        logActivity("ELIMINAR", "DATASOURCEDOCUMENTS", doc.getIdxdatasourcedocument(), 
                            "Documento eliminado: " + doc.getDocname());
                        loadData();
                        loadMetrics();
                    } catch (Exception e) {
                        log.error("Error al eliminar", e);
                    }
                }
            });
    }
    
    public String getDocStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "PROCESSED": return "success";
            case "PROCESSING": return "warning";
            case "ERROR": return "danger";
            default: return "info";
        }
    }
    
    public String formatDate(Timestamp ts) {
        return ts != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "-";
    }
    
    public String formatFileSize(Long size) {
        if (size == null) return "-";
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
    }
    
    @Destroy
    public void destroy() {
        if (documentsList != null) { 
            documentsList.clear(); 
            documentsList = null; 
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}