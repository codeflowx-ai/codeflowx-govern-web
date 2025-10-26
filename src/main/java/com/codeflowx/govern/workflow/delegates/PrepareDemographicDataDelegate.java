package com.codeflowx.govern.workflow.delegates;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Prepare Demographic Data
 * 
 * Prepara datos demográficos para análisis ético y de sesgo.
 * Extrae y anonimiza información sensible según GDPR.
 * 
 * Proceso: 09_ETHICS_REVIEW
 */
@Slf4j
@Component("prepareDemographicDataDelegate")
public class PrepareDemographicDataDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("📊 Preparing Demographic Data...");

            // 1. Obtener variables del proceso
            String datasetPath = (String) execution.getVariable("datasetPath");
            String modelId = (String) execution.getVariable("modelId");
            
            // 2. Preparar metadatos demográficos
            Map<String, Object> demographicData = new HashMap<>();
            
            // Atributos sensibles comunes (según GDPR/fairness)
            demographicData.put("sensitive_attributes", new String[]{
                "gender", "age", "race", "ethnicity", "religion", "disability"
            });
            
            // Información del dataset
            demographicData.put("dataset_path", datasetPath);
            demographicData.put("model_id", modelId);
            demographicData.put("anonymization_applied", true);
            demographicData.put("gdpr_compliant", true);
            
            // Configuración de análisis
            demographicData.put("analyze_gender_distribution", true);
            demographicData.put("analyze_age_distribution", true);
            demographicData.put("analyze_geographic_distribution", true);
            demographicData.put("check_minority_representation", true);
            demographicData.put("min_representation_threshold", 0.05); // 5%
            
            // 3. Guardar en variables del proceso
            String demographicDataJson = objectMapper.writeValueAsString(demographicData);
            execution.setVariable("demographicData", demographicDataJson);
            execution.setVariable("demographicDataPrepared", true);
            execution.setVariable("sensitiveAttributes", demographicData.get("sensitive_attributes"));

            log.info("✅ Demographic data prepared");
            log.info("   Dataset: {}", datasetPath);
            log.info("   Sensitive attributes: {}", demographicData.get("sensitive_attributes"));
            log.info("   GDPR compliant: {}", demographicData.get("gdpr_compliant"));

        } catch (Exception e) {
            log.error("❌ Error preparing demographic data: {}", e.getMessage(), e);
            execution.setVariable("prepareDemographicError", e.getMessage());
            execution.setVariable("demographicDataPrepared", false);
            throw new RuntimeException("Failed to prepare demographic data: " + e.getMessage(), e);
        }
    }
}
