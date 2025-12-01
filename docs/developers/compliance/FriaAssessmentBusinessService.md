# FriaAssessmentBusinessService

**Ubicación:** `com.codeflowx.govern.business.compliance.FriaAssessmentBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

El `FriaAssessmentBusinessService` gestiona el ciclo completo de **Fundamental Rights Impact Assessments (FRIA)** según el **Art. 27 del EU AI Act**. Las FRIA son evaluaciones obligatorias de impacto en derechos fundamentales que deben realizarse para todos los sistemas de IA de alto riesgo antes de su despliegue.

### Propósito

Este servicio permite:
- Crear y gestionar evaluaciones FRIA completas
- Validar que se cumplan los 6 elementos mandatorios del Art. 27.1
- Integrar con DPIA (Data Protection Impact Assessment)
- Notificar a autoridades competentes cuando la FRIA está completa
- Aprobar evaluaciones FRIA para su uso

---

## 🎯 Responsabilidades

### ✅ Qué Hace Este Servicio

1. **Gestión del Ciclo de Vida de FRIA**
   - Crear nuevas evaluaciones FRIA asociadas a proyectos
   - Actualizar secciones específicas de la evaluación
   - Calcular score de completitud automáticamente
   - Aprobar evaluaciones completas

2. **Validación de Completitud**
   - Verificar los 6 elementos mandatorios del Art. 27.1
   - Calcular score de completitud (0.00 - 1.00)
   - Validar que la FRIA esté completa antes de notificar/aprobar

3. **Integración con Sistemas Externos**
   - Integrar con DPIA según Art. 27.4
   - Notificar a autoridades competentes según Art. 27.3
   - Validar consistencia con métricas técnicas reales (INC-007) mediante `FriaValidationService`

4. **Consultas y Búsquedas**
   - Obtener todas las FRIA de un proyecto
   - Obtener la última FRIA de un proyecto

### ❌ Qué NO Hace Este Servicio

- **NO** gestiona el contenido específico de cada sección (eso lo hace el ViewModel)
- **NO** valida la calidad del contenido (solo verifica que exista)
- **NO** envía notificaciones reales a autoridades (solo marca como notificada)
- **NO** gestiona workflows de aprobación (eso lo hace BPMN)
- **NO** obtiene métricas técnicas directamente (lo hace `FriaValidationService` vía microservicio Python)

---

## 🔗 Dependencias

### Entidades

```java
import com.codeflowx.govern.entity.compliance.FriaAssessment;
import com.codeflowx.govern.entity.projects.Project;
import com.codeflowx.govern.entity.core.User;
```

### Servicios

```java
@Autowired
private BusinessService businessService; // DAO de EnArt Framework

@Autowired(required = false)
private FriaValidationService friaValidationService; // Validación cruzada INC-007
```

### Librerías

- `org.springframework.stereotype.Service`
- `org.enartframework.orm.exception.DaoException`
- `com.codeflowx.govern.business.exception.BussinessException`
- `codeflowx.nocode.persist.BusinessService`
- `lombok.extern.slf4j.Slf4j`

---

## 🏗️ Arquitectura

### Integración en el Sistema

```
┌─────────────────────────────────┐
│   FriaWizardViewModel (ZKoss)   │
│   - UI del wizard de FRIA       │
└──────────────┬──────────────────┘
               │ @WireVariable
               ▼
┌─────────────────────────────────┐
│  FriaAssessmentBusinessService │
│  - Lógica de negocio            │
└──────────────┬──────────────────┘
               │ BusinessService (DAO)
               ▼
