# Script para aplicar cambios a todas las páginas de infrastructure
# 1. Eliminar DevelopmentBanner
# 2. Eliminar gradientes
# 3. Mover título y subtítulo al lateral izquierdo

$files = Get-ChildItem -Path "app\(app)\infrastructure" -Filter "page.tsx" -Recurse

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw

    # Eliminar import de DevelopmentBanner
    $content = $content -replace "import DevelopmentBanner from `"@/components/ui/development-banner`";\s*\n", ""

    # Eliminar el componente DevelopmentBanner
    $content = $content -replace "<div className=`"relative z-10`">\s*<DevelopmentBanner[^>]*/>\s*</div>\s*", ""

    # Eliminar partículas animadas
    $content = $content -replace "<div className=`"absolute inset-0 overflow-hidden pointer-events-none`">.*?</div>\s*", ""

    # Reemplazar gradiente del div principal
    $content = $content -replace "className=`"min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden`"", "className=`"min-h-screen bg-background`""

    # Eliminar z-10 de los divs internos
    $content = $content -replace "className=`"relative z-10", "className=`"relative"

    # Reemplazar gradientes de títulos
    $content = $content -replace "bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent", "text-foreground"

    # Reemplazar backdrop-blur y gradientes de cards
    $content = $content -replace "backdrop-blur-md bg-background/60 border-border/50", "border-border"
    $content = $content -replace "border-border/50", "border-border"

    Set-Content -Path $file.FullName -Value $content -NoNewline
    Write-Host "Fixed: $($file.FullName)"
}

Write-Host "All files processed!"

