# 📘 GUÍA FUNCIONAL - GOBIERNO Y CUMPLIMIENTO DE PROYECTOS RAG

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Compliance Officers, Governance Teams, RAG Engineers, Project Managers, Legal Teams, Data Protection Officers

---

## 🎯 ¿QUÉ ES EL GOBIERNO DE PROYECTOS RAG?

El **Gobierno de Proyectos RAG** es un módulo integral que permite registrar, clasificar, evaluar y monitorear sistemas RAG (Retrieval Augmented Generation) desde la perspectiva de gobierno, cumplimiento normativo y monitorización. El módulo proporciona herramientas completas para el registro de proyectos RAG, clasificación de riesgo, evaluaciones de impacto, monitoreo continuo y gestión del ciclo de vida normativo, cumpliendo con múltiples estándares internacionales (ISO), normativas sectoriales y leyes regionales.

### 🎯 ¿Qué es un Proyecto RAG?

Un **Proyecto RAG** (Retrieval Augmented Generation) es un sistema de inteligencia artificial que combina modelos de lenguaje con bases de conocimiento para proporcionar respuestas basadas en información recuperada. Los sistemas RAG recuperan información relevante de fuentes de datos (documentos fragmentados en chunks) y la utilizan para generar respuestas contextualizadas y precisas.

#### **Componentes de un Sistema RAG:**

1. **Modelo de Embedding:** Convierte texto en vectores numéricos para búsqueda semántica
2. **Base de Conocimiento:** Colección de documentos fragmentados en chunks indexados
3. **Motor de Búsqueda:** Recupera chunks relevantes basándose en similitud semántica
4. **Modelo Reranker (Opcional):** Refina los resultados de búsqueda por relevancia
5. **Modelo LLM:** Genera respuestas basándose en los chunks recuperados y la consulta del usuario

### 🔗 Vinculación con Cumplimiento Normativo

**Cada proyecto RAG debe estar vinculado a un proyecto de cumplimiento** para garantizar el gobierno normativo completo y el cumplimiento de múltiples estándares internacionales, normativas sectoriales y leyes regionales aplicables.

#### **Vinculación Automática:**

Cuando un proyecto RAG se registra para gobierno:

1. **Se crea automáticamente un Project en `PRJPROJECTS`:**
   - `PRJISTYPE = "RAG"` (identifica que es un proyecto RAG)
   - `PRJRAGPROJECTID` = ID del proyecto RAG en el sistema de ejecución
   - Campos de cumplimiento inicializados automáticamente
   - `projectId` (UUID) generado automáticamente para identificación única

2. **El Project queda vinculado mediante:**
   - `RagPipeline.IDXPROJECT` → `Project.IDXPROJECT` (Foreign Key)
   - `Project.PRJRAGPROJECTID` → ID del RagSystem en sistema de ejecución
   - El UUID del Project (`projectId`) se usa para filtrar eventos en telemetría

3. **La vinculación es automática** - No requiere acción manual del usuario

#### **Sistema Dual: Ejecución vs Gobierno**

- **Sistema de Ejecución (`/rag/projects`):** Donde se crean, configuran y operan los proyectos RAG
  - Gestión técnica: modelos (embedding, reranker, LLM), fuentes de datos, chunks, configuración
  - Operación: búsquedas, generación de respuestas, métricas operativas

- **Sistema de Gobierno (`/governance/rag/registry`):** Donde se gobierna y monitorea el cumplimiento normativo
  - Cumplimiento: clasificación de riesgo, evaluaciones de impacto, registro regulatorio
  - Gobernanza: aprobaciones, métricas de cumplimiento, trazabilidad regulatoria
  - Monitoreo: seguimiento continuo de cumplimiento y rendimiento

### ⚖️ ¿Por Qué Debe Estar Vinculado para Gobierno y Cumplimiento Normativo?

Los sistemas RAG son **sistemas de inteligencia artificial** que requieren gobierno normativo completo, monitoreo continuo y cumplimiento de múltiples marcos regulatorios por las siguientes razones:

#### **1. Clasificación Obligatoria (Art. 6 EU AI Act)**
- Todos los proyectos RAG deben ser clasificados según nivel de riesgo
- Los sistemas RAG de alto riesgo están sujetos a requisitos adicionales del Anexo III
- La clasificación debe documentarse con justificación (mínimo 100 caracteres, 2 keywords de riesgo)
- Permite determinar qué requisitos normativos aplican al sistema

#### **2. Evaluación FRIA Obligatoria (Art. 27 EU AI Act)**
- Los proyectos RAG de alto riesgo requieren evaluación de impacto en derechos fundamentales
- Debe evaluar riesgos específicos de RAG:
  - Sesgo en modelos de embedding que puede afectar la recuperación
  - Calidad y veracidad de chunks (información incorrecta puede propagarse)
  - Protección de datos personales en chunks (PII, información sensible)
  - Transparencia de fuentes de datos (debe documentarse el origen)
  - Exactitud de información recuperada
  - Riesgos de alucinaciones del LLM
- Requiere medidas de mitigación y supervisión humana (HITL)
- Obligación de notificar a autoridades si riesgo crítico (Art. 27.3)

#### **3. Registro en Registro EU (Art. 49 EU AI Act)**
- Los sistemas RAG de alto riesgo deben registrarse en el registro europeo antes del despliegue
- Requiere documentación técnica completa del sistema
- Permite supervisión regulatoria y transparencia pública
- Estado del registro debe monitorearse continuamente

