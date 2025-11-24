# PROMPT: INC-008-004 - Falta Alerta Automática ante Detección de Manipulación
## EU AI Act Art. 19 - Registro Inmutable

**Incidencia:** INC-008-004  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 19.1 (inalterabilidad)  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend + Scheduler

---

## CONTEXTO

El sistema detecta manipulación mediante `verifyIntegrity()`, pero **no notifica automáticamente** a administradores cuando se detecta `TAMPERED` o `CHAIN_BROKEN`.

**Ubicación Actual:**
- `ImmutableLoggingBusinessService.java` - método `verifyIntegrity()` detecta pero no notifica
- Falta job programado para verificación periódica
- Falta sistema de notificaciones

---

## REQUISITOS

1. **Implementar notificación automática:**
   - Email a administradores cuando `verifyIntegrity()` detecta `TAMPERED`
   - Alertas en dashboard de administración
   - Integración con sistema de alertas (PagerDuty, Slack, etc.)

2. **Programar verificación periódica:**
   - Job scheduler que ejecute `verifyIntegrity()` diariamente
   - Alertar si detecta problemas

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Servicio de Notificaciones

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/logging/IntegrityAlertService.java` (NUEVO)

```java
package com.codeflowx.govern.business.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * Servicio para alertas automáticas de manipulación de logs inmutables
 * EU AI Act Art. 19.1 - Inalterabilidad
 */
@Service
@Slf4j
public class IntegrityAlertService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    @Autowired
    private ImmutableLoggingBusinessService immutableLoggingService;
    
    @Value("${compliance.alerts.email.enabled:true}")
    private boolean emailAlertsEnabled;
    
    @Value("${compliance.alerts.email.recipients:}")
    private String[] alertRecipients;
    
    @Value("${compliance.alerts.slack.webhook.url:}")
    private String slackWebhookUrl;
    
    /**
     * Verifica integridad y alerta si detecta problemas
     */
    public void verifyAndAlert() {
        log.info("Iniciando verificación de integridad de logs inmutables");
        
        // Verificar toda la cadena
        ImmutableLoggingBusinessService.LogIntegrityReport report = 
            immutableLoggingService.verifyIntegrity(1L, null);
        
        if (!report.isIntegrityValid()) {
            log.error("INTEGRIDAD COMPROMETIDA: {} logs corruptos detectados", 
                report.getCorruptedLogs().size());
            
            // Enviar alertas
            sendAlerts(report);
        } else {
            log.info("Verificación de integridad exitosa: {} logs verificados", 
                report.getTotalLogsChecked());
        }
    }
    
    /**
     * Envía alertas cuando se detecta manipulación
     */
    private void sendAlerts(ImmutableLoggingBusinessService.LogIntegrityReport report) {
        String alertMessage = buildAlertMessage(report);
        
        // Email
        if (emailAlertsEnabled && alertRecipients.length > 0) {
            sendEmailAlert(alertMessage);
        }
        
        // Slack (si está configurado)
        if (slackWebhookUrl != null && !slackWebhookUrl.isEmpty()) {
            sendSlackAlert(alertMessage);
        }
        
        // Log crítico
        log.error("ALERTA CRÍTICA - Manipulación detectada en logs inmutables:\n{}", alertMessage);
    }
    
    /**
     * Construye mensaje de alerta
     */
    private String buildAlertMessage(ImmutableLoggingBusinessService.LogIntegrityReport report) {
        StringBuilder message = new StringBuilder();
        message.append("🚨 ALERTA CRÍTICA - Manipulación Detectada en Logs Inmutables\n\n");
        message.append("EU AI Act Art. 19.1 - Inalterabilidad Comprometida\n\n");
        message.append("Detalles:\n");
        message.append("- Total logs verificados: ").append(report.getTotalLogsChecked()).append("\n");
        message.append("- Logs corruptos: ").append(report.getCorruptedLogs().size()).append("\n");
        message.append("- Estado: ").append(report.isIntegrityValid() ? "VÁLIDO" : "COMPROMETIDO").append("\n\n");
        
        if (!report.getCorruptedLogs().isEmpty()) {
            message.append("Logs Corruptos Detectados:\n");
            for (String corruptedLog : report.getCorruptedLogs()) {
                message.append("  - ").append(corruptedLog).append("\n");
            }
        }
        
        message.append("\nAcción Requerida:\n");
        message.append("1. Investigar inmediatamente los logs corruptos\n");
        message.append("2. Verificar acceso a base de datos\n");
        message.append("3. Revisar logs de sistema para actividad sospechosa\n");
        message.append("4. Contactar al equipo de seguridad\n");
        
        return message.toString();
    }
    
    /**
     * Envía alerta por email
     */
    private void sendEmailAlert(String message) {
        if (mailSender == null) {
            log.warn("JavaMailSender no configurado - saltando envío de email");
            return;
        }
        
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(alertRecipients);
            email.setSubject("🚨 ALERTA CRÍTICA - Manipulación Detectada en Logs Inmutables");
            email.setText(message);
            email.setFrom("compliance@codeflowx.com");
            
            mailSender.send(email);
            log.info("Alerta por email enviada a {} destinatarios", alertRecipients.length);
        } catch (Exception e) {
            log.error("Error enviando alerta por email", e);
        }
    }
    
    /**
     * Envía alerta a Slack
     */
    private void sendSlackAlert(String message) {
        try {
            // TODO: Implementar integración con Slack webhook
            // Por ahora, solo log
            log.info("Alerta Slack (no implementado aún):\n{}", message);
        } catch (Exception e) {
            log.error("Error enviando alerta a Slack", e);
        }
    }
}
```

### 2. Job Programado para Verificación Periódica

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/scheduler/IntegrityVerificationJob.java` (NUEVO)

