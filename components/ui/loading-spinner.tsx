// components/ui/LoadingSpinner.tsx
import React from 'react'

interface LoadingSpinnerProps {
    size?: 'sm' | 'lg'
    variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark'
    text?: string
    center?: boolean
}

export function LoadingSpinner({
                                   size,
                                   variant = 'primary',
                                   text,
                                   center = false
                               }: LoadingSpinnerProps) {
    const spinnerClasses = [
        'spinner-border',
        size && `spinner-border-${size}`,
        `text-${variant}`
    ].filter(Boolean).join(' ')

    const content = (
        <div className="d-flex align-items-center">
            <div className={spinnerClasses} role="status">
                <span className="visually-hidden">Loading...</span>
            </div>
            {text && <span className="ms-2">{text}</span>}
        </div>
    )

    return center ? (
        <div className="d-flex justify-content-center">
            {content}
        </div>
    ) : content
}