┌─────────────────────────────────┐
│   FRIAFUNDAMENTALRIGHTSASSESSMENTS│
│   (Tabla PostgreSQL)            │
└─────────────────────────────────┘
```

### Flujo de Datos

1. **Creación:** ViewModel → BusinessService → Base de Datos
2. **Actualización:** ViewModel → BusinessService → Base de Datos
3. **Validación:** BusinessService calcula score automáticamente
4. **Aprobación:** ViewModel → BusinessService → Base de Datos

---

## 📚 API Pública

### Métodos Principales

#### 1. `createFria(Long projectId, Long deployerUserId)`

Crea una nueva evaluación FRIA asociada a un proyecto.

**Parámetros:**
- `projectId` (Long, requerido): ID del proyecto
- `deployerUserId` (Long, opcional): ID del usuario que despliega el sistema

**Retorna:** `FriaAssessment` - La FRIA creada con ID generado

**Excepciones:**
- `BussinessException` si el proyecto no existe

**Ejemplo:**
```java
@WireVariable
private FriaAssessmentBusinessService friaService;

FriaAssessment fria = friaService.createFria(projectId, deployerUserId);
Long friaId = fria.getIdxfriaassessment();
```

---

#### 2. `updateFriaSection(Long friaId, String section, Object data)`

Actualiza una sección específica de la FRIA.

**Parámetros:**
- `friaId` (Long, requerido): ID de la FRIA
- `section` (String, requerido): Nombre de la sección a actualizar
  - Valores válidos: `"PROCESS_DESCRIPTION"`, `"USAGE_PERIOD"`, `"USAGE_FREQUENCY"`, etc.
- `data` (Object, requerido): Datos a guardar en la sección

**Excepciones:**
- `BussinessException` si la FRIA no existe

**Ejemplo:**
```java
friaService.updateFriaSection(friaId, "PROCESS_DESCRIPTION",
    "Sistema de IA para detección de fraude en transacciones bancarias");
