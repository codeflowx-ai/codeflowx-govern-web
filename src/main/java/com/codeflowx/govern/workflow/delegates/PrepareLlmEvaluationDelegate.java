package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.time.Instant;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.evaluation.LlmEvaluation;
import com.codeflowx.govern.entity.models.Model;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("prepareLlmEvaluationDelegate")
public class PrepareLlmEvaluationDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long modelId = (Long) execution.getVariable("model_id");
        
        log.info("🔄 Preparando evaluación LLM para modelo: {}", modelId);

        try {
            // 1. Verificar que modelo existe
            Model model = businessService.findById(Model.class, modelId);
            
            if (model == null) {
                throw new RuntimeException("Modelo no encontrado: " + modelId);
            }

            // 2. Crear registro inicial en BBDD
            LlmEvaluation evaluation = new LlmEvaluation();
            evaluation.setEvalname("LLM Evaluation - " + model.getModname());
            evaluation.setEvalstatus("PENDING");
            evaluation.setModel(model);
            evaluation.setEvalcreatedby((String) execution.getVariable("requester_user_id"));
            evaluation.setEvalcreatedat(Timestamp.from(Instant.now()));
            
            businessService.save(evaluation);

            // 3. Guardar variables en proceso
            execution.setVariable("evaluation_id", evaluation.getIdxllmevaluation());
            execution.setVariable("model_name", model.getModname());
            
            log.info("✅ Evaluación LLM preparada: evaluationId={}", evaluation.getIdxllmevaluation());

        } catch (DaoException e) {
            log.error("❌ Error preparando evaluación", e);
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}


