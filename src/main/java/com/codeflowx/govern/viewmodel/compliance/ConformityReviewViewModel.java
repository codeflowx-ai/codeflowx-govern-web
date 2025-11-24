package com.codeflowx.govern.viewmodel.compliance;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.TaskService;
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
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import codeflowx.nocode.persist.BusinessService;
import com.codeflowx.govern.service.models.ModelService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para User Task de Conformity Assessment Review (BPMN)
 * Permite a compliance officers revisar assessments borderline
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ConformityReviewViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private ModelService modelService;
    
    @WireVariable
    private TaskService taskService;
    
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
        // Not needed
    }

    // ========== Task info ==========
    private String taskId;
    private Long assessmentId;
    private String systemName;
    private String assessmentDate;
    
    // ========== Compliance scores ==========
    private Integer art9Score = 0;
    private Integer art10Score = 0;
    private Integer art11Score = 0;
    private Integer art12Score = 0;
    private Integer art13Score = 0;
    private Integer art14Score = 0;
    private Integer art15Score = 0;
    private Integer overallScore = 0;
    
    // ========== Compliance status ==========
    private Boolean art9Compliant = false;
    private Boolean art10Compliant = false;
    private Boolean art11Compliant = false;
    private Boolean art12Compliant = false;
    private Boolean art13Compliant = false;
    private Boolean art14Compliant = false;
    private Boolean art15Compliant = false;
    
    // ========== Review decision ==========
    private String decision = "";  // APPROVED / REJECTED
    private String reviewNotes = "";
    private String reviewerName = "";

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, 
                             @QueryParam("taskId") String taskId) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        this.taskId = taskId;
        
        log.info("Inicializando ConformityReviewViewModel - TaskID: {}", taskId);
        
        loadTaskVariables();
    }

    private void loadTaskVariables() {
        try {
            if (taskId != null && taskService != null) {
                Map<String, Object> vars = taskService.getVariables(taskId);
                
                assessmentId = (Long) vars.get("assessmentId");
                systemName = (String) vars.get("systemName");
                
                // Scores
                art9Score = getScoreFromVar(vars, "art9Score");
                art10Score = getScoreFromVar(vars, "art10Score");
                art11Score = getScoreFromVar(vars, "art11Score");
                art12Score = getScoreFromVar(vars, "art12Score");
                art13Score = getScoreFromVar(vars, "art13Score");
                art14Score = getScoreFromVar(vars, "art14Score");
                art15Score = getScoreFromVar(vars, "art15Score");
                overallScore = getScoreFromVar(vars, "overallComplianceScore");
                
                // Compliance
                art9Compliant = getBooleanFromVar(vars, "art9Compliant");
                art10Compliant = getBooleanFromVar(vars, "art10Compliant");
                art11Compliant = getBooleanFromVar(vars, "art11Compliant");
                art12Compliant = getBooleanFromVar(vars, "art12Compliant");
                art13Compliant = getBooleanFromVar(vars, "art13Compliant");
                art14Compliant = getBooleanFromVar(vars, "art14Compliant");
                art15Compliant = getBooleanFromVar(vars, "art15Compliant");
                
                log.info("Task variables cargadas - Overall Score: {}", overallScore);
            }
        } catch (Exception e) {
            log.error("Error cargando variables de task", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void approveReview() {
        log.info("Aprobando review - Task: {}, Assessment: {}", taskId, assessmentId);
        
        if (reviewNotes == null || reviewNotes.trim().isEmpty()) {
            Messagebox.show("Por favor ingrese notas de revisión", "Validación", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("reviewDecision", "APPROVED");
            vars.put("reviewNotes", reviewNotes);
            vars.put("reviewedBy", reviewerName);
            vars.put("reviewDate", new java.sql.Timestamp(System.currentTimeMillis()));
            
            taskService.complete(taskId, vars);
            
            Messagebox.show(
                "Assessment aprobado exitosamente.\n\n" +
                "El proceso continuará con la generación de la Declaration of Conformity.",
                "Aprobado",
                Messagebox.OK,
                Messagebox.INFORMATION,
                event -> closeWindow()
            );
            
        } catch (Exception e) {
            log.error("Error aprobando review", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void rejectReview() {
        log.info("Rechazando review - Task: {}, Assessment: {}", taskId, assessmentId);
        
        if (reviewNotes == null || reviewNotes.trim().isEmpty()) {
            Messagebox.show("Por favor ingrese razones de rechazo", "Validación", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("reviewDecision", "REJECTED");
            vars.put("reviewNotes", reviewNotes);
            vars.put("reviewedBy", reviewerName);
            vars.put("reviewDate", new java.sql.Timestamp(System.currentTimeMillis()));
            
            taskService.complete(taskId, vars);
            
            Messagebox.show(
                "Assessment rechazado.\n\n" +
                "El assessment ha sido marcado como no conforme. " +
                "Se requiere remediation antes de poder emitir declaración.",
                "Rechazado",
                Messagebox.OK,
                Messagebox.INFORMATION,
                event -> closeWindow()
            );
            
        } catch (Exception e) {
            log.error("Error rechazando review", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    private Integer getScoreFromVar(Map<String, Object> vars, String key) {
        Object val = vars.get(key);
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Number) return ((Number) val).intValue();
        return 0;
    }

    private Boolean getBooleanFromVar(Map<String, Object> vars, String key) {
        Object val = vars.get(key);
        return Boolean.TRUE.equals(val);
    }

    private void closeWindow() {
        // TODO: Cerrar ventana o redirigir
    }

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos");
        businessService = null;
        taskService = null;
    }
}


































