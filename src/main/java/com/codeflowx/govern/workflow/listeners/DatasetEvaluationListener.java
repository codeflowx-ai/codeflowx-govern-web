package com.codeflowx.govern.workflow.listeners;

import com.codeflowx.govern.entity.governance.DatasetQuality;
import codeflowx.nocode.persist.BusinessService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * RabbitMQ Listener para Dataset Evaluation
 * 
 * Consume mensajes de evaluaciones asíncronas desde Python:
 * - dataset_evaluation_results: Resultados completos
 * - dataset_evaluation_progress: Progreso intermedio
 * - dataset_evaluation_errors: Errores en evaluación
 * 
 * Responsabilidades:
 * - Persistir resultados en PostgreSQL
 * - Actualizar estado de proceso BPMN
 * - Manejar errores y timeouts
 */
@Slf4j
@Component
public class DatasetEvaluationListener {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Consume resultados completos de evaluación
     * Queue: dataset_evaluation_results
     */
    @RabbitListener(queues = "dataset_evaluation_results")
    public void handleEvaluationResult(String message) {
        try {
            log.info("📨 Received evaluation result from RabbitMQ");
            
            JsonNode json = objectMapper.readTree(message);
            String type = json.get("type").asText();
            
            if ("completed".equals(type)) {
                String evaluationId = json.get("evaluation_id").asText();
                JsonNode result = json.get("result");
                
                log.info("✅ Processing completed evaluation: {}", evaluationId);
                
                // Extraer datos del resultado
                String datasetName = result.get("dataset_name").asText();
                Double overallScore = result.get("overall_quality_score").asDouble();
                String decision = result.get("decision").asText();
                Boolean requiresHumanReview = result.get("requires_human_review").asBoolean();
                String justification = result.get("justification").asText();
                Double executionTime = result.get("execution_time_seconds").asDouble();
                
                // Buscar registro existente por evaluationId
                DatasetQuality dq = findByEvaluationId(evaluationId);
                
                if (dq == null) {
                    // Crear nuevo registro si no existe
                    dq = new DatasetQuality();
                    dq.setDqlevaluationid(evaluationId);
                    dq.setDqldatasetname(datasetName);
                    
                    // Extraer más datos si están disponibles
                    if (result.has("project_id")) {
                        dq.setDqlprojectid(result.get("project_id").asText());
                    }
                    if (result.has("created_by")) {
                        dq.setDqlcreatedby(result.get("created_by").asText());
                    }
                    if (result.has("process_instance_id")) {
                        dq.setDqlprocessinstanceid(result.get("process_instance_id").asText());
                    }
                    
                    dq.setDqlcreatedat(LocalDateTime.now());
                }
                
                // Actualizar con resultados
                dq.setDqloverallscore(overallScore);
                dq.setDqldecision(decision);
                dq.setDqlrequireshumanreview(requiresHumanReview);
                dq.setDqljustification(justification);
                dq.setDqlexecutiontime(executionTime);
                dq.setDqlstatus("COMPLETED");
                dq.setDqlupdatedat(LocalDateTime.now());
                
                // Extraer métricas si están disponibles
                if (result.has("metrics")) {
                    JsonNode metrics = result.get("metrics");
                    if (metrics.has("completeness_score")) {
                        dq.setDqlcompletenesscore(metrics.get("completeness_score").asDouble());
                    }
                    if (metrics.has("validity_score")) {
                        dq.setDqlvalidityscore(metrics.get("validity_score").asDouble());
                    }
                    if (metrics.has("uniqueness_score")) {
                        dq.setDqluniquenesscore(metrics.get("uniqueness_score").asDouble());
                    }
                    if (metrics.has("total_rows")) {
                        dq.setDqltotalrows(metrics.get("total_rows").asLong());
                    }
                    if (metrics.has("total_columns")) {
                        dq.setDqltotalcolumns(metrics.get("total_columns").asInt());
                    }
                    if (metrics.has("missing_values_percentage")) {
                        dq.setDqlmissingpercentage(metrics.get("missing_values_percentage").asDouble());
                    }
                    
                    // Guardar métricas completas como JSON
                    dq.setDqlmetricsjson(metrics.toString());
                }
                
                // Guardar issues, recommendations, etc.
                if (result.has("issues_found")) {
                    dq.setDqlissuesfound(result.get("issues_found").toString());
                }
                if (result.has("recommendations")) {
                    dq.setDqlrecommendations(result.get("recommendations").toString());
                }
                if (result.has("remediations_available")) {
                    dq.setDqlremediationsavailable(result.get("remediations_available").toString());
                }
                
                // Persistir en PostgreSQL
                businessService.save(dq);
                
                log.info("💾 Async evaluation persisted: {} - Score: {} - Decision: {}", 
                         evaluationId, overallScore, decision);
                
                // Actualizar variables del proceso BPMN si está asociado
                String processInstanceId = dq.getDqlprocessinstanceid();
                if (processInstanceId != null && !processInstanceId.isEmpty()) {
                    try {
                        // Establecer variables en el proceso
                        runtimeService.setVariable(processInstanceId, "qualityScore", overallScore);
                        runtimeService.setVariable(processInstanceId, "decision", decision);
                        runtimeService.setVariable(processInstanceId, "requiresHumanReview", requiresHumanReview);
                        runtimeService.setVariable(processInstanceId, "evaluationStatus", "COMPLETED");
                        runtimeService.setVariable(processInstanceId, "datasetQualityId", dq.getIdxdatasetquality());
                        
                        // Si el proceso tiene un ReceiveTask esperando por un mensaje específico:
                        // runtimeService.messageEventReceived("dataset_evaluation_completed", executionId);
                        
                        log.info("🔄 BPMN process variables updated: {}", processInstanceId);
                        
                    } catch (Exception e) {
                        log.warn("⚠️ Could not update BPMN process variables: {}", e.getMessage());
                    }
                }
                
            } else {
                log.warn("⚠️ Unknown message type: {}", type);
            }
            
        } catch (Exception e) {
            log.error("❌ Error processing evaluation result from RabbitMQ", e);
            // No re-lanzar excepción para evitar requeue infinito
        }
    }

