package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.workflow.services.ComplianceCheckService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar Compliance Check usando RAG + LLM + BusinessService
 */
@Slf4j
@Component("complianceCheckDelegate")
public class ComplianceCheckDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ComplianceCheckService complianceCheckService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        String entityType = (String) execution.getVariable("entityType");
        Long entityId = null;

        log.info("Ejecutando ComplianceCheckDelegate: processId={}, entityType={}",
                 processInstanceId, entityType);

        try {
            ComplianceCheckService.ComplianceCheckResult result;

            // 1. Ejecutar compliance check según tipo de entidad
            if ("AGENT".equals(entityType)) {
                entityId = (Long) execution.getVariable("agentId");
                result = complianceCheckService.checkAgentCompliance(entityId);
            } else if ("MODEL".equals(entityType)) {
                entityId = (Long) execution.getVariable("modelId");
                result = complianceCheckService.checkModelCompliance(entityId);
            } else {
                throw new IllegalArgumentException("Entity type no soportado: " + entityType);
            }

            // 2. Actualizar JPA correspondiente
            Long approvalId = (Long) execution.getVariable("approvalId");
            if (approvalId != null) {
                if ("AGENT".equals(entityType)) {
                    AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
                    if (approval != null) {
                        approval.setAgtcompliancecheck(result.toJson());
                        approval.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                        businessService.save(approval);
                    }
                } else if ("MODEL".equals(entityType)) {
                    ModelApproval approval = businessService.findById(ModelApproval.class, approvalId);
                    if (approval != null) {
                        approval.setModcompliancecheck(result.toJson());
                        approval.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                        businessService.save(approval);
                    }
                }
            }

            // 3. Guardar resultados en proceso
            execution.setVariable("complianceCheckResult", result.toJson());
            execution.setVariable("complianceStatus", result.getStatus());
            execution.setVariable("complianceViolations", result.getViolations().size());

            log.info("ComplianceCheckDelegate completado: status={}, violations={}",
                     result.getStatus(), result.getViolations().size());

        } catch (DaoException e) {
            log.error("Error de BBDD en ComplianceCheckDelegate", e);
            execution.setVariable("complianceCheckError", e.getMessage());
            execution.setVariable("complianceStatus", "ERROR");
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error en ComplianceCheckDelegate", e);
            execution.setVariable("complianceCheckError", e.getMessage());
            execution.setVariable("complianceStatus", "ERROR");
            throw new RuntimeException("Error en compliance check: " + e.getMessage(), e);
        }
    }
}

