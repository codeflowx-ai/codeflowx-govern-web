package com.codeflowx.govern.viewmodel.governance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.governance.ComplianceRequirement;
import com.codeflowx.govern.service.governance.PolicyService;
import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.entity.governance.PolicyChecklistItem;
import com.codeflowx.govern.entity.procedures.governance.RunComplianceCheck;
import com.codeflowx.govern.service.governance.ComplianceRequirementService;
import com.codeflowx.govern.service.governance.PolicyChecklistItemService;
import com.codeflowx.govern.service.governance.ComplianceAssessmentService;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para la verificación de compliance con EU AI Act
 * Muestra checklist visual de requisitos del AI Act y permite ejecutar verificaciones automáticas
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ComplianceAiActViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private ComplianceAssessmentService complianceAssessmentService;
    @WireVariable
    private PolicyChecklistItemService policyChecklistItemService;
    @WireVariable
    private ComplianceRequirementService complianceRequirementService;
    
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;
    
    // ========== Datos ==========
    private List<ComplianceRequirement> aiActRequirements = new ArrayList<>();
    private List<ComplianceAssessment> recentAssessments = new ArrayList<>();
    private ComplianceAssessment currentAssessment;
    
    // ========== KPIs ==========
    private Long totalRequirements = 0L;
    private Long completedRequirements = 0L;
    private Long pendingRequirements = 0L;
    private Long nonCompliantRequirements = 0L;
    private Integer compliancePercentage = 0;
    private BigDecimal overallScore = BigDecimal.ZERO;
    
    // ========== Categorías del AI Act ==========
    private Map<String, Integer> requirementsByCategory = new HashMap<>();

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();
        
        log.info("Inicializando ComplianceAiActViewModel");
        
        loadAiActRequirements();
        loadRecentAssessments();
        calculateKPIs();
        calculateCategoryBreakdown();
    }
    
    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .ascending(true)
                .sortField("category")
                .build();
    }

    // ========== Carga de Datos ==========
    
    /**
     * Carga todos los requisitos del EU AI Act
     */
    private void loadAiActRequirements() {
        try {
            log.debug("Cargando requisitos del EU AI Act");
            
            // Filtrar solo requisitos del framework EU_AI_ACT
            String sql = "SELECT * FROM GOVCOMPLIANCEREQUIREMENTS WHERE COMPLIANCEFRAMEWORK = :framework LIMIT 100";
            Map<String, Object> params = new HashMap<>();
            params.put("framework", "EU_AI_ACT");
            
            List<ComplianceRequirement> result = businessService.findByParams(
                ComplianceRequirement.class,
                sql,
                params
            );
            
            if (result != null) {
                aiActRequirements = result;
                log.info("Cargados {} requisitos del EU AI Act", aiActRequirements.size());
            } else {
                aiActRequirements = new ArrayList<>();
                log.warn("No se encontraron requisitos del EU AI Act");
            }
        } catch (Exception e) {
            log.error("Error al cargar requisitos del AI Act", e);
            aiActRequirements = new ArrayList<>();
        }
    }
    
    /**
     * Carga assessments recientes del AI Act
     */
    private void loadRecentAssessments() {
        try {
            log.debug("Cargando assessments recientes del AI Act");
            
            String sql = "SELECT * FROM GOVCOMPLIANCEASSESSMENTS WHERE COMPLIANCEFRAMEWORK = :framework ORDER BY ASSESSMENTDATE DESC LIMIT 5";
            Map<String, Object> params = new HashMap<>();
            params.put("framework", "EU_AI_ACT");
            
            List<ComplianceAssessment> result = businessService.findByParams(
                ComplianceAssessment.class,
                sql,
                params
            );
            
            if (result != null) {
                recentAssessments = result;
                log.info("Cargados {} assessments recientes", recentAssessments.size());
                
                // Tomar el assessment más reciente como actual
                if (!recentAssessments.isEmpty()) {
                    currentAssessment = recentAssessments.get(0);
                }
            }
        } catch (Exception e) {
            log.error("Error al cargar assessments recientes", e);
        }
    }
    
    /**
     * Calcula KPIs del AI Act compliance
     */
    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs de AI Act compliance");
            
            totalRequirements = (long) aiActRequirements.size();
            
            // Contar por estado
            completedRequirements = aiActRequirements.stream()
                .filter(r -> "COMPLIANT".equals(r.getStatus()))
                .count();
            
            pendingRequirements = aiActRequirements.stream()
                .filter(r -> "PENDING".equals(r.getStatus()))
                .count();
            
            nonCompliantRequirements = aiActRequirements.stream()
                .filter(r -> "NON_COMPLIANT".equals(r.getStatus()))
                .count();
            
            // Calcular porcentaje de compliance
            if (totalRequirements > 0) {
                compliancePercentage = (int) ((completedRequirements * 100) / totalRequirements);
            }
            
            // Score global del assessment actual
            if (currentAssessment != null && currentAssessment.getOverallscore() != null) {
                overallScore = currentAssessment.getOverallscore();
            }
            
            log.info("KPIs AI Act - Total: {}, Compliant: {}, Non-Compliant: {}, %: {}",
                totalRequirements, completedRequirements, nonCompliantRequirements, compliancePercentage);
            
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }
    
    /**
     * Calcula breakdown por categoría
     */
    private void calculateCategoryBreakdown() {
        try {
            requirementsByCategory.clear();
            
            for (ComplianceRequirement req : aiActRequirements) {
                String category = req.getCategory() != null ? req.getCategory() : "OTHER";
                requirementsByCategory.put(category, requirementsByCategory.getOrDefault(category, 0) + 1);
            }
            
            log.debug("Breakdown por categoría: {} categorías", requirementsByCategory.size());
        } catch (Exception e) {
            log.error("Error al calcular breakdown por categoría", e);
        }
    }

    // ========== Comandos ==========
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        log.info("Refrescando datos de AI Act compliance");
        try {
            loadAiActRequirements();
            loadRecentAssessments();
            calculateKPIs();
            calculateCategoryBreakdown();
            
            Messagebox.show("Datos actualizados correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al refrescar datos", e);
            Messagebox.show("Error al refrescar datos: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Ejecuta un nuevo compliance check para EU AI Act
     */
    @Command
    @NotifyChange({"recentAssessments", "currentAssessment", "overallScore"})
    public void runAiActComplianceCheck() {
        log.info("Ejecutando compliance check para EU AI Act");
        try {
            RunComplianceCheck procedure = new RunComplianceCheck();
            procedure.setPAssessmentName("EU AI Act Compliance Check - " + new Timestamp(System.currentTimeMillis()));
            procedure.setPFramework("EU_AI_ACT");
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                loadRecentAssessments();
                calculateKPIs();
                
                Messagebox.show("Compliance check ejecutado correctamente\nAssessment ID: " + procedure.getOResult(),
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("El compliance check no se completó correctamente",
                    "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al ejecutar compliance check", e);
            Messagebox.show("Error al ejecutar check: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * @TODO: Integración con RAG Service para obtener requisitos específicos del EU AI Act
     * 
     * Endpoint: POST http://rag-service:8080/api/v1/query
     * Request: {
     *   "query": "EU AI Act requirements for high-risk AI systems",
     *   "top_k": 20,
     *   "filters": {"document_type": "regulation", "source": "EU_AI_ACT"}
     * }
     * Response: {
     *   "documents": [
     *     {"id": "...", "text": "...", "metadata": {...}},
     *     ...
     *   ],
     *   "scores": [0.95, 0.89, ...],
     *   "latency_ms": 123
     * }
     * 
     * Debe: Extraer los requisitos de los documentos recuperados y crear/actualizar
     * automáticamente los ComplianceRequirement en la base de datos
     */
    @Command
    @NotifyChange({"aiActRequirements", "totalRequirements"})
    public void loadRequirementsFromRAG() {
        log.warn("TODO: Implementar integración con RAG Service para cargar requisitos del EU AI Act");
        
        Messagebox.show(
            "Esta funcionalidad requiere integración con RAG Service\n\n" +
            "Endpoint: POST http://rag-service:8080/api/v1/query\n" +
            "Query: 'EU AI Act requirements for high-risk AI systems'\n\n" +
            "Los requisitos se cargarán automáticamente desde la documentación oficial del EU AI Act.",
            "Integración Pendiente",
            Messagebox.OK,
            Messagebox.INFORMATION
        );
    }

    // ========== Cleanup ==========
    
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            if (aiActRequirements != null) {
                aiActRequirements.clear();
                aiActRequirements = null;
            }
            if (recentAssessments != null) {
                recentAssessments.clear();
                recentAssessments = null;
            }
            if (requirementsByCategory != null) {
                requirementsByCategory.clear();
                requirementsByCategory = null;
            }
            
            currentAssessment = null;
            pageParams = null;
            policyService = null;
            complianceAssessmentService = null;
            policyChecklistItemService = null;
            complianceRequirementService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}


