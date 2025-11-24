# INCIDENCIAS Y RECOMENDACIONES - CUMPLIMIENTO EU AI ACT
## CodeflowX OS - Auditoría de Cumplimiento

**Fecha:** 2 de noviembre de 2025  
**Auditoría:** AUDITORIA_013_CUMPLIMIENTO_AI_ACT.md  
**Versión:** 1.0

---

## RESUMEN EJECUTIVO

**Total Incidencias:** 18  
**Críticas:** 8  
**Medias:** 7  
**Bajas:** 3

**Cumplimiento Actual:** 87%  
**Cumplimiento Proyectado (post-implementación):** 98%

---

## INCIDENCIAS CRÍTICAS (Bloqueadores de Certificación)

### INC-001: Documentación Técnica Incompleta (Anexo IV)
**GAP Relacionado:** GAP-045  
**Artículo:** Art. 11 + Anexo IV  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
La documentación técnica generada no cumple completamente con la estructura requerida por el Anexo IV del EU AI Act. Faltan secciones específicas y la integración con el Sistema de Gestión de Calidad (QMS).

**Evidencia:**
- Servicio `AIActDocumentationService` en desarrollo
- Cobertura actual: 70% de secciones Anexo IV
- Faltan: Proceso desarrollo completo, QMS linkage, Documentación cambios

**Recomendación:**
1. Completar generador de documentación técnica
2. Implementar todas las 8 secciones del Anexo IV:
   - Descripción general del sistema
   - Descripción detallada (algoritmos, lógica decisión)
   - Especificaciones técnicas (arquitectura, hardware, software)
   - Proceso de desarrollo y metodologías de prueba
   - Sistemas de gestión de calidad aplicados
   - Información sobre datos de entrenamiento
   - Evaluación de precisión, robustez, ciberseguridad
   - Documentación de cambios
3. Auto-generar desde metadata existente
4. Integrar con QMS (GAP-020)

**Esfuerzo Estimado:** 3 días  
**Responsable:** Equipo Backend + Documentación  
**Fecha Objetivo:** 15 de noviembre de 2025

---

### INC-002: Falta Generación Automática de Instrucciones de Uso
**GAP Relacionado:** GAP-016  
**Artículo:** Art. 13 + Art. 16.k  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
No existe generación automática de "Instrucciones de Uso" según Art. 13 del EU AI Act. Las instrucciones son un requisito obligatorio para proveedores de sistemas de alto riesgo.

**Evidencia:**
- No existe template de Instrucciones de Uso
- No hay auto-generación desde metadata
- Faltan elementos mandatorios Art. 13.3

**Recomendación:**
1. Crear template "Instructions for Use" per Art. 13
2. Implementar auto-generación desde metadata del modelo
3. Incluir todos los elementos Art. 13.3:
   - Identidad y datos de contacto del proveedor
   - Características, capacidades y limitaciones del sistema
   - Rendimiento en relación con grupos específicos
   - Cambios predeterminados del sistema
   - Medidas de supervisión humana
   - Vida útil prevista
   - Recursos informáticos y hardware necesarios
   - Medidas de mantenimiento
4. Crear entidad `InstructionsForUse`
5. Soporte multiidioma (español, inglés mínimo)

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo Backend  
**Fecha Objetivo:** 10 de noviembre de 2025

---

### INC-003: Sistema PMM No Formalizado Documentalmente
**GAP Relacionado:** GAP-017  
**Artículo:** Art. 72 + Art. 16.g  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
Aunque existe monitoreo continuo (leka-agent-monitoring), falta la formalización documental del sistema de vigilancia poscomercialización según Art. 72. No existe un "Post-Market Monitoring Plan" documentado ni reportes automáticos.

**Evidencia:**
- Monitoreo técnico operativo (24/7)
- Falta entidad `PostMarketMonitoringPlan`
- Falta BPMN proceso formal "Post-Market Monitoring"
- Falta auto-generación de reportes de vigilancia

**Recomendación:**
1. Crear entidad `PostMarketMonitoringPlan` con campos:
   - Descripción del sistema de monitoreo
   - Métricas a monitorear
   - Umbrales de alerta
   - Frecuencia de reportes
   - Responsables
2. Crear BPMN proceso "Post-Market Monitoring" (Art. 72)
3. Integrar con servicios de monitoreo existentes
4. Auto-generar "Post-Market Surveillance Report"
5. Implementar workflow de "Serious Incident Report"
6. Definir umbrales de incidentes y triggers de notificación
7. Vincular con Art. 49 registro

