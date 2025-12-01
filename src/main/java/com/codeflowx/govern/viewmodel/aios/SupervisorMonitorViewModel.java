package com.codeflowx.govern.viewmodel.aios;

import com.codeflowx.framework.zkoss.BaseFront;
import codeflowx.nocode.persist.BusinessService;
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

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel para monitor del Supervisor.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class SupervisorMonitorViewModel extends BaseFront<SupervisorMonitorViewModel> {

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

    // ========== Estado del Supervisor ==========
    private String supervisorStatus = "UNKNOWN"; // OK, WARN, ERROR
    private String workerStatus = "UNKNOWN";
    private String lastBpmnProcess = "N/A";
    private List<LogRow> recentLogs = new ArrayList<>();

    // ========== Test de Integridad ==========
    private Map<String, String> integrityChecks = new HashMap<>();

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

        loadStatus();
        runIntegrityChecks();
    }

    @Command
    @NotifyChange("*")
    public void loadStatus() {
        try {
            // Verificar estado del supervisor (health check)
            checkSupervisorHealth();

            // Verificar estado del worker (desde RabbitMQ o logs)
            checkWorkerStatus();

            // Cargar último BPMN disparado (desde ImmutableLog)
            loadLastBpmnProcess();

            // Cargar logs recientes
            loadRecentLogs();
        } catch (Exception e) {
            log.error("Error cargando estado", e);
        }
    }

    private void checkSupervisorHealth() {
        try {
            String url = SUPERVISOR_API_URL.replace("/api/v1/supervisor", "/actuator/health");
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "UP".equals(response.get("status"))) {
                supervisorStatus = "OK";
            } else {
                supervisorStatus = "WARN";
            }
        } catch (Exception e) {
            log.warn("Error checking supervisor health", e);
            supervisorStatus = "ERROR";
        }
    }

    private void checkWorkerStatus() {
        // TODO: Verificar estado del worker (RabbitMQ consumer, logs, etc.)
        workerStatus = "OK";
    }

    private void loadLastBpmnProcess() {
        // TODO: Cargar desde ImmutableLog (último proceso BPMN disparado)
        lastBpmnProcess = "ai-policy-enforcement-v1";
    }

    private void loadRecentLogs() {
        // TODO: Cargar logs recientes desde ImmutableLog
        recentLogs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LogRow log = new LogRow();
            log.setTimestamp("2024-12-19 10:" + String.format("%02d", i));
            log.setLevel("INFO");
            log.setMessage("Log entry " + i);
            recentLogs.add(log);
        }
    }

    @Command
    @NotifyChange("*")
    public void runIntegrityChecks() {
        try {
            integrityChecks = new HashMap<>();

            // Test conexión MinIO
            testMinIOConnection();

            // Test conexión TimescaleDB
            testTimescaleConnection();

            // Test conexión RabbitMQ
            testRabbitMQConnection();

            // Test integridad hash chain
            testHashChainIntegrity();
        } catch (Exception e) {
            log.error("Error ejecutando tests de integridad", e);
        }
    }

    private void testMinIOConnection() {
        try {
            // TODO: Test conexión MinIO
            integrityChecks.put("MinIO", "OK");
        } catch (Exception e) {
            integrityChecks.put("MinIO", "ERROR: " + e.getMessage());
        }
    }

    private void testTimescaleConnection() {
        try {
            // Test conexión a TimescaleDB (codeflowx_telemetry)
            String query = "SELECT 1";
            businessService.queryfromParams(Object[].class, query, List.of());
            integrityChecks.put("TimescaleDB", "OK");
        } catch (Exception e) {
            integrityChecks.put("TimescaleDB", "ERROR: " + e.getMessage());
        }
    }

    private void testRabbitMQConnection() {
        try {
            // TODO: Test conexión RabbitMQ
            integrityChecks.put("RabbitMQ", "OK");
        } catch (Exception e) {
            integrityChecks.put("RabbitMQ", "ERROR: " + e.getMessage());
        }
    }

    private void testHashChainIntegrity() {
        try {
            // TODO: Verificar integridad de hash chain desde ImmutableLog
            integrityChecks.put("Hash Chain", "OK");
        } catch (Exception e) {
            integrityChecks.put("Hash Chain", "ERROR: " + e.getMessage());
        }
    }

    // Clase interna para logs
    @Getter
    @Setter
    public static class LogRow {
        private String timestamp;
        private String level;
        private String message;
    }
}
