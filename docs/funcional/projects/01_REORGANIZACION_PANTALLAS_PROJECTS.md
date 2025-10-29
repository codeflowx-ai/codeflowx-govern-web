# 📊 PROJECTS - REORGANIZACIÓN DE PANTALLAS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Análisis y reorganización de pantallas del módulo Projects

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Projects** gestiona **proyectos, recursos, costos y portfolios** de la organización. Se han identificado y analizado **49 pantallas ZUL** distribuidas en **3 directorios** principales.

### **Distribución de Pantallas:**
- **Platform Projects:** 46 pantallas (gestión completa)
- **Gobierno Projects:** 3 pantallas (governance)
- **StepApp DevOp:** 2 pantallas (desarrollo)

### **Total:** 49 pantallas ZUL implementadas

---

## 📁 ESTRUCTURA ACTUAL DE DIRECTORIOS

```
webapp/console/
├── platform/projects/          (46 pantallas)
│   ├── Core/                   (4 pantallas)
│   ├── Team/                   (4 pantallas)
│   ├── Resources/              (6 pantallas)
│   ├── Financial/              (14 pantallas)
│   ├── Planning/               (8 pantallas)
│   ├── Technology/             (6 pantallas)
│   └── Management/             (4 pantallas)
├── gobierno/projects/          (3 pantallas)
│   └── Governance/             (3 pantallas)
└── stepApp/devop/             (2 pantallas)
    └── Development/            (2 pantallas)
```

---

## 📊 ANÁLISIS DETALLADO POR MÓDULO

### **1. CORE - Gestión Básica de Proyectos**
**Directorio:** `platform/projects/`  
**Pantallas:** 4

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-overview.zul` | Vista general de proyectos |
| 2 | `project-detail.zul` | Detalle completo de proyecto |
| 3 | `project-domain-overview.zul` | Dominios de proyectos |
| 4 | `project-domain-detail.zul` | Detalle de dominios |

**Funcionalidades:**
- ✅ CRUD completo de proyectos
- ✅ Gestión de dominios
- ✅ Información general
- ✅ Dashboard ejecutivo

---

### **2. TEAM - Gestión de Equipo**
**Directorio:** `platform/projects/`  
**Pantallas:** 4

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-member-overview.zul` | Miembros del equipo |
| 2 | `project-member-detail.zul` | Detalle de miembro |
| 3 | `project-resource-allocation-overview.zul` | Asignación de recursos |
| 4 | `project-time-tracking-overview.zul` | Seguimiento de tiempo |

**Funcionalidades:**
- ✅ Gestión de miembros
- ✅ Asignación de recursos
- ✅ Tracking de tiempo
- ✅ Roles y permisos

---

### **3. RESOURCES - Gestión de Recursos**
**Directorio:** `platform/projects/`  
**Pantallas:** 6

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-resource-consumption-overview.zul` | Consumo de recursos |
| 2 | `project-resource-consumption-detail.zul` | Detalle de consumo |
| 3 | `project-artifact-overview.zul` | Artefactos del proyecto |
| 4 | `project-artifact-detail.zul` | Detalle de artefactos |
| 5 | `project-document-overview.zul` | Documentos del proyecto |
| 6 | `project-document-detail.zul` | Detalle de documentos |

**Funcionalidades:**
- ✅ Monitoreo de consumo
- ✅ Gestión de artefactos
- ✅ Gestión documental
- ✅ Control de recursos

---

### **4. FINANCIAL - Gestión Financiera**
**Directorio:** `platform/projects/`  
**Pantallas:** 14

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-financial-summary-overview.zul` | Resumen financiero |
| 2 | `project-cost-breakdown-overview.zul` | Desglose de costos |
| 3 | `project-cost-estimator-overview.zul` | Estimador de costos |
| 4 | `project-cost-estimator-detail.zul` | Detalle estimación |
| 5 | `project-billing-status-overview.zul` | Estado de facturación |
| 6 | `project-billing-detail-overview.zul` | Detalle facturación |
| 7 | `project-billing-detail-detail.zul` | Detalle profundo facturación |
| 8 | `project-invoice-overview.zul` | Facturas del proyecto |
| 9 | `project-invoice-detail.zul` | Detalle de factura |
| 10 | `project-roi-overview.zul` | ROI del proyecto |
| 11 | `project-roi-detail.zul` | Detalle de ROI |
| 12 | `project-roi-analysis-overview.zul` | Análisis de ROI |
| 13 | `project-license-overview.zul` | Licencias del proyecto |
| 14 | `project-license-detail.zul` | Detalle de licencia |

