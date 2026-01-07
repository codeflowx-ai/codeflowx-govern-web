# 📘 GUÍA FUNCIONAL - GESTIÓN DE MODELOS DE MACHINE LEARNING

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** ML Engineers, Data Scientists, Project Managers, Compliance Officers, DevOps Teams

---

## 🎯 ¿QUÉ ES LA GESTIÓN DE MODELOS?

La **Gestión de Modelos** es un módulo integral que permite registrar, versionar, gestionar y monitorear modelos de Machine Learning a lo largo de todo su ciclo de vida. El módulo proporciona herramientas completas para el registro de modelos, gestión de proveedores, control de versiones, seguimiento de costes y monitoreo de métricas.

### 🎯 Propósito Principal

El módulo de gestión de modelos permite:

1. **Registro Centralizado:** Mantener un registro único y centralizado de todos los modelos de ML utilizados en la organización
2. **Control de Versiones:** Gestionar versiones de modelos con versionado semántico automático (MAJOR.MINOR.PATCH)
3. **Gestión de Proveedores:** Administrar proveedores de modelos (internos y externos) y sus credenciales
4. **Seguimiento de Costes:** Monitorear costes y consumo de tokens por modelo y versión
5. **Métricas y Rendimiento:** Visualizar métricas de rendimiento, latencia, precisión y uso
6. **Trazabilidad Completa:** Mantener registro completo del ciclo de vida de cada modelo
7. **Workflow de Aprobación:** Integración automática con procesos de aprobación y validación

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 10 - Requisitos de Datos y Gobernanza de Datos

Los modelos de alto riesgo deben cumplir con requisitos de calidad de datos, incluyendo:
- Gestión y gobernanza de datos de entrenamiento, validación y prueba
- Documentación de datos de entrenamiento

#### Art. 15 - Requisitos de Transparencia y Provisión de Información

Los modelos deben proporcionar:
- Información adecuada y relevante sobre el sistema
- Instrucciones de uso apropiadas

#### Art. 20 - Monitoreo Post-Mercado

Requiere monitoreo continuo del rendimiento de los modelos en producción para detectar anomalías.

### ISO 42001 - Gestión de Sistemas de IA

#### Cláusula 8.2 - Diseño y Desarrollo

Requiere control de versiones y gestión de cambios en modelos de IA.

---

## 🚀 ¿PARA QUÉ SIRVE LA GESTIÓN DE MODELOS?

### 1. **Cumplimiento Normativo**

El módulo facilita el cumplimiento de:
- **EU AI Act Art. 10, 15, 20:** Gestión de datos, transparencia y monitoreo post-mercado
- **ISO 42001 8.2:** Control de versiones y gestión de cambios
- **Trazabilidad Regulatoria:** Registro completo de modelos para auditorías

### 2. **Gestión del Ciclo de Vida**

Proporciona herramientas para:
- **Registro Inicial:** Registrar nuevos modelos con información completa
- **Versionado Automático:** Control de versiones con semántica automática
- **Seguimiento de Cambios:** Historial completo de modificaciones
- **Gestión de Despliegues:** Información sobre dónde está desplegado cada modelo

### 3. **Optimización de Costes**

Permite:
- **Seguimiento de Consumo:** Monitorear tokens y costes por modelo
- **Análisis de Tendencias:** Identificar modelos con mayor consumo
- **Optimización de Recursos:** Tomar decisiones informadas sobre uso de modelos

### 4. **Monitoreo de Rendimiento**

Facilita:
- **Métricas en Tiempo Real:** KPIs de rendimiento, latencia, precisión
- **Análisis de Tendencias:** Gráficos de evolución de métricas
- **Detección de Degradación:** Identificar problemas de rendimiento tempranamente

### 5. **Gestión de Proveedores**

Permite:
- **Centralización:** Gestionar todos los proveedores (OpenAI, Anthropic, Mistral, etc.) en un solo lugar
- **Gestión de Credenciales:** Administrar credenciales de forma segura con asociación opcional a proyectos
- **Monitoreo de Uso:** Seguimiento de uso de credenciales por proyecto

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Registro de Modelos**

