package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.util.Map;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.workflow.services.ModelValidationService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar Model Validation usando BusinessService y JPAs
 */
@Slf4j
@Component("modelValidationDelegate")
public class ModelValidationDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ModelValidationService validationService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Long modelId = (Long) execution.getVariable("modelId");
        Long versionId = (Long) execution.getVariable("versionId");

        log.info("Ejecutando ModelValidationDelegate: modelId={}, versionId={}", modelId, versionId);

        try {
            // 1. Ejecutar validación de performance
            @SuppressWarnings("unchecked")
            Map<String, Double> thresholds = (Map<String, Double>) execution.getVariable("performanceThresholds");

            ModelValidationService.ValidationResult result = 
                    validationService.validatePerformance(modelId, versionId, thresholds);

            // 2. Actualizar JPA ModelApproval
            Long approvalId = (Long) execution.getVariable("approvalId");
            if (approvalId != null) {
                ModelApproval approval = businessService.findById(ModelApproval.class, approvalId);
                if (approval != null) {
                    approval.setModperformancevalidation(result.toJson());
                    approval.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 3. Guardar en variables del proceso
            execution.setVariable("performanceValidationResult", result.toJson());
            execution.setVariable("meetsPerformanceThresholds", result.getPassed());
            execution.setVariable("performanceMetrics", result.getMetrics());

            log.info("ModelValidationDelegate completado: passed={}", result.getPassed());

        } catch (DaoException e) {
            log.error("Error de BBDD en ModelValidationDelegate", e);
            execution.setVariable("validationError", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error en ModelValidationDelegate", e);
            execution.setVariable("validationError", e.getMessage());
            throw new RuntimeException("Error en model validation: " + e.getMessage(), e);
        }
    }
}

