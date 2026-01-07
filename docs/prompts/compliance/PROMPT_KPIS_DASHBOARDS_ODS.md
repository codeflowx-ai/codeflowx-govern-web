# 📊 PROMPT: KPIs Y DASHBOARDS DE IMPACTO ODS

**Fecha:** Diciembre 2025
**Versión:** 1.0
**Objetivo:** Definir KPIs y dashboards para medir el impacto de la plataforma en los Objetivos de Desarrollo Sostenible (ODS)

---

## 📋 CONTEXTO

La plataforma de gobernanza y compliance de IA contribuye a **10 Objetivos de Desarrollo Sostenible (ODS)** mediante:

- **11 módulos de compliance** (EU AI Act)
- **7 capacidades de gobernanza** (Agentes, Modelos, Datasets, Prompts, RAG, etc.)

**Necesidad:** Medir y visualizar el impacto real de la plataforma en cada ODS mediante KPIs específicos y dashboards interactivos.

---

## 🎯 OBJETIVOS DEL PROMPT

1. **Definir KPIs específicos** para cada ODS impactado
2. **Crear fórmulas de cálculo** para cada KPI
3. **Establecer metas y umbrales** de éxito
4. **Diseñar dashboards** de visualización
5. **Definir fuentes de datos** y agregaciones necesarias

---

## 📊 KPIs POR ODS

### **ODS 16: Paz, Justicia e Instituciones Sólidas**

#### **KPI 16.1: Tasa de Trazabilidad Completa**
- **Descripción:** Porcentaje de sistemas/activos con trazabilidad completa (modelo-dataset-output)
- **Fórmula:** `(Sistemas con trazabilidad completa / Total de sistemas) × 100`
- **Fuentes de Datos:**
  - Tabla: `IMLIMMUTABLELOGS` (logs inmutables)
  - Tabla: `PRJPROJECTS` (proyectos)
  - Tabla: `MODMODELS` (modelos)
  - Tabla: `AGTAGENTS` (agentes)
- **Meta:** 95%
- **Umbral Crítico:** < 80%
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por proyecto, por organización, global

#### **KPI 16.2: Tasa de Certificación de Sistemas**
- **Descripción:** Porcentaje de sistemas certificados según EU AI Act
- **Fórmula:** `(Sistemas certificados / Sistemas de alto riesgo) × 100`
- **Fuentes de Datos:**
  - Tabla: `COMCOMPLIANCEASSESSMENTS` (evaluaciones de conformidad)
  - Tabla: `PRJPROJECTS` (proyectos de alto riesgo)
- **Meta:** 80%
- **Umbral Crítico:** < 60%
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por organización, global

#### **KPI 16.3: Score Promedio de Compliance**
- **Descripción:** Score promedio de compliance QMS (0.00 - 1.00)
- **Fórmula:** `AVG(QMSOVERALLSCORE)`
- **Fuentes de Datos:**
  - Tabla: `GOVQUALITYMANAGEMENTSYSTEMS` (QMS)
- **Meta:** 0.85
- **Umbral Crítico:** < 0.70
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por proyecto, por organización, global

#### **KPI 16.4: Tasa de Registro en BD UE**
- **Descripción:** Porcentaje de sistemas de alto riesgo registrados en Base de Datos UE
- **Fórmula:** `(Sistemas registrados en BD UE / Sistemas de alto riesgo) × 100`
- **Fuentes de Datos:**
  - Tabla: `REGEUREGISTRATIONS` (registros UE)
  - Tabla: `PRJPROJECTS` (proyectos de alto riesgo)
- **Meta:** 100%
- **Umbral Crítico:** < 90%
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por organización, global

#### **KPI 16.5: Tasa de Aprobación Humana (HITL)**
- **Descripción:** Porcentaje de decisiones críticas con supervisión humana
- **Fórmula:** `(Decisiones con HITL / Decisiones críticas totales) × 100`
- **Fuentes de Datos:**
  - Tabla: `GOVHITLDECISIONS` (decisiones HITL)
  - Tabla: `GOVHITLSUPERVISIONS` (supervisiones)
- **Meta:** 100% para decisiones críticas
- **Umbral Crítico:** < 95%
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por proyecto, por tipo de decisión, global

