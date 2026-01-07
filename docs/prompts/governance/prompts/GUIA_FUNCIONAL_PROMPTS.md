# 📘 GUÍA FUNCIONAL - GESTIÓN DE PROMPTS DE IA

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** AI Engineers, Prompt Engineers, Project Managers, Compliance Officers, DevOps Teams

---

## 🎯 ¿QUÉ ES LA GESTIÓN DE PROMPTS?

La **Gestión de Prompts** es un módulo integral que permite registrar, versionar, gestionar, validar y monitorear prompts de IA a lo largo de todo su ciclo de vida. El módulo proporciona herramientas completas para el registro de prompts, control de versiones, análisis de cumplimiento normativo, evaluación de rendimiento y gestión de relaciones con agents, models y sistemas RAG.

### 🎯 Propósito Principal

El módulo de gestión de prompts permite:

1. **Registro Centralizado:** Mantener un registro único y centralizado de todos los prompts de IA utilizados en la organización
2. **Control de Versiones:** Gestionar versiones de prompts con versionado semántico automático (MAJOR.MINOR.PATCH)
3. **Análisis de Cumplimiento:** Analizar prompts para cumplimiento normativo (EU AI Act), seguridad (prompt injection) y detección de sesgos
4. **Evaluación de Rendimiento:** Evaluar rendimiento de prompts: latencia, tokens consumidos, costos operativos, precisión y calidad de respuestas
5. **Gestión de Relaciones:** Asociar prompts con agents, models y sistemas RAG
6. **Trazabilidad Completa:** Mantener registro completo del ciclo de vida de cada prompt
7. **Workflow de Aprobación:** Integración automática con procesos de aprobación y validación

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 12 - Transparencia y Provisión de Información

Los prompts deben proporcionar:
- Información adecuada y relevante sobre el sistema
- Instrucciones de uso apropiadas
- Transparencia en el funcionamiento del prompt

#### Art. 14 - Registro de Datos

Requiere registro y documentación de:
- Prompts utilizados en sistemas de alto riesgo
- Versiones y cambios realizados
- Validaciones y aprobaciones

#### Art. 17 - Gestión de Datos

Los prompts deben cumplir con:
- Requisitos de calidad de datos
- Documentación de fuentes de datos
- Gestión de sesgos en prompts

#### Art. 19 - Registro de Actividad

Requiere registro inmutable de:
- Todas las ejecuciones de prompts
- Cambios realizados en prompts
- Decisiones tomadas basadas en prompts

#### Art. 20 - Monitoreo Post-Mercado

Requiere monitoreo continuo del rendimiento de prompts en producción para detectar:
- Degradación de rendimiento
- Problemas de seguridad
- Cambios en comportamiento

#### Art. 27 - Documentación Técnica

Requiere documentación técnica completa de:
- Prompts utilizados
- Versiones y cambios
- Validaciones realizadas

#### Art. 43 - Registro de Sistemas de Alto Riesgo

Requiere registro de prompts utilizados en sistemas de alto riesgo.

#### Art. 49 - Sanciones

Establece sanciones por incumplimiento de requisitos de gestión de prompts.

### ISO 42001 - Gestión de Sistemas de IA

#### Cláusula 8.2 - Diseño y Desarrollo

Requiere control de versiones y gestión de cambios en prompts de IA.

---

## 🚀 ¿PARA QUÉ SIRVE LA GESTIÓN DE PROMPTS?

### 1. **Cumplimiento Normativo**

El módulo facilita el cumplimiento de:
- **EU AI Act Art. 12, 14, 17, 19, 20, 27, 43, 49:** Transparencia, registro, gestión de datos, monitoreo y documentación
- **ISO 42001 8.2:** Control de versiones y gestión de cambios
- **Trazabilidad Regulatoria:** Registro completo de prompts para auditorías

### 2. **Gestión del Ciclo de Vida**

