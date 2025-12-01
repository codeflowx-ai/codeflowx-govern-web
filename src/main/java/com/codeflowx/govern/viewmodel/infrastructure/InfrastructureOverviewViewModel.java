package com.codeflowx.govern.viewmodel.infrastructure;
import com.codeflowx.framework.zkoss.BaseFront;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.infrastructure.CloudResource;
import com.codeflowx.govern.service.infrastructure.CloudResourceService;
import com.codeflowx.govern.entity.views.infrastructure.InfrastructureMetricsSummary;
import com.codeflowx.govern.entity.views.infrastructure.InfrastructureOverview;
import com.codeflowx.govern.service.infrastructure.InfrastructureOverviewService;
import com.codeflowx.govern.service.infrastructure.InfrastructureMetricsSummaryService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

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

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class InfrastructureOverviewViewModel extends BaseFront<InfrastructureOverviewViewModel>{

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";

    @WireVariable
    private CloudResourceService cloudResourceService;

    @WireVariable
    private InfrastructureOverviewService infrastructureOverviewService;

    @WireVariable
    private InfrastructureMetricsSummaryService infrastructureMetricsSummaryService;

    @WireVariable
    public Environment environment;

    private PageParams pageParams;
    private PageResult<InfrastructureOverview> pageResult;

    private List<InfrastructureOverview> filteredResources = new ArrayList<>();
    private String searchTerm = "";
    private String typeFilter = "ALL";
    private String healthFilter = "ALL";

    // Métricas resumen
    private Long totalResources = 0L;
    private Long computeResources = 0L;
    private Long healthyResources = 0L;
    private Long totalClusters = 0L;
    private BigDecimal totalHourlyCost = BigDecimal.ZERO;

    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadMetrics();
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            log.info("Cargando recursos - Página: {}", pageParams.getPageActual());

            Criterias criterias = buildCriterias();

            pageResult = infrastructureOverviewService.findAll(
                pageParams,
                criterias
            );

            if (pageResult != null && pageResult.getContent() != null) {
                filteredResources = pageResult.getContent();
                log.info("Cargados {} recursos", filteredResources.size());
            } else {
                filteredResources = new ArrayList<>();
            }

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar recursos", e);
            Messagebox.show("Error al cargar recursos: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            filteredResources = new ArrayList<>();
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "infresname");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(typeFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "infresresourcetype");
            criteria.setValues(new Object[]{typeFilter});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(healthFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "infreshealthstatus");
            criteria.setValues(new Object[]{healthFilter});
            criterias.addCriteria(criteria);
        }

        return criterias;
    }

    @Command
    @NotifyChange({"totalResources", "computeResources", "healthyResources", "totalClusters", "totalHourlyCost"})
    public void loadMetrics() {
        try {
            log.debug("Cargando métricas globales");

            List<InfrastructureMetricsSummary> metrics = infrastructureMetricsSummaryService.findAll();

            if (metrics != null && !metrics.isEmpty()) {
                InfrastructureMetricsSummary summary = metrics.get(0);

                totalResources = summary.getTotalResources() != null ? summary.getTotalResources() : 0L;
                computeResources = summary.getComputeResources() != null ? summary.getComputeResources() : 0L;
                healthyResources = summary.getHealthyResources() != null ? summary.getHealthyResources() : 0L;
                totalClusters = summary.getTotalClusters() != null ? summary.getTotalClusters() : 0L;
                totalHourlyCost = summary.getTotalHourlyCost() != null ? summary.getTotalHourlyCost() : BigDecimal.ZERO;

                log.info("Métricas cargadas - Total: {}, Compute: {}, Healthy: {}",
                    totalResources, computeResources, healthyResources);
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar métricas", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        log.info("Aplicando filtros");
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void clearFilters() {
        log.info("Limpiando filtros");
        searchTerm = "";
        typeFilter = "ALL";
        healthFilter = "ALL";
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void nextPage() {
        if (pageResult != null && pageResult.getNextRow() > 0) {
            pageParams.setPageActual(pageParams.getPageActual() + 1);
            pageParams.setRowActual(pageResult.getNextRow());
            loadData();
        }
    }

    @Command
    @NotifyChange("*")
    public void previousPage() {
        if (pageParams.getPageActual() > 1) {
            pageParams.setPageActual(pageParams.getPageActual() - 1);
            int prevRow = (pageParams.getPageActual() - 2) * pageParams.getMaxRows();
            pageParams.setRowActual(Math.max(0, prevRow));
            loadData();
        }
    }

    @Command
    @NotifyChange("*")
    public void onPaging(@BindingParam("event") org.zkoss.zul.event.PagingEvent event) {
        int pageIndex = event.getActivePage();
        pageParams.setPageActual(pageIndex + 1);
        pageParams.setRowActual(pageIndex * pageParams.getMaxRows());
        loadData();
    }

    @Command
    public void registerResource() {
        log.info("Navegando a creación de recurso");
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "create");
        appendPage("gobierno/infrastructure/infrastructure-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    public void viewResourceDetails(@BindingParam("resourceId") Long resourceId) {
        log.info("Navegando a detalle de recurso ID={}", resourceId);
        Map<String, Object> params = new HashMap<>();
        params.put("resourceId", resourceId);
        params.put("mode", "edit");
        appendPage("gobierno/infrastructure/infrastructure-detail.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    @NotifyChange("*")
    public void deleteResource(@BindingParam("resourceId") Long resourceId) {
        try {
            Messagebox.show(
                "¿Está seguro de eliminar este recurso?",
                "Confirmar eliminación",
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            cloudResourceService.deleteById(resourceId);
                            log.info("Recurso eliminado: ID={}", resourceId);
                            loadData();
                            loadMetrics();
                            Messagebox.show("Recurso eliminado correctamente",
                                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                        } catch (Exception e) {
                            log.error("Error al eliminar recurso ID={}", resourceId, e);
                            Messagebox.show("Error al eliminar: " + e.getMessage(),
                                "Error", Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                }
            );
        } catch (Exception e) {
            log.error("Error en diálogo de eliminación", e);
        }
    }
}
