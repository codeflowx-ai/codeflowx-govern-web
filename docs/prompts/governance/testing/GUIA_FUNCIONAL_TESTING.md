# 📘 GUÍA FUNCIONAL - MÓDULO DE TESTING Y VALIDACIÓN

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** ML Engineers, Data Scientists, QA Engineers, Compliance Officers, DevOps Teams

---

## 🎯 ¿QUÉ ES EL MÓDULO DE TESTING?

El **Módulo de Testing y Validación** es un sistema integral estilo MLflow que permite organizar, ejecutar y comparar pruebas de entidades de IA (Prompts, Modelos, Agentes, RAG, Datasets) a lo largo de todo su ciclo de vida. El módulo proporciona herramientas completas para experimentación, pruebas A/B, evaluación con LLMs como juez, y análisis comparativo de resultados.

### 🎯 Propósito Principal

El módulo de testing permite:

1. **Experimentos Organizados:** Crear y gestionar experimentos que agrupan múltiples pruebas relacionadas
2. **Runs de Prueba:** Ejecutar y registrar pruebas individuales con métricas, parámetros y artefactos
3. **Pruebas A/B:** Comparar dos versiones de una entidad usando el mismo dataset de prueba
4. **LLMs as Judge:** Usar modelos LLM como evaluadores objetivos para calificar respuestas
5. **Análisis Comparativo:** Comparar múltiples runs para identificar la mejor versión
6. **Trazabilidad Completa:** Mantener registro completo de todas las pruebas y resultados
7. **Integración con Análisis:** Conectar automáticamente con análisis de sesgo, explicabilidad y rendimiento

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 10 - Requisitos de Datos y Gobernanza de Datos

Los sistemas de IA de alto riesgo deben cumplir con requisitos de calidad de datos, incluyendo:
- Validación y verificación de datos de prueba
- Documentación de datasets de prueba
- Trazabilidad de resultados de pruebas

#### Art. 13 - Registro y Transparencia

Requiere documentación y registro de:
- Procesos de testing y validación
- Resultados de pruebas
- Métricas de rendimiento

#### Art. 15 - Requisitos de Transparencia y Provisión de Información

Los sistemas deben proporcionar:
- Evidencia de testing y validación
- Métricas de rendimiento documentadas
- Comparación de versiones

### ISO 42001 - Gestión de Sistemas de IA

#### Cláusula 8.2 - Diseño y Desarrollo

Requiere:
- Procesos de testing y validación estructurados
- Documentación de pruebas
- Control de versiones de entidades de prueba

#### Cláusula 9.1 - Monitoreo, Medición, Análisis y Evaluación

Requiere:
- Métricas de rendimiento
- Análisis comparativo
- Evaluación continua

---

## 🚀 ¿PARA QUÉ SIRVE EL MÓDULO DE TESTING?

### 1. **Cumplimiento Normativo**

El módulo facilita el cumplimiento de:
- **EU AI Act Art. 10, 13, 15:** Validación de datos, registro de pruebas, transparencia
- **ISO 42001 8.2, 9.1:** Procesos de testing estructurados, evaluación continua
- **Trazabilidad Regulatoria:** Registro completo de pruebas para auditorías

### 2. **Validación y Verificación**

Proporciona herramientas para:
- **Testing Sistemático:** Ejecutar pruebas estructuradas de entidades de IA
- **Validación de Cambios:** Verificar que nuevas versiones mejoran o mantienen calidad
- **Regresión:** Detectar degradación de rendimiento en nuevas versiones
- **Validación de Datasets:** Probar calidad y representatividad de datasets

### 3. **Optimización de Entidades**

Permite:
- **Comparación Objetiva:** Pruebas A/B para identificar la mejor versión
- **Evaluación con Juez:** LLMs como evaluadores objetivos e imparciales
- **Análisis de Métricas:** Comparar métricas entre versiones
- **Toma de Decisiones:** Basar decisiones en datos objetivos

### 4. **Monitoreo de Calidad**

