# ✅ ANÁLISIS DE FACTIBILIDAD: KPIs ODS CON DATOS REALES

**Fecha:** Diciembre 2025
**Propósito:** Analizar qué KPIs ODS pueden calcularse REALMENTE con las tablas y datos disponibles

---

## 📊 RESUMEN EJECUTIVO

### ✅ **KPIs TOTALMENTE FACTIBLES (con datos reales):** 15 KPIs
### ⚠️ **KPIs PARCIALMENTE FACTIBLES (requieren ajustes):** 2 KPIs
### ❌ **KPIs NO FACTIBLES (faltan tablas/datos):** 17 KPIs

---

## ✅ ODS 16 - Instituciones Sólidas (6 KPIs)

### ✅ KPI 16.1: Tasa de Trazabilidad Completa
- **Factible:** ✅ SÍ
- **Tablas:** `IMLIMMUTABLELOGS`, `PRJPROJECTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 17-37
- **Nota:** Requiere verificar que `IMLACTION` tenga valores como 'MODEL_DEPLOYMENT', 'AGENT_EXECUTION', 'PROMPT_CHANGE'

### ✅ KPI 16.2: Tasa de Certificación de Sistemas
- **Factible:** ✅ SÍ
- **Tablas:** `COMCOMPLIANCEASSESSMENTS`, `PRJPROJECTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 39-49
- **Nota:** Requiere campo `READYFORCERTIFICATION` en `COMCOMPLIANCEASSESSMENTS`

### ✅ KPI 16.3: Score Promedio de Compliance
- **Factible:** ✅ SÍ
- **Tablas:** `GOVQUALITYMANAGEMENTSYSTEMS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 51-58
- **Nota:** Campo `QMSOVERALLSCORE` existe y es calculado

### ✅ KPI 16.4: Tasa de Registro en BD UE
- **Factible:** ✅ SÍ
- **Tablas:** `REGEUREGISTRATIONS`, `PRJPROJECTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 60-71
- **Nota:** Requiere campo `REGSTATUS` con valor 'REGISTERED'

### ✅ KPI 16.5: Tasa de Aprobación Humana (HITL)
- **Factible:** ✅ SÍ
- **Tablas:** `GOVHITLDECISIONS`, `GOVHITLSUPERVISIONS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 73-82
- **Nota:** Relación FK entre tablas existe

### ⚠️ KPI 16.6: Tiempo Promedio de Auditoría
- **Factible:** ⚠️ PARCIAL
- **Tablas:** `IMLIMMUTABLELOGS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 84-94
- **Nota:** Requiere que `IMLACTION` tenga valores 'AUDIT_START' y 'AUDIT_COMPLETE'. Si no existen, usar timestamps de logs relacionados.

---

## ✅ ODS 9 - Innovación (7 KPIs) - **4 FACTIBLES**

### ✅ KPI 9.1: Score QMS Promedio
- **Factible:** ✅ SÍ (igual que KPI 16.3)
- **Tablas:** `GOVQUALITYMANAGEMENTSYSTEMS`

### ✅ KPI 9.2: Tasa de Documentación Completa
- **Factible:** ✅ SÍ
- **Tablas:** `GOVAIAACTTECHNICALDOCS`, `PRJPROJECTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 106-115
- **Nota:** Requiere parsear JSONB `TECHSECTIONS` y contar secciones (11 total)

### ✅ KPI 9.3: Tiempo Promedio de Certificación
- **Factible:** ✅ SÍ
- **Tablas:** `COMCOMPLIANCEASSESSMENTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 117-124
- **Nota:** Requiere campos `CREATEDAT` y `UPDATEDAT` con `READYFORCERTIFICATION = true`

### ❌ KPI 9.4: Tasa de Reutilización de Agentes
- **Factible:** ❌ NO
- **Razón:** No hay relación confirmada entre `AGTAGENTS` y `PRJPROJECTS`
- **Solución:** Necesita tabla intermedia o campo en `PRJPROJECTS.METADATA` con `agentId`
- **Query propuesta:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 126-140 (comentada)

