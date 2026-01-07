# Prompt de Migración - Compliance - EU Registration (Art. 49)

## Contexto del Módulo

**Módulo:** Compliance
**Tipo de Pantalla:** Formulario de Registro
**Total de Pantallas:** 1
**Artículo EU AI Act:** Art. 49 - EU Database Registration

---

## Arquitectura del Módulo

### ViewModel Identificado

#### EuRegistrationFormViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.euregistration`
- **Archivo:** `com/codeflowx/govern/viewmodel/euregistration/EuRegistrationFormViewModel.java`
- **Servicios Usados:**
  - `ModelService` - Gestión de modelos
  - `TaskService` (Flowable) - Tareas BPMN
  - `EuRegistrationBusinessService` - Lógica de negocio
- **Entidades Usadas:**
  - `EuRegistration` - Registro UE
- **Tipo:** User Task BPMN (fillRegistrationForm)

### Entidades JPA Principales

- **EuRegistration** - `com.codeflowx.govern.entity.compliance.EuRegistration`
  - Campos según Anexo VIII (13 campos para Section A)
  - `EURREGISTRATIONTYPE` - Tipo de registro
  - `EURSTATUS` - Estado (DRAFT, PENDING, SUBMITTED, REGISTERED, REJECTED)
  - `EURSUBMISSIONDATA` (JSONB) - Datos de envío

### Business Services Disponibles

- **EuRegistrationBusinessService** ✅
  - `createRegistration(Long projectId, String registrationType)`
  - `updateSubmissionData(Long registrationId, Map<String, Object> data)`
  - `submitToEuDatabase(Long registrationId)`
  - `getRegistrationStatus(Long registrationId)`
  - `validateSubmissionData(Long registrationId)`

---

## Pantalla a Migrar

- **Archivo ZUL:** `console/bpmn/eu-registration-form.zul`
- **ViewModel Asociado:** `EuRegistrationFormViewModel.java`
- **Ruta Next.js:** `/governance/compliance/eu-registration`

---

## Estrategia de Migración

### Formulario de 3 Secciones (Anexo VIII):
1. **Section A:** Información del proveedor (13 campos)
2. **Section B:** Información del sistema
3. **Section C:** Información adicional

### Mock Data:
```typescript
export const mockEuRegistrationData = {
  sectionA: {
    providerName: "ACME Corporation",
    providerAddress: "123 Main St, Madrid, Spain",
    systemName: "AI Credit Scoring System",
    intendedPurpose: "Automated credit scoring",
    highriskCategory: "A3_6",
    conformityBody: "Notified Body XYZ"
  },
  status: "DRAFT",
  registrationType: "STANDARD"
};
```

---

**Última actualización:** Noviembre 2025
**Estado:** Listo para implementación