Facilita:
- **Métricas en Tiempo Real:** KPIs de calidad, precisión, latencia
- **Análisis de Tendencias:** Evolución de métricas a lo largo del tiempo
- **Detección de Problemas:** Identificar degradación de calidad tempranamente
- **Benchmarking:** Comparar rendimiento contra estándares

### 5. **Experimentación Organizada**

Permite:
- **Organización por Experimentos:** Agrupar pruebas relacionadas
- **Versionado de Pruebas:** Mantener historial de todas las pruebas
- **Reproducibilidad:** Documentar parámetros y condiciones de cada prueba
- **Colaboración:** Compartir resultados de pruebas entre equipos

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Ejecutar Pruebas**

Pantalla principal para ejecutar diferentes tipos de pruebas:

#### **Selector de Tipo de Entidad**
- **Tarjetas de Entidades:**
  - Modelo
  - Prompt
  - Agente
  - RAG
  - Dataset
- Cada tarjeta navega a la página específica de ejecución

#### **Páginas de Ejecución por Entidad**

Cada entidad tiene su propia página de ejecución con formulario específico:

**1. Ejecutar Prueba de Modelo:**
- **Tipos de Prueba:**
  - Análisis de Sesgo (BIAS_ANALYSIS)
  - Explicabilidad (EXPLAINABILITY)
  - Rendimiento (PERFORMANCE)
  - Prueba A/B (AB_TEST)
  - LLM Judge (LLM_JUDGE)
- **Campos del Formulario:**
  - Experimento (existente o nuevo)
  - Modelo a probar
  - Para A/B: Modelo A y Modelo B
  - Dataset de prueba (CSV)
  - Parámetros específicos del tipo de prueba
  - Modelo juez (si aplica)
  - Criterio de evaluación

**2. Ejecutar Prueba de Prompt:**
- **Tipos de Prueba:**
  - Prueba de Prompt (PROMPT_TEST)
  - Prueba A/B (AB_TEST)
  - LLM Judge (LLM_JUDGE)
- **Campos del Formulario:**
  - Experimento (existente o nuevo)
  - Prompt a probar
  - Para A/B: Prompt A y Prompt B
  - Dataset de prueba (CSV)
  - Modelo juez (si aplica)
  - Criterio de evaluación (quality, accuracy, compliance, custom)
  - Prompt de evaluación personalizado (opcional)

**3. Ejecutar Prueba de Agente:**
- **Tipos de Prueba:**
  - Prueba de Agente (AGENT_TEST)
  - Prueba A/B (AB_TEST)
  - LLM Judge (LLM_JUDGE)
- **Campos del Formulario:**
  - Experimento (existente o nuevo)
  - Agente a probar
  - Para A/B: Agente A y Agente B
  - Dataset de prueba (CSV)
  - Modelo juez (si aplica)
  - Criterio de evaluación

**4. Ejecutar Prueba de RAG:**
- **Tipos de Prueba:**
  - Prueba de RAG (RAG_TEST)
  - Prueba A/B (AB_TEST)
  - LLM Judge (LLM_JUDGE)
- **Campos del Formulario:**
  - Experimento (existente o nuevo)
  - RAG a probar
  - Para A/B: RAG A y RAG B
  - Dataset de prueba (CSV)
  - Modelo juez (si aplica)
  - Criterio de evaluación

**5. Ejecutar Prueba de Dataset:**
- **Tipos de Prueba:**
  - Validación de Dataset (DATASET_VALIDATION)
  - Análisis de Calidad (DATASET_QUALITY)
- **Campos del Formulario:**
  - Experimento (existente o nuevo)
  - Dataset a validar
  - Criterios de validación
  - Métricas de calidad

### 2. **Experimentos**

Pantalla para gestionar experimentos:

#### **Listado de Experimentos**
- **Vista Tabular:** Tabla paginada con todos los experimentos
- **Métricas Principales:**
  - Total de experimentos
  - Experimentos activos
  - Experimentos archivados
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo de entidad (MODEL, PROMPT, AGENT, RAG, DATASET)
  - Filtro por estado (ACTIVE, ARCHIVED, DELETED)
