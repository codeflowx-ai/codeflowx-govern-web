// app/error.tsx
'use client'

import { useEffect } from 'react'

export default function Error({
                                  error,
                                  reset,
                              }: {
    error: Error & { digest?: string }
    reset: () => void
}) {
    const isDevelopment = process.env.NODE_ENV === 'development'

    useEffect(() => {
        // Log del error en desarrollo
        if (isDevelopment) {
            console.error('Application Error:', error)
        }
    }, [error, isDevelopment])

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
            <div className="max-w-md w-full">
                <div className="text-center">
                    <div className="text-6xl mb-4">😵</div>
                    <h1 className="text-2xl font-bold text-gray-900 mb-2">
                        Oops! Something went wrong
                    </h1>
                    <p className="text-gray-600 mb-6">
                        We're sorry, but something unexpected happened. Our team has been notified.
                    </p>

                    {/* Mostrar detalles solo en desarrollo */}
                    {isDevelopment && (
                        <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6 text-left">
                            <h3 className="font-medium text-red-900 mb-2">Development Error Details:</h3>
                            <div className="text-sm text-red-700 font-mono bg-red-100 p-2 rounded overflow-auto max-h-32">
                                {error.message}
                            </div>
                            {error.digest && (
                                <p className="text-xs text-red-600 mt-2">Error ID: {error.digest}</p>
                            )}
                        </div>
                    )}

                    <div className="space-y-3">
                        <button
                            onClick={reset}
                            className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
                        >
                            🔄 Try Again
                        </button>

                        <button
                            onClick={() => window.location.href = '/dashboard'}
                            className="w-full px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 font-medium"
                        >
                            🏠 Go to Dashboard
                        </button>

                        <button
                            onClick={() => window.location.href = '/'}
                            className="w-full px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 font-medium"
                        >
                            🔐 Back to Login
                        </button>
                    </div>

                    {!isDevelopment && (
                        <div className="mt-6 text-sm text-gray-500">
                            <p>If this problem persists, please contact support.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}
