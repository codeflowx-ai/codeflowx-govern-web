# 🔧 GUÍA TÉCNICA - FRAMEWORK DE INTEGRACIONES PARA DESARROLLADORES

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Java, Desarrolladores Junior, Agentes de IA

---

## 🎯 ¿QUÉ ES ESTE FRAMEWORK?

El Framework de Integraciones es un sistema modular que permite conectar CodeflowX con proveedores externos (bases de datos, data lakes, plataformas MLOps) **sin modificar el código central**.

### Concepto Clave: Plugin Pattern

Cada proveedor (Snowflake, Databricks, S3, etc.) es un **módulo independiente** (JAR) que se registra automáticamente. Es como instalar un plugin: lo agregas y funciona.

---

## 🏗️ ARQUITECTURA SIMPLIFICADA

```
┌─────────────────────────────────────┐
│  Servicios de Negocio              │
│  (código que usa las integraciones)│
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  IntegrationOrchestratorService     │
│  (busca el proveedor correcto)      │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  IntegrationProvider                │
│  (tu implementación específica)    │
│  - SnowflakeProvider                │
│  - DatabricksProvider               │
│  - S3Provider                       │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  IntegrationContext                 │
│  (guarda datos en la base)          │
└─────────────────────────────────────┘
```

### Componentes Principales

1. **`IntegrationProvider`** (Interface)
   - Define qué debe hacer cada proveedor
   - Métodos opcionales (puedes no implementarlos todos)

2. **`IntegrationOrchestratorService`** (Servicio)
   - Recibe peticiones
   - Busca el proveedor correcto según el tipo
   - Delega la ejecución

3. **`IntegrationContext`** (Interface)
   - Proporciona métodos para guardar/leer datos
   - Implementado en el módulo `business`

---

## 📦 ESTRUCTURA DE MÓDULOS

Cada proveedor es un **módulo Maven independiente**:

```
nocode-service/
├── codeflowx.govern.integrations.core/          ← Framework base
│   └── IntegrationProvider.java                 ← Interface principal
│   └── IntegrationOrchestratorService.java     ← Orquestador
│
├── codeflowx.govern.integrations.snowflake/    ← Proveedor Snowflake
│   └── SnowflakeIntegrationProvider.java        ← Tu implementación
│
├── codeflowx.govern.integrations.databricks/   ← Proveedor Databricks
│   └── DatabricksIntegrationProvider.java
│
└── codeflowx.govern.integrations.s3/            ← Proveedor S3
    └── S3IntegrationProvider.java
```

---

## 🚀 CÓMO CREAR UN NUEVO PROVEEDOR (Paso a Paso)

### Paso 1: Crear el Módulo Maven

Crea una carpeta nueva en `nocode-service/`:

```
codeflowx.govern.integrations.miproveedor/
├── pom.xml
└── src/main/java/com/codeflowx/govern/integrations/miproveedor/
    └── MiProveedorIntegrationProvider.java
```

### Paso 2: Configurar `pom.xml`

Copia el `pom.xml` de otro proveedor (ej: `snowflake`) y ajusta:

```xml
<artifactId>codeflowx.govern.integrations.miproveedor</artifactId>
<name>CodeflowX Mi Proveedor Integration Provider</name>
<description>Proveedor de integración para Mi Proveedor</description>
```

**Dependencias mínimas necesarias:**
- `codeflowx.govern.integrations.core` (el framework)
- `codeflowx.govern.nocode.entitys` (entidades JPA)
- Spring Boot Starter
- SDK del proveedor (si existe)
- Jackson (para JSON)
- Lombok (opcional, recomendado)

### Paso 3: Implementar la Clase del Proveedor

Crea una clase que implemente `IntegrationProvider`:

