# DatabricksConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.DatabricksConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector Databricks + MLflow Registry para sincronizar modelos con CodeflowX governance. Sincroniza metadata sin mover datos.

---

## 🎯 Responsabilidades

- Sincronizar modelos desde MLflow Registry
- Notificar estado de aprobación a Databricks
- Actualizar tags y stages en MLflow

---

## 📚 API Pública

### `syncModelsFromDatabricks(Long platformId)`

Sincroniza modelos registrados en Databricks MLflow Registry hacia CodeflowX.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como DATABRICKS

**Ejemplo:**
```java
databricksService.syncModelsFromDatabricks(platformId);
```

**Nota:** Solo sincroniza si `eplsyncenabled = true` en la plataforma.

---

### `notifyApprovalToDatabricks(Long externalModelId, String approvalStatus)`

Notifica estado de aprobación al Registry de Databricks usando tags y transición de stage.

**Parámetros:**
- `externalModelId`: ID del modelo externo
- `approvalStatus`: Estado de aprobación ("APPROVED", "REJECTED", etc.)

**Ejemplo:**
```java
databricksService.notifyApprovalToDatabricks(externalModelId, "APPROVED");
```

---

## ⚙️ Configuración

**Property:** `govern.integrations.databricks.timeout-ms` (default: 30000)

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `workspaceUrl`: URL del workspace Databricks
- `token`: Personal Access Token o OAuth token

---

## 📖 Referencias

- **Integración:** Usado por ExternalIntegrationBusinessService
- **MLflow:** https://mlflow.org/

---

**Última actualización:** 25 de noviembre de 2025
