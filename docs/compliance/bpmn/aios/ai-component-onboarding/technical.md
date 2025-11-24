# AI Component Onboarding – Guía Técnica

## Artefactos
- **BPMN**: `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/ai-component-onboarding-v1.bpmn`
- **Reglas**: `rules/aios/onboarding/ai-component-onboarding.drl` (`ruleflow-group: ai-component-onboarding`)
- **Facts**: `AioComponentOnboardingFact`

## Delegates principales
| Delegate | Clase | Función |
| --- | --- | --- |
| `validateComponentMetadataDelegate` | `aios.onboarding.ValidateComponentMetadataDelegate` | Revisa metadata requerida y marca `metadataValid`. |
| `componentSecurityComplianceDelegate` | `aios.onboarding.ComponentSecurityComplianceDelegate` | Ejecuta controles automáticos; deja `securityScore`, `complianceScore`, `riskLevel`. `// @todo` clientes `leka-bias-detection` / `leka-llm-evaluation`. |
| `registerComponentFactSheetDelegate` | Registra `factSheetId` y payload. |
| `createComponentImmutableLogDelegate` | Genera registro `ImmutableLog`. |
| `attachDefaultPoliciesDelegate` | Adjunta políticas y cuenta `policyCoverage`. |
| `aiComponentOnboardingRulesDelegate` | Construye `AioComponentOnboardingFact` y ejecuta Drools (usa `DroolsRulesService.executeComponentOnboardingRules`). |
| `activateComponentDelegate` | Marca componente `ACTIVE`. |
| `rejectComponentDelegate` | Marca estado `REJECTED` con motivo. |

## Variables de proceso
- Entrada: `componentId`, `workspaceId`, `componentType`, `componentMetadata`.
- Derivadas: `metadataIssues`, `securityScore`, `complianceScore`, `riskLevel`, `policyCoverage`.
- Salida: `decision`, `requiresHumanReview`, `onboardingJustification`, `componentStatus`, `factSheetId`, `immutableLogId`.

## Integraciones
- `leka-bias-detection-service`: evaluación de datos / fairness (**pendiente cliente**).
- `leka-llm-evaluation`: pruebas de prompts/modelos (**pendiente cliente**).
- `ImmutableLog`: registro audit trail (actualmente mock).

## Reglas Drools
1. **Approve Component**: scores ≥85, cobertura ≥3 políticas y sin riesgo crítico.
2. **Conditional Approval**: scores 70‑84 → exige HITL.
3. **Reject Component**: resto de casos.

## Consideraciones técnicas
- Delegates registran logs con SLF4J (`@Slf4j`).
- Kie session configurada como `ai-component-onboarding-session` en `kmodule.xml`.
- Si se añaden nuevos campos, actualizar `AioComponentOnboardingFact` y la construcción del fact en el delegate de reglas.

