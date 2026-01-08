# 📘 GUÍA FUNCIONAL - GESTIÓN DE AGENTES DE IA

**Versión:** 2.0
**Fecha:** Enero 2025
**Audiencia:** AI Engineers, Developers, Project Managers, Compliance Officers, DevOps Teams, Governance Managers, Auditors

---

## 🎯 ¿QUÉ ES LA GESTIÓN DE AGENTES?

La **Gestión de Agentes** es un módulo integral de gobierno y cumplimiento que permite registrar, versionar, gestionar, desplegar, monitorear y certificar agentes de Inteligencia Artificial a lo largo de todo su ciclo de vida. El módulo proporciona herramientas completas para el gobierno operativo, cumplimiento normativo (EU AI Act), supervisión humana (HITL), certificación y retiro de agentes.

### 🎯 Propósito Principal

El módulo de gestión de agentes permite:

1. **Registro Centralizado:** Mantener un registro único y centralizado de todos los agentes de IA utilizados en la organización
2. **Control de Versiones:** Gestionar versiones de agentes con versionado semántico automático (MAJOR.MINOR.PATCH)
3. **Gestión de Despliegues:** Administrar despliegues de agentes en diferentes entornos (desarrollo, staging, producción)
4. **Workflow de Aprobación:** Procesos de aprobación y validación antes del despliegue con integración BPMN
5. **Monitoreo Continuo:** Seguimiento de métricas de rendimiento, interacciones y salud del agente
6. **Trazabilidad Completa:** Mantener registro completo del ciclo de vida de cada agente
7. **Cumplimiento Normativo:** Integración completa con módulo de Compliance para EU AI Act (clasificación, FRIA, registro EU, declaración de conformidad)
8. **Certificación de Agentes:** Proceso de certificación antes de producción con verificación de requisitos
9. **Políticas de Retiro:** Gestión del retiro de agentes con detección de dependencias y planes de migración
10. **Supervisión HITL:** Revisión humana de decisiones y aprobación de reversiones automáticas
11. **Reglas de Revisión Configurables:** Sistema flexible de reglas Drools y LLM prompts para determinar cuándo se requiere revisión humana
12. **Dashboard Ejecutivo:** Vista consolidada de salud, cumplimiento y gobernanza de agentes
13. **Gestión de Proyectos:** Asociar agentes a proyectos y rastrear su uso
14. **Telemetría de Interacciones:** Registro automático de interacciones usuario-agente (costos, tokens, duración)

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 10 - Requisitos de Datos y Gobernanza de Datos

Los agentes de alto riesgo deben cumplir con requisitos de calidad de datos, incluyendo:
- Gestión y gobernanza de datos de entrenamiento, validación y prueba
- Documentación de datos de entrenamiento

#### Art. 15 - Requisitos de Transparencia y Provisión de Información

Los agentes deben proporcionar:
- Información adecuada y relevante sobre el sistema
- Instrucciones de uso apropiadas
- Transparencia en las decisiones tomadas

#### Art. 20 - Monitoreo Post-Mercado

Requiere monitoreo continuo del rendimiento de los agentes en producción para detectar anomalías y degradación.

#### Art. 13 - Transparencia y Explicabilidad

Los agentes deben proporcionar explicaciones sobre sus decisiones y procesos, especialmente en sistemas de alto riesgo.

### ISO 42001 - Gestión de Sistemas de IA

#### Cláusula 8.2 - Diseño y Desarrollo

Requiere control de versiones y gestión de cambios en agentes de IA.

#### Cláusula 8.3 - Operación y Monitoreo

Requiere monitoreo continuo y gestión de riesgos durante la operación de agentes.

---

## 🚀 ¿PARA QUÉ SIRVE LA GESTIÓN DE AGENTES?

### 1. **Cumplimiento Normativo**

El módulo facilita el cumplimiento de:
- **EU AI Act Art. 10, 13, 15, 20:** Gestión de datos, transparencia, explicabilidad y monitoreo post-mercado
- **ISO 42001 8.2, 8.3:** Control de versiones, gestión de cambios y monitoreo operacional
- **Trazabilidad Regulatoria:** Registro completo de agentes para auditorías

### 2. **Gestión del Ciclo de Vida**

Proporciona herramientas para:
- **Registro Inicial:** Registrar nuevos agentes con información completa
- **Versionado Automático:** Control de versiones con semántica automática
- **Seguimiento de Cambios:** Historial completo de modificaciones
- **Gestión de Despliegues:** Información sobre dónde está desplegado cada agente
- **Rollback:** Capacidad de revertir a versiones anteriores si es necesario

### 3. **Monitoreo de Rendimiento**

Facilita:
- **Métricas en Tiempo Real:** KPIs de rendimiento, latencia, tasa de éxito
- **Análisis de Tendencias:** Gráficos de evolución de métricas
- **Detección de Degradación:** Identificar problemas de rendimiento tempranamente
- **Health Checks:** Monitoreo de salud y disponibilidad del agente

### 4. **Gobernanza y Aprobación**

Permite:
- **Workflow de Aprobación:** Procesos estructurados antes del despliegue
- **Control de Acceso:** Gestión de quién puede aprobar y desplegar agentes
- **Auditoría:** Registro completo de aprobaciones y decisiones
- **Compliance Checks:** Validaciones automáticas de cumplimiento

### 5. **Gestión de Proyectos**

Facilita:
- **Asociación con Proyectos:** Vincular agentes a proyectos específicos
- **Seguimiento de Uso:** Monitorear dónde y cómo se utilizan los agentes
- **Análisis de Reutilización:** Identificar agentes más utilizados
- **Gestión de Costes:** Asociar costes de agentes a proyectos

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Registro de Agentes (Registry)**

Pantalla principal para gestionar el catálogo de agentes:

#### **Listado de Agentes**
- **Vista Tabular:** Tabla paginada con todos los agentes registrados
- **Métricas Principales:**
  - Total de agentes registrados
  - Agentes activos
  - Agentes inactivos
  - Agentes pendientes de aprobación
- **Filtros Avanzados:**
  - Búsqueda por nombre, UUID o descripción
  - Filtro por dominio funcional
  - Filtro por estado (ACTIVE, INACTIVE, PENDING)
