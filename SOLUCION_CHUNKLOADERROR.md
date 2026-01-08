# 🔧 SOLUCIÓN: ChunkLoadError

## Error
```
ChunkLoadError: Loading chunk app/layout failed.
(timeout: http://localhost:3002/_next/static/chunks/app/layout.js)
```

## Causas Comunes

1. **Compilación incompleta**: El servidor aún está compilando
2. **Cache del navegador**: El navegador tiene archivos antiguos en cache
3. **Errores de compilación**: Hay errores que impiden la compilación completa

## Soluciones (en orden)

### 1. Esperar a que compile completamente ⏱️

El servidor necesita tiempo para compilar. Espera **1-2 minutos** después de iniciar el servidor.

**Indicadores de que está listo:**
- En la terminal verás: `✓ Ready in Xs` o `○ Compiling / ...`
- El directorio `.next` tendrá archivos compilados

### 2. Hard Refresh en el Navegador 🔄

**Windows/Linux:**
- `Ctrl + Shift + R` o `Ctrl + F5`

**Mac:**
- `Cmd + Shift + R`

Esto fuerza al navegador a descargar los archivos nuevos.

### 3. Limpiar Cache del Navegador 🧹

1. Abre las DevTools (F12)
2. Click derecho en el botón de recargar
3. Selecciona "Vaciar caché y volver a cargar de forma forzada"

O manualmente:
- Chrome: `Ctrl + Shift + Delete` → Limpiar datos de navegación
- Edge: `Ctrl + Shift + Delete` → Limpiar datos de navegación

### 4. Limpiar y Recompilar 🔨

Si el error persiste después de esperar:

```powershell
cd "C:\Users\Manuel\Documents\git\codeflowx-govern-web"

# Detener el servidor (Ctrl+C)

# Limpiar cache
npm run clean

# O limpiar completamente
Remove-Item -Recurse -Force .next -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force node_modules\.cache -ErrorAction SilentlyContinue

# Reiniciar servidor
npm run dev
```

### 5. Verificar Errores de Compilación ⚠️

Revisa la terminal donde corre `npm run dev` para ver si hay errores de:
- TypeScript
- Imports faltantes
- Sintaxis incorrecta

### 6. Verificar que el Puerto no esté en Uso 🔌

```powershell
# Ver qué proceso usa el puerto 3002
Get-NetTCPConnection -LocalPort 3002

# Si hay otro proceso, detenerlo o cambiar el puerto en package.json
```

## Estado Actual

✅ **Servidor iniciado** en puerto 3002
✅ **Dependencias instaladas**
✅ **Cache limpiado**
⏳ **Compilando...** (espera 1-2 minutos)

## Próximos Pasos

1. **Espera 1-2 minutos** para que compile completamente
2. **Haz un hard refresh** en el navegador (Ctrl+Shift+R)
3. **Verifica la terminal** del servidor para ver el progreso de compilación
4. Si persiste, sigue los pasos de limpieza arriba

---

**Nota**: En proyectos grandes de Next.js, la primera compilación puede tomar varios minutos. Sé paciente.
