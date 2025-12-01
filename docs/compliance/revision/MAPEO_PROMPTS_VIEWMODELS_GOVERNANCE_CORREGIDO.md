# MAPEO PROMPTS JAVA - VIEWMODELS GOVERNANCE (CORREGIDO)

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Mapear los 87 prompts de `RESUMEN_PROMPTS_JAVA.md` con los 91 ViewModels de governance de `REVISION_COMPLETA_VIEWMODELS.md`

**IMPORTANTE:** Todos los 91 ViewModels en `REVISION_COMPLETA_VIEWMODELS.md` son considerados ViewModels de Governance.

---

## 📊 RESUMEN EJECUTIVO CORREGIDO

| Categoría | Total | Afectan ViewModels Governance | No Afectan ViewModels | % Afectación |
|-----------|-------|-------------------------------|----------------------|--------------|
| **Prompts Totales** | 87 | ~65-70 | ~17-22 | ~75-80% |
| **ViewModels Governance** | 91 | ~65-70 afectados | ~21-26 sin prompts directos | ~72-77% |
| **BusinessServices** | ~20 core + 13 integraciones = 33 total | Todos afectan indirectamente | 0 | 100% |

---

## 📋 ANÁLISIS DETALLADO POR TIPO DE PROMPT

### PROMPTS QUE AFECTAN DIRECTAMENTE A VIEWMODELS (15 prompts)

#### ViewModels Mencionados Explícitamente:

1. **HighRiskClassifierViewModel** (5 prompts):
   - INC-001, INC-002, INC-003, INC-004, INC-005

2. **FriaWizardViewModel** (5 prompts):
   - INC-007, INC-009, INC-016, INC-021, INC-023

#### BusinessServices que Afectan ViewModels (5 prompts):

3. **FriaAssessmentBusinessService** (2 prompts):
   - INC-008, INC-013
   - **ViewModels Afectados:** FriaWizardViewModel, CompleteMissingElementsViewModel, DefineModificationsViewModel, EnhancedReviewViewModel

4. **ImmutableLoggingBusinessService** (3 prompts):
   - INC-006, INC-012, INC-018
   - **ViewModels Afectados:** TODOS los 91 ViewModels (indirectamente, a través de logging)

---

### PROMPTS QUE AFECTAN INDIRECTAMENTE A VIEWMODELS (50-55 prompts)

#### Prompts con Componente "Frontend" o "Java + Frontend" (15-20 prompts):

Estos prompts afectan a ViewModels porque mencionan Frontend/ZUL:

5. **INC-003-DS:** Visualizaciones Gráficas Bias
   - **Componente:** Frontend (ZUL + Chart.js/D3.js)
   - **ViewModels Afectados:** AnalyticsBiasViewModel, AnalyticsFairnessViewModel, AnalyticsTransparencyViewModel

6. **INC-008-DS:** Exportación Reportes
   - **Componente:** Java Backend + Frontend
   - **ViewModels Afectados:** Todos los ViewModels con funcionalidad de exportación (AnalyticsReportViewModel, ComplianceExecutiveReportService, etc.)

7. **INC-010-008:** Dashboard Supervisión Continua
   - **Componente:** Java + Frontend
   - **ViewModels Afectados:** MonitoringDashboardViewModel, GovernanceDashboardViewModel, ComplianceDashboardService

8. **INC-010-013:** Visualización Tendencias Avanzadas
   - **Componente:** Java + Frontend
   - **ViewModels Afectados:** AnalyticsTrendsViewModel, AnalyticsOverviewViewModel

9. **INC-012-001:** Pantalla Consolidada Controles Pre-Despliegue
   - **Componente:** Java + Frontend (ZUL)
   - **ViewModels Afectados:** ModelApprovalWorkflowViewModel, AgentApprovalWorkflowViewModel, PromptApprovalWorkflowViewModel

10. **INC-012-002:** Condiciones Aprobación Documentadas en UI
    - **Componente:** Java + Frontend (ZUL)
    - **ViewModels Afectados:** ModelApprovalWorkflowViewModel, AgentApprovalWorkflowViewModel, PromptApprovalWorkflowViewModel

