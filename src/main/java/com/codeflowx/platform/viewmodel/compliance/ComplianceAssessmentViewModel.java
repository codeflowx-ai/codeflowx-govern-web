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
import com.codeflowx.govern.entity.compliance.ComplianceAssessment;
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
 * ViewModel para gestión de Compliance Assessments según EU AI Act Anexo VI (Art. 43)
 * 
 * Permite crear, visualizar y gestionar evaluaciones de conformidad para sistemas de IA.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ComplianceAssessmentViewModel extends BaseFront<ComplianceAssessmentViewModel>{

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";

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
    private PageResult<ComplianceAssessment> pageResult;

    // ========== Filtros ==========
    private String filterAssessmentType;
    private Boolean filterCompliant;
    private Boolean filterReadyForCertification;
    private Timestamp filterDateFrom;
    private Timestamp filterDateTo;

    // ========== Selección ==========
    private ComplianceAssessment selectedAssessment;

    // ========== Datos Vista ==========
    private List<ComplianceAssessment> assessments;
    private List<Project> projects;

    // ========== Estadísticas ==========
    private Long totalAssessments = 0L;
    private Long compliantAssessments = 0L;
    private Long readyForCertification = 0L;
    private BigDecimal averageScore = BigDecimal.ZERO;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initDao();
        initPageParams();
        loadAssessments();
        loadProjects();
        calculateStatistics();
    }

    private void initPageParams() {
        pageParams = new PageParams();
        pageParams.setPage(0);
        pageParams.setSize(20);
    }

    /**
     * Carga assessments con paginación y filtros
     */
    private void loadAssessments() {
        try {
            Criterias criterias = new Criterias();

            // Filtros
            if (filterAssessmentType != null && !filterAssessmentType.isEmpty()) {
                criterias.add(new Criteria("comassessmenttype", Operation.EQUAL, filterAssessmentType));
            }
            if (filterCompliant != null) {
                criterias.add(new Criteria("comannexvicompliant", Operation.EQUAL, filterCompliant));
            }
            if (filterReadyForCertification != null) {
                criterias.add(new Criteria("comreadyforcertification", Operation.EQUAL, filterReadyForCertification));
            }
            if (filterDateFrom != null) {
                criterias.add(new Criteria("comassessmentdate", Operation.GREATER_THAN_OR_EQUAL, filterDateFrom));
            }
            if (filterDateTo != null) {
                criterias.add(new Criteria("comassessmentdate", Operation.LESS_THAN_OR_EQUAL, filterDateTo));
            }

            pageResult = businessService.findByCriteriaWithPagination(ComplianceAssessment.class, criterias, pageParams);
            assessments = pageResult != null ? pageResult.getResult() : new ArrayList<>();

            log.info("Loaded {} compliance assessments", assessments.size());
        } catch (Exception e) {
            log.error("Error loading compliance assessments", e);
            Messagebox.show("Error al cargar evaluaciones de conformidad", "Error", Messagebox.OK, Messagebox.ERROR);
            assessments = new ArrayList<>();
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
            // Total assessments
            totalAssessments = businessService.count(ComplianceAssessment.class, new Criterias());

            // Compliant assessments
            Criterias compliantCriteria = new Criterias();
            compliantCriteria.add(new Criteria("comannexvicompliant", Operation.EQUAL, true));
            compliantAssessments = businessService.count(ComplianceAssessment.class, compliantCriteria);

            // Ready for certification
            Criterias readyCriteria = new Criterias();
            readyCriteria.add(new Criteria("comreadyforcertification", Operation.EQUAL, true));
            readyForCertification = businessService.count(ComplianceAssessment.class, readyCriteria);

            // Average score
            List<ComplianceAssessment> allAssessments = businessService.findAll(ComplianceAssessment.class);
            if (!allAssessments.isEmpty()) {
                BigDecimal sum = allAssessments.stream()
                    .map(ComplianceAssessment::getComoverallscore)
                    .filter(score -> score != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                averageScore = sum.divide(BigDecimal.valueOf(allAssessments.size()), 2, BigDecimal.ROUND_HALF_UP);
            }

            log.info("Statistics calculated - Total: {}, Compliant: {}, Ready: {}, Avg Score: {}", 
                totalAssessments, compliantAssessments, readyForCertification, averageScore);
        } catch (Exception e) {
            log.error("Error calculating statistics", e);
        }
    }

    // ========== Commands ==========

    @Command
    @NotifyChange({"assessments", "pageResult"})
    public void search() {
        initPageParams();
        loadAssessments();
    }

    @Command
    @NotifyChange({"filterAssessmentType", "filterCompliant", "filterReadyForCertification", 
                   "filterDateFrom", "filterDateTo", "assessments"})
    public void clearFilters() {
        filterAssessmentType = null;
        filterCompliant = null;
        filterReadyForCertification = null;
        filterDateFrom = null;
        filterDateTo = null;
        loadAssessments();
    }

    @Command
    @NotifyChange("selectedAssessment")
    public void selectAssessment(@BindingParam("assessment") ComplianceAssessment assessment) {
        this.selectedAssessment = assessment;
        log.info("Selected assessment: {}", assessment.getIdxcomplianceassessment());
    }

    @Command
    @NotifyChange({"assessments", "totalAssessments", "compliantAssessments", 
                   "readyForCertification", "averageScore"})
    public void createNewAssessment() {
        try {
            ComplianceAssessment newAssessment = new ComplianceAssessment();
            newAssessment.setComassessmentdate(new Timestamp(System.currentTimeMillis()));
            newAssessment.setComannexvicompliant(false);
            newAssessment.setComreadyforcertification(false);

            businessService.save(newAssessment);
            loadAssessments();
            calculateStatistics();

            Messagebox.show("Evaluación de conformidad creada exitosamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            log.info("New compliance assessment created");
        } catch (Exception e) {
            log.error("Error creating compliance assessment", e);
            Messagebox.show("Error al crear evaluación de conformidad", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"assessments", "selectedAssessment"})
    public void deleteAssessment(@BindingParam("assessment") ComplianceAssessment assessment) {
        try {
            Messagebox.show(
                "¿Está seguro de eliminar esta evaluación?",
                "Confirmación",
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            businessService.delete(assessment);
                            loadAssessments();
                            calculateStatistics();
                            selectedAssessment = null;
                            Messagebox.show("Evaluación eliminada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                        } catch (Exception e) {
                            log.error("Error deleting assessment", e);
                            Messagebox.show("Error al eliminar evaluación", "Error", Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                }
            );
        } catch (Exception e) {
            log.error("Error showing delete confirmation", e);
        }
    }

    @Command
    @NotifyChange({"assessments", "pageResult"})
    public void onPaging() {
        loadAssessments();
    }
}

