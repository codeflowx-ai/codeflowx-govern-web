package com.codeflowx.govern.workflow.delegates;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

import com.codeflowx.govern.entity.evaluation.BiasAnalysis;
import com.codeflowx.govern.entity.evaluation.BiasDetection;
import com.codeflowx.govern.entity.evaluation.FairnessMetric;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Execute Bias Detection
 * 
 * Llama al microservicio Python (leka-server-serving-evaluation) 
 * para ejecutar análisis de sesgos en modelos ML.
 * 
 * Endpoint: POST /api/v1/evaluation/bias/detect
 * 
 * Proceso: 08_BIAS_DETECTION
 * 
 * JPAs: BiasAnalysis (principal) + FairnessMetric + BiasDetection
 */
@Slf4j
@Component("executeBiasDetectionDelegate")
public class ExecuteBiasDetectionDelegate implements JavaDelegate {

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
            log.info("🔍 Starting Bias Detection Analysis...");

            // 1. Obtener variables del proceso
            Long modelId = (Long) execution.getVariable("modelId");
            String modelName = (String) execution.getVariable("modelName");
            String modelPath = (String) execution.getVariable("modelPath");
            String datasetPath = (String) execution.getVariable("datasetPath");
            String sensitiveAttribute = (String) execution.getVariable("sensitiveAttribute");
            String createdBy = (String) execution.getVariable("initiator");
            String processInstanceId = execution.getProcessInstanceId();

            // 2. Construir request para Python
            Map<String, Object> request = new HashMap<>();
            request.put("model_id", modelId);
            request.put("model_name", modelName);
            request.put("model_path", modelPath);
            request.put("dataset_path", datasetPath);
            request.put("sensitive_attributes", new String[]{sensitiveAttribute != null ? sensitiveAttribute : "gender"});
            request.put("created_by", createdBy);
            request.put("process_instance_id", processInstanceId);

            // Umbrales para métricas de fairness
            Map<String, Double> thresholds = new HashMap<>();
            thresholds.put("max_disparate_impact", 0.20);
            thresholds.put("max_statistical_parity", 0.10);
            thresholds.put("max_equal_opportunity_diff", 0.15);
            request.put("fairness_thresholds", thresholds);

            // Opciones de análisis
            request.put("calculate_disparate_impact", true);
            request.put("calculate_statistical_parity", true);
            request.put("calculate_equal_opportunity", true);
            request.put("use_fairlearn", true);

            // 3. Llamar al endpoint Python
            String url = evaluationServiceUrl + "/api/v1/evaluation/bias/detect";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.info("📤 Calling Python service: {}", url);
            log.debug("Request payload: {}", objectMapper.writeValueAsString(request));

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                
                // 4. Crear BiasAnalysis (tabla principal)
                BiasAnalysis ba = new BiasAnalysis();
                ba.setModelid(modelId);
                ba.setOverallbiasscore(BigDecimal.valueOf(jsonResponse.path("overall_fairness_score").asDouble()));
                ba.setStatus("COMPLETED");
                ba.setCreatedby(createdBy);
                ba.setCreatedat(new Timestamp(System.currentTimeMillis()));
                ba.setUpdatedat(new Timestamp(System.currentTimeMillis()));
                ba.setAnalysistimestamp(new Timestamp(System.currentTimeMillis()));
                businessService.save(ba);
                log.info("💾 BiasAnalysis created. ID: {}", ba.getIdxbiasanalysis());
                
                // 5. Crear FairnessMetrics
                Double disparateImpact = jsonResponse.path("disparate_impact").asDouble();
                FairnessMetric fm1 = new FairnessMetric();
                fm1.setMetricname("DISPARATE_IMPACT");
                fm1.setMetricvalue(BigDecimal.valueOf(disparateImpact));
                fm1.setThresholdvalue(BigDecimal.valueOf(0.20));
                fm1.setStatus(disparateImpact > 0.20 ? "FAIL" : "PASS");
                fm1.setCalculatedat(new Timestamp(System.currentTimeMillis()));
                fm1.setAnalysis(ba);
                businessService.save(fm1);

                Double statisticalParity = jsonResponse.path("statistical_parity_difference").asDouble();
                FairnessMetric fm2 = new FairnessMetric();
                fm2.setMetricname("STATISTICAL_PARITY");
                fm2.setMetricvalue(BigDecimal.valueOf(statisticalParity));
                fm2.setThresholdvalue(BigDecimal.valueOf(0.10));
                fm2.setStatus(statisticalParity > 0.10 ? "FAIL" : "PASS");
                fm2.setCalculatedat(new Timestamp(System.currentTimeMillis()));
                fm2.setAnalysis(ba);
                businessService.save(fm2);

                log.info("💾 FairnessMetrics persisted (2 metrics)");

                // 6. Si hay sesgo detectado, crear BiasDetection
                boolean biasDetected = jsonResponse.path("bias_detected").asBoolean();
                if (biasDetected) {
                    BiasDetection bd = new BiasDetection();
                    bd.setBiastype("FAIRNESS_BIAS");
                    bd.setSeverity(disparateImpact > 0.30 ? "HIGH" : "MEDIUM");
                    bd.setDescription(jsonResponse.path("mitigation_recommendations").asText());
                    bd.setConfidence(BigDecimal.valueOf(0.95));
                    bd.setDetectionmethod("FAIRLEARN");
                    bd.setStatus("DETECTED");
                    bd.setDetectedat(new Timestamp(System.currentTimeMillis()));
                    bd.setAnalysis(ba);
                    businessService.save(bd);
                    log.info("⚠️ BiasDetection created (bias found)");
                }

                // 7. Guardar variables en proceso BPMN
                execution.setVariable("biasAnalysisId", ba.getIdxbiasanalysis());
                execution.setVariable("biasDetected", biasDetected);
                execution.setVariable("disparateImpact", disparateImpact);
                execution.setVariable("statisticalParity", statisticalParity);
                execution.setVariable("overallFairness", ba.getOverallbiasscore().doubleValue());
                execution.setVariable("mitigationRecommendations", jsonResponse.path("mitigation_recommendations").asText());

                log.info("✅ Bias detection completed. Bias detected: {}", biasDetected);
            } else {
                throw new RuntimeException("Python service returned error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("❌ Error in bias detection: {}", e.getMessage(), e);
            execution.setVariable("biasDetectionError", e.getMessage());
            execution.setVariable("biasDetected", true); // Por seguridad, asumimos sesgo si hay error
            throw new RuntimeException("Bias detection failed: " + e.getMessage(), e);
        }
    }
}