Pantalla principal para gestionar el catálogo de modelos:

#### **Listado de Modelos**
- **Vista Tabular:** Tabla paginada con todos los modelos registrados
- **Métricas Principales:**
  - Total de modelos registrados
  - Modelos activos
  - Modelos inactivos
  - Modelos pendientes de aprobación
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo de modelo
  - Filtro por estado (ACTIVE, INACTIVE, PENDING)
- **Acciones:**
  - Crear nuevo modelo
  - Ver detalles del modelo
  - Editar modelo
  - Eliminar modelo

#### **Página de Detalle del Modelo**

Página completa con 6 pestañas para gestión detallada:

**1. Información General:**
- Formulario editable para crear/editar modelo
- Campos: nombre, displayName, descripción, tipo, framework, estado, etapa actual
- **ID del modelo (UUID):** Generado automáticamente, campo de solo lectura
- **Versión Semántica:** Calculada automáticamente, campo de solo lectura
- **Proveedor:** Campo select para asociar proveedor

**2. Versiones:**
- Gestión completa de versiones del modelo
- Crear, editar, eliminar versiones
- Formulario inline para nuevas versiones
- **Versionado Semántico Automático:** Incrementa MAJOR.MINOR.PATCH automáticamente

**3. Proyectos:**
- Tabla con proyectos que utilizan el modelo
- Información: nombre del proyecto, número de usos, último uso
- Modal para ver detalles del proyecto

**4. Costes y Consumo:**
- **Métricas:**
  - Total Tokens consumidos
  - Coste Total acumulado
  - Coste Mensual
  - Coste por Token
- **Consumo por Versión:** Desglose detallado de tokens y costes por cada versión
- **Gráfico de Tendencias:** Visualización de evolución de costes y consumo

**5. Métricas:**
- **KPIs Principales:**
  - Total Requests
  - Tasa de Éxito
  - Latencia Promedio
  - Precisión
- **Gráfico de Tendencias:** Visualización de evolución de métricas de rendimiento

**6. Despliegues:**
- **Despliegues Internos:**
  - Información de despliegues en Kubernetes
  - Namespace, cluster, endpoint
  - Estado y configuración
- **Proveedor Externo:**
  - Información del proveedor externo
  - URL base, regiones disponibles
  - Health check y documentación

### 2. **Gestión de Proveedores**

Pantalla para administrar proveedores de modelos:

#### **Listado de Proveedores**
- **Vista Tabular:** Tabla paginada con todos los proveedores
- **Métricas Principales:**
  - Total de proveedores
  - Proveedores activos
  - Proveedores pendientes
  - Proveedores inactivos
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo (EXTERNAL, INTERNAL)
  - Filtro por estado (ACTIVE, INACTIVE, PENDING)
- **Acciones:**
  - Crear nuevo proveedor
  - Ver detalles del proveedor
  - Eliminar proveedor

#### **Página de Detalle del Proveedor**

Página completa con 5 pestañas:

**1. Información General:**
- Formulario editable para crear/editar proveedor
- Campos: nombre, displayName, descripción, tipo (EXTERNAL/INTERNAL), estado (ACTIVE/INACTIVE/PENDING), tipo de autenticación, URL base

**2. Credenciales:**
- Gestión completa de credenciales
- Crear, ver, eliminar credenciales
- Formulario inline para nuevas credenciales
- **Campos:** nombre, tipo, ambiente, estado, proyecto (asociación opcional)
- **Tabla de Credenciales:** Muestra nombre, tipo, ambiente, estado, proyecto asociado, fecha de expiración, último uso, contador de uso

**3. Modelos Asociados:**
- Tabla con modelos vinculados al proveedor
- Información: nombre del modelo, tipo, versión, estado

**4. Proyectos Asociados:**
- Tabla con proyectos que usan credenciales del proveedor
- Información: nombre del proyecto, credenciales usadas, último uso

