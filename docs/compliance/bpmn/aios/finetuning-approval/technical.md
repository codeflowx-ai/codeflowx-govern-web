# Fine‑Tuning Approval v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/finetuning-approval-v1.bpmn20.xml`
- **Delegates** (pendiente reforzar):
  - `ValidateFineTuningRequestDelegate`
  - `CheckAdapterAlternativesDelegate`
  - `FineTuningRiskAssessmentDelegate`
  - `GenerateFineTuningPlanDelegate`
  - `ApproveFineTuningDelegate`
  - `RejectFineTuningDelegate`
- **Reglas**: `rules/aios/finetuning/finetuning-approval.drl` (por crear)
- **Fact**: `FineTuningApprovalFact`

## Variables
- `baseModelId`, `targetCapability`, `datasetList`, `dataSensitivity`.
- `adapterAvailable`, `computeCost`, `riskScore`, `complianceStatus`.
- `decision`, `requiredControls`, `justification`.
- `immutableLogId`, `planDocumentUrl`.

## Integraciones
- **Adapter Catalog**: para comprobar alternativas.
- **Cost Estimator / MLOps**: cálculo de recursos necesarios.
- **Data Governance Service**: verificar datasets permitidos/licenciados.
- **Deployment Automation**: genera pipelines de fine‑tuning si se aprueba.

## Flujo técnico
1. `ValidateFineTuningRequestDelegate` valida datos, licencias y dependencias.
2. `CheckAdapterAlternativesDelegate` consulta catálogo y marca `adapterAvailable`.
3. `FineTuningRiskAssessmentDelegate` calcula `riskScore` (datos, coste, impacto).
4. Reglas (a implementar) deciden si se requiere `HITL`.
5. `ApproveFineTuningDelegate` crea el plan (entrenamientos, salvaguardas, monitoreo) y actualiza el estado.
6. `RejectFineTuningDelegate` registra la decisión y comunica la razón.

## Pendientes
- Implementar las reglas y los fact específicos.
- Construir clientes para estimación de coste/risks.
- Escribir pruebas que aseguren que no se aprueban solicitudes con `adapterAvailable=true` salvo excepciones justificadas.


