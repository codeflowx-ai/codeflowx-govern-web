# 🔗 INTEGRACIÓN - MÓDULO RAG

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Guía completa de integración para el módulo RAG con sistemas externos e internos

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **RAG** proporciona **integración completa** con sistemas externos e internos mediante **APIs REST**, **SDKs**, **webhooks** y **conectores especializados** para garantizar la **gobernanza efectiva** de sistemas RAG en entornos distribuidos.

---

## 🌐 INTEGRACIÓN EXTERNA

### **1. Plataformas de Vector Databases**

#### **Pinecone Integration**
```python
# Integración con Pinecone
import pinecone
from codeflowx_rag import RagClient

# Configuración
pinecone.init(api_key="your_pinecone_key", environment="us-west1-gcp")
client = RagClient(api_key="your_api_key")

def register_pinecone_index(index_name, system_name):
    # Obtener índice de Pinecone
    index = pinecone.Index(index_name)
    
    # Registrar en CodeflowX
    datasource_data = {
        "name": f"Pinecone Index - {index_name}",
        "type": "VECTOR_DATABASE",
        "connection": f"pinecone://{index_name}",
        "configuration": {
            "provider": "pinecone",
            "index_name": index_name,
            "dimension": index.describe_index_stats()["dimension"]
        }
    }
    
    return client.systems.datasources.create(system_name, datasource_data)
```

#### **Weaviate Integration**
```python
# Integración con Weaviate
import weaviate
from codeflowx_rag import RagClient

client = RagClient(api_key="your_api_key")

def register_weaviate_schema(schema_name, system_name):
    # Conectar a Weaviate
    client_weaviate = weaviate.Client("http://localhost:8080")
    
    # Obtener esquema
    schema = client_weaviate.schema.get()
    
    # Registrar en CodeflowX
    datasource_data = {
        "name": f"Weaviate Schema - {schema_name}",
        "type": "VECTOR_DATABASE",
        "connection": "weaviate://localhost:8080",
        "configuration": {
            "provider": "weaviate",
            "schema_name": schema_name,
            "classes": list(schema["classes"].keys())
        }
    }
    
    return client.systems.datasources.create(system_name, datasource_data)
```

### **2. Sistemas de Documentos**

#### **SharePoint Integration**
```python
# Integración con SharePoint
from office365.runtime.auth.authentication_context import AuthenticationContext
from codeflowx_rag import RagClient

client = RagClient(api_key="your_api_key")

def register_sharepoint_library(site_url, library_name, system_name):
    # Conectar a SharePoint
    ctx = AuthenticationContext(site_url)
    ctx.acquire_token_for_user(username, password)
    
    # Registrar en CodeflowX
    datasource_data = {
        "name": f"SharePoint - {library_name}",
        "type": "DOCUMENT_LIBRARY",
        "connection": f"sharepoint://{site_url}/{library_name}",
        "configuration": {
            "provider": "sharepoint",
            "site_url": site_url,
            "library_name": library_name,
            "sync_frequency": "hourly"
        }
    }
    
    return client.systems.datasources.create(system_name, datasource_data)
```

#### **Confluence Integration**
```python
# Integración con Confluence
from atlassian import Confluence
from codeflowx_rag import RagClient

client = RagClient(api_key="your_api_key")

def register_confluence_space(space_key, system_name):
    # Conectar a Confluence
    confluence = Confluence(
        url="https://your-domain.atlassian.net",
        username="your-email@domain.com",
        password="your-api-token"
    )
    
    # Obtener páginas del espacio
    pages = confluence.get_all_pages_from_space(space_key)
    
    # Registrar en CodeflowX
    datasource_data = {
        "name": f"Confluence Space - {space_key}",
        "type": "WIKI",
        "connection": f"confluence://{space_key}",
        "configuration": {
            "provider": "confluence",
            "space_key": space_key,
            "page_count": len(pages),
            "sync_frequency": "daily"
        }
    }
    
    return client.systems.datasources.create(system_name, datasource_data)
```

