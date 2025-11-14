# 🔗 INTEGRATION GUIDE - Bias Detection Service

**Complete guide for integrating with Java Spring Boot + ZKoss**

---

## 📋 Overview

This Python microservice provides bias detection capabilities for the AI Governance Platform. It integrates with the Java backend via REST API.

**Architecture:**
```
┌─────────────────┐      HTTP POST      ┌──────────────────┐
│  Java Backend   │ ─────────────────▶ │  Python Service  │
│  (Spring Boot)  │                     │   (FastAPI)      │
│                 │ ◀───────────────── │                  │
│  - ZKoss UI     │      JSON Response  │  - Fairlearn     │
│  - ViewModel    │                     │  - Scikit-learn  │
└─────────────────┘                     └──────────────────┘
```

---

## 🚀 Step-by-Step Integration

### **Step 1: Deploy Python Service**

#### Option A: Docker (Recommended)
```bash
cd bias-detection-service
docker-compose up -d
```

#### Option B: Local Python
```bash
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python main.py
```

**Verify service is running:**
```bash
curl http://localhost:8001/health
```

---

### **Step 2: Add Java Service Class**

Create: `com.codeflowx.platform.service.bias.BiasDetectionService.java`

```java
package com.codeflowx.platform.service.bias;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BiasDetectionService {
    
    @Value("${bias.service.url:http://localhost:8001}")
    private String biasServiceUrl;
    
    private final RestTemplate restTemplate;
    
    public BiasDetectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public BiasAnalysisResult analyzeBias(
        MultipartFile csvFile,
        String modelId,
        String protectedAttribute,
        Double threshold
    ) throws BiasAnalysisException {
        
        try {
            log.info("Analyzing bias for model: {}", modelId);
            
            // Prepare multipart request
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
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
            body.add("threshold", threshold != null ? threshold.toString() : "0.8");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            HttpEntity<MultiValueMap<String, Object>> request = 
                new HttpEntity<>(body, headers);
            
            String url = biasServiceUrl + "/api/bias-analysis/analyze";
            
            ResponseEntity<BiasAnalysisResult> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                BiasAnalysisResult.class
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Bias analysis failed", e);
            throw new BiasAnalysisException("Analysis failed: " + e.getMessage());
        }
    }
    
    public boolean isServiceAvailable() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                biasServiceUrl + "/health",
                String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
}
```

**DTOs:** See `java_integration_example.java` for complete DTO classes.

---

### **Step 3: Add Configuration**

#### `application.properties`
```properties
# Bias Detection Service
bias.service.url=http://localhost:8001
bias.service.timeout=30000
bias.service.max-file-size=100MB
```

#### `RestTemplateConfig.java`
```java
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        return new RestTemplate(factory);
    }
}
```

---

### **Step 4: Update ViewModel**

File: `com.codeflowx.platform.viewmodel.models.ModelBiasAnalysisOverviewViewModel.java`

```java
package com.codeflowx.platform.viewmodel.models;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import codeflowx.nocode.persist.BusinessService;
import com.codeflowx.platform.service.bias.BiasDetectionService;
import com.codeflowx.platform.service.bias.BiasAnalysisResult;
import com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class ModelBiasAnalysisOverviewViewModel extends MasterPage {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private BiasDetectionService biasDetectionService;
    
    private String selectedModelId;
    private String protectedAttribute = "gender";
    private org.zkoss.util.media.Media uploadedFile;
    private BiasAnalysisResult analysisResult;
    private boolean showResults = false;
    
    @Init
    public void init() {
        log.info("Initializing Bias Analysis ViewModel");
        checkServiceAvailability();
    }
    
    private void checkServiceAvailability() {
        if (!biasDetectionService.isServiceAvailable()) {
            Messagebox.show(
                "⚠️ Bias detection service is not available.",
                "Service Warning",
                Messagebox.OK,
                Messagebox.EXCLAMATION
            );
        }
    }
    
    @Command
    @NotifyChange({"uploadedFile"})
    public void handleFileUpload(@BindingParam("event") UploadEvent event) {
        this.uploadedFile = event.getMedia();
        log.info("File uploaded: {}", uploadedFile.getName());
    }
    
    @Command
    @NotifyChange({"analysisResult", "showResults"})
    public void analyzeBias() {
        try {
            // Validate
            if (!validateInputs()) {
                return;
            }
            
            // Convert to MultipartFile
            MultipartFile file = convertToMultipartFile(uploadedFile);
            
            // Call service
            analysisResult = biasDetectionService.analyzeBias(
                file,
                selectedModelId,
                protectedAttribute,
                0.8
            );
            
            showResults = true;
            
            Messagebox.show(
                "✅ Bias analysis completed!",
                "Success",
                Messagebox.OK,
                Messagebox.INFORMATION
            );
            
        } catch (Exception e) {
            log.error("Bias analysis error", e);
            Messagebox.show(
                "❌ Error: " + e.getMessage(),
                "Error",
                Messagebox.OK,
                Messagebox.ERROR
            );
        }
    }
    
    @Command
    @NotifyChange({"showResults", "uploadedFile"})
    public void saveBiasAnalysis() {
        try {
            // Create entity
            ModelBiasAnalysis entity = new ModelBiasAnalysis();
            entity.setModbiastype(protectedAttribute);
            entity.setModseverity(analysisResult.getClassification());
            entity.setModbiasdescription(analysisResult.getRecommendations());
            
            // Save metrics as JSON
            String metricsJson = convertMetricsToJson(analysisResult.getMetrics());
            entity.setModbiasmetrics(metricsJson);
            
            // Save using BusinessService
            businessService.save(entity);
            
            Messagebox.show(
                "✅ Analysis saved successfully!",
                "Success",
                Messagebox.OK,
                Messagebox.INFORMATION
            );
            
        } catch (Exception e) {
            log.error("Error saving analysis", e);
            Messagebox.show(
                "❌ Error saving: " + e.getMessage(),
                "Error",
                Messagebox.OK,
                Messagebox.ERROR
            );
        }
    }
    
    private boolean validateInputs() {
        if (selectedModelId == null || selectedModelId.trim().isEmpty()) {
            Messagebox.show("Please select a model", "Validation", 
                           Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        if (uploadedFile == null) {
            Messagebox.show("Please upload a CSV file", "Validation",
                           Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        if (protectedAttribute == null || protectedAttribute.trim().isEmpty()) {
            Messagebox.show("Please select protected attribute", "Validation",
                           Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        return true;
    }
    
    private MultipartFile convertToMultipartFile(Media media) {
        // Implementation...
        return new MediaMultipartFile(media);
    }
    
    private String convertMetricsToJson(BiasMetrics metrics) {
        // Use Jackson or Gson
        return new Gson().toJson(metrics);
    }
}
```

