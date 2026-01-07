# 📘 GUÍA FUNCIONAL - GOBIERNO DEL DATO (DATA GOVERNANCE)

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Data Scientists, Data Engineers, Compliance Officers, Governance Managers, Project Managers, Auditors

---

## 🎯 ¿QUÉ ES EL GOBIERNO DEL DATO?

El **Gobierno del Dato (Data Governance)** es un módulo integral que permite gestionar, monitorear y asegurar la calidad, privacidad, trazabilidad y cumplimiento normativo de todos los datasets utilizados en la organización. El módulo proporciona herramientas completas para el registro de datasets, gestión de orígenes de datos, análisis de calidad, gestión de riesgos, cumplimiento GDPR y trazabilidad completa.

### 🎯 Propósito Principal

El módulo de Gobierno del Dato permite:

1. **Registro Centralizado:** Mantener un catálogo único y centralizado de todos los datasets utilizados en la organización
2. **Gestión de Orígenes:** Administrar orígenes de datos internos y externos (HuggingFace, Kaggle, APIs, bases de datos)
3. **Estandarización Automática:** Conversión automática a formato Apache Parquet para análisis uniforme
4. **Análisis de Calidad:** Evaluación de calidad según 6 dimensiones ISO 8000 (Completitud, Precisión, Consistencia, Validez, Puntualidad, Unicidad)
5. **Gestión de Riesgos:** Identificación, evaluación y mitigación de riesgos de calidad, sesgos, seguridad, privacidad y compliance
6. **Cumplimiento GDPR:** Gestión de PII, consentimiento, DPIA y derechos de los interesados
7. **Trazabilidad Completa:** Línea de base completa con historial de transformaciones y decisiones
8. **Documentación:** Registro de decisiones, transformaciones y auditorías

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 10 - Requisitos de Datos y Gobernanza de Datos

Los sistemas de IA de alto riesgo deben cumplir con:
- **Gestión y gobernanza de datos:** Datos de entrenamiento, validación y prueba deben ser gestionados y documentados
- **Calidad de datos:** Los datos deben ser relevantes, representativos, exactos y completos
- **Detección y corrección de sesgos:** Identificación y mitigación de sesgos en los datos
- **Documentación:** Registro completo de datos de entrenamiento, validación y prueba

#### Art. 13 - Registro y Transparencia

Requiere documentación completa de:
- Datos utilizados para entrenamiento
- Origen de los datos
- Transformaciones aplicadas
- Decisiones tomadas sobre los datos

### GDPR - Reglamento General de Protección de Datos

#### Art. 6 - Base Legal del Procesamiento

Requiere identificación de la base legal para el procesamiento de datos personales.

#### Art. 7 - Condiciones para el Consentimiento

Requiere gestión de consentimiento explícito, informado y verificable.

#### Art. 35 - Evaluación de Impacto en la Protección de Datos (DPIA)

Requiere evaluación de impacto cuando el procesamiento pueda entrañar un alto riesgo para los derechos y libertades.

### ISO 8000 - Calidad de Datos

#### 6 Dimensiones de Calidad

1. **Completitud:** Grado en que los datos están completos
2. **Precisión:** Grado en que los datos son correctos
3. **Consistencia:** Grado en que los datos son consistentes
4. **Validez:** Grado en que los datos cumplen reglas de negocio
5. **Puntualidad:** Grado en que los datos están disponibles cuando se necesitan
6. **Unicidad:** Grado en que no hay duplicados

### ISO/IEC 38505-1:2017 - Gobernanza de Datos

Principios para la gobernanza efectiva de datos en organizaciones.

---

## 🚀 ¿PARA QUÉ SIRVE EL GOBIERNO DEL DATO?

### 1. **Cumplimiento Normativo**

El módulo facilita el cumplimiento de:
- **EU AI Act Art. 10, 13:** Gestión de datos, calidad, documentación y trazabilidad
- **GDPR Art. 6, 7, 35:** Base legal, consentimiento y DPIA
- **ISO 8000:** 6 dimensiones de calidad de datos
- **ISO/IEC 38505-1:** Principios de gobernanza de datos
- **Trazabilidad Regulatoria:** Registro completo para auditorías

### 2. **Gestión del Ciclo de Vida de Datos**

Proporciona herramientas para:
- **Registro Inicial:** Registrar nuevos datasets con información completa
- **Estandarización:** Conversión automática a Apache Parquet
- **Análisis Continuo:** Monitoreo de calidad, sesgos y compliance
- **Versionado:** Control de versiones de datasets
- **Aprobación:** Workflow de aprobación para uso en producción

### 3. **Calidad y Confiabilidad**

Permite:
- **Análisis de Calidad:** Evaluación según 6 dimensiones ISO 8000
- **Detección de Problemas:** Identificación temprana de problemas de calidad
- **Métricas Detalladas:** KPIs de calidad por dimensión
- **Alertas:** Notificaciones cuando la calidad está por debajo del umbral

### 4. **Gestión de Riesgos**

