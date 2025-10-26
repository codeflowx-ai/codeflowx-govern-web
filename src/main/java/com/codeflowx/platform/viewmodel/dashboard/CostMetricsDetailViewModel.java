package com.codeflowx.platform.viewmodel.dashboard;

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
import com.codeflowx.govern.entity.dashboard.CostMetrics;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de CostMetrics
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class CostMetricsDetailViewModel extends MasterPage {
    
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
    private Long idxcostmetric;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private CostMetrics currentCostMetrics;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableDshcosttypes = new ArrayList<>();
    private List<String> availableDshperiods = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxcostmetric = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando CostMetricsDetailViewModel - mode: {}, idxcostmetric: {}", mode, idxcostmetric);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxcostmetric != null) {
            loadItem(idxcostmetric);
        } else {
            log.error("Modo inválido o falta idxcostmetric");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentCostMetrics, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentCostMetrics = new CostMetrics();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadDshcosttypes();
        loadDshperiods();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentCostMetrics = businessService.findById(CostMetrics.class, id);
            
            if (currentCostMetrics == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentCostMetrics.getIdxcostmetric();
        loadDshcosttypes();
        loadDshperiods();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            
            // Auditar carga de registro
            logActivity("CONSULTA", "DSHCOSTMETRICS", id, "Consulta: " + currentCostMetrics.getIdxcostmetric());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentCostMetrics.getIdxcostmetric() == null;
            
            if (isNew) {
                businessService.save(currentCostMetrics);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "DSHCOSTMETRICS", currentCostMetrics.getIdxcostmetric(), 
                    "Creado: " + currentCostMetrics.getIdxcostmetric());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentCostMetrics);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "DSHCOSTMETRICS", currentCostMetrics.getIdxcostmetric(), 
                    "Actualizado: " + currentCostMetrics.getIdxcostmetric());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentCostMetrics.getDshcosttype() == null || currentCostMetrics.getDshcosttype().trim().isEmpty()) {
            errors.append("- Dshcosttype\n");
        }
        if (currentCostMetrics.getDshtotalcost() == null) {
            errors.append("- Dshtotalcost\n");
        }
        if (currentCostMetrics.getDshcreatedat() == null) {
            errors.append("- Dshcreatedat\n");
        }
        if (currentCostMetrics.getDshupdatedat() == null) {
            errors.append("- Dshupdatedat\n");
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
        params.put("dataParam", idxcostmetric);
        params.put("action", Action.LOAD);
        appendPage("plataforma/dashboard/dashboard-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadDshcosttypes() {
        // TODO: Cargar valores desde configuración o BD
        availableDshcosttypes.add("OPTION_1");
        availableDshcosttypes.add("OPTION_2");
        availableDshcosttypes.add("OPTION_3");
    }
    
    private void loadDshperiods() {
        // TODO: Cargar valores desde configuración o BD
        availableDshperiods.add("OPTION_1");
        availableDshperiods.add("OPTION_2");
        availableDshperiods.add("OPTION_3");
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
            currentCostMetrics = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableDshcosttypes != null) {
                availableDshcosttypes.clear();
                availableDshcosttypes = null;
            }
            if (availableDshperiods != null) {
                availableDshperiods.clear();
                availableDshperiods = null;
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
