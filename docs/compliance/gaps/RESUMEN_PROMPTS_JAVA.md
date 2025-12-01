# RESUMEN PROMPTS JAVA - GUÍA COMPLETA PARA DESARROLLO

**Fecha:** Diciembre 2025
**Total Prompts Java:** 82 prompts
**Propósito:** Guía completa para equipo de desarrollo y agentes de IA

---

## GLOSARIO DE COMPONENTES JAVA

- **ViewModel:** Componentes ZKoss para UI (pantallas .zul)
- **BusinessService:** Servicios de negocio (lógica de aplicación)
- **Entity:** Entidades JPA (modelos de datos)
- **Controller:** Controladores REST (APIs)
- **Scheduler:** Jobs programados (tareas automáticas)
- **DBA:** Scripts SQL y optimizaciones de base de datos

---

## PARTE 1: PROMPTS PRINCIPALES (INC-001 a INC-024)

### 🔴 ViewModels (Frontend/UI)

#### `HighRiskClassifierViewModel.java`
**Prompts:**
1. ✅ **INC-001:** Validación Coherencia Modelo-Dataset
   - **Descripción:** Validar que modelo alto riesgo tenga dataset documentado antes de clasificar
   - **Prioridad:** 🔴 CRÍTICA
   - **Esfuerzo:** 0.5 días
   - **📄 Documento Auditoría:** `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-001)

2. ✅ **INC-002:** Validación Confianza Sugerencia IA
   - **Descripción:** Aumentar umbral a 0.85 y requerir confirmación explícita
   - **Prioridad:** 🟡 MEDIA
   - **Esfuerzo:** 0.5 días
   - **📄 Documento Auditoría:** `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-002)

3. ✅ **INC-003:** Validación Documentación Técnica Completa
   - **Descripción:** Validar MODTECHNICALDOCCOMPLETE y MODTECHNICALDOCSCORE >= 0.90
   - **Prioridad:** 🔴 CRÍTICA
   - **Esfuerzo:** 1 día
   - **📄 Documento Auditoría:** `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-003)

4. ✅ **INC-004:** Validación Calidad Justificación
   - **Descripción:** Validar que justificación mencione categoría y explique riesgo
   - **Prioridad:** 🟡 MEDIA
   - **Esfuerzo:** 1 día
   - **📄 Documento Auditoría:** `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-004)

5. ✅ **INC-005:** Validación Sistemas Prohibidos Art. 5
   - **Descripción:** Checklist automático de sistemas prohibidos según Anexo II
   - **Prioridad:** 🔴 CRÍTICA
   - **Esfuerzo:** 1 día
   - **📄 Documento Auditoría:** `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-005)

#### `FriaWizardViewModel.java`
**Prompts:**
6. ✅ **INC-007:** Validación Cruzada FRIA vs Métricas Técnicas
   - **Descripción:** Integrar llamada a microservicio Python para validación cruzada
   - **Prioridad:** 🔴 CRÍTICA
   - **Esfuerzo:** 1 día (integración)
   - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-007)

7. ✅ **INC-009:** Persistencia Estado Árbol Riesgo
   - **Descripción:** Auto-guardar estado del árbol de riesgo cada 30 segundos
   - **Prioridad:** 🟢 BAJA
   - **Esfuerzo:** 0.5 días
   - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-009)

8. ✅ **INC-016:** Exportación Árbol Riesgo
   - **Descripción:** Exportar árbol en PDF, PNG, JSON para auditoría
   - **Prioridad:** 🟢 BAJA
   - **Esfuerzo:** 1 día
   - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-016)

9. ✅ **INC-021:** Versionado FRIA
   - **Descripción:** Implementar versionado de FRIA con historial de cambios
   - **Prioridad:** 🟡 MEDIA
   - **Esfuerzo:** 2 días
   - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-021)

10. ✅ **INC-023:** Validación Calidad FRIA
    - **Descripción:** Validar calidad de texto y especificidad de riesgos/medidas
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-023)

---

### 🔴 BusinessServices (Backend/Lógica de Negocio)

#### `ProjectBusinessService.java`
**Prompts:**
11. ✅ **INC-001:** Validación Modelo-Dataset (también en ViewModel)
    - **📄 Documento Auditoría:** `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-001)

