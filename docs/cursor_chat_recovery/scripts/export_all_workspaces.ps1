param(
  [string]$RepoRoot = "C:\Users\Manuel\Documents\git\codeflowx-studio",
  [string]$CursorRoaming = "C:\Users\Manuel\AppData\Roaming\Cursor"
)

$ErrorActionPreference = "Continue"

$workspaceStorageDir = Join-Path $CursorRoaming "User\workspaceStorage"
$checkpointsDir = Join-Path $CursorRoaming "User\globalStorage\anysphere.cursor-retrieval\checkpoints"
$baseOutputDir = Join-Path $RepoRoot "docs\cursor_chat_recovery\output_all_workspaces"
New-Item -ItemType Directory -Force -Path $baseOutputDir | Out-Null

$work = Join-Path $env:TEMP "cursor-chat-recovery-sqljs"
New-Item -ItemType Directory -Force -Path $work | Out-Null
Set-Location $work

if (!(Test-Path (Join-Path $work "package.json"))) {
  npm init -y | Out-Null
}
if (!(Test-Path (Join-Path $work "node_modules\sql.js"))) {
  Write-Host "Instalando sql.js..."
  npm install sql.js --no-fund --no-audit | Out-Null
}

$script = Join-Path $RepoRoot "docs\cursor_chat_recovery\scripts\export_from_cursor_storage.js"

$workspaces = Get-ChildItem $workspaceStorageDir -Directory | Where-Object {
  Test-Path (Join-Path $_.FullName "state.vscdb")
}

Write-Host ""
Write-Host "=== Encontrados $($workspaces.Count) workspaces con state.vscdb ===" -ForegroundColor Cyan

$summary = @()

foreach ($ws in $workspaces) {
  $workspaceId = $ws.Name
  $vscdb = Join-Path $ws.FullName "state.vscdb"
  $outputDir = Join-Path $baseOutputDir $workspaceId

  Write-Host ""
  Write-Host "--- Exportando workspace: $workspaceId ---" -ForegroundColor Yellow
  Write-Host "  vscdb: $vscdb"
  Write-Host "  salida: $outputDir"

  try {
    New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

    node $script --vscdb $vscdb --checkpoints $checkpointsDir --workspaceId $workspaceId --out $outputDir

    $composersFile = Join-Path $outputDir "composers.index.json"
    if (Test-Path $composersFile) {
      $composersData = Get-Content $composersFile -Raw | ConvertFrom-Json
      $composersCount = $composersData.count
    } else {
      $composersCount = 0
    }

    $checkpointsFile = Join-Path $outputDir "checkpoints.index.json"
    if (Test-Path $checkpointsFile) {
      $checkpointsData = Get-Content $checkpointsFile -Raw | ConvertFrom-Json
      $checkpointsCount = $checkpointsData.totalMatched
    } else {
      $checkpointsCount = 0
    }

    $summary += [PSCustomObject]@{
      WorkspaceId = $workspaceId
      Composers = $composersCount
      Checkpoints = $checkpointsCount
      Status = "OK"
      OutputDir = $outputDir
    }

    Write-Host "  [OK] $composersCount conversaciones, $checkpointsCount checkpoints" -ForegroundColor Green
  } catch {
    Write-Host "  [ERROR] $_" -ForegroundColor Red
    $summary += [PSCustomObject]@{
      WorkspaceId = $workspaceId
      Composers = 0
      Checkpoints = 0
      Status = "ERROR: $_"
      OutputDir = $outputDir
    }
  }
}

Write-Host ""
Write-Host "=== RESUMEN DE EXPORTACION ===" -ForegroundColor Cyan
$summary | Format-Table -AutoSize

$totalComposers = ($summary | Measure-Object -Property Composers -Sum).Sum
$totalCheckpoints = ($summary | Measure-Object -Property Checkpoints -Sum).Sum

Write-Host ""
Write-Host "Total: $totalComposers conversaciones, $totalCheckpoints checkpoints" -ForegroundColor Green
Write-Host "Output directory: $baseOutputDir" -ForegroundColor Green
