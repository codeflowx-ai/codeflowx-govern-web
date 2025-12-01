package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.business.compliance.ComplianceDashboardService;
import com.codeflowx.govern.business.exception.BussinessException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel para Dashboard Consolidado de Compliance.
 * INC-017: Dashboard Consolidado Compliance
 *
 * Muestra:
 * - KPIs de compliance (% completitud, riesgos, evaluaciones pendientes)
 * - Estado de clasificación
 * - Estado de FRIA
 * - Evaluaciones técnicas pendientes
 * - Alertas y acciones requeridas
 * - Timeline de compliance
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ComplianceDashboardViewModel extends BaseFront<ComplianceDashboardViewModel> {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private ComplianceDashboardService complianceDashboardService;

    // Datos del dashboard
    private Map<String, Object> complianceMetrics = new HashMap<>();

    // KPIs
    private Long totalAssessments = 0L;
    private Long completedAssessments = 0L;
    private Long totalFria = 0L;
    private Long approvedFria = 0L;
    private Long totalEuRegistrations = 0L;
    private Long submittedEuRegistrations = 0L;
    private BigDecimal averageComplianceScore = BigDecimal.ZERO;
    private Double completionRate = 0.0;
    private Double friaApprovalRate = 0.0;
    private Double euSubmissionRate = 0.0;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        loadDashboard();
    }

    @Command
    @NotifyChange({"complianceMetrics", "totalAssessments", "completedAssessments",
                   "totalFria", "approvedFria", "totalEuRegistrations", "submittedEuRegistrations",
                   "averageComplianceScore", "completionRate", "friaApprovalRate", "euSubmissionRate"})
    public void loadDashboard() {
        try {
            complianceMetrics = complianceDashboardService.getComplianceMetrics();

            // Extraer KPIs
            totalAssessments = (Long) complianceMetrics.getOrDefault("totalAssessments", 0L);
            completedAssessments = (Long) complianceMetrics.getOrDefault("completedAssessments", 0L);
            totalFria = (Long) complianceMetrics.getOrDefault("totalFria", 0L);
            approvedFria = (Long) complianceMetrics.getOrDefault("approvedFria", 0L);
            totalEuRegistrations = (Long) complianceMetrics.getOrDefault("totalEuRegistrations", 0L);
            submittedEuRegistrations = (Long) complianceMetrics.getOrDefault("submittedEuRegistrations", 0L);

            Object avgScore = complianceMetrics.get("averageComplianceScore");
            if (avgScore instanceof BigDecimal) {
                averageComplianceScore = (BigDecimal) avgScore;
            } else if (avgScore instanceof Number) {
                averageComplianceScore = BigDecimal.valueOf(((Number) avgScore).doubleValue());
            }

            completionRate = (Double) complianceMetrics.getOrDefault("completionRate", 0.0);
            friaApprovalRate = (Double) complianceMetrics.getOrDefault("friaApprovalRate", 0.0);
            euSubmissionRate = (Double) complianceMetrics.getOrDefault("euSubmissionRate", 0.0);

            log.debug("Dashboard loaded - Assessments: {}/{}, FRIA: {}/{}, EU Reg: {}/{}",
                completedAssessments, totalAssessments, approvedFria, totalFria,
                submittedEuRegistrations, totalEuRegistrations);

        } catch (BussinessException e) {
            log.error("Error loading compliance dashboard", e);
            org.zkoss.zk.ui.util.Clients.showNotification(
                "Error cargando dashboard: " + e.getMessage(), "error", null, null, 5000);
        }
    }

    @Command
    @NotifyChange("*")
    public void refresh() {
        loadDashboard();
    }

    // Getters para cálculos derivados
    public Long getPendingAssessments() {
        return totalAssessments - completedAssessments;
    }

    public Long getPendingFria() {
        return totalFria - approvedFria;
    }

    public Long getPendingEuRegistrations() {
        return totalEuRegistrations - submittedEuRegistrations;
    }

    public String getComplianceStatus() {
        if (completionRate >= 90 && friaApprovalRate >= 80 && euSubmissionRate >= 80) {
            return "EXCELLENT";
        } else if (completionRate >= 70 && friaApprovalRate >= 60 && euSubmissionRate >= 60) {
            return "GOOD";
        } else if (completionRate >= 50) {
            return "WARNING";
        } else {
            return "CRITICAL";
        }
    }
}
