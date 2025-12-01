# SageMakerConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.SageMakerConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector AWS SageMaker para monitoreo post-mercado según Art. 72 del EU AI Act. Permite listar endpoints activos y obtener métricas de CloudWatch (latencia, invocaciones) sin mover datos.

---

## 🎯 Responsabilidades

- Listar endpoints activos en cuenta SageMaker
- Obtener métricas CloudWatch (latencia, invocaciones)
- Monitorear performance de modelos desplegados
- Integrar con PostMarketMonitoringService

---

## 🔗 Dependencias

### Servicios
- `ExternalIntegrationBusinessService` - Gestión de plataformas externas

### Librerías AWS
- `software.amazon.awssdk.services.sagemaker.SageMakerClient`
- `software.amazon.awssdk.services.cloudwatch.CloudWatchClient`

---

## 📚 API Pública

### `listEndpoints(Long platformId)`

Lista endpoints activos en cuenta SageMaker.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como SAGEMAKER

**Retorna:** `List<SageMakerEndpointSummary>` con:
- `endpointName`: Nombre del endpoint
- `status`: Estado del endpoint
- `creationTime`: Fecha de creación
- `productionVariants`: Variantes de producción

**Ejemplo:**
```java
List<SageMakerEndpointSummary> endpoints = sageMakerService.listEndpoints(platformId);
```

---

### `monitorEndpoint(Long platformId, String endpointName, int hours)`

Obtiene métricas CloudWatch de un endpoint.

**Parámetros:**
- `platformId`: ID de la plataforma
- `endpointName`: Nombre del endpoint
- `hours`: Horas hacia atrás para consultar métricas (default: 24)

**Retorna:** `SageMakerMetrics` con:
- `avgLatency`: Latencia promedio
- `maxLatency`: Latencia máxima
- `invocations`: Número de invocaciones
- `startTime`, `endTime`: Período consultado

**Ejemplo:**
```java
SageMakerMetrics metrics = sageMakerService.monitorEndpoint(platformId, "my-endpoint", 24);
double avgLatency = metrics.getAvgLatency();
```

---

## ⚙️ Configuración

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `accessKey`: AWS Access Key
- `secretKey`: AWS Secret Key
- `region`: Región AWS

---

## 📖 Referencias

- **Art. 72 EU AI Act:** Post-Market Monitoring
- **Integración:** Usado por PostMarketMonitoringService

---

**Última actualización:** 25 de noviembre de 2025
