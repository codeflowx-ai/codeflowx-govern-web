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
 * Delegate para marcar un modelo como Production-Ready usando BusinessService y JPAs
 */
@Slf4j
@Component("markModelProductionDelegate")
public class MarkModelProductionDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Long modelId = (Long) execution.getVariable("modelId");
        Long versionId = (Long) execution.getVariable("versionId");

        log.info("Marcando modelo como Production-Ready: processId={}, modelId={}, versionId={}",
                 processInstanceId, modelId, versionId);

        try {
            // 1. Actualizar estado del modelo a PRODUCTION_READY
            Model model = businessService.findById(Model.class, modelId);
            if (model != null) {
                model.setModstatus("PRODUCTION_READY");
                model.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.save(model);
            }

            // 2. Buscar y actualizar registro de aprobación
            Long approvalId = (Long) execution.getVariable("approvalId");
            if (approvalId != null) {
                ModelApproval approval = businessService.findById(ModelApproval.class, approvalId);
                if (approval != null) {
                    approval.setModapprovalstatus("APPROVED");
                    approval.setModapprovedat(new Timestamp(System.currentTimeMillis()));
                    approval.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 3. Guardar en variables del proceso
            execution.setVariable("modelStatus", "PRODUCTION_READY");
            execution.setVariable("approvalStatus", "APPROVED");
            execution.setVariable("approvedAt", System.currentTimeMillis());

            // 4. Notificar aprobación
            notificationService.notifyApproval("MODEL", modelId, processInstanceId);

            log.info("Modelo marcado como Production-Ready exitosamente");

        } catch (DaoException e) {
            log.error("Error de BBDD marcando modelo como Production-Ready", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error marcando modelo como Production-Ready", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error marcando modelo: " + e.getMessage(), e);
        }
    }
}

