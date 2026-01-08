# 🇪🇺 PROMPT DE IMPLEMENTACIÓN - REGISTRO UE (Art. 49 + Anexo VIII)

**Módulo:** Compliance - EU Registration
**Artículo EU AI Act:** Art. 49 - Registro en Base de Datos UE + Anexo VIII
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 3-4 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el formulario completo de registro en la Base de Datos UE según el Art. 49 y Anexo VIII del EU AI Act. El sistema debe permitir completar 3 secciones (A, B, C) y enviar el registro a la Base de Datos UE.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/eu-registration/page.tsx` | ✅ Existe | Formulario registro UE | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/eu-registration/status/page.tsx` | ❌ No existe | Estado de registros | 🟡 Media |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/bpmn/eu-registration-form.zul`
  - ViewModel: `EuRegistrationFormViewModel`
  - Tipo: User Task BPMN (fillRegistrationForm)

### **ViewModels Java**
- **EuRegistrationFormViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.euregistration`
  - **Archivo:** `com/codeflowx/govern/viewmodel/euregistration/EuRegistrationFormViewModel.java`
  - **Servicios Usados:**
    - `@WireVariable ModelService modelService` - Gestión de modelos
    - `@WireVariable TaskService taskService` (Flowable) - Tareas BPMN
    - `@WireVariable EuRegistrationBusinessService euRegistrationBusinessService` - Lógica de negocio
  - **Métodos Principales:**
    - `loadRegistrationData(Long projectId)` - Carga datos del proyecto
    - `saveSectionA(Map<String, Object> data)` - Guarda sección A
    - `saveSectionB(Map<String, Object> data)` - Guarda sección B
    - `saveSectionC(Map<String, Object> data)` - Guarda sección C
    - `validateSubmissionData()` - Valida datos antes de enviar
    - `submitToEuDatabase()` - Envía a Base de Datos UE

- **EURegistrationStatusViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/EURegistrationStatusViewModel.java`
  - **Servicios Usados:**
    - `@WireVariable ModelService modelService`

### **Entidades JPA**
- **EuRegistration** - `com.codeflowx.govern.entity.compliance.EuRegistration`
  - **Tabla:** `REGEUREGISTRATIONS` (prefijo `REG`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java`
  - **Campos principales:**
    - `IDXREGREGISTRATION` (Long, PK) - ID autonumérico
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `REGREGISTRATIONTYPE` (String) - Tipo: STANDARD, SENSITIVE, NATIONAL
    - `REGSECTION` (String) - Sección actual: A, B, C
    - `REGSUBMISSIONDATA` (JSONB) - Datos de envío según Anexo VIII
    - `REGSTATUS` (String) - Estado: DRAFT, PENDING, SUBMITTED, REGISTERED, REJECTED
    - `REGREGISTRATIONID` (String) - ID de registro en BD UE (si existe)
    - `REGRESPONSE` (JSONB) - Respuesta de la BD UE
    - `REGERROR` (String) - Error si falla el registro
    - `REGCREATEDAT` (Timestamp) - Fecha creación
    - `REGCREATEDBY` (String) - Usuario creador
    - `REGSUBMITTEDAT` (Timestamp) - Fecha de envío
    - `IDUUID` (String) - UUID único

### **Business Services Disponibles**
- **EuRegistrationBusinessService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/EuRegistrationBusinessService.java`
  - **Métodos principales:**
    - `createRegistration(Long projectId, String registrationType, String createdBy)` - Crea registro
      - Consulta: `SELECT * FROM prjprojects WHERE idxproject = ? AND prjishighrisk = true`
      - INSERT: `INSERT INTO regeuregistrations (...)`
    - `updateSubmissionData(Long registrationId, String section, Map<String, Object> data)` - Actualiza sección
      - Actualiza: `UPDATE regeuregistrations SET regsubmissiondata = ?, regsection = ? WHERE idxregregistration = ?`
    - `validateSubmissionData(Long registrationId)` - Valida datos
      - Verifica que todas las secciones estén completas
      - Valida formato según Anexo VIII
    - `submitToEuDatabase(Long registrationId)` - Envía a BD UE
      - Genera payload JSON según Anexo VIII
      - Llamada microservicio (COMENTADA): `codeflowx-governance-api: POST /api/v1/eu-registrations/register`
      - Actualiza: `UPDATE regeuregistrations SET regstatus = 'SUBMITTED', regsubmittedat = ? WHERE idxregregistration = ?`
    - `getRegistrationStatus(Long registrationId)` - Obtiene estado
      - Consulta: `SELECT * FROM regeuregistrations WHERE idxregregistration = ?`

### **Servicios CRUD**
- **EuRegistrationService** ✅
  - **Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/EuRegistrationService.java`
  - **Métodos:** `findAll()`, `findById()`, `save()`, `delete()`

