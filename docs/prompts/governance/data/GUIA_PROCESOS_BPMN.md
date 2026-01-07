# 🔄 GUÍA DE PROCESOS BPMN - GOBIERNO DEL DATO

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** BPMN Developers, Workflow Engineers, Backend Developers

---

## 📋 INTRODUCCIÓN

Esta guía describe qué procesos BPMN deben crearse o modificarse para soportar el módulo de Gobierno del Dato. Los procesos BPMN se encuentran en `codeflowx.govern.workflow.lib` y utilizan Flowable como motor de workflows.

---

## 🏗️ PROCESOS BPMN EXISTENTES

### Procesos Relacionados con Datasets

Los siguientes procesos BPMN ya existen en el sistema:

| Proceso | Archivo | Versión | Estado |
|---------|---------|---------|--------|
| Dataset Quality Assessment | `dataset-quality-v1.bpmn` | 1.0 | ✅ Implementado |
| Dataset Risk Assessment | `dataset-risk-assessment-v1.bpmn` | 1.0 | ✅ Implementado |
| Dataset Privacy Assessment | `dataset-privacy-assessment-v1.bpmn` | 1.0 | ✅ Implementado |
| Dataset Approval | `dataset-approval-v1.bpmn` | 1.0 | ✅ Implementado |
| Dataset Lineage Tracking | `dataset-lineage-tracking-v1.bpmn` | 1.0 | ✅ Implementado |
| Dataset Documentation | `dataset-documentation-v1.bpmn` | 1.0 | ✅ Implementado |

### Delegates Existentes

| Delegate | Clase | Proceso |
|----------|-------|---------|
| AssessDatasetRiskDelegate | `AssessDatasetRiskDelegate.java` | Risk Assessment |
| CalculateDatasetRiskScoreDelegate | `CalculateDatasetRiskScoreDelegate.java` | Risk Assessment |
| StoreDatasetRiskAssessmentDelegate | `StoreDatasetRiskAssessmentDelegate.java` | Risk Assessment |
| NotifyDatasetRiskAssessmentDelegate | `NotifyDatasetRiskAssessmentDelegate.java` | Risk Assessment |
| ApproveDatasetRiskDelegate | `ApproveDatasetRiskDelegate.java` | Risk Assessment |
| DetectPIIDelegate | `DetectPIIDelegate.java` | Privacy Assessment |
| ApproveDatasetDelegate | `ApproveDatasetDelegate.java` | Approval |
| DataProfilingDelegate | `DataProfilingDelegate.java` | Quality Assessment |
| CalculateDatasetScoreDelegate | `CalculateDatasetScoreDelegate.java` | Quality Assessment |
| DetectDatasetBiasDelegate | `DetectDatasetBiasDelegate.java` | Quality Assessment |
| ValidateDatasetFormatDelegate | `ValidateDatasetFormatDelegate.java` | Quality Assessment |
| ValidateComplianceDelegate | `ValidateComplianceDelegate.java` | Compliance |

---

## 🆕 PROCESOS BPMN A CREAR

### 1. **Proceso de Estandarización de Dataset**

**Archivo:** `dataset-standardization-v1.bpmn`
**Prioridad:** 🔴 Alta
**Descripción:** Proceso que convierte un dataset a formato Parquet.

#### Flujo del Proceso

```
[Start] → [Validar Formato Origen] → [Convertir a Parquet] →
[Validar Parquet] → [Calcular Checksum] → [Almacenar en MinIO] →
[Actualizar Metadata] → [End]
```

#### Tareas de Servicio

1. **ValidateSourceFormatDelegate**
   - Valida que el formato origen es soportado (CSV, JSON, Excel, etc.)
   - Verifica que el archivo existe y es accesible

2. **ConvertToParquetDelegate**
   - Llama al microservicio `dataset-standardization` (8017)
   - Convierte el dataset a formato Parquet
   - Aplica compresión Snappy

3. **ValidateParquetDelegate**
   - Valida que el Parquet generado es válido
   - Verifica esquema y estructura

4. **CalculateChecksumDelegate**
   - Calcula checksum SHA256 del archivo Parquet
   - Almacena checksum en metadata

5. **StoreInMinIODelegate**
   - Almacena el archivo Parquet en MinIO
   - Actualiza ruta en metadata del dataset

6. **UpdateDatasetMetadataDelegate**
   - Actualiza metadata del dataset:
     - Formato: PARQUET
     - Ruta en MinIO
     - Checksum
     - Tamaño
     - Fecha de estandarización

#### Variables del Proceso

```java
- datasetId: String
- sourcePath: String
- sourceFormat: String (CSV, JSON, EXCEL, etc.)
- targetPath: String
- checksum: String
- fileSize: Long
- standardizationStatus: String (PENDING, IN_PROGRESS, COMPLETED, FAILED)
```

