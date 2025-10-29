# 🔗 INTEGRACIÓN - MÓDULO MODELOS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Guía completa de integración para el módulo de modelos con sistemas externos e internos

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **modelos** proporciona **integración completa** con sistemas externos e internos mediante **APIs REST**, **SDKs**, **webhooks** y **conectores especializados** para garantizar la **gobernanza efectiva** de modelos de IA en entornos distribuidos.

### **Características de Integración:**
- **APIs REST** para integración programática
- **SDKs oficiales** para Python, JavaScript y Java
- **Webhooks** para eventos en tiempo real
- **Conectores** para plataformas ML populares
- **Sincronización** con repositorios de modelos
- **Monitoreo** de modelos desplegados

---

## 🌐 INTEGRACIÓN EXTERNA

### **1. Plataformas de Machine Learning**

#### **MLflow Integration**
```python
# Integración con MLflow
import mlflow
from codeflowx_models import ModelClient

# Configuración
mlflow.set_tracking_uri("http://mlflow-server:5000")
client = ModelClient(api_key="your_api_key")

# Registrar modelo desde MLflow
def register_model_from_mlflow(mlflow_run_id, model_name):
    # Obtener modelo de MLflow
    model_uri = f"runs:/{mlflow_run_id}/model"
    model = mlflow.sklearn.load_model(model_uri)
    
    # Registrar en CodeflowX
    model_data = {
        "name": model_name,
        "type": "CLASSIFICATION",
        "framework": "SKLEARN",
        "architecture": "RandomForest",
        "parameters": model.get_params(),
        "metrics": mlflow.get_run(mlflow_run_id).data.metrics
    }
    
    return client.models.create(model_data)
```

#### **TensorFlow Hub Integration**
```python
# Integración con TensorFlow Hub
import tensorflow_hub as hub
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")

def register_tfhub_model(model_url, model_name):
    # Cargar modelo desde TF Hub
    model = hub.load(model_url)
    
    # Registrar en CodeflowX
    model_data = {
        "name": model_name,
        "type": "CLASSIFICATION",
        "framework": "TENSORFLOW",
        "architecture": "BERT",
        "endpoint": model_url,
        "parameters": {
            "input_shape": model.input_shape,
            "output_shape": model.output_shape
        }
    }
    
    return client.models.create(model_data)
```

#### **Hugging Face Integration**
```python
# Integración con Hugging Face
from transformers import AutoModel, AutoTokenizer
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")

def register_huggingface_model(model_name, hf_model_id):
    # Cargar modelo y tokenizer
    model = AutoModel.from_pretrained(hf_model_id)
    tokenizer = AutoTokenizer.from_pretrained(hf_model_id)
    
    # Registrar en CodeflowX
    model_data = {
        "name": model_name,
        "type": "CLASSIFICATION",
        "framework": "TRANSFORMERS",
        "architecture": "BERT",
        "parameters": {
            "model_id": hf_model_id,
            "vocab_size": tokenizer.vocab_size,
            "hidden_size": model.config.hidden_size
        }
    }
    
    return client.models.create(model_data)
```

### **2. Sistemas de CI/CD**

#### **GitHub Actions Integration**
```yaml
# .github/workflows/model-governance.yml
name: Model Governance

on:
  push:
    branches: [main]
    paths: ['models/**']

jobs:
  model-governance:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Setup Python
        uses: actions/setup-python@v2
        with:
          python-version: '3.9'
      
      - name: Install dependencies
        run: |
          pip install codeflowx-models-sdk
          pip install -r models/requirements.txt
      
      - name: Register Model
        env:
          CODEFLOWX_API_KEY: ${{ secrets.CODEFLOWX_API_KEY }}
        run: |
          python models/register_model.py
      
      - name: Run Validations
        run: |
          python models/validate_model.py
      
      - name: Request Approval
        run: |
          python models/request_approval.py
```

#### **Jenkins Integration**
```groovy
// Jenkinsfile
pipeline {
    agent any
    
    environment {
        CODEFLOWX_API_KEY = credentials('codeflowx-api-key')
    }
    
    stages {
        stage('Model Registration') {
            steps {
                script {
                    sh '''
                        python models/register_model.py
                        python models/validate_model.py
                        python models/request_approval.py
                    '''
                }
            }
        }
        
        stage('Model Deployment') {
            when {
                expression { 
                    sh(script: 'python models/check_approval.py', returnStdout: true).trim() == 'APPROVED'
                }
            }
            steps {
                sh 'python models/deploy_model.py'
            }
        }
    }
}
```

