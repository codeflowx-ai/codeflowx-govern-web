package com.codeflowx.govern.viewmodel.governance;

import com.codeflowx.framework.zkoss.BaseFront;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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
 * ViewModel: Dashboard Consolidado de Supervisión Humana (HITL/HOTL)
 *
 * Funcionalidad:
 * - Consolidar todas las intervenciones humanas (agentes, modelos, prompts)
 * - Mostrar registro de supervisión, auditoría, intervenciones, estado
 * - Métricas HITL (tiempo promedio, tasa de aprobación, SLA cumplimiento)
 * - Integración con ViewModels de BPMN: AgentApprovalHumanOverrideViewModel,
 *   ModelApprovalHumanOverrideViewModel, PromptHumanReviewViewModel, HitlSlaReminderViewModel
 *
 * Modo MOCK:
 * - URL: /gobierno/governance/hitl-supervision.zul?mock=true
 * - Variable de entorno: MOCK_MODE=true
 * - Carga datos de ejemplo para demo
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class HitlSupervisionViewModel extends BaseFront<HitlSupervisionViewModel> {

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

    @WireVariable
    private TaskService taskService;

    @WireVariable
    private com.codeflowx.govern.service.workflow.TaskManagementService taskManagementService;

    @WireVariable
    private com.codeflowx.govern.service.monitoring.AuditLogService auditLogService;

    @WireVariable
    private com.codeflowx.govern.service.governance.PolicyAuditLogService policyAuditLogService;

    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Modo MOCK ==========
    @Getter
    private boolean mockMode = false;

    // ========== Datos de Supervisión ==========
    @Getter
    private List<HitlIntervention> interventions = new ArrayList<>();

    @Getter
    private List<HitlAuditLog> auditLogs = new ArrayList<>();

    @Getter @Setter
    private HitlIntervention selectedIntervention;

    // ========== Métricas HITL ==========
    @Getter
    private int totalInterventions = 0;

    @Getter
    private int pendingInterventions = 0;

    @Getter
    private int approvedInterventions = 0;

    @Getter
    private int rejectedInterventions = 0;

    @Getter
    private double averageResponseTime = 0.0; // en horas

    @Getter
    private double slaComplianceRate = 0.0; // porcentaje

    @Getter
    private int slaExceededCount = 0;

    // ========== Filtros ==========
    @Getter @Setter
    private String filterType = "ALL"; // ALL, AGENT, MODEL, PROMPT

    @Getter @Setter
    private String filterStatus = "ALL"; // ALL, PENDING, APPROVED, REJECTED

    @Getter @Setter
    private String filterDateRange = "LAST_30_DAYS"; // LAST_7_DAYS, LAST_30_DAYS, LAST_90_DAYS

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        // Detectar modo MOCK
        String mockParam = Executions.getCurrent().getParameter("mock");
        mockMode = "true".equalsIgnoreCase(mockParam);

        if (System.getenv("MOCK_MODE") != null) {
            mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE"));
        }

        log.info("🚀 Inicializando HitlSupervisionViewModel - MOCK MODE: {}", mockMode);

        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }

    /**
     * Cargar datos reales desde servicios
     */
    private void loadRealData() {
        log.info("📊 Cargando datos reales de supervisión humana...");

        try {
            interventions.clear();
            auditLogs.clear();

            // Cargar tareas HITL desde TaskService
            if (taskManagementService != null && taskService != null) {
                String currentUser = (String) Sessions.getCurrent().getAttribute("username");
                if (currentUser == null) {
                    currentUser = "admin";
                }

                // Obtener tareas de aprobación humana (agent-approval, model-approval, prompt-approval)
                List<com.codeflowx.govern.service.workflow.TaskDTO> hitlTasks = taskManagementService.getPendingTasksForUser(currentUser);

                // Filtrar solo tareas HITL
                hitlTasks = hitlTasks.stream()
                    .filter(t -> t.getProcessDefinitionId() != null &&
                        (t.getProcessDefinitionId().contains("agent-approval") ||
                         t.getProcessDefinitionId().contains("model-approval") ||
                         t.getProcessDefinitionId().contains("prompt-approval")))
                    .collect(Collectors.toList());

                // Convertir a HitlIntervention
                for (com.codeflowx.govern.service.workflow.TaskDTO task : hitlTasks) {
                    HitlIntervention interv = new HitlIntervention();
                    interv.setId(task.getTaskId());
                    interv.setType(extractTypeFromProcess(task.getProcessDefinitionId()));
                    interv.setEntityName(task.getTaskName());
                    interv.setStatus("PENDING");
                    interv.setRequestedAt(new Timestamp(task.getCreatedAt().getTime()));
                    interventions.add(interv);
                }
            }

            // Cargar logs de auditoría relacionados con HITL
            if (auditLogService != null) {
                try {
                    // Consultar logs de auditoría relacionados con aprobaciones
                    codeflowx.nocode.persist.PageParams pageParams = codeflowx.nocode.persist.PageParams.builder()
                        .maxRows(50)
                        .pageActual(1)
                        .rowActual(0)
                        .ascending(false)
                        .sortField("createdat")
                        .build();
                    
                    codeflowx.nocode.persist.Criterias criterias = new codeflowx.nocode.persist.Criterias();
                    // Filtrar por acciones relacionadas con HITL
                    codeflowx.nocode.persist.Criteria criteria = new codeflowx.nocode.persist.Criteria(
                        codeflowx.nocode.persist.Operation.OR,
                        codeflowx.nocode.persist.Evaluation.LIKE,
                        "action"
                    );
                    criteria.setValues(new Object[]{"%APPROVE%", "%REJECT%", "%HITL%", "%HUMAN%"});
                    criterias.addCriteria(criteria);
                    
                    codeflowx.nocode.persist.PageResult<com.codeflowx.govern.entity.monitoring.AuditLog> auditLogResult = 
                        auditLogService.findAll(pageParams, criterias);
                    
                    if (auditLogResult != null && auditLogResult.getContent() != null) {
                        for (com.codeflowx.govern.entity.monitoring.AuditLog auditLog : auditLogResult.getContent()) {
                            HitlAuditLog log = new HitlAuditLog();
                            log.setTimestamp(auditLog.getCreatedat());
                            log.setAction(auditLog.getAction());
                            log.setEntityType(auditLog.getEntitytype());
                            log.setEntityName(auditLog.getEntityname());
                            log.setUser(auditLog.getUsername());
                            log.setDetails(auditLog.getDetails());
                            auditLogs.add(log);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error al cargar logs de auditoría HITL", e);
                }
            }
            
            // Cargar logs de políticas relacionados con HITL
            if (policyAuditLogService != null) {
                try {
                    codeflowx.nocode.persist.PageParams pageParams = codeflowx.nocode.persist.PageParams.builder()
                        .maxRows(30)
                        .pageActual(1)
                        .rowActual(0)
                        .ascending(false)
                        .sortField("createdat")
                        .build();
                    
                    codeflowx.nocode.persist.PageResult<com.codeflowx.govern.entity.governance.PolicyAuditLog> policyLogResult = 
                        policyAuditLogService.findAll(pageParams);
                    
                    if (policyLogResult != null && policyLogResult.getContent() != null) {
                        for (com.codeflowx.govern.entity.governance.PolicyAuditLog policyLog : policyLogResult.getContent()) {
                            HitlAuditLog log = new HitlAuditLog();
                            log.setTimestamp(policyLog.getCreatedat());
                            log.setAction("POLICY_" + policyLog.getAction());
                            log.setEntityType("POLICY");
                            log.setEntityName(policyLog.getPolicyname());
                            log.setUser(policyLog.getUsername());
                            log.setDetails(policyLog.getDetails());
                            auditLogs.add(log);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error al cargar logs de políticas HITL", e);
                }
            }

            calculateMetrics();
            log.info("✅ Cargadas {} intervenciones desde servicios", interventions.size());

        } catch (Exception e) {
            log.error("Error al cargar datos reales de supervisión", e);
            interventions.clear();
            auditLogs.clear();
        }
    }

    /**
     * Extraer tipo de entidad desde process definition ID
     */
    private String extractTypeFromProcess(String processDefId) {
        if (processDefId == null) return "UNKNOWN";
        if (processDefId.contains("agent")) return "AGENT";
        if (processDefId.contains("model")) return "MODEL";
        if (processDefId.contains("prompt")) return "PROMPT";
        return "UNKNOWN";
    }

    /**
     * Cargar datos MOCK para demo
     */
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Supervisión Humana...");

        // Intervenciones de ejemplo
        interventions.clear();

        // Intervención 1: Aprobación de Agente
        HitlIntervention interv1 = new HitlIntervention();
        interv1.setId("hitl-001");
        interv1.setType("AGENT");
        interv1.setEntityName("Agent-Fraud-Detection-v2");
        interv1.setEntityVersion("2.1.0");
        interv1.setStatus("APPROVED");
        interv1.setRequestedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(2)));
        interv1.setCompletedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
        interv1.setResponseTimeHours(1.0);
        interv1.setReviewer("admin@codeflowx.com");
        interv1.setDecision("APPROVE");
        interv1.setJustification("Agente cumple todos los umbrales de riesgo y compliance");
        interv1.setRiskScore(85.0);
        interv1.setComplianceScore(92.0);
        interv1.setEthicsScore(88.0);
        interventions.add(interv1);

        // Intervención 2: Revisión de Modelo
        HitlIntervention interv2 = new HitlIntervention();
        interv2.setId("hitl-002");
        interv2.setType("MODEL");
        interv2.setEntityName("Modelo-Prediccion-Fraude-v4.2");
        interv2.setEntityVersion("4.2.0");
        interv2.setStatus("PENDING");
        interv2.setRequestedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(5)));
        interv2.setResponseTimeHours(5.0);
        interv2.setReviewer("governance-lead@codeflowx.com");
        interv2.setRiskScore(78.0);
        interv2.setComplianceScore(85.0);
        interv2.setEthicsScore(82.0);
        interventions.add(interv2);

        // Intervención 3: Revisión de Prompt
        HitlIntervention interv3 = new HitlIntervention();
        interv3.setId("hitl-003");
        interv3.setType("PROMPT");
        interv3.setEntityName("Prompt-Customer-Support-v1.5");
        interv3.setEntityVersion("1.5.0");
        interv3.setStatus("APPROVED");
        interv3.setRequestedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        interv3.setCompletedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1).plusHours(3)));
        interv3.setResponseTimeHours(3.0);
        interv3.setReviewer("compliance-officer@codeflowx.com");
        interv3.setDecision("APPROVE");
        interv3.setJustification("Prompt validado, no contiene contenido sensible");
        interv3.setRiskScore(65.0);
        interv3.setComplianceScore(90.0);
        interv3.setEthicsScore(88.0);
        interventions.add(interv3);

        // Intervención 4: SLA Excedido
        HitlIntervention interv4 = new HitlIntervention();
        interv4.setId("hitl-004");
        interv4.setType("AGENT");
        interv4.setEntityName("Agent-Credit-Scoring-v3.0");
        interv4.setEntityVersion("3.0.0");
        interv4.setStatus("PENDING");
        interv4.setRequestedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(25)));
        interv4.setResponseTimeHours(25.0);
        interv4.setReviewer(null);
        interv4.setSlaExceeded(true);
        interv4.setRiskScore(92.0);
        interv4.setComplianceScore(75.0);
        interv4.setEthicsScore(80.0);
        interventions.add(interv4);

        // Logs de auditoría
        auditLogs.clear();
        HitlAuditLog log1 = new HitlAuditLog();
        log1.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
        log1.setAction("APPROVE");
        log1.setEntityType("AGENT");
        log1.setEntityName("Agent-Fraud-Detection-v2");
        log1.setUser("admin@codeflowx.com");
        log1.setDetails("Aprobación manual después de revisión de scores");
        auditLogs.add(log1);

        HitlAuditLog log2 = new HitlAuditLog();
        log2.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        log2.setAction("APPROVE");
        log2.setEntityType("PROMPT");
        log2.setEntityName("Prompt-Customer-Support-v1.5");
        log2.setUser("compliance-officer@codeflowx.com");
        log2.setDetails("Validación de contenido y compliance");
        auditLogs.add(log2);

        calculateMetrics();
        log.info("✅ Datos MOCK cargados - {} intervenciones, {} logs de auditoría",
                 interventions.size(), auditLogs.size());
    }

    /**
     * Calcular métricas HITL
     */
    private void calculateMetrics() {
        totalInterventions = interventions.size();
        pendingInterventions = (int) interventions.stream()
            .filter(i -> "PENDING".equals(i.getStatus()))
            .count();
        approvedInterventions = (int) interventions.stream()
            .filter(i -> "APPROVED".equals(i.getStatus()))
            .count();
        rejectedInterventions = (int) interventions.stream()
            .filter(i -> "REJECTED".equals(i.getStatus()))
            .count();

        // Calcular tiempo promedio de respuesta
        double totalTime = interventions.stream()
            .filter(i -> i.getResponseTimeHours() > 0)
            .mapToDouble(HitlIntervention::getResponseTimeHours)
            .sum();
        int count = (int) interventions.stream()
            .filter(i -> i.getResponseTimeHours() > 0)
            .count();
        averageResponseTime = count > 0 ? totalTime / count : 0.0;

        // Calcular cumplimiento SLA (asumiendo SLA de 24 horas)
        long slaCompliant = interventions.stream()
            .filter(i -> i.getResponseTimeHours() > 0 && i.getResponseTimeHours() <= 24.0)
            .count();
        slaComplianceRate = totalInterventions > 0 ? (slaCompliant * 100.0 / totalInterventions) : 0.0;

        slaExceededCount = (int) interventions.stream()
            .filter(i -> i.isSlaExceeded())
            .count();
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange({"interventions", "selectedIntervention"})
    public void filterInterventions() {
        log.info("🔍 Filtrando intervenciones - Tipo: {}, Estado: {}, Rango: {}",
                 filterType, filterStatus, filterDateRange);
        // TODO: Implementar filtrado real
        // Por ahora, en modo mock los datos ya están filtrados
    }

    @Command
    @NotifyChange({"selectedIntervention"})
    public void selectIntervention(@BindingParam("intervention") HitlIntervention intervention) {
        this.selectedIntervention = intervention;
        log.info("📋 Intervención seleccionada: {}", intervention.getId());
    }

    @Command
    public void openInterventionForm() {
        if (selectedIntervention == null) {
            Messagebox.show("Por favor seleccione una intervención", "Atención",
                          Messagebox.OK, Messagebox.INFORMATION);
            return;
        }

        String url = "/console/bpmn/";
        switch (selectedIntervention.getType()) {
            case "AGENT":
                url += "agent-approval-human-override-form.zul?taskId=" + selectedIntervention.getId() +
                       (mockMode ? "&mock=true" : "");
                break;
            case "MODEL":
                url += "model-approval-human-override-form.zul?taskId=" + selectedIntervention.getId() +
                       (mockMode ? "&mock=true" : "");
                break;
            case "PROMPT":
                url += "prompt-human-review-form.zul?taskId=" + selectedIntervention.getId() +
                       (mockMode ? "&mock=true" : "");
                break;
        }

        Executions.sendRedirect(url);
    }

    @Command
    public void refreshData() {
        log.info("🔄 Refrescando datos de supervisión...");
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }

    // ========== Clases Internas ==========

    /**
     * Representa una intervención humana (HITL)
     */
    @Getter
    @Setter
    public static class HitlIntervention {
        private String id;
        private String type; // AGENT, MODEL, PROMPT
        private String entityName;
        private String entityVersion;
        private String status; // PENDING, APPROVED, REJECTED
        private Timestamp requestedAt;
        private Timestamp completedAt;
        private double responseTimeHours;
        private String reviewer;
        private String decision; // APPROVE, REJECT
        private String justification;
        private double riskScore;
        private double complianceScore;
        private double ethicsScore;
        private boolean slaExceeded = false;
    }

    /**
     * Representa un log de auditoría de supervisión
     */
    @Getter
    @Setter
    public static class HitlAuditLog {
        private Timestamp timestamp;
        private String action; // APPROVE, REJECT, ESCALATE
        private String entityType;
        private String entityName;
        private String user;
        private String details;
    }
}
