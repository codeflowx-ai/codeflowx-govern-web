# Estructura Modular de Traducciones (i18n)

Esta estructura modular permite gestionar traducciones de forma escalable, facilitando la incorporación de múltiples idiomas.

## Estructura de Directorios

```
app/config/i18n/
├── index.ts              # Archivo principal que combina todos los módulos
├── types.ts              # Tipos TypeScript para i18n
├── config.ts             # Configuración de i18n
├── modules/              # Módulos de traducción organizados por funcionalidad
│   ├── common.ts         # Traducciones comunes (navegación, botones, etc.)
│   ├── compliance.ts     # Traducciones de Compliance
│   ├── governance.ts      # Traducciones de Governance
│   ├── projects.ts       # Traducciones de Proyectos
│   ├── bpmn.ts           # Traducciones de BPMN
│   └── ...               # Más módulos según necesidad
└── README.md             # Este archivo
```

## Cómo Añadir un Nuevo Módulo

1. Crear un nuevo archivo en `modules/` (ej: `modules/nuevo-modulo.ts`):

```typescript
import { TranslationModule } from "../types";

export const nuevoModuloTranslations: TranslationModule = {
  es: {
    // Traducciones en español
    titulo: "Título",
    subtitulo: "Subtítulo",
  },
  en: {
    // Traducciones en inglés
    titulo: "Title",
    subtitulo: "Subtitle",
  },
  fr: {
    // Traducciones en francés
    titulo: "Titre",
    subtitulo: "Sous-titre",
  },
  de: {
    // Traducciones en alemán
    titulo: "Titel",
    subtitulo: "Untertitel",
  },
  it: {
    // Traducciones en italiano
    titulo: "Titolo",
    subtitulo: "Sottotitolo",
  },
  pt: {
    // Traducciones en portugués
    titulo: "Título",
    subtitulo: "Subtítulo",
  },
};
```

2. Importar y añadir en `index.ts`:

```typescript
import { nuevoModuloTranslations } from "./modules/nuevo-modulo";

// En combineTranslations():
combined[lang] = {
  // ... otros módulos
  nuevoModulo: nuevoModuloTranslations[lang],
};
```

## Idiomas Soportados

Actualmente la aplicación soporta los siguientes idiomas:
- **es** - Español (idioma por defecto)
- **en** - Inglés
- **fr** - Francés
- **de** - Alemán
- **it** - Italiano
- **pt** - Portugués

## Cómo Añadir un Nuevo Idioma

1. Añadir el idioma a `types.ts`:

```typescript
export type Language = "es" | "en" | "fr" | "de" | "it" | "pt" | "nuevo_idioma";
```

2. Actualizar la interfaz `TranslationModule` en `types.ts`:

```typescript
export interface TranslationModule {
  es: any;
  en: any;
  fr: any;
  de: any;
  it: any;
  pt: any;
  nuevo_idioma: any; // Añadir aquí
}
```

3. Añadir el idioma a `config.ts`:

```typescript
availableLanguages: ["es", "en", "fr", "de", "it", "pt", "nuevo_idioma"],
```

4. Añadir traducciones en cada módulo existente:

```typescript
export const commonTranslations: TranslationModule = {
  es: { ... },
  en: { ... },
  fr: { ... },
  de: { ... },
  it: { ... },
  pt: { ... },
  nuevo_idioma: {
    // Traducciones en el nuevo idioma
  },
};
```

5. Actualizar `index.ts` para incluir el nuevo idioma en `combineTranslations()`:

```typescript
const languages: Language[] = ["es", "en", "fr", "de", "it", "pt", "nuevo_idioma"];
```

## Migración desde i18n.ts Monolítico

Para migrar módulos desde el archivo monolítico `i18n.ts`:

1. Identificar el módulo en `i18n.ts` (ej: `compliance: { ... }`)
2. Extraer las traducciones en español e inglés
3. Crear el archivo en `modules/compliance.ts`
4. Importar y añadir en `index.ts`
5. Probar que las traducciones funcionan correctamente

## Ventajas de esta Estructura

- ✅ **Escalable**: Fácil añadir nuevos idiomas
- ✅ **Mantenible**: Cada módulo en su propio archivo
- ✅ **Organizado**: Traducciones agrupadas por funcionalidad
- ✅ **Type-safe**: TypeScript ayuda a detectar errores
- ✅ **Colaborativo**: Múltiples desarrolladores pueden trabajar en paralelo
