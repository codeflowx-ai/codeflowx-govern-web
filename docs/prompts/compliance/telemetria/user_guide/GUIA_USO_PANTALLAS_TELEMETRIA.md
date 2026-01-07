# 📱 GUÍA DE USO - PANTALLAS DE TELEMETRÍA

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo de Telemetría](#acceso-al-módulo-de-telemetría)
2. [Dashboard de Telemetría](#dashboard-de-telemetría)
3. [Búsqueda de Eventos](#búsqueda-de-eventos)
4. [Análisis Mensual](#análisis-mensual)
5. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO DE TELEMETRÍA

### Desde el Menú Principal

1. Navegar a **Governance** → **Telemetry**
2. Se abrirá el Dashboard de Telemetría principal

### Navegación entre Pantallas

Desde el Dashboard principal, se puede acceder a:
- **Búsqueda:** Botón "Búsqueda" en la parte superior derecha
- **Análisis:** Botón "Análisis" en la parte superior derecha
- **Volver:** Botón "Volver" disponible en las pantallas de Búsqueda y Análisis

---

## 📊 DASHBOARD DE TELEMETRÍA

### Ubicación
`/governance/telemetry/dashboard`

### Descripción
Vista centralizada con KPIs principales, gráficos y estadísticas por componente.

### Componentes

#### 1. **Header**
- Título: "Dashboard de Telemetría"
- Subtítulo: Descripción del módulo
- **Botones de Acción:**
  - **Búsqueda:** Navega a la página de búsqueda de eventos
  - **Análisis:** Navega a la página de análisis mensual
  - **Actualizar:** Recarga los datos del dashboard

#### 2. **Filtros de Fecha**
- **Fecha Inicio:** Selector de fecha para inicio del período
- **Fecha Fin:** Selector de fecha para fin del período
- **Por Defecto:** Últimos 30 días
- **Actualización Automática:** Los datos se actualizan automáticamente al cambiar las fechas

#### 3. **KPIs Principales (5 Tarjetas)**

**Total de Eventos**
- Icono: Database
- Muestra: Número total de eventos en el período
- Color: Azul

**Costo Total (USD)**
- Icono: DollarSign
- Muestra: Suma de costos en dólares
- Color: Verde
- Formato: Moneda con 2 decimales

**Tokens Totales**
- Icono: Activity
- Muestra: Suma de tokens consumidos
- Color: Púrpura
- Formato: Número con separadores de miles

**Latencia Promedio (ms)**
- Icono: Clock
- Muestra: Tiempo promedio de respuesta
- Color: Naranja
- Formato: Número con 2 decimales + "ms"

**Componentes**
- Icono: Cpu
- Muestra: Número de componentes únicos
- Color: Gris

#### 4. **Gráficos**

**Distribución de Eventos por Componente**
- Tipo: Gráfico de barras
- Eje X: Componentes (UUID truncado)
- Eje Y: Número de eventos
- Permite identificar componentes más activos

**Costos por Componente**
- Tipo: Gráfico de barras
- Eje X: Componentes (UUID truncado)
- Eje Y: Costo en USD
- Permite identificar componentes más costosos

#### 5. **Estadísticas por Componente**

Tabla con columnas:
- **Componente UUID:** Identificador único del componente (truncado)
- **Total Eventos:** Número de eventos generados
- **Latencia Promedio:** Tiempo promedio de respuesta (ms)
- **Tokens Totales:** Suma de tokens consumidos
- **Costo Total:** Suma de costos en USD

**Características:**
- Ordenable por cualquier columna
- Scroll horizontal si hay muchos componentes
- Formato numérico con separadores

### Funcionalidades

#### Cambiar Rango de Fechas

1. Seleccionar nueva **Fecha Inicio** en el selector
2. Seleccionar nueva **Fecha Fin** en el selector
3. Los datos se actualizan automáticamente
4. Los KPIs, gráficos y tabla se recalculan

#### Actualizar Datos Manualmente

1. Hacer clic en el botón **"Actualizar"** (icono RefreshCw)
2. Se recargan todos los datos del dashboard
3. Se mantienen los filtros de fecha actuales

#### Navegar a Otras Pantallas

1. **Búsqueda:** Hacer clic en botón "Búsqueda" → Navega a `/governance/telemetry/search`
2. **Análisis:** Hacer clic en botón "Análisis" → Navega a `/governance/telemetry/analytics`

---

## 🔍 BÚSQUEDA DE EVENTOS

### Ubicación
`/governance/telemetry/search`

### Descripción
Pantalla para buscar y filtrar eventos de telemetría con múltiples criterios.

### Componentes Principales

#### 1. **Header**
- Título: "Búsqueda de Eventos de Telemetría"
- Subtítulo: Descripción del módulo
- **Botón Volver:** Navega de vuelta al dashboard principal

#### 2. **Filtros de Búsqueda**

**Búsqueda por Contenido**
- Campo de texto libre
- Busca en payload JSONB y métricas
- Placeholder: "Buscar en payload, métricas..."

**UUID del Componente**
- Campo de texto
- Filtrar por componente específico
- Placeholder: "550e8400-e29b-41d4-a716-446655440001"

**ID del Agente**
- Campo de texto
- Filtrar por `agentExternalId`
- Placeholder: "agent-123"

**Tipo de Evento**
- Campo de texto
- Filtrar por `eventType`
- Placeholder: "INTERACTION_COMPLETED"

**Severidad**
- Selector desplegable
- Opciones: Todas, INFO, WARN, ERROR, DEBUG

**Fecha Inicio**
- Selector de fecha
- Filtrar eventos desde esta fecha

**Fecha Fin**
- Selector de fecha
- Filtrar eventos hasta esta fecha

**Verificaciones de Gobernanza**
- Checkboxes:
  - **Bias:** Eventos con sesgo detectado
  - **Toxicidad:** Eventos con toxicidad detectada
  - **PII:** Eventos con datos personales detectados
  - **Secretos:** Eventos con secretos detectados

**Filtros de Compliance/Security**
- **Estado de Cumplimiento:** Selector con opciones: Todas, PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
- **Nivel de Riesgo:** Selector con opciones: Todas, LOW, MEDIUM, HIGH, CRITICAL
- **Categoría de Compliance:** Selector con opciones: Todas, GDPR, SECURITY, LEGAL
- **Etiqueta de Problema:** Campo de texto para filtrar por tags específicos (ej: pii_exposure, secret_leak, bias_critical)

#### 3. **Acciones de Filtros**
- **Botón Limpiar:** Resetea todos los filtros
- **Botón Buscar:** Ejecuta la búsqueda con los criterios actuales

#### 4. **Resultados de Búsqueda**

**Header de Resultados**
- Muestra total de resultados encontrados
- Controles de paginación:
  - **Anterior:** Ir a página anterior
  - **Página X / Y:** Indicador de página actual
  - **Siguiente:** Ir a página siguiente

**Cards de Eventos**

Cada evento se muestra en un card con:

**Sección 1: Información Temporal y Tipo**
- **Fecha y Hora:** Timestamp del evento (formato local)
- **Severidad:** Badge con color según severidad
  - INFO: Azul
  - WARN: Amarillo
  - ERROR: Rojo
  - DEBUG: Gris
- **Tipo de Evento:** Badge outline
- **Herramienta Origen:** Badge outline (si existe)

**Sección 2: Identificadores**
- **Componente:** UUID truncado (primeros 36 caracteres)
- **Agente:** `agentExternalId` (si existe)
- **Trace ID:** `traceId` (si existe)
- **Run ID:** `runId` (si existe)

**Sección 3: Verificaciones de Gobernanza**
- Badges para cada verificación activa:
  - **Bias:** Badge púrpura con icono CheckCircle
  - **Toxicidad:** Badge naranja con icono CheckCircle
  - **PII:** Badge rojo con icono AlertCircle
  - **Secretos:** Badge rojo con icono AlertCircle

**Sección 4: Compliance/Security**
- Badges para campos calculados automáticamente:
  - **Estado de Cumplimiento:** Badge con color según estado (PASS: verde, WARNING: amarillo, VIOLATION: naranja, CRITICAL_VIOLATION: rojo)
  - **Nivel de Riesgo:** Badge con color según nivel (LOW: verde, MEDIUM: amarillo, HIGH: naranja, CRITICAL: rojo)
  - **Categoría de Compliance:** Badge outline (GDPR, SECURITY, LEGAL)
  - **Etiquetas de Problemas:** Badges pequeños para cada tag (bias_critical, pii_exposure, secret_leak, etc.)

**Sección 5: Métricas**
- **Latencia:** En milisegundos (si existe)
- **Tokens:** Número de tokens (si existe)
- **Costo:** En USD (si existe)

**Sección 6: Acciones**
- **Ver Detalle Completo:** Botón que navega a la página de detalle del evento (`/governance/telemetry/events/{id}`)
- Permite ver payload completo, métricas, análisis y datos raw en una página dedicada

### Funcionalidades

#### Realizar una Búsqueda

1. **Búsqueda Automática al Cargar:**
   - Al entrar a la página, se ejecuta automáticamente una búsqueda genérica
   - Muestra eventos recientes sin necesidad de hacer clic

2. **Búsqueda Manual:**
   - Completar filtros deseados
   - Hacer clic en botón **"Buscar"**
   - Los resultados se muestran debajo

#### Limpiar Filtros

1. Hacer clic en botón **"Limpiar"** (icono X)
2. Todos los filtros se resetean
3. Los resultados se limpian

#### Navegar entre Páginas

1. Usar botones **"Anterior"** y **"Siguiente"**
2. Ver indicador de página actual
3. Los botones se deshabilitan en primera/última página

#### Ver Detalles de un Evento

1. Hacer clic en botón **"Ver Detalle Completo"** en cualquier evento
2. Navegar a la página de detalle (`/governance/telemetry/events/{id}`)
3. Ver información completa del evento:
   - Información principal (fecha, tipo, severidad, identificadores)
   - Verificaciones de gobernanza (bias, toxicidad, PII, secretos)
   - Campos de compliance/security (estado, nivel de riesgo, categoría, etiquetas)
   - Métricas (latencia, tokens, costo)
   - Payload completo (con scroll si es extenso)
   - Resultados de análisis
   - Datos raw del evento
4. Usar botones "Copiar" y "Descargar" para cada sección si es necesario

#### Volver al Dashboard

1. Hacer clic en botón **"Volver"** (icono ArrowLeft)
2. Navega a `/governance/telemetry/dashboard`

---

## 📄 DETALLE DE EVENTO

### Ubicación
`/governance/telemetry/events/{id}`

### Descripción
Página dedicada para visualizar todos los detalles de un evento de telemetría, incluyendo payload completo, métricas, análisis y datos raw.

### Componentes Principales

#### 1. **Header**
- Título: "Detalle de Evento de Telemetría"
- **Botón Volver:** Navega de vuelta a la página de búsqueda

#### 2. **Secciones de Información**

**Información Principal**
- UUID del evento
- Fecha y hora (timestamp)
- Tipo de evento y severidad
- Componente UUID
- Agente External ID (si existe)
- Trace ID y Run ID (si existen)
- Herramienta origen (sourceTool)

**Verificaciones de Gobernanza**
- Badges para cada verificación:
  - Bias Checked (si se verificó)
  - Toxicity Checked (si se verificó)
  - PII Detected (si se detectó)
  - Secret Detected (si se detectó)

**Compliance/Security**
- **Estado de Cumplimiento:** Badge con color (PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION)
- **Nivel de Riesgo:** Badge con color (LOW, MEDIUM, HIGH, CRITICAL)
- **Categoría de Compliance:** Badge (GDPR, SECURITY, LEGAL)
- **Etiquetas de Problemas:** Lista de tags (bias_critical, pii_exposure, secret_leak, etc.)

**Métricas**
- Latencia (ms)
- Tokens utilizados
- Costo (USD)
- JSON formateado con botones "Copiar" y "Descargar"

**Payload**
- JSON completo del payload
- Scroll automático si es extenso
- Botones "Copiar" y "Descargar"

**Resultados de Análisis**
- JSON con resultados de todos los análisis ejecutados (bias, toxicidad, PII, secretos, compliance)
- Botones "Copiar" y "Descargar"

**Datos Raw del Evento**
- JSON completo del evento tal como está almacenado
- Botones "Copiar" y "Descargar"

### Funcionalidades

#### Copiar Contenido
1. Hacer clic en botón **"Copiar"** en cualquier sección
2. El contenido se copia al portapapeles
3. Aparece notificación de confirmación

#### Descargar Contenido
1. Hacer clic en botón **"Descargar"** en cualquier sección
2. Se descarga un archivo JSON con el contenido de la sección
3. Nombre del archivo incluye el tipo de contenido y el UUID del evento

#### Volver a Búsqueda
1. Hacer clic en botón **"Volver"** (icono ArrowLeft)
2. Navega de vuelta a `/governance/telemetry/search`

---

## 📈 ANÁLISIS MENSUAL

### Ubicación
`/governance/telemetry/analytics`

### Descripción
Dashboard de análisis temporal con métricas mensuales y gráficos de tendencias.

### Componentes Principales

#### 1. **Header**
- Título: "Análisis de Telemetría"
- Subtítulo: "Métricas mensuales y análisis por año"
- **Controles:**
  - **Selector de Año:** Dropdown con opciones 2024 y 2025 (se actualiza automáticamente al cambiar)
  - **Botón Actualizar:** Recarga datos del año seleccionado
  - **Botón Volver:** Navega al dashboard principal

#### 2. **KPIs del Año**

**Total de Eventos del Año**
- Icono: Activity
- Muestra: Suma de eventos de todos los meses del año
- Formato: Número con separadores

**Costo Total del Año (USD)**
- Icono: DollarSign
- Muestra: Suma de costos de todos los meses
- Formato: Moneda con 2 decimales

**Tokens Totales del Año**
- Icono: Cpu
- Muestra: Suma de tokens de todos los meses
- Formato: Número con separadores

**Latencia Promedio del Año (ms)**
- Icono: Clock
- Muestra: Promedio de latencia de todos los meses
- Formato: Número con 2 decimales + "ms"

#### 3. **Gráficos de Tendencias**

**Total de Eventos por Mes**
- Tipo: Gráfico de línea
- Eje X: Meses del año (formato corto, ej: "ene 2025")
- Eje Y: Número de eventos
- Permite ver tendencias de actividad

**Costos Mensuales (USD)**
- Tipo: Gráfico de línea
- Eje X: Meses del año
- Eje Y: Costo en USD
- Permite analizar evolución de costos

**Tokens Mensuales**
- Tipo: Gráfico de línea
- Eje X: Meses del año
- Eje Y: Número de tokens
- Permite ver consumo de tokens

**Latencia Promedio Mensual (ms)**
- Tipo: Gráfico de línea
- Eje X: Meses del año
- Eje Y: Latencia en milisegundos
- Permite detectar degradación de rendimiento

**Características de los Gráficos:**
- Responsive (se adaptan al tamaño de pantalla)
- Tooltips al pasar el mouse
- Leyenda interactiva
- Colores adaptados a modo oscuro/claro

### Funcionalidades

#### Cambiar Año de Análisis

1. Seleccionar nuevo año en el dropdown (2024 o 2025)
2. Los datos se cargan automáticamente al cambiar el año
3. Se cargan métricas para cada mes del año seleccionado
4. Los gráficos se actualizan automáticamente
5. Los datos varían entre años (2025 tiene ~25% más actividad y mejor latencia que 2024)
6. Los datos varían por mes (mayor actividad en verano, menor en invierno)

#### Actualizar Datos

1. Hacer clic en botón **"Actualizar"** (icono RefreshCw)
2. Se recargan todos los datos del año actual
3. Los gráficos se refrescan

#### Volver al Dashboard

1. Hacer clic en botón **"Volver"** (icono ArrowLeft)
2. Navega a `/governance/telemetry/dashboard`

---

## 📝 TUTORIALES PASO A PASO

### Tutorial 1: Ver KPIs de los Últimos 7 Días

1. Navegar a **Governance** → **Telemetry** → **Dashboard**
2. En **Fecha Inicio**, seleccionar fecha de hace 7 días
3. En **Fecha Fin**, seleccionar fecha de hoy
4. Los KPIs se actualizan automáticamente
5. Revisar las 5 tarjetas de KPIs

### Tutorial 2: Buscar Eventos de Error del Último Mes

1. Navegar a **Governance** → **Telemetry** → **Búsqueda**
2. En **Severidad**, seleccionar "ERROR"
3. En **Fecha Inicio**, seleccionar fecha de hace 30 días
4. En **Fecha Fin**, seleccionar fecha de hoy
5. Hacer clic en **"Buscar"**
6. Revisar eventos encontrados

### Tutorial 3: Analizar Costos del Año Actual

1. Navegar a **Governance** → **Telemetry** → **Análisis**
2. Verificar que el año seleccionado sea el actual
3. Si no, cambiar el año y hacer clic en **"Actualizar"**
4. Revisar KPI "Costo Total del Año"
5. Revisar gráfico "Costos Mensuales (USD)"
6. Identificar meses con mayor costo

### Tutorial 4: Encontrar Eventos con Datos Personales

1. Navegar a **Governance** → **Telemetry** → **Búsqueda**
2. Opción A: Activar checkbox **"PII"** en Verificaciones de Gobernanza
3. Opción B: Seleccionar **"Categoría de Compliance"** = **"GDPR"**
4. Opción C: Usar **"Etiqueta de Problema"** = **"pii_exposure"**
5. Establecer **Fecha Inicio** y **Fecha Fin** según necesidad
6. Hacer clic en **"Buscar"**
7. Revisar eventos encontrados
8. Hacer clic en **"Ver Detalle Completo"** en eventos relevantes para ver información completa

### Tutorial 5: Comparar Rendimiento de Componentes

1. Navegar a **Governance** → **Telemetry** → **Dashboard**
2. Ajustar filtros de fecha al período deseado
3. Revisar tabla **"Estadísticas por Componente"**
4. Hacer clic en columna **"Latencia Promedio"** para ordenar
5. Identificar componentes con mayor/menor latencia
6. Comparar costos y tokens entre componentes

### Tutorial 6: Analizar Tendencias de Uso

1. Navegar a **Governance** → **Telemetry** → **Análisis**
2. Seleccionar año a analizar
3. Hacer clic en **"Actualizar"**
4. Revisar gráfico **"Total de Eventos por Mes"**
5. Identificar tendencias (crecimiento, decrecimiento, estacionalidad)
6. Comparar con gráfico de **"Tokens Mensuales"** para ver correlación

### Tutorial 7: Buscar Evento Específico por Contenido

1. Navegar a **Governance** → **Telemetry** → **Búsqueda**
2. En **Búsqueda por Contenido**, escribir texto conocido del evento
3. Establecer **Fecha Inicio** y **Fecha Fin** para limitar búsqueda
4. Hacer clic en **"Buscar"**
5. Revisar resultados
6. Si hay muchos resultados, usar filtros adicionales (componente, severidad, complianceStatus, riskLevel, etc.)
7. Hacer clic en **"Ver Detalle Completo"** para ver información completa del evento

### Tutorial 9: Filtrar Eventos por Compliance/Security

1. Navegar a **Governance** → **Telemetry** → **Búsqueda**
2. Establecer filtros de compliance/security:
   - **Estado de Cumplimiento:** CRITICAL_VIOLATION
   - **Nivel de Riesgo:** CRITICAL
   - **Categoría de Compliance:** SECURITY
3. Establecer **Fecha Inicio** y **Fecha Fin**
4. Hacer clic en **"Buscar"**
5. Revisar eventos críticos encontrados
6. Hacer clic en **"Ver Detalle Completo"** para análisis detallado

### Tutorial 8: Identificar Componente con Mayor Costo

1. Navegar a **Governance** → **Telemetry** → **Dashboard**
2. Ajustar filtros de fecha al período deseado
3. Revisar gráfico **"Costos por Componente"**
4. Identificar barra más alta (mayor costo)
5. Revisar tabla **"Estadísticas por Componente"**
6. Hacer clic en columna **"Costo Total"** para ordenar
7. Ver detalles del componente con mayor costo

---

## ⚠️ NOTAS IMPORTANTES

### Rendimiento

- Las búsquedas con muchos resultados pueden tardar varios segundos
- Se recomienda usar filtros de fecha para limitar resultados
- La paginación ayuda a manejar grandes volúmenes de datos

### Datos Mock

- En modo demo, se usan datos mock
- Los datos mock son representativos pero no reales
- En producción, todos los datos provienen de la base de datos real

### Filtros de Fecha

- Las fechas se interpretan en hora local del navegador
- La fecha fin incluye todo el día (hasta 23:59:59)
- Se recomienda usar rangos razonables para mejor rendimiento

### Paginación

- Por defecto se muestran 20 eventos por página
- Usar controles de paginación para navegar
- Los botones se deshabilitan automáticamente cuando no hay más páginas

---

## 🔗 REFERENCIAS

### Rutas de Pantallas

- **Dashboard:** `/governance/telemetry/dashboard`
- **Búsqueda:** `/governance/telemetry/search`
- **Análisis:** `/governance/telemetry/analytics`
- **Detalle de Evento:** `/governance/telemetry/events/{id}`

### Documentación Relacionada

- Ver **Guía Funcional** para entender conceptos y casos de uso
- Ver **Guías de Desarrolladores** para detalles técnicos

---

**Última Actualización:** Diciembre 2025

**Cambios Recientes:**
- Agregada página de detalle de eventos (`/governance/telemetry/events/{id}`)
- Nuevos filtros de compliance/security en búsqueda
- Campos de compliance/security mostrados en cards de eventos
- Selector de año mejorado en análisis mensual (dropdown con actualización automática)
- Datos mock más realistas con variaciones por mes y año
