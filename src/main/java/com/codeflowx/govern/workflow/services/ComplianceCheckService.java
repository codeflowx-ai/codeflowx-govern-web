package com.codeflowx.govern.workflow.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para verificar compliance de agentes/modelos usando RAG + LLM
 * Busca políticas aplicables y evalúa cumplimiento
 */
@Slf4j
@Service
public class ComplianceCheckService {

    @Autowired
    private ModelInferenceService inferenceService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Verifica compliance de un agente IA
     */
    public ComplianceCheckResult checkAgentCompliance(Long agentId) {
        log.info("Verificando compliance para agent: {}", agentId);

        try {
            // 1. Obtener información del agente
            Map<String, Object> agentInfo = getAgentInfo(agentId);

            // 2. Buscar políticas aplicables usando RAG
            ModelInferenceService.RagInferenceResult ragResult = inferenceService.runRagInference(
                    buildPolicyQuery(agentInfo),
                    "compliance-policies-rag", // ID del sistema RAG de políticas
                    10 // Top-K policies
            );

            // 3. Analizar compliance usando LLM
            String prompt = buildComplianceAnalysisPrompt(agentInfo, ragResult);
            ModelInferenceService.LlmInferenceResult llmResult = inferenceService.runLlmInference(
                    prompt,
                    "gpt-4",
                    Map.of(
                            "temperature", 0.2, // Baja para análisis consistente
                            "max_tokens", 1500
                    )
            );

            // 4. Parsear resultado
            String llmResponse = llmResult.getChoices().get(0).getMessage().getContent();
            ComplianceAnalysis analysis = parseLlmResponse(llmResponse);

            // 5. Determinar status de compliance
            String status = determineComplianceStatus(analysis);

            // 6. Generar lista de violaciones
            List<ComplianceViolation> violations = extractViolations(analysis);

            ComplianceCheckResult result = ComplianceCheckResult.builder()
                    .entityId(agentId)
                    .entityType("AGENT")
                    .status(status)
                    .analysis(analysis)
                    .violations(violations)
                    .policiesChecked(ragResult.getRetrievedDocuments().size())
                    .llmResponse(llmResponse)
                    .timestamp(System.currentTimeMillis())
                    .build();

            log.info("Compliance check completado: agentId={}, status={}, violations={}",
                     agentId, status, violations.size());

            return result;

        } catch (Exception e) {
            log.error("Error en compliance check para agent: " + agentId, e);
            throw new RuntimeException("Error verificando compliance: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica compliance de un modelo ML
     */
    public ComplianceCheckResult checkModelCompliance(Long modelId) {
        log.info("Verificando compliance para model: {}", modelId);

        try {
            Map<String, Object> modelInfo = getModelInfo(modelId);

            // Buscar políticas para modelos ML
            ModelInferenceService.RagInferenceResult ragResult = inferenceService.runRagInference(
                    "Políticas de compliance para modelos ML tipo: " + modelInfo.get("type"),
                    "compliance-policies-rag",
                    10
            );

            // Analizar con LLM
            String prompt = buildModelCompliancePrompt(modelInfo, ragResult);
            ModelInferenceService.LlmInferenceResult llmResult = inferenceService.runLlmInference(
                    prompt,
                    "gpt-4",
                    Map.of("temperature", 0.2, "max_tokens", 1500)
            );

            String llmResponse = llmResult.getChoices().get(0).getMessage().getContent();
            ComplianceAnalysis analysis = parseLlmResponse(llmResponse);
            String status = determineComplianceStatus(analysis);
            List<ComplianceViolation> violations = extractViolations(analysis);

            return ComplianceCheckResult.builder()
                    .entityId(modelId)
                    .entityType("MODEL")
                    .status(status)
                    .analysis(analysis)
                    .violations(violations)
                    .policiesChecked(ragResult.getRetrievedDocuments().size())
                    .llmResponse(llmResponse)
                    .timestamp(System.currentTimeMillis())
                    .build();

        } catch (Exception e) {
            log.error("Error en compliance check para model: " + modelId, e);
            throw new RuntimeException("Error verificando compliance: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getAgentInfo(Long agentId) {
        // TODO: Conectar a AgentRepository
        Map<String, Object> info = new HashMap<>();
        info.put("agentId", agentId);
        info.put("type", "AUTONOMOUS_AGENT");
        info.put("capabilities", Arrays.asList("data_access", "decision_making"));
        info.put("dataAccess", Arrays.asList("customer_pii", "financial_data"));
        info.put("autonomyLevel", "HIGH");
        return info;
    }

    private Map<String, Object> getModelInfo(Long modelId) {
        // TODO: Conectar a ModelRepository
        Map<String, Object> info = new HashMap<>();
        info.put("modelId", modelId);
        info.put("type", "CLASSIFICATION");
        info.put("purpose", "CREDIT_SCORING");
        info.put("dataTypes", Arrays.asList("financial", "demographic"));
        return info;
    }

    private String buildPolicyQuery(Map<String, Object> agentInfo) {
        return String.format(
                "Políticas de compliance para agente %s con acceso a %s y autonomía %s",
                agentInfo.get("type"),
                agentInfo.get("dataAccess"),
                agentInfo.get("autonomyLevel")
        );
    }

    private String buildComplianceAnalysisPrompt(Map<String, Object> agentInfo, 
                                                   ModelInferenceService.RagInferenceResult ragResult) {
        StringBuilder policies = new StringBuilder();
        for (ModelInferenceService.RetrievedDocument doc : ragResult.getRetrievedDocuments()) {
            policies.append("- ").append(doc.getContent()).append("\n");
        }

        return String.format("""
            Actúa como un experto en AI Compliance y evalúa si el siguiente agente cumple con las políticas:
            
            AGENTE:
            - Tipo: %s
            - Capacidades: %s
            - Acceso a datos: %s
            - Autonomía: %s
            
            POLÍTICAS APLICABLES:
            %s
            
            EVALÚA COMPLIANCE CON:
            1. EU AI Act (clasificación de riesgo, transparencia)
            2. GDPR (protección de datos personales)
            3. Políticas internas (de los documentos recuperados)
            
            FORMATO DE RESPUESTA (JSON):
            {
              "ai_act_compliance": {
                "status": "COMPLIANT/NON_COMPLIANT/NEEDS_REVIEW",
                "risk_classification": "MINIMAL/LIMITED/HIGH/UNACCEPTABLE",
                "violations": ["violación1", "violación2"],
                "details": "explicación"
              },
              "gdpr_compliance": {
                "status": "COMPLIANT/NON_COMPLIANT/NEEDS_REVIEW",
                "violations": ["violación1"],
                "details": "explicación"
              },
              "internal_policies_compliance": {
                "status": "COMPLIANT/NON_COMPLIANT/NEEDS_REVIEW",
                "violations": ["violación1"],
                "details": "explicación"
              },
              "overall_status": "COMPLIANT/NON_COMPLIANT/PARTIAL",
              "critical_violations": ["violación1", "violación2"],
              "recommendations": ["recomendación1", "recomendación2"]
            }
            
            Responde SOLO con el JSON.
            """,
                agentInfo.get("type"),
                agentInfo.get("capabilities"),
                agentInfo.get("dataAccess"),
                agentInfo.get("autonomyLevel"),
                policies.toString()
        );
    }

    private String buildModelCompliancePrompt(Map<String, Object> modelInfo,
                                               ModelInferenceService.RagInferenceResult ragResult) {
        // Similar al de agente pero para modelos
        return ""; // TODO: Implementar
    }

    @SuppressWarnings("unchecked")
    private ComplianceAnalysis parseLlmResponse(String llmResponse) {
        try {
            String jsonStr = extractJson(llmResponse);
            Map<String, Object> data = objectMapper.readValue(jsonStr, Map.class);

            return ComplianceAnalysis.builder()
                    .aiActCompliance(parseComplianceCategory(data, "ai_act_compliance"))
                    .gdprCompliance(parseComplianceCategory(data, "gdpr_compliance"))
                    .internalPoliciesCompliance(parseComplianceCategory(data, "internal_policies_compliance"))
                    .overallStatus((String) data.get("overall_status"))
                    .criticalViolations((List<String>) data.get("critical_violations"))
                    .recommendations((List<String>) data.get("recommendations"))
                    .build();

        } catch (Exception e) {
            log.error("Error parseando LLM response", e);
            return createDefaultAnalysis();
        }
    }

    @SuppressWarnings("unchecked")
    private ComplianceCategory parseComplianceCategory(Map<String, Object> data, String key) {
        Map<String, Object> category = (Map<String, Object>) data.get(key);
        return ComplianceCategory.builder()
                .status((String) category.get("status"))
                .violations((List<String>) category.getOrDefault("violations", new ArrayList<>()))
                .details((String) category.get("details"))
                .build();
    }

    private String extractJson(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}') + 1;
        if (start >= 0 && end > start) {
            return text.substring(start, end);
        }
        return text;
    }

    private String determineComplianceStatus(ComplianceAnalysis analysis) {
        if ("NON_COMPLIANT".equals(analysis.getOverallStatus())) {
            return "NON_COMPLIANT";
        }
        if (analysis.getCriticalViolations() != null && !analysis.getCriticalViolations().isEmpty()) {
            return "NON_COMPLIANT";
        }
        if ("PARTIAL".equals(analysis.getOverallStatus())) {
            return "PARTIAL_COMPLIANCE";
        }
        return "COMPLIANT";
    }

    private List<ComplianceViolation> extractViolations(ComplianceAnalysis analysis) {
        List<ComplianceViolation> violations = new ArrayList<>();

        if (analysis.getCriticalViolations() != null) {
            for (String violation : analysis.getCriticalViolations()) {
                violations.add(ComplianceViolation.builder()
                        .severity("CRITICAL")
                        .framework("MULTIPLE")
                        .description(violation)
                        .build());
            }
        }

        return violations;
    }

    private ComplianceAnalysis createDefaultAnalysis() {
        ComplianceCategory defaultCategory = ComplianceCategory.builder()
                .status("NEEDS_REVIEW")
                .violations(Arrays.asList("Análisis automático no disponible"))
                .details("Se requiere revisión manual")
                .build();

        return ComplianceAnalysis.builder()
                .aiActCompliance(defaultCategory)
                .gdprCompliance(defaultCategory)
                .internalPoliciesCompliance(defaultCategory)
                .overallStatus("NEEDS_REVIEW")
                .criticalViolations(new ArrayList<>())
                .recommendations(Arrays.asList("Realizar revisión manual de compliance"))
                .build();
    }

    // DTOs

    @Data
    @Builder
    public static class ComplianceCheckResult {
        private Long entityId;
        private String entityType; // AGENT, MODEL, PROMPT
        private String status; // COMPLIANT, NON_COMPLIANT, PARTIAL_COMPLIANCE, NEEDS_REVIEW
        private ComplianceAnalysis analysis;
        private List<ComplianceViolation> violations;
        private Integer policiesChecked;
        private String llmResponse;
        private Long timestamp;

        public String toJson() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return "{}";
            }
        }
    }

    @Data
    @Builder
    public static class ComplianceAnalysis {
        private ComplianceCategory aiActCompliance;
        private ComplianceCategory gdprCompliance;
        private ComplianceCategory internalPoliciesCompliance;
        private String overallStatus;
        private List<String> criticalViolations;
        private List<String> recommendations;
    }

    @Data
    @Builder
    public static class ComplianceCategory {
        private String status; // COMPLIANT, NON_COMPLIANT, NEEDS_REVIEW
        private List<String> violations;
        private String details;
    }

    @Data
    @Builder
    public static class ComplianceViolation {
        private String severity; // CRITICAL, HIGH, MEDIUM, LOW
        private String framework; // AI_ACT, GDPR, INTERNAL
        private String description;
    }
}

