# 🔍 Bias Detection Service

**AI Governance Platform - Python Microservice**

FastAPI service for analyzing bias in ML model predictions using fairness metrics.

---

## 📋 Features

- ✅ **Fairness Metrics**: Demographic Parity, Equal Opportunity, Disparate Impact
- ✅ **Automatic Classification**: NO_BIAS → LOW → MODERATE → HIGH → CRITICAL
- ✅ **Group Analysis**: Performance breakdown by protected attributes
- ✅ **Smart Recommendations**: Automated actionable insights
- ✅ **Production Ready**: Error handling, logging, validation
- ✅ **Docker Support**: Easy deployment

---

## 🚀 Quick Start

### **Option 1: Local Python**

```bash
# Install dependencies
pip install -r requirements.txt

# Run service
python main.py

# Or with uvicorn directly
uvicorn main:app --host 0.0.0.0 --port 8001 --reload
```

### **Option 2: Docker**

```bash
# Build image
docker build -t bias-detection-service:latest .

# Run container
docker run -d -p 8001:8001 --name bias-service bias-detection-service:latest

# Check logs
docker logs -f bias-service
```

### **Option 3: Docker Compose (Recommended)**

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  bias-detection:
    build: .
    ports:
      - "8001:8001"
    environment:
      - LOG_LEVEL=INFO
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8001/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

Run:
```bash
docker-compose up -d
```

---

## 📖 API Documentation

### **Base URL**
```
http://localhost:8001
```

### **Interactive Docs**
- Swagger UI: http://localhost:8001/docs
- ReDoc: http://localhost:8001/redoc

---

## 🔌 Endpoints

### **1. Health Check**

```bash
GET /health
```

**Response:**
```json
{
  "status": "healthy",
  "timestamp": "2025-10-30T20:00:00",
  "dependencies": {
    "pandas": "2.1.4",
    "numpy": "1.26.3"
  }
}
```

---

### **2. Analyze Bias**

```bash
POST /api/bias-analysis/analyze
```

**Request:**
- Content-Type: `multipart/form-data`
- Parameters:
  - `file` (file): CSV file with predictions
  - `model_id` (string): ID of the model
  - `protected_attribute` (string): Column name of protected attribute
  - `favorable_outcome` (string, optional): Favorable outcome value (default: "1")
  - `threshold` (float, optional): Fairness threshold (default: 0.8)

**CSV Format:**
```csv
y_true,y_pred,gender,age,race
1,1,male,35,white
0,0,female,28,black
1,0,female,42,white
...
```

**Required Columns:**
- `y_true`: Actual labels (0 or 1)
- `y_pred`: Predicted labels (0 or 1)
- `{protected_attribute}`: Protected attribute (e.g., gender, race, age)

**Example with curl:**

```bash
curl -X POST "http://localhost:8001/api/bias-analysis/analyze" \
  -F "file=@test_data.csv" \
  -F "model_id=model_123" \
  -F "protected_attribute=gender" \
  -F "favorable_outcome=1" \
  -F "threshold=0.8"
```

**Example with Python requests:**

```python
import requests

files = {'file': open('test_data.csv', 'rb')}
data = {
    'model_id': 'model_123',
    'protected_attribute': 'gender',
    'favorable_outcome': '1',
    'threshold': 0.8
}

response = requests.post(
    'http://localhost:8001/api/bias-analysis/analyze',
    files=files,
    data=data
)

print(response.json())
```

**Response:**

```json
{
  "model_id": "model_123",
  "analysis_date": "2025-10-30T20:15:30.123456",
  "metrics": {
    "demographic_parity_difference": 0.15,
    "equal_opportunity_difference": 0.12,
    "disparate_impact_ratio": 0.78
  },
  "classification": "MODERATE",
  "recommendations": "⚠️ Moderate bias detected in gender attribute.\nPerformance gap of 15.0% between 'female' and 'male' groups.\n\nRecommended actions:\n1. Review model before production deployment\n2. Consider rebalancing training dataset\n3. Monitor model performance by group in production\n4. Explore fairness-aware algorithms",
  "groups_analysis": [
    {
      "group": "male",
      "accuracy": 0.85,
      "precision": 0.82,
      "recall": 0.88,
      "count": 1000
    },
    {
      "group": "female",
      "accuracy": 0.70,
      "precision": 0.68,
      "recall": 0.72,
      "count": 800
    }
  ],
  "threshold_used": 0.8,
  "protected_attribute": "gender"
}
```

---

## 📊 Bias Classification Levels

| Level | Criteria | Action Required |
|-------|----------|-----------------|
| **NO_BIAS** | All metrics < 0.05 | ✅ Deploy freely |
| **LOW** | Metrics 0.05 - 0.10 | ⚠️ Monitor continuously |
| **MODERATE** | Metrics 0.10 - 0.20 | ⚠️ Review before deployment |
| **HIGH** | Metrics 0.20 - 0.30 | 🛑 Retrain required |
| **CRITICAL** | Metrics > 0.30 | 🚫 DO NOT DEPLOY |

---

## 🧪 Testing

### **Generate Test Data**

