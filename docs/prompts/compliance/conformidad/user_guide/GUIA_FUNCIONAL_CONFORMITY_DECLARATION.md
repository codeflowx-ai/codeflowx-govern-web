# 📘 GUÍA FUNCIONAL - CONFORMITY DECLARATION (DECLARACIÓN DE CONFORMIDAD)

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Usuarios finales, Compliance Officers, Project Managers

---

## 🎯 ¿QUÉ ES CONFORMITY DECLARATION?

Conformity Declaration (Declaración de Conformidad) es un sistema para **emitir y gestionar declaraciones de conformidad** según el EU AI Act Art. 48 y Annex V. Su objetivo es permitir a los proveedores de sistemas de IA de alto riesgo declarar que su sistema cumple con los requisitos del EU AI Act antes de ponerlo en el mercado.

### 🎯 Propósito Principal

El sistema de Conformity Declaration permite:

1. **Emisión de Declaraciones:** Crear declaraciones de conformidad basadas en assessments de compliance completados
2. **Gestión de Versiones:** Gestionar múltiples versiones de declaraciones para un mismo proyecto
3. **Firma Digital:** Firmar declaraciones para hacerlas oficiales
4. **Documentación:** Generar y descargar PDFs de declaraciones
5. **Cumplimiento Normativo:** Cumplimiento con EU AI Act Art. 48 y Annex V
6. **Trazabilidad:** Rastrear la evolución de declaraciones por proyecto

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículos Relevantes

#### Art. 48 - EU Declaration of Conformity
Establece la obligación de que los **proveedores de sistemas de IA de alto riesgo** emitan una **declaración de conformidad** antes de poner el sistema en el mercado. La declaración debe:

- Confirmar que el sistema cumple con los requisitos del EU AI Act
- Incluir información del proveedor y del sistema
- Estar firmada por el proveedor
- Ser accesible para autoridades competentes

#### Annex V - Template de Declaración
Define el **template oficial** que debe seguir la declaración de conformidad, incluyendo:

- Información del proveedor
- Información del sistema de IA
- Base legal de conformidad (Annex VI)
- Artículos cumplidos (Art. 9-15)
- Firma y fecha

---

## 🚀 ¿PARA QUÉ SIRVE CONFORMITY DECLARATION?

### 1. **Para Sistemas de IA de Alto Riesgo (Obligatorio)**

Según el **EU AI Act**, los sistemas clasificados como de **alto riesgo** (Anexo III) **DEBEN** emitir una declaración de conformidad antes de poner el sistema en el mercado. Esto incluye:

- ✅ Sistemas de biometría
- ✅ Sistemas de gestión de infraestructura crítica
- ✅ Sistemas de educación y formación profesional
- ✅ Sistemas de empleo y gestión de trabajadores
- ✅ Sistemas de acceso a servicios públicos
- ✅ Sistemas de aplicación de la ley
- ✅ Sistemas de migración y asilo
- ✅ Sistemas de administración de justicia

**En estos casos, Conformity Declaration es OBLIGATORIO por ley.**

### 2. **Para TODOS los Sistemas de IA (Recomendado)**

**IMPORTANTE:** El sistema de Conformity Declaration de CodeflowX está diseñado para ser **válido y útil** para todos los sistemas de IA, no solo los de alto riesgo.

#### Beneficios para Sistemas No-Alto-Riesgo:

1. **Demostración de Cumplimiento**
   - Documentación formal de cumplimiento
   - Evidencia para stakeholders
   - Preparación para futuras regulaciones

2. **Gestión de Versiones**
   - Rastreo de evolución del sistema
   - Historial de declaraciones
   - Comparación entre versiones

3. **Mejora de Confianza**
   - Demostración de compromiso con calidad
   - Transparencia con usuarios
   - Ventaja competitiva

4. **Gestión de Riesgos**
   - Documentación de cumplimiento
   - Protección legal
   - Reducción de riesgos regulatorios

