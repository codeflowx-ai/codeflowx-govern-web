#!/bin/bash

# Script FINAL para corregir TODOS los ViewModels
echo "🔧 CORRECCIÓN FINAL Y COMPLETA DE TODOS LOS VIEWMODELS"
echo "======================================================="

# Contador de archivos
count=0

# Función para corregir TODOS los problemas en un archivo
fix_all_problems() {
    local file="$1"
    ((count++))
    echo "[$count] Corrigiendo: $(basename $file)"
    
    # ========== CONSTRUCTOR BUSINESSSERVICE ==========
    # Incorrecto: new BusinessService((DataSource)...)
    # Correcto: new BusinessService(dao)
    sed -i 's/new BusinessService((DataSource) environment\.getProperty("APPLICATION_DS", DataSource\.class))/new BusinessService(dao)/g' "$file"
    sed -i 's/new BusinessService((IEntityLocal) context\.lookup(IEntityLocal\.class))/new BusinessService(dao)/g' "$file"
    
    # ========== MÉTODOS CRITERIAS ==========
    # Incorrecto: criterias.add(criteria)
    # Correcto: criterias.addCriteria(criteria)
    sed -i 's/criterias\.add(/criterias.addCriteria(/g' "$file"
    
    # ========== MÉTODOS FALTANTES EN ENTIDADES ==========
    # AgentHealth
    sed -i 's/setIdxagent(/setFkidxagent(/g' "$file"
    sed -i 's/getAgtcheckedat(/getAgtcreatedat(/g' "$file"
    sed -i 's/setAgtcheckedat(/setAgtcreatedat(/g' "$file"
    
    # AgentDeployment
    sed -i 's/deployment\.setIdxagent(/deployment.setFkidxagent(/g' "$file"
    
    # AgentMonitoring
    sed -i 's/getAgtexecutionstatus(/getAgtstatus(/g' "$file"
    sed -i 's/getAgtexecutiontime(/getAgtresponsetime(/g' "$file"
    
    # AgentApproval
    sed -i 's/setAgtapprovercomments(/setAgtapprovalnotes(/g' "$file"
    sed -i 's/getAgtapprovercomments(/getAgtapprovalnotes(/g' "$file"
    
    # BiasAnalysis
    sed -i 's/getBiasdetected(/getAnlbiasscore(/g' "$file"
    
    # PolicyEvaluation
    sed -i 's/getEvaluationresult(/getGovevaluationresult(/g' "$file"
    
    # ========== BIGDECIMAL DEPRECATED ==========
    sed -i 's/BigDecimal\.ROUND_HALF_UP/RoundingMode.HALF_UP/g' "$file"
    
    # Agregar import RoundingMode si usa BigDecimal y no tiene el import
    if grep -q "BigDecimal" "$file" && ! grep -q "import java.math.RoundingMode;" "$file"; then
        sed -i '/import java.math.BigDecimal;/a import java.math.RoundingMode;' "$file"
    fi
}

# ========== CORREGIR TODOS LOS VIEWMODELS ==========
echo ""
echo "📁 Procesando ViewModels..."
echo "---"

find /mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel -name "*.java" -type f | while read file; do
    fix_all_problems "$file"
done

echo "---"
echo "✅ CORRECCIÓN COMPLETADA"
echo ""
echo "📊 Total de archivos corregidos: $count"
echo ""
echo "🎯 Correcciones aplicadas:"
echo "  - Constructor BusinessService(dao)"
echo "  - criterias.addCriteria() en lugar de criterias.add()"
echo "  - Métodos corregidos en AgentHealth, AgentDeployment, AgentMonitoring"
echo "  - Métodos corregidos en AgentApproval, BiasAnalysis, PolicyEvaluation"
echo "  - BigDecimal.ROUND_HALF_UP → RoundingMode.HALF_UP"
echo ""
echo "✅ TODOS LOS VIEWMODELS CORREGIDOS!"