#### **KPI 16.6: Tiempo Promedio de Auditoría**
- **Descripción:** Tiempo promedio para completar auditoría de decisiones (horas)
- **Fórmula:** `AVG(Tiempo de auditoría en horas)`
- **Fuentes de Datos:**
  - Tabla: `IMLIMMUTABLELOGS` (logs con timestamps)
- **Meta:** < 24 horas
- **Umbral Crítico:** > 72 horas
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por tipo de auditoría, global

---

### **ODS 9: Industria, Innovación e Infraestructura**

#### **KPI 9.1: Score QMS Promedio**
- **Descripción:** Score promedio del Sistema de Gestión de Calidad
- **Fórmula:** `AVG(QMSOVERALLSCORE)`
- **Fuentes de Datos:**
  - Tabla: `GOVQUALITYMANAGEMENTSYSTEMS`
- **Meta:** 0.85
- **Umbral Crítico:** < 0.70
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por organización, global

#### **KPI 9.2: Tasa de Documentación Completa**
- **Descripción:** Porcentaje de sistemas con documentación técnica completa (Anexo IV)
- **Fórmula:** `(Sistemas con doc completa / Total de sistemas) × 100`
- **Fuentes de Datos:**
  - Tabla: `GOVAIAACTTECHNICALDOCS` (documentación técnica)
  - Tabla: `MODMODELS` (modelos)
- **Meta:** 90%
- **Umbral Crítico:** < 75%
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por proyecto, global

#### **KPI 9.3: Tiempo Promedio de Certificación**
- **Descripción:** Tiempo promedio desde inicio hasta certificación (días)
- **Fórmula:** `AVG(Fecha certificación - Fecha inicio evaluación)`
- **Fuentes de Datos:**
  - Tabla: `COMCOMPLIANCEASSESSMENTS`
- **Meta:** < 30 días
- **Umbral Crítico:** > 60 días
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por tipo de evaluación, global

#### **KPI 9.4: Tasa de Reutilización de Agentes**
- **Descripción:** Porcentaje de agentes reutilizados vs. nuevos
- **Fórmula:** `(Agentes reutilizados / Total de agentes) × 100`
- **Fuentes de Datos:**
  - Tabla: `AGTAGENTS` (agentes)
  - Tabla: `AIOCOMPONENTS` (componentes AI OS)
- **Meta:** 60%
- **Umbral Crítico:** < 40%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por organización, global

#### **KPI 9.5: Tasa de Reutilización de Prompts**
- **Descripción:** Porcentaje de prompts reutilizados vs. nuevos
- **Fórmula:** `(Prompts reutilizados / Total de prompts) × 100`
- **Fuentes de Datos:**
  - Tabla: `PRMPROMPTS` (prompts)
- **Meta:** 70%
- **Umbral Crítico:** < 50%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por proyecto, global

#### **KPI 9.6: Tiempo Promedio de Ciclo MLOps**
- **Descripción:** Tiempo promedio de entrenamiento-despliegue (días)
- **Fórmula:** `AVG(Fecha despliegue - Fecha inicio entrenamiento)`
- **Fuentes de Datos:**
  - Tabla: `MODMODELS` (modelos con fechas)
  - Tabla: `AIOCOMPONENTS` (componentes con versionado)
- **Meta:** < 7 días
- **Umbral Crítico:** > 14 días
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por tipo de modelo, global

#### **KPI 9.7: Tasa de Adopción de LLMs Open Source**
- **Descripción:** Porcentaje de uso de LLMs open source vs. propietarios
- **Fórmula:** `(Uso LLMs open source / Total uso LLMs) × 100`
- **Fuentes de Datos:**
  - Tabla: `AIOCOMPONENTS` (componentes con tipo)
  - Tabla: Telemetría de uso
- **Meta:** 40%
- **Umbral Crítico:** < 20%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por organización, global

---

### **ODS 10: Reducción de las Desigualdades**

#### **KPI 10.1: Tasa de FRIA Completada**
- **Descripción:** Porcentaje de sistemas de alto riesgo con FRIA completada
- **Fórmula:** `(FRIAs completadas / Sistemas de alto riesgo) × 100`
- **Fuentes de Datos:**
  - Tabla: `FRIAFUNDAMENTALRIGHTSASSESSMENTS` (FRIAs)
  - Tabla: `PRJPROJECTS` (proyectos de alto riesgo)
- **Meta:** 100%
- **Umbral Crítico:** < 90%
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por organización, global

