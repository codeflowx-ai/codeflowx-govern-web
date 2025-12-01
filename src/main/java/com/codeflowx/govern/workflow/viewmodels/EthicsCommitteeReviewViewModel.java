package com.codeflowx.govern.workflow.viewmodels;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Ethics Committee Review
 * 
 * BPMN Process: ethics-review-v1
 * User Task: committeeReviewTask
 * Candidate Groups: ethics-committee
 * 
 * Funcionalidad:
 * - Presentar sistema de IA para revisión del comité de ética
 * - Mostrar análisis automático de impacto ético
 * - Evaluar riesgos éticos identificados (privacidad, sesgo, transparencia, etc.)
 * - Decidir: APPROVE (aprobar), REQUEST_CHANGES (solicitar cambios), REJECT (rechazar)
 * - Registrar dictamen del comité con justificación detallada
 * - Recomendar mitigaciones si requiere cambios
 * 
 * Input Variables (desde proceso BPMN):
 * - systemId: Long - ID del sistema a revisar
 * - systemName: String - Nombre del sistema
 * - systemType: String - Tipo (CONVERSATIONAL, PREDICTIVE, GENERATIVE, MEDICAL, etc.)
 * - impactLevel: String - Nivel de impacto (LOW, MEDIUM, HIGH, CRITICAL)
 * - estimatedUsers: String - Usuarios afectados estimados
 * - ethicalRisks: List<String> - Riesgos éticos identificados
 * - autoAnalysisResult: String (JSON) - Resultado del análisis automático
 * - privacyScore: Integer - Score de privacidad (0-100)
 * - transparencyScore: Integer - Score de transparencia (0-100)
 * 
 * Output Variables (al completar task):
 * - committee_decision: String - 'approve', 'request_changes', 'reject'
 * - ethical_assessment: String - Evaluación detallada del comité
 * - mitigation_recommendations: List<String> - Recomendaciones de mitigación
 * - follow_up_required: Boolean - Si requiere seguimiento
 * - committee_notes: String - Notas y observaciones del comité
 * - reviewed_by_committee: String - Miembros del comité que revisaron
 * - review_date: Timestamp - Fecha/hora de la revisión
 * 
 * Modo MOCK:
 * - URL: /workflow/ethics-committee-review.zul?taskId=mock-11&mock=true
 * - Datos simulados: Chatbot médico, impacto 10K pacientes/mes, riesgos privacidad
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsCommitteeReviewViewModel extends BaseFront<EthicsCommitteeReviewViewModel>{

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private BusinessService businessService;
    
    @Autowired
    protected IEntityLocal dao;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    @WireVariable("APPLICATION_DS")
    protected DataSource ds;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskService taskService;

    @WireVariable
    private RuntimeService runtimeService;

    // ========== Modo MOCK ==========
    @Getter
    private boolean mockMode = false;

    // ========== Datos ==========
    private String taskId;
    private String processInstanceId;
    private String systemName;
    private String systemType;
    private String impactLevel;
    private String estimatedUsers;
    private Integer privacyScore;
    private Integer transparencyScore;
    private List<String> ethicalRisks = new ArrayList<>();
    private List<String> mitigationRecommendations = new ArrayList<>();
    private String selectedDecision;
    private String committeeNotes;
    private String assessment;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Detectar modo MOCK
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
        
        
        log.info("🚀 Inicializando EthicsCommitteeReviewViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }
    
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Ethics Committee Review...");
        
        taskId = Executions.getCurrent().getParameter("taskId");
        processInstanceId = "mock-process-11";
        
        // Datos del sistema
        systemName = "Chatbot Atención Médica v1.0";
        systemType = "MEDICAL_AI";
        impactLevel = "CRITICAL";
        estimatedUsers = "10,000 pacientes/mes";
        
        // Scores
        privacyScore = 75;
        transparencyScore = 82;
        
        // Riesgos éticos identificados
        ethicalRisks.add("⚠️ Privacidad: Manejo de datos médicos sensibles (HIPAA)");
        ethicalRisks.add("⚠️ Sesgo: Posible sesgo en diagnósticos por datos de entrenamiento");
        ethicalRisks.add("⚠️ Transparencia: Decisiones médicas no explicables al paciente");
        ethicalRisks.add("⚠️ Responsabilidad: Unclear accountability en errores diagnósticos");
        ethicalRisks.add("⚠️ Autonomía: Pacientes podrían no saber que interactúan con IA");
        
        // Recomendaciones
        mitigationRecommendations.add("🔒 Implementar encriptación end-to-end de datos médicos");
        mitigationRecommendations.add("🔍 Auditoría externa por especialista en ética médica");
        mitigationRecommendations.add("📋 Consentimiento informado explícito para uso de IA");
        mitigationRecommendations.add("🎯 Sistema de explicabilidad (XAI) para decisiones");
        mitigationRecommendations.add("👨‍⚕️ Supervisión obligatoria de médico humano");
        
        log.info("✅ Datos MOCK cargados - Sistema médico CRÍTICO para revisión");
    }
    
    private void loadRealData() {
        log.info("💼 Cargando datos reales desde Flowable...");
        
        try {
            taskId = Executions.getCurrent().getParameter("taskId");
            if (taskId == null) {
                Messagebox.show("Error: Task ID no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                Messagebox.show("Error: Tarea no encontrada", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            processInstanceId = task.getProcessInstanceId();

            // Cargar variables
            Map<String, Object> vars = runtimeService.getVariables(processInstanceId);
            systemName = (String) vars.get("systemName");
            systemType = (String) vars.get("systemType");
            impactLevel = (String) vars.get("impactLevel");
            estimatedUsers = (String) vars.get("estimatedUsers");
            privacyScore = (Integer) vars.get("privacyScore");
            transparencyScore = (Integer) vars.get("transparencyScore");
            
            log.info("✅ Ethics Committee Review cargado: {}", systemName);

        } catch (Exception e) {
            log.error("❌ Error inicializando formulario", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Comandos ==========
    
    @Command
    @NotifyChange("selectedDecision")
    public void selectDecision(String decision) {
        this.selectedDecision = decision;
        log.info("📋 Decisión del comité: {}", decision);
    }

    @Command
    public void submitReview() {
        if (mockMode) {
            log.info("🎭 MOCK: Simulando revisión del comité de ética...");
            Messagebox.show(
                "✅ DEMO: Revisión del Comité de Ética completada\n\n" +
                "Sistema: " + systemName + "\n" +
                "Tipo: " + systemType + "\n" +
                "Impacto: " + impactLevel + "\n" +
                "Decisión: " + selectedDecision + "\n\n" +
                "Riesgos evaluados: " + ethicalRisks.size() + "\n" +
                "Recomendaciones: " + mitigationRecommendations.size() + "\n\n" +
                "(Modo MOCK - No se guardó en BD)", 
                "Demo - Revisión Ética Completada", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        try {
            if (selectedDecision == null || committeeNotes == null || committeeNotes.isEmpty()) {
                Messagebox.show("Por favor, selecciona una decisión y proporciona notas del comité", 
                               "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            // Guardar variables en proceso
            Map<String, Object> taskVars = new HashMap<>();
            taskVars.put("committee_decision", selectedDecision);
            taskVars.put("ethical_assessment", assessment);
            taskVars.put("mitigation_recommendations", mitigationRecommendations);
            taskVars.put("follow_up_required", "REQUEST_CHANGES".equals(selectedDecision));
            taskVars.put("committee_notes", committeeNotes);
            taskVars.put("reviewed_by_committee", getUser().getUsername());
            taskVars.put("review_date", new java.sql.Timestamp(System.currentTimeMillis()));

            taskService.complete(taskId, taskVars);
            
            logActivity("REVISION_ETICA", "ETHETHICSREVIEWS", Long.parseLong(taskId), 
                       "Revisión ética: " + systemName + " - Decisión: " + selectedDecision);

            log.info("✅ Ethics Committee Review completado - Decisión: {}", selectedDecision);

            Messagebox.show("Revisión del comité completada correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/console/govern/ethics-dashboard.zul"));

        } catch (Exception e) {
            log.error("❌ Error completando revisión", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancel() {
        String redirectUrl = mockMode ? "/workflow/task-inbox.zul?mock=true" : "/console/govern/ethics-dashboard.zul";
        Executions.sendRedirect(redirectUrl);
    }
    

    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        if (ethicalRisks != null) { ethicalRisks.clear(); ethicalRisks = null; }
        if (mitigationRecommendations != null) { mitigationRecommendations.clear(); mitigationRecommendations = null; }
        businessService = null;
        taskService = null;
        runtimeService = null;
    }
}
