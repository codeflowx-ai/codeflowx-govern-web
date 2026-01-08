# 📘 GUÍA FUNCIONAL - MONITORIZACIÓN Y OBSERVABILIDAD

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** DevOps Engineers, SREs, System Administrators, Compliance Officers, Project Managers, Architects

---

## 🎯 ¿QUÉ ES LA MONITORIZACIÓN?

La **Monitorización** es un módulo operacional que permite observar, monitorear y gestionar el estado, rendimiento y salud de todos los sistemas, servicios y componentes de la plataforma **en tiempo real**. El módulo proporciona herramientas completas para el seguimiento de alertas operacionales, métricas técnicas de infraestructura, logs de auditoría, salud del sistema y gobernanza distribuida.

> **⚠️ Diferencia con Analytics:** Este módulo se enfoca en **monitoreo operacional** (infraestructura, sistemas, servicios). Para análisis de modelos ML, métricas de negocio, detección de sesgos y reportes analíticos, consulta el módulo **Analytics**.

### 🎯 Propósito Principal

El módulo de monitorización permite:

1. **Observabilidad Operacional:** Visibilidad en tiempo real del estado de todos los sistemas, servicios e infraestructura
2. **Gestión de Alertas del Sistema:** Detección, notificación y gestión de alertas operacionales (infraestructura, servicios, recursos)
3. **Métricas Técnicas:** Seguimiento de métricas técnicas de rendimiento (latencia, throughput, disponibilidad, CPU, memoria, disco, red)
4. **Logs de Auditoría Operacionales:** Registro y consulta de logs de eventos del sistema para trazabilidad y compliance
5. **Salud del Sistema:** Monitoreo de la salud general del sistema y sus componentes técnicos
6. **Gobernanza Distribuida:** Gestión de gobernanza en sistemas distribuidos y microservicios
7. **Respuesta a Incidentes:** Formularios y workflows para responder a alertas críticas operacionales
8. **Tendencias Operacionales:** Visualización de tendencias técnicas y análisis histórico de métricas de infraestructura

---

## 🔀 DIFERENCIA CON ANALYTICS

Es importante entender la diferencia entre **Monitorización** y **Analytics**:

| Aspecto | **Monitorización** (este módulo) | **Analytics** |
|---------|-----------------------------------|---------------|
| **Enfoque** | Operacional y técnico | Análisis de negocio y modelos ML |
| **Métricas** | Infraestructura, sistemas, servicios<br>(CPU, memoria, latencia, throughput, disponibilidad) | Modelos ML, precisión, sesgos, impacto<br>(métricas de negocio y compliance) |
| **Alertas** | Operacionales<br>(sistemas, recursos, disponibilidad) | No aplica (se enfoca en reportes) |
| **Temporalidad** | Tiempo real y corto plazo | Análisis histórico y tendencias |
| **Audiencia Principal** | DevOps, SREs, System Administrators | Data Analysts, ML Engineers, Business Analysts |
| **Casos de Uso** | ¿Está el sistema funcionando?<br>¿Hay problemas de infraestructura?<br>¿Cuál es la latencia del servicio? | ¿El modelo tiene sesgos?<br>¿Cuál es la precisión del modelo?<br>¿Cuál es el impacto en compliance? |

**Resumen:** Monitorización = "¿Está funcionando el sistema?" | Analytics = "¿Cómo está funcionando el modelo/negocio?"

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 20 - Monitoreo Post-Mercado

Los sistemas de IA de alto riesgo deben incluir:
- **Monitoreo continuo:** Seguimiento del rendimiento del sistema en producción
- **Detección de anomalías:** Identificación de problemas de rendimiento o comportamiento
- **Registro de incidentes:** Documentación de incidentes y respuestas
- **Actualización del sistema:** Mantenimiento y actualización según sea necesario

#### Art. 15 - Requisitos de Transparencia

Requiere:
- **Información sobre el sistema:** Estado y rendimiento del sistema
- **Registro de operaciones:** Trazabilidad de operaciones y decisiones

### ISO 42001 - Gestión de Sistemas de IA