- **Acciones:**
  - Crear nuevo agente
  - Ver detalles del agente
  - Editar agente
  - Eliminar agente

#### **Página de Detalle del Agente**

Página completa con 9 pestañas para gestión detallada:

**1. Información General:**
- Formulario editable para crear/editar agente
- Campos: nombre, descripción, dominio funcional, tipo, estado, versión
- **ID del agente (UUID):** Generado automáticamente, campo de solo lectura
- **Versión Semántica:** Calculada automáticamente, campo de solo lectura
- **Capacidades:** Lista de capacidades del agente
- **Configuración:** Configuración JSON del agente
- **Metadata:** Metadatos adicionales en formato JSON

**2. Versiones:**
- Gestión completa de versiones del agente
- Crear, editar, eliminar versiones
- Formulario inline para nuevas versiones
- **Versionado Semántico Automático:** Incrementa MAJOR.MINOR.PATCH automáticamente
- Historial completo de versiones con fecha de creación y creador

**3. Monitoreo:**
- **KPIs Principales:**
  - Total de Interacciones
  - Tasa de Éxito
  - Tiempo de Respuesta Promedio
  - Uptime (Disponibilidad)
- **Gráfico de Tendencias:** Visualización de evolución de métricas
- **Análisis de Rendimiento:** Comparación entre períodos

**4. Despliegues:**
- **Lista de Despliegues:**
  - Nombre del despliegue
  - Estado (ACTIVE, PENDING, INACTIVE)
  - Ambiente (development, staging, production)
  - Fecha de despliegue
  - Endpoint (URL del agente desplegado)
- **Información Detallada:** Configuración y estado de cada despliegue

**5. Proyectos:**
- Tabla con proyectos que utilizan el agente
- Información: nombre del proyecto, tipo de uso (PRIMARY, SECONDARY, REFERENCE, TESTING), rol del agente, último uso
- **Tipo de Uso:**
  - **PRIMARY:** Agente principal del proyecto
  - **SECONDARY:** Agente secundario o de apoyo
  - **REFERENCE:** Agente de referencia
  - **TESTING:** Agente usado solo para pruebas
- Modal para ver detalles completos del proyecto
- Navegación directa al proyecto asociado

**6. Cumplimiento:**
- **Puntuación General de Cumplimiento:** Puntuación consolidada de cumplimiento AI Act
- **Clasificación de Riesgo:**
  - Estado de clasificación (CLASSIFIED, NOT_CLASSIFIED)
  - Categoría de riesgo (MINIMAL_RISK, LIMITED_RISK, HIGH_RISK, PROHIBITED)
  - Categorías Anexo III aplicables
  - Última actualización
  - Enlace directo a clasificación completa en módulo Compliance
- **Evaluación FRIA (Fundamental Rights Impact Assessment):**
  - Estado de evaluación (COMPLETED, IN_PROGRESS, PENDING)
  - ID de evaluación
  - Nivel de riesgo identificado
  - Última evaluación realizada
  - Enlace directo a evaluación FRIA completa
- **Registro EU AI Act:**
  - Estado de registro (REGISTERED, NOT_REGISTERED)
  - ID de registro EU
  - Fecha de registro y expiración
  - Enlace directo a registro EU completo
- **Certificación:**
  - Estado de certificación (CERTIFIED, PENDING, NOT_CERTIFIED, EXPIRED, REVOKED)
  - ID de certificado
  - Organismo certificador
  - Fechas de certificación y expiración
  - Enlace a gestión de certificación
- **Declaración de Conformidad:**
  - Estado de declaración (DECLARED, NOT_DECLARED)
  - ID de declaración
  - Fecha de declaración
  - Enlace directo a declaración de conformidad

**7. Reglas de Revisión:**
- **Configuración de Reglas para Revisión HITL:**
  - Selección de reglas Drools para evaluación automática
  - Selección de prompts LLM para razonamiento complejo
  - Visualización de reglas/prompts disponibles por categoría
  - Búsqueda y filtrado de reglas/prompts
  - Reordenamiento mediante drag-and-drop
  - Configuración de prioridad y orden de ejecución
  - Visualización de estado de cumplimiento de prompts LLM
  - Integración con módulo de Prompts para cumplimiento y versionado

**8. Certificación:**
- **Estado de Certificación:**
  - Estado actual (CERTIFIED, PENDING, NOT_CERTIFIED, EXPIRED, REVOKED)
  - ID de certificado
  - Fecha de certificación y expiración
  - Organismo certificador
  - Usuario que certificó
- **Verificación de Requisitos:**
  - Clasificación de riesgo (✓/✗)
  - Evaluación FRIA (✓/✗)
  - Registro EU (✓/✗)
  - Declaración de conformidad (✓/✗)
  - Documentación técnica (✓/✗)
  - Sistema de Gestión de Calidad QMS (✓/✗)
- **Información de Certificación:**
  - Organismo certificador (editable)
  - Fecha de expiración (editable)
  - Notas adicionales (editable)
- **Proceso de Certificación:**
  - Botón "Solicitar Certificación" (disponible cuando todos los requisitos están cumplidos)
  - Lanza proceso BPMN para aprobación
  - Estado cambia a PENDING durante el proceso

**9. Retiro:**
- **Estado de Retiro:**
  - Estado actual (ACTIVE, PENDING_APPROVAL, APPROVED, RETIRED, CANCELLED)
  - Información de retiro pendiente o completado
- **Detección de Dependencias:**
  - Número de proyectos asociados
  - Número de despliegues activos
  - Número de integraciones
  - Alerta si hay dependencias activas
- **Solicitud de Retiro:**
  - Tipo de retiro (PLANNED, IMMEDIATE, GRACEFUL, REPLACEMENT)
  - Fecha de retiro propuesta
  - Razón del retiro (obligatorio)
  - Plan de migración (obligatorio si hay dependencias)
- **Proceso de Aprobación:**
  - Lanza proceso BPMN para aprobación de retiro
  - Requiere plan de migración detallado si hay dependencias
  - Información de aprobación (aprobador, fecha)
- **Criterios de Retiro:**
  - Guía de cuándo retirar un agente
  - Checklist de acciones previas al retiro

### 2. **Dashboard Ejecutivo de Gobierno**