```java
package com.codeflowx.govern.integrations.miproveedor;

import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.codeflowx.govern.integrations.core.IntegrationContext;
import com.codeflowx.govern.integrations.core.IntegrationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component  // ← IMPORTANTE: Spring lo detectará automáticamente
@RequiredArgsConstructor
public class MiProveedorIntegrationProvider implements IntegrationProvider {

    private static final String PROVIDER_TYPE = "MI_PROVEEDOR";

    private final IntegrationContext context;

    @Override
    public String getProviderType() {
        return PROVIDER_TYPE;  // Debe coincidir con el tipo en la BD
    }

    @Override
    public boolean testConnection(ExternalPlatformIntegration platform) {
        // Implementa la prueba de conexión
        try {
            // Tu lógica aquí
            return true;
        } catch (Exception ex) {
            log.warn("Test conexión falló: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public List<ExternalDataset> catalogDatasets(
            Long platformId,
            ExternalPlatformIntegration platform) {
        // Implementa la catalogación
        List<ExternalDataset> datasets = new ArrayList<>();

        // Tu lógica para listar datasets
        // ...

        // Guarda cada dataset usando el context
        for (ExternalDataset dataset : datasets) {
            context.saveExternalDataset(dataset);
        }

        context.updateSyncStatus(platformId, "SUCCESS", null);
        return datasets;
    }
}
```

### Paso 4: Registrar el Módulo en el POM Principal

Abre `nocode-service/pom.xml` y agrega:

```xml
<module>codeflowx.govern.integrations.miproveedor</module>
```

### Paso 5: ¡Listo!

Spring detectará automáticamente tu `@Component` y lo registrará. No necesitas configuración adicional.

---

## 📋 INTERFACE `IntegrationProvider` - Métodos Disponibles

### Métodos Obligatorios

#### `String getProviderType()`
Retorna el tipo del proveedor en mayúsculas (ej: `"SNOWFLAKE"`, `"S3"`).
**Debe coincidir** con el valor en `ExternalPlatformIntegration.eplplatformtype`.

#### `boolean testConnection(ExternalPlatformIntegration platform)`
Prueba si la conexión funciona. Retorna `true` si es exitosa, `false` si falla.

### Métodos Opcionales (Default Methods)

Puedes implementar solo los que necesites. Los demás lanzan `UnsupportedOperationException`.

#### `catalogDatasets(Long platformId, ExternalPlatformIntegration platform)`
Lista y cataloga datasets desde el proveedor. Solo extrae **metadata**, no copia datos.

**Ejemplo:**
```java
@Override
public List<ExternalDataset> catalogDatasets(
        Long platformId,
        ExternalPlatformIntegration platform) {
    List<ExternalDataset> datasets = new ArrayList<>();

    // 1. Conectar al proveedor
    // 2. Listar datasets (tablas, archivos, etc.)
    // 3. Para cada dataset, crear ExternalDataset
    // 4. Guardar con context.saveExternalDataset()

    context.updateSyncStatus(platformId, "SUCCESS", null);
    return datasets;
}
```

#### `syncModels(Long platformId, ExternalPlatformIntegration platform)`
Sincroniza modelos de ML desde el proveedor (MLflow, Hugging Face, etc.).

#### `evaluateDatasetQuality(...)`
Evalúa calidad de un dataset: completitud, duplicados, PII.

**Nota sobre PII:** Usa el servicio `PiiDetectionService` que llama a microservicios Python con IA. No uses regex simple.

#### `executeQuery(...)`
Ejecuta queries dinámicas (SELECT, INSERT, UPDATE, DELETE).
**Importante:** Valida y sanitiza queries para prevenir SQL injection.

#### `cleanDataset(...)`
Limpia un dataset eliminando filas según condiciones.
**Importante:** Soporta `dryRun` para simular sin ejecutar.

#### `notifyApproval(...)`
Notifica a la plataforma externa cuando un modelo es aprobado/rechazado.

---

## 🔑 `IntegrationContext` - Métodos Disponibles

El `IntegrationContext` te da acceso a servicios comunes:

