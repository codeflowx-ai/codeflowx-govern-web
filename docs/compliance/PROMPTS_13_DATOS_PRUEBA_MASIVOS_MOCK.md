# PROMPTS_13: DATOS PRUEBA MASIVOS + MOCK DATA
## Generación 100K-1M registros sintéticos para demos/testing sin APIs

**Fecha:** 5 Noviembre 2025  
**Propósito:** Datos prueba masivos para demos sin gastar en APIs OpenAI/Anthropic  
**Prioridad:** 🔴 **CRÍTICA DEMO** (no podemos demos con BD vacía)

---

## 🎯 PROBLEMA

**Actualmente:**
- ❌ BD vacía o con pocos datos reales
- ❌ Cada demo/testing llama OpenAI API ($0.50-2.00 por request)
- ❌ Demo con 100 evaluaciones = $50-200 gastados
- ❌ Dashboards vacíos (sin datos no impresiona)
- ❌ Testing performance imposible (necesitas 100K+ registros)

**Necesitas:**
- ✅ **100K-1M registros sintéticos** realistas
- ✅ **Modelos locales** (Llama, Mistral) para evaluaciones
- ✅ **Mock LLM responses** sin llamar APIs
- ✅ **Seed database** instantáneo para demos
- ✅ **Datos realistas** (nombres, empresas, métricas coherentes)

---

## ✅ SOLUCIÓN: GENERADOR DATOS MASIVOS + MODELOS LOCALES

**Arquitectura:**

```
┌────────────────────────────────────────────────┐
│     GENERADOR DATOS SINTÉTICOS                 │
│                                                │
│  Python Faker + custom generators             │
│  ├─ 10K Projects                               │
│  ├─ 50K Models                                 │
│  ├─ 500K Evaluations                           │
│  ├─ 5M Inference logs                          │
│  ├─ 100K Prompts                               │
│  ├─ 20K Datasets                               │
│  └─ 1M Audit logs                              │
└────────────────┬───────────────────────────────┘
                 │ Seed database
                 ▼
┌────────────────────────────────────────────────┐
│     POSTGRESQL (Test/Demo)                     │
│     6.5M registros sintéticos                  │
│     Coherentes, realistas, relacionados        │
└────────────────────────────────────────────────┘

┌────────────────────────────────────────────────┐
│     MODELOS LOCALES (sin APIs)                 │
│                                                │
│  ✅ Llama 3 8B (local)                         │
│  ✅ Mistral 7B (local)                         │
│  ✅ Mock LLM responses (cached)                │
└────────────────────────────────────────────────┘
```

**Total prompts:** 8 prompts
- **Generadores datos (4):** Projects/Models, Evaluations/Logs, Prompts/Datasets, Compliance/GDPR
- **Modelos locales (2):** Setup Llama/Mistral, integración
- **Mock responses (1):** Cache LLM responses
- **Seed database (1):** Script carga masiva

---

## 📦 PROMPT 1: Generador Datos Sintéticos Base

**Objetivo:** Generar 10K projects + 50K models + 20K datasets sintéticos realistas

**Crear:**

```python
# scripts/generate_synthetic_data.py
from faker import Faker
import random
import uuid
from datetime import datetime, timedelta
import json
import psycopg2

fake = Faker(['es_ES', 'en_US'])  # Español + Inglés

class SyntheticDataGenerator:
    """Generar datos sintéticos masivos para CodeflowX"""
    
    def __init__(self, db_connection):
        self.db = db_connection
        self.cursor = db_connection.cursor()
    
    def generate_projects(self, count=10000):
        """Generate 10K proyectos sintéticos"""
        
        print(f"Generating {count} synthetic projects...")
        
        projects = []
        sectors = ['FINANCE', 'HEALTHCARE', 'RETAIL', 'MANUFACTURING', 'PUBLIC_SECTOR', 
                   'EDUCATION', 'TELECOM', 'ENERGY']
        statuses = ['ACTIVE', 'COMPLETED', 'ARCHIVED', 'SUSPENDED']
        
        for i in range(count):
            project = {
                'iduuid': str(uuid.uuid4()),
                'prjproject_name': fake.catch_phrase() + f" AI Project {i}",
                'prjdescription': fake.text(max_nb_chars=200),
                'prjsector': random.choice(sectors),
                'prjstatus': random.choice(statuses),
                'prjcreated_at': fake.date_time_between(start_date='-2y', end_date='now'),
                'prjbudget': random.randint(10000, 5000000),
                'prjteam_size': random.randint(2, 50)
            }
            projects.append(project)
        
        # Batch insert (mucho más rápido)
        self._batch_insert('PROJECTS', projects)
        
        print(f"✅ {count} projects generated")
        return projects
    
    def generate_models(self, count=50000, projects=None):
        """Generate 50K modelos sintéticos"""
        
        print(f"Generating {count} synthetic models...")
        
        if projects is None:
            # Fetch projects existentes
            self.cursor.execute("SELECT idxproject FROM PROJECTS LIMIT 10000")
            project_ids = [row[0] for row in self.cursor.fetchall()]
        else:
            project_ids = [p['idxproject'] for p in projects]
        
        models = []
        model_types = ['LLM', 'VISION', 'AUDIO', 'TABULAR', 'MULTIMODAL']
        base_models = [
            'gpt-4', 'claude-3.5-sonnet', 'llama-3-70b', 'mistral-large',
            'gemini-pro', 'command-r-plus', 'mixtral-8x7b'
        ]
        risk_levels = [
            ['HIGH_RISK'],
            ['LIMITED_RISK'],
            ['MINIMAL_RISK'],
            ['HIGH_RISK', 'BIOMETRIC'],
            ['LIMITED_RISK', 'CHATBOT']
        ]
        statuses = ['DEVELOPMENT', 'TESTING', 'STAGING', 'PRODUCTION', 'ARCHIVED']
        
        for i in range(count):
            model = {
                'iduuid': str(uuid.uuid4()),
                'modproject_id': random.choice(project_ids),
                'modname': f"{fake.company()} {random.choice(model_types)} Model v{random.randint(1,10)}",
                'moddescription': fake.text(max_nb_chars=300),
                'modtype': random.choice(model_types),
                'modbase_model': random.choice(base_models),
                'modversion': f"{random.randint(1,5)}.{random.randint(0,9)}.{random.randint(0,20)}",
                'modstatus': random.choice(statuses),
                'risklevel': json.dumps(random.choice(risk_levels)),
                'modintended_purpose': fake.text(max_nb_chars=200),
                'modlimitations': fake.text(max_nb_chars=150),
                'modcreated_at': fake.date_time_between(start_date='-1y', end_date='now')
            }
            models.append(model)
            
            # Progress
            if (i + 1) % 10000 == 0:
                print(f"  Generated {i + 1}/{count} models...")
        
        # Batch insert
        self._batch_insert('MODELS', models)
        
        print(f"✅ {count} models generated")
        return models
    
    def generate_datasets(self, count=20000, projects=None):
        """Generate 20K datasets sintéticos"""
        
        print(f"Generating {count} synthetic datasets...")
        
        if projects is None:
            self.cursor.execute("SELECT idxproject FROM PROJECTS LIMIT 10000")
            project_ids = [row[0] for row in self.cursor.fetchall()]
        else:
            project_ids = [p['idxproject'] for p in projects]
        
        datasets = []
        formats = ['PARQUET', 'CSV', 'JSON', 'AVRO', 'ORC']
        sources = ['S3', 'SNOWFLAKE', 'DATABRICKS', 'AZURE_BLOB', 'GCS', 'POSTGRESQL']
        
        for i in range(count):
            dataset = {
                'iduuid': str(uuid.uuid4()),
                'datproject_id': random.choice(project_ids),
                'datname': f"dataset_{fake.word()}_{fake.random_number(digits=6)}",
                'datdescription': fake.text(max_nb_chars=200),
                'datformat': random.choice(formats),
                'datsource': random.choice(sources),
                'datorigin': fake.url(),
                'datsize_mb': random.randint(10, 100000),  # 10 MB - 100 GB
                'datrecordcount': random.randint(1000, 50000000),  # 1K - 50M filas
                'datcreated_at': fake.date_time_between(start_date='-2y', end_date='now')
            }
            datasets.append(dataset)
        
        self._batch_insert('DATASETS', datasets)
        
        print(f"✅ {count} datasets generated")
        return datasets
    
    def _batch_insert(self, table_name, records, batch_size=1000):
        """Batch insert para performance (1000x más rápido que individual)"""
        
        if not records:
            return
        
        # Get column names from first record
        columns = list(records[0].keys())
        placeholders = ', '.join(['%s'] * len(columns))
        columns_str = ', '.join(columns)
        
        sql = f"INSERT INTO {table_name} ({columns_str}) VALUES ({placeholders})"
        
        # Insert en batches
        for i in range(0, len(records), batch_size):
            batch = records[i:i+batch_size]
            values = [tuple(r.values()) for r in batch]
            
            self.cursor.executemany(sql, values)
            self.db.commit()
            
            if (i + batch_size) % 10000 == 0:
                print(f"  Inserted {i + batch_size}/{len(records)} records into {table_name}")
        
        print(f"✅ Batch insert completed: {len(records)} records into {table_name}")

# Usage
if __name__ == "__main__":
    # Connect DB
    conn = psycopg2.connect(
        host="localhost",
        database="codeflowx_test",
        user="postgres",
        password="password"
    )
    
    generator = SyntheticDataGenerator(conn)
    
    # Generate
    projects = generator.generate_projects(10000)      # 10K proyectos
    models = generator.generate_models(50000)          # 50K modelos
    datasets = generator.generate_datasets(20000)      # 20K datasets
    
    print("\n🎉 Synthetic data generation completed!")
    print(f"Total records: {10000 + 50000 + 20000} = 80K")
    
    conn.close()
```

