package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para programar revisión de issues no críticos de compliance
 */
@Slf4j
@Component("scheduleReviewDelegate")
public class ScheduleReviewDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Integer nonCompliantSystems = (Integer) execution.getVariable("nonCompliantSystems");

        log.info("Programando revisión de compliance: processId={}, nonCompliantSystems={}",
                 processInstanceId, nonCompliantSystems);

        try {
            // Calcular fecha de revisión (7 días desde ahora)
            LocalDateTime reviewDate = LocalDateTime.now().plusDays(7);
            String reviewDateFormatted = reviewDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Guardar en variables del proceso (para el Timer Boundary Event)
            execution.setVariable("reviewScheduledFor", reviewDateFormatted);
            execution.setVariable("reviewScheduled", true);
            execution.setVariable("reviewDaysDelay", 7);

            log.info("✅ Revisión programada para: {}", reviewDateFormatted);
            log.info("   El Timer Boundary Event esperará 7 días antes de disparar User Task");

        } catch (Exception e) {
            log.error("❌ Error programando revisión", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error programando revisión: " + e.getMessage(), e);
        }
    }
}


