# DOCKER - MICROSERVICIOS PYTHON COMPLIANCE EU AI ACT

**Fecha:** Diciembre 2025  
**Objetivo:** Despliegue completo de todos los microservicios Python de compliance

---

## 📋 ARCHIVOS INCLUIDOS

1. **`docker-compose-compliance-microservices.yml`**
   - Configuración Docker Compose para 15 microservicios Python
   - Red compartida: `ai-governance-network`
   - Health checks configurados
   - Variables de entorno por servicio

2. **`deploy-compliance-microservices.sh`**
   - Script de despliegue automatizado
   - Verificación de Dockerfiles
   - Generación automática de Dockerfiles estándar
   - Verificación de requirements.txt y main.py
   - Despliegue con docker-compose

---

## 🚀 DESPLIEGUE RÁPIDO

### Opción 1: Script Automatizado (Recomendado)

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/docker
./deploy-compliance-microservices.sh
```

### Opción 2: Docker Compose Manual

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/docker
docker-compose -f docker-compose-compliance-microservices.yml up -d
```

---

## 📊 MICROSERVICIOS INCLUIDOS

### Microservicios Existentes (7):
1. **leka-bias-detection-service** (8001)
2. **leka-llm-evaluation** (8002)
3. **leka-prompt-governance** (8003)
4. **leka-rag-evaluation** (8004)
5. **leka-agent-monitoring** (8005)
6. **leka-model-wrapper** (8006)
7. **leka-llm-interpreter** (8011)

### Microservicios Nuevos (7):
8. **leka-adversarial-robustness** (8007)
9. **leka-technical-documentation-generator** (8008)
10. **leka-conformity-assessment** (8009)
11. **leka-eu-declaration-generator** (8010)
12. **leka-fria-generator** (8012)
13. **leka-copyright-compliance** (8013)

### Microservicio Adicional (1):
14. **leka-server-serving-evaluation** (8014)

**Total:** 15 microservicios Python

---

## 🔧 REQUISITOS PREVIOS

### 1. Docker y Docker Compose
```bash
# Verificar instalación
docker --version
docker-compose --version  # o docker compose version
```

### 2. Estructura de Directorios
Los microservicios deben estar en:
```
/mnt/c/Users/ManuelGonzalez/git/
├── leka-bias-detection-service/
├── leka-llm-evaluation/
├── leka-prompt-governance/
├── leka-rag-evaluation/
├── leka-agent-monitoring/
├── leka-model-wrapper/
├── leka-adversarial-robustness/
├── leka-technical-documentation-generator/
├── leka-conformity-assessment/
├── leka-eu-declaration-generator/
├── leka-llm-interpreter/
├── leka-fria-generator/
├── leka-copyright-compliance/
└── leka-server-serving-evaluation/
```

### 3. Archivos Requeridos por Microservicio
Cada microservicio debe tener:
- ✅ `Dockerfile` (se genera automáticamente si falta)
- ✅ `requirements.txt` (se genera básico si falta)
- ✅ `main.py` (requerido, no se genera)

---

## 📝 FUNCIONALIDADES DEL SCRIPT

### Verificaciones Automáticas:
1. ✅ Verifica Docker y Docker Compose instalados
2. ✅ Verifica existencia de Dockerfiles
3. ✅ Genera Dockerfiles estándar si faltan
4. ✅ Verifica existencia de requirements.txt
5. ✅ Genera requirements.txt básico si falta
6. ✅ Verifica existencia de main.py (requerido)
7. ✅ Construye imágenes Docker
8. ✅ Inicia servicios en modo detached

### Generación Automática:
- **Dockerfile estándar:** Python 3.11-slim, FastAPI, uvicorn, health checks
- **requirements.txt básico:** FastAPI, uvicorn, pydantic, structlog, prometheus-client

---

## 🎯 COMANDOS ÚTILES

### Ver estado de servicios:
```bash
docker-compose -f docker-compose-compliance-microservices.yml ps
```

### Ver logs de un servicio:
```bash
docker-compose -f docker-compose-compliance-microservices.yml logs -f leka-bias-detection-service
```

