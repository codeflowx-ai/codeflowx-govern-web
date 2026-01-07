# 📊 QUERIES SQL PARA KPIs ODS IMPACT

**Fecha:** Diciembre 2025
**Propósito:** Documentar las queries SQL reales necesarias para calcular los KPIs ODS desde las tablas existentes

---

## ⚠️ IMPORTANTE

**Los datos actuales en los dashboards son MOCK (inventados para desarrollo).**
**Estas queries deben implementarse en el backend para obtener datos reales.**

---

## 📋 KPIs ODS 16 - Instituciones Sólidas

### KPI 16.1: Tasa de Trazabilidad Completa

```sql
-- Porcentaje de sistemas/activos con trazabilidad completa
WITH systems_with_traceability AS (
  SELECT DISTINCT
    p.IDXPROJECT,
    CASE
      WHEN EXISTS (
        SELECT 1 FROM IMLIMMUTABLELOGS iml
        WHERE iml.IMLENTITYTYPE = 'Project'
          AND iml.IMLENTITYID = p.IDXPROJECT
          AND iml.IMLACTION IN ('MODEL_DEPLOYMENT', 'AGENT_EXECUTION', 'PROMPT_CHANGE')
      ) THEN 1 ELSE 0
    END as has_traceability
  FROM PRJPROJECTS p
)
SELECT
  (COUNT(CASE WHEN has_traceability = 1 THEN 1 END) * 100.0 / COUNT(*)) as traceability_rate
FROM systems_with_traceability;
```

### KPI 16.2: Tasa de Certificación de Sistemas

```sql
-- Porcentaje de sistemas certificados
SELECT
  (COUNT(CASE WHEN ca.READYFORCERTIFICATION = true THEN 1 END) * 100.0 /
   COUNT(CASE WHEN p.PRJISHIGHRISK = true THEN 1 END)) as certification_rate
FROM PRJPROJECTS p
LEFT JOIN COMCOMPLIANCEASSESSMENTS ca ON ca.IDXPROJECT = p.IDXPROJECT
WHERE p.PRJISHIGHRISK = true;
```

### KPI 16.3: Score Promedio de Compliance

```sql
-- Score promedio QMS
SELECT
  AVG(qms.QMSOVERALLSCORE) as avg_compliance_score
FROM GOVQUALITYMANAGEMENTSYSTEMS qms;
```

### KPI 16.4: Tasa de Registro en BD UE

```sql
-- Porcentaje de sistemas registrados en BD UE
SELECT
  (COUNT(DISTINCT reg.IDXEUREGISTRATION) * 100.0 /
   COUNT(CASE WHEN p.PRJISHIGHRISK = true THEN 1 END)) as eu_registration_rate
FROM PRJPROJECTS p
LEFT JOIN REGEUREGISTRATIONS reg ON reg.IDXPROJECT = p.IDXPROJECT
  AND reg.REGSTATUS = 'REGISTERED'
WHERE p.PRJISHIGHRISK = true;
```

### KPI 16.5: Tasa de Aprobación Humana (HITL)

```sql
-- Porcentaje de decisiones con HITL
SELECT
  (COUNT(CASE WHEN hd.IDXHITLDECISION IS NOT NULL THEN 1 END) * 100.0 /
   COUNT(*)) as hitl_rate
FROM GOVHITLSUPERVISIONS hs
LEFT JOIN GOVHITLDECISIONS hd ON hd.IDXHITLSUPERVISION = hs.IDXHITLSUPERVISION;
```

### KPI 16.6: Tiempo Promedio de Auditoría

```sql
-- Tiempo promedio de auditoría (horas)
SELECT
  AVG(EXTRACT(EPOCH FROM (iml2.IMLTIMESTAMP - iml1.IMLTIMESTAMP)) / 3600) as avg_audit_hours
FROM IMLIMMUTABLELOGS iml1
JOIN IMLIMMUTABLELOGS iml2 ON iml2.IMLPREVIOUSHASH = iml1.IMLCURRENTHASH
WHERE iml1.IMLACTION = 'AUDIT_START'
  AND iml2.IMLACTION = 'AUDIT_COMPLETE';
```

---

## 📋 KPIs ODS 9 - Innovación

### KPI 9.1: Score QMS Promedio
```sql
-- Mismo que KPI 16.3
SELECT AVG(QMSOVERALLSCORE) FROM GOVQUALITYMANAGEMENTSYSTEMS;
```

### KPI 9.2: Tasa de Documentación Completa
```sql
-- Porcentaje de sistemas con documentación técnica completa
SELECT
  (COUNT(CASE WHEN td.TECHSECTIONS IS NOT NULL
    AND jsonb_array_length(td.TECHSECTIONS) >= 11 THEN 1 END) * 100.0 /
   COUNT(*)) as documentation_complete_rate
FROM PRJPROJECTS p
LEFT JOIN GOVAIAACTTECHNICALDOCS td ON td.IDXPROJECT = p.IDXPROJECT;
```

