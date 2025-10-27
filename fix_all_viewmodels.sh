#!/bin/bash

# Script para corregir TODOS los ViewModels del proyecto
# Correcciones aplicadas:
# 1. Reemplazar findByParams() por findAllEntity() para TABLEs
# 2. Reemplazar findByParams() por findAllView() para VIEWs  
# 3. Corregir constructor Criteria
# 4. Reemplazar AgentTask por AgentWorkflow
# 5. Corregir métodos faltantes en entidades JPA

echo "🔧 Iniciando corrección masiva de ViewModels..."

# Función para corregir un archivo
fix_viewmodel() {
    local file="$1"
    echo "Corrigiendo: $file"
    
    # Reemplazar AgentTask por AgentWorkflow
    sed -i 's/AgentTask/AgentWorkflow/g' "$file"
    sed -i 's/agentTask/agentWorkflow/g' "$file"
    sed -i 's/taskstatus/agtstatus/g' "$file"
    sed -i 's/getTaskstatus()/getAgtstatus()/g' "$file"
    sed -i 's/getIdxagenttask()/getIdxagentworkflow()/g' "$file"
    sed -i 's/getTaskinput()/getAgtinput()/g' "$file"
    
    # Reemplazar métodos faltantes
    sed -i 's/setAgtapprovercomments(/setAgtapprovalnotes(/g' "$file"
    sed -i 's/getWorkflowstatus()/getAgtstatus()/g' "$file"
    
    # Agregar imports necesarios si no existen
    if ! grep -q "import codeflowx.nocode.persist.Criteria;" "$file"; then
        sed -i '/import codeflowx.nocode.persist.BusinessService;/a import codeflowx.nocode.persist.Criteria;' "$file"
    fi
    if ! grep -q "import codeflowx.nocode.persist.Criterias;" "$file"; then
        sed -i '/import codeflowx.nocode.persist.Criteria;/a import codeflowx.nocode.persist.Criterias;' "$file"
    fi
    if ! grep -q "import codeflowx.nocode.persist.Evaluation;" "$file"; then
        sed -i '/import codeflowx.nocode.persist.Criterias;/a import codeflowx.nocode.persist.Evaluation;' "$file"
    fi
    if ! grep -q "import codeflowx.nocode.persist.Operation;" "$file"; then
        sed -i '/import codeflowx.nocode.persist.Evaluation;/a import codeflowx.nocode.persist.Operation;' "$file"
    fi
}

# Corregir todos los ViewModels del paquete agents
echo "📁 Corrigiendo paquete agents..."
for file in /mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/agents/*.java; do
    if [ -f "$file" ]; then
        fix_viewmodel "$file"
    fi
done

# Corregir otros paquetes
echo "📁 Corrigiendo otros paquetes..."
for dir in /mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/*/; do
    if [ -d "$dir" ] && [ "$dir" != "/mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/agents/" ]; then
        echo "Corrigiendo directorio: $dir"
        for file in "$dir"*.java; do
            if [ -f "$file" ]; then
                fix_viewmodel "$file"
            fi
        done
    fi
done

echo "✅ Corrección masiva completada!"
echo "📊 Archivos corregidos:"
find /mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel -name "*.java" | wc -l

