# 📑 INDEX - Bias Detection Service Documentation

Quick reference guide to all files in this service.

---

## 🚀 START HERE

**New to this service?** Read in this order:

1. **`DELIVERABLES.md`** - What's included and mission summary
2. **`QUICKSTART.md`** - Get running in 5 minutes
3. **`README.md`** - Full documentation
4. **`INTEGRATION_GUIDE.md`** - Connect with Java backend

---

## 📂 File Directory

### **Core Application Files**

| File | Purpose | Lines | Priority |
|------|---------|-------|----------|
| `main.py` | FastAPI application with bias detection logic | 505 | ⭐⭐⭐ |
| `requirements.txt` | Python dependencies | 20 | ⭐⭐⭐ |
| `Dockerfile` | Docker image configuration | 35 | ⭐⭐⭐ |
| `docker-compose.yml` | Docker Compose deployment | 30 | ⭐⭐⭐ |

**Start with:** `docker-compose up -d`

---

### **Documentation Files**

| File | Audience | When to Read |
|------|----------|--------------|
| `DELIVERABLES.md` | Project coordinator | First - overview of everything |
| `QUICKSTART.md` | Developers (first time) | Second - quick setup |
| `README.md` | All users | Reference - complete docs |
| `INTEGRATION_GUIDE.md` | Java developers | When integrating with backend |
| `INDEX.md` | Everyone | Finding files |

---

### **Testing & Development**

| File | Purpose | How to Use |
|------|---------|------------|
| `test_request.sh` | Automated API testing | `./test_request.sh` |
| `generate_test_data.py` | Create test datasets | `python generate_test_data.py moderate` |
| `test_main.py` | Unit tests (pytest) | `pytest test_main.py -v` |

---

### **Integration Code**

| File | Language | Purpose |
|------|----------|---------|
| `java_integration_example.java` | Java | Complete integration example with DTOs |

**For Java developers:** Copy relevant classes from this file.

---

### **Configuration Files**

| File | Purpose |
|------|---------|
| `.dockerignore` | Docker build optimization |
| `.gitignore` | Git version control |

---

## 🎯 Common Tasks

### **Task: Deploy Service**
**Files needed:** `docker-compose.yml`, `Dockerfile`, `main.py`, `requirements.txt`  
**Command:** `docker-compose up -d`  
**Verify:** `curl http://localhost:8001/health`

---

### **Task: Test Locally**
**Files needed:** `test_request.sh`, `generate_test_data.py`  
**Commands:**
```bash
python generate_test_data.py moderate
./test_request.sh
```

---

### **Task: Integrate with Java**
**Files needed:** `java_integration_example.java`, `INTEGRATION_GUIDE.md`  
**Steps:**
1. Read `INTEGRATION_GUIDE.md`
2. Copy code from `java_integration_example.java`
3. Update ViewModel and ZUL files
4. Configure `application.properties`

---

### **Task: Understand API**
**Files needed:** `README.md` (API section), Interactive docs at `/docs`  
**Command:** Open http://localhost:8001/docs in browser

---

### **Task: Run Unit Tests**
**Files needed:** `test_main.py`, `requirements.txt`  
**Command:** `pytest test_main.py -v`

---

### **Task: Generate Test Data**
**Files needed:** `generate_test_data.py`  
**Commands:**
```bash
# Single dataset
python generate_test_data.py moderate

# All datasets
python generate_test_data.py
```

---

## 📚 Documentation by Role

### **For Project Manager:**
- `DELIVERABLES.md` - What was delivered
- `QUICKSTART.md` - How to demo

### **For Backend Developer (Java):**
- `INTEGRATION_GUIDE.md` - Step-by-step integration
- `java_integration_example.java` - Code to copy
- `README.md` - API reference

### **For Data Scientist:**
- `main.py` - Fairness metric implementations
- `README.md` - Metrics explanation
- `generate_test_data.py` - Test data generation

### **For DevOps:**
- `Dockerfile` - Container image
- `docker-compose.yml` - Deployment config
- `README.md` - Deployment section

### **For QA Tester:**
- `test_request.sh` - Automated tests
- `test_main.py` - Unit tests
- `README.md` - Error codes reference

---

## 🔍 Finding Specific Information

### **"How do I deploy this?"**
→ `QUICKSTART.md` or `docker-compose.yml`

### **"What endpoints are available?"**
→ `README.md` (API Reference) or http://localhost:8001/docs

### **"How do I integrate with Java?"**
→ `INTEGRATION_GUIDE.md`

### **"What fairness metrics are calculated?"**
→ `README.md` (Features section) or `main.py` (functions)

### **"How do I test it?"**
→ `test_request.sh` or `test_main.py`

### **"What bias levels exist?"**
→ `README.md` (Bias Classification) or `DELIVERABLES.md`

### **"How do I generate test data?"**
→ `generate_test_data.py`

### **"What Python packages are needed?"**
→ `requirements.txt`

### **"How do I troubleshoot errors?"**
→ `README.md` (Error Handling) or `INTEGRATION_GUIDE.md` (Troubleshooting)

---

## 📊 File Statistics

```
Total Files:         15
Total Lines:         ~2,500+
Documentation:       ~1,500 lines
Code:                ~1,000 lines
Languages:           Python, Java, Shell, YAML, Markdown
```

---

## 🔄 Workflow Diagrams

### **Deployment Workflow**
```
1. Read DELIVERABLES.md (overview)
   ↓
2. Read QUICKSTART.md (setup)
   ↓
3. Run: docker-compose up -d
   ↓
4. Verify: curl localhost:8001/health
   ↓
5. Test: ./test_request.sh
   ↓
6. Done! ✅
```

### **Integration Workflow**
```
1. Deploy service (see above)
   ↓
2. Read INTEGRATION_GUIDE.md
   ↓
3. Copy code from java_integration_example.java
   ↓
4. Update ViewModel + ZUL
   ↓
5. Test end-to-end
   ↓
6. Done! ✅
```

---

## ⚡ Quick Command Reference

```bash
# Start service
docker-compose up -d

# Stop service
docker-compose down

# View logs
docker logs -f bias-detection-service

# Health check
curl http://localhost:8001/health

# Generate test data
python generate_test_data.py moderate

# Run automated tests
./test_request.sh

# Run unit tests
pytest test_main.py -v

# View API docs
open http://localhost:8001/docs  # Mac
xdg-open http://localhost:8001/docs  # Linux
start http://localhost:8001/docs  # Windows
```

---

## 📞 Need Help?

| Question | File to Check |
|----------|---------------|
| General overview | `DELIVERABLES.md` |
| Quick setup | `QUICKSTART.md` |
| Complete documentation | `README.md` |
| Java integration | `INTEGRATION_GUIDE.md` |
| API reference | `README.md` or `/docs` endpoint |
| Testing | `test_request.sh`, `test_main.py` |
| Code examples | `java_integration_example.java` |

---

**Updated:** October 30, 2025  
**Version:** 1.0.0  
**Status:** Production Ready ✅

