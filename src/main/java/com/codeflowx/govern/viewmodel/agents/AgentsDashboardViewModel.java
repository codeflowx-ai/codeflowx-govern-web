package com.codeflowx.govern.viewmodel.agents;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.views.agents.AgentComplianceStatus;
import com.codeflowx.govern.entity.views.agents.AgentDeploymentStatus;
import com.codeflowx.govern.entity.views.agents.AgentHealthDashboard;
import com.codeflowx.govern.entity.views.agents.AgentPerformanceMetrics;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para el Dashboard de Agentes
 * Gestiona la visualización de métricas, estado de salud y rendimiento de agentes
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AgentsDashboardViewModel extends BaseFront<AgentsDashboardViewModel> {

    private static final long serialVersionUID = 1L;
    
    
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;
    
    // ========== Datos del Dashboard ==========
    private AgentHealthDashboard healthDashboard;
    private List<AgentPerformanceMetrics> performanceMetrics = new ArrayList<>();
    private List<AgentDeploymentStatus> deploymentStatuses = new ArrayList<>();
    private List<AgentComplianceStatus> complianceStatuses = new ArrayList<>();
    private List<Agent> recentAgents = new ArrayList<>();
    
    // ========== KPIs ==========
    private Long totalAgents = 0L;
    private Long activeAgents = 0L;
    private Long deployedAgents = 0L;
    private Long offlineAgents = 0L;
    private BigDecimal avgPerformanceScore = BigDecimal.ZERO;
    private Integer healthScore = 100;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();
        
        log.info("Inicializando AgentsDashboardViewModel");
        
        loadHealthDashboard();
        loadPerformanceMetrics();
        loadDeploymentStatuses();
        loadComplianceStatuses();
        loadRecentAgents();
        calculateKPIs();
    }
    
    /**
     * Inicializa los parámetros de paginación
     */
    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("agtcreatedat")
                .build();
    }

    /**
     * Inicializa el BusinessService
     */

    // ========== Carga de Datos ==========
    
    /**
     * Carga el dashboard de salud de agentes
     */
    private void loadHealthDashboard() {
        try {
            log.debug("Cargando dashboard de salud de agentes");
            
            // VIEW - usar findAllView()
            PageParams pageParams = PageParams.builder()
                .maxRows(1)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<AgentHealthDashboard> result = businessService.findAllView(
                AgentHealthDashboard.class,
                pageParams,
                new Criterias()
            );
            
            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                healthDashboard = result.getContent().get(0);
                log.info("Dashboard de salud cargado correctamente");
            } else {
                log.warn("No se encontraron datos del dashboard de salud");
            }
        } catch (Exception e) {
            log.error("Error al cargar dashboard de salud", e);
        }
    }
    
    /**
     * Carga métricas de rendimiento de agentes
     */
    private void loadPerformanceMetrics() {
        try {
            log.debug("Cargando métricas de rendimiento");
            
            // VIEW - usar findAllView()
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<AgentPerformanceMetrics> result = businessService.findAllView(
                AgentPerformanceMetrics.class,
                pageParams,
                new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                performanceMetrics = result.getContent();
                log.info("Cargadas {} métricas de rendimiento", performanceMetrics.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas de rendimiento", e);
        }
    }
    
    /**
     * Carga estados de deployment de agentes
     */
    private void loadDeploymentStatuses() {
        try {
            log.debug("Cargando estados de deployment");
            
            // VIEW - usar findAllView()
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<AgentDeploymentStatus> result = businessService.findAllView(
                AgentDeploymentStatus.class,
                pageParams,
                new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                deploymentStatuses = result.getContent();
                log.info("Cargados {} estados de deployment", deploymentStatuses.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar estados de deployment", e);
        }
    }
    
    /**
     * Carga estados de compliance de agentes
     */
    private void loadComplianceStatuses() {
        try {
            log.debug("Cargando estados de compliance");
            
            // VIEW - usar findAllView()
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<AgentComplianceStatus> result = businessService.findAllView(
                AgentComplianceStatus.class,
                pageParams,
                new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                complianceStatuses = result.getContent();
                log.info("Cargados {} estados de compliance", complianceStatuses.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar estados de compliance", e);
        }
    }
    
    /**
     * Carga agentes recientes
     */
    private void loadRecentAgents() {
        try {
            log.debug("Cargando agentes recientes");
            
            // TABLE - usar findAllEntity()
            PageParams pageParams = PageParams.builder()
                .maxRows(10)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<Agent> result = businessService.findAllEntity(
                Agent.class,
                pageParams,
                new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                recentAgents = result.getContent();
                log.info("Cargados {} agentes recientes", recentAgents.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar agentes recientes", e);
        }
    }
    
    /**
     * Calcula KPIs del dashboard
     */
    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs de agentes");
            
            if (healthDashboard != null) {
                totalAgents = healthDashboard.getTotalItems() != null ? healthDashboard.getTotalItems() : 0L;
            }
            
            // Calcular agentes activos, deployados y offline desde las métricas
            activeAgents = recentAgents.stream()
                .filter(a -> "ACTIVE".equals(a.getAgtstatus()))
                .count();
            
            deployedAgents = (long) deploymentStatuses.size();
            
            offlineAgents = recentAgents.stream()
                .filter(a -> "OFFLINE".equals(a.getAgtstatus()))
                .count();
            
            // Calcular score promedio de rendimiento
            if (!performanceMetrics.isEmpty()) {
                long count = performanceMetrics.stream()
                    .filter(m -> m.getAvgScore() != null && !m.getAvgScore().isEmpty())
                    .count();
                
                if (count > 0) {
                    BigDecimal sum = performanceMetrics.stream()
                        .filter(m -> m.getAvgScore() != null && !m.getAvgScore().isEmpty())
                        .map(m -> {
                            try {
                                return new BigDecimal(m.getAvgScore());
                            } catch (NumberFormatException e) {
                                return BigDecimal.ZERO;
                            }
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    avgPerformanceScore = sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
                }
            }
            
            // Health score simplificado
            if (totalAgents > 0) {
                healthScore = (int) ((activeAgents * 100.0) / totalAgents);
            }
            
            log.info("KPIs calculados - Total: {}, Activos: {}, Desplegados: {}", 
                totalAgents, activeAgents, deployedAgents);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    // ========== Comandos ==========
    
    @Command
    @NotifyChange("*")
    public void refreshDashboard() {
        log.info("Refrescando dashboard de agentes");
        try {
            loadHealthDashboard();
            loadPerformanceMetrics();
            loadDeploymentStatuses();
            loadComplianceStatuses();
            loadRecentAgents();
            calculateKPIs();
            
            Messagebox.show("Dashboard actualizado correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al refrescar dashboard", e);
            Messagebox.show("Error al refrescar dashboard: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void viewAgentDetail(@org.zkoss.bind.annotation.BindingParam("agentId") Long agentId) {
        log.info("Navegando a detalle de agente: {}", agentId);
        try {
            Executions.sendRedirect("/agents/create/page.zul?id=" + agentId);
        } catch (Exception e) {
            log.error("Error al navegar a detalle", e);
        }
    }

    // ========== Getters ==========
    
    public AgentHealthDashboard getHealthDashboard() {
        return healthDashboard;
    }

    public List<AgentPerformanceMetrics> getPerformanceMetrics() {
        return performanceMetrics;
    }

    public List<AgentDeploymentStatus> getDeploymentStatuses() {
        return deploymentStatuses;
    }

    public List<AgentComplianceStatus> getComplianceStatuses() {
        return complianceStatuses;
    }

    public List<Agent> getRecentAgents() {
        return recentAgents;
    }

    public Long getTotalAgents() {
        return totalAgents;
    }

    public Long getActiveAgents() {
        return activeAgents;
    }

    public Long getDeployedAgents() {
        return deployedAgents;
    }

    public Long getOfflineAgents() {
        return offlineAgents;
    }

    public BigDecimal getAvgPerformanceScore() {
        return avgPerformanceScore;
    }

    public Integer getHealthScore() {
        return healthScore;
    }

   
    
    // ========== Limpieza ==========
    
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            // Limpiar datos del dashboard
            healthDashboard = null;
            
            if (performanceMetrics != null) {
                performanceMetrics.clear();
                performanceMetrics = null;
            }
            
            if (deploymentStatuses != null) {
                deploymentStatuses.clear();
                deploymentStatuses = null;
            }
            
            if (complianceStatuses != null) {
                complianceStatuses.clear();
                complianceStatuses = null;
            }
            
            if (recentAgents != null) {
                recentAgents.clear();
                recentAgents = null;
            }
            
            // Limpiar parámetros
            pageParams = null;
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}

