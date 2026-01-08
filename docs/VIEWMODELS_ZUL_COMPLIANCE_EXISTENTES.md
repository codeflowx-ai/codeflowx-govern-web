# ✅ VIEWMODELS Y ZUL EXISTENTES - PANTALLAS COMPLIANCE

**Fecha:** Noviembre 2025
**Objetivo:** Inventario de ViewModels y archivos ZUL existentes para las pantallas de Compliance pendientes

---

## ✅ VIEWMODELS Y ZUL ENCONTRADOS

### 1. **Classification (Art. 6)** ✅
- **ViewModel:** `HighRiskClassifierViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.HighRiskClassifierViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/HighRiskClassifierViewModel.java`
  - **Líneas:** ~994 líneas
- **ZUL:** `high-risk-classifier.zul`
  - **Ubicación:** `src/main/webapp/console/gobierno/compliance/high-risk-classifier.zul`
- **Estado:** ✅ **EXISTE** - Listo para migración

---

### 2. **FRIA Assessment (Art. 27)** ✅
- **ViewModel:** `FriaWizardViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.FriaWizardViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/FriaWizardViewModel.java`
- **ViewModel Alternativo:** `FriaAssessmentViewModel.java`
  - **Ubicación:** `com.codeflowx.platform.viewmodel.compliance.FriaAssessmentViewModel`
- **ZUL:** `fria-wizard.zul`
  - **Ubicación:** `src/main/webapp/console/gobierno/compliance/fria-wizard.zul`
- **ZUL Alternativo:** `fria-assessments.zul`
  - **Ubicación:** `src/main/webapp/console/platform/compliance/fria-assessments.zul`
- **Estado:** ✅ **EXISTE** - Listo para migración

---

### 3. **EU Registration (Art. 49)** ✅
- **ViewModel:** `EuRegistrationFormViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.euregistration.EuRegistrationFormViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/euregistration/EuRegistrationFormViewModel.java`
- **ViewModel Alternativo:** `EURegistrationStatusViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.EURegistrationStatusViewModel`
- **ViewModels Relacionados:**
  - `ReviewRegistrationPackageViewModel.java`
  - `ManualResolutionViewModel.java`
  - `FixValidationErrorsViewModel.java`
- **ZUL:** `eu-registration-form.zul`
  - **Ubicación:** `src/main/webapp/console/bpmn/eu-registration-form.zul`
- **ZUL Alternativo:** `eu-registration-status.zul`
  - **Ubicación:** `src/main/webapp/console/gobierno/compliance/eu-registration-status.zul`
- **Estado:** ✅ **EXISTE** - Listo para migración

---

### 4. **Immutable Logs (Art. 19)** ⚠️
- **ViewModel:** `LogSearchViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.LogSearchViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/LogSearchViewModel.java`
- **ZUL:** `log-search-advanced.zul`
  - **Ubicación:** `src/main/webapp/console/gobierno/compliance/log-search-advanced.zul`
- **Entidad:** `ImmutableLog` ✅
- **BusinessService:** `ImmutableLoggingBusinessService` ✅
- **Estado:** ⚠️ **PARCIAL** - Existe búsqueda de logs, pero puede necesitar pantalla específica de gestión

---

### 5. **Traceability Evidence (Art. 12)** ✅
- **ViewModel:** `TraceabilityEvidenceViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.TraceabilityEvidenceViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/TraceabilityEvidenceViewModel.java`
- **ZUL:** `traceability-evidence.zul`
  - **Ubicación:** `src/main/webapp/console/gobierno/compliance/traceability-evidence.zul`
- **Estado:** ✅ **EXISTE** - Listo para migración

---

### 6. **QMS (Art. 17)** ✅
- **ViewModel:** `ReviewQmsGapsViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.ReviewQmsGapsViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/ReviewQmsGapsViewModel.java`
- **ZUL:** `review-qms-gaps-form.zul`
  - **Ubicación:** `src/main/webapp/console/bpmn/review-qms-gaps-form.zul`
- **BusinessService:** `QualityManagementSystemBusinessService` ✅
- **Estado:** ✅ **EXISTE** - Listo para migración (puede necesitar dashboard adicional)

---

### 7. **Technical Docs (Art. 11)** ✅
- **ViewModel:** `AIActDocumentationGeneratorViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.AIActDocumentationGeneratorViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/AIActDocumentationGeneratorViewModel.java`
- **ViewModel Alternativo:** `CompleteDocumentationViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.compliance.CompleteDocumentationViewModel`
- **ZUL:** `ai-act-documentation-generator.zul`
  - **Ubicación:** `src/main/webapp/console/gobierno/compliance/ai-act-documentation-generator.zul`