### **3. Sistemas de Monitoreo**

#### **Prometheus Integration**
```python
# Integración con Prometheus
from prometheus_client import Counter, Histogram
from codeflowx_rag import RagClient

# Métricas de Prometheus
rag_requests = Counter('rag_requests_total', 'Total RAG requests', ['system_id', 'status'])
rag_latency = Histogram('rag_latency_seconds', 'RAG response latency', ['system_id'])

client = RagClient(api_key="your_api_key")

def sync_metrics_to_codeflowx(system_id):
    """Sincroniza métricas de Prometheus con CodeflowX"""
    
    # Obtener métricas de Prometheus
    requests_total = rag_requests.labels(system_id=system_id)._value.get()
    latency_avg = rag_latency.labels(system_id=system_id)._sum.get() / max(1, rag_latency.labels(system_id=system_id)._count.get())
    
    # Enviar a CodeflowX
    client.systems.metrics.update(system_id, {
        "total_requests": requests_total,
        "avg_latency": latency_avg,
        "timestamp": datetime.utcnow().isoformat()
    })
```

---

## 🏠 INTEGRACIÓN INTERNA

### **1. Módulo de Agentes**

#### **Integración con Agentes**
```python
# Integración entre módulos RAG y agentes
from codeflowx_rag import RagClient
from codeflowx_agents import AgentClient

rag_client = RagClient(api_key="your_api_key")
agents_client = AgentClient(api_key="your_api_key")

def link_rag_to_agent(system_id, agent_id):
    """Vincula un sistema RAG a un agente"""
    
    # Obtener sistema RAG
    rag_system = rag_client.systems.get(system_id)
    
    # Actualizar agente con sistema RAG
    agent_update = {
        "rag_system_id": system_id,
        "rag_endpoint": rag_system.endpoint,
        "rag_configuration": rag_system.configuration
    }
    
    return agents_client.agents.update(agent_id, agent_update)

def track_agent_rag_usage(agent_id, system_id, query, response, metrics):
    """Rastrea uso de RAG por agente"""
    
    # Registrar uso en RAG
    rag_client.systems.usage.track(system_id, {
        "agent_id": agent_id,
        "query": query,
        "response": response,
        "metrics": metrics,
        "timestamp": datetime.utcnow().isoformat()
    })
    
    # Actualizar métricas del agente
    agents_client.agents.metrics.update(agent_id, {
        "rag_queries": 1,
        "rag_accuracy": metrics.get("accuracy", 0),
        "rag_latency": metrics.get("latency", 0)
    })
```

### **2. Módulo de Modelos**

#### **Integración con Modelos**
```python
# Integración entre módulos RAG y modelos
from codeflowx_rag import RagClient
from codeflowx_models import ModelClient

rag_client = RagClient(api_key="your_api_key")
models_client = ModelClient(api_key="your_api_key")

def validate_rag_model_compatibility(system_id, model_id):
    """Valida compatibilidad entre sistema RAG y modelo"""
    
    # Obtener sistema RAG
    rag_system = rag_client.systems.get(system_id)
    
    # Obtener modelo
    model = models_client.models.get(model_id)
    
    # Verificar compatibilidad
    compatibility_check = {
        "rag_type": rag_system.type,
        "model_type": model.type,
        "compatible": check_compatibility(rag_system, model),
        "recommendations": get_recommendations(rag_system, model)
    }
    
    return compatibility_check

def optimize_rag_model_performance(system_id, model_id):
    """Optimiza rendimiento conjunto de RAG y modelo"""
    
    # Ejecutar evaluación conjunta
    evaluation = rag_client.systems.evaluate(system_id, {
        "type": "MODEL_COMPATIBILITY",
        "model_id": model_id,
        "criteria": {
            "min_accuracy": 0.85,
            "max_latency": 200
        }
    })
    
    return evaluation
```

