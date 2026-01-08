// lib/i18n.ts
export type Locale = 'en' | 'es' | 'fr' | 'de' | 'it' | 'pt' | 'zh' | 'ja'

export const locales: Locale[] = ['en', 'es', 'fr', 'de', 'it', 'pt', 'zh', 'ja']

export const defaultLocale: Locale = 'en'

export const localeNames: Record<Locale, string> = {
    en: 'English',
    es: 'Español',
    fr: 'Français',
    de: 'Deutsch',
    it: 'Italiano',
    pt: 'Português',
    zh: '中文',
    ja: '日本語'
}

export const localeFlags: Record<Locale, string> = {
    en: '🇺🇸',
    es: '🇪🇸',
    fr: '🇫🇷',
    de: '🇩🇪',
    it: '🇮🇹',
    pt: '🇵🇹',
    zh: '🇨🇳',
    ja: '🇯🇵'
}