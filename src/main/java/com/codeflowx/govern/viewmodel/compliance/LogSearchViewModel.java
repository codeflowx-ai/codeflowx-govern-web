package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.business.compliance.LogSearchService;
import com.codeflowx.govern.business.exception.BussinessException;
import com.codeflowx.govern.entity.logging.ImmutableLog;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel: Búsqueda Avanzada de Logs
 *
 * Funcionalidad:
 * - Búsqueda con filtros múltiples (fecha, nivel, proyecto, usuario)
 * - Búsqueda por texto libre
 * - Paginación de resultados
 * - Exportación de resultados
 *
 * EU AI Act Art. 19 - Logs Inmutables
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class LogSearchViewModel extends BaseFront<LogSearchViewModel> {

    private static final long serialVersionUID = 1L;

    // ========== Servicios ==========
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private LogSearchService logSearchService;

    // ========== Criterios de Búsqueda ==========
    @Getter @Setter
    private LogSearchService.LogSearchCriteria criteria = new LogSearchService.LogSearchCriteria();

    // ========== Resultados ==========
    @Getter
    private LogSearchService.LogSearchResult searchResult;

    @Getter
    private List<ImmutableLog> logs = new ArrayList<>();

    @Getter
    private List<String> availableActions = new ArrayList<>();

    @Getter
    private List<String> availableEntityTypes = new ArrayList<>();

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        // Inicializar criterios
        criteria.setPage(0);
        criteria.setPageSize(50);

        // Cargar listas desplegables
        loadAvailableOptions();
    }

    // ========== Comandos ==========

    @Command
    @NotifyChange({"searchResult", "logs"})
    public void search() {
        try {
            log.info("Searching logs with criteria: {}", criteria);
            searchResult = logSearchService.searchLogs(criteria);
            logs = searchResult.getLogs();

            if (logs.isEmpty()) {
                Messagebox.show("No se encontraron logs con los criterios especificados",
                              "Información", Messagebox.OK, Messagebox.INFORMATION);
            }

        } catch (BussinessException e) {
            log.error("Error searching logs", e);
            Messagebox.show("Error al buscar logs: " + e.getMessage(),
                          "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"criteria", "searchResult", "logs"})
    public void clearFilters() {
        criteria = new LogSearchService.LogSearchCriteria();
        criteria.setPage(0);
        criteria.setPageSize(50);
        searchResult = null;
        logs = new ArrayList<>();
    }

    @Command
    @NotifyChange({"searchResult", "logs"})
    public void nextPage() {
        if (searchResult != null && criteria.getPage() < searchResult.getTotalPages() - 1) {
            criteria.setPage(criteria.getPage() + 1);
            search();
        }
    }

    @Command
    @NotifyChange({"searchResult", "logs"})
    public void previousPage() {
        if (criteria.getPage() > 0) {
            criteria.setPage(criteria.getPage() - 1);
            search();
        }
    }

    @Command
    public void exportResults() {
        // TODO: Implementar exportación de resultados
        Messagebox.show("Funcionalidad de exportación en desarrollo", "Información",
                      Messagebox.OK, Messagebox.INFORMATION);
    }

    // ========== Métodos auxiliares ==========

    private void loadAvailableOptions() {
        try {
            availableActions = logSearchService.getAvailableActions();
            availableEntityTypes = logSearchService.getAvailableEntityTypes();
        } catch (BussinessException e) {
            log.error("Error loading available options", e);
        }
    }

    // ========== Getters para UI ==========

    public boolean isHasNextPage() {
        return searchResult != null && criteria.getPage() < searchResult.getTotalPages() - 1;
    }

    public boolean isHasPreviousPage() {
        return criteria.getPage() > 0;
    }

    public String getPageInfo() {
        if (searchResult == null) {
            return "Sin resultados";
        }
        return String.format("Página %d de %d (Total: %d)",
            criteria.getPage() + 1,
            searchResult.getTotalPages(),
            searchResult.getTotalCount());
    }
}
