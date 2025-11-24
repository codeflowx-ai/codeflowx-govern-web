# INCIDENCIAS Y RECOMENDACIONES - LOGS INMUTABLES Y TRAZABILIDAD
## EU AI Act Art. 19 - Registro Inmutable

**Fecha:** 2025-11-17  
**Referencia:** AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md  
**Prioridad:** 🔴 CRÍTICA / 🟡 MEDIA / 🟢 BAJA

---

## 🔴 INCIDENCIAS CRÍTICAS

### INC-008-001: Trazabilidad Completa Modelo-Dataset-Output No Integrada
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Alto - No se puede demostrar trazabilidad end-to-end para auditorías  
**Artículo EU AI Act:** Art. 19.1 (registros completos)

**Descripción:**
El sistema tiene componentes separados para linaje de modelos (`TRNEXPERIMENTLINEAGE`), predicciones (`srv_prediction`), y logs inmutables (`IMLIMMUTABLELOGS`), pero **no existe una vista unificada** que muestre la traza completa desde modelo → dataset → entrenamiento → evaluación → despliegue → predicción → output.

**Evidencia:**
- ✅ Existe `ExperimentLineage` con provenance graph
- ✅ Existe `ModelPrediction` con inputRef/outputRef
- ✅ Existe `ImmutableLog` con eventos críticos
- ❌ **FALTA:** Integración entre estos componentes

**Recomendación:**
1. **Crear vista SQL unificada** `v_complete_model_trace` que consolide:
   - Logs ImmutableLogs con `IMLENTITYTYPE = 'MODEL'` y `IMLACTION = 'TRAIN'`
   - Logs ImmutableLogs con `IMLENTITYTYPE = 'DATASET'` y `IMLACTION = 'USE'`
   - Logs ImmutableLogs con `IMLENTITYTYPE = 'PREDICTION'` y `IMLACTION = 'PREDICT'`
   - Unir mediante `IMLENTITYID` y referencias en `IMLDATA` (JSON)

2. **Implementar endpoint REST:**
   ```
   GET /api/v1/compliance/trace/model/{modelId}
   Response: {
     "model_id": "...",
     "datasets": [...],
     "training_events": [...],
     "evaluation_events": [...],
     "deployment_events": [...],
     "predictions": [...],
     "outputs": [...]
   }
   ```

3. **Crear ViewModel ZK** para visualización en UI:
   - Timeline interactivo mostrando traza completa
   - Filtros por fecha, acción, entidad
   - Exportación a PDF/CSV

**Esfuerzo Estimado:** 3 días  
**Archivos Afectados:**
- `sql-scripts/views/v_complete_model_trace.sql` (nuevo)
- `src/main/java/com/codeflowx/govern/business/trace/ModelTraceService.java` (nuevo)
- `src/main/java/com/codeflowx/govern/controller/TraceController.java` (nuevo)
- `src/main/java/com/codeflowx/govern/viewmodel/trace/ModelTraceViewModel.java` (nuevo)

---

### INC-008-002: Exportación para Auditores Externos Incompleta
**Prioridad:** 🔴 CRÍTICA  
**Impacto:** Alto - No se puede proporcionar evidencia a autoridades competentes  
**Artículo EU AI Act:** Art. 19.2 (accesibilidad para autoridades)

**Descripción:**
Existe servicio `AIActLogExportService`, pero:
- ❌ No hay endpoint REST expuesto
- ❌ No hay autenticación/autorización específica para auditores
- ❌ No hay formato estándar EU AI Act para exportación
- ❌ No hay documentación de uso

**Evidencia:**
```java
// Servicio existe pero no está expuesto
public File exportLogsForAudit(...) {
    // Implementación parcial
}
```

**Recomendación:**
1. **Implementar endpoint REST:**
   ```
   GET /api/v1/compliance/audit/export
   Query Params:
     - startDate (ISO 8601)
     - endDate (ISO 8601)
     - entityType (optional)
     - entityId (optional)
     - format (JSON|CSV|XML)
   Headers:
     - Authorization: Bearer <auditor_api_key>
   Response: File download
   ```

2. **Autenticación/Autorización:**
   - Crear rol `AUDITOR` en sistema de roles
   - API Keys específicas para auditores externos
   - Rate limiting: 10 exportaciones/día por auditor
   - Logging de accesos de auditores

