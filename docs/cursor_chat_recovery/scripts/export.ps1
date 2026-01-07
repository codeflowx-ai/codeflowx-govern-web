param(
  [string]$WorkspaceStorageId = "bef7dc40111986446ddc3f799b7f9f22",
  [string]$RepoRoot = "C:\Users\Manuel\Documents\git\codeflowx-studio",
  [string]$CursorRoaming = "C:\Users\Manuel\AppData\Roaming\Cursor"
)

$ErrorActionPreference = "Stop"

$outputDir = Join-Path $RepoRoot "docs\cursor_chat_recovery\output"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

$vscdb = Join-Path $CursorRoaming "User\workspaceStorage\$WorkspaceStorageId\state.vscdb"
$checkpointsDir = Join-Path $CursorRoaming "User\globalStorage\anysphere.cursor-retrieval\checkpoints"

Write-Host "Exportando desde:" $vscdb
Write-Host "Checkpoints:" $checkpointsDir
Write-Host "Salida:" $outputDir

# Temp node project to load sql.js without native deps
$work = Join-Path $env:TEMP "cursor-chat-recovery-sqljs"
New-Item -ItemType Directory -Force -Path $work | Out-Null
Set-Location $work

if (!(Test-Path (Join-Path $work "package.json"))) { npm init -y | Out-Null }
npm install sql.js --no-fund --no-audit | Out-Null

$script = Join-Path $RepoRoot "docs\cursor_chat_recovery\scripts\export_from_cursor_storage.js"
node $script --vscdb $vscdb --checkpoints $checkpointsDir --workspaceId $WorkspaceStorageId --out $outputDir

Write-Host "OK. Revisa:" $outputDir
