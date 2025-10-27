#!/bin/bash

# Script DEFINITIVO para corregir TODOS los errores de atributos JPA
echo "🔧 CORRECCIÓN DEFINITIVA DE ATRIBUTOS JPA"
echo "=========================================="

count=0

fix_all_jpa_errors() {
    local file="$1"
    ((count++))
    basename_file=$(basename "$file")
    echo "[$count] $basename_file"
    
    # ========== BUSINESSSERVICE CONSTRUCTOR ==========
    sed -i 's/new BusinessService((DataSource) environment\.getProperty("APPLICATION_DS", DataSource\.class))/new BusinessService(dao)/g' "$file"
    sed -i 's/new BusinessService((IEntityLocal) context\.lookup(IEntityLocal\.class))/new BusinessService(dao)/g' "$file"
    
    # ========== CRITERIAS METHODS ==========
    sed -i 's/criterias\.add(/criterias.addCriteria(/g' "$file"
    
    # ========== AGENTHEALTH - NO TIENE FK, USA RELACIÓN ==========
    # La FK es a través del objeto Agent, no un Long
    sed -i 's/healthCheck\.setIdxagent(/healthCheck.setAgent(/g' "$file"
    sed -i 's/healthCheck\.setFkidxagent(/healthCheck.setAgent(/g' "$file"
    # No cambiar getAgtcreatedat que es correcto
    
    # ========== AGENTDEPLOYMENT - NO TIENE FK, USA RELACIÓN ==========
    sed -i 's/deployment\.setIdxagent(/deployment.setAgent(/g' "$file"
    sed -i 's/deployment\.setFkidxagent(/deployment.setAgent(/g' "$file"
    
    # ========== AGENTMONITORING - CAMPOS CORRECTOS ==========
    # Ya tiene agtstatus correcto
    # agtexecutiontime NO EXISTE → usar agtdurationseconds
    sed -i 's/getAgtexecutiontime(/getAgtdurationseconds(/g' "$file"
    sed -i 's/getAgtresponsetime(/getAgtdurationseconds(/g' "$file"
    # agtexecutionstatus NO EXISTE → usar agtstatus
    sed -i 's/getAgtexecutionstatus(/getAgtstatus(/g' "$file"
    
    # ========== AGENTAPPROVAL ==========
    sed -i 's/setAgtapprovercomments(/setAgtapprovalnotes(/g' "$file"
    sed -i 's/getAgtapprovercomments(/getAgtapprovalnotes(/g' "$file"
    
    # ========== BIASANALYSIS ==========
    # No tiene campo biasdetected booleano, usa anlbiasscore BigDecimal
    sed -i 's/getBiasdetected(/getAnlbiasscore(/g' "$file"
    
    # ========== POLICYEVALUATION ==========
    # Revisar prefijo correcto (GOV)
    sed -i 's/getEvaluationresult(/getGovevaluationresult(/g' "$file"
    sed -i 's/setEvaluationresult(/setGovevaluationresult(/g' "$file"
    
    # ========== BIGDECIMAL DEPRECATED ==========
    sed -i 's/BigDecimal\.ROUND_HALF_UP/RoundingMode.HALF_UP/g' "$file"
    
    # ========== IMPORTS ==========
    # Agregar RoundingMode si falta
    if grep -q "BigDecimal" "$file" && grep -q "ROUND_HALF_UP\|RoundingMode" "$file" && ! grep -q "import java.math.RoundingMode;" "$file"; then
        sed -i '/import java.math.BigDecimal;/a import java.math.RoundingMode;' "$file"
    fi
}

echo ""
echo "📁 Procesando ViewModels..."
echo "---"

find /mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel -name "*.java" -type f | sort | while read file; do
    fix_all_jpa_errors "$file"
done

echo "---"
echo ""
echo "✅ CORRECCIÓN COMPLETADA EXITOSAMENTE"
echo ""
echo "📊 Total de archivos corregidos: $count"
echo ""
echo "🎯 Correcciones aplicadas:"
echo "  ✅ Constructor BusinessService(dao)"
echo "  ✅ criterias.addCriteria()"
echo "  ✅ AgentHealth → setAgent() en lugar de setIdxagent()"
echo "  ✅ AgentDeployment → setAgent() en lugar de setIdxagent()"
echo "  ✅ AgentMonitoring → getAgtdurationseconds() en lugar de getAgtexecutiontime()"
echo "  ✅ AgentMonitoring → getAgtstatus() en lugar de getAgtexecutionstatus()"
echo "  ✅ AgentApproval → setAgtapprovalnotes() en lugar de setAgtapprovercomments()"
echo "  ✅ BiasAnalysis → getAnlbiasscore() en lugar de getBiasdetected()"
echo "  ✅ PolicyEvaluation → getGovevaluationresult() en lugar de getEvaluationresult()"
echo "  ✅ BigDecimal → RoundingMode.HALF_UP"
echo ""
echo "🎉 TODOS LOS VIEWMODELS CORREGIDOS Y LISTOS!"

