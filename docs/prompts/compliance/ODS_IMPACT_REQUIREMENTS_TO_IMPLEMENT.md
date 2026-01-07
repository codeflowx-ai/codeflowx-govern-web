# 📋 REQUISITOS PARA IMPLEMENTAR KPIs ODS RESTANTES

**Fecha:** Diciembre 2025
**Propósito:** Definir qué tablas, campos y funcionalidades son necesarios para habilitar los 12 KPIs no factibles actualmente

---

## 📊 RESUMEN

### **KPIs Pendientes:** 12 KPIs (35% del total)
### **KPIs Parciales:** 2 KPIs (6% del total)

**Total a habilitar:** 14 KPIs

---

## 🎯 REQUISITOS POR ODS

### **ODS 5 - Igualdad de Género (2 KPIs pendientes)**

#### ❌ KPI 5.1: Tasa de Datasets Balanceados por Género
#### ❌ KPI 5.2: Score de Representación de Género

**Requisitos:**

1. **Crear tabla `DSDDATASETS` (Datasets de Gobernanza)**
   ```sql
   CREATE TABLE DSDDATASETS (
       IDXDATASET BIGSERIAL PRIMARY KEY,
       IDUUID VARCHAR(36) UNIQUE NOT NULL,
       IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT),
       DSDNAME VARCHAR(255) NOT NULL,
       DSDDESCRIPTION TEXT,
       DSDTYPE VARCHAR(50), -- TRAINING, VALIDATION, TEST

       -- Métricas de género
       DSDGENDERDISTRIBUTION JSONB, -- {"male": 45, "female": 48, "non_binary": 4, "other": 3}
       DSDGENDERBALANCESCORE DECIMAL(5,2), -- 0.00 - 1.00
       DSDGENDERBALANCED BOOLEAN, -- true si balance >= 0.40 para cada género

       -- Análisis de sesgos
       DSDBIASANALYSIS JSONB, -- {"genderBias": {"detected": true, "severity": "MEDIUM"}}
       DSDBIASANALYZED BOOLEAN DEFAULT false,
       DSDBIASANALYZEDAT TIMESTAMP,

       -- Representatividad
       DSDREPRESENTATIVITYSCORE DECIMAL(5,2), -- 0.00 - 1.00
       DSDREPRESENTATIVITYMETRICS JSONB, -- {"age": 0.85, "ethnicity": 0.78, "socioeconomic": 0.82}

       -- Auditoría
       DSDCREATEDAT TIMESTAMP NOT NULL,
       DSDUPDATEDAT TIMESTAMP,
       DSDCREATEDBY BIGINT
   );
   ```

2. **Integración con módulo de evaluación**
   - Conectar con `ModelBiasAnalysis` o crear servicio específico
   - Análisis automático de distribución de género en datasets
   - Cálculo de score de balance (0.00 - 1.00)

3. **Proceso de análisis**
   - Trigger o job que analice datasets al subirlos
   - Integración con herramientas de análisis de sesgos (Fairness metrics)

**Prioridad:** 🔴 ALTA (impacta ODS 5 y ODS 10)

---

### **ODS 10 - Reducción de Desigualdades (2 KPIs pendientes)**

#### ❌ KPI 10.3: Tasa de Detección de Sesgos en Datasets
#### ❌ KPI 10.4: Score de Representatividad de Datasets

**Requisitos:**

1. **Misma tabla `DSDDATASETS`** (ver ODS 5)
   - Campos ya definidos: `DSDBIASANALYSIS`, `DSDBIASANALYZED`, `DSDREPRESENTATIVITYSCORE`

2. **Servicio de análisis de sesgos**
   ```java
   public interface DatasetBiasAnalysisService {
       BiasAnalysisResult analyzeBias(Long datasetId);
       RepresentativityScore calculateRepresentativity(Long datasetId);
       void detectBiasTypes(Long datasetId); // género, edad, etnia, etc.
   }
   ```