#### **4. Monitoreo Post-Mercado (Art. 72 EU AI Act)**
- Requiere monitoreo continuo del cumplimiento y rendimiento en producción
- Detección de problemas de cumplimiento, degradación de rendimiento, sesgos
- Obligación de notificar a autoridades si se detectan problemas graves
- Análisis de métricas desde telemetría usando UUID del proyecto

#### **5. Trazabilidad y Auditorías**
- Auditorías regulatorias y de cumplimiento requieren registro completo del ciclo de vida normativo
- Evidencia documentada de clasificaciones, evaluaciones de impacto, y medidas de mitigación
- Historial completo de cambios y decisiones para cumplimiento de estándares ISO y normativas aplicables
- Registro de todas las acciones y justificaciones normativas

#### **6. Cumplimiento Multi-Marco Regulatorio**
- Cumplimiento de múltiples estándares ISO (ISO 42001, ISO 27001, etc.)
- Cumplimiento de normativas sectoriales específicas según industria
- Cumplimiento de leyes regionales aplicables (GDPR, EU AI Act, y otras según jurisdicción)
- Gestión centralizada de todos los requisitos normativos en un solo lugar

#### **7. Gestión de Riesgos Específicos de RAG**

Los sistemas RAG presentan riesgos únicos que requieren gobierno especializado:

- **Sesgo en modelos de embedding:** Puede hacer que se recuperen chunks sesgados o irrelevantes
- **Calidad y veracidad de chunks:** Información incorrecta en chunks puede propagarse a respuestas finales
- **Protección de datos personales:** Chunks pueden contener información sensible (PII) que debe protegerse
- **Transparencia de fuentes:** Los usuarios deben saber de dónde proviene la información utilizada
- **Riesgos de alucinaciones:** El LLM puede generar información no presente en los chunks recuperados
- **Actualización de conocimiento:** Los chunks pueden volverse obsoletos y requerir actualización

### 🎯 Propósito Principal

El módulo de gobierno de proyectos RAG permite:

1. **Registro Centralizado:** Mantener un registro único y centralizado de todos los proyectos RAG para gobierno y cumplimiento
2. **Clasificación de Riesgo:** Clasificar proyectos RAG según nivel de riesgo con justificación documentada según normativas aplicables
3. **Evaluaciones de Impacto:** Realizar evaluaciones de impacto (FRIA, DPIA, u otras según normativa) para proyectos de alto riesgo
4. **Registro Regulatorio:** Gestionar el registro de proyectos RAG en registros regulatorios correspondientes con seguimiento de estado
5. **Monitoreo Continuo:** Monitorear continuamente el cumplimiento, rendimiento y salud de proyectos RAG en producción
6. **Trazabilidad Completa:** Mantener registro completo del ciclo de vida normativo de cada proyecto RAG
7. **Cumplimiento Multi-Marco:** Gestionar cumplimiento de múltiples estándares ISO, normativas sectoriales y leyes regionales
8. **Integración con Telemetría:** Analizar métricas y eventos de RAG desde telemetría para monitoreo y análisis
9. **Sincronización con Ejecución:** Sincronizar datos entre sistema de ejecución y gobierno manteniendo trazabilidad

---

## 🌍 BASE LEGAL Y NORMATIVA

La plataforma de gobierno de proyectos RAG soporta el cumplimiento de múltiples marcos regulatorios y estándares:

### Estándares Internacionales (ISO)

#### ISO 42001 - Gestión de Sistemas de IA
- Control de versiones y gestión de cambios en sistemas RAG
- Trazabilidad de modificaciones y decisiones
- Gestión de riesgos y evaluación de impacto
- Monitoreo continuo y mejora continua

#### ISO 27001 - Seguridad de la Información
- Gestión de seguridad de la información en sistemas RAG
- Protección de datos en chunks y bases de conocimiento
- Control de acceso y gestión de credenciales

#### Otras Normas ISO Aplicables
- Según industria y requisitos específicos de la organización

### Normativas Regionales

#### EU AI Act (Europa)
- Clasificación de sistemas de IA de alto riesgo
- Evaluación de impacto en derechos fundamentales (FRIA)
- Registro en registro europeo
- Monitoreo post-mercado

#### GDPR (Protección de Datos)
- Evaluación de impacto en protección de datos (DPIA)
- Gestión de datos personales en chunks
- Derechos de los interesados
- Notificación de violaciones

#### Otras Leyes Regionales
- Normativas aplicables según jurisdicción (Américas, Asia, etc.)
- Leyes sectoriales específicas por región

### Normativas Sectoriales

- **Sanidad:** Regulaciones específicas de salud y dispositivos médicos
- **Finanzas:** Regulaciones financieras y de servicios bancarios
- **Educación:** Normativas educativas y protección de menores
- **Sector Público:** Regulaciones específicas de administración pública
- **Otros sectores:** Según industria específica

### Cumplimiento Multi-Marco

La plataforma permite gestionar el cumplimiento de múltiples marcos regulatorios simultáneamente, adaptándose a los requisitos específicos de cada normativa aplicable según el proyecto, sector e industria.

---

## 🚀 ¿PARA QUÉ SIRVE EL GOBIERNO DE PROYECTOS RAG?

### 1. **Gobierno y Cumplimiento Normativo**

