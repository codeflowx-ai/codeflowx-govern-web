# AuthorityNotificationService

**Ubicación:** `com.codeflowx.govern.business.compliance.AuthorityNotificationService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona la comunicación con autoridades competentes de la UE para registro de sistemas de IA según Art. 49.

---

## 🎯 Responsabilidades

- Notificar a autoridades sobre nuevos registros
- Consultar estado de registros en la autoridad
- Gestionar respuestas de autoridades

---

## ⚙️ Configuración

**Properties:**
- `eu.authority.api.url`: URL base de la API de autoridades (opcional)
- `eu.authority.api.key`: API key para autenticación (opcional)

**Modo Fallback:** Si no está configurado, funciona en modo simulado.

---

## 📚 API Pública

### `notifyAuthority(Long registrationId)`

Notifica a la autoridad sobre un registro.

**Parámetros:**
- `registrationId`: ID del registro

**Retorna:** `Map<String, Object>` con respuesta de la autoridad:
- `status`: "SUBMITTED", "PENDING", etc.
- `message`: Mensaje descriptivo
- `timestamp`: Fecha/hora de respuesta

**Ejemplo:**
```java
Map<String, Object> response = authorityService.notifyAuthority(registrationId);
String status = (String) response.get("status");
```

---

### `queryRegistrationStatus(Long registrationId)`

Consulta estado del registro en la autoridad.

**Retorna:** `Map<String, Object>` con estado actual:
- `status`: Estado ("DRAFT", "PENDING", "SUBMITTED", "REGISTERED", "REJECTED", "ERROR")
- `registrationId`: ID del registro
- `lastUpdated`: Última actualización

---

## 📖 Referencias

- **Art. 49 EU AI Act:** Registro y notificación
- **Prompt:** INC-020

---

**Última actualización:** 25 de noviembre de 2025
