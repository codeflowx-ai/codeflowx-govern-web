# AzureMLConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.AzureMLConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector Azure ML para monitorear despliegues online y detectar degradaciones según Art. 72 del EU AI Act.

---

## 🎯 Responsabilidades

- Listar despliegues online en Azure ML Workspace
- Obtener métricas de latencia y throughput
- Detectar degradaciones de performance
- Integrar con PostMarketMonitoringService

---

## 📚 API Pública

### `listDeployments(Long platformId)`

Lista despliegues online configurados en Azure ML Workspace.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como AZURE_ML

**Retorna:** `List<AzureDeploymentSummary>` con:
- `endpointName`: Nombre del endpoint
- `deploymentName`: Nombre del deployment
- `modelId`: ID del modelo
- `provisioningState`: Estado de aprovisionamiento

---

### `monitorDeployment(Long platformId, String endpointName, String deploymentName, int days)`

Obtiene métricas de latencia y throughput del deployment.

**Parámetros:**
- `platformId`: ID de la plataforma
- `endpointName`: Nombre del endpoint
- `deploymentName`: Nombre del deployment
- `days`: Días hacia atrás para consultar (default: 7)

**Retorna:** `AzureDeploymentMetrics` con métricas de performance

---

## ⚙️ Configuración

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `subscriptionId`: Azure Subscription ID
- `resourceGroup`: Resource Group
- `workspaceName`: Nombre del workspace
- `tenantId`, `clientId`, `clientSecret`: Para autenticación

---

## 📖 Referencias

- **Art. 72 EU AI Act:** Post-Market Monitoring
- **Integración:** Usado por PostMarketMonitoringService

---

**Última actualización:** 25 de noviembre de 2025
