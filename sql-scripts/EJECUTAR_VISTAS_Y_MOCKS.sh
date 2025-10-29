#!/bin/bash

# ====================================================================
# Script para ejecutar todas las vistas SQL y mock data
# Para el proyecto CodeflowX Govern (MLOps)
# ====================================================================

echo "=========================================="
echo "EJECUTANDO VISTAS SQL Y MOCK DATA"
echo "=========================================="

# Configuración de la base de datos
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="codeflowx_govern"
DB_USER="postgres"

# Color para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para ejecutar SQL
execute_sql() {
    local file=$1
    echo -e "${BLUE}Ejecutando: $file${NC}"
    psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$file"
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Completado: $file${NC}"
    else
        echo -e "${RED}✗ Error en: $file${NC}"
        exit 1
    fi
    echo ""
}

# 1. EJECUTAR VISTAS SQL
echo "=========================================="
echo "PASO 1: Ejecutando Vistas SQL"
echo "=========================================="

execute_sql "views/models_views.sql"
execute_sql "views/governance_views.sql"
execute_sql "views/prompts_views.sql"
execute_sql "views/rag_views.sql"
execute_sql "views/providers_views.sql"
execute_sql "views/infrastructure_views.sql"
execute_sql "views/training_views.sql"

# 2. EJECUTAR MOCK DATA
echo "=========================================="
echo "PASO 2: Ejecutando Mock Data"
echo "=========================================="

execute_sql "mock_data/08_models_extended_mock_data.sql"
execute_sql "mock_data/09_governance_mock_data.sql"
execute_sql "mock_data/10_prompts_mock_data.sql"
execute_sql "mock_data/11_rag_systems_mock_data.sql"
execute_sql "mock_data/12_providers_mock_data.sql"
execute_sql "mock_data/13_infrastructure_mock_data.sql"

echo "=========================================="
echo "✅ COMPLETADO - Todas las vistas y mock data ejecutados"
echo "=========================================="
echo ""
echo "Vistas creadas:"
echo "  - training_overview"
echo "  - training_metrics_summary"
echo "  - models_overview + models_metrics_summary"
echo "  - governance_overview + governance_metrics_summary"
echo "  - prompts_overview + prompts_metrics_summary"
echo "  - rag_overview + rag_metrics_summary"
echo "  - providers_overview + providers_metrics_summary"
echo "  - infrastructure_overview + infrastructure_metrics_summary"
echo ""
echo "Mock data insertados:"
echo "  - 50+ modelos"
echo "  - 25 políticas de governance"
echo "  - 20 prompts"
echo "  - 15 sistemas RAG"
echo "  - 10 proveedores"
echo "  - Recursos de infraestructura"
echo ""

