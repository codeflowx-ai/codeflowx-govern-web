#!/bin/bash

echo "=========================================="
echo "Aplicando optimizaciones de WSL para 128GB RAM"
echo "=========================================="

# Variables de entorno Java optimizadas
echo ""
echo "Configurando variables de entorno Java..."
cat >> ~/.bashrc << 'EOF'

# ============================================
# Optimizaciones Java para 32GB RAM en WSL
# ============================================
export JAVA_OPTS="-Xms4g -Xmx16g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ParallelRefProcEnabled"
export MAVEN_OPTS="-Xms2g -Xmx8g -XX:+UseG1GC -XX:ReservedCodeCacheSize=1g -XX:MaxMetaspaceSize=1g"
export GRADLE_OPTS="-Xms2g -Xmx8g -XX:+UseG1GC -XX:MaxMetaspaceSize=1g"
EOF

# Optimizaciones de sistema
echo ""
echo "Aplicando optimizaciones de sistema..."

# Crear backup de sysctl.conf
sudo cp /etc/sysctl.conf /etc/sysctl.conf.backup.$(date +%Y%m%d_%H%M%S)

# Agregar optimizaciones
sudo tee -a /etc/sysctl.conf << 'EOF'

# ============================================
# Optimizaciones para desarrollo con mucha RAM
# ============================================
# Memoria
vm.swappiness=10
vm.dirty_ratio=60
vm.dirty_background_ratio=2
vm.overcommit_memory=1

# Red
net.core.rmem_max=134217728
net.core.wmem_max=134217728
net.ipv4.tcp_rmem=4096 87380 134217728
net.ipv4.tcp_wmem=4096 65536 134217728

# Archivos
fs.file-max=2097152
fs.inotify.max_user_watches=524288
EOF

# Aplicar sysctl
echo ""
echo "Aplicando cambios de sysctl..."
sudo sysctl -p

# Configurar límites del sistema
echo ""
echo "Configurando límites del sistema..."
sudo tee -a /etc/security/limits.conf << 'EOF'

# Límites optimizados para desarrollo
* soft nofile 65536
* hard nofile 65536
* soft nproc 32768
* hard nproc 32768
EOF

# Crear archivo .mvn/jvm.config si no existe
echo ""
echo "Configurando Maven JVM options..."
mkdir -p .mvn
cat > .mvn/jvm.config << 'EOF'
-Xms2g
-Xmx8g
-XX:+UseG1GC
-XX:MaxMetaspaceSize=1g
-XX:ReservedCodeCacheSize=1g
-XX:MaxGCPauseMillis=200
EOF

echo ""
echo "=========================================="
echo "Optimizaciones aplicadas correctamente!"
echo "=========================================="
echo ""
echo "Para aplicar todos los cambios:"
echo "1. Cierra todas las ventanas de WSL"
echo "2. Desde PowerShell (como Administrador): wsl --shutdown"
echo "3. Abre WSL nuevamente"
echo ""
echo "Para verificar:"
echo "  free -h"
echo "  echo \$JAVA_OPTS"
echo "  echo \$MAVEN_OPTS"
echo ""

