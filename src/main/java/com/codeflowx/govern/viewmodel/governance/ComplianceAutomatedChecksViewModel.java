package com.codeflowx.govern.viewmodel.governance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.service.governance.ComplianceAssessmentService;
import com.codeflowx.govern.entity.governance.ComplianceFinding;
import com.codeflowx.govern.entity.views.governance.ComplianceGapsAnalysis;
import com.codeflowx.govern.entity.procedures.governance.BulkComplianceCheck;
import com.codeflowx.govern.entity.procedures.governance.RunComplianceCheck;
import com.codeflowx.govern.service.governance.ComplianceFindingService;
import com.codeflowx.govern.service.governance.ComplianceGapsAnalysisService;
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

/**
 * ViewModel para ejecución y visualización de compliance checks automáticos
 * Permite ejecutar verificaciones masivas y ver resultados en tiempo real
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ComplianceAutomatedChecksViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private ComplianceAssessmentService complianceAssessmentService;
    @WireVariable
    private ComplianceFindingService complianceFindingService;
    @WireVariable
    private ComplianceGapsAnalysisService complianceGapsAnalysisService;
    @WireVariable
    private BusinessService businessService; // Mantener para procedimientos almacenados


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
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;

    // ========== Datos ==========
    private List<ComplianceAssessment> assessments = new ArrayList<>();
    private List<ComplianceFinding> findings = new ArrayList<>();
    private List<ComplianceGapsAnalysis> gapsAnalysis = new ArrayList<>();

    // ========== KPIs ==========
    private Long totalChecks = 0L;
    private Long passedChecks = 0L;
    private Long failedChecks = 0L;
    private Long totalFindings = 0L;
    private Long criticalFindings = 0L;
    private BigDecimal averageScore = BigDecimal.ZERO;

    // ========== Estado de ejecución ==========
    private boolean isRunningBulkCheck = false;
    private String lastCheckStatus = "";
    private Timestamp lastCheckDate;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();

        log.info("Inicializando ComplianceAutomatedChecksViewModel");

        loadAssessments();
        loadFindings();
        loadGapsAnalysis();
        calculateKPIs();
    }

    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("performedat")
                .build();
    }

    // ========== Carga de Datos ==========

    private void loadAssessments() {
        try {
            log.debug("Cargando compliance assessments");

            PageResult<ComplianceAssessment> result = complianceAssessmentService.findAll(pageParams, new Criterias());

            if (result != null && result.getContent() != null) {
                assessments = result.getContent();
                log.info("Cargados {} assessments", assessments.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar assessments", e);
        }
    }

    private void loadFindings() {
        try {
            log.debug("Cargando compliance findings");

            PageResult<ComplianceFinding> result = complianceFindingService.findAll(pageParams, new Criterias());

            if (result != null && result.getContent() != null) {
                findings = result.getContent();
                log.info("Cargados {} findings", findings.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar findings", e);
        }
    }

    private void loadGapsAnalysis() {
        try {
            log.debug("Cargando gaps analysis");

            PageResult<ComplianceGapsAnalysis> result = complianceGapsAnalysisService.findAll(
                pageParams,
                new Criterias()
            );

            if (result != null && result.getContent() != null) {
                gapsAnalysis = result.getContent();
                log.info("Cargados {} gaps analysis", gapsAnalysis.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar gaps analysis", e);
        }
    }

    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs");

            totalChecks = (long) assessments.size();

            passedChecks = assessments.stream()
                .filter(a -> "PASS".equals(a.getStatus()))
                .count();

            failedChecks = assessments.stream()
                .filter(a -> "FAIL".equals(a.getStatus()))
                .count();

            totalFindings = (long) findings.size();

            criticalFindings = findings.stream()
                .filter(f -> "CRITICAL".equals(f.getSeverity()))
                .count();

            if (!assessments.isEmpty()) {
                BigDecimal sum = assessments.stream()
                    .filter(a -> a.getOverallscore() != null)
                    .map(ComplianceAssessment::getOverallscore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                averageScore = sum.divide(
                    BigDecimal.valueOf(assessments.size()),
                    2,
                    RoundingMode.HALF_UP
                );
            }

            log.info("KPIs calculados - Checks: {}, Passed: {}, Findings: {}",
                totalChecks, passedChecks, totalFindings);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    // ========== Comandos de Compliance Checks ==========

    /**
     * Ejecuta bulk compliance check para todos los frameworks
     */
    @Command
    @NotifyChange({"assessments", "findings", "totalChecks", "isRunningBulkCheck", "lastCheckStatus"})
    public void runBulkComplianceCheck() {
        log.info("Ejecutando bulk compliance check");
        try {
            isRunningBulkCheck = true;
            lastCheckStatus = "RUNNING";

            BulkComplianceCheck procedure = new BulkComplianceCheck();
            procedure = businessService.callProcedure(procedure);

            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                lastCheckStatus = "COMPLETED";
                lastCheckDate = new Timestamp(System.currentTimeMillis());

                loadAssessments();
                loadFindings();
                calculateKPIs();

                Messagebox.show("Bulk compliance check ejecutado correctamente\nTotal checks: " + totalChecks,
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                lastCheckStatus = "FAILED";
                Messagebox.show("El bulk check no se completó correctamente",
                    "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al ejecutar bulk check", e);
            lastCheckStatus = "ERROR";
            Messagebox.show("Error al ejecutar bulk check: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        } finally {
            isRunningBulkCheck = false;
        }
    }

    // ========== Cleanup ==========

    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            if (assessments != null) {
                assessments.clear();
                assessments = null;
            }
            if (findings != null) {
                findings.clear();
                findings = null;
            }
            if (gapsAnalysis != null) {
                gapsAnalysis.clear();
                gapsAnalysis = null;
            }

            pageParams = null;
            complianceAssessmentService = null;
            complianceFindingService = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