11. **INC-012-004:** Exportación Historial Aprobaciones
    - **Componente:** Java Backend + Frontend
    - **ViewModels Afectados:** ModelApprovalWorkflowViewModel, AgentApprovalWorkflowViewModel, PromptApprovalWorkflowViewModel

12. **INC-012-005:** SLA Tracking Dashboard
    - **Componente:** Java + Frontend
    - **ViewModels Afectados:** ModelApprovalWorkflowViewModel, AgentApprovalWorkflowViewModel, PromptApprovalWorkflowViewModel

13. **INC-HITL-004:** Exportación Automática Evidencia Auditorías
    - **Componente:** Java + Frontend
    - **ViewModels Afectados:** Todos los ViewModels con workflows de aprobación

14. **INC-HITL-006:** Configuración HITL por Partner/Tenant
    - **Componente:** Java + Frontend
    - **ViewModels Afectados:** Todos los ViewModels con configuración HITL

15. **INC-HITL-009:** Dashboard Métricas HITL
    - **Componente:** Java + Frontend
    - **ViewModels Afectados:** MonitoringDashboardViewModel, GovernanceDashboardViewModel, **HitlSupervisionViewModel** (pendiente crear)
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN** - Requiere crear dashboard consolidado de supervisión humana
    - **Relación con Demo:** Pantalla 7 - Supervisión Humana (HITL/HOTL)

#### Prompts con Componente "Java Backend" que Afectan ViewModels (20-25 prompts):

Estos prompts afectan a ViewModels a través de BusinessServices o funcionalidades que los ViewModels usan:

16. **INC-005-DS:** Validación Integridad Datasets
    - **Componente:** Java Backend + Python Microservicio
    - **ViewModels Afectados:** ViewModels que manejan datasets (TrainingDashboardViewModel, ExperimentsDetailViewModel)

17. **INC-007-DS:** Benchmarks Industria
    - **Componente:** Java Backend
    - **ViewModels Afectados:** AnalyticsOverviewViewModel, AnalyticsMetricViewModel, SectorDashboardViewModel

18. **INC-009-DS:** Notificaciones Automáticas
    - **Componente:** Java Backend
    - **ViewModels Afectados:** Todos los ViewModels que muestran notificaciones

19. **INC-010-DS:** Historial Versiones Evaluaciones
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ViewModels con historial (ModelsDetailViewModel, RagSystemsDetailViewModel)

20. **INC-008-001:** Trazabilidad Completa Modelo-Dataset-Output
    - **Componente:** Java Backend + SQL
    - **ViewModels Afectados:** Todos los ViewModels que muestran trazabilidad (ModelLineageTreeViewModel, ProjectsDashboardViewModel), **TraceabilityEvidenceViewModel** (pendiente crear)
    - **Estado:** ⚠️ **PENDIENTE** - Requiere crear vista consolidada de trazabilidad y evidencias
    - **Relación con Demo:** Pantalla 8 - Trazabilidad y Evidencias
    - **Integración:** Requiere integración con `ImmutableLoggingBusinessService` y telemetría

21. **INC-008-002:** Exportación Auditores Externos
    - **Componente:** Java Backend + Security
    - **ViewModels Afectados:** Todos los ViewModels con exportación

22. **INC-009-002:** Throttling y Backpressure
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ViewModels con telemetría (MonitoringDashboardViewModel, AgentsDashboardViewModel)

23. **INC-009-003:** Análisis Asíncrono
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ViewModels con análisis (AnalyticsOverviewViewModel, MonitoringDashboardViewModel)

24. **INC-009-004:** Métricas de Incidentes
    - **Componente:** Java Backend + Entity
    - **ViewModels Afectados:** MonitoringDashboardViewModel, Incident ViewModels

25. **INC-010-001:** Documentación Formal Sistema PMM
    - **Componente:** Java Backend + Entity + Documentación
    - **ViewModels Afectados:** ViewModels relacionados con PMM

26. **INC-010-002:** Generación Automática Post-Market Surveillance Report
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ViewModels relacionados con PMM

