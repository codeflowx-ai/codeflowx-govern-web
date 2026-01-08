# Configuración para Windows Nativo

## Ventajas de Windows Nativo

✅ **Rendimiento**: Compilación rápida y eficiente
✅ **Hot Reload**: Funciona instantáneamente
✅ **Sistema de archivos**: Acceso directo sin overhead
✅ **Herramientas**: Mejor integración con VS Code/IDEs de Windows

## Requisitos Previos

1. **Node.js** (versión 18 o superior)
   - Descargar desde: https://nodejs.org/
   - Verificar instalación:
     ```cmd
     node --version
     npm --version
     ```

2. **Git para Windows** (si no lo tienes)
   - Descargar desde: https://git-scm.com/download/win

## Pasos de Configuración

### Opción 1: Usar el proyecto existente (Recomendado)

El proyecto ya está en `C:\Users\ManuelGonzalez\git\codeflowx-studio`, así que:

1. **Abrir PowerShell o CMD en Windows**
   ```cmd
   cd C:\Users\ManuelGonzalez\git\codeflowx-studio
   ```

2. **Instalar dependencias** (solo la primera vez)
   ```cmd
   npm install
   ```
   O si usas pnpm:
   ```cmd
   npm install -g pnpm
   pnpm install
   ```

3. **Iniciar servidor de desarrollo**
   ```cmd
   npm run dev
   ```

### Opción 2: Clonar proyecto en Windows (si prefieres separar)

```cmd
cd C:\Users\ManuelGonzalez\git
git clone <url-del-repo> codeflowx-studio-windows
cd codeflowx-studio-windows
npm install
npm run dev
```

## Optimizaciones para Windows

### 1. Configurar Next.js para Windows

El archivo `next.config.js` ya está optimizado para Windows nativo con:
- Polling desactivado (no necesario en Windows)
- Configuración de caché optimizada
- Watch options ajustados para Windows

### 2. Variables de Entorno

En Windows, las variables de entorno se configuran diferente:

**PowerShell:**
```powershell
$env:NODE_OPTIONS="--max-old-space-size=4096"
npm run dev
```

**CMD:**
```cmd
set NODE_OPTIONS=--max-old-space-size=4096
npm run dev
```

### 3. Scripts de Windows

Si los scripts de `package.json` no funcionan en Windows, usa los comandos directos:

```cmd
# Desarrollo
set NODE_OPTIONS=--max-old-space-size=4096 && next dev -p 3001

# Build
set NODE_OPTIONS=--max-old-space-size=4096 && next build
```

## Rendimiento Esperado

| Operación | Tiempo Estimado |
|-----------|-----------------|
| Compilación inicial | ~1 min |
| Hot reload | <1 seg |
| Build completo | ~2-3 min |
| Inicio servidor | ~10 seg |

## Solución de Problemas

### Error: "next: command not found"

```cmd
npm install -g next
```

O usar npx:
```cmd
npx next dev -p 3001
```

### Error: Puerto 3001 en uso

```cmd
# Encontrar proceso
netstat -ano | findstr :3001

# Matar proceso (reemplazar PID)
taskkill /PID <PID> /F
```

### Error: Permisos

Ejecutar PowerShell como Administrador o verificar permisos de la carpeta.

## Limpieza y Mantenimiento

### Limpiar caché de Next.js

```cmd
npm run clean
```

O manualmente:
```cmd
rmdir /s /q .next
rmdir /s /q node_modules\.cache
```

### Reinstalar dependencias

Si hay problemas con las dependencias:
```cmd
npm run clean:all
npm install
```

## Notas Importantes

- **node_modules**: Mantener sincronizado con `package.json` y `package-lock.json`
- **.next**: La caché de Next.js se regenera automáticamente
- **Git**: Los archivos están en el sistema de archivos nativo de Windows
