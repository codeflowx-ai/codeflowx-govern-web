package com.codeflowx.govern.business.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelAdaptationStrategy;
import com.codeflowx.govern.entity.views.models.ModelLineageTree;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Business Service para gestión de adaptación de modelos
 * Art. 51-55 EU AI Act - GPAI Downstream Providers
 */
@Slf4j
@Service
public class ModelAdaptationBusinessService {

    @Autowired
    private BusinessService businessService;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Value("${leka.model.wrapper.url:http://localhost:8006}")
    private String modelWrapperUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Recomienda estrategia de adaptación óptima
     * Llama micro Python para análisis multi-criteria
     */
    public AdaptationRecommendation recommendStrategy(AdaptationRequest request) {
        log.info("Recomendando estrategia adaptación: useCase={}, budget={}, priority={}", 
                request.getUseCase(), request.getBudgetUsd(), request.getPriority());

        try {
            if (restTemplate != null) {
                String endpoint = modelWrapperUrl + "/api/model/recommend-adaptation";
                
                ResponseEntity<AdaptationRecommendation> response = restTemplate.postForEntity(
                    endpoint,
                    request,
                    AdaptationRecommendation.class
                );
                
                if (response.getBody() != null) {
                    log.info("Recomendación recibida: strategy={}, confidence={}", 
                            response.getBody().getRecommendedStrategy(),
                            response.getBody().getConfidence());
                    return response.getBody();
                }
            }
        } catch (Exception e) {
            log.warn("Error llamando micro Python, usando recomendación por defecto: {}", e.getMessage());
        }

        // Recomendación por defecto (mock)
        return createDefaultRecommendation(request);
    }

    /**
     * Guarda estrategia de adaptación en base de datos
     */
    public ModelAdaptationStrategy saveStrategy(AdaptationRequest request, AdaptationRecommendation recommendation, String selectedStrategy, String selectionReason) {
        log.info("Guardando estrategia de adaptación: selected={}", selectedStrategy);

        try {
            ModelAdaptationStrategy strategy = new ModelAdaptationStrategy();
            strategy.setIduuid(UUID.randomUUID().toString());
            strategy.setIdxproject(request.getProjectId());
            strategy.setModusecase(request.getUseCase());
            strategy.setModtargettask(request.getTargetTask());
            strategy.setModbudgetusd(request.getBudgetUsd());
            strategy.setModtimedays(request.getTimeDays());
            strategy.setModtargetperformance(request.getTargetPerformance());
            strategy.setModhardwareavailable(request.getHardwareAvailable());
            strategy.setModpriority(request.getPriority());
            strategy.setModrecommendations(objectMapper.writeValueAsString(recommendation));
            strategy.setModselectedstrategy(selectedStrategy);
            strategy.setModselectedreason(selectionReason);
            strategy.setModcreatedby(request.getCreatedBy());
            strategy.setModcreatedat(new Timestamp(System.currentTimeMillis()));
            
            businessService.save(strategy);
            
            log.info("Estrategia guardada: id={}, strategy={}", strategy.getIdxadaptationstrategy(), selectedStrategy);
            
            return strategy;
        } catch (Exception e) {
            log.error("Error guardando estrategia adaptación", e);
            throw new RuntimeException("Error guardando estrategia: " + e.getMessage(), e);
        }
    }

    /**
     * Crea modelo adapter desde base model
     * Art. 53 - Documentar modificaciones GPAI
     */
    public Model createAdapter(Long baseModelId, AdapterConfig config, String createdBy) {
        log.info("Creando adapter: baseModelId={}, type={}", baseModelId,
                config != null ? config.getAdapterType() : "ADAPTER_LORA");

        try {
            Model baseModel = requireBaseModel(baseModelId);
            AdapterConfig safeConfig = config != null ? config : new AdapterConfig();

            Model adapter = initializeDerivedModel(
                baseModel,
                safeConfig.getAdapterName(),
                "-adapter",
                "Adapter created from " + baseModel.getModname(),
                createdBy
            );

            adapter.setModisadapter(true);
            adapter.setModadaptationstrategy(
                safeConfig.getAdapterType() != null ? safeConfig.getAdapterType() : "ADAPTER_LORA");
            adapter.setModadapterconfig(toJson(safeConfig));
            adapter.setModgpaimodificationsdocurl(safeConfig.getModificationsDocUrl());

            Map<String, Object> metadata = buildAdaptationMetadata("ADAPTER", baseModelId);
            metadata.put("adapter_type", adapter.getModadaptationstrategy());
            metadata.put("rank", safeConfig.getRank());
            metadata.put("alpha", safeConfig.getAlpha());
            metadata.put("target_modules", safeConfig.getTargetModules());
            metadata.put("dropout", safeConfig.getDropout());
            metadata.put("training_dataset", safeConfig.getTrainingDataset());
            metadata.put("target_performance", safeConfig.getTargetPerformance());
            mergeExtraMetadata(metadata, safeConfig.getExtraMetadata());
            adapter.setModadaptationmetadata(toJson(metadata));

            businessService.save(adapter);

            log.info("Adapter creado: id={}, baseModel={}", adapter.getIdxmodel(), baseModelId);
            return adapter;
        } catch (Exception e) {
            log.error("Error creando adapter", e);
            throw new RuntimeException("Error creando adapter: " + e.getMessage(), e);
        }
    }