Pantalla consolidada para visión ejecutiva del gobierno de agentes:

#### **KPIs Principales**
- **Total de Agentes:** Número total de agentes registrados
- **Puntuación de Cumplimiento:** Puntuación general de cumplimiento AI Act (%)
- **Aprobaciones Pendientes:** Solicitudes de aprobación que requieren atención
- **Alertas Críticas:** Alertas que requieren acción inmediata

#### **Salud de Agentes**
- Lista de agentes con información consolidada:
  - Nombre del agente
  - Puntuación de salud (0-100%)
  - Estado operativo (ACTIVE, PENDING, INACTIVE)
  - Estado de cumplimiento (COMPLIANT, PENDING, NON_COMPLIANT)
  - Estado de certificación (CERTIFIED, PENDING, NOT_CERTIFIED)
  - Número de alertas activas
  - Interacciones en últimas 24 horas
  - Última auditoría realizada
- Navegación directa al detalle de cada agente

#### **Actividad Reciente**
- Lista de actividades recientes:
  - Tipo de actividad (APPROVAL, ALERT, CERTIFICATION, COMPLIANCE)
  - Nombre del agente
  - Descripción de la actividad
  - Estado
  - Timestamp
- Iconos diferenciados por tipo de actividad

#### **Métricas Adicionales**
- **Requieren Revisión HITL:** Agentes con decisiones pendientes de revisión humana
- **Total Interacciones:** Número total de interacciones registradas
- **Tiempo Respuesta Promedio:** Tiempo promedio de respuesta de agentes

### 3. **Gestión de Despliegues (Deployment)**

Pantalla para administrar despliegues de agentes:

#### **Listado de Despliegues**
- **Vista Tabular:** Tabla paginada con todos los despliegues
- **Métricas Principales:**
  - Total de despliegues
  - Despliegues activos
  - Despliegues pendientes
  - Despliegues inactivos
- **Filtros Avanzados:**
  - Búsqueda por nombre de agente, UUID o tipo de despliegue
  - Filtro por estado
  - Filtro por ambiente
- **Acciones:**
  - Crear nuevo despliegue
  - Ver detalles del despliegue
  - Editar despliegue
  - Eliminar despliegue

#### **Página de Detalle del Despliegue**
- **Información General:**
  - Nombre del despliegue
  - Agente asociado (select con lista de agentes disponibles)
  - Tipo de despliegue (PRODUCTION, STAGING, DEVELOPMENT)
  - Estado (ACTIVE, PENDING, INACTIVE)
  - Ambiente
  - Endpoint (URL del agente)
  - UUID del despliegue (generado automáticamente)
- **Configuración:**
  - Parámetros de configuración
  - Variables de entorno
  - Recursos asignados

### 4. **Aprobación de Agentes (Approval)**

Pantalla para gestionar aprobaciones de agentes:

#### **Listado de Aprobaciones**
- **Vista Tabular:** Tabla paginada con todas las aprobaciones
- **Métricas Principales:**
  - Total de aprobaciones
  - Aprobaciones activas
  - Aprobaciones pendientes
- **Filtros Avanzados:**
  - Búsqueda por razón de solicitud
  - Filtro por tipo de aprobación
  - Filtro por estado
- **Acciones:**
  - Crear nueva solicitud de aprobación
  - Ver detalles de la aprobación
  - Eliminar aprobación

#### **Página de Creación de Aprobación**
- **Información de la Solicitud:**
  - Tipo de aprobación
  - Razón de la solicitud
  - Detalles de la solicitud
  - Criterios de aprobación
- **Evaluación:**
  - Evaluación de riesgos
  - Verificación de cumplimiento
  - Revisión técnica
  - Revisión ética
- **Decisión:**
  - Notas de aprobación
  - Razón de rechazo (si aplica)
  - Condiciones (si aplica)
  - Nivel de aprobación
  - Aprobador

### 5. **Módulos de Gobernanza Adicionales**

El módulo incluye funcionalidades adicionales de gobernanza:

#### **Compliance (Cumplimiento)**
- Gestión de cumplimiento normativo
- Verificación de requisitos regulatorios
- Auditorías y reportes

#### **Ethics (Ética)**
- Evaluaciones éticas de agentes
- Análisis de impacto ético
- Guías y políticas éticas

#### **Bias Detection (Detección de Sesgos)**
- Detección automática de sesgos
- Análisis de equidad
- Reportes de sesgos identificados

#### **Transparency (Transparencia)**
- Documentación de decisiones del agente
- Explicabilidad de procesos
- Registro de transparencia

#### **Decisions (Decisiones) - HITL Review**
- **Vista de Listado:**
  - Estadísticas: Requieren Revisión, Revisadas, Baja Confianza
  - Filtros: Requieren Revisión, Baja Confianza, Alto Riesgo
  - Tabla con: Agente, Tipo de Decisión, Razón, Confianza, Estado HITL, Revisado por, Fecha
  - Las decisiones llegan automáticamente por telemetría (no se crean manualmente)
- **Pantalla de Revisión HITL:**
  - Detalles de la decisión del agente (tipo, confianza, razón, input/output)
  - Alerta si la confianza es baja
  - Notificación de cumplimiento AI Act
  - Sección de revisión humana con comentarios obligatorios
  - Acciones: Aprobar, Rechazar, Escalar (integración con BPMN)
  - Información de revisión previa si ya fue revisada
  - Sección de auditoría

#### **Rollback (Reversión) - HITL Approval**
- **Vista de Listado:**
  - Estadísticas: Pendientes Aprobación, Aprobadas, Completadas
  - Tabla con: Agente, Tipo de Reversión, Estado, Versión Origen/Destino, Razón, Trigger, Aprobado por, Fecha
  - Las reversiones son automáticas (no se crean manualmente)
- **Pantalla de Aprobación HITL:**
  - Detalles de la reversión (agente, tipo, versiones, razón, evento trigger)
  - Sección de aprobación con comentarios obligatorios
  - Acciones: Aprobar Reversión, Rechazar Reversión, Escalar (integración con BPMN)
  - Información de aprobación previa si ya fue aprobada
  - Sección de auditoría

