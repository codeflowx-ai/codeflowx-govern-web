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
 * ViewModel: Ethics Mitigation Plan
 * 
 * BPMN Process: ethics-review-v1
 * User Task: createMitigationPlanTask
 * Candidate Groups: ethics-committee, governance-admins
 * 
 * Funcionalidad:
 * - Crear plan de mitigación para problemas éticos detectados
 * - Definir acciones correctivas específicas
 * - Establecer timeline y responsables
 * - Definir criterios de validación y éxito
 * - Registrar plan para seguimiento
 * 
 * Input Variables (desde proceso BPMN):
 * - systemId: Long - ID del sistema
 * - systemName: String - Nombre del sistema
 * - ethicalIssues: List<String> - Problemas éticos identificados
 * - committeeRecommendations: List<String> - Recomendaciones del comité
 * - severity: String - Severidad de los problemas
 * 
 * Output Variables (al completar task):
 * - mitigation_plan: String - Plan detallado de mitigación
 * - mitigation_actions: List<String> - Acciones específicas
 * - timeline_days: Integer - Días estimados para completar
 * - responsible_team: String - Equipo responsable
 * - validation_criteria: String - Criterios de validación
 * - plan_created_by: String - Username
 * - plan_created_at: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/ethics-mitigation-plan.zul?taskId=mock-12&mock=true
 * - Datos simulados: Sistema automático con problemas éticos, plan de mitigación
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsMitigationPlanViewModel extends BaseFront<EthicsMitigationPlanViewModel>{

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
    private List<String> ethicalIssues = new ArrayList<>();
    private List<String> committeeRecommendations = new ArrayList<>();
    private String severity;
    private String mitigationPlan;
    private List<String> mitigationActions = new ArrayList<>();
    private Integer timelineDays;
    private String responsibleTeam;

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
       
       
        
        log.info("🚀 Inicializando EthicsMitigationPlanViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }
    
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Ethics Mitigation Plan...");
        
        taskId = Executions.getCurrent().getParameter("taskId");
        processInstanceId = "mock-process-12";
        
        systemName = "Sistema Automático Selección Personal v2.1";
        severity = "HIGH";
        
        ethicalIssues.add("Sesgo de género en selección de candidatos");
        ethicalIssues.add("Falta de transparencia en criterios de evaluación");
        ethicalIssues.add("No hay mecanismo de apelación para rechazados");
        
        committeeRecommendations.add("Rebalancear dataset de entrenamiento");
        committeeRecommendations.add("Implementar sistema de explicabilidad");
        committeeRecommendations.add("Crear proceso de apelación humana");
        
        // Plan sugerido
        timelineDays = 45;
        responsibleTeam = "ML Engineering + Ethics Team";
        
        log.info("✅ Datos MOCK cargados - Sistema RRHH con problemas éticos");
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

            Map<String, Object> vars = runtimeService.getVariables(processInstanceId);
            systemName = (String) vars.get("systemName");
            severity = (String) vars.get("severity");
            
            log.info("✅ Ethics Mitigation Plan cargado: {}", systemName);

        } catch (Exception e) {
            log.error("❌ Error inicializando formulario", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Comandos ==========
    
    @Command
    public void submitPlan() {
        if (mockMode) {
            log.info("🎭 MOCK: Simulando envío de plan de mitigación ética...");
            Messagebox.show(
                "✅ DEMO: Plan de Mitigación Ética creado\n\n" +
                "Sistema: " + systemName + "\n" +
                "Timeline: " + timelineDays + " días\n" +
                "Responsable: " + responsibleTeam + "\n" +
                "Acciones: " + mitigationActions.size() + "\n\n" +
                "(Modo MOCK - No se guardó en BD)", 
                "Demo - Plan Creado", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        try {
            Map<String, Object> taskVars = new HashMap<>();
            taskVars.put("mitigation_plan", mitigationPlan);
            taskVars.put("mitigation_actions", mitigationActions);
            taskVars.put("timeline_days", timelineDays);
            taskVars.put("responsible_team", responsibleTeam);
            taskVars.put("plan_created_by", getUser().getUsername());
            taskVars.put("plan_created_at", new java.sql.Timestamp(System.currentTimeMillis()));

            taskService.complete(taskId, taskVars);
            
            logActivity("PLAN_MITIGACION", "ETHETHICSREVIEWS", Long.parseLong(taskId), 
                       "Plan mitigación ética: " + systemName);

            Messagebox.show("Plan de mitigación creado correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/console/govern/ethics-dashboard.zul"));

        } catch (Exception e) {
            log.error("❌ Error enviando plan", e);
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
        if (ethicalIssues != null) { ethicalIssues.clear(); ethicalIssues = null; }
        if (committeeRecommendations != null) { committeeRecommendations.clear(); committeeRecommendations = null; }
        if (mitigationActions != null) { mitigationActions.clear(); mitigationActions = null; }
        businessService = null;
        taskService = null;
        runtimeService = null;
    }
}
