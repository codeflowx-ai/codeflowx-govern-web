package com.codeflowx.platform.viewmodel.domainingestion;

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
import com.codeflowx.govern.entity.domainingestion.Domain;
import com.codeflowx.govern.entity.domainingestion.IngestionJob;

import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para Dashboard de Domain Ingestion
 */
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class DomainIngestionDashboardViewModel extends BaseFront<DomainIngestionDashboardViewModel> {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    // Paginación
    private PageParams pageParams;
    private PageResult<Domain> pageResult;
    private PageResult<IngestionJob> jobsPageResult;
    
    // Filtros
    private String searchText = "";
    private String filterIndustry = "";
    private String filterStatus = "";
    
    // Datos
    private List<Domain> domainsList = new ArrayList<>();
    private List<IngestionJob> recentJobsList = new ArrayList<>();
    
    // Métricas
    private int activeDomains = 0;
    private int totalDocuments = 0;
    private int activeJobs = 0;
    private double successRate = 0.0;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder()
            .maxRows(12)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadDomains();
        loadRecentJobs();
        calculateMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void loadDomains() {
        try {
            Criterias criterias = buildCriterias();
            
            pageResult = businessService.findAllEntity(
                Domain.class,
                pageParams,
                criterias
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                domainsList = pageResult.getContent();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "DOMAINS", null, 
                    "Dashboard: " + domainsList.size() + " dominios");
                
                log.info("Cargados {} dominios", domainsList.size());
            } else {
                domainsList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error al cargar dominios", e);
            Messagebox.show("Error al cargar dominios: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadRecentJobs() {
        try {
            PageParams jobsParams = PageParams.builder()
                .maxRows(10)
                .pageActual(1)
                .build();
            
            // Ordenar por fecha de creación descendente
            Criterias criterias = new Criterias();
            
            jobsPageResult = businessService.findAllEntity(
                IngestionJob.class,
                jobsParams,
                criterias
            );
            
            if (jobsPageResult != null && jobsPageResult.getContent() != null) {
                recentJobsList = jobsPageResult.getContent();
                log.info("Cargados {} jobs recientes", recentJobsList.size());
            } else {
                recentJobsList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error al cargar jobs recientes", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void calculateMetrics() {
        try {
            // Dominios activos
            activeDomains = (int) domainsList.stream()
                .filter(d -> "active".equals(d.getDinstatus()))
                .count();
            
            // Total documentos
            totalDocuments = domainsList.stream()
                .mapToInt(d -> d.getDintotaldocuments() != null ? d.getDintotaldocuments() : 0)
                .sum();
            
            // Jobs activos
            activeJobs = (int) recentJobsList.stream()
                .filter(j -> "running".equals(j.getDinstatus()) || "pending".equals(j.getDinstatus()))
                .count();
            
            // Tasa de éxito
            long completedJobs = recentJobsList.stream()
                .filter(j -> "completed".equals(j.getDinstatus()))
                .count();
            
            long totalFinishedJobs = recentJobsList.stream()
                .filter(j -> "completed".equals(j.getDinstatus()) || "failed".equals(j.getDinstatus()))
                .count();
            
            if (totalFinishedJobs > 0) {
                successRate = Math.round((double) completedJobs / totalFinishedJobs * 1000) / 10.0;
            }
            
        } catch (Exception e) {
            log.error("Error al calcular métricas", e);
        }
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "dindomainname", searchText));
        }
        
        if (filterIndustry != null && !filterIndustry.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinindustry", filterIndustry));
        }
        
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinstatus", filterStatus));
        }
        
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void searchDomains() {
        pageParams.setPageActual(1);
        loadDomains();
        calculateMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadDomains();
        calculateMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void refreshDashboard() {
        loadDomains();
        loadRecentJobs();
        calculateMetrics();
    }
    
    @Command
    public void showCreateDomainWizard() {
        // TODO: Navegar a wizard de creación
        Messagebox.show("Navegando al wizard de creación de dominios", 
            "Crear Dominio", Messagebox.OK, Messagebox.INFORMATION);
        log.info("Navegación a wizard de creación de dominios");
    }
    
    @Command
    public void viewDomainDetails(@BindingParam("domain") Domain domain) {
        // Auditar acceso
        logActivity("VER", "DOMAINS", domain.getIdxdomain(), 
            "Vista de detalles: " + domain.getDindomainname());
        
        // TODO: Navegar a detalles
        log.info("Ver detalles del dominio: {}", domain.getDindomainname());
    }
    
    @Command
    public void editDomain(@BindingParam("domain") Domain domain) {
        logActivity("EDITAR", "DOMAINS", domain.getIdxdomain(), 
            "Edición iniciada: " + domain.getDindomainname());
        
        // TODO: Navegar a edición
        log.info("Editar dominio: {}", domain.getDindomainname());
    }
    
    @Command
    public void manageSources(@BindingParam("domain") Domain domain) {
        // TODO: Navegar a gestión de fuentes
        log.info("Gestionar fuentes del dominio: {}", domain.getDindomainname());
    }
    
    @Command
    @NotifyChange("*")
    public void deleteDomain(@BindingParam("domain") Domain domain) {
        Messagebox.show("¿Está seguro de eliminar el dominio: " + domain.getDindomainname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(domain);
                        
                        logActivity("ELIMINAR", "DOMAINS", domain.getIdxdomain(), 
                            "Dominio eliminado: " + domain.getDindomainname());
                        
                        loadDomains();
                        calculateMetrics();
                        
                        Messagebox.show("Dominio eliminado exitosamente", 
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar dominio", e);
                        Messagebox.show("Error al eliminar: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    @Command
    public void viewAllJobs() {
        // TODO: Navegar a monitor de jobs
        log.info("Ver todos los jobs");
    }
    
    // Helpers
    
    public String getDomainName(Domain domain) {
        return domain != null ? domain.getDindomainname() : "-";
    }
    
    public String translateStatus(String status) {
        if (status == null) return "N/A";
        switch (status) {
            case "active": return "Activo";
            case "inactive": return "Inactivo";
            case "error": return "Error";
            case "draft": return "Borrador";
            default: return status;
        }
    }
    
    public String getStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "active": return "badge bg-success";
            case "inactive": return "badge bg-secondary";
            case "error": return "badge bg-danger";
            case "draft": return "badge bg-warning";
            default: return "badge bg-secondary";
        }
    }
    
    public String translateJobType(String type) {
        if (type == null) return "N/A";
        switch (type) {
            case "document_upload": return "Documentos";
            case "web_scraping": return "Web Scraping";
            case "api": return "API";
            case "database": return "Base de Datos";
            default: return type;
        }
    }
    
    public String getJobTypeColor(String type) {
        if (type == null) return "badge bg-secondary";
        switch (type) {
            case "document_upload": return "badge bg-primary";
            case "web_scraping": return "badge bg-info";
            case "api": return "badge bg-success";
            case "database": return "badge bg-warning";
            default: return "badge bg-secondary";
        }
    }
    
    public String translateJobStatus(String status) {
        if (status == null) return "N/A";
        switch (status) {
            case "pending": return "Pendiente";
            case "running": return "En Ejecución";
            case "completed": return "Completado";
            case "failed": return "Fallido";
            case "paused": return "Pausado";
            case "queued": return "En Cola";
            default: return status;
        }
    }
    
    public String getJobStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "pending": return "badge bg-warning";
            case "running": return "badge bg-primary";
            case "completed": return "badge bg-success";
            case "failed": return "badge bg-danger";
            case "paused": return "badge bg-secondary";
            case "queued": return "badge bg-info";
            default: return "badge bg-secondary";
        }
    }
    
    public String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length) + "..." : text;
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
    
    @Destroy
    public void destroy() {
        if (domainsList != null) {
            domainsList.clear();
            domainsList = null;
        }
        if (recentJobsList != null) {
            recentJobsList.clear();
            recentJobsList = null;
        }
        pageResult = null;
        jobsPageResult = null;
        pageParams = null;
        businessService = null;
    }
}