Proporciona herramientas para:
- **Registro Inicial:** Registrar nuevos prompts con información completa
- **Versionado Automático:** Control de versiones con semántica automática
- **Seguimiento de Cambios:** Historial completo de modificaciones
- **Análisis Continuo:** Análisis de cumplimiento y evaluación de rendimiento

### 3. **Análisis de Cumplimiento y Seguridad**

Permite:
- **Análisis de Cumplimiento Normativo:** Verificar cumplimiento con EU AI Act
- **Detección de Sesgos:** Identificar sesgos en prompts
- **Análisis de Seguridad:** Detectar vulnerabilidades de prompt injection
- **Validaciones Automáticas:** Ejecutar validaciones de forma automatizada

### 4. **Evaluación de Rendimiento**

Facilita:
- **Métricas de Rendimiento:** Latencia, tokens consumidos, costos
- **Calidad de Respuestas:** Precisión y calidad de las respuestas generadas
- **Análisis de Tendencias:** Identificar problemas de rendimiento tempranamente
- **Optimización:** Tomar decisiones informadas sobre mejoras de prompts

### 5. **Gestión de Relaciones**

Permite:
- **Asociación con Agents:** Gestionar qué agents usan cada prompt
- **Asociación con Models:** Gestionar qué models usan cada prompt
- **Asociación con RAG Systems:** Gestionar qué sistemas RAG usan cada prompt
- **Visibilidad Completa:** Ver todas las relaciones de un prompt en un solo lugar

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Registro de Prompts**

Pantalla principal para gestionar el catálogo de prompts:

#### **Listado de Prompts**
- **Vista Tabular:** Tabla paginada con todos los prompts registrados
- **Métricas Principales:**
  - Total de prompts registrados
  - Prompts activos
  - Prompts inactivos
  - Prompts pendientes de aprobación
- **Filtros Avanzados:**
  - Búsqueda por nombre o descripción
  - Filtro por estado (ACTIVE, INACTIVE, PENDING, DRAFT)
  - Filtro por proyecto asociado
- **Acciones:**
  - Crear nuevo prompt
  - Ver detalles del prompt
  - Editar prompt
  - Eliminar prompt

#### **Página de Detalle del Prompt**

Página completa con 7 pestañas para gestión detallada:

**1. Overview (Resumen):**
- Métricas principales: versión, número de validaciones, número de versiones, estado de aprobación
- Información general: proyecto asociado, fecha de creación, fecha de actualización, parámetros
- Vista rápida del estado del prompt

**2. Content (Contenido):**
- Visualización completa del contenido del prompt
- Editor de código con syntax highlighting
- Formato Prompty para estructura profesional
- Plantillas predefinidas disponibles

**3. Versions (Versiones):**
- Gestión completa de versiones del prompt
- Historial de todas las versiones
- Información de cada versión: número de versión, descripción, contenido, parámetros, cambios realizados, fecha
- **Versionado Semántico Automático:** Incrementa MAJOR.MINOR.PATCH automáticamente

**4. Validations (Validaciones):**
- Tabla con todas las validaciones realizadas
- Información: tipo de validación (COMPLIANCE, PERFORMANCE, SAFETY, BIAS), resultado (PASSED, FAILED, PENDING), score, issues encontrados, fecha
- Botón para actualizar lista de validaciones
- Badges visuales para tipos y resultados

**5. Project (Proyecto):**
- Información del proyecto asociado al prompt
- Nombre del proyecto e ID
- Botón para navegar al detalle del proyecto
- Información sobre cómo el proyecto afecta el cumplimiento normativo

**6. Agents (Agents):**
- Lista de agents asociados al prompt
- Información: nombre del agent, ID
- Botones para ver detalle del agent y desasociar
- Modal para agregar nuevos agents

**7. Models (Models):**
- Lista de models asociados al prompt
- Información: nombre del model, ID
- Botones para ver detalle del model y desasociar
- Modal para agregar nuevos models

**8. RAG Systems (RAG Systems):**
- Lista de sistemas RAG asociados al prompt
- Información: nombre del RAG system, ID
- Botones para ver detalle del RAG system y desasociar
- Modal para agregar nuevos RAG systems

