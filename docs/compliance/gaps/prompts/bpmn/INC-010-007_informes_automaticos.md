# PROMPT: INC-010-007 - Implementación de Informes Automáticos

**Incidencia:** INC-010-007  
**Prioridad:** 🟡 ALTA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 2 días  
**Tipo:** BPMN + Java  
**Referencia:** GAP-017

---

## CONTEXTO

No hay generación automática de informes diarios/mensuales. Solo está documentado pero no implementado. Se requiere implementar timers BPMN para generación automática de informes según Art. 72.

**Estado Actual:**
- ✅ Servicio `PostMarketSurveillanceReportService` implementado (INC-010-002)
- ✅ Método `generateReport()` disponible
- ❌ No hay timer BPMN para generación automática
- ❌ No hay notificación automática a stakeholders

---

## REQUISITOS

1. Implementar timer BPMN para generación diaria
2. Implementar timer BPMN para generación mensual
3. Notificación automática a stakeholders
4. Almacenamiento en storage
5. Integración con proceso `compliance-monitoring-v1.bpmn`

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar Proceso BPMN `compliance-monitoring-v1.bpmn`

**Agregar timer para generación diaria:**
```xml
<process id="compliance-monitoring-v1" name="Compliance Monitoring">
    
    <!-- Timer para generación diaria de informes -->
    <startEvent id="dailyReportTimer">
        <timerEventDefinition>
            <timeCycle>0 0 1 * * ?</timeCycle> <!-- Diario a las 01:00 -->
        </timerEventDefinition>
    </startEvent>
    
    <sequenceFlow id="flow1" sourceRef="dailyReportTimer" targetRef="generateDailyReport" />
    
    <serviceTask id="generateDailyReport" name="Generar Informe Diario">
        <extensionElements>
            <camunda:class>com.codeflowx.govern.workflow.delegates.GenerateDailyPmmReportDelegate</camunda:class>
        </extensionElements>
    </serviceTask>
    
    <sequenceFlow id="flow2" sourceRef="generateDailyReport" targetRef="notifyStakeholders" />
    
    <serviceTask id="notifyStakeholders" name="Notificar Stakeholders">
        <extensionElements>
            <camunda:class>com.codeflowx.govern.workflow.delegates.NotifyPmmReportDelegate</camunda:class>
        </extensionElements>
    </serviceTask>
    
    <endEvent id="endDailyReport" />
    <sequenceFlow id="flow3" sourceRef="notifyStakeholders" targetRef="endDailyReport" />
    
    <!-- Timer para generación mensual de informes -->
    <startEvent id="monthlyReportTimer">
        <timerEventDefinition>
            <timeCycle>0 0 2 1 * ?</timeCycle> <!-- Mensual día 1 a las 02:00 -->
        </timerEventDefinition>
    </startEvent>
    
    <sequenceFlow id="flow4" sourceRef="monthlyReportTimer" targetRef="generateMonthlyReport" />
    
    <serviceTask id="generateMonthlyReport" name="Generar Informe Mensual">
        <extensionElements>
            <camunda:class>com.codeflowx.govern.workflow.delegates.GenerateMonthlyPmmReportDelegate</camunda:class>
        </extensionElements>
    </serviceTask>
    
    <sequenceFlow id="flow5" sourceRef="generateMonthlyReport" targetRef="approveMonthlyReport" />
    
    <userTask id="approveMonthlyReport" name="Aprobar Informe Mensual">
        <humanPerformer>
            <resourceAssignmentExpression>
                <formalExpression>compliance-managers</formalExpression>
            </resourceAssignmentExpression>
        </humanPerformer>
    </userTask>
    
    <sequenceFlow id="flow6" sourceRef="approveMonthlyReport" targetRef="notifyMonthlyReport" />
    
    <serviceTask id="notifyMonthlyReport" name="Notificar Informe Mensual">
        <extensionElements>
            <camunda:class>com.codeflowx.govern.workflow.delegates.NotifyPmmReportDelegate</camunda:class>
        </extensionElements>
    </serviceTask>
    
    <endEvent id="endMonthlyReport" />
    <sequenceFlow id="flow7" sourceRef="notifyMonthlyReport" targetRef="endMonthlyReport" />
    
</process>
```

