# 📍 Dónde Usar los Endpoints QMS F-M en el Frontend

**Fecha:** Diciembre 2025
**Estado:** ✅ Endpoints implementados - Pendiente integración frontend

---

## 🎯 RESUMEN

Este documento explica **dónde y cómo usar** los endpoints REST de los módulos F-M del QMS en el frontend, tanto en pantallas existentes como en nuevas funcionalidades.

---

## 📱 PANTALLAS EXISTENTES DONDE INTEGRAR

### **1. Pantalla Principal QMS** (`/governance/compliance/qms?projectId=X`)

**Ubicación:** `app/(app)/governance/compliance/qms/page.tsx`

**Endpoints a usar:**

#### **Módulo F: Gestión de Datos**
- `GET /api/v1/qms/data-management?projectId={id}`
- **Uso:** Mostrar métricas de calidad de datos en el dashboard
- **Dónde:** Agregar card adicional en la sección de métricas principales
- **Ejemplo:**
```typescript
// En loadData() o useEffect separado
const loadDataManagement = async () => {
  const response = await fetch(`/api/governance/compliance/qms/data-management?projectId=${projectId}`);
  const data = await response.json();
  // Mostrar en card: "Data Quality Score: 85%"
};
```

#### **Módulo G: Gestión de Riesgos**
- `GET /api/v1/qms/risk-management?projectId={id}`
- `GET /api/v1/qms/risks?projectId={id}`
- **Uso:** Mostrar resumen de riesgos y lista de riesgos activos
- **Dónde:**
  - Card de métricas: "Riesgos Identificados: 5, Mitigados: 3"
  - Sección expandible con lista de riesgos
- **Ejemplo:**
```typescript
// Agregar card de riesgos
<MetricCard
  title="Riesgos"
  value={`${riskData.identifiedRisks} / ${riskData.mitigatedRisks}`}
  description={`Score: ${(riskData.overallRiskScore * 100).toFixed(0)}%`}
/>
```

#### **Módulo H: Post-Market Monitoring**
- `GET /api/v1/qms/post-market-monitoring?projectId={id}`
- **Uso:** Mostrar estado de monitoreo poscomercialización
- **Dónde:** Card de métricas o sección dedicada
- **Nota:** Ya existe pantalla `/post-market-monitoring` - puede integrarse allí también

#### **Módulo I: Incidentes Graves**
- `GET /api/v1/qms/serious-incidents?projectId={id}`
- `POST /api/v1/qms/serious-incidents`
- **Uso:**
  - Mostrar alerta si hay incidentes graves sin notificar
  - Lista de incidentes en sección expandible
- **Dónde:**
  - Alerta en header si `incidents.filter(i => !i.authorityNotified).length > 0`
  - Sección "Incidentes Graves" con lista
- **Nota:** Ya existe pantalla `/incidents` - puede integrarse allí también

#### **Módulo J: Comunicaciones con Autoridades**
- `GET /api/v1/qms/authority-communications?projectId={id}`
- `POST /api/v1/qms/authority-communications`
- **Uso:** Mostrar historial de comunicaciones con autoridades
- **Dónde:** Sección "Comunicaciones" con timeline de comunicaciones

#### **Módulo K: Documentación Técnica**
- `GET /api/v1/qms/documentation-registry?projectId={id}`
- `GET /api/v1/qms/technical-documents?projectId={id}`
- `POST /api/v1/qms/technical-documents`
- **Uso:**
  - Mostrar progreso de documentación (8/10 documentos completos)
  - Lista de documentos con estado
- **Dónde:**
  - Card de métricas: "Documentación: 80% completa"
  - Sección expandible con lista de documentos
- **Nota:** Ya existe pantalla `/technical-docs` - puede integrarse allí también

#### **Módulo L: Gestión de Recursos**
- `GET /api/v1/qms/resource-management?projectId={id}`
- `POST /api/v1/qms/resource-management`
- **Uso:** Mostrar asignación de recursos y score de adecuación
- **Dónde:** Card de métricas: "Recursos: 5 humanos, 3 técnicos"

#### **Módulo M: Accountability Framework**
- `GET /api/v1/qms/accountability-framework?projectId={id}`
- `GET /api/v1/qms/accountability-assignments?projectId={id}`
- `POST /api/v1/qms/accountability-assignments`
- **Uso:** Mostrar responsables asignados por área
- **Dónde:** Sección "Responsabilidades" con lista de asignaciones

