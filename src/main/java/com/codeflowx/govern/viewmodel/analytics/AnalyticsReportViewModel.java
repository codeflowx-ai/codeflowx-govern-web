package com.codeflowx.govern.viewmodel.analytics;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.suinsit.nocode.web.MasterBeanUI;
import codeflowx.nocode.persist.*;
import com.codeflowx.govern.entity.analytics.AnalyticsReport;
import com.codeflowx.govern.service.analytics.AnalyticsReportService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;

/**
 * ViewModel CRUD para Reportes de Analytics
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AnalyticsReportViewModel extends MasterBeanUI {
    
    @WireVariable
    private AnalyticsReportService analyticsReportService;
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

    
    private PageResult<AnalyticsReport> pageResult;
    private PageParams pageParams;
    private String searchTerm = "";
    private String filterType = "all";
    private String filterStatus = "all";
    private AnalyticsReport selectedReport;
    private boolean showDialog = false;
    private boolean showDataDialog = false;
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
            log.debug("Cargando reportes - Página: {}, SearchTerm: '{}', Type: '{}', Status: '{}'", 
                     pageParams.getPageActual(), searchTerm, filterType, filterStatus);
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Búsqueda por nombre o descripción
                String sql = "SELECT * FROM ANLANALYTICSREPORTS WHERE " +
                            "UPPER(ANLREPORTNAME) LIKE :search OR " +
                            "UPPER(ANLREPORTDESCRIPTION) LIKE :search";
                
                if (!"all".equals(filterType)) {
                    sql += " AND ANLREPORTTYPE = :type";
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
                
                pageResult = businessService.findByParams(AnalyticsReport.class, sql, params, pageParams);
            } else if (!"all".equals(filterType) || !"all".equals(filterStatus)) {
                // Filtros sin búsqueda
                StringBuilder sql = new StringBuilder("SELECT * FROM ANLANALYTICSREPORTS WHERE 1=1");
                Map<String, Object> params = new HashMap<>();
                
                if (!"all".equals(filterType)) {
                    sql.append(" AND ANLREPORTTYPE = :type");
                    params.put("type", filterType);
                }
                if (!"all".equals(filterStatus)) {
                    sql.append(" AND ANLSTATUS = :status");
                    params.put("status", filterStatus);
                }
                
                sql.append(" ORDER BY ANLCREATEDAT DESC");
                pageResult = businessService.findByParams(AnalyticsReport.class, sql.toString(), params, pageParams);
            } else {
                // Listar todos
                String sqlAll = "SELECT * FROM ANLANALYTICSREPORTS ORDER BY ANLCREATEDAT DESC";
                pageResult = businessService.findByParams(AnalyticsReport.class, sqlAll, null, pageParams);
            }
            
            log.debug("Reportes cargados: {}", pageResult != null && pageResult.getContent() != null ? pageResult.getContent().size() : 0);
        } catch (Exception e) {
            log.error("Error cargando reportes de analytics", e);
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
    @NotifyChange({"selectedReport", "showDialog", "isEditing"})
    public void newReport() {
        selectedReport = new AnalyticsReport();
        isEditing = false;
        showDialog = true;
        log.debug("Abriendo formulario para nuevo reporte");
    }
    
    @Command
    @NotifyChange({"selectedReport", "showDialog", "isEditing"})
    public void editReport(@BindingParam("item") AnalyticsReport report) {
        try {
            selectedReport = analyticsReportService.findById(report.getIdxanalyticsreport());
            isEditing = true;
            showDialog = true;
            log.debug("Editando reporte: ID={}", report.getIdxanalyticsreport());
        } catch (GovernanceServiceException e) {
            log.error("Error cargando reporte para edición", e);
        }
    }
    
    @Command
    @NotifyChange({"pageResult", "showDialog", "selectedReport"})
    public void saveReport() {
        try {
            if (selectedReport != null) {
                log.debug("Guardando reporte: {}", selectedReport.getAnlreportname());
                selectedReport = analyticsReportService.create(selectedReport);
                log.info("Reporte guardado exitosamente: ID={}", selectedReport.getIdxanalyticsreport());
                showDialog = false;
                selectedReport = null;
                loadData();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error guardando reporte", e);
        }
    }
    
    @Command
    @NotifyChange("pageResult")
    public void deleteReport(@BindingParam("item") AnalyticsReport report) {
        try {
            if (report != null) {
                log.debug("Eliminando reporte: ID={}, Nombre={}", report.getIdxanalyticsreport(), report.getAnlreportname());
                analyticsReportService.deleteById(report.getIdxanalyticsreport());
                log.info("Reporte eliminado exitosamente: ID={}", report.getIdxanalyticsreport());
                loadData();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error eliminando reporte", e);
        }
    }
    
    @Command
    @NotifyChange({"selectedReport", "showDataDialog"})
    public void viewReportData(@BindingParam("item") AnalyticsReport report) {
        try {
            selectedReport = analyticsReportService.findById(report.getIdxanalyticsreport());
            showDataDialog = true;
            log.debug("Visualizando datos del reporte: ID={}", report.getIdxanalyticsreport());
        } catch (GovernanceServiceException e) {
            log.error("Error cargando datos del reporte", e);
        }
    }
    
    @Command
    @NotifyChange({"pageResult"})
    public void regenerateReport(@BindingParam("item") AnalyticsReport report) {
        try {
            if (report != null) {
                log.debug("Regenerando reporte: ID={}, Nombre={}", report.getIdxanalyticsreport(), report.getAnlreportname());
                // TODO: Implementar lógica de regeneración de reporte
                // Puede involucrar llamar a un stored procedure o función
                log.info("Reporte regenerado: ID={}", report.getIdxanalyticsreport());
                loadData();
            }
        } catch (Exception e) {
            log.error("Error regenerando reporte", e);
        }
    }
    
    @Command
    @NotifyChange({"showDialog", "selectedReport"})
    public void cancelEdit() {
        selectedReport = null;
        showDialog = false;
        log.debug("Cancelando edición");
    }
    
    @Command
    @NotifyChange({"showDataDialog", "selectedReport"})
    public void closeDataDialog() {
        showDataDialog = false;
        log.debug("Cerrando visor de datos");
    }
    
    @Command
    @NotifyChange("pageResult")
    public void refresh() {
        log.debug("Refrescando lista de reportes");
        loadData();
    }
    
    // Métodos auxiliares
    public String getReportStatusBadge(String status) {
        if (status == null) return "secondary";
        switch (status.toLowerCase()) {
            case "completed": return "success";
            case "generating": return "info";
            case "pending": return "warning";
            case "failed": return "danger";
            default: return "secondary";
        }
    }
    
    public String truncateDescription(String description) {
        if (description == null) return "";
        return description.length() > 100 ? description.substring(0, 100) + "..." : description;
    }


    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