3. **Integración con evaluación**
   - Usar `BiasDetection`, `BiasAnalysis` existentes
   - Extender para datasets (no solo modelos)

**Prioridad:** 🔴 ALTA (mismo que ODS 5)

---

### **ODS 9 - Innovación (1 KPI parcial)**

#### ⚠️ KPI 9.4: Tasa de Reutilización de Agentes

**Requisitos:**

1. **Opción A: Tabla intermedia `PRJAGENTS` (Recomendado)**
   ```sql
   CREATE TABLE PRJAGENTS (
       IDXPRJAGENT BIGSERIAL PRIMARY KEY,
       IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT) NOT NULL,
       IDXAGENT BIGINT REFERENCES AGTAGENTS(IDXAGENT) NOT NULL,
       PRJAGENTUSAGETYPE VARCHAR(50), -- PRIMARY, SECONDARY, REFERENCE
       PRJAGENTCREATEDAT TIMESTAMP NOT NULL,
       UNIQUE(IDXPROJECT, IDXAGENT)
   );
   ```

2. **Opción B: Campo en `PRJPROJECTS.METADATA`**
   - Añadir `agents: [{"agentId": 1, "usageType": "PRIMARY"}]` en JSONB
   - Menos normalizado pero más rápido de implementar

3. **Lógica de negocio**
   - Al asignar agente a proyecto, crear registro en `PRJAGENTS`
   - Query: contar agentes usados en >1 proyecto

**Prioridad:** 🟡 MEDIA

---

### **ODS 12 - Consumo Responsable (2 KPIs pendientes)**

#### ❌ KPI 12.2: Reducción de Consumo Energético
#### ❌ KPI 12.3: Tasa de Uso de Modelos Eficientes

**Requisitos:**

1. **Crear tabla `TELRESOURCETELEMETRY` (Telemetría de Recursos)**
   ```sql
   CREATE TABLE TELRESOURCETELEMETRY (
       IDXRESOURCETELEMETRY BIGSERIAL PRIMARY KEY,
       IDUUID VARCHAR(36) UNIQUE NOT NULL,
       IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT),
       IDXMODEL BIGINT REFERENCES MODMODELS(IDXMODEL),
       IDXAGENT BIGINT REFERENCES AGTAGENTS(IDXAGENT),

       -- Recursos consumidos
       TELCPUCORES INTEGER,
       TELGPUS INTEGER,
       TELMEMORYGB DECIMAL(10,2),
       TELSTORAGEGB DECIMAL(10,2),

       -- Energía
       TELENERGYCONSUMPTIONKWH DECIMAL(10,4), -- kWh consumidos
       TELENERGYCOSTUSD DECIMAL(10,2), -- Coste en USD
       TELCARBONFOOTPRINTKG DECIMAL(10,4), -- CO2 en kg

       -- Período
       TELSTARTTIME TIMESTAMP NOT NULL,
       TELENDTIME TIMESTAMP,
       TELDURATIONHOURS DECIMAL(10,2),

       -- Tipo de operación
       TELOPERATIONTYPE VARCHAR(50), -- TRAINING, INFERENCE, SERVING

       -- Optimización
       TELOPTIMIZED BOOLEAN DEFAULT false,
       TELOPTIMIZATIONTYPE VARCHAR(50), -- QUANTIZATION, PRUNING, DISTILLATION

       -- Auditoría
       TELCREATEDAT TIMESTAMP NOT NULL
   );
   ```

2. **Integración con infraestructura**
   - Conectar con `KubernetesCluster`, `CloudProvider`
   - Recopilar métricas de recursos en tiempo real
   - Calcular consumo energético basado en tipo de hardware

3. **Cálculo de reducción**
   - Comparar consumo antes/después de optimizaciones
   - Baseline: consumo promedio histórico
   - Reducción = (baseline - actual) / baseline * 100