3. **Formato EU AI Act Compliant:**
   ```json
   {
     "export_metadata": {
       "export_date": "2025-11-17T10:00:00Z",
       "exporter": "CodeflowX Govern Platform v1.0",
       "compliance_standard": "EU AI Act Art. 19",
       "period_covered": {...},
       "exporter_signature": "SHA256(...)" // Opcional
     },
     "hash_chain_verification": {
       "genesis_hash": "GENESIS_BLOCK_CODEFLOWX_GOVERN",
       "last_hash": "...",
       "total_logs": 1000,
       "integrity_status": "VALID",
       "verification_timestamp": "2025-11-17T10:00:00Z"
     },
     "logs": [...]
   }
   ```

4. **Documentación:**
   - Crear `docs/compliance/AUDITOR_EXPORT_GUIDE.md`
   - Incluir ejemplos de uso
   - Especificar formato de exportación

**Esfuerzo Estimado:** 2 días  
**Archivos Afectados:**
- `src/main/java/com/codeflowx/govern/controller/AuditExportController.java` (nuevo)
- `src/main/java/com/codeflowx/govern/business/logging/AIActLogExportService.java` (completar)
- `src/main/java/com/codeflowx/govern/security/AuditorAuthenticationFilter.java` (nuevo)
- `docs/compliance/AUDITOR_EXPORT_GUIDE.md` (nuevo)

---

## 🟡 INCIDENCIAS MEDIAS

### INC-008-003: Documentación Formal de Cumplimiento Art. 19 Ausente
**Prioridad:** 🟡 MEDIA  
**Impacto:** Medio - Dificulta demostración a autoridades  
**Artículo EU AI Act:** Art. 19 (obligación de registro inmutable)

**Descripción:**
No existe documento formal que demuestre cumplimiento Art. 19 para autoridades competentes. El sistema cumple técnicamente, pero falta documentación ejecutiva.

**Recomendación:**
1. **Crear documento:** `docs/compliance/EU_AI_ACT_ART19_COMPLIANCE_DECLARATION.md`
   - Descripción técnica del sistema de logs inmutables
   - Evidencias de inmutabilidad (triggers, hash chains)
   - Procedimientos de verificación de integridad
   - Políticas de retención (10 años)
   - Capacidades de exportación para auditores
   - Diagramas de arquitectura

2. **Incluir evidencia técnica:**
   - Scripts SQL de verificación de triggers
   - Ejemplos de hash chain verification
   - Políticas de retención TimescaleDB
   - Procedimientos de exportación

**Esfuerzo Estimado:** 1 día  
**Archivos Afectados:**
- `docs/compliance/EU_AI_ACT_ART19_COMPLIANCE_DECLARATION.md` (nuevo)

---

### INC-008-004: Falta Alerta Automática ante Detección de Manipulación
**Prioridad:** 🟡 MEDIA  
**Impacto:** Medio - No se notifica automáticamente cuando se detecta tampering  
**Artículo EU AI Act:** Art. 19.1 (inalterabilidad)

**Descripción:**
El sistema detecta manipulación mediante `verifyIntegrity()`, pero **no notifica automáticamente** a administradores cuando se detecta `TAMPERED` o `CHAIN_BROKEN`.

**Evidencia:**
```java
// Detecta pero no notifica
if (!calculatedHash.equals(log.getImlcurrenthash())) {
    report.setIntegrityValid(false);
    report.addCorruptedLog(...);
    log.error("Hash mismatch detected..."); // Solo log, no notificación
}
```

**Recomendación:**
1. **Implementar notificación automática:**
   - Email a administradores cuando `verifyIntegrity()` detecta `TAMPERED`
   - Alertas en dashboard de administración
   - Integración con sistema de alertas (PagerDuty, Slack, etc.)

2. **Programar verificación periódica:**
   - Job scheduler que ejecute `verifyIntegrity()` diariamente
   - Alertar si detecta problemas

**Esfuerzo Estimado:** 1 día  
**Archivos Afectados:**
- `src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java` (modificar)
- `src/main/java/com/codeflowx/govern/scheduler/IntegrityVerificationJob.java` (nuevo)

---

### INC-008-005: Inconsistencia en Algoritmo de Hash (Java vs PostgreSQL)
**Prioridad:** 🟡 MEDIA  
**Impacto:** Bajo - Funciona correctamente, pero falta consistencia  
**Artículo EU AI Act:** Art. 19 (precisión)

