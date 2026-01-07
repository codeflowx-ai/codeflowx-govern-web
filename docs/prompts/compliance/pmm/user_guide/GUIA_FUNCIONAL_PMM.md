# 📘 GUÍA FUNCIONAL - POST-MARKET MONITORING (PMM)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales, Compliance Officers, Project Managers

---

## 🎯 ¿QUÉ ES POST-MARKET MONITORING (PMM)?

Post-Market Monitoring (PMM) es un sistema de **vigilancia continua** de sistemas de Inteligencia Artificial después de su despliegue en producción. Su objetivo es detectar, analizar y responder proactivamente a problemas de rendimiento, degradación de calidad, sesgos, o cualquier anomalía que pueda afectar el funcionamiento correcto de los sistemas de IA.

### 🎯 Propósito Principal

El PMM permite:

1. **Monitoreo Continuo:** Vigilancia 24/7 del comportamiento de sistemas de IA en producción
2. **Detección Temprana:** Identificación proactiva de problemas antes de que impacten a usuarios
3. **Cumplimiento Normativo:** Cumplimiento con EU AI Act Art. 20, Art. 72, Art. 16.g, Art. 16.h, Art. 73
4. **Mejora Continua:** Datos para optimización y mejora continua de sistemas
5. **Gestión de Riesgos:** Identificación y mitigación temprana de riesgos operacionales

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 20 - Vigilancia Post-Comercialización
Establece la obligación de **monitorear continuamente** los sistemas de IA de alto riesgo después de su puesta en el mercado, para detectar y corregir problemas.

#### Art. 72 - Planes de Vigilancia
Requiere que los proveedores de sistemas de IA de alto riesgo mantengan **planes formales de vigilancia post-mercado** que incluyan:
- Métricas a monitorear
- Frecuencias de monitoreo
- Thresholds de alertas
- Procedimientos de respuesta

#### Art. 16.g - Requisitos de Alto Riesgo
Establece que los sistemas de alto riesgo deben tener **planes de monitoreo post-mercado** como parte de sus requisitos de conformidad.

#### Art. 73 - Notificación de Incidentes Graves
Requiere notificación **inmediata** a las autoridades competentes cuando se detecten incidentes graves que puedan afectar la salud, seguridad o derechos fundamentales.

---

## 🚀 ¿PARA QUÉ SIRVE PMM?

### 1. **Para Sistemas de IA de Alto Riesgo (Obligatorio)**

Según el **EU AI Act**, los sistemas clasificados como de **alto riesgo** (Anexo III) **DEBEN** tener planes formales de PMM. Esto incluye:

- ✅ Sistemas de biometría
- ✅ Sistemas de gestión de infraestructura crítica
- ✅ Sistemas de educación y formación profesional
- ✅ Sistemas de empleo y gestión de trabajadores
- ✅ Sistemas de acceso a servicios públicos
- ✅ Sistemas de aplicación de la ley
- ✅ Sistemas de migración y asilo
- ✅ Sistemas de administración de justicia

**En estos casos, PMM es OBLIGATORIO por ley.**

### 2. **Para TODOS los Sistemas de IA (Recomendado y Potente)**

**IMPORTANTE:** El sistema PMM de CodeflowX está diseñado para ser **mucho más potente y válido** que los requisitos mínimos legales. Por lo tanto, **está disponible para TODOS los sistemas de IA**, no solo los de alto riesgo.

#### Beneficios para Sistemas No-Alto-Riesgo:

1. **Calidad y Excelencia Operacional**
   - Detección temprana de degradación de rendimiento
   - Monitoreo de métricas clave (accuracy, latency, throughput)
   - Identificación de drift de datos
   - Detección de anomalías

2. **Gestión Proactiva de Problemas**
   - Identificación de problemas antes de que afecten usuarios
   - Reducción de tiempo de inactividad
   - Mejora de la experiencia del usuario
   - Optimización continua del sistema

3. **Cumplimiento Voluntario y Mejores Prácticas**
   - Demostración de compromiso con calidad y seguridad
   - Preparación para futuras regulaciones
   - Mejora de la confianza de stakeholders
   - Ventaja competitiva

4. **Gestión de Riesgos Empresariales**
   - Reducción de riesgos operacionales
   - Protección de reputación
   - Minimización de costos por incidentes
   - Mejora de la toma de decisiones

5. **Optimización y Eficiencia**
   - Identificación de oportunidades de mejora
   - Optimización de recursos
   - Mejora continua basada en datos
   - ROI mejorado

---

## 📊 COMPONENTES PRINCIPALES DE PMM

### 1. **Dashboard de Monitoreo**
Vista centralizada que muestra:
- **Métricas principales:** Sistemas monitoreados, incidentes activos, acciones pendientes, cumplimiento SLA
- **Sistemas en producción:** Estado de salud de cada sistema, métricas en tiempo real, alertas
- **Filtros:** Por proyecto y tipo de métrica

### 2. **Gestión de Incidentes**
Sistema completo para:
- **Reportar incidentes:** Detección de problemas, degradación, anomalías
- **Clasificar severidad:** LOW, MEDIUM, HIGH, CRITICAL
- **Notificación automática:** Incidentes HIGH/CRITICAL se notifican automáticamente a autoridades (Art. 20.1, Art. 73)
- **Seguimiento:** Estados (OPEN, INVESTIGATING, RESOLVED, CLOSED)
- **Análisis de causa raíz:** Identificación de causas fundamentales

### 3. **Acciones Correctoras**
Gestión de medidas para resolver incidentes:
- **Creación vinculada a incidentes:** Cada acción se asocia a un incidente específico
- **Tipos de acciones:** IMMEDIATE, SHORT_TERM, LONG_TERM
- **Seguimiento de estados:** PLANNED, IN_PROGRESS, COMPLETED
- **Evaluación de efectividad:** Medición del impacto de las acciones (0.00 - 1.00)

