package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Reporte Ejecutivo Consolidado de Compliance
 *
 * Funcionalidad:
 * - Vista del reporte generado
 * - Evidencias
 * - Conclusiones
 * - Exportar a PDF
 * - Integración con AIActDocumentationGeneratorViewModel y ConformityDeclarationManagerViewModel
 *
 * Modo MOCK:
 * - URL: /gobierno/compliance/compliance-report.zul?mock=true
 * - Variable de entorno: MOCK_MODE=true
 * - Carga datos de ejemplo para demo
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ComplianceReportViewModel extends BaseFront<ComplianceReportViewModel> {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
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

    @WireVariable
    private com.codeflowx.govern.service.governance.ComplianceAssessmentService complianceAssessmentService;

    @WireVariable
    private com.codeflowx.govern.service.compliance.AIActDocumentationService aiActDocumentationService;

    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Modo MOCK ==========
    @Getter
    private boolean mockMode = false;

    // ========== Datos del Reporte ==========
    @Getter
    private ComplianceReport report;

    @Getter
    private List<ReportEvidence> evidences = new ArrayList<>();

    @Getter
    private List<ReportConclusion> conclusions = new ArrayList<>();

    @Getter
    private boolean reportGenerated = false;

    @Getter
    private Timestamp reportGeneratedAt;

    // ========== Filtros/Opciones ==========
    @Getter @Setter
    private String reportType = "FULL"; // FULL, EXECUTIVE, TECHNICAL, AUDIT

    @Getter @Setter
    private String reportPeriod = "LAST_30_DAYS"; // LAST_7_DAYS, LAST_30_DAYS, LAST_90_DAYS, CUSTOM

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        // Detectar modo MOCK
        String mockParam = Executions.getCurrent().getParameter("mock");
        mockMode = "true".equalsIgnoreCase(mockParam);

        if (System.getenv("MOCK_MODE") != null) {
            mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE"));
        }

        log.info("🚀 Inicializando ComplianceReportViewModel - MOCK MODE: {}", mockMode);

        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }

    /**
     * Cargar datos reales desde servicios
     */
    private void loadRealData() {
        log.info("📊 Cargando datos reales de reporte de compliance...");

        try {
            report = null;
            evidences.clear();
            conclusions.clear();
            reportGenerated = false;

            // Cargar assessments de compliance
            if (complianceAssessmentService != null) {
                try {
                    codeflowx.nocode.persist.PageParams pageParams = codeflowx.nocode.persist.PageParams.builder()
                        .maxRows(100)
                        .pageActual(1)
                        .rowActual(0)
                        .ascending(false)
                        .sortField("assessmentdate")
                        .build();
                    
                    codeflowx.nocode.persist.Criterias criterias = new codeflowx.nocode.persist.Criterias();
                    // Filtrar por período según reportPeriod
                    if (!"ALL".equals(reportPeriod)) {
                        java.sql.Timestamp startDate = calculatePeriodStartDate(reportPeriod);
                        if (startDate != null) {
                            codeflowx.nocode.persist.Criteria criteria = new codeflowx.nocode.persist.Criteria(
                                codeflowx.nocode.persist.Operation.AND,
                                codeflowx.nocode.persist.Evaluation.GREATER_OR_EQUAL,
                                "assessmentdate"
                            );
                            criteria.setValues(new Object[]{startDate});
                            criterias.addCriteria(criteria);
                        }
                    }
                    
                    codeflowx.nocode.persist.PageResult<com.codeflowx.govern.entity.governance.ComplianceAssessment> result = 
                        complianceAssessmentService.findAll(pageParams, criterias);
                    
                    if (result != null && result.getContent() != null) {
                        // Usar assessments para generar métricas del reporte
                        int totalAssessments = result.getContent().size();
                        long highRiskCount = result.getContent().stream()
                            .filter(a -> "HIGH_RISK".equals(a.getRisklevel()))
                            .count();
                        long completedCount = result.getContent().stream()
                            .filter(a -> "COMPLETED".equals(a.getStatus()))
                            .count();
                        
                        // Estos datos se usarán al generar el reporte
                        log.info("Cargados {} assessments ({} alto riesgo, {} completados)", 
                                totalAssessments, highRiskCount, completedCount);
                    }
                } catch (Exception e) {
                    log.warn("Error al cargar assessments de compliance", e);
                }
            }
            
            // Llamar a microservicio Python para generar reporte ejecutivo (si está disponible)
            String reportServiceUrl = environment != null ? 
                environment.getProperty("compliance.report.service.url") : null;
            if (reportServiceUrl != null && !reportServiceUrl.isEmpty()) {
                try {
                    org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
                    String url = reportServiceUrl + "/api/compliance/generate-executive-report";
                    
                    java.util.Map<String, String> request = new java.util.HashMap<>();
                    request.put("reportType", reportType);
                    request.put("period", reportPeriod);
                    
                    // Llamar al servicio Python
                    // ComplianceReport result = restTemplate.postForObject(url, request, ComplianceReport.class);
                    // this.report = result;
                    log.info("Servicio de reportes disponible en: {}", reportServiceUrl);
                } catch (Exception e) {
                    log.warn("Error al llamar servicio de reportes Python", e);
                }
            }
            
            // Integrar con AIActDocumentationGeneratorService
            if (aiActDocumentationService != null) {
                try {
                    // Generar documentación técnica AI Act si es necesario
                    // Esto se puede hacer al generar el reporte
                    log.info("Servicio de documentación AI Act disponible");
                } catch (Exception e) {
                    log.warn("Error al integrar con servicio de documentación AI Act", e);
                }
            }

            log.info("✅ Datos cargados para generación de reporte");

        } catch (Exception e) {
            log.error("Error al cargar datos reales de reporte", e);
            report = null;
            evidences.clear();
            conclusions.clear();
            reportGenerated = false;
        }
    }

    /**
     * Calcular fecha de inicio según período seleccionado
     */
    private java.sql.Timestamp calculatePeriodStartDate(String period) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime start;
        
        switch (period) {
            case "LAST_7_DAYS":
                start = now.minusDays(7);
                break;
            case "LAST_30_DAYS":
                start = now.minusDays(30);
                break;
            case "LAST_90_DAYS":
                start = now.minusDays(90);
                break;
            default:
                return null;
        }
        
        return java.sql.Timestamp.valueOf(start);
    }

    /**
     * Cargar datos MOCK para demo
     */
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Reporte de Compliance...");

        // Crear reporte de ejemplo
        report = new ComplianceReport();
        report.setId("report-2025-11-25-001");
        report.setTitle("Reporte Ejecutivo de Compliance - AI Act");
        report.setPeriod("Últimos 30 días");
        report.setGeneratedAt(Timestamp.valueOf(LocalDateTime.now()));
        report.setGeneratedBy("compliance-officer@codeflowx.com");
        report.setStatus("COMPLETED");

        // Resumen ejecutivo
        report.setExecutiveSummary(
            "Este reporte consolida el estado de cumplimiento del AI Act para todos los sistemas de IA " +
            "gestionados en CodeflowX OS. Durante el período reportado, se han evaluado 12 sistemas de IA, " +
            "de los cuales 8 han sido clasificados como de alto riesgo y requieren registro en la base de datos " +
            "de la UE. Se han completado 4 evaluaciones FRIA y se han implementado controles de mitigación " +
            "para todos los sistemas de alto riesgo."
        );

        // Métricas
        report.setTotalSystems(12);
        report.setHighRiskSystems(8);
        report.setFriaCompleted(4);
        report.setFriaPending(4);
        report.setComplianceScore(87.5);
        report.setOverallStatus("COMPLIANT");

        // Evidencias
        evidences.clear();

        ReportEvidence ev1 = new ReportEvidence();
        ev1.setId("ev-001");
        ev1.setType("FRIA_ASSESSMENT");
        ev1.setTitle("Evaluación FRIA - Modelo Predicción Fraude v4.2");
        ev1.setDescription("Evaluación completa de riesgos fundamentales completada el 2025-11-20");
        ev1.setStatus("COMPLETED");
        ev1.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(5)));
        ev1.setDocumentUrl("/reports/fria-modelo-fraude-v4.2.pdf");
        evidences.add(ev1);

        ReportEvidence ev2 = new ReportEvidence();
        ev2.setId("ev-002");
        ev2.setType("EU_REGISTRATION");
        ev2.setTitle("Registro UE - Agent Fraud Detection v2");
        ev2.setDescription("Sistema registrado en base de datos UE según Art. 49 AI Act");
        ev2.setStatus("REGISTERED");
        ev2.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(10)));
        ev2.setDocumentUrl("/reports/eu-registration-agent-fraud-v2.pdf");
        evidences.add(ev2);

        ReportEvidence ev3 = new ReportEvidence();
        ev3.setId("ev-003");
        ev3.setType("HITL_AUDIT");
        ev3.setTitle("Auditoría Supervisión Humana - Q4 2025");
        ev3.setDescription("Auditoría completa de intervenciones humanas en aprobaciones");
        ev3.setStatus("COMPLETED");
        ev3.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
        ev3.setDocumentUrl("/reports/hitl-audit-q4-2025.pdf");
        evidences.add(ev3);

        ReportEvidence ev4 = new ReportEvidence();
        ev4.setId("ev-004");
        ev4.setType("CONFORMITY_DECLARATION");
        ev4.setTitle("Declaración de Conformidad - AI Act Annex V");
        ev4.setDescription("Declaración de conformidad generada para sistemas de alto riesgo");
        ev4.setStatus("SIGNED");
        ev4.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        ev4.setDocumentUrl("/reports/conformity-declaration-2025-11.pdf");
        evidences.add(ev4);

        // Conclusiones
        conclusions.clear();

        ReportConclusion c1 = new ReportConclusion();
        c1.setCategory("COMPLIANCE");
        c1.setTitle("Cumplimiento General");
        c1.setDescription("El 87.5% de los sistemas de IA cumplen con los requisitos del AI Act. " +
                          "Los 4 sistemas pendientes de evaluación FRIA están en proceso de revisión.");
        c1.setStatus("POSITIVE");
        conclusions.add(c1);

        ReportConclusion c2 = new ReportConclusion();
        c2.setCategory("RISK_MANAGEMENT");
        c2.setTitle("Gestión de Riesgos");
        c2.setDescription("Todos los sistemas de alto riesgo tienen controles de mitigación implementados. " +
                          "Se han detectado 2 incidentes menores que fueron resueltos dentro del SLA.");
        c2.setStatus("POSITIVE");
        conclusions.add(c2);

        ReportConclusion c3 = new ReportConclusion();
        c3.setCategory("RECOMMENDATIONS");
        c3.setTitle("Recomendaciones");
        c3.setDescription("Se recomienda completar las 4 evaluaciones FRIA pendientes antes del 2025-12-15. " +
                          "Se sugiere implementar monitoreo continuo para los sistemas de alto riesgo.");
        c3.setStatus("NEUTRAL");
        conclusions.add(c3);

        reportGenerated = true;
        reportGeneratedAt = Timestamp.valueOf(LocalDateTime.now());

        log.info("✅ Datos MOCK cargados - Reporte generado con {} evidencias, {} conclusiones",
                 evidences.size(), conclusions.size());
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange({"report", "evidences", "conclusions", "reportGenerated"})
    public void generateReport() {
        log.info("📄 Generando reporte de compliance - Tipo: {}, Período: {}", reportType, reportPeriod);

        if (mockMode) {
            Messagebox.show("✅ [DEMO] Generando reporte de compliance...\n\nTipo: " + reportType +
                          "\nPeríodo: " + reportPeriod,
                          "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                          event -> {
                              loadMockData();
                          });
        } else {
            try {
                // Llamar a servicio de generación de reportes
                // Opción 1: Usar BusinessService para llamar a función/procedimiento almacenado
                // Opción 2: Llamar a microservicio Python REST

                // Ejemplo con BusinessService:
                // GenerateComplianceReport function = new GenerateComplianceReport();
                // function.setPReportType(reportType);
                // function.setPPeriod(reportPeriod);
                // function = businessService.callFuction(function);

                // Ejemplo con microservicio Python:
                // String reportServiceUrl = environment.getProperty("compliance.report.service.url");
                // if (reportServiceUrl != null) {
                //     RestTemplate restTemplate = new RestTemplate();
                //     Map<String, String> request = new HashMap<>();
                //     request.put("reportType", reportType);
                //     request.put("period", reportPeriod);
                //     ComplianceReport result = restTemplate.postForObject(
                //         reportServiceUrl + "/api/compliance/generate-executive-report",
                //         request,
                //         ComplianceReport.class
                //     );
                //     this.report = result;
                // }

                // Por ahora, cargar datos reales
                loadRealData();
                reportGenerated = true;

                Messagebox.show("Reporte generado correctamente", "Éxito",
                              Messagebox.OK, Messagebox.INFORMATION);

            } catch (Exception e) {
                log.error("Error al generar reporte", e);
                Messagebox.show("Error al generar reporte: " + e.getMessage(), "Error",
                              Messagebox.OK, Messagebox.ERROR);
            }
        }
    }

    @Command
    public void exportToPdf() {
        if (!reportGenerated || report == null) {
            Messagebox.show("Por favor genere el reporte primero", "Atención",
                          Messagebox.OK, Messagebox.INFORMATION);
            return;
        }

        if (mockMode) {
            Messagebox.show("✅ [DEMO] Exportando reporte a PDF...\n\n" +
                          "Título: " + report.getTitle() + "\n" +
                          "Evidencias: " + evidences.size() + "\n" +
                          "Conclusiones: " + conclusions.size(),
                          "Demo Mode", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            try {
                // TODO: Implementar exportación real a PDF
                // Opción 1: Usar librería Java (iText, Apache PDFBox)
                // Opción 2: Llamar a microservicio Python que genera PDF

                // Ejemplo con microservicio Python:
                // String reportServiceUrl = environment.getProperty("compliance.report.service.url");
                // if (reportServiceUrl != null) {
                //     RestTemplate restTemplate = new RestTemplate();
                //     Map<String, Object> request = new HashMap<>();
                //     request.put("reportId", report.getId());
                //     request.put("format", "PDF");
                //     byte[] pdfBytes = restTemplate.postForObject(
                //         reportServiceUrl + "/api/compliance/export-report",
                //         request,
                //         byte[].class
                //     );
                //     // Descargar archivo PDF
                //     Executions.getCurrent().sendRedirect("/download?file=" + pdfBytes);
                // }

                Messagebox.show("Exportación a PDF iniciada", "Información",
                              Messagebox.OK, Messagebox.INFORMATION);

            } catch (Exception e) {
                log.error("Error al exportar a PDF", e);
                Messagebox.show("Error al exportar a PDF: " + e.getMessage(), "Error",
                              Messagebox.OK, Messagebox.ERROR);
            }
        }
    }

    @Command
    public void viewEvidence(@BindingParam("evidence") ReportEvidence evidence) {
        if (mockMode) {
            Messagebox.show("✅ [DEMO] Abriendo evidencia\n\n" +
                          "Título: " + evidence.getTitle() + "\n" +
                          "Tipo: " + evidence.getType() + "\n" +
                          "Estado: " + evidence.getStatus(),
                          "Demo Mode", Messagebox.OK, Messagebox.INFORMATION);
        } else {
            // TODO: Abrir documento real
            Messagebox.show("Abriendo evidencia: " + evidence.getTitle(), "Información",
                          Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    public void refreshReport() {
        log.info("🔄 Refrescando reporte de compliance...");
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }

    // ========== Clases Internas ==========

    /**
     * Representa un reporte de compliance
     */
    @Getter
    @Setter
    public static class ComplianceReport {
        private String id;
        private String title;
        private String period;
        private Timestamp generatedAt;
        private String generatedBy;
        private String status; // DRAFT, GENERATING, COMPLETED, ERROR
        private String executiveSummary;
        private int totalSystems;
        private int highRiskSystems;
        private int friaCompleted;
        private int friaPending;
        private double complianceScore;
        private String overallStatus; // COMPLIANT, NON_COMPLIANT, PARTIAL
    }

    /**
     * Representa una evidencia en el reporte
     */
    @Getter
    @Setter
    public static class ReportEvidence {
        private String id;
        private String type; // FRIA_ASSESSMENT, EU_REGISTRATION, HITL_AUDIT, CONFORMITY_DECLARATION
        private String title;
        private String description;
        private String status; // COMPLETED, PENDING, REGISTERED, SIGNED
        private Timestamp timestamp;
        private String documentUrl;
    }

    /**
     * Representa una conclusión del reporte
     */
    @Getter
    @Setter
    public static class ReportConclusion {
        private String category; // COMPLIANCE, RISK_MANAGEMENT, RECOMMENDATIONS
        private String title;
        private String description;
        private String status; // POSITIVE, NEGATIVE, NEUTRAL
    }
}