---

### **Step 5: Update ZUL File**

File: `src/main/webapp/console/platform/models/bias-analysis/overview.zul`

```xml
<zk xmlns:d="desktop" xmlns:h="xhtml">
    
    <div apply="org.zkoss.bind.BindComposer"
         viewModel="@id('vm') @init('com.codeflowx.platform.viewmodel.models.ModelBiasAnalysisOverviewViewModel')">
        
        <h:h2>🔍 Bias Analysis</h:h2>
        
        <!-- Model Selection -->
        <div class="form-group">
            <label>Select Model:</label>
            <combobox value="@bind(vm.selectedModelId)" 
                      width="300px"
                      placeholder="Choose a model...">
                <!-- Load models from database -->
            </combobox>
        </div>
        
        <!-- CSV Upload -->
        <div class="form-group">
            <label>Upload Predictions CSV:</label>
            <button label="Choose File" 
                    upload="true,maxsize=100000"
                    onUpload="@command('handleFileUpload', event=event)" />
            <label value="@load(vm.uploadedFile.name)" 
                   visible="@load(not empty vm.uploadedFile)" />
        </div>
        
        <!-- Protected Attribute -->
        <div class="form-group">
            <label>Protected Attribute:</label>
            <combobox value="@bind(vm.protectedAttribute)" width="200px">
                <comboitem label="Gender" value="gender" />
                <comboitem label="Race" value="race" />
                <comboitem label="Age" value="age_group" />
                <comboitem label="Geography" value="geography" />
            </combobox>
        </div>
        
        <!-- Analyze Button -->
        <button label="🔍 Analyze Bias" 
                onClick="@command('analyzeBias')"
                sclass="btn-primary" />
        
        <!-- Results Section -->
        <div visible="@load(vm.showResults)">
            
            <h:h3>Results</h:h3>
            
            <!-- Classification Badge -->
            <div>
                <label value="Classification:" />
                <label value="@load(vm.analysisResult.classification)"
                       sclass="@load(vm.analysisResult.classification eq 'NO_BIAS' ? 'badge-success' : 
                                     vm.analysisResult.classification eq 'LOW' ? 'badge-info' :
                                     vm.analysisResult.classification eq 'MODERATE' ? 'badge-warning' :
                                     'badge-danger')" />
            </div>
            
            <!-- Metrics -->
            <grid>
                <rows>
                    <row>
                        <label value="Demographic Parity Difference:" />
                        <label value="@load(vm.analysisResult.metrics.demographicParityDifference)" />
                    </row>
                    <row>
                        <label value="Equal Opportunity Difference:" />
                        <label value="@load(vm.analysisResult.metrics.equalOpportunityDifference)" />
                    </row>
                    <row>
                        <label value="Disparate Impact Ratio:" />
                        <label value="@load(vm.analysisResult.metrics.disparateImpactRatio)" />
                    </row>
                </rows>
            </grid>
            
            <!-- Recommendations -->
            <div>
                <h:h4>Recommendations:</h:h4>
                <label value="@load(vm.analysisResult.recommendations)"
                       multiline="true" />
            </div>
            
            <!-- Group Analysis -->
            <listbox model="@load(vm.analysisResult.groupsAnalysis)">
                <listhead>
                    <listheader label="Group" />
                    <listheader label="Accuracy" />
                    <listheader label="Precision" />
                    <listheader label="Recall" />
                    <listheader label="Count" />
                </listhead>
                <template name="model" var="group">
                    <listitem>
                        <listcell label="@load(group.group)" />
                        <listcell label="@load(group.accuracy)" />
                        <listcell label="@load(group.precision)" />
                        <listcell label="@load(group.recall)" />
                        <listcell label="@load(group.count)" />
                    </listitem>
                </template>
            </listbox>
            
            <!-- Save Button -->
            <button label="💾 Save Analysis" 
                    onClick="@command('saveBiasAnalysis')"
                    sclass="btn-success" />
        </div>
    </div>
</zk>
```

