# PROMPT 002 – AI-OS-RUNTIME (Orquestador Runtime)

## Contexto
- Modelo AI OS descrito en `docs/arquitectura/ARQUITECTURA_AI_OS.md`.
- Procesos BPMN actuales (aprobación de agentes, modelos, prompts, detección, monitoreo) ubicados en `codeflowx.govern.workflow.lib`.
- Stack tecnológico: Spring Boot 2.7.x, Flowable BPMN, Drools, integración Kubernetes/Terraform.
- Reglas corporativas: arquitectura hexagonal, SOLID, prefijos ENART, logging inmutable.

## Objetivo
Implementar la capa operativa que orquesta despliegues y monitoreo de componentes AI OS.

### Alcance
1. Desarrollar servicios `AioDeploymentService`, `AioServiceBindingService`, `AioTelemetryService` y `AioScheduleService`.
2. Crear adaptadores de infraestructura mínimos: Kubernetes Operator/Helm wrapper y mock de Terraform provider para entornos híbridos.
3. Implementar Delegates BPMN (`AioDeploymentOrchestratorDelegate`, `AioPolicyEnforcementDelegate`, `AioTelemetryCollectorDelegate`) conectados a los servicios anteriores.
4. Incorporar nuevos procesos BPMN (`ai-component-onboarding-v1.bpmn`, `ai-runtime-health-v1.bpmn`) y actualizar procesos existentes (`agent-approval`, `model-approval`, `prompt-approval`) para usar las capacidades AI OS.
5. Emitir eventos y registros de estado en `IMLIMMUTABLELOGS` y en métricas (`Prometheus/OpenTelemetry`) conforme a los KPI definidos.

## Restricciones
- Mantener puertos de dominio desacoplados de implementaciones concretas (patrón hexagonal).
- No introducir dependencias externas adicionales sin aprobación.
- Los delegates deben manejar fallos idempotentes y propagar estados a BPMN.
- Documentar endpoints internos y contratos de eventos.

## Entregables
- Código Java (servicios, adaptadores, delegates) en `codeflowx.govern.workflow.lib`.
- Archivos BPMN en `src/main/resources/bpmn`.
- Reglas Drools actualizadas y, si aplica, nuevos `Fact` objects.
- Tests unitarios/integración con mocks de Flowable y adaptadores.
- Documento técnico breve describiendo flujos de despliegue y monitoreo.

## Métricas de éxito
- Procesos BPMN ejecutan despliegue automático end-to-end con logs registrados.
- Servicios runtime responden a pruebas unitarias/integración.
- Telemetría básica disponible y documentada.
