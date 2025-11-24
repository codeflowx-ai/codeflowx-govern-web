package com.codeflowx.govern.viewmodel.agents;

import java.util.ArrayList;
import java.util.List;

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
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.service.agents.AgentService;
import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.service.agents.AgentApprovalService;

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
public class AgentApprovalWorkflowViewModel extends BaseFront<AgentApprovalWorkflowViewModel> {

    private static final long serialVersionUID = 1L;

    
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
            
            PageResult<AgentApproval> result1 = agentApprovalService.findAll(pageParams, new Criterias()
            );
            if (result1 != null && result1.getContent() != null) {
                approvalsList = result1.getContent();
            }
            
            // Cargar agentes (TABLE - usar findAllEntity)
            PageResult<Agent> result2 = agentService.findAll(pageParams, new Criterias()
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
            AgentApproval approval = agentApprovalService.findById(approvalId);
            if (approval != null) {
                approval.setAgtapprovalstatus("APPROVED");
                approval.setAgtapprovalnotes(comments);
                approval = agentService.update(approval);
                
                // Registrar actividad en auditoría
                logActivity("APPROVE", "AGENT_APPROVAL", approvalId, 
                    "Agente aprobado: " + (comments != null ? comments : "Sin comentarios"));
                
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
            AgentApproval approval = agentApprovalService.findById(approvalId);
            if (approval != null) {
                approval.setAgtapprovalstatus("REJECTED");
                approval.setAgtapprovalnotes(reason);
                approval = agentService.update(approval);
                
                // Registrar actividad en auditoría
                logActivity("REJECT", "AGENT_APPROVAL", approvalId, 
                    "Agente rechazado: " + (reason != null ? reason : "Sin razón especificada"));
                
                loadData();
                calculateKPIs();
                Messagebox.show("Agente rechazado", "Información", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error al rechazar agente", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
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
        agentService = null;
            agentApprovalService = null;
    }
}