```python
import pandas as pd
import numpy as np

# Create biased test dataset
np.random.seed(42)
n_samples = 1000

# Male group: higher accuracy
male_true = np.random.choice([0, 1], size=500, p=[0.4, 0.6])
male_pred = np.where(male_true == 1, 
                     np.random.choice([0, 1], size=500, p=[0.15, 0.85]),
                     np.random.choice([0, 1], size=500, p=[0.85, 0.15]))

# Female group: lower accuracy (biased)
female_true = np.random.choice([0, 1], size=500, p=[0.4, 0.6])
female_pred = np.where(female_true == 1,
                       np.random.choice([0, 1], size=500, p=[0.30, 0.70]),
                       np.random.choice([0, 1], size=500, p=[0.70, 0.30]))

# Combine
df = pd.DataFrame({
    'y_true': np.concatenate([male_true, female_true]),
    'y_pred': np.concatenate([male_pred, female_pred]),
    'gender': ['male'] * 500 + ['female'] * 500
})

df.to_csv('test_bias_data.csv', index=False)
print("Test data created: test_bias_data.csv")
```

### **Run Test**

```bash
# Test with generated data
curl -X POST "http://localhost:8001/api/bias-analysis/analyze" \
  -F "file=@test_bias_data.csv" \
  -F "model_id=test_model" \
  -F "protected_attribute=gender"
```

---

## 🔗 Integration with Java Backend

### **Java Example (RestTemplate)**

```java
@Service
public class BiasAnalysisService {
    
    private final RestTemplate restTemplate;
    private final String biasServiceUrl = "http://localhost:8001";
    
    public BiasAnalysisResult analyzeBias(
        MultipartFile csvFile,
        String modelId,
        String protectedAttribute
    ) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", csvFile.getResource());
        body.add("model_id", modelId);
        body.add("protected_attribute", protectedAttribute);
        body.add("threshold", 0.8);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        HttpEntity<MultiValueMap<String, Object>> request = 
            new HttpEntity<>(body, headers);
        
        ResponseEntity<BiasAnalysisResult> response = restTemplate.postForEntity(
            biasServiceUrl + "/api/bias-analysis/analyze",
            request,
            BiasAnalysisResult.class
        );
        
        return response.getBody();
    }
}
```

---

## ⚙️ Configuration

### **Environment Variables**

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Service port | 8001 |
| `LOG_LEVEL` | Logging level | INFO |
| `MAX_FILE_SIZE_MB` | Max CSV size | 100 |
| `TIMEOUT_SECONDS` | Analysis timeout | 30 |

### **Update `main.py`:**

```python
import os

MAX_FILE_SIZE_MB = int(os.getenv('MAX_FILE_SIZE_MB', '100'))
TIMEOUT_SECONDS = int(os.getenv('TIMEOUT_SECONDS', '30'))
```

---

## 🐛 Error Handling

### **Error Response Format**

```json
{
  "detail": "Missing required columns: ['protected_attribute']. Found columns: ['y_true', 'y_pred']"
}
```

### **HTTP Status Codes**

| Code | Description |
|------|-------------|
| 200 | Success |
| 400 | Invalid request (bad CSV, missing columns) |
| 413 | File too large (>100MB) |
| 500 | Internal server error |
| 504 | Timeout (>30 seconds) |

---

## 📈 Performance

- **Small datasets** (<1K rows): ~1-2 seconds
- **Medium datasets** (1K-10K rows): ~2-5 seconds
- **Large datasets** (10K-100K rows): ~5-15 seconds
- **Very large** (>100K rows): Consider sampling

---

## 🔒 Security Considerations

### **Production Deployment:**

1. **Enable Authentication:**
```python
from fastapi.security import HTTPBearer

security = HTTPBearer()

@app.post("/api/bias-analysis/analyze", dependencies=[Depends(security)])
async def analyze_bias(...):
    ...
```

2. **Restrict CORS:**
```python
allow_origins=["https://yourdomain.com"]
```

3. **Rate Limiting:**
```bash
pip install slowapi
```

4. **File Validation:**
- Already implemented: CSV validation, size limits
- Consider: virus scanning for production

---

## 📦 Deployment Options

### **Option 1: Docker Standalone**
```bash
docker run -d -p 8001:8001 bias-detection-service:latest
```

### **Option 2: Kubernetes**
See `k8s-deployment.yaml` (create if needed)

### **Option 3: Cloud Run (GCP)**
```bash
gcloud run deploy bias-detection-service \
  --image gcr.io/PROJECT_ID/bias-detection-service \
  --platform managed \
  --port 8001
```

### **Option 4: AWS ECS**
Create task definition with Docker image

---

## 🧪 Advanced Testing

### **Pytest Tests** (create `test_main.py`):

```python
from fastapi.testclient import TestClient
from main import app
import io

client = TestClient(app)

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "healthy"

def test_bias_analysis():
    # Create test CSV
    csv_content = "y_true,y_pred,gender\n1,1,male\n0,0,female\n"
    files = {"file": ("test.csv", io.StringIO(csv_content), "text/csv")}
    data = {
        "model_id": "test",
        "protected_attribute": "gender"
    }
    
    response = client.post("/api/bias-analysis/analyze", files=files, data=data)
    assert response.status_code == 200
```

---

## 📝 Logs

View logs:
```bash
# Docker
docker logs -f bias-service

# Local
tail -f app.log
```

---

## 🤝 Support

- **Documentation:** [Full docs](https://docs.suinsit.com)
- **Issues:** [GitHub Issues](https://github.com/suinsit/bias-service/issues)
- **Contact:** dev@suinsit.com

---

## 📄 License

Proprietary - AI Governance Platform © 2025

---

**Ready to detect bias! 🚀**