---

## 📋 FUNCIONALIDADES PRINCIPALES

### 1. **Gestión de Proyectos con Declaraciones**

**Descripción:**
Pantalla centralizada que lista todos los proyectos y muestra información sobre sus declaraciones de conformidad.

**Características:**
- Lista paginada de proyectos
- Estadísticas globales (total proyectos, con declaraciones, total declaraciones, firmadas, borradores)
- Filtros (todos, con declaraciones, sin declaraciones)
- Búsqueda por nombre de proyecto
- Cards con información resumida por proyecto
- Información de última declaración (fecha, estado, versión)
- Botones de acción (Gestionar Declaraciones / Crear Declaración)

**Cuándo Usar:**
- Ver todos los proyectos y su estado de declaraciones
- Encontrar proyectos que necesitan declaraciones
- Acceder rápidamente a la gestión de declaraciones de un proyecto

---

### 2. **Gestión de Declaraciones por Proyecto**

**Descripción:**
Pantalla para gestionar todas las declaraciones de conformidad de un proyecto específico.

**Características:**
- Header con información del proyecto
- Estadísticas en tiempo real (total, firmadas, borradores, assessments disponibles)
- Selección de assessment para generar nueva declaración
- Vista previa de declaración antes de crear
- Tabla de declaraciones existentes con:
  - Versión
  - Fecha de creación
  - Estado (DRAFT/SIGNED)
  - Sistema IA
  - Proveedor
- Acciones por declaración:
  - Firmar (solo DRAFT)
  - Descargar PDF
- Filtrado automático por proyecto (vía URL)

**Cuándo Usar:**
- Gestionar declaraciones de un proyecto específico
- Crear nueva declaración desde un assessment
- Firmar declaraciones pendientes
- Descargar PDFs de declaraciones

---

### 3. **Creación de Declaración desde Assessment**

**Descripción:**
Crear una nueva declaración de conformidad basada en un assessment de compliance completado.

**Requisitos:**
- El assessment debe estar **completado** y **listo para certificación** (`readyForCertification = true`)
- Debe haber completado todos los pasos del assessment (Step 1, 2, 3, 4)
- El overall score debe ser calculado

**Proceso:**
1. Seleccionar un assessment de la lista de assessments disponibles
2. El sistema muestra una vista previa con:
   - Nombre del sistema IA (del assessment)
   - Nombre del proveedor (ingresado manualmente)
   - Información del assessment
3. Confirmar creación
4. La declaración se crea en estado **DRAFT**
5. La versión se genera automáticamente: `v1.{count + 1}`

**Datos Copiados del Assessment:**
- Overall compliance score
- Compliance percentage
- Compliance por artículo (Art. 9-15)
- Información del proyecto

**Cuándo Usar:**
- Después de completar un assessment de compliance
- Cuando el assessment está listo para certificación
- Antes de poner el sistema en el mercado

---

### 4. **Firma de Declaración**

**Descripción:**
Firmar una declaración de conformidad para hacerla oficial.

**Requisitos:**
- La declaración debe estar en estado **DRAFT**
- Solo declaraciones DRAFT pueden ser firmadas
- Una vez firmada, no puede ser editada ni firmada nuevamente

**Proceso:**
1. En la tabla de declaraciones, encontrar la declaración en estado DRAFT
2. Hacer clic en el botón "Firmar"
3. Confirmar la acción
4. La declaración cambia a estado **SIGNED**
5. Se registra:
   - Usuario que firmó (`signedBy`)
   - Fecha de firma (`signatureDate`)
   - Firma digital (opcional)

**Nota:** Para producción, se recomienda integrar con eIDAS para firma digital avanzada con validez legal.

**Cuándo Usar:**
- Cuando la declaración está completa y lista para ser oficial
- Antes de poner el sistema en el mercado
- Cuando se requiere documentación oficial

---

### 5. **Descarga de PDF**

