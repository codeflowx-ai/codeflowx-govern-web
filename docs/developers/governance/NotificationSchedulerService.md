# NotificationSchedulerService

**Ubicación:** `com.codeflowx.govern.business.governance.NotificationSchedulerService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona programación automática de notificaciones para recordar vencimientos inminentes de elementos críticos del sistema.

---

## 🎯 Responsabilidades

- Programar notificaciones con anticipación configurable
- Consultar notificaciones programadas pendientes
- Procesar notificaciones cuando llega su fecha/hora
- Cancelar notificaciones programadas

---

## 📚 API Pública

### `scheduleExpirationNotification(Long userId, String notificationType, Timestamp dueDate, Integer daysBefore)`

Programa notificación de vencimiento.

**Parámetros:**
- `userId`: ID del usuario destinatario
- `notificationType`: Tipo (ej: "FRIA", "EU_REGISTRATION")
- `dueDate`: Fecha de vencimiento
- `daysBefore`: Días antes del vencimiento para notificar

**Ejemplo:**
```java
notificationService.scheduleExpirationNotification(
    userId, "FRIA", dueDate, 7); // Notificar 7 días antes
```

---

### `getPendingScheduledNotifications()`

Obtiene notificaciones programadas pendientes de enviar.

**Retorna:** `List<Notification>` ordenadas por fecha programada

---

### `processPendingNotifications()`

Procesa notificaciones programadas pendientes.

**Retorna:** `int` - Número de notificaciones procesadas

**Uso:** Ejecutar periódicamente (job scheduler)

---

### `cancelScheduledNotification(Long notificationId)`

Cancela notificación programada.

---

## 📖 Referencias

- **Prompt:** INC-019
- **ViewModels:** Todos los ViewModels con notificaciones

---

**Última actualización:** 25 de noviembre de 2025
