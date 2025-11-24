# External Model Approval – Guía Funcional

## Objetivo
Evaluar modelos de terceros antes de integrarlos en AI‑OS, garantizando que cumplen los requisitos de riesgo, documentación técnica y, cuando sea necesario, FRIA/Conformity.

## Roles
- **Vendor Manager**: gestiona relación con el proveedor externo.
- **Compliance/Legal**: revisa documentación y FRIA.
- **AIOS Admin**: ejecuta la aprobación final.

## Flujo funcional
1. **Solicitud de integración**: se ingresa `externalModelId`, `vendorName`, documentación adjunta.
2. **Clasificación de riesgo**: se determina `riskLevel` (puede forzar FRIA).
3. **FRIA** (si `riskLevel` = HIGH): se lanza automáticamente el BPMN `fria-process`.
4. **Validación de documentación**: se verifica que la evidencia técnica sea completa (score).
5. **Reglas de decisión**:
   - `APPROVE`: documentación ≥85 y riesgo controlado / FRIA completada.
   - `CONDITIONAL`: documentación 70‑84 o FRIA pendiente → se abre tarea HITL.
   - `REJECT`: casos restantes.
6. **Cierre**:
   - `APPROVE`: el modelo se registra en el catálogo externo.
   - `CONDITIONAL`: tarea humana define condiciones.
   - `REJECT`: se notifica al proveedor con motivos.

## Entradas
- `riskLevel`, `documentationScore`, `friaRequired`, `friaCompleted`.

## Salidas
- `externalDecision`, `externalDecisionJustification`.
- `externalModelStatus`, `externalModelRegisteredAt` (si aprobado).

## SLA
- FRIA: máximo 10 días.
- Decisión inicial: 5 días laborables.
- Notificación a proveedor: inmediata tras decisión.

