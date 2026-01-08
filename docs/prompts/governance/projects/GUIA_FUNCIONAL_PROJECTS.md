# 📘 GUÍA FUNCIONAL - GESTIÓN DE PROYECTOS DE IA Y CUMPLIMIENTO NORMATIVO

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Project Managers, Compliance Officers, Governance Managers, Auditors, Developers, Business Administrators

---

## 🎯 ¿QUÉ ES LA GESTIÓN DE PROYECTOS?

La **Gestión de Proyectos** es un módulo central que actúa como **hub de gobierno de IA, datos y cumplimiento normativo**. A diferencia de herramientas tradicionales de gestión de proyectos (Jira, ServiceNow), este módulo se enfoca en gobernar proyectos de IA, garantizar cumplimiento normativo y proporcionar visibilidad agregada de información desde otros módulos especializados.

### 🎯 Propósito Principal

El módulo de gestión de proyectos permite:

1. **Hub Central:** Agregar y conectar información de otros módulos (Compliance, Governance, Models, Agents) sin duplicar funcionalidad
2. **Portfolio Management:** Vista consolidada de todos los proyectos de IA y cumplimiento normativo
3. **Integraciones Externas:** Gestión centralizada de integraciones con herramientas externas (Jira, ServiceNow, etc.)
4. **Recursos de IA:** Asignación y seguimiento de recursos específicos de IA (GPUs, modelos, infraestructura)
5. **Análisis de Costos:** Desglose de costos específicos de IA (compute, storage, APIs, modelos)
6. **ROI de IA:** Análisis de retorno de inversión específico para proyectos de IA
7. **Sincronización:** Sincronización con herramientas externas de gestión de proyectos

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 10 - Requisitos de Datos y Gobernanza de Datos

Los proyectos de IA de alto riesgo deben cumplir con requisitos de calidad de datos y gobernanza, incluyendo:
- Gestión y gobernanza de datos de entrenamiento, validación y prueba
- Documentación de datos de entrenamiento por proyecto

#### Art. 15 - Requisitos de Transparencia y Provisión de Información

Los proyectos deben proporcionar:
- Información adecuada y relevante sobre el sistema
- Instrucciones de uso apropiadas
- Trazabilidad de decisiones

#### Art. 20 - Monitoreo Post-Mercado

Requiere monitoreo continuo del rendimiento de los proyectos en producción para detectar anomalías.

### ISO 42001 - Gestión de Sistemas de IA

#### Cláusula 8.2 - Diseño y Desarrollo

Requiere control de versiones y gestión de cambios en proyectos de IA.

#### Cláusula 9.1 - Monitoreo, Medición, Análisis y Evaluación

Requiere seguimiento de métricas y KPIs de proyectos de IA.

---

## 🚀 ¿PARA QUÉ SIRVE LA GESTIÓN DE PROYECTOS?

### 1. **Cumplimiento Normativo**

El módulo facilita el cumplimiento de:
- **EU AI Act Art. 10, 15, 20:** Gestión de datos, transparencia y monitoreo post-mercado por proyecto
- **ISO 42001 8.2, 9.1:** Control de versiones y seguimiento de métricas
- **Trazabilidad Regulatoria:** Registro completo de proyectos para auditorías

### 2. **Hub Central de Información**

Proporciona vistas agregadas de:
- **Estado de Compliance:** Resumen de cumplimiento normativo por proyecto (sin duplicar módulo Compliance)
- **Gobierno de IA:** Resumen de políticas y aprobaciones por proyecto (sin duplicar módulo Governance)
- **Recursos y Costos:** Visibilidad consolidada de recursos de IA y costos asociados

### 3. **Integración con Herramientas Externas**

Permite:
- **Sincronización:** Integración con Jira, ServiceNow y otras herramientas
- **Vista Unificada:** Consolidar información de múltiples fuentes
- **Gestión Centralizada:** Configurar y monitorear integraciones desde un solo lugar

### 4. **Optimización de Recursos de IA**

Facilita:
- **Asignación de Recursos:** GPUs, modelos, infraestructura por proyecto
- **Seguimiento de Consumo:** Monitorear uso de recursos de IA
- **Optimización:** Identificar proyectos con mayor consumo y optimizar asignación

### 5. **Análisis Financiero de IA**

Permite:
- **Desglose de Costos:** Costos específicos de IA (compute, storage, APIs, modelos)
- **ROI de IA:** Análisis de retorno de inversión específico para proyectos de IA
- **Tendencias:** Identificar proyectos con mejor ROI y optimizar inversión

