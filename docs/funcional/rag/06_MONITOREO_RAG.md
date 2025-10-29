# 📊 MONITOREO Y TRACING - MÓDULO RAG

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Sistema completo de monitoreo, tracing y alertas para el módulo RAG

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **RAG** implementa un **sistema de monitoreo integral** que incluye **métricas en tiempo real**, **tracing distribuido**, **detección de degradación**, **alertas automáticas** y **dashboards** para garantizar la **operación óptima** y **gobernanza efectiva** de sistemas RAG en producción.

---

## 📈 MÉTRICAS DE RENDIMIENTO

### **1. Métricas de Sistema RAG**

#### **Métricas de Precisión**
```python
# Métricas de precisión del sistema RAG
from codeflowx_rag import RagClient
import numpy as np

client = RagClient(api_key="your_api_key")

def calculate_rag_accuracy(system_id, queries, expected_answers):
    """Calcula precisión del sistema RAG"""
    
    accuracy_scores = []
    
    for query, expected in zip(queries, expected_answers):
        # Obtener respuesta del sistema RAG
        response = client.systems.query(system_id, {"query": query})
        
        # Calcular precisión usando métricas de NLP
        accuracy = calculate_semantic_similarity(response.answer, expected)
        accuracy_scores.append(accuracy)
    
    avg_accuracy = np.mean(accuracy_scores)
    
    # Enviar métricas a CodeflowX
    client.systems.metrics.update(system_id, {
        "accuracy": avg_accuracy,
        "query_count": len(queries),
        "timestamp": datetime.utcnow().isoformat()
    })
    
    return avg_accuracy

def calculate_semantic_similarity(answer1, answer2):
    """Calcula similitud semántica entre respuestas"""
    
    # Usar modelo de embeddings para calcular similitud
    from sentence_transformers import SentenceTransformer
    
    model = SentenceTransformer('all-MiniLM-L6-v2')
    embeddings = model.encode([answer1, answer2])
    
    # Calcular cosine similarity
    similarity = np.dot(embeddings[0], embeddings[1]) / (
        np.linalg.norm(embeddings[0]) * np.linalg.norm(embeddings[1])
    )
    
    return similarity
```

#### **Métricas de Latencia**
```python
# Métricas de latencia del sistema RAG
import time
from prometheus_client import Histogram, Counter

# Métricas de Prometheus
rag_latency = Histogram('rag_latency_seconds', 'RAG response latency', ['system_id'])
rag_requests = Counter('rag_requests_total', 'Total RAG requests', ['system_id', 'status'])

def track_rag_latency(system_id, query_func, query):
    """Rastrea latencia de respuesta del sistema RAG"""
    
    start_time = time.time()
    
    try:
        # Ejecutar consulta
        result = query_func(system_id, query)
        
        # Calcular latencia
        latency = time.time() - start_time
        
        # Registrar métricas
        rag_latency.labels(system_id=system_id).observe(latency)
        rag_requests.labels(system_id=system_id, status='success').inc()
        
        # Enviar a CodeflowX
        client.systems.metrics.update(system_id, {
            "latency": latency,
            "status": "success",
            "timestamp": datetime.utcnow().isoformat()
        })
        
        return result
        
    except Exception as e:
        # Registrar error
        rag_requests.labels(system_id=system_id, status='error').inc()
        
        # Enviar error a CodeflowX
        client.systems.metrics.update(system_id, {
            "status": "error",
            "error_message": str(e),
            "timestamp": datetime.utcnow().isoformat()
        })
        
        raise e
```

#### **Métricas de Cobertura**
```python
# Métricas de cobertura de documentos
def calculate_document_coverage(system_id, test_queries):
    """Calcula cobertura de documentos del sistema RAG"""
    
    coverage_scores = []
    
    for query in test_queries:
        # Obtener respuesta con fuentes
        response = client.systems.query(system_id, {
            "query": query,
            "include_sources": True
        })
        
        # Calcular cobertura basada en fuentes retornadas
        if response.sources and len(response.sources) > 0:
            coverage = min(1.0, len(response.sources) / 5.0)  # Normalizar a 5 fuentes máximo
        else:
            coverage = 0.0
            
        coverage_scores.append(coverage)
    
    avg_coverage = np.mean(coverage_scores)
    
    # Enviar métricas a CodeflowX
    client.systems.metrics.update(system_id, {
        "coverage": avg_coverage,
        "query_count": len(test_queries),
        "timestamp": datetime.utcnow().isoformat()
    })
    
    return avg_coverage
```