```

**Nota:** Este método solo actualiza campos específicos. Para actualizar múltiples secciones, llamar múltiples veces o implementar un método batch.

**Validación Automática (INC-007):**
Cuando se actualizan las secciones `"RISKS"` o `"MITIGATION_MEASURES"`, el servicio ejecuta automáticamente una validación cruzada con métricas técnicas reales. Si se detectan inconsistencias (score < 0.70), se registra un warning en los logs pero no se bloquea el flujo.

---

#### 3. `calculateCompletenessScore(Long friaId)`

Calcula el score de completitud basado en los 6 elementos mandatorios del Art. 27.1.

**Parámetros:**
- `friaId` (Long, requerido): ID de la FRIA

**Retorna:** `BigDecimal` - Score de 0.00 a 1.00

**Lógica de Cálculo:**
```
score = elementos completados / 6 elementos totales
```

**Elementos Verificados:**
1. Art. 27.1.a - Descripción del proceso (`friaprocessdescription`)
2. Art. 27.1.b - Período y frecuencia (`friausageperiod`, `friausagefrequency`)
3. Art. 27.1.c - Categorías afectadas (`friaaffectedcategories`)
4. Art. 27.1.d - Riesgos (`friarisks`)
5. Art. 27.1.e - Supervisión humana (`friahumanoversight`)
6. Art. 27.1.f - Medidas de mitigación (`friamitigationmeasures`)

**Ejemplo:**
```java
BigDecimal completeness = friaService.calculateCompletenessScore(friaId);
if (completeness.compareTo(new BigDecimal("1.00")) == 0) {
    // FRIA completa
    friaService.approveFria(friaId, approverUserId);
}
```

---

#### 4. `submitToAuthority(Long friaId)`

Envía notificación a la autoridad competente según Art. 27.3.

**Parámetros:**
- `friaId` (Long, requerido): ID de la FRIA

**Excepciones:**
- `BussinessException` si la FRIA no existe o no está completa

**Validaciones:**
- Verifica que `completenessScore == 1.00` antes de notificar

**Ejemplo:**
```java
try {
    friaService.submitToAuthority(friaId);
    log.info("FRIA {} submitted to authority", friaId);
} catch (BussinessException e) {
    log.warn("FRIA not complete: {}", e.getMessage());
}
```

**Nota:** Actualmente solo marca como notificada. La integración real con APIs de autoridades se implementará cuando esté disponible.

---

#### 5. `integrateWithDpia(Long friaId, String dpiaId)`

Integra la FRIA con un DPIA (Data Protection Impact Assessment) según Art. 27.4.

**Parámetros:**
- `friaId` (Long, requerido): ID de la FRIA
- `dpiaId` (String, requerido): ID del DPIA

**Ejemplo:**
```java
friaService.integrateWithDpia(friaId, "DPIA-2025-001");
```

---

#### 6. `approveFria(Long friaId, Long approverUserId)`

Aprueba una FRIA completa.

**Parámetros:**
- `friaId` (Long, requerido): ID de la FRIA
- `approverUserId` (Long, requerido): ID del usuario que aprueba

**Ejemplo:**
```java
friaService.approveFria(friaId, approverUserId);
```

---

#### 7. `getFriasByProject(Long projectId)`

Obtiene todas las FRIA de un proyecto ordenadas por fecha de creación descendente.

**Parámetros:**
- `projectId` (Long, requerido): ID del proyecto

**Retorna:** `List<FriaAssessment>` - Lista de FRIA del proyecto

**Ejemplo:**
```java
List<FriaAssessment> frias = friaService.getFriasByProject(projectId);
for (FriaAssessment fria : frias) {
    log.info("FRIA {} - Completeness: {}",
        fria.getIdxfriaassessment(),
        fria.getFriacompletenesscore());
}
```

---

#### 8. `getLatestFria(Long projectId)`

Obtiene la última FRIA creada para un proyecto.

**Parámetros:**
- `projectId` (Long, requerido): ID del proyecto

**Retorna:** `FriaAssessment` o `null` si no hay FRIA

**Ejemplo:**
```java
FriaAssessment latestFria = friaService.getLatestFria(projectId);
if (latestFria != null && latestFria.getFriaapproved()) {
    log.info("Project has approved FRIA");
}
```

---

#### 9. `validateFriaAgainstMetrics(Long friaId)` ⭐ NUEVO (INC-007)

Valida consistencia entre FRIA documental y métricas técnicas reales (INC-007).

**Parámetros:**
- `friaId` (Long, requerido): ID del FRIA a validar

**Retorna:** `CrossValidationResult` con:
- `consistencyScore` (Double): Score de consistencia (0.0 - 1.0)
- `isConsistent` (Boolean): true si score >= 0.70
- `requiresJustification` (Boolean): true si score < 0.70
- `inconsistencies` (List<InconsistencyDetail>): Lista de inconsistencias detectadas
- `recommendations` (List<String>): Recomendaciones automáticas

**Excepciones:**
- `BussinessException` si el FRIA no existe o `FriaValidationService` no está disponible

**Ejemplo:**
```java
try {
    CrossValidationResult result = friaService.validateFriaAgainstMetrics(friaId);

    if (result.getRequiresJustification()) {
        log.warn("FRIA {} requires justification - Score: {}",
                 friaId, result.getConsistencyScore());

        for (InconsistencyDetail inconsistency : result.getInconsistencies()) {
            log.warn("Inconsistency: {} - Severity: {}",
                     inconsistency.getDescription(),
                     inconsistency.getSeverity());
        }
    } else {
        log.info("FRIA {} is consistent - Score: {}",
                 friaId, result.getConsistencyScore());
    }
} catch (BussinessException e) {
    log.error("Error validating FRIA: {}", e.getMessage());
}
```

**Nota:** Este método llama internamente a `FriaValidationService.validateAgainstMetrics()` que se integra con el microservicio Python `leka-fria-generator` (puerto 8012).

---

#### 10. `validateFriaAgainstMetrics(Long friaId, Long modelId, Long datasetId)` ⭐ NUEVO (INC-007)

Valida consistencia FRIA con IDs específicos de modelo y dataset.

**Parámetros:**
- `friaId` (Long, requerido): ID del FRIA a validar
- `modelId` (Long, opcional): ID del modelo (puede ser null)
- `datasetId` (Long, opcional): ID del dataset (puede ser null)

**Retorna:** `CrossValidationResult` con score de consistencia e inconsistencias

**Ejemplo:**
```java
CrossValidationResult result = friaService.validateFriaAgainstMetrics(
    friaId,
    modelId,  // Opcional
    datasetId // Opcional
);
```

**Nota:** Útil cuando el FRIA no tiene estos IDs asociados directamente pero se conocen desde el contexto.

---

## 💡 Casos de Uso

### Caso 1: Crear y Completar una FRIA

```java
@WireVariable
private FriaAssessmentBusinessService friaService;

