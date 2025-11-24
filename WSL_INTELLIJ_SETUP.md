# Configuración de IntelliJ IDEA con WSL Ubuntu 24.04

Esta guía te ayudará a configurar IntelliJ IDEA para trabajar con WSL Ubuntu 24.04 como entorno de desarrollo.

## Configuración Inicial

### 1. Configurar Terminal WSL

1. Abre IntelliJ IDEA
2. **File → Settings** (Ctrl+Alt+S en Windows/Linux, Cmd+, en Mac)
3. Navega a **Tools → Terminal**
4. En **Shell path**, configura:
   ```
   wsl.exe -d Ubuntu-24.04
   ```
   O la ruta completa:
   ```
   C:\Windows\System32\wsl.exe -d Ubuntu-24.04
   ```
5. Marca **"Shell integration"** para mejor integración
6. Click **OK**

### 2. Configurar JDK desde WSL

IntelliJ puede usar el JDK instalado en WSL:

1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. Ve a **Project Settings → SDKs**
3. Click en **+** → **Add SDK → WSL**
4. Selecciona **Ubuntu-24.04**
5. IntelliJ buscará automáticamente los JDKs instalados en WSL
6. Selecciona **Java 17** (ruta típica: `/usr/lib/jvm/java-17-openjdk-amd64`)
7. Nombrar el SDK como "17 (WSL Ubuntu-24.04)"
8. Click **OK**

**Alternativa manual:**
Si IntelliJ no detecta automáticamente el JDK:
1. Click en **+** → **Add SDK → JDK**
2. En **JDK home path**, usa la ruta de WSL:
   ```
   \\wsl$\Ubuntu-24.04\usr\lib\jvm\java-17-openjdk-amd64
   ```
3. Nombrar el SDK y click **OK**

### 3. Configurar Maven desde WSL

1. **File → Settings** → **Build, Execution, Deployment → Build Tools → Maven**
2. En **Maven home path**, usa:
   ```
   \\wsl$\Ubuntu-24.04\opt\maven
   ```
   O desde la terminal de IntelliJ (WSL):
   ```
   /opt/maven
   ```
3. **User settings file**: 
   ```
   \\wsl$\Ubuntu-24.04\home\tu-usuario\.m2\settings.xml
   ```
   Reemplaza `tu-usuario` con tu usuario de WSL (puedes obtenerlo con `whoami` en WSL)
4. **Local repository**:
   ```
   \\wsl$\Ubuntu-24.04\home\tu-usuario\.m2\repository
   ```
5. Marca **"Use plugin registry"** si está disponible
6. Click **OK**

### 4. Configurar Git desde WSL

1. **File → Settings** → **Version Control → Git**
2. En **Path to Git executable**, usa:
   ```
   \\wsl$\Ubuntu-24.04\usr\bin\git
   ```
   O desde la terminal de IntelliJ (WSL):
   ```
   /usr/bin/git
   ```
3. Click **Test** para verificar que funciona
4. Click **OK**

## Verificar Configuración

### Desde Terminal de IntelliJ

Abre la terminal en IntelliJ (Alt+F12) y verifica:

```bash
# Verificar Java
java -version
# Debe mostrar: openjdk version "17.x.x"

# Verificar Maven
mvn -version
# Debe mostrar: Apache Maven 3.9.6

# Verificar Git
git --version
# Debe mostrar: git version 2.x.x

# Verificar rutas
which java    # /usr/bin/java
which mvn     # /usr/bin/mvn
which git     # /usr/bin/git
```

### Verificar en Project Structure

1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. **Project Settings → Project**:
   - **Project SDK**: Debe mostrar "17 (WSL Ubuntu-24.04)" o similar
   - **Project language level**: 17

## Ventajas de Usar WSL

1. **Consistencia**: Mismo entorno que tu servidor de desarrollo/producción
2. **Herramientas nativas**: Acceso a herramientas Linux nativas
3. **Rendimiento**: Mejor rendimiento para operaciones de archivos en WSL
4. **Compatibilidad**: Mismo comportamiento que en servidores Linux

## Solución de Problemas

### IntelliJ no encuentra WSL

1. Verifica que WSL esté instalado:
   ```powershell
   wsl --list --verbose
   ```
2. Verifica que Ubuntu-24.04 esté disponible:
   ```powershell
   wsl -d Ubuntu-24.04
   ```

### No puede acceder a rutas de WSL

Las rutas de WSL en Windows usan el formato `\\wsl$\Distribución\path`. Si no funciona:
1. Abre el Explorador de Windows
2. En la barra de direcciones, escribe: `\\wsl$\Ubuntu-24.04`
3. Si puedes acceder, las rutas funcionarán en IntelliJ

### JDK no se detecta automáticamente

1. Verifica que Java 17 esté instalado en WSL:
   ```bash
   ls -la /usr/lib/jvm/ | grep java-17
   ```
2. Si no está, instálalo:
   ```bash
   sudo apt update
   sudo apt install openjdk-17-jdk
   ```
3. Configura JAVA_HOME en WSL (opcional pero recomendado):
   ```bash
   echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
   source ~/.bashrc
   ```

### Maven no se encuentra

1. Verifica que Maven esté instalado:
   ```bash
   mvn -version
   ```
2. Si no está, instálalo:
   ```bash
   sudo apt update
   sudo apt install maven
   ```
   O descarga desde [maven.apache.org](https://maven.apache.org/download.cgi) y extrae en `/opt/maven`

### Problemas con permisos

Si tienes problemas de permisos al acceder a archivos de WSL desde Windows:
1. Asegúrate de que los archivos tengan permisos adecuados en WSL
2. Considera usar `chmod` si es necesario:
   ```bash
   chmod -R 755 ~/.m2
   ```

## Configuración Avanzada

### Variables de Entorno

Puedes configurar variables de entorno específicas para WSL en IntelliJ:

1. **File → Settings** → **Build, Execution, Deployment → Build Tools → Maven → Runner**
2. En **Environment variables**, agrega:
   ```
   JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
   MAVEN_HOME=/opt/maven
   ```

### Run Configurations con WSL

Para ejecutar aplicaciones usando herramientas de WSL:

1. **Run → Edit Configurations...**
2. Selecciona tu configuración de Spring Boot
3. En **Environment variables**, puedes agregar variables de WSL
4. En **Working directory**, usa rutas de WSL si es necesario

## Notas Finales

- Los archivos de configuración `.idea/wsl.xml` y `.idea/terminal.xml` ya están creados
- IntelliJ puede usar tanto herramientas de Windows como de WSL
- Para mejor rendimiento, usa herramientas de WSL cuando trabajes con proyectos en rutas de WSL
- La terminal integrada de IntelliJ ahora usará WSL por defecto

