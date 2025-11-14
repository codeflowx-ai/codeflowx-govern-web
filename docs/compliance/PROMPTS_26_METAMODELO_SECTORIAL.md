# PROMPTS 26 - METAMODELO SECTORIAL CODEFLOWX
## Extensibilidad sin cambios de esquema (LATAM / EU / nuevas geografías)

**Equipo responsable:** Architecture & Data Governance
**Fecha:** Noviembre 2025
**Objetivo:** Definir el metamodelo que permite extender CodeflowX a nuevos sectores o geografías sin modificar el esquema físico de base de datos, apoyándose en catálogos, configuraciones EnArt y estructuras JSONB/hstore existentes.

---

## 🧱 Principios de Diseño

1. **Esquema inmutable:** las tablas core (`gov_governance_events`, `gov_governance_results`, `gov_webhook_*`, `cor_*`, `prj_*`) actúan como *data lake* de gobernanza. No se añaden columnas ni tablas salvo casos regulatorios extremos.
2. **Metaconfiguración:** la especialización sectorial se logra mediante catálogos, plantillas, políticas y metadatos almacenados en JSONB/hstore (`metadata`, `result_payload`, `policies_payload`).
3. **Catálogo jerárquico:** uso de `cor_catalogue`, `cor_catalogitem`, `cor_configs` (o equivalentes EnArt) para describir sectores, casos de uso, controles y artefactos requeridos.
4. **Plantillas parametrizadas:** FRIA, reportes, workflows, dashboards se parametrizan con variables de catálogo (`sector_code`, `regulation_code`), evitando duplicidades.
5. **Policies as data:** reglas Drools, matrices RACI, límites SLA se versionan y almacenan en repositorios de políticas (git + catálogo), no en tablas específicas.
6. **Kits reutilizables:** frameworks FaaS empaquetan configuración (YAML/JSON), scripts y enlaces a catálogos; la activación se realiza en tiempo de despliegue sin migraciones.

---

## 🔧 Componentes del Metamodelo

- **Catálogo Sectorial (`cor_catalogitem`):**
  - `code`: identificador (ej. `FIN_BANKING`, `EDU_HE`).
  - `category`: `SECTOR` / `SUBSECTOR` / `REGULATION`.
  - `attributes`: JSONB con pares clave-valor (artículos EU AI Act, autoridad supervisora, paquetes FaaS asociados).

- **Configuración FaaS (`cor_configs`):**
  - `config_set`: `FAAS_FRAMEWORKS`.
  - `config_key`: nombre del framework (`FINANCE_BANKING_v1.0`).
  - `config_payload`: YAML/JSON con políticas activas, plantillas FRIA, mapeo de métricas y dashboards.

- **Políticas Drools (`cor_policyregistry`):**
  - `policy_type`: `SECTOR_RULES`.
  - `sector_code`: referencia al catálogo.
  - `policy_payload`: DRL versionado + metadatos (versión, responsable, fecha vigencia).

- **Workflows/Dashboards:**
  - Se almacenan en repos repositorios (`docs/bpmn`, `portal/dashboards`) etiquetados por `sector_code` en metadata.
  - Registro en catálogo `cor_catalogitem` con relación `linked_resource`.

- **Parametrización de eventos:**
  - `gov_governance_events.metadata` incluye claves estándar (`sector_code`, `use_case_code`, `regulator_ref`).
  - `gov_governance_results.result_payload` encapsula métricas específicas sin cambiar columnas.

---

## 📋 Prompts transversales a generar

### PROMPT 26.1 - Catálogo sectorial (ejecución)

**Objetivo:** Registrar sectores, subsectores y regulaciones en EnArt sin alterar el modelo físico, asegurando la trazabilidad multigeografía.

**Entradas clave:**
- `docs/compliance/*/01_requisitos.md` (requisitos sectoriales y sub-sectoriales).
- `PROMPTS_00_PLAYBOOK.md` (secuencia operativa) y `PROMPTS_19` a `PROMPTS_45` (prompts sectoriales existentes).
- Listado de regulaciones y estándares vigentes (EU AI Act, ISO/IEC 42001, NIST RMF, normas locales LATAM).

