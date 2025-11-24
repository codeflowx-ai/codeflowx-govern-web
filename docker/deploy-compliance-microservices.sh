#!/bin/bash

# Script de despliegue para microservicios Python de Compliance EU AI Act
# Fecha: Diciembre 2025

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Directorio base
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose-compliance-microservices.yml"
GIT_BASE="/mnt/c/Users/ManuelGonzalez/git"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Despliegue Microservicios Compliance${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Verificar Docker y Docker Compose
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker no está instalado${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo -e "${RED}❌ Docker Compose no está instalado${NC}"
    exit 1
fi

# Verificar que existe el archivo docker-compose
if [ ! -f "$COMPOSE_FILE" ]; then
    echo -e "${RED}❌ No se encuentra docker-compose-compliance-microservices.yml${NC}"
    exit 1
fi

# Función para verificar si un microservicio tiene Dockerfile
check_dockerfile() {
    local service_name=$1
    local service_path="${GIT_BASE}/${service_name}"
    
    if [ -f "${service_path}/Dockerfile" ]; then
        echo -e "${GREEN}✅${NC} ${service_name} - Dockerfile encontrado"
        return 0
    else
        echo -e "${YELLOW}⚠️${NC}  ${service_name} - Dockerfile NO encontrado"
        return 1
    fi
}

# Verificar Dockerfiles de todos los microservicios
echo -e "${YELLOW}Verificando Dockerfiles...${NC}"
echo ""

MISSING_DOCKERFILES=()

check_dockerfile "leka-bias-detection-service" || MISSING_DOCKERFILES+=("leka-bias-detection-service")
check_dockerfile "leka-llm-evaluation" || MISSING_DOCKERFILES+=("leka-llm-evaluation")
check_dockerfile "leka-prompt-governance" || MISSING_DOCKERFILES+=("leka-prompt-governance")
check_dockerfile "leka-rag-evaluation" || MISSING_DOCKERFILES+=("leka-rag-evaluation")
check_dockerfile "leka-agent-monitoring" || MISSING_DOCKERFILES+=("leka-agent-monitoring")
check_dockerfile "leka-model-wrapper" || MISSING_DOCKERFILES+=("leka-model-wrapper")
check_dockerfile "leka-adversarial-robustness" || MISSING_DOCKERFILES+=("leka-adversarial-robustness")
check_dockerfile "leka-technical-documentation-generator" || MISSING_DOCKERFILES+=("leka-technical-documentation-generator")
check_dockerfile "leka-conformity-assessment" || MISSING_DOCKERFILES+=("leka-conformity-assessment")
check_dockerfile "leka-eu-declaration-generator" || MISSING_DOCKERFILES+=("leka-eu-declaration-generator")
check_dockerfile "leka-llm-interpreter" || MISSING_DOCKERFILES+=("leka-llm-interpreter")
check_dockerfile "leka-fria-generator" || MISSING_DOCKERFILES+=("leka-fria-generator")
check_dockerfile "leka-copyright-compliance" || MISSING_DOCKERFILES+=("leka-copyright-compliance")
check_dockerfile "leka-server-serving-evaluation" || MISSING_DOCKERFILES+=("leka-server-serving-evaluation")

echo ""

if [ ${#MISSING_DOCKERFILES[@]} -gt 0 ]; then
    echo -e "${YELLOW}⚠️  Microservicios sin Dockerfile:${NC}"
    for service in "${MISSING_DOCKERFILES[@]}"; do
        echo -e "   - ${service}"
    done
    echo ""
    echo -e "${YELLOW}Se generarán Dockerfiles estándar para estos microservicios...${NC}"
    echo ""
fi

# Función para generar Dockerfile estándar
generate_standard_dockerfile() {
    local service_name=$1
    local service_path="${GIT_BASE}/${service_name}"
    local port=$2
    
    mkdir -p "${service_path}"
    
    cat > "${service_path}/Dockerfile" << EOF
# Dockerfile para ${service_name}
# Generado automáticamente - EU AI Act Compliance

FROM python:3.11-slim

LABEL maintainer="CodeFlowX Governance Team"
LABEL description="${service_name} - EU AI Act Compliance Microservice"
LABEL version="1.0.0"

WORKDIR /app

# Install system dependencies
RUN apt-get update && apt-get install -y \\
    gcc \\
    g++ \\
    curl \\
    && rm -rf /var/lib/apt/lists/*

# Copy requirements
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir --upgrade pip && \\
    pip install --no-cache-dir -r requirements.txt

# Copy application code
COPY . .

# Create non-root user for security
RUN useradd -m -u 1000 appuser && \\
    chown -R appuser:appuser /app

USER appuser

# Expose port
EXPOSE ${port}

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \\
    CMD curl -f http://localhost:${port}/health || exit 1

# Run application
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "${port}", "--workers", "2"]
EOF
    
    echo -e "${GREEN}✅${NC} Dockerfile generado para ${service_name}"
}

# Generar Dockerfiles faltantes
if [ ${#MISSING_DOCKERFILES[@]} -gt 0 ]; then
    echo -e "${YELLOW}Generando Dockerfiles estándar...${NC}"
    echo ""
    
    # Mapeo de servicios a puertos
    declare -A SERVICE_PORTS=(
        ["leka-bias-detection-service"]="8001"
        ["leka-llm-evaluation"]="8002"
        ["leka-prompt-governance"]="8003"
        ["leka-rag-evaluation"]="8004"
        ["leka-agent-monitoring"]="8005"
        ["leka-model-wrapper"]="8006"
        ["leka-adversarial-robustness"]="8007"
        ["leka-technical-documentation-generator"]="8008"
        ["leka-conformity-assessment"]="8009"
        ["leka-eu-declaration-generator"]="8010"
        ["leka-llm-interpreter"]="8011"
        ["leka-fria-generator"]="8012"
        ["leka-copyright-compliance"]="8013"
        ["leka-server-serving-evaluation"]="8014"
    )
    
    for service in "${MISSING_DOCKERFILES[@]}"; do
        port=${SERVICE_PORTS[$service]}
        if [ -z "$port" ]; then
            port="8000"
        fi
        generate_standard_dockerfile "$service" "$port"
    done
    echo ""
fi

# Verificar que requirements.txt existe para cada servicio
echo -e "${YELLOW}Verificando requirements.txt...${NC}"
echo ""

MISSING_REQUIREMENTS=()

SERVICES=(
    "leka-bias-detection-service"
    "leka-llm-evaluation"
    "leka-prompt-governance"
    "leka-rag-evaluation"
    "leka-agent-monitoring"
    "leka-model-wrapper"
    "leka-adversarial-robustness"
    "leka-technical-documentation-generator"
    "leka-conformity-assessment"
    "leka-eu-declaration-generator"
    "leka-llm-interpreter"
    "leka-fria-generator"
    "leka-copyright-compliance"
    "leka-server-serving-evaluation"
)

for service in "${SERVICES[@]}"; do
    service_path="${GIT_BASE}/${service}"
    if [ -f "${service_path}/requirements.txt" ]; then
        echo -e "${GREEN}✅${NC} ${service} - requirements.txt encontrado"
    else
        echo -e "${YELLOW}⚠️${NC}  ${service} - requirements.txt NO encontrado"
        MISSING_REQUIREMENTS+=("$service")
    fi
done

echo ""

# Generar requirements.txt básico si falta
if [ ${#MISSING_REQUIREMENTS[@]} -gt 0 ]; then
    echo -e "${YELLOW}Generando requirements.txt básico...${NC}"
    echo ""
    
    for service in "${MISSING_REQUIREMENTS[@]}"; do
        service_path="${GIT_BASE}/${service}"
        mkdir -p "${service_path}"
        
        cat > "${service_path}/requirements.txt" << EOF
# Requirements para ${service}
# EU AI Act Compliance Microservice

fastapi==0.104.1
uvicorn[standard]==0.24.0
pydantic==2.5.0
pydantic-settings==2.1.0
python-multipart==0.0.6
structlog==23.2.0
prometheus-client==0.19.0
httpx==0.25.2
EOF
        
        echo -e "${GREEN}✅${NC} requirements.txt generado para ${service}"
    done
    echo ""
fi

# Verificar que main.py existe
echo -e "${YELLOW}Verificando main.py...${NC}"
echo ""

MISSING_MAIN=()

for service in "${SERVICES[@]}"; do
    service_path="${GIT_BASE}/${service}"
    if [ -f "${service_path}/main.py" ] || [ -f "${service_path}/app/main.py" ]; then
        if [ -f "${service_path}/main.py" ]; then
            echo -e "${GREEN}✅${NC} ${service} - main.py encontrado (raíz)"
        else
            echo -e "${GREEN}✅${NC} ${service} - app/main.py encontrado"
        fi
    else
        echo -e "${RED}❌${NC} ${service} - main.py NO encontrado (ni en raíz ni en app/)"
        MISSING_MAIN+=("$service")
    fi
done

echo ""

if [ ${#MISSING_MAIN[@]} -gt 0 ]; then
    echo -e "${RED}❌ ERROR: Los siguientes microservicios no tienen main.py:${NC}"
    for service in "${MISSING_MAIN[@]}"; do
        echo -e "   - ${service}"
    done
    echo ""
    echo -e "${YELLOW}No se puede continuar sin main.py. Por favor, crea los archivos main.py primero.${NC}"
    exit 1
fi

# Desplegar con docker-compose
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Iniciando despliegue...${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Usar docker compose (nuevo) o docker-compose (antiguo)
if docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
else
    COMPOSE_CMD="docker-compose"
fi

cd "$SCRIPT_DIR"

echo -e "${YELLOW}Construyendo imágenes...${NC}"
$COMPOSE_CMD -f docker-compose-compliance-microservices.yml build

echo ""
echo -e "${YELLOW}Iniciando servicios...${NC}"
$COMPOSE_CMD -f docker-compose-compliance-microservices.yml up -d

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Despliegue completado${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Verificar estado de servicios
echo -e "${YELLOW}Verificando estado de servicios...${NC}"
echo ""

sleep 5

$COMPOSE_CMD -f docker-compose-compliance-microservices.yml ps

echo ""
echo -e "${GREEN}Microservicios desplegados:${NC}"
echo ""
echo "  Puerto 8001: leka-bias-detection-service"
echo "  Puerto 8002: leka-llm-evaluation"
echo "  Puerto 8003: leka-prompt-governance"
echo "  Puerto 8004: leka-rag-evaluation"
echo "  Puerto 8005: leka-agent-monitoring"
echo "  Puerto 8006: leka-model-wrapper"
echo "  Puerto 8007: leka-adversarial-robustness"
echo "  Puerto 8008: leka-technical-documentation-generator"
echo "  Puerto 8009: leka-conformity-assessment"
echo "  Puerto 8010: leka-eu-declaration-generator"
echo "  Puerto 8011: leka-llm-interpreter"
echo "  Puerto 8012: leka-fria-generator"
echo "  Puerto 8013: leka-copyright-compliance"
echo "  Puerto 8014: leka-server-serving-evaluation"
echo ""

echo -e "${GREEN}Para ver logs:${NC}"
echo "  $COMPOSE_CMD -f docker-compose-compliance-microservices.yml logs -f [servicio]"
echo ""
echo -e "${GREEN}Para detener:${NC}"
echo "  $COMPOSE_CMD -f docker-compose-compliance-microservices.yml down"
echo ""