### **3. Sistemas de Monitoreo**

#### **Prometheus Integration**
```python
# Integración con Prometheus
from prometheus_client import Counter, Histogram, Gauge
from codeflowx_models import ModelClient

# Métricas de Prometheus
model_requests = Counter('model_requests_total', 'Total model requests', ['model_id', 'status'])
model_latency = Histogram('model_latency_seconds', 'Model inference latency', ['model_id'])
model_accuracy = Gauge('model_accuracy', 'Model accuracy', ['model_id'])

client = ModelClient(api_key="your_api_key")

def sync_metrics_to_codeflowx(model_id):
    """Sincroniza métricas de Prometheus con CodeflowX"""
    
    # Obtener métricas de Prometheus
    requests_total = model_requests.labels(model_id=model_id)._value.get()
    latency_avg = model_latency.labels(model_id=model_id)._sum.get() / max(1, model_latency.labels(model_id=model_id)._count.get())
    accuracy = model_accuracy.labels(model_id=model_id)._value.get()
    
    # Enviar a CodeflowX
    client.models.metrics.update(model_id, {
        "total_requests": requests_total,
        "avg_latency": latency_avg,
        "accuracy": accuracy,
        "timestamp": datetime.utcnow().isoformat()
    })
```

#### **Grafana Integration**
```python
# Integración con Grafana
import requests
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")

def create_grafana_dashboard(model_id):
    """Crea dashboard de Grafana para un modelo"""
    
    # Obtener métricas del modelo
    metrics = client.models.metrics.get(model_id)
    
    # Crear dashboard
    dashboard = {
        "dashboard": {
            "title": f"Model {model_id} Dashboard",
            "panels": [
                {
                    "title": "Model Accuracy",
                    "type": "stat",
                    "targets": [
                        {
                            "expr": f"model_accuracy{{model_id=\"{model_id}\"}}",
                            "legendFormat": "Accuracy"
                        }
                    ]
                },
                {
                    "title": "Model Latency",
                    "type": "graph",
                    "targets": [
                        {
                            "expr": f"rate(model_latency_seconds_sum{{model_id=\"{model_id}\"}}[5m])",
                            "legendFormat": "Latency"
                        }
                    ]
                }
            ]
        }
    }
    
    # Enviar a Grafana
    response = requests.post(
        "http://grafana:3000/api/dashboards/db",
        json=dashboard,
        headers={"Authorization": f"Bearer {GRAFANA_API_KEY}"}
    )
    
    return response.json()
```

---

## 🏠 INTEGRACIÓN INTERNA

### **1. Módulo de Agentes**

#### **Integración con Agentes**
```python
# Integración entre módulos de modelos y agentes
from codeflowx_models import ModelClient
from codeflowx_agents import AgentClient

models_client = ModelClient(api_key="your_api_key")
agents_client = AgentClient(api_key="your_api_key")

def link_model_to_agent(model_id, agent_id):
    """Vincula un modelo a un agente"""
    
    # Obtener modelo
    model = models_client.models.get(model_id)
    
    # Actualizar agente con modelo
    agent_update = {
        "model_id": model_id,
        "model_endpoint": model.endpoint,
        "model_version": model.version
    }
    
    return agents_client.agents.update(agent_id, agent_update)

def sync_model_performance_to_agent(model_id, agent_id):
    """Sincroniza rendimiento del modelo con el agente"""
    
    # Obtener métricas del modelo
    metrics = models_client.models.metrics.get(model_id)
    
    # Actualizar métricas del agente
    agent_metrics = {
        "model_accuracy": metrics.accuracy,
        "model_latency": metrics.avg_latency,
        "model_throughput": metrics.throughput
    }
    
    return agents_client.agents.metrics.update(agent_id, agent_metrics)
```

### **2. Módulo de Prompts**

