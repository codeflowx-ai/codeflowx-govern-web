param(
  [string]$WorkspaceId = "339b1d28ceef130c67a12d8146b259d9",
  [string]$RepoRoot = "C:\Users\Manuel\Documents\git\codeflowx-studio",
  [string]$WorkspaceDir = "",
  [string]$OutputDir = ""
)

$ErrorActionPreference = "Continue"

# Si no se especifican, usar rutas por defecto
if ([string]::IsNullOrWhiteSpace($WorkspaceDir)) {
  $workspaceDir = Join-Path $RepoRoot "docs\cursor_chat_recovery\output_all_workspaces\$WorkspaceId"
} else {
  $workspaceDir = $WorkspaceDir
}

if ([string]::IsNullOrWhiteSpace($OutputDir)) {
  $outputDir = Join-Path $RepoRoot "docs\cursor_chat_recovery\conversations_markdown\$WorkspaceId"
} else {
  $outputDir = $OutputDir
}
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

$composersFile = Join-Path $workspaceDir "composers.index.json"
$vscdbFile = Join-Path $workspaceDir "vscdb.keys.parsed.json"
$checkpointsFile = Join-Path $workspaceDir "checkpoints.index.json"

if (!(Test-Path $composersFile)) {
  Write-Host "Error: No se encontro composers.index.json en $workspaceDir" -ForegroundColor Red
  exit 1
}

$composersData = Get-Content $composersFile -Raw | ConvertFrom-Json
$vscdbData = if (Test-Path $vscdbFile) { Get-Content $vscdbFile -Raw | ConvertFrom-Json } else { $null }
$checkpointsData = if (Test-Path $checkpointsFile) { Get-Content $checkpointsFile -Raw | ConvertFrom-Json } else { $null }

$generations = @{}
if ($vscdbData -and $vscdbData.'aiService.generations') {
  foreach ($gen in $vscdbData.'aiService.generations') {
    if ($gen.generationUUID) {
      $generations[$gen.generationUUID] = $gen
    }
  }
}

$checkpointsByRequestId = @{}
if ($checkpointsData -and $checkpointsData.checkpoints) {
  foreach ($cp in $checkpointsData.checkpoints) {
    if ($cp.agentRequestId) {
      if (!$checkpointsByRequestId[$cp.agentRequestId]) {
        $checkpointsByRequestId[$cp.agentRequestId] = @()
      }
      $checkpointsByRequestId[$cp.agentRequestId] += $cp
    }
  }
}

function SanitizeFileName($name) {
  $invalidChars = [IO.Path]::GetInvalidFileNameChars()
  $sanitized = $name
  foreach ($char in $invalidChars) {
    $sanitized = $sanitized.Replace($char, '_')
  }
  $sanitized = $sanitized.Trim()
  if ([string]::IsNullOrWhiteSpace($sanitized)) {
    $sanitized = "sin_nombre"
  }
  return $sanitized
}

function FormatDate($unixMs) {
  if ($unixMs) {
    try {
      return [DateTimeOffset]::FromUnixTimeMilliseconds($unixMs).LocalDateTime.ToString("yyyy-MM-dd HH:mm:ss")
    } catch {
      return "N/A"
    }
  }
  return "N/A"
}

$exported = 0

