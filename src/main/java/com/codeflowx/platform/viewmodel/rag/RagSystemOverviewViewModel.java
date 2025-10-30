package com.codeflowx.platform.viewmodel.rag;

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
import com.codeflowx.govern.entity.rag.RagSystem;
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
public class RagSystemOverviewViewModel extends BaseFront<RagSystemOverviewViewModel> {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    // Paginación
    private PageParams pageParams;
    private PageResult<RagSystem> pageResult;
    
    // Filtros
    private String searchText = "";
    private String filterType = "";
    private String filterStatus = "";
    private String filterCompliance = "";
    
    // Datos
    private List<RagSystem> systemsList = new ArrayList<>();
    
    // Métricas
    private int totalSystems = 0;
    private int activeSystems = 0;
    private long totalDocuments = 0L;
    private int complianceRate = 100;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadSystems();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void loadSystems() {
        try {
            Criterias criterias = buildCriterias();
            
            pageResult = businessService.findAllEntity(
                RagSystem.class,
                pageParams,
                criterias
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                systemsList = pageResult.getContent();
                totalSystems = pageResult.getTotalRows();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "RAGSYSTEMS", null, 
                    "Búsqueda: " + systemsList.size() + " sistemas RAG");
                
                log.info("Cargados {} sistemas RAG de {} totales", systemsList.size(), totalSystems);
            } else {
                systemsList = new ArrayList<>();
                totalSystems = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar sistemas RAG", e);
            Messagebox.show("Error al cargar sistemas: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        try {
            activeSystems = (int) systemsList.stream()
                .filter(s -> "ACTIVE".equals(s.getRagstatus()))
                .count();
                
            totalDocuments = systemsList.stream()
                .mapToLong(s -> s.getRagdocuments() != null ? s.getRagdocuments() : 0)
                .sum();
                
            long compliantSystems = systemsList.stream()
                .filter(s -> "COMPLIANT".equals(s.getRagcompliance()))
                .count();
                
            if (!systemsList.isEmpty()) {
                complianceRate = (int) ((compliantSystems * 100) / systemsList.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "ragsystemname", searchText));
        }
        
        if (filterType != null && !filterType.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragtype", filterType));
        }
        
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragstatus", filterStatus));
        }
        
        if (filterCompliance != null && !filterCompliance.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragcompliance", filterCompliance));
        }
        
        if (!"ALL".equals(ragstatusFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragstatus");
            criteria.setValues(new Object[]{ragstatusFilter});
            criterias.addCriteria(criteria);
        }
        
        if (!"ALL".equals(ragsyncstatusFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragsyncstatus");
            criteria.setValues(new Object[]{ragsyncstatusFilter});
            criterias.addCriteria(criteria);
        }
        
        if (!"ALL".equals(ragtypeFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragtype");
            criteria.setValues(new Object[]{ragtypeFilter});
            criterias.addCriteria(criteria);
        }
        
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void searchSystems() {
        pageParams.setPageActual(1);
        loadSystems();
    }
    
    @Command
    @NotifyChange("*")
    public void clearFilters() {
        searchText = "";
        filterType = "";
        filterStatus = "";
        filterCompliance = "";
        pageParams.setPageActual(1);
        loadSystems();
    }
    
    @Command
    @NotifyChange("*")
    public void refreshSystems() {
        loadSystems();
        loadMetrics();
    }
    
    @Command
    public void createSystem() {
        log.info("Crear nuevo sistema RAG");
        // TODO: Navegar a pantalla de creación
    }
    
    @Command
    public void viewSystem(@BindingParam("system") RagSystem system) {
        log.info("Ver sistema RAG: {}", system.getRagsystemname());
    }
    
    @Command
    public void editSystem(@BindingParam("system") RagSystem system) {
        log.info("Editar sistema RAG: {}", system.getRagsystemname());
    }
    
    @Command
    @NotifyChange("*")
    public void deleteSystem(@BindingParam("system") RagSystem system) {
        Messagebox.show("¿Está seguro de eliminar el sistema RAG: " + system.getRagsystemname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(system);
                        
                        // Auditar eliminación
                        logActivity("ELIMINAR", "RAGSYSTEMS", system.getIdxragsystem(), 
                            "Sistema RAG eliminado: " + system.getRagsystemname());
                        
                        loadSystems();
                        loadMetrics();
                        Messagebox.show("Sistema eliminado exitosamente", 
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar sistema", e);
                        Messagebox.show("Error: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    public String getStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "ACTIVE": return "badge bg-success";
            case "INACTIVE": return "badge bg-secondary";
            case "DRAFT": return "badge bg-warning";
            case "ARCHIVED": return "badge bg-dark";
            default: return "badge bg-secondary";
        }
    }
    
    public String getComplianceColor(String compliance) {
        if (compliance == null) return "badge bg-secondary";
        switch (compliance) {
            case "COMPLIANT": return "badge bg-success";
            case "NON_COMPLIANT": return "badge bg-danger";
            case "PENDING_REVIEW": return "badge bg-warning";
            default: return "badge bg-secondary";
        }
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
    
    @Destroy
    public void destroy() {
        if (systemsList != null) { 
            systemsList.clear(); 
            systemsList = null; 
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
