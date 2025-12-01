package com.codeflowx.govern.viewmodel.core;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.List;

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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.codeflowx.govern.entity.views.core.SecurityAuditSummary;
import com.codeflowx.govern.service.core.SecurityAuditSummaryService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Dashboard de Auditoría de Seguridad
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SecurityAuditViewModel extends BaseFront<SecurityAuditViewModel>{
    
    @WireVariable
    private SecurityAuditSummaryService securityAuditSummaryService;
    @Autowired
    protected IEntityLocal dao;
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
    
    private Long totalItems = 0L;
    private Long activeItems = 0L;
    private Long deployedItems = 0L;
    private Long trainingItems = 0L;
    private Long offlineItems = 0L;
    private java.math.BigDecimal avgScore = java.math.BigDecimal.ZERO;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        doAfterCompose(view);
        initDao();
        loadSecurityData();
    }
    
    @Command
    @NotifyChange("*")
    public void loadSecurityData() {
        try {
            log.debug("Cargando auditoría de seguridad");
            List<SecurityAuditSummary> securityData = securityAuditSummaryService.findAll();
            
            if (securityData != null && !securityData.isEmpty()) {
                SecurityAuditSummary security = securityData.get(0);
                log.debug("Auditoría cargada - Security Score: {}, Level: {}", security.getAvgScore(), getSecurityLevel());
                
                // Mapear datos
                this.totalItems = security.getTotalItems() != null ? security.getTotalItems() : 0L;
                this.activeItems = security.getActiveItems() != null ? security.getActiveItems() : 0L;
                this.deployedItems = security.getDeployedItems() != null ? security.getDeployedItems() : 0L;
                this.trainingItems = security.getTrainingItems() != null ? security.getTrainingItems() : 0L;
                this.offlineItems = security.getOfflineItems() != null ? security.getOfflineItems() : 0L;
                this.avgScore = security.getAvgScore() != null ? security.getAvgScore() : java.math.BigDecimal.ZERO;
                
                log.info("Auditoría de seguridad - Total: {}, Score: {}, Level: {}", 
                         totalItems, avgScore, getSecurityLevel());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error cargando auditoría de seguridad", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void refresh() {
        log.debug("Refrescando auditoría de seguridad");
        loadSecurityData();
    }
    
    public String getSecurityLevel() {
        if (avgScore == null) return "Desconocido";
        double score = avgScore.doubleValue();
        if (score >= 90) return "Excelente";
        if (score >= 70) return "Bueno";
        if (score >= 50) return "Aceptable";
        return "Crítico";
    }
    
    public String getSecurityColor() {
        String level = getSecurityLevel();
        switch (level) {
            case "Excelente": return "success";
            case "Bueno": return "primary";
            case "Aceptable": return "warning";
            case "Crítico": return "danger";
            default: return "secondary";
        }
    }

    

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
