# GUÍA AGENTE 6 - FRONTEND Y VISUALIZACIONES

**Agente:** Frontend + Backend
**Equipo:** Equipo 4 - Frontend y Visualizaciones
**Duración:** 10-12 horas
**Objetivo:** Implementar visualizaciones, exportaciones y búsquedas avanzadas

---

## 📋 INCIDENCIAS ASIGNADAS

| ID | Descripción | Esfuerzo | Prioridad | Estado Inicial |
|----|-------------|----------|-----------|----------------|
| **INC-003-DS** | Visualizaciones gráficas bias | 3h | 🟡 ALTA | 🔴 PENDIENTE |
| **INC-008-DS** | Exportación reportes | 2h | 🟡 MEDIA | 🔴 PENDIENTE |
| **INC-018** | Búsqueda avanzada logs | 2h | 🟡 MEDIA | 🔴 PENDIENTE |
| **INC-016** | Exportación árbol riesgo | 2h | 🟢 BAJA | 🔴 PENDIENTE |
| **INC-024** | Reporte ejecutivo consolidado | 2h | 🟢 BAJA | 🔴 PENDIENTE |

**Total:** 5 incidencias, ~11 horas

---

## 📚 DOCUMENTOS DE REFERENCIA

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`
- **Distribución:** `/docs/compliance/revision/DISTRIBUCION_INCIDENCIAS_AGENTES.md`

### Referencias Técnicas:
- **ViewModels:** `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/`
- **Pantallas ZUL:** `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/`
- **BusinessServices:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "ARQUITECTURA ENART" para convenciones completas.**

**Resumen rápido:**
- Prefijos de 3 caracteres para tablas
- PK autonumérica siempre
- Tercera forma normal
- KISS y SOLID
- Arquitectura hexagonal

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **ViewModels:**
```
/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/
├── compliance/          ← ViewModels de compliance
│   ├── BiasVisualizationViewModel.java  ← CREAR (INC-003-DS)
│   ├── LogSearchViewModel.java         ← CREAR (INC-018)
│   └── RiskTreeExportViewModel.java    ← CREAR (INC-016)
```

### **Pantallas ZUL:**
```
/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/
├── gobierno/
│   ├── compliance/
│   │   ├── bias-visualization.zul      ← CREAR (INC-003-DS)
│   │   ├── log-search-advanced.zul     ← CREAR (INC-018)
│   │   └── risk-tree-export.zul       ← CREAR (INC-016)
│   └── reports/
│       └── executive-report.zul       ← CREAR (INC-024)
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── compliance/
│   ├── BiasVisualizationService.java  ← CREAR (INC-003-DS)
│   ├── LogSearchService.java          ← CREAR (INC-018)
│   ├── ReportExportService.java       ← CREAR (INC-008-DS)
│   └── ExecutiveReportService.java    ← CREAR (INC-024)
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-003-DS: Visualizaciones Gráficas Bias**

#### **Archivos a Crear:**

1. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/BiasVisualizationService.java`

2. **ViewModel:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/BiasVisualizationViewModel.java`

3. **Pantalla ZUL:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/gobierno/compliance/bias-visualization.zul`

#### **Funcionalidades:**
- Histogramas de distribución de bias
- Box plots para comparación de grupos
- Gráficos de tendencias temporales
- Exportación de gráficos (PNG, SVG)

#### **Checklist:**
- [ ] Crear BusinessService según prompt
- [ ] Crear ViewModel con datos para gráficos
- [ ] Crear pantalla ZUL con componentes de gráficos
- [ ] Integrar con `BiasDetectionService` existente
- [ ] Probar visualizaciones
- [ ] Documentar BusinessService

---

### **INC-008-DS: Exportación Reportes**

#### **Archivos a Crear:**

1. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ReportExportService.java`

2. **Modificar ViewModels existentes:**
   - Añadir botones de exportación en dashboards relevantes

#### **Formatos:**
- **PDF:** Usar microservicio `codeflowx-governance-documentation`
- **Excel:** Apache POI
- **JSON:** Serialización estándar

#### **Checklist:**
- [ ] Crear BusinessService según prompt
- [ ] Implementar exportación PDF (usar microservicio)
- [ ] Implementar exportación Excel (Apache POI)
- [ ] Implementar exportación JSON
- [ ] Añadir botones de exportación en dashboards
- [ ] Probar todos los formatos
- [ ] Documentar BusinessService

---

### **INC-018: Búsqueda Avanzada Logs**

#### **Archivos a Crear:**

1. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/LogSearchService.java`

2. **ViewModel:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/LogSearchViewModel.java`