**Descripción:**
Generar y descargar el documento PDF de una declaración de conformidad.

**Características:**
- PDF generado según template de Annex V
- Incluye toda la información de la declaración
- Nombre de archivo: `declaration_{id}.pdf`
- Descarga automática al hacer clic

**Nota:** La estructura está implementada. Para producción, se recomienda usar template oficial de Annex V.

**Cuándo Usar:**
- Para documentación oficial
- Para registro en autoridades
- Para archivo interno
- Para compartir con stakeholders

---

### 6. **Gestión de Versiones**

**Descripción:**
Gestionar múltiples versiones de declaraciones para un mismo proyecto.

**Características:**
- Versiones automáticas: `v1.0`, `v1.1`, `v1.2`, etc.
- Ordenamiento por fecha de creación (más reciente primero)
- Visualización de versión en tabla
- Historial completo de versiones

**Cuándo Usar:**
- Cuando se actualiza el sistema y se necesita nueva declaración
- Cuando se corrige una declaración anterior
- Para rastrear evolución del sistema

---

## 🔄 FLUJO DE TRABAJO TÍPICO

### Escenario 1: Primera Declaración de un Proyecto

1. **Completar Assessment de Compliance**
   - Ir a Compliance Assessment
   - Completar todos los pasos (Step 1, 2, 3, 4)
   - Calcular overall score
   - Asegurar que `readyForCertification = true`

2. **Ir a Conformity Declaration**
   - Navegar a `/governance/compliance/conformity-declaration/projects`
   - Encontrar el proyecto
   - Hacer clic en "Gestionar Declaraciones"

3. **Crear Declaración**
   - Seleccionar el assessment completado
   - Ingresar nombre del proveedor
   - Revisar vista previa
   - Confirmar creación
   - La declaración se crea en estado DRAFT, versión `v1.0`

4. **Firmar Declaración**
   - En la tabla, encontrar la declaración DRAFT
   - Hacer clic en "Firmar"
   - Confirmar
   - La declaración cambia a SIGNED

5. **Descargar PDF**
   - Hacer clic en "Descargar PDF"
   - Guardar el archivo para documentación

---

### Escenario 2: Actualización de Declaración (Nueva Versión)

1. **Actualizar Assessment**
   - Si el sistema cambió, actualizar el assessment
   - Completar nuevos pasos si es necesario
   - Recalcular overall score

2. **Crear Nueva Declaración**
   - Ir a gestión de declaraciones del proyecto
   - Seleccionar el assessment actualizado
   - Crear nueva declaración
   - La nueva versión será `v1.1` (o siguiente)

3. **Firmar Nueva Versión**
   - Firmar la nueva declaración
   - La versión anterior queda en el historial

---

## 📊 ESTADOS DE DECLARACIÓN

### DRAFT (Borrador)
- Estado inicial al crear la declaración
- Puede ser editada (futuro)
- Puede ser firmada
- No es oficial

### SIGNED (Firmada)
- Declaración firmada por el proveedor
- Es oficial y válida
- No puede ser editada
- No puede ser firmada nuevamente
- Lista para uso y registro

### PUBLISHED (Publicada) - Futuro
- Declaración publicada en Registro EU (Art. 49)
- Estado final
- No puede ser modificada

### REVOKED (Revocada) - Futuro
- Declaración revocada
- Ya no es válida
- Se mantiene en historial

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones al Crear Declaración

1. **Assessment debe existir**
   - El assessment ID debe ser válido
   - El assessment debe estar en la base de datos

2. **Assessment debe estar listo para certificación**
   - `readyForCertification = true`
   - Todos los pasos completados
   - Overall score calculado

3. **Proyecto debe estar asociado**
   - El assessment debe tener un proyecto asociado

### Validaciones al Firmar Declaración

1. **Declaración debe existir**
   - El ID de declaración debe ser válido

2. **Declaración debe estar en DRAFT**
   - Solo declaraciones DRAFT pueden ser firmadas
   - Si ya está SIGNED, no se puede firmar nuevamente

