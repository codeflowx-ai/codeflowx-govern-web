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
 * Delegate: Validate Compliance (PII Detection)
 * 
 * Llama al microservicio Python para detectar PII (Personal Identifiable Information).
 * 
 * Endpoint: POST /api/v1/evaluation/dataset/pii
 * 
 * Responsabilidades:
 * - Detectar PII con Presidio (emails, teléfonos, DNI, etc.)
 * - Validar compliance GDPR
 * - Marcar como crítico si se detecta PII sin protección
 */
@Slf4j
@Component("validateComplianceDelegate")
public class ValidateComplianceDelegate implements JavaDelegate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${leka.evaluation.url:http://localhost:8003}")
    private String evaluationServiceUrl;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🔍 Starting PII Detection (Compliance Validation)...");

            // 1. Obtener variables
            String datasetName = (String) execution.getVariable("datasetName");
            String datasetPath = (String) execution.getVariable("datasetPath");
            String projectId = (String) execution.getVariable("projectId");
            String createdBy = (String) execution.getVariable("initiator");

            // 2. Construir request
            Map<String, Object> request = new HashMap<>();
            request.put("dataset_name", datasetName);
            request.put("dataset_path", datasetPath);
            request.put("pii_entities", List.of(
                "PERSON", "EMAIL_ADDRESS", "PHONE_NUMBER", "LOCATION", 
                "CREDIT_CARD", "IBAN_CODE", "IP_ADDRESS", 
                "US_SSN", "UK_NHS", "ES_NIF"
            ));
            request.put("languages", List.of("en", "es"));
            request.put("confidence_threshold", 0.75);
            request.put("analyze_all_columns", true);
            request.put("project_id", projectId);
            request.put("created_by", createdBy);

            // 3. Llamar endpoint Python
            String url = evaluationServiceUrl + "/api/v1/evaluation/dataset/pii";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.info("📤 Calling Python service for PII detection: {}", url);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("⚠️ Python service returned error for PII detection: {}", response.getStatusCode());
                execution.setVariable("piiFound", false);
                execution.setVariable("gdprCompliant", true);
                return;
            }

            // 4. Parsear response
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            
            String evaluationId = jsonResponse.get("evaluation_id").asText();
            Boolean piiFound = jsonResponse.get("pii_found").asBoolean();
            String piiSeverity = jsonResponse.get("pii_severity").asText();
            Integer totalPii = jsonResponse.get("metrics").get("total_pii_detected").asInt();
            Boolean gdprCompliant = jsonResponse.get("gdpr_compliant").asBoolean();
            String piiDecision = jsonResponse.get("decision").asText();

            log.info("✅ PII detection completed: {} - PII Found: {} - Total: {} - Severity: {}", 
                     evaluationId, piiFound, totalPii, piiSeverity);

            // 5. Guardar en variables BPMN
            execution.setVariable("piiEvaluationId", evaluationId);
            execution.setVariable("piiFound", piiFound);
            execution.setVariable("piiSeverity", piiSeverity);
            execution.setVariable("totalPiiDetected", totalPii);
            execution.setVariable("gdprCompliant", gdprCompliant);
            execution.setVariable("piiDecision", piiDecision);

            // Si se detecta PII crítico, forzar revisión humana
            if (piiFound && ("HIGH".equals(piiSeverity) || "CRITICAL".equals(piiSeverity))) {
                execution.setVariable("requiresHumanReview", true);
                execution.setVariable("decision", "REVIEW_REQUIRED");
                log.warn("⚠️ Critical PII detected - Human review REQUIRED");
            }

            // Si no cumple GDPR, rechazar automáticamente
            if (!gdprCompliant) {
                execution.setVariable("decision", "REJECTED");
                execution.setVariable("justification", 
                    "Dataset does not comply with GDPR - PII detected without proper anonymization");
                log.error("❌ GDPR non-compliant dataset - Rejected");
            }

            log.info("✅ ValidateComplianceDelegate completed successfully");

        } catch (Exception e) {
            log.error("❌ Error in ValidateComplianceDelegate", e);
            // No fallar el proceso por PII detection
            execution.setVariable("piiFound", false);
            execution.setVariable("gdprCompliant", true);
            log.warn("⚠️ PII detection failed, continuing with default values");
        }
    }
}