27. **INC-010-003:** Workflow Notificación Incidentes Graves
    - **Componente:** Java Backend + BPMN
    - **ViewModels Afectados:** DocumentIncidentDetailsViewModel, RootCauseAnalysisViewModel, DefineCorrectiveActionsViewModel, VerifyIncidentResolutionViewModel

28. **INC-010-004:** Implementación Real PostMarketMonitoringService
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ViewModels relacionados con PMM
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.compliance.PostMarketMonitoringService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringService.java`
    - **Métodos Principales:**
      - `createMonitoringPlan(Long projectId, String monitoringFrequency)` - Crea plan de PMM
      - `executeMonitoring(Long pmmId)` - Ejecuta monitoreo post-mercado
      - `getActiveMonitoringPlans()` - Obtiene planes de PMM activos
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private PostMarketMonitoringService postMarketMonitoringService;

      public void createPMMPlan() {
          PostMarketMonitoring pmm = postMarketMonitoringService.createMonitoringPlan(projectId, "MONTHLY");
          // Usar plan creado
      }
      ```

29. **INC-010-005:** Vinculación PMM con Registro Art. 49
    - **Componente:** Java Backend
    - **ViewModels Afectados:** EURegistrationStatusViewModel, EuRegistrationFormViewModel, ReviewRegistrationPackageViewModel

30. **INC-010-006:** Configuración Thresholds Alertas
    - **Componente:** Java Backend
    - **ViewModels Afectados:** MonitoringDashboardViewModel, AnalyticsMetricViewModel

31. **INC-010-007:** Implementación Informes Automáticos
    - **Componente:** BPMN + Java
    - **ViewModels Afectados:** ViewModels con workflows

32. **INC-010-009:** API REST Consulta Histórico
    - **Componente:** Java Backend (REST Controller)
    - **ViewModels Afectados:** ViewModels que consultan histórico (indirectamente)

33. **INC-010-010:** Integración Sistema Feedback
    - **Componente:** Java + Python
    - **ViewModels Afectados:** ViewModels con feedback

34. **INC-010-011:** Optimización Consultas Vistas Materializadas
    - **Componente:** DBA (SQL)
    - **ViewModels Afectados:** Todos los ViewModels (mejora performance)

35. **INC-010-012:** Análisis Sentimiento Feedback
    - **Componente:** Python Microservicio
    - **ViewModels Afectados:** ViewModels con feedback

36. **INC-010-014:** Configuración Frecuencias por Proyecto
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ProjectsDashboardViewModel, ProjectAIInventoryViewModel

37. **INC-010-015:** Integración Sistemas Externos
    - **Componente:** Java + Python
    - **ViewModels Afectados:** ExternalPlatformsViewModel, ProvidersOverviewViewModel, ProvidersDetailViewModel

38. **INC-011-01:** Control Acceso Basado en Roles
    - **Componente:** Java Backend + DBA
    - **ViewModels Afectados:** TODOS los 91 ViewModels (control de acceso)
    - **Estado:** ✅ **YA IMPLEMENTADO DE FACTO EN LA PLATAFORMA**
    - **Nota:** Sistema RBAC completo con roles, permisos y workflow de aprobación implementado

39. **INC-011-02:** Versionado Explícito Datasets
    - **Componente:** Java Backend + Entity
    - **ViewModels Afectados:** TrainingDashboardViewModel, ExperimentsDetailViewModel

40. **INC-011-03:** Versionado Explícito Evaluaciones
    - **Componente:** Java Backend + Entity
    - **ViewModels Afectados:** ViewModels con evaluaciones (ModelsDetailViewModel, RagSystemsDetailViewModel)

41. **INC-011-04:** Validación SemVer
    - **Componente:** Java Backend + DBA
    - **ViewModels Afectados:** ViewModels con versionado

42. **INC-012-003:** Validación FRIA Alto Riesgo
    - **Componente:** Java Backend
    - **ViewModels Afectados:** FriaWizardViewModel, CompleteMissingElementsViewModel, DefineModificationsViewModel, EnhancedReviewViewModel

43. **INC-012-006:** Validación Post Market Monitoring Plan
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ViewModels relacionados con PMM

44. **INC-012-007:** Justificación Rollback ImmutableLog
    - **Componente:** Java Backend
    - **ViewModels Afectados:** Todos los ViewModels con rollback