El módulo facilita el gobierno y cumplimiento de:
- **Clasificación de Riesgo:** Clasificación obligatoria de sistemas RAG según nivel de riesgo según normativas aplicables
- **Evaluaciones de Impacto:** Evaluaciones FRIA, DPIA u otras según normativa aplicable para proyectos de alto riesgo
- **Registro Regulatorio:** Registro en registros regulatorios correspondientes según normativa aplicable
- **Monitoreo Continuo:** Monitoreo continuo de cumplimiento, rendimiento y salud de proyectos RAG
- **Estándares ISO:** Cumplimiento de ISO 42001, ISO 27001 y otros estándares ISO aplicables
- **Normativas Regionales:** Cumplimiento de GDPR, EU AI Act y otras leyes regionales según jurisdicción
- **Normativas Sectoriales:** Cumplimiento de normativas específicas por sector e industria
- **Trazabilidad Regulatoria:** Registro completo de proyectos RAG para auditorías regulatorias y de cumplimiento

### 2. **Gestión del Ciclo de Vida Normativo**

Proporciona herramientas para:
- **Registro Inicial:** Registrar proyectos RAG para gobierno con vinculación automática a Project de cumplimiento
- **Clasificación:** Clasificar proyectos según riesgo con justificación documentada (mínimo 100 caracteres, 2 keywords de riesgo)
- **Evaluación de Impacto:** Realizar evaluaciones completas de impacto según normativa aplicable
- **Registro EU:** Gestionar el proceso de registro en el registro europeo con seguimiento de estado
- **Monitoreo Continuo:** Seguimiento de cumplimiento y rendimiento en producción con alertas automáticas
- **Aprobación:** Flujo de aprobación antes del despliegue con validaciones normativas

### 3. **Vinculación y Sincronización con Sistema de Ejecución**

Permite:
- **Vinculación Automática:** Creación automática de Project en PRJPROJECTS al registrar proyecto RAG
- **Sincronización Bidireccional:** Sincronizar datos desde el sistema de ejecución RAG (nombre, descripción, métricas)
- **Trazabilidad:** Mantener vínculo entre proyecto de ejecución (`ragProjectId`) y proyecto de gobierno (`projectId` UUID)
- **Métricas Operativas:** Obtener métricas de uso (chunks, searches, tiempos de respuesta) desde ejecución
- **Estado Unificado:** Visualizar estado operativo y normativo en un solo lugar

### 4. **Integración con Telemetría mediante UUID**

Facilita:
- **Identificación por UUID:** Los proyectos RAG tienen un `projectId` (UUID) que se usa para filtrar eventos en telemetría
- **Análisis de Eventos:** Analizar eventos RAG (`RAG_SEARCH`, `RAG_CHUNK_PROCESSED`, etc.) filtrados por UUID de proyecto
- **Métricas de Cumplimiento:** Tokens consumidos, costos, latencia desde telemetría agregados por proyecto
- **Detección de Problemas:** Identificar problemas de gobernanza (bias, toxicidad, PII, secretos) en eventos RAG
- **Enlaces Directos:** Navegar desde gobierno a telemetría con filtros preaplicados por UUID del proyecto

### 5. **Monitoreo y Análisis de Cumplimiento**

Permite:
- **Score de Cumplimiento:** Calcular score de cumplimiento normativo (0-100) basado en clasificación, FRIA, registro EU, monitoreo
- **Alertas Automáticas:** Detectar proyectos con incumplimientos, FRIAs próximos a vencer, registros EU pendientes
- **Dashboard Consolidado:** Vista general del estado de cumplimiento de todos los proyectos RAG
- **Análisis Comparativo:** Comparar proyectos RAG entre sí para identificar mejores prácticas
- **Reportes y Exportación:** Generar reportes de cumplimiento para auditorías y exportar métricas

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Registro de Proyectos RAG** (`/governance/rag/registry`)

Pantalla principal para gestionar el catálogo de proyectos RAG para gobierno:

#### **Listado de Proyectos RAG**
- **Vista Tabular:** Tabla paginada con todos los proyectos RAG registrados para gobierno
- **Métricas Principales:**
  - Total de proyectos RAG registrados
  - Proyectos con clasificación pendiente
  - Proyectos de alto riesgo
  - Proyectos con FRIA completado
  - Proyectos registrados en EU
- **Filtros Avanzados:**
  - Búsqueda por nombre, ID, owner
  - Filtro por estado de clasificación (PENDING, CLASSIFIED, HIGH_RISK)
  - Filtro por estado de FRIA (NOT_STARTED, IN_PROGRESS, COMPLETED, EXPIRED)
  - Filtro por estado de registro regulatorio (NOT_REGISTERED, PENDING, REGISTERED)
  - Filtro por nivel de riesgo
- **Información por Proyecto:**
  - ID del proyecto (UUID del Project en PRJPROJECTS)
  - RAG Project ID (ID en sistema de ejecución)
  - Nombre y descripción
  - Owner y creador
  - Estado de vinculación con Project de cumplimiento
  - Estado de clasificación
  - Estado de evaluación de impacto
  - Estado de registro regulatorio
  - Última sincronización con sistema de ejecución
  - Score de cumplimiento (0-100)
  - Métricas operativas (chunks, searches, etc.)
  - Métricas desde telemetría (eventos, costos, tokens, latencia)
- **Acciones:**
  - Registrar nuevo proyecto RAG (manual o sincronizar desde ejecución)
  - Ver detalles completos (modal o página)
  - Clasificar proyecto (navega a clasificación)
  - Iniciar FRIA (navega a FRIA)
  - Sincronizar con sistema de ejecución
  - Ver proyecto de cumplimiento vinculado
  - Ver en telemetría (con filtro por UUID)