**Prompt operativo para agente EnArt:**
```markdown
ACTÚA COMO DATA GOVERNANCE LEAD DE CODEFLOWX.
1. Crear el catálogo `SECTOR_GOVERNANCE` en `cor_catalogue` sin añadir nuevas tablas.
2. Registrar los 22 sectores analizados (`FIN`, `EDU`, `SAL`, `TEC`, etc.) usando `cor_catalogitem.code` con prefijo sectorial (ej. `FIN_BANKING`, `FIN_INSURANCE`).
3. Añadir subsectores y casos de uso (`FIN_BANKING_IFRS9`, `FIN_BANKING_AML`, `EDU_HE_PROCTORING`), referenciando su `parent_code`.
4. Cargar `attributes` en JSONB con las siguientes claves obligatorias:
   - `regulation_refs` (lista de artículos y normas sectoriales)
   - `authority_refs` (supervisores nacionales/regionales)
   - `faas_framework_code` (nombre del paquete de configuración)
   - `risk_profile` (ALTO/MEDIO/BAJO)
   - `fria_templates`, `bpmn_refs`, `drools_packages`, `metrics_kpis`
5. Registrar geografía soportada (`geo_scope`: `EU`, `LATAM`, `BR`, etc.) y estado (`version`: `1.0.0`).
6. Validar que todos los sectores del roadmap comercial (`ADOPCION_IA_SECTORES_ESPANA_NOV2025.md`) estén cubiertos y que cada subcarpeta `docs/compliance/<sector>/` tenga correspondencia en catálogo.
```

**Entrega mínima esperada:**
- Archivo de carga `config/enart/catalogues/SECTOR_GOVERNANCE.json` con 22 sectores y 47 subsectores.
- Matriz de trazabilidad `reports/sector_catalogue_traceability.csv` enlazando `sector_code` ↔ `archivo requisitos` ↔ `prompt principal`.

**Validación:**
- Ejecutar script de consistencia (`scripts/qa/validate_catalogue_sector.py`) verificando unicidad de códigos y presencia de atributos obligatorios.
- Confirmar en EnArt UI que cada `sector_code` muestra jerarquía completa y metadatos JSON.

### PROMPT 26.2 - Configuración FaaS (ejecución)

**Objetivo:** Versionar frameworks sectoriales en `cor_configs` reutilizando catálogos y manteniendo núcleo inmutable.

**Entradas clave:**
- Artefactos configurables por sector (BPMN, reglas Drools, dashboards, conectores) definidos en los prompts sectoriales.
- Metamodelo de pipelines Python (`PROMPTS_10`) y Java (`PROMPTS_08`).

**Prompt operativo para agente EnArt:**
```markdown
ACTÚA COMO ARQUITECTO DE CONFIGURACIÓN CODEFLOWX.
1. Para cada `sector_code` activo, crear entrada en `cor_configs` con `config_set = "FAAS_FRAMEWORKS"` y `config_key = "<SECTOR>_v1.0.0"`.
2. Construir `config_payload` en YAML con las secciones:
   - `catalog_refs`: sector y subsectores vinculados.
   - `workflows`: lista de BPMN (`id`, `version`, `repo_path`).
   - `rules`: paquetes Drools (`package`, `git_tag`).
   - `microservices`: bindings hacia `leka-*` con flags de activación.
   - `dashboards`: plantillas UI (`zul_view`, `dataset_id`).
   - `report_templates`: FRIA, informes regulatorios, matrices RACI.
   - `geo_scope` y `compliance_level` (Art. 6, high-risk, GPAI, etc.).
3. Declarar dependencias (`requires_frameworks`) para reutilizar aceleradores comunes (ej. `BASE_EU_HIGHRISK`).
4. Publicar payloads en repositorio `infrastructure/config/faas/<sector>/framework.yaml` y sincronizar con EnArt vía API.
5. Documentar en `PROMPTS_00_PLAYBOOK.md` el proceso de activación usando esta configuración.
```

