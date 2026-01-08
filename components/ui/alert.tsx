// components/ui/Alert.tsx
import React from 'react'

interface AlertProps {
    children: React.ReactNode
    variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark'
    className?: string
    dismissible?: boolean
    onClose?: () => void
}

interface AlertDescriptionProps {
    children: React.ReactNode
    className?: string
}

export function Alert({
                          children,
                          variant = 'info',
                          className = '',
                          dismissible = false,
                          onClose
                      }: AlertProps) {
    return (
        <div className={`alert alert-${variant} ${dismissible ? 'alert-dismissible' : ''} ${className}`} role="alert">
            {children}
            {dismissible && (
                <button
                    type="button"
                    className="btn-close"
                    onClick={onClose}
                    aria-label="Close"
                ></button>
            )}
        </div>
    )
}

export function AlertDescription({ children, className = '' }: AlertDescriptionProps) {
    return (
        <div className={className}>
            {children}
        </div>
    )
}