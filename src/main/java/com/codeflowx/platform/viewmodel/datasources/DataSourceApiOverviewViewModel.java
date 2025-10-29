package com.codeflowx.platform.viewmodel.datasources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.datasources.DataSourceApi;
import codeflowx.nocode.persist.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para búsqueda y listado de APIs de Data Sources
 */
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class DataSourceApiOverviewViewModel extends BaseFront<DataSourceApiOverviewViewModel> {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<DataSourceApi> pageResult;
    
    // ========== Filtros ==========
    private String searchText = "";
    private String filterMethod = "";
    private String filterStatus = "";
    
    // ========== Datos ==========
    private List<DataSourceApi> apiSourcesList = new ArrayList<>();
    
    // ========== Métricas ==========
    private int totalApis = 0;
    private int activeApis = 0;
    private int errorApis = 0;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadData();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            Criterias criterias = buildCriterias();
            
            pageResult = businessService.findAllEntity(
                DataSourceApi.class,
                pageParams,
                criterias
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                apiSourcesList = pageResult.getContent();
                totalApis = pageResult.getTotalRows();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "DATASOURCEAPIS", null, 
                    "Búsqueda: " + apiSourcesList.size() + " resultados");
                
                log.info("Cargados {} APIs de {} totales", apiSourcesList.size(), totalApis);
            } else {
                apiSourcesList = new ArrayList<>();
                totalApis = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar APIs", e);
            Messagebox.show("Error al cargar APIs: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        try {
            activeApis = (int) apiSourcesList.stream()
                .filter(api -> "ACTIVE".equals(api.getApistatus()))
                .count();
                
            errorApis = (int) apiSourcesList.stream()
                .filter(api -> "ERROR".equals(api.getApistatus()))
                .count();
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria("apiname", searchText, "LIKE");
        }
        
        if (filterMethod != null && !filterMethod.trim().isEmpty()) {
            criterias.addCriteria("apimethod", filterMethod, "=");
        }
        
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria("apistatus", filterStatus, "=");
        }
        
        
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void filterApis() {
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void searchApis() {
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void clearFilters() {
        searchText = "";
        filterMethod = "";
        filterStatus = "";
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void refreshApis() {
        loadData();
        loadMetrics();
    }
    
    @Command
    public void createApi() {
        log.info("Crear nuevo API");
        // TODO: Navegar a pantalla de creación
    }
    
    @Command
    public void viewApi(@BindingParam("api") DataSourceApi api) {
        log.info("Ver API: {}", api.getApiname());
    }
    
    @Command
    public void editApi(@BindingParam("api") DataSourceApi api) {
        log.info("Editar API: {}", api.getApiname());
    }
    
    @Command
    @NotifyChange("*")
    public void testApi(@BindingParam("api") DataSourceApi api) {
        Messagebox.show("Test API requires leka-server integration", 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void deleteApi(@BindingParam("api") DataSourceApi api) {
        Messagebox.show("¿Está seguro de eliminar la API: " + api.getApiname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(api);
                        
                        // Auditar eliminación
                        logActivity("ELIMINAR", "DATASOURCEAPIS", api.getIdxdatasourceapi(), 
                            "API eliminada: " + api.getApiname());
                        
                        loadData();
                        loadMetrics();
                        Messagebox.show("API eliminada exitosamente", 
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar API", e);
                        Messagebox.show("Error: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    public String getApiStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "ACTIVE": return "success";
            case "INACTIVE": return "secondary";
            case "ERROR": return "danger";
            default: return "secondary";
        }
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
    
    @Destroy
    public void destroy() {
        if (apiSourcesList != null) { 
            apiSourcesList.clear(); 
            apiSourcesList = null; 
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
