package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.AgentAlert;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("createRagAlertDelegate")
public class CreateRagAlertDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long ragId = (Long) execution.getVariable("ragId");
        Double overallScore = (Double) execution.getVariable("overallScore");

        log.info("Creando alerta para RAG degradado ID: {}", ragId);

        try {
            AgentAlert alert = new AgentAlert();
            alert.setAgtalerttype("RAG_DEGRADATION");
            alert.setAgtalertcategory("PERFORMANCE");
            alert.setAgtseverity("HIGH");
            alert.setAgtstatus("OPEN");
            alert.setAgttitle("RAG Performance Degradation ID: " + ragId);
            alert.setAgtdescription("RAG ID " + ragId + " has overall score of " + overallScore + ". Review required.");
            alert.setAgttriggeredat(new Timestamp(System.currentTimeMillis()));
            alert.setAgtcreatedby("SYSTEM_RAG_EVALUATION");
            alert.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));

            businessService.save(alert);
            log.info("Alerta RAG creada con ID: {}", alert.getIdxagentalert());

            execution.setVariable("alertCreated", true);
            execution.setVariable("alertId", alert.getIdxagentalert());

        } catch (Exception e) {
            log.error("Error creando alerta RAG", e);
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}


