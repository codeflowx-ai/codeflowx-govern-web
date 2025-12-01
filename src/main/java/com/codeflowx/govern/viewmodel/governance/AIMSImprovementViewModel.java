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
import nocode.services.entitys.governance.AIMImprovement;
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
public class AIMSImprovementViewModel extends BaseFront<AIMSImprovementViewModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private List<AIMImprovement> improvements;
    private AIMImprovement selectedImprovement;
    private AIMImprovement newImprovement;

    @Init
    public void init() {
        loadImprovements();
        newImprovement = createDefaultImprovement();
    }

    @Command
    @NotifyChange({"improvements", "selectedImprovement"})
    public void loadImprovements() {
        improvements = entityManager.createQuery(
                        "FROM AIMImprovement i ORDER BY i.aimduedate ASC NULLS LAST, i.aimcreatedat DESC",
                        AIMImprovement.class)
                .getResultList();
        if (!improvements.isEmpty()) {
            selectedImprovement = improvements.get(0);
        }
    }

    @Command
    @Transactional
    @NotifyChange({"improvements", "newImprovement"})
    public void createImprovement() {
        if (!validateImprovement(newImprovement)) {
            return;
        }
        newImprovement.setIduuid(UUID.randomUUID().toString());
        newImprovement.setAimcreatedat(Timestamp.from(Instant.now()));
        entityManager.persist(newImprovement);
        Clients.showNotification("Plan de mejora registrado", "info", null, "top_center", 3000);
        loadImprovements();
        newImprovement = createDefaultImprovement();
    }

    @Command
    @Transactional
    @NotifyChange("improvements")
    public void updateImprovement(@BindingParam("improvement") AIMImprovement improvement) {
        if (improvement == null) {
            Clients.showNotification("Seleccione un plan para actualizar", "warning", null, "top_center", 3000);
            return;
        }
        improvement.setAimupdatedat(Timestamp.from(Instant.now()));
        entityManager.merge(improvement);
        Clients.showNotification("Plan de mejora actualizado", "info", null, "top_center", 3000);
        loadImprovements();
    }

    @Command
    @Transactional
    @NotifyChange("improvements")
    public void markCompleted(@BindingParam("improvement") AIMImprovement improvement) {
        if (improvement == null) {
            Clients.showNotification("Seleccione un plan", "warning", null, "top_center", 3000);
            return;
        }
        improvement.setAimstatus("COMPLETED");
        improvement.setAimcompletiondate(Timestamp.from(Instant.now()));
        improvement.setAimupdatedat(Timestamp.from(Instant.now()));
        entityManager.merge(improvement);
        Clients.showNotification("Plan de mejora completado", "info", null, "top_center", 3000);
        loadImprovements();
    }

    @Command
    @Transactional
    @NotifyChange("improvements")
    public void deleteImprovement(@BindingParam("improvement") AIMImprovement improvement) {
        if (improvement == null) {
            Clients.showNotification("Seleccione un registro para eliminar", "warning", null, "top_center", 3000);
            return;
        }
        entityManager.remove(entityManager.contains(improvement) ? improvement : entityManager.merge(improvement));
        Clients.showNotification("Plan de mejora eliminado", "info", null, "top_center", 3000);
        loadImprovements();
    }

    public long getCompletedCount() {
        return improvements == null ? 0 : improvements.stream()
                .filter(i -> "COMPLETED".equalsIgnoreCase(i.getAimstatus()))
                .count();
    }

    private boolean validateImprovement(AIMImprovement improvement) {
        if (improvement == null) {
            Clients.showNotification("Registro inválido", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(improvement.getAimtitle())) {
            Clients.showNotification("El título es obligatorio", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(improvement.getAimdescription())) {
            Clients.showNotification("La descripción es obligatoria", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(improvement.getAimstatus())) {
            improvement.setAimstatus("PLANNED");
        }
        if (isBlank(improvement.getAimpriority())) {
            improvement.setAimpriority("MEDIUM");
        }
        return true;
    }

    private AIMImprovement createDefaultImprovement() {
        AIMImprovement improvement = new AIMImprovement();
        improvement.setAimstatus("PLANNED");
        improvement.setAimpriority("MEDIUM");
        improvement.setAimstartdate(Timestamp.from(Instant.now()));
        improvement.setAimcreatedat(Timestamp.from(Instant.now()));
        return improvement;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
