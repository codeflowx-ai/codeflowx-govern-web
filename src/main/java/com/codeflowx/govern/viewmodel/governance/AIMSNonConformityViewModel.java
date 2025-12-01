package com.codeflowx.govern.viewmodel.governance;
import com.codeflowx.framework.zkoss.BaseFront;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import nocode.services.entitys.governance.ANCNonConformity;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AIMSNonConformityViewModel extends BaseFront<AIMSNonConformityViewModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private List<ANCNonConformity> nonConformities;
    private ANCNonConformity selectedNonConformity;
    private ANCNonConformity newNonConformity;

    @Init
    public void init() {
        loadNonConformities();
        newNonConformity = createDefaultNonConformity();
    }

    @Command
    @NotifyChange({"nonConformities", "selectedNonConformity"})
    public void loadNonConformities() {
        nonConformities = entityManager.createQuery(
                        "FROM ANCNonConformity nc ORDER BY nc.ancduedate ASC NULLS LAST, nc.anccreatedat DESC",
                        ANCNonConformity.class)
                .getResultList();
        if (!nonConformities.isEmpty()) {
            selectedNonConformity = nonConformities.get(0);
        }
    }

    @Command
    @Transactional
    @NotifyChange({"nonConformities", "newNonConformity"})
    public void createNonConformity() {
        if (!validateNonConformity(newNonConformity)) {
            return;
        }
        newNonConformity.setIduuid(UUID.randomUUID().toString());
        newNonConformity.setAnccreatedat(Timestamp.from(Instant.now()));
        entityManager.persist(newNonConformity);
        Clients.showNotification("No conformidad registrada", "info", null, "top_center", 3000);
        loadNonConformities();
        newNonConformity = createDefaultNonConformity();
    }

    @Command
    @Transactional
    @NotifyChange("nonConformities")
    public void updateNonConformity(@BindingParam("record") ANCNonConformity record) {
        if (record == null) {
            Clients.showNotification("Seleccione un registro para actualizar", "warning", null, "top_center", 3000);
            return;
        }
        record.setAncupdatedat(Timestamp.from(Instant.now()));
        entityManager.merge(record);
        Clients.showNotification("No conformidad actualizada", "info", null, "top_center", 3000);
        loadNonConformities();
    }

    @Command
    @Transactional
    @NotifyChange("nonConformities")
    public void closeNonConformity(@BindingParam("record") ANCNonConformity record) {
        if (record == null) {
            Clients.showNotification("Seleccione un registro", "warning", null, "top_center", 3000);
            return;
        }
        record.setAncstatus("CLOSED");
        record.setAncclosuredate(Timestamp.from(Instant.now()));
        record.setAncupdatedat(Timestamp.from(Instant.now()));
        entityManager.merge(record);
        Clients.showNotification("No conformidad cerrada", "info", null, "top_center", 3000);
        loadNonConformities();
    }

    @Command
    @Transactional
    @NotifyChange("nonConformities")
    public void deleteNonConformity(@BindingParam("record") ANCNonConformity record) {
        if (record == null) {
            Clients.showNotification("Seleccione un registro para eliminar", "warning", null, "top_center", 3000);
            return;
        }
        entityManager.remove(entityManager.contains(record) ? record : entityManager.merge(record));
        Clients.showNotification("No conformidad eliminada", "info", null, "top_center", 3000);
        loadNonConformities();
    }

    public long getOpenCount() {
        return nonConformities == null ? 0 : nonConformities.stream()
                .filter(nc -> "OPEN".equalsIgnoreCase(nc.getAncstatus()) || "IN_PROGRESS".equalsIgnoreCase(nc.getAncstatus()))
                .count();
    }

    public long getCriticalCount() {
        return nonConformities == null ? 0 : nonConformities.stream()
                .filter(nc -> "CRITICAL".equalsIgnoreCase(nc.getAncseverity()))
                .count();
    }

    private boolean validateNonConformity(ANCNonConformity record) {
        if (record == null) {
            Clients.showNotification("Registro inválido", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(record.getAnctitle())) {
            Clients.showNotification("El título es obligatorio", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(record.getAncdescription())) {
            Clients.showNotification("La descripción es obligatoria", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(record.getAncseverity())) {
            record.setAncseverity("MEDIUM");
        }
        if (isBlank(record.getAncstatus())) {
            record.setAncstatus("OPEN");
        }
        return true;
    }

    private ANCNonConformity createDefaultNonConformity() {
        ANCNonConformity record = new ANCNonConformity();
        record.setAncseverity("MEDIUM");
        record.setAncstatus("OPEN");
        record.setAncdetectiondate(Timestamp.from(Instant.now()));
        record.setAnccreatedat(Timestamp.from(Instant.now()));
        return record;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
