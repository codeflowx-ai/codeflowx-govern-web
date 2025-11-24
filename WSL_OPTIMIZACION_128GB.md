# Optimización de WSL Ubuntu 24.04 con 128GB RAM

## Configuración de WSL para Máximo Rendimiento

### 1. Configurar Memoria de WSL

Crea o edita el archivo `.wslconfig` en Windows:

**Ubicación:** `C:\Users\TuUsuario\.wslconfig`

```ini
[wsl2]
# Asignar 64GB de RAM a WSL (puedes ajustar según necesites)
memory=64GB

# Asignar 16 cores (ajusta según tu CPU)
processors=16

# Swap file size (con tanta RAM, puedes reducirlo o eliminarlo)
swap=8GB

# Ubicación del swap (opcional, mejor en SSD)
swapFile=C:\\Users\\TuUsuario\\AppData\\Local\\Temp\\swap.vhdx

# Habilitar page reporting para mejor rendimiento
pageReporting=true

# Habilitar nested virtualization si usas VMs
nestedVirtualization=true

# Configuración de red (opcional)
networkingMode=mirrored
dnsTunneling=true
firewall=true
autoProxy=true

# Mejorar rendimiento de I/O
kernelCommandLine=sysctl.vm.swappiness=10
```

### 2. Configurar WSL dentro de Ubuntu

Edita `/etc/wsl.conf` en WSL:

```bash
sudo nano /etc/wsl.conf
```

Agrega:

```ini
[boot]
systemd=true

[user]
default=tu-usuario

[interop]
enabled=true
appendWindowsPath=true

[network]
generateHosts=true
generateResolvConf=true
```

### 3. Optimizar Swap en WSL

Con 128GB de RAM, puedes reducir o deshabilitar swap:

```bash
# Ver swap actual
free -h

# Reducir swappiness (menos uso de swap)
echo 'vm.swappiness=10' | sudo tee -a /etc/sysctl.conf
sudo sysctl -p
```

### 4. Configurar Variables de Entorno Java

Edita `~/.bashrc` o `~/.zshrc`:

```bash
# Java optimizado para mucha RAM
export JAVA_OPTS="-Xms8g -Xmx32g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ParallelRefProcEnabled"
export MAVEN_OPTS="-Xms4g -Xmx16g -XX:+UseG1GC"
export GRADLE_OPTS="-Xms4g -Xmx16g -XX:+UseG1GC -XX:MaxMetaspaceSize=2g"

# Para proyectos Maven grandes
export MAVEN_OPTS="$MAVEN_OPTS -XX:ReservedCodeCacheSize=2g"

# Aplicar cambios
source ~/.bashrc
```

### 5. Configurar Maven para Usar Más Memoria

Edita `~/.m2/settings.xml`:

```xml
<settings>
  <profiles>
    <profile>
      <id>performance</id>
      <properties>
        <maven.compiler.fork>true</maven.compiler.fork>
        <maven.compiler.meminitial>4g</maven.compiler.meminitial>
        <maven.compiler.maxmem>16g</maven.compiler.maxmem>
      </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>performance</activeProfile>
  </activeProfiles>
</settings>
```

### 6. Optimizar Sistema de Archivos

```bash
# Verificar sistema de archivos
df -h

# Optimizar para desarrollo (si usas ext4)
# Editar /etc/fstab y agregar opciones de montaje:
# noatime,nodiratime para reducir escrituras
```

## Configuración de IntelliJ con 128GB RAM

### 1. Memoria de IntelliJ IDEA

**Help → Edit Custom VM Options...**

```properties
# Configuración agresiva para 128GB RAM
-Xms8192m
-Xmx32768m
-XX:ReservedCodeCacheSize=4096m
-XX:+UseG1GC
-XX:SoftRefLRUPolicyMSPerMB=50
-XX:CICompilerCount=8
-ea
-Dsun.io.useCanonCaches=false
-Djava.net.preferIPv4Stack=true
-Djdk.http.auth.tunneling.disabledSchemes=""
-XX:+HeapDumpOnOutOfMemoryError
-XX:-UseBiasedLocking
-Djdk.attach.allowAttachSelf=true
-Dkotlinx.coroutines.debug=off
-Djdk.module.illegalAccess.silent=true

# Optimizaciones adicionales
-XX:+UseStringDeduplication
-XX:+OptimizeStringConcat
-XX:MaxGCPauseMillis=200
-XX:ParallelGCThreads=8
-XX:ConcGCThreads=4
```

### 2. Memoria del Compilador

**File → Settings → Build, Execution, Deployment → Compiler**

En **Shared build process VM options**:

```
-Xms4096m
-Xmx16384m
-XX:MaxMetaspaceSize=4096m
-XX:+UseG1GC
-XX:ReservedCodeCacheSize=2048m
```

### 3. Configuración de Maven en IntelliJ

**File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner**

En **VM options**:

```
-Xms4096m
-Xmx16384m
-XX:MaxMetaspaceSize=4096m
-XX:ReservedCodeCacheSize=2048m
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
```

### 4. Configuración de Indexación

**File → Settings → Directories**

Excluir:
- `target/`
- `build/`
- `node_modules/`
- `.git/`
- `dist/`
- Cualquier directorio con muchos archivos generados

### 5. Configuración de Inspecciones

**File → Settings → Editor → Inspections**

- Deshabilitar inspecciones costosas que no necesites
- Usar "Power Save Mode" solo cuando no estés desarrollando activamente

