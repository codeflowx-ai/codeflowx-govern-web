# Script para instalar la tarea programada de Windows que ejecuta el export diario
# NOTA: Requiere ejecutarse como Administrador

$ErrorActionPreference = "Stop"

$scriptPath = Join-Path $PSScriptRoot "daily_export.ps1"
$taskName = "CursorChatDailyExport"
$description = "Exporta diariamente las conversaciones de Cursor a archivos markdown organizados por fechas"

# Verificar que el script existe
if (!(Test-Path $scriptPath)) {
  Write-Host "Error: No se encuentra el script daily_export.ps1" -ForegroundColor Red
  exit 1
}

# Verificar permisos de administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (!$isAdmin) {
  Write-Host "`n=== ADVERTENCIA: Se requieren permisos de Administrador ===" -ForegroundColor Yellow
  Write-Host "Para instalar la tarea programada, ejecuta este script como Administrador:" -ForegroundColor Yellow
  Write-Host "  1. Abre PowerShell como Administrador" -ForegroundColor Yellow
  Write-Host "  2. Navega a: $PSScriptRoot" -ForegroundColor Yellow
  Write-Host "  3. Ejecuta: .\install_daily_task.ps1" -ForegroundColor Yellow
  Write-Host "`nAlternativamente, puedes ejecutar el script manualmente cada dia:" -ForegroundColor Cyan
  Write-Host "  .\daily_export.ps1" -ForegroundColor Cyan
  Write-Host "`nO crear la tarea manualmente desde el Programador de Tareas de Windows." -ForegroundColor Cyan
  exit 0
}

# Eliminar tarea existente si existe
$existingTask = Get-ScheduledTask -TaskName $taskName -ErrorAction SilentlyContinue
if ($existingTask) {
  Write-Host "Eliminando tarea existente..." -ForegroundColor Yellow
  Unregister-ScheduledTask -TaskName $taskName -Confirm:$false
}

# Crear la acción (ejecutar PowerShell con el script)
$action = New-ScheduledTaskAction -Execute "powershell.exe" `
  -Argument "-ExecutionPolicy Bypass -File `"$scriptPath`""

# Crear el trigger (diario a las 2:00 AM)
$trigger = New-ScheduledTaskTrigger -Daily -At "2:00AM"

# Configurar el principal (ejecutar como usuario actual, sin requerir elevación)
$principal = New-ScheduledTaskPrincipal -UserId $env:USERNAME -LogonType Interactive -RunLevel Limited

# Configurar settings
$settings = New-ScheduledTaskSettingsSet `
  -AllowStartIfOnBatteries `
  -DontStopIfGoingOnBatteries `
  -StartWhenAvailable `
  -RunOnlyIfNetworkAvailable:$false

# Registrar la tarea
Register-ScheduledTask `
  -TaskName $taskName `
  -Action $action `
  -Trigger $trigger `
  -Principal $principal `
  -Settings $settings `
  -Description $description `
  -Force

Write-Host "`n=== Tarea programada instalada exitosamente ===" -ForegroundColor Green
Write-Host "Nombre de la tarea: $taskName" -ForegroundColor Cyan
Write-Host "Descripcion: $description" -ForegroundColor Cyan
Write-Host "Horario: Diario a las 2:00 AM" -ForegroundColor Cyan
Write-Host "`nPara verificar o modificar la tarea:" -ForegroundColor Yellow
Write-Host "  - Abre 'Programador de tareas' de Windows" -ForegroundColor Yellow
Write-Host "  - Busca la tarea: $taskName" -ForegroundColor Yellow
Write-Host "`nPara ejecutar manualmente:" -ForegroundColor Yellow
Write-Host "  .\daily_export.ps1" -ForegroundColor Yellow
