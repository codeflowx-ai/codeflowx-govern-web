// components/ui/Input.tsx
'use client'

import React from 'react'
import { cn } from '@/lib/utils'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label?: string
    error?: string
    icon?: React.ReactNode
    iconPosition?: 'left' | 'right'
}

export function Input({
                          label,
                          error,
                          icon,
                          iconPosition = 'left',
                          className = '',
                          ...props
                      }: InputProps) {
    const inputClasses = cn(
        'block w-full rounded-lg border border-gray-300 px-3 py-2 text-sm placeholder-gray-400 transition-colors',
        'focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500',
        'disabled:bg-gray-50 disabled:text-gray-500 disabled:cursor-not-allowed',
        error && 'border-red-300 focus:border-red-500 focus:ring-red-500',
        icon && iconPosition === 'left' && 'pl-10',
        icon && iconPosition === 'right' && 'pr-10',
        className
    )

    return (
        <div className="space-y-1">
            {label && (
                <label className="block text-sm font-medium text-gray-700">
                    {label}
                </label>
            )}
            <div className="relative">
                {icon && (
                    <div className={cn(
                        'absolute inset-y-0 flex items-center pointer-events-none text-gray-400',
                        iconPosition === 'left' ? 'left-3' : 'right-3'
                    )}>
                        {icon}
                    </div>
                )}
                <input className={inputClasses} {...props} />
            </div>
            {error && (
                <p className="text-sm text-red-600">{error}</p>
            )}
        </div>
    )
}