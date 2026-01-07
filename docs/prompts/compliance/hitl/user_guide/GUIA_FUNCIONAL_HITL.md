# 📘 GUÍA FUNCIONAL - HUMAN-IN-THE-LOOP (HITL) SUPERVISION

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales, Compliance Officers, Supervisores HITL, Project Managers

---

## 🎯 ¿QUÉ ES HUMAN-IN-THE-LOOP (HITL) SUPERVISION?

Human-in-the-Loop (HITL) Supervision es un sistema de **supervisión humana** de decisiones críticas en sistemas de Inteligencia Artificial. Su objetivo es garantizar que las decisiones importantes que afectan la seguridad, cumplimiento normativo o calidad de los sistemas de IA sean revisadas y aprobadas por supervisores humanos antes de su ejecución.

### 🎯 Propósito Principal

El HITL Supervision permite:

1. **Supervisión Humana:** Revisión y aprobación humana de decisiones críticas de sistemas de IA
2. **Control de Calidad:** Garantizar que las decisiones automáticas cumplan con estándares de calidad y compliance
3. **Cumplimiento Normativo:** Cumplimiento con EU AI Act y regulaciones que requieren supervisión humana
4. **Trazabilidad:** Registro completo de decisiones humanas con razones documentadas
5. **Gestión de SLA:** Control de tiempos de respuesta y cumplimiento de SLAs de supervisión

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 14 - Requisitos de Alto Riesgo - Supervisión Humana
Establece que los sistemas de IA de alto riesgo deben tener **mecanismos de supervisión humana efectiva** para garantizar que las decisiones críticas sean revisadas por humanos.

#### Art. 15 - Transparencia y Provisión de Información
Requiere que los sistemas de IA proporcionen información suficiente para que los supervisores humanos puedan tomar decisiones informadas.

#### Art. 72 - Vigilancia Post-Comercialización
Establece la necesidad de **supervisión continua** y capacidad de intervención humana en sistemas de alto riesgo.

---

## 🚀 ¿PARA QUÉ SIRVE HITL SUPERVISION?

### 1. **Para Sistemas de IA de Alto Riesgo (Obligatorio)**
- Aprobación de despliegues de modelos
- Revisión de decisiones críticas de agentes
- Validación de prompts sensibles
- Supervisión de cambios en sistemas de producción

### 2. **Para Cumplimiento Normativo**
- Evidencia de supervisión humana para auditorías
- Trazabilidad completa de decisiones
- Registro de razones de aprobación/rechazo

### 3. **Para Control de Calidad**
- Revisión de decisiones automáticas antes de ejecución
- Validación de resultados de sistemas de IA
- Detección de errores o sesgos

### 4. **Para Gestión de Riesgos**
- Control de decisiones que pueden tener impacto significativo
- Prevención de decisiones erróneas o no conformes
- Mitigación de riesgos operacionales

---

## 📋 FUNCIONALIDADES PRINCIPALES

### 1. **Dashboard de Supervisión**
- **Métricas en Tiempo Real:**
  - Tiempo promedio de respuesta
  - Tasa de aprobación
  - Cumplimiento de SLA
  - Intervenciones pendientes

- **Visualización:**
  - Cards de métricas principales
  - Distribución de intervenciones por tipo
  - Distribución de intervenciones por estado

### 2. **Gestión de Intervenciones Pendientes**
- **Listado de Intervenciones:**
  - Grid de 3 columnas con cards individuales
  - Filtros por estado (PENDING, IN_REVIEW)
  - Filtros por tipo de entidad (Agent, Model, Prompt)
  - Indicadores de urgencia SLA

- **Acciones sobre Intervenciones:**
  - **Revisar:** Abre dialog para revisar y decidir
  - **Aprobar:** Aprobar directamente (abre dialog con decisión pre-seleccionada)
  - **Rechazar:** Rechazar directamente (abre dialog con decisión pre-seleccionada)

- **Información Mostrada:**
  - Tipo de entidad y nombre
  - Estado de la intervención
  - Urgencia (CRITICAL, HIGH, MEDIUM, LOW)
  - Tipo de intervención
  - Fecha de creación
  - SLA deadline
  - Tiempo restante para cumplir SLA
  - Alertas visuales para SLA urgente o próximo

### 3. **Registro de Decisiones**
- **Dialog de Decisión:**
  - Entidad a supervisar (solo lectura)
  - Tipo de decisión (APPROVED, REJECTED, MODIFIED)
  - Razón de la decisión (editor de texto enriquecido)
  - Botones de acción (Cancelar, Guardar)

- **Editor de Texto Enriquecido:**
  - Formato de texto (negrita, cursiva, subrayado)
  - Listas ordenadas y con viñetas
  - Colores de texto y fondo
  - Alineación de texto
  - Enlaces
  - Citas y bloques de código
  - Limpieza de formato

### 4. **Historial de Decisiones**
- **Listado de Decisiones Recientes:**
  - Grid de 3 columnas con cards individuales
  - Filtros por tipo de entidad
  - Información completa de cada decisión

- **Información Mostrada:**
  - Tipo de entidad y nombre
  - Decisión tomada (APPROVED, REJECTED, MODIFIED)
  - Razón de la decisión
  - Tiempo de respuesta
  - Fecha de decisión
  - Usuario que tomó la decisión

### 5. **Configuración de Supervisión**
- **Gestión de Configuración por Tipo:**
  - Habilitar/deshabilitar supervisión por tipo
  - Configurar SLA en horas
  - Activar/desactivar escalación automática

- **Tipos de Supervisión:**
  - AGENT_APPROVAL: Aprobación de agentes
  - MODEL_DEPLOYMENT: Despliegue de modelos
  - PROMPT_REVIEW: Revisión de prompts
  - Otros tipos configurables

