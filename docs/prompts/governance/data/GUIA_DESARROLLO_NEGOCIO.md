# 💼 GUÍA DE DESARROLLO DEL ÁREA DE NEGOCIO - GOBIERNO DEL DATO

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Backend Developers, Business Logic Developers, Architects

---

## 📋 INTRODUCCIÓN

Esta guía describe cómo desarrollar la lógica de negocio del módulo de Gobierno del Dato en el backend Java. Incluye servicios, repositorios, DTOs, validaciones y reglas de negocio.

---

## 🏗️ ARQUITECTURA DEL BACKEND

### Estructura de Paquetes

```
nocode-service/
├── codeflowx.govern.entitys/
│   └── src/main/java/com/codeflowx/govern/entity/governance/
│       ├── DataGovernanceDataset.java
│       ├── DataGovernanceOrigin.java
│       ├── DataGovernanceQualityMetric.java
│       ├── DataGovernanceDatasetRisk.java
│       ├── DataGovernanceDatasetPrivacy.java
│       ├── DataGovernanceLineage.java
│       ├── DataGovernanceDatasetDocumentation.java
│       └── DataGovernanceDatasetAnalysisConfig.java
│
├── codeflowx.govern.repository/
│   └── src/main/java/com/codeflowx/govern/repository/governance/
│       ├── DataGovernanceDatasetRepository.java
│       ├── DataGovernanceOriginRepository.java
│       └── ...
│
├── codeflowx.govern.nocode.dtos/
│   └── src/main/java/com/codeflowx/govern/nocode/dtos/datagovernance/
│       ├── DataGovernanceDatasetDto.java
│       ├── DataGovernanceOriginDto.java
│       └── ...
│
├── codeflowx.govern.bff.governance/
│   └── src/main/java/com/codeflowx/govern/bff/governance/
│       ├── controller/
│       │   ├── DataGovernanceDatasetController.java
│       │   ├── DataGovernanceOriginController.java
│       │   └── ...
│       ├── service/
│       │   ├── DataGovernanceDatasetService.java
│       │   ├── DataGovernanceOriginService.java
│       │   └── ...
│       └── service/impl/
│           ├── DataGovernanceDatasetServiceImpl.java
│           └── ...
│
└── codeflowx.govern.workflow.lib/
    └── src/main/java/com/codeflowx/govern/workflow/
        ├── delegates/dataset/
        └── services/
```

---

## 📊 ENTIDADES JPA

### DataGovernanceDataset

```java
@Entity
@Table(name = "dtg_dataset", schema = "governance")
public class DataGovernanceDataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxdataset")
    private Long idxdataset;

    @Column(name = "dtguuid", unique = true, nullable = false)
    private String dtguuid; // UUID generado automáticamente

    @Column(name = "dtgname", nullable = false)
    private String dtgname;

    @Column(name = "dtgdescription", length = 4000)
    private String dtgdescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "dtgtype")
    private DatasetType dtgtype; // TRAINING, VALIDATION, TEST, PRODUCTION, RAG

    @Enumerated(EnumType.STRING)
    @Column(name = "dtgorigintype")
    private OriginType dtgorigintype; // INTERNAL, EXTERNAL

    @Column(name = "dtgformat")
    private String dtgformat; // CSV, JSON, PARQUET, etc.

    @Column(name = "dtgpath")
    private String dtgpath; // Ruta en MinIO/S3

    @Column(name = "dtgsize")
    private Long dtgsize; // Tamaño en bytes

    @Column(name = "dtgchecksum")
    private String dtgchecksum; // SHA256

    @Column(name = "dtgqualityscore")
    private Double dtgqualityscore; // Score global 0.0-1.0

    @Enumerated(EnumType.STRING)
    @Column(name = "dtgstatus")
    private DatasetStatus dtgstatus; // DRAFT, PENDING, APPROVED, REJECTED

    @OneToOne(mappedBy = "dataset", cascade = CascadeType.ALL)
    private DataGovernanceDatasetAnalysisConfig analysisConfig;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<DataGovernanceQualityMetric> qualityMetrics;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<DataGovernanceDatasetRisk> risks;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<DataGovernanceLineage> lineage;

    // Audit fields
    @Column(name = "dtgcreatedate")
    private LocalDateTime dtgcreatedate;

    @Column(name = "dtgupdatedate")
    private LocalDateTime dtgupdatedate;

    @Column(name = "dtgcreateuser")
    private String dtgcreateuser;

    @Column(name = "dtgupdateuser")
    private String dtgupdateuser;
}
```