```java
package com.codeflowx.govern.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.codeflowx.govern.business.logging.IntegrityAlertService;
import lombok.extern.slf4j.Slf4j;

/**
 * Job programado para verificación periódica de integridad de logs inmutables
 * EU AI Act Art. 19.1 - Inalterabilidad
 * 
 * Ejecuta verificación diaria a las 2:00 AM
 */
@Component
@Slf4j
public class IntegrityVerificationJob {
    
    @Autowired
    private IntegrityAlertService integrityAlertService;
    
    /**
     * Verifica integridad de logs inmutables diariamente
     * Cron: 0 0 2 * * ? (2:00 AM todos los días)
     */
    @Scheduled(cron = "${compliance.integrity.verification.cron:0 0 2 * * ?}")
    public void verifyIntegrityDaily() {
        log.info("Ejecutando verificación periódica de integridad de logs inmutables");
        
        try {
            integrityAlertService.verifyAndAlert();
        } catch (Exception e) {
            log.error("Error en verificación periódica de integridad", e);
        }
    }
    
    /**
     * Verificación manual (para testing)
     */
    public void verifyIntegrityManual() {
        log.info("Ejecutando verificación manual de integridad");
        verifyIntegrityDaily();
    }
}
```

### 3. Configuración de Spring Scheduler

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/config/SchedulerConfig.java` (NUEVO o modificar existente)

```java
package com.codeflowx.govern.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuración para jobs programados
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // Configuración básica - Spring Boot detecta automáticamente @Scheduled
}
```

### 4. Configuración de Propiedades

**Archivo:** `suinsit.nova.web/src/main/resources/application.properties` (añadir)

```properties
# Configuración de alertas de integridad
compliance.integrity.verification.cron=0 0 2 * * ?
compliance.alerts.email.enabled=true
compliance.alerts.email.recipients=admin@codeflowx.com,security@codeflowx.com
compliance.alerts.slack.webhook.url=

# Configuración de email (si no existe)
spring.mail.host=smtp.codeflowx.com
spring.mail.port=587
spring.mail.username=compliance@codeflowx.com
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 5. Modificar ImmutableLoggingBusinessService para Actualizar Estado

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java` (MODIFICAR)

Añadir al método `verifyIntegrity()`:

```java
// Después de detectar hash mismatch o chain broken, actualizar estado en BD
if (!calculatedHash.equals(log.getImlcurrenthash())) {
    report.setIntegrityValid(false);
    report.addCorruptedLog(log.getIdximmutablelog(), "Current hash mismatch");
    log.error("Hash mismatch detected for log ID: {}", log.getIdximmutablelog());
    
    // Actualizar estado en BD
    updateIntegrityStatus(log.getIdximmutablelog(), "TAMPERED");
}

