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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.service.rag.RAGEvaluationService;
import com.codeflowx.governance.client.model.RAGFullPipelineResponse;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.PageParams;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para Overview de Evaluaciones RAG
 * 
 * Pantalla: platform/evaluation/rag-evaluation/overview.zul
 * Propósito: Listar todas las evaluaciones RAG realizadas con filtros y búsqueda
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RAGEvaluationOverviewViewModel extends MasterPage {
    
    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private RAGEvaluationService ragEvaluationService;

    @WireVariable
    public Environment environment;

    private Component view;

    // ========== Datos del grid ==========
    private List<RAGEvaluationItem> evaluationsList = new ArrayList<>();
    private RAGEvaluationItem selectedEvaluation;

    // ========== Filtros de búsqueda ==========
    private String searchTerm;
    private String selectedRiskLevel;
    private String selectedGrade;
    private Double minScore;
    private Double maxScore;

    // ========== Paginación ==========
    private PageParams pageParams;

    // ========== Métricas globales ==========
    private Long totalEvaluations = 0L;
    private Long lowRiskEvaluations = 0L;
    private Long mediumRiskEvaluations = 0L;
    private Long highRiskEvaluations = 0L;
    private Long criticalRiskEvaluations = 0L;
    private Double averageScore = 0.0;

    // ========== Opciones de filtro ==========
    private List<Map<String, String>> riskLevelOptions = new ArrayList<>();
    private List<Map<String, String>> gradeOptions = new ArrayList<>();

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        log.info("Inicializando RAGEvaluationOverviewViewModel");
        Selectors.wireComponents(view, this, false);
        this.view = view;
        initializePageParams();
        initializeFilterOptions();
        loadMetrics();
        loadData();
    }

    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("createdAt")
                .build();
    }

    private void initializeFilterOptions() {
        // Opciones de nivel de riesgo
        riskLevelOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        riskLevelOptions.add(createOption("LOW", "Bajo"));
        riskLevelOptions.add(createOption("MEDIUM", "Medio"));
        riskLevelOptions.add(createOption("HIGH", "Alto"));
        riskLevelOptions.add(createOption("CRITICAL", "Crítico"));

        // Opciones de grado
        gradeOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        gradeOptions.add(createOption("A", "A"));
        gradeOptions.add(createOption("B", "B"));
        gradeOptions.add(createOption("C", "C"));
        gradeOptions.add(createOption("D", "D"));
        gradeOptions.add(createOption("F", "F"));
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }

    // ========== Carga de datos ==========

    @Command
    @NotifyChange({"evaluationsList", "totalEvaluations"})
    public void loadData() {
        try {
            log.info("Cargando evaluaciones RAG - Página: {}", pageParams.getPageActual());
            
            // TODO: Cargar desde base de datos cuando esté implementado
            // Por ahora lista vacía
            evaluationsList = new ArrayList<>();
            totalEvaluations = 0L;
            
            log.info("Cargadas {} evaluaciones RAG", evaluationsList.size());
                
        } catch (Exception e) {
            log.error("Error al cargar evaluaciones RAG", e);
            Messagebox.show(Labels.getLabel("common.error.load"), 
                Labels.getLabel("common.error.title"),
                Messagebox.OK, Messagebox.ERROR);
            evaluationsList = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange({"totalEvaluations", "lowRiskEvaluations", "mediumRiskEvaluations", 
                   "highRiskEvaluations", "criticalRiskEvaluations", "averageScore"})
    public void loadMetrics() {
        try {
            // TODO: Calcular métricas desde base de datos
            log.debug("Métricas calculadas desde la lista de evaluaciones");
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }

    // ========== Filtros y búsqueda ==========

    @Command
    @NotifyChange({"evaluationsList", "pageResult"})
    public void applyFilters() {
        log.info("Aplicando filtros - Risk Level: {}, Grade: {}, Score: {}-{}", 
            selectedRiskLevel, selectedGrade, minScore, maxScore);
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange({"searchTerm", "selectedRiskLevel", "selectedGrade", "minScore", "maxScore",
                   "evaluationsList", "pageResult"})
    public void clearFilters() {
        log.info("Limpiando filtros");
        searchTerm = null;
        selectedRiskLevel = null;
        selectedGrade = null;
        minScore = null;
        maxScore = null;
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange({"evaluationsList", "pageResult"})
    public void onSearchChange(@BindingParam("event") InputEvent event) {
        searchTerm = event.getValue();
        log.info("Búsqueda cambiada: {}", searchTerm);
        pageParams.setPageActual(1);
        loadData();
    }

    // ========== Paginación ==========

    @Command
    @NotifyChange({"evaluationsList", "pageResult"})
    public void onPaging(@BindingParam("event") Event event) {
        org.zkoss.zul.event.PagingEvent pe = (org.zkoss.zul.event.PagingEvent) event;
        int activePage = pe.getActivePage();
        pageParams.setPageActual(activePage + 1);
        log.info("Cambio de página a: {}", pageParams.getPageActual());
        loadData();
    }

    // ========== Navegación ==========

    @Command
    public void createEvaluation() {
        log.info("Navegando a creación de nueva evaluación RAG");
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "create");
        appendPage("plataforma/evaluacion/rag-evaluation/page.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewEvaluationDetails(@BindingParam("evaluationId") String evaluationId) {
        log.info("Navegando a detalle de evaluación RAG ID={}", evaluationId);
        Map<String, Object> params = new HashMap<>();
        params.put("evaluationId", evaluationId);
        params.put("mode", "edit");
        appendPage("plataforma/evaluacion/rag-evaluation/page.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void refreshData() {
        log.info("Refrescando datos de evaluaciones RAG");
        loadMetrics();
        loadData();
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
    
    // ========== Clase interna para items de evaluación ==========
    
    @Getter
    @Setter
    public static class RAGEvaluationItem {
        private String evaluationId;
        private String query;
        private Double overallScore;
        private String riskLevel;
        private Double faithfulness;
        private Double answerRelevancy;
        private Double contextPrecision;
        private String grade;
        private java.util.Date createdAt;
    }
}

