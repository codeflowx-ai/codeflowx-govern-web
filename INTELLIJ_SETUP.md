# Configuración de IntelliJ IDEA para suinsit.nova.web

## Requisitos Previos

- IntelliJ IDEA (versión 2021.3 o superior recomendada)
- JDK 17 instalado y configurado
- Maven 3.6+ instalado
- Plugin Spring Boot (incluido en IntelliJ Ultimate, o instalar en Community)

## Pasos para Importar el Proyecto

### Opción 1: Importar desde IntelliJ (Recomendado)

1. **Abrir IntelliJ IDEA**
2. **File → Open** (o **File → Open Project**)
3. Seleccionar el directorio: `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web`
4. IntelliJ detectará automáticamente que es un proyecto Maven
5. Seleccionar **"Import Maven project automatically"** si aparece el diálogo
6. Esperar a que IntelliJ indexe y descargue las dependencias (puede tardar varios minutos debido a las muchas dependencias)

### Opción 2: Importar como Proyecto Maven

1. **File → New → Project from Existing Sources**
2. Seleccionar el directorio del proyecto
3. Elegir **"Import project from external model"** → **Maven**
4. Siguiente → Siguiente → Finish

## Configuración de WSL Ubuntu 24.04

Si estás trabajando en WSL (Windows Subsystem for Linux), configura IntelliJ para usar las herramientas de WSL:

### Configurar Terminal WSL

1. **File → Settings** (Ctrl+Alt+S) → **Tools → Terminal**
2. En **Shell path**, selecciona o escribe:
   ```
   wsl.exe -d Ubuntu-24.04
   ```
   O usa la ruta completa:
   ```
   C:\Windows\System32\wsl.exe -d Ubuntu-24.04
   ```
3. Marcar **"Shell integration"** si está disponible

### Configurar JDK desde WSL

1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. En **Project Settings → SDKs**, click en **+** → **Add SDK → WSL**
3. Seleccionar **Ubuntu-24.04**
4. Buscar JDK 17 en WSL:
   - Ruta típica: `/usr/lib/jvm/java-17-openjdk-amd64`
   - O usar: `/usr/lib/jvm/java-1.17.0-openjdk-amd64`
5. Nombrar el SDK como "17 (WSL Ubuntu-24.04)"
6. En **Project Settings → Project**:
   - **Project SDK**: Seleccionar "17 (WSL Ubuntu-24.04)"
   - **Project language level**: 17

### Configurar Maven desde WSL

1. **File → Settings** → **Build, Execution, Deployment → Build Tools → Maven**
2. En **Maven home path**, usar la ruta de WSL:
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
4. **Local repository**:
   ```
   \\wsl$\Ubuntu-24.04\home\tu-usuario\.m2\repository
   ```
5. Marcar **"Use plugin registry"** si está disponible

### Verificar Herramientas en WSL

Desde la terminal de IntelliJ (que ahora usa WSL), verifica:

```bash
java -version  # Debe mostrar Java 17
mvn -version   # Debe mostrar Maven 3.9.6
which java     # Debe mostrar /usr/bin/java
which mvn      # Debe mostrar /usr/bin/mvn
```

## Configuración Post-Importación

### 1. Verificar JDK

1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. En **Project Settings → Project**:
   - **Project SDK**: Seleccionar JDK 17 (preferiblemente el de WSL)
   - **Project language level**: 17 - Sealed types, always-strict floating-point semantics
3. En **Project Settings → Modules**:
   - Verificar que el módulo esté configurado con JDK 17

### 2. Configurar Maven

Si no usas WSL, configura Maven normalmente:

1. **File → Settings** (Ctrl+Alt+S) → **Build, Execution, Deployment → Build Tools → Maven**
2. Verificar:
   - **Maven home path**: Ruta a tu instalación de Maven
   - **User settings file**: `~/.m2/settings.xml` (o tu archivo de configuración)
   - **Local repository**: `~/.m2/repository`
3. Marcar **"Use plugin registry"** si está disponible

### 3. Configurar Repositorios Maven