### **2. Métricas de Uso**

#### **Métricas por Agente**
```python
# Métricas de uso por agente
from collections import defaultdict

class AgentRagMetricsTracker:
    def __init__(self):
        self.agent_queries = defaultdict(int)
        self.agent_accuracy = defaultdict(list)
        self.agent_latency = defaultdict(list)
    
    def track_agent_query(self, agent_id, system_id, query, response, accuracy, latency):
        """Rastrea consulta de agente"""
        
        self.agent_queries[agent_id] += 1
        self.agent_accuracy[agent_id].append(accuracy)
        self.agent_latency[agent_id].append(latency)
        
        # Enviar métricas a CodeflowX
        client.systems.metrics.update(system_id, {
            "agent_id": agent_id,
            "total_queries": self.agent_queries[agent_id],
            "avg_accuracy": np.mean(self.agent_accuracy[agent_id]),
            "avg_latency": np.mean(self.agent_latency[agent_id]),
            "timestamp": datetime.utcnow().isoformat()
        })

# Uso del tracker
agent_metrics_tracker = AgentRagMetricsTracker()

def track_agent_rag_usage(agent_id, system_id, query, response, accuracy, latency):
    """Rastrea uso de RAG por agente"""
    
    agent_metrics_tracker.track_agent_query(agent_id, system_id, query, response, accuracy, latency)
```

---

## 🔍 DETECCIÓN DE DEGRADACIÓN

### **1. Detección de Degradación de Calidad**

#### **Quality Degradation Detection**
```python
# Detección de degradación de calidad
from sklearn.ensemble import IsolationForest
from sklearn.preprocessing import StandardScaler

def detect_quality_degradation(system_id, current_metrics, baseline_metrics):
    """Detecta degradación en la calidad del sistema RAG"""
    
    # Métricas a comparar
    metrics_to_compare = ["accuracy", "coverage", "relevance"]
    
    degradation_detected = False
    degradation_details = {}
    
    for metric in metrics_to_compare:
        if metric in current_metrics and metric in baseline_metrics:
            current_value = current_metrics[metric]
            baseline_value = baseline_metrics[metric]
            
            # Calcular cambio porcentual
            change_percent = ((current_value - baseline_value) / baseline_value) * 100
            
            # Detectar degradación significativa
            if change_percent < -10.0:  # Umbral configurable
                degradation_detected = True
                degradation_details[metric] = {
                    "current_value": current_value,
                    "baseline_value": baseline_value,
                    "change_percent": change_percent,
                    "status": "DEGRADED"
                }
    
    if degradation_detected:
        # Enviar alerta a CodeflowX
        client.systems.alerts.create(system_id, {
            "type": "QUALITY_DEGRADATION",
            "details": degradation_details,
            "severity": "HIGH",
            "timestamp": datetime.utcnow().isoformat()
        })
    
    return degradation_detected, degradation_details
```

#### **Anomaly Detection**
```python
# Detección de anomalías en métricas
class RagAnomalyDetector:
    def __init__(self):
        self.anomaly_models = {}
        self.scalers = {}
    
    def train_anomaly_model(self, system_id, historical_metrics):
        """Entrena modelo de detección de anomalías"""
        
        # Preparar datos
        X = np.array([list(metrics.values()) for metrics in historical_metrics])
        
        # Normalizar datos
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)
        
        # Entrenar modelo
        anomaly_model = IsolationForest(contamination=0.1)
        anomaly_model.fit(X_scaled)
        
        # Guardar modelos
        self.anomaly_models[system_id] = anomaly_model
        self.scalers[system_id] = scaler
    
    def detect_anomalies(self, system_id, current_metrics):
        """Detecta anomalías en métricas actuales"""
        
        if system_id not in self.anomaly_models:
            return None
        
        # Preparar datos
        X = np.array([list(current_metrics.values())]).reshape(1, -1)
        
        # Normalizar
        X_scaled = self.scalers[system_id].transform(X)
        
        # Detectar anomalías
        anomaly_score = self.anomaly_models[system_id].decision_function(X_scaled)[0]
        is_anomaly = self.anomaly_models[system_id].predict(X_scaled)[0] == -1
        
        if is_anomaly:
            alert = {
                "type": "ANOMALY",
                "system_id": system_id,
                "anomaly_score": anomaly_score,
                "metrics": current_metrics,
                "timestamp": datetime.utcnow().isoformat()
            }
            
            # Enviar alerta
            client.systems.alerts.create(alert)
            
            return alert
        
        return None
```

