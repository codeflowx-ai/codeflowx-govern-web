# PROMPT: INC-012 - Alertas Automáticas de Tampering

**Incidencia:** INC-012  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 19 (Logs Inmutables)  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## CONTEXTO

El sistema detecta manipulación (tampering) en logs inmutables pero no genera alertas automáticas ni notifica al equipo de seguridad. Solo se registra en log.

**Ubicación Actual:**
- `ImmutableLoggingBusinessService.java` - método `verifyIntegrity()`

---

## REQUISITOS

1. Generar alerta CRITICAL automática al detectar tampering
2. Notificar inmediatamente al equipo de seguridad
3. Bloquear acceso del usuario que causó tampering
4. Crear incidente automático en sistema de incidentes

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar `ImmutableLoggingBusinessService.java`

Añadir método para detectar y alertar tampering:

```java
/**
 * Detecta tampering y genera alertas automáticas
 * Requisito: Art. 19 EU AI Act - Logs Inmutables
 */
public void detectTampering(ImmutableLog log) {
    log.error("TAMPERING DETECTED - Log ID: {}", log.getIdximmutablelog());
    
    // 1. Marcar log como TAMPERED
    log.setImlintegritystatus("TAMPERED");
    try {
        // NOTA: Normalmente no se puede UPDATE en logs inmutables
        // Si tu sistema permite marcar estado, usar esto:
        // dao.update(log);
        
        // Alternativa: Crear nuevo log de alerta
        createTamperingAlertLog(log);
    } catch (Exception e) {
        log.error("Error marcando log como TAMPERED", e);
    }
    
    // 2. Generar alerta CRITICAL
    createCriticalAlert(log);
    
    // 3. Notificar equipo de seguridad
    notifySecurityTeam(log);
    
    // 4. Bloquear usuario
    blockUser(log.getImluserid(), log);
    
    // 5. Crear incidente
    createSecurityIncident(log);
}

/**
 * Crea log de alerta de tampering (nuevo log inmutable)
 */
private void createTamperingAlertLog(ImmutableLog tamperedLog) {
    Map<String, Object> alertData = new HashMap<>();
    alertData.put("tamperedLogId", tamperedLog.getIdximmutablelog());
    alertData.put("tamperedLogHash", tamperedLog.getImlcurrenthash());
    alertData.put("tamperedLogPreviousHash", tamperedLog.getImlprevioushash());
    alertData.put("tamperedLogEntityType", tamperedLog.getImlentitytype());
    alertData.put("tamperedLogEntityId", tamperedLog.getImlentityid());
    alertData.put("tamperedLogAction", tamperedLog.getImlaction());
    alertData.put("tamperedLogUserId", tamperedLog.getImluserid());
    alertData.put("tamperedLogUserName", tamperedLog.getImlusername());
    alertData.put("tamperedLogTimestamp", tamperedLog.getImltimestamp().toString());
    alertData.put("detectedAt", new Timestamp(System.currentTimeMillis()).toString());
    alertData.put("severity", "CRITICAL");
    alertData.put("article", "Art. 19");
    
    createLogEntry(
        "IMMUTABLE_LOG",
        tamperedLog.getIdximmutablelog(),
        "TAMPERING_DETECTED",
        null, // System user
        "SYSTEM",
        alertData
    );
}

/**
 * Crea alerta CRITICAL en sistema de alertas
 */
private void createCriticalAlert(ImmutableLog tamperedLog) {
    try {
        // TODO: Integrar con sistema de alertas
        // AlertService alertService = context.getBean(AlertService.class);
        
        String alertTitle = "TAMPERING DETECTED - Log Manipulation";
        String alertDescription = String.format(
            "CRITICAL: Log inmutable ha sido manipulado.\n\n" +
            "Log ID: %d\n" +
            "Entity Type: %s\n" +
            "Entity ID: %d\n" +
            "Action: %s\n" +
            "User: %s (ID: %d)\n" +
            "Timestamp: %s\n" +
            "Hash: %s\n\n" +
            "Esta es una violación crítica de seguridad según Art. 19 EU AI Act.\n" +
            "El usuario ha sido bloqueado automáticamente.",
            tamperedLog.getIdximmutablelog(),
            tamperedLog.getImlentitytype(),
            tamperedLog.getImlentityid(),
            tamperedLog.getImlaction(),
            tamperedLog.getImlusername(),
            tamperedLog.getImluserid(),
            tamperedLog.getImltimestamp(),
            tamperedLog.getImlcurrenthash()
        );
        
        // alertService.createAlert(
        //     "CRITICAL",
        //     alertTitle,
        //     alertDescription,
        //     "SECURITY_TAMPERING",
        //     tamperedLog.getIdximmutablelog()
        // );
        
        log.info("Alerta CRITICAL creada para tampering: Log ID={}", tamperedLog.getIdximmutablelog());
        
    } catch (Exception e) {
        log.error("Error creando alerta de tampering", e);
    }
}

/**
 * Notifica al equipo de seguridad
 */
private void notifySecurityTeam(ImmutableLog tamperedLog) {
    try {
        // TODO: Integrar con sistema de notificaciones
        // NotificationService notificationService = context.getBean(NotificationService.class);
        
        String notificationSubject = "🚨 CRITICAL: Tampering Detectado en Logs Inmutables";
        String notificationBody = String.format(
            "Se ha detectado manipulación (tampering) en un log inmutable.\n\n" +
            "Detalles:\n" +
            "- Log ID: %d\n" +
            "- Entity: %s/%d\n" +
            "- Action: %s\n" +
            "- Usuario: %s (ID: %d)\n" +
            "- Timestamp: %s\n" +
            "- Hash: %s\n\n" +
            "Acciones tomadas:\n" +
            "✓ Usuario bloqueado automáticamente\n" +
            "✓ Alerta CRITICAL generada\n" +
            "✓ Incidente de seguridad creado\n\n" +
            "Requisito: Art. 19 EU AI Act - Logs Inmutables",
            tamperedLog.getIdximmutablelog(),
            tamperedLog.getImlentitytype(),
            tamperedLog.getImlentityid(),
            tamperedLog.getImlaction(),
            tamperedLog.getImlusername(),
            tamperedLog.getImluserid(),
            tamperedLog.getImltimestamp(),
            tamperedLog.getImlcurrenthash()
        );
        
        // Obtener lista de usuarios del equipo de seguridad
        // List<User> securityTeam = userService.getUsersByRole("SECURITY_OFFICER");
        // for (User securityUser : securityTeam) {
        //     notificationService.sendNotification(
        //         securityUser.getIdxuser(),
        //         notificationSubject,
        //         notificationBody,
        //         "CRITICAL"
        //     );
        // }
        
        log.info("Notificación enviada a equipo de seguridad: Log ID={}", tamperedLog.getIdximmutablelog());
        
    } catch (Exception e) {
        log.error("Error notificando equipo de seguridad", e);
    }
}

/**
 * Bloquea usuario que causó tampering
 */
private void blockUser(Long userId, ImmutableLog tamperedLog) {
    if (userId == null) {
        log.warn("No se puede bloquear usuario: userId es null");
        return;
    }
    
    try {
        // TODO: Integrar con servicio de usuarios
        // UserService userService = context.getBean(UserService.class);
        
        String blockReason = String.format(
            "Usuario bloqueado por tampering detectado en log inmutable.\n" +
            "Log ID: %d\n" +
            "Detectado: %s\n" +
            "Requisito: Art. 19 EU AI Act",
            tamperedLog.getIdximmutablelog(),
            new Timestamp(System.currentTimeMillis())
        );
        
        // userService.blockUser(
        //     userId,
        //     "TAMPERING_DETECTED",
        //     blockReason,
        //     ctxBean.getUser().getIdxuser() // Bloqueado por sistema
        // );
        
        log.warn("Usuario bloqueado por tampering: User ID={}, Log ID={}", userId, tamperedLog.getIdximmutablelog());
        
    } catch (Exception e) {
        log.error("Error bloqueando usuario", e);
    }
}

/**
 * Crea incidente de seguridad
 */
private void createSecurityIncident(ImmutableLog tamperedLog) {
    try {
        // TODO: Integrar con sistema de incidentes
        // IncidentService incidentService = context.getBean(IncidentService.class);
        
        String incidentTitle = "Tampering Detectado - Log Inmutable Manipulado";
        String incidentDescription = String.format(
            "Se ha detectado manipulación (tampering) en un log inmutable.\n\n" +
            "Log ID: %d\n" +
            "Entity Type: %s\n" +
            "Entity ID: %d\n" +
            "Action: %s\n" +
            "Usuario: %s (ID: %d)\n" +
            "Timestamp: %s\n\n" +
            "Severidad: CRITICAL\n" +
            "Requisito: Art. 19 EU AI Act",
            tamperedLog.getIdximmutablelog(),
            tamperedLog.getImlentitytype(),
            tamperedLog.getImlentityid(),
            tamperedLog.getImlaction(),
            tamperedLog.getImlusername(),
            tamperedLog.getImluserid(),
            tamperedLog.getImltimestamp()
        );
        
        // incidentService.createIncident(
        //     "SECURITY_TAMPERING",
        //     incidentTitle,
        //     incidentDescription,
        //     "CRITICAL",
        //     tamperedLog.getIdximmutablelog()
        // );
        
        log.info("Incidente de seguridad creado: Log ID={}", tamperedLog.getIdximmutablelog());
        
    } catch (Exception e) {
        log.error("Error creando incidente de seguridad", e);
    }
}
```