#### `FriaAssessmentBusinessService.java`
**Prompts:**
12. ✅ **INC-008:** Validación Fórmula Cálculo Riesgo
    - **Descripción:** Documentar y testear fórmula según Anexo IX
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-008)

13. ✅ **INC-013:** Validación Medidas Mitigación Implementadas
    - **Descripción:** Verificar que medidas declaradas estén realmente implementadas
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-013)

#### `ImmutableLoggingBusinessService.java`
**Prompts:**
14. ✅ **INC-006:** Verificación Automática Integridad Logs
    - **Descripción:** Job programado diario para verificar integridad de todos los logs
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md` (INC-006)

15. ✅ **INC-012:** Alertas Automáticas Tampering
    - **Descripción:** Generar alerta CRITICAL y notificar seguridad al detectar tampering
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 1 día
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-012)

16. ✅ **INC-018:** Búsqueda Avanzada Logs
    - **Descripción:** Filtros múltiples y búsqueda por texto en IMLDATA
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-018)

#### `ModelValidationService.java` (Nuevo)
**Prompts:**
17. ✅ **INC-011:** Checklist Completo Validación Modelos
    - **Descripción:** Validación completa por tipo de modelo (OpenAI, open source, interno)
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_MODELOS_EXTERNOS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-011)

18. ✅ **INC-015:** Verificación Términos OpenAI
    - **Descripción:** Validar compliance con términos de uso de OpenAI
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_MODELOS_EXTERNOS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-015)

#### `MetricThresholdService.java` (Nuevo)
**Prompts:**
19. ✅ **INC-010:** Umbrales Configurables Métricas
    - **Descripción:** Mover umbrales a tabla de configuración por sector/tipo
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_DATASETS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-010)

#### `EvaluationHistoryService.java` (Nuevo)
**Prompts:**
20. ✅ **INC-014:** Retención Histórica Evaluaciones
    - **Descripción:** Almacenar historial completo de todas las evaluaciones
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-014)

#### `AuthorityNotificationService.java` (Nuevo)
**Prompts:**
21. ✅ **INC-020:** Integración APIs Autoridades
    - **Descripción:** Preparar integración con API oficial de autoridades (Art. 27.3, 49)
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días + TBD
    - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-020)

#### `NotificationSchedulerService.java` (Nuevo)
**Prompts:**
22. ✅ **INC-019:** Notificaciones Vencimientos
    - **Descripción:** Job programado para notificar vencimientos de FRIA
    - **Prioridad:** 🟢 BAJA
    - **Esfuerzo:** 1 día
    - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-019)

#### `ComplianceDashboardService.java` (Nuevo)
**Prompts:**
23. ✅ **INC-017:** Dashboard Consolidado Compliance
    - **Descripción:** Crear dashboard único con KPIs de compliance
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 3 días
    - **📄 Documento Auditoría:** Múltiples
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-017)

#### `EvaluationCacheService.java` (Nuevo)
**Prompts:**
24. ✅ **INC-022:** Caché Evaluaciones Técnicas
    - **Descripción:** Implementar caché para mejorar performance de evaluaciones
    - **Prioridad:** 🟢 BAJA
    - **Esfuerzo:** 1 día
    - **📄 Documento Auditoría:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-022)

#### `ComplianceExecutiveReportService.java` (Nuevo)
**Prompts:**
25. ✅ **INC-024:** Reporte Ejecutivo Consolidado
    - **Descripción:** Generar reporte ejecutivo PDF con resumen de compliance
    - **Prioridad:** 🟢 BAJA
    - **Esfuerzo:** 3 días
    - **📄 Documento Auditoría:** Múltiples
    - **📋 Documento Incidencias:** `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-024)

---

## PARTE 2: PROMPTS EVALUACIÓN DATASETS