### DataGovernanceDatasetAnalysisConfig

```java
@Entity
@Table(name = "dtg_dataset_analysis_config", schema = "governance")
public class DataGovernanceDatasetAnalysisConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxconfig")
    private Long idxconfig;

    @OneToOne
    @JoinColumn(name = "idxdataset", nullable = false, unique = true)
    private DataGovernanceDataset dataset;

    @Column(name = "dtgsamplepercentage")
    @Min(10)
    @Max(100)
    private Integer dtgsamplepercentage = 10; // Porcentaje de muestra (10-100%)

    @Column(name = "dtgscheduleenabled")
    private Boolean dtgscheduleenabled = false;

    @Column(name = "dtgscheduletime")
    private String dtgscheduletime; // Formato HH:mm (ej: "02:00")

    @Column(name = "dtgschedulemonday")
    private Boolean dtgschedulemonday = false;

    @Column(name = "dtgscheduletuesday")
    private Boolean dtgscheduletuesday = false;

    @Column(name = "dtgschedulewednesday")
    private Boolean dtgschedulewednesday = false;

    @Column(name = "dtgschedulethursday")
    private Boolean dtgschedulethursday = false;

    @Column(name = "dtgschedulefriday")
    private Boolean dtgschedulefriday = false;

    @Column(name = "dtgschedulesaturday")
    private Boolean dtgschedulesaturday = false;

    @Column(name = "dtgschedulesunday")
    private Boolean dtgschedulesunday = false;
}
```

---

## 🔧 SERVICIOS DE NEGOCIO

### DataGovernanceDatasetService

```java
public interface DataGovernanceDatasetService {

    /**
     * Crear nuevo dataset
     */
    DataGovernanceDatasetDto createDataset(DataGovernanceDatasetDto dto);

    /**
     * Actualizar dataset existente
     */
    DataGovernanceDatasetDto updateDataset(Long id, DataGovernanceDatasetDto dto);

    /**
     * Obtener dataset por ID
     */
    DataGovernanceDatasetDto getDatasetById(Long id);

    /**
     * Listar datasets con filtros
     */
    Page<DataGovernanceDatasetDto> listDatasets(
        String search,
        DatasetType type,
        OriginType originType,
        DatasetStatus status,
        Pageable pageable
    );

    /**
     * Eliminar dataset
     */
    void deleteDataset(Long id);

    /**
     * Ejecutar análisis de calidad
     */
    void executeQualityAnalysis(Long datasetId, Integer samplePercentage);

    /**
     * Obtener configuración de análisis
     */
    DataGovernanceDatasetAnalysisConfigDto getAnalysisConfig(Long datasetId);

    /**
     * Guardar configuración de análisis
     */
    DataGovernanceDatasetAnalysisConfigDto saveAnalysisConfig(
        Long datasetId,
        DataGovernanceDatasetAnalysisConfigDto config
    );
}
```