---

## 🔄 SINCRONIZACIÓN DE DATOS

### **1. Sincronización con Repositorios**

#### **Git Integration**
```python
# Sincronización con repositorios Git
import git
from codeflowx_rag import RagClient

client = RagClient(api_key="your_api_key")

def sync_documents_from_git(repo_url, branch="main"):
    """Sincroniza documentos desde repositorio Git"""
    
    # Clonar repositorio
    repo = git.Repo.clone_from(repo_url, "/tmp/docs_repo")
    repo.git.checkout(branch)
    
    # Buscar documentos
    doc_files = []
    for root, dirs, files in os.walk("/tmp/docs_repo"):
        for file in files:
            if file.endswith(('.md', '.txt', '.pdf', '.docx')):
                doc_files.append(os.path.join(root, file))
    
    # Registrar documentos
    for doc_file in doc_files:
        doc_data = {
            "name": os.path.basename(doc_file),
            "type": "DOCUMENT",
            "source": "GIT_REPOSITORY",
            "repository_url": repo_url,
            "branch": branch,
            "file_path": doc_file
        }
        
        client.documents.create(doc_data)
```

### **2. Sincronización de Métricas**

#### **Real-time Metrics Sync**
```python
# Sincronización de métricas en tiempo real
import asyncio
from codeflowx_rag import RagClient

client = RagClient(api_key="your_api_key")

async def sync_rag_metrics_realtime(system_id):
    """Sincroniza métricas RAG en tiempo real"""
    
    while True:
        try:
            # Obtener métricas del sistema de monitoreo
            metrics = await get_rag_metrics(system_id)
            
            # Enviar a CodeflowX
            await client.systems.metrics.update(system_id, metrics)
            
            # Esperar 30 segundos
            await asyncio.sleep(30)
            
        except Exception as e:
            print(f"Error syncing RAG metrics: {e}")
            await asyncio.sleep(60)

async def get_rag_metrics(system_id):
    """Obtiene métricas RAG del sistema de monitoreo"""
    
    return {
        "accuracy": 0.92,
        "latency": 120,
        "throughput": 150,
        "coverage": 0.85,
        "timestamp": datetime.utcnow().isoformat()
    }
```

---

## 🔔 WEBHOOKS Y EVENTOS

### **1. Configuración de Webhooks**

#### **Webhook para RAG**
```python
# Configuración de webhook para eventos RAG
from flask import Flask, request, jsonify
from codeflowx_rag import RagClient

app = Flask(__name__)
client = RagClient(api_key="your_api_key")

@app.route('/webhooks/rag', methods=['POST'])
def handle_rag_webhook():
    """Maneja webhooks de eventos RAG"""
    
    payload = request.json
    event_type = payload.get('event')
    
    if event_type == 'rag.system.degraded':
        # Sistema RAG degradado - notificar equipo
        system_id = payload['data']['idxragsystem']
        notify_team(system_id, "RAG system performance degraded")
        
    elif event_type == 'rag.evaluation.completed':
        # Evaluación completada - revisar resultados
        system_id = payload['data']['idxragsystem']
        review_evaluation_results(system_id)
        
    elif event_type == 'rag.datasource.synced':
        # Fuente de datos sincronizada - actualizar índices
        datasource_id = payload['data']['idxragdatasource']
        update_vector_indexes(datasource_id)
    
    return jsonify({"status": "success"})

def notify_team(system_id, message):
    """Notifica al equipo sobre eventos del sistema RAG"""
    
    # Implementar notificación (Slack, email, etc.)
    pass

def review_evaluation_results(system_id):
    """Revisa resultados de evaluación"""
    
    # Obtener evaluación más reciente
    evaluation = client.systems.evaluations.get_latest(system_id)
    
    # Si la calidad es baja, crear alerta
    if evaluation.quality_score < 0.8:
        client.systems.alerts.create(system_id, {
            "type": "QUALITY_DEGRADATION",
            "message": f"RAG system quality below threshold: {evaluation.quality_score}",
            "severity": "HIGH"
        })
```