### **Lógica de Negocio Detallada**

#### **Formulario de Registro UE (3 Secciones según Anexo VIII):**

**Sección A: Información del Proveedor (13 campos)**
- Nombre del proveedor
- Dirección
- País
- Número de identificación fiscal
- Contacto
- etc.

**Sección B: Información del Sistema IA**
- Nombre del sistema
- Propósito previsto
- Categoría de alto riesgo (Anexo III)
- Descripción técnica
- etc.

**Sección C: Información de Conformidad**
- Base de conformidad
- Organismo notificado (si aplica)
- Certificado de conformidad
- etc.

#### **Envío a Base de Datos UE:**
```java
// Implementado en EuRegistrationBusinessService.submitToEuDatabase()
public EuRegistration submitToEuDatabase(Long registrationId) {
    EuRegistration registration = findById(registrationId);

    // Validar datos
    validateSubmissionData(registrationId);

    // Generar payload JSON según Anexo VIII
    Map<String, Object> payload = generatePayload(registration);

    // TODO: Llamar a microservicio Python para registro en EU Database
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        EuRegistrationResponse response = governanceApiClient.registerInEuDatabase(
            registration.getIdxproject(), payload
        );
        registration.setRegregistrationid(response.getRegistrationId());
        registration.setRegstatus("REGISTERED");
        registration.setRegresponse(response.toJson());
    } catch (Exception e) {
        log.error("Error registrando en EU Database", e);
        registration.setRegstatus("ERROR");
        registration.setRegerror(e.getMessage());
    }
    */

    // Por ahora, simular registro exitoso
    registration.setRegstatus("SUBMITTED");
    registration.setRegsubmittedat(new Timestamp(System.currentTimeMillis()));
    return save(registration);
}
```

---

## 📱 PANTALLA 1: Formulario Registro UE

### **Ruta:** `app/(app)/governance/compliance/eu-registration/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Formulario Multi-Step (3 Secciones):**
   - **Sección A:** Información del proveedor (13 campos)
   - **Sección B:** Información del sistema IA
   - **Sección C:** Información de conformidad

2. **Navegación:**
   - Tabs o wizard para cambiar entre secciones
   - Guardado automático al cambiar de sección
   - Validación antes de avanzar

3. **Envío:**
   - Botón "Enviar a Base de Datos UE"
   - Validación completa antes de enviar
   - Confirmación de envío
   - Monitoreo de estado

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockEuRegistration.ts
export const mockEuRegistration = {
  project: {
    id: 1,
    name: "AI Credit Scoring System"
  },
  sectionA: {
    providerName: "ACME Corporation",
    providerAddress: "123 Main St, Madrid, Spain",
    providerCountry: "ES",
    taxId: "B12345678",
    contactEmail: "contact@acme.com",
    contactPhone: "+34 123 456 789"
  },
  sectionB: {
    systemName: "AI Credit Scoring System",
    intendedPurpose: "Automated credit scoring for loan applications",
    highRiskCategory: "A3_4",
    technicalDescription: "Machine learning model for credit risk assessment...",
    deploymentDate: "2024-01-01"
  },
  sectionC: {
    conformityBasis: "Internal control procedure",
    notifiedBody: null,
    certificateId: null
  },
  status: "DRAFT",
  registrationType: "STANDARD"
};
```

### **API Routes Mock:**

```typescript
// app/api/compliance/eu-registration/route.ts
export async function POST(request: Request) {
  const { projectId, section, data } = await request.json();

  // Mock: Guardar sección
  return Response.json({
    success: true,
    registrationId: 1,
    section,
    status: "DRAFT"
  });
}