    /**
     * Crea modelo fine-tuned a partir de un modelo base
     */
    public Model createFineTuned(Long baseModelId, FineTuningConfig config, String createdBy) {
        log.info("Creando fine-tuned: baseModelId={}, epochs={}", baseModelId,
                config != null ? config.getEpochs() : null);

        try {
            Model baseModel = requireBaseModel(baseModelId);
            FineTuningConfig safeConfig = config != null ? config : new FineTuningConfig();

            Model fineTuned = initializeDerivedModel(
                baseModel,
                safeConfig.getTargetName(),
                "-finetuned",
                "Fine-tuned model derived from " + baseModel.getModname(),
                createdBy
            );

            fineTuned.setModisfinetuned(true);
            fineTuned.setModadaptationstrategy("FINE_TUNING");
            fineTuned.setModfinetuningconfig(toJson(safeConfig));
            fineTuned.setModgpaimodificationsdocurl(safeConfig.getModificationsDocUrl());

            Map<String, Object> metadata = buildAdaptationMetadata("FINE_TUNING", baseModelId);
            metadata.put("epochs", safeConfig.getEpochs());
            metadata.put("learning_rate", safeConfig.getLearningRate());
            metadata.put("training_dataset", safeConfig.getTrainingDataset());
            metadata.put("batch_size", safeConfig.getBatchSize());
            metadata.put("scheduler", safeConfig.getScheduler());
            mergeExtraMetadata(metadata, safeConfig.getExtraMetadata());
            fineTuned.setModadaptationmetadata(toJson(metadata));

            businessService.save(fineTuned);
            log.info("Fine-tuned model creado: id={}, baseModel={}", fineTuned.getIdxmodel(), baseModelId);
            return fineTuned;
        } catch (Exception e) {
            log.error("Error creando fine-tuned", e);
            throw new RuntimeException("Error creando fine-tuned: " + e.getMessage(), e);
        }
    }

    /**
     * Crea modelo quantized a partir de un modelo base
     */
    public Model createQuantized(Long baseModelId, QuantizationConfig config, String createdBy) {
        log.info("Creando quantized: baseModelId={}, bits={}", baseModelId,
                config != null ? config.getBits() : null);

        try {
            Model baseModel = requireBaseModel(baseModelId);
            QuantizationConfig safeConfig = config != null ? config : new QuantizationConfig();

            Model quantized = initializeDerivedModel(
                baseModel,
                safeConfig.getTargetName(),
                "-quantized",
                "Quantized model derived from " + baseModel.getModname(),
                createdBy
            );

            String strategy = safeConfig.getMethod() != null ? safeConfig.getMethod() : "QUANTIZATION";
            quantized.setModisquantized(true);
            quantized.setModadaptationstrategy(strategy);
            quantized.setModquantizationconfig(toJson(safeConfig));
            quantized.setModgpaimodificationsdocurl(safeConfig.getModificationsDocUrl());

            Map<String, Object> metadata = buildAdaptationMetadata("QUANTIZATION", baseModelId);
            metadata.put("method", strategy);
            metadata.put("bits", safeConfig.getBits());
            metadata.put("target_layers", safeConfig.getTargetLayers());
            metadata.put("hardware", safeConfig.getHardware());
            metadata.put("expected_latency_gain", safeConfig.getExpectedLatencyGain());
            metadata.put("expected_co2_savings", safeConfig.getExpectedCo2Savings());
            mergeExtraMetadata(metadata, safeConfig.getExtraMetadata());
            quantized.setModadaptationmetadata(toJson(metadata));

            businessService.save(quantized);
            log.info("Quantized model creado: id={}, baseModel={}", quantized.getIdxmodel(), baseModelId);
            return quantized;
        } catch (Exception e) {
            log.error("Error creando quantized", e);
            throw new RuntimeException("Error creando quantized: " + e.getMessage(), e);
        }
    }