El proyecto usa varios repositorios Maven:
- Maven Central
- ZK CE Repository (https://mavensync.zkoss.org/maven2)
- ZKoss EE Repository (https://maven.zkoss.org/repo/zk/ee/)
- Spring Milestones (https://repo.spring.io/milestone)

Asegúrate de tener acceso a estos repositorios. Si usas un repositorio privado para dependencias de `suinsit` o `codeflowx`, configúralo en tu `settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>tu-repositorio-privado</id>
      <username>tu-usuario</username>
      <password>tu-password</password>
    </server>
  </servers>
</settings>
```

### 4. Configurar Lombok

El proyecto usa Lombok. Asegúrate de:

1. **File → Settings** → **Build, Execution, Deployment → Compiler → Annotation Processors**
2. Marcar **"Enable annotation processing"**
3. Instalar el plugin de Lombok si no está instalado:
   - **File → Settings** → **Plugins**
   - Buscar "Lombok" e instalar
   - Reiniciar IntelliJ

### 5. Sincronizar Proyecto Maven

1. Click derecho en el archivo `pom.xml`
2. **Maven → Reload Project**
   - O usar el icono de Maven en la barra lateral derecha → **Reload All Maven Projects**

### 6. Configurar Encoding

1. **File → Settings** → **Editor → File Encodings**
2. Verificar:
   - **Global Encoding**: UTF-8
   - **Project Encoding**: UTF-8
   - **Default encoding for properties files**: UTF-8
   - Marcar **"Transparent native-to-ascii conversion"**

### 7. Configurar Compilador

1. **File → Settings** → **Build, Execution, Deployment → Compiler → Java Compiler**
2. Verificar:
   - **Project bytecode version**: 17
   - **Per-module bytecode version**: 17

### 8. Configurar Spring Boot

1. **File → Settings** → **Build, Execution, Deployment → Compiler → Spring**
2. Verificar que Spring Boot esté habilitado

### 9. Configurar Run Configuration para Spring Boot

1. **Run → Edit Configurations...**
2. Click en **+** → **Spring Boot**
3. Configurar:
   - **Name**: `Application`
   - **Main class**: `com.suinsit.studio.app.Application`
   - **Working directory**: `$PROJECT_DIR$`
   - **Use classpath of module**: Seleccionar el módulo del proyecto
4. En **VM options**, puedes agregar:
   ```
   -Dspring.profiles.active=dev
   -Xmx2048m
   -Xms512m
   ```
5. Click **OK**

### 10. Limpiar y Compilar

1. **Build → Rebuild Project** (Ctrl+Shift+F9)
2. O desde Maven: **Maven → Lifecycle → clean → install**

## Estructura del Proyecto

Este es un proyecto Spring Boot con:
- **Packaging**: WAR (para desplegar en servidor de aplicaciones)
- **Framework Web**: ZKoss Framework
- **BPMN Engine**: Flowable 6.8.1
- **Rules Engine**: Drools 8.44.0
- **Base de datos**: PostgreSQL
- **Spring Boot**: 2.7.3
- **Java**: 17

### Módulos y Características

- Aplicación web con ZKoss UI
- Integración con servicios de gobernanza (codeflowx)
- Soporte para aplicaciones nocode (suinsit)
- Integración con múltiples servicios cloud (AWS, Azure, GCP)
- Soporte para BPMN y reglas de negocio

## Atajos Útiles

- **Ctrl+Shift+F9**: Rebuild Project
- **Ctrl+F9**: Build Project
- **Ctrl+Alt+S**: Settings
- **Ctrl+Alt+Shift+S**: Project Structure
- **Alt+Insert**: Generar código (getters, setters, constructors, etc.)
- **Ctrl+Alt+L**: Reformat Code
- **Ctrl+Alt+O**: Optimize Imports
- **Ctrl+Shift+A**: Buscar acción
- **Double Shift**: Buscar en todo el proyecto
- **Ctrl+Shift+F10**: Run Application

## Solución de Problemas

### Error: "Cannot resolve symbol"
1. **File → Invalidate Caches / Restart**
2. Seleccionar **Invalidate and Restart**
3. Esperar a que reindexe

### Error: "Maven dependencies not found"
1. Verificar conexión a internet
2. Verificar acceso a repositorios ZKoss (pueden requerir credenciales)
3. **Maven → Reload Project**
4. **File → Invalidate Caches / Restart**

### Error: "JDK not found"
1. **File → Project Structure → Project Settings → Project**
2. Configurar **Project SDK** a JDK 17
3. Si no aparece, **Add SDK → Download JDK** o **Add SDK → JDK** para seleccionar instalación local

### Error: "Lombok annotations not working"
1. Instalar plugin de Lombok
2. **File → Settings** → **Build, Execution, Deployment → Compiler → Annotation Processors**
3. Marcar **"Enable annotation processing"**
4. Reiniciar IntelliJ

### Error: "ZKoss dependencies not found"
1. Verificar acceso a repositorios ZKoss
2. Si usas ZKoss EE, necesitarás credenciales en tu `settings.xml`:
```xml
<settings>
  <servers>
    <server>
      <id>zkoss-ee</id>
      <username>tu-usuario</username>
      <password>tu-password</password>
    </server>
  </servers>
</settings>
```

### Problemas con WAR Packaging
Si necesitas ejecutar como WAR en un servidor de aplicaciones:
1. **Run → Edit Configurations...**
2. Crear configuración **Tomcat Server** o **Jetty Server**
3. Configurar el artefacto WAR generado

## Notas

- Los archivos `.idea/` están en `.gitignore` por defecto, pero la configuración básica ya está creada
- Este proyecto tiene muchas dependencias, la primera sincronización puede tardar varios minutos
- El proyecto usa ZKoss Framework, que requiere repositorios específicos
- Para desarrollo local, puedes ejecutar como aplicación Spring Boot directamente
- Para producción, se genera un WAR que se despliega en un servidor de aplicaciones (Jetty configurado en el pom.xml)

