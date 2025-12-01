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

import com.codeflowx.govern.entity.views.core.AdminDashboardSummary;
import com.codeflowx.govern.service.core.AdminDashboardSummaryService;
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
 * Dashboard Administrativo - Resumen General del Sistema
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AdminDashboardViewModel extends BaseFront<AdminDashboardViewModel>{
    
    @WireVariable
    private AdminDashboardSummaryService adminDashboardSummaryService;
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
    
    // Métricas principales
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
        loadDashboardData();
    }
    
    @Command
    @NotifyChange("*")
    public void loadDashboardData() {
        try {
            log.debug("Cargando dashboard administrativo");
            // Cargar resumen desde vista (devuelve 1 fila con agregados)
            List<AdminDashboardSummary> summaries = adminDashboardSummaryService.findAll();
            
            if (summaries != null && !summaries.isEmpty()) {
                AdminDashboardSummary summary = summaries.get(0);
                log.debug("Dashboard cargado - Total items: {}", summary.getTotalItems());
                
                // Mapear datos
                this.totalItems = summary.getTotalItems() != null ? summary.getTotalItems() : 0L;
                this.activeItems = summary.getActiveItems() != null ? summary.getActiveItems() : 0L;
                this.deployedItems = summary.getDeployedItems() != null ? summary.getDeployedItems() : 0L;
                this.trainingItems = summary.getTrainingItems() != null ? summary.getTrainingItems() : 0L;
                this.offlineItems = summary.getOfflineItems() != null ? summary.getOfflineItems() : 0L;
                this.avgScore = summary.getAvgScore() != null ? summary.getAvgScore() : java.math.BigDecimal.ZERO;
                
                log.info("Dashboard administrativo cargado - Total: {}, Activos: {}, Desplegados: {}, Training: {}", 
                         totalItems, activeItems, deployedItems, trainingItems);
            }
        } catch (GovernanceServiceException e) {
            log.error("Error cargando dashboard administrativo", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void refresh() {
        log.debug("Refrescando dashboard administrativo");
        loadDashboardData();
    }
    
    // Métodos auxiliares para cálculos
    public String getActivePercentage() {
        if (totalItems == null || totalItems == 0) return "0";
        if (activeItems == null) return "0";
        return String.format("%.1f", (activeItems * 100.0 / totalItems));
    }
    
    public String getDeployedPercentage() {
        if (totalItems == null || totalItems == 0) return "0";
        if (deployedItems == null) return "0";
        return String.format("%.1f", (deployedItems * 100.0 / totalItems));
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

  
}
