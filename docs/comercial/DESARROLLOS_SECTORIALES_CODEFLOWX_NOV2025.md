---
title: "Roadmap de desarrollos sectoriales CodeflowX"
author: "Equipo Strategy & Product"
date: "Noviembre 2025"
confidencialidad: "Uso interno"
---

# Roadmap de desarrollos sectoriales CodeflowX (Noviembre 2025)

## 0. Objetivo

Definir, por sector económico, los entregables técnicos y funcionales que debemos completar para disponer de versiones adaptadas de CodeflowX (governance + compliance + evaluación + monitorización), alineadas con normativas sectoriales, ISO aplicables y casos de uso prioritarios identificados en `ADOPCION_IA_SECTORES_ESPANA_NOV2025.md`.

## 1. Tabla resumen de desarrollos pendientes

| Sector | Prioridad | Entregables clave | Componentes afectados | Dependencias | ETA sugerida |
|--------|-----------|-------------------|-----------------------|--------------|--------------|
| Información y comunicaciones (Agencias) | Alta | Plantillas FRIA multi-cliente, bundles BYOM y guía integración MCP | `PROMPTS_15`-`17`, `leka-llm-interpreter`, documentación ventas | Validar con partners agencias | Q1 2026 |
| Finanzas y seguros | Alta | Pack FinReg (scoring explicable, matrices AML), auditoría DORA, reporting Banco de España | `leka-bias-detection`, `leka-llm-evaluation`, `PROMPTS_18` | Necesitamos normativa DORA en prompts 20+ | Q1 2026 |
| Energía / Utilities | Media | Protocolos resiliencia OT, integración SCADA, FRIA específica CNMC | `leka-orchestrator`, triggers TimescaleDB, `PROMPTS_14` | Conectar con `leka-model-wrapper` para modelos físicos | Q2 2026 |
| Administración pública | Alta | Guia ENS completa, FRIA AP, plantillas Art.71, bundle multi-concejalia | `PROMPTS_18`, `PROMPTS_15`, dashboards ENS | Coordinación partners sector público | Q1 2026 |
| Educación | Media | Kit governance agentes educativos, gestión consentimiento menores, reporting ético | `leka-prompt-governance`, `leka-agent-monitoring`, documentación marketing | Alianzas universidades piloto | Q2 2026 |
| Industria manufacturera | Alta | Pack Industria 4.0 ampliado (trazabilidad cadena suministro, mantenimiento predictivo), conectores MES | `leka-model-wrapper`, adapters OPC-UA, `PROMPTS_14` | Sincronizar con EnArt generación entidades | Q1 2026 |
| Servicios profesionales (legaltech) | Media | Matrices responsabilidad proveedor/deployer, cláusulas contractuales, guías auditoría legaltech | Documentación comercial, `PROMPTS_05` | Sincronizar con equipo legal | Q1 2026 |
| Comercio | Media | Checklists omnicanal, controles sesgos recomendadores, plantillas auditoría marketing | `leka-llm-evaluation`, `leka-rag-evaluation`, dashboards marketing | Requiere datasets ejemplo retail | Q2 2026 |
| Transporte y logística | Media | Guías flotas IoT, alertas drift ENS medio, integraciones telemática | `leka-agent-monitoring`, `leka-orchestrator`, modbus adapters | Validar con partner 3PL | Q2 2026 |
| Actividades administrativas / BPO | Alta | SLAs de gobernanza multi-cliente, segregación datos, reporting cliente | `PROMPTS_15`, `leka-server-serving-evaluation`, portal partner | Alinear con roadmap multi-tenant | Q1 2026 |
| Construcción | Media | Compliance obra pública, integración BIM (IFC), gestión subcontratas | `leka-rag-evaluation`, connectors BIM, `PROMPTS_14` | Piloto con partner construcción | Q2 2026 |
| Hostelería y turismo | Media | Plantillas FRIA turismo, controles PII huéspedes, bundles destinos inteligentes | `leka-rag-evaluation`, `leka-prompt-governance`, integraciones PMS | Coordinar con Segittur | Q2 2026 |

## 2. Detalle por sector

### 2.1 Información y comunicaciones

- **Entregables:**
  - Plantillas FRIA para agencias que gestionan múltiples clientes (incluye definiciones de “proyecto gobernado” y límites 2/2/2).
  - Librería de prompts MCP para conectar CodeflowX con n8n, ChatGPT, Claude (refuerzo `PROMPTS_17`).
  - Bundle BYOM con guías de optimización coste (`leka-llm-interpreter` + control GPU/servless).
- **Acciones:**
  - Actualizar `COMPARATIVA_PLATAFORMAS_GOBIERNO_IA_NOV2025.md` con narrativa agencias.
  - Crear script EnArt para generar entidades `prj_project`, `gov_governancepolicy` (prefijo siguiendo convención cor_/prj_).

### 2.2 Finanzas y seguros

- **Entregables:**
  - Módulos de evaluación sesgo/cumplimiento para scoring crediticio (integración `leka-bias-detection`).
  - Flujo automatizado Art. 12-15 EU AI Act + matrices DORA (operational resilience).
  - Guía auditoría Banco de España (reporting anual).
- **Acciones:**
  - Extender `PROMPTS_18` con tablas `fin_riskassessment`, `fin_audittrail` (prefijo `fin_`).
  - Diseñar dashboards compliance en `codeflowx-portal` (EnArt UI).