**Descripción:**
Java usa `timestampEpoch` (Long) en hash, PostgreSQL usa `timestamp::TEXT`. Ambos son válidos, pero deberían ser consistentes para evitar confusión.

**Evidencia:**
```java
// Java: usa timestampEpoch (Long)
String hashInput = log.getImlprevioushash() +
                   log.getImltimestampepoch() + // Long
                   ...
```

```sql
-- PostgreSQL: usa timestamp::TEXT
hash_input :=
    last_hash ||
    NEW.IMLTIMESTAMP::TEXT || -- TEXT
    ...
```

**Recomendación:**
1. **Estandarizar algoritmo:**
   - Usar mismo orden de campos en ambos
   - Usar mismo formato de timestamp (epoch o ISO 8601)
   - Documentar formalmente el algoritmo

2. **Crear función SQL de referencia:**
   ```sql
   CREATE OR REPLACE FUNCTION iml_calculate_hash_reference(
       prev_hash VARCHAR(64),
       timestamp_epoch BIGINT,
       entity_type VARCHAR(50),
       entity_id BIGINT,
       action VARCHAR(100),
       user_id BIGINT,
       data TEXT
   ) RETURNS VARCHAR(64) AS $$
   BEGIN
       RETURN encode(
           digest(
               prev_hash ||
               timestamp_epoch::TEXT ||
               entity_type ||
               entity_id::TEXT ||
               action ||
               user_id::TEXT ||
               data,
               'sha256'
           ),
           'hex'
       );
   END;
   $$ LANGUAGE plpgsql;
   ```

**Esfuerzo Estimado:** 0.5 días  
**Archivos Afectados:**
- `src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java` (modificar)
- `sql-scripts/functions/iml_calculate_hash_reference.sql` (nuevo)
- `docs/compliance/HASH_ALGORITHM_SPECIFICATION.md` (nuevo)

---

## 🟢 MEJORAS RECOMENDADAS

### INC-008-006: Soporte para Timestamp Externo (RFC 3161)
**Prioridad:** 🟢 BAJA  
**Impacto:** Bajo - Mejora opcional para mayor seguridad  
**Artículo EU AI Act:** Art. 19 (precisión temporal)

**Descripción:**
El campo `IMLEXTERNALTIMESTAMP` existe pero no se utiliza. Podría implementarse soporte para timestamps externos (blockchain, TSA) para mayor garantía temporal.

**Recomendación:**
1. **Implementar integración opcional:**
   - Servicio de timestamp externo (blockchain, TSA)
   - Guardar proof en `IMLEXTERNALTIMESTAMP`
   - Verificación de timestamp externo en exportaciones

**Esfuerzo Estimado:** 2 días (opcional)  
**Archivos Afectados:**
- `src/main/java/com/codeflowx/govern/business/logging/ExternalTimestampService.java` (nuevo)

---

## 📊 RESUMEN DE INCIDENCIAS

| ID | Prioridad | Descripción | Esfuerzo |
|----|-----------|-------------|----------|
| INC-008-001 | 🔴 CRÍTICA | Trazabilidad completa no integrada | 3 días |
| INC-008-002 | 🔴 CRÍTICA | Exportación auditores incompleta | 2 días |
| INC-008-003 | 🟡 MEDIA | Documentación cumplimiento ausente | 1 día |
| INC-008-004 | 🟡 MEDIA | Falta alerta automática manipulación | 1 día |
| INC-008-005 | 🟡 MEDIA | Inconsistencia algoritmo hash | 0.5 días |
| INC-008-006 | 🟢 BAJA | Soporte timestamp externo | 2 días (opcional) |

**Total Esfuerzo Estimado:** 7.5 días (9.5 días con opcional)

---

## 🎯 PLAN DE ACCIÓN RECOMENDADO

### Fase 1: Críticas (Semana 1)
- [ ] INC-008-001: Trazabilidad completa (3 días)
- [ ] INC-008-002: Exportación auditores (2 días)

### Fase 2: Medias (Semana 2)
- [ ] INC-008-003: Documentación cumplimiento (1 día)
- [ ] INC-008-004: Alerta automática (1 día)
- [ ] INC-008-005: Consistencia hash (0.5 días)

### Fase 3: Opcionales (Futuro)
- [ ] INC-008-006: Timestamp externo (2 días)

---

**Última Actualización:** 2025-11-17  
**Responsable:** Equipo de Compliance CodeflowX  
**Próxima Revisión:** 2025-12-17

---

**FIN DEL DOCUMENTO**







