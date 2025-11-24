# PROMPT: INC-012-008 - Notificación Email al Propietario del Modelo al Completar Despliegue

**Incidencia:** INC-012-008  
**Prioridad:** 🟢 BAJA  
**Artículo EU AI Act:** N/A (Mejora UX)  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java

---

## CONTEXTO

Cuando un despliegue se completa exitosamente, el sistema no envía notificación por email al propietario del modelo. Solo se registra en logs, lo que puede llevar a que el propietario no sepa inmediatamente que su modelo está en producción.

**Ubicación Actual:**
- Delegate: `DeploymentExecutionDelegate.java`
- Service: `NotificationService.java` (si existe)
- Proceso BPMN: `deployment-automation-v1.bpmn`

**Problema:**
- Falta de notificación proactiva
- Propietarios no saben inmediatamente que su modelo está en producción
- Mejora de experiencia de usuario

---

## REQUISITOS

1. **Enviar email al propietario** cuando despliegue se completa exitosamente
2. **Incluir información relevante:**
   - Nombre del modelo y versión
   - Ambiente de despliegue
   - URL del endpoint
   - Timestamp de despliegue
3. **Usar servicio de notificaciones** existente o crear uno nuevo
4. **Manejar errores** de envío sin interrumpir despliegue

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar Delegate: `DeploymentExecutionDelegate.java`

**Ubicación:** `com.codeflowx.govern.delegate.bpmn.DeploymentExecutionDelegate`

Añadir envío de email al completar despliegue:

```java
package com.codeflowx.govern.delegate.bpmn;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.serving.ModelDeployment;
import com.codeflowx.govern.service.BusinessService;
import com.codeflowx.govern.service.notification.NotificationService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Delegate para ejecutar despliegue de modelo
 * Requisito: INC-012-008 - Notificación email al propietario
 */
@Component("deploymentExecutionDelegate")
public class DeploymentExecutionDelegate implements JavaDelegate {
    
    private static final Logger log = LoggerFactory.getLogger(DeploymentExecutionDelegate.class);
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long deploymentId = (Long) execution.getVariable("deploymentId");
        
        if (deploymentId == null) {
            log.error("Deployment ID is required");
            return;
        }
        
        log.info("Ejecutando despliegue para deployment: {}", deploymentId);
        
        // Obtener deployment
        ModelDeployment deployment = businessService.findById(ModelDeployment.class, deploymentId);
        if (deployment == null) {
            log.error("Deployment not found: {}", deploymentId);
            return;
        }
        
        // Obtener modelo
        Model model = businessService.findById(Model.class, deployment.getModelId());
        if (model == null) {
            log.error("Model not found: {}", deployment.getModelId());
            return;
        }
        
        // Ejecutar despliegue (lógica existente)
        boolean success = performDeployment(deployment, execution);
        
        if (success) {
            // Actualizar estado
            deployment.setStatus("ACTIVE");
            deployment.setDeployedAt(new Timestamp(System.currentTimeMillis()));
            businessService.save(deployment);
            
            // Actualizar estado del modelo
            model.setModstatus("PRODUCTION");
            businessService.save(model);
            
            // NUEVO: Enviar notificación email al propietario
            sendDeploymentNotificationEmail(deployment, model);
            
            log.info("Despliegue completado exitosamente para deployment: {}", deploymentId);
        } else {
            log.error("Despliegue falló para deployment: {}", deploymentId);
            // Manejar fallo (rollback, etc.)
        }
    }
    
    /**
     * Ejecuta despliegue (lógica existente)
     */
    private boolean performDeployment(ModelDeployment deployment, DelegateExecution execution) {
        // Lógica existente de despliegue
        // TODO: Mantener lógica actual
        return true; // Placeholder
    }
    
    /**
     * Envía notificación por email al propietario del modelo
     * Requisito: INC-012-008
     */
    private void sendDeploymentNotificationEmail(ModelDeployment deployment, Model model) {
        try {
            // Obtener email del propietario
            String ownerEmail = getOwnerEmail(model);
            
            if (ownerEmail == null || ownerEmail.trim().isEmpty()) {
                log.warn("No se puede enviar notificación: email del propietario no disponible para modelo {}", 
                    model.getIdxmodel());
                return;
            }
            
            // Construir asunto
            String subject = String.format(
                "Modelo desplegado exitosamente: %s v%s",
                model.getModname(),
                deployment.getVersion()
            );
            
            // Construir cuerpo del email
            String body = buildEmailBody(deployment, model);
            
            // Enviar email
            notificationService.sendEmail(
                ownerEmail,
                subject,
                body
            );
            
            log.info("Notificación email enviada al propietario del modelo: {} ({})", 
                model.getModname(), ownerEmail);
            
        } catch (Exception e) {
            // No lanzar excepción para no interrumpir despliegue
            log.error("Error enviando notificación email al propietario: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene email del propietario del modelo
     */
    private String getOwnerEmail(Model model) {
        // Opción 1: Si modelo tiene campo de email
        if (model.getModowneremail() != null) {
            return model.getModowneremail();
        }
        
        // Opción 2: Obtener desde usuario propietario
        if (model.getModownerid() != null) {
            String query = "SELECT usuemail FROM USUUSERS WHERE IDXUSER = ?";
            List<String> emails = businessService.findBySQL(String.class, query, model.getModownerid());
            if (!emails.isEmpty()) {
                return emails.get(0);
            }
        }
        
        // Opción 3: Obtener desde proyecto asociado
        if (model.getIdxproject() != null) {
            String query = "SELECT prjowneremail FROM PRJPROJECTS WHERE IDXPROJECT = ?";
            List<String> emails = businessService.findBySQL(String.class, query, model.getIdxproject());
            if (!emails.isEmpty()) {
                return emails.get(0);
            }
        }
        
        return null;
    }
    
    /**
     * Construye cuerpo del email
     */
    private String buildEmailBody(ModelDeployment deployment, Model model) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        StringBuilder body = new StringBuilder();
        body.append("Estimado/a,\n\n");
        body.append("Le informamos que su modelo de IA ha sido desplegado exitosamente en producción.\n\n");
        body.append("Detalles del despliegue:\n");
        body.append("─────────────────────────────────────────\n");
        body.append("Modelo: ").append(model.getModname()).append("\n");
        body.append("Versión: ").append(deployment.getVersion()).append("\n");
        body.append("Ambiente: ").append(deployment.getEnvironment()).append("\n");
        body.append("Fecha y hora: ").append(dateFormat.format(deployment.getDeployedAt())).append("\n");
        
        // Añadir URL del endpoint si está disponible
        if (deployment.getEndpointUrl() != null) {
            body.append("URL del endpoint: ").append(deployment.getEndpointUrl()).append("\n");
        }
        
        body.append("─────────────────────────────────────────\n\n");
        body.append("El modelo está ahora disponible para uso en producción.\n\n");
        body.append("Si tiene alguna pregunta o necesita asistencia, por favor contacte al equipo de soporte.\n\n");
        body.append("Saludos,\n");
        body.append("Equipo de CodeflowX\n");
        
        return body.toString();
    }
}
```