- **Acciones:**
  - Crear nuevo experimento
  - Ver detalles del experimento
  - Editar experimento
  - Archivar experimento

#### **Página de Detalle del Experimento**

Página completa con información del experimento y sus runs:

**Información General:**
- Nombre del experimento
- Descripción
- Tipo de entidad
- ID de entidad asociada (opcional)
- Estado
- Tags (JSONB)
- Fechas de creación y actualización

**Runs del Experimento:**
- Tabla con todos los runs asociados
- Información: nombre, tipo, estado, fecha de inicio, duración
- Filtros por tipo de run y estado
- Acciones: ver detalles, comparar runs

**Acciones:**
- Crear nuevo run
- Editar experimento
- Archivar experimento

### 3. **Runs**

Pantalla para gestionar todos los runs (cross-entity):

#### **Listado de Runs**
- **Vista Tabular:** Tabla paginada con todos los runs
- **Métricas Principales:**
  - Total de runs
  - Runs completados
  - Runs en ejecución
  - Runs fallidos
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo de entidad
  - Filtro por tipo de run
  - Filtro por estado (RUNNING, COMPLETED, FAILED)
  - Filtro por rango de fechas
- **Acciones:**
  - Ver detalles del run
  - Comparar runs

#### **Página de Detalle del Run**

Página completa con información detallada del run:

**Información General:**
- Nombre y descripción del run
- Experimento asociado
- Tipo de run
- Tipo de entidad e ID
- Estado
- Fechas de inicio y fin
- Duración

**Métricas:**
- Métricas del run (JSONB)
- Visualización de métricas clave
- Gráficos de métricas (si aplica)

**Parámetros:**
- Parámetros de ejecución (JSONB)
- Visualización estructurada

**Artefactos:**
- Artefactos generados (JSONB)
- Enlaces a archivos, logs, etc.

**Resultados de Análisis:**
- Enlaces a análisis asociados:
  - Análisis de Sesgo (si aplica)
  - Explicabilidad (si aplica)
  - Rendimiento (si aplica)

**Evaluación del Juez (si aplica):**
- Score del juez
- Feedback
- Aspectos positivos
- Aspectos a mejorar
- Scores por criterio

**Errores:**
- Error y detalles (si el run falló)

### 4. **Comparar Runs**

Pantalla para comparar múltiples runs:

#### **Selector de Runs**
- Seleccionar 2 o más runs para comparar
- Filtros por experimento, tipo de entidad, tipo de run

#### **Vista Comparativa**
- **Tabla Comparativa:**
  - Métricas lado a lado
  - Diferencias porcentuales
  - Indicadores de mejor/peor
- **Gráficos Comparativos:**
  - Gráficos de barras para métricas
  - Gráficos de líneas para tendencias
- **Análisis:**
  - Ganador identificado
  - Mejoras porcentuales
  - Significancia estadística

### 5. **Resultados A/B**

Pantalla específica para resultados de pruebas A/B:

#### **Vista de Resultados A/B**
- **Resumen Principal:**
  - Ganador identificado (A, B, o TIE)
  - Badge con resultado
- **Comparación de Runs:**
  - Run A: score promedio, victorias, métricas
  - Run B: score promedio, victorias, métricas
  - Botones para ver runs individuales
- **Comparación Detallada:**
  - Significancia estadística
  - Mejoras porcentuales
  - Comparación de métricas individuales
- **Acciones:**
  - Ver run de comparación
  - Comparar runs en página de comparación

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Ejecutar Prueba A/B con LLM Judge

1. **Acceso a Ejecutar Prueba**
   - Navegar a Testing → Execute Test
   - Seleccionar tipo de entidad (Modelo, Prompt, Agente, RAG)

