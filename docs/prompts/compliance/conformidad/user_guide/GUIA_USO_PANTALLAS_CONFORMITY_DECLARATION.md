# 📱 GUÍA DE USO - PANTALLAS CONFORMITY DECLARATION

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo](#acceso-al-módulo)
2. [Pantalla de Proyectos](#pantalla-de-proyectos)
3. [Pantalla de Gestión de Declaraciones](#pantalla-de-gestión-de-declaraciones)
4. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **Conformity Declaration** en el sidebar
3. Se abrirá la pantalla de **Proyectos con Declaraciones**

### URL Directa

- **Pantalla de Proyectos:** `/governance/compliance/conformity-declaration/projects`
- **Gestión de Declaraciones:** `/governance/compliance/conformity-declaration-manager?projectId={id}`

---

## 📊 PANTALLA DE PROYECTOS

### Ubicación
`/governance/compliance/conformity-declaration/projects`

### Descripción
Vista centralizada que lista todos los proyectos y muestra información sobre sus declaraciones de conformidad.

### Componentes

#### 1. **Título y Descripción**
- Título: "Conformity Declaration - Projects"
- Descripción breve del módulo

#### 2. **Estadísticas Globales (5 Contadores)**
- **Total Proyectos:** Total de proyectos en el sistema
- **Proyectos con Declaraciones:** Proyectos que tienen al menos una declaración
- **Total Declaraciones:** Suma de todas las declaraciones
- **Declaraciones Firmadas:** Declaraciones en estado SIGNED
- **Borradores:** Declaraciones en estado DRAFT

#### 3. **Filtros y Búsqueda**
- **Filtro "Tiene Declaraciones":**
  - Opciones: "Todos", "Con Declaraciones", "Sin Declaraciones"
  - Filtra proyectos según si tienen declaraciones o no
- **Búsqueda:**
  - Campo de texto para buscar por nombre de proyecto
  - Búsqueda en tiempo real

#### 4. **Lista de Proyectos (Cards)**
Grid de cards mostrando cada proyecto:

**Información Mostrada por Card:**
- **Nombre del Proyecto** (título)
- **Descripción** del proyecto
- **Estadísticas del Proyecto:**
  - Total declaraciones
  - Firmadas
  - Borradores
- **Última Declaración:**
  - Fecha (si existe)
  - Estado (DRAFT/SIGNED)
  - Versión (ej: v1.2)
- **Badge:** "Sin Declaraciones" si no tiene ninguna

**Botones de Acción (por proyecto):**
- **Gestionar Declaraciones** → Abre gestión de declaraciones del proyecto
- **Crear Declaración** → Abre diálogo para crear nueva declaración (si hay assessments disponibles)

#### 5. **Paginación**
- Controles de paginación en la parte inferior
- Información: "Mostrando X-Y de Z proyectos"
- Botones: Anterior, Siguiente
- Selector de tamaño de página (12, 24, 48)

### Flujo de Uso

1. **Ver Todos los Proyectos**
   - La pantalla carga automáticamente todos los proyectos
   - Ver estadísticas globales en la parte superior

2. **Filtrar Proyectos**
   - Seleccionar filtro "Con Declaraciones" para ver solo proyectos con declaraciones
   - Seleccionar "Sin Declaraciones" para ver proyectos que necesitan declaraciones

3. **Buscar Proyecto**
   - Escribir en el campo de búsqueda
   - Los resultados se filtran automáticamente

4. **Acceder a Gestión de Declaraciones**
   - Hacer clic en "Gestionar Declaraciones" en un card
   - Se abre la pantalla de gestión filtrada por ese proyecto

5. **Crear Nueva Declaración**
   - Hacer clic en "Crear Declaración" (si hay assessments disponibles)
   - Se abre diálogo para seleccionar assessment y crear

---

## 📝 PANTALLA DE GESTIÓN DE DECLARACIONES

### Ubicación
`/governance/compliance/conformity-declaration-manager?projectId={id}`

### Descripción
Pantalla para gestionar todas las declaraciones de conformidad de un proyecto específico.

### Componentes

#### 1. **Header con Información del Proyecto**
- **Nombre del Proyecto** (título grande)
- **ID del Proyecto**
- **Descripción** del proyecto (si está disponible)
- **Botón "Volver"** → Regresa a la pantalla de proyectos

#### 2. **Estadísticas en Tiempo Real (4 Contadores)**
- **Total Declaraciones:** Número total de declaraciones del proyecto
- **Firmadas:** Declaraciones en estado SIGNED
- **Borradores:** Declaraciones en estado DRAFT
- **Assessments Disponibles:** Assessments listos para certificación

#### 3. **Sección: Crear Nueva Declaración**
- **Selector de Assessment:**
  - Dropdown con lista de assessments disponibles
  - Solo muestra assessments con `readyForCertification = true`
  - Muestra nombre del assessment y fecha
- **Campos de Entrada:**
  - **Nombre del Proveedor** (requerido)
  - **Nombre del Sistema IA** (requerido, prellenado desde assessment)
- **Vista Previa:**
  - Muestra información que se incluirá en la declaración
  - Sistema IA, Proveedor, Assessment seleccionado
- **Botón "Generar Declaración":**
  - Crea la declaración en estado DRAFT
  - Muestra loading durante la creación
  - Muestra mensaje de éxito/error

#### 4. **Tabla de Declaraciones Existentes**
Tabla con las siguientes columnas:

| Columna | Descripción |
|---------|-------------|
| **Versión** | Versión de la declaración (ej: v1.0, v1.1) |
| **Sistema IA** | Nombre del sistema de IA |
| **Proveedor** | Nombre del proveedor |
| **Fecha** | Fecha de creación (YYYY-MM-DD) |
| **Estado** | Badge: DRAFT (amarillo) o SIGNED (verde) |
| **Acciones** | Botones: Firmar, Descargar PDF |

**Características:**
- Ordenamiento por fecha (más reciente primero)
- Badges de color para estados
- Botones de acción por fila
- Paginación si hay muchas declaraciones

#### 5. **Acciones por Declaración**

**Botón "Firmar" (solo DRAFT):**
- Visible solo para declaraciones en estado DRAFT
- Al hacer clic:
  - Muestra diálogo de confirmación
  - Firma la declaración
  - Cambia estado a SIGNED
  - Actualiza la tabla
  - Muestra mensaje de éxito

**Botón "Descargar PDF":**
- Visible para todas las declaraciones
- Al hacer clic:
  - Descarga el PDF de la declaración
  - Nombre de archivo: `declaration_{id}.pdf`
  - Descarga automática

### Flujo de Uso

#### Crear Nueva Declaración

1. **Seleccionar Assessment**
   - En el dropdown, seleccionar un assessment disponible
   - El sistema muestra información del assessment

2. **Ingresar Información**
   - Ingresar nombre del proveedor
   - Verificar nombre del sistema IA (prellenado)

3. **Revisar Vista Previa**
   - Verificar que la información sea correcta
   - Revisar datos del assessment

4. **Generar Declaración**
   - Hacer clic en "Generar Declaración"
   - Esperar confirmación
   - La declaración aparece en la tabla en estado DRAFT

#### Firmar Declaración

1. **Encontrar Declaración DRAFT**
   - En la tabla, buscar declaración con badge "DRAFT"

2. **Hacer Clic en "Firmar"**
   - Aparece diálogo de confirmación
   - Confirmar acción

3. **Verificar Cambio de Estado**
   - La declaración cambia a SIGNED
   - El badge cambia a verde
   - El botón "Firmar" desaparece

#### Descargar PDF

1. **Hacer Clic en "Descargar PDF"**
   - En cualquier declaración de la tabla

2. **Esperar Descarga**
   - El archivo se descarga automáticamente
   - Nombre: `declaration_{id}.pdf`

---

## 🎓 TUTORIALES PASO A PASO

### Tutorial 1: Crear Primera Declaración de un Proyecto

**Objetivo:** Crear y firmar la primera declaración de conformidad de un proyecto.

**Pasos:**

1. **Completar Assessment de Compliance**
   - Ir a Compliance Assessment
   - Completar todos los pasos
   - Asegurar que `readyForCertification = true`

2. **Ir a Conformity Declaration**
   - Navegar a `/governance/compliance/conformity-declaration/projects`
   - Encontrar el proyecto en la lista
   - Hacer clic en "Gestionar Declaraciones"

3. **Crear Declaración**
   - En el selector de assessment, seleccionar el assessment completado
   - Ingresar nombre del proveedor (ej: "TechCorp Medical")
   - Verificar nombre del sistema IA
   - Revisar vista previa
   - Hacer clic en "Generar Declaración"
   - Esperar confirmación: "Declaración generada exitosamente"

4. **Verificar Declaración Creada**
   - La declaración aparece en la tabla
   - Estado: DRAFT (badge amarillo)
   - Versión: v1.0

5. **Firmar Declaración**
   - Hacer clic en "Firmar" en la fila de la declaración
   - Confirmar en el diálogo
   - Esperar confirmación: "Declaración firmada exitosamente"

6. **Verificar Estado Firmado**
   - El badge cambia a SIGNED (verde)
   - El botón "Firmar" desaparece
   - La fecha de firma se registra

7. **Descargar PDF** (Opcional)
   - Hacer clic en "Descargar PDF"
   - Guardar el archivo

**Resultado:** Declaración de conformidad creada, firmada y lista para uso.

---

### Tutorial 2: Crear Nueva Versión de Declaración

**Objetivo:** Crear una nueva versión de declaración cuando el sistema se actualiza.

**Pasos:**

1. **Actualizar Assessment** (si es necesario)
   - Si el sistema cambió, actualizar el assessment
   - Completar nuevos pasos si aplica
   - Recalcular overall score

2. **Ir a Gestión de Declaraciones**
   - Desde la pantalla de proyectos, hacer clic en "Gestionar Declaraciones"
   - O ir directamente: `/governance/compliance/conformity-declaration-manager?projectId={id}`

3. **Ver Declaraciones Existentes**
   - En la tabla, ver las declaraciones anteriores
   - Notar las versiones: v1.0, v1.1, etc.

4. **Crear Nueva Declaración**
   - Seleccionar el assessment actualizado
   - Ingresar información actualizada
   - Generar declaración
   - La nueva versión será automática (ej: v1.2)

5. **Firmar Nueva Versión**
   - Firmar la nueva declaración
   - Las versiones anteriores quedan en el historial

**Resultado:** Nueva versión de declaración creada y firmada, manteniendo historial completo.

---

### Tutorial 3: Filtrar y Buscar Proyectos

**Objetivo:** Encontrar proyectos específicos o con características determinadas.

**Pasos:**

1. **Filtrar por "Con Declaraciones"**
   - En la pantalla de proyectos, seleccionar filtro "Con Declaraciones"
   - Solo se muestran proyectos que tienen al menos una declaración

2. **Filtrar por "Sin Declaraciones"**
   - Seleccionar filtro "Sin Declaraciones"
   - Solo se muestran proyectos que necesitan declaraciones

3. **Buscar por Nombre**
   - Escribir en el campo de búsqueda (ej: "healthcare")
   - Los resultados se filtran automáticamente
   - Ver solo proyectos que contienen el texto

4. **Combinar Filtros**
   - Usar filtro + búsqueda simultáneamente
   - Ejemplo: "Con Declaraciones" + búsqueda "medical"

**Resultado:** Lista filtrada de proyectos según criterios seleccionados.

---

## 🎨 ELEMENTOS DE INTERFAZ

### Badges de Estado

- **DRAFT:** Badge amarillo/naranja con texto "DRAFT"
- **SIGNED:** Badge verde con texto "SIGNED"

### Botones

- **Gestionar Declaraciones:** Botón primario azul
- **Crear Declaración:** Botón secundario
- **Firmar:** Botón pequeño en tabla (solo DRAFT)
- **Descargar PDF:** Botón pequeño en tabla
- **Volver:** Botón con icono de flecha izquierda

### Contadores

- **Estadísticas:** Cards con números grandes y etiquetas
- **Colores:** Azul para totales, verde para positivos, amarillo para pendientes

### Tabla

- **Filas alternadas:** Colores alternados para mejor legibilidad
- **Hover:** Resaltado al pasar el mouse
- **Responsive:** Se adapta a diferentes tamaños de pantalla

---

## 🚨 MENSAJES Y ALERTAS

### Mensajes de Éxito

- **"Declaración generada exitosamente"** - Al crear declaración
- **"Declaración firmada exitosamente"** - Al firmar declaración
- **"PDF descargado exitosamente"** - Al descargar PDF

### Mensajes de Error

- **"El assessment seleccionado no está listo para certificación"** - Assessment incompleto
- **"Solo las declaraciones DRAFT pueden ser firmadas"** - Intentar firmar SIGNED
- **"Error al generar declaración"** - Error general
- **"Error al firmar declaración"** - Error al firmar
- **"Error al descargar PDF"** - Error al descargar

### Validaciones

- **Campos requeridos:** Muestra error si faltan campos
- **Assessment no disponible:** Muestra mensaje si no hay assessments listos

---

## 🔗 NAVEGACIÓN

### Flujo de Navegación

```
Menú Principal
    ↓
Conformity Declaration (Sidebar)
    ↓
Pantalla de Proyectos
    ↓
[Gestionar Declaraciones] → Pantalla de Gestión
    ↓
[Volver] → Pantalla de Proyectos
```

### Enlaces Rápidos

- **Desde Proyectos:** Hacer clic en "Gestionar Declaraciones" → Gestión
- **Desde Gestión:** Hacer clic en "Volver" → Proyectos
- **URL Directa:** Usar `?projectId={id}` para filtrar por proyecto

---

## 📝 NOTAS IMPORTANTES

1. **Filtrado Automático:** La pantalla de gestión filtra automáticamente por `projectId` desde la URL. Si no hay `projectId`, muestra error.

2. **Estados Inmutables:** Una vez que una declaración está SIGNED, no puede volver a DRAFT ni ser editada.

3. **Versiones Automáticas:** Las versiones se generan automáticamente. No se puede editar manualmente.

4. **Assessments Disponibles:** Solo se muestran assessments con `readyForCertification = true`.

5. **Multi-idioma:** Todas las pantallas soportan 6 idiomas (es, en, fr, de, it, pt).

---

## 🆘 SOLUCIÓN DE PROBLEMAS

### Problema: No aparecen assessments disponibles

**Solución:**
- Verificar que el assessment esté completo
- Asegurar que `readyForCertification = true`
- Completar todos los pasos del assessment

### Problema: No puedo firmar una declaración

**Solución:**
- Verificar que la declaración esté en estado DRAFT
- Si está SIGNED, no se puede firmar nuevamente
- Crear nueva versión si es necesario

### Problema: El PDF no se descarga

**Solución:**
- Verificar conexión a internet
- Verificar permisos del navegador para descargas
- Intentar nuevamente
- Contactar soporte si persiste

---

## 🔗 REFERENCIAS

- **Guía Funcional:** `docs/prompts/compliance/conformidad/user_guide/GUIA_FUNCIONAL_CONFORMITY_DECLARATION.md`
- **Developer Guide Frontend:** `docs/prompts/compliance/conformidad/DEVELOPER_GUIDE_FRONTEND.md`
- **Developer Guide Backend:** `docs/prompts/compliance/conformidad/DEVELOPER_GUIDE_BACKEND.md`
- **Estado de Implementación:** `docs/prompts/compliance/conformidad/ESTADO_IMPLEMENTACION_CONFORMITY_DECLARATION.md`
