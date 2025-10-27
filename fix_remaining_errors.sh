#!/bin/bash

# Script para corregir TODOS los errores restantes en ViewModels
echo "🔧 Corrigiendo errores restantes en ViewModels..."

# Función para corregir errores específicos
fix_remaining_errors() {
    local file="$1"
    echo "Corrigiendo errores en: $file"
    
    # Corregir método add() por addCriteria()
    sed -i 's/criterias\.add(/criterias.addCriteria(/g' "$file"
    
    # Corregir getAgtinput() por getAgtdescription()
    sed -i 's/getAgtinput()/getAgtdescription()/g' "$file"
    
    # Corregir setAgtapprovercomments por setAgtapprovalnotes
    sed -i 's/setAgtapprovercomments(/setAgtapprovalnotes(/g' "$file"
    
    # Corregir BigDecimal.ROUND_HALF_UP por RoundingMode.HALF_UP
    sed -i 's/BigDecimal\.ROUND_HALF_UP/RoundingMode.HALF_UP/g' "$file"
    
    # Agregar import RoundingMode si no existe
    if ! grep -q "import java.math.RoundingMode;" "$file"; then
        sed -i '/import java.math.BigDecimal;/a import java.math.RoundingMode;' "$file"
    fi
}

# Corregir todos los ViewModels
echo "📁 Corrigiendo todos los ViewModels..."
find /mnt/e/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel -name "*.java" -type f | while read file; do
    fix_remaining_errors "$file"
done

echo "✅ Corrección de errores restantes completada!"

