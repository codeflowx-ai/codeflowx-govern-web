# Guía Rápida: Optimización para 32GB RAM en WSL

## Pasos Rápidos

### 1. Configurar WSL (Windows)

Copia el archivo `.wslconfig` a tu directorio home de Windows:
```
C:\Users\ManuelGonzalez\.wslconfig
```

O crea el archivo manualmente con este contenido:

```ini
[wsl2]
memory=32GB
processors=6
swap=4GB
pageReporting=true
nestedVirtualization=true
kernelCommandLine=sysctl.vm.swappiness=10
```

### 2. Aplicar Optimizaciones en WSL

Desde WSL, ejecuta:

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web
./apply_wsl_optimizations.sh
```

### 3. Reiniciar WSL

Desde PowerShell (como Administrador):

```powershell
wsl --shutdown
```

Espera 5 segundos y abre WSL nuevamente.

### 4. Configurar IntelliJ

1. Abre IntelliJ IDEA
2. **Help → Edit Custom VM Options...**
3. Copia el contenido de `INTELLIJ_128GB_RAM.vmoptions`
4. Reinicia IntelliJ

### 5. Configurar Compilador en IntelliJ

1. **File → Settings → Build, Execution, Deployment → Compiler**
2. En **Shared build process VM options**:
   ```
   -Xms2048m
   -Xmx8192m
   -XX:MaxMetaspaceSize=2048m
   -XX:+UseG1GC
   -XX:ReservedCodeCacheSize=1024m
   ```

### 6. Configurar Maven en IntelliJ

1. **File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner**
2. En **VM options**:
   ```
   -Xms2048m
   -Xmx8192m
   -XX:MaxMetaspaceSize=2048m
   -XX:ReservedCodeCacheSize=1024m
   -XX:+UseG1GC
   ```

## Verificar

```bash
# Verificar memoria WSL
free -h

# Verificar variables Java
echo $JAVA_OPTS
echo $MAVEN_OPTS

# Verificar en IntelliJ
# Help → Diagnostic Tools → Show Memory Indicator
```

## Resultado Esperado

- ✅ WSL con 32GB de RAM disponible
- ✅ IntelliJ usando hasta 16GB de RAM
- ✅ Maven usando hasta 8GB de RAM
- ✅ Compilaciones extremadamente rápidas
- ✅ Sin problemas de memoria

## Ajustes Adicionales

Si necesitas más o menos memoria, ajusta:

- **WSL**: Edita `.wslconfig` y cambia `memory=32GB`
- **IntelliJ**: Cambia `-Xmx16384m` en VM options
- **Maven**: Cambia `MAVEN_OPTS` en `~/.bashrc`

