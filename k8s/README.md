# ☸️ KUBERNETES SETUP - CODEFLOWX

## 🎯 ARQUITECTURA NAMESPACES

**Separación infraestructura vs aplicaciones:**

```
codeflowx-govern-systems  (Infraestructura)
├─ PostgreSQL + TimescaleDB
├─ Qdrant (vector DB)
├─ MinIO (object storage)
├─ OpenSearch + Dashboards
├─ Redis
└─ Ollama (LLMs locales)

codeflowx-govern  (Aplicaciones)
├─ Backend Java (Spring Boot)
├─ Microservicios Python FastAPI (20+)
├─ ZKoss UI
└─ BPMN Engine
```

**Ventajas separación:**
- ✅ **Isolation:** Problemas apps no afectan infra
- ✅ **RBAC:** Diferentes permisos (DBAs vs Developers)
- ✅ **Scaling:** Escalar apps sin tocar infra
- ✅ **Updates:** Actualizar apps sin downtime infra
- ✅ **Multi-tenant:** Múltiples aplicaciones usando misma infra

---

## 🚀 INSTALACIÓN RÁPIDA

### **Método 1: kubectl apply (Manual)**

```bash
# 1. Crear namespaces
kubectl apply -f 01-namespace.yaml

# Verificar
kubectl get namespaces | grep codeflowx

# Output esperado:
# codeflowx-govern-systems   Active   10s
# codeflowx-govern           Active   10s

# 2. Deploy infrastructure (en orden)
kubectl apply -f 02-postgresql-statefulset.yaml
kubectl apply -f 03-qdrant-statefulset.yaml
kubectl apply -f 04-minio-statefulset.yaml
kubectl apply -f 05-opensearch-statefulset.yaml
kubectl apply -f 06-redis-deployment.yaml

# 3. Esperar que todo esté running
kubectl get pods -n codeflowx-govern-systems --watch

# 4. Verificar health
kubectl get pods -n codeflowx-govern-systems
kubectl logs -n codeflowx-govern-systems postgres-0
kubectl logs -n codeflowx-govern-systems qdrant-0
kubectl logs -n codeflowx-govern-systems minio-0
kubectl logs -n codeflowx-govern-systems opensearch-0
```

**Tiempo:** 5-10 minutos (primera vez descarga imágenes)

---

### **Método 2: Helm Chart (Recomendado)**

```bash
# 1. Install Helm chart
helm install codeflowx-infra ./helm/codeflowx-infrastructure \
  --namespace codeflowx-govern-systems \
  --create-namespace \
  --values ./helm/codeflowx-infrastructure/values.yaml

# 2. Verificar instalación
helm list -n codeflowx-govern-systems
kubectl get pods -n codeflowx-govern-systems

# 3. Upgrade (cuando cambies values.yaml)
helm upgrade codeflowx-infra ./helm/codeflowx-infrastructure \
  --namespace codeflowx-govern-systems

# 4. Uninstall (si necesitas)
helm uninstall codeflowx-infra -n codeflowx-govern-systems
```

---

## 🔗 ACCESO CROSS-NAMESPACE

**Aplicaciones en `codeflowx-govern` acceden infra en `codeflowx-govern-systems`:**

```yaml
# Backend Java application (namespace: codeflowx-govern)
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend-java
  namespace: codeflowx-govern  # Namespace aplicaciones
spec:
  template:
    spec:
      containers:
      - name: backend
        env:
        # PostgreSQL (cross-namespace)
        - name: POSTGRES_HOST
          value: postgres.codeflowx-govern-systems.svc.cluster.local
        - name: POSTGRES_PORT
          value: "5432"
        
        # Qdrant (cross-namespace)
        - name: QDRANT_URL
          value: http://qdrant.codeflowx-govern-systems.svc.cluster.local:6333
        
        # MinIO (cross-namespace)
        - name: MINIO_ENDPOINT
          value: minio.codeflowx-govern-systems.svc.cluster.local:9000
        
        # OpenSearch (cross-namespace)
        - name: OPENSEARCH_URL
          value: http://opensearch.codeflowx-govern-systems.svc.cluster.local:9200
        
        # Redis (cross-namespace)
        - name: REDIS_HOST
          value: redis.codeflowx-govern-systems.svc.cluster.local
```