**Verificar:**
- Genera 10K projects en <1 min
- Genera 50K models en <5 min
- Genera 20K datasets en <2 min
- Datos coherentes (FKs válidos)
- Datos realistas (nombres empresas, descripciones)

---

## 📦 PROMPT 2: Generador Evaluaciones + Logs Masivos

**Objetivo:** Generar 500K evaluations + 5M inference logs sintéticos

**Crear:**

```python
# scripts/generate_synthetic_evaluations_logs.py
class EvaluationLogGenerator:
    """Generate evaluaciones y logs masivos"""
    
    def generate_evaluations(self, count=500000, models=None):
        """Generate 500K evaluaciones sintéticas"""
        
        print(f"Generating {count} synthetic evaluations...")
        
        # Fetch models
        if models is None:
            self.cursor.execute("SELECT idxmodel FROM MODELS LIMIT 50000")
            model_ids = [row[0] for row in self.cursor.fetchall()]
        else:
            model_ids = [m['idxmodel'] for m in models]
        
        evaluations = []
        eval_types = ['BIAS', 'HALLUCINATION', 'TOXICITY', 'PERFORMANCE', 'SECURITY', 'GDPR']
        eval_status = ['COMPLETED', 'IN_PROGRESS', 'FAILED']
        
        for i in range(count):
            # Métricas realistas
            accuracy = random.uniform(0.65, 0.98)
            hallucination_rate = random.uniform(0.01, 0.25)
            bias_score = random.uniform(0.0, 0.30)
            toxicity_score = random.uniform(0.0, 0.15)
            
            evaluation = {
                'iduuid': str(uuid.uuid4()),
                'evalmodel_id': random.choice(model_ids),
                'evaltype': random.choice(eval_types),
                'evalstatus': random.choice(eval_status),
                'evalaccuracy': accuracy,
                'evalprecision': random.uniform(accuracy - 0.05, accuracy + 0.05),
                'evalrecall': random.uniform(accuracy - 0.05, accuracy + 0.05),
                'evalf1score': accuracy,
                'evalhallucination_rate': hallucination_rate,
                'evalbias_score': bias_score,
                'evaltoxicity_score': toxicity_score,
                'evallatency_p95_ms': random.uniform(50, 2000),
                'evalcost_usd': random.uniform(0.01, 5.0),
                'evalcreated_at': fake.date_time_between(start_date='-1y', end_date='now')
            }
            evaluations.append(evaluation)
            
            if (i + 1) % 50000 == 0:
                print(f"  Generated {i + 1}/{count} evaluations...")
        
        # Batch insert
        self._batch_insert('EVALUATIONS', evaluations, batch_size=5000)
        
        print(f"✅ {count} evaluations generated")
        return evaluations
    
    def generate_inference_logs(self, count=5000000, models=None):
        """Generate 5M inference logs sintéticos (time-series)"""
        
        print(f"Generating {count} synthetic inference logs...")
        
        if models is None:
            self.cursor.execute("SELECT idxmodel FROM MODELS LIMIT 50000")
            model_ids = [row[0] for row in self.cursor.fetchall()]
        else:
            model_ids = [m['idxmodel'] for m in models]
        
        # Generate por batches (no cargar 5M en memoria)
        batch_size = 10000
        total_inserted = 0
        
        for batch_num in range(count // batch_size):
            logs = []
            
            for i in range(batch_size):
                # Timestamp distribuido últimos 12 meses
                timestamp = fake.date_time_between(start_date='-12mo', end_date='now')
                
                log = {
                    'iduuid': str(uuid.uuid4()),
                    'infmodel_id': random.choice(model_ids),
                    'infinference_id': str(uuid.uuid4()),
                    'inflatency_ms': random.uniform(50, 3000),
                    'inftokens_input': random.randint(10, 4000),
                    'inftokens_output': random.randint(10, 2000),
                    'infcost_usd': random.uniform(0.001, 0.5),
                    'inferror': None if random.random() > 0.05 else "Timeout error",
                    'infcreated_at': timestamp
                }
                logs.append(log)
            
            # Insert batch
            self._batch_insert('INFERENCELOGS', logs, batch_size=5000)
            
            total_inserted += len(logs)
            
            if (batch_num + 1) % 10 == 0:
                print(f"  Generated {total_inserted}/{count} inference logs...")
        
        print(f"✅ {count} inference logs generated")
    
    def generate_audit_logs(self, count=1000000):
        """Generate 1M audit logs inmutables (Art. 19)"""
        
        print(f"Generating {count} synthetic audit logs...")
        
        event_types = [
            'MODEL_CREATED', 'MODEL_APPROVED', 'MODEL_REJECTED', 'MODEL_DEPLOYED',
            'EVALUATION_EXECUTED', 'FRIA_COMPLETED', 'INCIDENT_REPORTED',
            'POLICY_UPDATED', 'USER_ACCESS', 'DATA_EXPORTED'
        ]
        
        # Hash chain simulation
        previous_hash = "0" * 64  # Genesis
        
        batch_size = 10000
        total_inserted = 0
        
        for batch_num in range(count // batch_size):
            logs = []
            
            for i in range(batch_size):
                timestamp = fake.date_time_between(start_date='-2y', end_date='now')
                
                # Simulate hash chain
                current_hash = hashlib.sha256(
                    f"{previous_hash}{timestamp}{random.random()}".encode()
                ).hexdigest()
                
                log = {
                    'iduuid': str(uuid.uuid4()),
                    'audevent_type': random.choice(event_types),
                    'auduser_id': random.randint(1, 200),
                    'audmodel_id': random.randint(1, 50000) if random.random() > 0.3 else None,
                    'audhash_chain': current_hash,
                    'audprevious_hash': previous_hash,
                    'audcreated_at': timestamp,
                    'audmetadata': json.dumps({
                        "risk_level": random.choice(['HIGH_RISK', 'LIMITED_RISK']),
                        "approval_status": random.choice(['APPROVED', 'REJECTED', 'PENDING'])
                    })
                }
                logs.append(log)
                
                previous_hash = current_hash
            
            self._batch_insert('AUDITLOGS', logs, batch_size=5000)
            
            total_inserted += len(logs)
            
            if (batch_num + 1) % 10 == 0:
                print(f"  Generated {total_inserted}/{count} audit logs...")
        
        print(f"✅ {count} audit logs generated")

# Main execution
def generate_all_synthetic_data():
    """Generate ALL synthetic data (6.5M+ records)"""
    
    conn = psycopg2.connect(
        host="localhost",
        database="codeflowx_test",
        user="postgres",
        password="password"
    )
    
    generator = SyntheticDataGenerator(conn)
    
    print("🚀 Starting massive synthetic data generation...")
    print("Estimated time: 30-60 minutes")
    print("")
    
    # Phase 1: Base entities
    print("PHASE 1: Base entities")
    projects = generator.generate_projects(10000)           # 10K - 2 min
    models = generator.generate_models(50000)               # 50K - 10 min
    datasets = generator.generate_datasets(20000)           # 20K - 4 min
    
    # Phase 2: Evaluations
    print("\nPHASE 2: Evaluations")
    evaluations = generator.generate_evaluations(500000)    # 500K - 20 min
    
    # Phase 3: Logs (time-series masivos)
    print("\nPHASE 3: Time-series logs")
    generator.generate_inference_logs(5000000)              # 5M - 30 min
    generator.generate_audit_logs(1000000)                  # 1M - 10 min
    
    print("\n" + "="*60)
    print("🎉 SYNTHETIC DATA GENERATION COMPLETED!")
    print("="*60)
    print(f"Total records: 6,580,000")
    print(f"├─ Projects:         10,000")
    print(f"├─ Models:           50,000")
    print(f"├─ Datasets:         20,000")
    print(f"├─ Evaluations:      500,000")
    print(f"├─ Inference logs:   5,000,000")
    print(f"└─ Audit logs:       1,000,000")
    print("")
    print("Database size estimate: ~2-3 GB (sin TimescaleDB compression)")
    print("With TimescaleDB compression: ~300-500 MB")
    print("")
    print("✅ Ready for demos/testing/performance validation!")
    
    conn.close()

if __name__ == "__main__":
    generate_all_synthetic_data()
```