#### **Página de Registro/Edición de Prompt**

Formulario completo para crear o editar prompts:

**Campos del Formulario:**
- **Nombre:** Nombre único del prompt (requerido)
- **Descripción:** Descripción detallada del prompt
- **Contenido:** Editor de código para el contenido del prompt (formato Prompty)
- **Parámetros:** Parámetros de configuración (temperature, max_tokens, etc.)
- **Versión:** Versión semántica (calculada automáticamente, solo lectura)
- **Estado:** Estado del prompt (DRAFT, ACTIVE, INACTIVE, PENDING)
- **Cambios/Notas:** Notas sobre los cambios realizados

**Funcionalidades Especiales:**
- **Editor de Código:** Editor con syntax highlighting para contenido del prompt
- **Plantilla Prompty:** Botón para cargar plantilla Prompty predefinida
- **Detección Automática de Cambios:** Si el contenido cambia, se crea nueva versión automáticamente
- **Botones de Análisis:**
  - **Analizar Cumplimiento:** Inicia análisis de cumplimiento normativo (EU AI Act), seguridad y detección de sesgos
  - **Evaluar Rendimiento:** Inicia evaluación de rendimiento (latencia, tokens, costos, calidad)
- **Gestión de Relaciones:** Sección para ver y gestionar agents, models y RAG systems asociados

### 2. **Validation Overview**

Pantalla para visualizar todas las validaciones de prompts:

#### **Listado de Validaciones**
- **Vista Tabular:** Tabla paginada con todas las validaciones
- **Métricas Principales:**
  - Total de validaciones
  - Validaciones activas
  - Validaciones pendientes
  - Validaciones inactivas
- **Filtros Avanzados:**
  - Búsqueda por término
  - Filtro por tipo de validación (COMPLIANCE, PERFORMANCE, SAFETY, BIAS)
  - Filtro por resultado (PASSED, FAILED, PENDING)
- **Información Mostrada:**
  - ID de validación
  - Tipo de validación
  - Resultado
  - Score de validación
  - Issues encontrados
  - Fecha de validación
- **Acciones:**
  - Ver detalles de validación
  - Eliminar validación
  - Registrar nueva validación

### 3. **Versioning Overview**

Pantalla para visualizar todas las versiones de prompts:

#### **Listado de Versiones**
- **Vista Tabular:** Tabla paginada con todas las versiones
- **Métricas Principales:**
  - Total de versiones
  - Versiones activas
  - Versiones pendientes
  - Versiones inactivas
- **Filtros Avanzados:**
  - Búsqueda por término
  - Filtro por estado
- **Información Mostrada:**
  - ID de versión
  - Versión (semver)
  - Descripción
  - Contenido del prompt
  - Parámetros
  - Cambios realizados
  - Fecha de creación
- **Acciones:**
  - Ver detalles de versión
  - Eliminar versión
  - Registrar nueva versión

### 4. **Performance Comparison**

Pantalla para comparar rendimiento de prompts:

#### **Métricas de Performance**
- **Métricas Principales:**
  - Total Items
  - Active Items
  - Deployed Items
  - Training Items
  - Offline Items
  - Average Score
- **Tabla de Comparación:**
  - Información de cada prompt
  - Métricas de rendimiento
  - Comparación visual
- **Filtros:**
  - Búsqueda por término
  - Limpieza de filtros
- **Acciones:**
  - Ver detalles de item

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Registro de un Nuevo Prompt

1. **Acceso al Registro**
   - Navegar a Prompts → Registry (o hacer clic en "Agregar Prompt")
   - Hacer clic en "Agregar Prompt"

2. **Completar Información Básica**
   - Completar formulario con:
     - Nombre del prompt (único, requerido)
     - Descripción del prompt
     - Contenido del prompt (usando editor de código)
     - Parámetros (temperature, max_tokens, etc.)
     - Estado inicial (DRAFT, ACTIVE, INACTIVE, PENDING)
   - **Versión se genera automáticamente (1.0.0)**

