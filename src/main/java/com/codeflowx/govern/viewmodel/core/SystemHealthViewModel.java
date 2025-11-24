package com.codeflowx.govern.viewmodel.core;

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

import com.codeflowx.govern.entity.views.core.SystemHealthOverview;
import com.codeflowx.govern.service.core.SystemHealthOverviewService;
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
 * Dashboard de Salud del Sistema
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SystemHealthViewModel extends MasterPage {
    
    @WireVariable
    private SystemHealthOverviewService systemHealthOverviewService;
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
        loadHealthData();
    }
    
    @Command
    @NotifyChange("*")
    public void loadHealthData() {
        try {
            log.debug("Cargando salud del sistema");
            List<SystemHealthOverview> healthData = systemHealthOverviewService.findAll();
            
            if (healthData != null && !healthData.isEmpty()) {
                SystemHealthOverview health = healthData.get(0);
                log.debug("Salud del sistema cargada - Status: {}", getHealthStatus());
                
                // Mapear datos
                this.totalItems = health.getTotalItems() != null ? health.getTotalItems() : 0L;
                this.activeItems = health.getActiveItems() != null ? health.getActiveItems() : 0L;
                this.deployedItems = health.getDeployedItems() != null ? health.getDeployedItems() : 0L;
                this.trainingItems = health.getTrainingItems() != null ? health.getTrainingItems() : 0L;
                this.offlineItems = health.getOfflineItems() != null ? health.getOfflineItems() : 0L;
                this.avgScore = health.getAvgScore() != null ? health.getAvgScore() : java.math.BigDecimal.ZERO;
                
                log.info("Salud del sistema - Total: {}, Activos: {}, Offline: {}, Score: {}", 
                         totalItems, activeItems, offlineItems, avgScore);
            }
        } catch (GovernanceServiceException e) {
            log.error("Error cargando salud del sistema", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void refresh() {
        log.debug("Refrescando salud del sistema");
        loadHealthData();
    }
    
    public String getHealthStatus() {
        if (offlineItems == null || avgScore == null) return "Desconocido";
        
        long offline = offlineItems;
        double score = avgScore.doubleValue();
        
        if (offline == 0 && score >= 90) return "Saludable";
        if (offline < 5 && score >= 70) return "Advertencia";
        return "Crítico";
    }
    
    public String getHealthColor() {
        String status = getHealthStatus();
        switch (status) {
            case "Saludable": return "success";
            case "Advertencia": return "warning";
            case "Crítico": return "danger";
            default: return "secondary";
        }
    }
    
    public String getHealthPercentage() {
        if (totalItems == null || totalItems == 0 || activeItems == null) return "0";
        return String.format("%.1f", (activeItems * 100.0 / totalItems));
    }

    

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
