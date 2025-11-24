package com.codeflowx.platform.viewmodel.rag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.service.rag.RagSystemService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
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
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class RagComplianceViewModel extends BaseFront<RagComplianceViewModel> {
    private static final long serialVersionUID = 1L;

    @org.zkoss.zk.ui.select.annotation.WireVariable
    private RagSystemService ragSystemService;

    @Override
    public void setBeans(Object bean) {}

    // Inner class para políticas de gobierno
    @Getter
    @Setter
    public static class GovernancePolicy {
        private String policyName;
        private String description;
        private String category;
        private String status;
    }

    // Paginación
    private PageParams pageParams;
    private PageResult<RagSystem> pageResult;

    // Filtros
    private String searchText = "";
    private String filterCompliance = "";
    private String filterRiskLevel = "";
    private String filterCategory = "";

    // Datos
    private List<RagSystem> systemsList = new ArrayList<>();
    private List<GovernancePolicy> governancePolicies = new ArrayList<>();

    // Métricas
    private int totalSystems = 0;
    private int compliantSystems = 0;
    private int activeAlerts = 0;
    private double globalComplianceScore = 0.0;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadSystems();
        calculateMetrics();
        loadGovernancePolicies();
    }

    @Command
    @NotifyChange("*")
    public void loadSystems() {
        try {
            Criterias criterias = buildCriterias();

            pageResult = ragSystemService.findAll(
                pageParams,
                criterias
            );

            if (pageResult != null && pageResult.getContent() != null) {
                systemsList = pageResult.getContent();
                totalSystems = pageResult.getTotalRows();

                // Auditar búsqueda
                logActivity("BUSCAR", "RAGSYSTEMS", null,
                    "Monitoreo de cumplimiento: " + systemsList.size() + " sistemas");

                log.info("Cargados {} sistemas para cumplimiento", systemsList.size());
            } else {
                systemsList = new ArrayList<>();
                totalSystems = 0;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar sistemas", e);
            Messagebox.show("Error al cargar sistemas: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void calculateMetrics() {
        try {
            if (systemsList.isEmpty()) {
                compliantSystems = 0;
                activeAlerts = 0;
                globalComplianceScore = 0.0;
                return;
            }

            compliantSystems = 0;
            activeAlerts = 0;
            double totalScore = 0.0;

            for (RagSystem system : systemsList) {
                // Conformes
                if ("COMPLIANT".equals(system.getRagcompliance())) {
                    compliantSystems++;
                }

                // Alertas (sistemas no conformes o críticos)
                if ("NON_COMPLIANT".equals(system.getRagcompliance()) ||
                    "CRITICAL".equals(system.getRagrisklevel())) {
                    activeAlerts++;
                }

                // Score global
                java.math.BigDecimal score = system.getRagoverallscore();
                if (score != null) {
                    totalScore += score.doubleValue();
                }
            }

            globalComplianceScore = totalSystems > 0 ? Math.round(totalScore / totalSystems * 10) / 10.0 : 0.0;

        } catch (Exception e) {
            log.error("Error al calcular métricas", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadGovernancePolicies() {
        try {
            // Cargar políticas de gobierno desde la base de datos
            // Por ahora, políticas de ejemplo
            governancePolicies = new ArrayList<>();

            GovernancePolicy p1 = new GovernancePolicy();
            p1.setPolicyName("Protección de Datos Personales");
            p1.setDescription("Los sistemas RAG no deben procesar ni almacenar PII sin consentimiento explícito");
            p1.setCategory("PRIVACY");
            p1.setStatus("ACTIVE");
            governancePolicies.add(p1);

            GovernancePolicy p2 = new GovernancePolicy();
            p2.setPolicyName("Control de Sesgos");
            p2.setDescription("Todas las fuentes deben ser analizadas para detectar sesgos antes de su uso");
            p2.setCategory("ETHICS");
            p2.setStatus("ACTIVE");
            governancePolicies.add(p2);

            GovernancePolicy p3 = new GovernancePolicy();
            p3.setPolicyName("Trazabilidad de Información");
            p3.setDescription("Todas las respuestas deben poder rastrearse a sus fuentes originales");
            p3.setCategory("QUALITY");
            p3.setStatus("ACTIVE");
            governancePolicies.add(p3);

            GovernancePolicy p4 = new GovernancePolicy();
            p4.setPolicyName("Cifrado de Datos");
            p4.setDescription("Todos los datos sensibles deben estar cifrados en reposo y en tránsito");
            p4.setCategory("SECURITY");
            p4.setStatus("ACTIVE");
            governancePolicies.add(p4);

            // TODO: Integrar con entidades de gobierno existentes

        } catch (Exception e) {
            log.error("Error al cargar políticas", e);
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (filterCompliance != null && !filterCompliance.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragcompliance", filterCompliance));
        }

        if (filterRiskLevel != null && !filterRiskLevel.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragrisklevel", filterRiskLevel));
        }

        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "ragsystemname", searchText));
        }

        return criterias;
    }

    @Command
    @NotifyChange("*")
    public void searchSystems() {
        pageParams.setPageActual(1);
        loadSystems();
        calculateMetrics();
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadSystems();
        calculateMetrics();
    }

    @Command
    @NotifyChange("*")
    public void refreshCompliance() {
        loadSystems();
        calculateMetrics();
        loadGovernancePolicies();
    }

    @Command
    public void viewGovernanceDashboard() {
        // INTEGRACIÓN: Navegar a pantalla de gobierno existente
        Messagebox.show(
            "Navegando al Dashboard de Gobierno y Cumplimiento principal.\n\n" +
            "Esta integración permite ver todas las políticas, auditorías y reportes de gobierno.",
            "Navegación a Gobierno",
            Messagebox.OK,
            Messagebox.INFORMATION);

        // TODO: Navegar a /console/governance/dashboard
        log.info("Navegación a dashboard de gobierno solicitada");
    }

    @Command
    public void viewSystemCompliance(@BindingParam("system") RagSystem system) {
        log.info("Ver cumplimiento detallado: {}", system.getRagsystemname());

        // Auditar acceso
        logActivity("VER", "RAGSYSTEMS", system.getIdxragsystem(),
            "Vista de cumplimiento detallado: " + system.getRagsystemname());

        // TODO: Navegar a vista detallada
    }

    @Command
    @NotifyChange("*")
    public void runComplianceCheck(@BindingParam("system") RagSystem system) {
        try {
            Messagebox.show(
                "Iniciando verificación de cumplimiento para: " + system.getRagsystemname() + "\n\n" +
                "Este proceso verificará:\n" +
                "- Políticas de privacidad\n" +
                "- Control de sesgos\n" +
                "- Seguridad de datos\n" +
                "- Trazabilidad\n" +
                "- Calidad de respuestas\n\n" +
                "NOTA: Requiere integración con leka-server.",
                "Verificación de Cumplimiento",
                Messagebox.OK,
                Messagebox.INFORMATION);

            // Auditar verificación
            logActivity("VERIFICAR", "RAGSYSTEMS", system.getIdxragsystem(),
                "Verificación de cumplimiento iniciada: " + system.getRagsystemname());

            // INTEGRACIÓN CON GOBIERNO: Si se detectan problemas, alertar
            if ("NON_COMPLIANT".equals(system.getRagcompliance())) {
                logActivity("ALERTAR", "GOVERNANCE", system.getIdxragsystem(),
                    "Sistema NO CONFORME requiere revisión: " + system.getRagsystemname());
            }

        } catch (Exception e) {
            log.error("Error al verificar cumplimiento", e);
        }
    }

    @Command
    public void viewAuditHistory(@BindingParam("system") RagSystem system) {
        // INTEGRACIÓN: Ver historial de auditoría en pantallas de gobierno
        Messagebox.show(
            "Navegando al historial de auditoría para: " + system.getRagsystemname() + "\n\n" +
            "Esta vista muestra todas las auditorías registradas en el módulo de Gobierno.",
            "Historial de Auditoría",
            Messagebox.OK,
            Messagebox.INFORMATION);

        // Auditar acceso
        logActivity("VER", "RAGSYSTEMS", system.getIdxragsystem(),
            "Acceso a historial de auditoría: " + system.getRagsystemname());

        // TODO: Navegar a /console/governance/audit-history?systemId=X
        log.info("Ver historial de auditoría: {}", system.getIdxragsystem());
    }

    @Command
    public void exportComplianceReport() {
        try {
            // Auditar exportación
            logActivity("EXPORTAR", "RAGSYSTEMS", null,
                "Exportación de reporte de cumplimiento: " + totalSystems + " sistemas");

            Messagebox.show(
                "Exportando reporte de cumplimiento de " + totalSystems + " sistemas.\n\n" +
                "El reporte incluirá:\n" +
                "- Estado de cumplimiento\n" +
                "- Scores por sistema\n" +
                "- Alertas activas\n" +
                "- Políticas aplicadas\n\n" +
                "NOTA: Funcionalidad de exportación en desarrollo.",
                "Exportación",
                Messagebox.OK,
                Messagebox.INFORMATION);

        } catch (Exception e) {
            log.error("Error al exportar reporte", e);
        }
    }

    @Command
    public void viewPolicyDetails(@BindingParam("policy") GovernancePolicy policy) {
        Messagebox.show(
            "Política: " + policy.getPolicyName() + "\n\n" +
            "Descripción: " + policy.getDescription() + "\n" +
            "Categoría: " + policy.getCategory() + "\n" +
            "Estado: " + policy.getStatus(),
            "Detalles de Política",
            Messagebox.OK,
            Messagebox.INFORMATION);
    }

    @Command
    public void viewAllAlerts() {
        // INTEGRACIÓN: Navegar a pantalla de alertas de gobierno
        Messagebox.show(
            "Navegando al módulo de Alertas de Gobierno.\n\n" +
            "Total de alertas activas: " + activeAlerts,
            "Alertas de Gobierno",
            Messagebox.OK,
            Messagebox.INFORMATION);

        // TODO: Navegar a /console/governance/alerts
        log.info("Ver todas las alertas de gobierno");
    }

    // Helpers

    public boolean hasActiveAlerts(RagSystem system) {
        return "NON_COMPLIANT".equals(system.getRagcompliance()) ||
               "CRITICAL".equals(system.getRagrisklevel());
    }

    public int getAlertCount(RagSystem system) {
        // Simulación - en producción calcular desde alertas reales
        if (hasActiveAlerts(system)) {
            if ("CRITICAL".equals(system.getRagrisklevel())) return 3;
            return 1;
        }
        return 0;
    }

    public String translateStatus(String status) {
        if (status == null) return "N/A";
        switch (status) {
            case "COMPLIANT": return "Conforme";
            case "NON_COMPLIANT": return "No Conforme";
            case "UNDER_REVIEW": return "En Revisión";
            default: return status;
        }
    }

    public String getComplianceProgressColor(Integer score) {
        if (score == null) return "bg-secondary";
        if (score >= 80) return "bg-success";
        if (score >= 50) return "bg-warning";
        return "bg-danger";
    }

    public String getComplianceStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "COMPLIANT": return "badge bg-success";
            case "NON_COMPLIANT": return "badge bg-danger";
            case "UNDER_REVIEW": return "badge bg-warning";
            default: return "badge bg-secondary";
        }
    }

    public String getRiskLevelColor(String level) {
        if (level == null) return "badge bg-secondary";
        switch (level) {
            case "CRITICAL": return "badge bg-dark";
            case "HIGH": return "badge bg-danger";
            case "MEDIUM": return "badge bg-warning";
            case "LOW": return "badge bg-success";
            default: return "badge bg-secondary";
        }
    }

    public String getPolicyBorderColor(GovernancePolicy policy) {
        if ("ACTIVE".equals(policy.getStatus())) return "border-success";
        if ("INACTIVE".equals(policy.getStatus())) return "border-secondary";
        return "border-warning";
    }

    public String getPolicyStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "ACTIVE": return "badge bg-success";
            case "INACTIVE": return "badge bg-secondary";
            case "PENDING": return "badge bg-warning";
            default: return "badge bg-secondary";
        }
    }

    public String getPolicyIcon(String category) {
        if (category == null) return "fas fa-file-alt";
        switch (category) {
            case "PRIVACY": return "fas fa-user-shield";
            case "SECURITY": return "fas fa-lock";
            case "QUALITY": return "fas fa-star";
            case "ETHICS": return "fas fa-balance-scale";
            case "LEGAL": return "fas fa-gavel";
            default: return "fas fa-file-alt";
        }
    }

    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }

    @Destroy
    public void destroy() {
        if (systemsList != null) {
            systemsList.clear();
            systemsList = null;
        }
        if (governancePolicies != null) {
            governancePolicies.clear();
            governancePolicies = null;
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