#### Eventos de Error

- `standardization_failed`: Si la conversión falla
- `validation_failed`: Si la validación falla
- `storage_failed`: Si el almacenamiento falla

---

### 2. **Proceso de Análisis de Calidad Completo**

**Archivo:** `dataset-quality-complete-v1.bpmn`
**Prioridad:** 🔴 Alta
**Descripción:** Proceso completo que ejecuta análisis de calidad según 6 dimensiones ISO 8000.

#### Flujo del Proceso

```
[Start] → [Cargar Configuración] → [Paralelo: 6 Dimensiones] →
  ├─ [Completitud] → [Almacenar Resultado]
  ├─ [Precisión] → [Almacenar Resultado]
  ├─ [Consistencia] → [Almacenar Resultado]
  ├─ [Validez] → [Almacenar Resultado]
  ├─ [Puntualidad] → [Almacenar Resultado]
  └─ [Unicidad] → [Almacenar Resultado]
→ [Calcular Score Global] → [Evaluar Umbrales] →
[Generar Alertas] → [Notificar Resultados] → [End]
```

#### Tareas de Servicio

1. **LoadQualityConfigDelegate**
   - Carga configuración de análisis (sample percentage, umbrales)
   - Valida que el dataset existe

2. **EvaluateCompletenessDelegate**
   - Llama al microservicio `dataset-quality` (8015)
   - Evalúa dimensión de Completitud
   - Almacena resultado en `DataGovernanceQualityMetric`

3. **EvaluateAccuracyDelegate**
   - Evalúa dimensión de Precisión
   - Almacena resultado

4. **EvaluateConsistencyDelegate**
   - Evalúa dimensión de Consistencia
   - Almacena resultado

5. **EvaluateValidityDelegate**
   - Evalúa dimensión de Validez
   - Almacena resultado

6. **EvaluateTimelinessDelegate**
   - Evalúa dimensión de Puntualidad
   - Almacena resultado

7. **EvaluateUniquenessDelegate**
   - Evalúa dimensión de Unicidad
   - Almacena resultado

8. **CalculateGlobalScoreDelegate**
   - Calcula score global promedio de las 6 dimensiones
   - Actualiza score en `DataGovernanceDataset`

9. **EvaluateThresholdsDelegate**
   - Evalúa si cada dimensión cumple umbrales
   - Genera alertas si hay dimensiones en WARNING o FAIL

10. **NotifyQualityResultsDelegate**
    - Notifica resultados a stakeholders
    - Envía alertas si es necesario

#### Gateway Exclusivo

Después de evaluar umbrales:
- Si `overallScore >= 0.9` → Flujo de éxito
- Si `overallScore >= 0.7 && overallScore < 0.9` → Flujo de advertencia
- Si `overallScore < 0.7` → Flujo de error

#### Variables del Proceso

```java
- datasetId: String
- samplePercentage: Integer (10-100)
- completenessScore: Double
- accuracyScore: Double
- consistencyScore: Double
- validityScore: Double
- timelinessScore: Double
- uniquenessScore: Double
- overallScore: Double
- alerts: List<String>
```

---

### 3. **Proceso de Análisis de Impacto de Cambios**

**Archivo:** `dataset-impact-analysis-v1.bpmn`
**Prioridad:** 🟡 Media
**Descripción:** Analiza qué datasets se verían afectados por cambios.

#### Flujo del Proceso

```
[Start] → [Cargar Dependencias] → [Analizar Impacto] →
[Clasificar Impacto] → [Generar Reporte] → [Notificar Stakeholders] → [End]
```

#### Tareas de Servicio

1. **LoadDependenciesDelegate**
   - Carga información de dependencias desde `DataGovernanceLineage`
   - Identifica datasets padre e hijos

2. **AnalyzeImpactDelegate**
   - Llama al microservicio `dataset-impact-analysis` (8018)
   - Analiza impacto de cambios propuestos
   - Identifica datasets afectados

3. **ClassifyImpactDelegate**
   - Clasifica impacto como HIGH, MEDIUM, LOW
   - Calcula métricas de impacto

4. **GenerateImpactReportDelegate**
   - Genera reporte de impacto
   - Almacena en `DataGovernanceDatasetDocumentation`

5. **NotifyStakeholdersDelegate**
   - Notifica a propietarios de datasets afectados
   - Envía alertas según nivel de impacto

#### Variables del Proceso

```java
- datasetId: String
- changeType: String (SCHEMA_CHANGE, DATA_CHANGE, DELETION)
- affectedDatasets: List<String>
- impactLevel: String (HIGH, MEDIUM, LOW)
- reportPath: String
```

