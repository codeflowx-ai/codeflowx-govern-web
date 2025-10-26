package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.workflow.services.NotificationService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para rechazar un modelo usando BusinessService y JPAs
 */
@Slf4j
@Component("rejectModelDelegate")
public class RejectModelDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Long modelId = (Long) execution.getVariable("modelId");
        String rejectionReason = (String) execution.getVariable("rejectionReason");

        log.info("Rechazando modelo: processId={}, modelId={}, reason={}",
                 processInstanceId, modelId, rejectionReason);

        try {
            // 1. Actualizar estado del modelo a REJECTED
            Model model = businessService.findById(Model.class, modelId);
            if (model != null) {
                model.setModstatus("REJECTED");
                model.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.save(model);
            }

            // 2. Actualizar registro de aprobación
            Long approvalId = (Long) execution.getVariable("approvalId");
            if (approvalId != null) {
                ModelApproval approval = businessService.findById(ModelApproval.class, approvalId);
                if (approval != null) {
                    approval.setModapprovalstatus("REJECTED");
                    approval.setModrejectionreason(rejectionReason);
                    approval.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 3. Guardar en variables del proceso
            execution.setVariable("modelStatus", "REJECTED");
            execution.setVariable("approvalStatus", "REJECTED");
            execution.setVariable("rejectedAt", System.currentTimeMillis());

            // 4. Notificar rechazo
            notificationService.notifyRejection("MODEL", modelId, rejectionReason);

            log.info("Modelo rechazado exitosamente");

        } catch (DaoException e) {
            log.error("Error de BBDD rechazando modelo", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error rechazando modelo", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error rechazando modelo: " + e.getMessage(), e);
        }
    }
}

