# ✅ IMPLEMENTACIÓN FASE 1 Y FASE 2 - KPIs ODS

**Fecha:** Diciembre 2025
**Estado:** ✅ Scripts SQL y Entidades JPA creados

---

## 📋 RESUMEN DE IMPLEMENTACIÓN

### **Fase 1: DSDDATASETS** ✅
- ✅ Script SQL creado
- ✅ Entidad JPA `Dataset.java` creada
- ✅ Habilita 4 KPIs (ODS 5: 2, ODS 10: 2)

### **Fase 2: Telemetría, Agentes, Organizaciones, RAG** ✅
- ✅ Script SQL creado
- ✅ Entidades JPA creadas:
  - `ResourceTelemetry.java`
  - `ProjectAgent.java`
  - `Organization.java`
  - `RagPipeline.java`
- ✅ Campos añadidos a `MODMODELS`
- ✅ Habilita 5 KPIs (ODS 9: 1, ODS 12: 2, ODS 4: 2)

---

## 📁 ARCHIVOS CREADOS

### **Backend (nocode-service):**

1. **Script SQL:**
   - `nocode.service.entitys/src/main/resources/sql/ods_impact_phase1_phase2_tables.sql`

2. **Entidades JPA:**
   - `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/Dataset.java`
   - `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/telemetry/ResourceTelemetry.java`
   - `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/projects/ProjectAgent.java`
   - `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/core/Organization.java`
   - `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/rag/RagPipeline.java`

3. **Modificaciones:**
   - Campos añadidos a `MODMODELS` (MODOPTIMIZED, MODOPTIMIZATIONTYPE, MODEFFICIENCYSCORE)
   - Campo añadido a `PRJPROJECTS` (IDXORGANIZATION)

---

## 🚀 PASOS PARA IMPLEMENTAR

### **1. Ejecutar Script SQL**

```bash
# Desde el directorio del proyecto
psql -U user -d database -f nocode.service.entitys/src/main/resources/sql/ods_impact_phase1_phase2_tables.sql
```

**Verificar:**
```sql
-- Verificar que las tablas se crearon
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public'
AND table_name IN ('DSDDATASETS', 'TELRESOURCETELEMETRY', 'PRJAGENTS', 'ORGORGANIZATIONS', 'RAGPIPELINES');

-- Verificar campos en MODMODELS
SELECT column_name FROM information_schema.columns
WHERE table_name = 'MODMODELS'
AND column_name IN ('MODOPTIMIZED', 'MODOPTIMIZATIONTYPE', 'MODEFFICIENCYSCORE');
```

### **2. Compilar Entidades JPA**

```bash
# En el proyecto nocode.service.entitys
mvn clean compile
```

**Verificar:** No debe haber errores de compilación.

### **3. Actualizar Queries SQL en Backend**

Las queries actualizadas están en:
- `docs/prompts/compliance/ODS_IMPACT_SQL_QUERIES.md`

**KPIs habilitados:**
- KPI 5.1, 5.2 (ODS 5)
- KPI 10.3, 10.4 (ODS 10)
- KPI 9.4 (ODS 9)
- KPI 12.2, 12.3 (ODS 12)
- KPI 4.1, 4.2 (ODS 4)
- KPI 7.1 (ODS 7)

### **4. Crear Repositorios JPA**

```java
// DatasetRepository
public interface DatasetRepository extends GenericRepository<Dataset, Long> {
    List<Dataset> findByProjectId(Long projectId);
    List<Dataset> findByGenderAnalyzed(Boolean analyzed);
    List<Dataset> findByBiasAnalyzed(Boolean analyzed);
}

// ResourceTelemetryRepository
public interface ResourceTelemetryRepository extends GenericRepository<ResourceTelemetry, Long> {
    List<ResourceTelemetry> findByProjectId(Long projectId);
    List<ResourceTelemetry> findByOptimized(Boolean optimized);
}

// ProjectAgentRepository
public interface ProjectAgentRepository extends GenericRepository<ProjectAgent, Long> {
    List<ProjectAgent> findByProjectId(Long projectId);
    List<ProjectAgent> findByAgentId(Long agentId);
    Long countByAgentId(Long agentId);
}

// OrganizationRepository
public interface OrganizationRepository extends GenericRepository<Organization, Long> {
    List<Organization> findByType(String type);
    List<Organization> findBySubtype(String subtype);
}

// RagPipelineRepository
public interface RagPipelineRepository extends GenericRepository<RagPipeline, Long> {
    List<RagPipeline> findByEducationalUse(Boolean educationalUse);
    List<RagPipeline> findByCategory(String category);
    Long countByEducationalUse(Boolean educationalUse);
}
```

