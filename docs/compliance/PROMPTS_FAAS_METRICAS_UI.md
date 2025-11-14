# PROMPT: Plantillas de Métricas, Dashboards y UI FaaS

## 1. Objetivo
Definir la estructura estándar para métricas, dashboards ZK y componentes UI asociados a los frameworks FaaS, permitiendo a equipos externos extender CodeflowX con mínima fricción.

## 2. Artefactos a producir
1. Catálogo de métricas (`metrics/<framework>/catalog.json`).
2. Dashboards ZK (`/console/gobierno/compliance/faas/<framework>-framework.zul`).
3. ViewModels Java (`com.codeflowx.govern.viewmodel.faas.<framework>.<Framework>ViewModel`).
4. Configuración de datasets/reporting (`datasets/faas/<framework>-dataset.yaml`).

## 3. Catálogo de Métricas (JSON)
- Estructura base:
  ```json
  {
    "framework": "TECH_PLATFORMS_v1.0.0",
    "version": "1.0.0",
    "kpis": [
      {
        "id": "governance.kpi.tech.uptime_sla",
        "label": "Disponibilidad SLA",
        "description": "Porcentaje de cumplimiento de SLA mensual",
        "unit": "%",
        "type": "PERCENTAGE",
        "aggregation": "MONTHLY",
        "criticality": "HIGH",
        "formula": "uptime_hours / total_hours * 100",
        "data_source": "faas-tech-platforms-micro",
        "dimensions": ["geo", "provider", "tier"],
        "thresholds": {
          "warning": 97.5,
          "critical": 95.0
        }
      }
    ]
  }
  ```
- Validar con JSON Schema `faas_metric_catalog_v1.json` (crear si falta).

## 4. ViewModels (ZK)
- Extender `AbstractFaasViewModel` (crear en módulo faas) con métodos:
  - `loadMetrics()` llama al micro mediante `FaasMetricsClient`.
  - `resolveCapabilities()` obtiene estado de normativas.
  - `loadProcesses()` consume `SectorMetamodelService` para listar procesos.
- Inyectar `TenantContext`, `GeoContext`, `FaasMetricsClient`.
- Exponer colecciones `List<KpiItem>`, `List<ProcessDescriptor>`, `List<CapabilityStatus>`.
- Usar `@Init` para cargar catálogo JSON desde recursos.

## 5. ZUL Layout
- Basado en layout responsive 12 columnas:
  - Panel 1: cabecera framework + estado regulatorio.
  - Panel 2: tarjetas KPI (component `faas-kpi-card.zul`).
  - Panel 3: tabla de procesos BPMN vinculados (botón “Ver diagrama”).
  - Panel 4: acordeón de normativas y evidencias.
- Binder ViewModel: `viewModel="@id('vm') @init('com.codeflowx.govern.viewmodel.faas.tech.TechPlatformsViewModel')"`.
- Componentes reutilizables en `/console/gobierno/compliance/faas/_shared/`:
  - `faas-kpi-card.zul`
  - `faas-capability-badge.zul`
  - `faas-process-table.zul`

## 6. Dataset y Reporting
- Definir YAML en `datasets/faas/<framework>-dataset.yaml`:
  ```yaml
  dataset: governance_metrics_tech_platforms
  source: faas-tech-platforms-micro
  refresh: 5m
  dimensions:
    - geo
    - provider
  measures:
    - uptime_sla
    - incidents_total
  ```
- Registrar en `datasets/datasets-index.yaml`.

## 7. Flujo de Trabajo
1. Leer `sector_catalogue.json` para conocer `metrics_kpis` requeridos.
2. Crear `catalog.json` con KPI necesarios (marcar `status: pending` si no existe fuente).
3. Implementar ViewModel + ZUL siguiendo plantilla.
4. Conectar con micro (si no está disponible, mock mediante `FaasMetricsClientStub`).
5. Ajustar `metadata` del framework con ruta ZUL y dataset.
6. Actualizar documentación del framework en `PROMPTS_26`.

## 8. Testing
- Pruebas ViewModel con `ZATS` simulando respuestas del micro.
- Validar catálogos con JSON Schema (usar `mvn -Pjson-validate`).
- UI: snapshots Selenium/ZATS para componentes clave.

## 9. Checklist
- [ ] `catalog.json` creado y validado.
- [ ] ViewModel extiende clase base y expone comandos.
- [ ] ZUL incorpora componentes compartidos.
- [ ] Dataset YAML registrado.
- [ ] Referencias actualizadas en metamodelo (`dashboards`, `metrics`).
- [ ] Documentación del framework actualizada.

## 10. Backlog asociado
- Crear `AbstractFaasViewModel` + componentes compartidos.
- Generar JSON Schema `faas_metric_catalog_v1.json`.
- Añadir pipeline de validación de catálogos.
- Documentar ejemplos por normativa (ENS, ISO 27001, Agro).
