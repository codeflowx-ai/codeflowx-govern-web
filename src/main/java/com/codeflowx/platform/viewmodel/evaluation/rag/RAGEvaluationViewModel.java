package com.codeflowx.platform.viewmodel.evaluation.rag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.enartframework.web.zk.page.MasterPage;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.service.rag.RAGEvaluationService;
import com.codeflowx.governance.client.model.*;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para Crear/Editar Evaluación RAG Completa
 * 
 * Pantalla: platform/evaluation/rag-evaluation/page.zul
 * Propósito: Formulario completo para evaluación del pipeline RAG
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RAGEvaluationViewModel extends MasterPage {
    
    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private RAGEvaluationService ragEvaluationService;

    @WireVariable
    public Environment environment;

    private Component view;

    // ========== Modo de operación ==========
    private String mode; // "create" o "edit"
    private String evaluationId;
    private boolean editing = false;
    private String pageTitle = "Evaluación RAG Completa";

    // ========== Datos de entrada ==========
    private String query = "";
    private String generatedAnswer = "";
    private String groundTruth = "";
    private List<RetrievedDocumentItem> retrievedDocuments = new ArrayList<>();

    // ========== Resultados de evaluación ==========
    private RAGFullPipelineResponse evaluationResult;
    private Double overallScore;
    private Double retrievalScore;
    private Double answerScore;
    private Double faithfulness;
    private Double answerRelevancy;
    private Double contextPrecision;
    private String grade;
    private String riskLevel;
    private List<String> bottlenecks = new ArrayList<>();
    private String recommendations = "";

    // ========== Estado ==========
    private boolean loading = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        // Obtener parámetros de navegación
        mode = (String) Executions.getCurrent().getParameter("mode");
        evaluationId = (String) Executions.getCurrent().getParameter("evaluationId");
        
        log.info("Inicializando RAGEvaluationViewModel - mode: {}, evaluationId: {}", mode, evaluationId);
        
        if ("create".equals(mode)) {
            initNewEvaluation();
        } else if ("edit".equals(mode) && evaluationId != null) {
            loadEvaluation(evaluationId);
        } else {
            log.error("Modo inválido o falta evaluationId");
            Executions.sendRedirect("/plataforma/evaluacion/rag-evaluation/overview.zul");
        }
    }

    private void initNewEvaluation() {
        log.debug("Inicializando nueva evaluación RAG");
        editing = false;
        pageTitle = "Nueva Evaluación RAG";
        
        // Inicializar lista vacía de documentos
        retrievedDocuments = new ArrayList<>();
    }

    private void loadEvaluation(String id) {
        try {
            log.debug("Cargando evaluación RAG ID={}", id);
            
            // TODO: Cargar desde base de datos cuando esté implementado
            editing = true;
            pageTitle = "Editar Evaluación RAG: " + id;
            
        } catch (Exception e) {
            log.error("Error al cargar evaluación RAG ID={}", id, e);
            Messagebox.show("Error al cargar evaluación: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/plataforma/evaluacion/rag-evaluation/overview.zul");
        }
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange({"evaluationResult", "overallScore", "retrievalScore", "answerScore",
                   "faithfulness", "answerRelevancy", "contextPrecision", "grade", 
                   "riskLevel", "bottlenecks", "recommendations", "loading"})
    public void evaluateFullPipeline() {
        if (query == null || query.trim().isEmpty()) {
            Messagebox.show("Por favor ingrese una consulta", "Validación", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        if (generatedAnswer == null || generatedAnswer.trim().isEmpty()) {
            Messagebox.show("Por favor ingrese una respuesta generada", "Validación", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        loading = true;
        
        try {
            log.info("Evaluando pipeline completo RAG - Query: {}", query);
            
            // Convertir documentos a formato esperado
            List<RetrievedDocument> docs = ragEvaluationService.convertToRetrievedDocuments(
                convertToMapList(retrievedDocuments)
            );
            
            // Construir request
            RAGFullPipelineRequest request = RAGFullPipelineRequest.builder()
                .query(query)
                .retrievedDocs(docs)
                .generatedAnswer(generatedAnswer)
                .groundTruth(groundTruth != null && !groundTruth.trim().isEmpty() ? groundTruth : null)
                .build();
            
            // Llamar servicio
            RAGFullPipelineResponse response = ragEvaluationService.evaluateFullPipeline(request);
            
            // Actualizar propiedades del ViewModel
            this.evaluationResult = response;
            this.overallScore = response.getOverallScore();
            this.retrievalScore = response.getRetrievalScore();
            this.answerScore = response.getAnswerScore();
            this.faithfulness = response.getFaithfulness();
            this.answerRelevancy = response.getAnswerRelevancy();
            this.contextPrecision = response.getContextPrecision();
            this.grade = response.getGrade();
            this.riskLevel = calculateRiskLevel(response);
            this.bottlenecks = response.getBottlenecks() != null ? response.getBottlenecks() : new ArrayList<>();
            this.recommendations = response.getRecommendations() != null ? response.getRecommendations() : "";
            
            // Mostrar mensaje de éxito
            Messagebox.show(
                String.format("Evaluación completada - Score: %.2f, Risk: %s", 
                             overallScore, riskLevel),
                "Éxito",
                Messagebox.OK,
                Messagebox.INFORMATION
            );
            
        } catch (Exception e) {
            log.error("Error evaluando pipeline RAG", e);
            Messagebox.show(
                "Error evaluando pipeline RAG: " + e.getMessage(),
                "Error",
                Messagebox.OK,
                Messagebox.ERROR
            );
        } finally {
            loading = false;
        }
    }

    @Command
    @NotifyChange({"loading"})
    public void validatePolicies() {
        if (query == null || query.trim().isEmpty()) {
            Messagebox.show("Por favor ingrese una consulta", "Validación", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        loading = true;
        
        try {
            log.info("Validando políticas RAG - Query: {}", query);
            
            // Convertir chunks a formato esperado
            List<Map<String, Object>> retrievedChunks = convertToMapList(retrievedDocuments);
            Map<String, Object> clientPolicies = getClientPolicies(); // Obtener desde sesión o BD
            
            RAGPolicyValidationRequest request = RAGPolicyValidationRequest.builder()
                .query(query)
                .retrievedChunks(retrievedChunks)
                .clientPolicies(clientPolicies)
                .validateBeforeGeneration(true)
                .build();
            
            RAGPolicyValidationResponse response = ragEvaluationService.validatePolicies(request);
            
            if (response.getValidationPassed()) {
                Messagebox.show(
                    String.format("Validación pasada - Alignment Score: %.2f", 
                                 response.getAlignmentScore()),
                    "Éxito",
                    Messagebox.OK,
                    Messagebox.INFORMATION
                );
            } else {
                Messagebox.show(
                    String.format("Validación falló - Alignment Score: %.2f. %s", 
                                 response.getAlignmentScore(), 
                                 response.getRecommendation() != null ? response.getRecommendation() : ""),
                    "Advertencia",
                    Messagebox.OK,
                    Messagebox.WARNING
                );
            }
            
        } catch (Exception e) {
            log.error("Error validando políticas", e);
            Messagebox.show(
                "Error validando políticas: " + e.getMessage(),
                "Error",
                Messagebox.OK,
                Messagebox.ERROR
            );
        } finally {
            loading = false;
        }
    }

    @Command
    @NotifyChange("retrievedDocuments")
    public void addRetrievedDocument() {
        RetrievedDocumentItem doc = new RetrievedDocumentItem();
        doc.setDocId("doc-" + (retrievedDocuments.size() + 1));
        doc.setContent("");
        doc.setScore(0.0);
        doc.setMetadata(new HashMap<>());
        retrievedDocuments.add(doc);
    }

    @Command
    @NotifyChange("retrievedDocuments")
    public void removeRetrievedDocument(@BindingParam("index") int index) {
        if (index >= 0 && index < retrievedDocuments.size()) {
            retrievedDocuments.remove(index);
        }
    }

    @Command
    @NotifyChange({"query", "generatedAnswer", "groundTruth", "retrievedDocuments", 
                   "evaluationResult", "overallScore", "riskLevel", "grade"})
    public void clearForm() {
        query = "";
        generatedAnswer = "";
        groundTruth = "";
        retrievedDocuments.clear();
        evaluationResult = null;
        overallScore = null;
        riskLevel = null;
        grade = null;
    }

    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición/creación de evaluación RAG, volviendo a overview");
        Executions.sendRedirect("/plataforma/evaluacion/rag-evaluation/overview.zul");
    }

    // ========== Métodos auxiliares ==========

    private List<Map<String, Object>> convertToMapList(List<RetrievedDocumentItem> items) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (RetrievedDocumentItem item : items) {
            Map<String, Object> doc = new HashMap<>();
            doc.put("doc_id", item.getDocId());
            doc.put("content", item.getContent());
            doc.put("score", item.getScore());
            doc.put("metadata", item.getMetadata() != null ? item.getMetadata() : Map.of());
            result.add(doc);
        }
        return result;
    }

    private String calculateRiskLevel(RAGFullPipelineResponse response) {
        Double score = response.getOverallScore();
        Double faithfulness = response.getFaithfulness();
        
        if (score >= 80 && faithfulness >= 0.8) return "LOW";
        if (score >= 60 && faithfulness >= 0.6) return "MEDIUM";
        if (score >= 40) return "HIGH";
        return "CRITICAL";
    }

    private Map<String, Object> getClientPolicies() {
        // TODO: Obtener políticas del cliente desde sesión o BD
        return Map.of(
            "language", "es",
            "topics", List.of("tecnología", "IA"),
            "tone", "profesional"
        );
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
    
    // ========== Clases internas ==========
    
    @Getter
    @Setter
    public static class RetrievedDocumentItem {
        private String docId;
        private String content;
        private Double score;
        private Map<String, Object> metadata;
    }
}

