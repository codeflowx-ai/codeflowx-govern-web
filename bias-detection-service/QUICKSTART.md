# ⚡ QUICKSTART - Bias Detection Service

**Get up and running in 5 minutes!**

---

## 🚀 Option 1: Docker (Recommended)

```bash
# 1. Navigate to service directory
cd bias-detection-service

# 2. Start service
docker-compose up -d

# 3. Check status
docker-compose ps

# 4. View logs
docker-compose logs -f

# 5. Test service
./test_request.sh
```

**Service URL:** http://localhost:8001  
**API Docs:** http://localhost:8001/docs

---

## 🐍 Option 2: Local Python

```bash
# 1. Create virtual environment
python3 -m venv venv
source venv/bin/activate  # Linux/Mac
# or
venv\Scripts\activate  # Windows

# 2. Install dependencies
pip install -r requirements.txt

# 3. Run service
python main.py

# 4. In another terminal, test
./test_request.sh
```

---

## 🧪 Quick Test

### 1. Generate Test Data

```bash
python generate_test_data.py moderate
```

This creates `test_bias_gender_moderate.csv`

### 2. Analyze Bias

```bash
curl -X POST "http://localhost:8001/api/bias-analysis/analyze" \
  -F "file=@test_bias_gender_moderate.csv" \
  -F "model_id=test_model" \
  -F "protected_attribute=gender"
```

### 3. View Results

Expected output:
```json
{
  "classification": "MODERATE",
  "metrics": {
    "demographic_parity_difference": 0.15,
    ...
  },
  "groups_analysis": [
    {"group": "male", "accuracy": 0.85, ...},
    {"group": "female", "accuracy": 0.70, ...}
  ]
}
```

---

## 🔗 Integration with Java

### Add to your ViewModel:

```java
@Service
public class BiasAnalysisService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BIAS_SERVICE_URL = "http://localhost:8001";
    
    public BiasAnalysisResponse analyzeBias(
        MultipartFile csvFile,
        String modelId,
        String protectedAttribute
    ) throws Exception {
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", csvFile.getResource());
        body.add("model_id", modelId);
        body.add("protected_attribute", protectedAttribute);
        body.add("threshold", 0.8);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        HttpEntity<MultiValueMap<String, Object>> request = 
            new HttpEntity<>(body, headers);
        
        ResponseEntity<BiasAnalysisResponse> response = 
            restTemplate.postForEntity(
                BIAS_SERVICE_URL + "/api/bias-analysis/analyze",
                request,
                BiasAnalysisResponse.class
            );
        
        return response.getBody();
    }
}
```

---

## 🛠️ Troubleshooting

### Service not starting?

```bash
# Check port 8001 is free
lsof -i :8001  # Linux/Mac
netstat -ano | findstr :8001  # Windows

# Check logs
docker-compose logs
```

### Import errors?

```bash
# Reinstall dependencies
pip install --upgrade -r requirements.txt
```

### CSV upload fails?

- Check CSV has columns: `y_true`, `y_pred`, `{protected_attribute}`
- Verify no missing values
- Ensure file size < 100MB

---

## 📚 Next Steps

1. ✅ Read full [README.md](README.md)
2. ✅ Explore interactive docs: http://localhost:8001/docs
3. ✅ Integrate with Java backend
4. ✅ Generate more test datasets: `python generate_test_data.py`
5. ✅ Deploy to production (see README)

---

## 🆘 Need Help?

- **Documentation:** See [README.md](README.md)
- **API Docs:** http://localhost:8001/docs
- **Issues:** Contact dev team

---

**Happy bias detecting! 🎯**