```java
// Buscar plataforma por ID
ExternalPlatformIntegration platform = context.findPlatformById(platformId);

// Actualizar estado de sincronización
context.updateSyncStatus(platformId, "SUCCESS", null);
context.updateSyncStatus(platformId, "ERROR", "Mensaje de error");

// Buscar dataset existente
ExternalDataset existing = context.findDatasetByExternalId(
    platformId, externalId);

// Guardar dataset (crea o actualiza)
ExternalDataset saved = context.saveExternalDataset(dataset);

// Lo mismo para modelos
ExternalModel model = context.findModelByExternalId(platformId, externalId);
ExternalModel saved = context.saveExternalModel(model);
```

---

## 📝 PATRONES Y MEJORES PRÁCTICAS

### 1. Validación de Plataforma

Siempre valida que la plataforma sea del tipo correcto:

```java
private void validatePlatform(ExternalPlatformIntegration platform) {
    if (platform == null || !PROVIDER_TYPE.equalsIgnoreCase(
            platform.getEplplatformtype())) {
        throw new IllegalArgumentException(
            "Plataforma incorrecta: esperado " + PROVIDER_TYPE);
    }
}
```

### 2. Resolución de Credenciales

Extrae credenciales de `platform.getEplmetadata()` (JSON) o campos directos:

```java
private Credentials resolveCredentials(ExternalPlatformIntegration platform) {
    JsonNode metadata = parseMetadata(platform.getEplmetadata());
    String host = metadata.path("host").asText();
    String user = platform.getEplclientid();
    String password = platform.getEplclientsecret();

    if (StringUtils.isAnyBlank(host, user, password)) {
        throw new IllegalStateException("Credenciales incompletas");
    }

    return new Credentials(host, user, password);
}
```

### 3. Upsert Pattern (Crear o Actualizar)

Siempre verifica si el dataset/modelo ya existe:

```java
private ExternalDataset upsertDataset(
        ExternalPlatformIntegration platform,
        Item item) {
    String externalId = buildExternalId(item);

    // Buscar existente
    ExternalDataset existing = context.findDatasetByExternalId(
        platform.getIdxexternalplatform(), externalId);

    boolean isNew = existing == null;
    ExternalDataset dataset = isNew ? new ExternalDataset() : existing;

    // Configurar campos
    if (isNew) {
        dataset.setPlatform(platform);
        dataset.setIduuid(UUID.randomUUID().toString());
        dataset.setExdcreatedat(Timestamp.from(Instant.now()));
    }

    dataset.setExdexternalid(externalId);
    dataset.setExdname(item.getName());
    dataset.setExdupdatedat(Timestamp.from(Instant.now()));

    // Guardar
    return context.saveExternalDataset(dataset);
}
```

### 4. Manejo de Errores

Siempre actualiza el estado de sincronización:

```java
try {
    // Tu lógica
    context.updateSyncStatus(platformId, "SUCCESS", null);
    return datasets;
} catch (Exception ex) {
    log.error("Error catalogando: {}", ex.getMessage(), ex);
    context.updateSyncStatus(platformId, "ERROR", ex.getMessage());
    throw new IllegalStateException("Error catalogando", ex);
}
```

### 5. Logging

Usa `@Slf4j` y log apropiado:

```java
log.info("Catalogación completada: {} datasets", datasets.size());
log.warn("Test conexión falló: {}", ex.getMessage());
log.error("Error crítico: {}", ex.getMessage(), ex);
```

---

## 🧪 TESTING

### Test de Conexión

```java
@Test
void testConnection() {
    ExternalPlatformIntegration platform = createTestPlatform();
    boolean result = provider.testConnection(platform);
    assertTrue(result);
}
```

### Test de Catalogación

```java
@Test
void catalogDatasets() {
    ExternalPlatformIntegration platform = createTestPlatform();
    List<ExternalDataset> datasets = provider.catalogDatasets(1L, platform);

    assertNotNull(datasets);
    assertFalse(datasets.isEmpty());
    // Verificar que se guardaron en BD
}
```

