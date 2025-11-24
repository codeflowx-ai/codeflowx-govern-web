package com.codeflowx.platform.viewmodel.datasources;

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
import com.codeflowx.govern.entity.datasources.DataSourceDatabase;
import com.codeflowx.govern.service.datasources.DataSourceDatabaseService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import org.zkoss.zk.ui.select.annotation.WireVariable;
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
public class DataSourceDatabaseOverviewViewModel extends BaseFront<DataSourceDatabaseOverviewViewModel> {

    private static final long serialVersionUID = 1L;

    @Override
    public void setBeans(Object bean) {}

    @WireVariable
    private DataSourceDatabaseService dataSourceDatabaseService;

    private PageParams pageParams;
    private PageResult<DataSourceDatabase> pageResult;
    private String searchText = "";
    private String filterType = "";
    private String filterStatus = "";
    private List<DataSourceDatabase> dbSourcesList = new ArrayList<>();
    private int totalDatabases = 0;
    private int activeDatabases = 0;
    private int errorDatabases = 0;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder().maxRows(20).pageActual(1).rowActual(0).build();
        loadData();
        loadMetrics();
    }

    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            Criterias criterias = buildCriterias();
            pageResult = dataSourceDatabaseService.findAll(pageParams, criterias);

            if (pageResult != null && pageResult.getContent() != null) {
                dbSourcesList = pageResult.getContent();
                totalDatabases = pageResult.getTotalRows();

                logActivity("BUSCAR", "DATASOURCEDATABASES", null,
                    "Búsqueda: " + dbSourcesList.size() + " resultados");
            } else {
                dbSourcesList = new ArrayList<>();
                totalDatabases = 0;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar bases de datos", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        activeDatabases = (int) dbSourcesList.stream().filter(db -> "ACTIVE".equals(db.getDbstatus())).count();
        errorDatabases = (int) dbSourcesList.stream().filter(db -> "ERROR".equals(db.getDbstatus())).count();
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "dbname", searchText));
        }
        if (filterType != null && !filterType.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dbtype", filterType));
        }
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dbstatus", filterStatus));
        }
        return criterias;
    }

    @Command
    @NotifyChange("*")
    public void filterDatabases() {
        pageParams.setPageActual(1);
        loadData();
    }

    @Command
    @NotifyChange("*")
    public void refreshDatabases() {
        loadData();
        loadMetrics();
    }

    @Command
    @NotifyChange("*")
    public void deleteDatabase(@BindingParam("db") DataSourceDatabase db) {
        Messagebox.show("¿Eliminar base de datos: " + db.getDbname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        dataSourceDatabaseService.deleteById(db.getIdxdatasourcedatabase());
                        logActivity("ELIMINAR", "DATASOURCEDATABASES", db.getIdxdatasourcedatabase(),
                            "Base de datos eliminada: " + db.getDbname());
                        loadData();
                        loadMetrics();
                    } catch (GovernanceServiceException e) {
                        log.error("Error al eliminar", e);
                    }
                }
            });
    }

    public String getDbStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "ACTIVE": return "success";
            case "ERROR": return "danger";
            case "CONNECTING": return "warning";
            default: return "secondary";
        }
    }

    public String formatDate(Timestamp ts) {
        return ts != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "-";
    }

    @Destroy
    public void destroy() {
        if (dbSourcesList != null) {
            dbSourcesList.clear();
            dbSourcesList = null;
        }
        pageResult = null;
        pageParams = null;
        dataSourceDatabaseService = null;
    }
}
