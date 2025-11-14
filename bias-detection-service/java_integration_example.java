/**
 * AI Governance Platform - Java Integration Example
 * 
 * Example code for integrating Bias Detection Service with Spring Boot + ZKoss
 * 
 * Package: com.codeflowx.platform.service.bias
 */

package com.codeflowx.platform.service.bias;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class BiasDetectionService {
    
    private final RestTemplate restTemplate;
    private final String biasServiceUrl;
    
    public BiasDetectionService() {
        this.restTemplate = new RestTemplate();
        // TODO: Move to application.properties
        this.biasServiceUrl = "http://localhost:8001";
    }
    
    /**
     * Analyze bias in model predictions
     * 
     * @param csvFile CSV file with predictions
     * @param modelId ID of the model
     * @param protectedAttribute Protected attribute to analyze (e.g., "gender", "race")
     * @param threshold Fairness threshold (default: 0.8)
     * @return BiasAnalysisResult with metrics and recommendations
     * @throws BiasAnalysisException if analysis fails
     */
    public BiasAnalysisResult analyzeBias(
        MultipartFile csvFile,
        String modelId,
        String protectedAttribute,
        Double threshold
    ) throws BiasAnalysisException {
        
        try {
            log.info("Starting bias analysis for model {} on attribute {}", 
                     modelId, protectedAttribute);
            
            // Validate inputs
            if (csvFile == null || csvFile.isEmpty()) {
                throw new BiasAnalysisException("CSV file is required");
            }
            
            if (modelId == null || modelId.trim().isEmpty()) {
                throw new BiasAnalysisException("Model ID is required");
            }
            
            if (protectedAttribute == null || protectedAttribute.trim().isEmpty()) {
                throw new BiasAnalysisException("Protected attribute is required");
            }
            
            // Set default threshold
            if (threshold == null) {
                threshold = 0.8;
            }
            
            // Prepare multipart request
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // Convert MultipartFile to ByteArrayResource
            ByteArrayResource fileResource = new ByteArrayResource(csvFile.getBytes()) {
                @Override
                public String getFilename() {
                    return csvFile.getOriginalFilename();
                }
            };
            
            body.add("file", fileResource);
            body.add("model_id", modelId);
            body.add("protected_attribute", protectedAttribute);
            body.add("favorable_outcome", "1");
            body.add("threshold", threshold.toString());
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            // Create request entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = 
                new HttpEntity<>(body, headers);
            
            // Call Python service
            String url = biasServiceUrl + "/api/bias-analysis/analyze";
            
            log.debug("Calling bias detection service: {}", url);
            
            ResponseEntity<BiasAnalysisResult> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                BiasAnalysisResult.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                BiasAnalysisResult result = response.getBody();
                log.info("Bias analysis completed. Classification: {}", 
                         result.getClassification());
                return result;
            } else {
                throw new BiasAnalysisException(
                    "Bias service returned status: " + response.getStatusCode()
                );
            }
            
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
            throw new BiasAnalysisException("Error reading CSV file: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error during bias analysis", e);
            throw new BiasAnalysisException("Bias analysis failed: " + e.getMessage());
        }
    }
    
    /**
     * Check if bias detection service is available
     * 
     * @return true if service is healthy
     */
    public boolean isServiceAvailable() {
        try {
            String url = biasServiceUrl + "/health";
            ResponseEntity<HealthResponse> response = restTemplate.getForEntity(
                url,
                HealthResponse.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("Bias detection service not available", e);
            return false;
        }
    }
}

/**
 * Response DTOs
 */

@Data
class BiasAnalysisResult {
    @JsonProperty("model_id")
    private String modelId;
    
    @JsonProperty("analysis_date")
    private String analysisDate;
    
    @JsonProperty("metrics")
    private BiasMetrics metrics;
    
    @JsonProperty("classification")
    private String classification; // NO_BIAS, LOW, MODERATE, HIGH, CRITICAL
    
    @JsonProperty("recommendations")
    private String recommendations;
    
    @JsonProperty("groups_analysis")
    private List<GroupAnalysis> groupsAnalysis;
    