// 1. Crear nueva FRIA
FriaAssessment fria = friaService.createFria(projectId, deployerUserId);
Long friaId = fria.getIdxfriaassessment();

// 2. Completar secciones
friaService.updateFriaSection(friaId, "PROCESS_DESCRIPTION",
    "Sistema de IA para análisis de CVs en procesos de selección");

friaService.updateFriaSection(friaId, "USAGE_PERIOD", "INDEFINITE");
friaService.updateFriaSection(friaId, "USAGE_FREQUENCY", "DAILY");

// 3. Calcular completitud
BigDecimal completeness = friaService.calculateCompletenessScore(friaId);
log.info("FRIA completeness: {}", completeness);

// 4. Si está completa, aprobar y notificar
if (completeness.compareTo(new BigDecimal("1.00")) == 0) {
    friaService.approveFria(friaId, approverUserId);
    friaService.submitToAuthority(friaId);
}
```

### Caso 2: Verificar Estado de FRIA en Proyecto

```java
FriaAssessment latestFria = friaService.getLatestFria(projectId);

if (latestFria == null) {
    // No hay FRIA, crear una nueva
    friaService.createFria(projectId, currentUserId);
} else if (!latestFria.getFriaapproved()) {
    // Hay FRIA pero no está aprobada, verificar completitud
    BigDecimal completeness = friaService.calculateCompletenessScore(
        latestFria.getIdxfriaassessment());

    if (completeness.compareTo(new BigDecimal("1.00")) < 0) {
        // Mostrar elementos faltantes en UI
        showMissingElements(latestFria, completeness);
    }
}
```

### Caso 3: Integrar con DPIA

```java
// Cuando se crea un DPIA para el mismo proyecto
String dpiaId = createDpia(projectId);

// Integrar con FRIA existente
FriaAssessment fria = friaService.getLatestFria(projectId);
if (fria != null) {
    friaService.integrateWithDpia(fria.getIdxfriaassessment(), dpiaId);
}
```

---

## ⚙️ Configuración

Este servicio no requiere configuración adicional. Usa la configuración estándar de:
- Spring Framework (inyección de dependencias)
- EnArt Framework (acceso a datos)
- Base de datos PostgreSQL

---

## 🧪 Testing

### Estrategia de Testing

1. **Unit Tests:** Probar cada método individualmente con mocks
2. **Integration Tests:** Probar flujos completos con base de datos de test
3. **Validation Tests:** Verificar que los 6 elementos se validan correctamente

### Ejemplo de Test Unitario

```java
@Test
public void testCalculateCompletenessScore() throws BussinessException {
    // Arrange
    FriaAssessment fria = createFriaWithPartialData();

    // Act
    BigDecimal score = friaService.calculateCompletenessScore(fria.getIdxfriaassessment());

    // Assert
    assertTrue(score.compareTo(BigDecimal.ZERO) > 0);
    assertTrue(score.compareTo(new BigDecimal("1.00")) <= 0);
    assertEquals(3, score.multiply(new BigDecimal("6")).intValue()); // 3/6 = 0.50
}
```

### Ejemplo de Test de Integración

```java
@Test
@Transactional
public void testCompleteFriaWorkflow() throws BussinessException {
    // 1. Crear FRIA
    FriaAssessment fria = friaService.createFria(projectId, userId);
    assertNotNull(fria);

    // 2. Completar todas las secciones
    friaService.updateFriaSection(fria.getIdxfriaassessment(), "PROCESS_DESCRIPTION", "Test");
    // ... completar todas las secciones

    // 3. Verificar completitud
    BigDecimal completeness = friaService.calculateCompletenessScore(fria.getIdxfriaassessment());
    assertEquals(new BigDecimal("1.00"), completeness);

    // 4. Aprobar
    friaService.approveFria(fria.getIdxfriaassessment(), approverUserId);

    // 5. Verificar estado
    FriaAssessment updated = friaService.getLatestFria(projectId);
    assertTrue(updated.getFriaapproved());
}
```

---

## 🔄 Evolución y Extensión

### Cómo Agregar Nuevas Secciones

1. Agregar campo en entidad `FriaAssessment`
2. Agregar caso en `updateFriaSection()`:
```java
case "NEW_SECTION":
    fria.setFrianewsection((String) data);
    break;
