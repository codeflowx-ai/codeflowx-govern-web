# Requisitos de Cumplimiento – Industria Manufacturera

## 1. Resumen y alcance
- **Sector**: Industria Manufacturera (`MANUFACTURING`).
- **Cobertura geográfica**: Unión Europea (UE) y Latinoamérica (LATAM).
- **Fecha de corte documental**: 2024-07-01 (aprobación formal del EU AI Act y publicación consolidada del Reglamento (UE) 2023/1230 sobre máquinas).
- **Objetivo**: definir las obligaciones normativas y controles mínimos que deben soportar los procesos FaaS de CodeflowX para fabricantes industriales con líneas de producción automatizadas y uso intensivo de IA.

## 2. Normativa y estándares aplicables
| Referencia | Jurisdicción | Contenido clave | Evidencia requerida |
|------------|--------------|-----------------|---------------------|
| Reglamento (UE) 2024/1689 (AI Act) – Anexo III.1 | UE | Clasifica sistemas de IA para control de procesos industriales como **alto riesgo**. Exige gestión de riesgos, trazabilidad, documentación técnica y supervisión humana. | Matriz de riesgos IA, registro de incidentes, reportes de conformidad. |
| Directiva (UE) 2022/2555 (NIS2) | UE | Impone medidas de ciberseguridad a operadores esenciales (manufactura crítica). Requiere gestión de incidentes en <24h y reporting estructurado. | Registro de eventos NIS2, métricas MTTR, evidencias de notificación. |
| Reglamento (UE) 2023/1230 (Máquinas) | UE | Sustituye la Directiva 2006/42/CE. Impone evaluación de conformidad, FRIA, documentación técnica y declaración UE. | FRIA firmada, declaración UE almacenada, historial de modificaciones. |
| ISO 9001:2015 | Global | Gestión de calidad y trazabilidad de procesos. | Planes de control, KPIs de calidad, auditorías internas. |
| IATF 16949:2016 | Global (Automoción) | Requisitos específicos de calidad automotriz, aplicable a fabricantes Tier 1/2. | Plan APQP, registros PPAP, planes de acción 8D. |
| ISO 45001:2018 | Global | Gestión de seguridad y salud ocupacional. | Evaluaciones de riesgos HSE, incidentes, planes de mitigación. |
| IEC 62443-3-3:2013 | Global | Seguridad de sistemas de control industrial (ICS). | Evidencias de hardening, segmentación, pruebas de penetración OT. |
| Buenas Prácticas de Fabricación (EU GMP Volumen 4) | UE | Gestión de calidad y documentación para procesos con impacto sanitario (dispositivos, life sciences). | Registros de lote, liberaciones QA, desviaciones. |

> Nota: mantener actualizada la lista de normativas específicas por subsector en `faas-framework-manufacturing-*/docs`.

## 3. Controles obligatorios
1. **Gestión de riesgos IA**: aplicación del proceso FRIA conforme al EU AI Act y al Reglamento de Máquinas. Debe contener identificación de peligros, mitigación y supervisión humana explicable.
2. **Monitorización de incidentes críticos**: canal único para eventos NIS2, IoT/OT y seguridad laboral. Requiere clasificación automática, telemetría y escalado humano bajo SLA `regulatory_critical`.
3. **Declaración UE y registros de conformidad**: generación automatizada de la Declaración UE de conformidad, con firmas electrónicas y almacenamiento en repositorio cifrado.
4. **Registro de telemetría OT**: integración con `aioTelemetryCollectorDelegate` para sincronizar evidencias de planta en ImmutableLog.
5. **Control de proveedores críticos**: evaluación periódica de proveedores OT/IT con métricas de cumplimiento contractual y ciberseguridad.

