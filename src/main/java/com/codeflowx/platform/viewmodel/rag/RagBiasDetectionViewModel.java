package com.codeflowx.platform.viewmodel.rag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.rag.RagDataSource;
import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.service.rag.RagSystemService;
import com.codeflowx.govern.service.rag.RagDataSourceService;
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
public class RagBiasDetectionViewModel extends BaseFront<RagBiasDetectionViewModel> {
    private static final long serialVersionUID = 1L;

    @org.zkoss.zk.ui.select.annotation.WireVariable
    private RagSystemService ragSystemService;

    @org.zkoss.zk.ui.select.annotation.WireVariable
    private RagDataSourceService ragDataSourceService;

    @Override
    public void setBeans(Object bean) {}

    // Paginación
    private PageParams pageParams;
    private PageResult<RagDataSource> pageResult;

    // Filtros
    private String searchText = "";
    private String filterRagSystem = "";
    private String filterRiskLevel = "";
    private String filterCompliance = "";

    // Datos
    private List<RagDataSource> sourcesList = new ArrayList<>();
    private List<RagSystem> ragSystemsList = new ArrayList<>();

    // Métricas
    private int totalSources = 0;
    private double averageBiasScore = 0.0;
    private int highRiskSources = 0;
    private double complianceRate = 0.0;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadRagSystems();
        loadSources();
        calculateMetrics();
    }

    @Command
    @NotifyChange("*")
    public void loadRagSystems() {
        try {
            PageParams params = PageParams.builder().maxRows(100).pageActual(1).build();
            PageResult<RagSystem> result = ragSystemService.findAll(params);

            if (result != null && result.getContent() != null) {
                ragSystemsList = result.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar sistemas RAG", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadSources() {
        try {
            Criterias criterias = buildCriterias();

            pageResult = ragDataSourceService.findAll(
                pageParams,
                criterias
            );

            if (pageResult != null && pageResult.getContent() != null) {
                sourcesList = pageResult.getContent();
                totalSources = pageResult.getTotalRows();

                // Auditar búsqueda
                logActivity("BUSCAR", "RAGDATASOURCES", null,
                    "Análisis de sesgo: " + sourcesList.size() + " fuentes");

                log.info("Cargadas {} fuentes para análisis de sesgo", sourcesList.size());
            } else {
                sourcesList = new ArrayList<>();
                totalSources = 0;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar fuentes", e);
            Messagebox.show("Error al cargar fuentes: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void calculateMetrics() {
        try {
            if (sourcesList.isEmpty()) {
                averageBiasScore = 0.0;
                highRiskSources = 0;
                complianceRate = 0.0;
                return;
            }

            // Score promedio
            double totalScore = 0.0;
            int countWithScore = 0;
            highRiskSources = 0;
            int compliantSources = 0;

            for (RagDataSource source : sourcesList) {
                java.math.BigDecimal biasScore = source.getRagdsbiasscore();
                if (biasScore != null) {
                    double score = biasScore.doubleValue();
                    totalScore += score;
                    countWithScore++;

                    if (score >= 70) {
                        highRiskSources++;
                    }

                    if (score < 70) {
                        compliantSources++;
                    }
                }
            }

            averageBiasScore = countWithScore > 0 ? Math.round(totalScore / countWithScore * 10) / 10.0 : 0.0;
            complianceRate = totalSources > 0 ? Math.round((double) compliantSources / totalSources * 1000) / 10.0 : 0.0;

        } catch (Exception e) {
            log.error("Error al calcular métricas", e);
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (filterRagSystem != null && !filterRagSystem.trim().isEmpty()) {
            try {
                Long systemId = Long.parseLong(filterRagSystem);
                criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragSystem.idxragsystem", systemId));
            } catch (NumberFormatException e) {
                log.warn("Invalid RAG system ID filter: {}", filterRagSystem);
            }
        }

        if (filterRiskLevel != null && !filterRiskLevel.trim().isEmpty()) {
            switch (filterRiskLevel) {
                case "HIGH":
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.GREATER_EQUALS, "ragdsbiasscore", 70));
                    break;
                case "MEDIUM":
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.GREATER_EQUALS, "ragdsbiasscore", 30));
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LESS_THAN_EQUALS, "ragdsbiasscore", 70));
                    break;
                case "LOW":
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LESS_THAN_EQUALS, "ragdsbiasscore", 30));
                    break;
            }
        }

        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "ragdssourcename", searchText));
        }

        return criterias;
    }

    @Command
    @NotifyChange("*")
    public void searchSources() {
        pageParams.setPageActual(1);
        loadSources();
        calculateMetrics();
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadSources();
        calculateMetrics();
    }

    @Command
    @NotifyChange("*")
    public void refreshAnalysis() {
        loadSources();
        calculateMetrics();
    }

    @Command
    @NotifyChange("*")
    public void runBiasAnalysis() {
        Messagebox.show(
            "Se iniciará el análisis de sesgo para todas las fuentes.\n\n" +
            "NOTA: Esta funcionalidad requiere integración con leka-server para:\n" +
            "- Análisis semántico de contenido\n" +
            "- Detección de sesgos lingüísticos\n" +
            "- Evaluación de diversidad de fuentes\n" +
            "- Scoring automático",
            "Análisis en Desarrollo",
            Messagebox.OK,
            Messagebox.INFORMATION);
    }

    @Command
    public void viewDetails(@BindingParam("source") RagDataSource source) {
        log.info("Ver detalles de sesgo: {}", source.getRagdssourcename());
        // TODO: Navegar a vista detallada
    }

    @Command
    @NotifyChange("*")
    public void analyzeSource(@BindingParam("source") RagDataSource source) {
        try {
            // Simular re-análisis
            Messagebox.show(
                "Re-analizando fuente: " + source.getRagdssourcename() + "\n\n" +
                "NOTA: Esta funcionalidad requiere integración con leka-server.",
                "Análisis en Desarrollo",
                Messagebox.OK,
                Messagebox.INFORMATION);

            // Auditar
            logActivity("ANALIZAR", "RAGDATASOURCES", source.getIdxragdatasource(),
                "Re-análisis de sesgo solicitado: " + source.getRagdssourcename());

        } catch (Exception e) {
            log.error("Error al analizar fuente", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void reportToGovernance(@BindingParam("source") RagDataSource source) {
        java.math.BigDecimal biasScore = source.getRagdsbiasscore();
        if (biasScore == null || biasScore.doubleValue() < 70) {
            Messagebox.show("Solo se pueden reportar fuentes de alto riesgo (score >= 70)",
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        Messagebox.show(
            "¿Desea reportar esta fuente de alto riesgo al equipo de Gobierno?\n\n" +
            "Fuente: " + source.getRagdssourcename() + "\n" +
            "Score de Sesgo: " + biasScore + "\n" +
            "Sistema: " + getRagSystemName(source.getRagSystem()),
            "Confirmar Reporte a Gobierno",
            Messagebox.OK | Messagebox.CANCEL,
            Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        // INTEGRACIÓN CON GOBIERNO: Registrar alerta de alto riesgo
                        String alertDescription = String.format(
                            "ALTO RIESGO - Sesgo detectado: Fuente '%s' (ID: %d) con score %d. " +
            "Sistema RAG: %s. Documentos afectados: %d. Requiere revisión inmediata.",
                            source.getRagdssourcename(),
                            source.getIdxragdatasource(),
            biasScore.intValue(),
                            getRagSystemName(source.getRagSystem()),
                            source.getRagdsdocumentcount()
                        );

                        logActivity("ALERTAR", "GOVERNANCE", source.getIdxragdatasource(), alertDescription);

                        // También registrar en la fuente
                        logActivity("REPORTAR", "RAGDATASOURCES", source.getIdxragdatasource(),
                            "Fuente reportada a Gobierno por alto riesgo de sesgo");

                        Messagebox.show(
                            "Reporte enviado exitosamente al equipo de Gobierno y Cumplimiento.\n\n" +
                            "El equipo será notificado y procederá con la revisión.",
                            "Éxito",
                            Messagebox.OK,
                            Messagebox.INFORMATION);

                    } catch (Exception e) {
                        log.error("Error al reportar a gobierno", e);
                        Messagebox.show("Error al enviar reporte: " + e.getMessage(),
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    @Command
    public void exportHighRiskReport() {
        try {
            // Auditar exportación
            logActivity("EXPORTAR", "RAGDATASOURCES", null,
                "Exportación de reporte de alto riesgo: " + highRiskSources + " fuentes");

            Messagebox.show(
                "Exportando reporte de " + highRiskSources + " fuentes de alto riesgo.\n\n" +
                "NOTA: Funcionalidad de exportación en desarrollo.",
                "Exportación",
                Messagebox.OK,
                Messagebox.INFORMATION);

        } catch (Exception e) {
            log.error("Error al exportar reporte", e);
        }
    }

    // Helpers

    public String getRagSystemName(RagSystem system) {
        return system != null ? system.getRagsystemname() : "-";
    }

    public String getRiskLevel(Integer biasScore) {
        if (biasScore == null) return "N/A";
        if (biasScore >= 70) return "HIGH";
        if (biasScore >= 30) return "MEDIUM";
        return "LOW";
    }

    public String getComplianceStatus(Integer biasScore) {
        if (biasScore == null) return "N/A";
        if (biasScore >= 70) return "NON_COMPLIANT";
        if (biasScore >= 30) return "UNDER_REVIEW";
        return "COMPLIANT";
    }

    public String getBiasScoreColor(Integer score) {
        if (score == null) return "text-secondary";
        if (score >= 70) return "text-danger";
        if (score >= 30) return "text-warning";
        return "text-success";
    }

    public String getBiasProgressColor(Integer score) {
        if (score == null) return "bg-secondary";
        if (score >= 70) return "bg-danger";
        if (score >= 30) return "bg-warning";
        return "bg-success";
    }

    public String getRiskLevelColor(String level) {
        if (level == null) return "badge bg-secondary";
        switch (level) {
            case "HIGH": return "badge bg-danger";
            case "MEDIUM": return "badge bg-warning";
            case "LOW": return "badge bg-success";
            default: return "badge bg-secondary";
        }
    }

    public String getComplianceColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "COMPLIANT": return "badge bg-success";
            case "NON_COMPLIANT": return "badge bg-danger";
            case "UNDER_REVIEW": return "badge bg-warning";
            default: return "badge bg-secondary";
        }
    }

    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }

    @Destroy
    public void destroy() {
        if (sourcesList != null) {
            sourcesList.clear();
            sourcesList = null;
        }
        if (ragSystemsList != null) {
            ragSystemsList.clear();
            ragSystemsList = null;
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
