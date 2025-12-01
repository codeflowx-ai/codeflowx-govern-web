package com.codeflowx.govern.viewmodel.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codeflowx.framework.zkoss.BaseFront;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.views.providers.ProvidersMetricsSummary;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.views.providers.ProvidersOverview;
import com.codeflowx.govern.service.providers.ProvidersOverviewService;
import com.codeflowx.govern.service.providers.ProvidersMetricsSummaryService;
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

/**
 * ViewModel para la pantalla de Overview de Proveedores de Modelos
 * Implementa el patrón establecido con Views SQL, Criterias API y navegación dinámica
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProvidersOverviewViewModel extends BaseFront<ProvidersOverviewViewModel> {

    private static final long serialVersionUID = 1L;

    private static final String IDDESKTOP = "contenedor";

    @WireVariable
    private ModelService modelService;

    @WireVariable
    private ProvidersOverviewService providersOverviewService;

    @WireVariable
    private ProvidersMetricsSummaryService providersMetricsSummaryService;

    @WireVariable
    public Environment environment;

    private Component view;

    // ========== Datos del grid ==========
    private List<ProvidersOverview> providers = new ArrayList<>();
    private ProvidersOverview selectedProvider;

    // ========== Filtros de búsqueda ==========
    private String searchTerm;
    private String selectedStatus;
    private String selectedProviderType;
    private Boolean selectedHasFreeTier;
    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<ProvidersOverview> pageResult;


    // ========== Métricas globales ==========
    private Long totalProviders = 0L;
    private Long activeProviders = 0L;
    private Long inactiveProviders = 0L;
    private Long totalModels = 0L;
    private Long totalCredentials = 0L;
    private Long supportsText = 0L;
    private Long supportsEmbeddings = 0L;

    // ========== Opciones de filtro ==========
    private List<Map<String, String>> statusOptions = new ArrayList<>();
    private List<Map<String, String>> providerTypeOptions = new ArrayList<>();
    private List<Map<String, Object>> freeTierOptions = new ArrayList<>();

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        log.info("Inicializando ProvidersOverviewViewModel");
        Selectors.wireComponents(view, this, false);
        this.view = view;
        initializePageParams();
        initializeFilterOptions();
        loadMetrics();
        loadData();
    }

    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(true)
                .sortField("modcreatedat")
                .build();

    }

    private void initializeFilterOptions() {
        // Opciones de estado
        statusOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        statusOptions.add(createOption("ACTIVE", Labels.getLabel("providers.status.active")));
        statusOptions.add(createOption("INACTIVE", Labels.getLabel("providers.status.inactive")));
        statusOptions.add(createOption("DEPRECATED", Labels.getLabel("providers.status.deprecated")));

        // Opciones de tipo de proveedor
        providerTypeOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        providerTypeOptions.add(createOption("OPENAI", Labels.getLabel("providers.type.openai")));
        providerTypeOptions.add(createOption("ANTHROPIC", Labels.getLabel("providers.type.anthropic")));
        providerTypeOptions.add(createOption("HUGGINGFACE", Labels.getLabel("providers.type.huggingface")));
        providerTypeOptions.add(createOption("AZURE", Labels.getLabel("providers.type.azure")));
        providerTypeOptions.add(createOption("AWS", Labels.getLabel("providers.type.aws")));
        providerTypeOptions.add(createOption("GOOGLE", Labels.getLabel("providers.type.google")));
        providerTypeOptions.add(createOption("CUSTOM", Labels.getLabel("providers.type.custom")));

        // Opciones de free tier
        freeTierOptions.add(createBooleanOption(null, Labels.getLabel("common.filter.all")));
        freeTierOptions.add(createBooleanOption(true, Labels.getLabel("common.yes")));
        freeTierOptions.add(createBooleanOption(false, Labels.getLabel("common.no")));
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }

    private Map<String, Object> createBooleanOption(Boolean value, String label) {
        Map<String, Object> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }

    // ========== Carga de datos ==========

    @Command
    @NotifyChange({"providers", "pageResult"})
    public void loadData() {
        try {
            log.info("Cargando proveedores - Página: {}, MaxRows: {}",
                pageParams.getPageActual(), pageParams.getMaxRows());

            Criterias criterias = buildCriterias();
            pageResult = providersOverviewService.findAll(pageParams, criterias);

            providers = pageResult != null ? pageResult.getContent() : new ArrayList<>();

            log.info("Cargados {} proveedores de {} totales",
                providers.size(), pageResult != null ? pageResult.getTotalRows() : 0);

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar proveedores", e);
            Messagebox.show(Labels.getLabel("common.error.load"),
                Labels.getLabel("common.error.title"),
                Messagebox.OK, Messagebox.ERROR);
            providers = new ArrayList<>();
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        // Filtro por término de búsqueda (nombre o display name)
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "modname");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }

        // Filtro por estado
        if (selectedStatus != null && !selectedStatus.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modstatus");
            criteria.setValues(new Object[]{selectedStatus});
            criterias.addCriteria(criteria);
        }

        // Filtro por tipo de proveedor
        if (selectedProviderType != null && !selectedProviderType.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modprovidertype");
            criteria.setValues(new Object[]{selectedProviderType});
            criterias.addCriteria(criteria);
        }

        // Filtro por free tier
        if (selectedHasFreeTier != null) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modhasfreetier");
            criteria.setValues(new Object[]{selectedHasFreeTier});
            criterias.addCriteria(criteria);
        }

        return criterias;
    }

    @Command
    @NotifyChange({"totalProviders", "activeProviders", "inactiveProviders", "totalModels",
                   "totalCredentials", "supportsText", "supportsEmbeddings"})
    public void loadMetrics() {
        try {
            log.debug("Cargando métricas globales desde V_PROVIDERS_METRICS_SUMMARY");
            List<ProvidersMetricsSummary> metrics = providersMetricsSummaryService.findAll();

            if (metrics != null && !metrics.isEmpty()) {
                ProvidersMetricsSummary summary = metrics.get(0);
                totalProviders = summary.getTotalProviders() != null ? summary.getTotalProviders() : 0L;
                activeProviders = summary.getActiveProviders() != null ? summary.getActiveProviders() : 0L;
                inactiveProviders = summary.getInactiveProviders() != null ? summary.getInactiveProviders() : 0L;
                totalModels = summary.getTotalModelsAll() != null ? summary.getTotalModelsAll() : 0L;
                totalCredentials = summary.getTotalCredentialsAll() != null ? summary.getTotalCredentialsAll() : 0L;
                supportsText = summary.getSupportsText() != null ? summary.getSupportsText() : 0L;
                supportsEmbeddings = summary.getSupportsEmbeddings() != null ? summary.getSupportsEmbeddings() : 0L;

                log.info("Métricas de proveedores cargadas - Total: {}, Activos: {}",
                    totalProviders, activeProviders);
            } else {
                log.warn("No se pudieron cargar métricas globales");
                totalProviders = 0L; activeProviders = 0L; inactiveProviders = 0L;
                totalModels = 0L; totalCredentials = 0L; supportsText = 0L; supportsEmbeddings = 0L;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar métricas de proveedores", e);
            totalProviders = 0L; activeProviders = 0L; inactiveProviders = 0L;
            totalModels = 0L; totalCredentials = 0L; supportsText = 0L; supportsEmbeddings = 0L;
        }
    }

    // ========== Filtros y búsqueda ==========

    @Command
    @NotifyChange({"providers", "pageResult"})
    public void applyFilters() {
        log.info("Aplicando filtros - Estado: {}, Tipo: {}, FreeTier: {}",
            selectedStatus, selectedProviderType, selectedHasFreeTier);
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange({"searchTerm", "selectedStatus", "selectedProviderType", "selectedHasFreeTier",
                   "providers", "pageResult"})
    public void clearFilters() {
        log.info("Limpiando filtros");
        searchTerm = null;
        selectedStatus = null;
        selectedProviderType = null;
        selectedHasFreeTier = null;
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange({"providers", "pageResult"})
    public void onSearchChange(@BindingParam("event") InputEvent event) {
        searchTerm = event.getValue();
        log.info("Búsqueda cambiada: {}", searchTerm);
        pageParams.setPageActual(1);
        loadData();
    }

    // ========== Paginación ==========

    @Command
    @NotifyChange({"providers", "pageResult"})
    public void onPaging(@BindingParam("event") Event event) {
        org.zkoss.zul.event.PagingEvent pe = (org.zkoss.zul.event.PagingEvent) event;
        int activePage = pe.getActivePage();
        pageParams.setPageActual(activePage + 1);
        log.info("Cambio de página a: {}", pageParams.getPageActual());
        loadData();
    }

    // ========== Navegación ==========

    @Command
    public void registerProvider() {
        log.info("Navegando a creación de nuevo proveedor");
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "create");
        appendPage("gobierno/providers/providers-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewProviderDetails(@BindingParam("providerId") Long providerId) {
        log.info("Navegando a detalle de proveedor ID={}", providerId);
        Map<String, Object> params = new HashMap<>();
        params.put("providerId", providerId);
        params.put("mode", "edit");
        appendPage("gobierno/providers/providers-detail.zul", page.getFellow(IDDESKTOP), params);
    }


    // ========== Acciones de contexto ==========

    @Command
    @NotifyChange({"providers", "pageResult"})
    public void deleteProvider(@BindingParam("providerId") Long providerId) {
        Messagebox.show(
            Labels.getLabel("providers.action.delete.confirm"),
            Labels.getLabel("providers.action.delete.title"),
            Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_YES.equals(event.getName())) {
                    try {
                        log.info("Eliminando proveedor ID={}", providerId);
                        Messagebox.show(Labels.getLabel("providers.action.delete.success"),
                            Labels.getLabel("common.success.title"),
                            Messagebox.OK, Messagebox.INFORMATION);
                        loadData();
                        loadMetrics();
                    } catch (Exception e) {
                        log.error("Error al eliminar proveedor", e);
                        Messagebox.show(Labels.getLabel("common.error.delete"),
                            Labels.getLabel("common.error.title"),
                            Messagebox.OK, Messagebox.ERROR);
                    }
                }
            }
        );
    }

    @Command
    public void refreshData() {
        log.info("Refrescando datos de proveedores");
        loadMetrics();
        loadData();
    }

	@Override
	public void setBeans(Object bean) {
		// TODO Auto-generated method stub

	}
}