#### **Integración con Prompts**
```python
# Integración entre módulos de modelos y prompts
from codeflowx_models import ModelClient
from codeflowx_prompts import PromptClient

models_client = ModelClient(api_key="your_api_key")
prompts_client = PromptClient(api_key="your_api_key")

def link_model_to_prompt(model_id, prompt_id):
    """Vincula un modelo a un prompt"""
    
    # Obtener modelo
    model = models_client.models.get(model_id)
    
    # Actualizar prompt con modelo
    prompt_update = {
        "model_id": model_id,
        "model_framework": model.framework,
        "model_architecture": model.architecture
    }
    
    return prompts_client.prompts.update(prompt_id, prompt_update)

def validate_prompt_with_model(prompt_id, model_id):
    """Valida un prompt con un modelo específico"""
    
    # Obtener prompt
    prompt = prompts_client.prompts.get(prompt_id)
    
    # Obtener modelo
    model = models_client.models.get(model_id)
    
    # Ejecutar validación
    validation_result = models_client.models.validations.create(model_id, {
        "type": "PROMPT_COMPATIBILITY",
        "prompt_id": prompt_id,
        "criteria": {
            "max_tokens": model.max_tokens,
            "supported_languages": model.supported_languages
        }
    })
    
    return validation_result
```

### **3. Módulo de Compliance**

#### **Integración con Compliance**
```python
# Integración con módulo de compliance
from codeflowx_models import ModelClient
from codeflowx_compliance import ComplianceClient

models_client = ModelClient(api_key="your_api_key")
compliance_client = ComplianceClient(api_key="your_api_key")

def check_model_compliance(model_id):
    """Verifica compliance de un modelo"""
    
    # Obtener modelo
    model = models_client.models.get(model_id)
    
    # Verificar compliance
    compliance_result = compliance_client.check_model_compliance({
        "model_id": model_id,
        "model_type": model.type,
        "model_framework": model.framework,
        "model_parameters": model.parameters,
        "model_training_data": model.training_data
    })
    
    # Actualizar estado de compliance del modelo
    if compliance_result.status == "COMPLIANT":
        models_client.models.update(model_id, {
            "compliance_status": "COMPLIANT",
            "compliance_score": compliance_result.score
        })
    
    return compliance_result
```

---

## 🔄 SINCRONIZACIÓN DE DATOS

### **1. Sincronización con Repositorios**

#### **Git Integration**
```python
# Sincronización con repositorios Git
import git
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")

def sync_models_from_git(repo_url, branch="main"):
    """Sincroniza modelos desde repositorio Git"""
    
    # Clonar repositorio
    repo = git.Repo.clone_from(repo_url, "/tmp/models_repo")
    repo.git.checkout(branch)
    
    # Buscar archivos de modelos
    model_files = []
    for root, dirs, files in os.walk("/tmp/models_repo"):
        for file in files:
            if file.endswith(('.pkl', '.h5', '.pb', '.onnx')):
                model_files.append(os.path.join(root, file))
    
    # Registrar modelos
    for model_file in model_files:
        model_data = {
            "name": os.path.basename(model_file),
            "type": "CLASSIFICATION",
            "framework": "SKLEARN",
            "file_path": model_file,
            "source": "GIT_REPOSITORY",
            "repository_url": repo_url,
            "branch": branch
        }
        
        client.models.create(model_data)
```

#### **S3 Integration**
```python
# Sincronización con Amazon S3
import boto3
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")
s3_client = boto3.client('s3')

def sync_models_from_s3(bucket_name, prefix=""):
    """Sincroniza modelos desde bucket S3"""
    
    # Listar objetos en S3
    response = s3_client.list_objects_v2(
        Bucket=bucket_name,
        Prefix=prefix
    )
    
    # Procesar cada modelo
    for obj in response.get('Contents', []):
        if obj['Key'].endswith(('.pkl', '.h5', '.pb', '.onnx')):
            model_data = {
                "name": os.path.basename(obj['Key']),
                "type": "CLASSIFICATION",
                "framework": "SKLEARN",
                "s3_bucket": bucket_name,
                "s3_key": obj['Key'],
                "source": "S3_BUCKET"
            }
            
            client.models.create(model_data)
```

### **2. Sincronización de Métricas**

