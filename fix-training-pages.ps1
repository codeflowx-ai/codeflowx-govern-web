$files = Get-ChildItem -Path "app\(app)\training" -Recurse -Filter "page.tsx"

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw

    # Quitar gradientes de títulos
    $content = $content -replace 'bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent', 'text-foreground'

    # Quitar gradientes de cards y cambiar a fondo normal
    $content = $content -replace 'bg-gradient-to-br from-[^"]+ to-[^"]+ border-0 text-white', 'bg-background/60 border-border/50'
    $content = $content -replace 'bg-gradient-to-br from-[^"]+ to-[^"]+ border-0 text-white', 'bg-background/60 border-border/50'

    # Quitar opacity-90 de títulos en cards
    $content = $content -replace 'opacity-90', ''

    # Cambiar text-center a izquierda en títulos principales
    $content = $content -replace 'text-center space-y-2', 'space-y-2'
    $content = $content -replace 'flex items-center justify-center gap-3', 'flex items-center gap-3'

    Set-Content -Path $file.FullName -Value $content -NoNewline
    Write-Host "Fixed: $($file.FullName)"
}

Write-Host "Done!"