45. **INC-012-008:** Notificación Email Propietario
    - **Componente:** Java Backend
    - **ViewModels Afectados:** Todos los ViewModels con notificaciones

46. **INC-HITL-001:** Protección Multicapa Controles Críticos
    - **Componente:** Java Backend + DBA
    - **ViewModels Afectados:** Todos los ViewModels con controles críticos
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN**

47. **INC-HITL-002:** Validación Obligatoria HITL Alto Riesgo
    - **Componente:** Java Backend
    - **ViewModels Afectados:** Todos los ViewModels con workflows de aprobación
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN**

48. **INC-HITL-003:** Registro Intentos Modificación Controles Críticos
    - **Componente:** Java Backend + DBA
    - **ViewModels Afectados:** Todos los ViewModels con controles críticos
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN**

49. **INC-HITL-005:** Firma Digital Aprobaciones
    - **Componente:** Java Backend
    - **ViewModels Afectados:** Todos los ViewModels con workflows de aprobación
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN**

50. **INC-HITL-007:** Validación Esquema JSONB
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ViewModels con configuraciones JSONB
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN**

51. **INC-HITL-008:** Campo IP/Origen Solicitudes Aprobación
    - **Componente:** Java Backend
    - **ViewModels Afectados:** Todos los ViewModels con workflows de aprobación
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN**

52. **INC-INT-001:** Conector Microsoft Copilot
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ExternalPlatformsViewModel

53. **INC-INT-002:** Cifrado Tokens y Credenciales
    - **Componente:** Java Backend + Security
    - **ViewModels Afectados:** ExternalPlatformsViewModel, ProvidersOverviewViewModel, ProvidersDetailViewModel
    - **Estado:** 🔴 **PENDIENTE** (Crítica)

54. **INC-INT-003:** Validación Integridad Metadata
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ExternalPlatformsViewModel, ProvidersOverviewViewModel, ProvidersDetailViewModel

55. **INC-INT-004:** Rate Limiting Webhooks
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ExternalPlatformsViewModel, ProvidersOverviewViewModel, ProvidersDetailViewModel

56. **INC-INT-005:** Monitoreo Latencia Sync
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ExternalPlatformsViewModel, ProvidersOverviewViewModel, ProvidersDetailViewModel

57. **INC-INT-006:** Retry Backoff Exponencial
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ExternalPlatformsViewModel, ProvidersOverviewViewModel, ProvidersDetailViewModel

58. **INC-005-003:** Validación Proactiva Políticas Cliente
    - **Componente:** Java Backend
    - **ViewModels Afectados:** ComplianceAiActViewModel, RagClientPoliciesOverviewViewModel, RagClientPoliciesDetailViewModel

#### BusinessServices Nuevos que Afectan ViewModels (10 prompts, 9 servicios únicos):

**📌 NOTA IMPORTANTE:**
- **Total BusinessServices en el módulo:** ~33 (20 core + 13 integraciones)
- **BusinessServices que ya existían:** ~11 (FriaAssessmentBusinessService, ImmutableLoggingBusinessService, ComplianceAssessmentBusinessService, QualityManagementSystemBusinessService, EuRegistrationBusinessService, TechnicalDocumentationBusinessService, AIObjectivesBusinessService, AICompetenceBusinessService, ModelAdaptationBusinessService, ExternalIntegrationBusinessService, AnnexIIICategoryBusinessService)
- **BusinessServices NUEVOS creados:** 9 servicios (listados abajo)
- Todos estos BusinessServices nuevos pueden desarrollarse **de forma independiente**. Ver análisis detallado en `ANALISIS_INDEPENDENCIA_BUSINESS_SERVICES.md`.

59. **INC-011:** Checklist Completo Validación Modelos (ModelValidationService)
    - **ViewModels Afectados:** ModelsOverviewViewModel, ModelsDetailViewModel, ModelApprovalWorkflowViewModel
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.models.ModelValidationService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java`
    - **Métodos Principales:**
      - `executeValidationChecklist(Long modelId)` - Ejecuta checklist completo de validación
      - `verifyOpenAITerms(Long modelId)` - Verifica términos específicos de OpenAI
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private ModelValidationService modelValidationService;

      public void validateModel() {
          Map<String, Object> results = modelValidationService.executeValidationChecklist(modelId);
          // Usar results para mostrar en UI
      }
      ```

