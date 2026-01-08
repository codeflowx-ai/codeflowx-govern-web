// hooks/useTranslation.ts
'use client'

import { useState } from 'react'

// Traducciones básicas para el designer
const translations = {
    en: {
        'common.back': 'Back',
        'common.save': 'Save',
        'common.preview': 'Preview'
    },
    es: {
        'common.back': 'Volver',
        'common.save': 'Guardar',
        'common.preview': 'Vista previa'
    }
}

export function useTranslation() {
    const [locale, setLocale] = useState<'en' | 'es'>('en')

    const t = (key: string) => {
        return translations[locale][key as keyof typeof translations['en']] || key
    }

    const changeLocale = (newLocale: 'en' | 'es') => {
        setLocale(newLocale)
    }

    return {
        t,
        locale,
        changeLocale
    }
}