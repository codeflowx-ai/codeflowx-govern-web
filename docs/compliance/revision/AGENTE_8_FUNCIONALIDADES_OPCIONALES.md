# GUÍA AGENTE 8 - FUNCIONALIDADES OPCIONALES

**Agente:** Backend
**Equipo:** Equipo 6 - Funcionalidades Complementarias
**Duración:** 6-8 horas
**Objetivo:** Implementar funcionalidades opcionales de baja prioridad

---

## 📋 INCIDENCIAS ASIGNADAS

| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-009** | Persistencia estado árbol riesgo | 1h | 🟢 BAJA | 🟢 COMPLETADO |
| **INC-019** | Notificaciones vencimientos | 1h | 🟢 BAJA | 🟢 COMPLETADO |
| **INC-022** | Caché evaluaciones técnicas | 2h | 🟢 BAJA | 🟢 COMPLETADO |
| **INC-010-DS** | Historial versiones evaluaciones | 1h | 🟢 BAJA | 🟢 COMPLETADO |
| **INC-009-001** | Documentación formal capacidad | 1h | 🟡 MEDIA | 🟢 COMPLETADO |

**Total:** 5 incidencias, ~6 horas - ✅ **TODAS COMPLETADAS**

---

## 📚 DOCUMENTOS DE REFERENCIA

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`
- **Distribución:** `/docs/compliance/revision/DISTRIBUCION_INCIDENCIAS_AGENTES.md`

### Referencias Técnicas:
- **Entidades Existentes:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- **BusinessServices Existentes:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`
- **Scripts SQL:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/`

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "ARQUITECTURA ENART" para convenciones completas.**

**Resumen rápido:**
- Prefijos de 3 caracteres para tablas
- PK autonumérica siempre
- Tercera forma normal
- KISS y SOLID

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Entidades:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/
├── governance/
│   ├── RiskTreeState.java              ← CREAR (INC-009)
│   └── EvaluationVersionHistory.java  ← CREAR (INC-010-DS)
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── governance/
│   ├── RiskTreeStateService.java      ← CREAR (INC-009)
│   ├── ExpirationNotificationService.java ← CREAR (INC-019)
│   ├── EvaluationCacheService.java     ← CREAR (INC-022)
│   └── EvaluationVersionHistoryService.java ← CREAR (INC-010-DS)
```

### **Scripts SQL:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/
├── governance/
│   ├── risk_tree_state.sql            ← CREAR (INC-009)
│   ├── expiration_notifications.sql    ← CREAR (INC-019)
│   ├── evaluation_cache.sql           ← CREAR (INC-022)
│   └── evaluation_version_history.sql ← CREAR (INC-010-DS)
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-009: Persistencia Estado Árbol Riesgo**

#### **Archivos a Crear:**

1. **Entidad:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/RiskTreeState.java`
   - Tabla: `RTSRISKTREESTATES` (prefijo `RTS`)

2. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/RiskTreeStateService.java`

3. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/risk_tree_state.sql`

#### **Funcionalidades:**
- Auto-guardado del estado del árbol de riesgo
- Restauración del estado
- Historial de cambios

#### **Checklist:**
- [ ] Crear entidad según prompt
- [ ] Crear BusinessService
- [ ] Crear script SQL
- [ ] Integrar con servicios de árbol de riesgo
- [ ] Implementar auto-guardado
- [ ] Documentar BusinessService

---

### **INC-019: Notificaciones Vencimientos**

#### **Archivos a Crear:**

1. **Entidad:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ExpirationNotification.java`
   - Tabla: `EXNEXPIRATIONNOTIFICATIONS` (prefijo `EXN`)

2. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ExpirationNotificationService.java`

3. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/expiration_notifications.sql`

#### **Funcionalidades:**
- Job programado para verificar vencimientos
- Notificaciones automáticas
- Alertas antes del vencimiento

#### **Checklist:**
- [ ] Crear entidad según prompt
- [ ] Crear BusinessService
- [ ] Crear script SQL
- [ ] Implementar job programado (Spring @Scheduled)
- [ ] Integrar con sistema de notificaciones
- [ ] Documentar BusinessService

---

### **INC-022: Caché Evaluaciones Técnicas**

#### **Archivos a Crear:**

1. **Entidad:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/evaluation/EvaluationCache.java`
   - Tabla: `EVCEVALUATIONCACHES` (prefijo `EVC`)

2. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationCacheService.java`

3. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/evaluation_cache.sql`

#### **Funcionalidades:**
- Caché de evaluaciones técnicas
- Invalidación automática
- Mejora de performance

#### **Checklist:**
- [ ] Crear entidad según prompt
- [ ] Crear BusinessService
- [ ] Crear script SQL
- [ ] Implementar lógica de caché
- [ ] Integrar con servicios de evaluación
- [ ] Probar mejora de performance
- [ ] Documentar BusinessService

---

### **INC-010-DS: Historial Versiones Evaluaciones**

#### **Archivos a Crear:**

1. **Entidad:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/evaluation/EvaluationVersionHistory.java`
   - Tabla: `EVHEVALUATIONVERSIONHISTORIES` (prefijo `EVH`)

2. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationVersionHistoryService.java`

3. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/evaluation_version_history.sql`

#### **Funcionalidades:**
- Historial de versiones de evaluaciones
- Comparación entre versiones
- Restauración de versiones anteriores

#### **Checklist:**
- [ ] Crear entidad según prompt
- [ ] Crear BusinessService
- [ ] Crear script SQL
- [ ] Integrar con servicios de evaluación
- [ ] Implementar versionado automático
- [ ] Documentar BusinessService

---

### **INC-009-001: Documentación Formal Capacidad**

#### **Archivos a Crear:**

1. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/CapacityDocumentationService.java`

2. **Tests de Carga:**
   - Crear tests de carga según especificaciones

#### **Funcionalidades:**
- Documentación formal de capacidad del sistema
- Tests de carga
- Métricas de capacidad

#### **Checklist:**
- [ ] Crear BusinessService
- [ ] Implementar tests de carga
- [ ] Documentar capacidad del sistema
- [ ] Generar reporte de capacidad
- [ ] Documentar BusinessService

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Leer prompts completos (si existen)
- [ ] Verificar entidades existentes relacionadas
- [ ] Revisar servicios similares como referencia

### **Durante Implementación:**
- [ ] Seguir convenciones EnArt
- [ ] Integrar con servicios existentes
- [ ] Implementar funcionalidades de forma simple (KISS)

### **Después de Implementación:**
- [ ] Compilar sin errores
- [ ] Probar todas las funcionalidades
- [ ] Actualizar `tablas.md`
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`
- [ ] Documentar BusinessServices en `/docs/developers/`
- [ ] Registrar archivos creados en este documento

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

### **INC-009: Persistencia Estado Árbol Riesgo**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `RiskTreeState.java` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/RiskTreeState.java`
- [x] BusinessService: `RiskTreeStateService.java` - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/RiskTreeStateService.java`
- [x] Script SQL: `risk_tree_state.sql` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/risk_tree_state.sql`

---

### **INC-019: Notificaciones Vencimientos**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `ExpirationNotification.java` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ExpirationNotification.java`
- [x] BusinessService: `ExpirationNotificationService.java` - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ExpirationNotificationService.java`
- [x] Script SQL: `expiration_notifications.sql` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/expiration_notifications.sql`

---

### **INC-022: Caché Evaluaciones Técnicas**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `EvaluationCache.java` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/evaluation/EvaluationCache.java`
- [x] BusinessService: `EvaluationCacheService.java` - Actualizado con métodos de caché persistente
- [x] Script SQL: `evaluation_cache.sql` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/evaluation_cache.sql`

---

### **INC-010-DS: Historial Versiones Evaluaciones**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `EvaluationVersionHistory.java` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/evaluation/EvaluationVersionHistory.java`
- [x] BusinessService: `EvaluationVersionHistoryService.java` - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationVersionHistoryService.java`
- [x] Script SQL: `evaluation_version_history.sql` - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/governance/evaluation_version_history.sql`

---

### **INC-009-001: Documentación Formal Capacidad**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] BusinessService: `CapacityDocumentationService.java` - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/CapacityDocumentationService.java`
- [x] Métodos para tests de carga y métricas de capacidad implementados

---

## 🔗 INTEGRACIONES REQUERIDAS

### **Servicios Existentes:**
- `RiskAssessmentService` - Para árbol de riesgo
- `ModelEvaluationService` - Para evaluaciones
- `NotificationService` - Para notificaciones

---

**Última Actualización:** 25 de noviembre de 2025