### **2. Detección de Degradación de Rendimiento**

#### **Performance Degradation Detection**
```python
# Detección de degradación de rendimiento
def detect_performance_degradation(system_id, current_metrics, baseline_metrics):
    """Detecta degradación en el rendimiento del sistema RAG"""
    
    degradation_results = {}
    
    # Métricas de rendimiento a comparar
    performance_metrics = ["latency", "throughput", "error_rate"]
    
    for metric in performance_metrics:
        if metric in current_metrics and metric in baseline_metrics:
            current_value = current_metrics[metric]
            baseline_value = baseline_metrics[metric]
            
            # Calcular cambio porcentual
            if metric == "latency":
                change_percent = ((current_value - baseline_value) / baseline_value) * 100
                degradation_detected = change_percent > 20.0  # Latencia aumentó más del 20%
            elif metric == "throughput":
                change_percent = ((baseline_value - current_value) / baseline_value) * 100
                degradation_detected = change_percent > 15.0  # Throughput disminuyó más del 15%
            elif metric == "error_rate":
                change_percent = ((current_value - baseline_value) / baseline_value) * 100
                degradation_detected = change_percent > 10.0  # Error rate aumentó más del 10%
            
            degradation_results[metric] = {
                "current_value": current_value,
                "baseline_value": baseline_value,
                "change_percent": change_percent,
                "degradation_detected": degradation_detected
            }
    
    # Enviar resultados a CodeflowX
    client.systems.metrics.update(system_id, {
        "degradation_analysis": degradation_results,
        "timestamp": datetime.utcnow().isoformat()
    })
    
    return degradation_results
```

---

## 🚨 SISTEMA DE ALERTAS

### **1. Alertas Automáticas**

#### **Alert Configuration**
```python
# Configuración de alertas para RAG
class RagAlertManager:
    def __init__(self):
        self.alert_rules = {}
        self.alert_channels = {}
    
    def add_alert_rule(self, system_id, rule_name, condition, threshold, severity):
        """Añade regla de alerta para sistema RAG"""
        
        self.alert_rules[f"{system_id}_{rule_name}"] = {
            "system_id": system_id,
            "rule_name": rule_name,
            "condition": condition,
            "threshold": threshold,
            "severity": severity,
            "enabled": True
        }
    
    def check_alerts(self, system_id, metrics):
        """Verifica alertas para un sistema RAG"""
        
        triggered_alerts = []
        
        for rule_key, rule in self.alert_rules.items():
            if rule["system_id"] == system_id and rule["enabled"]:
                if self.evaluate_condition(rule["condition"], metrics, rule["threshold"]):
                    alert = {
                        "rule_name": rule["rule_name"],
                        "severity": rule["severity"],
                        "condition": rule["condition"],
                        "threshold": rule["threshold"],
                        "actual_value": metrics.get(rule["condition"]),
                        "timestamp": datetime.utcnow().isoformat()
                    }
                    
                    triggered_alerts.append(alert)
                    
                    # Enviar alerta
                    self.send_alert(alert)
        
        return triggered_alerts
    
    def evaluate_condition(self, condition, metrics, threshold):
        """Evalúa condición de alerta"""
        
        if condition not in metrics:
            return False
        
        actual_value = metrics[condition]
        
        # Evaluar condición
        if condition == "accuracy":
            return actual_value < threshold
        elif condition == "latency":
            return actual_value > threshold
        elif condition == "coverage":
            return actual_value < threshold
        elif condition == "error_rate":
            return actual_value > threshold
        
        return False
    
    def send_alert(self, alert):
        """Envía alerta a canales configurados"""
        
        # Enviar a CodeflowX
        client.systems.alerts.create(alert)
        
        # Enviar a canales externos (Slack, email, etc.)
        for channel_name, channel_config in self.alert_channels.items():
            self.send_to_channel(channel_name, alert, channel_config)

# Uso del AlertManager
rag_alert_manager = RagAlertManager()

# Configurar alertas
rag_alert_manager.add_alert_rule(
    system_id="rag_system_123",
    rule_name="low_accuracy",
    condition="accuracy",
    threshold=0.8,
    severity="HIGH"
)

rag_alert_manager.add_alert_rule(
    system_id="rag_system_123",
    rule_name="high_latency",
    condition="latency",
    threshold=500,
    severity="MEDIUM"
)
```