4. **Campo en `MODMODELS`**
   ```sql
   ALTER TABLE MODMODELS ADD COLUMN MODOPTIMIZED BOOLEAN DEFAULT false;
   ALTER TABLE MODMODELS ADD COLUMN MODOPTIMIZATIONTYPE VARCHAR(50);
   ALTER TABLE MODMODELS ADD COLUMN MODEFFICIENCYSCORE DECIMAL(5,2); -- 0.00 - 1.00
   ```

**Prioridad:** 🟡 MEDIA (requiere infraestructura de telemetría)

---

### **ODS 17 - Alianzas (2 KPIs pendientes)**

#### ❌ KPI 17.1: Tasa de Compartir Agentes en Marketplace
#### ❌ KPI 17.2: Tasa de Adopción de Recursos Compartidos

**Requisitos:**

1. **Tabla `AGTMARKETPLACE` (Marketplace de Agentes)**
   ```sql
   CREATE TABLE AGTMARKETPLACE (
       IDXMARKETPLACEENTRY BIGSERIAL PRIMARY KEY,
       IDUUID VARCHAR(36) UNIQUE NOT NULL,
       IDXAGENT BIGINT REFERENCES AGTAGENTS(IDXAGENT) NOT NULL,

       -- Sharing
       MKTISHARED BOOLEAN DEFAULT false,
       MKTSHAREDAT TIMESTAMP,
       MKTSHAREDBY BIGINT,
       MKTVISIBILITY VARCHAR(20), -- PUBLIC, PRIVATE, ORGANIZATION

       -- Marketplace metadata
       MKTRATING DECIMAL(3,2), -- 0.00 - 5.00
       MKTUSECOUNT INTEGER DEFAULT 0,
       MKTORGANIZATIONCOUNT INTEGER DEFAULT 0,

       -- Auditoría
       MKTCREATEDAT TIMESTAMP NOT NULL,
       MKTUPDATEDAT TIMESTAMP
   );
   ```

2. **Tabla `AGTMARKETPLACEUSAGE` (Uso de Agentes Compartidos)**
   ```sql
   CREATE TABLE AGTMARKETPLACEUSAGE (
       IDXMARKETPLACEUSAGE BIGSERIAL PRIMARY KEY,
       IDXMARKETPLACEENTRY BIGINT REFERENCES AGTMARKETPLACE(IDXMARKETPLACEENTRY),
       IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT),
       IDXORGANIZATION BIGINT, -- Organización que usa el agente compartido
       USAGECREATEDAT TIMESTAMP NOT NULL
   );
   ```

3. **Funcionalidad de marketplace**
   - UI para compartir agentes
   - Catálogo de agentes compartidos
   - Sistema de ratings y reviews
   - Tracking de uso por organización

4. **Telemetría de recursos compartidos**
   - Extender `TELRESOURCETELEMETRY` con campo `TELISSHAREDRESOURCE`
   - Tracking de uso de recursos compartidos vs. propios

**Prioridad:** 🟢 BAJA (requiere funcionalidad completa de marketplace)

---

### **ODS 4 - Educación (2 KPIs pendientes)**

#### ❌ KPI 4.1: Tasa de Uso Educativo de LLMs Open Source
#### ❌ KPI 4.2: Número de Bases de Conocimiento Educativas

**Requisitos:**

1. **Campo en `PRJPROJECTS` o tabla `ORGANIZATIONS`**
   ```sql
   -- Opción A: Campo en Project
   ALTER TABLE PRJPROJECTS ADD COLUMN PRJORGANIZATIONTYPE VARCHAR(50);
   -- Valores: EDUCATIONAL, HEALTHCARE, FINANCE, GOVERNMENT, PRIVATE, etc.

   -- Opción B: Tabla Organizations (mejor)
   CREATE TABLE ORGORGANIZATIONS (
       IDXORGANIZATION BIGSERIAL PRIMARY KEY,
       ORGNAME VARCHAR(255) NOT NULL,
       ORGTYPE VARCHAR(50) NOT NULL, -- EDUCATIONAL, HEALTHCARE, etc.
       ORGSUBTYPE VARCHAR(50), -- UNIVERSITY, SCHOOL, HOSPITAL, etc.
       ORGCREATEDAT TIMESTAMP NOT NULL
   );

   -- Relación Project-Organization
   ALTER TABLE PRJPROJECTS ADD COLUMN IDXORGANIZATION BIGINT REFERENCES ORGORGANIZATIONS(IDXORGANIZATION);
   ```