3. **Usar Plantilla Prompty (Opcional)**
   - Hacer clic en "Cargar Plantilla"
   - Se carga una plantilla Prompty predefinida
   - Personalizar según necesidades

4. **Asociar Proyecto (Opcional)**
   - El prompt puede asociarse a un proyecto
   - El proyecto determina los requisitos de cumplimiento normativo

5. **Guardar Prompt**
   - El sistema genera automáticamente:
     - UUID único para el prompt
     - Versión inicial (1.0.0)
   - Se dispara automáticamente el workflow de aprobación

6. **Navegación a Detalle**
   - Se redirige automáticamente a la página de detalle
   - Puede completar información adicional en otras pestañas

### Flujo 2: Análisis de Cumplimiento Normativo

1. **Acceder a Detalle del Prompt**
   - Desde el listado de prompts, hacer clic en "Ver Detalles"
   - O desde la página de registro/edición

2. **Iniciar Análisis**
   - Hacer clic en botón "Analizar Cumplimiento"
   - El sistema muestra mensaje de progreso con icono de carga

3. **Proceso de Análisis**
   - El sistema ejecuta análisis de:
     - **Cumplimiento Normativo:** Verificación de cumplimiento con EU AI Act
     - **Seguridad:** Detección de vulnerabilidades de prompt injection
     - **Sesgos:** Detección de sesgos en el prompt
   - Se crea automáticamente una validación de tipo COMPLIANCE

4. **Revisar Resultados**
   - Los resultados aparecen en la pestaña "Validations"
   - Información mostrada:
     - Tipo: COMPLIANCE
     - Resultado: PASSED, FAILED, o PENDING
     - Score de validación
     - Issues encontrados
     - Recomendaciones
   - Mensaje de éxito o error se muestra en la interfaz

### Flujo 3: Evaluación de Rendimiento

1. **Acceder a Detalle del Prompt**
   - Desde el listado de prompts, hacer clic en "Ver Detalles"

2. **Iniciar Evaluación**
   - Hacer clic en botón "Evaluar Rendimiento"
   - El sistema muestra mensaje de progreso con icono de carga

3. **Proceso de Evaluación**
   - El sistema ejecuta evaluación de:
     - **Latencia:** Tiempo de respuesta del prompt
     - **Tokens Consumidos:** Número de tokens utilizados
     - **Costos Operativos:** Costos asociados al uso del prompt
     - **Calidad de Respuestas:** Precisión y calidad de las respuestas generadas
   - Se crea automáticamente una validación de tipo PERFORMANCE

4. **Revisar Resultados**
   - Los resultados aparecen en la pestaña "Validations"
   - Información mostrada:
     - Tipo: PERFORMANCE
     - Resultado: PASSED, FAILED, o PENDING
     - Score de validación
     - Feedback y recomendaciones
   - Mensaje de éxito o error se muestra en la interfaz

### Flujo 4: Gestión de Versiones

1. **Acceder a Pestaña Versiones**
   - Desde la página de detalle del prompt
   - Seleccionar pestaña "Versions"

2. **Crear Nueva Versión**
   - Al editar el contenido del prompt y guardar
   - **La versión se calcula automáticamente:**
     - Si es corrección de errores → incrementa PATCH (1.0.0 → 1.0.1)
     - Si es nueva funcionalidad → incrementa MINOR (1.0.0 → 1.1.0)
     - Si es cambio incompatible → incrementa MAJOR (1.0.0 → 2.0.0)
   - Completar descripción de los cambios en campo "Cambios/Notas"

3. **Gestionar Versiones Existentes**
   - Ver lista de todas las versiones en la pestaña "Versions"
   - Ver información detallada de cada versión
   - Comparar versiones

### Flujo 5: Gestión de Relaciones (Agents, Models, RAG)

