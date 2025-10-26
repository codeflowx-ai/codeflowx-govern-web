package com.codeflowx.govern.workflow.delegates;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.workflow.services.NotificationService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para enviar notificaciones usando BusinessService y JPAs
 */
@Slf4j
@Component("notifyApprovalDelegate")
public class NotifyApprovalDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        String entityType = (String) execution.getVariable("entityType");
        Long entityId = (Long) execution.getVariable("entityId");

        log.info("Notificando aprobación: processId={}, entityType={}, entityId={}",
                 processInstanceId, entityType, entityId);

        try {
            // 1. Obtener información de la entidad aprobada para notificación completa
            String entityName = null;
            
            if ("AGENT".equals(entityType)) {
                Long agentId = (Long) execution.getVariable("agentId");
                Agent agent = businessService.findById(Agent.class, agentId);
                if (agent != null) {
                    entityName = agent.getAgtname();
                }
            } else if ("MODEL".equals(entityType)) {
                Long modelId = (Long) execution.getVariable("modelId");
                Model model = businessService.findById(Model.class, modelId);
                if (model != null) {
                    entityName = model.getModname();
                }
            }

            // 2. Enviar notificación
            notificationService.notifyApproval(entityType, entityId, processInstanceId);
            
            // 3. Guardar en variables del proceso
            execution.setVariable("notificationSent", true);
            execution.setVariable("notificationTimestamp", System.currentTimeMillis());
            
            log.info("Notificación enviada exitosamente para: {} ({})", entityName, entityType);

        } catch (DaoException e) {
            log.error("Error de BBDD en NotifyApprovalDelegate", e);
            execution.setVariable("notificationError", e.getMessage());
            execution.setVariable("notificationSent", false);
        } catch (Exception e) {
            log.error("Error enviando notificación", e);
            execution.setVariable("notificationError", e.getMessage());
            execution.setVariable("notificationSent", false);
        }
    }
}