2. **Categorización de LLMs Open Source**
   - Ya existe `AIOCOMPONENTS` con relación a Project
   - Filtrar por `AIOCOMPONENTTYPE = 'LLM'` y `isOpenSource = true`
   - Agrupar por `PRJPROJECTS.PRJORGANIZATIONTYPE = 'EDUCATIONAL'`

3. **Tabla `RAGPIPELINES` con categorización**
   ```sql
   CREATE TABLE RAGPIPELINES (
       IDXRAGPIPELINE BIGSERIAL PRIMARY KEY,
       IDUUID VARCHAR(36) UNIQUE NOT NULL,
       IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT),

       -- Categorización
       RAGCATEGORY VARCHAR(50), -- EDUCATION, HEALTHCARE, LEGAL, etc.
       RAGEDUCATIONALUSE BOOLEAN DEFAULT false,
       RAGEDUCATIONALLEVEL VARCHAR(50), -- PRIMARY, SECONDARY, HIGHER

       -- Metadata
       RAGNAME VARCHAR(255) NOT NULL,
       RAGDESCRIPTION TEXT,
       RAGSTATUS VARCHAR(50),

       -- Auditoría
       RAGCREATEDAT TIMESTAMP NOT NULL
   );
   ```

**Prioridad:** 🟡 MEDIA

---

### **ODS 7 - Energía (1 KPI pendiente)**

#### ❌ KPI 7.1: Reducción de Consumo Energético

**Requisitos:**

1. **Misma tabla `TELRESOURCETELEMETRY`** (ver ODS 12.2)
   - Campos de energía ya definidos

2. **Cálculo de reducción**
   - Baseline histórico
   - Comparación antes/después optimizaciones
   - Porcentaje de reducción

**Prioridad:** 🟡 MEDIA (mismo que ODS 12.2)

---

### **ODS 16 - Instituciones Sólidas (1 KPI parcial)**

#### ⚠️ KPI 16.6: Tiempo Promedio de Auditoría

**Requisitos:**

1. **Asegurar valores en `IMLIMMUTABLELOGS.IMLACTION`**
   - `AUDIT_START` - Inicio de auditoría
   - `AUDIT_COMPLETE` - Fin de auditoría
   - Si no existen, usar otros valores relacionados

2. **Alternativa: Tabla `GOVAUDITS`**
   ```sql
   CREATE TABLE GOVAUDITS (
       IDXAUDIT BIGSERIAL PRIMARY KEY,
       IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT),
       AUDITSTARTDATE TIMESTAMP NOT NULL,
       AUDITENDDATE TIMESTAMP,
       AUDITSTATUS VARCHAR(50), -- IN_PROGRESS, COMPLETED, CANCELLED
       AUDITDURATIONHOURS DECIMAL(10,2),
       AUDITCREATEDAT TIMESTAMP NOT NULL
   );
   ```

**Prioridad:** 🟢 BAJA (puede calcularse con logs existentes si se usan acciones correctas)

---

## 📋 PLAN DE IMPLEMENTACIÓN PRIORIZADO

### **Fase 1: Alta Prioridad (Habilita 4 KPIs)**

1. **Crear tabla `DSDDATASETS`**
   - Habilita: ODS 5 (2 KPIs), ODS 10 (2 KPIs)
   - Esfuerzo: Medio
   - Impacto: Alto

2. **Servicio de análisis de sesgos**
   - Integración con módulo de evaluación
   - Esfuerzo: Alto
   - Impacto: Alto

**Resultado:** +4 KPIs factibles (de 20 a 24)

---

