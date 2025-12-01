package com.codeflowx.govern.viewmodel.compliance;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.RuntimeService;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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
 * ViewModel para iniciar Conformity Assessment Process
 * Anexo VI EU AI Act - User Task inicial
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class InitiateConformityAssessmentViewModel extends BaseFront<InitiateConformityAssessmentViewModel>{

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private ModelService modelService;
    
    @WireVariable
    private RuntimeService runtimeService;
    
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

    // ========== Form fields ==========
    private Long projectId;
    private String projectName;
    private String assessmentType = "SELF"; // SELF o NOTIFIED_BODY
    private String assessmentDescription;
    
    // ========== Options ==========
    private String[] assessmentTypes = {"SELF", "NOTIFIED_BODY"};

    /**
     * Iniciar proceso de conformity assessment
     */
    @Command
    @NotifyChange("*")
    public void startAssessment() {
        log.info("Iniciando Conformity Assessment: projectId={}, type={}", projectId, assessmentType);
        
        try {
            if (projectId == null) {
                Messagebox.show("Por favor selecciona un proyecto", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }
            
            // Preparar variables del proceso
            Map<String, Object> processVariables = new HashMap<>();
            processVariables.put("projectId", projectId);
            processVariables.put("assessmentType", assessmentType);
            processVariables.put("assessmentDescription", assessmentDescription);
            processVariables.put("initiatedBy", ctxBean.getUser().getUsuname());
            processVariables.put("initiatedAt", System.currentTimeMillis());
            
            // Iniciar proceso BPMN
            if (runtimeService != null) {
                org.flowable.engine.runtime.ProcessInstance processInstance = 
                    runtimeService.startProcessInstanceByKey("conformity_assessment_process", processVariables);
                
                log.info("Conformity Assessment iniciado: processId={}", processInstance.getId());
                
                Messagebox.show(
                    "Conformity Assessment iniciado exitosamente.\nProcess ID: " + processInstance.getId(),
                    "Éxito",
                    Messagebox.OK,
                    Messagebox.INFORMATION
                );
            } else {
                log.warn("RuntimeService no disponible");
                Messagebox.show("Service no disponible", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            
            // Limpiar form
            projectId = null;
            projectName = null;
            assessmentDescription = null;
            
        } catch (Exception e) {
            log.error("Error iniciando conformity assessment", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Cancelar
     */
    @Command
    public void cancel() {
        projectId = null;
        projectName = null;
        assessmentDescription = null;
    }
}