60. **INC-015:** Verificación Términos OpenAI (ModelValidationService)
    - **ViewModels Afectados:** ModelsOverviewViewModel, ModelsDetailViewModel, ModelApprovalWorkflowViewModel
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo
    - **BusinessService:** ✅ **IMPLEMENTADO** - Mismo servicio que INC-011 (ModelValidationService)
    - **Método Específico:** `verifyOpenAITerms(Long modelId)`

61. **INC-010:** Umbrales Configurables Métricas (MetricThresholdService)
    - **ViewModels Afectados:** AnalyticsMetricViewModel, AnalyticsOverviewViewModel, MonitoringDashboardViewModel
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.governance.MetricThresholdService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/MetricThresholdService.java`
    - **Métodos Principales:**
      - `configureThreshold(Long metricId, String thresholdType, BigDecimal thresholdValue)` - Configura umbral
      - `getThresholds(Long metricId)` - Obtiene umbrales configurados
      - `evaluateThresholds(Long metricId, BigDecimal currentValue)` - Evalúa si valor excede umbrales
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private MetricThresholdService metricThresholdService;

      public void configureMetricThreshold() {
          metricThresholdService.configureThreshold(metricId, "WARNING", new BigDecimal("0.80"));
      }
      ```

62. **INC-014:** Retención Histórica Evaluaciones (EvaluationHistoryService)
    - **ViewModels Afectados:** Todos los ViewModels con evaluaciones (ModelsDetailViewModel, RagSystemsDetailViewModel, etc.)
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.evaluation.EvaluationHistoryService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationHistoryService.java`
    - **Métodos Principales:**
      - `getModelEvaluationHistory(Long modelId, Integer limit)` - Historial de evaluaciones de modelo
      - `getRagEvaluationHistory(Long ragSystemId, Integer limit)` - Historial de evaluaciones RAG
      - `getModelEvaluationStatistics(Long modelId)` - Estadísticas del historial
      - `archiveOldEvaluations(Integer retentionDays)` - Archiva evaluaciones antiguas
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private EvaluationHistoryService evaluationHistoryService;

      public void loadEvaluationHistory() {
          List<ModelEvaluation> history = evaluationHistoryService.getModelEvaluationHistory(modelId, 10);
          // Mostrar historial en UI
      }
      ```

63. **INC-020:** Integración APIs Autoridades (AuthorityNotificationService)
    - **ViewModels Afectados:** EURegistrationStatusViewModel, EuRegistrationFormViewModel, ReviewRegistrationPackageViewModel
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo (integración externa opcional)
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.compliance.AuthorityNotificationService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/AuthorityNotificationService.java`
    - **Métodos Principales:**
      - `notifyAuthority(Long registrationId)` - Notifica a la autoridad sobre registro
      - `queryRegistrationStatus(Long registrationId)` - Consulta estado de registro en autoridad
    - **Configuración:** `eu.authority.api.url` y `eu.authority.api.key` en properties (opcional)
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private AuthorityNotificationService authorityNotificationService;

      public void submitToAuthority() {
          Map<String, Object> response = authorityNotificationService.notifyAuthority(registrationId);
          // Manejar respuesta
      }
      ```

64. **INC-019:** Notificaciones Vencimientos (NotificationSchedulerService)
    - **ViewModels Afectados:** Todos los ViewModels con notificaciones
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.governance.NotificationSchedulerService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/NotificationSchedulerService.java`
    - **Métodos Principales:**
      - `scheduleExpirationNotification(Long userId, String notificationType, Timestamp dueDate, Integer daysBefore)` - Programa notificación
      - `getPendingScheduledNotifications()` - Obtiene notificaciones pendientes
      - `processPendingNotifications()` - Procesa notificaciones programadas
      - `cancelScheduledNotification(Long notificationId)` - Cancela notificación programada
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private NotificationSchedulerService notificationSchedulerService;

      public void scheduleNotification() {
          notificationSchedulerService.scheduleExpirationNotification(userId, "FRIA", dueDate, 7);
      }
      ```

