# PROMPTS – BPMN AI‑OS PENDIENTES (ENART)

> **Fecha:** 15 noviembre 2025  
> **Responsable:** Equipo BPMN + Runtime AI‑OS  
> **Objetivo:** Definir completamente los procesos BPMN faltantes (AI‑OS runtime y marketplace) antes de implementar delegates/rules.  
> **Convenciones ENART:**  
> - Prefijos de tabla/entidad según módulo (`AIO`, `MRK`, `POL`, etc.).  
> - Delegates en `codeflowx.govern.workflow.lib` (`@Component` + logging).  
> - Cada tarea que llame a un micro Python existente debe dejar comentario `// @todo implement client for <micro>` y mockear la llamada hasta que el cliente esté listo.  
> - Reglas Drools en `rules/aios/*.drl`.  
> - BPMN ubicados en `src/main/resources/processes/aios/`.

---

## 1. `ai-component-onboarding-v1.bpmn`

- **ID proceso:** `ai_component_onboarding_v1`  
- **Nombre:** AI Component Onboarding  
- **Trigger:** Solicitud desde AI‑OS Portal (`componentId`, `workspaceId`, `componentType`).  
- **Rol responsable:** `AIOS_ADMIN`.

### Flujo

1. **Start Event – Onboarding Request**  
2. **Service Task – Validate Component Metadata**  
   - Delegate: `ValidateComponentMetadataDelegate`  
   - Reglas: verifica que `componentType` (MODEL, AGENT, PROMPT, SERVICE) tenga los campos obligatorios.  
3. **Exclusive Gateway – Metadata Valid?**  
   - NO → **User Task – Fix Metadata** (ViewModel `AioComponentMetadataFixViewModel`) → regresa a paso 2.  
4. **Service Task – Run Security & Compliance Checks**  
   - Delegate: `ComponentSecurityComplianceDelegate`  
   - Llama a micro `leka-bias-detection-service` y `leka-llm-evaluation` cuando aplique.  
   - Comentario requerido: `// @todo implement client for leka-bias-detection-service` etc.  
5. **Parallel Gateway – Provision Resources**  
   - Ramas:  
     a. **Service Task – Register Component Fact Sheet** (`RegisterComponentFactSheetDelegate`)  
     b. **Service Task – Create Immutable Log Entry** (`CreateComponentImmutableLogDelegate`)  
     c. **Service Task – Attach Policies** (`AttachDefaultPoliciesDelegate`, usa Drools `ai-component-policy-rules.drl`)  
6. **Join Gateway – After Provisioning**  
7. **Business Rule Task – Determine Onboarding Decision**  
   - Reglas en `rules/aios/ai-component-onboarding.drl`.  
   - Salidas: `APPROVE`, `CONDITIONAL`, `REJECT`.  
8. **Exclusive Gateway – Decision**  
   - `APPROVE` → **Service Task – Activate Component** (`ActivateComponentDelegate`).  
   - `CONDITIONAL` → **User Task – Admin Review** (ViewModel `AioComponentConditionalApprovalViewModel`).  
   - `REJECT` → **Service Task – Reject Component** (`RejectComponentDelegate`).  
9. **End Event – Component Onboarded / Rejected**.

### Variables
- `componentId`, `workspaceId`, `componentType`, `metadataValid`, `securityScore`, `complianceScore`, `policiesAttached`, `decision`.

---

## 2. `ai-runtime-health-v1.bpmn`

- **ID:** `ai_runtime_health_v1`  
- **Nombre:** AI Runtime Health Monitor  
- **Trigger:** Timer cada 15 minutos o evento de alerta (`runtimeAlert`).  

### Flujo

1. **Start – Timer/Alert**  
2. **Service Task – Collect Telemetry Snapshot**  
   - Delegate: `AioTelemetryCollectorDelegate`.  
   - `// @todo implement client for aio-telemetry-service`.  
3. **Service Task – Evaluate KPIs**  
   - Delegate: `RuntimeKpiEvaluatorDelegate`.  
   - Reglas: evalúa `latency`, `errorRate`, `drift`, `complianceScore`.  
4. **Parallel Gateway – Branch Actions**  
   - **Branch A:** **Business Rule Task – Determine Health State** (`rules/aios/runtime-health.drl`).  
   - **Branch B:** **Service Task – Update Health Dashboard** (`UpdateRuntimeDashboardDelegate`).  
5. **Exclusive Gateway – Health State?**  
   - `GREEN` → End (log only).  
   - `YELLOW` → **Service Task – Create Warning Ticket** (`CreateRuntimeWarningTicketDelegate`).  
   - `RED` → **Service Task – Trigger Incident BPMN** (`RuntimeIncidentTriggerDelegate`, invoca signal `incident-reporting-process`).  
6. **End – State Published**.

### Variables
- `runtimeId`, `snapshotId`, `latencyMs`, `errorRate`, `healthState`.

---

## 3. `ai-marketplace-publish-v1.bpmn`

