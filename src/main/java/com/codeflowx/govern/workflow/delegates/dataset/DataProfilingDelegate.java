package com.codeflowx.govern.workflow.delegates.dataset;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.codeflowx.govern.entity.governance.DatasetQuality;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Data Profiling
 * 
 * Llama al microservicio Python (leka-server-serving-evaluation) 
 * para evaluar la calidad del dataset.
 * 
 * Endpoint: POST /api/v1/evaluation/dataset/quality
 * 
 * Responsabilidades:
 * - Construir request para Python
 * - Llamar endpoint HTTP
 * - Persistir resultado en PostgreSQL (tabla DQLDATASETQUALITY)
 * - Guardar variables en proceso BPMN
 */
@Slf4j
@Component("dataProfilingDelegate")
public class DataProfilingDelegate implements JavaDelegate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${leka.evaluation.url:http://localhost:8003}")
    private String evaluationServiceUrl;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🔍 Starting Dataset Profiling...");

            // 1. Obtener variables del proceso
            String datasetName = (String) execution.getVariable("datasetName");
            String datasetPath = (String) execution.getVariable("datasetPath");
            String projectId = (String) execution.getVariable("projectId");
            String createdBy = (String) execution.getVariable("initiator");
            String processInstanceId = execution.getProcessInstanceId();

            // 2. Construir request para Python
            Map<String, Object> request = new HashMap<>();
            request.put("dataset_name", datasetName);
            request.put("dataset_path", datasetPath);
            request.put("project_id", projectId);
            request.put("created_by", createdBy);
            request.put("process_instance_id", processInstanceId);

            // Umbrales personalizados (si existen)
            Map<String, Double> thresholds = new HashMap<>();
            thresholds.put("min_completeness", 0.90);
            thresholds.put("min_validity", 0.85);
            thresholds.put("min_uniqueness", 0.80);
            thresholds.put("max_outliers_percentage", 0.10);
            thresholds.put("max_missing_percentage", 0.15);
            request.put("quality_thresholds", thresholds);

            // Opciones de análisis
            request.put("detect_outliers", true);
            request.put("validate_schema", true);
            request.put("analyze_distributions", true);

            // 3. Llamar al endpoint Python
            String url = evaluationServiceUrl + "/api/v1/evaluation/dataset/quality";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.info("📤 Calling Python service: {}", url);
            log.debug("Request payload: {}", objectMapper.writeValueAsString(request));

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Python service returned error: " + response.getStatusCode());
            }

            // 4. Parsear response
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            
            String evaluationId = jsonResponse.get("evaluation_id").asText();
            Double overallScore = jsonResponse.get("overall_quality_score").asDouble();
            String qualityRating = jsonResponse.get("quality_rating").asText();
            String decision = jsonResponse.get("decision").asText();
            Boolean requiresHumanReview = jsonResponse.get("requires_human_review").asBoolean();
            String justification = jsonResponse.get("justification").asText();
            Double executionTime = jsonResponse.get("execution_time_seconds").asDouble();

            log.info("✅ Evaluation completed: {} - Score: {} - Decision: {}", 
                     evaluationId, overallScore, decision);

            // 5. Extraer métricas detalladas
            JsonNode metricsNode = jsonResponse.get("metrics");
            Double completenessScore = metricsNode.get("completeness_score").asDouble();
            Double validityScore = metricsNode.get("validity_score").asDouble();
            Double uniquenessScore = metricsNode.get("uniqueness_score").asDouble();
            Double consistencyScore = metricsNode.has("consistency_score") ? 
                                     metricsNode.get("consistency_score").asDouble() : null;
            Long totalRows = metricsNode.get("total_rows").asLong();
            Integer totalColumns = metricsNode.get("total_columns").asInt();
            Double missingPercentage = metricsNode.get("missing_values_percentage").asDouble();
            Double outliersPercentage = metricsNode.has("outliers_percentage") ? 
                                        metricsNode.get("outliers_percentage").asDouble() : 0.0;

            // 6. Persistir en PostgreSQL (responsabilidad de Java)
            DatasetQuality dq = new DatasetQuality();
            dq.setDqlevaluationid(evaluationId);
            dq.setDqldatasetname(datasetName);
            dq.setDqldatasetpath(datasetPath);
            dq.setDqlsourcetype("CSV"); // TODO: detectar dinámicamente
            
            dq.setDqloverallscore(overallScore);
            dq.setDqlqualityrating(qualityRating);
            
            dq.setDqlcompletenesscore(completenessScore);
            dq.setDqlvalidityscore(validityScore);
            dq.setDqluniquenesscore(uniquenessScore);
            dq.setDqlconsistencyscore(consistencyScore);
            
            dq.setDqltotalrows(totalRows);
            dq.setDqltotalcolumns(totalColumns);
            dq.setDqlmissingpercentage(missingPercentage);
            dq.setDqloutlierspercentage(outliersPercentage);
            
            dq.setDqldecision(decision);
            dq.setDqlrequireshumanreview(requiresHumanReview);
            dq.setDqljustification(justification);
            
            // JSON completos
            dq.setDqlissuesfound(jsonResponse.has("issues_found") ? 
                                jsonResponse.get("issues_found").toString() : null);
            dq.setDqlrecommendations(jsonResponse.has("recommendations") ? 
                                    jsonResponse.get("recommendations").toString() : null);
            dq.setDqlremediationsavailable(jsonResponse.has("remediations_available") ? 
                                          jsonResponse.get("remediations_available").toString() : null);
            dq.setDqlmetricsjson(metricsNode.toString());
            
            dq.setDqlprojectid(projectId);
            dq.setDqlcreatedby(createdBy);
            dq.setDqlcreatedat(LocalDateTime.now());
            dq.setDqlstatus("COMPLETED");
            dq.setDqlexecutiontime(executionTime);
            dq.setDqlprocessinstanceid(processInstanceId);

            businessService.save(dq);
            
            log.info("💾 Dataset quality persisted: ID={}", dq.getIdxdatasetquality());

            // 7. Guardar variables en proceso BPMN
            execution.setVariable("evaluationId", evaluationId);
            execution.setVariable("qualityScore", overallScore);
            execution.setVariable("qualityRating", qualityRating);
            execution.setVariable("decision", decision);
            execution.setVariable("requiresHumanReview", requiresHumanReview);
            execution.setVariable("justification", justification);
            execution.setVariable("completenessScore", completenessScore);
            execution.setVariable("validityScore", validityScore);
            execution.setVariable("uniquenessScore", uniquenessScore);
            execution.setVariable("datasetQualityId", dq.getIdxdatasetquality());

            log.info("✅ DataProfilingDelegate completed successfully");

        } catch (Exception e) {
            log.error("❌ Error in DataProfilingDelegate", e);
            execution.setVariable("error", e.getMessage());
            execution.setVariable("decision", "REJECTED");
            throw new RuntimeException("Dataset profiling failed: " + e.getMessage(), e);
        }
    }
}