**5. Costes y KPIs:**
- **KPIs:**
  - Total Requests
  - Tasa de Éxito
  - Latencia Promedio
  - Uptime
  - Credenciales Activas/Expiradas
- **Costes:**
  - Total Mensual
  - Total Diario
  - Desglose por credencial
- **Gráfico de Tendencias:** Visualización de evolución de costes y KPIs

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Registro de un Nuevo Modelo

1. **Acceso al Registro**
   - Navegar a Models → Registry
   - Hacer clic en "Agregar Modelo"

2. **Completar Información Básica**
   - Completar formulario con:
     - Nombre del modelo
     - Display Name (nombre para mostrar)
     - Descripción del modelo
     - Tipo de modelo
     - Framework utilizado
     - Estado inicial
     - Etapa actual del ciclo de vida
   - **ID y Versión se generan automáticamente**

3. **Asociar Proveedor (Opcional)**
   - Seleccionar proveedor del dropdown
   - Si no existe, crear primero en Gestión de Proveedores

4. **Guardar Modelo**
   - El sistema genera automáticamente:
     - UUID único para el modelo
     - Versión inicial (1.0.0)
   - Se dispara automáticamente el workflow de aprobación

5. **Navegación a Detalle**
   - Se redirige automáticamente a la página de detalle
   - Puede completar información adicional en otras pestañas

### Flujo 2: Gestión de Versiones

1. **Acceder a Pestaña Versiones**
   - Desde la página de detalle del modelo
   - Seleccionar pestaña "Versiones"

2. **Crear Nueva Versión**
   - Hacer clic en "Agregar Versión"
   - Completar descripción de los cambios
   - **La versión se calcula automáticamente:**
     - Si es corrección de errores → incrementa PATCH (1.0.0 → 1.0.1)
     - Si es nueva funcionalidad → incrementa MINOR (1.0.0 → 1.1.0)
     - Si es cambio incompatible → incrementa MAJOR (1.0.0 → 2.0.0)

3. **Gestionar Versiones Existentes**
   - Ver lista de todas las versiones
   - Editar descripción de versiones
   - Eliminar versiones (con validaciones)

### Flujo 3: Gestión de Proveedores y Credenciales

1. **Crear Proveedor**
   - Navegar a Models → Providers
   - Hacer clic en "Agregar Proveedor"
   - Completar información:
     - Nombre, displayName, descripción
     - Tipo: EXTERNAL (OpenAI, Anthropic, etc.) o INTERNAL
     - Estado: ACTIVE, INACTIVE, PENDING
     - Tipo de autenticación
     - URL base

2. **Agregar Credenciales**
   - Acceder a detalle del proveedor
   - Seleccionar pestaña "Credenciales"
   - Hacer clic en "Agregar Credencial"
   - Completar:
     - Nombre de la credencial
     - Tipo (API Key, OAuth, etc.)
     - Ambiente (Development, Staging, Production)
     - Estado
     - **Proyecto asociado (opcional):** Permite asociar credencial a proyecto específico

3. **Monitorear Uso de Credenciales**
   - Ver tabla de credenciales con:
     - Estado y expiración
     - Último uso
     - Contador de uso
     - Proyecto asociado

### Flujo 4: Monitoreo de Costes y Métricas

1. **Ver Costes del Modelo**
   - Acceder a detalle del modelo
   - Seleccionar pestaña "Costes y Consumo"
   - Revisar:
     - Métricas generales (Total Tokens, Coste Total, Coste Mensual)
     - Desglose por versión
     - Gráfico de tendencias

2. **Analizar Métricas de Rendimiento**
   - Seleccionar pestaña "Métricas"
   - Revisar KPIs:
     - Total Requests
     - Tasa de Éxito
     - Latencia Promedio
     - Precisión
   - Analizar gráfico de tendencias

3. **Ver Despliegues**
   - Seleccionar pestaña "Despliegues"
   - Revisar información de despliegues internos (Kubernetes)
   - Ver información del proveedor externo si aplica

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **ML Engineers** 👨‍💻
- **Responsabilidad:** Desarrollo y gestión de modelos
- **Uso:** Registrar modelos, gestionar versiones, monitorear métricas
- **Beneficio:** Control centralizado de modelos, versionado automático, seguimiento de rendimiento

