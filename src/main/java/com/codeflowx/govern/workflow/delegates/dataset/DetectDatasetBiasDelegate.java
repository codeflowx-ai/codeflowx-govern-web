package com.codeflowx.govern.workflow.delegates.dataset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Delegate: Detect Dataset Bias
 * 
 * Llama al microservicio Python para detectar sesgos (fairness) en el dataset.
 * 
 * Endpoint: POST /api/v1/evaluation/dataset/bias
 * 
 * Responsabilidades:
 * - Detectar bias en atributos protegidos (género, edad, raza, etc.)
 * - Calcular métricas de fairness (demographic parity, disparate impact, etc.)
 * - Guardar score de fairness en variables BPMN
 */
@Slf4j
@Component("detectDatasetBiasDelegate")
public class DetectDatasetBiasDelegate implements JavaDelegate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${leka.evaluation.url:http://localhost:8003}")
    private String evaluationServiceUrl;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🔍 Starting Dataset Bias Detection...");

            // 1. Obtener variables del proceso
            String datasetName = (String) execution.getVariable("datasetName");
            String datasetPath = (String) execution.getVariable("datasetPath");
            String projectId = (String) execution.getVariable("projectId");
            String createdBy = (String) execution.getVariable("initiator");

            // Atributos protegidos (pueden venir del proceso o ser default)
            @SuppressWarnings("unchecked")
            List<String> protectedAttributes = (List<String>) execution.getVariable("protectedAttributes");
            if (protectedAttributes == null || protectedAttributes.isEmpty()) {
                protectedAttributes = List.of("gender", "age", "race");
            }

            String targetColumn = (String) execution.getVariable("targetColumn");
            if (targetColumn == null) {
                log.warn("⚠️ No target column specified for bias detection, skipping...");
                execution.setVariable("biasScore", 100.0);
                execution.setVariable("biasSeverity", "NONE");
                return;
            }

            // 2. Construir request
            Map<String, Object> request = new HashMap<>();
            request.put("dataset_name", datasetName);
            request.put("dataset_path", datasetPath);
            request.put("protected_attributes", protectedAttributes);
            request.put("target_column", targetColumn);
            request.put("fairness_metrics", List.of("demographic_parity", "equal_opportunity", "disparate_impact"));
            request.put("project_id", projectId);
            request.put("created_by", createdBy);

            // 3. Llamar endpoint Python
            String url = evaluationServiceUrl + "/api/v1/evaluation/dataset/bias";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.info("📤 Calling Python service for bias detection: {}", url);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("⚠️ Python service returned error for bias detection: {}", response.getStatusCode());
                execution.setVariable("biasScore", 100.0);
                execution.setVariable("biasSeverity", "UNKNOWN");
                return;
            }

            // 4. Parsear response
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            
            String evaluationId = jsonResponse.get("evaluation_id").asText();
            Double fairnessScore = jsonResponse.get("fairness_score").asDouble();
            String biasSeverity = jsonResponse.get("bias_severity").asText();
            String biasDecision = jsonResponse.get("decision").asText();

            log.info("✅ Bias detection completed: {} - Fairness Score: {} - Severity: {}", 
                     evaluationId, fairnessScore, biasSeverity);

            // 5. Guardar en variables BPMN
            execution.setVariable("biasEvaluationId", evaluationId);
            execution.setVariable("biasScore", fairnessScore);
            execution.setVariable("biasSeverity", biasSeverity);
            execution.setVariable("biasDecision", biasDecision);

            // Si el bias es crítico, marcar para revisión humana
            if ("HIGH".equals(biasSeverity) || "CRITICAL".equals(biasSeverity)) {
                execution.setVariable("requiresHumanReview", true);
                log.warn("⚠️ High/Critical bias detected - Human review required");
            }

            log.info("✅ DetectDatasetBiasDelegate completed successfully");

        } catch (Exception e) {
            log.error("❌ Error in DetectDatasetBiasDelegate", e);
            // No fallar el proceso por bias detection
            execution.setVariable("biasScore", 50.0);
            execution.setVariable("biasSeverity", "UNKNOWN");
            log.warn("⚠️ Bias detection failed, continuing with default values");
        }
    }
}