#### **Alerts (Alertas)**
- **Vista de Listado:**
  - Estadísticas: Total, Disparadas, Reconocidas, Resueltas
  - Tabla con: Agente, Título, Tipo, Categoría, Severidad, Estado, Prioridad, Impacto, Urgencia, Fecha
  - Codificación por colores según severidad y estado
  - Las alertas se generan automáticamente (no se crean manualmente)
- **Pantalla de Detalle:**
  - Información completa de la alerta
  - Acciones: Reconocer Alerta, Resolver Alerta (con notas obligatorias)
  - Historial de reconocimiento y resolución
  - Sección de auditoría

#### **Interactions (Interacciones) - Telemetría Usuario-Agente**
- **Vista de Listado:**
  - Estadísticas: Total, Interacciones Exitosas, Interacciones Fallidas, Costo Total
  - Tabla con: Agente, Usuario, Input (truncado), Output (truncado), Duración, Tokens, Costo, Fecha
  - Las interacciones se registran automáticamente por telemetría (no se crean manualmente)
- **Pantalla de Detalle:**
  - Información general: Agente, Usuario, Sesión, Fecha
  - Métricas: Duración, Tokens Utilizados, Costo
  - Input y Output completos (formateados si es JSON)
  - Solo visualización (las interacciones son registros de telemetría)

#### **Monitoring (Monitoreo)**
- Dashboard de monitoreo
- Health overview
- Métricas en tiempo real

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Registro de un Nuevo Agente

1. **Acceso al Registro**
   - Navegar a Agents → Registry
   - Hacer clic en "Registrar Agente"

2. **Completar Información Básica**
   - Completar formulario con:
     - Nombre del agente
     - Descripción del agente
     - Dominio funcional (Finance, Security, Document Processing, etc.)
     - Tipo de agente
     - Estado inicial
   - **ID (UUID) y Versión se generan automáticamente**

3. **Configurar Capacidades (Opcional)**
   - Agregar capacidades del agente
   - Configurar parámetros del agente
   - Agregar metadatos adicionales

4. **Guardar Agente**
   - El sistema genera automáticamente:
     - UUID único para el agente
     - Versión inicial (1.0.0)
   - Se dispara automáticamente el workflow de aprobación si está configurado

5. **Navegación a Detalle**
   - Se redirige automáticamente a la página de detalle
   - Puede completar información adicional en otras pestañas

### Flujo 2: Gestión de Versiones

1. **Acceder a Pestaña Versiones**
   - Desde la página de detalle del agente
   - Seleccionar pestaña "Versiones"

2. **Crear Nueva Versión**
   - Hacer clic en "Agregar Versión"
   - Completar descripción de los cambios
   - Seleccionar estado de la versión
   - **La versión se calcula automáticamente:**
     - Si es corrección de errores → incrementa PATCH (1.0.0 → 1.0.1)
     - Si es nueva funcionalidad → incrementa MINOR (1.0.0 → 1.1.0)
     - Si es cambio incompatible → incrementa MAJOR (1.0.0 → 2.0.0)

3. **Gestionar Versiones Existentes**
   - Ver lista de todas las versiones
   - Editar descripción y estado de versiones
   - Eliminar versiones (con validaciones)

### Flujo 3: Creación de Despliegue

1. **Acceder a Despliegues**
   - Navegar a Agents → Deployment → Overview
   - Hacer clic en "Crear Despliegue"

2. **Seleccionar Agente**
   - Seleccionar agente del dropdown (lista de agentes disponibles)
   - El sistema carga información del agente seleccionado

3. **Configurar Despliegue**
   - Completar información:
     - Nombre del despliegue
     - Tipo de despliegue (PRODUCTION, STAGING, DEVELOPMENT)
     - Estado inicial
     - Ambiente
     - Endpoint (URL del agente)
   - **UUID se genera automáticamente**

4. **Guardar Despliegue**
   - El sistema valida la configuración
   - Se crea el despliegue
   - Se redirige a la página de detalle del despliegue

### Flujo 4: Proceso de Aprobación

1. **Crear Solicitud de Aprobación**
   - Navegar a Agents → Approval → Overview
   - Hacer clic en "Registrar Aprobación"

2. **Completar Información**
   - Tipo de aprobación
   - Razón de la solicitud
   - Detalles de la solicitud
   - Criterios de aprobación

3. **Evaluación**
   - El sistema ejecuta evaluaciones automáticas:
     - Evaluación de riesgos
     - Verificación de cumplimiento
     - Revisión técnica
     - Revisión ética

4. **Revisión Humana**
   - Revisores asignados revisan la solicitud
   - Se agregan notas y comentarios

5. **Decisión Final**
   - Aprobación o rechazo
   - Si es aprobado, se pueden agregar condiciones
   - Se registra el aprobador y fecha

### Flujo 5: Monitoreo de Agente

1. **Acceder a Monitoreo**
   - Desde la página de detalle del agente
   - Seleccionar pestaña "Monitoreo"

2. **Revisar Métricas**
   - Total de Interacciones
   - Tasa de Éxito
   - Tiempo de Respuesta Promedio
   - Uptime

3. **Analizar Tendencias**
   - Revisar gráfico de evolución de métricas
   - Identificar patrones y anomalías
   - Comparar períodos

4. **Tomar Acciones**
   - Si se detectan problemas, crear alertas
   - Revisar despliegues activos
   - Considerar rollback si es necesario

### Flujo 6: Certificación de Agente

1. **Verificar Requisitos**
   - Desde la página de detalle del agente
   - Seleccionar pestaña "Certificación"
   - Revisar que todos los requisitos estén cumplidos:
     - Clasificación de riesgo
     - Evaluación FRIA
     - Registro EU
     - Declaración de conformidad
     - Documentación técnica
     - QMS

2. **Solicitar Certificación**
   - Si todos los requisitos están cumplidos, aparece botón "Solicitar Certificación"
   - Hacer clic en el botón
   - Se lanza proceso BPMN para aprobación
   - Estado cambia a PENDING

3. **Proceso de Aprobación**
   - Revisores asignados revisan la solicitud
   - Se puede editar información de certificación (organismo, fecha expiración, notas)
   - Una vez aprobado, estado cambia a CERTIFIED
   - Se genera ID de certificado

### Flujo 7: Retiro de Agente