---

## 🧪 Testing Integration

### **Test 1: Generate Test Data**
```bash
cd bias-detection-service
python generate_test_data.py moderate
```

### **Test 2: Test Python Service**
```bash
./test_request.sh
```

### **Test 3: Test from Java**
```java
@Test
public void testBiasAnalysis() {
    BiasDetectionService service = new BiasDetectionService(restTemplate);
    
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "test.csv",
        "text/csv",
        csvContent.getBytes()
    );
    
    BiasAnalysisResult result = service.analyzeBias(
        file,
        "test_model",
        "gender",
        0.8
    );
    
    assertNotNull(result);
    assertNotNull(result.getClassification());
}
```

---

## 🐛 Troubleshooting

### **Problem: Service not responding**

**Check:**
```bash
# Is service running?
docker ps | grep bias-detection

# Check logs
docker logs bias-detection-service

# Test manually
curl http://localhost:8001/health
```

**Fix:**
```bash
docker-compose restart
```

---

### **Problem: Connection refused from Java**

**Possible causes:**
1. Service not running
2. Wrong URL in `application.properties`
3. Firewall blocking port 8001
4. Docker network issues

**Fix:**
```java
// Add logging
log.info("Calling bias service at: {}", biasServiceUrl);
```

---

### **Problem: File upload fails**

**Check:**
1. File size < 100MB
2. File is CSV format
3. CSV has required columns: `y_true`, `y_pred`, `{protected_attribute}`

**Debug:**
```bash
# Test with curl
curl -X POST "http://localhost:8001/api/bias-analysis/analyze" \
  -F "file=@test.csv" \
  -F "model_id=test" \
  -F "protected_attribute=gender"
```

---

### **Problem: Timeout errors**

**Increase timeout:**
```properties
bias.service.timeout=60000
```

```python
# In main.py
uvicorn.run(app, host="0.0.0.0", port=8001, timeout_keep_alive=60)
```

---

## 📊 Monitoring

### **Health Check Endpoint**
```bash
# Automated monitoring
while true; do
  curl -s http://localhost:8001/health | jq '.status'
  sleep 60
done
```

### **Logging**
```bash
# View logs
docker logs -f bias-detection-service

# Filter errors only
docker logs bias-detection-service 2>&1 | grep ERROR
```

---

## 🚀 Production Deployment

### **1. Environment Variables**
```bash
# Set in production
export BIAS_SERVICE_URL=https://bias-api.yourdomain.com
export LOG_LEVEL=WARNING
export MAX_FILE_SIZE_MB=50
```

### **2. HTTPS/SSL**
Use nginx or cloud load balancer:
```nginx
server {
    listen 443 ssl;
    server_name bias-api.yourdomain.com;
    
    location / {
        proxy_pass http://localhost:8001;
        proxy_set_header Host $host;
    }
}
```

### **3. Authentication**
Add API key authentication:
```python
from fastapi.security import APIKeyHeader

api_key_header = APIKeyHeader(name="X-API-Key")

@app.post("/api/bias-analysis/analyze", dependencies=[Depends(verify_api_key)])
async def analyze_bias(...):
    ...
```

---

## 📦 Complete File Structure

```
bias-detection-service/
├── main.py                      # FastAPI application
├── requirements.txt             # Python dependencies
├── Dockerfile                   # Docker image
├── docker-compose.yml           # Docker Compose config
├── README.md                    # Full documentation
├── QUICKSTART.md                # Quick start guide
├── INTEGRATION_GUIDE.md         # This file
├── test_request.sh              # Test script
├── generate_test_data.py        # Test data generator
├── test_main.py                 # Unit tests
├── java_integration_example.java # Java code examples
├── .dockerignore
└── .gitignore
```

---

## ✅ Integration Checklist

- [ ] Python service deployed and running
- [ ] Health check endpoint responding
- [ ] Java service class created
- [ ] RestTemplate configured with timeout
- [ ] Configuration added to application.properties
- [ ] ViewModel updated with bias analysis logic
- [ ] ZUL file created/updated
- [ ] DTOs defined for response parsing
- [ ] Test data generated
- [ ] End-to-end test successful
- [ ] Error handling implemented
- [ ] Logging configured
- [ ] Production deployment planned

---

**Need help?** See `README.md` for full documentation or `QUICKSTART.md` for quick setup.

