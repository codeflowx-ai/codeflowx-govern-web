# 🔌 GUÍA DE INTEGRACIÓN CON CONECTORES - GOBIERNO DEL DATO

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Integration Developers, Backend Developers, Data Engineers

---

## 📋 INTRODUCCIÓN

Esta guía describe cómo desarrollar nuevas integraciones con conectores existentes o crear nuevos conectores para el módulo de Gobierno del Dato. Los conectores permiten acceder a diferentes fuentes de datos (bases de datos, data lakes, APIs, etc.) y catalogarlos como orígenes de datos.

---

## 🏗️ ARQUITECTURA DE CONECTORES

### Estructura Actual

Los conectores siguen el patrón **Provider** y se organizan en módulos Maven independientes:

```
codeflowx.govern.integrations.{provider}/
├── src/main/java/com/codeflowx/govern/integrations/{provider}/
│   └── {Provider}IntegrationProvider.java
├── pom.xml
└── README.md
```

### Conectores Existentes

| Conector | Módulo | Tipo | Estado |
|----------|--------|------|--------|
| PostgreSQL | `codeflowx.govern.integrations.postgresql` | Relacional | ✅ Implementado |
| MySQL | `codeflowx.govern.integrations.mysql` | Relacional | ✅ Implementado |
| MongoDB | `codeflowx.govern.integrations.mongodb` | NoSQL | ✅ Implementado |
| HuggingFace | `codeflowx.govern.integrations.huggingface` | ML Platform | ✅ Implementado |
| Kaggle | `codeflowx.govern.integrations.kaggle` | External | ✅ Implementado |
| S3 | `codeflowx.govern.integrations.s3` | Data Lake | ✅ Implementado |
| Azure Blob | `codeflowx.govern.integrations.azure-blob` | Data Lake | ✅ Implementado |
| GCS | `codeflowx.govern.integrations.gcs` | Data Lake | ✅ Implementado |
| Snowflake | `codeflowx.govern.integrations.snowflake` | Data Warehouse | ✅ Implementado |
| Databricks | `codeflowx.govern.integrations.databricks` | Data Warehouse | ✅ Implementado |
| BigQuery | `codeflowx.govern.integrations.bigquery` | Data Warehouse | ✅ Implementado |
| Redshift | `codeflowx.govern.integrations.redshift` | Data Warehouse | ✅ Implementado |
| SageMaker | `codeflowx.govern.integrations.sagemaker` | ML Platform | ✅ Implementado |
| Vertex AI | `codeflowx.govern.integrations.vertex-ai` | ML Platform | ✅ Implementado |
| Azure ML | `codeflowx.govern.integrations.azure-ml` | ML Platform | ✅ Implementado |
| MLflow | `codeflowx.govern.integrations.mlflow` | ML Platform | ✅ Implementado |
| Databricks ML | `codeflowx.govern.integrations.databricks-ml` | ML Platform | ✅ Implementado |
| Kubeflow | `codeflowx.govern.integrations.kubeflow` | ML Platform | ✅ Implementado |
| Seldon | `codeflowx.govern.integrations.seldon` | ML Platform | ✅ Implementado |
| WandB | `codeflowx.govern.integrations.wandb` | ML Platform | ✅ Implementado |
| HuggingFace | `codeflowx.govern.integrations.huggingface` | ML Platform | ✅ Implementado |
| Jira | `codeflowx.govern.integrations.jira` | External | ✅ Implementado |
| ServiceNow | `codeflowx.govern.integrations.servicenow` | External | ✅ Implementado |
| Zendesk | `codeflowx.govern.integrations.zendesk` | External | ✅ Implementado |
| Freshservice | `codeflowx.govern.integrations.freshservice` | External | ✅ Implementado |
| BMC Remedy | `codeflowx.govern.integrations.bmc-remedy` | External | ✅ Implementado |

---

## 🔧 INTERFAZ IntegrationProvider

Todos los conectores deben implementar la interfaz `IntegrationProvider`:

