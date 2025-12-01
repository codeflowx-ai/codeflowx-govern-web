package com.codeflowx.govern.viewmodel.governance;
import com.codeflowx.framework.zkoss.BaseFront;

import com.codeflowx.govern.business.governance.AICompetenceBusinessService;
import com.codeflowx.govern.business.governance.AICompetenceBusinessService.CompetenceDashboard;
import com.codeflowx.govern.business.governance.AICompetenceBusinessService.GapAnalysisResult;
import com.codeflowx.govern.entity.governance.AICCompetence;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.governance.AITTrainingRecord;
import com.codeflowx.govern.service.governance.AITTrainingRecordService;
import com.codeflowx.govern.service.governance.AICCompetenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
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
import org.zkoss.zul.Messagebox;
import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel de gestión de competencias y awareness ISO 42001 Clauses 7.2/7.3.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AICompetenceViewModel extends BaseFront<AICompetenceViewModel>{

    private static final long serialVersionUID = 1L;

    @WireVariable
    private ModelService modelService;

    @WireVariable
    private AICompetenceBusinessService competenceBusinessService;
    @WireVariable
    private AICCompetenceService aICCompetenceService;
    @WireVariable
    private AITTrainingRecordService aITTrainingRecordService;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<AICCompetence> competences = new ArrayList<>();
    private AICCompetence selectedCompetence;

    private List<String> missingCompetencies = new ArrayList<>();
    private List<String> extraCompetencies = new ArrayList<>();
    private String gapAnalysisJson;

    private boolean gapModalVisible;
    private boolean planModalVisible;
    private boolean trainingModalVisible;

    private String planOwner;
    private Timestamp planTargetDate;
    private String planGapsInput;

    private AITTrainingRecord trainingRecordForm;
    private List<AITTrainingRecord> trainingRecords = new ArrayList<>();

    private CompetenceDashboard dashboard = CompetenceDashboard.empty();

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
    @NotifyChange({"competences", "dashboard"})
    public void refreshData() {
        loadData();
        Messagebox.show("Datos actualizados correctamente", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    @NotifyChange({"selectedCompetence", "trainingRecords", "missingCompetencies", "extraCompetencies"})
    public void selectCompetence(@BindingParam("competence") AICCompetence competence) {
        selectedCompetence = competence;
        if (selectedCompetence != null) {
            reloadTrainingRecords();
            parseExistingGapAnalysis();
        } else {
            trainingRecords = new ArrayList<>();
            missingCompetencies = new ArrayList<>();
            extraCompetencies = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange({"gapModalVisible", "missingCompetencies", "extraCompetencies", "gapAnalysisJson", "selectedCompetence"})
    public void openGapAnalysis(@BindingParam("competence") AICCompetence competence) {
        if (competence == null) {
            Messagebox.show("Selecciona a un profesional para evaluar gaps", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        GapAnalysisResult result = competenceBusinessService.assessCompetenceGap(competence.getIdxaiccompetence());
        selectedCompetence = result.getCompetence();
        missingCompetencies = new ArrayList<>(result.getMissing());
        extraCompetencies = new ArrayList<>(result.getExtra());
        gapAnalysisJson = selectedCompetence.getAicgapanalysis();
        gapModalVisible = true;
    }

    @Command
    @NotifyChange("gapModalVisible")
    public void closeGapModal() {
        gapModalVisible = false;
    }

    @Command
    @NotifyChange({"planModalVisible", "planOwner", "planTargetDate", "planGapsInput"})
    public void openPlanModal(@BindingParam("competence") AICCompetence competence) {
        if (competence == null) {
            Messagebox.show("Selecciona a un profesional", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        selectedCompetence = competence;
        planOwner = competence.getAicpersonname();
        planTargetDate = Timestamp.from(Instant.now().plusSeconds(60L * 60 * 24 * 30));
        planGapsInput = String.join(", ", missingCompetencies.isEmpty() ? fetchMissingFromJson(competence.getAicgapanalysis()) : missingCompetencies);
        planModalVisible = true;
    }

    @Command
    @NotifyChange({"planModalVisible", "selectedCompetence"})
    public void generateTrainingPlan() {
        if (selectedCompetence == null) {
            return;
        }
        List<String> gaps = sanitizeList(planGapsInput);
        competenceBusinessService.createTrainingPlan(
            selectedCompetence.getIdxaiccompetence(),
            gaps,
            planOwner,
            planTargetDate
        );
        planModalVisible = false;
        loadData();
        Messagebox.show("Plan de formación generado", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    @NotifyChange({"planModalVisible"})
    public void cancelPlan() {
        planModalVisible = false;
    }

    @Command
    @NotifyChange({"trainingModalVisible", "trainingRecordForm"})
    public void openTrainingModal(@BindingParam("competence") AICCompetence competence) {
        if (competence == null) {
            Messagebox.show("Selecciona a un profesional", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        selectedCompetence = competence;
        trainingRecordForm = new AITTrainingRecord();
        trainingRecordForm.setAittrainingtype("AWARENESS");
        trainingRecordForm.setAittrainingdate(Timestamp.from(Instant.now()));
        trainingRecordForm.setAitcertificateobtained(Boolean.FALSE);
        trainingModalVisible = true;
    }

    @Command
    @NotifyChange({"trainingModalVisible", "trainingRecords"})
    public void saveTrainingRecord() {
        if (selectedCompetence == null || trainingRecordForm == null) {
            return;
        }
        try {
            competenceBusinessService.recordTraining(selectedCompetence.getIdxaiccompetence(), trainingRecordForm);
            trainingModalVisible = false;
            reloadTrainingRecords();
            loadData(); // refrescar awareness actualizado
            Messagebox.show("Registro de formación guardado", "ISO 42001", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error guardando training record", e);
            Messagebox.show("Error guardando formación: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("trainingModalVisible")
    public void cancelTrainingRecord() {
        trainingModalVisible = false;
        trainingRecordForm = null;
    }

    private void loadData() {
        try {
            competences = new ArrayList<>(competenceBusinessService.findAllCompetences());
            dashboard = competenceBusinessService.generateCompetenceReport();
            if (selectedCompetence != null) {
                selectedCompetence = competences.stream()
                    .filter(c -> c.getIdxaiccompetence().equals(selectedCompetence.getIdxaiccompetence()))
                    .findFirst()
                    .orElse(null);
                reloadTrainingRecords();
            }
        } catch (Exception e) {
            log.error("Error cargando matriz de competencias", e);
            competences = new ArrayList<>();
            dashboard = CompetenceDashboard.empty();
        }
    }

    private void reloadTrainingRecords() {
        if (selectedCompetence == null) {
            trainingRecords = new ArrayList<>();
            return;
        }
        trainingRecords = new ArrayList<>(competenceBusinessService.findTrainingRecords(selectedCompetence.getIdxaiccompetence()));
    }

    private void parseExistingGapAnalysis() {
        missingCompetencies = fetchMissingFromJson(selectedCompetence != null ? selectedCompetence.getAicgapanalysis() : null);
        extraCompetencies = fetchExtrasFromJson(selectedCompetence != null ? selectedCompetence.getAicgapanalysis() : null);
    }

    public List<String> fetchMissingFromJson(String json) {
        return fetchListFromJson(json, "missing");
    }

    public List<String> fetchExtrasFromJson(String json) {
        return fetchListFromJson(json, "extras");
    }

    private List<String> fetchListFromJson(String json, String field) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode array = node.get(field);
            if (array != null && array.isArray()) {
                List<String> values = new ArrayList<>();
                array.forEach(item -> {
                    if (item != null && item.isTextual()) {
                        values.add(item.asText());
                    }
                });
                return values;
            }
        } catch (JsonProcessingException e) {
            log.debug("JSON inválido ({}) {}", field, e.getMessage());
        }
        return new ArrayList<>();
    }

    private List<String> sanitizeList(String input) {
        if (StringUtils.isBlank(input)) {
            return Collections.emptyList();
        }
        LinkedHashSet<String> ordered = java.util.Arrays.stream(input.split(","))
            .map(String::trim)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(ordered);
    }

    @Destroy
    public void destroy() {
        competences = null;
        selectedCompetence = null;
        missingCompetencies = null;
        extraCompetencies = null;
        trainingRecords = null;
        trainingRecordForm = null;
        dashboard = null;
        aICCompetenceService = null;
            aITTrainingRecordService = null;
        competenceBusinessService = null;
    }
}


