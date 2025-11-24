package com.codeflowx.platform.viewmodel.datasources;

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
import com.codeflowx.govern.entity.datasources.DataSource;
import com.codeflowx.govern.service.datasources.DataSourceService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para búsqueda y listado de Data Sources
 * Pantalla principal de gestión de fuentes de datos
 */
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class DataSourcesOverviewViewModel extends BaseFront<DataSourcesOverviewViewModel> {

    private static final long serialVersionUID = 1L;

    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }

    @WireVariable
    private DataSourceService dataSourceService;

    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<DataSource> pageResult;

    // ========== Filtros ==========
    private String searchText = "";
    private String filterType = "";
    private String filterStatus = "";

    // ========== Datos ==========
    private List<DataSource> dataSourceList = new ArrayList<>();

    // ========== Métricas ==========
    private int totalDataSources = 0;
    private int activeDataSources = 0;
    private int errorDataSources = 0;
    private long totalDocuments = 0L;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadData();
        loadMetrics();
    }

    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            log.debug("Cargando data sources - Página: {}", pageParams.getPageActual());

            Criterias criterias = buildCriterias();

            pageResult = dataSourceService.findAll(pageParams, criterias);

            if (pageResult != null && pageResult.getContent() != null) {
                dataSourceList = pageResult.getContent();
                totalDataSources = pageResult.getTotalRows();

                // Auditar búsqueda
                logActivity("BUSCAR", "DATASOURCES", null,
                    "Búsqueda: " + dataSourceList.size() + " resultados");

                log.info("Cargados {} data sources de {} totales",
                    dataSourceList.size(), totalDataSources);
            } else {
                dataSourceList = new ArrayList<>();
                totalDataSources = 0;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar data sources", e);
            Messagebox.show("Error al cargar data sources: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        try {
            // TODO: Implementar integración con leka-server para métricas en tiempo real
            // Por ahora usamos datos locales
            activeDataSources = (int) dataSourceList.stream()
                .filter(ds -> "ACTIVE".equals(ds.getDsstatus()))
                .count();

            errorDataSources = (int) dataSourceList.stream()
                .filter(ds -> "ERROR".equals(ds.getDsstatus()))
                .count();

            totalDocuments = dataSourceList.stream()
                .mapToLong(ds -> ds.getDsdocumentcount() != null ? ds.getDsdocumentcount() : 0L)
                .sum();

        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        // Filtro por búsqueda de texto
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "dsname", searchText));
        }

        // Filtro por tipo
        if (filterType != null && !filterType.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dstype", filterType));
        }

        // Filtro por estado
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dsstatus", filterStatus));
        }

        return criterias;
    }

    @Command
    @NotifyChange("*")
    public void filterDataSources() {
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void searchDataSources() {
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void clearFilters() {
        searchText = "";
        filterType = "";
        filterStatus = "";
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void refreshDataSources() {
        loadData();
        loadMetrics();
    }

    @Command
    public void createDataSource() {
        // TODO: Navegar a pantalla de creación
        log.info("Crear nuevo data source");
    }

    @Command
    public void viewDataSource(@BindingParam("ds") DataSource dataSource) {
        // TODO: Navegar a vista detalle
        log.info("Ver data source: {}", dataSource.getDsname());
    }

    @Command
    public void editDataSource(@BindingParam("ds") DataSource dataSource) {
        // TODO: Navegar a pantalla de edición
        log.info("Editar data source: {}", dataSource.getDsname());
    }

    @Command
    @NotifyChange("*")
    public void testDataSource(@BindingParam("ds") DataSource dataSource) {
        try {
            // TODO: Implementar integración con leka-server para test de conexión
            log.info("Testeando data source: {}", dataSource.getDsname());
            Messagebox.show("Funcionalidad de test en desarrollo. Requiere integración con leka-server.",
                "Info", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al testear data source", e);
            Messagebox.show("Error al testear data source: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void deleteDataSource(@BindingParam("ds") DataSource dataSource) {
        Messagebox.show("¿Está seguro de eliminar el data source: " + dataSource.getDsname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        dataSourceService.deleteById(dataSource.getIdxdatasource());

                        // Auditar eliminación
                        logActivity("ELIMINAR", "DATASOURCES", dataSource.getIdxdatasource(),
                            "Data source eliminado: " + dataSource.getDsname());

                        loadData();
                        loadMetrics();
                        Messagebox.show("Data source eliminado exitosamente",
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (GovernanceServiceException e) {
                        log.error("Error al eliminar data source", e);
                        Messagebox.show("Error al eliminar data source: " + e.getMessage(),
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    @Command
    public void exportDataSources() {
        // TODO: Implementar exportación
        log.info("Exportar data sources");
        Messagebox.show("Funcionalidad de exportación en desarrollo",
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    // ========== Métodos auxiliares ==========

    public String getStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "ACTIVE": return "success";
            case "INACTIVE": return "secondary";
            case "ERROR": return "danger";
            case "SYNCING": return "warning";
            default: return "secondary";
        }
    }

    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }

    @Destroy
    public void destroy() {
        if (dataSourceList != null) {
            dataSourceList.clear();
            dataSourceList = null;
        }
        pageResult = null;
        pageParams = null;
        dataSourceService = null;
    }
}