### Implementación del Servicio

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DataGovernanceDatasetServiceImpl implements DataGovernanceDatasetService {

    private final DataGovernanceDatasetRepository datasetRepository;
    private final DataGovernanceDatasetAnalysisConfigRepository configRepository;
    private final AIGovernanceClient aiGovernanceClient;
    private final RuntimeService runtimeService; // Flowable

    @Override
    public DataGovernanceDatasetDto createDataset(DataGovernanceDatasetDto dto) {
        // 1. Generar UUID
        String uuid = UUID.randomUUID().toString();

        // 2. Crear entidad
        DataGovernanceDataset dataset = DataGovernanceDataset.builder()
            .dtguuid(uuid)
            .dtgname(dto.getName())
            .dtgdescription(dto.getDescription())
            .dtgtype(dto.getType())
            .dtgorigintype(dto.getOriginType())
            .dtgformat(dto.getFormat())
            .dtgstatus(DatasetStatus.DRAFT)
            .dtgcreatedate(LocalDateTime.now())
            .dtgcreateuser(getCurrentUser())
            .build();

        // 3. Guardar
        dataset = datasetRepository.save(dataset);

        // 4. Si hay configuración de análisis, guardarla
        if (dto.getAnalysisConfig() != null) {
            saveAnalysisConfig(dataset.getIdxdataset(), dto.getAnalysisConfig());
        }

        // 5. Disparar workflow de aprobación
        startApprovalWorkflow(dataset.getIdxdataset());

        // 6. Retornar DTO
        return mapToDto(dataset);
    }

    @Override
    public void executeQualityAnalysis(Long datasetId, Integer samplePercentage) {
        DataGovernanceDataset dataset = datasetRepository.findById(datasetId)
            .orElseThrow(() -> new EntityNotFoundException("Dataset no encontrado"));

        // 1. Validar que el dataset tiene formato Parquet
        if (!"PARQUET".equals(dataset.getDtgformat())) {
            throw new BusinessException("El dataset debe estar en formato Parquet");
        }

        // 2. Validar porcentaje de muestra
        if (samplePercentage < 10 || samplePercentage > 100) {
            throw new BusinessException("El porcentaje de muestra debe estar entre 10% y 100%");
        }

        // 3. Llamar al microservicio Python
        DatasetQualityRequest request = DatasetQualityRequest.builder()
            .datasetPath(dataset.getDtgpath())
            .samplePercentage(samplePercentage)
            .build();

        DatasetQualityResponse response = aiGovernanceClient
            .datasetQuality()
            .evaluateDimensions(request);

        // 4. Guardar resultados en base de datos
        saveQualityMetrics(datasetId, response);

        // 5. Actualizar score global
        dataset.setDtgqualityscore(response.getOverallScore());
        datasetRepository.save(dataset);

        // 6. Generar alertas si es necesario
        if (response.getOverallScore() < 0.7) {
            generateQualityAlert(datasetId, response);
        }
    }

    @Override
    public DataGovernanceDatasetAnalysisConfigDto saveAnalysisConfig(
        Long datasetId,
        DataGovernanceDatasetAnalysisConfigDto configDto
    ) {
        DataGovernanceDataset dataset = datasetRepository.findById(datasetId)
            .orElseThrow(() -> new EntityNotFoundException("Dataset no encontrado"));

        // Validar porcentaje
        if (configDto.getSamplePercentage() < 10 || configDto.getSamplePercentage() > 100) {
            throw new BusinessException("El porcentaje debe estar entre 10% y 100%");
        }

        // Validar hora
        if (configDto.getScheduleEnabled() && configDto.getScheduleTime() != null) {
            validateTimeFormat(configDto.getScheduleTime());
        }

        // Validar que hay al menos un día seleccionado si está habilitado
        if (configDto.getScheduleEnabled()) {
            boolean hasDay = configDto.getScheduleDays().getMonday() ||
                           configDto.getScheduleDays().getTuesday() ||
                           configDto.getScheduleDays().getWednesday() ||
                           configDto.getScheduleDays().getThursday() ||
                           configDto.getScheduleDays().getFriday() ||
                           configDto.getScheduleDays().getSaturday() ||
                           configDto.getScheduleDays().getSunday();

            if (!hasDay) {
                throw new BusinessException("Debe seleccionar al menos un día para el análisis programado");
            }
        }

        // Upsert configuración
        DataGovernanceDatasetAnalysisConfig config = configRepository
            .findByDatasetId(datasetId)
            .orElse(new DataGovernanceDatasetAnalysisConfig());

        config.setDataset(dataset);
        config.setDtgsamplepercentage(configDto.getSamplePercentage());
        config.setDtgscheduleenabled(configDto.getScheduleEnabled());
        config.setDtgscheduletime(configDto.getScheduleTime());
        config.setDtgschedulemonday(configDto.getScheduleDays().getMonday());
        config.setDtgscheduletuesday(configDto.getScheduleDays().getTuesday());
        config.setDtgschedulewednesday(configDto.getScheduleDays().getWednesday());
        config.setDtgschedulethursday(configDto.getScheduleDays().getThursday());
        config.setDtgschedulefriday(configDto.getScheduleDays().getFriday());
        config.setDtgschedulesaturday(configDto.getScheduleDays().getSaturday());
        config.setDtgschedulesunday(configDto.getScheduleDays().getSunday());

        config = configRepository.save(config);

        // Si está habilitado, programar análisis
        if (config.getDtgscheduleenabled()) {
            scheduleAnalysis(datasetId, config);
        }

        return mapToDto(config);
    }

    private void startApprovalWorkflow(Long datasetId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("datasetId", datasetId);

        runtimeService.startProcessInstanceByKey(
            "dataset-approval-v1",
            variables
        );
    }

    private void scheduleAnalysis(Long datasetId, DataGovernanceDatasetAnalysisConfig config) {
        // Crear expresión cron basada en configuración
        String cronExpression = buildCronExpression(config);

        // Programar job con Quartz o similar
        // ...
    }

    private String buildCronExpression(DataGovernanceDatasetAnalysisConfig config) {
        // Construir expresión cron: "0 {minute} {hour} ? * {days}"
        // Ejemplo: "0 0 2 ? * MON-FRI" (2 AM de lunes a viernes)
        // ...
    }
}
```

---

## 🔍 VALIDACIONES Y REGLAS DE NEGOCIO

### Validaciones de Dataset

```java
@Component
public class DatasetValidationService {