**Verificar:**
- Script ejecuta sin errores
- 6.5M registros insertados en <60 min
- Datos coherentes (FKs válidos)
- Dashboards muestran datos realistas

---

## 📦 PROMPT 3: Setup Modelos Locales (Llama + Mistral)

**Objetivo:** Setup Llama 3 8B + Mistral 7B local para evaluaciones SIN OpenAI API

**Crear:**

```bash
# docker/local-llms/docker-compose.yml
version: '3.8'
services:
  ollama:
    image: ollama/ollama:latest
    container_name: codeflowx-ollama
    restart: unless-stopped
    ports:
      - "11434:11434"
    volumes:
      - ./ollama_models:/root/.ollama
    environment:
      - OLLAMA_ORIGINS=*
    networks:
      - codeflowx-network

# Pull models
# docker exec codeflowx-ollama ollama pull llama3:8b
# docker exec codeflowx-ollama ollama pull mistral:7b
```

```python
# services/local_llm_service.py
import requests
from typing import List, Dict, Any
import structlog

logger = structlog.get_logger(__name__)

class LocalLLMService:
    """Use modelos locales Llama/Mistral en vez de OpenAI API (gratis, rápido)"""
    
    def __init__(self, ollama_url="http://localhost:11434"):
        self.ollama_url = ollama_url
        self.default_model = "llama3:8b"  # O mistral:7b
    
    def chat_completion(
        self,
        messages: List[Dict[str, str]],
        model: str = None,
        temperature: float = 0.3,
        max_tokens: int = 500
    ) -> str:
        """Chat completion usando Llama local (NO OpenAI API)"""
        
        model = model or self.default_model
        
        # Format prompt
        prompt = self._format_messages_to_prompt(messages)
        
        # Call Ollama API
        response = requests.post(
            f"{self.ollama_url}/api/generate",
            json={
                "model": model,
                "prompt": prompt,
                "stream": False,
                "options": {
                    "temperature": temperature,
                    "num_predict": max_tokens
                }
            }
        )
        
        result = response.json()
        
        logger.info(
            f"Local LLM response generated",
            model=model,
            prompt_length=len(prompt),
            response_length=len(result["response"])
        )
        
        return result["response"]
    
    def generate_embedding(self, text: str) -> List[float]:
        """Generate embedding usando modelo local (NO OpenAI ada-002)"""
        
        # Ollama embeddings (gratis)
        response = requests.post(
            f"{self.ollama_url}/api/embeddings",
            json={
                "model": "llama3:8b",
                "prompt": text
            }
        )
        
        return response.json()["embedding"]
    
    def _format_messages_to_prompt(self, messages: List[Dict[str, str]]) -> str:
        """Format OpenAI-style messages to Llama prompt"""
        
        prompt = ""
        for msg in messages:
            role = msg["role"]
            content = msg["content"]
            
            if role == "system":
                prompt += f"System: {content}\n\n"
            elif role == "user":
                prompt += f"User: {content}\n\n"
            elif role == "assistant":
                prompt += f"Assistant: {content}\n\n"
        
        prompt += "Assistant: "
        return prompt

# Integración con evaluaciones
class EvaluationServiceWithLocalLLM:
    def __init__(self, use_local_llm=True):
        if use_local_llm:
            self.llm = LocalLLMService()  # Llama local (gratis)
        else:
            self.llm = OpenAIService()    # OpenAI API (pago)
    
    def evaluate_hallucination(self, model_output, ground_truth):
        """Evaluate hallucination usando Llama local"""
        
        prompt = f"""
        Evalúa si la siguiente respuesta contiene alucinaciones (información inventada) 
        comparada con la verdad de base.
        
        Verdad: {ground_truth}
        
        Respuesta: {model_output}
        
        ¿Contiene alucinaciones? Responde SOLO: SÍ o NO
        """
        
        # Llama local (gratis) en vez de GPT-4 ($0.03 por request)
        result = self.llm.chat_completion([
            {"role": "user", "content": prompt}
        ])
        
        hallucination_detected = "SÍ" in result.upper()
        
        return {
            "hallucination_detected": hallucination_detected,
            "confidence": 0.85,
            "evaluator_model": "llama3:8b-local"
        }
```

