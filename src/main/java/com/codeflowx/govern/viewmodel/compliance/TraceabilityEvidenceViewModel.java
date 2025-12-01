package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
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

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Vista Consolidada de Trazabilidad y Evidencias
 *
 * Funcionalidad:
 * - Consolidar logs, prompts, outputs, decisiones, flags, incidencias
 * - Mostrar trazabilidad completa modelo-dataset-output
 * - Integración con ImmutableLoggingBusinessService
 * - Integración con telemetría (opcional)
 *
 * Modo MOCK:
 * - URL: /gobierno/compliance/traceability-evidence.zul?mock=true
 * - Variable de entorno: MOCK_MODE=true
 * - Carga datos de ejemplo para demo
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class TraceabilityEvidenceViewModel extends BaseFront<TraceabilityEvidenceViewModel> {

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

    @WireVariable("APPLICATION_DS")
    protected DataSource ds;

    @WireVariable
    private com.codeflowx.govern.service.logging.ImmutableLogService immutableLogService;

    @WireVariable
    private com.codeflowx.govern.service.monitoring.AuditLogService auditLogService;

    @WireVariable
    private com.codeflowx.govern.service.governance.PolicyAuditLogService policyAuditLogService;

    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Modo MOCK ==========
    @Getter
    private boolean mockMode = false;

    // ========== Datos de Trazabilidad ==========
    @Getter
    private List<TraceabilityLog> logs = new ArrayList<>();

    @Getter
    private List<EvidenceRecord> evidences = new ArrayList<>();

    @Getter @Setter
    private TraceabilityLog selectedLog;

    @Getter @Setter
    private EvidenceRecord selectedEvidence;

    // ========== Filtros ==========
    @Getter @Setter
    private String filterType = "ALL"; // ALL, LOG, PROMPT, OUTPUT, DECISION, FLAG, INCIDENT

    @Getter @Setter
    private String filterEntityType = "ALL"; // ALL, MODEL, AGENT, PROMPT, DATASET

    @Getter @Setter
    private String filterEntityName = "";

    @Getter @Setter
    private String filterDateRange = "LAST_30_DAYS";

    // ========== Estadísticas ==========
    @Getter
    private int totalLogs = 0;

    @Getter
    private int totalEvidences = 0;

    @Getter
    private int flaggedItems = 0;

    @Getter
    private int incidentCount = 0;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        // Detectar modo MOCK
        String mockParam = Executions.getCurrent().getParameter("mock");
        mockMode = "true".equalsIgnoreCase(mockParam);

        if (System.getenv("MOCK_MODE") != null) {
            mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE"));
        }

        log.info("🚀 Inicializando TraceabilityEvidenceViewModel - MOCK MODE: {}", mockMode);

        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }

    /**
     * Cargar datos reales desde servicios
     */
    private void loadRealData() {
        log.info("📊 Cargando datos reales de trazabilidad...");

        try {
            logs.clear();
            evidences.clear();

            // Cargar logs inmutables
            if (immutableLogService != null) {
                try {
                    codeflowx.nocode.persist.PageParams pageParams = codeflowx.nocode.persist.PageParams.builder()
                        .maxRows(100)
                        .pageActual(1)
                        .rowActual(0)
                        .ascending(false)
                        .sortField("createdat")
                        .build();
                    
                    codeflowx.nocode.persist.Criterias criterias = buildTraceabilityCriterias();
                    codeflowx.nocode.persist.PageResult<com.codeflowx.govern.entity.logging.ImmutableLog> result = 
                        immutableLogService.findAll(pageParams, criterias);
                    
                    if (result != null && result.getContent() != null) {
                        for (com.codeflowx.govern.entity.logging.ImmutableLog immutableLog : result.getContent()) {
                            TraceabilityLog log = new TraceabilityLog();
                            log.setId("immutable-" + immutableLog.getIdximmutablelog());
                            log.setType("LOG");
                            log.setEntityType(immutableLog.getEntitytype());
                            log.setEntityName(immutableLog.getEntityname());
                            log.setTimestamp(immutableLog.getCreatedat());
                            log.setUser(immutableLog.getUsername());
                            log.setAction(immutableLog.getAction());
                            log.setDetails(immutableLog.getDetails());
                            log.setMetadata(immutableLog.getMetadata());
                            logs.add(log);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error al cargar logs inmutables", e);
                }
            }

            // Cargar logs de auditoría
            if (auditLogService != null) {
                try {
                    codeflowx.nocode.persist.PageParams pageParams = codeflowx.nocode.persist.PageParams.builder()
                        .maxRows(100)
                        .pageActual(1)
                        .rowActual(0)
                        .ascending(false)
                        .sortField("createdat")
                        .build();
                    
                    codeflowx.nocode.persist.Criterias criterias = buildTraceabilityCriterias();
                    codeflowx.nocode.persist.PageResult<com.codeflowx.govern.entity.monitoring.AuditLog> result = 
                        auditLogService.findAll(pageParams, criterias);
                    
                    if (result != null && result.getContent() != null) {
                        for (com.codeflowx.govern.entity.monitoring.AuditLog auditLog : result.getContent()) {
                            TraceabilityLog log = new TraceabilityLog();
                            log.setId("audit-" + auditLog.getIdxauditlog());
                            log.setType(determineLogType(auditLog.getAction()));
                            log.setEntityType(auditLog.getEntitytype());
                            log.setEntityName(auditLog.getEntityname());
                            log.setTimestamp(auditLog.getCreatedat());
                            log.setUser(auditLog.getUsername());
                            log.setAction(auditLog.getAction());
                            log.setDetails(auditLog.getDetails());
                            log.setMetadata(auditLog.getMetadata());
                            logs.add(log);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error al cargar logs de auditoría", e);
                }
            }

            // Cargar logs de políticas
            if (policyAuditLogService != null) {
                try {
                    codeflowx.nocode.persist.PageParams pageParams = codeflowx.nocode.persist.PageParams.builder()
                        .maxRows(50)
                        .pageActual(1)
                        .rowActual(0)
                        .ascending(false)
                        .sortField("createdat")
                        .build();
                    
                    codeflowx.nocode.persist.PageResult<com.codeflowx.govern.entity.governance.PolicyAuditLog> result = 
                        policyAuditLogService.findAll(pageParams);
                    
                    if (result != null && result.getContent() != null) {
                        for (com.codeflowx.govern.entity.governance.PolicyAuditLog policyLog : result.getContent()) {
                            TraceabilityLog log = new TraceabilityLog();
                            log.setId("policy-" + policyLog.getIdxpolicyauditlog());
                            log.setType("DECISION");
                            log.setEntityType("POLICY");
                            log.setEntityName(policyLog.getPolicyname());
                            log.setTimestamp(policyLog.getCreatedat());
                            log.setUser(policyLog.getUsername());
                            log.setAction(policyLog.getAction());
                            log.setDetails(policyLog.getDetails());
                            logs.add(log);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error al cargar logs de políticas", e);
                }
            }

            // Integrar con telemetría (codeflowx-aios-telemetry) - Solo si no está en modo mock
            String telemetryUrl = environment != null ? environment.getProperty("telemetry.api.url") : null;
            if (telemetryUrl != null && !telemetryUrl.isEmpty()) {
                try {
                    // Llamar a API REST del microservicio de telemetría
                    org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
                    String url = telemetryUrl + "/api/telemetry/events";
                    
                    java.util.Map<String, String> params = new java.util.HashMap<>();
                    if (!"ALL".equals(filterEntityType)) {
                        params.put("entityType", filterEntityType);
                    }
                    if (filterEntityName != null && !filterEntityName.isEmpty()) {
                        params.put("entityName", filterEntityName);
                    }
                    
                    // Llamar al servicio (asumiendo que retorna JSON con eventos)
                    // String response = restTemplate.getForObject(url, String.class, params);
                    // Parsear respuesta y agregar a logs
                    log.info("Integración con telemetría disponible en: {}", telemetryUrl);
                } catch (Exception e) {
                    log.warn("Error al integrar con telemetría", e);
                }
            }

            calculateStatistics();
            log.info("✅ Cargados {} logs desde servicios", logs.size());

        } catch (Exception e) {
            log.error("Error al cargar datos reales de trazabilidad", e);
            logs.clear();
            evidences.clear();
        }
    }

    /**
     * Cargar datos MOCK para demo
     */
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Trazabilidad y Evidencias...");

        // Logs de ejemplo
        logs.clear();

        TraceabilityLog log1 = new TraceabilityLog();
        log1.setId("log-001");
        log1.setType("DECISION");
        log1.setEntityType("MODEL");
        log1.setEntityName("Modelo-Prediccion-Fraude-v4.2");
        log1.setEntityVersion("4.2.0");
        log1.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(2)));
        log1.setUser("admin@codeflowx.com");
        log1.setAction("APPROVE");
        log1.setDetails("Aprobación manual después de revisión HITL. Scores: Risk=78, Compliance=85, Ethics=82");
        log1.setMetadata("{\"riskScore\":78.0,\"complianceScore\":85.0,\"ethicsScore\":82.0}");
        logs.add(log1);

        TraceabilityLog log2 = new TraceabilityLog();
        log2.setId("log-002");
        log2.setType("PROMPT");
        log2.setEntityType("AGENT");
        log2.setEntityName("Agent-Fraud-Detection-v2");
        log2.setEntityVersion("2.1.0");
        log2.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(3)));
        log2.setUser("system");
        log2.setAction("EXECUTE");
        log2.setDetails("Prompt ejecutado: 'Analizar transacción #12345 para detectar patrones de fraude'");
        log2.setMetadata("{\"promptId\":\"prompt-001\",\"tokens\":150}");
        logs.add(log2);

        TraceabilityLog log3 = new TraceabilityLog();
        log3.setId("log-003");
        log3.setType("OUTPUT");
        log3.setEntityType("AGENT");
        log3.setEntityName("Agent-Fraud-Detection-v2");
        log3.setEntityVersion("2.1.0");
        log3.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(3).plusMinutes(5)));
        log3.setUser("system");
        log3.setAction("GENERATE");
        log3.setDetails("Output generado: 'Transacción marcada como FRAUDULENTA con confianza 94.5%'");
        log3.setMetadata("{\"confidence\":0.945,\"decision\":\"FRAUDULENT\"}");
        logs.add(log3);

        TraceabilityLog log4 = new TraceabilityLog();
        log4.setId("log-004");
        log4.setType("FLAG");
        log4.setEntityType("MODEL");
        log4.setEntityName("Modelo-Credit-Scoring-v3.0");
        log4.setEntityVersion("3.0.0");
        log4.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        log4.setUser("monitoring-system");
        log4.setAction("FLAG");
        log4.setDetails("Flag: Drift detectado - Score de drift: 0.28 (umbral: 0.15)");
        log4.setMetadata("{\"driftScore\":0.28,\"threshold\":0.15,\"severity\":\"HIGH\"}");
        log4.setFlagged(true);
        logs.add(log4);

        TraceabilityLog log5 = new TraceabilityLog();
        log5.setId("log-005");
        log5.setType("INCIDENT");
        log5.setEntityType("AGENT");
        log5.setEntityName("Agent-Credit-Scoring-v3.0");
        log5.setEntityVersion("3.0.0");
        log5.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
        log5.setUser("compliance-officer@codeflowx.com");
        log5.setAction("REPORT_INCIDENT");
        log5.setDetails("Incidente reportado: Sesgo detectado en decisiones de crédito para grupo demográfico específico");
        log5.setMetadata("{\"incidentType\":\"BIAS\",\"severity\":\"HIGH\",\"affectedGroup\":\"demographic\"}");
        log5.setFlagged(true);
        logs.add(log5);

        // Evidencias
        evidences.clear();

        EvidenceRecord ev1 = new EvidenceRecord();
        ev1.setId("ev-001");
        ev1.setType("MODEL_LINEAGE");
        ev1.setEntityType("MODEL");
        ev1.setEntityName("Modelo-Prediccion-Fraude-v4.2");
        ev1.setDescription("Trazabilidad completa: Modelo v4.2 → Dataset fraud-dataset-v3.1 → Output predictions-2025-11-25");
        ev1.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(2)));
        ev1.setHash("sha256:abc123def456...");
        ev1.setImmutable(true);
        evidences.add(ev1);

        EvidenceRecord ev2 = new EvidenceRecord();
        ev2.setId("ev-002");
        ev2.setType("PROMPT_VERSION");
        ev2.setEntityType("PROMPT");
        ev2.setEntityName("Prompt-Customer-Support-v1.5");
        ev2.setDescription("Versión 1.5 del prompt almacenada con hash inmutable");
        ev2.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        ev2.setHash("sha256:789ghi012jkl...");
        ev2.setImmutable(true);
        evidences.add(ev2);

        EvidenceRecord ev3 = new EvidenceRecord();
        ev3.setId("ev-003");
        ev3.setType("DECISION_AUDIT");
        ev3.setEntityType("AGENT");
        ev3.setEntityName("Agent-Fraud-Detection-v2");
        ev3.setDescription("Auditoría de decisión: Aprobación HITL registrada con firma digital");
        ev3.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(2)));
        ev3.setHash("sha256:345mno678pqr...");
        ev3.setImmutable(true);
        evidences.add(ev3);

        calculateStatistics();
        log.info("✅ Datos MOCK cargados - {} logs, {} evidencias",
                 logs.size(), evidences.size());
    }

    /**
     * Construir criterias de búsqueda para trazabilidad
     */
    private codeflowx.nocode.persist.Criterias buildTraceabilityCriterias() {
        codeflowx.nocode.persist.Criterias criterias = new codeflowx.nocode.persist.Criterias();
        
        if (!"ALL".equals(filterEntityType)) {
            codeflowx.nocode.persist.Criteria criteria = new codeflowx.nocode.persist.Criteria(
                codeflowx.nocode.persist.Operation.AND,
                codeflowx.nocode.persist.Evaluation.EQUAL,
                "entitytype"
            );
            criteria.setValues(new Object[]{filterEntityType});
            criterias.addCriteria(criteria);
        }
        
        if (filterEntityName != null && !filterEntityName.trim().isEmpty()) {
            codeflowx.nocode.persist.Criteria criteria = new codeflowx.nocode.persist.Criteria(
                codeflowx.nocode.persist.Operation.AND,
                codeflowx.nocode.persist.Evaluation.LIKE,
                "entityname"
            );
            criteria.setValues(new Object[]{"%" + filterEntityName.trim() + "%"});
            criterias.addCriteria(criteria);
        }
        
        return criterias;
    }
    
    /**
     * Determinar tipo de log desde acción
     */
    private String determineLogType(String action) {
        if (action == null) return "LOG";
        String upperAction = action.toUpperCase();
        if (upperAction.contains("PROMPT")) return "PROMPT";
        if (upperAction.contains("OUTPUT")) return "OUTPUT";
        if (upperAction.contains("DECISION") || upperAction.contains("APPROVE") || upperAction.contains("REJECT")) return "DECISION";
        if (upperAction.contains("FLAG")) return "FLAG";
        if (upperAction.contains("INCIDENT")) return "INCIDENT";
        return "LOG";
    }

    /**
     * Calcular estadísticas
     */
    private void calculateStatistics() {
        totalLogs = logs.size();
        totalEvidences = evidences.size();
        flaggedItems = (int) logs.stream()
            .filter(l -> l.isFlagged())
            .count();
        incidentCount = (int) logs.stream()
            .filter(l -> "INCIDENT".equals(l.getType()))
            .count();
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange({"logs", "evidences"})
    public void filterData() {
        log.info("🔍 Filtrando datos - Tipo: {}, Entidad: {}, Nombre: {}, Rango: {}",
                 filterType, filterEntityType, filterEntityName, filterDateRange);
        // TODO: Implementar filtrado real
    }

    @Command
    @NotifyChange({"selectedLog"})
    public void selectLog(@BindingParam("log") TraceabilityLog log) {
        this.selectedLog = log;
        log.info("📋 Log seleccionado: {}", log.getId());
    }

    @Command
    @NotifyChange({"selectedEvidence"})
    public void selectEvidence(@BindingParam("evidence") EvidenceRecord evidence) {
        this.selectedEvidence = evidence;
        log.info("📋 Evidencia seleccionada: {}", evidence.getId());
    }

    @Command
    public void exportLogs() {
        if (mockMode) {
            Messagebox.show("✅ [DEMO] Exportación de logs iniciada\n\nTotal logs: " + totalLogs,
                          "Demo Mode", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            // TODO: Implementar exportación real
            Messagebox.show("Exportación de logs iniciada", "Información",
                          Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    public void exportEvidences() {
        if (mockMode) {
            Messagebox.show("✅ [DEMO] Exportación de evidencias iniciada\n\nTotal evidencias: " + totalEvidences,
                          "Demo Mode", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            // TODO: Implementar exportación real
            Messagebox.show("Exportación de evidencias iniciada", "Información",
                          Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    public void refreshData() {
        log.info("🔄 Refrescando datos de trazabilidad...");
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }

    // ========== Clases Internas ==========

    /**
     * Representa un log de trazabilidad
     */
    @Getter
    @Setter
    public static class TraceabilityLog {
        private String id;
        private String type; // LOG, PROMPT, OUTPUT, DECISION, FLAG, INCIDENT
        private String entityType; // MODEL, AGENT, PROMPT, DATASET
        private String entityName;
        private String entityVersion;
        private Timestamp timestamp;
        private String user;
        private String action;
        private String details;
        private String metadata; // JSON
        private boolean flagged = false;
    }

    /**
     * Representa un registro de evidencia
     */
    @Getter
    @Setter
    public static class EvidenceRecord {
        private String id;
        private String type; // MODEL_LINEAGE, PROMPT_VERSION, DECISION_AUDIT, DATASET_VERSION
        private String entityType;
        private String entityName;
        private String description;
        private Timestamp timestamp;
        private String hash; // Hash SHA-256 para integridad
        private boolean immutable = true;
    }
}