    /**
     * Consume actualizaciones de progreso
     * Queue: dataset_evaluation_progress
     */
    @RabbitListener(queues = "dataset_evaluation_progress")
    public void handleProgressUpdate(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            
            String evaluationId = json.get("evaluation_id").asText();
            int progress = json.get("progress").asInt();
            String status = json.get("status").asText();
            
            log.debug("📊 Progress update: {} - {}% - {}", evaluationId, progress, status);
            
            // Opcional: Actualizar estado en BD si se desea tracking detallado
            DatasetQuality dq = findByEvaluationId(evaluationId);
            if (dq != null) {
                dq.setDqlstatus("PROCESSING: " + status);
                dq.setDqlupdatedat(LocalDateTime.now());
                businessService.save(dq);
                
                // Actualizar variable BPMN si hay proceso asociado
                String processInstanceId = dq.getDqlprocessinstanceid();
                if (processInstanceId != null) {
                    try {
                        runtimeService.setVariable(processInstanceId, "evaluationProgress", progress);
                        runtimeService.setVariable(processInstanceId, "evaluationStatus", status);
                    } catch (Exception e) {
                        log.debug("Could not update BPMN progress: {}", e.getMessage());
                    }
                }
            }
            
        } catch (Exception e) {
            log.warn("⚠️ Error processing progress update: {}", e.getMessage());
        }
    }

    /**
     * Consume errores de evaluación
     * Queue: dataset_evaluation_errors
     */
    @RabbitListener(queues = "dataset_evaluation_errors")
    public void handleEvaluationError(String message) {
        try {
            log.error("❌ Received evaluation error from RabbitMQ");
            
            JsonNode json = objectMapper.readTree(message);
            
            String evaluationId = json.get("evaluation_id").asText();
            String errorType = json.has("error_type") ? json.get("error_type").asText() : "UNKNOWN";
            String errorMessage = json.has("error_message") ? json.get("error_message").asText() : "Unknown error";
            String errorDetails = json.has("error_details") ? json.get("error_details").asText() : null;
            
            log.error("💥 Evaluation error: {} - Type: {} - Message: {}", 
                     evaluationId, errorType, errorMessage);
            
            // Buscar registro existente
            DatasetQuality dq = findByEvaluationId(evaluationId);
            
            if (dq == null) {
                // Crear registro de error si no existe
                dq = new DatasetQuality();
                dq.setDqlevaluationid(evaluationId);
                
                if (json.has("dataset_name")) {
                    dq.setDqldatasetname(json.get("dataset_name").asText());
                }
                if (json.has("project_id")) {
                    dq.setDqlprojectid(json.get("project_id").asText());
                }
                if (json.has("created_by")) {
                    dq.setDqlcreatedby(json.get("created_by").asText());
                }
                if (json.has("process_instance_id")) {
                    dq.setDqlprocessinstanceid(json.get("process_instance_id").asText());
                }
                
                dq.setDqlcreatedat(LocalDateTime.now());
            }
            
            // Marcar como FAILED
            dq.setDqlstatus("FAILED");
            dq.setDqldecision("REJECTED");
            dq.setDqlrequireshumanreview(true);
            
            // Construir justificación con error
            String justification = String.format(
                "Evaluation failed: [%s] %s", 
                errorType, 
                errorMessage
            );
            if (errorDetails != null) {
                justification += "\nDetails: " + errorDetails;
            }
            dq.setDqljustification(justification);
            
            // Scores default en caso de error
            if (dq.getDqloverallscore() == null) {
                dq.setDqloverallscore(0.0);
            }
            
            dq.setDqlupdatedat(LocalDateTime.now());
            
            // Persistir error
            businessService.save(dq);
            
            log.info("💾 Evaluation error persisted: {}", evaluationId);
            
            // Notificar al proceso BPMN del error
            String processInstanceId = dq.getDqlprocessinstanceid();
            if (processInstanceId != null && !processInstanceId.isEmpty()) {
                try {
                    // Establecer variables de error
                    runtimeService.setVariable(processInstanceId, "evaluationStatus", "FAILED");
                    runtimeService.setVariable(processInstanceId, "evaluationError", errorMessage);
                    runtimeService.setVariable(processInstanceId, "decision", "REJECTED");
                    runtimeService.setVariable(processInstanceId, "requiresHumanReview", true);
                    runtimeService.setVariable(processInstanceId, "justification", justification);
                    
                    // Si el proceso tiene un Error Boundary Event esperando, se puede lanzar:
                    // runtimeService.createExecutionQuery()
                    //     .processInstanceId(processInstanceId)
                    //     .list()
                    //     .forEach(exec -> runtimeService.trigger(exec.getId()));
                    
                    log.info("🔄 BPMN process variables updated with error: {}", processInstanceId);
                    
                } catch (Exception e) {
                    log.warn("⚠️ Could not update BPMN process with error: {}", e.getMessage());
                }
            }
            
            // TODO: Opcional - Enviar notificación por email/Slack
            // notificationService.sendErrorNotification(evaluationId, errorMessage);
            
        } catch (Exception e) {
            log.error("❌ Error processing evaluation error message (meta-error!)", e);
        }
    }

    /**
     * Helper: Buscar DatasetQuality por evaluationId
     */
    private DatasetQuality findByEvaluationId(String evaluationId) {
        try {
            // Usar BusinessService con query personalizado o buscar por campo
            // Aquí asumo que existe un método findByField o similar
            // Si no existe, se puede usar directamente el repositorio JPA
            
            // Opción 1: Si BusinessService tiene método genérico
            // return businessService.findByField(DatasetQuality.class, "dqlevaluationid", evaluationId);
            
            // Opción 2: Query directa (temporal, hasta tener método en BusinessService)
            // Por ahora retornamos null y creamos nuevo registro
            // En producción, implementar búsqueda real en BusinessService
            
            return null; // TODO: Implementar búsqueda real
            
        } catch (Exception e) {
            log.warn("⚠️ Error searching for evaluation {}: {}", evaluationId, e.getMessage());
            return null;
        }
    }
}