### 4. **Planes PMM Formales**
Configuración de planes de monitoreo:
- **Definición de métricas:** Qué métricas monitorear (accuracy, latency, throughput, bias, etc.)
- **Frecuencias de monitoreo:** HOURLY, DAILY, WEEKLY, MONTHLY, CUSTOM
- **Thresholds de alertas:** Umbrales de warning y critical por métrica
- **Frecuencias de reporte:** DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUAL
- **Estados:** DRAFT, ACTIVE, SUSPENDED, ARCHIVED

### 5. **Reportes de Vigilancia**
Generación y gestión de reportes:
- **Tipos de reportes:** DAILY, WEEKLY, MONTHLY, AD_HOC
- **Generación automática:** Según frecuencia configurada en planes PMM
- **Descarga PDF:** Reportes en formato PDF para auditorías
- **Estados:** DRAFT, GENERATED, APPROVED, ARCHIVED

---

## 🔄 FLUJO DE TRABAJO TÍPICO

### Escenario 1: Monitoreo Continuo

1. **Configuración Inicial**
   - Crear un Plan PMM para el proyecto
   - Definir métricas a monitorear
   - Configurar thresholds de alertas
   - Establecer frecuencias de monitoreo y reporte
   - Activar el plan

2. **Monitoreo Automático**
   - El sistema ejecuta monitoreo según la frecuencia configurada
   - Evalúa métricas contra thresholds
   - Genera alertas automáticamente si se superan umbrales

3. **Respuesta a Alertas**
   - Revisar alertas en el dashboard
   - Investigar métricas anómalas
   - Reportar incidente si es necesario

### Escenario 2: Gestión de Incidentes

1. **Detección**
   - Sistema detecta anomalía o degradación
   - Usuario reporta incidente manualmente
   - Alerta automática se genera

2. **Reporte**
   - Crear incidente con detalles completos
   - Clasificar severidad
   - Si es HIGH/CRITICAL → Notificación automática a autoridades

3. **Investigación**
   - Análisis de causa raíz
   - Documentación de detalles
   - Identificación de factores contribuyentes

4. **Acción Correctiva**
   - Definir acciones correctoras
   - Asignar responsables
   - Establecer fechas planificadas

5. **Implementación y Verificación**
   - Ejecutar acciones correctoras
   - Verificar resolución del incidente
   - Evaluar efectividad

6. **Cierre**
   - Documentar resolución
   - Cerrar incidente
   - Generar lecciones aprendidas

### Escenario 3: Generación de Reportes

1. **Configuración**
   - Plan PMM define frecuencia de reportes
   - Sistema programa generación automática

2. **Generación**
   - Sistema genera reporte según frecuencia
   - Incluye métricas, alertas, incidentes del período
   - Genera PDF automáticamente

3. **Revisión y Aprobación**
   - Revisar contenido del reporte
   - Aprobar si es correcto
   - Descargar PDF para auditorías

---

## 🎯 CASOS DE USO

### Caso de Uso 1: Sistema de Scoring Crediticio
**Tipo:** Alto Riesgo (Anexo III - Sistemas de acceso a servicios públicos)

**Necesidad:** Monitoreo obligatorio según EU AI Act

**Uso de PMM:**
- Monitoreo de accuracy del modelo
- Detección de drift en datos de entrada
- Identificación de sesgos en decisiones
- Reportes mensuales a autoridades
- Gestión de incidentes si se detectan problemas

### Caso de Uso 2: Sistema de Recomendaciones
**Tipo:** No-Alto-Riesgo

**Necesidad:** Calidad operacional y excelencia

**Uso de PMM:**
- Monitoreo de métricas de rendimiento (latency, throughput)
- Detección de degradación de calidad de recomendaciones
- Optimización continua basada en datos
- Gestión proactiva de problemas
- Mejora de experiencia del usuario

### Caso de Uso 3: Sistema de Chatbot
**Tipo:** No-Alto-Riesgo

**Necesidad:** Gestión de calidad y satisfacción

**Uso de PMM:**
- Monitoreo de tiempo de respuesta
- Detección de respuestas incorrectas o inapropiadas
- Análisis de satisfacción del usuario
- Identificación de áreas de mejora
- Optimización de rendimiento

---

## ✅ BENEFICIOS CLAVE

### Para Compliance Officers
- ✅ Cumplimiento automático con EU AI Act
- ✅ Documentación completa para auditorías
- ✅ Trazabilidad de incidentes y acciones
- ✅ Reportes listos para autoridades

### Para Project Managers
- ✅ Visibilidad completa del estado de sistemas
- ✅ Detección temprana de problemas
- ✅ Gestión proactiva de riesgos
- ✅ Datos para toma de decisiones

### Para Equipos Técnicos
- ✅ Alertas automáticas de problemas
- ✅ Métricas en tiempo real
- ✅ Herramientas para análisis de causa raíz
- ✅ Seguimiento de acciones correctoras

### Para la Organización
- ✅ Reducción de riesgos operacionales
- ✅ Mejora de calidad y confiabilidad
- ✅ Optimización continua
- ✅ Ventaja competitiva

---

## 📚 REFERENCIAS

- **EU AI Act:** Reglamento (UE) 2024/1689
- **Art. 20:** Vigilancia post-comercialización
- **Art. 72:** Planes de vigilancia
- **Art. 16.g:** Requisitos de sistemas de alto riesgo
- **Art. 73:** Notificación de incidentes graves

---

**Última Actualización:** Diciembre 2025