### 2. Crear Delegate para Generación Diaria

```java
package com.codeflowx.govern.workflow.delegates;

import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;
import com.codeflowx.govern.services.compliance.PostMarketSurveillanceReportService;
import com.codeflowx.govern.services.compliance.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateDailyPmmReportDelegate implements JavaDelegate {
    
    private final PostMarketSurveillanceReportService reportService;
    private final ProjectService projectService;
    
    @Override
    public void execute(DelegateExecution execution) {
        log.info("Iniciando generación automática de informes diarios PMM");
        
        // Obtener todos los proyectos activos con PMM plan activo
        List<Long> activeProjects = projectService.getActiveProjectsWithPmmPlan();
        
        for (Long projectId : activeProjects) {
            try {
                log.info("Generando informe diario para proyecto: {}", projectId);
                
                PostMarketSurveillanceReport report = reportService.generateReport(
                    projectId,
                    null, // Puede ser específico por modelo
                    PostMarketSurveillanceReport.ReportType.DAILY,
                    LocalDate.now().minusDays(1) // Informe del día anterior
                );
                
                execution.setVariable("lastReportId_" + projectId, report.getIdxpmsreport());
                execution.setVariable("lastReportDate_" + projectId, report.getPmsreportdate().toString());
                
                log.info("Informe diario generado exitosamente: {}", report.getIdxpmsreport());
            } catch (Exception e) {
                log.error("Error generando informe diario para proyecto: {}", projectId, e);
                // Continuar con otros proyectos
            }
        }
        
        log.info("Generación automática de informes diarios completada");
    }
}
```

### 3. Crear Delegate para Generación Mensual

```java
package com.codeflowx.govern.workflow.delegates;

import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;
import com.codeflowx.govern.services.compliance.PostMarketSurveillanceReportService;
import com.codeflowx.govern.services.compliance.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateMonthlyPmmReportDelegate implements JavaDelegate {
    
    private final PostMarketSurveillanceReportService reportService;
    private final ProjectService projectService;
    
    @Override
    public void execute(DelegateExecution execution) {
        log.info("Iniciando generación automática de informes mensuales PMM");
        
        // Obtener todos los proyectos activos con PMM plan activo
        List<Long> activeProjects = projectService.getActiveProjectsWithPmmPlan();
        
        for (Long projectId : activeProjects) {
            try {
                log.info("Generando informe mensual para proyecto: {}", projectId);
                
                LocalDate lastMonth = LocalDate.now().minusMonths(1);
                LocalDate firstDayOfLastMonth = lastMonth.withDayOfMonth(1);
                LocalDate lastDayOfLastMonth = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
                
                PostMarketSurveillanceReport report = reportService.generateReport(
                    projectId,
                    null,
                    PostMarketSurveillanceReport.ReportType.MONTHLY,
                    lastDayOfLastMonth // Informe del mes anterior
                );
                
                execution.setVariable("monthlyReportId_" + projectId, report.getIdxpmsreport());
                execution.setVariable("monthlyReportDate_" + projectId, report.getPmsreportdate().toString());
                
                log.info("Informe mensual generado exitosamente: {}", report.getIdxpmsreport());
            } catch (Exception e) {
                log.error("Error generando informe mensual para proyecto: {}", projectId, e);
                // Continuar con otros proyectos
            }
        }
        
        log.info("Generación automática de informes mensuales completada");
    }
}
```

### 4. Crear Delegate para Notificación

