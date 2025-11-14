package com.codeflowx.govern.viewmodel.governance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import nocode.services.entitys.governance.ICOISO42001Control;
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
public class ISO42001ControlsViewModel {

    @PersistenceContext
    private EntityManager entityManager;

    private List<ICOISO42001Control> controls;
    private ICOISO42001Control selectedControl;
    private ICOISO42001Control newControl;
    private String statusFilter;
    private String categoryFilter;

    @Init
    public void init() {
        loadControls();
        newControl = createDefaultControl();
    }

    @Command
    @NotifyChange({"controls", "selectedControl"})
    public void loadControls() {
        StringBuilder jpql = new StringBuilder("FROM ICOISO42001Control c WHERE 1=1");
        if (statusFilter != null && !statusFilter.isBlank()) {
            jpql.append(" AND c.icocontrolstatus = :status");
        }
        if (categoryFilter != null && !categoryFilter.isBlank()) {
            jpql.append(" AND c.icocontrolcategory = :category");
        }
        jpql.append(" ORDER BY c.icocontrolcode ASC");

        var query = entityManager.createQuery(jpql.toString(), ICOISO42001Control.class);
        if (statusFilter != null && !statusFilter.isBlank()) {
            query.setParameter("status", statusFilter);
        }
        if (categoryFilter != null && !categoryFilter.isBlank()) {
            query.setParameter("category", categoryFilter);
        }
        controls = query.getResultList();
        if (!controls.isEmpty()) {
            selectedControl = controls.get(0);
        }
    }

    @Command
    @NotifyChange("controls")
    public void filterControls() {
        loadControls();
    }

    @Command
    @Transactional
    @NotifyChange({"controls", "newControl"})
    public void createControl() {
        if (!validateControl(newControl)) {
            return;
        }
        newControl.setIduuid(UUID.randomUUID().toString());
        newControl.setIcocreatedat(Timestamp.from(Instant.now()));
        entityManager.persist(newControl);
        Clients.showNotification("Control ISO 42001 registrado", "info", null, "top_center", 3000);
        loadControls();
        newControl = createDefaultControl();
    }

    @Command
    @Transactional
    @NotifyChange("controls")
    public void updateControl(@BindingParam("control") ICOISO42001Control control) {
        if (control == null) {
            Clients.showNotification("Seleccione un control", "warning", null, "top_center", 3000);
            return;
        }
        control.setIcoupdatedat(Timestamp.from(Instant.now()));
        entityManager.merge(control);
        Clients.showNotification("Control actualizado", "info", null, "top_center", 3000);
        loadControls();
    }

    @Command
    @Transactional
    @NotifyChange("controls")
    public void deleteControl(@BindingParam("control") ICOISO42001Control control) {
        if (control == null) {
            Clients.showNotification("Seleccione un control para eliminar", "warning", null, "top_center", 3000);
            return;
        }
        entityManager.remove(entityManager.contains(control) ? control : entityManager.merge(control));
        Clients.showNotification("Control eliminado", "info", null, "top_center", 3000);
        loadControls();
    }

    public long getImplementedCount() {
        return controls == null ? 0 : controls.stream()
                .filter(c -> "IMPLEMENTED".equalsIgnoreCase(c.getIcocontrolstatus()))
                .count();
    }

    private boolean validateControl(ICOISO42001Control control) {
        if (control == null) {
            Clients.showNotification("Registro inválido", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(control.getIcocontrolcode())) {
            Clients.showNotification("El código del control es obligatorio", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(control.getIcocontroltitle())) {
            Clients.showNotification("El título es obligatorio", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(control.getIcocontroldescription())) {
            Clients.showNotification("La descripción es obligatoria", "error", null, "top_center", 3000);
            return false;
        }
        if (isBlank(control.getIcocontrolstatus())) {
            control.setIcocontrolstatus("PLANNED");
        }
        return true;
    }

    private ICOISO42001Control createDefaultControl() {
        ICOISO42001Control control = new ICOISO42001Control();
        control.setIcocontrolstatus("PLANNED");
        control.setIcocontrolcategory("GOVERNANCE");
        control.setIcocreatedat(Timestamp.from(Instant.now()));
        return control;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