1. **Acceder a Pestaña de Relación**
   - Desde la página de detalle del prompt
   - Seleccionar pestaña "Agents", "Models" o "RAG Systems"

2. **Agregar Nueva Relación**
   - Hacer clic en botón "Agregar Agent/Model/RAG System"
   - Se abre modal con lista de elementos disponibles
   - Los elementos ya asociados se filtran automáticamente
   - Seleccionar elemento de la lista
   - Hacer clic en "Asociar"
   - La lista se actualiza automáticamente

3. **Desasociar Relación**
   - Desde la lista de relaciones
   - Hacer clic en botón "X" (eliminar) junto al elemento
   - La relación se elimina inmediatamente

4. **Ver Detalle de Elemento Relacionado**
   - Hacer clic en botón "Ver" junto al elemento
   - Se navega a la página de detalle del elemento (agent, model o RAG system)

### Flujo 6: Edición de Prompt

1. **Acceder a Edición**
   - Desde el listado de prompts, hacer clic en "Editar"
   - O desde la página de detalle, hacer clic en botón "Editar"

2. **Modificar Información**
   - El formulario se carga con la información actual
   - Modificar campos necesarios
   - **Importante:** Si se modifica el contenido, se creará automáticamente una nueva versión

3. **Análisis y Evaluación Durante Edición**
   - Los botones "Analizar Cumplimiento" y "Evaluar Rendimiento" están disponibles
   - Se pueden ejecutar análisis mientras se edita
   - Los resultados aparecen en mensajes visuales

4. **Guardar Cambios**
   - Si el contenido cambió, se crea nueva versión automáticamente
   - Si solo cambió metadata, se actualiza sin crear versión
   - Se muestra mensaje de confirmación

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **Prompt Engineers** 👨‍💻
- **Responsabilidad:** Desarrollo y optimización de prompts
- **Uso:** Registrar prompts, gestionar versiones, analizar cumplimiento, evaluar rendimiento
- **Beneficio:** Control centralizado de prompts, versionado automático, análisis de cumplimiento

#### 2. **AI Engineers** 🤖
- **Responsabilidad:** Desarrollo de sistemas de IA
- **Uso:** Registrar prompts, asociar con agents/models/RAG, monitorear rendimiento
- **Beneficio:** Gestión de relaciones, visibilidad completa de uso de prompts

#### 3. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo
- **Uso:** Revisar prompts registrados, verificar análisis de cumplimiento, auditorías
- **Beneficio:** Cumplimiento EU AI Act, trazabilidad regulatoria, validaciones automáticas

#### 4. **Project Managers** 📋
- **Responsabilidad:** Gestión de proyectos
- **Uso:** Consultar prompts asociados a proyectos, monitorear validaciones
- **Beneficio:** Visibilidad de prompts por proyecto, control de cumplimiento

#### 5. **DevOps Engineers** ⚙️
- **Responsabilidad:** Operación de sistemas
- **Uso:** Monitorear rendimiento de prompts, revisar métricas
- **Beneficio:** Visibilidad de rendimiento, detección temprana de problemas

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Centralización:** Registro único de todos los prompts utilizados
2. **Trazabilidad:** Historial completo del ciclo de vida de cada prompt
3. **Cumplimiento Normativo:** Facilita cumplimiento de EU AI Act e ISO 42001
4. **Análisis Automatizado:** Análisis de cumplimiento y evaluación de rendimiento automatizados
5. **Control de Versiones:** Versionado semántico automático y gestión estructurada
6. **Automatización:** Integración con workflows de aprobación
7. **Gobernanza:** Control centralizado de relaciones con agents, models y RAG systems

### Para los Usuarios

1. **Facilidad de Uso:** Interfaz intuitiva y organizada
2. **Automatización:** UUID y versiones se generan automáticamente
3. **Visibilidad:** Información completa en un solo lugar
4. **Análisis Integrado:** Análisis de cumplimiento y evaluación de rendimiento integrados
5. **Eficiencia:** Búsquedas y filtros avanzados
6. **Documentación:** Información estructurada y completa

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo para auditorías
2. **Cumplimiento EU AI Act:** Transparencia, registro, gestión de datos, monitoreo
3. **ISO 42001:** Control de versiones y gestión de cambios
4. **Evidencia:** Documentación completa de prompts y sus validaciones

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Workflow de Aprobación de Prompts

