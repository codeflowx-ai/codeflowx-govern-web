package com.codeflowx.govern.viewmodel.projects;
import com.codeflowx.framework.zkoss.BaseFront;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.enartframework.annotation.context.Autowired;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.views.projects.ProjectCostBreakdown;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.views.projects.ProjectFinancialSummary;
import com.codeflowx.govern.entity.views.projects.ProjectPortfolioDashboard;
import com.codeflowx.govern.entity.views.projects.ProjectResourceAllocation;
import com.codeflowx.govern.entity.views.projects.ProjectRiskAssessment;
import com.codeflowx.govern.service.projects.ProjectPortfolioDashboardService;
import com.codeflowx.govern.service.projects.ProjectFinancialSummaryService;
import com.codeflowx.govern.service.projects.ProjectResourceAllocationService;
import com.codeflowx.govern.service.projects.ProjectRiskAssessmentService;
import com.codeflowx.govern.service.projects.ProjectCostBreakdownService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;

/**
 * ViewModel para el Dashboard de Projects
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProjectsDashboardViewModel extends BaseFront<ProjectsDashboardViewModel>{

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ProjectsDashboardViewModel.class);

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private ModelService modelService;
    
    @WireVariable
    private ProjectPortfolioDashboardService projectPortfolioDashboardService;
    
    @WireVariable
    private ProjectFinancialSummaryService projectFinancialSummaryService;
    
    @WireVariable
    private ProjectResourceAllocationService projectResourceAllocationService;
    
    @WireVariable
    private ProjectRiskAssessmentService projectRiskAssessmentService;
    
    @WireVariable
    private ProjectCostBreakdownService projectCostBreakdownService;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    
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
    
    private ProjectPortfolioDashboard portfolioDashboard;
    private List<ProjectFinancialSummary> financialSummaries = new ArrayList<>();
    private List<ProjectResourceAllocation> resourceAllocations = new ArrayList<>();
    private List<ProjectRiskAssessment> riskAssessments = new ArrayList<>();
    private List<ProjectCostBreakdown> costBreakdowns = new ArrayList<>();
    
    private Long totalProjects = 0L;
    private Long activeProjects = 0L;
    private BigDecimal totalBudget = BigDecimal.ZERO;
    private BigDecimal totalCost = BigDecimal.ZERO;
    private Integer onTimeProjects = 0;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();
        
        log.info("Inicializando ProjectsDashboardViewModel");
        
        loadPortfolioDashboard();
        loadFinancialSummaries();
        loadResourceAllocations();
        loadRiskAssessments();
        loadCostBreakdowns();
        calculateKPIs();
    }
    
    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("prjcreatedat")
                .build();
    }

    

    private void loadPortfolioDashboard() {
        try {
            PageResult<ProjectPortfolioDashboard> result = projectPortfolioDashboardService.findAll(
                pageParams, new Criterias()
            );
            
            if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
                portfolioDashboard = result.getContent().get(0);
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar portfolio dashboard", e);
        }
    }
    
    private void loadFinancialSummaries() {
        try {
            PageResult<ProjectFinancialSummary> result = projectFinancialSummaryService.findAll(
                pageParams, new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                financialSummaries = result.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar financial summaries", e);
        }
    }
    
    private void loadResourceAllocations() {
        try {
            PageResult<ProjectResourceAllocation> result = projectResourceAllocationService.findAll(
                pageParams, new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                resourceAllocations = result.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar resource allocations", e);
        }
    }
    
    private void loadRiskAssessments() {
        try {
            PageResult<ProjectRiskAssessment> result = projectRiskAssessmentService.findAll(
                pageParams, new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                riskAssessments = result.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar risk assessments", e);
        }
    }
    
    private void loadCostBreakdowns() {
        try {
            PageResult<ProjectCostBreakdown> result = projectCostBreakdownService.findAll(
                pageParams, new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                costBreakdowns = result.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar cost breakdowns", e);
        }
    }
    
    private void calculateKPIs() {
        try {
            if (portfolioDashboard != null) {
                totalProjects = portfolioDashboard.getTotalItems() != null ? portfolioDashboard.getTotalItems() : 0L;
                // activeItems es String, no Long - parsear si es necesario
                activeProjects = portfolioDashboard.getActiveItems() != null ? 
                    parseLong(portfolioDashboard.getActiveItems()) : 0L;
            }
            
            // financialSummaries y costBreakdowns también tienen getTotalItems() Long
            totalBudget = financialSummaries.stream()
                .filter(f -> f.getTotalItems() != null)
                .map(f -> BigDecimal.valueOf(f.getTotalItems()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            totalCost = costBreakdowns.stream()
                .filter(c -> c.getTotalItems() != null)
                .map(c -> BigDecimal.valueOf(c.getTotalItems()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            onTimeProjects = (int) riskAssessments.stream()
                .filter(r -> r.getTotalItems() != null && r.getTotalItems() > 0)
                .count();
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }
    
    private Long parseLong(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Long.parseLong(value.trim()) : 0L;
        } catch (NumberFormatException e) {
            log.warn("No se pudo parsear valor Long: {}", value);
            return 0L;
        }
    }

    @Command
    @NotifyChange("*")
    public void refreshDashboard() {
        log.info("Refrescando dashboard de projects");
        try {
            loadPortfolioDashboard();
            loadFinancialSummaries();
            loadResourceAllocations();
            loadRiskAssessments();
            loadCostBreakdowns();
            calculateKPIs();
            
            Messagebox.show("Dashboard actualizado correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al refrescar dashboard", e);
        }
    }

    public ProjectPortfolioDashboard getPortfolioDashboard() { return portfolioDashboard; }
    public List<ProjectFinancialSummary> getFinancialSummaries() { return financialSummaries; }
    public List<ProjectResourceAllocation> getResourceAllocations() { return resourceAllocations; }
    public List<ProjectRiskAssessment> getRiskAssessments() { return riskAssessments; }
    public List<ProjectCostBreakdown> getCostBreakdowns() { return costBreakdowns; }
    public Long getTotalProjects() { return totalProjects; }
    public Long getActiveProjects() { return activeProjects; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public BigDecimal getTotalCost() { return totalCost; }
    public Integer getOnTimeProjects() { return onTimeProjects; }

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            portfolioDashboard = null;
            if (financialSummaries != null) { financialSummaries.clear(); financialSummaries = null; }
            if (resourceAllocations != null) { resourceAllocations.clear(); resourceAllocations = null; }
            if (riskAssessments != null) { riskAssessments.clear(); riskAssessments = null; }
            if (costBreakdowns != null) { costBreakdowns.clear(); costBreakdowns = null; }
            pageParams = null;
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}

