package com.codeflowx.govern.workflow.delegates;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.workflow.services.ComplianceMonitoringService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar compliance checks programados
 * Usa BusinessService y JPAs para persistir resultados
 */
@Slf4j
@Component("scheduledComplianceDelegate")
public class ScheduledComplianceDelegate implements JavaDelegate {

    @Autowired
    private ComplianceMonitoringService complianceMonitoringService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        String checkType = execution.getVariable("checkType") != null ? 
                          (String) execution.getVariable("checkType") : "SCHEDULED";

        log.info("Ejecutando compliance check: processId={}, checkType={}", processInstanceId, checkType);

        try {
            // 1. Ejecutar compliance check en todos los sistemas
            Map<String, Object> checkResult = complianceMonitoringService.executeScheduledCompliance();
            
            // 2. Extraer resultados
            Integer totalChecked = (Integer) checkResult.get("totalSystemsChecked");
            Integer compliant = (Integer) checkResult.get("compliantSystems");
            Integer nonCompliant = (Integer) checkResult.get("nonCompliantSystems");
            Integer criticalIssues = (Integer) checkResult.get("criticalIssues");
            Double complianceRate = (Double) checkResult.get("complianceRate");

            // 3. Guardar variables en el proceso BPMN
            execution.setVariable("totalSystemsChecked", totalChecked != null ? totalChecked : 0);
            execution.setVariable("compliantSystems", compliant != null ? compliant : 0);
            execution.setVariable("nonCompliantSystems", nonCompliant != null ? nonCompliant : 0);
            execution.setVariable("criticalIssues", criticalIssues != null ? criticalIssues : 0);
            execution.setVariable("complianceRate", complianceRate != null ? complianceRate : 0.0);
            execution.setVariable("complianceReportJson", objectMapper.writeValueAsString(checkResult));
            execution.setVariable("checkTimestamp", System.currentTimeMillis());

            log.info("Compliance check completado - Total: {}, Compliant: {}, Non-Compliant: {}, Critical: {}",
                     totalChecked, compliant, nonCompliant, criticalIssues);

        } catch (Exception e) {
            log.error("Error ejecutando compliance check programado", e);
            execution.setVariable("error", e.getMessage());
            execution.setVariable("nonCompliantSystems", 0);
            throw new RuntimeException("Error en compliance check: " + e.getMessage(), e);
        }
    }
}


