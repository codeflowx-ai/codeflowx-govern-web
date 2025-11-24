package com.codeflowx.govern.viewmodel.governance;

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

import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.service.governance.ComplianceAssessmentService;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.service.models.ModelService;

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
 * Pantalla de Análisis de Impacto Ético
 * Gestiona análisis de impacto ético de modelos ML/AI
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsImpactViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private ComplianceAssessmentService complianceAssessmentService;
    @WireVariable
    private ModelService modelService;
    @WireVariable public Environment environment;
    @WireVariable("context") protected GenericApplicationContext contexto;
    @WireVariable("ctxBean") protected Context ctxBean;
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }
    
    @Override
    public void setBeans(Object bean) {}

    private PageParams pageParams;
    private List<ComplianceAssessment> assessmentsList = new ArrayList<>();
    private List<Model> modelsList = new ArrayList<>();
    
    // KPIs
    private Long totalImpactAssessments = 0L;
    private Long highImpactModels = 0L;
    private Long mediumImpactModels = 0L;
    private Long lowImpactModels = 0L;
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
        
        log.info("Inicializando EthicsImpactViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            PageResult<ComplianceAssessment> result1 = complianceAssessmentService.findAll(pageParams, new Criterias()
            );
            if (result1 != null && result1.getContent() != null) {
                assessmentsList = result1.getContent();
            }
            
            PageResult<Model> result2 = modelService.findAll(pageParams, new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                modelsList = result2.getContent();
            }
            
            log.info("Cargados {} assessments de impacto ético", assessmentsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos de impacto ético", e);
        }
    }
    
    private void calculateKPIs() {
        totalImpactAssessments = (long) assessmentsList.size();
        // ComplianceAssessment no tiene risklevel, filtrar por status u otro campo apropiado
        highImpactModels = 0L; // TODO: definir criterio apropiado para high impact
        mediumImpactModels = 0L; // TODO: definir criterio apropiado para medium impact
        lowImpactModels = 0L; // TODO: definir criterio apropiado para low impact
        
        if (!assessmentsList.isEmpty()) {
            BigDecimal sum = assessmentsList.stream()
                .filter(a -> a.getOverallscore() != null)
                .map(ComplianceAssessment::getOverallscore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgImpactScore = sum.divide(new BigDecimal(assessmentsList.size()), 2, RoundingMode.HALF_UP);
        }
        
        log.info("KPIs calculados - Total: {}, High: {}, Medium: {}, Low: {}, Avg: {}", 
            totalImpactAssessments, highImpactModels, mediumImpactModels, lowImpactModels, avgImpactScore);
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
    public void filterByStatus(String status) {
        try {
            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "status");
            criteria.setValueEnd(status);
            criterias.addCriteria(criteria);
            
            PageResult<ComplianceAssessment> result = complianceAssessmentService.findAll(pageParams, criterias
            );
            if (result != null && result.getContent() != null) {
                assessmentsList = result.getContent();
                calculateKPIs();
            }
        } catch (Exception e) {
            log.error("Error al filtrar por status", e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (assessmentsList != null) { 
            assessmentsList.clear(); 
            assessmentsList = null; 
        }
        if (modelsList != null) { 
            modelsList.clear(); 
            modelsList = null; 
        }
        complianceAssessmentService = null;
            modelService = null;
    }
}

