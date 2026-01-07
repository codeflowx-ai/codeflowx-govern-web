
# Revisión de Estilos y Traducciones - CodeflowX Studio

## 📋 Componentes Identificados

### 1. Layouts Principales

#### `app/layout.tsx` - Layout Raíz
- **Ubicación**: `app/layout.tsx`
- **Función**: Layout principal de Next.js, incluye configuración de tema y metadatos
- **Estilos**: Variables CSS del tema, prevención de FOUC
- **Traducciones**: Ninguna (solo metadatos)

#### `app/(app)/layout.tsx` - Layout de Aplicación
- **Ubicación**: `app/(app)/layout.tsx`
- **Función**: Wrapper para páginas de la aplicación, usa `AppLayout`
- **Estilos**: Ninguno específico
- **Traducciones**: Ninguna

#### `components/layout/AppLayout.tsx` - Layout Principal
- **Ubicación**: `components/layout/AppLayout.tsx`
- **Función**: Layout completo con Header, Sidebar, Main y Footer
- **Estilos**: Variables CSS del tema
- **Traducciones necesarias**:
  - ✅ `loadingTheme` (línea 22) - Ya usa traducción
  - ✅ `systemActive` (línea 51) - Ya usa traducción
  - ✅ `aiPowered` (línea 55) - Ya usa traducción
  - ✅ `documentation` (línea 66) - Ya usa traducción
  - ✅ `support` (línea 73) - Ya usa traducción
  - ✅ `status` (línea 80) - Ya usa traducción

#### `components/layout/main-layout.tsx` - Layout Alternativo
- **Ubicación**: `components/layout/main-layout.tsx`
- **Función**: Layout alternativo (no se usa actualmente)
- **Estilos**: Clases Tailwind básicas
- **Traducciones**: Ninguna

### 2. Sidebar

#### `components/layout/sidebar.tsx` - Barra Lateral
- **Ubicación**: `components/layout/sidebar.tsx`
- **Función**: Menú de navegación lateral con módulos y páginas
- **Estilos**: Variables CSS del tema, colores del sidebar
- **Traducciones**:
  - ❌ **Ninguna** - Los nombres de menú vienen de `modules.ts` y están hardcodeados
  - Los nombres de módulos y menús deberían usar traducciones

### 3. Header

#### `components/layout/Header.tsx` - Encabezado
- **Ubicación**: `components/layout/Header.tsx`
- **Función**: Barra superior con selector de módulos, usuario, tema e idioma
- **Estilos**: Variables CSS del tema para header
- **Traducciones necesarias**:
  - ❌ `"Módulos principales"` (línea 218) - **HARDCODEADO**
  - ❌ `"Cambiar módulo / área de trabajo"` (línea 198) - **HARDCODEADO**
  - ❌ `"Secured with 2FA"` (línea 313) - **HARDCODEADO**
  - ❌ `"Configuración"` (línea 411) - **HARDCODEADO**
  - ❌ `"Seguridad"` (línea 425) - **HARDCODEADO**
  - ❌ `"Cerrar sesión"` (línea 447) - **HARDCODEADO**
  - ❌ `"¿Cerrar sesión?"` (línea 479) - **HARDCODEADO**
  - ❌ `"Deberás ingresar tus credenciales y código 2FA para volver a acceder a CodeflowX."` (línea 485) - **HARDCODEADO**
  - ❌ `"Cancelar"` (línea 505) - **HARDCODEADO**
  - ✅ `welcomeMessage` (línea 300) - Ya usa traducción

### 4. Login