### ✅ KPI 9.5: Tasa de Reutilización de Prompts
- **Factible:** ✅ SÍ
- **Tablas:** `PRMPROMPTS`, `PRJPROJECTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 142-146 (actualizada)
- **Nota:** Tabla `PRMPROMPTS` existe con relación `IDXPROJECT`. Calcular reutilización contando prompts usados en múltiples proyectos.

### ❌ KPI 9.6: Tiempo Promedio de Ciclo MLOps
- **Factible:** ❌ NO
- **Razón:** No hay tablas de training y deployment
- **Solución:** Necesita tablas de entrenamiento y despliegue de modelos

### ❌ KPI 9.7: Tasa de Adopción de LLMs Open Source
- **Factible:** ❌ NO
- **Razón:** No hay telemetría específica de componentes AI OS
- **Solución:** Necesita tabla `AIOCOMPONENTS` con tracking de uso

---

## ✅ ODS 10 - Reducción de Desigualdades (6 KPIs) - **4 FACTIBLES**

### ✅ KPI 10.1: Tasa de FRIA Completada
- **Factible:** ✅ SÍ
- **Tablas:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS`, `PRJPROJECTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 164-173
- **Nota:** Requiere campo `FRIASTATUS` con valor 'COMPLETED'

### ✅ KPI 10.2: Riesgo Promedio en FRIA
- **Factible:** ✅ SÍ
- **Tablas:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 175-182
- **Nota:** Requiere parsear JSONB `FRARISKS` y extraer `overallRisk`

### ❌ KPI 10.3: Tasa de Detección de Sesgos en Datasets
- **Factible:** ❌ NO
- **Razón:** No existe tabla `DSDDATASETS` confirmada
- **Solución:** Crear tabla `DSDDATASETS` con campo de análisis de sesgos

### ❌ KPI 10.4: Score de Representatividad de Datasets
- **Factible:** ❌ NO
- **Razón:** No existe tabla `DSDDATASETS` confirmada
- **Solución:** Crear tabla `DSDDATASETS` con métricas de representatividad

### ✅ KPI 10.5: Tasa de Sistemas Prohibidos Detectados
- **Factible:** ✅ SÍ
- **Tablas:** `GOVPROHIBITEDSYSTEMS`, `PRJPROJECTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 196-206
- **Nota:** Requiere parsear `PRJPROJECTS.METADATA` para buscar `prohibitedSystemCode`