#### **KPI 10.2: Riesgo Promedio en FRIA**
- **Descripción:** Riesgo promedio calculado en evaluaciones FRIA (0.00 - 1.00)
- **Fórmula:** `AVG(FRAFINALRISK)`
- **Fuentes de Datos:**
  - Tabla: `FRIAFUNDAMENTALRIGHTSASSESSMENTS`
- **Meta:** < 0.50
- **Umbral Crítico:** > 0.75
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por categoría de riesgo, global

#### **KPI 10.3: Tasa de Detección de Sesgos en Datasets**
- **Descripción:** Porcentaje de datasets con análisis de sesgos completado
- **Fórmula:** `(Datasets con análisis de sesgos / Total de datasets) × 100`
- **Fuentes de Datos:**
  - Tabla: `DSDDATASETS` (datasets)
  - Tabla: Análisis de sesgos (a definir)
- **Meta:** 100%
- **Umbral Crítico:** < 80%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por tipo de dataset, global

#### **KPI 10.4: Score de Representatividad de Datasets**
- **Descripción:** Score promedio de representatividad de grupos en datasets (0.00 - 1.00)
- **Fórmula:** `AVG(Score de representatividad)`
- **Fuentes de Datos:**
  - Tabla: `DSDDATASETS` (con métricas de representatividad)
- **Meta:** 0.80
- **Umbral Crítico:** < 0.60
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por tipo de dataset, global

#### **KPI 10.5: Tasa de Sistemas Prohibidos Detectados**
- **Descripción:** Número de sistemas prohibidos detectados (meta: 0)
- **Fórmula:** `COUNT(Sistemas prohibidos detectados)`
- **Fuentes de Datos:**
  - Tabla: `GOVPROHIBITEDSYSTEMS` (sistemas prohibidos)
  - Tabla: `PRJPROJECTS` (proyectos bloqueados)
- **Meta:** 0 (prevención total)
- **Umbral Crítico:** > 0
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por categoría, global

#### **KPI 10.6: Tasa de Prompts Validados por Sesgos**
- **Descripción:** Porcentaje de prompts validados para detectar sesgos
- **Fórmula:** `(Prompts validados / Total de prompts) × 100`
- **Fuentes de Datos:**
  - Tabla: `PRMPROMPTS` (prompts con validación)
- **Meta:** 100%
- **Umbral Crítico:** < 80%
- **Frecuencia de Medición:** Semanal
- **Agregación:** Por proyecto, global

---

### **ODS 5: Igualdad de Género**

#### **KPI 5.1: Tasa de Datasets Balanceados por Género**
- **Descripción:** Porcentaje de datasets con balance de género adecuado
- **Fórmula:** `(Datasets balanceados / Total de datasets) × 100`
- **Fuentes de Datos:**
  - Tabla: `DSDDATASETS` (con métricas de género)
- **Meta:** 90%
- **Umbral Crítico:** < 70%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por tipo de dataset, global

#### **KPI 5.2: Score de Representación de Género**
- **Descripción:** Score promedio de representación de género en datasets (0.00 - 1.00)
- **Fórmula:** `AVG(Score de representación de género)`
- **Fuentes de Datos:**
  - Tabla: `DSDDATASETS` (con métricas de género)
- **Meta:** 0.85
- **Umbral Crítico:** < 0.70
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por tipo de dataset, global

#### **KPI 5.3: Tasa de Sistemas con Evaluación de Sesgos de Género**
- **Descripción:** Porcentaje de sistemas evaluados por sesgos de género
- **Fórmula:** `(Sistemas evaluados / Total de sistemas) × 100`
- **Fuentes de Datos:**
  - Tabla: `FRIAFUNDAMENTALRIGHTSASSESSMENTS` (FRIA con evaluación de género)
  - Tabla: Análisis de sesgos
- **Meta:** 100% para sistemas de alto riesgo
- **Umbral Crítico:** < 80%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por organización, global

---

### **ODS 8: Trabajo Decente y Crecimiento Económico**

#### **KPI 8.1: Tasa de Supervisión Humana (HITL)**
- **Descripción:** Porcentaje de decisiones críticas con supervisión humana
- **Fórmula:** `(Decisiones con HITL / Decisiones críticas) × 100`
- **Fuentes de Datos:**
  - Tabla: `GOVHITLDECISIONS`
