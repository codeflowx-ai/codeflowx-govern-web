package com.codeflowx.govern.workflow.delegates;

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

import com.codeflowx.govern.entity.monitoring.DriftDetection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Analyze Drift
 * 
 * Llama al microservicio Python (leka-server-serving-evaluation) 
 * para detectar drift en modelos ML (data drift y concept drift).
 * 
 * Endpoint: POST /api/v1/evaluation/drift/detect
 * 
 * Proceso: 05_DRIFT_DETECTION
 * 
 * JPA: DriftDetection (tabla DRFDRIFTDETECTIONS)
 * Propiedades: drftype, drfscore, drfthreshold, drfdetected, drfcreatedat...
 */
@Slf4j
@Component("analyzeDriftDelegate")
public class AnalyzeDriftDelegate implements JavaDelegate {

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
            log.info("🔍 Starting Drift Analysis...");

            // 1. Obtener variables del proceso
            Long modelId = (Long) execution.getVariable("modelId");
            String modelName = (String) execution.getVariable("modelName");
            String baselineDataPath = (String) execution.getVariable("baselineDataPath");
            String currentDataPath = (String) execution.getVariable("currentDataPath");
            String createdBy = (String) execution.getVariable("initiator");
            String processInstanceId = execution.getProcessInstanceId();

            // 2. Construir request para Python
            Map<String, Object> request = new HashMap<>();
            request.put("model_id", modelId);
            request.put("model_name", modelName);
            request.put("baseline_data_path", baselineDataPath);
            request.put("current_data_path", currentDataPath);
            request.put("created_by", createdBy);
            request.put("process_instance_id", processInstanceId);

            // Umbrales de drift
            Map<String, Double> thresholds = new HashMap<>();
            thresholds.put("max_psi_threshold", 0.25);
            thresholds.put("max_kl_divergence", 0.30);
            thresholds.put("max_js_distance", 0.20);
            request.put("drift_thresholds", thresholds);

            // Opciones de análisis
            request.put("detect_data_drift", true);
            request.put("detect_concept_drift", true);
            request.put("detect_prediction_drift", true);
            request.put("calculate_feature_drift", true);

            // 3. Llamar al endpoint Python
            String url = evaluationServiceUrl + "/api/v1/evaluation/drift/detect";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.info("📤 Calling Python service: {}", url);
            log.debug("Request payload: {}", objectMapper.writeValueAsString(request));

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                
                // 4. Persistir resultados (usando propiedades CORRECTAS con prefijo DRF)
                DriftDetection dd = new DriftDetection();
                dd.setIdxmodel(modelId);  // ✅ CORRECTO (no ddmodelid)
                dd.setDrftype(jsonResponse.path("drift_type").asText());  // ✅ CORRECTO (no dddrifttype)
                dd.setDrfscore(jsonResponse.path("psi_score").asDouble());  // ✅ CORRECTO (no ddmetricvalue)
                dd.setDrfthreshold(0.25);  // ✅ CORRECTO (no ddthreshold)
                dd.setDrfdetected(jsonResponse.path("drift_detected").asBoolean());  // ✅ CORRECTO
                dd.setDrfrecommendation(jsonResponse.path("recommendations").asText());  // ✅ CORRECTO (no ddrecommendations)
                dd.setDrfprocessinstanceid(processInstanceId);
                dd.setDrfstatus("DETECTED");  // ✅ CORRECTO
                dd.setDrfcreatedat(new Timestamp(System.currentTimeMillis()));  // ✅ CORRECTO (no LocalDateTime)
                dd.setDrfcreatedby(createdBy);  // ✅ CORRECTO
                
                businessService.save(dd);
                log.info("💾 Drift detection results persisted. ID: {}", dd.getIdxdriftdetection());

                // 5. Guardar variables en proceso BPMN
                execution.setVariable("driftDetectionId", dd.getIdxdriftdetection());
                execution.setVariable("driftDetected", dd.getDrfdetected());
                execution.setVariable("driftScore", dd.getDrfscore());
                execution.setVariable("driftType", dd.getDrftype());
                execution.setVariable("driftRecommendations", dd.getDrfrecommendation());

                log.info("✅ Drift analysis completed. Drift detected: {}", dd.getDrfdetected());
            } else {
                throw new RuntimeException("Python service returned error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("❌ Error in drift analysis: {}", e.getMessage(), e);
            execution.setVariable("driftAnalysisError", e.getMessage());
            execution.setVariable("driftDetected", true); // Por seguridad, asumimos drift si hay error
            throw new RuntimeException("Drift analysis failed: " + e.getMessage(), e);
        }
    }
}
