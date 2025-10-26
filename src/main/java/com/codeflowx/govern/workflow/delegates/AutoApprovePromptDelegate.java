package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.entity.prompts.PromptApproval;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para auto-aprobar prompts de bajo riesgo
 * Usa BusinessService y JPAs para actualizar estados
 */
@Slf4j
@Component("autoApprovePromptDelegate")
public class AutoApprovePromptDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Long promptId = (Long) execution.getVariable("promptId");
        Long approvalId = (Long) execution.getVariable("approvalId");
        Integer safetyScore = (Integer) execution.getVariable("safetyScore");
        Integer complianceScore = (Integer) execution.getVariable("complianceScore");

        log.info("Auto-aprobando prompt: processId={}, promptId={}, safetyScore={}, complianceScore={}",
                 processInstanceId, promptId, safetyScore, complianceScore);

        try {
            // 1. Actualizar estado del prompt a APPROVED
            Prompt prompt = businessService.findById(Prompt.class, promptId);
            if (prompt != null) {
                prompt.setPrmapprovalstatus("APPROVED");
                prompt.setPrmapprovedby("SYSTEM_AUTO_APPROVE");
                prompt.setPrmapprovedat(new Timestamp(System.currentTimeMillis()));
                prompt.setPrmupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.save(prompt);
                log.info("Prompt ID {} actualizado a APPROVED", promptId);
            }

            // 2. Actualizar registro de aprobación
            if (approvalId != null) {
                PromptApproval approval = businessService.findById(PromptApproval.class, approvalId);
                if (approval != null) {
                    approval.setPrmapprovalstatus("APPROVED");
                    approval.setPrmapproverid("SYSTEM");
                    approval.setPrmapprovername("Auto-Approval (Low Risk)");
                    approval.setPrmapprovedat(new Timestamp(System.currentTimeMillis()));
                    approval.setPrmapprovalnotes("Auto-approved based on high safety score (" + safetyScore + 
                                                  ") and perfect compliance score (" + complianceScore + ")");
                    approval.setPrmupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                    log.info("PromptApproval ID {} actualizado a APPROVED", approvalId);
                }
            }

            // 3. Guardar variables en el proceso
            execution.setVariable("approved", true);
            execution.setVariable("approvalStatus", "APPROVED");
            execution.setVariable("approverId", "SYSTEM");
            execution.setVariable("approvedAt", System.currentTimeMillis());
            execution.setVariable("approvalMethod", "AUTO_APPROVE");

            log.info("Prompt auto-aprobado exitosamente");

        } catch (DaoException e) {
            log.error("Error de BBDD auto-aprobando prompt", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error auto-aprobando prompt", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error auto-aprobando: " + e.getMessage(), e);
        }
    }
}


