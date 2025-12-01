# Documentación Técnica - Módulo Govern.Business

**Fecha:** 25 de noviembre de 2025
**Módulo:** `codeflowx.govern.business`
**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`

---

## 📋 Índice de BusinessServices

Este directorio contiene la documentación técnica y funcional completa de todos los BusinessServices del módulo `govern.business`, diseñada para desarrolladores y agentes de IA que necesiten entender, mantener y evolucionar el código.

### 🎯 Objetivo

Proporcionar documentación exhaustiva que permita:
- **Desarrolladores:** Comprender rápidamente la responsabilidad y uso de cada servicio
- **Agentes de IA:** Entender el contexto completo para generar código coherente
- **Arquitectos:** Evaluar dependencias y acoplamiento entre servicios
- **QA:** Entender el comportamiento esperado para crear tests efectivos

---

## 📚 Estructura de Documentación

Cada documento de servicio incluye:

1. **Descripción Funcional** - Qué hace el servicio y por qué existe
2. **Responsabilidades** - Qué debe hacer y qué NO debe hacer
3. **Dependencias** - Servicios, entidades y librerías que utiliza
4. **Arquitectura** - Cómo se integra en el sistema
5. **API Pública** - Métodos públicos con ejemplos
6. **Casos de Uso** - Escenarios reales de utilización
7. **Configuración** - Properties, variables de entorno
8. **Testing** - Estrategias de testing recomendadas
9. **Evolución** - Cómo extender o modificar el servicio
10. **Referencias** - Prompts asociados, artículos EU AI Act, ViewModels

---

## 📦 Servicios Documentados

**Total:** 22 servicios documentados

### Compliance (11 servicios)

1. **[FriaAssessmentBusinessService](./compliance/FriaAssessmentBusinessService.md)** - Evaluaciones de Impacto en Derechos Fundamentales (Art. 27)
2. **[ComplianceAssessmentBusinessService](./compliance/ComplianceAssessmentBusinessService.md)** - Evaluaciones de Conformidad (Anexo VI, Art. 43)
3. **[ComplianceDashboardService](./compliance/ComplianceDashboardService.md)** - Dashboard Consolidado de Compliance
4. **[ComplianceExecutiveReportService](./compliance/ComplianceExecutiveReportService.md)** - Reportes Ejecutivos de Compliance
5. **[EuRegistrationBusinessService](./compliance/EuRegistrationBusinessService.md)** - Registro en Base de Datos Europea (Art. 49)
6. **[QualityManagementSystemBusinessService](./compliance/QualityManagementSystemBusinessService.md)** - Sistema de Gestión de Calidad (Art. 17)
7. **[TechnicalDocumentationBusinessService](./compliance/TechnicalDocumentationBusinessService.md)** - Documentación Técnica (Anexo IV)
8. **[AuthorityNotificationService](./compliance/AuthorityNotificationService.md)** - Notificaciones a Autoridades
9. **[PostMarketMonitoringService](./compliance/PostMarketMonitoringService.md)** - Monitoreo Post-Mercado (Art. 72)
10. **[ProhibitedSystemBusinessService](./compliance/ProhibitedSystemBusinessService.md)** - Validación Sistemas Prohibidos (Art. 5, Anexo II) ⭐ NUEVO
11. **[MitigationMeasureValidationService](./compliance/MitigationMeasureValidationService.md)** - Validación Medidas Mitigación Implementadas (Art. 27.1.f) ⭐ NUEVO

### Governance (4 servicios)

10. **[MetricThresholdService](./governance/MetricThresholdService.md)** - Gestión de Umbrales de Métricas
11. **[NotificationSchedulerService](./governance/NotificationSchedulerService.md)** - Programación de Notificaciones
12. **[AIObjectivesBusinessService](./governance/AIObjectivesBusinessService.md)** - Objetivos IA (ISO/IEC 42001)
13. **[AICompetenceBusinessService](./governance/AICompetenceBusinessService.md)** - Competencias y Awareness (ISO/IEC 42001)

### Evaluation (2 servicios)

14. **[EvaluationHistoryService](./evaluation/EvaluationHistoryService.md)** - Historial de Evaluaciones
15. **[EvaluationCacheService](./evaluation/EvaluationCacheService.md)** - Caché de Evaluaciones Técnicas

### Models (2 servicios)

16. **[ModelValidationService](./models/ModelValidationService.md)** - Validación Completa de Modelos
17. **[ModelAdaptationBusinessService](./models/ModelAdaptationBusinessService.md)** - Adaptación de Modelos (Art. 51-55)

### Logging (1 servicio)

18. **[ImmutableLoggingBusinessService](./logging/ImmutableLoggingBusinessService.md)** - Logs Inmutables con Hash Chains (Art. 19)

### Integrations (13 servicios)

19. **[ExternalIntegrationBusinessService](./integrations/ExternalIntegrationBusinessService.md)** - Gestión General de Integraciones Externas
20. **[SageMakerConnectorService](./integrations/SageMakerConnectorService.md)** - Conector AWS SageMaker
21. **[AzureMLConnectorService](./integrations/AzureMLConnectorService.md)** - Conector Azure ML
22. **[VertexAIConnectorService](./integrations/VertexAIConnectorService.md)** - Conector Google Vertex AI
23. **[DatabricksConnectorService](./integrations/DatabricksConnectorService.md)** - Conector Databricks + MLflow
24. **[SnowflakeConnectorService](./integrations/SnowflakeConnectorService.md)** - Conector Snowflake
25. **[PurviewConnectorService](./integrations/PurviewConnectorService.md)** - Conector Microsoft Purview
26. **[FabricConnectorService](./integrations/FabricConnectorService.md)** - Conector Microsoft Fabric
27. **[JiraConnectorService](./integrations/JiraConnectorService.md)** - Conector Jira Cloud
28. **[ServiceNowConnectorService](./integrations/ServiceNowConnectorService.md)** - Conector ServiceNow ITSM
29. **[IbmWatsonxConnectorService](./integrations/IbmWatsonxConnectorService.md)** - Conector IBM watsonx.ai
30. **[SparkEvaluationService](./integrations/SparkEvaluationService.md)** - Evaluaciones Spark vía Livy
31. **[DataLakeCatalogService](./integrations/DataLakeCatalogService.md)** - Catalogación Data Lakes (S3, Azure Blob, GCS)

### Catalogs (1 servicio)

32. **[AnnexIIICategoryBusinessService](./catalogs/AnnexIIICategoryBusinessService.md)** - Categorías Anexo III

---

## 📊 Resumen Total

**Total de servicios documentados:** 34 servicios

- **Compliance:** 11 servicios
- **Governance:** 4 servicios
- **Evaluation:** 2 servicios
- **Models:** 2 servicios
- **Logging:** 1 servicio
- **Integrations:** 13 servicios
- **Catalogs:** 1 servicio

---

## 🏗️ Arquitectura del Módulo

```
codeflowx.govern.business
├── compliance/          # Servicios de compliance EU AI Act
├── governance/          # Servicios de governance y métricas
├── evaluation/          # Servicios de evaluación
├── models/             # Servicios de modelos
├── logging/             # Servicios de logging
├── integrations/        # Servicios de integración
└── catalogs/           # Servicios de catálogos
```

### Patrón de Diseño

Todos los servicios siguen el patrón **BusinessService** del framework EnArt:
- Usan `@Service` de Spring
- Inyectan `BusinessService` (DAO) para acceso a datos
- NO usan Repository pattern (solo DAO)
- Manejan excepciones con `BussinessException`
- Usan logging con SLF4J

### Inyección de Dependencias

```java
// En ViewModels (ZKoss)
@WireVariable
private FriaAssessmentBusinessService friaAssessmentBusinessService;