**Esfuerzo Estimado:** 2 días  
**Responsable:** Equipo Backend + BPMN  
**Fecha Objetivo:** 12 de noviembre de 2025

---

### INC-004: Falta Notificación Automática de Incidentes Graves
**GAP Relacionado:** GAP-025  
**Artículo:** Art. 73 + Art. 16.h  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
No existe definición formal de "incidente grave" según Art. 73 ni workflow automatizado para notificación a autoridades competentes. El sistema detecta incidentes pero no los clasifica ni notifica automáticamente.

**Evidencia:**
- Workflow `incident-response-rca-v1.bpmn` existe
- Falta definición de "serious incident" (Art. 73)
- Falta workflow de notificación a autoridades
- Falta entidad `SeriousIncident`

**Recomendación:**
1. Definir "serious incident" thresholds per Art. 73:
   - Muerte o lesión grave
   - Daño material significativo
   - Interrupción servicios esenciales
   - Violación derechos fundamentales
2. Crear BPMN proceso "Serious Incident Reporting"
3. Crear entidad `SeriousIncident` con campos:
   - Tipo de incidente
   - Severidad
   - Fecha detección
   - Sistemas afectados
   - Causa raíz
   - Acciones correctivas
   - Autoridad notificada
4. Implementar triggers automáticos de notificación
5. Crear template de reporte de incidente
6. Vincular con Art. 20 acciones correctivas
7. Integrar con base de datos de autoridades competentes (por Estado Miembro)

**Esfuerzo Estimado:** 2 días  
**Responsable:** Equipo Backend + BPMN  
**Fecha Objetivo:** 12 de noviembre de 2025

---

### INC-005: Falta Detección Específica de Adversarial Examples
**GAP Relacionado:** GAP-009  
**Artículo:** Art. 15.5  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
Aunque existen tests de robustez generales, falta detección específica de adversarial examples (entradas diseñadas para hacer que el modelo cometa errores). El Art. 15.5 requiere medidas específicas para prevenir y detectar estos ataques.

**Evidencia:**
- Tests de robustez generales existen
- Falta detección específica adversarial examples
- Falta testing adversarial robustness (FGSM, PGD, C&W)
- Falta sanitización de inputs contra ataques adversariales

**Recomendación:**
1. Crear nuevo microservicio `leka-adversarial-robustness` (port 8012)
2. Implementar algoritmos de detección adversarial:
   - FGSM (Fast Gradient Sign Method)
   - PGD (Projected Gradient Descent)
   - C&W (Carlini & Wagner)
3. Implementar métricas de robustez adversarial:
   - Epsilon-robustness
   - Adversarial accuracy
4. Integrar con pipeline de evaluación de modelos
5. Implementar sanitización de inputs
6. Endpoints:
   - `/api/adversarial/detect-examples`
   - `/api/adversarial/test-robustness`
   - `/api/adversarial/evaluate-epsilon-robustness`
7. Frameworks sugeridos: Foolbox, ART (Adversarial Robustness Toolbox), CleverHans

**Esfuerzo Estimado:** 2 días  
**Responsable:** Equipo ML/MLOps  
**Fecha Objetivo:** 20 de noviembre de 2025

---

### INC-006: Falta Detección de Model Evasion
**GAP Relacionado:** GAP-010  
**Artículo:** Art. 15.5  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
Falta detección específica de "model evasion" (evasión de modelos). El Art. 15.5 requiere medidas para prevenir, detectar y responder a intentos de evasión del modelo.

**Evidencia:**
- No existe detección de evasión de modelos
- Falta análisis de patrones de queries
- Falta detección de ataques black-box

**Recomendación:**
1. Incluir en microservicio `leka-adversarial-robustness` (INC-005)
2. Implementar análisis de patrones de queries
3. Detectar intentos de extracción de información
4. Implementar rate limiting y detección de anomalías en queries
5. Endpoint: `/api/adversarial/detect-evasion`

**Esfuerzo Estimado:** Incluido en INC-005  
**Responsable:** Equipo ML/MLOps  
**Fecha Objetivo:** 20 de noviembre de 2025

---

