# 📊 PROMPT DE IMPLEMENTACIÓN - POST-MARKET MONITORING (Art. 20, 72)

**Módulo:** Compliance - Post-Market Monitoring
**Artículo EU AI Act:** Art. 20 - Post-Market Monitoring + Art. 72
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 4-5 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el sistema completo de monitoreo post-mercado según el Art. 20 y Art. 72 del EU AI Act. El sistema debe monitorear continuamente sistemas en producción, detectar incidentes y gestionar acciones correctoras.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/post-market-monitoring/page.tsx` | ✅ Existe | Dashboard PMM | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/incidents/page.tsx` | ❌ No existe | Reporte de incidentes | 🔴 Alta |
| 3 | `app/(app)/governance/compliance/corrective-actions/page.tsx` | ❌ No existe | Acciones correctoras | 🟡 Media |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Dashboard:** `console/gobierno/compliance/post-market-monitoring-dashboard.zul`
  - ViewModel: No identificado específicamente

### **ViewModels Java**
- No se ha identificado un ViewModel específico para esta pantalla
- La funcionalidad puede estar implementada directamente usando BusinessService

### **Entidades JPA**
- **PostMarketMonitoring** - `com.codeflowx.govern.entity.compliance.PostMarketMonitoring`
  - **Tabla:** `GOVPOSTMARKETMONITORING` (prefijo `GOV`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketMonitoring.java`
  - **Campos principales:**
    - `IDXPOSTMARKETMONITORING` (Long, PK) - ID autonumérico
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `PMMMONITORINGDATE` (Timestamp) - Fecha de monitoreo
    - `PMMPERFORMANCEMETRICS` (JSONB) - Métricas de rendimiento
    - `PMMQUALITYMETRICS` (JSONB) - Métricas de calidad
    - `PMMDRIFTDETECTED` (Boolean) - Si se detectó drift
    - `PMMANOMALIESDETECTED` (Boolean) - Si se detectaron anomalías
    - `PMMCREATEDAT` (Timestamp) - Fecha creación
    - `IDUUID` (String) - UUID único

- **Incident** - `com.codeflowx.govern.entity.compliance.Incident`
  - **Tabla:** `GOVINCIDENTS` (prefijo `GOV`)
  - **Campos principales:**
    - `IDXINCIDENT` (Long, PK) - ID autonumérico
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `INCSeverity` (String) - Severidad: LOW, MEDIUM, HIGH, CRITICAL
    - `INCDESCRIPTION` (String) - Descripción del incidente
    - `INCINCIDENTDATE` (Timestamp) - Fecha del incidente
    - `INCAUTHORITYNOTIFIED` (Boolean) - Si fue notificado a autoridades (Art. 20.1)
    - `INCAUTHORITYNOTIFIEDAT` (Timestamp) - Fecha de notificación
    - `INCUSERSNOTIFIED` (Boolean) - Si fueron notificados usuarios
    - `INCRCA` (String) - Análisis de causa raíz (RCA)
    - `INCSTATUS` (String) - Estado: OPEN, INVESTIGATING, RESOLVED, CLOSED
    - `INCCREATEDAT` (Timestamp) - Fecha creación
    - `IDUUID` (String) - UUID único

- **CorrectiveAction** - `com.codeflowx.govern.entity.compliance.CorrectiveAction`
  - **Tabla:** `GOVCORRECTIVEACTIONS` (prefijo `GOV`)
  - **Campos principales:**
    - `IDXCORRECTIVEACTION` (Long, PK) - ID autonumérico
    - `IDXINCIDENT` (Long, FK) - Referencia a incidente
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `CADESCRIPTION` (String) - Descripción de la acción
    - `CAPLANNEDDATE` (Timestamp) - Fecha planificada
    - `CAACTUALDATE` (Timestamp) - Fecha real de ejecución
    - `CAEFFECTIVENESS` (BigDecimal) - Efectividad (0.00 - 1.00)
    - `CASTATUS` (String) - Estado: PLANNED, IN_PROGRESS, COMPLETED, VERIFIED
    - `CACREATEDAT` (Timestamp) - Fecha creación
    - `IDUUID` (String) - UUID único

### **Business Services Disponibles**
- **PostMarketMonitoringService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringService.java`
  - **Métodos principales:**
    - `monitorSystem(Long projectId)` - Monitorea sistema en producción
      - Consulta métricas de rendimiento y calidad
      - Detecta drift y degradación
      - Genera alertas automáticas
    - `detectAnomalies(Long projectId)` - Detecta anomalías
      - Compara métricas actuales con baseline
      - Identifica desviaciones significativas
    - `getMonitoringHistory(Long projectId, Date from, Date to)` - Obtiene historial
      - Consulta: `SELECT * FROM govpostmarketmonitoring WHERE idxproject = ? AND pmmmonitoringdate BETWEEN ? AND ?`

- **IncidentService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/IncidentService.java`
  - **Métodos principales:**
    - `reportIncident(Long projectId, IncidentData data, String reportedBy)` - Reporta incidente
      - INSERT: `INSERT INTO govincidents (...)`
      - Si severidad >= HIGH: notificar autoridades automáticamente (Art. 20.1)
    - `notifyAuthority(Long incidentId)` - Notifica a autoridades
      - Actualiza: `UPDATE govincidents SET incauthoritynotified = true, incauthoritynotifiedat = ? WHERE idxincident = ?`
    - `notifyUsers(Long incidentId)` - Notifica usuarios afectados
      - Actualiza: `UPDATE govincidents SET incusersnotified = true WHERE idxincident = ?`
    - `performRCA(Long incidentId, String rcaAnalysis)` - Realiza análisis de causa raíz
      - Actualiza: `UPDATE govincidents SET incrca = ? WHERE idxincident = ?`

- **CorrectiveActionService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/CorrectiveActionService.java`
  - **Métodos principales:**
    - `createCorrectiveAction(Long incidentId, CorrectiveActionData data)` - Crea acción correctora
      - INSERT: `INSERT INTO govcorrectiveactions (...)`
    - `trackEffectiveness(Long actionId, BigDecimal effectiveness)` - Rastrea efectividad
      - Actualiza: `UPDATE govcorrectiveactions SET caeffectiveness = ? WHERE idxcorrectiveaction = ?`
    - `closeIncident(Long incidentId)` - Cierra incidente
      - Actualiza: `UPDATE govincidents SET incstatus = 'CLOSED' WHERE idxincident = ?`

### **Servicios CRUD**
- **PostMarketMonitoringService** (CRUD)
- **IncidentService** (CRUD)
- **CorrectiveActionService** (CRUD)

### **Lógica de Negocio Detallada**

#### **Monitoreo Continuo:**
```java
// Implementado en PostMarketMonitoringService.monitorSystem()
public PostMarketMonitoring monitorSystem(Long projectId) {
    Project project = projectService.findById(projectId);

    // Obtener métricas actuales
    PerformanceMetrics currentMetrics = getCurrentMetrics(projectId);
    QualityMetrics qualityMetrics = getQualityMetrics(projectId);

    // Comparar con baseline
    PerformanceMetrics baseline = getBaselineMetrics(projectId);
    boolean driftDetected = detectDrift(currentMetrics, baseline);
    boolean anomaliesDetected = detectAnomalies(currentMetrics, baseline);

    // Crear registro de monitoreo
    PostMarketMonitoring monitoring = new PostMarketMonitoring();
    monitoring.setIdxproject(projectId);
    monitoring.setPmmmonitoringdate(new Timestamp(System.currentTimeMillis()));
    monitoring.setPmmperformancemetrics(currentMetrics.toJson());
    monitoring.setPmmqualitymetrics(qualityMetrics.toJson());
    monitoring.setPmmdriftdetected(driftDetected);
    monitoring.setPmmanomaliesdetected(anomaliesDetected);

    // Si se detectan anomalías, crear alerta
    if (anomaliesDetected) {
        createAlert(projectId, "Anomalías detectadas en monitoreo post-mercado");
    }

    return save(monitoring);
}
```

#### **Reporte de Incidentes (Art. 20.1):**
```java
// Implementado en IncidentService.reportIncident()
public Incident reportIncident(Long projectId, IncidentData data, String reportedBy) {
    Incident incident = new Incident();
    incident.setIdxproject(projectId);
    incident.setIncseverity(data.getSeverity());
    incident.setIncdescription(data.getDescription());
    incident.setIncincidentdate(data.getIncidentDate());
    incident.setIncstatus("OPEN");
    incident.setInccreatedat(new Timestamp(System.currentTimeMillis()));

    incident = save(incident);

    // Si severidad >= HIGH, notificar autoridades automáticamente (Art. 20.1)
    if (data.getSeverity() == Severity.HIGH || data.getSeverity() == Severity.CRITICAL) {
        notifyAuthority(incident.getId());
    }

    return incident;
}
```

---

## 📱 PANTALLA 1: Dashboard Post-Market Monitoring

### **Ruta:** `app/(app)/governance/compliance/post-market-monitoring/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Métricas Principales:**
   - Sistemas monitoreados
   - Incidentes activos
   - Acciones correctoras pendientes
   - Tasa de cumplimiento SLA

2. **Monitoreo Continuo:**
   - Lista de sistemas en producción
   - Métricas de rendimiento y calidad
   - Detección de drift
   - Alertas de anomalías

3. **Gráficos:**
   - Evolución de métricas en el tiempo
   - Distribución de incidentes por severidad
   - Efectividad de acciones correctoras

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockPostMarketMonitoring.ts
export const mockPostMarketMonitoring = {
  metrics: {
    systemsMonitored: 15,
    activeIncidents: 3,
    pendingActions: 5,
    slaCompliance: 0.92
  },
  systems: [
    {
      id: 1,
      projectName: "AI Credit Scoring System",
      lastMonitoring: "2025-12-01T10:00:00Z",
      performanceScore: 0.95,
      qualityScore: 0.88,
      driftDetected: false,
      anomaliesDetected: false
    },
    {
      id: 2,
      projectName: "Facial Recognition System",
      lastMonitoring: "2025-12-01T09:30:00Z",
      performanceScore: 0.72,
      qualityScore: 0.65,
      driftDetected: true,
      anomaliesDetected: true
    }
  ],
  recentIncidents: [
    {
      id: 1,
      projectName: "Facial Recognition System",
      severity: "HIGH",
      description: "Degradación significativa en precisión",
      incidentDate: "2025-12-01T09:00:00Z",
      status: "INVESTIGATING"
    }
  ]
};
```

---

## 📱 PANTALLA 2: Reporte de Incidentes

### **Ruta:** `app/(app)/governance/compliance/incidents/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Listado de Incidentes:**
   - Tabla con todos los incidentes
   - Columnas: Proyecto, Severidad, Fecha, Estado, Notificado
   - Filtros: por proyecto, severidad, estado, rango de fechas

2. **Reporte de Nuevo Incidente:**
   - Formulario con:
     - Proyecto afectado
     - Severidad (LOW, MEDIUM, HIGH, CRITICAL)
     - Descripción detallada
     - Fecha del incidente
   - Notificación automática si severidad >= HIGH (Art. 20.1)

3. **Gestión de Incidentes:**
   - Ver detalle
   - Notificar autoridades manualmente
   - Notificar usuarios afectados
   - Realizar análisis de causa raíz (RCA)
   - Cerrar incidente

### **Mock Data:**

```typescript
export const mockIncidents = [
  {
    id: 1,
    projectId: 2,
    projectName: "Facial Recognition System",
    severity: "HIGH",
    description: "Degradación significativa en precisión de reconocimiento facial",
    incidentDate: "2025-12-01T09:00:00Z",
    authorityNotified: true,
    authorityNotifiedAt: "2025-12-01T09:15:00Z",
    usersNotified: true,
    rca: "Análisis de causa raíz: Drift en datos de entrada debido a cambios en condiciones de iluminación",
    status: "INVESTIGATING",
    createdAt: "2025-12-01T09:00:00Z"
  },
  {
    id: 2,
    projectId: 1,
    projectName: "AI Credit Scoring System",
    severity: "MEDIUM",
    description: "Aumento en tasa de falsos positivos",
    incidentDate: "2025-11-28T14:30:00Z",
    authorityNotified: false,
    usersNotified: false,
    rca: null,
    status: "OPEN",
    createdAt: "2025-11-28T14:30:00Z"
  }
];
```

---

## 📱 PANTALLA 3: Acciones Correctoras

### **Ruta:** `app/(app)/governance/compliance/corrective-actions/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Listado de Acciones:**
   - Tabla con todas las acciones correctoras
   - Columnas: Incidente, Proyecto, Descripción, Fecha planificada, Estado, Efectividad
   - Filtros: por proyecto, estado, incidente

2. **Crear Acción Correctora:**
   - Formulario con:
     - Incidente asociado
     - Descripción de la acción
     - Fecha planificada
     - Responsable

3. **Seguimiento:**
   - Marcar como completada
   - Evaluar efectividad (0.00 - 1.00)
   - Cerrar incidente si efectividad >= 0.80

### **Mock Data:**

```typescript
export const mockCorrectiveActions = [
  {
    id: 1,
    incidentId: 1,
    projectId: 2,
    projectName: "Facial Recognition System",
    description: "Actualizar modelo con datos de nuevas condiciones de iluminación",
    plannedDate: "2025-12-05T00:00:00Z",
    actualDate: null,
    effectiveness: null,
    status: "PLANNED"
  },
  {
    id: 2,
    incidentId: 2,
    projectId: 1,
    projectName: "AI Credit Scoring System",
    description: "Reentrenar modelo con dataset balanceado",
    plannedDate: "2025-12-03T00:00:00Z",
    actualDate: "2025-12-03T10:00:00Z",
    effectiveness: 0.85,
    status: "VERIFIED"
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
import { Activity, AlertTriangle } from "lucide-react";

export default function PostMarketMonitoringPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-orange-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Activity className="w-8 h-8 text-orange-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-orange-500 to-orange-700 bg-clip-text text-transparent">
              {t("compliance.pmm.title", "Post-Market Monitoring")}
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
pmm: {
  title: {
    es: "Monitoreo Post-Mercado",
    en: "Post-Market Monitoring"
  },
  incidents: {
    title: {
      es: "Gestión de Incidentes",
      en: "Incident Management"
    },
    severity: {
      LOW: { es: "Baja", en: "Low" },
      MEDIUM: { es: "Media", en: "Medium" },
      HIGH: { es: "Alta", en: "High" },
      CRITICAL: { es: "Crítica", en: "Critical" }
    }
  },
  correctiveActions: {
    title: {
      es: "Acciones Correctoras",
      en: "Corrective Actions"
    }
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Dashboard (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Añadir métricas principales
- [ ] Implementar monitoreo continuo
- [ ] Añadir gráficos de evolución
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: Incidentes (Crear nueva)**
- [ ] Crear página `incidents/page.tsx`
- [ ] Implementar listado con filtros
- [ ] Añadir formulario de reporte
- [ ] Implementar notificación automática
- [ ] Añadir gestión de incidentes
- [ ] Añadir mock data
- [ ] Crear API routes mock
- [ ] Añadir entrada al menú

### **Pantalla 3: Acciones Correctoras (Crear nueva)**
- [ ] Crear página `corrective-actions/page.tsx`
- [ ] Implementar listado con filtros
- [ ] Añadir formulario de creación
- [ ] Implementar seguimiento de efectividad
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
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 20, 72

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
