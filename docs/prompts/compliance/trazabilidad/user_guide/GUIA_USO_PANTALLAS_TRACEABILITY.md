# 📱 GUÍA DE USO - PANTALLAS TRACEABILITY

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Acceso al Módulo Traceability](#acceso-al-módulo-traceability)
2. [Página Principal de Trazabilidad](#página-principal-de-trazabilidad)
3. [Página de Detalle de Entidad](#página-de-detalle-de-entidad)
4. [Tutoriales Paso a Paso](#tutoriales-paso-a-paso)

---

## 🚪 ACCESO AL MÓDULO TRACEABILITY

### Desde el Menú Principal

1. Navegar a **Governance** → **Compliance**
2. Seleccionar **Traceability** en el sidebar
3. Se abrirá la página principal de Trazabilidad

### URL Directa

- Página Principal: `/governance/compliance/traceability`
- Detalle de Modelo: `/governance/compliance/traceability/Model/123`
- Detalle de Proyecto: `/governance/compliance/traceability/Project/1`
- Detalle de Agente: `/governance/compliance/traceability/Agent/789`

---

## 📊 PÁGINA PRINCIPAL DE TRAZABILIDAD

### Ubicación
`/governance/compliance/traceability`

### Descripción
Vista centralizada con métricas generales, filtros de búsqueda y lista de evidencias de trazabilidad.

### Componentes

#### 1. **Header**
- **Título:** "Trazabilidad y Evidencias"
- **Subtítulo:** "Trazabilidad completa modelo-dataset-output según Art. 12 y 19 EU AI Act"
- **Layout:** 100% ancho, título alineado a la izquierda

#### 2. **Métricas Principales (4 Cards)**
Grid de 4 columnas mostrando:
- **Total Entidades:** Número total de entidades con trazabilidad (Model, Project, Agent)
- **Logs Verificados:** Número de logs inmutables verificados
- **Score de Integridad:** Porcentaje promedio de integridad (0% - 100%)
- **Decisiones HITL:** Número total de decisiones HITL registradas

#### 3. **Filtros de Búsqueda**
Sección con filtros para buscar evidencias:
- **Búsqueda por Entidad:** Campo de texto para buscar por nombre de entidad
- **Filtro por Fecha:** Selector de rango de fechas (desde - hasta)
- **Filtro por Usuario:** Campo de texto para filtrar por usuario
- **Botón Buscar:** Ejecuta la búsqueda con los filtros aplicados
- **Botón Limpiar:** Resetea todos los filtros

#### 4. **Lista de Evidencias**
Grid de 3 columnas mostrando cards de evidencias:

**Información en cada Card:**
- **Tipo de Entidad:** Badge con tipo (Model, Project, Agent)
- **Nombre de la Entidad:** Nombre de la entidad
- **ID:** Identificador de la entidad
- **Score de Integridad:** Badge con color según estado
  - Verde: INTEGRITY_OK (100%)
  - Amarillo: INTEGRITY_WARNING (< 100%)
  - Rojo: INTEGRITY_ERROR (< 50%)
- **Logs:** Número de logs inmutables
- **Decisiones HITL:** Número de decisiones
- **Outputs:** Número de outputs generados
- **Botón "Ver Detalles":** Navega a la página de detalle

#### 5. **Ejemplo de Cadena Model-Dataset-Output**
Card informativo mostrando:
- **Título:** "Cadena Modelo-Dataset-Output"
- **Descripción:** Explicación de la cadena de trazabilidad
- **Ejemplo Visual:** Diagrama o texto mostrando la relación Model → Dataset → Output

### Funcionalidades

#### Buscar Evidencias

1. **Usar Filtros**
   - Ingresar texto en "Buscar por Entidad"
   - Seleccionar rango de fechas
   - Ingresar usuario en "Filtro por Usuario"
   - Hacer clic en **"Buscar"**

2. **Resultados**
   - La lista de evidencias se filtra según los criterios
   - Se muestran solo las evidencias que coinciden

3. **Limpiar Filtros**
   - Hacer clic en **"Limpiar"**
   - Todos los filtros se resetean
   - Se muestran todas las evidencias

#### Ver Detalles de Evidencia

1. **Desde la Lista**
   - Hacer clic en **"Ver Detalles"** en cualquier card
   - Se navega a la página de detalle de esa entidad

2. **Navegación**
   - URL: `/governance/compliance/traceability/[entityType]/[id]`
   - Ejemplo: `/governance/compliance/traceability/Model/123`

---

## 🔍 PÁGINA DE DETALLE DE ENTIDAD

### Ubicación
`/governance/compliance/traceability/[entityType]/[id]`

**Ejemplos:**
- Modelo: `/governance/compliance/traceability/Model/123`
- Proyecto: `/governance/compliance/traceability/Project/1`
- Agente: `/governance/compliance/traceability/Agent/789`

### Descripción
Vista detallada de la trazabilidad completa de una entidad específica, incluyendo logs inmutables, decisiones HITL, outputs y verificación de integridad.

### Componentes

#### 1. **Header Compacto**
- **Título:** Nombre de la entidad (ej: "Credit Scoring Model v1.0")
- **Botón Volver:** Navega a la página principal
- **Botones de Exportación:**
  - **Exportar JSON:** Descarga evidencias en formato JSON
  - **Exportar PDF:** Descarga evidencias en formato PDF (cuando esté implementado)

#### 2. **Información y Verificación de Integridad (Card Combinado)**
Grid de 6 columnas mostrando:
- **Tipo de Entidad:** Badge con tipo (Model, Project, Agent)
- **Score de Integridad:** Porcentaje con badge de color
- **Logs Verificados:** X/Y (verificados/total)
- **Total de Logs:** Número total de logs
- **Decisiones HITL:** Número de decisiones
- **Outputs Generados:** Número de outputs

**Si hay problemas de integridad:**
- Se muestra un banner amarillo con lista de problemas detectados
- Icono de alerta (⚠️)

#### 3. **Trazabilidad Completa (Grid de 3 Columnas)**

##### **Columna 1: Logs Inmutables**
Card con scroll independiente (max-height: 600px):
- **Header:** "Logs Inmutables" con icono de escudo (🛡️) y badge con cantidad
- **Lista de Logs:**
  - Cada log muestra:
    - **Tipo:** Badge con tipo de acción
    - **Verificación:** Icono verde (✓) si verificado, amarillo (⚠️) si no
    - **Fecha:** Fecha formateada
    - **Descripción:** Texto de la acción
    - **Usuario:** ID del usuario
    - **Hash:** Primeros 12 caracteres del hash
- **Scroll:** Si hay muchos logs, aparece scroll vertical

##### **Columna 2: Decisiones HITL**
Card con scroll independiente (max-height: 600px):
- **Header:** "Decisiones HITL" con icono de usuarios (👥) y badge con cantidad
- **Lista de Decisiones:**
  - Cada decisión muestra:
    - **Tipo:** Badge con tipo de decisión
    - **Decisión:** Badge con color:
      - Verde: APPROVED
      - Rojo: REJECTED
      - Amarillo: PENDING
    - **Fecha:** Fecha formateada
    - **Usuario:** ID del usuario
    - **Notas:** Texto de la decisión (si existe)
- **Scroll:** Si hay muchas decisiones, aparece scroll vertical
- **Si no hay decisiones:** Muestra mensaje "No hay decisiones HITL"

##### **Columna 3: Outputs Generados**
Card con scroll independiente (max-height: 600px):
- **Header:** "Outputs Generados" con icono de actividad (📊) y badge con cantidad
- **Lista de Outputs:**
  - Cada output muestra:
    - **ID:** Badge "Output #X"
    - **Confidence:** Porcentaje de confianza
    - **Input:** Primeros 60 caracteres del input (truncado)
    - **Output:** Texto completo del output
    - **Fecha:** Timestamp formateado
- **Scroll:** Si hay muchos outputs, aparece scroll vertical
- **Si no hay outputs:** Muestra mensaje "No hay outputs generados"

#### 4. **Entidades Relacionadas**
Card mostrando entidades relacionadas:
- **Header:** "Entidades Relacionadas" con icono de red (🔗) y badge con cantidad
- **Grid de Cards (2-4 columnas según pantalla):**
  - Cada card muestra:
    - **Tipo de Entidad:** Badge con tipo
    - **Tipo de Relación:** Badge con relación (TRAINED_WITH, BELONGS_TO, USES, GENERATED_BY)
    - **Nombre:** Nombre de la entidad relacionada
    - **ID:** Identificador
    - **Botón "Ver Detalles":** Navega a la trazabilidad de esa entidad

### Funcionalidades

#### Exportar Evidencias

1. **Exportar JSON**
   - Hacer clic en **"Exportar JSON"** en el header
   - Se descarga un archivo JSON con:
     - Información completa de la entidad
     - Todos los logs inmutables
     - Todas las decisiones HITL
     - Todos los outputs generados
     - Verificación de integridad
     - Timestamp de exportación

2. **Exportar PDF**
   - Hacer clic en **"Exportar PDF"** en el header
   - Se descarga un archivo PDF formateado (cuando esté implementado)
   - Incluye toda la información de forma estructurada

#### Navegar a Entidades Relacionadas

1. **Desde Entidades Relacionadas**
   - Revisar la sección "Entidades Relacionadas"
   - Hacer clic en **"Ver Detalles"** de cualquier entidad relacionada
   - Se navega a la trazabilidad de esa entidad

2. **Navegación**
   - URL cambia a: `/governance/compliance/traceability/[nuevoTipo]/[nuevoId]`
   - Se carga la trazabilidad completa de la nueva entidad

#### Revisar Verificación de Integridad

1. **Ver Score**
   - Revisar el card "Información y Verificación de Integridad"
   - Ver el porcentaje de integridad (debe ser 100% para INTEGRITY_OK)

2. **Revisar Logs Verificados**
   - Ver cuántos logs están verificados vs total
   - Si hay diferencia, revisar logs individuales

3. **Si hay Problemas**
   - Se muestra banner amarillo con lista de problemas
   - Revisar cada problema
   - El sistema dispara automáticamente un workflow BPMN de alerta

---

## 🎓 TUTORIALES PASO A PASO

### Tutorial 1: Consultar Trazabilidad de un Modelo

**Objetivo:** Ver la trazabilidad completa de un modelo específico.

**Pasos:**

1. **Acceder a Traceability**
   - Navegar a Governance → Compliance → Traceability
   - O ir directamente a `/governance/compliance/traceability`

2. **Buscar el Modelo**
   - Usar el filtro "Buscar por Entidad" para buscar el modelo
   - O navegar por la lista de evidencias
   - Encontrar el card del modelo deseado

3. **Ver Detalles**
   - Hacer clic en **"Ver Detalles"** del modelo
   - Se abre la página de detalle: `/governance/compliance/traceability/Model/123`

4. **Revisar Información**
   - Ver información del modelo en el header
   - Revisar verificación de integridad (debe ser INTEGRITY_OK)
   - Revisar logs inmutables (scroll si hay muchos)
   - Revisar decisiones HITL
   - Revisar outputs generados

5. **Explorar Relaciones**
   - Revisar "Entidades Relacionadas"
   - Ver datasets relacionados (TRAINED_WITH)
   - Ver proyectos relacionados (BELONGS_TO)
   - Hacer clic en "Ver Detalles" para navegar a otra entidad

---

### Tutorial 2: Exportar Evidencias para Auditoría

**Objetivo:** Generar evidencias exportables para una auditoría regulatoria.

**Pasos:**

1. **Acceder a Trazabilidad**
   - Navegar a la trazabilidad de la entidad de interés
   - Ejemplo: `/governance/compliance/traceability/Model/123`

2. **Verificar Integridad**
   - Revisar el score de integridad
   - Debe ser 100% (INTEGRITY_OK) para auditorías
   - Si no, investigar problemas antes de exportar

3. **Exportar JSON**
   - Hacer clic en **"Exportar JSON"** en el header
   - Se descarga el archivo `traceability-Model-123.json`
   - El archivo contiene toda la información estructurada

4. **Exportar PDF (cuando esté disponible)**
   - Hacer clic en **"Exportar PDF"** en el header
   - Se descarga el archivo `traceability-Model-123.pdf`
   - El PDF está formateado para presentación

5. **Usar para Auditoría**
   - Presentar el archivo a auditores
   - Demostrar trazabilidad completa
   - Mostrar verificación de integridad

---

### Tutorial 3: Verificar Integridad de Logs

**Objetivo:** Verificar que los logs inmutables no han sido modificados.

**Pasos:**

1. **Acceder a Trazabilidad**
   - Navegar a cualquier entidad con trazabilidad
   - Ejemplo: `/governance/compliance/traceability/Project/1`

2. **Revisar Verificación de Integridad**
   - Ver el card "Información y Verificación de Integridad"
   - Revisar:
     - **Score de Integridad:** Debe ser 100%
     - **Logs Verificados:** Debe ser igual a Total de Logs
     - **Estado:** Debe ser INTEGRITY_OK

3. **Si hay Problemas**
   - Si el estado es INTEGRITY_WARNING o INTEGRITY_ERROR:
     - Revisar el banner amarillo con problemas
     - Revisar logs individuales
     - Buscar dónde se rompió la cadena de hashes
     - El sistema dispara automáticamente un workflow BPMN de alerta

4. **Investigar**
   - Revisar cada log en la lista
   - Verificar que cada log tiene el hash correcto del anterior
   - Identificar el log problemático

5. **Resolver**
   - Seguir el workflow BPMN de alerta
   - Corregir el problema detectado
   - Re-verificar integridad

---

### Tutorial 4: Explorar Cadena Model-Dataset-Output

**Objetivo:** Reconstruir la cadena completa modelo-dataset-output.

**Pasos:**

1. **Empezar con un Output**
   - Acceder a la trazabilidad de un modelo
   - Revisar la sección "Outputs Generados"
   - Identificar un output de interés

2. **Ver el Modelo**
   - Ya estás en la trazabilidad del modelo
   - Ver información del modelo
   - Ver logs de entrenamiento y despliegue

3. **Ver el Dataset**
   - Revisar "Entidades Relacionadas"
   - Buscar entidad de tipo "Dataset" con relación "TRAINED_WITH"
   - Hacer clic en **"Ver Detalles"** del dataset
   - Ver trazabilidad del dataset

4. **Ver el Proyecto**
   - Desde el modelo, revisar "Entidades Relacionadas"
   - Buscar entidad de tipo "Project" con relación "BELONGS_TO"
   - Hacer clic en **"Ver Detalles"** del proyecto
   - Ver trazabilidad del proyecto
   - Ver todos los modelos del proyecto

5. **Construir la Cadena Completa**
   - Dataset → Modelo → Output
   - Proyecto → Modelos → Outputs
   - Logs → Decisiones → Outputs

---

### Tutorial 5: Buscar Trazabilidad con Filtros

**Objetivo:** Encontrar evidencias específicas usando filtros avanzados.

**Pasos:**

1. **Acceder a Página Principal**
   - Navegar a `/governance/compliance/traceability`

2. **Aplicar Filtros**
   - **Buscar por Entidad:** Ingresar nombre parcial (ej: "Credit")
   - **Filtro por Fecha:** Seleccionar rango (ej: Último mes)
   - **Filtro por Usuario:** Ingresar ID de usuario (ej: "ml-engineer@example.com")
   - Hacer clic en **"Buscar"**

3. **Revisar Resultados**
   - La lista se filtra automáticamente
   - Solo se muestran evidencias que coinciden con todos los filtros
   - Ver cantidad de resultados

4. **Ajustar Filtros**
   - Si hay demasiados resultados, agregar más filtros
   - Si hay pocos resultados, quitar filtros
   - Usar **"Limpiar"** para empezar de nuevo

5. **Ver Detalles**
   - Hacer clic en **"Ver Detalles"** de cualquier resultado
   - Ver trazabilidad completa filtrada

---

## 💡 CONSEJOS Y MEJORES PRÁCTICAS

### Para Compliance Officers

1. **Revisar Integridad Regularmente**
   - Verificar que todos los sistemas tienen INTEGRITY_OK
   - Investigar inmediatamente si hay INTEGRITY_WARNING o INTEGRITY_ERROR
   - Seguir workflows BPMN de alerta

2. **Exportar Evidencias Periódicamente**
   - Exportar evidencias de sistemas críticos mensualmente
   - Almacenar para auditorías futuras
   - Verificar que score de integridad sea 100% antes de exportar

3. **Documentar Decisiones HITL**
   - Asegurar que todas las decisiones importantes tienen notas
   - Revisar decisiones de aprobación regularmente
   - Mantener trazabilidad de aprobaciones

### Para Auditores

1. **Usar Exportación JSON**
   - Exportar en JSON para análisis programático
   - Verificar estructura completa
   - Validar hash chains manualmente si es necesario

2. **Revisar Verificación de Integridad**
   - Siempre verificar que score = 100%
   - Si no, investigar causa antes de aceptar evidencias
   - Revisar logs individuales si hay dudas

3. **Seguir la Cadena Completa**
   - Empezar con un output
   - Seguir hasta el modelo
   - Seguir hasta el dataset
   - Verificar todas las relaciones

### Para Project Managers

1. **Revisar Trazabilidad de Proyectos**
   - Acceder a trazabilidad del proyecto
   - Ver todos los modelos asociados
   - Ver logs y decisiones del proyecto
   - Usar para reportes ejecutivos

2. **Explorar Relaciones**
   - Usar "Entidades Relacionadas" para navegar
   - Construir vista completa del ecosistema
   - Identificar dependencias

3. **Verificar Integridad**
   - Asegurar que proyectos críticos tienen INTEGRITY_OK
   - Investigar problemas inmediatamente
   - Documentar resoluciones

### Para Equipos Técnicos

1. **Revisar Logs Regularmente**
   - Verificar que todos los logs están verificados
   - Revisar acciones registradas
   - Identificar patrones o problemas

2. **Documentar Outputs**
   - Asegurar que outputs importantes están registrados
   - Incluir confidence scores
   - Mantener relación con inputs

3. **Mantener Hash Chains**
   - No modificar logs existentes
   - Asegurar que cada log tiene hash correcto
   - Reportar problemas de integridad inmediatamente

---

## ❓ PREGUNTAS FRECUENTES

### ¿Qué significa INTEGRITY_OK, INTEGRITY_WARNING, INTEGRITY_ERROR?
- **INTEGRITY_OK:** Todos los logs están verificados (score = 100%)
- **INTEGRITY_WARNING:** Algunos logs no están verificados (50% <= score < 100%)
- **INTEGRITY_ERROR:** Muchos logs no están verificados (score < 50%)

### ¿Puedo modificar logs inmutables?
**No.** Los logs inmutables no pueden ser modificados. Cualquier modificación rompería la cadena de hashes y se detectaría automáticamente.

### ¿Qué pasa si detecto un problema de integridad?
El sistema dispara automáticamente un workflow BPMN de alerta. Debes seguir el workflow para investigar y resolver el problema.

### ¿Cómo obtengo los modelos de un proyecto?
Accede a la trazabilidad del proyecto (`/governance/compliance/traceability/Project/X`). La sección "Entidades Relacionadas" mostrará todos los modelos asociados a través de deployments.

### ¿Puedo exportar evidencias de múltiples entidades a la vez?
Actualmente, la exportación es por entidad individual. Para múltiples entidades, exporta cada una por separado.

### ¿Qué formato de exportación debo usar?
- **JSON:** Para análisis programático o integración con otras herramientas
- **PDF:** Para presentación a auditores o autoridades (cuando esté implementado)

### ¿Cómo navego entre entidades relacionadas?
Haz clic en **"Ver Detalles"** en cualquier entidad relacionada. Se navegará automáticamente a su trazabilidad completa.

---

**Última Actualización:** Diciembre 2025