2. **Configurar Prueba A/B**
   - Seleccionar tipo de prueba: "Prueba A/B"
   - Completar formulario:
     - Experimento (existente o nuevo)
     - Entidad A (primera versión)
     - Entidad B (segunda versión)
     - Subir dataset CSV con casos de prueba
     - Seleccionar modelo juez (opcional pero recomendado)
     - Configurar criterio de evaluación (quality, accuracy, compliance, custom)
     - Prompt de evaluación personalizado (opcional)

3. **Ejecutar Prueba**
   - Hacer clic en "Ejecutar Prueba"
   - El sistema:
     - Crea o obtiene el experimento
     - Crea run A y run B
     - Parsea el dataset CSV
     - Ejecuta entidad A con cada caso de prueba
     - Ejecuta entidad B con cada caso de prueba
     - Si hay juez: evalúa cada resultado con el LLM juez
     - Calcula métricas agregadas
     - Compara resultados
     - Crea run de comparación

4. **Ver Resultados**
   - Redirección automática a página de resultados A/B
   - Visualización de:
     - Ganador identificado
     - Scores promedio
     - Número de victorias
     - Significancia estadística
     - Mejoras porcentuales
     - Comparación de métricas

5. **Análisis Detallado**
   - Ver run A individual
   - Ver run B individual
   - Ver run de comparación
   - Comparar runs en página de comparación

### Flujo 2: Ejecutar Prueba Individual con LLM Judge

1. **Acceso a Ejecutar Prueba**
   - Navegar a Testing → Execute Test
   - Seleccionar tipo de entidad

2. **Configurar Prueba Individual**
   - Seleccionar tipo de prueba (PROMPT_TEST, AGENT_TEST, etc.)
   - Completar formulario:
     - Experimento (existente o nuevo)
     - Entidad a probar
     - Subir dataset CSV
     - Seleccionar modelo juez
     - Configurar criterio de evaluación
     - Prompt de evaluación personalizado (opcional)

3. **Ejecutar Prueba**
   - Hacer clic en "Ejecutar Prueba"
   - El sistema:
     - Crea o obtiene el experimento
     - Crea run
     - Parsea el dataset CSV
     - Ejecuta entidad con cada caso de prueba
     - Evalúa cada resultado con el LLM juez
     - Calcula métricas agregadas
     - Completa el run

4. **Ver Resultados**
   - Redirección automática a página de detalle del run
   - Visualización de:
     - Métricas del run
     - Evaluaciones del juez (score, feedback, aspectos positivos/mejoras)
     - Parámetros y artefactos

### Flujo 3: Crear y Gestionar Experimento

1. **Crear Experimento**
   - Navegar a Testing → Experiments
   - Hacer clic en "Nuevo Experimento"
   - Completar formulario:
     - Nombre del experimento
     - Descripción
     - Tipo de entidad (MODEL, PROMPT, AGENT, RAG, DATASET)
     - ID de entidad asociada (opcional)
     - Tags (opcional)

2. **Gestionar Experimento**
   - Ver lista de runs del experimento
   - Crear nuevo run desde el experimento
   - Editar información del experimento
   - Archivar experimento

3. **Análisis del Experimento**
   - Ver todos los runs asociados
   - Comparar runs del experimento
   - Analizar tendencias de métricas

### Flujo 4: Comparar Múltiples Runs

1. **Acceso a Comparar**
   - Navegar a Testing → Compare Runs
   - O desde la página de detalle de un experimento

2. **Seleccionar Runs**
   - Seleccionar 2 o más runs para comparar
   - Filtros por experimento, tipo de entidad, tipo de run

3. **Ver Comparación**
   - Tabla comparativa con métricas lado a lado
   - Gráficos comparativos
   - Análisis de diferencias
   - Identificación de ganador

### Flujo 5: Análisis de Sesgo, Explicabilidad y Rendimiento (Modelos)

1. **Ejecutar Análisis de Modelo**
   - Navegar a Testing → Execute Test → Model
   - Seleccionar tipo: BIAS_ANALYSIS, EXPLAINABILITY, o PERFORMANCE

