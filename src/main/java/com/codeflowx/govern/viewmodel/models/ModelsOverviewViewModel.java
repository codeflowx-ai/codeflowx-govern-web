package com.codeflowx.govern.viewmodel.models;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.sql.DataSource;
import org.enartframework.suinsit.Context;
import org.enartframework.core.shared.commons.system.JavaMemManagement;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.nocode.datamodel.model.Entity;
import org.enartframework.orm.exception.DaoException;

import org.enartframework.web.annotation.Action;
import org.enartframework.web.exception.UiException;
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
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.PagingEvent;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.entity.views.models.ModelsMetricsSummary;
import com.codeflowx.govern.entity.views.models.ModelsOverview;
import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para BÚSQUEDA Y LISTADO de modelos AI/ML
 * 
 * Responsabilidades:
 * - Listado paginado con filtros
 * - Búsqueda avanzada (texto, proveedor, estado, tipo, compliance)
 * - Métricas generales del conjunto de modelos
 * - Navegación a pantalla de detalle/edición
 * - Eliminación con confirmación
 * 
 * NO incluye:
 * - Edición/creación de modelos (ver ModelsDetailViewModel)
 * - Operaciones de negocio complejas (ver ModelsDetailViewModel)
 * - Información descendente (ver ModelsDetailViewModel)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ModelsOverviewViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    // ========== Servicios y contexto Spring ==========
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
    
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
        
    }
    
    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<ModelsOverview> pageResult;
    
    // ========== Filtros de búsqueda ==========
    private String searchTerm = "";
    private Long providerFilter = null;
    private String statusFilter = "ALL";
    private String typeFilter = "ALL";
    private String complianceFilter = "ALL";
    
    // ========== Listas de datos ==========
    private List<ModelsOverview> filteredModels = new ArrayList<>();
    private List<ModelProvider> availableProviders = new ArrayList<>();
    
    // ========== Métricas resumen generales ==========
    private int totalModels = 0;
    private long activeModels = 0L;
    private long pendingApproval = 0L;
    private long rejectedModels = 0L;
    private BigDecimal averageAccuracy = BigDecimal.ZERO;
    private BigDecimal averageBiasScore = BigDecimal.ZERO;
    
    // ========== Inicialización ==========
    
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
        
        loadAvailableProviders();
        loadData();
    }
    
    /**
     * Carga proveedores desde BD para combo de filtros
     */
    private void loadAvailableProviders() {
        try {
            log.debug("Cargando proveedores desde MODPROVIDERS");
            
            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("modstatus", "ACTIVE");
            
            PageResult<ModelProvider> result = businessService.findAllEntity(
                ModelProvider.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                availableProviders = result.getContent();
                log.info("Cargados {} proveedores", availableProviders.size());
            } else {
                availableProviders = new ArrayList<>();
                log.warn("No se encontraron proveedores activos");
            }
            
        } catch (Exception e) {
            log.error("Error al cargar proveedores", e);
            availableProviders = new ArrayList<>();
        }
    }
    
    // ========== Carga de datos con filtros ==========
    
    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            log.debug("Cargando modelos - Página: {}", pageParams.getPageActual());
            
            Criterias criterias = buildCriterias();
            
            // Usar la VIEW con Criterias para filtros
            pageResult = businessService.findAllView(
                ModelsOverview.class,
                pageParams,
                criterias
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                filteredModels = pageResult.getContent();
                totalModels = pageResult.getTotalRows();
                
                // Cargar métricas globales desde VIEW
                loadGlobalMetrics();
                
                log.info("Cargados {} modelos de {} totales", 
                    filteredModels.size(), totalModels);
            } else {
                filteredModels = new ArrayList<>();
                totalModels = 0;
            }
            
        } catch (Exception e) {
            log.error("Error al cargar modelos", e);
            Messagebox.show(Labels.getLabel("models.error.load") + ": " + e.getMessage(), 
                Labels.getLabel("models.error.title"), Messagebox.OK, Messagebox.ERROR);
            filteredModels = new ArrayList<>();
        }
    }
    
    /**
     * Construye Criterias para filtros con BusinessService
     */
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        
        // Búsqueda por nombre (LIKE) - El framework agrega % automáticamente
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "modname");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }
        
        // Filtro por proveedor (EQUALS)
        if (providerFilter != null && providerFilter > 0) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "idmodprovider");
            criteria.setValues(new Object[]{providerFilter});
            criterias.addCriteria(criteria);
        }
        
        // Filtro por estado (EQUALS)
        if (!"ALL".equals(statusFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modstatus");
            criteria.setValues(new Object[]{statusFilter});
            criterias.addCriteria(criteria);
        }
        
        // Filtro por tipo (EQUALS)
        if (!"ALL".equals(typeFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modtype");
            criteria.setValues(new Object[]{typeFilter});
            criterias.addCriteria(criteria);
        }
        
        // Filtro por compliance (EQUALS)
        if (!"ALL".equals(complianceFilter)) {
            String approvalStatus = null;
            switch (complianceFilter) {
                case "COMPLIANT":
                    approvalStatus = "APPROVED";
                    break;
                case "NON_COMPLIANT":
                    approvalStatus = "REJECTED";
                    break;
                case "UNDER_REVIEW":
                    approvalStatus = "PENDING_APPROVAL";
                    break;
            }
            
            if (approvalStatus != null) {
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modapprovalstatus");
                criteria.setValues(new Object[]{approvalStatus});
                criterias.addCriteria(criteria);
            }
        }
        
        return criterias;
    }
    
    /**
     * Carga métricas globales desde VIEW (no de la página actual)
     */
    private void loadGlobalMetrics() {
        try {
            log.debug("Cargando métricas globales desde V_MODELS_METRICS_SUMMARY");
            
            List<ModelsMetricsSummary> metrics = businessService.findAllView(ModelsMetricsSummary.class);
            
            if (metrics != null && !metrics.isEmpty()) {
                ModelsMetricsSummary summary = metrics.get(0);
                
                // Usar valores de la vista
                activeModels = summary.getActiveModels() != null ? summary.getActiveModels() : 0L;
                pendingApproval = summary.getPendingApproval() != null ? summary.getPendingApproval() : 0L;
                rejectedModels = summary.getRejectedModels() != null ? summary.getRejectedModels() : 0L;
                averageAccuracy = summary.getAverageAccuracy() != null ? summary.getAverageAccuracy() : BigDecimal.ZERO;
                averageBiasScore = summary.getAverageBiasScore() != null ? summary.getAverageBiasScore() : BigDecimal.ZERO;
                
                log.info("Métricas globales cargadas - Total: {}, Activos: {}, Pendientes: {}", 
                    totalModels, activeModels, pendingApproval);
            } else {
                log.warn("No se pudieron cargar métricas globales");
                // Valores por defecto
                activeModels = 0L;
                pendingApproval = 0L;
                rejectedModels = 0L;
                averageAccuracy = BigDecimal.ZERO;
                averageBiasScore = BigDecimal.ZERO;
            }
            
        } catch (Exception e) {
            log.error("Error al cargar métricas globales", e);
            // Valores por defecto en caso de error
            activeModels = 0L;
            pendingApproval = 0L;
            rejectedModels = 0L;
            averageAccuracy = BigDecimal.ZERO;
            averageBiasScore = BigDecimal.ZERO;
        }
    }
    
    // ========== Comandos de búsqueda y filtros ==========
    
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        log.debug("Aplicando filtros - searchTerm: {}, provider: {}, status: {}", 
            searchTerm, providerFilter, statusFilter);
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void clearFilters() {
        log.debug("Limpiando filtros");
        searchTerm = "";
        providerFilter = null;
        statusFilter = "ALL";
        typeFilter = "ALL";
        complianceFilter = "ALL";
        pageParams.setPageActual(1);
        loadData();
    }
    
    // ========== Paginación ==========
    
   
    
    /**
     * Maneja el evento de paginación del componente ZK Paging
     */
    @Command
    @NotifyChange("*")
    public void onPaging(@BindingParam("event") PagingEvent event) {
        int pageIndex = event.getActivePage();
        pageParams.setPageActual(pageIndex + 1); // ZK usa 0-based, nosotros 1-based
        pageParams.setRowActual(pageIndex * pageParams.getMaxRows());
        loadData();
    }

    
    // ========== Navegación ==========
    
    @Command
    public void registerModel() {
        log.info("Navegando a creación de nuevo modelo");
        Map<String, Object> params = new HashMap<>();
        params.put("action", Action.CREATE);
        appendPage("gobierno/models/models-detail.zul", page.getFellow(IDDESKTOP), params);
     }
    
    @Command
    public void viewModelDetails(@BindingParam("modelId") Long modelId) {
        log.info("Navegando a detalle de modelo ID={}", modelId);
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", modelId);
        params.put("action", Action.LOAD);
        appendPage("gobierno/models/models-detail.zul", page.getFellow(IDDESKTOP), params);
     }
    
    // ========== Eliminación ==========
    
    @Command
    @NotifyChange("*")
    public void deleteModel(@BindingParam("modelId") Long modelId) {
        try {
            Messagebox.show(
                Labels.getLabel("models.confirm.delete.message"), 
                Labels.getLabel("models.confirm.title"), 
                Messagebox.YES | Messagebox.NO, 
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            businessService.removeFromID(Model.class, modelId);
                            log.info("Modelo eliminado: ID={}", modelId);
                            loadData();
                            Messagebox.show(Labels.getLabel("models.success.deleted"), 
                                Labels.getLabel("models.success.title"), Messagebox.OK, Messagebox.INFORMATION);
                        } catch (Exception e) {
                            log.error("Error al eliminar modelo ID={}", modelId, e);
                            Messagebox.show(Labels.getLabel("models.error.delete") + ": " + e.getMessage(), 
                                Labels.getLabel("models.error.title"), Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                }
            );
        } catch (Exception e) {
            log.error("Error en diálogo de eliminación", e);
        }
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
    	Ssoractividad log = new Ssoractividad();
    	log.setUsername(getUser().getUsername());
    	log.setAccion(action);
    	log.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
    	log.setModulo(model);
    	log.setIdtupla(pk.intValue());
    	log.setAplicacion(ctxBean.getApplicationName());
    	log.setValuetupla(mensaje);
    	businessService.save(log);
 	}
    
    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar lista filtrada
            if (filteredModels != null) {
                filteredModels.clear();
                filteredModels = null;
            }
            
            // Limpiar lista de providers
            if (availableProviders != null) {
                availableProviders.clear();
                availableProviders = null;
            }
            
            // Limpiar PageResult
            if (pageResult != null) {
                if (pageResult.getContent() != null) {
                    pageResult.getContent().clear();
                }
                pageResult = null;
            }
            
            // Limpiar PageParams
            pageParams = null;
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
