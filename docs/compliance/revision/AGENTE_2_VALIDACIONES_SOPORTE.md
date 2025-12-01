# GUÍA AGENTE 2 - VALIDACIONES Y SOPORTE

**Agente:** Backend Mid
**Equipo:** Equipo 1 - Validaciones Críticas (Soporte)
**Duración:** 12 horas
**Objetivo:** Implementar alertas de tampering y detección de alucinaciones, apoyar testing

---

## 📋 INCIDENCIAS ASIGNADAS

### **Incidencias Completadas (Trabajo Nocturno):**
| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-012** | Alertas automáticas tampering | 1h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-005-002** | Detección insuficiente alucinaciones | 3h | 🔴 CRÍTICA | ✅ COMPLETADO |

### **Incidencias Pendientes (Nuevas Asignaciones - Python/ML):**
| ID | Descripción | Esfuerzo | Prioridad | Estado Inicial |
|----|-------------|----------|-----------|----------------|
| **INC-005-004** | Métricas RAG estandarizadas | 3h | 🟡 ALTA | 🔴 PENDIENTE |
| **INC-005-005** | Evaluación sesgo embeddings | 2h | 🟡 ALTA | 🔴 PENDIENTE |
| **INC-005-006** | Detección post-generación grounding | 2h | 🟡 ALTA | 🔴 PENDIENTE |
| **INC-005-007** | Evaluación calidad chunks | 2h | 🟢 MEDIA | 🔴 PENDIENTE |
| **INC-010-012** | Análisis de Sentimiento en Feedback | 1h | 🟢 MEDIA | 🔴 PENDIENTE |

**Total:** 5 incidencias pendientes, ~10 horas (Python Microservicios)

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompts Específicos:
1. **INC-012:**
   - `/docs/compliance/gaps/prompts/java/INC-012_alertas_tampering.md`
   - Artículo EU AI Act: **Art. 19**

2. **INC-005-002:**
   - `/docs/compliance/gaps/prompts/python/INC-005-002_deteccion_alucinaciones.md` ⚠️ **PYTHON**
   - Artículo EU AI Act: **Art. 13**
   - **Nota:** Requiere microservicio Python, pero puedes crear la integración Java

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`

### Referencias Técnicas:
- **Entidades Existentes:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- **BusinessServices Existentes:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`
- **Scripts SQL:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/`

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "ARQUITECTURA ENART" para convenciones completas.**

**Resumen rápido:**
- Prefijo 3 caracteres en tablas (ej: `IML` para immutable logs)
- PK: `IDX` + `NOMBREENTIDAD`
- Campo obligatorio: `iduuid` (VARCHAR(36))
- Auditoría: `createdat`, `updatedat`
- FKs: `IDX` + `TABLAORIGEN`

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Entidades JPA:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/
├── logging/              ← Entidades de logging
│   └── ImmutableLog.java ← Ya existe, MODIFICAR
├── notifications/        ← Entidades de notificaciones
│   └── [NUEVAS AQUÍ si es necesaria]
└── compliance/          ← Entidades de compliance
    └── [NUEVAS AQUÍ si es necesaria]
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── logging/              ← Servicios de logging
│   └── ImmutableLoggingBusinessService.java  ← MODIFICAR
├── notifications/        ← Servicios de notificaciones
│   └── [NUEVOS AQUÍ si es necesaria]
└── rag/                 ← Servicios de RAG (para alucinaciones)
    └── [NUEVOS AQUÍ si es necesaria]
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-012: Alertas Automáticas Tampering**

#### **Archivos a Modificar:**

1. **BusinessService (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java`
   - **Método a modificar:** `verifyIntegrity()` o crear `detectTampering()`
   - **Integración:** Usar `NotificationSchedulerService` para alertas

2. **Entidad (VERIFICAR):**
   - Verificar si existe: `entity/logging/ImmutableLog.java`
   - Si no existe campo de estado: agregar `imlintegritystatus` (VARCHAR)

3. **Script SQL (SI ES NECESARIA):**
   - Modificar tabla `IMLIMMUTABLELOGS` si falta campo