3. **Pantalla ZUL:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/gobierno/compliance/log-search-advanced.zul`

#### **Funcionalidades:**
- Filtros múltiples (fecha, nivel, proyecto, usuario)
- Búsqueda por texto libre
- Paginación de resultados
- Exportación de resultados

#### **Checklist:**
- [ ] Crear BusinessService según prompt
- [ ] Crear ViewModel con filtros
- [ ] Crear pantalla ZUL con formulario de búsqueda
- [ ] Integrar con `ImmutableLoggingBusinessService`
- [ ] Implementar paginación
- [ ] Probar búsquedas complejas
- [ ] Documentar BusinessService

---

### **INC-016: Exportación Árbol Riesgo**

#### **Archivos a Crear:**

1. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/RiskTreeExportService.java`

2. **ViewModel:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/RiskTreeExportViewModel.java`

3. **Pantalla ZUL:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/gobierno/compliance/risk-tree-export.zul`

#### **Formatos:**
- **PDF:** Usar microservicio documentación
- **PNG:** Renderizado de árbol
- **JSON:** Estructura del árbol

#### **Checklist:**
- [ ] Crear BusinessService según prompt
- [ ] Crear ViewModel
- [ ] Crear pantalla ZUL
- [ ] Implementar exportación PDF
- [ ] Implementar exportación PNG
- [ ] Implementar exportación JSON
- [ ] Probar todos los formatos
- [ ] Documentar BusinessService

---

### **INC-024: Reporte Ejecutivo Consolidado**

#### **Archivos a Crear:**

1. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ExecutiveReportService.java`
   - **Nota:** Verificar si ya existe y solo modificar

2. **ViewModel:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/ExecutiveReportViewModel.java`

3. **Pantalla ZUL:**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/gobierno/reports/executive-report.zul`

#### **Funcionalidades:**
- Resumen ejecutivo de compliance
- KPIs principales
- Gráficos consolidados
- Exportación PDF

#### **Checklist:**
- [ ] Verificar si `ExecutiveReportService` existe
- [ ] Crear o modificar BusinessService
- [ ] Crear ViewModel
- [ ] Crear pantalla ZUL
- [ ] Integrar con múltiples servicios de compliance
- [ ] Generar PDF usando microservicio
- [ ] Probar reporte completo
- [ ] Documentar BusinessService

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Leer prompts completos (si existen)
- [ ] Verificar servicios existentes relacionados
- [ ] Revisar ViewModels similares como referencia

### **Durante Implementación:**
- [ ] Seguir convenciones EnArt
- [ ] Integrar con servicios existentes
- [ ] Usar microservicio de documentación para PDFs
- [ ] Probar visualizaciones en diferentes navegadores

### **Después de Implementación:**
- [ ] Compilar sin errores
- [ ] Probar todas las funcionalidades
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`
- [ ] Documentar BusinessServices en `/docs/developers/`
- [ ] Registrar archivos creados en este documento

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

### **INC-003-DS: Visualizaciones Gráficas Bias**

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**
- [ ] BusinessService: `BiasVisualizationService.java`
- [ ] ViewModel: `BiasVisualizationViewModel.java`
- [ ] Pantalla ZUL: `bias-visualization.zul`

---

### **INC-008-DS: Exportación Reportes**

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**
- [ ] BusinessService: `ReportExportService.java`
- [ ] Modificar ViewModels existentes (añadir botones exportación)

---

### **INC-018: Búsqueda Avanzada Logs**

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**
- [ ] BusinessService: `LogSearchService.java`
- [ ] ViewModel: `LogSearchViewModel.java`
- [ ] Pantalla ZUL: `log-search-advanced.zul`

---

### **INC-016: Exportación Árbol Riesgo**

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**
- [ ] BusinessService: `RiskTreeExportService.java`
- [ ] ViewModel: `RiskTreeExportViewModel.java`
- [ ] Pantalla ZUL: `risk-tree-export.zul`

---

### **INC-024: Reporte Ejecutivo Consolidado**

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**
- [ ] BusinessService: `ExecutiveReportService.java` (verificar si existe)
- [ ] ViewModel: `ExecutiveReportViewModel.java`
- [ ] Pantalla ZUL: `executive-report.zul`

---

## 🔗 INTEGRACIONES REQUERIDAS

### **Microservicio Documentación:**
- Usar `codeflowx-governance-documentation` para generación de PDFs
- Endpoint: `POST /api/documentation/generate-report`
- Ver: `/docs/compliance/PROMPTS_06_MICROSERVICIO_DOCUMENTACION_JAVA.md`

### **Servicios Existentes:**
- `BiasDetectionService` - Para datos de bias
- `ImmutableLoggingBusinessService` - Para búsqueda de logs
- `ComplianceDashboardService` - Para datos de compliance

---

**Última Actualización:** 25 de noviembre de 2025