Facilita:
- **Identificación:** Registro de riesgos de calidad, sesgos, seguridad, privacidad
- **Evaluación:** Matriz de riesgos (probabilidad × impacto)
- **Mitigación:** Planes de mitigación y seguimiento
- **Monitoreo:** Seguimiento del estado de riesgos

### 5. **Privacidad y GDPR**

Permite:
- **Detección de PII:** Identificación automática de datos personales
- **Gestión de Consentimiento:** Registro y seguimiento de consentimientos
- **DPIA:** Evaluación de impacto en la protección de datos
- **Derechos de los Interesados:** Gestión de solicitudes de acceso, rectificación, supresión

### 6. **Trazabilidad y Auditoría**

Facilita:
- **Línea de Base:** Historial completo de transformaciones
- **Documentación:** Registro de decisiones y cambios
- **Auditoría:** Trazabilidad completa para auditorías regulatorias

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Dashboard de Gobierno del Dato**

Pantalla principal con vista consolidada:

#### **KPIs Principales**
- **Total Datasets:** Número total de datasets registrados
- **Orígenes de Datos:** Número de orígenes configurados
- **Calidad Promedio:** Score promedio de calidad de todos los datasets
- **Compliance:** Número de datasets que cumplen con normativas

#### **Alertas y Riesgos**
- **Datasets con Riesgos:** Lista de datasets que requieren atención
- **Calidad Baja:** Datasets con calidad por debajo del umbral
- **PII Detectado:** Datasets con datos personales identificados

#### **Métricas de Calidad**
- Score global de calidad
- Distribución por dimensiones ISO 8000
- Alertas de datasets bajo umbral

#### **Accesos Rápidos**
- Enlaces directos a todas las secciones del módulo

### 2. **Gestión de Datasets**

#### **Listado de Datasets** (`/governance/data/datasets/overview`)

Pantalla principal para gestionar el catálogo de datasets:

- **Vista Tabular:** Tabla paginada con todos los datasets registrados
- **Métricas Principales:**
  - Total de datasets registrados
  - Datasets activos
  - Datasets pendientes de aprobación
  - Datasets con problemas de calidad
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo (TRAINING, VALIDATION, TEST, PRODUCTION, RAG)
  - Filtro por origen (INTERNAL, EXTERNAL)
  - Filtro por estado (DRAFT, PENDING, APPROVED, REJECTED)
  - Filtro por calidad (bajo umbral, aceptable, excelente)
- **Acciones:**
  - Crear nuevo dataset
  - Ver detalles del dataset
  - Analizar dataset
  - Aprobar/Rechazar dataset

#### **Crear Dataset** (`/governance/data/datasets/create`)

Formulario completo para registrar un nuevo dataset:

**Campos del Formulario:**
- **Nombre del Dataset:** Nombre único identificador
- **Descripción:** Descripción detallada del dataset
- **Tipo de Dataset:** TRAINING, VALIDATION, TEST, PRODUCTION, RAG
- **Tipo de Origen:** INTERNAL, EXTERNAL
- **Origen:** Selección del origen de datos (si ya existe) o creación de nuevo
- **Proyecto Asociado:** Asociación opcional a un proyecto
- **Fuente de Datos:**
  - **HuggingFace:** Selección de dataset de HuggingFace
  - **Kaggle:** Selección de dataset de Kaggle
  - **API:** Configuración de endpoint API
  - **Upload:** Carga directa de archivo
- **Estandarización Automática:** Checkbox para convertir automáticamente a Parquet
- **Formato Original:** CSV, JSON, PARQUET, AVRO, XML

**Proceso de Creación:**
1. Completar información básica
2. Seleccionar o crear origen
3. Configurar fuente de datos
4. Activar estandarización automática (recomendado)
5. Guardar → Se dispara workflow de aprobación

#### **Página de Detalle del Dataset** (`/governance/data/datasets/[id]`)

Página completa con **10 pestañas** para gestión detallada:

**1. Información General:**
- Formulario editable con toda la información del dataset
- Campos: nombre, descripción, tipo, origen, formato, estado
- **ID del Dataset (UUID):** Generado automáticamente, campo de solo lectura
- **Versión:** Control de versiones del dataset
- **Métricas Básicas:** Tamaño, número de registros, checksum
- **Estado de Aprobación:** Información sobre aprobación y aprobador

**2. Orígenes:**
- Lista de orígenes asociados al dataset
- Información de conexión y sincronización
- Última sincronización y estado
- Acciones: Sincronizar, Probar conexión

**3. Calidad:**
- **6 Dimensiones ISO 8000:**
  - Completitud: Porcentaje de valores no nulos
  - Precisión: Grado de exactitud de los datos
  - Consistencia: Coherencia entre campos relacionados
  - Validez: Cumplimiento de reglas de negocio
  - Puntualidad: Actualización y disponibilidad
  - Unicidad: Ausencia de duplicados
- **Score por Dimensión:** Visualización con barras de progreso
- **Estado:** PASS, WARNING, FAIL por cada dimensión
- **Umbrales:** Configuración de umbrales mínimos
- **Detalles:** Información detallada de cada métrica

