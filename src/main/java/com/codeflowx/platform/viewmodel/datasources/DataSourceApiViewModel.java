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
import com.codeflowx.platform.service.BaseFront.Criteria;
import com.codeflowx.platform.service.BaseFront.Criterias;
import com.codeflowx.platform.service.BaseFront.Evaluation;
import com.codeflowx.platform.service.BaseFront.Operation;

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
        logActivity("ACCESS", "DATA_SOURCES_API", null, "Usuario accedió a APIs de Data Sources");
        loadDataSourceApis();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Destroy
    public void destroy() {
        logActivity("LEAVE", "DATA_SOURCES_API", null, "Usuario salió de APIs de Data Sources");
    }

    private void loadDataSourceApis() {
        try {
            if (datasourceId != null) {
                dataSource = businessService.findById(DataSource.class, datasourceId);
                if (dataSource != null) {
                    apiList = dataSource.getSubdatasourceapis();
                }
            } else {
                Criterias criterias = new Criterias();
                apiList = businessService.find(DataSourceApi.class, criterias);
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
        
        logActivity("CREATE_INIT", "DATA_SOURCES_API", null, "Iniciando creación de nueva API");
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
            businessService.save(selectedApi);
            
            Messagebox.show("API guardada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            logActivity("SAVE", "DATA_SOURCES_API", selectedApi.getIdxdatasourceapi(), "API guardada: " + selectedApi.getApiname());
            
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
        logActivity("EDIT_INIT", "DATA_SOURCES_API", api.getIdxdatasourceapi(), "Editando API: " + api.getApiname());
    }

    @Command
    @NotifyChange({"selectedApi"})
    public void testConnection(DataSourceApi api) {
        try {
            api.setApilasttestat(new Timestamp(System.currentTimeMillis()));
            api.setApitestresult("Test ejecutado correctamente");
            api.setApistatus("ACTIVE");
            businessService.save(api);
            
            Messagebox.show("Test de conexión exitoso", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            logActivity("TEST", "DATA_SOURCES_API", api.getIdxdatasourceapi(), "Test de conexión: " + api.getApiname());
            
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
                        businessService.removeFromID(DataSourceApi.class, api.getIdxdatasourceapi());
                        Messagebox.show("API eliminada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                        
                        logActivity("DELETE", "DATA_SOURCES_API", api.getIdxdatasourceapi(), "API eliminada: " + api.getApiname());
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
        logActivity("CANCEL", "DATA_SOURCES_API", null, "Cancelada edición/creación");
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