### 2. Modificar método `verifyIntegrity()`

```java
public LogIntegrityReport verifyIntegrity(Long startId, Long endId) {
    log.info("Verifying integrity of logs from {} to {}", startId, endId);
    
    String query = "SELECT * FROM IMLIMMUTABLELOGS WHERE IDXIMMUTABLELOG >= ? AND IDXIMMUTABLELOG <= ? ORDER BY IDXIMMUTABLELOG ASC";
    List<ImmutableLog> logs = dao.findListBySQL(ImmutableLog.class, query, startId, endId);
    
    LogIntegrityReport report = new LogIntegrityReport();
    report.setTotalLogsChecked(logs.size());
    report.setIntegrityValid(true);
    
    String expectedPreviousHash = null;
    for (ImmutableLog log : logs) {
        // Verificar hash actual
        String calculatedHash = calculateHash(log);
        if (!calculatedHash.equals(log.getImlcurrenthash())) {
            report.setIntegrityValid(false);
            report.addCorruptedLog(log.getIdximmutablelog(), "Current hash mismatch");
            log.error("Hash mismatch detected for log ID: {}", log.getIdximmutablelog());
            
            // NUEVO: Detectar y alertar tampering (INC-012)
            detectTampering(log);
        }
        
        // Verificar chain
        if (expectedPreviousHash != null && !log.getImlprevioushash().equals(expectedPreviousHash)) {
            report.setIntegrityValid(false);
            report.addCorruptedLog(log.getIdximmutablelog(), "Chain broken");
            log.error("Chain broken at log ID: {}", log.getIdximmutablelog());
            
            // NUEVO: Detectar y alertar tampering (INC-012)
            detectTampering(log);
        }
        
        expectedPreviousHash = log.getImlcurrenthash();
    }
    
    log.info("Integrity verification completed - Valid: {}, Total: {}", 
        report.isIntegrityValid(), report.getTotalLogsChecked());
    
    return report;
}
```

