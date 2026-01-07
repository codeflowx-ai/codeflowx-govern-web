'use client'

import { Loader2 } from 'lucide-react'

interface LoadingPageProps {
    message?: string
    size?: 'sm' | 'md' | 'lg'
}

export function LoadingPage({ message = 'Cargando...', size = 'md' }: LoadingPageProps) {
    const sizeClasses = {
        sm: 'w-6 h-6',
        md: 'w-8 h-8',
        lg: 'w-12 h-12'
    }

    return (
        <div 
            className="min-h-screen flex items-center justify-center"
            style={{
                backgroundColor: 'var(--theme-background)',
                color: 'var(--theme-text)'
            }}
        >
            <div className="text-center space-y-4">
                <div className="flex justify-center">
                    <Loader2 
                        className={`${sizeClasses[size]} animate-spin`}
                        style={{ color: 'var(--theme-primary)' }}
                    />
                </div>
                <div 
                    className="text-lg font-medium"
                    style={{ color: 'var(--theme-text)' }}
                >
                    {message}
                </div>
                <div 
                    className="text-sm"
                    style={{ color: 'var(--theme-textSecondary)' }}
                >
                    Por favor, espera un momento...
                </div>
            </div>
        </div>
    )
}
