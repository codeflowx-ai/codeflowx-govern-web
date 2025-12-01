package com.codeflowx.govern.viewmodel.governance;
import com.codeflowx.framework.zkoss.BaseFront;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
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

import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.service.governance.PolicyService;
import com.codeflowx.govern.entity.governance.PolicyEvaluation;
import com.codeflowx.govern.entity.core.User;
import com.codeflowx.govern.entity.procedures.governance.RequestHumanReview;
import com.codeflowx.govern.service.governance.ComplianceAssessmentService;
import com.codeflowx.govern.service.governance.PolicyEvaluationService;
import com.codeflowx.govern.service.core.UserService;

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
 * Pantalla de Comité Ético
 * Gestiona revisiones humanas y decisiones del comité ético
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsCommitteeViewModel extends BaseFront<EthicsCommitteeViewModel>{

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private PolicyEvaluationService policyEvaluationService;
    @WireVariable
    private ComplianceAssessmentService complianceAssessmentService;
    @WireVariable
    private UserService userService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    
    @Override
    public void setBeans(Object bean) {}

    private PageParams pageParams;
    private List<ComplianceAssessment> pendingReviewsList = new ArrayList<>();
    private List<PolicyEvaluation> evaluationsList = new ArrayList<>();
    private List<User> committeeMembers = new ArrayList<>();
    
    // KPIs
    private Long totalReviews = 0L;
    private Long approvedReviews = 0L;
    private Long rejectedReviews = 0L;
    private Long pendingReviews = 0L;
    
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
        
        log.info("Inicializando EthicsCommitteeViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar assessments pendientes de revisión
            Criterias criterias1 = new Criterias();
            Criteria criteria1 = new Criteria(Operation.AND, Evaluation.EQUALS, "status");
            criteria1.setValueEnd("PENDING_REVIEW");
            criterias1.addCriteria(criteria1);
            
            PageResult<ComplianceAssessment> result1 = complianceAssessmentService.findAll(pageParams, criterias1
            );
            if (result1 != null && result1.getContent() != null) {
                pendingReviewsList = result1.getContent();
            }
            
            // Cargar todas las evaluaciones
            PageResult<PolicyEvaluation> result2 = policyEvaluationService.findAll(pageParams, new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                evaluationsList = result2.getContent();
            }
            
            // Cargar miembros del comité (usuarios con rol AI_GOVERNANCE_ADMIN)
            PageResult<User> result3 = userService.findAll(
                PageParams.builder().maxRows(100).pageActual(1).rowActual(0).build(), 
                new Criterias()
            );
            if (result3 != null && result3.getContent() != null) {
                committeeMembers = result3.getContent();
            }
            
            log.info("Cargadas {} revisiones pendientes", pendingReviewsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos del comité ético", e);
        }
    }
    
    private void calculateKPIs() {
        totalReviews = (long) pendingReviewsList.size();
        approvedReviews = pendingReviewsList.stream()
            .filter(a -> "APPROVED".equals(a.getStatus()))
            .count();
        rejectedReviews = pendingReviewsList.stream()
            .filter(a -> "REJECTED".equals(a.getStatus()))
            .count();
        pendingReviews = pendingReviewsList.stream()
            .filter(a -> "PENDING_REVIEW".equals(a.getStatus()))
            .count();
        
        log.info("KPIs calculados - Total: {}, Approved: {}, Rejected: {}, Pending: {}", 
            totalReviews, approvedReviews, rejectedReviews, pendingReviews);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    /**
     * Solicitar revisión humana para un assessment
     */
    @Command
    @NotifyChange("*")
    public void requestHumanReview(Long assessmentId, String reason) {
        try {
            RequestHumanReview procedure = new RequestHumanReview();
            procedure.setPInputParam(assessmentId);
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                loadData();
                calculateKPIs();
                Messagebox.show("Revisión humana solicitada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("No se pudo solicitar la revisión humana", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al solicitar revisión humana", e);
            Messagebox.show("Error al solicitar revisión: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Destroy
    public void destroy() {
        if (pendingReviewsList != null) { 
            pendingReviewsList.clear(); 
            pendingReviewsList = null; 
        }
        if (evaluationsList != null) { 
            evaluationsList.clear(); 
            evaluationsList = null; 
        }
        if (committeeMembers != null) { 
            committeeMembers.clear(); 
            committeeMembers = null; 
        }
        policyService = null;
            policyEvaluationService = null;
            complianceAssessmentService = null;
    }
}