1. **Revisar Dependencias**
   - Desde la página de detalle del agente
   - Seleccionar pestaña "Retiro"
   - Revisar dependencias activas:
     - Proyectos asociados
     - Despliegues activos
     - Integraciones

2. **Crear Solicitud de Retiro**
   - Hacer clic en "Iniciar Proceso de Retiro"
   - Completar formulario:
     - Tipo de retiro (PLANNED, IMMEDIATE, GRACEFUL, REPLACEMENT)
     - Fecha de retiro propuesta
     - Razón del retiro (obligatorio)
     - Plan de migración (obligatorio si hay dependencias)

3. **Proceso de Aprobación**
   - Se lanza proceso BPMN para aprobación
   - Estado cambia a PENDING_APPROVAL
   - Revisores revisan la solicitud y plan de migración
   - Una vez aprobado, estado cambia a APPROVED y luego RETIRED

### Flujo 8: Configuración de Reglas de Revisión

1. **Acceder a Reglas de Revisión**
   - Desde la página de detalle del agente
   - Seleccionar pestaña "Reglas de Revisión"

2. **Seleccionar Reglas Drools**
   - Ver reglas disponibles por categoría
   - Buscar y filtrar reglas
   - Seleccionar reglas para evaluación automática

3. **Seleccionar Prompts LLM**
   - Ver prompts disponibles (categoría DECISION_REVIEW)
   - Verificar estado de cumplimiento y versión
   - Seleccionar prompts para razonamiento complejo

4. **Configurar Orden y Prioridad**
   - Reordenar reglas seleccionadas mediante drag-and-drop
   - Configurar prioridad de ejecución
   - Guardar configuración

### Flujo 9: Revisión HITL de Decisiones

1. **Acceder a Decisiones**
   - Navegar a Agents → Decisions → Overview
   - Ver lista de decisiones que requieren revisión

2. **Filtrar Decisiones**
   - Usar filtros: Requieren Revisión, Baja Confianza, Alto Riesgo
   - Ver estadísticas de decisiones pendientes

3. **Revisar Decisión**
   - Hacer clic en "Ver" para abrir pantalla de revisión
   - Revisar detalles de la decisión del agente
   - Ver alerta si la confianza es baja
   - Revisar notificación de cumplimiento AI Act

4. **Tomar Decisión HITL**
   - Agregar comentarios obligatorios
   - Seleccionar acción: Aprobar, Rechazar, Escalar
   - Se integra con BPMN para workflow
   - Se registra revisión para auditoría

### Flujo 10: Aprobación HITL de Reversiones

1. **Acceder a Reversiones**
   - Navegar a Agents → Rollback → Overview
   - Ver lista de reversiones pendientes de aprobación

2. **Revisar Reversión**
   - Hacer clic en "Ver" para abrir pantalla de aprobación
   - Revisar detalles de la reversión (agente, versiones, razón, trigger)

3. **Aprobar o Rechazar**
   - Agregar comentarios obligatorios
   - Seleccionar acción: Aprobar Reversión, Rechazar Reversión, Escalar
   - Se integra con BPMN para workflow
   - Se registra aprobación para auditoría

### Flujo 11: Gestión de Alertas

1. **Acceder a Alertas**
   - Navegar a Agents → Alerts → Overview
   - Ver lista de alertas con estadísticas

2. **Revisar Alerta**
   - Hacer clic en "Ver" para abrir detalle
   - Revisar información completa de la alerta

3. **Gestionar Alerta**
   - Reconocer alerta (con notas obligatorias)
   - Resolver alerta (con notas obligatorias)
   - Ver historial de reconocimiento y resolución

### Flujo 12: Visualización de Interacciones

1. **Acceder a Interacciones**
   - Navegar a Agents → Interactions → Overview
   - Ver estadísticas: Total, Exitosas, Fallidas, Costo Total

2. **Revisar Interacción**
   - Hacer clic en "Ver" para abrir detalle
   - Ver información completa: agente, usuario, sesión, métricas, input/output
   - Las interacciones son solo de visualización (registros de telemetría)

### Flujo 13: Dashboard Ejecutivo

1. **Acceder al Dashboard**
   - Navegar a Agents → Dashboard
   - Ver KPIs consolidados de gobierno

2. **Revisar Salud de Agentes**
   - Ver lista de agentes con puntuación de salud
   - Hacer clic en agente para ver detalle completo

3. **Revisar Actividad Reciente**
   - Ver actividades recientes (aprobaciones, alertas, certificaciones, compliance)
   - Navegar a secciones específicas desde enlaces

### Flujo 14: Asociación con Proyectos

1. **Ver Proyectos Asociados**
   - Desde la página de detalle del agente
   - Seleccionar pestaña "Proyectos"

2. **Revisar Información**
   - Lista de proyectos que usan el agente
   - Tipo de uso en cada proyecto
   - Rol del agente en el proyecto
   - Último uso

3. **Navegar al Proyecto**
   - Hacer clic en el botón "Ver" para ver detalles del proyecto
   - Se abre modal con información completa
   - Opción de navegar al proyecto completo

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **AI Engineers** 👨‍💻
- **Responsabilidad:** Desarrollo y gestión de agentes
- **Uso:** Registrar agentes, gestionar versiones, monitorear métricas, crear despliegues
- **Beneficio:** Control centralizado de agentes, versionado automático, seguimiento de rendimiento

#### 2. **Developers** 💻
- **Responsabilidad:** Desarrollo e integración de agentes
- **Uso:** Registrar agentes, gestionar versiones, crear despliegues
- **Beneficio:** Trazabilidad de desarrollo, gestión de versiones estructurada

#### 3. **DevOps Engineers** ⚙️
- **Responsabilidad:** Despliegue y operación de agentes
- **Uso:** Gestionar despliegues, monitorear salud, revisar métricas operacionales
- **Beneficio:** Visibilidad de infraestructura, gestión de despliegues centralizada

#### 4. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo
- **Uso:** Revisar agentes registrados, verificar cumplimiento, auditorías
- **Beneficio:** Cumplimiento EU AI Act, trazabilidad regulatoria