---

## 🛡️ SEGURIDAD Y AUTENTICACIÓN

### **1. Autenticación de APIs**

#### **API Key Management**
```python
# Gestión de API Keys para RAG
from codeflowx_rag import RagClient

def create_rag_api_key(user_id, permissions):
    """Crea una nueva API Key para RAG"""
    
    api_key_data = {
        "user_id": user_id,
        "permissions": permissions,
        "expires_at": datetime.utcnow() + timedelta(days=365),
        "scope": "rag_systems"
    }
    
    return client.api_keys.create(api_key_data)

def rotate_rag_api_key(api_key_id):
    """Rota una API Key de RAG existente"""
    
    # Generar nueva API Key
    new_api_key = generate_new_api_key()
    
    # Actualizar API Key
    client.api_keys.update(api_key_id, {
        "key": new_api_key,
        "rotated_at": datetime.utcnow().isoformat()
    })
    
    return new_api_key
```

### **2. Autorización y Permisos**

#### **Role-Based Access Control**
```python
# Control de acceso basado en roles para RAG
from codeflowx_rag import RagClient

client = RagClient(api_key="your_api_key")

def check_rag_permission(user_id, system_id, action):
    """Verifica permisos de usuario sobre sistema RAG"""
    
    # Obtener roles del usuario
    user_roles = client.users.get_roles(user_id)
    
    # Obtener sistema RAG
    rag_system = client.systems.get(system_id)
    
    # Verificar permisos
    if action == "read":
        return "RAG_USER" in user_roles or "RAG_ADMIN" in user_roles
    elif action == "write":
        return "RAG_ENGINEER" in user_roles or "RAG_ADMIN" in user_roles
    elif action == "evaluate":
        return "RAG_MANAGER" in user_roles or "RAG_ADMIN" in user_roles
    elif action == "delete":
        return "RAG_ADMIN" in user_roles
    
    return False
```

---

## 📊 MONITOREO DE INTEGRACIÓN

### **1. Health Checks**

#### **RAG System Health Check**
```python
# Health check de sistemas RAG
from codeflowx_rag import RagClient
import requests

client = RagClient(api_key="your_api_key")

def check_rag_system_health(system_id):
    """Verifica salud de un sistema RAG"""
    
    try:
        # Verificar conectividad del sistema RAG
        rag_system = client.systems.get(system_id)
        
        # Test de respuesta
        test_response = client.systems.query(system_id, {
            "query": "test query",
            "max_results": 1
        })
        
        if test_response and len(test_response.results) > 0:
            return {
                "status": "healthy",
                "response_time": test_response.latency,
                "timestamp": datetime.utcnow().isoformat()
            }
        else:
            return {
                "status": "unhealthy",
                "error": "No response from RAG system",
                "timestamp": datetime.utcnow().isoformat()
            }
            
    except Exception as e:
        return {
            "status": "unhealthy",
            "error": str(e),
            "timestamp": datetime.utcnow().isoformat()
        }
```

---

## ✅ CONCLUSIÓN

La **integración del módulo RAG** proporciona una **conectividad completa** con sistemas externos e internos mediante:

- 🌐 **Integración externa** con Pinecone, Weaviate, SharePoint, Confluence
- 🏠 **Integración interna** con módulos de agentes y modelos
- 🔄 **Sincronización** con repositorios Git y sistemas de documentos
- 🔔 **Webhooks** para eventos en tiempo real
- 🛡️ **Seguridad** con autenticación y autorización robusta
- 📊 **Monitoreo** de salud y métricas de integración

**Esta integración está diseñada** para soportar entornos de producción complejos y alto volumen de operaciones con sistemas RAG.