### INC-007: Falta Detección de Model Poisoning
**GAP Relacionado:** GAP-011  
**Artículo:** Art. 15.5  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
Falta detección de "model poisoning" (envenenamiento de modelos), especialmente en componentes preentrenados utilizados durante el entrenamiento. El Art. 15.5 requiere medidas específicas.

**Evidencia:**
- No existe detección de model poisoning
- Falta detección de backdoors en modelos preentrenados
- Falta validación de integridad de modelos
- Falta tracking de provenance de modelos

**Recomendación:**
1. Extender `leka-llm-evaluation` o `leka-model-wrapper`
2. Implementar checks de integridad de modelos:
   - Validación de checksums/signatures
   - Detección de backdoors
   - Model fingerprinting
3. Implementar tracking de provenance de modelos:
   - Origen del modelo
   - Componentes preentrenados utilizados
   - Verificación de supply chain
4. Endpoint: `/api/model/detect-poisoning`
5. Endpoint: `/api/model/validate-integrity`

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo ML/MLOps  
**Fecha Objetivo:** 18 de noviembre de 2025

---

### INC-008: Falta Detección de Feedback Loop Bias
**GAP Relacionado:** GAP-012  
**Artículo:** Art. 15.4  
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Bloqueador de certificación

**Descripción:**
Falta detección específica de sesgos en bucles de retroalimentación (feedback loops) para sistemas que siguen aprendiendo después de su comercialización. El Art. 15.4 requiere medidas específicas.

**Evidencia:**
- Existe detección de drift general
- Falta detección específica de feedback loop bias
- Falta detección de amplificación de sesgos en retraining
- Falta tracking temporal de sesgos

**Recomendación:**
1. Extender `leka-bias-detection-service`
2. Implementar análisis de feedback loops:
   - Detectar amplificación de sesgos en ciclos de retraining
   - Tracking temporal de sesgos
   - Análisis de correlación entre outputs y inputs futuros
3. Implementar recomendaciones de mitigación
4. Endpoint: `/api/bias/analyze-feedback-loop-bias`

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo ML/MLOps  
**Fecha Objetivo:** 18 de noviembre de 2025

---

## INCIDENCIAS MEDIAS (Mejora de Cumplimiento)

### INC-009: Documentación de Sistemas Evaluados como NO Alto Riesgo
**GAP Relacionado:** GAP-001  
**Artículo:** Art. 6.4  
**Prioridad:** 🟡 MEDIA  
**Impacto:** Mejora de cumplimiento

**Descripción:**
Cuando un sistema en Anexo III se evalúa como NO alto riesgo, falta documentación específica de esta evaluación y proceso de registro según Art. 49.2.

**Recomendación:**
1. Crear workflow para documentación de "Not High-Risk" evaluations
2. Crear entidad/table para almacenar estas evaluaciones
3. Integrar con proceso de registro Art. 49.2
4. Añadir a proceso BPMN

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo Backend  
**Fecha Objetivo:** 25 de noviembre de 2025

---

### INC-010: Metodología FRIA Incompleta (Anexo IX)
**GAP Relacionado:** GAP-051  
**Artículo:** Art. 27 + Anexo IX  
**Prioridad:** 🟡 MEDIA  
**Impacto:** Mejora de cumplimiento

**Descripción:**
Aunque existe entidad `FriaAssessment` y wizard, falta implementación completa de metodología Anexo IX, especialmente checklist sistemático de derechos Charter UE.

**Recomendación:**
1. Completar checklist de derechos Charter UE per Anexo IX
2. Implementar evaluación específica por categoría de derechos
3. Crear templates para cada categoría
4. Vincular con módulos existentes (privacy, bias, vulnerable groups)

**Esfuerzo Estimado:** 2 días  
**Responsable:** Equipo Backend  
**Fecha Objetivo:** 28 de noviembre de 2025

---

### INC-011: Falta Proceso de Consulta con Stakeholders (FRIA)
**GAP Relacionado:** GAP-053  
**Artículo:** Art. 27 + Anexo IX  
**Prioridad:** 🟡 MEDIA  
**Impacto:** Mejora de cumplimiento

**Descripción:**
Falta workflow de consulta con partes interesadas y mecanismo de recolección de feedback para FRIA según Anexo IX.

**Recomendación:**
1. Crear workflow "Stakeholder Consultation"
2. Añadir formularios de recolección de feedback
3. Crear entidad `StakeholderConsultation`
4. Documentar resultados de consulta
5. Vincular con reporte FRIA

