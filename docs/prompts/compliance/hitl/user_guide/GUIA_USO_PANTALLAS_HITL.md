# 📱 GUÍA DE USO - PANTALLAS HITL SUPERVISION

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales, Supervisores HITL

---

## 📋 ÍNDICE

1. [Acceso al Módulo HITL](#-acceso-al-módulo-hitl)
2. [Dashboard de Supervisión](#-dashboard-de-supervisión)
3. [Gestión de Intervenciones Pendientes](#-gestión-de-intervenciones-pendientes)
4. [Registro de Decisiones](#-registro-de-decisiones)
5. [Historial de Decisiones](#-historial-de-decisiones)
6. [Configuración de Supervisión](#-configuración-de-supervisión)

---

## 🚪 ACCESO AL MÓDULO HITL

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **HITL Supervision**
3. La ruta es: `/governance/compliance/hitl-supervision`

### Requisitos de Acceso

- Rol: `GESTOR_GOBIERNO`, `CIENTIFICO_DATOS`, `COMPLIANCE_OFFICER`, o `ADMIN`
- Permiso: `hitl:view`

---

## 📊 DASHBOARD DE SUPERVISIÓN

### Ubicación
Pantalla principal del módulo HITL Supervision

### Descripción
Dashboard que muestra métricas clave y acceso a intervenciones pendientes y decisiones recientes.

### Componentes

#### 1. **Header**
- **Título:** "HITL Supervision" con icono
- **Subtítulo:** Descripción del módulo
- **Botón Configurar:** Abre dialog de configuración

#### 2. **Métricas Principales (3 Cards)**
Grid de 3 columnas con métricas clave:

- **Card 1: Tiempo Promedio de Respuesta**
  - Icono: Reloj (cyan)
  - Valor: Tiempo en horas (ej: "2.5h")
  - Indicador: Trending up (verde)

- **Card 2: Tasa de Aprobación**
  - Icono: CheckCircle (verde)
  - Valor: Porcentaje (ej: "85%")
  - Indicador: Trending up (verde)

- **Card 3: Tasa de Cumplimiento SLA**
  - Icono: AlertCircle (verde)
  - Valor: Porcentaje (ej: "92%")
  - Indicador: Trending up (verde)
  - Color del valor: Verde

#### 3. **Tabs de Navegación**
- **Tab 1: Intervenciones Pendientes**
  - Muestra contador de intervenciones pendientes
- **Tab 2: Decisiones Recientes**
  - Muestra contador de decisiones recientes

---

## 🚨 GESTIÓN DE INTERVENCIONES PENDIENTES

### Ubicación
Tab "Intervenciones Pendientes" en el dashboard

### Descripción
Listado de todas las intervenciones que requieren supervisión humana, organizadas en un grid de 3 columnas.

### Componentes Principales

#### 1. **Header del Tab**
- **Título:** "Intervenciones Pendientes"
- **Filtros:**
  - Filtro por Estado: ALL, PENDING, IN_REVIEW
  - Filtro por Tipo: ALL, Agent, Model, Prompt

#### 2. **Grid de Intervenciones (3 Columnas)**
Cada intervención se muestra en una card individual con:

**Layout:**
- Contenido principal a la izquierda
- Botones de acción a la derecha (apilados verticalmente)

**Información Mostrada:**
- **Badges:**
  - Tipo de entidad (outline)
  - Estado (PENDING, IN_REVIEW)
  - Urgencia (CRITICAL, HIGH, MEDIUM, LOW)
  - SLA Urgente (si tiempo < 1h)
  - SLA Próximo (si tiempo < 2h)

- **Nombre de Entidad:** Nombre de la entidad a supervisar

- **Detalles:**
  - Tipo de intervención
  - Fecha de creación
  - SLA deadline
  - Tiempo restante (con colores según urgencia)

**Colores según Urgencia:**
- **SLA Urgente (< 1h):** Fondo rojo claro, borde rojo
- **SLA Próximo (< 2h):** Fondo naranja claro, borde naranja
- **Normal:** Fondo normal

#### 3. **Botones de Acción (Lateral Derecho)**
Apilados verticalmente en cada card:

- **Revisar:**
  - Variante: outline
  - Acción: Abre dialog sin pre-seleccionar decisión
  - Icono: Eye

- **Aprobar:**
  - Variante: default (verde)
  - Acción: Abre dialog con APPROVED pre-seleccionado
  - Icono: CheckCircle

- **Rechazar:**
  - Variante: destructive (rojo)
  - Acción: Abre dialog con REJECTED pre-seleccionado
  - Icono: X

### Funcionalidades

#### Filtrar Intervenciones

1. **Por Estado:**
   - Seleccionar estado en dropdown "Estado"
   - Opciones: ALL, PENDING, IN_REVIEW
   - El grid se actualiza automáticamente

2. **Por Tipo de Entidad:**
   - Seleccionar tipo en dropdown "Tipo"
   - Opciones: ALL, Agent, Model, Prompt
   - El grid se actualiza automáticamente

#### Revisar Intervención

1. Hacer clic en botón **"Revisar"**
2. Se abre dialog de decisión
3. Seleccionar tipo de decisión
4. Escribir razón usando editor enriquecido
5. Hacer clic en **"Guardar"**

#### Aprobar Intervención

1. Hacer clic en botón **"Aprobar"**
2. Se abre dialog con APPROVED pre-seleccionado
3. Escribir razón de aprobación usando editor enriquecido
4. Hacer clic en **"Guardar"**
5. La intervención se aprueba y se dispara workflow BPMN

#### Rechazar Intervención

1. Hacer clic en botón **"Rechazar"**
2. Se abre dialog con REJECTED pre-seleccionado
3. Escribir razón de rechazo usando editor enriquecido
4. Hacer clic en **"Guardar"**
5. La intervención se rechaza y se dispara workflow BPMN

---

## ✅ REGISTRO DE DECISIONES

### Ubicación
Dialog que se abre al hacer clic en "Revisar", "Aprobar" o "Rechazar"

### Descripción
Dialog para registrar una decisión sobre una intervención HITL, con editor de texto enriquecido para la razón.

### Componentes

#### 1. **Header del Dialog**
- **Título:** "Registrar Decisión"
- **Botón de Cierre:** X en la esquina superior derecha

#### 2. **Formulario de Decisión**

**Campo 1: Entidad**
- **Label:** "Entidad"
- **Tipo:** Input de solo lectura
- **Valor:** Nombre de la entidad a supervisar

**Campo 2: Decisión**
- **Label:** "Decisión"
- **Tipo:** Select dropdown
- **Opciones:**
  - APPROVED (Aprobado)
  - REJECTED (Rechazado)
  - MODIFIED (Modificado)
- **Comportamiento:**
  - Si se abre desde "Aprobar": Pre-seleccionado APPROVED
  - Si se abre desde "Rechazar": Pre-seleccionado REJECTED
  - Si se abre desde "Revisar": Sin pre-selección

**Campo 3: Razón de la Decisión**
- **Label:** "Razón de la Decisión"
- **Tipo:** Editor de texto enriquecido (Quill)
- **Altura:** 350px mínimo
- **Funcionalidades del Editor:**
  - **Formato de Texto:**
    - Negrita (B)
    - Cursiva (I)
    - Subrayado (U)
    - Tachado
  - **Listas:**
    - Lista con viñetas
    - Lista numerada
  - **Colores:**
    - Color de texto
    - Color de fondo
  - **Alineación:**
    - Izquierda, Centro, Derecha, Justificado
  - **Otros:**
    - Enlaces
    - Citas
    - Bloques de código
    - Limpiar formato

#### 3. **Footer del Dialog**
- **Botón Cancelar:**
  - Variante: outline
  - Acción: Cierra dialog sin guardar
- **Botón Guardar:**
  - Variante: default
  - Icono: Save
  - Acción: Guarda decisión y dispara workflow BPMN

### Pasos para Registrar Decisión

1. **Abrir Dialog:**
   - Hacer clic en "Revisar", "Aprobar" o "Rechazar"

2. **Seleccionar Decisión:**
   - Si no está pre-seleccionada, elegir del dropdown

3. **Escribir Razón:**
   - Usar el editor de texto enriquecido
   - Aplicar formato según necesidad
   - Escribir razón detallada y clara

4. **Guardar:**
   - Hacer clic en "Guardar"
   - La decisión se guarda en BD
   - Se dispara workflow BPMN "hitl-decision-process"
   - El dialog se cierra
   - El dashboard se actualiza

---

## 📜 HISTORIAL DE DECISIONES

### Ubicación
Tab "Decisiones Recientes" en el dashboard

### Descripción
Listado de todas las decisiones recientes tomadas por supervisores, organizadas en un grid de 3 columnas.

### Componentes Principales

#### 1. **Header del Tab**
- **Título:** "Decisiones Recientes"
- **Filtro:**
  - Filtro por Tipo: ALL, Agent, Model, Prompt

#### 2. **Grid de Decisiones (3 Columnas)**
Cada decisión se muestra en una card individual con:

**Información Mostrada:**
- **Badges:**
  - Tipo de entidad (outline)
  - Decisión (APPROVED, REJECTED, MODIFIED)

- **Nombre de Entidad:** Nombre de la entidad supervisada

- **Detalles:**
  - Razón de la decisión
  - Tiempo de respuesta (en horas)
  - Fecha de decisión
  - Usuario que tomó la decisión

### Funcionalidades

#### Filtrar Decisiones

1. **Por Tipo de Entidad:**
   - Seleccionar tipo en dropdown "Tipo"
   - Opciones: ALL, Agent, Model, Prompt
   - El grid se actualiza automáticamente

---

## ⚙️ CONFIGURACIÓN DE SUPERVISIÓN

### Ubicación
Dialog que se abre al hacer clic en botón "Configurar" en el header

### Descripción
Configuración de parámetros de supervisión por tipo de entidad.

### Componentes

#### 1. **Header del Dialog**
- **Título:** "Configuración de Supervisión"
- **Botón de Cierre:** X en la esquina superior derecha
- **Scroll:** Máximo 90vh con scroll vertical

#### 2. **Cards de Configuración por Tipo**
Cada tipo de supervisión tiene su propia card:

**Tipos Disponibles:**
- AGENT_APPROVAL: Aprobación de agentes
- MODEL_DEPLOYMENT: Despliegue de modelos
- PROMPT_REVIEW: Revisión de prompts
- Otros tipos configurables

**Configuraciones por Tipo:**

1. **Habilitado:**
   - Tipo: Switch (toggle)
   - Descripción: Activa/desactiva supervisión para este tipo
   - Acción: Se guarda automáticamente al cambiar

2. **SLA (horas):**
   - Tipo: Input numérico
   - Descripción: Tiempo máximo en horas para tomar decisión
   - Valor: Número entero (ej: 4, 8, 24)
   - Acción: Se guarda automáticamente al cambiar

3. **Escalación Automática:**
   - Tipo: Switch (toggle)
   - Descripción: Activa escalación automática si se excede SLA
   - Acción: Se guarda automáticamente al cambiar

#### 3. **Footer del Dialog**
- **Botón Cerrar:**
  - Variante: outline
  - Acción: Cierra dialog

### Pasos para Configurar

1. **Abrir Configuración:**
   - Hacer clic en botón "Configurar" en el header

2. **Configurar Tipo:**
   - Seleccionar card del tipo a configurar
   - Modificar parámetros según necesidad

3. **Guardar Cambios:**
   - Los cambios se guardan automáticamente al modificar
   - No requiere botón de guardar explícito

4. **Cerrar:**
   - Hacer clic en "Cerrar" o en X

---

## 🎨 INDICADORES VISUALES

### Colores de Estado

- **APPROVED (Aprobado):** Badge verde
- **REJECTED (Rechazado):** Badge rojo (destructive)
- **MODIFIED (Modificado):** Badge azul
- **PENDING (Pendiente):** Badge amarillo
- **IN_REVIEW (En Revisión):** Badge azul

### Colores de Urgencia

- **CRITICAL:** Badge rojo (destructive)
- **HIGH:** Badge naranja
- **MEDIUM:** Badge amarillo
- **LOW:** Badge outline (gris)

### Indicadores de SLA

- **SLA Urgente (< 1h):**
  - Fondo: Rojo claro
  - Borde: Rojo
  - Badge: "SLA Urgente" (rojo)

- **SLA Próximo (< 2h):**
  - Fondo: Naranja claro
  - Borde: Naranja
  - Badge: "SLA Próximo" (naranja)

---

## 💡 CONSEJOS DE USO

### Para Supervisores

1. **Revisar Regularmente:**
   - Revisar dashboard diariamente
   - Priorizar intervenciones con SLA urgente

2. **Documentar Bien:**
   - Usar editor enriquecido para razones detalladas
   - Incluir contexto y justificación clara

3. **Cumplir SLAs:**
   - Monitorear tiempo restante
   - Tomar decisiones dentro del tiempo acordado

4. **Usar Filtros:**
   - Filtrar por tipo para enfocarse en áreas específicas
   - Filtrar por estado para ver solo pendientes

### Para Administradores

1. **Configurar SLAs:**
   - Establecer SLAs realistas según complejidad
   - Revisar y ajustar según experiencia

2. **Monitorear Métricas:**
   - Revisar tasa de aprobación
   - Monitorear cumplimiento de SLA
   - Identificar áreas de mejora

---

## 🆘 SOLUCIÓN DE PROBLEMAS

### Problema: No veo intervenciones pendientes

**Solución:**
- Verificar filtros activos
- Comprobar que hay intervenciones creadas
- Verificar permisos de acceso

### Problema: No puedo guardar decisión

**Solución:**
- Verificar que se ha seleccionado tipo de decisión
- Comprobar que se ha escrito razón
- Verificar conexión con backend

### Problema: El editor de texto no funciona

**Solución:**
- Recargar la página
- Verificar que JavaScript está habilitado
- Limpiar caché del navegador

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.0