---

## 📊 COMPONENTES PRINCIPALES DEL MÓDULO

### 1. **Portfolio Dashboard**

Dashboard principal con vista consolidada del portfolio de proyectos:

#### **Métricas Principales**
- **Total de Proyectos:** Número total de proyectos registrados
- **Proyectos Activos:** Proyectos en ejecución
- **Proyectos Completados:** Proyectos finalizados
- **Presupuesto Total:** Presupuesto asignado al portfolio
- **Presupuesto Gastado:** Presupuesto utilizado
- **Recursos Totales:** Recursos disponibles
- **Recursos Asignados:** Recursos en uso
- **Proyectos en Riesgo:** Proyectos con alertas o problemas
- **Proyectos a Tiempo:** Proyectos cumpliendo plazos

#### **Visualizaciones**
- Gráficos de distribución de proyectos por estado
- Tendencias de presupuesto y gasto
- Utilización de recursos
- Métricas de cumplimiento de plazos

### 2. **Lista de Proyectos**

Pantalla principal para gestionar el catálogo de proyectos:

#### **Listado de Proyectos**
- **Vista Tabular:** Tabla paginada con todos los proyectos
- **Métricas Principales:**
  - Total de proyectos registrados
  - Proyectos activos
  - Proyectos completados
  - Proyectos en riesgo
- **Filtros Avanzados:**
  - Búsqueda por nombre
  - Filtro por estado (ACTIVE, COMPLETED, ON_HOLD, CANCELLED)
  - Filtro por dominio
  - Filtro por tipo de proyecto
- **Acciones:**
  - Crear nuevo proyecto
  - Ver detalles del proyecto
  - Editar proyecto
  - Eliminar proyecto

#### **Página de Detalle del Proyecto**

Página completa con información detallada del proyecto:

**Información General:**
- Formulario editable para crear/editar proyecto
- Campos: nombre, descripción, estado, tipo, dominio, fechas, presupuesto
- **ID del proyecto (UUID):** Generado automáticamente, campo de solo lectura
- **Métricas del Proyecto:** KPIs consolidados

**Información Agregada (Links a otros módulos):**
- **Compliance Status:** Link a estado de cumplimiento en módulo Compliance
- **Gobierno de IA:** Link a políticas y aprobaciones en módulo Governance
- **Modelos Asociados:** Link a modelos en módulo Models
- **Agentes Asociados:** Link a agentes en módulo Agents
- **Trazabilidad:** Link a trazabilidad completa en módulo Compliance

### 3. **Integrations Dashboard**

Pantalla para gestionar integraciones con herramientas externas:

#### **Listado de Integraciones**
- **Vista Tabular:** Tabla con todas las integraciones configuradas
- **Tipos de Integración:**
  - Jira
  - ServiceNow
  - Azure DevOps
  - GitHub Projects
  - Otras herramientas
- **Estado de Integraciones:**
  - Activas
  - Inactivas
  - Con errores
  - Pendientes de configuración
- **Métricas:**
  - Total de integraciones
  - Integraciones activas
  - Última sincronización
  - Errores recientes

#### **Configuración de Integración**
- Formulario para configurar nueva integración
- Campos: tipo, nombre, URL base, credenciales, proyecto asociado
- Configuración de sincronización (frecuencia, campos a sincronizar)
- Prueba de conexión

#### **Logs de Sincronización**
- Historial de sincronizaciones
- Errores y advertencias
- Estadísticas de sincronización

### 4. **External Tools Sync**

Pantalla para gestionar sincronización con herramientas externas:

#### **Estado de Sincronización**
- Vista de sincronizaciones activas
- Última sincronización por herramienta
- Próxima sincronización programada
- Errores de sincronización

#### **Configuración de Sincronización**
- Programar sincronizaciones automáticas
- Configurar campos a sincronizar
- Mapeo de campos entre sistemas
- Resolución de conflictos

### 5. **Resource Allocation**

Pantalla para gestionar asignación de recursos de IA por proyecto:

#### **Asignación de Recursos**
- **Recursos de IA:**
  - GPUs asignadas por proyecto
  - Modelos asociados
  - Infraestructura (compute, storage)
  - APIs y servicios externos
- **Vista por Proyecto:**
  - Recursos asignados
  - Recursos disponibles
  - Utilización actual
  - Capacidad restante
- **Vista por Recurso:**
  - Proyectos que usan cada recurso
  - Distribución de recursos
  - Optimización de asignación