#### Cláusula 9.1 - Monitoreo, Medición, Análisis y Evaluación

Requiere:
- **Monitoreo continuo:** Seguimiento del rendimiento de los sistemas de IA
- **Métricas de rendimiento:** Medición de KPIs relevantes
- **Análisis de datos:** Evaluación de datos de monitoreo
- **Mejora continua:** Uso de datos de monitoreo para mejorar el sistema

### ISO/IEC 27001 - Seguridad de la Información

#### A.12.4 - Registro de Eventos

Requiere:
- **Logs de auditoría:** Registro de eventos relevantes para seguridad
- **Retención de logs:** Almacenamiento y retención de logs según políticas
- **Análisis de logs:** Revisión y análisis de logs de seguridad

---

## 🚀 ¿PARA QUÉ SIRVE LA MONITORIZACIÓN?

### 1. **Cumplimiento Normativo**

El módulo facilita el cumplimiento de:
- **EU AI Act Art. 20:** Monitoreo post-mercado y detección de anomalías
- **ISO 42001 9.1:** Monitoreo, medición y análisis continuo
- **ISO/IEC 27001 A.12.4:** Registro de eventos y auditoría
- **Trazabilidad Regulatoria:** Registro completo de eventos para auditorías

### 2. **Observabilidad del Sistema**

Proporciona herramientas para:
- **Visibilidad en Tiempo Real:** Estado actual de todos los sistemas, servicios e infraestructura
- **Detección Temprana:** Identificación proactiva de problemas operacionales antes de que impacten usuarios
- **Tendencias Operacionales:** Visualización de patrones y tendencias históricas de métricas técnicas
- **Dashboards Consolidados:** Vista unificada de métricas técnicas y alertas operacionales

### 3. **Gestión de Incidentes**

Permite:
- **Alertas Automáticas:** Notificación inmediata de problemas críticos
- **Clasificación de Alertas:** Priorización por severidad y tipo
- **Respuesta Estructurada:** Formularios y workflows para gestionar respuestas
- **Seguimiento de Resolución:** Trazabilidad completa de incidentes desde detección hasta resolución

### 4. **Optimización de Rendimiento Operacional**

Facilita:
- **Métricas Técnicas de Rendimiento:** KPIs operacionales (latencia, throughput, disponibilidad, CPU, memoria, disco, red)
- **Análisis de Cuellos de Botella:** Identificación de componentes de infraestructura con problemas de rendimiento
- **Optimización Continua:** Datos técnicos para tomar decisiones informadas sobre mejoras de infraestructura
- **Capacidad y Escalabilidad:** Análisis de uso de recursos técnicos y necesidades de escalado de infraestructura

### 5. **Auditoría y Compliance**

Permite:
- **Logs de Auditoría:** Registro completo de eventos y operaciones
- **Trazabilidad:** Seguimiento de quién, qué, cuándo y dónde
- **Cumplimiento:** Evidencia para auditorías regulatorias
- **Análisis Forense:** Investigación de incidentes y problemas

### 6. **Gobernanza Distribuida**

Facilita:
- **Monitoreo Distribuido:** Gestión de sistemas distribuidos y microservicios
- **Consistencia:** Asegurar coherencia en sistemas distribuidos
- **Performance de Vistas:** Monitoreo de rendimiento en arquitecturas distribuidas

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Dashboard de Monitorización**

Pantalla principal con vista consolidada del estado del sistema:

#### **KPIs Principales**
- **Total de Alertas:** Número total de alertas activas
- **Alertas Críticas:** Alertas con severidad alta
- **Métricas Activas:** Número de métricas siendo monitoreadas
- **Salud del Sistema:** Estado general del sistema (Healthy, Degraded, Critical)

#### **Alertas Recientes**
- Lista de alertas más recientes con:
  - Nombre de la alerta
  - Tipo y severidad
  - Estado (ACTIVE, ACKNOWLEDGED, RESOLVED)
  - Fecha de activación
  - Componente afectado