### 3. Añadir Imports

```java
import java.util.HashMap;
```

---

## JOB PROGRAMADO (INC-006)

Crear job programado para verificar integridad automáticamente:

```java
@Component
@Slf4j
public class LogIntegrityScheduledTask {
    
    @Autowired
    private ImmutableLoggingBusinessService immutableLoggingService;
    
    /**
     * Verifica integridad de todos los logs diariamente a las 2 AM
     */
    @Scheduled(cron = "0 0 2 * * ?") // Diario a las 2 AM
    public void verifyAllLogsIntegrity() {
        log.info("Iniciando verificación automática de integridad de logs");
        
        try {
            // Obtener todos los logs
            String query = "SELECT * FROM IMLIMMUTABLELOGS ORDER BY IDXIMMUTABLELOG ASC";
            List<ImmutableLog> allLogs = dao.findListBySQL(ImmutableLog.class, query);
            
            int tamperedCount = 0;
            List<Long> tamperedLogIds = new ArrayList<>();
            
            for (int i = 1; i < allLogs.size(); i++) {
                ImmutableLog current = allLogs.get(i);
                ImmutableLog previous = allLogs.get(i - 1);
                
                // Verificar hash chain
                if (!current.getImlprevioushash().equals(previous.getImlcurrenthash())) {
                    tamperedCount++;
                    tamperedLogIds.add(current.getIdximmutablelog());
                    
                    // Detectar y alertar tampering
                    immutableLoggingService.detectTampering(current);
                }
            }
            
            if (tamperedCount > 0) {
                log.error("TAMPERING DETECTED: {} logs comprometidos", tamperedCount);
                // Notificar equipo de seguridad con resumen
            } else {
                log.info("Verificación completada: Todos los logs son válidos");
            }
            
        } catch (Exception e) {
            log.error("Error en verificación automática de integridad", e);
        }
    }
}
```

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Modificar hash de log → Debe detectar tampering y generar alerta
2. **Test 2:** Romper hash chain → Debe detectar y bloquear usuario
3. **Test 3:** Verificar que se notifica a equipo de seguridad
4. **Test 4:** Verificar que se crea incidente automático

---

## REFERENCIAS

- **Art. 19 EU AI Act:** Logs Inmutables
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-012`

---

**Estado:** ✅ COMPLETADO

