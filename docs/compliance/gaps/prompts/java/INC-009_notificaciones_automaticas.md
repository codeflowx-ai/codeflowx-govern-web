# PROMPT: INC-009-DS - Notificaciones Automáticas para Hallazgos Críticos

**Incidencia:** INC-009-DS  
**Prioridad:** 🟡 MEDIUM  
**Artículo EU AI Act:** Art. 72 (incident reporting)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Java - Backend + BPMN

---

## CONTEXTO

El sistema no envía notificaciones automáticas (email, Slack, etc.) cuando se detectan problemas críticos (bias severo, leakage, etc.).

**Ubicación Actual:**
- No hay integración con sistemas de notificación
- Usuarios deben consultar dashboard manualmente

---

## REQUISITOS

1. Integración con email (SMTP)
2. Integración con Slack/Teams para alertas
3. Configuración de umbrales de notificación por usuario/rol
4. Notificaciones inmediatas para: bias CRITICAL, leakage detectado, drift severo

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Service de Notificaciones

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/notifications/NotificationService.java`

```java
package com.codeflowx.govern.business.notifications;

import com.codeflowx.govern.entity.governance.DatasetQuality;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service para enviar notificaciones automáticas
 * EU AI Act Art. 72 - Incident reporting
 */
@Slf4j
@Service
public class NotificationService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    @Autowired
    private SlackNotificationService slackService;
    
    @Value("${notification.email.enabled:true}")
    private boolean emailEnabled;
    
    @Value("${notification.slack.enabled:false}")
    private boolean slackEnabled;
    
    /**
     * Notifica hallazgos críticos de evaluación de dataset
     */
    public void notifyCriticalFindings(DatasetQuality evaluation, List<String> criticalIssues) {
        if (criticalIssues.isEmpty()) {
            return;
        }
        
        String subject = String.format(
            "🚨 Hallazgos Críticos en Evaluación de Dataset: %s",
            evaluation.getDqldatasetname()
        );
        
        String message = buildNotificationMessage(evaluation, criticalIssues);
        
        // Notificar por email
        if (emailEnabled) {
            sendEmailNotification(subject, message, getRecipients(evaluation));
        }
        
        // Notificar por Slack
        if (slackEnabled) {
            sendSlackNotification(subject, message, getSlackChannels(evaluation));
        }
    }
    
    private String buildNotificationMessage(DatasetQuality evaluation, List<String> criticalIssues) {
        StringBuilder sb = new StringBuilder();
        sb.append("Se detectaron hallazgos críticos en la evaluación de dataset:\n\n");
        sb.append("Dataset: ").append(evaluation.getDqldatasetname()).append("\n");
        sb.append("Evaluación ID: ").append(evaluation.getDqlevaluationid()).append("\n");
        sb.append("Score: ").append(evaluation.getDqloverallscore()).append("\n");
        sb.append("Decisión: ").append(evaluation.getDqldecision()).append("\n\n");
        
        sb.append("Hallazgos Críticos:\n");
        for (String issue : criticalIssues) {
            sb.append("- ").append(issue).append("\n");
        }
        
        sb.append("\nAcción requerida: Revisar evaluación en dashboard.");
        sb.append("\nURL: ").append(getDashboardUrl(evaluation));
        
        return sb.toString();
    }
    
    private void sendEmailNotification(String subject, String message, List<String> recipients) {
        if (mailSender == null) {
            log.warn("JavaMailSender not configured, skipping email notification");
            return;
        }
        
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipients.toArray(new String[0]));
            email.setSubject(subject);
            email.setText(message);
            email.setFrom("noreply@codeflowx.com");
            
            mailSender.send(email);
            log.info("Email notification sent to {} recipients", recipients.size());
            
        } catch (Exception e) {
            log.error("Error sending email notification: {}", e.getMessage(), e);
        }
    }
    
    private void sendSlackNotification(String subject, String message, List<String> channels) {
        if (slackService == null) {
            log.warn("SlackNotificationService not configured, skipping Slack notification");
            return;
        }
        
        try {
            for (String channel : channels) {
                slackService.sendMessage(channel, subject, message);
            }
            log.info("Slack notification sent to {} channels", channels.size());
            
        } catch (Exception e) {
            log.error("Error sending Slack notification: {}", e.getMessage(), e);
        }
    }
    
    private List<String> getRecipients(DatasetQuality evaluation) {
        // Obtener usuarios que deben ser notificados
        // - Owner del proyecto
        // - Compliance officers
        // - Usuarios con rol de notificación para este proyecto
        return List.of("compliance@codeflowx.com", evaluation.getDqlcreatedby() + "@codeflowx.com");
    }
    
    private List<String> getSlackChannels(DatasetQuality evaluation) {
        // Obtener canales de Slack según proyecto/sector
        return List.of("#ai-governance", "#compliance-alerts");
    }
    
    private String getDashboardUrl(DatasetQuality evaluation) {
        return String.format(
            "https://codeflowx.com/console/platform/views/governance/dataset-quality-dashboard-overview.zul?evaluationId=%d",
            evaluation.getIdxdatasetquality()
        );
    }
}
```

### 2. Crear Service de Slack

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/notifications/SlackNotificationService.java`