**Funcionalidades:**
- ✅ Control de costos
- ✅ Facturación
- ✅ Análisis de ROI
- ✅ Gestión de licencias
- ✅ Estimación de costos

---

### **5. PLANNING - Planificación y Tareas**
**Directorio:** `platform/projects/`  
**Pantallas:** 8

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-task-overview.zul` | Tareas del proyecto |
| 2 | `project-task-detail.zul` | Detalle de tarea |
| 3 | `project-timeline-gantt-overview.zul` | Timeline Gantt |
| 4 | `project-requirement-overview.zul` | Requisitos del proyecto |
| 5 | `project-requirement-detail.zul` | Detalle de requisito |
| 6 | `project-version-overview.zul` | Versiones del proyecto |
| 7 | `project-version-detail.zul` | Detalle de versión |
| 8 | `project-time-tracking-detail.zul` | Detalle tracking tiempo |

**Funcionalidades:**
- ✅ Gestión de tareas
- ✅ Timeline Gantt
- ✅ Gestión de requisitos
- ✅ Versionado
- ✅ Tracking de tiempo

---

### **6. TECHNOLOGY - Stack Tecnológico**
**Directorio:** `platform/projects/`  
**Pantallas:** 6

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-stack-overview.zul` | Stack tecnológico |
| 2 | `project-stack-detail.zul` | Detalle de stack |
| 3 | `project-technology-overview.zul` | Tecnologías del proyecto |
| 4 | `project-technology-detail.zul` | Detalle de tecnología |
| 5 | `project-token-overview.zul` | Tokens del proyecto |
| 6 | `project-token-detail.zul` | Detalle de token |

**Funcionalidades:**
- ✅ Gestión de stack
- ✅ Catálogo de tecnologías
- ✅ Gestión de tokens
- ✅ Configuración técnica

---

### **7. MANAGEMENT - Gestión Ejecutiva**
**Directorio:** `platform/projects/`  
**Pantallas:** 4

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `project-portfolio-dashboard-overview.zul` | Dashboard de portfolio |
| 2 | `project-risk-assessment-overview.zul` | Evaluación de riesgos |
| 3 | `project-financial-summary-overview.zul` | Resumen financiero ejecutivo |
| 4 | `project-resource-allocation-overview.zul` | Asignación global de recursos |

**Funcionalidades:**
- ✅ Dashboard ejecutivo
- ✅ Análisis de riesgos
- ✅ Portfolio management
- ✅ Reporting ejecutivo

---

### **8. GOVERNANCE - Gobierno de Proyectos**
**Directorio:** `gobierno/projects/`  
**Pantallas:** 3

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `projects-overview.zul` | Vista de governance |
| 2 | `projects-detail.zul` | Detalle de governance |
| 3 | `projects-dashboard.zul` | Dashboard de governance |

**Funcionalidades:**
- ✅ Compliance de proyectos
- ✅ Auditoría de proyectos
- ✅ Políticas de governance
- ✅ Reporting de governance

---

### **9. DEVELOPMENT - Desarrollo**
**Directorio:** `stepApp/devop/`  
**Pantallas:** 2

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `proyecto01.zul` | Pantalla desarrollo 1 |
| 2 | `proyecto02.zul` | Pantalla desarrollo 2 |

**Funcionalidades:**
- ✅ Herramientas de desarrollo
- ✅ Gestión de código
- ✅ CI/CD integration
- ✅ DevOps workflows

---

## 📊 ESTADÍSTICAS GENERALES

### **Por Tipo de Pantalla:**
- **Overview (Listados):** 24 pantallas
- **Detail (Detalles):** 23 pantallas
- **Dashboard:** 2 pantallas

### **Por Categoría Funcional:**
- **Financial:** 14 pantallas (29%)
- **Planning:** 8 pantallas (16%)
- **Resources:** 6 pantallas (12%)
- **Technology:** 6 pantallas (12%)
- **Core:** 4 pantallas (8%)
- **Team:** 4 pantallas (8%)
- **Management:** 4 pantallas (8%)
- **Governance:** 3 pantallas (6%)
- **Development:** 2 pantallas (4%)

### **Distribución por Directorio:**
- **Platform:** 46 pantallas (94%)
- **Gobierno:** 3 pantallas (6%)
- **StepApp:** 2 pantallas (4%)

---

## 🎯 ESTRUCTURA PROPUESTA (REORGANIZADA)

