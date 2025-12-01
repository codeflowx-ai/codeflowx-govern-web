# FabricConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.FabricConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector Microsoft Fabric para sincronizar workspaces, lakehouses y pipelines como datasets gobernados.

---

## 🎯 Responsabilidades

- Sincronizar workspaces de Fabric
- Catalogar lakehouses y pipelines
- Importar metadata sin mover datos

---

## 📚 API Pública

### `syncFabricAssets(Long platformId)`

Sincroniza activos de Fabric (workspaces, lakehouses, pipelines).

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como FABRIC

**Retorna:** `List<ExternalDataset>` - Assets sincronizados

**Tipos de assets:**
- `FABRIC_WORKSPACE`: Workspaces
- `FABRIC_LAKEHOUSE`: Lakehouses
- `FABRIC_PIPELINE`: Pipelines

---

## ⚙️ Configuración

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `apiBase`: URL base de la API de Fabric
- `tenantId`: Azure Tenant ID
- `clientId`, `clientSecret`: Para autenticación

---

## 📖 Referencias

- **Microsoft Fabric:** https://www.microsoft.com/microsoft-fabric
- **Integración:** Usado por ExternalIntegrationBusinessService

---

**Última actualización:** 25 de noviembre de 2025