### **5. Crear Servicios de Negocio**

```java
// DatasetBiasAnalysisService
public interface DatasetBiasAnalysisService {
    void analyzeGenderDistribution(Long datasetId);
    void analyzeBias(Long datasetId);
    void calculateRepresentativity(Long datasetId);
    BigDecimal calculateGenderBalanceScore(Long datasetId);
}

// ResourceTelemetryService
public interface ResourceTelemetryService {
    void recordTelemetry(Long projectId, ResourceTelemetryData data);
    BigDecimal calculateEnergyReduction(Long projectId);
    List<ResourceTelemetry> getOptimizedResources(Long projectId);
}
```

### **6. Crear Endpoints REST (BFF)**

```java
// OdsImpactController
@RestController
@RequestMapping("/api/v1/governance/compliance/ods-impact")
public class OdsImpactController {

    // KPIs ODS 5
    @GetMapping("/ods-5/kpi-5-1")
    public ResponseEntity<KPIMetric> getGenderBalancedDatasetsRate();

    @GetMapping("/ods-5/kpi-5-2")
    public ResponseEntity<KPIMetric> getGenderRepresentationScore();

    // KPIs ODS 10
    @GetMapping("/ods-10/kpi-10-3")
    public ResponseEntity<KPIMetric> getBiasDetectionRate();

    @GetMapping("/ods-10/kpi-10-4")
    public ResponseEntity<KPIMetric> getRepresentativityScore();

    // KPI ODS 9
    @GetMapping("/ods-9/kpi-9-4")
    public ResponseEntity<KPIMetric> getAgentReuseRate();

    // KPIs ODS 12
    @GetMapping("/ods-12/kpi-12-2")
    public ResponseEntity<KPIMetric> getEnergyReduction();

    @GetMapping("/ods-12/kpi-12-3")
    public ResponseEntity<KPIMetric> getEfficientModelsRate();

    // KPIs ODS 4
    @GetMapping("/ods-4/kpi-4-1")
    public ResponseEntity<KPIMetric> getEducationalLLMUsageRate();

    @GetMapping("/ods-4/kpi-4-2")
    public ResponseEntity<KPIMetric> getEducationalKnowledgeBasesCount();

    // KPI ODS 7
    @GetMapping("/ods-7/kpi-7-1")
    public ResponseEntity<KPIMetric> getEnergyReduction();
}
```

---

## ✅ RESULTADO ESPERADO

**Después de implementar Fase 1 y Fase 2:**
- ✅ **29 KPIs factibles (85%)**
- ⚠️ **1 KPI parcial (3%)**
- ❌ **4 KPIs pendientes (12%)** - Requieren Fase 3 (Marketplace)

---

## 📊 KPIs HABILITADOS

### **Fase 1 (4 KPIs):**
- ✅ KPI 5.1: Tasa de Datasets Balanceados por Género
- ✅ KPI 5.2: Score de Representación de Género
- ✅ KPI 10.3: Tasa de Detección de Sesgos en Datasets
- ✅ KPI 10.4: Score de Representatividad de Datasets

### **Fase 2 (5 KPIs):**
- ✅ KPI 9.4: Tasa de Reutilización de Agentes
- ✅ KPI 12.2: Reducción de Consumo Energético
- ✅ KPI 12.3: Tasa de Uso de Modelos Eficientes
- ✅ KPI 4.1: Tasa de Uso Educativo de LLMs Open Source
- ✅ KPI 4.2: Número de Bases de Conocimiento Educativas
- ✅ KPI 7.1: Reducción de Consumo Energético (mismo que 12.2)

---

## ⚠️ NOTAS IMPORTANTES

1. **Triggers UUID:** Los triggers se crean automáticamente en el script SQL
2. **Foreign Keys:** Verificar que las tablas referenciadas existan antes de ejecutar
3. **Índices:** Se crean automáticamente para optimizar queries
4. **Campos JSONB:** Requieren parsing en Java (Jackson ObjectMapper)

---

## 🔄 PRÓXIMOS PASOS

1. ✅ Ejecutar script SQL
2. ✅ Compilar entidades JPA
3. ⚠️ Crear repositorios JPA
4. ⚠️ Crear servicios de negocio
5. ⚠️ Crear endpoints REST
6. ⚠️ Actualizar frontend para usar nuevos KPIs
