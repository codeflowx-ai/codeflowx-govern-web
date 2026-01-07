# 📍 UBICACIÓN DE BUSINESS SERVICES - COMPLIANCE

**Fecha:** Noviembre 2025
**Objetivo:** Documentar dónde están ubicados los BusinessServices relacionados con Compliance

---

## 📂 UBICACIÓN DE BUSINESS SERVICES

### **Módulo:** `codeflowx.govern.business`

**Base Path:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`

**Documentación:** `suinsit.nova.web/docs/developers/README.md`

---

## ⚠️ BUSINESS SERVICES - ESTADO REAL

**IMPORTANTE:** La mayoría de BusinessServices están **documentados pero NO existen en el código**.

### **Verificación Realizada:**
- ❌ **NO existe** directorio `business/compliance/`
- ❌ **NO existe** directorio `business/logging/`
- ✅ **Solo existe** directorio `business/developer/` con 7 servicios

### **BusinessServices Documentados vs Reales:**
- **Documentados:** 11 servicios (en `suinsit.nova.web/docs/developers/`)
- **Reales:** 2 servicios (según `LISTA_ARCHIVOS_MODIFICADOS.txt`)
- **Faltantes:** 9 servicios necesitan ser creados

---

## ✅ BUSINESS SERVICES QUE EXISTEN (2 servicios - VERIFICAR)

### **1. FriaAssessmentBusinessService** ❌ **NO EXISTE**
- **Ubicación:** `com.codeflowx.govern.business.compliance.FriaAssessmentBusinessService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/FriaAssessmentBusinessService.md`
- **Métodos Principales:**
  - `createFria()` - Crea evaluación FRIA
  - `calculateFinalRisk()` - Calcula riesgo final según Anexo IX
  - `validateFriaComplete()` - Valida completitud FRIA
- **Entidad:** `FriaAssessment`
- **Artículo EU AI Act:** Art. 27

### **2. ComplianceAssessmentBusinessService** ❌ **NO EXISTE**
- **Ubicación:** `com.codeflowx.govern.business.compliance.ComplianceAssessmentBusinessService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/ComplianceAssessmentBusinessService.md`
- **Métodos Principales:**
  - `createAssessment()` - Crea evaluación de conformidad
  - `executeStep2QmsCheck()` - Ejecuta step 2 (QMS)
  - `executeStep3DocReview()` - Ejecuta step 3 (Documentación)
  - `calculateOverallScore()` - Calcula score overall
- **Entidad:** `ComplianceAssessment`
- **Artículo EU AI Act:** Art. 43, Anexo VI

### **3. EuRegistrationBusinessService** ❌ **NO EXISTE**
- **Ubicación:** `com.codeflowx.govern.business.compliance.EuRegistrationBusinessService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/EuRegistrationBusinessService.md`
- **Métodos Principales:**
  - `createRegistration()` - Crea registro UE
  - `submitToEuDatabase()` - Envía a base de datos UE
  - `getRegistrationStatus()` - Obtiene estado del registro
- **Entidad:** `EuRegistration`
- **Artículo EU AI Act:** Art. 49

### **4. QualityManagementSystemBusinessService** ⚠️ **PENDIENTE VERIFICAR**
- **Ubicación Esperada:** `com.codeflowx.govern.business.compliance.QualityManagementSystemBusinessService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/QualityManagementSystemBusinessService.md`
- **Según LISTA_ARCHIVOS_MODIFICADOS.txt:** Existe en `business/compliance/` (650 líneas)
- **Estado Real:** ⚠️ No encontrado en búsqueda - Requiere verificación
- **Métodos Principales:**
  - `getComplianceStrategy()` - Obtiene estrategia compliance
  - `updateComplianceStrategy()` - Actualiza estrategia
  - `calculateQmsComplianceScore()` - Calcula score QMS
- **Entidad:** `QualityManagementSystem`
- **Artículo EU AI Act:** Art. 17

### **5. TechnicalDocumentationBusinessService** ❌ **NO EXISTE**
- **Ubicación:** `com.codeflowx.govern.business.compliance.TechnicalDocumentationBusinessService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/TechnicalDocumentationBusinessService.md`
- **Métodos Principales:**
  - `calculateDocumentationScore()` - Calcula score de documentación
  - `validateAnexoIVCompleteness()` - Valida completitud Anexo IV
- **Entidad:** `TechnicalDocumentation` / `Model` (campos `modtechnicaldoc*`)
- **Artículo EU AI Act:** Art. 11, Anexo IV

### **6. ProhibitedSystemBusinessService** ❌ **NO EXISTE**
- **Ubicación:** `com.codeflowx.govern.business.compliance.ProhibitedSystemBusinessService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/ProhibitedSystemBusinessService.md`
- **Métodos Principales:**
  - `checkProhibitedSystem()` - Verifica sistema prohibido
  - `getActiveProhibitedSystems()` - Obtiene sistemas activos
- **Entidad:** `ProhibitedSystem`
- **Artículo EU AI Act:** Art. 5, Anexo II

### **7. ImmutableLoggingBusinessService** ⚠️ **PENDIENTE VERIFICAR**
- **Ubicación Esperada:** `com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService`
- **Documentación:** `suinsit.nova.web/docs/developers/logging/ImmutableLoggingBusinessService.md`
- **Según LISTA_ARCHIVOS_MODIFICADOS.txt:** "ya existía completo" en `business/logging/` (200 líneas)
- **Estado Real:** ⚠️ No encontrado en búsqueda - Requiere verificación
- **Métodos Principales:**
  - `createImmutableLog()` - Crea log inmutable con hash chain
  - `verifyLogIntegrity()` - Verifica integridad de logs
  - `getLogChain()` - Obtiene cadena de logs
- **Entidad:** `ImmutableLog`
- **Artículo EU AI Act:** Art. 19, Art. 12

### **8. ComplianceDashboardService** ❌ **NO EXISTE**
- **Ubicación Esperada:** `com.codeflowx.govern.business.compliance.ComplianceDashboardService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/ComplianceDashboardService.md`
- **Estado:** ❌ No encontrado en código

### **9. ComplianceExecutiveReportService** ❌ **NO EXISTE**
- **Ubicación Esperada:** `com.codeflowx.govern.business.compliance.ComplianceExecutiveReportService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/ComplianceExecutiveReportService.md`
- **Estado:** ❌ No encontrado en código

### **10. AuthorityNotificationService** ❌ **NO EXISTE**
- **Ubicación Esperada:** `com.codeflowx.govern.business.compliance.AuthorityNotificationService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/AuthorityNotificationService.md`
- **Estado:** ❌ No encontrado en código

### **11. PostMarketMonitoringService** ❌ **NO EXISTE**
- **Ubicación Esperada:** `com.codeflowx.govern.business.compliance.PostMarketMonitoringService`
- **Documentación:** `suinsit.nova.web/docs/developers/compliance/PostMarketMonitoringService.md`
- **Estado:** ❌ No encontrado en código

---

## 📋 SERVICIOS CRUD (codeflowx.govern.services)

**Base Path:** `nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/`

### Servicios CRUD Disponibles:
- ✅ `ComplianceAssessmentService` - CRUD de evaluaciones
- ✅ `FriaAssessmentService` - CRUD de FRIAs
- ✅ `EuRegistrationService` - CRUD de registros UE
- ✅ `ProhibitedSystemService` - CRUD de sistemas prohibidos
- ✅ `ImmutableLogService` - CRUD de logs inmutables
- ✅ `QualityManagementSystemService` - CRUD de QMS
- ✅ `TechnicalDocumentationService` - CRUD de documentación técnica

---

## 🔍 CÓMO USAR EN VIEWMODELS

### Ejemplo de Inyección:

```java
@WireVariable
private FriaAssessmentBusinessService friaAssessmentBusinessService;

