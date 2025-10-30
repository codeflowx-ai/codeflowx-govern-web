package com.codeflowx.platform.viewmodel.analytics;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.enartframework.suinsit.Context;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.analytics.AnalyticsReport;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import org.enartframework.orm.exception.DaoException;
import org.zkoss.zk.ui.UiException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AnalyticsReport
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AnalyticsReportDetailViewModel extends MasterPage {
    
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
    
    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }
    
    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    
    // ========== Modo de operación ==========
    private String mode;
    private Long idxanalyticsreport;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AnalyticsReport currentAnalyticsReport;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAnlreportname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAnlreporttypes = new ArrayList<>();
    private List<String> availableAnlstatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación - con protección para action null

        
        if (super.action != null) {

        
            mode = super.action.name();

        
        } else {

        
            // Si action es null, intentar determinar el modo por el contexto

        
            mode = (dataParam != null) ? "LOAD" : "NEW";

        
            log.warn("Action es null, infiriendo modo: {}", mode);

        
        }
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxanalyticsreport = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AnalyticsReportDetailViewModel - mode: {}, idxanalyticsreport: {}", mode, idxanalyticsreport);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxanalyticsreport != null) {
            loadItem(idxanalyticsreport);
        } else {
            log.error("Modo inválido o falta idxanalyticsreport");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("analytics/overview/page.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAnalyticsReport, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAnalyticsReport = new AnalyticsReport();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAnlreporttypes();
        loadAnlstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAnalyticsReport = businessService.findById(AnalyticsReport.class, id);
            
            if (currentAnalyticsReport == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/analytics/analytics-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAnalyticsReport.getAnlreportname();
        loadAnlreporttypes();
        loadAnlstatuss();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalAnlreportname = currentAnalyticsReport.getAnlreportname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "ANLANALYTICSREPORTS", id, "Consulta: " + currentAnalyticsReport.getAnlreportname());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/analytics/analytics-overview.zul", page.getFellow(IDDESKTOP), params);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void saveItem() {
        try {
            log.info("Guardando registro");
            
            // Validar campos obligatorios
            if (!validateRequiredFields()) {
                return;
            }
            
            boolean isNew = currentAnalyticsReport.getIdxanalyticsreport() == null;
            
            if (isNew) {
                businessService.save(currentAnalyticsReport);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "ANLANALYTICSREPORTS", currentAnalyticsReport.getIdxanalyticsreport(), 
                    "Creado: " + currentAnalyticsReport.getAnlreportname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentAnalyticsReport);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "ANLANALYTICSREPORTS", currentAnalyticsReport.getIdxanalyticsreport(), 
                    "Actualizado: " + currentAnalyticsReport.getAnlreportname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/analytics/analytics-overview.zul", page.getFellow(IDDESKTOP), params);
            
        } catch (Exception e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error al guardar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Valida que todos los campos obligatorios estén completos
     * @return true si la validación es exitosa
     */
    private boolean validateRequiredFields() {
        StringBuilder errors = new StringBuilder();
        
        if (currentAnalyticsReport.getAnlreportname() == null || currentAnalyticsReport.getAnlreportname().trim().isEmpty()) {
            errors.append("- Anlreportname\n");
        }
        if (currentAnalyticsReport.getAnlreportname() != null && currentAnalyticsReport.getAnlreportname().length() > 255) {
            errors.append("- Anlreportname no puede exceder 255 caracteres\n");
        }
        if (currentAnalyticsReport.getAnlreporttype() == null || currentAnalyticsReport.getAnlreporttype().trim().isEmpty()) {
            errors.append("- Anlreporttype\n");
        }
        if (currentAnalyticsReport.getAnlstatus() == null || currentAnalyticsReport.getAnlstatus().trim().isEmpty()) {
            errors.append("- Anlstatus\n");
        }
        if (currentAnalyticsReport.getAnlcreatedby() == null || currentAnalyticsReport.getAnlcreatedby().trim().isEmpty()) {
            errors.append("- Anlcreatedby\n");
        }
        if (currentAnalyticsReport.getAnlcreatedby() != null && currentAnalyticsReport.getAnlcreatedby().length() > 255) {
            errors.append("- Anlcreatedby no puede exceder 255 caracteres\n");
        }
        if (currentAnalyticsReport.getAnlcreatedat() == null) {
            errors.append("- Anlcreatedat\n");
        }
        
        if (errors.length() > 0) {
            Messagebox.show("Por favor complete los siguientes campos:\n" + errors.toString(),
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        return true;
    }
    
    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición");
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", idxanalyticsreport);
        params.put("action", Action.LOAD);
        appendPage("plataforma/analytics/analytics-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAnlreporttypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAnlreporttypes.add("OPTION_1");
        availableAnlreporttypes.add("OPTION_2");
        availableAnlreporttypes.add("OPTION_3");
    }
    
    private void loadAnlstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAnlstatuss.add("OPTION_1");
        availableAnlstatuss.add("OPTION_2");
        availableAnlstatuss.add("OPTION_3");
    }
    
    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelo XXXX
     * @throws DaoException
     * @throws UiException
     */
    private void logActivity(String action, String model, Long pk, String mensaje) throws DaoException, UiException {
        try {
            Ssoractividad log = new Ssoractividad();
            log.setUsername(getUser().getUsername());
            log.setAccion(action);
            log.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            log.setModulo(model);
            log.setIdtupla(pk != null ? pk.intValue() : 0);
            log.setAplicacion(ctxBean.getApplicationName());
            log.setValuetupla(mensaje);
            businessService.save(log);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
            // No lanzar excepción para que no interrumpa el flujo normal
        }
    }
    
    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar entidad actual
            currentAnalyticsReport = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAnlreporttypes != null) {
                availableAnlreporttypes.clear();
                availableAnlreporttypes = null;
            }
            if (availableAnlstatuss != null) {
                availableAnlstatuss.clear();
                availableAnlstatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            
            // Limpiar tags/roles JSONB
            
            // Limpiar validadores
            unique = null;
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
