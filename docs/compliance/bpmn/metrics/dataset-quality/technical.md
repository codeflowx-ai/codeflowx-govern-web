# Dataset Quality v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/metrics/dataset-quality-v1.bpmn`
- **Delegates**:
  - `DatasetQualityCheckDelegate`
  - `PublishDatasetBadgeDelegate`
  - `CreateDataIssueDelegate`
- **Reglas**: opcional (Drools) para thresholds específicos.

## Variables
- Entrada: `datasetId`, `datasetVersion`.
- Salida:
  - `qualityScore`
  - `issuesDetected` (List)
  - `qualityStatus`
  - `badgeUrl`

## Integraciones
- **leka-bias-detection-service**: endpoint `/api/tabular/evaluate-data-quality`.
- **DatasetCatalogService**: actualiza metadatos (badge).
- **IssueTracker**: crea ticket en caso de `FAIL`.

## Detalle delegates
- `DatasetQualityCheckDelegate`: invoca micro, guarda reporte, calcula `qualityScore`.
- `PublishDatasetBadgeDelegate`: genera insignia/metadata visual (puede subir a S3 y registrar URL).
- `CreateDataIssueDelegate`: crea issue/ticket con lista de problemas detectados.

## Testing
- Unit tests con mock del micro (pass/fail scenarios).
- Flowable test que confirme rutas PASS vs FAIL.

## Pendientes
- Añadir almacenamiento de reportes históricos (Timescale/MinIO).
- Enlazar con BPMN `ai-component-onboarding` y `model-approval` para verificación automática.

