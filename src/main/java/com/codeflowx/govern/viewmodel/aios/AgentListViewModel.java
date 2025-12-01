package com.codeflowx.govern.viewmodel.aios;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.aios.AioComponent;
import com.codeflowx.govern.entity.aios.enums.AioComponentType;
import com.codeflowx.govern.service.aios.AioComponentService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.PagingEvent;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel para listado de agentes AI OS.
 * Muestra agentes desde AioComponent (type=AGENT) con estado runtime.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentListViewModel extends BaseFront<AgentListViewModel> {

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    private static final String SUPERVISOR_API_URL = "http://localhost:8087/api/v1/supervisor";

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private AioComponentService aioComponentService;

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

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    private PageResult<AioComponent> pageResult;

    // ========== Filtros ==========
    private String searchTerm = "";
    private String stateFilter = "ALL";
    private String runtimeStateFilter = "ALL";

    // ========== Datos ==========
    private List<AgentRow> agentRows = new ArrayList<>();

    // ========== Métricas ==========
    private int totalAgents = 0;
    private int runningAgents = 0;
    private int pausedAgents = 0;
    private int terminatedAgents = 0;
    private int totalLoopsDetected = 0;
    private int totalPolicyViolations = 0;

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
            log.debug("Cargando agentes - Página: {}", pageParams.getPageActual());

            // Buscar solo componentes tipo AGENT
            Criterias criterias = buildCriterias();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "aiocomponenttype", AioComponentType.AGENT.name()));

            pageResult = aioComponentService.findAll(pageParams, criterias);

            if (pageResult != null && pageResult.getContent() != null) {
                agentRows = new ArrayList<>();
                for (AioComponent component : pageResult.getContent()) {
                    AgentRow row = buildAgentRow(component);
                    agentRows.add(row);
                }
                totalAgents = pageResult.getTotalRows();

                loadMetrics();
                log.info("Cargados {} agentes de {} totales", agentRows.size(), totalAgents);
            } else {
                agentRows = new ArrayList<>();
                totalAgents = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar agentes", e);
            Messagebox.show("Error al cargar agentes: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
            agentRows = new ArrayList<>();
        }
    }

    private AgentRow buildAgentRow(AioComponent component) {
        AgentRow row = new AgentRow();
        row.setComponent(component);
        row.setAgentId(component.getIduuid());
        row.setName(component.getAiocomponentname());
        row.setNamespace(component.getAiocomponentnamespace());
        row.setCode(component.getAiocomponentcode());
        row.setRisk(component.getAiocomponentriskprofile() != null ? component.getAiocomponentriskprofile() : "UNKNOWN");

        // Extraer runtimeState de metadata JSONB
        String runtimeState = "UNKNOWN";
        int loopsDetected = 0;
        String lastActivity = null;

        try {
            if (component.getAiocomponentmetadata() != null && !component.getAiocomponentmetadata().isEmpty()) {
                JsonNode metadata = objectMapper.readTree(component.getAiocomponentmetadata());
                if (metadata.has("runtimeState")) {
                    JsonNode runtimeStateNode = metadata.get("runtimeState");
                    if (runtimeStateNode.has("state")) {
                        runtimeState = runtimeStateNode.get("state").asText();
                    }
                    if (runtimeStateNode.has("loopsDetected")) {
                        loopsDetected = runtimeStateNode.get("loopsDetected").asInt();
                    }
                    if (runtimeStateNode.has("lastActionTimestamp")) {
                        lastActivity = runtimeStateNode.get("lastActionTimestamp").asText();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error parsing metadata for agent: {}", component.getIduuid(), e);
        }

        row.setRuntimeState(runtimeState);
        row.setLoopsDetected(loopsDetected);
        row.setLastActivity(lastActivity);

        return row;
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "aiocomponentname");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(stateFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "aiocomponentstate");
            criteria.setValues(new Object[]{stateFilter});
            criterias.addCriteria(criteria);
        }

        return criterias;
    }

    private void loadMetrics() {
        try {
            runningAgents = (int) agentRows.stream().filter(r -> "RUNNING".equals(r.getRuntimeState())).count();
            pausedAgents = (int) agentRows.stream().filter(r -> "PAUSED".equals(r.getRuntimeState())).count();
            terminatedAgents = (int) agentRows.stream().filter(r -> "TERMINATED".equals(r.getRuntimeState())).count();
            totalLoopsDetected = agentRows.stream().mapToInt(AgentRow::getLoopsDetected).sum();
            // TODO: Cargar policy violations desde ImmutableLog
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
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
        stateFilter = "ALL";
        runtimeStateFilter = "ALL";
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
    public void viewAgentDetails(@BindingParam("agentId") String agentId) {
        log.info("Navegando a detalle de agente: {}", agentId);
        Map<String, Object> params = new HashMap<>();
        params.put("agentId", agentId);
        appendPage("aios/agents/agent-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewMemories(@BindingParam("agentId") String agentId) {
        log.info("Navegando a memorias de agente: {}", agentId);
        Map<String, Object> params = new HashMap<>();
        params.put("agentId", agentId);
        appendPage("aios/agents/memory-list.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewTelemetry(@BindingParam("agentId") String agentId) {
        log.info("Navegando a telemetría de agente: {}", agentId);
        // TODO: Implementar navegación a telemetría
        Messagebox.show("Funcionalidad de telemetría en desarrollo", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    @NotifyChange("*")
    public void executeAction(@BindingParam("agentId") String agentId, @BindingParam("action") String action) {
        try {
            log.info("Ejecutando acción {} sobre agente {}", action, agentId);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("action", action);
            requestBody.put("reason", "Manual action from UI");

            String url = SUPERVISOR_API_URL + "/" + agentId + "/actions";
            restTemplate.postForEntity(url, requestBody, Map.class);

            Messagebox.show("Acción " + action + " enviada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);

            // Recargar datos después de un delay
            loadData();
        } catch (Exception e) {
            log.error("Error ejecutando acción", e);
            Messagebox.show("Error ejecutando acción: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void viewLogs(@BindingParam("agentId") String agentId) {
        log.info("Navegando a logs de agente: {}", agentId);
        // TODO: Implementar navegación a logs desde ImmutableLog
        Messagebox.show("Funcionalidad de logs en desarrollo", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    // Clase interna para representar una fila de agente
    @Getter
    @Setter
    public static class AgentRow {
        private AioComponent component;
        private String agentId;
        private String name;
        private String namespace;
        private String code;
        private String runtimeState; // RUNNING, PAUSED, TERMINATED, ERROR
        private String risk; // HIGH, LIMITED, MINIMAL
        private int loopsDetected;
        private String lastActivity;
    }
}
