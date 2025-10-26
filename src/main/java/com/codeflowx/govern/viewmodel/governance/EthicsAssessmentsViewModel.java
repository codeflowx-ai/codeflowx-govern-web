package com.codeflowx.govern.viewmodel.governance;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.entity.governance.PolicyEvaluation;

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
 * Pantalla de Evaluaciones Éticas
 * Gestiona assessments éticos de modelos ML/AI
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsAssessmentsViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable private BusinessService businessService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {}

    private PageParams pageParams;
    private List<ComplianceAssessment> assessmentsList = new ArrayList<>();
    private List<PolicyEvaluation> evaluationsList = new ArrayList<>();
    
    // KPIs
    private Long totalAssessments = 0L;
    private Long passedAssessments = 0L;
    private Long failedAssessments = 0L;
    private BigDecimal avgEthicsScore = BigDecimal.ZERO;
    
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
        
        log.info("Inicializando EthicsAssessmentsViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            PageResult<ComplianceAssessment> result1 = businessService.findAllEntity(
                ComplianceAssessment.class, 
                pageParams, 
                new Criterias()
            );
            if (result1 != null && result1.getContent() != null) {
                assessmentsList = result1.getContent();
            }
            
            PageResult<PolicyEvaluation> result2 = businessService.findAllEntity(
                PolicyEvaluation.class, 
                pageParams, 
                new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                evaluationsList = result2.getContent();
            }
            
            log.info("Cargados {} assessments éticos", assessmentsList.size());
        } catch (Exception e) {
            log.error("Error al cargar assessments éticos", e);
        }
    }
    
    private void calculateKPIs() {
        totalAssessments = (long) assessmentsList.size();
        passedAssessments = assessmentsList.stream()
            .filter(a -> "PASSED".equals(a.getStatus()))
            .count();
        failedAssessments = totalAssessments - passedAssessments;
        
        if (!assessmentsList.isEmpty()) {
            BigDecimal sum = assessmentsList.stream()
                .filter(a -> a.getOverallscore() != null)
                .map(ComplianceAssessment::getOverallscore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgEthicsScore = sum.divide(new BigDecimal(assessmentsList.size()), 2, RoundingMode.HALF_UP);
        }
        
        log.info("KPIs calculados - Total: {}, Passed: {}, Failed: {}, Avg Score: {}", 
            totalAssessments, passedAssessments, failedAssessments, avgEthicsScore);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void filterByFramework(String framework) {
        try {
            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "complianceframework");
            criteria.setValueEnd(framework);
            criterias.addCriteria(criteria);
            
            PageResult<ComplianceAssessment> result = businessService.findAllEntity(
                ComplianceAssessment.class, 
                pageParams, 
                criterias
            );
            if (result != null && result.getContent() != null) {
                assessmentsList = result.getContent();
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por framework", e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (assessmentsList != null) { 
            assessmentsList.clear(); 
            assessmentsList = null; 
        }
        if (evaluationsList != null) { 
            evaluationsList.clear(); 
            evaluationsList = null; 
        }
        businessService = null;
    }
}



