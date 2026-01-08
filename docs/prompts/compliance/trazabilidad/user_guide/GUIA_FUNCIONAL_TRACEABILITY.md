# 📘 GUÍA FUNCIONAL - TRACEABILITY (TRAZABILIDAD)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales, Compliance Officers, Project Managers, Auditores

---

## 🎯 ¿QUÉ ES TRACEABILITY (TRAZABILIDAD)?

Traceability (Trazabilidad) es un sistema de **registro inmutable y verificable** de todas las acciones, decisiones y outputs relacionados con sistemas de Inteligencia Artificial. Su objetivo es proporcionar una cadena completa de evidencia que permita rastrear el origen, procesamiento y resultados de cualquier decisión o output generado por un sistema de IA.

### 🎯 Propósito Principal

El sistema de Trazabilidad permite:

1. **Trazabilidad Completa:** Seguimiento de la cadena modelo-dataset-output según Art. 12 y Art. 19 EU AI Act
2. **Inmutabilidad:** Logs con hash chains que garantizan que no han sido modificados
3. **Verificación de Integridad:** Validación automática de la cadena de hashes
4. **Evidencias Exportables:** Generación de evidencias en formato JSON/PDF para auditorías
5. **Cumplimiento Normativo:** Cumplimiento con EU AI Act Art. 12 y Art. 19

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 12 - Trazabilidad y Registro
Establece la obligación de mantener **registros inmutables** de todas las operaciones de sistemas de IA de alto riesgo, incluyendo:
- Logs de todas las acciones realizadas
- Decisiones HITL (Human-in-the-Loop)
- Outputs generados por el sistema
- Relaciones entre modelos, datasets y outputs

#### Art. 19 - Trazabilidad de Outputs
Requiere que los proveedores puedan **demostrar la trazabilidad completa** de cualquier output generado, incluyendo:
- Qué modelo lo generó
- Qué dataset se usó para entrenar el modelo
- Qué decisiones HITL se tomaron
- Qué logs inmutables documentan el proceso

---

## 🚀 ¿PARA QUÉ SIRVE TRACEABILITY?

### 1. **Para Sistemas de IA de Alto Riesgo (Obligatorio)**

Según el **EU AI Act**, los sistemas clasificados como de **alto riesgo** (Anexo III) **DEBEN** mantener trazabilidad completa. Esto incluye:

- ✅ Sistemas de biometría
- ✅ Sistemas de gestión de infraestructura crítica
- ✅ Sistemas de educación y formación profesional
- ✅ Sistemas de empleo y gestión de trabajadores
- ✅ Sistemas de acceso a servicios públicos
- ✅ Sistemas de aplicación de la ley
- ✅ Sistemas de migración y asilo
- ✅ Sistemas de administración de justicia

**En estos casos, Trazabilidad es OBLIGATORIO por ley.**

### 2. **Para TODOS los Sistemas de IA (Recomendado y Potente)**

**IMPORTANTE:** El sistema de Trazabilidad de CodeflowX está diseñado para ser **mucho más potente y válido** que los requisitos mínimos legales. Por lo tanto, **está disponible para TODOS los sistemas de IA**, no solo los de alto riesgo.

#### Beneficios para Sistemas No-Alto-Riesgo:

1. **Auditoría y Cumplimiento**
   - Evidencias completas para auditorías internas y externas
   - Demostración de procesos y decisiones
   - Cumplimiento con estándares de calidad
   - Preparación para futuras regulaciones

2. **Transparencia y Confianza**
   - Visibilidad completa del proceso de toma de decisiones
   - Trazabilidad de outputs a sus orígenes
   - Demostración de integridad de datos
   - Mejora de la confianza de stakeholders

3. **Gestión de Riesgos**
   - Identificación de problemas de integridad
   - Detección de modificaciones no autorizadas
   - Protección contra manipulación de datos
   - Reducción de riesgos legales y regulatorios

4. **Mejora Continua**
   - Análisis histórico de decisiones
   - Identificación de patrones y tendencias
   - Optimización basada en evidencia
   - Lecciones aprendidas documentadas

