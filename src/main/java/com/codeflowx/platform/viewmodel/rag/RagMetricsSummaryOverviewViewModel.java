package com.codeflowx.platform.viewmodel.rag;

import java.math.BigDecimal;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.PagingEvent;
import com.codeflowx.govern.entity.views.rag.RagMetricsSummary;
import com.codeflowx.admin.Ssoractividad;
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
 * ViewModel para búsqueda y listado de RagMetricsSummary
 * Patrón: Overview (solo búsqueda/listado/navegación)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RagMetricsSummaryOverviewViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    
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
    
    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<RagMetricsSummary> pageResult;
    
    // ========== Filtros ==========
    private String searchTerm = "";
    private String statusFilter = "ALL";
    
    // ========== Datos ==========
    private List<RagMetricsSummary> filteredItems = new ArrayList<>();
    
    // ========== Métricas globales ==========
    private int totalItems = 0;
    private long activeItems = 0L;
    private long pendingApproval = 0L;
    
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
        
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            log.debug("Cargando datos - Página: {}", pageParams.getPageActual());
            
            Criterias criterias = buildCriterias();
            
            pageResult = businessService.findAllView(
                RagMetricsSummary.class,
                pageParams,
                criterias
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                filteredItems = pageResult.getContent();
                totalItems = pageResult.getTotalRows();
                
                loadGlobalMetrics();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "V_RAG_METRICS_SUMMARY", null, 
                    "Búsqueda: " + filteredItems.size() + " resultados (término: '" + searchTerm + "')");
                
                log.info("Cargados {} items de {} totales", 
                    filteredItems.size(), totalItems);
            } else {
                filteredItems = new ArrayList<>();
                totalItems = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar datos", e);
            Messagebox.show("Error al cargar datos: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
            filteredItems = new ArrayList<>();
        }
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "id");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }
        
        if (!"ALL".equals(statusFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "status");
            criteria.setValues(new Object[]{statusFilter});
            criterias.addCriteria(criteria);
        }
        
        return criterias;
    }
    
    private void loadGlobalMetrics() {
        try {
            log.debug("Cargando métricas globales");
            // TODO: Implementar carga de métricas desde vistas SQL cuando estén disponibles
            // Ejemplo: List<RagMetricsSummaryMetricsSummary> metrics = businessService.findAllView(RagMetricsSummaryMetricsSummary.class);
            log.debug("Métricas globales pendientes de implementación");
        } catch (Exception e) {
            log.error("Error al cargar métricas globales", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        log.debug("Aplicando filtros");
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void clearFilters() {
        log.debug("Limpiando filtros");
        searchTerm = "";
        statusFilter = "ALL";
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void onPaging(@BindingParam("event") PagingEvent event) {
        int pageIndex = event.getActivePage();
        pageParams.setPageActual(pageIndex + 1);
        pageParams.setRowActual(pageIndex * pageParams.getMaxRows());
        loadData();
    }
    
    @Command
    public void registerItem() {
        log.info("Navegando a creación");
        Map<String, Object> params = new HashMap<>();
        params.put("action", Action.NEW);
        appendPage("plataforma/rag/rag-detail.zul", page.getFellow(IDDESKTOP), params);
    }
    
    @Command
    public void viewItemDetails(@BindingParam("itemId") Long itemId) {
        log.info("Navegando a detalle ID={}", itemId);
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", itemId);
        params.put("action", Action.LOAD);
        appendPage("plataforma/rag/rag-detail.zul", page.getFellow(IDDESKTOP), params);
    }
    
    @Command
    @NotifyChange("*")
    public void deleteItem(@BindingParam("itemId") Long itemId) {
        try {
            Messagebox.show("¿Está seguro de eliminar este registro?", 
                "Confirmar eliminación", 
                Messagebox.YES | Messagebox.NO, 
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            businessService.removeFromID(RagMetricsSummary.class, itemId);
                            log.info("Registro eliminado: ID={}", itemId);
                            logActivity("BORRAR", "V_RAG_METRICS_SUMMARY", itemId, "Eliminado registro ID: " + itemId);
                            loadData();
                            Messagebox.show("Registro eliminado correctamente", 
                                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                        } catch (Exception e) {
                            log.error("Error al eliminar ID={}", itemId, e);
                            Messagebox.show("Error al eliminar: " + e.getMessage(), 
                                "Error", Messagebox.OK, Messagebox.ERROR);
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
            // Limpiar lista filtrada
            if (filteredItems != null) {
                filteredItems.clear();
                filteredItems = null;
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