## 4. Datos requeridos (modelo 3FN)
| Tabla | Prefijo | Campos clave | Fuente |
|-------|---------|--------------|--------|
| `man_fria_assessment` | `man_` | `id` (PK), `framework_id`, `line_id`, `risk_level`, `residual_risk`, `approved_by`, `approved_at`, `immutable_log_ref` | Proceso FRIA |
| `man_incident_registry` | `man_` | `id`, `framework_id`, `event_code`, `severity`, `reported_at`, `resolved_at`, `root_cause`, `immutable_log_ref` | Incident Reporting |
| `man_conformity_record` | `man_` | `id`, `framework_id`, `product_family`, `declaration_uri`, `signature_hash`, `issued_at`, `valid_until` | Declaración UE |
| `man_supplier_kpi` | `man_` | `id`, `framework_id`, `supplier_id`, `kpi_code`, `value`, `period_start`, `period_end`, `evidence_uri` | Evaluación proveedores |

> Cada tabla debe incluir columnas de auditoría (`created_at`, `created_by`, `updated_at`, `updated_by`) y claves foráneas con constraints explícitas según `PROMPTS_BASE_DATOS.md`.

## 5. Flujos de trabajo requeridos
- **Compliance Monitoring v1**: combina evaluación continua de métricas con análisis Drools para disparar medidas correctivas.
- **Incident Reporting v1**: orquesta respuesta NIS2 y HSE, integrando `aioPolicyEnforcementDelegate` y escalado HITL.
- **Conformity Assessment v1**: coordina FRIA, validaciones técnicas y emisión de la Declaración UE.
- **FRIA Process v1**: detalla pasos de identificación de riesgos, validación humana y registro en ImmutableLog.
- **EU Database Registration v1**: gestiona altas y actualizaciones en bases regulatorias (ej. EUDAMED para dispositivos, ICSMS para maquinaria).

## 6. Microservicios obligatorios
| Microservicio | Rol | Integraciones |
|----------------|-----|---------------|
| `leka-conformity-assessment` | Genera plantillas FRIA y declaraciones UE. | `aioPolicyEvaluationService`, repositorio documental. |
| `leka-fria-generator` | Calcula riesgos residuales e indicadores de control. | Kafka `govern.faas.events`, `man_fria_assessment`. |
| `leka-eu-declaration-generator` | Firma y publica la declaración UE. | Gestor de firmas, almacenamiento ECS. |
| `leka-orchestrator` | Coordina flujos BPMN y orquestación AI OS. | Delegates AI OS, Flowable. |
| `leka-bias-detection-service` (opcional) | Evalúa sesgos en modelos IA. | Data lake IA, pipeline ML. |
| `leka-llm-evaluation` (opcional) | Evalúa consistencia de respuestas generadas por IA. | AI OS, prompts QA. |

## 7. Evidencias mínimas
- Registro diario en ImmutableLog de eventos críticos.
- Reporte FRIA por línea de producción y versión del sistema IA.
- Declaración UE vigente y accesible para autoridades.
- Registro de incidentes y acciones correctivas (8D).
- Métricas KPI: `governance.kpi.manufacturing.risk_index`, `governance.kpi.art71_compliance_rate`, `governance.kpi.incident_mttr`, `governance.kpi.agreement_coverage`.

## 8. Responsables
- **ComplianceOwner**: validar FRIA y aprobar declaraciones.
- **RiskOfficer**: monitorizar KPIs, resolver incidentes, autorizar HITL.
- **AutomationServices**: operar microservicios, integrar telemetría y garantizar trazabilidad ImmutableLog.

## 9. Checklist de verificación
- [ ] Confirmar vigencia normativa (AI Act, NIS2, Reglamento de Máquinas) y registrar referencias.
- [ ] Verificar que las tablas `man_*` están implementadas y auditadas.
- [ ] Validar despliegue de microservicios obligatorios con endpoints securizados (OAuth2).
- [ ] Ejecutar pruebas Flowable (`ProcessEngineRule`) para cada BPMN.
- [ ] Ejecutar pruebas Drools (`KieServices`) con escenarios críticos (riesgo alto, sesgo detectado, incumplimiento NIS2).
- [ ] Registrar ejecución en `AgentExecutionLogEntity` con checklist completada.
# Requisitos de Cumplimiento – Industria Manufacturera