**4. Sesgos y Representatividad:**
- **Score de Sesgos:** Métrica general de sesgos detectados
- **Análisis de Representatividad:** Distribución por grupos demográficos
- **Detección de Sesgos:** Identificación de sesgos por categoría
- **Gráficos:** Visualización de distribución y representatividad

**5. Línea de Base:**
- **Dos Modos de Visualización:**
  - **Vista Gráfica Interactiva:** Diagrama SVG con nodos (datasets y transformaciones) y aristas (dependencias)
    - Layout circular: Dataset central con transformaciones distribuidas alrededor
    - Interactividad: Click en nodos para ver detalles en panel lateral
    - Iconos específicos por tipo de transformación (GitBranch, Network, Filter, GitMerge, etc.)
    - Colores diferenciados por tipo de transformación
  - **Vista Tabular:** Historial completo en formato de lista (vista original)
- **Timeline de Transformaciones:** Historial completo de cambios
- **Transformaciones:**
  - Tipo: FILTER, JOIN, AGGREGATION, CLEANING, TRANSFORMATION, SPLIT, MERGE
  - Detalles: Descripción y parámetros de cada transformación
  - Versión: Versión del dataset después de la transformación
  - Fecha y Usuario: Quién y cuándo se realizó
- **Dependencias:** Datasets padre e hijos
- **Visualización:** Árbol de dependencias con grafo interactivo

**6. Riesgos:**
- **Lista de Riesgos:** Todos los riesgos identificados para el dataset
- **Tipos de Riesgo:**
  - QUALITY: Problemas de calidad de datos
  - BIAS: Sesgos detectados
  - SECURITY: Vulnerabilidades de seguridad
  - PRIVACITY: Problemas de privacidad
  - COMPLIANCE: Incumplimientos normativos
  - LEGAL: Riesgos legales
- **Matriz de Riesgos:** Visualización de probabilidad × impacto
- **Gestión de Riesgos:**
  - Agregar nuevo riesgo
  - Editar riesgo existente
  - Plan de mitigación
  - Estado: IDENTIFIED, ASSESSED, TREATED, MONITORED, CLOSED
- **Score de Riesgo:** Cálculo automático basado en probabilidad e impacto

**7. Privacidad:**
- **Detección de PII:** Información sobre datos personales detectados
- **Análisis de PII:** Detalle de qué tipos de PII están presentes
- **Base Legal (GDPR Art. 6):** Identificación de la base legal del procesamiento
- **Gestión de Consentimiento (GDPR Art. 7):**
  - Registro de consentimientos
  - Estado y fecha de consentimiento
  - Verificación de consentimiento
- **Política de Retención:** Configuración de tiempo de retención
- **DPIA (GDPR Art. 35):**
  - Evaluación de impacto en la protección de datos
  - Resultado y recomendaciones
- **Derechos de los Interesados:**
  - Gestión de solicitudes de acceso, rectificación, supresión
  - Estado de solicitudes

**8. Documentación:**
- **Timeline de Decisiones:** Historial de decisiones importantes
- **Tipos de Documentación:**
  - DECISION_LOG: Registro de decisiones
  - TRANSFORMATION_DOC: Documentación de transformaciones
  - AUDIT_REPORT: Informes de auditoría
  - OTHER: Otra documentación
- **Agregar Documentación:**
  - Título y descripción
  - Tipo de documentación
  - Contenido o archivo adjunto
  - Tags para búsqueda
- **Búsqueda:** Filtros por tipo y fecha

**9. Análisis de Impacto:**
- **Análisis de Dependencias:** Identificación de datasets y orígenes afectados por cambios
- **Métricas de Impacto:**
  - Total de datasets afectados
  - Impacto alto (crítico)
  - Orígenes de datos relacionados
- **Clasificación de Impacto:**
  - HIGH: Impacto crítico (dependencia directa)
  - MEDIUM: Impacto moderado (dependencia indirecta)
  - LOW: Impacto bajo (referencia indirecta)
- **Visualización:**
  - Cards con información detallada de cada dataset afectado
  - Badges de impacto con colores (rojo/amarillo/verde)
  - Razones de impacto explicadas
  - Enlaces directos a datasets y orígenes afectados
- **Funcionalidad:**
  - Botón "Recalcular Impacto" para análisis en tiempo real
  - Información contextual sobre qué es el análisis de impacto
- **Uso:** Permite planificar cambios con conocimiento de impacto, evitar romper dependencias críticas y mostrar claramente el ecosistema de datos

**10. Compliance:**
- **Estado de Compliance:** Cumplimiento con normativas
- **Checklist de Compliance:**
  - EU AI Act Art. 10: Gestión de datos
  - GDPR Art. 6, 7, 35: Privacidad
  - ISO 8000: Calidad de datos
- **Alertas:** Incumplimientos identificados
- **Integración con Proyectos de Compliance:** Enlaces a proyectos de cumplimiento asociados