### 🔴 Visualizaciones (Frontend)
**Prompts:**
26. ✅ **INC-003-DS:** Visualizaciones Gráficas Bias
    - **Descripción:** Añadir histogramas, box plots para visualizar sesgos
    - **Prioridad:** 🟠 HIGH
    - **Esfuerzo:** 3-4 días
    - **Componente:** Frontend (ZUL + Chart.js/D3.js)
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_DATASETS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md` (INC-003-DS)

### 🔴 Validación Integridad (Backend + Python)
**Prompts:**
27. ✅ **INC-005-DS:** Validación Integridad Datasets
    - **Descripción:** Validar checksums SHA-256 de datasets subidos
    - **Prioridad:** 🟠 HIGH
    - **Esfuerzo:** 1-2 días
    - **Componente:** Java Backend + Python Microservicio
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_DATASETS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md` (INC-005-DS)

### 🔴 Benchmarks (Backend)
**Prompts:**
28. ✅ **INC-007-DS:** Benchmarks Industria
    - **Descripción:** Tabla de benchmarks por sector para comparación
    - **Prioridad:** 🟡 MEDIUM
    - **Esfuerzo:** 4-5 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_DATASETS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md` (INC-007-DS)

### 🔴 Exportación (Backend + Frontend)
**Prompts:**
29. ✅ **INC-008-DS:** Exportación Reportes
    - **Descripción:** Exportar reportes en PDF, Excel, JSON
    - **Prioridad:** 🟡 MEDIUM
    - **Esfuerzo:** 3-4 días
    - **Componente:** Java Backend + Frontend
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_DATASETS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md` (INC-008-DS)

### 🔴 Notificaciones (Backend)
**Prompts:**
30. ✅ **INC-009-DS:** Notificaciones Automáticas
    - **Descripción:** Email, Slack para alertas y notificaciones
    - **Prioridad:** 🟡 MEDIUM
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_DATASETS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md` (INC-009-DS)

### 🔴 Historial Versiones (Backend)
**Prompts:**
31. ✅ **INC-010-DS:** Historial Versiones Evaluaciones
    - **Descripción:** Versionado de evaluaciones con historial
    - **Prioridad:** 🟢 LOW
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_EVALUACION_DATASETS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md` (INC-010-DS)

---

## PARTE 3: PROMPTS LOGS INMUTABLES (INC-008-001 a INC-008-006)

### 🔴 Trazabilidad y Exportación
**Prompts:**
32. ✅ **INC-008-001:** Trazabilidad Completa Modelo-Dataset-Output
    - **Descripción:** Crear vista SQL unificada que consolide traza completa
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Backend + SQL
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md` (INC-008-001)

33. ✅ **INC-008-002:** Exportación Auditores Externos
    - **Descripción:** Exportación segura y verificable para auditores externos
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend + Security
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md` (INC-008-002)

### 🟡 Documentación y Alertas
**Prompts:**
34. ✅ **INC-008-003:** Documentación Cumplimiento Art. 19
    - **Descripción:** Documentar formalmente cumplimiento Art. 19
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Documentación
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md` (INC-008-003)

35. ✅ **INC-008-004:** Alerta Automática Manipulación
    - **Descripción:** Alerta automática cuando se detecta manipulación
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend + Scheduler
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md` (INC-008-004)

36. ✅ **INC-008-005:** Inconsistencia Algoritmo Hash
    - **Descripción:** Unificar algoritmo de hash entre Java y PostgreSQL
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 0.5 días
    - **Componente:** Java Backend + SQL
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md` (INC-008-005)

### 🟢 Opcional
**Prompts:**
37. ✅ **INC-008-006:** Soporte Timestamp Externo RFC 3161
    - **Descripción:** Integración con servicio de timestamp externo (opcional)
    - **Prioridad:** 🟢 BAJA
    - **Esfuerzo:** 2 días (opcional)
    - **Componente:** Java Backend + Integración Externa
    - **📄 Documento Auditoría:** `AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md` (INC-008-006)

---

## PARTE 4: PROMPTS MONITORIZACIÓN TIEMPO REAL (INC-009-002 a INC-009-006)

### 🟡 Throttling y Backpressure
**Prompts:**
38. ✅ **INC-009-002:** Throttling y Backpressure
    - **Descripción:** Implementar throttling y backpressure para telemetría
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md` (INC-009-002)