## Configuración de Proyectos Java

### Para nocode.service (Multi-módulo)

En cada módulo, configura `pom.xml` o crea `.mvn/jvm.config`:

```
-Xms2g
-Xmx8g
-XX:+UseG1GC
-XX:MaxMetaspaceSize=2g
```

### Para suinsit.nova.web

Crea `.mvn/jvm.config` en la raíz:

```
-Xms4g
-Xmx16g
-XX:+UseG1GC
-XX:MaxMetaspaceSize=4g
-XX:ReservedCodeCacheSize=2g
```

## Optimizaciones del Sistema

### 1. Aumentar Límites del Sistema

```bash
# Editar límites del sistema
sudo nano /etc/security/limits.conf
```

Agrega:

```
* soft nofile 65536
* hard nofile 65536
* soft nproc 32768
* hard nproc 32768
```

### 2. Optimizar Kernel Parameters

```bash
sudo nano /etc/sysctl.conf
```

Agrega:

```conf
# Optimizaciones de memoria
vm.swappiness=10
vm.dirty_ratio=60
vm.dirty_background_ratio=2
vm.overcommit_memory=1

# Optimizaciones de red
net.core.rmem_max=134217728
net.core.wmem_max=134217728
net.ipv4.tcp_rmem=4096 87380 134217728
net.ipv4.tcp_wmem=4096 65536 134217728

# Optimizaciones de archivos
fs.file-max=2097152
fs.inotify.max_user_watches=524288
```

Aplicar:

```bash
sudo sysctl -p
```

### 3. Configurar ZRAM (Opcional)

Con tanta RAM, ZRAM puede no ser necesario, pero si quieres:

```bash
sudo apt install zram-config
sudo systemctl enable zram-config
```

## Monitoreo de Recursos

### Scripts de Monitoreo

Crea `~/monitor_resources.sh`:

```bash
#!/bin/bash
echo "=== Memoria ==="
free -h
echo ""
echo "=== CPU ==="
top -bn1 | grep "Cpu(s)" | awk '{print $2}'
echo ""
echo "=== Disco ==="
df -h | grep -E '^/dev|Filesystem'
echo ""
echo "=== Procesos Java ==="
ps aux | grep java | grep -v grep | awk '{print $2, $3"%", $4"%", $11}'
```

Hacer ejecutable:

```bash
chmod +x ~/monitor_resources.sh
```

## Configuración de Git

### Optimizar Git para Proyectos Grandes

```bash
# Configurar Git para mejor rendimiento
git config --global core.preloadindex true
git config --global core.fscache true
git config --global gc.auto 256
git config --global pack.windowMemory "256m"
git config --global pack.packSizeLimit "2g"
git config --global pack.threads "8"
```

## Aplicar Todas las Configuraciones

### Script de Aplicación Rápida

Crea `~/apply_wsl_optimizations.sh`:

```bash
#!/bin/bash

echo "Aplicando optimizaciones de WSL..."

# Variables de entorno Java
cat >> ~/.bashrc << 'EOF'
export JAVA_OPTS="-Xms8g -Xmx32g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
export MAVEN_OPTS="-Xms4g -Xmx16g -XX:+UseG1GC -XX:ReservedCodeCacheSize=2g"
export GRADLE_OPTS="-Xms4g -Xmx16g -XX:+UseG1GC -XX:MaxMetaspaceSize=2g"
EOF

# Sysctl optimizations
sudo tee -a /etc/sysctl.conf << 'EOF'
vm.swappiness=10
vm.dirty_ratio=60
vm.dirty_background_ratio=2
fs.file-max=2097152
fs.inotify.max_user_watches=524288
EOF

# Aplicar sysctl
sudo sysctl -p

echo "Optimizaciones aplicadas. Reinicia WSL para aplicar todos los cambios."
echo "Ejecuta: wsl --shutdown (desde PowerShell) y luego vuelve a abrir WSL"
```

Hacer ejecutable y ejecutar:

```bash
chmod +x ~/apply_wsl_optimizations.sh
~/apply_wsl_optimizations.sh
```

## Reiniciar WSL

Después de hacer cambios en `.wslconfig`:

1. Cierra todas las ventanas de WSL
2. Desde PowerShell (como Administrador):
   ```powershell
   wsl --shutdown
   ```
3. Espera unos segundos
4. Abre WSL nuevamente

## Verificar Configuración

```bash
# Verificar memoria disponible
free -h

# Verificar configuración de WSL
cat /proc/meminfo | head -10

# Verificar variables de entorno
echo $JAVA_OPTS
echo $MAVEN_OPTS

# Verificar límites del sistema
ulimit -a
```

## Resultado Esperado

Con estas configuraciones deberías tener:

- ✅ WSL usando hasta 64GB de RAM
- ✅ IntelliJ usando hasta 32GB de RAM
- ✅ Maven usando hasta 16GB de RAM
- ✅ Compilaciones extremadamente rápidas
- ✅ Sin problemas de memoria
- ✅ Mejor rendimiento general

## Notas Importantes

1. **Ajusta según necesidad**: No necesitas usar toda la RAM disponible
2. **Monitorea el uso**: Usa `htop` o `free -h` para ver uso real
3. **Deja RAM para Windows**: No asignes toda la RAM a WSL
4. **SSD ayuda mucho**: Si tienes SSD, el rendimiento será aún mejor

