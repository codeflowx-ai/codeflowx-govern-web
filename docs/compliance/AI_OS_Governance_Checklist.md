# Checklist de Cumplimiento – AI OS Governance

## Resumen
Este checklist documenta cómo la capa AI OS de CodeflowX cumple los artículos relevantes del EU AI Act (12, 19, 27, 43, 49) tras la incorporación de los procesos `ai-marketplace-publish-v1.bpmn`, `ai-policy-review-v1.bpmn`, las reglas Drools `aios-governance-runtime.drl` y los nuevos delegates BPMN. Se integra con `ImmutableLog` para asegurar trazabilidad end-to-end y adopta métricas runtime (`riskScore`, `complianceScore`, `driftScore`, `fairnessScore`, `uptimePercentage`, `usageByWorkspace`).

## Matriz de Cobertura
| Marco | Artefacto | Evidencia | Observaciones |
|-------|-----------|-----------|---------------|
| Art. 12 – Registros | Delegates `ImmutableLogDelegate` en ambos procesos | Operaciones `AI_MARKETPLACE_SUBMISSION_INIT`, `AI_POLICY_REVIEW_TRIGGERED`, `AI_POLICY_APPLIED`, `AI_MARKETPLACE_REJECTED` | Entradas referencian `aiocomponentuuid` y `aiodeploymentuuid` |
| Art. 19 – Transparencia | `AioTelemetryPublisherDelegate`, `AioPolicyEvaluationService` | Eventos `POLICY_APPLIED`, `MARKETPLACE_PUBLISHED`, métricas asociadas | Se publica en telemetría y en dashboard SRE |
| Art. 27 – FRIA | `AioComplianceAggregatorDelegate` | Variables `friaCompleted`, `friaAssessmentUuid` | Bloquea marketplace cuando FRIA no está completa |
| Art. 43 – Evaluaciones de conformidad | Reglas Drools `aios-governance-runtime.drl` | Recomendaciones `AUTO_APPLY`, `REQUIRE_HITL`, `SUSPEND_DEPLOYMENTS` | Se exige revisión HITL cuando el riesgo supera umbral |
| Art. 49 – Registro UE | `AioMarketplacePublisherDelegate` | Metadata con `AnnexIIICategory`, `complianceAssessmentUuid` | Habilita payload automático para altas de alto riesgo |

## Checklist Operativo
- [ ] FRIA completada (`friaCompleted = true`) antes de aprobar marketplace.
- [ ] `policyViolations` vacío para autoaprobaciones; en caso contrario, justificar en `hitlOutcome`.
- [ ] `ImmutableLog` con operaciones `INIT`, `HITL`, `APPLIED`/`REJECTED` y hash de auditoría.
- [ ] `AioPolicyEvaluationFact` almacenado junto a recomendación y métricas de captura (`runtimeMetricsCapturedAt`).
- [ ] Acceso restringido a roles `AIOS_ADMIN` / `AIOS_OPERATOR` según la tarea BPMN.
- [ ] Suspensión automática de despliegues cuando `actionRecommendation = SUSPEND_DEPLOYMENTS`.
- [ ] Reporte de telemetría enviado (`AioTelemetryEventType.MARKETPLACE_PUBLISHED` o `POLICY_APPLIED`) y visible en dashboard de compliance.
- [ ] Checklist actualizado en `agent_execution_log.md` tras cada ejecución.

## Procedimiento de Evidencia
1. Ejecutar el proceso según el artefacto (`MARKETPLACE` o `POLICY_REVIEW`) usando el prompt operativo.
2. Capturar los UUIDs generados en `ImmutableLog` y evidencia FRIA.
3. Exportar métricas runtime (`riskScore`, `complianceScore`, `driftScore`, `usageByWorkspace`) a JSON firmado.
4. Adjuntar la salida de Drools (recomendación + `policyViolations`) al expediente de auditoría.
5. Registrar resultado en el dashboard de cumplimiento (`governance-kpis`) y notificar al responsable sectorial.

## Seguimiento
- Periodicidad recomendada: semanal (componentes activos) y previa a cada release en marketplace.
- Responsable: Oficina IA (AIOS_ADMIN).
- Herramientas: Flowable Modeler, Drools Workbench (solo lectura), panel `governance-kpis`.