#### **Métricas Técnicas en Tiempo Real**
- Gráficos de métricas operacionales clave:
  - Latencia promedio de servicios
  - Throughput de APIs
  - Tasa de error del sistema
  - Disponibilidad de servicios
  - Uso de CPU y memoria
  - Uso de disco y red

#### **Salud de Componentes**
- Estado de componentes principales:
  - APIs
  - Bases de datos
  - Servicios de backend
  - Infraestructura

### 2. **Gestión de Alertas**

Pantalla para gestionar alertas del sistema:

#### **Listado de Alertas**
- **Vista Tabular:** Tabla paginada con todas las alertas
- **Métricas Principales:**
  - Total de alertas
  - Alertas activas
  - Alertas reconocidas (acknowledged)
  - Alertas resueltas
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo de alerta
  - Filtro por severidad (HIGH, MEDIUM, LOW)
  - Filtro por estado (ACTIVE, ACKNOWLEDGED, RESOLVED)
- **Acciones:**
  - Ver detalles de la alerta
  - Reconocer alerta (acknowledge)
  - Resolver alerta
  - Responder a alerta crítica

#### **Página de Detalle de Alerta**

Página completa con información detallada:

**Información General:**
- Nombre de la alerta
- Tipo de alerta
- Severidad (HIGH, MEDIUM, LOW)
- Estado (ACTIVE, ACKNOWLEDGED, RESOLVED)
- Descripción detallada
- Datos de la alerta (JSON con información adicional)

**Timestamps:**
- Fecha de activación (triggered at)
- Fecha de reconocimiento (acknowledged at)
- Usuario que reconoció (acknowledged by)
- Fecha de resolución (resolved at)
- Usuario que resolvió (resolved by)
- Notas de resolución

**Metadata:**
- Origen del sistema
- Componente afectado
- Información adicional del contexto

**Acciones:**
- Modo edición para actualizar información
- Reconocer alerta
- Resolver alerta
- Navegar a formulario de respuesta

#### **Alertas del Sistema**

Pantalla específica para alertas del sistema (system alerts):
- Similar estructura a alertas generales
- Enfocado en alertas de infraestructura y sistema
- Información específica de componentes del sistema

### 3. **Gestión de Métricas Técnicas**

Pantalla para monitorear métricas técnicas de infraestructura y sistemas:

#### **Listado de Métricas Técnicas**
- **Vista Tabular:** Tabla paginada con todas las métricas operacionales
- **Métricas Principales:**
  - Total de métricas técnicas
  - Métricas activas
  - Métricas con valores anómalos
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo de métrica técnica (PERFORMANCE, AVAILABILITY, ERROR_RATE, RESOURCE_USAGE, NETWORK, etc.)
  - Filtro por estado
- **Acciones:**
  - Ver detalles de la métrica
  - Editar métrica
  - Ver gráficos de tendencias operacionales

#### **Página de Detalle de Métrica Técnica**

Página completa con información detallada:

**Información General:**
- Nombre de la métrica técnica
- Tipo de métrica (rendimiento, recursos, red, etc.)
- Valor actual
- Estado (ACTIVE, INACTIVE)
- Descripción
- Unidad de medida (ms, %, req/s, MB, etc.)

**Timestamps:**
- Fecha de recolección (collected at)
- Última actualización
- Frecuencia de recolección

**Gráficos:**
- Gráfico de tendencias históricas operacionales
- Comparación con períodos anteriores
- Análisis de patrones técnicos

#### **Métricas del Sistema**

Pantalla específica para métricas técnicas del sistema:
- **Métricas de Infraestructura:** CPU, memoria, disco, red
- **Métricas de Recursos:** Uso de recursos por componente
- **Métricas de Red:** Ancho de banda, latencia de red, conexiones
- **Métricas de Servicios:** Disponibilidad, tiempo de respuesta, throughput de servicios

### 4. **Logs de Auditoría**

Pantalla para consultar y analizar logs de auditoría:

#### **Página de Detalle de Log de Auditoría**

Información detallada de cada log:
- **Información General:**
  - ID del log
  - Tipo de evento
  - Nivel de severidad
  - Mensaje
  - Componente origen