#### **Real-time Metrics Sync**
```python
# Sincronización de métricas en tiempo real
import asyncio
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")

async def sync_metrics_realtime(model_id):
    """Sincroniza métricas en tiempo real"""
    
    while True:
        try:
            # Obtener métricas del sistema de monitoreo
            metrics = await get_system_metrics(model_id)
            
            # Enviar a CodeflowX
            await client.models.metrics.update(model_id, metrics)
            
            # Esperar 30 segundos
            await asyncio.sleep(30)
            
        except Exception as e:
            print(f"Error syncing metrics: {e}")
            await asyncio.sleep(60)

async def get_system_metrics(model_id):
    """Obtiene métricas del sistema de monitoreo"""
    
    # Implementar lógica para obtener métricas
    # desde Prometheus, Grafana, etc.
    
    return {
        "accuracy": 0.94,
        "latency": 120,
        "throughput": 100,
        "error_rate": 0.05,
        "timestamp": datetime.utcnow().isoformat()
    }
```

---

## 🔔 WEBHOOKS Y EVENTOS

### **1. Configuración de Webhooks**

#### **Webhook para Modelos**
```python
# Configuración de webhook para eventos de modelos
from flask import Flask, request, jsonify
from codeflowx_models import ModelClient

app = Flask(__name__)
client = ModelClient(api_key="your_api_key")

@app.route('/webhooks/models', methods=['POST'])
def handle_model_webhook():
    """Maneja webhooks de eventos de modelos"""
    
    payload = request.json
    event_type = payload.get('event')
    
    if event_type == 'model.approved':
        # Modelo aprobado - desplegar automáticamente
        model_id = payload['data']['idxmodel']
        deploy_model(model_id)
        
    elif event_type == 'model.drift.detected':
        # Drift detectado - notificar equipo
        model_id = payload['data']['idxmodel']
        notify_team(model_id, "Model drift detected")
        
    elif event_type == 'model.retrain.completed':
        # Reentrenamiento completado - validar nuevo modelo
        model_id = payload['data']['idxmodel']
        validate_retrained_model(model_id)
    
    return jsonify({"status": "success"})

def deploy_model(model_id):
    """Despliega un modelo aprobado"""
    
    # Obtener modelo
    model = client.models.get(model_id)
    
    # Desplegar en entorno de producción
    deployment_result = deploy_to_production(model)
    
    # Actualizar estado del modelo
    client.models.update(model_id, {
        "status": "DEPLOYED",
        "deployment_url": deployment_result.url
    })

def notify_team(model_id, message):
    """Notifica al equipo sobre eventos del modelo"""
    
    # Implementar notificación (Slack, email, etc.)
    pass

def validate_retrained_model(model_id):
    """Valida un modelo reentrenado"""
    
    # Ejecutar validaciones automáticas
    validation = client.models.validations.create(model_id, {
        "type": "RETRAIN_VALIDATION",
        "criteria": {
            "min_accuracy": 0.90,
            "max_latency": 200
        }
    })
    
    return validation
```

### **2. Eventos Personalizados**

#### **Eventos de Negocio**
```python
# Eventos de negocio personalizados
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")

def trigger_business_event(event_type, model_id, data):
    """Dispara eventos de negocio personalizados"""
    
    event_data = {
        "event": event_type,
        "model_id": model_id,
        "timestamp": datetime.utcnow().isoformat(),
        "data": data
    }
    
    # Enviar a sistema de eventos
    send_to_event_system(event_data)
    
    # Actualizar métricas de negocio
    update_business_metrics(model_id, event_type)

def send_to_event_system(event_data):
    """Envía evento al sistema de eventos"""
    
    # Implementar envío a Kafka, RabbitMQ, etc.
    pass

def update_business_metrics(model_id, event_type):
    """Actualiza métricas de negocio"""
    
    # Implementar actualización de métricas
    pass
```

---

## 🛡️ SEGURIDAD Y AUTENTICACIÓN

### **1. Autenticación de APIs**

#### **API Key Management**
```python
# Gestión de API Keys
from codeflowx_models import ModelClient

def create_api_key(user_id, permissions):
    """Crea una nueva API Key"""
    
    api_key_data = {
        "user_id": user_id,
        "permissions": permissions,
        "expires_at": datetime.utcnow() + timedelta(days=365)
    }
    
    return client.api_keys.create(api_key_data)

def rotate_api_key(api_key_id):
    """Rota una API Key existente"""
    
    # Generar nueva API Key
    new_api_key = generate_new_api_key()
    
    # Actualizar API Key
    client.api_keys.update(api_key_id, {
        "key": new_api_key,
        "rotated_at": datetime.utcnow().isoformat()
    })
    
    return new_api_key
```

