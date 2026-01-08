# 📘 GUÍA FUNCIONAL - TELEMETRÍA Y ANÁLISIS

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales, DevOps, Analistas de Datos, Project Managers

---

## 🎯 ¿QUÉ ES TELEMETRÍA Y ANÁLISIS?

Telemetría y Análisis es un sistema de **monitoreo y análisis de eventos** generados por los componentes de AI OS (Artificial Intelligence Operating System). Su objetivo es proporcionar visibilidad completa sobre el comportamiento, rendimiento y uso de los sistemas de IA en producción.

### 🎯 Propósito Principal

El sistema de Telemetría permite:

1. **Monitoreo en Tiempo Real:** Visualización de eventos y métricas de componentes de IA
2. **Análisis de Rendimiento:** KPIs agregados sobre latencia, costos, tokens y eventos
3. **Búsqueda Avanzada:** Búsqueda de eventos por múltiples criterios (contenido, componente, proyecto, fechas)
4. **Análisis de Componentes:** Estadísticas detalladas por componente de IA
5. **Análisis Temporal:** Visualización de métricas mensuales y tendencias
6. **Gobernanza:** Detección de bias, toxicidad, PII y secretos en eventos

---

## 🚀 ¿PARA QUÉ SIRVE TELEMETRÍA?

### 1. **Para Monitoreo Operacional**

- **Visibilidad Completa:** Ver todos los eventos generados por componentes de IA
- **KPIs Agregados:** Métricas consolidadas (total eventos, costos, tokens, latencia promedio)
- **Análisis de Componentes:** Comparar rendimiento entre diferentes componentes
- **Detección de Problemas:** Identificar componentes con alta latencia o costos elevados

### 2. **Para Optimización de Costos**

- **Análisis de Costos:** Visualizar costos totales y por componente
- **Uso de Tokens:** Monitorear consumo de tokens por componente
- **Identificación de Oportunidades:** Detectar componentes con costos inusualmente altos
- **Tendencias:** Analizar evolución de costos a lo largo del tiempo

### 3. **Para Análisis de Rendimiento**

- **Latencia:** Monitorear tiempos de respuesta de componentes
- **Throughput:** Analizar volumen de eventos procesados
- **Comparación:** Comparar rendimiento entre componentes
- **Tendencias:** Identificar degradación de rendimiento a lo largo del tiempo

### 4. **Para Gobernanza y Compliance**

