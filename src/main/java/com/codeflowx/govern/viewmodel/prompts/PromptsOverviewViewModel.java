package com.codeflowx.govern.viewmodel.prompts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.sql.DataSource;

import com.codeflowx.framework.zkoss.BaseFront;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.util.resource.Labels;

import com.codeflowx.govern.entity.views.prompts.PromptsOverview;
import com.codeflowx.govern.service.prompts.PromptService;
import com.codeflowx.govern.service.prompts.PromptsOverviewService;
import com.codeflowx.govern.service.prompts.PromptsMetricsSummaryService;
import com.codeflowx.govern.entity.views.prompts.PromptsMetricsSummary;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.Evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.enartframework.nocode.dao.IEntityLocal;

/**
 * ViewModel para la pantalla de búsqueda y listado de prompts.
 * Incluye filtros avanzados, métricas globales y navegación a detalles.
 * Patrón: Overview (solo búsqueda/listado/navegación)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PromptsOverviewViewModel extends BaseFront<PromptsOverviewViewModel> {

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";

    @WireVariable
    private PromptService promptService;
    @WireVariable
    private PromptsOverviewService promptsOverviewService;
    @WireVariable
    private PromptsMetricsSummaryService promptsMetricsSummaryService;
    @WireVariable
    private BusinessService businessService; // Mantener para procedimientos almacenados si es necesario
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;

    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<PromptsOverview> pageResult;

    // ========== Filtros ==========
    private String searchTerm = "";
    private String typeFilter = "ALL";
    private String statusFilter = "ALL";
    private String categoryFilter = "ALL";
    private String approvalStatusFilter = "ALL";

    // ========== Datos ==========
    private List<PromptsOverview> filteredPrompts = new ArrayList<>();

    // ========== Métricas globales ==========
    private int totalPrompts = 0;
    private long activePrompts = 0L;
    private long pendingApproval = 0L;
    private long rejectedPrompts = 0L;
    private BigDecimal averageValidationScore = BigDecimal.ZERO;
    private long totalFailedValidations = 0L;
    private long totalVersions = 0L; // Total de versiones de todos los prompts
    private BigDecimal avgRating = BigDecimal.ZERO; // Rating promedio (averageValidationScore)

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadData();
    }

    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            log.debug("Cargando prompts - Página: {}", pageParams.getPageActual());

            Criterias criterias = buildCriterias();

            pageResult = promptsOverviewService.findAll(pageParams, criterias);

            if (pageResult != null && pageResult.getContent() != null) {
                filteredPrompts = pageResult.getContent();
                totalPrompts = pageResult.getTotalRows();

                loadGlobalMetrics();

                log.info("Cargados {} prompts de {} totales",
                    filteredPrompts.size(), totalPrompts);
            } else {
                filteredPrompts = new ArrayList<>();
                totalPrompts = 0;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar prompts", e);
            Messagebox.show(Labels.getLabel("prompts.error.load") + ": " + e.getMessage(),
                Labels.getLabel("prompts.error.title"), Messagebox.OK, Messagebox.ERROR);
            filteredPrompts = new ArrayList<>();
        } catch (Exception e) {
            log.error("Error inesperado al cargar prompts", e);
            Messagebox.show(Labels.getLabel("prompts.error.load") + ": " + e.getMessage(),
                Labels.getLabel("prompts.error.title"), Messagebox.OK, Messagebox.ERROR);
            filteredPrompts = new ArrayList<>();
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "prmname");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(typeFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prmtype");
            criteria.setValues(new Object[]{typeFilter});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(statusFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prmstatus");
            criteria.setValues(new Object[]{statusFilter});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(categoryFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prmcategory");
            criteria.setValues(new Object[]{categoryFilter});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(approvalStatusFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prmapprovalstatus");
            criteria.setValues(new Object[]{approvalStatusFilter});
            criterias.addCriteria(criteria);
        }

        return criterias;
    }

    private void loadGlobalMetrics() {
        try {
            log.debug("Cargando métricas globales desde V_PROMPTS_METRICS_SUMMARY");
            List<PromptsMetricsSummary> metrics = promptsMetricsSummaryService.findAll();
            if (metrics != null && !metrics.isEmpty()) {
                PromptsMetricsSummary summary = metrics.get(0);
                activePrompts = summary.getActivePrompts() != null ? summary.getActivePrompts() : 0L;
                pendingApproval = summary.getPendingApproval() != null ? summary.getPendingApproval() : 0L;
                rejectedPrompts = summary.getRejectedPrompts() != null ? summary.getRejectedPrompts() : 0L;
                averageValidationScore = summary.getAverageValidationScore() != null ? summary.getAverageValidationScore() : BigDecimal.ZERO;
                totalFailedValidations = summary.getTotalFailedValidations() != null ? summary.getTotalFailedValidations() : 0L;
                totalVersions = summary.getTotalVersionsAll() != null ? summary.getTotalVersionsAll() : 0L;
                avgRating = averageValidationScore; // avgRating es el mismo que averageValidationScore
                log.info("Métricas globales cargadas - Total: {}, Activos: {}, Pendientes: {}, Versiones: {}",
                    totalPrompts, activePrompts, pendingApproval, totalVersions);
            } else {
                log.warn("No se pudieron cargar métricas globales");
                activePrompts = 0L; pendingApproval = 0L; rejectedPrompts = 0L;
                averageValidationScore = BigDecimal.ZERO; totalFailedValidations = 0L;
                totalVersions = 0L; avgRating = BigDecimal.ZERO;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar métricas globales", e);
            activePrompts = 0L; pendingApproval = 0L; rejectedPrompts = 0L;
            averageValidationScore = BigDecimal.ZERO; totalFailedValidations = 0L;
            totalVersions = 0L; avgRating = BigDecimal.ZERO;
        } catch (Exception e) {
            log.error("Error inesperado al cargar métricas globales", e);
            activePrompts = 0L; pendingApproval = 0L; rejectedPrompts = 0L;
            averageValidationScore = BigDecimal.ZERO; totalFailedValidations = 0L;
            totalVersions = 0L; avgRating = BigDecimal.ZERO;
        }
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        log.debug("Aplicando filtros - searchTerm: {}, type: {}, status: {}", searchTerm, typeFilter, statusFilter);
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void clearFilters() {
        log.debug("Limpiando filtros");
        searchTerm = "";
        typeFilter = "ALL";
        statusFilter = "ALL";
        categoryFilter = "ALL";
        approvalStatusFilter = "ALL";
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void onPaging(@BindingParam("event") PagingEvent event) {
        int pageIndex = event.getActivePage();
        pageParams.setPageActual(pageIndex + 1);
        pageParams.setRowActual(pageIndex * pageParams.getMaxRows());
        loadData();
    }

    // ========== Navegación ==========

    @Command
    public void createPrompt() {
        log.info("Navegando a creación de nuevo prompt");
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "create");
        appendPage("gobierno/prompts/prompts-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewPromptDetails(@BindingParam("promptId") Long promptId) {
        log.info("Navegando a detalle de prompt ID={}", promptId);
        Map<String, Object> params = new HashMap<>();
        params.put("promptId", promptId);
        params.put("mode", "edit");
        appendPage("gobierno/prompts/prompts-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    @NotifyChange("*")
    public void deletePrompt(@BindingParam("promptId") Long promptId) {
        try {
            Messagebox.show(Labels.getLabel("prompts.confirm.delete.message"),
                Labels.getLabel("prompts.confirm.title"),
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            promptService.deleteById(promptId);
                            log.info("Prompt eliminado: ID={}", promptId);
                            loadData();
                            Messagebox.show(Labels.getLabel("prompts.success.deleted"),
                                Labels.getLabel("prompts.success.title"), Messagebox.OK, Messagebox.INFORMATION);
                        } catch (GovernanceServiceException e) {
                            log.error("Error al eliminar prompt ID={}", promptId, e);
                            Messagebox.show(Labels.getLabel("prompts.error.delete") + ": " + e.getMessage(),
                                Labels.getLabel("prompts.error.title"), Messagebox.OK, Messagebox.ERROR);
                        } catch (Exception e) {
                            log.error("Error inesperado al eliminar prompt ID={}", promptId, e);
                            Messagebox.show(Labels.getLabel("prompts.error.delete") + ": " + e.getMessage(),
                                Labels.getLabel("prompts.error.title"), Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                }
            );
        } catch (Exception e) {
            log.error("Error en diálogo de eliminación", e);
        }
    }
}