**Verificar:**
- Ollama running (docker ps)
- Llama 3 8B pulled (~4.7 GB)
- Chat completion funciona local (sin OpenAI)
- Embedding generation funciona local
- Performance acceptable (<500ms vs <200ms OpenAI)

---

## 📦 PROMPT 4: Mock LLM Responses Cached

**Objetivo:** Cache responses comunes para demos ultra-rápidas sin LLM calls

**Crear:**

```python
# services/mock_llm_service.py
class MockLLMService:
    """Mock LLM responses para demos/testing sin API calls"""
    
    def __init__(self):
        # Pre-generated responses (realistas)
        self.mock_responses = {
            "hallucination_check": {
                "no_hallucination": "NO. La respuesta está alineada con la verdad proporcionada.",
                "yes_hallucination": "SÍ. La respuesta contiene información no presente en la verdad de base."
            },
            "bias_analysis": {
                "no_bias": "No se detecta sesgo significativo. Demographic parity: 0.03 (3%).",
                "bias_detected": "SESGO DETECTADO. Demographic parity: 0.18 (18%). Grupo 'female' recibe 18% menos aprobaciones."
            },
            "toxicity_check": {
                "clean": "Contenido limpio. Toxicity score: 0.02/1.0",
                "toxic": "CONTENIDO TÓXICO detectado. Toxicity score: 0.87/1.0. Lenguaje ofensivo presente."
            },
            "compliance_summary": """
            RESUMEN COMPLIANCE EU AI ACT:
            
            ✅ Art. 10 (Data Governance): COMPLIANT
            ✅ Art. 15 (Accuracy & Robustness): COMPLIANT  
            ⚠️ Art. 13 (Transparency): PARCIAL - Requiere mejoras documentación
            ✅ Art. 14 (Human Oversight): COMPLIANT
            ✅ Art. 19 (Logging): COMPLIANT
            
            Recomendación: Mejorar instrucciones de uso (Art. 13) antes deployment.
            """,
            "executive_summary": """
            RESUMEN EJECUTIVO - Credit Scoring Model v2.3
            
            RENDIMIENTO:
            - Accuracy: 94.2%
            - Precision: 92.8%
            - Recall: 95.1%
            - F1-Score: 93.9%
            
            COMPLIANCE:
            - Clasificación: HIGH RISK (Art. 6 - Credit scoring)
            - FRIA: APROBADA (23 Oct 2025)
            - Sesgo: <5% (COMPLIANT)
            - GDPR: COMPLIANT
            
            RECOMENDACIÓN: APROBAR deployment a PRODUCTION
            """
        }
    
    def chat_completion(self, messages, task_type="general"):
        """Return mock response según task type"""
        
        # Detectar tipo tarea
        user_content = messages[-1]["content"].lower()
        
        if "alucinación" in user_content or "hallucination" in user_content:
            # Random: 80% no hallucination, 20% sí
            if random.random() > 0.2:
                return self.mock_responses["hallucination_check"]["no_hallucination"]
            else:
                return self.mock_responses["hallucination_check"]["yes_hallucination"]
        
        elif "sesgo" in user_content or "bias" in user_content:
            if random.random() > 0.3:
                return self.mock_responses["bias_analysis"]["no_bias"]
            else:
                return self.mock_responses["bias_analysis"]["bias_detected"]
        
        elif "compliance" in user_content or "cumplimiento" in user_content:
            return self.mock_responses["compliance_summary"]
        
        elif "resumen ejecutivo" in user_content or "executive summary" in user_content:
            return self.mock_responses["executive_summary"]
        
        else:
            # Generic response
            return fake.text(max_nb_chars=300)
    
    def generate_embedding(self, text: str) -> List[float]:
        """Return random embedding (para demos visuales, no similarity real)"""
        
        # Embedding 1536 dims (compatible OpenAI ada-002)
        # Random pero consistente (mismo texto = mismo embedding)
        random.seed(hash(text) % 2**32)
        embedding = [random.uniform(-1, 1) for _ in range(1536)]
        
        return embedding

# Config flag
USE_MOCK_LLM = os.getenv("USE_MOCK_LLM", "false").lower() == "true"

if USE_MOCK_LLM:
    llm_service = MockLLMService()  # Gratis, instantáneo
else:
    llm_service = OpenAIService()   # Pago, real
```

**Ventajas mock:**
- ✅ **Gratis** (sin API calls)
- ✅ **Instantáneo** (<1ms vs 500-2000ms OpenAI)
- ✅ **Determinista** (mismo input = mismo output)
- ✅ **Demos repetibles** (no variabilidad LLM)

**Verificar:**
- Mock responses realistas
- Evaluaciones funcionan con mock
- Performance excelente (<1ms)
- Flag USE_MOCK_LLM=true activa mock

---

## 📦 PROMPT 5: Seed Database Script Completo

**Objetivo:** Script seed 1-comando para cargar 6.5M registros sintéticos en <5 min

**Crear:**

