package com.codeflowx.govern.workflow.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Servicio para integración con microservicio leka-server-governance
 * Ejecuta agentes IA para gobierno automatizado
 * Usa WebClient reactivo con Mono
 */
@Slf4j
@Service
public class AIAgentService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${governance.service.url:http://localhost:8002}")
    private String governanceServiceUrl;

    @Value("${governance.service.timeout:30000}")
    private int timeout;

    public AIAgentService(@Value("${governance.service.url:http://localhost:8002}") String governanceServiceUrl) {
        this.governanceServiceUrl = governanceServiceUrl;
        this.webClient = WebClient.builder()
            .baseUrl(governanceServiceUrl)
            .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Ejecuta evaluación de riesgos con IA
     * Usa WebClient reactivo con Mono
     * 
     * @param entityType Tipo de entidad (AGENT, MODEL, PROMPT, RAG)
     * @param entityId ID de la entidad
     * @param name Nombre
     * @param description Descripción
     * @param intendedUse Uso previsto
     * @param dataSources Fuentes de datos
     * @param userBase Base de usuarios
     * @return Map con risk_score, risk_category, key_concerns, etc.
     */
    public Map<String, Object> assessRisk(
        String entityType,
        Long entityId,
        String name,
        String description,
        String intendedUse,
        String dataSources,
        String userBase
    ) {
        try {
            log.info("🤖 Llamando a AI risk assessment para {} ID: {}", entityType, entityId);
            
            Map<String, Object> request = new HashMap<>();
            request.put("entity_type", entityType);
            request.put("entity_id", entityId);
            request.put("name", name);
            request.put("description", description);
            request.put("intended_use", intendedUse);
            request.put("data_sources", dataSources);
            request.put("user_base", userBase);
            
            // Llamada reactiva con Mono
            Mono<Map> responseMono = webClient.post()
                .uri("/api/v1/governance/risk-assessment")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(timeout));
            
            // Bloquear para obtener resultado (dentro del delegate)
            Map<String, Object> result = responseMono.block();
            
            if (result != null && result.containsKey("risk_score")) {
                log.info("✅ Risk assessment completado - Score: {}", result.get("risk_score"));
                return result;
            } else {
                throw new RuntimeException("Invalid response from governance service");
            }
            
        } catch (Exception e) {
            log.error("❌ Error calling risk assessment: {}", e.getMessage());
            return getFallbackRiskAssessment(entityType, entityId);
        }
    }