### ✅ KPI 10.6: Tasa de Prompts Validados por Sesgos
- **Factible:** ✅ SÍ
- **Tablas:** `PRMPROMPTS`, `PRMPROMPTVALIDATIONS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 208-212 (actualizada)
- **Nota:** Tabla `PRMPROMPTS` existe. Validaciones de sesgos están en `PRMPROMPTVALIDATIONS` con `PRMVALIDATIONTYPE = 'BIAS'`.

---

## ⚠️ ODS 5 - Igualdad de Género (3 KPIs)

### ❌ KPI 5.1: Tasa de Datasets Balanceados por Género
- **Factible:** ❌ NO
- **Razón:** No existe tabla `DSDDATASETS` confirmada
- **Solución:** Crear tabla `DSDDATASETS` con métricas de género

### ❌ KPI 5.2: Score de Representación de Género
- **Factible:** ❌ NO
- **Razón:** No existe tabla `DSDDATASETS` confirmada
- **Solución:** Crear tabla `DSDDATASETS` con score de género

### ⚠️ KPI 5.3: Tasa de Sistemas con Evaluación de Sesgos de Género
- **Factible:** ⚠️ PARCIAL
- **Tablas:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 230-237
- **Nota:** Requiere que JSONB `FRARISKS` contenga campo `genderBiasAssessed`. Si no existe, no se puede calcular.

---

## ✅ ODS 8 - Trabajo Decente (2 KPIs)

### ✅ KPI 8.1: Tasa de Supervisión Humana (HITL)
- **Factible:** ✅ SÍ (igual que KPI 16.5)
- **Tablas:** `GOVHITLDECISIONS`, `GOVHITLSUPERVISIONS`

### ✅ KPI 8.2: Tiempo Promedio de Respuesta HITL
- **Factible:** ✅ SÍ
- **Tablas:** `GOVHITLDECISIONS`, `GOVHITLSUPERVISIONS`
- **Query:** Ver `ODS_IMPACT_SQL_QUERIES.md` línea 248-255
- **Nota:** Requiere campos `HITLDECISIONDATE` y `HITLCREATEDAT`

---

## ❌ ODS 12 - Consumo Responsable (3 KPIs)

### ❌ KPI 12.1: Tasa de Reutilización de Modelos
- **Factible:** ❌ NO
- **Razón:** No confirmada relación `MODMODELS`-`PRJPROJECTS`
- **Solución:** Necesita tabla intermedia o campo en `PRJPROJECTS.METADATA`

### ❌ KPI 12.2: Reducción de Consumo Energético
- **Factible:** ❌ NO
- **Razón:** No hay telemetría de recursos y métricas de infraestructura
- **Solución:** Necesita tabla de telemetría de recursos (CPU, GPU, energía)

### ❌ KPI 12.3: Tasa de Uso de Modelos Eficientes
- **Factible:** ❌ NO
- **Razón:** No hay campo de optimización en `MODMODELS`
- **Solución:** Añadir campo `OPTIMIZED` o `EFFICIENCY_SCORE` a `MODMODELS`

---

## ❌ ODS 3 - Salud y Bienestar (2 KPIs)

### ❌ KPI 3.1: Tasa de Detección de Incidentes
- **Factible:** ❌ NO
- **Razón:** No existe tabla `GOVINCIDENTS` (existe `GOVQMSSERIOUSINCIDENTS` pero es para QMS, no sistemas de salud)
- **Solución:** Crear tabla `GOVINCIDENTS` específica para incidentes de sistemas de salud

### ❌ KPI 3.2: Tiempo Promedio de Detección
- **Factible:** ❌ NO
- **Razón:** Depende de `GOVINCIDENTS` que no existe
- **Solución:** Crear tabla `GOVINCIDENTS` con timestamps de ocurrencia y detección

---

## ❌ ODS 17 - Alianzas (2 KPIs)

### ❌ KPI 17.1: Tasa de Compartir Agentes en Marketplace
- **Factible:** ❌ NO
- **Razón:** No hay campo de marketplace/sharing en `AGTAGENTS`
- **Solución:** Añadir campo `SHARED_IN_MARKETPLACE` o tabla `AGTMARKETPLACE`

### ❌ KPI 17.2: Tasa de Adopción de Recursos Compartidos
- **Factible:** ❌ NO
- **Razón:** No hay telemetría de uso de recursos compartidos
- **Solución:** Necesita tracking de uso de recursos compartidos

---

## ❌ ODS 4 - Educación (2 KPIs)

### ❌ KPI 4.1: Tasa de Uso Educativo de LLMs Open Source
- **Factible:** ❌ NO
- **Razón:** No hay telemetría categorizada por tipo de organización
- **Solución:** Necesita campo `ORGANIZATION_TYPE` en proyectos o tabla de organizaciones

### ❌ KPI 4.2: Número de Bases de Conocimiento Educativas
- **Factible:** ❌ NO
- **Razón:** No hay tabla de RAG pipelines con categorización
- **Solución:** Necesita tabla `RAGPIPELINES` con campo `CATEGORY` (educación, salud, etc.)

---

## ❌ ODS 7 - Energía (1 KPI)

### ❌ KPI 7.1: Reducción de Consumo Energético
- **Factible:** ❌ NO
- **Razón:** Igual que KPI 12.2, no hay telemetría de recursos
- **Solución:** Necesita tabla de telemetría de recursos (CPU, GPU, energía)

---

## 📋 TABLAS QUE FALTAN PARA COMPLETAR TODOS LOS KPIs

### 🔴 **Críticas (necesarias para múltiples KPIs):**

1. **`DSDDATASETS`** - Datasets de entrenamiento
   - Campos necesarios: `BIAS_ANALYSIS`, `GENDER_DISTRIBUTION`, `REPRESENTATIVITY_SCORE`
   - Usado en: ODS 10 (KPIs 10.3, 10.4), ODS 5 (KPIs 5.1, 5.2)

2. **`PRMPROMPTS`** - Prompts y plantillas
   - Campos necesarios: `VALIDATED_FOR_BIAS`, `REUSED_COUNT`, `PROJECT_ID`
   - Usado en: ODS 9 (KPI 9.5), ODS 10 (KPI 10.6)

3. **`GOVINCIDENTS`** - Incidentes de sistemas
   - Campos necesarios: `INCIDENT_TYPE`, `OCCURRED_AT`, `DETECTED_AT`, `SEVERITY`
   - Usado en: ODS 3 (KPIs 3.1, 3.2)

4. **Tabla de Telemetría de Recursos** - Consumo energético
   - Campos necesarios: `RESOURCE_TYPE`, `ENERGY_CONSUMPTION`, `TIMESTAMP`, `PROJECT_ID`
   - Usado en: ODS 12 (KPI 12.2), ODS 7 (KPI 7.1)

### 🟡 **Importantes (mejoran KPIs existentes):**

5. **Relación `AGTAGENTS`-`PRJPROJECTS`**
   - Solución: Tabla intermedia `PRJAGENTS` o campo en `PRJPROJECTS.METADATA`
   - Usado en: ODS 9 (KPI 9.4), ODS 17 (KPI 17.1)

6. **Relación `MODMODELS`-`PRJPROJECTS`**
   - Solución: Tabla intermedia `PRJMODELS` o campo en `PRJPROJECTS.METADATA`
   - Usado en: ODS 12 (KPI 12.1)

7. **Tabla `AIOCOMPONENTS`** - Componentes AI Open Source
   - Campos necesarios: `COMPONENT_TYPE`, `USAGE_COUNT`, `PROJECT_ID`
   - Usado en: ODS 9 (KPI 9.7), ODS 4 (KPI 4.1)

8. **Tabla `RAGPIPELINES`** - Pipelines RAG
   - Campos necesarios: `CATEGORY`, `EDUCATIONAL_USE`, `PROJECT_ID`
   - Usado en: ODS 4 (KPI 4.2)

---

## ✅ PLAN DE ACCIÓN RECOMENDADO

### **Fase 1: KPIs Factibles (Inmediato)**
Implementar los **15 KPIs totalmente factibles** con las tablas existentes:
- ODS 16: 5 KPIs ✅ (+ 1 parcial)
- ODS 9: 4 KPIs ✅
- ODS 10: 4 KPIs ✅
- ODS 5: 0 KPIs (requiere ajustes)
- ODS 8: 2 KPIs ✅
- ODS 12: 0 KPIs
- ODS 3: 0 KPIs
- ODS 17: 0 KPIs
- ODS 4: 0 KPIs
- ODS 7: 0 KPIs

### **Fase 2: Tablas Críticas (Corto plazo)**
Crear tablas necesarias para habilitar más KPIs:
1. `DSDDATASETS` - Habilita 4 KPIs (ODS 10, ODS 5)
2. ~~`PRMPROMPTS`~~ ✅ **YA EXISTE** - Habilita 2 KPIs (ODS 9, ODS 10) - **IMPLEMENTAR QUERIES**
3. `GOVINCIDENTS` - Habilita 2 KPIs (ODS 3)

### **Fase 3: Relaciones y Telemetría (Mediano plazo)**
1. Relaciones `AGTAGENTS`-`PRJPROJECTS` y `MODMODELS`-`PRJPROJECTS`
2. Tabla de telemetría de recursos
3. Tabla `AIOCOMPONENTS` y `RAGPIPELINES`

---

## 📊 RESUMEN POR ODS

| ODS | KPIs Totales | Factibles | Parciales | No Factibles | % Factible |
|-----|--------------|-----------|-----------|--------------|------------|
| ODS 16 | 6 | 5 | 1 | 0 | 83% |
| ODS 9 | 7 | 4 | 0 | 3 | 57% |
| ODS 10 | 6 | 4 | 0 | 2 | 67% |
| ODS 5 | 3 | 0 | 1 | 2 | 0% |
| ODS 8 | 2 | 2 | 0 | 0 | 100% |
| ODS 12 | 3 | 0 | 0 | 3 | 0% |
| ODS 3 | 2 | 0 | 0 | 2 | 0% |
| ODS 17 | 2 | 0 | 0 | 2 | 0% |
| ODS 4 | 2 | 0 | 0 | 2 | 0% |
| ODS 7 | 1 | 0 | 0 | 1 | 0% |
| **TOTAL** | **34** | **15** | **2** | **17** | **44%** |

---

## ⚠️ CONCLUSIÓN

**Con los datos actuales, podemos calcular REALMENTE:**
- ✅ **15 KPIs (44%)** de forma completa
- ⚠️ **2 KPIs (6%)** de forma parcial
- ❌ **17 KPIs (50%)** requieren tablas o datos adicionales

**Recomendación:** Implementar primero los 15 KPIs factibles y documentar claramente cuáles requieren datos adicionales.