### 🟡 Análisis Asíncrono
**Prompts:**
39. ✅ **INC-009-003:** Análisis Asíncrono
    - **Descripción:** Procesar análisis de telemetría de forma asíncrona
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md` (INC-009-003)

### 🟡 Métricas de Incidentes
**Prompts:**
40. ✅ **INC-009-004:** Métricas de Incidentes
    - **Descripción:** Nueva entidad para métricas de incidentes
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend + Entity
    - **📄 Documento Auditoría:** `AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md` (INC-009-004)

### 🟢 Health Indicator
**Prompts:**
41. ✅ **INC-009-006:** Health Indicator
    - **Descripción:** HealthIndicator Spring para monitoreo de salud
    - **Prioridad:** 🟢 BAJA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend (Spring)
    - **📄 Documento Auditoría:** `AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md` (INC-009-006)

---

## PARTE 5: PROMPTS POST MARKET MONITORING (INC-010-001 a INC-010-015)

### 🔴 Críticas (6)
**Prompts:**
42. ✅ **INC-010-001:** Documentación Formal Sistema PMM
    - **Descripción:** Crear entidad PostMarketMonitoringPlan y documentación
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Backend + Entity + Documentación
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-001)

43. ✅ **INC-010-002:** Generación Automática Post-Market Surveillance Report
    - **Descripción:** Generar informes automáticos de vigilancia poscomercialización
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-002)

44. ✅ **INC-010-003:** Workflow Notificación Incidentes Graves
    - **Descripción:** Workflow completo para notificación de incidentes graves
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Backend + BPMN
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-003)

45. ✅ **INC-010-004:** Implementación Real PostMarketMonitoringService
    - **Descripción:** Implementar servicio completo de PMM
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 5 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-004)

46. ✅ **INC-010-005:** Vinculación PMM con Registro Art. 49
    - **Descripción:** Vincular PMM con registro UE según Art. 49
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-005)

47. ✅ **INC-010-006:** Configuración Thresholds Alertas
    - **Descripción:** Configurar umbrales de alertas para PMM
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-006)

### 🟡 Altas (5)
**Prompts:**
48. ✅ **INC-010-007:** Implementación Informes Automáticos
    - **Descripción:** Timers BPMN para generación automática de informes
    - **Prioridad:** 🟡 ALTA
    - **Esfuerzo:** 2 días
    - **Componente:** BPMN + Java
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-007)

49. ✅ **INC-010-008:** Dashboard Supervisión Continua
    - **Descripción:** Dashboard para supervisión continua de PMM
    - **Prioridad:** 🟡 ALTA
    - **Esfuerzo:** 3 días
    - **Componente:** Java + Frontend
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-008)

50. ✅ **INC-010-009:** API REST Consulta Histórico
    - **Descripción:** API REST para consulta de histórico de PMM
    - **Prioridad:** 🟡 ALTA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend (REST Controller)
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-009)

51. ✅ **INC-010-010:** Integración Sistema Feedback
    - **Descripción:** Integración con sistema de feedback de usuarios
    - **Prioridad:** 🟡 ALTA
    - **Esfuerzo:** 3 días
    - **Componente:** Java + Python
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-010)

52. ✅ **INC-010-011:** Optimización Consultas Vistas Materializadas
    - **Descripción:** Crear vistas materializadas para optimizar consultas PMM
    - **Prioridad:** 🟡 ALTA
    - **Esfuerzo:** 2 días
    - **Componente:** DBA (SQL)
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-011)

### 🟢 Medias (4)
**Prompts:**
53. ✅ **INC-010-012:** Análisis Sentimiento Feedback
    - **Descripción:** Análisis de sentimiento en feedback (Python)
    - **Prioridad:** 🟢 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Python Microservicio
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-012)

54. ✅ **INC-010-013:** Visualización Tendencias Avanzadas
    - **Descripción:** Visualización de tendencias avanzadas en dashboard
    - **Prioridad:** 🟢 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java + Frontend
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-013)

55. ✅ **INC-010-014:** Configuración Frecuencias por Proyecto
    - **Descripción:** Configurar frecuencias de monitoreo por proyecto
    - **Prioridad:** 🟢 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-014)

56. ✅ **INC-010-015:** Integración Sistemas Externos
    - **Descripción:** Integración con sistemas externos para PMM
    - **Prioridad:** 🟢 MEDIA
    - **Esfuerzo:** 3 días
    - **Componente:** Java + Python
    - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-015)

---

## PARTE 6: PROMPTS VERSIONADO Y CONTROL ACCESO (INC-011-01 a INC-011-04)

### 🔴 Control Acceso Roles
**Prompts:**
57. ✅ **INC-011-01:** Control Acceso Basado en Roles
    - **Descripción:** Sistema RBAC para prevenir cambios por partners
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 4.5-7.5 días
    - **Componente:** Java Backend + DBA
    - **📄 Documento Auditoría:** `AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md` (INC-011-01)
    - **Estado:** ✅ **YA IMPLEMENTADO DE FACTO EN LA PLATAFORMA**

### 🔴 Versionado Explícito
**Prompts:**
58. ✅ **INC-011-02:** Versionado Explícito Datasets
    - **Descripción:** Versionado explícito de datasets con semver
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Backend + Entity
    - **📄 Documento Auditoría:** `AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md` (INC-011-02)

59. ✅ **INC-011-03:** Versionado Explícito Evaluaciones
    - **Descripción:** Versionado explícito de evaluaciones
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend + Entity
    - **📄 Documento Auditoría:** `AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md` (INC-011-03)

60. ✅ **INC-011-04:** Validación SemVer
    - **Descripción:** Validar formato semver en versiones
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend + DBA
    - **📄 Documento Auditoría:** `AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md` (INC-011-04)

---

## PARTE 7: PROMPTS CONTROLES PRE-DESPLIEGUE (INC-012-001 a INC-012-008)

### 🔴 Pantalla Consolidada y Validaciones
**Prompts:**
61. ✅ **INC-012-001:** Pantalla Consolidada Controles Pre-Despliegue
    - **Descripción:** Pantalla única que muestre checklist completo de controles
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 5 días
    - **Componente:** Java + Frontend (ZUL)
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-001)

62. ✅ **INC-012-002:** Condiciones Aprobación Documentadas en UI
    - **Descripción:** Mostrar condiciones de aprobación explícitamente en UI
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **Componente:** Java + Frontend (ZUL)
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-002)

63. ✅ **INC-012-003:** Validación FRIA Alto Riesgo
    - **Descripción:** Validar que FRIA esté completo antes de despliegue alto riesgo
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-003)

### 🟡 Exportación y Tracking
**Prompts:**
64. ✅ **INC-012-004:** Exportación Historial Aprobaciones
    - **Descripción:** Exportar historial completo de aprobaciones
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend + Frontend
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-004)

65. ✅ **INC-012-005:** SLA Tracking Dashboard
    - **Descripción:** Dashboard para tracking de SLA de aprobaciones
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 3 días
    - **Componente:** Java + Frontend
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-005)

66. ✅ **INC-012-006:** Validación Post Market Monitoring Plan
    - **Descripción:** Validar que PMM plan esté configurado antes de despliegue
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-006)

67. ✅ **INC-012-007:** Justificación Rollback ImmutableLog
    - **Descripción:** Registrar justificación de rollback en log inmutable
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-007)

68. ✅ **INC-012-008:** Notificación Email Propietario
    - **Descripción:** Notificar por email al propietario del proyecto
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_012_PUESTA_PRODUCCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_012_PUESTA_PRODUCCION.md` (INC-012-008)

