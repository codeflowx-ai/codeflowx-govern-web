package com.codeflowx.govern.workflow.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para análisis de seguridad de prompts usando IA/LLM
 * Detecta jailbreaking, prompt injection, contenido malicioso y toxicidad
 */
@Slf4j
@Service
public class PromptSafetyService {

    @Value("${codeflowx.llm.base-url:http://localhost:8001}")
    private String llmBaseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Patrones conocidos de jailbreak
    private static final List<Pattern> JAILBREAK_PATTERNS = Arrays.asList(
        Pattern.compile("(?i)(ignore.*previous.*instructions?|forget.*instructions?)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(?i)(you are now|pretend to be|roleplay as)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(?i)(DAN|Developer Mode|God Mode|Jailbreak Mode)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(?i)(bypass.*restrictions?|override.*safety)", Pattern.CASE_INSENSITIVE)
    );

    // Patrones de prompt injection
    private static final List<Pattern> INJECTION_PATTERNS = Arrays.asList(
        Pattern.compile("(?i)(system:|assistant:|user:)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(?i)(</.*?>|<script|<iframe)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(?i)(\\[INST\\]|\\[/INST\\]|<\\|im_start\\|>|<\\|im_end\\|>)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(?i)(BEGIN PROMPT INJECTION|END PROMPT INJECTION)", Pattern.CASE_INSENSITIVE)
    );

    public PromptSafetyService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Analiza la seguridad completa de un prompt
     * 
     * @param promptContent Contenido del prompt a analizar
     * @return Map con resultados del análisis
     */
    public Map<String, Object> analyzeSafety(String promptContent) {
        log.info("Analizando seguridad del prompt (length: {})", promptContent.length());

        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. Detección de jailbreak (pattern matching + LLM)
            boolean jailbreakDetected = detectJailbreak(promptContent);
            result.put("jailbreakDetected", jailbreakDetected);
            
            // 2. Detección de prompt injection
            boolean injectionDetected = detectInjection(promptContent);
            result.put("injectionDetected", injectionDetected);
            
            // 3. Detección de contenido malicioso
            boolean maliciousContentDetected = detectMaliciousContent(promptContent);
            result.put("maliciousContentDetected", maliciousContentDetected);
            
            // 4. Análisis de toxicidad (usando LLM)
            Map<String, Object> toxicityAnalysis = analyzeToxicity(promptContent);
            result.put("toxicityAnalysis", toxicityAnalysis);
            
            // 5. Análisis de sensibilidad de datos
            boolean sensitiveDataDetected = detectSensitiveData(promptContent);
            result.put("sensitiveDataDetected", sensitiveDataDetected);
            
            // 6. Calcular score de seguridad global (0-100)
            int safetyScore = calculateSafetyScore(result);
            result.put("safetyScore", safetyScore);
            
            // 7. Resumen de riesgos
            List<String> risks = new ArrayList<>();
            if (jailbreakDetected) risks.add("Jailbreak attempt detected");
            if (injectionDetected) risks.add("Prompt injection detected");
            if (maliciousContentDetected) risks.add("Malicious content detected");
            if (sensitiveDataDetected) risks.add("Sensitive data detected");
            Integer toxicityScore = (Integer) toxicityAnalysis.get("toxicityScore");
            if (toxicityScore != null && toxicityScore > 50) {
                risks.add("High toxicity level (" + toxicityScore + "/100)");
            }
            result.put("risks", risks);
            
            // 8. Recomendación
            String recommendation = safetyScore >= 90 ? "SAFE_TO_USE" : 
                                   safetyScore >= 70 ? "REVIEW_REQUIRED" : 
                                   "NOT_RECOMMENDED";
            result.put("recommendation", recommendation);
            
            result.put("timestamp", System.currentTimeMillis());
            result.put("success", true);
            
            log.info("Análisis de seguridad completado - Score: {}, Recommendation: {}", 
                     safetyScore, recommendation);
            
        } catch (Exception e) {
            log.error("Error analizando seguridad del prompt", e);
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("safetyScore", 0);
            result.put("recommendation", "ERROR");
        }
        