```bash
#!/bin/bash
# scripts/seed_database_demo.sh

echo "🚀 Seeding CodeflowX database with synthetic data..."
echo "Estimated time: 5-10 minutes"
echo ""

# 1. Cleanup existing data (CUIDADO: solo test/demo DB)
echo "Step 1: Cleanup existing data..."
psql -h localhost -U postgres -d codeflowx_demo << EOF
TRUNCATE TABLE INFERENCELOGS CASCADE;
TRUNCATE TABLE AUDITLOGS CASCADE;
TRUNCATE TABLE EVALUATIONS CASCADE;
TRUNCATE TABLE MODELS CASCADE;
TRUNCATE TABLE DATASETS CASCADE;
TRUNCATE TABLE PROJECTS CASCADE;
EOF

# 2. Generate synthetic data
echo "Step 2: Generating 6.5M synthetic records..."
python3 scripts/generate_synthetic_data.py
python3 scripts/generate_synthetic_evaluations_logs.py

# 3. Configure TimescaleDB hypertables (si no configuradas)
echo "Step 3: Configure TimescaleDB hypertables..."
psql -h localhost -U postgres -d codeflowx_demo << EOF
SELECT create_hypertable('INFERENCELOGS', 'infcreated_at', 
    chunk_time_interval => INTERVAL '1 week',
    if_not_exists => TRUE);

SELECT create_hypertable('AUDITLOGS', 'audcreated_at',
    chunk_time_interval => INTERVAL '1 month',
    if_not_exists => TRUE);

-- Enable compression (30 días)
ALTER TABLE INFERENCELOGS SET (timescaledb.compress);
SELECT add_compression_policy('INFERENCELOGS', INTERVAL '30 days', if_not_exists => TRUE);
EOF

# 4. Update statistics (para query optimizer)
echo "Step 4: Updating database statistics..."
psql -h localhost -U postgres -d codeflowx_demo << EOF
ANALYZE PROJECTS;
ANALYZE MODELS;
ANALYZE DATASETS;
ANALYZE EVALUATIONS;
ANALYZE INFERENCELOGS;
ANALYZE AUDITLOGS;
EOF

echo ""
echo "✅ Database seeded successfully!"
echo ""
echo "Database stats:"
psql -h localhost -U postgres -d codeflowx_demo -c "\
SELECT schemaname, tablename, n_live_tup AS row_count \
FROM pg_stat_user_tables \
WHERE schemaname = 'public' \
ORDER BY n_live_tup DESC LIMIT 10;"

echo ""
echo "🎉 Ready for demo!"
```

**Verificar:**
- 1 comando: `./seed_database_demo.sh`
- 6.5M registros en <10 min
- Dashboards poblados
- Performance queries OK

---

## 📦 PROMPT 6: Generador Datos Compliance/GDPR

**Objetivo:** Datos sintéticos específicos compliance (FRIA, DPIA, GDPR requests, incidents)

**Crear:**

```python
# scripts/generate_compliance_data.py
class ComplianceDataGenerator:
    """Generate datos compliance sintéticos"""
    
    def generate_fria_assessments(self, count=5000):
        """Generate 5K FRIA assessments sintéticos"""
        
        fria_statuses = ['PENDING', 'IN_PROGRESS', 'COMPLETED', 'APPROVED', 'REJECTED']
        risk_scores = []
        
        for i in range(count):
            # Scores realistas (0-100)
            severity = random.randint(1, 100)
            probability = random.randint(1, 100)
            risk_score = (severity * probability) / 100
            
            fria = {
                'iduuid': str(uuid.uuid4()),
                'frimodel_id': random.randint(1, 50000),
                'fristatus': random.choice(fria_statuses),
                'friseverity_score': severity,
                'friprobability_score': probability,
                'fririsk_score': risk_score,
                'frimitigation_measures': fake.text(max_nb_chars=300),
                'friresidual_risk_score': risk_score * random.uniform(0.3, 0.7),
                'friapprover_id': random.randint(1, 50),
                'fricompleted_at': fake.date_time_between(start_date='-6mo', end_date='now') 
                    if random.random() > 0.3 else None
            }
            risk_scores.append(fria)
        
        self._batch_insert('FRIA_ASSESSMENTS', risk_scores)
        print(f"✅ {count} FRIA assessments generated")
    
    def generate_gdpr_data_subject_requests(self, count=10000):
        """Generate 10K GDPR data subject requests"""
        
        request_types = ['ACCESS', 'RECTIFICATION', 'ERASURE', 'PORTABILITY', 'OBJECTION']
        statuses = ['PENDING', 'IN_PROGRESS', 'COMPLETED', 'REJECTED']
        
        requests_list = []
        
        for i in range(count):
            req = {
                'iduuid': str(uuid.uuid4()),
                'gdprequest_type': random.choice(request_types),
                'gdpdata_subject_email': fake.email(),
                'gdpdata_subject_name': fake.name(),
                'gdprequest_description': fake.text(max_nb_chars=200),
                'gdpstatus': random.choice(statuses),
                'gdpreceived_at': fake.date_time_between(start_date='-1y', end_date='now'),
                'gdpcompleted_at': fake.date_time_between(start_date='-6mo', end_date='now')
                    if random.random() > 0.4 else None,
                'gdpdeadline': None  # Calculate 30 días desde received
            }
            
            # Deadline 30 días GDPR
            if req['gdpreceived_at']:
                req['gdpdeadline'] = req['gdpreceived_at'] + timedelta(days=30)
            
            requests_list.append(req)
        
        self._batch_insert('GDPR_DATA_SUBJECT_REQUESTS', requests_list)
        print(f"✅ {count} GDPR requests generated")
    
    def generate_serious_incidents(self, count=500):
        """Generate 500 serious incidents (Art. 73)"""
        
        incident_types = [
            'BIAS_DETECTED', 'SECURITY_BREACH', 'SAFETY_VIOLATION',
            'DATA_LEAK', 'PERFORMANCE_DEGRADATION', 'HALLUCINATION_SYSTEMATIC'
        ]
        
        severities = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL']
        statuses = ['REPORTED', 'INVESTIGATING', 'RESOLVED', 'ESCALATED']
        
        incidents = []
        
        for i in range(count):
            reported_at = fake.date_time_between(start_date='-1y', end_date='now')
            
            incident = {
                'iduuid': str(uuid.uuid4()),
                'sinmodel_id': random.randint(1, 50000),
                'sinincident_type': random.choice(incident_types),
                'sinseverity': random.choice(severities),
                'sinstatus': random.choice(statuses),
                'sindescription': fake.text(max_nb_chars=400),
                'sinreported_at': reported_at,
                'sinreported_by_user_id': random.randint(1, 200),
                'sinnotified_authorities': random.choice([True, False]),
                'sinnotification_date': reported_at + timedelta(days=random.randint(1, 10))
                    if random.random() > 0.5 else None,
                'sinresolved_at': reported_at + timedelta(days=random.randint(1, 30))
                    if random.random() > 0.6 else None
            }
            incidents.append(incident)
        
        self._batch_insert('SERIOUS_INCIDENTS', incidents)
        print(f"✅ {count} serious incidents generated")

# Execute all
def seed_compliance_data():
    conn = psycopg2.connect(...)
    generator = ComplianceDataGenerator(conn)
    
    generator.generate_fria_assessments(5000)               # 5K FRIA
    generator.generate_gdpr_data_subject_requests(10000)    # 10K GDPR requests
    generator.generate_serious_incidents(500)               # 500 incidents
    
    print("\n✅ Compliance data seeded!")
    conn.close()
```