5. **Responsabilidad y Accountability**
   - Asignación clara de responsabilidades
   - Trazabilidad de decisiones a usuarios específicos
   - Documentación de aprobaciones HITL
   - Evidencias para disputas o reclamos

---

## 📊 COMPONENTES PRINCIPALES DE TRACEABILITY

### 1. **Logs Inmutables**
Registros que no pueden ser modificados:
- **Hash Chains:** Cada log contiene el hash del log anterior
- **Verificación Automática:** Validación de la cadena de hashes
- **Inmutabilidad Garantizada:** Cualquier modificación rompe la cadena
- **Información Registrada:**
  - Tipo de acción (MODEL_TRAINING, MODEL_DEPLOYMENT, DATASET_VALIDATION, etc.)
  - Timestamp preciso
  - Usuario que realizó la acción
  - Descripción de la acción
  - Hash actual y hash anterior

### 2. **Decisiones HITL (Human-in-the-Loop)**
Registros de decisiones humanas:
- **Tipos:** MODEL_APPROVAL, DATASET_APPROVAL, MODEL_DEPLOYMENT, etc.
- **Decisiones:** APPROVED, REJECTED, PENDING
- **Información:**
  - Usuario que tomó la decisión
  - Razón de la decisión
  - Fecha y hora
  - Entidad relacionada (Model, Project, Agent)

### 3. **Outputs Generados**
Registros de outputs producidos por modelos:
- **Input:** Datos de entrada al modelo
- **Output:** Resultado generado
- **Confidence:** Nivel de confianza (0.0 - 1.0)
- **Timestamp:** Cuándo se generó
- **Modelo:** Qué modelo lo generó

### 4. **Verificación de Integridad**
Sistema automático de validación:
- **Score de Integridad:** Porcentaje de logs verificados (0% - 100%)
- **Estado:** INTEGRITY_OK, INTEGRITY_WARNING, INTEGRITY_ERROR
- **Verificación de Hash Chains:** Validación de que cada log tiene el hash correcto del anterior
- **Alertas Automáticas:** Workflow BPMN se dispara si hay problemas de integridad

### 5. **Evidencias Exportables**
Generación de evidencias para auditorías:
- **Formato JSON:** Estructura completa de trazabilidad
- **Formato PDF:** Documento formateado para auditorías (estructura preparada)
- **Incluye:**
  - Información completa de la entidad
  - Todos los logs inmutables
  - Todas las decisiones HITL
  - Todos los outputs generados
  - Verificación de integridad
  - Timestamp de exportación

---

## 🔄 FLUJO DE TRABAJO TÍPICO

### Escenario 1: Consultar Trazabilidad de un Modelo

1. **Acceso**
   - Navegar a Governance → Compliance → Traceability
   - Buscar el modelo de interés
   - O acceder directamente desde la página del modelo

2. **Visualización**
   - Ver información del modelo
   - Ver logs inmutables ordenados cronológicamente
   - Ver decisiones HITL relacionadas
   - Ver outputs generados
   - Ver verificación de integridad

3. **Análisis**
   - Revisar cadena de hashes
   - Verificar que todos los logs están verificados
   - Analizar decisiones de aprobación
   - Revisar outputs y su confianza

### Escenario 2: Exportar Evidencias para Auditoría

1. **Selección**
   - Acceder a la trazabilidad de la entidad (Model, Project, Agent)
   - Revisar que la verificación de integridad esté en INTEGRITY_OK

2. **Exportación**
   - Hacer clic en "Exportar JSON" o "Exportar PDF"
   - El sistema genera la evidencia completa
   - Incluye verificación de integridad

3. **Uso**
   - Descargar el archivo
   - Usar para auditorías internas o externas
   - Presentar a autoridades si es requerido

### Escenario 3: Verificar Integridad de Logs

1. **Acceso**
   - Acceder a la trazabilidad de cualquier entidad
   - Revisar la sección "Verificación de Integridad"