**11. Roles y Responsabilidades:**
- **Gestión de Roles por Dataset:** Asignación de roles específicos para cada dataset
- **Tipos de Roles:**
  - OWNER: Propietario del dataset
  - STEWARD: Data Steward responsable
  - COMPLIANCE_OFFICER: Oficial de cumplimiento
  - DATA_SCIENTIST: Científico de datos
  - REVIEWER: Revisor
  - APPROVER: Aprobador
- **Permisos Granulares por Acción:**
  - **Lectura (Read):** Ver y consultar el dataset
  - **Escritura (Write):** Modificar datos y configuración
  - **Eliminación (Delete):** Eliminar el dataset
  - **Aprobación (Approve):** Aprobar cambios y decisiones
- **Visualización:**
  - Tarjetas con información de cada rol asignado
  - Badges con iconos mostrando permisos activos
  - Indicadores visuales claros de capacidades por usuario
- **Gestión:**
  - Asignar nuevo rol con permisos específicos
  - Editar rol existente y sus permisos
  - Eliminar rol
  - Estado: ACTIVE, INACTIVE

#### **Análisis de Dataset** (`/governance/data/datasets/[id]/analyze`)

Pantalla dedicada para análisis profundo:

**3 Pestañas de Análisis:**

1. **Análisis de Calidad:**
   - Ejecutar análisis completo de calidad
   - Visualización de resultados por dimensión
   - Recomendaciones de mejora

2. **Análisis de Sesgos:**
   - Ejecutar análisis de sesgos
   - Visualización de distribución
   - Identificación de grupos subrepresentados

3. **Análisis de Compliance:**
   - Verificación de cumplimiento normativo
   - Checklist de requisitos
   - Reporte de compliance

**Acciones:**
- Botones para ejecutar cada tipo de análisis
- Visualización de resultados
- Exportar reportes

### 3. **Gestión de Orígenes de Datos**

#### **Listado de Orígenes** (`/governance/data/origins`)

Pantalla para gestionar orígenes de datos:

- **Vista Tabular:** Tabla paginada con todos los orígenes
- **Métricas Principales:**
  - Total de orígenes
  - Orígenes activos
  - Orígenes con errores de conexión
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo (INTERNAL, EXTERNAL)
  - Filtro por estado (ACTIVE, INACTIVE, ERROR)
- **Acciones:**
  - Crear nuevo origen
  - Ver detalles del origen
  - Editar origen
  - Probar conexión
  - Sincronizar

#### **Crear Origen** (`/governance/data/origins/create`)

Formulario para registrar un nuevo origen:

**Campos del Formulario:**
- **Nombre del Origen:** Nombre identificador
- **Descripción:** Descripción del origen
- **Tipo:** INTERNAL (bases de datos, sistemas internos) o EXTERNAL (HuggingFace, Kaggle, APIs, cloud storage)
- **Configuración de Conexión:**
  - **Para INTERNAL:**
    - Tipo de base de datos (PostgreSQL, MySQL, MongoDB, etc.)
    - Host, puerto, base de datos
    - Credenciales (almacenadas de forma segura)
  - **Para EXTERNAL:**
    - Tipo: HuggingFace, Kaggle, API REST, S3, Azure Blob, GCS
    - URL base o endpoint
    - Credenciales o API keys
- **Configuración de Sincronización:**
  - Frecuencia de sincronización
  - Última sincronización
  - Próxima sincronización programada

#### **Detalle del Origen** (`/governance/data/origins/[id]`)

Página completa con información detallada:

**Secciones:**
1. **Información General:** Datos básicos del origen
2. **Conexión:** Estado de conexión, última prueba, configuración
3. **Sincronización:** Historial de sincronizaciones, estado, errores
4. **Datasets Asociados:** Lista de datasets que usan este origen
5. **Acciones:**
   - Probar conexión
   - Sincronizar ahora
   - Editar origen

#### **Editar Origen** (`/governance/data/origins/[id]/edit`)

Formulario editable para modificar configuración del origen.

### 4. **Gestión de Riesgos** (`/governance/data/risks`)

Pantalla independiente para gestión consolidada de riesgos:

- **Lista de Riesgos:** Todos los riesgos de todos los datasets
- **Filtros:**
  - Tipo de riesgo (QUALITY, BIAS, SECURITY, PRIVACITY, COMPLIANCE, LEGAL)
  - Estado (IDENTIFIED, ASSESSED, TREATED, MONITORED, CLOSED)
  - Probabilidad (LOW, MEDIUM, HIGH, CRITICAL)
  - Impacto (LOW, MEDIUM, HIGH, CRITICAL)
- **Búsqueda:** Por nombre de riesgo o dataset
- **Visualización:**
  - Cards con información de cada riesgo
  - Score de riesgo destacado
  - Estado y plan de mitigación
- **Navegación:** Enlace directo al dataset asociado

### 5. **Métricas de Calidad** (`/governance/data/quality`)

Dashboard consolidado de calidad:

- **Score Global:** Promedio de las 6 dimensiones ISO 8000
- **6 Dimensiones:**
  - Cards individuales para cada dimensión
  - Score, estado (PASS/WARNING/FAIL), umbral
  - Visualización con barras de progreso
- **Alertas:** Datasets con calidad por debajo del umbral
- **Tendencias:** Gráficos de evolución de calidad

