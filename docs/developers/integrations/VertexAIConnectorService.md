# VertexAIConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.VertexAIConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector Google Vertex AI para sincronizar modelos sin mover datos (solo metadata). Integra modelos de Vertex AI con el catálogo de CodeflowX.

---

## 🎯 Responsabilidades

- Sincronizar modelos desde Vertex AI Model Registry
- Importar metadata de modelos (sin mover datos)
- Actualizar estado de sincronización

---

## 📚 API Pública

### `syncModels(Long platformId)`

Sincroniza modelos desde Vertex AI hacia CodeflowX.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como VERTEX_AI

**Retorna:** `List<ExternalModel>` - Modelos sincronizados

**Ejemplo:**
```java
List<ExternalModel> models = vertexService.syncModels(platformId);
```

---

## ⚙️ Configuración

**Property:** `govern.integrations.vertex.timeout-ms` (default: 30000)

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `projectId`: Google Cloud Project ID
- `location`: Región (ej: "us-central1")
- `serviceAccountKey`: JSON de service account (Base64)

---

## 📖 Referencias

- **Integración:** Usado por ExternalIntegrationBusinessService

---

**Última actualización:** 25 de noviembre de 2025
