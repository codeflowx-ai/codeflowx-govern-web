package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.entity.rag.RagEvaluation;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("storeRagEvaluationDelegate")
public class StoreRagEvaluationDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long ragId = (Long) execution.getVariable("ragId");
        Double overallScore = (Double) execution.getVariable("overallScore");
        String evaluationResultJson = (String) execution.getVariable("evaluationResultJson");

        log.info("Almacenando resultados de evaluación RAG ID: {}", ragId);

        try {
            RagEvaluation evaluation = new RagEvaluation();
            
            // Configurar campos obligatorios
            evaluation.setRagname("RAG Evaluation - " + ragId);
            evaluation.setRagstatus("COMPLETED");
            evaluation.setRagtestqueries(evaluationResultJson != null ? evaluationResultJson : "{}");
            evaluation.setRagcreatedby("SYSTEM_EVALUATION");
            evaluation.setRagcreatedat(new Timestamp(System.currentTimeMillis()));
            evaluation.setRagupdatedat(new Timestamp(System.currentTimeMillis()));
            evaluation.setRagcompletedat(new Timestamp(System.currentTimeMillis()));
            
            // Asociar al RAG system si existe
            if (ragId != null) {
                RagSystem ragSystem = businessService.findById(RagSystem.class, ragId);
                if (ragSystem != null) {
                    evaluation.setRagSystem(ragSystem);
                }
            }
            
            businessService.save(evaluation);
            log.info("RagEvaluation creado con ID: {}", evaluation.getIdxragevaluation());

            RagSystem rag = businessService.findById(RagSystem.class, ragId);
            if (rag != null) {
                rag.setRagupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.save(rag);
            }

            execution.setVariable("evaluationId", evaluation.getIdxragevaluation());

        } catch (DaoException e) {
            log.error("Error de BBDD", e);
            throw new RuntimeException("Error BBDD: " + e.getMessage(), e);
        }
    }
}