### KPI 9.3: Tiempo Promedio de Certificación
```sql
-- Tiempo promedio desde inicio hasta certificación (días)
SELECT
  AVG(EXTRACT(EPOCH FROM (ca.UPDATEDAT - ca.CREATEDAT)) / 86400) as avg_certification_days
FROM COMCOMPLIANCEASSESSMENTS ca
WHERE ca.READYFORCERTIFICATION = true;
```

### KPI 9.4: Tasa de Reutilización de Agentes
```sql
-- Porcentaje de agentes reutilizados (usados en múltiples proyectos)
WITH agent_usage AS (
  SELECT
    pa.IDXAGENT,
    COUNT(DISTINCT pa.IDXPROJECT) as usage_count
  FROM PRJAGENTS pa
  GROUP BY pa.IDXAGENT
)
SELECT
  (COUNT(CASE WHEN usage_count > 1 THEN 1 END) * 100.0 / COUNT(*)) as agent_reuse_rate
FROM agent_usage;
```

### KPI 9.5: Tasa de Reutilización de Prompts
```sql
-- Porcentaje de prompts reutilizados (usados en múltiples proyectos)
WITH prompt_usage AS (
  SELECT
    p.IDXPROMPT,
    COUNT(DISTINCT p.IDXPROJECT) as usage_count
  FROM PRMPROMPTS p
  GROUP BY p.IDXPROMPT
)
SELECT
  (COUNT(CASE WHEN usage_count > 1 THEN 1 END) * 100.0 / COUNT(*)) as prompt_reuse_rate
FROM prompt_usage;
```

### KPI 9.6: Tiempo Promedio de Ciclo MLOps
```sql
-- Tiempo promedio entrenamiento-despliegue (días)
-- Calculado desde TRNTRAININGEXECUTIONS (fin entrenamiento) hasta srvdeployment (despliegue)
SELECT
  AVG(EXTRACT(EPOCH FROM (d.srv_created_at - t.TRNENDDATE)) / 86400) as avg_mlops_cycle_days
FROM TRNTRAININGEXECUTIONS t
JOIN srvdeployment d ON d.srv_model_id = t.TRNMODELID
WHERE t.TRNENDDATE IS NOT NULL
  AND d.srv_created_at IS NOT NULL
  AND t.TRNSTATUS = 'COMPLETED';
```

### KPI 9.7: Tasa de Adopción de LLMs Open Source
```sql
-- Porcentaje de uso de LLMs open source
-- Calculado desde AIOCOMPONENTS donde AIOCOMPONENTTYPE = 'LLM' y es open source
SELECT
  (COUNT(CASE WHEN ac.AIOCOMPONENTTYPE = 'LLM'
    AND (ac.AIOCOMPONENTMETADATA::jsonb->>'isOpenSource')::boolean = true THEN 1 END) * 100.0 /
   COUNT(CASE WHEN ac.AIOCOMPONENTTYPE = 'LLM' THEN 1 END)) as llm_opensource_rate
FROM AIOCOMPONENTS ac
WHERE ac.AIOCOMPONENTTYPE = 'LLM';
```

---

## 📋 KPIs ODS 10 - Reducción de Desigualdades

### KPI 10.1: Tasa de FRIA Completada
```sql
-- Porcentaje de sistemas de alto riesgo con FRIA completada
SELECT
  (COUNT(CASE WHEN f.FRIASTATUS = 'COMPLETED' THEN 1 END) * 100.0 /
   COUNT(CASE WHEN p.PRJISHIGHRISK = true THEN 1 END)) as fria_completion_rate
FROM PRJPROJECTS p
LEFT JOIN FRIAFUNDAMENTALRIGHTSASSESSMENTS f ON f.IDXPROJECT = p.IDXPROJECT
WHERE p.PRJISHIGHRISK = true;
```

### KPI 10.2: Riesgo Promedio en FRIA
```sql
-- Riesgo promedio calculado en FRIA
SELECT
  AVG((f.FRARISKS::jsonb->>'overallRisk')::numeric) as avg_fria_risk
FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS f
WHERE f.FRIASTATUS = 'COMPLETED';
```

### KPI 10.3: Tasa de Detección de Sesgos en Datasets
```sql
-- Porcentaje de datasets con análisis de sesgos completado
SELECT
  (COUNT(CASE WHEN d.DSDBIASANALYZED = true THEN 1 END) * 100.0 /
   COUNT(*)) as bias_detection_rate
FROM DSDDATASETS d
WHERE d.DSDSTATUS = 'ACTIVE';
```

