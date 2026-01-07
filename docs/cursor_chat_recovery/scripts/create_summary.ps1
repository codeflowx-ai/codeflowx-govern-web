param(
  [string]$RepoRoot = "C:\Users\Manuel\Documents\git\codeflowx-studio"
)

$baseOutputDir = Join-Path $RepoRoot "docs\cursor_chat_recovery\output_all_workspaces"
$summaryFile = Join-Path $RepoRoot "docs\cursor_chat_recovery\RESUMEN_COMPLETO.md"

$workspaces = Get-ChildItem $baseOutputDir -Directory

$summary = @"
# Resumen Completo de Recuperacion de Chats/Agentes de Cursor

## Fecha de exportacion: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")

## Total de Workspaces Exportados: $($workspaces.Count)

"@

$totalComposers = 0
$totalCheckpoints = 0
$allComposers = @()

foreach ($ws in $workspaces) {
  $workspaceId = $ws.Name
  $composersFile = Join-Path $ws.FullName "composers.index.json"
  $checkpointsFile = Join-Path $ws.FullName "checkpoints.index.json"
  $vscdbFile = Join-Path $ws.FullName "vscdb.keys.parsed.json"

  $composersCount = 0
  $checkpointsCount = 0

  if (Test-Path $composersFile) {
    $composersData = Get-Content $composersFile -Raw | ConvertFrom-Json
    $composersCount = $composersData.count
    if ($composersData.composers) {
      foreach ($comp in $composersData.composers) {
        $allComposers += [PSCustomObject]@{
          WorkspaceId = $workspaceId
          ComposerId = $comp.composerId
          Name = $comp.name
          CreatedAt = if ($comp.createdAt) { [DateTimeOffset]::FromUnixTimeMilliseconds($comp.createdAt).LocalDateTime.ToString("yyyy-MM-dd HH:mm:ss") } else { "N/A" }
          LastUpdatedAt = if ($comp.lastUpdatedAt) { [DateTimeOffset]::FromUnixTimeMilliseconds($comp.lastUpdatedAt).LocalDateTime.ToString("yyyy-MM-dd HH:mm:ss") } else { "N/A" }
          UnifiedMode = $comp.unifiedMode
          Subtitle = $comp.subtitle
          FilesChanged = $comp.filesChangedCount
          LinesAdded = $comp.totalLinesAdded
          LinesRemoved = $comp.totalLinesRemoved
        }
      }
    }
  }

  if (Test-Path $checkpointsFile) {
    $checkpointsData = Get-Content $checkpointsFile -Raw | ConvertFrom-Json
    $checkpointsCount = $checkpointsData.totalMatched
  }

  $totalComposers += $composersCount
  $totalCheckpoints += $checkpointsCount

  $summary += @"

### Workspace: $workspaceId
- **Conversaciones/Composers**: $composersCount
- **Checkpoints**: $checkpointsCount
- **Ubicacion**: `$($ws.FullName)`

"@
}

$summary += @"

## Totales Generales
- **Total Conversaciones**: $totalComposers
- **Total Checkpoints**: $totalCheckpoints

## Lista Completa de Conversaciones

"@

$allComposers = $allComposers | Sort-Object LastUpdatedAt -Descending

foreach ($comp in $allComposers) {
  $summary += @"
- **$($comp.Name)** (Workspace: $($comp.WorkspaceId))
  - Modo: $($comp.UnifiedMode)
  - Creada: $($comp.CreatedAt)
  - Ultima actualizacion: $($comp.LastUpdatedAt)
  - Archivos modificados: $($comp.FilesChanged)
  - Lineas agregadas: $($comp.LinesAdded)
  - Lineas eliminadas: $($comp.LinesRemoved)
  - Subtitulo: $($comp.Subtitle)

"@
}

$summary | Out-File -FilePath $summaryFile -Encoding UTF8

Write-Host "Resumen creado en: $summaryFile" -ForegroundColor Green
Write-Host "Total conversaciones: $totalComposers" -ForegroundColor Cyan
Write-Host "Total checkpoints: $totalCheckpoints" -ForegroundColor Cyan
