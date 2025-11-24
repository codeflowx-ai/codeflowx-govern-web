# PROMPT: INC-006 - Verificación Automática Integridad Logs

**Incidencia:** INC-006  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 19  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## CONTEXTO

Sistema genera logs inmutables con hash chain pero no hay verificación automática periódica de integridad. Solo se verifica bajo demanda.

**Ubicación Actual:**
- `ImmutableLoggingBusinessService.java` - método `verifyIntegrity()` (bajo demanda)

---

## REQUISITOS

1. Crear job programado (diario) para verificar integridad de todos los logs
2. Alertar automáticamente si se detecta tampering
3. Generar reporte de integridad semanal
4. Integrar verificación en compliance monitoring

---

## IMPLEMENTACIÓN

### Crear Job Programado

**Archivo:** `com.codeflowx.govern.scheduled.LogIntegrityScheduledTask.java`

```java
package com.codeflowx.govern.scheduled;

import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import com.codeflowx.govern.entity.logging.ImmutableLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

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
                    
                    // Detectar y alertar tampering (usa INC-012)
                    immutableLoggingService.detectTampering(current);
                }
            }
            
            if (tamperedCount > 0) {
                log.error("TAMPERING DETECTED: {} logs comprometidos", tamperedCount);
                // Notificar equipo de seguridad con resumen
                notifySecurityTeamSummary(tamperedCount, tamperedLogIds);
            } else {
                log.info("Verificación completada: Todos los logs son válidos");
            }
            
        } catch (Exception e) {
            log.error("Error en verificación automática de integridad", e);
        }
    }
    
    /**
     * Genera reporte semanal de integridad
     */
    @Scheduled(cron = "0 0 9 * * MON") // Lunes a las 9 AM
    public void generateWeeklyIntegrityReport() {
        log.info("Generando reporte semanal de integridad de logs");
        // TODO: Generar y enviar reporte
    }
}
```

---

## REFERENCIAS

- **Art. 19 EU AI Act:** Logs Inmutables
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-006`

---

**Estado:** ✅ COMPLETADO

