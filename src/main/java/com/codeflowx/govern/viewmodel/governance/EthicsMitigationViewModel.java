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

import com.codeflowx.govern.entity.governance.ComplianceFinding;
import com.codeflowx.govern.service.governance.PolicyService;
import com.codeflowx.govern.entity.governance.PolicyViolation;
import com.codeflowx.govern.entity.evaluation.BiasRecommendation;
import com.codeflowx.govern.entity.procedures.governance.RemediateComplianceFinding;
import com.codeflowx.govern.service.governance.ComplianceFindingService;
import com.codeflowx.govern.service.governance.PolicyViolationService;
import com.codeflowx.govern.service.evaluation.BiasRecommendationService;

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
 * Pantalla de Estrategias de Mitigación Ética
 * Gestiona recomendaciones y acciones de mitigación
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsMitigationViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private PolicyViolationService policyViolationService;
    @WireVariable
    private ComplianceFindingService complianceFindingService;
    @WireVariable
    private BiasRecommendationService biasRecommendationService;
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
    private List<BiasRecommendation> recommendationsList = new ArrayList<>();
    private List<ComplianceFinding> findingsList = new ArrayList<>();
    private List<PolicyViolation> violationsList = new ArrayList<>();
    
    // KPIs
    private Long totalRecommendations = 0L;
    private Long implementedActions = 0L;
    private Long pendingActions = 0L;
    private BigDecimal mitigationEffectiveness = BigDecimal.ZERO;
    
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
        
        log.info("Inicializando EthicsMitigationViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            PageResult<BiasRecommendation> result1 = biasRecommendationService.findAll(pageParams, new Criterias());
            if (result1 != null && result1.getContent() != null) {
                recommendationsList = result1.getContent();
            }
            
            PageResult<ComplianceFinding> result2 = complianceFindingService.findAll(pageParams, new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                findingsList = result2.getContent();
            }
            
            PageResult<PolicyViolation> result3 = policyViolationService.findAll(pageParams, new Criterias()
            );
            if (result3 != null && result3.getContent() != null) {
                violationsList = result3.getContent();
            }
            
            log.info("Cargadas {} recomendaciones de mitigación", recommendationsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos de mitigación", e);
        }
    }
    
    private void calculateKPIs() {
        totalRecommendations = (long) recommendationsList.size();
        implementedActions = recommendationsList.stream()
            .filter(r -> "IMPLEMENTED".equals(r.getImplementationstatus()))
            .count();
        pendingActions = totalRecommendations - implementedActions;
        
        if (totalRecommendations > 0) {
            mitigationEffectiveness = new BigDecimal(implementedActions)
                .divide(new BigDecimal(totalRecommendations), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        }
        
        log.info("KPIs calculados - Total: {}, Implemented: {}, Pending: {}, Effectiveness: {}%", 
            totalRecommendations, implementedActions, pendingActions, mitigationEffectiveness);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    /**
     * Llamar procedure para remediar un finding
     */
    @Command
    @NotifyChange("*")
    public void remediateFinding(Long findingId, String action) {
        try {
            RemediateComplianceFinding procedure = new RemediateComplianceFinding();
            procedure.setPInputParam(findingId);
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                loadData();
                calculateKPIs();
                Messagebox.show("Finding remediado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("No se pudo remediar el finding", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al remediar finding", e);
            Messagebox.show("Error al remediar finding: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Destroy
    public void destroy() {
        if (recommendationsList != null) { 
            recommendationsList.clear(); 
            recommendationsList = null; 
        }
        if (findingsList != null) { 
            findingsList.clear(); 
            findingsList = null; 
        }
        if (violationsList != null) { 
            violationsList.clear(); 
            violationsList = null; 
        }
        policyService = null;
            policyViolationService = null;
            complianceFindingService = null;
    }
}