#### **JWT Token Management**
```python
# Gestión de JWT Tokens
import jwt
from datetime import datetime, timedelta

def create_jwt_token(user_id, permissions):
    """Crea un JWT Token"""
    
    payload = {
        "user_id": user_id,
        "permissions": permissions,
        "exp": datetime.utcnow() + timedelta(hours=24)
    }
    
    token = jwt.encode(payload, SECRET_KEY, algorithm="HS256")
    return token

def validate_jwt_token(token):
    """Valida un JWT Token"""
    
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=["HS256"])
        return payload
    except jwt.ExpiredSignatureError:
        return None
    except jwt.InvalidTokenError:
        return None
```

### **2. Autorización y Permisos**

#### **Role-Based Access Control**
```python
# Control de acceso basado en roles
from codeflowx_models import ModelClient

client = ModelClient(api_key="your_api_key")

def check_model_permission(user_id, model_id, action):
    """Verifica permisos de usuario sobre un modelo"""
    
    # Obtener roles del usuario
    user_roles = client.users.get_roles(user_id)
    
    # Obtener modelo
    model = client.models.get(model_id)
    
    # Verificar permisos
    if action == "read":
        return "MODEL_USER" in user_roles or "MODEL_ADMIN" in user_roles
    elif action == "write":
        return "MODEL_ENGINEER" in user_roles or "MODEL_ADMIN" in user_roles
    elif action == "approve":
        return "MODEL_MANAGER" in user_roles or "MODEL_ADMIN" in user_roles
    elif action == "delete":
        return "MODEL_ADMIN" in user_roles
    
    return False
```

---

## 📊 MONITOREO DE INTEGRACIÓN

### **1. Health Checks**

#### **API Health Check**
```python
# Health check de APIs
from codeflowx_models import ModelClient
import requests

client = ModelClient(api_key="your_api_key")

def check_api_health():
    """Verifica salud de la API"""
    
    try:
        # Verificar conectividad
        response = requests.get(f"{client.base_url}/health", timeout=5)
        
        if response.status_code == 200:
            return {
                "status": "healthy",
                "response_time": response.elapsed.total_seconds(),
                "timestamp": datetime.utcnow().isoformat()
            }
        else:
            return {
                "status": "unhealthy",
                "error": f"HTTP {response.status_code}",
                "timestamp": datetime.utcnow().isoformat()
            }
            
    except Exception as e:
        return {
            "status": "unhealthy",
            "error": str(e),
            "timestamp": datetime.utcnow().isoformat()
        }
```

#### **Integration Health Check**
```python
# Health check de integraciones
def check_integration_health():
    """Verifica salud de todas las integraciones"""
    
    health_status = {
        "api": check_api_health(),
        "mlflow": check_mlflow_health(),
        "prometheus": check_prometheus_health(),
        "grafana": check_grafana_health(),
        "s3": check_s3_health()
    }
    
    overall_status = "healthy" if all(
        status["status"] == "healthy" 
        for status in health_status.values()
    ) else "unhealthy"
    
    return {
        "overall_status": overall_status,
        "components": health_status,
        "timestamp": datetime.utcnow().isoformat()
    }
```

### **2. Métricas de Integración**

#### **Integration Metrics**
```python
# Métricas de integración
from prometheus_client import Counter, Histogram

# Métricas de Prometheus
integration_requests = Counter('integration_requests_total', 'Total integration requests', ['integration_type', 'status'])
integration_latency = Histogram('integration_latency_seconds', 'Integration latency', ['integration_type'])

def track_integration_metrics(integration_type, success, latency):
    """Registra métricas de integración"""
    
    status = "success" if success else "failure"
    integration_requests.labels(
        integration_type=integration_type,
        status=status
    ).inc()
    
    integration_latency.labels(
        integration_type=integration_type
    ).observe(latency)
```

---

## ✅ CONCLUSIÓN

La **integración del módulo modelos** proporciona una **conectividad completa** con sistemas externos e internos mediante:

- 🌐 **Integración externa** con MLflow, TensorFlow Hub, Hugging Face
- 🏠 **Integración interna** con módulos de agentes, prompts y compliance
- 🔄 **Sincronización** con repositorios Git y S3
- 🔔 **Webhooks** para eventos en tiempo real
- 🛡️ **Seguridad** con autenticación y autorización robusta
- 📊 **Monitoreo** de salud y métricas de integración

**Esta integración está diseñada** para soportar entornos de producción complejos y alto volumen de operaciones con modelos de IA.