### Ver logs de todos los servicios:
```bash
docker-compose -f docker-compose-compliance-microservices.yml logs -f
```

### Detener todos los servicios:
```bash
docker-compose -f docker-compose-compliance-microservices.yml down
```

### Detener y eliminar volúmenes:
```bash
docker-compose -f docker-compose-compliance-microservices.yml down -v
```

### Reconstruir un servicio específico:
```bash
docker-compose -f docker-compose-compliance-microservices.yml build leka-bias-detection-service
docker-compose -f docker-compose-compliance-microservices.yml up -d leka-bias-detection-service
```

### Verificar health de un servicio:
```bash
curl http://localhost:8001/health  # leka-bias-detection-service
curl http://localhost:8002/health  # leka-llm-evaluation
# ... etc
```

---

## 🔍 TROUBLESHOOTING

### Error: "Cannot connect to Docker daemon"
```bash
# Verificar que Docker está corriendo
sudo systemctl status docker  # Linux
# O iniciar Docker Desktop (Windows/Mac)
```

### Error: "Dockerfile not found"
El script genera Dockerfiles automáticamente. Si persiste:
```bash
# Verificar que el directorio del microservicio existe
ls -la /mnt/c/Users/ManuelGonzalez/git/leka-bias-detection-service/
```

### Error: "main.py not found"
Este archivo es requerido y no se genera automáticamente. Debe existir en cada microservicio.

### Error: "Port already in use"
```bash
# Verificar qué proceso usa el puerto
sudo lsof -i :8001  # Linux
netstat -ano | findstr :8001  # Windows

# Detener servicio conflictivo o cambiar puerto en docker-compose.yml
```

### Servicio no responde a health check
```bash
# Ver logs del servicio
docker-compose -f docker-compose-compliance-microservices.yml logs leka-bias-detection-service

# Verificar que el servicio está corriendo
docker ps | grep leka-bias-detection-service
```

---

## 🌐 RED Y CONECTIVIDAD

### Red Docker:
- **Nombre:** `ai-governance-network`
- **Tipo:** Bridge
- **Servicios:** Todos los microservicios están en la misma red

### Comunicación entre servicios:
Los servicios pueden comunicarse usando el nombre del contenedor:
```python
# Ejemplo: desde leka-fria-generator llamar a leka-bias-detection-service
url = "http://leka-bias-detection-service:8001/api/tabular/analyze-bias"
```

### Acceso desde host:
Desde el host (Java backend), usar `localhost:PUERTO`:
```java
String url = "http://localhost:8001/api/tabular/analyze-bias";
```

---

## 📊 MONITOREO

### Health Checks:
Todos los servicios tienen health checks configurados:
- **Interval:** 30 segundos
- **Timeout:** 10 segundos
- **Retries:** 3
- **Start Period:** 10-30 segundos (depende del servicio)

### Verificar health de todos los servicios:
```bash
for port in 8001 8002 8003 8004 8005 8006 8007 8008 8009 8010 8011 8012 8013 8014; do
    echo "Puerto $port:"
    curl -s http://localhost:$port/health || echo "  ❌ No responde"
    echo ""
done
```

---

## 🔐 SEGURIDAD

### Usuario no-root:
Todos los Dockerfiles generados crean un usuario no-root (`appuser`, UID 1000) para ejecutar los servicios.

### Variables de entorno:
Las variables sensibles deben configurarse en `.env` o pasarse como variables de entorno:
```bash
export LOG_LEVEL=INFO
export MAX_FILE_SIZE_MB=1024
```

---

## 📚 REFERENCIAS

- **Documento de Recuento:** `docs/compliance/gaps/RECUENTO_MICROSERVICIOS_PYTHON.md`
- **Mapeo BPMN:** `docs/compliance/gaps/MAPEO_PROCESOS_BPMN_MICROSERVICIOS.md`
- **Mapeo ViewModels:** `docs/compliance/gaps/MAPEO_VIEWMODELS_SERVICES_MICROSERVICIOS_PARTE1.md`

---

**Última actualización:** Diciembre 2025