Cuando se registra un nuevo prompt, se dispara automáticamente el workflow de aprobación que incluye:

1. **Validaciones Automáticas:**
   - Compliance Validation
   - Safety Validation
   - Bias Detection
   - Performance Evaluation

2. **Revisiones Humanas:**
   - Prompt Engineer Review
   - Governance Review

3. **Decisión Final:**
   - Aprobación mediante reglas
   - Estados: APPROVED, CONDITIONAL_APPROVAL, REJECTED

### Integración con Proyectos

- Los prompts se pueden asociar a proyectos
- El proyecto determina los requisitos de cumplimiento normativo
- Seguimiento de uso de prompts por proyecto
- Análisis de cumplimiento basado en el proyecto

### Integración con Agents

- Los prompts se pueden asociar a múltiples agents
- Los agents pueden usar prompts para generar respuestas
- Gestión centralizada de relaciones

### Integración con Models

- Los prompts se pueden asociar a múltiples models
- Los models pueden usar prompts directamente en chat
- No requiere agent intermedio

### Integración con RAG Systems

- Los prompts se pueden asociar a múltiples sistemas RAG
- Los sistemas RAG pueden usar prompts para generar respuestas enriquecidas
- Gestión de conocimiento contextual

### Integración con Compliance

- Análisis de cumplimiento normativo integrado
- Validaciones automáticas de seguridad y sesgos
- Trazabilidad completa para auditorías

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **UUID Automático**
- Cada prompt recibe un UUID único generado automáticamente
- El UUID es inmutable y se usa para identificación única
- Campo de solo lectura

### 2. **Versionado Semántico Automático**
- Las versiones siguen el estándar semántico: MAJOR.MINOR.PATCH
- Se calculan automáticamente según el tipo de cambio
- No requiere entrada manual del usuario

### 3. **Análisis de Cumplimiento Integrado**
- Botón "Analizar Cumplimiento" ejecuta análisis completo
- Análisis de cumplimiento normativo (EU AI Act)
- Detección de vulnerabilidades de seguridad (prompt injection)
- Detección de sesgos
- Resultados se guardan como validaciones

### 4. **Evaluación de Rendimiento Integrada**
- Botón "Evaluar Rendimiento" ejecuta evaluación completa
- Métricas: latencia, tokens consumidos, costos, calidad
- Resultados se guardan como validaciones

### 5. **Gestión de Relaciones**
- Asociación con agents, models y RAG systems
- Modales para agregar relaciones fácilmente
- Filtrado automático de elementos ya asociados
- Desasociación simple con un clic

### 6. **Editor de Código**
- Editor con syntax highlighting para contenido del prompt
- Soporte para formato Prompty
- Plantillas predefinidas disponibles

### 7. **Detección Automática de Cambios**
- Si el contenido del prompt cambia, se crea nueva versión automáticamente
- Si solo cambia metadata, se actualiza sin crear versión
- Historial completo de cambios

---

## ❓ PREGUNTAS FRECUENTES

### ¿Cómo se genera el UUID del prompt?

El UUID se genera automáticamente cuando se crea un nuevo prompt. Es un identificador único inmutable que no puede ser modificado.

### ¿Cómo funciona el versionado semántico automático?

El sistema calcula automáticamente la siguiente versión basándose en el tipo de cambio:
- **PATCH** (1.0.0 → 1.0.1): Correcciones de errores
- **MINOR** (1.0.0 → 1.1.0): Nuevas funcionalidades compatibles
- **MAJOR** (1.0.0 → 2.0.0): Cambios incompatibles

### ¿Puedo editar el UUID o la versión del prompt?

