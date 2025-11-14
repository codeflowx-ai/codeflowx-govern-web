package com.codeflowx.platform.viewmodel.datasources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;

import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para gestión de Web Scraping de Data Sources
 */
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class DataSourceWebscrapingViewModel extends BaseFront<DataSourceWebscrapingViewModel> {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    // ========== Datos del formulario ==========
    private String wsName;
    private String wsDescription;
    private String wsUrl;
    private String wsCronSchedule;
    private String wsStatus = "ACTIVE";
    
    // ========== Selectores CSS ==========
    private String titleSelector;
    private String contentSelector;
    private String linkSelector;
    private String dateSelector;
    
    // ========== Configuración ==========
    private String wsConfiguration = "{}";
    private String wsMetadata = "{}";
    
    // ========== Test Results ==========
    private boolean testResultVisible = false;
    private String testResultType = "info";
    private String testResultIcon = "info-circle";
    private String testResultMessage;
    private String testResultDetails;
    
    // ========== Lista de Web Scraping ==========
    private List<DataSourceWebscraping> webscrapingList = new ArrayList<>();
    private PageParams pageParams;
    private PageResult<DataSourceWebscraping> pageResult;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadWebscrapingSources();
    }
    
    @Command
    @NotifyChange("*")
    public void loadWebscrapingSources() {
        try {
            pageResult = businessService.findAllEntity(
                DataSourceWebscraping.class,
                pageParams,
                new Criterias()
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                webscrapingList = pageResult.getContent();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "DATASOURCEWEBSCRAPINGS", null, 
                    "Búsqueda: " + webscrapingList.size() + " fuentes de web scraping");
            } else {
                webscrapingList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error al cargar web scraping sources", e);
            Messagebox.show("Error al cargar fuentes: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void createWebscrapingDataSource() {
        clearForm();
    }
    
    @Command
    @NotifyChange("*")
    public void saveWebscrapingDataSource() {
        try {
            if (wsName == null || wsName.trim().isEmpty()) {
                Messagebox.show("El nombre es requerido", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (wsUrl == null || wsUrl.trim().isEmpty()) {
                Messagebox.show("La URL es requerida", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            DataSourceWebscraping wsSource = new DataSourceWebscraping();
            wsSource.setWsname(wsName);
            wsSource.setWsdescription(wsDescription);
            wsSource.setWsurl(wsUrl);
            wsSource.setWscronschedule(wsCronSchedule);
            wsSource.setWsstatus(wsStatus);
            wsSource.setWsconfiguration(wsConfiguration);
            wsSource.setWsmetadata(wsMetadata);
            wsSource.setWscreatedat(new Timestamp(System.currentTimeMillis()));
            
            // Construir selectores JSON
            String selectorsJson = buildSelectorsJson();
            wsSource.setWsselectors(selectorsJson);
            
            businessService.save(wsSource);
            
            // Auditar creación
            logActivity("CREAR", "DATASOURCEWEBSCRAPINGS", wsSource.getIdxdatasourcewebscraping(), 
                "Web scraping creado: " + wsSource.getWsname());
            
            clearForm();
            loadWebscrapingSources();
            
            Messagebox.show("Web scraping guardado exitosamente", 
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                
        } catch (Exception e) {
            log.error("Error al guardar web scraping", e);
            Messagebox.show("Error al guardar: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void testWebscraping() {
        try {
            // TODO: Implementar integración con leka-server para test de scraping
            testResultVisible = true;
            testResultType = "warning";
            testResultIcon = "exclamation-triangle";
            testResultMessage = "Funcionalidad de test en desarrollo. Requiere integración con leka-server.";
            testResultDetails = "URL: " + wsUrl;
            
            log.info("Test web scraping: {}", wsUrl);
        } catch (Exception e) {
            log.error("Error al testear web scraping", e);
            testResultVisible = true;
            testResultType = "danger";
            testResultIcon = "times-circle";
            testResultMessage = "Error al testear";
            testResultDetails = e.getMessage();
        }
    }
    
    @Command
    @NotifyChange("*")
    public void previewScrapedData() {
        Messagebox.show("Funcionalidad de preview en desarrollo. Requiere integración con leka-server.", 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void validateSelectors() {
        try {
            testResultVisible = true;
            List<String> errors = new ArrayList<>();
            
            if (wsName == null || wsName.trim().isEmpty()) {
                errors.add("Nombre requerido");
            }
            
            if (wsUrl == null || wsUrl.trim().isEmpty()) {
                errors.add("URL requerida");
            } else if (!wsUrl.startsWith("http")) {
                errors.add("URL debe comenzar con http:// o https://");
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
            log.error("Error al validar", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void runScraping() {
        Messagebox.show("Funcionalidad de ejecución en desarrollo. Requiere integración con leka-server.", 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void cancelWebscrapingConfig() {
        clearForm();
    }
    
    @Command
    @NotifyChange("*")
    public void refreshWebscrapingSources() {
        loadWebscrapingSources();
    }
    
    @Command
    public void viewWebscrapingSource(@BindingParam("ws") DataSourceWebscraping wsSource) {
        log.info("Ver web scraping: {}", wsSource.getWsname());
    }
    
    @Command
    @NotifyChange("*")
    public void editWebscrapingSource(@BindingParam("ws") DataSourceWebscraping wsSource) {
        wsName = wsSource.getWsname();
        wsDescription = wsSource.getWsdescription();
        wsUrl = wsSource.getWsurl();
        wsCronSchedule = wsSource.getWscronschedule();
        wsStatus = wsSource.getWsstatus();
        wsConfiguration = wsSource.getWsconfiguration();
        wsMetadata = wsSource.getWsmetadata();
        // TODO: Parsear selectores desde JSON
    }
    
    @Command
    @NotifyChange("*")
    public void testWebscrapingSource(@BindingParam("ws") DataSourceWebscraping wsSource) {
        Messagebox.show("Testeando web scraping: " + wsSource.getWsname(), 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void deleteWebscrapingSource(@BindingParam("ws") DataSourceWebscraping wsSource) {
        Messagebox.show("¿Está seguro de eliminar el web scraping: " + wsSource.getWsname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(wsSource);
                        
                        // Auditar eliminación
                        logActivity("ELIMINAR", "DATASOURCEWEBSCRAPINGS", wsSource.getIdxdatasourcewebscraping(), 
                            "Web scraping eliminado: " + wsSource.getWsname());
                        
                        loadWebscrapingSources();
                        Messagebox.show("Web scraping eliminado exitosamente", 
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar web scraping", e);
                        Messagebox.show("Error al eliminar: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    // ========== Métodos auxiliares ==========
    
    private void clearForm() {
        wsName = null;
        wsDescription = null;
        wsUrl = null;
        wsCronSchedule = null;
        wsStatus = "ACTIVE";
        titleSelector = null;
        contentSelector = null;
        linkSelector = null;
        dateSelector = null;
        wsConfiguration = "{}";
        wsMetadata = "{}";
        testResultVisible = false;
    }
    
    private String buildSelectorsJson() {
        // TODO: Construir JSON con los selectores
        return "{\"title\": \"" + (titleSelector != null ? titleSelector : "") + 
               "\", \"content\": \"" + (contentSelector != null ? contentSelector : "") + 
               "\", \"link\": \"" + (linkSelector != null ? linkSelector : "") + 
               "\", \"date\": \"" + (dateSelector != null ? dateSelector : "") + "\"}";
    }
    
    public String getWsStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "ACTIVE": return "success";
            case "INACTIVE": return "secondary";
            case "RUNNING": return "primary";
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
        if (webscrapingList != null) { 
            webscrapingList.clear(); 
            webscrapingList = null; 
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
