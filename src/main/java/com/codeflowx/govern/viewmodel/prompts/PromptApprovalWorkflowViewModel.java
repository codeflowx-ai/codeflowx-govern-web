package com.codeflowx.govern.viewmodel.prompts;

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

import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.entity.prompts.PromptVersion;
import com.codeflowx.govern.entity.procedures.governance.AutoApproveArtifact;
import com.codeflowx.govern.entity.procedures.governance.RequestHumanReview;

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
 * Pantalla de Flujo de Aprobación de Prompts
 * Gestiona el workflow de aprobación de prompts y versiones
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class PromptApprovalWorkflowViewModel extends MasterPage {

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
    private List<Prompt> pendingPromptsList = new ArrayList<>();
    private List<PromptVersion> versionsList = new ArrayList<>();
    
    // KPIs
    private Long totalPendingPrompts = 0L;
    private Long approvedPrompts = 0L;
    private Long rejectedPrompts = 0L;
    private Long inReviewPrompts = 0L;
    
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
        
        log.info("Inicializando PromptApprovalWorkflowViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar prompts pendientes de aprobación
            Criterias criterias1 = new Criterias();
            Criteria criteria1 = new Criteria(Operation.AND, Evaluation.EQUALS, "prtstatus");
            criteria1.setValueEnd("PENDING_APPROVAL");
            criterias1.addCriteria(criteria1);
            
            PageResult<Prompt> result1 = businessService.findAllEntity(
                Prompt.class, 
                pageParams, 
                criterias1
            );
            if (result1 != null && result1.getContent() != null) {
                pendingPromptsList = result1.getContent();
            }
            
            // Cargar versiones de prompts
            PageResult<PromptVersion> result2 = businessService.findAllEntity(
                PromptVersion.class, 
                pageParams, 
                new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                versionsList = result2.getContent();
            }
            
            log.info("Cargados {} prompts pendientes de aprobación", pendingPromptsList.size());
        } catch (Exception e) {
            log.error("Error al cargar prompts pendientes", e);
        }
    }
    
    private void calculateKPIs() {
        totalPendingPrompts = (long) pendingPromptsList.size();
        
        // Calcular prompts por estado
        approvedPrompts = pendingPromptsList.stream()
            .filter(p -> "APPROVED".equals(p.getPrmstatus()))
            .count();
        rejectedPrompts = pendingPromptsList.stream()
            .filter(p -> "REJECTED".equals(p.getPrmstatus()))
            .count();
        inReviewPrompts = pendingPromptsList.stream()
            .filter(p -> "PENDING_APPROVAL".equals(p.getPrmstatus()))
            .count();
        
        log.info("KPIs calculados - Pending: {}, Approved: {}, Rejected: {}, In Review: {}", 
            totalPendingPrompts, approvedPrompts, rejectedPrompts, inReviewPrompts);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    /**
     * Auto-aprobar prompt usando procedure
     */
    @Command
    @NotifyChange("*")
    public void autoApprovePrompt(Long promptId) {
        try {
            AutoApproveArtifact procedure = new AutoApproveArtifact();
            procedure.setPInputParam(promptId);
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                loadData();
                calculateKPIs();
                Messagebox.show("Prompt aprobado automáticamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("No se pudo aprobar el prompt automáticamente", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al auto-aprobar prompt", e);
            Messagebox.show("Error al aprobar prompt: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void approvePrompt(Long promptId, String comments) {
        try {
            Prompt prompt = businessService.findById(Prompt.class, promptId);
            if (prompt != null) {
                prompt.setPrmstatus("APPROVED");
                businessService.update(prompt);
                
                loadData();
                calculateKPIs();
                Messagebox.show("Prompt aprobado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error al aprobar prompt", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void rejectPrompt(Long promptId, String reason) {
        try {
            Prompt prompt = businessService.findById(Prompt.class, promptId);
            if (prompt != null) {
                prompt.setPrmstatus("REJECTED");
                businessService.update(prompt);
                
                loadData();
                calculateKPIs();
                Messagebox.show("Prompt rechazado", "Información", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error al rechazar prompt", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void requestReview(Long promptId) {
        try {
            RequestHumanReview procedure = new RequestHumanReview();
            procedure.setPInputParam(promptId);
            
            procedure = businessService.callProcedure(procedure);
            
            loadData();
            calculateKPIs();
            Messagebox.show("Revisión humana solicitada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al solicitar revisión", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Destroy
    public void destroy() {
        if (pendingPromptsList != null) { 
            pendingPromptsList.clear(); 
            pendingPromptsList = null; 
        }
        if (versionsList != null) { 
            versionsList.clear(); 
            versionsList = null; 
        }
        businessService = null;
    }
}

