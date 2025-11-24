#!/bin/bash

# Script para subir artefactos ZKoss desde .m2 a Nexus
# Repositorio: http://51.210.165.41:8081/repository/zkoss/

# No usar set -e para que continúe aunque algunos artefactos fallen
set +e

# Configuración
NEXUS_URL="http://51.210.165.41:8081/repository/zkoss/"
NEXUS_ID="zkoss"
MAVEN_REPO="/mnt/c/Users/ManuelGonzalez/.m2/repository"
TARGET_LIB="/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/target/codeflowx.govern.web-1.2.0/WEB-INF/lib"
POM_FILE="/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/pom.xml"

# Colores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Credenciales de Nexus (desde pip.conf)
NEXUS_USER="${NEXUS_USER:-admin}"
NEXUS_PASS="${NEXUS_PASS:-qwerty-2025}"

echo "Usando credenciales: usuario=${NEXUS_USER}"
echo "Repositorio: ${NEXUS_URL}"

# Función para subir un artefacto
upload_artifact() {
    local group_id=$1
    local artifact_id=$2
    local version=$3
    local packaging=${4:-jar}
    local classifier=$5
    
    local group_path=$(echo "$group_id" | tr '.' '/')
    local artifact_dir="${MAVEN_REPO}/${group_path}/${artifact_id}/${version}"
    
    # Construir nombre del archivo
    local file_name="${artifact_id}-${version}"
    if [ -n "$classifier" ]; then
        file_name="${file_name}-${classifier}"
    fi
    file_name="${file_name}.${packaging}"
    
    local file_path="${artifact_dir}/${file_name}"
    
    # Si no está en .m2, buscar en target/lib
    if [ ! -f "$file_path" ] && [ -d "$TARGET_LIB" ]; then
        local target_file="${TARGET_LIB}/${file_name}"
        if [ -f "$target_file" ]; then
            file_path="$target_file"
            echo -e "${YELLOW}  Usando archivo de target/lib${NC}"
        fi
    fi
    
    # Verificar que el archivo existe
    if [ ! -f "$file_path" ]; then
        echo -e "${RED}✗ No encontrado: ${group_id}:${artifact_id}:${version}:${packaging}${NC}"
        return 1
    fi
    
    # Construir URL del artefacto en Nexus
    local group_path_url=$(echo "$group_id" | tr '.' '/')
    local artifact_url="${NEXUS_URL}${group_path_url}/${artifact_id}/${version}"
    
    # Construir nombre del archivo para la URL
    local file_name_url="${artifact_id}-${version}"
    if [ -n "$classifier" ]; then
        file_name_url="${file_name_url}-${classifier}"
    fi
    file_name_url="${file_name_url}.${packaging}"
    
    local full_url="${artifact_url}/${file_name_url}"
    
    echo -e "${GREEN}Subiendo: ${group_id}:${artifact_id}:${version}:${packaging}${NC}"
    
    # Subir el archivo principal usando curl
    local upload_result=$(curl -s -w "\n%{http_code}" -X PUT -u "${NEXUS_USER}:${NEXUS_PASS}" --upload-file "${file_path}" "${full_url}" 2>&1)
    local http_code=$(echo "$upload_result" | tail -1)
    
    if [ "$http_code" = "201" ] || [ "$http_code" = "200" ]; then
        echo -e "${GREEN}  ✓ ${file_name_url} subido exitosamente (HTTP ${http_code})${NC}"
        
        # Subir POM si existe y no es un POM
        if [ "$packaging" != "pom" ]; then
            local pom_path="${artifact_dir}/${artifact_id}-${version}.pom"
            if [ ! -f "$pom_path" ]; then
                # Buscar en .m2 si el archivo viene de target/lib
                local m2_pom="${MAVEN_REPO}/${group_path}/${artifact_id}/${version}/${artifact_id}-${version}.pom"
                if [ -f "$m2_pom" ]; then
                    pom_path="$m2_pom"
                fi
            fi
            
            if [ -f "$pom_path" ]; then
                local pom_url="${artifact_url}/${artifact_id}-${version}.pom"
                local pom_result=$(curl -s -w "\n%{http_code}" -X PUT -u "${NEXUS_USER}:${NEXUS_PASS}" --upload-file "${pom_path}" "${pom_url}" 2>&1)
                local pom_code=$(echo "$pom_result" | tail -1)
                if [ "$pom_code" = "201" ] || [ "$pom_code" = "200" ]; then
                    echo -e "${GREEN}  ✓ POM subido exitosamente (HTTP ${pom_code})${NC}"
                else
                    echo -e "${YELLOW}  ⚠ POM no subido (HTTP ${pom_code})${NC}"
                fi
            fi
        fi
        
        return 0
    else
        echo -e "${RED}  ✗ Error al subir (HTTP ${http_code})${NC}"
        echo "$upload_result" | head -5
        return 1
    fi
}

