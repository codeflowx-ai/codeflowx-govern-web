package com.codeflowx.govern.viewmodel.aios;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.aios.AgentMemory;
import codeflowx.nocode.persist.BusinessService;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel para listado de memorias de un agente.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentMemoryListViewModel extends BaseFront<AgentMemoryListViewModel> {

    private static final long serialVersionUID = 1L;
    private static final String SUPERVISOR_API_URL = "http://localhost:8087/api/v1/supervisor";

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

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========== Datos ==========
    private String agentId;
    private List<MemoryRow> memoryRows = new ArrayList<>();
    private String selectedNamespace = "ALL";
    private String searchKey = "";

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
            loadMemories();
        } else {
            Messagebox.show("No se proporcionó ID de agente", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadMemories() {
        try {
            if (agentId == null || agentId.isEmpty()) {
                return;
            }

            // Buscar memorias del agente
            String query = "SELECT * FROM AIO_AGENT_MEMORIES WHERE IDXAGENT = ?";
            List<AgentMemory> memories = businessService.queryfromParams(AgentMemory.class, query, List.of(agentId));

            memoryRows = new ArrayList<>();
            for (AgentMemory memory : memories) {
                if (selectedNamespace.equals("ALL") || selectedNamespace.equals(memory.getNamespace())) {
                    if (searchKey == null || searchKey.isEmpty() ||
                        memory.getMemoryKey().toLowerCase().contains(searchKey.toLowerCase())) {
                        MemoryRow row = buildMemoryRow(memory);
                        memoryRows.add(row);
                    }
                }
            }

            log.info("Cargadas {} memorias para agente {}", memoryRows.size(), agentId);
        } catch (Exception e) {
            log.error("Error al cargar memorias", e);
            Messagebox.show("Error al cargar memorias: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
            memoryRows = new ArrayList<>();
        }
    }

    private MemoryRow buildMemoryRow(AgentMemory memory) {
        MemoryRow row = new MemoryRow();
        row.setMemory(memory);
        row.setNamespace(memory.getNamespace());
        row.setKey(memory.getMemoryKey());
        row.setVersion(memory.getVersion());
        row.setCreatedAt(memory.getCreatedAt() != null ? memory.getCreatedAt().toString() : "");
        row.setExpiresAt(memory.getExpiresAt() != null ? memory.getExpiresAt().toString() : "Never");
        row.setSize(calculateSize(memory));
        return row;
    }

    private String calculateSize(AgentMemory memory) {
        // Tamaño aproximado basado en storage path (en producción se calcularía desde MinIO)
        return "N/A";
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        loadMemories();
    }

    @Command
    public void viewDecryptedContent(@BindingParam("namespace") String namespace, @BindingParam("key") String key) {
        try {
            // Verificar rol AIOS_ADMIN
            // TODO: Implementar verificación de rol

            String url = SUPERVISOR_API_URL + "/" + agentId + "/memory?namespace=" + namespace + "&key=" + key;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("value")) {
                String content = objectMapper.writeValueAsString(response.get("value"));
                Messagebox.show("Contenido:\n" + content, "Contenido Decifrado", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("No se pudo obtener el contenido", "Error", Messagebox.OK, Messagebox.ERROR);
            }
        } catch (Exception e) {
            log.error("Error obteniendo contenido decifrado", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void compareVersions(@BindingParam("namespace") String namespace, @BindingParam("key") String key) {
        try {
            String url = SUPERVISOR_API_URL + "/" + agentId + "/memory/history?namespace=" + namespace + "&key=" + key;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("versions")) {
                List<Map<String, Object>> versions = (List<Map<String, Object>>) response.get("versions");
                // TODO: Mostrar diff en ventana modal
                Messagebox.show("Versiones encontradas: " + versions.size(), "Info", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error comparando versiones", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void deleteVersion(@BindingParam("namespace") String namespace, @BindingParam("key") String key) {
        try {
            int result = Messagebox.show(
                "¿Está seguro de eliminar esta versión?",
                "Confirmar eliminación",
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION
            );

            if (result == Messagebox.YES) {
                String url = SUPERVISOR_API_URL + "/" + agentId + "/memory?namespace=" + namespace + "&key=" + key;
                restTemplate.delete(url);

                Messagebox.show("Versión eliminada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                loadMemories();
            }
        } catch (Exception e) {
            log.error("Error eliminando versión", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void backToAgentList() {
        appendPage("aios/agents/agent-list.zul", page.getFellow("contenedor"), null);
    }

    // Clase interna para representar una fila de memoria
    @Getter
    @Setter
    public static class MemoryRow {
        private AgentMemory memory;
        private String namespace;
        private String key;
        private Integer version;
        private String createdAt;
        private String expiresAt;
        private String size;
    }
}