if (expectedPreviousHash != null && !log.getImlprevioushash().equals(expectedPreviousHash)) {
    report.setIntegrityValid(false);
    report.addCorruptedLog(log.getIdximmutablelog(), "Chain broken");
    log.error("Chain broken at log ID: {}", log.getIdximmutablelog());
    
    // Actualizar estado en BD
    updateIntegrityStatus(log.getIdximmutablelog(), "CHAIN_BROKEN");
}

// Método auxiliar para actualizar estado (solo UPDATE del campo de estado)
private void updateIntegrityStatus(Long logId, String status) {
    String query = "UPDATE IMLIMMUTABLELOGS SET IMLINTEGRITYSTATUS = ?, IMLLASTVERIFICATIONDATE = CURRENT_TIMESTAMP WHERE IDXIMMUTABLELOG = ?";
    dao.executeUpdate(query, status, logId);
}
```

**NOTA:** Este UPDATE solo modifica el campo de estado, no los datos del log. Es necesario para tracking, pero debe documentarse claramente.

### 6. Endpoint REST para Verificación Manual

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/IntegrityController.java` (NUEVO)

```java
package com.codeflowx.govern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.codeflowx.govern.business.logging.IntegrityAlertService;
import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para verificación de integridad de logs inmutables
 * EU AI Act Art. 19.1
 */
@RestController
@RequestMapping("/api/v1/compliance/logs/integrity")
@Slf4j
public class IntegrityController {
    
    @Autowired
    private ImmutableLoggingBusinessService immutableLoggingService;
    
    @Autowired
    private IntegrityAlertService integrityAlertService;
    
    /**
     * Verifica integridad de logs
     * GET /api/v1/compliance/logs/integrity/verify
     */
    @GetMapping("/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImmutableLoggingBusinessService.LogIntegrityReport> verifyIntegrity(
        @RequestParam(required = false) Long startId,
        @RequestParam(required = false) Long endId
    ) {
        log.info("Verificación manual de integridad solicitada: startId={}, endId={}", startId, endId);
        
        Long start = startId != null ? startId : 1L;
        Long end = endId != null ? endId : null;
        
        ImmutableLoggingBusinessService.LogIntegrityReport report = 
            immutableLoggingService.verifyIntegrity(start, end);
        
        return ResponseEntity.ok(report);
    }
    
    /**
     * Ejecuta verificación y alerta
     * POST /api/v1/compliance/logs/integrity/verify-and-alert
     */
    @PostMapping("/verify-and-alert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> verifyAndAlert() {
        log.info("Verificación y alerta solicitada manualmente");
        
        integrityAlertService.verifyAndAlert();
        
        return ResponseEntity.ok("Verificación completada. Alertas enviadas si se detectaron problemas.");
    }
}
```

---

## PRUEBAS REQUERIDAS

### 1. Prueba de Verificación Periódica

```bash
# Verificar que el job se ejecuta
# Revisar logs a las 2:00 AM
tail -f logs/application.log | grep "IntegrityVerificationJob"
```

### 2. Prueba de Alerta por Email

- Modificar manualmente un hash en BD (solo para testing)
- Ejecutar verificación
- Verificar que se envía email a destinatarios configurados

### 3. Prueba de Endpoint REST

```bash
# Verificación manual
curl -X GET "http://localhost:8080/api/v1/compliance/logs/integrity/verify" \
  -H "Authorization: Bearer <admin_token>"

# Verificación y alerta
curl -X POST "http://localhost:8080/api/v1/compliance/logs/integrity/verify-and-alert" \
  -H "Authorization: Bearer <admin_token>"
```

### 4. Prueba de Actualización de Estado

- Verificar que cuando se detecta `TAMPERED`, el campo `IMLINTEGRITYSTATUS` se actualiza
- Verificar que `IMLLASTVERIFICATIONDATE` se actualiza

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md#inc-008-004`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
- **Artículo EU AI Act:** Art. 19.1 (inalterabilidad)

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 1 día  
**Responsable:** Backend Team + DevOps Team