#### **Vinculación Automática con Proyecto de Cumplimiento**

Cuando se registra un proyecto RAG para gobierno:
- Se crea automáticamente un proyecto de cumplimiento asociado
- El proyecto queda identificado como sistema RAG en el sistema de cumplimiento
- Se establece la vinculación con el proyecto RAG en el sistema de ejecución
- Los campos de cumplimiento se inicializan automáticamente
- **La vinculación es automática** al registrar el proyecto RAG para gobierno

### 2. **Clasificación de Proyectos RAG** (`/governance/rag/classification`)

Pantalla para clasificar proyectos RAG según nivel de riesgo según normativas aplicables:

#### **Wizard de Clasificación**
- **Paso 1: Información del Proyecto RAG**
  - Muestra información básica del proyecto
  - Confirma que es el proyecto correcto a clasificar
- **Paso 2: Selección de Categoría de Riesgo**
  - Selección de categoría si aplica (sistemas de alto riesgo)
  - Lista de categorías relevantes según normativas aplicables
- **Paso 3: Justificación**
  - Campo de texto para justificación (mínimo 100 caracteres)
  - Debe mencionar la categoría seleccionada
  - Debe incluir al menos 2 palabras clave de riesgo
- **Paso 4: Confirmación y Guardado**
  - Resumen de la clasificación
  - Confirmación antes de guardar

#### **Validaciones**
- Justificación mínima de 100 caracteres
- Debe mencionar la categoría seleccionada
- Al menos 2 palabras clave de riesgo
- Usuario clasificador debe tener permisos adecuados

#### **Integración**
- Actualiza la clasificación del proyecto en el sistema de cumplimiento
- Almacena la categoría de alto riesgo si aplica
- Guarda la justificación proporcionada
- Registra fecha y usuario que realizó la clasificación
- Dispara procesos de aprobación automáticos si es alto riesgo
- Notifica a los responsables correspondientes

### 3. **Evaluación de Impacto para Proyectos RAG** (`/governance/rag/fria`)

Pantalla para realizar evaluaciones de impacto (FRIA, DPIA u otras según normativa aplicable) para proyectos RAG de alto riesgo:

#### **Wizard de Evaluación de Impacto (Pasos según normativa aplicable)**
- **a) Descripción de Procesos RAG:**
  - Descripción de cómo funciona el sistema RAG
  - Procesos de embedding, búsqueda, reranking, generación
  - Fuentes de datos utilizadas
- **b) Período y Frecuencia de Uso:**
  - Período de uso previsto
  - Frecuencia de uso (continuo, diario, semanal, etc.)
  - Volumen estimado de consultas
- **c) Categorías de Personas Afectadas:**
  - Grupos de personas que pueden verse afectadas
  - Vulnerabilidades específicas
- **d) Riesgos Específicos RAG:**
  - Sesgo en modelos de embedding
  - Calidad y veracidad de chunks
  - Protección de datos personales en chunks
  - Transparencia de fuentes de datos
  - Exactitud de información recuperada
  - Riesgos de alucinaciones
- **e) Medidas de Supervisión Humana (HITL):**
  - Human-in-the-loop (HITL) implementado
  - Procesos de revisión humana
  - Escalamiento a humanos
- **f) Medidas de Mitigación:**
  - Medidas para mitigar riesgos identificados
  - Controles técnicos y organizacionales
  - Plan de respuesta a incidentes

#### **Cálculo de Riesgo Final**
- Cálculo según metodología de evaluación de riesgo aplicable
- Nivel de riesgo: BAJO, MEDIO, ALTO, CRÍTICO
- Recomendaciones basadas en nivel de riesgo y normativas aplicables

#### **Integración**
- Vincula la evaluación de impacto con el proyecto de cumplimiento
- Notifica automáticamente a las autoridades competentes si el riesgo es crítico según normativa aplicable
- Integra con otras evaluaciones de impacto (DPIA, etc.) si aplica según normativa
- Actualiza el estado de la evaluación de impacto en el proyecto

### 4. **Health Dashboard** (`/governance/rag/monitoring/health-dashboard`)

Pantalla para monitorear salud y cumplimiento de proyectos RAG:

#### **Métricas de Salud por Proyecto**
- **Total Items (chunks):** Total de chunks indexados
- **Active Items:** Chunks activos
- **Deployed Items:** Chunks desplegados
- **Training Items:** Chunks en entrenamiento
- **Offline Items:** Chunks offline
- **Score Promedio de Calidad:** Score de calidad promedio de chunks

#### **Métricas de Cumplimiento**
- Proyectos con clasificación actualizada
- Proyectos con FRIA vencido (requiere renovación)
- Proyectos con registros EU pendientes
- Alertas de cumplimiento
- Score de cumplimiento por proyecto

#### **Filtros**
- Por proyecto
- Por estado de salud
- Por estado de cumplimiento
- Por nivel de riesgo

#### **Gráficos**
- Evolución de métricas de salud
- Distribución de estados de cumplimiento
- Tendencias de riesgos
- Comparación entre proyectos

### 5. **Aprobación de Proyectos RAG** (`/governance/rag/approval`)

Pantalla para gestionar el flujo de aprobación de proyectos RAG antes del despliegue:

#### **Listado de Solicitudes de Aprobación**
- **Vista Tabular:** Tabla con todas las solicitudes de aprobación
- **Métricas:**
  - Total pendientes
  - Total aprobadas
  - Total rechazadas
