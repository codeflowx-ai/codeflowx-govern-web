# ImmutableLoggingBusinessService

**Ubicación:** `com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona sistema de logs inmutables con hash chains según Art. 19 del EU AI Act para trazabilidad completa e inmutable.

---

## ⚠️ REGLA CRÍTICA

**Solo INSERT permitido, NUNCA UPDATE ni DELETE.**

---

## 🎯 Responsabilidades

- Crear logs inmutables con hash SHA-256
- Mantener hash chains (cada log incluye hash del anterior)
- Verificar integridad de la cadena
- Detectar corrupción o manipulación

---

## 🏗️ Hash Chain

```
hash = SHA-256(previousHash + timestampEpoch + entityType + entityId + action + userId + data)
```

Primer log usa hash inicial: "0000...0000" (64 ceros)

---

## 📚 API Pública

### `createLogEntry(String entityType, Long entityId, String action, Long userId, String userName, Map<String, Object> data)`

Crea entrada de log inmutable.

**Parámetros:**
- `entityType`: Tipo de entidad (ej: "MODEL", "FRIA", "PROJECT")
- `entityId`: ID de la entidad
- `action`: Acción (ej: "CREATE", "UPDATE", "DEPLOY")
- `userId`, `userName`: Usuario que realiza la acción
- `data`: Datos adicionales (se serializa a JSON)

**Retorna:** `ImmutableLog` con hash calculado

---

### `verifyIntegrity(Long startId, Long endId)`

Verifica integridad de un rango de logs.

**Retorna:** `LogIntegrityReport` con:
- `totalLogsChecked`: Número de logs verificados
- `integrityValid`: Boolean
- `corruptedLogs`: Lista de logs corruptos

---

### `getEntityLogsWithVerification(String entityType, Long entityId)`

Obtiene logs de una entidad con verificación automática.

**Retorna:** `List<ImmutableLog>` ordenados por timestamp

---

### `detectTampering(ImmutableLog logEntry)` ⭐ NUEVO (INC-012)

Detecta tampering y genera alertas automáticas.

**Requisito:** Art. 19 EU AI Act - Logs Inmutables

**Funcionalidades:**
- Crea log inmutable de alerta con todos los detalles del tampering
- Genera alerta CRITICAL en el sistema
- Notifica al equipo de seguridad
- Bloquea automáticamente al usuario que causó el tampering
- Crea incidente automático de seguridad

**Se ejecuta automáticamente** cuando `verifyIntegrity()` detecta:
- Hash mismatch (hash calculado ≠ hash almacenado)
- Chain broken (previousHash no coincide con hash anterior)

**Ejemplo:**
```java
// Se ejecuta automáticamente en verifyIntegrity()
LogIntegrityReport report = immutableLoggingService.verifyIntegrity(startId, endId);
// Si detecta corrupción, detectTampering() se ejecuta automáticamente
```

---

## 📖 Referencias

- **Art. 19 EU AI Act:** Trazabilidad y logging
- **Prompts:** INC-006, INC-012, INC-018, INC-008-001
- **ViewModels:** TODOS los 91 ViewModels de Governance

---

**Última actualización:** 25 de noviembre de 2025
