// Tipos para internacionalización
export type Language = "es" | "en" | "fr" | "de" | "it" | "pt";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export interface TranslationModule {
  es: any;
  en: any;
  fr: any;
  de: any;
  it: any;
  pt: any;
}