```java
public interface IntegrationProvider {

    /**
     * Identificador único del proveedor
     */
    String getProviderId();

    /**
     * Nombre legible del proveedor
     */
    String getProviderName();

    /**
     * Tipo de integración (DATABASE, DATA_LAKE, DATA_WAREHOUSE, ML_PLATFORM, EXTERNAL)
     */
    IntegrationType getType();

    /**
     * Tipos de autenticación soportados
     */
    List<AuthenticationType> getSupportedAuthenticationTypes();

    /**
     * Probar conexión con la configuración proporcionada
     */
    ConnectionTestResult testConnection(IntegrationContext context);

    /**
     * Catalogar datasets/tablas/colecciones disponibles
     */
    List<CatalogItem> catalogDatasets(IntegrationContext context);

    /**
     * Obtener esquema de un dataset específico
     */
    DatasetSchema getSchema(IntegrationContext context, String datasetId);

    /**
     * Sincronizar datos (opcional, según tipo)
     */
    SyncResult syncData(IntegrationContext context, String datasetId);
}
```

---

## 📝 CREAR NUEVO CONECTOR

### Paso 1: Crear Módulo Maven

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.codeflowx.govern</groupId>
        <artifactId>codeflowx.govern.integrations</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>codeflowx.govern.integrations.{provider}</artifactId>
    <name>CodeFlowX {Provider} Integration</name>

    <dependencies>
        <!-- Dependencias del conector específico -->
        <dependency>
            <groupId>com.{provider}</groupId>
            <artifactId>{provider}-client</artifactId>
            <version>X.X.X</version>
        </dependency>

        <!-- Dependencias comunes -->
        <dependency>
            <groupId>com.codeflowx.govern</groupId>
            <artifactId>codeflowx.govern.integrations.core</artifactId>
        </dependency>
    </dependencies>
</project>
```

### Paso 2: Implementar IntegrationProvider

```java
@Component
@Slf4j
@RequiredArgsConstructor
public class {Provider}IntegrationProvider implements IntegrationProvider {

    private static final String PROVIDER_ID = "{PROVIDER}";
    private static final String PROVIDER_NAME = "{Provider Name}";

    @Override
    public String getProviderId() {
        return PROVIDER_ID;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public IntegrationType getType() {
        return IntegrationType.DATABASE; // o DATA_LAKE, DATA_WAREHOUSE, etc.
    }

    @Override
    public List<AuthenticationType> getSupportedAuthenticationTypes() {
        return Arrays.asList(
            AuthenticationType.USERNAME_PASSWORD,
            AuthenticationType.OAUTH2
        );
    }

    @Override
    public ConnectionTestResult testConnection(IntegrationContext context) {
        try {
            // 1. Extraer configuración del contexto
            String host = context.getConfig("host");
            Integer port = context.getConfig("port");
            String database = context.getConfig("database");
            String username = context.getConfig("username");
            String password = context.getConfig("password");

            // 2. Crear conexión de prueba
            Connection connection = createConnection(host, port, database, username, password);

            // 3. Ejecutar query de prueba
            boolean isValid = connection.isValid(5);

            // 4. Cerrar conexión
            connection.close();

            return ConnectionTestResult.builder()
                .success(isValid)
                .message(isValid ? "Conexión exitosa" : "Conexión fallida")
                .build();

        } catch (Exception e) {
            log.error("Error probando conexión", e);
            return ConnectionTestResult.builder()
                .success(false)
                .message("Error: " + e.getMessage())
                .build();
        }
    }

    @Override
    public List<CatalogItem> catalogDatasets(IntegrationContext context) {
        try {
            // 1. Conectar a la fuente de datos
            Connection connection = createConnection(context);

            // 2. Obtener lista de tablas/colecciones/datasets
            List<CatalogItem> items = new ArrayList<>();

            // Ejemplo para base de datos relacional:
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(
                null, null, null, new String[]{"TABLE"}
            );

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                String schema = tables.getString("TABLE_SCHEMA");

                items.add(CatalogItem.builder()
                    .id(schema + "." + tableName)
                    .name(tableName)
                    .type("TABLE")
                    .schema(schema)
                    .build());
            }

            // 3. Cerrar conexión
            connection.close();

            return items;

        } catch (Exception e) {
            log.error("Error catalogando datasets", e);
            throw new IntegrationException("Error al catalogar: " + e.getMessage(), e);
        }
    }