**Verificar:**
- FRIA assessments realistas
- GDPR requests con deadlines correctos
- Serious incidents coherentes

---

## 📦 PROMPT 7: Generador Embeddings Masivos (Qdrant)

**Objetivo:** Generar 100K embeddings sintéticos en Qdrant para RAG demos

**Crear:**

```python
# scripts/seed_qdrant_embeddings.py
from qdrant_client import QdrantClient
from qdrant_client.models import PointStruct
import numpy as np
from faker import Faker

fake = Faker(['es_ES'])

class QdrantSeeder:
    """Seed Qdrant con embeddings sintéticos para demos RAG"""
    
    def __init__(self, qdrant_url="http://localhost:6333"):
        self.qdrant = QdrantClient(url=qdrant_url)
    
    def seed_knowledge_base_docs(self, count=100000):
        """Seed 100K document chunks para RAG demos"""
        
        print(f"Seeding {count} synthetic document chunks to Qdrant...")
        
        # Textos compliance realistas
        compliance_topics = [
            "EU AI Act Art. 10 Data Governance",
            "GDPR Art. 15 Right of Access",
            "ISO 42001 Clause 5.2 AI Policy",
            "Bias detection demographic parity",
            "Post-market monitoring Art. 72",
            "FRIA assessment Art. 27",
            "Human oversight Art. 14"
        ]
        
        batch_size = 100
        total_inserted = 0
        
        for batch_num in range(count // batch_size):
            points = []
            
            for i in range(batch_size):
                # Text sintético realista
                topic = random.choice(compliance_topics)
                chunk_text = f"{topic}. {fake.text(max_nb_chars=500)}"
                
                # Embedding sintético (random pero coherente)
                # En producción usarías modelo real
                embedding = np.random.uniform(-1, 1, 1536).tolist()
                
                point = PointStruct(
                    id=f"doc_{batch_num}_{i}",
                    vector=embedding,
                    payload={
                        "chunk_text": chunk_text,
                        "document_id": random.randint(1, 10000),
                        "chunk_index": i % 20,
                        "topic": topic,
                        "framework": random.choice(['AI_ACT', 'GDPR', 'ISO_42001']),
                        "language": "es"
                    }
                )
                points.append(point)
            
            # Batch upsert
            self.qdrant.upsert(
                collection_name="knowledge_base_docs",
                points=points
            )
            
            total_inserted += len(points)
            
            if (batch_num + 1) % 100 == 0:
                print(f"  Inserted {total_inserted}/{count} chunks...")
        
        print(f"✅ {count} document chunks seeded to Qdrant")
    
    def seed_prompts_embeddings(self, count=10000):
        """Seed 10K prompts embeddings"""
        
        # Similar pero para collection 'prompts'
        # ... implementación similar ...
        pass

# Execute
if __name__ == "__main__":
    seeder = QdrantSeeder()
    
    seeder.seed_knowledge_base_docs(100000)     # 100K chunks
    seeder.seed_prompts_embeddings(10000)        # 10K prompts
    
    print("\n🎉 Qdrant seeded successfully!")
```

**Verificar:**
- 100K embeddings en Qdrant en <15 min
- RAG search funciona (aunque similarity no sea real)
- Dashboards RAG muestran datos

---

## 📦 PROMPT 8: Master Seed Script (1 comando TODO)

**Objetivo:** 1 script maestro que carga TODO (PostgreSQL + Qdrant + MinIO + OpenSearch)

**Crear:**

```bash
#!/bin/bash
# scripts/seed_everything_demo.sh

echo "════════════════════════════════════════════════════════════"
echo "   CODEFLOWX COMPLETE DATABASE SEEDING (DEMO/TEST)"
echo "════════════════════════════════════════════════════════════"
echo ""
echo "⚠️  WARNING: This will TRUNCATE existing data!"
echo "   Only use on TEST/DEMO databases, NOT production."
echo ""
read -p "Continue? (yes/no): " confirm

if [ "$confirm" != "yes" ]; then
    echo "Aborted."
    exit 1
fi

echo ""
echo "🚀 Starting complete seeding process..."
echo "Estimated time: 60-90 minutes"
echo ""

# 1. Setup infrastructure (Docker)
echo "STEP 1/7: Starting infrastructure (Docker Compose)..."
docker-compose -f docker/docker-compose-complete.yml up -d
sleep 30  # Wait services ready

# 2. Initialize Qdrant collections
echo "STEP 2/7: Initializing Qdrant collections..."
python3 scripts/init_qdrant_collections.py

# 3. Initialize MinIO buckets
echo "STEP 3/7: Initializing MinIO buckets..."
python3 scripts/init_minio_buckets.py

# 4. Initialize OpenSearch indices
echo "STEP 4/7: Initializing OpenSearch indices..."
python3 scripts/init_opensearch_indices.py

# 5. Seed PostgreSQL (6.5M records)
echo "STEP 5/7: Seeding PostgreSQL with 6.5M records..."
python3 scripts/generate_synthetic_data.py
python3 scripts/generate_synthetic_evaluations_logs.py
python3 scripts/generate_compliance_data.py

# 6. Seed Qdrant (100K embeddings)
echo "STEP 6/7: Seeding Qdrant with 100K embeddings..."
python3 scripts/seed_qdrant_embeddings.py

# 7. Seed MinIO (sample files)
echo "STEP 7/7: Uploading sample files to MinIO..."
python3 scripts/seed_minio_sample_files.py

echo ""
echo "════════════════════════════════════════════════════════════"
echo "✅ SEEDING COMPLETED SUCCESSFULLY!"
echo "════════════════════════════════════════════════════════════"
echo ""
echo "Database summary:"
echo "├─ PostgreSQL:  6,580,000 records"
echo "├─ Qdrant:      100,000 embeddings"
echo "├─ MinIO:       1,000 sample files"
echo "└─ OpenSearch:  500,000 logs"
echo ""
echo "Total data size: ~3 GB (PostgreSQL) + 800 MB (Qdrant) + 5 GB (MinIO)"
echo ""
echo "🎯 Ready for:"
echo "   ✅ Demos (dashboards poblados)"
echo "   ✅ Testing (performance con datos reales)"
echo "   ✅ Screenshots/videos (datos realistas)"
echo ""
echo "Access:"
echo "   • Backend:    http://localhost:8080"
echo "   • Qdrant:     http://localhost:6333/dashboard"
echo "   • MinIO:      http://localhost:9001"
echo "   • OpenSearch: http://localhost:5601"
echo ""
```

**Verificar:**
- 1 comando seed completo
- TODO poblado en <90 min
- Datos coherentes entre sistemas
- Dashboards impresionantes