#### 5. **Governance Managers** 🏛️
- **Responsabilidad:** Gobernanza de agentes de IA
- **Uso:** Gestionar aprobaciones, revisar compliance, supervisar ética y transparencia
- **Beneficio:** Control de gobernanza, procesos estructurados de aprobación

#### 6. **Project Managers** 📋
- **Responsabilidad:** Gestión de proyectos
- **Uso:** Consultar agentes asociados a proyectos, monitorear uso
- **Beneficio:** Visibilidad de uso de agentes por proyecto, gestión de recursos

#### 7. **Auditors** 🔍
- **Responsabilidad:** Auditoría y verificación
- **Uso:** Revisar registros, verificar trazabilidad, analizar decisiones
- **Beneficio:** Acceso completo a información para auditorías

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Centralización:** Registro único de todos los agentes utilizados
2. **Trazabilidad:** Historial completo del ciclo de vida de cada agente
3. **Cumplimiento Normativo:** Integración completa con módulo Compliance para EU AI Act e ISO 42001
4. **Control de Versiones:** Versionado semántico automático y gestión estructurada
5. **Automatización:** Integración con workflows de aprobación BPMN
6. **Gobernanza:** Control centralizado de despliegues, aprobaciones, certificaciones y retiros
7. **Monitoreo:** Visibilidad completa del rendimiento y salud de agentes
8. **Supervisión HITL:** Revisión humana de decisiones y aprobación de reversiones
9. **Certificación:** Proceso estructurado de certificación antes de producción
10. **Gestión de Proyectos:** Asociación clara entre agentes y proyectos
11. **Dashboard Ejecutivo:** Vista consolidada de gobierno y cumplimiento
12. **Telemetría:** Registro automático de interacciones usuario-agente con métricas de costo

### Para los Usuarios

1. **Facilidad de Uso:** Interfaz intuitiva y organizada con tabs
2. **Automatización:** UUID y versiones se generan automáticamente
3. **Visibilidad:** Información completa en un solo lugar (9 tabs en detalle de agente)
4. **Eficiencia:** Búsquedas y filtros avanzados
5. **Documentación:** Información estructurada y completa
6. **Monitoreo:** Métricas en tiempo real y análisis de tendencias
7. **Dashboard Ejecutivo:** Vista consolidada de gobierno y cumplimiento
8. **Integración Compliance:** Enlaces directos a módulo de Compliance
9. **Configuración Flexible:** Reglas de revisión configurables con drag-and-drop
10. **Telemetría Automática:** Registro automático de interacciones sin intervención manual

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo para auditorías
2. **Cumplimiento EU AI Act:**
   - Integración completa con clasificación de riesgo (Anexo III)
   - Evaluación FRIA (Fundamental Rights Impact Assessment)
   - Registro EU AI Act
   - Declaración de conformidad
   - Certificación de agentes
   - Supervisión HITL para sistemas de alto riesgo
3. **ISO 42001:** Control de versiones, gestión de cambios y monitoreo operacional
4. **Evidencia:** Documentación completa de agentes, versiones, certificaciones y retiros
5. **Auditoría:** Registro completo de aprobaciones, decisiones, revisiones HITL y retiros
6. **Integración Compliance:** Enlaces directos entre módulo Agents y módulo Compliance
7. **Puntuación de Cumplimiento:** Métrica consolidada de cumplimiento AI Act por agente

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Workflow de Aprobación de Agentes

Cuando se registra un nuevo agente o se crea un despliegue, se puede disparar automáticamente un workflow de aprobación que incluye:

1. **Validaciones Automáticas:**
   - Performance Validation
   - Bias Detection
   - Compliance Check
   - Security Check

2. **Revisiones Humanas:**
   - AI Engineer Review
   - Governance Review
   - Compliance Review

3. **Decisión Final:**
   - Aprobación mediante reglas de negocio
   - Estados: APPROVED, CONDITIONAL_APPROVAL, REJECTED

### Integración con Proyectos

- Los agentes se pueden asociar a proyectos mediante la tabla `PRJAGENTS`
- Seguimiento de uso de agentes por proyecto
- Tipos de uso: PRIMARY, SECONDARY, REFERENCE, TESTING
- Análisis de reutilización de agentes

### Integración con Telemetría

- Métricas de rendimiento en tiempo real
- Monitoreo de uso y consumo
- Alertas de degradación
- Health checks automáticos

### Integración con Deployment

- Información de despliegues en diferentes ambientes
- Endpoints y configuraciones
- Estado de despliegues
- Gestión de versiones desplegadas

### Integración con Compliance

- **Integración Completa con Módulo de Compliance:**
  - Clasificación de riesgo (Anexo III) - Enlace directo a clasificación completa
  - Evaluación FRIA (Fundamental Rights Impact Assessment) - Enlace directo a evaluación
  - Registro EU AI Act - Enlace directo a registro EU
  - Declaración de Conformidad - Enlace directo a declaración
  - Puntuación general de cumplimiento consolidada
- **Verificación Automática:**
  - Verificación automática de requisitos de certificación
  - Validación de cumplimiento antes de despliegue
  - Auditorías programadas
  - Reportes de cumplimiento
- **Trazabilidad Regulatoria:**
  - Registro completo de estado de cumplimiento por agente
  - Historial de evaluaciones y certificaciones
  - Enlaces directos a documentación de compliance

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **UUID Automático**
- Cada agente recibe un UUID único generado automáticamente
- El UUID es inmutable y se usa para identificación única
- Campo de solo lectura
- También se genera UUID para despliegues

### 2. **Versionado Semántico Automático**
- Las versiones siguen el estándar semántico: MAJOR.MINOR.PATCH
- Se calculan automáticamente según el tipo de cambio
- No requiere entrada manual del usuario
- Historial completo de versiones

### 3. **Gestión de Proyectos con Tipos de Uso**
- Los agentes se pueden asociar a proyectos con diferentes tipos de uso:
  - **PRIMARY:** Agente principal del proyecto
  - **SECONDARY:** Agente secundario o de apoyo
  - **REFERENCE:** Agente de referencia
  - **TESTING:** Agente usado solo para pruebas
- Permite análisis de reutilización y gestión de costes

### 4. **Monitoreo en Tiempo Real**
- KPIs de rendimiento actualizados
- Gráficos de tendencias
- Comparación entre períodos
- Detección de anomalías