    @Override
    public DatasetSchema getSchema(IntegrationContext context, String datasetId) {
        try {
            // 1. Conectar
            Connection connection = createConnection(context);

            // 2. Obtener esquema
            DatabaseMetaData metaData = connection.getMetaData();
            String[] parts = datasetId.split("\\.");
            String schema = parts.length > 1 ? parts[0] : null;
            String table = parts.length > 1 ? parts[1] : parts[0];

            ResultSet columns = metaData.getColumns(null, schema, table, null);

            List<ColumnSchema> columnSchemas = new ArrayList<>();
            while (columns.next()) {
                columnSchemas.add(ColumnSchema.builder()
                    .name(columns.getString("COLUMN_NAME"))
                    .type(columns.getString("TYPE_NAME"))
                    .nullable(columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable)
                    .size(columns.getInt("COLUMN_SIZE"))
                    .build());
            }

            connection.close();

            return DatasetSchema.builder()
                .datasetId(datasetId)
                .columns(columnSchemas)
                .build();

        } catch (Exception e) {
            log.error("Error obteniendo esquema", e);
            throw new IntegrationException("Error al obtener esquema: " + e.getMessage(), e);
        }
    }

    @Override
    public SyncResult syncData(IntegrationContext context, String datasetId) {
        // Implementar sincronización si aplica
        // Para bases de datos, puede ser snapshot o incremental
        return SyncResult.builder()
            .success(true)
            .recordsSynced(0)
            .build();
    }

    private Connection createConnection(IntegrationContext context) {
        // Implementar lógica de conexión específica
        return null;
    }
}
```

### Paso 3: Registrar el Conector

El conector se registra automáticamente mediante `@Component` de Spring. Asegúrate de que el paquete esté escaneado en la configuración principal.

---

## 🔄 MODIFICAR CONECTOR EXISTENTE

### Casos Comunes de Modificación

#### 1. Agregar Nuevo Tipo de Autenticación

```java
@Override
public List<AuthenticationType> getSupportedAuthenticationTypes() {
    List<AuthenticationType> types = new ArrayList<>();
    types.add(AuthenticationType.USERNAME_PASSWORD);
    types.add(AuthenticationType.OAUTH2);
    types.add(AuthenticationType.API_KEY); // Nuevo
    return types;
}

private Connection createConnection(IntegrationContext context) {
    AuthenticationType authType = context.getAuthenticationType();

    if (authType == AuthenticationType.API_KEY) {
        // Nueva lógica para API_KEY
        String apiKey = context.getConfig("apiKey");
        return createConnectionWithApiKey(apiKey);
    }
    // ... resto de la lógica
}
```

#### 2. Mejorar Catalogación

```java
@Override
public List<CatalogItem> catalogDatasets(IntegrationContext context) {
    // Agregar filtros
    String filter = context.getConfig("filter");

    // Agregar paginación
    Integer page = context.getConfig("page", 0);
    Integer size = context.getConfig("size", 100);

    // Agregar metadatos adicionales
    List<CatalogItem> items = getItems(filter, page, size);
    items.forEach(item -> {
        item.setMetadata(Map.of(
            "rowCount", getRowCount(item.getId()),
            "lastModified", getLastModified(item.getId())
        ));
    });

    return items;
}
```

#### 3. Agregar Sincronización Incremental

```java
@Override
public SyncResult syncData(IntegrationContext context, String datasetId) {
    String lastSync = context.getConfig("lastSyncTimestamp");

    if (lastSync != null) {
        // Sincronización incremental
        return syncIncremental(context, datasetId, lastSync);
    } else {
        // Sincronización completa
        return syncFull(context, datasetId);
    }
}
```

---

## 🔌 INTEGRACIÓN CON GOBIERNO DEL DATO

### Flujo de Integración

```
1. Usuario crea Integración en Frontend
   ↓
2. Frontend llama a POST /api/v1/governance/data/integrations
   ↓
3. Backend Java guarda en DtgIntegration (PostgreSQL)
   ↓
4. Usuario hace clic en "Explorar Schema"
   ↓
5. Frontend llama a GET /api/v1/governance/integrations/{id}/schema
   ↓
6. IntegrationGovernanceService obtiene IntegrationProvider
   ↓
7. IntegrationProvider.catalogDatasets() retorna lista
   ↓
8. Backend crea DataGovernanceOrigin por cada item
   ↓
