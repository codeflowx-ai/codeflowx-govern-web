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
 * Servicio para evaluar riesgos de agentes IA usando LLM
 * Utiliza un LLM para analizar riesgos de forma inteligente
 */
@Slf4j
@Service
public class RiskAssessmentService {

    @Autowired
    private ModelInferenceService inferenceService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Evalúa los riesgos de un agente IA usando análisis LLM
     */
    public RiskAssessmentResult assessAgentRisk(Long agentId) {
        log.info("Iniciando risk assessment para agent: {}", agentId);

        try {
            // 1. Obtener información del agente (simulado - conectar a tu DB)
            Map<String, Object> agentInfo = getAgentInfo(agentId);

            // 2. Construir prompt para LLM
            String prompt = buildRiskAssessmentPrompt(agentInfo);

            // 3. Ejecutar análisis con LLM
            ModelInferenceService.LlmInferenceResult llmResult = inferenceService.runLlmInference(
                    prompt,
                    "gpt-4",
                    Map.of(
                            "temperature", 0.3, // Baja temperatura para análisis consistente
                            "max_tokens", 1000
                    )
            );

            // 4. Parsear resultado del LLM
            String llmResponse = llmResult.getChoices().get(0).getMessage().getContent();
            RiskAnalysis analysis = parseLlmResponse(llmResponse);

            // 5. Calcular score de riesgo
            int riskScore = calculateRiskScore(analysis);
            String riskLevel = determineRiskLevel(riskScore);

            // 6. Generar recomendaciones
            List<String> recommendations = generateRecommendations(analysis, riskLevel);

            RiskAssessmentResult result = RiskAssessmentResult.builder()
                    .agentId(agentId)
                    .riskScore(riskScore)
                    .riskLevel(riskLevel)
                    .analysis(analysis)
                    .recommendations(recommendations)
                    .llmResponse(llmResponse)
                    .timestamp(System.currentTimeMillis())
                    .build();

            log.info("Risk assessment completado: agentId={}, riskLevel={}, score={}", 
                     agentId, riskLevel, riskScore);

            return result;

        } catch (Exception e) {
            log.error("Error en risk assessment para agent: " + agentId, e);
            throw new RuntimeException("Error evaluando riesgos: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getAgentInfo(Long agentId) {
        // TODO: Conectar a tu repositorio AgentRepository
        // Por ahora retorna datos de ejemplo
        Map<String, Object> info = new HashMap<>();
        info.put("agentId", agentId);
        info.put("name", "Agent-" + agentId);
        info.put("type", "AUTONOMOUS");
        info.put("capabilities", Arrays.asList("data_access", "decision_making", "external_api_calls"));
        info.put("dataAccess", Arrays.asList("customer_data", "financial_data"));
        info.put("autonomyLevel", "HIGH");
        info.put("humanOversight", "LOW");
        info.put("impactArea", "FINANCIAL_OPERATIONS");
        return info;
    }

    private String buildRiskAssessmentPrompt(Map<String, Object> agentInfo) {
        return String.format("""
            Actúa como un experto en AI Risk Assessment y evalúa los riesgos del siguiente agente IA:
            
            INFORMACIÓN DEL AGENTE:
            - ID: %s
            - Nombre: %s
            - Tipo: %s
            - Capacidades: %s
            - Acceso a datos: %s
            - Nivel de autonomía: %s
            - Supervisión humana: %s
            - Área de impacto: %s
            
            EVALÚA LOS SIGUIENTES ASPECTOS:
            1. RIESGO TÉCNICO (1-10): Complejidad, fallos potenciales, dependencias
            2. RIESGO DE SEGURIDAD (1-10): Vulnerabilidades, acceso a datos sensibles
            3. RIESGO ÉTICO (1-10): Decisiones autónomas, impacto en personas
            4. RIESGO DE COMPLIANCE (1-10): Cumplimiento AI Act, GDPR, regulaciones
            5. RIESGO OPERACIONAL (1-10): Impacto de fallos, continuidad del negocio
            
            FORMATO DE RESPUESTA (JSON):
            {
              "technical_risk": {
                "score": <1-10>,
                "factors": ["factor1", "factor2"],
                "details": "explicación breve"
              },
              "security_risk": {
                "score": <1-10>,
                "factors": ["factor1", "factor2"],
                "details": "explicación breve"
              },
              "ethical_risk": {
                "score": <1-10>,
                "factors": ["factor1", "factor2"],
                "details": "explicación breve"
              },
              "compliance_risk": {
                "score": <1-10>,
                "factors": ["factor1", "factor2"],
                "details": "explicación breve"
              },
              "operational_risk": {
                "score": <1-10>,
                "factors": ["factor1", "factor2"],
                "details": "explicación breve"
              },
              "overall_assessment": "Resumen ejecutivo del riesgo general"
            }
            
            Responde SOLO con el JSON, sin texto adicional.
            """,
                agentInfo.get("agentId"),
                agentInfo.get("name"),
                agentInfo.get("type"),
                agentInfo.get("capabilities"),
                agentInfo.get("dataAccess"),
                agentInfo.get("autonomyLevel"),
                agentInfo.get("humanOversight"),
                agentInfo.get("impactArea")
        );
    }

    private RiskAnalysis parseLlmResponse(String llmResponse) {
        try {
            // Extraer JSON del response (a veces el LLM añade texto extra)
            String jsonStr = extractJson(llmResponse);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> data = objectMapper.readValue(jsonStr, Map.class);

            return RiskAnalysis.builder()
                    .technicalRisk(parseRiskCategory(data, "technical_risk"))
                    .securityRisk(parseRiskCategory(data, "security_risk"))
                    .ethicalRisk(parseRiskCategory(data, "ethical_risk"))
                    .complianceRisk(parseRiskCategory(data, "compliance_risk"))
                    .operationalRisk(parseRiskCategory(data, "operational_risk"))
                    .overallAssessment((String) data.get("overall_assessment"))
                    .build();

        } catch (Exception e) {
            log.error("Error parseando respuesta LLM", e);
            // Retornar análisis por defecto
            return createDefaultAnalysis();
        }
    }

    @SuppressWarnings("unchecked")
    private RiskCategory parseRiskCategory(Map<String, Object> data, String key) {
        Map<String, Object> category = (Map<String, Object>) data.get(key);
        return RiskCategory.builder()
                .score(((Number) category.get("score")).intValue())
                .factors((List<String>) category.get("factors"))
                .details((String) category.get("details"))
                .build();
    }

    private String extractJson(String text) {
        // Extraer JSON del texto (buscar entre {} o [])
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}') + 1;
        if (start >= 0 && end > start) {
            return text.substring(start, end);
        }
        return text;
    }

    private int calculateRiskScore(RiskAnalysis analysis) {
        // Score promedio ponderado
        int score = (int) Math.round(
                analysis.getTechnicalRisk().getScore() * 0.2 +
                analysis.getSecurityRisk().getScore() * 0.25 +
                analysis.getEthicalRisk().getScore() * 0.25 +
                analysis.getComplianceRisk().getScore() * 0.2 +
                analysis.getOperationalRisk().getScore() * 0.1
        );
        return Math.min(100, score * 10); // Escala 0-100
    }

    private String determineRiskLevel(int score) {
        if (score >= 70) return "HIGH";
        if (score >= 40) return "MEDIUM";
        return "LOW";
    }

    private List<String> generateRecommendations(RiskAnalysis analysis, String riskLevel) {
        List<String> recommendations = new ArrayList<>();

        if (analysis.getSecurityRisk().getScore() >= 7) {
            recommendations.add("Implementar controles de seguridad adicionales para acceso a datos sensibles");
            recommendations.add("Revisar políticas de cifrado y autenticación");
        }

        if (analysis.getEthicalRisk().getScore() >= 7) {
            recommendations.add("Incrementar supervisión humana en decisiones críticas");
            recommendations.add("Implementar mecanismos de explicabilidad de decisiones");
        }

        if (analysis.getComplianceRisk().getScore() >= 7) {
            recommendations.add("Realizar auditoría de compliance con AI Act y GDPR");
            recommendations.add("Documentar procesos de toma de decisiones del agente");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("El agente cumple con los estándares de riesgo aceptables");
        }

        return recommendations;
    }

    private RiskAnalysis createDefaultAnalysis() {
        RiskCategory defaultCategory = RiskCategory.builder()
                .score(5)
                .factors(Arrays.asList("Análisis no disponible"))
                .details("No se pudo completar el análisis automático")
                .build();

        return RiskAnalysis.builder()
                .technicalRisk(defaultCategory)
                .securityRisk(defaultCategory)
                .ethicalRisk(defaultCategory)
                .complianceRisk(defaultCategory)
                .operationalRisk(defaultCategory)
                .overallAssessment("Análisis manual requerido")
                .build();
    }

    // DTOs

    @Data
    @Builder
    public static class RiskAssessmentResult {
        private Long agentId;
        private Integer riskScore; // 0-100
        private String riskLevel; // LOW, MEDIUM, HIGH
        private RiskAnalysis analysis;
        private List<String> recommendations;
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
    public static class RiskAnalysis {
        private RiskCategory technicalRisk;
        private RiskCategory securityRisk;
        private RiskCategory ethicalRisk;
        private RiskCategory complianceRisk;
        private RiskCategory operationalRisk;
        private String overallAssessment;
    }

    @Data
    @Builder
    public static class RiskCategory {
        private Integer score; // 1-10
        private List<String> factors;
        private String details;
    }
}

