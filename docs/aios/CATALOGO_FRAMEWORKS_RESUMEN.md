# Catálogo Resumido de Frameworks CodeflowX OS

## Objetivo
Proporcionar una visión rápida de los frameworks sectoriales y genéricos disponibles en CodeflowX OS, destacando procesos, reglas y gaps normativos.

## Estructura del Catálogo
Por cada framework:
- **Código** (prefijo) y nombre.
- **Normativas cubiertas**.
- **Procesos BPMN clave**.
- **Reglas Drools destacadas**.
- **Micros/artefactos disponibles**.
- **Gaps normativos/documentación**.
- **Estado** (GA, Beta, Draft).

## Frameworks Core (Genéricos)
| Código | Nombre | Normativas | Procesos clave | Estado |
|--------|--------|------------|----------------|--------|
| `cor_aios` | Core AI OS | EU AI Act (general), ISO 27001 | `ai-component-onboarding`, `ai-runtime-health`, `ai-policy-review` | GA |
| `cor_market` | Marketplace AI | EU AI Act Art. 54, políticas internas | `ai-marketplace-publish`, `ai-marketplace-access` | Beta |
| `cor_ops` | Operaciones | ENS, SOC2 | `ai-incident-response`, `ai-compliance-report` | Draft |

## Frameworks Sectoriales (ejemplos)
| Código | Sector | Normativas | Procesos | Reglas destacadas | Micros | Gaps |
|--------|--------|------------|----------|-------------------|--------|------|
| `fin_crd` | Banca - Crédito | EBA, Basel III, LGPD | `credit-risk-evaluation`, `loan-approval-governance` | `drl_credit_scoring`, `drl_risk_alerts` | Micros de scoring y reporting | Documentación LGPD en actualización Q1 2026 |
| `hlth_cln` | Salud - Clínico | MDR, HIPAA, GDPR | `clinical-alg-review`, `incident-notification` | `drl_clinical_risk`, `drl_data_access` | Micros de validación y UI dashboards | Necesita evidencias EMEA 2024 en `knowledge_gap_report` |
| `gov_civ` | Sector Público | ENS, NIS2 | `citizen-bot-approval`, `policy-enforcement` | `drl_public_risk`, `drl_access_control` | Micros para registro y telemetría | Faltan anexos ENS alto nivel |
| `mfg_qms` | Manufactura/QM | ISO 9001, IATF | `quality-ai-approval`, `incident-escalation` | `drl_quality_score`, `drl_supply_chain_alerts` | Micros de integraciones ERP | Nuevas regulaciones Alemania pendientes |

## Frameworks en Desarrollo
- `agr_food` — Agroalimentario (Codex Alimentarius, FAO).  
- `med_dev` — Dispositivos médicos (FDA, MDR).  
- `ins_frs` — Seguros fraudes (Solvency II, local).  
- `edu_ai` — Educación (AI Act + normativas locales).  
Estado: Beta en `faas-frameworks/*`, require validación QA y documentación.

## Información Complementaria
- Detalles completos en cada `faas-framework-<sector>/README.md`.  
- Políticas y metamodelos en `metamodel/framework.yaml` y `policies.json`.  
- Uso de prompts específicos:
  - `PROMPTS_FAAS_MICROS.md` para microservicios.
  - `PROMPTS_FAAS_PROCESOS_BPMN.md` para procesos.
  - `PROMPTS_FAAS_REGLAS_DROOLS.md` para reglas.
- Gaps documentados en `knowledge_gap_report.md` por framework.

## Cómo Solicitar Nuevos Frameworks
- Crear ticket `AIOS-FRAMEWORK-REQUEST` especificando normativa, procesos requeridos y deadline.  
- Equipo AI OS evalúa factibilidad, asigna prioridad y dentro de 2 semanas entrega plan.  
- Se puede generar un MVP usando `PROMPTS_FAAST_AGENT_STARTER` modo FULL para prototipo rápido.

## Roadmap 2026 (Alta prioridad)
- `fin_esg` — Finanzas sostenibles (CSRD, SFDR).  
- `tel_reg` — Telecom (normativas espectro, IA en redes).  
- `ret_cust` — Retail (experiencia cliente, protección datos).  
- Actualización multi-jurisdicción Brasil/México/Colombia en frameworks existentes.