2. **Configurar Análisis**
   - Seleccionar modelo
   - Configurar parámetros específicos del análisis
   - Subir dataset si es necesario

3. **Ver Resultados**
   - El sistema crea automáticamente:
     - Experimento para el modelo
     - Run de tipo específico
     - Asociación con resultado de análisis (ModelBiasAnalysis, ModelExplainability, ModelPerformance)
   - Visualización en página de detalle del run
   - Enlaces a análisis detallados

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **ML Engineers** 👨‍💻
- **Responsabilidad:** Desarrollo y validación de modelos
- **Uso:** Ejecutar pruebas de modelos, análisis de sesgo/explicabilidad/rendimiento, pruebas A/B
- **Beneficio:** Validación sistemática, comparación objetiva, identificación de mejor versión

#### 2. **Data Scientists** 📊
- **Responsabilidad:** Desarrollo y validación de prompts y modelos
- **Uso:** Ejecutar pruebas de prompts, pruebas A/B, evaluación con juez LLM
- **Beneficio:** Optimización de prompts, validación de cambios, evaluación objetiva

#### 3. **QA Engineers** 🧪
- **Responsabilidad:** Aseguramiento de calidad de entidades de IA
- **Uso:** Ejecutar pruebas sistemáticas, validar datasets, comparar versiones
- **Beneficio:** Testing estructurado, trazabilidad completa, reportes de calidad

#### 4. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo
- **Uso:** Revisar experimentos y runs, verificar trazabilidad, auditorías
- **Beneficio:** Cumplimiento EU AI Act, trazabilidad regulatoria, evidencia de testing

#### 5. **DevOps Engineers** ⚙️
- **Responsabilidad:** Operación y monitoreo de sistemas de IA
- **Uso:** Monitorear métricas de runs, analizar rendimiento, validar despliegues
- **Beneficio:** Visibilidad de calidad, detección temprana de problemas

#### 6. **Product Managers** 📋
- **Responsabilidad:** Gestión de productos con IA
- **Uso:** Revisar resultados de pruebas A/B, tomar decisiones basadas en datos
- **Beneficio:** Decisiones informadas, optimización de productos

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Validación Sistemática:** Procesos estructurados de testing y validación
2. **Trazabilidad:** Registro completo de todas las pruebas y resultados
3. **Cumplimiento Normativo:** Facilita cumplimiento de EU AI Act e ISO 42001
4. **Optimización:** Identificación objetiva de mejores versiones mediante A/B testing
5. **Evaluación Objetiva:** LLMs como jueces imparciales y consistentes
6. **Reproducibilidad:** Documentación completa de parámetros y condiciones
7. **Colaboración:** Compartir resultados entre equipos

### Para los Usuarios

1. **Facilidad de Uso:** Interfaz intuitiva y organizada por entidades
2. **Automatización:** Creación automática de experimentos y runs
3. **Visibilidad:** Información completa en un solo lugar
4. **Comparación:** Herramientas visuales para comparar versiones
5. **Feedback Estructurado:** Evaluaciones del juez con feedback detallado
6. **Eficiencia:** Búsquedas y filtros avanzados

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo para auditorías
2. **Cumplimiento EU AI Act:** Validación de datos, registro de pruebas, transparencia
3. **ISO 42001:** Procesos de testing estructurados, evaluación continua
4. **Evidencia:** Documentación completa de pruebas y resultados

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Integración con Análisis de Modelos

Cuando se ejecuta un análisis de sesgo, explicabilidad o rendimiento de un modelo:
- Se crea automáticamente un experimento para el modelo
- Se crea un run de tipo específico (BIAS_ANALYSIS, EXPLAINABILITY, PERFORMANCE)
- Se asocia el resultado del análisis con el run
- Las métricas del análisis se almacenan en el run

### Integración con Gestión de Modelos

