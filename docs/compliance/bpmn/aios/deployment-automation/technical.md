# Deployment Automation v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/deployment-automation-v1.bpmn`
- **Delegates**:
  - `CheckAutoScalingDelegate`
  - `DeployModelDelegate`
  - `AutoScaleDeploymentDelegate`
  - `RollbackModelDelegate`
  - `SaveDeploymentLogDelegate` (si existe)
- **Integraciones**: Kubernetes Operator/Helm, Terraform provider, observabilidad.

## Variables
- `deploymentRequestId`, `modelId`, `version`, `environment`.
- `scalingConfig`, `deployResult`, `rollbackRequired`.
- `deploymentUrl`, `deploymentTicketId`.

## Flujo técnico
1. `CheckAutoScalingDelegate` valida configuraciones (resources, quotas).
2. `DeployModelDelegate` lanza pipeline (API de CD, Helm chart, etc.) y setea `deployResult`.
3. Gateway:
   - `SUCCESS` → `AutoScaleDeploymentDelegate` ajusta HPA/ASG y marca `deploymentStatus=SUCCESS`.
   - `FAIL` → `RollbackModelDelegate` revierte la versión anterior.
4. `SaveDeploymentLog`/`Notification` informan a stakeholders.

## Consideraciones
- Debe integrarse con `AioDeploymentService` y `AioTelemetryService`.
- Añadir soporte para “canary” y “blue/green” cuando aplique.
- Guardar logs en `DeploymentAudit` + `ImmutableLog`.

## Testing
- Mocks para APIs de despliegue (Kubernetes/Terraform).
- Flowable test: caso éxito + rollback.


