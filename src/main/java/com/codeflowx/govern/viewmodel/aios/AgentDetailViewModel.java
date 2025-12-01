package com.codeflowx.govern.viewmodel.aios;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.aios.AioComponent;
import com.codeflowx.govern.entity.aios.enums.AioComponentType;
import com.codeflowx.govern.service.aios.AioComponentService;
import java.util.List;
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
import org.springframework.web.client.RestTemplate;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel para detalle de agente AI OS.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentDetailViewModel extends BaseFront<AgentDetailViewModel> {

    private static final long serialVersionUID = 1L;
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

    // ========== Datos ==========
    private AioComponent currentAgent;
    private String agentId;
    private String runtimeState = "UNKNOWN";
    private Map<String, Object> runtimeMetadata = new HashMap<>();
    private String lastAction;
    private String lastActionTimestamp;
    private int loopsDetected = 0;

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

        // Obtener agentId de parámetros
        Map<String, Object> args = (Map<String, Object>) Executions.getCurrent().getArg();
        if (args != null && args.containsKey("agentId")) {
            agentId = (String) args.get("agentId");
            loadAgent();
        } else {
            Messagebox.show("No se proporcionó ID de agente", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadAgent() {
        try {
            if (agentId == null || agentId.isEmpty()) {
                return;
            }

            // Buscar componente por UUID usando BusinessService
            String query = "SELECT * FROM AIOCOMPONENTS WHERE iduuid = ? LIMIT 1";
            List<AioComponent> results = businessService.queryfromParams(AioComponent.class, query, List.of(agentId));
            currentAgent = results.isEmpty() ? null : results.get(0);

            if (currentAgent == null) {
                Messagebox.show("Agente no encontrado: " + agentId, "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            // Verificar que sea un agente
            if (currentAgent.getAiocomponenttype() != AioComponentType.AGENT) {
                Messagebox.show("El componente no es un agente", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            // Cargar estado runtime desde API
            loadRuntimeStatus();

            // Extraer metadata de runtimeState
            extractRuntimeMetadata();

            log.info("Agente cargado: {}", agentId);
        } catch (Exception e) {
            log.error("Error al cargar agente", e);
            Messagebox.show("Error al cargar agente: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private void loadRuntimeStatus() {
        try {
            String url = SUPERVISOR_API_URL + "/" + agentId + "/status";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                runtimeState = (String) response.getOrDefault("state", "UNKNOWN");
                if (response.containsKey("metadata")) {
                    runtimeMetadata = (Map<String, Object>) response.get("metadata");
                }
            }
        } catch (Exception e) {
            log.warn("Error cargando estado runtime desde API, usando metadata local", e);
            // Fallback a metadata local
        }
    }

    private void extractRuntimeMetadata() {
        try {
            if (currentAgent.getAiocomponentmetadata() != null && !currentAgent.getAiocomponentmetadata().isEmpty()) {
                JsonNode metadata = objectMapper.readTree(currentAgent.getAiocomponentmetadata());
                if (metadata.has("runtimeState")) {
                    JsonNode runtimeStateNode = metadata.get("runtimeState");
                    if (runtimeStateNode.has("state")) {
                        runtimeState = runtimeStateNode.get("state").asText();
                    }
                    if (runtimeStateNode.has("lastAction")) {
                        lastAction = runtimeStateNode.get("lastAction").asText();
                    }
                    if (runtimeStateNode.has("lastActionTimestamp")) {
                        lastActionTimestamp = runtimeStateNode.get("lastActionTimestamp").asText();
                    }
                    if (runtimeStateNode.has("loopsDetected")) {
                        loopsDetected = runtimeStateNode.get("loopsDetected").asInt();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error extrayendo metadata de runtime", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void executeAction(@BindingParam("action") String action) {
        try {
            log.info("Ejecutando acción {} sobre agente {}", action, agentId);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("action", action);
            requestBody.put("reason", "Manual action from UI");

            String url = SUPERVISOR_API_URL + "/" + agentId + "/actions";
            restTemplate.postForEntity(url, requestBody, Map.class);

            Messagebox.show("Acción " + action + " enviada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);

            // Recargar estado después de un delay
            Thread.sleep(1000);
            loadAgent();
        } catch (Exception e) {
            log.error("Error ejecutando acción", e);
            Messagebox.show("Error ejecutando acción: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void viewMemories() {
        log.info("Navegando a memorias de agente: {}", agentId);
        Map<String, Object> params = new HashMap<>();
        params.put("agentId", agentId);
        appendPage("aios/agents/memory-list.zul", page.getFellow("contenedor"), params);
    }

    @Command
    public void viewTelemetry() {
        log.info("Navegando a telemetría de agente: {}", agentId);
        // TODO: Implementar navegación a telemetría
        Messagebox.show("Funcionalidad de telemetría en desarrollo", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    public void viewLogs() {
        log.info("Navegando a logs de agente: {}", agentId);
        // TODO: Implementar navegación a logs desde ImmutableLog
        Messagebox.show("Funcionalidad de logs en desarrollo", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    public void cancelEdit() {
        log.info("Volviendo a listado de agentes");
        appendPage("aios/agents/agent-list.zul", page.getFellow("contenedor"), null);
    }

    public String getPageTitle() {
        return currentAgent != null ? currentAgent.getAiocomponentname() : "Detalle de Agente";
    }
}