65. **INC-017:** Dashboard Consolidado Compliance (ComplianceDashboardService)
    - **ViewModels Afectados:** GovernanceDashboardViewModel, ComplianceAiActViewModel, SectorDashboardViewModel
    - **Independencia:** ⚠️ **MEDIA** - Agrega datos de múltiples entidades, pero puede desarrollarse independientemente
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.compliance.ComplianceDashboardService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ComplianceDashboardService.java`
    - **Métodos Principales:**
      - `getComplianceMetrics()` - Obtiene métricas consolidadas de compliance
      - `getComplianceMetricsBySector()` - Obtiene métricas por sector
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private ComplianceDashboardService complianceDashboardService;

      public void loadDashboardMetrics() {
          Map<String, Object> metrics = complianceDashboardService.getComplianceMetrics();
          // Mostrar métricas en dashboard
      }
      ```

66. **INC-022:** Caché Evaluaciones Técnicas (EvaluationCacheService)
    - **ViewModels Afectados:** Todos los ViewModels con evaluaciones
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.evaluation.EvaluationCacheService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationCacheService.java`
    - **Métodos Principales:**
      - `getCachedModelEvaluation(Long modelId, String evaluationType)` - Obtiene evaluación desde caché
      - `getCachedRagEvaluation(Long ragSystemId, String evaluationType)` - Obtiene evaluación RAG desde caché
      - `invalidateModelEvaluationCache(Long modelId)` - Invalida caché de modelo
      - `invalidateRagEvaluationCache(Long ragSystemId)` - Invalida caché de RAG
      - `cleanExpiredCache()` - Limpia caché expirado
    - **TTL:** 1 hora (configurable)
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private EvaluationCacheService evaluationCacheService;

      public void loadEvaluation() {
          ModelEvaluation eval = evaluationCacheService.getCachedModelEvaluation(modelId, "TECHNICAL");
          // Usar evaluación (desde caché o BD)
      }
      ```