// En otros servicios (Spring)
@Autowired
private FriaAssessmentBusinessService friaAssessmentBusinessService;
```

---

## 🔗 Referencias Externas

- **EU AI Act:** https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32021R0106
- **ISO/IEC 42001:** Estándar de gestión de IA
- **Documentación Compliance:** `/docs/compliance/revision/`
- **Mapeo Prompts:** `/docs/compliance/revision/MAPEO_PROMPTS_VIEWMODELS_GOVERNANCE_CORREGIDO.md`

---

## 📝 Convenciones de Documentación

- **Código:** Se muestra con bloques de código con sintaxis Java
- **Ejemplos:** Todos los ejemplos son funcionales y probados
- **Referencias:** Se incluyen enlaces a artículos del EU AI Act cuando aplica
- **Prompts:** Se referencia el prompt asociado del sistema de incidencias

---

## 🚀 Cómo Usar Esta Documentación

1. **Para entender un servicio:** Lee el documento completo del servicio
2. **Para usar un servicio:** Ve a la sección "API Pública" y "Casos de Uso"
3. **Para modificar un servicio:** Revisa "Dependencias" y "Evolución"
4. **Para testear:** Consulta la sección "Testing"

---

**Última actualización:** 25 de noviembre de 2025
**Mantenido por:** CodeflowX Development Team
