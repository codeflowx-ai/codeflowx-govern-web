package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.evaluation.LlmEvaluation;
import com.codeflowx.govern.entity.models.Model;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para almacenar resultados de evaluación de modelo
 * Usa BusinessService y JPAs
 */
@Slf4j
@Component("storeEvaluationDelegate")
public class StoreEvaluationDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long modelId = (Long) execution.getVariable("modelId");
        String evaluationType = (String) execution.getVariable("evaluationType");
        Double performanceScore = (Double) execution.getVariable("performanceScore");
        Double accuracyScore = (Double) execution.getVariable("accuracyScore");
        Double f1Score = (Double) execution.getVariable("f1Score");
        Boolean driftDetected = (Boolean) execution.getVariable("driftDetected");
        String evaluationResultJson = (String) execution.getVariable("evaluationResultJson");

        log.info("Almacenando resultados de evaluación para modelo ID: {}", modelId);

        try {
            // 1. Crear registro de LlmEvaluation
            LlmEvaluation evaluation = new LlmEvaluation();
            // Configurar campos básicos
            evaluation.setEvalcreatedby("SYSTEM_EVALUATION");
            evaluation.setEvalcreatedat(new Timestamp(System.currentTimeMillis()));
            evaluation.setEvalupdatedat(new Timestamp(System.currentTimeMillis()));
            evaluation.setEvalstatus("COMPLETED");
            evaluation.setEvaldescription(String.format("Evaluación automática - Type: %s, Score: %.2f", 
                evaluationType, performanceScore != null ? performanceScore : 0.0));
            
            businessService.save(evaluation);
            Long evaluationId = evaluation.getIdxllmevaluation();
            
            log.info("LlmEvaluation creado con ID: {}", evaluationId);

            // 2. Actualizar modelo con último score
            Model model = businessService.findById(Model.class, modelId);
            if (model != null) {
                // TODO: Actualizar model.modlastevaluationscore si existe el campo
                model.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.save(model);
            }

            // 3. Guardar ID de evaluación en variables del proceso
            execution.setVariable("evaluationId", evaluationId);

            log.info("Resultados almacenados exitosamente");

        } catch (DaoException e) {
            log.error("Error de BBDD almacenando resultados", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error almacenando resultados", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error almacenando resultados: " + e.getMessage(), e);
        }
    }
}