- **Información por Solicitud:**
  - Proyecto RAG
  - Solicitante
  - Fecha de solicitud
  - Estado de clasificación
  - Estado de FRIA
  - Justificación de solicitud
  - Comentarios de revisores

#### **Acciones**
- **Aprobar Proyecto:** Aprobar con comentarios opcionales
- **Rechazar Proyecto:** Rechazar con razón obligatoria
- **Solicitar Más Información:** Solicitar información adicional
- **Ver Detalles Completos:** Ver información completa del proyecto

#### **Validaciones**
- Proyecto debe estar clasificado
- Si es alto riesgo, debe tener FRIA completado
- Debe tener documentación técnica
- Razón obligatoria para rechazo

### 6. **Rendimiento y Métricas de Cumplimiento** (`/governance/rag/performance`)

Pantalla para analizar rendimiento de proyectos RAG desde perspectiva de cumplimiento:

#### **Métricas de Rendimiento**
- **Latencia Promedio:** Tiempo de respuesta promedio
- **Tasa de Éxito:** Porcentaje de consultas exitosas
- **Calidad de Respuestas:** Score de calidad de respuestas
- **Uso de Recursos:** Consumo de recursos (CPU, memoria, etc.)

#### **Métricas de Cumplimiento**
- **Score de Cumplimiento Normativo:** Score de 0-100
- **Proyectos en Cumplimiento:** Número y porcentaje
- **Proyectos con Incumplimientos:** Número y detalles
- **Tendencias de Cumplimiento:** Evolución temporal

#### **Análisis Comparativo**
- Comparar proyectos RAG entre sí
- Benchmarking de cumplimiento
- Identificar mejores prácticas
- Comparar métricas de rendimiento

#### **Alertas**
- Degradación de rendimiento
- Incumplimientos detectados
- FRIA próximos a vencer
- Registros EU pendientes

### 7. **Analíticas de Gobierno RAG** (`/governance/rag/analytics`)

Dashboard analítico de gobierno y cumplimiento RAG:

#### **KPIs Principales**
- Total proyectos RAG gobernados
- Porcentaje con clasificación completa
- Porcentaje con FRIA completo
- Porcentaje registrados en EU
- Score promedio de cumplimiento
- Proyectos de alto riesgo
- Proyectos con incumplimientos

#### **Gráficos**
- Distribución por nivel de riesgo
- Evolución temporal de registros
- Distribución por categorías de riesgo según normativas aplicables
- Estado de FRIAs (completados, en progreso, vencidos)
- Tendencias de cumplimiento
- Comparación de scores de cumplimiento

#### **Filtros Temporales**
- Por rango de fechas
- Por trimestre/año
- Por proyecto
- Por estado de cumplimiento

#### **Exportación**
- Exportar reportes en PDF/Excel
- Exportar métricas en CSV
- Generar reportes personalizados

### 8. **Dashboard de Cumplimiento RAG** (`/governance/rag/compliance/dashboard`)

Vista consolidada de cumplimiento normativo de proyectos RAG:

#### **Vista Consolidada**
- **Estado General de Cumplimiento:** Score global y tendencias
- **Proyectos por Estado:** Distribución de proyectos por estado de cumplimiento
- **Alertas y Pendientes:** Lista de alertas y acciones pendientes
- **Próximas Acciones Requeridas:** Tareas próximas a vencer

#### **Matriz de Cumplimiento**
- **Proyectos vs Requisitos Normativos:** Tabla cruzada
- **Estado de Cada Requisito:** Cumplido, Pendiente, Incumplido
- **Enlaces a Detalle:** Navegación rápida a detalle de cada requisito

#### **Accesos Rápidos**
- Proyectos pendientes de clasificación
- Evaluaciones de impacto próximas a vencer
- Registros regulatorios pendientes
- Proyectos con incumplimientos
- Proyectos de alto riesgo sin evaluación de impacto

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Registro de un Nuevo Proyecto RAG para Gobierno

1. **Acceso al Registro**
   - Navegar a Governance → RAG Governance → Registry
   - Hacer clic en "Registrar Proyecto" o "Sincronizar"

2. **Registro Manual o Sincronización**
   - **Opción A - Registro Manual:**
     - Completar formulario con:
       - Nombre del proyecto
       - Descripción
       - Owner
       - RAG Project ID (ID en sistema de ejecución)
     - El sistema crea automáticamente el Project en PRJPROJECTS
   - **Opción B - Sincronización:**
     - Seleccionar proyecto desde sistema de ejecución
     - Sincronizar datos automáticamente
     - El sistema crea automáticamente el Project en PRJPROJECTS

3. **Vinculación Automática**
   - Se crea automáticamente un proyecto de cumplimiento asociado
   - El proyecto queda identificado como sistema RAG
   - Se establece la vinculación con el proyecto RAG en ejecución
   - Los campos de cumplimiento se inicializan automáticamente
   - El proyecto queda vinculado automáticamente

4. **Sincronización de Métricas**
   - El sistema sincroniza métricas desde el sistema de ejecución:
     - Total de chunks
     - Total de búsquedas
     - Tiempo de respuesta promedio
     - Tasa de éxito
   - El sistema obtiene métricas desde telemetría usando el identificador único del proyecto

5. **Navegación a Clasificación**
   - El proyecto queda registrado y listo para clasificación
   - Se puede navegar a clasificación desde el registro

### Flujo 2: Clasificación de un Proyecto RAG

