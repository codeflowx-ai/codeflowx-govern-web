package com.codeflowx.govern.viewmodel.agents;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
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

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.service.agents.AgentService;
import com.codeflowx.govern.entity.agents.AgentCollaboration;
import com.codeflowx.govern.entity.agents.AgentDecision;
import com.codeflowx.govern.entity.agents.AgentDeployment;
import com.codeflowx.govern.entity.agents.AgentHealth;
import com.codeflowx.govern.entity.agents.AgentMonitoring;
import com.codeflowx.govern.entity.agents.AgentTool;
import com.codeflowx.govern.entity.agents.AgentVersion;
import com.codeflowx.govern.entity.agents.AgentWorkflow;
import com.codeflowx.govern.entity.views.agents.AgentComplianceStatus;
import com.codeflowx.govern.entity.views.agents.AgentDeploymentStatus;
import com.codeflowx.govern.entity.views.agents.AgentHealthDashboard;
import com.codeflowx.govern.entity.views.agents.AgentPerformanceMetrics;
import com.codeflowx.govern.service.agents.AgentDecisionService;
import com.codeflowx.govern.service.agents.AgentCollaborationService;
import com.codeflowx.govern.service.agents.AgentMonitoringService;
import com.codeflowx.govern.service.agents.AgentHealthService;
import com.codeflowx.govern.service.agents.AgentWorkflowService;
import com.codeflowx.govern.service.agents.AgentToolService;
import com.codeflowx.govern.service.agents.AgentDeploymentService;
import com.codeflowx.govern.service.agents.AgentVersionService;
import com.codeflowx.govern.service.agents.AgentPerformanceMetricsService;
import com.codeflowx.govern.service.agents.AgentComplianceStatusService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

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
 * ViewModel complejo para DETALLE/EDICIÓN/CREACIÓN de Agentes
 *
 * Funcionalidad:
 * - CRUD completo de agentes
 * - Deploy/undeploy de agentes
 * - Health checks en vivo
 * - Testing de capabilities
 * - Gestión de workflows
 * - Monitoreo de performance
 * - Auditoría de decisiones
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AgentsDetailViewModel extends BaseFront<AgentsDetailViewModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private AgentService agentService;

    @WireVariable
    private AgentPerformanceMetricsService agentPerformanceMetricsService;

    @WireVariable
    private AgentComplianceStatusService agentComplianceStatusService;

    // ========== Entidad Principal ==========
    private Agent currentAgent;
    private boolean isNewAgent = false;
    private boolean isEditMode = false;

    // ========== Información Descendente (Tabs) ==========
    private List<AgentVersion> agentVersions = new ArrayList<>();
    private List<AgentDeployment> agentDeployments = new ArrayList<>();
    private List<AgentTool> agentTools = new ArrayList<>();
    private List<AgentWorkflow> agentWorkflows = new ArrayList<>();
    private List<AgentHealth> healthChecks = new ArrayList<>();
    private List<AgentMonitoring> monitoringData = new ArrayList<>();
    private List<AgentCollaboration> collaborations = new ArrayList<>();
    private List<AgentDecision> recentDecisions = new ArrayList<>();

    // ========== Métricas y Análisis ==========
    private AgentPerformanceMetrics performanceMetrics;
    private AgentComplianceStatus complianceStatus;

    // ========== KPIs del Agente ==========
    private Integer totalExecutions = 0;
    private Integer successfulExecutions = 0;
    private Integer failedExecutions = 0;
    private Double averageExecutionTime = 0.0;
    private String currentHealthStatus = "UNKNOWN";
    private Timestamp lastHealthCheck;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("Inicializando AgentsDetailViewModel");

        // Obtener ID desde parámetros
        Map<String, Object> params = (Map<String, Object>) Executions.getCurrent().getArg();
        String idParam = Executions.getCurrent().getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                Long agentId = Long.parseLong(idParam);
                loadAgent(agentId);
            } catch (NumberFormatException e) {
                log.error("ID de agente inválido: {}", idParam, e);
                Messagebox.show("ID de agente inválido", "Error", Messagebox.OK, Messagebox.ERROR);
            }
        } else {
            initNewAgent();
        }
    }


    /**
     * Inicializa un nuevo agente
     * @throws Exception
     */
    private void initNewAgent() throws Exception {
        log.info("Inicializando nuevo agente");
        isNewAgent = true;
        isEditMode = true;
        currentAgent = new Agent();

        // Valores por defecto
        currentAgent.setAgtstatus("DRAFT");
        currentAgent.setAgtversion("1.0.0");
        currentAgent.setAgtcreatedby(getUser().getUsername());
        currentAgent.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));
        currentAgent.setAgtupdatedby(getUser().getUsername());
        currentAgent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Carga un agente existente por ID
     */
    private void loadAgent(Long agentId) {
        try {
            log.info("Cargando agente ID: {}", agentId);

            currentAgent = agentService.findById(agentId);

            if (currentAgent != null) {
                isNewAgent = false;
                isEditMode = false;

                // Cargar información relacionada
                loadAgentVersions(agentId);
                loadAgentDeployments(agentId);
                loadAgentTools(agentId);
                loadAgentWorkflows(agentId);
                loadHealthChecks(agentId);
                loadMonitoringData(agentId);
                loadCollaborations(agentId);
                loadRecentDecisions(agentId);
                loadPerformanceMetrics(agentId);
                loadComplianceStatus(agentId);

                calculateKPIs();

                log.info("Agente cargado correctamente: {}", currentAgent.getAgtname());
            } else {
                log.warn("No se encontró agente con ID: {}", agentId);
                Messagebox.show("Agente no encontrado", "Advertencia",
                    Messagebox.OK, Messagebox.EXCLAMATION);
                initNewAgent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar agente", e);
            Messagebox.show("Error al cargar agente: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al cargar agente", e);
            Messagebox.show("Error inesperado al cargar agente: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Carga de Datos Relacionados ==========

    private void loadAgentVersions(Long agentId) {
        try {
            log.debug("Cargando versiones del agente");

            // TABLE - usar findAllEntity() con filtros
            PageParams pageParams = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentVersion> result = agentVersionService.findAll(pageParams, criterias);

            if (result != null && result.getContent() != null) {
                agentVersions = result.getContent();
                log.info("Cargadas {} versiones", agentVersions.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar versiones", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar versiones", e);
        }
    }

    private void loadAgentDeployments(Long agentId) {
        try {
            log.debug("Cargando deployments del agente");

            // TABLE - usar findAllEntity() con filtro por FK
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentDeployment> result = agentDeploymentService.findAll(pageParams, criterias
            );

            if (result != null && result.getContent() != null) {
                agentDeployments = result.getContent();
                log.info("Cargados {} deployments", agentDeployments.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar deployments", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar deployments", e);
        }
    }

    private void loadAgentTools(Long agentId) {
        try {
            log.debug("Cargando tools del agente");

            // TABLE - usar findAllEntity() con filtro por FK
            PageParams pageParams = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentTool> result = agentToolService.findAll(pageParams, criterias
            );

            if (result != null && result.getContent() != null) {
                agentTools = result.getContent();
                log.info("Cargadas {} tools", agentTools.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar tools", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar tools", e);
        }
    }

    private void loadAgentWorkflows(Long agentId) {
        try {
            log.debug("Cargando workflows del agente");

            // TABLE - usar findAllEntity() con filtro por FK
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentWorkflow> result = agentWorkflowService.findAll(pageParams, criterias
            );

            if (result != null && result.getContent() != null) {
                agentWorkflows = result.getContent();
                log.info("Cargados {} workflows", agentWorkflows.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar workflows", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar workflows", e);
        }
    }

    private void loadHealthChecks(Long agentId) {
        try {
            log.debug("Cargando health checks");

            // TABLE - usar findAllEntity() con filtro por FK
            PageParams pageParams = PageParams.builder()
                .maxRows(10)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentHealth> result = agentHealthService.findAll(pageParams, criterias
            );

            if (result != null && result.getContent() != null) {
                healthChecks = result.getContent();
                log.info("Cargados {} health checks", healthChecks.size());

                // Actualizar health status actual
                if (!healthChecks.isEmpty()) {
                    AgentHealth latest = healthChecks.get(0);
                    currentHealthStatus = latest.getAgthealthstatus();
                    lastHealthCheck = latest.getAgtcreatedat();
                }
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar health checks", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar health checks", e);
        }
    }

    private void loadMonitoringData(Long agentId) {
        try {
            log.debug("Cargando datos de monitoreo");

            // TABLE - usar findAllEntity() con filtro por FK
            PageParams pageParams = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentMonitoring> result = agentMonitoringService.findAll(pageParams, criterias
            );

            if (result != null && result.getContent() != null) {
                monitoringData = result.getContent();
                log.info("Cargados {} registros de monitoreo", monitoringData.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar datos de monitoreo", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar datos de monitoreo", e);
        }
    }

    private void loadCollaborations(Long agentId) {
        try {
            log.debug("Cargando colaboraciones");

            // TABLE - usar findAllEntity() con filtro por FK
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentCollaboration> result = agentCollaborationService.findAll(pageParams, criterias
            );

            if (result != null && result.getContent() != null) {
                collaborations = result.getContent();
                log.info("Cargadas {} colaboraciones", collaborations.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar colaboraciones", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar colaboraciones", e);
        }
    }

    private void loadRecentDecisions(Long agentId) {
        try {
            log.debug("Cargando decisiones recientes");

            // TABLE - usar findAllEntity() con filtro por FK
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDAGTAGENTS0");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentDecision> result = agentDecisionService.findAll(pageParams, criterias
            );

            if (result != null && result.getContent() != null) {
                recentDecisions = result.getContent();
                log.info("Cargadas {} decisiones", recentDecisions.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar decisiones", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar decisiones", e);
        }
    }

    private void loadPerformanceMetrics(Long agentId) {
        try {
            log.debug("Cargando métricas de performance");

            // VIEW - usar findAllView() con filtro
            PageParams pageParams = PageParams.builder()
                .maxRows(1)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDXAGENT");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentPerformanceMetrics> result = agentPerformanceMetricsService.findAll(pageParams, criterias);

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                performanceMetrics = result.getContent().get(0);
                log.info("Métricas de performance cargadas");
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar métricas de performance", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar métricas de performance", e);
        }
    }

    private void loadComplianceStatus(Long agentId) {
        try {
            log.debug("Cargando estado de compliance");

            // VIEW - usar findAllView() con filtro
            PageParams pageParams = PageParams.builder()
                .maxRows(1)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDXAGENT");
            criteria.setValueEnd(agentId);
            criterias.addCriteria(criteria);

            PageResult<AgentComplianceStatus> result = agentComplianceStatusService.findAll(pageParams, criterias);

            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                complianceStatus = result.getContent().get(0);
                log.info("Estado de compliance cargado");
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar estado de compliance", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar estado de compliance", e);
        }
    }

    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs del agente");

            totalExecutions = monitoringData.size();

            successfulExecutions = (int) monitoringData.stream()
                .filter(m -> "SUCCESS".equals(m.getAgtstatus()))
                .count();

            failedExecutions = (int) monitoringData.stream()
                .filter(m -> "FAILED".equals(m.getAgtstatus()))
                .count();

            // Calcular tiempo promedio de ejecución (en segundos)
            if (!monitoringData.isEmpty()) {
                double totalTime = monitoringData.stream()
                    .filter(m -> m.getAgtdurationseconds() != null)
                    .mapToDouble(m -> m.getAgtdurationseconds())
                    .sum();
                averageExecutionTime = totalTime / monitoringData.size();
            }

            log.info("KPIs calculados - Total: {}, Success: {}, Failed: {}",
                totalExecutions, successfulExecutions, failedExecutions);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    // ========== Comandos CRUD ==========

    @Command
    @NotifyChange("*")
    public void saveAgent() {
        log.info("Guardando agente");
        try {
            if (validateAgent()) {
                if (isNewAgent) {
                    currentAgent.setAgtcreatedby(getUser().getUsername());
                    currentAgent.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));
                }

                currentAgent.setAgtupdatedby(getUser().getUsername());
                currentAgent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));

                currentAgent = agentService.create(currentAgent);

                isNewAgent = false;
                isEditMode = false;

                Messagebox.show("Agente guardado correctamente", "Éxito",
                    Messagebox.OK, Messagebox.INFORMATION);

                log.info("Agente guardado exitosamente: {}", currentAgent.getIdxagent());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al guardar agente", e);
            Messagebox.show("Error al guardar agente: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al guardar agente", e);
            Messagebox.show("Error inesperado al guardar agente: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"isEditMode"})
    public void enableEdit() {
        log.info("Habilitando modo edición");
        isEditMode = true;
    }

    @Command
    @NotifyChange("*")
    public void cancelEdit() {
        log.info("Cancelando edición");
        if (isNewAgent) {
            Executions.sendRedirect("/agents/agents-overview.zul");
        } else {
            loadAgent(currentAgent.getIdxagent());
            isEditMode = false;
        }
    }

    private boolean validateAgent() {
        if (currentAgent.getAgtname() == null || currentAgent.getAgtname().trim().isEmpty()) {
            Messagebox.show("El nombre del agente es obligatorio", "Validación",
                Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }

        if (currentAgent.getAgttype() == null || currentAgent.getAgttype().trim().isEmpty()) {
            Messagebox.show("El tipo de agente es obligatorio", "Validación",
                Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }

        return true;
    }

    // ========== Comandos de Operaciones Especiales ==========

    @Command
    @NotifyChange({"currentHealthStatus", "lastHealthCheck", "healthChecks"})
    public void performHealthCheck() {
        log.info("Ejecutando health check para agente: {}", currentAgent.getIdxagent());
        try {
            // Crear registro de health check
            AgentHealth healthCheck = new AgentHealth();
            healthCheck.setAgent(currentAgent);
            healthCheck.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));

            // Lógica simplificada de health check
            // En producción, esto llamaría a un procedimiento o servicio externo
            if ("ACTIVE".equals(currentAgent.getAgtstatus())) {
                healthCheck.setAgthealthstatus("HEALTHY");
            } else {
                healthCheck.setAgthealthstatus("UNHEALTHY");
            }

            healthCheck = agentService.create(healthCheck);

            currentHealthStatus = healthCheck.getAgthealthstatus();
            lastHealthCheck = healthCheck.getAgtcreatedat();

            // Recargar health checks
            loadHealthChecks(currentAgent.getIdxagent());

            Messagebox.show("Health check completado: " + currentHealthStatus, "Éxito",
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (GovernanceServiceException e) {
            log.error("Error al ejecutar health check", e);
            Messagebox.show("Error al ejecutar health check: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al ejecutar health check", e);
            Messagebox.show("Error inesperado al ejecutar health check: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void deployAgent(@BindingParam("environment") String environment) {
        log.info("Desplegando agente {} en environment: {}", currentAgent.getIdxagent(), environment);
        try {
            if (environment == null || environment.trim().isEmpty()) {
                environment = "PRODUCTION";
            }

            AgentDeployment deployment = new AgentDeployment();
            deployment.setAgent(currentAgent);
            deployment.setAgtdeploymentname("Deployment " + environment);
            deployment.setAgtenvironment(environment);
            deployment.setAgtdeploymentstatus("DEPLOYING");
            deployment.setAgtversion(currentAgent.getAgtversion());
            deployment.setAgtcreatedby(getUser().getUsername());
            deployment.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));

            deployment = agentService.create(deployment);

            // Actualizar estado del agente
            currentAgent.setAgtstatus("DEPLOYED");
            currentAgent.setAgtupdatedby(getUser().getUsername());
            currentAgent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
            currentAgent = agentService.create(currentAgent);

            loadAgentDeployments(currentAgent.getIdxagent());

            Messagebox.show("Agente desplegado correctamente en " + environment, "Éxito",
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (GovernanceServiceException e) {
            log.error("Error al desplegar agente", e);
            Messagebox.show("Error al desplegar agente: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al desplegar agente", e);
            Messagebox.show("Error inesperado al desplegar agente: " + e.getMessage(), "Error",
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void refreshData() {
        log.info("Refrescando datos del agente");
        if (currentAgent != null && currentAgent.getIdxagent() != null) {
            loadAgent(currentAgent.getIdxagent());
        }
    }

    // ========== Getters ==========

    public Agent getCurrentAgent() {
        return currentAgent;
    }

    public boolean isNewAgent() {
        return isNewAgent;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public List<AgentVersion> getAgentVersions() {
        return agentVersions;
    }

    public List<AgentDeployment> getAgentDeployments() {
        return agentDeployments;
    }

    public List<AgentTool> getAgentTools() {
        return agentTools;
    }

    public List<AgentWorkflow> getAgentWorkflows() {
        return agentWorkflows;
    }

    public List<AgentHealth> getHealthChecks() {
        return healthChecks;
    }

    public List<AgentMonitoring> getMonitoringData() {
        return monitoringData;
    }

    public List<AgentCollaboration> getCollaborations() {
        return collaborations;
    }

    public List<AgentDecision> getRecentDecisions() {
        return recentDecisions;
    }

    public AgentPerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public AgentComplianceStatus getComplianceStatus() {
        return complianceStatus;
    }

    public Integer getTotalExecutions() {
        return totalExecutions;
    }

    public Integer getSuccessfulExecutions() {
        return successfulExecutions;
    }

    public Integer getFailedExecutions() {
        return failedExecutions;
    }

    public Double getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public String getCurrentHealthStatus() {
        return currentHealthStatus;
    }

    public Timestamp getLastHealthCheck() {
        return lastHealthCheck;
    }

    // ========== Cleanup ==========

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            currentAgent = null;
            performanceMetrics = null;
            complianceStatus = null;

            if (agentVersions != null) {
                agentVersions.clear();
                agentVersions = null;
            }

            if (agentDeployments != null) {
                agentDeployments.clear();
                agentDeployments = null;
            }

            if (agentTools != null) {
                agentTools.clear();
                agentTools = null;
            }

            if (agentWorkflows != null) {
                agentWorkflows.clear();
                agentWorkflows = null;
            }

            if (healthChecks != null) {
                healthChecks.clear();
                healthChecks = null;
            }

            if (monitoringData != null) {
                monitoringData.clear();
                monitoringData = null;
            }

            if (collaborations != null) {
                collaborations.clear();
                collaborations = null;
            }

            if (recentDecisions != null) {
                recentDecisions.clear();
                recentDecisions = null;
            }

            agentService = null;
            agentVersionService = null;
            agentDeploymentService = null;
            agentToolService = null;
            agentWorkflowService = null;
            agentHealthService = null;
            agentMonitoringService = null;
            agentCollaborationService = null;
            agentDecisionService = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }

	@Override
	public void setBeans(Object bean) {
		// TODO Auto-generated method stub

	}
}
