# ExternalIntegrationBusinessService

**Ubicación:** `com.codeflowx.govern.business.integrations.ExternalIntegrationBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona integraciones con plataformas externas, modelos externos y datasets externos. Proporciona acceso unificado a recursos externos.

---

## 🎯 Responsabilidades

- Gestionar plataformas externas (crear, actualizar, consultar)
- Gestionar modelos externos
- Gestionar datasets externos
- Actualizar estados de sincronización

---

## 📚 API Pública

### Plataformas

#### `findAllPlatforms()`

Obtiene todas las plataformas externas.

**Retorna:** `List<ExternalPlatformIntegration>`

---

#### `findPlatformsByType(String type)`

Obtiene plataformas por tipo.

**Parámetros:**
- `type`: Tipo de plataforma

---

#### `findPlatformById(Long platformId)`

Obtiene plataforma por ID.

---

#### `savePlatform(ExternalPlatformIntegration platform)`

Guarda o actualiza plataforma.

---

#### `updateSyncStatus(Long platformId, String status, String errorMessage)`

Actualiza estado de sincronización de plataforma.

**Parámetros:**
- `platformId`: ID de la plataforma
- `status`: Estado ("SYNCED", "SYNCING", "ERROR", etc.)
- `errorMessage`: Mensaje de error si aplica

---

### Modelos Externos

#### `findAllExternalModels()`

Obtiene todos los modelos externos.

**Retorna:** `List<ExternalModel>`

---

#### `findExternalModelsByPlatform(Long platformId)`

Obtiene modelos externos por plataforma.

---

### Datasets Externos

#### `findAllExternalDatasets()`

Obtiene todos los datasets externos.

**Retorna:** `List<ExternalDataset>`

---

## 📖 Referencias

- **ViewModels:** ExternalPlatformsViewModel, ProvidersOverviewViewModel, ProvidersDetailViewModel

---

**Última actualización:** 25 de noviembre de 2025