---

## PARTE 8: PROMPTS SUPERVISIÓN HUMANA HITL (INC-HITL-001 a INC-HITL-010)

### 🔴 Críticas (3)
**Prompts:**
69. ✅ **INC-HITL-001:** Protección Multicapa Controles Críticos
    - **Descripción:** Protección multicapa (BD, aplicación, auditoría) contra desactivación
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 5-7 días
    - **Componente:** Java Backend + DBA
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-001)

70. ✅ **INC-HITL-002:** Validación Obligatoria HITL Alto Riesgo
    - **Descripción:** Validar obligatoriamente HITL para sistemas de alto riesgo
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Backend
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-002)

71. ✅ **INC-HITL-003:** Registro Intentos Modificación Controles Críticos
    - **Descripción:** Registrar todos los intentos de modificación de controles críticos
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend + DBA
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-003)

### 🟠 Altas (3)
**Prompts:**
72. ✅ **INC-HITL-004:** Exportación Automática Evidencia Auditorías
    - **Descripción:** Exportar automáticamente evidencia para auditorías externas
    - **Prioridad:** 🟠 ALTA
    - **Esfuerzo:** 2 días
    - **Componente:** Java + Frontend
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-004)

73. ✅ **INC-HITL-005:** Firma Digital Aprobaciones
    - **Descripción:** Implementar firma digital en aprobaciones
    - **Prioridad:** 🟠 ALTA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Backend
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-005)

