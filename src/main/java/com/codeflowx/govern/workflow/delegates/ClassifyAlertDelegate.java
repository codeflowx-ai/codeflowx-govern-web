package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.workflow.drools.facts.AlertClassificationFact;
import com.codeflowx.govern.workflow.services.DroolsRulesService;

import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Classify Alert
 * Clasifica alerta usando Drools para determinar severidad y acción
 * Proceso: 10_ALERT_RESPONSE
 * Usa: AlertClassificationFact + alert-escalation.drl
 */
@Slf4j
@Component("classifyAlertDelegate")
public class ClassifyAlertDelegate implements JavaDelegate {

    @Autowired
    private DroolsRulesService droolsService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🎯 Classifying Alert with Drools");
            
            // 1. Crear Fact para Drools
            AlertClassificationFact fact = new AlertClassificationFact();
            fact.setAlertType((String) execution.getVariable("alertType"));
            fact.setAlertCategory((String) execution.getVariable("alertCategory"));
            fact.setDescription((String) execution.getVariable("alertMessage"));
            fact.setMetricValue((Double) execution.getVariable("metricValue"));
            fact.setMetricThreshold((Double) execution.getVariable("metricThreshold"));
            fact.setModelName((String) execution.getVariable("modelName"));
            fact.setIsProduction((Boolean) execution.getVariable("isProduction"));
            
            // 2. Ejecutar Drools Rules Engine
            log.info("🔧 Executing Drools Rules Engine...");
            droolsService.executeAlertClassificationRules(fact);
            
            // 3. Obtener resultados de Drools
            String severity = fact.getSeverity();
            String priority = fact.getPriority();
            String assignedTeam = fact.getAssignedTeam();
            Boolean requiresImmediateAction = fact.getRequiresImmediateAction();
            
            log.info("✅ Alert classified:");
            log.info("   Severity: {} | Priority: {}", severity, priority);
            log.info("   Assigned Team: {}", assignedTeam);
            log.info("   Immediate Action: {}", requiresImmediateAction);
            
            // 4. Guardar resultados en proceso
            execution.setVariable("severity", severity);
            execution.setVariable("priority", priority);
            execution.setVariable("assignedTeam", assignedTeam);
            execution.setVariable("requiresImmediateAction", requiresImmediateAction);
            
        } catch (Exception e) {
            log.error("❌ Error classifying alert: {}", e.getMessage(), e);
            // Default conservador
            execution.setVariable("severity", "HIGH");
            execution.setVariable("requiresImmediateAction", true);
            throw new RuntimeException("Alert classification failed: " + e.getMessage(), e);
        }
    }
}
