package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.util.Map;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.entity.prompts.PromptApproval;
import com.codeflowx.govern.workflow.services.PromptSafetyService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar análisis de seguridad de prompts
 * Usa BusinessService y JPAs para persistir resultados
 */
@Slf4j
@Component("promptSafetyDelegate")
public class PromptSafetyDelegate implements JavaDelegate {

    @Autowired
    private PromptSafetyService promptSafetyService;

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Long promptId = (Long) execution.getVariable("promptId");
        Long approvalId = (Long) execution.getVariable("approvalId");

        log.info("Ejecutando safety check para prompt: processId={}, promptId={}, approvalId={}",
                 processInstanceId, promptId, approvalId);

        try {
            // 1. Cargar el prompt desde BBDD
            Prompt prompt = businessService.findById(Prompt.class, promptId);
            if (prompt == null) {
                String errorMsg = "Prompt no encontrado con ID: " + promptId;
                log.error(errorMsg);
                execution.setVariable("error", errorMsg);
                execution.setVariable("safetyScore", 0);
                throw new RuntimeException(errorMsg);
            }

            String promptContent = prompt.getPrmcontent();
            if (promptContent == null || promptContent.trim().isEmpty()) {
                String errorMsg = "Prompt sin contenido para analizar";
                log.error(errorMsg);
                execution.setVariable("error", errorMsg);
                execution.setVariable("safetyScore", 0);
                throw new RuntimeException(errorMsg);
            }

            // 2. Ejecutar análisis de seguridad
            Map<String, Object> safetyAnalysis = promptSafetyService.analyzeSafety(promptContent);
            
            // 3. Extraer resultados
            Integer safetyScore = (Integer) safetyAnalysis.get("safetyScore");
            Boolean jailbreakDetected = (Boolean) safetyAnalysis.get("jailbreakDetected");
            Boolean injectionDetected = (Boolean) safetyAnalysis.get("injectionDetected");
            Boolean maliciousContentDetected = (Boolean) safetyAnalysis.get("maliciousContentDetected");
            String recommendation = (String) safetyAnalysis.get("recommendation");

            // 4. Guardar resultado en PromptApproval (BBDD)
            if (approvalId != null) {
                PromptApproval approval = businessService.findById(PromptApproval.class, approvalId);
                if (approval != null) {
                    String safetyJson = promptSafetyService.toJsonString(safetyAnalysis);
                    approval.setPrmsafetycheck(safetyJson);
                    approval.setPrmupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                    log.info("Resultado de safety check guardado en PromptApproval ID: {}", approvalId);
                }
            }

            // 5. Guardar variables en el proceso BPMN
            execution.setVariable("safetyCheckResult", promptSafetyService.toJsonString(safetyAnalysis));
            execution.setVariable("safetyScore", safetyScore != null ? safetyScore : 0);
            execution.setVariable("jailbreakDetected", Boolean.TRUE.equals(jailbreakDetected));
            execution.setVariable("injectionDetected", Boolean.TRUE.equals(injectionDetected));
            execution.setVariable("maliciousContentDetected", Boolean.TRUE.equals(maliciousContentDetected));
            execution.setVariable("safetyRecommendation", recommendation);

            log.info("Safety check completado exitosamente - Score: {}, Recommendation: {}", 
                     safetyScore, recommendation);

            // 6. Log de riesgos detectados
            if (safetyAnalysis.containsKey("risks")) {
                log.warn("Riesgos de seguridad detectados: {}", safetyAnalysis.get("risks"));
            }

        } catch (DaoException e) {
            log.error("Error de BBDD en safety check", e);
            execution.setVariable("error", e.getMessage());
            execution.setVariable("safetyScore", 0);
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error ejecutando safety check", e);
            execution.setVariable("error", e.getMessage());
            execution.setVariable("safetyScore", 0);
            throw new RuntimeException("Error en safety check: " + e.getMessage(), e);
        }
    }
}


