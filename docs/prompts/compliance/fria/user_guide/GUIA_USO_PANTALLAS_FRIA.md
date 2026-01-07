# 📱 GUÍA DE USO - PANTALLAS FRIA

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo FRIA](#acceso-al-módulo-fria)
2. [Pantalla de Proyectos FRIA](#pantalla-de-proyectos-fria)
3. [Wizard de Evaluación FRIA](#wizard-de-evaluación-fria)
4. [Pantalla de Detalle FRIA](#pantalla-de-detalle-fria)
5. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO FRIA

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **FRIA Projects** en el sidebar
3. Se abrirá la pantalla de proyectos con evaluaciones FRIA

**Ruta:** `/governance/compliance/fria/projects`

---

## 📊 PANTALLA DE PROYECTOS FRIA

### Ubicación
`/governance/compliance/fria/projects`

### Descripción
Vista centralizada que muestra todos los proyectos con sus evaluaciones FRIA agrupadas, versiones e historial.

### Componentes Principales

#### 1. **Header**
- **Título:** "Proyectos con Evaluaciones FRIA"
- **Subtítulo:** "Gestión de evaluaciones FRIA por proyecto, versiones e historial"

#### 2. **Estadísticas (4 Contadores)**
- **Total Proyectos:** Número total de proyectos con evaluaciones FRIA
- **Total Evaluaciones:** Número total de evaluaciones FRIA
- **Notificadas:** Proyectos con FRIA notificadas a autoridades
- **Borradores:** Proyectos con FRIA en estado borrador

#### 3. **Filtros y Búsqueda**
- **Búsqueda:** Buscar por nombre, descripción o categoría de proyecto
- **Filtro por Estado:** 
  - Todos los Estados
  - Borrador
  - Completada
  - Notificada
  - Aprobada

#### 4. **Lista de Proyectos**

Cada proyecto muestra:

**Header del Proyecto:**
- **Nombre del Proyecto**
- **Categoría:** Badge con código (ej: "B.1")
- **Total de Evaluaciones:** Badge con número
- **Estado de Última FRIA:** Badge con estado (Borrador, Completada, Notificada, Aprobada)
- **Nivel de Riesgo:** Badge con nivel (Bajo, Medio, Alto, Crítico)

**Botones de Acción:**
- **Expandir/Contraer:** Ver todas las evaluaciones del proyecto
- **Nueva FRIA:** Crear nueva evaluación para el proyecto

**Última Evaluación FRIA (Resumen):**
- Versión (si aplica)
- Fecha de creación
- Completitud (%)
- Riesgo (%)
- Creado por
- Botones:
  - **Ver** (👁️): Ver detalle completo
  - **Editar** (✏️): Solo si está en borrador
  - **Crear Versión** (📋): Crear nueva versión basada en esta

**Lista Expandida de Todas las Evaluaciones:**
- Al expandir, se muestra lista completa de todas las evaluaciones FRIA del proyecto
- Cada evaluación muestra:
  - Versión
  - Estado
  - Nivel de riesgo
  - Fecha de creación
  - Completitud y riesgo
  - Fechas de notificación/aprobación (si aplica)
  - Botones de acción (Ver, Editar, Crear Versión, Descargar PDF)

#### 5. **Paginación**
- Navegación entre páginas
- Muestra: "Mostrando X - Y de Z"
- Botones: Anterior / Siguiente

### Funcionalidades

#### Crear Nueva Evaluación FRIA

1. En la lista de proyectos, encontrar el proyecto deseado
2. Hacer clic en **"Nueva FRIA"**
3. Se abre el wizard de evaluación FRIA
4. Completar los 6 pasos del wizard
5. Guardar y completar la evaluación

#### Ver Detalle de Evaluación

1. En cualquier evaluación (última o en lista expandida)
2. Hacer clic en botón **"Ver"** (👁️)
3. Se abre pantalla de detalle con información completa

#### Editar Evaluación en Borrador

1. Identificar evaluación con estado "Borrador"
2. Hacer clic en botón **"Editar"** (✏️)
3. Se abre wizard con datos actuales
4. Modificar información necesaria
5. Guardar cambios

#### Crear Nueva Versión

1. Seleccionar evaluación base (última o en lista expandida)
2. Hacer clic en botón **"Crear Versión"** (📋)
3. Se abre wizard con datos pre-cargados de la evaluación base
4. Modificar información según cambios en el sistema
5. Guardar como nueva versión

#### Expandir/Contraer Proyecto

1. Hacer clic en botón **"Expandir"** / **"Contraer"**
2. Se muestra/oculta lista completa de evaluaciones
3. Útil para ver historial completo de evaluaciones

---

## 🧙 WIZARD DE EVALUACIÓN FRIA

### Ubicación
`/governance/compliance/fria`
`/governance/compliance/fria?projectId=X` (para proyecto específico)
`/governance/compliance/fria?friaId=X` (para editar)
`/governance/compliance/fria?projectId=X&baseFriaId=Y` (para crear versión)

### Descripción
Wizard paso a paso para crear o editar una evaluación FRIA completa.

### Componentes Principales

#### Header
- **Título:** "Evaluación FRIA"
- **Subtítulo:** "Fundamental Rights Impact Assessment según EU AI Act Art. 27"
- **Botón Back:** Volver a pantalla de proyectos

#### Barra de Progreso
- Muestra paso actual de 6
- Porcentaje de completitud
- Barra visual de progreso

#### Navegación
- **Botón Anterior:** Ir al paso anterior
- **Botón Siguiente:** Ir al siguiente paso
- **Botón Guardar:** Guardar progreso actual

### Pasos del Wizard

#### Paso 1: Descripción de Procesos (Art. 27.1.a)

**Campos:**
- **Descripción de Procesos:** Textarea (mínimo 50 caracteres)
- **Sugerencia IA:** Botón para generar sugerencia automática

**Acciones:**
- Completar descripción
- Opcional: Generar sugerencia IA
- Hacer clic en "Siguiente"

#### Paso 2: Período y Frecuencia de Uso (Art. 27.1.b)

**Campos:**
- **Período de Uso - Inicio:** Date picker
- **Período de Uso - Fin:** Date picker
- **Frecuencia de Uso:** Selector
  - Diaria
  - Semanal
  - Mensual
  - Continua
  - Otra

**Acciones:**
- Seleccionar fechas de inicio y fin
- Seleccionar frecuencia
- Hacer clic en "Siguiente"

#### Paso 3: Categorías de Personas Afectadas (Art. 27.1.c)

**Campos:**
- **Categorías Afectadas:** Lista dinámica
  - Botón "Añadir Categoría"
  - Cada categoría tiene botón "Eliminar"
- **Grupos Vulnerables:** Checkbox
- **Descripción de Grupos Vulnerables:** Textarea (si checkbox marcado)

**Acciones:**
- Añadir categorías de personas afectadas
- Marcar si incluye grupos vulnerables
- Si aplica, describir grupos vulnerables
- Hacer clic en "Siguiente"

#### Paso 4: Riesgos Específicos (Art. 27.1.d)

**Campos por cada riesgo:**
- **Tipo de Riesgo:** Selector
  - Discriminación
  - Privacidad
  - Transparencia
  - Autonomía
  - Dignidad
  - Otro
- **Severidad:** Selector (Baja, Media, Alta, Crítica)
- **Probabilidad:** Slider (0.0 - 1.0)
- **Impacto:** Selector (Bajo, Medio, Alto)
- **Descripción:** Textarea

**Acciones:**
- Hacer clic en "Añadir Riesgo"
- Completar información de cada riesgo
- Eliminar riesgo si es necesario
- Hacer clic en "Siguiente"

#### Paso 5: Supervisión Humana (Art. 27.1.e)

**Campos:**
- **HITL Habilitado:** Checkbox
- **Descripción de Medidas:** Textarea (mínimo 50 caracteres)

**Acciones:**
- Marcar si HITL está habilitado
- Describir medidas de supervisión humana
- Hacer clic en "Siguiente"

#### Paso 6: Medidas de Mitigación (Art. 27.1.f)

**Campos por cada medida:**
- **Tipo de Medida:** Selector
  - Preventiva
  - Detectiva
  - Correctiva
- **Descripción:** Textarea
- **Efectividad:** Slider (0.0 - 1.0)
- **Riesgo Asociado:** Selector (opcional, vincula con riesgo del paso 4)

**Acciones:**
- Hacer clic en "Añadir Medida"
- Completar información de cada medida
- Vincular con riesgos si aplica
- Eliminar medida si es necesario
- Hacer clic en "Siguiente" o "Completar FRIA"

### Funcionalidades Adicionales

#### Calcular Riesgo Final

1. Completar todos los pasos
2. Hacer clic en **"Calcular Riesgo"**
3. Sistema calcula riesgo según Anexo IX
4. Se muestra nivel de riesgo (Bajo, Medio, Alto, Crítico)

#### Validación Cruzada (INC-007)

1. Hacer clic en **"Validar vs Métricas Técnicas"**
2. Sistema compara con métricas técnicas reales
3. Se muestra score de consistencia
4. Si score < 0.70, se requiere justificación

#### Completar FRIA

1. Verificar que todos los pasos estén completos
2. Calcular riesgo final
3. Hacer clic en **"Completar FRIA"**
4. Estado cambia a "COMPLETED"

#### Notificar a Autoridades (Art. 27.3)

1. Solo disponible si:
   - FRIA está completa
   - Riesgo final ≥ 0.75
   - No ha sido notificada previamente
2. Hacer clic en **"Notificar Autoridades"**
3. Se envía notificación automática
4. Se registra ID de notificación

---

## 📄 PANTALLA DE DETALLE FRIA

### Ubicación
`/governance/compliance/fria/[id]`

### Descripción
Vista detallada de una evaluación FRIA completa, mostrando toda la información de los 6 pasos.

### Componentes Principales

#### Header
- **Título:** "Detalle FRIA #[id]"
- **Nombre del Proyecto**
- **Botón Back:** Volver a pantalla de proyectos
- **Botones de Acción:**
  - **Editar:** Solo si estado es DRAFT
  - **Notificar Autoridades:** Solo si riesgo ≥ 0.75 y no notificada
  - **Exportar PDF:** Descargar evaluación en PDF

#### Tarjetas de Resumen (4)
1. **Estado:** Badge con estado actual
2. **Completitud:** Porcentaje de completitud
3. **Riesgo Final:** Porcentaje y nivel de riesgo
4. **Fecha Creación:** Fecha de creación

#### Secciones Detalladas (6 Pasos)

Cada paso muestra:
- **Título:** "Paso X: [Nombre] (Art. 27.1.X)"
- **Contenido completo** del paso
- **Formato estructurado** según tipo de paso

**Paso 1:** Descripción de procesos (texto)

**Paso 2:** 
- Período de uso (fechas)
- Frecuencia

**Paso 3:**
- Lista de categorías afectadas (badges)
- Indicador de grupos vulnerables
- Descripción de grupos vulnerables (si aplica)

**Paso 4:**
- Lista de riesgos con:
  - Tipo
  - Severidad, Probabilidad, Impacto (badges)
  - Descripción

**Paso 5:**
- Indicador HITL habilitado
- Descripción de medidas

**Paso 6:**
- Lista de medidas con:
  - Descripción
  - Efectividad
  - Riesgo asociado (si aplica)

#### Información Adicional
- **Notificación a Autoridades:**
  - Estado (Notificado / No notificado)
  - Fecha de notificación (si aplica)
- **DPIA Vinculado:**
  - ID de DPIA (si aplica)
  - Estado (Vinculado / No vinculado)

---

## 🎓 TUTORIALES PASO A PASO

### Tutorial 1: Crear Nueva Evaluación FRIA Completa

**Objetivo:** Crear una evaluación FRIA desde cero para un proyecto.

**Pasos:**

1. **Acceder a Pantalla de Proyectos**
   - Ir a `/governance/compliance/fria/projects`
   - Localizar proyecto en la lista

2. **Iniciar Nueva Evaluación**
   - Hacer clic en **"Nueva FRIA"** del proyecto
   - Se abre wizard en paso 1

3. **Completar Paso 1**
   - Describir procesos donde se usa el sistema IA
   - Mínimo 50 caracteres
   - Opcional: Generar sugerencia IA
   - Hacer clic en "Siguiente"

4. **Completar Paso 2**
   - Seleccionar fecha de inicio
   - Seleccionar fecha de fin
   - Seleccionar frecuencia (ej: "Diaria")
   - Hacer clic en "Siguiente"

5. **Completar Paso 3**
   - Hacer clic en "Añadir Categoría"
   - Escribir categoría (ej: "Solicitantes de préstamos")
   - Añadir más categorías si aplica
   - Marcar checkbox si incluye grupos vulnerables
   - Si aplica, describir grupos vulnerables
   - Hacer clic en "Siguiente"

6. **Completar Paso 4**
   - Hacer clic en "Añadir Riesgo"
   - Seleccionar tipo (ej: "Discriminación")
   - Seleccionar severidad (ej: "Alta")
   - Ajustar probabilidad (ej: 0.75)
   - Seleccionar impacto (ej: "Alto")
   - Describir el riesgo
   - Añadir más riesgos si aplica
   - Hacer clic en "Siguiente"

7. **Completar Paso 5**
   - Marcar checkbox si HITL está habilitado
   - Describir medidas de supervisión humana
   - Mínimo 50 caracteres
   - Hacer clic en "Siguiente"

8. **Completar Paso 6**
   - Hacer clic en "Añadir Medida"
   - Seleccionar tipo (ej: "Preventiva")
   - Describir la medida
   - Ajustar efectividad (ej: 0.80)
   - Opcional: Vincular con riesgo del paso 4
   - Añadir más medidas si aplica
   - Hacer clic en "Completar FRIA"

9. **Calcular Riesgo**
   - Hacer clic en "Calcular Riesgo"
   - Revisar nivel de riesgo calculado

10. **Validación Cruzada (Opcional)**
    - Hacer clic en "Validar vs Métricas Técnicas"
    - Revisar score de consistencia
    - Si < 0.70, proporcionar justificación

11. **Completar Evaluación**
    - Verificar que todo esté correcto
    - Hacer clic en "Completar FRIA"
    - Estado cambia a "COMPLETED"

12. **Notificar (Si aplica)**
    - Si riesgo ≥ 0.75, aparece botón "Notificar Autoridades"
    - Hacer clic para enviar notificación
    - Estado cambia a "NOTIFIED"

### Tutorial 2: Crear Nueva Versión de Evaluación

**Objetivo:** Crear una nueva versión basada en una evaluación existente.

**Pasos:**

1. **Acceder a Proyecto**
   - Ir a pantalla de proyectos
   - Expandir proyecto deseado

2. **Seleccionar Evaluación Base**
   - En lista expandida, localizar evaluación base
   - Hacer clic en botón "Crear Versión" (📋)

3. **Modificar Datos**
   - Wizard se abre con datos pre-cargados
   - Modificar información según cambios en el sistema
   - Ejemplos:
     - Actualizar descripción de procesos si cambió
     - Añadir nuevos riesgos detectados
     - Actualizar medidas de mitigación

4. **Guardar Nueva Versión**
   - Completar cambios
   - Calcular nuevo riesgo
   - Guardar como nueva versión
   - Nueva versión aparece en historial

### Tutorial 3: Editar Evaluación en Borrador

**Objetivo:** Modificar una evaluación que está en estado borrador.

**Pasos:**

1. **Identificar Evaluación DRAFT**
   - En pantalla de proyectos, buscar evaluaciones con badge "Borrador"

2. **Abrir para Editar**
   - Hacer clic en botón "Editar" (✏️)
   - Se abre wizard con datos actuales

3. **Modificar Información**
   - Navegar a paso que necesita modificación
   - Actualizar información
   - Guardar cambios

4. **Completar Evaluación**
   - Una vez todas las modificaciones hechas
   - Completar FRIA
   - Estado cambia a "COMPLETED"

### Tutorial 4: Ver Detalle Completo de Evaluación

**Objetivo:** Revisar toda la información de una evaluación FRIA.

**Pasos:**

1. **Acceder a Evaluación**
   - Desde pantalla de proyectos
   - Hacer clic en botón "Ver" (👁️) de cualquier evaluación

2. **Revisar Información**
   - Ver tarjetas de resumen (Estado, Completitud, Riesgo, Fecha)
   - Revisar cada uno de los 6 pasos
   - Ver información adicional (Notificación, DPIA)

3. **Acciones Disponibles**
   - Si es DRAFT: Botón "Editar"
   - Si riesgo ≥ 0.75: Botón "Notificar Autoridades"
   - Siempre: Botón "Exportar PDF"

---

## 💡 CONSEJOS Y MEJORES PRÁCTICAS

### Al Completar el Wizard

1. **Sé Específico:** Proporciona descripciones detalladas y específicas
2. **Identifica Todos los Riesgos:** No omitas riesgos potenciales
3. **Define Medidas Efectivas:** Las medidas de mitigación deben ser concretas y medibles
4. **Revisa Antes de Completar:** Verifica que toda la información sea correcta

### Al Crear Versiones

1. **Documenta Cambios:** Explica por qué se crea nueva versión
2. **Actualiza Riesgos:** Si el sistema cambió, actualiza riesgos identificados
3. **Mantén Historial:** No elimines versiones anteriores, son parte del historial

### Al Notificar

1. **Verifica Requisitos:** Solo notifica si riesgo ≥ 0.75
2. **Revisa Información:** Asegúrate de que toda la información sea correcta antes de notificar
3. **Guarda Confirmación:** El ID de notificación se guarda automáticamente

---

## ❓ PREGUNTAS FRECUENTES

### ¿Puedo editar una evaluación completada?
No, solo se pueden editar evaluaciones en estado "Borrador". Para actualizar una evaluación completada, crea una nueva versión.

### ¿Cuándo debo notificar a autoridades?
Solo cuando el riesgo final calculado sea ≥ 0.75. La notificación es automática y obligatoria.

### ¿Puedo tener múltiples evaluaciones FRIA para un proyecto?
Sí, un proyecto puede tener múltiples evaluaciones FRIA. Esto es útil para versiones y re-evaluaciones.

### ¿Qué pasa si el score de validación cruzada es < 0.70?
Debes proporcionar una justificación explicando las inconsistencias detectadas.

### ¿Puedo descargar la evaluación en PDF?
Sí, desde la pantalla de detalle, haz clic en "Exportar PDF".

---

**Última actualización:** Diciembre 2025


