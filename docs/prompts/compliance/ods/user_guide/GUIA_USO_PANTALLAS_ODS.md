# 📱 GUÍA DE USO - PANTALLAS ODS IMPACT

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo ODS Impact](#acceso-al-módulo-ods-impact)
2. [Dashboard Principal ODS Impact](#dashboard-principal-ods-impact)
3. [Páginas de Detalle por ODS](#páginas-de-detalle-por-ods)
4. [Interpretación de KPIs](#interpretación-de-kpis)
5. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO ODS IMPACT

### Desde el Menú Principal

1. Navegar a **Governance** → **ODS Impact**
2. Se abrirá el Dashboard Principal de Impacto ODS

### Navegación Directa

- **Dashboard Principal:** `/governance/ods-impact`
- **ODS Específico:** `/governance/ods-impact/ods-{número}` (ej: `/governance/ods-impact/ods-16`)

---

## 📊 DASHBOARD PRINCIPAL ODS IMPACT

### Ubicación
`/governance/ods-impact`

### Descripción
Vista centralizada con métricas generales de impacto ODS y acceso a cada ODS individual.

### Componentes

#### 1. **Header**
- **Título:** "Impacto ODS"
- **Subtítulo:** "Medición del impacto de la plataforma en los Objetivos de Desarrollo Sostenible"

#### 2. **Métricas Principales (3 Cards)**

**Score General de Impacto:**
- Valor: 0-100 (promedio de todos los ODS)
- Color: Verde (bueno), Amarillo (atención), Rojo (crítico)
- Tendencia: Indicador de mejora/empeora/estable

**Tasa de Cumplimiento:**
- Porcentaje de KPIs que cumplen sus metas
- Formato: XX.X%

**Evolución Temporal (Últimos 12 meses):**
- Gráfico de línea con evolución del score general
- Eje X: Meses
- Eje Y: Score (0-100)

#### 3. **Impacto por ODS (Grid de Cards)**

Grid de 2-3 columnas mostrando una card por cada ODS:

**Información en cada Card:**
- **Número y Nombre del ODS:** Ej: "ODS 16 - Paz, Justicia e Instituciones Sólidas"
- **Score:** Valor 0-100
- **Estado:** Badge con color:
  - 🟢 **Bueno:** Score >= 70
  - 🟡 **Atención:** Score 50-69
  - 🔴 **Crítico:** Score < 50
- **Tendencia:** Indicador de mejora/empeora/estable
- **Módulos:** Número de módulos que impactan este ODS

**Acción:**
- Hacer clic en cualquier card para ver detalles del ODS

#### 4. **Top 5 KPIs Críticos**

Lista de los 5 KPIs que requieren atención inmediata:

**Información por KPI:**
- **Código:** Ej: "KPI 16.1"
- **Nombre:** Nombre del KPI
- **ODS:** ODS al que pertenece
- **Valor Actual:** Valor actual del KPI
- **Meta:** Valor objetivo
- **Estado:** Badge con color (Cumplido/Atención/Crítico)
- **Tendencia:** Indicador de mejora/empeora/estable

**Acción:**
- Hacer clic en un KPI para ver detalles (navega a página del ODS)

---

## 📈 PÁGINAS DE DETALLE POR ODS

### Ubicación
`/governance/ods-impact/ods-{número}`

Ejemplos:
- `/governance/ods-impact/ods-16` (Paz, Justicia e Instituciones Sólidas)
- `/governance/ods-impact/ods-9` (Industria, Innovación e Infraestructura)
- `/governance/ods-impact/ods-10` (Reducción de las Desigualdades)

### Descripción
Página dedicada a un ODS específico con todos sus KPIs, gráficos y métricas detalladas.

### Componentes Principales

#### 1. **Header**
- **Título:** Nombre del ODS (ej: "ODS 16 - Paz, Justicia e Instituciones Sólidas")
- **Botón Volver:** Regresa al dashboard principal
- **Score del ODS:** Valor 0-100 con estado y tendencia

#### 2. **Resumen del ODS**
- **Descripción:** Explicación del ODS y su importancia
- **Módulos que Impactan:** Lista de módulos de la plataforma que contribuyen
- **KPIs Totales:** Número de KPIs medidos para este ODS

#### 3. **KPIs del ODS**

Grid de cards mostrando cada KPI:

**Información por KPI Card:**
- **Código:** Identificador único (ej: "KPI 16.1")
- **Nombre:** Nombre descriptivo del KPI
- **Valor Actual:** Valor actual con unidad (ej: "85.5%")
- **Meta:** Valor objetivo (ej: "Meta: 95%")
- **Umbral Crítico:** Valor mínimo aceptable
- **Estado:** Badge con color:
  - 🟢 **Cumplido:** Valor >= Meta
  - 🟡 **Atención:** Valor entre Umbral Crítico y Meta
  - 🔴 **Crítico:** Valor < Umbral Crítico
- **Tendencia:** Indicador de mejora/empeora/estable con porcentaje
- **Descripción:** Explicación del KPI y su importancia
- **Fuentes de Datos:** Tablas de base de datos utilizadas

#### 4. **Gráficos de Evolución**

Gráficos de línea mostrando evolución temporal de KPIs principales:

**Tipos de Gráficos:**
- **Evolución de Score:** Evolución del score del ODS en el tiempo
- **Evolución de KPIs Específicos:** Evolución de KPIs individuales
- **Comparación de Métricas:** Comparación entre diferentes métricas

**Características:**
- Eje X: Tiempo (meses/semanas)
- Eje Y: Valor del KPI
- Líneas de referencia: Meta y Umbral Crítico
- Tooltips: Mostrar valores exactos al pasar el mouse

#### 5. **Gráficos de Distribución**

Gráficos de barras/pie mostrando distribución de métricas:

**Tipos de Gráficos:**
- **Distribución por Categoría:** Ej: Sistemas con/sin trazabilidad
- **Distribución por Estado:** Ej: Sistemas certificados vs pendientes
- **Distribución por Tipo:** Ej: Agentes compartidos vs no compartidos

---

## 📊 INTERPRETACIÓN DE KPIs

### Estados de KPI

#### 🟢 **Cumplido (GOOD)**
- **Condición:** Valor actual >= Meta
- **Significado:** El KPI está cumpliendo o superando su objetivo
- **Acción:** Mantener y optimizar

#### 🟡 **Atención (WARNING)**
- **Condición:** Valor actual entre Umbral Crítico y Meta
- **Significado:** El KPI está por debajo de la meta pero aún aceptable
- **Acción:** Monitorear y tomar medidas preventivas

#### 🔴 **Crítico (CRITICAL)**
- **Condición:** Valor actual < Umbral Crítico
- **Significado:** El KPI está en nivel inaceptable
- **Acción:** Requiere atención inmediata y acciones correctivas

### Tendencia de KPI

#### 📈 **Mejora (UP)**
- **Significado:** El KPI está mejorando con el tiempo
- **Indicador:** Flecha verde hacia arriba con porcentaje de mejora

#### 📉 **Empeora (DOWN)**
- **Significado:** El KPI está empeorando con el tiempo
- **Indicador:** Flecha roja hacia abajo con porcentaje de empeoramiento

#### ➡️ **Estable (STABLE)**
- **Significado:** El KPI se mantiene constante
- **Indicador:** Línea horizontal

### Score de ODS

El score de un ODS se calcula como el promedio ponderado de todos sus KPIs:

- **Rango:** 0-100
- **Interpretación:**
  - 80-100: Excelente impacto
  - 60-79: Buen impacto
  - 40-59: Impacto moderado (requiere mejora)
  - 0-39: Impacto bajo (requiere atención urgente)

---

## 🎓 TUTORIALES PASO A PASO

### Tutorial 1: Revisar Impacto General ODS

**Objetivo:** Obtener una visión general del impacto de la plataforma en ODS.

**Pasos:**

1. **Acceder al Dashboard Principal**
   - Navegar a Governance → ODS Impact
   - Ver score general de impacto

2. **Revisar Métricas Principales**
   - Ver Score General de Impacto
   - Revisar Tasa de Cumplimiento
   - Analizar Evolución Temporal (últimos 12 meses)

3. **Revisar Impacto por ODS**
   - Ver cards de cada ODS
   - Identificar ODS con estado "Atención" o "Crítico"
   - Hacer clic en ODS de interés para ver detalles

4. **Revisar Top 5 KPIs Críticos**
   - Ver lista de KPIs que requieren atención
   - Identificar KPIs prioritarios

### Tutorial 2: Analizar un ODS Específico

**Objetivo:** Analizar en detalle el impacto en un ODS específico.

**Pasos:**

1. **Acceder a ODS Específico**
   - Desde dashboard principal, hacer clic en card del ODS
   - O navegar directamente a `/governance/ods-impact/ods-{número}`

2. **Revisar Resumen del ODS**
   - Leer descripción del ODS
   - Ver módulos que impactan
   - Revisar score y estado actual

3. **Analizar KPIs**
   - Revisar cada KPI card
   - Identificar KPIs con estado "Atención" o "Crítico"
   - Leer descripciones y fuentes de datos

4. **Revisar Gráficos de Evolución**
   - Analizar tendencias temporales
   - Identificar períodos de mejora/empeoramiento
   - Comparar con metas y umbrales críticos

5. **Revisar Gráficos de Distribución**
   - Analizar distribución de métricas
   - Identificar categorías con mejor/peor desempeño

### Tutorial 3: Identificar Oportunidades de Mejora

**Objetivo:** Identificar KPIs que requieren atención y definir acciones.

**Pasos:**

1. **Identificar KPIs Críticos**
   - Revisar Top 5 KPIs Críticos en dashboard principal
   - O revisar KPIs con estado "Crítico" en cada ODS

2. **Analizar Causas**
   - Revisar descripción del KPI
   - Ver fuentes de datos
   - Analizar evolución temporal

3. **Definir Acciones**
   - Identificar módulos que impactan el KPI
   - Definir acciones de mejora
   - Priorizar acciones según impacto

4. **Monitorear Mejoras**
   - Revisar evolución del KPI después de implementar mejoras
   - Validar que el KPI mejora
   - Ajustar estrategia si es necesario

### Tutorial 4: Preparar Reporte de Impacto ODS

**Objetivo:** Recopilar información para reporte a stakeholders.

**Pasos:**

1. **Recopilar Datos Generales**
   - Acceder a dashboard principal
   - Anotar Score General de Impacto
   - Anotar Tasa de Cumplimiento
   - Capturar gráfico de Evolución Temporal

2. **Recopilar Datos por ODS**
   - Para cada ODS relevante:
     - Anotar score y estado
     - Listar KPIs principales con valores
     - Capturar gráficos de evolución

3. **Identificar Highlights**
   - Destacar ODS con mejor desempeño
   - Identificar mejoras significativas
   - Mencionar áreas de mejora

4. **Preparar Reporte**
   - Organizar información recopilada
   - Incluir gráficos y métricas
   - Agregar análisis y conclusiones

---

## 💡 CONSEJOS Y MEJORES PRÁCTICAS

### Para Usuarios Regulares

- ✅ **Revisar Semanalmente:** Revisar dashboard principal semanalmente para monitorear evolución
- ✅ **Enfocarse en Críticos:** Priorizar revisión de KPIs con estado "Crítico"
- ✅ **Analizar Tendencias:** Prestar atención a tendencias negativas
- ✅ **Documentar Acciones:** Documentar acciones tomadas para mejorar KPIs

### Para Compliance Officers

- ✅ **Reportes Mensuales:** Generar reportes mensuales de impacto ODS
- ✅ **Auditorías:** Usar datos para auditorías de sostenibilidad
- ✅ **Comunicación:** Comunicar resultados a stakeholders regularmente

### Para Governance Managers

- ✅ **Estrategia:** Usar datos para definir estrategias de mejora
- ✅ **Priorización:** Priorizar mejoras basándose en KPIs críticos
- ✅ **Benchmarking:** Comparar impacto entre proyectos y organizaciones

---

## ❓ PREGUNTAS FRECUENTES

### ¿Cómo se calcula el Score de un ODS?

El score de un ODS se calcula como el promedio ponderado de todos sus KPIs, normalizado a una escala de 0-100.

### ¿Con qué frecuencia se actualizan los KPIs?

Los KPIs se calculan en tiempo real basándose en los datos de la base de datos. La frecuencia de actualización depende de la frecuencia de medición de cada KPI (diaria, semanal, mensual).

### ¿Puedo exportar los datos?

Actualmente, los datos se muestran en pantalla. La funcionalidad de exportación está planificada para futuras versiones.

### ¿Qué hago si un KPI está en estado "Crítico"?

1. Revisar la descripción del KPI para entender qué mide
2. Analizar las fuentes de datos
3. Identificar los módulos que impactan el KPI
4. Definir y ejecutar acciones de mejora
5. Monitorear la evolución del KPI

### ¿Cómo interpreto la tendencia de un KPI?

- **UP (Mejora):** El KPI está mejorando, mantener acciones actuales
- **DOWN (Empeora):** El KPI está empeorando, requiere atención inmediata
- **STABLE (Estable):** El KPI se mantiene constante, considerar acciones de mejora

---

**Última Actualización:** Diciembre 2025
