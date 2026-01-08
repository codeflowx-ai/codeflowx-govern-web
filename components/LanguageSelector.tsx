// components/LanguageSelector.tsx
'use client'

import { useState } from 'react'
import { Button } from './ui/button'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select'
import { locales, localeNames, localeFlags, type Locale } from '@/lib/i18n'
import { Globe } from 'lucide-react'

interface LanguageSelectorProps {
    currentLocale: Locale
    onLocaleChange: (locale: Locale) => void
    variant?: 'select' | 'button'
}

export function LanguageSelector({ currentLocale, onLocaleChange, variant = 'select' }: LanguageSelectorProps) {
    if (variant === 'button') {
        return (
            <Button variant="ghost" size="sm" className="flex items-center space-x-2">
                <Globe className="w-4 h-4" />
                <span>{localeFlags[currentLocale]}</span>
                <span>{localeNames[currentLocale]}</span>
            </Button>
        )
    }

    return (
        <Select value={currentLocale} onValueChange={(value) => onLocaleChange(value as Locale)}>
            <SelectTrigger className="w-40">
                <div className="flex items-center space-x-2">
                    <Globe className="w-4 h-4" />
                    <span>{localeFlags[currentLocale]}</span>
                    <SelectValue />
                </div>
            </SelectTrigger>
            <SelectContent>
                {locales.map((locale) => (
                    <SelectItem key={locale} value={locale}>
                        <div className="flex items-center space-x-2">
                            <span>{localeFlags[locale]}</span>
                            <span>{localeNames[locale]}</span>
                        </div>
                    </SelectItem>
                ))}
            </SelectContent>
        </Select>
    )
}
