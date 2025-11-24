# Compliance Monitoring v1 – Guía Funcional

## Objetivo
Monitorear automáticamente el cumplimiento de sistemas AI de alto riesgo (Art. 15, 19 y 72 EU AI Act), detectar desviaciones y activar remediaciones antes de impactar a usuarios.

## Frecuencia / Trigger
- **Timer**: cada 24h.
- **Manual**: puede relanzarse desde el panel de gobierno.

## Roles
- **Compliance Ops**: revisa resultados y declara incidentes.
- **SRE / Ingeniería**: atiende tareas derivadas (alertas críticas, adversarial testing).
- **Comité AI**: recibe reportes agregados.

## Flujo resumido
1. **Start Timer (24h)**.
2. **executeComplianceCheck** (baseline original).
3. **Nuevo bloque paralelo**:
   - `verifyLogIntegrity` → comprueba hash chain / immutability.
   - `runAdversarialEvaluation` → ejecuta pruebas adversariales.
   - `runFeedbackLoopAnalysis` → analiza feedback loops, bias amplification.
   - `checkPostMarketMetrics` → verifica métricas productivas (drift, degradación, UX).
   - Si `tamperingDetected == true` → crea alerta crítica.
4. **Join** y paso a `nonComplianceGateway`.
5. **Resto del flujo existente**: generación de alertas, tareas de seguimiento y cierres.

## Variables clave
- Entradas: `projectId`, `modelId`, `sector_code`, `use_case_code`.
- Nuevas salidas:
  - `logIntegrityValid`, `tamperingDetected`, `corruptedLogsCount`.
  - `adversarialRobustnessScore`, `vulnerabilitiesFound`.
  - `feedbackLoopDetected`, `biasAmplificationFactor`.
  - `driftDetected`, `performanceDegradation`, `userSatisfactionDrop`.
- Estado final: `complianceStatus`, `alertsCreated`.

## SLA / Acciones humanas
- Alertas críticas (tampering) deben ser atendidas en < 2h.
- Resultados de evaluación adversarial se documentan en ImmutableLog.
- Feedback loops y drift generan tickets correctivos vinculados al BPMN `incident-response-rca`.

## Reportes
- Dashboard Compliance → muestra scores diarios y tendencias.
- Exportables (CSV/PDF) para auditorías Art. 72.