- **Meta:** 100% para decisiones críticas
- **Umbral Crítico:** < 95%
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por tipo de decisión, global

#### **KPI 8.2: Tiempo Promedio de Respuesta HITL**
- **Descripción:** Tiempo promedio de respuesta humana en decisiones HITL (horas)
- **Fórmula:** `AVG(HITLRESPONSETIME)`
- **Fuentes de Datos:**
  - Tabla: `GOVHITLDECISIONS`
- **Meta:** < 4 horas
- **Umbral Crítico:** > 8 horas
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por tipo de supervisión, global

---

### **ODS 12: Producción y Consumo Responsables**

#### **KPI 12.1: Tasa de Reutilización de Modelos**
- **Descripción:** Porcentaje de modelos reutilizados vs. nuevos
- **Fórmula:** `(Modelos reutilizados / Total de modelos) × 100`
- **Fuentes de Datos:**
  - Tabla: `MODMODELS` (modelos con versionado)
  - Tabla: `AIOCOMPONENTS` (componentes reutilizados)
- **Meta:** 50%
- **Umbral Crítico:** < 30%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por tipo de modelo, global

#### **KPI 12.2: Reducción de Consumo Energético**
- **Descripción:** Porcentaje de reducción de consumo energético por optimización
- **Fórmula:** `((Consumo anterior - Consumo actual) / Consumo anterior) × 100`
- **Fuentes de Datos:**
  - Tabla: Telemetría de recursos
  - Tabla: `AIOCOMPONENTS` (componentes con métricas)
- **Meta:** 20% de reducción anual
- **Umbral Crítico:** < 10%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por organización, global

#### **KPI 12.3: Tasa de Uso de Modelos Eficientes**
- **Descripción:** Porcentaje de uso de modelos optimizados para eficiencia
- **Fórmula:** `(Uso modelos eficientes / Total uso modelos) × 100`
- **Fuentes de Datos:**
  - Tabla: Telemetría de uso
  - Tabla: `MODMODELS` (modelos con métricas de eficiencia)
- **Meta:** 60%
- **Umbral Crítico:** < 40%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por tipo de modelo, global

---

### **ODS 3: Salud y Bienestar**

#### **KPI 3.1: Tasa de Detección de Incidentes en Sistemas de Salud**
- **Descripción:** Porcentaje de incidentes detectados en sistemas IA de salud
- **Fórmula:** `(Incidentes detectados / Sistemas de salud monitoreados) × 100`
- **Fuentes de Datos:**
  - Tabla: `GOVINCIDENTS` (incidentes)
  - Tabla: `PRJPROJECTS` (proyectos de salud)
- **Meta:** 100% de detección
- **Umbral Crítico:** < 90%
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por tipo de sistema de salud, global

#### **KPI 3.2: Tiempo Promedio de Detección de Incidentes**
- **Descripción:** Tiempo promedio desde ocurrencia hasta detección (horas)
- **Fórmula:** `AVG(Tiempo de detección)`
- **Fuentes de Datos:**
  - Tabla: `GOVINCIDENTS`
- **Meta:** < 1 hora
- **Umbral Crítico:** > 4 horas
- **Frecuencia de Medición:** Diaria
- **Agregación:** Por severidad, global

---

### **ODS 17: Alianzas para lograr los Objetivos**

#### **KPI 17.1: Tasa de Compartir Agentes en Marketplace**
- **Descripción:** Porcentaje de agentes compartidos en marketplace
- **Fórmula:** `(Agentes compartidos / Total de agentes) × 100`
- **Fuentes de Datos:**
  - Tabla: `AGTAGENTS` (con flag de compartido)
  - Tabla: Marketplace (a definir)
- **Meta:** 30%
- **Umbral Crítico:** < 15%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por organización, global

#### **KPI 17.2: Tasa de Adopción de Recursos Compartidos**
- **Descripción:** Porcentaje de uso de recursos compartidos (agentes, modelos, prompts)
- **Fórmula:** `(Uso recursos compartidos / Total uso recursos) × 100`
- **Fuentes de Datos:**
  - Tabla: Telemetría de uso
  - Tabla: Marketplace
- **Meta:** 40%
- **Umbral Crítico:** < 20%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por tipo de recurso, global

---

### **ODS 4: Educación de Calidad**

