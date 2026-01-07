# PROMPT 003 – AI-OS-GOV (Gobernanza y Compliance)

## Contexto
- Capa AI OS descrita en `docs/arquitectura/ARQUITECTURA_AI_OS.md`.
- Entidades regulatorias existentes: `ComplianceAssessment`, `FriaAssessment`, `ImmutableLog`, `EuRegistration`, `AnnexIIICategory`.
- Procesos BPMN en `codeflowx.govern.workflow.lib` y prompts sectoriales en `codeflowx-faas-platform`.
- Normativa EU AI Act (Art. 12, 19, 27, 43, 49) y lineamientos internos ENART.

## Objetivo
Asegurar que la capa AI OS opere bajo gobernanza automatizada, cumpliendo normativa y habilitando reutilización controlada.

### Alcance
1. Extender procesos BPMN existentes para enlazar `AioComponent`, `AioDeployment` y `AioPolicyBinding` con evaluaciones de cumplimiento y FRIA.
2. Diseñar y generar nuevos procesos (`ai-marketplace-publish-v1.bpmn`, `ai-policy-review-v1.bpmn`) que automaticen certificación y propagación de políticas.
3. Actualizar reglas Drools para incorporar métricas runtime (riskScore, complianceScore, drift) y decidir acciones (auto-approve, HITL, suspensión).
4. Preparar prompts de operación y compliance (sectoriales) que consuman APIs AI OS y registren resultados en `ImmutableLog`.
5. Elaborar reportes y dashboards que consoliden KPIs (`uptime`, `usageByWorkspace`, `complianceScore`) integrando telemetría y auditorías.

## Restricciones
- Mantener auditoría completa en `IMLIMMUTABLELOGS` con trazabilidad de decisiones.
- No duplicar lógica; reutilizar servicios AI OS ya expuestos.
- Documentación y comentarios en español técnico.
- Respetar segregación por `AioWorkspace` y roles (`AIOS_ADMIN`, `AIOS_OPERATOR`, `AIOS_CONSUMER`).

## Entregables
- BPMN actualizados y nuevos en `codeflowx.govern.workflow.lib`.
- Reglas Drools y `Fact` objects extendidos.
- Prompts (markdown) actualizados en `codeflowx-faas-platform/docs/prompts/`.
- Reporte de cumplimiento y checklist en `docs/compliance/` o `docs/governance/`.
- Scripts de automatización para reportes (si aplica) y guía de uso.

## Métricas de éxito
- Procesos de aprobación y marketplace verifican políticas AI OS automáticamente.
- Prompts sectoriales operan contra APIs AI OS con registro de auditoría.
- Reportes de cumplimiento generan evidencia rastreable para auditorías EU AI Act.
