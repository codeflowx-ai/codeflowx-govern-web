package com.codeflowx.govern.viewmodel.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.entity.governance.ComplianceAssessment;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Catálogo de Modelos
 * Vista detallada del catálogo de modelos ML con filtros avanzados
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class CatalogModelsViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable private BusinessService businessService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {}

    private PageParams pageParams;
    private List<Model> modelsList = new ArrayList<>();
    private List<ModelVersion> versionsList = new ArrayList<>();
    private List<ComplianceAssessment> assessmentsList = new ArrayList<>();
    
    // KPIs
    private Long totalModels = 0L;
    private Long deployedModels = 0L;
    private Long archivedModels = 0L;
    private Long draftModels = 0L;
    
    // Filtros
    private String categoryFilter = "";
    private String statusFilter = "";
    private String frameworkFilter = "";
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        pageParams = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("createdat")
                .build();
        
        log.info("Inicializando CatalogModelsViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar modelos
            String sql1 = "SELECT * FROM MDLMODELS LIMIT 100";
            List<Model> result1 = businessService.findByParams(Model.class, sql1, null);
            if (result1 != null) {
                modelsList = result1;
            }
            
            // Cargar versiones de modelos
            String sql2 = "SELECT * FROM MDLMODELVERSIONS LIMIT 100";
            List<ModelVersion> result2 = businessService.findByParams(ModelVersion.class, sql2, null);
            if (result2 != null) {
                versionsList = result2;
            }
            
            // Cargar assessments
            String sql3 = "SELECT * FROM CMPCOMPLIANCEASSESSMENTS LIMIT 100";
            List<ComplianceAssessment> result3 = businessService.findByParams(ComplianceAssessment.class, sql3, null);
            if (result3 != null) {
                assessmentsList = result3;
            }
            
            log.info("Cargados {} modelos en el catálogo", modelsList.size());
        } catch (Exception e) {
            log.error("Error al cargar catálogo de modelos", e);
        }
    }
    
    private void calculateKPIs() {
        totalModels = (long) modelsList.size();
        deployedModels = modelsList.stream()
            .filter(m -> "DEPLOYED".equals(m.getModstatus()))
            .count();
        archivedModels = modelsList.stream()
            .filter(m -> "ARCHIVED".equals(m.getModstatus()))
            .count();
        draftModels = modelsList.stream()
            .filter(m -> "DRAFT".equals(m.getModstatus()))
            .count();
        
        log.info("KPIs calculados - Total: {}, Deployed: {}, Archived: {}, Draft: {}", 
            totalModels, deployedModels, archivedModels, draftModels);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Command
    @NotifyChange("*")
    public void filterByCategory(String category) {
        categoryFilter = category;
        try {
            String sql = "SELECT * FROM MDLMODELS WHERE MDLCATEGORY = :category LIMIT 100";
            Map<String, Object> params = new HashMap<>();
            params.put("category", category);
            
            List<Model> result = businessService.findByParams(Model.class, sql, params);
            if (result != null) {
                modelsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por categoría", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void filterByStatus(String status) {
        statusFilter = status;
        try {
            String sql = "SELECT * FROM MDLMODELS WHERE MDLSTATUS = :status LIMIT 100";
            Map<String, Object> params = new HashMap<>();
            params.put("status", status);
            
            List<Model> result = businessService.findByParams(Model.class, sql, params);
            if (result != null) {
                modelsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por estado", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void filterByFramework(String framework) {
        frameworkFilter = framework;
        try {
            String sql = "SELECT * FROM MDLMODELS WHERE MDLFRAMEWORK = :framework LIMIT 100";
            Map<String, Object> params = new HashMap<>();
            params.put("framework", framework);
            
            List<Model> result = businessService.findByParams(Model.class, sql, params);
            if (result != null) {
                modelsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por framework", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void searchModels(String searchTerm) {
        try {
            String sql = "SELECT * FROM MDLMODELS WHERE MDLNAME LIKE :searchTerm LIMIT 100";
            Map<String, Object> params = new HashMap<>();
            params.put("searchTerm", "%" + searchTerm + "%");
            
            List<Model> result = businessService.findByParams(Model.class, sql, params);
            if (result != null) {
                modelsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al buscar modelos", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void clearFilters() {
        categoryFilter = "";
        statusFilter = "";
        frameworkFilter = "";
        loadData();
        calculateKPIs();
    }
    
    @Destroy
    public void destroy() {
        if (modelsList != null) { 
            modelsList.clear(); 
            modelsList = null; 
        }
        if (versionsList != null) { 
            versionsList.clear(); 
            versionsList = null; 
        }
        if (assessmentsList != null) { 
            assessmentsList.clear(); 
            assessmentsList = null; 
        }
        businessService = null;
    }
}

