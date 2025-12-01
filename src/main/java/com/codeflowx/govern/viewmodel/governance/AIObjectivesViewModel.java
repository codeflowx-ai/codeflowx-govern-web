package com.codeflowx.govern.viewmodel.governance;
import com.codeflowx.framework.zkoss.BaseFront;

import com.codeflowx.govern.business.governance.AIObjectivesBusinessService;
import com.codeflowx.govern.business.governance.AIObjectivesBusinessService.AIObjectivesReport;
import com.codeflowx.govern.entity.governance.AIObjective;
import com.codeflowx.govern.service.governance.AIObjectiveService;
import com.codeflowx.govern.service.models.ModelService;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.enartframework.suinsit.Context;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para la gestión de objetivos IA (ISO/IEC 42001 Clause 6.2).
 * Provee CRUD, seguimiento de progreso y generación de KPIs.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AIObjectivesViewModel extends BaseFront<AIObjectivesViewModel>{

    private static final long serialVersionUID = 1L;

    @WireVariable
    private ModelService modelService;

    @WireVariable
    private AIObjectivesBusinessService objectivesBusinessService;
    @WireVariable
    private AIObjectiveService aIObjectiveService;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    private List<AIObjective> objectives = new ArrayList<>();
    private AIObjective selectedObjective;
    private AIObjective formObjective;
    private boolean formVisible;
    private boolean editMode;

    private BigDecimal progressValue = BigDecimal.ZERO;
    private boolean progressModalVisible;

    private String reviewDecision;
    private Timestamp reviewNextDate;
    private boolean reviewModalVisible;

    private AIObjectivesReport report = AIObjectivesReport.empty();

    private final List<String> categories = List.of("TRANSPARENCY", "FAIRNESS", "ROBUSTNESS", "PRIVACY", "SAFETY");
    private final List<String> statuses = List.of("ACTIVE", "ACHIEVED", "REVISED", "DISCONTINUED");
    private final List<String> frequencies = List.of("MONTHLY", "QUARTERLY", "ANNUAL");

    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }

    @Override
    public void setBeans(Object bean) {
        // no-op
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        loadData();
    }

    @Command
    @NotifyChange({"objectives", "report", "selectedObjective"})
    public void refreshData() {
        loadData();
        Messagebox.show("Objetivos actualizados correctamente", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
    }

    private void loadData() {
        try {
            objectives = new ArrayList<>(objectivesBusinessService.findAllObjectives());
            report = objectivesBusinessService.generateObjectivesReport();
            if (selectedObjective != null) {
                selectedObjective = objectives.stream()
                    .filter(o -> Objects.equals(o.getIdxaiobjective(), selectedObjective.getIdxaiobjective()))
                    .findFirst()
                    .orElse(null);
            }
        } catch (Exception e) {
            log.error("Error cargando objetivos IA", e);
            objectives = new ArrayList<>();
            report = AIObjectivesReport.empty();
        }
    }

    @Command
    @NotifyChange({"formVisible", "formObjective", "editMode"})
    public void openCreate() {
        formObjective = new AIObjective();
        formObjective.setAiostatus("ACTIVE");
        formObjective.setAioreviewfrequency("MONTHLY");
        formVisible = true;
        editMode = false;
    }

    @Command
    @NotifyChange({"formVisible", "formObjective", "editMode"})
    public void openEdit(@BindingParam("objective") AIObjective objective) {
        if (objective == null) {
            Messagebox.show("Selecciona un objetivo para editar", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        formObjective = cloneObjective(objective);
        formVisible = true;
        editMode = true;
    }

    @Command
    @NotifyChange({"formVisible", "formObjective", "objectives", "report", "selectedObjective"})
    public void saveObjective() {
        if (formObjective == null) {
            return;
        }
        try {
            if (formObjective.getAiotargetvalue() != null && formObjective.getAiotargetvalue().compareTo(BigDecimal.ZERO) <= 0) {
                Messagebox.show("El valor objetivo debe ser mayor a cero", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (editMode && formObjective.getIdxaiobjective() != null) {
                AIObjective updated = objectivesBusinessService.updateObjective(formObjective);
                Messagebox.show("Objetivo actualizado correctamente", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
                selectedObjective = updated;
            } else {
                if (formObjective.getAiocurrentvalue() == null) {
                    formObjective.setAiocurrentvalue(BigDecimal.ZERO);
                }
                formObjective.setAiocreatedat(Timestamp.from(Instant.now()));
                formObjective.setAioupdatedat(Timestamp.from(Instant.now()));
                AIObjective created = objectivesBusinessService.createObjective(formObjective);
                Messagebox.show("Objetivo creado correctamente", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
                selectedObjective = created;
            }
            formVisible = false;
            formObjective = null;
            editMode = false;
            loadData();
        } catch (Exception e) {
            log.error("Error guardando objetivo IA", e);
            Messagebox.show("Error guardando objetivo: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"formVisible", "formObjective", "editMode"})
    public void cancelForm() {
        formVisible = false;
        formObjective = null;
        editMode = false;
    }

    @Command
    @NotifyChange({"progressModalVisible", "progressValue", "selectedObjective"})
    public void openProgressModal(@BindingParam("objective") AIObjective objective) {
        selectedObjective = objective;
        if (selectedObjective == null) {
            Messagebox.show("Selecciona un objetivo para actualizar progreso", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        progressValue = selectedObjective.getAiocurrentvalue() != null ? selectedObjective.getAiocurrentvalue() : BigDecimal.ZERO;
        progressModalVisible = true;
    }

    @Command
    @NotifyChange({"progressModalVisible", "objectives", "report", "selectedObjective"})
    public void updateProgress() {
        if (selectedObjective == null) {
            return;
        }
        try {
            if (progressValue == null) {
                Messagebox.show("Ingresa un valor válido de progreso", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            objectivesBusinessService.updateProgress(selectedObjective.getIdxaiobjective(), progressValue);
            progressModalVisible = false;
            loadData();
            Messagebox.show("Progreso actualizado", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error actualizando progreso", e);
            Messagebox.show("Error actualizando progreso: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"progressModalVisible"})
    public void cancelProgress() {
        progressModalVisible = false;
        progressValue = null;
    }

    @Command
    @NotifyChange({"reviewModalVisible", "reviewDecision", "reviewNextDate", "selectedObjective"})
    public void openReviewModal(@BindingParam("objective") AIObjective objective) {
        selectedObjective = objective;
        if (selectedObjective == null) {
            Messagebox.show("Selecciona un objetivo para registrar la revisión", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        reviewDecision = selectedObjective.getAiostatus();
        reviewNextDate = selectedObjective.getAionextreview();
        reviewModalVisible = true;
    }

    @Command
    @NotifyChange({"reviewModalVisible", "objectives", "report", "selectedObjective"})
    public void submitReview() {
        if (selectedObjective == null) {
            return;
        }
        try {
            objectivesBusinessService.reviewObjective(selectedObjective.getIdxaiobjective(), reviewDecision, reviewNextDate);
            reviewModalVisible = false;
            loadData();
            Messagebox.show("Revisión registrada", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error registrando revisión", e);
            Messagebox.show("Error registrando revisión: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"reviewModalVisible"})
    public void cancelReview() {
        reviewModalVisible = false;
    }

    @Command
    @NotifyChange({"objectives", "report", "selectedObjective"})
    public void deleteObjective(@BindingParam("objective") AIObjective objective) {
        if (objective == null) {
            Messagebox.show("Selecciona un objetivo para eliminar", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        Messagebox.show("¿Eliminar objetivo '" + objective.getAiometric() + "'?", "Confirmar",
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
            (EventListener<ClickEvent>) event -> {
                if (Messagebox.Button.YES.equals(event.getButton())) {
                    try {
                        objectivesBusinessService.deleteObjective(objective.getIdxaiobjective());
                        if (selectedObjective != null && Objects.equals(selectedObjective.getIdxaiobjective(), objective.getIdxaiobjective())) {
                            selectedObjective = null;
                        }
                        loadData();
                    } catch (Exception ex) {
                        log.error("Error eliminando objetivo", ex);
                        Messagebox.show("Error eliminando objetivo: " + ex.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    private AIObjective cloneObjective(AIObjective source) {
        AIObjective copy = new AIObjective();
        copy.setIdxaiobjective(source.getIdxaiobjective());
        copy.setAiodescription(source.getAiodescription());
        copy.setAiocategory(source.getAiocategory());
        copy.setAiometric(source.getAiometric());
        copy.setAiotargetvalue(source.getAiotargetvalue());
        copy.setAiocurrentvalue(source.getAiocurrentvalue());
        copy.setAiostatus(source.getAiostatus());
        copy.setAioresponsible(source.getAioresponsible());
        copy.setAioreviewfrequency(source.getAioreviewfrequency());
        copy.setAiolastreviewed(source.getAiolastreviewed());
        copy.setAionextreview(source.getAionextreview());
        copy.setAiorelatedpolicy(source.getAiorelatedpolicy());
        copy.setAiocreatedat(source.getAiocreatedat());
        copy.setAioupdatedat(source.getAioupdatedat());
        return copy;
    }

    @Destroy
    public void destroy() {
        objectives = null;
        report = null;
        formObjective = null;
        selectedObjective = null;
        aIObjectiveService = null;
        objectivesBusinessService = null;
    }
}