**Esfuerzo Estimado:** Incluido en INC-010  
**Responsable:** Equipo Backend  
**Fecha Objetivo:** 28 de noviembre de 2025

---

### INC-012: Documentación de Medidas de Supervisión Humana Incompleta
**GAP Relacionado:** GAP-056  
**Artículo:** Art. 14  
**Prioridad:** 🟡 MEDIA  
**Impacto:** Mejora de cumplimiento

**Descripción:**
Aunque HITL está implementado en workflows, falta documentación formal de medidas de supervisión humana según Art. 14.1.

**Recomendación:**
1. Documentar todas las medidas HITL per Art. 14.1
2. Crear sección "Human Oversight Documentation"
3. Crear entidad `HumanOversightMeasure` con campos:
   - Tipo de supervisión (approval, review, intervention, override)
   - Capacidades del sistema explicadas a humanos
   - Capacidades de detección de anomalías
   - Mecanismos de intervención
   - Procedimientos de override
4. Vincular con workflows BPMN existentes
5. Añadir a documentación técnica (GAP-045)
6. Crear "Override Decision Log"

**Esfuerzo Estimado:** 2 días  
**Responsable:** Equipo Backend + Documentación  
**Fecha Objetivo:** 30 de noviembre de 2025

---

### INC-013: Falta Documentación de Requisitos de Formación para Supervisores
**GAP Relacionado:** GAP-057  
**Artículo:** Art. 14.4  
**Prioridad:** 🟡 MEDIA  
**Impacto:** Efectividad de supervisión

**Descripción:**
Falta documentación de competencias, formación y autoridad necesarias para personas físicas asignadas a supervisión humana según Art. 14.4.

**Recomendación:**
1. Documentar competencias requeridas para supervisores humanos
2. Crear guía "Oversight Personnel Requirements"
3. Añadir recomendaciones de formación
4. Crear entidad `OversightPersonnel` (opcional para clientes)
5. Proporcionar materiales de formación/documentación para clientes

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo Documentación  
**Fecha Objetivo:** 1 de diciembre de 2025

---

### INC-014: Formato de Log Específico AI Act No Definido
**GAP Relacionado:** GAP-026  
**Artículo:** Art. 19.1  
**Prioridad:** 🟡 MEDIA  
**Impacto:** Mejora de cumplimiento

**Descripción:**
Aunque los logs inmutables funcionan, falta definición de formato específico AI Act con todos los campos mandatorios Art. 19.1.

**Recomendación:**
1. Definir formato de log AI Act compliant con campos mandatorios:
   - Timestamp
   - System ID
   - Model version
   - Input summary (sin PII)
   - Output/decision
   - Confidence score
   - Human oversight flag
   - Errores/warnings
2. Implementar validación de completitud
3. Crear entidad `AIActComplianceLog`
4. Añadir exportación para auditores

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo Backend  
**Fecha Objetivo:** 22 de noviembre de 2025

---

### INC-015: Falta Detección Específica de Data Poisoning
**GAP Relacionado:** GAP-013  
**Artículo:** Art. 15.5  
**Prioridad:** 🟡 MEDIA  
**Impacto:** Mejora de seguridad

**Descripción:**
Aunque existen checks de calidad de datos generales, falta detección específica de envenenamiento intencional de datos de entrenamiento.

**Recomendación:**
1. Extender `leka-bias-detection-service`
2. Implementar algoritmos específicos de detección de poisoning
3. Añadir detección de anomalías para datos maliciosos
4. Implementar tracking de provenance de datos

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo ML/MLOps  
**Fecha Objetivo:** 25 de noviembre de 2025

---

## INCIDENCIAS BAJAS (Mejora Opcional)

### INC-016: Falta Detección de Model Inversion / Membership Inference
**GAP Relacionado:** GAP-014  
**Artículo:** Art. 15.5  
**Prioridad:** 🟢 BAJA  
**Impacto:** Protección de privacidad

**Descripción:**
Falta detección de ataques a la confidencialidad como model inversion y membership inference.

**Recomendación:**
1. Extender `leka-prompt-governance` o `leka-llm-evaluation`
2. Implementar testing de membership inference
3. Implementar detección de model inversion
4. Implementar checks de differential privacy

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo ML/MLOps  
**Fecha Objetivo:** 5 de diciembre de 2025

---