#### **KPI 4.1: Tasa de Uso Educativo de LLMs Open Source**
- **Descripción:** Porcentaje de instituciones educativas usando LLMs open source
- **Fórmula:** `(Instituciones educativas usando LLMs OS / Total instituciones) × 100`
- **Fuentes de Datos:**
  - Tabla: `AIOCOMPONENTS` (con tipo de organización)
  - Tabla: Telemetría de uso
- **Meta:** 50%
- **Umbral Crítico:** < 30%
- **Frecuencia de Medición:** Trimestral
- **Agregación:** Por tipo de institución, global

#### **KPI 4.2: Número de Bases de Conocimiento Educativas**
- **Descripción:** Cantidad de bases de conocimiento RAG para educación
- **Fórmula:** `COUNT(Bases de conocimiento educativas)`
- **Fuentes de Datos:**
  - Tabla: RAG pipelines (a definir)
- **Meta:** 100 bases de conocimiento
- **Umbral Crítico:** < 50
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por nivel educativo, global

---

### **ODS 7: Energía Asequible y No Contaminante**

#### **KPI 7.1: Reducción de Consumo Energético por Optimización**
- **Descripción:** Porcentaje de reducción de consumo energético
- **Fórmula:** `((Consumo anterior - Consumo actual) / Consumo anterior) × 100`
- **Fuentes de Datos:**
  - Tabla: Telemetría de recursos
  - Tabla: Métricas de infraestructura
- **Meta:** 25% de reducción anual
- **Umbral Crítico:** < 10%
- **Frecuencia de Medición:** Mensual
- **Agregación:** Por organización, global

---

## 📈 KPIs AGREGADOS Y COMPUESTOS

### **KPI Compuesto: Score General de Impacto ODS**
- **Descripción:** Score agregado del impacto total en ODS (0.00 - 1.00)
- **Fórmula:** `Ponderación de KPIs por ODS según importancia`
- **Componentes:**
  - ODS 16: 30% del peso total
  - ODS 9: 25% del peso total
  - ODS 10: 20% del peso total
  - ODS 5: 10% del peso total
  - ODS 8, 12, 3, 17, 4, 7: 15% restante
- **Meta:** 0.85
- **Frecuencia de Medición:** Semanal

### **KPI Compuesto: Tasa de Cumplimiento Normativo**
- **Descripción:** Porcentaje de cumplimiento de todos los requisitos normativos
- **Fórmula:** `Promedio ponderado de tasas de cumplimiento por módulo`
- **Componentes:**
  - Trazabilidad: 15%
  - QMS: 20%
  - Clasificación: 10%
  - HITL: 10%
  - FRIA: 15%
  - Conformidad: 15%
  - Registro UE: 10%
  - Sistemas Prohibidos: 5%
- **Meta:** 90%
- **Frecuencia de Medición:** Semanal

---

## 🎨 DISEÑO DE DASHBOARDS

### **Dashboard 1: Vista General de Impacto ODS**

**Sección 1: Score General de Impacto**
- KPI Compuesto: Score General de Impacto ODS (gauge circular)
- Tasa de Cumplimiento Normativo (gauge circular)
- Tendencias de últimos 6 meses (gráfico de líneas)

**Sección 2: Impacto por ODS**
- 10 tarjetas (una por ODS) con:
  - Nombre del ODS
  - Score actual (0-100)
  - Tendencia (↑↓)
  - Número de módulos contribuyendo
  - Estado (Verde/Amarillo/Rojo según umbral)

**Sección 3: Top 5 KPIs Críticos**
- Lista de KPIs más críticos con:
  - Nombre del KPI
  - Valor actual
  - Meta
  - % de cumplimiento
  - Estado

**Sección 4: Evolución Temporal**
- Gráfico de líneas con evolución de KPIs principales
- Período: Últimos 12 meses
- Filtros: Por ODS, por organización

---

### **Dashboard 2: ODS 16 - Instituciones Sólidas**

**Sección 1: Métricas Principales**
- KPI 16.1: Tasa de Trazabilidad Completa (gauge)
- KPI 16.2: Tasa de Certificación (gauge)
- KPI 16.3: Score Promedio de Compliance (gauge)
- KPI 16.4: Tasa de Registro en BD UE (gauge)

**Sección 2: Trazabilidad**
- Gráfico de barras: Sistemas con/sin trazabilidad por organización
- Tabla: Top 10 sistemas sin trazabilidad completa
- Gráfico de líneas: Evolución de tasa de trazabilidad

