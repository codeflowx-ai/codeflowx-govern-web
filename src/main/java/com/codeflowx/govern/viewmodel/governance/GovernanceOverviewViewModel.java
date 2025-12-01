package com.codeflowx.govern.viewmodel.governance;
import com.codeflowx.framework.zkoss.BaseFront;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.enartframework.suinsit.Context;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.PagingEvent;
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
import org.zkoss.bind.annotation.Destroy;

import com.codeflowx.govern.entity.views.governance.GovernanceMetricsSummary;
import com.codeflowx.govern.entity.views.governance.GovernanceOverview;
import com.codeflowx.govern.service.governance.PolicyService;
/**
 * ViewModel para la pantalla de búsqueda y listado de políticas de governance.
 * Incluye filtros avanzados, métricas globales y navegación a detalles.
 * Patrón: Overview (solo búsqueda/listado/navegación)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class GovernanceOverviewViewModel extends BaseFront<GovernanceOverviewViewModel>{

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";

    @WireVariable private PolicyService policyService;
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
    private PageResult<GovernanceOverview> pageResult;

    // ========== Filtros ==========
    private String searchTerm = "";
    private String categoryFilter = "ALL";
    private String statusFilter = "ALL";
    private String policyTypeFilter = "ALL";
    private String enforcementLevelFilter = "ALL";

    // ========== Datos ==========
    private List<GovernanceOverview> filteredPolicies = new ArrayList<>();

    // ========== Métricas globales ==========
    private int totalPolicies = 0;
    private long activePolicies = 0L;
    private long pendingApproval = 0L;
    private long expiredPolicies = 0L;
    private long criticalViolations = 0L;
    private BigDecimal averageComplianceScore = BigDecimal.ZERO;

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
            log.debug("Cargando políticas - Página: {}", pageParams.getPageActual());

            Criterias criterias = buildCriterias();

            pageResult = policyService.findAllOverview(pageParams, criterias);

            if (pageResult != null && pageResult.getContent() != null) {
                filteredPolicies = pageResult.getContent();
                totalPolicies = pageResult.getTotalRows();

                loadGlobalMetrics();

                log.info("Cargadas {} políticas de {} totales",
                    filteredPolicies.size(), totalPolicies);
            } else {
                filteredPolicies = new ArrayList<>();
                totalPolicies = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar políticas", e);
            Messagebox.show(Labels.getLabel("governance.error.load") + ": " + e.getMessage(),
                Labels.getLabel("governance.error.title"), Messagebox.OK, Messagebox.ERROR);
            filteredPolicies = new ArrayList<>();
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.LIKE, "name");
            criteria.setValues(new Object[]{searchTerm.trim()});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(categoryFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "category");
            criteria.setValues(new Object[]{categoryFilter});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(statusFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "status");
            criteria.setValues(new Object[]{statusFilter});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(policyTypeFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "policytype");
            criteria.setValues(new Object[]{policyTypeFilter});
            criterias.addCriteria(criteria);
        }

        if (!"ALL".equals(enforcementLevelFilter)) {
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "enforcementlevel");
            criteria.setValues(new Object[]{enforcementLevelFilter});
            criterias.addCriteria(criteria);
        }

        return criterias;
    }

    private void loadGlobalMetrics() {
        try {
            log.debug("Cargando métricas globales desde V_GOVERNANCE_METRICS_SUMMARY");
            GovernanceMetricsSummary summary = policyService.getMetricsSummary();
            if (summary != null) {
                activePolicies = summary.getActivePolicies() != null ? summary.getActivePolicies() : 0L;
                pendingApproval = summary.getPendingApproval() != null ? summary.getPendingApproval() : 0L;
                expiredPolicies = summary.getExpiredPolicies() != null ? summary.getExpiredPolicies() : 0L;
                criticalViolations = summary.getCriticalViolations() != null ? summary.getCriticalViolations() : 0L;
                averageComplianceScore = summary.getAverageComplianceScore() != null ? summary.getAverageComplianceScore() : BigDecimal.ZERO;
                log.info("Métricas globales cargadas - Total: {}, Activas: {}, Pendientes: {}",
                    totalPolicies, activePolicies, pendingApproval);
            } else {
                log.warn("No se pudieron cargar métricas globales");
                activePolicies = 0L; pendingApproval = 0L; expiredPolicies = 0L; criticalViolations = 0L;
                averageComplianceScore = BigDecimal.ZERO;
            }
        } catch (Exception e) {
            log.error("Error al cargar métricas globales", e);
            activePolicies = 0L; pendingApproval = 0L; expiredPolicies = 0L; criticalViolations = 0L;
            averageComplianceScore = BigDecimal.ZERO;
        }
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        log.debug("Aplicando filtros - searchTerm: {}, category: {}, status: {}", searchTerm, categoryFilter, statusFilter);
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void clearFilters() {
        log.debug("Limpiando filtros");
        searchTerm = "";
        categoryFilter = "ALL";
        statusFilter = "ALL";
        policyTypeFilter = "ALL";
        enforcementLevelFilter = "ALL";
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
    public void createPolicy() {
        log.info("Navegando a creación de nueva política");
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "create");
        appendPage("governance/policies/page.zul", page.getFellow(IDDESKTOP), params);
    }
    
    @Command
    public void viewPolicyDetails(@BindingParam("policyId") Long policyId) {
        log.info("Navegando a detalle de política ID={}", policyId);
        Map<String, Object> params = new HashMap<>();
        params.put("policyId", policyId);
        params.put("mode", "edit");
        appendPage("governance/policies/page.zul", page.getFellow(IDDESKTOP), params);
    }

    @Command
    @NotifyChange("*")
    public void deletePolicy(@BindingParam("policyId") Long policyId) {
        try {
            Messagebox.show(Labels.getLabel("governance.confirm.delete.message"),
                Labels.getLabel("governance.confirm.title"),
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            policyService.deleteById(policyId);
                            log.info("Política eliminada: ID={}", policyId);
                            loadData();
                            Messagebox.show(Labels.getLabel("governance.success.deleted"),
                                Labels.getLabel("governance.success.title"), Messagebox.OK, Messagebox.INFORMATION);
                        } catch (Exception e) {
                            log.error("Error al eliminar política ID={}", policyId, e);
                            Messagebox.show(Labels.getLabel("governance.error.delete") + ": " + e.getMessage(),
                                Labels.getLabel("governance.error.title"), Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                }
            );
        } catch (Exception e) {
            log.error("Error en diálogo de eliminación", e);
        }
    }

    /**
     * Método de limpieza para optimización de memoria
     * Se ejecuta cuando el ViewModel es destruido
     */
    @Destroy
    public void destroy() {
        log.debug("Limpiando recursos del ViewModel");
        
        // Limpiar referencias a objetos grandes
        if (pageResult != null && pageResult.getContent() != null) {
            pageResult.getContent().clear();
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
        
        log.debug("Recursos limpiados exitosamente");
    }

}
