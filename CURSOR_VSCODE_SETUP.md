# Configuración de Cursor/VSCode para Detectar Errores como IntelliJ

## Problema

Cursor/VSCode no detecta errores que IntelliJ/Eclipse sí detectan porque:
- El Java Language Server no está bien configurado
- Faltan extensiones de validación
- La configuración de compilación no es estricta

## Solución: Configuración Completa

### 1. Instalar Extensiones Requeridas

Abre Cursor/VSCode y instala estas extensiones (ya están en `.vscode/extensions.json`):

#### Extensiones Críticas (OBLIGATORIAS):

1. **Extension Pack for Java** (`vscjava.vscode-java-pack`)
   - Incluye todo lo necesario para Java
   - Instala automáticamente: Language Support, Debugger, Test Runner, Maven, Dependency Viewer

2. **Lombok** (`GabrielBB.vscode-lombok`)
   - Necesario para proyectos con Lombok

3. **SonarLint** (`SonarSource.sonarlint-vscode`)
   - Detecta problemas de calidad de código en tiempo real

#### Extensiones Opcionales pero Recomendadas:

4. **Spring Boot Extension Pack** (si usas Spring)
5. **GitLens** (mejor integración Git)
6. **Checkstyle** (validación de estilo)

### 2. Configurar Java Language Server

El archivo `.vscode/settings.json` ya está configurado, pero verifica:

1. **Abrir Command Palette** (`Ctrl+Shift+P`)
2. Escribe: `Java: Configure Java Runtime`
3. Selecciona JDK 17: `/usr/lib/jvm/java-17-openjdk-amd64`

### 3. Sincronizar Proyecto Maven

1. **Command Palette** (`Ctrl+Shift+P`)
2. `Java: Clean Java Language Server Workspace`
3. Reinicia Cursor/VSCode
4. Espera a que el proyecto se sincronice (verás "Synchronizing..." en la barra de estado)

### 4. Verificar Detección de Errores

#### Activar Todas las Validaciones:

1. **Command Palette** → `Preferences: Open Settings (JSON)`
2. Verifica que estas configuraciones estén presentes:

```json
{
  "java.compile.nullAnalysis.mode": "automatic",
  "java.errors.incompleteClasspath.severity": "warning",
  "java.autobuild.enabled": true,
  "java.maxConcurrentBuilds": 1
}
```

#### Ver Problemas en Tiempo Real:

1. Abre el panel **Problems** (`Ctrl+Shift+M`)
2. Deberías ver todos los errores que IntelliJ detecta
3. Los errores aparecen con subrayado rojo en el editor

### 5. Configurar Maven Correctamente

#### Verificar Maven:

1. **Command Palette** → `Java: Configure Maven`
2. Verifica que apunte a: `/usr/bin/mvn`
3. Verifica `settings.xml`: `~/.m2/settings.xml`

#### Compilar con Maven desde Cursor:

1. Abre terminal integrado (`Ctrl+`` ` o `Ctrl+Shift+`` `)
2. Ejecuta:
   ```bash
   mvn clean compile
   ```
3. Los errores de compilación aparecerán en el panel Problems

### 6. Workflow Recomendado

#### Desarrollo Rápido con Cursor:

1. **Escribe código** en Cursor (rápido con IA)
2. **Revisa el panel Problems** (`Ctrl+Shift+M`) constantemente
3. **Compila con Maven** periódicamente:
   ```bash
   mvn clean compile
   ```
4. **Revisa errores** que aparezcan

#### Antes de Commit:

1. **Abre en IntelliJ** para revisión final
2. **Ejecuta todas las inspecciones**:
   - `Code → Inspect Code...`
   - Selecciona todo el proyecto
3. **Corrige errores** que IntelliJ detecte
4. **Vuelve a Cursor** y sincroniza

### 7. Sincronizar Configuraciones entre Cursor e IntelliJ

#### Compartir Configuración de Formato:

1. En IntelliJ: **File → Settings → Editor → Code Style → Java**
2. Exporta la configuración: **Export → IntelliJ IDEA code style XML**
3. Guarda como `.editorconfig` o `java-formatter.xml`
4. Cursor usará esta configuración automáticamente

#### Compartir Exclusiones:

Ambos IDEs deben excluir los mismos directorios:
- `target/`
- `.idea/` (solo en Cursor)
- `.vscode/` (solo en IntelliJ)

### 8. Comandos Útiles en Cursor

#### Command Palette (`Ctrl+Shift+P`):