## 1. Resumen y alcance
- **Sector**: Industria Manufacturera (`MANUFACTURING`).
- **Cobertura geográfica**: Unión Europea (UE) y Latinoamérica (LATAM).
- **Fecha de corte documental**: 2024-07-01 (aprobación formal del EU AI Act y publicación consolidada del Reglamento (UE) 2023/1230 sobre máquinas).
- **Objetivo**: definir las obligaciones normativas y controles mínimos que deben soportar los procesos FaaS de CodeflowX para fabricantes industriales con líneas de producción automatizadas y uso intensivo de IA.

## 2. Normativa y estándares aplicables
| Referencia | Jurisdicción | Contenido clave | Evidencia requerida |
|------------|--------------|-----------------|---------------------|
| Reglamento (UE) 2024/1689 (AI Act) – Anexo III.1 | UE | Clasifica sistemas de IA para control de procesos industriales como **alto riesgo**. Exige gestión de riesgos, trazabilidad, documentación técnica y supervisión humana. | Matriz de riesgos IA, registro de incidentes, reportes de conformidad. |
| Directiva (UE) 2022/2555 (NIS2) | UE | Impone medidas de ciberseguridad a operadores esenciales (manufactura crítica). Requiere gestión de incidentes en <24h y reporting estructurado. | Registro de eventos NIS2, métricas MTTR, evidencias de notificación. |
| Reglamento (UE) 2023/1230 (Máquinas) | UE | Sustituye la Directiva 2006/42/CE. Impone evaluación de conformidad, FRIA, documentación técnica y declaración UE. | FRIA firmada, declaración UE almacenada, historial de modificaciones. |
| ISO 9001:2015 | Global | Gestión de calidad y trazabilidad de procesos. | Planes de control, KPIs de calidad, auditorías internas. |
| IATF 16949:2016 | Global (Automoción) | Requisitos específicos de calidad automotriz, aplicable a fabricantes Tier 1/2. | Plan APQP, registros PPAP, planes de acción 8D. |
| ISO 45001:2018 | Global | Gestión de seguridad y salud ocupacional. | Evaluaciones de riesgos HSE, incidentes, planes de mitigación. |
| IEC 62443-3-3:2013 | Global | Seguridad de sistemas de control industrial (ICS). | Evidencias de hardening, segmentación, pruebas de penetración OT. |
| Buenas Prácticas de Fabricación (EU GMP Volumen 4) | UE | Gestión de calidad y documentación para procesos con impacto sanitario (dispositivos, life sciences). | Registros de lote, liberaciones QA, desviaciones. |

> Nota: mantener actualizada la lista de normativas específicas por subsector en `faas-framework-manufacturing-*/docs`.

## 3. Controles obligatorios
1. **Gestión de riesgos IA**: aplicación del proceso FRIA conforme al EU AI Act y al Reglamento de Máquinas. Debe contener identificación de peligros, mitigación y supervisión humana explicable.
2. **Monitorización de incidentes críticos**: canal único para eventos NIS2, IoT/OT y seguridad laboral. Requiere clasificación automática, telemetría y escalado humano bajo SLA `regulatory_critical`.
3. **Declaración UE y registros de conformidad**: generación automatizada de la Declaración UE de conformidad, con firmas electrónicas y almacenamiento en repositorio cifrado.
4. **Registro de telemetría OT**: integración con `aioTelemetryCollectorDelegate` para sincronizar evidencias de planta en ImmutableLog.
5. **Control de proveedores críticos**: evaluación periódica de proveedores OT/IT con métricas de cumplimiento contractual y ciberseguridad.