67. **INC-024:** Reporte Ejecutivo Consolidado (ComplianceExecutiveReportService)
    - **Componente:** Java Backend + Frontend
    - **ViewModels Afectados:** AnalyticsReportViewModel, GovernanceDashboardViewModel, **ComplianceReportViewModel** (✅ implementado)
    - **Estado:** ✅ **IMPLEMENTADO** - BusinessService creado
    - **Relación con Demo:** Pantalla 10 - Documentación Automática / Reporte Final (✅ ViewModel implementado)
    - **BusinessService:** ✅ **IMPLEMENTADO** - `com.codeflowx.govern.business.compliance.ComplianceExecutiveReportService`
    - **Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ComplianceExecutiveReportService.java`
    - **Independencia:** ⚠️ **MEDIA** - Puede desarrollarse con fallback local (microservicio Python opcional)
    - **Métodos Principales:**
      - `generateExecutiveReport(String reportType, Timestamp startDate, Timestamp endDate)` - Genera reporte ejecutivo
    - **Configuración:** `compliance.report.service.url` en properties (opcional para generación PDF)
    - **Cómo Usar en ViewModels:**
      ```java
      @WireVariable
      private ComplianceExecutiveReportService complianceExecutiveReportService;

      public void generateReport() {
          Map<String, Object> report = complianceExecutiveReportService.generateExecutiveReport("EXECUTIVE", startDate, endDate);
          // Mostrar reporte en UI o exportar PDF
      }
      ```

68. **INC-001:** Validación Modelo-Dataset (ProjectBusinessService)
    - **ViewModels Afectados:** ProjectsDashboardViewModel, ProjectAIInventoryViewModel, HighRiskClassifierViewModel
    - **Independencia:** ✅ **ALTA** - Puede desarrollarse en paralelo
    - **Nota:** Este servicio puede implementarse como método adicional en `ProjectService` existente o como servicio separado

---

### PROMPTS QUE NO AFECTAN DIRECTAMENTE A VIEWMODELS (17-22 prompts)

Estos prompts afectan principalmente a:
- Documentación
- DBA (SQL puro)
- Testing
- Infraestructura backend sin UI

1. **INC-008-003:** Documentación Cumplimiento Art. 19
   - **Componente:** Documentación
   - **No afecta ViewModels directamente**

2. **INC-008-005:** Inconsistencia Algoritmo Hash
   - **Componente:** Java Backend + SQL
   - **Afecta indirectamente (infraestructura)**

3. **INC-008-006:** Soporte Timestamp Externo RFC 3161
   - **Componente:** Java Backend + Integración Externa
   - **Afecta indirectamente (infraestructura)**

4. **INC-009-006:** Health Indicator
   - **Componente:** Java Backend (Spring)
   - **Afecta indirectamente (infraestructura)**

5. **INC-010-011:** Optimización Consultas Vistas Materializadas
   - **Componente:** DBA (SQL)
   - **Afecta indirectamente (performance)**

6. **INC-INT-007:** Documentación Límites APIs
   - **Componente:** Documentación
   - **No afecta ViewModels directamente**

7. **INC-INT-008:** Testing E2E Integraciones
   - **Componente:** Java Testing
   - **No afecta ViewModels directamente**

---

## 📊 RESUMEN POR CATEGORÍA DE VIEWMODELS

### ViewModels con Prompts Directos (2):
- HighRiskClassifierViewModel: 5 prompts
- FriaWizardViewModel: 5 prompts

### ViewModels con Prompts Indirectos (63-68 ViewModels):

#### Compliance (12 ViewModels):
- Todos afectados por prompts de compliance, PMM, versionado, controles pre-despliegue

#### FRIA (4 ViewModels):
- Todos afectados por prompts de FRIA, validación, versionado

#### EU Registration (4 ViewModels):
- Todos afectados por prompts de registro, PMM, APIs autoridades

#### Models (10 ViewModels):
- Todos afectados por prompts de validación modelos, versionado, controles pre-despliegue

#### RAG (4 ViewModels):
- Todos afectados por prompts de evaluación, versionado

#### Prompts (3 ViewModels):
- Todos afectados por prompts de validación, controles pre-despliegue

#### Training (2 ViewModels):
- Todos afectados por prompts de versionado datasets, integridad

#### Serving (1 ViewModel):
- Afectado por prompts de monitoreo, métricas

#### Projects (2 ViewModels):
- Todos afectados por prompts de validación modelo-dataset, versionado

#### Analytics (9 ViewModels):
- Todos afectados por prompts de visualizaciones, métricas, benchmarks

#### Agents (4 ViewModels):
- Todos afectados por prompts de monitoreo, controles pre-despliegue

#### Monitoring (1 ViewModel):
- Afectado por múltiples prompts de monitoreo, métricas, health

#### Integrations (1 ViewModel):
- Afectado por prompts de integración

#### Incident (4 ViewModels):
- Todos afectados por prompts de workflows, PMM

#### Infrastructure (2 ViewModels):
- Afectados indirectamente por prompts de infraestructura

#### Providers (2 ViewModels):
- Afectados por prompts de integración

#### Catalog (2 ViewModels):
- Afectados indirectamente

#### Core (5 ViewModels):
- Afectados por prompts de control acceso, logging

#### Dashboard (2 ViewModels):
- Afectados por prompts de dashboards, métricas

#### Education (1 ViewModel):
- Afectado indirectamente

#### Governance (16 ViewModels):
- Todos afectados por múltiples prompts de governance, compliance, HITL

---

## 🎯 CONCLUSIÓN

**Total Prompts que Afectan ViewModels de Governance:** ~65-70 de 87 (75-80%)

**Total ViewModels Afectados:** ~65-70 de 91 (72-77%)

**Prompts que NO Afectan ViewModels:** ~17-22 (20-25%)
- Principalmente: Documentación, DBA puro, Testing, Infraestructura backend sin UI

---

**Última actualización:** 25 de noviembre de 2025
**Análisis Corregido:** Sí - Considerando todos los 91 ViewModels como ViewModels de Governance

---

## ✅ BUSINESS SERVICES IMPLEMENTADOS

### BusinessServices NUEVOS Creados (9 de 9 - 100%)

Estos son los BusinessServices **nuevos** creados específicamente para cumplir con los prompts mencionados:

1. ✅ **ModelValidationService** - `com.codeflowx.govern.business.models.ModelValidationService`
   - **Prompts:** INC-011, INC-015
2. ✅ **MetricThresholdService** - `com.codeflowx.govern.business.governance.MetricThresholdService`
   - **Prompts:** INC-010
3. ✅ **EvaluationHistoryService** - `com.codeflowx.govern.business.evaluation.EvaluationHistoryService`
   - **Prompts:** INC-014
4. ✅ **AuthorityNotificationService** - `com.codeflowx.govern.business.compliance.AuthorityNotificationService`
   - **Prompts:** INC-020
5. ✅ **NotificationSchedulerService** - `com.codeflowx.govern.business.governance.NotificationSchedulerService`
   - **Prompts:** INC-019
6. ✅ **ComplianceDashboardService** - `com.codeflowx.govern.business.compliance.ComplianceDashboardService`
   - **Prompts:** INC-017
7. ✅ **EvaluationCacheService** - `com.codeflowx.govern.business.evaluation.EvaluationCacheService`
   - **Prompts:** INC-022
8. ✅ **ComplianceExecutiveReportService** - `com.codeflowx.govern.business.compliance.ComplianceExecutiveReportService`
   - **Prompts:** INC-024
9. ✅ **PostMarketMonitoringService** - `com.codeflowx.govern.business.compliance.PostMarketMonitoringService`
   - **Prompts:** INC-010-004

**Ubicación Base:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`