### KPI 10.4: Score de Representatividad de Datasets
```sql
-- Score promedio de representatividad
SELECT
  AVG(d.DSDREPRESENTATIVITYSCORE) as avg_representativity_score
FROM DSDDATASETS d
WHERE d.DSDREPRESENTATIVITYANALYZED = true;
```

### KPI 10.5: Tasa de Sistemas Prohibidos Detectados
```sql
-- Número de sistemas prohibidos detectados (debe ser 0)
SELECT COUNT(*) as prohibited_systems_count
FROM PRJPROJECTS p
WHERE EXISTS (
  SELECT 1 FROM GOVPROHIBITEDSYSTEMS ps
  WHERE ps.PROHIBITEDACTIVE = true
    AND p.METADATA::jsonb->>'prohibitedSystemCode' = ps.PROHIBITEDCODE
);
```

### KPI 10.6: Tasa de Prompts Validados por Sesgos
```sql
-- Porcentaje de prompts con validación de sesgos completada
SELECT
  (COUNT(DISTINCT CASE WHEN pv.PRMVALIDATIONTYPE = 'BIAS'
    AND pv.PRMSTATUS = 'PASSED' THEN p.IDXPROMPT END) * 100.0 /
   COUNT(DISTINCT p.IDXPROMPT)) as bias_validation_rate
FROM PRMPROMPTS p
LEFT JOIN PRMPROMPTVALIDATIONS pv ON pv.IDPRMPROMPTS0 = p.IDXPROMPT
WHERE p.PRMSTATUS = 'ACTIVE';
```

---

## 📋 KPIs ODS 5 - Igualdad de Género

### KPI 5.1: Tasa de Datasets Balanceados por Género
```sql
-- Porcentaje de datasets con balance de género adecuado (>= 40% para cada género)
SELECT
  (COUNT(CASE WHEN d.DSDGENDERBALANCED = true THEN 1 END) * 100.0 /
   COUNT(CASE WHEN d.DSDGENDERANALYZED = true THEN 1 END)) as gender_balanced_rate
FROM DSDDATASETS d
WHERE d.DSDGENDERANALYZED = true;
```

### KPI 5.2: Score de Representación de Género
```sql
-- Score promedio de representación de género
SELECT
  AVG(d.DSDGENDERBALANCESCORE) as avg_gender_representation_score
FROM DSDDATASETS d
WHERE d.DSDGENDERANALYZED = true;
```

### KPI 5.3: Tasa de Sistemas con Evaluación de Sesgos de Género
```sql
-- Porcentaje de sistemas evaluados por sesgos de género
SELECT
  (COUNT(CASE WHEN f.FRARISKS::jsonb->>'genderBiasAssessed' = 'true' THEN 1 END) * 100.0 /
   COUNT(*)) as gender_bias_evaluation_rate
FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS f;
```

---

## 📋 KPIs ODS 8 - Trabajo Decente

### KPI 8.1: Tasa de Supervisión Humana (HITL)
```sql
-- Mismo que KPI 16.5
```

### KPI 8.2: Tiempo Promedio de Respuesta HITL
```sql
-- Tiempo promedio de respuesta humana (horas)
SELECT
  AVG(EXTRACT(EPOCH FROM (hd.HITLDECISIONDATE - hs.HITLCREATEDAT)) / 3600) as avg_hitl_response_hours
FROM GOVHITLSUPERVISIONS hs
JOIN GOVHITLDECISIONS hd ON hd.IDXHITLSUPERVISION = hs.IDXHITLSUPERVISION;
```

---

## 📋 KPIs ODS 12 - Consumo Responsable

### KPI 12.1: Tasa de Reutilización de Modelos
```sql
-- Porcentaje de modelos reutilizados (desplegados en múltiples proyectos)
WITH model_usage AS (
  SELECT
    d.srv_model_id,
    COUNT(DISTINCT d.srv_project_id) as usage_count
  FROM srvdeployment d
  GROUP BY d.srv_model_id
)
SELECT
  (COUNT(CASE WHEN usage_count > 1 THEN 1 END) * 100.0 / COUNT(*)) as model_reuse_rate
FROM model_usage;
```

### KPI 12.2: Reducción de Consumo Energético
```sql
-- Porcentaje promedio de reducción de consumo energético vs baseline
SELECT
  AVG(t.TELENERGYREDUCTIONPERCENTAGE) as avg_energy_reduction_percentage
FROM TELRESOURCETELEMETRY t
WHERE t.TELOPTIMIZED = true
  AND t.TELENERGYREDUCTIONPERCENTAGE IS NOT NULL;
```

### KPI 12.3: Tasa de Uso de Modelos Eficientes
```sql
-- Porcentaje de modelos optimizados
SELECT
  (COUNT(CASE WHEN m.MODOPTIMIZED = true THEN 1 END) * 100.0 /
   COUNT(*)) as efficient_models_rate
FROM MODMODELS m
WHERE m.MODSTATUS = 'ACTIVE';
```

