# Requisitos de Cumplimiento - Dispositivos Médicos

## Alcance

- Sector: `MEDICAL_DEVICES`
- Framework: `MEDICAL_DEVICES_v1.0.0`
- Cobertura geográfica: Unión Europea, LATAM, FDA USA

## Normativa principal

| Norma | Jurisdicción | Artículos / Secciones | Obligaciones clave |
|-------|--------------|-----------------------|--------------------|
| Reglamento (UE) 2017/745 (MDR) | UE | Art. 10, 56, 61, 83-92 | Sistema de gestión de calidad, evaluación clínica, vigilancia post-comercialización, notificación de incidentes a EUDAMED. |
| ISO 13485:2016 | Global | Cláusulas 7.3, 7.5, 8.5 | Gestión de diseño, producción, CAPA, control documental. |
| FDA 21 CFR Part 820 (QSR) | EE. UU. | Subpartes C, E, J, M | CAPA, control de producción, registros de dispositivos médicos, reporting a FDA. |
| EU AI Act (borrador 2024) | UE | Art. 9-15, Anexo III | Sistema de gestión de riesgos, gobernanza de datos, logging, transparencia y HITL para AI de alto riesgo. |
| IEC 62304 | Global | Cap. 5, 6, 7 | Ciclo de vida software médico, gestión de riesgo y mantenimiento. |

## Requisitos funcionales

1. **Registro EUDAMED**: sincronización automática de `UDI`, estado de CAPA y vigilancia (`eu_database_registration_v1`).
2. **Vigilancia post-market**: monitoreo continuo, clasificación de incidentes, escalado HITL y FRIA (`incident_reporting_v1`, `fria_process_v1`).
3. **Conformidad técnica**: evaluación de dossier (MDR Anexo II), generación de FRIA, trazabilidad ImmutableLog (`conformity_assessment_v1`).
4. **CAPA / FSMP**: planes correctivos con seguimiento y referencia `ImmutableLog` (`compliance_monitoring_v1`).
5. **Gobernanza AI**: validación con `ai-policy-review-v1`, marketplace, telemetría en `governance.kpi.medical_devices.*`.

## Evidencia requerida

- Dossier técnico actualizado (`Technical Documentation Index`).
- Registros CAPA (`mdv_corrective_action`).
- Logs ImmutableLog (`MEDICAL_DEVICES_GOVERNANCE`).
- FRIA vigente y referenciada en marketplace.
- Resultados de evaluación de políticas (`PolicyResult`).

## KPI críticos

- `governance.kpi.medical_devices.risk_index` (Riesgo compuesto AI/MDR).
- `governance.kpi.postmarket_incident_rate` (Incidentes MDR Art. 87).
- `governance.kpi.technical_documentation_status` (% documentación conforme).
- `governance.kpi.fsmp_resolution_time` (Tiempo medio CAPA/FSMP).

## Hooks AI OS

- `ai-policy-review-v1`: evaluación de políticas globales.
- `ai-marketplace-publish-v1`: publicación marketplace.
- Delegates estándar: `aioTelemetryCollectorDelegate`, `aioComplianceAggregatorDelegate`, `aioPolicyEnforcementDelegate`.

## Dependencias externas

- Catálogos `gov_policy_catalog`, `prc_workflow_catalog` (véase `PROMPTS_BASE_DATOS.md`).
- Microservicios CodeflowX: `ImmutableLog`, `aioPolicyEvaluationService`, `aioTelemetryService`.

## Observaciones

- Actualizar plantilla FRIA (`templates/postmarket/medical-devices.docx`).
- Validar compatibilidad con EU AI Act versión final (mantener seguimiento legislativo 2025).


