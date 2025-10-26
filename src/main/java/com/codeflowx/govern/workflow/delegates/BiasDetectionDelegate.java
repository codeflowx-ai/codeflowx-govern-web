package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.workflow.services.BiasDetectionService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar Bias Detection usando BusinessService y JPAs
 */
@Slf4j
@Component("biasDetectionDelegate")
public class BiasDetectionDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private BiasDetectionService biasDetectionService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Long modelId = (Long) execution.getVariable("modelId");

        log.info("Ejecutando BiasDetectionDelegate: processId={}, modelId={}", 
                 processInstanceId, modelId);

        try {
            // 1. Ejecutar bias detection
            BiasDetectionService.BiasDetectionResult result = 
                    biasDetectionService.detectModelBias(modelId);

            // 2. Actualizar JPA ModelApproval
            Long approvalId = (Long) execution.getVariable("approvalId");
            if (approvalId != null) {
                ModelApproval approval = businessService.findById(ModelApproval.class, approvalId);
                if (approval != null) {
                    approval.setModbiasdetection(result.toJson());
                    approval.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 3. Guardar en variables del proceso
            execution.setVariable("biasDetectionResult", result.toJson());
            execution.setVariable("hasBias", result.getHasBias());
            execution.setVariable("biasScore", result.getBiasScore());

            log.info("BiasDetectionDelegate completado: hasBias={}, score={}",
                     result.getHasBias(), result.getBiasScore());

        } catch (DaoException e) {
            log.error("Error de BBDD en BiasDetectionDelegate", e);
            execution.setVariable("biasDetectionError", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error en BiasDetectionDelegate", e);
            execution.setVariable("biasDetectionError", e.getMessage());
            throw new RuntimeException("Error en bias detection: " + e.getMessage(), e);
        }
    }
}

