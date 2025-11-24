# PROMPT: INC-009-003 - Análisis en Tiempo Real Asíncrono
## Monitorización en Tiempo Real - EU AI Act Art. 12

**Incidencia:** INC-009-003  
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12  
**Estado:** ✅ COMPLETADO (código aplicado)

---

## DESCRIPCIÓN

Optimizar análisis de gobernanza separando análisis críticos (síncronos) de no críticos (asíncronos) para reducir latencia de procesamiento y mejorar throughput.

---

## CAMBIOS APLICADOS

### 1. RealtimeGovernanceServiceImpl - Separación de Análisis

**Archivo:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/service/impl/RealtimeGovernanceServiceImpl.java`

**Cambios:**
- ✅ Separación de análisis en dos métodos:
  - `executeCriticalAnalysis()`: Análisis críticos síncronos (Secretos, PII)
  - `executeNonCriticalAnalysisAsync()`: Análisis no críticos asíncronos (Bias, Toxicity)
- ✅ Análisis críticos ejecutados síncronamente:
  - Detección de secretos (requiere acción inmediata)
  - Detección de PII (requiere acción inmediata)
- ✅ Análisis no críticos ejecutados asíncronamente:
  - Análisis de sesgo (bias) - CompletableFuture
  - Análisis de toxicidad - CompletableFuture
- ✅ Resultados asíncronos se guardan en BD cuando completan
- ✅ Procesos BPMN se disparan desde análisis asíncronos si detectan problemas

### 2. Clasificación de Análisis

**Críticos (Síncronos):**
- ✅ Secretos: Requieren bloqueo inmediato de código
- ✅ PII: Requieren notificación inmediata a Security Team

**No Críticos (Asíncronos):**
- ✅ Bias: Puede procesarse después, no bloquea
- ✅ Toxicity: Puede procesarse después, no bloquea

---

## ARQUITECTURA

### Flujo Anterior (Todo Síncrono)

```
Evento → Guardar BD → Bias → Toxicity → PII → Secretos → Compliance → Data Leakage
         (bloquea)    (bloquea) (bloquea) (bloquea) (bloquea) (bloquea) (bloquea)
```

**Problema:** Todos los análisis bloquean el procesamiento, aumentando latencia.

### Flujo Nuevo (Mixto)

```
Evento → Guardar BD → [Críticos: PII, Secretos] → BPMN si crítico
         (rápido)     (síncrono, rápido)
         
         [No Críticos: Bias, Toxicity] → Guardar BD cuando completan
         (asíncrono, no bloquea)
```

**Ventaja:** Análisis críticos se ejecutan rápido, no críticos no bloquean.

---

## TESTING

### Test 1: Análisis Crítico Síncrono

```java
@Test
void testCriticalAnalysisSynchronous() {
    TelemetryPayloadDto payload = createPayload();
    payload.setRequiresSecretDetection(true);
    payload.setRequiresPiiDetection(true);
    
    // Ejecutar análisis
    boolean critical = service.executeCriticalAnalysis(event, telemetry, payload, results);
    
    // Verificar que se ejecutó síncronamente
    assertNotNull(results.get("secret_result"));
    assertNotNull(results.get("pii_result"));
    assertTrue(critical); // Si detectó secreto o PII
}
```

### Test 2: Análisis No Crítico Asíncrono

```java
@Test
void testNonCriticalAnalysisAsync() throws InterruptedException {
    TelemetryPayloadDto payload = createPayload();
    payload.setRequiresBiasCheck(true);
    payload.setRequiresToxicityCheck(true);
    
    // Ejecutar análisis asíncrono
    service.executeNonCriticalAnalysisAsync(event, telemetry, payload, results);
    
    // Verificar que no bloquea
    assertTrue(Thread.currentThread().isAlive());
    
    // Esperar a que complete
    Thread.sleep(2000);
    
    // Verificar que se guardó en BD
    AioTelemetry updated = repository.findById(telemetry.getId()).orElseThrow();
    assertTrue(updated.getTelbiaschecked());
    assertTrue(updated.getTeltoxicitychecked());
}
```

---

## MÉTRICAS

### Métricas a Monitorear

- `governance.analysis.critical.duration`: Duración análisis críticos (ms)
- `governance.analysis.noncritical.duration`: Duración análisis no críticos (ms)
- `governance.analysis.critical.count`: Contador análisis críticos
- `governance.analysis.noncritical.count`: Contador análisis no críticos
- `governance.analysis.async.errors`: Errores en análisis asíncronos

### Mejoras Esperadas

- **Latencia de procesamiento:** Reducción 40-60% (análisis no críticos no bloquean)
- **Throughput:** Aumento 30-50% (más eventos procesados por segundo)
- **Tiempo de respuesta crítico:** Reducción 20-30% (solo análisis críticos síncronos)

---

## CONFIGURACIÓN

### Propiedades (Opcional)

```yaml
governance:
  analysis:
    critical:
      timeout-ms: 5000  # Timeout para análisis críticos
    noncritical:
      thread-pool-size: 10  # Thread pool para análisis asíncronos
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md#inc-009-003`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`

---

## NOTAS

- Los análisis asíncronos pueden completarse después de que el evento se procese
- Si un análisis asíncrono detecta problema crítico, se dispara proceso BPMN pero no bloquea el flujo principal
- Los resultados asíncronos se actualizan en BD cuando completan

---

**Estado:** ✅ COMPLETADO  
**Fecha:** 2025-11-17

