#!/bin/bash
# ========================================
# SCRIPT MAESTRO: APLICAR PATCHES EU AI ACT
# Fecha: 2 nov 2025
# ========================================

# Configuración
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-codeflowx_govern}"
DB_USER="${DB_USER:-postgres}"

echo "=================================================="
echo "  APLICACIÓN PATCHES EU AI ACT COMPLIANCE"
echo "=================================================="
echo "Base de datos: $DB_NAME"
echo "Host: $DB_HOST:$DB_PORT"
echo "Usuario: $DB_USER"
echo ""

# Función para ejecutar un patch
ejecutar_patch() {
    local patch_file=$1
    local patch_name=$(basename $patch_file)
    
    echo "------------------------------------------------"
    echo "Aplicando: $patch_name"
    echo "------------------------------------------------"
    
    psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f $patch_file
    
    if [ $? -eq 0 ]; then
        echo "✓ $patch_name aplicado correctamente"
    else
        echo "✗ ERROR aplicando $patch_name"
        exit 1
    fi
    echo ""
}

# Obtener directorio del script
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Aplicar patches en orden
ejecutar_patch "$SCRIPT_DIR/06_eu_ai_act_model_extensions.sql"
ejecutar_patch "$SCRIPT_DIR/07_eu_ai_act_project_extensions.sql"
ejecutar_patch "$SCRIPT_DIR/08_eu_ai_act_evaluation_extensions.sql"
ejecutar_patch "$SCRIPT_DIR/09_eu_ai_act_immutable_logs_table.sql"
ejecutar_patch "$SCRIPT_DIR/10_eu_ai_act_fria_assessment_table.sql"

echo "=================================================="
echo "  TODOS LOS PATCHES APLICADOS CORRECTAMENTE"
echo "=================================================="
echo ""
echo "Resumen de cambios:"
echo "  - MODMODELS: 13 campos nuevos (Art. 6, 11, 15, 51)"
echo "  - PRJPROJECTS: 11 campos nuevos (Art. 5, 6, 49, Anexos I-III)"
echo "  - GOVMODELEVALUATIONS: 7 campos nuevos (Art. 15.4, 15.5)"
echo "  - IMLIMMUTABLELOGS: Tabla nueva (Art. 19) con triggers"
echo "  - FRIAFUNDAMENTALRIGHTSASSESSMENTS: Tabla nueva (Art. 27)"
echo ""
echo "Próximos pasos:"
echo "  1. Verificar las entidades JPA en nocode.service.entitys"
echo "  2. Crear/actualizar servicios en codeflowx-govern-api"
echo "  3. Crear/actualizar ViewModels en suinsit.nova.web"
echo ""

exit 0

