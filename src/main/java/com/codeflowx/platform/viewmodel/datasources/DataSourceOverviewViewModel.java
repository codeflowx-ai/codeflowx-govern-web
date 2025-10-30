package com.codeflowx.platform.viewmodel.datasources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.datasources.DataSource;
import com.codeflowx.platform.service.BaseFront;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceOverviewViewModel extends BaseFront {

    private List<DataSource> allDataSources = new ArrayList<>();
    private List<DataSource> filteredDataSources = new ArrayList<>();
    
    // Filters
    private String searchTerm = "";
    private String filterType;
    private String filterStatus;
    
    // Pagination
    private int activePage = 0;
    private int pageSize = 20;
    
    // Statistics
    private long totalDataSources = 0;
    private long activeDataSources = 0;
    private long syncingDataSources = 0;
    private long errorDataSources = 0;
    
    private List<String> availableTypes = List.of("API", "DATABASE", "UPLOAD_DOCUMENTS", "WEB_SCRAPING");
    private List<String> availableStatuses = List.of("ACTIVE", "INACTIVE", "ERROR", "SYNCING");

    @Wire
    private Component mainComp;

    @Init(superclass = true)
    public void init() {
        logActivity("DATA_SOURCES", "ACCESS", "Usuario accedió al módulo de Data Sources");
        loadDataSources();
        calculateStatistics();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        this.mainComp = view;
    }

    @Destroy
    public void destroy() {
        logActivity("DATA_SOURCES", "LEAVE", "Usuario salió del módulo de Data Sources");
    }

    private void loadDataSources() {
        try {
            Criterias criterias = new Criterias();
            allDataSources = getUXCriteriaManager().find(DataSource.class, criterias);
            applyFilters();
        } catch (Exception e) {
            log.error("Error loading data sources", e);
            Messagebox.show("Error al cargar las fuentes de datos: " + e.getMessage(), 
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"filteredDataSources", "dataSourcesList", "totalSize"})
    public void applyFilters() {
        filteredDataSources = allDataSources.stream()
            .filter(ds -> {
                if (StringUtils.isNotBlank(searchTerm) && 
                    !ds.getDsname().toLowerCase().contains(searchTerm.toLowerCase())) {
                    return false;
                }
                if (StringUtils.isNotBlank(filterType) && !ds.getDstype().equals(filterType)) {
                    return false;
                }
                if (StringUtils.isNotBlank(filterStatus) && !ds.getDsstatus().equals(filterStatus)) {
                    return false;
                }
                return true;
            })
            .collect(Collectors.toList());
    }

    @Command
    @NotifyChange({"searchTerm", "filterType", "filterStatus", "filteredDataSources", "dataSourcesList"})
    public void clearFilters() {
        searchTerm = "";
        filterType = null;
        filterStatus = null;
        applyFilters();
        logActivity("DATA_SOURCES", "CLEAR_FILTERS", "Filtros limpiados");
    }

    @Command
    @NotifyChange({"allDataSources", "filteredDataSources", "dataSourcesList", "totalDataSources", 
                   "activeDataSources", "syncingDataSources", "errorDataSources"})
    public void showCreateDialog() {
        // Navigate to create page or show dialog
        logActivity("DATA_SOURCES", "CREATE_INIT", "Iniciando creación de nueva fuente de datos");
    }

    @Command
    public void viewDetails(DataSource item) {
        logActivity("DATA_SOURCES", "VIEW_DETAILS", "Viendo detalles de: " + item.getDsname());
        // Navigate to details
    }

    @Command
    public void editDataSource(DataSource item) {
        logActivity("DATA_SOURCES", "EDIT_INIT", "Editando fuente de datos: " + item.getDsname());
        // Navigate to edit page
    }

    @Command
    @NotifyChange({"allDataSources", "filteredDataSources", "dataSourcesList", "activeDataSources", "syncingDataSources"})
    public void syncDataSource(DataSource item) {
        try {
            item.setDsstatus("SYNCING");
            item.setDssyncstatus("IN_PROGRESS");
            item.setDsupdatedat(new Timestamp(System.currentTimeMillis()));
            getUXCriteriaManager().save(item);
            
            Messagebox.show("Sincronización iniciada para: " + item.getDsname(), 
                          "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            
            logActivity("DATA_SOURCES", "SYNC", "Sincronización iniciada: " + item.getDsname());
            loadDataSources();
            calculateStatistics();
        } catch (Exception e) {
            log.error("Error syncing data source", e);
            Messagebox.show("Error al sincronizar: " + e.getMessage(), 
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"allDataSources", "filteredDataSources", "dataSourcesList", "totalDataSources", 
                   "activeDataSources", "syncingDataSources", "errorDataSources"})
    public void deleteDataSource(DataSource item) {
        Messagebox.show("¿Está seguro de eliminar la fuente de datos '" + item.getDsname() + "'?",
            "Confirmar Eliminación", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        getUXCriteriaManager().remove(item);
                        Messagebox.show("Fuente de datos eliminada correctamente", 
                                      "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                        
                        logActivity("DATA_SOURCES", "DELETE", "Fuente de datos eliminada: " + item.getDsname());
                        loadDataSources();
                        calculateStatistics();
                    } catch (Exception e) {
                        log.error("Error deleting data source", e);
                        Messagebox.show("Error al eliminar: " + e.getMessage(), 
                                      "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    @Command
    @NotifyChange({"dataSourcesList"})
    public void changePage() {
        logActivity("DATA_SOURCES", "PAGE_CHANGE", "Cambio a página: " + activePage);
    }

    @NotifyChange({"totalDataSources", "activeDataSources", "syncingDataSources", "errorDataSources"})
    private void calculateStatistics() {
        totalDataSources = allDataSources.size();
        activeDataSources = allDataSources.stream().filter(ds -> "ACTIVE".equals(ds.getDsstatus())).count();
        syncingDataSources = allDataSources.stream().filter(ds -> "SYNCING".equals(ds.getDsstatus())).count();
        errorDataSources = allDataSources.stream().filter(ds -> "ERROR".equals(ds.getDsstatus())).count();
    }

    // Getters and Setters
    public List<DataSource> getDataSourcesList() {
        int start = activePage * pageSize;
        int end = Math.min(start + pageSize, filteredDataSources.size());
        return start < filteredDataSources.size() ? 
               filteredDataSources.subList(start, end) : new ArrayList<>();
    }

    public int getTotalSize() {
        return filteredDataSources.size();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
        applyFilters();
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        this.filterStatus = filterStatus;
    }

    public int getActivePage() {
        return activePage;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalDataSources() {
        return totalDataSources;
    }

    public long getActiveDataSources() {
        return activeDataSources;
    }

    public long getSyncingDataSources() {
        return syncingDataSources;
    }

    public long getErrorDataSources() {
        return errorDataSources;
    }

    public List<String> getAvailableTypes() {
        return availableTypes;
    }

    public List<String> getAvailableStatuses() {
        return availableStatuses;
    }
}


