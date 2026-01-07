# ⚠️ ARCHIVOS PENDIENTES DE COPIA

## Archivos Modificados Recientemente (24/12/2025)

Los siguientes archivos fueron modificados **DESPUÉS** de la copia inicial y pueden necesitar actualizarse en `codeflowx-govern-web`:

### Archivos Modificados HOY (24/12/2025 11:53:13)

1. ✅ `components/layout/Header.tsx`
2. ✅ `app/config/i18n/modules/layout.ts`
3. ✅ `components/layout/sidebar.tsx`
4. ✅ `app/config/modules.ts`

### Archivos Modificados Recientemente

5. ✅ `app/(app)/governance/prompts/page.tsx` (24/12/2025 11:05:21)
6. ✅ `app/(app)/governance/security/page.tsx` (21/12/2025 16:56:14)

## Recomendación

Si se han hecho modificaciones en `codeflowx-studio` después de la copia, deberías:

1. **Verificar si estos archivos existen y están actualizados en `codeflowx-govern-web`**
2. **Copiar solo los archivos modificados recientemente** si hay diferencias
3. **O hacer una copia completa nuevamente** si hay muchas modificaciones

## Comando para Verificar

```powershell
# Verificar si un archivo es más reciente en origen
$orig = Get-Item "C:\Users\Manuel\Documents\git\codeflowx-studio\components\layout\Header.tsx"
$dest = Get-Item "C:\Users\Manuel\Documents\git\codeflowx-govern-web\components\layout\Header.tsx"
$diff = $orig.LastWriteTime - $dest.LastWriteTime
if ($diff.TotalHours -gt 0) {
    Write-Host "Origen es $([math]::Round($diff.TotalHours, 2)) horas más reciente"
}
```

---

**Fecha de verificación**: 2025-12-26
**Última copia**: 2025-12-26 (hoy)
