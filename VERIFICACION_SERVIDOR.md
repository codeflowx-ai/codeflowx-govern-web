# ✅ SERVIDOR DE DESARROLLO INICIADO

## Estado

✅ **Dependencias instaladas** correctamente con `--legacy-peer-deps`
✅ **Servidor iniciado** en segundo plano

## URL

**http://localhost:3002**

## Verificaciones Recomendadas

### 1. Carga de la Aplicación
- [ ] La aplicación carga sin errores
- [ ] No hay errores en la consola del navegador
- [ ] La página principal se muestra correctamente

### 2. Módulos de Desarrollo (NO deben aparecer)
- [ ] **bpmn-designer** NO aparece en el menú
- [ ] **drools-editor** NO aparece en el menú
- [ ] **development-tools** NO aparece en el menú

### 3. Módulos de Gobierno (SÍ deben aparecer)
- [ ] **governance** aparece en el menú
- [ ] **bpmn processes** (bandeja de tareas) aparece en el menú
- [ ] Otros módulos del sistema funcionan correctamente

### 4. Navegación
- [ ] Las rutas `/governance` funcionan
- [ ] Las rutas `/bpmn/processes` funcionan
- [ ] Las rutas `/bpmn-designer`, `/drools-editor`, `/development-tools` devuelven 404 (no existen)

## Comandos Útiles

### Detener el servidor
Presiona `Ctrl+C` en la terminal donde está corriendo, o:
```powershell
# Encontrar el proceso
Get-Process node | Where-Object {$_.Path -like "*codeflowx-govern-web*"}

# Matar el proceso (si es necesario)
Stop-Process -Name node -Force
```

### Ver logs
El servidor está corriendo en segundo plano. Para ver los logs en tiempo real, ejecuta:
```powershell
cd "C:\Users\Manuel\Documents\git\codeflowx-govern-web"
npm run dev
```

### Recompilar
Si hay errores de compilación:
```powershell
npm run build
```

---

**Fecha**: 2025-12-26
**Puerto**: 3002
**Estado**: ✅ Servidor iniciado
