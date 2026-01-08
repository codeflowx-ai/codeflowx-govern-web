# Script para mover módulos de desarrollo de codeflowx-govern-web a codeflowx-govern-studio-web
# Módulos de DESARROLLO que deben moverse:
# - bpmn-designer (diseñador BPMN)
# - drools-editor (editor de reglas Drools)
# - development-tools (herramientas de desarrollo)

$sourceDir = "C:\Users\Manuel\Documents\git\codeflowx-govern-web"
$targetDir = "C:\Users\Manuel\Documents\git\codeflowx-govern-studio-web"

# Módulos de desarrollo que deben moverse
$desarrolloModules = @(
    "app\(app)\bpmn-designer",
    "app\(app)\drools-editor",
    "app\(app)\development-tools"
)

Write-Host "=== MOVIENDO MÓDULOS DE DESARROLLO ===" -ForegroundColor Cyan
Write-Host "Origen: $sourceDir" -ForegroundColor Yellow
Write-Host "Destino: $targetDir" -ForegroundColor Yellow
Write-Host ""

foreach ($module in $desarrolloModules) {
    $sourcePath = Join-Path $sourceDir $module
    $targetPath = Join-Path $targetDir $module
    $moduleName = Split-Path $module -Leaf

    Write-Host "Moviendo: $moduleName" -ForegroundColor Green

    if (Test-Path $sourcePath) {
        # Crear directorio padre si no existe
        $targetParent = Split-Path $targetPath -Parent
        if (-not (Test-Path $targetParent)) {
            New-Item -ItemType Directory -Path $targetParent -Force | Out-Null
            Write-Host "  Creado directorio padre: $targetParent" -ForegroundColor Gray
        }

        # Si el destino ya existe, respaldar
        if (Test-Path $targetPath) {
            $backupPath = "$targetPath.backup-$(Get-Date -Format 'yyyyMMdd-HHmmss')"
            Write-Host "  ⚠️  El directorio ya existe en destino. Respaldando a: $backupPath" -ForegroundColor Yellow
            Move-Item -Path $targetPath -Destination $backupPath -Force
        }

        # Mover el directorio
        try {
            Move-Item -Path $sourcePath -Destination $targetPath -Force
            Write-Host "  ✅ Movido exitosamente" -ForegroundColor Green
        } catch {
            Write-Host "  ❌ Error al mover: $_" -ForegroundColor Red
        }
    } else {
        Write-Host "  ⚠️  No existe en origen: $sourcePath" -ForegroundColor Yellow
    }
    Write-Host ""
}

Write-Host "=== PROCESO COMPLETADO ===" -ForegroundColor Cyan