#### 2. **Data Scientists** 📊
- **Responsabilidad:** Análisis y desarrollo de modelos
- **Uso:** Registrar modelos, analizar métricas, gestionar versiones
- **Beneficio:** Trazabilidad de experimentos, comparación de versiones

#### 3. **DevOps Engineers** ⚙️
- **Responsabilidad:** Despliegue y operación de modelos
- **Uso:** Gestionar proveedores, monitorear despliegues, revisar métricas operacionales
- **Beneficio:** Visibilidad de infraestructura, gestión de credenciales centralizada

#### 4. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo
- **Uso:** Revisar modelos registrados, verificar trazabilidad, auditorías
- **Beneficio:** Cumplimiento EU AI Act, trazabilidad regulatoria

#### 5. **Project Managers** 📋
- **Responsabilidad:** Gestión de proyectos
- **Uso:** Consultar modelos asociados a proyectos, monitorear costes
- **Beneficio:** Visibilidad de uso de modelos por proyecto, control de costes

#### 6. **FinOps / Finance Teams** 💰
- **Responsabilidad:** Gestión financiera
- **Uso:** Analizar costes por modelo, revisar tendencias de consumo
- **Beneficio:** Optimización de costes, presupuestación precisa

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Centralización:** Registro único de todos los modelos utilizados
2. **Trazabilidad:** Historial completo del ciclo de vida de cada modelo
3. **Cumplimiento Normativo:** Facilita cumplimiento de EU AI Act e ISO 42001
4. **Optimización de Costes:** Visibilidad completa de consumo y costes
5. **Control de Versiones:** Versionado semántico automático y gestión estructurada
6. **Automatización:** Integración con workflows de aprobación
7. **Gobernanza:** Control centralizado de proveedores y credenciales

### Para los Usuarios

1. **Facilidad de Uso:** Interfaz intuitiva y organizada
2. **Automatización:** UUID y versiones se generan automáticamente
3. **Visibilidad:** Información completa en un solo lugar
4. **Eficiencia:** Búsquedas y filtros avanzados
5. **Documentación:** Información estructurada y completa

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo para auditorías
2. **Cumplimiento EU AI Act:** Gestión de datos, transparencia, monitoreo
3. **ISO 42001:** Control de versiones y gestión de cambios
4. **Evidencia:** Documentación completa de modelos y sus versiones

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Workflow de Aprobación de Modelos

Cuando se registra un nuevo modelo, se dispara automáticamente el workflow `model-approval-v1` que incluye:

1. **Validaciones Automáticas:**
   - Performance Validation
   - Bias Detection
   - Compliance Check

2. **Revisiones Humanas:**
   - ML Engineer Review
   - Governance Review

3. **Decisión Final:**
   - Aprobación mediante reglas Drools
   - Estados: APPROVED, CONDITIONAL_APPROVAL, REJECTED

### Integración con Proyectos

- Los modelos se pueden asociar a proyectos
- Seguimiento de uso de modelos por proyecto
- Análisis de costes por proyecto

### Integración con Telemetría

- Métricas de rendimiento en tiempo real
- Monitoreo de uso y consumo
- Alertas de degradación

### Integración con Deployment

- Información de despliegues (Kubernetes, proveedores externos)
- Endpoints y configuraciones
- Estado de despliegues

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **UUID Automático**
- Cada modelo recibe un UUID único generado automáticamente
- El UUID es inmutable y se usa para identificación única
- Campo de solo lectura

### 2. **Versionado Semántico Automático**
- Las versiones siguen el estándar semántico: MAJOR.MINOR.PATCH
- Se calculan automáticamente según el tipo de cambio
- No requiere entrada manual del usuario

### 3. **Gestión de Credenciales con Proyectos**
- Las credenciales de proveedores pueden asociarse opcionalmente a proyectos
- Permite segregación de credenciales por proyecto
- Facilita gestión de costes y auditorías

