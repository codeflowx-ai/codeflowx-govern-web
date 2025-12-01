package com.codeflowx.govern.viewmodel.models;
import com.codeflowx.framework.zkoss.BaseFront;

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

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.service.governance.ComplianceAssessmentService;
import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.entity.procedures.governance.AutoApproveArtifact;
import com.codeflowx.govern.entity.procedures.governance.RequestHumanReview;
import com.codeflowx.govern.service.models.ModelService;

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
 * Pantalla de Flujo de Aprobación de Modelos
 * Gestiona el workflow de aprobación de modelos ML
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ModelApprovalWorkflowViewModel extends BaseFront<ModelApprovalWorkflowViewModel>{

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private ComplianceAssessmentService complianceAssessmentService;
    @WireVariable
    private ModelService modelService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }
    
    @Override
    public void setBeans(Object bean) {}

    private PageParams pageParams;
    private List<Model> pendingModelsList = new ArrayList<>();
    private List<ComplianceAssessment> assessmentsList = new ArrayList<>();
    
    // KPIs
    private Long totalPendingModels = 0L;
    private Long approvedModels = 0L;
    private Long rejectedModels = 0L;
    private Long inReviewModels = 0L;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        pageParams = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("createdat")
                .build();
        
        log.info("Inicializando ModelApprovalWorkflowViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar modelos pendientes de aprobación
            Criterias criterias1 = new Criterias();
            Criteria criteria1 = new Criteria(Operation.AND, Evaluation.EQUALS, "mdlstatus");
            criteria1.setValueEnd("PENDING_APPROVAL");
            criterias1.addCriteria(criteria1);
            
            PageResult<Model> result1 = modelService.findAll(pageParams, criterias1
            );
            if (result1 != null && result1.getContent() != null) {
                pendingModelsList = result1.getContent();
            }
            
            // Cargar assessments de compliance
            PageResult<ComplianceAssessment> result2 = complianceAssessmentService.findAll(pageParams, new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                assessmentsList = result2.getContent();
            }
            
            log.info("Cargados {} modelos pendientes de aprobación", pendingModelsList.size());
        } catch (Exception e) {
            log.error("Error al cargar modelos pendientes", e);
        }
    }
    
    private void calculateKPIs() {
        totalPendingModels = (long) pendingModelsList.size();
        
        // Calcular modelos por estado desde assessments
        approvedModels = assessmentsList.stream()
            .filter(a -> "APPROVED".equals(a.getStatus()))
            .count();
        rejectedModels = assessmentsList.stream()
            .filter(a -> "REJECTED".equals(a.getStatus()))
            .count();
        inReviewModels = assessmentsList.stream()
            .filter(a -> "PENDING_REVIEW".equals(a.getStatus()))
            .count();
        
        log.info("KPIs calculados - Pending: {}, Approved: {}, Rejected: {}, In Review: {}", 
            totalPendingModels, approvedModels, rejectedModels, inReviewModels);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    /**
     * Auto-aprobar modelo usando procedure
     */
    @Command
    @NotifyChange("*")
    public void autoApproveModel(Long modelId) {
        try {
            AutoApproveArtifact procedure = new AutoApproveArtifact();
            procedure.setPInputParam(modelId);
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                loadData();
                calculateKPIs();
                Messagebox.show("Modelo aprobado automáticamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("No se pudo aprobar el modelo automáticamente", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al auto-aprobar modelo", e);
            Messagebox.show("Error al aprobar modelo: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void approveModel(Long modelId, String comments) {
        try {
            Model model = modelService.findById(modelId);
            if (model != null) {
            	model.setModapprovalstatus("APPROVED");
            	model.setModapprovedat(new Timestamp(System.currentTimeMillis()));
            	model.setModapprovedby(getUser().getUsername());
            	model.setModupdatedby(getUser().getUsername());
                model = complianceAssessmentService.update(model);
                loadData();
                calculateKPIs();
                Messagebox.show("Modelo aprobado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error al aprobar modelo", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void rejectModel(Long modelId, String reason) {
        try {
            Model model = modelService.findById(modelId);
            if (model != null) {
                model.setModapprovalstatus("REJECTED");
                model.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                model.setModupdatedby(getUser().getUsername());
                model = complianceAssessmentService.update(model);
                
                loadData();
                calculateKPIs();
                Messagebox.show("Modelo rechazado", "Información", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error al rechazar modelo", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void requestReview(Long modelId) {
        try {
            RequestHumanReview procedure = new RequestHumanReview();
            procedure.setPInputParam(modelId);
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                loadData();
                calculateKPIs();
                Messagebox.show("Revisión humana solicitada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("No se pudo solicitar la revisión humana", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al solicitar revisión", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Destroy
    public void destroy() {
        if (pendingModelsList != null) { 
            pendingModelsList.clear(); 
            pendingModelsList = null; 
        }
        if (assessmentsList != null) { 
            assessmentsList.clear(); 
            assessmentsList = null; 
        }
        complianceAssessmentService = null;
            modelService = null;
    }
}

