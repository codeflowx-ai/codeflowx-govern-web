package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.AgentAlert;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Create Drift Alert
 * Crea alerta cuando se detecta drift en modelo
 * Proceso: 05_DRIFT_DETECTION
 * JPA: AgentAlert (prefijo AGT)
 */
@Slf4j
@Component("createDriftAlertDelegate")
public class CreateDriftAlertDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🚨 Creating Drift Alert");
            
            Long modelId = (Long) execution.getVariable("modelId");
            String driftType = (String) execution.getVariable("driftType");
            Double driftScore = (Double) execution.getVariable("driftScore");
            String severity = driftScore != null && driftScore > 0.5 ? "HIGH" : (driftScore > 0.25 ? "MEDIUM" : "LOW");
            
            // Crear alerta (usando propiedades CORRECTAS con prefijo AGT)
            AgentAlert alert = new AgentAlert();
            alert.setAgtalerttype("DRIFT_DETECTED");  // ✅ CORRECTO (no aaalerttype)
            alert.setAgtseverity(severity);  // ✅ CORRECTO (no aaseverity)
            alert.setAgtstatus("OPEN");  // ✅ CORRECTO (no aastatus)
            // Nota: AgentAlert no tiene campo para modelid directamente,
            // se puede usar el campo de mensaje o metadata
            alert.setAgtmessage(String.format("Drift detected: %s (score: %.2f) for model ID: %d", 
                                driftType, driftScore, modelId));
            alert.setAgttriggeredat(new Timestamp(System.currentTimeMillis()));
            
            businessService.save(alert);
            execution.setVariable("alertId", alert.getIdxagentalert());
            
            log.info("✅ Drift alert created. ID: {} | Severity: {}", alert.getIdxagentalert(), severity);
            
        } catch (Exception e) {
            log.error("❌ Error creating drift alert: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create alert: " + e.getMessage(), e);
        }
    }
}
