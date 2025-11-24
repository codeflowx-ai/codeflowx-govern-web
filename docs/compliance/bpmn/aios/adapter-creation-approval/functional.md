# Adapter Creation Approval v1 – Guía Funcional

## Objetivo
Controlar la creación y despliegue de nuevos “adapters” o extensiones para modelos/LLM, asegurando que se prioriza la reutilización, se mitigan riesgos y se cumple el Art. 53 (modificaciones significativas).

## Roles
- **Ingeniero de Integración**: solicita la creación del adapter.
- **Arquitecto AI‑OS**: revisa impacto en arquitectura y dependencias.
- **Cumplimiento / Seguridad**: valida políticas, datos y licencias.
- **Comité de Cambios (CAB)**: aprueba la incorporación final.

## Flujo
1. **Inicio**: registro de solicitud (`adapterId`, `modelo base`, `objetivo`, `datos requeridos`).
2. **Validación de requisitos**:
   - `ValidateAdapterRequest` revisa justificación, cobertura de políticas y existencia de alternativas.
3. **Evaluación de riesgo y dependencia**:
   - `RiskScoring` calcula `riskScore`, `criticality`.
4. **Gateway**:
   - Si `riskScore` alto → obliga `HITL` (CAB).
   - Si se detecta un adapter similar → se sugiere reutilización y se puede rechazar.
5. **User Task – Revisión CAB** (opcional según riesgo).
6. **Decisión:**
   - `APPROVE` → se crea el adapter en el catálogo y se habilita su pipeline de despliegue.
   - `REJECT` → se notifica y se recomienda usar un adapter existente.

## Variables
- `adapterId`, `modelId`, `useCase`.
- `policyCoverage`, `dataSensitivity`, `estimatedImpact`.
- `riskScore`, `reuseCandidate`.
- `decision`, `justification`, `ownerNotification`.

## SLA
- Evaluación inicial: 24 h.
- Revisión CAB: 48 h.
- Onboarding de un adapter aprobado: controlado por `deployment-automation`.

## Reglas y criterios
- Priorizar adapters reutilizables frente a full fine‑tuning.
- Rechazar solicitudes que no demuestren beneficio claro o que dupliquen funcionalidad.


