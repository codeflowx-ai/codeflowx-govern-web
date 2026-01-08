# 🎨 Configuración de Branding

Este documento explica cómo personalizar el logo y nombre de la empresa que se muestra en el header de la aplicación.

## 📋 Descripción

El sistema de branding permite configurar:
- **Logo**: Imagen del logo del cliente
- **Nombre de la empresa**: Texto que se muestra junto al logo
- **Estilo del nombre**: Con o sin gradientes de colores
- **Modo logo solo**: Mostrar solo el logo sin texto

## ⚙️ Configuración

### Variables de Entorno

Puedes configurar el branding mediante variables de entorno en tu archivo `.env.local`:

```bash
# Ruta al logo (relativa a /public)
NEXT_PUBLIC_BRANDING_LOGO_PATH=/img/knowmad-mood-logo.svg

# Texto alternativo para el logo (accesibilidad)
NEXT_PUBLIC_BRANDING_LOGO_ALT=Knowmad Mood

# Nombre de la empresa/cliente
NEXT_PUBLIC_BRANDING_COMPANY_NAME=Knowmad Mood

# Usar gradientes de colores en el nombre (true/false)
NEXT_PUBLIC_BRANDING_USE_GRADIENT=false

# Mostrar solo el logo sin texto (true/false)
NEXT_PUBLIC_BRANDING_LOGO_ONLY=false
```

### Valores por Defecto

Si no se configuran las variables de entorno, el sistema usa estos valores por defecto:

- **Logo**: `/img/knowmad-mood-logo.svg`
- **Alt**: `Knowmad Mood`
- **Nombre**: `Knowmad Mood`
- **Gradientes**: `false` (nombre simple)
- **Logo solo**: `false` (muestra logo + nombre)

## 📝 Ejemplos de Configuración

### Ejemplo 1: Logo de Knowmad Mood (configuración actual)

```bash
NEXT_PUBLIC_BRANDING_LOGO_PATH=/img/knowmad-mood-logo.svg
NEXT_PUBLIC_BRANDING_LOGO_ALT=Knowmad Mood
NEXT_PUBLIC_BRANDING_COMPANY_NAME=Knowmad Mood
NEXT_PUBLIC_BRANDING_USE_GRADIENT=false
NEXT_PUBLIC_BRANDING_LOGO_ONLY=false
```

**Resultado**: Muestra el logo de Knowmad Mood + texto "Knowmad Mood" sin gradientes.

### Ejemplo 2: Solo Logo (sin texto)

```bash
NEXT_PUBLIC_BRANDING_LOGO_PATH=/img/knowmad-mood-logo.svg
NEXT_PUBLIC_BRANDING_LOGO_ALT=Knowmad Mood
NEXT_PUBLIC_BRANDING_LOGO_ONLY=true
```

**Resultado**: Muestra solo el logo, sin texto.

### Ejemplo 3: Nombre con Gradientes (estilo CodeflowX)

```bash
NEXT_PUBLIC_BRANDING_LOGO_PATH=/img/knowmad-mood-logo.svg
NEXT_PUBLIC_BRANDING_LOGO_ALT=Knowmad Mood
NEXT_PUBLIC_BRANDING_COMPANY_NAME=Knowmad Mood
NEXT_PUBLIC_BRANDING_USE_GRADIENT=true
NEXT_PUBLIC_BRANDING_LOGO_ONLY=false
```

**Resultado**: Muestra el logo + nombre "Knowmad Mood" con gradientes de colores (similar al estilo original de CodeflowX).

### Ejemplo 4: Volver a CodeflowX

```bash
NEXT_PUBLIC_BRANDING_LOGO_PATH=/img/icono1.png
NEXT_PUBLIC_BRANDING_LOGO_ALT=CodeflowX Icon
NEXT_PUBLIC_BRANDING_COMPANY_NAME=CodeflowX
NEXT_PUBLIC_BRANDING_USE_GRADIENT=true
NEXT_PUBLIC_BRANDING_LOGO_ONLY=false
```

**Resultado**: Muestra el logo original de CodeflowX + nombre "CodeflowX" con gradientes.

## 🔧 Ubicación del Logo

Los logos deben estar en la carpeta `public/img/`:

```
public/
  └── img/
      ├── knowmad-mood-logo.svg
      ├── icono1.png
      └── [tu-logo].svg o .png
```

**Formato recomendado:**
- **SVG**: Mejor calidad y escalabilidad
- **PNG**: Si necesitas transparencia
- **Tamaño**: El logo se ajusta automáticamente a altura de 40px (h-10)

## 📖 Uso en el Código

El branding se importa y usa automáticamente en el componente `Header`:

```typescript
import { brandingConfig } from "@/app/config/branding";

// El Header usa automáticamente:
// - brandingConfig.logoPath
// - brandingConfig.logoAlt
// - brandingConfig.companyName
// - brandingConfig.useGradientName
// - brandingConfig.logoOnly
```

## 🔄 Cambios en Tiempo de Ejecución

**Nota**: Los cambios en las variables de entorno requieren reiniciar el servidor de desarrollo:

```bash
# Detener el servidor (Ctrl+C)
# Reiniciar
npm run dev
```

## 📚 Referencias

- **Archivo de configuración**: `app/config/branding.ts`
- **Componente Header**: `components/layout/Header.tsx`
- **Logo por defecto**: `public/img/knowmad-mood-logo.svg`