- **Contexto:**
  - Usuario que generó el evento
  - IP de origen
  - Timestamp exacto
  - Datos adicionales (JSON)
- **Trazabilidad:**
  - Relación con otros eventos
  - Cadena de eventos relacionada

### 5. **Gobernanza Distribuida**

Pantalla para gestionar gobernanza en sistemas distribuidos:

#### **Página de Detalle de Gobernanza**

Información sobre gobernanza distribuida:
- **Estado de Consistencia:** Estado de coherencia en sistemas distribuidos
- **Configuración:** Configuraciones de gobernanza
- **Métricas:** Métricas de gobernanza distribuida
- **Eventos:** Eventos relacionados con gobernanza

### 6. **Salud del Sistema**

Pantalla para monitorear la salud general del sistema:

#### **Página de Detalle de Salud del Sistema**

Información sobre salud del sistema:
- **Estado General:** Healthy, Degraded, Critical
- **Componentes:**
  - Estado de cada componente
  - Métricas de salud por componente
  - Última verificación
- **Métricas de Salud:**
  - Disponibilidad
  - Tiempo de respuesta
  - Tasa de error
  - Uptime

### 7. **Formularios de Respuesta e Intervención**

#### **Formulario de Respuesta a Alertas**

Formulario para responder a alertas críticas:
- **Información de la Alerta:**
  - Nombre y tipo de alerta
  - Severidad
  - Estado actual
- **Formulario de Respuesta:**
  - **Decisión:** Campo de texto para describir la decisión tomada
  - **Notas:** Campo de texto largo para notas adicionales
  - **Acciones:**
    - Guardar respuesta
    - Reconocer alerta
    - Resolver alerta

#### **Formulario de Intervención de Performance**

Formulario para registrar intervenciones de rendimiento:
- **Información de la Intervención:**
  - Tipo de intervención
  - Componente afectado
  - Descripción del problema
  - Acciones tomadas
  - Resultados esperados

#### **Formulario de Performance de Vistas**

Formulario para gestionar performance en arquitecturas distribuidas:
- **Información de la Vista:**
  - Nombre de la vista
  - Estado de sincronización
  - Métricas de rendimiento
  - Configuración

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Gestión de una Alerta

1. **Detección de Alerta**
   - El sistema detecta una condición que requiere atención
   - Se genera automáticamente una alerta con:
     - Nombre y tipo
     - Severidad (calculada automáticamente)
     - Estado inicial: ACTIVE
     - Timestamp de activación

2. **Notificación**
   - La alerta aparece en el dashboard
   - Se muestra en el listado de alertas
   - Notificaciones según configuración (email, Slack, etc.)

3. **Revisión de la Alerta**
   - Navegar a la página de detalle de la alerta
   - Revisar información completa:
     - Descripción del problema
     - Datos de la alerta (JSON)
     - Componente afectado
     - Timestamps

4. **Reconocimiento (Acknowledgment)**
   - Si el equipo está trabajando en la alerta:
     - Hacer clic en "Reconocer Alerta"
     - Opcionalmente agregar notas
     - Estado cambia a ACKNOWLEDGED
     - Se registra quién y cuándo reconoció

5. **Respuesta a Alerta Crítica**
   - Para alertas de alta severidad:
     - Navegar a "Formulario de Respuesta a Alertas"
     - Completar:
       - Decisión tomada
       - Notas adicionales
     - Guardar respuesta

6. **Resolución**
   - Una vez resuelto el problema:
     - Hacer clic en "Resolver Alerta"
     - Agregar notas de resolución
     - Estado cambia a RESOLVED
     - Se registra quién y cuándo resolvió

### Flujo 2: Monitoreo de Métricas

1. **Visualización de Métricas**
   - Navegar a Monitoring → Metrics
   - Ver listado de todas las métricas
   - Filtrar por tipo o estado

2. **Análisis de Detalle**
   - Hacer clic en una métrica para ver detalles
   - Revisar:
     - Valor actual
     - Gráfico de tendencias
     - Comparación histórica
     - Estado y salud

