package com.codeflowx.govern.viewmodel.agents;

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

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.agents.AgentApproval;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Pantalla de Flujo de Aprobación de Agentes
 * Gestiona el workflow de aprobación de agentes ML
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AgentApprovalWorkflowViewModel extends MasterPage {

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
    private List<AgentApproval> approvalsList = new ArrayList<>();
    private List<Agent> agentsList = new ArrayList<>();
    
    // KPIs
    private Long totalApprovals = 0L;
    private Long pendingApprovals = 0L;
    private Long approvedAgents = 0L;
    private Long rejectedAgents = 0L;
    
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
                .sortField("agtrequestdate")
                .build();
        
        log.info("Inicializando AgentApprovalWorkflowViewModel");
        loadData();
        calculateKPIs();
    }
    
    private void loadData() {
        try {
            // Cargar aprobaciones de agentes (TABLE - usar findAllEntity)
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<AgentApproval> result1 = businessService.findAllEntity(
                AgentApproval.class, 
                pageParams, 
                new Criterias()
            );
            if (result1 != null && result1.getContent() != null) {
                approvalsList = result1.getContent();
            }
            
            // Cargar agentes (TABLE - usar findAllEntity)
            PageResult<Agent> result2 = businessService.findAllEntity(
                Agent.class, 
                pageParams, 
                new Criterias()
            );
            if (result2 != null && result2.getContent() != null) {
                agentsList = result2.getContent();
            }
            
            log.info("Cargadas {} aprobaciones de agentes", approvalsList.size());
        } catch (Exception e) {
            log.error("Error al cargar datos de aprobaciones", e);
        }
    }
    
    private void calculateKPIs() {
        totalApprovals = (long) approvalsList.size();
        pendingApprovals = approvalsList.stream()
            .filter(a -> "PENDING".equals(a.getAgtapprovalstatus()))
            .count();
        approvedAgents = approvalsList.stream()
            .filter(a -> "APPROVED".equals(a.getAgtapprovalstatus()))
            .count();
        rejectedAgents = approvalsList.stream()
            .filter(a -> "REJECTED".equals(a.getAgtapprovalstatus()))
            .count();
        
        log.info("KPIs calculados - Total: {}, Pending: {}, Approved: {}, Rejected: {}", 
            totalApprovals, pendingApprovals, approvedAgents, rejectedAgents);
    }
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        loadData();
        calculateKPIs();
        Messagebox.show("Datos actualizados correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
    }
    
    /**
     * Auto-aprobar artifact usando procedure
     */
    @Command
    @NotifyChange("*")
    public void autoApproveAgent(Long agentId) {
        try {
            // TODO: Crear JPA @Entidad(type="PROCEDURE") para sp_auto_approve_artifact
            // TODO: Crear JPA AutoApproveArtifactProcedure para sp_auto_approve_artifact
            // AutoApproveArtifactProcedure proc = new AutoApproveArtifactProcedure();
            // proc.setPArtifactType("AGENT");
            // proc.setPArtifactId(agentId);
            // proc.setPApprovalCriteria("AUTO_APPROVED");
            // businessService.callProcedure(proc);
            
            // TEMPORAL: Usar executeUpdate hasta crear la JPA
            String sql = "CALL sp_auto_approve_artifact(?, ?, ?)";
            List<Object> params = new ArrayList<>();
            params.add("AGENT");
            params.add(agentId);
            params.add("AUTO_APPROVED");
            businessService.executeUpdate(sql, params);
            
            loadData();
            calculateKPIs();
            Messagebox.show("Agente aprobado automáticamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al auto-aprobar agente", e);
            Messagebox.show("Error al aprobar agente: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void approveAgent(Long approvalId, String comments) {
        try {
            AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
            if (approval != null) {
                approval.setAgtapprovalstatus("APPROVED");
                approval.setAgtapprovalnotes(comments);
                businessService.update(approval);
                
                loadData();
                calculateKPIs();
                Messagebox.show("Agente aprobado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error al aprobar agente", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void rejectAgent(Long approvalId, String reason) {
        try {
            AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
            if (approval != null) {
                approval.setAgtapprovalstatus("REJECTED");
                approval.setAgtapprovalnotes(reason);
                businessService.update(approval);
                
                loadData();
                calculateKPIs();
                Messagebox.show("Agente rechazado", "Información", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error al rechazar agente", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Registra la actividad del usuario en el sistema de auditoría
     */
    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    @Destroy
    public void destroy() {
        if (approvalsList != null) { 
            approvalsList.clear(); 
            approvalsList = null; 
        }
        if (agentsList != null) { 
            agentsList.clear(); 
            agentsList = null; 
        }
        businessService = null;
    }
}

