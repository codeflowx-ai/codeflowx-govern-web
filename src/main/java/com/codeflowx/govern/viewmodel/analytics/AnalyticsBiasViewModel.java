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

import com.codeflowx.govern.entity.evaluation.BiasAnalysis;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.evaluation.BiasDetection;
import com.codeflowx.govern.service.evaluation.BiasAnalysisService;
import com.codeflowx.govern.service.evaluation.BiasDetectionService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

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
 * Dashboard consolidado de análisis de sesgos
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AnalyticsBiasViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private ModelService modelService;
    @WireVariable
    private BiasAnalysisService biasAnalysisService;
    @WireVariable
    private BiasDetectionService biasDetectionService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    
    @Override
    public void setBeans(Object bean) {}

    private PageParams pageParams;
    private List<BiasAnalysis> biasAnalysisList = new ArrayList<>();
    private List<BiasDetection> detections = new ArrayList<>();
    private Long totalAnalysis = 0L;
    private Long detectedBiases = 0L;
    private BigDecimal avgBiasScore = BigDecimal.ZERO;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        pageParams = PageParams.builder().maxRows(20).pageActual(1).rowActual(0).ascending(false).sortField("createdat").build();
        
        log.info("Inicializando AnalyticsBiasViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // TABLE - usar servicio dedicado
            PageParams pageParams1 = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<BiasAnalysis> result1 = biasAnalysisService.findAll(pageParams1);
            if (result1 != null && result1.getContent() != null) {
                biasAnalysisList = result1.getContent();
            }
            
            // TABLE - usar servicio dedicado
            PageParams pageParams2 = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<BiasDetection> result2 = biasDetectionService.findAll(pageParams2);
            if (result2 != null && result2.getContent() != null) {
                detections = result2.getContent();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar datos", e);
        }
    }
    
    private void calculateKPIs() {
        totalAnalysis = (long) biasAnalysisList.size();
        // Contar análisis con score alto (>0.7) como posibles sesgos detectados
        detectedBiases = biasAnalysisList.stream()
            .filter(b -> b.getOverallbiasscore() != null && b.getOverallbiasscore().compareTo(new BigDecimal("0.7")) > 0)
            .count();
        
        // Calcular score promedio
        if (!biasAnalysisList.isEmpty()) {
            BigDecimal totalScore = biasAnalysisList.stream()
                .filter(b -> b.getOverallbiasscore() != null)
                .map(BiasAnalysis::getOverallbiasscore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgBiasScore = totalScore.divide(new BigDecimal(biasAnalysisList.size()), 2, RoundingMode.HALF_UP);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    @Destroy
    public void destroy() {
        if (biasAnalysisList != null) { biasAnalysisList.clear(); biasAnalysisList = null; }
        if (detections != null) { detections.clear(); detections = null; }
    }
}

