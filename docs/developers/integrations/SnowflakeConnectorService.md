# SnowflakeConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.SnowflakeConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector Snowflake para catalogación y evaluación de calidad sin mover datos (solo metadata / samples controlados).

---

## 🎯 Responsabilidades

- Catalogar datasets desde INFORMATION_SCHEMA
- Evaluar calidad de datos con samples controlados
- Detectar PII (Personally Identifiable Information)
- Calcular estadísticas básicas sin copiar datos

---

## 📚 API Pública

### `catalogDatasets(Long platformId)`

Catalogación de datasets Snowflake (INFORMATION_SCHEMA.TABLES) sin copiar datos.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como SNOWFLAKE

**Retorna:** `List<ExternalDataset>` - Datasets catalogados

**Ejemplo:**
```java
List<ExternalDataset> datasets = snowflakeService.catalogDatasets(platformId);
```

---

### `evaluateDataQuality(Long platformId, String schema, String tableName)`

Evalúa calidad de datos usando sample controlado.

**Parámetros:**
- `platformId`: ID de la plataforma
- `schema`: Schema de la tabla
- `tableName`: Nombre de la tabla

**Retorna:** `DataQualityReport` con:
- `totalRows`: Número total de filas
- `nullCounts`: Conteo de nulos por columna
- `piiDetected`: Columnas con PII detectado
- `qualityScore`: Score de calidad (0.0 - 1.0)

---

## ⚙️ Configuración

**Property:** `govern.integrations.snowflake.sample-size` (default: 1000)

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `account`: Snowflake account identifier
- `warehouse`: Warehouse name
- `database`: Database name
- `schemas`: Lista de schemas a catalogar
- `username`, `password`: Credenciales

---

## 📖 Referencias

- **Integración:** Usado por ExternalIntegrationBusinessService

---

**Última actualización:** 25 de noviembre de 2025