**Sección 3: Certificación**
- Gráfico de barras: Sistemas certificados vs. pendientes
- Tabla: Sistemas en proceso de certificación con tiempo estimado
- Gráfico de líneas: Tiempo promedio de certificación

**Sección 4: Compliance**
- Gráfico de barras: Score QMS por organización
- Heatmap: Score por módulo QMS (13 módulos)
- Tabla: Gaps detectados por módulo

**Sección 5: HITL**
- Gráfico de barras: Decisiones con/sin HITL
- Gráfico de líneas: Tiempo promedio de respuesta HITL
- Tabla: Decisiones pendientes de supervisión humana

---

### **Dashboard 3: ODS 9 - Innovación**

**Sección 1: Métricas Principales**
- KPI 9.1: Score QMS Promedio (gauge)
- KPI 9.2: Tasa de Documentación Completa (gauge)
- KPI 9.3: Tiempo Promedio de Certificación (gauge)
- KPI 9.4: Tasa de Reutilización de Agentes (gauge)

**Sección 2: Reutilización**
- Gráfico de barras: Tasa de reutilización por tipo de activo (Agentes, Modelos, Prompts)
- Gráfico de líneas: Evolución de reutilización
- Tabla: Top 10 activos más reutilizados

**Sección 3: MLOps**
- Gráfico de líneas: Tiempo promedio de ciclo MLOps
- Gráfico de barras: Modelos por estado (entrenamiento, testing, producción)
- Tabla: Modelos con drift detectado

**Sección 4: LLMs Open Source**
- Gráfico de barras: Uso LLMs OS vs. Propietarios
- Gráfico de líneas: Evolución de adopción
- Tabla: Top 10 LLMs open source más usados

**Sección 5: Documentación**
- Gráfico de barras: Score de documentación por proyecto
- Heatmap: Completitud por sección Anexo IV (11 secciones)
- Tabla: Proyectos con documentación incompleta

---

### **Dashboard 4: ODS 10 - Reducción de Desigualdades**

**Sección 1: Métricas Principales**
- KPI 10.1: Tasa de FRIA Completada (gauge)
- KPI 10.2: Riesgo Promedio en FRIA (gauge)
- KPI 10.3: Tasa de Detección de Sesgos (gauge)
- KPI 10.4: Score de Representatividad (gauge)

**Sección 2: FRIA**
- Gráfico de barras: FRIAs completadas vs. pendientes
- Gráfico de líneas: Riesgo promedio en FRIA
- Tabla: Sistemas de alto riesgo sin FRIA

**Sección 3: Sesgos en Datasets**
- Gráfico de barras: Datasets con/sin análisis de sesgos
- Gráfico de líneas: Score de representatividad
- Tabla: Datasets con sesgos detectados

**Sección 4: Sistemas Prohibidos**
- Gráfico de barras: Sistemas prohibidos detectados por categoría
- Tabla: Sistemas bloqueados
- Alerta: Sistemas prohibidos activos (debe ser 0)

**Sección 5: Prompts**
- Gráfico de barras: Prompts validados vs. pendientes
- Tabla: Prompts con sesgos detectados
- Gráfico de líneas: Evolución de validación

---

### **Dashboard 5: ODS 5 - Igualdad de Género**

**Sección 1: Métricas Principales**
- KPI 5.1: Tasa de Datasets Balanceados (gauge)
- KPI 5.2: Score de Representación de Género (gauge)
- KPI 5.3: Tasa de Evaluación de Sesgos (gauge)

**Sección 2: Balance de Género en Datasets**
- Gráfico de barras: Distribución de género en datasets
- Gráfico de líneas: Score de representación de género
- Tabla: Datasets desbalanceados

**Sección 3: Evaluación de Sesgos**
- Gráfico de barras: Sistemas evaluados vs. pendientes
- Tabla: Sistemas con sesgos de género detectados
- Gráfico de líneas: Medidas de mitigación implementadas

---

### **Dashboard 6: Comparativo Multi-ODS**

**Sección 1: Matriz de Impacto**
- Heatmap: Impacto por ODS y por módulo
- Colores: Verde (alto impacto), Amarillo (medio), Rojo (bajo)

**Sección 2: Evolución Comparativa**
- Gráfico de líneas múltiples: Evolución de KPIs principales por ODS
- Período: Últimos 12 meses