### Mock de IntegrationContext

```java
@Mock
private IntegrationContext context;

@InjectMocks
private MiProveedorIntegrationProvider provider;

@Test
void testCatalog() {
    when(context.findDatasetByExternalId(any(), any())).thenReturn(null);
    when(context.saveExternalDataset(any())).thenAnswer(i -> i.getArgument(0));

    // Ejecutar test
}
```

---

## 🐛 TROUBLESHOOTING COMÚN

### Problema: "Proveedor no encontrado"

**Causa:** El tipo en `getProviderType()` no coincide con `eplplatformtype` en BD.

**Solución:** Verifica que ambos valores sean idénticos (case-insensitive).

### Problema: "Spring no detecta mi componente"

**Causa:** Falta `@Component` o el módulo no está en el `pom.xml` principal.

**Solución:**
1. Verifica `@Component` en tu clase
2. Verifica que el módulo esté en `nocode-service/pom.xml`
3. Recompila el proyecto

### Problema: "Error de dependencias"

**Causa:** Falta dependencia en `pom.xml` o versión incorrecta.

**Solución:** Revisa `pom.xml` de proveedores similares y copia dependencias necesarias.

### Problema: "Connection timeout"

**Causa:** Credenciales incorrectas o red bloqueada.

**Solución:**
1. Verifica `testConnection()` primero
2. Revisa credenciales en `eplmetadata` o campos directos
3. Verifica firewall/proxy

---

## 📦 INTEGRACIONES DISPONIBLES

### Data Lakes / Object Storage

#### AWS S3
**Módulo:** `codeflowx.govern.integrations.s3`
**Tipo:** `S3`
**Dependencia principal:**
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de objetos
**Ejemplo de uso:**
```java
// Configuración en metadata:
{
  "bucket": "mi-bucket",
  "region": "us-east-1",
  "prefix": "datasets/"
}
// Credenciales: eplclientid (accessKey), eplclientsecret (secretKey)
```

#### Azure Blob Storage
**Módulo:** `codeflowx.govern.integrations.azure-blob`
**Tipo:** `AZURE_BLOB`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-storage-blob</artifactId>
    <version>12.22.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de blobs
**Ejemplo de uso:**
```java
// Configuración en metadata:
{
  "connectionString": "DefaultEndpointsProtocol=https;AccountName=...",
  "container": "mi-container",
  "prefix": "datasets/"
}
```

#### Google Cloud Storage (GCS)
**Módulo:** `codeflowx.govern.integrations.gcs`
**Tipo:** `GCS`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-storage</artifactId>
    <version>2.30.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de objetos
**Ejemplo de uso:**
```java
// Configuración en metadata:
{
  "projectId": "mi-proyecto",
  "bucket": "mi-bucket",
  "prefix": "datasets/",
  "credentialsJson": "{...}" // Service account JSON
}
```

#### Azure Data Lake Storage Gen2
**Módulo:** `codeflowx.govern.integrations.adls-gen2`
**Tipo:** `ADLS_GEN2`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-storage-file-datalake</artifactId>
    <version>12.22.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de archivos (Hadoop-compatible)

#### Hadoop HDFS
**Módulo:** `codeflowx.govern.integrations.hdfs`
**Tipo:** `HDFS`
**Dependencia principal:**
```xml
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-client-api</artifactId>
    <version>3.3.6</version>
</dependency>
```
**Funcionalidades:** Catalogación de archivos HDFS

#### IBM Cloud Object Storage
**Módulo:** `codeflowx.govern.integrations.ibm-cos`
**Tipo:** `IBM_COS`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.ibm.cloud</groupId>
    <artifactId>ibm-cos-java-sdk</artifactId>
    <version>2.10.0</version>