9. Frontend muestra orígenes disponibles
```

### Servicio de Integración

El servicio `IntegrationGovernanceService` actúa como puente:

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class IntegrationGovernanceService {

    private final IntegrationOrchestratorService orchestratorService;
    private final DataGovernanceOriginService originService;

    public List<CatalogItem> getIntegrationSchema(Long integrationId) {
        // 1. Obtener integración
        DtgIntegration integration = integrationRepository.findById(integrationId)
            .orElseThrow();

        // 2. Obtener provider
        IntegrationProvider provider = orchestratorService
            .getProvider(integration.getDtgdatabasetype());

        // 3. Crear contexto
        IntegrationContext context = IntegrationContext.builder()
            .integrationId(integrationId)
            .config(extractConfig(integration))
            .authenticationType(integration.getDtgauthenticationtype())
            .build();

        // 4. Catalogar
        return provider.catalogDatasets(context);
    }

    public void catalogOrigins(Long integrationId) {
        List<CatalogItem> items = getIntegrationSchema(integrationId);

        // Crear orígenes
        for (CatalogItem item : items) {
            DataGovernanceOrigin origin = DataGovernanceOrigin.builder()
                .dtgname(item.getName())
                .dtgtype(item.getType())
                .dtgintegrationid(integrationId)
                .dtgpath(item.getId())
                .build();

            originService.createOrigin(origin);
        }
    }
}
```

---

## 📊 TIPOS DE CONECTORES POR CATEGORÍA

### Bases de Datos Relacionales

**Características:**
- Autenticación: USERNAME_PASSWORD, CERTIFICATE
- Catalogación: Tablas y vistas
- Esquema: Columnas con tipos SQL estándar
- Sincronización: SQL queries

**Ejemplo:** PostgreSQL, MySQL, SQL Server, Oracle

### Bases de Datos NoSQL

**Características:**
- Autenticación: USERNAME_PASSWORD, API_KEY
- Catalogación: Colecciones/Buckets
- Esquema: Documentos flexibles
- Sincronización: Queries específicas del motor

**Ejemplo:** MongoDB, Cassandra, Redis, Elasticsearch

### Data Lakes

**Características:**
- Autenticación: AWS_IAM, AZURE_AD, GCP_SERVICE_ACCOUNT
- Catalogación: Buckets/Containers y objetos
- Esquema: Inferido de archivos (Parquet, JSON, CSV)
- Sincronización: Descarga de archivos

**Ejemplo:** S3, Azure Blob, GCS, ADLS Gen2, MinIO

### Data Warehouses

**Características:**
- Autenticación: OAUTH2, API_KEY, AWS_IAM, GCP_SERVICE_ACCOUNT
- Catalogación: Databases, Schemas, Tables
- Esquema: Columnas con tipos específicos
- Sincronización: SQL queries o APIs

**Ejemplo:** Snowflake, Databricks, BigQuery, Redshift

### ML Platforms

**Características:**
- Autenticación: API_KEY, OAUTH2, AWS_IAM, AZURE_AD, GCP_SERVICE_ACCOUNT
- Catalogación: Datasets, Models, Experiments
- Esquema: Específico de la plataforma
- Sincronización: APIs REST

**Ejemplo:** SageMaker, Vertex AI, Azure ML, MLflow, Databricks ML

### External APIs

**Características:**
- Autenticación: API_KEY, OAUTH2, JWT
- Catalogación: Endpoints/Resources
- Esquema: JSON schemas
- Sincronización: API calls

**Ejemplo:** HuggingFace, Kaggle, APIs REST genéricas

---

## 📝 CHECKLIST DE DESARROLLO

### Crear Nuevo Conector

- [ ] Crear módulo Maven `codeflowx.govern.integrations.{provider}`
- [ ] Implementar `{Provider}IntegrationProvider`
- [ ] Implementar `testConnection()`
- [ ] Implementar `catalogDatasets()`
- [ ] Implementar `getSchema()`
- [ ] Implementar `syncData()` (si aplica)
- [ ] Agregar dependencias del cliente específico
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Documentación
- [ ] Registrar en frontend (si es nuevo tipo)

### Modificar Conector Existente

- [ ] Identificar funcionalidad a agregar
- [ ] Actualizar `getSupportedAuthenticationTypes()` (si aplica)
- [ ] Modificar método correspondiente
- [ ] Tests de regresión
- [ ] Actualizar documentación

---

## 🚀 PRÓXIMOS PASOS

1. **Identificar conectores faltantes:** Revisar lista de tipos soportados en frontend
2. **Priorizar desarrollo:** Empezar con conectores más usados
3. **Crear estructura base:** Template de conector para acelerar desarrollo
4. **Testing:** Tests automatizados para cada conector
5. **Documentación:** Documentar cada conector con ejemplos
6. **Integración:** Verificar integración con Gobierno del Dato

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** 📋 Planificación