- **Detección de Bias:** Identificar eventos con sesgos detectados
- **Toxicidad:** Detectar contenido tóxico en eventos
- **PII (Personally Identifiable Information):** Identificar datos personales en eventos
- **Secretos:** Detectar secretos o credenciales expuestas en eventos
- **Categorización Automática:** El sistema calcula automáticamente:
  - **Estado de Cumplimiento (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Nivel de Riesgo (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
  - **Categoría de Compliance (complianceCategory):** GDPR, SECURITY, LEGAL
  - **Etiquetas de Problemas (issueTags):** bias_critical, pii_exposure, secret_leak, toxicity_high, etc.

### 5. **Para Búsqueda y Auditoría**

- **Búsqueda por Contenido:** Buscar eventos que contengan texto específico en payload
- **Búsqueda por Componente:** Filtrar eventos por componente específico
- **Búsqueda por Proyecto:** Ver eventos de todos los componentes de un proyecto
- **Filtros Temporales:** Analizar eventos en rangos de fechas específicos

---

## 📊 COMPONENTES PRINCIPALES DE TELEMETRÍA

### 1. **Dashboard de Telemetría**

Vista centralizada que muestra:
- **KPIs Principales:** Total eventos, costos totales, tokens totales, latencia promedio, número de componentes
- **Filtros de Fecha:** Seleccionar rango de fechas para análisis
- **Estadísticas por Componente:** Tabla con métricas agregadas por componente
- **Gráficos:** Visualización de tendencias y distribuciones
- **Navegación:** Acceso rápido a búsqueda y análisis mensual

### 2. **Búsqueda de Eventos**

Sistema completo para buscar y filtrar eventos:
- **Búsqueda por Contenido:** Buscar texto en payload y métricas
- **Filtros Múltiples:** Por componente UUID, agente, tipo de evento, severidad
- **Filtros Temporales:** Rango de fechas de inicio y fin
- **Filtros de Gobernanza:** Bias, toxicidad, PII, secretos detectados
- **Filtros de Compliance/Security:**
  - **Estado de Cumplimiento:** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  - **Nivel de Riesgo:** LOW, MEDIUM, HIGH, CRITICAL
  - **Categoría de Compliance:** GDPR, SECURITY, LEGAL
  - **Etiquetas de Problemas:** Filtrar por tags específicos (bias_critical, pii_exposure, etc.)
- **Paginación:** Navegación por páginas de resultados
- **Visualización Detallada:** Página de detalle completa para cada evento con payload completo, métricas y análisis

### 3. **Análisis Mensual**

Dashboard de análisis temporal:
- **Métricas Mensuales:** Total eventos, costos, tokens, latencia promedio por mes
- **Gráficos de Tendencias:** Visualización de evolución mensual
- **Selector de Año:** Analizar diferentes años
- **Comparación:** Comparar métricas entre meses

### 4. **Estadísticas por Componente**

Análisis detallado por componente:
- **Métricas Agregadas:** Total eventos, latencia promedio, tokens totales, costos totales
- **Comparación:** Ver todos los componentes en una tabla
- **Filtros Temporales:** Analizar componentes en rangos de fechas específicos

---

## 🔄 FLUJO DE TRABAJO TÍPICO

### Escenario 1: Monitoreo Diario

1. **Acceso al Dashboard**
   - Navegar a Governance → Telemetry → Dashboard
   - Ver KPIs principales del período (últimos 30 días por defecto)

2. **Análisis de Componentes**
   - Revisar tabla de estadísticas por componente
   - Identificar componentes con alta latencia o costos elevados
   - Comparar rendimiento entre componentes

3. **Análisis de Tendencias**
   - Revisar gráficos de distribución
   - Identificar patrones o anomalías

### Escenario 2: Investigación de Problemas

1. **Identificación del Problema**
   - Detectar componente con métricas anómalas en el dashboard
   - O recibir alerta sobre evento específico

2. **Búsqueda de Eventos**
   - Ir a la página de Búsqueda
   - Filtrar por componente UUID o proyecto
   - Aplicar filtros de fecha para el período del problema
   - Buscar por contenido específico si se conoce

3. **Análisis de Eventos**
   - Revisar eventos encontrados
   - Navegar a la página de detalle de eventos relevantes
   - Ver payload completo, métricas y resultados de análisis
   - Revisar campos de compliance/security calculados automáticamente:
     - Estado de cumplimiento y nivel de riesgo
     - Categoría de compliance y etiquetas de problemas
   - Verificar detecciones de gobernanza (bias, toxicidad, PII, secretos)

4. **Toma de Decisiones**
   - Identificar causa raíz del problema
   - Determinar acciones correctivas necesarias

### Escenario 3: Análisis de Costos

1. **Análisis de Costos Totales**
   - Ir al Dashboard
   - Ver KPI de costos totales
   - Ajustar rango de fechas si es necesario

2. **Análisis por Componente**
   - Revisar tabla de estadísticas por componente
   - Identificar componentes con mayor costo
   - Analizar relación costo/tokens/eventos

3. **Análisis Temporal**
   - Ir a Análisis Mensual
   - Ver evolución de costos mes a mes
   - Identificar tendencias y picos

4. **Optimización**
   - Identificar oportunidades de optimización
   - Comparar costos entre componentes similares
   - Planificar acciones de reducción de costos

### Escenario 4: Auditoría de Gobernanza y Compliance

1. **Búsqueda de Eventos con Problemas**
   - Ir a la página de Búsqueda
   - Usar filtros de compliance/security:
     - **Estado de Cumplimiento:** VIOLATION o CRITICAL_VIOLATION
     - **Nivel de Riesgo:** HIGH o CRITICAL
     - **Categoría:** GDPR, SECURITY o LEGAL según necesidad
     - **Etiquetas:** Filtrar por tags específicos (pii_exposure, secret_leak, bias_critical, etc.)
   - O usar filtros de gobernanza tradicionales:
     - Bias detectado
     - Toxicidad detectada
     - PII detectado
     - Secretos detectados

2. **Análisis de Eventos**
   - Revisar eventos que activaron detecciones
   - Navegar a la página de detalle de cada evento
   - Ver payload completo, métricas y resultados de análisis
   - Revisar campos calculados automáticamente (complianceStatus, riskLevel, complianceCategory, issueTags)

3. **Documentación**
   - Exportar o documentar eventos relevantes
   - Generar reportes si es necesario
   - Usar los campos de compliance/security para clasificación y priorización

---

## 📈 MÉTRICAS Y KPIs

### KPIs Principales

1. **Total de Eventos**
   - Número total de eventos de telemetría en el período
   - Indicador de volumen de actividad

2. **Costo Total (USD)**
   - Suma de costos de todos los eventos
   - Calculado a partir de métricas de costo por evento

3. **Tokens Totales**
   - Suma de tokens consumidos en todos los eventos
   - Indicador de uso de modelos de IA

4. **Latencia Promedio (ms)**
   - Tiempo promedio de respuesta de componentes
   - Indicador de rendimiento

5. **Número de Componentes**
   - Cantidad de componentes únicos que generaron eventos
   - Indicador de cobertura

### Métricas por Componente

Para cada componente se calculan:
- **Total de Eventos:** Número de eventos generados
- **Latencia Promedio:** Tiempo promedio de respuesta
- **Tokens Totales:** Suma de tokens consumidos
- **Costo Total:** Suma de costos en USD

---

## 🔍 CAPACIDADES DE BÚSQUEDA

### Criterios de Búsqueda Disponibles

1. **Búsqueda por Contenido**
   - Busca texto en payload JSONB y métricas
   - Útil para encontrar eventos específicos

2. **Filtro por Componente UUID**
   - Filtrar eventos de un componente específico
   - Identificar componente por UUID

3. **Filtro por Agente**
   - Filtrar por `agentExternalId`
   - Útil para rastrear eventos de agentes específicos

4. **Filtro por Tipo de Evento**
   - Filtrar por `eventType` (ej: INTERACTION_COMPLETED, ERROR, etc.)

5. **Filtro por Severidad**
   - INFO, WARN, ERROR, DEBUG
   - Útil para encontrar eventos de error

6. **Filtros Temporales**
   - Fecha de inicio
   - Fecha de fin
   - Permite análisis en rangos específicos

7. **Filtros de Gobernanza**
   - **Bias Checked:** Eventos donde se verificó sesgo
   - **Toxicity Checked:** Eventos donde se verificó toxicidad
   - **PII Detected:** Eventos con datos personales detectados
   - **Secret Detected:** Eventos con secretos detectados

8. **Filtros de Compliance/Security**
   - **Estado de Cumplimiento (complianceStatus):** PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
   - **Nivel de Riesgo (riskLevel):** LOW, MEDIUM, HIGH, CRITICAL
   - **Categoría de Compliance (complianceCategory):** GDPR, SECURITY, LEGAL
   - **Etiqueta de Problema (issueTag):** bias_critical, bias_high, bias_medium, pii_exposure, secret_leak, toxicity_critical, toxicity_high, toxicity_medium, compliance_issue

### Combinación de Filtros

Todos los filtros pueden combinarse para búsquedas precisas:
- Ejemplo: Eventos de un componente específico en un rango de fechas con PII detectado
- Ejemplo: Eventos de error de un proyecto en el último mes

---

## 📊 VISUALIZACIONES

### Dashboard Principal

1. **Tarjetas de KPIs**
   - 5 tarjetas con métricas principales
   - Iconos y colores para identificación rápida

2. **Gráfico de Distribución de Eventos**
   - Gráfico de barras mostrando distribución de eventos por componente
   - Permite identificar componentes más activos

3. **Gráfico de Costos por Componente**
   - Gráfico de barras mostrando costos por componente
   - Útil para análisis de costos

4. **Tabla de Estadísticas por Componente**
   - Tabla completa con todas las métricas por componente
   - Ordenable y filtrable

### Análisis Mensual

1. **Gráfico de Eventos Mensuales**
   - Línea temporal mostrando total de eventos por mes
   - Permite ver tendencias

2. **Gráfico de Costos Mensuales**
   - Línea temporal mostrando costos totales por mes
   - Útil para análisis financiero

3. **Gráfico de Tokens Mensuales**
   - Línea temporal mostrando tokens totales por mes
   - Indicador de uso

4. **Gráfico de Latencia Promedio Mensual**
   - Línea temporal mostrando latencia promedio por mes
   - Permite detectar degradación de rendimiento

---

## 🎯 CASOS DE USO COMUNES

### Caso 1: "¿Cuánto cuesta mi sistema de IA este mes?"

1. Ir a Dashboard de Telemetría
2. Ajustar filtros de fecha al mes actual
3. Ver KPI "Costo Total (USD)"

### Caso 2: "¿Qué componente tiene mayor latencia?"

1. Ir a Dashboard de Telemetría
2. Revisar tabla de estadísticas por componente
3. Ordenar por latencia promedio (descendente)
4. Identificar componente con mayor latencia

### Caso 3: "Buscar todos los errores del último día"

1. Ir a Búsqueda de Eventos
2. Seleccionar severidad "ERROR"
3. Establecer fecha de inicio: ayer
4. Establecer fecha de fin: hoy
5. Hacer clic en "Buscar"

### Caso 4: "Analizar tendencias del último año"

1. Ir a Análisis Mensual
2. Seleccionar año actual
3. Revisar gráficos de tendencias
4. Comparar meses

### Caso 5: "Encontrar eventos con datos personales"

1. Ir a Búsqueda de Eventos
2. Usar filtro "Categoría de Compliance" = "GDPR" o activar checkbox "PII"
3. Establecer rango de fechas
4. Hacer clic en "Buscar"
5. Revisar eventos encontrados
6. Navegar a la página de detalle de eventos relevantes para ver información completa

### Caso 6: "Identificar eventos críticos de seguridad"

1. Ir a Búsqueda de Eventos
2. Establecer filtros:
   - **Estado de Cumplimiento:** CRITICAL_VIOLATION
   - **Nivel de Riesgo:** CRITICAL
   - **Categoría de Compliance:** SECURITY
3. Establecer rango de fechas
4. Hacer clic en "Buscar"
5. Revisar eventos encontrados
6. Navegar a la página de detalle para análisis completo

---

## 🔐 CONSIDERACIONES DE SEGURIDAD Y PRIVACIDAD

### Datos Sensibles

- Los eventos pueden contener información sensible en payloads
- El sistema detecta automáticamente PII y secretos
- Se recomienda revisar eventos con detecciones antes de compartir

### Acceso a Datos

- Solo usuarios autorizados pueden acceder a telemetría
- Los datos están protegidos por autenticación y autorización
- Se recomienda limitar acceso a datos sensibles

---

## 📚 REFERENCIAS

### Pantallas Relacionadas

- **Dashboard:** `/governance/telemetry/dashboard`
- **Búsqueda:** `/governance/telemetry/search`
- **Análisis Mensual:** `/governance/telemetry/analytics`
- **Detalle de Evento:** `/governance/telemetry/events/{id}` (página de detalle completo)

### Documentación Técnica

- Ver guías de desarrolladores para detalles técnicos
- Ver guía de uso de pantallas para instrucciones paso a paso

---

**Última Actualización:** Diciembre 2025

**Cambios Recientes:**
- Agregados campos de compliance/security calculados automáticamente (complianceStatus, riskLevel, complianceCategory, issueTags)
- Nuevos filtros de búsqueda por compliance/security
- Página de detalle separada para visualización completa de eventos
- Mejoras en datos mock con variaciones realistas por mes y año
