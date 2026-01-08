# 📱 GUÍA DE USO - PANTALLAS QMS

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo QMS](#acceso-al-módulo-qms)
2. [Dashboard QMS Principal](#dashboard-qms-principal)
3. [Lista de Proyectos QMS](#lista-de-proyectos-qms)
4. [Revisión de Conformidad](#revisión-de-conformidad)
5. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO QMS

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **QMS** en el sidebar
3. Se abrirá la lista de proyectos QMS

### Navegación Contextual por Proyecto

**IMPORTANTE:** Las pantallas de QMS aceptan el parámetro `projectId` en la URL para filtrar automáticamente:

- `/governance/compliance/qms?projectId=1` - Dashboard QMS para proyecto 1
- `/governance/compliance/qms/projects` - Lista de todos los proyectos
- `/governance/compliance/conformity-review?projectId=1` - Revisión de gaps para proyecto 1

**Ventaja:** Al acceder desde un proyecto específico, la pantalla se filtra automáticamente, evitando tener que seleccionar el proyecto manualmente.

---

## 📊 DASHBOARD QMS PRINCIPAL

### Ubicación
`/governance/compliance/qms?projectId=X`

### Descripción
Vista centralizada con métricas principales, scores por módulo, gaps detectados y plan de mejora.

### Componentes

#### 1. **Header (Parte Superior)**

**Estructura:**
- **Izquierda:** Botón "Volver" y título/subtítulo
- **Centro:** Datos del proyecto (nombre, score overall, estado de compliance)
- **Derecha:** Botones de acción (Exportar, Actualizar)

**Elementos:**
- **Botón Volver:** Navega a `/governance/compliance/qms/projects`
- **Título:** "Sistema de Gestión de Calidad (QMS)"
- **Subtítulo:** "Art. 17 EU AI Act - Monitoreo de compliance por módulos"
- **Nombre del Proyecto:** Nombre del proyecto actual
- **Score Overall:** Porcentaje de cumplimiento general (0-100%)
- **Estado:** Badge con estado (COMPLIANT, PARTIAL, NON_COMPLIANT)
- **Botón Exportar:** Exporta datos QMS (funcionalidad futura)
- **Botón Actualizar:** Recalcula scores QMS

#### 2. **Métricas Principales (4 Contadores)**

**Ubicación:** Debajo del header

**Métricas:**
- **Total Módulos:** Número total de módulos QMS (13)
- **Módulos Cumpliendo:** Módulos con score >= 0.80
- **Gaps Detectados:** Número de gaps (módulos con score < 0.80)
- **Score Overall:** Porcentaje de cumplimiento general

**Visualización:**
- Cards con iconos y colores según valor
- Verde: Buen estado
- Amarillo: Estado medio
- Rojo: Estado crítico

#### 3. **Gráfico de Barras: Scores por Módulo**

**Ubicación:** Sección central

**Descripción:**
- Gráfico de barras horizontal mostrando score de cada uno de los 13 módulos
- Módulos traducidos según idioma seleccionado
- Colores según score:
  - Verde: >= 85%
  - Amarillo: 70-84%
  - Rojo: < 70%

**Funcionalidad:**
- Scroll vertical si hay muchos módulos
- Valores mostrados con signo "%"
- Tooltips al pasar el mouse

**Módulos Mostrados:**
1. Estrategia de Cumplimiento (A)
2. Control de Diseño (B)
3. Aseguramiento de Calidad (C)
4. Validación y Testing (D)
5. Estándares Técnicos (E)
6. Gestión de Datos (F)
7. Gestión de Riesgos (G)
8. Post-Market Monitoring (H)
9. Incidentes Graves (I)
10. Comunicaciones con Autoridades (J)
11. Documentación Técnica (K)
12. Gestión de Recursos (L)
13. Accountability Framework (M)

#### 4. **Card: Gaps Detectados**

**Ubicación:** Sección inferior izquierda (50% ancho)

**Descripción:**
- Lista de gaps detectados (módulos con score < 0.80)
- Scroll vertical si hay muchos gaps

**Información por Gap:**
- **Módulo:** Nombre del módulo afectado
- **Severidad:** Badge con severidad (LOW, MEDIUM, HIGH)
- **Score Actual:** Porcentaje actual
- **Score Objetivo:** Porcentaje objetivo (80%)
- **Gap:** Diferencia entre objetivo y actual
- **Descripción:** Descripción del gap
- **Acciones Recomendadas:** Lista de acciones para cerrar el gap

**Visualización:**
- Cards con bordes según severidad
- Badges de color para severidad
- Scroll automático si contenido excede altura

#### 5. **Card: Plan de Mejora**

**Ubicación:** Sección inferior derecha (50% ancho)

**Descripción:**
- Lista de acciones recomendadas para cerrar gaps
- Scroll vertical si hay muchas acciones

**Información:**
- **Gap Relacionado:** Módulo y gap al que pertenece
- **Acción:** Descripción de la acción recomendada
- **Prioridad:** Prioridad de la acción
- **Estado:** Estado de implementación (si aplica)

**Visualización:**
- Lista ordenada por prioridad
- Scroll automático si contenido excede altura

### Funcionalidades

#### Calcular Scores QMS

1. Hacer clic en **"Actualizar"** (botón superior derecho)
2. El sistema:
   - Evalúa los 13 módulos automáticamente
   - Calcula scores por módulo
   - Calcula score overall (promedio)
   - Detecta gaps automáticamente
3. La pantalla se actualiza con los nuevos datos

#### Navegar a Revisión de Gaps

1. Desde el card "Gaps Detectados", hacer clic en **"Revisar Gaps"** (si está disponible)
2. O navegar directamente a `/governance/compliance/conformity-review?projectId=X`

#### Exportar Datos

1. Hacer clic en **"Exportar"** (botón superior derecho)
2. Se descarga un archivo con datos QMS (funcionalidad futura)

---

## 📋 LISTA DE PROYECTOS QMS

### Ubicación
`/governance/compliance/qms/projects`

### Descripción
Vista que muestra todos los proyectos con información resumida de QMS.

### Componentes

#### 1. **Header**

**Elementos:**
- **Título:** "Proyectos con QMS"
- **Subtítulo:** "Gestión de Quality Management System por proyecto - Art. 17 EU AI Act"
- **Búsqueda:** Campo de búsqueda por nombre de proyecto
- **Filtro por Estado:** Selector con opciones:
  - Todos
  - COMPLIANT
  - PARTIAL
  - NON_COMPLIANT

#### 2. **Estadísticas Agregadas**

**Ubicación:** Debajo del header

**Métricas:**
- **Total Proyectos:** Número total de proyectos con QMS
- **Compliant:** Proyectos con estado COMPLIANT
- **Partial:** Proyectos con estado PARTIAL
- **Non-Compliant:** Proyectos con estado NON_COMPLIANT
- **Score Promedio:** Promedio de scores overall de todos los proyectos

**Visualización:**
- Cards con iconos y colores
- Números destacados

#### 3. **Grid de Proyectos**

**Ubicación:** Sección principal

**Estructura:**
- Grid de 2 columnas (responsive: 1 columna en móvil)
- Cada proyecto se muestra en un card

**Información por Proyecto:**
- **Nombre del Proyecto:** Nombre completo
- **Score Overall:** Porcentaje de cumplimiento (grande y destacado)
- **Estado:** Badge con estado de compliance
- **Módulos Cumpliendo:** "X de 13 módulos cumpliendo"
- **Gaps Detectados:** Número de gaps
- **Última Actualización:** Fecha de última actualización
- **Botones de Acción:**
  - **Ver Dashboard:** Navega a `/governance/compliance/qms?projectId=X`
  - **Revisar Gaps:** Navega a `/governance/compliance/conformity-review?projectId=X`

**Visualización:**
- Cards con bordes según estado
- Colores según score (verde/amarillo/rojo)
- Botones pequeños en la parte inferior derecha

#### 4. **Paginación**

**Ubicación:** Parte inferior

**Elementos:**
- Botones "Anterior" y "Siguiente"
- Información: "Página X de Y"
- Tamaño de página: 12 proyectos por página

### Funcionalidades

#### Filtrar Proyectos

1. **Por Estado:**
   - Seleccionar estado en el filtro superior
   - El grid se actualiza automáticamente

2. **Por Búsqueda:**
   - Escribir en el campo de búsqueda
   - El grid se filtra por nombre de proyecto

#### Navegar a Dashboard de Proyecto

1. Hacer clic en **"Ver Dashboard"** en cualquier proyecto
2. Se abre el dashboard QMS para ese proyecto específico

#### Revisar Gaps de Proyecto

1. Hacer clic en **"Revisar Gaps"** en cualquier proyecto
2. Se abre la pantalla de revisión de conformidad para ese proyecto

---

## ✅ REVISIÓN DE CONFORMIDAD

### Ubicación
`/governance/compliance/conformity-review?projectId=X`

### Descripción
Pantalla para revisar gaps detectados, tomar decisiones de aprobación y solicitar correcciones.

### Componentes

#### 1. **Header (Parte Superior)**

**Estructura:**
- **Izquierda:** Botón "Volver" y título/subtítulo
- **Centro:** Datos del proyecto (nombre, ID, score overall)
- **Derecha:** Botones de acción (Exportar, Aprobar QMS, Solicitar Correcciones)

**Elementos:**
- **Botón Volver:** Navega a `/governance/compliance/qms/projects`
- **Título:** "Revisión de Conformidad QMS"
- **Subtítulo:** "Revisión y aprobación de gaps detectados - Art. 17 EU AI Act"
- **Nombre del Proyecto:** Nombre del proyecto actual
- **ID del Proyecto:** ID numérico del proyecto
- **Score Overall:** Porcentaje de cumplimiento general
- **Botón Exportar:** Exporta datos de revisión (funcionalidad futura)
- **Botón Aprobar QMS:** Aprueba el QMS si no hay gaps críticos
- **Botón Solicitar Correcciones:** Solicita correcciones para gaps detectados

#### 2. **Card: Gaps Detectados (50% Ancho)**

**Ubicación:** Sección principal izquierda

**Descripción:**
- Lista de gaps detectados con opción de marcar como aceptables
- Scroll vertical si hay muchos gaps

**Información por Gap:**
- **Módulo:** Nombre del módulo afectado (traducido)
- **Severidad:** Badge con severidad (LOW, MEDIUM, HIGH)
- **Score Actual:** Porcentaje actual
- **Score Objetivo:** Porcentaje objetivo (80%)
- **Gap:** Diferencia entre objetivo y actual
- **Descripción:** Descripción detallada del gap
- **Acciones Recomendadas:** Lista de acciones para cerrar el gap
- **Botón "Marcar como Aceptable":**
  - Permite marcar un gap como aceptable
  - Cambia el estado visual del gap
  - El botón cambia a "Marcado como Aceptable" con check verde

**Visualización:**
- Cards con bordes
- Si un gap está marcado como aceptable:
  - Borde verde
  - Badge "Aceptable" verde
  - Botón con check verde

#### 3. **Card: Decisión de Revisión (50% Ancho)**

**Ubicación:** Sección principal derecha

**Descripción:**
- Formulario para tomar decisión de revisión
- Scroll vertical si contenido excede altura

**Campos:**
- **Nombre del Revisor:** Campo pre-llenado con nombre del usuario conectado (readonly, disabled)
- **Notas de Revisión:** Campo de texto multilínea (requerido)
  - **Importante:** Este campo es obligatorio para habilitar los botones de acción
  - Si está vacío, los botones "Aprobar QMS" y "Solicitar Correcciones" están deshabilitados
  - Se muestra un mensaje informativo si está vacío

**Botones de Acción:**
- **Aprobar QMS:**
  - Solo habilitado si hay notas de revisión
  - Aprueba el QMS si no hay gaps críticos
  - Completa tarea BPMN si `taskId` está presente
  - Muestra mensaje de éxito

- **Solicitar Correcciones:**
  - Solo habilitado si hay notas de revisión
  - Solicita correcciones para gaps detectados
  - Lanza workflow BPMN de correcciones
  - Completa tarea BPMN si `taskId` está presente
  - Muestra mensaje de éxito

**Mensaje Informativo:**
- Si las notas de revisión están vacías, se muestra:
  - "Las notas de revisión son requeridas para aprobar o solicitar correcciones."
  - En color rojo

### Funcionalidades

#### Marcar Gap como Aceptable

1. Hacer clic en **"Marcar como Aceptable"** en cualquier gap
2. El gap cambia de estado:
   - Borde se vuelve verde
   - Aparece badge "Aceptable" verde
   - El botón cambia a "Marcado como Aceptable" con check verde
3. Para desmarcar, hacer clic nuevamente en el botón

#### Aprobar QMS

1. Completar campo **"Notas de Revisión"** (obligatorio)
2. Revisar todos los gaps
3. Marcar gaps como aceptables si es necesario
4. Hacer clic en **"Aprobar QMS"**
5. El sistema:
   - Valida que hay notas de revisión
   - Procesa la aprobación
   - Completa tarea BPMN si está en un workflow
   - Muestra mensaje de éxito

#### Solicitar Correcciones

1. Completar campo **"Notas de Revisión"** (obligatorio)
2. Revisar todos los gaps
3. Hacer clic en **"Solicitar Correcciones"**
4. El sistema:
   - Valida que hay notas de revisión
   - Procesa la solicitud de correcciones
   - Lanza workflow BPMN de correcciones
   - Completa tarea BPMN si está en un workflow
   - Muestra mensaje de éxito

---

## 📚 TUTORIALES PASO A PASO

### Tutorial 1: Evaluación Inicial de QMS para un Proyecto

**Objetivo:** Evaluar el estado de QMS de un proyecto por primera vez.

**Pasos:**

1. **Acceder a Lista de Proyectos**
   - Navegar a `/governance/compliance/qms/projects`
   - Buscar el proyecto deseado

2. **Abrir Dashboard del Proyecto**
   - Hacer clic en **"Ver Dashboard"** en el proyecto
   - Se abre `/governance/compliance/qms?projectId=X`

3. **Calcular Scores QMS**
   - Hacer clic en **"Actualizar"** (botón superior derecho)
   - Esperar a que el sistema calcule los scores
   - La pantalla se actualiza automáticamente

4. **Revisar Métricas**
   - Revisar métricas principales (total módulos, cumpliendo, gaps, score overall)
   - Revisar gráfico de barras con scores por módulo
   - Identificar módulos con score bajo (< 80%)

5. **Revisar Gaps**
   - Revisar card "Gaps Detectados"
   - Leer descripción de cada gap
   - Revisar acciones recomendadas

6. **Revisar Plan de Mejora**
   - Revisar card "Plan de Mejora"
   - Identificar acciones prioritarias
   - Planificar implementación de mejoras

### Tutorial 2: Revisar y Aprobar Gaps QMS

**Objetivo:** Revisar gaps detectados y aprobar o solicitar correcciones.

**Pasos:**

1. **Acceder a Revisión de Conformidad**
   - Desde dashboard QMS, hacer clic en **"Revisar Gaps"** (si está disponible)
   - O navegar directamente a `/governance/compliance/conformity-review?projectId=X`

2. **Revisar Gaps Detectados**
   - Revisar cada gap en el card izquierdo
   - Leer descripción y acciones recomendadas
   - Evaluar severidad de cada gap

3. **Marcar Gaps como Aceptables (Opcional)**
   - Si un gap es aceptable, hacer clic en **"Marcar como Aceptable"**
   - El gap se marca visualmente como aceptable
   - Se puede desmarcar haciendo clic nuevamente

4. **Completar Notas de Revisión**
   - En el card derecho, escribir notas de revisión en el campo **"Notas de Revisión"**
   - **Importante:** Este campo es obligatorio
   - Incluir justificación de decisiones tomadas

5. **Tomar Decisión**
   - **Opción A: Aprobar QMS**
     - Si no hay gaps críticos o todos son aceptables
     - Hacer clic en **"Aprobar QMS"**
     - Confirmar aprobación
   - **Opción B: Solicitar Correcciones**
     - Si hay gaps que requieren corrección
     - Hacer clic en **"Solicitar Correcciones"**
     - Confirmar solicitud

6. **Verificar Resultado**
   - Verificar mensaje de éxito
   - Si está en un workflow BPMN, la tarea se completa automáticamente
   - Si se solicitaron correcciones, se lanza workflow de correcciones

### Tutorial 3: Monitoreo Continuo de QMS

**Objetivo:** Monitorear el estado de QMS de múltiples proyectos.

**Pasos:**

1. **Acceder a Lista de Proyectos**
   - Navegar a `/governance/compliance/qms/projects`

2. **Revisar Estadísticas Agregadas**
   - Revisar total proyectos, compliant, partial, non-compliant
   - Revisar score promedio
   - Identificar tendencias generales

3. **Filtrar Proyectos**
   - Usar filtro por estado para ver proyectos específicos
   - Usar búsqueda para encontrar proyectos por nombre

4. **Revisar Proyectos Individuales**
   - Hacer clic en **"Ver Dashboard"** para proyectos de interés
   - Revisar scores y gaps
   - Identificar proyectos que requieren atención

5. **Tomar Acciones**
   - Para proyectos con gaps, navegar a revisión de conformidad
   - Aprobar o solicitar correcciones según corresponda
   - Documentar decisiones

### Tutorial 4: Cerrar Gaps y Mejorar Scores

**Objetivo:** Implementar mejoras para cerrar gaps y mejorar scores QMS.

**Pasos:**

1. **Identificar Gaps**
   - Desde dashboard QMS, revisar card "Gaps Detectados"
   - Identificar gaps prioritarios (alta severidad)

2. **Revisar Acciones Recomendadas**
   - Para cada gap, revisar acciones recomendadas en "Plan de Mejora"
   - Priorizar acciones según severidad y impacto

3. **Implementar Mejoras**
   - Implementar acciones recomendadas
   - Actualizar datos de módulos afectados (según módulo específico)
   - Documentar cambios realizados

4. **Recalcular Scores**
   - Volver a dashboard QMS
   - Hacer clic en **"Actualizar"** para recalcular scores
   - Verificar que scores hayan mejorado

5. **Verificar Cierre de Gaps**
   - Revisar que gaps se hayan cerrado (score >= 80%)
   - Si aún hay gaps, continuar con mejoras
   - Repetir proceso hasta alcanzar compliance

---

## 🎯 CONSEJOS Y MEJORES PRÁCTICAS

### Para Compliance Officers

- ✅ **Revisar regularmente:** Monitorear scores QMS semanalmente
- ✅ **Documentar decisiones:** Siempre incluir notas de revisión detalladas
- ✅ **Priorizar gaps críticos:** Enfocarse en gaps de alta severidad primero
- ✅ **Mantener evidencia:** Exportar datos QMS para auditorías

### Para Project Managers

- ✅ **Calcular scores regularmente:** Actualizar scores después de cambios importantes
- ✅ **Revisar gaps proactivamente:** No esperar a auditorías para revisar gaps
- ✅ **Implementar mejoras continuas:** Usar plan de mejora como guía
- ✅ **Comunicar estado:** Compartir métricas QMS con stakeholders

### Para Equipos Técnicos

- ✅ **Actualizar datos de módulos:** Mantener información de módulos actualizada
- ✅ **Implementar acciones recomendadas:** Seguir plan de mejora
- ✅ **Documentar cambios:** Documentar mejoras implementadas
- ✅ **Colaborar en cierre de gaps:** Trabajar en equipo para cerrar gaps

---

## 📚 REFERENCIAS

- **Guía Funcional:** `docs/prompts/compliance/qms/user_guide/GUIA_FUNCIONAL_QMS.md`
- **Estado de Implementación:** `docs/prompts/compliance/qms/ESTADO_IMPLEMENTACION_QMS.md`
- **EU AI Act Art. 17:** Sistema de Gestión de Calidad

---

**Última actualización:** Diciembre 2025
**Versión:** 1.0.0