```java
package com.codeflowx.govern.workflow.delegates;

import com.codeflowx.govern.services.compliance.PostMarketSurveillanceReportService;
import com.codeflowx.govern.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotifyPmmReportDelegate implements JavaDelegate {
    
    private final PostMarketSurveillanceReportService reportService;
    private final NotificationService notificationService;
    
    @Override
    public void execute(DelegateExecution execution) {
        log.info("Iniciando notificación de informes PMM");
        
        // Obtener IDs de informes generados
        Map<String, Object> variables = execution.getVariables();
        
        variables.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith("lastReportId_") || 
                            entry.getKey().startsWith("monthlyReportId_"))
            .forEach(entry -> {
                Long reportId = (Long) entry.getValue();
                String reportType = entry.getKey().contains("monthly") ? "MENSUAL" : "DIARIO";
                
                try {
                    PostMarketSurveillanceReport report = reportService.getReport(reportId)
                        .orElseThrow();
                    
                    // Obtener stakeholders del proyecto
                    List<String> stakeholders = getStakeholdersForProject(report.getIdxproject());
                    
                    // Enviar notificación
                    notificationService.sendPmmReportNotification(
                        stakeholders,
                        report,
                        reportType
                    );
                    
                    log.info("Notificación enviada para informe {}: {}", reportType, reportId);
                } catch (Exception e) {
                    log.error("Error enviando notificación para informe: {}", reportId, e);
                }
            });
        
        log.info("Notificación de informes PMM completada");
    }
    
    private List<String> getStakeholdersForProject(Long projectId) {
        // Obtener emails de stakeholders del proyecto
        // Compliance managers, MLOps engineers, etc.
        return List.of(); // TODO: Implementar
    }
}
```

### 5. Crear Servicio de Notificación

```java
package com.codeflowx.govern.services.notification;

import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final JavaMailSender mailSender;
    
    /**
     * Envía notificación de informe PMM
     */
    public void sendPmmReportNotification(
            List<String> recipients,
            PostMarketSurveillanceReport report,
            String reportType) {
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(recipients.toArray(new String[0]));
            helper.setSubject(String.format("Informe PMM %s - Proyecto %d", reportType, report.getIdxproject()));
            helper.setText(buildEmailContent(report, reportType), true);
            
            // Adjuntar PDF si está disponible
            if (report.getPmsreportpdf() != null) {
                helper.addAttachment("report.pdf", 
                    new ByteArrayResource(report.getPmsreportpdf()));
            }
            
            mailSender.send(message);
            
            log.info("Notificación enviada a {} destinatarios", recipients.size());
        } catch (Exception e) {
            log.error("Error enviando notificación", e);
            throw new RuntimeException("Error enviando notificación", e);
        }
    }
    
    private String buildEmailContent(PostMarketSurveillanceReport report, String reportType) {
        return String.format("""
            <html>
            <body>
                <h2>Informe PMM %s</h2>
                <p>Se ha generado un nuevo informe de Post Market Monitoring.</p>
                <ul>
                    <li><strong>Proyecto:</strong> %d</li>
                    <li><strong>Tipo:</strong> %s</li>
                    <li><strong>Fecha:</strong> %s</li>
                </ul>
                <p>El informe se adjunta en formato PDF.</p>
            </body>
            </html>
            """, reportType, report.getIdxproject(), report.getPmsreporttype(), report.getPmsreportdate());
    }
}
```

---

## VALIDACIONES

1. ✅ Timer BPMN para generación diaria implementado
2. ✅ Timer BPMN para generación mensual implementado
3. ✅ Notificación automática a stakeholders funcional
4. ✅ Almacenamiento en storage verificado
5. ✅ Integración con proceso `compliance-monitoring-v1.bpmn` completa

---

## NOTAS

- Los timers se ejecutan automáticamente según configuración
- Los informes mensuales requieren aprobación manual
- Las notificaciones se envían por email con PDF adjunto
- Se debe configurar SMTP para envío de emails

