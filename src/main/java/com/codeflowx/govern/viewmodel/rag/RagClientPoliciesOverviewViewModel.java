package com.codeflowx.govern.viewmodel.rag;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.enartframework.web.zk.page.MasterPage;
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

import com.codeflowx.govern.entity.rag.RagClientPolicy;
import com.codeflowx.govern.service.rag.RagClientPolicyService;

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
 * ViewModel para la pantalla de Overview de Políticas de Cliente RAG
 * Implementa el patrón establecido con Criterias API y navegación dinámica
 */
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class RagClientPoliciesOverviewViewModel extends BaseFront<RagClientPoliciesOverviewViewModel>{

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";

    @WireVariable
    private RagClientPolicyService ragClientPolicyService;

    @WireVariable
    public Environment environment;

    private Component view;

    // ========== Datos del grid ==========
    private List<RagClientPolicy> policiesList = new ArrayList<>();
    private RagClientPolicy selectedPolicy;

    // ========== Filtros de búsqueda ==========
    private String searchTerm;
    private String selectedPolicyType;
    private String selectedEnabled;
    private Long selectedClientId;

    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<RagClientPolicy> pageResult;

    // ========== Métricas globales ==========
    private Long totalPolicies = 0L;
    private Long enabledPolicies = 0L;
    private Long disabledPolicies = 0L;
    private Long totalClients = 0L;

    // ========== Opciones de filtro ==========
    private List<Map<String, String>> policyTypeOptions = new ArrayList<>();
    private List<Map<String, String>> enabledOptions = new ArrayList<>();

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        log.info("Inicializando RagClientPoliciesOverviewViewModel");
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
                .ascending(false)
                .sortField("rcpcreatedat")
                .build();
    }

    private void initializeFilterOptions() {
        // Opciones de tipo de política
        policyTypeOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        policyTypeOptions.add(createOption("TONE", "Tono"));
        policyTypeOptions.add(createOption("CONTENT", "Contenido"));
        policyTypeOptions.add(createOption("ETHICS", "Ética"));
        policyTypeOptions.add(createOption("VALUES", "Valores"));
        policyTypeOptions.add(createOption("LANGUAGE", "Idioma"));

        // Opciones de habilitado
        enabledOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        enabledOptions.add(createOption("true", Labels.getLabel("common.enabled")));
        enabledOptions.add(createOption("false", Labels.getLabel("common.disabled")));
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }

    // ========== Carga de datos ==========

    @Command
    @NotifyChange({"policiesList", "pageResult", "totalPolicies", "enabledPolicies", "disabledPolicies"})
    public void loadData() {
        try {
            log.info("Cargando políticas de cliente RAG - Página: {}, MaxRows: {}",
                pageParams.getPageActual(), pageParams.getMaxRows());

            Criterias criterias = buildCriterias();
            pageResult = ragClientPolicyService.findAll(pageParams, criterias);

            policiesList = pageResult != null ? pageResult.getContent() : new ArrayList<>();

            if (pageResult != null) {
                totalPolicies = (long) pageResult.getTotalRows();
            }

            // Calcular métricas
            enabledPolicies = policiesList.stream()
                .filter(p -> p.getRcpenabled() != null && p.getRcpenabled())
                .count();
            disabledPolicies = totalPolicies - enabledPolicies;

            log.info("Cargadas {} políticas de cliente RAG de {} totales",
                policiesList.size(), totalPolicies);

        } catch (Exception e) {
            log.error("Error al cargar políticas de cliente RAG", e);
            Messagebox.show(Labels.getLabel("common.error.load"),
                Labels.getLabel("common.error.title"),
                Messagebox.OK, Messagebox.ERROR);
            policiesList = new ArrayList<>();
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        // Filtro por término de búsqueda (nombre)
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "rcppolicyname");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }

        // Filtro por tipo de política
        if (selectedPolicyType != null && !selectedPolicyType.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "rcppolicytype");
            criteria.setValues(new Object[]{selectedPolicyType});
            criterias.addCriteria(criteria);
        }

        // Filtro por habilitado
        if (selectedEnabled != null && !selectedEnabled.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "rcpenabled");
            criteria.setValues(new Object[]{Boolean.parseBoolean(selectedEnabled)});
            criterias.addCriteria(criteria);
        }

        // Filtro por cliente
        if (selectedClientId != null) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "rcpclientid");
            criteria.setValues(new Object[]{selectedClientId});
            criterias.addCriteria(criteria);
        }

        return criterias;
    }

    @Command
    @NotifyChange({"totalPolicies", "enabledPolicies", "disabledPolicies", "totalClients"})
    public void loadMetrics() {
        try {
            // Cargar métricas básicas desde la lista
            // En el futuro se puede optimizar con una vista SQL
            log.debug("Métricas calculadas desde la lista de políticas");
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }

    // ========== Filtros y búsqueda ==========

    @Command
    @NotifyChange({"policiesList", "pageResult"})
    public void applyFilters() {
        log.info("Aplicando filtros - Tipo: {}, Habilitado: {}, Cliente: {}",
            selectedPolicyType, selectedEnabled, selectedClientId);
        pageParams.setPageActual(1); // Reset a primera página
        loadData();
    }

    @Command
    @NotifyChange({"searchTerm", "selectedPolicyType", "selectedEnabled", "selectedClientId",
                   "policiesList", "pageResult"})
    public void clearFilters() {
        log.info("Limpiando filtros");
        searchTerm = null;
        selectedPolicyType = null;
        selectedEnabled = null;
        selectedClientId = null;
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange({"policiesList", "pageResult"})
    public void onSearchChange(@BindingParam("event") InputEvent event) {
        searchTerm = event.getValue();
        log.info("Búsqueda cambiada: {}", searchTerm);
        pageParams.setPageActual(1);
        loadData();
    }

    // ========== Paginación ==========

    @Command
    @NotifyChange({"policiesList", "pageResult"})
    public void onPaging(@BindingParam("event") Event event) {
        org.zkoss.zul.event.PagingEvent pe = (org.zkoss.zul.event.PagingEvent) event;
        int activePage = pe.getActivePage();
        pageParams.setPageActual(activePage + 1);
        log.info("Cambio de página a: {}", pageParams.getPageActual());
        loadData();
    }

    // ========== Navegación ==========

    @Command
    public void createRagClientPolicy() {
        log.info("Navegando a creación de nueva política de cliente RAG");
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "create");
        appendPage("gobierno/rag/rag-client-policies-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewRagClientPolicyDetails(@BindingParam("policyId") Long policyId) {
        log.info("Navegando a detalle de política de cliente RAG ID={}", policyId);
        Map<String, Object> params = new HashMap<>();
        params.put("policyId", policyId);
        params.put("mode", "edit");
        appendPage("gobierno/rag/rag-client-policies-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    // ========== Acciones de contexto ==========

    @Command
    @NotifyChange({"policiesList", "pageResult"})
    public void deleteRagClientPolicy(@BindingParam("policyId") Long policyId) {
        Messagebox.show(
            Labels.getLabel("rag.policy.delete.confirm"),
            Labels.getLabel("rag.policy.delete.title"),
            Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_YES.equals(event.getName())) {
                    try {
                        boolean deleted = ragClientPolicyService.deleteById(policyId);
                        if (deleted) {
                            log.info("Política de cliente RAG eliminada ID={}", policyId);
                            Messagebox.show(Labels.getLabel("rag.policy.delete.success"),
                                Labels.getLabel("common.success.title"),
                                Messagebox.OK, Messagebox.INFORMATION);
                            loadData();
                            loadMetrics();
                        } else {
                            Messagebox.show(Labels.getLabel("common.error.delete"),
                                Labels.getLabel("common.error.title"),
                                Messagebox.OK, Messagebox.ERROR);
                        }
                    } catch (Exception e) {
                        log.error("Error al eliminar política de cliente RAG", e);
                        Messagebox.show(Labels.getLabel("common.error.delete") + ": " + e.getMessage(),
                            Labels.getLabel("common.error.title"),
                            Messagebox.OK, Messagebox.ERROR);
                    }
                }
            }
        );
    }

    @Command
    public void refreshData() {
        log.info("Refrescando datos de políticas de cliente RAG");
        loadMetrics();
        loadData();
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
