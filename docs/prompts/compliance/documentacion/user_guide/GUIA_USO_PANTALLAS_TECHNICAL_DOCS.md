# 📱 GUÍA DE USO - PANTALLAS TECHNICAL DOCUMENTATION

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo](#acceso-al-módulo)
2. [Listado de Modelos](#listado-de-modelos)
3. [Detalle de Documentación](#detalle-de-documentación)
4. [Completar Documentación (BPMN)](#completar-documentación-bpmn)
5. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **Technical Documentation** en el sidebar
3. Se abrirá la pantalla principal de listado de modelos

### URL Directa

```
/governance/compliance/technical-docs
```

---

## 📊 LISTADO DE MODELOS

### Ubicación
`/governance/compliance/technical-docs`

### Descripción
Pantalla principal que muestra todos los modelos del sistema con su estado de documentación técnica.

### Componentes de la Pantalla

#### 1. **Encabezado**
- **Título:** "Documentación Técnica"
- **Subtítulo:** Descripción del módulo según Art. 11 y Anexo IV EU AI Act

#### 2. **Filtros**

Ubicados en la parte superior, permiten filtrar los modelos:

- **Buscar por Modelo:**
  - Campo de búsqueda de texto libre
  - Busca por nombre del modelo
  - Filtra en tiempo real mientras escribes

- **Filtrar por Score:**
  - Dropdown con opciones:
    - **Todos:** Muestra todos los modelos
    - **High (≥80%):** Solo modelos con score alto
    - **Medium (50-79%):** Solo modelos con score medio
    - **Low (<50%):** Solo modelos con score bajo

- **Filtrar por Completitud:**
  - Dropdown con opciones:
    - **Todos:** Muestra todos los modelos
    - **Completo:** Solo modelos con documentación completa (100%)
    - **Incompleto:** Solo modelos con documentación incompleta (<100%)

#### 3. **Tabla de Modelos**

La tabla muestra las siguientes columnas:

| Columna | Descripción |
|---------|-------------|
| **Modelo** | Nombre del modelo |
| **Score** | Porcentaje de completitud (0-100%) con barra de progreso visual |
| **Estado** | Badge indicando si está completo o incompleto |
| **Secciones** | X/11 secciones completas (ejemplo: 8/11) |
| **Acciones** | Botones de acción disponibles |

#### 4. **Badges de Estado**

- **✅ Completo (Verde):** Score = 100%, todas las secciones completas
- **⚠️ Incompleto (Amarillo/Naranja):** Score < 100%, faltan secciones
- **🔴 Crítico (Rojo):** Score < 50%, documentación muy incompleta

#### 5. **Acciones por Modelo**

Cada fila tiene un botón **"Ver"** que:
- Abre la pantalla de detalle de documentación para ese modelo
- Muestra todas las secciones y permite editarlas

### Funcionalidades

1. **Búsqueda y Filtrado:**
   - Buscar modelos por nombre
   - Filtrar por nivel de completitud (score)
   - Filtrar por estado (completo/incompleto)

2. **Visualización de Estado:**
   - Ver rápidamente qué modelos tienen documentación completa
   - Identificar modelos que requieren atención

3. **Navegación:**
   - Acceder rápidamente al detalle de cualquier modelo

---

## 📄 DETALLE DE DOCUMENTACIÓN

### Ubicación
`/governance/compliance/technical-docs/[modelId]`

Ejemplo: `/governance/compliance/technical-docs/1001`

### Descripción
Pantalla completa para gestionar la documentación técnica de un modelo específico.

### Componentes de la Pantalla

#### 1. **Botón Volver**
- Ubicado en la parte superior izquierda
- Vuelve a la pantalla de listado de modelos

#### 2. **Información del Modelo (Card Superior)**

Muestra:
- **Nombre del Modelo:** Nombre identificador del modelo
- **Score General:** Porcentaje de completitud (ejemplo: 85%)
- **Barra de Progreso:** Visualización gráfica del score

#### 3. **Acciones Rápidas**

Tres botones principales:

- **Generar Automáticamente:**
  - Genera contenido inicial para todas las secciones
  - Usa datos del modelo para pre-llenar contenido
  - Se muestra un mensaje de confirmación al completar

- **Validar Completitud:**
  - Verifica que las 11 secciones estén completas
  - Muestra resultado con:
    - Score actual
    - Secciones completas
    - Secciones faltantes (si las hay)
  - Mensaje de alerta con el resultado

- **Generar PDF:**
  - Genera un PDF completo de la documentación
  - Incluye todas las secciones completas
  - Se puede descargar después de la generación
  - Mensaje de confirmación cuando se completa

#### 4. **Listado de Secciones (11 Secciones del Anexo IV)**

Cada sección se muestra como un card con:

- **Nombre de la Sección:** (ejemplo: "1. General Description")
- **Estado de Completitud:**
  - ✅ Badge verde si está completa
  - ⚠️ Badge amarillo/naranja si está incompleta
- **Score de la Sección:** Porcentaje de completitud individual
- **Descripción:** Breve descripción de qué debe contener la sección
- **Botón Editar:** Abre el editor para esa sección

#### 5. **Editor de Sección**

Cuando haces clic en **"Editar"** en una sección:

- **Título:** Muestra el nombre de la sección
- **Estado y Score:** Badges indicando completitud
- **Área de Contenido:**
  - Textarea grande para editar el contenido
  - Muestra el contenido actual de la sección
  - Permite escribir y modificar texto libremente
- **Botones de Acción:**
  - **Guardar:** Guarda los cambios realizados
  - **Cancelar:** Cierra el editor sin guardar

### Flujo de Trabajo Típico

#### Paso 1: Generar Documentación Inicial

1. Hacer clic en **"Generar Automáticamente"**
2. Esperar el mensaje de confirmación
3. El contenido se pre-llena en todas las secciones

#### Paso 2: Revisar y Completar Secciones

1. Revisar cada sección en el listado
2. Identificar secciones incompletas (badge amarillo/naranja)
3. Hacer clic en **"Editar"** en una sección
4. Completar o mejorar el contenido
5. Hacer clic en **"Guardar"**
6. Repetir para todas las secciones faltantes

#### Paso 3: Validar Completitud

1. Hacer clic en **"Validar Completitud"**
2. Revisar el resultado mostrado en la alerta:
   - Score actual
   - Secciones completas (X/11)
   - Secciones faltantes (si las hay)
3. Si faltan secciones, completarlas según el paso 2

#### Paso 4: Generar PDF

1. Una vez que todas las secciones estén completas (score = 100%)
2. Hacer clic en **"Generar PDF"**
3. Esperar el mensaje de confirmación
4. El PDF queda disponible para descarga

---

## ✅ COMPLETAR DOCUMENTACIÓN (BPMN)

### Ubicación
`/governance/compliance/technical-docs/complete`

### Descripción
Pantalla especial para completar documentación técnica como parte de un proceso BPMN (Business Process Management Notation). Esta pantalla aparece cuando hay una tarea de usuario para completar documentación.

### Cuándo se Usa

- Como parte del proceso de evaluación de conformidad
- Cuando se necesita completar documentación faltante
- Como tarea asignada en un workflow BPMN

### Componentes de la Pantalla

#### 1. **Información del Sistema**

Card superior que muestra:
- **Nombre del Sistema**
- **ID de Evaluación:** ID del proceso BPMN
- **Score Global:** Porcentaje de completitud actual

#### 2. **Scores por Artículo**

- Muestra el score de completitud por sección/artículo
- Tabla o cards con cada sección y su estado

#### 3. **Secciones Incompletas**

Listado de solo las secciones que están incompletas:

- **Título de la Sección**
- **Estado:** Incompleta (badge amarillo/rojo)
- **Contenido Actual:** Muestra el contenido existente (si hay)
- **Editor Inline:** Permite editar directamente en la lista

#### 4. **Acciones**

- **Guardar Cambios:** Guarda las modificaciones realizadas
- **Validar:** Valida la completitud
- **Generar PDF:** Genera PDF de la documentación
- **Marcar como Completo:** Finaliza la tarea y continúa el proceso BPMN

### Flujo de Trabajo

1. **Revisar Secciones Faltantes:**
   - Ver la lista de secciones incompletas
   - Identificar qué contenido falta

2. **Completar Secciones:**
   - Editar cada sección incompleta
   - Completar el contenido requerido
   - Guardar cambios

3. **Validar:**
   - Validar que todas las secciones estén completas
   - Revisar el resultado

4. **Marcar como Completo:**
   - Solo disponible cuando todas las secciones están completas
   - Finaliza la tarea BPMN
   - El proceso continúa automáticamente

---

## 📚 TUTORIALES PASO A PASO

### Tutorial 1: Crear Documentación desde Cero

**Objetivo:** Crear documentación técnica completa para un nuevo modelo.

**Pasos:**

1. **Acceder al Módulo:**
   - Ir a Governance → Compliance → Technical Documentation
   - Ver la lista de modelos

2. **Seleccionar el Modelo:**
   - Buscar el modelo en la lista (usar el filtro si es necesario)
   - Hacer clic en **"Ver"**

3. **Generar Contenido Inicial:**
   - Hacer clic en **"Generar Automáticamente"**
   - Esperar confirmación de generación

4. **Revisar Secciones Generadas:**
   - Revisar cada sección en el listado
   - Verificar que el contenido generado sea relevante

5. **Completar Secciones Faltantes:**
   - Para cada sección incompleta (badge amarillo/naranja):
     - Hacer clic en **"Editar"**
     - Completar o mejorar el contenido
     - Hacer clic en **"Guardar"**

6. **Validar Completitud:**
   - Hacer clic en **"Validar Completitud"**
   - Verificar que el score sea 100%
   - Si faltan secciones, completarlas

7. **Generar PDF:**
   - Hacer clic en **"Generar PDF"**
   - Descargar el PDF generado
   - El PDF queda almacenado para auditorías

**Resultado:** Documentación técnica completa con score 100% y PDF generado.

---

### Tutorial 2: Actualizar Documentación Existente

**Objetivo:** Actualizar una sección específica de documentación existente.

**Pasos:**

1. **Acceder al Modelo:**
   - Ir a Technical Documentation
   - Buscar y seleccionar el modelo

2. **Identificar la Sección a Actualizar:**
   - Revisar el listado de secciones
   - Identificar la sección que necesita actualización

3. **Editar la Sección:**
   - Hacer clic en **"Editar"** en la sección
   - Modificar el contenido en el editor
   - Hacer clic en **"Guardar"**

4. **Regenerar PDF (Opcional):**
   - Si los cambios son significativos, regenerar el PDF
   - Hacer clic en **"Generar PDF"**

**Resultado:** Sección actualizada y PDF regenerado si es necesario.

---

### Tutorial 3: Completar Documentación en Proceso BPMN

**Objetivo:** Completar documentación como parte de una tarea BPMN.

**Pasos:**

1. **Acceder a la Tarea:**
   - Navegar a la tarea BPMN asignada
   - Se abre la pantalla de completar documentación

2. **Revisar Secciones Faltantes:**
   - Ver la lista de secciones incompletas
   - Revisar el contenido actual (si existe)

3. **Completar Cada Sección:**
   - Para cada sección incompleta:
     - Editar el contenido
     - Completar la información requerida
     - Guardar cambios

4. **Validar Completitud:**
   - Hacer clic en **"Validar"**
   - Verificar que todas las secciones estén completas

5. **Marcar como Completo:**
   - Hacer clic en **"Marcar como Completo"**
   - La tarea se completa y el proceso continúa

**Resultado:** Documentación completada y proceso BPMN continuado.

---

### Tutorial 4: Filtrar Modelos por Estado

**Objetivo:** Encontrar rápidamente modelos con documentación incompleta.

**Pasos:**

1. **Acceder al Listado:**
   - Ir a Technical Documentation

2. **Aplicar Filtro de Completitud:**
   - En el dropdown "Filtrar por Completitud"
   - Seleccionar **"Incompleto"**

3. **Aplicar Filtro de Score (Opcional):**
   - En el dropdown "Filtrar por Score"
   - Seleccionar **"Low (<50%)"** para ver los más críticos

4. **Revisar Resultados:**
   - La tabla muestra solo los modelos que cumplen los filtros
   - Identificar modelos que requieren atención

5. **Acceder a un Modelo:**
   - Hacer clic en **"Ver"** para abrir el detalle
   - Completar la documentación según el Tutorial 1

**Resultado:** Lista filtrada de modelos que requieren trabajo.

---

## 💡 CONSEJOS Y MEJORES PRÁCTICAS

### 1. **Usar Generación Automática como Base**

- La generación automática crea contenido inicial útil
- Siempre revisar y mejorar el contenido generado
- Completar manualmente las secciones que no se pueden generar

### 2. **Validar Regularmente**

- Validar la completitud después de completar cada sección
- Verificar el score periódicamente
- Asegurar 100% antes de considerar completa

### 3. **Guardar Frecuentemente**

- Guardar cambios después de editar cada sección
- No perder trabajo por no guardar

### 4. **Revisar Contenido Completo**

- Revisar todas las secciones antes de generar PDF
- Asegurar que el contenido sea coherente y completo
- Verificar que cumpla con los requisitos del Anexo IV

### 5. **Actualizar cuando el Sistema Cambie**

- Actualizar documentación cuando el modelo o sistema cambie
- Regenerar PDF después de cambios significativos
- Mantener la documentación actualizada

---

## ❓ PREGUNTAS FRECUENTES

### ¿Puedo editar múltiples secciones a la vez?

**Respuesta:** No, debes editar una sección a la vez. Después de guardar una sección, puedes editar la siguiente.

### ¿Qué pasa si cierro el editor sin guardar?

**Respuesta:** Los cambios no guardados se perderán. Siempre haz clic en **"Guardar"** antes de cerrar.

### ¿Puedo ver el historial de cambios?

**Respuesta:** El sistema guarda la fecha de última actualización, pero no un historial detallado de cambios.

### ¿El PDF incluye todas las secciones?

**Respuesta:** Sí, el PDF incluye todas las 11 secciones del Anexo IV, incluso si alguna está incompleta.

### ¿Puedo generar PDF sin completar todas las secciones?

**Respuesta:** Sí, puedes generar PDF en cualquier momento, pero para cumplimiento legal se requiere 100% de completitud.

---

## 📞 SOPORTE

Para más información:
- Consultar la **Guía Funcional** para conceptos generales
- Contactar al equipo de Compliance
- Revisar la documentación técnica del sistema

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.0