1. **Acceder a Clasificación**
   - Desde el registro, hacer clic en "Clasificar" en un proyecto
   - O navegar directamente a Governance → RAG Governance → Classification

2. **Completar Wizard de Clasificación**
   - **Paso 1:** Confirmar información del proyecto
   - **Paso 2:** Seleccionar categoría de riesgo según normativas aplicables si aplica
   - **Paso 3:** Escribir justificación (mínimo 100 caracteres, 2 keywords de riesgo)
   - **Paso 4:** Confirmar y guardar

3. **Actualización Automática**
   - Se actualiza la clasificación de riesgo del proyecto
   - Se registra la categoría de alto riesgo si aplica
   - Se guarda la justificación, fecha y usuario que realizó la clasificación
   - Si es alto riesgo, se activan procesos de aprobación automáticos

4. **Navegación a FRIA (si es alto riesgo)**
   - Si el proyecto es clasificado como alto riesgo, se debe iniciar FRIA
   - Navegación automática a FRIA desde clasificación

### Flujo 3: Evaluación FRIA para Proyecto RAG de Alto Riesgo

1. **Acceder a FRIA**
   - Desde el registro, hacer clic en "Iniciar FRIA" en un proyecto de alto riesgo
   - O navegar directamente a Governance → RAG Governance → FRIA

2. **Completar Wizard FRIA (6 Pasos)**
   - **a) Descripción de Procesos RAG:** Describir cómo funciona el sistema RAG
   - **b) Período y Frecuencia:** Definir período y frecuencia de uso
   - **c) Personas Afectadas:** Identificar categorías de personas afectadas
   - **d) Riesgos Específicos RAG:** Identificar riesgos específicos (sesgo, calidad de chunks, etc.)
   - **e) Supervisión Humana:** Describir medidas HITL
   - **f) Medidas de Mitigación:** Describir medidas de mitigación

3. **Cálculo de Riesgo Final**
   - El sistema calcula el nivel de riesgo según metodología aplicable
   - Genera recomendaciones basadas en el nivel de riesgo

4. **Guardado y Notificaciones**
   - Se guarda la evaluación FRIA vinculada al proyecto de cumplimiento
   - Si el riesgo es crítico, se notifica automáticamente a las autoridades competentes según normativa aplicable
   - Se integra con otras evaluaciones de impacto (DPIA, etc.) si aplica según normativa
   - Se actualiza el estado de la evaluación FRIA en el proyecto

### Flujo 4: Registro EU de Proyecto RAG

1. **Verificar Requisitos**
   - Proyecto debe estar clasificado como alto riesgo
   - Proyecto debe tener FRIA completado
   - Documentación técnica debe estar completa

2. **Iniciar Registro EU**
   - Desde el registro, hacer clic en "Registrar en EU"
   - Completar información requerida para registro EU

3. **Seguimiento de Estado**
   - Estado: NOT_REGISTERED → PENDING → REGISTERED
   - Monitorear estado del registro
   - Actualizar cuando se complete el registro

### Flujo 5: Monitoreo y Análisis con Telemetría

1. **Ver Métricas desde Telemetría**
   - Desde el registro, hacer clic en "Ver en Telemetría"
   - Se filtra automáticamente por UUID del Project
   - Ver eventos RAG filtrados por proyecto

2. **Analizar Eventos RAG**
   - Filtrar por tipo de evento (RAG_SEARCH, RAG_CHUNK_PROCESSED, etc.)
   - Ver métricas agregadas (tokens, costos, latencia)
   - Analizar tendencias temporales

3. **Detectar Problemas de Cumplimiento**
   - Identificar eventos con problemas de gobernanza
   - Detectar sesgos o toxicidad
   - Identificar PII o secretos en chunks

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo y auditorías
- **Uso:** Registrar proyectos RAG, clasificar, revisar FRIAs, monitorear cumplimiento
- **Beneficio:** Cumplimiento normativo multi-marco, trazabilidad regulatoria, evidencia para auditorías

#### 2. **Governance Teams** 🛡️
- **Responsabilidad:** Gobernanza de sistemas de IA
- **Uso:** Gestionar ciclo de vida normativo, aprobar proyectos, revisar métricas
- **Beneficio:** Control centralizado, visibilidad completa, gestión estructurada

#### 3. **RAG Engineers** 👨‍💻
- **Responsabilidad:** Desarrollo y operación de sistemas RAG
- **Uso:** Registrar proyectos, sincronizar métricas, revisar clasificaciones
- **Beneficio:** Integración con gobierno, cumplimiento desde el inicio

#### 4. **Project Managers** 📋
- **Responsabilidad:** Gestión de proyectos RAG
- **Uso:** Consultar estado de cumplimiento, revisar métricas, gestionar aprobaciones
- **Beneficio:** Visibilidad de cumplimiento, gestión de riesgos

#### 5. **Legal Teams** ⚖️
- **Responsabilidad:** Asesoramiento legal y regulatorio
- **Uso:** Revisar FRIAs, validar clasificaciones, revisar registros EU
- **Beneficio:** Cumplimiento legal, mitigación de riesgos legales