### 5. **Gestión de Despliegues**
- Múltiples despliegues por agente
- Diferentes ambientes (development, staging, production)
- Endpoints configurables
- Estado y configuración de cada despliegue

### 6. **Workflow de Aprobación**
- Procesos estructurados de aprobación
- Evaluaciones automáticas y humanas
- Registro completo de decisiones
- Condiciones y niveles de aprobación
- Integración con BPMN para workflows automatizados
- Estados: DRAFT (borrador), PENDING (pendiente aprobación), APPROVED, REJECTED

### 7. **Supervisión Humana (HITL)**
- Revisión humana de decisiones del agente
- Aprobación HITL de reversiones automáticas
- Configuración de reglas para determinar cuándo se requiere revisión
- Integración con BPMN para workflows de aprobación
- Cumplimiento AI Act para sistemas de alto riesgo

### 8. **Certificación de Agentes**
- Proceso de certificación antes de producción
- Verificación automática de requisitos (clasificación, FRIA, registro EU, declaración, documentación técnica, QMS)
- Gestión de certificados con fechas de expiración
- Integración con proceso BPMN para aprobación
- Estados: CERTIFIED, PENDING, NOT_CERTIFIED, EXPIRED, REVOKED

### 9. **Políticas de Retiro**
- Gestión del ciclo de vida completo incluyendo retiro
- Detección automática de dependencias (proyectos, despliegues, integraciones)
- Tipos de retiro: PLANIFICADO, INMEDIATO, GRADUAL, REEMPLAZO
- Plan de migración obligatorio cuando hay dependencias
- Proceso de aprobación mediante BPMN
- Criterios y guías de retiro documentadas

### 10. **Reglas de Revisión Configurables**
- Sistema flexible de reglas para determinar cuándo se requiere revisión HITL
- Integración con reglas Drools para evaluación automática
- Integración con prompts LLM para razonamiento complejo
- Visualización de reglas/prompts disponibles por categoría
- Reordenamiento mediante drag-and-drop
- Configuración de prioridad y orden de ejecución
- Integración con módulo de Prompts para cumplimiento y versionado de prompts LLM

---

## ❓ PREGUNTAS FRECUENTES

### ¿Cómo se genera el UUID del agente?

El UUID se genera automáticamente cuando se crea un nuevo agente por el sistema backend. Es un identificador único inmutable que no puede ser modificado y se asigna antes de persistir la entidad en la base de datos.

### ¿Cómo funciona el versionado semántico automático?

El sistema calcula automáticamente la siguiente versión basándose en el tipo de cambio:
- **PATCH** (1.0.0 → 1.0.1): Correcciones de errores
- **MINOR** (1.0.0 → 1.1.0): Nuevas funcionalidades compatibles
- **MAJOR** (1.0.0 → 2.0.0): Cambios incompatibles

### ¿Puedo editar el UUID o la versión del agente?

No, tanto el UUID como la versión son campos calculados automáticamente y de solo lectura para garantizar la integridad y trazabilidad del sistema.

### ¿Qué pasa cuando registro un nuevo agente?

Al registrar un nuevo agente:
1. Se genera automáticamente el UUID y versión inicial (1.0.0)
2. Se puede disparar automáticamente el workflow de aprobación si está configurado
3. El agente queda disponible para ser asociado a proyectos y desplegado

### ¿Cómo gestiono los despliegues de agentes?

1. Navega a Agents → Deployment → Overview
2. Haz clic en "Crear Despliegue"
3. Selecciona el agente del dropdown
4. Completa la información del despliegue (tipo, ambiente, endpoint)
5. Guarda el despliegue

### ¿Puedo tener múltiples despliegues del mismo agente?

Sí, puedes tener múltiples despliegues del mismo agente en diferentes ambientes (development, staging, production) o con diferentes configuraciones.

### ¿Cómo veo qué proyectos usan un agente?

En la página de detalle del agente, accede a la pestaña "Proyectos". Ahí verás:
- Lista de proyectos que usan el agente
- Tipo de uso en cada proyecto (PRIMARY, SECONDARY, REFERENCE, TESTING)
- Rol del agente en el proyecto
- Último uso

### ¿Cómo monitoreo el rendimiento de un agente?

En la página de detalle del agente, accede a la pestaña "Monitoreo". Verás:
- KPIs principales (Total Interacciones, Tasa de Éxito, Tiempo de Respuesta, Uptime)
- Gráfico de tendencias de métricas
- Análisis de evolución del rendimiento

### ¿Qué métricas están disponibles?

En la pestaña "Monitoreo" del agente verás:
- Total de Interacciones
- Tasa de Éxito
- Tiempo de Respuesta Promedio
- Uptime (Disponibilidad)
- Gráfico de tendencias de estas métricas

### ¿Cómo filtro agentes por dominio o estado?

En el listado de agentes, utiliza los filtros superiores:
- Búsqueda por nombre, UUID o descripción
- Filtro por dominio funcional
- Filtro por estado (ACTIVE, INACTIVE, PENDING)

### ¿Cómo funciona el proceso de aprobación?

1. Crea una solicitud de aprobación en Agents → Approval
2. El sistema ejecuta evaluaciones automáticas (riesgos, cumplimiento, técnica, ética)
3. Los revisores asignados revisan la solicitud
4. Se toma una decisión final (aprobado, condicional, rechazado)
5. Se registra toda la información para auditoría

### ¿Qué es el tipo de uso en la asociación con proyectos?

El tipo de uso indica cómo se utiliza el agente en el proyecto:
- **PRIMARY:** Agente principal del proyecto
- **SECONDARY:** Agente secundario o de apoyo
- **REFERENCE:** Agente de referencia
- **TESTING:** Agente usado solo para pruebas

Esto permite análisis de reutilización y gestión de costes por tipo de uso.

### ¿Cómo funciona el Dashboard Ejecutivo de Gobierno?

El Dashboard proporciona una vista consolidada de:
- KPIs principales (total agentes, cumplimiento, aprobaciones pendientes, alertas críticas)
- Salud de agentes con puntuación consolidada
- Actividad reciente (aprobaciones, alertas, certificaciones, compliance)
- Métricas adicionales (HITL, interacciones, tiempo de respuesta)