---

## 📊 DASHBOARDS Y VISUALIZACIÓN

### **1. Dashboard de Sistema RAG**

#### **RAG System Dashboard**
```python
# Dashboard de sistema RAG individual
def create_rag_system_dashboard(system_id):
    """Crea dashboard para un sistema RAG específico"""
    
    # Obtener datos del sistema
    rag_system = client.systems.get(system_id)
    metrics = client.systems.metrics.get(system_id)
    evaluations = client.systems.evaluations.get(system_id)
    
    dashboard = {
        "title": f"RAG System Dashboard - {rag_system.name}",
        "panels": [
            {
                "title": "System Overview",
                "type": "stat",
                "targets": [
                    {"expr": f"rag_accuracy{{system_id=\"{system_id}\"}}", "legendFormat": "Accuracy"},
                    {"expr": f"rag_latency{{system_id=\"{system_id}\"}}", "legendFormat": "Latency"},
                    {"expr": f"rag_coverage{{system_id=\"{system_id}\"}}", "legendFormat": "Coverage"}
                ]
            },
            {
                "title": "Performance Trends",
                "type": "graph",
                "targets": [
                    {"expr": f"rate(rag_accuracy{{system_id=\"{system_id}\"}}[5m])", "legendFormat": "Accuracy Trend"},
                    {"expr": f"rate(rag_latency{{system_id=\"{system_id}\"}}[5m])", "legendFormat": "Latency Trend"}
                ]
            },
            {
                "title": "Usage by Agent",
                "type": "graph",
                "targets": [
                    {"expr": f"rag_queries_by_agent{{system_id=\"{system_id}\"}}", "legendFormat": "Queries by Agent"}
                ]
            },
            {
                "title": "Error Rate",
                "type": "graph",
                "targets": [
                    {"expr": f"rate(rag_errors{{system_id=\"{system_id}\"}}[5m])", "legendFormat": "Error Rate"}
                ]
            }
        ]
    }
    
    return dashboard
```

### **2. Reportes Automáticos**

#### **Daily RAG Report**
```python
# Reporte diario automático de sistemas RAG
def generate_daily_rag_report():
    """Genera reporte diario de sistemas RAG"""
    
    # Obtener métricas del día
    end_time = datetime.utcnow()
    start_time = end_time - timedelta(days=1)
    
    systems = client.systems.list()
    report_data = []
    
    for system in systems:
        metrics = client.systems.metrics.get(
            system.id, 
            start_time=start_time.isoformat(),
            end_time=end_time.isoformat()
        )
        
        report_data.append({
            "system_id": system.id,
            "system_name": system.name,
            "total_queries": metrics.get("total_queries", 0),
            "avg_accuracy": metrics.get("accuracy", 0),
            "avg_latency": metrics.get("latency", 0),
            "avg_coverage": metrics.get("coverage", 0),
            "error_rate": metrics.get("error_rate", 0)
        })
    
    # Generar reporte
    report = {
        "date": end_time.date().isoformat(),
        "systems": report_data,
        "summary": {
            "total_systems": len(systems),
            "total_queries": sum(data["total_queries"] for data in report_data),
            "avg_accuracy": np.mean([data["avg_accuracy"] for data in report_data]),
            "avg_latency": np.mean([data["avg_latency"] for data in report_data])
        }
    }
    
    # Enviar reporte
    client.reports.create(report)
    
    return report
```