---

## 📋 KPIs ODS 3 - Salud y Bienestar

### KPI 3.1: Tasa de Detección de Incidentes
```sql
-- Porcentaje de incidentes detectados (todos los incidentes en GOVINCIDENTS están detectados)
-- Si hay campo INCDETECTEDAT, usar ese. Si no, usar INCCREATEDAT como proxy
SELECT
  (COUNT(CASE WHEN i.INCDETECTEDAT IS NOT NULL OR i.INCCREATEDAT IS NOT NULL THEN 1 END) * 100.0 /
   COUNT(*)) as incident_detection_rate
FROM GOVINCIDENTS i;
```

### KPI 3.2: Tiempo Promedio de Detección
```sql
-- Tiempo promedio desde ocurrencia hasta detección (horas)
-- Si existe INCDETECTEDAT, usar ese. Si no, usar INCCREATEDAT como proxy
SELECT
  AVG(EXTRACT(EPOCH FROM (
    COALESCE(i.INCDETECTEDAT, i.INCCREATEDAT) - i.INCINCIDENTDATE
  )) / 3600) as avg_detection_hours
FROM GOVINCIDENTS i
WHERE i.INCINCIDENTDATE IS NOT NULL;
```

---

## 📋 KPIs ODS 17 - Alianzas

### KPI 17.1: Tasa de Compartir Agentes en Marketplace
```sql
-- Porcentaje de agentes compartidos
-- Necesita tabla AGTAGENTS con campo de marketplace/sharing
```

### KPI 17.2: Tasa de Adopción de Recursos Compartidos
```sql
-- Porcentaje de uso de recursos compartidos
-- Necesita telemetría de uso de recursos compartidos
```

---

## 📋 KPIs ODS 4 - Educación

### KPI 4.1: Tasa de Uso Educativo de LLMs Open Source
```sql
-- Porcentaje de proyectos educativos usando LLMs Open Source
WITH educational_projects AS (
  SELECT DISTINCT p.IDXPROJECT
  FROM PRJPROJECTS p
  JOIN ORGORGANIZATIONS o ON o.IDXORGANIZATION = p.IDXORGANIZATION
  WHERE o.ORGTYPE = 'EDUCATIONAL'
),
llm_usage AS (
  SELECT DISTINCT p.IDXPROJECT
  FROM PRJPROJECTS p
  JOIN AIOCOMPONENTS ac ON ac.IDXPROJECT = p.IDXPROJECT
  WHERE ac.AIOCOMPONENTTYPE = 'LLM'
    AND (ac.AIOCOMPONENTMETADATA::jsonb->>'isOpenSource')::boolean = true
)
SELECT
  (COUNT(DISTINCT lu.IDXPROJECT) * 100.0 /
   COUNT(DISTINCT ep.IDXPROJECT)) as educational_llm_opensource_rate
FROM educational_projects ep
LEFT JOIN llm_usage lu ON lu.IDXPROJECT = ep.IDXPROJECT;
```

### KPI 4.2: Número de Bases de Conocimiento Educativas
```sql
-- Cantidad de bases de conocimiento RAG para educación
SELECT
  COUNT(*) as educational_knowledge_bases_count
FROM RAGPIPELINES rp
WHERE rp.RAGEDUCATIONALUSE = true
  AND rp.RAGSTATUS = 'ACTIVE';
```

---

## 📋 KPIs ODS 7 - Energía

### KPI 7.1: Reducción de Consumo Energético
```sql
-- Porcentaje promedio de reducción de consumo energético (mismo que KPI 12.2)
SELECT
  AVG(t.TELENERGYREDUCTIONPERCENTAGE) as avg_energy_reduction_percentage
FROM TELRESOURCETELEMETRY t
WHERE t.TELOPTIMIZED = true
  AND t.TELENERGYREDUCTIONPERCENTAGE IS NOT NULL;
```

---

## ⚠️ NOTAS IMPORTANTES

1. **Algunas tablas pueden no existir aún** (ej: `DSDDATASETS`, `PRMPROMPTS`, tablas de telemetría específicas)
2. **Los campos JSONB** requieren parsing específico según estructura
3. **Las agregaciones temporales** necesitan funciones de ventana o CTEs
4. **Performance:** Estas queries pueden ser pesadas, considerar:
   - Índices en campos de JOIN y WHERE
   - Materialized views para agregaciones
   - Caché de resultados
   - Actualización incremental

---

## 🔄 PRÓXIMOS PASOS

1. **Validar existencia de tablas** mencionadas
2. **Revisar estructura real** de campos JSONB
3. **Implementar queries en backend** (Repository/Service)
4. **Crear endpoints REST** para cada KPI
5. **Reemplazar datos mock** por llamadas reales