**Snippet YAML de referencia:**
```yaml
config_set: FAAS_FRAMEWORKS
config_key: FINANCE_BANKING_v1.0.0
config_payload:
  catalog_refs:
    sector_code: FIN_BANKING
    sub_sectors:
      - FIN_BANKING_IFRS9
      - FIN_BANKING_AML
  workflows:
    - id: conformity_assessment_v1
      repo_path: src/main/resources/processes/conformity-assessment-process.bpmn20.xml
      sla_profile: high_risk_default
  rules:
    - package: com.codeflowx.rules.financial.aml
      git_tag: v1.0.0
  microservices:
    - service_id: leka-conformity-assessment
      activation: mandatory
  dashboards:
    - zul_view: /zul/compliance/finance/basel_dashboard.zul
      dataset_id: governance_metrics_finance
  report_templates:
    fria_template: templates/fria/finance/default.docx
  geo_scope:
    - EU
    - LATAM
  compliance_level: high_risk
```

**Validación:**
- Testear carga con `scripts/qa/validate_faas_config.py` asegurando referencias existentes.
- Revisar que `codeflowx-govern-portal` consume la configuración sin requerir migraciones.

### PROMPT 26.3 - Políticas Drools como datos (ejecución)

**Objetivo:** Centralizar reglas en `cor_policyregistry` para despliegues multi-sector.

**Entradas clave:**
- Packages Drools definidos en prompts sectoriales.
- Reglas comunes (`base_high_risk.drl`, `bias_mitigation.drl`).

**Prompt operativo:**
```markdown
ACTÚA COMO POLICY ENGINE OWNER.
1. Crear registro por sector/subsector en `cor_policyregistry` con `policy_type = "SECTOR_RULES"` y `policy_code = "<SECTOR>_<TOPICO>_v1.0.0"`.
2. Incluir `policy_payload` en formato JSON con campos:
   - `drl_source` (contenido codificado base64 o ruta git).
   - `activation_conditions` (metadatos Art. EU AI Act, ISO).
   - `decision_tables` (referencia DMN cuando aplique).
   - `rollback_plan` y `test_suite`.
3. Versionar políticas en repos `rules/drools/<sector>/<policy>.drl` y vincular etiqueta git en `policy_metadata`.
4. Registrar matriz RACI y aprobaciones (`approved_by`, `valid_until`).
5. Conectar con `PROMPTS_18` para validación automática vía JSON Schema.
```

**Plantilla de carga JSON:**
```json
{
  "policy_type": "SECTOR_RULES",
  "policy_code": "FIN_BANKING_AML_v1.0.0",
  "sector_code": "FIN_BANKING",
  "policy_payload": {
    "drl_source": "git::rules/drools/fin_banking/aml_rules.drl@v1.0.0",
    "activation_conditions": {
      "articulos": ["EU_AI_ACT_ART_10", "EU_AI_ACT_ART_13"],
      "iso": ["ISO_37301", "ISO_27001"],
      "region": ["EU", "LATAM"]
    },
    "decision_tables": ["dmn/fin/aml_screening_v1.dmn"],
    "rollback_plan": "policy_rollback/fin_aml_v1.md",
    "test_suite": "tests/drools/fin_aml_golden.feature"
  },
  "metadata": {
    "version": "1.0.0",
    "approved_by": "governance_board",
    "valid_from": "2025-11-01",
    "valid_until": "2026-05-01"
  }
}
```

**Validación:**
- Ejecutar pruebas con `mvn test -Pdrools-regression` asegurando compilación de DRL.
- Revisar auditoría de cambios (hash + firma) conforme Art. 19.

### PROMPT 26.4 - Parametrización de eventos/resultados (ejecución)

**Objetivo:** Normalizar metadatos en `gov_governance_events` y `gov_governance_results` para soportar nuevas geografías.