**DNS Format:** `<service-name>.<namespace>.svc.cluster.local`

---

## 🔐 NETWORK POLICIES (Opcional pero recomendado)

**Permitir solo aplicaciones acceder infraestructura:**

```yaml
# network-policy-infrastructure.yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-from-applications
  namespace: codeflowx-govern-systems
spec:
  podSelector: {}  # Apply a todos los pods en namespace systems
  policyTypes:
  - Ingress
  ingress:
  # Solo permitir tráfico desde namespace applications
  - from:
    - namespaceSelector:
        matchLabels:
          app: codeflowx
          tier: applications
    ports:
    - protocol: TCP
      port: 5432  # PostgreSQL
    - protocol: TCP
      port: 6333  # Qdrant HTTP
    - protocol: TCP
      port: 6334  # Qdrant gRPC
    - protocol: TCP
      port: 9000  # MinIO API
    - protocol: TCP
      port: 9200  # OpenSearch
    - protocol: TCP
      port: 6379  # Redis
```

---

## 📊 RECURSOS RECOMENDADOS

### **Desarrollo/Testing:**

```yaml
# values-dev.yaml
postgresql:
  resources:
    requests: {memory: 2Gi, cpu: 1}
    limits: {memory: 4Gi, cpu: 2}
  persistence:
    size: 20Gi

qdrant:
  resources:
    requests: {memory: 1Gi, cpu: 500m}
    limits: {memory: 2Gi, cpu: 1}
  persistence:
    storage: {size: 10Gi}

minio:
  persistence:
    size: 50Gi

opensearch:
  resources:
    requests: {memory: 2Gi, cpu: 1}
    limits: {memory: 4Gi, cpu: 2}
  persistence:
    size: 50Gi
```

**Total:** ~8 GB RAM, 4 CPU, 150 GB storage

---

### **Producción:**

```yaml
# values-production.yaml
postgresql:
  resources:
    requests: {memory: 8Gi, cpu: 4}
    limits: {memory: 16Gi, cpu: 8}
  persistence:
    size: 200Gi
  replicaCount: 2  # Master + 1 replica

qdrant:
  resources:
    requests: {memory: 4Gi, cpu: 2}
    limits: {memory: 8Gi, cpu: 4}
  persistence:
    storage: {size: 100Gi}
  replicaCount: 3  # HA cluster

minio:
  persistence:
    size: 1Ti
  replicaCount: 4  # Distributed mode

opensearch:
  resources:
    requests: {memory: 8Gi, cpu: 4}
    limits: {memory: 16Gi, cpu: 8}
  persistence:
    size: 500Gi
  replicaCount: 3  # Cluster 3 nodos
```

**Total:** ~50 GB RAM, 25 CPU, 2 TB storage

---

## 🚀 COMANDOS ÚTILES

### **Ver todos los recursos infraestructura:**

```bash
# Pods
kubectl get pods -n codeflowx-govern-systems

# Services
kubectl get svc -n codeflowx-govern-systems

# PVCs
kubectl get pvc -n codeflowx-govern-systems

# Secrets
kubectl get secrets -n codeflowx-govern-systems

# TODO en un namespace
kubectl get all -n codeflowx-govern-systems
```

---

### **Port-forward para acceso local:**

```bash
# PostgreSQL
kubectl port-forward -n codeflowx-govern-systems svc/postgres 5432:5432

# Qdrant Dashboard
kubectl port-forward -n codeflowx-govern-systems svc/qdrant 6333:6333
# Access: http://localhost:6333/dashboard

# MinIO Console
kubectl port-forward -n codeflowx-govern-systems svc/minio 9001:9001
# Access: http://localhost:9001

# OpenSearch Dashboards
kubectl port-forward -n codeflowx-govern-systems svc/opensearch-dashboards 5601:5601
# Access: http://localhost:5601
```

