// components/ui/Progress.tsx
'use client'

import React from 'react'

interface ProgressProps {
    value: number
    max?: number
    variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info'
    striped?: boolean
    animated?: boolean
    label?: string
    size?: 'sm' | 'lg'
    className?: string
}

export function Progress({
                             value,
                             max = 100,
                             variant = 'primary',
                             striped = false,
                             animated = false,
                             label,
                             size,
                             className = ''
                         }: ProgressProps) {
    const percentage = Math.min(Math.max((value / max) * 100, 0), 100)

    const progressClasses = [
        'progress-bar',
        `bg-${variant}`,
        striped && 'progress-bar-striped',
        animated && 'progress-bar-animated'
    ].filter(Boolean).join(' ')

    const containerClasses = [
        'progress',
        size && `progress-${size}`
    ].filter(Boolean).join(' ')

    const variantColors = {
        primary: 'bg-blue-600',
        secondary: 'bg-gray-600',
        success: 'bg-green-600',
        danger: 'bg-red-600',
        warning: 'bg-yellow-600',
        info: 'bg-blue-500'
    }

    return (
        <div className={className}>
            {label && (
                <div className="flex justify-between mb-1">
                    <span className="text-sm">{label}</span>
                    <span className="text-sm">{Math.round(percentage)}%</span>
                </div>
            )}
            <div className={`w-full bg-gray-200 rounded-full h-2 ${size === 'sm' ? 'h-1' : size === 'lg' ? 'h-3' : ''}`}>
                <div
                    className={`h-full rounded-full transition-all duration-300 ${variantColors[variant]} ${striped ? 'bg-striped' : ''} ${animated ? 'animate-pulse' : ''}`}
                    style={{ width: `${percentage}%` }}
                    role="progressbar"
                    aria-valuenow={value}
                    aria-valuemin={0}
                    aria-valuemax={max}
                >
                    {!label && (
                        <span className="sr-only">{Math.round(percentage)}%</span>
                    )}
                </div>
            </div>
        </div>
    )
}