- Los runs de modelos se pueden asociar a modelos específicos
- Las métricas de runs se pueden visualizar en la página del modelo
- Los experimentos se pueden filtrar por modelo

### Integración con Gestión de Prompts

- Los runs de prompts se pueden asociar a prompts específicos
- Las pruebas A/B permiten comparar versiones de prompts
- Los resultados ayudan a optimizar prompts

### Integración con Gestión de Agentes

- Los runs de agentes se pueden asociar a agentes específicos
- Las pruebas A/B permiten comparar versiones de agentes
- Los resultados ayudan a optimizar agentes

### Integración con Workflows

- Los resultados de pruebas pueden disparar workflows de aprobación
- Los runs pueden estar asociados a instancias de workflow
- Los estados de runs pueden sincronizarse con estados de workflow

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **Sistema de Experimentos y Runs (Estilo MLflow)**

- **Experimentos:** Contenedores que agrupan runs relacionados
- **Runs:** Ejecuciones individuales de pruebas con:
  - Métricas (JSONB)
  - Parámetros (JSONB)
  - Artefactos (JSONB)
  - Tags (JSONB)
  - Estado y duración
- **Trazabilidad Completa:** Historial completo de todas las pruebas

### 2. **Pruebas A/B Automatizadas**

- Comparación automática de dos versiones
- Ejecución en paralelo o secuencial
- Cálculo automático de métricas comparativas
- Determinación automática de ganador
- Significancia estadística

### 3. **LLMs as Judge**

- Evaluación objetiva con modelos LLM
- Criterios configurables (quality, accuracy, compliance, custom)
- Prompts personalizables
- Feedback estructurado:
  - Score numérico (0.0 - 1.0)
  - Feedback textual
  - Aspectos positivos
  - Aspectos a mejorar
  - Scores por criterio específico

### 4. **Parseo Inteligente de CSV**

- Detección automática de columnas de input/output
- Soporte para campos con comillas
- Manejo de diferentes encodings
- Validación de formato

### 5. **Visualización Comparativa**

- Gráficos de métricas lado a lado
- Tablas comparativas
- Indicadores de mejor/peor
- Mejoras porcentuales
- Significancia estadística

### 6. **Integración Automática con Análisis**

- Los análisis de modelos (sesgo, explicabilidad, rendimiento) se integran automáticamente
- Creación automática de experimentos y runs
- Asociación automática de resultados

---

## ❓ PREGUNTAS FRECUENTES

### ¿Qué es un experimento?

Un experimento es un contenedor que agrupa múltiples runs relacionados. Por ejemplo, un experimento puede ser "Optimización de Prompt de Clasificación" y contener varios runs de pruebas de diferentes versiones del prompt.

### ¿Qué es un run?

Un run es una ejecución individual de una prueba. Cada run tiene:
- Nombre y descripción
- Tipo de run (PROMPT_TEST, AB_TEST, BIAS_ANALYSIS, etc.)
- Métricas, parámetros y artefactos
- Estado (RUNNING, COMPLETED, FAILED)
- Fechas de inicio y fin

### ¿Cómo funciona una prueba A/B?

1. Seleccionas dos entidades del mismo tipo (ej: Prompt A y Prompt B)
2. Subes un dataset CSV con casos de prueba
3. El sistema ejecuta ambas entidades con cada caso
4. Si hay juez LLM, evalúa cada resultado
5. Compara métricas y determina ganador
6. Muestra resultados comparativos

### ¿Qué es un LLM Judge?

Un LLM Judge es un modelo de lenguaje grande (como GPT-4 o Claude) que actúa como evaluador objetivo. Evalúa las respuestas de otras entidades según criterios específicos (calidad, precisión, cumplimiento) y proporciona scores y feedback estructurado.

### ¿Puedo usar un LLM Judge sin hacer prueba A/B?

Sí, puedes ejecutar una prueba individual y usar un LLM Judge para evaluar los resultados. El juez evaluará cada resultado y proporcionará scores y feedback.

### ¿Qué formato debe tener el dataset CSV?

