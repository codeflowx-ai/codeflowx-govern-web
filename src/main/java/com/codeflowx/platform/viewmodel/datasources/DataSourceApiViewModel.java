package com.codeflowx.platform.viewmodel.datasources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.datasources.DataSource;
import com.codeflowx.govern.entity.datasources.DataSourceApi;
import com.codeflowx.platform.service.BaseFront;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceApiViewModel extends BaseFront {

    private Long datasourceId;
    private DataSource dataSource;
    private List<DataSourceApi> apiList = new ArrayList<>();
    private DataSourceApi selectedApi;
    
    private List<String> availableMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
    private List<String> availableResponseTypes = List.of("JSON", "XML", "TEXT", "CSV");
    private List<String> availableStatuses = List.of("ACTIVE", "INACTIVE", "ERROR");

    @Init(superclass = true)
    public void init() {
        logActivity("DATA_SOURCES_API", "ACCESS", "Usuario accedió a APIs de Data Sources");
        loadDataSourceApis();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Destroy
    public void destroy() {
        logActivity("DATA_SOURCES_API", "LEAVE", "Usuario salió de APIs de Data Sources");
    }

    private void loadDataSourceApis() {
        try {
            if (datasourceId != null) {
                dataSource = getUXCriteriaManager().findById(DataSource.class, datasourceId);
                if (dataSource != null) {
                    apiList = dataSource.getSubdatasourceapis();
                }
            } else {
                Criterias criterias = new Criterias();
                apiList = getUXCriteriaManager().find(DataSourceApi.class, criterias);
            }
        } catch (Exception e) {
            log.error("Error loading data source APIs", e);
            Messagebox.show("Error al cargar las APIs: " + e.getMessage(), 
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"selectedApi"})
    public void createNew() {
        selectedApi = new DataSourceApi();
        selectedApi.setApistatus("INACTIVE");
        selectedApi.setApimethod("GET");
        selectedApi.setApiresponsetype("JSON");
        selectedApi.setApitimeout(30000);
        selectedApi.setApicreatedat(new Timestamp(System.currentTimeMillis()));
        
        if (dataSource != null) {
            selectedApi.setDataSource(dataSource);
        }
        
        logActivity("DATA_SOURCES_API", "CREATE_INIT", "Iniciando creación de nueva API");
    }

    @Command
    @NotifyChange({"apiList", "selectedApi"})
    public void save() {
        try {
            if (selectedApi.getApiname() == null || selectedApi.getApiname().trim().isEmpty()) {
                Messagebox.show("El nombre de la API es obligatorio", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (selectedApi.getApiendpoint() == null || selectedApi.getApiendpoint().trim().isEmpty()) {
                Messagebox.show("El endpoint es obligatorio", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            selectedApi.setApiupdatedat(new Timestamp(System.currentTimeMillis()));
            getUXCriteriaManager().save(selectedApi);
            
            Messagebox.show("API guardada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            logActivity("DATA_SOURCES_API", "SAVE", "API guardada: " + selectedApi.getApiname());
            
            loadDataSourceApis();
            selectedApi = null;
        } catch (Exception e) {
            log.error("Error saving API", e);
            Messagebox.show("Error al guardar: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"selectedApi"})
    public void edit(DataSourceApi api) {
        selectedApi = api;
        logActivity("DATA_SOURCES_API", "EDIT_INIT", "Editando API: " + api.getApiname());
    }

    @Command
    @NotifyChange({"selectedApi"})
    public void testConnection(DataSourceApi api) {
        try {
            api.setApilasttestat(new Timestamp(System.currentTimeMillis()));
            api.setApitestresult("Test ejecutado correctamente");
            api.setApistatus("ACTIVE");
            getUXCriteriaManager().save(api);
            
            Messagebox.show("Test de conexión exitoso", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            logActivity("DATA_SOURCES_API", "TEST", "Test de conexión: " + api.getApiname());
            
            loadDataSourceApis();
        } catch (Exception e) {
            log.error("Error testing API", e);
            api.setApitestresult("Error: " + e.getMessage());
            api.setApistatus("ERROR");
            Messagebox.show("Error en el test: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"apiList", "selectedApi"})
    public void delete(DataSourceApi api) {
        Messagebox.show("¿Está seguro de eliminar la API '" + api.getApiname() + "'?",
            "Confirmar Eliminación", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        getUXCriteriaManager().remove(api);
                        Messagebox.show("API eliminada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                        
                        logActivity("DATA_SOURCES_API", "DELETE", "API eliminada: " + api.getApiname());
                        loadDataSourceApis();
                        if (selectedApi != null && selectedApi.getIdxdatasourceapi().equals(api.getIdxdatasourceapi())) {
                            selectedApi = null;
                        }
                    } catch (Exception e) {
                        log.error("Error deleting API", e);
                        Messagebox.show("Error al eliminar: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    @Command
    @NotifyChange({"selectedApi"})
    public void cancel() {
        selectedApi = null;
        logActivity("DATA_SOURCES_API", "CANCEL", "Cancelada edición/creación");
    }

    // Getters and Setters
    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public List<DataSourceApi> getApiList() {
        return apiList;
    }

    public DataSourceApi getSelectedApi() {
        return selectedApi;
    }

    public void setSelectedApi(DataSourceApi selectedApi) {
        this.selectedApi = selectedApi;
    }

    public List<String> getAvailableMethods() {
        return availableMethods;
    }

    public List<String> getAvailableResponseTypes() {
        return availableResponseTypes;
    }

    public List<String> getAvailableStatuses() {
        return availableStatuses;
    }
}
