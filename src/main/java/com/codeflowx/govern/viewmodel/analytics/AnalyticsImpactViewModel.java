package com.codeflowx.govern.viewmodel.analytics;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * Dashboard consolidado de análisis de Impact (Impacto)
 * Gestiona análisis de impacto social, económico y ambiental
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AnalyticsImpactViewModel extends MasterPage {

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
    private List<ComplianceAssessment> assessmentsList = new ArrayList<>();
    
    // KPIs
    private Long totalModels = 0L;
    private Long highRiskModels = 0L;
    private Long mediumRiskModels = 0L;
    private Long lowRiskModels = 0L;
    private BigDecimal avgImpactScore = BigDecimal.ZERO;
    
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
        
        log.info("Inicializando AnalyticsImpactViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar modelos
            String sql1 = "SELECT * FROM MODMODELS ORDER BY MODCREATEDAT DESC LIMIT 20";
            List<Model> result1 = businessService.findByParams(
                Model.class, sql1, null
            );
            if (result1 != null) {
                modelsList = result1;
            }
            
            // Cargar assessments de compliance
            String sql2 = "SELECT * FROM GOVCOMPLIANCEASSESSMENTS ORDER BY CREATEDAT DESC LIMIT 20";
            List<ComplianceAssessment> result2 = businessService.findByParams(
                ComplianceAssessment.class, sql2, null
            );
            if (result2 != null) {
                assessmentsList = result2;
            }
            
            log.info("Cargados {} modelos y {} assessments para análisis de impacto", 
                modelsList.size(), assessmentsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos de impacto", e);
        }
    }
    
    private void calculateKPIs() {
        totalModels = (long) modelsList.size();
        
        // Analizar nivel de riesgo desde status de assessments
        // Nota: ComplianceAssessment no tiene campo 'risklevel', usar 'status' como aproximación
        highRiskModels = assessmentsList.stream()
            .filter(a -> "NON_COMPLIANT".equals(a.getStatus()))
            .count();
        mediumRiskModels = assessmentsList.stream()
            .filter(a -> "PARTIAL".equals(a.getStatus()))
            .count();
        lowRiskModels = assessmentsList.stream()
            .filter(a -> "COMPLIANT".equals(a.getStatus()))
            .count();
        
        // Calcular score promedio de impacto usando overallscore
        if (!assessmentsList.isEmpty()) {
            BigDecimal sum = assessmentsList.stream()
                .filter(a -> a.getOverallscore() != null)
                .map(ComplianceAssessment::getOverallscore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgImpactScore = sum.divide(new BigDecimal(assessmentsList.size()), 2, RoundingMode.HALF_UP);
        }
        
        log.info("KPIs calculados - Total: {}, High Risk: {}, Medium Risk: {}, Low Risk: {}, Avg: {}", 
            totalModels, highRiskModels, mediumRiskModels, lowRiskModels, avgImpactScore);
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
    public void filterByRiskLevel(String riskLevel) {
        try {
            // Mapear risk level a status
            String status = "HIGH".equals(riskLevel) ? "NON_COMPLIANT" : 
                           "MEDIUM".equals(riskLevel) ? "PARTIAL" : "COMPLIANT";
            
            String sql = "SELECT * FROM GOVCOMPLIANCEASSESSMENTS WHERE STATUS = :status ORDER BY CREATEDAT DESC LIMIT 20";
            Map<String, Object> params = new HashMap<>();
            params.put("status", status);
            
            List<ComplianceAssessment> result = businessService.findByParams(
                ComplianceAssessment.class, sql, params
            );
            if (result != null) {
                assessmentsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por nivel de riesgo", e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (modelsList != null) { 
            modelsList.clear(); 
            modelsList = null; 
        }
        if (assessmentsList != null) { 
            assessmentsList.clear(); 
            assessmentsList = null; 
        }
        businessService = null;
    }
}

