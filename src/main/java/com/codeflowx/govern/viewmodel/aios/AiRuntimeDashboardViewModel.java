package com.codeflowx.govern.viewmodel.aios;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import com.codeflowx.govern.entity.aios.AioComponent;
import com.codeflowx.govern.entity.aios.enums.AioComponentType;
import com.codeflowx.govern.service.aios.AioComponentService;
import codeflowx.nocode.persist.BusinessService;
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
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel para dashboard de runtime de AI OS.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AiRuntimeDashboardViewModel extends BaseFront<AiRuntimeDashboardViewModel> {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private AioComponentService aioComponentService;

    @WireVariable
    private ImmutableLoggingBusinessService immutableLoggingBusinessService;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========== KPIs ==========
    private int activeAgents = 0;
    private int totalLoopsDetected = 0;
    private int policyViolations24h = 0;
    private long totalMemories = 0;
    private int bpmnAlertsToday = 0;

    // ========== Datos para gráficas ==========
    private List<ActivityDataPoint> hourlyActivity = new ArrayList<>();
    private Map<String, Integer> agentsByState = new HashMap<>();
    private Map<String, Integer> risksDistribution = new HashMap<>();

    // ========== Últimos eventos ==========
    private List<EventRow> recentEvents = new ArrayList<>();

    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        loadDashboardData();
    }

    @Command
    @NotifyChange("*")
    public void loadDashboardData() {
        try {
            loadKPIs();
            loadHourlyActivity();
            loadAgentsByState();
            loadRisksDistribution();
            loadRecentEvents();
        } catch (Exception e) {
            log.error("Error cargando datos del dashboard", e);
        }
    }

    private void loadKPIs() {
        try {
            // Contar agentes activos - buscar todos los agentes
            String query = "SELECT * FROM AIOCOMPONENTS WHERE AIOCOMPONENTTYPE = ?";
            List<AioComponent> agents = businessService.queryfromParams(AioComponent.class, query, List.of(AioComponentType.AGENT.name()));
            activeAgents = 0;
            totalLoopsDetected = 0;

            for (AioComponent agent : agents) {
                try {
                    if (agent.getAiocomponentmetadata() != null && !agent.getAiocomponentmetadata().isEmpty()) {
                        JsonNode metadata = objectMapper.readTree(agent.getAiocomponentmetadata());
                        if (metadata.has("runtimeState")) {
                            JsonNode runtimeState = metadata.get("runtimeState");
                            if ("RUNNING".equals(runtimeState.get("state").asText())) {
                                activeAgents++;
                            }
                            if (runtimeState.has("loopsDetected")) {
                                totalLoopsDetected += runtimeState.get("loopsDetected").asInt();
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error parsing metadata for agent: {}", agent.getIduuid(), e);
                }
            }

            // Contar memorias
            String query = "SELECT COUNT(*) FROM AIO_AGENT_MEMORIES";
            List<Object[]> result = businessService.queryfromParams(Object[].class, query, List.of());
            if (!result.isEmpty()) {
                totalMemories = ((Number) result.get(0)[0]).longValue();
            }

            // TODO: Cargar policy violations desde ImmutableLog (últimas 24h)
            policyViolations24h = 0;

            // TODO: Cargar BPMN alerts desde procesos (hoy)
            bpmnAlertsToday = 0;

        } catch (Exception e) {
            log.error("Error cargando KPIs", e);
        }
    }

    private void loadHourlyActivity() {
        try {
            // TODO: Cargar desde TimescaleDB (codeflowx_telemetry)
            // Por ahora datos de ejemplo
            hourlyActivity = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                ActivityDataPoint point = new ActivityDataPoint();
                point.setHour(i);
                point.setCount((int) (Math.random() * 100));
                hourlyActivity.add(point);
            }
        } catch (Exception e) {
            log.error("Error cargando actividad horaria", e);
        }
    }

    private void loadAgentsByState() {
        try {
            agentsByState = new HashMap<>();
            String query = "SELECT * FROM AIOCOMPONENTS WHERE AIOCOMPONENTTYPE = ?";
            List<AioComponent> agents = businessService.queryfromParams(AioComponent.class, query, List.of(AioComponentType.AGENT.name()));

            for (AioComponent agent : agents) {
                String state = "UNKNOWN";
                try {
                    if (agent.getAiocomponentmetadata() != null && !agent.getAiocomponentmetadata().isEmpty()) {
                        JsonNode metadata = objectMapper.readTree(agent.getAiocomponentmetadata());
                        if (metadata.has("runtimeState") && metadata.get("runtimeState").has("state")) {
                            state = metadata.get("runtimeState").get("state").asText();
                        }
                    }
                } catch (Exception e) {
                    // Ignorar
                }
                agentsByState.put(state, agentsByState.getOrDefault(state, 0) + 1);
            }
        } catch (Exception e) {
            log.error("Error cargando agentes por estado", e);
        }
    }

    private void loadRisksDistribution() {
        try {
            risksDistribution = new HashMap<>();
            String query = "SELECT * FROM AIOCOMPONENTS WHERE AIOCOMPONENTTYPE = ?";
            List<AioComponent> agents = businessService.queryfromParams(AioComponent.class, query, List.of(AioComponentType.AGENT.name()));

            for (AioComponent agent : agents) {
                String risk = agent.getAiocomponentriskprofile() != null ?
                    agent.getAiocomponentriskprofile() : "UNKNOWN";
                risksDistribution.put(risk, risksDistribution.getOrDefault(risk, 0) + 1);
            }
        } catch (Exception e) {
            log.error("Error cargando distribución de riesgos", e);
        }
    }

    private void loadRecentEvents() {
        try {
            // TODO: Cargar desde ImmutableLog (últimos 10 eventos)
            recentEvents = new ArrayList<>();
            // Por ahora datos de ejemplo
            for (int i = 0; i < 10; i++) {
                EventRow event = new EventRow();
                event.setTimestamp("2024-12-19 10:" + String.format("%02d", i));
                event.setType("AGENT_ACTION");
                event.setAgentId("agent-" + i);
                event.setDescription("Action executed");
                recentEvents.add(event);
            }
        } catch (Exception e) {
            log.error("Error cargando eventos recientes", e);
        }
    }

    // Clases internas para datos
    @Getter
    @Setter
    public static class ActivityDataPoint {
        private int hour;
        private int count;
    }

    @Getter
    @Setter
    public static class EventRow {
        private String timestamp;
        private String type;
        private String agentId;
        private String description;
    }
}
