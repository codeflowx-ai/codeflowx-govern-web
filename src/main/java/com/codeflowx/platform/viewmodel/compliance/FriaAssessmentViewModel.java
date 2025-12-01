package com.codeflowx.platform.viewmodel.compliance;
import com.codeflowx.framework.zkoss.BaseFront;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
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
import com.codeflowx.govern.entity.compliance.FriaAssessment;
import com.codeflowx.govern.entity.projects.Project;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para gestión de FRIA - Fundamental Rights Impact Assessment según Art. 27
 * 
 * Permite crear y gestionar evaluaciones de impacto en derechos fundamentales.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class FriaAssessmentViewModel extends BaseFront<FriaAssessmentViewModel>{

    private static final long serialVersionUID = 1L;

    @WireVariable
    private BusinessService businessService;

    @Autowired
    protected IEntityLocal dao;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    @WireVariable("APPLICATION_DS")
    protected DataSource ds;

    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<FriaAssessment> pageResult;

    // ========== Filtros ==========
    private Boolean filterArt27Compliant;
    private Boolean filterApproved;
    private Boolean filterNotified;
    private String filterImpactSeverity;

    // ========== Selección ==========
    private FriaAssessment selectedFria;

    // ========== Datos Vista ==========
    private List<FriaAssessment> friaAssessments;
    private List<Project> projects;

    // ========== Estadísticas ==========
    private Long totalFrias = 0L;
    private Long art27Compliant = 0L;
    private Long approved = 0L;
    private Long notified = 0L;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initDao();
        initPageParams();
        loadFriaAssessments();
        loadProjects();
        calculateStatistics();
    }

    private void initPageParams() {
        pageParams = new PageParams();
        pageParams.setPage(0);
        pageParams.setSize(20);
    }

    /**
     * Carga FRIAs con paginación y filtros
     */
    private void loadFriaAssessments() {
        try {
            Criterias criterias = new Criterias();

            // Filtros
            if (filterArt27Compliant != null) {
                criterias.add(new Criteria("friaart27compliant", Operation.EQUAL, filterArt27Compliant));
            }
            if (filterApproved != null) {
                criterias.add(new Criteria("friaapproved", Operation.EQUAL, filterApproved));
            }
            if (filterNotified != null) {
                criterias.add(new Criteria("frianotified", Operation.EQUAL, filterNotified));
            }
            if (filterImpactSeverity != null && !filterImpactSeverity.isEmpty()) {
                criterias.add(new Criteria("friaimpactseverity", Operation.EQUAL, filterImpactSeverity));
            }

            pageResult = businessService.findByCriteriaWithPagination(FriaAssessment.class, criterias, pageParams);
            friaAssessments = pageResult != null ? pageResult.getResult() : new ArrayList<>();

            log.info("Loaded {} FRIA assessments", friaAssessments.size());
        } catch (Exception e) {
            log.error("Error loading FRIA assessments", e);
            Messagebox.show("Error al cargar evaluaciones FRIA", "Error", Messagebox.OK, Messagebox.ERROR);
            friaAssessments = new ArrayList<>();
        }
    }

    /**
     * Carga proyectos para dropdown
     */
    private void loadProjects() {
        try {
            projects = businessService.findAll(Project.class);
            log.info("Loaded {} projects", projects.size());
        } catch (Exception e) {
            log.error("Error loading projects", e);
            projects = new ArrayList<>();
        }
    }

    /**
     * Calcula estadísticas del dashboard
     */
    private void calculateStatistics() {
        try {
            // Total FRIAs
            totalFrias = businessService.count(FriaAssessment.class, new Criterias());

            // Art 27 Compliant
            Criterias compliantCriteria = new Criterias();
            compliantCriteria.add(new Criteria("friaart27compliant", Operation.EQUAL, true));
            art27Compliant = businessService.count(FriaAssessment.class, compliantCriteria);

            // Approved
            Criterias approvedCriteria = new Criterias();
            approvedCriteria.add(new Criteria("friaapproved", Operation.EQUAL, true));
            approved = businessService.count(FriaAssessment.class, approvedCriteria);

            // Notified
            Criterias notifiedCriteria = new Criterias();
            notifiedCriteria.add(new Criteria("frianotified", Operation.EQUAL, true));
            notified = businessService.count(FriaAssessment.class, notifiedCriteria);

            log.info("Statistics calculated - Total: {}, Compliant: {}, Approved: {}, Notified: {}", 
                totalFrias, art27Compliant, approved, notified);
        } catch (Exception e) {
            log.error("Error calculating statistics", e);
        }
    }

    // ========== Commands ==========

    @Command
    @NotifyChange({"friaAssessments", "pageResult"})
    public void search() {
        initPageParams();
        loadFriaAssessments();
    }

    @Command
    @NotifyChange({"filterArt27Compliant", "filterApproved", "filterNotified", 
                   "filterImpactSeverity", "friaAssessments"})
    public void clearFilters() {
        filterArt27Compliant = null;
        filterApproved = null;
        filterNotified = null;
        filterImpactSeverity = null;
        loadFriaAssessments();
    }

    @Command
    @NotifyChange("selectedFria")
    public void selectFria(@BindingParam("fria") FriaAssessment fria) {
        this.selectedFria = fria;
        log.info("Selected FRIA: {}", fria.getIdxfriaassessment());
    }

    @Command
    @NotifyChange({"friaAssessments", "totalFrias", "art27Compliant", "approved", "notified"})
    public void createNewFria() {
        try {
            FriaAssessment newFria = new FriaAssessment();
            newFria.setFriaart27compliant(false);
            newFria.setFriaapproved(false);
            newFria.setFrianotified(false);
            newFria.setFriadpiaintegrated(false);
            newFria.setFriahitlenabled(false);
            newFria.setFriavulnerablegroupsincluded(false);

            businessService.save(newFria);
            loadFriaAssessments();
            calculateStatistics();

            Messagebox.show("FRIA creada exitosamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            log.info("New FRIA assessment created");
        } catch (Exception e) {
            log.error("Error creating FRIA assessment", e);
            Messagebox.show("Error al crear FRIA", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"friaAssessments", "selectedFria"})
    public void deleteFria(@BindingParam("fria") FriaAssessment fria) {
        try {
            Messagebox.show(
                "¿Está seguro de eliminar esta FRIA?",
                "Confirmación",
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            businessService.delete(fria);
                            loadFriaAssessments();
                            calculateStatistics();
                            selectedFria = null;
                            Messagebox.show("FRIA eliminada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                        } catch (Exception e) {
                            log.error("Error deleting FRIA", e);
                            Messagebox.show("Error al eliminar FRIA", "Error", Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                }
            );
        } catch (Exception e) {
            log.error("Error showing delete confirmation", e);
        }
    }

    @Command
    @NotifyChange({"friaAssessments", "pageResult"})
    public void onPaging() {
        loadFriaAssessments();
    }
}

