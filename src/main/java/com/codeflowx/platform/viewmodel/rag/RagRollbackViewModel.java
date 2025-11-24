package com.codeflowx.platform.viewmodel.rag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.rag.RagRollback;
import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.entity.rag.RagVersion;
import com.codeflowx.govern.service.rag.RagSystemService;
import com.codeflowx.govern.service.rag.RagVersionService;
import com.codeflowx.govern.service.rag.RagRollbackService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class RagRollbackViewModel extends BaseFront<RagRollbackViewModel> {
    private static final long serialVersionUID = 1L;

    @org.zkoss.zk.ui.select.annotation.WireVariable
    private RagSystemService ragSystemService;

    @org.zkoss.zk.ui.select.annotation.WireVariable
    private RagVersionService ragVersionService;

    @org.zkoss.zk.ui.select.annotation.WireVariable
    private RagRollbackService ragRollbackService;

    @Override
    public void setBeans(Object bean) {}

    // Paginación
    private PageParams pageParams;
    private PageResult<RagRollback> pageResult;

    // Filtros
    private String searchText = "";
    private String filterRagSystem = "";
    private String filterStatus = "";
    private String filterImpact = "";

    // Datos
    private List<RagRollback> rollbacksList = new ArrayList<>();
    private List<RagSystem> ragSystemsList = new ArrayList<>();
    private List<RagVersion> availableVersions = new ArrayList<>();

    // Métricas
    private int totalRollbacks = 0;
    private int successRollbacks = 0;
    private int failedRollbacks = 0;
    private int inProgressRollbacks = 0;

    // Modal
    private boolean showModal = false;
    private Long rollbackRagSystemId;
    private String currentVersion;
    private String rollbackToVersion;
    private String rollbackReason;
    private String rollbackImpact = "MEDIUM";
    private boolean showImpactWarning = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadRagSystems();
        loadRollbacks();
        loadMetrics();
    }

    @Command
    @NotifyChange("*")
    public void loadRagSystems() {
        try {
            PageParams params = PageParams.builder().maxRows(100).pageActual(1).build();
            PageResult<RagSystem> result = ragSystemService.findAll(params);

            if (result != null && result.getContent() != null) {
                ragSystemsList = result.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar sistemas RAG", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadRollbacks() {
        try {
            Criterias criterias = buildCriterias();

            pageResult = ragRollbackService.findAll(
                pageParams,
                criterias
            );

            if (pageResult != null && pageResult.getContent() != null) {
                rollbacksList = pageResult.getContent();
                totalRollbacks = pageResult.getTotalRows();

                // Auditar búsqueda
                logActivity("BUSCAR", "RAGROLLBACKS", null,
                    "Búsqueda rollbacks: " + rollbacksList.size() + " resultados");

                log.info("Cargados {} rollbacks", rollbacksList.size());
            } else {
                rollbacksList = new ArrayList<>();
                totalRollbacks = 0;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar rollbacks", e);
            Messagebox.show("Error al cargar rollbacks: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        try {
            successRollbacks = (int) rollbacksList.stream()
                .filter(r -> "SUCCESS".equals(r.getRagrstatus()))
                .count();

            failedRollbacks = (int) rollbacksList.stream()
                .filter(r -> "FAILED".equals(r.getRagrstatus()))
                .count();

            inProgressRollbacks = (int) rollbacksList.stream()
                .filter(r -> "IN_PROGRESS".equals(r.getRagrstatus()))
                .count();
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (filterRagSystem != null && !filterRagSystem.trim().isEmpty()) {
            try {
                Long systemId = Long.parseLong(filterRagSystem);
                criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragSystem.idxragsystem", systemId));
            } catch (NumberFormatException e) {
                log.warn("Invalid RAG system ID filter: {}", filterRagSystem);
            }
        }

        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragrstatus", filterStatus));
        }

        if (filterImpact != null && !filterImpact.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragrimpact", filterImpact));
        }

        return criterias;
    }

    @Command
    @NotifyChange("*")
    public void searchRollbacks() {
        pageParams.setPageActual(1);
        loadRollbacks();
        loadMetrics();
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadRollbacks();
        loadMetrics();
    }

    @Command
    @NotifyChange("*")
    public void refreshRollbacks() {
        loadRollbacks();
        loadMetrics();
    }

    @Command
    @NotifyChange("*")
    public void showInitiateModal() {
        clearForm();
        showModal = true;
    }

    @Command
    @NotifyChange("*")
    public void loadVersions() {
        try {
            if (rollbackRagSystemId == null) {
                availableVersions = new ArrayList<>();
                currentVersion = null;
                return;
            }

            // Cargar sistema actual
            RagSystem system = ragSystemService.findById(rollbackRagSystemId);
            if (system != null) {
                currentVersion = system.getRagversion();
            }

            // Cargar versiones disponibles
            Criterias criterias = new Criterias();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragSystem.idxragsystem", rollbackRagSystemId));
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.NOT_EQUALS, "ragvstatus", "FAILED"));

            PageParams params = PageParams.builder().maxRows(50).pageActual(1).build();
            PageResult<RagVersion> result = ragVersionService.findAll(params, criterias);

            if (result != null && result.getContent() != null) {
                availableVersions = result.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar versiones", e);
        }
    }

    @Command
    @NotifyChange("showImpactWarning")
    public void checkImpact() {
        showImpactWarning = "HIGH".equals(rollbackImpact);
    }

    @Command
    @NotifyChange("*")
    public void initiateRollback() {
        try {
            // Validaciones
            if (rollbackRagSystemId == null) {
                Messagebox.show("Debe seleccionar un sistema RAG", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (rollbackToVersion == null || rollbackToVersion.trim().isEmpty()) {
                Messagebox.show("Debe seleccionar una versión destino", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (rollbackReason == null || rollbackReason.trim().isEmpty()) {
                Messagebox.show("Debe especificar la razón del rollback", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            // Crear rollback
            RagRollback rollback = new RagRollback();

            RagSystem ragSystem = ragSystemService.findById(rollbackRagSystemId);
            if (ragSystem == null) {
                Messagebox.show("Sistema RAG no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            rollback.setRagSystem(ragSystem);
            rollback.setRagrfromversion(currentVersion);
            rollback.setRagrtoversion(rollbackToVersion);
            rollback.setRagrreason(rollbackReason);
            rollback.setRagrimpact(rollbackImpact);
            rollback.setRagrstatus("IN_PROGRESS");
            rollback.setRagrinitiatedat(new Timestamp(System.currentTimeMillis()));
            rollback.setRagrinitiatedby("System"); // TODO: Obtener usuario actual

            businessService.save(rollback);

            // Auditar - INTEGRACIÓN CON GOBIERNO
            String auditDescription = String.format(
                "Rollback iniciado: %s de versión %s a %s. Impacto: %s. Razón: %s",
                ragSystem.getRagsystemname(),
                currentVersion,
                rollbackToVersion,
                rollbackImpact,
                rollbackReason
            );

            logActivity("CREAR", "RAGROLLBACKS", rollback.getIdxragrollback(), auditDescription);

            // Si es de alto impacto, registrar en gobierno
            if ("HIGH".equals(rollbackImpact)) {
                logActivity("ALERTAR", "GOVERNANCE", rollback.getIdxragrollback(),
                    "ALTO IMPACTO: " + auditDescription);
            }

            showModal = false;
            loadRollbacks();
            loadMetrics();

            // TODO: Integración con leka-server para ejecutar rollback real
            Messagebox.show(
                "Rollback iniciado exitosamente.\n\n" +
                "NOTA: La ejecución real del rollback requiere integración con leka-server.\n" +
                "El estado se actualizará cuando el proceso complete.",
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);

        } catch (Exception e) {
            log.error("Error al iniciar rollback", e);
            Messagebox.show("Error al iniciar rollback: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void closeModal() {
        showModal = false;
        clearForm();
    }

    @Command
    public void viewRollback(@BindingParam("rollback") RagRollback rollback) {
        log.info("Ver rollback: {} -> {}", rollback.getRagrfromversion(), rollback.getRagrtoversion());
        // TODO: Navegar a vista detallada
    }

    @Command
    @NotifyChange("*")
    public void cancelRollback(@BindingParam("rollback") RagRollback rollback) {
        if (!"IN_PROGRESS".equals(rollback.getRagrstatus())) {
            Messagebox.show("Solo se pueden cancelar rollbacks en progreso",
                "Info", Messagebox.OK, Messagebox.INFORMATION);
            return;
        }

        Messagebox.show("¿Está seguro de cancelar este rollback?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        rollback.setRagrstatus("CANCELLED");
                        rollback.setRagrcompletedat(new Timestamp(System.currentTimeMillis()));
                        businessService.save(rollback);

                        // Auditar
                        logActivity("EDITAR", "RAGROLLBACKS", rollback.getIdxragrollback(),
                            "Rollback cancelado: " + rollback.getRagrfromversion() + " -> " + rollback.getRagrtoversion());

                        loadRollbacks();
                        loadMetrics();

                        Messagebox.show("Rollback cancelado",
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al cancelar rollback", e);
                        Messagebox.show("Error: " + e.getMessage(),
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    private void clearForm() {
        rollbackRagSystemId = null;
        currentVersion = null;
        rollbackToVersion = null;
        rollbackReason = null;
        rollbackImpact = "MEDIUM";
        showImpactWarning = false;
        availableVersions = new ArrayList<>();
    }

    public String getRagSystemName(RagSystem system) {
        return system != null ? system.getRagsystemname() : "-";
    }

    public String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length) + "..." : text;
    }

    public String formatDuration(Integer seconds) {
        if (seconds == null) return "-";
        if (seconds < 60) return seconds + "s";
        int minutes = seconds / 60;
        if (minutes < 60) return minutes + "m";
        int hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m";
    }

    public String getStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "SUCCESS": return "badge bg-success";
            case "FAILED": return "badge bg-danger";
            case "IN_PROGRESS": return "badge bg-warning";
            case "CANCELLED": return "badge bg-secondary";
            default: return "badge bg-secondary";
        }
    }

    public String getImpactColor(String impact) {
        if (impact == null) return "badge bg-secondary";
        switch (impact) {
            case "HIGH": return "badge bg-danger";
            case "MEDIUM": return "badge bg-warning";
            case "LOW": return "badge bg-success";
            default: return "badge bg-secondary";
        }
    }

    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }

    @Destroy
    public void destroy() {
        if (rollbacksList != null) {
            rollbacksList.clear();
            rollbacksList = null;
        }
        if (ragSystemsList != null) {
            ragSystemsList.clear();
            ragSystemsList = null;
        }
        if (availableVersions != null) {
            availableVersions.clear();
            availableVersions = null;
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
