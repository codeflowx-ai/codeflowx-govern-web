package com.codeflowx.govern.workflow.delegates.dataset;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Delegate: Validate Dataset Format
 * 
 * Valida que el dataset existe y está en un formato soportado (CSV, JSON, Parquet).
 * 
 * Responsabilidades:
 * - Verificar que el archivo existe
 * - Verificar extensión del archivo
 * - Guardar resultado en variable 'formatValid'
 */
@Slf4j
@Component("validateDatasetFormatDelegate")
public class ValidateDatasetFormatDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🔍 Validating dataset format...");

            String datasetPath = (String) execution.getVariable("datasetPath");
            
            if (datasetPath == null || datasetPath.isEmpty()) {
                log.error("❌ Dataset path is null or empty");
                execution.setVariable("formatValid", false);
                execution.setVariable("formatError", "Dataset path is required");
                return;
            }

            // Verificar si el archivo existe
            File file = new File(datasetPath);
            if (!file.exists()) {
                log.warn("⚠️ Dataset file does not exist: {}", datasetPath);
                execution.setVariable("formatValid", false);
                execution.setVariable("formatError", "File not found: " + datasetPath);
                return;
            }

            // Verificar extensión
            String lowerPath = datasetPath.toLowerCase();
            boolean isSupportedFormat = 
                lowerPath.endsWith(".csv") ||
                lowerPath.endsWith(".json") ||
                lowerPath.endsWith(".parquet") ||
                lowerPath.endsWith(".jsonl") ||
                lowerPath.endsWith(".tsv");

            if (!isSupportedFormat) {
                log.warn("⚠️ Unsupported file format: {}", datasetPath);
                execution.setVariable("formatValid", false);
                execution.setVariable("formatError", "Unsupported format. Supported: CSV, JSON, Parquet, JSONL, TSV");
                return;
            }

            // Verificar tamaño mínimo (no vacío)
            if (file.length() == 0) {
                log.warn("⚠️ Dataset file is empty: {}", datasetPath);
                execution.setVariable("formatValid", false);
                execution.setVariable("formatError", "File is empty");
                return;
            }

            // Todo OK
            log.info("✅ Dataset format is valid: {}", datasetPath);
            execution.setVariable("formatValid", true);
            execution.setVariable("formatError", null);

        } catch (Exception e) {
            log.error("❌ Error validating dataset format", e);
            execution.setVariable("formatValid", false);
            execution.setVariable("formatError", "Error: " + e.getMessage());
        }
    }
}