---

## 🔍 TRACING DISTRIBUIDO

### **1. Request Tracing**

#### **Distributed Tracing**
```python
# Tracing distribuido de consultas RAG
import opentelemetry
from opentelemetry import trace
from opentelemetry.exporter.jaeger.thrift import JaegerExporter
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor

# Configurar tracing
trace.set_tracer_provider(TracerProvider())
tracer = trace.get_tracer(__name__)

# Configurar exportador Jaeger
jaeger_exporter = JaegerExporter(
    agent_host_name="jaeger",
    agent_port=6831,
)

# Añadir procesador
span_processor = BatchSpanProcessor(jaeger_exporter)
trace.get_tracer_provider().add_span_processor(span_processor)

def trace_rag_query(system_id, query):
    """Traza consulta del sistema RAG"""
    
    with tracer.start_as_current_span("rag_query") as span:
        # Añadir atributos
        span.set_attribute("rag.system_id", system_id)
        span.set_attribute("rag.query_length", len(query))
        
        try:
            # Obtener sistema RAG
            rag_system = client.systems.get(system_id)
            span.set_attribute("rag.system_name", rag_system.name)
            span.set_attribute("rag.system_type", rag_system.type)
            
            # Ejecutar consulta
            with tracer.start_as_current_span("rag_retrieval") as retrieval_span:
                retrieval_result = execute_retrieval(rag_system, query)
                retrieval_span.set_attribute("rag.sources_found", len(retrieval_result.sources))
                retrieval_span.set_attribute("rag.retrieval_time", retrieval_result.retrieval_time)
            
            # Generar respuesta
            with tracer.start_as_current_span("rag_generation") as generation_span:
                response = execute_generation(rag_system, query, retrieval_result)
                generation_span.set_attribute("rag.response_length", len(response.answer))
                generation_span.set_attribute("rag.generation_time", response.generation_time)
            
            # Registrar métricas
            span.set_attribute("rag.success", True)
            span.set_attribute("rag.total_time", time.time() - start_time)
            
            return response
            
        except Exception as e:
            # Registrar error
            span.set_attribute("rag.success", False)
            span.set_attribute("rag.error", str(e))
            span.record_exception(e)
            raise e
```

### **2. Response Tracing**

#### **Response Audit Trail**
```python
# Auditoría de respuestas del sistema RAG
def trace_rag_response(system_id, query, response, metadata):
    """Traza respuesta del sistema RAG para auditoría"""
    
    response_trace = {
        "system_id": system_id,
        "query": query,
        "response": response.answer,
        "sources": response.sources,
        "metadata": metadata,
        "timestamp": datetime.utcnow().isoformat(),
        "trace_id": trace.get_current_span().get_span_context().trace_id,
        "span_id": trace.get_current_span().get_span_context().span_id
    }
    
    # Enviar a CodeflowX
    client.systems.responses.create(response_trace)
    
    # Enviar a sistema de auditoría
    send_to_audit_system(response_trace)
    
    return response_trace

def send_to_audit_system(response_trace):
    """Envía traza de respuesta al sistema de auditoría"""
    
    # Implementar envío a sistema de auditoría
    # (ej: Elasticsearch, Splunk, etc.)
    pass
```

---

## ✅ CONCLUSIÓN

El **sistema de monitoreo y tracing del módulo RAG** proporciona una **visibilidad completa** y **control efectivo** sobre sistemas RAG mediante:

- 📈 **Métricas en tiempo real** de precisión, latencia y cobertura
- 🔍 **Detección automática** de degradación de calidad y rendimiento
- 🚨 **Sistema de alertas** inteligente con múltiples canales
- 📊 **Dashboards** interactivos para visualización
- 🔍 **Tracing distribuido** para auditoría y debugging
- 📋 **Reportes automáticos** de rendimiento y calidad

**Este sistema está diseñado** para garantizar la **operación óptima** y **gobernanza efectiva** de sistemas RAG en entornos de producción.