Accede desde Agents → Dashboard.

### ¿Cómo se integra el módulo con Compliance?

El tab de Compliance en el detalle del agente muestra:
- Estado de clasificación de riesgo (con enlace a módulo Compliance)
- Estado de evaluación FRIA (con enlace directo)
- Estado de registro EU AI Act (con enlace directo)
- Estado de declaración de conformidad (con enlace directo)
- Puntuación general de cumplimiento

Todos los enlaces llevan directamente a las secciones correspondientes del módulo de Compliance.

### ¿Cómo funciona la certificación de agentes?

1. Verifica que todos los requisitos estén cumplidos (clasificación, FRIA, registro EU, declaración, documentación técnica, QMS)
2. Si todos están cumplidos, aparece el botón "Solicitar Certificación"
3. Se lanza proceso BPMN para aprobación
4. Una vez aprobado, el agente queda certificado con ID de certificado y fechas de validez

### ¿Cómo gestiono el retiro de un agente?

1. Accede a la pestaña "Retiro" en el detalle del agente
2. Revisa las dependencias activas (proyectos, despliegues, integraciones)
3. Si hay dependencias, crea un plan de migración detallado
4. Completa la solicitud de retiro (tipo, fecha, razón, plan de migración)
5. Se lanza proceso BPMN para aprobación
6. Una vez aprobado, el agente puede ser retirado

### ¿Cómo configuro las reglas de revisión HITL?

1. Accede a la pestaña "Reglas de Revisión" en el detalle del agente
2. Selecciona reglas Drools para evaluación automática
3. Selecciona prompts LLM para razonamiento complejo (con verificación de cumplimiento)
4. Reordena las reglas seleccionadas mediante drag-and-drop
5. Configura prioridad y orden de ejecución
6. Guarda la configuración

### ¿Cómo funcionan las decisiones del agente con HITL?

Las decisiones llegan automáticamente por telemetría. El sistema:
1. Filtra decisiones que requieren revisión según las reglas configuradas
2. Muestra estadísticas de decisiones pendientes
3. Permite revisar cada decisión con detalles completos
4. El revisor humano puede: Aprobar, Rechazar o Escalar
5. Se integra con BPMN para workflow de aprobación
6. Se registra toda la revisión para auditoría

### ¿Cómo funcionan las reversiones con HITL?

Las reversiones se disparan automáticamente. El sistema:
1. Muestra reversiones pendientes de aprobación
2. Permite revisar detalles de la reversión (agente, versiones, razón, trigger)
3. El revisor humano puede: Aprobar Reversión, Rechazar Reversión o Escalar
4. Se integra con BPMN para workflow de aprobación
5. Se registra toda la aprobación para auditoría

### ¿Cómo veo las interacciones usuario-agente?

Las interacciones se registran automáticamente por telemetría. Puedes:
1. Ver estadísticas en el overview (total, exitosas, fallidas, costo total)
2. Ver lista de interacciones con métricas (duración, tokens, costo)
3. Hacer clic en "Ver" para ver detalle completo (input/output formateados)
4. Las interacciones son solo de visualización (no se editan)

### ¿Qué información muestra el tab de Compliance?

El tab muestra:
- Puntuación general de cumplimiento AI Act
- Estado de clasificación de riesgo con categorías Anexo III
- Estado de evaluación FRIA con nivel de riesgo
- Estado de registro EU con ID y fechas
- Estado de certificación con información del certificado
- Estado de declaración de conformidad
- Enlaces directos a cada sección del módulo Compliance

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DEL REGISTRO

Una vez que un agente es registrado:

1. ✅ **Versionado:** Se puede gestionar versiones del agente
2. ✅ **Asociación a Proyectos:** El agente puede ser asociado a proyectos
3. ✅ **Cumplimiento:** Completar clasificación, FRIA, registro EU, declaración de conformidad
4. ✅ **Certificación:** Solicitar certificación una vez cumplidos todos los requisitos
5. ✅ **Configuración de Reglas:** Configurar reglas de revisión HITL (Drools + LLM prompts)
6. ✅ **Aprobación:** Se puede iniciar el proceso de aprobación si es necesario
7. ✅ **Despliegue:** Si es aprobado y certificado, puede procederse al despliegue
8. ✅ **Monitoreo:** Monitoreo continuo en producción (métricas, salud, interacciones)
9. ✅ **Revisión HITL:** Revisión humana de decisiones y aprobación de reversiones
10. ✅ **Retiro:** Cuando corresponda, gestionar el retiro del agente con plan de migración

---

---

## 🆕 NUEVAS FUNCIONALIDADES v2.0

### Dashboard Ejecutivo de Gobierno
- Vista consolidada de KPIs de gobierno de agentes
- Salud de agentes con puntuación consolidada
- Actividad reciente con diferentes tipos de eventos
- Métricas adicionales (HITL, interacciones, tiempo de respuesta)

### Integración Completa con Compliance
- Tab de Compliance mejorado con integración real
- Clasificación de riesgo con enlaces a módulo Compliance
- Evaluación FRIA con enlaces directos
- Registro EU AI Act con estado y fechas
- Declaración de conformidad integrada
- Puntuación general de cumplimiento

### Certificación de Agentes
- Proceso de certificación antes de producción
- Verificación automática de requisitos
- Gestión de certificados con fechas de expiración
- Integración con BPMN para aprobación

### Políticas de Retiro
- Gestión del retiro de agentes
- Detección automática de dependencias
- Plan de migración obligatorio
- Proceso de aprobación mediante BPMN

### Reglas de Revisión Configurables
- Sistema flexible de reglas Drools y LLM prompts
- Reordenamiento mediante drag-and-drop
- Integración con módulo de Prompts para cumplimiento

### Mejoras en HITL
- Decisions: Revisión humana de decisiones del agente
- Rollback: Aprobación HITL de reversiones automáticas
- Interfaz optimizada para revisión y aprobación

### Telemetría de Interacciones
- Registro automático de interacciones usuario-agente
- Métricas: duración, tokens, costo
- Visualización de input/output formateados

---

**Última Actualización:** Enero 2025
**Versión del Módulo:** 2.0
**Estado:** ✅ Operativo con gobierno completo de agentes