---

### **2. Pantalla de Proyectos QMS** (`/governance/compliance/qms/projects`)

**Ubicación:** `app/(app)/governance/compliance/qms/projects/page.tsx`

**Endpoints a usar:**
- **Ninguno directamente** - Esta pantalla lista proyectos, no detalles
- **Pero:** Los botones "Ver Dashboard" y "Revisar Gaps" navegan a pantallas que SÍ usan estos endpoints

---

### **3. Pantalla de Revisión de Conformidad** (`/governance/compliance/conformity-review?projectId=X`)

**Ubicación:** `app/(app)/governance/compliance/conformity-review/page.tsx`

**Endpoints a usar:**

#### **Módulo I: Incidentes Graves**
- `GET /api/v1/qms/serious-incidents?projectId={id}`
- **Uso:** Mostrar incidentes graves relacionados con los gaps detectados
- **Dónde:** Sección adicional "Incidentes Graves Relacionados"

#### **Módulo J: Comunicaciones con Autoridades**
- `GET /api/v1/qms/authority-communications?projectId={id}`
- **Uso:** Mostrar si hay comunicaciones pendientes relacionadas con la revisión
- **Dónde:** Alerta si hay comunicaciones requeridas

---

## 🆕 NUEVAS PANTALLAS RECOMENDADAS

### **1. Pantalla de Detalle por Módulo** (`/governance/compliance/qms/modules/[module]?projectId=X`)

**Propósito:** Mostrar detalles completos de un módulo específico

**Estructura sugerida:**
```
/governance/compliance/qms/modules/
  ├── data-management?projectId=X      (Módulo F)
  ├── risk-management?projectId=X     (Módulo G)
  ├── post-market-monitoring?projectId=X (Módulo H)
  ├── serious-incidents?projectId=X    (Módulo I)
  ├── authority-communications?projectId=X (Módulo J)
  ├── technical-documents?projectId=X  (Módulo K)
  ├── resource-management?projectId=X  (Módulo L)
  └── accountability?projectId=X      (Módulo M)
```

**Ejemplo: Pantalla de Gestión de Riesgos**
```typescript
// app/(app)/governance/compliance/qms/modules/risk-management/page.tsx
export default function RiskManagementModulePage() {
  const searchParams = useSearchParams();
  const projectId = parseInt(searchParams.get("projectId") || "1");

  // Cargar datos del módulo
  useEffect(() => {
    // GET /api/v1/qms/risk-management?projectId={id}
    // GET /api/v1/qms/risks?projectId={id}
  }, [projectId]);

  // Formulario para registrar nuevo riesgo
  const handleRegisterRisk = async (riskData) => {
    // POST /api/v1/qms/risks
  };

  return (
    <div>
      <h1>Gestión de Riesgos (Módulo G)</h1>
      {/* Métricas del sistema */}
      {/* Lista de riesgos */}
      {/* Formulario para agregar riesgo */}
    </div>
  );
}
```

**Endpoints por pantalla:**

| Módulo | Pantalla | GET Endpoints | POST Endpoints |
|--------|----------|---------------|----------------|
| F | `/modules/data-management` | `/data-management` | - |
| G | `/modules/risk-management` | `/risk-management`, `/risks` | `/risks` |
| H | `/modules/post-market-monitoring` | `/post-market-monitoring` | - |
| I | `/modules/serious-incidents` | `/serious-incidents` | `/serious-incidents` |
| J | `/modules/authority-communications` | `/authority-communications` | `/authority-communications` |
| K | `/modules/technical-documents` | `/documentation-registry`, `/technical-documents` | `/technical-documents` |
| L | `/modules/resource-management` | `/resource-management` | `/resource-management` |
| M | `/modules/accountability` | `/accountability-framework`, `/accountability-assignments` | `/accountability-assignments` |

---

### **2. Modal/Formulario para Gestión de Datos del Módulo**

**Propósito:** Permitir editar/agregar datos de un módulo sin cambiar de pantalla

**Uso en:** Pantalla principal QMS o pantalla de detalle del módulo