## 4. Datos requeridos (modelo 3FN)
| Tabla | Prefijo | Campos clave | Fuente |
|-------|---------|--------------|--------|
| `man_fria_assessment` | `man_` | `id` (PK), `framework_id`, `line_id`, `risk_level`, `residual_risk`, `approved_by`, `approved_at`, `immutable_log_ref` | Proceso FRIA |
| `man_incident_registry` | `man_` | `id`, `framework_id`, `event_code`, `severity`, `reported_at`, `resolved_at`, `root_cause`, `immutable_log_ref` | Incident Reporting |
| `man_conformity_record` | `man_` | `id`, `framework_id`, `product_family`, `declaration_uri`, `signature_hash`, `issued_at`, `valid_until` | Declaración UE |
| `man_supplier_kpi` | `man_` | `id`, `framework_id`, `supplier_id`, `kpi_code`, `value`, `period_start`, `period_end`, `evidence_uri` | Evaluación proveedores |

> Cada tabla debe incluir columnas de auditoría (`created_at`, `created_by`, `updated_at`, `updated_by`) y claves foráneas con constraints explícitas según `PROMPTS_BASE_DATOS.md`.

## 5. Flujos de trabajo requeridos
- **Compliance Monitoring v1**: combina evaluación continua de métricas con análisis Drools para disparar medidas correctivas.
- **Incident Reporting v1**: orquesta respuesta NIS2 y HSE, integrando `aioPolicyEnforcementDelegate` y escalado HITL.
- **Conformity Assessment v1**: coordina FRIA, validaciones técnicas y emisión de la Declaración UE.
- **FRIA Process v1**: detalla pasos de identificación de riesgos, validación humana y registro en ImmutableLog.
- **EU Database Registration v1**: gestiona altas y actualizaciones en bases regulatorias (ej. EUDAMED para dispositivos, ICSMS para maquinaria).

## 6. Microservicios obligatorios
| Microservicio | Rol | Integraciones |
|----------------|-----|---------------|
| `leka-conformity-assessment` | Genera plantillas FRIA y declaraciones UE. | `aioPolicyEvaluationService`, repositorio documental. |
| `leka-fria-generator` | Calcula riesgos residuales e indicadores de control. | Kafka `govern.faas.events`, `man_fria_assessment`. |
| `leka-eu-declaration-generator` | Firma y publica la declaración UE. | Gestor de firmas, almacenamiento ECS. |
| `leka-orchestrator` | Coordina flujos BPMN y orquestación AI OS. | Delegates AI OS, Flowable. |
| `leka-bias-detection-service` (opcional) | Evalúa sesgos en modelos IA. | Data lake IA, pipeline ML. |
| `leka-llm-evaluation` (opcional) | Evalúa consistencia de respuestas generadas por IA. | AI OS, prompts QA. |

## 7. Evidencias mínimas
- Registro diario en ImmutableLog de eventos críticos.
- Reporte FRIA por línea de producción y versión del sistema IA.
- Declaración UE vigente y accesible para autoridades.
- Registro de incidentes y acciones correctivas (8D).
- Métricas KPI: `governance.kpi.manufacturing.risk_index`, `governance.kpi.art71_compliance_rate`, `governance.kpi.incident_mttr`, `governance.kpi.agreement_coverage`.

## 8. Responsables
- **ComplianceOwner**: validar FRIA y aprobar declaraciones.
- **RiskOfficer**: monitorizar KPIs, resolver incidentes, autorizar HITL.
- **AutomationServices**: operar microservicios, integrar telemetría y garantizar trazabilidad ImmutableLog.

## 9. Checklist de verificación
- [ ] Confirmar vigencia normativa (AI Act, NIS2, Reglamento de Máquinas) y registrar referencias.
- [ ] Verificar que las tablas `man_*` están implementadas y auditadas.
- [ ] Validar despliegue de microservicios obligatorios con endpoints securizados (OAuth2).
- [ ] Ejecutar pruebas Flowable (`ProcessEngineRule`) para cada BPMN.
- [ ] Ejecutar pruebas Drools (`KieServices`) con escenarios críticos (riesgo alto, sesgo detectado, incumplimiento NIS2).
- [ ] Registrar ejecución en `AgentExecutionLogEntity` con checklist completada.
