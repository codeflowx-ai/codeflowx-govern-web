// components/ui/Button.tsx
'use client'

import React from 'react'
import { cn } from '@/lib/utils'

interface ButtonProps {
    variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark' | 'outline' | 'ghost'
    size?: 'sm' | 'md' | 'lg' | 'icon' | 'default'
    children: React.ReactNode
    className?: string
    disabled?: boolean
    type?: 'button' | 'submit' | 'reset'
}

const baseClasses = 'inline-flex items-center justify-center font-medium rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed'

const variants = {
    primary: 'bg-primary-600 text-white hover:bg-primary-700 focus:ring-primary-500 shadow-sm hover:shadow-md',
    secondary: 'bg-gray-600 text-white hover:bg-gray-700 focus:ring-gray-500 shadow-sm hover:shadow-md',
    success: 'bg-green-600 text-white hover:bg-green-700 focus:ring-green-500 shadow-sm hover:shadow-md',
    danger: 'bg-red-600 text-white hover:bg-red-700 focus:ring-red-500 shadow-sm hover:shadow-md',
    warning: 'bg-yellow-600 text-white hover:bg-yellow-700 focus:ring-yellow-500 shadow-sm hover:shadow-md',
    info: 'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500 shadow-sm hover:shadow-md',
    light: 'bg-gray-100 text-gray-900 hover:bg-gray-200 focus:ring-gray-500',
    dark: 'bg-gray-900 text-white hover:bg-gray-800 focus:ring-gray-500',
    outline: 'border border-gray-300 text-gray-700 hover:bg-gray-50 focus:ring-primary-500',
    ghost: 'text-gray-700 hover:bg-gray-100 focus:ring-primary-500'
}

const sizes = {
    sm: 'px-3 py-1.5 text-sm',
    md: 'px-4 py-2 text-sm',
    lg: 'px-6 py-3 text-base',
    icon: 'h-9 w-9'
}

export function buttonVariants({
    variant = 'primary',
    size = 'md',
}: {
    variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark' | 'outline' | 'ghost'
    size?: 'sm' | 'md' | 'lg' | 'icon' | 'default'
} = {}) {
    const sizeClass = size === 'default' ? sizes.md : sizes[size] || sizes.md
    return cn(baseClasses, variants[variant], sizeClass)
}

export function Button({
    variant = 'primary',
    size = 'md',
    className = '',
    children,
    disabled = false,
    onClick,
    type = 'button',
    ...props
}: ButtonProps & React.ButtonHTMLAttributes<HTMLButtonElement>) {
    const sizeClass = size === 'default' ? sizes.md : sizes[size] || sizes.md
    return (
        <button
            type={type}
            className={cn(baseClasses, variants[variant], sizeClass, className)}
            disabled={disabled}
            onClick={onClick}
            {...props}
        >
            {children}
        </button>
    )
}