---

### 4. **Proceso de Análisis Programado**

**Archivo:** `dataset-scheduled-analysis-v1.bpmn`
**Prioridad:** 🟡 Media
**Descripción:** Proceso que se ejecuta automáticamente según configuración de análisis.

#### Flujo del Proceso

```
[Timer Event] → [Cargar Configuración] → [Verificar Día/Hora] →
[Ejecutar Análisis de Calidad] → [Ejecutar Detección de PII] →
[Ejecutar Análisis de Sesgos] → [Generar Reporte] → [End]
```

#### Timer Event

- **Tipo:** Timer con expresión cron
- **Configuración:** Basada en `DataGovernanceDatasetAnalysisConfig`
  - `scheduleTime`: Hora de ejecución (ej: "02:00")
  - `scheduleDays`: Días de la semana (lunes-domingo)

#### Tareas de Servicio

1. **LoadAnalysisConfigDelegate**
   - Carga configuración desde `DataGovernanceDatasetAnalysisConfig`
   - Verifica que el análisis esté habilitado

2. **CheckScheduleDelegate**
   - Verifica que es el día y hora programados
   - Si no coincide, termina el proceso

3. **ExecuteQualityAnalysisDelegate**
   - Ejecuta proceso `dataset-quality-complete-v1.bpmn`
   - Usa `samplePercentage` de la configuración

4. **ExecutePIIDetectionDelegate**
   - Ejecuta detección de PII
   - Actualiza `DataGovernanceDatasetPrivacy`

5. **ExecuteBiasAnalysisDelegate**
   - Ejecuta análisis de sesgos
   - Actualiza métricas de sesgos

6. **GenerateScheduledReportDelegate**
   - Genera reporte consolidado
   - Almacena en documentación

---

## 🔄 PROCESOS BPMN A MODIFICAR

### 1. **Dataset Approval Process (dataset-approval-v1.bpmn)**

#### Modificaciones Necesarias

**Agregar Tarea:** Validar Análisis de Calidad
- Antes de aprobar, verificar que el análisis de calidad se ha ejecutado
- Si no existe, ejecutar análisis automáticamente

**Agregar Tarea:** Validar PII y GDPR
- Verificar que se ha configurado base legal (GDPR Art. 6)
- Si hay PII, verificar consentimiento (GDPR Art. 7)

**Agregar Gateway:** Evaluación de Requisitos
- Si todos los requisitos se cumplen → Aprobación automática
- Si faltan requisitos → Revisión manual

#### Nuevos Delegates

1. **ValidateQualityAnalysisDelegate**
   - Verifica que existe análisis de calidad reciente
   - Valida que el score está por encima del umbral mínimo

2. **ValidateGDPRComplianceDelegate**
   - Verifica cumplimiento GDPR
   - Valida base legal y consentimiento si aplica

---

### 2. **Dataset Risk Assessment (dataset-risk-assessment-v1.bpmn)**

#### Modificaciones Necesarias

**Agregar Tarea:** Análisis de Impacto
- Después de identificar riesgos, analizar impacto en otros datasets
- Integrar con proceso de análisis de impacto

**Agregar Tarea:** Plan de Mitigación Automático
- Generar sugerencias de mitigación basadas en tipo de riesgo
- Usar reglas Drools para recomendaciones

---

## 📊 RESUMEN DE PROCESOS

### Nuevos Procesos a Crear

| Proceso | Archivo | Prioridad | Complejidad |
|---------|---------|-----------|-------------|
| Dataset Standardization | `dataset-standardization-v1.bpmn` | 🔴 Alta | Media |
| Dataset Quality Complete | `dataset-quality-complete-v1.bpmn` | 🔴 Alta | Alta |
| Dataset Impact Analysis | `dataset-impact-analysis-v1.bpmn` | 🟡 Media | Media |
| Dataset Scheduled Analysis | `dataset-scheduled-analysis-v1.bpmn` | 🟡 Media | Media |

### Procesos a Modificar

| Proceso | Archivo | Modificaciones | Prioridad |
|---------|---------|----------------|-----------|
| Dataset Approval | `dataset-approval-v1.bpmn` | Validaciones de calidad y GDPR | 🔴 Alta |
| Dataset Risk Assessment | `dataset-risk-assessment-v1.bpmn` | Análisis de impacto | 🟡 Media |

---

## 🔧 ESPECIFICACIONES TÉCNICAS

### Estructura de Archivos BPMN

Los procesos BPMN deben seguir esta estructura:

```xml
<bpmn:definitions>
  <bpmn:process id="dataset-standardization-v1">
    <!-- Start Event -->
    <bpmn:startEvent id="start"/>

    <!-- Service Tasks -->
    <bpmn:serviceTask id="validateFormat"
                      name="Validar Formato Origen"
                      implementation="${validateSourceFormatDelegate}"/>

    <!-- Exclusive Gateways -->
    <bpmn:exclusiveGateway id="formatValid"/>

    <!-- End Events -->
    <bpmn:endEvent id="end"/>
  </bpmn:process>
</bpmn:definitions>
```

### Convenciones de Nomenclatura

- **Procesos:** `dataset-{funcionalidad}-v{version}.bpmn`
- **Delegates:** `{Funcionalidad}Delegate.java`
- **Variables:** camelCase (ej: `datasetId`, `samplePercentage`)
- **Eventos:** snake_case (ej: `standardization_failed`)

### Integración con Delegates

Cada delegate debe:
1. Implementar `JavaDelegate` de Flowable
2. Inyectar servicios necesarios con `@Autowired`
3. Usar `DelegateExecution` para acceder a variables
4. Manejar excepciones y lanzar `BpmnError` si es necesario

**Ejemplo:**

```java
@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateSourceFormatDelegate implements JavaDelegate {

    private final DataGovernanceDatasetService datasetService;

    @Override
    public void execute(DelegateExecution execution) {
        String datasetId = (String) execution.getVariable("datasetId");
        String sourceFormat = (String) execution.getVariable("sourceFormat");

        // Validar formato
        if (!isSupportedFormat(sourceFormat)) {
            throw new BpmnError("UNSUPPORTED_FORMAT",
                "Formato no soportado: " + sourceFormat);
        }

        execution.setVariable("formatValid", true);
        log.info("Formato validado para dataset: {}", datasetId);
    }
}
```

---

## 📝 CHECKLIST DE IMPLEMENTACIÓN

### Dataset Standardization Process

- [ ] Crear archivo BPMN `dataset-standardization-v1.bpmn`
- [ ] Implementar `ValidateSourceFormatDelegate`
- [ ] Implementar `ConvertToParquetDelegate`
- [ ] Implementar `ValidateParquetDelegate`
- [ ] Implementar `CalculateChecksumDelegate`
- [ ] Implementar `StoreInMinIODelegate`
- [ ] Implementar `UpdateDatasetMetadataDelegate`
- [ ] Configurar eventos de error
- [ ] Tests de integración
- [ ] Documentación

### Dataset Quality Complete Process

- [ ] Crear archivo BPMN `dataset-quality-complete-v1.bpmn`
- [ ] Implementar `LoadQualityConfigDelegate`
- [ ] Implementar delegates para 6 dimensiones
- [ ] Implementar `CalculateGlobalScoreDelegate`
- [ ] Implementar `EvaluateThresholdsDelegate`
- [ ] Implementar `NotifyQualityResultsDelegate`
- [ ] Configurar gateway exclusivo
- [ ] Tests de integración
- [ ] Documentación

### Dataset Impact Analysis Process

- [ ] Crear archivo BPMN `dataset-impact-analysis-v1.bpmn`
- [ ] Implementar `LoadDependenciesDelegate`
- [ ] Implementar `AnalyzeImpactDelegate`
- [ ] Implementar `ClassifyImpactDelegate`
- [ ] Implementar `GenerateImpactReportDelegate`
- [ ] Implementar `NotifyStakeholdersDelegate`
- [ ] Tests de integración
- [ ] Documentación

### Dataset Scheduled Analysis Process

- [ ] Crear archivo BPMN `dataset-scheduled-analysis-v1.bpmn`
- [ ] Configurar timer event con expresión cron
- [ ] Implementar `LoadAnalysisConfigDelegate`
- [ ] Implementar `CheckScheduleDelegate`
- [ ] Integrar con otros procesos
- [ ] Tests de integración
- [ ] Documentación

### Modificaciones a Procesos Existentes

- [ ] Modificar `dataset-approval-v1.bpmn`
- [ ] Implementar `ValidateQualityAnalysisDelegate`
- [ ] Implementar `ValidateGDPRComplianceDelegate`
- [ ] Modificar `dataset-risk-assessment-v1.bpmn`
- [ ] Agregar análisis de impacto
- [ ] Tests de regresión
- [ ] Documentación

---

## 🚀 PRÓXIMOS PASOS

1. **Priorizar procesos:** Empezar con Dataset Standardization (crítico)
2. **Crear estructura base:** Archivos BPMN con estructura básica
3. **Implementar delegates:** Uno por uno, con tests
4. **Integrar con servicios:** Conectar con microservicios Python y servicios Java
5. **Testing:** Tests de integración con Flowable
6. **Documentación:** Documentar cada proceso con diagramas
7. **Despliegue:** Configurar en K8s y registrar en Flowable

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** 📋 Planificación
