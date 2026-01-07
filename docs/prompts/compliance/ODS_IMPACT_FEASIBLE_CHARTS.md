# 📊 GRÁFICAS FACTIBLES PARA DASHBOARDS ODS

**Fecha:** Diciembre 2025
**Propósito:** Lista de gráficas que SÍ pueden calcularse con datos reales de las tablas existentes

---

## ✅ GRÁFICAS FACTIBLES (CON DATOS REALES)

### **ODS 16 - Instituciones Sólidas**

#### ✅ Factibles:
1. **Evolución de Tasa de Trazabilidad** (Línea temporal)
   - **Fuente:** `IMLIMMUTABLELOGS.IMLTIMESTAMP` + `PRJPROJECTS`
   - **Query:** Agregar por mes/año proyectos con logs vs. total proyectos

2. **Sistemas Certificados vs. Pendientes** (Barras)
   - **Fuente:** `COMCOMPLIANCEASSESSMENTS.READYFORCERTIFICATION` + `PRJPROJECTS.PRJISHIGHRISK`
   - **Query:** COUNT por estado (certificado, en proceso, pendiente)

3. **Score QMS por Módulo** (Barras/Heatmap)
   - **Fuente:** `GOVQUALITYMANAGEMENTSYSTEMS.QMSMODULESCORES` (JSONB)
   - **Query:** Parsear JSONB y extraer scores de 13 módulos

4. **Decisiones HITL con/sin Supervisión** (Barras)
   - **Fuente:** `GOVHITLDECISIONS` + `GOVHITLSUPERVISIONS`
   - **Query:** COUNT decisiones con/sin HITL

5. **Tiempo Promedio de Respuesta HITL** (Línea temporal)
   - **Fuente:** `GOVHITLDECISIONS.HITLDECISIONDATE` - `GOVHITLSUPERVISIONS.HITLCREATEDAT`
   - **Query:** AVG tiempo de respuesta por mes

6. **Tasa de Registro en BD UE** (Barras)
   - **Fuente:** `REGEUREGISTRATIONS.REGSTATUS` + `PRJPROJECTS.PRJISHIGHRISK`
   - **Query:** COUNT registrados vs. pendientes

#### ❌ NO Factibles (eliminar):
- Trazabilidad por Organización (no hay campo organización en PRJPROJECTS confirmado)
- Top 10 Sistemas sin Trazabilidad (necesita cálculo complejo de completitud)

---

### **ODS 9 - Innovación**

#### ✅ Factibles:
1. **Score de Documentación por Proyecto** (Barras)
   - **Fuente:** `GOVAIAACTTECHNICALDOCS.TECHSECTIONS` (JSONB)
   - **Query:** Parsear JSONB y contar secciones completas vs. total (11 secciones Anexo IV)

2. **Completitud por Sección Anexo IV** (Barras)
   - **Fuente:** `GOVAIAACTTECHNICALDOCS.TECHSECTIONS` (JSONB)
   - **Query:** Por cada sección (1-11), COUNT proyectos con sección completa / total proyectos

3. **Tiempo Promedio de Certificación** (Línea temporal)
   - **Fuente:** `COMCOMPLIANCEASSESSMENTS.UPDATEDAT` - `COMCOMPLIANCEASSESSMENTS.CREATEDAT`
   - **Query:** AVG días de certificación por mes

4. **Score QMS Promedio** (Línea temporal)
   - **Fuente:** `GOVQUALITYMANAGEMENTSYSTEMS.QMSOVERALLSCORE`
   - **Query:** AVG score por mes

#### ❌ NO Factibles (eliminar):
- Reutilización de Agentes (no confirmada relación AGTAGENTS-PRJPROJECTS)
- Reutilización de Prompts (no existe tabla PRMPROMPTS)
- Evolución de Reutilización (depende de lo anterior)
- Top 10 Activos Reutilizados (depende de lo anterior)
- Tiempo Ciclo MLOps (no hay tablas training/deployment)
- Modelos por Estado (no hay tabla de estados de modelos)
- Modelos con Drift (no hay detección de drift)
- LLMs Open Source (no hay telemetría específica)
- Top 10 LLMs Open Source (no hay telemetría)
- Proyectos con Documentación Incompleta (puede calcularse, pero mejor como tabla)

---

### **ODS 10 - Reducción de Desigualdades**

#### ✅ Factibles:
1. **FRIAs Completadas vs. Pendientes** (Barras)
   - **Fuente:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRIASTATUS` + `PRJPROJECTS.PRJISHIGHRISK`
   - **Query:** COUNT por estado (COMPLETED, PENDING, IN_PROGRESS)

2. **Riesgo Promedio en FRIA** (Línea temporal)
   - **Fuente:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRARISKS` (JSONB)
   - **Query:** Parsear JSONB y extraer overallRisk, AVG por mes

3. **Sistemas Prohibidos Detectados** (Barras - debe ser 0)
   - **Fuente:** `GOVPROHIBITEDSYSTEMS.PROHIBITEDACTIVE` + `PRJPROJECTS`
   - **Query:** COUNT proyectos con sistemas prohibidos activos