### 2. Crear/Modificar Service: `NotificationService.java`

**Ubicación:** `com.codeflowx.govern.service.notification.NotificationService`

```java
package com.codeflowx.govern.service.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para enviar notificaciones
 * Requisito: INC-012-008
 */
@Service
public class NotificationService {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    /**
     * Envía email
     */
    public void sendEmail(String to, String subject, String body) {
        if (mailSender == null) {
            log.warn("JavaMailSender no configurado - no se puede enviar email a: {}", to);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@codeflowx.com"); // Ajustar según configuración
            
            mailSender.send(message);
            
            log.info("Email enviado exitosamente a: {}", to);
            
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Error enviando email", e);
        }
    }
}
```

### 3. Configurar JavaMailSender (si no existe)

**Ubicación:** `application.properties` o `application.yml`

```properties
# Configuración SMTP para notificaciones
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=noreply@codeflowx.com
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Añadir template HTML** para emails más profesionales
2. **Incluir enlaces** a dashboard y documentación
3. **Añadir opción** para desactivar notificaciones por usuario
4. **Enviar notificación también** cuando despliegue falla (con motivo)

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Completar despliegue exitoso → Verificar que email se envía al propietario
2. **Test 2:** Verificar que email incluye toda la información relevante
3. **Test 3:** Verificar que error en envío de email no interrumpe despliegue
4. **Test 4:** Verificar que se maneja correctamente cuando email del propietario no está disponible
5. **Test 5:** Verificar formato del email (legible y profesional)

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_012_PUESTA_PRODUCCION.md#inc-012-008`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_012_PUESTA_PRODUCCION.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar obtención de email según estructura real de entidades
- Configurar SMTP según proveedor de email (Gmail, SendGrid, etc.)
- Considerar usar servicio de email externo (SendGrid, AWS SES, etc.)
- Añadir template HTML para emails más profesionales
- Considerar hacer notificaciones configurables por usuario
- Añadir logs de auditoría para cada email enviado

---

**Estado:** ✅ COMPLETADO