---

### **Logs:**

```bash
# PostgreSQL logs
kubectl logs -n codeflowx-govern-systems postgres-0 -f

# Qdrant logs
kubectl logs -n codeflowx-govern-systems qdrant-0 -f

# MinIO logs
kubectl logs -n codeflowx-govern-systems minio-0 -f

# OpenSearch logs
kubectl logs -n codeflowx-govern-systems opensearch-0 -f

# Logs últimas 100 líneas
kubectl logs -n codeflowx-govern-systems opensearch-0 --tail=100
```

---

### **Scaling:**

```bash
# Escalar OpenSearch a 3 nodos (cluster HA)
kubectl scale statefulset opensearch -n codeflowx-govern-systems --replicas=3

# Verificar
kubectl get pods -n codeflowx-govern-systems -l app=opensearch
```

---

## 🔄 BACKUP & RESTORE

### **PostgreSQL:**

```bash
# Backup
kubectl exec -n codeflowx-govern-systems postgres-0 -- \
  pg_dump -U codeflowx_user codeflowx > backup_$(date +%Y%m%d).sql

# Restore
kubectl exec -i -n codeflowx-govern-systems postgres-0 -- \
  psql -U codeflowx_user codeflowx < backup_20251105.sql
```

### **Qdrant:**

```bash
# Create snapshot
curl -X POST http://localhost:6333/snapshots

# Download snapshot
kubectl cp codeflowx-govern-systems/qdrant-0:/qdrant/snapshots/ ./qdrant-backup/
```

### **MinIO:**

```bash
# Usar mc (MinIO client)
mc alias set codeflowx-k8s http://localhost:9000 admin password
mc mirror codeflowx-k8s/technical-docs ./backup/minio/technical-docs
```

---

## 📝 NOTAS IMPORTANTES

### **1. OpenSearch vs Elasticsearch vs Solr:**

| Característica | OpenSearch | Elasticsearch | Solr |
|----------------|-----------|---------------|------|
| **Licencia** | Apache 2.0 (open source) | SSPL + Elastic (propietaria) | Apache 2.0 |
| **Origen** | Fork Elasticsearch 7.10.2 (2021) | Elastic NV | Apache Lucene |
| **Mantenido por** | Amazon + comunidad | Elastic NV | Apache Foundation |
| **APIs** | 100% compatible Elasticsearch | Elasticsearch | Solr (diferentes) |
| **Dashboards** | OpenSearch Dashboards (fork Kibana) | Kibana | Solr Admin UI |
| **Cloud** | AWS OpenSearch Service | Elastic Cloud | Solr Cloud |
| **Uso principal** | Logs, time-series, search | Search, analytics | Search tradicional |
| **Comunidad** | Grande (AWS backing) | Grande | Mediana |
| **Para CodeflowX** | ✅ **RECOMENDADO** | ⚠️ Licencia restrictiva | ❌ Menos features logs |

**Conclusión:** **OpenSearch es correcto** para CodeflowX (open source, logs/time-series, compatible APIs Elasticsearch).

---

### **2. Acceso cross-namespace:**

**Aplicaciones deben usar FQDN:**

```
# ❌ INCORRECTO (mismo namespace):
POSTGRES_HOST=postgres

# ✅ CORRECTO (cross-namespace):
POSTGRES_HOST=postgres.codeflowx-govern-systems.svc.cluster.local
```

---

### **3. Secrets management:**

**Producción:** Usar herramientas enterprise:
- AWS Secrets Manager
- Azure Key Vault
- Google Secret Manager
- HashiCorp Vault
- Sealed Secrets (K8s)

**Ejemplo con External Secrets Operator:**

```yaml
apiVersion: external-secrets.io/v1beta1
kind: ExternalSecret
metadata:
  name: postgres-secret
  namespace: codeflowx-govern-systems
spec:
  secretStoreRef:
    name: aws-secrets-manager
    kind: SecretStore
  target:
    name: postgres-secret
  data:
  - secretKey: password
    remoteRef:
      key: codeflowx/postgres/password
```