74. ✅ **INC-HITL-006:** Configuración HITL por Partner/Tenant
    - **Descripción:** Configurar HITL por partner o tenant
    - **Prioridad:** 🟠 ALTA
    - **Esfuerzo:** 3 días
    - **Componente:** Java + Frontend
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-006)

### 🟡 Medias (4)
**Prompts:**
75. ✅ **INC-HITL-007:** Validación Esquema JSONB
    - **Descripción:** Validar esquema JSONB de configuraciones HITL
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-007)

76. ✅ **INC-HITL-008:** Campo IP/Origen Solicitudes Aprobación
    - **Descripción:** Añadir campo de IP/origen en solicitudes de aprobación
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-008)

77. ✅ **INC-HITL-009:** Dashboard Métricas HITL
    - **Descripción:** Dashboard con métricas de supervisión humana
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 2 días
    - **Componente:** Java + Frontend
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-009)

78. ✅ **INC-HITL-010:** Documentación Criterios Supervisión
    - **Descripción:** Documentar criterios de supervisión humana
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Documentación
    - **Estado:** ✅ IMPLEMENTADO
    - **📄 Documento Auditoría:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md` (INC-HITL-010)

---

## PARTE 9: PROMPTS INTEGRACIÓN SIN SUSTITUCIÓN (INC-INT-001 a INC-INT-008)

### 🔴 Críticas (2)
**Prompts:**
79. ✅ **INC-INT-001:** Conector Microsoft Copilot
    - **Descripción:** Crear conector para Microsoft Copilot con Microsoft Graph API
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 3-5 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-001)

80. ✅ **INC-INT-002:** Cifrado Tokens y Credenciales
    - **Descripción:** Cifrar tokens y credenciales de integraciones externas
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend + Security
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-002)

### 🟠 Altas (3)
**Prompts:**
81. ✅ **INC-INT-003:** Validación Integridad Metadata
    - **Descripción:** Validar integridad de metadata sincronizada desde sistemas externos
    - **Prioridad:** 🟠 ALTA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-003)

82. ✅ **INC-INT-004:** Rate Limiting Webhooks
    - **Descripción:** Implementar rate limiting para webhooks de integraciones
    - **Prioridad:** 🟠 ALTA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-004)

83. ✅ **INC-INT-005:** Monitoreo Latencia Sync
    - **Descripción:** Monitorear latencia de sincronización con sistemas externos
    - **Prioridad:** 🟠 ALTA
    - **Esfuerzo:** 2 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-005)

### 🟡 Medias (3)
**Prompts:**
84. ✅ **INC-INT-006:** Retry Backoff Exponencial
    - **Descripción:** Implementar retry con backoff exponencial para integraciones
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-006)

85. ✅ **INC-INT-007:** Documentación Límites APIs
    - **Descripción:** Documentar límites de APIs de sistemas externos
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 1 día
    - **Componente:** Documentación
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-007)

86. ✅ **INC-INT-008:** Testing E2E Integraciones
    - **Descripción:** Tests end-to-end para integraciones con sistemas externos
    - **Prioridad:** 🟡 MEDIA
    - **Esfuerzo:** 3 días
    - **Componente:** Java Testing
    - **📄 Documento Auditoría:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md` (INC-INT-008)

