# Conformity Assessment Process – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/conformity-assessment-process.bpmn20.xml`
- **Delegates clave**:
  - `VerifyQmsComplianceDelegate`
  - `ReviewTechnicalDocumentationDelegate`
  - `VerifyProcessConsistencyDelegate`
  - `GenerateConformityReportDelegate`
  - `MarkAsConformityAssessedDelegate`
  - `DocumentRejectionDelegate`
- **Forms/ViewModels**: `InitiateConformityAssessment`, `ReviewQmsGaps`, `CompleteDocumentation`, `FinalReview`, `ApproveConformityAssessment`.

## Variables técnicas
- Entradas:
  - `projectId: Long`
  - `assessmentType: String`
- Outputs intermedios:
  - `qmsScore: BigDecimal`, `docScore`, `consistencyScore`
  - `qmsGaps: List<String>`, `docGaps`
- Finales:
  - `overallScore: BigDecimal`, `reportUrl`, `approved`
  - `conformityAssessed: Boolean`, `rejectionReason`

## Integraciones
- **QualityManagementSystemService**: expuesto en `nocode.service`.
- **TechnicalDocumentationService** + micro `leka-technical-documentation-generator`.
- **ConformityReportService** (PDF + storage).
- **ImmutableLog**: registrar cada hito.

## Lógica delegate (resumen)
- `VerifyQmsComplianceDelegate`: llama servicio QMS, genera score/gaps.
- `ReviewTechnicalDocumentationDelegate`: valida checklists Anexo IV, invoca micro Python.
- `VerifyProcessConsistencyDelegate`: verifica tiempos de diseño y plan post-market.
- `GenerateConformityReportDelegate`: compone PDF y sube a S3/minio.
- `MarkAsConformityAssessedDelegate`: actualiza entidad `ComplianceAssessment` y genera EU declaration (si aplica).
- `DocumentRejectionDelegate`: persiste motivo y notifica stakeholders.

## Reglas / Validaciones
- Score mínimo recomendado: 0.90 para aprobar sin condiciones.
- Campos obligatorios en cada ViewModel (validar via Bean Validation).

## Testing
- `@SpringBootTest` con Flowable para verificar:
  - Branches QMS/Doc correctamente.
  - Generación de reporte y persistencia en BD.
  - Mensajes a `ImmutableLog`.

## Pendientes
- Unificar `conformity-assessment-process` con `internal-conformity-assessment` (reutilizar delegates).
- Documentar endpoints REST en micro `workflow.engine`.