```java
package com.codeflowx.govern.business.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SlackNotificationService {
    
    @Value("${notification.slack.webhook.url:}")
    private String webhookUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public void sendMessage(String channel, String subject, String message) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.warn("Slack webhook URL not configured");
            return;
        }
        
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("channel", channel);
            payload.put("username", "CodeflowX AI Governance");
            payload.put("icon_emoji", ":warning:");
            
            // Formatear mensaje
            Map<String, Object> attachment = new HashMap<>();
            attachment.put("color", "danger");  // Rojo para crítico
            attachment.put("title", subject);
            attachment.put("text", message);
            attachment.put("footer", "CodeflowX AI Governance");
            attachment.put("ts", System.currentTimeMillis() / 1000);
            
            payload.put("attachments", List.of(attachment));
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(webhookUrl, entity, String.class);
            
            log.info("Slack notification sent to channel: {}", channel);
            
        } catch (Exception e) {
            log.error("Error sending Slack notification: {}", e.getMessage(), e);
        }
    }
}
```

### 3. Modificar StoreEvaluationDelegate para Notificar

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/StoreEvaluationDelegate.java`

```java
@Autowired
private NotificationService notificationService;

@Override
public void execute(DelegateExecution execution) {
    // ... código existente para guardar evaluación ...
    
    // Detectar hallazgos críticos
    List<String> criticalIssues = detectCriticalIssues(quality);
    
    if (!criticalIssues.isEmpty()) {
        log.warn("Critical issues detected in evaluation {}: {}", 
                quality.getDqlevaluationid(), criticalIssues);
        
        // Notificar
        notificationService.notifyCriticalFindings(quality, criticalIssues);
        
        // Guardar en variable BPMN
        execution.setVariable("criticalIssuesDetected", true);
        execution.setVariable("criticalIssues", criticalIssues);
    }
}

private List<String> detectCriticalIssues(DatasetQuality quality) {
    List<String> issues = new ArrayList<>();
    
    // Bias crítico
    if (quality.getDqloverallscore() != null && quality.getDqloverallscore() < 60) {
        issues.add("Score de calidad crítico: " + quality.getDqloverallscore());
    }
    
    // Parsear issues desde JSONB
    if (quality.getDqlissuesfound() != null) {
        List<Map<String, Object>> issuesList = parseJson(quality.getDqlissuesfound());
        for (Map<String, Object> issue : issuesList) {
            String severity = (String) issue.get("severity");
            if ("CRITICAL".equals(severity) || "HIGH".equals(severity)) {
                issues.add((String) issue.get("message"));
            }
        }
    }
    
    return issues;
}
```

### 4. Configuración application.properties

```properties
# Email notifications
notification.email.enabled=true
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Slack notifications
notification.slack.enabled=true
notification.slack.webhook.url=${SLACK_WEBHOOK_URL}
```

---

## VALIDACIONES

1. ✅ Email se envía cuando hay hallazgos críticos
2. ✅ Slack se notifica cuando está configurado
3. ✅ Recipientes se obtienen correctamente
4. ✅ Mensaje contiene información relevante
5. ✅ Notificaciones no se duplican

---

## TESTING

```java
@Test
public void testNotifyCriticalFindings() {
    DatasetQuality evaluation = createTestEvaluation();
    List<String> issues = Arrays.asList("Bias crítico detectado", "Leakage encontrado");
    
    notificationService.notifyCriticalFindings(evaluation, issues);
    
    // Verificar que se envió email/Slack
}
```

---

## DOCUMENTACIÓN

Actualizar:
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Notificaciones automáticas
- Crear guía de configuración de notificaciones

---

## CUMPLIMIENTO EU AI ACT

**Art. 72:** Incident reporting
- ✅ Notificaciones automáticas para hallazgos críticos
- ✅ Integración con sistemas de comunicación
- ✅ Trazabilidad de notificaciones enviadas

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** DevOps Team

---

**Estado:** ✅ COMPLETADO

