package com.codeflowx.govern.workflow.delegates;

import codeflowx.nocode.persist.BusinessService;
import com.codeflowx.govern.entity.models.ModelApproval;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;

/**
 * Delegate: Mark Model Conditional Approval
 * 
 * Proceso: model-approval-v1
 * Tarea: conditionalTask
 * 
 * Funcionalidad:
 * - Marca modelo como "CONDITIONAL_APPROVAL" en BD
 * - Aprobado PERO requiere monitorización adicional
 * - Se usa cuando scores son borderline o hay concerns menores
 * 
 * Input Variables:
 * - modelId (Long)
 * - finalDecision (String) = 'CONDITIONAL_APPROVAL'
 * - justification (String)
 * - requiresMonitoring (Boolean) = true
 * - minScore (Integer)
 * 
 * Output:
 * - Actualiza ModelApproval en BD
 */
@Slf4j
@Component("markModelConditionalDelegate")
public class MarkModelConditionalDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        log.info("🔄 Ejecutando MarkModelConditionalDelegate");

        try {
            // Obtener variables
            Long modelId = (Long) execution.getVariable("modelId");
            String justification = (String) execution.getVariable("justification");
            Integer minScore = (Integer) execution.getVariable("minScore");
            Boolean requiresMonitoring = (Boolean) execution.getVariable("requiresMonitoring");

            if (modelId == null) {
                throw new IllegalArgumentException("modelId es requerido");
            }

            log.info("📋 Marcando Model ID {} como CONDITIONAL_APPROVAL", modelId);

            // Buscar o crear ModelApproval
            ModelApproval approval = businessService.findById(ModelApproval.class, modelId);
            if (approval == null) {
                approval = new ModelApproval();
                approval.setIdxmodelapproval(modelId);
            }

            // Actualizar aprobación condicional
            approval.setModapprovalstatus("CONDITIONAL");
            approval.setModapprovedat(new Timestamp(System.currentTimeMillis()));
            approval.setModapprovalnotes(justification);
            
            // Guardar detalles adicionales en JSONB
            String details = String.format(
                "{\"minScore\": %d, \"requiresMonitoring\": %b, \"decision\": \"CONDITIONAL_APPROVAL\"}",
                minScore != null ? minScore : 0,
                requiresMonitoring != null ? requiresMonitoring : true
            );
            approval.setModgovernancereview(details);
            
            // Guardar
            businessService.save(approval);

            log.info("✅ Model {} marcado como CONDITIONAL_APPROVAL (monitorización requerida)", modelId);
            log.info("   Min Score: {}/100", minScore);
            log.info("   Requires Monitoring: {}", requiresMonitoring);

        } catch (Exception e) {
            log.error("❌ Error en MarkModelConditionalDelegate: {}", e.getMessage(), e);
            throw new RuntimeException("Error marcando aprobación condicional: " + e.getMessage(), e);
        }
    }
}