3. **Identificación de Anomalías**
   - Si una métrica muestra valores anómalos:
     - Revisar gráfico de tendencias
     - Comparar con períodos anteriores
     - Analizar contexto (eventos relacionados)

4. **Acción Correctiva**
   - Si es necesario:
     - Crear alerta manual
     - Documentar observaciones
     - Iniciar investigación

### Flujo 3: Consulta de Logs de Auditoría

1. **Acceso a Logs**
   - Navegar a Monitoring → Distributed → Audit Log
   - Buscar logs por:
     - Tipo de evento
     - Rango de fechas
     - Usuario
     - Componente

2. **Revisión de Detalle**
   - Hacer clic en un log para ver detalles completos
   - Revisar:
     - Información del evento
     - Contexto (usuario, IP, timestamp)
     - Datos adicionales
     - Eventos relacionados

3. **Análisis y Trazabilidad**
   - Analizar cadena de eventos
   - Identificar patrones
   - Usar para auditorías o investigaciones

### Flujo 4: Respuesta a Alerta Crítica

1. **Identificación de Alerta Crítica**
   - Alerta con severidad HIGH aparece en dashboard
   - Notificación inmediata según configuración

2. **Acceso al Formulario de Respuesta**
   - Navegar a Monitoring → Alerts → Response Form
   - O desde la página de detalle de la alerta

3. **Completar Formulario**
   - **Decisión:** Describir la decisión tomada
     - Ejemplo: "Escalar a equipo de infraestructura"
     - Ejemplo: "Aplicar hotfix inmediato"
   - **Notas:** Agregar información adicional
     - Contexto adicional
     - Acciones ya tomadas
     - Próximos pasos

4. **Guardar y Actuar**
   - Guardar respuesta
   - La respuesta queda registrada en la alerta
   - Continuar con acciones de resolución

### Flujo 5: Monitoreo de Salud del Sistema

1. **Vista General**
   - Acceder al Dashboard de Monitorización
   - Revisar estado general del sistema
   - Identificar componentes con problemas

2. **Análisis de Componentes**
   - Navegar a Monitoring → Distributed → System Health
   - Revisar estado de cada componente:
     - APIs
     - Bases de datos
     - Servicios backend
     - Infraestructura

3. **Acción Preventiva**
   - Si un componente muestra degradación:
     - Investigar métricas relacionadas
     - Revisar alertas asociadas
     - Tomar acciones preventivas antes de fallo crítico

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **DevOps Engineers** ⚙️
- **Responsabilidad:** Operación y mantenimiento de sistemas
- **Uso:** Monitoreo continuo, gestión de alertas, respuesta a incidentes
- **Beneficio:** Visibilidad completa del sistema, detección temprana de problemas

#### 2. **SREs (Site Reliability Engineers)** 🔧
- **Responsabilidad:** Confiabilidad y rendimiento de sistemas
- **Uso:** Análisis de métricas, optimización de rendimiento, gestión de SLA
- **Beneficio:** Datos para mejorar confiabilidad, análisis de tendencias

#### 3. **System Administrators** 🖥️
- **Responsabilidad:** Administración de infraestructura
- **Uso:** Monitoreo de salud del sistema, gestión de alertas de infraestructura
- **Beneficio:** Visibilidad de infraestructura, gestión proactiva

#### 4. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo
- **Uso:** Consulta de logs de auditoría, verificación de trazabilidad
- **Beneficio:** Evidencia para auditorías, cumplimiento regulatorio

#### 5. **Project Managers** 📋
- **Responsabilidad:** Gestión de proyectos
- **Uso:** Monitoreo de estado de sistemas relacionados con proyectos
- **Beneficio:** Visibilidad de salud de sistemas críticos para proyectos

