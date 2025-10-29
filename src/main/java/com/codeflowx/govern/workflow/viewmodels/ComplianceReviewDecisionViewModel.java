package com.codeflowx.govern.workflow.viewmodels;

import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
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
 * ViewModel: Compliance Review Decision
 * 
 * Formulario BPMN User Task para decidir acción después del Timer Boundary Event.
 * 
 * Proceso: compliance-monitoring-process
 * Task: reviewDecisionTask
 * Triggered by: Timer Boundary Event (después de 7 días)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ComplianceReviewDecisionViewModel extends MasterPage {

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

    // Variables del proceso BPMN
    @Getter @Setter
    private String taskId;
    
    @Getter @Setter
    private String processInstanceId;

    @Getter @Setter
    private Integer nonCompliantSystems;

    @Getter @Setter
    private String reviewScheduledFor;

    @Getter @Setter
    private Integer reviewDaysDelay;

    // Variables del formulario
    @Getter @Setter
    private String selectedDecision;

    @Getter @Setter
    private String reviewNotes;

    private Integer postponeDays = 7;
    
    private boolean mockMode = false;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando ComplianceReviewDecisionViewModel");
        
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
        if (mockMode) {
            this.mockMode = true;
            loadMockData();
            log.info("🎭 Mock mode activado");
            return;
        }
        
        try {
            // 1. Obtener taskId desde parámetros
            taskId = Executions.getCurrent().getParameter("taskId");
            
            if (taskId == null || taskId.isEmpty()) {
                log.error("❌ taskId no proporcionado");
                Messagebox.show("Error: Task ID no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            // 2. Cargar tarea de Flowable
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            
            if (task == null) {
                log.error("❌ Task no encontrada: {}", taskId);
                Messagebox.show("Error: Tarea no encontrada", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            processInstanceId = task.getProcessInstanceId();

            // 3. Cargar variables del proceso
            nonCompliantSystems = (Integer) runtimeService.getVariable(processInstanceId, "nonCompliantSystems");
            reviewScheduledFor = (String) runtimeService.getVariable(processInstanceId, "reviewScheduledFor");
            reviewDaysDelay = (Integer) runtimeService.getVariable(processInstanceId, "reviewDaysDelay");

            // Defaults
            if (reviewDaysDelay == null) reviewDaysDelay = 7;
            if (nonCompliantSystems == null) nonCompliantSystems = 0;

            log.info("✅ Formulario cargado - Task: {} | Process: {} | Non-Compliant: {}", 
                     taskId, processInstanceId, nonCompliantSystems);

        } catch (Exception e) {
            log.error("❌ Error inicializando formulario", e);
            Messagebox.show("Error cargando datos: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("selectedDecision")
    public void selectDecision(String decision) {
        this.selectedDecision = decision;
        log.info("📋 Decisión seleccionada: {}", decision);
    }

    @Command
    public void confirmDecision() {
        try {
            if (selectedDecision == null || selectedDecision.isEmpty()) {
                Messagebox.show("Por favor, selecciona una decisión", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando confirmDecision - decision={}", selectedDecision);
                logActivity("MOCK_COMPLIANCE_DECISION", "ComplianceDecision", null, "Simulación: " + selectedDecision);
                String message = getConfirmationMessage();
                Messagebox.show("✅ [DEMO] " + message, "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }

            log.info("✅ Confirmando decisión: {}", selectedDecision);

            // 1. Guardar variables en el proceso BPMN
            runtimeService.setVariable(processInstanceId, "reviewDecision", selectedDecision);
            runtimeService.setVariable(processInstanceId, "reviewNotes", reviewNotes);

            // Si posterga, actualizar los días
            if ("POSTPONE".equals(selectedDecision)) {
                runtimeService.setVariable(processInstanceId, "reviewDaysDelay", postponeDays);
                log.info("⏸️ Revisión postergada {} días", postponeDays);
            }

            // 2. Completar la tarea BPMN
            taskService.complete(taskId);

            log.info("✅ Task completada - Decisión: {} | Process: {}", selectedDecision, processInstanceId);

            // 3. Mostrar confirmación y cerrar ventana
            String message = getConfirmationMessage();
            Messagebox.show(message, "Decisión Confirmada", Messagebox.OK, Messagebox.INFORMATION, 
                event -> {
                    // Cerrar ventana
                    Executions.sendRedirect("/governance/reports/effectiveness.zul");
                });

        } catch (Exception e) {
            log.error("❌ Error confirmando decisión", e);
            Messagebox.show("Error al confirmar: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancel() {
        String redirect = mockMode ? "/plataforma/workflow/my-tasks.zul?mock=true" : "/governance/reports/effectiveness.zul";
        Executions.sendRedirect(redirect);
    }
    
    private void loadMockData() {
        this.taskId = "mock-compliance-decision-001";
        this.processInstanceId = "mock-compliance-decision-process-001";
        this.nonCompliantSystems = 3;
        this.reviewScheduledFor = "2025-11-15";
        this.reviewDaysDelay = 7;
        log.info("🎭 Mock data loaded for Compliance Review Decision");
    }

    private String getConfirmationMessage() {
        switch (selectedDecision) {
            case "RESTART":
                return "✅ Se reiniciará el proceso de revisión de compliance.\n" +
                       "Se ejecutará un nuevo compliance check completo.";
            case "POSTPONE":
                return "⏸️ La revisión se ha postergado " + postponeDays + " días.\n" +
                       "Se creará un nuevo timer que disparará esta tarea nuevamente.";
            case "FINISH":
                return "✅ El proceso de revisión se ha finalizado y archivado.\n" +
                       "No se tomarán más acciones automáticas.";
            default:
                return "Decisión registrada";
        }
    }
    
    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        businessService = null;
        taskService = null;
        runtimeService = null;
    }
}

