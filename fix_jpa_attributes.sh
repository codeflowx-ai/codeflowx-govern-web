#!/bin/bash

# Script para corregir TODOS los atributos incorrectos de entidades JPA
echo "🔧 Corrigiendo atributos incorrectos de entidades JPA..."

# Función para corregir atributos en un archivo
fix_jpa_attributes() {
    local file="$1"
    echo "Corrigiendo atributos JPA en: $file"
    
    # AgentHealth - campos incorrectos
    sed -i 's/setIdxagent(/setFkidxagent(/g' "$file"
    sed -i 's/getAgtcheckedat(/getAgtcreatedat(/g' "$file"
    sed -i 's/setAgtcheckedat(/setAgtcreatedat(/g' "$file"
    
    # AgentDeployment - campos incorrectos
    sed -i 's/deployment\.setIdxagent(/deployment.setFkidxagent(/g' "$file"
    
    # AgentMonitoring - campos incorrectos
    sed -i 's/getAgtexecutionstatus(/getAgtstatus(/g' "$file"
    sed -i 's/getAgtexecutiontime(/getAgtresponsetime(/g' "$file"
    
    # Constructor BusinessService incorrecto - corregir en AgentsDetailViewModel
    if [[ "$file" == *"AgentsDetailViewModel.java" ]]; then
        sed -i 's/new BusinessService(dao)/new BusinessService(dao)/g' "$file"
    fi
}

# Corregir todos los ViewModels
echo "📁 Corrigiendo todos los ViewModels..."
find /mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel -name "*.java" -type f | while read file; do
    fix_jpa_attributes "$file"
done

echo "✅ Corrección de atributos JPA completada!"