### 2.3 Energía, agua y residuos

- **Entregables:**
  - Playbook resiliencia OT: triggers TimescaleDB + hash chain para eventos SCADA.
  - FRIA sector CNMC con indicadores sostenibilidad.
  - Integraciones OPC-UA y MQTT para ingestión de datos operacionales.
- **Acciones:**
  - Actualizar `PROMPTS_14` para soportar extensiones Timescale en tablas `ene_eventlog`.
  - Crear pruebas de carga en `codeflowx-portal` para dashboards ESG.

### 2.4 Administración pública

- **Entregables:**
  - Plantillas de informe Art. 71 y FRIA ENS Alto.
  - Matriz de compatibilidad ENS (integrar en `PROMPTS_18`).
  - Soporte multi-concejalía (sub-tenants) en Cloud Governance / Enterprise.
- **Acciones:**
  - Extender `codeflowx-govern-backend` con dominios `adm_` para licitaciones.
  - Generar prompts de despliegue Kubernetes ENS (sin hardcode, usar config).

### 2.5 Educación

- **Entregables:**
  - Librería de políticas data governance menores y consentimiento.
  - Evaluación de agentes educativos (RAG académico, tutoría) con `leka-rag-evaluation`.
  - Plantillas de informe ético (bias, diversidad datasets).
- **Acciones:**
  - Añadir a `PROMPTS_05` entidades `edu_learningasset`, `edu_consentrecord`.
  - Diseñar dashboards específicos en `codeflowx-portal` (UNIDIGITAL).

### 2.6 Industria manufacturera

- **Entregables:**
  - Conectores MES/SCADA (OPC-UA) y pipelines ingestión en `leka-orchestrator`.
  - Evaluación RAG técnica (manuales planta) + control drift modelo visión.
  - Plantillas compliance proveedores tier-1/2.
- **Acciones:**
  - Incorporar triggers `cor_equipmentstatus` con Timescale.
  - Prompts EnArt para servicios `prj_manufacturinggovernanceService` siguiendo SOLID.

### 2.7 Servicios profesionales y legaltech

- **Entregables:**
  - Documentos contractuales provider/deployer estándar.
  - Protocolos auditoría `leka-prompt-governance` (registro versiones).
  - Plantillas FRIA legaltech (eDiscovery, revisión contractual).
- **Acciones:**
  - Actualizar `PROMPTS_15` con endpoints auditoría legaltech.
  - Crear dashboards evidencias legales en `codeflowx-portal`.

### 2.8 Comercio mayorista/minorista

- **Entregables:**
  - Checklists omnicanal (web, tienda física, marketplaces).
  - Evaluaciones sesgo en recomendadores y pricing dinámico.
  - Integración rápida con ERPs (conectores API estándares).
- **Acciones:**
  - Extender `leka-llm-evaluation` con suite marketing.
  - Crear prompts `com_channelpolicy`, `com_auditlog`.

### 2.9 Transporte y logística

- **Entregables:**
  - Pipelines ingestión telemática (API flotas, tachógrafos).
  - Alertas drift y incident management orientado SLA.
  - FRIA ENS Medio adaptada a operadores 3PL.
- **Acciones:**
  - Añadir triggers `log_routeevent` en `PROMPTS_18` (Timescale + hash).
  - Generar dashboards SLA en `codeflowx-portal`.

### 2.10 Actividades administrativas / BPO

- **Entregables:**
  - Matriz segregación clientes y datos (multi-tenant) con `codeflowx-portal`.
  - SLAs de gobernanza y plantillas reporte mensual.
  - Automatización auditorías para cada cliente (FRIA, Art. 12-15).
- **Acciones:**
  - Extender `PROMPTS_15` con endpoints `bpo_` para reporting y segregación.
  - Integrar logging inmutable por tenant (Timescale + pgcrypto).

### 2.11 Construcción

- **Entregables:**
  - Integración BIM/IFC (ingestión en `leka-rag-evaluation`).
  - Plantillas compliance obra pública (Ley 9/2017) y PRL.
  - Monitoreo IA en seguridad obra (visión + alertas).
- **Acciones:**
  - Crear prompts `con_projectrisk`, `con_subcontractor` (prefijo `con_`).
  - Diseñar dashboards compliance licitaciones.

### 2.12 Hostelería y turismo

- **Entregables:**
  - Plantillas FRIA turismo inteligente (Segittur).
  - Controles PII huéspedes, integraciones PMS.
  - Evaluación asistentes multilingüe (nativos + turistas).
- **Acciones:**
  - Extender `leka-rag-evaluation` con datasets hospitality.
  - Crear prompts `tur_guestconsent`, `tur_servicelevel`.

## 3. Próximos pasos

1. Priorizar desarrollos Alta prioridad (Finanzas, AP, Industria, BPO, Agencias) en roadmap Q1 2026.
2. Actualizar `TRACKING_PROMPTS_IMPLEMENTACION.md` con nuevas entradas (PROMPTS_19+ sectoriales) y porcentajes.
3. Coordinar con Product Marketing para crear decks sectoriales (basados en este roadmap + comparativas).
4. Definir owners por microservicio y fecha de entrega (`codeflowx-govern`, `leka-*`).
5. Establecer pipeline de feedback con partners verticales (agencias, banca, sector público).

---

**Preparado por:** Strategy & Product  
**Contacto:** roadmap@codeflowx.com  
**Confidencialidad:** Uso interno – no distribuir sin autorización.