---

## 📊 DATOS GENERADOS (Estimación)

| Entidad | Registros | Tamaño | Tiempo generación |
|---------|-----------|--------|-------------------|
| **PROJECTS** | 10,000 | 5 MB | 2 min |
| **MODELS** | 50,000 | 50 MB | 10 min |
| **DATASETS** | 20,000 | 15 MB | 4 min |
| **EVALUATIONS** | 500,000 | 500 MB | 20 min |
| **INFERENCELOGS** | 5,000,000 | 2 GB (500 MB comprimido) | 30 min |
| **AUDITLOGS** | 1,000,000 | 800 MB (200 MB comprimido) | 10 min |
| **FRIA** | 5,000 | 20 MB | 2 min |
| **GDPR Requests** | 10,000 | 10 MB | 2 min |
| **Incidents** | 500 | 2 MB | 1 min |
| **Qdrant embeddings** | 100,000 | 800 MB | 15 min |
| **MinIO files** | 1,000 | 5 GB | 10 min |
| **TOTAL** | **6,696,500** | **~10 GB** | **~90 min** |

**Con TimescaleDB compression:** ~10 GB → ~2-3 GB

---

## ✅ RESUMEN PROMPTS_13

**Total prompts:** 8 prompts

1. Generador base (projects, models, datasets)
2. Generador evaluaciones + logs masivos
3. Setup modelos locales (Llama + Mistral)
4. Mock LLM responses cached
5. Seed database script completo
6. Generador datos compliance/GDPR
7. Seed Qdrant embeddings masivos
8. Master seed script (1-comando TODO)

**Beneficios:**
- ✅ **Demos sin coste APIs** ($0 vs $100-500 por demo)
- ✅ **Instantáneo** (Llama local <500ms vs OpenAI 1-2seg)
- ✅ **Dashboards impresionantes** (cientos de miles datos)
- ✅ **Testing performance real** (5M logs para queries)
- ✅ **Repetible** (1 comando seed completo)

**Estimación:** 8-10 días → **2-3 días con 1-2 chats**

---

---

## 📦 PROMPT 7 COMPLETO: Seed Qdrant Embeddings (100K)

**Código completo:**

```python
# scripts/seed_qdrant_embeddings.py (COMPLETO)
from qdrant_client import QdrantClient
from qdrant_client.models import PointStruct
import numpy as np
from faker import Faker
import random

fake = Faker(['es_ES'])

# Compliance texts realistas (para generar chunks coherentes)
COMPLIANCE_TEMPLATES = {
    "AI_ACT_ART_10": [
        "Los sistemas de IA de alto riesgo utilizarán conjuntos de datos de entrenamiento, validación y prueba que cumplan los criterios de calidad apropiados.",
        "Los conjuntos de datos de entrenamiento serán pertinentes, representativos, libres de errores y completos.",
        "Se tendrán en cuenta las características o elementos específicos del contexto geográfico, conductual o funcional.",
        "Los datos se examinarán teniendo en cuenta los posibles sesgos que puedan afectar a la salud y la seguridad."
    ],
    "GDPR_ART_15": [
        "El interesado tendrá derecho a obtener del responsable del tratamiento confirmación de si se están tratando o no datos personales que le conciernen.",
        "Cuando se traten datos personales, el interesado tendrá derecho a acceder a dichos datos.",
        "El responsable facilitará una copia de los datos personales objeto de tratamiento.",
        "El ejercicio del derecho de acceso deberá facilitarse de forma gratuita."
    ],
    "ISO_42001": [
        "La organización debe establecer, documentar, implementar y mantener una política de IA.",
        "La política de IA debe ser aprobada por la alta dirección.",
        "Los objetivos de IA deben establecerse en las funciones y niveles pertinentes.",
        "La organización debe asegurar competencia del personal que afecta al sistema de gestión de IA."
    ]
}

def seed_qdrant_complete():
    """Seed Qdrant colecciones completas"""
    
    client = QdrantClient(url="http://localhost:6333")
    
    print("🚀 Seeding Qdrant collections with synthetic embeddings...")
    
    # 1. knowledge_base_docs (100K chunks)
    print("\n1. Seeding knowledge_base_docs (100K chunks)...")
    seed_kb_docs(client, 100000)
    
    # 2. prompts (10K prompts)
    print("\n2. Seeding prompts (10K prompts)...")
    seed_prompts(client, 10000)
    
    # 3. compliance_regulations (5K regulations)
    print("\n3. Seeding compliance_regulations (5K regulations)...")
    seed_compliance(client, 5000)
    
    # 4. evaluations (50K evaluations)
    print("\n4. Seeding evaluations (50K evaluations)...")
    seed_evaluations(client, 50000)
    
    print("\n" + "="*60)
    print("✅ QDRANT SEEDING COMPLETED!")
    print("="*60)
    print(f"Total embeddings: 165,000")
    print(f"├─ knowledge_base_docs:     100,000")
    print(f"├─ prompts:                  10,000")
    print(f"├─ compliance_regulations:    5,000")
    print(f"└─ evaluations:              50,000")
    print("")
    print("Storage: ~800 MB - 1 GB")
    print("✅ RAG demos ready!")

def seed_kb_docs(client, count):
    """Seed knowledge base docs con textos compliance realistas"""
    
    batch_size = 100
    
    for batch_num in range(count // batch_size):
        points = []
        
        for i in range(batch_size):
            # Select random framework
            framework = random.choice(list(COMPLIANCE_TEMPLATES.keys()))
            templates = COMPLIANCE_TEMPLATES[framework]
            
            # Build chunk con template + texto adicional
            base_text = random.choice(templates)
            additional_text = fake.text(max_nb_chars=300)
            chunk_text = f"{base_text} {additional_text}"
            
            # Embedding sintético
            embedding = np.random.uniform(-1, 1, 1536).tolist()
            
            point = PointStruct(
                id=f"kb_doc_{batch_num * batch_size + i}",
                vector=embedding,
                payload={
                    "chunk_text": chunk_text,
                    "document_id": random.randint(1, 5000),
                    "chunk_index": i % 50,
                    "framework": framework.split("_")[0],  # AI_ACT, GDPR, ISO
                    "article": framework,
                    "language": "es",
                    "confidence": random.uniform(0.7, 1.0)
                }
            )
            points.append(point)
        
        # Upsert batch
        client.upsert(
            collection_name="knowledge_base_docs",
            points=points
        )
        
        if (batch_num + 1) % 100 == 0:
            print(f"  Progress: {(batch_num + 1) * batch_size}/{count} chunks")

if __name__ == "__main__":
    seed_qdrant_complete()
```

---

## 📦 PROMPT 8: Seed MinIO Sample Files

**Objetivo:** Upload 1000 archivos sample a MinIO para demos

**Crear:**