**Ejemplo: Modal para Registrar Incidente Grave**
```typescript
// Componente: SeriousIncidentModal.tsx
const SeriousIncidentModal = ({ projectId, onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    incidentType: "",
    incidentDescription: "",
    incidentSeverity: "MEDIUM"
  });

  const handleSubmit = async () => {
    const response = await fetch("/api/governance/compliance/qms/serious-incidents", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        projectId,
        ...formData
      })
    });

    if (response.ok) {
      onSuccess();
      onClose();
    }
  };

  return (
    <Dialog>
      <DialogTitle>Registrar Incidente Grave</DialogTitle>
      {/* Formulario */}
      <Button onClick={handleSubmit}>Registrar</Button>
    </Dialog>
  );
};
```

---

## 🔗 INTEGRACIÓN CON PANTALLAS EXISTENTES

### **Pantalla de Incidentes** (`/governance/compliance/incidents`)

**Integración:**
- Usar `GET /api/v1/qms/serious-incidents?projectId={id}` para mostrar incidentes graves QMS
- Filtrar/agrupar por tipo: "QMS Serious Incident" vs "General Incident"
- Usar `POST /api/v1/qms/serious-incidents` para registrar desde esta pantalla

### **Pantalla de Post-Market Monitoring** (`/governance/compliance/post-market-monitoring`)

**Integración:**
- Usar `GET /api/v1/qms/post-market-monitoring?projectId={id}` para mostrar métricas QMS
- Integrar datos QMS con datos existentes de PMM

### **Pantalla de Technical Docs** (`/governance/compliance/technical-docs`)

**Integración:**
- Usar `GET /api/v1/qms/technical-documents?projectId={id}` para mostrar documentos QMS
- Usar `GET /api/v1/qms/documentation-registry?projectId={id}` para mostrar progreso
- Usar `POST /api/v1/qms/technical-documents` para registrar documentos QMS

---

## 📋 PLAN DE IMPLEMENTACIÓN RECOMENDADO

### **Fase 1: Integración en Pantalla Principal (Prioridad Alta)**
1. ✅ Agregar cards de métricas para módulos F-M
2. ✅ Mostrar datos básicos (scores, conteos)
3. ✅ Agregar enlaces a pantallas de detalle

### **Fase 2: Pantallas de Detalle por Módulo (Prioridad Media)**
1. Crear estructura `/modules/[module]`
2. Implementar pantallas para módulos más críticos:
   - Módulo I (Serious Incidents) - **CRÍTICO** (Art. 73)
   - Módulo G (Risk Management) - **ALTO**
   - Módulo K (Technical Documents) - **ALTO**
3. Implementar resto de módulos

### **Fase 3: Modales y Formularios (Prioridad Baja)**
1. Crear componentes modales para cada módulo
2. Integrar en pantalla principal y pantallas de detalle
3. Agregar validaciones y feedback

### **Fase 4: Integración con Pantallas Existentes (Prioridad Media)**
1. Integrar endpoints en `/incidents`
2. Integrar endpoints en `/post-market-monitoring`
3. Integrar endpoints en `/technical-docs`

---

## 💡 EJEMPLOS DE CÓDIGO

### **Ejemplo 1: Agregar Card de Riesgos en Dashboard Principal**

```typescript
// En app/(app)/governance/compliance/qms/page.tsx

const [riskData, setRiskData] = useState(null);

useEffect(() => {
  const loadRiskData = async () => {
    const projectId = getProjectId();
    const response = await fetch(
      `/api/governance/compliance/qms/risk-management?projectId=${projectId}`
    );
    if (response.ok) {
      const data = await response.json();
      setRiskData(data);
    }
  };
  loadRiskData();
}, [searchParams]);

// En el JSX, agregar card:
<MetricCard
  title={t("governance.qms.riskManagement", "Gestión de Riesgos")}
  value={`${riskData?.identifiedRisks || 0} / ${riskData?.mitigatedRisks || 0}`}
  description={`Score: ${((riskData?.overallRiskScore || 0) * 100).toFixed(0)}%`}
  icon={<AlertTriangle className="w-5 h-5" />}
  color="orange"
/>
```

### **Ejemplo 2: Lista de Incidentes Graves con Alerta**

