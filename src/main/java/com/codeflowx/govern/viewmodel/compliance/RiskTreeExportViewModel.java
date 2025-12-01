package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.business.compliance.RiskTreeExportService;
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

/**
 * ViewModel: Exportación Árbol de Riesgos
 *
 * Funcionalidad:
 * - Construir árbol de riesgos desde FRIA
 * - Exportar a PDF, PNG, JSON
 * - Visualización del árbol
 *
 * EU AI Act Art. 27 - FRIA
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class RiskTreeExportViewModel extends BaseFront<RiskTreeExportViewModel> {

    private static final long serialVersionUID = 1L;

    // ========== Servicios ==========
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private RiskTreeExportService riskTreeExportService;

    // ========== Datos ==========
    @Getter @Setter
    private Long friaId;

    @Getter
    private RiskTreeExportService.RiskTree riskTree;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        // Obtener friaId desde parámetros
        String idParam = Executions.getCurrent().getParameter("friaId");
        if (idParam != null) {
            try {
                friaId = Long.parseLong(idParam);
                loadRiskTree();
            } catch (NumberFormatException e) {
                log.error("Invalid friaId parameter: {}", idParam);
                Messagebox.show("ID de FRIA inválido", "Error",
                              Messagebox.OK, Messagebox.ERROR);
            }
        }
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange("riskTree")
    public void loadRiskTree() {
        if (friaId == null) {
            Messagebox.show("Seleccione una evaluación FRIA", "Advertencia",
                          Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            log.info("Loading risk tree for FRIA: {}", friaId);
            riskTree = riskTreeExportService.buildRiskTree(friaId);
        } catch (BussinessException e) {
            log.error("Error loading risk tree", e);
            Messagebox.show("Error al cargar árbol de riesgos: " + e.getMessage(),
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void exportRiskTree(@BindingParam("format") String format) {
        if (friaId == null) {
            Messagebox.show("Seleccione una evaluación FRIA", "Advertencia",
                          Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            log.info("Exporting risk tree in format: {}", format);
            byte[] fileContent = riskTreeExportService.exportRiskTree(friaId, format);

            String contentType;
            String extension;
            String filename;

            switch (format.toUpperCase()) {
                case "PDF":
                    contentType = "application/pdf";
                    extension = "pdf";
                    filename = "risk-tree-" + friaId + ".pdf";
                    break;
                case "PNG":
                    contentType = "image/png";
                    extension = "png";
                    filename = "risk-tree-" + friaId + ".png";
                    break;
                case "JSON":
                    contentType = "application/json";
                    extension = "json";
                    filename = "risk-tree-" + friaId + ".json";
                    break;
                default:
                    Messagebox.show("Formato no soportado: " + format, "Error",
                                  Messagebox.OK, Messagebox.ERROR);
                    return;
            }

            Filedownload.save(fileContent, contentType, filename);
            Messagebox.show("Árbol de riesgos exportado correctamente", "Éxito",
                          Messagebox.OK, Messagebox.INFORMATION);

        } catch (BussinessException e) {
            log.error("Error exporting risk tree", e);
            Messagebox.show("Error al exportar árbol de riesgos: " + e.getMessage(),
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}