---

## 🔄 FLUJOS DE TRABAJO

### Flujo 1: Revisión y Aprobación de Intervención

```
1. Sistema detecta necesidad de supervisión
   ↓
2. Se crea intervención HITL (dispara workflow BPMN)
   ↓
3. Intervención aparece en dashboard como PENDING
   ↓
4. Supervisor revisa la intervención
   ↓
5. Supervisor toma decisión:
   - Aprobar → Abre dialog con APPROVED pre-seleccionado
   - Rechazar → Abre dialog con REJECTED pre-seleccionado
   - Revisar → Abre dialog sin pre-selección
   ↓
6. Supervisor escribe razón usando editor enriquecido
   ↓
7. Supervisor guarda decisión
   ↓
8. Se guarda decisión en BD
   ↓
9. Se dispara workflow BPMN "hitl-decision-process"
   ↓
10. Sistema continúa según la decisión
```

### Flujo 2: Configuración de Supervisión

```
1. Supervisor accede a configuración
   ↓
2. Selecciona tipo de supervisión
   ↓
3. Configura parámetros:
   - Habilitado/Deshabilitado
   - SLA en horas
   - Escalación automática
   ↓
4. Guarda configuración
   ↓
5. Configuración se aplica a nuevas intervenciones
```

---

## 📊 MÉTRICAS Y KPIs

### Métricas Principales

1. **Tiempo Promedio de Respuesta:**
   - Tiempo promedio desde creación hasta decisión
   - Medido en horas
   - Indicador de eficiencia del proceso

2. **Tasa de Aprobación:**
   - Porcentaje de decisiones aprobadas vs rechazadas
   - Indicador de calidad de decisiones automáticas

3. **Cumplimiento de SLA:**
   - Porcentaje de decisiones tomadas dentro del SLA
   - Indicador de cumplimiento de tiempos acordados

4. **Intervenciones Pendientes:**
   - Número de intervenciones esperando decisión
   - Indicador de carga de trabajo

### Distribuciones

- **Por Tipo de Entidad:**
  - Agent
  - Model
  - Prompt
  - Otros

- **Por Estado:**
  - PENDING
  - IN_REVIEW
  - APPROVED
  - REJECTED
  - MODIFIED

---

## 🎨 INTERFAZ DE USUARIO

### Layout Principal

- **Header:**
  - Título: "HITL Supervision"
  - Subtítulo: Descripción del módulo
  - Botón de configuración

- **Métricas Principales:**
  - Grid de 3 cards con métricas clave
  - Iconos y colores distintivos
  - Valores numéricos destacados

- **Tabs:**
  - Tab 1: Intervenciones Pendientes
  - Tab 2: Decisiones Recientes

### Cards de Intervenciones

- **Layout:**
  - Grid de 3 columnas (responsive)
  - Contenido principal a la izquierda
  - Botones de acción a la derecha (vertical)

- **Información:**
  - Badges de tipo y estado
  - Nombre de entidad
  - Detalles de SLA
  - Indicadores de urgencia

- **Acciones:**
  - Botón "Revisar"
  - Botón "Aprobar"
  - Botón "Rechazar"

### Dialog de Decisión

- **Tamaño:**
  - Ancho: 700px
  - Altura mínima: 550px, máxima: 80vh

- **Componentes:**
  - Header con título y botón de cierre
  - Campo de entidad (solo lectura)
  - Selector de decisión
  - Editor de texto enriquecido (altura: 350px)
  - Footer con botones Cancelar y Guardar

---

## 🔐 PERMISOS Y ROLES

### Roles con Acceso

- **GESTOR_GOBIERNO:** Acceso completo
- **CIENTIFICO_DATOS:** Acceso de lectura y decisión
- **COMPLIANCE_OFFICER:** Acceso completo
- **ADMIN:** Acceso completo

### Permisos Requeridos

- **Ver Dashboard:** `hitl:view`
- **Revisar Intervenciones:** `hitl:review`
- **Tomar Decisiones:** `hitl:decide`
- **Configurar Supervisión:** `hitl:configure`

---

## 🔗 INTEGRACIONES

### 1. **Workflow BPMN**
- **Creación de Intervención:** Dispara `hitl-supervision-process`
- **Registro de Decisión:** Dispara `hitl-decision-process`

### 2. **Microservicios de Python (Opcional)**
- **Evaluación Automática:** Usa `AIGovernanceClient` para evaluación previa
- **Contexto Adicional:** Proporciona información adicional al supervisor

### 3. **Backend Services**
- **HitlSupervisionBusinessService:** Lógica de negocio
- **Repositorios JPA:** Acceso a datos
- **BFF Compliance:** Agregación y routing

---

## 📝 NOTAS IMPORTANTES

### SLAs y Urgencia

- **SLA Urgente:** Tiempo restante < 1 hora (fondo rojo)
- **SLA Próximo:** Tiempo restante < 2 horas (fondo naranja)
- **SLA Normal:** Tiempo restante >= 2 horas (fondo normal)

### Decisiones Disponibles

- **APPROVED:** La intervención es aprobada
- **REJECTED:** La intervención es rechazada
- **MODIFIED:** La intervención requiere modificaciones

### Tipos de Entidades

- **Agent:** Agentes de IA
- **Model:** Modelos de IA
- **Prompt:** Prompts de sistemas
- **Otros:** Tipos configurables

---

## 🆘 SOPORTE Y AYUDA

Para más información:
- Consultar la **Guía de Uso de Pantallas**
- Revisar la **Guía de Desarrollador**
- Contactar al equipo de Compliance

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.0
