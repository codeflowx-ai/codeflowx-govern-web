package com.codeflowx.govern.service.rag;

import com.codeflowx.governance.client.AIGovernanceClient;
import com.codeflowx.governance.client.exception.AIGovernanceException;
import com.codeflowx.governance.client.exception.ServiceUnavailableException;
import com.codeflowx.governance.client.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para evaluación RAG con integración al microservicio Python
 * 
 * Este servicio encapsula las llamadas al RAGEvaluationClient y proporciona
 * métodos de alto nivel para evaluaciones RAG con persistencia opcional.
 * 
 * Microservicio: RAG Evaluation (puerto 8004 via gateway 8000)
 * Cliente: AIGovernanceClient.ragEvaluation()
 */
@Slf4j
@Service
public class RAGEvaluationService {
    
    @Autowired
    private AIGovernanceClient aiGovernanceClient;
    
    /**
     * Evalúa el pipeline completo de RAG
     * 
     * @param request Request con query, documentos recuperados, respuesta generada, ground truth
     * @return Respuesta con scores y métricas completas
     */
    public RAGFullPipelineResponse evaluateFullPipeline(RAGFullPipelineRequest request) {
        log.info("Evaluando pipeline completo RAG - Query: {}", request.getQuery());
        
        try {
            RAGFullPipelineResponse response = aiGovernanceClient
                .ragEvaluation()
                .evaluateFullPipeline(request);
            
            log.info("Evaluación completa exitosa - Overall Score: {}, Risk: {}", 
                response.getOverallScore(), calculateRiskLevel(response));
            
            return response;
            
        } catch (ServiceUnavailableException e) {
            log.error("Microservicio RAG Evaluation no disponible", e);
            throw new RuntimeException("Servicio de evaluación RAG no disponible", e);
            
        } catch (AIGovernanceException e) {
            log.error("Error en evaluación RAG: {}", e.getMessage(), e);
            throw new RuntimeException("Error evaluando RAG: " + e.getMessage(), e);
            
        } catch (Exception e) {
            log.error("Error inesperado en evaluación RAG", e);
            throw new RuntimeException("Error inesperado en evaluación RAG", e);
        }
    }
    
    /**
     * Evalúa la calidad del retrieval
     */
    public RAGRetrievalResponse evaluateRetrieval(RAGRetrievalRequest request) {
        log.info("Evaluando retrieval RAG - Query: {}", request.getQuery());
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .evaluateRetrieval(request);
                
        } catch (Exception e) {
            log.error("Error evaluando retrieval RAG", e);
            throw new RuntimeException("Error evaluando retrieval: " + e.getMessage(), e);
        }
    }
    
    /**
     * Evalúa la calidad de una respuesta generada
     */
    public RAGAnswerResponse evaluateAnswer(RAGAnswerRequest request) {
        log.info("Evaluando respuesta RAG - Query: {}", request.getQuery());
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .evaluateAnswer(request);
                
        } catch (Exception e) {
            log.error("Error evaluando respuesta RAG", e);
            throw new RuntimeException("Error evaluando respuesta: " + e.getMessage(), e);
        }
    }
    
    /**
     * Valida políticas antes de generar respuesta (EU AI Act Art. 10)
     */
    public RAGPolicyValidationResponse validatePolicies(RAGPolicyValidationRequest request) {
        log.info("Validando políticas RAG - Query: {}", request.getQuery());
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .validatePolicies(request);
                
        } catch (Exception e) {
            log.error("Error validando políticas RAG", e);
            throw new RuntimeException("Error validando políticas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Ejecuta benchmarking del sistema RAG
     */
    public RAGBenchmarkResponse benchmarkSystem(RAGBenchmarkRequest request) {
        log.info("Ejecutando benchmark RAG - Dataset: {}", request.getBenchmarkDataset());
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .benchmarkSystem(request);
                
        } catch (Exception e) {
            log.error("Error ejecutando benchmark RAG", e);
            throw new RuntimeException("Error ejecutando benchmark: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene cola de validaciones humanas
     */
    public RAGValidationQueueResponse getValidationQueue(Integer limit, String priority) {
        log.info("Obteniendo cola de validaciones - Limit: {}, Priority: {}", limit, priority);
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .getValidationQueue(limit, priority);
                
        } catch (Exception e) {
            log.error("Error obteniendo cola de validaciones", e);
            throw new RuntimeException("Error obteniendo cola: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene detalle de validación por evaluation ID
     */
    public RAGHumanValidationResponse getValidationById(String evaluationId) {
        log.info("Obteniendo validación por ID: {}", evaluationId);
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .getValidationById(evaluationId);
                
        } catch (Exception e) {
            log.error("Error obteniendo validación", e);
            throw new RuntimeException("Error obteniendo validación: " + e.getMessage(), e);
        }
    }
    
    /**
     * Envía validación humana
     */
    public RAGHumanValidationResponse submitValidation(RAGHumanValidationRequest request) {
        log.info("Enviando validación humana - EvaluationId: {}", request.getEvaluationId());
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .submitValidation(request);
                
        } catch (Exception e) {
            log.error("Error enviando validación humana", e);
            throw new RuntimeException("Error enviando validación: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene estadísticas de validaciones
     */
    public RAGValidationStatisticsResponse getValidationStatistics() {
        log.info("Obteniendo estadísticas de validaciones");
        
        try {
            return aiGovernanceClient
                .ragEvaluation()
                .getValidationStatistics();
                
        } catch (Exception e) {
            log.error("Error obteniendo estadísticas", e);
            throw new RuntimeException("Error obteniendo estadísticas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Calcula nivel de riesgo basado en scores
     */
    private String calculateRiskLevel(RAGFullPipelineResponse response) {
        Double score = response.getOverallScore();
        Double faithfulness = response.getFaithfulness();
        
        if (score >= 80 && faithfulness >= 0.8) return "LOW";
        if (score >= 60 && faithfulness >= 0.6) return "MEDIUM";
        if (score >= 40) return "HIGH";
        return "CRITICAL";
    }
    
    /**
     * Convierte lista de maps a RetrievedDocument
     */
    public List<RetrievedDocument> convertToRetrievedDocuments(List<Map<String, Object>> docs) {
        return docs.stream()
            .map(doc -> RetrievedDocument.builder()
                .docId((String) doc.get("doc_id"))
                .content((String) doc.get("content"))
                .score(((Number) doc.getOrDefault("score", 0.0)).doubleValue())
                .metadata((Map<String, Object>) doc.getOrDefault("metadata", Map.of()))
                .build())
            .collect(Collectors.toList());
    }
}