#### 6. **Architects** 🏗️
- **Responsabilidad:** Arquitectura y diseño de sistemas
- **Uso:** Análisis de métricas, identificación de patrones, optimización
- **Beneficio:** Datos para decisiones arquitectónicas, análisis de rendimiento

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Observabilidad Completa:** Visibilidad en tiempo real de todo el sistema
2. **Detección Temprana:** Identificación proactiva de problemas antes de impactar usuarios
3. **Cumplimiento Normativo:** Facilita cumplimiento de EU AI Act, ISO 42001, ISO/IEC 27001
4. **Reducción de MTTR:** Tiempo medio de resolución más bajo gracias a detección temprana
5. **Trazabilidad:** Registro completo de eventos para auditorías e investigaciones
6. **Optimización Continua:** Datos para mejorar rendimiento y confiabilidad
7. **Gobernanza:** Control y monitoreo de sistemas distribuidos

### Para los Usuarios

1. **Interfaz Intuitiva:** Dashboard y vistas organizadas y fáciles de usar
2. **Información Consolidada:** Toda la información de monitoreo en un solo lugar
3. **Alertas Contextuales:** Información completa para tomar decisiones informadas
4. **Búsqueda y Filtros:** Herramientas potentes para encontrar información rápidamente
5. **Visualizaciones:** Gráficos y métricas visuales para análisis rápido

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo para auditorías
2. **Cumplimiento EU AI Act:** Monitoreo post-mercado y detección de anomalías
3. **ISO 42001:** Monitoreo, medición y análisis continuo
4. **ISO/IEC 27001:** Registro de eventos y auditoría de seguridad
5. **Evidencia:** Documentación completa de eventos y respuestas

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Integración con Infraestructura

- Monitoreo de recursos de infraestructura
- Alertas de capacidad y escalado
- Métricas de infraestructura (CPU, memoria, disco, red)

### Integración con Servicios

- Monitoreo de servicios backend
- Métricas de APIs y endpoints
- Alertas de disponibilidad de servicios

### Integración con Modelos

- Monitoreo operacional de rendimiento de modelos (latencia técnica, throughput)
- Métricas técnicas de infraestructura para modelos
- Alertas operacionales de degradación de modelos (tiempos de respuesta, disponibilidad)
- **Nota:** Para análisis de precisión, sesgos y métricas de negocio de modelos, consulta el módulo **Analytics**

### Integración con Proyectos

- Monitoreo de sistemas relacionados con proyectos
- Alertas específicas por proyecto
- Métricas de uso por proyecto

### Integración con Workflows

- Alertas automáticas desde workflows
- Métricas de ejecución de workflows
- Logs de auditoría de workflows

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **Alertas Automáticas**

- Detección automática de condiciones anómalas
- Clasificación automática por severidad
- Notificaciones configurables (email, Slack, etc.)

### 2. **Métricas Técnicas en Tiempo Real**

- Actualización continua de métricas operacionales
- Gráficos de tendencias históricas técnicas
- Comparación con períodos anteriores
- Métricas de infraestructura (CPU, memoria, disco, red)

### 3. **Logs de Auditoría Completos**

- Registro de todos los eventos relevantes
- Trazabilidad completa (quién, qué, cuándo, dónde)
- Retención configurable según políticas

### 4. **Formularios de Respuesta Estructurados**

- Workflows para responder a alertas críticas
- Documentación de decisiones y acciones
- Trazabilidad de respuestas

### 5. **Dashboard Consolidado**

- Vista unificada de todo el sistema
- KPIs principales en un vistazo
- Navegación rápida a detalles

### 6. **Gobernanza Distribuida**

- Monitoreo de sistemas distribuidos
- Gestión de consistencia
- Performance de vistas distribuidas

---

## ❓ PREGUNTAS FRECUENTES

### ¿Cómo se generan las alertas automáticamente?

Las alertas se generan automáticamente cuando el sistema detecta condiciones que requieren atención. El sistema monitorea métricas, logs y eventos, y cuando se cumplen ciertos umbrales o patrones, se genera una alerta con severidad calculada automáticamente.

### ¿Cómo funciona la clasificación de severidad?

La severidad se calcula automáticamente basándose en:
- **HIGH:** Impacto crítico en el sistema o usuarios
- **MEDIUM:** Impacto significativo pero no crítico
- **LOW:** Impacto menor o informativo