---

## PARTE 10: PROMPTS ADICIONALES

### 🔴 Validación Proactiva Políticas
**Prompts:**
87. ✅ **INC-005-003:** Validación Proactiva Políticas Cliente
    - **Descripción:** Validación proactiva de políticas antes de generación
    - **Prioridad:** 🔴 CRÍTICA
    - **Esfuerzo:** 2-3 días
    - **Componente:** Java Backend
    - **📄 Documento Auditoría:** `AUDITORIA_005_EVALUACION_RAG.md`
    - **📋 Documento Incidencias:** `INCIDENCIAS_005_EVALUACION_RAG.md` (INC-005-003)

---

## RESUMEN TOTAL POR PRIORIDAD

### 🔴 CRÍTICAS (25)
- INC-001, INC-003, INC-005, INC-007, INC-011, INC-012, INC-013, INC-020
- INC-008-001, INC-008-002
- INC-010-001 a INC-010-006
- INC-011-01 a INC-011-04
- INC-012-001 a INC-012-003
- INC-HITL-001 a INC-HITL-003
- INC-INT-001, INC-INT-002
- INC-005-003

### 🟠 ALTAS (8)
- INC-003-DS, INC-005-DS
- INC-010-007 a INC-010-011
- INC-HITL-004 a INC-HITL-006
- INC-INT-003 a INC-INT-005

### 🟡 MEDIAS (35)
- INC-002, INC-004, INC-006, INC-008, INC-010, INC-014, INC-015, INC-017, INC-018, INC-021, INC-023
- INC-007-DS, INC-008-DS, INC-009-DS
- INC-008-003 a INC-008-005
- INC-009-002 a INC-009-004
- INC-010-012 a INC-010-015
- INC-012-004 a INC-012-008
- INC-HITL-007 a INC-HITL-010
- INC-INT-006 a INC-INT-008

### 🟢 BAJAS (14)
- INC-009, INC-016, INC-019, INC-022, INC-024
- INC-008-006
- INC-009-006
- INC-010-DS

---

## REFERENCIAS A DOCUMENTOS DE AUDITORÍA

### Documentos de Auditoría Principales:
- **`AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`** - Catalogación y clasificación (INC-001 a INC-005)
- **`AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`** - FRIA y evaluaciones (INC-007, INC-008, INC-009, INC-013, INC-014, INC-016, INC-019, INC-020, INC-021, INC-022, INC-023)
- **`AUDITORIA_EVALUACION_DATASETS.md`** - Evaluación de datasets (INC-010, INC-003-DS, INC-005-DS, INC-007-DS, INC-008-DS, INC-009-DS, INC-010-DS)
- **`AUDITORIA_EVALUACION_MODELOS_EXTERNOS.md`** - Evaluación de modelos (INC-011, INC-015)
- **`AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`** - Logs inmutables (INC-006, INC-012, INC-018, INC-008-001 a INC-008-006)
- **`AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`** - Monitorización tiempo real (INC-009-002, INC-009-003, INC-009-004, INC-009-006)
- **`AUDITORIA_010_POST_MARKET_MONITORING.md`** - Post Market Monitoring (INC-010-001 a INC-010-015)
- **`AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`** - Versionado y control cambios (INC-011-01 a INC-011-04)
- **`AUDITORIA_012_PUESTA_PRODUCCION.md`** - Puesta en producción (INC-012-001 a INC-012-008)
- **`AUDITORIA_SUPERVISION_HUMANA_HITL.md`** - Supervisión humana (INC-HITL-001 a INC-HITL-010)
- **`AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`** - Integración sin sustitución (INC-INT-001 a INC-INT-008)
- **`AUDITORIA_005_EVALUACION_RAG.md`** - Evaluación RAG (INC-005-003)

