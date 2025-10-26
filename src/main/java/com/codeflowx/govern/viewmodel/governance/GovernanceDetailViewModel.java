package com.codeflowx.govern.viewmodel.governance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.functions.governance.CalculatePolicyScore;
import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.entity.governance.Policy;
import com.codeflowx.govern.entity.governance.PolicyEvaluation;
import com.codeflowx.govern.entity.governance.PolicyRule;
import com.codeflowx.govern.entity.governance.PolicyViolation;
import com.codeflowx.govern.entity.procedures.governance.EvaluatePolicy;
import com.codeflowx.govern.entity.procedures.governance.RunComplianceCheck;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

/**
 * ViewModel para la pantalla de detalle y edición de una política de governance.
 * Incluye la lógica para crear, editar, y ejecutar operaciones de negocio
 * como evaluación de políticas y verificación de cumplimiento. También carga información descendente
 * como reglas, evaluaciones, violaciones y evaluaciones de cumplimiento.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class GovernanceDetailViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
    @WireVariable private BusinessService businessService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;

    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Modo de operación ==========
    private String mode; // "create" o "edit"
    private Long policyId;
    private boolean isEditing = false;
    private String pageTitle = "Detalle de la Política";

    // ========== Datos de la política ==========
    private Policy currentPolicy;

    // ========== Información descendente ==========
    private List<PolicyRule> policyRules = new ArrayList<>();
    private List<PolicyEvaluation> policyEvaluations = new ArrayList<>();
    private List<PolicyViolation> policyViolations = new ArrayList<>();
    private List<ComplianceAssessment> complianceAssessments = new ArrayList<>();

    // ========== Estadísticas específicas de la política ==========
    private Long totalRules = 0L;
    private Long totalEvaluations = 0L;
    private Long totalViolations = 0L;
    private Long totalAssessments = 0L;
    private BigDecimal currentPolicyScore = BigDecimal.ZERO;
    private BigDecimal averageComplianceScore = BigDecimal.ZERO;
    private String lastEvaluationStatus = "NOT_EVALUATED";
    private Timestamp lastEvaluationDate;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        // Obtener parámetros de navegación
        mode = (String) Executions.getCurrent().getParameter("mode");
        String policyIdStr = Executions.getCurrent().getParameter("policyId");

        if (policyIdStr != null) {
            policyId = Long.parseLong(policyIdStr);
        }

        log.info("Inicializando GovernanceDetailViewModel - mode: {}, policyId: {}", mode, policyId);

        if ("create".equals(mode)) {
            initNewPolicy();
        } else if ("edit".equals(mode) && policyId != null) {
            loadPolicy(policyId);
        } else {
            log.error("Modo inválido o falta policyId");
            Executions.sendRedirect("/governance/governance-overview.zul");
        }
    }

    /**
     * Inicializa una nueva política con valores por defecto
     */
    private void initNewPolicy() {
        log.debug("Inicializando nueva política");
        currentPolicy = new Policy();
        currentPolicy.setCreatedat(new Timestamp(System.currentTimeMillis()));
        currentPolicy.setUpdatedat(new Timestamp(System.currentTimeMillis()));
        currentPolicy.setCreatedby(1L); // TODO: Obtener usuario actual
        currentPolicy.setStatus("DRAFT");
        currentPolicy.setVersion(1);
        currentPolicy.setEnforcementlevel("MEDIUM");
        currentPolicy.setPolicytype("COMPLIANCE");

        isEditing = false;
        pageTitle = Labels.getLabel("governance.detail.title.create");
    }

    /**
     * Carga política existente desde BD
     */
    private void loadPolicy(Long id) {
        try {
            log.debug("Cargando política ID={}", id);

            currentPolicy = businessService.findById(Policy.class, id);

            if (currentPolicy == null) {
                log.error("Política no encontrada: ID={}", id);
                Messagebox.show(Labels.getLabel("governance.error.notfound"), Labels.getLabel("governance.error.title"),
                    Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/governance/governance-overview.zul");
                return;
            }

            log.info("Política cargada: {}", currentPolicy.getName());

            isEditing = true;
            pageTitle = Labels.getLabel("governance.detail.title.edit") + ": " + currentPolicy.getName();

            // Cargar información descendente
            loadPolicyRules();
            loadPolicyEvaluations();
            loadPolicyViolations();
            loadComplianceAssessments();
            loadPolicyStatistics();

        } catch (Exception e) {
            log.error("Error al cargar política ID={}", id, e);
            Messagebox.show(Labels.getLabel("governance.error.load") + ": " + e.getMessage(),
                Labels.getLabel("governance.error.title"), Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/governance/governance-overview.zul");
        }
    }

    /**
     * Carga estadísticas específicas de la política
     */
    private void loadPolicyStatistics() {
        try {
            log.debug("Cargando estadísticas de la política ID={}", currentPolicy.getIdxpolicy());

            // Contar reglas
            totalRules = (long) policyRules.size();

            // Contar evaluaciones
            totalEvaluations = (long) policyEvaluations.size();

            // Contar violaciones
            totalViolations = (long) policyViolations.size();

            // Contar evaluaciones de cumplimiento
            totalAssessments = (long) complianceAssessments.size();

            // Calcular score de política desde evaluaciones
            if (!policyEvaluations.isEmpty()) {
                currentPolicyScore = policyEvaluations.stream()
                    .filter(e -> e.getConfidencescore() != null)
                    .map(e -> e.getConfidencescore())
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(policyEvaluations.size()), 2, RoundingMode.HALF_UP);
            } else {
                currentPolicyScore = BigDecimal.ZERO;
            }

            // Calcular score de cumplimiento promedio
            if (!complianceAssessments.isEmpty()) {
                averageComplianceScore = complianceAssessments.stream()
                    .filter(a -> a.getOverallscore() != null)
                    .map(ComplianceAssessment::getOverallscore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(complianceAssessments.size()), 2, RoundingMode.HALF_UP);
            } else {
                averageComplianceScore = BigDecimal.ZERO;
            }

            log.info("Estadísticas cargadas - Reglas: {}, Evaluaciones: {}, Violaciones: {}",
                totalRules, totalEvaluations, totalViolations);

        } catch (Exception e) {
            log.error("Error al cargar estadísticas de la política", e);
        }
    }

    // ========== Información descendente ==========

    @Command
    @NotifyChange({"policyRules", "totalRules"})
    public void loadPolicyRules() {
        try {
            log.debug("Cargando reglas de la política ID={}", currentPolicy.getIdxpolicy());

            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDGOVPOLICIES0");
            criteria.setValueEnd(currentPolicy.getIdxpolicy());
            criterias.addCriteria(criteria);

            PageResult<PolicyRule> result = businessService.findAllEntity(
                PolicyRule.class,
                params,
                criterias
            );

            if (result != null && result.getContent() != null) {
                policyRules = result.getContent();
                totalRules = (long) policyRules.size();
                log.info("Cargadas {} reglas de la política", totalRules);
            } else {
                policyRules = new ArrayList<>();
                totalRules = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar reglas de la política", e);
            policyRules = new ArrayList<>();
            totalRules = 0L;
        }
    }

    @Command
    @NotifyChange({"policyEvaluations", "totalEvaluations"})
    public void loadPolicyEvaluations() {
        try {
            log.debug("Cargando evaluaciones de la política ID={}", currentPolicy.getIdxpolicy());

            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "IDGOVPOLICIES0");
            criteria.setValueEnd(currentPolicy.getIdxpolicy());
            criterias.addCriteria(criteria);

            PageResult<PolicyEvaluation> result = businessService.findAllEntity(
                PolicyEvaluation.class,
                params,
                criterias
            );

            if (result != null && result.getContent() != null) {
                policyEvaluations = result.getContent();
                totalEvaluations = (long) policyEvaluations.size();
                log.info("Cargadas {} evaluaciones de la política", totalEvaluations);
            } else {
                policyEvaluations = new ArrayList<>();
                totalEvaluations = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar evaluaciones de la política", e);
            policyEvaluations = new ArrayList<>();
            totalEvaluations = 0L;
        }
    }

    @Command
    @NotifyChange({"policyViolations", "totalViolations"})
    public void loadPolicyViolations() {
        try {
            log.debug("Cargando violaciones de la política ID={}", currentPolicy.getIdxpolicy());

            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "policyname");
            criteria.setValueEnd(currentPolicy.getName());
            criterias.addCriteria(criteria);

            PageResult<PolicyViolation> result = businessService.findAllEntity(
                PolicyViolation.class,
                params,
                criterias
            );

            if (result != null && result.getContent() != null) {
                policyViolations = result.getContent();
                totalViolations = (long) policyViolations.size();
                log.info("Cargadas {} violaciones de la política", totalViolations);
            } else {
                policyViolations = new ArrayList<>();
                totalViolations = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar violaciones de la política", e);
            policyViolations = new ArrayList<>();
            totalViolations = 0L;
        }
    }

    @Command
    @NotifyChange({"complianceAssessments", "totalAssessments"})
    public void loadComplianceAssessments() {
        try {
            log.debug("Cargando evaluaciones de cumplimiento de la política ID={}", currentPolicy.getIdxpolicy());

            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            PageResult<ComplianceAssessment> result = businessService.findAllEntity(
                ComplianceAssessment.class,
                params,
                new Criterias()
            );

            if (result != null && result.getContent() != null) {
                complianceAssessments = result.getContent();
                totalAssessments = (long) complianceAssessments.size();
                log.info("Cargadas {} evaluaciones de cumplimiento", totalAssessments);
            } else {
                complianceAssessments = new ArrayList<>();
                totalAssessments = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar evaluaciones de cumplimiento", e);
            complianceAssessments = new ArrayList<>();
            totalAssessments = 0L;
        }
    }

    // ========== Comandos CRUD ==========

    @Command
    @NotifyChange({"currentPolicy", "isEditing", "pageTitle", "policyRules", "policyEvaluations", "policyViolations", "complianceAssessments", "totalRules", "totalEvaluations", "totalViolations", "totalAssessments", "currentPolicyScore", "averageComplianceScore"})
    public void savePolicy() {
        try {
            log.info("Guardando política: {}", currentPolicy.getName());

            // Validaciones de negocio
            if (currentPolicy.getName() == null || currentPolicy.getName().trim().isEmpty()) {
                Messagebox.show(Labels.getLabel("governance.validation.required.name"),
                    Labels.getLabel("governance.validation.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (currentPolicy.getCategory() == null || currentPolicy.getCategory().trim().isEmpty()) {
                Messagebox.show(Labels.getLabel("governance.validation.required.category"),
                    Labels.getLabel("governance.validation.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (currentPolicy.getPolicytype() == null || currentPolicy.getPolicytype().trim().isEmpty()) {
                Messagebox.show(Labels.getLabel("governance.validation.required.type"),
                    Labels.getLabel("governance.validation.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (currentPolicy.getEnforcementlevel() == null || currentPolicy.getEnforcementlevel().trim().isEmpty()) {
                Messagebox.show(Labels.getLabel("governance.validation.required.enforcement"),
                    Labels.getLabel("governance.validation.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (currentPolicy.getIdxpolicy() == null) {
                businessService.save(currentPolicy);
                log.info("Política creada exitosamente: ID={}, nombre={}",
                    currentPolicy.getIdxpolicy(), currentPolicy.getName());
                Messagebox.show(Labels.getLabel("governance.success.created"),
                    Labels.getLabel("governance.success.title"), Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPolicy.setUpdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.update(currentPolicy);
                log.info("Política actualizada exitosamente: ID={}, nombre={}",
                    currentPolicy.getIdxpolicy(), currentPolicy.getName());
                Messagebox.show(Labels.getLabel("governance.success.updated"),
                    Labels.getLabel("governance.success.title"), Messagebox.OK, Messagebox.INFORMATION);
            }

            Executions.sendRedirect("/governance/governance-overview.zul");

        } catch (Exception e) {
            log.error("Error al guardar política", e);
            Messagebox.show(Labels.getLabel("governance.error.save") + ": " + e.getMessage(),
                Labels.getLabel("governance.error.title"), Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición/creación de política, volviendo a overview");
        Executions.sendRedirect("/governance/governance-overview.zul");
    }

    // ========== Operaciones especiales (funciones/procedimientos) ==========

    @Command
    @NotifyChange({"currentPolicy", "policyEvaluations", "currentPolicyScore"})
    public void evaluatePolicy() {
        if (currentPolicy == null || currentPolicy.getIdxpolicy() == null) {
            Messagebox.show(Labels.getLabel("governance.validation.required.name"),
                Labels.getLabel("governance.warning.title"), Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Ejecutando evaluación de política ID={}", currentPolicy.getIdxpolicy());
        try {
            EvaluatePolicy function = new EvaluatePolicy();
            function.setPPolicyId(currentPolicy.getIdxpolicy());

            function = businessService.callFuction(function);

            if (function != null && function.getOResult() != null) {
                Gson gson = new Gson();
                JsonObject jsonObj = gson.fromJson(function.getOResult().toString(), JsonObject.class);

                boolean success = jsonObj.get("success").getAsBoolean();
                double score = jsonObj.get("score").getAsDouble();

                JsonObject checksObj = jsonObj.getAsJsonObject("checks");
                boolean hasName = checksObj.get("has_name").getAsBoolean();
                boolean hasRules = checksObj.get("has_rules").getAsBoolean();
                boolean hasValidStatus = checksObj.get("has_valid_status").getAsBoolean();
                boolean hasEffectiveDate = checksObj.get("has_effective_date").getAsBoolean();

                String level = score < 50 ? "CRITICAL" : (score < 80 ? "WARNING" : "SUCCESS");
                boolean allValid = hasName && hasRules && hasValidStatus && hasEffectiveDate;

                log.info("Evaluación completada - Score: {}, Level: {}", score, level);

                String message = String.format(
                    "Evaluación de política completada\n\n" +
                    "Score: %.1f/100\n" +
                    "Nivel: %s\n" +
                    "Success: %s\n\n" +
                    "Checks:\n" +
                    "✓ Nombre: %s\n" +
                    "✓ Reglas: %s\n" +
                    "✓ Estado: %s\n" +
                    "✓ Fecha efectiva: %s",
                    score,
                    level,
                    success ? "Sí" : "No",
                    hasName ? "OK" : "FAIL",
                    hasRules ? "OK" : "FAIL",
                    hasValidStatus ? "OK" : "FAIL",
                    hasEffectiveDate ? "OK" : "FAIL"
                );

                Messagebox.show(message, Labels.getLabel("governance.operation.evaluation.title"),
                    Messagebox.OK,
                    allValid ? Messagebox.INFORMATION : Messagebox.EXCLAMATION);
            }

        } catch (Exception e) {
            log.error("Error al evaluar política", e);
            lastEvaluationStatus = "ERROR";
            Messagebox.show(Labels.getLabel("governance.error.evaluation") + ": " + e.getMessage(),
                Labels.getLabel("governance.error.title"), Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"currentPolicy", "policyEvaluations", "currentPolicyScore"})
    public void calculatePolicyScore() {
        if (currentPolicy == null || currentPolicy.getIdxpolicy() == null) {
            Messagebox.show(Labels.getLabel("governance.validation.required.name"),
                Labels.getLabel("governance.warning.title"), Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Calculando score de política ID={}", currentPolicy.getIdxpolicy());
        try {
            CalculatePolicyScore function = new CalculatePolicyScore();
            function.setPPolicyId(currentPolicy.getIdxpolicy());

            function = businessService.callFuction(function);

            if (function != null && function.getOResult() != null) {
                Gson gson = new Gson();
                JsonObject jsonObj = gson.fromJson(function.getOResult().toString(), JsonObject.class);

                double policyScore = jsonObj.get("policy_score").getAsDouble();

                JsonObject metricsObj = jsonObj.getAsJsonObject("metrics");
                int totalRules = metricsObj.get("total_rules").getAsInt();
                int activeRules = metricsObj.get("active_rules").getAsInt();
                int totalEvaluations = metricsObj.get("total_evaluations").getAsInt();
                double avgConfidence = metricsObj.get("avg_confidence").getAsDouble();

                String level = policyScore > 80 ? "EXCELLENT" : (policyScore > 60 ? "GOOD" : "NEEDS_IMPROVEMENT");
                boolean isHighQuality = policyScore > 70;

                log.info("Score calculado - Score: {}, Level: {}", policyScore, level);

                String message = String.format(
                    "Cálculo de Score completado\n\n" +
                    "Policy Score: %.1f\n" +
                    "Nivel: %s\n\n" +
                    "Métricas:\n" +
                    "• Total Reglas: %d\n" +
                    "• Reglas Activas: %d\n" +
                    "• Total Evaluaciones: %d\n" +
                    "• Confianza Promedio: %.2f%%",
                    policyScore,
                    level,
                    totalRules,
                    activeRules,
                    totalEvaluations,
                    avgConfidence
                );

                String icon = isHighQuality ? Messagebox.INFORMATION : Messagebox.EXCLAMATION;
                Messagebox.show(message, Labels.getLabel("governance.operation.score.title"), Messagebox.OK, icon);
            }

        } catch (Exception e) {
            log.error("Error al calcular score de política", e);
            Messagebox.show(Labels.getLabel("governance.error.score") + ": " + e.getMessage(),
                Labels.getLabel("governance.error.title"), Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"complianceAssessments", "totalAssessments"})
    public void runComplianceCheck() {
        if (currentPolicy == null || currentPolicy.getIdxpolicy() == null) {
            Messagebox.show(Labels.getLabel("governance.validation.required.name"),
                Labels.getLabel("governance.warning.title"), Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Ejecutando verificación de cumplimiento para política ID={}", currentPolicy.getIdxpolicy());
        try {
            RunComplianceCheck procedure = new RunComplianceCheck();
            procedure.setPPolicyId(currentPolicy.getIdxpolicy());
            procedure.setPAssessmentName("Verificación automática - " + currentPolicy.getName());
            procedure.setPFramework("INTERNAL");

            procedure = businessService.callProcedure(procedure);

            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                Messagebox.show(Labels.getLabel("governance.success.compliance.check") + "\nAssessment ID: " + procedure.getOResult(),
                    Labels.getLabel("governance.success.title"), Messagebox.OK, Messagebox.INFORMATION);
                loadComplianceAssessments();
                loadPolicyStatistics();
            } else {
                Messagebox.show(Labels.getLabel("governance.warning.compliance.failed"),
                    Labels.getLabel("governance.warning.title"), Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al ejecutar verificación de cumplimiento", e);
            Messagebox.show(Labels.getLabel("governance.error.compliance") + ": " + e.getMessage(),
                Labels.getLabel("governance.error.title"), Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Método de limpieza para optimización de memoria
     * Se ejecuta cuando el ViewModel es destruido
     */
    @Destroy
    public void destroy() {
        log.debug("Limpiando recursos del ViewModel");
        
        // Limpiar todas las referencias para facilitar GC
        businessService = null;
        
        log.debug("Recursos limpiados exitosamente");
    }

}
