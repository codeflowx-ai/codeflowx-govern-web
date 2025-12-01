# PurviewConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.PurviewConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector Microsoft Purview para consumir catálogo y linaje sin duplicar datos. Sincroniza activos del catálogo Purview como datasets externos.

---

## 🎯 Responsabilidades

- Sincronizar activos del catálogo Purview
- Obtener linaje de datos
- Catalogar datasets sin mover datos

---

## 📚 API Pública

### `syncCatalog(Long platformId)`

Sincroniza activos del catálogo Purview como datasets externos.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como PURVIEW

**Retorna:** `List<ExternalDataset>` - Datasets sincronizados

**Ejemplo:**
```java
List<ExternalDataset> datasets = purviewService.syncCatalog(platformId);
```

---

### `testConnection(ExternalPlatformIntegration platform)`

Prueba conexión con Purview.

**Retorna:** `Boolean` - true si la conexión es exitosa

---

## ⚙️ Configuración

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `accountName`: Nombre de la cuenta Purview
- `tenantId`: Azure Tenant ID
- `clientId`, `clientSecret`: Para autenticación

---

## 📖 Referencias

- **Microsoft Purview:** https://azure.microsoft.com/services/purview/
- **Integración:** Usado por ExternalIntegrationBusinessService

---

**Última actualización:** 25 de noviembre de 2025
