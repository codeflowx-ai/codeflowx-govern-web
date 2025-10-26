package com.codeflowx.govern.viewmodel.rag;

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

import com.codeflowx.govern.entity.views.rag.RagOverview;
import com.codeflowx.govern.entity.views.rag.RagMetricsSummary;

import codeflowx.nocode.persist.BusinessService;
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
 * ViewModel para la pantalla de Overview de Sistemas RAG
 * Implementa el patrón establecido con Views SQL, Criterias API y navegación dinámica
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RagSystemsOverviewViewModel extends MasterPage {
    
    private static final long serialVersionUID = 1L;

    private static final String IDDESKTOP = "contenedor";
    
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    public Environment environment;

    private Component view;

    // ========== Datos del grid ==========
    private List<RagOverview> ragSystems = new ArrayList<>();
    private RagOverview selectedRagSystem;

    // ========== Filtros de búsqueda ==========
    private String searchTerm;
    private String selectedStatus;
    private String selectedType;
    private String selectedApprovalStatus;
 // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<RagOverview> pageResult;
    // ========== Paginación ==========
    
    // ========== Métricas globales ==========
    private Long totalRagSystems = 0L;
    private Long activeRagSystems = 0L;
    private Long draftRagSystems = 0L;
    private Long pendingApproval = 0L;
    private Long approvedRagSystems = 0L;
    private Long totalDatasources = 0L;
    private Long totalDocuments = 0L;

    // ========== Opciones de filtro ==========
    private List<Map<String, String>> statusOptions = new ArrayList<>();
    private List<Map<String, String>> typeOptions = new ArrayList<>();
    private List<Map<String, String>> approvalStatusOptions = new ArrayList<>();

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        log.info("Inicializando RagSystemsOverviewViewModel");
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
                .sortField("ragcreatedat")
                .build();
        
    }

    private void initializeFilterOptions() {
        // Opciones de estado
        statusOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        statusOptions.add(createOption("ACTIVE", Labels.getLabel("rag.status.active")));
        statusOptions.add(createOption("DRAFT", Labels.getLabel("rag.status.draft")));
        statusOptions.add(createOption("ARCHIVED", Labels.getLabel("rag.status.archived")));
        statusOptions.add(createOption("DEPRECATED", Labels.getLabel("rag.status.deprecated")));

        // Opciones de tipo
        typeOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        typeOptions.add(createOption("SEMANTIC_SEARCH", Labels.getLabel("rag.type.semantic_search")));
        typeOptions.add(createOption("QA_SYSTEM", Labels.getLabel("rag.type.qa_system")));
        typeOptions.add(createOption("KNOWLEDGE_BASE", Labels.getLabel("rag.type.knowledge_base")));
        typeOptions.add(createOption("HYBRID", Labels.getLabel("rag.type.hybrid")));

        // Opciones de aprobación
        approvalStatusOptions.add(createOption("", Labels.getLabel("common.filter.all")));
        approvalStatusOptions.add(createOption("PENDING", Labels.getLabel("common.approval.pending")));
        approvalStatusOptions.add(createOption("APPROVED", Labels.getLabel("common.approval.approved")));
        approvalStatusOptions.add(createOption("REJECTED", Labels.getLabel("common.approval.rejected")));
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }

    // ========== Carga de datos ==========

    @Command
    @NotifyChange({"ragSystems", "pageResult"})
    public void loadData() {
        try {
            log.info("Cargando sistemas RAG - Página: {}, MaxRows: {}", 
                pageParams.getPageActual(), pageParams.getMaxRows());
            
            Criterias criterias = buildCriterias();
            pageResult = businessService.findAllView(RagOverview.class, pageParams, criterias);
            
            ragSystems = pageResult != null ? pageResult.getContent() : new ArrayList<>();
            
            log.info("Cargados {} sistemas RAG de {} totales", 
                ragSystems.size(), pageResult != null ? pageResult.getTotalRows() : 0);
                
        } catch (Exception e) {
            log.error("Error al cargar sistemas RAG", e);
            Messagebox.show(Labels.getLabel("common.error.load"), 
                Labels.getLabel("common.error.title"),
                Messagebox.OK, Messagebox.ERROR);
            ragSystems = new ArrayList<>();
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        // Filtro por término de búsqueda (nombre)
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "ragname");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }

        // Filtro por estado
        if (selectedStatus != null && !selectedStatus.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragstatus");
            criteria.setValues(new Object[]{selectedStatus});
            criterias.addCriteria(criteria);
        }

        // Filtro por tipo
        if (selectedType != null && !selectedType.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragtype");
            criteria.setValues(new Object[]{selectedType});
            criterias.addCriteria(criteria);
        }

        // Filtro por estado de aprobación
        if (selectedApprovalStatus != null && !selectedApprovalStatus.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "ragapprovalstatus");
            criteria.setValues(new Object[]{selectedApprovalStatus});
            criterias.addCriteria(criteria);
        }

        return criterias;
    }

    @Command
    @NotifyChange({"totalRagSystems", "activeRagSystems", "draftRagSystems", "pendingApproval", 
                   "approvedRagSystems", "totalDatasources", "totalDocuments"})
    public void loadMetrics() {
        try {
            PageParams metricsParams = PageParams.builder()
                .maxRows(1)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<RagMetricsSummary> result = businessService.findAllView(
                RagMetricsSummary.class, 
                metricsParams, 
                new Criterias()
            );
            
            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                RagMetricsSummary metrics = result.getContent().get(0);
                totalRagSystems = metrics.getTotalRagSystems() != null ? metrics.getTotalRagSystems() : 0L;
                activeRagSystems = metrics.getActiveRagSystems() != null ? metrics.getActiveRagSystems() : 0L;
                draftRagSystems = metrics.getDraftRagSystems() != null ? metrics.getDraftRagSystems() : 0L;
                pendingApproval = metrics.getPendingApproval() != null ? metrics.getPendingApproval() : 0L;
                approvedRagSystems = metrics.getApprovedRagSystems() != null ? metrics.getApprovedRagSystems() : 0L;
                totalDatasources = metrics.getTotalDatasourcesAll() != null ? metrics.getTotalDatasourcesAll() : 0L;
                totalDocuments = metrics.getTotalDocumentsIndexed() != null ? metrics.getTotalDocumentsIndexed() : 0L;
                
                log.info("Métricas RAG cargadas - Total: {}, Activos: {}, Borradores: {}", 
                    totalRagSystems, activeRagSystems, draftRagSystems);
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas de RAG", e);
        }
    }

    // ========== Filtros y búsqueda ==========

    @Command
    @NotifyChange({"ragSystems", "pageResult"})
    public void applyFilters() {
        log.info("Aplicando filtros - Estado: {}, Tipo: {}, Aprobación: {}", 
            selectedStatus, selectedType, selectedApprovalStatus);
        pageParams.setPageActual(1); // Reset a primera página
        loadData();
    }

    @Command
    @NotifyChange({"searchTerm", "selectedStatus", "selectedType", "selectedApprovalStatus", 
                   "ragSystems", "pageResult"})
    public void clearFilters() {
        log.info("Limpiando filtros");
        searchTerm = null;
        selectedStatus = null;
        selectedType = null;
        selectedApprovalStatus = null;
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange({"ragSystems", "pageResult"})
    public void onSearchChange(@BindingParam("event") InputEvent event) {
        searchTerm = event.getValue();
        log.info("Búsqueda cambiada: {}", searchTerm);
        pageParams.setPageActual(1);
        loadData();
    }

    // ========== Paginación ==========

    @Command
    @NotifyChange({"ragSystems", "pageResult"})
    public void onPaging(@BindingParam("event") Event event) {
        org.zkoss.zul.event.PagingEvent pe = (org.zkoss.zul.event.PagingEvent) event;
        int activePage = pe.getActivePage();
        pageParams.setPageActual(activePage + 1);
        log.info("Cambio de página a: {}", pageParams.getPageActual());
        loadData();
    }

    // ========== Navegación ==========

    @Command
    public void registerRagSystem() {
        log.info("Navegando a creación de nuevo sistema RAG");
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "create");
        appendPage("gobierno/rag/rag-systems-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewRagSystemDetails(@BindingParam("ragSystemId") Long ragSystemId) {
        log.info("Navegando a detalle de sistema RAG ID={}", ragSystemId);
        Map<String, Object> params = new HashMap<>();
        params.put("ragSystemId", ragSystemId);
        params.put("mode", "edit");
        appendPage("gobierno/rag/rag-systems-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    

    // ========== Acciones de contexto ==========

    @Command
    @NotifyChange({"ragSystems", "pageResult"})
    public void deleteRagSystem(@BindingParam("ragSystemId") Long ragSystemId) {
        Messagebox.show(
            Labels.getLabel("rag.action.delete.confirm"),
            Labels.getLabel("rag.action.delete.title"),
            Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_YES.equals(event.getName())) {
                    try {
                        // TODO: Implementar lógica de eliminación
                        log.info("Eliminando sistema RAG ID={}", ragSystemId);
                        Messagebox.show(Labels.getLabel("rag.action.delete.success"),
                            Labels.getLabel("common.success.title"),
                            Messagebox.OK, Messagebox.INFORMATION);
                        loadData();
                        loadMetrics();
                    } catch (Exception e) {
                        log.error("Error al eliminar sistema RAG", e);
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
        log.info("Refrescando datos de sistemas RAG");
        loadMetrics();
        loadData();
    }

	@Override
	public void setBeans(Object bean) {
		// TODO Auto-generated method stub
		
	}
}