    public void validateDatasetCreation(DataGovernanceDatasetDto dto) {
        // 1. Validar nombre único
        if (datasetRepository.existsByDtgname(dto.getName())) {
            throw new BusinessException("Ya existe un dataset con ese nombre");
        }

        // 2. Validar tipo y origen
        if (dto.getType() == DatasetType.PRODUCTION &&
            dto.getOriginType() == OriginType.EXTERNAL) {
            throw new BusinessException(
                "Los datasets de producción no pueden ser de origen externo"
            );
        }

        // 3. Validar formato
        if (dto.getFormat() != null &&
            !isSupportedFormat(dto.getFormat())) {
            throw new BusinessException("Formato no soportado: " + dto.getFormat());
        }
    }

    public void validateAnalysisConfig(DataGovernanceDatasetAnalysisConfigDto config) {
        // 1. Validar porcentaje
        if (config.getSamplePercentage() < 10) {
            throw new BusinessException(
                "El porcentaje mínimo recomendado es 10%"
            );
        }

        if (config.getSamplePercentage() > 100) {
            throw new BusinessException("El porcentaje no puede ser mayor a 100%");
        }

        // 2. Validar hora
        if (config.getScheduleEnabled() && config.getScheduleTime() != null) {
            if (!config.getScheduleTime().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                throw new BusinessException("Formato de hora inválido. Use HH:mm");
            }
        }

        // 3. Validar días
        if (config.getScheduleEnabled()) {
            ScheduleDaysDto days = config.getScheduleDays();
            boolean hasDay = days.getMonday() || days.getTuesday() ||
                           days.getWednesday() || days.getThursday() ||
                           days.getFriday() || days.getSaturday() ||
                           days.getSunday();

            if (!hasDay) {
                throw new BusinessException(
                    "Debe seleccionar al menos un día para el análisis programado"
                );
            }
        }
    }
}
```

---

## 📝 DTOs

### DataGovernanceDatasetDto

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataGovernanceDatasetDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private DatasetType type;

    @NotNull
    private OriginType originType;

    private String format;

    private String path;

    private Long size;

    private String checksum;

    private Double qualityScore;

    private DatasetStatus status;

    private DataGovernanceDatasetAnalysisConfigDto analysisConfig;

    private List<DataGovernanceQualityMetricDto> qualityMetrics;

    private List<DataGovernanceDatasetRiskDto> risks;

    private List<DataGovernanceLineageDto> lineage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
```

