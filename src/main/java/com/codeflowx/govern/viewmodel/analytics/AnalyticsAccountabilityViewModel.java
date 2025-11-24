package com.codeflowx.govern.viewmodel.analytics;

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

import com.codeflowx.govern.entity.governance.PolicyAuditLog;
import com.codeflowx.govern.service.governance.PolicyService;
import com.codeflowx.govern.entity.governance.PolicyEvaluation;
import com.codeflowx.govern.service.governance.PolicyAuditLogService;
import com.codeflowx.govern.service.governance.PolicyEvaluationService;

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
 * Dashboard consolidado de análisis de Accountability (Responsabilidad)
 * Gestiona audit logs y trazabilidad de decisiones
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AnalyticsAccountabilityViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private PolicyEvaluationService policyEvaluationService;
    @WireVariable
    private PolicyAuditLogService policyAuditLogService;
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
    private List<PolicyAuditLog> auditLogsList = new ArrayList<>();
    private List<PolicyEvaluation> evaluationsList = new ArrayList<>();
    
    // KPIs
    private Long totalAudits = 0L;
    private Long passedAudits = 0L;
    private Long failedAudits = 0L;
    private BigDecimal accountabilityScore = BigDecimal.ZERO;
    
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
        
        log.info("Inicializando AnalyticsAccountabilityViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar audit logs (TABLE - usar findAllEntity)
            PageParams pageParams1 = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<PolicyAuditLog> result1 = policyAuditLogService.findAll(pageParams1, new Criterias()
            );
            if (result1 != null && result1.getContent() != null) {
                auditLogsList = result1.getContent();
            }
            
            // Cargar evaluaciones de políticas (TABLE - usar findAllEntity)
            PageParams pageParams2 = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<PolicyEvaluation> result2 = policyEvaluationService.findAll(pageParams2, new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                evaluationsList = result2.getContent();
            }
            
            log.info("Cargados {} audit logs", auditLogsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos de accountability", e);
        }
    }
    
    private void calculateKPIs() {
        totalAudits = (long) auditLogsList.size();
        passedAudits = evaluationsList.stream()
            .filter(e -> "PASSED".equals(e.getResult()))
            .count();
        failedAudits = evaluationsList.stream()
            .filter(e -> "FAILED".equals(e.getResult()))
            .count();
        
        if (totalAudits > 0) {
            accountabilityScore = new BigDecimal(passedAudits)
                .divide(new BigDecimal(totalAudits), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        }
        
        log.info("KPIs calculados - Total: {}, Passed: {}, Failed: {}, Score: {}%", 
            totalAudits, passedAudits, failedAudits, accountabilityScore);
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
    public void filterByAction(String action) {
        try {
            // TABLE - usar findAllEntity con filtro
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "govaction");
            criteria.setValueEnd(action);
            criterias.addCriteria(criteria);
            
            PageResult<PolicyAuditLog> result = policyAuditLogService.findAll(pageParams, criterias
            );
            if (result != null && result.getContent() != null) {
                auditLogsList = result.getContent();
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por acción", e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (auditLogsList != null) { 
            auditLogsList.clear(); 
            auditLogsList = null; 
        }
        if (evaluationsList != null) { 
            evaluationsList.clear(); 
            evaluationsList = null; 
        }
        policyService = null;
            policyEvaluationService = null;
            policyAuditLogService = null;
    }
}

