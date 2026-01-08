# Ubicación del Servicio Python - Dataset Standardization

**Fecha:** Diciembre 2025
**Estado:** ✅ Microservicio Python Completo

---

## 📍 UBICACIÓN

El servicio Python de estandarización de datasets es un **microservicio independiente** ubicado en:

```
codeflowx-govern-web/dataset-standardization-service/
```

---

## 🏗️ ESTRUCTURA DEL MICROSERVICIO

```
codeflowx-govern-web/dataset-standardization-service/
├── main.py                          # Aplicación FastAPI principal
├── requirements.txt                 # Dependencias Python
├── Dockerfile                       # Imagen Docker
├── README.md                        # Documentación
├── services/
│   ├── __init__.py
│   └── dataset_standardization.py  # Servicio de estandarización
└── api/
    ├── __init__.py
    └── dataset_endpoints.py        # Endpoints REST
```

---

## 🔌 CONFIGURACIÓN

### Puerto
- **Puerto:** 8002
- **URL Base:** `http://localhost:8002` (desarrollo)
- **URL Kubernetes:** `http://dataset-standardization-service:8002`

### Variables de Entorno

```bash
MINIO_ENDPOINT=localhost:9000
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
MINIO_SECURE=false
STORAGE_BUCKET=datasets
SERVICE_PORT=8002
```

---

## 📡 ENDPOINTS

### Health Check
```
GET /health
```

### Obtener Dataset Estandarizado
```
GET /api/v1/datasets/{dataset_id}/standardized?format=parquet
```

### Obtener Schema
```
GET /api/v1/datasets/{dataset_id}/schema
```

### Obtener Estadísticas
```
GET /api/v1/datasets/{dataset_id}/statistics
```

---

## 🔗 INTEGRACIÓN CON BACKEND JAVA

El backend Java (BFF) llama a este microservicio para:

1. **Estandarizar datasets** descargados de HuggingFace/Kaggle
2. **Obtener datasets estandarizados** para análisis
3. **Obtener schemas** para validación

### Ejemplo de Llamada desde Java

```java
@Autowired
private WebClient webClient;

public Mono<DatasetStandardizationResult> standardizeDataset(Long datasetId) {
    return webClient.post()
        .uri("http://dataset-standardization-service:8002/api/v1/datasets/{id}/standardize", datasetId)
        .retrieve()
        .bodyToMono(DatasetStandardizationResult.class);
}
```

---

## 🐳 DOCKER

### Construir Imagen

```bash
cd codeflowx-govern-web/dataset-standardization-service
docker build -t dataset-standardization-service:1.0.0 .
```

### Ejecutar

```bash
docker run -p 8002:8002 \
  -e MINIO_ENDPOINT=localhost:9000 \
  -e MINIO_ACCESS_KEY=minioadmin \
  -e MINIO_SECRET_KEY=minioadmin \
  dataset-standardization-service:1.0.0
```

---

## ✅ CARACTERÍSTICAS

- ✅ **Microservicio independiente** con FastAPI
- ✅ **Puerto dedicado:** 8002
- ✅ **Integración con MinIO/S3**
- ✅ **Estandarización a Parquet**
- ✅ **API REST completa**
- ✅ **Health checks**
- ✅ **Dockerizado**

---

## 📝 NOTAS

- Este es un **microservicio Python independiente**, no parte del backend Java
- Se comunica con el backend Java vía REST
- Los microservicios Python de análisis (calidad, sesgos) consumen los datasets estandarizados desde este servicio

---

**Documento creado:** Diciembre 2025