3. **signedBy es requerido**
   - Debe especificar quién firma la declaración

---

## 🎯 MEJORES PRÁCTICAS

### 1. **Completar Assessment Antes de Crear Declaración**
- Asegurar que el assessment esté completo
- Verificar que todos los pasos estén aprobados
- Calcular overall score antes de crear

### 2. **Revisar Vista Previa Antes de Crear**
- Verificar que la información sea correcta
- Confirmar nombre del proveedor y sistema
- Revisar scores y compliance

### 3. **Firmar Solo Cuando Esté Completa**
- No firmar declaraciones incompletas
- Revisar toda la información antes de firmar
- Una vez firmada, no se puede editar

### 4. **Mantener Historial de Versiones**
- No eliminar declaraciones antiguas
- Mantener todas las versiones para trazabilidad
- Usar versiones para rastrear evolución

### 5. **Descargar PDFs para Archivo**
- Descargar PDFs de todas las declaraciones firmadas
- Guardar en sistema de archivo
- Mantener copias de seguridad

---

## 🔗 INTEGRACIÓN CON OTROS MÓDULOS

### Compliance Assessment
- **Relación:** Las declaraciones se crean desde assessments
- **Dependencia:** Assessment debe estar completo y listo para certificación
- **Datos compartidos:** Scores, compliance por artículo, información del proyecto

### EU Registration (Art. 49) - Futuro
- **Relación:** Las declaraciones firmadas pueden publicarse en Registro EU
- **Integración:** Automática o manual
- **Estado:** Opcional, recomendado para cumplimiento completo

---

## 📝 NOTAS IMPORTANTES

1. **Una declaración por assessment:** Cada declaración está vinculada a un assessment específico. Para crear múltiples declaraciones, se necesitan múltiples assessments.

2. **Versiones automáticas:** Las versiones se generan automáticamente basadas en el número de declaraciones del proyecto. No se puede editar manualmente.

3. **Estados inmutables:** Una vez que una declaración está SIGNED, no puede volver a DRAFT ni ser editada.

4. **Firma digital:** La implementación actual permite firma básica. Para producción, se recomienda integrar con eIDAS.

5. **PDF template:** La estructura está implementada. Para producción, se recomienda usar template oficial de Annex V.

---

## 🆘 PREGUNTAS FRECUENTES

### ¿Puedo crear una declaración sin completar el assessment?
No. El assessment debe estar completo y listo para certificación (`readyForCertification = true`).

### ¿Puedo editar una declaración después de crearla?
Actualmente no. Las declaraciones son inmutables una vez creadas. Para cambios, crear una nueva versión.

### ¿Puedo firmar una declaración dos veces?
No. Solo las declaraciones en estado DRAFT pueden ser firmadas, y solo una vez.

### ¿Cómo se generan las versiones?
Las versiones se generan automáticamente: `v1.{count + 1}`, donde `count` es el número de declaraciones existentes del proyecto.

### ¿Puedo eliminar una declaración?
No. Las declaraciones no se eliminan para mantener trazabilidad. Se mantienen en el historial.

### ¿Qué pasa si el assessment cambia después de crear la declaración?
La declaración no se actualiza automáticamente. Debe crear una nueva declaración desde el assessment actualizado.

---

## 🔗 REFERENCIAS

- **Guía de Uso de Pantallas:** `docs/prompts/compliance/conformidad/user_guide/GUIA_USO_PANTALLAS_CONFORMITY_DECLARATION.md`
- **Developer Guide Frontend:** `docs/prompts/compliance/conformidad/DEVELOPER_GUIDE_FRONTEND.md`
- **Developer Guide Backend:** `docs/prompts/compliance/conformidad/DEVELOPER_GUIDE_BACKEND.md`
- **EU AI Act Art. 48:** EU Declaration of Conformity
- **EU AI Act Annex V:** Template de Declaración
