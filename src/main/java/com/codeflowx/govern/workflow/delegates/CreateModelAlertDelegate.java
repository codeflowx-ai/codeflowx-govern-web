package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.AgentAlert;
import com.codeflowx.govern.entity.models.Model;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para crear alerta cuando un modelo está degradado
 * Usa BusinessService y JPAs
 */
@Slf4j
@Component("createModelAlertDelegate")
public class CreateModelAlertDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long modelId = (Long) execution.getVariable("modelId");
        Double performanceScore = (Double) execution.getVariable("performanceScore");
        Boolean driftDetected = (Boolean) execution.getVariable("driftDetected");
        Double threshold = (Double) execution.getVariable("threshold");

        log.info("Creando alerta para modelo degradado ID: {}, score: {}, threshold: {}",
                 modelId, performanceScore, threshold);

        try {
            // 1. Obtener información del modelo
            Model model = businessService.findById(Model.class, modelId);
            String modelName = model != null ? model.getModname() : "Unknown Model";

            // 2. Crear alerta
            AgentAlert alert = new AgentAlert();
            alert.setAgtalerttype("MODEL_DEGRADATION");
            alert.setAgtalertcategory("PERFORMANCE");
            alert.setAgtseverity(performanceScore != null && performanceScore < 70 ? "CRITICAL" : "HIGH");
            alert.setAgtstatus("OPEN");
            alert.setAgttitle("Model Performance Degradation: " + modelName);
            
            StringBuilder description = new StringBuilder();
            description.append("Model ID ").append(modelId).append(" (").append(modelName).append(") ");
            description.append("has performance score of ").append(performanceScore);
            description.append(", below threshold of ").append(threshold).append(". ");
            if (Boolean.TRUE.equals(driftDetected)) {
                description.append("DRIFT DETECTED. ");
            }
            description.append("Action required: Review model and consider retraining.");
            
            alert.setAgtdescription(description.toString());
            alert.setAgttriggeredat(new Timestamp(System.currentTimeMillis()));
            alert.setAgtcreatedby("SYSTEM_MODEL_EVALUATION");
            alert.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));

            businessService.save(alert);
            
            log.info("Alerta de degradación creada con ID: {}", alert.getIdxagentalert());

            // 3. Guardar en variables del proceso
            execution.setVariable("alertCreated", true);
            execution.setVariable("alertId", alert.getIdxagentalert());

            // TODO: Enviar notificación por email/Slack a ML engineers

        } catch (DaoException e) {
            log.error("Error de BBDD creando alerta de modelo", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error creando alerta de modelo", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error creando alerta: " + e.getMessage(), e);
        }
    }
}


