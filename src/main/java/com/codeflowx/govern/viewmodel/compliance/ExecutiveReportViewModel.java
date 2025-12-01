package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.business.compliance.ComplianceExecutiveReportService;
import com.codeflowx.govern.business.compliance.ReportExportService;
import com.codeflowx.govern.business.exception.BussinessException;
import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Map;

/**
 * ViewModel: Reporte Ejecutivo Consolidado
 *
 * Funcionalidad:
 * - Generar reporte ejecutivo consolidado de compliance
 * - Visualizar métricas y resumen ejecutivo
 * - Exportar a PDF
 *
 * EU AI Act - Múltiples artículos
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ExecutiveReportViewModel extends BaseFront<ExecutiveReportViewModel> {

    private static final long serialVersionUID = 1L;

    // ========== Servicios ==========
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private ComplianceExecutiveReportService complianceExecutiveReportService;

    @WireVariable
    private ReportExportService reportExportService;

    // ========== Datos del Reporte ==========
    @Getter @Setter
    private String reportType = "EXECUTIVE"; // FULL, EXECUTIVE, TECHNICAL, AUDIT

    @Getter @Setter
    private Timestamp startDate;

    @Getter @Setter
    private Timestamp endDate;

    @Getter
    private Map<String, Object> reportData;

    @Getter
    private boolean reportGenerated = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        // Inicializar fechas por defecto (últimos 30 días)
        endDate = new Timestamp(System.currentTimeMillis());
        startDate = new Timestamp(System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000));
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange({"reportData", "reportGenerated"})
    public void generateReport() {
        try {
            log.info("Generating executive report, type: {}, period: {} to {}",
                    reportType, startDate, endDate);

            reportData = complianceExecutiveReportService.generateExecutiveReport(
                reportType, startDate, endDate
            );

            reportGenerated = true;
            log.info("Executive report generated successfully");

            Messagebox.show("Reporte ejecutivo generado correctamente", "Éxito",
                          Messagebox.OK, Messagebox.INFORMATION);

        } catch (BussinessException e) {
            log.error("Error generating executive report", e);
            Messagebox.show("Error al generar reporte ejecutivo: " + e.getMessage(),
                          "Error", Messagebox.OK, Messagebox.ERROR);
            reportGenerated = false;
        }
    }

    @Command
    public void exportToPdf() {
        if (!reportGenerated || reportData == null) {
            Messagebox.show("Genere el reporte primero antes de exportar", "Advertencia",
                          Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            log.info("Exporting executive report to PDF");
            byte[] pdfBytes = reportExportService.exportReport(
                reportData, "PDF", "executive-report-" + reportType.toLowerCase()
            );

            String filename = String.format("executive-report-%s-%d.pdf",
                reportType.toLowerCase(), System.currentTimeMillis());

            Filedownload.save(pdfBytes, "application/pdf", filename);
            Messagebox.show("Reporte exportado correctamente", "Éxito",
                          Messagebox.OK, Messagebox.INFORMATION);

        } catch (BussinessException e) {
            log.error("Error exporting report to PDF", e);
            Messagebox.show("Error al exportar reporte: " + e.getMessage(),
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"reportType"})
    public void setReportType(@BindingParam("type") String type) {
        this.reportType = type;
    }

    // ========== Getters para UI ==========

    public Map<String, Object> getMetrics() {
        if (reportData == null) {
            return null;
        }
        return (Map<String, Object>) reportData.get("metrics");
    }

    public Map<String, Object> getExecutiveSummary() {
        if (reportData == null) {
            return null;
        }
        return (Map<String, Object>) reportData.get("executiveSummary");
    }

    public java.util.List<String> getConclusions() {
        if (reportData == null) {
            return null;
        }
        return (java.util.List<String>) reportData.get("conclusions");
    }

    public java.util.List<String> getRecommendations() {
        if (reportData == null) {
            return null;
        }
        return (java.util.List<String>) reportData.get("recommendations");
    }
}