#### 6. **Data Protection Officers (DPO)** 🔒
- **Responsabilidad:** Protección de datos personales
- **Uso:** Revisar FRIAs, validar protección de datos en chunks, revisar medidas de mitigación
- **Beneficio:** Cumplimiento GDPR, protección de datos personales

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Gobierno y Cumplimiento Normativo:** Facilita gobierno completo y cumplimiento de múltiples marcos regulatorios para sistemas RAG
2. **Trazabilidad:** Historial completo del ciclo de vida normativo de cada proyecto RAG
3. **Centralización:** Registro único de todos los proyectos RAG para gobierno
4. **Automatización:** Vinculación automática con Project de cumplimiento, workflows automáticos
5. **Integración:** Integración con telemetría, sistema de ejecución, y otros módulos de compliance
6. **Monitoreo:** Monitoreo continuo de cumplimiento y rendimiento
7. **Gobernanza:** Control centralizado de proyectos RAG desde perspectiva normativa

### Para los Usuarios

1. **Facilidad de Uso:** Interfaz intuitiva y organizada
2. **Automatización:** Vinculación automática, sincronización automática
3. **Visibilidad:** Información completa en un solo lugar
4. **Eficiencia:** Búsquedas y filtros avanzados
5. **Documentación:** Información estructurada y completa
6. **Enlaces Directos:** Navegación rápida entre módulos (telemetría, ejecución, etc.)

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo para auditorías
2. **Cumplimiento Multi-Marco:** Múltiples estándares ISO, normativas sectoriales y leyes regionales completamente cubiertas
3. **ISO 42001:** Control de versiones y gestión de cambios
4. **Evidencia:** Documentación completa de clasificaciones, FRIAs, y registros
5. **Notificaciones:** Notificaciones automáticas a autoridades cuando es requerido

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Sistema de Ejecución RAG

**Sincronización de Datos:**
- Sincronización de información básica (nombre, descripción, propietario)
- Sincronización de métricas operativas (chunks, búsquedas, etc.)
- Estado operativo del proyecto

**Vinculación:**
- El sistema mantiene la vinculación entre el proyecto RAG en ejecución y el proyecto de cumplimiento
- Los cambios en el sistema de ejecución se reflejan automáticamente en el sistema de gobierno

### Telemetría

**Integración mediante Identificador Único:**
- Los proyectos RAG en gobierno tienen un identificador único que permite su seguimiento en telemetría
- Telemetría permite filtrar eventos por el identificador único del proyecto RAG
- Los eventos RAG incluyen información que permite su identificación y análisis

**Métricas desde Telemetría:**
- Total de eventos del proyecto
- Costo total desde telemetría
- Latencia promedio desde telemetría
- Tokens consumidos
- Eventos con problemas de gobernanza (bias, toxicidad, PII, secretos)

**Enlaces Directos:**
- Botón "Ver en Telemetría" que filtra automáticamente por el proyecto RAG
- Navegación directa a la vista de telemetría con los filtros aplicados

### Módulos de Compliance

**Reutilización de Funcionalidades:**
- El módulo reutiliza las funcionalidades de clasificación de alto riesgo para RAG
- Reutiliza las funcionalidades de evaluación FRIA adaptadas para proyectos RAG
- Comparte los procesos de registro EU con otros sistemas de IA
- Utiliza las mismas capacidades de monitoreo post-mercado que otros módulos

**Gestión Unificada:**
- Los proyectos RAG se gestionan en el mismo sistema de cumplimiento que otros sistemas de IA
- Las evaluaciones FRIA se almacenan de forma unificada independientemente del tipo de sistema
- El registro EU se gestiona de forma consistente para todos los sistemas de IA

### Workflows BPMN

**Workflows Automáticos:**
- Cuando un proyecto RAG es clasificado como alto riesgo, se dispara workflow BPMN
- Workflow de aprobación para proyectos RAG
- Workflow de notificación a autoridades si riesgo crítico

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **Vinculación Automática con Proyecto de Cumplimiento**

Cuando se registra un proyecto RAG para gobierno:
- Se crea automáticamente un proyecto de cumplimiento asociado
- El proyecto queda identificado como sistema RAG en el sistema de cumplimiento
- Se establece la vinculación con el proyecto RAG en ejecución
- **La vinculación es automática** - no requiere acción manual

### 2. **Integración con Telemetría mediante Identificador Único**

- Los proyectos RAG tienen un identificador único que se usa para filtrar eventos en telemetría
- Telemetría recibe eventos RAG asociados al identificador único del proyecto
- Las pantallas de gobierno muestran métricas desde telemetría filtradas por proyecto
- Enlaces directos desde gobierno a telemetría con filtros preaplicados

### 3. **Reutilización de Servicios de Compliance**

- Clasificación, FRIA, Registro EU, y PMM se reutilizan completamente para RAG
- No se duplica lógica - se adapta para RAG mediante filtros y configuraciones
- Mantiene consistencia con otros sistemas de IA (models, agents, prompts, datasets)

### 4. **Riesgos Específicos RAG en FRIA**

El FRIA para RAG incluye riesgos específicos:
- Sesgo en modelos de embedding
- Calidad y veracidad de chunks
- Protección de datos personales en chunks
- Transparencia de fuentes de datos
- Exactitud de información recuperada
- Riesgos de alucinaciones

### 5. **Score de Cumplimiento Normativo**

- Score de 0-100 calculado automáticamente
- Basado en: clasificación, FRIA, registro EU, monitoreo, métricas
- Actualizado en tiempo real
- Visible en todas las pantallas de gobierno

### 6. **Sincronización con Sistema de Ejecución**

- Sincronización manual o automática desde sistema de ejecución
- Mantiene datos actualizados entre ejecución y gobierno
- Gobierno es la fuente de verdad para cumplimiento normativo

---

## ❓ PREGUNTAS FRECUENTES

### ¿Qué es un proyecto RAG?

