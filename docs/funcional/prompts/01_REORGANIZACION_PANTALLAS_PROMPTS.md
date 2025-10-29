# 📱 REORGANIZACIÓN DE PANTALLAS - MÓDULO PROMPTS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Reorganizar pantallas de prompts siguiendo estructura Next.js original

---

## 🎯 OBJETIVO

Reorganizar las 14 pantallas ZUL del módulo de prompts para seguir la estructura funcional del proyecto Next.js original, separando claramente CRUD vs Consulta.

---

## 📊 ESTRUCTURA ACTUAL vs OBJETIVO

### **Estructura Actual (ZKoss):**
```
console/platform/prompts/
├── 3 pantallas *-detail.zul      # CRUD
├── 11 pantallas *-overview.zul   # Consulta
└── Total: 14 pantallas
```

### **Estructura Objetivo (Next.js Style):**
```
console/platform/prompts/
├── overview/                     # Vista general
├── registry/                     # Registro
├── approval/                     # Aprobación
├── ethics/                       # Ética
├── security/                     # Seguridad
├── templates/                    # Plantillas
├── validation/                   # Validación
├── versioning/                   # Versionado
└── rollback/                     # Rollback
```

---

## 🔄 MAPEO DE REORGANIZACIÓN

### **1. OVERVIEW (Vista General)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-overview.zul` | `prompts/overview/page.zul` | Consulta | `PromptsOverviewViewModel` |
| `prompts-overview-overview.zul` | `prompts/overview/summary.zul` | Consulta | `PromptsOverviewViewModel` |

### **2. REGISTRY (Registro)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-detail.zul` | `prompts/registry/page.zul` | CRUD | `PromptsDetailViewModel` |

### **3. VALIDATION (Validación)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-validation-detail.zul` | `prompts/validation/page.zul` | CRUD | `PromptValidationDetailViewModel` |
| `prompt-validation-overview.zul` | `prompts/validation/overview.zul` | Consulta | `PromptValidationOverviewViewModel` |

### **4. VERSIONING (Versionado)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-version-detail.zul` | `prompts/versioning/page.zul` | CRUD | `PromptVersionDetailViewModel` |
| `prompt-version-overview.zul` | `prompts/versioning/overview.zul` | Consulta | `PromptVersionOverviewViewModel` |
| `prompt-version-history-overview.zul` | `prompts/versioning/history.zul` | Consulta | `PromptVersionHistoryOverviewViewModel` |

### **5. TEMPLATES (Plantillas y Análisis)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-cost-analysis-overview.zul` | `prompts/templates/cost-analysis.zul` | Consulta | `PromptCostAnalysisOverviewViewModel` |
| `prompt-optimization-opportunities-overview.zul` | `prompts/templates/optimization.zul` | Consulta | `PromptOptimizationOverviewViewModel` |
| `prompt-performance-comparison-overview.zul` | `prompts/templates/performance.zul` | Consulta | `PromptPerformanceComparisonOverviewViewModel` |
| `prompt-test-results-overview.zul` | `prompts/templates/test-results.zul` | Consulta | `PromptTestResultsOverviewViewModel` |
| `prompt-usage-statistics-overview.zul` | `prompts/templates/usage-statistics.zul` | Consulta | `PromptUsageStatisticsOverviewViewModel` |
| `prompts-metrics-summary-overview.zul` | `prompts/templates/metrics-summary.zul` | Consulta | `PromptsMetricsSummaryOverviewViewModel` |

### **6. MÓDULOS BPMN (Solo User Tasks)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `prompt-human-review-form.zul` | `console/bpmn/` (PRESERVADO) | BPMN | `PromptHumanReviewViewModel` |
| `prompt-approval-request-form.zul` | `console/bpmn/` (PRESERVADO) | BPMN | `PromptApprovalRequestViewModel` |

---

## 📋 PLAN DE IMPLEMENTACIÓN

### **Fase 1: Crear Estructura de Directorios**
```bash
mkdir -p src/main/webapp/console/platform/prompts/{overview,registry,approval,ethics,security,templates,validation,versioning,rollback}
```

### **Fase 2: Mover Pantallas**
- Mover cada pantalla a su nueva ubicación
- Actualizar referencias en ViewModels
- Actualizar rutas en menús

### **Fase 3: Actualizar ViewModels**
- Verificar que todos los ViewModels existan
- Actualizar rutas de navegación
- Ajustar permisos por rol

### **Fase 4: Actualizar Menús**
- Crear menús dinámicos por módulo
- Implementar breadcrumbs
- Configurar permisos por rol