</dependency>
```
**Funcionalidades:** Catalogación (S3-compatible)

#### Oracle Cloud Infrastructure Object Storage
**Módulo:** `codeflowx.govern.integrations.oci-object-storage`
**Tipo:** `OCI_OBJECT_STORAGE`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.oracle.oci.sdk</groupId>
    <artifactId>oci-java-sdk-objectstorage</artifactId>
    <version>3.0.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de objetos

#### MinIO
**Módulo:** `codeflowx.govern.integrations.minio`
**Tipo:** `MINIO`
**Dependencia principal:**
```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.7</version>
</dependency>
```
**Funcionalidades:** Catalogación (S3-compatible)

---

### Bases de Datos Relacionales

#### Snowflake
**Módulo:** `codeflowx.govern.integrations.snowflake`
**Tipo:** `SNOWFLAKE`
**Dependencia principal:**
```xml
<dependency>
    <groupId>net.snowflake</groupId>
    <artifactId>snowflake-jdbc</artifactId>
    <version>3.14.0</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection
**Ejemplo de uso:**
```java
// Configuración en metadata:
{
  "account": "mi-account",
  "warehouse": "COMPUTE_WH",
  "database": "MI_DB",
  "schemas": ["PUBLIC", "ANALYTICS"]
}
// Credenciales: eplclientid (user), eplclientsecret (password)
```

#### PostgreSQL
**Módulo:** `codeflowx.govern.integrations.postgresql`
**Tipo:** `POSTGRESQL`
**Dependencia principal:**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection

#### MySQL
**Módulo:** `codeflowx.govern.integrations.mysql`
**Tipo:** `MYSQL`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.2.0</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection

#### Microsoft SQL Server
**Módulo:** `codeflowx.govern.integrations.sqlserver`
**Tipo:** `SQL_SERVER`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.4.2.jre11</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection

#### Oracle Database
**Módulo:** `codeflowx.govern.integrations.oracle`
**Tipo:** `ORACLE`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <version>23.2.0.0</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection
**Ejemplo de uso:**
```java
// Configuración en metadata:
{
  "host": "localhost",
  "port": "1521",
  "sid": "ORCL",  // O usar "serviceName" en lugar de "sid"
  "serviceName": "ORCL"
}
```

#### IBM DB2 (LUW)
**Módulo:** `codeflowx.govern.integrations.db2`
**Tipo:** `DB2`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.ibm.db2</groupId>
    <artifactId>jcc</artifactId>
    <version>11.5.8.0</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection

#### IBM DB2 for z/OS (Mainframe)
**Módulo:** `codeflowx.govern.integrations.db2-zos`
**Tipo:** `DB2_ZOS`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.ibm.db2</groupId>
    <artifactId>jcc</artifactId>
    <version>11.5.8.0</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection
**Ejemplo de uso (específico para mainframe):**
```java
// Configuración en metadata:
{
  "host": "mainframe.example.com",
  "port": "446",
  "locationName": "DB2A"  // Nombre del subsistema DB2 en z/OS
}
```

---

### Bases de Datos Cloud

#### Google BigQuery
**Módulo:** `codeflowx.govern.integrations.bigquery`
**Tipo:** `BIGQUERY`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-bigquery</artifactId>
    <version>2.30.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de datasets y tablas

#### AWS Redshift
**Módulo:** `codeflowx.govern.integrations.redshift`
**Tipo:** `REDSHIFT`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.amazon.redshift</groupId>
    <artifactId>redshift-jdbc42</artifactId>
    <version>2.1.0.28</version>