### BusinessServices que Ya Existían (~11 servicios)

Estos BusinessServices ya existían antes de este análisis y fueron documentados:

1. ✅ **FriaAssessmentBusinessService** - `com.codeflowx.govern.business.compliance.FriaAssessmentBusinessService`
2. ✅ **ImmutableLoggingBusinessService** - `com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService`
3. ✅ **ComplianceAssessmentBusinessService** - `com.codeflowx.govern.business.compliance.ComplianceAssessmentBusinessService`
4. ✅ **QualityManagementSystemBusinessService** - `com.codeflowx.govern.business.compliance.QualityManagementSystemBusinessService`
5. ✅ **EuRegistrationBusinessService** - `com.codeflowx.govern.business.compliance.EuRegistrationBusinessService`
6. ✅ **TechnicalDocumentationBusinessService** - `com.codeflowx.govern.business.compliance.TechnicalDocumentationBusinessService`
7. ✅ **AIObjectivesBusinessService** - `com.codeflowx.govern.business.governance.AIObjectivesBusinessService`
8. ✅ **AICompetenceBusinessService** - `com.codeflowx.govern.business.governance.AICompetenceBusinessService`
9. ✅ **ModelAdaptationBusinessService** - `com.codeflowx.govern.business.models.ModelAdaptationBusinessService`
10. ✅ **ExternalIntegrationBusinessService** - `com.codeflowx.govern.business.integrations.ExternalIntegrationBusinessService`
11. ✅ **AnnexIIICategoryBusinessService** - `com.codeflowx.govern.business.catalogs.AnnexIIICategoryBusinessService`

### Resumen Total

- **BusinessServices NUEVOS:** 9 servicios (100% implementados)
- **BusinessServices EXISTENTES:** ~11 servicios (documentados)
- **BusinessServices TOTALES en módulo:** ~33 servicios (20 core + 13 integraciones)
- **Total Documentados:** 32 servicios (ver `docs/developers/README.md`)

**Nota:** Todos los BusinessServices están listos para ser inyectados en ViewModels usando `@WireVariable` o `@Autowired`.

---

## 📚 DOCUMENTOS RELACIONADOS

- **`ANALISIS_INDEPENDENCIA_BUSINESS_SERVICES.md`** - Análisis detallado sobre si los BusinessServices pueden desarrollarse de forma independiente
- **`PANTALLAS_DEMO_GOVERNANCE.md`** - Mapeo de pantallas de demo con su estado de implementación
- **`ESTADO_PROMPTS_SEGURIDAD_ROLES.md`** - Estado de prompts relacionados con seguridad y roles