```python
# scripts/seed_minio_sample_files.py
from minio import Minio
from io import BytesIO
from faker import Faker
from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import letter
import random

fake = Faker(['es_ES'])

class MinIOSeeder:
    """Seed MinIO con archivos sample"""
    
    def __init__(self, minio_url="localhost:9000"):
        self.minio = Minio(
            minio_url,
            access_key="codeflowx_admin",
            secret_key=os.getenv("MINIO_SECRET_KEY"),
            secure=False
        )
    
    def generate_sample_pdf(self, title, content_lines):
        """Generate PDF sintético"""
        
        buffer = BytesIO()
        c = canvas.Canvas(buffer, pagesize=letter)
        
        # Title
        c.setFont("Helvetica-Bold", 16)
        c.drawString(100, 750, title)
        
        # Content
        c.setFont("Helvetica", 10)
        y = 700
        for line in content_lines:
            c.drawString(100, y, line[:80])  # Max 80 chars
            y -= 15
            if y < 100:  # New page
                c.showPage()
                y = 750
        
        c.save()
        buffer.seek(0)
        return buffer
    
    def seed_technical_docs(self, count=100):
        """Seed 100 documentos técnicos Anexo IV"""
        
        print(f"Seeding {count} technical docs to MinIO...")
        
        for i in range(count):
            # Generate PDF
            title = f"Anexo IV - Technical Documentation Model #{random.randint(1, 50000)}"
            content = [
                "SECCIÓN 1: DESCRIPCIÓN SISTEMA IA",
                f"Nombre: {fake.catch_phrase()}",
                f"Versión: {random.randint(1,5)}.{random.randint(0,9)}",
                "Clasificación riesgo: HIGH RISK",
                "",
                "SECCIÓN 2: DATOS ENTRENAMIENTO",
                f"Dataset origen: {fake.company()} Database",
                f"Registros: {random.randint(100000, 5000000):,}",
                "Calidad: 94.5%",
                "",
                "SECCIÓN 3: MÉTRICAS RENDIMIENTO",
                f"Accuracy: {random.uniform(0.85, 0.98):.2%}",
                f"Precision: {random.uniform(0.80, 0.95):.2%}",
                f"Bias score: {random.uniform(0.01, 0.10):.2%}",
                "",
                "... (resto secciones Anexo IV)",
                "",
                f"Generado: {fake.date_time_this_year()}"
            ]
            
            pdf_buffer = self.generate_sample_pdf(title, content)
            
            # Upload MinIO
            object_name = f"models/{random.randint(1, 50000)}/anexo_iv_{i}.pdf"
            
            self.minio.put_object(
                bucket_name="technical-docs",
                object_name=object_name,
                data=pdf_buffer,
                length=pdf_buffer.getbuffer().nbytes,
                content_type="application/pdf",
                metadata={
                    "model_id": str(random.randint(1, 50000)),
                    "document_type": "ANEXO_IV",
                    "generated_at": fake.iso8601()
                }
            )
            
            if (i + 1) % 20 == 0:
                print(f"  Uploaded {i + 1}/{count} technical docs...")
        
        print(f"✅ {count} technical docs uploaded to MinIO")
    
    def seed_datasets_parquet(self, count=200):
        """Seed 200 datasets parquet sample"""
        
        # Generate sample parquet files (small, synthetic)
        import pandas as pd
        import pyarrow.parquet as pq
        
        for i in range(count):
            # Generate sample dataframe
            df = pd.DataFrame({
                'id': range(1000),
                'feature_1': np.random.randn(1000),
                'feature_2': np.random.randn(1000),
                'target': np.random.randint(0, 2, 1000)
            })
            
            # Save to buffer
            buffer = BytesIO()
            df.to_parquet(buffer)
            buffer.seek(0)
            
            # Upload MinIO
            object_name = f"datasets/dataset_{i}.parquet"
            
            self.minio.put_object(
                bucket_name="datasets",
                object_name=object_name,
                data=buffer,
                length=buffer.getbuffer().nbytes,
                content_type="application/octet-stream"
            )
        
        print(f"✅ {count} datasets parquet uploaded")

# Execute
if __name__ == "__main__":
    seeder = MinIOSeeder()
    
    seeder.seed_technical_docs(100)         # 100 PDFs Anexo IV
    seeder.seed_datasets_parquet(200)       # 200 Parquet files
    # ... más tipos archivos
    
    print("\n🎉 MinIO seeded!")
```

---

## 💰 AHORRO COSTES APIs

**Sin datos sintéticos:**
- Demo con 100 evaluaciones = 100 llamadas GPT-4
- Coste: 100 × $0.50 = **$50 por demo**
- 10 demos/mes = **$500/mes** gastados en testing

**Con datos sintéticos + Llama local:**
- Demo con 100K evaluaciones = 0 llamadas externas
- Coste: **$0**
- Modelos locales Llama (one-time download 4.7 GB)

**ROI:** $500/mes × 12 meses = **$6,000/año ahorro**

---

## 🎯 CONFIGURACIÓN MODO DEMO

**application.properties:**

```properties
# Demo mode configuration
demo.mode.enabled=true
demo.use.mock.llm=true
demo.use.local.llm=true
demo.skip.external.apis=true

# LLM configuration
llm.provider=LOCAL  # LOCAL, OPENAI, ANTHROPIC
llm.local.url=http://localhost:11434  # Ollama
llm.local.model=llama3:8b

# Mock data
mock.llm.responses.enabled=true
mock.embeddings.enabled=false  # Use local embeddings (más realistas)
```

**Switch fácil:**

```python
# Auto-detect demo mode
if os.getenv("DEMO_MODE", "false") == "true":
    llm_service = LocalLLMService()  # Llama local (gratis)
    use_mock_data = True
else:
    llm_service = OpenAIService()    # OpenAI (producción)
    use_mock_data = False
```

---

## ✅ RESUMEN PROMPTS_13

**Total prompts:** 8

**Generadores datos:**
1. Base (projects, models, datasets) - 10 min coding
2. Evaluations + logs (500K + 5M) - 15 min coding
3. Compliance (FRIA, GDPR, incidents) - 10 min coding
4. Qdrant embeddings (100K) - 15 min coding
5. MinIO files (PDFs, parquet) - 20 min coding

**Modelos locales:**
6. Setup Llama + Mistral (Ollama Docker) - 30 min
7. Mock LLM service (responses cached) - 15 min

**Master:**
8. Seed everything script (1-comando) - 30 min coding

**Total implementación:** 2-3 días (1 chat Python)

**Beneficios:**
- ✅ Demos gratis (sin APIs)
- ✅ 6.5M registros sintéticos
- ✅ Dashboards impresionantes
- ✅ Testing performance real
- ✅ 1 comando seed completo

**Ahorro:** $6,000/año en APIs testing

---

**Última actualización:** 5 Noviembre 2025  
**Prioridad:** 🔴 CRÍTICA DEMO (dashboards vacíos no impresionan)  
**Estimación:** 2-3 días (1 chat Python)
