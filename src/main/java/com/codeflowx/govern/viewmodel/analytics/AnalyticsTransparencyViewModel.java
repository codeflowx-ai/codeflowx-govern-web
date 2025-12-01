package com.codeflowx.govern.viewmodel.analytics;
import com.codeflowx.framework.zkoss.BaseFront;

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

import com.codeflowx.govern.entity.governance.PolicyAuditLog;
import com.codeflowx.govern.service.governance.PolicyService;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.service.governance.PolicyAuditLogService;

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
 * Dashboard consolidado de análisis de Transparency (Transparencia)
 * Gestiona documentación, explicabilidad y acceso a información
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AnalyticsTransparencyViewModel extends BaseFront<AnalyticsTransparencyViewModel>{

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private PolicyAuditLogService policyAuditLogService;
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
    private List<Model> modelsList = new ArrayList<>();
    private List<PolicyAuditLog> auditLogsList = new ArrayList<>();
    
    // KPIs
    private Long totalModels = 0L;
    private Long documentedModels = 0L;
    private Long undocumentedModels = 0L;
    private BigDecimal transparencyScore = BigDecimal.ZERO;
    
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
        
        log.info("Inicializando AnalyticsTransparencyViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar modelos
            String sql1 = "SELECT * FROM MODMODELS ORDER BY MODCREATEDAT DESC LIMIT 20";
            List<Model> result1 = businessService.findByParams(Model.class, sql1, null);
            if (result1 != null) {
                modelsList = result1;
            }
            
            // Cargar audit logs para transparencia
            String sql2 = "SELECT * FROM GOVPOLICYAUDITLOGS ORDER BY GOVCHANGEDAT DESC LIMIT 20";
            List<PolicyAuditLog> result2 = businessService.findByParams(PolicyAuditLog.class, sql2, null);
            if (result2 != null) {
                auditLogsList = result2;
            }
            
            log.info("Cargados {} modelos para análisis de transparencia", modelsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos de transparencia", e);
        }
    }
    
    private void calculateKPIs() {
        totalModels = (long) modelsList.size();
        documentedModels = modelsList.stream()
            .filter(m -> m.getModdescription() != null && !m.getModdescription().trim().isEmpty())
            .count();
        undocumentedModels = totalModels - documentedModels;
        
        if (totalModels > 0) {
            transparencyScore = new BigDecimal(documentedModels)
                .divide(new BigDecimal(totalModels), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        }
        
        log.info("KPIs calculados - Total: {}, Documented: {}, Undocumented: {}, Score: {}%", 
            totalModels, documentedModels, undocumentedModels, transparencyScore);
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
    public void filterUndocumented() {
        try {
            undocumentedModels = modelsList.stream()
                .filter(m -> m.getModdescription() == null || m.getModdescription().trim().isEmpty())
                .count();
            Messagebox.show("Encontrados " + undocumentedModels + " modelos sin documentar", 
                "Información", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al filtrar modelos sin documentar", e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (modelsList != null) { 
            modelsList.clear(); 
            modelsList = null; 
        }
        if (auditLogsList != null) { 
            auditLogsList.clear(); 
            auditLogsList = null; 
        }
        policyService = null;
            policyAuditLogService = null;
            modelService = null;
    }
}

