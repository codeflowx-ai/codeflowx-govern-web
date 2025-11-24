# AI Component Onboarding – Guía Funcional

## Objetivo
Incorporar nuevos componentes AI‑OS (modelos, agentes, prompts o servicios) garantizando que cumplen requisitos de metadata, seguridad, compliance y políticas antes de activarlos en un workspace.

## Roles involucrados
- **AIOS Admin**: inicia solicitudes, corrige metadata, realiza revisiones HITL.
- **Compliance/Governance**: valida hallazgos, aprueba excepciones.
- **Stakeholders técnicos**: aportan evidencias o documentación adicional.

## Flujo funcional
1. **Recepción de solicitud** desde el Portal (variables `componentId`, `workspaceId`, `componentType`).
2. **Validación de metadata**: se revisan campos obligatorios; si faltan, el usuario AIOS Admin debe corregirlos en la tarea “Fix Metadata”.
3. **Controles automáticos**: se ejecutan verificaciones de seguridad/compliance (microservicios `leka-bias-detection`, `leka-llm-evaluation` cuando aplique).
4. **Provisionamiento paralelo**:
   - Registro de la ficha (`factSheet`) del componente.
   - Creación de entrada en `ImmutableLog`.
   - Anclaje de políticas por defecto.
5. **Decisión automática** mediante reglas (`APPROVE`, `CONDITIONAL`, `REJECT`):
   - `APPROVE`: se activa el componente y queda disponible en el workspace.
   - `CONDITIONAL`: el AIOS Admin revisa hallazgos y decide condiciones adicionales.
   - `REJECT`: se notifica la razón y la solicitud finaliza.

## Entradas clave
- `componentMetadata` (mapa con campos obligatorios).
- `securityScore` / `complianceScore` (0‑100).
- `policyCoverage`.
- `riskLevel`.

## Salidas clave
- `decision`, `onboardingJustification`.
- `componentStatus` (`ACTIVE`, `REJECTED`).
- `factSheetId`, `immutableLogId`, `attachedPolicies`.

## SLA y consideraciones
- Correcciones de metadata: 2 días hábiles.
- Revisión HITL (si aplica): 3 días hábiles.
- Cada activación registra entrada en `ImmutableLog` y notificaciones internas.

