# DataLakeCatalogService

**Ubicación:** `com.codeflowx.govern.business.integrations.DataLakeCatalogService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Servicio para catalogar datasets en data lakes (S3, Azure Blob, GCS) únicamente mediante metadata. No mueve datos, solo lee metadata y estructura.

---

## 🎯 Responsabilidades

- Catalogar datasets en S3
- Catalogar datasets en Azure Blob Storage
- Catalogar datasets en Google Cloud Storage
- Importar solo metadata (nombre, tamaño, fecha, estructura)

---

## 📚 API Pública

### `catalogS3(Long platformId)`

Catalogación de datasets en S3.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como S3

**Retorna:** `List<ExternalDataset>` - Datasets catalogados

**Ejemplo:**
```java
List<ExternalDataset> datasets = dataLakeService.catalogS3(platformId);
```

---

### `catalogAzureBlob(Long platformId)`

Catalogación de datasets en Azure Blob Storage.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como AZURE_BLOB

**Retorna:** `List<ExternalDataset>` - Datasets catalogados

---

### `catalogGCS(Long platformId)`

Catalogación de datasets en Google Cloud Storage.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como GCS

**Retorna:** `List<ExternalDataset>` - Datasets catalogados

---

## ⚙️ Configuración

### S3
**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `bucket`: Nombre del bucket
- `prefix`: Prefijo opcional
- `region`: Región AWS
- `accessKey`, `secretKey`: Credenciales AWS

### Azure Blob
- `connectionString`: Connection string de Azure Storage
- `container`: Nombre del contenedor
- `prefix`: Prefijo opcional

### GCS
- `projectId`: Google Cloud Project ID
- `bucket`: Nombre del bucket
- `prefix`: Prefijo opcional
- `serviceAccountKey`: JSON de service account (Base64)

---

## 📖 Referencias

- **Integración:** Usado por ExternalIntegrationBusinessService
- **Principio:** Solo metadata, nunca mover datos

---

**Última actualización:** 25 de noviembre de 2025
