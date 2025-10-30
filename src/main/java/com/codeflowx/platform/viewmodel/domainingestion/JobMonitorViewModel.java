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

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class JobMonitorViewModel extends BaseFront<JobMonitorViewModel> {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private PageParams pageParams;
    private PageResult<IngestionJob> pageResult;
    
    private String searchText = "";
    private Domain filterDomain;
    private String filterType = "";
    private String filterStatus = "";
    
    private List<IngestionJob> jobsList = new ArrayList<>();
    private List<Domain> domainsList = new ArrayList<>();
    
    // Métricas
    private Long totalJobs = 0L;
    private Long runningJobs = 0L;
    private Long completedJobs = 0L;
    private Long failedJobs = 0L;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder()
            .maxRows(100)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadDomains();
        loadJobs();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void loadDomains() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinstatus", "active"));
            PageResult<Domain> result = businessService.findAllEntity(Domain.class, PageParams.builder().maxRows(1000).build(), criterias);
            if (result != null && result.getContent() != null) {
                domainsList = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar dominios", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadJobs() {
        try {
            Criterias criterias = buildCriterias();
            pageResult = businessService.findAllEntity(IngestionJob.class, pageParams, criterias);
            
            if (pageResult != null && pageResult.getContent() != null) {
                jobsList = pageResult.getContent();
                logActivity("BUSCAR", "INGESTION_JOBS", null, "Monitor: " + jobsList.size() + " jobs");
            } else {
                jobsList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error al cargar jobs", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "dinjobname", searchText));
        }
        if (filterType != null && !filterType.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinjobtype", filterType));
        }
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinstatus", filterStatus));
        }
        if (filterDomain != null) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "domain.idxdomain", filterDomain.getIdxdomain()));
        }
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        try {
            Criterias criterias = new Criterias();
            PageResult<IngestionJob> allJobs = businessService.findAllEntity(IngestionJob.class, 
                PageParams.builder().maxRows(10000).build(), criterias);
            
            if (allJobs != null && allJobs.getContent() != null) {
                totalJobs = (long) allJobs.getContent().size();
                runningJobs = allJobs.getContent().stream().filter(j -> "running".equals(j.getDinstatus())).count();
                completedJobs = allJobs.getContent().stream().filter(j -> "completed".equals(j.getDinstatus())).count();
                failedJobs = allJobs.getContent().stream().filter(j -> "failed".equals(j.getDinstatus())).count();
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void searchJobs() {
        pageParams.setPageActual(1);
        loadJobs();
    }
    
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadJobs();
    }
    
    @Command
    @NotifyChange("*")
    public void refreshJobs() {
        loadJobs();
        loadMetrics();
        Messagebox.show("Jobs actualizados", "Actualización", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    public void createNewJob() {
        log.info("Crear nuevo job");
    }
    
    @Command
    public void viewJobDetail(@BindingParam("job") IngestionJob job) {
        logActivity("VER", "INGESTION_JOBS", job.getIdxingestionjob(), "Detalle: " + job.getDinjobname());
        log.info("Ver detalle job: {}", job.getDinjobname());
    }
    
    @Command
    @NotifyChange("*")
    public void pauseJob(@BindingParam("job") IngestionJob job) {
        try {
            job.setDinstatus("paused");
            businessService.save(job);
            logActivity("PAUSAR", "INGESTION_JOBS", job.getIdxingestionjob(), "Job pausado: " + job.getDinjobname());
            loadJobs();
            Messagebox.show("Job pausado", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al pausar job", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void resumeJob(@BindingParam("job") IngestionJob job) {
        try {
            job.setDinstatus("running");
            businessService.save(job);
            logActivity("REANUDAR", "INGESTION_JOBS", job.getIdxingestionjob(), "Job reanudado: " + job.getDinjobname());
            loadJobs();
            Messagebox.show("Job reanudado", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al reanudar job", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void cancelJob(@BindingParam("job") IngestionJob job) {
        Messagebox.show("¿Cancelar job: " + job.getDinjobname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        job.setDinstatus("failed");
                        job.setDinerrormessage("Cancelado por el usuario");
                        businessService.save(job);
                        logActivity("CANCELAR", "INGESTION_JOBS", job.getIdxingestionjob(), 
                            "Job cancelado: " + job.getDinjobname());
                        loadJobs();
                        loadMetrics();
                        Messagebox.show("Job cancelado", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al cancelar job", e);
                        Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    public String translateStatus(String status) {
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
    
    public String getStatusBadgeClass(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "running": return "badge bg-warning";
            case "completed": return "badge bg-success";
            case "failed": return "badge bg-danger";
            case "paused": return "badge bg-info";
            case "pending": return "badge bg-secondary";
            case "queued": return "badge bg-primary";
            default: return "badge bg-secondary";
        }
    }
    
    public String getProgressBarClass(String status) {
        if (status == null) return "bg-secondary";
        switch (status) {
            case "running": return "bg-warning progress-bar-striped progress-bar-animated";
            case "completed": return "bg-success";
            case "failed": return "bg-danger";
            case "paused": return "bg-info";
            default: return "bg-secondary";
        }
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp);
    }
    
    @Destroy
    public void destroy() {
        if (jobsList != null) {
            jobsList.clear();
            jobsList = null;
        }
        if (domainsList != null) {
            domainsList.clear();
            domainsList = null;
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}

