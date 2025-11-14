package com.codeflowx.govern.viewmodel.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.business.models.ModelAdaptationBusinessService;
import com.codeflowx.govern.business.models.ModelAdaptationBusinessService.AdaptationRecommendation;
import com.codeflowx.govern.business.models.ModelAdaptationBusinessService.AdaptationRequest;
import com.codeflowx.govern.business.models.ModelAdaptationBusinessService.AdapterConfig;
import com.codeflowx.govern.business.models.ModelAdaptationBusinessService.StrategyAlternative;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelAdaptationStrategy;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para recomendaciones de adaptación de modelos (Adapters, Fine-Tuning, Quantization, Merge)
 * Cumple con EU AI Act Art. 51-55 - GPAI Downstream Providers
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ModelAdaptationRecommendationViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_PRIORITY = "cost";

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private ModelAdaptationBusinessService adaptationBusinessService;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    private List<Model> baseModels = new ArrayList<>();
    private Model selectedBaseModel;
    private Long selectedBaseModelId;
    private List<String> selectedHardware = new ArrayList<>();

    private AdaptationForm form = new AdaptationForm();
    private AdaptationRecommendation recommendation;
    private StrategyAlternative selectedAlternative;
    private boolean recommendationVisible = false;
    private boolean saving = false;

    private List<String> priorityOptions = List.of("cost", "time", "co2", "performance");

    protected void initDao() {
        if (businessService == null && environment != null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {}

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        initDao();
        loadBaseModels();
        form.setPriority(DEFAULT_PRIORITY);
    }

    private void loadBaseModels() {
        try {
            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            Map<String, Object> filters = new HashMap<>();
            filters.put("modstatus", "ACTIVE");

            PageResult<Model> result = businessService.findAllEntity(Model.class, params, filters);
            if (result != null && result.getContent() != null) {
                baseModels = result.getContent();
            } else {
                baseModels = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error cargando modelos base", e);
            baseModels = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange({"recommendation", "selectedAlternative", "recommendationVisible"})
    public void generateRecommendation() {
        try {
            AdaptationRequest request = buildRequest();
            log.info("Solicitando recomendación adaptación para modelo base={} prioridad={}", selectedBaseModelId, form.getPriority());
            recommendation = adaptationBusinessService.recommendStrategy(request);
            recommendationVisible = true;
            if (recommendation != null && recommendation.getAlternativesRanked() != null && !recommendation.getAlternativesRanked().isEmpty()) {
                selectedAlternative = recommendation.getAlternativesRanked().get(0);
            }
        } catch (Exception e) {
            log.error("Error generando recomendación", e);
            recommendationVisible = false;
            Messagebox.show("No fue posible obtener la recomendación: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("selectedAlternative")
    public void selectAlternative(@org.zkoss.bind.annotation.BindingParam("alternative") StrategyAlternative alternative) {
        if (alternative != null) {
            selectedAlternative = alternative;
        }
    }

    @Command
    @NotifyChange("saving")
    public void saveStrategy() {
        if (selectedBaseModelId == null) {
            Messagebox.show("Selecciona un modelo base para registrar la estrategia.", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        if (recommendation == null) {
            Messagebox.show("Genera una recomendación antes de guardar.", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        saving = true;
        try {
            AdaptationRequest request = buildRequest();
            String selectedStrategy = selectedAlternative != null ? selectedAlternative.getStrategy() : recommendation.getRecommendedStrategy();
            String reason = form.getSelectionReason();
            ModelAdaptationStrategy strategy = adaptationBusinessService.saveStrategy(request, recommendation, selectedStrategy, reason);
            log.info("Estrategia de adaptación registrada: {}", strategy.getIdxadaptationstrategy());
            Messagebox.show("Estrategia registrada correctamente.", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error guardando estrategia adaptación", e);
            Messagebox.show("Error guardando estrategia: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        } finally {
            saving = false;
        }
    }

    private AdaptationRequest buildRequest() {
        AdaptationRequest request = new AdaptationRequest();
        request.setProjectId(form.getProjectId());
        request.setUseCase(form.getUseCase());
        request.setTargetTask(form.getTargetTask());
        request.setDatasetSize(form.getDatasetSize());
        request.setBudgetUsd(form.getBudgetUsd());
        request.setTimeDays(form.getTimeDays());
        request.setTargetPerformance(form.getTargetPerformance());
        request.setPriority(form.getPriority());
        request.setCreatedBy(ctxBean != null && ctxBean.getUser() != null ? ctxBean.getUser().getUsuname() : "SYSTEM");
        request.setBaseModelOptions(resolveBaseModelOptions());
        if (selectedHardware != null && !selectedHardware.isEmpty()) {
            request.setHardware(new ArrayList<>(selectedHardware));
        } else {
            request.setHardware(Collections.emptyList());
        }
        return request;
    }

    private List<String> resolveBaseModelOptions() {
        if (selectedBaseModel != null) {
            return List.of(selectedBaseModel.getModname());
        }
        if (selectedBaseModelId == null) {
            return form.getBaseModelOptions();
        }
        return baseModels.stream()
            .filter(model -> Objects.equals(model.getIdxmodel(), selectedBaseModelId))
            .map(Model::getModname)
            .findFirst()
            .map(List::of)
            .orElse(form.getBaseModelOptions());
    }

    @Command
    @NotifyChange("selectedHardware")
    public void toggleHardware(@BindingParam("value") String value, @BindingParam("checked") boolean checked) {
        if (value == null) {
            return;
        }
        if (checked) {
            if (!selectedHardware.contains(value)) {
                selectedHardware.add(value);
            }
        } else {
            selectedHardware.remove(value);
        }
    }

    public void setSelectedBaseModel(Model selectedBaseModel) {
        this.selectedBaseModel = selectedBaseModel;
        this.selectedBaseModelId = selectedBaseModel != null ? selectedBaseModel.getIdxmodel() : null;
    }

    @Command
    public void preloadAdapterConfig() {
        if (recommendation == null) {
            return;
        }
        AdapterConfig adapterConfig = form.getAdapterConfig();
        if (adapterConfig == null) {
            adapterConfig = new AdapterConfig();
            form.setAdapterConfig(adapterConfig);
        }
        adapterConfig.setAdapterType("ADAPTER_LORA");
        adapterConfig.setRank(16);
        adapterConfig.setAlpha(32);
        adapterConfig.setTargetModules(List.of("q_proj", "v_proj"));
        adapterConfig.setDropout(0.1);
    }

    @Getter
    @Setter
    public static class AdaptationForm {
        private Long projectId;
        private String useCase;
        private String targetTask;
        private Integer datasetSize = 10000;
        private BigDecimal budgetUsd = new BigDecimal("500");
        private Integer timeDays = 7;
        private BigDecimal targetPerformance = new BigDecimal("0.85");
        private String priority = DEFAULT_PRIORITY;
        private List<String> baseModelOptions = new ArrayList<>();
        private String selectionReason;
        private AdapterConfig adapterConfig = new AdapterConfig();
    }
}
