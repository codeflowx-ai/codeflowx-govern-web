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

import com.codeflowx.govern.entity.governance.PolicyViolation;
import com.codeflowx.govern.service.governance.PolicyService;
import com.codeflowx.govern.entity.governance.ComplianceFinding;
import com.codeflowx.govern.entity.procedures.governance.RemediateComplianceFinding;
import com.codeflowx.govern.service.governance.ComplianceFindingService;
import com.codeflowx.govern.service.governance.PolicyViolationService;

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
 * Pantalla de Violaciones Éticas
 * Gestiona violaciones de políticas éticas y compliance
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsViolationsViewModel extends BaseFront<EthicsViolationsViewModel>{

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private PolicyViolationService policyViolationService;
    @WireVariable
    private ComplianceFindingService complianceFindingService;
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
    private List<PolicyViolation> violationsList = new ArrayList<>();
    private List<ComplianceFinding> findingsList = new ArrayList<>();
    
    // KPIs
    private Long totalViolations = 0L;
    private Long criticalViolations = 0L;
    private Long resolvedViolations = 0L;
    private Long pendingViolations = 0L;
    
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
                .sortField("detectedat")
                .build();
        
        log.info("Inicializando EthicsViolationsViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            PageResult<PolicyViolation> result1 = policyViolationService.findAll(pageParams, new Criterias()
            );
            if (result1 != null && result1.getContent() != null) {
                violationsList = result1.getContent();
            }
            
            PageResult<ComplianceFinding> result2 = complianceFindingService.findAll(pageParams, new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                findingsList = result2.getContent();
            }
            
            log.info("Cargadas {} violaciones éticas", violationsList.size());
        } catch (Exception e) {
            log.error("Error al cargar violaciones éticas", e);
        }
    }
    
    private void calculateKPIs() {
        totalViolations = (long) violationsList.size();
        criticalViolations = violationsList.stream()
            .filter(v -> "CRITICAL".equals(v.getSeverity()))
            .count();
        resolvedViolations = violationsList.stream()
            .filter(v -> "RESOLVED".equals(v.getStatus()))
            .count();
        pendingViolations = totalViolations - resolvedViolations;
        
        log.info("KPIs calculados - Total: {}, Critical: {}, Resolved: {}, Pending: {}", 
            totalViolations, criticalViolations, resolvedViolations, pendingViolations);
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
    public void filterBySeverity(String severity) {
        try {
            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "severity");
            criteria.setValueEnd(severity);
            criterias.addCriteria(criteria);
            
            PageResult<PolicyViolation> result = policyViolationService.findAll(pageParams, criterias
            );
            if (result != null && result.getContent() != null) {
                violationsList = result.getContent();
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por severidad", e);
        }
    }
    
    /**
     * Llamar procedure para remediar una violación
     */
    @Command
    @NotifyChange("*")
    public void remediateViolation(Long violationId) {
        try {
            RemediateComplianceFinding procedure = new RemediateComplianceFinding();
            procedure.setPInputParam(violationId);
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                loadData();
                calculateKPIs();
                Messagebox.show("Violación remediada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("No se pudo remediar la violación", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al remediar violación", e);
            Messagebox.show("Error al remediar violación: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Destroy
    public void destroy() {
        if (violationsList != null) { 
            violationsList.clear(); 
            violationsList = null; 
        }
        if (findingsList != null) { 
            findingsList.clear(); 
            findingsList = null; 
        }
        policyService = null;
            policyViolationService = null;
            complianceFindingService = null;
    }
}