#### **Métricas de Utilización**
- Gráficos de utilización de recursos
- Tendencias de consumo
- Alertas de sobreutilización
- Recomendaciones de optimización

### 6. **Cost Breakdown**

Pantalla para analizar costos específicos de IA por proyecto:

#### **Desglose de Costos**
- **Costos por Categoría:**
  - Compute (GPUs, CPUs)
  - Storage (datasets, modelos)
  - APIs (OpenAI, Anthropic, etc.)
  - Modelos (licencias, uso)
  - Infraestructura (Kubernetes, cloud)
- **Costos por Proyecto:**
  - Total por proyecto
  - Desglose por categoría
  - Tendencias mensuales
  - Comparación entre proyectos

#### **Visualizaciones**
- Gráficos de distribución de costos
- Tendencias de costos en el tiempo
- Comparación entre proyectos
- Análisis de costos por recurso

### 7. **ROI Analysis**

Pantalla para analizar retorno de inversión de proyectos de IA:

#### **Métricas de ROI**
- **ROI por Proyecto:**
  - Inversión total
  - Beneficios generados
  - ROI calculado
  - Tiempo de retorno
- **Métricas de Impacto:**
  - Impacto de modelos (accuracy, precision, recall)
  - Impacto de agentes (automatización, eficiencia)
  - Impacto en cumplimiento normativo
  - Impacto en gobernanza

#### **Visualizaciones**
- Gráficos de ROI por proyecto
- Comparación de ROI entre proyectos
- Tendencias de ROI en el tiempo
- Análisis de factores que impactan ROI

---

## 🔄 PROCESOS Y FLUJOS DE TRABAJO

### Flujo 1: Crear un Nuevo Proyecto

1. **Acceso al Listado**
   - Navegar a Projects → Projects List
   - Hacer clic en "Agregar Proyecto"

2. **Completar Información Básica**
   - Completar formulario con:
     - Nombre del proyecto
     - Descripción
     - Tipo de proyecto
     - Dominio
     - Fechas (inicio, fin estimado)
     - Presupuesto
     - Estado inicial
   - **ID se genera automáticamente**

3. **Asociar Recursos (Opcional)**
   - Asignar recursos de IA (GPUs, modelos, infraestructura)
   - Configurar límites de recursos

4. **Guardar Proyecto**
   - El sistema genera automáticamente:
     - UUID único para el proyecto
     - Métricas iniciales
   - Se crean enlaces automáticos a módulos relacionados

5. **Navegación a Detalle**
   - Se redirige automáticamente a la página de detalle
   - Puede completar información adicional

### Flujo 2: Configurar Integración Externa

1. **Acceder a Integrations Dashboard**
   - Navegar a Projects → Integrations Dashboard
   - Hacer clic en "Agregar Integración"

2. **Seleccionar Tipo de Integración**
   - Elegir herramienta (Jira, ServiceNow, etc.)
   - Completar información de conexión:
     - URL base
     - Credenciales
     - Proyecto asociado (opcional)

3. **Configurar Sincronización**
   - Definir frecuencia de sincronización
   - Seleccionar campos a sincronizar
   - Configurar mapeo de campos

4. **Probar Conexión**
   - Hacer clic en "Probar Conexión"
   - Verificar que la conexión funciona

5. **Activar Integración**
   - Guardar configuración
   - La sincronización se ejecuta según la frecuencia configurada

### Flujo 3: Asignar Recursos de IA

1. **Acceder a Resource Allocation**
   - Navegar a Projects → Resource Allocation
   - Seleccionar proyecto

2. **Asignar Recursos**
   - Seleccionar recursos disponibles:
     - GPUs
     - Modelos
     - Infraestructura
   - Definir cantidad y límites

3. **Monitorear Utilización**
   - Ver métricas de utilización
   - Revisar alertas de sobreutilización
   - Ajustar asignación según necesidad

### Flujo 4: Analizar Costos y ROI

1. **Ver Cost Breakdown**
   - Navegar a Projects → Cost Breakdown
   - Seleccionar proyecto o vista de portfolio
   - Revisar desglose de costos por categoría

2. **Analizar ROI**
   - Navegar a Projects → ROI Analysis
   - Seleccionar proyecto
   - Revisar métricas de ROI e impacto

3. **Tomar Decisiones**
   - Comparar costos y ROI entre proyectos
   - Identificar proyectos con mejor ROI
   - Optimizar asignación de recursos

