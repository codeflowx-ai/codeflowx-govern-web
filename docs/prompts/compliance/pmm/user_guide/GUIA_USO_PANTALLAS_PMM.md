# 📱 GUÍA DE USO - PANTALLAS PMM

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo PMM](#acceso-al-módulo-pmm)
2. [Dashboard PMM](#dashboard-pmm)
3. [Gestión de Incidentes](#gestión-de-incidentes)
4. [Acciones Correctoras](#acciones-correctoras)
5. [Planes PMM](#planes-pmm)
6. [Reportes de Vigilancia](#reportes-de-vigilancia)
7. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO PMM

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **Post-Market Monitoring** en el sidebar
3. Se abrirá el Dashboard PMM principal

### Navegación Contextual por Proyecto

**IMPORTANTE:** Las pantallas de **Incidentes** y **Acciones Correctoras** ya no aparecen en el sidebar. Se acceden desde el Dashboard PMM:

1. Ir al Dashboard PMM
2. En la sección "Sistemas en Producción", encontrar el proyecto deseado
3. Hacer clic en los botones de acción del proyecto:
   - **Ver Incidentes** → Abre incidentes filtrados por ese proyecto
   - **Ver Acciones** → Abre acciones correctoras filtradas por ese proyecto
   - **Ver Planes** → Abre planes PMM filtrados por ese proyecto
   - **Ver Reportes** → Abre reportes filtrados por ese proyecto

**Ventaja:** Al acceder desde el proyecto, la pantalla se filtra automáticamente, evitando tener que seleccionar el proyecto manualmente.

---

## 📊 DASHBOARD PMM

### Ubicación
`/governance/compliance/post-market-monitoring`

### Descripción
Vista centralizada con métricas principales y sistemas monitoreados.

### Componentes

#### 1. **Métricas Principales (4 Contadores)**
- **Sistemas Monitoreados:** Total de sistemas en producción bajo monitoreo
- **Incidentes Activos:** Número de incidentes abiertos o en investigación
- **Acciones Pendientes:** Acciones correctoras en estado PLANNED o IN_PROGRESS
- **Cumplimiento SLA:** Porcentaje de cumplimiento de SLAs de respuesta

#### 2. **Filtros**
- **Proyecto:** Filtrar por proyecto específico (opcional)
- **Tipo de Métrica:** Filtrar por tipo (PERFORMANCE, DRIFT, BIAS, ACCURACY, LATENCY)

#### 3. **Acciones Rápidas**
Botones para acceso rápido a todas las secciones (sin filtro de proyecto):
- Ver Incidentes
- Ver Acciones Correctoras
- Ver Planes PMM
- Ver Reportes

#### 4. **Sistemas en Producción**
Grid de 2 columnas mostrando cada sistema/proyecto:

**Información Mostrada:**
- **Nombre del Proyecto**
- **Estado:** HEALTHY (verde), WARNING (amarillo), CRITICAL (rojo)
- **Badges:** Drift Detectado, Anomalías (si aplica)
- **Métricas:**
  - Precisión (accuracy)
  - Latencia (ms)
  - Throughput (req/s)
- **Última Verificación:** Fecha y hora del último check

**Botones de Acción (por proyecto):**
- **Ver Incidentes** → `/governance/compliance/incidents?projectId=X`
- **Ver Acciones** → `/governance/compliance/corrective-actions?projectId=X`
- **Ver Planes** → `/governance/compliance/post-market-monitoring/plans?projectId=X`
- **Ver Reportes** → `/governance/compliance/post-market-monitoring/reports?projectId=X`

---

## 🚨 GESTIÓN DE INCIDENTES

### Ubicación
`/governance/compliance/incidents`
`/governance/compliance/incidents?projectId=X` (filtrado por proyecto)

### Descripción
Pantalla para reportar, consultar y gestionar incidentes detectados en sistemas de IA.

### Componentes Principales

#### 1. **Header**
- Título: "Gestión de Incidentes"
- Subtítulo: Descripción del módulo
- Botón: **"Reportar Incidente"** (abre diálogo)

#### 2. **Filtros**
- **Búsqueda:** Buscar por texto en descripción
- **Severidad:** LOW, MEDIUM, HIGH, CRITICAL, Todas
- **Estado:** OPEN, INVESTIGATING, RESOLVED, CLOSED, Todos
- **Proyecto:**
  - Si viene de URL (`?projectId=X`): Muestra nombre del proyecto (no editable)
  - Si no: Selector con todos los proyectos
- **Botón Limpiar:** Resetea todos los filtros

#### 3. **Lista de Incidentes**
Tabla con columnas:
- **ID**
- **Proyecto:** Nombre del proyecto afectado
- **Severidad:** Badge con color según severidad
- **Descripción:** Texto del incidente
- **Fecha Reporte:** Cuándo se reportó
- **Estado:** Badge con estado actual
- **Notificado:** Indicador si se notificó a autoridades
- **Acciones:**
  - **Ver Detalles** (👁️): Abre diálogo con información completa
  - **Notificar Autoridades** (🔔): Solo para incidentes HIGH/CRITICAL no notificados

### Funcionalidades

#### Reportar Nuevo Incidente

1. Hacer clic en **"Reportar Incidente"**
2. Completar formulario:
   - **Proyecto Afectado:** Selector con formato "ID - Nombre Proyecto"
     - Si viene de URL, ya está pre-seleccionado
   - **Severidad:** LOW, MEDIUM, HIGH, CRITICAL
   - **Descripción:** Detalles del incidente
   - **Impacto Estimado:** Impacto esperado
3. Hacer clic en **"Reportar"**
4. Si severidad es HIGH o CRITICAL → Notificación automática a autoridades (Art. 20.1)

#### Ver Detalles de Incidente

1. Hacer clic en **👁️ Ver** en cualquier incidente
2. Se abre diálogo con:
   - Información completa del incidente
   - Estado actual
   - Severidad
   - Descripción completa
   - Impacto
   - Fecha de notificación a autoridades (si aplica)
   - Análisis de causa raíz (si existe)

#### Notificar Autoridades

1. Solo disponible para incidentes HIGH o CRITICAL no notificados
2. Hacer clic en **🔔 Notificar autoridades**
3. Sistema envía notificación automática según Art. 20.1 y Art. 73

---

## ✅ ACCIONES CORRECTORAS

### Ubicación
`/governance/compliance/corrective-actions`
`/governance/compliance/corrective-actions?projectId=X` (filtrado por proyecto)

### Descripción
Gestión de acciones correctoras vinculadas a incidentes para resolver problemas detectados.

### Componentes Principales

#### 1. **Header**
- Título: "Acciones Correctoras"
- Subtítulo: Descripción del módulo
- Botón: **"Crear Acción"** (abre diálogo)

#### 2. **Filtros**
- **Búsqueda:** Buscar por texto en descripción
- **Estado:** PLANNED, IN_PROGRESS, COMPLETED, Todos
- **Incidente:** Selector con incidentes disponibles
- **Proyecto:**
  - Si viene de URL (`?projectId=X`): Filtra automáticamente acciones de incidentes de ese proyecto
  - No se muestra selector (filtrado automático)

#### 3. **Lista de Acciones**
Cards con información:
- **ID de Acción**
- **Incidente Vinculado:** "#ID - Proyecto - Descripción..."
- **Descripción:** Detalles de la acción
- **Estado:** Badge con estado actual
- **Fecha Planificada:** Cuándo se planea ejecutar
- **Efectividad:** Si está completada, muestra valor (0.00 - 1.00)
- **Acciones:**
  - **Ver Detalles** (👁️): Abre diálogo con información completa
  - **Evaluar Efectividad** (📊): Solo para acciones COMPLETED

### Funcionalidades

#### Crear Nueva Acción Correctora

1. Hacer clic en **"Crear Acción"**
2. Completar formulario:
   - **Incidente:** Selector con formato "#ID - Proyecto - Descripción..."
   - **Descripción:** Detalles de la acción correctora
   - **Fecha Planificada:** Cuándo se ejecutará
3. Hacer clic en **"Crear"**
4. La acción se crea en estado PLANNED

#### Ver Detalles de Acción

1. Hacer clic en **👁️ Ver** en cualquier acción
2. Se abre diálogo con:
   - Información completa de la acción
   - Incidente vinculado
   - Estado actual
   - Descripción
   - Fechas (planificada, inicio, fin)
   - Efectividad (si está completada)

#### Evaluar Efectividad

1. Solo disponible para acciones en estado COMPLETED
2. Hacer clic en **📊 Evaluar Efectividad**
3. Ingresar valor de efectividad (0.00 - 1.00)
   - 0.00 = No efectiva
   - 1.00 = Completamente efectiva
4. Guardar evaluación

---

## 📋 PLANES PMM

### Ubicación
`/governance/compliance/post-market-monitoring/plans`
`/governance/compliance/post-market-monitoring/plans?projectId=X` (filtrado por proyecto)

### Descripción
Gestión de planes formales de monitoreo post-mercado según EU AI Act Art. 16.g y Art. 72.

### Componentes Principales

#### 1. **Header**
- Título: "Planes de Monitoreo Post-Mercado"
- Subtítulo: Descripción del módulo
- Botón: **"Crear Plan"** (abre diálogo)

#### 2. **Filtros**
- **Proyecto:**
  - Si viene de URL (`?projectId=X`): Muestra nombre del proyecto (no editable)
  - Si no: Selector con todos los proyectos
- **Estado:** DRAFT, ACTIVE, SUSPENDED, ARCHIVED, Todos

#### 3. **Lista de Planes**
Cards con información:
- **Nombre del Plan**
- **Proyecto:** Nombre del proyecto asociado
- **Frecuencia de Monitoreo:** HOURLY, DAILY, WEEKLY, MONTHLY, CUSTOM
- **Frecuencia de Reporte:** DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUAL
- **Estado:** Badge con estado actual
- **Último Monitoreo:** Fecha del último monitoreo ejecutado
- **Acciones:**
  - **Editar** (✏️): Solo para planes DRAFT o SUSPENDED
  - **Activar** (▶️): Solo para planes DRAFT o SUSPENDED
  - **Suspender** (⏸️): Solo para planes ACTIVE
  - **Eliminar** (🗑️): Soft delete (cambia a ARCHIVED)

### Funcionalidades

#### Crear Nuevo Plan PMM

1. Hacer clic en **"Crear Plan"**
2. Completar formulario:
   - **Nombre del Plan:** Ej: "Plan de Monitoreo Diario - AI Credit Scoring"
   - **Proyecto:** Selector con formato "ID - Nombre Proyecto"
     - Si viene de URL, ya está pre-seleccionado
   - **Frecuencia de Monitoreo:** HOURLY, DAILY, WEEKLY, MONTHLY, CUSTOM
     - Si CUSTOM: Ingresar horas personalizadas
   - **Métricas:** JSON con métricas a monitorear (por defecto: accuracy, latency, throughput)
   - **Thresholds de Alertas:** JSON con umbrales por métrica (por defecto: accuracy {warning: 0.05, critical: 0.10})
   - **Frecuencia de Reporte:** DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUAL
3. Hacer clic en **"Crear"**
4. El plan se crea en estado DRAFT

**Validaciones:**
- Nombre del plan es obligatorio
- Proyecto es obligatorio
- Frecuencia de monitoreo es obligatoria
- Si CUSTOM, horas personalizadas deben ser > 0
- Al menos una métrica debe estar configurada
- Thresholds de alertas son obligatorios
- Frecuencia de reporte es obligatoria

#### Activar Plan

1. Solo disponible para planes en estado DRAFT o SUSPENDED
2. Hacer clic en **▶️ Activar**
3. **IMPORTANTE:** Si hay otro plan ACTIVE del mismo proyecto, se suspende automáticamente
4. El plan se marca como ACTIVE y comienza el monitoreo automático

#### Suspender Plan

1. Solo disponible para planes en estado ACTIVE
2. Hacer clic en **⏸️ Suspender**
3. El plan se marca como SUSPENDED y se detiene el monitoreo automático

#### Editar Plan

1. Solo disponible para planes en estado DRAFT o SUSPENDED
2. Hacer clic en **✏️ Editar**
3. Modificar campos necesarios
4. Guardar cambios

#### Eliminar Plan

1. Hacer clic en **🗑️ Eliminar**
2. El plan se marca como ARCHIVED (soft delete)
3. No se puede reactivar un plan archivado

---

## 📄 REPORTES DE VIGILANCIA

### Ubicación
`/governance/compliance/post-market-monitoring/reports`
`/governance/compliance/post-market-monitoring/reports?projectId=X` (filtrado por proyecto)

### Descripción
Generación, consulta y descarga de reportes de vigilancia post-mercado según EU AI Act Art. 72.

### Componentes Principales

#### 1. **Header**
- Título: "Reportes de Vigilancia Post-Mercado"
- Subtítulo: Descripción del módulo
- Botones de Generación:
  - **Generar Reporte Semanal**
  - **Generar Reporte Mensual**

#### 2. **Filtros**
- **Proyecto:**
  - Si viene de URL (`?projectId=X`): Muestra nombre del proyecto (no editable)
  - Si no: Selector con todos los proyectos
- **Tipo:** DAILY, WEEKLY, MONTHLY, AD_HOC, Todos

#### 3. **Lista de Reportes**
Cards con información:
- **Tipo y Fecha:** Ej: "Semanal - 15/01/2025"
- **Estado:** Badge con estado (DRAFT, GENERATED, APPROVED, ARCHIVED)
- **Proyecto:** Nombre del proyecto
- **Tipo de Reporte:** DAILY, WEEKLY, MONTHLY, AD_HOC
- **Fecha del Reporte:** Fecha de generación
- **Fecha de Creación:** Cuándo se creó el registro
- **Acciones:**
  - **Aprobar** (✓): Solo para reportes GENERATED
  - **Descargar PDF** (📥): Descarga el reporte en PDF

### Funcionalidades

#### Generar Reporte

1. Hacer clic en **"Generar Reporte Semanal"** o **"Generar Reporte Mensual"**
2. El sistema genera el reporte automáticamente:
   - Si hay `projectId` en URL, usa ese proyecto
   - Si no, usa el primer proyecto disponible o el seleccionado en filtro
3. El reporte se crea en estado GENERATED
4. Incluye:
   - Métricas recopiladas durante el período
   - Alertas generadas
   - Incidentes detectados
   - Análisis de tendencias

#### Aprobar Reporte

1. Solo disponible para reportes en estado GENERATED
2. Hacer clic en **✓ Aprobar**
3. El reporte se marca como APPROVED
4. Ya está listo para auditorías

#### Descargar PDF

1. Hacer clic en **📥 Descargar PDF** en cualquier reporte
2. Se descarga el reporte en formato PDF
3. El PDF incluye toda la información del reporte formateada

---

## 🎓 TUTORIALES PASO A PASO

### Tutorial 1: Crear y Activar un Plan PMM

**Objetivo:** Configurar monitoreo continuo para un proyecto.

**Pasos:**

1. **Acceder al Dashboard PMM**
   - Navegar a Governance → Compliance → Post-Market Monitoring

2. **Ir a Planes PMM**
   - Hacer clic en el botón **"Ver Planes"** del proyecto deseado
   - O usar el botón general "Ver Planes PMM" y filtrar después

3. **Crear Plan**
   - Hacer clic en **"Crear Plan"**
   - Completar:
     - Nombre: "Plan Monitoreo Diario - [Nombre Proyecto]"
     - Proyecto: Seleccionar el proyecto (ya pre-seleccionado si viene de URL)
     - Frecuencia Monitoreo: DAILY
     - Métricas: Dejar valores por defecto o personalizar
     - Thresholds: Dejar valores por defecto o personalizar
     - Frecuencia Reporte: WEEKLY
   - Hacer clic en **"Crear"**

4. **Activar Plan**
   - En la lista de planes, encontrar el plan recién creado
   - Hacer clic en **▶️ Activar**
   - Confirmar que el plan está ahora en estado ACTIVE

5. **Verificar**
   - El plan aparece en estado ACTIVE
   - El monitoreo automático comenzará según la frecuencia configurada

---

### Tutorial 2: Reportar y Gestionar un Incidente

**Objetivo:** Reportar un problema detectado y gestionar su resolución.

**Pasos:**

1. **Acceder a Incidentes**
   - Desde Dashboard PMM, hacer clic en **"Ver Incidentes"** del proyecto
   - O usar el botón general "Ver Incidentes"

2. **Reportar Incidente**
   - Hacer clic en **"Reportar Incidente"**
   - Completar:
     - Proyecto: Seleccionar (ya pre-seleccionado si viene de URL)
     - Severidad: HIGH (ejemplo)
     - Descripción: "Degradación de accuracy detectada: 0.85 (threshold: 0.90)"
     - Impacto: "Puede afectar decisiones crediticias"
   - Hacer clic en **"Reportar"**
   - **Nota:** Si severidad es HIGH o CRITICAL, se notifica automáticamente a autoridades

3. **Ver Detalles**
   - En la lista, hacer clic en **👁️ Ver** del incidente
   - Revisar información completa

4. **Crear Acción Correctora**
   - Ir a Acciones Correctoras (botón "Ver Acciones" del proyecto)
   - Hacer clic en **"Crear Acción"**
   - Seleccionar el incidente reportado
   - Completar:
     - Descripción: "Reentrenar modelo con datos actualizados"
     - Fecha Planificada: Seleccionar fecha
   - Hacer clic en **"Crear"**

5. **Seguimiento**
   - La acción aparece en estado PLANNED
   - Cuando se ejecute, cambiar a IN_PROGRESS
   - Al completar, cambiar a COMPLETED y evaluar efectividad

---

### Tutorial 3: Generar y Descargar Reporte de Vigilancia

**Objetivo:** Generar un reporte mensual para auditoría.

**Pasos:**

1. **Acceder a Reportes**
   - Desde Dashboard PMM, hacer clic en **"Ver Reportes"** del proyecto
   - O usar el botón general "Ver Reportes"

2. **Generar Reporte**
   - Hacer clic en **"Generar Reporte Mensual"**
   - El sistema genera el reporte automáticamente
   - Aparece en la lista en estado GENERATED

3. **Revisar Reporte**
   - El reporte muestra:
     - Tipo: MONTHLY
     - Fecha: Fecha actual
     - Proyecto: Proyecto seleccionado
     - Estado: GENERATED

4. **Aprobar Reporte**
   - Hacer clic en **✓ Aprobar**
   - El estado cambia a APPROVED

5. **Descargar PDF**
   - Hacer clic en **📥 Descargar PDF**
   - El PDF se descarga automáticamente
   - Listo para auditorías o envío a autoridades

---

### Tutorial 4: Navegación Contextual por Proyecto

**Objetivo:** Aprovechar el filtrado automático por proyecto.

**Pasos:**

1. **Acceder al Dashboard PMM**
   - Governance → Compliance → Post-Market Monitoring

2. **Identificar Proyecto**
   - En "Sistemas en Producción", encontrar el proyecto de interés
   - Ver su estado y métricas actuales

3. **Acceder a Funcionalidades del Proyecto**
   - Hacer clic en **"Ver Incidentes"** del proyecto
   - **Resultado:** Pantalla de incidentes se abre automáticamente filtrada por ese proyecto
   - No es necesario seleccionar el proyecto en el filtro

4. **Navegar a Otras Secciones**
   - Volver al dashboard (usar navegación del navegador)
   - Hacer clic en **"Ver Planes"** del mismo proyecto
   - **Resultado:** Pantalla de planes filtrada automáticamente

5. **Ventaja**
   - Ahorro de tiempo al no tener que filtrar manualmente
   - Especialmente útil cuando hay muchos proyectos (100+)
   - Contexto claro de qué proyecto se está gestionando

---

## 💡 CONSEJOS Y MEJORES PRÁCTICAS

### Para Compliance Officers

1. **Revisar Dashboard Diariamente**
   - Verificar métricas principales
   - Revisar sistemas con estado WARNING o CRITICAL
   - Revisar incidentes activos

2. **Activar Planes PMM Inmediatamente**
   - No dejar planes en DRAFT
   - Activar tan pronto como se creen
   - Solo un plan ACTIVE por proyecto

3. **Revisar Reportes Regularmente**
   - Aprobar reportes generados
   - Descargar PDFs para archivo
   - Usar reportes para auditorías

### Para Project Managers

1. **Usar Navegación Contextual**
   - Acceder a funcionalidades desde el proyecto en el dashboard
   - Aprovechar el filtrado automático

2. **Monitorear Métricas Clave**
   - Revisar accuracy, latency, throughput regularmente
   - Actuar ante degradaciones tempranas

3. **Gestionar Incidentes Proactivamente**
   - Reportar incidentes tan pronto como se detecten
   - Crear acciones correctoras inmediatamente
   - Seguir el estado hasta resolución

### Para Equipos Técnicos

1. **Configurar Thresholds Apropiados**
   - Thresholds muy bajos → Muchas alertas falsas
   - Thresholds muy altos → Se pierden problemas reales
   - Ajustar según experiencia operacional

2. **Documentar Acciones Correctoras**
   - Describir claramente qué se hará
   - Establecer fechas realistas
   - Evaluar efectividad honestamente

3. **Usar Análisis de Causa Raíz**
   - No solo resolver síntomas
   - Identificar causas fundamentales
   - Prevenir recurrencias

---

## ❓ PREGUNTAS FRECUENTES

### ¿Puedo tener múltiples planes PMM activos para un proyecto?
**No.** Solo puede haber un plan ACTIVE por proyecto. Al activar un nuevo plan, el anterior se suspende automáticamente.

### ¿Los planes PMM son solo para sistemas de alto riesgo?
**No.** Los planes PMM están disponibles para TODOS los sistemas de IA, no solo los de alto riesgo. Es una herramienta universal de monitoreo y calidad.

### ¿Qué pasa si reporto un incidente con severidad HIGH o CRITICAL?
Se notifica automáticamente a las autoridades competentes según EU AI Act Art. 20.1 y Art. 73. El sistema marca el incidente como notificado.

### ¿Puedo editar un plan PMM activo?
**No.** Solo se pueden editar planes en estado DRAFT o SUSPENDED. Para modificar un plan activo, primero debe suspenderse.

### ¿Cómo accedo a incidentes de un proyecto específico?
Desde el Dashboard PMM, hacer clic en **"Ver Incidentes"** del proyecto deseado. La pantalla se abrirá automáticamente filtrada por ese proyecto.

### ¿Los reportes se generan automáticamente?
Sí, si hay un plan PMM ACTIVO con frecuencia de reporte configurada. También se pueden generar manualmente usando los botones "Generar Reporte Semanal/Mensual".

---

**Última Actualización:** Diciembre 2025