### 6. **Gestión de Privacidad** (`/governance/data/privacy`)

Vista consolidada de privacidad y GDPR:

- **KPIs:**
  - Datasets con PII detectado
  - Datasets con consentimiento
  - DPIA completados
  - Compliance GDPR
- **Requisitos GDPR:**
  - Art. 6 - Base Legal: Estado de cumplimiento
  - Art. 7 - Consentimiento: Gestión de consentimientos
  - Art. 35 - DPIA: Evaluaciones completadas
- **Alertas:**
  - Datasets no cumplientes
  - PII sin consentimiento
  - DPIA pendientes

### 7. **Línea de Base** (`/governance/data/lineage`)

Visualización global de flujos de datos:

- **Vista Gráfica Interactiva:** Diagrama SVG con visualización de dependencias
  - Nodos representando datasets y transformaciones
  - Aristas mostrando relaciones y dependencias
  - Interactividad para explorar el grafo
- **Vista Tabular:** Lista cronológica de transformaciones
- **Flujos de Datos:** Visualización de transformaciones entre datasets
- **Tipos de Transformación:** FILTER, JOIN, AGGREGATION, CLEANING, TRANSFORMATION, SPLIT, MERGE
- **Timeline:** Orden cronológico de transformaciones
- **Dependencias:** Árbol de dependencias entre datasets con visualización gráfica

### 8. **Documentación** (`/governance/data/documentation`)

Búsqueda global de documentación:

- **Lista de Documentación:** Toda la documentación de todos los datasets
- **Filtros:**
  - Tipo (DECISION, TRANSFORMATION, APPROVAL, REJECTION, CHANGE, INCIDENT)
  - Búsqueda por texto
- **Visualización:**
  - Cards con información de cada documento
  - Tipo, fecha, dataset asociado
  - Tags y contenido
- **Navegación:** Enlace directo al dataset

### 9. **Roles y Responsabilidades** (`/governance/data/roles`)

Vista consolidada de roles y responsabilidades:

- **Lista de Roles:** Roles asignados por dataset (vista de auditoría/overview)
- **Tipos de Roles:**
  - OWNER: Propietario del dataset
  - STEWARD: Data Steward responsable
  - COMPLIANCE_OFFICER: Oficial de cumplimiento
  - DATA_SCIENTIST: Científico de datos
  - REVIEWER: Revisor
  - APPROVER: Aprobador
- **Información:**
  - Usuario asignado
  - Tipo de rol
  - Estado (ACTIVE, INACTIVE)
  - Permisos granulares (Read, Write, Delete, Approve)
- **Navegación:** Enlace directo al dataset para gestión detallada
- **Nota:** La gestión detallada de roles (asignar, editar, eliminar) se realiza desde la pestaña "Roles" en el detalle de cada dataset

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Registro de un Nuevo Dataset

1. **Acceso al Registro**
   - Navegar a Data Governance → Datasets Overview
   - Hacer clic en "Crear Dataset"

2. **Completar Información Básica**
   - Completar formulario con:
     - Nombre del dataset
     - Descripción
     - Tipo (TRAINING, VALIDATION, TEST, PRODUCTION, RAG)
     - Tipo de origen (INTERNAL, EXTERNAL)
   - **UUID se genera automáticamente**

3. **Configurar Origen**
   - Seleccionar origen existente o crear nuevo
   - Si es nuevo, completar información de conexión
   - Probar conexión

4. **Configurar Fuente de Datos**
   - Seleccionar tipo de fuente:
     - HuggingFace: Buscar y seleccionar dataset
     - Kaggle: Buscar y seleccionar dataset
     - API: Configurar endpoint
     - Upload: Subir archivo
   - Activar "Estandarización Automática" (recomendado)

5. **Guardar Dataset**
   - El sistema genera automáticamente:
     - UUID único para el dataset
     - Versión inicial (1.0.0)
   - Se dispara automáticamente:
     - Estandarización a Parquet (si está activada)
     - Workflow de aprobación
     - Análisis inicial de calidad

6. **Navegación a Detalle**
   - Se redirige automáticamente a la página de detalle
   - Puede revisar resultados de análisis y completar información adicional

### Flujo 2: Análisis de Calidad

1. **Acceder a Análisis**
   - Desde la página de detalle del dataset
   - Seleccionar pestaña "Calidad" o ir a "Analizar"

2. **Ejecutar Análisis**
   - Hacer clic en "Ejecutar Análisis de Calidad"
   - El sistema analiza las 6 dimensiones ISO 8000

3. **Revisar Resultados**
   - Ver scores por dimensión
   - Identificar dimensiones con problemas (WARNING o FAIL)
   - Revisar detalles de cada métrica

4. **Tomar Acciones**
   - Si hay problemas, crear riesgos asociados
   - Documentar decisiones en pestaña "Documentación"
   - Planificar mejoras

### Flujo 3: Gestión de Riesgos

1. **Identificar Riesgo**
   - Desde la pestaña "Riesgos" del dataset
   - Hacer clic en "Agregar Riesgo"

