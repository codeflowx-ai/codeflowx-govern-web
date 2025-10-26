package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.AgentAlert;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para crear alertas de compliance basadas en issues detectados
 * Usa BusinessService y JPAs para crear alertas
 */
@Slf4j
@Component("createComplianceAlertsDelegate")
public class CreateComplianceAlertsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        String complianceReportJson = (String) execution.getVariable("complianceReportJson");
        Integer nonCompliantSystems = (Integer) execution.getVariable("nonCompliantSystems");

        log.info("Creando alertas de compliance: processId={}, nonCompliantSystems={}",
                 processInstanceId, nonCompliantSystems);

        int alertsCreated = 0;
        int criticalAlertsCreated = 0;

        try {
            if (complianceReportJson == null || nonCompliantSystems == null || nonCompliantSystems == 0) {
                log.info("No hay sistemas non-compliant, no se crean alertas");
                execution.setVariable("alertsCreated", 0);
                execution.setVariable("criticalAlertsCreated", 0);
                return;
            }

            // 1. Parsear reporte de compliance
            Map<String, Object> report = objectMapper.readValue(complianceReportJson, Map.class);
            List<Map<String, Object>> systemResults = (List<Map<String, Object>>) report.get("systemResults");

            // 2. Crear alerta por cada sistema non-compliant
            if (systemResults != null) {
                for (Map<String, Object> systemResult : systemResults) {
                    Boolean compliant = (Boolean) systemResult.get("compliant");
                    if (Boolean.FALSE.equals(compliant)) {
                        String entityType = (String) systemResult.get("entityType");
                        Long entityId = Long.valueOf(systemResult.get("entityId").toString());
                        Integer criticalIssuesCount = (Integer) systemResult.get("criticalIssuesCount");
                        
                        // Crear alerta (usando AgentAlert como base, adaptar según necesidad)
                        AgentAlert alert = new AgentAlert();
                        alert.setAgtalerttype("COMPLIANCE_ISSUE");
                        alert.setAgtalertcategory("COMPLIANCE");
                        alert.setAgtseverity(criticalIssuesCount != null && criticalIssuesCount > 0 ? "CRITICAL" : "HIGH");
                        alert.setAgtstatus("OPEN");
                        alert.setAgttitle("Compliance Issue Detected: " + entityType + " ID " + entityId);
                        alert.setAgtdescription("Compliance check detected issues in " + entityType + " ID " + entityId + 
                                               ". Critical issues: " + (criticalIssuesCount != null ? criticalIssuesCount : 0));
                        alert.setAgttriggeredat(new Timestamp(System.currentTimeMillis()));
                        alert.setAgtcreatedby("SYSTEM_COMPLIANCE_MONITOR");
                        alert.setAgtcreatedat(new Timestamp(System.currentTimeMillis()));
                        
                        businessService.save(alert);
                        alertsCreated++;
                        
                        if (criticalIssuesCount != null && criticalIssuesCount > 0) {
                            criticalAlertsCreated++;
                        }
                        
                        log.info("Alerta creada para {} ID: {}, Severity: {}", 
                                 entityType, entityId, alert.getAgtseverity());
                    }
                }
            }

            // 3. Guardar variables en el proceso
            execution.setVariable("alertsCreated", alertsCreated);
            execution.setVariable("criticalAlertsCreated", criticalAlertsCreated);

            log.info("Alertas de compliance creadas - Total: {}, Critical: {}", alertsCreated, criticalAlertsCreated);

        } catch (DaoException e) {
            log.error("Error de BBDD creando alertas de compliance", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error creando alertas de compliance", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error creando alertas: " + e.getMessage(), e);
        }
    }
}


