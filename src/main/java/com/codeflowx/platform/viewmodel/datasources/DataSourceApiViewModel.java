package com.codeflowx.platform.viewmodel.datasources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.datasources.DataSourceApi;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para gestión de APIs de Data Sources
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class DataSourceApiViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
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
    protected javax.sql.DataSource ds;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((javax.sql.DataSource) environment.getProperty("APPLICATION_DS", javax.sql.DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }
    
    // ========== Datos del formulario ==========
    private String apiName;
    private String apiDescription;
    private String apiEndpoint;
    private String apiMethod = "GET";
    private String apiResponseType = "JSON";
    private Integer apiTimeout = 30;
    private String apiStatus = "ACTIVE";
    private String apiHeaders = "{}";
    private String apiParameters = "{}";
    
    // ========== Autenticación ==========
    private String authType = "NONE";
    private String authToken;
    private String authUsername;
    
    // ========== Test Results ==========
    private boolean testResultVisible = false;
    private String testResultType = "info";
    private String testResultIcon = "info-circle";
    private String testResultMessage;
    private String testResultDetails;
    
    // ========== Lista de APIs ==========
    private List<DataSourceApi> apiSourcesList = new ArrayList<>();
    private PageParams pageParams;
    private PageResult<DataSourceApi> pageResult;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadApiSources();
    }
    
    @Command
    @NotifyChange("*")
    public void loadApiSources() {
        try {
            pageResult = businessService.findAllEntity(
                DataSourceApi.class,
                pageParams,
                new Criterias()
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                apiSourcesList = pageResult.getContent();
            } else {
                apiSourcesList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error al cargar API sources", e);
            Messagebox.show("Error al cargar API sources: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void createApiDataSource() {
        clearForm();
    }
    
    @Command
    @NotifyChange("*")
    public void saveApiDataSource() {
        try {
            // Validaciones
            if (apiName == null || apiName.trim().isEmpty()) {
                Messagebox.show("El nombre de la API es requerido", 
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (apiEndpoint == null || apiEndpoint.trim().isEmpty()) {
                Messagebox.show("El endpoint de la API es requerido", 
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            DataSourceApi apiSource = new DataSourceApi();
            apiSource.setApiname(apiName);
            apiSource.setApidescription(apiDescription);
            apiSource.setApiendpoint(apiEndpoint);
            apiSource.setApimethod(apiMethod);
            apiSource.setApiresponsetype(apiResponseType);
            apiSource.setApitimeout(apiTimeout);
            apiSource.setApistatus(apiStatus);
            apiSource.setApiheaders(apiHeaders);
            apiSource.setApiparameters(apiParameters);
            apiSource.setApicreatedat(new Timestamp(System.currentTimeMillis()));
            
            // Configurar autenticación
            String authConfig = buildAuthenticationConfig();
            apiSource.setApiauthentication(authConfig);
            
            businessService.saveEntity(apiSource);
            
            clearForm();
            loadApiSources();
            
            Messagebox.show("API guardada exitosamente", 
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                
        } catch (Exception e) {
            log.error("Error al guardar API source", e);
            Messagebox.show("Error al guardar API: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void testApiConnection() {
        try {
            // TODO: Implementar integración con leka-server para test de conexión API
            testResultVisible = true;
            testResultType = "warning";
            testResultIcon = "exclamation-triangle";
            testResultMessage = "Funcionalidad de test en desarrollo. Requiere integración con leka-server.";
            testResultDetails = "Endpoint: " + apiEndpoint + "\nMethod: " + apiMethod;
            
            log.info("Test API connection: {} {}", apiMethod, apiEndpoint);
        } catch (Exception e) {
            log.error("Error al testear API", e);
            testResultVisible = true;
            testResultType = "danger";
            testResultIcon = "times-circle";
            testResultMessage = "Error al testear API";
            testResultDetails = e.getMessage();
        }
    }
    
    @Command
    @NotifyChange("*")
    public void previewApiData() {
        // TODO: Implementar integración con leka-server para preview de datos
        Messagebox.show("Funcionalidad de preview en desarrollo. Requiere integración con leka-server.", 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void validateApiConfig() {
        try {
            testResultVisible = true;
            testResultType = "info";
            testResultIcon = "info-circle";
            testResultMessage = "Validando configuración...";
            
            // Validaciones básicas
            List<String> errors = new ArrayList<>();
            
            if (apiName == null || apiName.trim().isEmpty()) {
                errors.add("Nombre de API requerido");
            }
            
            if (apiEndpoint == null || apiEndpoint.trim().isEmpty()) {
                errors.add("Endpoint requerido");
            } else if (!apiEndpoint.startsWith("http")) {
                errors.add("Endpoint debe comenzar con http:// o https://");
            }
            
            if (errors.isEmpty()) {
                testResultType = "success";
                testResultIcon = "check-circle";
                testResultMessage = "Configuración válida";
                testResultDetails = null;
            } else {
                testResultType = "danger";
                testResultIcon = "times-circle";
                testResultMessage = "Errores de validación encontrados";
                testResultDetails = String.join("\n", errors);
            }
        } catch (Exception e) {
            log.error("Error al validar configuración", e);
            testResultType = "danger";
            testResultIcon = "times-circle";
            testResultMessage = "Error al validar";
            testResultDetails = e.getMessage();
        }
    }
    
    @Command
    @NotifyChange("*")
    public void cancelApiConfig() {
        clearForm();
    }
    
    @Command
    @NotifyChange("*")
    public void updateAuthFields() {
        // Se actualiza automáticamente con @NotifyChange("*")
    }
    
    @Command
    @NotifyChange("*")
    public void refreshApiSources() {
        loadApiSources();
    }
    
    @Command
    public void viewApiSource(@BindingParam("api") DataSourceApi apiSource) {
        log.info("Ver API source: {}", apiSource.getApiname());
    }
    
    @Command
    @NotifyChange("*")
    public void editApiSource(@BindingParam("api") DataSourceApi apiSource) {
        apiName = apiSource.getApiname();
        apiDescription = apiSource.getApidescription();
        apiEndpoint = apiSource.getApiendpoint();
        apiMethod = apiSource.getApimethod();
        apiResponseType = apiSource.getApiresponsetype();
        apiTimeout = apiSource.getApitimeout();
        apiStatus = apiSource.getApistatus();
        apiHeaders = apiSource.getApiheaders();
        apiParameters = apiSource.getApiparameters();
    }
    
    @Command
    @NotifyChange("*")
    public void testApiSource(@BindingParam("api") DataSourceApi apiSource) {
        // TODO: Implementar test de API source específica
        Messagebox.show("Testeando API: " + apiSource.getApiname(), 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void deleteApiSource(@BindingParam("api") DataSourceApi apiSource) {
        Messagebox.show("¿Está seguro de eliminar la API: " + apiSource.getApiname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.deleteEntity(apiSource);
                        loadApiSources();
                        Messagebox.show("API eliminada exitosamente", 
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar API source", e);
                        Messagebox.show("Error al eliminar API: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    // ========== Métodos auxiliares ==========
    
    private void clearForm() {
        apiName = null;
        apiDescription = null;
        apiEndpoint = null;
        apiMethod = "GET";
        apiResponseType = "JSON";
        apiTimeout = 30;
        apiStatus = "ACTIVE";
        apiHeaders = "{}";
        apiParameters = "{}";
        authType = "NONE";
        authToken = null;
        authUsername = null;
        testResultVisible = false;
    }
    
    private String buildAuthenticationConfig() {
        // TODO: Construir configuración de autenticación en formato JSON
        return "{}";
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
}