    @JsonProperty("threshold_used")
    private Double thresholdUsed;
    
    @JsonProperty("protected_attribute")
    private String protectedAttribute;
}

@Data
class BiasMetrics {
    @JsonProperty("demographic_parity_difference")
    private Double demographicParityDifference;
    
    @JsonProperty("equal_opportunity_difference")
    private Double equalOpportunityDifference;
    
    @JsonProperty("disparate_impact_ratio")
    private Double disparateImpactRatio;
}

@Data
class GroupAnalysis {
    @JsonProperty("group")
    private String group;
    
    @JsonProperty("accuracy")
    private Double accuracy;
    
    @JsonProperty("precision")
    private Double precision;
    
    @JsonProperty("recall")
    private Double recall;
    
    @JsonProperty("count")
    private Integer count;
}

@Data
class HealthResponse {
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("timestamp")
    private String timestamp;
}

/**
 * Custom exception
 */
class BiasAnalysisException extends Exception {
    public BiasAnalysisException(String message) {
        super(message);
    }
    
    public BiasAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}


/**
 * EXAMPLE USAGE IN VIEWMODEL
 * 
 * File: ModelBiasAnalysisOverviewViewModel.java
 */

/*
package com.codeflowx.platform.viewmodel.models;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.zk.ui.event.UploadEvent;

@Slf4j
@Getter
@Setter
public class ModelBiasAnalysisOverviewViewModel extends MasterPage {
    
    @WireVariable
    private BiasDetectionService biasDetectionService;
    
    private String selectedModelId;
    private String protectedAttribute;
    private org.zkoss.util.media.Media uploadedFile;
    private BiasAnalysisResult analysisResult;
    private boolean showResults = false;
    
    @Init
    public void init() {
        log.info("Initializing Bias Analysis ViewModel");
        
        // Check if service is available
        if (!biasDetectionService.isServiceAvailable()) {
            Messagebox.show(
                "Bias detection service is not available. Please contact administrator.",
                "Service Unavailable",
                Messagebox.OK,
                Messagebox.EXCLAMATION
            );
        }
    }
    
    @Command
    @NotifyChange({"uploadedFile"})
    public void handleFileUpload(@BindingParam("event") UploadEvent event) {
        log.info("File uploaded: {}", event.getMedia().getName());
        this.uploadedFile = event.getMedia();
    }
    
    @Command
    @NotifyChange({"analysisResult", "showResults"})
    public void analyzeBias() {
        try {
            // Validate inputs
            if (selectedModelId == null || selectedModelId.trim().isEmpty()) {
                Messagebox.show("Please select a model", "Validation Error", 
                               Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (uploadedFile == null) {
                Messagebox.show("Please upload a CSV file", "Validation Error",
                               Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (protectedAttribute == null || protectedAttribute.trim().isEmpty()) {
                Messagebox.show("Please select a protected attribute", "Validation Error",
                               Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            // Convert Media to MultipartFile
            MultipartFile multipartFile = convertMediaToMultipartFile(uploadedFile);
            
            // Call bias detection service
            log.info("Calling bias detection service...");
            
            analysisResult = biasDetectionService.analyzeBias(
                multipartFile,
                selectedModelId,
                protectedAttribute,
                0.8
            );
            
            showResults = true;
            
            Messagebox.show(
                "Bias analysis completed successfully!",
                "Success",
                Messagebox.OK,
                Messagebox.INFORMATION
            );
            
        } catch (BiasAnalysisException e) {
            log.error("Error during bias analysis", e);
            Messagebox.show(
                "Error during bias analysis: " + e.getMessage(),
                "Error",
                Messagebox.OK,
                Messagebox.ERROR
            );
        }
    }
    
    @Command
    @NotifyChange({"analysisResult", "uploadedFile", "showResults"})
    public void saveBiasAnalysis() {
        // TODO: Save to database using BusinessService
        // Save to ModelBiasAnalysis entity
    }
    
    private MultipartFile convertMediaToMultipartFile(org.zkoss.util.media.Media media) {
        // Implementation to convert ZKoss Media to Spring MultipartFile
        // ... implementation details
        return null; // Placeholder
    }
}
*/

