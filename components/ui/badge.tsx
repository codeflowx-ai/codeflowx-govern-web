// components/ui/badge.tsx
'use client'

import React from 'react'
import { cn } from '@/lib/utils'

export interface BadgeProps {
    children: React.ReactNode
    variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark' | 'outline'
    size?: 'sm' | 'md' | 'lg'
    className?: string
}

export function Badge({
    children,
    variant = 'primary',
    size = 'md',
    className = ''
}: BadgeProps) {
    const baseClasses = 'inline-flex items-center font-medium rounded-full'

    const variants = {
        primary: 'bg-primary-100 text-primary-800',
        secondary: 'bg-gray-100 text-gray-800',
        success: 'bg-green-100 text-green-800',
        danger: 'bg-red-100 text-red-800',
        warning: 'bg-yellow-100 text-yellow-800',
        info: 'bg-blue-100 text-blue-800',
        light: 'bg-gray-50 text-gray-600',
        dark: 'bg-gray-800 text-gray-100',
        outline: 'border border-gray-300 text-gray-700 bg-transparent'
    }

    const sizes = {
        sm: 'px-2 py-0.5 text-xs',
        md: 'px-2.5 py-1 text-sm',
        lg: 'px-3 py-1.5 text-base'
    }

    return (
        <span className={cn(baseClasses, variants[variant], sizes[size], className)}>
            {children}
        </span>
    )
}

// Export default for compatibility
export default Badge;