```
3. Actualizar `calculateCompletenessScore()` si la sección es mandatoria

### Cómo Integrar con APIs de Autoridades

1. Crear servicio `AuthorityNotificationService` (ya existe)
2. Modificar `submitToAuthority()` para llamar al servicio:
```java
@Autowired
private AuthorityNotificationService authorityNotificationService;

public void submitToAuthority(Long friaId) throws BussinessException {
    // ... validaciones existentes ...

    // Integración real
    Map<String, Object> response = authorityNotificationService.notifyAuthority(
        friaId, "FRIA", friaData);

    fria.setFrianotificationid(response.get("authorityId").toString());
    // ...
}
```

### Mejoras Futuras

1. **Validación de Calidad:** No solo verificar existencia, sino calidad del contenido
2. **Workflow de Aprobación:** Integrar con BPMN para workflows complejos
3. **Versionado:** Permitir versiones de FRIA cuando se modifica el sistema
4. **Templates:** Plantillas predefinidas para diferentes tipos de sistemas

---

## 📖 Referencias

### Prompts Asociados

- **INC-008:** FRIA Assessment BusinessService
- **INC-013:** Validación FRIA Completa
- **INC-012-003:** Validación FRIA Alto Riesgo

### Artículos EU AI Act

- **Art. 27:** Fundamental Rights Impact Assessment
  - https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32021R0106#d1e1830-27-1

### ViewModels Relacionados

- `FriaWizardViewModel` - Wizard completo de creación/edición
- `CompleteMissingElementsViewModel` - Completar elementos faltantes
- `DefineModificationsViewModel` - Definir modificaciones
- `EnhancedReviewViewModel` - Revisión mejorada

### Documentación Relacionada

- `/docs/compliance/revision/MAPEO_PROMPTS_VIEWMODELS_GOVERNANCE_CORREGIDO.md`
- `/docs/compliance/revision/PANTALLAS_DEMO_GOVERNANCE.md`

---

## 🐛 Troubleshooting

### Problema: Score de completitud no se actualiza

**Causa:** No se llama `calculateCompletenessScore()` después de actualizar secciones.

**Solución:** Llamar `calculateCompletenessScore()` después de cada `updateFriaSection()` o implementar actualización automática.

### Problema: No se puede notificar a autoridad

**Causa:** FRIA no está completa (completenessScore < 1.00).

**Solución:** Verificar que todos los 6 elementos mandatorios estén completos.

### Problema: Error al crear FRIA

**Causa:** Proyecto no existe o no se puede acceder a la base de datos.

**Solución:** Verificar que el `projectId` existe y que la conexión a BD está activa.

---

**Última actualización:** 25 de noviembre de 2025
**Mantenido por:** CodeflowX Development Team
