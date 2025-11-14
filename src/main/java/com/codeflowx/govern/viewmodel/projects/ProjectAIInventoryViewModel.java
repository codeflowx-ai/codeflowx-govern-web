package com.codeflowx.govern.viewmodel.projects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nocode.services.entitys.projects.PRJProject;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProjectAIInventoryViewModel {

    private static final List<String> AI_SYSTEM_TYPES =
            Collections.unmodifiableList(Arrays.asList("MODEL", "AGENT", "RAG", "PROMPT", "HYBRID"));

    private static final List<String> AI_LIFECYCLE_STAGES =
            Collections.unmodifiableList(Arrays.asList("DESIGN", "DEVELOPMENT", "TESTING", "DEPLOYMENT",
                    "MONITORING", "DECOMMISSIONED"));

    private static final List<String> AI_RISK_LEVELS =
            Collections.unmodifiableList(Arrays.asList("HIGH_RISK", "LIMITED_RISK", "MINIMAL_RISK", "PROHIBITED"));

    @PersistenceContext
    private EntityManager entityManager;

    private Long projectId;
    private PRJProject projectInventory;

    @Init
    public void init(@ExecutionArgParam("projectId") Object projectIdArg,
                     @ExecutionParam("projectId") String projectIdParam) {
        this.projectId = extractProjectId(projectIdArg != null ? projectIdArg : projectIdParam);
        loadInventory();
    }

    @Command
    @NotifyChange("projectInventory")
    public void refreshInventory() {
        loadInventory();
        Clients.showNotification("Metadatos de inventario recargados.", "info", null, "top_center", 3000);
    }

    @Command
    @Transactional
    @NotifyChange("projectInventory")
    public void saveInventory() {
        if (!ensureInventoryLoaded()) {
            return;
        }
        entityManager.merge(projectInventory);
        Clients.showNotification("Inventario ISO 42001 guardado correctamente.", "info", null, "top_center", 3000);
    }

    @Command
    @Transactional
    @NotifyChange("projectInventory")
    public void reviewInventory() {
        if (!ensureInventoryLoaded()) {
            return;
        }
        Timestamp now = Timestamp.from(Instant.now());
        projectInventory.setPrjailastinventoryreview(now);
        projectInventory.setPrjainextinventoryreview(calculateNextReview(projectInventory.getPrjailifecyclestage()));
        entityManager.merge(projectInventory);
        Clients.showNotification("Revisión de inventario registrada.", "info", null, "top_center", 3000);
    }

    public List<String> getSystemTypes() {
        return AI_SYSTEM_TYPES;
    }

    public List<String> getLifecycleStages() {
        return AI_LIFECYCLE_STAGES;
    }

    public List<String> getRiskLevels() {
        return AI_RISK_LEVELS;
    }

    public long getDaysToNextReview() {
        if (projectInventory == null || projectInventory.getPrjainextinventoryreview() == null) {
            return -1;
        }
        Instant next = projectInventory.getPrjainextinventoryreview().toInstant();
        long days = ChronoUnit.DAYS.between(Instant.now(), next);
        return Math.max(days, 0);
    }

    private void loadInventory() {
        if (projectId == null) {
            projectInventory = null;
            return;
        }
        projectInventory = entityManager.find(PRJProject.class, projectId);
        if (projectInventory == null) {
            projectInventory = new PRJProject();
            projectInventory.setIdxproject(projectId);
            projectInventory.setPrjailifecyclestage("DESIGN");
        }
    }

    private Timestamp calculateNextReview(String lifecycleStage) {
        long months;
        if (lifecycleStage == null) {
            months = 3;
        } else {
            switch (lifecycleStage) {
                case "DESIGN":
                case "DEVELOPMENT":
                    months = 1;
                    break;
                case "TESTING":
                    months = 2;
                    break;
                case "DEPLOYMENT":
                    months = 3;
                    break;
                case "MONITORING":
                    months = 6;
                    break;
                case "DECOMMISSIONED":
                    months = 12;
                    break;
                default:
                    months = 3;
            }
        }
        Instant next = Instant.now().plus(months, ChronoUnit.MONTHS);
        return Timestamp.from(next);
    }

    private boolean ensureInventoryLoaded() {
        if (projectInventory == null || projectInventory.getIdxproject() == null) {
            Clients.showNotification(
                    "Seleccione un proyecto existente antes de actualizar el inventario ISO 42001.",
                    "warning",
                    null,
                    "top_center",
                    3500);
            return false;
        }
        return true;
    }

    private Long extractProjectId(Object projectIdArg) {
        if (projectIdArg instanceof Long) {
            return (Long) projectIdArg;
        }
        if (projectIdArg instanceof Number) {
            return ((Number) projectIdArg).longValue();
        }
        if (projectIdArg instanceof String) {
            String value = ((String) projectIdArg).trim();
            if (!value.isEmpty()) {
                try {
                    return Long.valueOf(value);
                } catch (NumberFormatException ignored) {
                    Clients.showNotification("El identificador de proyecto proporcionado no es válido.",
                            "warning",
                            null,
                            "top_center",
                            4000);
                }
            }
        }
        return null;
    }
}