2. **Análisis**
   - Verificar score de integridad (debe ser 100% para INTEGRITY_OK)
   - Revisar número de logs verificados vs total
   - Si hay problemas (INTEGRITY_WARNING o INTEGRITY_ERROR):
     - Revisar logs individuales
     - Identificar dónde se rompió la cadena
     - Investigar causa

3. **Acción**
   - Si se detecta problema, el sistema dispara automáticamente un workflow BPMN de alerta
   - Seguir el workflow para investigar y resolver

### Escenario 4: Explorar Entidades Relacionadas

1. **Navegación**
   - Acceder a la trazabilidad de una entidad
   - Revisar la sección "Entidades Relacionadas"

2. **Exploración**
   - Ver modelos relacionados con un proyecto
   - Ver datasets relacionados con un modelo
   - Ver proyectos que usan un modelo

3. **Trazabilidad Completa**
   - Hacer clic en "Ver Detalles" de una entidad relacionada
   - Navegar a su trazabilidad completa
   - Construir la cadena completa modelo-dataset-output

---

## 🎯 CASOS DE USO

### Caso de Uso 1: Auditoría Regulatoria
**Tipo:** Alto Riesgo - Sistema de Scoring Crediticio

**Necesidad:** Demostrar trazabilidad completa para auditoría regulatoria

**Uso de Traceability:**
- Exportar evidencias completas del modelo
- Demostrar que todos los logs están verificados (INTEGRITY_OK)
- Mostrar decisiones HITL de aprobación
- Presentar outputs generados con sus inputs
- Evidencias en formato PDF para autoridades

### Caso de Uso 2: Investigación de Problema
**Tipo:** Cualquier Sistema de IA

**Necesidad:** Investigar por qué un output específico fue generado

**Uso de Traceability:**
- Buscar el output específico
- Ver el input que lo generó
- Ver qué modelo lo generó
- Ver qué dataset se usó para entrenar el modelo
- Ver decisiones HITL relacionadas
- Reconstruir el proceso completo

### Caso de Uso 3: Verificación de Integridad
**Tipo:** Cualquier Sistema de IA

**Necesidad:** Verificar que los logs no han sido modificados

**Uso de Traceability:**
- Revisar verificación de integridad
- Verificar que score = 100%
- Revisar cadena de hashes
- Si hay problemas, investigar causa
- Corregir problemas detectados

### Caso de Uso 4: Trazabilidad de Proyecto Completo
**Tipo:** Proyecto con Múltiples Modelos

**Necesidad:** Ver trazabilidad completa de un proyecto

**Uso de Traceability:**
- Acceder a trazabilidad del proyecto
- Ver todos los modelos asociados (a través de ModelDeployment)
- Ver logs del proyecto
- Ver decisiones HITL del proyecto
- Ver relación entre modelos y outputs

---

## ✅ BENEFICIOS CLAVE

### Para Compliance Officers
- ✅ Cumplimiento automático con EU AI Act Art. 12 y Art. 19
- ✅ Evidencias exportables para auditorías
- ✅ Verificación automática de integridad
- ✅ Trazabilidad completa documentada

### Para Auditores
- ✅ Acceso completo a evidencias
- ✅ Verificación de integridad de logs
- ✅ Trazabilidad de outputs a orígenes
- ✅ Documentación completa y exportable

### Para Project Managers
- ✅ Visibilidad completa del proceso
- ✅ Trazabilidad de decisiones
- ✅ Relaciones entre entidades
- ✅ Evidencias para reportes

### Para Equipos Técnicos
- ✅ Logs inmutables de todas las acciones
- ✅ Verificación de integridad automática
- ✅ Trazabilidad de outputs
- ✅ Herramientas para investigación

### Para la Organización
- ✅ Cumplimiento regulatorio
- ✅ Reducción de riesgos legales
- ✅ Transparencia y confianza
- ✅ Mejora continua basada en evidencia

---

## 📚 REFERENCIAS

- **EU AI Act:** Reglamento (UE) 2024/1689
- **Art. 12:** Trazabilidad y registro
- **Art. 19:** Trazabilidad de outputs

---

**Última Actualización:** Diciembre 2025