        return result;
    }

    /**
     * Detecta intentos de jailbreak usando pattern matching y LLM
     */
    public boolean detectJailbreak(String promptContent) {
        // Pattern matching básico
        for (Pattern pattern : JAILBREAK_PATTERNS) {
            if (pattern.matcher(promptContent).find()) {
                log.warn("Jailbreak pattern detected: {}", pattern.pattern());
                return true;
            }
        }

        // Análisis con LLM (más sofisticado)
        try {
            Map<String, Object> llmRequest = new HashMap<>();
            llmRequest.put("task", "jailbreak_detection");
            llmRequest.put("prompt", promptContent);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(llmRequest, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                llmBaseUrl + "/prompt-safety/jailbreak",
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Boolean isJailbreak = (Boolean) response.getBody().get("isJailbreak");
                return Boolean.TRUE.equals(isJailbreak);
            }
        } catch (Exception e) {
            log.warn("Error en análisis LLM de jailbreak, usando solo pattern matching: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Detecta prompt injection
     */
    public boolean detectInjection(String promptContent) {
        for (Pattern pattern : INJECTION_PATTERNS) {
            if (pattern.matcher(promptContent).find()) {
                log.warn("Prompt injection pattern detected: {}", pattern.pattern());
                return true;
            }
        }
        return false;
    }

    /**
     * Detecta contenido malicioso usando LLM
     */
    public boolean detectMaliciousContent(String promptContent) {
        try {
            Map<String, Object> llmRequest = new HashMap<>();
            llmRequest.put("task", "malicious_content_detection");
            llmRequest.put("prompt", promptContent);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(llmRequest, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                llmBaseUrl + "/prompt-safety/malicious-content",
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Boolean isMalicious = (Boolean) response.getBody().get("isMalicious");
                return Boolean.TRUE.equals(isMalicious);
            }
        } catch (Exception e) {
            log.warn("Error en análisis de contenido malicioso: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Analiza toxicidad del prompt usando LLM
     */
    private Map<String, Object> analyzeToxicity(String promptContent) {
        Map<String, Object> toxicityResult = new HashMap<>();
        
        try {
            Map<String, Object> llmRequest = new HashMap<>();
            llmRequest.put("task", "toxicity_analysis");
            llmRequest.put("prompt", promptContent);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(llmRequest, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                llmBaseUrl + "/prompt-safety/toxicity",
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                toxicityResult = response.getBody();
            } else {
                toxicityResult.put("toxicityScore", 0);
                toxicityResult.put("categories", Collections.emptyList());
            }
        } catch (Exception e) {
            log.warn("Error en análisis de toxicidad: {}", e.getMessage());
            toxicityResult.put("toxicityScore", 0);
            toxicityResult.put("error", e.getMessage());
        }
        
        return toxicityResult;
    }

    /**
     * Detecta datos sensibles en el prompt (PII, credenciales, etc.)
     */
    private boolean detectSensitiveData(String promptContent) {
        // Patrones básicos de detección de datos sensibles
        List<Pattern> sensitivePatterns = Arrays.asList(
            Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b"), // SSN
            Pattern.compile("\\b\\d{16}\\b"), // Credit Card
            Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE), // Email
            Pattern.compile("(?i)(password|pwd|passwd)\\s*[:=]\\s*\\S+") // Password
        );

        for (Pattern pattern : sensitivePatterns) {
            if (pattern.matcher(promptContent).find()) {
                log.warn("Sensitive data pattern detected");
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula el score de seguridad global (0-100)
     */
    public int calculateSafetyScore(Map<String, Object> analysis) {
        int score = 100;

        // Penalizaciones
        if (Boolean.TRUE.equals(analysis.get("jailbreakDetected"))) {
            score -= 40;
        }
        if (Boolean.TRUE.equals(analysis.get("injectionDetected"))) {
            score -= 35;
        }
        if (Boolean.TRUE.equals(analysis.get("maliciousContentDetected"))) {
            score -= 30;
        }
        if (Boolean.TRUE.equals(analysis.get("sensitiveDataDetected"))) {
            score -= 20;
        }

        // Penalización por toxicidad
        Map<String, Object> toxicityAnalysis = (Map<String, Object>) analysis.get("toxicityAnalysis");
        if (toxicityAnalysis != null) {
            Integer toxicityScore = (Integer) toxicityAnalysis.get("toxicityScore");
            if (toxicityScore != null && toxicityScore > 0) {
                score -= (toxicityScore / 5); // Reducción proporcional a la toxicidad
            }
        }

        return Math.max(0, Math.min(100, score));
    }

    /**
     * Convierte el resultado del análisis a JSON string
     */
    public String toJsonString(Map<String, Object> analysis) {
        try {
            return objectMapper.writeValueAsString(analysis);
        } catch (JsonProcessingException e) {
            log.error("Error convirtiendo análisis a JSON", e);
            return "{}";
        }
    }
}


