param(
  [string]$CursorRoaming = "C:\Users\Manuel\AppData\Roaming\Cursor",
  [string]$OutputBaseDir = "$env:USERPROFILE\Documents\prompts"
)

$ErrorActionPreference = "Continue"

$today = Get-Date -Format "yyyy-MM-dd"
$outputDir = Join-Path $OutputBaseDir $today
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

$workspaceStorageDir = Join-Path $CursorRoaming "User\workspaceStorage"
$checkpointsDir = Join-Path $CursorRoaming "User\globalStorage\anysphere.cursor-retrieval\checkpoints"

# Temp node project to load sql.js
$work = Join-Path $env:TEMP "cursor-chat-recovery-sqljs"
New-Item -ItemType Directory -Force -Path $work | Out-Null
Set-Location $work

if (!(Test-Path (Join-Path $work "package.json"))) {
  npm init -y | Out-Null
}
if (!(Test-Path (Join-Path $work "node_modules\sql.js"))) {
  Write-Host "Instalando sql.js..." -ForegroundColor Yellow
  npm install sql.js --no-fund --no-audit | Out-Null
}

$exportScript = Join-Path $PSScriptRoot "export_from_cursor_storage.js"
$exportConversationsScript = Join-Path $PSScriptRoot "export_conversations_to_markdown.ps1"

# Obtener todos los workspaces
$workspaces = Get-ChildItem $workspaceStorageDir -Directory | Where-Object {
  Test-Path (Join-Path $_.FullName "state.vscdb")
}

Write-Host "`n=== Exportacion diaria de conversaciones - $today ===" -ForegroundColor Cyan
Write-Host "Workspaces encontrados: $($workspaces.Count)" -ForegroundColor Cyan
Write-Host "Directorio de salida: $outputDir" -ForegroundColor Cyan

$summary = @()
$totalConversations = 0

foreach ($ws in $workspaces) {
  $workspaceId = $ws.Name
  $vscdb = Join-Path $ws.FullName "state.vscdb"
  $workspaceOutputDir = Join-Path $outputDir "workspace_$workspaceId"

  Write-Host "`n--- Procesando workspace: $workspaceId ---" -ForegroundColor Yellow

  try {
    New-Item -ItemType Directory -Force -Path $workspaceOutputDir | Out-Null

    # Exportar datos del workspace
    node $exportScript --vscdb $vscdb --checkpoints $checkpointsDir --workspaceId $workspaceId --out $workspaceOutputDir

    # Exportar conversaciones a markdown directamente al directorio de salida
    $conversationsOutputDir = Join-Path $workspaceOutputDir "conversations"

    # Usar el script original con parámetros personalizados
    & $exportConversationsScript -WorkspaceId $workspaceId -WorkspaceDir $workspaceOutputDir -OutputDir $conversationsOutputDir

    # Leer estadísticas
    $composersFile = Join-Path $workspaceOutputDir "composers.index.json"
    $composersCount = 0
    if (Test-Path $composersFile) {
      $composersData = Get-Content $composersFile -Raw | ConvertFrom-Json
      $composersCount = $composersData.count
    }

    $checkpointsFile = Join-Path $workspaceOutputDir "checkpoints.index.json"
    $checkpointsCount = 0
    if (Test-Path $checkpointsFile) {
      $checkpointsData = Get-Content $checkpointsFile -Raw | ConvertFrom-Json
      $checkpointsCount = $checkpointsData.totalMatched
    }

    $totalConversations += $composersCount

    $summary += [PSCustomObject]@{
      WorkspaceId = $workspaceId
      Conversaciones = $composersCount
      Checkpoints = $checkpointsCount
      Status = "OK"
    }

    Write-Host "  [OK] $composersCount conversaciones, $checkpointsCount checkpoints" -ForegroundColor Green
  } catch {
    Write-Host "  [ERROR] $_" -ForegroundColor Red
    $summary += [PSCustomObject]@{
      WorkspaceId = $workspaceId
      Conversaciones = 0
      Checkpoints = 0
      Status = "ERROR: $_"
    }
  }
}

# Crear resumen del día
$summaryFile = Join-Path $outputDir "resumen_$today.md"
$summaryMd = @"
# Resumen de Exportacion Diaria - $today

## Fecha: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")

## Total de Workspaces: $($workspaces.Count)
## Total de Conversaciones: $totalConversations

## Detalle por Workspace

"@

foreach ($item in $summary) {
  $summaryMd += @"
### Workspace: $($item.WorkspaceId)
- **Conversaciones**: $($item.Conversaciones)
- **Checkpoints**: $($item.Checkpoints)
- **Estado**: $($item.Status)

"@
}

$summaryMd += @"

## Ubicacion de Archivos

Todos los archivos se encuentran en: ``$outputDir``

### Estructura:
- ``workspace_<id>/`` - Datos exportados de cada workspace
  - ``conversations/`` - Archivos markdown de cada conversacion
  - ``composers.index.json`` - Indice de conversaciones
  - ``checkpoints.index.json`` - Indice de checkpoints
  - ``vscdb.keys.parsed.json`` - Datos completos del state.vscdb

## Nota

Los transcripts completos de las respuestas del asistente no estan disponibles localmente.
Solo se pueden recuperar los prompts del usuario, metadatos y checkpoints.

"@

$summaryMd | Out-File -FilePath $summaryFile -Encoding UTF8

Write-Host "`n=== Resumen ===" -ForegroundColor Cyan
$summary | Format-Table -AutoSize
Write-Host "`nTotal conversaciones exportadas: $totalConversations" -ForegroundColor Green
Write-Host "Resumen guardado en: $summaryFile" -ForegroundColor Green
Write-Host "Todos los archivos en: $outputDir" -ForegroundColor Green
