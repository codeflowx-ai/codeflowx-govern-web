package com.codeflowx.govern.viewmodel.infrastructure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.enartframework.suinsit.Context;
import javax.sql.DataSource;

import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.enartframework.nocode.dao.IEntityLocal;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.infrastructure.CloudResource;
import com.codeflowx.govern.entity.infrastructure.InfrastructureMetric;
import com.codeflowx.govern.entity.infrastructure.InfrastructureCost;
import com.codeflowx.govern.entity.functions.infrastructure.CalculateResourceEfficiency;
import com.codeflowx.govern.entity.procedures.infrastructure.ExecuteHealthCheck;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.Destroy;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de recursos cloud
 * 
 * Responsabilidades:
 * - Creación de nuevos recursos cloud
 * - Edición de recursos existentes
 * - Operaciones de negocio: checkHealthStatus(), calculateEfficiency()
 * - Información descendente: métricas, costos
 * - Estadísticas y contadores específicos del recurso
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class InfrastructureDetailViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
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
    
    // ========== Modo de operación ==========
    private String mode; // "create" o "edit"
    private Long resourceId;
    private boolean editing = false;
    private String pageTitle = "Detalle del Recurso Cloud";
    
    // ========== Datos del recurso ==========
    private CloudResource currentResource;
    
    // ========== Información descendente ==========
    private List<InfrastructureMetric> resourceMetrics = new ArrayList<>();
    private List<InfrastructureCost> resourceCosts = new ArrayList<>();
    
    // ========== Estadísticas específicas del recurso ==========
    private Long totalMetrics = 0L;
    private Long totalCosts = 0L;
    private BigDecimal totalHourlyCost = BigDecimal.ZERO;
    private BigDecimal totalCapacity = BigDecimal.ZERO;
    private String healthStatus = "UNKNOWN";
    private Timestamp lastHealthCheck;
    private BigDecimal efficiencyScore = BigDecimal.ZERO;
    
    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) Executions.getCurrent().getParameter("mode");
        String resourceIdStr = Executions.getCurrent().getParameter("resourceId");
        
        if (resourceIdStr != null) {
            resourceId = Long.parseLong(resourceIdStr);
        }
        
        log.info("Inicializando InfrastructureDetailViewModel - mode: {}, resourceId: {}", mode, resourceId);
        
        if ("create".equals(mode)) {
            initNewResource();
        } else if ("edit".equals(mode) && resourceId != null) {
            loadResource(resourceId);
        } else {
            log.error("Modo inválido o falta resourceId");
            Executions.sendRedirect("/infrastructure/infrastructure-overview.zul");
        }
    }
    
    /**
     * Inicializa un nuevo recurso con valores por defecto
     */
    private void initNewResource() {
        log.debug("Inicializando nuevo recurso cloud");
        currentResource = new CloudResource();
        currentResource.setInfrescreatedat(new Timestamp(System.currentTimeMillis()));
        currentResource.setInfresupdatedat(new Timestamp(System.currentTimeMillis()));
        currentResource.setInfrescreatedby(1L); // TODO: Obtener usuario actual
        currentResource.setInfreshealthstatus("UNKNOWN");
        currentResource.setInfresresourcetype("COMPUTE");
        
        editing = false;
        pageTitle = "Crear Nuevo Recurso Cloud";
    }
    
    /**
     * Carga recurso existente desde BD
     */
    private void loadResource(Long id) {
        try {
            log.debug("Cargando recurso cloud ID={}", id);
            
            currentResource = businessService.findById(CloudResource.class, id);
            
            if (currentResource == null) {
                log.error("Recurso cloud no encontrado: ID={}", id);
                Messagebox.show("Recurso cloud no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/infrastructure/infrastructure-overview.zul");
                return;
            }
            
            log.info("Recurso cloud cargado: {}", currentResource.getInfresname());
            
            editing = true;
            pageTitle = "Editar Recurso: " + currentResource.getInfresname();
            
            // Cargar información descendente
            loadResourceMetrics();
            loadResourceCosts();
            loadResourceStatistics();
            
        } catch (Exception e) {
            log.error("Error al cargar recurso cloud ID={}", id, e);
            Messagebox.show("Error al cargar recurso: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/infrastructure/infrastructure-overview.zul");
        }
    }
    
    /**
     * Carga estadísticas específicas del recurso
     */
    private void loadResourceStatistics() {
        try {
            log.debug("Cargando estadísticas del recurso ID={}", currentResource.getIdxcloudresource());
            
            totalMetrics = (long) resourceMetrics.size();
            totalCosts = (long) resourceCosts.size();
            
            // Sumar costos
            totalHourlyCost = resourceCosts.stream()
                .filter(c -> c.getInfcosthourlyrate() != null)
                .map(InfrastructureCost::getInfcosthourlyrate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Capacidad total
            if (currentResource.getInfrescapacitytotal() != null) {
                totalCapacity = BigDecimal.valueOf(currentResource.getInfrescapacitytotal());
            }
            
            // Health status
            healthStatus = currentResource.getInfreshealthstatus() != null ? 
                currentResource.getInfreshealthstatus() : "UNKNOWN";
            lastHealthCheck = currentResource.getInfreslasthealthcheck();
            
            log.info("Estadísticas cargadas - Métricas: {}, Costos: {}, Health: {}",
                totalMetrics, totalCosts, healthStatus);
                
        } catch (Exception e) {
            log.error("Error al cargar estadísticas del recurso", e);
        }
    }
    
    // ========== Información descendente ==========
    
    @Command
    @NotifyChange({"resourceMetrics", "totalMetrics"})
    public void loadResourceMetrics() {
        try {
            log.debug("Cargando métricas del recurso ID={}", currentResource.getIdxcloudresource());
            
            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("infmetricresourceid", currentResource.getIdxcloudresource());
            
            PageResult<InfrastructureMetric> result = businessService.findAllEntity(
                InfrastructureMetric.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                resourceMetrics = result.getContent();
                totalMetrics = (long) resourceMetrics.size();
                log.info("Cargadas {} métricas", totalMetrics);
            } else {
                resourceMetrics = new ArrayList<>();
                totalMetrics = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas del recurso", e);
            resourceMetrics = new ArrayList<>();
            totalMetrics = 0L;
        }
    }
    
    @Command
    @NotifyChange({"resourceCosts", "totalCosts"})
    public void loadResourceCosts() {
        try {
            log.debug("Cargando costos del recurso ID={}", currentResource.getIdxcloudresource());
            
            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("infcostresourceid", currentResource.getIdxcloudresource());
            
            PageResult<InfrastructureCost> result = businessService.findAllEntity(
                InfrastructureCost.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                resourceCosts = result.getContent();
                totalCosts = (long) resourceCosts.size();
                log.info("Cargados {} registros de costos", totalCosts);
            } else {
                resourceCosts = new ArrayList<>();
                totalCosts = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar costos del recurso", e);
            resourceCosts = new ArrayList<>();
            totalCosts = 0L;
        }
    }
    
    // ========== Comandos CRUD ==========
    
    @Command
    @NotifyChange("*")
    public void saveResource() {
        try {
            log.info("Guardando recurso: {}", currentResource.getInfresname());
            
            // Validaciones de negocio
            if (currentResource.getInfresname() == null || currentResource.getInfresname().trim().isEmpty()) {
                Messagebox.show("El nombre del recurso es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentResource.getInfresresourcetype() == null || currentResource.getInfresresourcetype().trim().isEmpty()) {
                Messagebox.show("El tipo de recurso es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentResource.getIdxcloudresource() == null) {
                businessService.save(currentResource);
                log.info("Recurso creado exitosamente: ID={}, nombre={}",
                    currentResource.getIdxcloudresource(), currentResource.getInfresname());
                Messagebox.show("Recurso creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentResource.setInfresupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.update(currentResource);
                log.info("Recurso actualizado exitosamente: ID={}, nombre={}",
                    currentResource.getIdxcloudresource(), currentResource.getInfresname());
                Messagebox.show("Recurso actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            Executions.sendRedirect("/infrastructure/infrastructure-overview.zul");
            
        } catch (Exception e) {
            log.error("Error al guardar recurso", e);
            Messagebox.show("Error al guardar recurso: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición/creación de recurso, volviendo a overview");
        Executions.sendRedirect("/infrastructure/infrastructure-overview.zul");
    }
    
    // ========== Operaciones especiales (funciones/procedimientos) ==========
    
    @Command
    @NotifyChange({"currentResource", "healthStatus", "lastHealthCheck"})
    public void checkHealthStatus() {
        if (currentResource == null || currentResource.getIdxcloudresource() == null) {
            Messagebox.show("Debe guardar el recurso antes de verificar su estado",
                "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Verificando estado de salud del recurso ID={}", currentResource.getIdxcloudresource());
        try {
            ExecuteHealthCheck procedure = new ExecuteHealthCheck();
            procedure.setPInputParam(currentResource.getIdxcloudresource());
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                // El resultado indica el health status code
                Long resultCode = procedure.getOResult();
                
                // Mapear código a estado
                if (resultCode != null) {
                    if (resultCode == 1L) {
                        healthStatus = "HEALTHY";
                    } else if (resultCode == 2L) {
                        healthStatus = "DEGRADED";
                    } else {
                        healthStatus = "UNHEALTHY";
                    }
                } else {
                    healthStatus = "UNKNOWN";
                }
                
                lastHealthCheck = new Timestamp(System.currentTimeMillis());
                currentResource.setInfreshealthstatus(healthStatus);
                currentResource.setInfreslasthealthcheck(lastHealthCheck);
                
                String icon = "HEALTHY".equals(healthStatus) ? "INFORMATION" : 
                             "DEGRADED".equals(healthStatus) ? "EXCLAMATION" : "ERROR";
                
                Messagebox.show("Verificación de salud completada\n\nEstado: " + healthStatus,
                    "Health Check", Messagebox.OK, icon);
            } else {
                Messagebox.show("No se pudo completar la verificación de salud",
                    "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al verificar estado de salud", e);
            Messagebox.show("Error al verificar estado: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
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
            // Limpiar recurso actual
            currentResource = null;
            
            // Limpiar listas descendentes
            if (resourceMetrics != null) {
                resourceMetrics.clear();
                resourceMetrics = null;
            }
            if (resourceCosts != null) {
                resourceCosts.clear();
                resourceCosts = null;
            }
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }

}