**Sección 3: Ranking de Organizaciones**
- Tabla: Top 10 organizaciones por impacto ODS
- Columnas: Organización, Score General, ODS principales, Tendencias

**Sección 4: Gaps y Oportunidades**
- Lista: ODS con menor impacto
- Recomendaciones: Acciones para mejorar impacto

---

## 📊 FUENTES DE DATOS Y AGREGACIONES

### **Tablas Principales:**

1. **Compliance:**
   - `GOVQUALITYMANAGEMENTSYSTEMS` (QMS)
   - `COMCOMPLIANCEASSESSMENTS` (Conformidad)
   - `FRIAFUNDAMENTALRIGHTSASSESSMENTS` (FRIA)
   - `REGEUREGISTRATIONS` (Registro UE)
   - `GOVPROHIBITEDSYSTEMS` (Sistemas Prohibidos)
   - `GOVHITLSUPERVISIONS` y `GOVHITLDECISIONS` (HITL)
   - `IMLIMMUTABLELOGS` (Trazabilidad)
   - `GOVAIAACTTECHNICALDOCS` (Documentación)

2. **Gobernanza:**
   - `PRJPROJECTS` (Proyectos)
   - `MODMODELS` (Modelos)
   - `AGTAGENTS` (Agentes)
   - `PRMPROMPTS` (Prompts)
   - `DSDDATASETS` (Datasets)
   - `AIOCOMPONENTS` (Componentes AI OS)

3. **Telemetría:**
   - Tablas de telemetría y analytics
   - Métricas de uso y rendimiento

### **Agregaciones Necesarias:**

- **Por Organización:** Agrupar métricas por organización
- **Por Proyecto:** Agrupar métricas por proyecto
- **Por Tipo de Activo:** Agrupar por tipo (Modelo, Agente, Prompt, etc.)
- **Temporal:** Agregaciones diarias, semanales, mensuales, anuales
- **Comparativas:** Comparar períodos (mes anterior, año anterior)

---

## 🎯 METAS Y UMBRALES

### **Sistema de Alertas:**

- **Verde:** KPI >= Meta
- **Amarillo:** KPI >= Umbral Crítico pero < Meta
- **Rojo:** KPI < Umbral Crítico

### **Notificaciones:**

- **Críticas:** Cuando KPI < Umbral Crítico
- **Advertencias:** Cuando KPI < Meta pero >= Umbral Crítico
- **Informativas:** Tendencias negativas detectadas

---

## 📅 FRECUENCIAS DE ACTUALIZACIÓN

- **Tiempo Real:** KPIs críticos (Trazabilidad, HITL, Incidentes)
- **Diaria:** KPIs principales de compliance
- **Semanal:** KPIs de gobernanza y reutilización
- **Mensual:** KPIs de impacto social y educativo
- **Trimestral:** KPIs de colaboración y alianzas

---

## 🔄 PROCESO DE IMPLEMENTACIÓN

### **Fase 1: KPIs Básicos (Sprint 1-2)**
- Implementar KPIs de ODS 16 (Instituciones Sólidas)
- Dashboard básico de impacto general
- Alertas críticas

### **Fase 2: KPIs de Gobernanza (Sprint 3-4)**
- Implementar KPIs de ODS 9 (Innovación)
- Dashboards de reutilización y MLOps
- Métricas de LLMs open source

### **Fase 3: KPIs Sociales (Sprint 5-6)**
- Implementar KPIs de ODS 10, 5 (Desigualdades, Género)
- Dashboards de FRIA y sesgos
- Métricas de datasets

### **Fase 4: KPIs Complementarios (Sprint 7-8)**
- Implementar KPIs de ODS 8, 12, 3, 17, 4, 7
- Dashboard comparativo multi-ODS
- Reportes ejecutivos

---

## 📝 NOTAS DE IMPLEMENTACIÓN

1. **Priorización:** Empezar con KPIs de ODS 16 y 9 (mayor impacto)
2. **Validación:** Validar fórmulas con stakeholders antes de implementar
3. **Performance:** Optimizar queries para dashboards en tiempo real
4. **Caché:** Implementar caché para agregaciones pesadas
5. **Exportación:** Permitir exportar KPIs a Excel/PDF para reportes

---

**Última actualización:** Diciembre 2025
**Estado:** ✅ Documento completo - Listo para implementación
