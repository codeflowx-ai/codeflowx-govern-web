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

import com.codeflowx.govern.entity.evaluation.FairnessMetric;
import com.codeflowx.govern.entity.evaluation.BiasAnalysis;

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
 * Dashboard consolidado de análisis de Fairness (Equidad)
 * Gestiona métricas de fairness y análisis de equidad en modelos ML
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AnalyticsFairnessViewModel extends MasterPage {

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
    private List<FairnessMetric> fairnessMetricsList = new ArrayList<>();
    private List<BiasAnalysis> biasAnalysisList = new ArrayList<>();
    
    // KPIs
    private Long totalMetrics = 0L;
    private Long passedMetrics = 0L;
    private Long failedMetrics = 0L;
    private BigDecimal avgFairnessScore = BigDecimal.ZERO;
    private String worstPerformingGroup = "";
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("createdat")
                .build();
        
        log.info("Inicializando AnalyticsFairnessViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar métricas de fairness
            String sql1 = "SELECT * FROM ANLFAIRNESSMETRICS ORDER BY ANLCREATEDAT DESC LIMIT 20";
            List<FairnessMetric> result1 = businessService.findByParams(
                FairnessMetric.class, sql1, null
            );
            if (result1 != null) {
                fairnessMetricsList = result1;
            }
            
            // Cargar análisis de bias relacionados
            String sql2 = "SELECT * FROM ANLBIASANALYSES ORDER BY ANLCREATEDAT DESC LIMIT 20";
            List<BiasAnalysis> result2 = businessService.findByParams(
                BiasAnalysis.class, sql2, null
            );
            if (result2 != null) {
                biasAnalysisList = result2;
            }
            
            log.info("Cargadas {} métricas de fairness", fairnessMetricsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos de fairness", e);
        }
    }
    
    private void calculateKPIs() {
        totalMetrics = (long) fairnessMetricsList.size();
        passedMetrics = fairnessMetricsList.stream()
            .filter(m -> m.getMetricvalue() != null && m.getMetricvalue().compareTo(new BigDecimal("0.8")) >= 0)
            .count();
        failedMetrics = totalMetrics - passedMetrics;
        
        if (!fairnessMetricsList.isEmpty()) {
            BigDecimal sum = fairnessMetricsList.stream()
                .filter(m -> m.getMetricvalue() != null)
                .map(FairnessMetric::getMetricvalue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgFairnessScore = sum.divide(new BigDecimal(fairnessMetricsList.size()), 2, RoundingMode.HALF_UP);
        }
        
        log.info("KPIs calculados - Total: {}, Passed: {}, Failed: {}, Avg: {}", 
            totalMetrics, passedMetrics, failedMetrics, avgFairnessScore);
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
    public void filterByMetricType(String metricType) {
        try {
            String sql = "SELECT * FROM ANLFAIRNESSMETRICS WHERE METRICNAME = :metricType ORDER BY ANLCREATEDAT DESC LIMIT 20";
            Map<String, Object> params = new HashMap<>();
            params.put("metricType", metricType);
            
            List<FairnessMetric> result = businessService.findByParams(
                FairnessMetric.class, sql, params
            );
            if (result != null) {
                fairnessMetricsList = result;
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por tipo de métrica", e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (fairnessMetricsList != null) { 
            fairnessMetricsList.clear(); 
            fairnessMetricsList = null; 
        }
        if (biasAnalysisList != null) { 
            biasAnalysisList.clear(); 
            biasAnalysisList = null; 
        }
        businessService = null;
    }
}

