package com.codeflowx.govern.workflow.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para ejecutar inferencias en el servidor CodeFlowX Inference
 * 
 * Integra con:
 * - Servidor de inferencia propio (FastAPI)
 * - Endpoints /chat/completions (OpenAI-compatible)
 * - Endpoints /api/inference/* (custom)
 * - Soporte para LLM, RAG, y modelos ML
 */
@Slf4j
@Service
public class ModelInferenceService {

    @Value("${codeflowx.inference.url:http://localhost:8000}")
    private String inferenceUrl;

    @Value("${codeflowx.inference.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ModelInferenceService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Ejecuta inferencia en batch para un modelo
     * Útil para evaluaciones de performance
     */
    public InferenceResult runBatchInference(Long modelId, Long versionId, String datasetId) {
        log.info("Ejecutando batch inference: modelId={}, versionId={}, datasetId={}", 
                 modelId, versionId, datasetId);

        String url = String.format("%s/api/inference/batch", inferenceUrl);

        BatchInferenceRequest request = BatchInferenceRequest.builder()
                .modelId(modelId)
                .versionId(versionId)
                .datasetId(datasetId)
                .batchSize(100)
                .build();

        HttpHeaders headers = createHeaders();
        HttpEntity<BatchInferenceRequest> httpEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<InferenceResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    InferenceResult.class
            );

            log.info("Batch inference completada: {} predictions", 
                     response.getBody().getPredictions().size());
            return response.getBody();

        } catch (Exception e) {
            log.error("Error ejecutando batch inference", e);
            throw new RuntimeException("Error en batch inference: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta inferencia LLM usando endpoint OpenAI-compatible
     * Para evaluaciones de prompts y LLMs
     */
    public LlmInferenceResult runLlmInference(String prompt, String modelName, Map<String, Object> parameters) {
        log.info("Ejecutando LLM inference: model={}, promptLength={}", 
                 modelName, prompt.length());

        String url = String.format("%s/chat/completions", inferenceUrl);

        // Construir request OpenAI-compatible
        Map<String, Object> request = new HashMap<>();
        request.put("model", modelName);
        request.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        
        // Parámetros opcionales
        if (parameters != null) {
            request.putAll(parameters); // temperature, max_tokens, etc.
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<LlmInferenceResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    LlmInferenceResult.class
            );

            log.info("LLM inference completada: {} tokens generados", 
                     response.getBody().getUsage().getCompletionTokens());
            return response.getBody();

        } catch (Exception e) {
            log.error("Error ejecutando LLM inference", e);
            throw new RuntimeException("Error en LLM inference: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta inferencia LLM en batch para evaluaciones
     * Útil para testing de múltiples prompts
     */
    public List<LlmInferenceResult> runLlmBatchInference(List<String> prompts, String modelName) {
        log.info("Ejecutando LLM batch inference: {} prompts, model={}", prompts.size(), modelName);

        return prompts.stream()
                .map(prompt -> runLlmInference(prompt, modelName, null))
                .toList();
    }

    /**
     * Ejecuta RAG retrieval + generation
     * Para evaluaciones de sistemas RAG
     */
    public RagInferenceResult runRagInference(String query, String ragSystemId, Integer topK) {
        log.info("Ejecutando RAG inference: ragSystemId={}, query={}, topK={}", 
                 ragSystemId, query, topK);

        String url = String.format("%s/api/rag/query", inferenceUrl);

        Map<String, Object> request = new HashMap<>();
        request.put("rag_system_id", ragSystemId);
        request.put("query", query);
        request.put("top_k", topK != null ? topK : 5);

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<RagInferenceResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    RagInferenceResult.class
            );

            log.info("RAG inference completada: {} documentos recuperados", 
                     response.getBody().getRetrievedDocuments().size());
            return response.getBody();

        } catch (Exception e) {
            log.error("Error ejecutando RAG inference", e);
            throw new RuntimeException("Error en RAG inference: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta agente IA
     * Para workflows de aprobación que requieren análisis IA
     */
    public AgentInferenceResult runAgentInference(Long agentId, Map<String, Object> input) {
        log.info("Ejecutando Agent inference: agentId={}", agentId);

        String url = String.format("%s/api/agents/%d/execute", inferenceUrl, agentId);

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(input, headers);

        try {
            ResponseEntity<AgentInferenceResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    AgentInferenceResult.class
            );

            log.info("Agent inference completada: status={}", response.getBody().getStatus());
            return response.getBody();

        } catch (Exception e) {
            log.error("Error ejecutando Agent inference", e);
            throw new RuntimeException("Error en Agent inference: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // API Key si está configurada
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.setBearerAuth(apiKey);
        }
        
        return headers;
    }

    // DTOs

    @Data
    @Builder
    public static class BatchInferenceRequest {
        private Long modelId;
        private Long versionId;
        private String datasetId;
        private Integer batchSize;
    }

    @Data
    public static class InferenceResult {
        private Long modelId;
        private Long versionId;
        private String datasetId;
        private List<Prediction> predictions;
        private Map<String, Double> aggregatedMetrics;
        private Long durationMs;
    }

    @Data
    public static class Prediction {
        private String inputId;
        private Object input;
        private Object prediction;
        private Object groundTruth;
        private Double confidence;
        private Map<String, Object> metadata;
    }

    @Data
    public static class LlmInferenceResult {
        private String id;
        private String model;
        private List<Choice> choices;
        private Usage usage;
        private Long created;
    }

    @Data
    public static class Choice {
        private Integer index;
        private Message message;
        private String finishReason;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    public static class Usage {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
    }

    @Data
    public static class RagInferenceResult {
        private String query;
        private String answer;
        private List<RetrievedDocument> retrievedDocuments;
        private Map<String, Double> metrics;
        private Long durationMs;
    }

    @Data
    public static class RetrievedDocument {
        private String documentId;
        private String content;
        private Double score;
        private Map<String, Object> metadata;
    }

    @Data
    public static class AgentInferenceResult {
        private Long agentId;
        private String status; // COMPLETED, FAILED, IN_PROGRESS
        private Map<String, Object> output;
        private List<String> executionLog;
        private Long durationMs;
    }
}