### INC-017: Falta Accesibilidad de Logs para Deployers
**GAP Relacionado:** GAP-027  
**Artículo:** Art. 19.2  
**Prioridad:** 🟢 BAJA  
**Impacto:** Soporte a clientes

**Descripción:**
Falta acceso específico y documentación para responsables de despliegue (deployers) sobre gestión de logs.

**Recomendación:**
1. Añadir acceso basado en roles para "Deployers"
2. Crear funcionalidad de exportación de logs para deployers
3. Añadir documentación para deployers:
   - Cómo acceder a logs
   - Requisitos de retención
   - Interpretación del formato
4. Crear template "Log Handover Report"
5. Vincular con obligaciones Art. 26 deployers

**Esfuerzo Estimado:** 0.5 días  
**Responsable:** Equipo Backend  
**Fecha Objetivo:** 3 de diciembre de 2025

---

### INC-018: Falta Evaluación de Impacto en Grupos Vulnerables
**GAP Relacionado:** GAP-002  
**Artículo:** Art. 9.9  
**Prioridad:** 🟢 BAJA  
**Impacto:** Mejora de cumplimiento

**Descripción:**
Falta evaluación específica de impacto en menores (<18 años) y otros grupos vulnerables según Art. 9.9.

**Recomendación:**
1. Añadir módulo "Vulnerable Groups Impact Assessment" a gestión de riesgos
2. Crear checklist para impacto en menores
3. Crear checklist para grupos vulnerables (ancianos, discapacitados, minorías, etc.)
4. Añadir a workflow de evaluación de riesgos
5. Crear entidad/campos para almacenar esta información

**Esfuerzo Estimado:** 1 día  
**Responsable:** Equipo Backend  
**Fecha Objetivo:** 8 de diciembre de 2025

---

## PLAN DE ACCIÓN CONSOLIDADO

### Fase 1: Críticos (Semanas 1-2) - Prioridad MÁXIMA

| Incidencia | Esfuerzo | Responsable | Fecha |
|------------|----------|-------------|-------|
| INC-002: Instrucciones de Uso | 1 día | Backend | 10 nov |
| INC-003: PMM Formal | 2 días | Backend + BPMN | 12 nov |
| INC-004: Notificación Incidentes | 2 días | Backend + BPMN | 12 nov |
| INC-001: Documentación Anexo IV | 3 días | Backend + Doc | 15 nov |
| INC-007: Model Poisoning | 1 día | ML/MLOps | 18 nov |
| INC-008: Feedback Loop Bias | 1 día | ML/MLOps | 18 nov |
| INC-005: Adversarial Examples | 2 días | ML/MLOps | 20 nov |
| INC-014: Formato Log AI Act | 1 día | Backend | 22 nov |

**Total Fase 1:** 13 días

### Fase 2: Medios (Semanas 3-4)

| Incidencia | Esfuerzo | Responsable | Fecha |
|------------|----------|-------------|-------|
| INC-009: Doc Sistemas NO Alto Riesgo | 1 día | Backend | 25 nov |
| INC-015: Data Poisoning | 1 día | ML/MLOps | 25 nov |
| INC-010: FRIA Completo | 2 días | Backend | 28 nov |
| INC-012: Doc Supervisión Humana | 2 días | Backend + Doc | 30 nov |
| INC-013: Formación Supervisores | 1 día | Documentación | 1 dic |

**Total Fase 2:** 7 días

### Fase 3: Bajas (Semana 5)

| Incidencia | Esfuerzo | Responsable | Fecha |
|------------|----------|-------------|-------|
| INC-017: Logs para Deployers | 0.5 días | Backend | 3 dic |
| INC-016: Model Inversion | 1 día | ML/MLOps | 5 dic |
| INC-018: Grupos Vulnerables | 1 día | Backend | 8 dic |

**Total Fase 3:** 2.5 días

---

## MÉTRICAS DE SEGUIMIENTO

### KPIs de Implementación

- **Incidencias resueltas:** X/18
- **Cumplimiento actual:** 87%
- **Cumplimiento proyectado:** 98%
- **Tiempo promedio resolución:** [Tracking]
- **Incidencias críticas pendientes:** [Tracking]

### Revisión Periódica

- **Frecuencia:** Semanal
- **Responsable:** Compliance Officer
- **Reporte:** Dashboard de seguimiento

---

**Fin del Documento de Incidencias y Recomendaciones**

**Próxima Revisión:** 9 de noviembre de 2025  
**Responsable:** Equipo de Compliance