---

## 📦 ORDEN DEPLOYMENT

**Importante:** Deployar en orden (dependencias):

```bash
# Paso 1: Namespaces
kubectl apply -f 01-namespace.yaml

# Paso 2: Infraestructura base (sin dependencias)
kubectl apply -f 02-postgresql-statefulset.yaml
kubectl apply -f 03-qdrant-statefulset.yaml
kubectl apply -f 04-minio-statefulset.yaml
kubectl apply -f 06-redis-deployment.yaml

# Esperar que estén ready
kubectl wait --for=condition=ready pod -l app=postgres -n codeflowx-govern-systems --timeout=300s
kubectl wait --for=condition=ready pod -l app=qdrant -n codeflowx-govern-systems --timeout=300s

# Paso 3: OpenSearch (más lento arrancar)
kubectl apply -f 05-opensearch-statefulset.yaml

# Esperar OpenSearch ready
kubectl wait --for=condition=ready pod -l app=opensearch -n codeflowx-govern-systems --timeout=600s

# Paso 4: Inicializar (collections, buckets, índices)
kubectl exec -n codeflowx-govern-systems qdrant-0 -- /scripts/init_qdrant_collections.py
# ... etc

# Paso 5: Deploy aplicaciones (en namespace codeflowx-govern)
kubectl apply -f 07-backend-java-deployment.yaml
kubectl apply -f 08-python-microservices-deployments.yaml
```

---

## 🌐 INGRESS (Exponer servicios)

```yaml
# 07-ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: codeflowx-infrastructure-ingress
  namespace: codeflowx-govern-systems
  annotations:
    kubernetes.io/ingress.class: nginx
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  tls:
  - hosts:
    - dashboards.codeflowx.example.com
    - minio-console.codeflowx.example.com
    secretName: codeflowx-infra-tls
  rules:
  # OpenSearch Dashboards
  - host: dashboards.codeflowx.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: opensearch-dashboards
            port:
              number: 5601
  # MinIO Console
  - host: minio-console.codeflowx.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: minio
            port:
              number: 9001
```

**Access:**
- OpenSearch Dashboards: https://dashboards.codeflowx.example.com
- MinIO Console: https://minio-console.codeflowx.example.com

---

## 📊 MONITORING

```bash
# Prometheus ServiceMonitor (si tienes Prometheus Operator)
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: infrastructure-metrics
  namespace: codeflowx-govern-systems
spec:
  selector:
    matchLabels:
      app: infrastructure
  endpoints:
  - port: metrics
    interval: 30s
```

**Métricas disponibles:**
- PostgreSQL: postgres-exporter
- Qdrant: /metrics endpoint (built-in)
- MinIO: /minio/v2/metrics/cluster (built-in)
- OpenSearch: /_prometheus/metrics (plugin)
- Redis: redis-exporter

---

## ✅ VERIFICACIÓN POST-INSTALACIÓN

```bash
# Script verificación completo
./scripts/verify-infrastructure.sh

# Output esperado:
# ✅ Namespace codeflowx-govern-systems: EXISTS
# ✅ PostgreSQL: RUNNING (1/1 ready)
# ✅ Qdrant: RUNNING (1/1 ready)
# ✅ MinIO: RUNNING (1/1 ready)
# ✅ OpenSearch: RUNNING (1/1 ready)
# ✅ OpenSearch Dashboards: RUNNING (1/1 ready)
# ✅ Redis: RUNNING (1/1 ready)
#
# ✅ PostgreSQL health: OK
# ✅ Qdrant health: OK
# ✅ MinIO health: OK
# ✅ OpenSearch cluster: GREEN
#
# 🎉 Infrastructure ready!
```

---

**Última actualización:** 5 Noviembre 2025  
**Namespace infraestructura:** `codeflowx-govern-systems`  
**Namespace aplicaciones:** `codeflowx-govern`