#### ❌ NO Factibles (eliminar):
- Sesgos en Datasets (no existe tabla DSDDATASETS)
- Score de Representatividad (no existe DSDDATASETS)
- Datasets con Sesgos Detectados (no existe DSDDATASETS)
- Prompts Validados (no existe tabla PRMPROMPTS)
- Evolución de Validación de Prompts (no existe tabla PRMPROMPTS)
- Prompts con Sesgos (no existe tabla PRMPROMPTS)

---

### **ODS 5 - Igualdad de Género**

#### ✅ Factibles:
1. **Sistemas Evaluados vs. Pendientes** (Barras)
   - **Fuente:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRARISKS` (JSONB)
   - **Query:** COUNT sistemas donde JSONB contiene `genderBiasAssessed: true` vs. total

2. **Medidas de Mitigación Implementadas** (Línea temporal)
   - **Fuente:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRARISKS` (JSONB)
   - **Query:** COUNT medidas de mitigación por mes (si está en JSONB)

#### ❌ NO Factibles (eliminar):
- Distribución de Género en Datasets (no existe DSDDATASETS)
- Score de Representación de Género (no existe DSDDATASETS)
- Datasets Desbalanceados (no existe DSDDATASETS)
- Sistemas con Sesgos de Género Detectados (puede calcularse de FRIA si tiene campo, pero mejor como tabla)

---

### **ODS 8 - Trabajo Decente**

#### ✅ Factibles:
1. **Decisiones con/sin HITL** (Barras)
   - **Fuente:** `GOVHITLDECISIONS` + `GOVHITLSUPERVISIONS`
   - **Query:** COUNT decisiones con/sin supervisión

2. **Tiempo Promedio de Respuesta HITL** (Línea temporal)
   - **Fuente:** `GOVHITLDECISIONS.HITLDECISIONDATE` - `GOVHITLSUPERVISIONS.HITLCREATEDAT`
   - **Query:** AVG tiempo de respuesta por mes

---

### **ODS 12 - Consumo Responsable**

#### ❌ NO Factibles (eliminar todas):
- Reutilización de Modelos (no confirmada relación MODMODELS-PRJPROJECTS)
- Reducción de Consumo Energético (no hay telemetría de recursos)
- Uso de Modelos Eficientes (no hay campo de optimización)

**Nota:** Este ODS necesita implementación de telemetría de recursos primero.

---

### **ODS 3 - Salud y Bienestar**

#### ❌ NO Factibles (eliminar todas):
- Detección de Incidentes (no existe tabla GOVINCIDENTS)
- Tiempo de Detección (no existe tabla GOVINCIDENTS)

**Nota:** Existe `GOVQMSSERIOUSINCIDENTS` pero es para QMS, no para sistemas de salud específicos.

---

### **ODS 17 - Alianzas**

#### ❌ NO Factibles (eliminar todas):
- Compartir Agentes en Marketplace (no hay campo en AGTAGENTS)
- Adopción de Recursos Compartidos (no hay telemetría)
- Top 10 Agentes Compartidos (no hay campo)

**Nota:** Este ODS necesita implementación de funcionalidad de marketplace primero.

---

### **ODS 4 - Educación**

#### ❌ NO Factibles (eliminar todas):
- Uso Educativo de LLMs Open Source (no hay telemetría categorizada por tipo de organización)
- Bases de Conocimiento Educativas (no hay tabla RAG con categorización)

**Nota:** Este ODS necesita categorización de organizaciones y RAG pipelines.

---

### **ODS 7 - Energía**

#### ❌ NO Factibles (eliminar todas):
- Reducción de Consumo Energético (no hay telemetría de recursos)
- Optimizaciones Implementadas (no hay tracking de optimizaciones)

**Nota:** Este ODS necesita implementación de telemetría de recursos primero.

---

## 📋 RESUMEN

### ✅ Dashboards con Gráficas Factibles:
- **ODS 16:** 6 gráficas factibles
- **ODS 9:** 4 gráficas factibles
- **ODS 10:** 3 gráficas factibles
- **ODS 5:** 2 gráficas factibles
- **ODS 8:** 2 gráficas factibles

### ❌ Dashboards SIN Gráficas Factibles:
- **ODS 12:** 0 gráficas factibles (eliminar secciones de gráficas)
- **ODS 3:** 0 gráficas factibles (eliminar secciones de gráficas)
- **ODS 17:** 0 gráficas factibles (eliminar secciones de gráficas)
- **ODS 4:** 0 gráficas factibles (eliminar secciones de gráficas)
- **ODS 7:** 0 gráficas factibles (eliminar secciones de gráficas)

---

## 🔄 ACCIONES REQUERIDAS

1. **Eliminar gráficas no factibles** de los dashboards
2. **Mantener solo KPIs** en dashboards sin gráficas factibles
3. **Añadir comentarios** en código indicando qué datos son necesarios para futuras gráficas
4. **Documentar** qué funcionalidades faltan para habilitar gráficas futuras
