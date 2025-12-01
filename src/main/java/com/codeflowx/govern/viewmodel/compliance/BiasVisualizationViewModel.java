package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.business.compliance.BiasVisualizationService;
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
import org.zkoss.zul.Messagebox;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel: Visualizaciones Gráficas de Bias
 *
 * Funcionalidad:
 * - Visualización de histogramas de distribución por grupos protegidos
 * - Box plots de métricas de fairness
 * - Heatmaps de correlaciones entre atributos
 * - Gráficos de tendencias temporales
 * - Exportación de gráficos (PNG, SVG)
 *
 * EU AI Act Art. 10.2 - Transparencia en detección de sesgos
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class BiasVisualizationViewModel extends BaseFront<BiasVisualizationViewModel> {

    private static final long serialVersionUID = 1L;

    // ========== Servicios ==========
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private BiasVisualizationService biasVisualizationService;

    // ========== Datos de Visualización ==========
    @Getter
    private Long biasAnalysisId;

    @Getter
    private Map<String, Object> histogramData = new HashMap<>();

    @Getter
    private Map<String, Object> boxPlotData = new HashMap<>();

    @Getter
    private Map<String, Object> heatmapData = new HashMap<>();

    @Getter
    private Map<String, Object> trendData = new HashMap<>();

    @Getter
    private boolean dataLoaded = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        // Obtener biasAnalysisId desde parámetros
        String idParam = Executions.getCurrent().getParameter("biasAnalysisId");
        if (idParam != null) {
            try {
                biasAnalysisId = Long.parseLong(idParam);
                loadVisualizationData();
            } catch (NumberFormatException e) {
                log.error("Invalid biasAnalysisId parameter: {}", idParam);
                Messagebox.show("ID de análisis de bias inválido", "Error",
                              Messagebox.OK, Messagebox.ERROR);
            }
        }
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange({"histogramData", "boxPlotData", "heatmapData", "trendData", "dataLoaded"})
    public void loadVisualizationData() {
        if (biasAnalysisId == null) {
            Messagebox.show("Seleccione un análisis de bias", "Advertencia",
                          Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            log.info("Loading visualization data for bias analysis: {}", biasAnalysisId);

            // Cargar todos los datos de visualización
            histogramData = biasVisualizationService.getHistogramData(biasAnalysisId);
            boxPlotData = biasVisualizationService.getBoxPlotData(biasAnalysisId);
            heatmapData = biasVisualizationService.getHeatmapData(biasAnalysisId);

            // Para tendencias, necesitamos obtener el modelId del análisis
            // Esto se puede hacer desde el análisis de bias
            // Por ahora, intentamos cargar tendencias si es posible
            try {
                trendData = biasVisualizationService.getTrendData(biasAnalysisId);
            } catch (Exception e) {
                log.warn("Could not load trend data: {}", e.getMessage());
                trendData = new HashMap<>();
            }

            dataLoaded = true;
            log.info("Visualization data loaded successfully");

        } catch (BussinessException e) {
            log.error("Error loading visualization data", e);
            Messagebox.show("Error al cargar datos de visualización: " + e.getMessage(),
                          "Error", Messagebox.OK, Messagebox.ERROR);
            dataLoaded = false;
        } catch (Exception e) {
            log.error("Unexpected error loading visualization data", e);
            Messagebox.show("Error inesperado al cargar datos de visualización",
                          "Error", Messagebox.OK, Messagebox.ERROR);
            dataLoaded = false;
        }
    }

    @Command
    public void refreshData() {
        loadVisualizationData();
    }

    @Command
    public void exportChart(@BindingParam("chartType") String chartType) {
        // TODO: Implementar exportación de gráficos (PNG, SVG)
        Messagebox.show("Funcionalidad de exportación en desarrollo", "Información",
                      Messagebox.OK, Messagebox.INFORMATION);
    }

    // ========== Métodos auxiliares ==========

    /**
     * Obtiene los datos del histograma formateados para ZK Chart
     */
    public Map<String, Object> getHistogramChartModel() {
        if (histogramData.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Object> model = new HashMap<>();
        model.put("type", "column");
        model.put("title", histogramData.getOrDefault("title", "Distribución de Bias"));
        model.put("categories", histogramData.get("categories"));

        Map<String, Object> series = new HashMap<>();
        series.put("name", histogramData.getOrDefault("metric", "Bias Score"));
        series.put("data", histogramData.get("values"));
        model.put("series", series);

        return model;
    }

    /**
     * Obtiene los datos del box plot formateados para ZK Chart
     */
    public Map<String, Object> getBoxPlotChartModel() {
        if (boxPlotData.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Object> model = new HashMap<>();
        model.put("type", "column");
        model.put("title", boxPlotData.getOrDefault("title", "Métricas de Fairness"));
        model.put("categories", boxPlotData.get("categories"));
        model.put("series", boxPlotData.get("series"));

        return model;
    }

    /**
     * Obtiene los datos del heatmap formateados para ZK Chart
     */
    public Map<String, Object> getHeatmapChartModel() {
        if (heatmapData.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Object> model = new HashMap<>();
        model.put("type", "heatmap");
        model.put("title", heatmapData.getOrDefault("title", "Correlación entre Atributos"));
        model.put("attributes", heatmapData.get("attributes"));
        model.put("groups", heatmapData.get("groups"));
        model.put("matrix", heatmapData.get("matrix"));

        return model;
    }

    /**
     * Obtiene los datos de tendencias formateados para ZK Chart
     */
    public Map<String, Object> getTrendChartModel() {
        if (trendData.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Object> model = new HashMap<>();
        model.put("type", "line");
        model.put("title", trendData.getOrDefault("title", "Tendencias Temporales"));
        model.put("categories", trendData.get("dates"));
        model.put("series", trendData.get("series"));

        return model;
    }
}