    /**
     * Crea modelo merged a partir de múltiples modelos base
     */
    public Model createMerged(List<Long> modelIds, MergeConfig config, String createdBy) {
        List<Long> sources = (config != null && config.getSourceModelIds() != null && !config.getSourceModelIds().isEmpty())
            ? config.getSourceModelIds()
            : (modelIds != null ? modelIds : Collections.emptyList());

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("Se requieren modelos origen para merge");
        }

        MergeConfig safeConfig = config != null ? config : new MergeConfig();
        if (safeConfig.getSourceModelIds() == null || safeConfig.getSourceModelIds().isEmpty()) {
            safeConfig.setSourceModelIds(sources);
        }

        Long baseModelId = sources.get(0);
        log.info("Creando merged: baseModelId={}, method={}, sources={}", baseModelId,
                safeConfig.getMethod(), sources);

        try {
            Model baseModel = requireBaseModel(baseModelId);

            Model merged = initializeDerivedModel(
                baseModel,
                safeConfig.getTargetName(),
                "-merged",
                "Merged model derived from sources " + sources,
                createdBy
            );

            String strategy = safeConfig.getMethod() != null ? safeConfig.getMethod() : "MERGE";
            merged.setModismerged(true);
            merged.setModadaptationstrategy(strategy);
            merged.setModmergeconfig(toJson(safeConfig));
            merged.setModgpaimodificationsdocurl(safeConfig.getModificationsDocUrl());

            Map<String, Object> metadata = buildAdaptationMetadata("MERGE", baseModelId);
            metadata.put("source_models", sources);
            metadata.put("weights", safeConfig.getWeights());
            metadata.put("method", strategy);
            metadata.put("comment", safeConfig.getComment());
            mergeExtraMetadata(metadata, safeConfig.getExtraMetadata());
            merged.setModadaptationmetadata(toJson(metadata));

            businessService.save(merged);
            log.info("Merged model creado: id={}, sources={}", merged.getIdxmodel(), sources);
            return merged;
        } catch (Exception e) {
            log.error("Error creando merged", e);
            throw new RuntimeException("Error creando merged: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene lineage tree completo de un modelo
     */
    public List<ModelLineageNode> getModelLineage(Long modelId) {
        log.info("Obteniendo lineage tree para modelo: {}", modelId);

        try {
            ModelLineageTree current = businessService.findBySQL(
                ModelLineageTree.class,
                "SELECT * FROM V_MODEL_LINEAGE_TREE WHERE model_id = ?",
                modelId
            );

            if (current == null) {
                return new ArrayList<>();
            }

            List<ModelLineageTree> nodes = businessService.findListBySQL(
                ModelLineageTree.class,
                "SELECT * FROM V_MODEL_LINEAGE_TREE WHERE root_model_id = ? ORDER BY path",
                current.getRootModelId()
            );

            return nodes.stream()
                .map(this::mapLineageNode)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error obteniendo lineage", e);
            return new ArrayList<>();
        }
    }

    /**
     * Verifica si una nueva relación generaría un ciclo en el lineage
     */
    public boolean wouldCreateCycle(Long baseModelId, Long candidateModelId) {
        if (baseModelId == null || candidateModelId == null) {
            return false;
        }

        try {
            ModelLineageTree candidate = businessService.findBySQL(
                ModelLineageTree.class,
                "SELECT * FROM V_MODEL_LINEAGE_TREE WHERE model_id = ?",
                candidateModelId
            );

            if (candidate == null || candidate.getPath() == null) {
                return false;
            }

            boolean createsCycle = pathContains(candidate.getPath(), baseModelId);
            log.debug("Evaluación ciclo base={} candidate={} result={}", baseModelId, candidateModelId, createsCycle);
            return createsCycle;
        } catch (Exception e) {
            log.warn("No fue posible evaluar ciclos para base={} candidate={} error={}",
                    baseModelId, candidateModelId, e.getMessage());
            return false;
        }
    }

    /**
     * Crea recomendación por defecto (mock)
     */
    private AdaptationRecommendation createDefaultRecommendation(AdaptationRequest request) {
        AdaptationRecommendation rec = new AdaptationRecommendation();
        rec.setRecommendedStrategy("ADAPTER_LORA");
        rec.setRecommendedBaseModel("llama-2-7b");
        rec.setConfidence(0.92);
        rec.setJustification("LoRA adapter provides best cost/performance balance for this use case");
        
        List<StrategyAlternative> alternatives = new ArrayList<>();
        
        // Opción 1: ADAPTER_LORA (recomendada)
        StrategyAlternative lora = new StrategyAlternative();
        lora.setStrategy("ADAPTER_LORA");
        lora.setBaseModel("llama-2-7b");
        lora.setScore(0.92);
        lora.setEstimatedCostUsd(new BigDecimal("150"));
        lora.setEstimatedTimeHours(24);
        lora.setEstimatedCo2Kg(new BigDecimal("2.5"));
        lora.setEstimatedPerformance(new BigDecimal("0.87"));
        lora.setPros(List.of("Lowest cost", "Fast training", "Low CO2", "Easy to deploy"));
        lora.setCons(List.of("Slightly lower performance vs fine-tuning"));
        alternatives.add(lora);
        
        // Opción 2: QUANTIZATION (segunda opción)
        StrategyAlternative quant = new StrategyAlternative();
        quant.setStrategy("QUANTIZATION");
        quant.setBaseModel("llama-2-7b");
        quant.setScore(0.78);
        quant.setEstimatedCostUsd(new BigDecimal("50"));
        quant.setEstimatedTimeHours(2);
        quant.setEstimatedCo2Kg(new BigDecimal("0.5"));
        quant.setEstimatedPerformance(new BigDecimal("0.82"));
        quant.setPros(List.of("Fastest", "Cheapest", "Minimal CO2", "Instant deploy"));
        quant.setCons(List.of("Lower performance", "Precision loss"));
        alternatives.add(quant);
        
        // Opción 3: FINE_TUNING (NO recomendada)
        StrategyAlternative ft = new StrategyAlternative();
        ft.setStrategy("FINE_TUNING");
        ft.setBaseModel("llama-2-7b");
        ft.setScore(0.45);
        ft.setEstimatedCostUsd(new BigDecimal("3000"));
        ft.setEstimatedTimeHours(240);
        ft.setEstimatedCo2Kg(new BigDecimal("50"));
        ft.setEstimatedPerformance(new BigDecimal("0.91"));
        ft.setPros(List.of("Highest performance"));
        ft.setCons(List.of("20x cost", "20x CO2", "10x time", "NOT RECOMMENDED"));
        alternatives.add(ft);
        
        rec.setAlternativesRanked(alternatives);
        
        // Sustainability analysis
        Map<String, String> sustainability = new HashMap<>();
        sustainability.put("adapter_vs_finetuning_co2_savings", "95%");
        sustainability.put("adapter_vs_finetuning_cost_savings", "95%");
        sustainability.put("adapter_vs_finetuning_time_savings", "90%");
        rec.setSustainabilityAnalysis(sustainability);
        
        rec.setRecommendation("STRONGLY RECOMMEND ADAPTER - 95% cost/CO2 savings, performance acceptable");
        
        return rec;
    }

    private Model initializeDerivedModel(Model baseModel, String desiredName, String suffix, String description, String createdBy) {
        String name = (desiredName != null && !desiredName.isBlank())
            ? desiredName
            : baseModel.getModname() + suffix + "-" + System.currentTimeMillis();

        Model derived = new Model();
        derived.setModname(name);
        derived.setModdescription(description);
        derived.setModtype(baseModel.getModtype());
        derived.setModframework(baseModel.getModframework());
        derived.setModversion(baseModel.getModversion() != null ? baseModel.getModversion() : "v1.0");
        derived.setModstatus("PENDING_TRAINING");
        derived.setModapprovalstatus("PENDING");
        derived.setIdmodbasemodel(baseModel.getIdxmodel());
        derived.setModisgpai(baseModel.getModisgpai());
        derived.setModcurrentstage("ADAPTATION");
        derived.setModlifecyclestage("ADAPTATION");

        String actor = (createdBy != null && !createdBy.isBlank()) ? createdBy : "SYSTEM";
        derived.setModcreatedby(actor);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        derived.setModcreatedat(now);
        derived.setModcreationtimestamp(now.getTime());

        return derived;
    }

    private Model requireBaseModel(Long baseModelId) {
        if (baseModelId == null) {
            throw new IllegalArgumentException("Base model ID required");
        }
        Model baseModel = businessService.findById(Model.class, baseModelId);
        if (baseModel == null) {
            throw new IllegalArgumentException("Base model not found: " + baseModelId);
        }
        return baseModel;
    }

    private Map<String, Object> buildAdaptationMetadata(String strategy, Long baseModelId) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("strategy", strategy);
        metadata.put("base_model_id", baseModelId);
        metadata.put("timestamp", System.currentTimeMillis());
        return metadata;
    }

    private void mergeExtraMetadata(Map<String, Object> target, Map<String, Object> extraMetadata) {
        if (target == null || extraMetadata == null) {
            return;
        }
        extraMetadata.entrySet().stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> target.put(entry.getKey(), entry.getValue()));
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.warn("No se pudo serializar a JSON: {}", e.getMessage());
            return null;
        }
    }

    private ModelLineageNode mapLineageNode(ModelLineageTree tree) {
        ModelLineageNode node = new ModelLineageNode();
        node.setModelId(tree.getModelId());
        node.setModelName(tree.getModelName());
        node.setParentId(tree.getParentId());
        node.setStrategy(tree.getStrategy());
        node.setDepth(tree.getDepth());
        node.setLineageType(tree.getLineageType());
        return node;
    }

    private boolean pathContains(String path, Long modelId) {
        if (path == null || modelId == null) {
            return false;
        }
        String cleaned = path.replace("{", "").replace("}", "");
        String[] tokens = cleaned.split(",");
        for (String token : tokens) {
            if (token != null && token.trim().equals(String.valueOf(modelId))) {
                return true;
            }
        }
        return false;
    }

    // ========== DTOs ==========

    @Data
    public static class AdaptationRequest {
        private Long projectId;
        private String useCase;
        private String targetTask;
        private List<String> baseModelOptions;
        private Integer datasetSize;
        private BigDecimal budgetUsd;
        private Integer timeDays;
        private List<String> hardware;
        private BigDecimal targetPerformance;
        private String priority; // cost, time, co2, performance
        private String createdBy;
    }

    @Data
    public static class AdaptationRecommendation {
        private String recommendedStrategy;
        private String recommendedBaseModel;
        private Double confidence;
        private String justification;
        private List<StrategyAlternative> alternativesRanked;
        private Map<String, String> sustainabilityAnalysis;
        private String recommendation;
    }

    @Data
    public static class StrategyAlternative {
        private String strategy;
        private String baseModel;
        private Double score;
        private BigDecimal estimatedCostUsd;
        private Integer estimatedTimeHours;
        private BigDecimal estimatedCo2Kg;
        private BigDecimal estimatedPerformance;
        private List<String> pros;
        private List<String> cons;
    }

    @Data
    public static class AdapterConfig {
        private String adapterName;
        private String adapterType; // LORA, QLORA
        private Integer rank;
        private Integer alpha;
        private List<String> targetModules;
        private Double dropout;
        private String trainingDataset;
        private BigDecimal targetPerformance;
        private String modificationsDocUrl;
        private Map<String, Object> extraMetadata;
    }

    @Data
    public static class ModelLineageNode {
        private Long modelId;
        private String modelName;
        private Long parentId;
        private String strategy;
        private Integer depth;
        private String lineageType;
    }

    @Data
    public static class FineTuningConfig {
        private String targetName;
        private Integer epochs;
        private BigDecimal learningRate;
        private Integer batchSize;
        private String scheduler;
        private String trainingDataset;
        private String modificationsDocUrl;
        private Map<String, Object> extraMetadata;
    }

    @Data
    public static class QuantizationConfig {
        private String targetName;
        private String method; // e.g. BITSANDBYTES, GPTQ
        private Integer bits;
        private List<String> targetLayers;
        private String hardware;
        private BigDecimal expectedLatencyGain;
        private BigDecimal expectedCo2Savings;
        private String modificationsDocUrl;
        private Map<String, Object> extraMetadata;
    }

    @Data
    public static class MergeConfig {
        private String targetName;
        private String method; // e.g. SLERP, TIES
        private List<Long> sourceModelIds;
        private List<BigDecimal> weights;
        private String comment;
        private String modificationsDocUrl;
        private Map<String, Object> extraMetadata;
    }
}


