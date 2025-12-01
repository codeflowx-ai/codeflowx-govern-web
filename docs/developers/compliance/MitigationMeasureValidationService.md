# MitigationMeasureValidationService

**Ubicación:** `com.codeflowx.govern.business.compliance.MitigationMeasureValidationService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

El `MitigationMeasureValidationService` valida que las medidas de mitigación declaradas en una FRIA estén realmente implementadas y funcionando en el sistema. Según el **Art. 27.1.f del EU AI Act**, las medidas de mitigación deben estar implementadas, no solo declaradas.

### Propósito

Este servicio permite:
- Validar que medidas declaradas estén implementadas
- Verificar configuración técnica de medidas
- Validar que medidas estén activas y funcionando
- Requerir evidencia técnica de implementación

---

## 🎯 Responsabilidades

### ✅ Qué Hace Este Servicio

1. **Validación de Medidas de Mitigación**
   - Parsea medidas desde JSONB del FRIA
   - Valida medidas preventivas, detective y correctivas
   - Verifica implementación real de cada medida

2. **Verificación Técnica**
   - Monitoreo de sesgos (bias detection)
   - Validación de inputs
   - Monitoreo continuo
   - Alertas automáticas
   - Proceso de apelación
   - Override humano
   - Rollback automático

3. **Cálculo de Score**
   - Calcula score basado en medidas implementadas vs declaradas
   - Identifica medidas no implementadas

### ❌ Qué NO Hace Este Servicio

- **NO** gestiona CRUD de medidas (se almacenan en FRIA como JSONB)
- **NO** implementa las medidas (solo valida que estén implementadas)
- **NO** modifica el FRIA (solo valida y reporta)

---

## 🔗 Dependencias

### Entidades

```java
import com.codeflowx.govern.entity.compliance.FriaAssessment;
import com.codeflowx.govern.entity.compliance.MitigationMeasure;
import com.codeflowx.govern.entity.projects.Project;
```

### Servicios

```java
@Autowired
private BiasDetectionService biasDetectionService; // Service CRUD
@Autowired
private ModelEvaluationService modelEvaluationService; // Service CRUD
@Autowired
private AdversarialEvaluationService adversarialEvaluationService; // Workflow service
@Autowired
private DriftDetectionService driftDetectionService; // Service CRUD
@Autowired
private SystemAlertService systemAlertService; // Service CRUD
@Autowired
private BusinessService businessService; // Para consultas SQL directas
```

### Librerías

- `org.springframework.stereotype.Service`
- `com.fasterxml.jackson.databind.ObjectMapper`
- `lombok.extern.slf4j.Slf4j`

---

## 🏗️ Arquitectura

### Integración en el Sistema

```
┌─────────────────────────────────┐
│  FriaWizardViewModel            │
│  - Generación de FRIA           │
└──────────────┬──────────────────┘
               │ @WireVariable
               ▼
┌─────────────────────────────────┐
│  MitigationMeasureValidationService│
│  - validateMitigationMeasures() │
└──────────────┬──────────────────┘
               │ Usa Services
               ▼
┌─────────────────────────────────┐
│  BiasDetectionService           │
│  ModelEvaluationService         │
│  AdversarialEvaluationService   │
└─────────────────────────────────┘
```

---

## 📚 API Pública

### Métodos Principales

#### 1. `validateMitigationMeasures(FriaAssessment fria, Project project)`

Valida que medidas de mitigación declaradas estén implementadas.

**Parámetros:**
- `fria`: Evaluación FRIA con medidas declaradas (no puede ser null)
- `project`: Proyecto asociado (no puede ser null)

**Retorna:** `MitigationValidationResult` con:
- `issues`: Lista de issues encontrados
- `score`: Score de implementación (0.00 - 1.00)
- `allImplemented`: true si todas las medidas están implementadas
- `implementedCount`: Número de medidas implementadas
- `totalCount`: Número total de medidas declaradas

**Uso:**
```java
@WireVariable
private MitigationMeasureValidationService mitigationValidationService;

MitigationValidationResult result =
    mitigationValidationService.validateMitigationMeasures(fria, project);