**Entradas clave:**
- JSON Schemas actuales (ver `PROMPTS_18_JSON_SCHEMAS_VALIDATION.md`).
- Requerimientos de auditoría Art. 19 y 71 EU AI Act.

**Prompt operativo:**
```markdown
ACTÚA COMO PRODUCT OWNER DEL DATA LAKE DE GOVERNANCE.
1. Definir `metadata_schema` estándar con claves obligatorias (`sector_code`, `use_case_code`, `regulation_refs`, `sla_profile`, `geo_scope`, `tenant_id`).
2. Documentar JSON Schema en `docs/compliance/jsonschema/governance_event_v1.json` y `governance_result_v1.json`.
3. Implementar validadores en `codeflowx-governance-api` (módulo Java) que rechacen eventos sin `sector_code` registrado en catálogo.
4. Actualizar ETL en `leka-orchestrator` para enrutar métricas respetando `sector_code`/`geo_scope`.
5. Registrar plantilla de hashing (`hash_chain_profile`) alineada con `ImmutableLoggingBusinessService`.
```

**Fragmento JSON Schema:**
```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "title": "GovernanceEvent",
  "type": "object",
  "required": ["event_id", "sector_code", "use_case_code", "regulation_refs", "timestamp", "tenant_id"],
  "properties": {
    "sector_code": {
      "type": "string",
      "pattern": "^[A-Z_]{3,40}$"
    },
    "use_case_code": {
      "type": "string",
      "pattern": "^[A-Z0-9_]{5,60}$"
    },
    "regulation_refs": {
      "type": "array",
      "items": { "type": "string" },
      "minItems": 1
    },
    "sla_profile": { "type": "string" },
    "metadata": { "type": "object" }
  }
}
```

**Validación:**
- Ejecutar `mvn test -Pmetadata-schema` en `codeflowx-govern-backend`.
- Verificar ingestión en TimescaleDB sin columnas adicionales y con hash chain estable.

### PROMPT 26.5 - Portal y reporting parametrizado (ejecución)

**Objetivo:** Alinear vistas y reportes del portal a configuraciones FaaS sin clonar UI.

**Entradas clave:**
- Definiciones de dashboards vigentes (`portal/dashboard/*.json`).
- Plantillas FRIA y reportes regulatorios por sector.

**Prompt operativo:**
```markdown
ACTÚA COMO TECH LEAD DEL PORTAL CODEFLOWX.
1. Crear plantillas ZUL parametrizadas (`/zul/compliance/faas/sector_dashboard.zul`) que cargan componentes según `sector_code` recibido.
2. Exponer servicio en `ViewModel` (`SectorDashboardViewModel`) que consulta `FAAS_FRAMEWORKS` para decidir widgets, indicadores y workflows disponibles.
3. Configurar datasets en `reports/datasets/faas/` utilizando vistas SQL que filtran por `metadata->>'sector_code'`.
4. Generar reportes automáticos (PDF/Word) con `faas_report_builder` usando variables `${sector_code}`, `${faas_framework_code}` y templates definidos.
5. Documentar procedimiento de onboarding en `PROMPTS_00_PLAYBOOK.md` para que agentes sepan activar dashboards sectoriales.
```

**Artefactos esperados:**
- Vista reutilizable `SectorDashboardViewModel.java` y `sector_dashboard.zul` parametrizada (sin clase nueva por sector).
- Configuración `reporting/faas/templates/index.json` con mapping de reportes ↔ sector.

**Validación:**
- Probar en `codeflowx-portal` que cambiar `sector_code` en sesión alterna KPIs y workflows sin redeploy.
- Revisar que datasets usan filtros JSONB (`metadata ->> 'sector_code'`) y cumplen segregación multi-tenant.

---

## ✅ Checklist de validación

- [x] Catálogo sectorial cargado con jerarquía y atributos necesarios.
- [x] Frameworks FaaS registrados como configuraciones (sin migraciones DB).
- [x] Políticas Drools versionadas y vinculadas a sectores vía catálogo.
- [x] Payloads estándar en `gov_*` validados contra JSON Schema.
- [x] Dashboards/reportes parametrizados por `sector_code` sin tablas específicas.
- [x] Documentación de extensibilidad para nuevos países/regulaciones disponible.