2. **Completar Información del Riesgo**
   - Tipo de riesgo (QUALITY, BIAS, SECURITY, PRIVACITY, COMPLIANCE, LEGAL)
   - Nombre y descripción
   - Probabilidad (LOW, MEDIUM, HIGH, CRITICAL)
   - Impacto (LOW, MEDIUM, HIGH, CRITICAL)
   - **El score se calcula automáticamente**

3. **Definir Plan de Mitigación**
   - Describir plan de mitigación
   - Asignar responsable
   - Establecer fecha objetivo

4. **Seguimiento**
   - Actualizar estado del riesgo
   - Monitorear progreso de mitigación
   - Cerrar cuando esté resuelto

### Flujo 4: Gestión de Privacidad y GDPR

1. **Detección de PII**
   - El sistema detecta automáticamente PII en el dataset
   - Revisar análisis en pestaña "Privacidad"

2. **Configurar Base Legal (Art. 6)**
   - Identificar base legal del procesamiento
   - Documentar justificación

3. **Gestionar Consentimiento (Art. 7)**
   - Si se requiere consentimiento, registrar:
     - Tipo de consentimiento
     - Fecha de obtención
     - Verificación de consentimiento

4. **Realizar DPIA (Art. 35)**
   - Si es necesario, completar evaluación de impacto
   - Documentar riesgos y medidas de mitigación
   - Obtener aprobación

5. **Configurar Política de Retención**
   - Establecer tiempo de retención de datos
   - Configurar proceso de eliminación

### Flujo 5: Estandarización a Parquet

1. **Activar Estandarización**
   - Al crear dataset, activar "Estandarización Automática"
   - O desde detalle, ejecutar estandarización manual

2. **Proceso Automático**
   - El microservicio Python recibe el dataset
   - Convierte a formato Apache Parquet
   - Aplica compresión Snappy
   - Valida esquema

3. **Almacenamiento**
   - Dataset estandarizado se almacena en MinIO
   - Se actualiza información del dataset (ruta, checksum, tamaño)

4. **Verificación**
   - Revisar que el dataset esté en formato Parquet
   - Verificar checksum y tamaño
   - El dataset queda listo para análisis

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **Data Scientists** 📊
- **Responsabilidad:** Desarrollo y análisis de datasets
- **Uso:** Registrar datasets, analizar calidad, gestionar transformaciones
- **Beneficio:** Estandarización automática, análisis de calidad, trazabilidad de experimentos

#### 2. **Data Engineers** ⚙️
- **Responsabilidad:** Gestión de orígenes y pipelines de datos
- **Uso:** Configurar orígenes, gestionar sincronizaciones, estandarización
- **Beneficio:** Centralización de orígenes, automatización de procesos

#### 3. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo
- **Uso:** Revisar compliance, gestionar riesgos, verificar GDPR
- **Beneficio:** Cumplimiento EU AI Act y GDPR, trazabilidad regulatoria

#### 4. **Governance Managers** 🎯
- **Responsabilidad:** Gobernanza de datos
- **Uso:** Gestionar roles, aprobar datasets, revisar documentación
- **Beneficio:** Control centralizado, visibilidad completa

#### 5. **Project Managers** 📋
- **Responsabilidad:** Gestión de proyectos
- **Uso:** Consultar datasets asociados a proyectos, monitorear calidad
- **Beneficio:** Visibilidad de datos por proyecto, control de calidad

#### 6. **Auditors** 🔍
- **Responsabilidad:** Auditorías y verificaciones
- **Uso:** Revisar trazabilidad, documentación, compliance
- **Beneficio:** Acceso completo a historial y documentación

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Centralización:** Registro único de todos los datasets utilizados
2. **Estandarización:** Formato uniforme (Apache Parquet) para análisis
3. **Cumplimiento Normativo:** Facilita cumplimiento de EU AI Act, GDPR, ISO 8000
4. **Calidad:** Análisis continuo de calidad según 6 dimensiones ISO 8000
5. **Gestión de Riesgos:** Identificación y mitigación proactiva de riesgos
6. **Privacidad:** Cumplimiento GDPR con gestión de PII y consentimiento
7. **Trazabilidad:** Historial completo para auditorías regulatorias
8. **Automatización:** Estandarización y análisis automáticos

### Para los Usuarios

1. **Facilidad de Uso:** Interfaz intuitiva y organizada
2. **Automatización:** UUID, versiones y análisis se generan automáticamente
3. **Visibilidad:** Información completa en un solo lugar
4. **Eficiencia:** Búsquedas y filtros avanzados
5. **Documentación:** Información estructurada y completa

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo para auditorías
2. **Cumplimiento EU AI Act:** Gestión de datos, calidad, documentación
3. **Cumplimiento GDPR:** Gestión de PII, consentimiento, DPIA
4. **ISO 8000:** Análisis de calidad según 6 dimensiones
5. **Evidencia:** Documentación completa de decisiones y transformaciones

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Workflow de Aprobación de Datasets

Cuando se registra un nuevo dataset, se dispara automáticamente el workflow de aprobación que incluye:

1. **Validaciones Automáticas:**
   - Análisis de calidad
   - Detección de sesgos
   - Verificación de compliance
   - Detección de PII

2. **Revisiones Humanas:**
   - Data Steward Review
   - Compliance Review
   - Governance Review

3. **Decisión Final:**
   - Aprobación mediante reglas
   - Estados: APPROVED, CONDITIONAL_APPROVAL, REJECTED

### Integración con Proyectos

- Los datasets se pueden asociar a proyectos
- Seguimiento de uso de datasets por proyecto
- Análisis de calidad por proyecto

### Integración con Compliance

- Los datasets se integran con proyectos de cumplimiento
- EU AI Act menciona gobierno del dato como requisito
- Trazabilidad completa para auditorías

### Integración con Microservicios Python

- **Estandarización:** Microservicio convierte datasets a Parquet
- **Análisis:** Microservicios de análisis de calidad, sesgos, compliance
- **Almacenamiento:** Integración con MinIO para almacenamiento

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **UUID Automático**
- Cada dataset recibe un UUID único generado automáticamente
- El UUID es inmutable y se usa para identificación única
- Campo de solo lectura

### 2. **Estandarización Automática a Apache Parquet**
- Conversión automática a formato Parquet
- Compresión Snappy para eficiencia
- Validación de esquema
- Almacenamiento en MinIO

### 3. **Análisis de Calidad ISO 8000**
- 6 dimensiones de calidad evaluadas automáticamente
- Scores por dimensión con umbrales configurables
- Alertas cuando la calidad está por debajo del umbral

### 4. **Gestión de Riesgos con Matriz**
- Matriz visual de probabilidad × impacto
- Score de riesgo calculado automáticamente
- Planes de mitigación y seguimiento

### 5. **Cumplimiento GDPR Completo**
- Detección automática de PII
- Gestión de consentimiento
- DPIA integrado
- Gestión de derechos de los interesados

### 6. **Línea de Base Completa con Visualización Gráfica**
- Historial completo de transformaciones
- **Visualización gráfica interactiva:** Diagrama SVG con nodos y aristas
- **Dos modos de visualización:** Gráfico interactivo y vista tabular
- Visualización de dependencias con layout circular
- Timeline de cambios
- Interactividad: Click en nodos para ver detalles

### 7. **Documentación Estructurada**
- Timeline de decisiones
- Tipos de documentación categorizados
- Búsqueda y filtros avanzados

### 8. **Permisos Granulares por Acción**
- Control fino sobre permisos independientes del rol
- Permisos por acción: Lectura, Escritura, Eliminación, Aprobación
- Visualización clara de permisos asignados
- Flexibilidad para combinar roles con permisos específicos

### 9. **Análisis de Impacto de Cambios**
- Identificación de datasets afectados por cambios
- Clasificación de impacto (HIGH, MEDIUM, LOW)
- Visualización de orígenes de datos relacionados
- Métricas de impacto para planificación de cambios
- Prevención de romper dependencias críticas

---

## ❓ PREGUNTAS FRECUENTES

### ¿Cómo se genera el UUID del dataset?

El UUID se genera automáticamente cuando se crea un nuevo dataset usando `crypto.randomUUID()`. Es un identificador único inmutable que no puede ser modificado.

### ¿Qué es Apache Parquet y por qué se usa?

Apache Parquet es un formato de almacenamiento columnar optimizado para análisis. Se usa porque:
- **Eficiencia:** Compresión y lectura optimizadas
- **Esquema:** Esquema embebido para validación
- **Estándar:** Formato estándar para análisis de datos
- **Compatibilidad:** Compatible con herramientas de análisis

### ¿Cómo funciona la estandarización automática?

1. El dataset se envía al microservicio Python
2. Se convierte al formato Parquet
3. Se aplica compresión Snappy
4. Se valida el esquema
5. Se almacena en MinIO
6. Se actualiza la información del dataset

### ¿Qué son las 6 dimensiones ISO 8000?

1. **Completitud:** Grado en que los datos están completos
2. **Precisión:** Grado en que los datos son correctos
3. **Consistencia:** Grado en que los datos son consistentes
4. **Validez:** Grado en que los datos cumplen reglas de negocio
5. **Puntualidad:** Grado en que los datos están disponibles cuando se necesitan
6. **Unicidad:** Grado en que no hay duplicados

### ¿Cómo se calcula el score de riesgo?

El score de riesgo se calcula combinando probabilidad e impacto:
- **Probabilidad:** LOW (0.25), MEDIUM (0.50), HIGH (0.75), CRITICAL (1.0)
- **Impacto:** LOW (0.25), MEDIUM (0.50), HIGH (0.75), CRITICAL (1.0)
- **Score:** Probabilidad × Impacto (0.0 - 1.0)

### ¿Qué pasa cuando se detecta PII?

Cuando se detecta PII:
1. Se marca el dataset con flag de PII detectado
2. Se genera alerta en dashboard
3. Se requiere configuración de base legal (GDPR Art. 6)
4. Si es necesario, se requiere consentimiento (GDPR Art. 7)
5. Puede requerirse DPIA (GDPR Art. 35)