---

## 👥 ¿PARA QUIÉN ES ESTE MÓDULO?

### Roles y Responsabilidades

#### 1. **Project Managers** 📋
- **Responsabilidad:** Gestión de proyectos de IA y cumplimiento normativo
- **Uso:** Crear proyectos, asignar recursos, monitorear costos, analizar ROI
- **Beneficio:** Visibilidad completa del portfolio, control de recursos y costos

#### 2. **Compliance Officers** 👔
- **Responsabilidad:** Cumplimiento normativo
- **Uso:** Revisar estado de cumplimiento por proyecto, auditorías
- **Beneficio:** Vista agregada de cumplimiento sin duplicar módulo Compliance

#### 3. **Governance Managers** 🛡️
- **Responsabilidad:** Gobierno de IA
- **Uso:** Revisar políticas y aprobaciones por proyecto
- **Beneficio:** Vista agregada de gobierno sin duplicar módulo Governance

#### 4. **Auditors** 🔍
- **Responsabilidad:** Auditorías y verificaciones
- **Uso:** Revisar proyectos, verificar trazabilidad, auditorías
- **Beneficio:** Acceso consolidado a información de proyectos

#### 5. **Business Administrators** 💼
- **Responsabilidad:** Gestión empresarial
- **Uso:** Analizar ROI, optimizar recursos, tomar decisiones estratégicas
- **Beneficio:** Visibilidad financiera y estratégica del portfolio

#### 6. **Developers** 👨‍💻
- **Responsabilidad:** Desarrollo técnico
- **Uso:** Consultar proyectos asociados, recursos asignados
- **Beneficio:** Visibilidad de contexto del proyecto

---

## ✅ BENEFICIOS DEL MÓDULO

### Para la Organización

1. **Hub Central:** Vista consolidada de información de múltiples módulos sin duplicación
2. **Cumplimiento Normativo:** Facilita cumplimiento de EU AI Act e ISO 42001 por proyecto
3. **Optimización de Recursos:** Visibilidad completa de recursos de IA y su utilización
4. **Control de Costos:** Desglose detallado de costos específicos de IA
5. **Análisis de ROI:** Métricas de retorno de inversión específicas para proyectos de IA
6. **Integración:** Sincronización con herramientas externas de gestión de proyectos
7. **Trazabilidad:** Registro completo de proyectos para auditorías

### Para los Usuarios

1. **Vista Consolidada:** Información agregada de múltiples módulos en un solo lugar
2. **Sin Duplicación:** No duplica funcionalidad de otros módulos, solo agrega información
3. **Facilidad de Uso:** Interfaz intuitiva y organizada
4. **Automatización:** Integraciones automáticas con herramientas externas
5. **Visibilidad:** Información completa del portfolio en un solo lugar

### Para el Cumplimiento Legal

1. **Trazabilidad Regulatoria:** Registro completo de proyectos para auditorías
2. **Cumplimiento EU AI Act:** Gestión de datos, transparencia, monitoreo por proyecto
3. **ISO 42001:** Control de versiones y seguimiento de métricas
4. **Evidencia:** Documentación completa de proyectos y sus recursos

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Integración con Compliance

- **Estado Agregado:** Vista resumida de cumplimiento por proyecto
- **Links a Compliance:** Enlaces directos a módulo Compliance para detalles completos
- **NO Duplica:** No duplica funcionalidad de Compliance, solo agrega información

### Integración con Governance

- **Políticas Agregadas:** Vista resumida de políticas y aprobaciones por proyecto
- **Links a Governance:** Enlaces directos a módulo Governance para detalles completos
- **NO Duplica:** No duplica funcionalidad de Governance, solo agrega información

### Integración con Models

- **Modelos Asociados:** Lista de modelos utilizados por proyecto
- **Links a Models:** Enlaces directos a módulo Models para detalles completos
- **Costos:** Costos de modelos agregados en Cost Breakdown

### Integración con Agents

- **Agentes Asociados:** Lista de agentes utilizados por proyecto
- **Links a Agents:** Enlaces directos a módulo Agents para detalles completos
- **Impacto:** Impacto de agentes en métricas de ROI

### Integración con Data Governance

- **Datasets Asociados:** Lista de datasets utilizados por proyecto
- **Links a Data Governance:** Enlaces directos a módulo Data Governance para detalles completos
- **Trazabilidad:** Línea de base de datos en Data Lineage Overview

---

## 📋 CARACTERÍSTICAS ESPECIALES