</dependency>
```
**Funcionalidades:** Catalogación, evaluación de calidad, queries dinámicas, limpieza, PII detection

---

### Bases de Datos NoSQL

#### MongoDB
**Módulo:** `codeflowx.govern.integrations.mongodb`
**Tipo:** `MONGODB`
**Dependencia principal:**
```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.11.1</version>
</dependency>
```
**Funcionalidades:** Catalogación de colecciones

#### Apache Cassandra
**Módulo:** `codeflowx.govern.integrations.cassandra`
**Tipo:** `CASSANDRA`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.datastax.oss</groupId>
    <artifactId>java-driver-core</artifactId>
    <version>4.17.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de keyspaces y tablas

#### AWS DynamoDB
**Módulo:** `codeflowx.govern.integrations.dynamodb`
**Tipo:** `DYNAMODB`
**Dependencia principal:**
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>dynamodb</artifactId>
    <version>2.20.0</version>
</dependency>
```
**Funcionalidades:** Catalogación de tablas

---

### Plataformas MLOps

#### Databricks
**Módulo:** `codeflowx.govern.integrations.databricks`
**Tipo:** `DATABRICKS`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.databricks</groupId>
    <artifactId>databricks-sdk-java</artifactId>
    <version>0.30.0</version>
</dependency>
```
**Funcionalidades:** Catalogación (Unity Catalog), sincronización de modelos (MLflow), notificación de aprobaciones
**Ejemplo de uso:**
```java
// Configuración:
// eplhosturl: "https://mi-workspace.cloud.databricks.com"
// eplapitoken: "dapi..."
```

#### AWS SageMaker
**Módulo:** `codeflowx.govern.integrations.sagemaker`
**Tipo:** `SAGEMAKER`
**Dependencia principal:**
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>sagemaker</artifactId>
    <version>2.20.0</version>
</dependency>
```
**Funcionalidades:** Sincronización de modelos, notificación de aprobaciones
**Ejemplo de uso:**
```java
// Configuración en metadata:
{
  "region": "us-east-1"
}
// Credenciales: eplclientid (accessKey), eplclientsecret (secretKey)
```

#### Google Vertex AI
**Módulo:** `codeflowx.govern.integrations.vertexai`
**Tipo:** `VERTEX_AI`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-aiplatform</artifactId>
    <version>3.0.0</version>
</dependency>
```
**Funcionalidades:** Sincronización de modelos, notificación de aprobaciones
**Ejemplo de uso:**
```java
// Configuración:
// eplprojectid: "mi-proyecto"
// eplregion: "us-central1"
// eplcredentialjson: "{...}" // Service account JSON
```

#### Azure Machine Learning
**Módulo:** `codeflowx.govern.integrations.azure-ml`
**Tipo:** `AZURE_ML`
**Dependencia principal:**
```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-ai-ml</artifactId>
    <version>1.0.0</version>
</dependency>
```
**Funcionalidades:** Sincronización de modelos, notificación de aprobaciones
**Ejemplo de uso:**
```java
// Configuración en metadata:
{
  "subscriptionId": "...",
  "resourceGroup": "...",
  "workspace": "..."
}
// Credenciales: epltenantid, eplclientid, eplclientsecret (Service Principal)
```

#### MLflow Standalone
**Módulo:** `codeflowx.govern.integrations.mlflow`
**Tipo:** `MLFLOW`
**Dependencia principal:**
```xml
<!-- Usa WebClient (Spring WebFlux) - ya incluido en spring-boot-starter-webflux -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```
**Funcionalidades:** Sincronización de modelos, notificación de aprobaciones
**Ejemplo de uso:**
```java
// Configuración:
// eplhosturl: "http://mlflow-server:5000"
// eplapitoken: "..." (opcional)
```

#### Hugging Face Hub
**Módulo:** `codeflowx.govern.integrations.huggingface`
**Tipo:** `HUGGING_FACE`
**Dependencia principal:**
```xml
<!-- Usa WebClient (Spring WebFlux) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```
**Funcionalidades:** Sincronización de modelos y datasets, filtros por organización/licencia/tarea
**Ejemplo de uso:**
```java
// Configuración:
// eplapitoken: "hf_..." (opcional, para acceso privado)
// eplorganization: "nombre-org" (opcional, filtrar por autor)
// Metadata adicional:
{
  "task": "text-classification",  // Filtro por tarea
  "license": "apache-2.0",        // Filtro por licencia
  "sort": "downloads",            // Ordenar por descargas
  "direction": "descending"       // Dirección del orden
}
```

#### Kubeflow Pipelines
**Módulo:** `codeflowx.govern.integrations.kubeflow`
**Tipo:** `KUBEFLOW`
**Dependencia principal:**
```xml
<!-- Usa WebClient (Spring WebFlux) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```
**Funcionalidades:** Catalogación de pipelines y runs
**Ejemplo de uso:**
```java
// Configuración:
// eplhosturl: "https://kubeflow.example.com"
// eplapitoken: "..." (opcional)
```

#### Seldon Core
**Módulo:** `codeflowx.govern.integrations.seldon`
**Tipo:** `SELDON`
**Dependencia principal:**
```xml
<!-- Usa WebClient (Spring WebFlux) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```
**Funcionalidades:** Catalogación de deployments/endpoints, ejecución de inferencias
**Ejemplo de uso:**
```java
// Configuración:
// eplhosturl: "https://seldon.example.com"
// eplapitoken: "..." (opcional)