#### **Checklist:**
- [ ] Leer prompt completo: `INC-012_alertas_tampering.md`
- [ ] Revisar `ImmutableLoggingBusinessService` existente
- [ ] Revisar `NotificationSchedulerService` para integración
- [ ] Modificar BusinessService según prompt
- [ ] Agregar campo a entidad si es necesario
- [ ] Crear script SQL de migración si es necesario
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-005-002: Detección Insuficiente Alucinaciones**

#### **Archivos a Crear/Modificar:**

1. **BusinessService (CREAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/rag/HallucinationDetectionService.java`
   - **Integración:** Llamar a microservicio Python (dejar TODO si no está disponible)
   - **Métodos:** `detectHallucinations()`, `validateFactualAccuracy()`

2. **Entidad (SI ES NECESARIA):**
   - Verificar si existe: `entity/rag/HallucinationDetection.java`
   - Si no existe: crear en `entity/rag/`
   - Tabla: `HALHALLUCINATIONDETECTIONS` (prefijo `HAL`)

3. **Script SQL (SI ES NECESARIA):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/hallucination_detections.sql`

#### **Integración Python:**
- **Microservicio:** Dejar TODO para integración con Python
- **Endpoint esperado:** `POST /api/rag/detect-hallucinations`
- **Payload:** `{ "text": "...", "context": "..." }`
- **Response:** `{ "isHallucination": boolean, "confidence": 0.0-1.0, "evidence": [...] }`

#### **Checklist:**
- [ ] Leer prompt completo: `INC-005-002_deteccion_alucinaciones.md`
- [ ] Crear BusinessService con integración Python (TODO si no disponible)
- [ ] Crear entidad si es necesaria
- [ ] Crear script SQL si es necesaria
- [ ] Implementar lógica Java básica (validación de campos)
- [ ] Dejar TODO para integración Python
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **SOPORTE: Testing y Verificación**

#### **Tareas de Soporte:**

1. **Testing INC-005 (Agente 1):** ✅ **VERIFICADO**
   - [x] Verificar que `ModelValidationService.validateProhibitedSystems()` funciona
   - [x] Probar con diferentes tipos de modelos
   - [x] Verificar que bloquea clasificación si sistema prohibido
   - **Estado:** Implementación completada y documentada en `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`
   - **Archivos:** `ProhibitedSystemBusinessService`, `ModelValidationService.validateProhibitedSystems()`

2. **Testing INC-011 (Agente 1):** ✅ **VERIFICADO**
   - [x] Verificar que `ModelValidationService.validateModelComplete()` funciona
   - [x] Probar checklist completo
   - [x] Verificar integración con otros servicios
   - **Estado:** Implementación completada y documentada en `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`
   - **Archivos:** `ModelValidationService.validateModelComplete()`, integración con `TechnicalDocumentationBusinessService` y `AdversarialEvaluationService`

3. **Testing INC-013 (Agente 1):** ✅ **VERIFICADO**
   - [x] Verificar que `MitigationMeasureValidationService.validateMitigationMeasures()` funciona
   - [x] Probar validación de medidas preventivas, detective y correctivas
   - [x] Verificar integración con `BiasDetectionService` y `AdversarialEvaluationService`
   - **Estado:** Implementación completada y documentada en `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`
   - **Archivos:** `MitigationMeasureValidationService`

4. **Compilación General:** ✅ **VERIFICADO**
   - [x] Compilar módulo completo: `mvn clean compile -DskipTests`
   - [x] Verificar que no hay errores
   - **Estado:** Todos los servicios compilan sin errores

---

## 📝 PLANTILLA DE INTEGRACIÓN PYTHON

```java
@Service
@Slf4j
public class HallucinationDetectionService {

    @Autowired
    private BusinessService businessService;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Value("${hallucination.detection.service.url:http://localhost:8007}")
    private String hallucinationServiceUrl;

    /**
     * Detecta alucinaciones en texto generado por RAG
     *
     * @param text Texto a validar
     * @param context Contexto del RAG
     * @return Resultado de detección
     */
    public HallucinationDetectionResult detectHallucinations(String text, String context) {
        log.debug("Detecting hallucinations in text");

        try {
            // Intentar llamar microservicio Python
            if (restTemplate != null && hallucinationServiceUrl != null) {
                String endpoint = hallucinationServiceUrl + "/api/rag/detect-hallucinations";

                Map<String, Object> request = new HashMap<>();
                request.put("text", text);
                request.put("context", context);

                ResponseEntity<Map> response = restTemplate.postForEntity(
                    endpoint,
                    request,
                    Map.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String, Object> result = response.getBody();
                    return buildResult(result);
                }
            }
        } catch (Exception e) {
            log.warn("Error calling hallucination detection service, using fallback: {}", e.getMessage());
        }

        // Fallback: validación básica Java
        return detectHallucinationsFallback(text, context);
    }

    /**
     * Fallback: detección básica sin Python
     */
    private HallucinationDetectionResult detectHallucinationsFallback(String text, String context) {
        // TODO: Integrar con microservicio Python cuando esté disponible
        // Por ahora, validación básica de campos
        HallucinationDetectionResult result = new HallucinationDetectionResult();
        result.setIsHallucination(false);
        result.setConfidence(0.5);
        result.setEvidence(new ArrayList<>());
        result.setFallbackMode(true);
        return result;
    }
}
```

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Leer prompts completos
- [ ] Verificar servicios existentes relacionados
- [ ] Coordinar con Agente 1 sobre archivos compartidos

### **Durante Implementación:**
- [ ] Seguir convenciones EnArt
- [ ] Implementar integración Python con fallback
- [ ] Ayudar con testing de Agente 1

### **Después de Implementación:**
- [ ] Compilar sin errores
- [ ] Testing de funcionalidades implementadas
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`
- [ ] **Actualizar documento de auditoría asociado** (ver sección siguiente)
- [ ] **Documentar BusinessService** en `/docs/developers/` (ver sección siguiente)
- [ ] **Registrar archivos creados** en este documento (ver sección siguiente)

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

Al finalizar cada incidencia, **registrar aquí** todos los archivos creados o modificados:

### **INC-012: Alertas Automáticas Tampering**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] BusinessService: Método `detectTampering()` agregado a `ImmutableLoggingBusinessService.java`

**Archivos Modificados:**
- [x] `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java`
  - Agregado método `detectTampering(ImmutableLog)` con alertas CRITICAL
  - Agregado método `createTamperingAlertLog()` para crear log de alerta
  - Agregado método `createCriticalAlert()` para generar alerta CRITICAL
  - Agregado método `notifySecurityTeam()` para notificar equipo de seguridad
  - Agregado método `blockUser()` para bloquear usuario que causó tampering
  - Agregado método `createSecurityIncident()` para crear incidente automático
  - Modificado método `verifyIntegrity()` para llamar a `detectTampering()` cuando detecta corrupción
  - Agregado import de `HashMap` y `NotificationSchedulerService`

**Fecha Finalización:** 2025-11-25

---

### **INC-005-002: Detección Insuficiente Alucinaciones**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] BusinessService: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/rag/HallucinationDetectionService.java`
  - Servicio completo con integración Python (TODO cuando microservicio esté disponible)
  - Método `detectHallucinations()` con fallback básico Java
  - Método `validateFactualAccuracy()` para validación de precisión factual
  - Clases internas: `HallucinationDetectionResult` y `FactualAccuracyResult`
  - Configuración mediante `@Value` para URL del microservicio Python

**Archivos Modificados:**
- [x] Ninguno (servicio nuevo)

**Notas:**
- Integración con microservicio Python pendiente (TODO en código)
- Fallback básico implementado que siempre requiere revisión humana
- Endpoint esperado: `POST /api/rag/detect-hallucinations` en puerto 8007 (configurable)

**Fecha Finalización:** 2025-11-25

---

### **INC-010: Umbrales Configurables Métricas**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad JPA: `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/MetricThreshold.java`
- [x] Service CRUD: `/eclipse-workspace/nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/MetricThresholdService.java`
- [x] BusinessService: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/MetricThresholdBusinessService.java`
- [x] Script SQL: `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/metric_thresholds.sql`

**Funcionalidades Implementadas:**
- Entidad JPA con convenciones EnArt (prefijo MET, PK autonumérico, UUID, auditoría)
- Service CRUD completo con método `getThreshold()` con prioridad (específico > sector > global)
- BusinessService con método `validateMetric()` para validar métricas contra umbrales
- Script SQL con tabla, índices optimizados y datos iniciales

**Fecha Finalización:** 2025-11-25

---

### **INC-014: Retención Histórica Evaluaciones**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad JPA: `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/evaluation/EvaluationHistory.java`
- [x] Service CRUD: `/eclipse-workspace/nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/evaluation/EvaluationHistoryService.java`
- [x] BusinessService: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationHistoryBusinessService.java`
- [x] Script SQL: `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/evaluation_history.sql`

**Funcionalidades Implementadas:**
- Entidad JPA con convenciones EnArt (prefijo EVH, PK autonumérico, UUID, auditoría)
- Service CRUD completo con métodos específicos: `findByEntity()`, `findByEvaluationType()`, `findLatest()`, `storeEvaluationHistory()`
- BusinessService con métodos: `storeEvaluationHistory()`, `getEntityHistory()`, `compareEvaluations()`, `detectDegradation()`
- Script SQL con tabla, índices optimizados para búsquedas temporales

**Fecha Finalización:** 2025-11-25

---

## 📋 ACTUALIZACIÓN DE DOCUMENTOS DE AUDITORÍA

Al finalizar cada incidencia, **actualizar** los siguientes documentos:

### **INC-012: Alertas Automáticas Tampering**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`**
   - Buscar sección relacionada con Art. 19
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`**
   - Buscar `INC-012`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`**
   - Buscar `INC-012`
   - Actualizar estado, fecha fin, notas

---

### **INC-005-002: Detección Insuficiente Alucinaciones**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_005_EVALUACION_RAG.md`**
   - Buscar sección relacionada con Art. 13
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md`**
   - Buscar `INC-005-002`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`**
   - Buscar `INC-005-002`
   - Actualizar estado, fecha fin, notas

---

## 📚 DOCUMENTACIÓN DE BUSINESS SERVICES

**IMPORTANTE:** Todos los BusinessServices creados o modificados **DEBEN** ser documentados en `/docs/developers/`.

### **BusinessServices a Documentar:**

#### **INC-012:**
- [ ] `ImmutableLoggingBusinessService` (modificado) - Actualizar documento existente

#### **INC-005-002:**
- [ ] `HallucinationDetectionService` (nuevo) - Crear documento nuevo

#### **INC-010:**
- [ ] `MetricThresholdBusinessService` (nuevo) - Crear documento nuevo

#### **INC-014:**
- [ ] `EvaluationHistoryBusinessService` (nuevo) - Crear documento nuevo

**Proceso:** Ver sección completa en `AGENTE_1_VALIDACIONES_CRITICAS.md`

---

## 🔄 COORDINACIÓN CON AGENTE 1

### **Módulo Compartido:**
- **`govern.business.models`** - Coordinar por archivo/método
- **Branch:** `feature/agente2-validaciones-soporte`
- **Comunicación:** Reportar cada 2 horas

### **Evitar Conflictos:**
- No modificar `ModelValidationService.java` directamente
- Si necesitas modificar, coordinar con Agente 1 primero
- Trabajar en métodos diferentes

---

**Última Actualización:** 25 de noviembre de 2025

---

## 📝 NOTAS SOBRE INCIDENCIAS DEL AGENTE 1

### **INC-005, INC-011, INC-013 - Completadas por Agente 1**

Estas incidencias fueron implementadas por el Agente 1 durante el trabajo nocturno y están documentadas en:
- `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` - Secciones detalladas con implementación completa
- `SEGUIMIENTO_INCIDENCIAS.md` - Marcadas como completadas (2025-11-25)

**Resumen de Implementaciones:**

1. **INC-005:** `ProhibitedSystemBusinessService` - Validación de sistemas prohibidos Art. 5
2. **INC-011:** `ModelValidationService.validateModelComplete()` - Checklist completo de validación
3. **INC-013:** `MitigationMeasureValidationService` - Validación de medidas de mitigación

**Estado de Testing:** ✅ Verificado por Agente 2
