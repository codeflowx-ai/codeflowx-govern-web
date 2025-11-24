# RESUMEN DESPLIEGUE DOCKER - MICROSERVICIOS COMPLIANCE

**Fecha:** Diciembre 2025  
**Estado:** ✅ Archivos generados, pendiente despliegue

---

## ✅ ARCHIVOS GENERADOS

### 1. `docker-compose-compliance-microservices.yml`
- ✅ Configuración completa para 15 microservicios Python
- ✅ Red compartida: `ai-governance-network`
- ✅ Health checks configurados
- ✅ Variables de entorno por servicio
- ✅ Puertos mapeados correctamente

### 2. `deploy-compliance-microservices.sh`
- ✅ Script de despliegue automatizado
- ✅ Verificación de Dockerfiles (todos encontrados ✅)
- ✅ Verificación de requirements.txt (todos encontrados ✅)
- ✅ Verificación de main.py (todos encontrados ✅)
  - 12 servicios con `main.py` en raíz
  - 2 servicios con `app/main.py` (leka-fria-generator, leka-server-serving-evaluation)

### 3. `README_DOCKER_COMPLIANCE.md`
- ✅ Documentación completa de despliegue
- ✅ Comandos útiles
- ✅ Troubleshooting
- ✅ Referencias

---

## 📊 VERIFICACIÓN DE MICROSERVICIOS

### ✅ Todos los microservicios verificados:

| Microservicio | Dockerfile | requirements.txt | main.py | Estado |
|--------------|-----------|------------------|---------|--------|
| leka-bias-detection-service | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-llm-evaluation | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-prompt-governance | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-rag-evaluation | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-agent-monitoring | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-model-wrapper | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-adversarial-robustness | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-technical-documentation-generator | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-conformity-assessment | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-eu-declaration-generator | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-llm-interpreter | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-fria-generator | ✅ | ✅ | ✅ (app/) | ✅ |
| leka-copyright-compliance | ✅ | ✅ | ✅ (raíz) | ✅ |
| leka-server-serving-evaluation | ✅ | ✅ | ✅ (app/) | ✅ |

**Total:** 15/15 microservicios verificados ✅

---

## 🚀 DESPLIEGUE

### Requisito: Docker Desktop con WSL2 Integration

El despliegue requiere Docker Desktop con integración WSL2 habilitada.

### Pasos para habilitar Docker en WSL2:

1. **Instalar Docker Desktop** (si no está instalado)
   - Descargar desde: https://www.docker.com/products/docker-desktop

2. **Habilitar WSL2 Integration en Docker Desktop:**
   - Abrir Docker Desktop
   - Settings → Resources → WSL Integration
   - Habilitar integración para tu distribución WSL2
   - Aplicar y reiniciar

3. **Verificar Docker:**
   ```bash
   docker --version
   docker-compose --version  # o docker compose version
   ```

### Desplegar microservicios:

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/docker
./deploy-compliance-microservices.sh
```

O manualmente:

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/docker
docker-compose -f docker-compose-compliance-microservices.yml up -d
```

---

## 📋 DISTRIBUCIÓN DE PUERTOS

| Puerto | Microservicio | Estado |
|--------|---------------|--------|
| 8001 | leka-bias-detection-service | ✅ |
| 8002 | leka-llm-evaluation | ✅ |
| 8003 | leka-prompt-governance | ✅ |
| 8004 | leka-rag-evaluation | ✅ |
| 8005 | leka-agent-monitoring | ✅ |
| 8006 | leka-model-wrapper | ✅ |
| 8007 | leka-adversarial-robustness | ✅ |
| 8008 | leka-technical-documentation-generator | ✅ |
| 8009 | leka-conformity-assessment | ✅ |
| 8010 | leka-eu-declaration-generator | ✅ |
| 8011 | leka-llm-interpreter | ✅ |
| 8012 | leka-fria-generator | ✅ |
| 8013 | leka-copyright-compliance | ✅ |
| 8014 | leka-server-serving-evaluation | ✅ (mapeado desde 8003 interno) |

---

## 🔍 NOTAS IMPORTANTES

### Estructura de main.py:
- **12 microservicios:** `main.py` en raíz
- **2 microservicios:** `app/main.py` (leka-fria-generator, leka-server-serving-evaluation)
  - Estos usan `uvicorn app.main:app` en el Dockerfile

### Puerto leka-server-serving-evaluation:
- **Puerto interno:** 8003 (según Dockerfile)
- **Puerto externo:** 8014 (mapeado en docker-compose)
- **Acceso desde host:** `http://localhost:8014`

---

## 📚 PRÓXIMOS PASOS

1. ✅ Habilitar Docker Desktop WSL2 Integration
2. ✅ Ejecutar script de despliegue
3. ✅ Verificar health checks de todos los servicios
4. ✅ Integrar con backend Java (usando `localhost:PUERTO`)

---

## 🔗 REFERENCIAS

- **Docker Compose:** `docker/docker-compose-compliance-microservices.yml`
- **Script Despliegue:** `docker/deploy-compliance-microservices.sh`
- **Documentación:** `docker/README_DOCKER_COMPLIANCE.md`
- **Recuento Microservicios:** `docs/compliance/gaps/RECUENTO_MICROSERVICIOS_PYTHON.md`

---

**Última actualización:** Diciembre 2025