### DataGovernanceDatasetAnalysisConfigDto

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataGovernanceDatasetAnalysisConfigDto {

    @Min(10)
    @Max(100)
    @NotNull
    private Integer samplePercentage;

    @NotNull
    private Boolean scheduleEnabled;

    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String scheduleTime;

    @NotNull
    private ScheduleDaysDto scheduleDays;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDaysDto {
        private Boolean monday;
        private Boolean tuesday;
        private Boolean wednesday;
        private Boolean thursday;
        private Boolean friday;
        private Boolean saturday;
        private Boolean sunday;
    }
}
```

---

## 🔄 INTEGRACIÓN CON MICROSERVICIOS PYTHON

### Cliente para Dataset Quality

```java
// En AIGovernanceClient.java
public DatasetQualityClient datasetQuality() {
    return new DatasetQualityClient(webClient, baseUrl + "/api/dataset-quality");
}

// Uso en servicio
DatasetQualityResponse response = aiGovernanceClient
    .datasetQuality()
    .evaluateDimensions(DatasetQualityRequest.builder()
        .datasetPath(dataset.getDtgpath())
        .samplePercentage(samplePercentage)
        .build());
```

---

## 📊 REPOSITORIOS

### DataGovernanceDatasetRepository

```java
@Repository
public interface DataGovernanceDatasetRepository extends JpaRepository<DataGovernanceDataset, Long> {

    boolean existsByDtgname(String name);

    Optional<DataGovernanceDataset> findByDtguuid(String uuid);

    Page<DataGovernanceDataset> findByDtgnameContainingIgnoreCase(
        String name,
        Pageable pageable
    );

    Page<DataGovernanceDataset> findByDtgtypeAndDtgstatus(
        DatasetType type,
        DatasetStatus status,
        Pageable pageable
    );

    List<DataGovernanceDataset> findByDtgqualityscoreLessThan(Double threshold);
}
```

---

## 📝 CHECKLIST DE DESARROLLO

### Servicios de Negocio

- [ ] Implementar `DataGovernanceDatasetService`
- [ ] Implementar `DataGovernanceOriginService`
- [ ] Implementar `DataGovernanceQualityMetricService`
- [ ] Implementar `DataGovernanceDatasetRiskService`
- [ ] Implementar `DataGovernanceDatasetPrivacyService`
- [ ] Implementar `DataGovernanceLineageService`
- [ ] Implementar `DataGovernanceDatasetDocumentationService`
- [ ] Implementar `DataGovernanceDatasetAnalysisConfigService`
- [ ] Implementar validaciones de negocio
- [ ] Integrar con microservicios Python
- [ ] Integrar con workflows BPMN
- [ ] Tests unitarios
- [ ] Tests de integración

### DTOs y Mapeo

- [ ] Crear DTOs para todas las entidades
- [ ] Implementar mappers (MapStruct o manual)
- [ ] Validaciones con Bean Validation
- [ ] Documentación Swagger

### Repositorios

- [ ] Crear repositorios JPA
- [ ] Agregar queries personalizadas
- [ ] Optimizar queries con índices
- [ ] Tests de repositorios

---

## 🚀 PRÓXIMOS PASOS

1. **Implementar servicios core:** Dataset, Origin, Quality
2. **Agregar validaciones:** Reglas de negocio y validaciones
3. **Integrar microservicios:** Conectar con Python
4. **Integrar workflows:** Disparar procesos BPMN
5. **Testing:** Tests completos
6. **Documentación:** Swagger y documentación técnica

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** 📋 Planificación