### 1. **Hub Central (NO Duplica)**

El módulo Projects actúa como hub central que:
- **Agrega información** de otros módulos sin duplicar funcionalidad
- **Proporciona enlaces** a módulos especializados para detalles completos
- **Consolida métricas** de múltiples fuentes en un solo lugar

### 2. **Integraciones Externas**

- Sincronización automática con Jira, ServiceNow y otras herramientas
- Configuración centralizada de integraciones
- Logs y monitoreo de sincronizaciones

### 3. **Recursos de IA**

- Asignación específica de recursos de IA (GPUs, modelos, infraestructura)
- Seguimiento de utilización de recursos
- Optimización de asignación

### 4. **Costos Específicos de IA**

- Desglose de costos por categoría (compute, storage, APIs, modelos)
- Análisis de tendencias de costos
- Comparación entre proyectos

### 5. **ROI de IA**

- Métricas de ROI específicas para proyectos de IA
- Análisis de impacto de modelos y agentes
- Comparación de ROI entre proyectos

---

## ❓ PREGUNTAS FRECUENTES

### ¿Cómo se diferencia este módulo de herramientas tradicionales de gestión de proyectos?

Este módulo se enfoca en **gobierno de IA y cumplimiento normativo**, no en gestión tradicional de tareas. Se integra con herramientas externas (Jira, ServiceNow) para sincronizar información, pero su propósito es gobernar proyectos de IA, no gestionar tareas o timelines.

### ¿Por qué no hay pantallas de tareas, time tracking o invoices?

Estas funcionalidades se manejan en herramientas externas (Jira, ServiceNow). El módulo Projects se integra con estas herramientas para sincronizar información, pero no duplica su funcionalidad.

### ¿Cómo funciona la integración con otros módulos?

Projects **agrega información** de otros módulos sin duplicar funcionalidad. Por ejemplo:
- Muestra estado agregado de Compliance, pero los detalles están en módulo Compliance
- Muestra políticas agregadas de Governance, pero los detalles están en módulo Governance
- Proporciona enlaces directos a módulos especializados para información completa

### ¿Puedo crear un proyecto sin asignar recursos?

Sí, la asignación de recursos es opcional. Puedes crear un proyecto y asignar recursos más tarde.

### ¿Cómo se calcula el ROI de un proyecto?

El ROI se calcula considerando:
- Inversión total (recursos, costos de IA, infraestructura)
- Beneficios generados (impacto de modelos, agentes, cumplimiento normativo)
- Métricas de impacto específicas de IA

### ¿Cómo configuro una integración con Jira?

1. Navega a Projects → Integrations Dashboard
2. Haz clic en "Agregar Integración"
3. Selecciona "Jira"
4. Completa URL base y credenciales
5. Configura frecuencia de sincronización
6. Prueba la conexión
7. Activa la integración

### ¿Cómo veo los costos de un proyecto?

Navega a Projects → Cost Breakdown y selecciona el proyecto. Verás:
- Desglose por categoría (compute, storage, APIs, modelos)
- Tendencias mensuales
- Comparación con otros proyectos

### ¿Qué recursos de IA puedo asignar a un proyecto?

Puedes asignar:
- GPUs
- Modelos (de módulo Models)
- Infraestructura (compute, storage)
- APIs y servicios externos

### ¿Cómo monitoreo la utilización de recursos?

Navega a Projects → Resource Allocation y selecciona el proyecto. Verás:
- Recursos asignados
- Utilización actual
- Alertas de sobreutilización
- Recomendaciones de optimización

---

## 🔄 PRÓXIMOS PASOS DESPUÉS DE CREAR UN PROYECTO

Una vez que un proyecto es creado:

1. ✅ **Asignación de Recursos:** Asignar recursos de IA (GPUs, modelos, infraestructura)
2. ✅ **Configuración de Integraciones:** Configurar integraciones con herramientas externas si es necesario
3. ✅ **Seguimiento de Costos:** Monitorear costos específicos de IA en Cost Breakdown
4. ✅ **Análisis de ROI:** Analizar retorno de inversión en ROI Analysis
5. ✅ **Enlaces a Módulos:** Acceder a módulos especializados (Compliance, Governance, Models, Agents) para detalles completos
6. ✅ **Monitoreo Continuo:** Monitorear métricas del proyecto en Portfolio Dashboard

---

**Última Actualización:** Diciembre 2025
**Versión del Módulo:** 1.0
**Estado:** ✅ Operativo y listo para producción