// Ejecutar inferencia:
QueryResult result = orchestrator.executeQuery(
    platformId, platform,
    "deployment:namespace/name",  // Query especial para inferencia
    Map.of("payload", jsonPayload),
    null, false
);
```

#### Weights & Biases (W&B)
**Módulo:** `codeflowx.govern.integrations.wandb`
**Tipo:** `WANDB`
**Dependencia principal:**
```xml
<!-- Usa WebClient (Spring WebFlux) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```
**Funcionalidades:** Sincronización de runs/experimentos con métricas
**Ejemplo de uso:**
```java
// Configuración:
// eplapitoken: "..." (API key de W&B)
// eplorganization: "mi-org" (entity/organización)
```

---

## 📚 EJEMPLOS DE REFERENCIA

### Proveedor Simple (S3, Azure Blob)

Ver: `codeflowx.govern.integrations.s3.S3IntegrationProvider`

- Solo catalogación de objetos
- Sin evaluación de calidad
- Sin queries dinámicas

### Proveedor Completo (Snowflake)

Ver: `codeflowx.govern.integrations.snowflake.SnowflakeIntegrationProvider`

- Catalogación
- Evaluación de calidad
- Queries dinámicas
- Limpieza de datasets
- Detección de PII

### Proveedor MLOps (Databricks)

Ver: `codeflowx.govern.integrations.databricks.DatabricksIntegrationProvider`

- Catalogación de datasets (Unity Catalog)
- Sincronización de modelos (MLflow)
- Notificación de aprobaciones
- Usa SDK oficial

---

## ✅ CHECKLIST PARA NUEVO PROVEEDOR

- [ ] Módulo Maven creado con estructura correcta
- [ ] `pom.xml` configurado con dependencias necesarias
- [ ] Clase implementa `IntegrationProvider`
- [ ] Anotada con `@Component`
- [ ] `getProviderType()` retorna valor correcto
- [ ] `testConnection()` implementado
- [ ] Métodos opcionales implementados según necesidad
- [ ] Validación de plataforma implementada
- [ ] Manejo de errores con `updateSyncStatus`
- [ ] Logging apropiado
- [ ] Módulo agregado a `pom.xml` principal
- [ ] Tests básicos creados

---

## 🔗 RECURSOS ADICIONALES

- **Código de referencia:** `codeflowx.govern.integrations.snowflake`
- **Framework base:** `codeflowx.govern.integrations.core`
- **Documentación funcional:** `GUIA_FUNCIONAL_INTEGRACIONES.md`

---

**Última Actualización:** Diciembre 2025
**Versión del Framework:** 1.0
**Estado:** ✅ Operativo y listo para desarrollo
