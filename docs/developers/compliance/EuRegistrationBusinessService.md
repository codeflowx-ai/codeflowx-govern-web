# EuRegistrationBusinessService

**Ubicación:** `com.codeflowx.govern.business.compliance.EuRegistrationBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona el registro obligatorio de sistemas de IA de alto riesgo en la Base de Datos Europea según Art. 49 del EU AI Act.

---

## 🎯 Responsabilidades

- Crear registros (draft) asociados a proyectos
- Actualizar datos de submission (formulario Anexo VIII)
- Enviar registros a Base de Datos Europea
- Reintentar submissions fallidos con exponential backoff
- Gestionar registros sensibles y nacionales

---

## 📚 API Pública

### `createRegistration(Long projectId, String registrationType)`

Crea nuevo registro en estado DRAFT.

**Parámetros:**
- `projectId`: ID del proyecto
- `registrationType`: Tipo de registro (HIGH_RISK, etc.)

**Retorna:** `EuRegistration`

---

### `updateSubmissionData(Long registrationId, Map<String, Object> submissionData)`

Actualiza datos de submission (formato Anexo VIII).

**Parámetros:**
- `registrationId`: ID del registro
- `submissionData`: Map con datos del formulario (se serializa a JSON)

---

### `submitToEuDatabase(Long registrationId)`

Envía registro a Base de Datos Europea.

**Validaciones:**
- Verifica que submission data esté completo
- Actualmente simula el envío (requiere integración real con API UE)

**Estados:** DRAFT → SUBMITTED → REGISTERED o ERROR

---

### `retrySubmission(Long registrationId)`

Reintenta submission con exponential backoff.

**Lógica:** delay = 2^intentos minutos, máximo 5 intentos

---

### `markAsSensitive(Long registrationId, boolean isSensitive)`

Marca registro como sensible (Art. 49.4).

---

### `configureNationalRegistration(Long registrationId, String nationalRegistrationId)`

Configura registro nacional adicional (Art. 49.5).

---

## 💡 Casos de Uso

```java
// Crear registro
EuRegistration reg = euService.createRegistration(projectId, "HIGH_RISK");

// Completar datos
Map<String, Object> data = new HashMap<>();
data.put("systemName", "AI System");
data.put("provider", "Provider Name");
euService.updateSubmissionData(reg.getIdxeuregistration(), data);

// Enviar
euService.submitToEuDatabase(reg.getIdxeuregistration());

// Verificar estado
String status = euService.checkSubmissionStatus(reg.getIdxeuregistration());
```

---

## 📖 Referencias

- **Art. 49 EU AI Act:** Registro en Base de Datos Europea
- **Anexo VIII:** Datos de submission
- **Prompts:** INC-020, INC-010-005

---

**Última actualización:** 25 de noviembre de 2025