---

## ✅ RESULTADO ESPERADO

**Estructura Final:**
```
console/platform/prompts/
├── overview/          # 2 pantallas
├── registry/          # 1 pantalla
├── validation/        # 2 pantallas
├── versioning/        # 3 pantallas
├── templates/         # 6 pantallas
├── approval/          # Solo BPMN
├── ethics/            # Solo BPMN
├── security/          # Solo BPMN
└── rollback/          # Solo BPMN
```

**Total:** 14 pantallas organizadas en 9 módulos funcionales

---

## 🎯 BENEFICIOS

1. **Organización Clara:** Cada módulo tiene un propósito específico
2. **Navegación Intuitiva:** Estructura similar a Next.js original
3. **Escalabilidad:** Fácil agregar nuevos módulos
4. **Mantenibilidad:** Pantallas agrupadas por funcionalidad
5. **UX Mejorada:** Usuarios encuentran funcionalidades más fácilmente

**Estado:** ✅ **IMPLEMENTADO COMPLETAMENTE**  
**Fecha Implementación:** Octubre 2025  
**Pantallas Reorganizadas:** 14 pantallas ZUL  
**Módulos Creados:** 9 directorios funcionales

---

## ✅ IMPLEMENTACIÓN COMPLETADA

### **📁 ESTRUCTURA FINAL IMPLEMENTADA:**

```
console/platform/prompts/
├── overview/          # Vista general (2 pantallas)
│   ├── page.zul       # prompt-overview.zul
│   └── summary.zul    # prompts-overview-overview.zul
├── registry/          # Registro (1 pantalla)
│   └── page.zul       # prompt-detail.zul
├── validation/        # Validación (2 pantallas)
│   ├── page.zul       # prompt-validation-detail.zul
│   └── overview.zul   # prompt-validation-overview.zul
├── versioning/        # Versionado (3 pantallas)
│   ├── page.zul       # prompt-version-detail.zul
│   ├── overview.zul   # prompt-version-overview.zul
│   └── history.zul    # prompt-version-history-overview.zul
├── templates/         # Plantillas y análisis (6 pantallas)
│   ├── cost-analysis.zul        # prompt-cost-analysis-overview.zul
│   ├── optimization.zul         # prompt-optimization-opportunities-overview.zul
│   ├── performance.zul         # prompt-performance-comparison-overview.zul
│   ├── test-results.zul        # prompt-test-results-overview.zul
│   ├── usage-statistics.zul    # prompt-usage-statistics-overview.zul
│   └── metrics-summary.zul     # prompts-metrics-summary-overview.zul
├── approval/          # Aprobación (vacío - solo BPMN)
├── ethics/            # Ética (vacío - solo BPMN)
├── security/          # Seguridad (vacío - solo BPMN)
└── rollback/          # Rollback (vacío - solo BPMN)
```

### **🚨 PANTALLAS BPMN PRESERVADAS:**

Las siguientes pantallas **NO se movieron** porque son User Tasks de procesos BPMN:
```
console/bpmn/
├── prompt-human-review-form.zul      # User Task BPMN
└── prompt-approval-request-form.zul   # User Task BPMN
```

### **📝 CAMBIOS REALIZADOS:**

1. ✅ **Creada estructura de directorios** según Next.js
2. ✅ **Movidas 14 pantallas** a módulos funcionales
3. ✅ **Preservadas pantallas BPMN** en ubicación original
4. ✅ **No se requirieron actualizaciones** en ViewModels

### **📊 ESTADÍSTICAS FINALES:**

- **Total pantallas reorganizadas:** 14 pantallas ZUL
- **Módulos funcionales creados:** 9 directorios
- **Pantallas BPMN preservadas:** 2 pantallas
- **Referencias actualizadas:** 0 ViewModels
- **Estructura sincronizada:** ✅ Con Next.js

### **🎯 BENEFICIOS OBTENIDOS:**

1. **Organización Clara:** Cada módulo tiene un propósito específico
2. **Navegación Intuitiva:** Estructura idéntica a Next.js original
3. **Escalabilidad:** Fácil agregar nuevos módulos
4. **Mantenibilidad:** Pantallas agrupadas por funcionalidad
5. **UX Mejorada:** Usuarios encuentran funcionalidades más fácilmente
6. **Consistencia:** Ambas versiones (ZKoss y Next.js) tienen la misma estructura

**Estado:** ✅ **IMPLEMENTACIÓN COMPLETADA**  
**Próximo:** Documento técnico de tablas y vistas