# 📱 GUÍA DE USO - PANTALLAS PROHIBITED SYSTEMS

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo](#acceso-al-módulo)
2. [Pantalla Principal de Sistemas Prohibidos](#pantalla-principal-de-sistemas-prohibidos)
3. [Pantalla de Detalle de Detección](#pantalla-de-detalle-de-detección)
4. [Pantalla de Catálogo](#pantalla-de-catálogo)
5. [Integración con Clasificación](#integración-con-clasificación)
6. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **Prohibited Systems** en el sidebar
3. Se abrirá la pantalla principal de Sistemas Prohibidos

### Navegación Contextual

**IMPORTANTE:** La verificación de sistemas prohibidos también se realiza automáticamente durante el proceso de clasificación de proyectos.

---

## 📊 PANTALLA PRINCIPAL DE SISTEMAS PROHIBIDOS

### Ubicación
`/governance/compliance/prohibited-systems`

### Descripción
Vista centralizada con métricas principales y listado de sistemas detectados.

### Componentes

#### 1. **Header (Título y Acciones)**

**Layout:**
- **Izquierda:** Título "Sistemas Prohibidos" y subtítulo descriptivo
- **Derecha:** Botones de acción
  - **Ver Catálogo:** Navega a `/governance/compliance/prohibited-systems/catalog`
  - **Verificar Todos los Proyectos:** Ejecuta verificación masiva

#### 2. **Métricas Principales (3 Contadores)**

- **Sistemas Prohibidos Activos:** Total de sistemas detectados actualmente
- **Despliegues Bloqueados:** Número de proyectos con despliegue bloqueado
- **Total de Detecciones:** Total histórico de detecciones

#### 3. **Catálogo de Referencia (Card)**

Muestra información sobre las categorías del Art. 5:
- Art. 5.1.a - Manipulación Subliminal
- Art. 5.1.b - Explotación de Vulnerabilidades
- Art. 5.1.c - Social Scoring
- Art. 5.1.d - Identificación Biométrica Remota

Cada categoría muestra:
- Descripción breve
- Ejemplos de sistemas prohibidos

#### 4. **Sistemas Detectados (Grid de Cards)**

**Layout:** Grid responsive con máximo 3 cards por fila
- **Mobile:** 1 columna
- **Tablet (md):** 2 columnas
- **Desktop (lg):** 3 columnas

**Cada Card muestra:**
- **Nombre del Sistema Detectado**
- **Nombre del Proyecto**
- **Estado:** Badge con color según estado
  - `PROHIBITED` (rojo)
  - `WARNING` (amarillo)
  - `BLOCKED` (rojo oscuro)
  - `CLEAN` (verde)
  - `RESOLVED` (gris)
- **Nivel de Confianza:** Porcentaje (ej: 95%)
- **Tipo de Sistema:** Tipo de sistema de IA
- **Sistemas Detectados:** Lista de sistemas del catálogo que coincidieron
- **Fecha de Detección:** Fecha y hora
- **Botones de Acción:**
  - **Ver Detalles:** Navega a `/governance/compliance/prohibited-systems/{id}`
  - **Bloquear Despliegue:** Bloquea el despliegue del proyecto
  - **Marcar como Falso Positivo:** Marca la detección como incorrecta

#### 5. **Filtros**

- **Búsqueda:** Campo de texto para buscar por nombre de proyecto o sistema
- **Filtro por Estado:** Dropdown con opciones:
  - Todos
  - Prohibido
  - Advertencia
  - Limpio
  - Bloqueado
  - Resuelto

---

## 🔍 PANTALLA DE DETALLE DE DETECCIÓN

### Ubicación
`/governance/compliance/prohibited-systems/[id]`

### Descripción
Vista detallada de una detección específica con información completa del proyecto y la detección.

### Componentes

#### 1. **Header**

- **Título:** "Detalle de Detección de Sistema Prohibido"
- **Botón Volver:** Navega a `/governance/compliance/prohibited-systems`

#### 2. **Grid de 2 Columnas (50% cada una)**

**Columna 1: Información del Proyecto**
- **Nombre del Proyecto**
- **Descripción del Proyecto**
- **Modelos Asociados:** Lista de modelos vinculados
- **Estado del Proyecto:** Badge con estado actual

**Columna 2: Detalles de Detección**
- **Sistema Detectado:** Nombre del sistema del catálogo
- **Nivel de Confianza:** Porcentaje
- **Palabras Clave Coincidentes:** Badges con keywords detectadas
  - Estilo: `bg-red-100 text-red-800 border-red-300`
- **Evidencia:** Texto descriptivo de la evidencia
- **Fecha de Detección:** Fecha y hora

#### 3. **Estado y Acciones (100% ancho)**

- **Estado Actual:**
  - Badge indicando si el despliegue está bloqueado o permitido
- **Acciones Disponibles:**
  - **Bloquear Despliegue:** Si no está bloqueado
  - **Marcar como Falso Positivo:** Para marcar detección incorrecta
  - **Contactar Propietario del Proyecto:** Para comunicación

---

## 📚 PANTALLA DE CATÁLOGO

### Ubicación
`/governance/compliance/prohibited-systems/catalog`

### Descripción
Vista del catálogo completo de sistemas prohibidos definidos en el sistema.

### Componentes

#### 1. **Header**

- **Izquierda:** Título "Catálogo de Sistemas Prohibidos" y subtítulo
- **Derecha:** Botón "Volver a Verificación" que navega a `/governance/compliance/prohibited-systems`

#### 2. **Filtros**

- **Búsqueda:** Campo de texto para buscar por nombre
- **Filtro por Categoría:** Dropdown con opciones:
  - Todas
  - Art. 5.1.a - Manipulación Subliminal
  - Art. 5.1.b - Explotación de Vulnerabilidades
  - Art. 5.1.c - Social Scoring
  - Art. 5.1.d - Identificación Biométrica Remota
- **Filtro por Estado:** Dropdown con opciones:
  - Todos
  - Activo
  - Inactivo

#### 3. **Grid de Catálogo (4 Columnas)**

**Layout:** Grid responsive con máximo 4 cards por fila
- **Mobile:** 1 columna
- **Tablet (md):** 2 columnas
- **Desktop (lg):** 3 columnas
- **Large Desktop (xl):** 4 columnas

**Cada Card muestra:**
- **Nombre del Sistema**
- **Categoría:** Badge con categoría (Art. 5.1.a, b, c, d)
- **Estado:** Badge Activo/Inactivo
- **Descripción:** Texto descriptivo (truncado)
- **Palabras Clave:** Lista de keywords con badges
  - Estilo: `bg-blue-100 text-blue-800 border-blue-300`
- **Fechas:** Creado y Actualizado
- **Botón Visualizar:** Abre dialog de visualización (read-only)

#### 4. **Dialog de Visualización**

**Trigger:** Botón "Visualizar" en cada card

**Contenido (Read-Only):**
- **Nombre:** Campo de texto (readonly)
- **Categoría:** Dropdown (readonly)
- **Descripción:** Textarea (readonly)
- **Palabras Clave:** Lista de badges
- **Estado:** Badge Activo/Inactivo

**Botones:**
- **Cerrar:** Cierra el dialog

**Nota:** El dialog es de solo lectura. No se permite edición ni eliminación desde la interfaz de usuario.

---

## 🔄 INTEGRACIÓN CON CLASIFICACIÓN

### Ubicación
`/governance/compliance/classification`

### Descripción
La verificación de sistemas prohibidos está integrada automáticamente en el proceso de clasificación.

### Componentes

#### 1. **Verificación Automática**

**Al cargar el proyecto:**
- El sistema verifica automáticamente si el proyecto usa sistemas prohibidos
- Muestra un indicador de carga mientras verifica

**Antes de clasificar:**
- El sistema verifica nuevamente antes de permitir la clasificación
- Si se detecta un sistema prohibido, se muestra un Alert y se bloquea la clasificación

#### 2. **Alert de Sistema Prohibido**

**Cuándo se muestra:**
- Cuando se detecta al menos un sistema prohibido

**Contenido:**
- **Título:** "Sistema Prohibido Detectado"
- **Mensaje:** Información sobre los sistemas detectados
- **Lista de Sistemas:** Muestra cada sistema detectado con:
  - Nombre
  - Categoría
  - Nivel de confianza
- **Acción Sugerida:** "Por favor, revise la información antes de continuar con la clasificación"

**Comportamiento:**
- El Alert bloquea la posibilidad de continuar con la clasificación
- El usuario debe revisar el proyecto antes de proceder

#### 3. **Indicador de Verificación Exitosa**

**Cuándo se muestra:**
- Cuando la verificación se completa y no se detectan sistemas prohibidos

**Contenido:**
- **Mensaje:** "No se detectaron sistemas prohibidos en este proyecto"
- **Icono:** CheckCircle (verde)

---

## 📝 TUTORIALES PASO A PASO

### Tutorial 1: Verificar un Proyecto Manualmente

1. **Acceder a Sistemas Prohibidos**
   - Navegar a Governance → Compliance → Prohibited Systems

2. **Ver Listado de Detecciones**
   - La pantalla muestra automáticamente todas las detecciones existentes

3. **Filtrar Detecciones (Opcional)**
   - Usar el campo de búsqueda para buscar por nombre
   - Usar el filtro de estado para filtrar por estado

4. **Ver Detalle de una Detección**
   - Hacer clic en "Ver Detalles" en una card
   - Se abre la pantalla de detalle con información completa

5. **Tomar Acción**
   - Si es un falso positivo: Hacer clic en "Marcar como Falso Positivo"
   - Si es confirmado: Hacer clic en "Bloquear Despliegue"

---

### Tutorial 2: Consultar el Catálogo

1. **Acceder al Catálogo**
   - Desde la pantalla principal, hacer clic en "Ver Catálogo"
   - O navegar directamente a `/governance/compliance/prohibited-systems/catalog`

2. **Filtrar Catálogo (Opcional)**
   - Usar el campo de búsqueda para buscar por nombre
   - Usar el filtro de categoría para filtrar por Art. 5.1.a, b, c, d
   - Usar el filtro de estado para filtrar por Activo/Inactivo

3. **Ver Detalle de un Sistema**
   - Hacer clic en "Visualizar" en una card
   - Se abre un dialog con información completa (read-only)

4. **Volver**
   - Hacer clic en "Cerrar" para cerrar el dialog
   - Hacer clic en "Volver a Verificación" para volver a la pantalla principal

---

### Tutorial 3: Verificar Durante Clasificación

1. **Iniciar Clasificación**
   - Navegar a Governance → Compliance → Classification
   - Cargar un proyecto para clasificar

2. **Verificación Automática**
   - El sistema verifica automáticamente al cargar el proyecto
   - Muestra un indicador de carga

3. **Resultado de Verificación**
   - **Si NO se detecta sistema prohibido:**
     - Se muestra un mensaje de éxito
     - Se permite continuar con la clasificación
   - **Si SÍ se detecta sistema prohibido:**
     - Se muestra un Alert con la información
     - Se bloquea la clasificación
     - Se debe revisar el proyecto antes de continuar

4. **Revisar y Continuar**
   - Si es un falso positivo: Contactar al administrador
   - Si es confirmado: Modificar el proyecto antes de clasificar

---

## 🎨 ELEMENTOS VISUALES

### Badges de Estado

- **PROHIBITED:** Rojo (`bg-red-100 text-red-800`)
- **WARNING:** Amarillo (`bg-yellow-100 text-yellow-800`)
- **BLOCKED:** Rojo oscuro (`bg-red-900 text-red-100`)
- **CLEAN:** Verde (`bg-green-100 text-green-800`)
- **RESOLVED:** Gris (`bg-gray-100 text-gray-800`)

### Badges de Keywords

- **Detectadas (Detail Page):** Rojo (`bg-red-100 text-red-800 border-red-300`)
- **Catálogo:** Azul (`bg-blue-100 text-blue-800 border-blue-300`)

### Iconos

- **XCircle:** Sistema prohibido
- **AlertTriangle:** Advertencia
- **Shield:** Protección/Compliance
- **CheckCircle:** Verificación exitosa
- **Ban:** Bloqueo
- **FileText:** Visualizar

---

## 📚 REFERENCIAS

- **Guía Funcional:** Ver `GUIA_FUNCIONAL_PROHIBITED_SYSTEMS.md`
- **Documentación Técnica:** Ver `DEVELOPER_GUIDE_BACKEND.md` y `DEVELOPER_GUIDE_FRONTEND.md`
- **Guía BPMN:** Ver `BPMN_WORKFLOW_GUIDE.md`
