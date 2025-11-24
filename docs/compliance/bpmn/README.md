# Documentación BPMN - CodeflowX Govern

Esta carpeta contiene toda la documentación relacionada con los procesos BPMN del proyecto CodeflowX Govern.

## Estructura

### Documentos principales

- **[BPMN_CATALOG.md](./BPMN_CATALOG.md)**: Catálogo completo de todos los procesos BPMN, organizados por categorías (AI-OS, Compliance, Audit, Metrics). Incluye responsabilidades funcionales, cobertura normativa, integraciones AI-OS y estado actual.

- **[BPMN_AUDIT_STATUS.md](./BPMN_AUDIT_STATUS.md)**: Estado detallado de la auditoría de cada proceso BPMN. Indica qué documentación existe (funcional/técnica), estado de implementación, gaps identificados y próximos pasos recomendados.

- **[BPMN_RUNTIME_VALIDATION_PLAN.md](./BPMN_RUNTIME_VALIDATION_PLAN.md)**: Plan de validación runtime para verificar la correcta ejecución de procesos BPMN, delegates y reglas Drools. Incluye estrategia de smoke tests, mocks para servicios externos y checklist de ejecución.

- **[BPMN_PROCESS_GUIDES.md](./BPMN_PROCESS_GUIDES.md)**: Guías de referencia rápida para desarrolladores.

- **[BPMN_API_MAPPING.md](./BPMN_API_MAPPING.md)**: Mapeo completo entre endpoints del API AI-OS y procesos BPMN. Identifica qué endpoints deben disparar procesos, qué procesos no tienen endpoints asociados, y propone nuevos endpoints para integración externa completa.

### Documentación por categoría

#### AI-OS (`aios/`)
Procesos relacionados con el AI Operating System:
- Onboarding de componentes AI
- Aprobaciones (agents, models, prompts, adapters, finetuning)
- Runtime health y monitoreo
- Marketplace y publicación
- Policy review
- Evaluaciones (LLM, RAG, Model)
- Deployment automation
- Model retraining orchestration
- External model approval

Cada proceso tiene su carpeta con:
- `functional.md`: Documentación funcional para usuarios/negocio
- `technical.md`: Documentación técnica para desarrolladores (delegates, reglas, variables, integraciones)

#### Compliance (`compliance/`)
Procesos de cumplimiento regulatorio (EU AI Act, GDPR):
- **Compliance monitoring** (`compliance-monitoring/`): Monitoreo continuo de cumplimiento regulatorio
- **Conformity assessment** (`conformity-assessment/`): Evaluación de conformidad con normativas
- **Internal conformity assessment** (`internal-conformity/`): Evaluación interna de conformidad
- **Consent management** (`consent-management/`): Gestión de consentimientos GDPR
- **Incident reporting** (`incident-reporting/`): Reporte de incidentes regulatorios
- **FRIA** (`fria/`): Fundamental Rights Impact Assessment (EU AI Act)
- **EU Database registration** (`eu-database-registration/`): Registro en base de datos EU
- **Risk assessment** (`risk-assessment/`): Evaluación de riesgos
- **Ethics review** (`ethics-review/`): Revisión ética de sistemas AI

#### Audit (`audit/`)
Procesos de auditoría y gestión ISO 42001 / ISO 38507:
- ISO 42001 internal audit
- ISO 42001 management review
- ISO 42001 corrective action
- ISO 42001 competence gap
- ISO 42001 AI decommissioning
- ISO 38507 board decision

#### Metrics (`metrics/`)
Procesos de monitoreo operacional:
- Bias detection
- Drift detection
- Performance degradation
- Alert response
- Incident response RCA
- Dataset quality

## Ubicación de los archivos BPMN

Los archivos `.bpmn` y `.bpmn20.xml` se encuentran en:
```
codeflowx.govern.workflow.lib/src/main/resources/processes/
├── aios/
├── compliance/
├── audit/
└── metrics/
```

## Convenciones y especificaciones

- **ENART Specification**: Todos los delegates y reglas Drools siguen las convenciones ENART.
- **Naming**: Los delegates siguen el patrón `{ProcessName}{TaskName}Delegate`.
- **Drools Facts**: POJOs en `com.codeflowx.govern.workflow.drools.facts`.
- **Drools Rules**: Archivos `.drl` en `src/main/resources/rules/{category}/`.

## Estado actual

- ✅ **Documentación funcional y técnica**: Completada para todos los procesos
- ✅ **Delegates Java**: Implementados siguiendo ENART
- ✅ **Reglas Drools**: Creadas y configuradas en `kmodule.xml`
- 🔄 **Clientes Python**: Pendientes (marcados con `@todo` en delegates)
- 🔄 **Validación runtime**: En planificación (ver `BPMN_RUNTIME_VALIDATION_PLAN.md`)

## Próximos pasos

1. Implementar clientes Python para microservicios externos
2. Ejecutar validación runtime (smoke tests, mocks)
3. Actualizar referencias cruzadas en otros documentos
4. Completar tests unitarios/integración para delegates y reglas

