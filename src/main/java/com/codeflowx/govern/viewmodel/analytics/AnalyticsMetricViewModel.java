package com.codeflowx.govern.viewmodel.analytics;

import java.math.BigDecimal;
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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.codeflowx.govern.entity.analytics.AnalyticsMetric;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel CRUD para Métricas de Analytics
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AnalyticsMetricViewModel extends MasterPage {
    
    @WireVariable
    private BusinessService businessService;
    @Autowired
    protected IEntityLocal dao;
    @WireVariable
    public Environment environment;
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    @WireVariable("ctxBean")
    protected Context ctxBean;
    @WireVariable("APPLICATION_DS")
    protected DataSource ds;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    
    private PageResult<AnalyticsMetric> pageResult ;
    private PageParams pageParams;
    private String searchTerm = "";
    private String filterType = "all";
    private String filterStatus = "all";
    private AnalyticsMetric selectedMetric;
    private boolean showDialog = false;
    private boolean isEditing = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        doAfterCompose(view);
        initDao();
        
        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadData();
    }
    
    @Command
    @NotifyChange("pageResult")
    public void loadData() {
        try {
            log.debug("Cargando métricas - Página: {}, SearchTerm: '{}', Type: '{}', Status: '{}'", 
                     pageParams.getPageActual(), searchTerm, filterType, filterStatus);
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Búsqueda por nombre
                String sql = "SELECT * FROM ANLANALYTICSMETRICS WHERE UPPER(ANLMETRICNAME) LIKE :search";
                
                if (!"all".equals(filterType)) {
                    sql += " AND ANLMETRICTYPE = :type";
                }
                if (!"all".equals(filterStatus)) {
                    sql += " AND ANLSTATUS = :status";
                }
                
                Map<String, Object> params = new HashMap<>();
                params.put("search", "%" + searchTerm.toUpperCase() + "%");
                if (!"all".equals(filterType)) {
                    params.put("type", filterType);
                }
                if (!"all".equals(filterStatus)) {
                    params.put("status", filterStatus);
                }
                
                pageResult = businessService.findByParams(AnalyticsMetric.class, sql, params, pageParams);
            } else if (!"all".equals(filterType) || !"all".equals(filterStatus)) {
                // Filtros sin búsqueda
                StringBuilder sql = new StringBuilder("SELECT * FROM ANLANALYTICSMETRICS WHERE 1=1");
                Map<String, Object> params = new HashMap<>();
                
                if (!"all".equals(filterType)) {
                    sql.append(" AND ANLMETRICTYPE = :type");
                    params.put("type", filterType);
                }
                if (!"all".equals(filterStatus)) {
                    sql.append(" AND ANLSTATUS = :status");
                    params.put("status", filterStatus);
                }
                
                sql.append(" ORDER BY ANLCREATEDAT DESC");
                pageResult = businessService.findByParams(AnalyticsMetric.class, sql.toString(), params, pageParams);
            } else {
                // Listar todos
                String sqlAll = "SELECT * FROM ANLANALYTICSMETRICS ORDER BY ANLCREATEDAT DESC";
                pageResult = businessService.findByParams(AnalyticsMetric.class, sqlAll, null, pageParams);
            }
            
            log.debug("Métricas cargadas: {}", pageResult != null && pageResult.getContent() != null ? pageResult.getContent().size() : 0);
        } catch (Exception e) {
            log.error("Error cargando métricas de analytics", e);
        }
    }
    
    @Command
    @NotifyChange("pageResult")
    public void search() {
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void clearSearch() {
        searchTerm = "";
        filterType = "all";
        filterStatus = "all";
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("pageResult")
    public void nextPage() {
        if (pageResult != null && pageResult.getNextRow() > 0) {
            pageParams.setRowActual(pageResult.getNextRow());
            pageParams.setPageActual(pageParams.getPageActual() + 1);
            loadData();
        }
        if (pageResult != null && pageResult.getNextRow() > 0) {
            pageParams.setRowActual(pageResult.getNextRow());
            pageParams.setPageActual(pageParams.getPageActual() + 1);
            loadData();
        }
    }
    
    @Command
    @NotifyChange("pageResult")
    public void previousPage() {
        if (pageParams.getPageActual() > 1) {
            pageParams.setPageActual(pageParams.getPageActual() - 1);
            pageParams.setRowActual((pageParams.getPageActual() - 1) * pageParams.getMaxRows());
            loadData();
        }
    }
    
    @Command
    @NotifyChange({"selectedMetric", "showDialog", "isEditing"})
    public void newMetric() {
        selectedMetric = new AnalyticsMetric();
        isEditing = false;
        showDialog = true;
        log.debug("Abriendo formulario para nueva métrica");
    }
    
    @Command
    @NotifyChange({"selectedMetric", "showDialog", "isEditing"})
    public void editMetric(@BindingParam("item") AnalyticsMetric metric) {
        try {
            selectedMetric = businessService.findById(AnalyticsMetric.class, metric.getIdxanalyticsmetric());
            isEditing = true;
            showDialog = true;
            log.debug("Editando métrica: ID={}", metric.getIdxanalyticsmetric());
        } catch (Exception e) {
            log.error("Error cargando métrica para edición", e);
        }
    }
    
    @Command
    @NotifyChange({"pageResult", "showDialog", "selectedMetric"})
    public void saveMetric() {
        try {
            if (selectedMetric != null) {
                log.debug("Guardando métrica: {}", selectedMetric.getAnlmetricname());
                businessService.save(selectedMetric);
                log.info("Métrica guardada exitosamente: ID={}", selectedMetric.getIdxanalyticsmetric());
                showDialog = false;
                selectedMetric = null;
                loadData();
            }
        } catch (Exception e) {
            log.error("Error guardando métrica", e);
        }
    }
    
    @Command
    @NotifyChange("pageResult")
    public void deleteMetric(@BindingParam("item") AnalyticsMetric metric) {
        try {
            if (metric != null) {
                log.debug("Eliminando métrica: ID={}, Nombre={}", metric.getIdxanalyticsmetric(), metric.getAnlmetricname());
                businessService.removeFromID(metric);
                log.info("Métrica eliminada exitosamente: ID={}", metric.getIdxanalyticsmetric());
                loadData();
            }
        } catch (Exception e) {
            log.error("Error eliminando métrica", e);
        }
    }
    
    @Command
    @NotifyChange({"showDialog", "selectedMetric"})
    public void cancelEdit() {
        selectedMetric = null;
        showDialog = false;
        log.debug("Cancelando edición");
    }
    
    @Command
    @NotifyChange("pageResult")
    public void refresh() {
        log.debug("Refrescando lista de métricas");
        loadData();
    }
    
    // Métodos auxiliares para visualización
    public String getMetricStatusBadge(String status) {
        if (status == null) return "secondary";
        switch (status.toLowerCase()) {
            case "active": return "success";
            case "warning": return "warning";
            case "critical": return "danger";
            default: return "secondary";
        }
    }
    
    public boolean isThresholdExceeded(AnalyticsMetric metric) {
        if (metric == null || metric.getAnlmetricvalue() == null) return false;
        
        BigDecimal value = metric.getAnlmetricvalue();
        if (metric.getAnlthresholdmax() != null && value.compareTo(metric.getAnlthresholdmax()) > 0) {
            return true;
        }
        if (metric.getAnlthresholdmin() != null && value.compareTo(metric.getAnlthresholdmin()) < 0) {
            return true;
        }
        return false;
    }


    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
