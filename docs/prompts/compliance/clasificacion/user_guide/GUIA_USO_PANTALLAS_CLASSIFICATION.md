# 📱 GUÍA DE USO - PANTALLAS DE CLASIFICACIÓN

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo de Clasificación](#acceso-al-módulo-de-clasificación)
2. [Pantalla 1: Clasificador de Alto Riesgo](#pantalla-1-clasificador-de-alto-riesgo)
3. [Pantalla 2: Catálogo de Categorías Anexo III](#pantalla-2-catálogo-de-categorías-anexo-iii)
4. [Pantalla 3: Lista de Proyectos Clasificados](#pantalla-3-lista-de-proyectos-clasificados)
5. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)
6. [Procesos Automáticos](#procesos-automáticos)

---

## 🚪 ACCESO AL MÓDULO DE CLASIFICACIÓN

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **Classification** en el sidebar
3. Se abrirá la pantalla principal del Clasificador de Alto Riesgo

### Rutas de Acceso

- **Clasificador Principal:** `/governance/compliance/classification`
- **Catálogo de Categorías:** `/governance/compliance/classification/annex-iii-categories`
- **Lista de Proyectos:** `/governance/compliance/classification/projects`

### Navegación Contextual

Las pantallas están interconectadas:
- Desde el Clasificador puedes navegar al Catálogo de Categorías
- Desde la Lista de Proyectos puedes editar una clasificación existente
- El Catálogo está disponible desde cualquier pantalla

---

## 📊 PANTALLA 1: CLASIFICADOR DE ALTO RIESGO

### Ubicación
`/governance/compliance/classification`

### Descripción
Pantalla principal para clasificar proyectos como sistemas de alto riesgo según el Anexo III del EU AI Act.

### Componentes Principales

#### 1. **Header con Información del Proyecto**

**Información Mostrada (Solo Lectura):**
- **ID del Proyecto**
- **Nombre del Proyecto**
- **Descripción del Proyecto**
- **Estado del Proyecto**
- **Fecha de Creación**

**Botones de Acción:**
- **Volver:** Regresa a la lista de proyectos
- **Cancelar:** Cancela el proceso de clasificación

#### 2. **Tab: General**

**Componentes:**
- ✅ **Información del Proyecto:** Visualización en modo solo lectura
- ✅ **Descripción del Sistema:** Descripción detallada del sistema de IA

#### 3. **Tab: Clasificación**

**Componentes:**

##### A. Sugerencia Automática con IA (Opcional)
- **Botón:** "Obtener Sugerencias con IA" (con icono de Sparkles ✨)
- **Funcionalidad:**
  - Analiza la descripción del proyecto usando IA/LLM
  - Sugiere las 3 categorías más relevantes del Anexo III
  - Muestra explicaciones de por qué cada categoría es relevante
  - Incluye score de relevancia (0-100%)

**Ejemplo de Uso:**
1. Completar la descripción del proyecto
2. Hacer clic en "Obtener Sugerencias con IA"
3. Esperar a que el sistema analice (puede tardar unos segundos)
4. Revisar las 3 sugerencias con sus explicaciones
5. Seleccionar una de las sugerencias o elegir manualmente

##### B. Selección de Categoría Principal
- **Selector:** Dropdown con las 8 categorías principales del Anexo III
  - III.1 - Sistemas de identificación biométrica remota
  - III.2 - Sistemas de gestión e infraestructura crítica
  - III.3 - Sistemas de educación y formación profesional
  - III.4 - Sistemas de empleo, gestión de trabajadores y acceso al trabajo
  - III.5 - Sistemas de acceso a servicios privados esenciales y públicos esenciales
  - III.6 - Sistemas de aplicación de la ley
  - III.7 - Sistemas de migración, asilo y control fronterizo
  - III.8 - Sistemas de administración de justicia y procesos democráticos

**Validación:** Campo obligatorio

##### C. Selección de Subcategorías
- **Selector Multi-Select:** Permite seleccionar múltiples subcategorías
- **Dependencia:** Solo se muestran subcategorías de la categoría principal seleccionada
- **Opcional:** Aunque es opcional, se recomienda seleccionar subcategorías específicas

**Ejemplo:**
- Si seleccionas III.5, verás subcategorías como:
  - III.5.a - Evaluación de solvencia crediticia
  - III.5.b - Servicios públicos esenciales
  - III.5.c - Prestaciones públicas esenciales

##### D. Justificación Obligatoria
- **Campo:** Textarea multilínea
- **Validaciones Automáticas:**
  - ✅ **Mínimo 100 caracteres:** Debe tener al menos 100 caracteres
  - ✅ **Mencionar categoría:** Debe mencionar el nombre de la categoría seleccionada
  - ✅ **2+ palabras clave de riesgo:** Debe contener al menos 2 palabras clave relacionadas con riesgo

**Palabras Clave de Riesgo Reconocidas:**
- riesgo, alto riesgo, vulnerable, crítico, critical
- impacta, afecta, derechos fundamentales, seguridad, salud

**Mensajes de Validación en Tiempo Real:**
- ❌ "Justificación debe tener al menos 100 caracteres. Actual: X caracteres"
- ❌ "Justificación debe mencionar la categoría seleccionada: [Nombre Categoría]"
- ❌ "Justificación debe contener al menos 2 palabras clave de riesgo. Encontradas: X"

**Ejemplo de Justificación Válida:**
```
Este sistema de evaluación de solvencia crediticia debe clasificarse como de alto riesgo
según la categoría III.5 porque afecta significativamente el acceso de los ciudadanos a
servicios financieros esenciales. El sistema tiene un impacto crítico en los derechos
fundamentales de las personas, ya que puede determinar su capacidad para acceder a
préstamos y servicios bancarios, lo cual es esencial para su bienestar económico y social.
Además, existe un riesgo alto de sesgos algorítmicos que podrían afectar
desproporcionadamente a grupos vulnerables.
```

#### 4. **Tab: Adicional**

##### A. Validación Art. 5 - Sistemas Prohibidos
- **Checkbox Obligatorio:** "Verifico que este sistema NO está prohibido según el Art. 5 del EU AI Act"
- **Validación:** Debe estar marcado para poder clasificar
- **Justificación Adicional (Opcional):** Campo de texto para explicar por qué no está prohibido

**Sistemas Prohibidos según Art. 5:**
- Sistemas de manipulación subliminal
- Sistemas que explotan vulnerabilidades de grupos específicos
- Sistemas de puntuación social por parte de autoridades públicas
- Sistemas de reconocimiento biométrico en tiempo real en espacios públicos (con excepciones)

##### B. Sector Regulado (Opcional)
- **Checkbox:** "Este sistema pertenece a un sector regulado"
- **Legislación del Anexo I (Opcional):** Selección múltiple de legislaciones aplicables

#### 5. **Botones de Acción**

- **Volver:** Regresa sin guardar cambios
- **Cancelar:** Cancela el proceso
- **Clasificar como Sistema de Alto Riesgo:** Ejecuta la clasificación (solo habilitado si todas las validaciones pasan)

### Validaciones en Tiempo Real

El sistema valida automáticamente mientras escribes:

1. **Validación de Longitud:** Muestra contador de caracteres
2. **Validación de Categoría:** Verifica que se haya seleccionado una categoría
3. **Validación de Justificación:** Revisa longitud, mención de categoría y palabras clave
4. **Validación Art. 5:** Verifica que el checkbox esté marcado

### Mensajes de Éxito y Error

- ✅ **Éxito:** "Proyecto clasificado exitosamente como sistema de alto riesgo"
- ❌ **Error:** Mensajes específicos para cada validación fallida
- ⚠️ **Advertencia:** Alertas informativas sobre requisitos

### Flujo Completo de Clasificación

1. **Acceder al Clasificador**
   - Navegar desde el menú o desde la lista de proyectos

2. **Revisar Información del Proyecto**
   - Verificar que es el proyecto correcto
   - Revisar descripción del sistema

3. **Obtener Sugerencias con IA (Opcional)**
   - Hacer clic en "Obtener Sugerencias con IA"
   - Revisar las 3 sugerencias

4. **Seleccionar Categoría**
   - Seleccionar categoría principal del Anexo III
   - Seleccionar subcategorías relevantes (recomendado)

5. **Escribir Justificación**
   - Escribir justificación detallada (mínimo 100 caracteres)
   - Asegurarse de mencionar la categoría
   - Incluir palabras clave de riesgo

6. **Validar Art. 5**
   - Marcar checkbox de verificación
   - Agregar justificación adicional si es necesario

7. **Clasificar**
   - Hacer clic en "Clasificar como Sistema de Alto Riesgo"
   - Esperar confirmación de éxito
   - El sistema redirige automáticamente o muestra mensaje de éxito

---

## 📚 PANTALLA 2: CATÁLOGO DE CATEGORÍAS ANEXO III

### Ubicación
`/governance/compliance/classification/annex-iii-categories`

### Descripción
Catálogo completo de las 8 categorías principales y sus subcategorías del Anexo III del EU AI Act.

### Componentes Principales

#### 1. **Header**

- **Título:** "Catálogo de Categorías Anexo III"
- **Descripción:** Información sobre el propósito del catálogo
- **Búsqueda:** Campo de búsqueda para filtrar por código, nombre o descripción

#### 2. **Listado de Categorías**

**Estructura:**
- **8 Categorías Principales:** Cada una en una tarjeta expandible
- **Iconos:** Indicador visual para expandir/colapsar
- **Información por Categoría:**
  - Código (ej: III.1)
  - Nombre completo
  - Descripción
  - Número de subcategorías
  - Ejemplos de sistemas

**Acciones por Categoría:**
- **Expandir/Colapsar:** Ver/ocultar subcategorías
- **Editar (Admin):** Modificar categoría
- **Agregar Subcategoría (Admin):** Crear nueva subcategoría

#### 3. **Subcategorías**

**Al expandir una categoría, se muestran:**
- Lista de subcategorías con:
  - Código (ej: III.1.a)
  - Nombre
  - Descripción
  - Referencias legales

**Acciones por Subcategoría (Admin):**
- **Editar:** Modificar subcategoría
- **Eliminar:** Eliminar subcategoría

#### 4. **Funcionalidades de Búsqueda**

- **Búsqueda por Texto:** Busca en código, nombre y descripción
- **Filtrado en Tiempo Real:** Los resultados se filtran mientras escribes
- **Resaltado:** Los términos buscados se resaltan en los resultados

#### 5. **CRUD de Categorías (Solo Admin)**

**Crear Categoría:**
1. Hacer clic en botón "Agregar Categoría" (solo visible para admins)
2. Completar formulario:
   - Código (ej: III.1)
   - Nombre
   - Descripción
3. Guardar

**Editar Categoría:**
1. Hacer clic en botón "Editar" de una categoría
2. Modificar campos en el diálogo
3. Guardar cambios

**Eliminar Categoría:**
1. Hacer clic en botón "Eliminar" de una categoría
2. Confirmar eliminación
3. La categoría se elimina (si no tiene proyectos asociados)

**Crear Subcategoría:**
1. Expandir categoría principal
2. Hacer clic en "Agregar Subcategoría"
3. Completar formulario:
   - Código (ej: III.1.a)
   - Nombre
   - Descripción
4. Guardar

**Editar/Eliminar Subcategoría:**
- Similar al proceso de categorías

### Ejemplo de Uso

**Búsqueda de Categoría:**
1. Escribir "biométrica" en el campo de búsqueda
2. El sistema muestra la categoría III.1 y sus subcategorías
3. Hacer clic para expandir y ver detalles

**Consulta de Subcategorías:**
1. Expandir categoría III.5
2. Ver todas las subcategorías relacionadas con servicios esenciales
3. Revisar descripciones y ejemplos

---

## 📋 PANTALLA 3: LISTA DE PROYECTOS CLASIFICADOS

### Ubicación
`/governance/compliance/classification/projects`

### Descripción
Vista completa de todos los proyectos que han sido clasificados como sistemas de alto riesgo.

### Componentes Principales

#### 1. **Header**

- **Título:** "Proyectos Clasificados como Alto Riesgo"
- **Descripción:** Información sobre la lista
- **Botón:** "Nuevo Proyecto" o "Clasificar Proyecto" (navega al clasificador)

#### 2. **Filtros**

**Filtros Disponibles:**
- **Búsqueda:** Buscar por nombre o descripción del proyecto
- **Categoría:** Filtrar por categoría del Anexo III (dropdown)
  - Todas
  - III.1, III.2, III.3, etc.
- **Estado:** Filtrar por estado del proyecto
  - Todos
  - Activo
  - Pendiente Revisión
  - Archivado

**Botón Limpiar:** Resetea todos los filtros

#### 3. **Estadísticas**

**Tarjetas con Métricas:**
- **Total de Proyectos:** Número total de proyectos clasificados
- **Activos:** Proyectos con estado activo
- **Pendientes de Revisión:** Proyectos que requieren revisión
- **Por Categoría:** Distribución gráfica o lista de proyectos por categoría

#### 4. **Tabla de Proyectos**

**Columnas:**
- **ID:** Identificador único del proyecto
- **Nombre:** Nombre del proyecto
- **Categoría:** Código y nombre de la categoría aplicada (ej: III.5)
- **Subcategorías:** Lista de subcategorías seleccionadas
- **Fecha de Clasificación:** Cuándo se clasificó
- **Clasificado Por:** Usuario que realizó la clasificación
- **Estado:** Badge con estado (Activo, Pendiente, Archivado)

**Acciones por Proyecto:**
- **Ver Detalles (👁️):** Abre diálogo con información completa
- **Editar (✏️):** Navega al clasificador para actualizar clasificación
- **Ver Incidentes (si aplica):** Navega a gestión de incidentes PMM
- **Ver Acciones (si aplica):** Navega a acciones correctoras PMM

#### 5. **Paginación**

- **10 proyectos por página:** Configurable
- **Navegación:** Botones anterior/siguiente
- **Indicador:** "Mostrando X-Y de Z proyectos"

### Funcionalidades

#### Ver Detalles de un Proyecto

1. Hacer clic en botón "Ver Detalles" (👁️)
2. Se abre diálogo con:
   - Información completa del proyecto
   - Categoría y subcategorías aplicadas
   - Justificación de clasificación
   - Fecha y usuario que clasificó
   - Estado del workflow de compliance
   - Próximos pasos requeridos

#### Editar Clasificación

1. Hacer clic en botón "Editar" (✏️)
2. Se navega al clasificador con los datos precargados
3. Modificar categoría, subcategorías o justificación
4. Guardar cambios (se mantiene historial)

#### Filtrar y Buscar

1. Usar filtros para encontrar proyectos específicos
2. Combinar múltiples filtros (categoría + estado + búsqueda)
3. Los resultados se actualizan en tiempo real

---

## 📖 TUTORIALES PASO A PASO

### Tutorial 1: Clasificar un Proyecto Nuevo

**Objetivo:** Clasificar un proyecto de IA como sistema de alto riesgo.

**Pasos:**

1. **Acceder al Clasificador**
   - Ir a Governance → Compliance → Classification
   - O desde lista de proyectos, hacer clic en "Clasificar Proyecto"

2. **Seleccionar Proyecto**
   - Si hay selector de proyecto, seleccionarlo
   - O el proyecto ya está pre-seleccionado desde la navegación

3. **Revisar Información**
   - Revisar nombre y descripción del proyecto
   - Verificar que es el proyecto correcto

4. **Obtener Sugerencias con IA (Recomendado)**
   - Ir a tab "Clasificación"
   - Hacer clic en "Obtener Sugerencias con IA"
   - Esperar análisis (10-30 segundos)
   - Revisar las 3 sugerencias

5. **Seleccionar Categoría**
   - Seleccionar categoría principal del dropdown
   - Revisar subcategorías disponibles
   - Seleccionar subcategorías relevantes (opcional pero recomendado)

6. **Escribir Justificación**
   - Escribir justificación detallada (mínimo 100 caracteres)
   - Asegurarse de mencionar el nombre de la categoría
   - Incluir palabras clave de riesgo (riesgo, crítico, afecta, etc.)
   - Revisar validaciones en tiempo real

7. **Validar Art. 5**
   - Ir a tab "Adicional"
   - Marcar checkbox "Verifico que este sistema NO está prohibido"
   - Agregar justificación adicional si es necesario

8. **Clasificar**
   - Revisar que todas las validaciones pasan (sin errores en rojo)
   - Hacer clic en "Clasificar como Sistema de Alto Riesgo"
   - Esperar confirmación de éxito

9. **Resultado**
   - Mensaje de éxito: "Proyecto clasificado exitosamente"
   - El proyecto aparece en la lista de proyectos clasificados
   - Se dispara automáticamente el workflow de compliance

---

### Tutorial 2: Consultar Proyectos Clasificados

**Objetivo:** Encontrar y revisar proyectos clasificados como de alto riesgo.

**Pasos:**

1. **Acceder a la Lista**
   - Ir a Governance → Compliance → Classification → Projects
   - O hacer clic en "Ver Proyectos Clasificados" desde el clasificador

2. **Revisar Estadísticas**
   - Ver métricas en las tarjetas superiores
   - Entender distribución por categoría

3. **Filtrar Proyectos**
   - Usar filtro de categoría para ver solo proyectos de una categoría específica
   - Usar filtro de estado para ver activos/pendientes
   - Usar búsqueda para encontrar por nombre

4. **Ver Detalles**
   - Hacer clic en "Ver Detalles" de un proyecto
   - Revisar información completa en el diálogo

5. **Editar si es Necesario**
   - Si necesita actualizar, hacer clic en "Editar"
   - Modificar y guardar cambios

---

### Tutorial 3: Consultar Catálogo de Categorías

**Objetivo:** Entender las categorías del Anexo III y sus subcategorías.

**Pasos:**

1. **Acceder al Catálogo**
   - Ir a Governance → Compliance → Classification → Annex III Categories
   - O desde el clasificador, hacer clic en "Ver Catálogo"

2. **Explorar Categorías**
   - Ver las 8 categorías principales
   - Hacer clic para expandir y ver subcategorías

3. **Buscar Categoría Específica**
   - Usar campo de búsqueda
   - Escribir código (ej: "III.5") o nombre (ej: "servicios")

4. **Revisar Detalles**
   - Leer descripciones de categorías
   - Revisar ejemplos de sistemas
   - Ver referencias legales

---

## 🔄 PROCESOS AUTOMÁTICOS

### 1. **Workflow de Compliance (BPMN)**

**Cuándo se Dispara:**
- Automáticamente después de clasificar un proyecto como de alto riesgo
- Inmediatamente después de guardar la clasificación

**Qué Hace:**
1. Crea instancia de workflow de compliance
2. Asigna tareas iniciales:
   - Registro en Base de Datos UE (Art. 49)
   - Configuración de PMM Plan (Art. 20)
   - Inicio de FRIA Assessment (Art. 27)
   - Preparación de documentación técnica

**Estado del Workflow:**
- Se puede consultar desde los detalles del proyecto
- Muestra tareas pendientes y completadas

### 2. **Notificaciones Automáticas**

**Quién Recibe Notificaciones:**
- Compliance Officers
- Project Managers del proyecto
- Stakeholders relevantes

**Qué se Notifica:**
- Proyecto clasificado como de alto riesgo
- Tareas pendientes del workflow
- Recordatorios de próximos pasos

### 3. **Actualización de Métricas**

**Cuándo se Actualiza:**
- Automáticamente después de cada clasificación
- En tiempo real en el dashboard

**Qué se Actualiza:**
- Contador de proyectos clasificados
- Distribución por categoría
- Estadísticas generales

---

## ❓ PREGUNTAS FRECUENTES

### ¿Puedo editar una clasificación después de guardarla?

**Sí**, puedes editar una clasificación existente:
1. Ir a la lista de proyectos clasificados
2. Hacer clic en "Editar" del proyecto
3. Modificar categoría, subcategorías o justificación
4. Guardar cambios

**Nota:** Se mantiene historial de cambios para auditoría.

### ¿Qué pasa si no paso las validaciones?

El sistema mostrará mensajes de error específicos:
- Si la justificación es muy corta: "Debe tener al menos 100 caracteres"
- Si no menciona la categoría: "Debe mencionar la categoría seleccionada"
- Si faltan palabras clave: "Debe contener al menos 2 palabras clave de riesgo"

Corrige los errores y vuelve a intentar.

### ¿Puedo usar el sistema sin conexión a internet?

**No**, el sistema requiere conexión a internet para:
- Acceder a la base de datos
- Obtener sugerencias con IA
- Sincronizar cambios

### ¿Cómo sé si un proyecto ya está clasificado?

Puedes verificar de dos formas:
1. Ir a la lista de proyectos clasificados y buscar el proyecto
2. Si intentas clasificar un proyecto ya clasificado, el sistema mostrará la clasificación existente y permitirá editarla

---

**Última Actualización:** Diciembre 2025
**Versión del Módulo:** 1.0
**Estado:** ✅ Operativo y listo para producción