# Lista de artefactos ZKoss del pom.xml
declare -a zkoss_artifacts=(
    # ZK Core (10.0.2)
    "org.zkoss.common:zcommon:10.0.2:jar"
    "org.zkoss.common:zel:10.0.2:jar"
    "org.zkoss.common:zweb:10.0.2:jar"
    "org.zkoss.common:zweb-dsp:10.0.2:jar"
    "org.zkoss.zk:zhtml:10.0.2:jar"
    "org.zkoss.zk:zk:10.0.2:jar"
    "org.zkoss.zk:zkbind:10.0.2:jar"
    "org.zkoss.zk:zkplus:10.0.2:jar"
    "org.zkoss.zk:zkwebfragment:10.0.2:jar"
    "org.zkoss.zk:zul:10.0.2:jar"
    # ZK PE/EE (10.0.2)
    "org.zkoss.zk:zkex:10.0.2:jar"
    "org.zkoss.zk:zkmax:10.0.2:jar"
    "org.zkoss.zk:zml:10.0.2:jar"
    "org.zkoss.zk:zuti:10.0.2:jar"
    "org.zkoss.zk:za11y:10.0.2:jar"
    # Themes (10.0.2)
    "org.zkoss.themepack:theme-pack:10.0.2:jar"
    # Addons
    "org.zkoss.zkforge:ckez:4.21.0.0:jar"
    "org.zkoss.zkforge:gmapsz:4.0.3:jar"
    "org.zkoss.addons:rxzk:0.8.0:jar"
    "org.zkoss.chart:zkcharts:11.4.7.0:jar"
    "org.zkoss.pivot:pivottable:3.0.0:jar"
    "org.zkoss.poi:zpoi:3.9.17:jar"
    "org.zkoss.poi:zpoiex:3.9.17:jar"
    # Spring Boot Integration
    "org.zkoss.zkspringboot:zkspringboot-starter:3.2.6.1:pom"
    # Keikai (disponibles)
    "io.keikai:keikai-oss:5.0.0.1:jar"
    "io.keikai:keikai-model-oss:5.0.0.1:jar"
    "io.keikai:parent:keikai-build-oss:5.0.0.1:pom"
)

echo "=========================================="
echo "Subiendo artefactos ZKoss a Nexus"
echo "Repositorio: ${NEXUS_URL}"
echo "Total de artefactos: ${#zkoss_artifacts[@]}"
echo "=========================================="
echo ""

success_count=0
fail_count=0

# Subir cada artefacto
for artifact in "${zkoss_artifacts[@]}"; do
    IFS=':' read -r group_id artifact_id version packaging <<< "$artifact"
    
    if upload_artifact "$group_id" "$artifact_id" "$version" "$packaging"; then
        ((success_count++))
    else
        ((fail_count++))
    fi
    echo ""
done

# Subir sources de keikai si existen
echo "Subiendo sources de keikai..."
for keikai_artifact in "io.keikai:keikai-oss:5.0.0.1" "io.keikai:keikai-model-oss:5.0.0.1"; do
    IFS=':' read -r group_id artifact_id version <<< "$keikai_artifact"
    if upload_artifact "$group_id" "$artifact_id" "$version" "jar" "sources"; then
        ((success_count++))
    fi
done

echo ""
echo "=========================================="
echo "Resumen:"
echo -e "${GREEN}Exitosos: ${success_count}${NC}"
echo -e "${RED}Fallidos: ${fail_count}${NC}"
echo "=========================================="