- **ID:** `ai_marketplace_publish_v1`  
- **Nombre:** AI Marketplace Publication  
- **Trigger:** Solicitud de publicar componente aprobado (`componentId`, `marketplaceTier`).  

### Flujo

1. **Start Event – Publish Request**  
2. **Service Task – Verify Certification Artifacts** (`VerifyCertificationArtifactsDelegate`).  
3. **Exclusive Gateway – Artifacts Complete?**  
   - NO → **User Task – Upload Missing Evidence** (`MarketplaceEvidenceUploadViewModel`).  
4. **Service Task – Calculate Marketplace Score**  
   - Delegate: `MarketplaceScoringDelegate`.  
   - Reglas `rules/aios/marketplace-scoring.drl`.  
5. **Business Rule Task – Determine Publication Tier**  
   - Salidas: `PUBLIC`, `RESTRICTED`, `DENIED`.  
6. **Exclusive Gateway – Tier**  
   - `PUBLIC` → **Service Task – Publish to Marketplace** (`PublishComponentMarketplaceDelegate`).  
   - `RESTRICTED` → **User Task – Legal Review** (`MarketplaceLegalReviewViewModel`).  
   - `DENIED` → **Service Task – Notify Denial** (`NotifyMarketplaceDenialDelegate`).  
7. **End – Publication Completed**.

### Variables
- `componentId`, `marketplaceTier`, `certificationSet`, `marketplaceScore`, `publicationDecision`.

---

## 4. `ai-policy-review-v1.bpmn`

- **ID:** `ai_policy_review_v1`  
- **Nombre:** AI Policy Review & Enforcement  
- **Trigger:** Cambio en `AioPolicyBinding` o scheduler mensual.  

### Flujo

1. **Start – Policy Review Trigger**  
2. **Service Task – Load Active Policies** (`LoadPoliciesDelegate`).  
3. **Service Task – Evaluate Runtime Evidence** (`EvaluatePolicyEvidenceDelegate`)  
   - `// @todo implement client for aio-policy-service`.  
4. **Parallel Gateway – Split**  
   - **Branch A:** **Business Rule Task – Policy Compliance Decision** (`rules/aios/policy-review.drl`).  
   - **Branch B:** **Service Task – Update Policy Dashboard** (`UpdatePolicyDashboardDelegate`).  
5. **Exclusive Gateway – Decision**  
   - `COMPLIANT` → **Service Task – Log Compliance** (`LogPolicyComplianceDelegate`).  
   - `HITL` → **User Task – Policy Committee Review** (`PolicyCommitteeReviewViewModel`).  
   - `NON_COMPLIANT` → **Service Task – Enforce Remediation** (`PolicyRemediationDelegate`) → **Service Task – Trigger Suspension** (`TriggerPolicySuspensionDelegate`, si `criticalFailure`).  
6. **End – Policy Review Closed**.

### Variables
- `policyBindingId`, `evidenceSet`, `complianceScore`, `decision`, `remediationPlan`.

---

## 5. `external-model-approval-workflow.bpmn`

- **ID:** `external_model_approval_v1`  
- **Nombre:** External Model Approval Workflow  
- **Trigger:** Integración de modelo de terceros.  

### Flujo

1. **Start – External Model Request**  
2. **Service Task – Classify External Model Risk** (`ExternalModelRiskClassificationDelegate`)  
   - Usa micro `leka-risk-evaluator` (`// @todo implement client for leka-risk-evaluator`).  
3. **Exclusive Gateway – High Risk?**  
   - SÍ → **Service Task – Launch FRIA Process** (`LaunchFriaForExternalModelDelegate`, llama a `fria-process`).  
   - NO → sigue.  
4. **Service Task – Validate Technical Documentation** (`ValidateExternalDocumentationDelegate`).  
5. **Business Rule Task – Approval Decision** (`rules/aios/external-model-approval.drl`).  
6. **Exclusive Gateway – Decision**  
   - `APPROVE` → **Service Task – Register External Model** (`RegisterExternalModelDelegate`).  
   - `CONDITIONAL` → **User Task – External Model HITL Review** (`ExternalModelHitlReviewViewModel`).  
   - `REJECT` → **Service Task – Notify External Vendor** (`NotifyExternalVendorDelegate`).  
7. **End – External Model Decision**.

### Variables
- `externalModelId`, `vendorName`, `riskLevel`, `documentationScore`, `decision`.

---

## 6. Checklist previo a implementación

1. Crear directorio `src/main/resources/processes/aios/` y guardar los cinco BPMN.  
2. Actualizar `docs/compliance/bpmn/BPMN_CATALOG.md` y `docs/compliance/bpmn/BPMN_PROCESS_GUIDES.md` con estos procesos.  
3. Definir delegados con anotaciones ENART; donde se integra con micros Python, dejar `// @todo implement client for <micro>` y mock.  
4. Crear reglas Drools por proceso (`ai-component-onboarding.drl`, `runtime-health.drl`, etc.).  
5. Añadir pruebas `@SpringBootTest` que desplieguen los BPMN nuevos en el micro `workflow.engine`.