#### `app/auth/login/page.tsx` - Página de Login
- **Ubicación**: `app/auth/login/page.tsx`
- **Función**: Página de autenticación con formulario y usuarios demo
- **Estilos**: Gradientes, efectos glassmorphism, animaciones
- **Traducciones necesarias** (TODOS HARDCODEADOS):
  - ❌ `"Plataforma de Inteligencia Artificial"` (línea 176)
  - ❌ `"🚀 Demo de CodeflowX"` (línea 183)
  - ❌ `"Bienvenido a la demo"` (línea 187)
  - ❌ `"datos sintéticos"` (línea 190)
  - ❌ `"Explora cada módulo"` (línea 199)
  - ❌ `"Perfiles demo disponibles:"` (línea 211)
  - ❌ `"💎 Únete al Ecosistema CodeflowX"` (línea 221)
  - ❌ `"Early Adopter"`, `"Startup"`, `"Partner"` (líneas 225-227)
  - ❌ `"Visitar CodeflowX.com"` (línea 235)
  - ❌ `"Te avisaremos cuando la plataforma esté lista para producción"` (línea 251)
  - ❌ `"Acceso a la Plataforma"` (línea 266)
  - ❌ `"Ingresa tus credenciales para continuar"` (línea 269)
  - ❌ `"Autenticación"` (línea 295)
  - ❌ `"Ingresa tu email y contraseña"` (línea 299)
  - ❌ `"Hard Refresh (DEV)"` (línea 308)
  - ❌ `"Email"` (línea 319)
  - ❌ `"Ingresa tu email"` (línea 324)
  - ❌ `"Contraseña"` (línea 342)
  - ❌ `"Ingresa tu contraseña"` (línea 348)
  - ❌ `"Autenticando..."` (línea 378)
  - ❌ `"Acceder"` (línea 383)
  - ❌ `"Credenciales inválidas. Por favor, verifica tu email y contraseña."` (línea 77)
  - ❌ `"Mostrar"` / `"Ocultar"` (línea 406)
  - ❌ `"Usuarios Demo"` (línea 406)
  - ❌ `"Filtrar por rol:"` (línea 414)
  - ❌ `"Todos los roles"` (línea 421)
  - ❌ `"CodeflowX v2.0.1 • Plataforma de IA"` (línea 477)

## 🎨 Estilos Identificados

### Archivos de Estilos

1. **`app/globals.css`**
   - Variables CSS del tema (`--theme-*`)
   - Estilos para tema `trekker-federation`
   - Estilos para header, sidebar, footer
   - Estilos para tablas, botones, inputs
   - Animaciones y efectos visuales
   - Estilos del Page Designer
   - Estilos del AI Playground

### Variables CSS del Tema

```css
--theme-primary
--theme-secondary
--theme-accent
--theme-background
--theme-surface
--theme-card
--theme-popover
--theme-text
--theme-textSecondary
--theme-textMuted
--theme-border
--theme-borderLight
--theme-success
--theme-warning
--theme-error
--theme-info
--theme-hover
--theme-hoverLight
--theme-shadow
--theme-shadowLight
--theme-header-background
--theme-header-text
--theme-header-border
--theme-header-hover
--theme-sidebar-background
--theme-sidebar-text
--theme-sidebar-border
--theme-sidebar-hover
--theme-sidebar-active
--theme-footer-background
--theme-footer-text
--theme-footer-border
```

## 📝 Traducciones Existentes

### Archivos de Traducción

1. **`app/config/i18n/index.ts`** - Hook principal de traducciones
2. **`app/config/i18n/config.ts`** - Configuración de idiomas
3. **`app/config/i18n/modules/common.ts`** - Traducciones comunes
4. **`app/config/i18n/modules/dashboard.ts`** - Traducciones del dashboard
5. **`app/config/i18n/modules/governance/`** - Traducciones de governance

### Idiomas Soportados

- ✅ Español (es) - Idioma por defecto
- ✅ Inglés (en)
- ✅ Francés (fr)
- ✅ Alemán (de)
- ✅ Italiano (it)
- ✅ Portugués (pt)

## 🔧 Acciones Recomendadas

### 1. ✅ Crear Módulo de Traducciones para Layout - COMPLETADO

✅ Creado `app/config/i18n/modules/layout.ts` con:

```typescript
export const layoutTranslations: TranslationModule = {
  es: {
    modules: "Módulos principales",
    changeModule: "Cambiar módulo / área de trabajo",
    securedWith2FA: "Secured with 2FA",
    settings: "Configuración",
    security: "Seguridad",
    logout: "Cerrar sesión",
    logoutConfirm: "¿Cerrar sesión?",
    logoutMessage: "Deberás ingresar tus credenciales y código 2FA para volver a acceder a CodeflowX.",
    cancel: "Cancelar",
    loadingTheme: "Cargando tema...",
    systemActive: "Sistema Activo",
    aiPowered: "Impulsado por IA",
    documentation: "Documentación",
    support: "Soporte",
    status: "Estado",
  },
  en: { /* ... */ },
  fr: { /* ... */ },
  de: { /* ... */ },
  it: { /* ... */ },
  pt: { /* ... */ },
};
```

### 2. ✅ Crear Módulo de Traducciones para Login - COMPLETADO

✅ Creado `app/config/i18n/modules/auth.ts` con todas las traducciones del login (25+ textos traducidos a 6 idiomas).

### 3. ⏳ Actualizar Componentes - PENDIENTE

- **Header.tsx**: Reemplazar textos hardcodeados con `t("layout.header.*")`
- **sidebar.tsx**: Agregar traducciones para nombres de módulos y menús
- **login/page.tsx**: Reemplazar todos los textos hardcodeados con `t("auth.login.*")`
- **AppLayout.tsx**: Ya usa traducciones correctamente (`t("layout.footer.*")`)

### 4. Revisar Estilos

- Verificar consistencia de variables CSS
- Asegurar que todos los componentes usen variables del tema
- Revisar responsive design
- Verificar accesibilidad (contrastes, tamaños de fuente)

## 📊 Resumen de Textos Hardcodeados

| Componente | Textos Hardcodeados | Prioridad |
|------------|---------------------|-----------|
| Header | 9 textos | 🔴 Alta |
| Login | 25+ textos | 🔴 Alta |
| Sidebar | Nombres de módulos | 🟡 Media |
| AppLayout | 0 (ya traducido) | ✅ OK |

## 🎯 Próximos Pasos

1. ✅ Identificar componentes (COMPLETADO)
2. ✅ Crear módulos de traducción para layout y auth (COMPLETADO)
3. ⏳ Actualizar componentes para usar traducciones (PENDIENTE)
   - Header.tsx: Usar `t("layout.header.*")`
   - sidebar.tsx: Usar `t("layout.sidebar.*")` y traducir nombres de módulos
   - login/page.tsx: Usar `t("auth.login.*")`
4. ⏳ Revisar y estandarizar estilos (PENDIENTE)
5. ⏳ Probar cambios en todos los idiomas (PENDIENTE)

## 📦 Archivos Creados

### Módulos de Traducción

1. **`app/config/i18n/modules/layout.ts`**
   - Traducciones para Header (9 textos)
   - Traducciones para Sidebar (1 texto)
   - Traducciones para Footer (6 textos)
   - Total: 16 textos × 6 idiomas = 96 traducciones

2. **`app/config/i18n/modules/auth.ts`**
   - Traducciones para Login (25+ textos)
   - Total: 25 textos × 6 idiomas = 150 traducciones

3. **`app/config/i18n/index.ts`** (Actualizado)
   - Importa y combina `layoutTranslations` y `authTranslations`
   - Disponible como `layout` y `auth` en el hook `useTranslation()`

### Uso en Componentes

```typescript
// Header.tsx
const { t } = useTranslation();
t("layout.header.modules") // "Módulos principales"
t("layout.header.logout") // "Cerrar sesión"
t("layout.header.welcomeMessage", undefined, { name: "Juan" }) // "Bienvenido, Juan"

// Login page.tsx
t("auth.login.title") // "Plataforma de Inteligencia Artificial"
t("auth.login.access") // "Acceder"
t("auth.login.email") // "Email"
```