    /**
     * Ejecuta verificación de compliance con IA
     * Usa WebClient reactivo con Mono
     */
    public Map<String, Object> checkCompliance(
        String entityType,
        Long entityId,
        Map<String, Object> systemInfo
    ) {
        try {
            log.info("🤖 Llamando a AI compliance check para {} ID: {}", entityType, entityId);
            
            Map<String, Object> request = new HashMap<>();
            request.put("entity_type", entityType);
            request.put("entity_id", entityId);
            request.put("system_info", systemInfo);
            
            // Llamada reactiva con Mono
            Mono<Map> responseMono = webClient.post()
                .uri("/api/v1/governance/compliance-check")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(timeout));
            
            Map<String, Object> result = responseMono.block();
            
            if (result != null && result.containsKey("compliance_score")) {
                log.info("✅ Compliance check completado - Score: {}", result.get("compliance_score"));
                return result;
            } else {
                throw new RuntimeException("Invalid response from governance service");
            }
            
        } catch (Exception e) {
            log.error("❌ Error calling compliance check: {}", e.getMessage());
            return getFallbackComplianceCheck(entityType, entityId);
        }
    }

    /**
     * Ejecuta revisión ética con IA
     * Usa WebClient reactivo con Mono
     */
    public Map<String, Object> reviewEthics(
        String entityType,
        Long entityId,
        Map<String, Object> systemInfo
    ) {
        try {
            log.info("🤖 Llamando a AI ethical review para {} ID: {}", entityType, entityId);
            
            Map<String, Object> request = new HashMap<>();
            request.put("entity_type", entityType);
            request.put("entity_id", entityId);
            request.put("system_info", systemInfo);
            
            // Llamada reactiva con Mono
            Mono<Map> responseMono = webClient.post()
                .uri("/api/v1/governance/ethical-review")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(timeout));
            
            Map<String, Object> result = responseMono.block();
            
            if (result != null && result.containsKey("ethics_score")) {
                log.info("✅ Ethical review completado - Score: {}", result.get("ethics_score"));
                return result;
            } else {
                throw new RuntimeException("Invalid response from governance service");
            }
            
        } catch (Exception e) {
            log.error("❌ Error calling ethical review: {}", e.getMessage());
            return getFallbackEthicalReview(entityType, entityId);
        }
    }

    /**
     * Ejecuta análisis de seguridad de prompts con IA
     * Usa WebClient reactivo con Mono
     */
    public Map<String, Object> checkPromptSafety(String promptContent) {
        try {
            log.info("🤖 Llamando a AI prompt safety check");
            
            Map<String, Object> request = new HashMap<>();
            request.put("prompt_content", promptContent);
            
            // Llamada reactiva con Mono
            Mono<Map> responseMono = webClient.post()
                .uri("/api/v1/governance/prompt-safety")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(timeout));
            
            Map<String, Object> result = responseMono.block();
            
            if (result != null) {
                log.info("✅ Prompt safety completado - Score: {}", result.get("safety_score"));
                return result;
            } else {
                throw new RuntimeException("Invalid response from governance service");
            }
            
        } catch (Exception e) {
            log.error("❌ Error calling prompt safety: {}", e.getMessage());
            return getFallbackPromptSafety();
        }
    }

    /**
     * Detecta alucinaciones con IA
     * Usa WebClient reactivo con Mono
     */
    public Map<String, Object> detectHallucination(
        String question,
        String context,
        String generatedAnswer
    ) {
        try {
            log.info("🤖 Llamando a AI hallucination detection");
            
            Map<String, Object> request = new HashMap<>();
            request.put("question", question);
            request.put("context", context);
            request.put("generated_answer", generatedAnswer);
            
            // Llamada reactiva con Mono
            Mono<Map> responseMono = webClient.post()
                .uri("/api/v1/governance/hallucination-detection")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(timeout));
            
            Map<String, Object> result = responseMono.block();
            
            if (result != null) {
                log.info("✅ Hallucination detection completado");
                return result;
            }
            
            return new HashMap<>();
            
        } catch (Exception e) {
            log.error("❌ Error calling hallucination detection: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    // ========== FALLBACK METHODS ==========

    private Map<String, Object> getFallbackRiskAssessment(String entityType, Long entityId) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("success", false);
        fallback.put("risk_score", 75); // Conservador: asumir riesgo alto
        fallback.put("risk_category", "HIGH");
        fallback.put("ai_act_classification", "HIGH_RISK");
        fallback.put("key_concerns", Arrays.asList(
            "Unable to complete automated assessment",
            "Manual review required",
            "Governance service unavailable"
        ));
        fallback.put("mitigation_recommendations", Arrays.asList(
            "Conduct manual risk assessment",
            "Ensure governance service is operational",
            "Review system logs"
        ));
        fallback.put("justification", "Automated assessment failed. Conservative HIGH risk classification applied pending manual review.");
        fallback.put("confidence_score", 40);
        fallback.put("entity_id", entityId);
        return fallback;
    }

    private Map<String, Object> getFallbackComplianceCheck(String entityType, Long entityId) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("success", false);
        fallback.put("compliance_score", 60);
        fallback.put("compliant", false);
        fallback.put("violations", new ArrayList<>());
        fallback.put("recommendations", Arrays.asList(
            "Governance service unavailable",
            "Manual compliance review required"
        ));
        fallback.put("confidence_score", 40);
        return fallback;
    }

    private Map<String, Object> getFallbackEthicalReview(String entityType, Long entityId) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("success", false);
        fallback.put("ethics_score", 70);
        fallback.put("concerns", new ArrayList<>());
        fallback.put("recommendations", Arrays.asList(
            "Governance service unavailable",
            "Manual ethical review required"
        ));
        fallback.put("confidence_score", 40);
        return fallback;
    }

    private Map<String, Object> getFallbackPromptSafety() {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("success", false);
        fallback.put("safety_score", 50);
        fallback.put("jailbreak_detected", false);
        fallback.put("injection_detected", false);
        fallback.put("malicious_content_detected", false);
        fallback.put("toxicity_score", 0);
        fallback.put("vulnerabilities", Arrays.asList("Automated check unavailable"));
        fallback.put("confidence_score", 40);
        return fallback;
    }

    /**
     * Verifica si el servicio de governance está disponible
     * Usa WebClient reactivo con Mono
     */
    public boolean isServiceAvailable() {
        try {
            Mono<Map> responseMono = webClient.get()
                .uri("/api/v1/governance/health")
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(5000));
            
            Map<String, Object> result = responseMono.block();
            return result != null && "healthy".equals(result.get("status"));
            
        } catch (Exception e) {
            log.warn("⚠️ Governance service not available: {}", e.getMessage());
            return false;
        }
    }
}