### Resumen de ejecución (alpha)

- Se generó el catálogo maestro `sector_catalogue.json` con 26 sectores/subsectores, referencias normativas y métricas asociadas.
- Se creó `faas_frameworks.yaml` agrupando workflows BPMN, microservicios Python, dashboards y reportes parametrizados por `sector_code`.
- Se construyó `policy_registry.json` que vincula cada sector/subsector con la regla Drools común `faas-sector-governance.drl`.
- Nuevos JSON Schema (`governance_event_v1.json`, `governance_result_v1.json`) definen la estructura estándar para eventos/resultados de gobierno.
- `SectorMetamodelService` + records (`SectorCatalogue`, `FaasFrameworkCollection`, `PolicyRegistry`) permiten consumir el metamodelo desde Spring.
- Vista ZUL `sector-dashboard.zul` y `SectorDashboardViewModel` muestran catálogo, frameworks y políticas sin duplicar artefactos por vertical.
- Documento actualizado con checklist y artefactos entregados para guiar a agentes y a DevOps.
- Se creó el módulo Maven `codeflowx.govern.faas` dentro de `nocode.service`, empaquetando clases, recursos y reglas para reutilizarlos desde front, microservicios o SDKs.

### Pendientes siguientes iteraciones

- Sembrar los artefactos (`sector_catalogue.json`, `faas_frameworks.yaml`, `policy_registry.json`) en las tablas `cor_catalogue`, `cor_configs`, `cor_policyregistry` mediante script o loader oficial.
- Conectar el dashboard sectorial al menú del portal y preparar datasets/reports definitivos (`reports/datasets/faas/*`, plantillas FRIA) filtrando por `metadata->>'sector_code'`.
- Validar extremo a extremo: ejecutar `mvn test -Pmetadata-schema`, pruebas de carga ZK y smoke-test de la regla Drools contra eventos reales.
- Documentar en `PROMPTS_00_PLAYBOOK.md` el proceso de activación (volver a ejecutar cuando se publique el loader) y versionar procedimientos DevOps.

### Artefactos implementados (alpha)

- `codeflowx.govern.faas/src/main/resources/metamodel/sector_catalogue.json`
- `codeflowx.govern.faas/src/main/resources/metamodel/faas_frameworks.yaml` (frameworks base + catálogo común)
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/EDU_SERVICES/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/FIN_BANKING/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/FIN_INSURANCE/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/PRO_SERVICES/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/HOSPITALITY_TOURISM/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/AGRIFOOD/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/ENV_SERVICES/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/frameworks/TECH_PLATFORMS/faas_framework.yaml`
- `codeflowx.govern.faas/src/main/resources/metamodel/policy_registry.json` (políticas comunes)
- `codeflowx.govern.faas/src/main/resources/metamodel/policies/**/policy_registry.json` (políticas por framework: EDU, FIN_BANKING, FIN_INSURANCE, PRO_SERVICES, HOSPITALITY_TOURISM, AGRIFOOD, ENV_SERVICES, TECH_PLATFORMS)
- `codeflowx.govern.faas/src/main/resources/rules/faas/faas-sector-governance.drl`
- `codeflowx.govern.faas/src/main/java/com/codeflowx/govern/faas/*.java`
- `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/SectorDashboardViewModel.java`
- `suinsit.nova.web/src/main/webapp/console/gobierno/compliance/sector-dashboard.zul`

Los frameworks sectoriales correspondientes a los prompts **19 (Banca)**, **20 (Seguros)**, **07 (Servicios Profesionales)**, **12 (Hostelería/Turismo)**, **15 (Agroalimentario)**, **19_Servicios_Medioambientales (PROMPTS_42)** y **20_Tecnología_Plataformas (PROMPTS_43)** quedan ahora desacoplados en paquetes dedicados dentro de `codeflowx.govern.faas`, alineados con la estrategia FaaS modular.