foreach ($composer in $composersData.composers) {
  $composerId = $composer.composerId
  $name = if ($composer.name) { $composer.name } else { "Sin nombre" }
  $fileName = SanitizeFileName($name)
  $mdFile = Join-Path $outputDir "$fileName.md"

  $md = "# $name`r`n`r`n"
  $md += "## Metadatos`r`n`r`n"
  $md += "- **ID de Conversacion**: ``$composerId```r`n"
  $md += "- **Modo**: $($composer.unifiedMode)`r`n"
  $md += "- **Creada**: $(FormatDate $composer.createdAt)`r`n"
  $md += "- **Ultima actualizacion**: $(FormatDate $composer.lastUpdatedAt)`r`n"
  if ($composer.createdOnBranch) {
    $md += "- **Rama**: $($composer.createdOnBranch)`r`n"
  }
  if ($composer.subtitle) {
    $md += "- **Subtitulo**: $($composer.subtitle)`r`n"
  }
  $md += "`r`n"

  $md += "## Estadisticas`r`n`r`n"
  $md += "- **Archivos modificados**: $($composer.filesChangedCount)`r`n"
  $md += "- **Lineas agregadas**: $($composer.totalLinesAdded)`r`n"
  $md += "- **Lineas eliminadas**: $($composer.totalLinesRemoved)`r`n"
  $md += "- **Uso de contexto**: $([math]::Round($composer.contextUsagePercent, 2))%`r`n"
  $md += "`r`n"

  $md += "## Prompts y Generaciones`r`n`r`n"

  $relatedPrompts = @()
  foreach ($gen in $generations.Values) {
    if ($gen.type -eq "composer" -and $gen.textDescription) {
      $relatedPrompts += [PSCustomObject]@{
        Text = $gen.textDescription
        Timestamp = $gen.unixMs
        UUID = $gen.generationUUID
      }
    }
  }

  $relatedPrompts = $relatedPrompts | Sort-Object Timestamp

  if ($relatedPrompts.Count -gt 0) {
    foreach ($prompt in $relatedPrompts) {
      $date = FormatDate $prompt.Timestamp
      $md += "### Prompt - $date`r`n`r`n"
      $promptText = $prompt.Text
      $promptText = $promptText -replace '```', '``````'
      $codeBlock = '```' + $promptText + '```' + "`r`n`r`n"
      $md += $codeBlock

      if ($checkpointsByRequestId[$prompt.UUID]) {
        $md += "**Checkpoints asociados:**`r`n`r`n"
        foreach ($cp in $checkpointsByRequestId[$prompt.UUID]) {
          $cpDate = FormatDate $cp.startTrackingDateUnixMilliseconds
          $md += "- Checkpoint ``$($cp.checkpointId)`` - $cpDate`r`n"
          if ($cp.requestFiles -and $cp.requestFiles.Count -gt 0) {
            $md += "  - Archivos: "
            $files = $cp.requestFiles | ForEach-Object {
              $path = $_.fsPath
              $path -replace '.*[\\/]', ''
            }
            $md += ($files -join ", ") + "`r`n"
          }
          $md += "`r`n"
        }
      }
    }
  } else {
    $md += "*No se encontraron prompts asociados a esta conversacion.*`r`n`r`n"
  }

  if ($checkpointsByRequestId.ContainsKey($composerId)) {
    $md += "## Checkpoints de esta conversacion`r`n`r`n"
    foreach ($cp in $checkpointsByRequestId[$composerId]) {
      $cpDate = FormatDate $cp.startTrackingDateUnixMilliseconds
      $md += "### Checkpoint: $($cp.checkpointId)`r`n`r`n"
      $md += "- **Fecha**: $cpDate`r`n"
      $md += "- **Tamaño**: $($cp.fileSizeBytes) bytes`r`n"
      if ($cp.requestFiles -and $cp.requestFiles.Count -gt 0) {
        $md += "- **Archivos modificados**:`r`n"
        foreach ($file in $cp.requestFiles) {
          $md += "  - ``$($file.fsPath)```r`n"
        }
      }
      $md += "`r`n"
    }
  }

  $md += "---`r`n`r`n"
  $md += "*Este documento fue generado automaticamente desde los metadatos recuperados de Cursor.*`r`n"
  $md += "*Los transcripts completos de las respuestas del asistente no estan disponibles localmente.*`r`n"

  [System.IO.File]::WriteAllText($mdFile, $md, [System.Text.Encoding]::UTF8)
  $exported++
  Write-Host "Exportada: $name -> $fileName.md" -ForegroundColor Green
}

Write-Host "`n=== Resumen ===" -ForegroundColor Cyan
Write-Host "Conversaciones exportadas: $exported" -ForegroundColor Green
Write-Host "Ubicacion: $outputDir" -ForegroundColor Green
