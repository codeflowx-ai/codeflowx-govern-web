package com.codeflowx.platform.viewmodels.playground.documents;
import com.codeflowx.framework.zkoss.BaseFront;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

/**
 * ViewModel para Pantalla de Estado de Procesamiento
 * Muestra progreso en tiempo real de jobs de procesamiento de documentos
 * Endpoints: GET /api/v1/document/jobs/{job_id}/progress y /result
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class ProcessingStatusViewModel extends BaseFront<ProcessingStatusViewModel> {

    @WireVariable
    private RestTemplate restTemplate;

    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";

    @Getter
    private List<JobStatus> activeJobs = new ArrayList<>();

    @Getter
    private List<JobStatus> completedJobs = new ArrayList<>();

    @Getter
    @Setter
    private JobStatus selectedJob;

    @Getter
    @Setter
    private boolean loading = false;

    @Getter
    @Setter
    private String errorMessage = "";

    @Init
    public void init() {
        log.info("✅ Processing Status ViewModel initialized");
        loadJobs();
    }

    /**
     * Carga el estado de todos los jobs
     */
    @Command
    @NotifyChange({"activeJobs", "completedJobs", "loading", "errorMessage"})
    public void loadJobs() {
        loading = true;
        errorMessage = "";

        // TODO: Implementar endpoint que retorne lista de jobs
        // Por ahora simulamos con datos de prueba
        
        loading = false;
        log.info("✅ Jobs loaded");
    }

    /**
     * Obtiene el progreso de un job específico (polling)
     */
    @Command
    @NotifyChange("selectedJob")
    public void checkJobProgress(@BindingParam("jobId") String jobId) {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/jobs/" + jobId + "/progress";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                
                JobStatus job = findOrCreateJob(jobId);
                job.setStatus((String) body.get("status"));
                job.setProgressPercent(((Number) body.get("progress_percent")).doubleValue());
                job.setCurrentStep((String) body.get("current_step"));
                job.setEstimatedRemainingTime(((Number) body.get("estimated_remaining_time")).doubleValue());
                job.setDocumentsProcessed((Integer) body.get("documents_processed"));
                job.setTotalDocuments((Integer) body.get("total_documents"));

                log.info("✅ Job progress: {} - {}%", jobId, job.getProgressPercent());

                // Si está completado, mover a historial
                if ("completed".equals(job.getStatus()) || "failed".equals(job.getStatus())) {
                    activeJobs.remove(job);
                    if (!completedJobs.contains(job)) {
                        completedJobs.add(0, job);
                    }
                }
            }

        } catch (Exception e) {
            log.error("❌ Error checking job progress", e);
        }
    }

    /**
     * Obtiene el resultado de un job completado
     */
    @Command
    public void viewJobResult(@BindingParam("jobId") String jobId) {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/jobs/" + jobId + "/result";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> result = response.getBody();
                
                log.info("✅ Job result retrieved: {}", jobId);
                
                // TODO: Mostrar resultado en ventana modal o navegar a pantalla de detalles
                Messagebox.show("Job completed successfully!\nProcessed: " + result.get("documents_processed") + " documents", 
                                "Job Result", Messagebox.OK, Messagebox.INFORMATION);
            }

        } catch (Exception e) {
            log.error("❌ Error retrieving job result", e);
            Messagebox.show("Error al obtener resultado: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Encuentra o crea un job en la lista
     */
    private JobStatus findOrCreateJob(String jobId) {
        return activeJobs.stream()
                .filter(j -> jobId.equals(j.getJobId()))
                .findFirst()
                .orElseGet(() -> {
                    JobStatus newJob = new JobStatus();
                    newJob.setJobId(jobId);
                    activeJobs.add(newJob);
                    return newJob;
                });
    }

    /**
     * Clase para representar el estado de un job
     */
    @Getter
    @Setter
    public static class JobStatus {
        private String jobId;
        private String status; // processing, completed, failed
        private double progressPercent;
        private String currentStep;
        private double estimatedRemainingTime;
        private int documentsProcessed;
        private int totalDocuments;
        private Date startTime;
        private Date endTime;
    }
}

