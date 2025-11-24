# PROMPT: INC-019 - Notificaciones Vencimientos

**Incidencia:** INC-019  
**Prioridad:** 🟢 BAJA  
**Artículo EU AI Act:** Art. 27, Art. 49  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## IMPLEMENTACIÓN

```java
@Scheduled(cron = "0 0 9 * * ?") // Diario a las 9 AM
public void checkExpiringFrias() {
    List<FriaAssessment> expiringSoon = friaService.findExpiringInDays(30);
    
    for (FriaAssessment fria : expiringSoon) {
        notificationService.sendNotification(
            fria.getDeployerUser(),
            "FRIA próximo a vencer",
            "FRIA ID: " + fria.getIdxfriaassessment() + 
            " vence en " + getDaysUntilExpiry(fria) + " días"
        );
    }
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-019`

---

**Estado:** ✅ COMPLETADO