### 4. **Monitoreo de Costes**
- Seguimiento detallado de tokens y costes
- Desglose por versión del modelo
- Gráficos de tendencias para análisis

### 5. **Métricas en Tiempo Real**
- KPIs de rendimiento actualizados
- Gráficos de tendencias
- Comparación entre versiones

---

## ❓ PREGUNTAS FRECUENTES

### ¿Cómo se genera el UUID del modelo?

El UUID se genera automáticamente cuando se crea un nuevo modelo usando `crypto.randomUUID()`. Es un identificador único inmutable que no puede ser modificado.

### ¿Cómo funciona el versionado semántico automático?

El sistema calcula automáticamente la siguiente versión basándose en el tipo de cambio:
- **PATCH** (1.0.0 → 1.0.1): Correcciones de errores
- **MINOR** (1.0.0 → 1.1.0): Nuevas funcionalidades compatibles
- **MAJOR** (1.0.0 → 2.0.0): Cambios incompatibles

### ¿Puedo editar el UUID o la versión del modelo?

No, tanto el UUID como la versión son campos calculados automáticamente y de solo lectura para garantizar la integridad y trazabilidad del sistema.

### ¿Qué pasa cuando registro un nuevo modelo?

Al registrar un nuevo modelo:
1. Se genera automáticamente el UUID y versión inicial (1.0.0)
2. Se dispara automáticamente el workflow de aprobación (`model-approval-v1`)
3. El modelo queda en estado PENDING hasta que se complete la aprobación

### ¿Cómo gestiono las credenciales de proveedores?

1. Crea o selecciona el proveedor en "Gestión de Proveedores"
2. Accede a la pestaña "Credenciales" del proveedor
3. Haz clic en "Agregar Credencial"
4. Completa la información (opcionalmente asocia a un proyecto)
5. Las credenciales se almacenan de forma segura (encriptadas)

### ¿Puedo asociar una credencial a múltiples proyectos?

Actualmente, cada credencial puede asociarse opcionalmente a un proyecto específico. Si necesitas usar la misma credencial en múltiples proyectos, puedes crear múltiples credenciales (una por proyecto) o dejarla sin asociar.

### ¿Cómo veo dónde está desplegado un modelo?

En la página de detalle del modelo, accede a la pestaña "Despliegues". Ahí verás:
- Despliegues internos (Kubernetes): namespace, cluster, endpoint
- Proveedor externo: URL base, regiones, health check

### ¿Cómo monitoreo los costes de un modelo?

En la página de detalle del modelo, accede a la pestaña "Costes y Consumo". Verás:
- Métricas generales (Total Tokens, Coste Total, Coste Mensual)
- Desglose por versión
- Gráfico de tendencias de costes y consumo

### ¿Qué métricas están disponibles?

En la pestaña "Métricas" del modelo verás:
- Total Requests
- Tasa de Éxito
- Latencia Promedio
- Precisión
- Gráfico de tendencias de estas métricas

### ¿Cómo filtro modelos por tipo o estado?

En el listado de modelos, utiliza los filtros superiores:
- Búsqueda por nombre
- Filtro por tipo de modelo
- Filtro por estado (ACTIVE, INACTIVE, PENDING)

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DEL REGISTRO

Una vez que un modelo es registrado:

1. ✅ **Workflow Automático:** Se dispara automáticamente el workflow de aprobación
2. ✅ **Validaciones:** Se ejecutan validaciones automáticas (performance, bias, compliance)
3. ✅ **Revisiones:** ML Engineer y Governance revisan el modelo
4. ✅ **Aprobación:** Decisión final mediante reglas Drools
5. ✅ **Despliegue:** Si es aprobado, puede procederse al despliegue
6. ✅ **Monitoreo:** Monitoreo continuo en producción (métricas, costes)

---

**Última Actualización:** Diciembre 2025
**Versión del Módulo:** 1.0
**Estado:** ✅ Operativo y listo para producción
