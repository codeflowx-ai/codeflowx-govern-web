package com.codeflowx.platform.viewmodel.datasources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.enartframework.suinsit.Context;
import org.enartframework.nocode.dao.IEntityLocal;
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
import com.codeflowx.govern.entity.datasources.DataSourceDatabase;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para gestión de Bases de Datos de Data Sources
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class DataSourceDatabaseViewModel extends MasterPage {

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
    public void setBeans(Object bean) {}
    
    // ========== Datos del formulario ==========
    private String dbName;
    private String dbDescription;
    private String dbType = "MYSQL";
    private String dbHost;
    private Integer dbPort;
    private String dbUsername;
    private String dbPassword;
    private String dbSchema;
    private String dbConnectionString;
    private String dbConfiguration = "{}";
    private String dbStatus = "ACTIVE";
    
    // ========== Test Results ==========
    private boolean testResultVisible = false;
    private String testResultType = "info";
    private String testResultIcon = "info-circle";
    private String testResultMessage;
    private String testResultDetails;
    
    // ========== Lista de Bases de Datos ==========
    private List<DataSourceDatabase> dbSourcesList = new ArrayList<>();
    private PageParams pageParams;
    private PageResult<DataSourceDatabase> pageResult;
    
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
        
        loadDbSources();
    }
    
    @Command
    @NotifyChange("*")
    public void loadDbSources() {
        try {
            pageResult = businessService.findAllEntity(
                DataSourceDatabase.class,
                pageParams,
                new Criterias()
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                dbSourcesList = pageResult.getContent();
            } else {
                dbSourcesList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error al cargar DB sources", e);
            Messagebox.show("Error al cargar bases de datos: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void createDatabaseDataSource() {
        clearForm();
    }
    
    @Command
    @NotifyChange("*")
    public void saveDatabaseDataSource() {
        try {
            if (dbName == null || dbName.trim().isEmpty()) {
                Messagebox.show("El nombre de la base de datos es requerido", 
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            DataSourceDatabase dbSource = new DataSourceDatabase();
            dbSource.setDbname(dbName);
            dbSource.setDbdescription(dbDescription);
            dbSource.setDbtype(dbType);
            dbSource.setDbhost(dbHost);
            dbSource.setDbport(dbPort);
            dbSource.setDbusername(dbUsername);
            dbSource.setDbpassword(dbPassword);
            dbSource.setDbschema(dbSchema);
            dbSource.setDbconnectionstring(dbConnectionString);
            dbSource.setDbconfiguration(dbConfiguration);
            dbSource.setDbstatus(dbStatus);
            dbSource.setDbcreatedat(new Timestamp(System.currentTimeMillis()));
            
            businessService.saveEntity(dbSource);
            
            clearForm();
            loadDbSources();
            
            Messagebox.show("Base de datos guardada exitosamente", 
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                
        } catch (Exception e) {
            log.error("Error al guardar DB source", e);
            Messagebox.show("Error al guardar base de datos: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void testDbConnection() {
        try {
            // TODO: Implementar integración con leka-server para test de conexión DB
            testResultVisible = true;
            testResultType = "warning";
            testResultIcon = "exclamation-triangle";
            testResultMessage = "Funcionalidad de test en desarrollo. Requiere integración con leka-server.";
            testResultDetails = "Host: " + dbHost + ":" + dbPort + "\nDatabase: " + dbSchema;
            
            log.info("Test DB connection: {}:{}", dbHost, dbPort);
        } catch (Exception e) {
            log.error("Error al testear DB", e);
            testResultVisible = true;
            testResultType = "danger";
            testResultIcon = "times-circle";
            testResultMessage = "Error al testear base de datos";
            testResultDetails = e.getMessage();
        }
    }
    
    @Command
    @NotifyChange("*")
    public void listDbTables() {
        // TODO: Implementar integración con leka-server para listar tablas
        Messagebox.show("Funcionalidad de listado de tablas en desarrollo. Requiere integración con leka-server.", 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void validateDbConfig() {
        try {
            testResultVisible = true;
            List<String> errors = new ArrayList<>();
            
            if (dbName == null || dbName.trim().isEmpty()) {
                errors.add("Nombre de base de datos requerido");
            }
            
            if (dbHost == null || dbHost.trim().isEmpty()) {
                errors.add("Host requerido");
            }
            
            if (dbPort == null || dbPort <= 0) {
                errors.add("Puerto inválido");
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
        }
    }
    
    @Command
    @NotifyChange("*")
    public void cancelDbConfig() {
        clearForm();
    }
    
    @Command
    @NotifyChange("*")
    public void updateDbFields() {
        // Se actualiza automáticamente
    }
    
    @Command
    @NotifyChange("*")
    public void refreshDbSources() {
        loadDbSources();
    }
    
    @Command
    public void viewDbSource(@BindingParam("db") DataSourceDatabase dbSource) {
        log.info("Ver DB source: {}", dbSource.getDbname());
    }
    
    @Command
    @NotifyChange("*")
    public void editDbSource(@BindingParam("db") DataSourceDatabase dbSource) {
        dbName = dbSource.getDbname();
        dbDescription = dbSource.getDbdescription();
        dbType = dbSource.getDbtype();
        dbHost = dbSource.getDbhost();
        dbPort = dbSource.getDbport();
        dbUsername = dbSource.getDbusername();
        dbPassword = dbSource.getDbpassword();
        dbSchema = dbSource.getDbschema();
        dbConnectionString = dbSource.getDbconnectionstring();
        dbConfiguration = dbSource.getDbconfiguration();
        dbStatus = dbSource.getDbstatus();
    }
    
    @Command
    @NotifyChange("*")
    public void testDbSource(@BindingParam("db") DataSourceDatabase dbSource) {
        Messagebox.show("Testeando base de datos: " + dbSource.getDbname(), 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void deleteDbSource(@BindingParam("db") DataSourceDatabase dbSource) {
        Messagebox.show("¿Está seguro de eliminar la base de datos: " + dbSource.getDbname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.deleteEntity(dbSource);
                        loadDbSources();
                        Messagebox.show("Base de datos eliminada exitosamente", 
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar DB source", e);
                        Messagebox.show("Error al eliminar base de datos: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    private void clearForm() {
        dbName = null;
        dbDescription = null;
        dbType = "MYSQL";
        dbHost = null;
        dbPort = null;
        dbUsername = null;
        dbPassword = null;
        dbSchema = null;
        dbConnectionString = null;
        dbConfiguration = "{}";
        dbStatus = "ACTIVE";
        testResultVisible = false;
    }
    
    public String getDbStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "ACTIVE": return "success";
            case "INACTIVE": return "secondary";
            case "ERROR": return "danger";
            case "CONNECTING": return "warning";
            default: return "secondary";
        }
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
}