### Documentos de Incidencias y Recomendaciones:
- **`INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`** - Incidencias principales (INC-001 a INC-024)
- **`INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md`** - Incidencias evaluación datasets
- **`INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md`** - Incidencias logs inmutables
- **`INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md`** - Incidencias monitorización
- **`INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md`** - Incidencias PMM
- **`INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md`** - Incidencias versionado
- **`INCIDENCIAS_012_PUESTA_PRODUCCION.md`** - Incidencias puesta producción
- **`INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md`** - Incidencias HITL
- **`INCIDENCIAS_INTEGRACION_SIN_SUSTITUCION.md`** - Incidencias integración
- **`INCIDENCIAS_005_EVALUACION_RAG.md`** - Incidencias RAG

### Ubicación de Documentos:
Todos los documentos están en: `/docs/compliance/auditoria/`

---

## ORDEN DE IMPLEMENTACIÓN RECOMENDADO

### Fase 1: Críticas Bloqueantes (Semana 1-2)
1. **INC-001, INC-003, INC-005** (Catalogación) - Bloquean clasificación
2. **INC-011** (Validación modelos) - Requerido para compliance
3. **INC-012, INC-012-001 a INC-012-003** (Controles pre-despliegue) - Bloquean despliegue
4. **INC-008-001, INC-008-002** (Trazabilidad) - Requerido Art. 19
5. **INC-HITL-001 a INC-HITL-003** (Protección HITL) - Seguridad crítica
6. **INC-010-001 a INC-010-006** (PMM crítico) - Certification blocker
7. **INC-INT-001, INC-INT-002** (Integración crítica) - Seguridad

### Fase 2: Altas (Semana 3-4)
8. **INC-003-DS, INC-005-DS** (Datasets)
9. **INC-010-007 a INC-010-011** (PMM altas)
10. **INC-HITL-004 a INC-HITL-006** (HITL altas)
11. **INC-INT-003 a INC-INT-005** (Integración altas)

### Fase 3: Medias (Semana 5-6)
12. **INC-002, INC-004, INC-006, INC-008, INC-010, INC-014, INC-015, INC-017, INC-018, INC-021, INC-023**
13. **INC-007-DS, INC-008-DS, INC-009-DS**
14. **INC-008-003 a INC-008-005**
15. **INC-009-002 a INC-009-004**
16. **INC-010-012 a INC-010-015**
17. **INC-012-004 a INC-012-008**
18. **INC-HITL-007 a INC-HITL-010**
19. **INC-INT-006 a INC-INT-008**

### Fase 4: Bajas (Semana 7)
20. **INC-009, INC-016, INC-019, INC-022, INC-024**
21. **INC-008-006**
22. **INC-009-006**
23. **INC-010-DS**

---

## NOTAS PARA EQUIPO DE DESARROLLO

1. **Cada prompt incluye:**
   - Contexto completo del problema
   - Ubicación exacta del código actual
   - Requisitos según EU AI Act
   - Implementación detallada con código
   - Pruebas requeridas
   - Referencias a documentos de auditoría

2. **Antes de implementar:**
   - Leer el prompt completo
   - Revisar documento de auditoría relacionado
   - Verificar incidencia en documento de incidencias
   - Consultar con equipo de compliance si hay dudas

3. **Después de implementar:**
   - Actualizar `SEGUIMIENTO_INCIDENCIAS.md`
   - Marcar prompt como completado
   - Documentar cambios en código
   - Ejecutar pruebas

---

**Última actualización:** Diciembre 2025
**Total Prompts Java Documentados:** 87/87 (100%) ✅ COMPLETO
