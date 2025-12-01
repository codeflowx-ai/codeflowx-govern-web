# IbmWatsonxConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.IbmWatsonxConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector IBM watsonx.ai / Watson Machine Learning para sincronizar modelos con CodeflowX governance.

---

## 🎯 Responsabilidades

- Sincronizar modelos desde watsonx.ai
- Importar metadata de modelos
- Actualizar estado de sincronización

---

## 📚 API Pública

### `syncModels(Long platformId)`

Sincroniza modelos desde IBM watsonx hacia CodeflowX.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como IBM_WATSONX

**Retorna:** `List<ExternalModel>` - Modelos sincronizados

**Ejemplo:**
```java
List<ExternalModel> models = watsonxService.syncModels(platformId);
```

---

## ⚙️ Configuración

**Property:** `govern.integrations.ibm.timeout-ms` (default: 30000)

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `instanceUrl`: URL de la instancia watsonx
- `instanceId`: ML Instance ID
- `projectId`: Project ID
- `apiVersion`: Versión de API
- `apiKey`: API Key para IAM

---

## 📖 Referencias

- **IBM watsonx.ai:** https://www.ibm.com/products/watsonx-ai
- **Integración:** Usado por ExternalIntegrationBusinessService

---

**Última actualización:** 25 de noviembre de 2025
