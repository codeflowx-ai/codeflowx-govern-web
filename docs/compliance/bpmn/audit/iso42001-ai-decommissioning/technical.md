# ISO 42001 AI Decommissioning – Guía Técnica

## Artefactos
- **BPMN**: `processes/audit/iso42001-ai-decommissioning-v1.bpmn`
- **Delegates sugeridos**:
  - `PlanDecommissioningDelegate`
  - `FreezeDeploymentsDelegate`
  - `ArchiveArtifactsDelegate`
  - `ExecuteDataRetentionDelegate`
  - `DisableIntegrationsDelegate`
  - `UpdateSystemRegistryDelegate`
- **Forms**: `decommission_plan_form.zul`, `compliance_review_form.zul`.
- **Entidades**: `AioComponent`, `DecommissionReport`.

## Variables
- `systemId`, `decommissioningId`, `reason`.
- `planDetails`, `planDocumentUrl`.
- `archiveStatus`, `archiveLocation`.
- `dataDeletionEvidence`.
- `complianceApproval`, `finalReportUrl`.

## Integraciones
- **DeploymentController**: para `FreezeDeployments`.
- **Artifact Storage** (S3/MinIO) para archivado.
- **Data Management Service**: ejecuta políticas de retención/borrado.
- **IAM**: revoca credenciales/integraciones.
- **Catalog Service / EU DB**: marca sistema como `DECOMMISSIONED`.

## Consideraciones
- Uso de `Parallel Gateway` para ejecutar tareas técnicas en simultáneo.
- Timer opcional para esperar confirmaciones de data retention.
- `Compliance Review` valida checklists (Art. 54).

## Testing
- Unit tests para delegates (mock servicios).
- Simulación Flowable: escenario completo y fallos parciales (ej. delete fallido → loop).

## Pendientes
- Detallar formato del informe final.
- Integrar con `ImmutableLog`.