### **Fase 2: Media Prioridad (Habilita 5 KPIs)**

3. **Tabla `TELRESOURCETELEMETRY`**
   - Habilita: ODS 12 (2 KPIs), ODS 7 (1 KPI)
   - Esfuerzo: Alto (requiere integración infraestructura)
   - Impacto: Medio

4. **Tabla `PRJAGENTS` o campo en metadata**
   - Habilita: ODS 9 (1 KPI parcial)
   - Esfuerzo: Bajo
   - Impacto: Medio

5. **Tabla `ORGORGANIZATIONS` y `RAGPIPELINES`**
   - Habilita: ODS 4 (2 KPIs)
   - Esfuerzo: Medio
   - Impacto: Medio

**Resultado:** +5 KPIs factibles (de 24 a 29)

---

### **Fase 3: Baja Prioridad (Habilita 2 KPIs)**

6. **Funcionalidad Marketplace**
   - Habilita: ODS 17 (2 KPIs)
   - Esfuerzo: Muy Alto (requiere UI completa)
   - Impacto: Bajo

7. **Mejora KPI 16.6**
   - Habilita: ODS 16 (1 KPI parcial)
   - Esfuerzo: Bajo
   - Impacto: Bajo

**Resultado:** +2 KPIs factibles (de 29 a 31)

---

## 📊 RESUMEN DE REQUISITOS

### **Tablas a Crear:**

1. ✅ `DSDDATASETS` - Datasets de gobernanza (Fase 1)
2. ✅ `TELRESOURCETELEMETRY` - Telemetría de recursos (Fase 2)
3. ✅ `PRJAGENTS` - Relación Project-Agent (Fase 2)
4. ✅ `ORGORGANIZATIONS` - Organizaciones (Fase 2)
5. ✅ `RAGPIPELINES` - Pipelines RAG (Fase 2)
6. ✅ `AGTMARKETPLACE` - Marketplace de agentes (Fase 3)
7. ✅ `AGTMARKETPLACEUSAGE` - Uso de marketplace (Fase 3)
8. ✅ `GOVAUDITS` - Auditorías (Fase 3, opcional)

### **Campos a Añadir:**

1. `MODMODELS.MODOPTIMIZED` - Modelo optimizado
2. `MODMODELS.MODOPTIMIZATIONTYPE` - Tipo de optimización
3. `MODMODELS.MODEFFICIENCYSCORE` - Score de eficiencia
4. `PRJPROJECTS.PRJORGANIZATIONTYPE` - Tipo de organización (alternativa a tabla)

### **Servicios a Crear:**

1. `DatasetBiasAnalysisService` - Análisis de sesgos en datasets
2. `ResourceTelemetryService` - Recopilación de telemetría de recursos
3. `MarketplaceService` - Gestión de marketplace de agentes
4. `OrganizationService` - Gestión de organizaciones

---

## 🎯 RESULTADO FINAL ESPERADO

**Después de implementar todas las fases:**
- ✅ **31 KPIs factibles (91%)**
- ⚠️ **2 KPIs parciales (6%)**
- ❌ **1 KPI no factible (3%)**

**El único KPI que quedaría sin implementar sería:**
- KPI 17.2 (si no se implementa telemetría de recursos compartidos)

---

## ⚠️ NOTAS IMPORTANTES

1. **Telemetría de recursos** requiere integración con infraestructura (Kubernetes, Cloud providers)
2. **Marketplace** requiere funcionalidad completa de UI y gestión
3. **Análisis de sesgos** puede reutilizar módulo de evaluación existente
4. **Organizaciones** puede implementarse como campo en Project si no se necesita gestión completa

---

## 📞 PRÓXIMOS PASOS

1. **Revisar y aprobar** requisitos de Fase 1
2. **Crear scripts SQL** para `DSDDATASETS`
3. **Implementar servicio** de análisis de sesgos
4. **Probar queries** de los 4 nuevos KPIs
5. **Iterar** con Fase 2 y Fase 3 según prioridades
