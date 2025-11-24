# Fine‑Tuning Approval v1 – Guía Funcional

## Objetivo
Regir las solicitudes de fine‑tuning completo de modelos fundacionales, priorizando el uso de adapters y garantizando que sólo se aprueban ajustes cuando hay justificación técnica/regulatoria (Art. 53 EU AI Act).

## Roles
- **ML Researcher**: solicita el fine‑tuning.
- **Model Governance Board**: evalúa impacto, coste y riesgo.
- **Security/Compliance**: revisan datos, licencias y restricciones.

## Flujo
1. **Inicio**: se presenta la solicitud con `baseModel`, `datasets`, `objetivo`.
2. **Verify Preconditions**:
   - ¿Existe un adapter que satisfaga el mismo objetivo?  
     - Si SÍ → se rechaza o se redirige a `adapter-creation`.
3. **Risk & Resource Assessment**: calcula coste, dependencia de datos sensibles, requisitos infra.
4. **Gateway**:
   - `LOW/MED risk` + sin alternativas → pasa a revisión humana.
   - `HIGH risk` o datos no aprobados → rechazo automático.
5. **User Task – Gov Board Review**: evalúa justificación y plan de despliegue.
6. **Decisión**:
   - `APPROVED (RESTRICTED)` → se crea plan de fine‑tuning con salvaguardas.
   - `DENIED`.
7. **End** con registro en `ImmutableLog`.

## Variables
- `baseModel`, `targetCapability`, `datasets`, `dataSensitivity`.
- `adapterAvailable`, `estimatedCost`, `riskScore`.
- `decision`, `conditions`, `ownerNotification`.

## SLA
- Evaluación inicial: 48 h.
- Revisión del Board: 5 días.
- Fine‑tuning aprobado debe tener plan de monitoreo y rollback definido.

## Consideraciones
- Se debe exigir “fallback plan” y evidencias de comparación con adapters.
- Las solicitudes aprobadas alimentan `deployment-automation` y `model-retraining`.