if (!result.isAllImplemented()) {
    // Mostrar alerta y requerir implementación
    showMitigationValidationAlert(result);
}
```

---

## 🔍 Detalles de Implementación

### Tipos de Medidas Validadas

#### Medidas Preventivas (PREVENTIVE)
- Auditoría periódica de sesgos
- Validación de datos de entrada
- Validación de robustez adversarial

#### Medidas Detective (DETECTIVE)
- Monitoreo continuo de métricas
- Alertas automáticas
- Detección de drift

#### Medidas Correctivas (CORRECTIVE)
- Proceso de apelación
- Capacidad de override humano
- Rollback automático

### Algoritmo de Validación

1. Parsea medidas desde JSONB del FRIA (`friamitigationmeasures`)
2. Para cada medida:
   - Identifica tipo (PREVENTIVE, DETECTIVE, CORRECTIVE)
   - Valida según tipo usando métodos específicos
   - Verifica implementación real en el sistema
3. Calcula score: `implementedCount / totalCount`
4. Retorna resultado con issues y score

### Métodos de Verificación

Los métodos de verificación están implementados y verifican la existencia real de las medidas en el sistema:

- **`checkBiasMonitoringActive(Project project)`** - Verifica monitoreo de sesgos activo
  - Busca detecciones de sesgo recientes (últimos 30 días) en modelos del proyecto
  - Consulta `GOVBIASDETECTIONS` y `GOVBIASANALYSIS` relacionadas con modelos del proyecto

- **`checkInputValidationActive(Project project)`** - Verifica validación de inputs activa
  - Consulta metadata del proyecto para configuración de validación
  - Busca políticas activas en `GOVPOLICIES` relacionadas con validación de inputs

- **`checkMonitoringActive(Project project)`** - Verifica monitoreo continuo activo
  - Busca métricas de monitoreo recientes (últimas 24 horas) en `MONMONITORINGMETRICS` o `GOVSYSTEMMETRICS`
  - Verifica que haya actividad de monitoreo para el proyecto

- **`checkAlertsConfigured(Project project)`** - Verifica alertas configuradas
  - Consulta `GOVSYSTEMALERTS` para alertas relacionadas con el proyecto
  - Verifica metadata del proyecto para configuración de alertas

- **`checkDriftDetectionActive(Project project)`** - Verifica detección de drift activa
  - Busca detecciones de drift recientes (últimos 30 días) en `DRFDRIFTDETECTIONS` para modelos del proyecto
  - Verifica que haya actividad de detección de drift

- **`checkAppealProcessExists(Project project)`** - Verifica proceso de apelación
  - Busca procesos BPMN en `BPMMPROCES` relacionados con apelación
  - Verifica metadata del proyecto para configuración de proceso de apelación

- **`checkOverrideCapability(Project project)`** - Verifica override humano
  - Consulta metadata del proyecto para configuración de override
  - Verifica metadata de modelos del proyecto para capacidad de override

- **`checkRollbackCapability(Project project)`** - Verifica rollback
  - Verifica existencia de versiones de modelos en `MODMODELVERSIONS` (necesarias para rollback)
  - Consulta metadata del proyecto para configuración de rollback

- **`checkRobustnessValidated(Project project)`** - Verifica robustez adversarial validada
  - Busca validaciones de robustez pasadas en `MODMODELVALIDATIONS` para modelos del proyecto
  - Verifica que haya validaciones de tipo "robustness" o "adversarial" con status "PASSED"

---

## 📖 Referencias

- **Art. 27.1.f EU AI Act:** Medidas de Mitigación
- **Prompt:** INC-013
- **Entidad FRIA:** `FriaAssessment`
- **Entidad Medida:** `MitigationMeasure` (@Embeddable)

---

**Nota:** Todos los métodos de verificación están implementados y utilizan consultas SQL directas a través de `BusinessService` para verificar la existencia real de las medidas en el sistema. Las verificaciones consultan tablas de base de datos y metadata del proyecto para determinar si las medidas están realmente implementadas y activas.

---

**Última actualización:** 25 de noviembre de 2025 (Métodos auxiliares implementados)