El CSV debe tener al menos una columna de input. Columnas soportadas:
- `input`, `entrada`, `query`, `text` → Input a probar
- `expected_output`, `esperado`, `output` → Output esperado (opcional)

Ejemplo:
```csv
input,expected_output
"¿Qué es la IA?","La Inteligencia Artificial es..."
"¿Cómo funciona ML?","Machine Learning es..."
```

### ¿Cómo se determina el ganador en una prueba A/B?

El ganador se determina considerando:
- Score promedio del juez (70% del peso)
- Número de victorias individuales (30% del peso)
- Significancia estadística

### ¿Puedo comparar más de 2 runs?

Sí, en la página "Comparar Runs" puedes seleccionar múltiples runs para comparar. Se mostrará una tabla comparativa con todas las métricas lado a lado.

### ¿Qué tipos de pruebas puedo ejecutar para modelos?

Para modelos puedes ejecutar:
- **BIAS_ANALYSIS:** Análisis de sesgo
- **EXPLAINABILITY:** Análisis de explicabilidad
- **PERFORMANCE:** Análisis de rendimiento
- **AB_TEST:** Prueba A/B comparando dos modelos
- **LLM_JUDGE:** Evaluación con juez LLM

### ¿Qué tipos de pruebas puedo ejecutar para prompts?

Para prompts puedes ejecutar:
- **PROMPT_TEST:** Prueba individual del prompt
- **AB_TEST:** Prueba A/B comparando dos prompts
- **LLM_JUDGE:** Evaluación con juez LLM

### ¿Puedo archivar un experimento?

Sí, puedes archivar un experimento desde su página de detalle. Los experimentos archivados no se eliminan pero se marcan como ARCHIVED y pueden filtrarse en el listado.

### ¿Cómo veo las métricas de un run?

En la página de detalle del run, verás:
- Métricas principales en formato estructurado
- Gráficos de métricas (si aplica)
- Métricas completas en formato JSON (expandible)

### ¿Qué pasa si un run falla?

Si un run falla:
- El estado cambia a FAILED
- Se registra el error y detalles del error
- Puedes ver el error en la página de detalle del run
- Puedes intentar ejecutar nuevamente

### ¿Puedo ejecutar pruebas sin juez LLM?

Sí, puedes ejecutar pruebas sin juez LLM. Sin embargo, para pruebas A/B se recomienda usar un juez para obtener comparaciones más objetivas.

### ¿Cómo se calcula la significancia estadística?

Se usa un t-test simplificado que compara las distribuciones de scores entre los dos runs. El resultado es un valor entre 0.0 y 1.0 que indica el nivel de confianza en la diferencia observada.

### ¿Puedo exportar los resultados de una prueba?

Actualmente los resultados se pueden visualizar en la interfaz. La exportación a PDF/CSV está planificada para futuras versiones.

### ¿Cómo filtro runs por tipo o estado?

En el listado de runs, utiliza los filtros superiores:
- Búsqueda por nombre
- Filtro por tipo de entidad
- Filtro por tipo de run
- Filtro por estado (RUNNING, COMPLETED, FAILED)
- Filtro por rango de fechas

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DE EJECUTAR UNA PRUEBA

Una vez que una prueba es ejecutada:

1. ✅ **Run Creado:** Se crea automáticamente el run con toda la información
2. ✅ **Métricas Calculadas:** Se calculan y almacenan todas las métricas
3. ✅ **Evaluación del Juez:** Si hay juez, se evalúan todos los resultados
4. ✅ **Comparación (A/B):** Si es A/B, se compara y determina ganador
5. ✅ **Visualización:** Resultados disponibles inmediatamente en la interfaz
6. ✅ **Análisis:** Puedes analizar resultados, comparar con otros runs
7. ✅ **Toma de Decisiones:** Basar decisiones en resultados objetivos

---

**Última Actualización:** Enero 2025
**Versión del Módulo:** 1.0
**Estado:** ✅ Operativo y listo para producción