### **Objetivo:** Organizar las 49 pantallas en módulos funcionales claros

```
projects/
├── 01-core/                    (4 pantallas)
│   ├── project-overview.zul
│   ├── project-detail.zul
│   ├── project-domain-overview.zul
│   └── project-domain-detail.zul
│
├── 02-team/                    (4 pantallas)
│   ├── project-member-overview.zul
│   ├── project-member-detail.zul
│   ├── project-resource-allocation-overview.zul
│   └── project-time-tracking-overview.zul
│
├── 03-planning/                (8 pantallas)
│   ├── project-task-overview.zul
│   ├── project-task-detail.zul
│   ├── project-timeline-gantt-overview.zul
│   ├── project-requirement-overview.zul
│   ├── project-requirement-detail.zul
│   ├── project-version-overview.zul
│   ├── project-version-detail.zul
│   └── project-time-tracking-detail.zul
│
├── 04-resources/               (6 pantallas)
│   ├── project-resource-consumption-overview.zul
│   ├── project-resource-consumption-detail.zul
│   ├── project-artifact-overview.zul
│   ├── project-artifact-detail.zul
│   ├── project-document-overview.zul
│   └── project-document-detail.zul
│
├── 05-financial/               (14 pantallas)
│   ├── project-financial-summary-overview.zul
│   ├── project-cost-breakdown-overview.zul
│   ├── project-cost-estimator-overview.zul
│   ├── project-cost-estimator-detail.zul
│   ├── project-billing-status-overview.zul
│   ├── project-billing-detail-overview.zul
│   ├── project-billing-detail-detail.zul
│   ├── project-invoice-overview.zul
│   ├── project-invoice-detail.zul
│   ├── project-roi-overview.zul
│   ├── project-roi-detail.zul
│   ├── project-roi-analysis-overview.zul
│   ├── project-license-overview.zul
│   └── project-license-detail.zul
│
├── 06-technology/              (6 pantallas)
│   ├── project-stack-overview.zul
│   ├── project-stack-detail.zul
│   ├── project-technology-overview.zul
│   ├── project-technology-detail.zul
│   ├── project-token-overview.zul
│   └── project-token-detail.zul
│
├── 07-management/              (4 pantallas)
│   ├── project-portfolio-dashboard-overview.zul
│   ├── project-risk-assessment-overview.zul
│   ├── project-financial-summary-overview.zul
│   └── project-resource-allocation-overview.zul
│
├── 08-governance/              (3 pantallas)
│   ├── projects-overview.zul
│   ├── projects-detail.zul
│   └── projects-dashboard.zul
│
└── 09-development/             (2 pantallas)
    ├── proyecto01.zul
    └── proyecto02.zul
```

---

## 🎯 MAPEO DE MIGRACIÓN

### **Origen → Destino**

#### **Core (4 pantallas):**
```
platform/projects/project-overview.zul                    → 01-core/project-overview.zul
platform/projects/project-detail.zul                      → 01-core/project-detail.zul
platform/projects/project-domain-overview.zul             → 01-core/project-domain-overview.zul
platform/projects/project-domain-detail.zul               → 01-core/project-domain-detail.zul
```

#### **Team (4 pantallas):**
```
platform/projects/project-member-overview.zul             → 02-team/project-member-overview.zul
platform/projects/project-member-detail.zul               → 02-team/project-member-detail.zul
platform/projects/project-resource-allocation-overview.zul → 02-team/project-resource-allocation-overview.zul
platform/projects/project-time-tracking-overview.zul      → 02-team/project-time-tracking-overview.zul
```

#### **Planning (8 pantallas):**
```
platform/projects/project-task-overview.zul               → 03-planning/project-task-overview.zul
platform/projects/project-task-detail.zul                 → 03-planning/project-task-detail.zul
platform/projects/project-timeline-gantt-overview.zul     → 03-planning/project-timeline-gantt-overview.zul
platform/projects/project-requirement-overview.zul        → 03-planning/project-requirement-overview.zul
platform/projects/project-requirement-detail.zul          → 03-planning/project-requirement-detail.zul
platform/projects/project-version-overview.zul            → 03-planning/project-version-overview.zul
platform/projects/project-version-detail.zul              → 03-planning/project-version-detail.zul
platform/projects/project-time-tracking-detail.zul        → 03-planning/project-time-tracking-detail.zul
```