```typescript
const [seriousIncidents, setSeriousIncidents] = useState([]);

useEffect(() => {
  const loadIncidents = async () => {
    const projectId = getProjectId();
    const response = await fetch(
      `/api/governance/compliance/qms/serious-incidents?projectId=${projectId}`
    );
    if (response.ok) {
      const data = await response.json();
      setSeriousIncidents(data);
    }
  };
  loadIncidents();
}, [searchParams]);

// Alerta si hay incidentes sin notificar
{seriousIncidents.filter(i => !i.authorityNotified).length > 0 && (
  <Alert variant="destructive">
    <AlertTriangle className="h-4 w-4" />
    <AlertTitle>Acción Requerida</AlertTitle>
    <AlertDescription>
      Hay {seriousIncidents.filter(i => !i.authorityNotified).length} incidente(s) grave(s)
      que deben notificarse a autoridades en 15 días (Art. 73 EU AI Act)
    </AlertDescription>
  </Alert>
)}
```

### **Ejemplo 3: Formulario para Registrar Riesgo**

```typescript
const RiskRegistrationForm = ({ projectId, onSuccess }) => {
  const [formData, setFormData] = useState({
    riskDescription: "",
    riskSeverity: "MEDIUM",
    riskLikelihood: "MEDIUM"
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch("/api/governance/compliance/qms/risks", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        projectId,
        ...formData
      })
    });

    if (response.ok) {
      onSuccess();
      // Reset form
      setFormData({
        riskDescription: "",
        riskSeverity: "MEDIUM",
        riskLikelihood: "MEDIUM"
      });
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <Input
        label="Descripción del Riesgo"
        value={formData.riskDescription}
        onChange={(e) => setFormData({...formData, riskDescription: e.target.value})}
        required
      />
      <Select
        label="Severidad"
        value={formData.riskSeverity}
        onChange={(value) => setFormData({...formData, riskSeverity: value})}
        options={["LOW", "MEDIUM", "HIGH", "CRITICAL"]}
      />
      <Select
        label="Probabilidad"
        value={formData.riskLikelihood}
        onChange={(value) => setFormData({...formData, riskLikelihood: value})}
        options={["LOW", "MEDIUM", "HIGH"]}
      />
      <Button type="submit">Registrar Riesgo</Button>
    </form>
  );
};
```

---

## 🎨 DISEÑO UI/UX RECOMENDADO

### **Cards de Métricas en Dashboard**
- Usar `MetricCard` component existente
- Colores según score:
  - Verde: >= 85%
  - Amarillo: 70-84%
  - Rojo: < 70%

### **Listas de Items**
- Usar `Card` con `CardBody` scrollable
- Badges para estados (severidad, completado, etc.)
- Botones de acción inline (editar, eliminar, ver detalles)

### **Formularios**
- Usar `Dialog` o `Sheet` para modales
- Validación en tiempo real
- Feedback visual (toast notifications)

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla Principal QMS**
- [ ] Agregar card Módulo F (Data Management)
- [ ] Agregar card Módulo G (Risk Management)
- [ ] Agregar card Módulo H (Post-Market Monitoring)
- [ ] Agregar alerta Módulo I (Serious Incidents)
- [ ] Agregar sección Módulo J (Authority Communications)
- [ ] Agregar card Módulo K (Technical Documents)
- [ ] Agregar card Módulo L (Resource Management)
- [ ] Agregar sección Módulo M (Accountability)

### **Nuevas Pantallas de Detalle**
- [ ] Crear estructura `/modules/[module]`
- [ ] Implementar pantalla Módulo G (Risk Management)
- [ ] Implementar pantalla Módulo I (Serious Incidents)
- [ ] Implementar pantalla Módulo K (Technical Documents)
- [ ] Implementar resto de módulos

### **Integración con Pantallas Existentes**
- [ ] Integrar en `/incidents`
- [ ] Integrar en `/post-market-monitoring`
- [ ] Integrar en `/technical-docs`

---

## 📚 REFERENCIAS

- **Documentación Endpoints:** `docs/prompts/compliance/QMS_ENDPOINTS_F_M.md`
- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Pantalla Principal QMS:** `app/(app)/governance/compliance/qms/page.tsx`
- **Pantalla Proyectos QMS:** `app/(app)/governance/compliance/qms/projects/page.tsx`