### ¿Cómo gestiono el consentimiento?

En la pestaña "Privacidad" del dataset:
1. Revisar PII detectado
2. Si se requiere consentimiento, hacer clic en "Agregar Consentimiento"
3. Completar información: tipo, fecha, verificación
4. El sistema registra y monitorea el consentimiento

### ¿Qué es un DPIA?

DPIA (Data Protection Impact Assessment) es una evaluación de impacto en la protección de datos requerida por GDPR Art. 35 cuando el procesamiento puede entrañar un alto riesgo. En el módulo se puede:
- Completar evaluación de impacto
- Documentar riesgos y medidas de mitigación
- Obtener aprobación

### ¿Cómo veo la línea de base de un dataset?

En la página de detalle del dataset, accede a la pestaña "Línea de Base". Verás:
- **Dos modos de visualización:**
  - **Vista Gráfica:** Diagrama interactivo con nodos (datasets y transformaciones) y aristas (dependencias)
    - Layout circular con dataset central
    - Click en nodos para ver detalles
    - Iconos y colores por tipo de transformación
  - **Vista Tabular:** Lista cronológica de transformaciones
- Timeline de transformaciones
- Tipos de transformación aplicadas
- Dependencias con otros datasets
- Visualización de flujo de datos interactiva

### ¿Cómo agrego documentación a un dataset?

En la pestaña "Documentación" del dataset:
1. Hacer clic en "Agregar Documentación"
2. Seleccionar tipo (DECISION, TRANSFORMATION, AUDIT_REPORT, OTHER)
3. Completar título y contenido
4. Agregar tags para búsqueda
5. Guardar

### ¿Cómo filtro datasets por calidad?

En el listado de datasets, utiliza los filtros:
- Por score de calidad (bajo umbral, aceptable, excelente)
- Por dimensión específica (si alguna está en WARNING o FAIL)
- Por estado de compliance

### ¿Qué es el Análisis de Impacto de Cambios?

El Análisis de Impacto muestra qué datasets y orígenes de datos se verían afectados si se realizan cambios en este dataset o en sus orígenes dependientes. Permite:
- Planificar cambios con conocimiento de impacto
- Evitar romper dependencias críticas
- Entender el ecosistema de datos completo
- Clasificar impacto como HIGH, MEDIUM o LOW

Accede desde la pestaña "Análisis de Impacto" en el detalle del dataset.

### ¿Cómo funcionan los Permisos Granulares?

Los permisos granulares permiten control fino sobre qué acciones puede realizar cada usuario en un dataset, independientemente de su rol:
- **Lectura:** Ver y consultar el dataset
- **Escritura:** Modificar datos y configuración
- **Eliminación:** Eliminar el dataset
- **Aprobación:** Aprobar cambios y decisiones

Se asignan desde la pestaña "Roles" en el detalle del dataset, donde puedes combinar un rol con permisos específicos.

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DEL REGISTRO

Una vez que un dataset es registrado:

1. ✅ **Estandarización Automática:** Se convierte a Parquet (si está activada)
2. ✅ **Análisis Inicial:** Se ejecutan análisis de calidad y detección de PII
3. ✅ **Workflow Automático:** Se dispara automáticamente el workflow de aprobación
4. ✅ **Validaciones:** Se ejecutan validaciones automáticas (calidad, sesgos, compliance)
5. ✅ **Revisiones:** Data Steward, Compliance y Governance revisan el dataset
6. ✅ **Aprobación:** Decisión final mediante reglas
7. ✅ **Monitoreo Continuo:** Monitoreo continuo de calidad, riesgos y compliance

---

**Última Actualización:** Enero 2025
**Versión del Módulo:** 1.1
**Estado:** ✅ Operativo y listo para producción

### 🆕 Mejoras Implementadas (Versión 1.1)

#### Visualización Gráfica de Línea de Base
- **Vista Gráfica Interactiva:** Diagrama SVG con nodos y aristas para visualizar dependencias
- **Dos Modos:** Vista gráfica interactiva y vista tabular tradicional
- **Interactividad:** Click en nodos para ver detalles en panel lateral
- **Layout Circular:** Dataset central con transformaciones distribuidas alrededor
- **Iconos y Colores:** Diferenciación visual por tipo de transformación

#### Permisos Granulares por Acción
- **Control Fino:** Permisos independientes por acción (Read, Write, Delete, Approve)
- **Flexibilidad:** Combinación de roles con permisos específicos
- **Visualización:** Badges con iconos mostrando permisos activos
- **Gestión:** Asignación y edición de permisos desde la pestaña "Roles"

#### Análisis de Impacto de Cambios
- **Nueva Pestaña:** "Análisis de Impacto" en detalle de dataset
- **Métricas:** Total de datasets afectados, impacto alto, orígenes relacionados
- **Clasificación:** HIGH, MEDIUM, LOW con colores diferenciados
- **Funcionalidad:** Recalcular impacto en tiempo real
- **Navegación:** Enlaces directos a datasets y orígenes afectados