Un proyecto RAG (Retrieval Augmented Generation) es un sistema de inteligencia artificial que combina modelos de lenguaje con bases de conocimiento para proporcionar respuestas basadas en información recuperada. Incluye modelos de embedding, base de conocimiento (chunks), motor de búsqueda, reranker opcional y modelo LLM.

### ¿Cómo se vincula un proyecto RAG a cumplimiento normativo?

La vinculación ocurre **automáticamente** cuando se registra un proyecto RAG para gobierno. El sistema crea automáticamente un proyecto de cumplimiento asociado que queda vinculado con el proyecto RAG en el sistema de ejecución. Esta vinculación permite gestionar todo el ciclo de vida normativo del proyecto RAG.

### ¿Por qué debe estar vinculado para cumplimiento normativo?

Los sistemas RAG son sistemas de IA que requieren gobierno normativo completo, incluyendo:
- Clasificación obligatoria según normativas aplicables
- Evaluación de impacto si es alto riesgo según normativa aplicable
- Registro en registros regulatorios correspondientes
- Monitoreo continuo de cumplimiento y rendimiento
- Trazabilidad regulatoria para auditorías

### ¿Cómo funciona la integración con telemetría?

Los proyectos RAG tienen un identificador único que permite su seguimiento en telemetría. Telemetría permite filtrar eventos por este identificador único del proyecto. Los eventos RAG incluyen información que permite su identificación y análisis. Las pantallas de gobierno muestran métricas desde telemetría filtradas por proyecto.

### ¿Puedo editar la clasificación de un proyecto RAG?

Sí, puedes editar la clasificación navegando a la pantalla de clasificación. Sin embargo, los cambios deben estar justificados y pueden requerir actualización de la evaluación de impacto si cambia el nivel de riesgo.

### ¿Qué pasa cuando registro un nuevo proyecto RAG para gobierno?

Al registrar un proyecto RAG para gobierno:
1. Se crea automáticamente un proyecto de cumplimiento asociado
2. Se establece la vinculación con el proyecto RAG en ejecución
3. Se sincronizan métricas desde el sistema de ejecución
4. Se obtienen métricas desde telemetría usando el identificador único del proyecto
5. El proyecto queda listo para clasificación

### ¿Cómo gestiono las evaluaciones FRIA?

1. Accede a la pantalla FRIA desde el registro
2. Completa el wizard de evaluación de impacto según normativa aplicable
3. El sistema calcula el nivel de riesgo según metodología aplicable
4. Se guarda el FRIA vinculado al Project
5. Si el riesgo es crítico, se notifica a autoridades automáticamente

### ¿Puedo sincronizar proyectos desde el sistema de ejecución?

Sí, puedes sincronizar proyectos RAG desde el sistema de ejecución usando el botón "Sincronizar" en el registro. Esto actualiza la información y métricas del proyecto desde el sistema de ejecución.

### ¿Cómo veo las métricas de telemetría de un proyecto RAG?

Desde el registro, puedes hacer clic en "Ver en Telemetría" que te lleva a telemetría con filtros preaplicados por el proyecto RAG. También puedes ver métricas agregadas directamente en el registro (eventos, costos, tokens, latencia).

### ¿Qué métricas están disponibles desde telemetría?

Las métricas desde telemetría incluyen:
- Total de eventos del proyecto (últimos 30 días)
- Costo total desde telemetría (USD)
- Latencia promedio desde telemetría (ms)
- Tokens consumidos
- Eventos con problemas de gobernanza (bias, toxicidad, PII, secretos)

### ¿Cómo filtro proyectos por estado de cumplimiento?

En el listado de proyectos, utiliza los filtros superiores:
- Filtro por estado de clasificación (PENDING, CLASSIFIED, HIGH_RISK)
- Filtro por estado de evaluación de impacto (NOT_STARTED, IN_PROGRESS, COMPLETED, EXPIRED)
- Filtro por estado de registro regulatorio (NOT_REGISTERED, PENDING, REGISTERED)
- Filtro por nivel de riesgo
- Búsqueda por nombre, ID, owner

### ¿Qué pasa si un proyecto RAG es clasificado como alto riesgo?

Si un proyecto RAG es clasificado como alto riesgo:
1. Se marca el proyecto como de alto riesgo en el sistema de cumplimiento
2. Se activan automáticamente procesos de aprobación
3. Se debe iniciar evaluación de impacto según normativa aplicable
4. Se debe registrar en registros regulatorios correspondientes antes del despliegue
5. Se requiere monitoreo continuo de cumplimiento y rendimiento

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DEL REGISTRO

Una vez que un proyecto RAG es registrado para gobierno:

1. ✅ **Vinculación Automática:** Se crea automáticamente el proyecto de cumplimiento asociado
2. ✅ **Sincronización:** Se sincronizan métricas desde sistema de ejecución y telemetría
3. ✅ **Clasificación:** Se debe clasificar el proyecto según normativas aplicables
4. ✅ **Evaluación de Impacto (si es alto riesgo):** Se debe realizar evaluación de impacto según normativa aplicable
5. ✅ **Registro Regulatorio (si es alto riesgo):** Se debe registrar en registros regulatorios correspondientes
6. ✅ **Monitoreo:** Monitoreo continuo de cumplimiento y rendimiento
7. ✅ **Aprobación:** Flujo de aprobación antes del despliegue

---

**Última Actualización:** Diciembre 2025
**Versión del Módulo:** 1.0
**Estado:** ✅ Operativo y listo para producción