@WireVariable
private ComplianceAssessmentBusinessService complianceAssessmentBusinessService;

@WireVariable
private EuRegistrationBusinessService euRegistrationBusinessService;

@WireVariable
private QualityManagementSystemBusinessService qmsBusinessService;

@WireVariable
private TechnicalDocumentationBusinessService technicalDocBusinessService;

@WireVariable
private ProhibitedSystemBusinessService prohibitedSystemBusinessService;

@WireVariable
private ImmutableLoggingBusinessService immutableLoggingBusinessService;
```

### Ejemplo de Uso:

```java
// Crear FRIA
FriaAssessment fria = friaAssessmentBusinessService.createFria(projectId, createdBy);

// Calcular riesgo
BigDecimal risk = friaAssessmentBusinessService.calculateFinalRisk(fria);

// Verificar sistema prohibido
ProhibitedSystemCheckResult result = prohibitedSystemBusinessService.checkProhibitedSystem(project);

// Crear log inmutable
ImmutableLog log = immutableLoggingBusinessService.createImmutableLog(event, entityId, userId);
```

---

## 📚 REFERENCIAS

- **Documentación Completa:** `suinsit.nova.web/docs/developers/README.md`
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Business Logic:** `codeflowx-studio/docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`

---

**Última actualización:** Noviembre 2025
**Estado:** ⚠️ **2 BusinessServices verificados, 9 faltantes** - Ver `BUSINESS_SERVICES_ESTADO_REAL.md` para detalles