### ¿Puedo crear alertas manualmente?

Sí, puedes crear alertas manualmente desde la página de listado de alertas o desde el dashboard. Esto es útil para documentar problemas conocidos o para pruebas.

### ¿Qué información se registra en los logs de auditoría?

Los logs de auditoría registran:
- Tipo de evento
- Usuario que generó el evento
- IP de origen
- Timestamp exacto
- Componente origen
- Datos adicionales del contexto
- Relación con otros eventos

### ¿Cómo veo las tendencias de una métrica técnica?

En la página de detalle de una métrica técnica, encontrarás:
- Gráfico de tendencias históricas operacionales
- Comparación con períodos anteriores
- Análisis de patrones y anomalías técnicas

> **Nota:** Para análisis de tendencias de modelos ML, métricas de negocio y reportes analíticos, consulta el módulo **Analytics**.

### ¿Qué es el formulario de respuesta a alertas?

El formulario de respuesta a alertas es un workflow estructurado para responder a alertas críticas. Permite documentar:
- La decisión tomada
- Notas adicionales sobre el contexto
- Acciones planificadas

### ¿Cómo monitoreo la salud del sistema?

Accede a Monitoring → Distributed → System Health para ver:
- Estado general del sistema (Healthy, Degraded, Critical)
- Estado de cada componente
- Métricas de salud por componente
- Última verificación

### ¿Puedo exportar datos de métricas o alertas?

Actualmente, los datos se pueden visualizar en la interfaz. La funcionalidad de exportación está planificada para futuras versiones.

### ¿Cómo se integra con otros módulos?

El módulo de monitorización se integra automáticamente con:
- **Infraestructura:** Métricas técnicas y alertas de recursos
- **Servicios:** Monitoreo operacional de APIs y endpoints
- **Modelos:** Métricas técnicas de rendimiento (latencia, throughput) - *Para análisis de precisión y sesgos, ver Analytics*
- **Proyectos:** Alertas operacionales y métricas técnicas por proyecto
- **Workflows:** Logs y métricas técnicas de ejecución

### ¿Cuál es la diferencia entre Monitorización y Analytics?

**Monitorización (este módulo):**
- Enfoque: **Operacional y técnico**
- Métricas: Infraestructura, sistemas, servicios (CPU, memoria, latencia, throughput)
- Alertas: Operacionales (sistemas, recursos, disponibilidad)
- Temporalidad: Tiempo real y corto plazo
- Audiencia: DevOps, SREs, System Administrators

**Analytics:**
- Enfoque: **Análisis de negocio y modelos ML**
- Métricas: Modelos ML, precisión, sesgos, impacto en compliance
- Reportes: Analíticos estructurados, tendencias de negocio
- Temporalidad: Análisis histórico y tendencias
- Audiencia: Data Analysts, ML Engineers, Business Analysts

### ¿Qué es la gobernanza distribuida?

La gobernanza distribuida permite gestionar y monitorear sistemas distribuidos, asegurando:
- Consistencia entre componentes
- Monitoreo de performance de vistas
- Gestión de configuración distribuida

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DE CONFIGURAR MONITORIZACIÓN

Una vez que el módulo de monitorización está configurado:

1. ✅ **Monitoreo Continuo:** El sistema monitorea automáticamente todos los componentes operacionales
2. ✅ **Alertas Automáticas:** Se generan alertas operacionales cuando se detectan problemas técnicos
3. ✅ **Análisis de Métricas Técnicas:** Revisión regular de métricas técnicas y tendencias operacionales
4. ✅ **Respuesta a Incidentes:** Uso de formularios y workflows para gestionar respuestas a incidentes operacionales
5. ✅ **Optimización Operacional:** Uso de datos técnicos para mejorar rendimiento y confiabilidad de infraestructura
6. ✅ **Auditorías:** Consulta de logs de auditoría operacionales para cumplimiento y análisis forense

---

**Última Actualización:** Diciembre 2025
**Versión del Módulo:** 1.0
**Estado:** ✅ Operativo y listo para producción