No, tanto el UUID como la versión son campos calculados automáticamente y de solo lectura para garantizar la integridad y trazabilidad del sistema.

### ¿Qué pasa cuando registro un nuevo prompt?

Al registrar un nuevo prompt:
1. Se genera automáticamente el UUID y versión inicial (1.0.0)
2. Se dispara automáticamente el workflow de aprobación
3. El prompt queda en estado PENDING hasta que se complete la aprobación

### ¿Cuál es la diferencia entre "Analizar Cumplimiento" y "Evaluar Rendimiento"?

- **Analizar Cumplimiento:** Analiza cumplimiento normativo (EU AI Act), seguridad (prompt injection) y detección de sesgos. Crea una validación de tipo COMPLIANCE.
- **Evaluar Rendimiento:** Evalúa rendimiento del prompt: latencia, tokens consumidos, costos operativos, precisión y calidad de respuestas. Crea una validación de tipo PERFORMANCE.

### ¿Cómo gestiono las relaciones con agents, models y RAG systems?

1. Accede a la página de detalle del prompt
2. Selecciona la pestaña correspondiente (Agents, Models o RAG Systems)
3. Haz clic en "Agregar Agent/Model/RAG System"
4. Selecciona el elemento de la lista (los ya asociados se filtran automáticamente)
5. Haz clic en "Asociar"
6. Para desasociar, haz clic en el botón "X" junto al elemento

### ¿Puedo asociar un prompt a múltiples agents, models o RAG systems?

Sí, un prompt puede estar asociado a múltiples agents, múltiples models y múltiples RAG systems simultáneamente. Cada relación se gestiona de forma independiente.

### ¿Cómo veo el historial de versiones de un prompt?

En la página de detalle del prompt, accede a la pestaña "Versions". Ahí verás:
- Lista completa de todas las versiones
- Información de cada versión: número, descripción, contenido, parámetros, cambios, fecha

### ¿Cómo monitoreo las validaciones de un prompt?

En la página de detalle del prompt, accede a la pestaña "Validations". Ahí verás:
- Lista de todas las validaciones realizadas
- Tipo de validación (COMPLIANCE, PERFORMANCE, SAFETY, BIAS)
- Resultado (PASSED, FAILED, PENDING)
- Score de validación
- Issues encontrados
- Fecha de validación

### ¿Qué formato debo usar para el contenido del prompt?

Se recomienda usar el formato **Prompty** para una estructura profesional. Puedes usar el botón "Cargar Plantilla" para obtener una plantilla Prompty predefinida.

### ¿Cómo filtro prompts por estado o proyecto?

En el listado de prompts, utiliza los filtros superiores:
- Búsqueda por nombre o descripción
- Filtro por estado (ACTIVE, INACTIVE, PENDING, DRAFT)
- Filtro por proyecto asociado

### ¿Qué pasa si modifico el contenido de un prompt existente?

Si modificas el contenido del prompt y guardas:
- Se crea automáticamente una nueva versión
- La versión se calcula automáticamente según el tipo de cambio
- El historial de versiones se actualiza

Si solo modificas metadata (nombre, descripción, parámetros) sin cambiar el contenido:
- Se actualiza el prompt sin crear nueva versión
- La versión actual se mantiene

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DEL REGISTRO

Una vez que un prompt es registrado:

1. ✅ **Workflow Automático:** Se dispara automáticamente el workflow de aprobación
2. ✅ **Validaciones:** Se ejecutan validaciones automáticas (compliance, safety, bias)
3. ✅ **Revisiones:** Prompt Engineer y Governance revisan el prompt
4. ✅ **Aprobación:** Decisión final mediante reglas
5. ✅ **Asociación:** Puede asociarse con agents, models y RAG systems
6. ✅ **Monitoreo:** Monitoreo continuo en producción (validaciones, rendimiento)

---

**Última Actualización:** Diciembre 2025
**Versión del Módulo:** 1.0
**Estado:** ✅ Operativo y listo para producción