// app/api/compliance/eu-registration/submit/route.ts
export async function POST(request: Request) {
  const { registrationId } = await request.json();

  // Mock: Simular envío
  return Response.json({
    success: true,
    registrationId: 1,
    status: "SUBMITTED",
    euRegistrationId: "EU-REG-2025-001234"
  });
}
```

---

## 📱 PANTALLA 2: Estado de Registros

### **Ruta:** `app/(app)/governance/compliance/eu-registration/status/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Listado de Registros:**
   - Tabla con todos los registros
   - Columnas: Proyecto, Tipo, Estado, Fecha envío, ID Registro UE
   - Filtros: por proyecto, estado, tipo, rango de fechas

2. **Estados:**
   - DRAFT - Borrador
   - PENDING - Pendiente de envío
   - SUBMITTED - Enviado a BD UE
   - REGISTERED - Registrado exitosamente
   - REJECTED - Rechazado

3. **Acciones:**
   - Ver detalle del registro
   - Reenviar si fue rechazado
   - Descargar payload JSON
   - Ver respuesta de BD UE

### **Mock Data:**

```typescript
export const mockEuRegistrationStatus = [
  {
    id: 1,
    projectId: 1,
    projectName: "AI Credit Scoring System",
    registrationType: "STANDARD",
    status: "REGISTERED",
    submittedAt: "2025-12-01T10:00:00Z",
    euRegistrationId: "EU-REG-2025-001234",
    response: {
      success: true,
      registrationId: "EU-REG-2025-001234",
      registeredAt: "2025-12-01T10:05:00Z"
    }
  },
  {
    id: 2,
    projectId: 2,
    projectName: "Facial Recognition System",
    registrationType: "SENSITIVE",
    status: "PENDING",
    submittedAt: null,
    euRegistrationId: null,
    response: null
  }
];
```

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Globe, CheckCircle } from "lucide-react";

export default function EuRegistrationPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Globe className="w-8 h-8 text-blue-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
              {t("compliance.euRegistration.title", "EU Database Registration")}
            </h1>
          </div>
        </div>

        {/* Contenido */}
      </div>
    </div>
  );
}
```

---

## 📝 TRADUCCIONES REQUERIDAS

Añadir en `app/config/i18n/modules/governance/compliance.ts`:

```typescript
euRegistration: {
  title: {
    es: "Registro en Base de Datos UE",
    en: "EU Database Registration"
  },
  sections: {
    sectionA: {
      title: {
        es: "Sección A: Información del Proveedor",
        en: "Section A: Provider Information"
      }
    },
    sectionB: {
      title: {
        es: "Sección B: Información del Sistema IA",
        en: "Section B: AI System Information"
      }
    },
    sectionC: {
      title: {
        es: "Sección C: Información de Conformidad",
        en: "Section C: Conformity Information"
      }
    }
  },
  status: {
    DRAFT: { es: "Borrador", en: "Draft" },
    PENDING: { es: "Pendiente", en: "Pending" },
    SUBMITTED: { es: "Enviado", en: "Submitted" },
    REGISTERED: { es: "Registrado", en: "Registered" },
    REJECTED: { es: "Rechazado", en: "Rejected" }
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Formulario (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar Sección A (13 campos)
- [ ] Implementar Sección B
- [ ] Implementar Sección C
- [ ] Añadir navegación entre secciones
- [ ] Implementar validación
- [ ] Añadir envío a BD UE (mock)
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: Status (Crear nueva)**
- [ ] Crear página `status/page.tsx`
- [ ] Implementar listado con filtros
- [ ] Añadir visualización de estados
- [ ] Añadir acciones (ver, reenviar, descargar)
- [ ] Añadir mock data
- [ ] Crear API routes mock
- [ ] Añadir entrada al menú

### **General**
- [ ] Añadir traducciones completas
- [ ] Verificar estilos "Wow Factor"
- [ ] Probar funcionalidad completa
- [ ] Documentar integración con backend

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Prompt Migración:** `docs/prompts/MIGRACION_COMPLIANCE_EU_REGISTRATION.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 49 + Anexo VIII

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