- `Java: Clean Java Language Server Workspace` - Limpia y resincroniza
- `Java: Rebuild Projects` - Recompila todo
- `Java: Reload Projects` - Recarga proyectos Maven
- `Java: Show Build Job Status` - Ver estado de compilación
- `Java: Show Runtime Information` - Ver info de Java/Maven

#### Atajos de Teclado:

- `Ctrl+Shift+M` - Panel de Problemas
- `F8` - Siguiente problema
- `Shift+F8` - Problema anterior
- `Ctrl+.` - Quick Fix (correcciones rápidas)
- `Alt+Shift+F` - Formatear documento

### 9. Verificar que Funciona

#### Test de Detección de Errores:

1. Crea un archivo de prueba con errores:

```java
package com.test;

public class TestError {
    public void test() {
        String s = null;
        s.length(); // Error: NullPointerException potencial
        int x = "string"; // Error: Tipo incorrecto
    }
}
```

2. Deberías ver errores inmediatamente en:
   - Panel Problems (`Ctrl+Shift+M`)
   - Subrayado rojo en el editor
   - Hover sobre el error muestra detalles

3. Si NO ves errores:
   - Verifica que las extensiones estén instaladas
   - Ejecuta: `Java: Clean Java Language Server Workspace`
   - Reinicia Cursor

### 10. Solución de Problemas

#### No se detectan errores:

1. **Verifica extensiones instaladas:**
   ```bash
   code --list-extensions | grep java
   ```

2. **Limpia workspace:**
   - `Ctrl+Shift+P` → `Java: Clean Java Language Server Workspace`
   - Reinicia Cursor

3. **Verifica Java Home:**
   - `Ctrl+Shift+P` → `Java: Configure Java Runtime`
   - Selecciona JDK 17

4. **Verifica Maven:**
   - Terminal: `mvn -version`
   - Debe mostrar Maven 3.6+

#### Errores diferentes a IntelliJ:

1. **Sincroniza proyecto Maven:**
   - `Ctrl+Shift+P` → `Java: Reload Projects`

2. **Verifica configuración de compilador:**
   - Asegúrate de que `java.compile.nullAnalysis.mode` esté en `automatic`

3. **Compara configuración:**
   - IntelliJ: `File → Project Structure → Project → Language Level`
   - Cursor: Debe ser Java 17

#### Performance Lenta:

1. **Reduce validaciones en tiempo real:**
   ```json
   {
     "java.autobuild.enabled": false
   }
   ```
   Luego compila manualmente con `mvn compile`

2. **Excluye más directorios:**
   ```json
   {
     "files.watcherExclude": {
       "**/target/**": true,
       "**/.git/**": true
     }
   }
   ```

### 11. Workflow Híbrido Recomendado

#### Para Desarrollo Rápido:

1. **Usa Cursor** para:
   - Generar código con IA
   - Desarrollo rápido
   - Edición de archivos

2. **Revisa constantemente**:
   - Panel Problems abierto
   - Compila con Maven cada cierto tiempo
   - Revisa errores antes de continuar

#### Para Revisión Final:

1. **Abre en IntelliJ**:
   - `File → Open` → Selecciona proyecto
   - Espera a que indexe

2. **Ejecuta inspecciones**:
   - `Code → Inspect Code...`
   - Revisa todos los problemas

3. **Corrige errores**:
   - IntelliJ sugiere correcciones automáticas
   - Aplica correcciones

4. **Vuelve a Cursor**:
   - Los cambios se sincronizan automáticamente
   - Verifica que todo esté bien

### 12. Configuración Adicional

#### Para Proyectos Multi-módulo:

El archivo `settings.json` ya está configurado para detectar todos los módulos Maven automáticamente.

#### Para Mejor Rendimiento:

Si Cursor va lento con proyectos grandes:

```json
{
  "java.maxConcurrentBuilds": 1,
  "java.autobuild.enabled": false,
  "files.watcherExclude": {
    "**/target/**": true,
    "**/node_modules/**": true
  }
}
```

Luego compila manualmente cuando necesites.

## Resultado Esperado

Después de esta configuración:

- ✅ Cursor detecta los mismos errores que IntelliJ
- ✅ Panel Problems muestra todos los problemas
- ✅ Errores aparecen en tiempo real mientras escribes
- ✅ Compilación Maven funciona correctamente
- ✅ Puedes desarrollar rápido en Cursor y revisar en IntelliJ

## Notas Importantes

1. **Siempre compila con Maven** antes de commit
2. **Revisa en IntelliJ** antes de mergear a main
3. **Mantén sincronizadas** las configuraciones entre ambos IDEs
4. **Limpia workspace** si hay problemas de sincronización

