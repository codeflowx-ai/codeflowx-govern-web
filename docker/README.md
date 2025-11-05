# 🐳 DOCKER SETUP - CODEFLOWX INFRASTRUCTURE

## 🎯 Quick Start

### **Opción 1: Docker Compose (Desarrollo/Testing)**

```bash
# 1. Clonar repo
git clone https://github.com/codeflowx-ai/codeflowx-govern.git
cd codeflowx-govern/docker

# 2. Crear .env
cp .env.example .env
# Editar .env con passwords reales

# 3. Start infrastructure
docker-compose -f docker-compose-infrastructure.yml up -d

# 4. Verificar health
docker-compose ps
docker-compose logs -f

# 5. Access services
# PostgreSQL:  localhost:5432
# Qdrant:      http://localhost:6333/dashboard
# MinIO:       http://localhost:9001 (console)
# OpenSearch:  http://localhost:9200
# Dashboards:  http://localhost:5601
# Ollama:      http://localhost:11434
```

**Tiempo setup:** 5-10 minutos (primera vez descarga imágenes ~15 GB)

---

### **Opción 2: Kubernetes (Producción)**

Ver: `../k8s/README.md`

---

## 📦 SERVICIOS INCLUIDOS

| Servicio | Puerto | UI/API | Descripción |
|----------|--------|--------|-------------|
| **PostgreSQL + TimescaleDB** | 5432 | - | Base datos relacional + time-series |
| **Qdrant** | 6333 (HTTP), 6334 (gRPC) | http://localhost:6333/dashboard | Vector database (embeddings RAG) |
| **MinIO** | 9000 (API), 9001 (Console) | http://localhost:9001 | Object storage S3-compatible |
| **OpenSearch** | 9200 (API), 9600 (Perf) | - | Logs + auditoría + full-text search |
| **OpenSearch Dashboards** | 5601 | http://localhost:5601 | Kibana-like UI (dashboards compliance) |
| **Ollama** | 11434 | - | Modelos LLM locales (Llama, Mistral) |
| **Redis** | 6379 | - | Cache + kill-switch flags |
| **RabbitMQ** | 5672 (AMQP), 15672 (UI) | http://localhost:15672 | Message broker (eventos, workflows asíncronos) |

---

## 🔐 SEGURIDAD

**IMPORTANTE:** Cambiar passwords en `.env`:

```bash
# .env
POSTGRES_PASSWORD=secure_password_here_min_12_chars
MINIO_ROOT_PASSWORD=minio_password_min_8_chars
RABBITMQ_PASSWORD=rabbitmq_password_here
```

**Producción:**
- ✅ Usar secretos K8s (no .env)
- ✅ Habilitar OpenSearch security plugin
- ✅ Configurar TLS/SSL
- ✅ Network policies K8s

---

## 💾 STORAGE

**Volúmenes Docker:**

```bash
# Ver volúmenes
docker volume ls | grep codeflowx

# Backup PostgreSQL
docker exec codeflowx-postgres pg_dump -U codeflowx_user codeflowx > backup.sql

# Backup Qdrant (snapshot)
curl -X POST http://localhost:6333/snapshots

# Backup MinIO (mc client)
mc mirror minio/technical-docs ./backup/minio/technical-docs
```

---

## 🚀 INICIALIZACIÓN

**Después de `docker-compose up`, inicializar:**

```bash
# 1. Crear collections Qdrant
python3 scripts/init_qdrant_collections.py

# 2. Crear buckets MinIO
python3 scripts/init_minio_buckets.py

# 3. Crear índices OpenSearch
python3 scripts/init_opensearch_indices.py

# 4. Configure TimescaleDB hypertables
psql -h localhost -U codeflowx_user -d codeflowx < scripts/configure_timescaledb.sql

# 5. Seed datos demo (opcional)
./scripts/seed_everything_demo.sh
```

---

## 📊 RECURSOS

**Mínimo (desarrollo):**
- CPU: 8 cores
- RAM: 16 GB
- Disk: 50 GB SSD

**Recomendado (testing):**
- CPU: 16 cores
- RAM: 32 GB
- Disk: 200 GB SSD

**Producción:**
- CPU: 32+ cores
- RAM: 64+ GB
- Disk: 500 GB+ SSD NVMe

---

## 🔍 TROUBLESHOOTING

**OpenSearch no arranca:**
```bash
# Error: vm.max_map_count too low
sudo sysctl -w vm.max_map_count=262144
echo "vm.max_map_count=262144" | sudo tee -a /etc/sysctl.conf
```

**Qdrant out of memory:**
```bash
# Aumentar límite en docker-compose.yml
services:
  qdrant:
    deploy:
      resources:
        limits:
          memory: 8Gi  # Aumentar de 4Gi
```

**MinIO lento:**
```bash
# Verificar XFS filesystem (mejor que ext4 para object storage)
df -T | grep minio
```

---

## 📝 NOTAS

**OpenSearch vs Elasticsearch:**
- OpenSearch = Fork open source de Elasticsearch (versión 7.10.2)
- Creado por Amazon cuando Elastic cambió licencia
- 100% compatible APIs Elasticsearch
- Apache 2.0 license (sin vendor lock-in)

**¿Por qué no Solr?**
- Solr = Apache Lucene (competidor Elasticsearch)
- OpenSearch/Elasticsearch > Solr (más usado, mejor comunidad, RESTful)
- OpenSearch mejor para logs/time-series (vs Solr mejor para search tradicional)