#### **Resources (6 pantallas):**
```
platform/projects/project-resource-consumption-overview.zul → 04-resources/project-resource-consumption-overview.zul
platform/projects/project-resource-consumption-detail.zul   → 04-resources/project-resource-consumption-detail.zul
platform/projects/project-artifact-overview.zul             → 04-resources/project-artifact-overview.zul
platform/projects/project-artifact-detail.zul               → 04-resources/project-artifact-detail.zul
platform/projects/project-document-overview.zul             → 04-resources/project-document-overview.zul
platform/projects/project-document-detail.zul               → 04-resources/project-document-detail.zul
```

#### **Financial (14 pantallas):**
```
platform/projects/project-financial-summary-overview.zul    → 05-financial/project-financial-summary-overview.zul
platform/projects/project-cost-breakdown-overview.zul       → 05-financial/project-cost-breakdown-overview.zul
platform/projects/project-cost-estimator-overview.zul       → 05-financial/project-cost-estimator-overview.zul
platform/projects/project-cost-estimator-detail.zul         → 05-financial/project-cost-estimator-detail.zul
platform/projects/project-billing-status-overview.zul       → 05-financial/project-billing-status-overview.zul
platform/projects/project-billing-detail-overview.zul       → 05-financial/project-billing-detail-overview.zul
platform/projects/project-billing-detail-detail.zul         → 05-financial/project-billing-detail-detail.zul
platform/projects/project-invoice-overview.zul              → 05-financial/project-invoice-overview.zul
platform/projects/project-invoice-detail.zul                → 05-financial/project-invoice-detail.zul
platform/projects/project-roi-overview.zul                  → 05-financial/project-roi-overview.zul
platform/projects/project-roi-detail.zul                    → 05-financial/project-roi-detail.zul
platform/projects/project-roi-analysis-overview.zul         → 05-financial/project-roi-analysis-overview.zul
platform/projects/project-license-overview.zul              → 05-financial/project-license-overview.zul
platform/projects/project-license-detail.zul                → 05-financial/project-license-detail.zul
```

#### **Technology (6 pantallas):**
```
platform/projects/project-stack-overview.zul              → 06-technology/project-stack-overview.zul
platform/projects/project-stack-detail.zul                → 06-technology/project-stack-detail.zul
platform/projects/project-technology-overview.zul         → 06-technology/project-technology-overview.zul
platform/projects/project-technology-detail.zul           → 06-technology/project-technology-detail.zul
platform/projects/project-token-overview.zul              → 06-technology/project-token-overview.zul
platform/projects/project-token-detail.zul                → 06-technology/project-token-detail.zul
```

#### **Management (4 pantallas):**
```
platform/projects/project-portfolio-dashboard-overview.zul → 07-management/project-portfolio-dashboard-overview.zul
platform/projects/project-risk-assessment-overview.zul     → 07-management/project-risk-assessment-overview.zul
platform/projects/project-financial-summary-overview.zul   → 07-management/project-financial-summary-overview.zul (duplicado)
platform/projects/project-resource-allocation-overview.zul → 07-management/project-resource-allocation-overview.zul (duplicado)
```

#### **Governance (3 pantallas):**
```
gobierno/projects/projects-overview.zul                   → 08-governance/projects-overview.zul
gobierno/projects/projects-detail.zul                     → 08-governance/projects-detail.zul
gobierno/projects/projects-dashboard.zul                  → 08-governance/projects-dashboard.zul
```

#### **Development (2 pantallas):**
```
stepApp/devop/proyecto01.zul                             → 09-development/proyecto01.zul
stepApp/devop/proyecto02.zul                             → 09-development/proyecto02.zul
```

---

## 🎯 CONCLUSIONES

### **Implementación Actual:**
- ✅ **49 pantallas ZUL** completamente implementadas
- ✅ **9 módulos funcionales** bien definidos
- ✅ **Cobertura completa** de project management
- ✅ **Gestión financiera** muy detallada
- ✅ **Integración** con governance y desarrollo

### **Fortalezas:**
- **Gestión financiera completa:** 14 pantallas dedicadas
- **Planning robusto:** Timeline Gantt, tareas, requisitos
- **Portfolio management:** Dashboard ejecutivo
- **Governance integrada:** 3 pantallas específicas

### **Pantallas Clave:**
- **project-portfolio-dashboard-overview.zul:** Dashboard ejecutivo
- **project-timeline-gantt-overview.zul:** Planificación visual
- **project-roi-analysis-overview.zul:** Análisis de retorno
- **project-resource-allocation-overview.zul:** Asignación de recursos

**Total:** 49 pantallas ZUL organizadas en 9 módulos funcionales.