- **Entidad:** `Model` (campos `modtechnicaldoc*`) ✅
- **Estado:** ✅ **EXISTE** - Listo para migración

---

### 8. **HITL Supervision (Art. 14)** ✅
- **ViewModel:** `HitlSupervisionViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.viewmodel.governance.HitlSupervisionViewModel`
  - **Archivo:** `src/main/java/com/codeflowx/govern/viewmodel/governance/HitlSupervisionViewModel.java`
- **ViewModel Relacionado:** `HitlSlaReminderViewModel.java`
  - **Ubicación:** `com.codeflowx.govern.workflow.viewmodels.HitlSlaReminderViewModel`
- **ZUL:** `hitl-supervision.zul`
  - **Ubicación:** `src/main/webapp/console/gobierno/governance/hitl-supervision.zul`
- **ZUL Relacionado:** `hitl-sla-reminder-form.zul`
  - **Ubicación:** `src/main/webapp/console/bpmn/hitl-sla-reminder-form.zul`
- **Estado:** ✅ **EXISTE** - Listo para migración

---

### 9. **Prohibited Systems (Art. 5)** ⚠️
- **ViewModel:** ❌ **NO ENCONTRADO** (pero existe BusinessService)
- **BusinessService:** `ProhibitedSystemBusinessService` ✅
- **Entidad:** `ProhibitedSystem` ✅
- **ZUL:** ❌ **NO ENCONTRADO**
- **Estado:** ⚠️ **PENDIENTE** - Necesita crear ViewModel y ZUL desde cero

---

## 📊 RESUMEN

| Pantalla | ViewModel | ZUL | Estado |
|----------|-----------|-----|--------|
| **Classification (Art. 6)** | ✅ `HighRiskClassifierViewModel` | ✅ `high-risk-classifier.zul` | ✅ Listo |
| **FRIA Assessment (Art. 27)** | ✅ `FriaWizardViewModel` | ✅ `fria-wizard.zul` | ✅ Listo |
| **EU Registration (Art. 49)** | ✅ `EuRegistrationFormViewModel` | ✅ `eu-registration-form.zul` | ✅ Listo |
| **Immutable Logs (Art. 19)** | ⚠️ `LogSearchViewModel` | ⚠️ `log-search-advanced.zul` | ⚠️ Parcial |
| **Traceability Evidence (Art. 12)** | ✅ `TraceabilityEvidenceViewModel` | ✅ `traceability-evidence.zul` | ✅ Listo |
| **QMS (Art. 17)** | ✅ `ReviewQmsGapsViewModel` | ✅ `review-qms-gaps-form.zul` | ✅ Listo |
| **Technical Docs (Art. 11)** | ✅ `AIActDocumentationGeneratorViewModel` | ✅ `ai-act-documentation-generator.zul` | ✅ Listo |
| **HITL Supervision (Art. 14)** | ✅ `HitlSupervisionViewModel` | ✅ `hitl-supervision.zul` | ✅ Listo |
| **Prohibited Systems (Art. 5)** | ❌ No encontrado | ❌ No encontrado | ❌ Pendiente |

**Total:** 8/9 pantallas tienen ViewModels y ZUL existentes (89%)

---

## 🎯 RECOMENDACIONES

### **Pantallas Listas para Migración (8):**
1. ✅ **Classification (Art. 6)** - Crear prompt de migración
2. ✅ **FRIA Assessment (Art. 27)** - Crear prompt de migración
3. ✅ **EU Registration (Art. 49)** - Crear prompt de migración
4. ✅ **Traceability Evidence (Art. 12)** - Crear prompt de migración
5. ✅ **QMS (Art. 17)** - Crear prompt de migración
6. ✅ **Technical Docs (Art. 11)** - Crear prompt de migración
7. ✅ **HITL Supervision (Art. 14)** - Crear prompt de migración
8. ⚠️ **Immutable Logs (Art. 19)** - Revisar si `LogSearchViewModel` es suficiente o necesita pantalla adicional

### **Pantalla Pendiente (1):**
9. ❌ **Prohibited Systems (Art. 5)** - Crear ViewModel y ZUL desde cero, luego crear prompt de migración

---

## 📝 NOTAS

- **ViewModels en `compliance/`:** 21 ViewModels identificados
- **ViewModels en `euregistration/`:** 4 ViewModels identificados
- **ViewModels en `governance/`:** `HitlSupervisionViewModel` identificado
- **Archivos ZUL en `console/gobierno/compliance/`:** 15+ archivos ZUL
- **Archivos ZUL en `console/bpmn/`:** Varios formularios BPMN relacionados

---

**Última actualización:** Noviembre 2025
**Estado:** 8/9 pantallas tienen ViewModels y ZUL existentes (89%)
