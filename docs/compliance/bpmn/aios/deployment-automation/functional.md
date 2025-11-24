# Deployment Automation v1 – Guía Funcional

## Objetivo
Automatizar la preparación, despliegue y escalado de modelos/agentes aprobados en entornos de AI‑OS (staging/producción), manteniendo trazabilidad y gobernanza.

## Actores
- **MLOps / Plataforma**: opera el pipeline.
- **Propietario del modelo**: solicita el despliegue.
- **Seguridad / Compliance**: verifican políticas antes del release.

## Flujo
1. **Inicio**: `deploymentRequest` con `modelId`, `versión`, `entorno`.
2. **Check Auto Scaling**: valida que existan recursos y políticas (HPA, quotas).
3. **Deploy Model**:
   - Genera artefactos (containers, config).
   - Ejecuta pipeline CD (SageMaker, Kubernetes, etc.).
4. **Gateway – Resultado**:
   - `SUCCESS`: pasa a `Auto Scale Deployment` (aplica configuración HPA/ASG).
   - `FAIL`: se ejecuta `RollbackModel`.
5. **Registro en `DeploymentLog`** y notificaciones.

## Variables
- `deploymentId`, `modelId`, `version`, `environment`.
- `deploymentStatus`, `deploymentUrl`, `scalingPolicyId`.
- `rollbackTriggered`, `rollbackReason`.

## SLA
- Tiempo máximo de despliegue: < 30 minutos.
- Rollback automático ante fallos: < 10 minutos.
- Notificación al owner al cerrar el pipeline.

## Consideraciones
- Este flujo suele ser llamado por `model-approval` o manualmente desde AI‑OS.
- Debe incluir chequeos de `policy enforcement` y `runtime readiness`.


